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
package fr.paris.lutece.plugins.folderlisting.service;

import fr.paris.lutece.portal.service.resource.Resource;
import fr.paris.lutece.portal.service.resource.ResourceLoader;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.filesystem.DirectoryNotFoundException;
import fr.paris.lutece.util.filesystem.FileSystemUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;


public class FolderLoaderProperties implements ResourceLoader
{
    /////////////////////////////////////////////////////////////////////
    ////Properties
    private static final String PROPERTY_FOLDER_FILES_PATH = "folderlisting.files.path";
    private static final String PROPERTY_FOLDER_NAME = "folder.name";
    private static final String PROPERTY_FOLDER_PATH = "folder.path";
    private static final String PROPERTY_FOLDER_WORKGROUP = "folder.workgroup";
    private static final String EXT_FOLDER_FILES = ".properties";
    private String _strFolderFilesPath;

    /**
     * Constructor
     */
    public FolderLoaderProperties(  )
    {
        super(  );
        _strFolderFilesPath = AppPropertiesService.getProperty( PROPERTY_FOLDER_FILES_PATH );
    }

    /**
     * Gets all resource available for this Loader
     * @return A collection of resources
     */
    public Collection getResources(  )
    {
        ArrayList listFolders = new ArrayList(  );

        try
        {
            String strRootDirectory = AppPathService.getWebAppPath(  );

            for ( File file : FileSystemUtil.getFiles( strRootDirectory, _strFolderFilesPath ) )
            {
                String fileName = file.getName(  );

                if ( fileName.endsWith( EXT_FOLDER_FILES ) )
                {
                    Folder folder = loadFolder( file );
                    listFolders.add( folder );
                }
            }
        }
        catch ( DirectoryNotFoundException e )
        {
            AppLogService.error( e.getMessage(  ), e );
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
        String strFilePath = AppPathService.getPath( PROPERTY_FOLDER_FILES_PATH, strId + EXT_FOLDER_FILES );
        File file = new File( strFilePath );

        if ( file.exists(  ) )
        {
            resource = loadFolder( file );
        }

        return resource;
    }

    /**
     * Return the page
     * @param file The File to load
     * @return folder
     */
    private Folder loadFolder( File file )
    {
        Folder folder = new Folder(  );
        Properties properties = new Properties(  );

        try
        {
            FileInputStream is = new FileInputStream( file );
            properties.load( is );
            folder.setName( properties.getProperty( PROPERTY_FOLDER_NAME ) );
            folder.setPath( properties.getProperty( PROPERTY_FOLDER_PATH ) );
            folder.setWorkgroup( properties.getProperty( PROPERTY_FOLDER_WORKGROUP ) );

            String strFolderId = file.getName(  ).substring( 0, file.getName(  ).lastIndexOf( "." ) );
            folder.setId( strFolderId );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        return folder;
    }
}
