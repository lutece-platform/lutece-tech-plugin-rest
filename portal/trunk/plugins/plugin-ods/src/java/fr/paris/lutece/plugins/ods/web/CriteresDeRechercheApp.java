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

import fr.paris.lutece.plugins.ods.business.utilisateur.Utilisateur;
import fr.paris.lutece.plugins.ods.service.manager.LuteceUserManager;
import fr.paris.lutece.plugins.ods.service.search.requete.RequeteUtilisateur;
import fr.paris.lutece.plugins.ods.service.search.requete.RequeteUtilisateurHome;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * MesCriteresApp
 */
public class CriteresDeRechercheApp implements XPageApplication
{
    private static final String TEMPLATE_MES_CRITERES = "skin/plugins/ods/mes_criteres.html";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.mes_criteres";
    private static final String PROPERTY_PAGE_TITLE = "ods.front.criteres.page.title";
    private static final String PARAM_SAVE_CRITERE = "save";
    private static final String PARAM_NOM_REQUETE = "nom_requete";
    private static final String PARAM_VALUE_NEXT = "next";
    private static final String PARAM_TYPE_REQUETE = "type_requete";
    private static final String PARAM_DATE_1 = "date_1";
    private static final String PARAM_DATE_2 = "date_2";
    private static final String PARAM_CHAMP_RECHERCHE = "champ_recherche";
    private static final String PARAM_TYPES_DOCUMENTS = "selected_types_document";
    private static final String PARAM_FORMATIONS_CONSEIL = "selected_formations_conseil";
    private static final String PARAM_COMMISSIONS = "selected_commissions";
    private static final String PARAM_RAPPORTEURS = "selected_rapporteurs";
    private static final String PARAM_ARRONDISSEMENTS = "selected_arrondissements";
    private static final String PARAM_DIRECTIONS = "selected_directions";
    private static final String PARAM_CATEGORIES_DELIBERATION = "selected_categories_deliberation";
    private static final String PARAM_GROUPES_DEPOSITAIRES = "selected_groupes_depositaires";
    private static final String PARAM_ELUS_DEPOSITAIRES = "selected_elus_depositaires";
    private static final String PARAM_REQUETE = "id_requete";
    private static final String PARAM_TODO = "a_faire";
    private static final String MARK_ERREUR_MESSAGE = "message";
    private static final String MARK_URL_RECHERCHE_RESULTATS = "url_recherche_resultats";
    private static final String MARK_VALUE_URL_RECHERCHE_RESULTATS = "?page=ods_recherche";
    private static final String MARK_URL_RECHERCHE_ARCHIVES_RESULTATS = "url_recherche_archives_resultats";
    private static final String MARK_VALUE_URL_RECHERCHE_ARCHIVES_RESULTATS = "?page=ods_recherche_archives";
    private static final String MARK_URL_FORMULAIRE = "url_formulaire";
    private static final String MARK_VALUE_URL_FORMULAIRE = "?page=ods_recherche";
    private static final String MARK_URL_PROFIL = "url_profil";
    private static final String MARK_VALUE_URL_PROFIL = "?page=ods_mon_profil";
    private static final String MARK_LISTE_REQUETES = "liste_requetes";
    private static final String MARK_UTILISATEUR = "utilisateur";
    private static final String PROPERTY_PAGE_ERREUR_TITRE = "ods.front.error.page.title";
    private static final String PROPERTY_PAGE_ERREUR_REQUETE = "ods.front.error.page.title.requete";
    private static final String TEMPLATE_REQUETE_NON_VALIDE = "skin/plugins/ods/non_valid_search.html";
    private static final String ERROR_NO_REQUETE_NAME = "ods.front.mescriteres.error.noname";
    private static final String ERROR_NO_ACCESS = "ods.front.mescriteres.error.noaccess";
    private static final String CONSTANTE_ALERT = "alert";
    private static final String CONSTANTE_NOTIFIE = "notifie";
    private static final String CONSTANTE_ODS = "ods";

