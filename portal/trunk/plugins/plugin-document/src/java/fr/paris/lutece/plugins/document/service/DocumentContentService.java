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
package fr.paris.lutece.plugins.document.service;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentComment;
import fr.paris.lutece.plugins.document.business.DocumentCommentHome;
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.document.business.DocumentType;
import fr.paris.lutece.plugins.document.business.DocumentTypeHome;
import fr.paris.lutece.plugins.document.business.portlet.DocumentListPortlet;
import fr.paris.lutece.plugins.document.business.portlet.DocumentListPortletHome;
import fr.paris.lutece.portal.business.portlet.AliasPortlet;
import fr.paris.lutece.portal.business.portlet.AliasPortletHome;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.style.ModeHome;
import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;


/**
 *
 */
public class DocumentContentService extends ContentService
{
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String CONTENT_SERVICE_NAME = "Document Content Service";
    private static final String EMPTY_STRING = "";
    private static final String ACCEPT_SITE_COMMENTS = "1";
    private static final int MODE_ADMIN = 1;

    // Parameters
    private static final String PARAMETER_DOCUMENT_ID = "document_id";
    private static final String PARAMETER_COMMENT_DOCUMENT = "comment";
    private static final String PARAMETER_MANDATORY_FIELD = "mandatory";
    private static final String PARAMETER_XSS_ERROR = "xsserror";
    private static final String PARAMETER_CHECK_EMAIL = "checkemail";
    private static final String PARAMETER_SITE_PATH = "site-path";

    // Markers
    private static final String MARK_DOCUMENT = "document";
    private static final String MARK_ACCEPT_COMMENT = "accept_comment";
    private static final String MARK_PORTLET = "portlet";
    private static final String MARK_CATEGORY = "categories";
    private static final String MARK_DOCUMENT_ID = "document_id";
    private static final String MARK_PORTLET_ID = "portlet_id";
    private static final String MARK_PORTLET_ID_LIST = "portlet_id_list";
    private static final String MARK_DOCUMENT_COMMENTS = "document_comments";
    private static final String MARK_DOCUMENT_COMMENT_FORM = "document_comment_form";
    private static final String MARK_DOCUMENT_COMMENTS_LIST = "document_comments_list";
    private static final String MARK_DOCUMENT_CATEGORIES_LIST = "document_categories_list";
    private static final String MARK_XSS_ERROR_MESSAGE = "xss_error_message";
    private static final String MARK_CHECK_EMAIL_MESSAGE = "check_email_message";
    private static final String MARK_MANDATORY_FIELD_MESSAGE = "mandatory_field_message";
    private static final String MARK_MAILINGLIST = "mailinglist";

    // Templates
    private static final String TEMPLATE_DOCUMENT_PAGE_DEFAULT = "/skin/plugins/document/document_content_service.html";
    private static final String TEMPLATE_DOCUMENT_COMMENTS = "/skin/plugins/document/document_comments.html";
    private static final String TEMPLATE_DOCUMENT_CATEGORIES = "/skin/plugins/document/document_categories.html";
    private static final String TEMPLATE_ADD_DOCUMENT_COMMENT = "/skin/plugins/document/add_document_comment.html";

