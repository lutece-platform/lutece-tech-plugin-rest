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

import java.util.List;


/**
 * This class represents a Subject object.
 */
public class Subject
{
    private int _nIdSubject;
    private String _strSubject;
    private List _questions;

    /**
     * Creates a new Subject object.
     */
    public Subject(  )
    {
    }

    /**
     * Returns the identifier of the subject
     * @return The identifier of the subject
     */
    public int getIdSubject(  )
    {
        return _nIdSubject;
    }

    /**
     * Sets the identifier of the subject to the specified value
     * @param nIdSubject The new value
     */
    public void setIdSubject( int nIdSubject )
    {
        _nIdSubject = nIdSubject;
    }

    /**
     * Returns the subject string
     * @return The subject string
     */
    public String getSubject(  )
    {
        return _strSubject;
    }

    /**
     * Sets the subject string to the specified value
     * @param strSubject The new value
     */
    public void setSubject( String strSubject )
    {
        _strSubject = strSubject;
    }

    /**
     * Returns the questions associated with the subject
     * @return A List of QuestionAnswer objects.
     */
    public List getQuestions(  )
    {
        return _questions;
    }

    /**
     * Assigns the specified set of questions to the subject
     * @param questions The new List
     */
    public void setQuestions( List questions )
    {
        _questions = questions;
    }
}
