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
package fr.paris.lutece.plugins.ods.web;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.commission.CommissionHome;
import fr.paris.lutece.plugins.ods.business.documentseance.DocumentSeance;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierFilter;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocumentEnum;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilEnum;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourFilter;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.pdd.PDDFilter;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravaux;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravauxFilter;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravauxHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementFilter;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementHome;
import fr.paris.lutece.plugins.ods.service.manager.LuteceUserManager;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Affichage de l'interface prochain conseil
 */
public class ProchainConseilApp implements XPageApplication
{
    public static final String PROPERTY_TELECHARGEMENT_HOST = "telechargement.host";
    public static final String PROPERTY_TELECHARGEMENT_WEBAPP = "telechargement.webapp";
    public static final String PROPERTY_TELECHARGEMENT_SERVLET_DOCUMENTS_SEANCE = "telechargement.servlet.documentsSeance";
    public static final String PROPERTY_TELECHARGEMENT_COOKIE_DOMAIN = "telechargement.cookie.domain";
    private static final String TEMPLATE_PROCHAIN_CONSEIL = "skin/plugins/ods/prochain_conseil.html";
    private static final String MARK_LISTE_COMMISSION = "liste_commission";
    private static final String MARK_LISTE_FORMATION_CONSEIL = "liste_formation_conseil";
    private static final String MARK_LISTE_FORMATION_CONSEIL_PJ = "liste_formation_conseil_pj";
    private static final String MARK_LISTE_FORMATION_CONSEIL_PP = "liste_formation_conseil_pp";
    private static final String MARK_LISTE_FORMATION_CONSEIL_VA = "liste_formation_conseil_va";
    private static final String MARK_LISTE_ORDRE_DU_JOUR_CONSEIL = "liste_ordre_du_jour_conseil";
    private static final String MARK_LISTE_ORDRE_DU_JOUR_REFERENCE = "liste_ordre_du_jour_reference";
    private static final String MARK_LISTE_ORDRE_DU_JOUR_COMMISSION = "liste_ordre_du_jour_commission";
    private static final String MARK_LISTE_AUTRES_DOCUMENTS_SEANCE = "liste_autres_documents_seance";
    private static final String MARK_LISTE_DOCUMENT_SEANCE = "liste_document_seance";
    private static final String MARK_URL_DOWNLOAD_DOCUMENTS_SEANCE = "url_download_documents_seance";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.prochain_conseil";
    private static final String PROPERTY_PAGE_TITLE = "ods.front.prochainconseil.page.title";

    /**
     * Permet d'initialiser la liste des choix de type de conseil
     * @param plugin plugin
     * @return la liste des commissions
     */
    private ReferenceList initRefListCommission( Plugin plugin )
    {
        ReferenceList refListCommission = new ReferenceList(  );
        List<Commission> listCommission = CommissionHome.findAllCommissionsActives( plugin );

        //ajout dans la referenceList de l'item chaine vide
        //ajout dans la referenceList de l'ensemble des items représentant les groupes politiques
        for ( Commission commission : listCommission )
        {
            refListCommission.addItem( commission.getIdCommission(  ),
                OdsConstants.CONSTANTE_CHAINE_VIDE + commission.getNumero(  ) );
        }

        return refListCommission;
    }

