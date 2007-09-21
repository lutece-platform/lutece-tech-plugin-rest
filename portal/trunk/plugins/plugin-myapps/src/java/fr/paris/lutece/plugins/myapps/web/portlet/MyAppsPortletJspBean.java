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
package fr.paris.lutece.plugins.myapps.web.portlet;

import fr.paris.lutece.plugins.myapps.business.MyApps;
import fr.paris.lutece.plugins.myapps.business.MyAppsHome;
import fr.paris.lutece.plugins.myapps.business.MyAppsUser;
import fr.paris.lutece.plugins.myapps.business.MyAppsUserHome;
import fr.paris.lutece.plugins.myapps.business.portlet.MyAppsPortlet;
import fr.paris.lutece.plugins.myapps.business.portlet.MyAppsPortletHome;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import javax.servlet.http.HttpServletRequest;


public class MyAppsPortletJspBean extends PortletJspBean
{
    private static final String PARAMETER_PAGE_ID = "page_id";
    private static final String PARAMETER_PORTLET_ID = "portlet_id";
    private static final String PARAMETER_PORTLET_TYPE_ID = "portlet_type_id";
    private static final String PARAMETER_PLUGIN_NAME = "plugin_name";
    private static final String PARAMETER_ID_APP = "id_app";

    /**
     * Returns portlet user application's creation form
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

    public String doCreate( HttpServletRequest request )
    {
        MyAppsPortlet portlet = new MyAppsPortlet(  );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        int nIdPage = Integer.parseInt( request.getParameter( PARAMETER_PAGE_ID ) );
        portlet.setPageId( nIdPage );
        MyAppsPortletHome.getInstance(  ).create( portlet );

        //Displays the page with the new Portlet
        return getPageUrl( nIdPage );
    }

    /**
     * Returns portlet user application's modification form
     *
     * @param request request
     * @return Html form
     */
    public String getModify( HttpServletRequest request )
    {
        int nPortletId = Integer.parseInt( request.getParameter( PARAMETER_PORTLET_ID ) );
        MyAppsPortlet portlet = (MyAppsPortlet) PortletHome.findByPrimaryKey( nPortletId );
        HtmlTemplate template = getModifyTemplate( portlet );

        return template.getHtml(  );
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
        int nPortletId = Integer.parseInt( request.getParameter( PARAMETER_PORTLET_ID ) );
        MyAppsPortlet portlet = (MyAppsPortlet) PortletHome.findByPrimaryKey( nPortletId );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        // Modifying the portlet
        portlet.update(  );

        //Displays the page with the updated portlet
        return getPageUrl( portlet.getPageId(  ) );
    }

    /**
     * Returns application url with parameters
     *
     * @param request request
     * @return strLink application url
     */
    public String doOpenMyApp( HttpServletRequest request )
    {
        String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );
        Plugin plugin = PluginService.getPlugin( strPluginName );

        LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );
        
        // Checks whether user's session is still valid.
        if( user == null )
        {
            return "../../Portal.jsp?page=myapps&action=unselect";
        }
        String strUserName = user.getName(  );

        int nIdApp = Integer.parseInt( request.getParameter( PARAMETER_ID_APP ) );
        MyAppsUser myAppsUser = MyAppsUserHome.getCredentials( nIdApp, strUserName, plugin );
        MyApps myapps = MyAppsHome.findByPrimaryKey( nIdApp, plugin );

        //The login and the url
        String strUrl = myapps.getUrl(  );
        String strLoginFieldName = myapps.getCode(  );
        String strUserLogin = myAppsUser.getStoredUserName(  );

        //Password
        String strPasswordField = myapps.getPassword(  );
        String strUserPassword = myAppsUser.getStoredUserPassword(  );

        //Extra Field
        String strExtraField = myapps.getData(  );
        String strExtraFieldValue = myAppsUser.getStoredUserData(  );
        UrlItem url = new UrlItem( strUrl );
        url.addParameter( strLoginFieldName, strUserLogin );
        url.addParameter( strPasswordField, strUserPassword );

        if ( !strExtraField.equals( null ) && !strExtraField.equals( "" ) )
        {
            url.addParameter( strExtraField, strExtraFieldValue );
        }

        return url.getUrl(  );
    }

    /**
     * Returns the properties prefix used for myapps portlet and defined in lutece.properties file
     *
     * @return the value of the property prefix
     */
    public String getPropertiesPrefix(  )
    {
        return "portlet.myapps";
    }
}
