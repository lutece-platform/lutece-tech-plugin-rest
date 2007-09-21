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
package fr.paris.lutece.plugins.rss.business;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.portlet.DocumentListPortletHome;
import fr.paris.lutece.portal.business.portlet.PortletImpl;
import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * This class provides Data Access methods for RSS objects
 */
public final class RssGeneratedFileDAO implements IRssGeneratedFileDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = " SELECT max(id_rss) FROM rss_generation ";
    private static final String SQL_QUERY_INSERT = "  INSERT INTO rss_generation ( id_rss, id_portlet, name, state, date_update, description)" +
        " VALUES ( ?, ?, ?, ?, ?, ?)";
    private static final String SQL_QUERY_DELETE = " DELETE FROM rss_generation WHERE id_rss = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE rss_generation SET state = ?" + " WHERE id_rss = ? ";
    private static final String SQL_QUERY_RSS_FILE_LIST = "SELECT a.id_rss, a.id_portlet, a.name, " +
        "a.state, a.date_update, a.description, b.name FROM rss_generation as a, core_portlet as b " +
        "WHERE a.id_portlet=b.id_portlet ORDER BY a.name ASC ";
    private static final String SQL_QUERY_UPDATE_RSS_FILE = " UPDATE rss_generation SET id_portlet = ?, name = ?, state = ?, date_update = ?, description =? " +
        " WHERE id_rss = ?";
    private static final String SQL_QUERY_SELECT_GENERATE_FILE = " SELECT id_portlet, name, state, date_update,description" +
        " FROM rss_generation" + " WHERE id_rss = ?";
    private static final String SQL_QUERY_EXIST_RSS_FILE = " SELECT id_rss, name, state, date_update" +
        " FROM rss_generation" + " WHERE id_portlet = ?";
    private static final String SQL_QUERY_FILE_NAME_EXIST = "SELECT id_rss " + "FROM rss_generation " +
        "WHERE name = ?";
    private static final String SQL_QUERY_SELECT_RSS_PORTLET = " SELECT a.id_portlet, a.name, a.date_update " +
        " FROM portlet a LEFT JOIN rss_generation b ON a.id_portlet=b.id_portlet " +
        " WHERE b.id_portlet IS NULL AND a.id_portlet_type = ? ";
    private static final String SQL_QUERY_SELECT_PORTLET_NAME = " SELECT name " + " FROM portlet " +
        " WHERE id_portlet = ? ";
    private static final String SQL_QUERY_DOCUMENT_TYPE_PORTLET = " SELECT DISTINCT id_portlet , name FROM core_portlet WHERE id_portlet_type='DOCUMENT_PORTLET'  ";
    private static final String SQL_QUERY_SELECT_ALL_RSS = " SELECT portlet.id_portlet, portlet.name, portlet.date_update " +
        "FROM portlet WHERE  portlet.id_portlet_type = ? ";
    private static final String SQL_QUERY_CHECK_PORTLET_EXISTENCE = "SELECT id_portlet" + " FROM core_portlet" +
        " WHERE id_portlet = ?";
    private static final String SQL_QUERY_SELECT_DOCUMENT_BY_PORTLET = "SELECT a.id_document , a.code_document_type " +
        ", a.date_creation , a.date_modification,a.title,a.xml_validated_content FROM document as a  " +
        " INNER JOIN document_published AS b WHERE a.id_document=b.id_document AND b.id_portlet = ? ";
    private static final String SQL_QUERY_SELECT_XSL_FILE = " SELECT id_stylesheet , description , file_name, source " +
        " FROM stylesheet " + " WHERE id_stylesheet = ? ";

    /**
     * Calculates a new primary key to add a new record
     * @return The new key.
     * @param plugin The plugin
     */
    private int newPrimaryKey(  )
    {
        int nKey;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );

        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            // If the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
     * Inserts a new record in the table rss_generation.
     * @param pushRss The Instance of the object RssFile
     * @param plugin The plugin
     */
    public void insert( RssGeneratedFile rssFile )
    {
        int nNewPrimaryKey = newPrimaryKey(  );
        rssFile.setId( nNewPrimaryKey );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        daoUtil.setInt( 1, rssFile.getId(  ) );
        daoUtil.setInt( 2, rssFile.getPortletId(  ) );
        daoUtil.setString( 3, rssFile.getName(  ) );
        daoUtil.setInt( 4, rssFile.getState(  ) );
        daoUtil.setTimestamp( 5, new java.sql.Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setString( 6, rssFile.getDescription(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Deletes a record from the table
     * @param nRssFileId The identifier of the rssFile object
     */
    public void delete( int nRssFileId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nRssFileId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Updates the record in the table rss_generation
     * @param rssFile The Instance of the object rssFile
     */
    public void store( RssGeneratedFile rssFile )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_RSS_FILE );

        daoUtil.setInt( 1, rssFile.getPortletId(  ) );
        daoUtil.setString( 2, rssFile.getName(  ) );
        daoUtil.setInt( 3, rssFile.getState(  ) );
        daoUtil.setTimestamp( 4, new java.sql.Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setString( 5, rssFile.getDescription(  ) );
        daoUtil.setInt( 6, rssFile.getId(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Updates the rssFile's state
     * @param rssFile The Instance of the object rssFile
     * @param plugin The plugin
     */
    public void updateState( RssGeneratedFile rssFile )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setInt( 1, rssFile.getState(  ) );
        daoUtil.setInt( 2, rssFile.getId(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Loads the data of a rssFile file from the table
     * @param nRssFileId The identifier of the rssFile file
     * @param plugin The plugin
     */
    public RssGeneratedFile load( int nRssFileId )
    {
        RssGeneratedFile rssFile = null;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_GENERATE_FILE );
        daoUtil.setInt( 1, nRssFileId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            rssFile = new RssGeneratedFile(  );
            rssFile.setId( nRssFileId );
            rssFile.setPortletId( daoUtil.getInt( 1 ) );
            rssFile.setName( daoUtil.getString( 2 ) );
            rssFile.setState( daoUtil.getInt( 3 ) );
            rssFile.setUpdateDate( daoUtil.getTimestamp( 4 ) );
            rssFile.setDescription( daoUtil.getString( 5 ) );
        }

        daoUtil.free(  );

        return rssFile;
    }

    /**
     * Checks if a rssFile file exist for this portlet identifier
     * @param nPortletId The identifier of the portlet
     * @param plugin The plugin
     * @return true if a rssFile file exist for this portlet
     */
    public boolean checkExistPushrssByPortlet( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_EXIST_RSS_FILE );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return false;
        }

        daoUtil.free(  );

        return true;
    }

    /**
     * Tests if a push rss file exist with the same name
     * @param strRssFileName The push RSS file's name
     * @param plugin The plugin
     * @return true if the name already exist
     */
    public boolean checkRssFileFileName( String strRssFileName )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FILE_NAME_EXIST );
        daoUtil.setString( 1, strRssFileName );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return false;
        }

        daoUtil.free(  );

        return true;
    }

    /**
     * Returns the list of the rss_generation files
     * @param plugin The plugin
     * @return the List of rss files
     */
    public List<RssGeneratedFile> selectRssFileList(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_RSS_FILE_LIST );
        daoUtil.executeQuery(  );

        List<RssGeneratedFile> list = new ArrayList<RssGeneratedFile>(  );

        while ( daoUtil.next(  ) )
        {
            RssGeneratedFile rssFile = new RssGeneratedFile(  );
            rssFile.setId( daoUtil.getInt( 1 ) );
            rssFile.setPortletId( daoUtil.getInt( 2 ) );
            rssFile.setName( daoUtil.getString( 3 ) );
            rssFile.setState( daoUtil.getInt( 4 ) );
            rssFile.setUpdateDate( daoUtil.getTimestamp( 5 ) );
            rssFile.setDescription( daoUtil.getString( 6 ) );
            rssFile.setPortletName( daoUtil.getString( 7 ) );

            list.add( rssFile );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Returns the list of the portlets which are document portlets
     * @param plugin The plugin
     * @return the list in form of a Collection object
     */
    public ReferenceList selectDocumentTypePortlets(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DOCUMENT_TYPE_PORTLET );
        daoUtil.executeQuery(  );

        ReferenceList list = new ReferenceList(  );

        while ( daoUtil.next(  ) )
        {
            list.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Returns a collection of portlets for which there isn't any RSS files
     * @return the portlets in form of Collection
     */
    public Collection selectRssPortlets(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_RSS_PORTLET );
        daoUtil.executeQuery(  );

        ArrayList list = new ArrayList(  );

        while ( daoUtil.next(  ) )
        {
            PortletImpl portlet = new PortletImpl(  );
            portlet.setId( daoUtil.getInt( 1 ) );
            portlet.setName( daoUtil.getString( 2 ) );
            portlet.setDateUpdate( daoUtil.getTimestamp( 3 ) );
            list.add( portlet );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Returns a collection of all portlets
     * @return the portlets in form of Collection
     */
    public Collection selectAllRssPortlets(  )
    {
        String strPortletTypeId = DocumentListPortletHome.getInstance(  ).getPortletTypeId(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_RSS );

        daoUtil.setString( 1, strPortletTypeId );

        daoUtil.executeQuery(  );

        ArrayList list = new ArrayList(  );

        while ( daoUtil.next(  ) )
        {
            PortletImpl portlet = new PortletImpl(  );
            portlet.setId( daoUtil.getInt( 1 ) );
            portlet.setName( daoUtil.getString( 2 ) );
            portlet.setDateUpdate( daoUtil.getTimestamp( 3 ) );
            list.add( portlet );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Reads the portlet's name
     * @param nPortletId the identifier of the portlet
     * @return The name of the portlet
     */
    public String selectRssFilePortletName( int nPortletId )
    {
        String strPortletName = "";
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_NAME );
        daoUtil.setInt( 1, nPortletId );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            strPortletName = daoUtil.getString( 1 );
        }

        daoUtil.free(  );

        return strPortletName;
    }

    /**
     * Tests if the portlet has not been deleted before update
     * @param nPortletId The portlet identifier for this RSS file
     * @param plugin The plugin
     * @return true il the portlet exist
     */
    public boolean checkRssFilePortlet( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_PORTLET_EXISTENCE );
        daoUtil.setInt( 1, nPortletId );

        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return false;
        }

        daoUtil.free(  );

        return true;
    }

    /**
     * Returns all the documents of a portlet whose identifier is specified in parameter
     * @param nPortletId the identifier of the portlet
     * @param pluginName the plugin name
     * @return List of documents
     */
    public List selectDocumentsByPortlet( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_DOCUMENT_BY_PORTLET );
        daoUtil.setInt( 1, nPortletId );

        daoUtil.executeQuery(  );

        List list = new ArrayList(  );

        while ( daoUtil.next(  ) )
        {
            Document document = new Document(  );
            document.setId( daoUtil.getInt( 1 ) );
            document.setCodeDocumentType( daoUtil.getString( 2 ) );
            document.setDateCreation( daoUtil.getTimestamp( 3 ) );
            document.setDateModification( daoUtil.getTimestamp( 4 ) );
            document.setTitle( daoUtil.getString( 5 ) );
            document.setXmlValidatedContent( daoUtil.getString( 6 ) );
            list.add( document );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Returns the stylesheet for RSS files
     * @param nStyleSheetId the identifier of the Stylesheet
     * @return the stylesheet
     */
    public StyleSheet selectXslFile( int nStyleSheetId )
    {
        StyleSheet stylesheet = new StyleSheet(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_XSL_FILE );
        daoUtil.setInt( 1, nStyleSheetId );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            stylesheet.setId( daoUtil.getInt( 1 ) );
            stylesheet.setDescription( daoUtil.getString( 2 ) );
            stylesheet.setFile( daoUtil.getString( 3 ) );
            stylesheet.setSource( daoUtil.getBytes( 4 ) );
        }

        daoUtil.free(  );

        return stylesheet;
    }
}