    /**
     * @param request la requête HTTP
     * @param nMode le mode
     * @param plugin le plugin
     * @return XPage template
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        LuteceUserManager.gestionUtilisateur( model, request, plugin );

        //récupération de la prochaine séance
        Seance seance = SeanceHome.getProchaineSeance( plugin );

        ReferenceList refListCommission = initRefListCommission( plugin );

        XPage page = new XPage(  );
        Locale locale = request.getLocale(  );
        int nIdCommission = 1;
        List<FormationConseil> formationConseils = FormationConseilHome.findFormationConseilList( plugin );
        List<FormationConseil> formationConseilsPP = new ArrayList<FormationConseil>( formationConseils );
        List<FormationConseil> formationConseilsPJ = new ArrayList<FormationConseil>( formationConseils );
        List<FormationConseil> formationConseilsVA = new ArrayList<FormationConseil>( formationConseils );
        List<OrdreDuJour> listeOrdreDuJourConseil = new ArrayList<OrdreDuJour>(  );
        List<OrdreDuJour> listeOrdreDuJourReference = new ArrayList<OrdreDuJour>(  );

        List<OrdreDuJour> listeOrdreDuJourCommission = new ArrayList<OrdreDuJour>(  );
        List<DocumentSeance> listeDocumentProchaineSeance = new ArrayList<DocumentSeance>(  );
        List<DocumentSeance> listeDocumentCommission = new ArrayList<DocumentSeance>(  );
        List<DocumentSeance> listeAutresDocumentsSeance = new ArrayList<DocumentSeance>(  );
        List<Fichier> listeFichier = new ArrayList<Fichier>(  );

        // On récupère la liste des Fichiers mis en ligne après la date de dernière connexion de l'utilisateur
        List<Fichier> listFichiers = OdsUtils.getNouveauxFichiersPublies( request, plugin );

        if ( listFichiers != null )
        {
            model.put( OdsMarks.MARK_LISTE_NOUVEAUX_FICHIERS, listFichiers );
        }

        // On récupère la liste des Odj mis en ligne après la date de dernière connexion de l'utilisateur
        List<OrdreDuJour> listOdj = OdsUtils.getNouveauxOdjPublies( request, plugin );

        if ( listOdj != null )
        {
            model.put( OdsMarks.MARK_LISTE_NOUVEAUX_ODJ, listOdj );
        }

        if ( ( seance != null ) && ( formationConseils != null ) && ( formationConseils.size(  ) > 1 ) )
        {
            //on recupere la liste des projets de la prochaine seance pour le conseil municipal 
            PDDFilter pddFilter = new PDDFilter(  );
            pddFilter.setType( PDD.CONSTANTE_TYPE_PROJET );
            pddFilter.setProchaineSeance( seance.getIdSeance(  ) );
            pddFilter.setPublication( 1 );
            pddFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILMUNICIPAL.getId(  ) );

            List<PDD> pdds = PDDHome.findByFilter( pddFilter, plugin );

            int nIndex = 0;

            if ( ( pdds == null ) || ( pdds.size(  ) == 0 ) )
            {
                formationConseilsPJ.remove( nIndex );
            }
            else
            {
                nIndex++;
            }

            //on recupere la liste des projets de la prochaine seance pour le conseil general
            pddFilter = new PDDFilter(  );
            pddFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) );
            pddFilter.setPublication( 1 );
            pddFilter.setType( PDD.CONSTANTE_TYPE_PROJET );
            pddFilter.setProchaineSeance( seance.getIdSeance(  ) );
            pdds = PDDHome.findByFilter( pddFilter, plugin );

            if ( ( pdds == null ) || ( pdds.size(  ) == 0 ) )
            {
                formationConseilsPJ.remove( nIndex );
            }
            else
            {
                nIndex++;
            }

            //on recupere la liste des propositions de la prochaine seance pour le conseil municipal
            pddFilter = new PDDFilter(  );
            pddFilter.setType( PDD.CONSTANTE_TYPE_PROP );
            pddFilter.setPublication( 1 );
            pddFilter.setProchaineSeance( seance.getIdSeance(  ) );
            pddFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILMUNICIPAL.getId(  ) );
            pdds = PDDHome.findByFilter( pddFilter, plugin );
            nIndex = 0;

            if ( ( pdds == null ) || ( pdds.size(  ) == 0 ) )
            {
                formationConseilsPP.remove( nIndex );
            }
            else
            {
                nIndex++;
            }

            //on recupere la liste des propositions de la prochaine seance pour le conseil general
            pddFilter = new PDDFilter(  );
            pddFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) );
            pddFilter.setPublication( 1 );
            pddFilter.setProchaineSeance( seance.getIdSeance(  ) );
            pddFilter.setType( PDD.CONSTANTE_TYPE_PROP );
            pdds = PDDHome.findByFilter( pddFilter, plugin );

            if ( ( pdds == null ) || ( pdds.size(  ) == 0 ) )
            {
                formationConseilsPP.remove( nIndex );
            }
            else
            {
                nIndex++;
            }

            //on recupere la liste des voeux amendements de la prochaine seance pour le conseil municipal
            VoeuAmendementFilter voeuAmendementFilter = new VoeuAmendementFilter(  );
            voeuAmendementFilter.setIdPublie( 1 );
            voeuAmendementFilter.setIdSeance( seance.getIdSeance(  ) );
            voeuAmendementFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILMUNICIPAL.getId(  ) );

            List<VoeuAmendement> vas = VoeuAmendementHome.findLiasseByFilter( voeuAmendementFilter, plugin );
            nIndex = 0;

            if ( ( vas == null ) || ( vas.size(  ) == 0 ) )
            {
                formationConseilsVA.remove( nIndex );
            }
            else
            {
                nIndex++;
            }

            //on recupere la liste des voeux amendements de la prochaine seance pour le conseil general
            voeuAmendementFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) );
            vas = VoeuAmendementHome.findLiasseByFilter( voeuAmendementFilter, plugin );

            if ( ( vas == null ) || ( vas.size(  ) == 0 ) )
            {
                formationConseilsVA.remove( nIndex );
            }
            else
            {
                nIndex++;
            }

            if ( null != request.getParameter( OdsParameters.ID_COMMISSION ) )
            {
                try
                {
                    nIdCommission = Integer.parseInt( request.getParameter( OdsParameters.ID_COMMISSION ) );
                }
                catch ( NumberFormatException ne )
                {
                    AppLogService.error( ne );
                }
            }

            /************************** Traitement pour Relevé des Travaux **************************/
            ReleveDesTravaux releve = null;
            ReleveDesTravauxFilter rdtFilter = new ReleveDesTravauxFilter(  );

