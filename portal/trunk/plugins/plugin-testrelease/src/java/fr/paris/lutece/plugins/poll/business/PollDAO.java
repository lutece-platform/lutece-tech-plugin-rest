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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for Poll objects
 */

public final class PollDAO implements IPollDAO
{
    
    // SQL Queries
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_poll ) FROM poll ";
    private static final String SQL_QUERY_SELECT = " SELECT id_poll, name, status, workgroup_key FROM poll WHERE id_poll = ?  ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO poll ( id_poll, name, status , workgroup_key ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM poll WHERE id_poll = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE poll SET id_poll = ?, name = ?, status = ?, workgroup_key = ? WHERE id_poll = ?  ";
    private static final String SQL_QUERY_SELECTALL = " SELECT id_poll, name FROM poll ";
    private static final String SQL_QUERY_SELECTQUESTIONSBYPOLL = "SELECT id_question, id_poll, label_question FROM poll_question WHERE id_poll = ? ";
    
    
    /** This class implements the Singleton design pattern. */
    
    /**
     * Generates a new primary key
     * @param plugin The Plugin using this data access service
     * @return The new primary key
     */
    
    int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK , plugin );
        daoUtil.executeQuery();
        
        int nKey;
        
        if( !daoUtil.next() )
        {
            // if the table is empty
            nKey = 1;
        }
        
        nKey = daoUtil.getInt( 1 ) + 1;
        
        daoUtil.free();
        return nKey;
    }
    
    
    
    
    /**
     * Insert a new record in the table.
     *
     * @param poll The poll object
     * @param plugin The Plugin using this data access service
     */
    
    public void insert( Poll poll , Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT , plugin );
        poll.setId( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, poll.getId() );
        daoUtil.setString( 2, poll.getName() );
        daoUtil.setBoolean( 3, poll.getStatus() );
        daoUtil.setString( 4 , poll.getWorkgroup() );
        
        daoUtil.executeUpdate();
        daoUtil.free();
    }
    
    
    /**
     * Load the data of Poll from the table
     *
     * @param nPollId The identifier of Poll
     * @param plugin The Plugin using this data access service
     * @return the instance of the Poll
     */
    
    
    public Poll load( int nPollId , Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT , plugin );
        daoUtil.setInt( 1 , nPollId );
        daoUtil.executeQuery();
        
        Poll poll = null;
        
        if ( daoUtil.next() )
        {
            poll = new Poll();
            poll.setId( daoUtil.getInt( 1 ) );
            poll.setName( daoUtil.getString( 2 ) );
            poll.setStatus( daoUtil.getBoolean( 3 ) );
            poll.setWorkgroup( daoUtil.getString( 4 ));
            selectQuestions( poll, plugin );
        }
        
        daoUtil.free();
        return poll;
    }
    
    
    /**
     * Delete a record from the table
     * @param nPollId The Poll Id
     * @param plugin The Plugin using this data access service
     */
    
    public void delete( int nPollId , Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE , plugin );
        daoUtil.setInt( 1 , nPollId );
        daoUtil.executeUpdate();
        daoUtil.free();
    }
    
    
    /**
     * Update the record in the table
     * @param poll The reference of poll
     * @param plugin The Plugin using this data access service
     */
    
    public void store( Poll poll , Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE , plugin );
        daoUtil.setInt( 1, poll.getId() );
        daoUtil.setString( 2, poll.getName() );
        daoUtil.setBoolean( 3, poll.getStatus() );
        daoUtil.setString( 4 , poll.getWorkgroup() );
        daoUtil.setInt( 5, poll.getId() );
        
        daoUtil.executeUpdate();
        daoUtil.free();
    }
    
    
    
    /**
     * Load the list of polls
     * @param plugin The Plugin using this data access service
     * @return The list of the Polls as a List object
     */
    
    public List<Poll> selectPollsList( Plugin plugin )
    {
        List<Poll> pollsList = new ArrayList<Poll>();
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery();
        
        while ( daoUtil.next() )
        {
            pollsList.add( load( daoUtil.getInt(1), plugin ) );
        }
        
        daoUtil.free();
        return pollsList;
    }
    
    /**
     * Load the list of pollQuestion by poll
     * @param poll The PollQuestion object
     * @param plugin The Plugin using this data access service     *
     */
    void selectQuestions( Poll poll, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTQUESTIONSBYPOLL, plugin );
        daoUtil.setInt( 1, poll.getId() );
        daoUtil.executeQuery();
        
        while ( daoUtil.next() )
        {
            PollQuestion pollQuestion = PollQuestionHome.findByPrimaryKey( daoUtil.getInt( 1 ), plugin );
            poll.getQuestions().add( pollQuestion );
        }
        
        daoUtil.free();
    }
    
    /**
     * Load the list of polls as a ReferenceList
     * 
     * @param plugin The Plugin using this data access service
     */    
    public ReferenceList getPollsList(Plugin plugin)
    {
        ReferenceList listPolls = new ReferenceList();
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery();
        
        while ( daoUtil.next() )
        {
            Poll poll = new Poll();
            poll.setId( daoUtil.getInt( 1 ) );
            poll.setName( daoUtil.getString( 2 ) );
            listPolls.addItem( poll.getId() , poll.getName() );
        }
        
        daoUtil.free();
        return listPolls;
    }
    
}