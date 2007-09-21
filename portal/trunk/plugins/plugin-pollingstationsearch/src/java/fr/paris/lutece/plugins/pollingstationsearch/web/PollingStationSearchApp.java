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
package fr.paris.lutece.plugins.pollingstationsearch.web;

import fr.paris.lutece.plugins.pollingstationsearch.business.Address;
import fr.paris.lutece.plugins.pollingstationsearch.business.AddressHome;
import fr.paris.lutece.plugins.pollingstationsearch.business.NumberSuffixtHome;
import fr.paris.lutece.plugins.pollingstationsearch.business.PollingStation;
import fr.paris.lutece.plugins.pollingstationsearch.business.PollingStationHome;
import fr.paris.lutece.plugins.pollingstationsearch.business.Street;
import fr.paris.lutece.plugins.pollingstationsearch.business.StreetHome;
import fr.paris.lutece.plugins.pollingstationsearch.business.StreetType;
import fr.paris.lutece.plugins.pollingstationsearch.business.StreetTypeHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;


/**
 * This class manages the polling station search page.
 */
public class PollingStationSearchApp implements XPageApplication
{
    // Constants
    private static final String MARK_SUFFIX_LIST = "mark_suffix_list";
    private static final String MARK_STREET_TYPE_LIST = "mark_street_type_list";
    private static final String MARK_NUMBER_SUFFIX = "mark_number_suffix";
    private static final String MARK_NUMBER_SUFFIX_ID = "mark_number_suffix_id";
    private static final String MARK_STREET_TYPE = "mark_street_type";
    private static final String MARK_STREET_NAME = "mark_street_name";
    private static final String MARK_STREET_NAME_IN_DATABASE = "mark_street_name_in_database";
    private static final String MARK_NUMBER_SUFFIX_IN_DATABASE = "mark_number_suffix_in_database";
    private static final String MARK_STREET_URBAN_DISTRICT_IN_DATABASE = "mark_street_urban_district_in_database";
    private static final String MARK_STREET_NUMBER = "mark_street_number";
    private static final String MARK_STREET_URBAN_DISTRICT = "mark_street_urban_district";
    private static final String MARK_STREET_LIST = "mark_street_list";
    private static final String MARK_POLLING_STATION = "mark_polling_station";
    private static final String MARK_CONTENT = "content";
    private static final String MARK_CITY = "mark_city";
    private static final String MARK_PLUGIN_NAME = "mark_plugin_name";
    private static final String MARK_URL_JSP = "mark_url_jsp";
    private static final String MARK_MESSAGE = "mark_message";
    private static final String MARK_MESSAGE_RESULT_INFO = "mark_message_result_info";

    // params - sent by requests
    private static final String PARAMETER_ACTION = "action_";
    private static final String PARAMETER_ACTION_SEARCH = "action_search";
    private static final String PARAMETER_ACTION_NEWSEARCH = "action_newsearch";
    private static final String PARAMETER_ACTION_RESET = "action_reset";
    private static final String PARAMETER_ACTION_SELECT = "action_select";
    private static final String PARAMETER_STREET_NUMBER = "street_number";
    private static final String PARAMETER_NUMBER_SUFFIX = "number_suffix";
    private static final String PARAMETER_STREET_NAME = "street_name";
    private static final String PARAMETER_STREET_TYPE = "short_street_type";
    private static final String PARAMETER_STREET_URBAN_DISTRICT = "street_urban_district";
    private static final String PARAMETER_STREET_CODE = "street_code";

    // properties - used to read in conf file
    //labels & titles
    private static final String PROPERTY_ERROR_LABEL = "schoolsearch.error.label";
    private static final String PROPERTY_ERROR_TITLE = "schoolsearch.error.title";
    private static final String PROPERTY_STREET_TYPE_OTHER_CHOICE_ID = "pollingstationsearch.street.type.other.choice.id";
    private static final String PROPERTY_NO_NUMBER_SUFFIX_CHOOSEN_ID = "pollingstationsearch.no.number.suffix.choosen.id";

