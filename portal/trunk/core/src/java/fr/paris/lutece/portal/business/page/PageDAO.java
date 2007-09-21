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
package fr.paris.lutece.portal.business.page;

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * This class porvides Data Access methods for Page objects
 */
public final class PageDAO implements IPageDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = " SELECT max(id_page) FROM core_page";
    private static final String SQL_QUERY_SELECT = " SELECT a.id_parent, a.name, a.description, a.id_template, b.file_name, " +
        " a.page_order, a.status, a.role , a.code_theme , a.node_status , a.image_content, a.mime_type " +
        " FROM core_page a, core_page_template b WHERE a.id_template = b.id_template AND a.id_page = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO core_page ( id_page , id_parent , name , description, date_update, " +
        " id_template,  page_order, status, role, date_creation, code_theme , node_status, image_content , mime_type ) " +
        " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = " DELETE FROM core_page WHERE id_page = ?";
    private static final String SQL_QUERY_UPDATE = " UPDATE core_page SET id_parent = ?,  name = ?, description = ? , date_update = ? , " +
        " id_template = ? , page_order = ? , status = ? , role = ? , code_theme = ? , node_status = ? , " +
        " image_content = ? , mime_type = ? " + " WHERE id_page = ?";
    private static final String SQL_QUERY_CHECKPK = " SELECT id_page FROM core_page WHERE id_page = ?";
    private static final String SQL_QUERY_CHILDPAGE = " SELECT id_page , id_parent, name, description, " +
        " page_order , status , role, code_theme, image_content, mime_type " +
        " FROM core_page WHERE id_parent = ? ORDER BY page_order";
    private static final String SQL_QUERY_SELECTALL = " SELECT id_page , id_parent,  name, description, date_update, " +
        " page_order, status, role, code_theme, image_content, mime_type FROM core_page ";
    private static final String SQL_QUERY_SELECT_PORTLET = " SELECT id_portlet FROM core_portlet WHERE id_page = ? ORDER BY portlet_order";
    private static final String SQL_QUERY_UPDATE_PAGE_DATE = " UPDATE core_page SET date_update = ? WHERE id_page = ?";
    private static final String SQL_QUERY_SELECTALL_NODE_PAGE = " SELECT id_page ,  name FROM core_page WHERE node_status = 0";
    private static final String SQL_QUERY_NEW_CHILD_PAGE_ORDER = " SELECT max(page_order) FROM core_page WHERE id_parent = ?";
    private static final String SQL_QUERY_CHECK_PAGE_EXIST = " SELECT id_page FROM core_page " + " WHERE id_page = ? ";

    // ImageResource queries
    private static final String SQL_QUERY_SELECT_RESOURCE_IMAGE = " SELECT image_content , mime_type FROM core_page " +
        " WHERE id_page = ? ";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Generates a new primary key
     * @return The new primary key
     */
    int newPrimaryKey(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nKey;
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.page.IPageDAO#insert(fr.paris.lutece.portal.business.page.Page)
         */
    public void insert( Page page )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        int nNewPrimaryKey = newPrimaryKey(  );
        page.setId( nNewPrimaryKey );

        daoUtil.setInt( 1, page.getId(  ) );
        daoUtil.setInt( 2, page.getParentPageId(  ) );
        daoUtil.setString( 3, page.getName(  ) );
        daoUtil.setString( 4, page.getDescription(  ) );
        page.setDateUpdate( new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setTimestamp( 5, page.getDateUpdate(  ) );
        daoUtil.setInt( 6, page.getPageTemplateId(  ) );
        daoUtil.setInt( 7, page.getOrder(  ) );
        daoUtil.setInt( 8, page.getStatus(  ) );
        daoUtil.setString( 9, page.getRole(  ) );

        // For a new object, update time = creation time
        daoUtil.setTimestamp( 10, page.getDateUpdate(  ) );
        daoUtil.setString( 11, page.getCodeTheme(  ) );
        daoUtil.setInt( 12, page.getNodeStatus(  ) );
        daoUtil.setBytes( 13, page.getImageContent(  ) );
        daoUtil.setString( 14, page.getMimeType(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.page.IPageDAO#load(int, boolean)
         */
    public Page load( int nPageId, boolean bPortlets )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nPageId );

        daoUtil.executeQuery(  );

        Page page = new Page(  );

        if ( daoUtil.next(  ) )
        {
            page.setId( nPageId );
            page.setParentPageId( daoUtil.getInt( 1 ) );
            page.setName( daoUtil.getString( 2 ) );
            page.setDescription( daoUtil.getString( 3 ) );
            page.setPageTemplateId( daoUtil.getInt( 4 ) );
            page.setTemplate( daoUtil.getString( 5 ) );
            page.setOrder( daoUtil.getInt( 6 ) );
            page.setStatus( daoUtil.getInt( 7 ) );
            page.setRole( daoUtil.getString( 8 ) );
            page.setCodeTheme( daoUtil.getString( 9 ) );
            page.setNodeStatus( daoUtil.getInt( 10 ) );
            page.setImageContent( daoUtil.getBytes( 11 ) );
            page.setMimeType( daoUtil.getString( 12 ) );

            // Loads the portlets contained into the page
            if ( bPortlets )
            {
                loadPortlets( page );
            }
        }

        daoUtil.free(  );

        return page;
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.page.IPageDAO#delete(int)
         */
    public void delete( int nPageId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nPageId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.page.IPageDAO#store(fr.paris.lutece.portal.business.page.Page)
         */
    public void store( Page page )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        daoUtil.setInt( 1, page.getParentPageId(  ) );
        daoUtil.setString( 2, page.getName(  ) );
        daoUtil.setString( 3, page.getDescription(  ) );
        page.setDateUpdate( new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setTimestamp( 4, page.getDateUpdate(  ) );
        daoUtil.setInt( 5, page.getPageTemplateId(  ) );
        daoUtil.setInt( 6, page.getOrder(  ) );
        daoUtil.setInt( 7, page.getStatus(  ) );
        daoUtil.setString( 8, page.getRole(  ) );
        daoUtil.setString( 9, page.getCodeTheme(  ) );
        daoUtil.setInt( 10, page.getNodeStatus(  ) );
        daoUtil.setBytes( 11, page.getImageContent(  ) );
        daoUtil.setString( 12, page.getMimeType(  ) );
        daoUtil.setInt( 13, page.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Checks if the page identifier exists
     * @param nKey  The page identifier
     * @return true if the identifier exists, false if not
     */
    boolean checkPrimaryKey( int nKey )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECKPK );

        daoUtil.setInt( 1, nKey );
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
     * loads the portlets list contained into the page
     *
     * @param page The object page
     */
    void loadPortlets( Page page )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLET );
        daoUtil.setInt( 1, page.getId(  ) );

        daoUtil.executeQuery(  );

        ArrayList<Portlet> pageColl = new ArrayList<Portlet>(  );

        while ( daoUtil.next(  ) )
        {
            int nPortletId = daoUtil.getInt( 1 );
            Portlet portlet = PortletHome.findByPrimaryKey( nPortletId );
            pageColl.add( portlet );
        }

        page.setPortlets( pageColl );
        daoUtil.free(  );
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.page.IPageDAO#selectChildPages(int)
         */
    public Collection<Page> selectChildPages( int nParentPageId )
    {
        Collection<Page> pageList = new ArrayList<Page>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHILDPAGE );
        daoUtil.setInt( 1, nParentPageId );

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Page page = new Page(  );

            page.setId( daoUtil.getInt( 1 ) );
            page.setParentPageId( daoUtil.getInt( 2 ) );
            page.setName( daoUtil.getString( 3 ) );
            page.setDescription( daoUtil.getString( 4 ) );
            page.setOrder( daoUtil.getInt( 5 ) );
            page.setStatus( daoUtil.getInt( 6 ) );
            page.setRole( daoUtil.getString( 7 ) );
            page.setCodeTheme( daoUtil.getString( 8 ) );
            page.setImageContent( daoUtil.getBytes( 9 ) );
            page.setMimeType( daoUtil.getString( 10 ) );
            pageList.add( page );
        }

        daoUtil.free(  );

        return pageList;
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.page.IPageDAO#selectAllPages()
         */
    public List<Page> selectAllPages(  )
    {
        List<Page> pageList = new ArrayList<Page>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Page page = new Page(  );

            page.setId( daoUtil.getInt( 1 ) );
            page.setParentPageId( daoUtil.getInt( 2 ) );
            page.setName( daoUtil.getString( 3 ) );
            page.setDescription( daoUtil.getString( 4 ) );
            page.setDateUpdate( daoUtil.getTimestamp( 5 ) );
            page.setOrder( daoUtil.getInt( 6 ) );
            page.setStatus( daoUtil.getInt( 7 ) );
            page.setRole( daoUtil.getString( 8 ) );
            page.setCodeTheme( daoUtil.getString( 9 ) );
            page.setImageContent( daoUtil.getBytes( 10 ) );
            page.setMimeType( daoUtil.getString( 11 ) );
            pageList.add( page );
        }

        daoUtil.free(  );

        return pageList;
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.page.IPageDAO#invalidatePage(int)
         */
    public void invalidatePage( int nPageId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_PAGE_DATE );

        daoUtil.setTimestamp( 1, new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setInt( 2, nPageId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.portal.business.page.IPageDAO#getPagesList()
     */
    public ReferenceList getPagesList(  )
    {
        ReferenceList listPages = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_NODE_PAGE );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Page page = new Page(  );
            page.setId( daoUtil.getInt( 1 ) );
            page.setName( daoUtil.getString( 2 ) );
            listPages.addItem( page.getId(  ), page.getName(  ) );
        }

        daoUtil.free(  );

        return listPages;
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.page.IPageDAO#selectNewChildPageOrder(int)
         */
    public int selectNewChildPageOrder( int nParentPageId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_CHILD_PAGE_ORDER );
        daoUtil.setInt( 1, nParentPageId );
        daoUtil.executeQuery(  );

        int nPageOrder;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nPageOrder = 1;
        }

        nPageOrder = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nPageOrder;
    }

    /* (non-Javadoc)
         * @see fr.paris.lutece.portal.business.page.IPageDAO#loadImageResource(int)
         */
    public ImageResource loadImageResource( int nIdPage )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_RESOURCE_IMAGE );
        daoUtil.setInt( 1, nIdPage );
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

    /**
     * Tests if page exist
     *
     * @param nPageId The identifier of the document
     * @return true if the page existed, false otherwise
     */
    public boolean checkPageExist( int nPageId )
    {
        boolean bPageExisted = false;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_PAGE_EXIST );

        daoUtil.setInt( 1, nPageId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            bPageExisted = true;
        }

        daoUtil.free(  );

        return bPageExisted;
    }
}
