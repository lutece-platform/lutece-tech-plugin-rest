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
package fr.paris.lutece.plugins.folderlisting.web;

import fr.paris.lutece.plugins.folderlisting.business.FolderListingDatabase;
import fr.paris.lutece.plugins.folderlisting.business.FolderListingDatabaseHome;
import fr.paris.lutece.plugins.folderlisting.business.portlet.FolderListingPortletHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;

import java.io.File;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * Creates a new FolderListingDatabaseJspBean object.
 */
public class FolderListingDatabaseJspBean extends PluginAdminPageJspBean
{
    //////////////////////////////////////////////////////////////////////////////////////////
    // Constants
    public static final String RIGHT_FOLDERLISTING_MANAGEMENT = "FOLDERLISTING_MANAGEMENT";

    // Markers
    private static final String MARK_FOLDER_LIST = "folder_list";
    private static final String MARK_FOLDER = "folder";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_WORKGROUP_LIST = "workgroup_list";

    // Parameters
    private static final String PARAMETER_FOLDER_NAME = "folder_name";
    private static final String PARAMETER_FOLDER_PATH = "folder_path";
    private static final String PARAMETER_FOLDER_WORKGROUP = "folder_workgroup";
    private static final String PARAMETER_ID_FOLDER = "id_folder";
    private static final String PARAMETER_PAGE_INDEX = "page_index";

    // properties for page titles
    private static final String PROPERTY_PAGE_TITLE_FOLDERS = "folderlisting.folders.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE = "folderlisting.create_folder.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY = "folderlisting.modify_folder.pageTitle";
    private static final String PROPERTY_FOLDER_PER_PAGE = "folderlisting.folderPerPage";

    // Messages
    private static final String MESSAGE_FOLDER_INVALID = "folderlisting.message.folderInvalid";
    private static final String MESSAGE_CONFIRM_DELETE_FOLDER = "folderlisting.message.confirmDeleteFolder";
    private static final String MESSAGE_FOLDER_LINKED_PORTLET = "folderlisting.message.folderLinkedPortlet";

    // Templates
    private static final String TEMPLATE_FOLDERS = "/admin/plugins/folderlisting/folders.html";
    private static final String TEMPLATE_CREATE_FOLDER = "/admin/plugins/folderlisting/create_folder.html";
    private static final String TEMPLATE_MODIFY_FOLDER = "/admin/plugins/folderlisting/modify_folder.html";

    //Jsp
    private static final String JSP_DELETE_FOLDER = "jsp/admin/plugins/folderlisting/DoDeleteFolder.jsp";
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;

