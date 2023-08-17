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
package fr.paris.lutece.plugins.rest.service.mapper;

import fr.paris.lutece.portal.service.util.AppLogService;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static javax.ws.rs.core.Response.Status;

/**
 * Generic exception mapper, implementing {@link ExceptionMapper}, used to convert uncaught exceptions to a proper {@link Response} to return.<br/>
 * To implement your own in a Lutece plugin or module using lutece-rest-plugin-rest, extend this class, annotate it with {@link javax.ws.rs.ext.Provider}, and
 * add it as a bean in the context.xml file.
 * 
 * @param <T>
 *            The sub-class of {@link Throwable} to catch
 * @param <E>
 *            The entity type wanted in the {@link Response}
 */
public abstract class GenericUncaughtExceptionMapper<T extends Throwable, E> implements ExceptionMapper<T>
{

    public static final String ERROR_DURING_TREATMENT = "An error occurred during the treatment.";

    @Override
    public Response toResponse( final T exception )
    {
        AppLogService.error( "REST : Uncaught exception occured.", exception );
        return Response.status( getStatus( ) ).entity( buildEntity( exception ) ).type( getType( ) ).build( );
    }

    protected abstract Status getStatus( );

    protected abstract E buildEntity( final T exception );

    protected abstract String getType( );

}
