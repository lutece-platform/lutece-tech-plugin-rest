/*
 * Copyright (c) 2002-2007, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.ods.service.search;

import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierFilter;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysique;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysiqueHome;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.pdd.PDDFilter;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementFilter;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementHome;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.search.SearchIndexer;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import org.pdfbox.exceptions.CryptographyException;
import org.pdfbox.exceptions.InvalidPasswordException;

import org.pdfbox.pdmodel.PDDocument;

import org.pdfbox.util.PDFTextStripper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.List;


/**
 * ODSSearchIndexer : permet de récupérer les documents pour indexer/supprimer dans/de l'index
 */
public class ODSSearchIndexer implements SearchIndexer
{
    private static final String PLUGIN_ODS_NAME = "ods";
    private static final String TYPE_TITRE = "titre";
    private static final String TYPE_PDD_OBJECT = "objet_pdd";
    private static final String TYPE_VA_OBJECT = "objet_va";
    private static final String TYPE_PDD_REFERENCE = "reference_pdd";
    private static final String TYPE_VA_REFERENCE = "reference_va";
    private static final String PROPERTY_INDEXER_NAME = "odssearchindexer.name";
    private static final String INDEXER_DESCRIPTION = "ods.odssearchindexer.description";
    private static final String INDEXER_VERSION = "1.0.0";
    private static final String PROPERTY_INDEXER_ENABLE = "ods.odssearchindexer.enable";
    private static final String FIELD_TYPE_OBJET = "type_objet";
    private static final String FIELD_ID_OBJET = "id_objet";
    private static final String FIELD_CONTENT = "contents";
    private boolean _bArchiveIndexer;

    /**
     * Constructeur: récupère tous les documents à indexer, soit pour les archives soit pour la prochaine séance
     * @param bArchiveIndexer le SearchIndexer agit sur les archives ou la prochaine séance
     */
    public ODSSearchIndexer( boolean bArchiveIndexer )
    {
        _bArchiveIndexer = bArchiveIndexer;
    }

    /**
     * Retourne tous les documents à indexer, autrement dit la liste des documents pour l'initialisation de l'index<br/>
     * Par défaut cette méthode renvoie les documents de la prochaine séance du conseil
     * @see fr.paris.lutece.portal.service.search.SearchIndexer
     * @return List<Document> liste de documents
     * @throws IOException exception héritée de fr.paris.lutece.portal.service.search.SearchIndexer
     * @throws InterruptedException exception héritée de fr.paris.lutece.portal.service.search.SearchIndexer
     */
    public List<Document> getDocuments(  ) throws IOException, InterruptedException
    {
        Plugin plugin = PluginService.getPlugin( PLUGIN_ODS_NAME );
        int nIdSeance = SeanceHome.getProchaineSeance( plugin ).getIdSeance(  );

        return getDocuments( nIdSeance );
    }

    /**
     * Retourne tous les documents à indexer pour une séance
     * @param nIdSeance l'id de la séance voulue
     * @return List<Document> liste de documents
     */
    public List<Document> getDocuments( int nIdSeance )
    {
        List<Document> listDocs = new ArrayList<Document>(  );

        try
        {
            listDocs.addAll( getAllDocumentsFichiers( nIdSeance ) );
            listDocs.addAll( getAllDocumentsPDDs( nIdSeance ) );
            listDocs.addAll( getAllDocumentsVoeuAmendement( nIdSeance ) );
        }
        catch ( NullPointerException npe )
        {
            AppLogService.error( npe );
        }

        return listDocs;
    }

