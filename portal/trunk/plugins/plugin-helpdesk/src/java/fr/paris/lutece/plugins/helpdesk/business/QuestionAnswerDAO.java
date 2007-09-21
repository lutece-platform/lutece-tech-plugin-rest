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
import fr.paris.lutece.util.sql.DAOUtil;
import fr.paris.lutece.util.string.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/* ajouter ici : import fr.paris.lutece.(nom_projet).util.*; */

/**
 * This class provides Data Access methods for QuestionAnswerAnswer objects
 */
public final class QuestionAnswerDAO implements IQuestionAnswerDAO
{
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_qa ) FROM helpdesk_question_answer";
    private static final String SQL_QUERY_SELECT = " SELECT id_qa, question, answer, id_subject, status FROM helpdesk_question_answer WHERE id_qa = ?";
    private static final String SQL_QUERY_INSERT = " INSERT INTO helpdesk_question_answer ( id_qa, question, answer, id_subject, status ) VALUES ( ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = " DELETE FROM helpdesk_question_answer WHERE id_qa = ?";
    private static final String SQL_QUERY_DELETE_BY_SUBJECT = " DELETE FROM helpdesk_question_answer WHERE id_subject = ?";
    private static final String SQL_QUERY_UPDATE = " UPDATE helpdesk_question_answer SET id_qa = ?, question = ?, answer = ?, id_subject = ?, status = ? WHERE id_qa = ?";
    private static final String SQL_QUERY_SELECTALL = " SELECT id_qa, question, answer, id_subject, status FROM helpdesk_question_answer ";
    private static final String SQL_QUERY_SELECT_BY_KEYWORDS = " SELECT id_qa, question, answer, id_subject, status FROM helpdesk_question_answer ";
    private static final String SQL_QUERY_SELECT_COUNT = " SELECT count(id_qa) FROM helpdesk_question_answer WHERE id_subject = ? ";

    // /////////////////////////////////////////////////////////////////////////////////////
    // Access methods to data
    /**
     * Calculate a new primary key to add a new QuestionAnswer
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
     * @param questionAnswer The Instance of the object QuestionAnswer
     * @param plugin The Plugin using this data access service
     */
    public void insert( QuestionAnswer questionAnswer, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        questionAnswer.setIdQuestionAnswer( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, questionAnswer.getIdQuestionAnswer(  ) );
        daoUtil.setString( 2, questionAnswer.getQuestion(  ) );
        daoUtil.setString( 3, questionAnswer.getAnswer(  ) );
        daoUtil.setInt( 4, questionAnswer.getIdSubject(  ) );

        if ( questionAnswer.isEnabled(  ) )
        {
            daoUtil.setInt( 5, 1 );
        }

        else
        {
            daoUtil.setInt( 5, 0 );
        }

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete a record from the table
     *
     * @param nIdQuestionAnswer The indentifier of the object QuestionAnswer
     * @param plugin The Plugin using this data access service
     */
    public void delete( int nIdQuestionAnswer, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdQuestionAnswer );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete a record from the table
     *
     * @param nIdSubject The indentifier of the object QuestionAnswer
     * @param plugin The Plugin using this data access service
     */
    public void deleteBySubject( int nIdSubject, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_SUBJECT, plugin );
        daoUtil.setInt( 1, nIdSubject );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * load the data of QuestionAnswer from the table
     *
     * @param nIdQuestionAnswer The indentifier of the object QuestionAnswer
     * @param plugin The Plugin using this data access service
     * @return The Instance of the object QuestionAnswer
     */
    public QuestionAnswer load( int nIdQuestionAnswer, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nIdQuestionAnswer );
        daoUtil.executeQuery(  );

        QuestionAnswer questionanswer = null;

        if ( daoUtil.next(  ) )
        {
            questionanswer = new QuestionAnswer(  );
            questionanswer.setIdQuestionAnswer( daoUtil.getInt( 1 ) );
            questionanswer.setQuestion( daoUtil.getString( 2 ) );
            questionanswer.setAnswer( daoUtil.getString( 3 ) );
            questionanswer.setIdSubject( daoUtil.getInt( 4 ) );
            questionanswer.setStatus( daoUtil.getInt( 5 ) );
        }

        daoUtil.free(  );

        return questionanswer;
    }

    /**
     * Update the record in the table
     *
     * @param questionAnswer The instance of the QuestionAnswer to update
     * @param plugin The Plugin using this data access service
     */
    public void store( QuestionAnswer questionAnswer, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, questionAnswer.getIdQuestionAnswer(  ) );
        daoUtil.setString( 2, questionAnswer.getQuestion(  ) );
        daoUtil.setString( 3, questionAnswer.getAnswer(  ) );
        daoUtil.setInt( 4, questionAnswer.getIdSubject(  ) );

        if ( questionAnswer.isEnabled(  ) )
        {
            daoUtil.setInt( 5, 1 );
        }
        else
        {
            daoUtil.setInt( 5, 0 );
        }

        daoUtil.setInt( 6, questionAnswer.getIdQuestionAnswer(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Find all objects.
     *
     * @param plugin The Plugin using this data access service
     * @return A Collection of objects
     */
    public List<QuestionAnswer> findAll( Plugin plugin )
    {
        List listQuestionAnswer = new ArrayList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            QuestionAnswer questionanswer = new QuestionAnswer(  );
            questionanswer.setIdQuestionAnswer( daoUtil.getInt( 1 ) );
            questionanswer.setQuestion( daoUtil.getString( 2 ) );
            questionanswer.setAnswer( daoUtil.getString( 3 ) );
            questionanswer.setIdSubject( daoUtil.getInt( 4 ) );
            questionanswer.setStatus( daoUtil.getInt( 5 ) );

            listQuestionAnswer.add( questionanswer );
        }

        daoUtil.free(  );

        return listQuestionAnswer;
    }

    /**
     * load the data of QuestionAnswer from the table
     *
     * @param strKeywords The keywords which are searched in question/answer
     * @param plugin The Plugin using this data access service
     * @return The collection of QuestionAnswer object
     */
    public List<QuestionAnswer> findByKeywords( String strKeywords, Plugin plugin )
    {
        // Temporary variable to avoid reassigning strKeywords :
        String strKeywordsEscaped = StringUtil.substitute( strKeywords, "\\'", "'" );
        List listQuestionAnswer = new ArrayList(  );

        StringTokenizer st = new StringTokenizer( strKeywordsEscaped );

        int counter = 0;
        String sqlRequest = SQL_QUERY_SELECT_BY_KEYWORDS;

        while ( st.hasMoreTokens(  ) )
        {
            String motActuel = st.nextToken(  );

            if ( counter == 0 )
            {
                sqlRequest += ( " WHERE (question like '%" + motActuel + "%' OR answer like '%" + motActuel + "%')" );
            }
            else
            {
                sqlRequest += ( " AND (question like '%" + motActuel + "%' OR answer like '%" + motActuel + "%')" );
            }

            counter++;
        }

        sqlRequest += " order by id_subject ";

        DAOUtil daoUtil = new DAOUtil( sqlRequest, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            QuestionAnswer questionanswer = new QuestionAnswer(  );
            questionanswer.setIdQuestionAnswer( daoUtil.getInt( 1 ) );
            questionanswer.setQuestion( daoUtil.getString( 2 ) );
            questionanswer.setAnswer( daoUtil.getString( 3 ) );
            questionanswer.setIdSubject( daoUtil.getInt( 4 ) );
            questionanswer.setStatus( daoUtil.getInt( 5 ) );
            listQuestionAnswer.add( questionanswer );
        }

        daoUtil.free(  );

        return listQuestionAnswer;
    }

    /**
     *
     * @param plugin The Plugin using this data access service
     * @param nIdSubject The Subject ID
     * @return count
     */
    public int countbySubject( int nIdSubject, Plugin plugin )
    {
        int count = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_COUNT, plugin );

        daoUtil.setInt( 1, nIdSubject );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            count = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return count;
    }
}
