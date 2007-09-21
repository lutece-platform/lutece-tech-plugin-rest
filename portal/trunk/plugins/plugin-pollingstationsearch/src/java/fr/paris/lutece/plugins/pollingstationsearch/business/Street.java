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
package fr.paris.lutece.plugins.pollingstationsearch.business;


/**
 * This class represents the business object street.
 */
public class Street
{
    private String _strStreetId;
    private String _strStreetUrbanDistrict;
    private String _strLongStreetName;
    private String _strStreetType;
    private String _strShortStreetName;

    /**
     * Returns the street id.
     * @return the street id.
     */
    public String getStreetId(  )
    {
        return _strStreetId;
    }

    /**
     * Sets the street id.
     * @param strStreetId the new value.
     */
    public void setStreetId( String strStreetId )
    {
        _strStreetId = strStreetId;
    }

    /**
     * Returns the street urban district.
     * @return the street urban district.
     */
    public String getStreetUrbanDistrict(  )
    {
        return _strStreetUrbanDistrict;
    }

    /**
     * Sets the street urban district.
     * @param strStreetUrbanDistrict the new value.
     */
    public void setStreetUrbanDistrict( String strStreetUrbanDistrict )
    {
        _strStreetUrbanDistrict = strStreetUrbanDistrict;
    }

    /**
     * Returns the long street name.
     * @return the long street name.
     */
    public String getLongStreetName(  )
    {
        return _strLongStreetName;
    }

    /**
     * Sets the long street name.
     * @param strLongStreetName the new value.
     */
    public void setLongStreetName( String strLongStreetName )
    {
        _strLongStreetName = strLongStreetName;
    }

    /**
     * Returns the short version of the street name.
     * @return the short version of the street name.
     */
    public String getShortStreetName(  )
    {
        return _strShortStreetName;
    }

    /**
     * Sets the short version of the street name.
     * @param strShortStreetName the new value.
     */
    public void setShortStreetName( String strShortStreetName )
    {
        _strShortStreetName = strShortStreetName;
    }

    /**
     * Returns the street type.
     * @return the street type.
     */
    public String getStreetType(  )
    {
        return _strStreetType;
    }

    /**
     * Sets the street type.
     * @param strStreetType the new value.
     */
    public void setStreetType( String strStreetType )
    {
        _strStreetType = strStreetType;
    }
}
