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
package fr.paris.lutece.plugins.pollingstationsearch.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.Collection;


/**
 * This class provides instances management methods (create, find, ...) for streets
 */
public final class StreetHome
{
    private static final String PROPERTY_FILTER_VALUES = "pollingstationsearch.filter.values";
    private static final String PROPERTY_FILTER_SEPARATOR = "pollingstationsearch.filter.separator";
    private static final String PROPERTY_FORBIDDEN_WORDS = "pollingstationsearch.forbidden.words";
    private static final String CONSTANT_SPACE_CHAR = " ";
    private static final String CONSTANT_DOUBLE_SPACE_CHAR = "  ";
    private static final String CONSTANT_PERCENT_CHAR = "%";
    private static final String CONSTANT_EMPTY_STRING = "";

    // Static variable pointed at the DAO instance
    private static IStreetDAO _dao = (IStreetDAO) SpringContextService.getPluginBean( "pollingstationsearch",
            "streetDAO" );

    /**
     * Constructor
     */
    private StreetHome(  )
    {
        // No-op
    }

    /**
     * Find all streets by number, suffix, type, name and urban district.
     *
     * @param plugin the plugin.
     * @param strStreetNumber the number in the street.
     * @param strStreetSuffix the street suffix.
     * @param strStreetType the street type.
     * @param strStreetName the street name.
     * @param strStreetUrbanDistrict the street urban district.
     * @return A collection of Streets.
     *
     */
    public static Collection<Street> findByUrbanDistrictTypeName( Plugin plugin, String strStreetNumber,
        String strStreetSuffix, String strStreetType, String strStreetName, String strStreetUrbanDistrict )
    {
        AppLogService.debug( "strStreetNumber = " + strStreetNumber );
        AppLogService.debug( "strStreetSuffix = " + strStreetSuffix );
        AppLogService.debug( " strStreetType = " + strStreetType );
        AppLogService.debug( " strStreetName = " + strStreetName );
        AppLogService.debug( " strStreetUrbanDistrict = " + strStreetUrbanDistrict );

        // The street name is filtered to remove quotes and hyphens
        String strFilteredStreetName = firstFilterInputString( strStreetName );

        // FIRST SEARCH : the street's type is known
        Collection<Street> listStreets = null;

        if ( strStreetType != null )
        {
            AppLogService.debug( "SEARCH 1 : search with postcode, type and name" );
            listStreets = _dao.findByUrbanDistrictTypeName( plugin, strStreetUrbanDistrict, strStreetType,
                    strFilteredStreetName );
        }

        // SECOND SEARCH
        if ( ( strStreetType == null ) || ( listStreets == null ) || listStreets.isEmpty(  ) )
        {
            AppLogService.debug( "SEARCH 2 : search with postcode and name" );
            listStreets = _dao.findByUrbanDistrictAndName( plugin, strStreetUrbanDistrict, strFilteredStreetName );
        }

        // THIRD SEARCH
        if ( ( listStreets == null ) || listStreets.isEmpty(  ) )
        {
            // The street name is filtered again
            strFilteredStreetName = secondFilterInputString( strFilteredStreetName );
            AppLogService.debug( "SEARCH 3 : search with postcode and word" );

            String[] arrayFilterValues = strFilteredStreetName.split( CONSTANT_PERCENT_CHAR );
            Collection<Street> collection = _dao.findByUrbanDistrictAndName( plugin, strStreetUrbanDistrict,
                    arrayFilterValues );

            if ( ( collection != null ) && ( collection.size(  ) != 0 ) )
            {
                if ( ( listStreets == null ) || listStreets.isEmpty(  ) )
                {
                    listStreets = collection;
                }
                else
                {
                    listStreets.addAll( collection );
                }
            }
        }

        return listStreets;
    }

    /**
     * Find a street by its primary key.
     *
     * @param plugin the plugin.
     * @param strStreetkey the first part of the  primary key of the street.
     * @param strStreetUrbanDistrict the last part of the primary key  of the street.
     * @return The Street.
     *
     */
    public static Street findByPrimaryKey( Plugin plugin, String strStreetkey,String strStreetUrbanDistrict ) 
    {
        Street street = _dao.findByPrimaryKey( plugin, strStreetkey,strStreetUrbanDistrict );

        return street;
    }

