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
package fr.paris.lutece.plugins.rss.web;

import fr.paris.lutece.plugins.rss.business.RssFeedHome;
import fr.paris.lutece.plugins.rss.business.RssGeneratedFile;
import fr.paris.lutece.plugins.rss.business.RssGeneratedFileHome;
import fr.paris.lutece.plugins.rss.service.RssGeneratorService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.filesystem.UploadUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage pushrss objects
 * features ( publishing, unselecting, ... )
 */
public class RssJspBean extends PluginAdminPageJspBean
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String PARAMETER_PUSH_RSS_PORTLET_ID = "rss_portlet_id";
    private static final String PARAMETER_PUSH_RSS_ID = "rss_id";
    private static final String PARAMETER_PUSH_RSS_NAME = "rss_name";
    private static final String PARAMETER_PUSH_RSS_DESCRIPTION = "rss_description";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_ITEMS_PER_PAGE = "items_per_page";
    private static final String TEMPLATE_MANAGE_RSS_FILE = "admin/plugins/rss/manage_rss_file.html";
    private static final String TEMPLATE_CREATE_PUSH_RSS_FILE = "admin/plugins/rss/create_rss_file.html";
    private static final String TEMPLATE_MODIFY_PUSH_RSS_FILE = "admin/plugins/rss/modify_rss_file.html";
    private static final String MARK_RSS_FILE = "rssFile";
    private static final String MARK_PUSH_RSS_LIST = "rss_files_list";
    private static final String MARK_PUSH_RSS_URL = "rss_file_url";
    private static final String MARK_PORTLET_LIST = "portlet_list";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final int STATE_OK = 0;
    private static final int STATE_PORTLET_MISSING = 1;
    private static final String PROPERTY_PATH_PLUGIN_WAREHOUSE = "path.plugins.warehouse";
    private static final String PROPERTY_FILE_TYPE = "rss.file.type";
    private static final String PROPERTY_NAME_MAX_LENGTH = "rss.name.max.length";
    private static final String PROPERTY_PAGE_TITLE_FILES = "rss.manage_rss_feeds.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE = "rss.create_rss.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY = "rss.modify.pageTitle";
    private static final String PROPERTY_RSS_PER_PAGE = "rss.rssPerPage";
    public static final String RIGHT_RSS_MANAGEMENT = "RSS_MANAGEMENT";

    //Messages
    private static final String MESSAGE_FILENAME_TOO_LONG = "rss.message.filenameTooLong";
    private static final String MESSAGE_FILENAME_ALREADY_EXISTS = "rss.message.filenameAlreadyExists";
    private static final String MESSAGE_NO_DOCUMENT_PORTLET = "rss.message.NoDocumentPortlet";
    private static final String MESSAGE_CONFIRM_DELETE_RSS_FILE = "rss.message.confirmRemoveRssFile";
    private static final String MESSAGE_RSS_LINKED_FEED = "rss.message.linkedToFeed";

    //JSPs
    private static final String JSP_DELETE_RSS_FILE = "jsp/admin/plugins/rss/DoDeleteRssFile.jsp";
    private int _nItemsPerPage;
    private String _strCurrentPageIndex;

    /**
     * Returns  rss files management form
     *
     * @param request The Http request
     * @return Html form
     */
    public String getManageRssFile( HttpServletRequest request ) //Implement paginator
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_FILES );
        _nItemsPerPage = getItemsPerPage( request );
        _strCurrentPageIndex = getPageIndex( request );

        List listRssFileList = RssGeneratedFileHome.getRssFileList(  );
        Paginator paginator = new Paginator( listRssFileList, _nItemsPerPage, getHomeUrl( request ),
                PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        HashMap model = new HashMap(  );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PUSH_RSS_LIST, paginator.getPageItems(  ) );
        model.put( MARK_PUSH_RSS_URL, getRssFileUrl( "", request ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_RSS_FILE, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Modification of a push RSS file
     *
     * @return The Jsp URL of the process result
     * @param request requete Http
     */
    public String doModifyRssFile( HttpServletRequest request )
    {
        // Recovery of parameters processing
        String strRssFileId = request.getParameter( PARAMETER_PUSH_RSS_ID );
        int nRssFileId = Integer.parseInt( strRssFileId );
        String strPortletId = request.getParameter( PARAMETER_PUSH_RSS_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        String strRssFileName = request.getParameter( PARAMETER_PUSH_RSS_NAME );
        String strRssFileDescription = request.getParameter( PARAMETER_PUSH_RSS_DESCRIPTION );

        RssGeneratedFile rssFile = new RssGeneratedFile(  );
        rssFile.setPortletId( nPortletId );
        rssFile.setId( nRssFileId );
        rssFile.setName( strRssFileName );
        rssFile.setState( STATE_OK );

        rssFile.setDescription( strRssFileDescription );

        // Update the database with the new push RSS file
        RssGeneratedFileHome.update( rssFile );

        // Check if the portlet does exist
        if ( !RssGeneratedFileHome.checkRssFilePortlet( nPortletId ) )
        {
            rssFile.setState( STATE_PORTLET_MISSING );
        }

        // Format the new file name
        strRssFileName = UploadUtil.cleanFileName( strRssFileName );

        // Call the create xml document method
        String strRssDocument = RssGeneratorService.createRssDocument( nPortletId, rssFile.getDescription(  ) );

        // Call the create file method
        RssGeneratorService.createFileRss( strRssFileName, strRssDocument );

        // Update the push Rss object in the database
        RssGeneratedFileHome.update( rssFile );

        // Display the page of publishing
        return getHomeUrl( request );
    }

    /**
     * Creates the push RSS file corresponding to the given portlet
     *
     * @return The Jsp URL of the process result
     * @param request requete Http
     */
    public String doCreateRssFile( HttpServletRequest request )
    {
        //Verifies whether a document list portlet exists
        ReferenceList listInvolvedPortlets = RssGeneratedFileHome.findDocumentTypePortlets(  );

        if ( listInvolvedPortlets.isEmpty(  ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_NO_DOCUMENT_PORTLET, AdminMessage.TYPE_STOP );
        }

        String strPortletId = request.getParameter( PARAMETER_PUSH_RSS_PORTLET_ID );
        String strRssFileName = request.getParameter( PARAMETER_PUSH_RSS_NAME );
        String strRssFileDescription = request.getParameter( PARAMETER_PUSH_RSS_DESCRIPTION );
        int nPortletId = Integer.parseInt( strPortletId );

        //Mandatory fields
        if ( request.getParameter( PARAMETER_PUSH_RSS_NAME ).equals( "" ) ||
                request.getParameter( PARAMETER_PUSH_RSS_DESCRIPTION ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Check the file name length
        String strNameMaxLength = AppPropertiesService.getProperty( PROPERTY_NAME_MAX_LENGTH );

        if ( strRssFileName.length(  ) > Integer.parseInt( strNameMaxLength ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_FILENAME_TOO_LONG, AdminMessage.TYPE_STOP );
        }

        // Format the new file name
        strRssFileName = UploadUtil.cleanFileName( strRssFileName );

        // Check the type of the name
        String strFileType = AppPropertiesService.getProperty( PROPERTY_FILE_TYPE );

        if ( !strRssFileName.toLowerCase(  ).endsWith( strFileType ) )
        {
            strRssFileName = strRssFileName + strFileType;
        }

        // Verifies whether the file's name exists
        if ( RssGeneratedFileHome.checkRssFileFileName( strRssFileName ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_FILENAME_ALREADY_EXISTS, AdminMessage.TYPE_STOP );
        }

        // Check if a RSS file exists for this portlet
        String strRssDocument = RssGeneratorService.createRssDocument( nPortletId, strRssFileDescription );

        // Call the create file method
        RssGeneratorService.createFileRss( strRssFileName, strRssDocument );

        // Update the database with the new push RSS file
        RssGeneratedFile rssFile = new RssGeneratedFile(  );
        rssFile.setPortletId( nPortletId );
        rssFile.setName( strRssFileName );
        rssFile.setState( STATE_OK );
        rssFile.setDescription( strRssFileDescription );
        RssGeneratedFileHome.create( rssFile );

        return getHomeUrl( request );
    }

    /**
     * Returns the creation form of a rss file
     *
     * @param request The Http request
     * @return Html form
     */
    public String getCreateRssFile( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE );

        ReferenceList listInvolvedPortlets = RssGeneratedFileHome.findDocumentTypePortlets(  );
        HashMap model = new HashMap(  );
        model.put( MARK_PORTLET_LIST, listInvolvedPortlets );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_PUSH_RSS_FILE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the form to update a rss file
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    public String getModifyRssFile( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY );

        int nRssFileId = Integer.parseInt( request.getParameter( PARAMETER_PUSH_RSS_ID ) );
        RssGeneratedFile rss = RssGeneratedFileHome.findByPrimaryKey( nRssFileId );
        ReferenceList listInvolvedPortlets = RssGeneratedFileHome.findDocumentTypePortlets(  );
        HashMap model = new HashMap(  );
        model.put( MARK_PORTLET_LIST, listInvolvedPortlets );
        model.put( MARK_RSS_FILE, rss );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_PUSH_RSS_FILE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Private implementation

    /**
     * Utility method to build the URL of the given rssFile file
     *
     * @param strNameRssFile The HTML template to fill
     * @param request The HttpServletRequest
     * @return the URL to get the rssFile file
     */
    private String getRssFileUrl( String strNameRssFile, HttpServletRequest request )
    {
        String strBaseUrl = AppPathService.getBaseUrl( request );
        String strStockingDirectoryName = AppPropertiesService.getProperty( RssGeneratorService.PROPERTY_STORAGE_DIRECTORY_NAME );
        String strPluginWarehouse = AppPropertiesService.getProperty( PROPERTY_PATH_PLUGIN_WAREHOUSE );

        //Removes extra slash in the url
        if ( strPluginWarehouse.startsWith( "/" ) )
        {
            strPluginWarehouse = strPluginWarehouse.substring( 1 );
        }

        String strRssFileRepository = strPluginWarehouse + "/" + strStockingDirectoryName;
        strBaseUrl = strBaseUrl + strRssFileRepository + "/" + strNameRssFile;

        return strBaseUrl;
    }

    /**
     * Process removal of a file
     * @param request The Http request
     * @return String The url of the administration console
     */
    public String doDeleteRssFile( HttpServletRequest request )
    {
        int nIdFileRss = Integer.parseInt( request.getParameter( PARAMETER_PUSH_RSS_ID ) );
        RssGeneratedFileHome.remove( nIdFileRss );

        return getHomeUrl( request );
    }

    /**
     * Confirms the removal of a rss file
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doConfirmDeleteRssFile( HttpServletRequest request )
    {
        String strIdFile = request.getParameter( PARAMETER_PUSH_RSS_ID );
        int nIdFile = Integer.parseInt( strIdFile );
        String strDeleteUrl = JSP_DELETE_RSS_FILE + "?" + PARAMETER_PUSH_RSS_ID + "=" + strIdFile;
        String strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_RSS_LINKED_FEED, AdminMessage.TYPE_STOP );

        if ( checkNoRssExternalFeed( nIdFile, request ) )
        {
            RssGeneratedFile rssFile = RssGeneratedFileHome.findByPrimaryKey( nIdFile );
            Object[] messageArgs = { rssFile.getName(  ) };
            strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_RSS_FILE, messageArgs,
                    strDeleteUrl, AdminMessage.TYPE_CONFIRMATION );
        }

        return strUrl;
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
                nItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_RSS_PER_PAGE, 10 );
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
     * Checks whether the generated rss file is linked as an external rss feed
     * @param nIdRssFile the identifier of the rss file
     * @param request The HttpRequest
     * @return a boolean
     */
    public boolean checkNoRssExternalFeed( int nIdRssFile, HttpServletRequest request )
    {
        RssGeneratedFile rssFile = RssGeneratedFileHome.findByPrimaryKey( nIdRssFile );
        String strUrlFile = getRssFileUrl( rssFile.getName(  ), request );

        //if the url of the file is used as an external feed
        return RssFeedHome.checkUrlNotUsed( strUrlFile );
    }
}
