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
import java.util.List;



/**
 * This class provides instances management methods (create, find, ...) for PollQuestions objects
 */
public final class PollQuestionHome
{
    // Static variable pointed at the DAO instance
    private static IPollQuestionDAO _dao = (IPollQuestionDAO) SpringContextService.getPluginBean( "poll" , "pollQuestionDAO");    
    
    /**
     * Private constructor - this class need not be instantiated
     */
    private PollQuestionHome()
    {
    }
    
    /**
     * Creation of an instance of an article PollQuestion
     *
     * @param question An instance of the PollQuestion which contains the
     *        informations to store
     * @param plugin The Plugin object
     * @return The instance of the PollQuestion which has been created
     */
    public static PollQuestion create( PollQuestion question, Plugin plugin )
    {
        _dao.insert( question, plugin );
        
        return question;
    }
    
    /**
     * Updates of the PollQuestion instance specified in parameter
     *
     * @param question An instance of the PollQuestion which contains the
     *        informations to store
     * @param plugin The Plugin object
     * @return The instance of the PollQuestion which has been updated.
     */
    public static PollQuestion update( PollQuestion question, Plugin plugin )
    {
        _dao.store( question, plugin );
        
        return question;
    }
    
    /**
     * Deletes the PollQuestion instance whose identifier is specified in
     * parameter
     *
     * @param nIdQuestion The identifier of the article PollQuestion to delete
     *        in the database
     * @param plugin The Plugin object
     */
    public static void remove( int nIdQuestion, Plugin plugin )
    {
        _dao.delete( nIdQuestion, plugin );
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // Finders
    
    /**
     * Returns an instance of PollQuestion whose identifier is
     * specified in parameter
     *
     * @param nIdQuestion The primary key of the article to find in the database
     * @param plugin The Plugin object
     * @return An instance of the PollQuestion which corresponds to the key
     */
    public static PollQuestion findByPrimaryKey( int nIdQuestion, Plugin plugin )
    {
        return _dao.load( nIdQuestion, plugin );
    }
    
    /**
     * Returns the list of the questions of a Poll object
     *
     * @param nIdPoll The primary key of the poll to find in the database
     * @param plugin The Plugin object
     * @return An instance of the Poll which corresponds to the key
     */
    public static List<PollQuestion> findPollQuestionListByPoll( int nIdPoll, Plugin plugin )
    {
        return _dao.selectPollQuestionListByPoll( nIdPoll, plugin);
    }
    
}
