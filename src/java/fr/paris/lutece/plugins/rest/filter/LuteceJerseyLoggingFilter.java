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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.message.internal.ReaderWriter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

public class LuteceJerseyLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter
{
    private static final Logger LOGGER = Logger.getLogger( RestConstants.REST_LOGGER );
    private static final ObjectMapper mapper = new ObjectMapper( ).enable( SerializationFeature.INDENT_OUTPUT );
    private static final String HTTP_RESPONSE_LABEL = "HTTP RESPONSE\n";
    private static final String HTTP_REQUEST_LABEL = "HTTP REQUEST\n";
    private static final String HEADERS_LABEL = "Header: ";
    private static final String DATE_LABEL = "Date: ";
    private static final String STATUS_LABEL = "Status: ";
    private static final String METHOD_LABEL = "Method: ";
    private static final String ENDPOINT_LABEL = "Endpoint: ";
    private static final String PATH_LABEL = "Path: ";
    private static final String ENTITY_LABEL = "Entity: ";
    private static final String USER_LABEL = "User: ";
    private static final String NO_USER = "no user";
    private static final String DATE_PATTERN = "dd-MM-yyyy HH:mm:ss.SSS";

    public LuteceJerseyLoggingFilter( )
    {
    }

    @Override
    public void filter( ContainerRequestContext requestContext ) throws IOException
    {
        LOGGER.debug( HTTP_REQUEST_LABEL + this.formatRequest( requestContext ) );
    }

    @Override
    public void filter( ContainerRequestContext requestContext, ContainerResponseContext responseContext ) throws IOException
    {

        final String formattedResponse = this.formatResponse( responseContext );
        if ( responseContext.getStatus( ) > 300 )
        {
            if ( !LOGGER.isDebugEnabled( ) )
            {
                LOGGER.info( HTTP_REQUEST_LABEL + this.formatRequest( requestContext ) );
            }
            LOGGER.info( HTTP_RESPONSE_LABEL + formattedResponse );
        }
        else
        {
            LOGGER.debug( HTTP_RESPONSE_LABEL + formattedResponse );
        }
    }

    private String formatResponse( final ContainerResponseContext responseContext )
    {
        final StringBuilder sb = new StringBuilder( );
        if ( responseContext.getDate( ) != null )
        {
            sb.append( DATE_LABEL ).append( DateFormatUtils.format( responseContext.getDate( ), DATE_PATTERN ) ).append( "\n" );
        }
        sb.append( STATUS_LABEL ).append( responseContext.getStatus( ) ).append( "\n" );
        sb.append( this.formatHeaders( responseContext.getStringHeaders( ) ) ).append( "\n" );
        if ( responseContext.getEntity( ) != null )
        {
            sb.append( ENTITY_LABEL ).append( this.prettyPrint( responseContext.getEntity( ) ) );
        }
        return sb.toString( );
    }

    private String formatRequest( final ContainerRequestContext requestContext )
    {
        final StringBuilder sb = new StringBuilder( );
        if ( requestContext.getDate( ) != null )
        {
            sb.append( DATE_LABEL ).append( DateFormatUtils.format( requestContext.getDate( ), DATE_PATTERN ) ).append( "\n" );
        }
        sb.append( METHOD_LABEL ).append( requestContext.getMethod( ) ).append( "\n" );
        sb.append( ENDPOINT_LABEL ).append( requestContext.getUriInfo( ).getBaseUri( ) ).append( "\n" );
        sb.append( PATH_LABEL ).append( requestContext.getUriInfo( ).getPath( ) ).append( "\n" );
        sb.append( USER_LABEL )
                .append( requestContext.getSecurityContext( ).getUserPrincipal( ) == null ? NO_USER : requestContext.getSecurityContext( ).getUserPrincipal( ) )
                .append( "\n" );
        sb.append( this.formatHeaders( requestContext.getHeaders( ) ) ).append( "\n" );
        sb.append( ENTITY_LABEL ).append( this.formatEntityBody( requestContext ) );
        return sb.toString( );
    }

    private String formatHeaders( final MultivaluedMap<String, String> headers )
    {
        return headers.entrySet( ).stream( ).map( entry -> HEADERS_LABEL + entry.getKey( ) + ": " + entry.getValue( ) ).collect( Collectors.joining( "\n" ) );
    }

    private String formatEntityBody( final ContainerRequestContext requestContext )
    {

        try ( final InputStream in = requestContext.getEntityStream( ) )
        {
            final StringBuilder b = new StringBuilder( );
            final ByteArrayOutputStream out = new ByteArrayOutputStream( );
            ReaderWriter.writeTo( in, out );
            final byte [ ] requestEntity = out.toByteArray( );
            if ( requestEntity.length > 0 )
            {
                b.append( new String( requestEntity ) );
            }
            requestContext.setEntityStream( new ByteArrayInputStream( requestEntity ) );
            return this.prettyPrint( b.toString( ) );
        }
        catch( final Exception ex )
        {
            LOGGER.debug( "Error while reading request entity", ex );
        }

        return "";
    }

    public String prettyPrint( final String strEntity )
    {
        try
        {
            return this.prettyPrint( mapper.readValue( strEntity, Object.class ) );
        }
        catch( final JsonProcessingException e )
        {
            return strEntity;
        }
    }

    public String prettyPrint( final Object entity )
    {
        try
        {
            return mapper.writeValueAsString( entity );
        }
        catch( final JsonProcessingException e )
        {
            return entity.toString( );
        }
    }
}
