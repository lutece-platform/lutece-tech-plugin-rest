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
package fr.paris.lutece.plugins.folderlisting.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for FolderListingDatabase objects
 */
public final class FolderListingDatabaseDAO implements IFolderListingDatabaseDAO
{
    ///////////////////////////////////////////////////////////////////////
    // Constants
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_folder ) FROM folderlisting_folders ";
    private static final String SQL_QUERY_SELECT = " SELECT id_folder, folder_name, folder_path, workgroup_key FROM folderlisting_folders WHERE id_folder = ?  ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO folderlisting_folders ( id_folder, folder_name, folder_path, workgroup_key ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM folderlisting_folders WHERE id_folder = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE folderlisting_folders SET id_folder = ?, folder_name = ?, folder_path = ?, workgroup_key = ? WHERE id_folder = ?  ";
    private static final String SQL_QUERY_SELECTALL = " SELECT id_folder, folder_name, folder_path, workgroup_key FROM folderlisting_folders ";

    /**
     * Generates a new primary key
     * @param plugin The Plugin using this data access service
     * @return The new primary key
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
     * @param folderListingDatabase The folderListingDatabase object
     * @param plugin The Plugin using this data access service
     */
    public void insert( FolderListingDatabase folderListingDatabase, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        folderListingDatabase.setId( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, folderListingDatabase.getId(  ) );
        daoUtil.setString( 2, folderListingDatabase.getFolderName(  ) );
        daoUtil.setString( 3, folderListingDatabase.getFolderPath(  ) );
        daoUtil.setString( 4, folderListingDatabase.getWorkgroup(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of FolderListingDatabase from the table
     * @param nFolderListingDatabaseId The identifier of FolderListingDatabase
     * @param plugin The Plugin using this data access service
     * @return the instance of the FolderListingDatabase
     */
    public FolderListingDatabase load( int nFolderListingDatabaseId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nFolderListingDatabaseId );
        daoUtil.executeQuery(  );

        FolderListingDatabase folderListingDatabase = null;

        if ( daoUtil.next(  ) )
        {
            folderListingDatabase = new FolderListingDatabase(  );
            folderListingDatabase.setId( daoUtil.getInt( 1 ) );
            folderListingDatabase.setFolderName( daoUtil.getString( 2 ) );
            folderListingDatabase.setFolderPath( daoUtil.getString( 3 ) );
            folderListingDatabase.setWorkgroup( daoUtil.getString( 4 ) );
        }

        daoUtil.free(  );

        return folderListingDatabase;
    }

    /**
     * Delete a record from the table
     * @param nFolderListingDatabaseId The FolderListingDatabase Id
     * @param plugin The Plugin using this data access service
     */
    public void delete( int nFolderListingDatabaseId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nFolderListingDatabaseId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param folderListingDatabase The reference of folderListingDatabase
     * @param plugin The Plugin using this data access service
     */
    public void store( FolderListingDatabase folderListingDatabase, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, folderListingDatabase.getId(  ) );
        daoUtil.setString( 2, folderListingDatabase.getFolderName(  ) );
        daoUtil.setString( 3, folderListingDatabase.getFolderPath(  ) );
        daoUtil.setString( 4, folderListingDatabase.getWorkgroup(  ) );
        daoUtil.setInt( 5, folderListingDatabase.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the list of folderListingDatabases
     * @param plugin The Plugin using this data access service
     * @return The Collection of the FolderListingDatabases
     */
    public List selectFolderListingDatabaseList( Plugin plugin )
    {
        List listFolderListingDatabases = new ArrayList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            FolderListingDatabase folderListingDatabase = new FolderListingDatabase(  );
            folderListingDatabase.setId( daoUtil.getInt( 1 ) );
            folderListingDatabase.setFolderName( daoUtil.getString( 2 ) );
            folderListingDatabase.setFolderPath( daoUtil.getString( 3 ) );
            folderListingDatabase.setWorkgroup( daoUtil.getString( 4 ) );

            listFolderListingDatabases.add( folderListingDatabase );
        }

        daoUtil.free(  );

        return listFolderListingDatabases;
    }
}
