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


/**
 * This class represents the business object ReportingPeriod
 */
public class ReportingPeriod implements Comparable
{
    // Variables declarations 
    private int _nIdPeriod;
    private String _strName;
    private boolean _bCurrent;

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
     * Returns the status of the period
     * @return true if current period, false otherwise
     */
    public boolean getCurrent(  )
    {
        return _bCurrent;
    }

    /**
     * Sets the status of the period
     * @param bCurrent true if current period, false otherwise
     */
    public void setCurrent( boolean bCurrent )
    {
        _bCurrent = bCurrent;
    }

    /**
     * Compare two ReportingPeriod by nIdPeriod for sort a list of ReportingPeriod
     *
     * @param otherReportingPeriod an other reporting project.
     * @return -1 for ">" , 1 for "<", and 0 to "=".
     */
    public int compareTo( Object otherReportingPeriod )
    {
        int nIdPeriodOther = ( (ReportingPeriod) otherReportingPeriod ).getIdPeriod(  );
        int nIdPeriod = this.getIdPeriod(  );

        if ( nIdPeriodOther > nIdPeriod )
        {
            return -1;
        }
        else if ( nIdPeriodOther == nIdPeriod )
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }
}