    public FolderListingDatabaseJspBean(  )
    {
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_FOLDER_PER_PAGE, 10 );
    }

    /**
     * Returns the creation form of a folder
     *
     * @param request The Http request
     *
     * @return Html form
     */
    public String getCreateFolder( HttpServletRequest request )
    {
        HashMap<String, ReferenceList> model = new HashMap<String, ReferenceList>(  );
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE );

        model.put( MARK_WORKGROUP_LIST, AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_FOLDER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns folders management form
     *
     * @param request The Http request
     *
     * @return Html form
     */
    public String getManageFolders( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_FOLDERS );

        //_nItemsPerPage = getItemsPerPage( request );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );
        //_strCurrentPageIndex = getPageIndex( request );
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        List<FolderListingDatabase> listFolderList = FolderListingDatabaseHome.findFolderListingDatabasesList( getPlugin(  ) );
        listFolderList = (List<FolderListingDatabase>) AdminWorkgroupService.getAuthorizedCollection( listFolderList,
                getUser(  ) );

        Paginator paginator = new Paginator( listFolderList, _nItemsPerPage, getHomeUrl( request ),
                PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        HashMap model = new HashMap(  );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_FOLDER_LIST, paginator.getPageItems(  ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_FOLDERS, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Returns the form to update info about a folder
     *
     * @param request The Http request
     *
     * @return The HTML form to update info
     */
    public String getModifyFolder( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY );

        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_FOLDER ) );
        FolderListingDatabase folder = FolderListingDatabaseHome.findByPrimaryKey( nId, getPlugin(  ) );

        model.put( MARK_FOLDER, folder );
        model.put( MARK_WORKGROUP_LIST, AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_FOLDER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the confirmation of a removal of a folder
     *
     * @param request The Http Request
     *
     * @return A dynamic url
     */
    public String doConfirmDelete( HttpServletRequest request )
    {
        String strIdFolder = request.getParameter( PARAMETER_ID_FOLDER );
        int nIdFolder = Integer.parseInt( strIdFolder );
        String strDeleteUrl = JSP_DELETE_FOLDER + "?" + PARAMETER_ID_FOLDER + "=" + strIdFolder;
        String strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_FOLDER_LINKED_PORTLET,
                AdminMessage.TYPE_STOP );

        // Check whether a folder is linked to a portlet
        if ( FolderListingPortletHome.checkNoFolderInPortlet( nIdFolder ) )
        {
            FolderListingDatabase folder = FolderListingDatabaseHome.findByPrimaryKey( nIdFolder, getPlugin(  ) );
            Object[] messageArgs = { folder.getFolderName(  ) };
            strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_FOLDER, messageArgs,
                    strDeleteUrl, AdminMessage.TYPE_CONFIRMATION );
        }

        return strUrl;
    }

    /**
     * Process the data capture form of a new Folder
     *
     * @param request The Http Request
     *
     * @return The Jsp URL of the process result
     */
    public String doCreateFolder( HttpServletRequest request )
    {
        FolderListingDatabase folder = new FolderListingDatabase(  );
        String strFolderPath = request.getParameter( PARAMETER_FOLDER_PATH );
        strFolderPath = cleanPath( strFolderPath );
        folder.setFolderName( request.getParameter( PARAMETER_FOLDER_NAME ) );
        folder.setFolderPath( strFolderPath );
        folder.setWorkgroup( request.getParameter( PARAMETER_FOLDER_WORKGROUP ) );

        // Mandatory fields
        if ( request.getParameter( PARAMETER_FOLDER_NAME ).equals( "" ) ||
                request.getParameter( PARAMETER_FOLDER_PATH ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        String strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_FOLDER_INVALID, AdminMessage.TYPE_STOP );

        if ( isValidFolder( folder.getFolderPath(  ) ) )
        {
            FolderListingDatabaseHome.create( folder, getPlugin(  ) );
            strUrl = getHomeUrl( request );
        }

        return strUrl;
    }

    /**
     * Process folder removal
     *
     * @param request The Http request
     *
     * @return String The url of the administration console
     */
    public String doDeleteFolder( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_FOLDER ) );
        FolderListingDatabaseHome.remove( nId, getPlugin(  ) );

        return getHomeUrl( request );
    }

    /**
     * Process folder modification
     *
     * @param request The Http request
     *
     * @return String The url of the administration console
     */
    public String doModifyFolder( HttpServletRequest request )
    {
        int nIdFolder = Integer.parseInt( request.getParameter( PARAMETER_ID_FOLDER ) );
        String strFolderPath = request.getParameter( PARAMETER_FOLDER_PATH );
        String strFolderName = request.getParameter( PARAMETER_FOLDER_NAME );
        strFolderPath = cleanPath( strFolderPath );

        // Mandatory fields
        if ( request.getParameter( PARAMETER_FOLDER_PATH ).equals( "" ) ||
                request.getParameter( PARAMETER_FOLDER_NAME ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        FolderListingDatabase folder = FolderListingDatabaseHome.findByPrimaryKey( nIdFolder, getPlugin(  ) );
        folder.setFolderName( strFolderName );
        folder.setFolderPath( strFolderPath );
        folder.setWorkgroup( request.getParameter( PARAMETER_FOLDER_WORKGROUP ) );

        String strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_FOLDER_INVALID, AdminMessage.TYPE_STOP );

        if ( isValidFolder( folder.getFolderPath(  ) ) )
        {
            FolderListingDatabaseHome.update( folder, getPlugin(  ) );
            strUrl = getHomeUrl( request );
        }

        return strUrl;
    }

    /**
     * Verifies whether the folder is valid
     * @param strFolderPath The Path of the folder
     * @return true if the folder is a valid folder
     */
    private boolean isValidFolder( String strFolderPath )
    {
        strFolderPath = "/" + strFolderPath;

        String strAbsolutePath = AppPathService.getAbsolutePathFromRelativePath( strFolderPath );
        boolean bValue = false;

        if ( new File( strAbsolutePath ).exists(  ) )
        {
            bValue = true;
        }

        return bValue;
    }

    /**
     * Cleans the path of the folder path
     * @param strPath
     * @return
     */
    private String cleanPath( String strPath )
    {
        if ( !strPath.startsWith( "/" ) )
        {
            strPath = "/" + strPath;
        }

        return strPath;
    }
}
