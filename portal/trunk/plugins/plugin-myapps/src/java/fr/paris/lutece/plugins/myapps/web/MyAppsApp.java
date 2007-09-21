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
package fr.paris.lutece.plugins.myapps.web;

import fr.paris.lutece.plugins.myapps.business.MyApps;
import fr.paris.lutece.plugins.myapps.business.MyAppsHome;
import fr.paris.lutece.plugins.myapps.business.MyAppsUser;
import fr.paris.lutece.plugins.myapps.business.MyAppsUserHome;
import fr.paris.lutece.plugins.myapps.service.MyAppsService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the main search page
 */
public class MyAppsApp implements XPageApplication
{
    //Parameters
    private static final String PARAMETER_PAGE = "page";
    private static final String PARAMETER_ACTION = "action";
    private static final String PARAMETER_ID_APP = "id_app";
    private static final String PARAMETER_ID_USER = "id_user";
    private static final String PARAMETER_USER_LOGIN = "user_login";
    private static final String PARAMETER_USER_PASSWORD = "user_password";
    private static final String PARAMETER_USER_EXTRA_DATA = "user_extra_data";
    private static final String ACTION_INSERT = "insert";
    private static final String ACTION_MODIFY = "modify";
    private static final String ACTION_UPDATE = "update";
    private static final String ACTION_CREATE_APP = "create";
    private static final String ACTION_DELETE = "delete";
    private static final String ACTION_UNSELECT = "unselect";

    //Templates
    private static final String TEMPLATE_MYAPPS = "skin/plugins/myapps/page_myapps.html";
    private static final String TEMPLATE_MYAPPS_INSERT = "skin/plugins/myapps/page_app_insert.html";
    private static final String TEMPLATE_MYAPPS_MODIFY = "skin/plugins/myapps/page_app_modify.html";
    private static final String MARK_MYAPPS_LIST = "myapps_list";
    private static final String MARK_USER = "user";
    private static final String MARK_MYAPPS = "myapps";
    private static final String MARK_MYAPPS_LIST_USER = "myapps_list_user";

    //Properties
    private static final String PROPERTY_PAGE_TITLE = "myapps.page_myapps.pageTitle";
    private static final String PROPERTY_PAGE_PATH = "myapps.page_myapps.pagePathLabel";
    private static final String PROPERTY_INSERT_PAGE_TITLE = "myapps.page_app_insert.pageTitle";
    private static final String PROPERTY_INSERT_PAGE_PATH = "myapps.page_app_insert.pagePathLabel";
    private static final String PROPERTY_MODIFY_PAGE_TITLE = "myapps.page_app_modify.pageTitle";
    private static final String PROPERTY_MODIFY_PAGE_PATH = "myapps.page_app_modify.pagePathLabel";
    private static final String PROPERTY_UNSELECT_PAGE_TITLE = "myapps.pagePathLabel";
    private static final String PROPERTY_UNSELECT_PAGE_PATH = "myapps.page_app_modify.pagePathLabel";

