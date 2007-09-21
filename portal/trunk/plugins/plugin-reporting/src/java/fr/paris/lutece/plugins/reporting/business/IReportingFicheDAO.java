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

import java.util.Collection;
import java.util.List;


/**
* IReportingFicheDAO Interface
*/
public interface IReportingFicheDAO
{
    /**
     * Insert a new record in the table.
     * @param reportingFiche instance of the ReportingFiche object to inssert
     * @param plugin the Plugin
     */
    void insert( ReportingFiche reportingFiche, Plugin plugin );

    /**
    * Update the record in the table
    *
    * @param reportingFiche the reference of the ReportingFiche
    * @param plugin the Plugin
    */
    void store( ReportingFiche reportingFiche, Plugin plugin );

    /**
     * Delete a record from the table
     *
     * @param nIdReportingFiche int identifier of the ReportingFiche to delete
     * @param plugin the Plugin
     */
    void delete( int nIdReportingFiche, Plugin plugin );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * load the data of the right from the table
     *
     * @param nKey The identifier of the reportingFiche
     * @param plugin the Plugin
     * @return The instance of the reportingFiche
     */
    ReportingFiche load( int nKey, Plugin plugin );

    /**
    * Loads the data of all the reportingFiches and returns them in form of a collection
    *
    * @param plugin the Plugin
    * @return the collection which contains the data of all the reportingFiches
    */
    Collection<ReportingFiche> selectReportingFichesList( Plugin plugin );

    /**
     * Loads the data of the all reportingFiches and return the last reportingFiche update
     * of the project nIdProject.
     *
     * @param nIdProject The identifier of the reportingProject
     * @param plugin the Plugin
     * @return the last update of project
     */
    ReportingFiche getLastUpdateOfProject( int nIdProject, Plugin plugin );

    /**
     *Loads the data of the all reportingFiches and return the all reportingFiche
     * of the project nIdProject.
     * @param nIdProject The identifier of the reportingProject
     * @param plugin the Plugin
     * @return all reporting fiche of project
     */
    List<ReportingFiche> getAllReportingFicheOfProject( int nIdProject, Plugin plugin );
}
