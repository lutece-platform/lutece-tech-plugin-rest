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
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for PollChoice objects
 */

public final class PollChoiceDAO implements IPollChoiceDAO
{
    
    // SQL Queries
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_choice ) FROM poll_choice ";
    private static final String SQL_QUERY_SELECT = " SELECT id_choice, id_question, label_choice, score FROM poll_choice WHERE id_choice = ?  ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO poll_choice ( id_choice, id_question, label_choice, score ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM poll_choice WHERE id_choice = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE poll_choice SET id_choice = ?, id_question = ?, label_choice = ?, score = ? WHERE id_choice = ?  ";
    
    
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
     * @param pollChoice The pollChoice object
     * @param plugin The Plugin using this data access service
     */
    
    public void insert( PollChoice pollChoice , Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT , plugin );
        pollChoice.setId( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, pollChoice.getId() );
        daoUtil.setInt( 2, pollChoice.getQuestionId() );
        daoUtil.setString( 3, pollChoice.getLabel() );
        daoUtil.setInt( 4, pollChoice.getScore() );
        
        daoUtil.executeUpdate();
        daoUtil.free();
    }
    
    
    /**
     * Load the data of PollChoice from the table
     *
     * @param nPollChoiceId The identifier of PollChoice
     * @param plugin The Plugin using this data access service
     * @return the instance of the PollChoice
     */
    
    
    public PollChoice load( int nPollChoiceId , Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT , plugin );
        daoUtil.setInt( 1 , nPollChoiceId );
        daoUtil.executeQuery();
        
        PollChoice pollChoice = null;
        
        if ( daoUtil.next() )
        {
            pollChoice = new PollChoice();
            pollChoice.setId( daoUtil.getInt( 1 ));
            pollChoice.setQuestionId( daoUtil.getInt( 2 ));
            pollChoice.setLabel( daoUtil.getString( 3 ));
            pollChoice.setScore( daoUtil.getInt( 4 ));
        }
        
        daoUtil.free();
        return pollChoice;
    }
    
    
    /**
     * Delete a record from the table
     * @param nPollChoiceId The PollChoice Id
     * @param plugin The Plugin using this data access service
     */
    
    public void delete( int nPollChoiceId , Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE , plugin );
        daoUtil.setInt( 1 , nPollChoiceId );
        daoUtil.executeUpdate();
        daoUtil.free();
    }
    
    
    /**
     * Update the record in the table
     * @param pollChoice The reference of pollChoice
     * @param plugin The Plugin using this data access service
     */
    
    public void store( PollChoice pollChoice , Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE , plugin );
        daoUtil.setInt( 1, pollChoice.getId() );
        daoUtil.setInt( 2, pollChoice.getQuestionId() );
        daoUtil.setString( 3, pollChoice.getLabel() );
        daoUtil.setInt( 4, pollChoice.getScore() );
        daoUtil.setInt( 5, pollChoice.getId() );
        
        daoUtil.executeUpdate();
        daoUtil.free();
    }
}
