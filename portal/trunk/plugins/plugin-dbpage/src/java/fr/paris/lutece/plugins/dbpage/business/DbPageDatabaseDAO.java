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
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for DbPageDatabase objects
 */
public final class DbPageDatabaseDAO implements IDbPageDatabaseDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_page ) FROM dbpage_page ";
    private static final String SQL_QUERY_SELECT = " SELECT id_page, param_name, title, workgroup_key FROM dbpage_page WHERE id_page = ?  ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO dbpage_page ( id_page, param_name, title, workgroup_key ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM dbpage_page WHERE id_page = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE dbpage_page SET id_page = ?, param_name = ?, title = ?, workgroup_key = ? WHERE id_page = ?  ";
    private static final String SQL_QUERY_SELECTALL = " SELECT id_page, param_name, title, workgroup_key FROM dbpage_page ";
    private static final String SQL_QUERY_SELECT_COMBO = " SELECT id_page , param_name FROM dbpage_page ";
    private static final String SQL_QUERY_SELECT_BY_NAME = "SELECT id_page, param_name, title, workgroup_key FROM dbpage_page where param_name = ?  ";

    /**
     * Generates a new primary key
     * @return The new primary key
     * @param plugin The plugin object
     */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
     * Insert a new record in the table.
     *
     * @param dbPageDatabase The dbPageDatabase object
     */
    public void insert( DbPageDatabase dbPageDatabase, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        dbPageDatabase.setId( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, dbPageDatabase.getId(  ) );
        daoUtil.setString( 2, dbPageDatabase.getParamName(  ) );
        daoUtil.setString( 3, dbPageDatabase.getTitle(  ) );
        daoUtil.setString( 4, dbPageDatabase.getWorkgroup(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of DbPageDatabase from the table
     *
     * @return the instance of the DbPageDatabase
     * @param nDbPageDatabaseId The identifier of DbPageDatabase
     */
    public DbPageDatabase load( int nDbPageDatabaseId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nDbPageDatabaseId );
        daoUtil.executeQuery(  );

        DbPageDatabase dbPageDatabase = null;

        if ( daoUtil.next(  ) )
        {
            dbPageDatabase = new DbPageDatabase(  );
            dbPageDatabase.setId( daoUtil.getInt( 1 ) );
            dbPageDatabase.setParamName( daoUtil.getString( 2 ) );
            dbPageDatabase.setTitle( daoUtil.getString( 3 ) );
            dbPageDatabase.setWorkgroup( daoUtil.getString( 4 ) );
        }

        daoUtil.free(  );

        return dbPageDatabase;
    }

    /**
    * Load the data of DbPageDatabase from the table
    *
    * @return the instance of the DbPageDatabase
    * @param strDbPageName The identifier of DbPageDatabase
    */
    public DbPageDatabase loadByName( String strDbPageName, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_NAME, plugin );
        daoUtil.setString( 1, strDbPageName );
        daoUtil.executeQuery(  );

        DbPageDatabase dbPageDatabase = null;

        if ( daoUtil.next(  ) )
        {
            dbPageDatabase = new DbPageDatabase(  );
            dbPageDatabase.setId( daoUtil.getInt( 1 ) );
            dbPageDatabase.setParamName( daoUtil.getString( 2 ) );
            dbPageDatabase.setTitle( daoUtil.getString( 3 ) );
            dbPageDatabase.setWorkgroup( daoUtil.getString( 4 ) );
        }

        daoUtil.free(  );

        return dbPageDatabase;
    }

    /**
     * Delete a record from the table
     * @param nDbPageDatabaseId The DbPageDatabase Id
     */
    public void delete( int nDbPageDatabaseId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nDbPageDatabaseId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param dbPageDatabase The reference of dbPageDatabase
     */
    public void store( DbPageDatabase dbPageDatabase, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, dbPageDatabase.getId(  ) );
        daoUtil.setString( 2, dbPageDatabase.getParamName(  ) );
        daoUtil.setString( 3, dbPageDatabase.getTitle(  ) );
        daoUtil.setString( 4, dbPageDatabase.getWorkgroup(  ) );
        daoUtil.setInt( 5, dbPageDatabase.getId(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
    * Load the list of dbPageDatabases
    * @return The List of the DbPages
    */
    public List<DbPageDatabase> selectDbPageDatabaseList( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        List<DbPageDatabase> list = new ArrayList(  );

        while ( daoUtil.next(  ) )
        {
            DbPageDatabase dbPageDatabase = new DbPageDatabase(  );
            dbPageDatabase.setId( daoUtil.getInt( 1 ) );
            dbPageDatabase.setParamName( daoUtil.getString( 2 ) );
            dbPageDatabase.setTitle( daoUtil.getString( 3 ) );
            dbPageDatabase.setWorkgroup( daoUtil.getString( 4 ) );
            list.add( dbPageDatabase );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Load the list of dbpages
     *
     * @return The ReferenceList of the DbPages
     */
    public ReferenceList selectDbPagesList( Plugin plugin )
    {
        ReferenceList dbPageList = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_COMBO, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            dbPageList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return dbPageList;
    }
}
