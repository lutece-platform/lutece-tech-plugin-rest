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

/**
 * Nom du Fichier : $RCSfile: CaptchaServlet.java,v $
 * Version CVS : $Revision: 1.6 $
 * Auteur : A.Floquet
 * Description : Servlet de génération de l'image captcha.
 *
 */
package fr.paris.lutece.plugins.jcaptcha.service.image;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Classe permettant de générer un test captcha.
 *
 * Cette classe invoque le service captcha depuis l'applicationContext et
 * récupère un test. La réponse au test est stockée par le service, dans une
 * map, avec comme clé l'id de la session utilisateur.<br>
 * Cette Servlet renvoi l'image générée, encodée en jpeg.
 *
 *
 */
public class ImageCaptchaServlet extends HttpServlet
{
    private static final String PARAMETER_SESSION_IMAGE_CAPTCHA = "imageCaptcha";
    private static final String LOGGER = "lutece.captcha";
    private static final long serialVersionUID = -1806578484091247923L;

    /**
     * (non-Javadoc)
     * @param request the request
     * @param response the response
     * @throws ServletException the Servlet Exception
     * @throws IOException the IOException
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        AppLogService.debug( LOGGER, "challenge captcha generation start" );

        byte[] captchaChallengeImage = null;

        // the output stream to render the captcha image as jpeg into
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream(  );

        try
        {
            // grab bean
            // MultiTypeCaptchaService captcha = (MultiTypeCaptchaService) SpringContextService.getPluginBean("jcaptcha", "imageCaptchaService");
            GenericManageableCaptchaService captcha = (GenericManageableCaptchaService) SpringContextService.getPluginBean( "jcaptcha",
                    "imageCaptchaService" );
            AppLogService.info( "captcha : " + captcha );

            // get the session id that will identify the generated captcha.
            // the same id must be used to validate the response, the session id
            // is a good candidate!
            BufferedImage challengeImage = captcha.getImageChallengeForID( request.getSession(  ).getId(  ),
                    request.getLocale(  ) );

            // a jpeg encoder
            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder( jpegOutputStream );
            jpegEncoder.encode( challengeImage );
        }
        catch ( IllegalArgumentException e )
        {
            AppLogService.error( "exception :" + e );
            response.sendError( HttpServletResponse.SC_NOT_FOUND );

            return;
        }
        catch ( CaptchaServiceException e )
        {
            AppLogService.error( "exception :" + e );
            response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );

            return;
        }

        captchaChallengeImage = jpegOutputStream.toByteArray(  );
        request.getSession(  ).setAttribute( PARAMETER_SESSION_IMAGE_CAPTCHA, captchaChallengeImage );
        // flush it in the response
        response.setHeader( "cache-control", "no-cache, no-store,must-revalidate,max-age=0" );
        response.setHeader( "pragma", "no-store, no-cache" );
        response.setHeader( "expires", "1" );
        response.setContentType( "image/jpeg" );

        ServletOutputStream responseOutputStream = response.getOutputStream(  );
        responseOutputStream.write( captchaChallengeImage );
        responseOutputStream.flush(  );
        responseOutputStream.close(  );
        AppLogService.debug( LOGGER, "challenge captcha generation end" );
    }
}
