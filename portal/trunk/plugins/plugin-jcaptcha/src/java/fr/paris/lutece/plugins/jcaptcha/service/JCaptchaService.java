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
package fr.paris.lutece.plugins.jcaptcha.service;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.octo.captcha.service.sound.SoundCaptchaService;

import fr.paris.lutece.portal.service.captcha.ICaptchaService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import javax.servlet.http.HttpServletRequest;


/**
 *
 */
public class JCaptchaService implements ICaptchaService
{
    private static final String TEMPLATE_JCAPTCHA = "jcaptcha.template.captchaTemplate";
    private static final String SPRING_CONTEXT_NAME = "jcaptcha";
    private static final String LOGGER = "lutece.captcha";

    /**
     * @param request the request
     * @return true if the captcha is valid
     *
     * @see fr.paris.lutece.plugins.jcaptcha.service.IJCaptchaValidator#validate(javax.servlet.http.HttpServletRequest)
     */
    public boolean validate( HttpServletRequest request )
    {
        AppLogService.debug( LOGGER, "Validate captcha response for id : " + request.getSession(  ).getId(  ) );

        String captchaReponse = request.getParameter( "j_captcha_response" ).toLowerCase(  );
        ImageCaptchaService imageCaptcha = (ImageCaptchaService) SpringContextService.getPluginBean( SPRING_CONTEXT_NAME,
                "imageCaptchaService" );
        SoundCaptchaService soundCaptcha = (SoundCaptchaService) SpringContextService.getPluginBean( SPRING_CONTEXT_NAME,
                "soundCaptchaService" );
        boolean validImage = false;
        boolean validSound = false;
        String sessionId = request.getSession(  ).getId(  );

        try
        {
            validImage = imageCaptcha.validateResponseForID( sessionId, captchaReponse );
            validSound = soundCaptcha.validateResponseForID( sessionId, captchaReponse );
        }
        catch ( CaptchaServiceException e )
        {
            AppLogService.debug( LOGGER, e );
        }

        if ( validImage || validSound )
        {
            AppLogService.debug( LOGGER, "Valid response" );

            return true;
        }
        else
        {
            AppLogService.debug( LOGGER, "Unvalid response" );

            return false;
        }
    }

    /**
     * Builds the html code of a captcha from a template.
     *
     * @return the html code of a captcha
     */
    public String getHtmlCode(  )
    {
        String strCaptchaTemplate = AppPropertiesService.getProperty( TEMPLATE_JCAPTCHA );

        HtmlTemplate captchaTemplate = AppTemplateService.getTemplate( strCaptchaTemplate );

        return captchaTemplate.getHtml(  );
    }
}
