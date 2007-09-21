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
package fr.paris.lutece.plugins.document.business.rules;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.service.DocumentException;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.i18n.Localizable;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * Interface of rules that can be applied on documents by the RuleEngine
 */
public interface Rule extends Localizable
{
    ////////////////////////////////////////////////////////////////////////////
    // Methods implemented by AbstractRule

    /**
     * Sets the Rule Id
     * @param nId The rule Id
     */
    public void setId( int nId );

    /**
     * Gets the Rule Id
     * @return the Rule Id
     */
    public int getId(  );

    /**
     * Sets the RuleTypeId
     * @param strId The Rule type Id
     */
    public void setRuleTypeId( String strId );

    /**
     * Gets the Rule Type Id
     * @return the Rule Type Id
     */
    public String getRuleTypeId(  );

    /**
     * Gets a specific attribute by its name
     * @param strAttributeName The attribute name
     * @return The attribute value
     */
    public String getAttribute( String strAttributeName );

    /**
     * Sets a specific rule attribute
     * @param strAttributeName The attribute name
     * @param strAttributeValue The attribute value
     */
    public void setAttribute( String strAttributeName, String strAttributeValue );

    /**
     * Reads rule attributes
     * @param request The HTTP request
     */
    public void readAttributes( HttpServletRequest request );

    ////////////////////////////////////////////////////////////////////////////
    // Method specific to the rule

    /**
     * Execute the rule
     * @param document The document concerned
     * @param user The user concerned
     * @throws DocumentException raise when error occurs in event or rule
     */
    public void apply( Document document, AdminUser user )throws DocumentException;
    
    /**
     * Check the rule
     * @return null if rule is valid, message if rule not valid
     */
    String validateRule(  );

    /**
     * Return the HTML form containing specific fields of the rule
     * @param user The current user
     * @param locale The Locale
     * @return The Create HTML form
     */
    public String getCreateForm( AdminUser user, Locale locale );

    /**
     * Gives all specific attributes of the rule
     * @return A string array containing all attributes names
     */
    public String[] getAttributesList(  );

    /**
     * Gets the Rule definition
     * @return The Rule definition
     */
    public String getRule(  );

    /**
     * Gets the Rule name key
     * @return The Rule name key
     */
    public String getNameKey(  );
}
