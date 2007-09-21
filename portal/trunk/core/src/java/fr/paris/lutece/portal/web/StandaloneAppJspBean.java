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
package fr.paris.lutece.portal.web;

import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.content.XPageAppService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.portal.StandaloneAppService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.xpages.XPageApplicationEntry;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * Class of the StandaloneAppJspBean object.
 */
public class StandaloneAppJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final int MODE_HTML = 0;
    private static final String MARK_PLUGIN_NAME = "plugin_name";
    private static final String MARK_PLUGIN_DESCRIPTION = "plugin_description";
    private static final String MARK_PLUGIN_VERSION = "plugin_version";
    private static final String MARK_PLUGIN_PROVIDER = "plugin_provider";
    private static final String TEMPLATE_STANDALONE = "skin/site/standalone_app.html";
    private static final String TEMPLATE_STANDALONE_PLUGINS_LIST = "skin/site/standalone_plugins_list.html";
    private static final String MARK_PLUGINS_LIST = "plugins_list";
    private static final String MARK_BASE_URL = "base_url";

    /**
     * Returns the content of a page according to the parameters found in the http request. One distinguishes article,
     * page and xpage and the mode.
     *
     * @param request The http request
     * @return the html code for the display of a page of a site
     * @throws UserNotSignedException The UserNotSignedException
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    public String getContent( HttpServletRequest request )
        throws UserNotSignedException, SiteMessageException
    {
        return getContent( request, MODE_HTML );
    }

    /**
     * Returns the content of a page according to the parameters found in the http request. One distinguishes article,
     * page and xpage and the mode.
     *
     * @param request The http request
     * @param nMode The mode (normal or administration)
     * @return the html code for the display of a page of a site
     * @throws UserNotSignedException The UserNotSignedException
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    public String getContent( HttpServletRequest request, int nMode )
        throws UserNotSignedException, SiteMessageException
    {
        StandaloneAppService standaloneAppService = new StandaloneAppService(  );

        ContentService cs = standaloneAppService;

        if ( cs != null )
        {
            return cs.getPage( request, nMode );
        }

        return PortalService.getDefaultPage( request, nMode );
    }

    /**
     * Display the list of plugins app installed on the instance of lutece
     *
     * @return the list
     */
    public String getPluginList( HttpServletRequest request )
    {
        HashMap<String, String> modelList = null;
        HashMap<String, String> modelLine = new HashMap<String, String>(  );
        Locale locale = ( request == null ) ? null : request.getLocale(  );

        StringBuffer strLines = new StringBuffer(  );

        // Scan of the list
        for ( XPageApplicationEntry entry : XPageAppService.getXPageApplicationsList(  ) )
        {
            modelLine = new HashMap<String, String>(  );

            if ( entry.isEnable(  ) )
            {
                Plugin plugin = entry.getPlugin(  );

                if ( plugin != null )
                {
                    modelLine.put( MARK_PLUGIN_NAME, plugin.getName(  ) );
                    modelLine.put( MARK_PLUGIN_DESCRIPTION, plugin.getDescription(  ) );
                    modelLine.put( MARK_PLUGIN_VERSION, plugin.getVersion(  ) );
                    modelLine.put( MARK_PLUGIN_PROVIDER, plugin.getProvider(  ) );

                    HtmlTemplate templateLine = AppTemplateService.getTemplate( TEMPLATE_STANDALONE_PLUGINS_LIST,
                            locale, modelLine );
                    strLines.append( templateLine.getHtml(  ) );
                }
            }
        }

        // Insert the rows in the list
        modelList.put( MARK_PLUGINS_LIST, strLines.toString(  ) );
        modelList.put( MARK_BASE_URL, AppPathService.getBaseUrl( request ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_STANDALONE, locale, modelList );

        return templateList.getHtml(  );
    }
}
