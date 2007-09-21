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
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementHome;
import fr.paris.lutece.plugins.ods.service.search.requete.RequeteUtilisateur;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;


/**
 * Cette classe réalise la recherche dans l'index Lucene
 */
public class ODSIndexedDocSearchService
{
    private static final String ERREUR_CHARGEMENT_OBJETS = "ods.odsindexeddocsearchservice.message.erreurChargement";
    private static final String ERREUR_RECHERCHE = "ods.odsindexeddocsearchservice.message.erreurRecherche";
    private static final String ERREUR_LECTURE_INDEX = "ods.odsindexeddocsearchservice.message.erreurLecture";
    private static final String ERREUR_FERMETURE_INDEX = "ods.odsindexeddocsearchservice.message.erreurFermeture";
    private static final String ERREUR_OUVERTURE_INDEX = "ods.odsindexeddocsearchservice.message.erreurOuverture";
    private static final String ERREUR_CHARGEMENT_ANALYSEUR = "ods.odsindexeddocsearchservice.message.erreurChargementAnalyseur";
    private static final String ERREUR_CLASSE_ANALYSEUR_INVALIDE = "ods.odsindexeddocsearchservice.message.erreurAnalyseurInvalide";
    private static final String TYPE_TITRE = "titre";
    private static final String TYPE_PDD_OBJECT = "objet_pdd";
    private static final String TYPE_VA_OBJECT = "objet_va";
    private static final String TYPE_PDD_REFERENCE = "reference_pdd";
    private static final String TYPE_VA_REFERENCE = "reference_va";
    private static final String PROPERTY_ANALYSER_CLASSNAME = "odssearch.lucene.analyser.className";
    private static final String FIELD_ID_OBJET = "id_objet";
    private static final String DEFAULT_INDEX_PATH = "ods.odssearch.lucene.default.indexPath";

    private static final String FIELD_CONTENT = "contents";
    private static String _strIndex;
    private static IndexSearcher _searcher;
    private static Analyzer _analyzer;

    /**
     * Constructeur privï¿½, initialise les valeurs de _strIndex et _analyzer
     * @param strIndex le chemin de l'index
     */
    public ODSIndexedDocSearchService( String strIndex )
    {
        if ( ( strIndex != null ) && !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strIndex ) )
        {
            _strIndex = strIndex;
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
     * Retourne la liste des objets indexï¿½s dans l'index Lucene qui contiennents le(s) mot(s) recherchï¿½s
     * @param requete la requête utilisateur
     * @param plugin le plugin
     * @return une liste d'objets indexés reherchés par mot(s) clé
     * @throws AppException AppException
     */
    public List<Object> getIndexedObjects( RequeteUtilisateur requete, Plugin plugin )
        throws AppException
    {
        List<Object> listObjets = new ArrayList<Object>(  );

        try
        {
            _searcher = new IndexSearcher( _strIndex );
        }
        catch ( IOException ioe )
        {
            throw new AppException( I18nService.getLocalizedString( ERREUR_OUVERTURE_INDEX, Locale.getDefault(  ) ) +
                _strIndex );
        }

        StringTokenizer stRequete = new StringTokenizer( requete.getChampRecherche(  ), " " );
        String strRequeteLucene = OdsConstants.CONSTANTE_CHAINE_VIDE;

        while ( stRequete.hasMoreTokens(  ) )
        {
            strRequeteLucene += ( stRequete.nextToken(  ) + "~ " );
        }

        QueryParser parser = new QueryParser( FIELD_CONTENT, _analyzer );
        parser.setDefaultOperator( QueryParser.Operator.AND );

        Query query;

        try
        {
            query = parser.parse( strRequeteLucene );
        }
        catch ( ParseException pe )
        {
            fermerIndex(  );
            throw new AppException( I18nService.getLocalizedString( ERREUR_LECTURE_INDEX, Locale.getDefault(  ) ) +
                _strIndex + pe.getMessage(  ) );
        }

        Hits hits = null;

        try
        {
            hits = _searcher.search( query );
        }
        catch ( IOException ioe )
        {
            fermerIndex(  );
            throw new AppException( I18nService.getLocalizedString( ERREUR_RECHERCHE, Locale.getDefault(  ) ) );
        }
        catch ( Exception e )
        {
            fermerIndex(  );
        }

        Iterator iterator = hits.iterator(  );
        List<Document> listDocs = new ArrayList<Document>(  );

        while ( iterator.hasNext(  ) )
        {
            Hit hit = (Hit) ( iterator.next(  ) );
            Document doc;

            try
            {
                doc = hit.getDocument(  );
            }
            catch ( IOException ioe )
            {
                fermerIndex(  );
                throw new AppException( I18nService.getLocalizedString( ERREUR_CHARGEMENT_OBJETS, Locale.getDefault(  ) ) +
                    ioe.getMessage(  ) );
            }

            listDocs.add( doc );
        }

        fermerIndex(  );

        for ( Document document : listDocs )
        {
            listObjets.add( getObjetFromDocument( document, plugin ) );
        }

        return listObjets;
    }

    /**
     * Force la fermeture de l'index si une opï¿½ration s'est mal dï¿½roulï¿½e
     */
    private void fermerIndex(  ) throws AppException
    {
        try
        {
            _searcher.close(  );
        }
        catch ( IOException ioe )
        {
            throw new AppException( I18nService.getLocalizedString( ERREUR_FERMETURE_INDEX, Locale.getDefault(  ) ) +
                ioe.getMessage(  ) );
        }
    }

    /**
     * Convertit un document de l'index en objet compatible ODS
     * @param document le document lucene
     * @param plugin le plugin
     * @return un objet d'ODS : un Fichier, un PDD ou un VoeuAmendement
     */
    private Object getObjetFromDocument( Document document, Plugin plugin )
    {
        Object objet = null;

        String strIdObjet = document.get( FIELD_ID_OBJET );

        StringTokenizer tokenizer = new StringTokenizer( strIdObjet, " " );
        String strTypeObjet = tokenizer.nextToken(  );
        int nIdObjet = Integer.parseInt( tokenizer.nextToken(  ) );

        if ( strTypeObjet.equals( TYPE_PDD_OBJECT ) || strTypeObjet.equals( TYPE_PDD_REFERENCE ) )
        {
            objet = PDDHome.findByPrimaryKey( nIdObjet, plugin );
        }
        else if ( strTypeObjet.equals( TYPE_VA_OBJECT ) || strTypeObjet.equals( TYPE_VA_REFERENCE ) )
        {
            objet = VoeuAmendementHome.findByPrimaryKey( nIdObjet, plugin );
        }
        else if ( strTypeObjet.equals( Fichier.MARK_FICHIER ) || strTypeObjet.equals( TYPE_TITRE ) )
        {
            objet = FichierHome.findByPrimaryKey( nIdObjet, plugin );
        }

        return objet;
    }
}
