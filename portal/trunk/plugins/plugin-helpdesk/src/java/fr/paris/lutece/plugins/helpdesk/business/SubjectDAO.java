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
 * This class provides Data Access methods for Subject objects
 */
public final class SubjectDAO implements ISubjectDAO
{
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_subject ) FROM helpdesk_subject";
    private static final String SQL_QUERY_SELECT = " SELECT id_subject, subject FROM helpdesk_subject WHERE id_subject = ?";
    private static final String SQL_QUERY_INSERT = " INSERT INTO helpdesk_subject ( id_subject, subject ) VALUES ( ?, ? )";
    private static final String SQL_QUERY_DELETE = " DELETE FROM helpdesk_subject WHERE id_subject = ?";
    private static final String SQL_QUERY_UPDATE = " UPDATE helpdesk_subject SET id_subject = ?, subject = ? WHERE id_subject = ?";
    private static final String SQL_QUERY_SELECTALL = " SELECT id_subject, subject FROM helpdesk_subject ORDER BY subject";
    private static final String SQL_QUERY_SELECT_QUESTION = "  SELECT id_qa, question, answer, id_subject, status FROM helpdesk_question_answer WHERE id_subject = ? ";
    private static final String SQL_QUERY_COUNT_QUESTION = " SELECT count(id_qa) FROM helpdesk_question_answer WHERE id_subject = ? ";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Calculate a new primary key to add a new Subject
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
     * @param subject The Instance of the object Subject
     * @param plugin The Plugin using this data access service
     */
    public void insert( Subject subject, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        subject.setIdSubject( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, subject.getIdSubject(  ) );
        daoUtil.setString( 2, subject.getSubject(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete a record from the table
     *
     * @param nIdSubject The indentifier of the object Subject
     * @param plugin The Plugin using this data access service
     */
    public void delete( int nIdSubject, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdSubject );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * load the data of Subject from the table
     *
     * @param nIdSubject The indentifier of the object Subject
     * @param plugin The Plugin using this data access service
     * @return The Instance of the object Subject
     */
    public Subject load( int nIdSubject, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nIdSubject );
        daoUtil.executeQuery(  );

        Subject subject = null;

        if ( daoUtil.next(  ) )
        {
            subject = new Subject(  );
            subject.setIdSubject( daoUtil.getInt( 1 ) );
            subject.setSubject( daoUtil.getString( 2 ) );
            // Load questions
            subject.setQuestions( findQuestions( nIdSubject, plugin ) );
        }

        daoUtil.free(  );

        return subject;
    }

    /**
     * Update the record in the table
     *
     * @param subject The instance of the Subject to update
     * @param plugin The Plugin using this data access service
     */
    public void store( Subject subject, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, subject.getIdSubject(  ) );
        daoUtil.setString( 2, subject.getSubject(  ) );
        daoUtil.setInt( 3, subject.getIdSubject(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Finds all objects of this type
     * @param plugin The Plugin using this data access service
     * @return A collection of objects
     */
    public List<Subject> findAll( Plugin plugin )
    {
        List list = new ArrayList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Subject subject = new Subject(  );
            subject.setIdSubject( daoUtil.getInt( 1 ) );
            subject.setSubject( daoUtil.getString( 2 ) );
            subject.setQuestions( findQuestions( daoUtil.getInt( 1 ), plugin ) );
            list.add( subject );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Finds all objects of this type
     * @param plugin The Plugin using this data access service
     * @return A collection of objects
     */
    public ReferenceList findSubject( Plugin plugin )
    {
        ReferenceList list = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Subject subject = new Subject(  );
            subject.setIdSubject( daoUtil.getInt( 1 ) );
            subject.setSubject( daoUtil.getString( 2 ) );
            subject.setQuestions( findQuestions( daoUtil.getInt( 1 ), plugin ) );
            list.addItem( subject.getIdSubject(  ), subject.getSubject(  ) );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Returns all questions on a subject
     * @param nIdSubject The identifier of the subject
     * @param plugin The Plugin using this data access service
     * @return A collection of questions
     */
    public List<QuestionAnswer> findQuestions( int nIdSubject, Plugin plugin )
    {
        List list = new ArrayList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_QUESTION, plugin );
        daoUtil.setInt( 1, nIdSubject );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            QuestionAnswer question = new QuestionAnswer(  );
            question.setIdQuestionAnswer( daoUtil.getInt( 1 ) );
            question.setQuestion( daoUtil.getString( 2 ) );
            question.setAnswer( daoUtil.getString( 3 ) );
            question.setIdSubject( daoUtil.getInt( 4 ) );
            question.setStatus( daoUtil.getInt( 5 ) );
            list.add( question );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * return the count of all announce for Field
     * @param plugin The current plugin using this method
     * @param nIdSubject The subject ID
     * @return count of announce for Field
     */
    public int countQuestion( int nIdSubject, Plugin plugin )
    {
        int nCount = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_QUESTION, plugin );
        daoUtil.setInt( 1, nIdSubject );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nCount = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nCount;
    }
}
