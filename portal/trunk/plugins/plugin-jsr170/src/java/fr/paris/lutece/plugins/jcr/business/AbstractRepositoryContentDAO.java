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

import org.springmodules.jcr.JcrCallback;

import sun.net.www.MimeTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;


public abstract class AbstractRepositoryContentDAO extends AbstractRepositoryDAO implements IRepositoryFileDAO
{
    protected static final String NODE_TYPE_FOLDER = "NODE_TYPE_FOLDER";
    protected static final String NODE_TYPE_FILE = "NODE_TYPE_FILE";
    protected static final String NODE_TYPE_FILE_RESOURCE = "NODE_TYPE_FILE_RESOURCE";
    protected static final String ATTRIBUTE_NODE_SIZE = "ATTRIBUTE_NODE_SIZE";
    protected static final String NODE_TYPE_JCR_CONTENT = "NODE_TYPE_JCR_CONTENT";
    protected static final String REGEXP_ABSOLUTE_PATH = "REGEXP_ABSOLUTE_PATH";
    protected static final String MIXIN_REFERENCEABLE = "MIXIN_REFERENCEABLE";
    protected static final String MIXIN_VERSIONNABLE = "MIXIN_VERSIONNABLE";
    protected static final String ROOT_NODE_PATH = "ROOT_NODE_PATH";
    protected static final String DEFAULT_MIME_TYPE = "DEFAULT_MIME_TYPE";
    protected static final String PROPERTY_JCR_MIMETYPE = "PROPERTY_JCR_MIMETYPE";
    protected static final String PROPERTY_JCR_DATA = "PROPERTY_JCR_DATA";
    protected static final String PROPERTY_JCR_LASTMODIFIED = "PROPERTY_JCR_LASTMODIFIED";
    protected static final String PROPERTY_JCR_NAME = "PROPERTY_JCR_NAME";

    /**
     * @param strWorkspace the workspace name
     * @param strPath the absolute path
     * @see fr.paris.lutece.plugins.jcr.business.IRepositoryFileDAO#create(java.lang.String, java.lang.String)
     */
    public void create( String strWorkspace, final String strPath )
    {
        execute( strWorkspace,
            new JcrCallback(  )
            {
                public Object doInJcr( Session session )
                    throws IOException, RepositoryException
                {
                    String strRelPath = strPath.replaceFirst( getProperty( REGEXP_ABSOLUTE_PATH ), "" );
                    Node addedNode = getRootNode( session ).addNode( strRelPath, getProperty( NODE_TYPE_FOLDER ) );

                    if ( getProperty( MIXIN_REFERENCEABLE ) != null )
                    {
                        addedNode.addMixin( getProperty( MIXIN_REFERENCEABLE ) );
                    }

                    setName( addedNode, strRelPath );
                    session.save(  );

                    return null;
                }
            } );
    }

    /**
     * @param strWorkspace the workspace name
     * @param strFileName the absolute filename to create
     * @param file the file content
     * @see fr.paris.lutece.plugins.jcr.business.IRepositoryFileDAO#create(java.lang.String, java.lang.String, java.io.File)
     */
    public void create( String strWorkspace, final String strFileName, final File file )
    {
        execute( strWorkspace,
            new JcrCallback(  )
            {
                public Object doInJcr( Session session )
                    throws IOException, RepositoryException
                {
                    String strFilePath = strFileName.replaceAll( getProperty( REGEXP_ABSOLUTE_PATH ), "" );
                    Node addedNode = getRootNode( session ).addNode( strFilePath, getProperty( NODE_TYPE_FILE ) );

                    if ( getProperty( ATTRIBUTE_NODE_SIZE ) != null )
                    {
                        addedNode.setProperty( getProperty( ATTRIBUTE_NODE_SIZE ), file.length(  ) );
                    }

                    if ( getProperty( MIXIN_REFERENCEABLE ) != null )
                    {
                        addedNode.addMixin( getProperty( MIXIN_REFERENCEABLE ) );
                    }

                    setName( addedNode, strFilePath );

                    setContent( addedNode, new FileInputStream( file ), strFilePath );
                    session.save(  );

                    return null;
                }
            } );
    }

    /**
     * @param strWorkspace the workspace name
     * @param strPath the absolute path to delete
     * @see fr.paris.lutece.plugins.jcr.business.IRepositoryFileDAO#delete(java.lang.String, java.lang.String)
     */
    public void delete( String strWorkspace, final String strPath )
    {
        execute( strWorkspace,
            new JcrCallback(  )
            {
                public Object doInJcr( Session session )
                    throws IOException, RepositoryException
                {
                    Node currentNode = getNode( session, strPath );
                    currentNode.remove(  );
                    session.save(  );

                    return null;
                }
            } );
    }

