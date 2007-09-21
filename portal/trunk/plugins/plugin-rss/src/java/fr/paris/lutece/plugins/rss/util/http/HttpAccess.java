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
package fr.paris.lutece.plugins.rss.util.http;

import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;


/**
 * Http net Object Accessor <br/>
 *
 * <b>Properties read here: </b>
 * <ul>
 * <li><i>base </i> <b><code>.proxyHost</code> </b>&nbsp;: The host proxy to
 * use for http access.</li>
 * <li><i>base </i> <b><code>.proxyPort</code> </b>&nbsp;: The port proxy to
 * use for http access.</li>
 * <li><i>base </i> <b><code>.proxyUserName</code> </b>&nbsp;: The proxy username to
 * use for http access.</li>
 * <li><i>base </i> <b><code>.proxyPassword</code> </b>&nbsp;: The proxy password to
 * use for http access.</li>
 * <li><i>base </i> <b><code>.hostName</code> </b>&nbsp;: The host name to
 * use for http access.</li>
 * <li><i>base </i> <b><code>.domainName</code> </b>&nbsp;: The domain name to
 * use for http access.</li>
 * <li><i>base </i> <b><code>.realm</code> </b>&nbsp;: The realm to
 * use for http access.</li>
 * <li><i>base </i> <b><code>.defaultEncoding</code> </b>&nbsp;: Encoding to
 * use if no encoding will be give in the HTTP header.</li>
 * <li><i>base </i> <b><code>.defaultBufferSize</code> </b>&nbsp;: Default
 * buffer size to use if no length will be give in the HTTP header.</li>
 * </ul>
 */
public class HttpAccess
{
    // proxy authentication settings
    private static final String PROPERTY_HTTP_ACCESS = ".httpAccess";
    private static final String PROPERTY_FRAGMENT_PROXY_HOST = ".proxyHost";
    private static final String PROPERTY_FRAGMENT_PROXY_PORT = ".proxyPort";
    private static final String PROPERTY_FRAGMENT_PROXY_USERNAME = ".proxyUserName";
    private static final String PROPERTY_FRAGMENT_PROXY_PASSWORD = ".proxyPassword";
    private static final String PROPERTY_FRAGMENT_HOST_NAME = ".hostName";
    private static final String PROPERTY_FRAGMENT_DOMAIN_NAME = ".domainName";
    private static final String PROPERTY_FRAGMENT_REALM = ".realm";

    // private fields
    private String _strConfigPropertiesPrefix;
    private String _strProxyHost;
    private String _strProxyPort;
    private String _strProxyUserName;
    private String _strProxyPassword;
    private String _strHostName;
    private String _strDomainName;
    private String _strRealm;

    /**
     * Initialize the object
     * @param strConfigPropertiesPrefix The prefix of all configuration properties needed by the Http client
     */
    public void init( String strConfigPropertiesPrefix )
    {
        _strConfigPropertiesPrefix = strConfigPropertiesPrefix;

        String strBase = strConfigPropertiesPrefix + PROPERTY_HTTP_ACCESS;
        _strProxyHost = AppPropertiesService.getProperty( strBase + PROPERTY_FRAGMENT_PROXY_HOST );
        _strProxyPort = AppPropertiesService.getProperty( strBase + PROPERTY_FRAGMENT_PROXY_PORT );

        _strProxyUserName = AppPropertiesService.getProperty( strBase + PROPERTY_FRAGMENT_PROXY_USERNAME );
        _strProxyPassword = AppPropertiesService.getProperty( strBase + PROPERTY_FRAGMENT_PROXY_PASSWORD );
        _strHostName = AppPropertiesService.getProperty( strBase + PROPERTY_FRAGMENT_HOST_NAME );
        _strDomainName = AppPropertiesService.getProperty( strBase + PROPERTY_FRAGMENT_DOMAIN_NAME );
        _strRealm = AppPropertiesService.getProperty( strBase + PROPERTY_FRAGMENT_REALM );
    }

    /**
     * Send a GET HTTP request to an Url and return the response content.
     * @param strUrl The Url to access
     * @return The response content of the Get request to the given Url
     * @throws fr.paris.lutece.plugins.rss.util.http.HttpAccessException if there is a problem to access to the given Url
     */
    public String doGet( String strUrl ) throws HttpAccessException
    {
        String strResponseBody = null;

        try
        {
            // Create an instance of HttpClient.
            HttpClient client = new HttpClient(  );

            HttpMethodBase method = new GetMethod( strUrl );
            method.setFollowRedirects( true );

            // if proxy host and port found, set the correponding elements
            if ( ( _strProxyHost != null ) && ( !_strProxyHost.equals( "" ) ) && ( _strProxyPort != null ) &&
                    ( !_strProxyPort.equals( "" ) ) )
            {
                client.getHostConfiguration(  ).setProxy( _strProxyHost, Integer.parseInt( _strProxyPort ) );
            }

            Credentials cred = null;

            //  if hostname and domain name found, consider we are in NTLM authentication scheme
            // else if only username and password found, use simple UsernamePasswordCredentials
            if ( ( _strHostName != null ) && ( _strDomainName != null ) )
            {
                cred = new NTCredentials( _strProxyUserName, _strProxyPassword, _strHostName, _strDomainName );
            }
            else if ( ( _strProxyUserName != null ) && ( _strProxyPassword != null ) )
            {
                cred = new UsernamePasswordCredentials( _strProxyUserName, _strProxyPassword );
            }

            if ( cred != null )
            {
                client.getState(  ).setProxyCredentials( _strRealm, _strProxyHost, cred );
                client.getState(  ).setAuthenticationPreemptive( true );
                method.setDoAuthentication( true );
            }

            try
            {
                client.executeMethod( method );
                strResponseBody = method.getResponseBodyAsString(  );
            }
            catch ( HttpException e )
            {
                String strError = "HttpAccess error - config : " + _strConfigPropertiesPrefix +
                    " - Http error connecting to '" + strUrl + "' : ";
                AppLogService.error( strError + e.getMessage(  ), e );
                throw new HttpAccessException( strError + e.getMessage(  ), e );
            }
            catch ( IOException e )
            {
                String strError = "HttpAccess error - config : " + _strConfigPropertiesPrefix +
                    " - Unable to connect to '" + strUrl + "' : ";
                AppLogService.error( strError + e.getMessage(  ), e );
                throw new HttpAccessException( strError + e.getMessage(  ), e );
            }
            finally
            {
                // Release the connection.
                method.releaseConnection(  );
            }
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage(  ), e );
            throw new HttpAccessException( e.getMessage(  ), e );
        }

        return strResponseBody;
    }
}
