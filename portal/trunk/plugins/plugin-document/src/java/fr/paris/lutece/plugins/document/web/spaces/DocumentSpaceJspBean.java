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
package fr.paris.lutece.plugins.document.web.spaces;

import fr.paris.lutece.plugins.document.business.DocumentFilter;
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.document.business.DocumentTypeHome;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpace;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpaceHome;
import fr.paris.lutece.plugins.document.service.spaces.DocumentSpacesService;
import fr.paris.lutece.plugins.document.service.spaces.SpaceResourceIdService;
import fr.paris.lutece.plugins.document.web.DocumentJspBean;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * JSP Bean for spaces management
 */
public class DocumentSpaceJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_DOCUMENT_SPACE_MANAGEMENT = "DOCUMENT_MANAGEMENT";
    private static final String TEMPLATE_CREATE_SPACE = "/admin/plugins/document/spaces/create_space.html";
    private static final String TEMPLATE_MODIFY_SPACE = "/admin/plugins/document/spaces/modify_space.html";
    private static final String TEMPLATE_MOVE_SPACE = "/admin/plugins/document/spaces/move_space.html";
    private static final String JSP_MANAGE_DOCUMENT = "ManageDocuments.jsp";
    private static final String JSP_DELETE_SPACE = "DoDeleteSpace.jsp";
    private static final String PATH_JSP = "jsp/admin/plugins/document/";
    private static final String PARAMETER_SPACE_ID = "id_space";
    private static final String PARAMETER_PARENT_SPACE_ID = "id_parent_space";
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_DESCRIPTION = "description";
    private static final String PARAMETER_VIEW_TYPE = "view_type";
    private static final String PARAMETER_ICON = "icon";
    private static final String PARAMETER_DOCUMENT_TYPE = "document_type";
    private static final String PARAMETER_ALLOW_CREATION = "allow_creation";
    private static final String MARK_SPACE = "space";
    private static final String MARK_PARENT_SPACE = "parent_space";
    private static final String MARK_VIEW_TYPE = "view_type";
    private static final String MARK_VIEW_TYPES_LIST = "view_types_list";
    private static final String MARK_ICONS_LIST = "icons_list";
    private static final String MARK_DOCUMENT_TYPES_LIST = "document_types_list";
    private static final String MARK_SPACES_BROWSER = "spaces_browser";
    private static final String MARK_SUBMIT_BUTTON_DISABLED = "submit_button_disabled";
    private static final String MESSAGE_CANNOT_DELETE_HAS_CHILDS = "document.spaces.message.cannotDeleteHasChilds";
    private static final String MESSAGE_CANNOT_DELETE_HAS_DOCS = "document.spaces.message.cannotDeleteHasDocs";
    private static final String MESSAGE_CONFIRM_DELETE = "document.spaces.message.confirmDeleteSpace";
    private static final String DEFAULT_VIEW_TYPE = "1";
    private static final String PROPERTY_CREATE_SPACE_PAGE_TITLE = "document.create_space.pageTitle";
    private static final String PROPERTY_MODIFY_SPACE_PAGE_TITLE = "document.modify_space.pageTitle";
    private static final String PROPERTY_MOVE_SPACE_PAGE_TITLE = "document.move_space.pageTitle";
    private static final String MESSAGE_MOVING_SPACE_NOT_AUTHORIZED = "document.message.movingSpaceNotAuthorized";

    /**
     * Gets the create space page
     * @param request The HTTP request
     * @return The create space page
     */
    public String getCreateSpace( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_CREATE_SPACE_PAGE_TITLE );

        String strParentId = request.getParameter( PARAMETER_SPACE_ID );
        int nParentId = Integer.parseInt( strParentId );
        DocumentSpace spaceParent = DocumentSpaceHome.findByPrimaryKey( nParentId );
        HashMap model = new HashMap(  );
        model.put( MARK_VIEW_TYPE, DEFAULT_VIEW_TYPE );
        model.put( MARK_VIEW_TYPES_LIST, DocumentSpaceHome.getViewTypeList( getLocale(  ) ) );
        model.put( MARK_ICONS_LIST, DocumentSpaceHome.getIconsList(  ) );
        model.put( MARK_DOCUMENT_TYPES_LIST, DocumentTypeHome.getDocumentTypesList(  ) );
        model.put( MARK_PARENT_SPACE, spaceParent );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_SPACE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Perform the space creation
     * @param request The HTTP request
     * @return The Feature Home Page
     */
    public String doCreateSpace( HttpServletRequest request )
    {
        DocumentSpace space = new DocumentSpace(  );
        String strErrorUrl = getRequestData( request, space );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        DocumentSpaceHome.create( space );

        return JSP_MANAGE_DOCUMENT;
    }

    /**
     * Gets the modify space page
     * @param request The HTTP request
     * @return the modify space page
     */
    public String getModifySpace( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MODIFY_SPACE_PAGE_TITLE );

        String strIdSpace = request.getParameter( PARAMETER_SPACE_ID );
        int nIdSpace = Integer.parseInt( strIdSpace );
        DocumentSpace space = DocumentSpaceHome.findByPrimaryKey( nIdSpace );
        ReferenceList listDocumentTypes = DocumentTypeHome.getDocumentTypesList(  );
        listDocumentTypes.checkItems( space.getAllowedDocumentTypes(  ) );

        HashMap model = new HashMap(  );
        model.put( MARK_SPACE, space );
        model.put( MARK_VIEW_TYPE, DEFAULT_VIEW_TYPE );
        model.put( MARK_VIEW_TYPES_LIST, DocumentSpaceHome.getViewTypeList( getLocale(  ) ) );
        model.put( MARK_ICONS_LIST, DocumentSpaceHome.getIconsList(  ) );
        model.put( MARK_DOCUMENT_TYPES_LIST, listDocumentTypes );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_SPACE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Perform the space modification
     * @param request The HTTP request
     * @return The Feature Home Page
     */
    public String doModifySpace( HttpServletRequest request )
    {
        String strIdSpace = request.getParameter( PARAMETER_SPACE_ID );
        int nIdSpace = Integer.parseInt( strIdSpace );
        DocumentSpace space = DocumentSpaceHome.findByPrimaryKey( nIdSpace );
        String strErrorUrl = getRequestData( request, space );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        DocumentSpaceHome.update( space );

        return JSP_MANAGE_DOCUMENT;
    }

    private String getRequestData( HttpServletRequest request, DocumentSpace space )
    {
        String strErrorUrl = null;
        String strParentId = request.getParameter( PARAMETER_PARENT_SPACE_ID );
        String strName = request.getParameter( PARAMETER_NAME );
        String strDescription = request.getParameter( PARAMETER_DESCRIPTION );
        String strViewType = request.getParameter( PARAMETER_VIEW_TYPE );
        String strIcon = request.getParameter( PARAMETER_ICON );
        String[] strDocumentType = request.getParameterValues( PARAMETER_DOCUMENT_TYPE );
        String strAllowCreation = request.getParameter( PARAMETER_ALLOW_CREATION );
        boolean bAllowCreation = ( ( strAllowCreation != null ) && ( strAllowCreation.equals( "on" ) ) ) ? true : false;

        // Mandatory fields
        if ( strName.equals( "" ) || strDescription.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        space.setIdParent( Integer.parseInt( strParentId ) );
        space.setName( strName );
        space.setDescription( strDescription );
        space.setViewType( strViewType );
        space.setIdIcon( Integer.parseInt( strIcon ) );
        space.resetAllowedDocumentTypesList(  );
        space.setDocumentCreationAllowed( bAllowCreation );

        if ( strDocumentType != null )
        {
            for ( int i = 0; i < strDocumentType.length; i++ )
            {
                space.addAllowedDocumentType( strDocumentType[i] );
            }
        }

        return strErrorUrl;
    }

    /**
     * Confirm the deletion
     * @param request The HTTP request
     * @return The Url to go
     */
    public String deleteSpace( HttpServletRequest request )
    {
        String strSpaceId = request.getParameter( PARAMETER_SPACE_ID );
        int nSpaceId = Integer.parseInt( strSpaceId );

        // Check if there is child spaces
        Collection childs = DocumentSpaceHome.findChilds( nSpaceId );

        if ( childs.size(  ) > 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_HAS_CHILDS, AdminMessage.TYPE_STOP );
        }

        // Check if documents are stored in this space
        DocumentFilter filter = new DocumentFilter(  );
        filter.setIdSpace( nSpaceId );

        Collection docs = DocumentHome.findByFilter( filter, Locale.getDefault(  ) );

        if ( docs.size(  ) > 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_HAS_DOCS, AdminMessage.TYPE_STOP );
        }

        // Check for user's rights
        if ( !RBACService.isAuthorized( DocumentSpace.RESOURCE_TYPE, strSpaceId,
                    SpaceResourceIdService.PERMISSION_DELETE, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_STOP );
        }

        DocumentSpace space = DocumentSpaceHome.findByPrimaryKey( nSpaceId );

        Object[] messageArgs = { space.getName(  ) };
        UrlItem url = new UrlItem( PATH_JSP + JSP_DELETE_SPACE );
        url.addParameter( PARAMETER_SPACE_ID, nSpaceId );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE, messageArgs, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Perform the deletion
     * @param request The HTTP request
     * @return The Feature Home Page
     */
    public String doDeleteSpace( HttpServletRequest request )
    {
        String strSpaceId = request.getParameter( PARAMETER_SPACE_ID );

        int nSpaceId = Integer.parseInt( strSpaceId );
        DocumentSpace documentSpace = DocumentSpaceHome.findByPrimaryKey( nSpaceId );
        int nParentSpaceId = documentSpace.getIdParent(  );
        UrlItem url = new UrlItem( JSP_MANAGE_DOCUMENT );

        if ( nSpaceId != 0 ) //if space is not root space
        {
            DocumentSpaceHome.remove( nSpaceId );
            url.addParameter( DocumentJspBean.PARAMETER_SPACE_ID_FILTER, nParentSpaceId );
        }

        return url.getUrlWithEntity(  );
    }

    public String getMoveSpace( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MOVE_SPACE_PAGE_TITLE );

        String strSpaceToMoveId = request.getParameter( PARAMETER_SPACE_ID );
        String strSpaceId = request.getParameter( DocumentSpacesService.PARAMETER_BROWSER_SELECTED_SPACE_ID );
        boolean bSubmitButtonDisabled = Boolean.TRUE;
        DocumentSpace spaceToMove = null;

        if ( strSpaceToMoveId != null )
        {
            spaceToMove = DocumentSpaceHome.findByPrimaryKey( Integer.parseInt( strSpaceToMoveId ) );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_ERROR );
        }

        if ( ( strSpaceId != null ) && !strSpaceId.equals( "" ) )
        {
            bSubmitButtonDisabled = Boolean.FALSE;
        }

        HashMap model = new HashMap(  );

        // Spaces browser
        model.put( MARK_SPACE, spaceToMove );
        model.put( MARK_SUBMIT_BUTTON_DISABLED, bSubmitButtonDisabled );
        model.put( MARK_SPACES_BROWSER,
            DocumentSpacesService.getInstance(  ).getSpacesBrowser( request, getUser(  ), getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MOVE_SPACE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    public String doMoveSpace( HttpServletRequest request )
    {
        boolean bCreationAuthorized = Boolean.FALSE;
        String strSpaceId = request.getParameter( DocumentSpacesService.PARAMETER_BROWSER_SELECTED_SPACE_ID );

        if ( strSpaceId == null )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nSpaceId = Integer.parseInt( strSpaceId );
        DocumentSpace space = DocumentSpaceHome.findByPrimaryKey( nSpaceId );
        String strSpaceToMoveId = request.getParameter( PARAMETER_SPACE_ID );
        DocumentSpace spaceToMove = DocumentSpaceHome.findByPrimaryKey( Integer.parseInt( strSpaceToMoveId ) );

        // Check if selected space isn't the space to move
        if ( space.getId(  ) == spaceToMove.getId(  ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_MOVING_SPACE_NOT_AUTHORIZED,
                AdminMessage.TYPE_STOP );
        }

        // Check if selected space isn't child from space to move
        DocumentSpace tmpSpace = DocumentSpaceHome.findByPrimaryKey( space.getId(  ) );

        while ( tmpSpace != null )
        {
            if ( tmpSpace.getIdParent(  ) == spaceToMove.getId(  ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_MOVING_SPACE_NOT_AUTHORIZED,
                    AdminMessage.TYPE_STOP );
            }

            tmpSpace = DocumentSpaceHome.findByPrimaryKey( tmpSpace.getIdParent(  ) );
        }

        // Check if user have rights to create a space into this space
        if ( RBACService.isAuthorized( space, SpaceResourceIdService.PERMISSION_CREATE, getUser(  ) ) )
        {
            bCreationAuthorized = Boolean.TRUE;
        }

        if ( bCreationAuthorized )
        {
            spaceToMove.setIdParent( nSpaceId );
            DocumentSpaceHome.update( spaceToMove );

            return getHomeUrl( request );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_MOVING_SPACE_NOT_AUTHORIZED,
                AdminMessage.TYPE_STOP );
        }
    }
}
