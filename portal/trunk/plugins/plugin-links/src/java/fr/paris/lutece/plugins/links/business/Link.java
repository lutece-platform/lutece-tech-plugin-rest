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
package fr.paris.lutece.plugins.links.business;

import fr.paris.lutece.plugins.links.util.LinksImageProvider;
import fr.paris.lutece.portal.business.XmlContent;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupResource;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.util.xml.XmlUtil;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents business object Link
 */
public class Link implements XmlContent, AdminWorkgroupResource
{
    private static final String EMPTY_STRING = "";
    private static final String PROPERTY_LINKS_URL_DEFAULT_KEY_NAME = "links.url.defaultKeyName";

    /////////////////////////////////////////////////////////////////////////////////
    // Xml Tags
    private static final String TAG_LINK = "link";
    private static final String TAG_LINK_ID = "link-id";
    private static final String TAG_LINK_NAME = "link-name";
    private static final String TAG_LINK_DESCRIPTION = "link-description";
    private static final String TAG_LINK_DATE = "link-date";
    private static final String TAG_LINK_URL = "link-url";
    private static final String TAG_LINK_IMAGE = "link-image";

    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    private int _nLinkId;
    private String _strName;
    private String _strUrl;
    private String _strDescription;
    private java.sql.Date _date;
    private ReferenceList _listUrls;
    private byte[] _imageContent;
    private String _strWorkgroupKey;
    private String _strMimeType;

    /**
     * Creates a new Link object.
     */
    public Link(  )
    {
    }

    /**
     * Sets the Id of this link
     *
     * @param nId the new Id
     */
    public void setId( int nId )
    {
        _nLinkId = nId;
    }

    /**
     * Returns the Id of this link
     *
     * @return the link Id
     */
    public int getId(  )
    {
        return _nLinkId;
    }

    /**
     * Sets the url of this link
     *
     * @param strUrl the new url
     */
    public void setUrl( String strUrl )
    {
        _strUrl = ( strUrl == null ) ? EMPTY_STRING : strUrl;
    }

    /**
     * Returns the url of this link
     *
     * @return the link url
     */
    public String getUrl(  )
    {
        return _strUrl;
    }

    /**
     * Sets the name of this link
     *
     * @param strName the new name
     */
    public void setName( String strName )
    {
        _strName = ( strName == null ) ? EMPTY_STRING : strName;
    }

    /**
     * Returns the name of this link
     *
     * @return the link name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the description of this link
     *
     * @param strDescription the new name
     */
    public void setDescription( String strDescription )
    {
        _strDescription = ( strDescription == null ) ? EMPTY_STRING : strDescription;
    }

    /**
     * Returns the description of this link
     *
     * @return the link description
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the date of this link
     *
     * @param date The new date
     */
    public void setDate( java.sql.Date date )
    {
        _date = date;
    }

    /**
     * Returns the date of this link
     *
     * @return the link date
     */
    public java.sql.Date getDate(  )
    {
        return _date;
    }

    ////////////////////////////////////////////////////////////////////////////
    // XML Generation

    /**
     * Returns the xml of this link
     *
     * @param request The HTTP Servlet request
     * @return the link xml
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer strXml = new StringBuffer(  );
        XmlUtil.beginElement( strXml, TAG_LINK );

        String strId = Integer.toString( getId(  ) );
        XmlUtil.addElement( strXml, TAG_LINK_ID, strId );
        XmlUtil.addElementHtml( strXml, TAG_LINK_NAME, getName(  ) );
        XmlUtil.addElementHtml( strXml, TAG_LINK_DESCRIPTION, getDescription(  ) );
        XmlUtil.addElement( strXml, TAG_LINK_DATE, DateUtil.getDateString( getDate(  ) ) );

        String strHostKey = AppPathService.getVirtualHostKey( request );

        XmlUtil.addElementHtml( strXml, TAG_LINK_URL, getUrl( strHostKey ) );

        //set url image
        UrlItem url = new UrlItem( Parameters.IMAGE_SERVLET );
        url.addParameter( Parameters.RESOURCE_TYPE, LinksImageProvider.getInstance(  ).getResourceTypeId(  ) );
        url.addParameter( Parameters.RESOURCE_ID, getId(  ) );
        XmlUtil.addElement( strXml, TAG_LINK_IMAGE, url.getUrlWithEntity(  ) );

        XmlUtil.endElement( strXml, TAG_LINK );

        return strXml.toString(  );
    }

    /**
     * Get the path of the image
     *
     * @return Image path
     */
    public String getImagePath(  )
    {
        UrlItem url = new UrlItem( Parameters.IMAGE_SERVLET );
        url.addParameter( Parameters.RESOURCE_TYPE, LinksImageProvider.getInstance(  ).getResourceTypeId(  ) );
        url.addParameter( Parameters.RESOURCE_ID, getId(  ) );

        return url.getUrl(  );
    }

