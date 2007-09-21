/*
 * Copyright (c) 2002-2006, Mairie de Paris
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
package fr.paris.lutece.plugins.projeau.business;

import java.sql.Date;
import java.sql.Timestamp;


/**
 * This class represents the business object Projeau
 */
public class Projeau
{
    // Variables declarations
    private String _strIdInternet;
    private Timestamp _dateRegistrationDate;
    private String _strTitle;
    private String _strLastName;
    private String _strFirstName;
    private String _strEmail;

    /**
     * Returns the IdInternet
     *
     * @return The IdInternet
     */
    public String getIdInternet(  )
    {
        return _strIdInternet;
    }

    /**
     * Sets the IdInternet
     *
     * @param strIdInternet The IdInternet
     */
    public void setIdInternet( String strIdInternet )
    {
        _strIdInternet = strIdInternet;
    }

    /**
     * Returns the RegistrationDate
     *
     * @return The RegistrationDate as a timestamp
     */
    public Timestamp getRegistrationDate(  )
    {
        return _dateRegistrationDate;
    }

    /**
     * Sets the RegistrationDate
     *
     * @param dateRegistration The RegistrationDate as a timestamp
     */
    public void setRegistrationDate( Timestamp dateRegistration )
    {
        _dateRegistrationDate = dateRegistration;
    }

    /**
     * Returns the Title
     *
     * @return The Title
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * Sets the Title
     *
     * @param strTitle The Title
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Returns the LastName
     *
     * @return The LastName
     */
    public String getLastName(  )
    {
        return _strLastName;
    }

    /**
     * Sets the LastName
     *
     * @param strLastName The LastName
     */
    public void setLastName( String strLastName )
    {
        _strLastName = strLastName;
    }

    /**
     * Returns the FirstName
     *
     * @return The FirstName
     */
    public String getFirstName(  )
    {
        return _strFirstName;
    }

    /**
     * Sets the FirstName
     *
     * @param strFirstName The FirstName
     */
    public void setFirstName( String strFirstName )
    {
        _strFirstName = strFirstName;
    }

    /**
     * Returns the Email
     *
     * @return The Email
     */
    public String getEmail(  )
    {
        return _strEmail;
    }

    /**
     * Sets the  Email
     *
     * @param strEmail The Email
     */
    public void setEmail( String strEmail )
    {
        _strEmail = strEmail;
    }
}
