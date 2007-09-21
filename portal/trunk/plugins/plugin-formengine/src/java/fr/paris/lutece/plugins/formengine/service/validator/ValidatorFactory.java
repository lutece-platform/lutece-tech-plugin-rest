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

import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.CheckRule;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;


/**
 * This class is responsible for calling the appropriate validator for a check rule.
 */
public class ValidatorFactory
{
    private static final String PROPERTY_VALIDATOR_PREFIX = "formengine.validator.";
    private static final String PROPERTY_VALIDATOR_SPECIFIC_TYPE_NAME = "formengine.validator.specific.type.name";

    /**
     * Constructor
     */
    public ValidatorFactory(  )
    {
    }

    /**
     * This method can be called to get a validator for a given checkrule
     * @param rule the rule to check
     * @return An instance of the validator that will be able to check the given rule.
     */
    public static Validator getValidator( CheckRule rule )
    {
        Validator validator = null;
        String strValidatorClass;

        String strValidatorSpecificRuleType = AppPropertiesService.getProperty( PROPERTY_VALIDATOR_SPECIFIC_TYPE_NAME );

        String strType = rule.getType(  );
        String strParameter = rule.getParameter(  );

        try
        {
            // if the rule is "specific", the parameter is the class name
            if ( ( strType != null ) && ( strType.trim(  ).equals( strValidatorSpecificRuleType ) ) )
            {
                strValidatorClass = strParameter;
                validator = (Validator) Class.forName( strValidatorClass ).newInstance(  );
            }

            // else : get the class name from the property
            else
            {
                strValidatorClass = AppPropertiesService.getProperty( PROPERTY_VALIDATOR_PREFIX + strType );
                validator = (Validator) Class.forName( strValidatorClass ).newInstance(  );
                validator.setRuleParameter( rule.getParameter(  ) );
            }

            // set the min/max parameters
            validator.setMin( rule.getMin(  ) );
            validator.setMax( rule.getMax(  ) );

            // set the message if one is given
            if ( ( rule.getErrorMessage(  ) != null ) && ( !rule.getErrorMessage(  ).trim(  ).equals( "" ) ) )
            {
                validator.setErrorMessage( rule.getErrorMessage(  ) );
            }
            else if ( ( rule.getErrorKey(  ) != null ) && ( !rule.getErrorKey(  ).trim(  ).equals( "" ) ) )
            {
                String strErrorMessage = AppPropertiesService.getProperty( rule.getErrorKey(  ) );
                validator.setErrorMessage( strErrorMessage );
            }
        }
        catch ( InstantiationException e )
        {
            throw new AppException( "Formengine : an error occurred while instancing a validator", e );
        }
        catch ( IllegalAccessException e )
        {
            throw new AppException( "Formengine : an error occurred while instancing a validator", e );
        }
        catch ( ClassNotFoundException e )
        {
            throw new AppException( "Formengine : an error occurred while instancing a validator", e );
        }

        return validator;
    }
}
