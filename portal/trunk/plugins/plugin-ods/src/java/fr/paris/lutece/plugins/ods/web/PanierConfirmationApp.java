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

import fr.paris.lutece.plugins.ods.business.panier.Panier;
import fr.paris.lutece.plugins.ods.business.panier.PanierHome;
import fr.paris.lutece.plugins.ods.service.manager.LuteceUserManager;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * PanierConfirmationApp: a pour responsabilité d'afficher la popup de téléchargement du panier
 */
public class PanierConfirmationApp implements XPageApplication
{
    private static final String TEMPLATE_SELECTION = "skin/plugins/ods/selection.html";
    private static final String TEMPLATE_SELECTION_KO = "skin/plugins/ods/selectionKO.html";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.panier";
    private static final String PROPERTY_PAGE_TITLE = "ods.front.panier.page.title";
    private static final String MARK_ZIP_IS_FINISHED = "isZipFinished";
    private static final String MARK_ZIP_ID_DOWNLOAD = "idToDownload";
    private static final String MARK_URL_IMAGE = "urlImage";
    private static final String MARK_TAILLE_TOTAL_MAX = "tailleTotalMax";
    private static final String MARK_URL_DOWNLOAD_PANIER = "url_download_panier";
    private static final String PROPERTY_TAILLE_TOTAL_MAX = "taille_total_max_telechargement";
    private static final int DEFAULT_PROPERTY_TAILLE_TOTAL_MAX = 22020096;
    private static final String CONSTANTE_DATEAJOUT_NULL = "null";
    private static final String URL_IMAGE = "images/local/skin/plugins/ods/dot3_progress.gif";

    /* Variables de session */
    private String _dateAjout;

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

        LuteceUserManager.gestionUtilisateur( model, request, plugin );

        XPage page = new XPage(  );
        Locale locale = request.getLocale(  );

        /* Récupère la date de la demande de téléchargment du panier */
        _dateAjout = request.getParameter( PanierApp.MARK_DATE_AJOUT );

        Panier panier = null;

        /* Si la date d'ajout est correcte alors on vérifie si l'archive est présente dans la base de données */
        if ( ( _dateAjout != null ) && !CONSTANTE_DATEAJOUT_NULL.equals( _dateAjout ) )
        {
        	panier = PanierHome.findZipByDateAjout( _dateAjout, plugin );
            if ( panier != null )
            {
                model.put( MARK_ZIP_IS_FINISHED, true );
                model.put( MARK_ZIP_ID_DOWNLOAD, panier.getIdToDownload(  ) );
            }
            else
            {
                model.put( MARK_ZIP_IS_FINISHED, false );
            }

            model.put( MARK_URL_IMAGE, AppPathService.getBaseUrl( request ) + URL_IMAGE );
            
            /* Gestion du lien externe vers la webapp de telechargement du panier */
            String url = AppPropertiesService.getProperty( PanierApp.PROPERTY_TELECHARGEMENT_HOST ) + "/" +
    			AppPropertiesService.getProperty( PanierApp.PROPERTY_TELECHARGEMENT_WEBAPP ) + "/" +
    			AppPropertiesService.getProperty( PanierApp.PROPERTY_TELECHARGEMENT_SERVLET_PANIER ) + 
    			"?id=";
            model.put( MARK_URL_DOWNLOAD_PANIER, url);

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SELECTION, request.getLocale(  ), model );
            String strPagetitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale );
            String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );
            page.setContent( template.getHtml(  ) );
            page.setTitle( strPagetitle );
            page.setPathLabel( strPagePathLabel );
        }
        else
        {
            /* Sinon on indique à l'utilisateur qu'une erreur a eu lieu */
            if ( _dateAjout != null )
            {
                model.put( MARK_TAILLE_TOTAL_MAX,
                    AppPropertiesService.getPropertyInt( PROPERTY_TAILLE_TOTAL_MAX, DEFAULT_PROPERTY_TAILLE_TOTAL_MAX ) / 10000 );

                HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SELECTION_KO, request.getLocale(  ),
                        model );
                String strPagetitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale );
                String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );
                page.setContent( template.getHtml(  ) );
                page.setTitle( strPagetitle );
                page.setPathLabel( strPagePathLabel );
            }
        }

        return page;
    }
}
