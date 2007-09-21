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

import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexModifier;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 *
 * ODSIndexationService
 *
 */
public abstract class ODSIndexationService
{
    public static final boolean IS_ARCHIVE = true;
    private static final String ERREUR_MODIFICATION_INDEX = "ods.odsindexationservice.message.erreur.modifyIndex";
    private static final String ERREUR_FERMETURE_INDEX = "ods.odsindexationservice.message.erreur.closeIndex";
    private static final String ERREUR_CHARGEMENT_ANALYSEUR = "ods.odsindexationservice.message.erreur.loadAnalyser";
    private static final String ERREUR_CLASSE_ANALYSEUR_INVALIDE = "ods.odsindexationservice.message.erreur.invalidAnalyser";
    private static final String ERREUR_OUVERTURE_INDEX = "ods.odsindexationservice.message.erreur.openIndex";

    // variables définies dans WEB-INF/conf/plugins/ods.properties
    private static final String PROPERTY_WRITER_MERGE_FACTOR = "odssearch.mergeFactor";
    private static final String PROPERTY_WRITER_MAX_FIELD_LENGTH = "odssearch.maxFieldLength";
    private static final String PROPERTY_ANALYSER_CLASSNAME = "odssearch.lucene.analyser.className";
    private static final String DEFAULT_INDEX_PATH = "ods.odssearch.lucene.default.indexPath";
    private static final int DEFAULT_WRITER_MERGE_FACTOR = 20;
    private static final int DEFAULT_WRITER_MAX_FIELD_LENGTH = 1000000;
    private static final String FIELD_ID_OBJET = "id_objet";
    private static final String PLUGIN_ODS_NAME = "ods";
    private static String _strIndex;
    private static Analyzer _analyzer;
    private static int _nWriterMergeFactor = DEFAULT_WRITER_MERGE_FACTOR;
    private static int _nWriterMaxFieldLength = DEFAULT_WRITER_MAX_FIELD_LENGTH;
    private boolean _bArchiveService;

    /**
     * Constructeur
     * @param strIndexPath le chemin de l'index
     * @param bArchiveService indique si le service porte sur l'index archive ou prochaine seance
     */
    public ODSIndexationService( String strIndexPath, boolean bArchiveService )
    {
        _bArchiveService = bArchiveService;

        if ( ( strIndexPath != null ) && !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strIndexPath ) )
        {
            _strIndex = strIndexPath;
        }
        else
        {
            _strIndex = AppPathService.getPath( DEFAULT_INDEX_PATH );
        }

        if ( ( _strIndex == null ) || ( OdsConstants.CONSTANTE_CHAINE_VIDE.equals( _strIndex ) ) )
        {
            throw new AppException(  );
        }

        String strAnalyserClassName = AppPropertiesService.getProperty( PROPERTY_ANALYSER_CLASSNAME );

