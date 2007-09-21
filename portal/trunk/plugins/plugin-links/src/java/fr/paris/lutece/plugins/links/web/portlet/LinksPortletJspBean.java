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
package fr.paris.lutece.plugins.links.web.portlet;

import fr.paris.lutece.plugins.links.business.Link;
import fr.paris.lutece.plugins.links.business.LinkHome;
import fr.paris.lutece.plugins.links.business.portlet.LinksPortlet;
import fr.paris.lutece.plugins.links.business.portlet.LinksPortletHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage Link Portlet
 */
public class LinksPortletJspBean extends PortletJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";

    // JSP
    private static final String JSP_DO_MODIFY_PORTLET = "../../DoModifyPortlet.jsp";

    // Templates
    private static final String TEMPLATE_LINKS_LIST = "/admin/plugins/links/links_list_portlet.html";
    private static final String TEMPLATE_UNSELECTED_LINKS = "/admin/plugins/links/unselected_links.html";

    // Parameters
    private static final String PARAMETER_LINKS_ORDER = "links_order";
    private static final String PARAMETER_PORTLET_ID = "portlet_id";
    private static final String PARAMETER_PAGE_ID = "page_id";
    private static final String PARAMETER_PORTLET_TYPE_ID = "portlet_type_id";
    private static final String PARAMETER_LINKS = "links";
    private static final String PARAMETER_LINK_ID = "link_id";

    // Properties
    private static final String PROPERTY_NO_IMAGE_ADMIN = "links.no.image.admin";
    private static final String MESSAGE_PORTLET_LINK_SELECTED = "links.message.already_selected";

    // Marker
    private static final String MARK_LINKS_LIST = "links_list";
    private static final String MARK_LINK = "link";
    private static final String MARK_LINK_DEFAULT_ORDER = "link_default_order";
    private static final String MARK_ORDER_LIST = "order_list";
    private static final String MARK_PORTLET_ID = "portlet_id";
    private static final String MARK_NEW_LINK = "new_link";
    private static final String MARK_DEFAULT_LINK = "default_link";
    private static final String MARK_DEFAULT_ORDER = "default_order";
    private static final String MARK_NO_IMAGE = "no_image";
    private static final String MARK_UNSELECTED_LINKS = "unselected_links";

    /**
     * Creates a new LinkPortletJspBean object.
     */
    public LinksPortletJspBean(  )
    {
    }

    /**
     * Returns portlet links's creation form
     *
     * @param request request
     * @return Html form
     */
    public String getCreate( HttpServletRequest request )
    {
        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        String strPortletTypeId = request.getParameter( PARAMETER_PORTLET_TYPE_ID );

        HtmlTemplate template = getCreateTemplate( strPageId, strPortletTypeId );

        return template.getHtml(  );
    }

    /**
     * Returns portlet links's modification form
     *
     * @param request request
     * @return Html form
     */
    public String getModify( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        Portlet portlet = PortletHome.findByPrimaryKey( nPortletId );
        HashMap<String, String> model = new HashMap<String, String>(  );

        // get links list
        model.put( MARK_LINKS_LIST, getLinksInPortletList( nPortletId ) );

        //get unselected links
        model.put( MARK_UNSELECTED_LINKS, getUnselectedLinks( nPortletId ) );

        HtmlTemplate template = getModifyTemplate( portlet, model );

        return template.getHtml(  );
    }

    /**
     * Process linksPortlet's creation
     *
     * @param request request
     * @return Portlet's modification url
     */
    public String doCreate( HttpServletRequest request )
    {
        LinksPortlet portlet = new LinksPortlet(  );
        String strPageId = request.getParameter( PARAMETER_PAGE_ID );
        int nPageId = Integer.parseInt( strPageId );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        portlet.setPageId( nPageId );

        // Creating portlet
        LinksPortletHome.getInstance(  ).create( portlet );

        //Displays the page with the new Portlet
        return getPageUrl( nPageId );
    }

    /**
     * Process links order modification
     *
     * @param request request
     * @return Portlet's modification url
     */
    public String doModifyOrderLinks( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        String strLinkId = request.getParameter( PARAMETER_LINK_ID );
        int nLinkId = Integer.parseInt( strLinkId );
        int nOldOrder = LinksPortletHome.getLinkOrder( nPortletId, nLinkId );
        String strOrderLink = request.getParameter( PARAMETER_LINKS_ORDER );
        int nOrder = Integer.parseInt( strOrderLink );

        if ( nOrder < nOldOrder )
        {
            for ( int i = nOldOrder - 1; i > ( nOrder - 1 ); i-- )
            {
                int nLinkIdTemp = LinksPortletHome.getLinkIdByOrder( nPortletId, i );
                LinksPortletHome.updateLinkOrder( i + 1, nPortletId, nLinkIdTemp );
            }

            LinksPortletHome.updateLinkOrder( nOrder, nPortletId, nLinkId );
        }
        else if ( nOrder > nOldOrder )
        {
            for ( int i = nOldOrder; i < ( nOrder + 1 ); i++ )
            {
                int nLinkIdTemp = LinksPortletHome.getLinkIdByOrder( nPortletId, i );
                LinksPortletHome.updateLinkOrder( i - 1, nPortletId, nLinkIdTemp );
            }

            LinksPortletHome.updateLinkOrder( nOrder, nPortletId, nLinkId );
        }

        return JSP_DO_MODIFY_PORTLET + "?" + PARAMETER_PORTLET_ID + "=" + nPortletId;
    }

    /**
     * Process portlet's modification
     *
     * @param request request
     * @return The Jsp management URL of the process result
     */
    public String doModify( HttpServletRequest request )
    {
        // Getting portlet
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        LinksPortlet portlet = (LinksPortlet) PortletHome.findByPrimaryKey( nPortletId );

        String strStyleId = request.getParameter( Parameters.STYLE );

        if ( ( strStyleId == null ) || strStyleId.trim(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_ERROR );
        }

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        // mandatory fields
        String strName = portlet.getName(  );

        if ( strName.trim(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_ERROR );
        }

        // Modificating portlet
        portlet.update(  );

        return getPageUrl( portlet.getPageId(  ) );
    }

    /**
     *
     *
     * @param nPortletId The identifier of the portlet
     * @return
     */
    private String getUnselectedLinks( int nPortletId )
    {
        HashMap model = new HashMap(  );
        ReferenceList ordersList = getNewLinkOrdersList( nPortletId );
        //ReferenceList linksList = LinksPortletHome.getLinksList(  );
        
        ReferenceList linksList = new ReferenceList(  );
        
        Collection<Link> links = LinkHome.getLinksList(  );
        
        links = (Collection<Link>) AdminWorkgroupService.getAuthorizedCollection( links, getUser(  ) );
        
        for ( Link link : links)
        {
        	linksList.addItem(link.getId(  ), link.getName(  ) + " " + link.getUrl(  ) );
        }
        
        model.put( MARK_ORDER_LIST, ordersList );
        model.put( MARK_LINKS_LIST, linksList );
        model.put( MARK_PORTLET_ID, nPortletId );
        model.put( MARK_DEFAULT_ORDER, 0 );
        model.put( MARK_DEFAULT_LINK, 0 );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_UNSELECTED_LINKS, getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Returns The list of links wich belong to a specified portlet
     *
     * @param nPortletId The identifier of the portlet
     * @return A list of links
     */
    private String getLinksInPortletList( int nPortletId )
    {
        StringBuffer strLinksList = new StringBuffer(  );
        Collection<Link> list = LinksPortletHome.getLinksInPortletList( nPortletId );
        ReferenceList ordersList = getOrdersList( nPortletId );
        ReferenceList optUrls = null;
        ReferenceList virtualHosts = virtualHosts = new ReferenceList( );

        for ( Link link : list )
        {
            HashMap model = new HashMap(  );
            Integer nOrderLink = new Integer( LinksPortletHome.getLinkOrder( nPortletId, link.getId(  ) ) );

            //Replace links optional url code by url name
            optUrls = link.getOptionalUrls(  );

            try
            {                
                if( AppPathService.getAvailableVirtualHosts(  ) != null)
                { 
                    virtualHosts = AppPathService.getAvailableVirtualHosts(  );                
                    for ( ReferenceItem item : virtualHosts )
                    {
                        for ( ReferenceItem optUrl : optUrls )
                        {
                            if ( optUrl.getCode(  ).equals( item.getCode(  ) ) )
                            {
                                optUrl.setCode( item.getName(  ) );
                            }
                        }
                    }
                }
            }
            catch( NullPointerException  e )
            {
                AppLogService.error( e.getMessage() , e );
            }            
            
            link.setOptionalUrls( optUrls );

            model.put( MARK_LINK, link );
            model.put( MARK_NO_IMAGE, AppPropertiesService.getProperty( PROPERTY_NO_IMAGE_ADMIN ) );
            model.put( MARK_ORDER_LIST, ordersList );
            model.put( MARK_LINK_DEFAULT_ORDER, nOrderLink );
            model.put( MARK_PORTLET_ID, nPortletId );
            model.put( MARK_NEW_LINK, "0" );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LINKS_LIST, getLocale(  ), model );
            strLinksList.append( template.getHtml(  ) );
        }

        return strLinksList.toString(  );
    }

    /**
     * Returns an orders list
     *
     * @param nPortletId The identifier of the portlet
     * @return A list of orders
     */
    private ReferenceList getOrdersList( int nPortletId )
    {
        int nMax = LinksPortletHome.getMaxOrder( nPortletId );
        ReferenceList list = new ReferenceList(  );

        for ( int i = 1; i < ( nMax + 1 ); i++ )
        {
            list.addItem( i, Integer.toString( i ) );
        }

        return list;
    }

    /**
     * Returns an orders list for a new link in a specified portlet
     *
     * @param nPortletId The identifier of the portlet
     * @return A list of orders
     */
    private ReferenceList getNewLinkOrdersList( int nPortletId )
    {
        int nMax = LinksPortletHome.getMaxOrder( nPortletId );
        ReferenceList list = new ReferenceList(  );

        for ( int i = 1; i < ( nMax + 2 ); i++ )
        {
            list.addItem( i, Integer.toString( i ) );
        }

        return list;
    }

    /**
     * Process links's selecting
     *
     * @param request request
     * @return Portlet's modification url
     */
    public String doSelectLink( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        String strLinkId = request.getParameter( PARAMETER_LINKS );
        int nLinkId = Integer.parseInt( strLinkId );
        String strOrder = request.getParameter( PARAMETER_LINKS_ORDER );
        int nOrder = Integer.parseInt( strOrder );

        // Checking duplicate
        if ( LinksPortletHome.testDuplicate( nPortletId, nLinkId ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_PORTLET_LINK_SELECTED, AdminMessage.TYPE_ERROR );
        }

        int nMax = LinksPortletHome.getMaxOrder( nPortletId );

        for ( int i = nOrder; i < ( nMax + 1 ); i++ )
        {
            int nLinkIdTemp = LinksPortletHome.getLinkIdByOrder( nPortletId, i );
            LinksPortletHome.updateLinkOrder( i + 1, nPortletId, nLinkIdTemp );
        }

        LinksPortletHome.insertLink( nPortletId, nLinkId, nOrder );

        return JSP_DO_MODIFY_PORTLET + "?" + PARAMETER_PORTLET_ID + "=" + nPortletId;
    }

    /**
     * Process Link's unselecting
     *
     * @param request request
     * @return Portlet's modification url
     */
    public String doUnselectLinks( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        String strLinkId = request.getParameter( PARAMETER_LINK_ID );
        int nLinkId = Integer.parseInt( strLinkId );
        int nOrder = LinksPortletHome.getLinkOrder( nPortletId, nLinkId );
        int nMax = LinksPortletHome.getMaxOrder( nPortletId );

        // Updating Database
        LinksPortletHome.removeLink( nPortletId, nLinkId );

        for ( int i = nOrder + 1; i < ( nMax + 1 ); i++ )
        {
            int nLinkIdTemp = LinksPortletHome.getLinkIdByOrder( nPortletId, i );
            LinksPortletHome.updateLinkOrder( i - 1, nPortletId, nLinkIdTemp );
        }

        return JSP_DO_MODIFY_PORTLET + "?" + PARAMETER_PORTLET_ID + "=" + nPortletId;
    }

    /**
     * Returns portlet's properties prefix
     *
     * @return prefix
     */
    public String getPropertiesPrefix(  )
    {
        return "portlet.links";
    }
}
