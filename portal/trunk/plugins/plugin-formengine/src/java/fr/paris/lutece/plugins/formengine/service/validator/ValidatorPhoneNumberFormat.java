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


/**
 * This class is responsible of validating numeric fields.
 */
public class ValidatorPhoneNumberFormat extends ValidatorPattern
{
    private static final String PROPERTY_VALIDATION_MESSAGE = "formengine.validator.message.phoneNumber";
    private static final String PHONE_PATTERN = "^0[1-6]([0-9]{8})$";

    public ValidatorPhoneNumberFormat(  )
    {
    }

    /**
    * The validate implementation.
    * Check that the value of a field is only made of digits.
    * @param field The field to validate
    * @param errors A FormErrorsList to add errors into
    * @return True if valid, false otherwise
     */
    public boolean validate( Field field, FormErrorsList errors )
    {
        String strFieldValue = field.getValue(  );

        if ( ( strFieldValue == null ) || ( strFieldValue.trim(  ).equals( "" ) ) )
        {
            return true;
        }

        String[] messageParams = new String[3];
        messageParams[0] = field.getName(  );
        messageParams[1] = field.getLabel(  );

        boolean bMatches = checkValueOnPattern( PHONE_PATTERN, strFieldValue );

        if ( bMatches )
        {
            return true;
        }
        else
        {
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
}
