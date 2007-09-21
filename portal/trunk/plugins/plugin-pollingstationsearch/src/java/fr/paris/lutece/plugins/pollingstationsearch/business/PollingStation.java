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
 * This class represents the business object PollingStation
 */
public class PollingStation
{
    // Variables declarations 
    private int _nPollingStationId;
    private String _strPollingStationUrbanDistrict;
    private String _strPollingStationNumber;
    private String _strPollingStationName;
    private String _strPollingStationLocationSuffix;
    private String _strPollingStationAddrNumber;
    private String _strPollingStationAddr;
    private String _strPollingStationAddrUrbanDistrict;

    /**
     * Returns the Id
     *
     * @return The Id
     */
    public int getId(  )
    {
        return _nPollingStationId;
    }

    /**
     * Sets the Id
     *
     * @param nPollingStationId The Id
     */
    public void setId( int nPollingStationId )
    {
        _nPollingStationId = nPollingStationId;
    }

    /**
     * Returns the PollingStationUrbanDistrict
     *
     * @return The PollingStationUrbanDistrict
     */
    public String getPollingStationUrbanDistrict(  )
    {
        return _strPollingStationUrbanDistrict;
    }

    /**
     * Sets the PollingStationUrbanDistrict
     *
     * @param strPollingStationUrbanDistrict The PollingStationUrbanDistrict
     */
    public void setPollingStationUrbanDistrict( String strPollingStationUrbanDistrict )
    {
        _strPollingStationUrbanDistrict = strPollingStationUrbanDistrict;
    }

    /**
     * Returns the PollingStationNumber
     *
     * @return The PollingStationNumber
     */
    public String getPollingStationNumber(  )
    {
        return _strPollingStationNumber;
    }

    /**
     * Sets the PollingStationNumber
     *
     * @param strPollingStationNumber The PollingStationNumber
     */
    public void setPollingStationNumber( String strPollingStationNumber )
    {
        _strPollingStationNumber = strPollingStationNumber;
    }

    /**
     * Returns the PollingStationName
     *
     * @return The PollingStationName
     */
    public String getPollingStationName(  )
    {
        return _strPollingStationName;
    }

    /**
     * Sets the PollingStationName
     *
     * @param strPollingStationName The PollingStationName
     */
    public void setPollingStationName( String strPollingStationName )
    {
        _strPollingStationName = strPollingStationName;
    }

    /**
     * Returns the PollingStationLocationComplement
     *
     * @return The PollingStationLocationComplement
     */
    public String getPollingStationLocationComplement(  )
    {
        return _strPollingStationLocationSuffix;
    }

    /**
     * Sets the PollingStationLocationComplement
     *
     * @param strPollingStationLocationComplement The PollingStationLocationComplement
     */
    public void setPollingStationLocationComplement( String strPollingStationLocationComplement )
    {
        _strPollingStationLocationSuffix = strPollingStationLocationComplement;
    }

    /**
     * Returns the PollingStationAddrNumber
     *
     * @return The PollingStationAddrNumber
     */
    public String getPollingStationAddrNumber(  )
    {
        return _strPollingStationAddrNumber;
    }

    /**
     * Sets the PollingStationAddrNumber
     *
     * @param strPollingStationAddrNumber The PollingStationAddrNumber
     */
    public void setPollingStationAddrNumber( String strPollingStationAddrNumber )
    {
        _strPollingStationAddrNumber = strPollingStationAddrNumber;
    }

    /**
     * Returns the PollingStationAddr
     *
     * @return The PollingStationAddr
     */
    public String getPollingStationAddr(  )
    {
        return _strPollingStationAddr;
    }

    /**
     * Sets the PollingStationAddr
     *
     * @param strPollingStationAddr The PollingStationAddr
     */
    public void setPollingStationAddr( String strPollingStationAddr )
    {
        _strPollingStationAddr = strPollingStationAddr;
    }

    /**
     * Returns the PollingStationAddrUrbanDistrict
     *
     * @return The PollingStationAddrUrbanDistrict
     */
    public String getPollingStationAddrUrbanDistrict(  )
    {
        return _strPollingStationAddrUrbanDistrict;
    }

    /**
     * Sets the PollingStationAddrUrbanDistrict
     *
     * @param strPollingStationAddrUrbanDistrict The PollingStationAddrUrbanDistrict
     */
    public void setPollingStationAddrUrbanDistrict( String strPollingStationAddrUrbanDistrict )
    {
        _strPollingStationAddrUrbanDistrict = strPollingStationAddrUrbanDistrict;
    }
}