    /**
     * Retourne la liste des documents Lucene créés à partir des fichiers de la base pour une séance donnée
     * @param nIdSeance la séance voulue
     * @return la liste des documents Lucene créés à partir des fichiers de la base
     */
    private List<Document> getAllDocumentsFichiers( int nIdSeance )
    {
        Document doc;
        Plugin plugin = PluginService.getPlugin( PLUGIN_ODS_NAME );
        List<Document> listDocs = new ArrayList<Document>(  );
        List<Fichier> listFichiers = new ArrayList<Fichier>(  );

        // initialisation de la liste des fichiers
        FichierFilter fichierFilter = new FichierFilter(  );
        fichierFilter.setIdSeance( nIdSeance );
        listFichiers.addAll( FichierHome.findByFilter( fichierFilter, plugin ) );
        fichierFilter.setIdSeance( FichierFilter.ALL_INT );

        PDDFilter pddFilter = new PDDFilter(  );

        if ( _bArchiveIndexer )
        {
            pddFilter.setSeance( nIdSeance );
        }
        else
        {
            pddFilter.setProchaineSeance( nIdSeance );
        }

        List<PDD> listPDD = PDDHome.findByFilter( pddFilter, plugin );

        for ( PDD pdd : listPDD )
        {
            fichierFilter.setPDD( pdd.getIdPdd(  ) );
            listFichiers.addAll( FichierHome.findByFilter( fichierFilter, plugin ) );
        }

        for ( Fichier fichier : listFichiers )
        {
            if ( OdsConstants.CONSTANTE_PDF.equals( fichier.getExtension(  ) ) )
            {
                doc = new Document(  );

                String strContenu = OdsConstants.CONSTANTE_CHAINE_VIDE;
                FichierPhysique fichierPhysique = FichierPhysiqueHome.findByPrimaryKey( fichier.getFichier(  )
                                                                                               .getIdFichier(  ),
                        PluginService.getPlugin( PLUGIN_ODS_NAME ) );

                strContenu += getContent( fichierPhysique.getDonnees(  ), fichier.getId(  ),
                    fichierPhysique.getIdFichier(  ) );
                strContenu += ( ( fichier.getTexte(  ) != null ) ? fichier.getTexte(  )
                                                                 : OdsConstants.CONSTANTE_CHAINE_VIDE );

                if ( !strContenu.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    doc.add( new Field( FIELD_ID_OBJET, Fichier.MARK_FICHIER + " " + fichier.getId(  ),
                            Field.Store.YES, Field.Index.UN_TOKENIZED ) );
                    doc.add( new Field( FIELD_TYPE_OBJET, Fichier.MARK_FICHIER, Field.Store.YES, Field.Index.TOKENIZED ) );
                    doc.add( new Field( FIELD_CONTENT, strContenu, Field.Store.YES, Field.Index.TOKENIZED ) );
                    listDocs.add( doc );
                }

                strContenu += ( ( fichier.getTexte(  ) != null ) ? fichier.getTexte(  )
                                                                 : OdsConstants.CONSTANTE_CHAINE_VIDE );
                doc = new Document(  );
                doc.add( new Field( FIELD_ID_OBJET, TYPE_TITRE + " " + fichier.getId(  ), Field.Store.YES,
                        Field.Index.UN_TOKENIZED ) );
                doc.add( new Field( FIELD_TYPE_OBJET, TYPE_TITRE, Field.Store.YES, Field.Index.TOKENIZED ) );
                doc.add( new Field( FIELD_CONTENT, fichier.getTitre(  ), Field.Store.YES, Field.Index.TOKENIZED ) );
                listDocs.add( doc );
            }
        }

        return listDocs;
    }

