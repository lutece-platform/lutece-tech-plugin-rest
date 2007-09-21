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
package fr.paris.lutece.plugins.ods.web.indexer;

import fr.paris.lutece.plugins.ods.service.search.indexer.Indexer;
import fr.paris.lutece.plugins.ods.service.search.indexer.IndexerHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * Gestion des indexations complètes
 */
public class FrontIndexerJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_INDEXER = "ODS_INDEXER";
    
    private static final String PARAM_BOUTON_ACTION = "action";
    private static final String MARK_LISTE_INDEXERS = "liste_indexers";
    private static final String TEMPLATE_LISTE_INDEXER = "admin/plugins/ods/indexer/liste_indexer.html";
    private static final String PROPERTY_PAGE_TITLE = "ods.listeindexer.page.title";
    private static final String PARAM_ID_INDEXER = "id_indexer";
    private static final String JSP_DO_DEMANDE_INDEXATION = "jsp/admin/plugins/ods/indexer/DoDemandeIndexation.jsp";
    private static final String MESSAGE_CONFIRM_DEMANDE = "ods.demandeindexation.message.confirmDemande";
    private static final String MESSAGE_CONFIRM_ANNULATION = "ods.demandeindexation.message.confirmAnnulation";
    private static final String MESSAGE_DEMANDE = "ods.listeindexer.button.demande";

    /**
     * Retourne la page de gestion des indexers
     * @param request la requête Http
     * @return la liste des indexers
     */
    public String getListeIndexer( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( MARK_LISTE_INDEXERS, IndexerHome.findAllIndexers( getPlugin(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_INDEXER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Retourne la page de confirmation de demande d'indexation
     * @param request la requête Http
     * @return un message de confirmation
     */
    public String getDemandeIndexation( HttpServletRequest request )
    {
        String strAction = request.getParameter( PARAM_BOUTON_ACTION );
        String strIdIndexer = request.getParameter( PARAM_ID_INDEXER );
        UrlItem url = new UrlItem( JSP_DO_DEMANDE_INDEXATION );
        url.addParameter( PARAM_ID_INDEXER, strIdIndexer );
        url.addParameter( PARAM_BOUTON_ACTION, strAction );

        String strConfirm;

        if ( strAction.equals( I18nService.getLocalizedString( MESSAGE_DEMANDE, request.getLocale(  ) ) ) )
        {
            strConfirm = MESSAGE_CONFIRM_DEMANDE;
        }
        else
        {
            strConfirm = MESSAGE_CONFIRM_ANNULATION;
        }

        return AdminMessageService.getMessageUrl( request, strConfirm, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Demande ou annule la demande d'indexation complète
     * @param request la erquête Http
     * @return la page de gestion des inedexers
     */
    public String doDemandeIndexation( HttpServletRequest request )
    {
        String strAction = request.getParameter( PARAM_BOUTON_ACTION );
        String strIdIndexer = request.getParameter( "id_indexer" );
        int nIdIndexer;

        try
        {
            nIdIndexer = Integer.parseInt( strIdIndexer );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );

            return getHomeUrl( request );
        }

        Indexer indexer = IndexerHome.findByPrimaryKey( nIdIndexer, getPlugin(  ) );

        if ( strAction.equals( I18nService.getLocalizedString( MESSAGE_DEMANDE, request.getLocale(  ) ) ) )
        {
            indexer.setIndexationComplete( true );
        }
        else
        {
            indexer.setIndexationComplete( false );
        }

        IndexerHome.update( indexer, getPlugin(  ) );

        return getHomeUrl( request );
    }
}
