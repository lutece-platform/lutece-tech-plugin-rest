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
package fr.paris.lutece.plugins.links.web;

import fr.paris.lutece.plugins.links.business.Link;
import fr.paris.lutece.plugins.links.business.LinkHome;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.insert.InsertServiceJspBean;
import fr.paris.lutece.portal.web.insert.InsertServiceSelectionBean;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


public class LinksServiceJspBean extends InsertServiceJspBean implements InsertServiceSelectionBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // Templates
    private static final String TEMPLATE_SELECTOR_PAGE = "admin/plugins/links/links_selector.html";

    // Parameters
    private static final String PARAMETER_SELECTED_TEXT = "selected_text";
    private static final String PARAMETER_TEXT = "text";
    private static final String PARAMETER_TITLE = "title";
    private static final String PARAMETER_TARGET = "target";
    private static final String PARAMETER_LINK = "link";
    private static final String PARAMETER_INPUT = "input";

    // Marker
    private static final String MARK_LINKS_LIST = "links_list";
    private static final String MARK_SELECTED_TEXT = "selected_text";
    private static final String MARK_INPUT = "input";

    ////////////////////////////////////////////////////////////////////////////
    // Methods

    /**
    * Return the html form for image selection.
    *
    * @param request The Http Request
    * @return The html form.
    */
    public String getInsertServiceSelectorUI( HttpServletRequest request )
    {
        String strSelectedText = request.getParameter( PARAMETER_SELECTED_TEXT );
        String strInput = request.getParameter( PARAMETER_INPUT );

        // Scan of the list
        HashMap model = new HashMap(  );
        model.put( MARK_LINKS_LIST, LinkHome.getLinksList(  ) );
        model.put( MARK_SELECTED_TEXT, strSelectedText );
        model.put( MARK_INPUT, strInput );

        // Gets the locale of the user
        Locale locale = AdminUserService.getLocale( request );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SELECTOR_PAGE, locale, model );

        return template.getHtml(  );
    }

    public String doInsertLink( HttpServletRequest request )
    {
        String strText = request.getParameter( PARAMETER_TEXT );
        String strTitle = request.getParameter( PARAMETER_TITLE );
        String strTarget = request.getParameter( PARAMETER_TARGET );
        String strLink = request.getParameter( PARAMETER_LINK );
        String strInput = request.getParameter( PARAMETER_INPUT );

        // Check mandatory fields
        if ( strText.equals( "" ) || strTitle.equals( "" ) || ( strLink == null ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nLink = Integer.parseInt( strLink );
        Link link = LinkHome.findByPrimaryKey( nLink );
        String strInsertLink = buildLink( strText, link.getUrl(  ), strTitle, strTarget );

        return insertUrl( request, strInput, strInsertLink );
    }
}
