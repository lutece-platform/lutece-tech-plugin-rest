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
 * This class represents the business object Address
 */
public class Address
{
    // Variables declarations 
    private int _nAddrId;
    private String _strAddrUrbanDistrict;
    private String _strAddrNumber;
    private String _strAddrNumberSuffix;
    private String _strStreetId;
    private String _strAddrStreetName;
    private int _nPollingStationId;

    /**
     * Returns the Id
     *
     * @return The Id
     */
    public int getId(  )
    {
        return _nAddrId;
    }

    /**
     * Sets the Id
     *
     * @param nAddrId The Id
     */
    public void setId( int nAddrId )
    {
        _nAddrId = nAddrId;
    }

    /**
     * Returns the AddrUrbanDistrict
     *
     * @return The AddrUrbanDistrict
     */
    public String getAddrUrbanDistrict(  )
    {
        return _strAddrUrbanDistrict;
    }

    /**
     * Sets the AddrUrbanDistrict
     *
     * @param strAddrUrbanDistrict The AddrUrbanDistrict
     */
    public void setAddrUrbanDistrict( String strAddrUrbanDistrict )
    {
        _strAddrUrbanDistrict = strAddrUrbanDistrict;
    }

    /**
     * Returns the AddrNumber
     *
     * @return The AddrNumber
     */
    public String getAddrNumber(  )
    {
        return _strAddrNumber;
    }

    /**
     * Sets the AddrNumber
     *
     * @param strAddrNumber The AddrNumber
     */
    public void setAddrNumber( String strAddrNumber )
    {
        _strAddrNumber = strAddrNumber;
    }

    /**
     * Returns the AddrNumberSuffix
     *
     * @return The AddrNumberSuffix
     */
    public String getAddrNumberSuffix(  )
    {
        return _strAddrNumberSuffix;
    }

    /**
     * Sets the AddrNumberSuffix
     *
     * @param strAddrNumberSuffix the address suffix
     */
    public void setAddrNumberSuffix( String strAddrNumberSuffix )
    {
        _strAddrNumberSuffix = strAddrNumberSuffix;
    }

    /**
     * Returns the StreetId
     *
     * @return The StreetId
     */
    public String getStreetId(  )
    {
        return _strStreetId;
    }

    /**
     * Sets the StreetId
     *
     * @param strStreetId The StreetId
     */
    public void setStreetId( String strStreetId )
    {
        _strStreetId = strStreetId;
    }

    /**
     * Returns the PollingStationId
     *
     * @return The PollingStationId
     */
    public int getPollingStationId(  )
    {
        return _nPollingStationId;
    }

    /**
     * Sets the PollingStationId
     *
     * @param nPollingStationId The PollingStationId
     */
    public void setPollingStationId( int nPollingStationId )
    {
        _nPollingStationId = nPollingStationId;
    }

    /**
     * Returns the StreetId
     *
     * @return The street name
     */
    public String getAddrStreetName(  )
    {
        return _strAddrStreetName;
    }

    /**
     * Sets the StreetId
     *
     * @param strAddrStreetName The street name
     */
    public void setAddrStreetName( String strAddrStreetName )
    {
        _strAddrStreetName = strAddrStreetName;
    }
}