    /**
     * Returns the document page for a given document and a given portlet. The page is built from XML data or retrieved
     * from the cache if it's enable and the document in it.
     *
     * @param request The HTTP request.
     * @param nMode The current mode.
     * @return The HTML code of the page as a String.
     * @throws UserNotSignedException
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    public String getPage( HttpServletRequest request, int nMode )
        throws UserNotSignedException, SiteMessageException
    {
        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        String strPortletId = request.getParameter( Parameters.PORTLET_ID );
        int nPortletId;
        int nDocumentId;
        boolean bPortletExist = false;

        try
        {
            nPortletId = Integer.parseInt( strPortletId );
            nDocumentId = Integer.parseInt( strDocumentId );
        }
        catch ( NumberFormatException nfe )
        {
            return PortalService.getDefaultPage( request, nMode );
        }

        Document document = DocumentHome.findByPrimaryKey( nDocumentId );

        if ( document == null )
        {
            return PortalService.getDefaultPage( request, nMode );
        }

        DocumentType type = DocumentTypeHome.findByPrimaryKey( document.getCodeDocumentType(  ) );
        Collection listPortlet = DocumentListPortletHome.getPortletsListbyDocumentId( strDocumentId );

        // Check if document is published in the specified portlet
        for ( DocumentListPortlet documentPortlet : (ArrayList<DocumentListPortlet>) listPortlet )
        {
            // Check if portlet is an alias portlet
            boolean bIsAlias = DocumentListPortletHome.checkIsAliasPortlet( documentPortlet.getId(  ) );

            if ( bIsAlias && ( documentPortlet.getId(  ) != nPortletId ) )
            {
                AliasPortlet alias = (AliasPortlet) AliasPortletHome.findByPrimaryKey( nPortletId );
                nPortletId = alias.getAliasId(  );
                strPortletId = Integer.toString( nPortletId );
            }

            if ( ( documentPortlet.getId(  ) == nPortletId ) && ( documentPortlet.getStatus(  ) == 0 ) )
            {
                bPortletExist = true;
            }
        }

        if ( bPortletExist )
        {
            String strDocument = XmlTransformerService.transformBySource( document.getXmlValidatedContent(  ),
                    type.getContentServiceXslSource(  ), null, null );
            HashMap model = new HashMap(  );
            model.put( MARK_DOCUMENT, strDocument );
            model.put( MARK_ACCEPT_COMMENT, document.getAcceptSiteComments(  ) );
            model.put( MARK_PORTLET, getPortlet( request, strPortletId, nMode ) );
            model.put( MARK_CATEGORY, getRelatedDocumentsPortlet( request, document, nPortletId, nMode ) );
            model.put( MARK_DOCUMENT_ID, strDocumentId );
            model.put( MARK_PORTLET_ID, strPortletId );
            model.put( MARK_DOCUMENT_COMMENTS, getComments( strDocumentId, strPortletId, nMode, request ) );

            HtmlTemplate template = AppTemplateService.getTemplate( getTemplatePage( document ), request.getLocale(  ),
                    model );

            // Fill a PageData structure for those elements
            PageData data = new PageData(  );
            data.setName( document.getTitle(  ) );
            data.setPagePath( PortalService.getXPagePathContent( document.getTitle(  ), 0, request ) );
            data.setContent( template.getHtml(  ) );

            return PortalService.buildPageContent( data, nMode, request );
        }
        else //portlet does not exists
        {
            //TODO : view access denied page
            return PortalService.getDefaultPage( request, nMode );
        }
    }

    /**
     * Analyzes request parameters to see if the request should be handled by the current Content Service
     *
     * @param request The HTTP request
     * @return true if this ContentService should handle this request
     */
    public boolean isInvoked( HttpServletRequest request )
    {
        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        String strIdPortlet = request.getParameter( Parameters.PORTLET_ID );

        if ( ( strDocumentId != null ) && ( strDocumentId.length(  ) > 0 ) && ( strIdPortlet != null ) &&
                ( strIdPortlet.length(  ) > 0 ) )
        {
            return true;
        }

        return false;
    }

    /**
     * Returns the Content Service name
     *
     * @return The name as a String
     */
    public String getName(  )
    {
        return CONTENT_SERVICE_NAME;
    }

