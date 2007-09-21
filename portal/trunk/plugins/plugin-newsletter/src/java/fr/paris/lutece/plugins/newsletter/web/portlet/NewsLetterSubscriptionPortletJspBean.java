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
package fr.paris.lutece.plugins.newsletter.web.portlet;

import fr.paris.lutece.plugins.newsletter.business.NewsLetter;
import fr.paris.lutece.plugins.newsletter.business.NewsLetterHome;
import fr.paris.lutece.plugins.newsletter.business.portlet.NewsLetterSubscriptionPortlet;
import fr.paris.lutece.plugins.newsletter.business.portlet.NewsLetterSubscriptionPortletHome;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;

//import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage newsletter subscription portlets.
 */
public class NewsLetterSubscriptionPortletJspBean extends PortletJspBean
{
    // Prefix of the properties related to this checkbox
    private static final String PROPERTIES_PREFIX = "portlet.newsletter_subscription";

    // Prefix used to generate checkbox names
    private static final String PREFIX_CHECKBOX_NAME = "cbx_snd_";

    // Bookmarks
    private static final String BOOKMARK_PAGE_ID = "@page_id@";
    private static final String BOOKMARK_PORTLET_ID = "@portlet_id@";

    // Parameters
    private static final String PARAMETER_PAGE_ID = "page_id";
    private static final String PARAMETER_PORTLET_ID = "portlet_id";
    private static final String PARAMETER_PORTLET_TYPE_ID = "portlet_type_id";

    // Templates
    private static final String MARK_NEWSLETTER_LIST = "subscribed_newsletter_list";
    private static final String MARK_SELECTED_NEWSLETTER_LIST = "selected_newsletter_list";

    /**
     * Returns the creation form for the portlet
     *
     * @param request the HTML request
     * @return the HTML code for the page
     */
    public String getCreate( HttpServletRequest request )
    {
        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        String strPortletTypeId = request.getParameter( PARAMETER_PORTLET_TYPE_ID );

        HtmlTemplate template = getCreateTemplate( strPageId, strPortletTypeId );

        return template.getHtml(  );
    }

    /**
     * Processes the creation of the portlet
     *
     * @param request the HTML request
     * @return the URL to redirect to
     */
    public String doCreate( HttpServletRequest request )
    {
        NewsLetterSubscriptionPortlet portlet = new NewsLetterSubscriptionPortlet(  );

        // Standard controls on the creation form
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        int nIdPage = Integer.parseInt( strIdPage );

        String strStyleId = request.getParameter( Parameters.STYLE );

        if ( ( strStyleId == null ) || strStyleId.trim(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        setPortletCommonData( request, portlet );

        // mandatory field
        String strName = portlet.getName(  );

        if ( strName.trim(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        portlet.setPageId( nIdPage );

        // Creating portlet
        NewsLetterSubscriptionPortletHome.getInstance(  ).create( portlet );

        // Returns page with new created portlet
        return getHomeUrl( request );
    }

    /**
     * Returns the modification form for the portlet
     *
     * @param request the HTML request
     * @return the HTML code for the page
     */
    public String getModify( HttpServletRequest request )
    {
        // Use the id in the request to load the portlet
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        NewsLetterSubscriptionPortlet portlet = (NewsLetterSubscriptionPortlet) PortletHome.findByPrimaryKey( nPortletId );

        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );

        HashMap model = new HashMap(  );
        model.put( BOOKMARK_PORTLET_ID, strPortletId );
        model.put( BOOKMARK_PAGE_ID, strIdPage );

        Plugin plugin = PluginService.getPlugin( portlet.getPluginName(  ) );

        Collection<NewsLetter> colNewsLetter = NewsLetterHome.findAll( plugin );
        colNewsLetter = AdminWorkgroupService.getAuthorizedCollection( colNewsLetter, getUser(  ) );

        Set selectedNewsletterList = NewsLetterSubscriptionPortletHome.findSelectedNewsletters( nPortletId );
        model.put( MARK_NEWSLETTER_LIST, colNewsLetter );
        model.put( MARK_SELECTED_NEWSLETTER_LIST, selectedNewsletterList );

        // Fill the specific part of the modify form
        HtmlTemplate template = getModifyTemplate( portlet, model );

        return template.getHtml(  );
    }

    /**
     * Processes the modification of the portlet
     *
     * @param request the HTTP request
     * @return the URL to redirect to
     */
    public String doModify( HttpServletRequest request )
    {
        // Use the id in the request to load the portlet
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        NewsLetterSubscriptionPortlet portlet = (NewsLetterSubscriptionPortlet) PortletHome.findByPrimaryKey( nPortletId );

        // Standard controls on the creation form
        String strStyleId = request.getParameter( Parameters.STYLE );

        if ( ( strStyleId == null ) || strStyleId.trim(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        setPortletCommonData( request, portlet );

        // mandatory field
        String strName = portlet.getName(  );

        if ( strName.trim(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Update generic values
        portlet.update(  );

        // Update the selected subscriptions
        modifySubscriptions( request, portlet );

        // Returns page with new created portlet
        return getHomeUrl( request );
    }

    /**
     * Returns portlet's properties prefix
     *
     * @return prefix
     */
    public String getPropertiesPrefix(  )
    {
        return PROPERTIES_PREFIX;
    }

    /**
     * Helper method to determine which subscriptions were checked in the portlet
     * modification form, and update the database accordingly.
     *
     * @param request the HTTP request
     * @param portlet the portlet
     */
    private static void modifySubscriptions( HttpServletRequest request, NewsLetterSubscriptionPortlet portlet )
    {
        // Build the set of the subscriptions that were checked in the page
        Set<Integer> checkedSubscriptions = new HashSet<Integer>(  );

        // Read all request parameters
        Enumeration enumParameterNames = request.getParameterNames(  );

        while ( enumParameterNames.hasMoreElements(  ) )
        {
            String strParameterName = (String) enumParameterNames.nextElement(  );

            // If parameter is a subscription checkbox
            if ( strParameterName.startsWith( PREFIX_CHECKBOX_NAME ) )
            {
                // Extract the int value concatenated to the prefix
                String strSubscriptionId = strParameterName.substring( PREFIX_CHECKBOX_NAME.length(  ) );

                // Add the Integer object to the set
                checkedSubscriptions.add( new Integer( strSubscriptionId ) );
            }
        }

        // Build the set of the subscriptions that were previously associated to the
        // portlet
        Set<Integer> previousSubscriptions = NewsLetterSubscriptionPortletHome.findSelectedNewsletters( portlet.getId(  ) );

        // Add the subscriptions that are checked now but were not present before
        for ( Integer newSubscription : checkedSubscriptions )
        {
            if ( !previousSubscriptions.contains( newSubscription ) )
            {
                NewsLetterSubscriptionPortletHome.insertSubscription( portlet.getId(  ), newSubscription.intValue(  ) );
            }
        }

        // Remove the subscriptions that were present before but are unchecked now
        for ( Integer oldSubscription : previousSubscriptions )
        {
            if ( !checkedSubscriptions.contains( oldSubscription ) )
            {
                NewsLetterSubscriptionPortletHome.removeNewsletter( portlet.getId(  ), oldSubscription.intValue(  ) );
            }
        }
    }
}
