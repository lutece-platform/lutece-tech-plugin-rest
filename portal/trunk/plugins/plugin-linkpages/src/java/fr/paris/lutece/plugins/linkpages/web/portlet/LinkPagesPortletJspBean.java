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
package fr.paris.lutece.plugins.linkpages.web.portlet;

import fr.paris.lutece.plugins.linkpages.business.portlet.LinkPagesPortlet;
import fr.paris.lutece.plugins.linkpages.business.portlet.LinkPagesPortletHome;
import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage LinkPages Portlet
 */
public class LinkPagesPortletJspBean extends PortletJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";

    // MARKS
    private static final String MARK_COMBO_LINKPAGES = "combo_linkpages";
    private static final String MARK_LINKPAGE_ORDER = "linkpage_order";
    private static final String MARK_COMBO_LINKPAGES_ORDER = "combo_linkpages_order";
    private final static String MARK_LINKPAGE_ID = "linkpage_id";
    private final static String MARK_LINKPAGE_NAME = "linkpage_name";
    private final static String MARK_LINKPAGE_DESCRIPTION = "linkpage_description";
    private final static String MARK_NEW_LINKPAGE = "new_linkpage";
    private static final String MARK_PORTLET_ID = "portlet_id";
    private static final String MARK_LINKPAGES_LIST = "linkpages_list";
    private static final String MARK_PAGE_ID = "page_id";

    //Parameters
    private static final String PARAMETER_LINKPAGE = "linkpage";
    private static final String PARAMETER_PAGE_ID = "page_id";
    private static final String PARAMETER_LINKPAGE_ORDER = "linkpage_order";

    //Templates
    private static final String TEMPLATE_LINKPAGES_LIST = "admin/plugins/linkpages/linkpages_list.html";

    //Messages
    private static final String MESSAGE_LINKPAGE_NOT_EXIST = "linkpages.message.mandatory.linkpageNotExisted";
    private static final String MESSAGE_PORTLET_LINK_PAGE_SELECTED = "linkpages.message.portlet.linkpageAlreadySelected";

    // Jsp
    private static final String JSP_DO_MODIFY_PORTLET = "../../DoModifyPortlet.jsp";

    /**
     * Returns portlet's properties prefix
     *
     * @return prefix
     */
    public String getPropertiesPrefix(  )
    {
        return "portlet.link.pages";
    }

    /**
     * Returns the Download portlet creation form
     *
     * @param request The http request
     * @return The HTML form
     */
    public String getCreate( HttpServletRequest request )
    {
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        String strIdPortletType = request.getParameter( PARAMETER_PORTLET_TYPE_ID );
        HtmlTemplate template = getCreateTemplate( strIdPage, strIdPortletType );

        return template.getHtml(  );
    }

    /**
     * Returns the modified portlet in HTML form
     *
     * @param request The http request
     * @return The HTML form
     */
    public String getModify( HttpServletRequest request )
    {
        String strIdPortlet = request.getParameter( PARAMETER_PORTLET_ID );
        int nIdPortlet = Integer.parseInt( strIdPortlet );
        LinkPagesPortlet portlet = (LinkPagesPortlet) PortletHome.findByPrimaryKey( nIdPortlet );
        int nPageId = portlet.getPageId(  );

        HashMap model = new HashMap(  );
        model.put( MARK_LINKPAGES_LIST, getLinkPagesInPortletList( nIdPortlet ) );
        model.put( MARK_PORTLET_ID, strIdPortlet );
        model.put( MARK_PAGE_ID, nPageId );

        // LinkPages order combo
        int nMax = LinkPagesPortletHome.getMaxOrder( nIdPortlet );
        nMax = nMax + 1;
        model.put( MARK_LINKPAGE_ORDER, Integer.toString( nMax ) );
        model.put( MARK_COMBO_LINKPAGES_ORDER, getNewLinkPageOrdersList( nIdPortlet ) );
        // LinkPages list combo
        model.put( MARK_COMBO_LINKPAGES, LinkPagesPortletHome.getLinkPagesList(  ) );

        HtmlTemplate template = getModifyTemplate( portlet, model );

        return template.getHtml(  );
    }

    /**
     * Process portlet's creation
     *
     * @param request The Http request
     * @return The Jsp management URL of the process result
     */
    public String doCreate( HttpServletRequest request )
    {
        LinkPagesPortlet portlet = new LinkPagesPortlet(  );
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        int nIdPage = Integer.parseInt( strIdPage );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        portlet.setPageId( nIdPage );

        //Portlet creation
        LinkPagesPortletHome.getInstance(  ).create( portlet );

        //Displays the page with the new Portlet
        return getPageUrl( nIdPage );
    }

    /**
     * Process portlet's modification
     *
     * @param request The http request
     * @return Management's Url
     */
    public String doModify( HttpServletRequest request )
    {
        //recovery of the portlet
        String strIdPortlet = request.getParameter( PARAMETER_PORTLET_ID );
        int nIdPortlet = Integer.parseInt( strIdPortlet );
        LinkPagesPortlet portlet = (LinkPagesPortlet) PortletHome.findByPrimaryKey( nIdPortlet );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        //Update of the portlet
        portlet.update(  );

        //Displays the page with the updated portlet
        return getPageUrl( portlet.getPageId(  ) );
    }

    /**
    * Returns an orders list for a new linkpage in a specified portlet
    *
    * @param nPortletId The identifier of the portlet
    * @return A list of orders
    */
    private ReferenceList getNewLinkPageOrdersList( int nPortletId )
    {
        int nMax = LinkPagesPortletHome.getMaxOrder( nPortletId );
        ReferenceList list = new ReferenceList(  );

        for ( int i = 1; i < ( nMax + 2 ); i++ )
        {
            list.addItem( i, Integer.toString( i ) );
        }

        return list;
    }

    /**
     * Returns The list of linkpages wich belong to a specified portlet
     *
     * @param nPortletId The identifier of the portlet
     * @return A list of linkpages
     */
    private String getLinkPagesInPortletList( int nPortletId )
    {
        StringBuffer strLinkPagesList = new StringBuffer(  );

        for ( Page page : LinkPagesPortletHome.getLinkPagesInPortletList( nPortletId ) )
        {
            //Page page = (Page) i.next (  );
            HashMap model = new HashMap(  );
            int nIdPage = page.getId(  );
            model.put( MARK_LINKPAGE_ID, page.getId(  ) );
            model.put( MARK_LINKPAGE_NAME, page.getName(  ) );
            model.put( MARK_LINKPAGE_DESCRIPTION, page.getDescription(  ) );
            model.put( MARK_NEW_LINKPAGE, "0" );
            model.put( MARK_COMBO_LINKPAGES_ORDER, getOrdersList( nPortletId ) );
            model.put( MARK_PORTLET_ID, nPortletId );

            Integer nOrderLinkPage = new Integer( LinkPagesPortletHome.getLinkPageOrder( nPortletId, page.getId(  ) ) );
            model.put( MARK_LINKPAGE_ORDER, nOrderLinkPage.toString(  ) );

            strLinkPagesList.append( AppTemplateService.getTemplate( TEMPLATE_LINKPAGES_LIST, getLocale(  ), model )
                                                       .getHtml(  ) );
        }

        return strLinkPagesList.toString(  );
    }

    /**
     * Returns an orders list
     *
     * @param nPortletId The identifier of the portlet
     * @return A list of orders
     */
    private ReferenceList getOrdersList( int nPortletId )
    {
        int nMax = LinkPagesPortletHome.getMaxOrder( nPortletId );
        ReferenceList list = new ReferenceList(  );

        for ( int i = 1; i < ( nMax + 1 ); i++ )
        {
            list.addItem( i, Integer.toString( i ) );
        }

        return list;
    }

    /**
     * Process link pages order modification
     *
     * @param request request
     * @return Portlet's modification url
     */
    public String doModifyOrderLinkPage( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        String strLinkPageId = request.getParameter( PARAMETER_PAGE_ID );
        int nLinkPageId = Integer.parseInt( strLinkPageId );
        int nOldOrder = LinkPagesPortletHome.getLinkPageOrder( nPortletId, nLinkPageId );
        String strOrder = request.getParameter( PARAMETER_LINKPAGE_ORDER );
        int nOrder = Integer.parseInt( strOrder );

        if ( nOrder < nOldOrder )
        {
            for ( int i = nOldOrder - 1; i > ( nOrder - 1 ); i-- )
            {
                int nIdTemp = LinkPagesPortletHome.getLinkPageIdByOrder( nPortletId, i );
                LinkPagesPortletHome.updateLinkPageOrder( i + 1, nPortletId, nIdTemp );
            }

            LinkPagesPortletHome.updateLinkPageOrder( nOrder, nPortletId, nLinkPageId );
        }
        else if ( nOrder > nOldOrder )
        {
            for ( int i = nOldOrder; i < ( nOrder + 1 ); i++ )
            {
                int nIdTemp = LinkPagesPortletHome.getLinkPageIdByOrder( nPortletId, i );
                LinkPagesPortletHome.updateLinkPageOrder( i - 1, nPortletId, nIdTemp );
            }

            LinkPagesPortletHome.updateLinkPageOrder( nOrder, nPortletId, nLinkPageId );
        }

        return JSP_DO_MODIFY_PORTLET + "?" + PARAMETER_PORTLET_ID + "=" + nPortletId;
    }

    /**
     * Process link page's unselecting
     *
     * @param request request
     * @return Portlet's modification url
     */
    public String doUnselectLinkPage( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        String strLinkPageId = request.getParameter( PARAMETER_PAGE_ID );
        int nLinkPageId = Integer.parseInt( strLinkPageId );
        int nOrder = LinkPagesPortletHome.getLinkPageOrder( nPortletId, nLinkPageId );
        int nMax = LinkPagesPortletHome.getMaxOrder( nPortletId );

        // Updating Database
        LinkPagesPortletHome.removeLinkPage( nPortletId, nLinkPageId );

        for ( int i = nOrder + 1; i < ( nMax + 1 ); i++ )
        {
            int nLinkPageIdTemp = LinkPagesPortletHome.getLinkPageIdByOrder( nPortletId, i );
            LinkPagesPortletHome.updateLinkPageOrder( i - 1, nPortletId, nLinkPageIdTemp );
        }

        return JSP_DO_MODIFY_PORTLET + "?" + PARAMETER_PORTLET_ID + "=" + nPortletId;
    }

    /**
     * Process link page's selecting
     *
     * @param request request
     * @return Portlet's modification url
     */
    public String doSelectLinkPage( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        String strOrder = request.getParameter( PARAMETER_LINKPAGE_ORDER );
        int nOrder = Integer.parseInt( strOrder );

        String strLinkPageId = request.getParameter( PARAMETER_LINKPAGE );

        if ( ( strLinkPageId == null ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_LINKPAGE_NOT_EXIST, AdminMessage.TYPE_ERROR );
        }

        int nLinkPageId = Integer.parseInt( strLinkPageId );

        // Checking duplicate
        if ( LinkPagesPortletHome.testDuplicate( nPortletId, nLinkPageId ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_PORTLET_LINK_PAGE_SELECTED,
                AdminMessage.TYPE_ERROR );
        }

        int nMax = LinkPagesPortletHome.getMaxOrder( nPortletId );

        for ( int i = nOrder; i < ( nMax + 1 ); i++ )
        {
            int nLinkPageIdTemp = LinkPagesPortletHome.getLinkPageIdByOrder( nPortletId, i );
            LinkPagesPortletHome.updateLinkPageOrder( i + 1, nPortletId, nLinkPageIdTemp );
        }

        LinkPagesPortletHome.insertLinkPage( nPortletId, nLinkPageId, nOrder );

        return JSP_DO_MODIFY_PORTLET + "?" + PARAMETER_PORTLET_ID + "=" + nPortletId;
    }

    /**
     * Process the selection of all link pages
     *
     * @param request request
     * @return Portlet's modification url
     */
    public String doSelectAllLinkPage( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );

        LinkPagesPortlet portlet = (LinkPagesPortlet) PortletHome.findByPrimaryKey( nPortletId );

        int nMax = LinkPagesPortletHome.getMaxOrder( nPortletId );

        int nOrder = nMax + 1;

        Collection linkPagesList = PageHome.getChildPages( portlet.getPageId(  ) );

        Iterator i = linkPagesList.iterator(  );

        while ( i.hasNext(  ) )
        {
            Page linkPage = (Page) i.next(  );

            // Checking duplicate
            if ( !LinkPagesPortletHome.testDuplicate( nPortletId, linkPage.getId(  ) ) )
            {
                LinkPagesPortletHome.insertLinkPage( nPortletId, linkPage.getId(  ), nOrder );
                nOrder++;
            }
        }

        return JSP_DO_MODIFY_PORTLET + "?" + PARAMETER_PORTLET_ID + "=" + nPortletId;
    }
}