    /**
     * get optional url associated with strHostKey if any
     * @param strHostKey the key of the url host
     * @return the optional url or null if no url
     */
    public String getOptionalUrl( String strHostKey )
    {
        if ( strHostKey == null )
        {
            return null;
        }

        for ( ReferenceItem item : this._listUrls )
        {
            if ( strHostKey.equals( item.getCode(  ) ) )
            {
                return item.getName(  );
            }
        }

        return null;
    }

    /**
     * Returns the url of this link according to the virtual host key
     *
     * @param strHostKey virtual host key or null for the default url
     * @return the link url or null if the url isn't defined for this virtual host
     */
    public String getUrl( String strHostKey )
    {
        String strUrl = null;
        String strDefaultKey = AppPropertiesService.getProperty( PROPERTY_LINKS_URL_DEFAULT_KEY_NAME, "" );

        if ( ( strHostKey == null ) || "".equals( strHostKey ) || strDefaultKey.equals( strHostKey ) )
        {
            // default url
            strUrl = getUrl(  );
        }
        else
        {
            // optional url
            strUrl = getOptionalUrl( strHostKey );
        }

        return strUrl;
    }

    /**
    * Returns the xml document of this link
    *
    * @param request The HTTP servlet request
    * @return the link xml document
    */
    public String getXmlDocument( HttpServletRequest request )
    {
        return XmlUtil.getXmlHeader(  ) + getXml( request );
    }

    /**
     * set optional urls from a referenceList
     * @param listUrls list of (hostKey, url)
     */
    public void setOptionalUrls( ReferenceList listUrls )
    {
        this._listUrls = listUrls;
    }

    /**
     * get ReferenceList of optional urls
     * @return list of (hostKey, url)
     */
    public ReferenceList getOptionalUrls(  )
    {
        return this._listUrls;
    }

    /**
     * Returns the ImageContent
     *
     * @return The ImageContent
     */
    public byte[] getImageContent(  )
    {
        return _imageContent;
    }

    /**
     * Sets the ImageContent
     *
     * @param content The ImageContent
     */
    public void setImageContent( byte[] content )
    {
        _imageContent = content;
    }

    /**
     * Returns the MimeType
     *
     * @return The MimeType
     */
    public String getMimeType(  )
    {
        return _strMimeType;
    }

    /**
     * Sets the MimeType
     *
     * @param mimeType The MimeType
     */
    public void setMimeType( String mimeType )
    {
        _strMimeType = mimeType;
    }

    /**
     * for methods AdminWorkgroupService.getAuthorizedCollection(Collection<? extends AdminWorkgroupResource> arg0, AdminUser arg1)
     *
     * Returns the Workgroup
     * @return The Workgroup
     */
    public String getWorkgroup(  )
    {
        return _strWorkgroupKey;
    }
    
    /**
     * Returns the WorkgroupKey
     * @return The WorkgroupKey
     */
	public String getWorkgroupKey() 
	{
		return _strWorkgroupKey;
	}
	
	/**
     * Sets the WorkgroupKey
     * @param strWorkgroupKey The WorkgroupKey
     */
	public void setWorkgroupKey(String strWorkgroupKey) 
	{
		_strWorkgroupKey = strWorkgroupKey;
	}
}
