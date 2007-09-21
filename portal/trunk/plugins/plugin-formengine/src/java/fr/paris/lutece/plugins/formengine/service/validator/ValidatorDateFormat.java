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
package fr.paris.lutece.plugins.formengine.service.validator;

import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.Field;
import fr.paris.lutece.plugins.formengine.web.FormErrorsList;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;


/**
 * This class is responsible of validating dates.
 */
public class ValidatorDateFormat extends FieldValidator
{
    private static final String PROPERTY_VALIDATION_MESSAGE_FORMAT = "formengine.validator.message.date.format";
    private static final String PROPERTY_VALIDATION_MESSAGE_MIN = "formengine.validator.message.date.min";
    private static final String PROPERTY_VALIDATION_MESSAGE_MAX = "formengine.validator.message.date.max";
    private static final String PROPERTY_VALIDATION_MESSAGE_MIN_NOW = "formengine.validator.message.date.min.now";
    private static final String PROPERTY_VALIDATION_MESSAGE_MAX_NOW = "formengine.validator.message.date.max.now";
    private static final String PROPERTY_VALIDATION_KEYWORD_NOW = "formengine.validator.keyword.date.now";

    public ValidatorDateFormat(  )
    {
    }

    /**
     * Check the format according to the pattern given in parameters.
     * The patterns allowed are the numeric ones, ie built on the following
     * symbols.
     */
    public boolean validate( Field field, FormErrorsList errors )
    {
        String strDateValue = field.getValue(  );
        String strDateFormat = this.getRuleParameter(  );

        String[] messageParams = new String[3];
        messageParams[0] = field.getName(  );
        messageParams[1] = field.getLabel(  );

        String strErrorPropertyMessage;

        if ( ( strDateValue == null ) || ( strDateValue.trim(  ).equals( "" ) ) )
        {
            // we don't try to validate an empty date
            return true;
        }

        if ( strDateFormat == null )
        {
            return false;
        }

        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat( strDateFormat );
            dateFormat.setLenient( false );

            Date date = dateFormat.parse( strDateValue );

            String strDateMinValue = this.getMin(  );
            String strDateMaxValue = this.getMax(  );
            String strKeywordDateNow = AppPropertiesService.getProperty( PROPERTY_VALIDATION_KEYWORD_NOW );

            if ( strDateMinValue != null )
            {
                try
                {
                    Date dateMin;

                    if ( strDateMinValue.equals( strKeywordDateNow ) )
                    {
                        //dateMin = new Date(  );
                        dateMin = dateFormat.parse( dateFormat.format( new Date(  ) ) );
                        strErrorPropertyMessage = PROPERTY_VALIDATION_MESSAGE_MIN_NOW;
                    }
                    else
                    {
                        dateMin = dateFormat.parse( strDateMinValue );
                        messageParams[2] = dateFormat.format( dateMin );
                        strErrorPropertyMessage = PROPERTY_VALIDATION_MESSAGE_MIN;
                    }

                    if ( date.before( dateMin ) )
                    {
                        if ( this.getErrorMessage(  ) != null )
                        {
                            errors.addErrorMessage( this.getErrorMessage(  ) );
                        }
                        else
                        {
                            errors.addError( strErrorPropertyMessage, messageParams );
                        }

                        return false;
                    }
                }
                catch ( ParseException e )
                {
                    // if dateMin not parsed, just ignore it
                }
            }

            if ( strDateMaxValue != null )
            {
                try
                {
                    Date dateMax;

                    if ( strDateMaxValue.equals( strKeywordDateNow ) )
                    {
                        dateMax = dateFormat.parse( dateFormat.format( new Date(  ) ) );

                        // dateMax = new Date(  );
                        strErrorPropertyMessage = PROPERTY_VALIDATION_MESSAGE_MAX_NOW;
                    }
                    else
                    {
                        dateMax = dateFormat.parse( strDateMaxValue );
                        messageParams[2] = dateFormat.format( dateMax );
                        strErrorPropertyMessage = PROPERTY_VALIDATION_MESSAGE_MAX;
                    }

                    if ( date.after( dateMax ) )
                    {
                        if ( this.getErrorMessage(  ) != null )
                        {
                            errors.addErrorMessage( this.getErrorMessage(  ) );
                        }
                        else
                        {
                            errors.addError( strErrorPropertyMessage, messageParams );
                        }

                        return false;
                    }
                }
                catch ( ParseException e )
                {
                    // if dateMax not parsed, just ignore it
                }
            }

            return true;
        }
        catch ( Exception e )
        {
            messageParams[2] = strDateFormat;
            strErrorPropertyMessage = PROPERTY_VALIDATION_MESSAGE_FORMAT;

            if ( this.getErrorMessage(  ) != null )
            {
                errors.addErrorMessage( this.getErrorMessage(  ) );
            }
            else
            {
                errors.addError( strErrorPropertyMessage, messageParams );
            }

            return false;
        }
    }
}
