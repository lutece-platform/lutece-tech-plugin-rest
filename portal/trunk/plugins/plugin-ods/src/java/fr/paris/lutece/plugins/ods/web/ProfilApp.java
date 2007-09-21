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
import fr.paris.lutece.plugins.ods.business.utilisateur.UtilisateurHome;
import fr.paris.lutece.plugins.ods.service.manager.LuteceUserManager;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * MonProfilApp
 */
public class ProfilApp implements XPageApplication
{
    private static final String TEMPLATE_MON_PROFIL = "skin/plugins/ods/mon_profil.html";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.mon_profil";
    private static final String PROPERTY_PAGE_TITLE = "ods.front.profil.page.title";
    private static final String PROPERTY_URL_PASSWORD = "profil.urlPassword";
    private static final String MARK_UTILISATEUR = "utilisateur";
    private static final String MARK_PASSWORD = "password";
    private static final String CONSTANTE_ENREGISTRER = "enregistrer";

    /**
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

        // On récupère l'utilisateur courant
        Utilisateur user = LuteceUserManager.getUtilisateurCourant( request, plugin );

        // Si on affiche cette page suite à une requête d'enregistrement,
        // alors on sauvegarde en base les adresses mail de copie.
        if ( request.getParameter( CONSTANTE_ENREGISTRER ) != null )
        {
            if ( ( request.getParameter( OdsParameters.MAIL_COPIE_1 ) != null ) &&
                    !request.getParameter( OdsParameters.MAIL_COPIE_1 ).trim(  )
                                .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                user.setMailCopie1( request.getParameter( OdsParameters.MAIL_COPIE_1 ).trim(  ) );
            }
            else
            {
                user.setMailCopie1( null );
            }

            if ( ( request.getParameter( OdsParameters.MAIL_COPIE_2 ) != null ) &&
                    !request.getParameter( OdsParameters.MAIL_COPIE_2 ).trim(  )
                                .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                user.setMailCopie2( request.getParameter( OdsParameters.MAIL_COPIE_2 ).trim(  ) );
            }
            else
            {
                user.setMailCopie2( null );
            }

            UtilisateurHome.update( user, plugin );
        }

        // On instancie une nouvelle XPage
        XPage page = new XPage(  );
        Locale locale = request.getLocale(  );

        model.put( MARK_UTILISATEUR, user );
        
        String strUrlPassword = AppPropertiesService.getProperty( PROPERTY_URL_PASSWORD );
        model.put( MARK_PASSWORD, strUrlPassword );

        // Chargement et affichage de la template
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MON_PROFIL, request.getLocale(  ), model );
        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );
        page.setContent( template.getHtml(  ) );

        // on affecte un titre et un chemin à la XPage
        page.setTitle( strPageTitle );
        page.setPathLabel( strPagePathLabel );

        return page;
    }
}
