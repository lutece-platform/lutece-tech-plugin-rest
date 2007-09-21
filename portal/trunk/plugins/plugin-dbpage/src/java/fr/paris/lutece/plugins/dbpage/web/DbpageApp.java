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
package fr.paris.lutece.plugins.dbpage.web;

import fr.paris.lutece.plugins.dbpage.business.DbPage;
import fr.paris.lutece.plugins.dbpage.service.DbPageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class manages dbpage.
 */
public class DbpageApp implements XPageApplication
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String TEMPLATE_MANAGE_DBPAGE = "skin/plugins/dbpage/dbpage.html";
    private static final String MARK_PAGE_TITLE = "page_title";
    private static final String MARK_SECTIONS = "sections";
    private static final String PARAMETER_DBPAGE_NAME = "dbpage";
    private static final String PROPERTY_PAGE_TITLE = "dbpage.pageTitle";
    private static final String PROPERTY_PAGE_PATH = "dbpage.pagePathLabel";
    private static final String STR_PAGE_ERROR = AppPropertiesService.getProperty( 
            "dbpage.page.properties.message.error" );
    private static final String STR_PARAMETER_ERROR = AppPropertiesService.getProperty( 
            "dbpage.parameter.properties.message.error" );

    /**
     * Creates a new DbPage object
     */
    public DbpageApp(  )
    {
    }

    /**
     *
     * @param request The request
     * @param nMode
     * @return Returns the content
     */
    public String getContent( HttpServletRequest request, int nMode )
    {
        return "";
    }

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

        page.setTitle( AppPropertiesService.getProperty( PROPERTY_PAGE_TITLE ) );
        page.setPathLabel( AppPropertiesService.getProperty( PROPERTY_PAGE_PATH ) );

        String strDbPageName = request.getParameter( PARAMETER_DBPAGE_NAME );

        if ( ( strDbPageName != null ) && ( !strDbPageName.equals( "" ) ) )
        {
            DbPage dbPage = DbPageService.getInstance(  ).getDbPage( strDbPageName );

            if ( dbPage != null )
            {
                List listValues = DbPageService.getInstance(  ).getValues( request );
                String strPageContent = buildPage( dbPage, listValues, request );
                page.setContent( strPageContent );
                page.setTitle( dbPage.getTitle(  ) );
                page.setPathLabel( dbPage.getTitle(  ) );
            }
            else
            {
                page.setContent( STR_PAGE_ERROR );
            }
        }
        else
        {
            page.setContent( STR_PARAMETER_ERROR );
        }

        return page;
    }

    /**
     * Returns the Html of the dbpage
     *
     * @param dbPage The dbPage object
     * @param listValues The listValues substitute in the SQL request
     * @param plugin The Plugin object
     * @return the html code of the dbPage
     */
    private String buildPage( DbPage dbPage, List listValues, HttpServletRequest request )
    {
        HashMap model = new HashMap(  );
        model.put( MARK_PAGE_TITLE, dbPage.getTitle(  ) );
        model.put( MARK_SECTIONS, dbPage.getContent( listValues, request ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_DBPAGE, request.getLocale(  ), model );

        return template.getHtml(  );
    }
}
