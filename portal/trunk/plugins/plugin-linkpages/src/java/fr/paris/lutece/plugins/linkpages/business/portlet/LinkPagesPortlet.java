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
package fr.paris.lutece.plugins.linkpages.business.portlet;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.web.admin.AdminPageJspBean;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents business objects LinkPagesPortlet
 */
public class LinkPagesPortlet extends Portlet
{
    /////////////////////////////////////////////////////////////////////////////////
    // Xml Tags
    public static final String TAG_LINK_PAGE = "link-page";
    public static final String TAG_LINK_PAGE_ID = "link-page-id";
    public static final String TAG_LINK_PAGE_NAME = "link-page-name";
    public static final String TAG_LINK_PAGE_DESCRIPTION = "link-page-description";
    public static final String TAG_LINK_PAGE_IMAGE = "link-page-image";
    public static final String TAG_LINK_PAGES_PORTLET_LIST = "link-pages-portlet";

    /**
     * Sets the identifier of the portlet type to the value specified in the LinkPagesPortletHome class
     */
    public LinkPagesPortlet(  )
    {
        setPortletTypeId( LinkPagesPortletHome.getInstance(  ).getPortletTypeId(  ) );
    }

    /**
     * Returns the Xml code of the Linkpages portlet without XML heading
     *
     * @param request The HTTP Servlet Request
     * @return the Xml code of the Linkpages portlet content
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer strXml = new StringBuffer(  );
        XmlUtil.beginElement( strXml, TAG_LINK_PAGES_PORTLET_LIST );

        Iterator i = LinkPagesPortletHome.getLinkPagesInPortletList( getId(  ) ).iterator(  );

        while ( i.hasNext(  ) )
        {
            Page page = (Page) i.next(  );

            if ( page.isVisible( request ) )
            {
                XmlUtil.beginElement( strXml, TAG_LINK_PAGE );
                XmlUtil.addElement( strXml, TAG_LINK_PAGE_ID, page.getId(  ) );
                XmlUtil.addElement( strXml, TAG_LINK_PAGE_NAME, page.getName(  ) );
                XmlUtil.addElement( strXml, TAG_LINK_PAGE_DESCRIPTION, page.getDescription(  ) );

                AdminPageJspBean adminPage = new AdminPageJspBean(  );

                if ( page.getImageContent(  ) != null )
                {
                    int nImageLength = page.getImageContent(  ).length;

                    if ( nImageLength >= 1 )
                    {
                        String strPageId = new Integer( page.getId(  ) ).toString(  );
                        XmlUtil.addElement( strXml, TAG_LINK_PAGE_IMAGE,
                            adminPage.getResourceImagePage( page, strPageId ) );
                    }
                }

                XmlUtil.endElement( strXml, TAG_LINK_PAGE );
            }
        }

        XmlUtil.endElement( strXml, TAG_LINK_PAGES_PORTLET_LIST );

        return addPortletTags( strXml );
    }

    /**
    * Returns the Xml code of the LinkPage portlet with XML heading
    *
    * @param request The HTTP Servlet Request
    * @return the Xml code of the LinkPage portlet
    */
    public String getXmlDocument( HttpServletRequest request )
    {
        return XmlUtil.getXmlHeader(  ) + getXml( request );
    }

    /**
     * Updates the current instance of the LinkPage Portlet object
     */
    public void update(  )
    {
        LinkPagesPortletHome.getInstance(  ).update( this );
    }

    /**
     * Removes the current instance of the LinkPage Portlet object
     */
    public void remove(  )
    {
        LinkPagesPortletHome.getInstance(  ).remove( this );
    }
}
