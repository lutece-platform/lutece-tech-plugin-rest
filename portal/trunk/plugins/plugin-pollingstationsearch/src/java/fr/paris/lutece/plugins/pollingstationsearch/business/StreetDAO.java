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
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides data access methods for streets.
 */
public final class StreetDAO implements IStreetDAO
{
    private static final String SQL_QUERY_SELECT_BY_URBAN_DISTRICT_TYPE_NAME = "SELECT street_id, short_street_name, long_street_name, short_street_type, street_urban_district " +
        "FROM street " + "WHERE street_urban_district = ? AND short_street_type = ? AND short_street_name LIKE ? " +
        "ORDER BY long_street_name ";
    private static final String SQL_QUERY_SELECT_BY_URBAN_DISTRICT_NAME = "SELECT street_id, short_street_name, long_street_name, short_street_type, street_urban_district " +
        "FROM street " + "WHERE street_urban_district = ? AND short_street_name LIKE ? " +
        "ORDER BY long_street_name ";
    private static final String SQL_QUERY_SELECT_BY_PRIMARY_KEY = "SELECT street_id, street_urban_district, long_street_name, short_street_type, short_street_name " +
        "FROM street WHERE street_id = ? and street_urban_district = ?";

    private String CONSTANT_EMPTY_STRING = "";

    /**
     * Find all streets by urban district, type and begining with the given name.
     *
     * @param plugin The Plugin using this data access service
     * @param strStreetUrbanDistrict the street's urban district
     * @param strStreetType the street's type
     * @param strStreetName the street's name
     * @return A collection of Street.
     */
    public Collection<Street> findByUrbanDistrictTypeName( Plugin plugin, String strStreetUrbanDistrict,
        String strStreetType, String strStreetName )
    {
        Collection<Street> listResult = new ArrayList<Street>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_URBAN_DISTRICT_TYPE_NAME, plugin );
        daoUtil.setString( 1, strStreetUrbanDistrict );
        daoUtil.setString( 2, strStreetType );
        daoUtil.setString( 3, strStreetName );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Street street = new Street(  );
            street.setStreetId( daoUtil.getString( 1 ) );
            street.setShortStreetName( daoUtil.getString( 2 ) );
            street.setLongStreetName( daoUtil.getString( 3 ) );
            street.setStreetType( daoUtil.getString( 4 ) );
            street.setStreetUrbanDistrict( daoUtil.getString( 5 ) );
            listResult.add( street );
        }

        daoUtil.free(  );

        return listResult;
    }

    /**
     * Find all streets by urban district and begining with the given name.
     *
     * @param plugin The Plugin using this data access service
     * @param strStreetUrbanDistrict the street's urban district
     * @param strStreetName the street's name
     * @return A collection of Street.
     */
    public Collection<Street> findByUrbanDistrictAndName( Plugin plugin, String strStreetUrbanDistrict,
        String strStreetName )
    {
        Collection<Street> listResult = new ArrayList<Street>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_URBAN_DISTRICT_NAME, plugin );
        daoUtil.setString( 1, strStreetUrbanDistrict );
        daoUtil.setString( 2, strStreetName );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Street street = new Street(  );
            street.setStreetId( daoUtil.getString( 1 ) );
            street.setShortStreetName( daoUtil.getString( 2 ) );
            street.setLongStreetName( daoUtil.getString( 3 ) );
            street.setStreetType( daoUtil.getString( 4 ) );
            street.setStreetUrbanDistrict( daoUtil.getString( 5 ) );
            listResult.add( street );
        }

        daoUtil.free(  );

        return listResult;
    }

    /**
     * Find all streets by urban district and begining with the given name.
     *
     * @param plugin The Plugin using this data access service
     * @param strStreetUrbanDistrict the street urban district
     * @param strListStreetName a list of words corresponding to the street name
     * @return A collection of Street.
     */
    public Collection<Street> findByUrbanDistrictAndName( Plugin plugin, String strStreetUrbanDistrict,
        String[] strListStreetName )
    {

        String query = "SELECT street_id, short_street_name, long_street_name, short_street_type, street_urban_district " +
            "FROM street " + "WHERE street_urban_district = '" + strStreetUrbanDistrict + "'" + " AND ";
        boolean bFirstLoop = true;

        for ( String strStreetName : strListStreetName )
        {
            if ( !CONSTANT_EMPTY_STRING.equals( strStreetName ) )
            {
                AppLogService.debug( "Searching on word = " + strStreetName );

                if ( bFirstLoop )
                {
                    bFirstLoop = false;
                    query = query + "(short_street_name LIKE '%" + strStreetName + "%' ";
                }
                else
                {
                    query = query + "OR short_street_name LIKE '%" + strStreetName + "%' ";
                }
            }
        }

        query = query + ") ORDER BY long_street_name ";

        Collection<Street> listResult = new ArrayList<Street>(  );
        DAOUtil daoUtil = new DAOUtil( query, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Street street = new Street(  );
            street.setStreetId( daoUtil.getString( 1 ) );
            street.setShortStreetName( daoUtil.getString( 2 ) );
            street.setLongStreetName( daoUtil.getString( 3 ) );
            street.setStreetType( daoUtil.getString( 4 ) );
            street.setStreetUrbanDistrict( daoUtil.getString( 5 ) );
            listResult.add( street );
        }

        daoUtil.free(  );

        return listResult;
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
    public Street findByPrimaryKey( Plugin plugin, String strStreetkey, String strStreetUrbanDistrict )
    {
        Street street = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PRIMARY_KEY, plugin );
        daoUtil.setString( 1, strStreetkey );
        daoUtil.setString( 2, strStreetUrbanDistrict );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            street = new Street(  );
            street.setStreetId( daoUtil.getString( 1 ) );
            street.setStreetUrbanDistrict( daoUtil.getString( 2 ) );
            street.setLongStreetName( daoUtil.getString( 3 ) );
            street.setStreetType( daoUtil.getString( 4 ) );
            street.setShortStreetName( daoUtil.getString( 5 ) );
        }

        daoUtil.free(  );

        return street;
    }
}