    private String getTemplatePage( Document document )
    {
        if ( document.getPageTemplateDocumentId(  ) != 0 )
        {
            String strPageTemplateDocument = DocumentHome.getPageTemplateDocumentPath( document.getPageTemplateDocumentId(  ) );

            return strPageTemplateDocument;
        }
        else
        {
            return TEMPLATE_DOCUMENT_PAGE_DEFAULT;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Comments implementation
    /**
     * Gets the documents list portlet containing the document
     *
     * @param strPortletId The ID of the documents list portlet where the document has been published.
     * @param nMode The current mode.
     * @param request The Http request
     * @return The HTML code of the documents list portlet as a String
     */
    private static synchronized String getPortlet( HttpServletRequest request, String strPortletId, int nMode )
    {
        try
        {
            int nPortletId = Integer.parseInt( strPortletId );

            DocumentListPortlet portlet = (DocumentListPortlet) PortletHome.findByPrimaryKey( nPortletId );
            String strXml = portlet.getXmlDocument( request );

            // Selection of the XSL stylesheet
            byte[] baXslSource = portlet.getXslSource( nMode );

            // Get request paramaters and store them in a hashtable
            Enumeration enumParam = request.getParameterNames(  );
            Hashtable<String, String> htParamRequest = new Hashtable<String, String>(  );
            String paramName = "";

            while ( enumParam.hasMoreElements(  ) )
            {
                paramName = (String) enumParam.nextElement(  );
                htParamRequest.put( paramName, request.getParameter( paramName ) );
            }

            Properties outputProperties = ModeHome.getOuputXslProperties( nMode );

            // Add a path param for choose url to use in admin or normal mode
            if ( nMode != MODE_ADMIN )
            {
                htParamRequest.put( PARAMETER_SITE_PATH, AppPathService.getPortalUrl(  ) );
            }
            else
            {
                htParamRequest.put( PARAMETER_SITE_PATH, AppPathService.getAdminPortalUrl(  ) );
            }

            return XmlTransformerService.transformBySource( strXml, baXslSource, htParamRequest, outputProperties );
        }
        catch ( NumberFormatException e )
        {
            return null;
        }
    }

    /**
     * Gets the category list portlet linked with the document
     *
     * @param request The Http request
     * @param document The document
     * @param nPortletId The ID of the documents list portlet where the document has been published.
     * @param nMode The current mode.
     * @return The HTML code of the categories list portlet as a String
     */
    private static synchronized String getRelatedDocumentsPortlet( HttpServletRequest request, Document document,
        int nPortletId, int nMode )
    {
        if ( ( nMode != MODE_ADMIN ) && ( document.getCategories(  ) != null ) &&
                ( document.getCategories(  ).size(  ) > 0 ) )
        {
            HashMap model = new HashMap(  );
            List<Document> listRelatedDocument = DocumentHome.findPublishedByRelatedCategories( document,
                    request.getLocale(  ) );

            List<Document> listDocument = new ArrayList<Document>(  );
            ReferenceList listDocumentPortlet = new ReferenceList(  );

            // Create list of related documents from the specified categories of input document 
            for ( Document relatedDocument : listRelatedDocument )
            {
                // Get list of portlets for each document
                for ( DocumentListPortlet documentListPortlet : (Collection<DocumentListPortlet>) DocumentListPortletHome.getPortletsListbyDocumentId( 
                        Integer.toString( relatedDocument.getId(  ) ) ) )
                {
                    // Check if document and portlet are published and document is not the input document 
                    if ( ( DocumentListPortletHome.checkPublishingIntoPortlet( relatedDocument.getId(  ),
                                documentListPortlet.getId(  ) ) == true ) && ( documentListPortlet.getStatus(  ) == 0 ) &&
                            ( relatedDocument.getId(  ) != document.getId(  ) ) )
                    {
                        listDocumentPortlet.addItem( Integer.toString( relatedDocument.getId(  ) ),
                            Integer.toString( documentListPortlet.getId(  ) ) );
                        listDocument.add( relatedDocument );

                        break;
                    }
                }
            }

            model.put( MARK_DOCUMENT_CATEGORIES_LIST, listDocument );
            model.put( MARK_PORTLET_ID_LIST, listDocumentPortlet );

            HtmlTemplate templateComments = AppTemplateService.getTemplate( TEMPLATE_DOCUMENT_CATEGORIES,
                    request.getLocale(  ), model );

            return templateComments.getHtml(  );
        }
        else
        {
            return EMPTY_STRING;
        }
    }

    /**
     * Returns the HTML code for the comments area
     * @param strDocumentId the identifier of the document
     * @param strPortletId The identifier of the documents list portlet where the documznt has been published.
     * @param nMode The current mode.
     * @param request The HTTP servlet request
     * @return the HTML code corresponding to the comment area (empty string if the document cannot be commented)
     */
    private static String getComments( String strDocumentId, String strPortletId, int nMode, HttpServletRequest request )
    {
        int nDocumentId = Integer.parseInt( strDocumentId );
        Document document = DocumentHome.findByPrimaryKey( nDocumentId );

        int nMailingListId = document.getMailingListId(  );
        String strMailingListId = Integer.toString( nMailingListId );

        HashMap model = new HashMap(  );
        model.put( MARK_DOCUMENT, document );

        if ( ( nMode != MODE_ADMIN ) && ( document.getAcceptSiteComments(  ) == 1 ) )
        {
            // if the addition of a comment has been requested, display the form
            String strComment = request.getParameter( PARAMETER_COMMENT_DOCUMENT );

            // check mandatory fields
            String strMandatoryField = request.getParameter( PARAMETER_MANDATORY_FIELD );
            strMandatoryField = ( strMandatoryField != null ) ? strMandatoryField : "";

            // check xss errors
            String strXssError = request.getParameter( PARAMETER_XSS_ERROR );
            strXssError = ( strXssError != null ) ? strXssError : "";

            // check emails errors
            String strCheckEmail = request.getParameter( PARAMETER_CHECK_EMAIL );
            strCheckEmail = ( strCheckEmail != null ) ? strCheckEmail : "";

            if ( ACCEPT_SITE_COMMENTS.equals( strComment ) )
            {
                // Generate the add document form
                model.put( MARK_DOCUMENT_COMMENT_FORM,
                    getAddCommentForm( request, strDocumentId, strPortletId, strMailingListId, strXssError,
                        strCheckEmail, strMandatoryField ) );
            }
            else
            {
                model.put( MARK_DOCUMENT_COMMENT_FORM, EMPTY_STRING );
            }

            // Generate the list of comments            
            List<DocumentComment> documentComments = DocumentCommentHome.findPublishedByDocument( nDocumentId );
            model.put( MARK_DOCUMENT_COMMENTS_LIST, documentComments );

            HtmlTemplate templateComments = AppTemplateService.getTemplate( TEMPLATE_DOCUMENT_COMMENTS,
                    request.getLocale(  ), model );

            return templateComments.getHtml(  );
        }
        else
        {
            return EMPTY_STRING;
        }
    }

    /**
     * Return the comment creation form
     * @param strDocumentId the identifier of the document
     * @param strPortletId the identifier of the portlet
     * @return the HTML code of the form
     */
    private static String getAddCommentForm( HttpServletRequest request, String strDocumentId, String strPortletId,
        String strMailingListId, String strXssError, String strCheckEmail, String strMandatoryField )
    {
        HashMap model = new HashMap(  );

        model.put( MARK_DOCUMENT_ID, strDocumentId );
        model.put( MARK_PORTLET_ID, strPortletId );
        model.put( MARK_MAILINGLIST, strMailingListId );
        model.put( MARK_XSS_ERROR_MESSAGE, strXssError );
        model.put( MARK_CHECK_EMAIL_MESSAGE, strCheckEmail );
        model.put( MARK_MANDATORY_FIELD_MESSAGE, strMandatoryField );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ADD_DOCUMENT_COMMENT, request.getLocale(  ),
                model );

        return template.getHtml(  );
    }
}
