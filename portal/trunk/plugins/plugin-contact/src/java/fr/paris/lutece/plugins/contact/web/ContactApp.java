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
package fr.paris.lutece.plugins.contact.web;

import fr.paris.lutece.plugins.contact.business.Contact;
import fr.paris.lutece.plugins.contact.business.ContactHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.mail.MailUtil;
import fr.paris.lutece.util.string.StringUtil;

import java.util.HashMap;

import javax.mail.MessagingException;

import javax.servlet.http.HttpServletRequest;


/**
 * This class manages Contact page.
 */
public class ContactApp implements XPageApplication
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String TEMPLATE_XPAGE_CONTACT = "skin/plugins/contact/page_contact.html";
    private static final String TEMPLATE_MESSAGE_CONTACT = "skin/plugins/contact/message_contact.html";
    private static final String MARK_CONTACTS_LIST = "contacts_list";
    private static final String MARK_DEFAULT_CONTACT = "default_contact";
    private static final String MARK_CONTACT_ALERT = "alert";
    private static final String MARK_VISITOR_LASTNAME = "visitor_last_name";
    private static final String MARK_VISITOR_FIRSTNAME = "visitor_first_name";
    private static final String MARK_VISITOR_ADDRESS = "visitor_address";
    private static final String MARK_VISITOR_EMAIL = "visitor_email";
    private static final String MARK_OBJECT = "message_object";
    private static final String MARK_MESSAGE = "message";
    private static final String MARK_STYLE_LAST_NAME = "style_last_name";
    private static final String MARK_STYLE_FIRST_NAME = "style_first_name";
    private static final String MARK_STYLE_EMAIL = "style_email";
    private static final String MARK_STYLE_OBJECT = "style_object";
    private static final String MARK_STYLE_MESSAGE = "style_message";
    private static final String MARK_STYLE_CONTACT = "style_contact";
    private static final String MARK_PORTAL_URL = "portal_url";
    private static final String MARK_CONTACT_NAME = "contact_name";
    private static final String MARK_CURRENT_DATE = "current_date";
    private static final String PARAMETER_PAGE = "page";
    private static final String PARAMETER_CONTACT = "contact";
    private static final String PARAMETER_VISITOR_LASTNAME = "visitor_last_name";
    private static final String PARAMETER_VISITOR_FIRSTNAME = "visitor_first_name";
    private static final String PARAMETER_VISITOR_ADDRESS = "visitor_address";
    private static final String PARAMETER_VISITOR_EMAIL = "visitor_email";
    private static final String PARAMETER_MESSAGE_OBJECT = "message_object";
    private static final String PARAMETER_MESSAGE = "message";
    private static final String PARAMETER_SEND = "send";
    private static final String PARAMETER_PORTAL_URL = "portal_url";
    private static final String PROPERTY_MAIL_HOST = "mail.server";
    private static final String PROPERTY_SENDING_OK = "contact.message_contact.sending.ok";
    private static final String PROPERTY_MANDATORY_FIELD_MISSING = "contact.message_contact.mandatory.field";
    private static final String PROPERTY_SENDING_NOK = "contact.message_contact.sending.nok";
    private static final String PROPERTY_RECIPIENT_MISSING = "contact.message_contact.recipient.missing";
    private static final String PROPERTY_ERROR_EMAIL = "contact.message_contact.error.email";
    private static final String PROPERTY_COMBO_CHOOSE = "contact.message_contact.comboChoose";
    private static final String PROPERTY_PAGE_TITLE = "contact.pageTitle";
    private static final String PROPERTY_PAGE_PATH = "contact.pagePathLabel";

    // private fields
    private Plugin _plugin;

    /**
     * Returns the content of the page Contact. It is composed by a form which to capture the data to send a message to
     * a contact of the portal.
     *
     * @param request The http request
     * @param nMode The current mode
     * @param plugin The plugin object
     * @return the Content of the page Contact
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        XPage page = new XPage(  );

        String strPluginName = request.getParameter( PARAMETER_PAGE );
        _plugin = PluginService.getPlugin( strPluginName );

        String strPortalUrl = request.getRequestURI(  );

        page.setTitle( AppPropertiesService.getProperty( PROPERTY_PAGE_TITLE ) );
        page.setPathLabel( AppPropertiesService.getProperty( PROPERTY_PAGE_PATH ) );

        String strContact = "0";
        String strVisitorLastName = "";
        String strVisitorFirstName = "";
        String strVisitorEmail = "";
        String strVisitorAddress = "";
        String strObject = "";
        String strMessage = "";
        String strAlert = "";
        String strStyleLastName = "";
        String strStyleFirstName = "";
        String strStyleEmail = "";
        String strStyleContact = "";

        String strStyleObject = "";
        String strStyleMessage = "";
        String strSendMessage = request.getParameter( PARAMETER_SEND );

        if ( ( strSendMessage != null ) && ( strSendMessage.equals( "done" ) ) )
        {
            strAlert = I18nService.getLocalizedString( PROPERTY_SENDING_OK, request.getLocale(  ) );
        }

        if ( ( strSendMessage != null ) && ( strSendMessage.equals( "error_field" ) ) )
        {
            strVisitorLastName = request.getParameter( PARAMETER_VISITOR_LASTNAME );
            strVisitorFirstName = request.getParameter( PARAMETER_VISITOR_FIRSTNAME );
            strVisitorEmail = request.getParameter( PARAMETER_VISITOR_EMAIL );
            strVisitorAddress = request.getParameter( PARAMETER_VISITOR_ADDRESS );
            strObject = request.getParameter( PARAMETER_MESSAGE_OBJECT );
            strMessage = request.getParameter( PARAMETER_MESSAGE );
            strContact = request.getParameter( PARAMETER_CONTACT );
            strAlert = I18nService.getLocalizedString( PROPERTY_MANDATORY_FIELD_MISSING, request.getLocale(  ) );
            strStyleLastName = strVisitorLastName.equals( "" ) ? "error" : "";
            strStyleFirstName = strVisitorFirstName.equals( "" ) ? "error" : "";
            strStyleEmail = strVisitorEmail.equals( "" ) ? "error" : "";
            strStyleObject = strObject.equals( "" ) ? "error" : "";
            strStyleMessage = strMessage.equals( "" ) ? "error" : "";
            strStyleContact = strContact.equals( "0" ) ? "error" : "";
        }

        if ( ( strSendMessage != null ) && ( strSendMessage.equals( "error_recipient" ) ) )
        {
            strVisitorLastName = request.getParameter( PARAMETER_VISITOR_LASTNAME );
            strVisitorFirstName = request.getParameter( PARAMETER_VISITOR_FIRSTNAME );
            strVisitorEmail = request.getParameter( PARAMETER_VISITOR_EMAIL );
            strVisitorAddress = request.getParameter( PARAMETER_VISITOR_ADDRESS );
            strObject = request.getParameter( PARAMETER_MESSAGE_OBJECT );
            strMessage = request.getParameter( PARAMETER_MESSAGE );
            strContact = request.getParameter( PARAMETER_CONTACT );
            strAlert = I18nService.getLocalizedString( PROPERTY_RECIPIENT_MISSING, request.getLocale(  ) );
            strStyleLastName = strVisitorLastName.equals( "" ) ? "error" : "";
            strStyleFirstName = strVisitorFirstName.equals( "" ) ? "error" : "";
            strStyleEmail = strVisitorEmail.equals( "" ) ? "error" : "";
            strStyleObject = strObject.equals( "" ) ? "error" : "";
            strStyleMessage = strMessage.equals( "" ) ? "error" : "";
            strStyleContact = strContact.equals( "0" ) ? "error" : "";
        }

        if ( ( strSendMessage != null ) && ( strSendMessage.equals( "error_email" ) ) )
        {
            strVisitorLastName = request.getParameter( PARAMETER_VISITOR_LASTNAME );
            strVisitorFirstName = request.getParameter( PARAMETER_VISITOR_FIRSTNAME );
            strVisitorEmail = request.getParameter( PARAMETER_VISITOR_EMAIL );
            strVisitorAddress = request.getParameter( PARAMETER_VISITOR_ADDRESS );
            strObject = request.getParameter( PARAMETER_MESSAGE_OBJECT );
            strMessage = request.getParameter( PARAMETER_MESSAGE );
            strContact = request.getParameter( PARAMETER_CONTACT );
            strAlert = I18nService.getLocalizedString( PROPERTY_ERROR_EMAIL, request.getLocale(  ) );
            strStyleLastName = strVisitorLastName.equals( "" ) ? "error" : "";
            strStyleFirstName = strVisitorFirstName.equals( "" ) ? "error" : "";
            strStyleEmail = strVisitorEmail.equals( "" ) ? "error" : "";
            strStyleObject = strObject.equals( "" ) ? "error" : "";
            strStyleMessage = strMessage.equals( "" ) ? "error" : "";
            strStyleContact = strContact.equals( "0" ) ? "error" : "";
        }

        if ( ( strSendMessage != null ) && ( strSendMessage.equals( "error_exception" ) ) )
        {
            strAlert = I18nService.getLocalizedString( PROPERTY_SENDING_NOK, request.getLocale(  ) );
        }

        HashMap model = new HashMap(  );
        model.put( MARK_CONTACT_ALERT, strAlert );
        model.put( MARK_VISITOR_LASTNAME, strVisitorLastName );
        model.put( MARK_VISITOR_FIRSTNAME, strVisitorFirstName );
        model.put( MARK_VISITOR_EMAIL, strVisitorEmail );
        model.put( MARK_VISITOR_ADDRESS, strVisitorAddress );
        model.put( MARK_OBJECT, strObject );
        model.put( MARK_MESSAGE, strMessage );
        model.put( MARK_STYLE_LAST_NAME, strStyleLastName );
        model.put( MARK_STYLE_FIRST_NAME, strStyleFirstName );
        model.put( MARK_STYLE_OBJECT, strStyleObject );
        model.put( MARK_STYLE_EMAIL, strStyleEmail );
        model.put( MARK_STYLE_MESSAGE, strStyleMessage );
        model.put( MARK_STYLE_CONTACT, strStyleContact );
        model.put( MARK_PORTAL_URL, strPortalUrl );

        // Contacts Combo
        ReferenceList listContact = ContactHome.getContactList( _plugin );
        String strComboItem = I18nService.getLocalizedString( PROPERTY_COMBO_CHOOSE, request.getLocale(  ) );
        listContact.addItem( 0, strComboItem );
        model.put( MARK_CONTACTS_LIST, listContact );
        model.put( MARK_DEFAULT_CONTACT, ( ( strContact == null ) || ( strContact.equals( "" ) ) ) ? "0" : strContact );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_CONTACT, request.getLocale(  ), model );

        page.setContent( template.getHtml(  ) );

        return page;
    }

    /**
     * This method tests the parameters stored in the request and send the message if they are corrects. Otherwise, it
     * displays an error message.
     *
     * @param request The http request
     * @return The result of the process of the sending message.
     */
    public String doSendMessage( HttpServletRequest request )
    {
        String strPortalUrl = request.getParameter( PARAMETER_PORTAL_URL );
        String strUrl = strPortalUrl + "?page=contact&send=done";

        String strHost = AppPropertiesService.getProperty( PROPERTY_MAIL_HOST );
        String strVisitorLastName = ( request.getParameter( PARAMETER_VISITOR_LASTNAME ) == null ) ? ""
                                                                                                   : request.getParameter( PARAMETER_VISITOR_LASTNAME );
        String strVisitorFirstName = ( request.getParameter( PARAMETER_VISITOR_FIRSTNAME ) == null ) ? ""
                                                                                                     : request.getParameter( PARAMETER_VISITOR_FIRSTNAME );
        String strVisitorAddress = ( request.getParameter( PARAMETER_VISITOR_ADDRESS ) == null ) ? ""
                                                                                                 : request.getParameter( PARAMETER_VISITOR_ADDRESS );
        String strVisitorEmail = ( request.getParameter( PARAMETER_VISITOR_EMAIL ) == null ) ? ""
                                                                                             : request.getParameter( PARAMETER_VISITOR_EMAIL );
        String strObject = ( request.getParameter( PARAMETER_MESSAGE_OBJECT ) == null ) ? ""
                                                                                        : request.getParameter( PARAMETER_MESSAGE_OBJECT );
        String strMessage = ( request.getParameter( PARAMETER_MESSAGE ) == null ) ? ""
                                                                                  : request.getParameter( PARAMETER_MESSAGE );
        String strDateOfDay = DateUtil.getCurrentDateString(  );
        String strContact = request.getParameter( PARAMETER_CONTACT );
        int nContact = ( strContact == null ) ? 0 : Integer.parseInt( strContact );

        //test the selection of the contact
        if ( nContact == 0 )
        {
            return strPortalUrl + "?page=contact&send=error_recipient&visitor_last_name=" + strVisitorLastName +
            "&visitor_first_name=" + strVisitorFirstName + "&visitor_email=" + strVisitorEmail + "&visitor_address=" +
            strVisitorAddress + "&contact=" + strContact + "&message_object=" + strObject + "&message=" + strMessage;
        }

        String strPluginName = request.getParameter( PARAMETER_PAGE );
        _plugin = PluginService.getPlugin( strPluginName );

        Contact contact = ContactHome.findByPrimaryKey( nContact, _plugin );
        String strEmailContact = contact.getEmail(  );
        String strContactName = contact.getName(  );

        //tests the length of the message  ( 1000 characters maximums )
        if ( strMessage.length(  ) > 1000 )
        {
            strMessage = strMessage.substring( 0, 1000 );
        }

        // Mandatory fields
        if ( strVisitorLastName.equals( "" ) || strVisitorFirstName.equals( "" ) || strVisitorEmail.equals( "" ) ||
                strContact.equals( "" ) || strObject.equals( "" ) || strMessage.equals( "" ) )
        {
            return strPortalUrl + "?page=contact&send=error_field&visitor_last_name=" + strVisitorLastName +
            "&visitor_first_name=" + strVisitorFirstName + "&visitor_email=" + strVisitorEmail + "&visitor_address=" +
            strVisitorAddress + "&contact=" + strContact + "&message_object=" + strObject + "&message=" + strMessage;
        }

        //test the email of the visitor
        //Checking of the presence of the email address and of its format (@ caracter in the address).
        if ( StringUtil.checkEmail( strVisitorEmail ) != true )
        {
            return strPortalUrl + "?page=contact&send=error_email&visitor_last_name=" + strVisitorLastName +
            "&visitor_first_name=" + strVisitorFirstName + "&visitor_email=" + strVisitorEmail + "&visitor_address=" +
            strVisitorAddress + "&contact=" + strContact + "&message_object=" + strObject + "&message=" + strMessage;
        }

        HashMap model = new HashMap(  );
        model.put( MARK_VISITOR_LASTNAME, strVisitorLastName );
        model.put( MARK_VISITOR_FIRSTNAME, strVisitorFirstName );
        model.put( MARK_VISITOR_ADDRESS, strVisitorAddress );
        model.put( MARK_VISITOR_EMAIL, strVisitorEmail );
        model.put( MARK_CONTACT_NAME, strContactName );
        model.put( MARK_MESSAGE, strMessage );
        model.put( MARK_CURRENT_DATE, strDateOfDay );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MESSAGE_CONTACT, request.getLocale(  ), model );

        String strMessageText = template.getHtml(  );

        try
        {
            MailUtil.sendMessageHtml( strHost, strEmailContact, strVisitorLastName, strVisitorEmail, strObject,
                strMessageText );
        }
        catch ( MessagingException e )
        {
            AppLogService.debug( e );

            return strPortalUrl + "?page=contact&send=error_exception";
        }

        return strUrl;
    }
}
