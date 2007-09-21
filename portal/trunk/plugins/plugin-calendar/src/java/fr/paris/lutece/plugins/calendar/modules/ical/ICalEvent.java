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
package fr.paris.lutece.plugins.calendar.modules.ical;

import fr.paris.lutece.plugins.calendar.business.Event;


/**
 * This class implements the Event interface for  events using
 * the iCalendar format (RFC 2445).
 */
public class ICalEvent implements Event
{
    // Constants
    private static final int DATE_ONLY_LENGTH = 8;
    private static final int DATE_TIME_LENGTH = 15;

    // Variables
    private String _strDate;
    private String _strDescription;
    private String _strLocation;
    private String _strStartHour;
    private String _strStartMinute;
    private String _strEndHour;
    private String _strEndMinute;
    private String _strTitle;
    private String _strEventClass;
    private String _strDateTimeStart;
    private String _strDateTimeEnd;
    private String _strCategories;
    private String _strStatus;
    private int _nPriority;
    private String _strUrl;

    // Event interface implementation

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
     * Returns the Title
     *
     * @return The Title
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * Returns the EventClass
     *
     * @return The Evenet class
     */
    public String getEventClass(  )
    {
        return _strEventClass;
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
     * Returns the Description
     *
     * @return The Description
     */
    public String getDescription(  )
    {
        return _strDescription;
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
     * Returns the DateTimeEnd
     *
     * @return The DateTimeEnd
     */
    public String getDateTimeEnd(  )
    {
        return _strDateTimeEnd;
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
     * Returns the Status
     *
     * @return The Status
     */
    public String getStatus(  )
    {
        return _strStatus;
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
     * Returns the Url
     *
     * @return The Url
     */
    public String getUrl(  )
    {
        return _strUrl;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters : scope package

    /**
     * Sets the DateTimeStart
     *
     * @param strDateTimeStart The Date
     */
    void setDateTimeStart( String strDateTimeStart )
    {
        if ( strDateTimeStart.length(  ) == DATE_ONLY_LENGTH )
        {
            _strDate = strDateTimeStart;
        }
        else if ( strDateTimeStart.length(  ) == DATE_TIME_LENGTH )
        {
            _strDate = strDateTimeStart.substring( 0, 8 );
            _strStartHour = strDateTimeStart.substring( 9, 11 );
            _strStartMinute = strDateTimeStart.substring( 11, 13 );
            _strDateTimeStart = _strStartHour + ":" + _strStartMinute;
        }
    }

    /**
     * Sets the DateTimeEnd
     *
     * @param strDateTimeEnd The Date
     */
    void setDateTimeEnd( String strDateTimeEnd )
    {
        if ( strDateTimeEnd.length(  ) == DATE_TIME_LENGTH )
        {
            _strDate = strDateTimeEnd.substring( 0, 8 );
            _strEndHour = strDateTimeEnd.substring( 9, 11 );
            _strEndMinute = strDateTimeEnd.substring( 11, 13 );
            _strDateTimeEnd = _strEndHour + ":" + _strEndMinute;
        }
    }

    /**
     * Sets the Title
     *
     * @param strTitle The Title
     */
    void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Sets the Location
     *
     * @param strLocation The Location
     */
    void setLocation( String strLocation )
    {
        _strLocation = strLocation;
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
     * Sets the Description
     *
     * @param strDescription The Description
     */
    void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Sets the Categories
     *
     * @param strCategories The Categories
     */
    void setCategories( String strCategories )
    {
        _strCategories = strCategories;
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
     * Sets the Priority
     *
     * @param nPriority The Priority
     */
    public void setPriority( int nPriority )
    {
        _nPriority = nPriority;
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
