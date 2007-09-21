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
 * This class represents the business object ReportingMonthly
 */
public class ReportingMonthly
{
    // Variables declarations 
    private int _nIdMonthly;
    private int _nIdProject;
    private int _nIdPeriod;
    private String _strNamePeriod;
    private String _strFact;
    private String _strEvent;
    private Date _dateDateModification;
    private int _nTendency;
    private int _nRisk;

    /**
     * Returns the IdMonthly
     * @return The IdMonthly
     */
    public int getIdMonthly(  )
    {
        return _nIdMonthly;
    }

    /**
     * Sets the IdMonthly
     * @param nIdMonthly The IdMonthly
     */
    public void setIdMonthly( int nIdMonthly )
    {
        _nIdMonthly = nIdMonthly;
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
     * Returns the IdPeriod
     * @return The IdPeriod
     */
    public int getIdPeriod(  )
    {
        return _nIdPeriod;
    }

    /**
     * Sets the IdPeriod
     * @param nIdPeriod The IdPeriod
     */
    public void setIdPeriod( int nIdPeriod )
    {
        _nIdPeriod = nIdPeriod;
    }

    /**
     * Returns the Fact
     * @return The Fact
     */
    public String getNamePeriod(  )
    {
        return _strNamePeriod;
    }

    /**
     * Sets the name period
     * @param strNamePeriod The new name period
     */
    public void setNamePeriod( String strNamePeriod )
    {
        _strNamePeriod = strNamePeriod;
    }

    /**
     * Returns the Fact
     * @return The Fact
     */
    public String getFact(  )
    {
        return _strFact;
    }

    /**
     * Sets the Fact
     * @param strFact The Fact
     */
    public void setFact( String strFact )
    {
        _strFact = strFact;
    }

    /**
     * Returns the Evenement
     * @return The Evenement
     */
    public String getEvent(  )
    {
        return _strEvent;
    }

    /**
     * Sets the event
     * @param strEvent The Event
     */
    public void setEvent( String strEvent )
    {
        _strEvent = strEvent;
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
     * @return The risk
     */
    public int getRisk(  )
    {
        return _nRisk;
    }

    /**
     * Sets the Risk
     * @param nRisk The risk
     */
    public void setRisk( int nRisk )
    {
        _nRisk = nRisk;
    }

    /**
     * Returns the DateModification
     * @return the DateModification
     */
    public Date getDateModification(  )
    {
        return _dateDateModification;
    }

    /**
     * Sets the DateModification
     * @param dateModification the DateModification
     */
    public void setDateModification( Date dateModification )
    {
        _dateDateModification = dateModification;
    }
}
