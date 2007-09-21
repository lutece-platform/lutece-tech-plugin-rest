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
package fr.paris.lutece.plugins.links.business.portlet;

import fr.paris.lutece.plugins.links.business.Link;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for LinksPortlet objects
 */
public final class LinksPortletDAO implements ILinksPortletDAO
{

    // Constants
    private static final String SQL_QUERY_SELECT = " SELECT id_portlet FROM core_portlet WHERE id_portlet = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO link_list_portlet ( id_portlet, id_link, link_order ) VALUES ( ? , ? , ? )";
    private static final String SQL_QUERY_DELETE = " DELETE FROM link_list_portlet WHERE id_portlet = ? ";   
    private static final String SQL_QUERY_DELETE_LINK_PORTLET = " DELETE FROM link_portlet WHERE id_portlet = ? ";
    private static final String SQL_QUERY_DELETE_LINK = " DELETE FROM link_list_portlet WHERE id_portlet=? AND id_link = ? ";
    private static final String SQL_QUERY_SELECT_LINK = " SELECT id_link, name, url FROM link ORDER BY name ";
    private static final String SQL_QUERY_SELECT_ID_LINK = " SELECT id_link FROM link_list_portlet WHERE  id_portlet = ? AND  id_link = ? ";
    private static final String SQL_QUERY_SELECT_LINK_IN_PORTLET_LIST = " SELECT a.id_link, a.name, a.url, a.description, a.image_content, a.mime_type " +
                                                                        " FROM link a, link_list_portlet b WHERE a.id_link = b.id_link AND b.id_portlet = ? " +
                                                                        " ORDER BY b.link_order";
    private static final String SQL_QUERY_SELECT_LINK_ORDER = " SELECT link_order FROM link_list_portlet WHERE id_portlet = ? AND id_link = ? "; 
    private static final String SQL_QUERY_SELECT_MAX_ORDER = " SELECT max( link_order ) FROM link_list_portlet WHERE id_portlet= ? ";
    private static final String SQL_QUERY_SELECT_PORTLET_MAX_ORDER = " SELECT max( portlet_link_order ) FROM link_portlet ";
    private static final String SQL_QUERY_UPDATE_LINK_ORDER = " UPDATE link_list_portlet SET link_order = ? WHERE id_portlet = ? AND id_link = ? ";
    private static final String SQL_QUERY_SELECT_LINK_ID_BY_ORDER = " SELECT id_link FROM link_list_portlet WHERE id_portlet = ? AND link_order = ? ";
    private static final String SQL_QUERY_SELECT_UNSELECTED_PORTLET = " SELECT a.id_portlet, a.name FROM core_portlet a " +
                                                                      " LEFT JOIN link_portlet b ON a.id_portlet=b.id_portlet WHERE b.id_portlet is NULL " +
                                                                      " AND a.id_portlet_type= ? ";
    private static final String SQL_QUERY_SELECT_PORTLET_LINK_PAGE = " SELECT a.id_portlet, a.portlet_link_order, b.name " +
                                                                     " FROM link_portlet a, core_portlet b WHERE a.portlet_link_order > -1 " +
                                                                     " AND a.id_portlet=b.id_portlet ORDER BY a.portlet_link_order ";
    
    private static final String SQL_QUERY_SELECT_PORTLET_LINK_ORDER = " SELECT portlet_link_order FROM link_portlet WHERE id_portlet = ? ";
    private static final String SQL_QUERY_DELETE_PORTLET = " DELETE FROM link_portlet WHERE id_portlet= ? ";
    private static final String SQL_QUERY_DELETE_LINK_FROM_PORTLET = " DELETE FROM link_list_portlet WHERE id_link= ? ";
    private static final String SQL_QUERY_INSERT_INTO_PORTLET =  " INSERT INTO link_portlet ( id_portlet, portlet_link_order ) VALUES ( ? , ? ) ";
    private static final String SQL_QUERY_SELECT_PORTLET_ID =  " SELECT id_portlet FROM link_portlet WHERE portlet_link_order = ? ";
    private static final String SQL_QUERY_UPDATE_PORTLET_LINK =  " UPDATE link_portlet SET portlet_link_order = ? WHERE id_portlet = ? ";
    private static final String SQL_QUERY_SELECT_URL_LIST =  " SELECT virtual_host_key, url FROM link_virtual_host WHERE id_link = ? ";
    
    
    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Insert a new record in the table.
     *
     * @param portlet The identifier of the portlet
     */
    public void insert( Portlet portlet )
    {
    }

