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
package fr.paris.lutece.plugins.dbpage.business.portlet;

import fr.paris.lutece.portal.business.portlet.IPortletInterfaceDAO;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * this class provides Data Access methods for DbPagePortlet objects
 */
public final class DbPagePortletDAO implements IDbPagePortletDAO
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT id_portlet, dbpage_name, dbpage_values FROM dbpage_portlet WHERE id_portlet = ? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO dbpage_portlet ( id_portlet, dbpage_name, dbpage_values ) VALUES ( ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = "DELETE FROM dbpage_portlet WHERE id_portlet = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE dbpage_portlet SET id_portlet = ?, dbpage_name = ?, dbpage_values = ? WHERE id_portlet = ? ";
    private static final String SQL_QUERY_CHECK = " SELECT dbpage_name FROM dbpage_portlet WHERE dbpage_name = ? ";

    ///////////////////////////////////////////////////////////////////////////////////////
    // Access methods to data

    /**
     * Delete record from table
     *
     * @param nPortletId The indentifier of the Portlet
     */
    public void delete( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Insert a new record in the table.
     *
     * @param portlet The Instance of the Portlet
     */
    public void insert( Portlet portlet )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );

        DbPagePortlet p = (DbPagePortlet) portlet;

        daoUtil.setInt( 1, p.getId(  ) );
        daoUtil.setString( 2, p.getDbPageName(  ) );
        daoUtil.setString( 3, p.getDbValues(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * load the data of dbpagePortlet from the table
     *
     * @param nIdPortlet The indentifier of the  portlet
     * @return portlet The instance of the object portlet
     */
    public Portlet load( int nIdPortlet )
    {
        DbPagePortlet portlet = new DbPagePortlet(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nIdPortlet );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            portlet.setId( daoUtil.getInt( 1 ) );
            portlet.setDbPageName( daoUtil.getString( 2 ) );
            portlet.setValues( daoUtil.getString( 3 ) );
        }

        daoUtil.free(  );

        return portlet;
    }

    /**
     * Update the record in the table
     *
     * @param portlet The reference of the portlet
     */
    public void store( Portlet portlet )
    {
        DbPagePortlet p = (DbPagePortlet) portlet;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setInt( 1, p.getId(  ) );
        daoUtil.setString( 2, p.getDbPageName(  ) );
        daoUtil.setString( 3, p.getDbValues(  ) );
        daoUtil.setInt( 4, p.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
    * Checks whether the dbPage is registered in a Portlet
    *
    * @param nDbPageId The identifier of the DbPage
    * @return The boolean result
    */
    public boolean checkNoDbPagePortlet( int nDbPageId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK );
        daoUtil.setInt( 1, nDbPageId );
        daoUtil.executeQuery(  );

        boolean bResult = false;

        if ( daoUtil.next(  ) )
        {
            bResult = true;
        }

        daoUtil.free(  );

        return bResult;
    }
}
