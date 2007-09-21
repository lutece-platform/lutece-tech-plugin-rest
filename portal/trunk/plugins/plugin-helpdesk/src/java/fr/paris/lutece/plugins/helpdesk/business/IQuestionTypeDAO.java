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
 *
 * Interface for QuestionType DAO
 */
public interface IQuestionTypeDAO
{
    /**
     * Calculate a new primary key to add a new QuestionType
     *
     *
     * @param plugin The Plugin using this data access service
     * @return The new key.
     */
    int newPrimaryKey( Plugin plugin );

    /**
      * Delete a record from the table
      * @param nIdQuestionType the QuestionType identifier
     * @param plugin The plugin
      */
    void delete( int nIdQuestionType, Plugin plugin );

    /**
     * Insert a new record in the table.
     * @param questionType The QuestionType object
     * @param plugin The plugin
     */
    void insert( QuestionType questionType, Plugin plugin );

    /**
     * Load the data of QuestionType from the table
     * @param nIdQuestionType The identifier of QuestionType
     * @param plugin The plugin
     * @return the instance of the QuestionType
     */
    QuestionType load( int nIdQuestionType, Plugin plugin );

    /**
     * Update the record in the table
     * @param questionType The instance of the QuestionType to update
     * @param plugin The Plugin using this data access service
     */
    void store( QuestionType questionType, Plugin plugin );

    /**
     * Find all objects
     * @param plugin The Plugin using this data access service
     * @return A Collection of objects
     */
    ReferenceList findAll( Plugin plugin );

    /**
     * Find all objects
     * @param plugin The Plugin using this data access service
     * @return A Collection of objects
     */
    List<QuestionType> getQuestionTypeList( Plugin plugin );

    /**
     * Returns the questions of a given type.
     * @param nIdQuestionType The identifier of the question type
     * @param plugin The Plugin using this data access service
     * @return A Collection of QuestionAnswer objects
     */
    List<VisitorQuestion> getQuestions( int nIdQuestionType, Plugin plugin );
}