    /**
     * Retourne la liste des documents Lucene créés à partir des PDD de la base
     * @param nIdSeance la séance
     * @return la liste des documents Lucene créés à partir des PDD de la base
     */
    private List<Document> getAllDocumentsPDDs( int nIdSeance )
    {
        Document doc;
        Plugin plugin = PluginService.getPlugin( PLUGIN_ODS_NAME );
        List<Document> listDocs = new ArrayList<Document>(  );
        List<PDD> listPdds;

        // initialisation de la liste des PDD
        PDDFilter filter = new PDDFilter(  );

        if ( _bArchiveIndexer )
        {
            filter.setSeance( nIdSeance );
        }
        else
        {
            filter.setProchaineSeance( nIdSeance );
        }

        listPdds = PDDHome.findByFilter( filter, plugin );

        for ( PDD pdd : listPdds )
        {
            doc = new Document(  );
            doc.add( new Field( FIELD_ID_OBJET, TYPE_PDD_OBJECT + " " + pdd.getIdPdd(  ), Field.Store.YES,
                    Field.Index.UN_TOKENIZED ) );
            doc.add( new Field( FIELD_TYPE_OBJET, TYPE_PDD_OBJECT, Field.Store.YES, Field.Index.TOKENIZED ) );
            doc.add( new Field( FIELD_CONTENT, pdd.getObjet(  ), Field.Store.YES, Field.Index.TOKENIZED ) );
            listDocs.add( doc );

            if ( pdd.getReference(  ) != null )
            {
                doc = new Document(  );
                doc.add( new Field( FIELD_ID_OBJET, TYPE_PDD_REFERENCE + " " + pdd.getIdPdd(  ), Field.Store.YES,
                        Field.Index.UN_TOKENIZED ) );
                doc.add( new Field( FIELD_TYPE_OBJET, TYPE_PDD_REFERENCE, Field.Store.YES, Field.Index.TOKENIZED ) );
                doc.add( new Field( FIELD_CONTENT, pdd.getReference(  ), Field.Store.YES, Field.Index.TOKENIZED ) );
                listDocs.add( doc );
            }
        }

        return listDocs;
    }

    /**
     * Retourne la liste des documents Lucene créé à partir des VoeuAmendement de la base
     * @param nIdSeance la séance
     * @return la liste des documents Lucene créé à partir des VoeuAmendement de la base
     */
    private List<Document> getAllDocumentsVoeuAmendement( int nIdSeance )
    {
        Document doc;
        Plugin plugin = PluginService.getPlugin( PLUGIN_ODS_NAME );
        List<Document> listDocs = new ArrayList<Document>(  );

        List<VoeuAmendement> listVoeuAmendements;
        VoeuAmendementFilter filter = new VoeuAmendementFilter(  );
        filter.setIdSeance( nIdSeance );
        listVoeuAmendements = VoeuAmendementHome.findVoeuAmendementListByFilter( filter, plugin );

        for ( VoeuAmendement va : listVoeuAmendements )
        {
            doc = new Document(  );
            doc.add( new Field( FIELD_ID_OBJET, TYPE_VA_OBJECT + " " + va.getIdVoeuAmendement(  ), Field.Store.YES,
                    Field.Index.UN_TOKENIZED ) );
            doc.add( new Field( FIELD_TYPE_OBJET, TYPE_VA_OBJECT, Field.Store.YES, Field.Index.TOKENIZED ) );
            doc.add( new Field( FIELD_CONTENT, va.getObjet(  ), Field.Store.YES, Field.Index.TOKENIZED ) );
            listDocs.add( doc );

            doc = new Document(  );
            doc.add( new Field( FIELD_ID_OBJET, TYPE_VA_REFERENCE + " " + va.getIdVoeuAmendement(  ), Field.Store.YES,
                    Field.Index.UN_TOKENIZED ) );
            doc.add( new Field( FIELD_TYPE_OBJET, TYPE_VA_REFERENCE, Field.Store.YES, Field.Index.TOKENIZED ) );
            doc.add( new Field( FIELD_CONTENT, va.getReferenceComplete(  ), Field.Store.YES, Field.Index.TOKENIZED ) );
            listDocs.add( doc );
        }

        return listDocs;
    }

    /**
     * Retourne tous les documents si <b>increment</b> vaut <b>false</b>;;
     * uniquement les documents ajoutés depuis la dernière indexation sinon
     * @param increment indexation incrémentale ou non
     * @return une liste de documents
     * @throws InterruptedException héritée de getDocuments(  )
     * @throws IOException héritée de getDocuments(  )
     */
    public List<Document> getDocuments( boolean increment )
        throws IOException, InterruptedException
    {
        if ( increment )
        {
            return getDocumentsToIndex(  );
        }

        return getDocuments(  );
    }

