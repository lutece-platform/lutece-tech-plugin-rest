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
 * This class provides Data Access methods for QuestionType objects
 */
public final class QuestionTypeDAO implements IQuestionTypeDAO
{
    /** This class implements the Singleton design pattern. */
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_qt ) FROM helpdesk_question_type";
    private static final String SQL_QUERY_SELECT = " SELECT id_qt, question_type FROM helpdesk_question_type WHERE id_qt = ?";
    private static final String SQL_QUERY_INSERT = " INSERT INTO helpdesk_question_type ( id_qt, question_type ) VALUES ( ?, ? )";
    private static final String SQL_QUERY_DELETE = " DELETE FROM helpdesk_question_type WHERE id_qt = ?";
    private static final String SQL_QUERY_UPDATE = " UPDATE helpdesk_question_type SET id_qt = ?, question_type = ? WHERE id_qt = ?";
    private static final String SQL_QUERY_SELECTALL = " SELECT id_qt, question_type FROM helpdesk_question_type ORDER BY question_type";
    private static final String SQL_QUERY_SELECT_QUESTION = " SELECT id_vq, last_name, first_name, email, question, id_qt, answer, date_vq, id_user, id_question_topic FROM helpdesk_visitor_question WHERE id_qt = ? ";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Calculate a new primary key to add a new QuestionType
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
     * @param questionType The Instance of the object QuestionType
     * @param plugin The Plugin using this data access service
     */
    public void insert( QuestionType questionType, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        questionType.setIdQuestionType( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, questionType.getIdQuestionType(  ) );
        daoUtil.setString( 2, questionType.getQuestionType(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete a record from the table
     *
     * @param nIdQuestionType The indentifier of the object QuestionType
     * @param plugin The Plugin using this data access service
     */
    public void delete( int nIdQuestionType, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdQuestionType );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * load the data of QuestionType from the table
     *
     * @param nIdQuestionType The indentifier of the object QuestionType
     * @param plugin The Plugin using this data access service
     * @return The Instance of the object QuestionType
     */
    public QuestionType load( int nIdQuestionType, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nIdQuestionType );
        daoUtil.executeQuery(  );

        QuestionType questionType = null;

        if ( daoUtil.next(  ) )
        {
            questionType = new QuestionType(  );
            questionType.setIdQuestionType( daoUtil.getInt( 1 ) );
            questionType.setQuestionType( daoUtil.getString( 2 ) );
            // Load questions
            questionType.setQuestions( getQuestions( nIdQuestionType, plugin ) );
        }

        daoUtil.free(  );

        return questionType;
    }

    /**
     * Update the record in the table
     *
     * @param questionType The instance of the QuestionType to update
     * @param plugin The Plugin using this data access service
     */
    public void store( QuestionType questionType, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, questionType.getIdQuestionType(  ) );
        daoUtil.setString( 2, questionType.getQuestionType(  ) );
        daoUtil.setInt( 3, questionType.getIdQuestionType(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Find all objects
     * @param plugin The Plugin using this data access service
     * @return A Collection of objects
     */
    public ReferenceList findAll( Plugin plugin )
    {
        ReferenceList listQuestionType = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            QuestionType questionType = new QuestionType(  );
            questionType.setIdQuestionType( daoUtil.getInt( 1 ) );
            questionType.setQuestionType( daoUtil.getString( 2 ) );
            questionType.setQuestions( getQuestions( daoUtil.getInt( 1 ), plugin ) );
            listQuestionType.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return listQuestionType;
    }

    /**
     * Find all objects
     * @param plugin The Plugin using this data access service
     * @return A Collection of objects
     */
    public List<QuestionType> getQuestionTypeList( Plugin plugin )
    {
        List listQuestionType = new ArrayList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            QuestionType questionType = new QuestionType(  );
            questionType.setIdQuestionType( daoUtil.getInt( 1 ) );
            questionType.setQuestionType( daoUtil.getString( 2 ) );
            questionType.setQuestions( getQuestions( daoUtil.getInt( 1 ), plugin ) );
            listQuestionType.add( questionType );
        }

        daoUtil.free(  );

        return listQuestionType;
    }

    /**
     * Returns the questions of a given type.
     * @param nIdQuestionType The identifier of the question type
     * @param plugin The Plugin using this data access service
     * @return A Collection of QuestionAnswer objects
     */
    public List<VisitorQuestion> getQuestions( int nIdQuestionType, Plugin plugin )
    {
        List listVisitorQuestion = new ArrayList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_QUESTION, plugin );
        daoUtil.setInt( 1, nIdQuestionType );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            VisitorQuestion visitorQuestion = new VisitorQuestion(  );
            visitorQuestion.setIdVisitorQuestion( daoUtil.getInt( 1 ) );
            visitorQuestion.setLastname( daoUtil.getString( 2 ) );
            visitorQuestion.setFirstname( daoUtil.getString( 3 ) );
            visitorQuestion.setEmail( daoUtil.getString( 4 ) );
            visitorQuestion.setQuestion( daoUtil.getString( 5 ) );
            visitorQuestion.setIdQuestionType( daoUtil.getInt( 6 ) );
            visitorQuestion.setAnswer( daoUtil.getString( 7 ) );
            visitorQuestion.setDate( daoUtil.getDate( 8 ) );
            visitorQuestion.setIdUser( daoUtil.getInt( 9 ) );
            visitorQuestion.setIdQuestionTopic( daoUtil.getInt( 10 ) );
            listVisitorQuestion.add( visitorQuestion );
        }

        daoUtil.free(  );

        return listVisitorQuestion;
    }
}