            if ( ( request.getParameter( OdsParameters.ID_COMMISSION ) == null ) ||
                    request.getParameter( OdsParameters.ID_COMMISSION ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                rdtFilter.setIdCommission( 1 );
            }
            else
            {
                rdtFilter.setIdCommission( Integer.parseInt( request.getParameter( OdsParameters.ID_COMMISSION ) ) );
            }

            rdtFilter.setIdPublie( 1 );
            rdtFilter.setIdSeance( seance.getIdSeance(  ) );

            List<ReleveDesTravaux> listReleves = ReleveDesTravauxHome.findReleveDesTravauxByFilter( rdtFilter, plugin );

            if ( ( listReleves != null ) && ( listReleves.size(  ) > 0 ) )
            {
                releve = listReleves.get( 0 );
                model.put( ReleveDesTravaux.MARK_RELEVE, releve );
            }

            /************************* Traitement pour les Ordres du Jour du Conseil *********************/
            OrdreDuJourFilter ordreDuJourFilter = new OrdreDuJourFilter(  );
            ordreDuJourFilter.setIdSeance( seance.getIdSeance(  ) );
            ordreDuJourFilter.setIdPublie( 1 );
            ordreDuJourFilter.setIdSauvegarde( 0 );
            listeOrdreDuJourConseil = OrdreDuJourHome.findOrdreDuJourList( plugin, ordreDuJourFilter, true );

            //document prochaine seance de type odj
            for ( OrdreDuJour odj : listeOrdreDuJourConseil )
            {
                DocumentSeance odjDocument = new DocumentSeance(  );
                odjDocument.setOrdreDuJour( odj );
                listeDocumentProchaineSeance.add( odjDocument );
            }

            //ordre du jour de reference
            //pour le conseil municipal
            ordreDuJourFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILMUNICIPAL.getId(  ) );

            OrdreDuJour ordreDuJour = OrdreDuJourHome.findOdjReferenceByFilter( ordreDuJourFilter, plugin );

            if ( ordreDuJour != null )
            {
                listeOrdreDuJourReference.add( ordreDuJour );
            }

