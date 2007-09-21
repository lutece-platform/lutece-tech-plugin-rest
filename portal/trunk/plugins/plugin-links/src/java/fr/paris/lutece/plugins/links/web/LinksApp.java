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
package fr.paris.lutece.plugins.links.web;

import fr.paris.lutece.plugins.links.business.Link;
import fr.paris.lutece.plugins.links.business.LinkHome;
import fr.paris.lutece.plugins.links.business.portlet.LinksPortletHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * This class implements the links XPage
 */
public class LinksApp implements XPageApplication
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String PROPERTY_LINKS_PATHLABEL = "links.xPageLinks.pagePathLabel";
    private static final String PROPERTY_LINKS_TITLE = "links.xPageLinks.pageTitle";
    private static final String TEMPLATE_PORTLET_BLOCK = "skin/plugins/links/block_portlet.html";
    private static final String TEMPLATE_XPAGE_LINKS = "skin/plugins/links/page_links_summary.html";
    private static final String MARK_PORTLET_NAME = "portlet_name";
    private static final String MARK_LINKS_LIST = "links_list";
    private static final String MARK_PORTLETS_LIST = "list_portlets";

    /**
     * Crée un nouvel objet LinksApp
     */
    public LinksApp(  )
    {
    }

    /**
     * Returns the Links Page content depending on the request parameters and the current mode.
     *
     * @param request The HTTP request.
     * @param nMode The current mode.
     * @param plugin The plugin
     * @return The page content.
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        String strServerKey = AppPathService.getVirtualHostKey( request );
        XPage page = new XPage(  );

        page.setContent( getPortletBlock( request, strServerKey ) );
        page.setTitle( I18nService.getLocalizedString( PROPERTY_LINKS_TITLE, request.getLocale(  ) ) );
        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_LINKS_PATHLABEL, request.getLocale(  ) ) );

        return page;
    }

    /**
     * Return all the links portlets
     *
     * @param strServerKey the virtual host key or null for default host
     * @return The Html block portlet
     */
    public String getPortletBlock( HttpServletRequest request, String strServerKey )
    {
        StringBuffer strBlocPortlet = new StringBuffer(  );
        Collection<Portlet> listePortlet = LinksPortletHome.getPortletsInLinksPage(  );
        Collection<Link> listLinksForVhost = new ArrayList<Link>(  );
        HashMap<String, String> modelTemplateXpageLinks = new HashMap<String, String>(  );

        for ( Portlet portlet : listePortlet )
        {
            int nPortletId = portlet.getId(  );
            HashMap model = new HashMap(  );
            Collection<Link> listLinks = LinkHome.findByPortlet( nPortletId );

            for ( Link link : listLinks )
            {
                String urlVhost = link.getUrl( strServerKey );

                if ( urlVhost != null )
                {
                    link.setUrl( urlVhost );
                    listLinksForVhost.add( link );
                }
            }

            if ( listLinksForVhost.size(  ) > 0 )
            {
                model.put( MARK_LINKS_LIST, listLinksForVhost );
                model.put( MARK_PORTLET_NAME, portlet.getName(  ) );

                HtmlTemplate tListLinks = AppTemplateService.getTemplate( TEMPLATE_PORTLET_BLOCK,
                        request.getLocale(  ), model );
                strBlocPortlet.append( tListLinks.getHtml(  ) );
                listLinksForVhost.clear(  );
            }
        }

        modelTemplateXpageLinks.put( MARK_PORTLETS_LIST, strBlocPortlet.toString(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_LINKS, request.getLocale(  ),
                modelTemplateXpageLinks );

        return template.getHtml(  );
    }
}
