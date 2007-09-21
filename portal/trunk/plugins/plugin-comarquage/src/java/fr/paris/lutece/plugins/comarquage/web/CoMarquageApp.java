/*
 * Copyright (c) 2002-2006, Mairie de Paris
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
package fr.paris.lutece.plugins.comarquage.web;

import fr.paris.lutece.plugins.comarquage.service.daemon.CoMarquageIndexerDaemon;
import fr.paris.lutece.plugins.comarquage.util.CoMarquageUtils;
import fr.paris.lutece.plugins.comarquage.util.FileUtils;
import fr.paris.lutece.plugins.comarquage.util.cache.comarquageimpl.CardKey;
import fr.paris.lutece.plugins.comarquage.util.search.SearchUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Hits;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides comarquage pages.
 */
public class CoMarquageApp implements XPageApplication
{
    //public static final int ERR_NUMBER_COMARQUAGE_IO = 1001;

    // templates
    private static final String TEMPLATE_RESULTS_ENTRY = "skin/plugins/comarquage/search/search_results_entry.html";
    private static final String TEMPLATE_XPAGE_COMARQUAGE = "skin/plugins/comarquage/page_comarquage.html";
    private static final String TEMPLATE_RESULTS = "skin/plugins/comarquage/search/search_results.html";
    private static final String TEMPLATE_RESULTS_ROW = "skin/plugins/comarquage/search/search_results_list_row.html";
    private static final String TEMPLATE_ERROR = "skin/plugins/comarquage/search/search_error_request.html";
    private static final String TEMPLATE_BUTTON_PREVIOUS_RESULTS = "skin/plugins/comarquage/search/button_search_results_previous.html";
    private static final String TEMPLATE_BUTTON_NEXT_RESULTS = "skin/plugins/comarquage/search/button_search_results_next.html";
    private static final String TEMPLATE_BUTTON_BACK = "skin/plugins/comarquage/search/button_search_results_back.html";

    // MARKs
    private static final String MARK_QUERY = "query";
    private static final String MARK_START_PREVIOUS_PAGE = "start_previous_page";
    private static final String MARK_START_NEXT_PAGE = "start_next_page";
    private static final String MARK_PAGE = "page";
    private static final String MARK_START = "start";
    private static final String MARK_END = "end";
    private static final String MARK_TOTAL = "total";
    private static final String MARK_RESULTS = "results";
    private static final String MARK_BUTTON_PREVIOUS = "button_previous";
    private static final String MARK_BUTTON_NEXT = "button_next";
    private static final String MARK_BUTTON_BACK = "button_back";
    private static final String MARK_INDEX = "index";
    private static final String MARK_SCORE = "score";
    private static final String MARK_URL = "url";
    private static final String MARK_SEARCH_HEADER = "search_header";
    private static final String MARK_ID = "id";

    // Properties
    private static final String PROPERTY_PATHLABEL_FRAGMENT = ".xpage.pagePathLabel";
    private static final String PROPERTY_PAGETITLE_FRAGMENT = ".xpage.pageTitle";
    private static final String PROPERTY_ENTRY_CDC_ACCUEIL_FRAGMENT = ".entry.cdcHtmlAccueil";
    private static final String PROPERTY_ENTRY_CDC_THEME_FRAGMENT = ".entry.cdcHtmlTheme";
    private static final String PROPERTY_RESULTS_PER_PAGE = ".indexing.nbDocsPerPage";
    private static final String PROPERTY_RESULTS_PER_PAGE_DEFAULT = "10";
    private static final String PROPERTY_ENTRY_CDC_ACCUEIL_PERSO_FRAGMENT = ".entry.cdcHtmlAccueilPerso.";
    private static final String PROPERTY_ENTRY_CDC_PAGE_LINK_FRAGMENT = ".entry.cdcPageLink";
    private static final String PROPERTY_LOCALS_XSL_PATH_FRAGMENT = ".path.xsl";

    // parameters
    private static final String PARAMETER_QUERY = "query";
    private static final String PARAMETER_START = "start";
    private static final String PARAMETER_XPAGE_APP = "page";

    //
    private static final String STRING_TITRE = "title";
    private static final String STRING_URL = "url";

    /**
    * Creates a new CoMarquageApp object
    */
    public CoMarquageApp(  )
    {
    }

    /**
    * Returns the CoMarquage XPage content depending on the request
    * parameters and the current mode.
    *
    * @param request The HTTP request.
    * @param nMode The current mode.
    * @param plugin the plugin
    * @return The page content.
    */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        XPage page = new XPage(  );

