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
package fr.paris.lutece.plugins.dbpage.business.portlet;

import fr.paris.lutece.plugins.dbpage.business.DbPage;
import fr.paris.lutece.plugins.dbpage.service.DbPageService;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents business objects DbPagePortlet
 */
public class DbPagePortlet extends Portlet
{
    private static final String TAG_PORTLET_DBPAGE = "dbpage-portlet";
    private static final String TAG_PORTLET_DBPAGE_CONTENT = "dbpage-portlet-content";
    private static final String VALUES_DELIMITER = ";";
    private String _strDbPageName;
    private String _strValues;

    /**
     * Creates a new DbPagePortlet object.
     */
    public DbPagePortlet(  )
    {
        setPortletTypeId( DbPagePortletHome.getInstance(  ).getPortletTypeId(  ) );
    }

    /**
     * Sets the name of the dbpage
     *
     * @param strDbPageName the dbpage name
     */
    public void setDbPageName( String strDbPageName )
    {
        _strDbPageName = strDbPageName;
    }

    /**
     * Returns the name of this dbpage
     *
     * @return the dbpage name
     */
    public String getDbPageName(  )
    {
        return _strDbPageName;
    }

    /**
     * Returns values of this dbpage
     *
     * @return the dbpage values
     */
    public String getDbValues(  )
    {
        return _strValues;
    }

    /**
     * Sets values of the dbpage
     *
     * @param strValues the values
     */
    public void setValues( String strValues )
    {
        _strValues = ( strValues != null ) ? strValues : "";
    }

    /**
     * Returns values of this dbpage
     *
     * @return the dbpage values
     */
    public List getValuesList(  )
    {
        ArrayList list = new ArrayList(  );

        if ( ( _strValues != null ) && ( !_strValues.equals( "" ) ) )
        {
            StringTokenizer st = new StringTokenizer( _strValues, VALUES_DELIMITER );

            while ( st.hasMoreTokens(  ) )
            {
                list.add( st.nextToken(  ) );
            }
        }

        return list;
    }

    /**
     * Returns the Xml code of the DbPage portlet without XML heading
     *
     * @param request The http request
     * @return the Xml code of the DbPage portlet content
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer strXml = new StringBuffer(  );
        XmlUtil.beginElement( strXml, TAG_PORTLET_DBPAGE );

        DbPage dbpage = DbPageService.getInstance(  ).getDbPage( getDbPageName(  ) );
        List listValues = getValuesList(  );
        String strContent = "";

        if ( dbpage != null )
        {
            strContent = dbpage.getContent( listValues, request );
        }

        XmlUtil.addElementHtml( strXml, TAG_PORTLET_DBPAGE_CONTENT, strContent );
        XmlUtil.endElement( strXml, TAG_PORTLET_DBPAGE );

        return addPortletTags( strXml );
    }

    /**
     * Returns the Xml code of the DbPage portlet with XML heading
     *
     * @param request the HttpServletRequest
     * @return the Xml code of the DbPage portlet
     */
    public String getXmlDocument( HttpServletRequest request )
    {
        return XmlUtil.getXmlHeader(  ) + getXml( request );
    }

    /**
     * Remove portlet
     */
    public void remove(  )
    {
        DbPagePortletHome.getInstance(  ).remove( this );
    }

    /**
     * Update the portlet
     */
    public void update(  )
    {
        DbPagePortletHome.getInstance(  ).update( this );
    }
}
