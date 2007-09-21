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
package fr.paris.lutece.plugins.rss.service;

import fr.paris.lutece.plugins.rss.business.RssGeneratedFileHome;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * This class provides utilities to create RSS documents.
 */
public final class RssGeneratorService
{
    /* Constants */
    private static final String TEMPLATE_PUSH_RSS_XML = "admin/plugins/rss/rss_xml.html";
    private static final String MARK_ITEM_LIST = "itemList";
    private static final String MARK_RSS_SITE_NAME = "site_name";
    private static final String MARK_RSS_FILE_LANGUAGE = "file_language";
    private static final String MARK_RSS_SITE_URL = "site_url";
    private static final String MARK_RSS_SITE_DESCRIPTION = "site_description";
    private static final String MARK_ID_PORTLET = "id_portlet";
    private static final String PROPERTY_SITE_NAME = "lutece.name";
    private static final String PROPERTY_SITE_LANGUAGE = "rss.language";
    private static final String PROPERTY_WEBAPP_PROD_URL = "lutece.prod.url";

    /**
     * The path which points the rss files are stored
     */
    public static final String PROPERTY_RSS_STORAGE_FOLDER_PATH = "rss.storage.folder.path";
    public static final String PROPERTY_STORAGE_DIRECTORY_NAME = "rss.storage.directory.name";

    /**
     * Private constructor
     */
    private RssGeneratorService(  )
    {
    }

    ///////////////////////////////////////////////////////////////////////////
    // Create RSS document

    /**
     * Creates the push RSS document corresponding to the given portlet
     *
     * @param nIdPortlet the portlet id for wich the file is created
     * @param strRssFileDescription the Description
     * @return String the XML content of the RSS document
     */
    public static String createRssDocument( int nIdPortlet, String strRssFileDescription )
    {
        HashMap model = new HashMap(  );

        // Update the head of the document
        String strRssFileSiteName = AppPropertiesService.getProperty( PROPERTY_SITE_NAME );
        String strRssFileLanguage = AppPropertiesService.getProperty( PROPERTY_SITE_LANGUAGE );
        String strIdPortlet = Integer.toString( nIdPortlet );
        String strWebAppUrl = AppPropertiesService.getProperty( PROPERTY_WEBAPP_PROD_URL );
        String strSiteUrl = strWebAppUrl + "/";
        model.put( MARK_RSS_SITE_NAME, strRssFileSiteName );
        model.put( MARK_RSS_FILE_LANGUAGE, strRssFileLanguage );
        model.put( MARK_RSS_SITE_URL, strSiteUrl );
        model.put( MARK_ID_PORTLET, strIdPortlet );
        model.put( MARK_RSS_SITE_DESCRIPTION, strRssFileDescription );

        // Find documents by portlet
        List listDocuments = RssGeneratedFileHome.findDocumentsByPortlet( nIdPortlet );
        //The date must respect RFC-822 date-time
        model.put( MARK_ITEM_LIST, listDocuments );

        Locale locale = new Locale( strRssFileLanguage );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PUSH_RSS_XML, locale, model );

        return template.getHtml(  );
    }

    /**
     * Creates the pushrss file in the directory
     *
     * @param strRssFileName The file's name that must be deleted
     * @param strRssDocument The content of the new RSS file
     */
    public static void createFileRss( String strRssFileName, String strRssDocument )
    {
        FileWriter fileRssWriter = null;

        try
        {
            // fetches the pushRss directory path
            String strFolderPath = AppPathService.getPath( RssGeneratorService.PROPERTY_RSS_STORAGE_FOLDER_PATH, "" );

            // Test if the pushRss directory exist and create it if it doesn't exist
            if ( !new File( strFolderPath ).exists(  ) )
            {
                File fileFolder = new File( strFolderPath );
                fileFolder.mkdir(  );
            }

            // Creates a temporary RSS file
            String strFileRss = AppPathService.getPath( RssGeneratorService.PROPERTY_RSS_STORAGE_FOLDER_PATH, "" ) +
                strRssFileName;
            String strFileDirectory = AppPathService.getPath( RssGeneratorService.PROPERTY_RSS_STORAGE_FOLDER_PATH, "" );
            File fileRss = new File( strFileRss );
            File fileRssDirectory = new File( strFileDirectory );
            File fileRssTemp = File.createTempFile( "tmp", null, fileRssDirectory );
            fileRssWriter = new FileWriter( fileRssTemp );
            fileRssWriter.write( strRssDocument );
            fileRssWriter.flush(  );
            fileRssWriter.close(  );

            // Deletes the file if the file exists and renames the temporary file into the file
            if ( new File( strFileRss ).exists(  ) )
            {
                File file = new File( strFileRss );
                file.delete(  );
            }

            fileRssTemp.renameTo( fileRss );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( NullPointerException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
    }

    /**
     * Deletes the pushrss file in the directory
     *
     * @param strRssFileName The name of the RSS file
     * @param strPluginName The plugin's name
     */
    public static void deleteFileRss( String strRssFileName, String strPluginName )
    {
        try
        {
            // Define pushRss directory
            String strFileRss = AppPathService.getPath( RssGeneratorService.PROPERTY_RSS_STORAGE_FOLDER_PATH, "" ) +
                strRssFileName;

            // Delete the file if file exists
            if ( new File( strFileRss ).exists(  ) )
            {
                File file = new File( strFileRss );
                file.delete(  );
            }
        }
        catch ( NullPointerException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
    }
}
