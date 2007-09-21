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
package fr.paris.lutece.plugins.dbpage.web;

import fr.paris.lutece.plugins.dbpage.business.DbPageDatabase;
import fr.paris.lutece.plugins.dbpage.business.DbPageDatabaseHome;
import fr.paris.lutece.plugins.dbpage.business.DbPageDatabaseSection;
import fr.paris.lutece.plugins.dbpage.business.DbPageDatabaseSectionHome;
import fr.paris.lutece.plugins.dbpage.business.DbPageDatabaseType;
import fr.paris.lutece.plugins.dbpage.business.DbPageDatabaseTypeHome;
import fr.paris.lutece.plugins.dbpage.business.DbPageHome;
import fr.paris.lutece.plugins.dbpage.business.portlet.DbPagePortletHome;
import fr.paris.lutece.plugins.dbpage.service.DbPageConnectionService;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.service.database.AppConnectionService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.filesystem.UploadUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.SQLException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage dbpage features ( manage, create, modify, remove the dbpage)
 *
 */
public class DbPageJspBean extends PluginAdminPageJspBean
{
    //The Templates
    private static final String TEMPLATE_DBPAGES = "admin/plugins/dbpage/manage_dbpages.html";
    private static final String TEMPLATE_MODIFY_SECTION = "admin/plugins/dbpage/modify_section.html";
    private static final String TEMPLATE_MODIFY_DBPAGE = "admin/plugins/dbpage/modify_dbpage.html";
    private static final String TEMPLATE_CREATE_SECTION = "admin/plugins/dbpage/create_section.html";
    private static final String TEMPLATE_CREATE_DBPAGE = "admin/plugins/dbpage/create_dbpage.html";

    //The Bookmarks
    private static final String MARK_DBPAGE = "dbpage";
    private static final String MARK_DBPAGE_ID = "dbpage_id";
    private static final String MARK_DBPAGE_SECTIONS = "dbpage_sections";
    private static final String MARK_SECTION_TYPE_LIST = "type_list";
    private static final String MARK_SECTION_POOL_LIST = "pool_list";
    private static final String MARK_SECTION_ID = "section_id";
    private static final String MARK_TYPE_LIST = "type_list";
    private static final String MARK_SECTION = "section";
    private static final String MARK_SECTION_ROLE_LIST = "role_list";
    private static final String MARK_WORKGROUP_LIST = "workgroup_list";

    //The Parameters
    private static final String PARAMETER_DBPAGE_ID = "dbpage_id";
    private static final String PARAMETER_DBPAGE_NAME = "dbpage_name";
    private static final String PARAMETER_DBPAGE_TITLE = "dbpage_title";
    private static final String PARAMETER_SECTION_ID = "section_id";
    private static final String PARAMETER_SECTION_ORDER = "section_order";
    private static final String PARAMETER_ITEMS_PER_PAGE = "items_per_page";
    private static final String PARAMETER_SECTION_TYPE_ID = "type_id";
    private static final String PARAMETER_SECTION_DESC_SQL = "section_desc_sql";
    private static final String PARAMETER_SECTION_DESC_COLUMN = "section_desc_column";
    private static final String PARAMETER_SECTION_POOL = "section_pool";
    private static final String PARAMETER_SECTION_TITLE = "section_title";
    private static final String PARAMETER_SECTION_TEMPLATE = "section_template";
    private static final String PARAMETER_SECTION_ROLE_ID = "role";
    private static final String PARAMETER_TEMPLATE_DEFAULT = "template_default";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_DBPAGE_WORKGROUP = "dbpage_workgroup";
    private static final String PROPERTY_FILES_PATH = "dbpage.files.path";
    private static final String PROPERTY_DBPAGE_PER_PAGE = "dbpage.dbPagePerPage";
    private static final String PROPERTY_PAGE_TITLE_CREATE = "dbpage.create_dbpage.pageTitle";
    private static final String PROPERTY_PAGE_MODIFY_SECTION = "dbpage.modify_section.pageTitle";
    private static final String PROPERTY_PAGE_CREATE_SECTION = "dbpage.create_section.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_DBPAGES = "dbpage.manage_dbpages.pageTitle";
    private static final String PROPERTY_DEFAULT_ITEMS_PER_PAGE = "dbpage.itemsPerPage";

    //Bookmarks
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_DBPAGE_LIST = "dbpage_list";

