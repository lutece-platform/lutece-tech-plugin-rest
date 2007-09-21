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
package fr.paris.lutece.plugins.jcr.business;

import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.springmodules.jcr.JcrTemplate;

import sun.net.www.MimeTable;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import java.util.Calendar;
import java.util.Properties;

import javax.jcr.Node;
import javax.jcr.RepositoryException;


/**
 * An IRepositoryFileDAO implementation for Alfresco JCR
 */
public class AlfrescoRepositoryFileDAO extends AbstractRepositoryContentDAO implements IRepositoryFileDAO
{
    private static final String PROPERTY_JCR_CREATED = "JCR_CREATED";
    private static Properties _properties;

    /** This class implements the Singleton design pattern. */
    private static AlfrescoRepositoryFileDAO _dao;
    protected String PATH_SEPARATOR = "/";
    protected String RESOURCE_TYPE = "JSR170_FILE";

    ///////////////////////////////////////////////////////////////////////////////////////
    // Constructor and instanciation methods

    /**
     * Returns the unique instance of the singleton.
     * @param jcrTemplate a jcrTemplate
     * @param repositoryInitializer a repositoryInitializer
     * @param strDefaultWorkspaceName a default workspace name
     * @return the instance
     */
    static IRepositoryFileDAO getInstance( JcrTemplate jcrTemplate, IRepositoryInitializer repositoryInitializer,
        String strDefaultWorkspaceName )
    {
        if ( _dao == null )
        {
            _properties = new Properties(  );
            _properties.setProperty( NODE_TYPE_FOLDER, "cm:folder" );
            _properties.setProperty( NODE_TYPE_FILE, "cm:content" );
            _properties.setProperty( NODE_TYPE_FILE_RESOURCE, "nt:resource" );
            //            _properties.setProperty( ATTRIBUTE_NODE_SIZE, "lutece:size");
            _properties.setProperty( NODE_TYPE_JCR_CONTENT, "cm:content" );
            _properties.setProperty( REGEXP_ABSOLUTE_PATH, "^/+" );
            _properties.setProperty( ROOT_NODE_PATH, "app:company_home" );
            _properties.setProperty( DEFAULT_MIME_TYPE, "application/octet-stream" );
            _properties.setProperty( PROPERTY_JCR_MIMETYPE, "jcr:mimeType" );
            _properties.setProperty( PROPERTY_JCR_DATA, "jcr:data" );
            _properties.setProperty( PROPERTY_JCR_LASTMODIFIED, "cm:modified" );
            _properties.setProperty( PROPERTY_JCR_NAME, "cm:name" );
            _properties.setProperty( MIXIN_VERSIONNABLE, "cm:versionable" );
            _properties.setProperty( PROPERTY_JCR_CREATED, "cm:created" );
            _dao = new AlfrescoRepositoryFileDAO(  );
            _dao.init( jcrTemplate, repositoryInitializer, strDefaultWorkspaceName );
        }

        return _dao;
    }

    /**
     * Get the absolute path of a node
     * @param node the node
     * @return a string containing the absolute path or null if node doesn't exist
     */
    protected String getAbsolutePath( Node node )
    {
        if ( node != null )
        {
            try
            {
                return node.getPath(  ).replaceFirst( getProperties( ).getProperty( ROOT_NODE_PATH ), "" );
            }
            catch ( RepositoryException e )
            {
                e.printStackTrace(  );
            }
        }

        return null;
    }

    /**
     * Get the node content
     * @param node the node
     * @return an inputstream or null if content doesn't exist
     */
    protected InputStream getFileContent( Node node )
    {
        InputStream fileContent = null;

        try
        {
            fileContent = node.getProperty( getProperties( ).getProperty( NODE_TYPE_JCR_CONTENT ) ).getStream(  );
        }
        catch ( RepositoryException e )
        {
            // nothing to do
            AppLogService.error( e.getLocalizedMessage(  ), e );
        }

        return fileContent;
    }

    /**
     * Get the size of node content
     * @param node the node
     * @return the size in bytes
     */
    protected long getSize( Node node )
    {
        long size = 0L;
        InputStream fileContent = getFileContent( node );
        long skip;

        try
        {
            do
            {
                skip = fileContent.skip( Long.MAX_VALUE );
                size += skip;
            }
            while ( skip > 0L );
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace(  );
        }
        finally
        {
            try
            {
                fileContent.close(  );
            }
            catch ( IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace(  );
            }
        }

        return size;
    }

    /**
     * Get the node type
     * @param node the node
     * @return true if it is a directory, false otherwise
     */
    protected boolean isDirectory( Node node )
    {
        return isTypeOf( node, getProperties( ).getProperty( NODE_TYPE_FOLDER ) );
    }

