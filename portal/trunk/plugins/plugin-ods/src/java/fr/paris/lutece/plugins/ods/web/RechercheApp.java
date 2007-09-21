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

import fr.paris.lutece.plugins.ods.business.categoriedeliberation.CategorieDeliberation;
import fr.paris.lutece.plugins.ods.business.categoriedeliberation.CategorieDeliberationHome;
import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.commission.CommissionHome;
import fr.paris.lutece.plugins.ods.business.direction.Direction;
import fr.paris.lutece.plugins.ods.business.direction.DirectionHome;
import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.elu.EluHome;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilHome;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitique;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitiqueHome;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.service.manager.LuteceUserManager;
import fr.paris.lutece.plugins.ods.service.search.ODSSearchService;
import fr.paris.lutece.plugins.ods.service.search.requete.RequeteUtilisateur;
import fr.paris.lutece.plugins.ods.service.search.requete.RequeteUtilisateurHome;
import fr.paris.lutece.plugins.ods.service.search.requete.ResultatRequete;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;


/**
 * RechercheApp
 */
public class RechercheApp implements XPageApplication
{
    private static final String MARK_LISTE_SELECTED_TYPES_DOCUMENT = "liste_selected_types_document";
    private static final String MARK_VALUE_URL_FORMULAIRE = "/jsp/site/Portal.jsp?page=ods_recherche";
    private static final String MARK_URL_FORMULAIRE = "url_formulaire";
    private static final String PROPERTY_MAX_NOMBRE_FICHIERS = "odssearch.maxNombreFichiers";
    private static final int DEFAUT_MAX_NOMBRE_FICHIERS = 50;
    private static final int MAX_NOMBRE_FICHIERS = AppPropertiesService.getPropertyInt( PROPERTY_MAX_NOMBRE_FICHIERS,
            DEFAUT_MAX_NOMBRE_FICHIERS );
    private static final String TEMPLATE_RECHERCHE = "skin/plugins/ods/recherche.html";
    private static final String TEMPLATE_RECHERCHE_RESULTATS = "skin/plugins/ods/recherche_resultats.html";
    private static final String TEMPLATE_REQUETE_NON_VALIDE = "skin/plugins/ods/non_valid_search.html";
    private static final String MARK_URL_PDD = "url_pdd";
    private static final String MARK_VALUE_URL_PDD = "jsp/site/Portal.jsp?page=ods_pdd";
    private static final String PARAM_PROJET = "projet";
    private static final String PARAM_PROPOSITION = "proposition";
    private static final String PARAM_AMENDEMENT = "amendement";
    private static final String PARAM_VOEU = "voeu";
    private static final String PARAM_CONSEIL = "conseil";
    private static final String PARAM_SEARCH = "search";
    private static final String PARAM_REFERENCE = "code_pdd";
    private static final String PARAM_COMMISSIONS = "commission";
    private static final String PARAM_RAPPORTEURS = "rapporteur";
    private static final String PARAM_ARRONDISSEMENT = "arrondissement";
    private static final String PARAM_DIRECTION = "direction";
    private static final String PARAM_CATEGORIES = "categorie";
    private static final String PARAM_GROUPES = "groupe";
    private static final String PARAM_ELUS = "elu";
    private static final String PARAM_REQUETE = "id_requete";
    private static final String PARAM_RESULT = "result";
    private static final String MARK_ERREUR_MESSAGE = "message";
    private static final String MARK_LISTE_COMMISSIONS = "liste_commissions";
    private static final String MARK_LISTE_RAPPORTEURS = "liste_elus_rapporteurs";
    private static final String MARK_LISTE_DIRECTIONS = "liste_directions";
    private static final String MARK_LISTE_CATEGORIES = "liste_categories";
    private static final String MARK_LISTE_GROUPES = "liste_groupes";
    private static final String MARK_LISTE_ELUS = "liste_elus";
    private static final String MARK_LISTE_CONSEILS = "liste_conseils";
    private static final String MARK_LISTE_SELECTED_COMMISSIONS = "liste_selected_commissions";
    private static final String MARK_LISTE_SELECTED_ARRONDISSEMENTS = "liste_selected_arrondissements";
    private static final String MARK_LISTE_SELECTED_RAPPORTEURS = "liste_selected_elus_rapporteurs";
    private static final String MARK_LISTE_SELECTED_DIRECTIONS = "liste_selected_directions";
    private static final String MARK_LISTE_SELECTED_CATEGORIES = "liste_selected_categories";
    private static final String MARK_LISTE_SELECTED_GROUPES = "liste_selected_groupes";
    private static final String MARK_LISTE_SELECTED_ELUS = "liste_selected_elus";
    private static final String MARK_LISTE_SELECTED_CONSEILS = "liste_selected_conseils";
    private static final String MARK_LISTE_PDDS = "liste_pdds";
    private static final String MARK_LISTE_AMENDEMENTS = "liste_amendements";
    private static final String MARK_LISTE_VOEUX = "liste_voeux";
    private static final String MARK_NOMBRE_FICHIERS = "nombre_fichiers";
    private static final String MARK_NOMBRE_OBJETS = "nombre_objets";
    private static final String MARK_REQUETE_UTILISATEUR = "user_search";
    private static final String MARK_LISTE_PDD_RATTACHE_ODJ = "liste_pdds_rattache_odj";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.recherche_resultats";
    private static final String PROPERTY_PAGE_RESULTAT_TITLE = "ods.front.rechercheresultats.page.title";
    private static final String PROPERTY_PAGE_FORMULAIRE_TITLE = "ods.front.recherche.page.title";
    private static final String PROPERTY_PAGE_ERREUR_TITRE = "ods.front.error.page.title";
    private static final String ERREUR_REQUETE_SIMPLE_NON_VALIDE = "ods.front.rechercheresultats.message.nonValidSimpleRequest";
    private static final String ERREUR_REQUETE_MULTI_NON_VALIDE = "ods.front.rechercheresultats.message.nonValidMultiRequest";
    private static final String ERREUR_RECHERCHE_MOT_CLE_NON_VALIDE = "ods.front.rechercheresultats.message.nonValidChampMotCle";
    private static final String ERREUR_REQUETE_UTILISATEUR = "ods.front.mescriteres.error.noaccess";
    private static final String ERREUR_TROP_DE_RESULTATS = "ods.front.rechercheresultats.message.tooManyResults";
    private static final String ERREUR_PAS_DE_PROCHAINE_SEANCE = "ods.front.rechercheresultats.message.aucuneProchaineSeance";
    private static final String TYPE_REQUETE_SIMPLE = "simple";
    private static final String TYPE_PROJET = "projet";
    private static final String TYPE_PROPOSITION = "proposition";
    private static final String TYPE_AMENDEMENT = "amendement";
    private static final String TYPE_VOEU = "voeu";
    private static final String ALL_TYPES_DOC = "proposition:projet:amendement:voeu";
    private static final String ALL_FORMATIONS_CONSEIL = "0:1";
    private RequeteUtilisateur _requeteUtilisateur;

