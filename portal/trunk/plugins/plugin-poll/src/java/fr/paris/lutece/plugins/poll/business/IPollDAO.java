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
package fr.paris.lutece.plugins.poll.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;


/**
 * PollDAO Interface
 */
public interface IPollDAO
{
    /**
     * Delete a record from the table
     *
     * @param nPollId The Poll Id
     * @param plugin The Plugin using this data access service
     */
    void delete( int nPollId, Plugin plugin );

    /**
     * Load the list of polls
     *
     * @param plugin The Plugin using this data access service
     * @return The list of the Polls as a List object
     */
    List<Poll> selectPollsList( Plugin plugin );

    /**
     * Insert a new record in the table.
     *
     *
     * @param poll The poll object
     * @param plugin The Plugin using this data access service
     */
    void insert( Poll poll, Plugin plugin );

    /**
     * Load the data of Poll from the table
     *
     *
     * @param nPollId The identifier of Poll
     * @param plugin The Plugin using this data access service
     * @return the instance of the Poll
     */
    Poll load( int nPollId, Plugin plugin );

    /**
     * Update the record in the table
     *
     * @param poll The reference of poll
     * @param plugin The Plugin using this data access service
     */
    void store( Poll poll, Plugin plugin );

    /**
     * Load the list of polls as a ReferenceList
     *
     * @param plugin The Plugin using this data access service
     */
    ReferenceList getPollsList( Plugin plugin );
}
