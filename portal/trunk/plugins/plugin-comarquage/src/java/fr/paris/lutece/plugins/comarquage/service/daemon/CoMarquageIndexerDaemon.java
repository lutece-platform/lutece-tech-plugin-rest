/*
 * Copyright (c) 2002-2006, Mairie de Paris
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
package fr.paris.lutece.plugins.comarquage.service.daemon;

import fr.paris.lutece.plugins.comarquage.util.FileUtils;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.xml.XmlUtil;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.index.IndexWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;


/**
 * This class provides methods which allows Daemon to manage and process the
 * treatment of the indexing for comarquage.
 */
public class CoMarquageIndexerDaemon extends Daemon
{
    public static final String PROPERTY_INDEXING_BASE_PATH = ".indexing.basePath";
    public static final String PROPERTY_INDEXING_CDC_PATH = ".indexing.cdcPath";
    private static final String PROPERTY_INDEXING_CDC_TEMP_PATH = ".indexing.cdcTempPath";
    private static final String PROPERTY_INDEXING_XSL_PATH = ".indexing.xslPath";
    private static final String PROPERTY_INDEXING_XSL_BASE_VAR = ".path.xsl";
    private static final String PROPERTY_INDEXING_XML_BASE_VAR = ".path.xml";
    private static final String PROPERTY_INDEXING_CDC_XML_INDEX = ".indexing.cdcXmlIndex";
    private static final String PROPERTY_INDEXING_XSL_LOCAL_PATH = ".indexing.localXslPath";
    private static final String PROPERTY_INDEXING_LOCALS_PATH = ".indexing.localBasePath";
    private String _strMessage;

    /**
     * Creates a new CoMarquageIndexerDaemon object.
     */
    public CoMarquageIndexerDaemon(  )
    {
    }

    /**
     * Implementation of the run method of the Runnable interface.It processes
     * the daemon treatment.
     */
    public void run(  )
    {
        try
        {
            long lTimeStart = System.currentTimeMillis(  );

            //Daemon's core evolution
            String _strPluginName = getPluginName(  );

            // Launching of the indexing.
            String strCDCTempPath = AppPropertiesService.getProperty( _strPluginName + PROPERTY_INDEXING_CDC_TEMP_PATH );
            String strIdxTempPath = AppPathService.getPath( _strPluginName + PROPERTY_INDEXING_BASE_PATH, strCDCTempPath );

            // Create dir, if doesn't exist
            File fileIdxTempPath = new File( strIdxTempPath );

            if ( fileIdxTempPath.getParentFile(  ).exists(  ) )
            {
                fileIdxTempPath.getParentFile(  ).mkdirs(  );
            }
            else
            {
                FileUtils.deleteDirectory( fileIdxTempPath );
            }

            // Index CDC cards
            String strXslFile = AppPropertiesService.getProperty( _strPluginName + PROPERTY_INDEXING_XSL_PATH );
            String strXslPath = AppPathService.getPath( _strPluginName + PROPERTY_INDEXING_XSL_BASE_VAR, strXslFile );

            String strXmlFile = AppPropertiesService.getProperty( _strPluginName + PROPERTY_INDEXING_CDC_XML_INDEX );
            String strXmlPath = AppPathService.getPath( _strPluginName + PROPERTY_INDEXING_XML_BASE_VAR, strXmlFile );
            _strMessage = autoIndexUsingXSL( strXslPath, strXmlPath, strIdxTempPath, true, _strPluginName );

            long lTimeEnd1 = System.currentTimeMillis(  );
            AppLogService.info( _strPluginName + ": Time spent to index the public cards : " +
                ( lTimeEnd1 - lTimeStart ) );

            // Index local cards
            String strLocalXslFile = AppPropertiesService.getProperty( _strPluginName +
                    PROPERTY_INDEXING_XSL_LOCAL_PATH );
            String strLocalXslPath = AppPathService.getPath( _strPluginName + PROPERTY_INDEXING_XSL_BASE_VAR,
                    strLocalXslFile );
            String strLocalBasePath = AppPropertiesService.getProperty( _strPluginName + PROPERTY_INDEXING_LOCALS_PATH );
            String strLocalPath = AppPathService.getPath( _strPluginName + PROPERTY_INDEXING_XML_BASE_VAR,
                    strLocalBasePath );
            _strMessage += autoIndexAllUsingXSL( strLocalXslPath, strLocalPath, strIdxTempPath, false, _strPluginName );

            long lTimeEnd2 = System.currentTimeMillis(  );
            AppLogService.info( _strPluginName + ": Time spent to index the local cards : " + ( lTimeEnd2 - lTimeEnd1 ) );

            String strCDCPath = AppPropertiesService.getProperty( _strPluginName + PROPERTY_INDEXING_CDC_PATH );
            String strIdxPath = AppPathService.getPath( _strPluginName + PROPERTY_INDEXING_BASE_PATH, strCDCPath );

            // Optimize indexe
            optimizeIndexDirectory( strIdxTempPath );

            // The switching must be faster as possible
            File fileIdxTemp2 = new File( strIdxPath + "-todel" );
            File fileIdxTemp = new File( strIdxTempPath );
            File fileIdx = new File( strIdxPath );

            // If the index directory exist, rename it to "*-todel"
            if ( fileIdx.exists(  ) )
            {
                fileIdx.renameTo( fileIdxTemp2 );
            }

            // The new temp index becomes the new index directory
            fileIdxTemp.renameTo( fileIdx );

            // Delete the old index directory ("*-todel")
            if ( fileIdxTemp2.exists(  ) )
            {
                FileUtils.deleteDirectory( fileIdxTemp2 );
            }

            long lTimeEnd = System.currentTimeMillis(  );
            AppLogService.info( _strPluginName + ": Time spent for optimization and directory renaming: " +
                ( lTimeEnd - lTimeEnd2 ) );
            AppLogService.info( _strPluginName + ": Summary of the logs\n" + _strMessage );
            AppLogService.info( _strPluginName + ": Time spend for indexing: " + ( lTimeEnd - lTimeStart ) );
        }
        catch ( Exception e )
        {
            _strMessage = e.getMessage(  );
            AppLogService.error( "Error while making index: " + e.getMessage(  ), e );
        }
    }