    // technical configuration
    private static final String PROPERTY_URL_JSP = "pollingstationsearch.url.jsp";
    private static final String PROPERTY_STREET_NAME_MINLENGTH = "pollingstationsearch.streetname.minLength";
    private static final String PROPERTY_DATEVALIDITY_PATTERN = "pollingstationsearch.date.validity.pattern";

    // templates - to build pages
    private static final String TEMPLATE_PAGE = "skin/plugins/pollingstationsearch/page.html";
    private static final String TEMPLATE_SEARCH = "skin/plugins/pollingstationsearch/search.html";
    private static final String TEMPLATE_STREET_LIST = "skin/plugins/pollingstationsearch/street_list.html";
    private static final String TEMPLATE_RESULTS = "skin/plugins/pollingstationsearch/results.html";

    // keys which values are in pollingstation_message.properties
    private static final String KEY_SEARCHPAGE_TITLE = "pollingstationsearch.search_page.title";
    private static final String KEY_SEARCHPAGE_LABEL = "pollingstationsearch.search_page.label";
    private static final String KEY_SEARCHPAGE_STREET_NAME_HINT = "pollingstationsearch.search_page.streetname.hint";
    private static final String KEY_SEARCHPAGE_STREET_NUMBER_REQUIRED = "pollingstationsearch.search_page.streetnumber.required";
    private static final String KEY_SEARCHPAGE_STREET_NUMBER_FORMAT = "pollingstationsearch.search_page.streetnumber.format";
    private static final String KEY_SEARCHPAGE_STREET_NUMBER_POSITIVE = "pollingstationsearch.search_page.streetnumber.positive";
    private static final String KEY_SEARCHPAGE_STREET_NAME = "pollingstationsearch.search_page.streetname";
    private static final String KEY_SEARCHPAGE_STREET_TYPE_REQUIRED = "pollingstationsearch.search_page.streettype.required";
    private static final String KEY_SEARCHPAGE_STREET_URBAN_DISTRICT_REQUIRED = "pollingstationsearch.search_page.streeturbandistrict.required";
    private static final String KEY_SEARCHPAGE_NO_RESULT = "pollingstationsearch.search_page.noResults";
    private static final String KEY_SEARCHPAGE_SELECT_STREET_TYPE = "pollingstationsearch.search_page.select.street_type";
    private static final String KEY_SEARCHPAGE_STREET_TYPE_OTHER_CHOICE = "pollingstationsearch.search_page.street_type.other_choice";
    private static final String KEY_STREET_LIST_LABEL = "pollingstationsearch.street_list.label";
    private static final String KEY_STREET_LIST_TITLE = "pollingstationsearch.street_list.title";
    private static final String KEY_STREET_LIST_NO_PERIMETER_ALL = "pollingstationsearch.street_list.noPerimeter.all";
    private static final String KEY_STREET_LIST_SELECT_ADDRESS = "pollingstationsearch.street_list.select.address";
    private static final String KEY_RESULT_LABEL = "pollingstationsearch.result_page.label";
    private static final String KEY_RESULT_TITLE = "pollingstationsearch.result_page.title";
    private static final String KEY_RESULT_INFO = "pollingstationsearch.result_page.info";
    private static final String KEY_RESULT_CITY_NAME = "pollingstationsearch.result_page.city_name";
    private static final String KEY_MESSAGE_PAGE_NOT_FOUND = "message.pageNotFound";

    // other constant values
    private static final String CONSTANT_SPACE_CHAR = " ";
    private static final String CONSTANT_EMPTY_STRING = "";
    private static final String CONSTANT_DEFAULT_COMBO_ID = "0";

    // private fields
    private Plugin _plugin;
    private String _strUrlJsp;
    private int _nStreetNameMinLength;

    /**
     * Creates a new instance.
     */
    public PollingStationSearchApp(  )
    {
        String strMinLength = AppPropertiesService.getProperty( PROPERTY_STREET_NAME_MINLENGTH );
        _nStreetNameMinLength = Integer.parseInt( strMinLength );
        _strUrlJsp = AppPropertiesService.getProperty( PROPERTY_URL_JSP );
    }

