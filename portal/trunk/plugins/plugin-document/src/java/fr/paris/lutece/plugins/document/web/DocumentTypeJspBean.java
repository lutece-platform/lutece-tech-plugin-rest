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

import fr.paris.lutece.plugins.document.business.DocumentType;
import fr.paris.lutece.plugins.document.business.DocumentTypeHome;
import fr.paris.lutece.plugins.document.business.attributes.AttributeTypeHome;
import fr.paris.lutece.plugins.document.business.attributes.AttributeTypeParameter;
import fr.paris.lutece.plugins.document.business.attributes.DocumentAttribute;
import fr.paris.lutece.plugins.document.business.attributes.DocumentAttributeHome;
import fr.paris.lutece.plugins.document.service.AttributeManager;
import fr.paris.lutece.plugins.document.service.AttributeService;
import fr.paris.lutece.plugins.document.service.metadata.MetadataService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * JSP Bean for document type management
 */
public class DocumentTypeJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_DOCUMENT_TYPES_MANAGEMENT = "DOCUMENT_TYPES_MANAGEMENT";
    private static final String TEMPLATE_MANAGE_DOCUMENT_TYPES = "admin/plugins/document/manage_document_types.html";
    private static final String TEMPLATE_CREATE_DOCUMENT_TYPE = "admin/plugins/document/create_document_type.html";
    private static final String TEMPLATE_MODIFY_DOCUMENT_TYPE = "admin/plugins/document/modify_document_type.html";
    private static final String TEMPLATE_ADD_ATTRIBUTE = "admin/plugins/document/add_document_type_attribute.html";
    private static final String TEMPLATE_MODIFY_ATTRIBUTE = "admin/plugins/document/modify_document_type_attribute.html";
    private static final String PROPERTY_PAGE_TITLE_CREATE_DOCUMENT_TYPE = "document.create_document_type.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_DOCUMENT_TYPE = "document.modify_document_type.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_ADD_ATTRIBUTE = "document.add_document_type_attribute.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_ATTRIBUTE = "document.modify_document_type_attribute.pageTitle";
    private static final String PROPERTY_CANNOT_DELETE_DOCUMENTS = "document.message.cannotRemoveTypeDocuments";
    private static final String PROPERTY_CONFIRM_DELETE_TYPE = "document.message.confirmDeleteType";
    private static final String PROPERTY_CONFIRM_DELETE_ATTRIBUTE = "document.message.confirmDeleteAttribute";
    private static final String MESSAGE_DOCUMENT_ALREADY_EXIST = "document.message.documentType.errorAlreadyExist";
    private static final String MARK_DOCUMENT_TYPES_LIST = "document_types_list";
    private static final String MARK_ATTRIBUTES_LIST = "attributes_list";
    private static final String MARK_DOCUMENT_TYPE = "document_type";
    private static final String MARK_ATTRIBUTE_TYPES_LIST = "attribute_types_list";
    private static final String MARK_DOCUMENT_TYPE_CODE = "document_type_code";
    private static final String MARK_ATTRIBUTE_TYPE_CODE = "attribute_type_code";
    private static final String MARK_ATTRIBUTE_EXTRAS_PARAMETERS = "attribute_parameters";
    private static final String MARK_ATTRIBUTE = "attribute";
    private static final String MARK_METADATA_HANDLERS_LIST = "metadata_handlers_list";
    private static final String PARAMETER_DOCUMENT_TYPE_CODE = "document_type_code";
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_CODE = "code";
    private static final String PARAMETER_DESCRIPTION = "description";
    private static final String PARAMETER_ICON_URL = "icon_url";
    private static final String PARAMETER_REQUIRED = "required";
    private static final String PARAMETER_SEARCHABLE = "searchable";
    private static final String PARAMETER_ATTRIBUTE_TYPE_CODE = "attribute_type_code";
    private static final String PARAMETER_ATTRIBUTE_ID = "attribute_id";
    private static final String PARAMETER_INDEX = "index";
    private static final String PARAMETER_METADATA = "metadata";
    private static final String JSP_MODIFY_DOCUMENT_TYPE = "ModifyDocumentType.jsp";
    private static final String JSP_DELETE_DOCUMENT_TYPE = "jsp/admin/plugins/document/DoDeleteDocumentType.jsp";
    private static final String JSP_DELETE_ATTRIBUTE = "jsp/admin/plugins/document/DoDeleteAttribute.jsp";
    private static final String CHECK_ON = "on";
    private ArrayList _attributesList = new ArrayList(  );
    private String _strDocumentTypeCode;

    /**
     * Gets the Document Types Management Page
     * @param request The HTTP request
     * @return The Document Types Management Page
     */
    public String getManageDocumentTypes( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );
        model.put( MARK_DOCUMENT_TYPES_LIST, DocumentTypeHome.findAll(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_DOCUMENT_TYPES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Gets the document type creation page
     * @param request The HTTP request
     * @return The document type creation page
     */
    public String getCreateDocumentType( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_DOCUMENT_TYPE );

        HashMap model = new HashMap(  );
        model.put( MARK_METADATA_HANDLERS_LIST, MetadataService.getMetadataHandlersList(  ) );
        model.put( MARK_ATTRIBUTES_LIST, _attributesList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_DOCUMENT_TYPE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Perform the document type creation
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doCreateDocumentType( HttpServletRequest request )
    {
        String strName = request.getParameter( PARAMETER_NAME );
        String strDescription = request.getParameter( PARAMETER_DESCRIPTION );
        String strCode = request.getParameter( PARAMETER_CODE );
        String strMetadata = request.getParameter( PARAMETER_METADATA );

        // Mandatory fields
        if ( strName.equals( "" ) || strDescription.equals( "" ) || strCode.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( DocumentTypeHome.findByPrimaryKey( strCode ) != null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_DOCUMENT_ALREADY_EXIST, AdminMessage.TYPE_STOP );
        }

        DocumentType documentType = new DocumentType(  );
        documentType.setName( strName );
        documentType.setCode( strCode );
        documentType.setDescription( strDescription );
        documentType.setMetadataHandler( strMetadata );
        DocumentTypeHome.create( documentType );

        return JSP_MODIFY_DOCUMENT_TYPE + "?document_type_code=" + strCode;
    }

    /**
     * Gets the document type modification page
     * @param request The HTTP request
     * @return The document type modification page
     */
    public String getModifyDocumentType( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_DOCUMENT_TYPE );

        String strDocumentTypeCode = request.getParameter( PARAMETER_DOCUMENT_TYPE_CODE );

        if ( strDocumentTypeCode != null )
        {
            _strDocumentTypeCode = strDocumentTypeCode;
        }
        else
        {
            strDocumentTypeCode = _strDocumentTypeCode;
        }

        DocumentType documentType = DocumentTypeHome.findByPrimaryKey( strDocumentTypeCode );
        ReferenceList listAttributeTypes = AttributeTypeHome.getAttributeTypesList( getLocale(  ) );
        HashMap model = new HashMap(  );
        model.put( MARK_DOCUMENT_TYPE, documentType );
        model.put( MARK_METADATA_HANDLERS_LIST, MetadataService.getMetadataHandlersList(  ) );
        model.put( MARK_ATTRIBUTE_TYPES_LIST, listAttributeTypes );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_DOCUMENT_TYPE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Perform document type modification creation
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doModifyDocumentType( HttpServletRequest request )
    {
        String strCode = request.getParameter( PARAMETER_CODE );
        String strName = request.getParameter( PARAMETER_NAME );
        String strDescription = request.getParameter( PARAMETER_DESCRIPTION );
        String strIconUrl = request.getParameter( PARAMETER_ICON_URL );
        String strMetadata = request.getParameter( PARAMETER_METADATA );

        // Mandatory fields
        if ( strName.equals( "" ) || strDescription.equals( "" ) || strCode.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        DocumentType documentType = DocumentTypeHome.findByPrimaryKey( strCode );
        documentType.setName( strName );
        documentType.setDefaultThumbnailUrl( strIconUrl );
        documentType.setDescription( strDescription );
        documentType.setMetadataHandler( strMetadata );
        DocumentTypeHome.update( documentType );

        return getHomeUrl( request );
    }

    /**
     * Gets ttribute creation page
     * @param request The HTTP request
     * @return the html template
     */
    public String getAddAttribute( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_ADD_ATTRIBUTE );

        String strAttributeTypeCode = request.getParameter( PARAMETER_ATTRIBUTE_TYPE_CODE );
        AttributeManager manager = AttributeService.getInstance(  ).getManager( strAttributeTypeCode );
        HashMap model = new HashMap(  );
        model.put( MARK_ATTRIBUTE_TYPE_CODE, strAttributeTypeCode );
        //        model.put( MARK_ATTRIBUTE_EXTRAS_PARAMETERS , manager.getExtraParameters(  getLocale() ) );
        model.put( MARK_ATTRIBUTE_EXTRAS_PARAMETERS, manager.getCreateParametersFormHtml( getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADD_ATTRIBUTE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Perform attribute creation
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doAddAttribute( HttpServletRequest request )
    {
        DocumentAttribute attribute = new DocumentAttribute(  );
        getAttributeData( request, attribute );

        String strValidateMessage = getAttributeValidationMessage( attribute );

        if ( strValidateMessage != null )
        {
            return AdminMessageService.getMessageUrl( request, strValidateMessage, AdminMessage.TYPE_STOP );
        }

        DocumentAttributeHome.create( attribute );

        return JSP_MODIFY_DOCUMENT_TYPE;
    }

    /**
     * Perform attribute modification
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doModifyAttribute( HttpServletRequest request )
    {
        String strAttributeId = request.getParameter( PARAMETER_ATTRIBUTE_ID );
        int nAttributeId = Integer.parseInt( strAttributeId );
        DocumentAttribute attribute = DocumentAttributeHome.findByPrimaryKey( nAttributeId );
        getAttributeData( request, attribute );

        String strValidateMessage = getAttributeValidationMessage( attribute );

        if ( strValidateMessage != null )
        {
            return AdminMessageService.getMessageUrl( request, strValidateMessage, AdminMessage.TYPE_STOP );
        }

        DocumentAttributeHome.update( attribute );

        return JSP_MODIFY_DOCUMENT_TYPE;
    }

    private void getAttributeData( HttpServletRequest request, DocumentAttribute attribute )
    {
        String strName = request.getParameter( PARAMETER_NAME );
        String strCode = request.getParameter( PARAMETER_CODE );
        String strDescription = request.getParameter( PARAMETER_DESCRIPTION );
        String strRequired = request.getParameter( PARAMETER_REQUIRED );
        String strSearchable = request.getParameter( PARAMETER_SEARCHABLE );
        String strAttributeTypeCode = request.getParameter( PARAMETER_ATTRIBUTE_TYPE_CODE );
        String[] arrayStrValues;
        List<String> listValues = new ArrayList<String>(  );

        AttributeManager manager = AttributeService.getInstance(  ).getManager( strAttributeTypeCode );
        List<AttributeTypeParameter> listParameters = manager.getExtraParameters( getLocale(  ) );

        for ( AttributeTypeParameter parameter : listParameters )
        {
            arrayStrValues = request.getParameterValues( parameter.getName(  ) );

            if ( arrayStrValues != null )
            {
                for ( int i = 0; i < arrayStrValues.length; i++ )
                {
                    listValues.add( arrayStrValues[i] );
                }
            }

            parameter.setValueList( listValues );
            listValues.clear(  );
        }

        attribute.setName( strName );
        attribute.setCode( strCode );
        attribute.setDescription( strDescription );
        attribute.setRequired( ( strRequired != null ) && ( strRequired.equals( CHECK_ON ) ) );
        attribute.setSearchable( ( strSearchable != null ) && ( strSearchable.equals( CHECK_ON ) ) );
        attribute.setCodeDocumentType( _strDocumentTypeCode );
        attribute.setCodeAttributeType( strAttributeTypeCode );

        if ( attribute.getAttributeOrder(  ) == 0 )
        {
            // Order is not defined (creation). Put this attribute at the end of the list
            DocumentType documentType = DocumentTypeHome.findByPrimaryKey( _strDocumentTypeCode );
            attribute.setAttributeOrder( documentType.getAttributes(  ).size(  ) + 1 );
        }

        attribute.setParameters( listParameters );
    }

    private String getAttributeValidationMessage( DocumentAttribute attribute )
    {
        String strMessage = null;

        // Mandatory fields
        if ( ( attribute.getName(  ).equals( "" ) ) || ( attribute.getDescription(  ).equals( "" ) ) ||
                ( attribute.getCode(  ).equals( "" ) ) )
        {
            return Messages.MANDATORY_FIELDS;
        }

        AttributeManager manager = AttributeService.getInstance(  ).getManager( attribute.getCodeAttributeType(  ) );
        String strValidationErrorMessageKey = manager.validateValueParameters( attribute.getParameters(  ) );

        if ( strValidationErrorMessageKey != null )
        {
            return strValidationErrorMessageKey;
        }

        return strMessage;
    }

    /**
     * Gets the modification page
     * @param request The HTTP request
     * @return The modification page
     */
    public String getModifyAttribute( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_ATTRIBUTE );

        String strAttributeId = request.getParameter( PARAMETER_ATTRIBUTE_ID );
        int nAttributeId = Integer.parseInt( strAttributeId );
        DocumentAttribute attribute = DocumentAttributeHome.findByPrimaryKey( nAttributeId );
        AttributeManager manager = AttributeService.getInstance(  ).getManager( attribute.getCodeAttributeType(  ) );

        HashMap model = new HashMap(  );
        model.put( MARK_DOCUMENT_TYPE_CODE, _strDocumentTypeCode );
        model.put( MARK_ATTRIBUTE_TYPE_CODE, attribute.getCodeAttributeType(  ) );
        //        model.put( MARK_ATTRIBUTE_EXTRAS_PARAMETERS , listParameters );
        model.put( MARK_ATTRIBUTE_EXTRAS_PARAMETERS, manager.getModifyParametersFormHtml( getLocale(  ), nAttributeId ) );
        model.put( MARK_ATTRIBUTE, attribute );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_ATTRIBUTE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Confirm the deletion of a document type
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doConfirmDelete( HttpServletRequest request )
    {
        String strCode = request.getParameter( PARAMETER_DOCUMENT_TYPE_CODE );
        String strDeleteUrl = JSP_DELETE_DOCUMENT_TYPE + "?" + PARAMETER_DOCUMENT_TYPE_CODE + "=" + strCode;
        String strUrl = AdminMessageService.getMessageUrl( request, PROPERTY_CONFIRM_DELETE_TYPE, strDeleteUrl,
                AdminMessage.TYPE_CONFIRMATION );

        // Check if this type has documents
        if ( DocumentTypeHome.checkDocuments( strCode ) )
        {
            strUrl = AdminMessageService.getMessageUrl( request, PROPERTY_CANNOT_DELETE_DOCUMENTS,
                    AdminMessage.TYPE_STOP );
        }

        return strUrl;
    }

    /**
     * Perform the deletion of a document type
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doDeleteDocumentType( HttpServletRequest request )
    {
        String strCode = request.getParameter( PARAMETER_DOCUMENT_TYPE_CODE );
        DocumentTypeHome.remove( strCode );

        return getHomeUrl( request );
    }

    /**
     * Confirm the deletion of an attribute
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doConfirmDeleteAttribute( HttpServletRequest request )
    {
        String strId = request.getParameter( PARAMETER_ATTRIBUTE_ID );
        String strDeleteUrl = JSP_DELETE_ATTRIBUTE + "?" + PARAMETER_ATTRIBUTE_ID + "=" + strId;
        String strUrl = AdminMessageService.getMessageUrl( request, PROPERTY_CONFIRM_DELETE_ATTRIBUTE, strDeleteUrl,
                AdminMessage.TYPE_CONFIRMATION );

        return strUrl;
    }

    /**
     * Perform the attribute deletion
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doDeleteAttribute( HttpServletRequest request )
    {
        String strId = request.getParameter( PARAMETER_ATTRIBUTE_ID );
        int nId = Integer.parseInt( strId );
        DocumentAttributeHome.remove( nId );

        return JSP_MODIFY_DOCUMENT_TYPE;
    }

    /**
     * Perform the move up action
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doAttributeMoveUp( HttpServletRequest request )
    {
        String strIndex = request.getParameter( PARAMETER_INDEX );
        int nIndex = Integer.parseInt( strIndex );

        if ( nIndex > 1 )
        {
            DocumentType documentType = DocumentTypeHome.findByPrimaryKey( _strDocumentTypeCode );
            List list = documentType.getAttributes(  );
            DocumentAttribute attribute1 = (DocumentAttribute) list.get( nIndex - 1 );
            DocumentAttribute attribute2 = (DocumentAttribute) list.get( nIndex - 2 );
            DocumentTypeHome.reorderAttributes( attribute1.getId(  ), nIndex - 1, attribute2.getId(  ), nIndex );
        }

        return JSP_MODIFY_DOCUMENT_TYPE;
    }

    /**
     * Perform the move down action
     * @param request The HTTP request
     * @return The URL to go after performing the action
     */
    public String doAttributeMoveDown( HttpServletRequest request )
    {
        String strIndex = request.getParameter( PARAMETER_INDEX );
        int nIndex = Integer.parseInt( strIndex );
        DocumentType documentType = DocumentTypeHome.findByPrimaryKey( _strDocumentTypeCode );
        List list = documentType.getAttributes(  );

        if ( nIndex < list.size(  ) )
        {
            DocumentAttribute attribute1 = (DocumentAttribute) list.get( nIndex - 1 );
            DocumentAttribute attribute2 = (DocumentAttribute) list.get( nIndex );
            DocumentTypeHome.reorderAttributes( attribute1.getId(  ), nIndex + 1, attribute2.getId(  ), nIndex );
        }

        return JSP_MODIFY_DOCUMENT_TYPE;
    }
}