        //	records the plugin for futher use
        String strPluginName = plugin.getName(  );

        // set the title of the xpage 
        page.setTitle( I18nService.getLocalizedString( strPluginName + PROPERTY_PAGETITLE_FRAGMENT, request.getLocale() ) );
        
        String strHtmlCode = "";

        // get the id parameter
        String strId = CoMarquageUtils.getId( request );

        // get the query paramter
        String strQuery = request.getParameter( PARAMETER_QUERY );

        // get basic path of the xpage
        String strPathLabel = I18nService.getLocalizedString( strPluginName + PROPERTY_PATHLABEL_FRAGMENT, request.getLocale() );
        // if the query parameter is not null, load the search page
        // else if no id parameter is found, load the welcome page,
        // otherwise load the theme page corresponding to the given id
        if ( ( strQuery != null ) && ( strQuery.length(  ) > 0 ) )
        {
            // set the path of the xpage (built the normal pathLabel)
            page.setPathLabel( strPathLabel );

            strHtmlCode = getSearchPage( request, strPluginName );
        }
        else if ( ( strId == null ) || ( strId.equals( "" ) ) )
        {
            // set the path of the xpage (built the normal pathLabel)
            page.setPathLabel( strPathLabel );

            // get the welcome page html code
            strHtmlCode = getWelcomePage( request, strPluginName );
        }
        else
        {
            String strName = request.getParameter( PARAMETER_XPAGE_APP );
            String strPropertyCoMarquageCode = strName + CoMarquageConstants.PROPERTY_COMARQUAGE_CODE_FRAGMENT;
            CardKey cardKey = new CardKey( AppPropertiesService.getProperty( strPropertyCoMarquageCode ), strId, '/' );

            // get the entry for the chain manager corresponding to the page Link
            String strPropertyEntryPageLink = strName + PROPERTY_ENTRY_CDC_PAGE_LINK_FRAGMENT;
            String strPageLinkEntry = AppPropertiesService.getProperty( strPropertyEntryPageLink );

            // call the chain manager to retrieve the xml data that gives the list of items of the extended page path
            String strPathLabelItemsXml = CoMarquageUtils.callChainManagerByPluginName( strName, strPageLinkEntry,
                    cardKey );

            if ( ( strPathLabelItemsXml != null ) && ( strPathLabelItemsXml.length(  ) != 0 ) )
            {
                // set the extended page path
                page.setXmlExtendedPathLabel( strPathLabelItemsXml );
            }
            else
            {
                // set the path of the xpage (built the normal pathLabel)
                page.setPathLabel( strPathLabel );
            }

            // get the page html code
            strHtmlCode = getThemePage( request, strId, strPluginName );
        }

        // set the page content
        page.setContent( strHtmlCode );