    /**
     * Insert a new record in the table.
     *
     * @param nPortletId The identifier of the portlet
     * @param nLinkId The identifier of the link
     * @param nOrder The order of the portlet to insert
     */
    public void insertLink( int nPortletId, int nLinkId, int nOrder )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        daoUtil.setInt( 1, nPortletId );
        daoUtil.setInt( 2, nLinkId );
        daoUtil.setInt( 3, nOrder );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Remove a specified link from a specified portlet
     *
     * @param nPortletId The identifier of the portlet
     * @param nLinkId The identifier of the link
     */
    public void deleteLink( int nPortletId, int nLinkId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_LINK );

        daoUtil.setInt( 1, nPortletId );
        daoUtil.setInt( 2, nLinkId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete a record from the table
     * @param nPortletId The identifier of the portlet
     */
    public void delete( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        daoUtil = new DAOUtil( SQL_QUERY_DELETE_LINK_PORTLET );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Gets the data from database
     * @param nPortletId The identifier of the portlet
     * @return portlet The instance of the object portlet
     */
    public Portlet load( int nPortletId )
    {
        LinksPortlet portlet = new LinksPortlet(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );

        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            portlet.setId( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );
        return portlet;
    }

    /**
     * Update the record in the table
     * @param portlet The instance of the object portlet
     */
    public void store( Portlet portlet )
    {
    }

    /**
     * Returns a list of all the links
     * @return A list of links in form of a ReferenceList object
     */
    public ReferenceList selectLinksList(  )
    {
        ReferenceList list = new ReferenceList(  );       
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LINK );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            list.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) + " " + daoUtil.getString( 3 ) );
        }

        daoUtil.free(  );
        return list;
    }

    /**
     * Check if a specified links is not already registered in a specified portlet
     * @param nPortletId The identifier of the portlet
     * @param nLinkId The identifier of the link
     * @return The result(boolean)
     */
    public boolean testDuplicate( int nPortletId, int nLinkId )
    {
        boolean bResult;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ID_LINK );

        daoUtil.setInt( 1, nPortletId );
        daoUtil.setInt( 2, nLinkId );
        daoUtil.executeQuery(  );

        bResult = daoUtil.next(  );
        daoUtil.free(  );

        return bResult;
    }

    /**
     * Return a list of links wich belong to a specified portlet
     *
     * @param nPortletId The identifier of the portlet
     * @return A collection of links objects
     */
    public Collection<Link> selectLinksInPortletList( int nPortletId )
    {
        ArrayList<Link> list = new ArrayList<Link>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LINK_IN_PORTLET_LIST );

        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Link link = new Link(  );
            link.setId( daoUtil.getInt( 1 ) );
            link.setName( daoUtil.getString( 2 ) );
            link.setUrl( daoUtil.getString( 3 ) );
            link.setDescription( daoUtil.getString( 4 ) );
            link.setImageContent( daoUtil.getBytes( 5 ) );
            link.setMimeType( daoUtil.getString( 6 ) );
            link.setOptionalUrls( this.selectUrlsList( link.getId(  ) ) );
            list.add( link );
        }

        daoUtil.free(  );
        return list;
    }

    /**
     * Return the order of a specified link in a specified portlet
     *
     * @param nPortletId The identifier of the portlet
     * @param nLinkId The identifier of the link
     * @return The link's order
     */
    public int selectLinkOrder( int nPortletId, int nLinkId )
    {
        int nOrder = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LINK_ORDER );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.setInt( 2, nLinkId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nOrder = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );
        return nOrder;
    }

    /**
     * Calculate a new primary key to add a new link
     * @param nPortletId The identifier of the portlet
     * @return The new key.
     */
    public int selectMaxOrder( int nPortletId )
    {
        int nOrder = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_MAX_ORDER );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nOrder = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );
        return nOrder;
    }

    /**
     * Returns the maximum order of the portlets in the links page
     * @return the max order
     */
    public int selectPortletMaxOrder(  )
    {
        int nOrder = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_MAX_ORDER );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nOrder = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );
        return nOrder;
    }

    /**
     * Update the order of a specified link in a specified portlet
     *
     * @param nPortletId The identifier of the portlet
     * @param nLinkId The identifier of the link
     * @param nOrder The new order
     */
    public void storeLinkOrder( int nOrder, int nPortletId, int nLinkId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_LINK_ORDER );

        daoUtil.setInt( 1, nOrder );
        daoUtil.setInt( 2, nPortletId );
        daoUtil.setInt( 3, nLinkId );

        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }

    /**
     * Returns the id of an link wich has a specified order in a specified portlet
     * @param nPortletId The identifier of the portlet
     * @param nOrder The link's order
     * @return The identifier of the link
     */
    public int selectLinkIdByOrder( int nPortletId, int nOrder )
    {
        int nResult = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LINK_ID_BY_ORDER );

        daoUtil.setInt( 1, nPortletId );
        daoUtil.setInt( 2, nOrder );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nResult = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );
        return nResult;
    }

    /**
     * Finds the portlets which have not been selected in the links page
     * @return the list of the unselected portlets
     */
    public ReferenceList findUnselectedPortlets(  )
    {
        ReferenceList list = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_UNSELECTED_PORTLET );
        String strPortletTypeId = LinksPortletHome.getInstance(  ).getPortletTypeId(  );

        daoUtil.setString( 1, strPortletTypeId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            list.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );
        return list;
    }

    /**
     * Selects the list of the portlets in the links page
     *
     * @return a collection of the unselected portlets
     */
    public Collection<Portlet> selectPortletsInLinksPage(  )
    {
        ArrayList<Portlet> list = new ArrayList<Portlet>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_LINK_PAGE );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            LinksPortlet portlet = new LinksPortlet(  );
            portlet.setId( daoUtil.getInt( 1 ) );
            portlet.setPortletOrder( daoUtil.getInt( 2 ) );
            portlet.setName( daoUtil.getString( 3 ) );
            list.add( portlet );
        }

        daoUtil.free(  );
        return list;
    }

    
    /**
     * Selects the order of a portlet in the links page
     * @param nPortletId The identifier of the portlet
     * @return the order
     */
    public int selectPortletOrder( int nPortletId )
    {
        int nOrder = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_LINK_ORDER );

        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nOrder = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );
        return nOrder;
    }

    /**
     * Remove a portlet from the links page
     * @param nPortletId The identifier of the portlet
     */
    public void removePortlet( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_PORTLET );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }

    /**
     * Removes a link from all the portlets
     * @param nLinkId The identifier of the link
     */
    public void removeLinkFromPortlets( int nLinkId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_LINK_FROM_PORTLET );
        daoUtil.setInt( 1, nLinkId );
        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }

    /**
     * Insert a new portlet in the links page
     * @param nPortletId The identifier of the portlet
     * @param nOrder The order of the portlet
     */
    public void insertPortlet( int nPortletId, int nOrder )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_INTO_PORTLET );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.setInt( 2, nOrder );
        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }

    /**
     * Selects a portlet Id from the links page by its order
     * @param nOrder The order of the portlet
     * @return the portlet Id
     */
    public int selectPortletIdByOrder( int nOrder )
    {
        int nResult = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_ID );
        daoUtil.setInt( 1, nOrder );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nResult = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );
        return nResult;
    }

    /**
     * Stores the order of a portlet in the links page

     * @param nOrder The order of the portlet
     * @param nPortletId The identifier of the portlet
     */
    public void storePortletOrder( int nOrder, int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_PORTLET_LINK );
        daoUtil.setInt( 1, nOrder );
        daoUtil.setInt( 2, nPortletId );
        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }

    /**
     * load all the optional urls
     *
     * @param idLink the link's id
     * @return the optional urls ReferenceList
     */
    private ReferenceList selectUrlsList( int idLink )
    {
        ReferenceList list = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_URL_LIST );
        daoUtil.setInt( 1, idLink );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            list.addItem( daoUtil.getString( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );
        return list;
    }
}
