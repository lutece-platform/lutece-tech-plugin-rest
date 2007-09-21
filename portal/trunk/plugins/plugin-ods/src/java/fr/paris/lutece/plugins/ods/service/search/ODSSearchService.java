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
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocument;
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocumentEnum;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.pdd.PDDFilter;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementHome;
import fr.paris.lutece.plugins.ods.service.search.action.IndexerAction;
import fr.paris.lutece.plugins.ods.service.search.action.IndexerActionHome;
import fr.paris.lutece.plugins.ods.service.search.indexer.Indexer;
import fr.paris.lutece.plugins.ods.service.search.indexer.IndexerHome;
import fr.paris.lutece.plugins.ods.service.search.requete.RequeteUtilisateur;
import fr.paris.lutece.plugins.ods.service.search.requete.ResultatRequete;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * ODSSearchService
 */
public class ODSSearchService
{
    private static final String PROPERTY_INDEXER_NAME = "odssearchindexer.name";
    private static final String PARAM_PROJET = "projet";
    private static final String PARAM_PROPOSITION = "proposition";
    private static final String PARAM_AMENDEMENT = "amendement";
    private static final String PARAM_VOEU = "voeu";
    private static final String PARAM_SEANCE = "seance";
    private static final String PARAM_AUTRES = "autres";

    //////////////////////////////////////////////////////////////////////////////////////////
    //																						//
    //								Fonctions d'indexation									//
    //																						//
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Pour chaque indexer, cette méthode insère une nouvelle action de création dans l'index
     * @param object l'objet à indexer
     * @param bArchive indique dans quel index il faut ajouter l'objet
     * @param plugin le plugin
     */
    public static void addObjectToIndex( Object object, boolean bArchive, Plugin plugin )
    {
        // 3 types d'objet possibles: Fichier, VoeuAmendement ou PDD
        List<Indexer> listIndexers = IndexerHome.findAllIndexers( plugin );
        IndexerAction action = new IndexerAction(  );

        if ( object instanceof Fichier )
        {
            action.setType( IndexerAction.TYPE_FICHIER );
            action.setIdObjet( ( (Fichier) object ).getId(  ) );
        }
        else if ( object instanceof PDD )
        {
            action.setType( IndexerAction.TYPE_PDD );
            action.setIdObjet( ( (PDD) object ).getIdPdd(  ) );
        }
        else if ( object instanceof VoeuAmendement )
        {
            action.setType( IndexerAction.TYPE_VA );
            action.setIdObjet( ( (VoeuAmendement) object ).getIdVoeuAmendement(  ) );
        }

        action.setCodeTask( IndexerAction.ACTION_CREATE );
        action.setArchive( bArchive );

        for ( Indexer indexer : listIndexers )
        {
            action.setIdIndexer( indexer.getIdIndexer(  ) );
            IndexerActionHome.create( action, plugin );
        }
    }

    /**
     * Pour chaque indexer, cette méthode insère une nouvelle action de modification de l'index
     * @param object l'objet à modifier dans l'index
     * @param bArchive indique dans quel index il faut modifier l'objet
     * @param plugin le plugin
     */
    public static void updateObjectInIndex( Object object, boolean bArchive, Plugin plugin )
    {
        List<Indexer> listIndexers = IndexerHome.findAllIndexers( plugin );
        IndexerAction action = new IndexerAction(  );

        for ( Indexer indexer : listIndexers )
        {
            // si l'action d'ajout a été effectué par l'indexer ( = null),
            // demander la modif; sinon ne rien faire, les infos seront directement les bonnes
            if ( ( IndexerActionHome.findActionOnObject( indexer.getIdIndexer(  ), IndexerAction.ACTION_CREATE, object,
                        plugin ) == null ) &&
                    ( IndexerActionHome.findActionOnObject( indexer.getIdIndexer(  ), IndexerAction.ACTION_MODIFY,
                        object, plugin ) == null ) )
            {
                if ( object instanceof Fichier )
                {
                    action.setType( IndexerAction.TYPE_FICHIER );
                    action.setIdObjet( ( (Fichier) object ).getId(  ) );
                }
                else if ( object instanceof PDD )
                {
                    action.setType( IndexerAction.TYPE_PDD );
                    action.setIdObjet( ( (PDD) object ).getIdPdd(  ) );
                }
                else if ( object instanceof VoeuAmendement )
                {
                    action.setType( IndexerAction.TYPE_VA );
                    action.setIdObjet( ( (VoeuAmendement) object ).getIdVoeuAmendement(  ) );
                }

                action.setIdIndexer( indexer.getIdIndexer(  ) );
                action.setCodeTask( IndexerAction.ACTION_MODIFY );
                action.setArchive( bArchive );
                IndexerActionHome.create( action, plugin );
            }
        }
    }