    /**
     * The rights attributes
     */
    public static final String RIGHT_DBPAGE_MANAGEMENT = "DBPAGE_MANAGEMENT";

    //Messages
    private static final String MESSAGE_DBPAGE_LINKED_PORTLET = "dbpage.message.dbpageLinkedPortlet";
    private static final String MESSAGE_CONFIRM_DELETE_DBPAGE = "dbpage.message.confirmDeleteDbPage";
    private static final String MESSAGE_CONFIRM_REMOVE_SECTION = "dbpage.message.confirmRemoveSection";
    private static final String MESSAGE_DBPAGE_NAME_ALREADY_EXISTS = "dbpage.message.nameAlreadyExists";
    private static final String MESSAGE_SQL_ERROR_IN_QUERY = "dbpage.message.sqlErrorInQuery";

    // Jsp
    private static final String JSP_DELETE_DBPAGE = "jsp/admin/plugins/dbpage/DoDeleteDbPage.jsp";
    private static final String JSP_DO_REMOVE_SECTION = "jsp/admin/plugins/dbpage/DoRemoveSection.jsp";
    private static final String URL_MODIFY_DBPAGE_JSP = "jsp/admin/plugins/dbpage/ModifyDbPage.jsp";
    private static final String URL_MODIFY_SECTION_JSP = "jsp/admin/plugins/dbpage/ModifySection.jsp";
    private static final String JSP_MANAGE_DBPAGE = "ManageDbPages.jsp";
    private static final String JSP_MODIFY_DBPAGE = "ModifyDbPage.jsp";
    private static final FileFilter dirFilter = new FileFilter(  )
        {
            public boolean accept( File pathname )
            {
                return pathname.isDirectory(  );
            }
        };

    //Variables
    private int _nItemsPerPage;
    private String _strCurrentPageIndex;

    /**
     * Creates a new DbPageJspBean object.
     */
    public DbPageJspBean(  )
    {
    }

    /**
     * Returns the list of dbPages
     *
     * @param request The Http request
     * @return the dbPage list
     */
    public String getManageDbPages( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_DBPAGES );
        _nItemsPerPage = getItemsPerPage( request );
        _strCurrentPageIndex = getPageIndex( request );

        Collection<DbPageDatabase> colDbPage = DbPageDatabaseHome.findDbPageDatabasesList( getPlugin(  ) );
        colDbPage = AdminWorkgroupService.getAuthorizedCollection( colDbPage, getUser(  ) );

