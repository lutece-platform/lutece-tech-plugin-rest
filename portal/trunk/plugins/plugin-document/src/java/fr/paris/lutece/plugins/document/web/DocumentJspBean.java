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
package fr.paris.lutece.plugins.document.web;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentFilter;
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.document.business.DocumentPageTemplate;
import fr.paris.lutece.plugins.document.business.DocumentPageTemplateHome;
import fr.paris.lutece.plugins.document.business.DocumentType;
import fr.paris.lutece.plugins.document.business.DocumentTypeHome;
import fr.paris.lutece.plugins.document.business.attributes.DocumentAttribute;
import fr.paris.lutece.plugins.document.business.category.Category;
import fr.paris.lutece.plugins.document.business.category.CategoryHome;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpace;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpaceHome;
import fr.paris.lutece.plugins.document.business.spaces.SpaceActionHome;
import fr.paris.lutece.plugins.document.business.workflow.DocumentStateHome;
import fr.paris.lutece.plugins.document.service.AttributeManager;
import fr.paris.lutece.plugins.document.service.AttributeService;
import fr.paris.lutece.plugins.document.service.DocumentException;
import fr.paris.lutece.plugins.document.service.DocumentService;
import fr.paris.lutece.plugins.document.service.DocumentTypeResourceIdService;
import fr.paris.lutece.plugins.document.service.category.CategoryService;
import fr.paris.lutece.plugins.document.service.category.CategoryService.CategoryDisplay;
import fr.paris.lutece.plugins.document.service.metadata.MetadataHandler;
import fr.paris.lutece.plugins.document.service.spaces.DocumentSpacesService;
import fr.paris.lutece.plugins.document.service.spaces.SpaceResourceIdService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.fileupload.FileItem;

import java.sql.Timestamp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import javax.xml.transform.Source;


/**
 * JspBean for document management
 */
