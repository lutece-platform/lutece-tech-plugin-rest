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
package fr.paris.lutece.plugins.newsletter.business;

import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupResource;

import java.sql.Timestamp;


/**
 * This class represents business NewsLetter Object
 */
public class NewsLetter implements AdminWorkgroupResource
{
    private static final String EMPTY_STRING = "";

    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    private int _nId;
    private String _strName;
    private Timestamp _dateLastSending;
    private String _strHtml;
    private int _nNewsLetterTemplateId;
    private int _nDocumentTemplateId;
    private String _strWorkgroup;
    private String _strTestRecipients;
    private String _strUnsubscribe;
    private String _strNewsletterSenderMail;

    /**
     * Returns the identifier of the newsletter
     *
     * @return the newsletter identifier
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the identifier of the newsletter
     *
     * @param nId the newsletter identifier
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the name of the newsletter
     *
     * @return the newsletter name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the name of the newsletter
     *
     * @param strName the newsletter name
     */
    public void setName( String strName )
    {
        _strName = ( strName == null ) ? EMPTY_STRING : strName;
    }

    /**
    * Returns the unsubscription of the newsletter
    *
    * @return the newsletter unsubscrition value
    */
    public String getUnsubscribe(  )
    {
        return _strUnsubscribe;
    }

    /**
     * Sets the value of the unsubscription to the newsletter
     *
     * @param strUnsubscribe the newsletter name
     */
    public void setUnsubscribe( String strUnsubscribe )
    {
        _strUnsubscribe = ( strUnsubscribe == null ) ? EMPTY_STRING : strUnsubscribe;
    }

    /**
     * Sets the date of the last newsletter sending
     *
     * @param dateLastSending the last date of sending
     */
    public void setDateLastSending( Timestamp dateLastSending )
    {
        _dateLastSending = dateLastSending;
    }

    /**
     * Returns the date of the last sending
     *
     * @return the last sending date
     */
    public Timestamp getDateLastSending(  )
    {
        return _dateLastSending;
    }

    /**
     * Returns the html content of the newsletter
     *
     * @return the html content of the newsletter
     */
    public String getHtml(  )
    {
        return _strHtml;
    }

    /**
     * Sets the html content of the newsletter
     *
     * @param strHtml the html content of the newsletter
     */
    public void setHtml( String strHtml )
    {
        _strHtml = ( strHtml == null ) ? EMPTY_STRING : strHtml;
    }

    /**
     * Returns the identifier of the newsletter template
     *
     * @return the newsletter template identifier
     */
    public int getNewsLetterTemplateId(  )
    {
        return _nNewsLetterTemplateId;
    }

    /**
     * Sets the identifier of the newsletter template
     *
     * @param nNewsLetterTemplateId the newsletter template identifier
     */
    public void setNewsLetterTemplateId( int nNewsLetterTemplateId )
    {
        _nNewsLetterTemplateId = nNewsLetterTemplateId;
    }

    /**
     * Returns the identifier of the document template
     *
     * @return the Document template identifier
     */
    public int getDocumentTemplateId(  )
    {
        return _nDocumentTemplateId;
    }

    /**
     * Sets the identifier of the Document template
     *
     * @param nDocumentTemplateId the Document template identifier
     */
    public void setDocumentTemplateId( int nDocumentTemplateId )
    {
        _nDocumentTemplateId = nDocumentTemplateId;
    }

    /**
     * Returns the workgroup
     * @return The workgroup
     */
    public String getWorkgroup(  )
    {
        return _strWorkgroup;
    }

    /**
     * Sets the workgroup
     * @param strWorkgroup The workgroup
     */
    public void setWorkgroup( String strWorkgroup )
    {
        _strWorkgroup = strWorkgroup;
    }

    /**
    * Returns the test recipients
    * @return The test recipients
    */
    public String getTestRecipients(  )
    {
        return _strTestRecipients;
    }

    /**
     * Sets the test recipients
     * @param strTestRecipients The Test recipients
     */
    public void setTestRecipients( String strTestRecipients )
    {
        _strTestRecipients = strTestRecipients;
    }

    /**
    * Returns the Newsletter sender mail
    * @return The Newsletter sender mail
    */
    public String getNewsletterSenderMail(  )
    {
        return _strNewsletterSenderMail;
    }

    /**
     * Sets the Newsletter sender mail
     * @param strNewsletterSenderMail The NewsletterSenderMail
     */
    public void setNewsletterSenderMail( String strNewsletterSenderMail )
    {
        _strNewsletterSenderMail = strNewsletterSenderMail;
    }
}