        Paginator paginator = new Paginator( (List<DbPageDatabase>) colDbPage, _nItemsPerPage, getHomeUrl( request ),
                PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        HashMap model = new HashMap(  );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_DBPAGE_LIST, paginator.getPageItems(  ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_DBPAGES, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Returns the form to create a section
     *
     * @param request The Http request
     * @return the html code of the dbPage form
     */
    public String getCreateSection( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_CREATE_SECTION );

        HashMap model = new HashMap(  );
        String strPageId = request.getParameter( PARAMETER_DBPAGE_ID );

        Collection<DbPageDatabase> colDbPage = DbPageDatabaseHome.findDbPageDatabasesList( getPlugin(  ) );
        colDbPage = AdminWorkgroupService.getAuthorizedCollection( colDbPage, getUser(  ) );

        if ( colDbPage.size(  ) == 0 )
        {
            // Return dbpage management page
            return getManageDbPages( request );
        }

        int nPageId = Integer.parseInt( strPageId );
        model.put( MARK_DBPAGE_ID, nPageId );
        model.put( MARK_SECTION_TYPE_LIST, DbPageDatabaseTypeHome.findTypeComboList( getPlugin(  ) ) );

        //Add roles List
        ReferenceList roleList = RoleHome.getRolesList(  );
        model.put( MARK_SECTION_ROLE_LIST, roleList );

        ReferenceList listPools = new ReferenceList(  );
        AppConnectionService.getPoolList( listPools );

        model.put( MARK_SECTION_POOL_LIST, listPools );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_SECTION, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the form to update the section
     *
     * @param request The Http request
     * @return the html code of the dbPage form
     */
    public String getModifySection( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_MODIFY_SECTION );

        HashMap model = new HashMap(  );
        String strPageId = request.getParameter( PARAMETER_DBPAGE_ID );
        int nPageId = Integer.parseInt( strPageId );
        DbPageDatabase dbPageDatabase = DbPageDatabaseHome.findByPrimaryKey( nPageId, getPlugin(  ) );

        if ( !AdminWorkgroupService.isAuthorized( dbPageDatabase, getUser(  ) ) )
        {
            // Return dbpage management page
            return getManageDbPages( request );
        }

        int nSectionId = Integer.parseInt( request.getParameter( PARAMETER_SECTION_ID ) );
        DbPageDatabaseSection section = DbPageDatabaseSectionHome.findByPrimaryKey( nSectionId, getPlugin(  ) );
        ReferenceList listPools = new ReferenceList(  );
        AppConnectionService.getPoolList( listPools );
        model.put( MARK_DBPAGE_ID, nPageId );
        model.put( MARK_SECTION_ID, nSectionId );
        model.put( MARK_SECTION, section );
        model.put( MARK_SECTION_TYPE_LIST, DbPageDatabaseTypeHome.findTypeComboList( getPlugin(  ) ) );
        model.put( MARK_SECTION_POOL_LIST, listPools );

        //Add roles List
        ReferenceList roleList = RoleHome.getRolesList(  );
        model.put( MARK_SECTION_ROLE_LIST, roleList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_SECTION, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the change form of a dbPage
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doModifyDbPage( HttpServletRequest request )
    {
        String strPageId = request.getParameter( PARAMETER_DBPAGE_ID );
        int nPageId = Integer.parseInt( strPageId );
        DbPageDatabase dbPage = DbPageDatabaseHome.findByPrimaryKey( nPageId, getPlugin(  ) );

        if ( !AdminWorkgroupService.isAuthorized( dbPage, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        dbPage.setParamName( request.getParameter( PARAMETER_DBPAGE_NAME ) );
        dbPage.setTitle( request.getParameter( PARAMETER_DBPAGE_TITLE ) );
        dbPage.setWorkgroup( request.getParameter( PARAMETER_DBPAGE_WORKGROUP ) );

        if ( request.getParameter( PARAMETER_DBPAGE_TITLE ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        DbPageDatabaseHome.update( dbPage, getPlugin(  ) );

        return JSP_MANAGE_DBPAGE;
    }

    /**
     * Creates a section
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doCreateSection( HttpServletRequest request )
    {
        boolean bDefaultTemplate = true;

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        String strDefaultTemplate = multipartRequest.getParameter( PARAMETER_TEMPLATE_DEFAULT );
        String strPageId = request.getParameter( PARAMETER_DBPAGE_ID );
        int nPageId = Integer.parseInt( strPageId );
        DbPageDatabase dbPage = DbPageDatabaseHome.findByPrimaryKey( nPageId, getPlugin(  ) );

        if ( !AdminWorkgroupService.isAuthorized( dbPage, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        String strPageDesc = UploadUtil.cleanFileName( dbPage.getParamName(  ) );
        String strDirectoryPath = AppPropertiesService.getProperty( PROPERTY_FILES_PATH );
        String strAbsolutePath = AppPathService.getWebAppPath(  ) + strDirectoryPath + strPageDesc;

        if ( !new File( strAbsolutePath ).isDirectory(  ) )
        {
            File fileFolder = new File( strAbsolutePath );
            fileFolder.mkdir(  );
        }

        String strSectionTitle = multipartRequest.getParameter( PARAMETER_SECTION_TITLE );

        if ( strSectionTitle.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( ( strDefaultTemplate == null ) || !strDefaultTemplate.equals( "on" ) )
        {
            bDefaultTemplate = false;
        }

        DbPageDatabaseSection section = new DbPageDatabaseSection(  );
        section.setTitle( strSectionTitle );

        String strSql = multipartRequest.getParameter( PARAMETER_SECTION_DESC_SQL );
        String strPoolName = multipartRequest.getParameter( PARAMETER_SECTION_POOL );

        try
        {
            DbPageHome.selectRows( strSql, DbPageConnectionService.getConnectionService( strPoolName ) );
        }
        catch ( SQLException e )
        {
            UrlItem url = new UrlItem( URL_MODIFY_DBPAGE_JSP );
            url.addParameter( PARAMETER_DBPAGE_ID, strPageId );

            Object[] messageArgs = { e.getMessage(  ) };

            return AdminMessageService.getMessageUrl( request, MESSAGE_SQL_ERROR_IN_QUERY, messageArgs, url.getUrl(  ),
                AdminMessage.TYPE_STOP );
        }

        section.setSql( strSql );
        section.setColumn( multipartRequest.getParameter( PARAMETER_SECTION_DESC_COLUMN ) );
        section.setPool( strPoolName );
        section.setIdType( Integer.parseInt( multipartRequest.getParameter( PARAMETER_SECTION_TYPE_ID ) ) );
        section.setIdPage( nPageId );
        section.setRole( multipartRequest.getParameter( PARAMETER_SECTION_ROLE_ID ) );

        int nIdOrder = DbPageDatabaseSectionHome.getMaxIdByOrder( nPageId, getPlugin(  ) );
        section.setOrder( nIdOrder + 1 );

        FileItem fileItem = multipartRequest.getFile( PARAMETER_SECTION_TEMPLATE );

        try
        {
            localTemplateFile( section, fileItem, strPageDesc, bDefaultTemplate );
        }
        catch ( IOException e )
        {
            //Add in error log
        }

        DbPageDatabaseSectionHome.create( section, getPlugin(  ) );

        UrlItem url = new UrlItem( JSP_MODIFY_DBPAGE );
        url.addParameter( PARAMETER_DBPAGE_ID, nPageId );

        return url.getUrl(  );
    }

    /**
     * Modifies a section
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doModifySection( HttpServletRequest request )
    {
        boolean bDefaultTemplate = true;

        try
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

            String strSectionId = multipartRequest.getParameter( PARAMETER_SECTION_ID );
            int nSectionId = Integer.parseInt( strSectionId );
            String strDefaultTemplate = multipartRequest.getParameter( PARAMETER_TEMPLATE_DEFAULT );
            String strSectionTitle = multipartRequest.getParameter( PARAMETER_SECTION_TITLE );

            if ( strSectionTitle.equalsIgnoreCase( "" ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            if ( ( strDefaultTemplate == null ) || !strDefaultTemplate.equals( "on" ) )
            {
                bDefaultTemplate = false;
            }

            DbPageDatabaseSection section = DbPageDatabaseSectionHome.findByPrimaryKey( nSectionId, getPlugin(  ) );
            int nOrder = section.getOrder(  );
            int nPageId = section.getIdPage(  );
            String strPageId = Integer.toString( nPageId );
            DbPageDatabase dbPage = DbPageDatabaseHome.findByPrimaryKey( nPageId, getPlugin(  ) );

            if ( !AdminWorkgroupService.isAuthorized( dbPage, getUser(  ) ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
            }

            String strPageDesc = UploadUtil.cleanFileName( dbPage.getParamName(  ) );
            section.setIdPage( nPageId );
            section.setOrder( nOrder );

            String strSql = multipartRequest.getParameter( PARAMETER_SECTION_DESC_SQL );
            String strPoolName = multipartRequest.getParameter( PARAMETER_SECTION_POOL );

            try
            {
                DbPageHome.selectRows( strSql, DbPageConnectionService.getConnectionService( strPoolName ) );
            }
            catch ( SQLException e )
            {
                UrlItem url = new UrlItem( URL_MODIFY_SECTION_JSP );
                url.addParameter( PARAMETER_SECTION_ID, strSectionId );
                url.addParameter( PARAMETER_DBPAGE_ID, strPageId );

                Object[] messageArgs = { e.getMessage(  ) };

                return AdminMessageService.getMessageUrl( request, MESSAGE_SQL_ERROR_IN_QUERY, messageArgs,
                    url.getUrl(  ), AdminMessage.TYPE_STOP );
            }

            section.setIdType( Integer.parseInt( multipartRequest.getParameter( PARAMETER_SECTION_TYPE_ID ) ) );
            section.setColumn( multipartRequest.getParameter( PARAMETER_SECTION_DESC_COLUMN ) );
            section.setPool( strPoolName );
            section.setTitle( multipartRequest.getParameter( PARAMETER_SECTION_TITLE ) );
            section.setSql( strSql );
            section.setRole( multipartRequest.getParameter( PARAMETER_SECTION_ROLE_ID ) );

            FileItem fileItem = multipartRequest.getFile( PARAMETER_SECTION_TEMPLATE );
            localTemplateFile( section, fileItem, strPageDesc, bDefaultTemplate );

            DbPageDatabaseSectionHome.update( section, getPlugin(  ) );

            UrlItem url = new UrlItem( JSP_MODIFY_DBPAGE );
            url.addParameter( PARAMETER_DBPAGE_ID, strPageId );

            return url.getUrl(  );
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage(  ), e );

            return getHomeUrl( request );
        }
    }

    /**
     * Returns the form to create a DbPage
     *
     * @param request The Http request
     * @return the html code of the contact form
     */
    public String getCreateDbPage( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE );
        model.put( MARK_WORKGROUP_LIST, AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_DBPAGE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the creation of a DbPage
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    public String doCreateDbPage( HttpServletRequest request )
    {
        DbPageDatabase dbPage = new DbPageDatabase(  );

        if ( request.getParameter( PARAMETER_DBPAGE_NAME ).equals( "" ) ||
                request.getParameter( PARAMETER_DBPAGE_TITLE ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( DbPageDatabaseHome.findByName( request.getParameter( PARAMETER_DBPAGE_NAME ), getPlugin(  ) ) != null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_DBPAGE_NAME_ALREADY_EXISTS,
                AdminMessage.TYPE_STOP );
        }

        dbPage.setParamName( request.getParameter( PARAMETER_DBPAGE_NAME ) );
        dbPage.setTitle( request.getParameter( PARAMETER_DBPAGE_TITLE ) );
        dbPage.setWorkgroup( request.getParameter( PARAMETER_DBPAGE_WORKGROUP ) );

        DbPageDatabaseHome.create( dbPage, getPlugin(  ) );

        DbPageDatabase dbPagedb = DbPageDatabaseHome.findByName( dbPage.getParamName(  ), getPlugin(  ) );
        UrlItem url = new UrlItem( JSP_MODIFY_DBPAGE );
        url.addParameter( PARAMETER_DBPAGE_ID, dbPagedb.getId(  ) );

        return url.getUrl(  );
    }

    /**
     * Process the deletion of a DbPage
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    public String doDeleteDbPage( HttpServletRequest request )
    {
        String strPageId = request.getParameter( PARAMETER_DBPAGE_ID );
        int nPageId = Integer.parseInt( strPageId );

        DbPageDatabase dbPage = DbPageDatabaseHome.findByPrimaryKey( nPageId, getPlugin(  ) );

        if ( !AdminWorkgroupService.isAuthorized( dbPage, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        String strDirectoryPath = AppPropertiesService.getProperty( PROPERTY_FILES_PATH );
        String strDbPageName = UploadUtil.cleanFileName( dbPage.getParamName(  ) );
        String filePath = AppPathService.getWebAppPath(  ) + strDirectoryPath + strDbPageName;
        File file = new File( filePath );
        deleteDirectory( file );
        DbPageDatabaseHome.remove( nPageId, getPlugin(  ) );

        return getHomeUrl( request );
    }

    /**
     * Process the deletion of a section
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    public String doRemoveSection( HttpServletRequest request )
    {
        String strPageId = request.getParameter( PARAMETER_DBPAGE_ID );
        int nPageId = Integer.parseInt( strPageId );
        DbPageDatabase dbPage = DbPageDatabaseHome.findByPrimaryKey( nPageId, getPlugin(  ) );

        if ( !AdminWorkgroupService.isAuthorized( dbPage, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        int nSectionId = Integer.parseInt( request.getParameter( PARAMETER_SECTION_ID ) );
        DbPageDatabaseSectionHome.remove( nSectionId, getPlugin(  ) );

        UrlItem url = new UrlItem( JSP_MODIFY_DBPAGE );
        url.addParameter( PARAMETER_DBPAGE_ID, strPageId );

        return url.getUrl(  );
    }

    /**
     * Returns the form for modification of a dbpage
     * @param request  the request
     * @return The modification form
     */
    public String getModifyDbPage( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_MODIFY_SECTION );

        String strPageId = request.getParameter( PARAMETER_DBPAGE_ID );
        int nPageId = Integer.parseInt( strPageId );
        DbPageDatabase dbPage = DbPageDatabaseHome.findByPrimaryKey( nPageId, getPlugin(  ) );

        if ( !AdminWorkgroupService.isAuthorized( dbPage, getUser(  ) ) )
        {
            // Return dbpage management page
            return getManageDbPages( request );
        }

        List<DbPageDatabaseSection> listSections = DbPageDatabaseSectionHome.findSectionsByPage( nPageId, getPlugin(  ) );
        List<DbPageDatabaseType> listTypes = DbPageDatabaseTypeHome.findDbPageDatabaseTypesList( getPlugin(  ) );
        HashMap model = new HashMap(  );
        model.put( MARK_DBPAGE, dbPage );
        model.put( MARK_DBPAGE_SECTIONS, listSections );
        model.put( MARK_TYPE_LIST, listTypes );
        model.put( MARK_WORKGROUP_LIST, AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_DBPAGE, getLocale(  ), model );

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
                nItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DBPAGE_PER_PAGE, nDefaultItemsPerPage );
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
     * Process the confirmation of the removal of a dbpage
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doConfirmDelete( HttpServletRequest request )
    {
        String strPageId = request.getParameter( PARAMETER_DBPAGE_ID );
        int nPageId = Integer.parseInt( strPageId );
        DbPageDatabase dbPage = DbPageDatabaseHome.findByPrimaryKey( nPageId, getPlugin(  ) );

        if ( !AdminWorkgroupService.isAuthorized( dbPage, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        String strDeleteUrl = JSP_DELETE_DBPAGE + "?" + PARAMETER_DBPAGE_ID + "=" + strPageId;
        String strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_DBPAGE_LINKED_PORTLET,
                AdminMessage.TYPE_STOP );

        // Check whether a dbpage is linked to a portlet
        if ( !DbPagePortletHome.checkNoDbPageInPortlet( nPageId ) )
        {
            //Delete all sections associated to the dbpage
            List<DbPageDatabaseSection> listSections = DbPageDatabaseSectionHome.findSectionsByPage( nPageId,
                    getPlugin(  ) );

            for ( DbPageDatabaseSection section : listSections )
            {
                DbPageDatabaseSectionHome.remove( section.getId(  ), getPlugin(  ) );
            }

            DbPageDatabase dbpage = DbPageDatabaseHome.findByPrimaryKey( nPageId, getPlugin(  ) );
            Object[] messageArgs = { dbpage.getTitle(  ) };
            strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_DBPAGE, messageArgs,
                    strDeleteUrl, AdminMessage.TYPE_CONFIRMATION );
        }

        return strUrl;
    }

    /**
     * Manages the removal form of a section whose identifier is in the http request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    public String getConfirmRemoveSection( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( JSP_DO_REMOVE_SECTION );
        String strPageId = request.getParameter( PARAMETER_DBPAGE_ID );
        int nPageId = Integer.parseInt( strPageId );
        DbPageDatabase dbPage = DbPageDatabaseHome.findByPrimaryKey( nPageId, getPlugin(  ) );

        if ( !AdminWorkgroupService.isAuthorized( dbPage, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        int nIdSection = Integer.parseInt( request.getParameter( PARAMETER_SECTION_ID ) );
        url.addParameter( PARAMETER_SECTION_ID, nIdSection );
        url.addParameter( PARAMETER_DBPAGE_ID, strPageId );

        DbPageDatabaseSection section = DbPageDatabaseSectionHome.findByPrimaryKey( nIdSection, getPlugin(  ) );
        Object[] messageArgs = { section.getTitle(  ) };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_SECTION, messageArgs, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Create and Update the local downloaded file
     * @param section The section
     * @param fileItem The fileItem extracted from the request
     * @param strDbPageName The dbpage name
     * @param bDefaultTemplate Whether to use a default template or not
     * @throws java.io.IOException Exception throwed while writing the template file
     */
    private void localTemplateFile( DbPageDatabaseSection section, FileItem fileItem, String strDbPageName,
        boolean bDefaultTemplate ) throws IOException
    {
        String strFileName = fileItem.getName(  );
        File file = new File( strFileName );

        if ( !file.getName(  ).equals( "" ) && !strDbPageName.equals( null ) )
        {
            String strNameFile = file.getName(  );

            String strDirectoryPath = AppPropertiesService.getProperty( PROPERTY_FILES_PATH );
            String filePath = AppPathService.getWebAppPath(  ) + strDirectoryPath + strDbPageName + "/" + strNameFile;

            //if file is not a directory and default template is off
            if ( !bDefaultTemplate && !new File( filePath ).isDirectory(  ) )
            {
                file = new File( filePath );

                if ( file.exists(  ) )
                {
                    file.delete(  );
                }

                FileOutputStream fosFile = new FileOutputStream( file );
                fosFile.flush(  );
                fosFile.write( fileItem.get(  ) );
                fosFile.close(  );
                section.setTemplatePath( strDbPageName + "/" + strNameFile );
            }
        }
        else
        {
            section.setTemplatePath( "" );
        }
    }

    /**
     * Performs the move up action
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doSectionMoveUp( HttpServletRequest request )
    {
        String strPageId = request.getParameter( PARAMETER_DBPAGE_ID );
        int nPageId = Integer.parseInt( strPageId );
        DbPageDatabase dbPage = DbPageDatabaseHome.findByPrimaryKey( nPageId, getPlugin(  ) );

        if ( !AdminWorkgroupService.isAuthorized( dbPage, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        int nIdOrder = Integer.parseInt( request.getParameter( PARAMETER_SECTION_ORDER ) );

        if ( nIdOrder > 1 )
        {
            List<DbPageDatabaseSection> list = DbPageDatabaseSectionHome.findSectionsByPage( nPageId, getPlugin(  ) );

            DbPageDatabaseSection section1 = list.get( nIdOrder - 1 );
            DbPageDatabaseSection section2 = list.get( nIdOrder - 2 );
            DbPageDatabaseSectionHome.reorderSections( section1.getId(  ), nIdOrder - 1, section2.getId(  ), nIdOrder,
                getPlugin(  ) );
        }

        UrlItem url = new UrlItem( JSP_MODIFY_DBPAGE );
        url.addParameter( PARAMETER_DBPAGE_ID, nPageId );

        return url.getUrl(  );
    }

    /**
     * Performs the move down action
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doSectionMoveDown( HttpServletRequest request )
    {
        String strPageId = request.getParameter( PARAMETER_DBPAGE_ID );
        int nPageId = Integer.parseInt( strPageId );

        DbPageDatabase dbPage = DbPageDatabaseHome.findByPrimaryKey( nPageId, getPlugin(  ) );

        if ( !AdminWorkgroupService.isAuthorized( dbPage, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        int nIdOrder = Integer.parseInt( request.getParameter( PARAMETER_SECTION_ORDER ) );

        List<DbPageDatabaseSection> list = DbPageDatabaseSectionHome.findSectionsByPage( nPageId, getPlugin(  ) );

        if ( nIdOrder < list.size(  ) )
        {
            DbPageDatabaseSection section1 = list.get( nIdOrder - 1 );
            DbPageDatabaseSection section2 = list.get( nIdOrder );
            DbPageDatabaseSectionHome.reorderSections( section1.getId(  ), nIdOrder + 1, section2.getId(  ), nIdOrder,
                getPlugin(  ) );
        }

        UrlItem url = new UrlItem( JSP_MODIFY_DBPAGE );
        url.addParameter( PARAMETER_DBPAGE_ID, nPageId );

        return url.getUrl(  );
    }

    /**
     * Deletes a directory recursively.
     * @param fdirectory The directory to be deleted
     */
    private static void deleteDirectory( File fdirectory )
    {
        // We use a Stack (LIFO) to keep track of the directories to delete
        Stack<File> dirsToDelete = new Stack<File>(  );

        if ( fdirectory.exists(  ) )
        {
            // The stack is initialized with the main directory
            dirsToDelete.push( fdirectory );

            // Loop until all directories have been deleted
            while ( !dirsToDelete.empty(  ) )
            {
                // Look at the directory on top of the stack (don't remove it!)
                File currentDir = (File) dirsToDelete.peek(  );

                // Are there any subdirectories?
                File[] subDirs = currentDir.listFiles( dirFilter );

                if ( ( subDirs.length > 0 ) && ( subDirs != null ) )
                {
                    // If so, add them to the stack
                    for ( int i = 0; i < subDirs.length; i++ )
                    {
                        dirsToDelete.push( subDirs[i] );
                    }
                }
                else
                {
                    // If not, delete all files in the directory
                    File[] files = currentDir.listFiles(  );

                    for ( int i = 0; i < files.length; i++ )
                    {
                        files[i].delete(  );
                    }

                    // Then delete the directory
                    currentDir.delete(  );
                    // Then remove the directory from the stack
                    dirsToDelete.pop(  );
                }
            }
        }
    }
}
