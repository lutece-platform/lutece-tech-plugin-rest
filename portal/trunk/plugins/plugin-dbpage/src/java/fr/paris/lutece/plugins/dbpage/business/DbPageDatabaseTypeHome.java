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
package fr.paris.lutece.plugins.dbpage.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for DbPageDatabaseType objects
 */
public final class DbPageDatabaseTypeHome
{
    // Static variable pointed at the DAO instance
    private static IDbPageDatabaseTypeDAO _dao = (IDbPageDatabaseTypeDAO) SpringContextService.getPluginBean( "dbpage",
            "dbPageDatabaseTypeDAO" );

    /**
     * Private constructor - this class needs not be instantiated
     */
    private DbPageDatabaseTypeHome(  )
    {
    }

    /**
     * Creation of an instance of dbPageDatabaseType
     *
     * @return The  instance of dbPageDatabaseType which has been created with its primary key.
     * @param dbPageDatabaseType The instance of the dbPageDatabaseType which contains the informations to store
     */
    public static DbPageDatabaseType create( DbPageDatabaseType dbPageDatabaseType, Plugin plugin )
    {
        _dao.insert( dbPageDatabaseType, plugin );

        return dbPageDatabaseType;
    }

    /**
     * Update of the dbPageDatabaseType which is specified in parameter
     *
     * @return The instance of the  dbPageDatabaseType which has been updated
     * @param dbPageDatabaseType The instance of the dbPageDatabaseType which contains the data to store
     */
    public static DbPageDatabaseType update( DbPageDatabaseType dbPageDatabaseType, Plugin plugin )
    {
        _dao.store( dbPageDatabaseType, plugin );

        return dbPageDatabaseType;
    }

    /**
     * Remove the DbPageDatabaseType whose identifier is specified in parameter
     *
     * @param nDbPageDatabaseTypeId The DbPageDatabaseType Id
     */
    public static void remove( int nDbPageDatabaseTypeId, Plugin plugin )
    {
        _dao.delete( nDbPageDatabaseTypeId, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a dbPageDatabaseType whose identifier is specified in parameter
     * @return An instance of dbPageDatabaseType
     * @param plugin The plugin object
     * @param nKey The Primary key of the dbPageDatabaseType
     */
    public static DbPageDatabaseType findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Returns a collection of dbPageDatabaseTypes objects
     * @return A collection of dbPageDatabaseTypes
     * @param plugin The plugin object
     */
    public static List findDbPageDatabaseTypesList( Plugin plugin )
    {
        return _dao.selectDbPageDatabaseTypeList( plugin );
    }

    /**
     * Returns a ReferenceList of Types
     * @return A ReferenceList of types
     * @param plugin The plugin object
     */
    public static List findTypeComboList( Plugin plugin )
    {
        return _dao.selectTypesList( plugin );
    }
}