    /**
     * Returns the polling station search page depending on the request parameters and
     * current mode.
     *
     * @param request the HTTP request.
     * @param nMode the current mode.
     * @param plugin the plugin.
     * @return the page content.
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        XPage page = new XPage(  );
        String strAction = getAction( request );

        // records the plugin for further use
        _plugin = plugin;

        String strContent = CONSTANT_EMPTY_STRING;
        String strTitle = CONSTANT_EMPTY_STRING;
        String strPathLabel = CONSTANT_EMPTY_STRING;
        HashMap modelTemplateXpagePollingStationSearch = new HashMap(  );
        HtmlTemplate template = null;

        if ( ( strAction == null ) || strAction.equals( PARAMETER_ACTION_NEWSEARCH ) )
        {
            // Return the search form
            strContent = getSearchPage( request, CONSTANT_EMPTY_STRING, false );
            strPathLabel = I18nService.getLocalizedString( KEY_SEARCHPAGE_LABEL, request.getLocale(  ) );
            strTitle = I18nService.getLocalizedString( KEY_SEARCHPAGE_TITLE, request.getLocale(  ) );
            modelTemplateXpagePollingStationSearch.put( MARK_CONTENT, strContent );
            template = AppTemplateService.getTemplate( TEMPLATE_PAGE, request.getLocale(  ),
                    modelTemplateXpagePollingStationSearch );
        }
        else if ( strAction.equals( PARAMETER_ACTION_RESET ) )
        {
            strContent = getSearchPage( request, CONSTANT_EMPTY_STRING, true );
            strPathLabel = I18nService.getLocalizedString( KEY_SEARCHPAGE_LABEL, request.getLocale(  ) );
            strTitle = I18nService.getLocalizedString( KEY_SEARCHPAGE_TITLE, request.getLocale(  ) );
            modelTemplateXpagePollingStationSearch.put( MARK_CONTENT, strContent );
            template = AppTemplateService.getTemplate( TEMPLATE_PAGE, request.getLocale(  ),
                    modelTemplateXpagePollingStationSearch );
        }
        else if ( strAction.equals( PARAMETER_ACTION_SEARCH ) )
        {
            // Return the street list, or the result page if there is only
            // one street name
            strContent = getStreetNames( request, true, CONSTANT_EMPTY_STRING, null, null, null, null, null );
            strPathLabel = I18nService.getLocalizedString( KEY_STREET_LIST_LABEL, request.getLocale(  ) );
            strTitle = I18nService.getLocalizedString( KEY_STREET_LIST_TITLE, request.getLocale(  ) );
            modelTemplateXpagePollingStationSearch.put( MARK_CONTENT, strContent );
            template = AppTemplateService.getTemplate( TEMPLATE_PAGE, request.getLocale(  ),
                    modelTemplateXpagePollingStationSearch );
        }
        else if ( strAction.equals( PARAMETER_ACTION_SELECT ) )
        {
            // Return the adresses result page
            strContent = getResults( request, true, null, null, null, null, null, null );
            strPathLabel = I18nService.getLocalizedString( KEY_RESULT_LABEL, request.getLocale(  ) );
            strTitle = I18nService.getLocalizedString( KEY_RESULT_TITLE, request.getLocale(  ) );
            modelTemplateXpagePollingStationSearch.put( MARK_CONTENT, strContent );
            template = AppTemplateService.getTemplate( TEMPLATE_PAGE, request.getLocale(  ),
                    modelTemplateXpagePollingStationSearch );
        }
        else
        {
            strContent = I18nService.getLocalizedString( KEY_MESSAGE_PAGE_NOT_FOUND, request.getLocale(  ) );
            strPathLabel = AppPropertiesService.getProperty( PROPERTY_ERROR_LABEL );
            strTitle = AppPropertiesService.getProperty( PROPERTY_ERROR_TITLE );
        }

        page.setContent( template.getHtml(  ) );
        page.setPathLabel( strPathLabel );
        page.setTitle( strTitle );

        return page;
    }

    /**
     * Returns the action of the form. By convention, fields referring to an
     * action have a predefined prefix.
     *
     * @param request the http request
     * @return the first action found.
     */
    private String getAction( HttpServletRequest request )
    {
        String strResult = null;

        Enumeration enumNames = request.getParameterNames(  );

        while ( ( strResult == null ) && enumNames.hasMoreElements(  ) )
        {
            String strName = (String) enumNames.nextElement(  );

            if ( strName.startsWith( PARAMETER_ACTION ) )
            {
                strResult = strName;
            }
        }

        return strResult;
    }

