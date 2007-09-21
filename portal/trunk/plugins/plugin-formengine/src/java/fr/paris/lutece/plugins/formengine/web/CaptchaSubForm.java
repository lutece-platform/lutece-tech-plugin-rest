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

import fr.paris.lutece.plugins.formengine.web.SubForm;
import fr.paris.lutece.portal.service.captcha.CaptchaSecurityService;
import fr.paris.lutece.portal.service.captcha.ICaptchaSecurityService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Class for implementation of captcha-enabled subform
 *
 * Provides automatic validation and ease of captcha integration
 */
public abstract class CaptchaSubForm extends SubForm
{
    private static final String PROPERTY_FRAGMENT_ERROR_MESSAGE_INVALID_CAPTCHA = ".error.message.invalidCaptcha";

    /**
     * @param strXmlCode the xml code
     * @param strXslFileName the xsl file
     * @param params parameters for the xsl file
     * @return the html code
     */
    protected String getHtml( String strXmlCode, String strXslFileName, Map params )
    {
        // automatically add the captcha code
        params.put( SharedConstants.PARAMETER_XSL_CAPTCHA, getCaptchaService(  ).getHtmlCode(  ) );

        return super.getHtml( strXmlCode, strXslFileName, params );
    }

    /**
     * @param request the request
     * @return true if fields are valids
     */
    public boolean validateFields( HttpServletRequest request )
    {
        ICaptchaSecurityService captchaService = getCaptchaService(  );
        boolean bValid = ( captchaService == null ) || captchaService.validate( request );

        if ( !bValid )
        {
            this.getParentForm(  )
                .addError( request, this.getParentForm(  ).getName(  ) +
                PROPERTY_FRAGMENT_ERROR_MESSAGE_INVALID_CAPTCHA );

            return false;
        }

        return super.validateFields( request );
    }

    /**
     *
     * @return the captcha security service from the core
     */
    private ICaptchaSecurityService getCaptchaService(  )
    {
        return new CaptchaSecurityService( );
    }
}
