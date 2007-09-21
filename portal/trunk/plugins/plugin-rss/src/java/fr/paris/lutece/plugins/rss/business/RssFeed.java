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
package fr.paris.lutece.plugins.rss.business;

import java.sql.Timestamp;


/**
 * This class represents the business object RssFeed
 */
public class RssFeed
{
    // Variables declarations
    private int _nId;
    private String _strName;
    private String _strUrl;
    private Timestamp _dateLastFetch;
    private int _nLastFetchStatus;
    private String _strLastFetchError;

    /**
     * Returns the Id
     *
     * @return The Id
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the Id
     *
     * @param nId The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the Name
     *
     * @return The Name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the Name
     *
     * @param strName The Name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the Url
     *
     * @return The Url
     */
    public String getUrl(  )
    {
        return _strUrl;
    }

    /**
     * Sets the Url
     *
     * @param strUrl The Url
     */
    public void setUrl( String strUrl )
    {
        _strUrl = strUrl;
    }

    /**
     * Returns the LastFetchDate
     *
     * @return The LastFetchDate
     */
    public Timestamp getLastFetchDate(  )
    {
        return _dateLastFetch;
    }

    /**
     * Sets the LastFetchDate
     *
     * @param dateLastFetch
     */
    public void setLastFetchDate( Timestamp dateLastFetch )
    {
        _dateLastFetch = dateLastFetch;
    }

    public String toString(  )
    {
        return "RSS Feed : " + _strName + "\n - url : " + _strUrl + "\n - last fetch : " + _dateLastFetch +
        "\n - status : " + ( ( _nLastFetchStatus == 0 ) ? "success" : "failed" );
    }

    /**
     * Returns the LastFetchStatus
     *
     * @return The LastFetchStatus
     */
    public int getLastFetchStatus(  )
    {
        return _nLastFetchStatus;
    }

    /**
     * Sets the LastFetchStatus
     *
     * @param nLastFetchStatus The LastFetchStatus
     */
    public void setLastFetchStatus( int nLastFetchStatus )
    {
        _nLastFetchStatus = nLastFetchStatus;
    }

    /**
     * Returns the LastFetchError
     *
     * @return The LastFetchError
     */
    public String getLastFetchError(  )
    {
        return _strLastFetchError;
    }

    /**
     * Sets the LastFetchError
     *
     * @param strLastFetchError The LastFetchError
     */
    public void setLastFetchError( String strLastFetchError )
    {
        _strLastFetchError = strLastFetchError;
    }
}
