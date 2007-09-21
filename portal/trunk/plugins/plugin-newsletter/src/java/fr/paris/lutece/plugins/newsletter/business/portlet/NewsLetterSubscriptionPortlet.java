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
package fr.paris.lutece.plugins.newsletter.business.portlet;

import fr.paris.lutece.plugins.newsletter.business.NewsLetter;
import fr.paris.lutece.plugins.newsletter.business.NewsLetterHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents the business object NewsLetterSubscriptionPortlet.
 */
public class NewsLetterSubscriptionPortlet extends Portlet
{
    // The names of the XML tags
    private static final String TAG_NEWSLETTER_SUBSCRIPTION_LIST = "newsletter-subscription-list";
    private static final String TAG_NEWSLETTER_SUBSCRIPTION = "newsletter-subscription";
    private static final String TAG_NEWSLETTER_SUBSCRIPTION_ID = "newsletter-subscription-id";
    private static final String TAG_NEWSLETTER_SUBSCRIPTION_EMAIL = "newsletter-subscription-email";
    private static final String TAG_NEWSLETTER_SUBSCRIPTION_DATE = "newsletter-subscription-date";
    private static final String TAG_NEWSLETTER_SUBSCRIPTION_DESC = "newsletter-subscription-subject";
    private static final String TAG_NEWSLETTER_SUBSCRIPTION_BUTTON = "newsletter-subscription-button";
    private static final String TAG_NEWSLETTER_EMAIL_ERROR = "newsletter-email-error";
    private static final String TAG_NEWSLETTER_NO_CHOICE_ERROR = "subscription-nochoice-error";
    private static final String PROPERTY_LABEL_MAIL = "newsletter.portlet.mail.label";
    private static final String PROPERTY_LABEL_BUTTON = "newsletter.portlet.button.label";
    private static final String PROPERTY_ERROR_INVALID_MAIL = "newsletter.message.error.newsletter.invalid.mail";
    private static final String PROPERTY_ERROR_NO_CHOICE_ERROR = "newsletter.message.error.newsletter.nochoice.error";
    private static final String PARAMETER_EMAIL_ERROR = "email-error";
    private static final String PARAMETER_NO_NEWSLETTER_CHOSEN = "nochoice-error";

    // Comparator for sorting - date descendant order
    private static final Comparator COMPARATOR_DATE_DESC = new Comparator(  )
        {
            public int compare( Object obj1, Object obj2 )
            {
                NewsLetter sending1 = (NewsLetter) obj1;
                NewsLetter sending2 = (NewsLetter) obj2;

                return sending2.getDateLastSending(  ).compareTo( sending1.getDateLastSending(  ) );
            }
        };

    private Plugin _plugin;

    /**
    * Creates a new NewsLetterArchivePortlet object.
    */
    public NewsLetterSubscriptionPortlet(  )
    {
    }

    /**
     * Sets the name of the plugin associated with this portlet.
     *
     * @param strPluginName The plugin name.
     */
    public void setPluginName( String strPluginName )
    {
        super.setPluginName( strPluginName );

        // We override this method in order to initialize the plugin instance :
        this._plugin = PluginService.getPlugin( strPluginName );
    }

    /**
     * Returns the Xml code of the Subscriber portlet with XML heading
     *
     * @param request The HTTP servlet request
     * @return the Xml code of the Subscription portlet
     */
    public String getXmlDocument( HttpServletRequest request )
    {
        return XmlUtil.getXmlHeader(  ) + getXml( request );
    }