        return page;
    }

    /**
     * Returns the Html code for the welcome page of the comarquage plugin
     *
     * @param request the http request
     * @param strPluginName the plugin name
     * @return the html code for tha welcome page
     */
    private String getWelcomePage( HttpServletRequest request, String strPluginName )
    {
        HashMap<String,String> model = new HashMap<String,String>(  );

        String strData = null;

        String strPropertyCoMarquageCode = strPluginName + CoMarquageConstants.PROPERTY_COMARQUAGE_CODE_FRAGMENT;
        CardKey cardKey = new CardKey( AppPropertiesService.getProperty( strPropertyCoMarquageCode ),
                CoMarquageConstants.ROOT_NODE_ID, '/' );

        String strRoleName = request.getParameter( CoMarquageConstants.PARAMETER_ROLE_NAME );

        String strPropertyEntryCdcAccueil;
        String strPropertyFileXslWelcomePage;

        if ( strRoleName != null )
        {
            strPropertyEntryCdcAccueil = strPluginName + PROPERTY_ENTRY_CDC_ACCUEIL_PERSO_FRAGMENT + strRoleName;
            strPropertyFileXslWelcomePage = strPluginName + CoMarquageConstants.PROPERTY_XSL_FILENAME_WELCOME_FRAGMENT +
                "." + strRoleName;
        }
        else
        {
            strPropertyEntryCdcAccueil = strPluginName + PROPERTY_ENTRY_CDC_ACCUEIL_FRAGMENT;
            strPropertyFileXslWelcomePage = strPluginName + CoMarquageConstants.PROPERTY_XSL_FILENAME_WELCOME_FRAGMENT;
        }

        String strXslFileName = AppPropertiesService.getProperty( strPropertyFileXslWelcomePage );

        String strFilePath = AppPathService.getPath( strPluginName + PROPERTY_LOCALS_XSL_PATH_FRAGMENT, strXslFileName );

        File file = new File( strFilePath );
        boolean btestXslWelcomePagePresent = FileUtils.fileExists( file );

        if ( btestXslWelcomePagePresent )
        {
            String strAccueilEntry = AppPropertiesService.getProperty( strPropertyEntryCdcAccueil );
            strData = CoMarquageUtils.callChainManagerByPluginName( strPluginName, strAccueilEntry, cardKey );
        }
        else
        {
            String strPropertyEntryCdcTheme = strPluginName + PROPERTY_ENTRY_CDC_THEME_FRAGMENT;
            String strThemeEntry = AppPropertiesService.getProperty( strPropertyEntryCdcTheme );
            strData = CoMarquageUtils.callChainManagerByPluginName( strPluginName, strThemeEntry, cardKey );
        }

        if ( strData == null )
        {
            String strPropertyMessageNoPage = strPluginName + CoMarquageConstants.PROPERTY_MESSAGE_NO_PAGE_FRAGMENT;
            strData = AppPropertiesService.getProperty( strPropertyMessageNoPage );
        }

        model.put( CoMarquageConstants.MARK_COMARQUAGE_DATA, strData );

        String strSearchHeader = getHeader( request, "" );
        model.put( MARK_SEARCH_HEADER, strSearchHeader );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_COMARQUAGE, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Returns the Html code for a theme page of the comarquage plugin
     *
     * @param request The HTTP request
     * @param strPluginName the plugin name
     * @return the html code for the card whose id is given in request
     */
    private String getThemePage( HttpServletRequest request, String strId, String strPluginName )
    {
        HashMap<String,String> model = new HashMap<String,String>(  );

        String strData = null;

        String strPropertyCoMarquageCode = strPluginName + CoMarquageConstants.PROPERTY_COMARQUAGE_CODE_FRAGMENT;
        CardKey cardKey = new CardKey( AppPropertiesService.getProperty( strPropertyCoMarquageCode ), strId, '/' );

        String strPropertyEntryCdcTheme = strPluginName + PROPERTY_ENTRY_CDC_THEME_FRAGMENT;
        String strThemeEntry = AppPropertiesService.getProperty( strPropertyEntryCdcTheme );

        strData = CoMarquageUtils.callChainManagerByPluginName( strPluginName, strThemeEntry, cardKey );

        if ( strData == null )
        {
            String strPropertyMessageNoPage = strPluginName + CoMarquageConstants.PROPERTY_MESSAGE_NO_PAGE_FRAGMENT;
            strData = AppPropertiesService.getProperty( strPropertyMessageNoPage );
        }

        model.put( CoMarquageConstants.MARK_COMARQUAGE_DATA, strData );

        String strSearchHeader = getHeader( request, "" );
        model.put( MARK_SEARCH_HEADER, strSearchHeader );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_COMARQUAGE, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Returns the Html code for the search page
     * @param request the http request
     * @param strPluginName the plugin name
     * @return the Html code for the search page
     */
    private String getSearchPage( HttpServletRequest request, String strPluginName )
    {
        // get the query paramter
        String strQuery = request.getParameter( PARAMETER_QUERY );

        // Search service
        String strCDCPath = AppPropertiesService.getProperty( strPluginName +
                CoMarquageIndexerDaemon.PROPERTY_INDEXING_CDC_PATH );
        String strIndexPath = AppPathService.getPath( strPluginName +
                CoMarquageIndexerDaemon.PROPERTY_INDEXING_BASE_PATH, strCDCPath );
        String strStartIndex = request.getParameter( PARAMETER_START );

        int nStartIndex = 0;

        if ( strStartIndex != null )
        {
            try
            {
                nStartIndex = Integer.parseInt( strStartIndex );
            }
            catch ( NumberFormatException e )
            {
                nStartIndex = 0;
            }
        }

        String strSearch = "";

        if ( ( strQuery != null ) && ( strQuery.length(  ) > 0 ) )
        {
            strSearch = search( request, strPluginName, strIndexPath, strQuery, nStartIndex );
        }

        return strSearch;
    }

    /**
     * Returns the search header
     * @param strQuery the query
     * @return the header
     */
    private String getHeader( HttpServletRequest request, String strQuery )
    {
        HashMap<String,String> model = new HashMap<String,String>(  );

        if ( strQuery == null )
        {
            model.put( MARK_QUERY, "" );
        }
        else
        {
            model.put( MARK_QUERY, strQuery );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RESULTS_ENTRY, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Returns the next fragment of the results when clicking on the next button
     * @param strPluginName the plugin name
     * @param strQuery the query
     * @param nHitsCount the hit count
     * @param nStartIndex the index of the first result to retrieve
     * @param nEndIndex the index of the last result to retrieve
     * @param nResultsPerPage the number of result per page
     * @return the html page
     */
    private String getNextButtonFragment( HttpServletRequest request, String strPluginName, String strQuery,
        int nHitsCount, int nStartIndex, int nEndIndex, int nResultsPerPage )
    {
        // Next button management
        String strNextButton = "";

        HashMap<String,String> model = new HashMap<String,String>(  );

        if ( nEndIndex < nHitsCount )
        {
            // Insert the next button
            String strStartNextPage = String.valueOf( nStartIndex + nResultsPerPage );

            model.put( MARK_PAGE, strPluginName );
            model.put( MARK_QUERY, strQuery );
            model.put( MARK_START_NEXT_PAGE, strStartNextPage );

            HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_BUTTON_NEXT_RESULTS, request.getLocale(  ), model );

            strNextButton = t.getHtml(  );
        }

        return strNextButton;
    }

    /**
     * Returns the previous fragment of the results when clicking on the next button
     * @param strPluginName the plugin name
     * @param strQuery the query
     * @param nStartIndex the index of the first result to retrieve
     * @param nResultsPerPage the number of result per page
     * @return the html page
     */
    private String getPreviousButtonFragment( HttpServletRequest request, String strPluginName, String strQuery,
        int nStartIndex, int nResultsPerPage )
    {
        HashMap<String,String> model = new HashMap<String,String>(  );

        // Previous button management
        String strPreviousButton = "";

        if ( nStartIndex > ( nResultsPerPage - 1 ) )
        {
            String strStartPagePrecedente = String.valueOf( nStartIndex - nResultsPerPage );

            // Insert the previous button
            model.put( MARK_PAGE, strPluginName );
            model.put( MARK_QUERY, strQuery );
            model.put( MARK_START_PREVIOUS_PAGE, strStartPagePrecedente );

            HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_BUTTON_PREVIOUS_RESULTS, request.getLocale(  ),
                    model );

            strPreviousButton = t.getHtml(  );
        }

        return strPreviousButton;
    }

    /**
     * Returns the back button fragment
     * @param request the http request to retrieve the id
     * @param strPluginName the instance's name
     * @param nStartIndex the index of the first result to retrieve
     * @param nResultsPerPage the number of result per page
     * @return the html page
     */
    private String getBackButtonFragment( HttpServletRequest request, String strPluginName, int nStartIndex,
        int nResultsPerPage )
    {
        HashMap<String,String> model = new HashMap<String,String>(  );

        // Previous button management
        String strBackButton = "";

        // get the id parameter
        String strId = CoMarquageUtils.getId( request );

        if ( nStartIndex <= ( nResultsPerPage - 1 ) )
        {
            // Insert the back button
            if ( strId != null )
            {
                model.put( MARK_ID, strId );
            }
            else
            {
                model.put( MARK_ID, "" );
            }

            model.put( CoMarquageConstants.MARK_PLUGIN_NAME, strPluginName );

            HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_BUTTON_BACK, request.getLocale(  ), model );

            strBackButton = t.getHtml(  );
        }

        return strBackButton;
    }

    /**
     * Searches the card
     * @param request the http request
     * @param strPluginName the plugin name
     * @param strIndexPath the path
     * @param strQuery the query
     * @param nStartIndex the index to be used
     * @return the results of the search
     */
    private String search( HttpServletRequest request, String strPluginName, String strIndexPath, String strQuery,
        int nStartIndex )
    {
        HashMap<String,String> model = new HashMap<String,String>(  );

        if ( ( strQuery == null ) || ( strQuery.length(  ) == 0 ) )
        {
            model.put( MARK_BUTTON_PREVIOUS, "" );
            model.put( MARK_BUTTON_NEXT, "" );

            String strSearchHeader = getHeader( request, "" );
            model.put( MARK_SEARCH_HEADER, strSearchHeader );

            model.put( MARK_QUERY, "" );
            model.put( MARK_START, "" );
            model.put( MARK_END, "" );
            model.put( MARK_TOTAL, "" );
            model.put( MARK_RESULTS, "" );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RESULTS, request.getLocale(  ), model );

            return template.getHtml(  );
        }

        String strResultsPerPage = AppPropertiesService.getProperty( strPluginName + PROPERTY_RESULTS_PER_PAGE,
                PROPERTY_RESULTS_PER_PAGE_DEFAULT );
        int nResultsPerPage = Integer.parseInt( strResultsPerPage );

        try
        {
            // Get results documents
            Hits hits = SearchUtils.search( strPluginName, strIndexPath, strQuery );

            // Error in user query expression?
            if ( hits == null )
            {
                // get the search field
                String strSearchHeader = getHeader( request, strQuery );
                model.put( MARK_SEARCH_HEADER, strSearchHeader );

                // substitute the query value
                model.put( MARK_QUERY, strQuery );

                // get the button to go back to the card
                String strBackButton = getBackButtonFragment( request, strPluginName, 0, nResultsPerPage );
                model.put( MARK_BUTTON_BACK, strBackButton );

                HtmlTemplate tErreur = AppTemplateService.getTemplate( TEMPLATE_ERROR, request.getLocale(  ), model );

                // return the html code
                return tErreur.getHtml(  );
            }

            int nEndIndex = Math.min( hits.length(  ), nStartIndex + nResultsPerPage );

            // Update UI parameters
            String strStart = ( nEndIndex == 0 ) ? String.valueOf( 0 ) : String.valueOf( nStartIndex + 1 );
            String strEnd = String.valueOf( nEndIndex );
            String strTotal = String.valueOf( hits.length(  ) );

            String strNextButton = getNextButtonFragment( request, strPluginName, strQuery, hits.length(  ),
                    nStartIndex, nEndIndex, nResultsPerPage );
            String strPreviousButton = getPreviousButtonFragment( request, strPluginName, strQuery, nStartIndex,
                    nResultsPerPage );
            String strBackButton = getBackButtonFragment( request, strPluginName, nStartIndex, nResultsPerPage );

            model.put( MARK_BUTTON_PREVIOUS, strPreviousButton );
            model.put( MARK_BUTTON_NEXT, strNextButton );

            model.put( MARK_BUTTON_BACK, strBackButton );

            String strSearchHeader = getHeader( request, strQuery );
            model.put( MARK_SEARCH_HEADER, strSearchHeader );

            model.put( MARK_QUERY, strQuery );
            model.put( MARK_START, strStart );
            model.put( MARK_END, strEnd );
            model.put( MARK_TOTAL, strTotal );

            //HtmlTemplate templateLigne = AppTemplateService.getTemplate( TEMPLATE_RESULTS_ROW, request.getLocale(  ),
              //      model );

            StringBuffer strResults = new StringBuffer(  );

            for ( int i = nStartIndex; i < nEndIndex; ++i )
            {
                // Display each document found
                Document doc = hits.doc( i );

                String strScore = String.valueOf( (int) ( hits.score( i ) * 100.0f ) );
                String strIndex = String.valueOf( i + 1 );
                String strTitle = doc.get( STRING_TITRE );

                String strUrl = doc.get( STRING_URL );

                model.put( MARK_INDEX, strIndex );
                model.put( MARK_SCORE, strScore );
                model.put( CoMarquageConstants.MARK_TITLE, strTitle );
                model.put( MARK_PAGE, strPluginName );
                model.put( MARK_URL, strUrl );

                HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_RESULTS_ROW, request.getLocale(  ),
                      model );

                strResults.append( t.getHtml(  ) );
            }

            model.put( MARK_RESULTS, strResults.toString(  ) );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RESULTS, request.getLocale(  ), model );

            return template.getHtml(  );
        }
        catch ( ParseException e )
        {
            // get the search field
            String strSearchHeader = getHeader( request, strQuery );
            model.put( MARK_SEARCH_HEADER, strSearchHeader );

            // substitute the query value
            model.put( MARK_QUERY, strQuery );

            // get the button to go back to the card
            String strBackButton = getBackButtonFragment( request, strPluginName, 0, nResultsPerPage );
            model.put( MARK_BUTTON_BACK, strBackButton );

            // Error in string entered by user
            HtmlTemplate tErreur = AppTemplateService.getTemplate( TEMPLATE_ERROR, request.getLocale(  ), model );

            // return the html code
            return tErreur.getHtml(  );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );

            return e.getMessage(  );
        }
    }
}
