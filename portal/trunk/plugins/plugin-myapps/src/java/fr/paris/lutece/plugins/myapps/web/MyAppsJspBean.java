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
import fr.paris.lutece.plugins.myapps.service.MyAppsService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.fileupload.FileItem;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/*
 * This class provides the user interface to manage myapps features ( manage,
 * create, modify, remove)
 */
public class MyAppsJspBean extends PluginAdminPageJspBean
{
    private static final String PARAMETER_APP_ID = "app_id";

    //The Templates
    private static final String TEMPLATE_MYAPPS = "admin/plugins/myapps/manage_myapps.html";
    private static final String TEMPLATE_CREATE_APPLICATION = "admin/plugins/myapps/create_application.html";
    private static final String TEMPLATE_MODIFY_APPLICATION = "admin/plugins/myapps/modify_application.html";

    //Bookmarks
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_MYAPPS_LIST = "myapps_list";
    private static final String MARK_MYAPPS = "myapps";
    private final static String MARK_ICON_URL = "icon_url";

    /**
     * The rights attributes
     */
    public static final String RIGHT_MYAPPS_MANAGEMENT = "MYAPPS_MANAGEMENT";
    private static final String PROPERTY_PAGE_TITLE_MYAPPS = "myapps.manage_myapps.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE = "myapps.create_application.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY = "myapps.modify_application.pageTitle";
    private static final String PROPERTY_DEFAULT_ITEMS_PER_PAGE = "myapps.itemsPerPage";
    private static final String PROPERTY_MYAPPS_PER_PAGE = "myapps.myappsPerPage";
    private static final String PARAMETER_ITEMS_PER_PAGE = "items_per_page";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_APP_NAME = "app_name";
    private static final String PARAMETER_APP_URL = "app_url";
    private static final String PARAMETER_APP_ICON = "app_icon";
    private static final String PARAMETER_APP_DESCRIPTION = "app_description";
    private static final String PARAMETER_USER_NAME = "user_name";
    private static final String PARAMETER_CODE_HEADING = "code_heading";
    private static final String PARAMETER_USER_PASSWORD = "user_password";
    private static final String PARAMETER_USER_FIELD = "user_field";
    private static final String PARAMETER_USER_FIELD_HEADING = "user_field_heading";
    private static final String PARAMETER_UPDATE_FILE = "update_file";

    // Jsp
    private static final String JSP_DO_REMOVE_MYAPP = "jsp/admin/plugins/myapps/DoRemoveMyApp.jsp";

    // Messages
    private static final String MESSAGE_CONFIRM_REMOVE_MYAPP = "myapps.message.confirmRemoveMyApp";

    //Variables
    private int _nItemsPerPage;
    private String _strCurrentPageIndex;

    /*
     * Creates a new myAppsJspBean object.
     */
    public MyAppsJspBean(  )
    {
    }

    /**
     * Returns the list of myapps
     *
     * @param request The Http request
     * @return the myapps list
     */
    public String getManageApplications( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MYAPPS );
        _nItemsPerPage = getItemsPerPage( request );
        _strCurrentPageIndex = getPageIndex( request );

