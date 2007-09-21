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
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;
import java.util.ArrayList;

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for Poll objects
 */
public final class PollHome
{
    // Static variable pointed at the DAO instance
    private static IPollDAO _dao = (IPollDAO) SpringContextService.getPluginBean( "poll" , "pollDAO");
    
    /**
     * Private constructor - this class need not be instantiated
     */
    private PollHome()
    {
    }
    
    /**
     * Creation of an instance of an article Poll
     *
     * @param poll An instance of the Poll which contains the informations to store
     * @param plugin The Plugin object
     * @return The instance of the Poll which has been created
     */
    public static Poll create( Poll poll, Plugin plugin )
    {
        _dao.insert( poll, plugin );
        
        return poll;
    }
    
    /**
     * Updates of the Poll instance specified in parameter
     *
     * @param poll An instance of the Poll which contains the informations to store
     * @param connectionService The ConnectionService
     * @return The instance of the Poll which has been updated.
     */
    public static Poll update( Poll poll, Plugin plugin )
    {
        _dao.store( poll, plugin );
        
        return poll;
    }
    
    /**
     * Deletes the Poll instance whose identifier is specified in parameter
     *
     * @param nIdPoll The identifier of the Poll to delete in the database
     * @param plugin The Plugin object
     */
    public static void remove( int nIdPoll, Plugin plugin )
    {
        _dao.delete( nIdPoll, plugin );
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // Finders
    
    /**
     * Returns an instance of the Poll whose identifier is specified in parameter
     *
     * @param nKey The primary key of the poll to find in the database
     * @param plugin The Plugin object
     * @return An instance of the Poll which corresponds to the key
     */
    public static Poll findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }
    
    /**
     * Returns Poll list
     *
     * @param plugin The Plugin object
     * @return the list of the Poll of the database as a List object
     */
    public static List<Poll> findAll( Plugin plugin )
    {
        return _dao.selectPollsList( plugin );
    }
    
    /**
     * Returns the list of avaiable polls
     *
     * @param plugin The Plugin object
     * @return the list of the avaiable poll as a List
     */
    public static List<Poll> getEnabledPollList( Plugin plugin )
    {
        List<Poll> listEnabledPoll = new ArrayList<Poll>();
        List<Poll> listPoll = _dao.selectPollsList( plugin );
        for ( Poll poll : listPoll )
        {
            if ( poll.getStatus() )
            {
                listEnabledPoll.add( poll );
            }
        }
        return listEnabledPoll;
    }

    public static ReferenceList getPollsList(Plugin plugin)
    {
        return _dao.getPollsList( plugin );
    }
    
}
