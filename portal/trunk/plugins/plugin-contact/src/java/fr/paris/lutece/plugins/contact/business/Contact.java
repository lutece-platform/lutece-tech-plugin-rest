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
package fr.paris.lutece.plugins.contact.business;


/**
 * This class represents business object Contact
 */
public class Contact
{
    private static final String EMPTY_STRING = "";

    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    private int _nId;
    private int _nContactOrder;
    private String _strName;
    private String _strEmail;

    /**
     * Returns the identifier of this contact.
     *
     * @return the contact identifier
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the identifier of the contact to the specified integer.
     *
     * @param nId the new identifier
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the name of this contact.
     *
     * @return the contact name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the name of the contact to the specified string.
     *
     * @param strName the new name
     */
    public void setName( String strName )
    {
        _strName = ( strName == null ) ? EMPTY_STRING : strName;
    }

    /**
     * Returns the email of this contact.
     *
     * @return the contact email
     */
    public String getEmail(  )
    {
        return _strEmail;
    }

    /**
     * Sets the email of the contact to the specified string.
     *
     * @param strEmail the new email
     */
    public void setEmail( String strEmail )
    {
        _strEmail = ( strEmail == null ) ? EMPTY_STRING : strEmail;
    }

    /**
     * Returns the contact order of this contact.
     *
     * @return the contact order
     */
    public int getContactOrder(  )
    {
        return _nContactOrder;
    }

    /**
     * Sets the contact order of the contact to the specified integer.
     *
     * @param nContactOrder the new contact order
     */
    public void setContactOrder( int nContactOrder )
    {
        _nContactOrder = nContactOrder;
    }
}
