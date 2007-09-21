/*
 * Copyright (c) 2002-2006, Mairie de Paris
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
package fr.paris.lutece.plugins.projeau.service.output;

import fr.paris.lutece.plugins.formengine.service.output.MailOutputProcessor;
import fr.paris.lutece.plugins.formengine.web.Form;
import fr.paris.lutece.plugins.projeau.business.Projeau;
import fr.paris.lutece.plugins.projeau.business.ProjeauFormDocument;
import fr.paris.lutece.portal.service.util.AppPropertiesService;


/**
 * This method is responsible for ouputing and sending an email to the projeau applicant (if one was given).
 */
public class ProjeauMailOutputProcessor extends MailOutputProcessor
{
    private static final String PROPERTY_FRAGMENT_MAIL_STYLESHEET = ".xsl.file.email.body";
    private static final String PROPERTY_FRAGMENT_EMAIL_SENDER_NAME = ".email.sender.name";
    private static final String PROPERTY_FRAGMENT_EMAIL_SENDER_EMAIL = ".email.sender.email";

    /**
     * Initializes the mail output processor.
     * This method actually allows to define :
     * <ul>
     * <li>The xml content that provides data to show in the mail</li>
     * <li>The xsl stylesheet to use in order to build the mail content</li>
     * <li>The email subject</li>
     * <li> The email and name to show as sender information</li>
     * <li> The email of the recipient (from the projeau)</li>
     * </ul>
     * @see fr.paris.lutece.plugins.formengine.service.output.OutputProcessor#init(fr.paris.lutece.plugins.formengine.web.Form, java.lang.Object)
     */
    protected void init( Form form, Object transactionObject )
    {
        ProjeauFormDocument formElements = (ProjeauFormDocument) transactionObject;
        Projeau projeau = formElements.getProjeau(  );

        // set xml content
        setXmlContent( formElements.getXmlDocument(  ) );

        // xsl stylesheet used for display
        String strXslFileName = AppPropertiesService.getProperty( form.getName(  ) + PROPERTY_FRAGMENT_MAIL_STYLESHEET );
        String strXslDirectory = form.getXslDirectoryPath(  );
        setXslFilePath( strXslDirectory + strXslFileName );

        // set email subject
        setEmailSubject( form.getTitle(  ) );

        // set email recipient
        String strRecipient = projeau.getEmail(  );
        setRecipientEmail( strRecipient );

        // set the sender name
        String strSenderName = AppPropertiesService.getProperty( form.getName(  ) +
                PROPERTY_FRAGMENT_EMAIL_SENDER_NAME );
        setSenderName( strSenderName );

        // set the sender email
        String strSenderEmail = AppPropertiesService.getProperty( form.getName(  ) +
                PROPERTY_FRAGMENT_EMAIL_SENDER_EMAIL );
        setSenderEmail( strSenderEmail );
    }
}
