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
package fr.paris.lutece.plugins.formengine.web;

import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.text.MessageFormat;

import java.util.ArrayList;


/**
 * This class provides a list of errors for error management
 */
public class FormErrorsList extends ArrayList<String>
{
    private static final long serialVersionUID = -224390005524091311L;

    /**
     * The constructor
     */
    public FormErrorsList(  )
    {
    }

    /**
     * Add an error to the list
     * @param strMessage the string message to display
     * @param strParameter the parameter to complete the message
     */
    public void addErrorMessage( String strMessage )
    {
        this.add( strMessage );
    }

    /**
     * Add an error to the list
     * @param strErrorKey the property giving the message associated with the error
     */
    public void addError( String strErrorKey )
    {
        String strErrorMessage = AppPropertiesService.getProperty( strErrorKey );

        this.add( strErrorMessage );
    }

    /**
    * Add an error to the list
    * @param strErrorKey the property giving the message associated with the error
    * @param strParameter the parameter to complete the message
    */
    public void addError( String strErrorKey, String strParameter )
    {
        String strErrorMessage = AppPropertiesService.getProperty( strErrorKey );

        if ( strParameter == null )
        {
            this.addErrorMessage( strErrorMessage );
        }
        else
        {
            String[] messageFormatParam = new String[1];
            messageFormatParam[0] = strParameter;
            addError( strErrorKey, messageFormatParam );
        }
    }

    /**
     * Add an error to the list
     * @param strErrorKey the property giving the message associated with the error
     * @param parameters the array of parameters to complete the message
     */
    public void addError( String strErrorKey, String[] parameters )
    {
        String strErrorMessage = AppPropertiesService.getProperty( strErrorKey );

        if ( parameters == null )
        {
            this.add( strErrorMessage );
        }
        else
        {
            String strErrorMessageWithParam = MessageFormat.format( strErrorMessage, (Object[]) parameters );
            this.add( strErrorMessageWithParam );
        }
    }
}
