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
import java.util.List;


/**
 * This class provides Data Access methods for ReportingProject objects
 */
public final class ReportingProjectDAO implements IReportingProjectDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_project ) FROM reporting_project";
    private static final String SQL_QUERY_SELECT = "SELECT id_project, name, id_catia, project_manager, follow_up, date_creation, workgroup_key FROM reporting_project WHERE id_project = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO reporting_project ( id_project, name, id_catia, project_manager, follow_up, date_creation, workgroup_key ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM reporting_project WHERE id_project = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE reporting_project SET id_project = ?, name = ?, id_catia = ?, project_manager = ?, follow_up = ?, workgroup_key = ? WHERE id_project = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_project, name, id_catia, project_manager, follow_up, date_creation, workgroup_key FROM reporting_project ORDER BY name";
    private static final String SQL_QUERY_SELECTALL_ORDER_BY_WORKGROUP = "SELECT id_project, name, id_catia, project_manager, follow_up, date_creation, workgroup_key FROM reporting_project ORDER BY workgroup_key";
    private static final String SQL_QUERY_SELECT_BY_WORKGROUP = "SELECT id_project, name, id_catia, project_manager, follow_up, date_creation, workgroup_key FROM reporting_project WHERE workgroup_key= ?";

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
     * @param reportingProject instance of the ReportingProject object to insert
     * @param plugin the plugin
     */
    public void insert( ReportingProject reportingProject, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        reportingProject.setIdProject( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, reportingProject.getIdProject(  ) );
        daoUtil.setString( 2, reportingProject.getName(  ) );
        daoUtil.setInt( 3, reportingProject.getIdCatia(  ) );
        daoUtil.setString( 4, reportingProject.getProjectManager(  ) );
        daoUtil.setBoolean( 5, reportingProject.getFollowUp(  ) );
        daoUtil.setDate( 6, new java.sql.Date( new java.util.Date(  ).getTime(  ) ) );
        daoUtil.setString( 7, reportingProject.getWorkgroupKey(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of the reportingProject from the table
     *
     * @param nId The identifier of the reportingProject
     * @param plugin the plugin
     * @return the instance of the ReportingProject
     */
    public ReportingProject load( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        ReportingProject reportingProject = null;

        if ( daoUtil.next(  ) )
        {
            reportingProject = new ReportingProject(  );

            reportingProject.setIdProject( daoUtil.getInt( 1 ) );
            reportingProject.setName( daoUtil.getString( 2 ) );
            reportingProject.setIdCatia( daoUtil.getInt( 3 ) );
            reportingProject.setProjectManager( daoUtil.getString( 4 ) );
            reportingProject.setFollowUp( daoUtil.getBoolean( 5 ) );
            reportingProject.setDateCreation( daoUtil.getDate( 6 ) );
            reportingProject.setWorkgroupKey( daoUtil.getString( 7 ) );
        }

        daoUtil.free(  );

        return reportingProject;
    }

    /**
     * Delete a record from the table
     *
     * @param nReportingProjectId The identifier of the reportingProject
     * @param plugin the plugin
     */
    public void delete( int nReportingProjectId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nReportingProjectId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     *
     * @param reportingProject The reference of the reportingProject
     * @param plugin the plugin
     */
    public void store( ReportingProject reportingProject, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, reportingProject.getIdProject(  ) );
        daoUtil.setString( 2, reportingProject.getName(  ) );
        daoUtil.setInt( 3, reportingProject.getIdCatia(  ) );
        daoUtil.setString( 4, reportingProject.getProjectManager(  ) );
        daoUtil.setBoolean( 5, reportingProject.getFollowUp(  ) );
        daoUtil.setString( 6, reportingProject.getWorkgroupKey(  ) );
        daoUtil.setInt( 7, reportingProject.getIdProject(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of all the reportingProjects and returns them in form of a collection
     *
     * @param plugin the plugin
     * @return The Collection which contains the data of all the reportingProjects
     */
    public List<ReportingProject> selectReportingProjectsList( Plugin plugin )
    {
        List<ReportingProject> reportingProjectList = new ArrayList<ReportingProject>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            ReportingProject reportingProject = new ReportingProject(  );

            reportingProject.setIdProject( daoUtil.getInt( 1 ) );
            reportingProject.setName( daoUtil.getString( 2 ) );
            reportingProject.setIdCatia( daoUtil.getInt( 3 ) );
            reportingProject.setProjectManager( daoUtil.getString( 4 ) );
            reportingProject.setFollowUp( daoUtil.getBoolean( 5 ) );
            reportingProject.setDateCreation( daoUtil.getDate( 6 ) );
            reportingProject.setWorkgroupKey( daoUtil.getString( 7 ) );

            reportingProjectList.add( reportingProject );
        }

        daoUtil.free(  );

        return reportingProjectList;
    }

    /**
     * Loads the data of all the reportingProjects and returns them With Last ReportingFiche
     * in form of a collection
     *
     * @param plugin the Plugin
     * @return the collection which contains the data of all the reportingProjects
     */
    public List<ReportingProject> selectReportingProjectsListWithLastReportingFiche( Plugin plugin )
    {
        List<ReportingProject> reportingProjectList = new ArrayList<ReportingProject>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ORDER_BY_WORKGROUP, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            ReportingFiche lastReportingFiche = ReportingFicheHome.getLastUpdateOfProject( daoUtil.getInt( 1 ), plugin );

            if ( lastReportingFiche != null )
            {
                ReportingProject reportingProject = new ReportingProject(  );

                reportingProject.setIdProject( daoUtil.getInt( 1 ) );
                reportingProject.setName( daoUtil.getString( 2 ) );
                reportingProject.setIdCatia( daoUtil.getInt( 3 ) );
                reportingProject.setProjectManager( daoUtil.getString( 4 ) );
                reportingProject.setFollowUp( daoUtil.getBoolean( 5 ) );
                reportingProject.setDateCreation( daoUtil.getDate( 6 ) );
                reportingProject.setWorkgroupKey( daoUtil.getString( 7 ) );

                if ( lastReportingFiche.getCurrentTasks(  ) != null )
                {
                    String strCurrentSpots = ReportingUtils.convertNewLineToHtmlTag( lastReportingFiche.getCurrentTasks(  ) );
                    lastReportingFiche.setCurrentTasks( strCurrentSpots );
                }

                if ( lastReportingFiche.getCompletedTasks(  ) != null )
                {
                    String strCompletedTasks = ReportingUtils.convertNewLineToHtmlTag( lastReportingFiche.getCompletedTasks(  ) );
                    lastReportingFiche.setCompletedTasks( strCompletedTasks );
                }

                if ( lastReportingFiche.getComingTasks(  ) != null )
                {
                    String strComingTasks = ReportingUtils.convertNewLineToHtmlTag( lastReportingFiche.getComingTasks(  ) );
                    lastReportingFiche.setComingTasks( strComingTasks );
                }

                reportingProject.setLastReportingFiche( lastReportingFiche );

                reportingProjectList.add( reportingProject );
            }
        }

        daoUtil.free(  );

        return reportingProjectList;
    }

    /**
     * Loads the data of all the reportingProjects and returns them With Last ReportingMonthly
     * in form of a collection
     *
     * @param plugin the Plugin
     * @return the collection which contains the data of all the reportingProjects
     */
    public List<ReportingProject> selectReportingProjectsListWithLastReportingMontly( Plugin plugin )
    {
        List<ReportingProject> reportingProjectList = new ArrayList<ReportingProject>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ORDER_BY_WORKGROUP, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            ReportingPeriod lastReportingPeriod = ReportingPeriodHome.getReportingCurrentPeriod( plugin );
            ReportingMonthly lastReportingMonthly = ReportingMonthlyHome.getLastReportingMonthlyOfProject( daoUtil.getInt( 
                        1 ), lastReportingPeriod.getIdPeriod(  ), plugin );

            if ( lastReportingMonthly != null )
            {
                ReportingProject reportingProject = new ReportingProject(  );

                reportingProject.setIdProject( daoUtil.getInt( 1 ) );
                reportingProject.setName( daoUtil.getString( 2 ) );
                reportingProject.setIdCatia( daoUtil.getInt( 3 ) );
                reportingProject.setProjectManager( daoUtil.getString( 4 ) );
                reportingProject.setFollowUp( daoUtil.getBoolean( 5 ) );
                reportingProject.setDateCreation( daoUtil.getDate( 6 ) );
                reportingProject.setWorkgroupKey( daoUtil.getString( 7 ) );

                if ( lastReportingMonthly.getEvent(  ) != null )
                {
                    String strEvenement = ReportingUtils.convertNewLineToHtmlTag( lastReportingMonthly.getEvent(  ) );
                    lastReportingMonthly.setEvent( strEvenement );
                }

                if ( lastReportingMonthly.getFact(  ) != null )
                {
                    String strFact = ReportingUtils.convertNewLineToHtmlTag( lastReportingMonthly.getFact(  ) );
                    lastReportingMonthly.setFact( strFact );
                }

                reportingProject.setLastReportingMonthly( lastReportingMonthly );

                reportingProjectList.add( reportingProject );
            }
        }

        daoUtil.free(  );

        return reportingProjectList;
    }

    /**
     * Loads the data of all the reportingProjects and returns them With Last ReportingFiche
     * in form of a collection
     *
     * @param plugin the Plugin
     * @param strWorkgroup the workgroup
     * @return the collection which contains the data of all the reportingProjects
     */
    public List<ReportingProject> selectReportingProjectsListByWorkgroupWithLastReportingFiche( String strWorkgroup,
        Plugin plugin )
    {
        List<ReportingProject> reportingProjectList = new ArrayList<ReportingProject>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_WORKGROUP, plugin );
        daoUtil.setString( 1, strWorkgroup );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            ReportingFiche lastReportingFiche = ReportingFicheHome.getLastUpdateOfProject( daoUtil.getInt( 1 ), plugin );

            if ( lastReportingFiche != null )
            {
                ReportingProject reportingProject = new ReportingProject(  );

                reportingProject.setIdProject( daoUtil.getInt( 1 ) );
                reportingProject.setName( daoUtil.getString( 2 ) );
                reportingProject.setIdCatia( daoUtil.getInt( 3 ) );
                reportingProject.setProjectManager( daoUtil.getString( 4 ) );
                reportingProject.setFollowUp( daoUtil.getBoolean( 5 ) );
                reportingProject.setDateCreation( daoUtil.getDate( 6 ) );
                reportingProject.setWorkgroupKey( daoUtil.getString( 7 ) );

                if ( lastReportingFiche.getCurrentTasks(  ) != null )
                {
                    String currentTasks = ReportingUtils.convertNewLineToHtmlTag( lastReportingFiche.getCurrentTasks(  ) );
                    lastReportingFiche.setCurrentTasks( currentTasks );
                }

                if ( lastReportingFiche.getCompletedTasks(  ) != null )
                {
                    String completedTasks = ReportingUtils.convertNewLineToHtmlTag( lastReportingFiche.getCompletedTasks(  ) );
                    lastReportingFiche.setCompletedTasks( completedTasks );
                }

                if ( lastReportingFiche.getComingTasks(  ) != null )
                {
                    String comingTasks = ReportingUtils.convertNewLineToHtmlTag( lastReportingFiche.getComingTasks(  ) );
                    lastReportingFiche.setComingTasks( comingTasks );
                }

                reportingProject.setLastReportingFiche( lastReportingFiche );

                reportingProjectList.add( reportingProject );
            }
        }

        daoUtil.free(  );

        return reportingProjectList;
    }
}
