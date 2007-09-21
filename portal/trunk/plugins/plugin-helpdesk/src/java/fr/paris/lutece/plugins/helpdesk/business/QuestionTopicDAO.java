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
package fr.paris.lutece.plugins.helpdesk.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for QuestionTopic objects
 */
public final class QuestionTopicDAO implements IQuestionTopicDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_question_topic ) FROM helpdesk_question_topic";
    private static final String SQL_QUERY_SELECT = "SELECT id_question_topic, question_topic, id_question_type FROM helpdesk_question_topic WHERE id_question_topic = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO helpdesk_question_topic ( id_question_topic, question_topic, id_question_type ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM helpdesk_question_topic WHERE id_question_topic = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE helpdesk_question_topic SET id_question_topic = ?, question_topic = ?, id_question_type = ? WHERE id_question_topic = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_question_topic, question_topic, id_question_type FROM helpdesk_question_topic";
    private static final String SQL_QUERY_SELECT_BY_TYPE = "SELECT id_question_topic, question_topic, id_question_type FROM helpdesk_question_topic WHERE id_question_type = ? ";

    /**
     * Generates a new primary key
     *
     * @param plugin The Plugin using this data access service
     * @return The new key.
     */
    public int newPrimaryKey( Plugin plugin )
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
     * @param questionTopic instance of the QuestionTopic object to insert
     * @param plugin the Plugin
     */
    public void insert( QuestionTopic questionTopic, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        questionTopic.setIdQuestionTopic( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, questionTopic.getIdQuestionTopic(  ) );
        daoUtil.setString( 2, questionTopic.getQuestionTopic(  ) );
        daoUtil.setInt( 3, questionTopic.getIdQuestionType(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of the questionTopic from the table
     *
     * @param nId The identifier of the questionTopic
     * @param plugin the Plugin
     * @return the instance of the QuestionTopic
     */
    public QuestionTopic load( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        QuestionTopic questionTopic = null;

        if ( daoUtil.next(  ) )
        {
            questionTopic = new QuestionTopic(  );

            questionTopic.setIdQuestionTopic( daoUtil.getInt( 1 ) );
            questionTopic.setQuestionTopic( daoUtil.getString( 2 ) );
            questionTopic.setIdQuestionType( daoUtil.getInt( 3 ) );
        }

        daoUtil.free(  );

        return questionTopic;
    }

    /**
     * Delete a record from the table
     *
     * @param nQuestionTopicId The identifier of the questionTopic
     * @param plugin the Plugin
     */
    public void delete( int nQuestionTopicId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nQuestionTopicId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     *
     * @param questionTopic The reference of the questionTopic
     * @param plugin the Plugin
     */
    public void store( QuestionTopic questionTopic, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, questionTopic.getIdQuestionTopic(  ) );
        daoUtil.setString( 2, questionTopic.getQuestionTopic(  ) );
        daoUtil.setInt( 3, questionTopic.getIdQuestionType(  ) );
        daoUtil.setInt( 4, questionTopic.getIdQuestionTopic(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of all the questionTopics and returns them in form of a collection
     *
     * @param plugin the Plugin
     * @return The List which contains the data of all the questionTopics
     */
    public ReferenceList selectQuestionTopicsList( Plugin plugin )
    {
        ReferenceList questionTopicList = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            QuestionTopic questionTopic = new QuestionTopic(  );

            questionTopic.setIdQuestionTopic( daoUtil.getInt( 1 ) );
            questionTopic.setQuestionTopic( daoUtil.getString( 2 ) );
            questionTopic.setIdQuestionType( daoUtil.getInt( 3 ) );
            questionTopicList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return questionTopicList;
    }

    /**
     * Load the data of all the questionTopics and returns them in form of a collection
     * @param plugin the Plugin
     * @return The List which contains the data of all the questionTopics
     */
    public List<QuestionTopic> selectTopicList( Plugin plugin )
    {
        List questionTopicList = new ArrayList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            QuestionTopic questionTopic = new QuestionTopic(  );

            questionTopic.setIdQuestionTopic( daoUtil.getInt( 1 ) );
            questionTopic.setQuestionTopic( daoUtil.getString( 2 ) );
            questionTopic.setIdQuestionType( daoUtil.getInt( 3 ) );
            questionTopicList.add( questionTopic );
        }

        daoUtil.free(  );

        return questionTopicList;
    }

    /**
     * Load the data of all the questionTopics and returns them in form of a collection
     * @param nIdQuestionType The Identifier of Question Type
     * @param plugin the Plugin
     * @return The List which contains the data of all the questionTopics
     */
    public List<QuestionTopic> selectQuestionTopicList( int nIdQuestionType, Plugin plugin )
    {
        List<QuestionTopic> questionTopicList = new ArrayList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_TYPE, plugin );
        daoUtil.setInt( 1, nIdQuestionType );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            QuestionTopic questionTopic = new QuestionTopic(  );

            questionTopic.setIdQuestionTopic( daoUtil.getInt( 1 ) );
            questionTopic.setQuestionTopic( daoUtil.getString( 2 ) );
            questionTopic.setIdQuestionType( daoUtil.getInt( 3 ) );

            questionTopicList.add( questionTopic );
        }

        daoUtil.free(  );

        return questionTopicList;
    }
}
