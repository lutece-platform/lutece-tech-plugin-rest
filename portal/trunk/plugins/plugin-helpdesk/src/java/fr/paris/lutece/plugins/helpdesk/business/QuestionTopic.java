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

import fr.paris.lutece.portal.service.rbac.RBACResource;


/**
 * This class represents a type of question.
 */
public class QuestionTopic implements RBACResource
{
    public static final String RESOURCE_TYPE = "QUESTION_TOPIC";
    private int _nIdQuestionTopic;
    private int _nIdQuestionType;
    private String _strQuestionTopic;

    /**
     * Creates a new QuestionType object.
     */
    public QuestionTopic(  )
    {
    }

    /**
     * Returns the identifier of the object
     * @return The identifier
     */
    public int getIdQuestionTopic(  )
    {
        return _nIdQuestionTopic;
    }

    /**
     * Sets the identifier of the object to the specified value
     * @param nIdQuestionTopic The new value
     */
    public void setIdQuestionTopic( int nIdQuestionTopic )
    {
        _nIdQuestionTopic = nIdQuestionTopic;
    }

    /**
     * Returns the identifier of the object
     * @return The identifier
     */
    public int getIdQuestionType(  )
    {
        return _nIdQuestionType;
    }

    /**
     * Sets the identifier of the object to the specified value
     * @param nIdQuestionType The new value
     */
    public void setIdQuestionType( int nIdQuestionType )
    {
        _nIdQuestionType = nIdQuestionType;
    }

    /**
     * Returns the question topic
     * @return A string literal containing the question topic
     */
    public String getQuestionTopic(  )
    {
        return _strQuestionTopic;
    }

    /**
     * Sets the question topic to the specified value
     * @param strQuestionTopic The new value
     */
    public void setQuestionTopic( String strQuestionTopic )
    {
        _strQuestionTopic = strQuestionTopic;
    }

    ////////////////////////////////////////////////////////////////////////////
    // RBAC Resource implementation

    /**
     * Returns the Resource Type Code that identify the resource type
     * @return The Resource Type Code
     */
    public String getResourceTypeCode(  )
    {
        return RESOURCE_TYPE;
    }

    /**
     * Returns the resource Id of the current object
     * @return The resource Id of the current object
     */
    public String getResourceId(  )
    {
        return "" + getIdQuestionTopic(  );
    }
}
