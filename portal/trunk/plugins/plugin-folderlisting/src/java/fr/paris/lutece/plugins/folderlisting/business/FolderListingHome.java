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

import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.filesystem.DirectoryNotFoundException;
import fr.paris.lutece.util.filesystem.FileSystemUtil;

import java.io.File;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


/**
 * This class provides instances management methods (accept, getDescription, getFiles) for FolderListingFile objects.
 */
public class FolderListingHome
{
    ///////////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String PROPERTY_DATE_FORMAT = "folderlisting.format.date";

    /**
     * Return file informations
     * @param strPath The relative path of the folder
     * @return A collection of FolderListFiles
     */
    public static Collection<FolderListingFile> getFiles( String strPath )
        throws DirectoryNotFoundException
    {
        Collection listFiles = new ArrayList(  );

        for ( File file : FileSystemUtil.getFiles( AppPathService.getWebAppPath(  ), strPath ) )
        {
            FolderListingFile f = new FolderListingFile(  );

            // Get the file extension by getting the position of the last dot
            String strExtension = null;
            int nPos = file.getName(  ).lastIndexOf( "." );

            if ( nPos != -1 )
            {
                strExtension = file.getName(  ).substring( nPos + 1 );
            }

            // Get the last modified date formatted
            String strDateFormat = AppPropertiesService.getProperty( PROPERTY_DATE_FORMAT );
            DateFormat formatter = new SimpleDateFormat( strDateFormat );
            Date date = new Date( file.lastModified(  ) );
            String strDate = formatter.format( date );

            // Sets attributes of the FolderListingFile
            f.setName( file.getName(  ) );
            f.setSize( "" + ( ( file.length(  ) / 1024L ) + 1 ) );
            f.setPath( strPath );
            f.setDate( strDate );
            f.setExtension( strExtension );

            listFiles.add( f );
        }

        return listFiles;
    }

    /**
     * Return subdirectories
     * @param strPath The relative path of the folder
     * @return A collection of FolderListFiles
     */
    public static Collection<FolderListingDirectory> getSubDirectories( String strPath )
        throws DirectoryNotFoundException
    {
        Collection listDirs = new ArrayList(  );

        for ( File file : FileSystemUtil.getSubDirectories( AppPathService.getWebAppPath(  ), strPath ) )
        {
            FolderListingDirectory dir = new FolderListingDirectory(  );

            // Get the last modified date formatted
            String strDateFormat = AppPropertiesService.getProperty( PROPERTY_DATE_FORMAT );
            DateFormat formatter = new SimpleDateFormat( strDateFormat );
            Date date = new Date( file.lastModified(  ) );
            String strDate = formatter.format( date );

            // Sets attributes of the FolderListingFile
            dir.setName( file.getName(  ) );
            dir.setPath( strPath );
            dir.setDate( strDate );

            listDirs.add( dir );
        }

        return listDirs;
    }
}
