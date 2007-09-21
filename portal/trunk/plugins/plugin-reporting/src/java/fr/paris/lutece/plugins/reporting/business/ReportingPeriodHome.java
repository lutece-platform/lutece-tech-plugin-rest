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
 * This class provides instances management methods (create, find, ...) for ReportingPeriod objects
 */
public final class ReportingPeriodHome
{
    // Static variable pointed at the DAO instance
    private static IReportingPeriodDAO _dao = (IReportingPeriodDAO) SpringContextService.getPluginBean( "reporting",
            "reportingPeriodDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ReportingPeriodHome(  )
    {
    }

    /**
     * Creation of an instance of reportingPeriod
     *
     * @param reportingPeriod The instance of the ReportingPeriod which contains the informations to store
     * @param plugin the Plugin
     * @return The  instance of reportingPeriod which has been created with its primary key.
     */
    public static ReportingPeriod create( ReportingPeriod reportingPeriod, Plugin plugin )
    {
        _dao.insert( reportingPeriod, plugin );

        return reportingPeriod;
    }

    /**
     * Update of the reportingPeriod which is specified in parameter
     *
     * @param reportingPeriod The instance of the ReportingPeriod which contains the data to store
     * @param plugin the Plugin
     * @return The instance of the  reportingPeriod which has been updated
     */
    public static ReportingPeriod update( ReportingPeriod reportingPeriod, Plugin plugin )
    {
        _dao.store( reportingPeriod, plugin );

        return reportingPeriod;
    }

    /**
     * Remove the reportingPeriod whose identifier is specified in parameter
     *
     * @param nReportingPeriodId The reportingPeriod Id
     * @param plugin the Plugin
     */
    public static void remove( int nReportingPeriodId, Plugin plugin )
    {
        _dao.delete( nReportingPeriodId, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a reportingPeriod whose identifier is specified in parameter
     *
     * @param nKey The reportingPeriod primary key
     * @param plugin the Plugin
     * @return an instance of ReportingPeriod
     */
    public static ReportingPeriod findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Loads the data of all the reportingPeriods and returns them in form of a collection
     *
     * @param plugin the Plugin
     * @return the collection which contains the data of all the reportingPeriods
     */
    public static List<ReportingPeriod> getReportingPeriodsList( Plugin plugin )
    {
        return _dao.selectReportingPeriodsList( plugin );
    }

    /**
     *
     * @param plugin the Plugin
     * @return return the current reportingPeriod
     */
    public static ReportingPeriod getReportingCurrentPeriod( Plugin plugin )
    {
        return _dao.getReportingCurrentPeriod( plugin );
    }
}
