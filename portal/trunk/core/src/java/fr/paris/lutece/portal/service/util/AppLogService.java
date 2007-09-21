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
package fr.paris.lutece.portal.service.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.InputStream;

import java.util.Properties;


/**
 *  This class provides writing services in the application logs files
 */
public final class AppLogService
{
    // Constants
    private static final String LOGGER_EVENTS = "lutece.event";
    private static final String LOGGER_DEBUG = "lutece.debug";
    private static final String LOGGER_ERRORS = "lutece.error";
    private static final String SYSTEM_PROPERTY_LOG4J_CONFIGURATION = "log4j.configuration";
    private static Logger _loggerEvents;
    private static Logger _loggerErrors;
    private static Logger _loggerDebug;

    /**
     * Creates a new AppLogService object.
     */
    private AppLogService(  )
    {
    }

    /**
     * initializes the errors log file and the application log file
     * @param strConfigPath The strConfigPath
     * @param strConfigFile The strConfigFile
     */
    public static void init( String strConfigPath, String strConfigFile )
    {
        //Initialize the logger and configures it with the values of the properties file : config.properties
        try
        {
            // Get a logger for errors
            _loggerErrors = Logger.getLogger( LOGGER_ERRORS );

            // Get a logger for application events
            _loggerEvents = Logger.getLogger( LOGGER_EVENTS );
            _loggerEvents.setAdditivity( false );

            // Get a logger for debug and trace
            _loggerDebug = Logger.getLogger( LOGGER_DEBUG );
            _loggerDebug.setAdditivity( false );

            // Load loggers configuration from the config.properties
            InputStream is = AppPathService.getResourceAsStream( strConfigPath, strConfigFile );
            Properties props = new Properties(  );
            props.load( is );
            PropertyConfigurator.configure( props );
            is.close(  );

            // Define the config.properties as log4j configuration file for other libraries using
            // the System property "log4j.configuration"
            String strAbsoluteConfigDirectoryPath = AppPathService.getAbsolutePathFromRelativePath( strConfigPath );
            String strLog4jConfigFile = strAbsoluteConfigDirectoryPath +
                ( ( strAbsoluteConfigDirectoryPath.endsWith( "/" ) ) ? "" : "/" ) + strConfigFile;
            System.setProperty( SYSTEM_PROPERTY_LOG4J_CONFIGURATION, strLog4jConfigFile );
        }
        catch ( Exception e )
        {
            System.err.println( "Bad Configuration of Log4j : " + e );
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Log4j wrappers

    /**
     * Tells if the logger accepts debug messages. If not it prevents to build
     * consuming messages that will be ignored.
     * @return True if the logger accepts debug messages, otherwise false.
     */
    public static boolean isDebugEnabled(  )
    {
        return _loggerDebug.isDebugEnabled(  );
    }

    /**
     * Log a message object with the DEBUG level. It is logged in application.log
     *
     * @param objToLog the message object to log
     */
    public static void debug( Object objToLog )
    {
        if ( _loggerDebug.isDebugEnabled(  ) )
        {
            _loggerDebug.debug( objToLog );
        }
    }

    /**
     * Tells if the logger accepts debug messages. If not it prevents to build
     * consuming messages that will be ignored.
     * @return True if the logger accepts debug messages, otherwise false.
     */
    public static boolean isDebugEnabled( String strLogger )
    {
        Logger logger = Logger.getLogger( strLogger );

        return logger.isDebugEnabled(  );
    }

    /**
     * Log a message object with the DEBUG level. It is logged in application.log
     *
     * @param objToLog the message object to log
     */
    public static void debug( String strLogger, Object objToLog )
    {
        Logger logger = Logger.getLogger( strLogger );

        if ( logger.isDebugEnabled(  ) )
        {
            logger.debug( objToLog );
        }
    }

    /**
     * Log a message object with the ERROR Level. It is logged in error.log
     *
     * @param objToLog the message object to log
     */
    public static void error( Object objToLog )
    {
        _loggerErrors.error( objToLog );
    }

    /**
     * Log a message object with the ERROR level including the stack trace of
     * the Throwable t passed as parameter. It
     * is logged in error.log
     *
     * @param message the message object to log
     * @param t the exception to log, including its stack trace
     */
    public static void error( Object message, Throwable t )
    {
        _loggerErrors.error( message, t );
    }

    /**
     * Log a message object with the INFO Level in application.log
     *
     * @param objToLog the message object to log
     */
    public static void info( Object objToLog )
    {
        if ( _loggerEvents.isInfoEnabled(  ) )
        {
            _loggerEvents.info( objToLog );
        }
    }
}
