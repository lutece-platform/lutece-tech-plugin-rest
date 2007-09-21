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
package fr.paris.lutece.plugins.searchstats.business;

/**
 * TimedRecord
 */
public class TimedRecord
{
    private int _nYear;
    private int _nMonth;
    private int _nDay;
    private int _nHour;
    /**
     * Returns the Year
     *
     * @return The Year
     */
    public int getYear()
    {
        return _nYear;
    }
    
    /**
     * Sets the Year
     *
     * @param nYear The Year
     */
    public void setYear( int nYear )
    {
        _nYear = nYear;
    }
    
    /**
     * Returns the Month
     *
     * @return The Month
     */
    public int getMonth()
    {
        return _nMonth;
    }
    
    /**
     * Sets the Month
     *
     * @param nMonth The Month
     */
    public void setMonth( int nMonth )
    {
        _nMonth = nMonth;
    }
    
    /**
     * Returns the Day
     *
     * @return The Day
     */
    public int getDay()
    {
        return _nDay;
    }
    
    /**
     * Sets the Day
     *
     * @param nDay The Day
     */
    public void setDay( int nDay )
    {
        _nDay = nDay;
    }
    
    /**
     * Returns the Hour
     *
     * @return The Hour
     */
    public int getHour()
    {
        return _nHour;
    }
    
    /**
     * Sets the Hour
     *
     * @param nHour The Hour
     */
    
    public void setHour( int nHour )
    {
        _nHour = nHour;
    }
    
    
}