    /**
     * Retourne la liste des documents supprimés ou modifiés à supprimer de l'index
     * @return une liste de documents
     */
    public List<Document> getDocumentsToRemove(  )
    {
        ODSSearchService searchService = new ODSSearchService(  );
        List<Document> listDocs = new ArrayList<Document>(  );
        Document doc;
        List<Object> listObjets = searchService.getDeletedObjects( _bArchiveIndexer,
                PluginService.getPlugin( PLUGIN_ODS_NAME ) );
        listObjets.addAll( searchService.getUpdatedObjects( false, _bArchiveIndexer,
                PluginService.getPlugin( PLUGIN_ODS_NAME ) ) );

        for ( Object object : listObjets )
        {
            if ( object instanceof Fichier )
            {
                doc = new Document(  );
                doc.add( new Field( FIELD_ID_OBJET, Fichier.MARK_FICHIER + " " + ( (Fichier) object ).getId(  ),
                        Field.Store.YES, Field.Index.UN_TOKENIZED ) );
                listDocs.add( doc );

                doc = new Document(  );
                doc.add( new Field( FIELD_ID_OBJET, TYPE_TITRE + " " + ( (Fichier) object ).getId(  ), Field.Store.YES,
                        Field.Index.UN_TOKENIZED ) );
                listDocs.add( doc );
            }
            else if ( object instanceof PDD )
            {
                doc = new Document(  );
                doc.add( new Field( FIELD_ID_OBJET, TYPE_PDD_OBJECT + " " + ( (PDD) object ).getIdPdd(  ),
                        Field.Store.YES, Field.Index.UN_TOKENIZED ) );
                listDocs.add( doc );

                doc = new Document(  );
                doc.add( new Field( FIELD_ID_OBJET, TYPE_PDD_REFERENCE + " " + ( (PDD) object ).getIdPdd(  ),
                        Field.Store.YES, Field.Index.UN_TOKENIZED ) );
                listDocs.add( doc );
            }
            else if ( object instanceof VoeuAmendement )
            {
                doc = new Document(  );
                doc.add( new Field( FIELD_ID_OBJET,
                        TYPE_VA_OBJECT + " " + ( (VoeuAmendement) object ).getIdVoeuAmendement(  ), Field.Store.YES,
                        Field.Index.UN_TOKENIZED ) );
                listDocs.add( doc );

                doc = new Document(  );
                doc.add( new Field( FIELD_ID_OBJET,
                        TYPE_VA_REFERENCE + " " + ( (VoeuAmendement) object ).getIdVoeuAmendement(  ), Field.Store.YES,
                        Field.Index.UN_TOKENIZED ) );
                listDocs.add( doc );
            }
        }

        return listDocs;
    }

