/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.rest.util.json;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Map;


/**
 * JSON Utils
 */
public final class JSONUtil
{
    
    /** Private constructor */
    private JSONUtil(  )
    {
    }

    /**
     * Convert a model into a JSON formatted string
     * @param model The model
     * @return The JSON string
     */
    public static String model2Json( Map model )
    {
        ObjectNode json = json( model );

        return json.toPrettyString( );
    }


    /**
     * Convert a model into a JSON object
     * @param model The model
     * @return The JSON Object
     */
    public static ObjectNode json( Map model )
    {
        ObjectMapper objectMapper = new ObjectMapper( );

        return objectMapper.convertValue( model, ObjectNode.class );
    }

    /**
     * Format error as JSON
     * @param strMessage the error message
     * @param nCode The error code
     * @return The formatted string
     */
    public static String formatError( String strMessage, int nCode )
    {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        ObjectNode detail = mapper.createObjectNode();
        
        detail.put( "message", strMessage );
        detail.put( "code", nCode );
        json.set( "error", detail );

        return json.toPrettyString( );
    }
}
