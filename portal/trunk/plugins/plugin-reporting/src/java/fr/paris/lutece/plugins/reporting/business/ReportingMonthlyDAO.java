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

import fr.paris.lutece.plugins.reporting.utils.ReportingUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * This class provides Data Access methods for ReportingMonthly objects
 */
public final class ReportingMonthlyDAO implements IReportingMonthlyDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_monthly ) FROM reporting_monthly";
    private static final String SQL_QUERY_SELECT = "SELECT id_monthly, id_project, id_period, fact, event, date_modification, tendency, risk FROM reporting_monthly WHERE id_monthly = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO reporting_monthly ( id_monthly, id_project, id_period, fact, event, date_modification, tendency, risk ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM reporting_monthly WHERE id_monthly = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE reporting_monthly SET id_monthly = ?, id_project = ?, id_period = ?, fact = ?, event = ?, date_modification = ?, tendency = ?, risk = ? WHERE id_monthly = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_monthly, id_project, id_period, fact, event, tendency, risk FROM reporting_monthly";
    private static final String SQL_QUERY_SELECT_ALL_OF_PROJECT = "SELECT id_monthly, id_project, id_period, fact, event, date_modification, tendency, risk FROM reporting_monthly WHERE id_project= ? ORDER BY id_period DESC";
    private static final String SQL_QUERY_SELECT_LAST_OF_PROJECT = "SELECT id_monthly, id_project, id_period, fact, event, date_modification, tendency, risk FROM reporting_monthly WHERE id_project= ? AND id_period= ?";

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
     * @param reportingMonthly instance of the ReportingMonthly object to insert
     * @param plugin the plugin
     */
    public void insert( ReportingMonthly reportingMonthly, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        reportingMonthly.setIdMonthly( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, reportingMonthly.getIdMonthly(  ) );
        daoUtil.setInt( 2, reportingMonthly.getIdProject(  ) );
        daoUtil.setInt( 3, reportingMonthly.getIdPeriod(  ) );
        daoUtil.setString( 4, reportingMonthly.getFact(  ) );
        daoUtil.setString( 5, reportingMonthly.getEvent(  ) );
        daoUtil.setDate( 6, new java.sql.Date( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setInt( 7, reportingMonthly.getTendency(  ) );
        daoUtil.setInt( 8, reportingMonthly.getRisk(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of the reportingMonthly from the table
     *
     * @param nId The identifier of the reportingMonthly
     * @param plugin the plugin
     * @return the instance of the ReportingMonthly
     */
    public ReportingMonthly load( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        ReportingMonthly reportingMonthly = null;

        if ( daoUtil.next(  ) )
        {
            reportingMonthly = new ReportingMonthly(  );

            reportingMonthly.setIdMonthly( daoUtil.getInt( 1 ) );
            reportingMonthly.setIdProject( daoUtil.getInt( 2 ) );
            reportingMonthly.setIdPeriod( daoUtil.getInt( 3 ) );
            reportingMonthly.setFact( daoUtil.getString( 4 ) );
            reportingMonthly.setEvent( daoUtil.getString( 5 ) );
            reportingMonthly.setDateModification( daoUtil.getDate( 6 ) );
            reportingMonthly.setTendency( daoUtil.getInt( 7 ) );
            reportingMonthly.setRisk( daoUtil.getInt( 8 ) );
        }

        daoUtil.free(  );

        return reportingMonthly;
    }

    /**
     * Delete a record from the table
     *
     * @param nReportingMonthlyId The identifier of the reportingMonthly
     * @param plugin the plugin
     */
    public void delete( int nReportingMonthlyId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nReportingMonthlyId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     *
     * @param reportingMonthly The reference of the reportingMonthly
     * @param plugin the plugin
     */
    public void store( ReportingMonthly reportingMonthly, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, reportingMonthly.getIdMonthly(  ) );
        daoUtil.setInt( 2, reportingMonthly.getIdProject(  ) );
        daoUtil.setInt( 3, reportingMonthly.getIdPeriod(  ) );
        daoUtil.setString( 4, reportingMonthly.getFact(  ) );
        daoUtil.setString( 5, reportingMonthly.getEvent(  ) );
        daoUtil.setDate( 6, new java.sql.Date( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setInt( 7, reportingMonthly.getTendency(  ) );
        daoUtil.setInt( 8, reportingMonthly.getRisk(  ) );
        daoUtil.setInt( 9, reportingMonthly.getIdMonthly(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of all the reportingMonthlys and returns them in form of a collection
     *
     * @param plugin the plugin
     * @return The Collection which contains the data of all the reportingMonthlys
     */
    public Collection<ReportingMonthly> selectReportingMonthlysList( Plugin plugin )
    {
        Collection<ReportingMonthly> reportingMonthlyList = new ArrayList<ReportingMonthly>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            ReportingMonthly reportingMonthly = new ReportingMonthly(  );

            reportingMonthly.setIdMonthly( daoUtil.getInt( 1 ) );
            reportingMonthly.setIdProject( daoUtil.getInt( 2 ) );
            reportingMonthly.setIdPeriod( daoUtil.getInt( 3 ) );
            reportingMonthly.setFact( daoUtil.getString( 4 ) );
            reportingMonthly.setEvent( daoUtil.getString( 5 ) );
            reportingMonthly.setDateModification( daoUtil.getDate( 6 ) );
            reportingMonthly.setTendency( daoUtil.getInt( 7 ) );
            reportingMonthly.setRisk( daoUtil.getInt( 8 ) );

            reportingMonthlyList.add( reportingMonthly );
        }

        daoUtil.free(  );

        return reportingMonthlyList;
    }

    /**
     *Loads the data of the all reporting Monthly and return all reporting Monthly
     * of the project nIdProject.
     * @param nIdProject the reporting project primary key
     * @param plugin the plugin
     * @return All Reporting Monthly Of Project
     */
    public List<ReportingMonthly> getAllReportingMonthlyOfProject( int nIdProject, Plugin plugin )
    {
        List<ReportingMonthly> reportingMonthlyList = new ArrayList<ReportingMonthly>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL_OF_PROJECT, plugin );
        daoUtil.setInt( 1, nIdProject );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            ReportingPeriod reportingPeriod = ReportingPeriodHome.findByPrimaryKey( daoUtil.getInt( 3 ), plugin );

            ReportingMonthly reportingMonthly = new ReportingMonthly(  );

            reportingMonthly.setIdMonthly( daoUtil.getInt( 1 ) );
            reportingMonthly.setIdProject( daoUtil.getInt( 2 ) );
            reportingMonthly.setIdPeriod( daoUtil.getInt( 3 ) );
            reportingMonthly.setFact( daoUtil.getString( 4 ) );
            reportingMonthly.setEvent( daoUtil.getString( 5 ) );
            reportingMonthly.setDateModification( daoUtil.getDate( 6 ) );
            reportingMonthly.setTendency( daoUtil.getInt( 7 ) );
            reportingMonthly.setRisk( daoUtil.getInt( 8 ) );
            reportingMonthly.setNamePeriod( reportingPeriod.getName(  ) );

            reportingMonthlyList.add( reportingMonthly );
        }

        daoUtil.free(  );

        return reportingMonthlyList;
    }

    /**
     *Loads the data of the all reporting Monthly and return the last reporting Monthly update
     * of the project nIdProject.
     * @param nIdProject the reporting project primary key
     * @param nIdPeriod the reporting period primary key
     * @param plugin the plugin
     * @return the Last Reporting Monthly Of Project
     */
    public ReportingMonthly getLastReportingMonthlyOfProject( int nIdProject, int nIdPeriod, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_LAST_OF_PROJECT, plugin );
        daoUtil.setInt( 1, nIdProject );
        daoUtil.setInt( 2, nIdPeriod );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return null;
        }
        else
        {
            String strFactWithBr = ReportingUtils.removeHtmlTagBr( daoUtil.getString( 4 ) );
            String strEventTasksWithBr = ReportingUtils.removeHtmlTagBr( daoUtil.getString( 5 ) );

            ReportingMonthly reportingMonthly = new ReportingMonthly(  );

            reportingMonthly.setIdMonthly( daoUtil.getInt( 1 ) );
            reportingMonthly.setIdProject( daoUtil.getInt( 2 ) );
            reportingMonthly.setIdPeriod( daoUtil.getInt( 3 ) );
            reportingMonthly.setFact( strFactWithBr );
            reportingMonthly.setEvent( strEventTasksWithBr );
            reportingMonthly.setDateModification( daoUtil.getDate( 6 ) );
            reportingMonthly.setTendency( daoUtil.getInt( 7 ) );
            reportingMonthly.setRisk( daoUtil.getInt( 8 ) );

            daoUtil.free(  );

            return reportingMonthly;
        }
    }
}
