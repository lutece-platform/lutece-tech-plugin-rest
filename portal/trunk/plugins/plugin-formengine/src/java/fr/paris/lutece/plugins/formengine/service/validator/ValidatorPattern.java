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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class is responsible for checking a field against a regexp pattern
 */
public class ValidatorPattern extends FieldValidator
{
    private static final String PROPERTY_VALIDATION_MESSAGE = "formengine.validator.message.pattern";

    public ValidatorPattern(  )
    {
    }

    /**
    * The validate implementation.
    * Checks that the fields value matches the pattern given as a rule parameter.
    * @param field The field to validate
    * @param errors A FormErrorsList to add errors into
    * @return True if validate, false otherwise
    */
    public boolean validate( Field field, FormErrorsList errors )
    {
        String strFieldValue = field.getValue(  );
        String strPattern = this.getRuleParameter(  );

        if ( ( strFieldValue == null ) || ( strFieldValue.trim(  ).equals( "" ) ) )
        {
            // we don't try to validate an empty field
            return true;
        }

        boolean bMatches = checkValueOnPattern( strPattern, strFieldValue );

        if ( bMatches )
        {
            return true;
        }
        else
        {
            String[] messageParams = new String[3];
            messageParams[0] = field.getName(  );
            messageParams[1] = field.getLabel(  );
            messageParams[2] = strPattern;

            if ( this.getErrorMessage(  ) != null )
            {
                errors.addErrorMessage( this.getErrorMessage(  ) );
            }
            else
            {
                errors.addError( PROPERTY_VALIDATION_MESSAGE, messageParams );
            }

            return false;
        }
    }

    /**
     * Check a given string against a given pattern
     * @param strPattern the pattern to match
     * @param strValue the value to check
     * @return true if pattern matches, false otherwise.
     */
    protected boolean checkValueOnPattern( String strPattern, String strValue )
    {
        Pattern pattern = Pattern.compile( strPattern );
        Matcher matcher = pattern.matcher( strValue );

        return matcher.matches(  );
    }
}
