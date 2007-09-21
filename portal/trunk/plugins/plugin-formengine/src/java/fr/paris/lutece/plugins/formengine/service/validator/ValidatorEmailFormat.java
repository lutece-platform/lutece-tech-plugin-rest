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
 * This class is responsible of validating emails.
 * The rule parameter retrieve from the form definition file
 * tells wether the domain name existence should be checked.
 */
public class ValidatorEmailFormat extends ValidatorPattern
{
    private static final String PROPERTY_PATTERN_VALIDATION_MESSAGE = "formengine.validator.message.email";
    private static final String EMAIL_PATTERN = "^[\\w_.-]+@[\\w_.-]+\\.[\\w]+$";

    public ValidatorEmailFormat(  )
    {
    }

    /**
    * The validate implementation.
    * Check that the field value matches the pattern "xxx@yyy.zzz"
    * If the rule parameter is "checkDomain", the domain name is also checked.
    * @param field The field to validate
    * @param errors A FormErrorsList to add errors into
    * @return True if validate, false otherwise
    */
    public boolean validate( Field field, FormErrorsList errors )
    {
        String strEmailValue = field.getValue(  );

        if ( ( strEmailValue == null ) || ( strEmailValue.trim(  ).equals( "" ) ) )
        {
            // we don't try to validate an empty address
            return true;
        }

        // check the pattern
        boolean bMatchesPattern = this.checkValueOnPattern( EMAIL_PATTERN, strEmailValue );

        if ( !bMatchesPattern )
        {
            String[] messageParams = new String[2];
            messageParams[0] = field.getName(  );
            messageParams[1] = field.getLabel(  );

            if ( this.getErrorMessage(  ) != null )
            {
                errors.addErrorMessage( this.getErrorMessage(  ) );
            }
            else
            {
                errors.addError( PROPERTY_PATTERN_VALIDATION_MESSAGE, messageParams );
            }

            return false;
        }
        else
        {
            return true;
        }
    }
}
