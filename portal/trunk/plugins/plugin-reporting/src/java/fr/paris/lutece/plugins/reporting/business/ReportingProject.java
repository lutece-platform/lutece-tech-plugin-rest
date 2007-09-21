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

import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupResource;

import java.sql.Date;


/**
 * This class represents the business object ReportingProject
 */
public class ReportingProject implements AdminWorkgroupResource
{
    // Variables declarations 
    private int _nIdProject;
    private String _strName;
    private int _nIdCatia;
    private String _strProjectManager;
    private boolean _bFollowUp;
    private Date _dateDateCreation;
    private String _strWorkgroupKey;
    private ReportingFiche _lastReportingFiche;
    private ReportingMonthly _lastReportingMonthly;

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
     * Returns the Name
     * @return The Name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the Name
     * @param strName The Name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the ProjectManager
     * @return The ProjectManager
     */
    public String getProjectManager(  )
    {
        return _strProjectManager;
    }

    /**
     * Sets the ProjectManager
     * @param strProjectManager The ProjectManager
     */
    public void setProjectManager( String strProjectManager )
    {
        _strProjectManager = strProjectManager;
    }

    /**
     * Returns the FollowUp
     * @return The FollowUp
     */
    public boolean getFollowUp(  )
    {
        return _bFollowUp;
    }

    /**
     * Sets the FollowUp
     * @param bFollowUp The FollowUp
     */
    public void setFollowUp( boolean bFollowUp )
    {
        _bFollowUp = bFollowUp;
    }

    /**
     * Returns the DateCreation
     * @return The DateCreation
     */
    public Date getDateCreation(  )
    {
        return _dateDateCreation;
    }

    /**
     * Sets the DateCreation
     * @param dateDateCreation The DateCreation
     */
    protected void setDateCreation( Date dateDateCreation )
    {
        _dateDateCreation = dateDateCreation;
    }

    /**
     * Returns the WorkgroupKey
     * @return The WorkgroupKey
     */
    public String getWorkgroupKey(  )
    {
        return _strWorkgroupKey;
    }

    /**
     * for methods AdminWorkgroupService.getAuthorizedCollection(Collection<? extends AdminWorkgroupResource> arg0, AdminUser arg1)
     *
     * Returns the Workgroup
     * @return The Workgroup
     */
    public String getWorkgroup(  )
    {
        return _strWorkgroupKey;
    }

    /**
     * Sets the WorkgroupKey
     * @param strWorkgroupKey The WorkgroupKey
     */
    public void setWorkgroupKey( String strWorkgroupKey )
    {
        _strWorkgroupKey = strWorkgroupKey;
    }

    /**
     * Return The last reporting fiche of project instance
     *
     * @param lastReportingFiche The last ReportingFiche
     */
    public void setLastReportingFiche( ReportingFiche lastReportingFiche )
    {
        _lastReportingFiche = lastReportingFiche;
    }

    /**
     *
     * @return The last ReportingFiche
     */
    public ReportingFiche getLastReportingFiche(  )
    {
        return _lastReportingFiche;
    }

    /**
     * Returns idCatia
     * @return The idCatia
     */
    public int getIdCatia(  )
    {
        return _nIdCatia;
    }

    /**
     * Sets theidCatia
     * @param nIdCatia The idCatia
     */
    public void setIdCatia( int nIdCatia )
    {
        _nIdCatia = nIdCatia;
    }

    /**
     * Returns The last reporting Monthly
     * @return The last reporting Monthly
     */
    public ReportingMonthly getLastReportingMonthly(  )
    {
        return _lastReportingMonthly;
    }

    /**
     * Sets The last reporting Monthly
     * @param lastReportingMonthly The last reporting Monthly
     */
    public void setLastReportingMonthly( ReportingMonthly lastReportingMonthly )
    {
        _lastReportingMonthly = lastReportingMonthly;
    }
}
