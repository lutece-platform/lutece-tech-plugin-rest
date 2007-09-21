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
package fr.paris.lutece.plugins.folderlisting.business.portlet;

import fr.paris.lutece.plugins.folderlisting.business.FolderListingDirectory;
import fr.paris.lutece.plugins.folderlisting.business.FolderListingFile;
import fr.paris.lutece.plugins.folderlisting.business.FolderListingHome;
import fr.paris.lutece.plugins.folderlisting.service.Folder;
import fr.paris.lutece.plugins.folderlisting.service.FolderService;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.filesystem.DirectoryNotFoundException;
import fr.paris.lutece.util.xml.XmlUtil;

import java.io.File;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents business objects FolderListingPortlet
 */
public class FolderListingPortlet extends Portlet
{
    ///////////////////////////////////////////////////////////////////////
    // Constants

    // Xml tags
    private static final String TAG_ALIAS_ROOT_DIRECTORY = "directory-root-alias";
    private static final String TAG_PORTLET_FOLDERLISTING = "folderlisting-portlet";
    private static final String TAG_DIRECTORY_PATH = "directory-path";

    // Variables declarations
    private int _nFolderListingFileId;
    private String _strFolderPath;
    private String _strRootFolderId;

    /**
     * Creates a new FolderListingPortlet object.
     */
    public FolderListingPortlet(  )
    {
        setPortletTypeId( FolderListingPortletHome.getInstance(  ).getPortletTypeId(  ) );
    }

    /**
     * Sets the Id of the folderlistingfile
     *
     * @param nFolderListingFileId the new Id
     */
    public void setFolderListingFileId( int nFolderListingFileId )
    {
        _nFolderListingFileId = nFolderListingFileId;
    }

    /**
     * Returns the Id of this folderlistingfile
     *
     * @return the folderlistingfile Id
     */
    public int getFolderListingFileId(  )
    {
        return _nFolderListingFileId;
    }

    /**
     * Returns the FolderPath
     *
     * @return The FolderPath
     */
    public String getFolderPath(  )
    {
        return _strFolderPath;
    }

    /**
     * Sets the RootFolderId
     *
     * @param strRootFolderId The RootFolderId
     */
    public void setRootFolderId( String strRootFolderId )
    {
        _strRootFolderId = strRootFolderId;
    }

    /**
     * Returns the RootFolderId
     *
     * @return The RootFolderId
     */
    public String getRootFolderId(  )
    {
        return _strRootFolderId;
    }

    /**
     * Sets the FolderPath
     *
     * @param strFolderPath The FolderPath
     */
    public void setFolderPath( String strFolderPath )
    {
        _strFolderPath = strFolderPath;
    }

    /**
     * Get the path corresponding to Root Folder Id
     * @return The path of the root folder
     */
    public String getRootFolderPath(  )
    {
        String strFolderPath = "";

        Folder folder = FolderService.getInstance(  ).getFolder( getRootFolderId(  ) );

        if ( folder != null )
        {
            strFolderPath = folder.getPath(  );
        }
        else
        {
            AppLogService.error( "Folderlisting : Invalid folder Id :" + getRootFolderId(  ) );
        }

        return strFolderPath;
    }

    /**
     * Get the path corresponding to Root Folder Id
     * @return The path of the root folder
     */
    public String getRootFolderName(  )
    {
        String strRootFolderName = "";

        Folder folder = FolderService.getInstance(  ).getFolder( getRootFolderId(  ) );

        if ( folder != null )
        {
            strRootFolderName = folder.getName(  );
        }
        else
        {
            AppLogService.error( "Folderlisting : Invalid folder Id :" + getRootFolderId(  ) );
        }

        return strRootFolderName;
    }

    /**
     * Returns the Xml code of the FolderListing portlet without XML heading
     *
     * @param request The http request
     * @return the Xml code of the FolderListing portlet content
     */
    public String getXml( HttpServletRequest request )
    {
        // retrieve the folder path from the request
        setFolderPathFromRequest( request );

        StringBuffer sbXml = new StringBuffer(  );

        XmlUtil.beginElement( sbXml, TAG_PORTLET_FOLDERLISTING );

        XmlUtil.addElement( sbXml, TAG_ALIAS_ROOT_DIRECTORY, getRootFolderName(  ) );

        XmlUtil.addElement( sbXml, TAG_DIRECTORY_PATH, getFolderPath(  ) );

        try
        {
            for ( FolderListingDirectory folderlistingDirectory : FolderListingHome.getSubDirectories( getRootFolderPath(  ) +
                    getFolderPath(  ) ) )
            {
                sbXml.append( folderlistingDirectory.getXml( request ) );
            }

            for ( FolderListingFile folderlistingFile : FolderListingHome.getFiles( getRootFolderPath(  ) +
                    getFolderPath(  ) ) )
            {
                sbXml.append( folderlistingFile.getXml( request ) );
            }

            XmlUtil.endElement( sbXml, TAG_PORTLET_FOLDERLISTING );
        }
        catch ( DirectoryNotFoundException e )
        {
            AppLogService.error( e.getMessage(  ), e );
            // Clear the buffer
            sbXml.delete( 0, sbXml.length(  ) );
        }

        return addPortletTags( sbXml );
    }

    /**
     * Set the current folder path from the string passed in request.
     * This string is the path from the webapp folder.
     * The retrieved path should :
     * <ul>
     * <li>match an existing directory on the filesystem</li>
     * <li>start with the root path defined for the portlet : we only allow to browse folders above the one defined as root for the portlet.</li>
     * </ul>
     * If not, it is replaced by the root path.
     *
     * @param request the http request
     */
    private void setFolderPathFromRequest( HttpServletRequest request )
    {
        String strFolderPath = null;

        if ( request != null )
        {
            strFolderPath = request.getParameter( "folder_" + getId(  ) );
        }

        if ( ( strFolderPath == null ) || ( strFolderPath.trim(  ).equals( "" ) ) )
        {
            setFolderPath( "" );
        }
        else
        {
            //  check that the directory exists. if not, replace by root dir.
            File fDirectory = new File( AppPathService.getAbsolutePathFromRelativePath( getRootFolderPath(  ) +
                        strFolderPath ) );

            if ( fDirectory.exists(  ) )
            {
                setFolderPath( strFolderPath );
            }
            else
            {
                setFolderPath( "" );
            }
        }
    }

    /**
     * Returns the Xml code of the FolderListing portlet with XML heading
     *
     * @param request the HttpServletRequest
     * @return the Xml code of the FolderListing portlet
     */
    public String getXmlDocument( HttpServletRequest request )
    {
        return XmlUtil.getXmlHeader(  ) + getXml( request );
    }

    /**
     * Update the portlet
     */
    public void update(  )
    {
        FolderListingPortletHome.getInstance(  ).update( this );
    }

    /**
     * Remove portlet
     */
    public void remove(  )
    {
        FolderListingPortletHome.getInstance(  ).remove( this );
    }
}