    /**
     * renvoie la page.
     * @param request le requête http
     * @param nMode le mode
     * @param plugin le Plugin actif
     * @return la page
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        // Met à jour la liste des utilisateurs et gère la session de l'utilisateur
        LuteceUserManager.gestionUtilisateur( model, request, plugin );

        // On instancier une nouvelle XPage
        XPage page = new XPage(  );
        Locale locale = request.getLocale(  );

        // Ce paramètre vient de la page de Résultat d'une recherche.
        // Si cette page est appelée avec une requête d'enregistrement de celle-ci,
        // on enregistre la requête de recherche si le nom affecté à celui-ci n'est pas null ou vide
        if ( request.getParameter( PARAM_SAVE_CRITERE ) != null )
        {
            if ( ( request.getParameter( PARAM_NOM_REQUETE ) == null ) ||
                    request.getParameter( PARAM_NOM_REQUETE ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                return getErrorPage( request, ERROR_NO_REQUETE_NAME, locale );
            }

            saveCriteria( request );
        }

        // Ce paramètre vient de cette même page qui se rappelle.
        String strToDo = request.getParameter( PARAM_TODO );

        if ( strToDo != null )
        {
            // On récupère la requête à partir de son identifiant
            int nIdRequete;

            try
            {
                nIdRequete = Integer.parseInt( request.getParameter( PARAM_REQUETE ) );
            }
            catch ( NumberFormatException nfe )
            {
                nIdRequete = -1;
            }

            RequeteUtilisateur requete = RequeteUtilisateurHome.findByPrimaryKey( nIdRequete, plugin );

            // Si la requête n'est pas nulle mais qu'elle n'appartient pas à l'utilisateur courant, 
            // alors on affiche un message d'erreur
            if ( ( requete == null ) ||
                    !requete.getUserName(  )
                                .equals( SecurityService.getInstance(  ).getRegisteredUser( request ).getName(  ) ) )
            {
                return getErrorPage( request, ERROR_NO_ACCESS, locale );
            }

            // Si l'action demandée est d'être alerté, on met à jour la requête en base
            if ( strToDo.equals( CONSTANTE_ALERT ) )
            {
                requete.setNotifie( Boolean.parseBoolean( request.getParameter( CONSTANTE_NOTIFIE ) ) );
                RequeteUtilisateurHome.store( requete, plugin );
            }

            // Sinon l'action demandée est une action de suppression et donc on supprime la requête de la base
            else
            {
                RequeteUtilisateurHome.remove( requete, plugin );
            }
        }

        String strPortalUrl = AppPathService.getPortalUrl(  );

        model.put( MARK_URL_RECHERCHE_RESULTATS, strPortalUrl + MARK_VALUE_URL_RECHERCHE_RESULTATS );
        model.put( MARK_URL_RECHERCHE_ARCHIVES_RESULTATS, strPortalUrl + MARK_VALUE_URL_RECHERCHE_ARCHIVES_RESULTATS );

        List<RequeteUtilisateur> listeRequetes = RequeteUtilisateurHome.findByUser( SecurityService.getInstance(  )
                                                                                                   .getRegisteredUser( request )
                                                                                                   .getName(  ), plugin );

        for ( RequeteUtilisateur requete : listeRequetes )
        {
            requete.toHtml( plugin, locale );
        }

        model.put( MARK_LISTE_REQUETES, listeRequetes );
        model.put( MARK_URL_FORMULAIRE, strPortalUrl + MARK_VALUE_URL_FORMULAIRE );
        model.put( MARK_URL_PROFIL, strPortalUrl + MARK_VALUE_URL_PROFIL );
        
        // On récupère l'utilisateur courant
        Utilisateur user = LuteceUserManager.getUtilisateurCourant( request, plugin );
        model.put( MARK_UTILISATEUR, user );

        // Chargement et affichage de la template
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MES_CRITERES, request.getLocale(  ), model );
        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );
        page.setContent( template.getHtml(  ) );

        // On affecte un titre et un chemin à la XPage
        page.setTitle( strPageTitle );
        page.setPathLabel( strPagePathLabel );

        return page;
    }

    /**
     * Retourne une page d'erreur avec le message adapté
     * @param request la requete Http
     * @param strErrorMessage le message à afficher
     * @param locale la locale du navigateur
     * @return une page d'erreur
     */
    private XPage getErrorPage( HttpServletRequest request, String strErrorMessage, Locale locale )
    {
        XPage page = new XPage(  );
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( MARK_ERREUR_MESSAGE, strErrorMessage );
        model.put( OdsMarks.MARK_USER_GIVEN_NAME, LuteceUserManager.getGivenName( request ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_REQUETE_NON_VALIDE, locale, model );
        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_ERREUR_TITRE, locale ) + " : " +
            I18nService.getLocalizedString( PROPERTY_PAGE_ERREUR_REQUETE, locale );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );
        page.setTitle( strPageTitle );
        page.setContent( template.getHtml(  ) );
        page.setPathLabel( strPagePathLabel );

