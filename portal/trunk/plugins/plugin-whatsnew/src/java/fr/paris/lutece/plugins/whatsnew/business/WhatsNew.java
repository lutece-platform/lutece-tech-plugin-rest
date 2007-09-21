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
package fr.paris.lutece.plugins.whatsnew.business;

import fr.paris.lutece.portal.business.XmlContent;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.xml.XmlUtil;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;


/**
 *  This class represents business object WhatsNew
 */
public class WhatsNew implements XmlContent
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants
    /** parameters. */
    public static final String PARAMETER_DOCUMENT_ID = "document_id";

    /** constants to refer to the styles */
    public static final int TYPE_PORTLET = 1;
    public static final int TYPE_PAGE = 2;
    public static final int TYPE_DOCUMENT = 0;

    /** properties. */
    private static final String PROPERTY_PREFIX = "portlet.whatsnew";
    private static final String PROPERTY_FRAGMENT_TYPE_DOCUMENT = ".type.document";
    private static final String PROPERTY_FRAGMENT_TYPE_PORTLET = ".type.portlet";
    private static final String PROPERTY_FRAGMENT_TYPE_PAGE = ".type.page";

    /** Tags. */
    private static final String TAG_WHATS_NEW_ELEMENT = "whatsnew-element";
    private static final String TAG_WHATS_NEW_TITLE = "whatsnew-title";
    private static final String TAG_WHATS_NEW_TYPE = "whatsnew-type";
    private static final String TAG_WHATS_NEW_DESCRIPTION = "whatsnew-description";
    private static final String TAG_WHATS_NEW_DATE_UPDATE = "whatsnew-date-update";
    private static final String TAG_WHATS_NEW_URL = "whatsnew-url";

    ////////////////////////////////////////////////////////////////////////////
    // Class attributes

    /** Type of the whatsnew object(Document (0), Portlet(1), Page(2)) */
    private int _nType;

    /** Title of the whatsnew object */
    private String _strTitle;

    /** Description of the whatsnew object */
    private String _strDescription;

    /** Date of the last update */
    private Timestamp _dateUpdate;

    /** ids that are used to build the url to the element */
    private int _nPageId;
    private int _nPortletId;
    private int _nDocumentId;

    /**
     * Creates a new WhatsNew object.
     */
    public WhatsNew(  )
    {
    }

    /**
     * Returns the type of the whatsnew object
     * @return the type of the whatsnew object
     */
    public int getType(  )
    {
        return _nType;
    }

    /**
     * Returns the title of the whatsnew object
     * @return the title of the whatsnew object
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * Returns the description of the whatsnew object
     * @return the description of the whatsnew object
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Returns the date of the last update of the whatsnew object
     * @return the date of the last update of the whatsnew object
     */
    public Timestamp getDateUpdate(  )
    {
        return _dateUpdate;
    }

    /**
     * Sets the type of the whatsnew object to the specified String
     * @param nType the new type of the whatsnew object
     */
    public void setType( int nType )
    {
        _nType = nType;
    }

    /**
     * Sets the title of the whatsnew object to the specified String
     * @param strTitle the new title of the whatsnew object
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Sets the description of the whatsnew object to the specified String
     * @param strDescription the new description of the whatsnew object
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Sets the date of the last update of the whatsnew object
     * @param date the date update of the whatsnew object
     */
    public void setDateUpdate( Timestamp date )
    {
        _dateUpdate = date;
    }

    /**
     * @return Returns the _nDocumentId.
     */
    public int getDocumentId(  )
    {
        return _nDocumentId;
    }

    /**
     * @param nArticleId The _nArticleId to set.
     */
    public void setDocumentId( int nDocumentId )
    {
        _nDocumentId = nDocumentId;
    }

    /**
     * @return Returns the _nPageId.
     */
    public int getPageId(  )
    {
        return _nPageId;
    }

    /**
     * @param nPageId The _nPageId to set.
     */
    public void setPageId( int nPageId )
    {
        _nPageId = nPageId;
    }

    /**
     * @return Returns the _nPortletId.
     */
    public int getPortletId(  )
    {
        return _nPortletId;
    }

    /**
     * @param nPortletId The _nPortletId to set.
     */
    public void setPortletId( int nPortletId )
    {
        _nPortletId = nPortletId;
    }

    /////////////////////////////////////////////////////////////////////////////
    // XML generation

    /**
     * Builds the XML content of this whatsnew object and returns it
     * @param request The HTTP Servlet request
     * @return the Xml content of this whatsnew object
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer strXml = new StringBuffer(  );
        XmlUtil.beginElement( strXml, TAG_WHATS_NEW_ELEMENT );

        String strType = "";

        if ( getType(  ) == TYPE_DOCUMENT )
        {
            strType = AppPropertiesService.getProperty( getPropertiesPrefix(  ) + PROPERTY_FRAGMENT_TYPE_DOCUMENT );
        }
        else if ( getType(  ) == TYPE_PORTLET )
        {
            strType = AppPropertiesService.getProperty( getPropertiesPrefix(  ) + PROPERTY_FRAGMENT_TYPE_PORTLET );
        }
        else if ( getType(  ) == TYPE_PAGE )
        {
            strType = AppPropertiesService.getProperty( getPropertiesPrefix(  ) + PROPERTY_FRAGMENT_TYPE_PAGE );
        }
        else
        {
            strType = "";
        }

        XmlUtil.addElementHtml( strXml, TAG_WHATS_NEW_TYPE, strType );
        XmlUtil.addElementHtml( strXml, TAG_WHATS_NEW_TITLE, getTitle(  ) );

        String strDescription = getDescription(  );

        if ( ( strDescription != null ) && ( !strDescription.trim(  ).equals( "" ) ) )
        {
            XmlUtil.addElementHtml( strXml, TAG_WHATS_NEW_DESCRIPTION, strDescription );
        }

        XmlUtil.addElementHtml( strXml, TAG_WHATS_NEW_DATE_UPDATE, DateUtil.getDateString( getDateUpdate(  ) ) );

        XmlUtil.addElementHtml( strXml, TAG_WHATS_NEW_URL, buildUrl(  ) );

        XmlUtil.endElement( strXml, TAG_WHATS_NEW_ELEMENT );

        return strXml.toString(  );
    }

    /**
     * Adds the XML header to the XML content of this whatsnew object and returns it
         * @param request The HTTP Servlet request
         * @return the Xml document with header
     */
    public String getXmlDocument( HttpServletRequest request )
    {
        return XmlUtil.getXmlHeader(  ) + getXml( request );
    }

    /**
     * Returns portlet's properties prefix
     *
     * @return prefix
     */
    public String getPropertiesPrefix(  )
    {
        return PROPERTY_PREFIX;
    }

    /**
     * Build the url to insert into the xml to provide a link to the element.
     * the url depends on the type.
     * @return the url to insert.
     */
    private String buildUrl(  )
    {
        // initialise the url (to the element). Using only '?' will allow us to use the current page (can be Portal.jsp or StandAloneApp.jsp)
        String strUrl = "?";

        // build the url depending on the type
        if ( getType(  ) == TYPE_PAGE )
        {
            strUrl += Parameters.PAGE_ID;
            strUrl += "=";
            strUrl += getPageId(  );
        }
        else if ( getType(  ) == TYPE_PORTLET )
        {
            strUrl += Parameters.PAGE_ID;
            strUrl += "=";
            strUrl += String.valueOf( getPageId(  ) );
            strUrl += "#";
            strUrl += Parameters.PORTLET_ID;
            strUrl += "=";
            strUrl += String.valueOf( getPortletId(  ) );
        }
        else if ( getType(  ) == TYPE_DOCUMENT )
        {
            strUrl += PARAMETER_DOCUMENT_ID;
            strUrl += "=";
            strUrl += String.valueOf( getDocumentId(  ) );
            strUrl += "&";
            strUrl += Parameters.PORTLET_ID;
            strUrl += "=";
            strUrl += String.valueOf( getPortletId(  ) );
        }

        return strUrl;
    }
}
