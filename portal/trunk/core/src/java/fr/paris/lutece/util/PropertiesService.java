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
package fr.paris.lutece.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * This class provides utility methods to read values of the properties stored in the .properties file of the
 * application.
 */
public class PropertiesService
{
    // Static variables
    private static String _strRootPath;
    private static Properties _properties = new Properties(  );
    private static Map<String, String> _mapPropertiesFiles = new HashMap<String, String>(  );

    /**
     * Constructor should define the base root path for properties files
     * @param strRootPath The root path
     */
    public PropertiesService( String strRootPath )
    {
        _strRootPath = ( strRootPath.endsWith( "/" ) ) ? strRootPath : ( strRootPath + "/" );
    }

    /**
     * Add properties from a properties file
     * @param strRelativePath Relative path from the root path
     * @param strFilename The filename of the properties file (ie: config.properties)
     * @throws java.io.FileNotFoundException If the file is not found
     * @throws java.io.IOException If an error occurs reading the file
     */
    public void addPropertiesFile( String strRelativePath, String strFilename )
        throws FileNotFoundException, IOException
    {
        String strFullPath = _strRootPath +
            ( ( strRelativePath.endsWith( "/" ) ) ? strRelativePath : ( strRelativePath + "/" ) ) + strFilename;
        _mapPropertiesFiles.put( strFilename, strFullPath );
        loadFile( strFullPath );
    }

    /**
     * Add properties from all files found in a given directory
     * @param strRelativePath Relative path from the root path
     * @throws java.io.FileNotFoundException If the file is not found
     * @throws java.io.IOException If an error occurs reading the file
     */
    public void addPropertiesDirectory( String strRelativePath )
        throws FileNotFoundException, IOException
    {
        File directory = new File( _strRootPath + strRelativePath );

        if ( ( directory != null ) && directory.exists(  ) )
        {
            File[] listFile = directory.listFiles(  );

            for ( int i = 0; i < listFile.length; i++ )
            {
                File file = (File) listFile[i];

                if ( file.getName(  ).endsWith( ".properties" ) )
                {
                    String strFullPath = file.getAbsolutePath(  );
                    _mapPropertiesFiles.put( file.getName(  ), strFullPath );
                    loadFile( strFullPath );
                }
            }
        }
    }

    /**
     * Load properties of a file
     * @param strFullPath The absolute path of the properties file
     * @throws java.io.FileNotFoundException If the file is not found
     * @throws java.io.IOException If an error occurs reading the file
     */
    private void loadFile( String strFullPath ) throws FileNotFoundException, IOException
    {
        File file = new File( strFullPath );
        FileInputStream fis = new FileInputStream( file );
        _properties.load( fis );
    }

    /**
     * Reload a properties file .
     * @param strFilename The filename of the properties file
     * @throws java.io.FileNotFoundException If the file is not found
     * @throws java.io.IOException If an error occurs reading the file
     */
    public void reload( String strFilename ) throws FileNotFoundException, IOException
    {
        String strFullPath = _mapPropertiesFiles.get( strFilename );
        loadFile( strFullPath );
    }

    /**
     * Reload all properties files
     * @throws java.io.FileNotFoundException If the file is not found
     * @throws java.io.IOException If an error occurs reading the file
     */
    public void reloadAll(  ) throws FileNotFoundException, IOException
    {
        for ( String strFullPath : _mapPropertiesFiles.values(  ) )
        {
            loadFile( strFullPath );
        }
    }

    /**
     * Returns the value of a variable defined in the .properties file of the application as a String
     *
     * @param strProperty The variable name
     * @return The variable value read in the properties file
     */
    public String getProperty( String strProperty )
    {
        return _properties.getProperty( strProperty );
    }

    /**
     * Returns the value of a variable defined in the .properties file of the application as a String
     *
     * @param strProperty The variable name
     * @param strDefault The default value which is returned if no value is found for the variable in the .properties
     *        file.
     * @return The variable value read in the properties file
     */
    public String getProperty( String strProperty, String strDefault )
    {
        return _properties.getProperty( strProperty, strDefault );
    }

    /**
     * Returns the value of a variable defined in the .properties file of the application as an int
     *
     * @param strProperty The variable name
     * @param nDefault The default value which is returned if no value is found for the variable in the le downloadFile
     *        .properties. .properties file.
     * @return The variable value read in the properties file
     */
    public int getPropertyInt( String strProperty, int nDefault )
    {
        String strValue = _properties.getProperty( strProperty );

        if ( strValue == null )
        {
            return nDefault;
        }

        int nValue = Integer.parseInt( strValue );

        return nValue;
    }
}
