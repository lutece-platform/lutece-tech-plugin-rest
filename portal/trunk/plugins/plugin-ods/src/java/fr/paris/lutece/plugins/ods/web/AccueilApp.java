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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.utilisateur.Utilisateur;
import fr.paris.lutece.plugins.ods.service.manager.LuteceUserManager;
import fr.paris.lutece.plugins.ods.service.search.requete.RequeteUtilisateurHome;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * AccueilApp
 */
public class AccueilApp implements XPageApplication
{
    private static final String PROPERTY_PAGE_TITLE = "ods.front.accueil.page.title";
    private static final String TEMPLATE_ACCUEIL = "skin/plugins/ods/accueil.html";
    private static final String MARK_URL_PDD_LIST_NEW = "url_pdd_list_new";
    private static final String MARK_VALUE_URL_PDD_LIST_NEW = "ods_pdd_list_new";
    private static final String MARK_URL_CALENDRIER = "url_calendrier";
    private static final String MARK_VALUE_URL_CALENDRIER = "ods_calendrier";
    private static final String MARK_URL_RECHERCHE_RESULTATS = "url_recherche_resultats";
    private static final String MARK_VALUE_URL_RECHERCHE_RESULTATS = "ods_recherche";
    private static final String MARK_URL_CRITERES = "url_mes_criteres";
    private static final String MARK_VALUE_URL_CRITERES = "ods_mes_criteres";
    private static final String MARK_LISTE_REQUETES = "liste_requetes";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.accueil";
    private static final String MARK_URL_PARIS_FR = "url_paris_fr";
    private static final String MARK_NBR_PROJETS = "nbr_projets";
    private static final String MARK_NBR_PROPOSITIONS = "nbr_propositions";
    private static final String MARK_DATE_DERNIERE_CONNEXION = "date_derniere_connexion";
    private static final String URL_PARIS_FR = "http://www.paris.fr";

    /**
     * @param request la requête HTTP
     * @param nMode le mode
     * @param plugin le plugin
     * @return XPage template
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        // Met à jour la liste des utilisateurs et gère la session de l'utilisateur
        LuteceUserManager.gestionUtilisateur( model, request, plugin );

        // On instancie la XPage
        XPage page = new XPage(  );
        Locale locale = request.getLocale(  );
        String strPortalUrl = AppPathService.getPortalUrl(  ) + "?page=";

        // On met dans la Hashmap les liens vers les pages accessibles depuis la page d'acceuil
        model.put( MARK_URL_PDD_LIST_NEW, strPortalUrl + MARK_VALUE_URL_PDD_LIST_NEW );
        model.put( MARK_URL_CALENDRIER, strPortalUrl + MARK_VALUE_URL_CALENDRIER );
        model.put( MARK_URL_RECHERCHE_RESULTATS, strPortalUrl + MARK_VALUE_URL_RECHERCHE_RESULTATS );
        model.put( MARK_URL_CRITERES, strPortalUrl + MARK_VALUE_URL_CRITERES );

        // On met dans la Hashmap la liste des requêtes de l'utilisateur
        if ( SecurityService.getInstance(  ).getRegisteredUser( request ) != null )
        {
            model.put( MARK_LISTE_REQUETES,
                RequeteUtilisateurHome.findByUser( SecurityService.getInstance(  ).getRegisteredUser( request ).getName(  ),
                    plugin ) );
        }

        // On met dans la Hashmap l'url du site paris.fr
        model.put( MARK_URL_PARIS_FR, URL_PARIS_FR );

        // On récupère le nombre de projets et du nombre de propositions nouvellements créés
        Seance seance = SeanceHome.getProchaineSeance( plugin );
        int nNbrProjets = 0;
        int nNbrPropositions = 0;

        // On récupère le nombre de Projets et de Propositions de délibération de la prochaine séance
        // et on ajoute ces données dans la Hashmap
        if ( seance != null )
        {
            List<PDD> pdds = OdsUtils.getNouveauxPddsPublies( request, plugin );

            if ( pdds != null )
            {
                for ( PDD pdd : pdds )
                {
                    if ( pdd.getTypePdd(  ).equals( PDD.CONSTANTE_TYPE_PROJET ) )
                    {
                        nNbrProjets++;
                    }
                    else
                    {
                        nNbrPropositions++;
                    }
                }
            }
        }

        model.put( MARK_NBR_PROJETS, nNbrProjets );
        model.put( MARK_NBR_PROPOSITIONS, nNbrPropositions );

        // On met dans la Hashmap la date de dernière connexion de l'utilisateur courant 
        Utilisateur utilisateur = LuteceUserManager.getUtilisateurCourant( request, plugin );

        if ( utilisateur != null )
        {
            model.put( MARK_DATE_DERNIERE_CONNEXION, utilisateur.getDerniereConnexion(  ) );
        }

        // On appelle la template et on l'affecte à la XPage
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ACCUEIL, request.getLocale(  ), model );
        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );
        page.setContent( template.getHtml(  ) );

        // On affecte un titre et un chemin à la XPage
        page.setTitle( strPageTitle );
        page.setPathLabel( strPagePathLabel );

        return page;
    }
}
