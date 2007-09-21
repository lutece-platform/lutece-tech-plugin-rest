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
package fr.paris.lutece.plugins.calendar.business;


/**
 *
 */
public class SimpleEvent implements Event
{
    // Variables declarations
    private int _nId;
    private String _strDate;
    private String _strTitle;
    private String _strLocation;
    private String _strEventClass;
    private String _strDescription;
    private String _strDateTimeStart;
    private String _strDateTimeEnd;
    private String _strCategories;
    private String _strStatus;
    private int _nPriority;
    private String _strUrl;

    /**
    * Returns the id of the event
    *
    * @return The id
    */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the Date
     *
     * @param nId The id of the event
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the Date
     *
     * @return The Date
     */
    public String getDate(  )
    {
        return _strDate;
    }

    /**
     * Sets the Date
     *
     * @param strDate The Date
     */
    public void setDate( String strDate )
    {
        _strDate = strDate;
    }

    /**
     * Returns the Title
     *
     * @return The Title
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * Sets the Title
     *
     * @param strTitle The Title
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Returns the Location
     *
     * @return The Location
     */
    public String getLocation(  )
    {
        return _strLocation;
    }

    /**
     * Sets the Location
     *
     * @param strLocation The Location
     */
    public void setLocation( String strLocation )
    {
        _strLocation = strLocation;
    }

    /**
     * Returns the EventClass
     *
     * @return The EventClass
     */
    public String getEventClass(  )
    {
        return _strEventClass;
    }

    /**
     * Sets the EventClass
     *
     * @param strEventClass The EventClass
     */
    public void setEventClass( String strEventClass )
    {
        _strEventClass = strEventClass;
    }

    /**
     * Returns the Description
     *
     * @return The Description
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     *
     * @param strDescription The Description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Returns the DateTimeStart
     *
     * @return The DateTimeStart
     */
    public String getDateTimeStart(  )
    {
        return _strDateTimeStart;
    }

    /**
     * Sets the DateTimeStart
     *
     * @param strDateTimeStart The DateTimeStart
     */
    public void setDateTimeStart( String strDateTimeStart )
    {
        _strDateTimeStart = strDateTimeStart;
    }

    /**
     * Returns the DateTimeEnd
     *
     * @return The DateTimeEnd
     */
    public String getDateTimeEnd(  )
    {
        return _strDateTimeEnd;
    }

    /**
     * Sets the DateTimeEnd
     *
     * @param strDateTimeEnd The DateTimeEnd
     */
    public void setDateTimeEnd( String strDateTimeEnd )
    {
        _strDateTimeEnd = strDateTimeEnd;
    }

    /**
     * Returns the Categories
     *
     * @return The Categories
     */
    public String getCategories(  )
    {
        return _strCategories;
    }

    /**
     * Sets the Categories
     *
     * @param strCategories The Categories
     */
    public void setCategories( String strCategories )
    {
        _strCategories = strCategories;
    }

    /**
     * Returns the Status
     *
     * @return The Status
     */
    public String getStatus(  )
    {
        return _strStatus;
    }

    /**
     * Sets the Status
     *
     * @param strStatus The Status
     */
    public void setStatus( String strStatus )
    {
        _strStatus = strStatus;
    }

    /**
     * Returns the Priority
     *
     * @return The Priority
     */
    public int getPriority(  )
    {
        return _nPriority;
    }

    /**
     * Sets the Priority
     *
     * @param nPriority The Priority
     */
    public void setPriority( int nPriority )
    {
        _nPriority = nPriority;
    }

    /**
     * Returns the Url
     *
     * @return The Url
     */
    public String getUrl(  )
    {
        return _strUrl;
    }

    /**
     * Sets the Url
     *
     * @param strUrl The Url
     */
    public void setUrl( String strUrl )
    {
        _strUrl = strUrl;
    }
}