    /**
     * Find all streets by number, suffix, type, name and urban district.
     *
     * @param plugin the plugin.
     * @param strStreetNumber the street number.
     * @param strStreetSuffix the street suffix.
     * @param strFilteredStreetName the street name.
     * @param strStreetType the street type.
     * @param strStreetUrbanDistrict the street urban district.
     * @return A collection of Addresses.
     *
     */
    public static Collection<Street> findStreetList( Plugin plugin, String strStreetNumber, String strStreetSuffix,
        String strStreetType, String strFilteredStreetName, String strStreetUrbanDistrict )
    {
        Collection<Street> streetList = findByUrbanDistrictTypeName( plugin, strStreetNumber, strStreetSuffix,
                strStreetType, strFilteredStreetName, strStreetUrbanDistrict );

        return streetList;
    }

    /**
     * Return the input string filtered
     * @param strInputString the string to filter
     * @return the new string without the filtered characters
     */
    private static String firstFilterInputString( String strInputString )
    {
        String strFiltered = strInputString.toUpperCase(  );
        String strFilterValues = AppPropertiesService.getProperty( PROPERTY_FILTER_VALUES );
        String strFilterSeparator = AppPropertiesService.getProperty( PROPERTY_FILTER_SEPARATOR );
        String[] arrayFilterValues = strFilterValues.split( strFilterSeparator );

        if ( strFiltered != null )
        {
            AppLogService.debug( "string before 1rst filtering : " + strFiltered );

            for ( String strFilterValue : arrayFilterValues )
            {
                AppLogService.debug( "Replace |" + strFilterValue + "| with " + CONSTANT_SPACE_CHAR );
                strFiltered = strFiltered.replaceAll( strFilterValue, CONSTANT_SPACE_CHAR );
                strFiltered = strFiltered.trim(  );
            }
        }

        // delete double spaces
        boolean bHasSpaces = true;

        while ( bHasSpaces )
        {
            if ( strFiltered.indexOf( CONSTANT_DOUBLE_SPACE_CHAR ) != -1 )
            {
                strFiltered = strFiltered.replaceAll( CONSTANT_DOUBLE_SPACE_CHAR, CONSTANT_SPACE_CHAR );
            }
            else
            {
                bHasSpaces = false;
            }
        }

        // replace spaces by %
        strFiltered = strFiltered.replaceAll( CONSTANT_SPACE_CHAR, CONSTANT_PERCENT_CHAR );
        // add a % at the begining and the end of the string
        strFiltered = CONSTANT_PERCENT_CHAR + strFiltered + CONSTANT_PERCENT_CHAR;
        AppLogService.debug( "string after 1rst filtering : " + strFiltered );

        return strFiltered;
    }

    /**
     * Return the input string filtered
     * @param strInputString the string to filter
     * @return the new string without the filtered characters
     */
    private static String secondFilterInputString( String strInputString )
    {
        String strFiltered = strInputString;
        String strFilterValues = AppPropertiesService.getProperty( PROPERTY_FORBIDDEN_WORDS );
        String strFilterSeparator = AppPropertiesService.getProperty( PROPERTY_FILTER_SEPARATOR );
        String[] arrayFilterValues = strFilterValues.split( strFilterSeparator );

        if ( strFiltered != null )
        {
            AppLogService.debug( "string before 2nd filtering : " + strFiltered );

            for ( String strFilterValue : arrayFilterValues )
            {
                strFiltered = strFiltered.trim(  );
                AppLogService.debug( "Replace |" + CONSTANT_PERCENT_CHAR + strFilterValue + CONSTANT_PERCENT_CHAR +
                    "| with |" + CONSTANT_EMPTY_STRING + "|" );
                strFiltered = strFiltered.replaceAll( CONSTANT_PERCENT_CHAR + strFilterValue + CONSTANT_PERCENT_CHAR,
                        CONSTANT_PERCENT_CHAR );
            }
        }

        AppLogService.debug( "string after 2nd filtering : " + strFiltered );

        return strFiltered;
    }
}
