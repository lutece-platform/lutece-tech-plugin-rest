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
package fr.paris.lutece.plugins.dbpage.web.portlet;

import fr.paris.lutece.plugins.dbpage.business.DbPage;
import fr.paris.lutece.plugins.dbpage.business.portlet.DbPagePortlet;
import fr.paris.lutece.plugins.dbpage.business.portlet.DbPagePortletHome;
import fr.paris.lutece.plugins.dbpage.service.DbPageService;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage DbPage Portlet features
 */
public class DbPagePortletJspBean extends PortletJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String PARAMETER_PAGE_ID = "page_id";
    private static final String PARAMETER_PORTLET_ID = "portlet_id";
    private static final String PARAMETER_PORTLET_TYPE_ID = "portlet_type_id";
    private static final String PARAMETER_DBPAGE_NAME = "dbpage_name";
    private static final String PARAMETER_DBPAGE_VALUES = "dbpage_param_values";
    private static final String COMBO_DBPAGE_LIST = "@combo_dbpage@";
    private static final String MARK_DBPAGE_VALUES = "dbpage_values";

    // Markers
    private static final String MARK_DBPAGE_LIST = "dbpage_list";
    private static final String MARK_DBPAGE_ID = "default_dbpage_id";
    private static final String PROPERTY_PORTLET_PREFIX = "portlet.dbpage";

    //////////////////////////////////////////////////////////////////////////////////
    //Templates
    private static final String TEMPLATE_COMBO_DBPAGE = "admin/plugins/dbpage/portlet/combo_dbpage.html";

    ////////////////////////////////////////////////////////////////////////////
    // Methods

    /**
     * Returns the DbPage portlet creation form
     *
     * @param request The Http request
     * @return The HTML form
     */
    public String getCreate( HttpServletRequest request )
    {
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        String strPortletIdType = request.getParameter( PARAMETER_PORTLET_TYPE_ID );
        HtmlTemplate template = getCreateTemplate( strIdPage, strPortletIdType );

        Collection<DbPage> colDbPage = DbPageService.getInstance(  ).getDbPagesCollection(  );
        colDbPage = AdminWorkgroupService.getAuthorizedCollection( colDbPage, getUser(  ) );

        template.substitute( COMBO_DBPAGE_LIST, getDbPageListCombo( colDbPage, "" ) );
        template.substitute( MARK_DBPAGE_VALUES, " " );

        return template.getHtml(  );
    }

    /**
     * Returns the DbPage portlet modification form
     *
     * @param request The Http request
     * @return The HTML form
     */
    public String getModify( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        DbPagePortlet portlet = (DbPagePortlet) PortletHome.findByPrimaryKey( nPortletId );

        HtmlTemplate template = getModifyTemplate( portlet );
        Collection<DbPage> colDbPage = DbPageService.getInstance(  ).getDbPagesCollection(  );
        colDbPage = AdminWorkgroupService.getAuthorizedCollection( colDbPage, getUser(  ) );

        template.substitute( COMBO_DBPAGE_LIST, getDbPageListCombo( colDbPage, portlet.getDbPageName(  ) ) );
        template.substitute( MARK_DBPAGE_VALUES, portlet.getDbValues(  ) );

        return template.getHtml(  );
    }

    /**
     * Returns the portlet's properties prefix
     * @return The portlet's properties prefix
     */
    public String getPropertiesPrefix(  )
    {
        return PROPERTY_PORTLET_PREFIX;
    }

    /**
     * Process portlet's creation
     *
     * @param request The Http request
     * @return The Jsp management URL of the process result
     */
    public String doCreate( HttpServletRequest request )
    {
        DbPagePortlet portlet = new DbPagePortlet(  );
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        String strValues = request.getParameter( PARAMETER_DBPAGE_VALUES );
        int nIdPage = Integer.parseInt( strIdPage );
        String strDbPage = request.getParameter( PARAMETER_DBPAGE_NAME );
        portlet.setDbPageName( strDbPage );
        portlet.setValues( strValues );

        DbPage dbPage = DbPageService.getInstance(  ).getDbPage( portlet.getDbPageName(  ) );

        if ( !AdminWorkgroupService.isAuthorized( dbPage, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        portlet.setPageId( nIdPage );

        // Portlet's creation
        DbPagePortletHome.getInstance(  ).create( portlet );

        //Displays the page with the new Portlet
        return getPageUrl( nIdPage );
    }

    /**
     * Process portlet's modification
     *
     * @param request The Http request
     * @return The Jsp management URL of the process result
     */
    public String doModify( HttpServletRequest request )
    {
        // recovers portlet attributes
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        DbPagePortlet portlet = (DbPagePortlet) PortletHome.findByPrimaryKey( nPortletId );

        // retrieve portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        String strDbPageName = request.getParameter( PARAMETER_DBPAGE_NAME );

        DbPage dbPage = DbPageService.getInstance(  ).getDbPage( strDbPageName );

        if ( !AdminWorkgroupService.isAuthorized( dbPage, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        portlet.setDbPageName( strDbPageName );

        String strDbPageValues = request.getParameter( PARAMETER_DBPAGE_VALUES );
        portlet.setValues( strDbPageValues );

        // updates the portlet
        portlet.update(  );

        // displays the page with the portlet updated
        return getPageUrl( portlet.getPageId(  ) );
    }

    /**
     * Returns an html template containing the list of the dbpages
     * @param strDbPageId The default DbPage identifier
     * @return The html code
     */
    private String getDbPageListCombo( ReferenceList DbPageList, String strDbPageId )
    {
        HashMap model = new HashMap(  );
        model.put( MARK_DBPAGE_ID, strDbPageId );
        model.put( MARK_DBPAGE_LIST, DbPageList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_COMBO_DBPAGE, getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Returns an html template containing the list of the dbpages
     * @param strDbPageId The default DbPage identifier
     * @return The html code
     */
    private String getDbPageListCombo( Collection<DbPage> DbPageCollection, String strDbPageId )
    {
        ReferenceList DbPageList = new ReferenceList(  );

        for ( DbPage dbPage : DbPageCollection )
        {
            DbPageList.addItem( dbPage.getName(  ), dbPage.getName(  ) );
        }

        return getDbPageListCombo( DbPageList, strDbPageId );
    }
}