    /**
     * Front Office application to manage myapps application
     * @param request The request
     * @param nMode The mode
     * @param _plugin The plugin
     * @return The Xpage
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin _plugin )
    {
        XPage page = new XPage(  );
        page.setTitle( I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, request.getLocale(  ) ) );
        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_PAGE_PATH, request.getLocale(  ) ) );

        String strPluginName = request.getParameter( PARAMETER_PAGE );
        _plugin = PluginService.getPlugin( strPluginName );

        String strAction = request.getParameter( PARAMETER_ACTION );

        if ( strAction == null )
        {
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

            //return unregistered form if unregistered user wants to acces application management form
            if ( ( user == null ) || ( user.getName(  ) == null ) || user.getName(  ).equals( "" ) )
            {
                page.setContent( "" );
                page.setTitle( I18nService.getLocalizedString( PROPERTY_UNSELECT_PAGE_TITLE, request.getLocale(  ) ) );
                page.setPathLabel( I18nService.getLocalizedString( PROPERTY_UNSELECT_PAGE_PATH, request.getLocale(  ) ) );
            }
            else
            {
                page.setContent( getMainTemplate( user.getName(  ), request, _plugin ).getHtml(  ) );
            }
        }

        else if ( strAction.equals( ACTION_INSERT ) )
        {
            int nIdApp = Integer.parseInt( request.getParameter( PARAMETER_ID_APP ) );
            MyApps myapps = MyAppsHome.findByPrimaryKey( nIdApp, _plugin );
            HashMap model = new HashMap(  );
            model.put( MARK_MYAPPS, myapps );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MYAPPS_INSERT, request.getLocale(  ), model );
            page.setContent( template.getHtml(  ) );
            page.setTitle( I18nService.getLocalizedString( PROPERTY_INSERT_PAGE_TITLE, request.getLocale(  ) ) );
            page.setPathLabel( I18nService.getLocalizedString( PROPERTY_INSERT_PAGE_PATH, request.getLocale(  ) ) );
        }

        else if ( strAction.equals( ACTION_CREATE_APP ) )
        {
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );
            String strUserName = user.getName(  );
            MyAppsUser myAppsUser = new MyAppsUser(  );

            int nIdApp = Integer.parseInt( request.getParameter( PARAMETER_ID_APP ) );
            String strUserLogin = request.getParameter( PARAMETER_USER_LOGIN );
            String strPassword = request.getParameter( PARAMETER_USER_PASSWORD );
            String strExtraData = request.getParameter( PARAMETER_USER_EXTRA_DATA );
            myAppsUser.setName( strUserName );
            myAppsUser.setIdApplication( nIdApp );
            myAppsUser.setStoredUserName( strUserLogin );
            myAppsUser.setStoredUserPassword( strPassword );
            myAppsUser.setStoredUserData( strExtraData );
            MyAppsUserHome.create( myAppsUser, _plugin );

            page.setContent( getMainTemplate( strUserName, request, _plugin ).getHtml(  ) );
        }

        else if ( strAction.equals( ACTION_MODIFY ) )
        {
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );
            String strUserName = user.getName(  );
            int nIdApp = Integer.parseInt( request.getParameter( PARAMETER_ID_APP ) );
            MyApps myapps = MyAppsHome.findByPrimaryKey( nIdApp, _plugin );
            MyAppsUser myAppsUser = MyAppsUserHome.getCredentials( nIdApp, strUserName, _plugin );
            HashMap model = new HashMap(  );
            model.put( MARK_MYAPPS, myapps );
            model.put( MARK_USER, myAppsUser );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MYAPPS_MODIFY, request.getLocale(  ), model );
            page.setContent( template.getHtml(  ) );
            page.setTitle( I18nService.getLocalizedString( PROPERTY_MODIFY_PAGE_TITLE, request.getLocale(  ) ) );
            page.setPathLabel( I18nService.getLocalizedString( PROPERTY_MODIFY_PAGE_PATH, request.getLocale(  ) ) );
        }

        else if ( strAction.equals( ACTION_UPDATE ) )
        {
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );
            String strUserName = user.getName(  );
            MyAppsUser myAppsUser = new MyAppsUser(  );

            int nIdApp = Integer.parseInt( request.getParameter( PARAMETER_ID_APP ) );
            int nIdUser = Integer.parseInt( request.getParameter( PARAMETER_ID_USER ) );
            String strUserLogin = request.getParameter( PARAMETER_USER_LOGIN );
            String strPassword = request.getParameter( PARAMETER_USER_PASSWORD );
            String strExtraData = request.getParameter( PARAMETER_USER_EXTRA_DATA );
            myAppsUser.setIdUser( nIdUser );
            myAppsUser.setName( strUserName );
            myAppsUser.setIdApplication( nIdApp );
            myAppsUser.setStoredUserName( strUserLogin );
            myAppsUser.setStoredUserPassword( strPassword );
            myAppsUser.setStoredUserData( strExtraData );
            MyAppsUserHome.update( myAppsUser, _plugin );

            page.setContent( getMainTemplate( strUserName, request, _plugin ).getHtml(  ) );
        }

        else if ( strAction.equals( ACTION_DELETE ) )
        {
            //Delete a tuple representing the rights of a specific application
            LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );
            String strUserName = user.getName(  );
            int nIdUser = Integer.parseInt( request.getParameter( PARAMETER_ID_USER ) );
            MyAppsUserHome.remove( nIdUser, _plugin );

            page.setContent( getMainTemplate( strUserName, request, _plugin ).getHtml(  ) );
        }
        else if ( strAction.equals( ACTION_UNSELECT ) )
        {
            page.setContent( "" );
            page.setTitle( I18nService.getLocalizedString( PROPERTY_UNSELECT_PAGE_TITLE, request.getLocale(  ) ) );
            page.setPathLabel( I18nService.getLocalizedString( PROPERTY_UNSELECT_PAGE_PATH, request.getLocale(  ) ) );
        }

        return page;
    }

    /**
     * This method generates the main management screen
     * @param strUserName The username
     * @param request The request
     * @return the html template
     */
    private HtmlTemplate getMainTemplate( String strUserName, HttpServletRequest request, Plugin plugin )
    {
        List listApplications = MyAppsHome.getmyAppsList( plugin );
        List<MyApps> listApplicationsUser = MyAppsHome.getmyAppsListByUser( strUserName, plugin );
        HashMap model = new HashMap(  );
        model.put( MARK_MYAPPS_LIST, listApplications );
        model.put( MARK_MYAPPS_LIST_USER, listApplicationsUser );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MYAPPS, request.getLocale(  ), model );

        return template;
    }

    /**
    * Management of the image associated to the application
    * @param page The MyApps Object
    * @param strMyAppsId The myapps identifier
    */
    public String getResourceImagePage( String strMyAppsId )
    {
        String strResourceType = MyAppsService.getInstance(  ).getResourceTypeId(  );
        UrlItem url = new UrlItem( Parameters.IMAGE_SERVLET );
        url.addParameter( Parameters.RESOURCE_TYPE, strResourceType );
        url.addParameter( Parameters.RESOURCE_ID, strMyAppsId );

        return url.getUrlWithEntity(  );
    }
}
