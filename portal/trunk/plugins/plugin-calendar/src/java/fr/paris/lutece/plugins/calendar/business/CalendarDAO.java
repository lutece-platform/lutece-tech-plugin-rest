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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *  This DAO class used to fetch the calendars in the database
 */
public class CalendarDAO implements ICalendarDAO
{
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_agenda ) FROM calendar_agendas ";
    private static final String SQL_QUERY_NEW_PK_EVENTS = " SELECT max( id_event ) FROM calendar_events ";
    private static final String SQL_QUERY_INSERT_AGENDA = " INSERT INTO calendar_agendas ( id_agenda, agenda_name, agenda_image, agenda_prefix, role ,role_manage, workgroup_key) VALUES ( ?, ?, ?, ?, ?, ? ,? ) ";
    private static final String SQL_QUERY_UPDATE_AGENDA = " UPDATE calendar_agendas SET agenda_name = ?, agenda_image = ?, agenda_prefix = ?, role = ?, role_manage = ?, workgroup_key = ? WHERE id_agenda = ?  ";
    private static final String SQL_QUERY_DELETE_AGENDA = " DELETE FROM calendar_agendas WHERE id_agenda = ?  ";
    private static final String SQL_QUERY_SELECT_AGENDA = "SELECT id_agenda, agenda_name, agenda_image, agenda_prefix, role, role_manage, workgroup_key FROM calendar_agendas WHERE id_agenda=?";
    private static final String SQL_QUERY_SELECTALL_AGENDAS = "SELECT id_agenda, agenda_name, agenda_image, agenda_prefix, role,role_manage, workgroup_key FROM calendar_agendas ORDER BY agenda_name";
    private static final String SQL_QUERY_INSERT_EVENT = " INSERT INTO calendar_events ( id_event, id_agenda, event_date, event_time_start, event_time_end, event_title ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_UPDATE_EVENT = " UPDATE calendar_events SET id_agenda =?, event_date = ?, event_time_start = ?, event_time_end = ?, event_title = ? WHERE id_event = ? ";
    private static final String SQL_QUERY_DELETE_EVENT = " DELETE FROM calendar_events WHERE id_agenda = ? AND id_event= ? ";
    private static final String SQL_QUERY_SELECT_EVENT = "SELECT  event_date, event_time_start, event_time_end, event_title FROM calendar_events WHERE id_event= ? ";
    private static final String SQL_QUERY_SELECT_EVENTS = "SELECT id_event, event_date, event_time_start, event_time_end, event_title FROM calendar_events WHERE id_agenda = ? ORDER BY event_date ";

    /**
     * Generates a new primary key
     * @param plugin The Plugin using this data access service
     * @return The new primary key
     */
    int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nKey;
    }

    /**
    * Generates a new primary key for the event
    * @param plugin The Plugin using this data access service
    * @return The new primary key
    */
    int newEventPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK_EVENTS, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nKey;
    }

    /**
     * Insert a new agenda in the table calendar_agendas.
     *
     * @param agenda The AgendaResource object
     * @param plugin The Plugin using this data access service
     */
    public void insertAgenda( AgendaResource agenda, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_AGENDA, plugin );
        agenda.setId( String.valueOf( newPrimaryKey( plugin ) ) );
        daoUtil.setInt( 1, Integer.parseInt( agenda.getId(  ) ) );
        daoUtil.setString( 2, agenda.getName(  ) );
        daoUtil.setString( 3, agenda.getEventImage(  ) );
        daoUtil.setString( 4, agenda.getEventPrefix(  ) );
        daoUtil.setString( 5, agenda.getRole(  ) );
        daoUtil.setString( 6, agenda.getRoleManager(  ) );
        daoUtil.setString( 7, agenda.getWorkgroup(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the agenda in the table calendar_agendas
     * @param agenda The reference of AgendaResource
     * @param plugin The Plugin using this data access service
     */
    public void storeAgenda( AgendaResource agenda, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_AGENDA, plugin );
        daoUtil.setString( 1, agenda.getName(  ) );
        daoUtil.setString( 2, agenda.getEventImage(  ) );
        daoUtil.setString( 3, agenda.getEventPrefix(  ) );
        daoUtil.setString( 4, agenda.getRole(  ) );
        daoUtil.setString( 5, agenda.getRoleManager(  ) );
        daoUtil.setString( 6, agenda.getWorkgroup(  ) );
        daoUtil.setInt( 7, Integer.parseInt( agenda.getId(  ) ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete an agenda from the table calendar_agendas
     * @param nAgendaId The Agenda Id
     * @param plugin The Plugin using this data access service
     */
    public void deleteAgenda( int nAgendaId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_AGENDA, plugin );
        daoUtil.setInt( 1, nAgendaId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Insert a new event in the table calendar_events.
     * @param nAgendaId The agenda id
     * @param event The event to be inserted
     * @param plugin The Plugin using this data access service
     */
    public void insertEvent( int nAgendaId, SimpleEvent event, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_EVENT, plugin );
        event.setId( newEventPrimaryKey( plugin ) );
        daoUtil.setInt( 1, event.getId(  ) );
        daoUtil.setInt( 2, nAgendaId );
        daoUtil.setString( 3, event.getDate(  ) );
        daoUtil.setString( 4, event.getDateTimeStart(  ) );
        daoUtil.setString( 5, event.getDateTimeEnd(  ) );
        daoUtil.setString( 6, event.getTitle(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the event in the table calendar_event
     *
     * @param event The reference of SimpleEvent
     * @param nAgendaId The identifier of the agenda
     * @param plugin The Plugin using this data access service
     */
    public void storeEvent( int nAgendaId, SimpleEvent event, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_EVENT, plugin );
        daoUtil.setInt( 1, nAgendaId );
        daoUtil.setString( 2, event.getDate(  ) );
        daoUtil.setString( 3, event.getDateTimeStart(  ) );
        daoUtil.setString( 4, event.getDateTimeEnd(  ) );
        daoUtil.setString( 5, event.getTitle(  ) );
        daoUtil.setInt( 6, event.getId(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete an Event from the table calendar_events
     * @param nEventId The id of the event
     * @param nAgendaId The agenda Id
     * @param plugin The Plugin using this data access service
     */
    public void deleteEvent( int nAgendaId, int nEventId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_EVENT, plugin );
        daoUtil.setInt( 1, nAgendaId );
        daoUtil.setInt( 2, nEventId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of AgendaResource from the table
     *
     *
     * @return the instance of the AgendaResource
     * @param nId The identifier of AgendaResource
     * @param plugin The plugin
     */
    public AgendaResource loadAgenda( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_AGENDA, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        AgendaResource agenda = null;

        if ( daoUtil.next(  ) )
        {
            agenda = new AgendaResource(  );
            agenda.setId( String.valueOf( daoUtil.getInt( 1 ) ) );
            agenda.setName( daoUtil.getString( 2 ) );
            agenda.setEventImage( daoUtil.getString( 3 ) );
            agenda.setEventPrefix( daoUtil.getString( 4 ) );
            agenda.setRole( daoUtil.getString( 5 ) );
            agenda.setRoleManager( daoUtil.getString( 6 ) );
            agenda.setWorkgroup( daoUtil.getString( 7 ) );
        }

        daoUtil.free(  );

        return agenda;
    }

    /**
     * Load the list of AgendaResources
     *
     * @param plugin The plugin
     * @return The Collection of the AgendaResources
     */
    public List<AgendaResource> selectAgendaResourceList( Plugin plugin )
    {
        List<AgendaResource> agendaList = new ArrayList<AgendaResource>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_AGENDAS, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AgendaResource agenda = new AgendaResource(  );
            agenda.setId( String.valueOf( daoUtil.getInt( 1 ) ) );
            agenda.setName( daoUtil.getString( 2 ) );
            agenda.setEventImage( daoUtil.getString( 3 ) );
            agenda.setEventPrefix( daoUtil.getString( 4 ) );
            agenda.setRole( daoUtil.getString( 5 ) );
            agenda.setRole( daoUtil.getString( 6 ) );
            agenda.setWorkgroup( daoUtil.getString( 7 ) );

            agendaList.add( agenda );
        }

        daoUtil.free(  );

        return agendaList;
    }

    /**
     * Load the data of SimpleEvent from the table
     * @return the instance of the SimpleEvent
     * @param nEventId The id of the event
     * @param plugin The plugin
     */
    public SimpleEvent loadEvent( int nEventId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_EVENT, plugin );
        daoUtil.setInt( 1, nEventId );
        daoUtil.executeQuery(  );

        SimpleEvent event = null;

        if ( daoUtil.next(  ) )
        {
            event = new SimpleEvent(  );
            event.setId( nEventId );
            event.setDate( daoUtil.getString( 1 ) );
            event.setDateTimeStart( daoUtil.getString( 2 ) );
            event.setDateTimeEnd( daoUtil.getString( 3 ) );
            event.setTitle( daoUtil.getString( 4 ) );
        }

        daoUtil.free(  );

        return event;
    }

    /**
     * Load the list of Events
     * @return The Collection of the Events
     * @param nSortEvents An integer used for sorting
     * @param plugin The plugin
     * @param nAgendaId The identifier of the agenda
     */
    public List<SimpleEvent> selectEventsList( int nAgendaId, int nSortEvents, Plugin plugin )
    {
        List<SimpleEvent> eventList = new ArrayList<SimpleEvent>(  );
        String strSortEvents = null;

        if ( nSortEvents == 1 )
        {
            strSortEvents = "ASC";
        }
        else
        {
            strSortEvents = "DESC";
        }

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_EVENTS + strSortEvents, plugin );
        daoUtil.setInt( 1, nAgendaId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            SimpleEvent event = new SimpleEvent(  );
            event.setId( daoUtil.getInt( 1 ) );
            event.setDate( daoUtil.getString( 2 ) );
            event.setDateTimeStart( daoUtil.getString( 3 ) );
            event.setDateTimeEnd( daoUtil.getString( 4 ) );
            event.setTitle( daoUtil.getString( 5 ) );
            eventList.add( event );
        }

        daoUtil.free(  );

        return eventList;
    }
}
