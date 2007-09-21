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
package fr.paris.lutece.plugins.formengine.service.output;

import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.xml.XmlUtil;

import java.io.File;
import java.io.StringReader;

import javax.mail.MessagingException;

import javax.xml.transform.stream.StreamSource;


/**
 * Abstract class to define basis of mail output processors
 */
public abstract class MailOutputProcessor extends OutputProcessor
{
    private static final String PROPERTY_MAIL_HOST = "mail.server";
    private String _strXslFilePath;
    private String _strXmlContent;
    private String _strSenderName;
    private String _strSenderEmail;
    private String _strEmailSubject;
    private String _strRecipientEmail;

    /**
     * @see fr.paris.lutece.plugins.formengine.service.output.OutputProcessor#generateOutput(java.lang.Object)
     */
    protected void generateOutput( Object transactionObject )
    {
        // if no mail specified, do nothing
        if ( ( _strRecipientEmail != null ) && ( !_strRecipientEmail.trim(  ).equals( "" ) ) )
        {
            String strXslPath = getXslFilePath(  );

            File fileXsl = new File( strXslPath );
            StreamSource sourceStyleSheet = new StreamSource( fileXsl );
            StringReader srInput = new StringReader( getXmlContent(  ) );
            StreamSource sourceDocument = new StreamSource( srInput );

            try
            {
                String strMessageText = XmlUtil.transform( sourceDocument, sourceStyleSheet, null, null );
                MailService.sendMail( getRecipientEmail(  ), getSenderName(  ), getSenderEmail(  ),
                    getEmailSubject(  ), strMessageText );
            }
            catch ( Exception e )
            {
                AppLogService.error( e.getMessage(  ) );
            }
        }
    }

    /**
     * @return Returns the _strXslFilePath.
     */
    public String getXslFilePath(  )
    {
        return _strXslFilePath;
    }

    /**
     * @param strXslFileFilePath The _strXslFilePath to set.
     */
    public void setXslFilePath( String strXslFilePath )
    {
        _strXslFilePath = strXslFilePath;
    }

    /**
     * @return Returns the _strEmailSubject.
     */
    public String getEmailSubject(  )
    {
        return _strEmailSubject;
    }

    /**
     * @param strEmailSubject The _strEmailSubject to set.
     */
    public void setEmailSubject( String strEmailSubject )
    {
        _strEmailSubject = strEmailSubject;
    }

    /**
     * @return Returns the _strRecipientEmail.
     */
    public String getRecipientEmail(  )
    {
        return _strRecipientEmail;
    }

    /**
     * @param strRecipientEmail The _strRecipientEmail to set.
     */
    public void setRecipientEmail( String strRecipientEmail )
    {
        _strRecipientEmail = strRecipientEmail;
    }

    /**
     * @return Returns the _strSenderEmail.
     */
    public String getSenderEmail(  )
    {
        return _strSenderEmail;
    }

    /**
     * @param strSenderEmail The _strSenderEmail to set.
     */
    public void setSenderEmail( String strSenderEmail )
    {
        _strSenderEmail = strSenderEmail;
    }

    /**
     * @return Returns the _strSenderName.
     */
    public String getSenderName(  )
    {
        return _strSenderName;
    }

    /**
     * @param strSenderName The _strSenderName to set.
     */
    public void setSenderName( String strSenderName )
    {
        _strSenderName = strSenderName;
    }

    /**
     * @return Returns the _strXmlContent.
     */
    public String getXmlContent(  )
    {
        return _strXmlContent;
    }

    /**
     * @param strXmlContent The _strXmlContent to set.
     */
    public void setXmlContent( String strXmlContent )
    {
        _strXmlContent = strXmlContent;
    }
}