    /**
     * Retourne la page de recherche
     *
     * @param request la requête HTTP
     * @param nMode le mode
     * @param plugin le plugin
     * @return XPage template
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        // On met à jour la liste des utilisateurs et gère la session de l'utilisateur
        LuteceUserManager.gestionUtilisateur( model, request, plugin );

        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );

        // Si il n'y a pas de prochaine séance, on affiche une page d'erreur
        if ( SeanceHome.getProchaineSeance( plugin ) == null )
        {
            return getErrorPage( model, ERREUR_PAS_DE_PROCHAINE_SEANCE, request.getLocale(  ) );
        }

        String strResult = request.getParameter( PARAM_RESULT );
        String strIdRequete = request.getParameter( PARAM_REQUETE );

        if ( ( ( strResult == null ) || strResult.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) &&
                ( strIdRequete == null ) )
        {
            return getPageFormulaire( model, request, plugin );
        }

        return getPageResultat( model, request, plugin );
    }

    /**
     * Retourne la page du formulaire de recherche multi-criteres
     * @param request la requete Http
     * @param model  le modèle pour le template
     * @param plugin le plugin ods
     * @return la page du formulaire de recherche
     */
    private XPage getPageFormulaire( Map<Object, Object> model, HttpServletRequest request, Plugin plugin )
    {
        XPage page = new XPage(  );
        Locale locale = request.getLocale(  );
        model.put( MARK_LISTE_COMMISSIONS, CommissionHome.findAllCommissionsActives( plugin ) );
        model.put( MARK_LISTE_DIRECTIONS, DirectionHome.findAllDirectionsActives( plugin ) );
        model.put( MARK_LISTE_CATEGORIES, CategorieDeliberationHome.findAllActivesOrderByLibelle( plugin ) );
        model.put( MARK_LISTE_GROUPES, GroupePolitiqueHome.findGroupesActifs( plugin ) );
        model.put( MARK_LISTE_ELUS, EluHome.findElusActifs( plugin ) );
        model.put( MARK_LISTE_CONSEILS, FormationConseilHome.findFormationConseilList( plugin ) );
        model.put( MARK_LISTE_RAPPORTEURS, EluHome.findElusRapporteursActifs( plugin ) );

        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_FORMULAIRE_TITLE, locale );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RECHERCHE, request.getLocale(  ), model );
        page.setTitle( strPageTitle );
        page.setPathLabel( strPagePathLabel );
        page.setContent( template.getHtml(  ) );

        return page;
    }

    /**
     * Retourne le résultat d'une recherche dans les documents et objets d'ODS
     * @param request la requête Http
     * @param plugin le plugin
     * @param model  le modèle pour le template
     * @return la page de résultat d'une recherche
     */
    private XPage getPageResultat( Map<Object, Object> model, HttpServletRequest request, Plugin plugin )
    {
        XPage page = new XPage(  );
        Locale locale = request.getLocale(  );
        String strTypeRequete = null;
        String strIdRequete = request.getParameter( PARAM_REQUETE );
        boolean bRechercheOk = true;

        if ( strIdRequete == null )
        {
            XPage xpage = null;

            strTypeRequete = request.getParameter( OdsParameters.TYPE );

            if ( strTypeRequete.equals( TYPE_REQUETE_SIMPLE ) )
            {
                if ( !isSimpleValideForm( request ) )
                {
                    xpage = getErrorPage( model, ERREUR_REQUETE_SIMPLE_NON_VALIDE, locale );
                }
                else if ( !isChampMotCleValide( request ) )
                {
                    xpage = getErrorPage( model, ERREUR_RECHERCHE_MOT_CLE_NON_VALIDE, locale );
                }
                else
                {
                    prepareRequeteUtilisateurSimple( request );
                    bRechercheOk = setSimpleSearchModel( model, plugin );
                }
            }
            else if ( strTypeRequete.equals( OdsParameters.REFERENCE ) )
            {
                if ( !isReferenceValideForm( request ) )
                {
                    xpage = getErrorPage( model, ERREUR_REQUETE_SIMPLE_NON_VALIDE, locale );
                }

                if ( xpage == null )
                {
                    prepareRequeteUtilisateurReference( request );
                    bRechercheOk = setReferenceSearchModel( model, plugin );
                }
            }
            else if ( strTypeRequete.equals( OdsParameters.MULTI_CRITERE ) )
            {
                if ( !isMultiCriteriaValideForm( request ) )
                {
                    xpage = getErrorPage( model, ERREUR_REQUETE_MULTI_NON_VALIDE, locale );
                }
                else if ( !isChampMotCleValide( request ) )
                {
                    xpage = getErrorPage( model, ERREUR_RECHERCHE_MOT_CLE_NON_VALIDE, locale );
                }

                if ( xpage == null )
                {
                    prepareRequeteUtilisateurMultiCritere( request, plugin );
                    bRechercheOk = setMultiCriteriaSearchModel( model, plugin );
                }
            }

            if ( ( xpage == null ) && !bRechercheOk )
            {
                xpage = getErrorPage( model, ERREUR_TROP_DE_RESULTATS, locale );
            }

            if ( xpage != null )
            {
                return xpage;
            }
        }
        else
        {
            XPage xpage = null;

            try
            {
                _requeteUtilisateur = RequeteUtilisateurHome.findByPrimaryKey( Integer.parseInt( strIdRequete ), plugin );
            }
            catch ( Exception e )
            {
                xpage = getErrorPage( model, ERREUR_REQUETE_UTILISATEUR, locale );
            }

            if ( ( xpage == null ) &&
                    ( ( _requeteUtilisateur == null ) ||
                    ( SecurityService.getInstance(  ).getRegisteredUser( request ) == null ) ||
                    !_requeteUtilisateur.getUserName(  )
                                            .equals( SecurityService.getInstance(  ).getRegisteredUser( request )
                                                                        .getName(  ) ) ) )
            {
                xpage = getErrorPage( model, ERREUR_REQUETE_UTILISATEUR, locale );
            }

            if ( xpage != null )
            {
                return xpage;
            }

            strTypeRequete = _requeteUtilisateur.getTypeRequete(  );

            if ( strTypeRequete.equals( TYPE_REQUETE_SIMPLE ) )
            {
                bRechercheOk = setSimpleSearchModel( model, plugin );
            }
            else if ( strTypeRequete.equals( OdsParameters.REFERENCE ) )
            {
                bRechercheOk = setReferenceSearchModel( model, plugin );
            }
            else if ( strTypeRequete.equals( OdsParameters.MULTI_CRITERE ) )
            {
                bRechercheOk = setMultiCriteriaSearchModel( model, plugin );
            }

            if ( !bRechercheOk )
            {
                return getErrorPage( model, ERREUR_TROP_DE_RESULTATS, locale );
            }
        }

        String strPortalUrl = AppPathService.getBaseUrl( request );
        model.put( MARK_URL_PDD, strPortalUrl + MARK_VALUE_URL_PDD );
        model.put( MARK_URL_FORMULAIRE, strPortalUrl + MARK_VALUE_URL_FORMULAIRE );

        // On récupère la liste des PDD mis en ligne après la date de dernière connexion de l'utilisateur
        List<PDD> listPdd = OdsUtils.getNouveauxPddsPublies( request, plugin );

        if ( listPdd != null )
        {
            model.put( OdsMarks.MARK_LISTE_NOUVEAUX_PDDS, listPdd );
        }

        // On récupère la liste des VA mis en ligne après la date de dernière connexion de l'utilisateur
        List<VoeuAmendement> listVa = OdsUtils.getNouveauxVAPublies( request, plugin );

        if ( listVa != null )
        {
            model.put( OdsMarks.MARK_LISTE_NOUVEAUX_VAS, listVa );
        }

        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_RESULTAT_TITLE, locale );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RECHERCHE_RESULTATS, request.getLocale(  ),
                model );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );
        page.setTitle( strPageTitle );
        page.setContent( template.getHtml(  ) );
        page.setPathLabel( strPagePathLabel );

        return page;
    }

    /**
     * Retourne une page d'erreur avec le message adapté
     * @param strErrorMessage le message à afficher
     * @param locale la locale du navigateur
     * @param model  le modèle pour le template
     * @return une page d'erreur
     */
    private XPage getErrorPage( Map<Object, Object> model, String strErrorMessage, Locale locale )
    {
        XPage page = new XPage(  );
        model.put( MARK_ERREUR_MESSAGE, strErrorMessage );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_REQUETE_NON_VALIDE, locale, model );
        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_ERREUR_TITRE, locale );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );
        page.setTitle( strPageTitle );
        page.setContent( template.getHtml(  ) );
        page.setPathLabel( strPagePathLabel );

        return page;
    }

    /**
     * Retourne le résultat d'une recherche par référence de PDD
     * @param plugin le plugin
     * @param model  le modèle pour le template
     * @return TRUE si la recherche s'est bien déroulée, FALSE sinon
     */
    private boolean setReferenceSearchModel( Map<Object, Object> model, Plugin plugin )
    {
        prepareSelectedItemsModel( model, plugin );

        model.put( OdsParameters.TYPE, OdsParameters.REFERENCE );

        List<PDD> listPDDs = ODSSearchService.getPDDByReference( _requeteUtilisateur, plugin ).getListePDDs(  );

        if ( listPDDs.size(  ) > MAX_NOMBRE_FICHIERS )
        {
            return false;
        }

        if ( listPDDs.size(  ) != 0 )
        {
            model.put( MARK_LISTE_PDDS, listPDDs );
        }

        model.put( MARK_NOMBRE_OBJETS, listPDDs.size(  ) );

        int nNombreFichiers = 0;

        for ( PDD pdd : listPDDs )
        {
            nNombreFichiers += pdd.getFichiers(  ).size(  );
        }

        /*
         * Recuperation de  la liste des pdds inscrits à l'ordre du jour de reference
         * pour le conseil general et municipal
         */
        if ( !_requeteUtilisateur.isRechercheArchive(  ) )
        {
            List<PDD> pddRelieOdj = OdsUtils.getPddsInscritsOdj( SeanceHome.getProchaineSeance( plugin ), null, plugin );

            if ( pddRelieOdj != null )
            {
                model.put( MARK_LISTE_PDD_RATTACHE_ODJ, pddRelieOdj );
            }
        }

        model.put( MARK_NOMBRE_FICHIERS, nNombreFichiers );

        return true;
    }

    /**
     * Retourne le résultat d'une recherche multi-critères
     * @param plugin le plugin
     * @param model  le modèle pour le template
     * @return TRUE si la recherche s'est bien déroulée, FALSE sinon
     */
    private boolean setMultiCriteriaSearchModel( Map<Object, Object> model, Plugin plugin )
    {
        prepareSelectedItemsModel( model, plugin );

        model.put( OdsParameters.TYPE, OdsParameters.MULTI_CRITERE );

        ResultatRequete resultatRequete = ODSSearchService.getAllObjectsByCriterias( _requeteUtilisateur, plugin );
        List<PDD> listPDDs = resultatRequete.getListePDDs(  );
        List<VoeuAmendement> listAmendements = resultatRequete.getListeAmendements(  );
        List<VoeuAmendement> listVoeux = resultatRequete.getListeVoeux(  );

        int nNombreObjets = 0;
        nNombreObjets += ( ( listPDDs != null ) ? listPDDs.size(  ) : 0 );
        nNombreObjets += ( ( listVoeux != null ) ? listVoeux.size(  ) : 0 );
        nNombreObjets += ( ( listAmendements != null ) ? listAmendements.size(  ) : 0 );

        model.put( MARK_NOMBRE_OBJETS, nNombreObjets );

        int nNombreFichiers = 0;

        if ( listPDDs != null )
        {
            for ( PDD pdd : listPDDs )
            {
                nNombreFichiers += pdd.getFichiers(  ).size(  );
            }
        }

        nNombreFichiers += ( ( listVoeux != null ) ? listVoeux.size(  ) : 0 );
        nNombreFichiers += ( ( listAmendements != null ) ? listAmendements.size(  ) : 0 );

        if ( nNombreFichiers > MAX_NOMBRE_FICHIERS )
        {
            return false;
        }

        model.put( MARK_NOMBRE_FICHIERS, nNombreFichiers );

        if ( ( listPDDs != null ) && ( listPDDs.size(  ) != 0 ) )
        {
            model.put( MARK_LISTE_PDDS, listPDDs );
        }

        if ( ( listAmendements != null ) && ( listAmendements.size(  ) != 0 ) )
        {
            model.put( MARK_LISTE_AMENDEMENTS, listAmendements );
        }

        if ( ( listVoeux != null ) && ( listVoeux.size(  ) != 0 ) )
        {
            model.put( MARK_LISTE_VOEUX, listVoeux );
        }

        /*
         * Recuperation de  la liste des pdds inscrits à l'ordre du jour de reference
         * pour le conseil general et municipal
         */
        if ( !_requeteUtilisateur.isRechercheArchive(  ) )
        {
            List<PDD> pddRelieOdj = OdsUtils.getPddsInscritsOdj( SeanceHome.getProchaineSeance( plugin ), null, plugin );

            if ( pddRelieOdj != null )
            {
                model.put( MARK_LISTE_PDD_RATTACHE_ODJ, pddRelieOdj );
            }
        }

        return true;
    }

    /**
     * Retourne le résultat d'une recherche par le bandeau haut
     * @param plugin le plugin
     * @param model  le modèle pour le template
     * @return TRUE si la recherche s'est bien déroulée, FALSE sinon
     */
    private boolean setSimpleSearchModel( Map<Object, Object> model, Plugin plugin )
    {
        prepareSelectedItemsModel( model, plugin );

        ResultatRequete resultatRequete = ODSSearchService.getIndexedObjects( _requeteUtilisateur, plugin );

        List<PDD> listPDDs = resultatRequete.getListePDDs(  );
        List<VoeuAmendement> listAmendements = resultatRequete.getListeAmendements(  );
        List<VoeuAmendement> listVoeux = resultatRequete.getListeVoeux(  );

        int nNombreObjets = 0;
        nNombreObjets += ( ( listPDDs != null ) ? listPDDs.size(  ) : 0 );
        nNombreObjets += ( ( listVoeux != null ) ? listVoeux.size(  ) : 0 );
        nNombreObjets += ( ( listAmendements != null ) ? listAmendements.size(  ) : 0 );

        model.put( MARK_NOMBRE_OBJETS, nNombreObjets );

        int nNombreFichiers = 0;

        for ( PDD pdd : listPDDs )
        {
            nNombreFichiers += pdd.getFichiers(  ).size(  );
        }

        nNombreFichiers += ( ( listVoeux != null ) ? listVoeux.size(  ) : 0 );
        nNombreFichiers += ( ( listAmendements != null ) ? listAmendements.size(  ) : 0 );

        if ( nNombreFichiers > MAX_NOMBRE_FICHIERS )
        {
            return false;
        }

        model.put( MARK_NOMBRE_FICHIERS, nNombreFichiers );

        if ( ( listPDDs != null ) && ( listPDDs.size(  ) != 0 ) )
        {
            model.put( MARK_LISTE_PDDS, listPDDs );
        }

        if ( ( listAmendements != null ) && ( listAmendements.size(  ) != 0 ) )
        {
            model.put( MARK_LISTE_AMENDEMENTS, listAmendements );
        }

        if ( ( listVoeux != null ) && ( listVoeux.size(  ) != 0 ) )
        {
            model.put( MARK_LISTE_VOEUX, listVoeux );
        }

        /*
         * Recuperation de  la liste des pdds inscrits à l'ordre du jour de reference
         * pour le conseil general et municipal
         */
        if ( !_requeteUtilisateur.isRechercheArchive(  ) )
        {
            List<PDD> pddRelieOdj = OdsUtils.getPddsInscritsOdj( SeanceHome.getProchaineSeance( plugin ), null, plugin );

            if ( pddRelieOdj != null )
            {
                model.put( MARK_LISTE_PDD_RATTACHE_ODJ, pddRelieOdj );
            }
        }

        return true;
    }

    /**
     * Prépare l'objet RequeteUtilisateur avec les paramètres pour une recherche par mot-clé.
     * Si l'utilisateur décide de l'enregistrer l'objet sera ainsi déjà créé (à transmettre en attribut de la requête)
     * @param request la requête
     */
    private void prepareRequeteUtilisateurReference( HttpServletRequest request )
    {
        _requeteUtilisateur = new RequeteUtilisateur(  );

        _requeteUtilisateur.setRechercheArchive( false );
        _requeteUtilisateur.setTypeRequete( OdsParameters.REFERENCE );
        _requeteUtilisateur.setChampRecherche( request.getParameter( PARAM_REFERENCE ) );
        _requeteUtilisateur.setListeTypesDocument( ALL_TYPES_DOC );
        _requeteUtilisateur.setListeFormationsConseil( ALL_FORMATIONS_CONSEIL );
        _requeteUtilisateur.setNotifie( false );
    }

    /**
     * Prépare l'objet RequeteUtilisateur avec les paramètres pour une recherche par référence de PDD.
     * Si l'utilisateur décide de l'enregistrer l'objet sera ainsi déjà créé (à transmettre en attribut de la requête)
     * @param request la requête
     */
    private void prepareRequeteUtilisateurSimple( HttpServletRequest request )
    {
        _requeteUtilisateur = new RequeteUtilisateur(  );

        _requeteUtilisateur.setRechercheArchive( false );
        _requeteUtilisateur.setTypeRequete( TYPE_REQUETE_SIMPLE );
        _requeteUtilisateur.setChampRecherche( request.getParameter( PARAM_SEARCH ) );
        _requeteUtilisateur.setListeTypesDocument( ALL_TYPES_DOC );
        _requeteUtilisateur.setListeFormationsConseil( ALL_FORMATIONS_CONSEIL );
        _requeteUtilisateur.setNotifie( false );
    }

    /**
     * Prépare l'objet RequeteUtilisateur avec les paramètres pour une recherche multi-critère.
     * Si l'utilisateur décide de l'enregistrer l'objet sera ainsi déjà créé (à transmettre en attribut de la requête)
     * @param request la requête
     * @param plugin le plugin
     */
    private void prepareRequeteUtilisateurMultiCritere( HttpServletRequest request, Plugin plugin )
    {
        _requeteUtilisateur = new RequeteUtilisateur(  );
        _requeteUtilisateur.setRechercheArchive( false );
        _requeteUtilisateur.setTypeRequete( OdsParameters.MULTI_CRITERE );
        _requeteUtilisateur.setChampRecherche( request.getParameter( PARAM_SEARCH ) );
        _requeteUtilisateur.setTypesDocumentFromListe( getListeTypesDocumentSelectionnes( request ) );
        _requeteUtilisateur.setFormationsConseilFromListe( getListeFormationsConseilSelectionnees( request, plugin ) );
        _requeteUtilisateur.setCommissionsFromListe( getListeCommissionsSelectionnees( request, plugin ) );
        _requeteUtilisateur.setRapporteursFromListe( getListeRapporteursSelectionnes( request, plugin ) );
        _requeteUtilisateur.setArrondissementsFromListe( getListeArrondissementSelectionnes( request ) );
        _requeteUtilisateur.setDirectionsFromListe( getListeDirectionsSelectionnees( request, plugin ) );
        _requeteUtilisateur.setCategoriesDeliberationFromListe( getListeCategoriesDeliberationSelectionnees( request,
                plugin ) );
        _requeteUtilisateur.setGroupesDepositairesFromListe( getListeGroupesDepositairesSelectionnes( request, plugin ) );
        _requeteUtilisateur.setElusDepositairesFromListe( getListeElusDepositairesSelectionnes( request, plugin ) );
        _requeteUtilisateur.setNotifie( false );
    }

    /**
     * Retourne la liste des formations sélectionnées à partir de la requête Http
     * @param request la requête
     * @return la liste des formations sélectionnées
     */
    private List<String> getListeTypesDocumentSelectionnes( HttpServletRequest request )
    {
        List<String> listeTypesDocument = new ArrayList<String>(  );

        if ( request.getParameter( PARAM_PROPOSITION ) != null )
        {
            listeTypesDocument.add( TYPE_PROPOSITION );
        }

        if ( request.getParameter( PARAM_PROJET ) != null )
        {
            listeTypesDocument.add( TYPE_PROJET );
        }

        if ( request.getParameter( PARAM_AMENDEMENT ) != null )
        {
            listeTypesDocument.add( TYPE_AMENDEMENT );
        }

        if ( request.getParameter( PARAM_VOEU ) != null )
        {
            listeTypesDocument.add( TYPE_VOEU );
        }

        return listeTypesDocument;
    }

    /**
     * Retourne la liste des formations de conseil sélectionnées à partir de la requête Http
     * @param request la requête Http
     * @param plugin le plugin
     * @return la liste des formations de conseil sélectionnées
     */
    private List<FormationConseil> getListeFormationsConseilSelectionnees( HttpServletRequest request, Plugin plugin )
    {
        List<FormationConseil> listeFormationsConseil = new ArrayList<FormationConseil>(  );
        String[] tabFormationsConseil = request.getParameterValues( PARAM_CONSEIL );

        // si aucun sélectionné erreur déjà survenue
        if ( tabFormationsConseil != null )
        {
            for ( String strFormationConseil : tabFormationsConseil )
            {
                listeFormationsConseil.add( FormationConseilHome.findByPrimaryKey( Integer.parseInt( 
                            strFormationConseil ), plugin ) );
            }
        }

        return listeFormationsConseil;
    }

    /**
     * Retourne la liste des commissions sélectionnées à partir de la requête Http
     * @param request la requête
     * @param plugin le plugin
     * @return la liste des commissions sélectionnées
     */
    private List<Commission> getListeCommissionsSelectionnees( HttpServletRequest request, Plugin plugin )
    {
        List<Commission> listeCommissions = new ArrayList<Commission>(  );
        String[] tabCommissions = request.getParameterValues( PARAM_COMMISSIONS );

        if ( tabCommissions != null )
        {
            for ( String strCommission : tabCommissions )
            {
                listeCommissions.add( CommissionHome.findByPrimaryKey( Integer.parseInt( strCommission ), plugin ) );
            }
        }

        return listeCommissions;
    }

    /**
     * Retourne la lsite des élus sélectionnés à partir de la requête
     * @param request la requête
     * @param plugin le plugin
     * @return la liste de élus sélectionnés
     */
    private List<Elu> getListeRapporteursSelectionnes( HttpServletRequest request, Plugin plugin )
    {
        List<Elu> listeRapporteurs = new ArrayList<Elu>(  );
        String[] tabRapporteurs = request.getParameterValues( PARAM_RAPPORTEURS );

        if ( tabRapporteurs != null )
        {
            for ( String strRapporteur : tabRapporteurs )
            {
                listeRapporteurs.add( EluHome.findByPrimaryKey( Integer.parseInt( strRapporteur ), plugin ) );
            }
        }

        return listeRapporteurs;
    }

    /**
     * Retourne la liste des arrondissements sélectionnés à partir de la requête
     * @param request la requête
     * @return la liste des arrondissements sélectionnés
     */
    private int[] getListeArrondissementSelectionnes( HttpServletRequest request )
    {
        String[] tabArrondissement = request.getParameterValues( PARAM_ARRONDISSEMENT );
        int[] listeArrondissement = new int[0];

        if ( tabArrondissement != null )
        {
            listeArrondissement = new int[tabArrondissement.length];

            int nIndex = 0;

            for ( String strArrondissement : tabArrondissement )
            {
                listeArrondissement[nIndex++] = Integer.parseInt( strArrondissement );
            }
        }

        return listeArrondissement;
    }

    /**
     * Retourne la liste des directions sélectionnées à partir de la requête Http
     * @param request la requête
     * @param plugin le plugin
     * @return la liste de dirctions sélectionnées
     */
    private List<Direction> getListeDirectionsSelectionnees( HttpServletRequest request, Plugin plugin )
    {
        List<Direction> listeDirections = new ArrayList<Direction>(  );
        String[] tabDirections = request.getParameterValues( PARAM_DIRECTION );

        if ( tabDirections != null )
        {
            for ( String strDirection : tabDirections )
            {
                listeDirections.add( DirectionHome.findByPrimaryKey( Integer.parseInt( strDirection ), plugin ) );
            }
        }

        return listeDirections;
    }

    /**
     * Retourne la liste des catégories de délibération sélectionnées à partir de la requête Http
     * @param request la requête
     * @param plugin le plugin
     * @return la liste de dirctions sélectionnées
     */
    private List<CategorieDeliberation> getListeCategoriesDeliberationSelectionnees( HttpServletRequest request,
        Plugin plugin )
    {
        List<CategorieDeliberation> listeCategoriesDeliberation = new ArrayList<CategorieDeliberation>(  );
        String[] tabCategoriesDeliberation = request.getParameterValues( PARAM_CATEGORIES );

        if ( tabCategoriesDeliberation != null )
        {
            for ( String strCategoriesDeliberation : tabCategoriesDeliberation )
            {
                listeCategoriesDeliberation.add( CategorieDeliberationHome.findByPrimaryKey( Integer.parseInt( 
                            strCategoriesDeliberation ), plugin ) );
            }
        }

        return listeCategoriesDeliberation;
    }

    /**
     * Retourne la liste des gropes politiques dépositaires sélectionnés à partir de la requête Http
     * @param request la requête Http
     * @param plugin le plugin
     * @return la liste des groupes dépositaires sélectionnés
     */
    private List<GroupePolitique> getListeGroupesDepositairesSelectionnes( HttpServletRequest request, Plugin plugin )
    {
        List<GroupePolitique> listeGroupesDepositaires = new ArrayList<GroupePolitique>(  );
        String[] tabGroupesDepositaires = request.getParameterValues( PARAM_GROUPES );

        if ( tabGroupesDepositaires != null )
        {
            for ( String strGroupesDepositaires : tabGroupesDepositaires )
            {
                listeGroupesDepositaires.add( GroupePolitiqueHome.findByPrimaryKey( Integer.parseInt( 
                            strGroupesDepositaires ), plugin ) );
            }
        }

        return listeGroupesDepositaires;
    }

    /**
     * Retourne la liste des élus dépositaires sélectionnés à partir de la requête
     * @param request la requête
     * @param plugin le plugin
     * @return la liste de élus dépositaires sélectionnés
     */
    private List<Elu> getListeElusDepositairesSelectionnes( HttpServletRequest request, Plugin plugin )
    {
        List<Elu> listeElusDepositaires = new ArrayList<Elu>(  );
        String[] tabElusDepositaires = request.getParameterValues( PARAM_ELUS );

        if ( tabElusDepositaires != null )
        {
            for ( String strEluDepositaire : tabElusDepositaires )
            {
                listeElusDepositaires.add( EluHome.findByPrimaryKey( Integer.parseInt( strEluDepositaire ), plugin ) );
            }
        }

        return listeElusDepositaires;
    }

    /**
     * Retourne le modèle avec les objets sélectionnés
     * @param plugin le plugin
     * @param model  le modèle pour le template
     */
    private void prepareSelectedItemsModel( Map<Object, Object> model, Plugin plugin )
    {
        model.put( MARK_REQUETE_UTILISATEUR, _requeteUtilisateur );

        model.put( MARK_LISTE_COMMISSIONS, CommissionHome.findAllCommissionsActives( plugin ) );
        model.put( MARK_LISTE_RAPPORTEURS, EluHome.findElusRapporteursActifs( plugin ) );
        model.put( MARK_LISTE_DIRECTIONS, DirectionHome.findAllDirectionsActives( plugin ) );
        model.put( MARK_LISTE_CATEGORIES, CategorieDeliberationHome.findAllActivesOrderByLibelle( plugin ) );
        model.put( MARK_LISTE_GROUPES, GroupePolitiqueHome.findGroupesActifs( plugin ) );
        model.put( MARK_LISTE_ELUS, EluHome.findElusActifs( plugin ) );
        model.put( MARK_LISTE_CONSEILS, FormationConseilHome.findFormationConseilList( plugin ) );
        model.put( MARK_LISTE_SELECTED_TYPES_DOCUMENT, _requeteUtilisateur.getListeTypesDocumentFromRepresentation(  ) );
        model.put( MARK_LISTE_SELECTED_ARRONDISSEMENTS,
            _requeteUtilisateur.getListeArrondissementsFromRepresentation( plugin ) );
        model.put( MARK_LISTE_SELECTED_COMMISSIONS, _requeteUtilisateur.getListeCommissionsFromRepresentation( plugin ) );
        model.put( MARK_LISTE_SELECTED_RAPPORTEURS, _requeteUtilisateur.getListeRapporteursFromRepresentation( plugin ) );
        model.put( MARK_LISTE_SELECTED_DIRECTIONS, _requeteUtilisateur.getListeDirectionsFromRepresentation( plugin ) );
        model.put( MARK_LISTE_SELECTED_CATEGORIES,
            _requeteUtilisateur.getListeCategorieDeliberationFromRepresentation( plugin ) );
        model.put( MARK_LISTE_SELECTED_GROUPES,
            _requeteUtilisateur.getListeGroupesDepositairesFromRepresentation( plugin ) );
        model.put( MARK_LISTE_SELECTED_ELUS, _requeteUtilisateur.getListeElusDepositairesFromRepresentation( plugin ) );
        model.put( MARK_LISTE_SELECTED_CONSEILS,
            _requeteUtilisateur.getListeFormationsConseilFromRepresentation( plugin ) );
    }

    /**
     * Indique si une requête d'une recherche par référence de code de PDD est valide,
     * c'est à dire que le champ de recherche est rempli
     * @param request la requête
     * @return <b>true</b> si la requête est valide, <b>false</b> sinon
     */
    private boolean isReferenceValideForm( HttpServletRequest request )
    {
        return ( ( request.getParameter( PARAM_REFERENCE ) != null ) &&
        !request.getParameter( PARAM_REFERENCE ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) );
    }

    /**
     * Indique si une requête d'une recherche "simple" par mot-clé est valide, c'est à dire que
     * le champ de recherche est rempli
     * @param request la requête
     * @return <b>true</b> si la requête est valide, <b>false</b> sinon
     */
    private boolean isSimpleValideForm( HttpServletRequest request )
    {
        return ( ( request.getParameter( PARAM_SEARCH ) != null ) &&
        !request.getParameter( PARAM_SEARCH ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) );
    }

    /**
     * Indique si une requête de recherche multi-critère est valide, c'est à dire
     * qu'au moins un type de document est choisi et au moins une formation de conseil est choisie;
     * @param request la requête
     * @return <b>true</b> si la requête est valide, <b>false</b> sinon
     */
    private boolean isMultiCriteriaValideForm( HttpServletRequest request )
    {
        return ( ( request.getParameterValues( PARAM_CONSEIL ) != null ) &&
        ( request.getParameterValues( PARAM_CONSEIL ).length != 0 ) ) &&
        ( ( request.getParameter( PARAM_PROJET ) != null ) || ( request.getParameter( PARAM_PROPOSITION ) != null ) ||
        ( request.getParameter( PARAM_VOEU ) != null ) || ( request.getParameter( PARAM_AMENDEMENT ) != null ) );
    }

    /**
     * Vérifie que le champ de recherche par mot-clé ne contient pas de caractère spécial qui altèrerait le
     * résultat de la recherche dans l'index Lucene
     * @param request la requête
     * @return <b>true</b> si le champ de recherche est correct, <b>false</b> sinon
     */
    private boolean isChampMotCleValide( HttpServletRequest request )
    {
        String strRequete = request.getParameter( PARAM_SEARCH );

        if ( ( strRequete != null ) && !strRequete.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            String strDelimiteurs = "&~|!*?+-(){}[]^:\"";
            String[] tabMotsExclus = 
                {
                    "&", "&", "|", "|", "~", "!", "*", "?", "+", "-", "(", ")", "{", "}", "[", "]", "^", ":", "\"", "\\",
                    "AND", "and", "OR", "or",
                };
            List<String> listMotsExclus = new ArrayList<String>(  );

            for ( String strMotExclu : tabMotsExclus )
            {
                listMotsExclus.add( strMotExclu );
            }

            StringTokenizer stringTokenizer = new StringTokenizer( strRequete, strDelimiteurs, true );

            while ( stringTokenizer.hasMoreTokens(  ) )
            {
                if ( listMotsExclus.contains( stringTokenizer.nextToken(  ) ) )
                {
                    return false;
                }
            }
        }

        return true;
    }
}
