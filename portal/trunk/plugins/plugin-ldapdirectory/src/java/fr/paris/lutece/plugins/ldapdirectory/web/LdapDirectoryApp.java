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
package fr.paris.lutece.plugins.ldapdirectory.web;

import fr.paris.lutece.plugins.ldapdirectory.business.LdapEntity;
import fr.paris.lutece.plugins.ldapdirectory.util.LdapBrowser;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class manages LdapDirectory Page
 */
public class LdapDirectoryApp implements XPageApplication
{
    //Constants
    private static final String MARK_COMBO_TYPES = "combo_types";
    private static final String TEMPLATE_RESULT_TEMPLATE = "/skin/plugins/ldapdirectory/result_ldap.html";
    private static final String PROPERTY_LDAP_SEARCH_PAGE_URL = "ldapdirectory.pageSearch.baseUrl";
    private static final String PARAM_ACTION = "action";
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_GIVEN_NAME = "given_name";
    private static final String PARAMETER_COMPLETE_NAME = "complete_name";
    private static final String PARAMETER_SOCIETY = "society";
    private static final String PARAMETER_SERVICE = "service";
    private static final String PARAMETER_PHONE_NUMBER = "phone_number";
    private static final String PARAMETER_RNIS_NUMBER = "rnis_number";
    private static final String PARAMETER_MAIL = "mail";
    private static final String PARAMETER_TITLE = "title";
    private static final String PARAMETER_FLOOR = "floor";
    private static final String PARAMETER_ROOM_NUMBER = "room_number";
    private static final String PARAMETER_POSTAL_ADDRESS = "postal_address";
    private static final String PARAMETER_POSTAL_CODE = "postal_code";
    private static final String PARAMETER_TOWN = "town";
    private static final String PARAMETER_DEPARTMENT = "department";
    private static final String PARAMETER_MODIFICATION_DATE = "modification_date";
    private static final String PARAMETER_TYPE = "type";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_NB_ITEMS_PER_PAGE = "items_per_page";
    private static final String PROPERTY_RESULTS_PER_PAGE = "ldapdirectory.nb.docs.per.page";
    private static final String PROPERTY_DEFAULT_HOME_TYPE = "ldapdirectory.home.page.type";
    private static final String PROPERTY_LABEL = "ldapdirectory.field";
    private static final String DEFAULT_RESULTS_PER_PAGE = "20";
    private static final String DEFAULT_PAGE_INDEX = "1";
    private static final String MARK_RESULTS_LIST = "results_list";
    private static final String ACTION_FIND = "find";
    private static final String ACTION_CHOOSE = "choose";
    private static final String MARK_USER_LIST = "user_list";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_ERROR = "error";
    private static final String PROPERTY_TYPE = "ldapdirectory.type";
    private static final String PROPERTY_FIELD = "ldapdirectory.field";
    private static final String PROPERTY_PAGE_TITLE = "ldapdirectory.pageTitle";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ldapdirectory.pagePathLabel";
    private static final String PROPERTY_TYPE_CHOOSE = "ldapdirectory.type.choose";
    private static final String CONSTANT_WILDCARD = "*";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String PROPERTY_INPUT_ERROR_MESSAGE_TITLE = "ldapdirectory.siteMessage.invalidInput.messageTitle";
    private static final String PROPERTY_INPUT_ERROR_MESSAGE = "ldapdirectory.siteMessage.invalidInput.message";
    private static final String PROPERTY_NO_RESULT_MESSAGE= "ldapdirectory.siteMessage.noResult.messageTitle";
    private static final String PROPERTY_NO_RESULT_TITLE= "ldapdirectory.siteMessage.noResult.message";
            
    
    /**
     * getPage
     * @return XPage
     * @param request HttpServletRequest
     * @param nMode int
     * @param plugin Plugin
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException The exception which handles front office message notices
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    throws SiteMessageException
    {
        XPage page = new XPage(  );
        
        page.setTitle( I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, request.getLocale(  ) ) );
        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, request.getLocale(  ) ) );
        
        String strContent = null;
        String strAction = request.getParameter( PARAM_ACTION );
        String strType = request.getParameter( PARAMETER_TYPE );
        
        if ( ( strType != null ) && !strType.equals( "" ) && ( strAction != null ) &&
                ( strAction.equals( ACTION_CHOOSE ) ) )
        {
            int nType = Integer.parseInt( strType );
            HashMap model = new HashMap(  );
            model.put( MARK_COMBO_TYPES, getTypesList(  ) );
            
            HtmlTemplate template = AppTemplateService.getTemplate( getTemplate( nType ), request.getLocale(  ), model );
            strContent = template.getHtml(  );
        }
        else if ( ( strAction != null ) && ( strAction.equals( ACTION_FIND ) ) && ( strType != null ) )
        {
            
            String strName = request.getParameter( PARAMETER_NAME );
            if(strName !=null )
            {
                strName = strName.toUpperCase(  );
            }
            
            String strGivenName = request.getParameter( PARAMETER_GIVEN_NAME );
            String strCompleteName = request.getParameter( PARAMETER_COMPLETE_NAME );
            String strSociety = request.getParameter( PARAMETER_SOCIETY );
            String strService = request.getParameter( PARAMETER_SERVICE );
            String strTelephoneNumber = request.getParameter( PARAMETER_PHONE_NUMBER );
            String strRnisNumber = request.getParameter( PARAMETER_RNIS_NUMBER );
            String strMail = request.getParameter( PARAMETER_MAIL );
            String strTitle = request.getParameter( PARAMETER_TITLE );
            String strFloor = request.getParameter( PARAMETER_FLOOR );
            String strRoomNumber = request.getParameter( PARAMETER_ROOM_NUMBER );
            String strPostalAddress = request.getParameter( PARAMETER_POSTAL_ADDRESS );
            String strPostalCode = request.getParameter( PARAMETER_POSTAL_CODE );
            String strTown = request.getParameter( PARAMETER_TOWN );
            String strDepartment = request.getParameter( PARAMETER_DEPARTMENT );
            String strModificationDate = request.getParameter( PARAMETER_MODIFICATION_DATE );
            
            
            
            int nType = Integer.parseInt( strType );
            verifyFields( request ,nType );
            String strAdditionalParameter = getEntityClass( nType );
            HashMap model = new HashMap(  );
            
            LdapBrowser ldap = new LdapBrowser(  );
            List<LdapEntity> userList = ldap.getEntityList( ( strName != null ) ? strName : ( "" + CONSTANT_WILDCARD ),
                    ( strGivenName != null ) ? strGivenName : ( "" + CONSTANT_WILDCARD ),
                    ( strCompleteName != null ) ? strCompleteName : ( "" + CONSTANT_WILDCARD ),
                    ( strSociety != null ) ? strSociety : ( "" + CONSTANT_WILDCARD ),
                    ( strService != null ) ? strService : ( "" + CONSTANT_WILDCARD ),
                    ( strTelephoneNumber != null ) ? strTelephoneNumber : ( "" + CONSTANT_WILDCARD ),
                    ( strRnisNumber != null ) ? strRnisNumber : ( "" + CONSTANT_WILDCARD ),
                    ( strMail != null ) ? strMail : ( "" + CONSTANT_WILDCARD ),
                    ( strTitle != null ) ? strTitle : ( "" + CONSTANT_WILDCARD ),
                    ( strFloor != null ) ? strFloor : ( "" + CONSTANT_WILDCARD ),
                    ( strRoomNumber != null ) ? strRoomNumber : ( "" + CONSTANT_WILDCARD ),
                    ( strPostalAddress != null ) ? strPostalAddress : ( "" + CONSTANT_WILDCARD ),
                    ( strPostalCode != null ) ? strPostalCode : ( "" + CONSTANT_WILDCARD ),
                    ( strTown != null ) ? strTown : ( "" + CONSTANT_WILDCARD ),
                    ( strDepartment != null ) ? strDepartment : ( "" + CONSTANT_WILDCARD ),
                    ( strModificationDate != null ) ? strModificationDate : ( "" + CONSTANT_WILDCARD ),
                    strAdditionalParameter, nType );
            verifyNoResults( request , userList );
            model.put( MARK_USER_LIST, userList );
            
            String strNbItemPerPage = request.getParameter( PARAMETER_NB_ITEMS_PER_PAGE );
            String strDefaultNbItemPerPage = AppPropertiesService.getProperty( PROPERTY_RESULTS_PER_PAGE,
                    DEFAULT_RESULTS_PER_PAGE );
            strNbItemPerPage = ( strNbItemPerPage != null ) ? strNbItemPerPage : strDefaultNbItemPerPage;
            
            int nNbItemsPerPage = Integer.parseInt( strNbItemPerPage );
            String strCurrentPageIndex = request.getParameter( PARAMETER_PAGE_INDEX );
            strCurrentPageIndex = ( strCurrentPageIndex != null ) ? strCurrentPageIndex : DEFAULT_PAGE_INDEX;
            
            int nPageIndex = Integer.parseInt( strCurrentPageIndex );
//            int nStartIndex = ( nPageIndex - 1 ) * nNbItemsPerPage;
            
            String strLdapPageUrl = AppPropertiesService.getProperty( PROPERTY_LDAP_SEARCH_PAGE_URL );
            String strError = "";
            UrlItem url = new UrlItem( strLdapPageUrl );
            
            url.addParameter( PARAMETER_NB_ITEMS_PER_PAGE, nNbItemsPerPage );
            url.addParameter( PARAM_ACTION, ACTION_FIND );
            
            if ( strName != null )
            {
                url.addParameter( PARAMETER_NAME, strName );
            }
            
            if ( strGivenName != null )
            {
                url.addParameter( PARAMETER_GIVEN_NAME, strGivenName );
            }
            
            if ( strCompleteName != null )
            {
                url.addParameter( PARAMETER_COMPLETE_NAME, strCompleteName );
            }
            
            if ( strSociety != null )
            {
                url.addParameter( PARAMETER_SOCIETY, strSociety );
            }
            
            if ( strService != null )
            {
                url.addParameter( PARAMETER_SERVICE, strService );
            }
            
            if ( strTelephoneNumber != null )
            {
                url.addParameter( PARAMETER_PHONE_NUMBER, strTelephoneNumber );
            }
            
            if ( strRnisNumber != null )
            {
                url.addParameter( PARAMETER_RNIS_NUMBER, strRnisNumber );
            }
            
            if ( strMail != null )
            {
                url.addParameter( PARAMETER_MAIL, strMail );
            }
            
            if ( strTitle != null )
            {
                url.addParameter( PARAMETER_TITLE, strTitle );
            }
            
            if ( strFloor != null )
            {
                url.addParameter( PARAMETER_FLOOR, strFloor );
            }
            
            if ( strRoomNumber != null )
            {
                url.addParameter( PARAMETER_ROOM_NUMBER, strRoomNumber );
            }
            
            if ( strPostalAddress != null )
            {
                url.addParameter( PARAMETER_POSTAL_ADDRESS, strPostalAddress );
            }
            
            if ( strPostalCode != null )
            {
                url.addParameter( PARAMETER_POSTAL_CODE, strPostalCode );
            }
            
            if ( strTown != null )
            {
                url.addParameter( PARAMETER_TOWN, strTown );
            }
            
            if ( strDepartment != null )
            {
                url.addParameter( PARAMETER_DEPARTMENT, strDepartment );
            }
            
            if ( strModificationDate != null )
            {
                url.addParameter( PARAMETER_MODIFICATION_DATE, strModificationDate );
            }
            
            url.addParameter( PARAMETER_TYPE, strType );
            
            Paginator paginator = new Paginator( userList, nNbItemsPerPage, url.getUrl(  ), PARAMETER_PAGE_INDEX,
                    strCurrentPageIndex );
            model.put( MARK_RESULTS_LIST, paginator.getPageItems(  ) );
            model.put( MARK_NB_ITEMS_PER_PAGE, strNbItemPerPage );
            model.put( MARK_PAGINATOR, paginator );
            model.put( MARK_ERROR, strError );
            
            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RESULT_TEMPLATE, request.getLocale(  ),
                    model );
            strContent = template.getHtml(  );
        }
        else
        {
            //The default page is set to person
            int nType = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_HOME_TYPE, 2 );
            HashMap model = new HashMap(  );
            model.put( MARK_COMBO_TYPES, getTypesList(  ) );
            
            HtmlTemplate template = AppTemplateService.getTemplate( getTemplate( nType ), request.getLocale(  ), model );
            strContent = template.getHtml(  );
        }
        
        page.setContent( strContent );
        
        return page;
    }
    
    /**
     * Return the list of types
     *
     * @return the list of types
     */
    private ReferenceList getTypesList(  )
    {
        ReferenceList listTypes = new ReferenceList(  );
        listTypes.addItem( 1, AppPropertiesService.getProperty( PROPERTY_TYPE_CHOOSE ) );
        
        String strTypeText;
        int i = 1;
        
        while ( ( strTypeText = AppPropertiesService.getProperty( PROPERTY_TYPE + i + ".text" ) ) != null )
        {
            listTypes.addItem( i, strTypeText );
            i++;
        }
        
        return listTypes;
    }
    
