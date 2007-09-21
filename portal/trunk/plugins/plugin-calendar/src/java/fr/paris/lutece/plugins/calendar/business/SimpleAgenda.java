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

import fr.paris.lutece.plugins.calendar.service.Utils;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * A basic implementation of an agenda
 */
public class SimpleAgenda implements Agenda
{
    private String _strName;
    private String _strKeyName;
    private HashMap _mapDays = new HashMap(  );
    private ArrayList _list = new ArrayList(  );

    /**
     * Returns the name of the Agenda
     * @return The agenda's name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Defines the name of the Agenda
     * @param strName The agenda's name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the KeyName
     *
     * @return The KeyName
     */
    public String getKeyName(  )
    {
        return _strKeyName;
    }

    /**
     * Sets the KeyName
     *
     * @param strKeyName The KeyName
     */
    public void setKeyName( String strKeyName )
    {
        _strKeyName = strKeyName;
    }

    /**
     * Indicates if the agenda gets events for a given date
     * @param strDate A date code
     * @return True if there is events, otherwise false
     */
    public boolean hasEvents( String strDate )
    {
        return _mapDays.containsKey( strDate );
    }

    /**
     * Retrieves events for a given date
     * @param strDate A date code
     * @return A list of events
     */
    public List getEventsByDate( String strDate )
    {
        List listEvents = null;

        if ( hasEvents( strDate ) )
        {
            listEvents = (List) _mapDays.get( strDate );
        }

        return listEvents;
    }

    /**
     * Add an event to the agenda
     * @param event The event to add
     */
    public void addEvent( Event event )
    {
        String strDate = event.getDate(  );
        List listEvents = null;

        if ( hasEvents( strDate ) )
        {
            listEvents = getEventsByDate( strDate );
        }
        else
        {
            listEvents = new ArrayList(  );
            _mapDays.put( strDate, listEvents );
        }

        listEvents.add( event );
    }

    /**
     * Retrieves all events of the agenda
     * @return A list of events
     */
    public List getEvents(  )
    {
        _list.clear(  );

        Set<String> setKey = _mapDays.keySet(  );

        for ( String strDate : setKey )
        {
            List<Event> listEvents = getEventsByDate( strDate );

            for ( Event event : listEvents )
            {
                _list.add( event );
            }
        }

        return _list;
    }

    /**
     * Fetches the events by date
     * @param strDateBegin The start date
     * @param strDateEnd The end date
     * @return The list of evensts
     */
    public List getEventsByDate( String strDateBegin, String strDateEnd )
    {
        _list.clear(  );

        java.util.Calendar calendar = new GregorianCalendar(  );
        calendar.set( Utils.getYear( strDateBegin ), Utils.getMonth( strDateBegin ), Utils.getDay( strDateBegin ) );

        java.util.Calendar calendar1 = new GregorianCalendar(  );
        calendar1.set( Utils.getYear( strDateEnd ), Utils.getMonth( strDateEnd ), Utils.getDay( strDateEnd ) );

        while ( !Utils.getDate( calendar ).equals( Utils.getDate( calendar1 ) ) )
        {
            List<Event> listEvents = getEventsByDate( Utils.getDate( calendar ) );

            if ( listEvents != null )
            {
                for ( Event event : listEvents )
                {
                    _list.add( event );
                }
            }

            calendar.add( java.util.Calendar.DATE, 1 );
        }

        return _list;
    }
}
