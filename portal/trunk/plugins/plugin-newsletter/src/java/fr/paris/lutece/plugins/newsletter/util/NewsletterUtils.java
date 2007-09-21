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
package fr.paris.lutece.plugins.newsletter.util;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.newsletter.business.NewsLetterTemplate;
import fr.paris.lutece.plugins.newsletter.business.NewsLetterTemplateHome;
import fr.paris.lutece.plugins.newsletter.web.SharedConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;

import java.util.HashMap;
import java.util.Locale;


/**
 * This classe provides utility methods for newsletters.
 */
public final class NewsletterUtils
{
    // private static final String PARAMETER_DOCUMENT_ID = "document_id";
    //private static final String PROPERTY_LINKS_URL_DEFAUT_KEY_NAME = ".links.url.defaultKeyName";

    // private static final String BOOKMARK_ALTERNATE_URL = "@alternate_url@";
    //    private static final String BOOKMARK_URLS_LIST = "@urls_list@";
    //    private static final String BOOKMARK_VIRTUAL_HOST_DESCRIPTION = "@virtual_host_description@";
    private static final String MARK_VIRTUAL_HOSTS = "virtual_hosts";
    private static final String PROPERTY_VIRTUAL_HOST = "virtualHost.";
    private static final String SUFFIX_BASE_URL = ".baseUrl";

    //    private static final String PROPERTY_LINKS_URL_DEFAUT_KEY_NAME = ".links.url.defaultKeyName";
    //    private static final String SUFFIX_BASE_URL = ".baseUrl";
    /**
     * Retrieve the html template for the given template id
     * @param nTemplateId the id of the template to retrieve
     * @param plugin the plugin
     * @return the html template to use
     */
    public static String getHtmlTemplatePath( int nTemplateId, Plugin plugin )
    {
        NewsLetterTemplate newsletterTemplate = NewsLetterTemplateHome.findByPrimaryKey( nTemplateId, plugin );
        String strTemplatePathName = AppPropertiesService.getProperty( plugin.getName(  ) +
                SharedConstants.PROPERTY_PATH_FILE_NEWSLETTER_TEMPLATE );
        strTemplatePathName += "/";
        strTemplatePathName += newsletterTemplate.getFileName(  );

        return strTemplatePathName;
    }

    /**
     * Cleans a string in order to make it usable in a javascript script
     * @param strIn the string to clean
     * @return the javascript escaped String
     */
    public static String convertForJavascript( String strIn )
    {
        // Convert problem characters to JavaScript Escaped values
        if ( strIn == null )
        {
            return "";
        }

        String strOut = strIn;
        strOut = StringUtil.substitute( strOut, "\\\\", "\\" ); // replace backslash with \\
        strOut = StringUtil.substitute( strOut, "\\\'", "'" ); // replace an single quote with \'
        strOut = StringUtil.substitute( strOut, "\\\"", "\"" ); // replace a double quote with \"
        strOut = StringUtil.substitute( strOut, "\\r", "\r\n" ); // replace CR with \r;
        strOut = StringUtil.substitute( strOut, "\\n", "\n" ); // replace LF with \n;

        return strOut;
    }

    /**
     * Fills a given document template with the document data
     * @param t the html template
     * @param document the object gathering the document data
     * @param nPortletId the portlet id
     * @param plugin the plugin needed to retrieve properties
     * @param locale the locale used to build the template
     * @return the html code corresponding to the document data
     */
    public static String fillTemplateWithDocumentInfos( String strTemplatePath, Document document, int nPortletId,
        Plugin plugin, Locale locale )
    {
        HashMap model = new HashMap(  );
        model.put( SharedConstants.MARK_DOCUMENT, document );

        try
        {
            if ( AppPathService.getAvailableVirtualHosts(  ) != null )
            {
                ReferenceList hostKeysList = AppPathService.getAvailableVirtualHosts(  );
                ReferenceList list = new ReferenceList(  );

                for ( int i = 0; i < hostKeysList.size(  ); i++ )
                {
                    list.addItem( hostKeysList.get( i ).getName(  ),
                        AppPropertiesService.getProperty( PROPERTY_VIRTUAL_HOST + hostKeysList.get( i ).getCode(  ) +
                            SUFFIX_BASE_URL ) );
                }

                model.put( MARK_VIRTUAL_HOSTS, list );
            }
        }
        catch ( NullPointerException e )
        {
        }

        String strPortalUrl = AppPropertiesService.getProperty( SharedConstants.PROPERTY_PROD_URL );
        model.put( SharedConstants.MARK_PORTAL_URL, strPortalUrl );
        model.put( SharedConstants.MARK_DOCUMENT_THUMBNAIL, document.getThumbnail(  ) );
        model.put( SharedConstants.MARK_DOCUMENT_PORTLET_ID, nPortletId );

        HtmlTemplate template = AppTemplateService.getTemplate( strTemplatePath, locale, model );

        return template.getHtml(  );
    }
}
