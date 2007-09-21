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
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...)
 * for QuestionType objects
 */
public final class QuestionTypeHome
{
    // Static variable pointed at the DAO instance
    private static IQuestionTypeDAO _dao = (IQuestionTypeDAO) SpringContextService.getPluginBean( "helpdesk",
            "questionTypeDAO" );

    /**
     * Private constructor - this class need not be instantiated.
     */
    private QuestionTypeHome(  )
    {
    }

    /**
     * Creation of an instance of an article QuestionType
     *
     * @param questionType An instance of the QuestionType which contains the informations to store
     * @param plugin The current plugin using this method
     * @return The instance of the QuestionType which has been created
     */
    public static QuestionType create( QuestionType questionType, Plugin plugin )
    {
        _dao.insert( questionType, plugin );

        return questionType;
    }

    /**
     * Updates of the QuestionType instance specified in parameter
     *
     * @param questionType An instance of the QuestionType which contains the informations to store
     * @param plugin The current plugin using this method
     * @return The instance of the QuestionType which has been updated.
     */
    public static QuestionType update( QuestionType questionType, Plugin plugin )
    {
        _dao.store( questionType, plugin );

        return questionType;
    }

    /**
     * Deletes the QuestionType instance whose identifier is specified in parameter
     *
     * @param nIdQuestionType The identifier of the article QuestionType to delete in the database
     * @param plugin The current plugin using this method
     */
    public static void remove( int nIdQuestionType, Plugin plugin )
    {
        _dao.delete( nIdQuestionType, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of the article QuestionType whose identifier is specified in parameter
     *
     * @param nKey The primary key of the article to find in the database
     * @param plugin The current plugin using this method
     * @return An instance of the QuestionType which corresponds to the key
     */
    public static QuestionType findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Returns QuestionType list
     *
     * @param plugin The current plugin using this method
     * @return the list of the QuestionType of the database in form of a QuestionType Collection object
     */
    public static ReferenceList findAll( Plugin plugin )
    {
        return _dao.findAll( plugin );
    }

    /**
     * Returns QuestionType list
     *
     * @param plugin The current plugin using this method
     * @return the list of the QuestionType of the database in form of a QuestionType Collection object
     */
    public static List<QuestionType> selectQuestionTypeList( Plugin plugin )
    {
        return _dao.getQuestionTypeList( plugin );
    }

    /**
     * Return all the users for a given question type.
     * @param nIdQuestionType The identifier of the question type
     * @param plugin The Plugin using this data access service
     * @return the list of Users which have a role
     */
    public static List<VisitorQuestion> getQuestions( int nIdQuestionType, Plugin plugin )
    {
        return _dao.getQuestions( nIdQuestionType, plugin );
    }
}
