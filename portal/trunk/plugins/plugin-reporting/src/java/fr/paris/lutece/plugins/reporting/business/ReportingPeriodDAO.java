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
package fr.paris.lutece.plugins.reporting.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for ReportingPeriod objects
 */
public final class ReportingPeriodDAO implements IReportingPeriodDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_period ) FROM reporting_period";
    private static final String SQL_QUERY_SELECT = "SELECT id_period, name, current FROM reporting_period WHERE id_period = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO reporting_period ( id_period, name, current ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM reporting_period WHERE id_period = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE reporting_period SET id_period = ?, name = ?, current = ? WHERE id_period = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_period, name, current FROM reporting_period";
    private static final String SQL_QUERY_SELECT_PERIOD_CURRENT = "SELECT id_period, name, current FROM reporting_period WHERE current = 1";

    /**
     * Generates a new primary key
     *
     * @param plugin the plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
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
     *
     * @param reportingPeriod instance of the ReportingPeriod object to insert
     * @param plugin the plugin
     */
    public void insert( ReportingPeriod reportingPeriod, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        reportingPeriod.setIdPeriod( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, reportingPeriod.getIdPeriod(  ) );
        daoUtil.setString( 2, reportingPeriod.getName(  ) );
        daoUtil.setBoolean( 3, reportingPeriod.getCurrent(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of the reportingPeriod from the table
     *
     * @param nId The identifier of the reportingPeriod
     * @param plugin the plugin
     * @return the instance of the ReportingPeriod
     */
    public ReportingPeriod load( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        ReportingPeriod reportingPeriod = null;

        if ( daoUtil.next(  ) )
        {
            reportingPeriod = new ReportingPeriod(  );

            reportingPeriod.setIdPeriod( daoUtil.getInt( 1 ) );
            reportingPeriod.setName( daoUtil.getString( 2 ) );
            reportingPeriod.setCurrent( daoUtil.getBoolean( 3 ) );
        }

        daoUtil.free(  );

        return reportingPeriod;
    }

    /**
     * Delete a record from the table
     *
     * @param nReportingPeriodId The identifier of the reportingPeriod
     * @param plugin the plugin
     */
    public void delete( int nReportingPeriodId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nReportingPeriodId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     *
     * @param reportingPeriod The reference of the reportingPeriod
     * @param plugin the plugin
     */
    public void store( ReportingPeriod reportingPeriod, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, reportingPeriod.getIdPeriod(  ) );
        daoUtil.setString( 2, reportingPeriod.getName(  ) );
        daoUtil.setBoolean( 3, reportingPeriod.getCurrent(  ) );
        daoUtil.setInt( 4, reportingPeriod.getIdPeriod(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of all the reportingPeriods and returns them in form of a collection
     *
     * @param plugin the plugin
     * @return The Collection which contains the data of all the reportingPeriods
     */
    public List<ReportingPeriod> selectReportingPeriodsList( Plugin plugin )
    {
        List<ReportingPeriod> reportingPeriodList = new ArrayList<ReportingPeriod>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            ReportingPeriod reportingPeriod = new ReportingPeriod(  );

            reportingPeriod.setIdPeriod( daoUtil.getInt( 1 ) );
            reportingPeriod.setName( daoUtil.getString( 2 ) );
            reportingPeriod.setCurrent( daoUtil.getBoolean( 3 ) );

            reportingPeriodList.add( reportingPeriod );
        }

        daoUtil.free(  );

        return reportingPeriodList;
    }

    /**
     *Loads the data of all the reportingPeriods and return the current period
     *
     * @param plugin the Plugin
     * @return the current period
     */
    public ReportingPeriod getReportingCurrentPeriod( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PERIOD_CURRENT, plugin );
        daoUtil.executeQuery(  );

        ReportingPeriod reportingPeriod = null;

        if ( daoUtil.next(  ) )
        {
            reportingPeriod = new ReportingPeriod(  );

            reportingPeriod.setIdPeriod( daoUtil.getInt( 1 ) );
            reportingPeriod.setName( daoUtil.getString( 2 ) );
            reportingPeriod.setCurrent( daoUtil.getBoolean( 3 ) );
        }

        daoUtil.free(  );

        return reportingPeriod;
    }
}
