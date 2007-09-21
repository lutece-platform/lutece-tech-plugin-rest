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
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * AN interface used to access the calendar business layer
 */
public interface ICalendarDAO
{
    /**
     * Delete an Event from the table calendar_events
     * @param nEventId The id of the event
     * @param nAgendaId The Agenda Id
     * @param plugin The Plugin using this data access service
     */
    void deleteEvent( int nAgendaId, int nEventId, Plugin plugin );

    /**
     * Delete a Agenda from the table calendar_agendas
     *
     * @param nAgendaId The agenda Id
     * @param plugin The Plugin using this data access service
     */
    void deleteAgenda( int nAgendaId, Plugin plugin );

    /**
     * Insert a new event in the table calendar_events.
     * @param nAgendaId The agenda id
     * @param event The event
     * @param plugin The Plugin using this data access service
     * @throws fr.paris.lutece.portal.service.util.AppException An AppException error
     */
    void insertEvent( int nAgendaId, SimpleEvent event, Plugin plugin )
        throws AppException;

    /**
     * Insert a new agenda in the table calendar_agendas.
     *
     *
     * @param agenda The AgendaResource object
     * @param plugin The Plugin using this data access service
     */
    void insertAgenda( AgendaResource agenda, Plugin plugin );

    /**
     * Load the data of AgendaResource from the table
     *
     * @return the instance of the AgendaResource
     * @param nId The identifier of AgendaResource
     * @param plugin The plugin
     */
    AgendaResource loadAgenda( int nId, Plugin plugin );

    /**
     * Load the data of SimpleEvent from the table
     * @return the instance of the SimpleEvent
     * @param nEventId The id of the event
     * @param plugin The plugin
     */
    SimpleEvent loadEvent( int nEventId, Plugin plugin );

    /**
     * Load the list of AgendaResources
     *
     *
     * @param plugin The plugin
     * @return The Collection of the AgendaResources
     */
    List<AgendaResource> selectAgendaResourceList( Plugin plugin );

    /**
     * Load the list of Events
     * @return The Collection of the Events
     * @param nSortEvents Parameter used for event sorting
     * @param plugin The plugin
     * @param nAgendaId The identifier of the agenda
     */
    List<SimpleEvent> selectEventsList( int nAgendaId, int nSortEvents, Plugin plugin );

    /**
     * Update the agenda in the table calendar_agendas
     *
     * @param agenda The reference of AgendaResource
     * @param plugin The Plugin using this data access service
     */
    void storeAgenda( AgendaResource agenda, Plugin plugin );

    /**
     * Update the event in the table calendar_event
     *
     * @param event The reference of SimpleEvent
     * @param nAgendaId The identifier of the agenda
     * @param plugin The Plugin using this data access service
     * @throws fr.paris.lutece.portal.service.util.AppException An AppException
     */
    void storeEvent( int nAgendaId, SimpleEvent event, Plugin plugin )
        throws AppException;
}
