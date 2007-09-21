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
package fr.paris.lutece.plugins.ods.web.utils;


/**
 * DateConvertException
 *
 */
public class DateConvertException extends Exception
{
    private static final long serialVersionUID = 1728596006120636669L;
    private static final String DEFAULT_ERROR_MESSAGE = "";

    /**
     * Crée une exception due à une erreur de conversion String en Timestamp avec le message par défaut
     */
    public DateConvertException(  )
    {
        super( DEFAULT_ERROR_MESSAGE );
    }

    /**
     * Créé une exception due à une erreur de conversion String en Timestamp avec un message particulier
     * @param strErrorMessage le message d'erreur
     */
    public DateConvertException( String strErrorMessage )
    {
        super( strErrorMessage );
    }

    /**
     * Créé une exception due à une erreur de conversion String en Timestamp avec un
     * message particulier et la cause originale de l'exception
     * @param strErrorMessage le message d'erreur
     * @param cause la cause de l'exception
     */
    public DateConvertException( String strErrorMessage, Throwable cause )
    {
        super( strErrorMessage, cause );
    }

    /**
     * Créé une exception due à une erreur de conversion String en Timestamp avec la cause originale de l'exception
     * @param cause la cause de l'exception
     */
    public DateConvertException( Throwable cause )
    {
        super( cause );
    }
}