    /**
     * Returns the Xml code of the Subscuption portlet without XML heading
     *
     * @param request The HTTP servlet request
     * @return the Xml code of the Subscription portlet content
     */
    public String getXml( HttpServletRequest request )
    {
        //        List listErrors = new ArrayList(  );
        String strMailError = null;
        String strNoChoiceError = null;

        if ( request != null )
        {
            strMailError = request.getParameter( PARAMETER_EMAIL_ERROR );
            strNoChoiceError = request.getParameter( PARAMETER_NO_NEWSLETTER_CHOSEN );
        }

        if ( ( strMailError == null ) || ( strMailError.trim(  ).equals( "" ) ) )
        {
            strMailError = "";
        }

        if ( ( strNoChoiceError == null ) || ( strNoChoiceError.trim(  ).equals( "" ) ) )
        {
            strNoChoiceError = "";
        }

        StringBuffer strXml = new StringBuffer(  );

        XmlUtil.beginElement( strXml, TAG_NEWSLETTER_SUBSCRIPTION_LIST );

        // We need the data of the sendings associated with this portlet.
        // However, the association table and the sendings table are on two
        // different datasources (AppConnectionService and
        // PluginConnectionService respectively). Therefore we can't roll
        // everything into a single SQL request.
        // Hence the manual join and ordering below.
        // Get the ids of the newsletter sendings to display in the portlet.
        Set sendingIds = NewsLetterSubscriptionPortletHome.findSelectedNewsletters( this.getId(  ) );
        Iterator iterIds = sendingIds.iterator(  );

        // Read all the sendings from the plugin-specific datasource
        List sendings = new ArrayList(  );

        while ( iterIds.hasNext(  ) )
        {
            int sendingId = ( (Integer) iterIds.next(  ) ).intValue(  );

            // Read the content of the sending on the PluginConnectionService :
            NewsLetter sending = NewsLetterHome.findByPrimaryKey( sendingId, _plugin );

            sendings.add( sending );
        }

        // Then order the sendings by date
        Collections.sort( sendings, COMPARATOR_DATE_DESC );

        // Then generate the XML code
        Iterator iterSendings = sendings.iterator(  );

        while ( iterSendings.hasNext(  ) )
        {
            NewsLetter sending = (NewsLetter) iterSendings.next(  );

            // Generate the XML code for the sending :
            XmlUtil.beginElement( strXml, TAG_NEWSLETTER_SUBSCRIPTION );
            XmlUtil.addElement( strXml, TAG_NEWSLETTER_SUBSCRIPTION_ID, sending.getId(  ) );
            XmlUtil.addElement( strXml, TAG_NEWSLETTER_SUBSCRIPTION_DESC, sending.getName(  ) );
            XmlUtil.addElement( strXml, TAG_NEWSLETTER_SUBSCRIPTION_DATE,
                DateUtil.getDateString( sending.getDateLastSending(  ) ) );
            XmlUtil.endElement( strXml, TAG_NEWSLETTER_SUBSCRIPTION );
        }

        XmlUtil.endElement( strXml, TAG_NEWSLETTER_SUBSCRIPTION_LIST );

        if ( !strMailError.equals( "" ) )
        {
            XmlUtil.addElement( strXml, TAG_NEWSLETTER_EMAIL_ERROR,
                I18nService.getLocalizedString( PROPERTY_ERROR_INVALID_MAIL, request.getLocale(  ) ) );
        }

        if ( !strNoChoiceError.equals( "" ) )
        {
            XmlUtil.addElement( strXml, TAG_NEWSLETTER_NO_CHOICE_ERROR,
                I18nService.getLocalizedString( PROPERTY_ERROR_NO_CHOICE_ERROR, request.getLocale(  ) ) );
        }

        XmlUtil.addElement( strXml, TAG_NEWSLETTER_SUBSCRIPTION_BUTTON,
            I18nService.getLocalizedString( PROPERTY_LABEL_BUTTON, request.getLocale(  ) ) );
        XmlUtil.addElement( strXml, TAG_NEWSLETTER_SUBSCRIPTION_EMAIL,
            I18nService.getLocalizedString( PROPERTY_LABEL_MAIL, request.getLocale(  ) ) );
        XmlUtil.addElement( strXml, TAG_NEWSLETTER_SUBSCRIPTION_BUTTON,
            I18nService.getLocalizedString( PROPERTY_LABEL_BUTTON, request.getLocale(  ) ) );

        String str = addPortletTags( strXml );

        return str;
    }

    /**
     * Updates the current instance of the HtmlPortlet object
     */
    public void update(  )
    {
        NewsLetterSubscriptionPortletHome.getInstance(  ).update( this );
    }

    /**
     * Removes the current instance of the HtmlPortlet object
     */
    public void remove(  )
    {
        NewsLetterSubscriptionPortletHome.getInstance(  ).remove( this );
    }
}