            //ordre du jour de reference
            //pour le conseil general
            ordreDuJourFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) );
            ordreDuJour = OrdreDuJourHome.findOdjReferenceByFilter( ordreDuJourFilter, plugin );

            if ( ordreDuJour != null )
            {
                listeOrdreDuJourReference.add( ordreDuJour );
            }

            //document prochaine seance de type fichier
            FichierFilter fichierFilter = new FichierFilter(  );
            fichierFilter.setIdSeance( seance.getIdSeance(  ) );
            fichierFilter.setIdTypeDocument( TypeDocumentEnum.ORDRE_DU_JOUR.getId(  ) );
            fichierFilter.setCommissionNul( 1 );
            listeFichier = FichierHome.findByFilter( fichierFilter, plugin );

            for ( Fichier fichier : listeFichier )
            {
                DocumentSeance odjDocument = new DocumentSeance(  );
                odjDocument.setFichier( fichier );
                listeDocumentProchaineSeance.add( odjDocument );
            }

            //on trie la liste par date de publication
            Collections.sort( listeDocumentProchaineSeance, new DocumentSeance(  ) );

            /*********************************traitement pour les ordres du jour des commissions******************/
            ordreDuJourFilter.setIdFormationConseil( OrdreDuJourFilter.ALL_INT );
            ordreDuJourFilter.setIdCommission( nIdCommission );
            listeOrdreDuJourCommission = OrdreDuJourHome.findOrdreDuJourList( plugin, ordreDuJourFilter, true );

            for ( OrdreDuJour odj : listeOrdreDuJourCommission )
            {
                DocumentSeance odjDocument = new DocumentSeance(  );
                odjDocument.setOrdreDuJour( odj );
                listeDocumentCommission.add( odjDocument );
            }

            //ajout des fichiers de la prochaine seance
            //les fichiers PDF qui sont liés à la prochaine séance et dont le numéro de commission 
            //est la commission choisie. Le type de fichier est « Ordre du jour » ou « Relevé ».
            fichierFilter.setCommissionNul( FichierFilter.ALL_INT );
            fichierFilter.setIdCommission( nIdCommission );
            fichierFilter.setIdTypeDocument( TypeDocumentEnum.ORDRE_DU_JOUR.getId(  ) );
            listeFichier = FichierHome.findByFilter( fichierFilter, plugin );

            for ( Fichier fichier : listeFichier )
            {
                DocumentSeance odjDocument = new DocumentSeance(  );
                odjDocument.setFichier( fichier );
                listeDocumentCommission.add( odjDocument );
            }

            fichierFilter.setIdTypeDocument( TypeDocumentEnum.RELEVE.getId(  ) );
            listeFichier = FichierHome.findByFilter( fichierFilter, plugin );

            for ( Fichier fichier : listeFichier )
            {
                DocumentSeance odjDocument = new DocumentSeance(  );
                odjDocument.setFichier( fichier );
                listeDocumentCommission.add( odjDocument );
            }

            //on trie la liste par date de publication
            Collections.sort( listeDocumentCommission, new DocumentSeance(  ) );

            /******************************traitement pour les autres documents **********************************/
            FichierFilter filterAutreDocumentSeance = new FichierFilter(  );
            filterAutreDocumentSeance.setIdSeance( seance.getIdSeance(  ) );
            filterAutreDocumentSeance.setAutreDocumentsSeance( 1 );
            filterAutreDocumentSeance.setPublication( 1 );

            listeFichier = FichierHome.findByFilter( filterAutreDocumentSeance, plugin );

            for ( Fichier fichier : listeFichier )
            {
                DocumentSeance odjDocument = new DocumentSeance(  );
                odjDocument.setFichier( fichier );
                listeAutresDocumentsSeance.add( odjDocument );
            }

            Collections.sort( listeAutresDocumentsSeance, new DocumentSeance(  ) );
        }
        
        /* Création du cookie pour attester de l'authentification sur le serveur de téléchargement */
        HttpServletResponse response = LocalVariables.getResponse();        
        Cookie[] cookies = request.getCookies();
        String strGuid = "";

        if (cookies != null) {
        	for (Cookie c : cookies) 
        	{
        		if ((c != null) && 
        				(c.getName() != null) && 
        				c.getName().equals( AppPropertiesService.getProperty( PanierApp.PROPERTY_COOKIE_WSSO_NAME_SOURCE ) ) )
        		{
        			strGuid = c.getValue();
        			break;
        		}
        	}
        }

        Cookie newCookie = new Cookie( AppPropertiesService.getProperty( PanierApp.PROPERTY_COOKIE_WSSO_NAME_DESTINATION ), strGuid );
        newCookie.setDomain( AppPropertiesService.getProperty( ProchainConseilApp.PROPERTY_TELECHARGEMENT_COOKIE_DOMAIN ) );
        newCookie.setPath("/");
        response.addCookie( newCookie );

        String url = AppPropertiesService.getProperty( ProchainConseilApp.PROPERTY_TELECHARGEMENT_HOST ) + "/" +
        	AppPropertiesService.getProperty( ProchainConseilApp.PROPERTY_TELECHARGEMENT_WEBAPP ) + "/" +
        	AppPropertiesService.getProperty( ProchainConseilApp.PROPERTY_TELECHARGEMENT_SERVLET_DOCUMENTS_SEANCE );

        model.put( MARK_LISTE_DOCUMENT_SEANCE, listeDocumentProchaineSeance );
        model.put( MARK_LISTE_FORMATION_CONSEIL, formationConseils );
        model.put( MARK_LISTE_FORMATION_CONSEIL_PJ, formationConseilsPJ );
        model.put( MARK_LISTE_FORMATION_CONSEIL_PP, formationConseilsPP );
        model.put( MARK_LISTE_FORMATION_CONSEIL_VA, formationConseilsVA );
        model.put( MARK_LISTE_COMMISSION, refListCommission );
        model.put( MARK_LISTE_ORDRE_DU_JOUR_CONSEIL, listeOrdreDuJourConseil );
        model.put( MARK_LISTE_ORDRE_DU_JOUR_REFERENCE, listeOrdreDuJourReference );
        model.put( MARK_LISTE_ORDRE_DU_JOUR_COMMISSION, listeDocumentCommission );
        model.put( MARK_LISTE_AUTRES_DOCUMENTS_SEANCE, listeAutresDocumentsSeance );
        model.put( Seance.MARK_SEANCE, seance );
        model.put( OdsParameters.ID_COMMISSION, nIdCommission );
        model.put( MARK_URL_DOWNLOAD_DOCUMENTS_SEANCE, url);

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PROCHAIN_CONSEIL, request.getLocale(  ), model );
        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );
        page.setTitle( strPageTitle );
        page.setContent( template.getHtml(  ) );
        page.setPathLabel( strPagePathLabel );

        return page;
    }
}
