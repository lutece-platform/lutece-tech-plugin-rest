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

import fr.paris.lutece.plugins.newsletter.business.NewsLetter;
import fr.paris.lutece.plugins.newsletter.business.NewsLetterHome;
import fr.paris.lutece.plugins.newsletter.business.SendingNewsLetter;
import fr.paris.lutece.plugins.newsletter.business.SendingNewsLetterHome;
import fr.paris.lutece.plugins.newsletter.business.Subscriber;
import fr.paris.lutece.plugins.newsletter.business.SubscriberHome;
import fr.paris.lutece.plugins.newsletter.business.portlet.NewsLetterSubscriptionPortlet;
import fr.paris.lutece.plugins.newsletter.business.portlet.NewsLetterSubscriptionPortletHome;

//import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.template.AppTemplateService;

//import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;

import java.sql.Timestamp;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * This XPage handles the newsletter subscription, and the newsletter archives.
 */
public class NewsLetterApp implements XPageApplication
{
    // Templates used to generate the HTML code
    private static final String TEMPLATE_XPAGE_NEWSLETTER = "skin/plugins/newsletter/page_newsletter.html";
    private static final String TEMPLATE_XPAGE_NEWSLETTER_ARCHIVE = "skin/plugins/newsletter/page_newsletter_archive.html";
    private static final String TEMPLATE_CONFIRM_UNREGISTRATION = "skin/plugins/newsletter/confirm_unregistration.html";

    // Bookmarks to replace dynamic values in the templates
    private static final String MARK_NEWSLETTER_ALERT = "alert";
    private static final String MARK_NEWSLETTER_EMAIL_SUBSCRIBER = "email";

    // Request parameters
    private static final String PARAMETER_ACTION = "action";
    private static final String PARAMETER_SENDING_ID = "sending_id";
    private static final String PARAMETER_PORTLET_ID = "portlet_id";
    private static final String PARAMETER_NEWSLETTER = "newsletter";
    private static final String PARAMETER_SUBSCRIPTION = "subscription";
    private static final String PARAMETER_EMAIL = "email";
    private static final String PARAMETER_PAGE_ID = "page_id";
    private static final String PARAMETER_EMAIL_ERROR = "email-error";
    private static final String PARAMETER_NO_NEWSLETTER_CHOSEN = "nochoice-error";
    private static final String PARAMETER_NEWSLETTER_ID = "newsletter_id";
    private static final String PARAMETER_SUBSCRIBER_EMAIL = "subscriber_email";

    // Possible values for the 'action' parameter
    private static final String ACTION_REGISTER = "register";
    private static final String ACTION_SHOW_ARCHIVE = "show_archive";

    //    private static final String ACTION_INVALID_MAIL = "invalid_email";

    // Possible values for the 'subscription' parameter
    private static final String SUBSCRIPTION_DONE = "done";
    private static final String SUBSCRIPTION_ERROR = "error";
    private static final String SUBSCRIPTION_INVALID_MAIL = "invalid-mail";
    private static final String SUBSCRIPTION_NO_NEWSLETTER_CHOSEN = "newsletter-nochoice";

    // URL of the portal JSP
    private static final String JSP_URL_PORTAL = "../../Portal.jsp";
    private static final String JSP_URL_CONFIRM_UNREGISTRATION = "ConfirmUnregistration.jsp";

    // Properties in the configuration files
    private static final String PROPERTY_PATHLABEL = "newsletter.pagePathLabel";
    private static final String PROPERTY_PAGETITLE = "newsletter.pageTitle";
    private static final String PROPERTY_MSG_INVALID_MAIL = "newsletter.message.error.invalid.mail";
    private static final String PROPERTY_MSG_REGISTRATION_OK = "newsletter.message.alert.newsletter.registration_ok.text";
    private static final String PROPERTY_MSG_MANDATORY = "newsletter.message.error.mandatory.text";
    private static final String PROPERTY_MSG_WRONG_ACTION = "newsletter.message.error.newsletter.wrong_action";
    private static final String PROPERTY_MSG_WRONG_SENDING_ID = "newsletter.message.error.newsletter.wrong_sending_id";
    private static final String MARK_NEWSLETTERS_LIST = "newsletters_list";
    private static final String MARK_PLUGIN = "plugin";
    private static final String MARK_SENDING = "sending";

