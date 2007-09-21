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
package fr.paris.lutece.plugins.myapps.business;


/**
 * This class represents the business object MyApps
 */
public class MyApps
{
    // Variables declarations
    private int _nIdApplication;
    private String _strName;
    private String _strDescription;
    private String _strUrl;
    private String _strCode;
    private String _strPassword;
    private String _strData;
    private String _strCodeHeading;
    private String _strDataHeading;
    private String _strIconMimeType;
    private byte[] _strIconContent;

    /**
     * Returns the IdApplication
     * @return The IdApplication
     */
    public int getIdApplication(  )
    {
        return _nIdApplication;
    }

    /**
     * Sets the IdApplication
     * @param nIdApplication The IdApplication
     */
    public void setIdApplication( int nIdApplication )
    {
        _nIdApplication = nIdApplication;
    }

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
     * Returns the Description
     * @return The Description
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     * @param strDescription The Description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Returns the Url
     * @return The Url
     */
    public String getUrl(  )
    {
        return _strUrl;
    }

    /**
     * Sets the Url
     * @param strUrl The Url
     */
    public void setUrl( String strUrl )
    {
        _strUrl = strUrl;
    }

    /**
     * Returns the Code
     * @return The Code
     */
    public String getCode(  )
    {
        return _strCode;
    }

    /**
     * Sets the Code
     * @param strCode The Code
     */
    public void setCode( String strCode )
    {
        _strCode = strCode;
    }

    /**
     * Returns the Password
     * @return The Password
     */
    public String getPassword(  )
    {
        return _strPassword;
    }

    /**
     * Sets the Password
     * @param strPassword The Password
     */
    public void setPassword( String strPassword )
    {
        _strPassword = strPassword;
    }

    /**
     * Returns the Data
     * @return The Data
     */
    public String getData(  )
    {
        return _strData;
    }

    /**
     * Sets the Data
     * @param strData The Data
     */
    public void setData( String strData )
    {
        _strData = strData;
    }

    /**
     * Returns the CodeHeading
     * @return The CodeHeading
     */
    public String getCodeHeading(  )
    {
        return _strCodeHeading;
    }

    /**
     * Sets the CodeHeading
     * @param strCodeHeading The CodeHeading
     */
    public void setCodeHeading( String strCodeHeading )
    {
        _strCodeHeading = strCodeHeading;
    }

    /**
     * Returns the DataHeading
     * @return The DataHeading
     */
    public String getDataHeading(  )
    {
        return _strDataHeading;
    }

    /**
     * Sets the DataHeading
     * @param strDataHeading The DataHeading
     */
    public void setDataHeading( String strDataHeading )
    {
        _strDataHeading = strDataHeading;
    }

    /**
    * Returns the icon mime type
    * @return The IconMimeType
    */
    public String getIconMimeType(  )
    {
        return _strIconMimeType;
    }

    /**
     * Sets the Icon mime type
     * @param strIconMimeType The mime type
     */
    public void setIconMimeType( String strIconMimeType )
    {
        _strIconMimeType = strIconMimeType;
    }

    /**
     * Returns the IconContent
     *
     * @return The IconContent
     */
    public byte[] getIconContent(  )
    {
        return _strIconContent;
    }

    /**
     * Sets the IconContent
     *
     * @param strIconContent The IconContent
     */
    public void setIconContent( byte[] strIconContent )
    {
        _strIconContent = strIconContent;
    }
}