    /**
     * Retourne la liste des documents ajoutés ou modifiés à ajouter dans l'index
     * @return une liste de documents
     */
    public List<Document> getDocumentsToIndex(  )
    {
        ODSSearchService searchService = new ODSSearchService(  );
        List<Document> listDocs = new ArrayList<Document>(  );
        Document doc;
        List<Object> listObjets = searchService.getAddedObjects( _bArchiveIndexer,
                PluginService.getPlugin( PLUGIN_ODS_NAME ) );
        listObjets.addAll( searchService.getUpdatedObjects( true, _bArchiveIndexer,
                PluginService.getPlugin( PLUGIN_ODS_NAME ) ) );

        // Pour chaque objet on ajoute 2 "Documents", selon le type
        for ( Object object : listObjets )
        {
            String strContenu;

            if ( object instanceof Fichier )
            {
                doc = new Document(  );

                strContenu = OdsConstants.CONSTANTE_CHAINE_VIDE;

                FichierPhysique fichier = FichierPhysiqueHome.findByPrimaryKey( ( (Fichier) object ).getFichier(  )
                                                                                  .getIdFichier(  ),
                        PluginService.getPlugin( PLUGIN_ODS_NAME ) );

                try
                {
                    strContenu += getContent( fichier.getDonnees(  ), ( (Fichier) object ).getId(  ),
                        fichier.getIdFichier(  ) );
                }
                catch ( Exception e )
                {
                    AppLogService.error( "Error getting content from file: id = " + fichier.getIdFichier(  ) );
                }

                strContenu += ( ( ( (Fichier) object ).getTexte(  ) != null ) ? ( (Fichier) object ).getTexte(  )
                                                                              : OdsConstants.CONSTANTE_CHAINE_VIDE );

                if ( !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strContenu ) )
                {
                    doc.add( new Field( FIELD_ID_OBJET, Fichier.MARK_FICHIER + " " + ( (Fichier) object ).getId(  ),
                            Field.Store.YES, Field.Index.UN_TOKENIZED ) );
                    doc.add( new Field( FIELD_TYPE_OBJET, Fichier.MARK_FICHIER, Field.Store.YES, Field.Index.TOKENIZED ) );
                    doc.add( new Field( FIELD_CONTENT, strContenu, Field.Store.YES, Field.Index.TOKENIZED ) );
                    listDocs.add( doc );
                }

                if ( ( ( (Fichier) object ).getTitre(  ) != null ) &&
                        !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( ( (Fichier) object ).getTitre(  ) ) )
                {
                    doc = new Document(  );
                    doc.add( new Field( FIELD_ID_OBJET, TYPE_TITRE + " " + ( (Fichier) object ).getId(  ),
                            Field.Store.YES, Field.Index.UN_TOKENIZED ) );
                    doc.add( new Field( FIELD_TYPE_OBJET, TYPE_TITRE, Field.Store.YES, Field.Index.TOKENIZED ) );
                    doc.add( new Field( FIELD_CONTENT, ( (Fichier) object ).getTitre(  ), Field.Store.YES,
                            Field.Index.TOKENIZED ) );
                    listDocs.add( doc );
                }
            }
            else if ( object instanceof PDD )
            {
                if ( ( ( (PDD) object ).getObjet(  ) != null ) &&
                        !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( ( (PDD) object ).getObjet(  ) ) )
                {
                    doc = new Document(  );
                    doc.add( new Field( FIELD_ID_OBJET, TYPE_PDD_OBJECT + " " + ( (PDD) object ).getIdPdd(  ),
                            Field.Store.YES, Field.Index.UN_TOKENIZED ) );
                    doc.add( new Field( FIELD_TYPE_OBJET, TYPE_PDD_OBJECT, Field.Store.YES, Field.Index.TOKENIZED ) );
                    doc.add( new Field( FIELD_CONTENT, ( (PDD) object ).getObjet(  ), Field.Store.YES,
                            Field.Index.TOKENIZED ) );
                    listDocs.add( doc );
                }

                if ( ( ( (PDD) object ).getReference(  ) != null ) &&
                        !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( ( (PDD) object ).getReference(  ) ) )
                {
                    doc = new Document(  );
                    doc.add( new Field( FIELD_ID_OBJET, TYPE_PDD_REFERENCE + " " + ( (PDD) object ).getIdPdd(  ),
                            Field.Store.YES, Field.Index.UN_TOKENIZED ) );
                    doc.add( new Field( FIELD_TYPE_OBJET, TYPE_PDD_REFERENCE, Field.Store.YES, Field.Index.TOKENIZED ) );
                    doc.add( new Field( FIELD_CONTENT, ( (PDD) object ).getReference(  ), Field.Store.YES,
                            Field.Index.TOKENIZED ) );
                    listDocs.add( doc );
                }
            }
            else if ( object instanceof VoeuAmendement )
            {
                if ( ( ( (VoeuAmendement) object ).getObjet(  ) != null ) &&
                        !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( ( (VoeuAmendement) object ).getObjet(  ) ) )
                {
                    doc = new Document(  );
                    doc.add( new Field( FIELD_ID_OBJET,
                            TYPE_VA_OBJECT + " " + ( (VoeuAmendement) object ).getIdVoeuAmendement(  ),
                            Field.Store.YES, Field.Index.UN_TOKENIZED ) );
                    doc.add( new Field( FIELD_TYPE_OBJET, TYPE_VA_OBJECT, Field.Store.YES, Field.Index.TOKENIZED ) );
                    doc.add( new Field( FIELD_CONTENT, ( (VoeuAmendement) object ).getObjet(  ), Field.Store.YES,
                            Field.Index.TOKENIZED ) );
                    listDocs.add( doc );
                }

                if ( ( ( (VoeuAmendement) object ).getReferenceComplete(  ) != null ) &&
                        !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( 
                            ( (VoeuAmendement) object ).getReferenceComplete(  ) ) )
                {
                    doc = new Document(  );
                    doc.add( new Field( FIELD_ID_OBJET,
                            TYPE_VA_REFERENCE + " " + ( (VoeuAmendement) object ).getIdVoeuAmendement(  ),
                            Field.Store.YES, Field.Index.UN_TOKENIZED ) );
                    doc.add( new Field( FIELD_TYPE_OBJET, TYPE_VA_REFERENCE, Field.Store.YES, Field.Index.TOKENIZED ) );
                    doc.add( new Field( FIELD_CONTENT, ( (VoeuAmendement) object ).getReferenceComplete(  ),
                            Field.Store.YES, Field.Index.TOKENIZED ) );
                    listDocs.add( doc );
                }
            }
        }

