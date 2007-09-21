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


/**
 * Interface for AddressDAO
 */
public interface IAddressDAO
{
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
    Address findByStreetIdAndNumberSuffix( Plugin plugin, String strStreetId, String strStreetNumber,
        String strAddrNumberSuffix, String strDistrictNumber );

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
    Address findByStreetId( Plugin plugin, String strStreetId, String strStreetNumber, String strDistrictNumber );

    /**
     * Find the polling station corresponding to the address given.
     *
     * @param plugin the plugin.
     * @param nAddressId the identifier of the address.
     * @return The polling station id corresponding to the address.
     *
     */
    int findPollingStationIdByAddressId( Plugin plugin, int nAddressId );
}
