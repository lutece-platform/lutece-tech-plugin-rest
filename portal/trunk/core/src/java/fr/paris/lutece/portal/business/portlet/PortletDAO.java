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
package fr.paris.lutece.portal.business.portlet;

import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * This class provides Data Access methods for Portlet objects
 */
public final class PortletDAO implements IPortletDAO
{
    // queries
    private static final String SQL_QUERY_NEW_PK = "SELECT max(id_portlet) FROM core_portlet ";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_portlet SET name = ?, date_update = ?, column_no = ?, " +
        " portlet_order = ? , id_style = ? , accept_alias = ? , display_portlet_title = ? " + " WHERE id_portlet = ?";
    private static final String SQL_QUERY_SELECT = " SELECT b.id_portlet_type, a.id_page, a.id_style, a.name , b.name, " +
        " b.url_creation, b.url_update, a.date_update, a.column_no, a.portlet_order, " +
        " b.home_class, a.accept_alias , b.plugin_name , a.display_portlet_title, a.status " +
        " FROM core_portlet a , core_portlet_type b WHERE a.id_portlet_type = b.id_portlet_type AND a.id_portlet = ?";
    private static final String SQL_QUERY_SELECT_ALIAS = " SELECT a.id_portlet FROM core_portlet a, core_portlet_alias b " +
        " WHERE a.id_portlet = b.id_portlet AND b.id_alias= ? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM core_portlet WHERE id_portlet = ?";
    private static final String SQL_QUERY_UPDATE_STATUS = " UPDATE core_portlet SET status = ? WHERE id_portlet = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_portlet ( id_portlet, id_portlet_type, id_page, id_style, name, " +
        " date_creation, date_update, column_no, portlet_order, accept_alias, display_portlet_title ) " +
        " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?)";
    private static final String SQL_QUERY_SELECT_PORTLET_LIST_BY_STYLE = "SELECT id_portlet, name, id_page FROM core_portlet WHERE id_style=?";
    private static final String SQL_QUERY_SELECT_XSL_FILE = " SELECT a.id_stylesheet , a.description , a.file_name, a.source " +
        " FROM core_stylesheet a, core_portlet b, core_style_mode_stylesheet c " +
        " WHERE a.id_stylesheet = c.id_stylesheet " +
        " AND b.id_style = c.id_style AND b.id_portlet = ? AND c.id_mode = ? ";
    private static final String SQL_QUERY_SELECT_STYLE_LIST = " SELECT distinct a.id_style , a.description_style " +
        " FROM core_style a , core_style_mode_stylesheet b " + " WHERE  a.id_style = b.id_style " +
        " AND a.id_portlet_type = ? ";
    private static final String SQL_QUERY_SELECT_PORTLET_TYPE = " SELECT id_portlet_type , name , url_creation, url_update, plugin_name " +
        " FROM core_portlet_type WHERE id_portlet_type = ? ORDER BY id_portlet_type ";
    private static final String SQL_QUERY_SELECT_PORTLET_ALIAS = " SELECT a.id_portlet FROM core_portlet a , core_portlet_alias b" +
        " WHERE a.id_portlet = b.id_portlet " + " AND b.id_alias= ? ";
    private static final String SQL_QUERY_SELECT_PORTLET_LIST_BY_NAME = " SELECT id_portlet , id_page , name FROM core_portlet WHERE name LIKE ? ";
    private static final String SQL_QUERY_SELECT_PORTLET_LIST_BY_TYPE = " SELECT a.id_portlet, a.name , a.date_update " +
        " FROM core_portlet a, core_page b  WHERE a.id_page = b.id_page " + " AND a.id_portlet_type = ? ";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.portlet.IPortletDAO#insert(fr.paris.lutece.portal.business.portlet.Portlet)
         */
    public void insert( Portlet portlet )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        daoUtil.setInt( 1, portlet.getId(  ) );
        daoUtil.setString( 2, portlet.getPortletTypeId(  ) );
        daoUtil.setInt( 3, portlet.getPageId(  ) );
        daoUtil.setInt( 4, portlet.getStyleId(  ) );
        daoUtil.setString( 5, portlet.getName(  ) );
        daoUtil.setTimestamp( 6, new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setTimestamp( 7, new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setInt( 8, portlet.getColumn(  ) );
        daoUtil.setInt( 9, portlet.getOrder(  ) );
        daoUtil.setInt( 10, portlet.getAcceptAlias(  ) );
        daoUtil.setInt( 11, portlet.getDisplayPortletTitle(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.portlet.IPortletDAO#delete(int)
         */
    public void delete( int nPortletId )
    {
        // we recover the alias of the portlet parent to delete
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALIAS );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AliasPortletHome.getInstance(  ).remove( PortletHome.findByPrimaryKey( daoUtil.getInt( 1 ) ) );
        }

        daoUtil.free(  );

        // we delete the portlet
        daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nPortletId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.portlet.IPortletDAO#load(int)
         */
    public Portlet load( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        PortletImpl portlet = new PortletImpl(  );

        if ( daoUtil.next(  ) )
        {
            portlet.setId( nPortletId );
            portlet.setPortletTypeId( daoUtil.getString( 1 ) );
            portlet.setPageId( daoUtil.getInt( 2 ) );
            portlet.setStyleId( daoUtil.getInt( 3 ) );
            portlet.setName( daoUtil.getString( 4 ) );
            portlet.setPortletTypeName( daoUtil.getString( 5 ) );
            portlet.setUrlCreation( daoUtil.getString( 6 ) );
            portlet.setUrlUpdate( daoUtil.getString( 7 ) );
            portlet.setDateUpdate( daoUtil.getTimestamp( 8 ) );
            portlet.setColumn( daoUtil.getInt( 9 ) );
            portlet.setOrder( daoUtil.getInt( 10 ) );
            portlet.setHomeClassName( daoUtil.getString( 11 ) );
            portlet.setAcceptAlias( daoUtil.getInt( 12 ) );
            portlet.setPluginName( daoUtil.getString( 13 ) );
            portlet.setDisplayPortletTitle( daoUtil.getInt( 14 ) );
            portlet.setStatus( daoUtil.getInt( 15 ) );
        }

        daoUtil.free(  );

        return portlet;
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.portlet.IPortletDAO#store(fr.paris.lutece.portal.business.portlet.Portlet)
         */
    public void store( Portlet portlet )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setString( 1, portlet.getName(  ) );
        daoUtil.setTimestamp( 2, new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setInt( 3, portlet.getColumn(  ) );
        daoUtil.setInt( 4, portlet.getOrder(  ) );
        daoUtil.setInt( 5, portlet.getStyleId(  ) );
        daoUtil.setInt( 6, portlet.getAcceptAlias(  ) );
        daoUtil.setInt( 7, portlet.getDisplayPortletTitle(  ) );
        daoUtil.setInt( 8, portlet.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Returns a new primary key which will be used to add a new portlet
     *
     * @return The new key.
     */
    public int newPrimaryKey(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            nKey = 1; // if the table is empty
        }

        nKey = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nKey;
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.portlet.IPortletDAO#updateStatus(fr.paris.lutece.portal.business.portlet.Portlet, int)
         */
    public void updateStatus( Portlet portlet, int nStatus )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_STATUS );

        daoUtil.setInt( 1, nStatus );
        daoUtil.setInt( 2, portlet.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.portlet.IPortletDAO#selectXslFile(int, int)
         */
    public StyleSheet selectXslFile( int nPortletId, int nIdMode )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_XSL_FILE );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.setInt( 2, nIdMode );
        daoUtil.executeQuery(  );

        StyleSheet stylesheet = new StyleSheet(  );

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

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.portlet.IPortletDAO#selectPortletsListbyName(java.lang.String)
         */
    public Collection selectPortletsListbyName( String strPortletName )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_LIST_BY_NAME );
        daoUtil.setString( 1, '%' + strPortletName + '%' );
        daoUtil.executeQuery(  );

        ArrayList list = new ArrayList(  );

        while ( daoUtil.next(  ) )
        {
            PortletImpl portlet = new PortletImpl(  );
            portlet.setId( daoUtil.getInt( 1 ) );
            portlet.setPageId( daoUtil.getInt( 2 ) );
            portlet.setName( daoUtil.getString( 3 ) );
            list.add( portlet );
        }

        daoUtil.free(  );

        return list;
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.portlet.IPortletDAO#selectPortletsByType(java.lang.String)
         */
    public List<Portlet> selectPortletsByType( String strPortletTypeId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_LIST_BY_TYPE );
        daoUtil.setString( 1, strPortletTypeId );
        daoUtil.executeQuery(  );

        List<Portlet> list = new ArrayList(  );

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

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.portlet.IPortletDAO#selectStylesList(java.lang.String)
         */
    public ReferenceList selectStylesList( String strPortletTypeId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_STYLE_LIST );
        daoUtil.setString( 1, strPortletTypeId );
        daoUtil.executeQuery(  );

        ReferenceList list = new ReferenceList(  );

        while ( daoUtil.next(  ) )
        {
            list.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return list;
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.portlet.IPortletDAO#hasAlias(int)
         */
    public boolean hasAlias( int nPortletId )
    {
        boolean bHasAlias = false;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_ALIAS );
        daoUtil.setInt( 1, nPortletId );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            bHasAlias = true;
        }

        daoUtil.free(  );

        return bHasAlias;
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.portlet.IPortletDAO#selectPortletType(java.lang.String)
         */
    public PortletType selectPortletType( String strPortletTypeId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_TYPE );

        daoUtil.setString( 1, strPortletTypeId );
        daoUtil.executeQuery(  );

        PortletType portletType = new PortletType(  );

        if ( daoUtil.next(  ) )
        {
            portletType.setId( daoUtil.getString( 1 ) );
            portletType.setNameKey( daoUtil.getString( 2 ) );
            portletType.setUrlCreation( daoUtil.getString( 3 ) );
            portletType.setUrlUpdate( daoUtil.getString( 4 ) );
            portletType.setPluginName( daoUtil.getString( 5 ) );
        }

        daoUtil.free(  );

        return portletType;
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.portlet.IPortletDAO#selectPortletListByStyle(int)
         */
    public Collection selectPortletListByStyle( int nStyleId )
    {
        Collection portletList = new ArrayList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET_LIST_BY_STYLE );

        daoUtil.setInt( 1, nStyleId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            PortletImpl portlet = new PortletImpl(  );

            portlet.setId( daoUtil.getInt( 1 ) );
            portlet.setName( daoUtil.getString( 2 ) );
            portlet.setPageId( daoUtil.getInt( 3 ) );

            portletList.add( portlet );
        }

        daoUtil.free(  );

        return portletList;
    }
}
