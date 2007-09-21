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
package fr.paris.lutece.plugins.xmlpage.web;

import fr.paris.lutece.plugins.xmlpage.service.XmlPageElement;
import fr.paris.lutece.plugins.xmlpage.service.XmlPageService;
import fr.paris.lutece.plugins.xmlpage.util.XmlPageContentUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import javax.servlet.http.HttpServletRequest;


/**
 * This class manages Xml Page.
 */
public class XmlPageApp implements XPageApplication
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String TEMPLATE_PAGE_XMLPAGE = "skin/plugins/xmlpage/page_xmlpage.html";
    private static final String PARAMETER_XMLPAGE_NAME = "xmlpage";
    private static final String PARAMETER_XMLPAGE_STYLE = "style";
    private static final String PROPERTY_ERROR_PAGE_TITLE = "xmlpage.error.pageTitle";
    private static final String PROPERTY_ERROR_PAGE_PATH = "xmlpage.error.pagePathLabel";
    private static final String PROPERTY_ERROR_MISSING_PARAMETERS = "xmlpage.error.message.missing.parameters";
    private static final String PROPERTY_ERROR_PAGE_NOT_FOUND = "xmlpage.error.message.page.not.found";
    private static final String PROPERTY_ERROR_UNKNOWN = "xmlpage.error.message.unknown";
    private static final String BOOKMARK_PAGE_CONTENT = "@page_content@";
    private static final String EMPTY_STRING = "";

    /**
     * Returns the page.
     * It is composed of the title, page path and content.
     * The content is the result of the xml/xsl transformation.
     * Xml data is retrieved from the "xmlpage" parameter.
     * Xsl data is retrieved from the "style" parameter.
     *
     * @param request The http request
     * @param nMode The current mode
     * @param plugin The plugin object
     * @return the Content of the page Contact
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        XPage page = new XPage(  );

        String strXmlPageName = request.getParameter( PARAMETER_XMLPAGE_NAME );
        String strStyle = request.getParameter( PARAMETER_XMLPAGE_STYLE );
        String strPageContent;
        String strPageTitle;
        String strPagePath;

        if ( ( strXmlPageName != null ) && !EMPTY_STRING.equals( strXmlPageName ) && ( strStyle != null ) &&
                !EMPTY_STRING.equals( strStyle ) )
        {
            XmlPageElement xmlPageElement = XmlPageService.getInstance(  ).getXmlPageResource( strXmlPageName );

            if ( xmlPageElement != null )
            {
                strPageContent = XmlPageContentUtils.getContent( xmlPageElement, strStyle );

                // if the strPageContent is null, this means that there has been a problem when getting the content
                if ( strPageContent == null )
                {
                    strPageContent = AppPropertiesService.getProperty( PROPERTY_ERROR_UNKNOWN );
                    strPageTitle = AppPropertiesService.getProperty( PROPERTY_ERROR_PAGE_TITLE );
                    strPagePath = AppPropertiesService.getProperty( PROPERTY_ERROR_PAGE_PATH );
                }
                else
                {
                    strPageTitle = xmlPageElement.getTitle(  );
                    strPagePath = xmlPageElement.getTitle(  );
                }
            }
            else
            {
                strPageContent = AppPropertiesService.getProperty( PROPERTY_ERROR_PAGE_NOT_FOUND );
                strPageTitle = AppPropertiesService.getProperty( PROPERTY_ERROR_PAGE_TITLE );
                strPagePath = AppPropertiesService.getProperty( PROPERTY_ERROR_PAGE_PATH );
            }
        }
        else
        {
            strPageContent = AppPropertiesService.getProperty( PROPERTY_ERROR_MISSING_PARAMETERS );
            strPageTitle = AppPropertiesService.getProperty( PROPERTY_ERROR_PAGE_TITLE );
            strPagePath = AppPropertiesService.getProperty( PROPERTY_ERROR_PAGE_PATH );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PAGE_XMLPAGE );
        template.substitute( BOOKMARK_PAGE_CONTENT, strPageContent );
        page.setContent( template.getHtml(  ) );
        page.setTitle( strPageTitle );
        page.setPathLabel( strPagePath );

        return page;
    }
}
