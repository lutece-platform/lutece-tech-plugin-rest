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

import java.util.List;


/**
 *  This interface describes the minimum implementation for agendas
 */
public interface Agenda
{
    /**
     * Indicates if the agenda gets events for a given date
     * @param strDate A date code
     * @return True if there is events, otherwise false
     */
    boolean hasEvents( String strDate );

    /**
     * Retrieves events for a given date
     * @param strDate A date code
     * @return A list of events
     */
    List getEventsByDate( String strDate );

    /**
     * Retrieves events for a given date
     * @return A list of events
     * @param strDateBegin The start date
     * @param strDateEnd The end date
     */
    List getEventsByDate( String strDateBegin, String strDateEnd );

    /**
     * Retrieves all events of the agenda
     * @return A list of events
     */
    List getEvents(  );

    /**
     * Returns the name of the Agenda
     * @return The agenda's name
     */
    String getName(  );

    /**
     * Sets the name of the Agenda
     * @param strName The agenda's name
     */
    void setName( String strName );

    /**
     * Returns the key of the Agenda
     * @return The agenda's key
     */
    String getKeyName(  );

    /**
     * Sets the key of the Agenda
     * @param strKeyName The agenda's key name
     */
    void setKeyName( String strKeyName );
}