    /**
     * @param strWorkspace the workspace name
     * @param strPath the absolute path
     * @return a collection of IRepositoryFile contained in strPath
     * @see fr.paris.lutece.plugins.jcr.business.IRepositoryFileDAO#listFiles(java.lang.String, java.lang.String)
     */
    @SuppressWarnings( "unchecked" )
    public List<IRepositoryFile> listFiles( String strWorkspace, final String strPath )
    {
        return (List<IRepositoryFile>) execute( strWorkspace,
            new JcrCallback(  )
            {
                public Object doInJcr( Session session )
                    throws IOException, RepositoryException
                {
                    Node currentNode = getNode( session, strPath );

                    if ( currentNode == null )
                    {
                        return null;
                    }

                    ArrayList<IRepositoryFile> listFiles = new ArrayList<IRepositoryFile>(  );

                    NodeIterator it = currentNode.getNodes(  );

                    while ( it.hasNext(  ) )
                    {
                        Node node = it.nextNode(  );

                        // only select files and directory
                        if ( isDirectory( node ) || isFile( node ) )
                        {
                            listFiles.add( nodeToRepositoryFile( node ) );
                        }
                    }

                    return listFiles;
                }
            } );
    }

    /**
     * @param strWorkspace the workspace name
     * @param strId the id to find
     * @return an IRepositoryFile
     * @see fr.paris.lutece.plugins.jcr.business.IRepositoryFileDAO#findById(java.lang.String, java.lang.String)
     */
    public IRepositoryFile findById( String strWorkspace, final String strId )
    {
        return (IRepositoryFile) execute( strWorkspace,
            new JcrCallback(  )
            {
                public Object doInJcr( Session session )
                    throws IOException, RepositoryException
                {
                    if ( strId == null )
                    {
                        throw new NullPointerException(  );
                    }

                    Node currentNode = getNodeById( session, strId );

                    if ( currentNode == null )
                    {
                        return null;
                    }
                    else
                    {
                        return nodeToRepositoryFile( currentNode );
                    }
                }
            } );
    }

    /**
     * @param strWorkspace the workspace name
     * @param strId the id to find
     * @param strVersion the version name to retrieve
     * @return an IRepositoryFile
     * @see fr.paris.lutece.plugins.jcr.business.IRepositoryFileDAO#findById(java.lang.String, java.lang.String)
     */
    public IRepositoryFile findById( String strWorkspace, final String strId, final String strVersion )
    {
        return (IRepositoryFile) execute( strWorkspace,
            new JcrCallback(  )
            {
                public Object doInJcr( Session session )
                    throws IOException, RepositoryException
                {
                    if ( strId == null )
                    {
                        throw new NullPointerException(  );
                    }

                    Node currentNode = getNodeById( session, strId );

                    if ( currentNode == null )
                    {
                        return null;
                    }
                    else
                    {
                        Version version = currentNode.getVersionHistory(  ).getVersion( strVersion );

                        return nodeToRepositoryFile( version );
                    }
                }
            } );
    }

    /**
     * Set the content of a node
     * @param node the node
     * @param inputStream a stream with the content
     * @param strMimeType the Mime type
     * @return true if the transfert is ok and node is a file, false otherwise
     * @throws FileNotFoundException
     */
    protected boolean setContent( Node node, InputStream inputStream, String strFileName )
    {
        try
        {
            if ( isDirectory( node ) )
            {
                return false;
            }

            MimeTable mt = MimeTable.getDefaultTable(  );
            String strMimeType = mt.getContentTypeFor( strFileName );

            if ( strMimeType == null )
            {
                strMimeType = getProperty( DEFAULT_MIME_TYPE );
            }

            Node resNode = null;

            if ( !node.hasNode( getProperty( NODE_TYPE_JCR_CONTENT ) ) )
            {
                resNode = node.addNode( getProperty( NODE_TYPE_JCR_CONTENT ), getProperty( NODE_TYPE_FILE_RESOURCE ) );
            }
            else
            {
                resNode = node.getNode( getProperty( NODE_TYPE_JCR_CONTENT ) );
            }

            resNode.setProperty( getProperty( PROPERTY_JCR_MIMETYPE ), strMimeType );
            resNode.setProperty( getProperty( PROPERTY_JCR_DATA ), inputStream );
            resNode.setProperty( getProperty( PROPERTY_JCR_LASTMODIFIED ), Calendar.getInstance(  ) );

            return true;
        }
        catch ( RepositoryException e )
        {
            throw new AppException( e.getMessage(  ), e );
        }
    }