    /**
     * Returns the search form
     *
     * @param request the request
     * @param strMessage the message to display
     * @param bReset indicates whether the form must be reset.
     * @return the Html code for the search form
     */
    private String getSearchPage( HttpServletRequest request, String strMessage, boolean bReset )
    {
        // Values of the form fields
        String strStreetNumber;
        String strStreetSuffix;
        String strStreetType;
        String strStreetName;
        String strStreetUrbanDistrict;

        if ( bReset )
        {
            strStreetNumber = CONSTANT_EMPTY_STRING;
            strStreetSuffix = CONSTANT_SPACE_CHAR;
            strStreetType = CONSTANT_DEFAULT_COMBO_ID;
            strStreetName = CONSTANT_EMPTY_STRING;
            strStreetUrbanDistrict = CONSTANT_DEFAULT_COMBO_ID;
        }
        else
        {
            // Read request parameters
            strStreetNumber = request.getParameter( PARAMETER_STREET_NUMBER );

            if ( strStreetNumber == null )
            {
                strStreetNumber = CONSTANT_EMPTY_STRING;
            }

            strStreetSuffix = request.getParameter( PARAMETER_NUMBER_SUFFIX );

            if ( strStreetSuffix == null )
            {
                strStreetSuffix = CONSTANT_SPACE_CHAR;
            }

            strStreetType = request.getParameter( PARAMETER_STREET_TYPE );

            if ( strStreetType == null )
            {
                strStreetType = CONSTANT_DEFAULT_COMBO_ID;
            }

            strStreetName = request.getParameter( PARAMETER_STREET_NAME );

            if ( strStreetName == null )
            {
                strStreetName = CONSTANT_EMPTY_STRING;
            }

            strStreetUrbanDistrict = request.getParameter( PARAMETER_STREET_URBAN_DISTRICT );

            if ( strStreetUrbanDistrict == null )
            {
                strStreetUrbanDistrict = CONSTANT_DEFAULT_COMBO_ID;
            }
        }

        HashMap model = new HashMap(  );

        // STREET NUMBER
        model.put( MARK_STREET_NUMBER, strStreetNumber );

        // Fill in combo STREET SUFFIX
        model.put( MARK_NUMBER_SUFFIX, strStreetSuffix );

        ReferenceList listSuffixes = NumberSuffixtHome.getNumberSuffixList( _plugin );
        model.put( MARK_SUFFIX_LIST, listSuffixes );

        // Fill in combo STREET TYPE
        model.put( MARK_STREET_TYPE, strStreetType );

        String strOtherChoice = I18nService.getLocalizedString( KEY_SEARCHPAGE_STREET_TYPE_OTHER_CHOICE,
                request.getLocale(  ) );
        String strMsgSelectType = I18nService.getLocalizedString( KEY_SEARCHPAGE_SELECT_STREET_TYPE,
                request.getLocale(  ) );
        ReferenceList listStreetType = StreetTypeHome.getStreetTypeList( _plugin, strOtherChoice, strMsgSelectType );
        model.put( MARK_STREET_TYPE_LIST, listStreetType );

        // STREET NAME
        model.put( MARK_STREET_NAME, strStreetName );

        // Value in combo STREET URBAN DISTRICT
        model.put( MARK_STREET_URBAN_DISTRICT, strStreetUrbanDistrict );

        model.put( MARK_PLUGIN_NAME, _plugin.getName(  ) );
        model.put( MARK_URL_JSP, _strUrlJsp );
        model.put( MARK_MESSAGE, strMessage );

        HtmlTemplate tSearchPage = AppTemplateService.getTemplate( TEMPLATE_SEARCH, request.getLocale(  ), model );

        return tSearchPage.getHtml(  );
    }

