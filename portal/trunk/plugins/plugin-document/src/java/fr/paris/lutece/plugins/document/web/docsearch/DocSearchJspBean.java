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
package fr.paris.lutece.plugins.document.web.docsearch;

import fr.paris.lutece.plugins.document.service.docsearch.DocSearchItem;
import fr.paris.lutece.plugins.document.service.docsearch.DocSearchService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * DocSearchJspBean
 */
public class DocSearchJspBean extends PluginAdminPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String TEMPLATE_RESULTS = "admin/plugins/document/docsearch/search_results.html";
    private static final String PROPERTY_SEARCH_PAGE_URL = "document.docsearch.pageSearch.baseUrl";
    private static final String PROPERTY_RESULTS_PER_PAGE = "document.docsearch.nb.docs.per.page";
    private static final String MESSAGE_INVALID_SEARCH_TERMS = "document.message.invalidSearchTerms";
    private static final String PROPERTY_PAGE_TITLE_SEARCH = "document.search_results.pageTitle";
    private static final String DEFAULT_RESULTS_PER_PAGE = "10";
    private static final String DEFAULT_PAGE_INDEX = "1";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_NB_ITEMS_PER_PAGE = "items_per_page";
    private static final String PARAMETER_QUERY = "query";
    private static final String MARK_RESULTS_LIST = "results_list";
    private static final String MARK_QUERY = "query";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_ERROR = "error";

    /**
     * Returns search results
     *
     * @param request The HTTP request.
     * @return The HTML code of the page.
     */
    public String getSearch( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_SEARCH );

        String strQuery = request.getParameter( PARAMETER_QUERY );
        String strSearchPageUrl = AppPropertiesService.getProperty( PROPERTY_SEARCH_PAGE_URL );
        String strError = "";
        Locale locale = getLocale(  );

        // Check XSS characters
        if ( ( strQuery != null ) && ( StringUtil.containsXssCharacters( strQuery ) ) )
        {
            strError = I18nService.getLocalizedString( MESSAGE_INVALID_SEARCH_TERMS, locale );
            strQuery = "";
        }

        String strNbItemPerPage = request.getParameter( PARAMETER_NB_ITEMS_PER_PAGE );
        String strDefaultNbItemPerPage = AppPropertiesService.getProperty( PROPERTY_RESULTS_PER_PAGE,
                DEFAULT_RESULTS_PER_PAGE );
        strNbItemPerPage = ( strNbItemPerPage != null ) ? strNbItemPerPage : strDefaultNbItemPerPage;

        int nNbItemsPerPage = Integer.parseInt( strNbItemPerPage );
        String strCurrentPageIndex = request.getParameter( PARAMETER_PAGE_INDEX );
        strCurrentPageIndex = ( strCurrentPageIndex != null ) ? strCurrentPageIndex : DEFAULT_PAGE_INDEX;

        int nPageIndex = Integer.parseInt( strCurrentPageIndex );
        int nStartIndex = ( nPageIndex - 1 ) * nNbItemsPerPage;

        List<DocSearchItem> listResults = DocSearchService.getInstance(  )
                                                          .getSearchResults( strQuery, nStartIndex, getUser(  ) );

        UrlItem url = new UrlItem( strSearchPageUrl );
        url.addParameter( PARAMETER_QUERY, strQuery );
        url.addParameter( PARAMETER_NB_ITEMS_PER_PAGE, nNbItemsPerPage );

        Paginator paginator = new Paginator( listResults, nNbItemsPerPage, url.getUrl(  ), PARAMETER_PAGE_INDEX,
                strCurrentPageIndex );

        HashMap model = new HashMap(  );
        model.put( MARK_RESULTS_LIST, paginator.getPageItems(  ) );
        model.put( MARK_QUERY, strQuery );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, strNbItemPerPage );
        model.put( MARK_ERROR, strError );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RESULTS, locale, model );

        return getAdminPage( template.getHtml(  ) );
    }
}
