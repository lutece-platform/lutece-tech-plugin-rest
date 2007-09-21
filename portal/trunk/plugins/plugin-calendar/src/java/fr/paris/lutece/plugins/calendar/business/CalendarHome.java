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

import fr.paris.lutece.plugins.calendar.service.AgendaResource;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * This class provides instances management methods (selectEventsList, findByPrimaryKey, findAgendasList ...) for
 * Calendar objects ( AgendaResource, Events, ...)
 */
public class CalendarHome
{
    // Static variable pointed at the DAO instance
    private static ICalendarDAO _dao = (ICalendarDAO) SpringContextService.getPluginBean( "calendar", "calendarDAO" );

    /**
     * Insert a new agenda in the table calendar_agendas.
     *
     * @param agenda The AgendaResource object
     * @param plugin The Plugin using this data access service
     */
    public static void createAgenda( AgendaResource agenda, Plugin plugin )
    {
        _dao.insertAgenda( agenda, plugin );
    }

    /**
     * Update the agenda in the table calendar_agendas
     * @param agenda The reference of AgendaResource
     * @param plugin The Plugin using this data access service
     */
    public static void updateAgenda( AgendaResource agenda, Plugin plugin )
    {
        _dao.storeAgenda( agenda, plugin );
    }

    /**
     * Delete an agenda from the table calendar_agendas
     * @param nAgendaId The agenda Id
     * @param plugin The Plugin using this data access service
     */
    public static void removeAgenda( int nAgendaId, Plugin plugin )
    {
        _dao.deleteAgenda( nAgendaId, plugin );
    }

    /**
     * Load the list of AgendaResources
     *
     * @param plugin The plugin
     * @return The Collection of the AgendaResources
     */
    public static List<AgendaResource> findAgendaResourcesList( Plugin plugin )
    {
        return _dao.selectAgendaResourceList( plugin );
    }

    /**
     * Returns an instance of a AgendaResource whose identifier is specified in parameter
     *
     * @param nKey The Primary key of the contact
     * @param plugin The Plugin object
     * @return an instance of AgendaResource
     */
    public static AgendaResource findAgendaResource( int nKey, Plugin plugin )
    {
        return _dao.loadAgenda( nKey, plugin );
    }

    /**
     * Insert a new event in the table calendar_events.
     * @param nAgendaId The agenda id
     * @param event the event
     * @param plugin The Plugin using this data access service
     * @throws fr.paris.lutece.portal.service.util.AppException AppException
     */
    public static void createEvent( int nAgendaId, SimpleEvent event, Plugin plugin )
        throws AppException
    {
        _dao.insertEvent( nAgendaId, event, plugin );
    }

    /**
     * Update the event in the table calendar_event
     * @param nAgendaId The agenda id
     * @param strTitle The title of the event
     * @param event The reference of SimpleEvent
     * @param strDate the old event date
     * @param plugin The Plugin using this data access service
     * @throws fr.paris.lutece.portal.service.util.AppException Exception thrown if an error is detected
     */
    public static void updateEvent( int nAgendaId, SimpleEvent event, Plugin plugin )
        throws AppException
    {
        _dao.storeEvent( nAgendaId, event, plugin );
    }

    /**
     * Delete an Event from the table calendar_events
     * @param strDate The date
     * @param strTitle The title
     * @param nAgendaId The agenda Id
     * @param plugin The Plugin using this data access service
     */
    public static void removeEvent( int nAgendaId, int nEventId, Plugin plugin )
    {
        _dao.deleteEvent( nAgendaId, nEventId, plugin );
    }

    /**
     * Load the data of SimpleEvent from the table
     * @return the instance of the SimpleEvent
     * @param nAgendaId The agenda id
     * @param strEventDate The event date
     * @param strTitle The title of the event
     * @param plugin The plugin
     */
    public static SimpleEvent findEvent( int nEventId, Plugin plugin )
    {
        return _dao.loadEvent( nEventId, plugin );
    }

    /**
     * Load the list of Events
     * @return The Collection of the Events
     * @param nSortEvents An integer used for sorting issues
     * @param plugin The plugin
     * @param nAgendaId The identifier of the agenda
     */
    public static List<SimpleEvent> findEventsList( int nAgendaId, int nSortEvents, Plugin plugin )
    {
        return _dao.selectEventsList( nAgendaId, nSortEvents, plugin );
    }
}
