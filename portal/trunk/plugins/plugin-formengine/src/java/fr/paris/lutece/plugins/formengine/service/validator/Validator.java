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


/**
 * This class defines the main characteritics of a validator.
 */
public abstract class Validator
{
    private String _strRuleParameter;
    private String _strRuleMin;
    private String _strRuleMax;
    private String _strErrorMessage;

    public Validator(  )
    {
    }

    /**
     * Setter for the check rule min value
     * @param strMin
     */
    public void setMin( String strMin )
    {
        _strRuleMin = strMin;
    }

    /**
     * Getter for the check rule min value
     * @return the min value
     */
    public String getMin(  )
    {
        return _strRuleMin;
    }

    /**
     * Setter for the check rule max value
     * @param strMax
     */
    public void setMax( String strMax )
    {
        _strRuleMax = strMax;
    }

    /**
     * Getter for the check rule max value
     * @return the max value
     */
    public String getMax(  )
    {
        return _strRuleMax;
    }

    /**
     * Setter for the check rule error message
     * @param strErrorMessage
     */
    public void setErrorMessage( String strErrorMessage )
    {
        _strErrorMessage = strErrorMessage;
    }

    /**
     * Getter for the check rule ErrorMessage
     * @return the Error Message
     */
    public String getErrorMessage(  )
    {
        return _strErrorMessage;
    }

    /**
     * Setter for the check rule parameter
     * @param strRuleParameter
     */
    public void setRuleParameter( String strRuleParameter )
    {
        _strRuleParameter = strRuleParameter;
    }

    /**
     * Getter for the check rule parameter
     * @return the parameter
     */
    public String getRuleParameter(  )
    {
        return _strRuleParameter;
    }
}
