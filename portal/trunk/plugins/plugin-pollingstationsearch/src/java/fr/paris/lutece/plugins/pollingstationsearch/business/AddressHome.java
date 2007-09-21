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


/**
 * This class provides instances management methods (create, find, ...) for addresses
 */
public final class AddressHome
{
    // Static variable pointed at the DAO instance
    private static IAddressDAO _dao = (IAddressDAO) SpringContextService.getPluginBean( "pollingstationsearch",
            "addressDAO" );

    /**
     * Constructor
     */
    private AddressHome(  )
    {
        // No-op
    }

    /**
     * Find the address correcponding to the street and the number suffix given.
     *
     * @param plugin the plugin.
     * @param strStreetId the identifier of the street.
     * @param strStreetNumber Number in the street.
     * @param strNumberSuffix the number suffix in the street.
     * @param strDistrictNumber the district number
     * @return The Address corresponding to the street.
     *
     */
    public static Address findByStreetIdAndNumberSuffix( Plugin plugin, String strStreetId, String strStreetNumber,
        String strNumberSuffix, String strDistrictNumber )
    {
        Address address;

        if ( strNumberSuffix == null )
        {
            address = _dao.findByStreetId( plugin, strStreetId, strStreetNumber, strDistrictNumber );
        }
        else
        {
            address = _dao.findByStreetIdAndNumberSuffix( plugin, strStreetId, strStreetNumber, strNumberSuffix, strDistrictNumber );
        }

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
    public static int findPollingStationIdByAddressId( Plugin plugin, int nAddressId )
    {
        int nPollingStation = _dao.findPollingStationIdByAddressId( plugin, nAddressId );

        return nPollingStation;
    }
}
