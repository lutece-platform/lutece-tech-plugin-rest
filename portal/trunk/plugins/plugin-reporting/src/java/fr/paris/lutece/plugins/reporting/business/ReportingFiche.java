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

import java.sql.Date;


/**
 * This class represents the business object ReportingFiche
 */
public class ReportingFiche
{
    // Variables declarations 
    private int _nIdFiche;
    private int _nIdProject;
    private String _strCurrentTasks;
    private String _strCompletedTasks;
    private String _strComingTasks;
    private int _nTendency;
    private int _nRisk;
    private Date _dateCreation;

    /**
     * Returns the IdFiche
     * @return The IdFiche
     */
    public int getIdFiche(  )
    {
        return _nIdFiche;
    }

    /**
     * Sets the IdFiche
     * @param nIdFiche The IdFiche
     */
    public void setIdFiche( int nIdFiche )
    {
        _nIdFiche = nIdFiche;
    }

    /**
     * Returns the IdProject
     * @return The IdProject
     */
    public int getIdProject(  )
    {
        return _nIdProject;
    }

    /**
     * Sets the IdProject
     * @param nIdProject The IdProject
     */
    public void setIdProject( int nIdProject )
    {
        _nIdProject = nIdProject;
    }

    /**
     * Returns the Current tasks
     * @return The Current tasks
     */
    public String getCurrentTasks(  )
    {
        return _strCurrentTasks;
    }

    /**
     * Sets the Current tasks
     * @param strCurrentTasks The Current tasks
     */
    public void setCurrentTasks( String strCurrentTasks )
    {
        _strCurrentTasks = strCurrentTasks;
    }

    /**
     * Returns the completed Tasks
     * @return The completed Tasks
     */
    public String getCompletedTasks(  )
    {
        return _strCompletedTasks;
    }

    /**
     * Sets the completed Tasks
     * @param strCompletedTasks The completed Tasks
     */
    public void setCompletedTasks( String strCompletedTasks )
    {
        _strCompletedTasks = strCompletedTasks;
    }

    /**
     * Returns the coming tasks
     * @return The coming tasks
     */
    public String getComingTasks(  )
    {
        return _strComingTasks;
    }

    /**
     * Sets the coming tasks
     * @param strComingTasks the coming tasks
     */
    public void setComingTasks( String strComingTasks )
    {
        _strComingTasks = strComingTasks;
    }

    /**
     * Returns the Tendency
     * @return The Tendency
     */
    public int getTendency(  )
    {
        return _nTendency;
    }

    /**
     * Sets the Tendency
     * @param nTendency The Tendency
     */
    public void setTendency( int nTendency )
    {
        _nTendency = nTendency;
    }

    /**
     * Returns the Risk
     * @return The Risk
     */
    public int getRisk(  )
    {
        return _nRisk;
    }

    /**
     * Sets the Risk
     * @param nRisk The Risk
     */
    public void setRisk( int nRisk )
    {
        _nRisk = nRisk;
    }

    /**
     * Returns the DateCreation
     * @return The DateCreation
     */
    public Date getDateCreation(  )
    {
        return _dateCreation;
    }

    /**
     * Sets the DateCreation
     * @param dateDateCreation The DateCreation
     */
    public void setDateCreation( Date dateDateCreation )
    {
        _dateCreation = dateDateCreation;
    }
}