    /**
     * @param strWorkspace the workspace name
     * @param strPath the absolute path
     * @return an IRepositoryFile
     * @see fr.paris.lutece.plugins.jcr.business.IRepositoryFileDAO#findByPath(java.lang.String, java.lang.String)
     */
    public IRepositoryFile findByPath( String strWorkspace, final String strPath )
    {
        return (IRepositoryFile) execute( strWorkspace,
            new JcrCallback(  )
            {
                public Object doInJcr( Session session )
                    throws IOException, RepositoryException
                {
                    AppLogService.debug( strPath );

                    Node currentNode = getNode( session, strPath );

                    return nodeToRepositoryFile( currentNode );
                }
            } );
    }

    /**
     * Get a node by its path
     * @param session the session
     * @param strPath the absolute path
     * @return a node
     */
    private Node getNode( Session session, String strPath )
    {
        String strRelPath = ( strPath == null ) ? "" : strPath.replaceFirst( getProperty( REGEXP_ABSOLUTE_PATH ), "" );

        try
        {
            if ( "".equals( strRelPath ) )
            {
                // we're looking for the root node
                return getRootNode( session );
            }
            else
            {
                return getRootNode( session ).getNode( strRelPath );
            }
        }
        catch ( PathNotFoundException e )
        {
            throw new AppException( e.getMessage(  ), e );
        }
        catch ( RepositoryException e )
        {
            throw new AppException( e.getMessage(  ), e );
        }
    }

    /**
     * Return the root node of this repository depending on ROOT_NODE_PATH
     * @param session the session
     * @return the root node
     * @throws PathNotFoundException occurs when default root node doesn't exist
     * @throws RepositoryException occurs on other error
     */
    private Node getRootNode( Session session ) throws PathNotFoundException, RepositoryException
    {
        return ( getProperty( ROOT_NODE_PATH ) == null ) ? session.getRootNode(  )
                                                         : session.getRootNode(  ).getNode( getProperty( ROOT_NODE_PATH ) );
    }

    /**
     * @param session the session to use
     * @param strId the id to find
     * @return a node or null if node is not found
     * @throws RepositoryException
     */
    private Node getNodeById( Session session, String strId )
        throws RepositoryException
    {
        Node result = null;

        try
        {
            result = session.getNodeByUUID( strId );
        }
        catch ( ItemNotFoundException e )
        {
            e.printStackTrace(  );
            AppLogService.debug( e );
            result = null;
        }

        return result;
    }

    /**
     * Instanciate a IRepositoryFile from node informations
     * @param node the node
     * @return an IRepositoryFile associated with this node
     */
    private IRepositoryFile nodeToRepositoryFile( Node node )
    {
        IRepositoryFile addedFile;

        if ( isVersionnable( node ) )
        {
            VersionnableRepositoryFileImpl version = new VersionnableRepositoryFileImpl(  );
            nodeToRepositoryFile( node, version );
            version.setVersion( getVersion( node ) );
            addedFile = version;
        }
        else
        {
            addedFile = nodeToRepositoryFile( node, new JcrRepositoryFileImpl(  ) );
        }

        return addedFile;
    }

    /**
     * Instanciate a IRepositoryFile from node informations
     * @param node the node
     * @return an IRepositoryFile associated with this node
     */
    private IRepositoryFile nodeToRepositoryFile( Node node, AbstractRepositoryFile addedFile )
    {
        AppLogService.debug( node );
        addedFile.setAbsolutePath( getAbsolutePath( node ) );
        addedFile.setExists( exists( node ) );
        addedFile.setDirectory( isDirectory( node ) );
        addedFile.setFile( isFile( node ) );
        addedFile.setName( getName( node ) );

        addedFile.setParentId( getParentUUID( node ) );
        addedFile.setResourceId( getUUID( node ) );
        addedFile.setPath( getPath( node ) );

        // FIXME change default root directory
        // FIXME alfresco repository use iso8859 encoding for filenames, find a better way
        //            addedFile.setPath( URLEncoder.encode( node.getName(  ), "ISO-8859-1" ).replaceAll( "_x0020_", " " ) );
        if ( addedFile.isFile(  ) )
        {
            addedFile.setMimeType( getMimeType( node ) );
            addedFile.setContent( getFileContent( node ) );
            addedFile.setLastModified( getLastModified( node ) );
            addedFile.setLength( getSize( node ) );
        }

        if ( addedFile.isDirectory(  ) )
        {
            addedFile.setLastModified( getCreated( node ) );
        }

        AppLogService.debug( addedFile );

        return addedFile;
    }

    private IRepositoryFile nodeToRepositoryFile( Version version )
    {
        VersionFileImpl file = new VersionFileImpl(  );

        try
        {
            // we get the first child of this version
            Node childNode = version.getNodes(  ).nextNode(  );
            nodeToRepositoryFile( childNode, file );
            file.setName( version.getName(  ) );

            //            file.setContent( getFileContent( childNode ) );
            //            file.setLastModified( getLastModified( childNode ) );
            //            file.setResourceId( getUUID( childNode ) );
        }
        catch ( RepositoryException e )
        {
            AppLogService.error( version, e );
        }

        return file;
    }

