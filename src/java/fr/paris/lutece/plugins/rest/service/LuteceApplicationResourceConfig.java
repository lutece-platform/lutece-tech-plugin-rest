/*
 * Copyright (c) 2002-2023, City of Paris
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
package fr.paris.lutece.plugins.rest.service;

import fr.paris.lutece.plugins.rest.service.mapper.UncaughtThrowableMapper;
import fr.paris.lutece.plugins.rest.service.mediatype.MediaTypeMapping;
import fr.paris.lutece.plugins.rest.service.mediatype.RestMediaTypes;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.paris.lutece.plugins.rest.service.LuteceJerseySpringServlet.LOGGER;

public class LuteceApplicationResourceConfig extends ResourceConfig
{
    // PROPERTIES
    private static final String GENERIC_EXCEPTION_MAPPER = "rest.generic.exception.mapper";
    private static final String OUTBOUND_CONTENT_BUFFER = "rest.outbound.content.buffer.size";

    public LuteceApplicationResourceConfig( )
    {
        if ( AppPropertiesService.getPropertyBoolean( GENERIC_EXCEPTION_MAPPER, true ) )
        {
            // Registering JacksonFeature without its built-in ExceptionMapper so that we can register our own generic one
            register( JacksonFeature.withoutExceptionMappers( ) );
            register( new UncaughtThrowableMapper( ) );
        }

        // Automatically register all beans with @Path annotation because
        // this is was the previous versions of plugin-rest did
        Map<String, Object> providers = SpringContextService.getContext( ).getBeansWithAnnotation( Provider.class );
        for ( Object o : providers.values( ) )
        {
            register( o.getClass( ) );
        }

        Map<String, Object> pathes = SpringContextService.getContext( ).getBeansWithAnnotation( Path.class );
        for ( Object o : pathes.values( ) )
        {
            register( o.getClass( ) );
        }

        try
        {
            try
            {
                Map<String, MediaType> mapExtensionToMediaType = new HashMap<>( );

                // map default ".extension" to MediaType
                mapExtensionToMediaType.put( "atom", MediaType.APPLICATION_ATOM_XML_TYPE );
                mapExtensionToMediaType.put( "xml", MediaType.APPLICATION_XML_TYPE );
                mapExtensionToMediaType.put( "json", MediaType.APPLICATION_JSON_TYPE );
                mapExtensionToMediaType.put( "kml", RestMediaTypes.APPLICATION_KML_TYPE );

                // add specific-plugin-provided extensions
                List<MediaTypeMapping> listMappings = SpringContextService.getBeansOfType( MediaTypeMapping.class );

                if ( CollectionUtils.isNotEmpty( listMappings ) )
                {
                    for ( MediaTypeMapping mapping : listMappings )
                    {
                        String strExtension = mapping.getExtension( );
                        MediaType mediaType = mapping.getMediaType( );

                        if ( StringUtils.isNotBlank( strExtension ) && ( mediaType != null ) )
                        {
                            mapExtensionToMediaType.put( strExtension, mediaType );
                        }
                        else
                        {
                            LOGGER.error( "Can't add media type mapping for extension : " + strExtension + ", mediatype : " + mediaType
                                    + ". Please check your context configuration." );
                        }
                    }
                }

                property( ServerProperties.MEDIA_TYPE_MAPPINGS, mapExtensionToMediaType );

                int bufferSize = AppPropertiesService.getPropertyInt(OUTBOUND_CONTENT_BUFFER, -1);
                if (bufferSize >= 0) { // -1 signifie que la propriété n'existe pas ou n'est pas valide
                        property(ServerProperties.OUTBOUND_CONTENT_LENGTH_BUFFER, bufferSize);
                    }

            }

            catch( UnsupportedOperationException uoe )
            {
                // In jersey 1.x, this might have been an immutable map.
                // In jersey 2.x, I don't know if this is useful
                LOGGER.error( uoe.getMessage( ) + ". Won't support extension mapping (.json, .xml, .atom)", uoe );
            }

            // log services
            if ( LOGGER.isDebugEnabled( ) )
            {
                LOGGER.debug( "Listing registered services and providers" );

                for ( Class<?> clazz : getClasses( ) )
                {
                    LOGGER.debug( clazz );
                }

                LOGGER.debug( "End of listing" );
            }
        }
        catch( RuntimeException e )
        {
            LOGGER.log( Level.ERROR, "REST services won't be available. Please check your configuration or enable at least on rest module." );
            LOGGER.log( Level.ERROR, "LuteceJerseySpringServlet : Exception occurred when intialization", e );
            // throw e;
        }
    }
}