        return listDocs;
    }

    /**
     * Retourne le contenu du fichier PDF
     * @param byteArray le contenu du fichier PDF
     * @param nIdFichier l'identifiant du fichier
     * @param nIdFichierPhysique l'identifiant du fichier physique
     * @return le contenu du fichier
     */
    private String getContent( byte[] byteArray, int nIdFichier, int nIdFichierPhysique )
    {
        String strContent = OdsConstants.CONSTANTE_CHAINE_VIDE;
        PDDocument pdfDocument = null;

        try
        {
            pdfDocument = PDDocument.load( new ByteArrayInputStream( byteArray ) );

            if ( pdfDocument.isEncrypted(  ) )
            {
                pdfDocument.decrypt( OdsConstants.CONSTANTE_CHAINE_VIDE );
            }

            StringWriter writer = new StringWriter(  );
            PDFTextStripper stripper = new PDFTextStripper(  );
            stripper.writeText( pdfDocument, writer );
            strContent = writer.getBuffer(  ).toString(  );
        }
        catch ( CryptographyException e )
        {
            AppLogService.error( "Erreur récupération pdfDocument:\nfichier.id = " + nIdFichier +
                "\nfichierPhysique.id = " + nIdFichierPhysique + "\nErreur: " + e.getClass(  ) );
        }
        catch ( IOException e )
        {
            AppLogService.error( "Erreur récupération pdfDocument:\nfichier.id = " + nIdFichier +
                "\nfichierPhysique.id = " + nIdFichierPhysique + "\nErreur: " + e.getClass(  ) );
        }
        catch ( InvalidPasswordException e )
        {
            AppLogService.error( "Erreur récupération pdfDocument:\nfichier.id = " + nIdFichier +
                "\nfichierPhysique.id = " + nIdFichierPhysique + "\nErreur: " + e.getClass(  ) );
        }
        finally
        {
            if ( pdfDocument != null )
            {
                try
                {
                    pdfDocument.close(  );
                }
                catch ( IOException e )
                {
                    AppLogService.error( "Erreur fermeture pdfDocument:\nfichier.id = " + nIdFichier +
                        "\nfichierPhysique.id = " + nIdFichierPhysique + "\nErreur: " + e.getClass(  ) );
                }
            }
        }

        return strContent;
    }

    /**
     * Retourne la description de l'indexer
     * @return la description
     */
    public String getDescription(  )
    {
        return INDEXER_DESCRIPTION;
    }

    /**
     * Retourne le nom de l'indexer
     * @return le nom de l'indexer
     */
    public String getName(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_INDEXER_NAME );
    }

    /**
     * Retourne la version de l'indexer
     * @return la version de l'indexer
     */
    public String getVersion(  )
    {
        return INDEXER_VERSION;
    }

    /**
     * Indique si l'indexer est actif ou non
     * @return <b>true</b> si l'indexer est actif, <b>false</b> sinon
     */
    public boolean isEnable(  )
    {
        String strEnable = AppPropertiesService.getProperty( PROPERTY_INDEXER_ENABLE, "true" );

        return ( strEnable.equalsIgnoreCase( "true" ) );
    }
}
