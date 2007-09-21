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
import fr.paris.lutece.util.ReferenceList;

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for DbPageDatabase objects
 */
public final class DbPageDatabaseHome
{
    // Static variable pointed at the DAO instance
    private static IDbPageDatabaseDAO _dao = (IDbPageDatabaseDAO) SpringContextService.getPluginBean( "dbpage",
            "dbPageDatabaseDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DbPageDatabaseHome(  )
    {
    }

    /**
     * Creation of an instance of dbPageDatabase
     *
     * @return The  instance of dbPageDatabase which has been created with its primary key.
     * @param dbPageDatabase The instance of the dbPageDatabase which contains the informations to store
     */
    public static DbPageDatabase create( DbPageDatabase dbPageDatabase, Plugin plugin )
    {
        _dao.insert( dbPageDatabase, plugin );

        return dbPageDatabase;
    }

    /**
     * Update of the dbPageDatabase which is specified in parameter
     *
     * @return The instance of the  dbPageDatabase which has been updated
     * @param dbPageDatabase The instance of the dbPageDatabase which contains the data to store
     */
    public static DbPageDatabase update( DbPageDatabase dbPageDatabase, Plugin plugin )
    {
        _dao.store( dbPageDatabase, plugin );

        return dbPageDatabase;
    }

    /**
     * Remove the DbPageDatabase whose identifier is specified in parameter
     *
     * @param nDbPageDatabaseId The DbPageDatabase Id
     */
    public static void remove( int nDbPageDatabaseId, Plugin plugin )
    {
        _dao.delete( nDbPageDatabaseId, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a dbPageDatabase whose identifier is specified in parameter
     *
     * @return An instance of dbPageDatabase
     * @param nKey The Primary key of the dbPageDatabase
     */
    public static DbPageDatabase findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
    * Returns an instance of a dbPageDatabase whose identifier is specified in parameter
    *
    * @return An instance of dbPageDatabase
    * @param strName The name the dbPageDatabase
    */
    public static DbPageDatabase findByName( String strName, Plugin plugin )
    {
        return _dao.loadByName( strName, plugin );
    }

    /**
    * Returns a referenceList of dbPageDatabases objects
    * @return A ReferenceList of dbPageDatabases
    */
    public static List<DbPageDatabase> findDbPageDatabasesList( Plugin plugin )
    {
        return _dao.selectDbPageDatabaseList( plugin );
    }

    /**
     * Returns a ReferenceList of dbPageDatabases objects
     * @return A collection of dbPageDatabases
     */
    public static ReferenceList findDbPageComboList( Plugin plugin )
    {
        return _dao.selectDbPagesList( plugin );
    }
}