        return page;
    }

    /**
     * Crée la requete utilisateur à partir de la requete Http et la sauvegarde
     * @param request la requete Http
     */
    private void saveCriteria( HttpServletRequest request )
    {
        if ( SecurityService.getInstance(  ).getRegisteredUser( request ) != null )
        {
            RequeteUtilisateur requeteUtilisateur = new RequeteUtilisateur(  );
            String strUserName = SecurityService.getInstance(  ).getRegisteredUser( request ).getName(  );
            String strNomRequete = request.getParameter( PARAM_NOM_REQUETE );
            requeteUtilisateur.setUserName( strUserName );
            requeteUtilisateur.setNomRequete( strNomRequete );
            requeteUtilisateur.setRechercheArchive( !request.getParameter( PARAM_SAVE_CRITERE ).equals( PARAM_VALUE_NEXT ) );

            requeteUtilisateur.setTypeRequete( request.getParameter( PARAM_TYPE_REQUETE ) );
            requeteUtilisateur.setChampRecherche( request.getParameter( PARAM_CHAMP_RECHERCHE ) );
            requeteUtilisateur.setListeTypesDocument( request.getParameter( PARAM_TYPES_DOCUMENTS ) );
            requeteUtilisateur.setListeFormationsConseil( request.getParameter( PARAM_FORMATIONS_CONSEIL ) );

            String strDate1 = request.getParameter( PARAM_DATE_1 );
            String strDate2 = request.getParameter( PARAM_DATE_2 );

            if ( strDate1 != null )
            {
                requeteUtilisateur.setPremiereDate( OdsUtils.getDate( strDate1, true ) );
            }

            if ( strDate2 != null )
            {
                requeteUtilisateur.setDeuxiemeDate( OdsUtils.getDate( strDate2, true ) );
            }

            requeteUtilisateur.setListeCommissions( request.getParameter( PARAM_COMMISSIONS ) );
            requeteUtilisateur.setListeRapporteurs( request.getParameter( PARAM_RAPPORTEURS ) );
            requeteUtilisateur.setListeArrondissements( request.getParameter( PARAM_ARRONDISSEMENTS ) );
            requeteUtilisateur.setListeDirections( request.getParameter( PARAM_DIRECTIONS ) );
            requeteUtilisateur.setListeCategoriesDeliberation( request.getParameter( PARAM_CATEGORIES_DELIBERATION ) );
            requeteUtilisateur.setListeGroupesDepositaires( request.getParameter( PARAM_GROUPES_DEPOSITAIRES ) );
            requeteUtilisateur.setListeElusDepositaires( request.getParameter( PARAM_ELUS_DEPOSITAIRES ) );
            requeteUtilisateur.setNotifie( false );

            RequeteUtilisateurHome.create( requeteUtilisateur, PluginService.getPlugin( CONSTANTE_ODS ) );
        }
    }
}