    /**
     * Return template of generation
     * @param nIndex the index
     * @return the template
     */
    private String getTemplate( int nIndex )
    {
        String strTemplate = AppPropertiesService.getProperty( PROPERTY_TYPE + nIndex + ".template" );
        
        return strTemplate;
    }
    
    /**
     * Return template of generation
     * @param nIndex the index
     * @return the template
     */
    private String getEntityClass( int nIndex )
    {
        String strClass = AppPropertiesService.getProperty( PROPERTY_TYPE + nIndex + ".class" );
        
        return strClass;
    }
    
    /**
     * The method fetches the minimum number of characeters needed to validate the fields input
     * @param index The index of the input field
     * @return The numerical value of the minimum
     */
    private int getMinimalValue( int index , int nType)
    {
        int nMinimalValue = AppPropertiesService.getPropertyInt( PROPERTY_FIELD + index + ".minimumCaracterNumber.type"+nType, 0 );
        return nMinimalValue;
    }
    
    /**
     * Fetches the description of the input field
     * @param index The index of the input field
     * @return The input field label
     */
    private String getFieldLabel( int index )
    {
        String strLabel = AppPropertiesService.getProperty( PROPERTY_LABEL + index + ".label" );
        
        return strLabel;
    }
    
    /**
     * Fetches a value corresponding to an input in the request by the corresponding parameter which is specified in the properties file
     * @param request The request
     * @param nIndex The index of the input field
     * @return The string representing the input of a specific field
     */
    private String getInputField( HttpServletRequest request, int nIndex )
    {
        String strLabel = AppPropertiesService.getProperty( PROPERTY_LABEL + nIndex + ".param" );
        String strInputFieldLabel = request.getParameter( strLabel );
        
        return strInputFieldLabel;
    }
    