    /**
     * Returns the list of street names corresponding to a street name search.
     *
     * @param request the http request
     * @param bUseRequest boolean indicating if the street number, street suffix, street type, street name and street urban district
     * must be read from request parameters. It is used when there is just one address found but no polling station corresponding
     * @param strMessage the message to display (used when there is just one address found but no polling station corresponding)
     * @param strStreetNumber the street number
     * @param strStreetSuffixId the street suffix
     * @param strStreetTypeId the type of the street
     * @param strStreetName the name of the street
     * @param strStreetUrbanDistrict the street urban district
     * @return the html code for the list of results found
     */
    private String getStreetNames( HttpServletRequest request, boolean bUseRequest, String strMessage,
        String strStreetNumber, String strStreetSuffixId, String strStreetTypeId, String strStreetName,
        String strStreetUrbanDistrict )
    {
        String strStreetNumberNew = strStreetNumber;
        String strStreetSuffixIdNew = strStreetSuffixId;
        String strStreetTypeIdNew = strStreetTypeId;
        String strStreetNameNew = strStreetName;
        String strStreetUrbanDistrictNew = strStreetUrbanDistrict;

        if ( bUseRequest )
        {
            // Read request parameters
            strStreetNumberNew = request.getParameter( PARAMETER_STREET_NUMBER );
            strStreetSuffixIdNew = request.getParameter( PARAMETER_NUMBER_SUFFIX );
            strStreetTypeIdNew = request.getParameter( PARAMETER_STREET_TYPE );
            strStreetNameNew = request.getParameter( PARAMETER_STREET_NAME );
            strStreetUrbanDistrictNew = request.getParameter( PARAMETER_STREET_URBAN_DISTRICT );
        }

        // Check text fields if we come from the first page
        // required
        if ( ( strStreetUrbanDistrictNew == null ) || CONSTANT_DEFAULT_COMBO_ID.equals( strStreetUrbanDistrictNew ) )
        {
            String strMessageSearchPage = I18nService.getLocalizedString( KEY_SEARCHPAGE_STREET_URBAN_DISTRICT_REQUIRED,
                    request.getLocale(  ) );

            return getSearchPage( request, strMessageSearchPage, false );
        }

        if ( ( strStreetNumberNew == null ) || CONSTANT_EMPTY_STRING.equals( strStreetNumberNew.trim(  ) ) )
        {
            String strMessageSearchPage = I18nService.getLocalizedString( KEY_SEARCHPAGE_STREET_NUMBER_REQUIRED,
                    request.getLocale(  ) );

            return getSearchPage( request, strMessageSearchPage, false );
        }

        if ( ( strStreetTypeIdNew == null ) || CONSTANT_DEFAULT_COMBO_ID.equals( strStreetTypeIdNew ) )
        {
            String strMessageSearchPage = I18nService.getLocalizedString( KEY_SEARCHPAGE_STREET_TYPE_REQUIRED,
                    request.getLocale(  ) );

            return getSearchPage( request, strMessageSearchPage, false );
        }

        if ( ( strStreetNameNew == null ) || CONSTANT_EMPTY_STRING.equals( strStreetNameNew.trim(  ) ) )
        {
            String strMessageSearchPage = I18nService.getLocalizedString( KEY_SEARCHPAGE_STREET_NAME,
                    request.getLocale(  ) );

            return getSearchPage( request, strMessageSearchPage, false );
        }

        // length
        if ( ( strStreetNameNew != null ) && ( strStreetNameNew.length(  ) < _nStreetNameMinLength ) )
        {
            String[] strParameters = new String[1];
            strParameters[0] = String.valueOf( _nStreetNameMinLength );

            String message = I18nService.getLocalizedString( KEY_SEARCHPAGE_STREET_NAME_HINT, strParameters,
                    request.getLocale(  ) );

            return getSearchPage( request, message, false );
        }

        // The street name must not contain letters and must be > 0
        // trim the number
        strStreetNumberNew = strStreetNumberNew.trim(  );

        try
        {
            int nStreetNumber = Integer.parseInt( strStreetNumberNew );

            if ( !( nStreetNumber > 0 ) )
            {
                String strMessageSearchPage = I18nService.getLocalizedString( KEY_SEARCHPAGE_STREET_NUMBER_POSITIVE,
                        request.getLocale(  ) );

                return getSearchPage( request, strMessageSearchPage, false );
            }
        }
        catch ( NumberFormatException e )
        {
            String strMessageSearchPage = I18nService.getLocalizedString( KEY_SEARCHPAGE_STREET_NUMBER_FORMAT,
                    request.getLocale(  ) );

            return getSearchPage( request, strMessageSearchPage, false );
        }

        // SEARCH the streets
        Collection<Street> listStreet;

        if ( strStreetTypeIdNew.equals( AppPropertiesService.getProperty( PROPERTY_STREET_TYPE_OTHER_CHOICE_ID ) ) )
        {
            listStreet = StreetHome.findStreetList( _plugin, strStreetNumberNew, strStreetSuffixIdNew, null,
                    strStreetNameNew, strStreetUrbanDistrictNew );
        }
        else
        {
            listStreet = StreetHome.findStreetList( _plugin, strStreetNumberNew, strStreetSuffixIdNew,
                    strStreetTypeIdNew, strStreetNameNew, strStreetUrbanDistrictNew );
        }

        if ( listStreet.isEmpty(  ) )
        {
            String strMessageSearchPage = I18nService.getLocalizedString( KEY_SEARCHPAGE_NO_RESULT,
                    request.getLocale(  ) );

            return getSearchPage( request, strMessageSearchPage, false );
        }

        // search
        else
        {
            // Go directly to the results page
            if ( ( listStreet.size(  ) == 1 ) && ( CONSTANT_EMPTY_STRING.equals( strMessage ) ) )
            {
                String strStreetCode = null;
                Iterator<Street> iterator = listStreet.iterator(  );
                Street street = iterator.next(  );
                strStreetCode = street.getStreetId(  );
                AppLogService.debug( "Go directly to the results page" );

                return getResults( request, false, strStreetCode, strStreetNumberNew, strStreetSuffixIdNew,
                    strStreetTypeIdNew, strStreetNameNew, strStreetUrbanDistrictNew );
            }
        }

        String strStreetSuffix = CONSTANT_EMPTY_STRING;

        if ( ( strStreetSuffixIdNew != null ) && !( CONSTANT_EMPTY_STRING.equals( strStreetSuffixIdNew ) ) &&
                !( AppPropertiesService.getProperty( PROPERTY_NO_NUMBER_SUFFIX_CHOOSEN_ID ).equals( strStreetSuffixIdNew ) ) )
        {
            strStreetSuffix = ( NumberSuffixtHome.findByPrimaryKey( _plugin, strStreetSuffixIdNew ) ).getNumberSuffixLabel(  );
        }

        HashMap model = new HashMap(  );
        model.put( MARK_STREET_NUMBER, strStreetNumberNew );

        StreetType streetType;

        if ( AppPropertiesService.getProperty( PROPERTY_STREET_TYPE_OTHER_CHOICE_ID ).equals( strStreetTypeIdNew ) )
        {
            streetType = new StreetType(  );
            streetType.setShortStreetType( strStreetTypeIdNew );

            String strStreetTypeDefaultLabel = I18nService.getLocalizedString( KEY_SEARCHPAGE_STREET_TYPE_OTHER_CHOICE,
                    request.getLocale(  ) );
            streetType.setLongStreetType( strStreetTypeDefaultLabel );
        }
        else
        {
            streetType = StreetTypeHome.findByPrimaryKey( _plugin, strStreetTypeIdNew );
        }

        model.put( MARK_STREET_TYPE, streetType );
        model.put( MARK_STREET_NAME, strStreetNameNew );
        model.put( MARK_STREET_URBAN_DISTRICT, strStreetUrbanDistrictNew );
        model.put( MARK_STREET_LIST, listStreet );
        model.put( MARK_NUMBER_SUFFIX_ID, strStreetSuffixIdNew );
        model.put( MARK_NUMBER_SUFFIX, strStreetSuffix );
        model.put( MARK_PLUGIN_NAME, _plugin.getName(  ) );
        model.put( MARK_URL_JSP, _strUrlJsp );
        model.put( MARK_MESSAGE, strMessage );

        HtmlTemplate tStreetResults = AppTemplateService.getTemplate( TEMPLATE_STREET_LIST, request.getLocale(  ), model );

        return tStreetResults.getHtml(  );
    }

