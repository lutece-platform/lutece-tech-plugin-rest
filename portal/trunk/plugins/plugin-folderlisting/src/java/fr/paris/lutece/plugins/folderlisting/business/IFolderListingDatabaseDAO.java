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

import java.util.List;


/**
 *
 * @author lenaini
 */
public interface IFolderListingDatabaseDAO
{
    /**
     * Delete a record from the table
     * @param nFolderListingDatabaseId The FolderListingDatabase Id
     * @param plugin The Plugin using this data access service
     */
    void delete( int nFolderListingDatabaseId, Plugin plugin );

    /**
     * Insert a new record in the table.
     * @param folderListingDatabase The folderListingDatabase object
     * @param plugin The Plugin using this data access service
     */
    void insert( FolderListingDatabase folderListingDatabase, Plugin plugin );

    /**
     * Load the data of FolderListingDatabase from the table
     * @param nFolderListingDatabaseId The identifier of FolderListingDatabase
     * @param plugin The Plugin using this data access service
     * @return the instance of the FolderListingDatabase
     */
    FolderListingDatabase load( int nFolderListingDatabaseId, Plugin plugin );

    /**
     * Load the list of folderListingDatabases
     * @param plugin The Plugin using this data access service
     * @return The Collection of the FolderListingDatabases
     */
    List selectFolderListingDatabaseList( Plugin plugin );

    /**
     * Update the record in the table
     * @param folderListingDatabase The reference of folderListingDatabase
     * @param plugin The Plugin using this data access service
     */
    void store( FolderListingDatabase folderListingDatabase, Plugin plugin );
}