        List listMyApps = MyAppsHome.getmyAppsList( getPlugin(  ) );
        Paginator paginator = new Paginator( listMyApps, _nItemsPerPage, getHomeUrl( request ), PARAMETER_PAGE_INDEX,
                _strCurrentPageIndex );
        HashMap model = new HashMap(  );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_MYAPPS_LIST, paginator.getPageItems(  ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MYAPPS, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Returns the form to create a myapp
     *
     * @param request The Http request
     * @return the html code of the myapp form
     */
    public String getCreateApplication( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_APPLICATION, getLocale(  ), null );

        return getAdminPage( template.getHtml(  ) );
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
        int nDefaultItemsPerPage = Integer.parseInt( AppPropertiesService.getProperty( PROPERTY_DEFAULT_ITEMS_PER_PAGE ) );

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
                nItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_MYAPPS_PER_PAGE, nDefaultItemsPerPage );
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
     * Process the confirmation of the removal of an application
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String getConfirmRemoveMyApp( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( JSP_DO_REMOVE_MYAPP );
        url.addParameter( PARAMETER_APP_ID, request.getParameter( PARAMETER_APP_ID ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_MYAPP, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Handles the removal form of a myapp
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage applications
     */
    public String doRemoveMyApp( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_APP_ID ) );
        MyAppsHome.remove( nId, getPlugin(  ) );

        // Go to the parent page
        return getHomeUrl( request );
    }

    /**
     * Returns the form to update a myapp
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    public String getModifyMyApp( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY );

        int nId = Integer.parseInt( request.getParameter( PARAMETER_APP_ID ) );
        MyApps myapp = MyAppsHome.findByPrimaryKey( nId, getPlugin(  ) );
        HashMap model = new HashMap(  );

        if ( myapp.getIconContent(  ) != null )
        {
            int nIconLength = myapp.getIconContent(  ).length;

            if ( nIconLength >= 1 )
            {
                model.put( MARK_ICON_URL, getResourceImagePage( Integer.toString( myapp.getIdApplication(  ) ) ) );
            }
        }

        model.put( MARK_MYAPPS, myapp );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_APPLICATION, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the form to create a myapp
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    public String getCreateMyApp( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_APPLICATION, getLocale(  ), null );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the myapp creation
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    public String doCreateMyApp( HttpServletRequest request )
    {
        // create the multipart request
        MultipartHttpServletRequest multiRequest = ( MultipartHttpServletRequest ) request;

        // Mandatory fields
        if ( multiRequest.getParameter( PARAMETER_USER_NAME ).equals( "" ) ||
                multiRequest.getParameter( PARAMETER_USER_PASSWORD ).equals( "" ) ||
                multiRequest.getParameter( PARAMETER_APP_URL ).equals( "" ) ||
                multiRequest.getParameter( PARAMETER_APP_NAME ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        MyApps myapps = new MyApps(  );
        myapps.setName( request.getParameter( PARAMETER_APP_NAME ) );
        myapps.setUrl( request.getParameter( PARAMETER_APP_URL ) );
        myapps.setPassword( request.getParameter( PARAMETER_USER_PASSWORD ) );
        myapps.setCode( request.getParameter( PARAMETER_USER_NAME ) );
        myapps.setCodeHeading( request.getParameter( PARAMETER_CODE_HEADING ) );
        myapps.setData( request.getParameter( PARAMETER_USER_FIELD ) );
        myapps.setDataHeading( request.getParameter( PARAMETER_USER_FIELD_HEADING ) );
        myapps.setDescription( request.getParameter( PARAMETER_APP_DESCRIPTION ) );

        try
        {
            FileItem itemIcon = multiRequest.getFile( PARAMETER_APP_ICON );

            if ( itemIcon != null )
            {
                byte[] bytes = itemIcon.get(  );
                String strMimeType = itemIcon.getContentType(  );
                myapps.setIconContent( bytes );
                myapps.setIconMimeType( strMimeType );
            }
            else
            {
                myapps.setIconContent( null );
                myapps.setIconMimeType( null );
            }

            MyAppsHome.create( myapps, getPlugin(  ) );
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        return getHomeUrl( request );
    }

    /**
     * Process the myapp creation
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    public String doModifyMyApp( HttpServletRequest request )
    {
        // create the multipart request
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

        // Mandatory fields
        if ( request.getParameter( PARAMETER_USER_NAME ).equals( "" ) ||
                request.getParameter( PARAMETER_USER_PASSWORD ).equals( "" ) ||
                request.getParameter( PARAMETER_APP_URL ).equals( "" ) ||
                request.getParameter( PARAMETER_APP_NAME ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nIdApplication = Integer.parseInt( request.getParameter( PARAMETER_APP_ID ) );
        MyApps myapps = MyAppsHome.findByPrimaryKey( nIdApplication, getPlugin(  ) );
        myapps.setName( request.getParameter( PARAMETER_APP_NAME ) );
        myapps.setUrl( request.getParameter( PARAMETER_APP_URL ) );
        myapps.setPassword( request.getParameter( PARAMETER_USER_PASSWORD ) );
        myapps.setCode( request.getParameter( PARAMETER_USER_NAME ) );
        myapps.setCodeHeading( request.getParameter( PARAMETER_CODE_HEADING ) );
        myapps.setData( request.getParameter( PARAMETER_USER_FIELD ) );
        myapps.setDataHeading( request.getParameter( PARAMETER_USER_FIELD_HEADING ) );
        myapps.setDescription( request.getParameter( PARAMETER_APP_DESCRIPTION ) );

        boolean bUpdateImage = ( multiRequest.getParameter( PARAMETER_UPDATE_FILE ) != null );

        if ( bUpdateImage && ( multiRequest.getFile( PARAMETER_APP_ICON ) != null ) )
        {
            try
            {
                FileItem itemIcon = multiRequest.getFile( PARAMETER_APP_ICON );

                if ( itemIcon != null )
                {
                    byte[] bytes = itemIcon.get(  );
                    String strMimeType = itemIcon.getContentType(  );
                    myapps.setIconContent( bytes );
                    myapps.setIconMimeType( strMimeType );
                }

                MyAppsHome.update( myapps, getPlugin(  ) );
            }
            catch ( Exception e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }
        }

        MyAppsHome.update( myapps, getPlugin(  ) );

        return getHomeUrl( request );
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
