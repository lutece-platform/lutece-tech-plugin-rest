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
package fr.paris.lutece.plugins.jcr.web.portlet;

import fr.paris.lutece.plugins.jcr.business.IRepositoryFile;
import fr.paris.lutece.plugins.jcr.business.RepositoryFileHome;
import fr.paris.lutece.plugins.jcr.business.admin.AdminJcrHome;
import fr.paris.lutece.plugins.jcr.business.admin.AdminView;
import fr.paris.lutece.plugins.jcr.business.admin.AdminWorkspace;
import fr.paris.lutece.plugins.jcr.business.portlet.Jsr170Portlet;
import fr.paris.lutece.plugins.jcr.business.portlet.Jsr170PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.fileupload.FileItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage Jsr170 Portlet features
 */
public class Jsr170PortletJspBean extends PortletJspBean
{
    // Rights
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";
    public static final int FILE_NOT_FOUND = 0;
    public static final int FILE_NOT_ALLOWED = 1;

    // Messages
    private static final String MESSAGE_FOLDER_INVALID = "jsr170.message.folderInvalid";
    private static final String MESSAGE_FILE_UPLOAD_EMPTY = "jsr170.message.fileUploadEmpty";
    private static final String MESSAGE_DIRECTORY_EMPTY = "jsr170.message.directoryEmpty";
    private static final String MESSAGE_FILE_ALREADY_EXIST = "jsr170.message.fileAlreadyExist";

    //Parameters
    private static final String PARAMETER_PAGE_ID = "page_id";
    private static final String PARAMETER_PORTLET_ID = "portlet_id";
    private static final String PARAMETER_PORTLET_TYPE_ID = "portlet_type_id";
    private static final String PARAMETER_VIEW_ID = "view_id";
    private static final String PARAMETER_ERROR_UPLOAD = "error_upload";

    //Markers
    private static final String MARK_FOLDER_LIST = "folder_list";
    private static final String MARK_FOLDER_ID = "folder_id";

    // Templates
    private static final String TEMPLATE_COMBO_FOLDER = "admin/plugins/jsr170/portlet/combo_folder.html";
    private static final String PARAMETER_VIEW = "view";
    private static final String PARAMETER_FILE_ID = "file_id";

    //  Util
    private static final String SEPARATOR = "/";
    public static final String CONTEXT_NAME = "jsr170";
    public static final String SPRING_REPOSITORY_FILE_DAO = ".repositoryFileDAO";
    private static final String PROPERTY_LABEL_ALL_VIEWS = "jsr170.label.allViews";
    private static final String PARAMETER_WORKSPACE_ID = "workspace_id";

    private String _strViewId = "default";
    private final Plugin _plugin = PluginService.getPlugin( "jsr170" );
    private Jsr170Portlet _portlet;
    private AdminView _view;
    private String _strIdPortlet;
    private AdminWorkspace _workspace;

    /**
     * Returns the jsr170 portlet creation form
     *
     * @param request The Http request
     *
     * @return The HTML form
     */
    public String getCreate( HttpServletRequest request )
    {
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        String strPortletIdType = request.getParameter( PARAMETER_PORTLET_TYPE_ID );

        Collection<AdminView> listViews = AdminJcrHome.getInstance(  ).findAllViews( _plugin );
        Collection filteredList = AdminWorkgroupService.getAuthorizedCollection( listViews, getUser(  ) );
        ReferenceList adminViewCombo = ReferenceList.convert( filteredList, "id", "name", true );

        adminViewCombo.addItem( 0, I18nService.getLocalizedString( PROPERTY_LABEL_ALL_VIEWS, getLocale( ) ) );
        HashMap model = new HashMap(  );
        model.put( MARK_FOLDER_LIST, adminViewCombo );
        model.put( MARK_FOLDER_ID, "" );

        HtmlTemplate template = getCreateTemplate( strIdPage, strPortletIdType, model );

        return template.getHtml(  );
    }

