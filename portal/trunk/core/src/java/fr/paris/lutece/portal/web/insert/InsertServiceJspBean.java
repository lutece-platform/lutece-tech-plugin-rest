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
package fr.paris.lutece.portal.web.insert;

import fr.paris.lutece.portal.service.html.EncodingService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * Base class for InsertServiceJspBean
 */
public abstract class InsertServiceJspBean
{
    private static final String PARAMETER_INPUT = "input";
    private static final String PARAMETER_INSERT = "insert";
    private static final String JSP_DO_INSERT = "jsp/admin/insert/DoInsertIntoEditor.jsp";
    private static final String TEMPLATE_LINK = "/admin/insert/insert_link.html";
    private static final String MARK_TEXT = "text";
    private static final String MARK_URL = "url";
    private static final String MARK_TITLE = "title";
    private static final String MARK_TARGET = "target";

    /**
     * Build the Url to insert HTML code into the current rich text editor
     * @param request The HTTP request
     * @param strInput The rich text input field
     * @param strInsert The code to insert
     * @return The Url that will provide the insertion
     */
    protected String insertUrl( HttpServletRequest request, String strInput, String strInsert )
    {
        // Encode the HTML code to insert
        String strEncodedInsert = EncodingService.encodeUrl( strInsert );

        // Build the url to make the insert
        UrlItem urlDoInsert = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_DO_INSERT );
        urlDoInsert.addParameter( PARAMETER_INPUT, strInput );
        urlDoInsert.addParameter( PARAMETER_INSERT, strEncodedInsert );

        return urlDoInsert.getUrl(  );
    }

    /**
     * Build an HTML link
     * @param strText The text of the link
     * @param strUrl The Url of the link
     * @param strTitle The title of the link
     * @param strTarget The target window
     * @return The HTML link
     */
    protected String buildLink( String strText, String strUrl, String strTitle, String strTarget )
    {
        HashMap model = new HashMap(  );
        model.put( MARK_TEXT, strText );
        model.put( MARK_URL, strUrl );
        model.put( MARK_TITLE, strTitle );
        model.put( MARK_TARGET, strTarget );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LINK, Locale.getDefault(  ), model );

        return template.getHtml(  );
    }
}