    /**
     * Returns the polling station for a selected street.
     *
     * @param request the http request
     * @param bUseRequest boolean indicating if the street code, number and suffix
     * must be read from request parameters. It is used when the street list
     * was skipped because there was only one record.
     * @param strStreetCode street code (id) if bUseRequest is false.
     * @param strStreetNumber street number name if bUseRequest is false.
     * @param strSuffixCode street suffix if bUseRequest is false.
     * @param strStreetTypeId the type of the street
     * @param strStreetName the name of the street
     * @param strStreetUrbanDistrict street's urban district
     *
     * @return the html code for the list of results found
     */
    private String getResults( HttpServletRequest request, boolean bUseRequest, String strStreetCode,
        String strStreetNumber, String strSuffixCode, String strStreetTypeId, String strStreetName,
        String strStreetUrbanDistrict )
    {
        String strStreetCodeNew = strStreetCode;
        String strStreetNumberNew = strStreetNumber;
        String strSuffixCodeNew = strSuffixCode;
        String strStreetTypeIdNew = strStreetTypeId;
        String strStreetNameNew = strStreetName;
        String strStreetUrbanDistrictNew = strStreetUrbanDistrict;

        String strMessage = CONSTANT_EMPTY_STRING;

        if ( bUseRequest )
        {
            strStreetCodeNew = request.getParameter( PARAMETER_STREET_CODE );
            strStreetNumberNew = request.getParameter( PARAMETER_STREET_NUMBER );
            strSuffixCodeNew = request.getParameter( PARAMETER_NUMBER_SUFFIX );
            strStreetTypeIdNew = request.getParameter( PARAMETER_STREET_TYPE );
            strStreetNameNew = request.getParameter( PARAMETER_STREET_NAME );
            strStreetUrbanDistrictNew = request.getParameter( PARAMETER_STREET_URBAN_DISTRICT );
        }

        // if no address has been selected
        if ( ( strStreetCodeNew == null ) && bUseRequest )
        {
            String strMessageStreetList = I18nService.getLocalizedString( KEY_STREET_LIST_SELECT_ADDRESS,
                    request.getLocale(  ) );

            return getStreetNames( request, false, strMessageStreetList, strStreetNumberNew, strSuffixCodeNew,
                strStreetTypeIdNew, strStreetNameNew, strStreetUrbanDistrictNew );
        }

        // search the street in database
        Street street = StreetHome.findByPrimaryKey( _plugin, strStreetCodeNew, strStreetUrbanDistrictNew );

        if ( AppPropertiesService.getProperty( PROPERTY_NO_NUMBER_SUFFIX_CHOOSEN_ID ).equals( strSuffixCodeNew ) )
        {
            strSuffixCodeNew = null;
        }

        // search for the address and polling station corresponding to the street
        Address address = AddressHome.findByStreetIdAndNumberSuffix( _plugin, street.getStreetId(  ),
                strStreetNumberNew, strSuffixCodeNew, street.getStreetUrbanDistrict( ) );

        if ( ( strSuffixCodeNew == null ) ||
                AppPropertiesService.getProperty( PROPERTY_NO_NUMBER_SUFFIX_CHOOSEN_ID ).equals( strSuffixCodeNew ) )
        {
            strSuffixCodeNew = CONSTANT_EMPTY_STRING;
        }

        PollingStation pollingStation = null;

        if ( address == null )
        {
            strMessage = I18nService.getLocalizedString( KEY_STREET_LIST_NO_PERIMETER_ALL, request.getLocale(  ) );
        }

        else
        {
            int nPollingStationId = AddressHome.findPollingStationIdByAddressId( _plugin, address.getId(  ) );
            pollingStation = PollingStationHome.findByPrimaryKey( _plugin, nPollingStationId );

            if ( pollingStation == null )
            {
                strMessage = I18nService.getLocalizedString( KEY_STREET_LIST_NO_PERIMETER_ALL, request.getLocale(  ) );
            }
        }

        HashMap model = new HashMap(  );
        model.put( MARK_PLUGIN_NAME, _plugin.getName(  ) );
        model.put( MARK_URL_JSP, _strUrlJsp );
        model.put( MARK_MESSAGE, strMessage );
        model.put( MARK_CITY, I18nService.getLocalizedString( KEY_RESULT_CITY_NAME, request.getLocale(  ) ) );

        model.put( MARK_POLLING_STATION, pollingStation );

        model.put( MARK_NUMBER_SUFFIX, strSuffixCodeNew );
        model.put( MARK_STREET_NUMBER, strStreetNumberNew );
        model.put( MARK_STREET_TYPE, strStreetTypeIdNew );
        model.put( MARK_STREET_NAME, strStreetNameNew );
        model.put( MARK_STREET_URBAN_DISTRICT, strStreetUrbanDistrictNew );

        if ( ( address != null ) && ( address.getAddrNumberSuffix(  ) != null ) )
        {
            model.put( MARK_NUMBER_SUFFIX_IN_DATABASE, address.getAddrNumberSuffix(  ) );
        }
        else
        {
            if ( CONSTANT_EMPTY_STRING.equals( strSuffixCodeNew ) )
            {
                model.put( MARK_NUMBER_SUFFIX_IN_DATABASE, CONSTANT_EMPTY_STRING );
            }
            else
            {
                model.put( MARK_NUMBER_SUFFIX_IN_DATABASE,
                    NumberSuffixtHome.findByPrimaryKey( _plugin, strSuffixCodeNew ).getNumberSuffixLabel(  ) );
            }
        }

        model.put( MARK_STREET_URBAN_DISTRICT_IN_DATABASE, street.getStreetUrbanDistrict(  ) );
        model.put( MARK_STREET_NAME_IN_DATABASE, street.getLongStreetName(  ) );

        
        Calendar calCurrent = new GregorianCalendar( );
        Calendar calStart = new GregorianCalendar( );
        Calendar calEnd = new GregorianCalendar( );
               
        if ( calCurrent.get( Calendar.MONTH ) < Calendar.FEBRUARY )
        {
        	calStart.set( Calendar.YEAR, calCurrent.get( Calendar.YEAR ) - 1 );
        	calEnd.set( Calendar.YEAR, calCurrent.get( Calendar.YEAR ) );
        }
        else
        {
        	calStart.set( Calendar.YEAR, calCurrent.get( Calendar.YEAR ) );
        	calEnd.set( Calendar.YEAR, calCurrent.get( Calendar.YEAR ) + 1 );
        }
        
        calStart.set( Calendar.MONTH, Calendar.MARCH );
        calStart.set( Calendar.DAY_OF_MONTH, calStart.getMinimum( Calendar.DAY_OF_MONTH ) );
        
        calEnd.set( Calendar.MONTH, Calendar.FEBRUARY );
        int nLastDayFebruary = calEnd.getActualMaximum( Calendar.DAY_OF_MONTH );
        calEnd.set( Calendar.DAY_OF_MONTH, nLastDayFebruary );
        
        DateFormat df = new SimpleDateFormat( AppPropertiesService.getProperty( PROPERTY_DATEVALIDITY_PATTERN ) );
        String[] strParameters = new String[2];
        strParameters[0] = df.format( calStart.getTime( ) ) ;
        strParameters[1] = df.format( calEnd.getTime( ) ) ;

        String strInfo = I18nService.getLocalizedString( KEY_RESULT_INFO, strParameters, request.getLocale(  ) );
        model.put( MARK_MESSAGE_RESULT_INFO, strInfo );
      
        HtmlTemplate tPollingStationResults = AppTemplateService.getTemplate( TEMPLATE_RESULTS, request.getLocale(  ),
                model );

        return tPollingStationResults.getHtml(  );
    }
    
    
}
