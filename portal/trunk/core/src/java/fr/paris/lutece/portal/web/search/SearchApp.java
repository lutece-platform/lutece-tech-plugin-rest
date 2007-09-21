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
package fr.paris.lutece.portal.web.search;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.search.QueryEvent;
import fr.paris.lutece.portal.service.search.QueryListenersService;
import fr.paris.lutece.portal.service.search.SearchEngine;
import fr.paris.lutece.portal.service.search.SearchResult;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides search results pages.
 */
public class SearchApp implements XPageApplication
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String BEAN_SEARCH_ENGINE = "searchEngine"; 
    private static final String TEMPLATE_RESULTS = "skin/search/search_results.html";
    private static final String PROPERTY_SEARCH_PAGE_URL = "search.pageSearch.baseUrl";
    private static final String PROPERTY_RESULTS_PER_PAGE = "search.nb.docs.per.page";
    private static final String PROPERTY_PATH_LABEL = "portal.search.search_results.pathLabel";
    private static final String PROPERTY_PAGE_TITLE = "portal.search.search_results.pageTitle";
    private static final String MESSAGE_INVALID_SEARCH_TERMS = "portal.search.message.invalidSearchTerms";
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
     * @param nMode The current mode.
     * @param plugin The plugin
     * @return The HTML code of the page.
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        XPage page = new XPage(  );
        String strQuery = request.getParameter( PARAMETER_QUERY );
        String strSearchPageUrl = AppPropertiesService.getProperty( PROPERTY_SEARCH_PAGE_URL );
        String strError = "";
        Locale locale = request.getLocale(  );

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

        SearchEngine engine = (SearchEngine) SpringContextService.getBean( BEAN_SEARCH_ENGINE );
        List<SearchResult> listResults = engine.getSearchResults( strQuery );
        
        // Notify results infos to QueryEventListeners 
        notifyQueryListeners( strQuery , listResults.size() , request );
        
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

        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_PATH_LABEL, locale ) );
        page.setTitle( I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale ) );
        page.setContent( template.getHtml(  ) );

        return page;
    }
    
    /**
     * Notify all query Listeners
     * @param strQuery The query
     * @param nResultsCount The results count
     * @param request The request
     */
    private void notifyQueryListeners( String strQuery , int nResultsCount , HttpServletRequest request )
    {
        QueryEvent event = new QueryEvent();
        event.setQuery( strQuery );
        event.setResultsCount( nResultsCount );
        Locale localeVisitor = request.getLocale();
        event.setRequest( request );
        QueryListenersService.getInstance().notifyListeners( event );
    }
}
