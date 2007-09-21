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
package fr.paris.lutece.plugins.newsletter.web;

import au.com.bytecode.opencsv.CSVReader;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.newsletter.business.NewsLetter;
import fr.paris.lutece.plugins.newsletter.business.NewsLetterHome;
import fr.paris.lutece.plugins.newsletter.business.NewsLetterTemplate;
import fr.paris.lutece.plugins.newsletter.business.NewsLetterTemplateHome;
import fr.paris.lutece.plugins.newsletter.business.SendingNewsLetter;
import fr.paris.lutece.plugins.newsletter.business.SendingNewsLetterHome;
import fr.paris.lutece.plugins.newsletter.business.Subscriber;
import fr.paris.lutece.plugins.newsletter.business.SubscriberHome;
import fr.paris.lutece.plugins.newsletter.service.NewsLetterResourceIdService;
import fr.paris.lutece.plugins.newsletter.util.NewsletterUtils;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;

//import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Bookmarks;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.filesystem.UploadUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.mail.MailUtil;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.fileupload.FileItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage NewsLetters features
 */
public class NewsletterJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_NEWSLETTER_MANAGEMENT = "NEWSLETTER_MANAGEMENT";
    private static final String CONSTANT_CSV_FILE_EXTENSION = ".csv";
    private static final String CONSTANT_EMAIL_COLUMN_INDEX = ".csv.import.columnindex";

    // private static final String EMAIL_PATTERN = "^[\\w_.\\-]+@[\\w_.\\-]+\\.[\\w]+$";
    private static final String PROPERTY_LIMIT_MAX_SUSCRIBER = "newsletter.limit.max";
    private static final String PROPERTY_LIMIT_MIN_SUSCRIBER = "newsletter.limit.min";
    private static final String PROPERTY_MAIL_HOST = "mail.server";
    private static final String PROPERTY_PORTAL_NAME = "lutece.name";
    private static final String PROPERTY_PATH_TEMPLATE = "path.templates";
    private static final String PROPERTY_PATH_IMAGE_NEWSLETTER_TEMPLATE = ".path.image.newsletter.template";
    private static final String PROPERTY_REGISTER_ACTION = ".compose_newsletter.buttonRegister";
    private static final String PROPERTY_PREPARE_SENDING_ACTION = ".compose_newsletter.buttonPrepareSending";
    private static final String PROPERTY_CANCEL_ACTION = ".compose_newsletter.buttonCancel";
    private static final String PROPERTY_EMAIL_TESTER_DELIMITER = ".emailTester.delimiter";
    
    // Handling of CSV
    private static final String PROPERTY_IMPORT_DELIMITER = ".csv.import.delimiter";
    

    // templates
    private static final String TEMPLATE_MANAGE_NEWSLETTERS = "admin/plugins/newsletter/manage_newsletters.html";
    private static final String TEMPLATE_MODIFY_NEWSLETTER = "admin/plugins/newsletter/modify_newsletter.html";
    private static final String TEMPLATE_CREATE_NEWSLETTER = "admin/plugins/newsletter/create_newsletter.html";
    private static final String TEMPLATE_COMPOSE_NEWSLETTER = "admin/plugins/newsletter/compose_newsletter.html";
    private static final String TEMPLATE_PREPARE_NEWSLETTER = "admin/plugins/newsletter/prepare_newsletter.html";
    private static final String TEMPLATE_SEND_NEWSLETTER = "admin/plugins/newsletter/send_newsletter.html";
    private static final String TEMPLATE_DOCUMENT_LISTS = "admin/plugins/newsletter/document_lists.html";
    private static final String TEMPLATE_MANAGE_NEWSLETTER_TEMPLATE = "admin/plugins/newsletter/manage_templates.html";
    private static final String TEMPLATE_CREATE_NEWSLETTER_TEMPLATE = "admin/plugins/newsletter/add_newsletter_template.html";
    private static final String TEMPLATE_MODIFY_NEWSLETTER_TEMPLATE = "admin/plugins/newsletter/modify_newsletter_template.html";
    private static final String TEMPLATE_MODIFY_NEWSLETTER_TEMPLATE_FILE = "admin/plugins/newsletter/modify_newsletter_template_file.html";
    private static final String TEMPLATE_MANAGE_SUBSCRIBERS = "admin/plugins/newsletter/manage_subscribers.html";
    private static final String TEMPLATE_IMPORT_SUBSCRIBERS = "admin/plugins/newsletter/import_subscribers.html";
    private static final String MARK_LIST_DOCUMENT_TEMPLATES = "document_templates";
    private static final String MARK_LIST_NEWSLETTER_TEMPLATES = "newsletter_templates";
    private static final String MARK_NEWSLETTER = "newsletter";
    private static final String MARK_NEWSLETTER_ID = "newsletter_id";
    private static final String MARK_DOCUMENT_LIST_DESCRIPTION = "document_list_description";
    private static final String MARK_DOCUMENT_LISTS_LIST = "document_lists_list";
    private static final String MARK_NEWSLETTER_CONTENT = "newsletter_content";
    private static final String MARK_HTML_CONTENT = "html_content";
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_NEWSLETTER_TEMPLATE_ID = "newsletter_template_id";
    private static final String MARK_DOCUMENT_TEMPLATE_ID = "document_template_id";
    private static final String MARK_PREVIEW = "newsletter_preview";
    private static final String MARK_NEWSLETTER_OBJECT = "newsletter_object";
    private static final String MARK_REGISTER_ACTION = "register_action";
    private static final String MARK_PREPARE_SENDING_ACTION = "prepare_sending_action";
    private static final String MARK_CANCEL_ACTION = "cancel_action";
    private static final String MARK_DATE_LAST_SEND = "newsletter_last_sent";
    private static final String MARK_IMG_PATH = "img_path";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_NEWSLETTER_LIST = "newsletters_list";
    private static final String MARK_PLUGIN = "plugin";
    private static final String MARK_SUBSCRIBERS_COUNT_LIST = "subscribers_count_list";
    private static final String MARK_SUBSCRIBERS_LIST = "subscribers_list";
    private static final String MARK_TEMPLATES_LIST = "template_list";
    private static final String MARK_WORKGROUP_LIST = "workgroup_list";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_TEMPLATE_ACCESS_BUTTON = "access_button";
    private static final String MARK_UNSUBSCRIBE = "unsubscribe";
    private static final String MARK_UNSUBSCRIBE_LIST = "unsubscribe_list";

    // PARAMETER
    private static final String PARAMETER_ACTION = "action";
    private static final String PARAMETER_NEWSLETTER_ID = "newsletter_id";
    private static final String PARAMETER_DOCUMENT_LIST_ID = "document_list_id";
    private static final String PARAMETER_NEWSLETTER_NAME = "newsletter_name";
    private static final String PARAMETER_NEWSLETTER_SENDER_MAIL = "newsletter_sender_mail";
    private static final String PARAMETER_DATE_FIRST_SEND = "date_first_send";
    private static final String PARAMETER_DATE_LAST_SEND = "date_last_send";
    private static final String PARAMETER_SUBSCRIBER_ID = "subscriber_id";
    private static final String PARAMETER_DOCUMENT_TEMPLATE_ID = "document_template_id";
    private static final String PARAMETER_NEWSLETTER_OBJECT = "newsletter_object";
    private static final String PARAMETER_GENERATE = "generate";
    private static final String PARAMETER_HTML_CONTENT = "html_content";
    private static final String PARAMETER_SUBSCRIBERS_FILE = "newsletter_import_path";
    private static final String PARAMETER_TEMPLATE_PICTURE = "newsletter_template_picture";
    private static final String PARAMETER_TEMPLATE_FILE = "newsletter_template_file";
    private static final String PARAMETER_NEWSLETTER_WORKGROUP = "newsletter_workgroup";
    private static final String PARAMETER_NEWSLETTER_UNSUBSCRIBE = "newsletter_unsubscribe";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_TEST_RECIPIENTS = "newsletter_test_recipients";

    // URL
    private static final String JSP_DO_REMOVE_NEWSLETTER_TEMPLATE = "jsp/admin/plugins/newsletter/DoRemoveNewsLetterTemplate.jsp";
    private static final String JSP_URL_DO_COMPOSE_NEWSLETTER = "ComposeNewsLetter.jsp";
    private static final String JSP_URL_DO_PREPARE_NEWSLETTER = "DoPrepareNewsLetter.jsp";
    private static final String JSP_URL_DO_REMOVE_NEWSLETTER = "jsp/admin/plugins/newsletter/DoRemoveNewsLetter.jsp";
    private static final String JSP_URL_DO_REMOVE_SUBSCRIBER = "jsp/admin/plugins/newsletter/DoUnsubscribeNewsLetterAdmin.jsp";
    private static final String JSP_URL_MANAGE_NEWSLETTER = "ManageNewsLetter.jsp";
    private static final String JSP_URL_MANAGE_NEWSLETTER_TEMPLATES = "ManageTemplates.jsp";
    private static final String JSP_URL_MANAGE_SUBSCRIBERS = "ManageSubscribers.jsp";
    private static final String JSP_URL_PREPARE_NEWSLETTER = "PrepareNewsLetter.jsp";
    private static final String JSP_URL_SEND_NEWSLETTER = "jsp/admin/plugins/newsletter/DoSendNewsLetter.jsp";
    private static final String JSP_URL_TEST_NEWSLETTER = "jsp/admin/plugins/newsletter/DoTestNewsLetter.jsp";

    // messages
    private static final String MESSAGE_CONFIRM_TEST_NEWSLETTER = "newsletter.message.confirmTestNewsletter";
    private static final String MESSAGE_LINKED_TO_NEWSLETTER = "newsletter.message.linkedPortlet";
    private static final String MESSAGE_CONFIRM_CANCEL_COMPOSE = "newsletter.message.confirmCancelComposeNewsletter";
    private static final String MESSAGE_CONFIRM_REMOVE_NEWSLETTER = "newsletter.message.confirmRemoveNewsletter";
    private static final String MESSAGE_CONFIRM_REMOVE_NEWSLETTER_TEMPLATE = "newsletter.message.confirmRemoveNewsletterTemplate";
    private static final String MESSAGE_CONFIRM_REMOVE_SUBSCRIBER = "newsletter.message.confirmRemoveSubscriber";
    private static final String MESSAGE_CONFIRM_SEND_NEWSLETTER = "newsletter.message.confirmSendNewsletter";
    private static final String MESSAGE_CSV_FILE_EMPTY_OR_NOT_VALID_EMAILS = "newsletter.message.csvFileEmptyOrNotValidEmails";
    private static final String MESSAGE_COLUMN_INDEX_NOT_EXIST = "newsletter.message.csvColumnIndexNotExist";
    private static final String MESSAGE_CSV_FILE_EXTENSION = "newsletter.message.csvFileExtension";
    private static final String MESSAGE_EMAIL_EXISTS = "newsletter.message.emailExists";
    private static final String MESSAGE_FIELD_EMAIL_VALID = "newsletter.message.fieldEmailValid";
    private static final String MESSAGE_NO_SUBSCRIBER = "newsletter.message.noSubscriber";
    private static final String MESSAGE_WRONG_EMAIL = "newsletter.message.wrongEmail";
    private static final String MESSAGE_WRONG_EMAIL_SENDER = "newsletter.message.wrongEmailSender";
    private static final String MESSAGE_SENDING_EMPTY_NOT_ALLOWED = "newsletter.message.sendingEmptyNotAllowed";
    private static final String MESSAGE_USED_TEMPLATE = "newsletter.message.usedTemplate";
    private static final String MESSAGE_NO_TEMPLATE = "newsletter.message.noTemplate";
    private static final String MESSAGE_OBJECT_NOT_SPECIFIED = "newsletter.message.noObjectSpecified";
    private static final String PROPERTY_PAGE_TITLE_IMPORT = "newsletter.import_subscribers.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_NEWSLETTERS = "newsletter.manage_newsletters.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE = "newsletter.create_newsletter.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_COMPOSE = "newsletter.compose_newsletter.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY = "newsletter.modify_newsletter.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MANAGE_TEMPLATES = "newsletter.manage_templates.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_ADD_TEMPLATE = "newsletter.add_newsletter_template.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_TEMPLATE = "newsletter.modify_newsletter_template.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_TEMPLATE_FILE = "newsletter.modify_newsletter_template_file.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MANAGE_SUBSCRIBERS = "newsletter.manage_subscribers.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_PREPARE = "newsletter.prepare_newsletter.pageTitle";
    private static final String PROPERTY_ENABLE_TEMPLATE_BUTTON = "enable";
    private static final String MESSAGE_FRAGMENT_NO_CHANGE = "newsletter.message.fragment_no_change";
    private static final String PROPERTY_USERS_PER_PAGE = "paginator.user.itemsPerPage";
    private static final String PROPERTY_NEWSLETTERS_PER_PAGE = "newsletter.newslettersPerPage";
    private static final String PROPERTY_LABEL_UNSUBSCRIBE_TRUE = "newsletter.unsubscribe.true";
    private static final String PROPERTY_LABEL_UNSUBSCRIBE_FALSE = "newsletter.unsubscribe.false";
    private static final String MARK_SEARCH_STRING = "search_string";
    private static final String MARK_VIRTUAL_HOSTS = "virtual_hosts";
    private static final String PROPERTY_VIRTUAL_HOST = "virtualHost.";
    private static final String PROPERTY_TEST_SUBJECT = "newsletter.test.subject";
    private static final String SUFFIX_BASE_URL = ".baseUrl";
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;

    /**
     * Creates a new NewsletterJspBean object.
     */
    public NewsletterJspBean(  )
    {
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_NEWSLETTERS_PER_PAGE, 10 );
    }

    /**
     * Returns the list of newsletters
     *
     * @param request the HTTP request
     * @return the html code for display the newsletters list
     */
    public String getManageNewsLetters( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_NEWSLETTERS );
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        HashMap model = new HashMap(  );
        Collection<NewsLetter> listNewsletter = NewsLetterHome.findAll( getPlugin(  ) );
        listNewsletter = AdminWorkgroupService.getAuthorizedCollection( listNewsletter, getUser(  ) );

        Map<String, Integer> listSubscribersCount = new HashMap<String, Integer>(  );

        for ( NewsLetter newsletter : listNewsletter )
        {
            int nNewsletterId = newsletter.getId(  );
            listSubscribersCount.put( String.valueOf( nNewsletterId ),
                NewsLetterHome.findNbrSubscribers( nNewsletterId, getPlugin(  ) ) );

            //The workgroup description is needed for coherence and not the key
            if ( newsletter.getWorkgroup(  ).equals( SharedConstants.ALL_GROUPS ) )
            {
                newsletter.setWorkgroup( I18nService.getLocalizedString( SharedConstants.PROPERTY_LABEL_ALL_GROUPS,
                        getLocale(  ) ) );
            }
            else
            {
                newsletter.setWorkgroup( AdminWorkgroupHome.findByPrimaryKey( newsletter.getWorkgroup(  ) )
                                                           .getDescription(  ) );
            }
        }

        Paginator paginator = new Paginator( (List<NewsLetter>) listNewsletter, _nItemsPerPage, getHomeUrl( request ),
                PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        model.put( MARK_NEWSLETTER_LIST, paginator.getPageItems(  ) );
        model.put( MARK_SUBSCRIBERS_COUNT_LIST, listSubscribersCount );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );

        Collection refListAllTemplates = NewsLetterTemplateHome.getTemplatesList( getPlugin(  ) );
        refListAllTemplates = RBACService.getAuthorizedCollection( refListAllTemplates,
                NewsLetterResourceIdService.PERMISSION_MANAGE, getUser(  ) );

        if ( refListAllTemplates.size(  ) != 0 )
        {
            model.put( MARK_TEMPLATE_ACCESS_BUTTON, PROPERTY_ENABLE_TEMPLATE_BUTTON );
        }

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_NEWSLETTERS, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Returns the newsletter form for creation
     *
     * @param request The Http request
     * @return the html code of the newsletter form
     */
    public String getCreateNewsLetter( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE );

        HashMap model = new HashMap(  );

        // get the list of document lists
        ReferenceList listUnsubscribe = new ReferenceList(  );
        listUnsubscribe.addItem( SharedConstants.PROPERTY_UNSUBSCRIBE_TRUE,
            I18nService.getLocalizedString( PROPERTY_LABEL_UNSUBSCRIBE_TRUE, getLocale(  ) ) );
        listUnsubscribe.addItem( SharedConstants.PROPERTY_UNSUBSCRIBE_FALSE,
            I18nService.getLocalizedString( PROPERTY_LABEL_UNSUBSCRIBE_FALSE, getLocale(  ) ) );

        ReferenceList listDocumentLists = NewsLetterHome.getDocumentLists(  );
        model.put( MARK_DOCUMENT_LISTS_LIST, listDocumentLists );
        model.put( MARK_DATE_LAST_SEND, DateUtil.getCurrentDateString(  ) );
        model.put( MARK_WORKGROUP_LIST, AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) ) );
        model.put( MARK_UNSUBSCRIBE_LIST, listUnsubscribe );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_NEWSLETTER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the newsletter form of newsletter composition
     *
     * @param request The Http rquest
     * @return the html code of the newsletter composition form
     */
    public String doComposeNewsLetter( HttpServletRequest request )
    {
        Collection newsletterTemplatesList = NewsLetterTemplateHome.getTemplatesCollectionByType( NewsLetterTemplate.CONSTANT_ID_NEWSLETTER,
                getPlugin(  ) );

        Collection documentTemplatesList = NewsLetterTemplateHome.getTemplatesCollectionByType( NewsLetterTemplate.CONSTANT_ID_DOCUMENT,
                getPlugin(  ) );

        // composition not possible if not at least one template for newsletters
        // and one for documents
        if ( ( newsletterTemplatesList.size(  ) == 0 ) || ( documentTemplatesList.size(  ) == 0 ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_NO_TEMPLATE, AdminMessage.TYPE_STOP );
        }

        UrlItem url = new UrlItem( JSP_URL_DO_COMPOSE_NEWSLETTER );
        url.addParameter( PARAMETER_NEWSLETTER_ID, request.getParameter( PARAMETER_NEWSLETTER_ID ) );

        return url.getUrl(  );
    }

    /**
     * Returns the newsletter form of newsletter composition
     *
     * @param request The Http rquest
     * @return the html code of the newsletter composition form
     */
    public String getComposeNewsLetter( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_COMPOSE );

        List<NewsLetterTemplate> newsletterTemplatesList = NewsLetterTemplateHome.getTemplatesCollectionByType( NewsLetterTemplate.CONSTANT_ID_NEWSLETTER,
                getPlugin(  ) );

        List<NewsLetterTemplate> documentTemplatesList = NewsLetterTemplateHome.getTemplatesCollectionByType( NewsLetterTemplate.CONSTANT_ID_DOCUMENT,
                getPlugin(  ) );

        String strBaseUrl = AppPathService.getBaseUrl( request );
        String strPortalUrl = AppPropertiesService.getProperty( SharedConstants.PROPERTY_PROD_URL );
        String strPathImageTemplate = strPortalUrl +
            AppPropertiesService.getProperty( getPlugin(  ).getName(  ) + PROPERTY_PATH_IMAGE_NEWSLETTER_TEMPLATE );

        int nNewsLetterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );
        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( nNewsLetterId, getPlugin(  ) );
        HashMap model = new HashMap(  );

        // Fills the template with specific values
        String strGenerate = request.getParameter( PARAMETER_GENERATE );

        int nTemplateNewsLetterId;
        int nTemplateDocumentId;
        String strHtmlContent;

        if ( ( strGenerate == null ) )
        {
            nTemplateNewsLetterId = newsletter.getNewsLetterTemplateId(  );

            if ( nTemplateNewsLetterId == 0 )
            {
                nTemplateNewsLetterId = newsletterTemplatesList.get( 0 ).getId(  );
            }

            nTemplateDocumentId = newsletter.getDocumentTemplateId(  );

            if ( nTemplateDocumentId == 0 )
            {
                nTemplateDocumentId = documentTemplatesList.get( 0 ).getId(  );
            }

            strHtmlContent = ( newsletter.getHtml(  ) == null ) ? SharedConstants.CONSTANT_EMPTY_STRING
                                                                : newsletter.getHtml(  );
        }
        else
        {
            nTemplateNewsLetterId = Integer.parseInt( request.getParameter( 
                        SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_ID ) );
            nTemplateDocumentId = Integer.parseInt( request.getParameter( PARAMETER_DOCUMENT_TEMPLATE_ID ) );
            strHtmlContent = generateNewsletterHtmlCode( nNewsLetterId, nTemplateNewsLetterId, nTemplateDocumentId );
        }

        strHtmlContent = strHtmlContent.replaceAll( SharedConstants.MARK_PORTAL_URL, strPortalUrl );
        strHtmlContent = strHtmlContent.replaceAll( Bookmarks.WEBAPP_PATH_FOR_LINKSERVICE, strBaseUrl );

        model.put( MARK_HTML_CONTENT, strHtmlContent );
        model.put( MARK_WEBAPP_URL, strBaseUrl );

        // the document lists list
        int[] arrayThemeIds = NewsLetterHome.findNewsletterThemesIds( nNewsLetterId, getPlugin(  ) );
        ReferenceList listDocumenetLists = NewsLetterHome.getDocumentLists(  );
        String[] strSelectedThemes = new String[arrayThemeIds.length];

        for ( int i = 0; i < arrayThemeIds.length; i++ )
        {
            strSelectedThemes[i] = String.valueOf( arrayThemeIds[i] );
        }

        listDocumenetLists.checkItems( strSelectedThemes );
        model.put( MARK_DOCUMENT_LISTS_LIST, listDocumenetLists );
        model.put( MARK_LIST_NEWSLETTER_TEMPLATES, newsletterTemplatesList );
        model.put( MARK_LIST_DOCUMENT_TEMPLATES, documentTemplatesList );
        model.put( MARK_NEWSLETTER, newsletter );
        model.put( MARK_NEWSLETTER_TEMPLATE_ID, nTemplateNewsLetterId );
        model.put( MARK_DOCUMENT_TEMPLATE_ID, nTemplateDocumentId );
        model.put( MARK_REGISTER_ACTION,
            AppPropertiesService.getProperty( getPlugin(  ).getName(  ) + PROPERTY_REGISTER_ACTION ) );
        model.put( MARK_PREPARE_SENDING_ACTION,
            AppPropertiesService.getProperty( getPlugin(  ).getName(  ) + PROPERTY_PREPARE_SENDING_ACTION ) );
        model.put( MARK_CANCEL_ACTION,
            AppPropertiesService.getProperty( getPlugin(  ).getName(  ) + PROPERTY_CANCEL_ACTION ) );

        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, getLocale(  ).getLanguage(  ) );
        model.put( MARK_IMG_PATH, strPathImageTemplate );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_COMPOSE_NEWSLETTER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes the creation form of a new newsletter
     *
     * @param request
     *            The Http request
     * @return The jsp URL which displays the view of the created newsletter
     */
    public String doCreateNewsLetter( HttpServletRequest request )
    {
        // retrieve name and date
        String strNewsletterName = request.getParameter( PARAMETER_NEWSLETTER_NAME );
        String strDateFirstSend = request.getParameter( PARAMETER_DATE_FIRST_SEND );
        String strWorkGroup = request.getParameter( PARAMETER_NEWSLETTER_WORKGROUP );
        String[] strDocumentListIds = request.getParameterValues( PARAMETER_DOCUMENT_LIST_ID );
        String strSenderMail = request.getParameter( PARAMETER_NEWSLETTER_SENDER_MAIL );
        String strTestRecipients = request.getParameter( PARAMETER_TEST_RECIPIENTS );

        // Mandatory fields
        if ( ( strSenderMail == null ) || strSenderMail.equals( "" ) || ( strTestRecipients == null ) ||
                strTestRecipients.equals( "" ) || ( strNewsletterName == null ) || strNewsletterName.equals( "" ) ||
                ( strDateFirstSend == null ) || strDateFirstSend.equals( "" ) || ( strDocumentListIds == null ) ||
                strDocumentListIds.equals( "" ) || ( strWorkGroup == null ) || strWorkGroup.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( !isWrongEmail( strTestRecipients ).equals( "" ) )
        {
            Object[] messageArgs = { isWrongEmail( strTestRecipients ) };

            return AdminMessageService.getMessageUrl( request, MESSAGE_WRONG_EMAIL, messageArgs, AdminMessage.TYPE_STOP );
        }

        if ( !StringUtil.checkEmail( strSenderMail ) )
        {
            Object[] messageArgs = { strSenderMail };

            return AdminMessageService.getMessageUrl( request, MESSAGE_WRONG_EMAIL_SENDER, messageArgs,
                AdminMessage.TYPE_STOP );
        }

        NewsLetter newsletter = new NewsLetter(  );
        newsletter.setName( strNewsletterName );

        java.sql.Date dateFirstSend = DateUtil.getDateSql( strDateFirstSend );

        if ( dateFirstSend != null )
        {
            newsletter.setDateLastSending( new java.sql.Timestamp( dateFirstSend.getTime(  ) ) );
        }
        else
        {
            newsletter.setDateLastSending( new java.sql.Timestamp( new java.util.Date(  ).getTime(  ) ) );
        }

        newsletter.setWorkgroup( strWorkGroup );
        newsletter.setTestRecipients( strTestRecipients );
        newsletter.setNewsletterSenderMail( strSenderMail );
        newsletter.setUnsubscribe( request.getParameter( PARAMETER_NEWSLETTER_UNSUBSCRIBE ) );
        NewsLetterHome.create( newsletter, getPlugin(  ) );

        // Associate a document list to a newsletter
        for ( int i = 0; i < strDocumentListIds.length; i++ )
        {
            int nThemeId = Integer.parseInt( strDocumentListIds[i] );
            NewsLetterHome.associateNewsLetterTheme( newsletter.getId(  ), nThemeId, getPlugin(  ) );
        }

        return getHomeUrl( request );
    }

    /**
     * Returns the newsletter form for modification
     *
     * @param request    The Http request
     * @return the html code of the newsletter form
     */
    public String getModifyNewsLetter( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY );

        Map model = new HashMap(  );
        int nNewsletterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );
        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( nNewsletterId, getPlugin(  ) );

        if ( !AdminWorkgroupService.isAuthorized( newsletter, getUser(  ) ) )
        {
            return getManageNewsLetters( request );
        }

        ReferenceList listUnsubscribe = new ReferenceList(  );
        listUnsubscribe.addItem( "TRUE",
            I18nService.getLocalizedString( PROPERTY_LABEL_UNSUBSCRIBE_TRUE, getLocale(  ) ) );
        listUnsubscribe.addItem( "FALSE",
            I18nService.getLocalizedString( PROPERTY_LABEL_UNSUBSCRIBE_FALSE, getLocale(  ) ) );

        model.put( MARK_NEWSLETTER, newsletter );
        model.put( MARK_WORKGROUP_LIST, AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) ) );
        model.put( MARK_UNSUBSCRIBE_LIST, listUnsubscribe );

        // get the list of document lists associated to the newsletter
        int[] arrayThemeIds = NewsLetterHome.findNewsletterThemesIds( nNewsletterId, getPlugin(  ) );

        // get the list of all document lists
        ReferenceList listDocumentList = NewsLetterHome.getDocumentLists(  );
        String[] strSelectedThemes = new String[arrayThemeIds.length];

        for ( int i = 0; i < arrayThemeIds.length; i++ )
        {
            strSelectedThemes[i] = String.valueOf( arrayThemeIds[i] );
        }

        listDocumentList.checkItems( strSelectedThemes );
        model.put( MARK_DOCUMENT_LISTS_LIST, listDocumentList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_NEWSLETTER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes the update form of the newsletter whose identifier is in the
     * http request
     *
     * @param request
     *            The Http request
     * @return The jsp URL which displays the view of the updated newsletter
     */
    public String doModifyNewsLetter( HttpServletRequest request )
    {
        // retrieve the required parameters
        String strSenderMail = request.getParameter( PARAMETER_NEWSLETTER_SENDER_MAIL );
        String strNewsletterName = request.getParameter( PARAMETER_NEWSLETTER_NAME );
        String strDateLastSend = request.getParameter( PARAMETER_DATE_LAST_SEND );
        String strTestRecipients = request.getParameter( PARAMETER_TEST_RECIPIENTS );
        String[] strThemeIds = request.getParameterValues( PARAMETER_DOCUMENT_LIST_ID );

        // Mandatory fields
        if ( ( strTestRecipients == null ) || strTestRecipients.equals( "" ) || ( strNewsletterName == null ) ||
                strNewsletterName.equals( "" ) || ( strDateLastSend == null ) || strDateLastSend.equals( "" ) ||
                ( strThemeIds == null ) || ( strSenderMail == null ) || strSenderMail.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( !isWrongEmail( strTestRecipients ).equals( "" ) )
        {
            Object[] messageArgs = { isWrongEmail( strTestRecipients ) };

            return AdminMessageService.getMessageUrl( request, MESSAGE_WRONG_EMAIL, messageArgs, AdminMessage.TYPE_STOP );
        }

        if ( !StringUtil.checkEmail( strSenderMail ) )
        {
            Object[] messageArgs = { strSenderMail };

            return AdminMessageService.getMessageUrl( request, MESSAGE_WRONG_EMAIL_SENDER, messageArgs,
                AdminMessage.TYPE_STOP );
        }

        int nNewsletterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );
        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( nNewsletterId, getPlugin(  ) );
        newsletter.setName( strNewsletterName );
        newsletter.setTestRecipients( strTestRecipients );
        newsletter.setNewsletterSenderMail( strSenderMail );

        java.sql.Date dateLastSend = DateUtil.getDateSql( strDateLastSend );

        if ( dateLastSend != null )
        {
            newsletter.setDateLastSending( new java.sql.Timestamp( dateLastSend.getTime(  ) ) );
        }

        newsletter.setUnsubscribe( request.getParameter( PARAMETER_NEWSLETTER_UNSUBSCRIBE ) );
        newsletter.setWorkgroup( request.getParameter( PARAMETER_NEWSLETTER_WORKGROUP ) );
        // if not, newsletter.getDateLastSending keeps its value
        NewsLetterHome.update( newsletter, getPlugin(  ) );
        NewsLetterHome.removeNewsLetterTheme( nNewsletterId, getPlugin(  ) );

        // recreate the list of themes with the new selection
        for ( int i = 0; i < strThemeIds.length; i++ )
        {
            int nThemeId = Integer.parseInt( strThemeIds[i] );
            NewsLetterHome.associateNewsLetterTheme( nNewsletterId, nThemeId, getPlugin(  ) );
        }

        String strId = Integer.toString( nNewsletterId );
        UrlItem url = new UrlItem( JSP_URL_MANAGE_NEWSLETTER );
        url.addParameter( PARAMETER_NEWSLETTER_ID, strId );

        return url.getUrl(  );
    }

    /**
     * Manages the removal form of a newsletter whose identifier is in the http
     * request
     *
     * @param request  The Http request
     * @return the html code to confirm
     */
    public String getConfirmRemoveSubscriber( HttpServletRequest request )
    {
        UrlItem urlItem = new UrlItem( JSP_URL_DO_REMOVE_SUBSCRIBER );
        int nNewsletterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );
        int nSubscriberId = Integer.parseInt( request.getParameter( PARAMETER_SUBSCRIBER_ID ) );
        urlItem.addParameter( PARAMETER_NEWSLETTER_ID, nNewsletterId );
        urlItem.addParameter( PARAMETER_SUBSCRIBER_ID, nSubscriberId );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_SUBSCRIBER, urlItem.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Processes the unregistration of a subscriber for a newsletter
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage newsletters
     */
    public String doUnregistrationAdmin( HttpServletRequest request )
    {
        /* parameters */
        int nNewsletterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );
        int nSubscriberId = Integer.parseInt( request.getParameter( PARAMETER_SUBSCRIBER_ID ) );

        Subscriber subscriber = SubscriberHome.findByPrimaryKey( nSubscriberId, getPlugin(  ) );

        if ( subscriber != null )
        {
            removeSubscriberFromNewsletter( subscriber, nNewsletterId, getPlugin(  ) );
        }

        return JSP_URL_MANAGE_SUBSCRIBERS + "?plugin_name=" + getPlugin(  ).getName(  ) + "&" +
        PARAMETER_NEWSLETTER_ID + "=" + nNewsletterId;
    }

    /**
     * Manages the removal form of a newsletter whose identifier is in the http
     * request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    public String getRemoveNewsLetter( HttpServletRequest request )
    {
        int nNewsletterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );

        if ( NewsLetterHome.checkLinkedPortlets( nNewsletterId ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_LINKED_TO_NEWSLETTER, AdminMessage.TYPE_STOP );
        }

        UrlItem urlItem = new UrlItem( JSP_URL_DO_REMOVE_NEWSLETTER );
        urlItem.addParameter( PARAMETER_NEWSLETTER_ID, nNewsletterId );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_NEWSLETTER, urlItem.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Manages the removal form of a newsletter template whose identifier is in
     * the http request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    public String getRemoveNewsLetterTemplate( HttpServletRequest request )
    {
        int nNewsletterTemplateId = Integer.parseInt( request.getParameter( 
                    SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_ID ) );

        if ( !NewsLetterHome.findTemplate( nNewsletterTemplateId, getPlugin(  ) ) )
        {
            UrlItem url = new UrlItem( JSP_DO_REMOVE_NEWSLETTER_TEMPLATE );
            url.addParameter( SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_ID,
                Integer.parseInt( request.getParameter( SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_ID ) ) );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_NEWSLETTER_TEMPLATE,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_USED_TEMPLATE, AdminMessage.TYPE_STOP );
        }
    }

    /**
     * Processes the removal form of a newsletter
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage newsletters
     */
    public String doRemoveNewsLetter( HttpServletRequest request )
    {
        int nNewsletterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );

        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( nNewsletterId, getPlugin(  ) );

        if ( !AdminWorkgroupService.isAuthorized( newsletter, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.USER_ACCESS_DENIED, AdminMessage.TYPE_ERROR );
        }

        /* Looks for the list of the subscribers */
        Collection<Subscriber> list = SubscriberHome.findSubscribers( nNewsletterId,
                SharedConstants.CONSTANT_EMPTY_STRING,
                Integer.parseInt( AppPropertiesService.getProperty( PROPERTY_LIMIT_MIN_SUSCRIBER ) ),
                Integer.parseInt( AppPropertiesService.getProperty( PROPERTY_LIMIT_MAX_SUSCRIBER ) ), getPlugin(  ) );

        for ( Subscriber subscriber : list )
        {
            NewsLetterHome.removeSubscriber( newsletter.getId(  ), subscriber.getId(  ), getPlugin(  ) );
        }

        // removes relationship between the newsletter and document list
        NewsLetterHome.removeNewsLetterTheme( nNewsletterId, getPlugin(  ) );

        // removes the newsletter
        NewsLetterHome.remove( nNewsletterId, getPlugin(  ) );

        return getHomeUrl( request );
    }

    /**
     * Processes the removal form of a newsletter template
     *
     * @param request  The Http request
     * @return the jsp URL to display the form to manage newsletter templates
     */
    public String doRemoveNewsLetterTemplate( HttpServletRequest request )
    {
        int nNewsletterTemplateId = Integer.parseInt( request.getParameter( 
                    SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_ID ) );

        NewsLetterTemplate newsLetterTemplate = NewsLetterTemplateHome.findByPrimaryKey( nNewsletterTemplateId,
                getPlugin(  ) );
        String strFileName = newsLetterTemplate.getFileName(  );
        String strPictureName = newsLetterTemplate.getPicture(  );

        // removes the file
        String strPathFileNewsletterTemplate = AppPathService.getPath( PROPERTY_PATH_TEMPLATE ) +
            AppPropertiesService.getProperty( getPlugin(  ).getName(  ) +
                SharedConstants.PROPERTY_PATH_FILE_NEWSLETTER_TEMPLATE );
        File file = new File( strPathFileNewsletterTemplate + "/" + strFileName );

        if ( file.exists(  ) )
        {
            file.delete(  );
        }

        // removes the picture
        String strPathImageNewsletterTemplate = AppPathService.getPath( getPlugin(  ).getName(  ) +
                PROPERTY_PATH_IMAGE_NEWSLETTER_TEMPLATE );
        File picture = new File( strPathImageNewsletterTemplate + "/" + strPictureName );

        if ( picture.exists(  ) )
        {
            picture.delete(  );
        }

        // removes the newsletter template from the database
        NewsLetterTemplateHome.remove( nNewsletterTemplateId, getPlugin(  ) );

        // loads the newsletter templates management page
        // If the operation occurred well returns on the info of the newsletter
        UrlItem url = new UrlItem( JSP_URL_MANAGE_NEWSLETTER_TEMPLATES );

        return url.getUrl(  );
    }

    /**
     * Builds the newsletter's templates management page
     *
     * @param request The HTTP request
     * @return the html code for newsletter's templates management page (liste
     *         of templates + available actions)
     */
    public String getManageTemplates( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_TEMPLATES );

        HashMap model = new HashMap(  );

        Collection refListAllTemplates = NewsLetterTemplateHome.getTemplatesList( getPlugin(  ) );
        refListAllTemplates = RBACService.getAuthorizedCollection( refListAllTemplates,
                NewsLetterResourceIdService.PERMISSION_MANAGE, getUser(  ) );
        model.put( MARK_TEMPLATES_LIST, refListAllTemplates );
        model.put( MARK_PLUGIN, getPlugin(  ) );

        // get the list of all templates
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_NEWSLETTER_TEMPLATE, getLocale(  ),
                model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Builds the newsletter's templates creation page
     *
     * @param request  The HTTP request
     * @return the html code for newsletter's templates creation page
     */
    public String getAddNewsLetterTemplate( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_ADD_TEMPLATE );

        // get the list of template types
        // nothing should be checked
        String strDefaultCheckedType = "";
        Map model = new HashMap(  );
        model.put( SharedConstants.MARK_TEMPLATE_TYPE, buildTemplateTypeList( strDefaultCheckedType ) );
        model.put( MARK_PLUGIN, getPlugin(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_NEWSLETTER_TEMPLATE, getLocale(  ),
                model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Builds the newsletter's templates modification page
     *
     * @param request The HTTP request
     * @return the html code for newsletter's templates creation page
     */
    public String getModifyNewsLetterTemplate( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_TEMPLATE );

        int nIdTemplate = Integer.parseInt( request.getParameter( SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_ID ) );
        NewsLetterTemplate newsletterTemplate = NewsLetterTemplateHome.findByPrimaryKey( nIdTemplate, getPlugin(  ) );

        // get the list of template types
        String strDefaultCheckedType = newsletterTemplate.getType(  ) + "";
        Map model = new HashMap(  );
        model.put( SharedConstants.MARK_TEMPLATE_TYPE, buildTemplateTypeList( strDefaultCheckedType ) );

        model.put( SharedConstants.MARK_TEMPLATE, newsletterTemplate );
        model.put( MARK_PLUGIN, getPlugin(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_NEWSLETTER_TEMPLATE, getLocale(  ),
                model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Builds the newsletter's templates modification page (with the
     * modification of the file content)
     *
     * @param request  The HTTP request
     * @return the html code for newsletter's templates creation page
     */
    public String getModifyNewsLetterTemplateFile( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_TEMPLATE_FILE );

        Map model = new HashMap(  );

        try
        {
            int nIdTemplate = Integer.parseInt( request.getParameter( SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_ID ) );
            NewsLetterTemplate newsletterTemplate = NewsLetterTemplateHome.findByPrimaryKey( nIdTemplate, getPlugin(  ) );

            // get the file content
            String strPathFileNewsletterTemplate = AppPathService.getPath( PROPERTY_PATH_TEMPLATE ) +
                AppPropertiesService.getProperty( getPlugin(  ).getName(  ) +
                    SharedConstants.PROPERTY_PATH_FILE_NEWSLETTER_TEMPLATE );

            String strFileName = newsletterTemplate.getFileName(  );
            BufferedReader fileReader = new BufferedReader( new FileReader( strPathFileNewsletterTemplate +
                        File.separator + strFileName ) );

            String strSource = "";
            String line = fileReader.readLine(  );

            while ( line != null )
            {
                strSource += ( line + "\n" );
                line = fileReader.readLine(  );
            }

            fileReader.close(  );

            String strDefaultCheckedType = newsletterTemplate.getType(  ) + "";

            model.put( SharedConstants.MARK_TEMPLATE_TYPE, buildTemplateTypeList( strDefaultCheckedType ) );

            model.put( SharedConstants.MARK_TEMPLATE_SOURCE, strSource );
            model.put( SharedConstants.MARK_TEMPLATE_FILE_NAME, strFileName );
            model.put( SharedConstants.MARK_TEMPLATE, newsletterTemplate );
            model.put( MARK_PLUGIN, getPlugin(  ) );
        }
        catch ( FileNotFoundException f )
        {
            AppLogService.debug( f );
        }
        catch ( IOException i )
        {
            AppLogService.debug( i );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_NEWSLETTER_TEMPLATE_FILE,
                getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Build a radio buttons list of template types from properties
     *
     * @param plugin the plugin
     * @param strCheckedType the element to be selected
     * @return the html code for the radio buttons list
     */
    private static ReferenceList buildTemplateTypeList( String strCheckedType )
    {
        ReferenceList refTemplateTypeList = new ReferenceList(  );
        refTemplateTypeList.addItem( NewsLetterTemplate.CONSTANT_ID_NEWSLETTER,
            NewsLetterTemplate.TEMPLATE_NAMES[NewsLetterTemplate.CONSTANT_ID_NEWSLETTER] );
        refTemplateTypeList.addItem( NewsLetterTemplate.CONSTANT_ID_DOCUMENT,
            NewsLetterTemplate.TEMPLATE_NAMES[NewsLetterTemplate.CONSTANT_ID_DOCUMENT] );
        refTemplateTypeList.checkItems( new String[] { strCheckedType } );

        return refTemplateTypeList;
    }

    /**
     * Builds the newsletter's subscribers management page
     *
     * @param request The HTTP request
     * @return the html code for newsletter's subscribers management page
     */
    public String getManageSubscribers( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_SUBSCRIBERS );

        HashMap model = new HashMap(  );
        int nNewsletterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );

        // check that newsletter exist in db
        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( nNewsletterId, getPlugin(  ) );

        model.put( MARK_NEWSLETTER, newsletter );

        String strSearchString = request.getParameter( SharedConstants.PARAMETER_SUBSCRIBER_SEARCH );

        if ( strSearchString == null )
        {
            strSearchString = SharedConstants.CONSTANT_EMPTY_STRING;
        }

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_USERS_PER_PAGE, 10 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        // get a list of subscribers
        List refListSubscribers = (List<Subscriber>) SubscriberHome.findSubscribers( nNewsletterId, strSearchString,
                Integer.parseInt( AppPropertiesService.getProperty( PROPERTY_LIMIT_MIN_SUSCRIBER ) ),
                Integer.parseInt( AppPropertiesService.getProperty( PROPERTY_LIMIT_MAX_SUSCRIBER ) ), getPlugin(  ) );
        UrlItem url = new UrlItem( request.getRequestURI(  ) );
        url.addParameter( PARAMETER_NEWSLETTER_ID, nNewsletterId );
        url.addParameter( SharedConstants.PARAMETER_SUBSCRIBER_SEARCH, strSearchString );

        Paginator paginator = new Paginator( refListSubscribers, _nItemsPerPage, url.getUrl(  ),
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_SEARCH_STRING, strSearchString );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_SUBSCRIBERS_LIST, paginator.getPageItems(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_SUBSCRIBERS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes the registration of a subscriber
     *
     * @param request The Http request
     * @return The jsp URL which displays the subscribers management page
     */
    public String doAddSubscriber( HttpServletRequest request )
    {
        String strEmail = request.getParameter( SharedConstants.PARAMETER_EMAIL );

        // Mandatory fields
        if ( ( strEmail == null ) || strEmail.equals( SharedConstants.CONSTANT_EMPTY_STRING ) ||
                !StringUtil.checkEmail( strEmail.trim(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_FIELD_EMAIL_VALID, AdminMessage.TYPE_STOP );
        }

        // Checks if a subscriber with the same email address doesn't exist yet
        Subscriber subscriber = SubscriberHome.findByEmail( strEmail, getPlugin(  ) );

        if ( subscriber == null )
        {
            // The email doesn't exist, so create a new subcriber
            subscriber = new Subscriber(  );
            subscriber.setEmail( strEmail.trim(  ) );
            SubscriberHome.create( subscriber, getPlugin(  ) );
        }

        int nNewsLetterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );

        // adds a subscriber to the current newsletter
        if ( NewsLetterHome.findRegistration( nNewsLetterId, subscriber.getId(  ), getPlugin(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_EMAIL_EXISTS, AdminMessage.TYPE_STOP );
        }

        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( nNewsLetterId, getPlugin(  ) );

        // the current date
        Timestamp tToday = new java.sql.Timestamp( new java.util.Date(  ).getTime(  ) );
        NewsLetterHome.addSubscriber( newsletter.getId(  ), subscriber.getId(  ), tToday, getPlugin(  ) );

        // Returns the jsp URL to display the subscribers management page with
        // the new one
        UrlItem urlItem = new UrlItem( JSP_URL_MANAGE_SUBSCRIBERS );
        urlItem.addParameter( PARAMETER_NEWSLETTER_ID, nNewsLetterId );

        return urlItem.getUrl(  );
    }

    /**
     * Processes the creation form of a new newsletter template by recovering
     * the parameters in the http request
     *
     * @param request the http request
     * @return The Jsp URL of the process result
     */
    public String doCreateNewsletterTemplate( HttpServletRequest request )
    {
        NewsLetterTemplate newsletterTemplate = new NewsLetterTemplate(  );

        try
        {
            // initialize the paths
            String strPathImageNewsletterTemplate = AppPathService.getPath( getPlugin(  ).getName(  ) +
                    PROPERTY_PATH_IMAGE_NEWSLETTER_TEMPLATE );
            String strPathFileNewsletterTemplate = AppPathService.getPath( PROPERTY_PATH_TEMPLATE ) +
                AppPropertiesService.getProperty( getPlugin(  ).getName(  ) +
                    SharedConstants.PROPERTY_PATH_FILE_NEWSLETTER_TEMPLATE );

            // create the multipart request
            MultipartHttpServletRequest multi = (MultipartHttpServletRequest) request;

            // Mandatory fields
            String strType = multi.getParameter( SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_TYPE );
            String strDescription = multi.getParameter( SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_NAME );

            if ( ( strType == null ) || ( strType.equals( "" ) ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            if ( ( strDescription == null ) || ( strDescription.equals( "" ) ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            FileItem imageItem = multi.getFile( PARAMETER_TEMPLATE_PICTURE );
            String strImageFileName = UploadUtil.cleanFileName( imageItem.getName(  ) );

            if ( ( imageItem == null ) || strImageFileName.equals( "" ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            //create the directory if it doesn't exist
            if ( !new File( strPathImageNewsletterTemplate ).exists(  ) )
            {
                File fDirectory = new File( strPathImageNewsletterTemplate );
                fDirectory.mkdir(  );
            }

            imageItem.write( new File( strPathImageNewsletterTemplate + File.separator + strImageFileName ) );
            newsletterTemplate.setPicture( strImageFileName );

            FileItem modelItem = multi.getFile( PARAMETER_TEMPLATE_FILE );
            String strTemplateFileName = UploadUtil.cleanFileName( modelItem.getName(  ) );

            if ( ( modelItem == null ) || "".equals( strTemplateFileName ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            modelItem.write( new File( strPathFileNewsletterTemplate + File.separator + strTemplateFileName ) );
            newsletterTemplate.setFileName( strTemplateFileName );

            // Complete the newsLetterTemplate
            newsletterTemplate.setDescription( strDescription );
            newsletterTemplate.setType( Integer.parseInt( strType ) );
            NewsLetterTemplateHome.create( newsletterTemplate, getPlugin(  ) );
        }
        catch ( Exception e )
        {
            AppLogService.error( e );
        }

        return getHomeUrl( request );
    }

    /**
     * Processes the modification form of a newsletter template modified by hand
     * by recovering the parameters in the http request
     *
     * @param request  the http request
     * @return The Jsp URL of the process result
     */
    public String doModifyNewsletterTemplateFile( HttpServletRequest request )
    {
        try
        {
            // initialize the paths
            String strPathImageNewsletterTemplate = AppPathService.getPath( getPlugin(  ).getName(  ) +
                    PROPERTY_PATH_IMAGE_NEWSLETTER_TEMPLATE );
            String strPathFileNewsletterTemplate = AppPathService.getPath( PROPERTY_PATH_TEMPLATE ) +
                AppPropertiesService.getProperty( getPlugin(  ).getName(  ) +
                    SharedConstants.PROPERTY_PATH_FILE_NEWSLETTER_TEMPLATE );

            // create the multipart request
            MultipartHttpServletRequest multi = (MultipartHttpServletRequest) request;

            // creation of the NewsLetterTemplate
            NewsLetterTemplate newsletterTemplate = NewsLetterTemplateHome.findByPrimaryKey( Integer.parseInt( 
                        multi.getParameter( SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_ID ) ), getPlugin(  ) );

            // Mandatory fields
            String strType = multi.getParameter( SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_TYPE );
            String strDescription = multi.getParameter( SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_NAME );

            if ( strDescription.equals( "" ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            // Names of the old files
            String strOldFileName = newsletterTemplate.getFileName(  );
            String strOldImageName = newsletterTemplate.getPicture(  );

            FileItem imageItem = multi.getFile( "newsletter_template_new_picture" );

            if ( ( imageItem != null ) && ( imageItem.getSize(  ) != 0 ) )
            {
                String strFileName = UploadUtil.cleanFileName( imageItem.getName(  ) );
                imageItem.write( new File( strPathImageNewsletterTemplate + File.separator + strFileName ) );
                newsletterTemplate.setPicture( strFileName );

                // we delete the old picture
                File oldImageFile = new File( strPathImageNewsletterTemplate + File.separator + strOldImageName );
                oldImageFile.delete(  );
            }

            // Writes the new content of the file.
            String fileContent = multi.getParameter( SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_SOURCE );

            FileWriter fileWriter = new FileWriter( strPathFileNewsletterTemplate + File.separator + strOldFileName );
            fileWriter.write( fileContent );
            fileWriter.close(  );

            // Complete the newsLetterTemplate
            newsletterTemplate.setDescription( strDescription );
            newsletterTemplate.setType( Integer.parseInt( strType ) );
            NewsLetterTemplateHome.update( newsletterTemplate, getPlugin(  ) );
        }
        catch ( IOException e )
        {
            AppLogService.error( e );
        }
        catch ( Exception e )
        {
            AppLogService.error( e );
        }

        return getHomeUrl( request );
    }

    /**
     * Processes the modification form of a newsletter template by recovering
     * the parameters in the http request
     *
     * @param request the http request
     * @return The Jsp URL of the process result
     */
    public String doModifyNewsletterTemplate( HttpServletRequest request )
    {
        try
        {
            // initialize the paths
            String strPathImageNewsletterTemplate = AppPathService.getPath( getPlugin(  ).getName(  ) +
                    PROPERTY_PATH_IMAGE_NEWSLETTER_TEMPLATE );
            String strPathFileNewsletterTemplate = AppPathService.getPath( PROPERTY_PATH_TEMPLATE ) +
                AppPropertiesService.getProperty( getPlugin(  ).getName(  ) +
                    SharedConstants.PROPERTY_PATH_FILE_NEWSLETTER_TEMPLATE );

            // create the multipart request
            MultipartHttpServletRequest multi = (MultipartHttpServletRequest) request;

            // creation of the NewsLetterTemplate
            NewsLetterTemplate newsletterTemplate = NewsLetterTemplateHome.findByPrimaryKey( Integer.parseInt( 
                        multi.getParameter( SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_ID ) ), getPlugin(  ) );

            // Mandatory fields
            String strType = multi.getParameter( SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_TYPE );
            String strDescription = multi.getParameter( SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_NAME );

            if ( strDescription.equals( "" ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            // Names of the old files
            String strOldFileName = newsletterTemplate.getFileName(  );
            String strOldImageName = newsletterTemplate.getPicture(  );

            FileItem imageItem = multi.getFile( "newsletter_template_new_picture" );

            if ( ( imageItem != null ) && ( imageItem.getSize(  ) != 0 ) )
            {
                // we delete the old picture
                File oldImageFile = new File( strPathImageNewsletterTemplate + File.separator + strOldImageName );
                oldImageFile.delete(  );

                String strFileName = UploadUtil.cleanFileName( imageItem.getName(  ) );
                imageItem.write( new File( strPathImageNewsletterTemplate + File.separator + strFileName ) );
                newsletterTemplate.setPicture( strFileName );
            }

            FileItem modelItem = multi.getFile( "newsletter_template_new_file" );

            if ( ( modelItem != null ) && ( modelItem.getSize(  ) != 0 ) )
            {
                // we delete the old file
                File oldFile = new File( strPathFileNewsletterTemplate + File.separator + strOldFileName );
                oldFile.delete(  );

                String strFileName = UploadUtil.cleanFileName( modelItem.getName(  ) );
                modelItem.write( new File( strPathFileNewsletterTemplate + File.separator + strFileName ) );
                newsletterTemplate.setFileName( strFileName );
            }

            // Complete the newsLetterTemplate
            newsletterTemplate.setDescription( strDescription );
            newsletterTemplate.setType( Integer.parseInt( strType ) );
            NewsLetterTemplateHome.update( newsletterTemplate, getPlugin(  ) );
        }
        catch ( IOException e )
        {
            AppLogService.error( e );
        }
        catch ( Exception e )
        {
            AppLogService.error( e );
        }

        return getHomeUrl( request );
    }

    /**
     * Builds the page of preparation before sending
     *
     * @param request the http request
     * @return the html code for the preparation page
     */
    public String getPrepareNewsLetter( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_PREPARE );

        int nNewsletterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );
        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( nNewsletterId, getPlugin(  ) );
        HashMap model = new HashMap(  );
        String strObject = request.getParameter( PARAMETER_NEWSLETTER_OBJECT );

        if ( strObject != null )
        {
            model.put( MARK_NEWSLETTER_OBJECT, strObject );
        }
        else
        {
            model.put( MARK_NEWSLETTER_OBJECT, "" );
        }

        String strPortalUrl = AppPropertiesService.getProperty( SharedConstants.PROPERTY_PROD_URL );
        model.put( MARK_PREVIEW, newsletter.getHtml(  ) );
        model.put( MARK_UNSUBSCRIBE, newsletter.getUnsubscribe(  ) );
        model.put( MARK_NEWSLETTER, newsletter );
        model.put( SharedConstants.MARK_PORTAL_URL, strPortalUrl );
        model.put( SharedConstants.MARK_SUBSCRIBER_EMAIL, SharedConstants.MARK_SUBSCRIBER_EMAIL_EACH );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PREPARE_NEWSLETTER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Builds the page of preparation before sending
     *
     * @param request  the Http request
     * @return the html code for the preparation page
     */
    public String doPrepareNewsLetter( HttpServletRequest request )
    {
        int nNewsletterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );
        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( nNewsletterId, getPlugin(  ) );

        // allow to send only if the newsletter is not empty
        if ( ( newsletter.getHtml(  ) == null ) || newsletter.getHtml(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_SENDING_EMPTY_NOT_ALLOWED, AdminMessage.TYPE_STOP );
        }

        UrlItem urlItem = new UrlItem( JSP_URL_PREPARE_NEWSLETTER );
        urlItem.addParameter( PARAMETER_NEWSLETTER_ID, nNewsletterId );

        return urlItem.getUrl(  );
    }

    /**
     * Builds the page of preparation before sending
     *
     * @param request the http request
     * @return the html code for the preparation page
     */
    public String getPreviewNewsLetter( HttpServletRequest request )
    {
        int nNewsletterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );
        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( nNewsletterId, getPlugin(  ) );

        String strPortalUrl = AppPropertiesService.getProperty( SharedConstants.PROPERTY_PROD_URL );
        HashMap model = new HashMap(  );
        model.put( SharedConstants.MARK_PORTAL_URL, strPortalUrl );
        model.put( MARK_NEWSLETTER_CONTENT, newsletter.getHtml(  ) );
        model.put( MARK_NEWSLETTER_ID, nNewsletterId );
        model.put( SharedConstants.MARK_SUBSCRIBER_EMAIL, "" );

        HtmlTemplate templateNewsLetter = AppTemplateService.getTemplate( TEMPLATE_SEND_NEWSLETTER, getLocale(  ), model );

        String strBaseUrl = AppPathService.getBaseUrl( request );
        templateNewsLetter.substitute( Bookmarks.WEBAPP_PATH_FOR_LINKSERVICE, strBaseUrl );

        return templateNewsLetter.getHtml(  );
    }

    /**
     * Displays the confirmation page before sending the newsletter
     *
     * @param request the http request
     * @return the html code for the confirmation page
     */
    public String doConfirmSendNewsLetter( HttpServletRequest request )
    {
        int nNewsletterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );
        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( nNewsletterId, getPlugin(  ) );

        // allow to send only if the newsletter is not empty
        if ( ( newsletter.getHtml(  ) == null ) || newsletter.getHtml(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_SENDING_EMPTY_NOT_ALLOWED, AdminMessage.TYPE_STOP );
        }

        // allow to send only if at list one subscriber
        int nNbrSubscribers = NewsLetterHome.findNbrSubscribers( nNewsletterId, getPlugin(  ) );

        if ( nNbrSubscribers == 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_NO_SUBSCRIBER, AdminMessage.TYPE_STOP );
        }

        String strObject = request.getParameter( PARAMETER_NEWSLETTER_OBJECT );

        //Block access if no object for the newsletter specified
        if ( ( strObject == null ) || strObject.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_OBJECT_NOT_SPECIFIED, AdminMessage.TYPE_STOP );
        }

        UrlItem urlItem = new UrlItem( JSP_URL_SEND_NEWSLETTER );
        urlItem.addParameter( PARAMETER_NEWSLETTER_OBJECT, strObject );
        urlItem.addParameter( PARAMETER_NEWSLETTER_ID, nNewsletterId );

        // warn if the newletter html content is the same as the one of the last
        // sending for that newsletter
        SendingNewsLetter lastSending = SendingNewsLetterHome.findLastSendingForNewsletterId( nNewsletterId,
                getPlugin(  ) );

        if ( ( lastSending != null ) && lastSending.getHtml(  ).equals( newsletter.getHtml(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_FRAGMENT_NO_CHANGE, urlItem.getUrl(  ),
                AdminMessage.TYPE_CONFIRMATION );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_SEND_NEWSLETTER, urlItem.getUrl(  ),
                AdminMessage.TYPE_CONFIRMATION );
        }
    }

    /**
     * Displays the confirmation page before testing the newsletter
     *
     * @param request the http request
     * @return the html code for the confirmation page
     */
    public String doConfirmTestNewsLetter( HttpServletRequest request )
    {
        int nNewsletterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );
        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( nNewsletterId, getPlugin(  ) );

        // allow to send only if the newsletter is not empty
        if ( ( newsletter.getHtml(  ) == null ) || newsletter.getHtml(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_SENDING_EMPTY_NOT_ALLOWED, AdminMessage.TYPE_STOP );
        }

        // allow to send only if at list one subscriber
        int nNbrSubscribers = NewsLetterHome.findNbrSubscribers( nNewsletterId, getPlugin(  ) );

        if ( nNbrSubscribers == 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_NO_SUBSCRIBER, AdminMessage.TYPE_STOP );
        }

        String strObject = request.getParameter( PARAMETER_NEWSLETTER_OBJECT );

        UrlItem urlItem = new UrlItem( JSP_URL_TEST_NEWSLETTER );
        urlItem.addParameter( PARAMETER_NEWSLETTER_OBJECT, strObject );
        urlItem.addParameter( PARAMETER_NEWSLETTER_ID, nNewsletterId );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_TEST_NEWSLETTER, urlItem.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Processes the testing of a newsletter
     *
     * @param request  the http request
     * @return the url of the confirmation page
     */
    public String doTestNewsLetter( HttpServletRequest request )
    {
        int nNewsletterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );

        //Allow to send test if the list of test recipients is not empty
        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( nNewsletterId, getPlugin(  ) );
        String strTestRecipients = newsletter.getTestRecipients(  );

        if ( !isWrongEmail( strTestRecipients ).equals( "" ) )
        {
            Object[] messageArgs = { isWrongEmail( strTestRecipients ) };

            return AdminMessageService.getMessageUrl( request, MESSAGE_WRONG_EMAIL, messageArgs, AdminMessage.TYPE_STOP );
        }

        SendingNewsLetter sending = new SendingNewsLetter(  );
        sending.setNewsLetterId( nNewsletterId );
        sending.setDate( new Timestamp( new java.util.Date(  ).getTime(  ) ) ); // the
                                                                                // current
                                                                                // date

        String strObject = I18nService.getLocalizedString( PROPERTY_TEST_SUBJECT, getLocale(  ) ) + " " +
            newsletter.getName(  );
        ;

        String strSenderEmail = newsletter.getNewsletterSenderMail(  );

        /* lutece.properties */
        String strHost = AppPropertiesService.getProperty( PROPERTY_MAIL_HOST );
        String strPortal = AppPropertiesService.getProperty( PROPERTY_PORTAL_NAME );
        String strPortalUrl = AppPropertiesService.getProperty( SharedConstants.PROPERTY_PROD_URL );

        //String strEmailWebmaster = AppPropertiesService.getProperty( PROPERTY_WEBMASTER_EMAIL );
        String strBaseUrl = AppPathService.getBaseUrl( request );

        String strNewsLetterCode;

        HashMap sendingModel = new HashMap(  );
        sendingModel.put( MARK_UNSUBSCRIBE, newsletter.getUnsubscribe(  ) );
        sendingModel.put( MARK_NEWSLETTER_ID, nNewsletterId );
        sendingModel.put( MARK_NEWSLETTER_CONTENT, newsletter.getHtml(  ) );
        sendingModel.put( SharedConstants.MARK_PORTAL_URL, strPortalUrl );
        sendingModel.put( SharedConstants.MARK_SUBSCRIBER_EMAIL, SharedConstants.MARK_SUBSCRIBER_EMAIL_EACH );

        HtmlTemplate templateNewsLetter = AppTemplateService.getTemplate( TEMPLATE_SEND_NEWSLETTER, getLocale(  ),
                sendingModel );

        Map mapAttachment = null;

        if ( isMhtmlActivated( getPlugin(  ) ) )
        {
            String strTemplate = templateNewsLetter.getHtml(  );
            strTemplate = StringUtil.substitute( strTemplate, strBaseUrl, Bookmarks.WEBAPP_PATH_FOR_LINKSERVICE );

            // we use absolute urls if there is no preproduction process
            boolean useAbsoluteUrl = strBaseUrl.equals( strPortalUrl );
            mapAttachment = MailUtil.getAttachmentList( strTemplate, strBaseUrl, useAbsoluteUrl );

            // all images, css urls are relative
            String strWebappPath = useAbsoluteUrl ? strPortalUrl : strBaseUrl.replaceFirst( "https?://[^/]+/", "/" );
            templateNewsLetter.substitute( Bookmarks.WEBAPP_PATH_FOR_LINKSERVICE, strWebappPath );
        }
        else
        {
            templateNewsLetter.substitute( Bookmarks.WEBAPP_PATH_FOR_LINKSERVICE, strPortalUrl + "/" );
        }

        List<String> list = convertToList( newsletter.getTestRecipients(  ) );

        for ( String strTestRecipientMail : list )
        {
            HtmlTemplate t = new HtmlTemplate( templateNewsLetter );
            t.substitute( SharedConstants.MARK_SUBSCRIBER_EMAIL, strTestRecipientMail );

            strNewsLetterCode = t.getHtml(  );

            try
            {
                if ( mapAttachment == null )
                {
                    MailUtil.sendMessageHtml( strHost, strTestRecipientMail, strPortal, strSenderEmail, strObject,
                        strNewsLetterCode );
                }
                else
                {
                    MailUtil.sendMessageHtml( strHost, strTestRecipientMail, strPortal, strSenderEmail, strObject,
                        strNewsLetterCode, mapAttachment );
                }
            }
            catch ( MessagingException e )
            {
                AppLogService.debug( e );
            }
        }

        return getHomeUrl( request );
    }

    /**
     * Processes the sending of a newsletter
     *
     * @param request  the http request
     * @return the url of the confirmation page
     */
    public String doSendNewsLetter( HttpServletRequest request )
    {
        int nNewsletterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );
        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( nNewsletterId, getPlugin(  ) );

        // allow to send only if at list one subscriber
        int nNbrSubscribers = NewsLetterHome.findNbrSubscribers( nNewsletterId, getPlugin(  ) );

        if ( nNbrSubscribers == 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_NO_SUBSCRIBER, AdminMessage.TYPE_STOP );
        }

        SendingNewsLetter sending = new SendingNewsLetter(  );
        sending.setNewsLetterId( nNewsletterId );
        sending.setDate( new Timestamp( new java.util.Date(  ).getTime(  ) ) ); // the

        sending.setCountSubscribers( nNbrSubscribers );
        sending.setHtml( newsletter.getHtml(  ) );

        String strObject = request.getParameter( PARAMETER_NEWSLETTER_OBJECT );

        if ( ( strObject == null ) && ( strObject.trim(  ).equals( "" ) ) )
        {
            strObject = newsletter.getName(  );
        }

        String strSenderEmail = newsletter.getNewsletterSenderMail(  );

        sending.setEmailSubject( strObject );
        SendingNewsLetterHome.create( sending, getPlugin(  ) );

        /* lutece.properties */
        String strHost = AppPropertiesService.getProperty( PROPERTY_MAIL_HOST );
        String strPortal = AppPropertiesService.getProperty( PROPERTY_PORTAL_NAME );
        String strPortalUrl = AppPropertiesService.getProperty( SharedConstants.PROPERTY_PROD_URL );
        String strBaseUrl = AppPathService.getBaseUrl( request );

        /* list of subscribers */
        Collection<Subscriber> list = SubscriberHome.findSubscribers( nNewsletterId,
                Integer.parseInt( AppPropertiesService.getProperty( PROPERTY_LIMIT_MIN_SUSCRIBER ) ),
                Integer.parseInt( AppPropertiesService.getProperty( PROPERTY_LIMIT_MAX_SUSCRIBER ) ), getPlugin(  ) );
        String strNewsLetterCode;

        HashMap sendingModel = new HashMap(  );
        sendingModel.put( MARK_UNSUBSCRIBE, newsletter.getUnsubscribe(  ) );
        sendingModel.put( MARK_NEWSLETTER_ID, nNewsletterId );
        sendingModel.put( MARK_NEWSLETTER_CONTENT, sending.getHtml(  ) );
        sendingModel.put( SharedConstants.MARK_PORTAL_URL, strPortalUrl );
        sendingModel.put( SharedConstants.MARK_SUBSCRIBER_EMAIL, SharedConstants.MARK_SUBSCRIBER_EMAIL_EACH );

        HtmlTemplate templateNewsLetter = AppTemplateService.getTemplate( TEMPLATE_SEND_NEWSLETTER, getLocale(  ),
                sendingModel );

        Map mapAttachment = null;

        if ( isMhtmlActivated( getPlugin(  ) ) )
        {
            String strTemplate = templateNewsLetter.getHtml(  );
            strTemplate = StringUtil.substitute( strTemplate, strBaseUrl, Bookmarks.WEBAPP_PATH_FOR_LINKSERVICE );

            // we use absolute urls if there is no preproduction process
            boolean useAbsoluteUrl = strBaseUrl.equals( strPortalUrl );
            mapAttachment = MailUtil.getAttachmentList( strTemplate, strBaseUrl, useAbsoluteUrl );

            // all images, css urls are relative
            String strWebappPath = useAbsoluteUrl ? strPortalUrl : strBaseUrl.replaceFirst( "https?://[^/]+/", "/" );
            templateNewsLetter.substitute( Bookmarks.WEBAPP_PATH_FOR_LINKSERVICE, strWebappPath );
        }
        else
        {
            templateNewsLetter.substitute( Bookmarks.WEBAPP_PATH_FOR_LINKSERVICE, strPortalUrl + "/" );
        }

        for ( Subscriber subscriber : list )
        {
            HtmlTemplate t = new HtmlTemplate( templateNewsLetter );
            t.substitute( SharedConstants.MARK_SUBSCRIBER_EMAIL_EACH, subscriber.getEmail(  ) );

            strNewsLetterCode = t.getHtml(  );

            try
            {
                if ( mapAttachment == null )
                {
                    MailUtil.sendMessageHtml( strHost, subscriber.getEmail(  ), strPortal, strSenderEmail, strObject,
                        strNewsLetterCode );
                }
                else
                {
                    MailUtil.sendMessageHtml( strHost, subscriber.getEmail(  ), strPortal, strSenderEmail, strObject,
                        strNewsLetterCode, mapAttachment );
                }
            }
            catch ( MessagingException e )
            {
                AppLogService.debug( e );
            }
        }

        // updates the sending date
        newsletter.setDateLastSending( sending.getDate(  ) );
        NewsLetterHome.update( newsletter, getPlugin(  ) );

        return getHomeUrl( request );
    }

    /**
     * Processes the registration of a newsletter and loads the newsletter
     * management page
     *
     * @param request  The Http request
     * @return The jsp URL which displays the newsletters management page
     */
    public String doRegisterNewsLetter( HttpServletRequest request )
    {
        String strAction = request.getParameter( PARAMETER_ACTION );
        String strReturn = null;

        if ( !strAction.equals( I18nService.getLocalizedString( getPlugin(  ).getName(  ) + PROPERTY_CANCEL_ACTION,
                        getLocale(  ) ) ) )
        {
            int nNewsLetterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );

            NewsLetter newsLetter = NewsLetterHome.findByPrimaryKey( nNewsLetterId, getPlugin(  ) );
            newsLetter.setNewsLetterTemplateId( Integer.parseInt( request.getParameter( 
                        SharedConstants.PARAMETER_NEWSLETTER_TEMPLATE_ID ) ) );
            newsLetter.setDocumentTemplateId( Integer.parseInt( request.getParameter( PARAMETER_DOCUMENT_TEMPLATE_ID ) ) );

            String strBaseUrl = AppPathService.getBaseUrl( request );
            newsLetter.setHtml( doClean( request.getParameter( PARAMETER_HTML_CONTENT ), strBaseUrl ) );

            NewsLetterHome.update( newsLetter, getPlugin(  ) );

            if ( strAction.equals( I18nService.getLocalizedString( getPlugin(  ).getName(  ) +
                            PROPERTY_REGISTER_ACTION, getLocale(  ) ) ) )
            {
                // register action
                strReturn = getHomeUrl( request );
            }
            else if ( strAction.equals( I18nService.getLocalizedString( getPlugin(  ).getName(  ) +
                            PROPERTY_PREPARE_SENDING_ACTION, getLocale(  ) ) ) )
            {
                UrlItem url = new UrlItem( JSP_URL_DO_PREPARE_NEWSLETTER );
                url.addParameter( PARAMETER_NEWSLETTER_ID, nNewsLetterId );
                strReturn = url.getUrl(  );
            }
        }
        else
        {
            String strUrl = getHomeUrl( request );
            strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_CANCEL_COMPOSE, strUrl,
                    AdminMessage.TYPE_CONFIRMATION );
        }

        return strReturn;
    }

    /**
     * Builds the subscribers import page
     *
     * @param request The HTTP request
     * @return the html code for subscribers import page
     */
    public String getImportSubscribers( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_IMPORT );

        int nNewsletterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );
        HashMap model = new HashMap(  );
        model.put( MARK_NEWSLETTER_ID, nNewsletterId );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_IMPORT_SUBSCRIBERS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes the import of subscribers due to a csv file and loads the
     * subscribers management page
     *
     * @param request The Http request
     * @return The jsp URL which displays the subscribers management page
     */
    public String doImportSubscribers( HttpServletRequest request )
    {
        try
        {
            // create the multipart request
            MultipartHttpServletRequest multi = (MultipartHttpServletRequest) request;

            int nNewsLetterId = Integer.parseInt( multi.getParameter( PARAMETER_NEWSLETTER_ID ) );

            FileItem csvItem = multi.getFile( PARAMETER_SUBSCRIBERS_FILE );

            String strMultiFileName = UploadUtil.cleanFileName( csvItem.getName(  ) );

            if ( ( csvItem == null ) || strMultiFileName.equals( "" ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            // test the extension of the file must be 'csv'
            String strExtension = strMultiFileName.substring( strMultiFileName.length(  ) - 4,
                    strMultiFileName.length(  ) );

            if ( !strExtension.equals( CONSTANT_CSV_FILE_EXTENSION ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_CSV_FILE_EXTENSION, AdminMessage.TYPE_STOP );
            }

            Reader fileReader = new InputStreamReader( csvItem.getInputStream(  ) );
            CSVReader csvReader = new CSVReader( fileReader,
                    AppPropertiesService.getProperty( getPlugin(  ).getName(  ) + PROPERTY_IMPORT_DELIMITER ).charAt( 0 ) );

            List<String[]> tabUsers = csvReader.readAll(  );

            // the file is empty
            if ( tabUsers == null )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_CSV_FILE_EMPTY_OR_NOT_VALID_EMAILS,
                    AdminMessage.TYPE_STOP );
            }

            // Remove the old users and add the new users
            if ( tabUsers.size(  ) == 0 )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_CSV_FILE_EMPTY_OR_NOT_VALID_EMAILS,
                    AdminMessage.TYPE_ERROR );
            }
            else
            {
                // Add the new users
                for ( String[] strEmailTemp : tabUsers )
                {
                	int nColumnIndex = Integer.parseInt( AppPropertiesService.getProperty( getPlugin(  ).getName(  ) + CONSTANT_EMAIL_COLUMN_INDEX ) );
                    
                	if ( strEmailTemp.length < nColumnIndex )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_COLUMN_INDEX_NOT_EXIST,
                            AdminMessage.TYPE_ERROR );
                    }
                	
                	String strEmail = strEmailTemp[nColumnIndex];

                    //check if the email is not null and is valid
                    if ( ( strEmail != null ) && StringUtil.checkEmail( strEmail.trim(  ) ) )
                    {
                        // Checks if a subscriber with the same email address
                        // doesn't exist yet
                        Subscriber subscriber = SubscriberHome.findByEmail( strEmail, getPlugin(  ) );

                        if ( subscriber == null )
                        {
                            // The email doesn't exist, so create a new subcriber
                            subscriber = new Subscriber(  );
                            subscriber.setEmail( strEmail );
                            SubscriberHome.create( subscriber, getPlugin(  ) );
                        }

                        // the current date
                        Timestamp tToday = new java.sql.Timestamp( new java.util.Date(  ).getTime(  ) );

                        // adds a subscriber to the current newsletter
                        NewsLetterHome.addSubscriber( nNewsLetterId, subscriber.getId(  ), tToday, getPlugin(  ) );
                    }
                }
            }

            UrlItem urlItem = new UrlItem( JSP_URL_MANAGE_SUBSCRIBERS );
            urlItem.addParameter( PARAMETER_NEWSLETTER_ID, nNewsLetterId );

            return urlItem.getUrl(  );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e.getMessage(  ) );
        }
    }

    /**
     * Remove a known suscriber from a newsletter
     *
     * @param subscriber the subscriber to remove
     * @param nNewsletterId the newsletter id from which to remove the subscriber
     */
    private void removeSubscriberFromNewsletter( Subscriber subscriber, int nNewsletterId, Plugin plugin )
    {
        /* checks newsletter exist in database */
        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( nNewsletterId, plugin );

        if ( ( subscriber != null ) && ( newsletter != null ) )
        {
            int nSubscriberId = subscriber.getId(  );

            /* checks if the subscriber identified is registered */
            if ( NewsLetterHome.findRegistration( nNewsletterId, nSubscriberId, plugin ) )
            {
                /* unregistration */
                NewsLetterHome.removeSubscriber( nNewsletterId, nSubscriberId, plugin );
            }

            /*
             * if the subscriber is not registered to an other newsletter, his
             * account is deleted
             */
            if ( SubscriberHome.findNewsLetters( nSubscriberId, plugin ) == 0 )
            {
                SubscriberHome.remove( nSubscriberId, plugin );
            }
        }
    }

    /**
     * Generate the html code for the documents corresponding
     * to the documents associated with the newsletter and to the date of the last
     * sending of the newsletter
     *
     * @param nNewsLetterId the newsletter for which the document list should be generated
     * @param nTemplateId the document id to use
     * @return the html code for the document list
     */
    private String generateDocumentsList( int nNewsLetterId, int nTemplateId )
    {
        NewsLetter newsletter = NewsLetterHome.findByPrimaryKey( nNewsLetterId, getPlugin(  ) );
        int[] arrayThemeIds = NewsLetterHome.findNewsletterThemesIds( nNewsLetterId, getPlugin(  ) );
        String strPathDocumentTemplatePath = NewsletterUtils.getHtmlTemplatePath( nTemplateId, getPlugin(  ) );
        StringBuffer sbDocumentLists = new StringBuffer(  );

        for ( int i = 0; i < arrayThemeIds.length; i++ )
        {
            Collection<Document> list = NewsLetterHome.findDocumentsByDateAndList( arrayThemeIds[i],
                    newsletter.getDateLastSending(  ) );
            List<String> listDocuments = new ArrayList(  );
            String strPortalUrl = AppPropertiesService.getProperty( SharedConstants.PROPERTY_PROD_URL );
            HashMap model = new HashMap(  );
            model.put( SharedConstants.MARK_DOCUMENT_LIST, list );

            ReferenceList hostKeysList = new ReferenceList(  );

            try
            {
                if ( AppPathService.getAvailableVirtualHosts(  ) != null )
                {
                    hostKeysList = AppPathService.getAvailableVirtualHosts(  );

                    ReferenceList listHosts = new ReferenceList(  );

                    for ( int j = 0; j < hostKeysList.size(  ); j++ )
                    {
                        listHosts.addItem( hostKeysList.get( j ).getName(  ),
                            AppPropertiesService.getProperty( PROPERTY_VIRTUAL_HOST +
                                hostKeysList.get( j ).getCode(  ) + SUFFIX_BASE_URL ) );
                    }

                    model.put( MARK_VIRTUAL_HOSTS, listHosts );
                }
            }
            catch ( NullPointerException e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }

            model.put( MARK_DOCUMENT_LIST_DESCRIPTION, NewsLetterHome.findDocumentListDescription( arrayThemeIds[i] ) );

            model.put( SharedConstants.MARK_PORTAL_URL, strPortalUrl );
            model.put( SharedConstants.MARK_DOCUMENT_PORTLET_ID, arrayThemeIds[i] );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DOCUMENT_LISTS, getLocale(  ), model );

            sbDocumentLists.append( template.getHtml(  ) );
        }

        return sbDocumentLists.toString(  );
    }

    /**
     * Generate the html code of the newsletter according to the document and
     * newsletter templates
     *
     * @param nNewsLetterId the newsletter id
     * @param nTemplateNewsLetterId  the newsletter template id
     * @param nTemplateDocumentId the document template id
     * @return the html code for the newsletter content
     */
    private String generateNewsletterHtmlCode( int nNewsLetterId, int nTemplateNewsLetterId, int nTemplateDocumentId )
    {
        String strTemplatePath = NewsletterUtils.getHtmlTemplatePath( nTemplateNewsLetterId, getPlugin(  ) );

        HashMap model = new HashMap(  );
        model.put( SharedConstants.MARK_DOCUMENT_LIST, generateDocumentsList( nNewsLetterId, nTemplateDocumentId ) );

        HtmlTemplate templateNewsLetter = AppTemplateService.getTemplate( strTemplatePath, getLocale(  ), model );

        return templateNewsLetter.getHtml(  );
    }

    // //////////////////////////////////////////////////////////////////////////////////
    // Private Implementation

    /**
     * To translate the absolute url's in SEMI-relativre url's of the
     * html_content ( use before insertion in db)
     *
     * @param strContent The html code
     * @param strBaseUrl The base url
     * @return The clean code
     */
    private String doClean( String strContent, String strBaseUrl )
    {
        String strNewContent = strContent;
        strNewContent = StringUtil.substitute( strNewContent, Bookmarks.WEBAPP_PATH_FOR_LINKSERVICE, strBaseUrl );

        return strNewContent;
    }

    /**
     * Determine if we must send mail in MHTML
     *
     * @param plugin  The Plugin used to retrieve plugin's name
     * @return true whether MHTML is needed
     */
    private boolean isMhtmlActivated( Plugin plugin )
    {
        String strProperty = AppPropertiesService.getProperty( getPlugin(  ).getName(  ) +
                SharedConstants.PROPERTY_MAIL_MULTIPART );

        return ( strProperty != null ) && Boolean.valueOf( strProperty ).booleanValue(  );
    }

    /**
     * Tests whether all the e-mails represented by a string are valid
     * @param strRecipientLists
     * @return The last wrong invalid e-mail in the list or an empty String if all e-mails are valid
     */
    private String isWrongEmail( String strRecipientLists )
    {
        String strWrongEmail = "";

        String strDelimiter = AppPropertiesService.getProperty( getPlugin(  ).getName(  ) + PROPERTY_EMAIL_TESTER_DELIMITER );

        String[] strEmails = strRecipientLists.split( strDelimiter );

        for ( int j = 0; j < strEmails.length; j++ )
        {
            if ( !StringUtil.checkEmail( strEmails[j] ) )
            {
                strWrongEmail = strEmails[j];
            }
        }

        return strWrongEmail;
    }

    /**
     * Takes a list of recipients in a form of a String and converts it into a list
     *  @param strRecipients
     *
     */
    private List<String> convertToList( String strRecipients )
    {
        List listRecipients = new ArrayList(  );

        if ( ( strRecipients != null ) && !strRecipients.equals( "" ) )
        {
            String strDelimiter = AppPropertiesService.getProperty( getPlugin(  ).getName(  ) +
            		PROPERTY_EMAIL_TESTER_DELIMITER );

            String[] strEmails = strRecipients.split( strDelimiter );

            for ( int j = 0; j < strEmails.length; j++ )
            {
                if ( StringUtil.checkEmail( strEmails[j] ) )
                {
                    listRecipients.add( strEmails[j] );
                }
            }
        }

        return listRecipients;
    }
}
