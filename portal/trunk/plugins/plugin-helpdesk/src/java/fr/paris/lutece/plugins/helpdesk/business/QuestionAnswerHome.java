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

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...)
 * for QuestionAnswer objects
 */
public final class QuestionAnswerHome
{
    // Static variable pointed at the DAO instance
    private static IQuestionAnswerDAO _dao = (IQuestionAnswerDAO) SpringContextService.getPluginBean( "helpdesk",
            "questionAnswerDAO" );

    /**
     * Private constructor - this class need not be instantiated.
     */
    private QuestionAnswerHome(  )
    {
    }

    /**
     * Creation of an instance of an article QuestionAnswer
     *
     * @param questionanswer An instance of the QuestionAnswer which contains
     *        the informations to store
     * @param plugin The current plugin using this method
     * @return The instance of the QuestionAnswer which has been created
     */
    public static QuestionAnswer create( QuestionAnswer questionanswer, Plugin plugin )
    {
        _dao.insert( questionanswer, plugin );

        return questionanswer;
    }

    /**
     * Updates of the QuestionAnswer instance specified in parameter
     *
     * @param questionanswer An instance of the QuestionAnswer which contains
     *        the informations to store
     * @param plugin The current plugin using this method
     * @return The instance of the QuestionAnswer which has been updated.
     */
    public static QuestionAnswer update( QuestionAnswer questionanswer, Plugin plugin )
    {
        _dao.store( questionanswer, plugin );

        return questionanswer;
    }

    /**
     * Deletes the QuestionAnswer instance whose identifier is specified in
     * parameter
     *
     * @param nIdQuestionAnswer The identifier of the article QuestionAnswer to delete in
     *        the database
     * @param plugin The current plugin using this method
     */
    public static void remove( int nIdQuestionAnswer, Plugin plugin )
    {
        _dao.delete( nIdQuestionAnswer, plugin );
    }

    /**
     * Deletes the QuestionAnswer instance whose identifier is specified in
     * parameter
     *
     * @param nIdSubject The identifier of the article QuestionAnswer to delete in
     *        the database
     * @param plugin The current plugin using this method
     */
    public static void removeBySubject( int nIdSubject, Plugin plugin )
    {
        _dao.deleteBySubject( nIdSubject, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of the article QuestionAnswer whose identifier is
     * specified in parameter
     *
     * @param nKey The primary key of the article to find in the database
     * @param plugin The current plugin using this method
     * @return An instance of the QuestionAnswer which corresponds to the key
     */
    public static QuestionAnswer findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Returns QuestionAnswer list
     *
     * @param plugin The current plugin using this method
     * @return the list of the QuestionAnswer of the database in form of a
     *         QuestionAnswer Collection object
     */
    public static List<QuestionAnswer> findAll( Plugin plugin )
    {
        return _dao.findAll( plugin );
    }

    /**
     * Find questions containing a list of keywords
     * @param strKeywords The keywords
     * @param plugin The current plugin using this method
     * @return A Collection containing the results
     */
    public static List<QuestionAnswer> findByKeywords( String strKeywords, Plugin plugin )
    {
        return _dao.findByKeywords( strKeywords, plugin );
    }

    /**
     * Count QuestionAnswewr for one Subject
     *
     * @param nIdSubject ID
     * @param plugin The current plugin using this method
     * @return The count of the QuestionAnswer for this ID.
     */
    public static int countbySubject( int nIdSubject, Plugin plugin )
    {
        return _dao.countbySubject( nIdSubject, plugin );
    }
}
