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
package fr.paris.lutece.plugins.links.business.portlet;

import fr.paris.lutece.plugins.links.business.Link;
import fr.paris.lutece.plugins.links.business.LinkHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents business object LinkPortlet
 */
public class LinksPortlet extends Portlet
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constantes
    private static final String TAG_LINKS_PORTLET = "links-portlet";
    private int _nLinkId;
    private int _nPortletOrder;

    /**
     * Creates a new LinkPortlet object.
     */
    public LinksPortlet(  )
    {
        setPortletTypeId( LinksPortletHome.getInstance(  ).getPortletTypeId(  ) );
    }

    /**
     * Returns the identifier of the Link of this portlet
     *
     * @return The Link identifier
     */
    public int getLinkId(  )
    {
        return _nLinkId;
    }

    /**
     * Sets the identifier of the Link of this LinkPortlet to the specified int
     *
     * @param nLinkId the new identifier of the Link
     */
    public void setLinkId( int nLinkId )
    {
        _nLinkId = nLinkId;
    }

    /**
     * Returns the XML content of the portlet without the XML header
     *
     * @param request The HTTP servlet request
     * @return The Xml content of this portlet
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer strXml = new StringBuffer(  );
        XmlUtil.beginElement( strXml, TAG_LINKS_PORTLET );

        Collection<Link> list = LinkHome.findByPortlet( getId(  ) );

        String strServerKey = AppPathService.getVirtualHostKey( request );

        for ( Link link : list )
        {
            if ( link.getUrl( strServerKey ) != null )
            {
                strXml.append( link.getXml( request ) );
            }
        }

        XmlUtil.endElement( strXml, TAG_LINKS_PORTLET );

        return addPortletTags( strXml );
    }

    /**
     * Returns the XML content of the portlet with the XML header
     *
     * @param request The HTTP servlet request
         * @return The XML content of this portlet
     */
    public String getXmlDocument( HttpServletRequest request )
    {
        return XmlUtil.getXmlHeader(  ) + getXml( request );
    }

    /**
     * Updates this portlet
     */
    public void update(  )
    {
        LinksPortletHome.getInstance(  ).update( this );
    }

    /**
     * Removes this portlet
     */
    public void remove(  )
    {
        LinksPortletHome.getInstance(  ).remove( this );
    }

    /**
     * Return the order of the portlet in the links page
     *
     * @return the order
     */
    public int getPortletOrder(  )
    {
        return _nPortletOrder;
    }

    /**
     * Sets the order of the portlet in the links page
     *
     * @param nPortletOrder The new value
     */
    public void setPortletOrder( int nPortletOrder )
    {
        this._nPortletOrder = nPortletOrder;
    }
}
