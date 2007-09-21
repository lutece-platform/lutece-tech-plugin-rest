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
package fr.paris.lutece.plugins.links.business;

import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for Link objects
 */
public final class LinkDAO implements ILinkDAO
{
    /** This class implements the Singleton design pattern. */
    private static LinkDAO _dao = new LinkDAO(  );
    private static final String INSERT_URLS_SQL = "INSERT INTO link_virtual_host (id_link, virtual_host_key, url) " +
        " VALUES ( ?, ?, ?)";
    private static final String DELETE_URLS_SQL = "DELETE FROM link_virtual_host WHERE id_link = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO link ( id_link, name, description, date, url, image_content, workgroup_key, mime_type ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM link WHERE id_link = ?";
    private static final String SQL_QUERY_SELECT = "SELECT name,  description, date, url, id_link, image_content, workgroup_key, mime_type FROM link WHERE id_link = ?";
    private static final String SQL_QUERY_SELECT_URLS_LIST = "SELECT virtual_host_key, url FROM link_virtual_host WHERE id_link = ?";
    private static final String SQL_QUERY_UPDATE = "UPDATE link SET name = ?,  description = ?, date = ?, url=?, image_content=?, workgroup_key=?, mime_type=? WHERE id_link = ?";
    private static final String SQL_QUERY_NEW_PRIMARY_KEY = "SELECT max(id_link) FROM link";
    private static final String SQL_QUERY_SELECT_ALL = "SELECT id_link , name ,  description, date, url, image_content, workgroup_key, mime_type" +
        " FROM link ORDER BY name";
    private static final String SQL_QUERY_SELECT_BY_PORTLET = " SELECT a.id_link , a.name, a.url, a.description, a.image_content, a.workgroup_key, a.mime_type" +
        " FROM link a , link_list_portlet b" + " WHERE a.id_link = b.id_link " + " AND b.id_portlet = ? " +
        " ORDER BY b.link_order  ";
    private static final String SQL_QUERY_SELECT_RESOURCE_IMAGE = " SELECT image_content , mime_type FROM link " +
        " WHERE id_link = ? ";

    /**
     * Creates a new LinkDAO object.
     */
    private LinkDAO(  )
    {
    }

    /**
     * Returns the unique instance of the singleton.
     *
     * @return the instance
     */
    static LinkDAO getInstance(  )
    {
        return _dao;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Insert a new record in the table.
     *
     * @param link The instance of link object
     */
    public void insert( Link link )
    {
        int nNewPrimaryKey = newPrimaryKey(  );
        link.setId( nNewPrimaryKey );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        daoUtil.setInt( 1, link.getId(  ) );
        daoUtil.setString( 2, link.getName(  ) );
        daoUtil.setString( 3, link.getDescription(  ) );
        daoUtil.setDate( 4, link.getDate(  ) );
        daoUtil.setString( 5, link.getUrl(  ) );
        daoUtil.setString( 7, link.getWorkgroupKey(  ) );
        
        if ( ( link.getImageContent(  ) == null ) )
        {
            daoUtil.setBytes( 6, null );
            daoUtil.setString( 8, "" );
        }
        else
        {
            daoUtil.setBytes( 6, link.getImageContent(  ) );
            daoUtil.setString( 8, link.getMimeType(  ) );
        }
        
        
       
        
        daoUtil.executeUpdate(  );

        insertUrlsList( link );
        daoUtil.free(  );
    }

    /**
     * Delete a record from the table
     *
     * @param nLinkId The indentifier of the link object
     */
    public void delete( int nLinkId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nLinkId );

        daoUtil.executeUpdate(  );

        daoUtil.free(  );
        daoUtil = new DAOUtil( DELETE_URLS_SQL );
        daoUtil.setInt( 1, nLinkId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of link from the table
     *
     * @param nLinkId The indentifier of the link object
     * @return An instance of link object
     */
    public Link load( int nLinkId )
    {
        Link link = new Link(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );

        daoUtil.setInt( 1, nLinkId );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            link.setId( nLinkId );
            link.setName( daoUtil.getString( 1 ) );
            link.setDescription( daoUtil.getString( 2 ) );
            link.setDate( daoUtil.getDate( 3 ) );
            link.setUrl( daoUtil.getString( 4 ) );
            link.setId( daoUtil.getInt( 5 ) );
            link.setImageContent( daoUtil.getBytes( 6 ) );
            link.setWorkgroupKey( daoUtil.getString( 7 ) );
            link.setMimeType( daoUtil.getString( 8 ) );
            link.setOptionalUrls( this.selectUrlsList( nLinkId ) );
        }

        daoUtil.free(  );

        return link;
    }

    /**
     * Update the record in the table
     *
     * @param link The instance of link object
     */
    public void store( Link link )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setString( 1, link.getName(  ) );
        daoUtil.setString( 2, link.getDescription(  ) );
        daoUtil.setDate( 3, link.getDate(  ) );
        daoUtil.setString( 4, link.getUrl(  ) );
        daoUtil.setBytes( 5, link.getImageContent(  ) );
        daoUtil.setString( 6, link.getWorkgroupKey(  ) );
        daoUtil.setString( 7, link.getMimeType(  ) );
        
        daoUtil.setInt( 8, link.getId(  ) );

        daoUtil.executeUpdate(  );

        daoUtil.free(  );

        daoUtil = new DAOUtil( DELETE_URLS_SQL );
        daoUtil.setInt( 1, link.getId(  ) );
        daoUtil.executeUpdate(  );

        insertUrlsList( link );

        daoUtil.free(  );
    }

