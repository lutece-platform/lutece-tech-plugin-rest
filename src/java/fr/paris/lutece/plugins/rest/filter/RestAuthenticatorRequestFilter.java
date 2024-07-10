/*
 * Copyright (c) 2002-2024, City of Paris
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
package fr.paris.lutece.plugins.rest.filter;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.plugins.rest.service.RestRequestAuthenticator;
import fr.paris.lutece.util.signrequest.RequestAuthenticator;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class RestAuthenticatorRequestFilter implements ContainerRequestFilter 
{

    private static final Logger LOGGER = LogManager.getLogger( RestConstants.REST_LOGGER );
    private static final String SECURITY_ACTIVATED = "rest.security.activated";
    
    @Inject 
    private HttpServletRequest _httpRequest;
    
    @Inject
    @RestRequestAuthenticator
    private RequestAuthenticator _requestAuthenticator;

    private final Config _config;
    
    public RestAuthenticatorRequestFilter( ) {
        this._config = ConfigProvider.getConfig( );
    }
    
    @Override
    public void filter( ContainerRequestContext requestContext ) throws IOException
    {
        if ( _config.getOptionalValue( SECURITY_ACTIVATED, Boolean.class ).orElse( false ) )
        {
            if ( isRequestAuthenticated( _httpRequest ) )
            {
                if ( LOGGER.isDebugEnabled( ) )
                {
                    LOGGER.debug(
                            "LuteceRestAuthenticatorRequestFilter processing request : " + _httpRequest.getMethod( ) + " " + _httpRequest.getContextPath( )
                                    + _httpRequest.getServletPath( ) + _httpRequest.getPathInfo( ) );
                }
            }
            else
            {
                requestContext.abortWith( Response.status( Response.Status.UNAUTHORIZED ).build( ) );
            }
        }
    }
    
    private boolean isRequestAuthenticated(HttpServletRequest httpRequest) {
        return _requestAuthenticator.isRequestAuthenticated( httpRequest );
    }

}
