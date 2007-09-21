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
 * This class provides data access methods for number suffixes.
 */
public final class NumberSuffixDAO implements INumberSuffixDAO
{
    private static final String PROPERTY_NO_NUMBER_SUFFIX_CHOOSEN_ID = "pollingstationsearch.no.number.suffix.choosen.id";
    private static final String CONSTANT_EMPTY_STRING = "";
    private static final String SQL_QUERY_SELECT = "SELECT number_suffix, number_suffix_label FROM number_suffix ";
    private static final String SQL_QUERY_SELECT_BY_PRIMARY_KEY = "SELECT number_suffix, number_suffix_label FROM number_suffix WHERE number_suffix = ?";

    /**
     * Returns the list of all number suffixes
     * @param plugin The plugin
     * @return the ReferenceList of all number suffixes
     */
    public ReferenceList selectNumberSuffixList( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.executeQuery(  );

        ReferenceList list = new ReferenceList(  );

        list.addItem( AppPropertiesService.getProperty( PROPERTY_NO_NUMBER_SUFFIX_CHOOSEN_ID ), CONSTANT_EMPTY_STRING );

        while ( daoUtil.next(  ) )
        {
            list.addItem( daoUtil.getString( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Find a number suffix by its primary key.
     *
     * @param plugin the plugin.
     * @param strNumberSuffixKey the primary key of the number suffix.
     * @return The NumberSuffix.
     *
     */
    public NumberSuffix findByPrimaryKey( Plugin plugin, String strNumberSuffixKey )
    {
        NumberSuffix numberSuffix = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PRIMARY_KEY, plugin );
        daoUtil.setString( 1, strNumberSuffixKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            numberSuffix = new NumberSuffix(  );
            numberSuffix.setNumberSuffix( daoUtil.getString( 1 ) );
            numberSuffix.setNumberSuffixLabel( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return numberSuffix;
    }
}
