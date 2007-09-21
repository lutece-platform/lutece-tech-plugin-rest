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
package fr.paris.lutece.plugins.ldapdirectory.business;


/**
 * This is the business class for the object LdapEntity
 */
public class LdapEntity
{
    // Variables declarations
    private String _strName;
    private String _strGivenName;
    private String _strCompleteName;
    private String _strSociety;
    private String _strService;
    private String _strTelephoneNumber;
    private String _strRnisNumber;
    private String _strMail;
    private String _strTitle;
    private String _strFloor;
    private String _strRoomNumber;
    private String _strPostalAddress;
    private String _strPostalCode;
    private String _strTown;
    private String _strDepartment;
    private String _strModificationDate;
    private String _strType;

    /**
     * Returns the Name
     * @return The Name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the Name
     * @param strName The Name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the GivenName
     * @return The GivenName
     */
    public String getGivenName(  )
    {
        return _strGivenName;
    }

    /**
     * Sets the GivenName
     * @param strGivenName The GivenName
     */
    public void setGivenName( String strGivenName )
    {
        _strGivenName = strGivenName;
    }

    /**
     * Returns the CompleteName
     * @return The CompleteName
     */
    public String getCompleteName(  )
    {
        return _strCompleteName;
    }

    /**
     * Sets the CompleteName
     * @param strCompleteName The CompleteName
     */
    public void setCompleteName( String strCompleteName )
    {
        _strCompleteName = strCompleteName;
    }

    /**
     * Returns the Society
     * @return The Society
     */
    public String getSociety(  )
    {
        return _strSociety;
    }

    /**
     * Sets the Society
     * @param strSociety The Society
     */
    public void setSociety( String strSociety )
    {
        _strSociety = strSociety;
    }

    /**
     * Returns the Service
     * @return The Service
     */
    public String getService(  )
    {
        return _strService;
    }

    /**
     * Sets the Service
     * @param strService The Service
     */
    public void setService( String strService )
    {
        _strService = strService;
    }

    /**
     * Returns the TelephoneNumber
     * @return The TelephoneNumber
     */
    public String getTelephoneNumber(  )
    {
        return _strTelephoneNumber;
    }

    /**
     * Sets the TelephoneNumber
     * @param strTelephoneNumber The TelephoneNumber
     */
    public void setTelephoneNumber( String strTelephoneNumber )
    {
        _strTelephoneNumber = strTelephoneNumber;
    }

    /**
     * Returns the RnisNumber
     * @return The RnisNumber
     */
    public String getRnisNumber(  )
    {
        return _strRnisNumber;
    }

    /**
     * Sets the RnisNumber
     * @param strRnisNumber The RnisNumber
     */
    public void setRnisNumber( String strRnisNumber )
    {
        _strRnisNumber = strRnisNumber;
    }

    /**
     * Returns the Mail
     * @return The Mail
     */
    public String getMail(  )
    {
        return _strMail;
    }

    /**
     * Sets the Mail
     * @param strMail The Mail
     */
    public void setMail( String strMail )
    {
        _strMail = strMail;
    }

    /**
     * Returns the Title
     * @return The Title
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * Sets the Title
     * @param strTitle The Title
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Returns the Floor
     * @return The Floor
     */
    public String getFloor(  )
    {
        return _strFloor;
    }

    /**
     * Sets the Floor
     * @param strFloor The Floor
     */
    public void setFloor( String strFloor )
    {
        _strFloor = strFloor;
    }

    /**
     * Returns the RoomNumber
     * @return The RoomNumber
     */
    public String getRoomNumber(  )
    {
        return _strRoomNumber;
    }

    /**
     * Sets the RoomNumber
     * @param strRoomNumber The RoomNumber
     */
    public void setRoomNumber( String strRoomNumber )
    {
        _strRoomNumber = strRoomNumber;
    }

    /**
     * Returns the PostalAddress
     * @return The PostalAddress
     */
    public String getPostalAddress(  )
    {
        return _strPostalAddress;
    }

    /**
     * Sets the PostalAddress
     * @param strPostalAddress The PostalAddress
     */
    public void setPostalAddress( String strPostalAddress )
    {
        _strPostalAddress = strPostalAddress;
    }

    /**
     * Returns the PostalCode
     * @return The PostalCode
     */
    public String getPostalCode(  )
    {
        return _strPostalCode;
    }

    /**
     * Sets the PostalCode
     * @param strPostalCode The PostalCode
     */
    public void setPostalCode( String strPostalCode )
    {
        _strPostalCode = strPostalCode;
    }

    /**
     * Returns the Town
     * @return The Town
     */
    public String getTown(  )
    {
        return _strTown;
    }

    /**
     * Sets the Town
     * @param strTown The Town
     */
    public void setTown( String strTown )
    {
        _strTown = strTown;
    }

    /**
     * Returns the Department
     * @return The Department
     */
    public String getDepartment(  )
    {
        return _strDepartment;
    }

    /**
     * Sets the Department
     * @param strDepartment The Department
     */
    public void setDepartment( String strDepartment )
    {
        _strDepartment = strDepartment;
    }

    /**
     * Returns the ModificationDate
     * @return The ModificationDate
     */
    public String getModificationDate(  )
    {
        return _strModificationDate;
    }

    /**
     * Sets the ModificationDate
     * @param strModificationDate The ModificationDate
     */
    public void setModificationDate( String strModificationDate )
    {
        _strModificationDate = strModificationDate;
    }

    /**
    * Returns the Type
    * @return The Type
    */
    public String getType(  )
    {
        return _strType;
    }

    /**
     * Sets the Type
     * @param strType The Type of the entity
     */
    public void setType( String strType )
    {
        _strType = strType;
    }
}
