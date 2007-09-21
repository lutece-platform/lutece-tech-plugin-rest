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
 * This class provides instances management methods (create, find, ...) for QuestionTopic objects
 */
public final class QuestionTopicHome
{
    // Static variable pointed at the DAO instance
    private static IQuestionTopicDAO _dao = (IQuestionTopicDAO) SpringContextService.getPluginBean( "helpdesk",
            "questionTopicDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private QuestionTopicHome(  )
    {
    }

    /**
     * Creation of an instance of questionTopic
     *
     * @param questionTopic The instance of the QuestionTopic which contains the informations to store
     * @param plugin the Plugin
     * @return The  instance of questionTopic which has been created with its primary key.
     */
    public static QuestionTopic create( QuestionTopic questionTopic, Plugin plugin )
    {
        _dao.insert( questionTopic, plugin );

        return questionTopic;
    }

    /**
     * Update of the questionTopic which is specified in parameter
     *
     * @param questionTopic The instance of the QuestionTopic which contains the data to store
     * @param plugin the Plugin
     * @return The instance of the  questionTopic which has been updated
     */
    public static QuestionTopic update( QuestionTopic questionTopic, Plugin plugin )
    {
        _dao.store( questionTopic, plugin );

        return questionTopic;
    }

    /**
     * Remove the questionTopic whose identifier is specified in parameter
     *
     * @param nQuestionTopicId The questionTopic Id
     * @param plugin the Plugin
     */
    public static void remove( int nQuestionTopicId, Plugin plugin )
    {
        _dao.delete( nQuestionTopicId, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a questionTopic whose identifier is specified in parameter
     *
     * @param nKey The questionTopic primary key
     * @param plugin the Plugin
     * @return an instance of QuestionTopic
     */
    public static QuestionTopic findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Loads the data of all the questionTopics and returns them in form of a collection
     *
     * @param plugin the Plugin
     * @return the list which contains the data of all the questionTopics
     */
    public static ReferenceList getQuestionTopicsList( Plugin plugin )
    {
        return _dao.selectQuestionTopicsList( plugin );
    }

    /**
     * Loads the data of all the questionTopics and returns them in form of a collection
     * @param nIdQuestionType The questionType Id
     * @param plugin the Plugin
     * @return the collection which contains the data of all the questionTopics
     */
    public static List<QuestionTopic> getQuestionTopicList( int nIdQuestionType, Plugin plugin )
    {
        return _dao.selectQuestionTopicList( nIdQuestionType, plugin );
    }

    /**
     * Loads the data of all the questionTopics and returns them in form of a collection
     *
     * @param plugin the Plugin
     * @return the list which contains the data of all the questionTopics
     */
    public static List<QuestionTopic> selectTopicList( Plugin plugin )
    {
        return _dao.selectTopicList( plugin );
    }
}