    /**
     * Launches the auto-indexing
     * @param strXslPath the path of the Xsl files
     * @param strBasePath the base path
     * @param strIndexDirectory the index directory
     * @param bCreateDirectory true if directory should be created, false otherwise
     * @param strPluginName the plugin name
     * @return the files string
     */
    private String autoIndexAllUsingXSL( String strXslPath, String strBasePath, String strIndexDirectory,
        boolean bCreateDirectory, String strPluginName )
    {
        File fileBasePath = new File( strBasePath );

        return autoIndexAllUsingXSL( strXslPath, fileBasePath, strIndexDirectory, bCreateDirectory, strPluginName );
    }

    /**
     * Launches the auto-indexing
     * @param strXslPath the path of the Xsl files
     * @param fileBasePath the base path
     * @param strIndexDirectory the index directory
     * @param bCreateDirectory true if directory should be created, false otherwise
     * @param strPluginName the plugin name
     * @return the files string
     */
    private static String autoIndexAllUsingXSL( String strXslPath, File fileBasePath, String strIndexDirectory,
        boolean bCreateDirectory, String strPluginName )
    {
        if ( fileBasePath.isFile(  ) )
        {
            return autoIndexUsingXSL( strXslPath, fileBasePath.getAbsolutePath(  ), strIndexDirectory,
                bCreateDirectory, strPluginName );
        }

        StringBuffer result = new StringBuffer(  );
        File[] files = fileBasePath.listFiles(  );

        for ( File fileCurrent : files )
        {

            if ( !fileCurrent.getAbsolutePath(  ).endsWith( "CVS" ) )
            {
                result.append( autoIndexAllUsingXSL( strXslPath, fileCurrent, strIndexDirectory, false, strPluginName ) );
            }
        }

        return result.toString(  );
    }

    /**
     * Launches the auto indexing of a XML card.
     * @param strXslPath the path of the Xsl
     * @param strXmlPath the path of the Xml card
     * @param strIndexDirectory the directory of the index
     * @param bCreateDirectory true if directory should be created, false otherwise
     * @param strPluginName the plugin name
         * @return strContent
     */
    private static String autoIndexUsingXSL( String strXslPath, String strXmlPath, String strIndexDirectory,
        boolean bCreateDirectory, String strPluginName )
    {
        String strContent = null;

        try
        {
            StreamSource sourceStyleSheet = null;
            InputStream isXMLDocument = null;

            try
            {
                File fileXsl = new File( strXslPath );

                sourceStyleSheet = new StreamSource( fileXsl );

                isXMLDocument = new FileInputStream( strXmlPath );

                final StreamSource ssXMLDocument = new StreamSource( isXMLDocument );

                try
                {
                    Map<String, String> params = new HashMap<String, String>(  );
                    params.put( "targetDirectory", strIndexDirectory );
                    params.put( "createDirectory", ( bCreateDirectory ? "true" : "false" ) );
                    params.put( "pluginName", strPluginName );

                    /*(void)*/ XmlUtil.transform( ssXMLDocument, sourceStyleSheet, params, null );
                    strContent = "Parsed file '" + strXmlPath + "' (xsl '" + strXslPath + "')\n";
                }
                catch ( Exception e )
                {
                    strContent = "Can't parse file '" + strXmlPath + "' (xsl '" + strXslPath + "'), error: " +
                        e.getMessage(  ) + "\n";
                    AppLogService.error( strContent, e );
                }
            }
            finally
            {
                if ( isXMLDocument != null )
                {
                    isXMLDocument.close(  );
                }

                if ( ( sourceStyleSheet != null ) && ( sourceStyleSheet.getInputStream(  ) != null ) )
                {
                    sourceStyleSheet.getInputStream(  ).close(  );
                }
            }
        }
        catch ( IOException e )
        {
            strContent = "Can't parse file '" + strXmlPath + "' (xsl '" + strXslPath + "'), error: " +
                e.getMessage(  ) + "\n";
            AppLogService.error( strContent, e );
        }

        return strContent;
    }

    /**
     * Optimizes the index directory
     * @param strIndexePath the path of the index directory
     * @return true if ok, false otherwise
     */
    private boolean optimizeIndexDirectory( String strIndexePath )
    {
        Analyzer analyzer = new StopAnalyzer(  );
        IndexWriter writer = null;

        try
        {
            writer = new IndexWriter( strIndexePath, analyzer, false );
            writer.optimize(  );

            return true;
        }
        catch ( IOException e )
        {
            AppLogService.error( "Error while optimizing the index: " + e.getMessage(  ), e );

            return false;
        }
        finally
        {
            if ( writer != null )
            {
                try
                {
                    writer.close(  );
                }
                catch ( IOException e )
                {
                    AppLogService.error( "Error while closing the writer: " + e.getMessage(  ), e );
                }
            }
        }
    }
}
