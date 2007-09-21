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
package fr.paris.lutece.plugins.rss.web;

import fr.paris.lutece.plugins.rss.business.RssFeed;
import fr.paris.lutece.plugins.rss.business.RssFeedHome;
import fr.paris.lutece.plugins.rss.business.portlet.RssPortletHome;
import fr.paris.lutece.plugins.rss.service.RssContentLoader;
import fr.paris.lutece.plugins.rss.service.RssContentService;
import fr.paris.lutece.plugins.rss.service.RssParsingException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author Pierre
 */
public class RssFeedsJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_RSS_FEEDS_MANAGEMENT = "RSS_FEEDS_MANAGEMENT";

    // Templates
    private static final String TEMPLATE_FEEDS = "admin/plugins/rss/manage_rss_feeds.html";
    private static final String TEMPLATE_CREATE_RSS_FEED = "admin/plugins/rss/create_rss_feed.html";
    private static final String TEMPLATE_MODIFY_RSS_FEED = "admin/plugins/rss/modify_rss_feed.html";

    // Parameters
    private static final String PARAMETER_RSS_FEED_ID = "id_rss_feed";
    private static final String PARAMETER_RSS_FEED_NAME = "rss_feed_name";
    private static final String PARAMETER_RSS_FEED_URL = "rss_feed_url";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_ITEMS_PER_PAGE = "items_per_page";

    // Properties
    private static final String PROPERTY_PAGE_TITLE_FEEDS = "rss.manage_rss_feeds.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE = "rss.create_rss_feed.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY = "rss.modify_rss_feed.pageTitle";
    private static final String PROPERTY_RSS_PER_PAGE = "rss.rssPerPage";

    //Bookmarks
    private static final String MARK_RSS_FEEDS_LIST = "rss_feeds_list";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";

    // JSP
    private static final String JSP_DELETE_RSS_FEED = "jsp/admin/plugins/rss/DoDeleteRssFeed.jsp";

    //Messages
    private static final String MESSAGE_CONFIRM_DELETE_RSS_FEED = "rss.message.confirmDeleteRssFeed";
    private static final String MESSAGE_RSS_LINKED_PORTLET = "rss.message.linkedToPortlet";

    //Variables
    private int _nItemsPerPage;
    private String _strCurrentPageIndex;

    /**
     * Returns external rss feeds management form
     *
     * @param request The Http request
     * @return Html form
     */
    public String getManageRssFeeds( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_FEEDS );
        _nItemsPerPage = getItemsPerPage( request );
        _strCurrentPageIndex = getPageIndex( request );

        List listRssFeedList = RssFeedHome.getRssFeeds(  );
        Paginator paginator = new Paginator( listRssFeedList, _nItemsPerPage, getHomeUrl( request ),
                PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        HashMap model = new HashMap(  );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_RSS_FEEDS_LIST, paginator.getPageItems(  ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_FEEDS, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Confirms the removal of a feed
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doConfirmDeleteRssFeed( HttpServletRequest request )
    {
        String strIdFeed = request.getParameter( PARAMETER_RSS_FEED_ID );
        int nIdFeed = Integer.parseInt( strIdFeed );
        String strDeleteUrl = JSP_DELETE_RSS_FEED + "?" + PARAMETER_RSS_FEED_ID + "=" + strIdFeed;
        String strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_RSS_LINKED_PORTLET, AdminMessage.TYPE_STOP );

        if ( checkNoPortletLinked( nIdFeed ) )
        {
            RssFeed rss = RssFeedHome.findByPrimaryKey( nIdFeed );
            Object[] messageArgs = { rss.getName(  ) };
            strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_RSS_FEED, messageArgs,
                    strDeleteUrl, AdminMessage.TYPE_CONFIRMATION );
        }

        return strUrl;
    }

    /**
     * Returns the creation form of a feed
     *
     * @param request The Http request
     * @return Html form
     */
    public String getCreateRssFeed( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_RSS_FEED, getLocale(  ), null );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form of a new rss feed
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    public String doCreateRssFeed( HttpServletRequest request )
    {
        RssFeed rss = new RssFeed(  );
        rss.setName( request.getParameter( PARAMETER_RSS_FEED_NAME ) );
        rss.setUrl( request.getParameter( PARAMETER_RSS_FEED_URL ) );

        // Mandatory fields
        if ( request.getParameter( PARAMETER_RSS_FEED_NAME ).equals( "" ) ||
                request.getParameter( PARAMETER_RSS_FEED_URL ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        RssFeedHome.create( rss );

        // Load the content to fetch the RSS feed a first time
        RssContentService.getInstance(  ).getRssContent( "" + rss.getId(  ) );

        return getHomeUrl( request );
    }

    /**
     * Process modification of a feed
     * @param request The Http request
     * @return String The url of the administration console
     */
    public String doModifyRssFeed( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_RSS_FEED_ID ) );
        String strFeedUrl = request.getParameter( PARAMETER_RSS_FEED_URL );
        String strFeedName = request.getParameter( PARAMETER_RSS_FEED_NAME );
        RssFeed rss = RssFeedHome.findByPrimaryKey( nId );
        rss.setName( strFeedName );
        rss.setUrl( strFeedUrl );

        // Mandatory fields
        if ( request.getParameter( PARAMETER_RSS_FEED_URL ).equals( "" ) ||
                request.getParameter( PARAMETER_RSS_FEED_NAME ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        RssFeedHome.update( rss );

        return getHomeUrl( request );
    }

    /**
     * Returns the form to update info about a rss feed
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    public String getModifyRssFeed( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY );

        int nId = Integer.parseInt( request.getParameter( PARAMETER_RSS_FEED_ID ) );
        RssFeed rssFeed = RssFeedHome.findByPrimaryKey( nId );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_RSS_FEED, getLocale(  ), rssFeed );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process removal of a feed
     * @param request The Http request
     * @return String The url of the administration console
     */
    public String doDeleteRssFeed( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_RSS_FEED_ID ) );
        RssFeedHome.remove( nId );

        return getHomeUrl( request );
    }

    /**
     * Process reloading of a feed
     * @param request The Http request
     * @return String The url of the administration console
     */
    public String doFetchRssFeed( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_RSS_FEED_ID ) );

        try
        {
            RssContentLoader.fetchRssFeed( nId );
        }
        catch ( RssParsingException ex )
        {
            AppLogService.error( ex.getMessage(  ) );
        }

        return getHomeUrl( request );
    }

    /**
     * Used by the paginator to fetch a number of items
     * @param request The HttpRequest
     * @return The number of items
     */
    private int getItemsPerPage( HttpServletRequest request )
    {
        int nItemsPerPage;
        String strItemsPerPage = request.getParameter( PARAMETER_ITEMS_PER_PAGE );

        if ( strItemsPerPage != null )
        {
            nItemsPerPage = Integer.parseInt( strItemsPerPage );
        }
        else
        {
            if ( _nItemsPerPage != 0 )
            {
                nItemsPerPage = _nItemsPerPage;
            }
            else
            {
                nItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_RSS_PER_PAGE, 10 );
            }
        }

        return nItemsPerPage;
    }

    /**
     * Fetches the page index
     * @param request The HttpRequest
     * @return The PageIndex
     */
    private String getPageIndex( HttpServletRequest request )
    {
        String strPageIndex = request.getParameter( PARAMETER_PAGE_INDEX );
        strPageIndex = ( strPageIndex != null ) ? strPageIndex : _strCurrentPageIndex;

        return strPageIndex;
    }

    /**
     * Checks if the feed is linked to a portlet
     * @param nIdRssFeed The id of the rss feed
     * @return boolean
     */
    public boolean checkNoPortletLinked( int nIdRssFeed )
    {
        return RssPortletHome.checkNoPortletLinked( nIdRssFeed );
    }
}
