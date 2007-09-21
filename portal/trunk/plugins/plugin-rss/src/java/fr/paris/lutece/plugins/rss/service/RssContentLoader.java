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

import fr.paris.lutece.plugins.rss.business.RssFeed;
import fr.paris.lutece.plugins.rss.business.RssFeedHome;
import fr.paris.lutece.plugins.rss.util.http.HttpAccess;
import fr.paris.lutece.plugins.rss.util.http.HttpAccessException;
import fr.paris.lutece.portal.service.resource.Resource;
import fr.paris.lutece.portal.service.resource.ResourceLoader;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 *
 * This class delivers RSS contents loaded from Internet Feeds
 */
public class RssContentLoader implements ResourceLoader
{
    private static final String PLUGIN_NAME = "rss";
    private static final String PROPERTY_CACHE_DIRECTORY = "rss.content.service.cache.directory";
    private static final String FILE_EXT = ".xml";
    private static final int FETCH_OK = 0;
    private static final int FETCH_ERROR = 1;

    /**
     * Implementation of the ResourceLoader interface
     * @return A collection of resources
     */
    public Collection getResources(  )
    {
        ArrayList listRssContents = new ArrayList(  );
        Collection colRssFeeds = RssFeedHome.getRssFeeds(  );
        Iterator i = colRssFeeds.iterator(  );

        while ( i.hasNext(  ) )
        {
            RssFeed rss = (RssFeed) i.next(  );
            Resource resource = getResource( "" + rss.getId(  ) );
            listRssContents.add( resource );
        }

        return listRssContents;
    }

    /**
     * Implementation of the ResourceLoader interface
     * @param strId The resource Id
     * @return The Resource
     */
    public Resource getResource( String strId )
    {
        RssContent rssContent = new RssContent(  );
        String strContent = getFromCacheFile( strId );

        if ( strContent == null )
        {
            int nId = Integer.parseInt( strId );

            try
            {
                strContent = fetchRssFeed( nId );
            }
            catch ( RssParsingException ex )
            {
                AppLogService.error( ex.getMessage(  ) );
            }
        }

        rssContent.setContent( strContent );

        return rssContent;
    }

    /**
     * Fetch a RSS feed
     * @param nId The RSS feed Id
     * @return The content of the RSS feed
     * @throws RssParsingException
     */
    public static String fetchRssFeed( int nId ) throws RssParsingException
    {
        String strContent = null;
        RssFeed rss = RssFeedHome.findByPrimaryKey( nId );
        HttpAccess httpAccess = new HttpAccess(  );
        httpAccess.init( PLUGIN_NAME );

        try
        {
            strContent = httpAccess.doGet( rss.getUrl(  ) );
            rss.setLastFetchStatus( FETCH_OK );
            rss.setLastFetchError( "" );
            saveInCacheFile( rss.getId(  ), strContent );
        }
        catch ( HttpAccessException e )
        {
            rss.setLastFetchStatus( FETCH_ERROR );
            rss.setLastFetchError( e.getMessage(  ) );
        }

        rss.setLastFetchDate( new Timestamp( new Date(  ).getTime(  ) ) );
        RssFeedHome.updateLastFetchInfos( rss );

        return strContent;
    }

    /**
     * Gets the content of the RSS feed from a cache file
     * @param strId The RS feed Id
     * @return The content of the RSS feed
     */
    private static String getFromCacheFile( String strId )
    {
        String strContent = null;
        String strFilePath = AppPathService.getPath( PROPERTY_CACHE_DIRECTORY, strId + FILE_EXT );
        File file = new File( strFilePath );
        BufferedReader input = null;

        StringBuffer sbContent = new StringBuffer(  );

        try
        {
            //use buffering
            //this implementation reads one line at a time
            //FileReader always assumes default encoding is OK!
            input = new BufferedReader( new FileReader( file ) );

            String strLine = null;

            while ( ( strLine = input.readLine(  ) ) != null )
            {
                sbContent.append( strLine );
            }

            strContent = sbContent.toString(  );
        }
        catch ( FileNotFoundException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        finally
        {
            try
            {
                if ( input != null )
                {
                    //flush and close both "input" and its underlying FileReader
                    input.close(  );
                }
            }
            catch ( IOException e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }
        }

        return strContent;
    }

    /**
     * Save the content of the RSS feed in a cache file
     * @param nId The RSS feed ID
     * @param strContent The content of the RSS feed to store
     */
    private static void saveInCacheFile( int nId, String strContent )
    {
        String strFilePath = AppPathService.getPath( PROPERTY_CACHE_DIRECTORY, "" + nId + FILE_EXT );

        try
        {
            File file = new File( strFilePath );

            if ( file.exists(  ) )
            {
                file.delete(  );
            }

            FileOutputStream fos = new FileOutputStream( file );
            fos.flush(  );
            fos.write( strContent.getBytes(  ) );
            fos.close(  );
        }
        catch ( IOException e )
        {
            AppLogService.error( e );
        }
    }

    public static String fetchAllRssFeeds(  )
    {
        StringBuffer sbProcessingLog = new StringBuffer(  );
        Collection colRssFeeds = RssFeedHome.getRssFeeds(  );
        Iterator i = colRssFeeds.iterator(  );

        while ( i.hasNext(  ) )
        {
            RssFeed rss = (RssFeed) i.next(  );

            try
            {
                fetchRssFeed( rss.getId(  ) );
            }
            catch ( RssParsingException ex )
            {
                AppLogService.error( ex.getMessage(  ) );
            }

            sbProcessingLog.append( "\nFetching " + rss.toString(  ) );
        }

        return sbProcessingLog.toString(  );
    }

    /**
     *  Use parsing for validate the modify xsl file
     *  @param strContent the xml content
     *  @return the message exception when the validation is false
     *  @throws Exception
     */
    private static boolean isValid( String strContent )
        throws SAXException, IOException, ParserConfigurationException
    {
        SAXParserFactory factory = SAXParserFactory.newInstance(  );
        SAXParser analyzer = null;
        analyzer = factory.newSAXParser(  );
        analyzer.getXMLReader(  ).parse( strContent );

        return true;
    }
}