    /**
     * Calculate a new primary key to add a new record
     *
     * @return The new key.
     */
    public int newPrimaryKey(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PRIMARY_KEY );
        int nKey;

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
     * Returns a list of all the links
     *
     * @return A collection of links objects
     */
    public Collection<Link> selectList(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL );

        daoUtil.executeQuery(  );

        ArrayList<Link> list = new ArrayList<Link>(  );

        while ( daoUtil.next(  ) )
        {
            Link link = new Link(  );
            link.setId( daoUtil.getInt( 1 ) );
            link.setName( daoUtil.getString( 2 ) );
            link.setDescription( daoUtil.getString( 3 ) );
            link.setDate( daoUtil.getDate( 4 ) );
            link.setUrl( daoUtil.getString( 5 ) );
            link.setImageContent( daoUtil.getBytes( 6 ) );
            link.setWorkgroupKey( daoUtil.getString( 7 ) );
            link.setMimeType( daoUtil.getString( 8 ) );
            link.setOptionalUrls( this.selectUrlsList( daoUtil.getInt( 1 ) ) );

            list.add( link );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * load all the links registered in a specified portlet
     *
     * @param nIdPortlet The identifier of the portlet
     * @return A collection of Links objects
     */
    public Collection<Link> selectByPortlet( int nIdPortlet )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PORTLET );
        daoUtil.setInt( 1, nIdPortlet );

        daoUtil.executeQuery(  );

        ArrayList<Link> list = new ArrayList<Link>(  );

        while ( daoUtil.next(  ) )
        {
            Link link = new Link(  );
            link.setId( daoUtil.getInt( 1 ) );
            link.setName( daoUtil.getString( 2 ) );
            link.setUrl( daoUtil.getString( 3 ) );
            link.setDescription( daoUtil.getString( 4 ) );
            link.setImageContent( daoUtil.getBytes( 5 ) );
            link.setWorkgroupKey( daoUtil.getString( 6 ) );
            link.setMimeType( daoUtil.getString( 7 ) );
            link.setOptionalUrls( this.selectUrlsList( daoUtil.getInt( 1 ) ) );
            list.add( link );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * load all the optional urls
     *
     * @param idLink the link's id
     * @return the optional urls ReferenceList
     */
    private ReferenceList selectUrlsList( int idLink )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_URLS_LIST );

        // get optional links
        daoUtil.setInt( 1, idLink );

        daoUtil.executeQuery(  );

        ReferenceList list = new ReferenceList(  );

        while ( daoUtil.next(  ) )
        {
            list.addItem( daoUtil.getString( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * insert all the optional urls
     *
     * @param link the link to search for
     */
    private void insertUrlsList( Link link )
    {
        // optional links insertion
        DAOUtil daoUtil = new DAOUtil( INSERT_URLS_SQL );

        for ( ReferenceItem item : link.getOptionalUrls(  ) )
        {
            daoUtil.setInt( 1, link.getId(  ) );
            daoUtil.setString( 2, item.getCode(  ) );
            daoUtil.setString( 3, item.getName(  ) );

            daoUtil.executeUpdate(  );
        }

        daoUtil.free(  );
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.plugins.links.business.ILinkDAO#loadImageResource(int)
         */
    public ImageResource loadImageResource( int nIdLink )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_RESOURCE_IMAGE );
        daoUtil.setInt( 1, nIdLink );
        daoUtil.executeQuery(  );

        ImageResource image = null;

        if ( daoUtil.next(  ) )
        {
            image = new ImageResource(  );
            image.setImage( daoUtil.getBytes( 1 ) );
            image.setMimeType( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return image;
    }
}