    /**
     * Get the node type
     * @param node the node
     * @return true if it is a file, false otherwise
     */
    protected boolean isFile( Node node )
    {
        return isTypeOf( node, getProperties( ).getProperty( NODE_TYPE_FILE ) );
    }

    /**
     * Get the node existence
     * @param node the node
     * @return true if node exists, false otherwise
     */
    protected boolean exists( Node node )
    {
        return node != null;
    }

    /**
     * Get the Mime type
     * @param node the node
     * @return the mime type
     */
    protected String getMimeType( Node node )
    {
        // TODO get the mime type
        String mimeType = null;

        try
        {
            MimeTable mt = MimeTable.getDefaultTable(  );
            mimeType = mt.getContentTypeFor( node.getName(  ) );

            if ( mimeType == null )
            {
                mimeType = "application/octet-stream";
            }
        }
        catch ( Exception e )
        {
            // ignore error
            AppLogService.debug( e );
        }

        return mimeType;
    }

    /**
     * Get the creation date
     * @param node the node
     * @return the creation date
     */
    protected Calendar getCreated( Node node )
    {
        Calendar created = null;

        try
        {
            created = node.getProperty( getProperties( ).getProperty( PROPERTY_JCR_CREATED ) ).getDate(  );
        }
        catch ( RepositoryException e )
        {
            // nothing to do
            AppLogService.error( e.getLocalizedMessage(  ), e );
        }

        return created;
    }

    /**
     * Get the modification date
     * @param node the node
     * @return the modification date
     */
    protected Calendar getLastModified( Node node )
    {
        Calendar lastModified = null;

        try
        {
            lastModified = node.getProperty( getProperties( ).getProperty( PROPERTY_JCR_LASTMODIFIED ) ).getDate(  );
        }
        catch ( RepositoryException e )
        {
            // nothing to do
            AppLogService.error( e.getLocalizedMessage(  ), e );
        }

        return lastModified;
    }

    /**
     * Test the node type
     * @param node the node
     * @param strType a type
     * @return return true is node type is strType, false otherwise
     */
    private boolean isTypeOf( Node node, String strType )
    {
        try
        {
            return ( node != null ) && node.isNodeType( strType );
        }
        catch ( RepositoryException e )
        {
            throw new AppException( e.getMessage(  ), e );
        }
    }

    /**
     * Set the content of a node
     * @param node the node
     * @param inputStream a stream with the content
     * @param strMimeType the Mime type
     * @return true if the transfert is ok and node is a file, false otherwise
     */
    protected boolean setContent( Node node, InputStream inputStream, String strMimeType )
    {
        try
        {
            if ( isDirectory( node ) )
            {
                return false;
            }

            node.setProperty( getProperties( ).getProperty( NODE_TYPE_JCR_CONTENT ), inputStream );

            return true;
        }
        catch ( RepositoryException e )
        {
            throw new AppException( e.getMessage(  ), e );
        }
    }

    /**
     * Gets the name of a node
     * @param node the node
     * @return the name (property "cm:name") of the node
     */
    protected String getName( Node node )
    {
        String strName = null;

        try
        {
            strName = node.getProperty( getProperties( ).getProperty( PROPERTY_JCR_NAME ) ).getString(  );
        }
        catch ( RepositoryException e )
        {
            // nothing to do
            AppLogService.error( e.getLocalizedMessage(  ), e );
        }

        return strName;
    }

    protected String getParentUUID( Node node )
    {
        String result;

        try
        {
            result = getUUID( node.getParent(  ) );
        }
        catch ( RepositoryException e )
        {
            // do nothing: this node is not referenceable
            result = null;
        }

        return result;
    }

    /**
     * @param node
     * @return
     */
    protected String getUUID( Node node )
    {
        String result;

        try
        {
            result = node.getUUID(  );
        }
        catch ( RepositoryException e )
        {
            // do nothing: this node is not referenceable
            result = null;
        }

        return result;
    }

    @Override
    protected String getPath( Node node )
    {
        String strPath;
        String strName = null;

        try
        {
            strName = node.getName(  );
        }
        catch ( RepositoryException e1 )
        {
            AppLogService.error( node, e1 );
        }

        try
        {
            strPath = URLEncoder.encode( strName, "ISO-8859-1" ).replaceAll( "_x0020_", " " );
        }
        catch ( UnsupportedEncodingException e )
        {
            strPath = strName;
        }

        return strPath;
    }

    @Override
    protected Properties getProperties(  )
    {
        return _properties;
    }
}
