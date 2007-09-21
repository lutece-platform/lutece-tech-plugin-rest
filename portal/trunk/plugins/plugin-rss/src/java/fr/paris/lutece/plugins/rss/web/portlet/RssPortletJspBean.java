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
package fr.paris.lutece.plugins.rss.web.portlet;

import fr.paris.lutece.plugins.rss.business.RssFeedHome;
import fr.paris.lutece.plugins.rss.business.portlet.RssPortlet;
import fr.paris.lutece.plugins.rss.business.portlet.RssPortletHome;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage Rss Portlet features
 */
public class RssPortletJspBean extends PortletJspBean
{
    ///////////////////////////////////////////////////////////////////////////////////
    // Constants

    /**
     * The rights required to use RssPortletJspBean
     */
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";

    ////////////////////////////////
    private static final String PARAMETER_PAGE_ID = "page_id";
    private static final String PARAMETER_PORTLET_ID = "portlet_id";
    private static final String PARAMETER_PORTLET_TYPE_ID = "portlet_type_id";
    private static final String PARAMETER_FEED_ID = "feed_id";
    private static final String COMBO_FEED_LIST = "@combo_feeds@";
    private static final String MARK_FEED_LIST = "feed_list";
    private static final String MARK_FEED_ID = "default_feed_id";

    //////////////////////////////////////////////////////////////////////////////////
    //Templates
    private static final String TEMPLATE_COMBO_FEEDS = "admin/plugins/rss/portlet/combo_feed.html";

    /**
     * Returns the properties prefix used for rss portlet and defined in lutece.properties file
     *
     * @return the value of the property prefix
     */
    public String getPropertiesPrefix(  )
    {
        return "portlet.rss";
    }

    /**
     * Returns the Rss Portlet form of creation
     *
     * @param request The Http rquest
     * @return the html code of the rss portlet form
     */
    public String getCreate( HttpServletRequest request )
    {
        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        String strPortletTypeId = request.getParameter( PARAMETER_PORTLET_TYPE_ID );
        HtmlTemplate template = getCreateTemplate( strPageId, strPortletTypeId );
        ReferenceList listFeeds = RssFeedHome.getRssFeedsReferenceList(  );
        String strHtmlCombo = getFeedIndexCombo( listFeeds, "" );
        template.substitute( COMBO_FEED_LIST, strHtmlCombo );

        return template.getHtml(  );
    }

    /**
     * Returns the Rss Portlet form for update
     * @param request The Http request
     * @return the html code of the rss portlet form
     */
    public String getModify( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        RssPortlet portlet = (RssPortlet) PortletHome.findByPrimaryKey( nPortletId );
        HtmlTemplate template = getModifyTemplate( portlet );

        // fills the template with specific values
        ReferenceList listFeeds = RssFeedHome.getRssFeedsReferenceList(  );
        String strHtmlCombo = getFeedIndexCombo( listFeeds, portlet.getRssFeedId(  ) );
        template.substitute( COMBO_FEED_LIST, strHtmlCombo );

        return template.getHtml(  );
    }

    /**
     * Treats the creation form of a new rss portlet
     *
     * @param request The Http request
     * @return The jsp URL which displays the view of the created rss portlet
     */
    public String doCreate( HttpServletRequest request )
    {
        RssPortlet portlet = new RssPortlet(  );

        // recovers portlet specific attributes
        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        int nPageId = Integer.parseInt( strPageId );
        String strFeedId = request.getParameter( PARAMETER_FEED_ID );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        portlet.setPageId( nPageId );
        portlet.setRssFeedId( strFeedId );

        // Creates the portlet
        RssPortletHome.getInstance(  ).create( portlet );

        //Displays the page with the new Portlet
        return getPageUrl( nPageId );
    }

    /**
     * Treats the update form of the rss portlet whose identifier is in the http request
     *
     * @param request The Http request
     * @return The jsp URL which displays the view of the updated portlet
     */
    public String doModify( HttpServletRequest request )
    {
        // fetches portlet attributes
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        RssPortlet portlet = (RssPortlet) PortletHome.findByPrimaryKey( nPortletId );

        // retrieve portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        // fetches portlet specific attributes
        String strFeedId = request.getParameter( PARAMETER_FEED_ID );
        portlet.setRssFeedId( strFeedId );

        // updates the portlet
        portlet.update(  );

        // displays the page with the updated portlet
        return getPageUrl( portlet.getPageId(  ) );
    }

    /**
     * Return the feed listing depending on rights
     * @param listFeeds list of available rss feeds
     * @param strDefaultFeedId The id of the feed used in the context
     * @return The html code of the combo
     */
    String getFeedIndexCombo( ReferenceList listFeeds, String strDefaultFeedId )
    {
        HashMap model = new HashMap(  );
        model.put( MARK_FEED_LIST, listFeeds );
        model.put( MARK_FEED_ID, strDefaultFeedId );

        HtmlTemplate templateCombo = AppTemplateService.getTemplate( TEMPLATE_COMBO_FEEDS, getLocale(  ), model );

        return templateCombo.getHtml(  );
    }
}
