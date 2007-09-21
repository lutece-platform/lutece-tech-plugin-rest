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
package fr.paris.lutece.portal.web.stylesheet;

import fr.paris.lutece.portal.business.style.Mode;
import fr.paris.lutece.portal.business.style.ModeHome;
import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.business.stylesheet.StyleSheetHome;
import fr.paris.lutece.portal.service.fileupload.FileUploadService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.fileupload.FileItem;

import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * This class provides the user interface to manage StyleSheet features
 */
public class StyleSheetJspBean extends AdminFeaturesPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_STYLESHEET = "CORE_STYLESHEET_MANAGEMENT";

    // Markers
    private static final String MARK_MODE_ID = "mode_id";
    private static final String MARK_MODE_LIST = "mode_list";
    private static final String MARK_STYLESHEET_LIST = "stylesheet_list";
    private static final String MARK_STYLE_LIST = "style_list";
    private static final String MARK_STYLESHEET = "stylesheet";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";

    // Templates files path
    private static final String TEMPLATE_MANAGE_STYLESHEETS = "admin/stylesheet/manage_stylesheets.html";
    private static final String TEMPLATE_CREATE_STYLESHEET = "admin/stylesheet/create_stylesheet.html";
    private static final String TEMPLATE_MODIFY_STYLESHEET = "admin/stylesheet/modify_stylesheet.html";

    // Properties
    private static final String PROPERTY_PATH_XSL = "path.stylesheet";
    private static final String PROPERTY_STYLESHEETS_PER_PAGE = "paginator.stylesheet.itemsPerPage";
    private static final String MESSAGE_STYLESHEET_ALREADY_EXISTS = "portal.style.message.stylesheetAlreadyExists";
    private static final String MESSAGE_STYLESHEET_NOT_VALID = "portal.style.message.stylesheetNotValid";
    private static final String MESSAGE_CONFIRM_DELETE_STYLESHEET = "portal.style.message.stylesheetConfirmDelete";
    private static final String LABEL_ALL = "portal.util.labelAll";
    private static final String JSP_DO_REMOVE_STYLESHEET = "jsp/admin/style/DoRemoveStyleSheet.jsp";
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;

    /**
     * Displays the stylesheets list
     * @return the html code for displaying the stylesheets list
     * @param request The request
     */
    public String getManageStyleSheet( HttpServletRequest request )
    {
        // Parameters processing
        String strModeId = request.getParameter( Parameters.MODE_ID );
        strModeId = ( strModeId != null ) ? strModeId : "-1";

        int nModeId = Integer.parseInt( strModeId );

        ReferenceList listModes = ModeHome.getModes(  );
        String strComboItem = I18nService.getLocalizedString( LABEL_ALL, getLocale(  ) );
        listModes.addItem( -1, strComboItem );

        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_STYLESHEETS_PER_PAGE, 10 );
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        List listStyleSheets = (List<StyleSheet>) StyleSheetHome.getStyleSheetList( nModeId );
        Paginator paginator = new Paginator( listStyleSheets, _nItemsPerPage, getHomeUrl( request ),
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        HashMap model = new HashMap(  );
        model.put( MARK_MODE_ID, strModeId );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_STYLESHEET_LIST, paginator.getPageItems(  ) );
        model.put( MARK_MODE_LIST, listModes );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_STYLESHEETS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the create form of a new stylesheet with the upload field
     * @param request the http request
     * @return the html code for the create form of a new stylesheet
     */
    public String getCreateStyleSheet( HttpServletRequest request )
    {
        String strModeId = request.getParameter( Parameters.MODE_ID );

        HashMap model = new HashMap(  );
        model.put( MARK_STYLE_LIST, StyleSheetHome.getStylesList(  ) );
        model.put( MARK_MODE_LIST, ModeHome.getModes(  ) );
        model.put( MARK_MODE_ID, strModeId );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_STYLESHEET, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes the creation form of a new stylesheet by recovering the parameters
     * in the http request
     * @param request the http request
     * @return The Jsp URL of the process result
     */
    public String doCreateStyleSheet( HttpServletRequest request )
    {
        StyleSheet stylesheet = new StyleSheet(  );
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String strErrorUrl = getData( multipartRequest, stylesheet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        //insert in the table stylesheet of the database
        StyleSheetHome.create( stylesheet );

        //create a local file
        localStyleSheetFile( stylesheet );

        //Displays the list of the stylesheet files
        return getHomeUrl( request );
    }

    private String getData( MultipartHttpServletRequest multipartRequest, StyleSheet stylesheet )
    {
        String strErrorUrl = null;
        String strDescription = multipartRequest.getParameter( Parameters.STYLESHEET_NAME );
        String strStyleId = multipartRequest.getParameter( Parameters.STYLES );
        String strModeId = multipartRequest.getParameter( Parameters.MODE_STYLESHEET );

        FileItem fileSource = multipartRequest.getFile( Parameters.STYLESHEET_SOURCE );
        byte[] baXslSource = fileSource.get(  );
        String strFilename = FileUploadService.getFileNameOnly( fileSource );

        // Mandatory fields
        if ( strDescription.equals( "" ) || ( strFilename == null ) || strFilename.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( multipartRequest, Messages.MANDATORY_FIELDS,
                AdminMessage.TYPE_STOP );
        }

        //test the existence of style or mode already associate with this stylesheet
        int nStyleId = Integer.parseInt( strStyleId );
        int nModeId = Integer.parseInt( strModeId );
        int nCount = StyleSheetHome.getStyleSheetNbPerStyleMode( nStyleId, nModeId );

        // Do not create a stylesheet of there is already one
        if ( ( nCount >= 1 ) && ( stylesheet.getId(  ) == 0 /* creation */ ) )
        {
            return AdminMessageService.getMessageUrl( multipartRequest, MESSAGE_STYLESHEET_ALREADY_EXISTS,
                AdminMessage.TYPE_STOP );
        }

        // Check the XML validity of the XSL stylesheet
        if ( isValid( baXslSource ) != null )
        {
            Object[] args = { isValid( baXslSource ) };

            return AdminMessageService.getMessageUrl( multipartRequest, MESSAGE_STYLESHEET_NOT_VALID, args,
                AdminMessage.TYPE_STOP );
        }

        stylesheet.setDescription( strDescription );
        stylesheet.setStyleId( Integer.parseInt( strStyleId ) );
        stylesheet.setModeId( Integer.parseInt( strModeId ) );
        stylesheet.setSource( baXslSource );
        stylesheet.setFile( strFilename );

        return strErrorUrl;
    }

    /**
     * Returns the form to update a stylesheet whose identifer is stored in the http request
     * @param request The http request
     * @return The html code
     */
    public String getModifyStyleSheet( HttpServletRequest request )
    {
        String strStyleSheetId = request.getParameter( Parameters.STYLESHEET_ID );
        int nId = Integer.parseInt( strStyleSheetId );

        HashMap model = new HashMap(  );
        model.put( MARK_STYLE_LIST, StyleSheetHome.getStylesList(  ) );
        model.put( MARK_MODE_LIST, ModeHome.getModes(  ) );
        model.put( MARK_STYLESHEET, StyleSheetHome.findByPrimaryKey( nId ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_STYLESHEET, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes the updating form of a stylesheet whose new parameters are stored in the
     * http request
     * @param request The http request
     * @return The Jsp URL of the process result
     */
    public String doModifyStyleSheet( HttpServletRequest request )
    {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        int nId = Integer.parseInt( multipartRequest.getParameter( Parameters.STYLESHEET_ID ) );
        StyleSheet stylesheet = StyleSheetHome.findByPrimaryKey( nId );
        String strErrorUrl = getData( multipartRequest, stylesheet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        // Remove the old local file
        removeOldLocalStyleSheet( nId );

        // Update the stylesheet in database
        StyleSheetHome.update( stylesheet );

        // Recreate the local file
        localStyleSheetFile( stylesheet );

        // Displays the management stylesheet page
        return getHomeUrl( request );
    }

    /**
     * Returns the confirm of removing the style whose identifier is in
     * the http request
     *
     * @param request The Http request
     * @return the html code for the remove confirmation page
     */
    public String getRemoveStyleSheet( HttpServletRequest request )
    {
        String strId = request.getParameter( Parameters.STYLESHEET_ID );
        UrlItem url = new UrlItem( JSP_DO_REMOVE_STYLESHEET );
        url.addParameter( Parameters.STYLESHEET_ID, strId );

        StyleSheet stylesheet = StyleSheetHome.findByPrimaryKey( Integer.parseInt( strId ) );
        Object[] args = { stylesheet.getDescription(  ) };

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_STYLESHEET, args, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Processes the deletion of a stylesheet
     * @param request the http request
     * @return The Jsp URL of the process result
     */
    public String doRemoveStyleSheet( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( Parameters.STYLESHEET_ID ) );
        StyleSheet stylesheet = StyleSheetHome.findByPrimaryKey( nId );
        String strFile = stylesheet.getFile(  );
        StyleSheetHome.remove( nId );

        //removal of the XSL file
        int nModeId = stylesheet.getModeId(  );
        Mode mode = ModeHome.findByPrimaryKey( nModeId );
        String strPathStyleSheet = AppPathService.getPath( PROPERTY_PATH_XSL ) + mode.getPath(  );
        File fileToDelete = new File( strPathStyleSheet, strFile );

        if ( ( fileToDelete != null ) && fileToDelete.exists(  ) )
        {
            fileToDelete.delete(  );
        }

        return getHomeUrl( request );
    }

    //////////////////////////////////////////////////////////////////////////////////
    // Private implementation

    /**
     *  Use parsing for validate the modify xsl file
     *  @param strSource the xsl source
     *  @return the message exception when the validation is false
     */
    private String isValid( byte[] baXslSource )
    {
        String strError = null;

        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance(  );
            SAXParser analyzer = factory.newSAXParser(  );
            InputSource is = new InputSource( new ByteArrayInputStream( baXslSource ) );
            analyzer.getXMLReader(  ).parse( is );
        }
        catch ( Exception e )
        {
            strError = e.getMessage(  );
        }

        return strError;
    }

    /**
     * Create and Update the local download file
     * @param nId The identifier of the XSL document whose download file is stored
     * in a blob an will be copied in the directory of the local site.
     */
    private void localStyleSheetFile( StyleSheet stylesheet )
    {
        int nModeId = stylesheet.getModeId(  );
        Mode mode = ModeHome.findByPrimaryKey( nModeId );
        String strPathStyleSheet = AppPathService.getPath( PROPERTY_PATH_XSL ) + mode.getPath(  );
        String strFileName = stylesheet.getFile(  );
        String strFilePath = strPathStyleSheet + strFileName;

        try
        {
            File file = new File( strFilePath );

            if ( file.exists(  ) )
            {
                file.delete(  );
            }

            FileOutputStream fos = new FileOutputStream( file );
            fos.write( stylesheet.getSource(  ) );
            fos.flush(  );
            fos.close(  );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
    }

    /**
     * remove the xsl file from the tmp directory
     * @param nId the identifier of the file
     */
    private void removeOldLocalStyleSheet( int nId )
    {
        //Remove the file which been modify
        StyleSheet stylesheet = StyleSheetHome.findByPrimaryKey( nId );
        int nMode = stylesheet.getModeId(  );
        Mode mode = ModeHome.findByPrimaryKey( nMode );
        String strPathStyleSheet = AppPathService.getPath( PROPERTY_PATH_XSL ) + mode.getPath(  );
        String strOldFileName = stylesheet.getFile(  );
        String strOldFilePath = strPathStyleSheet + strOldFileName;
        File oldFile = new File( strOldFilePath );

        if ( oldFile.exists(  ) )
        {
            oldFile.delete(  );
        }
    }
}
