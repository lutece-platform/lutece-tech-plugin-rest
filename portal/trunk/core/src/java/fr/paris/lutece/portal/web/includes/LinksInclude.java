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
package fr.paris.lutece.portal.web.includes;

import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


public class LinksInclude implements PageInclude
{
    private static final String PROPERTY_FAVOURITE = "lutece.favourite";
    private static final String MARK_FAVOURITE = "favourite";
    private static final String MARK_PLUGINS_CSS_LINKS = "plugins_css_links";
    private static final String MARK_PLUGINS_JAVASCRIPT_LINKS = "plugins_javascript_links";
    private static final String MARK_PLUGIN_CSS_STYLESHEET = "plugin_css_stylesheet";
    private static final String MARK_PLUGIN_JAVASCRIPT_FILE = "plugin_javascript_file";
    private static final String MARK_CSS_PREFIX = "css_prefix";
    private static final String TEMPLATE_PLUGIN_CSS_LINK = "skin/site/plugin_css_link.html";
    private static final String TEMPLATE_PLUGIN_JAVASCRIPT_LINK = "skin/site/plugin_javascript_link.html";
    private static final String PREFIX_PLUGINS_CSS = "css/plugins/";
    private static final String ABSOLUTE_URL = "http://";

    /**
     * Substitue specific bookmarks in the page template.
     * @param template The page template containing bookmarks to substitute
     * @param data A PageData object containing applications data
     * @param nMode The current mode
     * @param request The HTTP request
     */
    public void fillTemplate( HashMap<String, String> rootModel, PageData data, int nMode, HttpServletRequest request )
    {
        // Add links coming from the data object
        String strFavourite = ( data.getFavourite(  ) != null ) ? data.getFavourite(  )
                                                                : AppPropertiesService.getProperty( PROPERTY_FAVOURITE );
        rootModel.put( MARK_FAVOURITE, strFavourite );

        Locale locale = ( request == null ) ? null : request.getLocale(  );

        // Add CSS links coming from plugins
        Collection<Plugin> listPlugins = PluginService.getPluginList(  );
        StringBuffer sbCssLinks = new StringBuffer(  );
        StringBuffer sbJsLinks = new StringBuffer(  );

        for ( Plugin plugin : listPlugins )
        {
            if ( plugin.isInstalled(  ) )
            {
                if ( plugin.getCssStylesheet(  ) != null )
                {
                    String strPrefix = (plugin.getCssStylesheet().startsWith( ABSOLUTE_URL )) ? "" : PREFIX_PLUGINS_CSS;
                    
                    HashMap<String, String> model = new HashMap<String, String>(  );
                    model.put( MARK_PLUGIN_CSS_STYLESHEET, plugin.getCssStylesheet(  ) );
                    model.put( MARK_CSS_PREFIX , strPrefix );
                    HtmlTemplate tCss = AppTemplateService.getTemplate( TEMPLATE_PLUGIN_CSS_LINK, locale, model );
                    sbCssLinks.append( tCss.getHtml(  ) );
                }

                if ( plugin.getJavascriptFile(  ) != null )
                {
                    HashMap<String, String> model = new HashMap<String, String>(  );
                    model.put( MARK_PLUGIN_JAVASCRIPT_FILE, plugin.getJavascriptFile(  ) );

                    HtmlTemplate tJs = AppTemplateService.getTemplate( TEMPLATE_PLUGIN_JAVASCRIPT_LINK, locale, model );
                    sbJsLinks.append( tJs.getHtml(  ) );
                }
            }
        }

        rootModel.put( MARK_PLUGINS_CSS_LINKS, sbCssLinks.toString(  ) );
        rootModel.put( MARK_PLUGINS_JAVASCRIPT_LINKS, sbJsLinks.toString(  ) );
    }
}
