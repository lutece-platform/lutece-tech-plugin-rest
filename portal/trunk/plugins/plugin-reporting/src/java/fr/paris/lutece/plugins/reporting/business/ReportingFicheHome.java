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

import java.util.Collection;
import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for ReportingFiche objects
 */
public final class ReportingFicheHome
{
    // Static variable pointed at the DAO instance
    private static IReportingFicheDAO _dao = (IReportingFicheDAO) SpringContextService.getPluginBean( "reporting",
            "reportingFicheDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ReportingFicheHome(  )
    {
    }

    /**
     * Creation of an instance of reportingFiche
     *
     * @param reportingFiche The instance of the ReportingFiche which contains the informations to store
     * @param plugin the Plugin
     * @return The  instance of reportingFiche which has been created with its primary key.
     */
    public static ReportingFiche create( ReportingFiche reportingFiche, Plugin plugin )
    {
        _dao.insert( reportingFiche, plugin );

        return reportingFiche;
    }

    /**
     * Update of the reportingFiche which is specified in parameter
     *
     * @param reportingFiche The instance of the ReportingFiche which contains the data to store
     * @param plugin the Plugin
     * @return The instance of the  reportingFiche which has been updated
     */
    public static ReportingFiche update( ReportingFiche reportingFiche, Plugin plugin )
    {
        _dao.store( reportingFiche, plugin );

        return reportingFiche;
    }

    /**
     * Remove the reportingFiche whose identifier is specified in parameter
     *
     * @param nReportingFicheId The reportingFiche Id
     * @param plugin the Plugin
     */
    public static void remove( int nReportingFicheId, Plugin plugin )
    {
        _dao.delete( nReportingFicheId, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a reportingFiche whose identifier is specified in parameter
     *
     * @param nKey The reportingFiche primary key
     * @param plugin the Plugin
     * @return an instance of ReportingFiche
     */
    public static ReportingFiche findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Loads the data of all the reportingFiches and returns them in form of a collection
     *
     * @param plugin the Plugin
     * @return the collection which contains the data of all the reportingFiches
     */
    public static Collection<ReportingFiche> getReportingFichesList( Plugin plugin )
    {
        return _dao.selectReportingFichesList( plugin );
    }

    /**
     *
     * @param nIdProject The reportingProject primary key
     * @param plugin the Plugin
     * @return the last reportingFiche of project nIdProject
     */
    public static ReportingFiche getLastUpdateOfProject( int nIdProject, Plugin plugin )
    {
        return _dao.getLastUpdateOfProject( nIdProject, plugin );
    }

    /**
     *
     * @param nIdProject The reportingProject primary key
     * @param plugin the Plugin
     * @return the collection which contains the data of all the reportingFiches of project nIdProject
     */
    public static List<ReportingFiche> getAllReportingFicheOfProject( int nIdProject, Plugin plugin )
    {
        return _dao.getAllReportingFicheOfProject( nIdProject, plugin );
    }
}
