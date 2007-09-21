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
package fr.paris.lutece.plugins.ldapdirectory.util;

import fr.paris.lutece.plugins.ldapdirectory.business.LdapEntity;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.List;

import javax.naming.CommunicationException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;


/**
 * LDAP browser : to retrieve information from LDAP repository
 */
public class LdapBrowser
{
    //ldap
    private static final String PROPERTY_INITIAL_CONTEXT_PROVIDER = "ldapdirectory.ldap.initialContextProvider";
    private static final String PROPERTY_PROVIDER_URL = "ldapdirectory.ldap.connectionUrl";
    private static final String PROPERTY_BIND_DN = "ldapdirectory.ldap.connectionName";
    private static final String PROPERTY_BIND_PASSWORD = "ldapdirectory.ldap.connectionPassword";
    private static final String PROPERTY_USER_DN_SEARCH_BASE = "ldapdirectory.ldap.userBase";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_SN = "ldapdirectory.ldap.userSearch.criteria.sn";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_GIVEN_NAME = "ldapdirectory.ldap.userSearch.criteria.givenname";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_COMPLETE_NAME = "ldapdirectory.ldap.userSearch.criteria.completename";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_SOCIETY = "ldapdirectory.ldap.userSearch.criteria.society";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_SERVICE = "ldapdirectory.ldap.userSearch.criteria.service";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_PHONE_NUMBER = "ldapdirectory.ldap.userSearch.criteria.telephonenumber";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_RNIS_NUMBER = "ldapdirectory.ldap.userSearch.criteria.rnisnumber";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_MAIL = "ldapdirectory.ldap.userSearch.criteria.mail";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_TITLE = "ldapdirectory.ldap.userSearch.criteria.title";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_FLOOR = "ldapdirectory.ldap.userSearch.criteria.floor";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_ROOM_NUMBER = "ldapdirectory.ldap.userSearch.criteria.roomnumber";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_POSTAL_ADDRESS = "ldapdirectory.ldap.userSearch.criteria.postaladdress";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_POSTAL_CODE = "ldapdirectory.ldap.userSearch.criteria.postalCode";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_TOWN = "ldapdirectory.ldap.userSearch.criteria.town";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_DEPARTMENT = "ldapdirectory.ldap.userSearch.criteria.department";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_MODIFICATION_DATE = "ldapdirectory.ldap.userSearch.criteria.modificationdate";
    private static final String PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_OBJECT_CLASS = "ldapdirectory.ldap.userSearch.criteria.objectclass";
    private static final String PROPERTY_USER_SUBTREE = "ldapdirectory.ldap.userSubtree";
    private static final String PROPERTY_DN_ATTRIBUTE_SURNAME = "ldapdirectory.ldap.dn.attributeName.sn";
    private static final String PROPERTY_DN_ATTRIBUTE_GIVEN_NAME = "ldapdirectory.ldap.dn.attributeName.givenname";
    private static final String PROPERTY_DN_ATTRIBUTE_COMPLETE_NAME = "ldapdirectory.ldap.dn.attributeName.completename";
    private static final String PROPERTY_DN_ATTRIBUTE_SOCIETY = "ldapdirectory.ldap.dn.attributeName.society";
    private static final String PROPERTY_DN_ATTRIBUTE_SERVICE = "ldapdirectory.ldap.dn.attributeName.service";
    private static final String PROPERTY_DN_ATTRIBUTE_TELEPHONE_NUMBER = "ldapdirectory.ldap.dn.attributeName.telephonenumber";
    private static final String PROPERTY_DN_ATTRIBUTE_RNIS_NUMBER = "ldapdirectory.ldap.dn.attributeName.rnisnumber";
    private static final String PROPERTY_DN_ATTRIBUTE_EMAIL = "ldapdirectory.ldap.dn.attributeName.mail";
    private static final String PROPERTY_DN_ATTRIBUTE_TITLE = "ldapdirectory.ldap.dn.attributeName.title";
    private static final String PROPERTY_DN_ATTRIBUTE_FLOOR = "ldapdirectory.ldap.dn.attributeName.floor";
    private static final String PROPERTY_DN_ATTRIBUTE_ROOM_NUMBER = "ldapdirectory.ldap.dn.attributeName.roomnumber";
    private static final String PROPERTY_DN_ATTRIBUTE_POSTAL_ADDRESS = "ldapdirectory.ldap.dn.attributeName.postaladdress";
    private static final String PROPERTY_DN_ATTRIBUTE_POSTAL_CODE = "ldapdirectory.ldap.dn.attributeName.postalCode";
    private static final String PROPERTY_DN_ATTRIBUTE_TOWN = "ldapdirectory.ldap.dn.attributeName.town";
    private static final String PROPERTY_DN_ATTRIBUTE_DEPARTMENT = "ldapdirectory.ldap.dn.attributeName.department";
    private static final String PROPERTY_DN_ATTRIBUTE_MODIFICATION_DATE = "ldapdirectory.ldap.dn.attributeName.modificationdate";
    private static final String PROPERTY_DN_ATTRIBUTE_OBJECT_CLASS = "ldapdirectory.ldap.dn.attributeName.objectclass";
    private static final String ATTRIBUTE_SURNAME = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_SURNAME );
    private static final String ATTRIBUTE_OBJECT_CLASS = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_OBJECT_CLASS );
    private static final String ATTRIBUTE_GIVEN_NAME = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_GIVEN_NAME );
    private static final String ATTRIBUTE_COMPLETE_NAME = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_COMPLETE_NAME );
    private static final String ATTRIBUTE_SOCIETY = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_SOCIETY );
    private static final String ATTRIBUTE_SERVICE = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_SERVICE );
    private static final String ATTRIBUTE_TELEPHONE_NUMBER = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_TELEPHONE_NUMBER );
    private static final String ATTRIBUTE_RNIS_NUMBER = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_RNIS_NUMBER );
    private static final String ATTRIBUTE_EMAIL = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_EMAIL );
    private static final String ATTRIBUTE_TITLE = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_TITLE );
    private static final String ATTRIBUTE_FLOOR = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_FLOOR );
    private static final String ATTRIBUTE_ROOM_NUMBER = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_ROOM_NUMBER );
    private static final String ATTRIBUTE_POSTAL_ADDRESS = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_POSTAL_ADDRESS );
    private static final String ATTRIBUTE_POSTAL_CODE = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_POSTAL_CODE );
    private static final String ATTRIBUTE_TOWN = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_TOWN );
    private static final String ATTRIBUTE_DEPARTMENT = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_DEPARTMENT );
    private static final String ATTRIBUTE_MODIFICATION_DATE = AppPropertiesService.getProperty( PROPERTY_DN_ATTRIBUTE_MODIFICATION_DATE );
    private static final String PROPERTY_TYPE_PERSON = "person";
    private static final String PROPERTY_TYPE_ROOM = "room";
    private static final String PROPERTY_TYPE_SERVICE = "organizationalunit";
    private static final String PROPERTY_TYPE_UNKNOWN = "unknown";

    /**
     * Search controls for the user entry search
     */
    private SearchControls _scUserSearchControls;

    /**
     * Context opened on the LDAP server
     */
    private DirContext _context;

    /**
     * The class responsible to browse the directory
     */
    public LdapBrowser(  )
    {
    }

    /**
     * Returns a list of users corresponding to the given parameters.
     * An empty parameter is remplaced by the wildcard (*)
     * @return the LdapUser list
     * @param strValueName The name
     * @param strValueGivenName The given name
     * @param strValueCompleteName The complete name
     * @param strValueSociety Society
     * @param strValueService Service
     * @param strValueTelephoneNumber Phone number
     * @param strValueRnisNumber Rnis number
     * @param strValueMail Mail
     * @param strValueTitle Title
     * @param strValueFloor Floor
     * @param strValueRoomNumber Room number
     * @param strValuePostalAddress Postal Address
     * @param strValuePostalCode Postal Code
     * @param strValueTown Town
     * @param strValueDepartment Department
     * @param strValueModificationDate Modification date
     * @param strValueAdditionalParameter Additional param used to determine search type
     * @param nType The type of search
     */
    public List<LdapEntity> getEntityList( String strValueName, String strValueGivenName, String strValueCompleteName,
        String strValueSociety, String strValueService, String strValueTelephoneNumber, String strValueRnisNumber,
        String strValueMail, String strValueTitle, String strValueFloor, String strValueRoomNumber,
        String strValuePostalAddress, String strValuePostalCode, String strValueTown, String strValueDepartment,
        String strValueModificationDate, String strValueAdditionalParameter, int nType )
    {
        ArrayList<LdapEntity> userList = new ArrayList<LdapEntity>(  );
        SearchResult sr = null;
        Object[] messageFormatParam = new Object[17];
        String[] messageFormatFilter = new String[17];

        start(  );

        messageFormatParam[0] = checkSyntax( strValueName );
        messageFormatParam[1] = checkSyntax( strValueGivenName );
        messageFormatParam[2] = checkSyntax( strValueCompleteName );
        messageFormatParam[3] = checkSyntax( strValueSociety );
        messageFormatParam[4] = checkSyntax( strValueService );
        messageFormatParam[5] = checkSyntax( strValueTelephoneNumber );
        messageFormatParam[6] = checkSyntax( strValueRnisNumber );
        messageFormatParam[7] = checkSyntax( strValueMail );
        messageFormatParam[8] = checkSyntax( strValueTitle );
        messageFormatParam[9] = checkSyntax( strValueFloor );
        messageFormatParam[10] = checkSyntax( strValueRoomNumber );
        messageFormatParam[11] = checkSyntax( strValuePostalAddress );
        messageFormatParam[12] = checkSyntax( strValuePostalCode );
        messageFormatParam[13] = checkSyntax( strValueTown );
        messageFormatParam[14] = checkSyntax( strValueDepartment );
        messageFormatParam[15] = checkSyntax( strValueModificationDate );
        messageFormatParam[16] = checkSyntax( strValueAdditionalParameter );

        messageFormatFilter[0] = getUserDnSearchFilterByCriteriaSn(  );
        messageFormatFilter[1] = getUserDnSearchFilterByCriteriaGivenname(  );
        messageFormatFilter[2] = getUserDnSearchFilterByCriteriaCompleteName(  );
        messageFormatFilter[3] = getUserDnSearchFilterByCriteriaSociety(  );
        messageFormatFilter[4] = getUserDnSearchFilterByCriteriaService(  );
        messageFormatFilter[5] = getUserDnSearchFilterByCriteriaTelephoneNumber(  );
        messageFormatFilter[6] = getUserDnSearchFilterByCriteriaRnisNumber(  );
        messageFormatFilter[7] = getUserDnSearchFilterByCriteriaMail(  );
        messageFormatFilter[8] = getUserDnSearchFilterByCriteriaTitle(  );
        messageFormatFilter[9] = getUserDnSearchFilterByCriteriaFloor(  );
        messageFormatFilter[10] = getUserDnSearchFilterByCriteriaRoomNumber(  );
        messageFormatFilter[11] = getUserDnSearchFilterByCriteriaPostalAddress(  );
        messageFormatFilter[12] = getUserDnSearchFilterByCriteriaPostalCode(  );
        messageFormatFilter[13] = getUserDnSearchFilterByCriteriaTown(  );
        messageFormatFilter[14] = getUserDnSearchFilterByCriteriaDepartment(  );
        messageFormatFilter[15] = getUserDnSearchFilterByCriteriaModificationDate(  );

        messageFormatFilter[16] = getUserDnSearchFilterByCriteriaClass(  );

        String strUserSearchFilter = buildRequest( messageFormatFilter, messageFormatParam );

        try
        {
            NamingEnumeration userResults = LdapUtil.searchUsers( _context, strUserSearchFilter,
                    getUserDnSearchBase(  ), "", _scUserSearchControls );

            while ( ( userResults != null ) && userResults.hasMore(  ) )
            {
                sr = (SearchResult) userResults.next(  );

                Attributes attributes = sr.getAttributes(  );

                String strName = "";

                if ( attributes.get( ATTRIBUTE_SURNAME ) != null )
                {
                    strName = attributes.get( ATTRIBUTE_SURNAME ).get(  ).toString(  );
                }

                String strGivenName = "";

                if ( attributes.get( ATTRIBUTE_GIVEN_NAME ) != null )
                {
                    strGivenName = attributes.get( ATTRIBUTE_GIVEN_NAME ).get(  ).toString(  );
                }

                String strCompleteName = "";

                if ( attributes.get( ATTRIBUTE_COMPLETE_NAME ) != null )
                {
                    strCompleteName = attributes.get( ATTRIBUTE_COMPLETE_NAME ).get(  ).toString(  );
                }

                String strSociety = "";

                if ( attributes.get( ATTRIBUTE_SOCIETY ) != null )
                {
                    strSociety = attributes.get( ATTRIBUTE_SOCIETY ).get(  ).toString(  );
                }

                String strService = "";

                if ( attributes.get( ATTRIBUTE_SERVICE ) != null )
                {
                    strService = attributes.get( ATTRIBUTE_SERVICE ).get(  ).toString(  );
                }

                String strTelephoneNumber = "";

                if ( attributes.get( ATTRIBUTE_TELEPHONE_NUMBER ) != null )
                {
                    strTelephoneNumber = attributes.get( ATTRIBUTE_TELEPHONE_NUMBER ).get(  ).toString(  );
                }

                String strRnisNumber = "";

                if ( attributes.get( ATTRIBUTE_RNIS_NUMBER ) != null )
                {
                    strRnisNumber = attributes.get( ATTRIBUTE_RNIS_NUMBER ).get(  ).toString(  );
                }

                String strMail = "";

                if ( attributes.get( ATTRIBUTE_EMAIL ) != null )
                {
                    strMail = attributes.get( ATTRIBUTE_EMAIL ).get(  ).toString(  );
                }

                String strTitle = "";

                if ( attributes.get( ATTRIBUTE_TITLE ) != null )
                {
                    strTitle = attributes.get( ATTRIBUTE_TITLE ).get(  ).toString(  );
                }

                String strFloor = "";

                if ( attributes.get( ATTRIBUTE_FLOOR ) != null )
                {
                    strFloor = attributes.get( ATTRIBUTE_FLOOR ).get(  ).toString(  );
                }

                String strRoomNumber = "";

                if ( attributes.get( ATTRIBUTE_ROOM_NUMBER ) != null )
                {
                    strRoomNumber = attributes.get( ATTRIBUTE_ROOM_NUMBER ).get(  ).toString(  );
                }

                String strPostalAddress = "";

                if ( attributes.get( ATTRIBUTE_POSTAL_ADDRESS ) != null )
                {
                    strPostalAddress = attributes.get( ATTRIBUTE_POSTAL_ADDRESS ).get(  ).toString(  );
                }

                String strPostalCode = "";

                if ( attributes.get( ATTRIBUTE_POSTAL_CODE ) != null )
                {
                    strPostalCode = attributes.get( ATTRIBUTE_POSTAL_CODE ).get(  ).toString(  );
                }

                String strTown = "";

                if ( attributes.get( ATTRIBUTE_TOWN ) != null )
                {
                    strTown = attributes.get( ATTRIBUTE_TOWN ).get(  ).toString(  );
                }

                String strDepartment = "";

                if ( attributes.get( ATTRIBUTE_DEPARTMENT ) != null )
                {
                    strDepartment = attributes.get( ATTRIBUTE_DEPARTMENT ).get(  ).toString(  );
                }

                String strModificationDate = "";

                if ( attributes.get( ATTRIBUTE_MODIFICATION_DATE ) != null )
                {
                    strModificationDate = attributes.get( ATTRIBUTE_MODIFICATION_DATE ).get(  ).toString(  );
                }

                //  List<String> listObjectClass = new ArrayList(  );
                String strType = "";

                if ( attributes.get( ATTRIBUTE_OBJECT_CLASS ) != null )
                {
                    // fetch the attribute
                    NamingEnumeration namingEnum = attributes.get( ATTRIBUTE_OBJECT_CLASS ).getAll(  );

                    while ( namingEnum.hasMore(  ) )
                    {
                        String strAttr = (String) namingEnum.next(  );

                        //Add the value to the list
                        if ( strAttr.equalsIgnoreCase( PROPERTY_TYPE_SERVICE ) )
                        {
                            strType = PROPERTY_TYPE_SERVICE;
                        }

                        if ( strAttr.equalsIgnoreCase( PROPERTY_TYPE_ROOM ) )
                        {
                            strType = PROPERTY_TYPE_ROOM;
                        }

                        if ( strAttr.equalsIgnoreCase( PROPERTY_TYPE_PERSON ) )
                        {
                            strType = PROPERTY_TYPE_PERSON;
                        }

                        if ( strType.equalsIgnoreCase( "" ) && !strAttr.equalsIgnoreCase( PROPERTY_TYPE_SERVICE ) &&
                                !strAttr.equalsIgnoreCase( PROPERTY_TYPE_ROOM ) &&
                                !strAttr.equalsIgnoreCase( PROPERTY_TYPE_PERSON ) )
                        {
                            strType = PROPERTY_TYPE_UNKNOWN;
                        }

                        // listObjectClass.add( strAttr);
                    }
                }

                LdapEntity user = new LdapEntity(  );
                user.setName( strName );
                user.setCompleteName( strCompleteName );
                user.setDepartment( strDepartment );
                user.setFloor( strFloor );
                user.setGivenName( strGivenName );
                user.setMail( strMail );
                user.setModificationDate( strModificationDate );
                user.setPostalAddress( strPostalAddress );
                user.setPostalCode( strPostalCode );
                user.setRnisNumber( strRnisNumber );
                user.setRoomNumber( strRoomNumber );
                user.setService( strService );
                user.setSociety( strSociety );
                user.setTelephoneNumber( strTelephoneNumber );
                user.setTitle( strTitle );
                user.setTown( strTown );
                // user.setObjectClass( listObjectClass );
                user.setType( strType );
                userList.add( user );
            }

            return userList;
        }
        catch ( CommunicationException e )
        {
            AppLogService.error( "Error while searching for users '" + "' with search filter : " +
                getDebugInfo( strUserSearchFilter ), e );

            return null;
        }
        catch ( NamingException e )
        {
            AppLogService.error( "Error while searching for users ", e );

            return null;
        }
        finally
        {
            close(  );
        }
    }

    /**
     * Replace the null string or empty string by the wilcard
     * @param in The input
     * @return Cleaned input
     */
    private String checkSyntax( String in )
    {
        return ( ( ( in == null ) || ( in.equals( "" ) ) ) ? "*" : in );
    }

    /**
     * Return info for debugging
     * @param strUserSearchFilter The search filter
     * @return Info for debug
     */
    private String getDebugInfo( String strUserSearchFilter )
    {
        StringBuffer sb = new StringBuffer(  );
        sb.append( "userBase : " );
        sb.append( getUserDnSearchBase(  ) );
        sb.append( "\nuserSearch : " );
        sb.append( strUserSearchFilter );

        return sb.toString(  );
    }

    /**
     * Get the initial context provider from the properties
     * @return The context
     */
    private String getInitialContextProvider(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_INITIAL_CONTEXT_PROVIDER );
    }

    /**
     * Get the provider url from the properties
     * @return The url
     */
    private String getProviderUrl(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_PROVIDER_URL );
    }

    /**
     * Get the base user dn from the properties
     * @return The base dn
     */
    private String getUserDnSearchBase(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_BASE );
    }

    /**
     * Get the filter for search by sn
     * @return Sn
     */
    private String getUserDnSearchFilterByCriteriaSn(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_SN );
    }

    /**
     * Get the filter for search by givenname
     * @return givenname
     */
    private String getUserDnSearchFilterByCriteriaGivenname(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_GIVEN_NAME );
    }

    /**
     * Get the filter for search by mail
     * @return mail
     */
    private String getUserDnSearchFilterByCriteriaMail(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_MAIL );
    }

    /**
     * Get the filter for search by class
     * @return class
     */
    private String getUserDnSearchFilterByCriteriaClass(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_OBJECT_CLASS );
    }

    /**
     * Get the filter for search by complete name
     * @return complete name
     */
    private String getUserDnSearchFilterByCriteriaCompleteName(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_COMPLETE_NAME );
    }

    /**
     * Get the filter for search by society
     * @return society
     */
    private String getUserDnSearchFilterByCriteriaSociety(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_SOCIETY );
    }

    /**
     * Get the filter for search by postal address
     * @return postal address
     */
    private String getUserDnSearchFilterByCriteriaPostalAddress(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_POSTAL_ADDRESS );
    }

    /**
     * Get the filter for search by room number
     * @return room number
     */
    private String getUserDnSearchFilterByCriteriaRoomNumber(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_ROOM_NUMBER );
    }

    /**
     * Get the filter for search by rnis number
     * @return rnis number
     */
    private String getUserDnSearchFilterByCriteriaRnisNumber(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_RNIS_NUMBER );
    }

    /**
     * Get the filter for search by town
     * @return town
     */
    private String getUserDnSearchFilterByCriteriaTown(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_TOWN );
    }

    /**
     * Get the filter for search by floor
     * @return floor
     */
    private String getUserDnSearchFilterByCriteriaFloor(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_FLOOR );
    }

    /**
     * Get the filter for search by phone number
     * @return phone number
     */
    private String getUserDnSearchFilterByCriteriaTelephoneNumber(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_PHONE_NUMBER );
    }

    /**
     * Get the filter for search by service
     * @return service
     */
    private String getUserDnSearchFilterByCriteriaService(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_SERVICE );
    }

    /**
     * Get the filter for search by title
     * @return title
     */
    private String getUserDnSearchFilterByCriteriaTitle(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_TITLE );
    }

    /**
     * Get the filter for search by postal code
     * @return postal code
     */
    private String getUserDnSearchFilterByCriteriaPostalCode(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_POSTAL_CODE );
    }

    /**
     * Get the filter for search by modification date
     * @return modification date
     */
    private String getUserDnSearchFilterByCriteriaModificationDate(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_MODIFICATION_DATE );
    }

    /**
     * Get the filter for search by department
     * @return department
     */
    private String getUserDnSearchFilterByCriteriaDepartment(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_USER_DN_SEARCH_FILTER_BY_CRITERIA_DEPARTMENT );
    }

    /**
     * Get the user dn search scope
     * @return the scope
     */
    private int getUserDnSearchScope(  )
    {
        String strSearchScope = AppPropertiesService.getProperty( PROPERTY_USER_SUBTREE );

        if ( strSearchScope.equalsIgnoreCase( "true" ) )
        {
            return SearchControls.SUBTREE_SCOPE;
        }

        return SearchControls.ONELEVEL_SCOPE;
    }

    /**
     * get the bind dn
     * @return the bind dn
     */
    private String getBindDn(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_BIND_DN );
    }

    /**
     * Get the bing password
     * @return the bind password
     */
    private String getBindPassword(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_BIND_PASSWORD );
    }

    /**
     * build request for search by sn, givenname and mail
     * @return build a valid ldap request
     * @param messageFormatFilter The format filter
     * @param messageFormatParam The params
     */
    public String buildRequest( String[] messageFormatFilter, Object[] messageFormatParam )
    {
        String strUserSearchFilter = "(&";

        for ( int i = 0; i < messageFormatParam.length; i++ )
        {
            if ( messageFormatParam[i].equals( "*" ) == false )
            {
                strUserSearchFilter += MessageFormat.format( messageFormatFilter[i] , messageFormatParam );
            }
        }

        strUserSearchFilter += ")";
        if(strUserSearchFilter.equals("(&)")){strUserSearchFilter="objectclass=*";}

        return strUserSearchFilter;
    }

    /**
     * Open the directory context used for authentication
     * and authorization.
     * @throws NamingException Exception in context name
     */
    private void open(  ) throws NamingException
    {
        if ( _context != null )
        {
            close(  );
        }

        AppLogService.info( "Connecting to URL " + getProviderUrl(  ) );
        _context = LdapUtil.getContext( getInitialContextProvider(  ), getProviderUrl(  ), getBindDn(  ),
                getBindPassword(  ) );
        AppLogService.info( "Connected to URL " + getProviderUrl(  ) );
    }

    /**
     * Close the specified directory context
     */
    private void close(  )
    {
        if ( _context == null )
        {
            return;
        }

        try
        {
            AppLogService.info( "Closing directory context" );
            LdapUtil.freeContext( _context );
        }
        catch ( NamingException e )
        {
            AppLogService.error( "Error while closing the directory context", e );
        }

        _context = null;
    }

    /**
     * Prepare for the beginning of active use of the public methods of this component.
     */
    private void start(  )
    {
        try
        {
            open(  );

            _scUserSearchControls = new SearchControls(  );
            _scUserSearchControls.setSearchScope( getUserDnSearchScope(  ) );
            _scUserSearchControls.setReturningObjFlag( true );
            _scUserSearchControls.setCountLimit( 0 );
        }
        catch ( NamingException e )
        {
            throw new AppException( "Error while opening the directory context", e );
        }
    }
}
