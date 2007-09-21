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

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for PollQuestion objects
 */
public final class PollQuestionDAO implements IPollQuestionDAO
{
    // SQL Queries
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_question ) FROM poll_question ";
    private static final String SQL_QUERY_SELECT = " SELECT id_question, id_poll, label_question FROM poll_question WHERE id_question = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO poll_question ( id_question, id_poll, label_question ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM poll_question WHERE id_question = ? ";
    private static final String SQL_QUERY_UPDATE = " UPDATE poll_question SET id_question = ?, id_poll = ?, label_question = ? WHERE id_question = ? ";
    private static final String SQL_QUERY_SELECTALLBYPOLL = " SELECT id_question, id_poll, label_question FROM poll_question WHERE id_poll = ? ";
    private static final String SQL_QUERY_SELECTBYQUESTION = "SELECT id_choice, id_question, label_choice, score FROM poll_choice WHERE id_question = ? ";

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
     * Insert a new record in the table.
     *
     * @param pollQuestion The pollQuestion object
     * @param plugin The Plugin using this data access service
     */
    public void insert( PollQuestion pollQuestion, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        pollQuestion.setId( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, pollQuestion.getId(  ) );
        daoUtil.setInt( 2, pollQuestion.getIdPoll(  ) );
        daoUtil.setString( 3, pollQuestion.getLabel(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of PollQuestion from the table
     *
     * @param nPollQuestionId The identifier of PollQuestion
     * @param plugin The Plugin using this data access service
     * @return the instance of the PollQuestion
     */
    public PollQuestion load( int nPollQuestionId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nPollQuestionId );
        daoUtil.executeQuery(  );

        PollQuestion pollQuestion = null;

        if ( daoUtil.next(  ) )
        {
            pollQuestion = new PollQuestion(  );
            pollQuestion.setId( daoUtil.getInt( 1 ) );
            pollQuestion.setIdPoll( daoUtil.getInt( 2 ) );
            pollQuestion.setLabel( daoUtil.getString( 3 ) );
            selectChoices( pollQuestion, plugin );

            int nVotesCount = 0;
            List<PollChoice> listChoices = pollQuestion.getChoices(  );

            for ( PollChoice choice : listChoices )
            {
                nVotesCount += choice.getScore(  );
            }

            pollQuestion.setVotesCount( nVotesCount );
        }

        daoUtil.free(  );

        return pollQuestion;
    }

    /**
     * Delete a record from the table
     * @param nPollQuestionId The PollQuestion Id
     * @param plugin The Plugin using this data access service
     */
    public void delete( int nPollQuestionId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nPollQuestionId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param pollQuestion The reference of pollQuestion
     * @param plugin The Plugin using this data access service
     */
    public void store( PollQuestion pollQuestion, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, pollQuestion.getId(  ) );
        daoUtil.setInt( 2, pollQuestion.getIdPoll(  ) );
        daoUtil.setString( 3, pollQuestion.getLabel(  ) );
        daoUtil.setInt( 4, pollQuestion.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the list of pollQuestions by poll id
     * @param nIdPoll The poll identifier
     * @param plugin The Plugin using this data access service
     * @return The List of the PollQuestions
     */
    public List<PollQuestion> selectPollQuestionListByPoll( int nIdPoll, Plugin plugin )
    {
        List<PollQuestion> listPollQuestions = new ArrayList<PollQuestion>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALLBYPOLL, plugin );
        daoUtil.setInt( 1, nIdPoll );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            PollQuestion pollQuestion = new PollQuestion(  );
            pollQuestion.setId( daoUtil.getInt( 1 ) );
            pollQuestion.setIdPoll( daoUtil.getInt( 2 ) );
            pollQuestion.setLabel( daoUtil.getString( 3 ) );
            selectChoices( pollQuestion, plugin );

            listPollQuestions.add( pollQuestion );
        }

        daoUtil.free(  );

        return listPollQuestions;
    }

    /**
     * Load the list of pollChoices by question
     * @param pollQuestion The pollQuestion object
     * @param plugin The Plugin using this data access service     *
     */
    void selectChoices( PollQuestion pollQuestion, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTBYQUESTION, plugin );
        daoUtil.setInt( 1, pollQuestion.getId(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            PollChoice pollChoice = new PollChoice(  );
            pollChoice.setId( daoUtil.getInt( 1 ) );
            pollChoice.setQuestionId( daoUtil.getInt( 2 ) );
            pollChoice.setLabel( daoUtil.getString( 3 ) );
            pollChoice.setScore( daoUtil.getInt( 4 ) );
            pollQuestion.getChoices(  ).add( pollChoice );
            pollQuestion.setVotesCount( pollQuestion.getVotesCount(  ) + pollChoice.getScore(  ) );
        }

        daoUtil.free(  );
    }
}
