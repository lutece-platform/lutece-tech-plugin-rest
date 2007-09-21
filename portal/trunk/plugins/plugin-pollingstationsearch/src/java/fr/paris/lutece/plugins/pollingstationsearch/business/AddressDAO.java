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
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * This class provides Data Access methods for Address objects
 */
public final class AddressDAO implements IAddressDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT_ADDRESS_BY_STREET_AND_NUMBER_SUFFIX = " SELECT addr_id, addr_urban_district, addr_number, addr_number_suffix, street_id, addr_street_name, polling_station_id FROM address WHERE street_id = ? AND addr_number = ? AND addr_number_suffix = ? AND addr_urban_district = ?";
    private static final String SQL_QUERY_SELECT_ADDRESS_BY_STREET = " SELECT addr_id, addr_urban_district, addr_number, addr_number_suffix, street_id, addr_street_name, polling_station_id FROM address WHERE street_id = ? AND addr_number = ? AND addr_urban_district = ? ";
    private static final String SQL_QUERY_SELECT_POLLING_STATION_BY_ADDRESS = " SELECT polling_station_id FROM address WHERE addr_id = ? ";

    /**
     * Find the address corresponding to the street and  suffix number given.
     *
     * @param plugin the plugin.
     * @param strStreetId the identifier of the street.
     * @param strStreetNumber the number in the street.
     * @param strAddrNumberSuffix the number suffix in the street.
     * @param strDistrictNumber the district number
     * @return The Address corresponding to the street.
     *
     */
    public Address findByStreetIdAndNumberSuffix( Plugin plugin, String strStreetId, String strStreetNumber,
        String strAddrNumberSuffix, String strDistrictNumber )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ADDRESS_BY_STREET_AND_NUMBER_SUFFIX, plugin );
        daoUtil.setString( 1, strStreetId );
        daoUtil.setString( 2, strStreetNumber );
        daoUtil.setString( 3, strAddrNumberSuffix );
        daoUtil.setString( 4, strDistrictNumber );
        daoUtil.executeQuery(  );

        Address address = null;

        while ( daoUtil.next(  ) )
        {
            address = new Address(  );
            address.setId( daoUtil.getInt( 1 ) );
            address.setAddrUrbanDistrict( daoUtil.getString( 2 ) );
            address.setAddrNumber( daoUtil.getString( 3 ) );
            address.setAddrNumberSuffix( daoUtil.getString( 4 ) );
            address.setStreetId( daoUtil.getString( 5 ) );
            address.setAddrStreetName( daoUtil.getString( 6 ) );
            address.setPollingStationId( daoUtil.getInt( 7 ) );
        }

        daoUtil.free(  );

        return address;
    }

    /**
     * Find the address corresponding to the street given.
     *
     * @param plugin the plugin.
     * @param strStreetId the identifier of the street.
     * @param strStreetNumber the number in the street.
     * @param strDistrictNumber the district number
     * @return The Address corresponding to the street.
     *
     */
    public Address findByStreetId( Plugin plugin, String strStreetId, String strStreetNumber, String strDistrictNumber )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ADDRESS_BY_STREET, plugin );
        daoUtil.setString( 1, strStreetId );
        daoUtil.setString( 2, strStreetNumber );
        daoUtil.setString( 3, strDistrictNumber );
        daoUtil.executeQuery(  );

        Address address = null;

        while ( daoUtil.next(  ) )
        {
            address = new Address(  );
            address.setId( daoUtil.getInt( 1 ) );
            address.setAddrUrbanDistrict( daoUtil.getString( 2 ) );
            address.setAddrNumber( daoUtil.getString( 3 ) );
            address.setAddrNumberSuffix( daoUtil.getString( 4 ) );
            address.setStreetId( daoUtil.getString( 5 ) );
            address.setAddrStreetName( daoUtil.getString( 6 ) );
            address.setPollingStationId( daoUtil.getInt( 7 ) );
        }

        daoUtil.free(  );

        return address;
    }

    /**
     * Find the polling station corresponding to the address given.
     *
     * @param plugin the plugin.
     * @param nAddressId the identifier of the address.
     * @return The polling station id corresponding to the address.
     *
     */
    public int findPollingStationIdByAddressId( Plugin plugin, int nAddressId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_POLLING_STATION_BY_ADDRESS, plugin );
        daoUtil.setInt( 1, nAddressId );
        daoUtil.executeQuery(  );

        int nPollingStationId = 0;

        while ( daoUtil.next(  ) )
        {
            nPollingStationId = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nPollingStationId;
    }
}
