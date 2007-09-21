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
 * This class provides Data Access methods for DbPageDatabaseType objects
 */
public final class DbPageDatabaseTypeDAO implements IDbPageDatabaseTypeDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_type ) FROM dbpage_section_type ";
    private static final String SQL_QUERY_SELECT = " SELECT id_type, class_desc, description FROM dbpage_section_type WHERE id_type = ?  ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO dbpage_section_type ( id_type, class_desc, description ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM dbpage_section_type WHERE id_type = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE dbpage_section_type SET id_type = ?, class_desc = ?, description = ? WHERE id_type = ?  ";
    private static final String SQL_QUERY_SELECTALL = " SELECT id_type, class_desc, description FROM dbpage_section_type ";
    private static final String SQL_QUERY_SELECT_COMBO = "SELECT id_type , description FROM dbpage_section_type ";

    /**
     * Generates a new primary key
     * @return The new primary key
     * @param plugin The plugin
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
     * @param dbPageDatabaseType The dbPageDatabaseType object
     */
    public void insert( DbPageDatabaseType dbPageDatabaseType, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        dbPageDatabaseType.setId( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, dbPageDatabaseType.getId(  ) );
        daoUtil.setString( 2, dbPageDatabaseType.getClassDesc(  ) );
        daoUtil.setString( 3, dbPageDatabaseType.getDescription(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of DbPageDatabaseType from the table
     * @param nDbPageDatabaseTypeId The identifier of DbPageDatabaseType
     * @param plugin The Plugin Object
     * @return the instance of the DbPageDatabaseType
     */
    public DbPageDatabaseType load( int nDbPageDatabaseTypeId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nDbPageDatabaseTypeId );
        daoUtil.executeQuery(  );

        DbPageDatabaseType dbPageDatabaseType = null;

        if ( daoUtil.next(  ) )
        {
            dbPageDatabaseType = new DbPageDatabaseType(  );
            dbPageDatabaseType.setId( daoUtil.getInt( 1 ) );
            dbPageDatabaseType.setClassDesc( daoUtil.getString( 2 ) );
            dbPageDatabaseType.setDescription( daoUtil.getString( 3 ) );
        }

        daoUtil.free(  );

        return dbPageDatabaseType;
    }

    /**
     * Delete a record from the table
     * @param nDbPageDatabaseTypeId The DbPageDatabaseType Id
     */
    public void delete( int nDbPageDatabaseTypeId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nDbPageDatabaseTypeId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param dbPageDatabaseType The reference of dbPageDatabaseType
     */
    public void store( DbPageDatabaseType dbPageDatabaseType, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, dbPageDatabaseType.getId(  ) );
        daoUtil.setString( 2, dbPageDatabaseType.getClassDesc(  ) );
        daoUtil.setString( 3, dbPageDatabaseType.getDescription(  ) );
        daoUtil.setInt( 4, dbPageDatabaseType.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load a database type list
     * @param plugin The Plugin Object
     * @return the type list
     */
    public List<DbPageDatabaseType> selectDbPageDatabaseTypeList( Plugin plugin )
    {
        List<DbPageDatabaseType> listDbPageDatabaseTypes = new ArrayList<DbPageDatabaseType>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DbPageDatabaseType dbPageDatabaseType = new DbPageDatabaseType(  );
            dbPageDatabaseType.setId( daoUtil.getInt( 1 ) );
            dbPageDatabaseType.setClassDesc( daoUtil.getString( 2 ) );
            dbPageDatabaseType.setDescription( daoUtil.getString( 3 ) );

            listDbPageDatabaseTypes.add( dbPageDatabaseType );
        }

        daoUtil.free(  );

        return listDbPageDatabaseTypes;
    }

    /**
     * Load the list of types
     * @return The ReferenceList of the Types
     */
    public ReferenceList selectTypesList( Plugin plugin )
    {
        ReferenceList typeList = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_COMBO, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            typeList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return typeList;
    }
}
