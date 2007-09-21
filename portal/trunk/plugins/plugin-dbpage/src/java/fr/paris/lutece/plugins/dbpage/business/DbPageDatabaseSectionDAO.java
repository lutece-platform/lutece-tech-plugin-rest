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
package fr.paris.lutece.plugins.dbpage.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for DbPageDatabaseSection objects
 */
public final class DbPageDatabaseSectionDAO implements IDbPageDatabaseSectionDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_section ) FROM dbpage_section ";
    private static final String SQL_QUERY_SELECT = " SELECT id_section, id_page, id_type, template, desc_column, desc_sql, pool, title , id_order, role FROM dbpage_section WHERE id_section = ?  ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO dbpage_section ( id_section, id_page, id_type, template, desc_column, desc_sql, pool, title , id_order, role ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? , ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM dbpage_section WHERE id_section = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE dbpage_section SET id_section = ?, id_page = ?, id_type = ?, template = ?, desc_column = ?, desc_sql = ?, pool = ?, title = ? , id_order = ?, role = ? WHERE id_section = ?  ";
    private static final String SQL_QUERY_SELECT_ALL = " SELECT id_section, id_page, id_type, template, desc_column, desc_sql, pool, title , id_order, role FROM dbpage_section ORDER BY id_order ";
    private static final String SQL_QUERY_COUNT_SECTIONS = " SELECT count(*) FROM dbpage_section WHERE id_page= ? ";
    private static final String SQL_QUERY_SECTIONS_BY_PAGE = "SELECT id_section, id_page, id_type, template, desc_column, desc_sql, pool, title , id_order, role FROM dbpage_section WHERE id_page = ? ORDER BY id_order ";
    private static final String SQL_QUERY_SELECT_COMBO = "SELECT id_order , id_order FROM dbpage_section WHERE id_page=? ";
    private static final String SQL_QUERY_MODIFY_ORDER = "UPDATE dbpage_section SET id_order = ?  WHERE id_section = ? and id_page = ? ";
    private static final String SQL_QUERY_SELECT_ORDER_BY_ID = "SELECT id_section FROM dbpage_section  WHERE id_order = ? and id_page = ? ";
    private static final String SQL_QUERY_MAX_ORDER_BY_PAGE = "SELECT MAX(id_order) FROM dbpage_section  WHERE id_page= ?  ";
    private static final String SQL_QUERY_REORDER_SECTION = " UPDATE dbpage_section SET id_order = ? WHERE id_section = ? ";

    /**
     * Generates a new primary key
     * @return The new primary key
     */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
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

    /**
     * Insert a new record in the table.
     * @param dbPageDatabaseSection The dbPageDatabaseSection object
     */
    public void insert( DbPageDatabaseSection dbPageDatabaseSection, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        dbPageDatabaseSection.setId( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, dbPageDatabaseSection.getId(  ) );
        daoUtil.setInt( 2, dbPageDatabaseSection.getIdPage(  ) );
        daoUtil.setInt( 3, dbPageDatabaseSection.getIdType(  ) );
        daoUtil.setString( 4, dbPageDatabaseSection.getTemplatePath(  ) );
        daoUtil.setString( 5, dbPageDatabaseSection.getColumn(  ) );
        daoUtil.setString( 6, dbPageDatabaseSection.getSql(  ) );
        daoUtil.setString( 7, dbPageDatabaseSection.getPool(  ) );
        daoUtil.setString( 8, dbPageDatabaseSection.getTitle(  ) );
        daoUtil.setInt( 9, dbPageDatabaseSection.getOrder(  ) );
        daoUtil.setString( 10, dbPageDatabaseSection.getRole(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of DbPageDatabaseSection from the table
     * @return the instance of the DbPageDatabaseSection
     * @param nDbPageDatabaseSectionId The identifier of DbPageDatabaseSection
     */
    public DbPageDatabaseSection load( int nDbPageDatabaseSectionId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nDbPageDatabaseSectionId );
        daoUtil.executeQuery(  );

        DbPageDatabaseSection dbPageDatabaseSection = null;

        if ( daoUtil.next(  ) )
        {
            dbPageDatabaseSection = new DbPageDatabaseSection(  );
            dbPageDatabaseSection.setId( daoUtil.getInt( 1 ) );
            dbPageDatabaseSection.setIdPage( daoUtil.getInt( 2 ) );
            dbPageDatabaseSection.setIdType( daoUtil.getInt( 3 ) );
            dbPageDatabaseSection.setTemplatePath( daoUtil.getString( 4 ) );
            dbPageDatabaseSection.setColumn( daoUtil.getString( 5 ) );
            dbPageDatabaseSection.setSql( daoUtil.getString( 6 ) );
            dbPageDatabaseSection.setPool( daoUtil.getString( 7 ) );
            dbPageDatabaseSection.setTitle( daoUtil.getString( 8 ) );
            dbPageDatabaseSection.setOrder( daoUtil.getInt( 9 ) );
            dbPageDatabaseSection.setRole( daoUtil.getString( 10 ) );
        }

        daoUtil.free(  );

        return dbPageDatabaseSection;
    }

    /**
     * Load the sections related to a page
     * @return the A List of the DbPageDatabaseSections
     * @param nDbPageId The identifier of DbPageDatabase
     */
    public List<DbPageDatabaseSection> loadSectionsByPageId( int nDbPageId, Plugin plugin )
    {
        List<DbPageDatabaseSection> listDbPageDatabaseSections = new ArrayList<DbPageDatabaseSection>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SECTIONS_BY_PAGE, plugin );
        daoUtil.setInt( 1, nDbPageId );
        daoUtil.executeQuery(  );

        DbPageDatabaseSection dbPageDatabaseSection = null;

        while ( daoUtil.next(  ) )
        {
            dbPageDatabaseSection = new DbPageDatabaseSection(  );
            dbPageDatabaseSection.setId( daoUtil.getInt( 1 ) );
            dbPageDatabaseSection.setIdPage( daoUtil.getInt( 2 ) );
            dbPageDatabaseSection.setIdType( daoUtil.getInt( 3 ) );
            dbPageDatabaseSection.setTemplatePath( daoUtil.getString( 4 ) );
            dbPageDatabaseSection.setColumn( daoUtil.getString( 5 ) );
            dbPageDatabaseSection.setSql( daoUtil.getString( 6 ) );
            dbPageDatabaseSection.setPool( daoUtil.getString( 7 ) );
            dbPageDatabaseSection.setTitle( daoUtil.getString( 8 ) );
            dbPageDatabaseSection.setOrder( daoUtil.getInt( 9 ) );
            dbPageDatabaseSection.setRole( daoUtil.getString( 10 ) );

            listDbPageDatabaseSections.add( dbPageDatabaseSection );
        }

        daoUtil.free(  );

        return listDbPageDatabaseSections;
    }

    /**
     * Delete a record from the table
     * @param nDbPageDatabaseSectionId The DbPageDatabaseSection Id
     */
    public void delete( int nDbPageDatabaseSectionId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nDbPageDatabaseSectionId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param dbPageDatabaseSection The reference of dbPageDatabaseSection
     */
    public void store( DbPageDatabaseSection dbPageDatabaseSection, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, dbPageDatabaseSection.getId(  ) );
        daoUtil.setInt( 2, dbPageDatabaseSection.getIdPage(  ) );
        daoUtil.setInt( 3, dbPageDatabaseSection.getIdType(  ) );
        daoUtil.setString( 4, dbPageDatabaseSection.getTemplatePath(  ) );
        daoUtil.setString( 5, dbPageDatabaseSection.getColumn(  ) );
        daoUtil.setString( 6, dbPageDatabaseSection.getSql(  ) );
        daoUtil.setString( 7, dbPageDatabaseSection.getPool(  ) );
        daoUtil.setString( 8, dbPageDatabaseSection.getTitle(  ) );
        daoUtil.setInt( 9, dbPageDatabaseSection.getOrder(  ) );
        daoUtil.setString( 10, dbPageDatabaseSection.getRole(  ) );

        daoUtil.setInt( 11, dbPageDatabaseSection.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the list of dbPageDatabaseSections by page
     * @return The List of the DbPageDatabaseSections
     */
    public List<DbPageDatabaseSection> selectDbPageDatabaseSectionList( Plugin plugin )
    {
        List<DbPageDatabaseSection> listDbPageDatabaseSections = new ArrayList<DbPageDatabaseSection>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DbPageDatabaseSection dbPageDatabaseSection = new DbPageDatabaseSection(  );
            dbPageDatabaseSection.setId( daoUtil.getInt( 1 ) );
            dbPageDatabaseSection.setIdPage( daoUtil.getInt( 2 ) );
            dbPageDatabaseSection.setIdType( daoUtil.getInt( 3 ) );
            dbPageDatabaseSection.setTemplatePath( daoUtil.getString( 4 ) );
            dbPageDatabaseSection.setColumn( daoUtil.getString( 5 ) );
            dbPageDatabaseSection.setSql( daoUtil.getString( 6 ) );
            dbPageDatabaseSection.setPool( daoUtil.getString( 7 ) );
            dbPageDatabaseSection.setTitle( daoUtil.getString( 8 ) );
            dbPageDatabaseSection.setOrder( daoUtil.getInt( 9 ) );
            dbPageDatabaseSection.setRole( daoUtil.getString( 10 ) );

            listDbPageDatabaseSections.add( dbPageDatabaseSection );
        }

        daoUtil.free(  );

        return listDbPageDatabaseSections;
    }

    /**
     * Calculates the number of sections in a page
     * @param nDbPageId The id of the page
     * @return The number of sections
     */
    public int countSections( int nDbPageId, Plugin plugin )
    {
        int nCount = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_SECTIONS, plugin );
        daoUtil.setInt( 1, nDbPageId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nCount = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nCount;
    }

    /**
     * Load the list of orders
     * @return The ReferenceList of the Orders
     * @param nPageId The identifier of the page
     */
    public ReferenceList selectOrderList( int nPageId, Plugin plugin )
    {
        ReferenceList orderList = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_COMBO, plugin );
        daoUtil.setInt( 1, nPageId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            orderList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return orderList;
    }

    /**
     * Modify the order of a section
     *
     * @param nPageId The Page identifier
     * @param nNewOrder The order number
     * @param nIdSection The Section identifier
     */
    public void getModifySectionOrder( int nPageId, int nNewOrder, int nIdSection, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_MODIFY_ORDER, plugin );
        daoUtil.setInt( 1, nNewOrder );
        daoUtil.setInt( 2, nIdSection );
        daoUtil.setInt( 3, nPageId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Returns the identifier of a section in a distinct order
     * @return The order of the Section
     * @param nPageId The identifier of the page
     * @param nSectionOrder The order number
     */
    public int selectIdByOrder( int nPageId, int nSectionOrder, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ORDER_BY_ID, plugin );
        int nResult = nSectionOrder;
        daoUtil.setInt( 1, nSectionOrder );
        daoUtil.setInt( 2, nPageId );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            // If number order doesn't exist
            nResult = 1;
        }

        nResult = daoUtil.getInt( 1 );
        daoUtil.free(  );

        return nResult;
    }

    /**
     * Returns the highest order on a page
     * @return The order of the Section
     * @param nPageId The identifier of the page
     */
    public int selectMaxIdOrder( int nPageId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_MAX_ORDER_BY_PAGE, plugin );
        int nResult = nPageId;
        daoUtil.setInt( 1, nPageId );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            // If number order doesn't exist
            nResult = 1;
        }

        nResult = daoUtil.getInt( 1 );
        daoUtil.free(  );

        return nResult;
    }

    /**
     *
     * @param nIdSection1 the section order
     * @param nOrderSection1 the section order
     * @param nIdSection2 the section order
     * @param nOrderSection2 the section order
     */
    public void reorderSections( int nIdSection1, int nOrderSection1, int nIdSection2, int nOrderSection2, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REORDER_SECTION, plugin );
        daoUtil.setInt( 1, nOrderSection1 );
        daoUtil.setInt( 2, nIdSection1 );
        daoUtil.executeUpdate(  );
        daoUtil.setInt( 1, nOrderSection2 );
        daoUtil.setInt( 2, nIdSection2 );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
