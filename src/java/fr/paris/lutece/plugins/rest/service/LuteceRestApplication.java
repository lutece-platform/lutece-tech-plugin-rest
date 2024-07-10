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
package fr.paris.lutece.plugins.rest.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import fr.paris.lutece.plugins.rest.service.mediatype.RestMediaTypes;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;

@ApplicationPath( RestConstants.APP_PATH )
public class LuteceRestApplication extends Application
{
    
    private static final Logger LOGGER = LogManager.getLogger( RestConstants.REST_LOGGER );

    @Override
    public Map<String, Object> getProperties( )
    {
        Map<String, Object> mapExtensionToMediaType = new HashMap<>( );

        // map default ".extension" to MediaType
        mapExtensionToMediaType.put( "atom", MediaType.APPLICATION_ATOM_XML_TYPE );
        mapExtensionToMediaType.put( "xml", MediaType.APPLICATION_XML_TYPE );
        mapExtensionToMediaType.put( "json", MediaType.APPLICATION_JSON_TYPE );
        mapExtensionToMediaType.put( "kml", RestMediaTypes.APPLICATION_KML_TYPE );
        
        // add specific extensions
        Config config = ConfigProvider.getConfig( );
        Map<String, String> mapMediaTypeMapping = config.getOptionalValue( "rest.mediaTypeMapping", Map.class ).orElse( new HashMap<>( 0 ) );
        
        for ( Entry<String, String> mapping : mapMediaTypeMapping.entrySet( ) )
        {
            String strExtension = mapping.getKey( );
            MediaType mediaType = MediaType.valueOf( mapping.getValue( ));
            
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
        
        return mapExtensionToMediaType;
    }
    
}