    /**
     * Returns the jsr170 portlet modification form
     *
     * @param request The Http request
     *
     * @return The HTML form
     */
    public String getModify( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        Jsr170Portlet portlet = (Jsr170Portlet) PortletHome.findByPrimaryKey( nPortletId );
        Collection<AdminView> listViews = AdminJcrHome.getInstance(  ).findAllViews( _plugin );
        Collection filteredList = AdminWorkgroupService.getAuthorizedCollection( listViews, getUser(  ) );

        ReferenceList adminViewCombo = ReferenceList.convert( filteredList, "id", "name", true );
        
        adminViewCombo.addItem( 0, I18nService.getLocalizedString( PROPERTY_LABEL_ALL_VIEWS, getLocale( ) ) );
        HashMap model = new HashMap(  );
        model.put( MARK_FOLDER_LIST, adminViewCombo );
        model.put( MARK_FOLDER_ID, portlet.hasDefaultView(  ) ? portlet.getDefaultView(  ) : ( -1 ) );

        HtmlTemplate template = getModifyTemplate( portlet, model );

        return template.getHtml(  );
    }

    /**
     * Returns portlet's properties prefix
     *
     * @return prefix
     */
    public String getPropertiesPrefix(  )
    {
        return "portlet.jsr170";
    }

    /**
     * Process portlet's creation
     *
     * @param request The Http request
     *
     * @return The Jsp management URL of the process result
     */
    public String doCreate( HttpServletRequest request )
    {
        Jsr170Portlet portlet = new Jsr170Portlet(  );
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        String strViewId = request.getParameter( PARAMETER_VIEW );
        int nIdPage = Integer.parseInt( strIdPage );
        int nViewId = Integer.parseInt( strViewId );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        portlet.setPageId( nIdPage );

        if ( strViewId != null )
        {
            portlet.setDefaultView( nViewId );
        }

        if ( isValidFolder( strViewId ) )
        {
            Jsr170PortletHome.getInstance(  ).create( portlet );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_FOLDER_INVALID, AdminMessage.TYPE_STOP );
        }

