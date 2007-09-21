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
 * This class provides Data Access methods for ReportingFiche objects
 */
public final class ReportingFicheDAO implements IReportingFicheDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_fiche ) FROM reporting_fiche";
    private static final String SQL_QUERY_SELECT = "SELECT id_fiche, id_project, current_tasks, completed_tasks, coming_tasks, tendency, risk, date_update FROM reporting_fiche WHERE id_fiche = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO reporting_fiche ( id_fiche, id_project, current_tasks, completed_tasks, coming_tasks, tendency, risk, date_update ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM reporting_fiche WHERE id_fiche = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE reporting_fiche SET id_fiche = ?, id_project = ?, current_tasks = ?, completed_tasks = ?, coming_tasks = ?, tendency = ?, risk = ?, date_update = ? WHERE id_fiche = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_fiche, id_project, current_tasks, completed_tasks, coming_tasks, tendency, risk, date_update FROM reporting_fiche";
    private static final String SQL_QUERY_SELECTALL_OF_PROJECT = "SELECT id_fiche, id_project, current_tasks, completed_tasks, coming_tasks, tendency, risk, date_update FROM reporting_fiche WHERE id_project = ? ORDER BY date_update DESC";

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
     * @param reportingFiche instance of the ReportingFiche object to insert
     * @param plugin the plugin
     */
    public void insert( ReportingFiche reportingFiche, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        reportingFiche.setIdFiche( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, reportingFiche.getIdFiche(  ) );
        daoUtil.setInt( 2, reportingFiche.getIdProject(  ) );
        daoUtil.setString( 3, reportingFiche.getCurrentTasks(  ) );
        daoUtil.setString( 4, reportingFiche.getCompletedTasks(  ) );
        daoUtil.setString( 5, reportingFiche.getComingTasks(  ) );
        daoUtil.setInt( 6, reportingFiche.getTendency(  ) );
        daoUtil.setInt( 7, reportingFiche.getRisk(  ) );
        daoUtil.setTimestamp( 8, new java.sql.Timestamp( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of the reportingFiche from the table
     *
     * @param nId The identifier of the reportingFiche
     * @param plugin the plugin
     * @return the instance of the ReportingFiche
     */
    public ReportingFiche load( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        ReportingFiche reportingFiche = null;

        if ( daoUtil.next(  ) )
        {
            reportingFiche = new ReportingFiche(  );

            reportingFiche.setIdFiche( daoUtil.getInt( 1 ) );
            reportingFiche.setIdProject( daoUtil.getInt( 2 ) );
            reportingFiche.setCurrentTasks( daoUtil.getString( 3 ) );
            reportingFiche.setCompletedTasks( daoUtil.getString( 4 ) );
            reportingFiche.setComingTasks( daoUtil.getString( 5 ) );
            reportingFiche.setTendency( daoUtil.getInt( 6 ) );
            reportingFiche.setRisk( daoUtil.getInt( 7 ) );
            reportingFiche.setDateCreation( daoUtil.getDate( 8 ) );
        }

        daoUtil.free(  );

        return reportingFiche;
    }

    /**
     * Delete a record from the table
     *
     * @param nReportingFicheId The identifier of the reportingFiche
     * @param plugin the plugin
     */
    public void delete( int nReportingFicheId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nReportingFicheId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     *
     * @param reportingFiche The reference of the reportingFiche
     * @param plugin the plugin
     */
    public void store( ReportingFiche reportingFiche, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, reportingFiche.getIdFiche(  ) );
        daoUtil.setInt( 2, reportingFiche.getIdProject(  ) );
        daoUtil.setString( 3, reportingFiche.getCurrentTasks(  ) );
        daoUtil.setString( 4, reportingFiche.getCompletedTasks(  ) );
        daoUtil.setString( 5, reportingFiche.getComingTasks(  ) );
        daoUtil.setInt( 6, reportingFiche.getTendency(  ) );
        daoUtil.setInt( 7, reportingFiche.getRisk(  ) );
        daoUtil.setDate( 8, reportingFiche.getDateCreation(  ) );
        daoUtil.setInt( 9, reportingFiche.getIdFiche(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of all the reportingFiches and returns them in form of a collection
     *
     * @param plugin the plugin
     * @return The Collection which contains the data of all the reportingFiches
     */
    public Collection<ReportingFiche> selectReportingFichesList( Plugin plugin )
    {
        Collection<ReportingFiche> reportingFicheList = new ArrayList<ReportingFiche>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            ReportingFiche reportingFiche = new ReportingFiche(  );

            reportingFiche.setIdFiche( daoUtil.getInt( 1 ) );
            reportingFiche.setIdProject( daoUtil.getInt( 2 ) );
            reportingFiche.setCurrentTasks( daoUtil.getString( 3 ) );
            reportingFiche.setCompletedTasks( daoUtil.getString( 4 ) );
            reportingFiche.setComingTasks( daoUtil.getString( 5 ) );
            reportingFiche.setTendency( daoUtil.getInt( 6 ) );
            reportingFiche.setRisk( daoUtil.getInt( 7 ) );
            reportingFiche.setDateCreation( daoUtil.getDate( 8 ) );

            reportingFicheList.add( reportingFiche );
        }

        daoUtil.free(  );

        return reportingFicheList;
    }

    /**
     * Loads the data of the all reportingFiches and return the last reportingFiche update
     * of the project nIdProject.
     *
     * @param nIdProject the reporting project primary key
     * @param plugin the Plugin
     * @return the Last Update Of roject
     */
    public ReportingFiche getLastUpdateOfProject( int nIdProject, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_OF_PROJECT, plugin );
        daoUtil.setInt( 1, nIdProject );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return null;
        }
        else
        {
            ReportingFiche reportingFiche = new ReportingFiche(  );

            String strCurrentTasksWithBr = ReportingUtils.removeHtmlTagBr( daoUtil.getString( 3 ) );
            String strComletedTasksWithBr = ReportingUtils.removeHtmlTagBr( daoUtil.getString( 4 ) );
            String strComingTasksWithBr = ReportingUtils.removeHtmlTagBr( daoUtil.getString( 5 ) );

            reportingFiche.setIdFiche( daoUtil.getInt( 1 ) );
            reportingFiche.setIdProject( daoUtil.getInt( 2 ) );
            reportingFiche.setCurrentTasks( strCurrentTasksWithBr );
            reportingFiche.setCompletedTasks( strComletedTasksWithBr );
            reportingFiche.setComingTasks( strComingTasksWithBr );
            reportingFiche.setTendency( daoUtil.getInt( 6 ) );
            reportingFiche.setRisk( daoUtil.getInt( 7 ) );
            reportingFiche.setDateCreation( daoUtil.getDate( 8 ) );

            daoUtil.free(  );

            return reportingFiche;
        }
    }

    /**
     *Loads the data of the all reportingFiches and return the all reportingFiche
     * of the project nIdProject.
     *
     * @param nIdProject the reporting project primary key
     * @param plugin the Plugin
     * @return All Reporting Fiche Of Project
     */
    public List<ReportingFiche> getAllReportingFicheOfProject( int nIdProject, Plugin plugin )
    {
        List<ReportingFiche> reportingFicheList = new ArrayList<ReportingFiche>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_OF_PROJECT, plugin );
        daoUtil.setInt( 1, nIdProject );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            ReportingFiche reportingFiche = new ReportingFiche(  );

            reportingFiche.setIdFiche( daoUtil.getInt( 1 ) );
            reportingFiche.setIdProject( daoUtil.getInt( 2 ) );
            reportingFiche.setCurrentTasks( daoUtil.getString( 3 ) );
            reportingFiche.setCompletedTasks( daoUtil.getString( 4 ) );
            reportingFiche.setComingTasks( daoUtil.getString( 5 ) );
            reportingFiche.setTendency( daoUtil.getInt( 6 ) );
            reportingFiche.setRisk( daoUtil.getInt( 7 ) );
            reportingFiche.setDateCreation( daoUtil.getDate( 8 ) );

            reportingFicheList.add( reportingFiche );
        }

        daoUtil.free(  );

        return reportingFicheList;
    }
}