    /**
     * Verifies whether the size of the input field suits the rule defined for the minimum length.
     * @param request The Http request
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException The Exception which will cause the stop message in the front office
     */
    private void verifyFields( HttpServletRequest request , int nType )
    throws SiteMessageException
    {
        int i = 1;
        String strFieldText;
        
        while ( ( strFieldText = AppPropertiesService.getProperty( PROPERTY_FIELD + i + ".param" ) ) != null )
        {
            int nMinimalCharacters = getMinimalValue( i ,nType );
            String strInputField = getInputField( request, i );
            
            if ( ( strInputField != null ) && ( strInputField.length(  ) < nMinimalCharacters ) )
            {
                String strField = I18nService.getLocalizedString( getFieldLabel( i ), request.getLocale(  ) );
                
                SiteMessageService.setMessage( request, PROPERTY_INPUT_ERROR_MESSAGE,
                        new String[] { strField, "" + nMinimalCharacters }, PROPERTY_INPUT_ERROR_MESSAGE_TITLE, null, "",
                        SiteMessage.TYPE_STOP );
            }
            
            i++;
        }
    }
    
  
    private void verifyNoResults( HttpServletRequest request , List<LdapEntity> list )
    throws SiteMessageException
    {
        if( list.size()==0 )
        {
            SiteMessageService.setMessage( request, PROPERTY_NO_RESULT_MESSAGE,
                    new String[] { "" }, PROPERTY_NO_RESULT_TITLE, null, "",
                    SiteMessage.TYPE_STOP );
        }

    }
    
}
