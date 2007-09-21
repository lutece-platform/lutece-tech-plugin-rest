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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.jcr.Node;

import org.springmodules.jcr.JcrTemplate;


/**
 * An IRepositoryFileDAO implementation for Alfresco JCR
 */
public class RemoteAlfrescoRepositoryFileDAO extends AlfrescoRepositoryFileDAO
{
    private static final String PROPERTY_JCR_CREATED = "JCR_CREATED";
    private static Properties _properties;

    /** This class implements the Singleton design pattern. */
    private static RemoteAlfrescoRepositoryFileDAO _dao;
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
//            _properties.setProperty( MIXIN_VERSIONNABLE, "cm:versionable" );
            _properties.setProperty( PROPERTY_JCR_CREATED, "cm:created" );
            _dao = new RemoteAlfrescoRepositoryFileDAO(  );
            _dao.init( jcrTemplate, repositoryInitializer, strDefaultWorkspaceName );
        }

        return _dao;
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
        	byte buf[] = new byte[1024];
            do
            {
            	skip = fileContent.read(buf);
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

    @Override
    protected Properties getProperties(  )
    {
        return _properties;
    }
}
