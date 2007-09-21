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

import java.util.List;


/**
* IQuestionTopicDAO Interface
*/
public interface IQuestionTopicDAO
{
    /**
     * Insert a new record in the table.
     * @param questionTopic instance of the QuestionTopic object to inssert
     * @param plugin the Plugin
     */
    void insert( QuestionTopic questionTopic, Plugin plugin );

    /**
    * Update the record in the table
    *
    * @param questionTopic the reference of the QuestionTopic
    * @param plugin the Plugin
    */
    void store( QuestionTopic questionTopic, Plugin plugin );

    /**
     * Delete a record from the table
     *
     * @param nIdQuestionTopic int identifier of the QuestionTopic to delete
     * @param plugin the Plugin
     */
    void delete( int nIdQuestionTopic, Plugin plugin );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * load the data of the right from the table
     *
     * @param nKey The identifier of the questionTopic
     * @param plugin the Plugin
     * @return The instance of the questionTopic
     */
    QuestionTopic load( int nKey, Plugin plugin );

    /**
    * Loads the data of all the questionTopics and returns them in form of a collection
    *
    * @param plugin the Plugin
    * @return the collection which contains the data of all the questionTopics
    */
    ReferenceList selectQuestionTopicsList( Plugin plugin );

    /**
    * Loads the data of all the questionTopics and returns them in form of a collection
    * @param nIdQuestionType int identifier of the QuestionType
    * @param plugin the Plugin
    * @return the collection which contains the data of all the questionTopics
    */
    List<QuestionTopic> selectQuestionTopicList( int nIdQuestionType, Plugin plugin );

    /**
     * Load the data of all the questionTopics and returns them in form of a collection
     * @param plugin the Plugin
     * @return The List which contains the data of all the questionTopics
     */
    List<QuestionTopic> selectTopicList( Plugin plugin );
}