        if ( ( strAnalyserClassName == null ) || ( OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strAnalyserClassName ) ) )
        {
            throw new AppException( I18nService.getLocalizedString( ERREUR_CLASSE_ANALYSEUR_INVALIDE,
                    Locale.getDefault(  ) ) );
        }

        _nWriterMergeFactor = AppPropertiesService.getPropertyInt( PROPERTY_WRITER_MERGE_FACTOR,
                DEFAULT_WRITER_MERGE_FACTOR );
        _nWriterMaxFieldLength = AppPropertiesService.getPropertyInt( PROPERTY_WRITER_MAX_FIELD_LENGTH,
                DEFAULT_WRITER_MAX_FIELD_LENGTH );

        try
        {
            _analyzer = (Analyzer) Class.forName( strAnalyserClassName ).newInstance(  );
        }
        catch ( Exception e )
        {
            throw new AppException( I18nService.getLocalizedString( ERREUR_CHARGEMENT_ANALYSEUR, Locale.getDefault(  ) ),
                e );
        }
    }

    /**
    * Lance l'indexation
    * @throws AppException en cas d'erreur lors de la manipulation de l'index
    */
    public void processIndexing(  ) throws AppException
    {
        ODSSearchIndexer searchIndexer = new ODSSearchIndexer( _bArchiveService );
        IndexModifier indexModifier;

        // On ouvre l'écriture sur l'index
        try
        {
            indexModifier = new IndexModifier( _strIndex, _analyzer, false );
        }
        catch ( IOException ioe )
        {
            throw new AppException( ERREUR_OUVERTURE_INDEX, ioe );
        }

        indexModifier.setMergeFactor( _nWriterMergeFactor );
        indexModifier.setMaxFieldLength( _nWriterMaxFieldLength );

        List<Document> listDocs = null;

        // On récupère puis on supprime les documents à supprimer 
        listDocs = searchIndexer.getDocumentsToRemove(  );

        for ( Document doc : listDocs )
        {
            try
            {
                indexModifier.deleteDocuments( new Term( FIELD_ID_OBJET, doc.getField( FIELD_ID_OBJET ).stringValue(  ) ) );
            }
            catch ( IOException ioe )
            {
                throw new AppException( ERREUR_MODIFICATION_INDEX, ioe );
            }
        }

        // On récupère puis on supprime les documents à ajouter
        listDocs = searchIndexer.getDocumentsToIndex(  );

        for ( Document doc : listDocs )
        {
            try
            {
                indexModifier.addDocument( doc );
            }
            catch ( IOException ioe )
            {
                throw new AppException( ERREUR_MODIFICATION_INDEX, ioe );
            }
        }

        //      On ferme l'écriture sur l'index
        try
        {
            indexModifier.flush(  );
            indexModifier.optimize(  );
            indexModifier.close(  );
        }
        catch ( IOException ioe )
        {
            throw new AppException( ERREUR_FERMETURE_INDEX, ioe );
        }
    }

    /**
     * Initialise l'index
     */
    public void initIndex(  )
    {
        ODSSearchIndexer searchIndexer = new ODSSearchIndexer( _bArchiveService );
        Plugin plugin = PluginService.getPlugin( PLUGIN_ODS_NAME );
        List<Document> listeDocumentsAIndexer;
        viderIndex(  );

        if ( !_bArchiveService )
        {
            try
            {
                listeDocumentsAIndexer = searchIndexer.getDocuments(  );
            }
            catch ( Exception e )
            {
                listeDocumentsAIndexer = new ArrayList<Document>(  );
            }

            initIndex( listeDocumentsAIndexer );
        }
        else
        {
            List<Seance> listSeances = SeanceHome.findOldSeance( plugin );

            for ( Seance seance : listSeances )
            {
                listeDocumentsAIndexer = searchIndexer.getDocuments( seance.getIdSeance(  ) );
                initIndex( listeDocumentsAIndexer );
            }
        }
    }

    /**
     * Initialise l'index avec une liste de documents à indexer; si l'argument est <b>null</b> alors
     * la fonction récupère tous les objets de la base (et les convertit en Document Lucene)
     * @param listDocs la liste des documents Lucene
     */
    public void initIndex( List<Document> listDocs ) throws AppException
    {
        List<Document> listeDocumentsAIndexer = listDocs;
        IndexWriter indexWriter = null;

        try
        {
        	// param booleen à true ==> crée l'index s'il n'existe pas encore
            indexWriter = new IndexWriter( _strIndex, _analyzer, true );
        }
        catch ( IOException ioe )
        {
            throw new AppException( ERREUR_OUVERTURE_INDEX, ioe );
        }
        
        for ( Document doc : listeDocumentsAIndexer )
        {
            try
            {
                indexWriter.addDocument( doc );
            }
            catch ( IOException ioe )
            {
                AppLogService.error( "Error indexing document: " + doc.getField( FIELD_ID_OBJET ).stringValue(  ) );
            }
        }

        try
        {
            indexWriter.close(  );
        }
        catch ( IOException ioe )
        {
            throw new AppException( ERREUR_FERMETURE_INDEX, ioe );
        }
    }

    /**
     * Rajoute les documents de l'ordre du jour définitif de la dernière séance à l'index archive
     * @param ancienneSeance l'ancienne séance
     * @throws AppException
     */
    public void changeSeance( Seance ancienneSeance ) throws AppException
    {
        ODSSearchIndexer searchIndexer = new ODSSearchIndexer( IS_ARCHIVE );
        List<Document> listeDocumentsAIndexer = searchIndexer.getDocuments( ancienneSeance.getIdSeance(  ) );
        IndexWriter indexWriter = null;

        try
        {
            indexWriter = new IndexWriter( _strIndex, _analyzer, true );
        }
        catch ( IOException ioe )
        {
            throw new AppException( ERREUR_OUVERTURE_INDEX, ioe );
        }

        for ( Document doc : listeDocumentsAIndexer )
        {
            try
            {
                indexWriter.addDocument( doc );
            }
            catch ( IOException ioe )
            {
                AppLogService.error( "Error indexing document: " + doc.getField( FIELD_ID_OBJET ).stringValue(  ) );
            }
        }

        try
        {
            indexWriter.optimize(  );
            indexWriter.close(  );
        }
        catch ( IOException ioe )
        {
            throw new AppException( ERREUR_FERMETURE_INDEX, ioe );
        }
    }

    /**
     * Fonction qui force la réinitialisation de l'index en supprimant tous ses documents
     */
    private void viderIndex(  )
    {
        try
        {
            IndexModifier indexModifier = new IndexModifier( _strIndex, _analyzer, false );
            int nNbDocs = indexModifier.docCount(  );

            for ( int i = 0; i < nNbDocs; i++ )
            {
                indexModifier.deleteDocument( i );
            }

            indexModifier.flush(  );
            indexModifier.optimize(  );
            indexModifier.close(  );
        }
        catch ( IOException ioe )
        {
            AppLogService.error( ioe );
        }
    }
}
