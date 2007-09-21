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
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for ReportingProject objects
 */
public final class ReportingProjectHome
{
    // Static variable pointed at the DAO instance
    private static IReportingProjectDAO _dao = (IReportingProjectDAO) SpringContextService.getPluginBean( "reporting",
            "reportingProjectDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ReportingProjectHome(  )
    {
    }

    /**
     * Creation of an instance of reportingProject
     *
     * @param reportingProject The instance of the ReportingProject which contains the informations to store
     * @param plugin the Plugin
     * @return The  instance of reportingProject which has been created with its primary key.
     */
    public static ReportingProject create( ReportingProject reportingProject, Plugin plugin )
    {
        _dao.insert( reportingProject, plugin );

        return reportingProject;
    }

    /**
     * Update of the reportingProject which is specified in parameter
     *
     * @param reportingProject The instance of the ReportingProject which contains the data to store
     * @param plugin the Plugin
     * @return The instance of the  reportingProject which has been updated
     */
    public static ReportingProject update( ReportingProject reportingProject, Plugin plugin )
    {
        _dao.store( reportingProject, plugin );

        return reportingProject;
    }

    /**
     * Remove the reportingProject whose identifier is specified in parameter
     *
     * @param nReportingProjectId The reportingProject Id
     * @param plugin the Plugin
     */
    public static void remove( int nReportingProjectId, Plugin plugin )
    {
        _dao.delete( nReportingProjectId, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a reportingProject whose identifier is specified in parameter
     *
     * @param nKey The reportingProject primary key
     * @param plugin the Plugin
     * @return an instance of ReportingProject
     */
    public static ReportingProject findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Loads the data of all the reportingProjects and returns them in form of a collection
     *
     * @param plugin the Plugin
     * @return the collection which contains the data of all the reportingProjects
     */
    public static List<ReportingProject> getReportingProjectsList( Plugin plugin )
    {
        return _dao.selectReportingProjectsList( plugin );
    }

    /**
     * Loads the data of all the reportingProjects and returns them in form of a collection
     *
     * @param plugin the Plugin
     * @return the collection which contains the data of all the reportingProjects
     */
    public static List<ReportingProject> getReportingProjectsListWithLastReportingFiche( Plugin plugin )
    {
        return _dao.selectReportingProjectsListWithLastReportingFiche( plugin );
    }

    /**
     * Loads the data of all the reportingProjects and returns them in form of a collection
     *
     * @param plugin the Plugin
     * @return the collection which contains the data of all the reportingProjects
     */
    public static List<ReportingProject> getReportingProjectsListWithLastReportingMonthly( Plugin plugin )
    {
        return _dao.selectReportingProjectsListWithLastReportingMontly( plugin );
    }

    /**
     * Loads the data of all the reportingProjects and returns them in form of a collection
     *
     * @param strWorkgroup the workgroup
     * @param plugin the Plugin
     * @return the collection which contains the data of all the reportingProjects by workgroup
     */
    public static List<ReportingProject> getReportingProjectsListByWorkgroupWithLastReportingFiche( 
        String strWorkgroup, Plugin plugin )
    {
        return _dao.selectReportingProjectsListByWorkgroupWithLastReportingFiche( strWorkgroup, plugin );
    }

    /**
     *
     * @param nIdProject The reportingProject primary key
     * @param plugin the plugin
     * @return the collection which contains the data of all the ReportingFiche of project
     */
    public static List<ReportingFiche> getAllReportingFiche( int nIdProject, Plugin plugin )
    {
        return ReportingFicheHome.getAllReportingFicheOfProject( nIdProject, plugin );
    }

    /**
     *
     * @param nIdProject The reportingProject primary key
     * @param plugin the plugin
     * @return the collection which contains the data of all the ReportingMonthly of project
     */
    public static List<ReportingMonthly> getAllReportingMonthly( int nIdProject, Plugin plugin )
    {
        return ReportingMonthlyHome.getAllReportingMonthlyOfProject( nIdProject, plugin );
    }
}
