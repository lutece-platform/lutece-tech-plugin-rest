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

import fr.paris.lutece.plugins.dbpage.business.section.DbPageSection;
import fr.paris.lutece.portal.service.resource.Resource;
import fr.paris.lutece.portal.service.resource.ResourceLoader;
import fr.paris.lutece.portal.service.util.AppException;
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
import java.util.List;
import java.util.Properties;


/**
 * This class provides the properties object loader
 */
public class DbPageLoaderProperties implements ResourceLoader
{
    private static final String PROPERTY_PAGE_TITLE = "page.title";
    private static final String PROPERTY_PAGE_NAME = "page.paramname";
    private static final String PROPERTY_PAGE_WORKGROUP = "page.workgroup";
    private static final String PROPERTY_PAGE_NUMBER_SECTIONS = "page.nbsections";
    private static final String PROPERTY_SECTION_CLASS = "dbpage.section.type.class.";
    private static final String PROPERTY_SECTION = "section";
    private static final String PROPERTY_SECTION_TYPE = ".type";
    private static final String PROPERTY_SECTION_TEMPLATE = ".template";
    private static final String PROPERTY_SECTION_COLUMN = ".column";
    private static final String PROPERTY_SECTION_SQL = ".sql";
    private static final String PROPERTY_SECTION_TITLE = ".title";
    private static final String PROPERTY_SECTION_POOL = ".pool";
    private static final String PROPERTY_SECTION_ROLE = ".role";
    private static final String PROPERTY_DBPAGE_FILES_PATH = "dbpage.files.path";
    private static final String EXT_DBPAGE_FILES = ".properties";
    private String _strDbPageFilesPath;

    /**
     * Constructor
     */
    public DbPageLoaderProperties(  )
    {
        super(  );
        _strDbPageFilesPath = AppPropertiesService.getProperty( PROPERTY_DBPAGE_FILES_PATH );
    }

    /**
     * Gets all resource available for this Loader
     * @return A collection of resources
     */
    public Collection getResources(  )
    {
        List<DbPage> listDbPage = new ArrayList(  );
        String strRootDirectory = AppPathService.getWebAppPath(  );
        List<File> listPages = null;

        try
        {
            listPages = FileSystemUtil.getFiles( strRootDirectory, _strDbPageFilesPath );

            for ( File file : listPages )
            {
                if ( file.getName(  ).endsWith( EXT_DBPAGE_FILES ) )
                {
                    DbPage dbPage = loadPage( file );
                    listDbPage.add( dbPage );
                }
            }
        }
        catch ( DirectoryNotFoundException e )
        {
            throw new AppException( e.getMessage(  ), e );
        }

        return listDbPage;
    }

    /**
     * Get a resource by its Id
     * @param strId The resource Id
     * @return The resource
     */
    public Resource getResource( String strId )
    {
        Resource resource = null;
        String strFilePath = AppPathService.getPath( PROPERTY_DBPAGE_FILES_PATH, strId + EXT_DBPAGE_FILES );
        File file = new File( strFilePath );

        if ( file.exists(  ) )
        {
            resource = loadPage( file );
        }

        return resource;
    }

    /**
     * Return the page
     * @param file The File to load
     * @return dbPage
     */
    private DbPage loadPage( File file )
    {
        DbPage dbPage = new DbPage(  );
        Properties properties = new Properties(  );

        try
        {
            FileInputStream is = new FileInputStream( file );
            properties.load( is );

            // read page data in the properties file
            dbPage.setName( properties.getProperty( PROPERTY_PAGE_NAME ) );
            dbPage.setTitle( properties.getProperty( PROPERTY_PAGE_TITLE ) );
            dbPage.setWorkgroup( properties.getProperty( PROPERTY_PAGE_WORKGROUP ) );

            String strNbSection = properties.getProperty( PROPERTY_PAGE_NUMBER_SECTIONS );
            int nSections = 0;
            nSections = Integer.parseInt( strNbSection );
            dbPage.setNbSection( nSections );

            List myCollection = loadListSection( properties, nSections );
            dbPage.setListSection( myCollection );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        return dbPage;
    }

    /**
     * Return a list of sections
     * @param properties the list of properties
     * @param nSections the section of the properties
     * @return the collection of dbPageSection
     */
    public List<DbPageSection> loadListSection( Properties properties, int nSections )
    {
        List<DbPageSection> listSections = new ArrayList(  );

        for ( int i = 1; i <= nSections; i++ )
        {
            String strPrefixSection = PROPERTY_SECTION + i;
            String strType = properties.getProperty( strPrefixSection + PROPERTY_SECTION_TYPE );
            String strSectionClass = AppPropertiesService.getProperty( PROPERTY_SECTION_CLASS + strType );

            DbPageSection dbPageSection = null;

            try
            {
                dbPageSection = (DbPageSection) Class.forName( strSectionClass ).newInstance(  );
            }
            catch ( ClassNotFoundException e )
            {
                AppLogService.error( e.getMessage(  ), e );

                return listSections;
            }
            catch ( IllegalAccessException e )
            {
                AppLogService.error( e.getMessage(  ), e );

                return listSections;
            }
            catch ( InstantiationException e )
            {
                AppLogService.error( e.getMessage(  ), e );

                return listSections;
            }

            dbPageSection.setType( properties.getProperty( strPrefixSection + PROPERTY_SECTION_TYPE ) );
            dbPageSection.setTitle( properties.getProperty( strPrefixSection + PROPERTY_SECTION_TITLE ) );
            dbPageSection.setSql( properties.getProperty( strPrefixSection + PROPERTY_SECTION_SQL ) );
            dbPageSection.setDbPool( properties.getProperty( strPrefixSection + PROPERTY_SECTION_POOL ) );
            dbPageSection.setRole( properties.getProperty( strPrefixSection + PROPERTY_SECTION_ROLE ) );
            dbPageSection.setTemplatePath( properties.getProperty( strPrefixSection + PROPERTY_SECTION_TEMPLATE ) );

            String strTemplate = properties.getProperty( strPrefixSection + PROPERTY_SECTION_TEMPLATE );
            dbPageSection.setTemplatePath( strTemplate );

            //Columns name loading
            String strColumnsName = properties.getProperty( strPrefixSection + PROPERTY_SECTION_COLUMN );

            if ( strColumnsName != null )
            {
                ArrayList aCollumnsName = new ArrayList(  );

                while ( !strColumnsName.equals( "" ) )
                {
                    int nPos = strColumnsName.indexOf( "," );

                    if ( nPos != -1 )
                    {
                        String strValue = strColumnsName.substring( 0, nPos );
                        strValue = strValue.trim(  );
                        strColumnsName = strColumnsName.substring( nPos + 1 );
                        aCollumnsName.add( strValue );
                    }
                    else
                    {
                        aCollumnsName.add( strColumnsName.trim(  ) );
                        strColumnsName = "";
                    }

                    dbPageSection.setColumnNames( aCollumnsName );
                }
            }

            listSections.add( dbPageSection );
        }

        return listSections;
    }
}
