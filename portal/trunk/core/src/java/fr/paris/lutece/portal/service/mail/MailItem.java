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
package fr.paris.lutece.portal.service.mail;

import java.io.Serializable;
import java.util.Map;


/**
 * MailIem
 */
public class MailItem implements Serializable
{
    public static final int FORMAT_HTML = 0; // Default
    public static final int FORMAT_TEXT = 1;
    public static final int FORMAT_HTML_WITH_ATTACHEMENTS = 2;
    
    // Variables declarations
    private String _strRecipient;
    private String _strSenderName;
    private String _strSenderEmail;
    private String _strSubject;
    private String _strMessage;
    private int _nFormat;
    private Map _mapAttachements;
    
    /**
     * Returns the Recipient
     *
     * @return The Recipient
     */
    public String getRecipient(  )
    {
        return _strRecipient;
    }
    
    /**
     * Sets the Recipient
     *
     * @param strRecipient The Recipient
     */
    public void setRecipient( String strRecipient )
    {
        _strRecipient = strRecipient;
    }
    
    /**
     * Returns the SenderName
     *
     * @return The SenderName
     */
    public String getSenderName(  )
    {
        return _strSenderName;
    }
    
    /**
     * Sets the SenderName
     *
     * @param strSenderName The SenderName
     */
    public void setSenderName( String strSenderName )
    {
        _strSenderName = strSenderName;
    }
    
    /**
     * Returns the SenderEmail
     *
     * @return The SenderEmail
     */
    public String getSenderEmail(  )
    {
        return _strSenderEmail;
    }
    
    /**
     * Sets the SenderEmail
     *
     * @param strSenderEmail The SenderEmail
     */
    public void setSenderEmail( String strSenderEmail )
    {
        _strSenderEmail = strSenderEmail;
    }
    
    /**
     * Returns the Subject
     *
     * @return The Subject
     */
    public String getSubject(  )
    {
        return _strSubject;
    }
    
    /**
     * Sets the Subject
     *
     * @param strSubject The Subject
     */
    public void setSubject( String strSubject )
    {
        _strSubject = strSubject;
    }
    
    /**
     * Returns the Message
     *
     * @return The Message
     */
    public String getMessage(  )
    {
        return _strMessage;
    }
    
    /**
     * Sets the Message
     *
     * @param strMessage The Message
     */
    public void setMessage( String strMessage )
    {
        _strMessage = strMessage;
    }
    
    /**
     * Returns the Format
     *
     * @return The Format
     */
    public int getFormat()
    {
        return _nFormat;
    }
    
    /**
     * Sets the Format
     *
     * @param nFormat The Format
     */
    public void setFormat( int nFormat )
    {
        _nFormat = nFormat;
    }
    
    /**
     * Returns the Attachements map
     *
     * @return The Attachements Map
     */
    public Map getAttachements()
    {
        return _mapAttachements;
    }
    
    /**
     * Sets the MapAttachements
     *
     * @param MapAttachements The MapAttachements
     */
    public void setAttachements( Map mapAttachements )
    {
        _mapAttachements = mapAttachements;
    }
    
}
