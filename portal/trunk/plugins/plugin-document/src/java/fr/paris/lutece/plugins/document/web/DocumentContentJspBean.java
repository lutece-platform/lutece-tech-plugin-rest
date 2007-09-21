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
import fr.paris.lutece.plugins.document.business.DocumentComment;
import fr.paris.lutece.plugins.document.business.DocumentCommentHome;
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.document.business.DocumentType;
import fr.paris.lutece.plugins.document.business.DocumentTypeHome;
import fr.paris.lutece.plugins.document.service.publishing.PublishingService;
import fr.paris.lutece.portal.business.mailinglist.Recipient;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;
import java.util.Collection;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * Document Content JspBean
 */
public class DocumentContentJspBean
{
    //////////////////////////////////////////////////////////////////////////////
    // Constants

    // Parameters
    private static final String PARAMETER_DOCUMENT_ID = "document_id";
    private static final String PARAMETER_SEND_MESSAGE = "send";
    private static final String PARAMETER_VISITOR_LASTNAME = "visitor_last_name";
    private static final String PARAMETER_VISITOR_FIRSTNAME = "visitor_first_name";
    private static final String PARAMETER_VISITOR_EMAIL = "visitor_email";
    private static final String PARAMETER_RECIPIENT_EMAIL = "recipient_email";
    private static final String PARAMETER_COMMENT_DOCUMENT = "comment";
    private static final String PARAMETER_PORTLET_ID = "portlet_id";
    private static final String PARAMETER_EMAIL = "email";
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_CONTENT = "content";
    private static final String PARAMETER_MANDATORY_FIELD = "mandatory";
    private static final String PARAMETER_XSS_ERROR = "xsserror";
    private static final String PARAMETER_CHECK_EMAIL = "checkemail";


    // Markers
    private static final String MARK_DOCUMENT = "document";
    private static final String MARK_BASE_URL = "base_url";
    private static final String MARK_PREVIEW = "preview";
    private static final String MARK_PORTAL_DOMAIN = "portal_domain";
    private static final String MARK_PORTAL_NAME = "portal_name";
    private static final String MARK_PORTAL_URL = "portal_url";
    private static final String MARK_ALERT = "alert";
    private static final String MARK_VISITOR_LAST_NAME = "visitor_last_name";
    private static final String MARK_VISITOR_FIRST_NAME = "visitor_first_name";
    private static final String MARK_VISITOR_EMAIL = "visitor_email";
    private static final String MARK_RECIPIENT_EMAIL = "recipient_email";
    private static final String MARK_COMMENT_DOCUMENT = "comment";
    private static final String MARK_CURRENT_DATE = "current_date";
    private static final String MARK_DOCUMENT_COMMENT = "document_comment";
            
    // Properties
    private static final String PROPERTY_SENDING_OK = "document.message.sending.ok";
    private static final String PROPERTY_SENDING_NOK = "document.message.sending.nok";
    private static final String PROPERTY_MANDATORY_FIELD = "document.message.mandatory.field";
    private static final String PROPERTY_COMMENT_NOTIFY_SUBJECT = "document.message.notify.subject";
    private static final String PROPERTY_MAIL_HOST = "mail.server";
    private static final String PROPERTY_PORTAL_NAME = "lutece.name";
    private static final String PROPERTY_PORTAL_URL = "lutece.prod.url";

    // Templates
    private static final String TEMPLATE_PAGE_PRINT_DOCUMENT = "skin/plugins/document/page_print_document.html";
    private static final String TEMPLATE_PAGE_SEND_DOCUMENT = "skin/plugins/document/page_send_document.html";
    private static final String TEMPLATE_MESSAGE_DOCUMENT = "skin/plugins/document/message_document.html";
    private static final String TEMPLATE_COMMENT_NOTIFY_MESSAGE = "skin/plugins/document/comment_notify_message.html";
    
    // Jsp
    private static final String PAGE_PORTAL = "jsp/site/Portal.jsp";
    private static final String PAGE_SEND_DOCUMENT = "SendDocument.jsp";
    private static final String EMPTY_STRING = "";
    