    /**
     * Pour chaque indexer, cette méthode insère une nouvelle action de suppression dans l'index
     * @param object l'objet à supprimer de l'index
     * @param bArchive indique dans quel index il faut supprimer l'objet
     * @param plugin plugin
     */
    public static void deleteObjectFromIndex( Object object, boolean bArchive, Plugin plugin )
    {
        List<Indexer> listIndexers = IndexerHome.findAllIndexers( plugin );
        IndexerAction action = new IndexerAction(  );

        for ( Indexer indexer : listIndexers )
        {
            // si l'action d'ajout a été effectué par l'indexer ( = null),
            // demander la suppression; sinon annuler l'ajout
            if ( IndexerActionHome.findActionOnObject( indexer.getIdIndexer(  ), IndexerAction.ACTION_CREATE, object,
                        plugin ) == null )
            {
                if ( object instanceof Fichier )
                {
                    action.setType( IndexerAction.TYPE_FICHIER );
                    action.setIdObjet( ( (Fichier) object ).getId(  ) );
                }
                else if ( object instanceof PDD )
                {
                    action.setType( IndexerAction.TYPE_PDD );
                    action.setIdObjet( ( (PDD) object ).getIdPdd(  ) );
                }
                else if ( object instanceof VoeuAmendement )
                {
                    action.setType( IndexerAction.TYPE_VA );
                    action.setIdObjet( ( (VoeuAmendement) object ).getIdVoeuAmendement(  ) );
                }

                action.setIdIndexer( indexer.getIdIndexer(  ) );
                action.setCodeTask( IndexerAction.ACTION_DELETE );
                action.setArchive( bArchive );
                IndexerActionHome.create( action, plugin );
            }
            else
            {
                IndexerActionHome.remove( IndexerActionHome.findActionOnObject( indexer.getIdIndexer(  ),
                        IndexerAction.ACTION_CREATE, object, plugin ), plugin );
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //																						//
    //						 Fonctions pour l'indexation incrémentale						//
    //																						//
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Retourne la liste des objets ajoutés depuis la dernière indexation
     * @param bArchive indique sur quel index opérer (archive ou prochaine séance)
     * @param plugin le plugin
     * @return la liste de fichiers ajoutés depuis la dernière indexation
     * @throws  AppException  AppException
     */
    public List<Object> getAddedObjects( boolean bArchive, Plugin plugin )
        throws AppException
    {
        String strNomIndexer = AppPropertiesService.getProperty( PROPERTY_INDEXER_NAME );

        int nIdIndexer = IndexerHome.findByName( strNomIndexer, plugin ).getIdIndexer(  );

        List<Object> listObjets = new ArrayList<Object>(  );

        List<IndexerAction> indexerActions = IndexerActionHome.findActionsByIndexer( nIdIndexer,
                IndexerAction.ACTION_CREATE, true, bArchive, plugin );

        int nIdObjet;

        for ( IndexerAction action : indexerActions )
        {
            nIdObjet = action.getIdObjet(  );

            if ( action.getType(  ) == IndexerAction.TYPE_FICHIER )
            {
                listObjets.add( FichierHome.findByPrimaryKey( nIdObjet, plugin ) );
            }
            else if ( action.getType(  ) == IndexerAction.TYPE_PDD )
            {
                listObjets.add( PDDHome.findByPrimaryKey( nIdObjet, plugin ) );
            }
            else if ( action.getType(  ) == IndexerAction.TYPE_VA )
            {
                listObjets.add( VoeuAmendementHome.findByPrimaryKey( nIdObjet, plugin ) );
            }
        }

        return listObjets;
    }

    /**
     * Retourne la liste des objets modifiés depuis la dernière indexation
     * @param bDelete indique si l'on doit supprimer l'action après l'avoir lue
     * @param bArchive indique sur quel index opérer (archive ou prochaine séance)
     * @param plugin le plugin
     * @return la liste des objets modifiés depuis la dernière indexation
     */
    public List<Object> getUpdatedObjects( boolean bDelete, boolean bArchive, Plugin plugin )
        throws AppException
    {
        String strNomIndexer = AppPropertiesService.getProperty( PROPERTY_INDEXER_NAME );

        int nIdIndexer = IndexerHome.findByName( strNomIndexer, plugin ).getIdIndexer(  );

        List<Object> listObjets = new ArrayList<Object>(  );

        List<IndexerAction> indexerActions = IndexerActionHome.findActionsByIndexer( nIdIndexer,
                IndexerAction.ACTION_MODIFY, bDelete, bArchive, plugin );

        int nIdObjet;

        for ( IndexerAction action : indexerActions )
        {
            nIdObjet = action.getIdObjet(  );

            if ( action.getType(  ) == IndexerAction.TYPE_FICHIER )
            {
                listObjets.add( FichierHome.findByPrimaryKey( nIdObjet, plugin ) );
            }
            else if ( action.getType(  ) == IndexerAction.TYPE_PDD )
            {
                listObjets.add( PDDHome.findByPrimaryKey( nIdObjet, plugin ) );
            }
            else if ( action.getType(  ) == IndexerAction.TYPE_VA )
            {
                listObjets.add( VoeuAmendementHome.findByPrimaryKey( nIdObjet, plugin ) );
            }
        }

        return listObjets;
    }

    /**
     * Retourne la liste des objets supprimés depuis la dernière indexation
     * @param bArchive indique sur quel index opérer (archive ou prochaine séance)
     * @param plugin le plugin
     * @return la liste des objets supprimés depuis la dernière indexation
     */
    public List<Object> getDeletedObjects( boolean bArchive, Plugin plugin )
        throws AppException
    {
        String strNomIndexer = AppPropertiesService.getProperty( PROPERTY_INDEXER_NAME );

        int nIdIndexer = IndexerHome.findByName( strNomIndexer, plugin ).getIdIndexer(  );

        List<Object> listObjets = new ArrayList<Object>(  );

        List<IndexerAction> indexerActions = IndexerActionHome.findActionsByIndexer( nIdIndexer,
                IndexerAction.ACTION_DELETE, true, bArchive, plugin );

        int nIdObjet;

        for ( IndexerAction action : indexerActions )
        {
            nIdObjet = action.getIdObjet(  );

            if ( action.getType(  ) == IndexerAction.TYPE_FICHIER )
            {
                listObjets.add( FichierHome.findByPrimaryKey( nIdObjet, plugin ) );
            }
            else if ( action.getType(  ) == IndexerAction.TYPE_PDD )
            {
                listObjets.add( PDDHome.findByPrimaryKey( nIdObjet, plugin ) );
            }
            else if ( action.getType(  ) == IndexerAction.TYPE_VA )
            {
                listObjets.add( VoeuAmendementHome.findByPrimaryKey( nIdObjet, plugin ) );
            }
        }

        return listObjets;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //																						//
    //								Fonctions de recherche									//
    //																						//
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Retourne la liste des PDD recherchés par référence
     * @param requete la requête utilisateur
     * @param plugin le plugin
     * @return la liste des PDD dont la référence contient la chaîne passée dans la requête
     */
    public static ResultatRequete getPDDByReference( RequeteUtilisateur requete, Plugin plugin )
    {
        String strRecherche = requete.getChampRecherche(  );
        ResultatRequete resultatRequete = new ResultatRequete(  );
        Seance prochaineSeance = SeanceHome.getProchaineSeance( plugin );

        List<PDD> listPDDs = null;

        if ( prochaineSeance == null )
        {
            listPDDs = new ArrayList<PDD>(  );
        }
        else
        {
            listPDDs = PDDHome.findPddsListSearchedByReference( strRecherche, requete.isRechercheArchive(  ),
                    prochaineSeance, plugin );

            FichierFilter filter = new FichierFilter(  );

            for ( PDD pdd : listPDDs )
            {
                filter.setPDD( pdd.getIdPdd(  ) );
                pdd.setFichiers( FichierHome.findByFilter( filter, plugin ) );
            }
        }

        resultatRequete.setListePDDs( listPDDs );
        resultatRequete.setRequeteUtilisateur( requete );

        return resultatRequete;
    }

    /**
     * Retourne la liste des objets indexés recherchés par mot(s) clé;
     * une telle recherche porte forcément sur la prochaine séance
     * @param requete la requête utilisateur
     * @param plugin le plugin
     * @return une liste d'objets indexés
     */
    public static ResultatRequete getIndexedObjects( RequeteUtilisateur requete, Plugin plugin )
    {
        ResultatRequete resultatRequete = new ResultatRequete(  );

        List<Object> listObjets = ODSIndexedDocSearchServiceProchaineSeance.getInstance(  )
                                                                           .getIndexedObjects( requete, plugin );

        Set<Fichier> listFichiers = getFichiersFromObjetsIndexes( listObjets );

        resultatRequete.setListePDDs( new ArrayList<PDD>( getPDDsIndexes( listObjets, listFichiers,
                    requete.isRechercheArchive(  ), plugin ) ) );
        resultatRequete.setListeAmendements( new ArrayList<VoeuAmendement>( getAmendementsIndexes( listObjets,
                    listFichiers, requete.isRechercheArchive(  ), plugin ) ) );
        resultatRequete.setListeVoeux( new ArrayList<VoeuAmendement>( getVoeuxIndexes( listObjets, listFichiers,
                    requete.isRechercheArchive(  ), plugin ) ) );

        resultatRequete.setRequeteUtilisateur( requete );

        return resultatRequete;
    }

    /**
     * Retourne une partie du modèle, contenant les objets correspondant aux critères de recherche
     * @param requete la requête utilisateur
     * @param plugin le plugin
     * @return les listes de PDDs, Amendements et Voeux correspondant aux critères
     */
    public static ResultatRequete getAllObjectsByCriterias( RequeteUtilisateur requete, Plugin plugin )
    {
        ResultatRequete resultatRequete = new ResultatRequete(  );

        ODSDocSearchService docSearchService;
        boolean bArchive = requete.isRechercheArchive(  );

        if ( bArchive )
        {
            docSearchService = new ODSDocSearchServiceArchive(  );
        }
        else
        {
            docSearchService = new ODSDocSearchServiceProchaineSeance(  );
        }

        List<Object> listObjets = null;
        Set<Fichier> listFichiers = null;
        boolean bMotCle = ( requete.getChampRecherche(  ) != null ) &&
            ( !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( requete.getChampRecherche(  ) ) );

        if ( bMotCle )
        {
            if ( bArchive )
            {
                listObjets = ODSIndexedDocSearchServiceArchive.getInstance(  ).getIndexedObjects( requete, plugin );
            }
            else
            {
                listObjets = ODSIndexedDocSearchServiceProchaineSeance.getInstance(  ).getIndexedObjects( requete,
                        plugin );
            }

            listFichiers = getFichiersFromObjetsIndexes( listObjets );
        }

        Set<PDD> listPDDs = null;

        if ( ( requete.getListeTypesDocumentFromRepresentation(  ).contains( PARAM_PROJET ) ) ||
                ( requete.getListeTypesDocumentFromRepresentation(  ).contains( PARAM_PROPOSITION ) ) )
        {
            listPDDs = docSearchService.getPDD( requete, plugin );

            if ( bMotCle )
            {
                listPDDs.retainAll( getPDDsIndexes( listObjets, listFichiers, requete.isRechercheArchive(  ), plugin ) );
                listPDDs = filtrerFichiers( listPDDs, listFichiers, plugin );
            }
        }

        Set<VoeuAmendement> listAmendements = null;

        if ( requete.getListeTypesDocumentFromRepresentation(  ).contains( PARAM_AMENDEMENT ) )
        {
            listAmendements = docSearchService.getAmendements( requete, plugin );

            if ( bMotCle )
            {
                listAmendements.retainAll( getAmendementsIndexes( listObjets, listFichiers,
                        requete.isRechercheArchive(  ), plugin ) );
            }
        }

        Set<VoeuAmendement> listVoeux = null;

        if ( requete.getListeTypesDocumentFromRepresentation(  ).contains( PARAM_VOEU ) )
        {
            listVoeux = docSearchService.getVoeux( requete, plugin );

            if ( bMotCle )
            {
                listVoeux.retainAll( getVoeuxIndexes( listObjets, listFichiers, requete.isRechercheArchive(  ), plugin ) );
            }
        }

        List<Seance> listSeances = null;

        if ( requete.getListeTypesDocumentFromRepresentation(  ).contains( PARAM_SEANCE ) )
        {
            listSeances = docSearchService.getSeances( requete, plugin );
        }

        List<Fichier> listAutresFichiers = null;

        if ( requete.getListeTypesDocumentFromRepresentation(  ).contains( PARAM_AUTRES ) )
        {
            listAutresFichiers = docSearchService.getAutresDocuments( requete, listFichiers, plugin );
        }

        resultatRequete.setListePDDs( ( listPDDs == null ) ? null : new ArrayList<PDD>( listPDDs ) );
        resultatRequete.setListeAmendements( ( listAmendements == null ) ? null
                                                                         : new ArrayList<VoeuAmendement>( 
                listAmendements ) );
        resultatRequete.setListeVoeux( ( listVoeux == null ) ? null : new ArrayList<VoeuAmendement>( listVoeux ) );
        resultatRequete.setListeSeances( listSeances );
        resultatRequete.setListeAutresDocuments( listAutresFichiers );
        resultatRequete.setRequeteUtilisateur( requete );

        return resultatRequete;
    }

    /**
     * Retourne la liste des PDD à partir des objets indexés
     * @param listObjets la liste des objets (VoeuAmendement ou PDD) indexés
     * @param listFichiers la liste des fichiers indexés
     * @param bArchive indique si la recherche porte sur la prochaine séance ou les archives
     * @param plugin le plugin
     * @return la liste des PDD correspondant aux critères
     */
    private static Set<PDD> getPDDsIndexes( List<Object> listObjets, Set<Fichier> listFichiers, boolean bArchive,
        Plugin plugin )
    {
        ODSDocSearchService docSearchService;

        if ( bArchive )
        {
            docSearchService = new ODSDocSearchServiceArchive(  );
        }
        else
        {
            docSearchService = new ODSDocSearchServiceProchaineSeance(  );
        }

        Set<PDD> listPDDs = new HashSet<PDD>(  );

        listPDDs.addAll( getPDDsFromObjetsIndexes( listObjets, plugin ) );
        listPDDs.addAll( getPDDsFromFichiersIndexes( listFichiers, plugin ) );

        PDDFilter filter = docSearchService.getGenericPDDFilter( plugin );

        if ( bArchive )
        {
            filter.setProchaineSeance( PDDFilter.ALL_INT );
        }

        listPDDs.retainAll( PDDHome.findByFilter( filter, plugin ) );

        return listPDDs;
    }

    /**
     * Retourne la liste des amendements à partir des objets indexés
     * @param listObjets la liste des objets (VoeuAmendement ou PDD) indexés
     * @param listFichiers la liste des fichiers indexés
     * @param bArchive indique si la recherche porte sur la prochaine séance ou les archives
     * @param plugin le plugin
     * @return la liste des amendements correspondant aux critères
     */
    private static Set<VoeuAmendement> getAmendementsIndexes( List<Object> listObjets, Set<Fichier> listFichiers,
        boolean bArchive, Plugin plugin )
    {
        ODSDocSearchService docSearchService;

        if ( bArchive )
        {
            docSearchService = new ODSDocSearchServiceArchive(  );
        }
        else
        {
            docSearchService = new ODSDocSearchServiceProchaineSeance(  );
        }

        Set<VoeuAmendement> listAmendements = new HashSet<VoeuAmendement>(  );

        listAmendements.addAll( getAmendementsFromObjetsIndexes( listObjets ) );
        listAmendements.addAll( getAmendementsFromFichiersIndexes( listFichiers, plugin ) );
        listAmendements.retainAll( VoeuAmendementHome.findVoeuAmendementListByFilter( 
                docSearchService.getGenericAmendementFilter( plugin ), plugin ) );

        return listAmendements;
    }

    /**
     * Retourne la liste des voeux à partir des objets indexés
     * @param listObjets la liste des objets (VoeuAmendement ou PDD) indexés
     * @param listFichiers la liste des fichiers indexés
     * @param bArchive indique si la recherche porte sur la prochaine séance ou les archives
     * @param plugin le plugin
     * @return la liste des voeux correspondant aux critères
     */
    private static Set<VoeuAmendement> getVoeuxIndexes( List<Object> listObjets, Set<Fichier> listFichiers,
        boolean bArchive, Plugin plugin )
    {
        ODSDocSearchService docSearchService;

        if ( bArchive )
        {
            docSearchService = new ODSDocSearchServiceArchive(  );
        }
        else
        {
            docSearchService = new ODSDocSearchServiceProchaineSeance(  );
        }

        Set<VoeuAmendement> listVoeux = new HashSet<VoeuAmendement>(  );

        listVoeux.addAll( getVoeuxFromObjetsIndexes( listObjets ) );
        listVoeux.addAll( getVoeuxFromFichiersIndexes( listFichiers, plugin ) );
        listVoeux.retainAll( VoeuAmendementHome.findVoeuAmendementListByFilter( docSearchService.getGenericVoeuFilter( 
                    plugin ), plugin ) );

        return listVoeux;
    }

    /**
     * Retourne la liste des fichiers à partir des objets récupérés dans l'index Lucene
     * @param listObjets la liste des objets retournés par Lucene
     * @return la liste des fichiers de l'index
     */
    public static Set<Fichier> getFichiersFromObjetsIndexes( List<Object> listObjets )
    {
        Set<Fichier> listFichiers = new HashSet<Fichier>(  );

        for ( Object objet : listObjets )
        {
            if ( objet instanceof Fichier )
            {
                listFichiers.add( (Fichier) objet );
            }
        }

        return listFichiers;
    }

    /**
     * Retourne la liste des PDDs à partir des objets récupérés dans l'index Lucene
     * @param listObjets la liste des objets retournés par Lucene
     * @param plugin le plugin
     * @return la liste des PDDs de l'index
     */
    private static Set<PDD> getPDDsFromObjetsIndexes( List<Object> listObjets, Plugin plugin )
    {
        Set<PDD> listPDDs = new HashSet<PDD>(  );

        for ( Object objet : listObjets )
        {
            if ( objet instanceof PDD )
            {
                FichierFilter filter = new FichierFilter(  );
                filter.setPDD( ( (PDD) objet ).getIdPdd(  ) );
                ( (PDD) objet ).setFichiers( FichierHome.findByFilter( filter, plugin ) );
                listPDDs.add( (PDD) objet );
            }
        }

        return listPDDs;
    }

    /**
     * Retourne la liste des amendements à partir des objets récupérés dans l'index Lucene
     * @param listObjets la liste des objets retournés par Lucene
     * @return la liste des amendements de l'index
     */
    private static Set<VoeuAmendement> getAmendementsFromObjetsIndexes( List<Object> listObjets )
    {
        Set<VoeuAmendement> listAmendements = new HashSet<VoeuAmendement>(  );

        for ( Object objet : listObjets )
        {
            if ( objet instanceof VoeuAmendement &&
                    ( ( (VoeuAmendement) objet ).getType(  ).equals( VoeuAmendementJspBean.CONSTANTE_TYPE_A ) ||
                    ( (VoeuAmendement) objet ).getType(  ).equals( VoeuAmendementJspBean.CONSTANTE_TYPE_LR ) ) )
            {
                listAmendements.add( (VoeuAmendement) objet );
            }
        }

        return listAmendements;
    }

    /**
     * Retourne la liste des voeux à partir des objets récupérés dans l'index Lucene
     * @param listObjets liste des objets
     * @return la liste des voeux de l'index
     */
    private static Set<VoeuAmendement> getVoeuxFromObjetsIndexes( List<Object> listObjets )
    {
        Set<VoeuAmendement> listVoeux = new HashSet<VoeuAmendement>(  );

        for ( Object objet : listObjets )
        {
            if ( objet instanceof VoeuAmendement &&
                    ( ( (VoeuAmendement) objet ).getType(  ).equals( VoeuAmendementJspBean.CONSTANTE_TYPE_VNR ) ||
                    ( (VoeuAmendement) objet ).getType(  ).equals( VoeuAmendementJspBean.CONSTANTE_TYPE_VR ) ) )
            {
                listVoeux.add( (VoeuAmendement) objet );
            }
        }

        return listVoeux;
    }

    /**
     * Retourne la liste des PDD dont les fichiers contiennent les mots clés
     * @param listFichiers la liste des fichiers indexés
     * @param plugin le plugin
     * @return la liste des PDD
     */
    private static Set<PDD> getPDDsFromFichiersIndexes( Set<Fichier> listFichiers, Plugin plugin )
    {
        Set<PDD> listPDDs = new HashSet<PDD>(  );

        for ( Fichier fichier : listFichiers )
        {
            PDD pdd = fichier.getPDD(  );

            if ( pdd != null )
            {
                FichierFilter filter = new FichierFilter(  );
                filter.setPDD( pdd.getIdPdd(  ) );
                pdd.setFichiers( FichierHome.findByFilter( filter, plugin ) );
                listPDDs.add( pdd );
            }
        }

        return listPDDs;
    }

    /**
     * Retourne la liste des amendements à partir des fichiers récupérés dans l'index Lucene
     * @param listFichiers la liste des fichiers
     * @param plugin le plugin
     * @return la liste des amendements correspondant aux fichiers
     */
    private static Set<VoeuAmendement> getAmendementsFromFichiersIndexes( Set<Fichier> listFichiers, Plugin plugin )
    {
        Set<VoeuAmendement> listAmendements = new HashSet<VoeuAmendement>(  );

        for ( Fichier fichier : listFichiers )
        {
            TypeDocument type = fichier.getTypdeDocument(  );

            if ( type.getId(  ) == TypeDocumentEnum.AMENDEMENT.getId(  ) )
            {
                listAmendements.add( VoeuAmendementHome.findByTexteInitial( fichier, plugin ) );
            }
        }

        return listAmendements;
    }

    /**
     * Retourne la liste des voeux à partir des fichiers récupérés dans l'index Lucene
     * @param listFichiers la liste des fichiers
     * @param plugin le plugin
     * @return la liste des voeux correspondant aux fichiers
     */
    private static Set<VoeuAmendement> getVoeuxFromFichiersIndexes( Set<Fichier> listFichiers, Plugin plugin )
    {
        Set<VoeuAmendement> listVoeux = new HashSet<VoeuAmendement>(  );

        for ( Fichier fichier : listFichiers )
        {
            TypeDocument type = fichier.getTypdeDocument(  );

            if ( type.getId(  ) == TypeDocumentEnum.VOEU.getId(  ) )
            {
                listVoeux.add( VoeuAmendementHome.findByTexteInitial( fichier, plugin ) );
            }
        }

        return listVoeux;
    }

    /**
     * Pour chaque PDD supprime les fichiers ne contenant pas la chaîne de caractères recherchée
     * @param listPDDs la liste des PDDs
     * @param listFichiers la liste des fichiers indexés
     * @param plugin le plugin
     * @return la liste des PDD sans les fichiers ne contenant pas le(s) mot(s) recherché(s)
     */
    private static Set<PDD> filtrerFichiers( Set<PDD> listPDDs, Set<Fichier> listFichiers, Plugin plugin )
    {
        Set<PDD> listPDDsTemporaire = new HashSet<PDD>(  );
        List<Fichier> fichiersDuPDD;
        FichierFilter filter = new FichierFilter(  );

        for ( PDD pdd : listPDDs )
        {
            filter.setPDD( pdd.getIdPdd(  ) );
            fichiersDuPDD = FichierHome.findByFilter( filter, plugin );
            fichiersDuPDD.retainAll( listFichiers );
            pdd.setFichiers( new ArrayList<Fichier>( fichiersDuPDD ) );
            listPDDsTemporaire.add( pdd );
        }

        return listPDDsTemporaire;
    }
}
