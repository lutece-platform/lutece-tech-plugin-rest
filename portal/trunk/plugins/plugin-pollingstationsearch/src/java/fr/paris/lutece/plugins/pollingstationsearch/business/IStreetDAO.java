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

import java.util.Collection;


/**
 * Interface for StreetDAO
 */
public interface IStreetDAO
{
    /**
     * Find all streets by urban district, type and begining with the given name.
     *
     * @param plugin The Plugin using this data access service
     * @param strStreetUrbanDistrict the street's urban district
     * @param strStreetType the street's type
     * @param strStreetName the street's name
     * @return A collection of Street.
     */
    Collection<Street> findByUrbanDistrictTypeName( Plugin plugin, String strStreetUrbanDistrict, String strStreetType,
        String strStreetName );

    /**
     * Find all streets by urban district and begining with the given name.
     *
     * @param plugin The Plugin using this data access service
     * @param strStreetUrbanDistrict the street's urban district
     * @param strStreetName the street's name
     * @return A collection of Street.
     */
    Collection<Street> findByUrbanDistrictAndName( Plugin plugin, String strStreetUrbanDistrict, String strStreetName );

    /**
     * Find all streets by urban district and begining with the given name.
     *
     * @param plugin The Plugin using this data access service
     * @param strStreetUrbanDistrict the street urban district
     * @param strListStreetName a list of words corresponding to the street name
     * @return A collection of Street.
     */
    Collection<Street> findByUrbanDistrictAndName( Plugin plugin, String strStreetUrbanDistrict,
        String[] strListStreetName );

    /**
     * Find a street by its primary key.
     *
     * @param plugin the plugin.
     * @param strStreetkey the first part of the  primary key of the street.
     * @param strStreetUrbanDistrict the last part of the primary key of the street.
     * @return The Street.
     *
     */
    Street findByPrimaryKey( Plugin plugin, String strStreetkey ,String strStreetUrbanDistrict );
}
