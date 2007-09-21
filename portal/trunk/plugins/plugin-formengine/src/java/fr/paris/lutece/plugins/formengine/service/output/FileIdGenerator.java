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
package fr.paris.lutece.plugins.formengine.service.output;

import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * This abstract class defines the basis for id generation from a file.
 *
 */
public abstract class FileIdGenerator extends IdGenerator
{
    private String _strFileName;
    private String _strDirectoryPathProperty;

    /**
     * @see fr.paris.lutece.plugins.formengine.service.output.IdGenerator#loadId()
     */
    protected String loadId(  )
    {
        String strId = null;

        try
        {
            //            File file = new File( AppPathService.getPath( getDirectoryPathProperty(  ), getFileName(  ) ) );
            File file = new File( AppPropertiesService.getProperty( getDirectoryPathProperty(  ) ) + getFileName(  ) );

            // return true to indicate a directory creation, false if directory exist
            file.getParentFile(  ).mkdirs(  );

            // return true to indicate a file creation, false if file exist
            boolean bNewFile = file.createNewFile(  );

            RandomAccessFile accessfile = new RandomAccessFile( file, "r" );

            if ( bNewFile == false )
            {
                strId = accessfile.readUTF(  );
            }

            accessfile.close(  );
        }
        catch ( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace(  );
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace(  );
        }

        return strId;
    }

    /**
     * @see fr.paris.lutece.plugins.formengine.service.output.IdGenerator#storeId(java.lang.String)
     */
    protected void storeId( String strId )
    {
        try
        {
            //            File file = new File( AppPathService.getPath( getDirectoryPathProperty(  ), getFileName(  ) ) );
            File file = new File( AppPropertiesService.getProperty( getDirectoryPathProperty(  ) ) + getFileName(  ) );

            // return true to indicate a directory creation, false if directory exist
            file.getParentFile(  ).mkdirs(  );

            // return true to indicate a file creation, false if file exist
            boolean bNewFile = file.createNewFile(  );

            RandomAccessFile accessfile = new RandomAccessFile( file, "rw" );

            if ( bNewFile == false )
            {
                accessfile.seek( 0 );
                accessfile.writeUTF( strId );
            }

            accessfile.close(  );
        }
        catch ( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace(  );
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace(  );
        }
    }

    /**
     * Get the name of the file storing the id
     * @return the filename
     */
    protected String getFileName(  )
    {
        return _strFileName;
    }

    /**
     * Set the name of the file storing the id
     * @param strFileName the name of the id file
     */
    protected void setFileName( String strFileName )
    {
        _strFileName = strFileName;
    }

    /**
     * Get the property giving the path name
     * the id is stored
     * @return the property
     */
    protected String getDirectoryPathProperty(  )
    {
        return _strDirectoryPathProperty;
    }

    /**
     * Set the property giving the directory path name
     * the id is stored
     * @param strDirectoryPathProperty the property
     */
    protected void setDirectoryPathProperty( String strDirectoryPathProperty )
    {
        _strDirectoryPathProperty = strDirectoryPathProperty;
    }
}
