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
package fr.paris.lutece.plugins.document.modules.rulenotifyusers.business;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.rules.AbstractRule;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpaceHome;
import fr.paris.lutece.plugins.document.business.workflow.DocumentState;
import fr.paris.lutece.plugins.document.business.workflow.DocumentStateHome;
import fr.paris.lutece.plugins.document.service.DocumentException;
import fr.paris.lutece.portal.business.mailinglist.MailingList;
import fr.paris.lutece.portal.business.mailinglist.MailingListHome;
import fr.paris.lutece.portal.business.mailinglist.Recipient;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.mailinglist.AdminMailingListService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;


/**
 * This class provides a rule to notify users on document events
 */
public class NotifyUsersRule extends AbstractRule
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String REGEX_ID = "^[\\d]+$";
    private static final String NO_MAILING_LIST = "none";
    private static final String TEMPLATE_CREATE_RULE = "/admin/plugins/document/modules/rulenotifyusers/create_rule_notify_users.html";
    private static final String MARK_SPACES_LIST = "spaces_list";
    private static final String MARK_STATES_LIST = "states_list";
    private static final String MARK_MAILINGLISTS_LIST = "mailinglists_list";
    private static final String MARK_MESSAGE_TEMPLATES_LIST = "message_templates_list";
    private static final String MARK_USER = "user";
    private static final String MARK_DOCUMENT = "document";
    private static final String MARK_URL_PREVIEW = "url_preview";
    private static final String PARAMETER_SPACE_SOURCE_ID = "id_space_source";
    private static final String PARAMETER_STATE_ID = "id_state";
    private static final String PARAMETER_MAILINGLIST_ID = "id_mailinglist";
    private static final String PARAMETER_MESSAGE_TEMPLATE_KEY = "message_template_key";

    // defined in rulenotifyusers_messages.properties
    private static final String PROPERTY_RULE_NAME = "module.document.rulenotifyusers.ruleName";
    private static final String PROPERTY_RULE_DESCRIPTION = "module.document.rulenotifyusers.ruleLiteral";
    private static final String PROPERTY_MAIL_SENDER_NAME = "module.document.rulenotifyusers.mailSenderName";
    private static final String PROPERTY_RULE_ERROR_MAILING_LIST_ID = "module.document.rulenotifyusers.message.create_rule_notify_users.errorMailingListId";
    private static final String PROPERTY_CHOOSE_MAILING_LIST = "module.document.rulenotifyusers.create_rule_notify_users.chooseMailingList";
    private static final String PROPERTY_RULE_UNKNOWN_ERROR = "module.document.rulemovespace.message.create_rule_notify_users.unknownError";

    // defined in document-rulenotifyusers.properties
    private static final String PROPERTY_MESSAGE_TEMPLATES_ENTRIES = "document-rulenotifyusers.messages";
    private static final String PROPERTY_MESSAGE_PREFIX = "document-rulenotifyusers.message.";
    private static final String PROPERTY_MESSAGE_PREFIX_INTERNATIONALIZATION = "module.document.rulenotifyusers.notify_users_rule.message.";
    private static final String SUFFIX_TEMPLATE = ".template";
    private static final String SUFFIX_SUBJECT = ".subject";
    private static final String SUFFIX_DESCRIPTION = ".description";

    // suffix that allows to get the url used in the mail - defined in document-rulenotifyusers.properties
    private static final String SUFFIX_URL_PREVIEW = "document-rulenotifyusers.previewDocument";
    private static String[] _attributes = 
        {
            PARAMETER_SPACE_SOURCE_ID, PARAMETER_MAILINGLIST_ID, PARAMETER_STATE_ID, PARAMETER_MESSAGE_TEMPLATE_KEY,
        };

    /**
     * Gets the Rule name key
     * @return The Rule name key
     */
    public String getNameKey(  )
    {
        return PROPERTY_RULE_NAME;
    }

    /**
     * Apply the rule if conditions are met by the document and the user
     * @param document The document
     * @param user The user
     * @throws DocumentException raise when error occurs in event or rule
     */
    public void apply( Document document, AdminUser user )
        throws DocumentException
    {
        try
        {
            int nSourceSpace = Integer.parseInt( getAttribute( PARAMETER_SPACE_SOURCE_ID ) );
            int nState = Integer.parseInt( getAttribute( PARAMETER_STATE_ID ) );
            int nMailingListId = Integer.parseInt( getAttribute( PARAMETER_MAILINGLIST_ID ) );

            UrlItem url = AppPathService.buildRedirectUrlItem( "", SUFFIX_URL_PREVIEW );

            if ( document.getStateId(  ) == nState )
            {
                if ( document.getSpaceId(  ) == nSourceSpace )
                {
                    Collection<Recipient> listRecipients = AdminMailingListService.getRecipients( nMailingListId );

                    for ( Recipient recipient : listRecipients )
                    {
                        // Build the mail message
                        HashMap model = new HashMap(  );
                        model.put( MARK_USER, user );
                        model.put( MARK_DOCUMENT, document );
                        model.put( MARK_URL_PREVIEW, url.getUrl(  ) );

                        String strMessageTemplate = getMessageTemplate( getAttribute( PARAMETER_MESSAGE_TEMPLATE_KEY ) );
                        String strSubject = getMessageSubject( getAttribute( PARAMETER_MESSAGE_TEMPLATE_KEY ),
                                user.getLocale(  ) );
                        HtmlTemplate t = AppTemplateService.getTemplate( strMessageTemplate, user.getLocale(  ), model );

                        // Send Mail
                        String strSenderName = I18nService.getLocalizedString( PROPERTY_MAIL_SENDER_NAME,
                                user.getLocale(  ) );
                        String strSenderEmail = MailService.getNoReplyEmail(  );

                        MailService.sendMail( recipient.getEmail(  ), strSenderName, strSenderEmail, strSubject,
                            t.getHtml(  ) );
                    }
                }
            }
        }
        catch ( Exception e )
        {
        	AppLogService.error( "Error in NotifyUserRule event : " + e.getMessage(  ) );
        	e.printStackTrace();
        }
    }

    /**
     * Gets the Rule create form
     * @param user The current user using the form
     * @param locale The current locale
     * @return The HTML form
     */
    public String getCreateForm( AdminUser user, Locale locale )
    {
        HashMap model = new HashMap(  );
        Collection listSpaces = DocumentSpaceHome.getDocumentSpaceList(  );
        Collection listStates = DocumentStateHome.getDocumentStatesList( locale );
        ReferenceList listMailingLists = new ReferenceList(  );

        listMailingLists.addItem( NO_MAILING_LIST,
            I18nService.getLocalizedString( PROPERTY_CHOOSE_MAILING_LIST, locale ) );
        listMailingLists.addAll( AdminMailingListService.getMailingLists( user ) );

        model.put( MARK_SPACES_LIST, listSpaces );
        model.put( MARK_STATES_LIST, listStates );
        model.put( MARK_MAILINGLISTS_LIST, listMailingLists );
        model.put( MARK_MESSAGE_TEMPLATES_LIST, getMessageTemplatesList( locale ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_RULE, locale, model );

        return template.getHtml(  );
    }

    /**
     * Check the rule
     *
     * @return null if rule is valid, message if rule not valid
     */
    public String validateRule(  )
    {
        String strMailingListId = getAttribute( PARAMETER_MAILINGLIST_ID );

        if ( ( strMailingListId == null ) || !strMailingListId.matches( REGEX_ID ) )
        {
            return PROPERTY_RULE_ERROR_MAILING_LIST_ID;
        }

        return null;
    }

    /**
     * Gets all attributes of the rule
     * @return attributes of the rule
     */
    public String[] getAttributesList(  )
    {
        return _attributes;
    }

    /**
     * Gets the explicit text of the rule
     * @return The text of the rule
     */
    public String getRule(  )
    {
        int nSourceSpaceId = Integer.parseInt( getAttribute( PARAMETER_SPACE_SOURCE_ID ) );
        String strSourceSpace = DocumentSpaceHome.findByPrimaryKey( nSourceSpaceId ).getName(  );
        String strMailingListId = getAttribute( PARAMETER_MAILINGLIST_ID );
        String strMailingList = null;

        if ( ( strMailingListId != null ) && strMailingListId.matches( REGEX_ID ) )
        {
            int nMailingListId = Integer.parseInt( strMailingListId );
            MailingList mailinglist = MailingListHome.findByPrimaryKey( nMailingListId );
            strMailingList = mailinglist.getDescription(  );
        }

        String strMessageTemplate = getMessageDescription( getAttribute( PARAMETER_MESSAGE_TEMPLATE_KEY ), getLocale(  ) );
        int nStateId = Integer.parseInt( getAttribute( PARAMETER_STATE_ID ) );
        DocumentState state = DocumentStateHome.findByPrimaryKey( nStateId );
        state.setLocale( getLocale(  ) );

        String strState = state.getName(  );
        String[] ruleArgs = { strSourceSpace, strState, strMailingList, strMessageTemplate };

        return I18nService.getLocalizedString( PROPERTY_RULE_DESCRIPTION, ruleArgs, getLocale(  ) );
    }

    /**
     * Gets the list of message templates
     *
     * @param locale The current locale
     * @return A ReferenceList containing all available messages templates
     */
    private ReferenceList getMessageTemplatesList( Locale locale )
    {
        ReferenceList listTemplates = new ReferenceList(  );

        String strEntries = AppPropertiesService.getProperty( PROPERTY_MESSAGE_TEMPLATES_ENTRIES );

        // extracts each item (separated by a comma) from the list
        StringTokenizer strTokens = new StringTokenizer( strEntries, "," );

        while ( strTokens.hasMoreTokens(  ) )
        {
            String strMessageKey = (String) strTokens.nextToken(  );
            String strTemplateDescription = getMessageDescription( strMessageKey, locale );
            listTemplates.addItem( strMessageKey, strTemplateDescription );
        }

        return listTemplates;
    }

    /**
     * Gets the message description from the
     * @param strMessageKey The message key
     * @param locale The current locale
     * @return The message description
     */
    private String getMessageDescription( String strMessageKey, Locale locale )
    {
        return I18nService.getLocalizedString( PROPERTY_MESSAGE_PREFIX_INTERNATIONALIZATION + strMessageKey +
            SUFFIX_DESCRIPTION, locale );
    }

    /**
     * Gets the message subject from the
     * @param strMessageKey The message key
     * @param locale The current locale
     * @return The message subject
     */
    private String getMessageSubject( String strMessageKey, Locale locale )
    {
        return I18nService.getLocalizedString( PROPERTY_MESSAGE_PREFIX_INTERNATIONALIZATION + strMessageKey +
            SUFFIX_SUBJECT, locale );
    }

    /**
     * Gets the message template from the
     * @param strMessageKey The message key
     * @return The message template
     */
    private String getMessageTemplate( String strMessageKey )
    {
        return AppPropertiesService.getProperty( PROPERTY_MESSAGE_PREFIX + strMessageKey + SUFFIX_TEMPLATE );
    }
}