    // suffix that allows to get the url used in the mail - defined in document-rulenotifyusers.properties
    private static final String SUFFIX_URL_PREVIEW = "document-rulenotifyusers.previewDocument";

    

    /////////////////////////////////////////////////////////////////////////////
    // page management of printing document

    /**
     * Return the view of an document before printing
     * @param request  Http request
     * @return the HTML page
     */
    public String getPrintDocumentPage( HttpServletRequest request )
    {
        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        int nDocumentId = Integer.parseInt( strDocumentId );
        Document document = DocumentHome.findByPrimaryKey( nDocumentId );
        DocumentType type = DocumentTypeHome.findByPrimaryKey( document.getCodeDocumentType(  ) );

        String strPreview = XmlTransformerService.transformBySource( document.getXmlValidatedContent(  ),
                type.getContentServiceXslSource(  ), null, null );
        HashMap model = new HashMap(  );

        model.put( MARK_BASE_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_PREVIEW, strPreview );
        model.put( MARK_PORTAL_DOMAIN, request.getServerName(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PAGE_PRINT_DOCUMENT, request.getLocale(  ),
                model );

        return template.getHtml(  );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Document sending

    /**
     * Return the view of an document before sending
     * @param request  Http request
     * @return the HTML page
     */
    public String getSendDocumentPage( HttpServletRequest request )
    {
        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        int nDocumentId = Integer.parseInt( strDocumentId );

        Document document = DocumentHome.findByPrimaryKey( nDocumentId );
        DocumentType type = DocumentTypeHome.findByPrimaryKey( document.getCodeDocumentType(  ) );
        HashMap model = new HashMap(  );

        String strPreview = XmlTransformerService.transformBySource( document.getXmlValidatedContent(  ),
                type.getContentServiceXslSource(  ), null, null );

        String strSendMessage = request.getParameter( PARAMETER_SEND_MESSAGE );
        String strAlert = "";
        String strVisitorLastName = request.getParameter( PARAMETER_VISITOR_LASTNAME );
        strVisitorLastName = ( strVisitorLastName != null ) ? strVisitorLastName : "";

        String strVisitorFirstName = request.getParameter( PARAMETER_VISITOR_FIRSTNAME );
        strVisitorFirstName = ( strVisitorFirstName != null ) ? strVisitorFirstName : "";

        String strVisitorEmail = request.getParameter( PARAMETER_VISITOR_EMAIL );
        strVisitorEmail = ( strVisitorEmail != null ) ? strVisitorEmail : "";

        String strRecipientEmail = request.getParameter( PARAMETER_RECIPIENT_EMAIL );
        strRecipientEmail = ( strRecipientEmail != null ) ? strRecipientEmail : "";

        String strCommentDocument = request.getParameter( PARAMETER_COMMENT_DOCUMENT );
        strCommentDocument = ( strCommentDocument != null ) ? strCommentDocument : "";

        if ( ( strSendMessage != null ) && ( strSendMessage.equals( "done" ) ) )
        {
            strAlert = I18nService.getLocalizedString( PROPERTY_SENDING_OK, request.getLocale(  ) );
        }

        if ( ( strSendMessage != null ) && ( strSendMessage.equals( "empty_field" ) ) )
        {
            strAlert = I18nService.getLocalizedString( PROPERTY_MANDATORY_FIELD, request.getLocale(  ) );
        }

        if ( ( strSendMessage != null ) && ( strSendMessage.equals( "error_exception" ) ) )
        {
            strAlert = I18nService.getLocalizedString( PROPERTY_SENDING_NOK, request.getLocale(  ) );
        }

        model.put( MARK_BASE_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_PREVIEW, strPreview );
        model.put( MARK_DOCUMENT, document );
        model.put( MARK_PORTAL_DOMAIN, request.getServerName(  ) );
        model.put( MARK_ALERT, strAlert );
        model.put( MARK_VISITOR_LAST_NAME, strVisitorLastName );
        model.put( MARK_VISITOR_FIRST_NAME, strVisitorFirstName );
        model.put( MARK_VISITOR_EMAIL, strVisitorEmail );
        model.put( MARK_RECIPIENT_EMAIL, strRecipientEmail );
        model.put( MARK_COMMENT_DOCUMENT, strCommentDocument );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_PAGE_SEND_DOCUMENT, request.getLocale(  ), model );

        return t.getHtml(  );
    }

    /**
     * Processes of document sending
     * @param request The Http request
     * @return The jsp url of the parent page
     */
    public String doSendDocument( HttpServletRequest request )
    {
        String strPortalName = AppPropertiesService.getProperty( PROPERTY_PORTAL_NAME );
        String strPortalUrl = AppPropertiesService.getProperty( PROPERTY_PORTAL_URL );

        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        int nDocumentId = Integer.parseInt( strDocumentId );

        HashMap model = new HashMap(  );
        Document document = DocumentHome.findByPrimaryKey( nDocumentId );
        DocumentType type = DocumentTypeHome.findByPrimaryKey( document.getCodeDocumentType(  ) );

        String strPreview = XmlTransformerService.transformBySource( document.getXmlValidatedContent(  ),
                type.getContentServiceXslSource(  ), null, null );

        String strVisitorLastName = request.getParameter( PARAMETER_VISITOR_LASTNAME );
        String strVisitorFirstName = request.getParameter( PARAMETER_VISITOR_FIRSTNAME );
        String strVisitorEmail = request.getParameter( PARAMETER_VISITOR_EMAIL );
        String strRecipientEmail = request.getParameter( PARAMETER_RECIPIENT_EMAIL );

        /*String strCommentDocument = request.getParameter( PARAMETER_COMMENT_DOCUMENT );
        strCommentDocument = ( strCommentDocument != null ) ? strCommentDocument : "";*/
        String strCurrentDate = DateUtil.getCurrentDateString(  );

        // Mandatory Fields
        if ( strVisitorLastName.equals( "" ) || strVisitorFirstName.equals( "" ) || strVisitorEmail.equals( "" ) ||
                strRecipientEmail.equals( "" ) )
        {
            return PAGE_SEND_DOCUMENT + "?send=empty_field&" + PARAMETER_DOCUMENT_ID + "=" + nDocumentId +
            "&visitor_last_name=" + strVisitorLastName + "&visitor_first_name=" + strVisitorFirstName +
            "&visitor_email=" + strVisitorEmail + "&recipient_email=" + strRecipientEmail; /* + "&comment=" +
            strCommentDocument;*/
        }

        model.put( MARK_BASE_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_PREVIEW, strPreview );
        model.put( MARK_DOCUMENT, document );
        model.put( MARK_PORTAL_DOMAIN, request.getServerName(  ) );
        model.put( MARK_PORTAL_NAME, strPortalName );
        model.put( MARK_PORTAL_URL, strPortalUrl );
        model.put( MARK_CURRENT_DATE, strCurrentDate );
        model.put( MARK_VISITOR_LAST_NAME, strVisitorLastName );
        model.put( MARK_VISITOR_FIRST_NAME, strVisitorFirstName );
        model.put( MARK_VISITOR_EMAIL, strVisitorEmail );
        model.put( MARK_RECIPIENT_EMAIL, strRecipientEmail );

        //     model.put( MARK_COMMENT_DOCUMENT, strCommentDocument );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MESSAGE_DOCUMENT, request.getLocale(  ), model );

        String strMessageText = template.getHtml(  );
        MailService.sendMail( strRecipientEmail, strVisitorLastName, strVisitorEmail, document.getTitle(  ),
            strMessageText );

        return PAGE_SEND_DOCUMENT + "?send=done&" + PARAMETER_DOCUMENT_ID + "=" + nDocumentId;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Creation of comments on the site

    /**
     * Processes the creation of an document comment
     *
     * @param request the Http request
     * @return The jsp URL to which the user is redirected
     */
    public String doAddComment( HttpServletRequest request )
    {
        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        String strEmail = request.getParameter( PARAMETER_EMAIL );
        String strName = request.getParameter( PARAMETER_NAME );
        String strContent = request.getParameter( PARAMETER_CONTENT );

        String strPagePortal = AppPathService.getBaseUrl( request ) + PAGE_PORTAL;

        // Check XSS characters
        if ( ( strContent != null ) && ( StringUtil.containsXssCharacters( strContent ) ) )
        {
            return strPagePortal + "?" + PARAMETER_DOCUMENT_ID + "=" + strDocumentId + "&" + PARAMETER_PORTLET_ID +
            "=" + strPortletId + "&" + PARAMETER_COMMENT_DOCUMENT + "=1&" + PARAMETER_XSS_ERROR + "=1";
        }

        if ( EMPTY_STRING.equals( strEmail.trim(  ) ) || EMPTY_STRING.equals( strName.trim(  ) ) ||
                EMPTY_STRING.equals( strEmail.trim(  ) ) )
        {
            return strPagePortal + "?" + PARAMETER_DOCUMENT_ID + "=" + strDocumentId + "&" + PARAMETER_PORTLET_ID +
            "=" + strPortletId + "&" +  PARAMETER_COMMENT_DOCUMENT + "=1&" + PARAMETER_MANDATORY_FIELD + "=1";
        }

        if ( ! StringUtil.checkEmail( strEmail.trim() ) )
        {
            return strPagePortal + "?" + PARAMETER_DOCUMENT_ID + "=" + strDocumentId + "&" + PARAMETER_PORTLET_ID +
            "=" + strPortletId + "&" + PARAMETER_COMMENT_DOCUMENT + "=1&" + PARAMETER_CHECK_EMAIL + "=1";                        
        }
        // Retrieve the document from the database :
        int nDocumentId = Integer.parseInt( strDocumentId );
        Document document = DocumentHome.findByPrimaryKey( nDocumentId );

        // Only add the comment if the document accepts comments
        if ( document.getAcceptSiteComments(  ) == 1 )
        {
            DocumentComment documentComment = new DocumentComment(  );

            // Properties submitted with the form :
            documentComment.setDocumentId( nDocumentId );
            documentComment.setEmail( strEmail );
            documentComment.setName( strName );
            documentComment.setComment( strContent );

            // Computed properties :
            documentComment.setIpAddress( request.getRemoteAddr(  ) );

            // The date is set automatically in the DAO
            // If the document is not moderated, the comment is published
            // immediately
            if ( document.getIsModeratedComment(  ) == 0 )
            {
                documentComment.setStatus( 1 );

                // Delete the document from the page cache since new
                // comments have been published.                            
            }
            else
            {
                documentComment.setStatus( 0 );
            }

            // Create the comment in the database
            DocumentCommentHome.create( documentComment );

            if ( document.getIsEmailNotifiedComment() == 1 )
            {
                // Send notification
                sendCommentNotification( request , document , documentComment );            
            }
        }

        return strPagePortal + "?" + PARAMETER_DOCUMENT_ID + "=" + strDocumentId + "&" + PARAMETER_PORTLET_ID + "=" +
        strPortletId;
    }    
    
    private void sendCommentNotification( HttpServletRequest request , Document document , DocumentComment documentComment )
    {
        int nMailingListId = document.getMailingListId();
        Collection<Recipient> listRecipients = AdminMailingListService.getRecipients( nMailingListId );

        for ( Recipient recipient : listRecipients )
        {
            HashMap model = new HashMap();
            
            String strSenderEmail = request.getParameter( PARAMETER_EMAIL );
            String strSenderName = request.getParameter( PARAMETER_NAME );
            String strSubject = I18nService.getLocalizedString( PROPERTY_COMMENT_NOTIFY_SUBJECT , request.getLocale() );            

            // Generate the subject of the message
            strSubject += ( " " + document.getTitle(  ) );

            // Generate the body of the message
            model.put( MARK_DOCUMENT, document );
            model.put( MARK_DOCUMENT_COMMENT, documentComment );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_COMMENT_NOTIFY_MESSAGE, request.getLocale(), model );            
            String strBody = template.getHtml(  );


            MailService.sendMail( recipient.getEmail(  ), strSenderName, strSenderEmail, strSubject, strBody );    
        }    
    }
}