    public void store( String strWorkspace, final IRepositoryFile file )
    {
        execute( strWorkspace,
            new JcrCallback(  )
            {
                public Object doInJcr( Session session )
                    throws IOException, RepositoryException
                {
                    Node currentNode = getNodeById( session, file.getResourceId(  ) );
                    boolean bIsVersionable = isVersionnable( currentNode );

                    if ( bIsVersionable )
                    {
                        currentNode.checkout(  );
                    }

                    setContent( currentNode, file.getContent(  ), file.getName(  ) );
                    session.save(  );

                    if ( bIsVersionable )
                    {
                        Version v = currentNode.checkin(  );
                        AppLogService.debug( v );
                    }

                    return null;
                }
            } );
    }

    public void setVersionnable( String strWorkspace, final String strNodeId, final boolean isVersionnable )
    {
        execute( strWorkspace,
            new JcrCallback(  )
            {
                public Object doInJcr( Session session )
                    throws IOException, RepositoryException
                {
                	String strVersionable = getProperty( MIXIN_VERSIONNABLE );
                	if(strVersionable == null)
                	{
                		return null;
                	}
                    Node currentNode = getNodeById( session, strNodeId );
                    boolean isCurrentNodeVersionnable = isVersionnable( currentNode );

                    if ( !isVersionnable && isCurrentNodeVersionnable )
                    {
                        currentNode.removeMixin( strVersionable );
                        session.save(  );
                    }
                    else if ( isVersionnable && !isCurrentNodeVersionnable )
                    {
                        currentNode.addMixin( strVersionable );
                        session.save(  );
                        currentNode.checkin(  );
                    }

                    return null;
                }
            } );
    }

    @SuppressWarnings( "unchecked" )
    public List<IRepositoryFile> getHistory( String strWorkspace, final String strNodeId )
    {
        return (List<IRepositoryFile>) execute( strWorkspace,
            new JcrCallback(  )
            {
                public Object doInJcr( Session session )
                    throws IOException, RepositoryException
                {
                    Node currentNode = getNodeById( session, strNodeId );
                    boolean isCurrentNodeVersionnable = isVersionnable( currentNode );

                    if ( !isCurrentNodeVersionnable )
                    {
                        return null;
                    }

                    ArrayList<IRepositoryFile> resultList = new ArrayList<IRepositoryFile>(  );
                    VersionHistory history = currentNode.getVersionHistory(  );

                    for ( VersionIterator it = history.getAllVersions(  ); it.hasNext(  ); )
                    {
                        Version version = (Version) it.next(  );

                        if ( !"jcr:rootVersion".equals( version.getName(  ) ) )
                        {
                            resultList.add( nodeToRepositoryFile( version ) );
                        }
                    }

                    return resultList;
                }
            } );
    }

    private boolean isVersionnable( Node node )
    {
        String strMixinVersionnable = getProperty( MIXIN_VERSIONNABLE );

        if ( strMixinVersionnable == null )
        {
            return false;
        }

        try
        {
            for ( NodeType mixinNode : node.getMixinNodeTypes(  ) )
            {
                if ( ( mixinNode != null ) && strMixinVersionnable.equals( mixinNode.getName(  ) ) )
                {
                    return true;
                }
            }
        }
        catch ( RepositoryException e )
        {
            // nothing to do, we just log the exception
            AppLogService.error( node, e );
        }

        return false;
    }

    private String getVersion( Node node )
    {
        String currentVersion = null;

        try
        {
            Version baseVersion = node.getBaseVersion(  );
            currentVersion = ( baseVersion == null ) ? null : baseVersion.getName(  );
        }
        catch ( RepositoryException e )
        {
            // log the exception
            AppLogService.error( node, e );
        }

        return currentVersion;
    }

    private void setName( Node node, String strPath ) throws RepositoryException
    {
        if ( getProperty( PROPERTY_JCR_NAME ) != null )
        {
            node.setProperty( getProperty( PROPERTY_JCR_NAME ), new File( strPath ).getName(  ) );
        }
    }

    private String getProperty( String strPropertyName )
    {
        return getProperties(  ).getProperty( strPropertyName );
    }

    protected abstract Calendar getCreated( Node node );

    protected abstract long getSize( Node node );

    protected abstract Calendar getLastModified( Node node );

    protected abstract InputStream getFileContent( Node node );

    protected abstract String getMimeType( Node node );

    protected abstract String getPath( Node node );

    protected abstract String getUUID( Node node );

    protected abstract String getParentUUID( Node node );

    protected abstract String getName( Node node );

    protected abstract boolean isFile( Node node );

    protected abstract boolean isDirectory( Node node );

    protected abstract boolean exists( Node node );

    protected abstract String getAbsolutePath( Node node );

    protected abstract Properties getProperties(  );
}
