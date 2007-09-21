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
package fr.paris.lutece.portal.service.template;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Locale;


/**
 * This Service is used to retreive HTML templates, stored as files in the WEB-INF/templates directory of the webapp,
 * to build the user interface. It provides a cache feature to prevent from loading file each time it is asked.
 */
public class AppTemplateService
{
    // Variables
    private static String _strTemplateDefaultPath;

    /**
     * Protected constructor
     */
    private AppTemplateService(  )
    {
    }

    /**
     * Initializes the service with the templates's path
     * @param strTemplatePath The template path
     */
    public static void init( String strTemplatePath )
    {
        _strTemplateDefaultPath = strTemplatePath;
        FreeMarkerTemplateService.init( strTemplatePath );
    }

    /**
     * Reset the cache
     */
    public static void resetCache(  )
    {
        FreeMarkerTemplateService.resetCache(  );
    }

    /**
     * Returns a reference on a template object (load the template or get it from the cache if present.)
     *
     * @param strTemplate The name of the template
     * @return The template object.
     */
    public static HtmlTemplate getTemplate( String strTemplate )
    {
        return getTemplate( strTemplate, _strTemplateDefaultPath );
    }

    /**
     * Returns a reference on a template object (load the template or get it from the cache if present.)
     *
     * @param strTemplate The name of the template
     * @param strPath The specific path to load the template
     * @return The template object.
     * @since 1.3.1
     */
    public static HtmlTemplate getTemplate( String strTemplate, String strPath )
    {
        return getTemplate( strTemplate, strPath, null, null );
    }

    ////////////////////////////////////////////////////////////////////////////
    // v1.5

    /**
     * Returns a reference on a template object (load the template or get it from the cache if present.)
     *
     * @param strTemplate The name of the template
     * @param locale The current locale to localize the template
     * @return The template object.
     * @since 1.5
     */
    public static HtmlTemplate getTemplate( String strTemplate, Locale locale )
    {
        return getTemplate( strTemplate, _strTemplateDefaultPath, locale, null );
    }

    /**
     * Returns a reference on a template object (load the template or get it from the cache if present.)
     * @param strTemplate The name of the template
     * @param locale The current locale to localize the template
     * @param model the model to use for loading
     * @return The template object.
     * @since 1.5
     */
    public static HtmlTemplate getTemplate( String strTemplate, Locale locale, Object model )
    {
        HtmlTemplate template = null;

        // Load the template from the file
        template = getTemplate( strTemplate, _strTemplateDefaultPath, locale, model );

        return template;
    }

    /**
     * Returns a reference on a template object (load the template or get it from the cache if present.)
     *
     * @param strTemplate The name of the template
     * @param strPath The specific path to load the template
     * @param locale The current locale to localize the template
     * @param model the model to use for loading
     * @return The template object.
     * @since 1.5
     */
    public static HtmlTemplate getTemplate( String strTemplate, String strPath, Locale locale, Object model )
    {
        HtmlTemplate template = null;

        // Load the template from the file
        template = loadTemplate( strPath, strTemplate, locale, model );

        return template;
    }

    /**
     * Load the template from the file
     * @param strTemplate The name of the template
     * @param strPath The specific path to load the template
     * @param locale The current locale to localize the template
     * @param model the model to use for loading
     * @return The loaded template
     */
    private static HtmlTemplate loadTemplate( String strPath, String strTemplate, Locale locale, Object model )
    {
        HtmlTemplate template = null;
        template = FreeMarkerTemplateService.loadTemplate( strPath, strTemplate, locale, model );

        if ( locale != null )
        {
            String strLocalized = I18nService.localize( template.getHtml(  ), locale );
            template = new HtmlTemplate( strLocalized );
        }

        return template;
    }
}