public class DocumentJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_DOCUMENT_MANAGEMENT = "DOCUMENT_MANAGEMENT";
    public static final String PARAMETER_SPACE_ID_FILTER = "id_space_filter";

    // Templates
    private static final String TEMPLATE_MANAGE_DOCUMENTS = "admin/plugins/document/manage_documents.html";
    private static final String TEMPLATE_CREATE_DOCUMENT = "admin/plugins/document/create_document.html";
    private static final String TEMPLATE_MODIFY_DOCUMENT = "admin/plugins/document/modify_document.html";
    private static final String TEMPLATE_PREVIEW_DOCUMENT = "admin/plugins/document/preview_document.html";
    private static final String TEMPLATE_MOVE_DOCUMENT = "admin/plugins/document/move_document.html";
    private static final String TEMPLATE_DOCUMENT_PAGE_TEMPLATE_ROW = "admin/plugins/document/page_template_list_row.html";
    private static final String TEMPLATE_FORM_CATEGORY = "admin/plugins/document/category/list_category.html";

    // Markers
    private static final String MARK_DOCUMENT = "document";
    private static final String MARK_PREVIEW = "preview";
    private static final String MARK_DOCUMENTS_LIST = "documents_list";
    private static final String MARK_DOCUMENT_TYPES_LIST = "document_types_list";
    private static final String MARK_DOCUMENT_TYPES_FILTER_LIST = "document_types_filter_list";
    private static final String MARK_STATES_FILTER_LIST = "states_filter_list";
    private static final String MARK_SPACES_TREE = "spaces_tree";
    private static final String MARK_SPACE_ACTIONS_LIST = "space_actions_list";
    private static final String MARK_SPACE = "space";
    private static final String MARK_STATE_ID = "id_state";
    private static final String MARK_CHILD_SPACES_LIST = "child_spaces_list";
    private static final String MARK_FIELDS = "fields";
    private static final String MARK_DOCUMENT_TYPE = "document_type";
    private static final String MARK_DEFAULT_DOCUMENT_TYPE = "default_document_type";
    private static final String MARK_DEFAULT_STATE = "default_state";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_VIEW_TYPE = "view_type";
    private static final String MARK_VIEW_TYPES_LIST = "view_types_list";
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_CATEGORY = "category";
    private static final String MARK_CATEGORY_LIST = "category_list";
    private static final String MARK_METADATA = "metadata";
    private static final String MARK_SUBMIT_BUTTON_DISABLED = "submit_button_disabled";
    private static final String MARK_DATE_VALIDITY_BEGIN = "date_validity_begin";
    private static final String MARK_DATE_VALIDITY_END = "date_validity_end";
    private static final String MARK_ACCEPT_SITE_COMMENTS = "accept_site_comments";
    private static final String MARK_IS_MODERATED_COMMENT = "is_moderated_comment";
    private static final String MARK_IS_EMAIL_NOTIFIED_COMMENT = "is_email_notified_comment";
    private static final String MARK_MAILINGLISTS_LIST = "mailinglists_list";
    private static final String MARK_DOCUMENT_PAGE_TEMPLATES_LIST = "page_template_list";
    private final static String MARK_DOCUMENT_PAGE_TEMPLATE = "document_page_template";
    private final static String MARK_INDEX_ROW = "index_row";
    private static final String MARK_DOCUMENT_PAGE_TEMPLATE_CHECKED = "checked";
    private static final String MARK_SPACES_BROWSER = "spaces_browser";

    // Parameters    
    private static final String PARAMETER_DOCUMENT_TYPE_CODE = "document_type_code";
    private static final String PARAMETER_DOCUMENT_TITLE = "document_title";
    private static final String PARAMETER_DOCUMENT_SUMMARY = "document_summary";
    private static final String PARAMETER_DOCUMENT_COMMENT = "document_comment";
    private static final String PARAMETER_VALIDITY_BEGIN = "document_validity_begin";
    private static final String PARAMETER_VALIDITY_END = "document_validity_end";
    private static final String PARAMETER_ACCEPT_SITE_COMMENTS = "accept_site_comments";
    private static final String PARAMETER_IS_MODERATED_COMMENT = "is_moderated_comment";
    private static final String PARAMETER_IS_EMAIL_NOTIFIED_COMMENT = "is_email_notified_comment";
    private static final String PARAMETER_DOCUMENT_ID = "id_document";
    private static final String PARAMETER_STATE_ID = "id_state";
    private static final String PARAMETER_VIEW_TYPE = "view_type";
    private static final String PARAMETER_DOCUMENT_TYPE_CODE_FILTER = "document_type_code_filter";
    private static final String PARAMETER_STATE_ID_FILTER = "id_state_filter";
    private static final String PARAMETER_MAILING_LIST = "mailinglists";
    private static final String PARAMETER_PAGE_TEMPLATE_DOCUMENT_ID = "page_template_id";
    private static final String PARAMETER_CATEGORY = "category_id";

    // Properties
    private static final String PROPERTY_FILTER_ALL = "document.manage_documents.filter.labelAll";
    private static final String PROPERTY_DOCUMENTS_PER_PAGE = "document.documentsPerPage";
    private static final String PROPERTY_DEFAULT_VIEW_TYPE = "document.manageDocuments.defaultViewType";
    private static final String PROPERTY_PREVIEW_DOCUMENT_PAGE_TITLE = "document.preview_document.pageTitle";
    private static final String PROPERTY_MOVE_DOCUMENT_PAGE_TITLE = "document.move_document.pageTitle";

    // Jsp
    private static final String JSP_DELETE_DOCUMENT = "DoDeleteDocument.jsp";
    private static final String JSP_PREVIEW_DOCUMENT = "PreviewDocument.jsp";

    // Messages
    private static final String MESSAGE_CONFIRM_DELETE = "document.message.confirmDeleteDocument";
    private static final String MESSAGE_INVALID_DOCUMENT_ID = "document.message.invalidDocumentId";
    private static final String MESSAGE_DOCUMENT_NOT_FOUND = "document.message.documentNotFound";
    private static final String MESSAGE_DOCUMENT_NOT_AUTHORIZED = "document.message.documentNotAuthorized";
    private static final String MESSAGE_MOVING_NOT_AUTHORIZED = "document.message.movingNotAuthorized";
    private static final String MESSAGE_DOCUMENT_IS_PUBLISHED = "document.message.documentIsPublished";
    private static final String MESSAGE_ERROR_DATEEND_BEFORE_DATEBEGIN = "document.message.dateEndBeforeDateBegin";
    private static final String MESSAGE_INVALID_DATEEND = "document.message.invalidDateEnd";
    private static final String MESSAGE_INVALID_DATEBEGIN = "document.message.invalidDateBegin";
    private static final String MESSAGE_DOCUMENT_ERROR = "document.message.documentError"; //TODO message erreur	
    private static final String PATH_JSP = "jsp/admin/plugins/document/";
    private static final String XSL_PARAMETER_CURRENT_SPACE = "current-space-id";
    private static final String FILTER_ALL = "-1";
    private static final String PAGE_INDEX_FIRST = "1";
    private static final String PATTERN_DATE = "dd/MM/yyyy";
    private static final String EXPREG_DATE = "([0-9]{1,2})/([0-9]{1,2})/([0-9]{4})";
    private String _strCurrentDocumentTypeFilter;
    private String _strCurrentStateFilter;
    private String _strCurrentSpaceId;
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;
    private String _strViewType;
    private int _nDefaultItemsPerPage;

    public DocumentJspBean(  )
    {
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DOCUMENTS_PER_PAGE, 10 );
    }

    /**
     * Gets the document management page
     * @param request The HTTP request
     * @return The document management page
     */
    public String getManageDocuments( HttpServletRequest request )
    {
        setPageTitleProperty( null );

        // Gets new criterias or space changes from the request
        DocumentFilter filter = new DocumentFilter(  );
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _strCurrentDocumentTypeFilter = getDocumentType( request, filter );
        _strCurrentStateFilter = getState( request, filter );
        _strCurrentSpaceId = getSpaceId( request, filter );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );
        _strViewType = getViewType( request );

        // Build document list according criterias
        List listDocuments = DocumentHome.findByFilter( filter, getLocale(  ) );
        setDocumentsActions( listDocuments );

        // Spaces
        String strXmlSpaces = DocumentSpacesService.getInstance(  ).getXmlSpacesList( getUser(  ) );
        Source sourceXsl = DocumentSpacesService.getInstance(  ).getTreeXsl(  );
        HashMap htXslParameters = new HashMap(  );
        htXslParameters.put( XSL_PARAMETER_CURRENT_SPACE, _strCurrentSpaceId );

        String strSpacesTree = XmlTransformerService.transformBySource( strXmlSpaces, sourceXsl, htXslParameters, null );

        List listSpaceActions = SpaceActionHome.getActionsList( getLocale(  ) );
        int nCurrentSpaceId = Integer.parseInt( _strCurrentSpaceId );
        DocumentSpace currentSpace = DocumentSpaceHome.findByPrimaryKey( nCurrentSpaceId );
        listSpaceActions = (List) RBACService.getAuthorizedActionsCollection( listSpaceActions, currentSpace,
                getUser(  ) );

        // Build filter combos
        // Document Types
        ReferenceList listDocumentTypes = DocumentSpaceHome.getAllowedDocumentTypes( Integer.parseInt( 
                    _strCurrentSpaceId ) );
        listDocumentTypes = RBACService.getAuthorizedReferenceList( listDocumentTypes, DocumentType.RESOURCE_TYPE,
                DocumentTypeResourceIdService.PERMISSION_VIEW, getUser(  ) );
        listDocumentTypes.addItem( FILTER_ALL, I18nService.getLocalizedString( PROPERTY_FILTER_ALL, getLocale(  ) ) );

        // Documents States
        ReferenceList listStates = DocumentStateHome.getDocumentStatesList( getLocale(  ) );
        listStates.addItem( FILTER_ALL, I18nService.getLocalizedString( PROPERTY_FILTER_ALL, getLocale(  ) ) );

        // Childs spaces
        Collection listChildSpaces = DocumentSpaceHome.findChilds( nCurrentSpaceId );

        // Creation document types list for the current space
        ReferenceList listCreateDocumentTypes = DocumentSpaceHome.getAllowedDocumentTypes( Integer.parseInt( 
                    _strCurrentSpaceId ) );
        listCreateDocumentTypes = RBACService.getAuthorizedReferenceList( listCreateDocumentTypes,
                DocumentType.RESOURCE_TYPE, DocumentTypeResourceIdService.PERMISSION_CREATE, getUser(  ) );

        Paginator paginator = new Paginator( listDocuments, _nItemsPerPage, getHomeUrl( request ),
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        HashMap model = new HashMap(  );
        model.put( MARK_SPACES_TREE, strSpacesTree );
        model.put( MARK_SPACE_ACTIONS_LIST, listSpaceActions );
        model.put( MARK_SPACE, currentSpace );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_VIEW_TYPES_LIST, DocumentSpaceHome.getViewTypeList( getLocale(  ) ) );
        model.put( MARK_VIEW_TYPE, _strViewType );
        model.put( MARK_DOCUMENTS_LIST, paginator.getPageItems(  ) );
        model.put( MARK_DOCUMENT_TYPES_FILTER_LIST, listDocumentTypes );
        model.put( MARK_DEFAULT_DOCUMENT_TYPE, _strCurrentDocumentTypeFilter );
        model.put( MARK_STATES_FILTER_LIST, listStates );
        model.put( MARK_DEFAULT_STATE, _strCurrentStateFilter );
        model.put( MARK_CHILD_SPACES_LIST, listChildSpaces );
        model.put( MARK_DOCUMENT_TYPES_LIST, listCreateDocumentTypes );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_DOCUMENTS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Gets the document creation page
     * @param request The HTTP request
     * @return The document creation page
     */
    public String getCreateDocument( HttpServletRequest request )
    {
        String strDocumentTypeCode = request.getParameter( PARAMETER_DOCUMENT_TYPE_CODE );
        DocumentType documentType = DocumentTypeHome.findByPrimaryKey( strDocumentTypeCode );
        HashMap model = new HashMap(  );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ).getLanguage(  ) );
        model.put( MARK_DOCUMENT_TYPE, documentType.getCode(  ) );

        model.put( MARK_CATEGORY, getCategoryCreateForm( request ) );
        model.put( MARK_METADATA, getMetadataCreateForm( request, strDocumentTypeCode ) );
        model.put( MARK_FIELDS, DocumentService.getInstance(  ).getCreateForm( strDocumentTypeCode, getLocale(  ) ) );

        // PageTemplate
        int nIndexRow = 1;
        StringBuffer strPageTemplatesRow = new StringBuffer(  );

        // Scan of the list
        for ( DocumentPageTemplate documentPageTemplate : DocumentPageTemplateHome.getPageTemplatesList(  ) )
        {
            strPageTemplatesRow.append( getTemplatesPageList( documentPageTemplate.getId(  ), 0,
                    Integer.toString( nIndexRow ) ) );
            nIndexRow++;
        }

        model.put( MARK_DOCUMENT_PAGE_TEMPLATES_LIST, strPageTemplatesRow );

        AdminUser user = AdminUserService.getAdminUser( request );
        ReferenceList listMailingLists = AdminMailingListService.getMailingLists( user );
        model.put( MARK_MAILINGLISTS_LIST, listMailingLists );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_DOCUMENT, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Return the part of creation form corresponding to Category
     * @param request The http request
     * @return The html form
     */
    private String getCategoryCreateForm( HttpServletRequest request )
    {
        HashMap<String, Collection<CategoryDisplay>> model = new HashMap<String, Collection<CategoryDisplay>>(  );
        model.put( MARK_CATEGORY_LIST, CategoryService.getAllCategoriesDisplay(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_FORM_CATEGORY, getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Return the part of modification form corresponding to Category
     * @param request The http request
     * @param nIdDocument The id of Document object to modify
     * @return The html form
     */
    private String getCategoryModifyForm( HttpServletRequest request, Document document )
    {
        HashMap<String, Collection<CategoryDisplay>> model = new HashMap<String, Collection<CategoryDisplay>>(  );
        Collection<CategoryDisplay> listCategoryDisplay = CategoryService.getAllCategoriesDisplay( document.getCategories(  ) );

        model.put( MARK_CATEGORY_LIST, listCategoryDisplay );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_FORM_CATEGORY, getLocale(  ), model );

        return template.getHtml(  );
    }

    private String getMetadataCreateForm( HttpServletRequest request, String strDocumentTypeCode )
    {
        MetadataHandler hMetadata = getMetadataHandler( strDocumentTypeCode );

        return ( hMetadata != null ) ? hMetadata.getCreateForm( request ) : "";
    }

    private String getMetadataModifyForm( HttpServletRequest request, Document document )
    {
        MetadataHandler hMetadata = getMetadataHandler( document.getCodeDocumentType(  ) );

        return ( hMetadata != null ) ? hMetadata.getModifyForm( request, document.getXmlMetadata(  ) ) : "";
    }

    private MetadataHandler getMetadataHandler( String strDocumentTypeCode )
    {
        DocumentType documentType = DocumentTypeHome.findByPrimaryKey( strDocumentTypeCode );

        return documentType.metadataHandler(  );
    }

    /**
     * Perform the creation
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doCreateDocument( HttpServletRequest request )
    {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        String strDocumentTypeCode = multipartRequest.getParameter( PARAMETER_DOCUMENT_TYPE_CODE );
        Document document = new Document(  );
        document.setCodeDocumentType( strDocumentTypeCode );

        String strError = getDocumentData( multipartRequest, document );

        if ( strError != null )
        {
            return strError;
        }

        document.setSpaceId( Integer.parseInt( _strCurrentSpaceId ) );
        document.setStateId( 1 );
        document.setCreatorId( getUser(  ).getUserId(  ) );

        try
        {
            DocumentService.getInstance(  ).createDocument( document, getUser(  ) );
        }
        catch ( DocumentException e )
        {
            return getErrorMessageUrl( request, e.getI18nMessage(  ) );
        }

        return getHomeUrl( request );
    }

    /**
     * Gets the document modification page
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String getModifyDocument( HttpServletRequest request )
    {
        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        Document document = DocumentHome.findByPrimaryKey( Integer.parseInt( strDocumentId ) );
        String strStateId = ( request.getParameter( PARAMETER_STATE_ID ) != null )
            ? request.getParameter( PARAMETER_STATE_ID ) : "";
        HashMap model = new HashMap(  );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ).getLanguage(  ) );
        model.put( MARK_DOCUMENT, document );

        // Date Management
        model.put( MARK_DATE_VALIDITY_BEGIN, DateUtil.getDateString( document.getDateValidityBegin(  ) ) );
        model.put( MARK_DATE_VALIDITY_END, DateUtil.getDateString( document.getDateValidityEnd(  ) ) );

        // Site Comments management
        model.put( MARK_ACCEPT_SITE_COMMENTS, document.getAcceptSiteComments(  ) );
        model.put( MARK_IS_MODERATED_COMMENT, document.getIsModeratedComment(  ) );

        // Notification
        model.put( MARK_IS_EMAIL_NOTIFIED_COMMENT, document.getIsEmailNotifiedComment(  ) );

        // PageTemplate
        int nIndexRow = 1;
        StringBuffer strPageTemplatesRow = new StringBuffer(  );

        // Scan of the list
        for ( DocumentPageTemplate documentPageTemplate : DocumentPageTemplateHome.getPageTemplatesList(  ) )
        {
            strPageTemplatesRow.append( getTemplatesPageList( documentPageTemplate.getId(  ),
                    document.getPageTemplateDocumentId(  ), Integer.toString( nIndexRow ) ) );
            nIndexRow++;
        }

        model.put( MARK_DOCUMENT_PAGE_TEMPLATES_LIST, strPageTemplatesRow );

        AdminUser user = AdminUserService.getAdminUser( request );
        ReferenceList listMailingLists = AdminMailingListService.getMailingLists( user );
        model.put( MARK_MAILINGLISTS_LIST, listMailingLists );

        model.put( MARK_CATEGORY, getCategoryModifyForm( request, document ) );
        model.put( MARK_METADATA, getMetadataModifyForm( request, document ) );
        model.put( MARK_FIELDS, DocumentService.getInstance(  ).getModifyForm( strDocumentId, getLocale(  ) ) );
        model.put( MARK_STATE_ID, strStateId );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_DOCUMENT, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Perform the modification
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doModifyDocument( HttpServletRequest request )
    {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        String strDocumentId = multipartRequest.getParameter( PARAMETER_DOCUMENT_ID );
        Document document = DocumentHome.findByPrimaryKey( Integer.parseInt( strDocumentId ) );
        String strError = getDocumentData( multipartRequest, document );

        if ( strError != null )
        {
            return strError;
        }

        try
        {
            DocumentService.getInstance(  ).modifyDocument( document, getUser(  ) );
        }
        catch ( DocumentException e )
        {
            return getErrorMessageUrl( request, e.getI18nMessage(  ) );
        }

        // If a state is defined, it should be set after to the document after the modification
        String strStateId = multipartRequest.getParameter( PARAMETER_STATE_ID );

        if ( ( strStateId != null ) && ( !strStateId.equals( "" ) ) )
        {
            int nStateId = Integer.parseInt( strStateId );

            try
            {
                DocumentService.getInstance(  ).changeDocumentState( document, getUser(  ), nStateId );
            }
            catch ( DocumentException e )
            {
                return getErrorMessageUrl( request, e.getI18nMessage(  ) );
            }
        }

        return getHomeUrl( request );
    }

    private String getDocumentData( MultipartHttpServletRequest mRequest, Document document )
    {
        String strDocumentTitle = mRequest.getParameter( PARAMETER_DOCUMENT_TITLE );
        String strDocumentSummary = mRequest.getParameter( PARAMETER_DOCUMENT_SUMMARY );
        String strDocumentComment = mRequest.getParameter( PARAMETER_DOCUMENT_COMMENT );
        String strDateValidityBegin = mRequest.getParameter( PARAMETER_VALIDITY_BEGIN );
        String strDateValidityEnd = mRequest.getParameter( PARAMETER_VALIDITY_END );
        String strAcceptSiteComments = ( mRequest.getParameter( PARAMETER_ACCEPT_SITE_COMMENTS ) != null )
            ? mRequest.getParameter( PARAMETER_ACCEPT_SITE_COMMENTS ) : "0";
        String strIsModeratedComment = ( mRequest.getParameter( PARAMETER_IS_MODERATED_COMMENT ) != null )
            ? mRequest.getParameter( PARAMETER_IS_MODERATED_COMMENT ) : "0";
        String strIsEmailNotifiedComment = ( mRequest.getParameter( PARAMETER_IS_EMAIL_NOTIFIED_COMMENT ) != null )
            ? mRequest.getParameter( PARAMETER_IS_EMAIL_NOTIFIED_COMMENT ) : "0";
        String strMailingListId = ( mRequest.getParameter( PARAMETER_MAILING_LIST ) != null )
            ? mRequest.getParameter( PARAMETER_MAILING_LIST ) : "0";
        int nMailingListId = Integer.parseInt( strMailingListId );
        String strPageTemplateDocumentId = ( mRequest.getParameter( PARAMETER_PAGE_TEMPLATE_DOCUMENT_ID ) != null )
            ? mRequest.getParameter( PARAMETER_PAGE_TEMPLATE_DOCUMENT_ID ) : "0";
        int nPageTemplateDocumentId = Integer.parseInt( strPageTemplateDocumentId );
        String[] arrayCategory = mRequest.getParameterValues( PARAMETER_CATEGORY );

        // Check for mandatory value
        if ( strDocumentTitle.trim(  ).equals( "" ) || strDocumentSummary.trim(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( mRequest, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        DocumentType documentType = DocumentTypeHome.findByPrimaryKey( document.getCodeDocumentType(  ) );
        List<DocumentAttribute> listAttributes = documentType.getAttributes(  );

        for ( DocumentAttribute attribute : listAttributes )
        {
            String strParameterStringValue = mRequest.getParameter( attribute.getCode(  ) );
            FileItem fileParameterBinaryValue = mRequest.getFile( attribute.getCode(  ) );

            if ( strParameterStringValue != null ) // If the field is a string
            {
                // Check for mandatory value
                if ( attribute.isRequired(  ) && strParameterStringValue.trim(  ).equals( "" ) )
                {
                    return AdminMessageService.getMessageUrl( mRequest, Messages.MANDATORY_FIELDS,
                        AdminMessage.TYPE_STOP );
                }

                // Check for specific attribute validation
                AttributeManager manager = AttributeService.getInstance(  )
                                                           .getManager( attribute.getCodeAttributeType(  ) );
                String strValidationErrorMessageKey = manager.validateValue( strParameterStringValue );

                if ( strValidationErrorMessageKey != null )
                {
                    return AdminMessageService.getMessageUrl( mRequest, strValidationErrorMessageKey,
                        AdminMessage.TYPE_STOP );
                }

                attribute.setTextValue( strParameterStringValue );
            }
            else if ( fileParameterBinaryValue != null ) // If the field is a file
            {
                attribute.setBinary( true );

                String strContentType = fileParameterBinaryValue.getContentType(  );
                byte[] bytes = fileParameterBinaryValue.get(  );

                if ( fileParameterBinaryValue.getSize(  ) == 0 )
                {
                    // there is no new value then take the old file value
                    DocumentAttribute oldAttribute = document.getAttribute( attribute.getCode(  ) );

                    if ( ( oldAttribute != null ) && ( oldAttribute.getBinaryValue(  ).length > 0 ) )
                    {
                        bytes = oldAttribute.getBinaryValue(  );
                        strContentType = oldAttribute.getValueContentType(  );
                    }
                }

                // Check for mandatory value
                if ( attribute.isRequired(  ) && ( bytes.length == 0 ) )
                {
                    return AdminMessageService.getMessageUrl( mRequest, Messages.MANDATORY_FIELDS,
                        AdminMessage.TYPE_STOP );
                }

                // Check for specific attribute validation
                AttributeManager manager = AttributeService.getInstance(  )
                                                           .getManager( attribute.getCodeAttributeType(  ) );
                String strValidationErrorMessageKey = manager.validateValue( bytes );

                if ( strValidationErrorMessageKey != null )
                {
                    return AdminMessageService.getMessageUrl( mRequest, strValidationErrorMessageKey,
                        AdminMessage.TYPE_STOP );
                }

                attribute.setBinaryValue( bytes );
                attribute.setValueContentType( strContentType );
            }
        }

        Timestamp datValidityBegin = null;
        Timestamp datValidityEnd = null;

        if ( ( strDateValidityBegin != null ) && !strDateValidityBegin.equals( "" ) && ( strDateValidityEnd != null ) &&
                !strDateValidityEnd.equals( "" ) )
        {
            // validate date begin format
            if ( strDateValidityBegin.matches( EXPREG_DATE ) )
            {
                datValidityBegin = getDate( strDateValidityBegin );
            }
            else
            {
                return AdminMessageService.getMessageUrl( mRequest, MESSAGE_INVALID_DATEBEGIN, AdminMessage.TYPE_STOP );
            }

            //validate date end format
            if ( strDateValidityEnd.matches( EXPREG_DATE ) )
            {
                datValidityEnd = getDate( strDateValidityEnd );
            }
            else
            {
                return AdminMessageService.getMessageUrl( mRequest, MESSAGE_INVALID_DATEEND, AdminMessage.TYPE_STOP );
            }

            //validate period (dateEnd > dateBegin )
            if ( datValidityEnd.before( datValidityBegin ) )
            {
                return AdminMessageService.getMessageUrl( mRequest, MESSAGE_ERROR_DATEEND_BEFORE_DATEBEGIN,
                    AdminMessage.TYPE_STOP );
            }
        }

        document.setTitle( strDocumentTitle );
        document.setSummary( strDocumentSummary );
        document.setComment( strDocumentComment );
        document.setDateValidityBegin( datValidityBegin );
        document.setDateValidityEnd( datValidityEnd );
        document.setAcceptSiteComments( Integer.parseInt( strAcceptSiteComments ) );
        document.setIsModeratedComment( Integer.parseInt( strIsModeratedComment ) );
        document.setIsEmailNotifiedComment( Integer.parseInt( strIsEmailNotifiedComment ) );
        document.setMailingListId( nMailingListId );
        document.setPageTemplateDocumentId( nPageTemplateDocumentId );

        MetadataHandler hMetadata = documentType.metadataHandler(  );

        if ( hMetadata != null )
        {
            document.setXmlMetadata( hMetadata.getXmlMetadata( mRequest.getParameterMap(  ) ) );
        }

        document.setAttributes( listAttributes );

        //Categories
        List<Category> listCategories = new ArrayList<Category>(  );

        if ( arrayCategory != null )
        {
            for ( String strIdCategory : arrayCategory )
            {
                listCategories.add( CategoryHome.find( Integer.parseInt( strIdCategory ) ) );
            }
        }

        document.setCategories( listCategories );

        return null; // No error
    }

    private Timestamp getDate( String strDate )
    {
        try
        {
            DateFormat dateFormat = new SimpleDateFormat( PATTERN_DATE, getLocale(  ).getDefault(  ) );
            dateFormat.setLenient( false );

            return new Timestamp( dateFormat.parse( strDate ).getTime(  ) );
        }
        catch ( ParseException e )
        {
            return null;
        }
    }

    /**
     * Confirm the deletion
     * @param request The HTTP request
     * @return The URL to go after performing the confirmation
     */
    public String deleteDocument( HttpServletRequest request )
    {
        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        int nDocumentId = Integer.parseInt( strDocumentId );

        Document document = DocumentHome.findByPrimaryKey( nDocumentId );
        Object[] messageArgs = { document.getTitle(  ) };
        UrlItem url = new UrlItem( PATH_JSP + JSP_DELETE_DOCUMENT );
        url.addParameter( PARAMETER_DOCUMENT_ID, nDocumentId );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE, messageArgs, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Perform the deletion
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doDeleteDocument( HttpServletRequest request )
    {
        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        int nDocumentId = Integer.parseInt( strDocumentId );

        // Test if the document is published or assigned
        boolean bPublishedDocument = DocumentHome.documentIsPublished( nDocumentId );

        if ( bPublishedDocument )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_DOCUMENT_IS_PUBLISHED, AdminMessage.TYPE_STOP );
        }

        DocumentHome.remove( nDocumentId );

        return getHomeUrl( request );
    }

    /**
     * Perform the changing of state
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doChangeState( HttpServletRequest request )
    {
        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        int nDocumentId = Integer.parseInt( strDocumentId );
        String strStateId = request.getParameter( PARAMETER_STATE_ID );
        int nStateId = Integer.parseInt( strStateId );
        Document document = DocumentHome.findByPrimaryKey( nDocumentId );

        try
        {
            DocumentService.getInstance(  ).changeDocumentState( document, getUser(  ), nStateId );
        }
        catch ( DocumentException e )
        {
            return getErrorMessageUrl( request, e.getI18nMessage(  ) );
        }

        return getHomeUrl( request );
    }

    /**
     * Perform the document validation
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doValidateDocument( HttpServletRequest request )
    {
        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        int nDocumentId = Integer.parseInt( strDocumentId );
        String strStateId = request.getParameter( PARAMETER_STATE_ID );
        int nStateId = Integer.parseInt( strStateId );
        Document document = DocumentHome.findByPrimaryKey( nDocumentId );

        try
        {
            DocumentService.getInstance(  ).validateDocument( document, getUser(  ), nStateId );
        }
        catch ( DocumentException e )
        {
            return getErrorMessageUrl( request, e.getI18nMessage(  ) );
        }

        return getHomeUrl( request );
    }

    /**
     * Perform the document filing
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doArchiveDocument( HttpServletRequest request )
    {
        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        int nDocumentId = Integer.parseInt( strDocumentId );
        String strStateId = request.getParameter( PARAMETER_STATE_ID );
        int nStateId = Integer.parseInt( strStateId );
        Document document = DocumentHome.findByPrimaryKey( nDocumentId );

        // Test if the document is published or assigned
        boolean bPublishedDocument = DocumentHome.documentIsPublished( nDocumentId );

        if ( bPublishedDocument )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_DOCUMENT_IS_PUBLISHED, AdminMessage.TYPE_STOP );
        }

        try
        {
            DocumentService.getInstance(  ).archiveDocument( document, getUser(  ), nStateId );
        }
        catch ( DocumentException e )
        {
            return getErrorMessageUrl( request, e.getI18nMessage(  ) );
        }

        return getHomeUrl( request );
    }

    /**
     * Perform a document search by Id
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doSearchDocumentById( HttpServletRequest request )
    {
        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        int nDocumentId;

        try
        {
            nDocumentId = Integer.parseInt( strDocumentId );
        }
        catch ( NumberFormatException e )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_DOCUMENT_ID, AdminMessage.TYPE_STOP );
        }

        Document document = DocumentHome.findByPrimaryKey( nDocumentId );

        if ( document == null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_DOCUMENT_NOT_FOUND, AdminMessage.TYPE_STOP );
        }

        DocumentType type = DocumentTypeHome.findByPrimaryKey( document.getCodeDocumentType(  ) );

        if ( !RBACService.isAuthorized( type, DocumentTypeResourceIdService.PERMISSION_VIEW, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_DOCUMENT_NOT_AUTHORIZED, AdminMessage.TYPE_STOP );
        }

        if ( !DocumentSpacesService.getInstance(  ).isAuthorizedView( document.getSpaceId(  ), getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_DOCUMENT_NOT_AUTHORIZED, AdminMessage.TYPE_STOP );
        }

        UrlItem url = new UrlItem( JSP_PREVIEW_DOCUMENT );
        url.addParameter( PARAMETER_DOCUMENT_ID, nDocumentId );

        return url.getUrl(  );
    }

    /**
     * Get the preview page
     * @param request The HTTP request
     * @return The preview page
     */
    public String getPreviewDocument( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PREVIEW_DOCUMENT_PAGE_TITLE );

        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        int nDocumentId = Integer.parseInt( strDocumentId );

        Document document = DocumentHome.findByPrimaryKey( nDocumentId );
        document.setLocale( getLocale(  ) );
        DocumentService.getInstance(  ).getActions( document, getLocale(  ), getUser(  ) );

        DocumentType type = DocumentTypeHome.findByPrimaryKey( document.getCodeDocumentType(  ) );

        String strPreview = XmlTransformerService.transformBySource( document.getXmlWorkingContent(  ),
                type.getAdminXslSource(  ), null, null );

        HashMap model = new HashMap(  );
        model.put( MARK_DOCUMENT, document );
        model.put( MARK_PREVIEW, strPreview );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PREVIEW_DOCUMENT, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Perform the changing of space
     * @param request The HTTP request
     * @return The document move page
     */
    public String getMoveDocument( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MOVE_DOCUMENT_PAGE_TITLE );

        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        Document document = DocumentHome.findByPrimaryKey( Integer.parseInt( strDocumentId ) );
        HashMap model = new HashMap(  );
        String strSpaceId = request.getParameter( DocumentSpacesService.PARAMETER_BROWSER_SELECTED_SPACE_ID );
        boolean bSubmitButtonDisabled = Boolean.TRUE;

        if ( ( strSpaceId != null ) && !strSpaceId.equals( "" ) )
        {
            bSubmitButtonDisabled = Boolean.FALSE;
        }

        // Spaces browser
        model.put( MARK_SPACES_BROWSER,
            DocumentSpacesService.getInstance(  ).getSpacesBrowser( request, getUser(  ), getLocale(  ) ) );
        model.put( MARK_DOCUMENT, document );
        model.put( MARK_SUBMIT_BUTTON_DISABLED, bSubmitButtonDisabled );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MOVE_DOCUMENT, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Perform the document moving
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doMoveDocument( HttpServletRequest request )
    {
        boolean bTypeAllowed = Boolean.FALSE;
        boolean bCreationAuthorized = Boolean.FALSE;
        String strSpaceId = request.getParameter( DocumentSpacesService.PARAMETER_BROWSER_SELECTED_SPACE_ID );

        if ( strSpaceId == null )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nSpaceId = Integer.parseInt( strSpaceId );
        DocumentSpace space = DocumentSpaceHome.findByPrimaryKey( nSpaceId );
        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        Document document = DocumentHome.findByPrimaryKey( Integer.parseInt( strDocumentId ) );

        // Check if the specified document type is authorized for this space
        for ( String documentType : space.getAllowedDocumentTypes(  ) )
        {
            if ( document.getCodeDocumentType(  ).equals( documentType ) )
            {
                bTypeAllowed = Boolean.TRUE;
            }
        }

        // Check if user have rights to create a document into this space
        if ( RBACService.isAuthorized( space, SpaceResourceIdService.PERMISSION_CREATE, getUser(  ) ) )
        {
            bCreationAuthorized = Boolean.TRUE;
        }

        if ( bTypeAllowed && bCreationAuthorized )
        {
            document.setSpaceId( nSpaceId );
            DocumentHome.update( document, true );

            return getHomeUrl( request );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_MOVING_NOT_AUTHORIZED, AdminMessage.TYPE_STOP );
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Private implementation to manage UI parameters
    private String getViewType( HttpServletRequest request )
    {
        String strViewType = request.getParameter( PARAMETER_VIEW_TYPE );

        if ( strViewType == null )
        {
            if ( _strViewType != null )
            {
                strViewType = _strViewType;
            }
            else
            {
                strViewType = AppPropertiesService.getProperty( PROPERTY_DEFAULT_VIEW_TYPE );
            }
        }
        else
        {
            updateSpaceView( strViewType );
        }

        return strViewType;
    }

    private void setView( int nSpaceId )
    {
        DocumentSpace space = DocumentSpaceHome.findByPrimaryKey( nSpaceId );

        if ( space != null )
        {
            _strViewType = space.getViewType(  );
        }
    }

    private String getDocumentType( HttpServletRequest request, DocumentFilter filter )
    {
        // Filter for document type
        String strCodeDocumentTypeFilter = request.getParameter( PARAMETER_DOCUMENT_TYPE_CODE_FILTER );

        if ( strCodeDocumentTypeFilter == null )
        {
            if ( _strCurrentDocumentTypeFilter != null )
            {
                strCodeDocumentTypeFilter = _strCurrentDocumentTypeFilter;
            }
            else
            {
                strCodeDocumentTypeFilter = FILTER_ALL;
            }
        }

        if ( !strCodeDocumentTypeFilter.equals( FILTER_ALL ) )
        {
            filter.setCodeDocumentType( strCodeDocumentTypeFilter );
        }

        if ( !strCodeDocumentTypeFilter.equals( _strCurrentDocumentTypeFilter ) )
        {
            resetPageIndex(  );
        }

        return strCodeDocumentTypeFilter;
    }

    private String getState( HttpServletRequest request, DocumentFilter filter )
    {
        String strStateId = request.getParameter( PARAMETER_STATE_ID_FILTER );

        if ( strStateId == null )
        {
            if ( _strCurrentStateFilter != null )
            {
                strStateId = _strCurrentStateFilter;
            }
            else
            {
                strStateId = FILTER_ALL;
            }
        }

        if ( !strStateId.equals( FILTER_ALL ) )
        {
            int nStateId = Integer.parseInt( strStateId );
            filter.setIdState( nStateId );
        }

        if ( !strStateId.equals( _strCurrentStateFilter ) )
        {
            resetPageIndex(  );
        }

        return strStateId;
    }

    private String getSpaceId( HttpServletRequest request, DocumentFilter filter )
    {
        String strSpaceId = request.getParameter( PARAMETER_SPACE_ID_FILTER );

        if ( strSpaceId == null )
        {
            if ( ( _strCurrentSpaceId != null ) && !_strCurrentSpaceId.equals( "-1" ) )
            {
                strSpaceId = _strCurrentSpaceId;
            }
            else
            {
                int nSpaceId = DocumentSpacesService.getInstance(  ).getUserDefaultSpace( getUser(  ) );
                strSpaceId = "" + nSpaceId;
            }
        }

        int nSpaceId = Integer.parseInt( strSpaceId );
        filter.setIdSpace( nSpaceId );

        if ( !strSpaceId.equals( _strCurrentSpaceId ) )
        {
            // Reset the page index
            resetPageIndex(  );

            // Sets the view corresponding to the new space
            setView( nSpaceId );
        }

        return strSpaceId;
    }

    private void resetPageIndex(  )
    {
        _strCurrentPageIndex = PAGE_INDEX_FIRST;
    }

    private void setDocumentsActions( Collection<Document> listDocuments )
    {
        for ( Document document : listDocuments )
        {
            DocumentService.getInstance(  ).getActions( document, getLocale(  ), getUser(  ) );
        }
    }

    private void updateSpaceView( String strViewType )
    {
        int nSpaceId = Integer.parseInt( _strCurrentSpaceId );
        DocumentSpace space = DocumentSpaceHome.findByPrimaryKey( nSpaceId );
        space.setViewType( strViewType );
        DocumentSpaceHome.update( space );
    }

    /**
     * return admin message url for generic error with specific action message
     * @param request The HTTPrequest
     * @param strI18nMessage The i18n message
     * @return The admin message url
     */
    private String getErrorMessageUrl( HttpServletRequest request, String strI18nMessage )
    {
        return AdminMessageService.getMessageUrl( request, MESSAGE_DOCUMENT_ERROR,
            new String[] { I18nService.getLocalizedString( strI18nMessage, getLocale(  ) ) }, AdminMessage.TYPE_ERROR );
    }

    /**
     * Gets an html template displaying the patterns list available in the portal for the layout
     *
     * @param nTemplatePageId The identifier of the layout to select in the list
     * @param nPageTemplatePageId The pafa templatepage id
     * @param nIndexRow the index row
     * @return The html code of the list
     */
    private String getTemplatesPageList( int nTemplatePageId, int nPageTemplateDocumentId, String nIndexRow )
    {
        HashMap model = new HashMap(  );
        DocumentPageTemplate documentPageTemplate = DocumentPageTemplateHome.findByPrimaryKey( nTemplatePageId );
        model.put( MARK_DOCUMENT_PAGE_TEMPLATE, documentPageTemplate );

        model.put( MARK_INDEX_ROW, nIndexRow );

        String strChecked = ( documentPageTemplate.getId(  ) == nPageTemplateDocumentId ) ? "checked=\"checked\"" : "";
        model.put( MARK_DOCUMENT_PAGE_TEMPLATE_CHECKED, strChecked );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DOCUMENT_PAGE_TEMPLATE_ROW, getLocale(  ),
                model );

        return template.getHtml(  );
    }
}
