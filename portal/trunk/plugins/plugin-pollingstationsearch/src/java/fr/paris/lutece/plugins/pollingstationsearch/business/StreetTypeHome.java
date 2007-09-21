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
import fr.paris.lutece.util.ReferenceList;


/**
 * This class provides instances management methods (create, find, ...) for street type
 */
public final class StreetTypeHome
{
    // Static variable pointed at the DAO instance
    private static IStreetTypeDAO _dao = (IStreetTypeDAO) SpringContextService.getPluginBean( "pollingstationsearch",
            "streetTypeDAO" );

    /**
     * Constructor
     */
    private StreetTypeHome(  )
    {
        // No-op
    }

    /**
     * Returns the list of all street types
     * @param plugin The plugin
     * @param strOtherChoice the value in the combo box for other choice
     * @param strMsgSelectType value in the combo box : "Select the street's type"
     * @return the ReferenceList of all street types
     */
    public static ReferenceList getStreetTypeList( Plugin plugin, String strOtherChoice, String strMsgSelectType )
    {
        ReferenceList list = _dao.selectStreetTypeList( plugin, strOtherChoice, strMsgSelectType );

        return list;
    }

    /**
     * Find a streetType by its primary key.
     *
     * @param plugin the plugin.
     * @param strStreetTypeKey the primary key of the streetType.
     * @return The StreetType.
     *
     */
    public static StreetType findByPrimaryKey( Plugin plugin, String strStreetTypeKey )
    {
        StreetType streetType = _dao.findByPrimaryKey( plugin, strStreetTypeKey );

        return streetType;
    }
}
