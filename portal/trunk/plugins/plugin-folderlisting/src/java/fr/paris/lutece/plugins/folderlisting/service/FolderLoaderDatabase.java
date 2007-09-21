/*
 * Copyright (c) 2002-2004, Mairie de Paris
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
package fr.paris.lutece.plugins.folderlisting.service;

import fr.paris.lutece.plugins.folderlisting.business.FolderListingDatabase;
import fr.paris.lutece.plugins.folderlisting.business.FolderListingDatabaseHome;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.resource.Resource;
import fr.paris.lutece.portal.service.resource.ResourceLoader;

import java.util.ArrayList;
import java.util.Collection;


public class FolderLoaderDatabase implements ResourceLoader
{
    private static final String PLUGIN_NAME = "folderlisting";
    Plugin _plugin;

    /**
     * Gets all resource available for this Loader
     * @return A collection of resources
     */
    public Collection getResources(  )
    {
        if ( _plugin == null )
        {
            _plugin = PluginService.getPlugin( PLUGIN_NAME );
        }

        ArrayList listFolders = new ArrayList(  );

        for ( FolderListingDatabase folderListing : FolderListingDatabaseHome.findFolderListingDatabasesList( _plugin ) )
        {
            Folder folder = loadFolder( folderListing.getId(  ) );
            listFolders.add( folder );
        }

        return listFolders;
    }

    /**
     * Get a resource by its Id
     * @param strId The resource Id
     * @return The resource
     */
    public Resource getResource( String strId )
    {
        Resource resource = null;

        if ( !( strId.startsWith( "folder" ) ) )
        {
            resource = loadFolder( Integer.parseInt( strId ) );
        }

        return resource;
    }

    /**
     * Return the Folder
     * @param int The primary key of the folder to load
     * @return folder
     */
    private Folder loadFolder( int nIdFolder )
    {
        if ( _plugin == null )
        {
            _plugin = PluginService.getPlugin( PLUGIN_NAME );
        }

        Folder folder = new Folder(  );
        FolderListingDatabase folderListing = FolderListingDatabaseHome.findByPrimaryKey( nIdFolder, _plugin );
        folder.setName( folderListing.getFolderName(  ) );
        folder.setPath( folderListing.getFolderPath(  ) );
        folder.setWorkgroup( folderListing.getWorkgroup(  ) );

        String strFolderId = Integer.toString( nIdFolder );
        folder.setId( strFolderId );

        return folder;
    }
}
