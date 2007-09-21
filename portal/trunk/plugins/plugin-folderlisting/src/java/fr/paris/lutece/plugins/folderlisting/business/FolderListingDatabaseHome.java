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
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for FolderListingDatabase objects
 */
public final class FolderListingDatabaseHome
{
    // Static variable pointed at the DAO instance
    private static IFolderListingDatabaseDAO _dao = (IFolderListingDatabaseDAO) SpringContextService.getPluginBean( "folderlisting",
            "folderListingDatabaseDAO" );

    /**
     * Creation of an instance of folderListingDatabase
     *
     * @param folderListingDatabase The instance of the folderListingDatabase which contains the informations to store
     * @param plugin The current plugin using this method
     * @return The  instance of folderListingDatabase which has been created with its primary key.
     */
    public static FolderListingDatabase create( FolderListingDatabase folderListingDatabase, Plugin plugin )
    {
        _dao.insert( folderListingDatabase, plugin );

        return folderListingDatabase;
    }

    /**
     * Update of the folderListingDatabase which is specified in parameter
     *
     * @param folderListingDatabase The instance of the folderListingDatabase which contains the data to store
     * @param plugin The current plugin using this method
     * @return The instance of the  folderListingDatabase which has been updated
     */
    public static FolderListingDatabase update( FolderListingDatabase folderListingDatabase, Plugin plugin )
    {
        _dao.store( folderListingDatabase, plugin );

        return folderListingDatabase;
    }

    /**
     * Remove the FolderListingDatabase whose identifier is specified in parameter
     *
     * @param nFolderListingDatabaseId The FolderListingDatabase Id
     * @param plugin The current plugin using this method
     */
    public static void remove( int nFolderListingDatabaseId, Plugin plugin )
    {
        _dao.delete( nFolderListingDatabaseId, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a folderListingDatabase whose identifier is specified in parameter
     *
     * @param nKey The Primary key of the folderListingDatabase
     * @param plugin The current plugin using this method
     * @return An instance of folderListingDatabase
     */
    public static FolderListingDatabase findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Returns a list of folderListingDatabases objects
     * @param plugin The current plugin using this method
     * @return A list of folders in the database
     */
    public static List<FolderListingDatabase> findFolderListingDatabasesList( Plugin plugin )
    {
        return _dao.selectFolderListingDatabaseList( plugin );
    }
}