    /**
     * Creates a new NewsLetterPage object
     */
    public NewsLetterApp(  )
    {
    }

    /**
     * Returns the Newsletter XPage content depending on the request
     * parameters and the current mode.
     *
     * @param request The HTTP request.
     * @param nMode The current mode.
     * @param plugin The Plugin
     * @return The page content.
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        String strAction = request.getParameter( PARAMETER_ACTION );
        XPage resultPage = null;

        if ( ( strAction == null ) || strAction.equals( "" ) || strAction.equals( ACTION_REGISTER ) )
        {
            resultPage = getRegisterPage( request, nMode, plugin );
        }
        else if ( strAction.equals( ACTION_SHOW_ARCHIVE ) )
        {
            resultPage = getShowArchivePage( request, nMode, plugin );
        }
        else
        {
            resultPage = getErrorPage( I18nService.getLocalizedString( PROPERTY_MSG_WRONG_ACTION, request.getLocale(  ) ),
                    request );
        }

        return resultPage;
    }

    /**
     * Returns the XPage to handle user registration.
     *
     * @param request The HTTP request
     * @param nMode The current mode
     * @param plugin The Plugin
     * @return The page content
     */
    private static XPage getRegisterPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        XPage page = new XPage(  );
        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_PATHLABEL, request.getLocale(  ) ) );
        page.setTitle( I18nService.getLocalizedString( PROPERTY_PAGETITLE, request.getLocale(  ) ) );

        String strSubscription = request.getParameter( PARAMETER_SUBSCRIPTION );
        String strEmail = request.getParameter( PARAMETER_EMAIL );

        if ( strEmail != null )
        {
            if ( !StringUtil.checkEmail( strEmail ) )
            {
                strSubscription = SUBSCRIPTION_INVALID_MAIL;
            }
        }

        String strAlert = "";

        HashMap model = new HashMap(  );

        if ( ( strSubscription != null ) && strSubscription.equals( SUBSCRIPTION_INVALID_MAIL ) )
        {
            strAlert = I18nService.getLocalizedString( PROPERTY_MSG_INVALID_MAIL, request.getLocale(  ) );
        }

        if ( ( strSubscription != null ) && strSubscription.equals( SUBSCRIPTION_DONE ) )
        {
            strAlert = I18nService.getLocalizedString( PROPERTY_MSG_REGISTRATION_OK, request.getLocale(  ) );
        }

        if ( ( strSubscription != null ) && strSubscription.equals( SUBSCRIPTION_ERROR ) )
        {
            strAlert = I18nService.getLocalizedString( PROPERTY_MSG_MANDATORY, request.getLocale(  ) );
        }

        model.put( MARK_NEWSLETTER_EMAIL_SUBSCRIBER, ( strEmail == null ) ? "" : strEmail );

        Collection list = NewsLetterHome.findAll( plugin );
        model.put( MARK_NEWSLETTERS_LIST, list );
        model.put( MARK_NEWSLETTER_ALERT, strAlert );
        model.put( MARK_PLUGIN, plugin );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_NEWSLETTER, request.getLocale(  ), model );
        page.setContent( template.getHtml(  ) );

        return page;
    }

    /**
     * Returns the Newsletter archive XPage content depending on the request
     * parameters and the current mode.
     *
     * @param request The HTTP request.
     * @param nMode The current mode.
     * @param plugin The Plugin
     *
     * @return The page content.
     */
    private static XPage getShowArchivePage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        // Create a new XPage according to the properties declared in
        // newsletter.properties
        XPage page = new XPage(  );
        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_PATHLABEL, request.getLocale(  ) ) );
        page.setTitle( I18nService.getLocalizedString( PROPERTY_PAGETITLE, request.getLocale(  ) ) );

        // Retrieve the identifier of the sending from the request
        String strSendingId = request.getParameter( PARAMETER_SENDING_ID );
        int nSendingId = Integer.parseInt( strSendingId );

        // Retrieve the sending's data from the database
        SendingNewsLetter sending = SendingNewsLetterHome.findByPrimaryKey( nSendingId, plugin );

        if ( sending == null )
        {
            return getErrorPage( I18nService.getLocalizedString( PROPERTY_MSG_WRONG_SENDING_ID, request.getLocale(  ) ),
                request );
        }

        // Load the template and fill in with the sending's data
        HashMap model = new HashMap(  );
        model.put( MARK_SENDING, sending );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_NEWSLETTER_ARCHIVE,
                request.getLocale(  ), model );

        // Put the HTML in the XPage
        page.setContent( template.getHtml(  ) );

        return page;
    }

    /**
     * Returns an error page for the XPage.
     *
     * @param strMessage the error message to be displayed on the page
     * @param request the http request
     * @return The page content
     */
    private static XPage getErrorPage( String strMessage, HttpServletRequest request )
    {
        XPage page = new XPage(  );
        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_PATHLABEL, request.getLocale(  ) ) );
        page.setTitle( I18nService.getLocalizedString( PROPERTY_PAGETITLE, request.getLocale(  ) ) );
        page.setContent( strMessage );

        return page;
    }

    /**
     * Returns an error page for the XPage.
     *
     * @param strMessage the error message to be displayed on the page
     * @param request the http request
     * @return The page content
     */

    /* private XPage getInvalidMailPage( String strMessage, HttpServletRequest request )
     {
         XPage page = new XPage(  );
         page.setPathLabel( I18nService.getLocalizedString( PROPERTY_PATHLABEL, request.getLocale(  ) ) );
         page.setTitle( I18nService.getLocalizedString( PROPERTY_PAGETITLE, request.getLocale(  ) ) );
         page.setContent( strMessage );
    
         return page;
     }*/

    /**
     * Process the subscription of the subscriber whose email is stored in the
     * http request
     *
     * @param request the http request
     * @return the jsp url of the
     */
    public String doSubscription( HttpServletRequest request )
    {
        String strEmail = request.getParameter( PARAMETER_EMAIL );
        String strPluginName = request.getParameter( SharedConstants.PARAMETER_PLUGIN_NAME );
        boolean bEmailValid = StringUtil.checkEmail( strEmail );

        if ( !bEmailValid )
        {
            return JSP_URL_PORTAL + "?page=" + strPluginName + "&subscription=error&email=" + strEmail;
        }

        Plugin plugin = PluginService.getPlugin( strPluginName );

        //Checks if a subscriber with the same email address doesn't exist yet
        Subscriber subscriber = SubscriberHome.findByEmail( strEmail, plugin );

        if ( subscriber == null )
        {
            // The email doesn't exist, so create a new subcriber
            subscriber = new Subscriber(  );
            subscriber.setEmail( strEmail );
            SubscriberHome.create( subscriber, plugin );
        }

        Collection<NewsLetter> list = NewsLetterHome.findAll( plugin );
        int nCountSubscription = 0;

        for ( NewsLetter newsLetter : list )
        {
            String strParameter = PARAMETER_NEWSLETTER + newsLetter.getId(  );
            String strValue = request.getParameter( strParameter );
            Timestamp tToday = new java.sql.Timestamp( new java.util.Date(  ).getTime(  ) ); // the current date

            if ( ( strValue != null ) && ( strValue.equals( "on" ) ) )
            {
                NewsLetterHome.addSubscriber( newsLetter.getId(  ), subscriber.getId(  ), tToday, plugin );
                nCountSubscription++;
            }
        }

        if ( nCountSubscription == 0 )
        {
            return JSP_URL_PORTAL + "?page=" + strPluginName + "&subscription=error&email=" + strEmail;
        }
        else
        {
            return JSP_URL_PORTAL + "?page=" + strPluginName + "&subscription=done";
        }
    }

    /**
     * Confirm to the subscriber that his request was recorded
     *
     * @return the message form
     */
    public String confirmUnregistration( HttpServletRequest request )
    {
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONFIRM_UNREGISTRATION, request.getLocale(  ) );

        return template.getHtml(  );
    }

    /**
     * This method is call by the JSP named DoSubscribePortletNewsletter.jsp
     * @param request The HTTP request
     * @return The URL to forward depending of the result of the login.
     */
    public String doSubscribeFromPortlet( HttpServletRequest request )
    {
        String strError = SUBSCRIPTION_INVALID_MAIL;
        String strNoChoice = "";
        String strEmail = request.getParameter( PARAMETER_EMAIL );
        boolean bEmailValid = StringUtil.checkEmail( strEmail );

        if ( bEmailValid )
        {
            strError = "";

            String strPluginName = request.getParameter( SharedConstants.PARAMETER_PLUGIN_NAME );
            Plugin plugin = PluginService.getPlugin( strPluginName );

            //Checks if a subscriber with the same email address doesn't exist yet
            Subscriber subscriber = SubscriberHome.findByEmail( strEmail, plugin );

            if ( subscriber == null )
            {
                // The email doesn't exist, so create a new subcriber
                subscriber = new Subscriber(  );
                subscriber.setEmail( strEmail );
                SubscriberHome.create( subscriber, plugin );
            }

            Collection<NewsLetter> list = NewsLetterHome.findAll( plugin );
            int nCountSubscription = 0;

            for ( NewsLetter newsLetter : list )
            {
                String strParameter = PARAMETER_NEWSLETTER + newsLetter.getId(  );
                String strValue = request.getParameter( strParameter );
                Timestamp tToday = new java.sql.Timestamp( new java.util.Date(  ).getTime(  ) ); // the current date

                if ( ( strValue != null ) && ( strValue.equals( "on" ) ) )
                {
                    NewsLetterHome.addSubscriber( newsLetter.getId(  ), subscriber.getId(  ), tToday, plugin );
                    nCountSubscription++;
                }
            }

            if ( nCountSubscription == 0 )
            {
                strNoChoice = SUBSCRIPTION_NO_NEWSLETTER_CHOSEN;
            }
        }

        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );

        NewsLetterSubscriptionPortlet portlet = (NewsLetterSubscriptionPortlet) NewsLetterSubscriptionPortletHome.getInstance(  )
                                                                                                                 .findByPrimaryKey( nPortletId );
        int nPageId = portlet.getPageId(  );
        //   String strPortalUrl = AppPropertiesService.getProperty( SharedConstants.PROPERTY_PORTAL_URL );
        //      List listErrors = new ArrayList(  );
        PortalService.resetCache(  );

        UrlItem url = new UrlItem( JSP_URL_PORTAL );
        url.addParameter( PARAMETER_PAGE_ID, Integer.toString( nPageId ) );
        url.addParameter( PARAMETER_EMAIL_ERROR, strError );
        url.addParameter( PARAMETER_NO_NEWSLETTER_CHOSEN, strNoChoice );

        return url.getUrl(  );
    }

    /**
     * Processes the unregistration of a subscriber for a newsletter
     *
     * @param request     The Http request
     * @return the jsp URL to display the form to confirm unregistration
     */
    public String doUnregistration( HttpServletRequest request )
    {
        String strEmail = request.getParameter( PARAMETER_SUBSCRIBER_EMAIL );
        int nNewsletterId = Integer.parseInt( request.getParameter( PARAMETER_NEWSLETTER_ID ) );
        removeEMailFromNewsletter( strEmail, nNewsletterId );

        UrlItem url = new UrlItem( JSP_URL_CONFIRM_UNREGISTRATION );
        url.addParameter( SharedConstants.PARAMETER_PLUGIN_NAME, SharedConstants.PLUGIN_NAME );

        return url.getUrl(  );
    }

    /**
     * Remove a subscriber from a newsletter
     *
     * @param strEmail the email of the subscriber
     * @param nNewsletterId the subscribed newsletter
     */
    private void removeEMailFromNewsletter( String strEmail, int nNewsletterId )
    {
        /* finds the id of the subscriber */
        Plugin plugin = PluginService.getPlugin( SharedConstants.PLUGIN_NAME );
        Subscriber subscriber = SubscriberHome.findByEmail( strEmail, plugin );

        if ( subscriber != null )
        {
            removeSubscriberFromNewsletter( subscriber, nNewsletterId, plugin );
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
}