        //Displays the page with the new Portlet
        return getPageUrl( nIdPage );
    }

    /**
     * Process portlet's modification
     *
     * @param request The Http request
     *
     * @return The Jsp management URL of the process result
     */
    public String doModify( HttpServletRequest request )
    {
        // Getting portlet
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        Jsr170Portlet portlet = (Jsr170Portlet) PortletHome.findByPrimaryKey( nPortletId );
        String strViewId = request.getParameter( PARAMETER_VIEW );
        int nViewId = Integer.parseInt( strViewId );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        // Getting portlet's common attributs
        setPortletCommonData( request, portlet );

        portlet.setDefaultView( nViewId );

        portlet.update(  );

        return getPageUrl( portlet.getPageId(  ) );
    }

    /**
     * Return the folder listing depending on rights
     *
     * @param listFolders The list of folders
     * @param strDefaultFolderId the default id of the folder
     *
     * @return DOCUMENT ME!
     */
    String getFolderIndexCombo( ReferenceList listFolders, String strDefaultFolderId )
    {
        HashMap model = new HashMap(  );
        model.put( MARK_FOLDER_LIST, listFolders );
        model.put( MARK_FOLDER_ID, strDefaultFolderId );

        HtmlTemplate templateCombo = AppTemplateService.getTemplate( TEMPLATE_COMBO_FOLDER, getLocale(  ), model );

        return templateCombo.getHtml(  );
    }

    /**
     * Verifies whether the folder is valid
     *
     * @param strFolderId The Id corresponding to the folder
     *
     * @return true if the folder is a valid folder
     */
    private boolean isValidFolder( String strFolderId )
    {
        //TODO: still necessary?
        return true;
    }

    /**
     * Get the binary content corresponding to the file to display
     *
     * @param request the http request
     *
     * @return the content to display
     */
    public byte[] getFileContent( HttpServletRequest request )
    {
        getParameters( request );

        String strFileId = request.getParameter( "file_id" );

        IRepositoryFile file = RepositoryFileHome.getInstance(  ).getRepositoryFileById( _workspace, strFileId );
        AppLogService.debug( file );
        AppLogService.debug( file.getContent(  ) );

        if ( ( file != null ) && !file.isDirectory(  ) )
        {
            try
            {
                final InputStream in = file.getContent(  );

                final byte[] buf = new byte[1000];
                ByteArrayOutputStream out = new ByteArrayOutputStream(  );
                int len = 0;

                do
                {
                    len = in.read( buf );

                    if ( len == -1 )
                    {
                        break;
                    }

                    out.write( buf, 0, len );
                }
                while ( len > 0 );

                in.close(  );
                out.flush(  );

                return out.toByteArray(  );
            }
            catch ( FileNotFoundException e )
            {
                AppLogService.error( e.getMessage(  ), e );

                return null;
            }
            catch ( IOException e )
            {
                AppLogService.error( e.getMessage(  ), e );

                return null;
            }
        }

        return null;
    }

    /**
     * Get the binary content corresponding to the file to display
     *
     * @param request the http request
     *
     * @return the content to display
     */
    public byte[] getFileVersionContent( HttpServletRequest request )
    {
        getParameters( request );

        String strFileId = request.getParameter( "file_id" );
        String strVersion = request.getParameter( "version_name" );
        IRepositoryFile file = RepositoryFileHome.getInstance(  )
                                                 .getRepositoryFileVersionById( _workspace, strFileId, strVersion );
        AppLogService.debug( file );
        AppLogService.debug( file.getContent(  ) );

        if ( ( file != null ) && !file.isDirectory(  ) )
        {
            try
            {
                final InputStream in = file.getContent(  );

                final byte[] buf = new byte[1000];
                ByteArrayOutputStream out = new ByteArrayOutputStream(  );
                int len = 0;

                do
                {
                    len = in.read( buf );

                    if ( len == -1 )
                    {
                        break;
                    }

                    out.write( buf, 0, len );
                }
                while ( len > 0 );

                in.close(  );
                out.flush(  );

                return out.toByteArray(  );
            }
            catch ( FileNotFoundException e )
            {
                AppLogService.error( e.getMessage(  ), e );

                return null;
            }
            catch ( IOException e )
            {
                AppLogService.error( e.getMessage(  ), e );

                return null;
            }
        }

        return null;
    }

    /**
     * Upload a file in current workspace
     * @param request the request
     * @return the redirect url
     */
    public String uploadFile( HttpServletRequest request )
    {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        FileItem item = mRequest.getFile( "upload_file" );
        getParameters( request );

        String strFolderName = request.getParameter( "directory_path" );

        if ( strFolderName == null )
        {
            strFolderName = "";
        }

        IRepositoryFile parentFile = null;
        RepositoryFileHome instance = null;

        try
        {
            instance = RepositoryFileHome.getInstance(  );

            parentFile = instance.getRepositoryFile( _workspace, strFolderName );

            if ( ( item.getName(  ) == null ) || item.getName(  ).equals( "" ) )
            {
                String strMessageError = I18nService.getLocalizedString( MESSAGE_FILE_UPLOAD_EMPTY,
                        request.getLocale(  ) );

                return getRedirectUrlError( request, parentFile, strMessageError );
            }
            else
            {
                String strTmpFileUpload = parentFile.getAbsolutePath(  ) + SEPARATOR + item.getName(  );

                if ( exist( instance, strTmpFileUpload ) )
                {
                    String strMessageTmp = I18nService.getLocalizedString( MESSAGE_FILE_ALREADY_EXIST,
                            request.getLocale(  ) );

                    return getRedirectUrlError( request, parentFile, strMessageTmp );
                }

                File f = File.createTempFile( item.getName(  ), "" );
                item.write( f );

                instance.addFile( _workspace, strFolderName + "/" + item.getName(  ), f );
            }
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getLocalizedMessage(  ), e );

            return getRedirectUrlError( request, parentFile, e.getLocalizedMessage(  ) );
        }

        // reload portlet
        return getRedirectUrl( request, parentFile );
    }

    /**
     * Modify a file content
     * @param request
     * @return
     */
    public String modifyFile( HttpServletRequest request )
    {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        FileItem item = mRequest.getFile( "upload_file" );
        getParameters( request );

        String strFileId = request.getParameter( PARAMETER_FILE_ID );
        RepositoryFileHome instance = RepositoryFileHome.getInstance(  );

        IRepositoryFile parentFile = null;

        try
        {
            IRepositoryFile modifiedFile = instance.getRepositoryFileById( _workspace, strFileId );
            modifiedFile.setContent( item.getInputStream(  ) );

            if ( modifiedFile.getParentId(  ) != null )
            {
                parentFile = instance.getRepositoryFileById( _workspace, modifiedFile.getParentId(  ) );
            }
            else
            {
                parentFile = instance.getRepositoryFile( _workspace, "" );
            }

            instance.update( _workspace, modifiedFile );
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getLocalizedMessage(  ), e );

            return getRedirectUrlError( request, parentFile, e.getLocalizedMessage(  ) );
        }

        // reload portlet
        return getRedirectUrl( request, parentFile );
    }

    /**
     * Test if file exist in courant directory
     * @param instance instance fo repositoryFileHome
     * @param strTmpFileUpload the upload file
     * @return false if file not exist in directory
     */
    private boolean exist( RepositoryFileHome instance, String strTmpFileUpload )
    {
        try
        {
            instance.getRepositoryFile( _workspace, strTmpFileUpload );
        }
        catch ( Exception e )
        {
            return false;
        }

        return true;
    }

    /**
     * Get filename from request
     * @param request the request
     * @return the filename
     */
    public IRepositoryFile getFile( HttpServletRequest request )
    {
        getParameters( request );

        String strFileId = request.getParameter( "file_id" );

        IRepositoryFile file = RepositoryFileHome.getInstance(  ).getRepositoryFileById( _workspace, strFileId );

        return file;
    }

    /**
     * Delete a file from current workspace
     * @param request the request
     * @return the redirect url
     */
    public String deleteFile( HttpServletRequest request )
    {
        // TODO : add confirmation screen
        getParameters( request );

        String strFileId = request.getParameter( PARAMETER_FILE_ID );
        RepositoryFileHome instance = RepositoryFileHome.getInstance(  );
        IRepositoryFile currentFile = instance.getRepositoryFileById( _workspace, strFileId );
        String strPathFile = currentFile.getAbsolutePath(  );
        IRepositoryFile parentFile = instance.getRepositoryFile( _workspace,
                strPathFile.substring( 0, strPathFile.lastIndexOf( "/" ) ) );

        try
        {
            instance.removeRepositoryFile( _workspace, currentFile.getAbsolutePath(  ) );
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getLocalizedMessage(  ), e );

            return getRedirectUrlError( request, parentFile, e.getLocalizedMessage(  ) );
        }

        return getRedirectUrl( request, parentFile );
    }

    /**
     * Add file versioning
     * @param request the request
     * @return the redirect url
     */
    public String doAddFileVersioning( HttpServletRequest request )
    {
        // TODO : add confirmation screen
        getParameters( request );

        String strFileId = request.getParameter( PARAMETER_FILE_ID );
        RepositoryFileHome instance = RepositoryFileHome.getInstance(  );
        IRepositoryFile currentFile = instance.getRepositoryFileById( _workspace, strFileId );
        String strPathFile = currentFile.getAbsolutePath(  );
        IRepositoryFile parentFile = instance.getRepositoryFile( _workspace,
                strPathFile.substring( 0, strPathFile.lastIndexOf( "/" ) ) );

        try
        {
            instance.setVersionnable( _workspace, strFileId, true );
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getLocalizedMessage(  ), e );

            return getRedirectUrlError( request, parentFile, e.getLocalizedMessage(  ) );
        }

        return getRedirectUrl( request, parentFile );
    }

    /**
     * Create a directory in current workspace
     * @param request the request
     * @return the redirect url
     */
    public String createDirectory( HttpServletRequest request )
    {
        getParameters( request );

        String strFolderName = request.getParameter( "directory_path" );
        String strNewDirectoryName = request.getParameter( "new_directory" );

        String strAbsolutePath = strFolderName + "/" + strNewDirectoryName;
        IRepositoryFile parentFile = null;

        try
        {
            RepositoryFileHome instance = RepositoryFileHome.getInstance(  );

            parentFile = instance.getRepositoryFile( _workspace, strFolderName );

            if ( ( strNewDirectoryName == null ) || strNewDirectoryName.equals( "" ) )
            {
                String strMessageError = I18nService.getLocalizedString( MESSAGE_DIRECTORY_EMPTY, request.getLocale(  ) );

                return getRedirectUrlError( request, parentFile, strMessageError );
            }
            else
            {
                instance.createFolder( _workspace, strAbsolutePath );
            }
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getLocalizedMessage(  ), e );

            return getRedirectUrlError( request, parentFile, e.getLocalizedMessage(  ) );
        }

        // reload portlet
        return getRedirectUrl( request, parentFile );
    }

    /**
     * Select a new view
     * @param request the request
     * @return the url
     */
    public String changeView( HttpServletRequest request )
    {
        UrlItem url = AppPathService.resolveRedirectUrl( request, AppPathService.getPortalUrl(  ) );

        return url.getUrl(  );
    }

    /**
     * Get some parameter from request
     * @param request the request
     */
    private void getParameters( HttpServletRequest request )
    {
        _strIdPortlet = request.getParameter( Parameters.PORTLET_ID );
        _strViewId = request.getParameter( PARAMETER_VIEW_ID + "_" + _strIdPortlet );
        if(_strIdPortlet != null )
        {
            _portlet = (Jsr170Portlet) PortletHome.findByPrimaryKey( Integer.parseInt( _strIdPortlet ) );
            
            int nViewId = ( _portlet.hasDefaultView( ) ) ? _portlet.getDefaultView(  ) : Integer.parseInt( _strViewId );
            _view = AdminJcrHome.getInstance(  ).findViewById( nViewId, _plugin );
        }

        String strWorkspaceId = request.getParameter( PARAMETER_WORKSPACE_ID );
        int nWorkspaceId = ( strWorkspaceId == null ) ? _view.getWorkspaceId(  ) : Integer.parseInt( strWorkspaceId );
        _workspace = AdminJcrHome.getInstance(  ).findWorkspaceById( nWorkspaceId, _plugin );
    }

    /**
     * Return the url of page with parameters
     * @param request the request
     * @param file the current directory
     * @return
     */
    private String getRedirectUrl( HttpServletRequest request, IRepositoryFile file )
    {
        return getRedirectUrlError( request, file, null );
    }

    /**
     * Return the url of page with parameters
     * @param request the request
     * @param file the courant directory
     * @param errorMessage the error message
     * @return the url
     */
    private String getRedirectUrlError( HttpServletRequest request, IRepositoryFile file, String errorMessage )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + AppPathService.getPortalUrl(  ) );
        String strResourceId = file.getResourceId(  );

        if ( !( "".equals( strResourceId ) || ( strResourceId == null ) ) )
        {
            url.addParameter( PARAMETER_FILE_ID + "_" + _strIdPortlet, strResourceId );
        }

        url.addParameter( Parameters.PORTLET_ID, _strIdPortlet );
        url.addParameter( PARAMETER_VIEW_ID + "_" + _strIdPortlet, _strViewId );
        url.addParameter( PARAMETER_PAGE_ID, request.getParameter( PARAMETER_PAGE_ID ) );

        if ( errorMessage != null )
        {
            url.addParameter( PARAMETER_ERROR_UPLOAD, errorMessage );
        }

        return url.getUrl(  );
    }
}
