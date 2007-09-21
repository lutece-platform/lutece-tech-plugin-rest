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
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * This class provides data access methods for street types.
 */
public final class StreetTypeDAO implements IStreetTypeDAO
{
    private static final String PROPERTY_STREET_TYPE_OTHER_CHOICE_ID = "pollingstationsearch.street.type.other.choice.id";
    private static final String SQL_QUERY_SELECT = "SELECT short_street_type, long_street_type FROM street_type ORDER BY short_street_type ";
    private static final String SQL_QUERY_SELECT_STREET_TYPE = "SELECT short_street_type, long_street_type FROM street_type WHERE short_street_type=? ";

    /**
     * Returns the list of all street types
     * @param plugin The plugin
     * @param strOtherChoice the value in the combo box for other choice
     * @param strMsgSelectType value in the combo box : "Select the street's type"
     * @return the ReferenceList of all street types
     */
    public ReferenceList selectStreetTypeList( Plugin plugin, String strOtherChoice, String strMsgSelectType )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.executeQuery(  );

        ReferenceList list = new ReferenceList(  );

        list.addItem( "0", strMsgSelectType );

        while ( daoUtil.next(  ) )
        {
            list.addItem( daoUtil.getString( 1 ), daoUtil.getString( 2 ) );
        }

        list.addItem( AppPropertiesService.getProperty( PROPERTY_STREET_TYPE_OTHER_CHOICE_ID ), strOtherChoice );

        daoUtil.free(  );

        return list;
    }

    /**
     * Return Street Type by primary key
     * @param plugin The plugin
     * @param strPrimaryKey the primary key parameter
     * @return the Street Type corresponding to the key
     */
    public StreetType findByPrimaryKey( Plugin plugin, String strPrimaryKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_STREET_TYPE, plugin );
        daoUtil.setString( 1, strPrimaryKey );
        daoUtil.executeQuery(  );

        StreetType streetType = null;

        if ( daoUtil.next(  ) )
        {
            streetType = new StreetType(  );
            streetType.setShortStreetType( daoUtil.getString( 1 ) );
            streetType.setLongStreetType( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return streetType;
    }
}
