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
package fr.paris.lutece.portal.business.portlet;

import fr.paris.lutece.portal.business.XmlContent;
import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.xml.XmlUtil;

import java.sql.Timestamp;

import java.util.Map;


/**
 * This class represents business objects Portlet. It is the base class of all portlets. It is abstract and the
 * implementation of the interface XmlContent is compulsary.
 */
public abstract class Portlet implements XmlContent
{
    ////////////////////////////////////////////////////////////////////////////
    // Privates variables common to all the portlets
    private static final int MODE_NORMAL = 0;
    private static final int MODE_ADMIN = 1;
    private int _nId;
    private int _nPageId;
    private int _nStyleId;
    private int _nColumn;
    private int _nOrder;
    private int _nStatus;
    private int _nAcceptAlias;
    private int _nDisplayPortletTitle;
    private String _strName;
    private String _strPortletTypeId;
    private String _strPortletTypeName;
    private String _strUrlCreation;
    private String _strUrlUpdate;
    private String _strPluginName;
    private String _strHomeClassName;
    private Timestamp _dateUpdate;

    ////////////////////////////////////////////////////////////////////////////
    // Accessors

    /**
     * Returns the identifier of this portlet.
     *
     * @return the portlet identifier
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the identifier of the portlet to the specified int.
     *
     * @param nId the new identifier
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the style identifier of this portlet
     *
     * @return the style identifier
     */
    public int getStyleId(  )
    {
        return _nStyleId;
    }

    /**
     * Sets the identifier of the portlet style with the specified int.
     *
     * @param nStyleId the new style identifier
     */
    public void setStyleId( int nStyleId )
    {
        _nStyleId = nStyleId;
    }

    /**
     * Returns the page identifier associated to this portlet
     *
     * @return the page identifier
     */
    public int getPageId(  )
    {
        return _nPageId;
    }

    /**
     * Sets the identifier of the portlet style with the specified int.
     *
     * @param nPageId The identifier of the page
     */
    public void setPageId( int nPageId )
    {
        _nPageId = nPageId;
    }

    /**
     * Returns the identifier of this portlet.
     *
     * @return the portlet identifier
     */
    public int getStatus(  )
    {
        return _nStatus;
    }

    /**
     * Sets the identifier of the portlet to the specified int.
     *
     * @param nStatus the new status
     */
    public void setStatus( int nStatus )
    {
        _nStatus = nStatus;
    }

    /**
     * Returns the name of this portlet
     *
     * @return the portlet name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the name of this portlet to the specified string.
     *
     * @param strName the new name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the identifier of the portlet type of this portlet which caracterizes the portlet.
     *
     * @return the portlet type identifier
     */
    public String getPortletTypeId(  )
    {
        return _strPortletTypeId;
    }

    /**
     * Sets the identifier of the portlet type to the specified int.
     *
     * @param strPortletTypeId the portlet type identifier
     */
    public void setPortletTypeId( String strPortletTypeId )
    {
        _strPortletTypeId = strPortletTypeId;
    }

    /**
     * Returns the portlet type name of this portlet
     *
     * @return the portlet type name
     */
    public String getPortletTypeName(  )
    {
        return _strPortletTypeName;
    }

    /**
     * Sets the name of this portlet type with the specified string.
     *
     * @param strPortletTypeName the new portlet type name
     */
    public void setPortletTypeName( String strPortletTypeName )
    {
        _strPortletTypeName = strPortletTypeName;
    }

    /**
     * Returns the url of the program which manages the creation of a portlet
     *
     * @return the url of the creation
     */
    public String getUrlCreation(  )
    {
        return _strUrlCreation;
    }

    /**
     * Sets the url of the program which creates this portlet
     *
     * @param strUrlCreation The url of creation
     */
    public void setUrlCreation( String strUrlCreation )
    {
        _strUrlCreation = strUrlCreation;
    }

    /**
     * Returns the url of the program which manages the update of a portlet
     *
     * @return the url of the program as a String
     */
    public String getUrlUpdate(  )
    {
        return _strUrlUpdate;
    }

    /**
     * Sets the url of the program which updates this portlet
     *
     * @param strUrlUpdate The url of update
     */
    public void setUrlUpdate( String strUrlUpdate )
    {
        _strUrlUpdate = strUrlUpdate;
    }

    /**
     * Returns the date of update of this portlet
     *
     * @return the update date
     */
    public Timestamp getDateUpdate(  )
    {
        return _dateUpdate;
    }

    /**
     * Sets the date of update of this portlet with the specified date.
     *
     * @param dateUpdate the new date
     */
    public void setDateUpdate( Timestamp dateUpdate )
    {
        _dateUpdate = dateUpdate;
    }

    /**
     * Return the number of the column of this portlet in the page
     *
     * @return the number of the column
     */
    public int getColumn(  )
    {
        return _nColumn;
    }

    /**
     * Sets the number of the column of this portlet in its page with the specified int.
     *
     * @param nColumn the new number of column
     */
    public void setColumn( int nColumn )
    {
        _nColumn = nColumn;
    }

    /**
     * Returns the order of this portlet in the page which contains it.
     *
     * @return the order
     */
    public int getOrder(  )
    {
        return _nOrder;
    }

    /**
     * Sets the order of this portlet in its page with the specified int.
     *
     * @param nType the new order
     */
    public void setOrder( int nType )
    {
        _nOrder = nType;
    }

    /**
     * Returns the name of the java class which manages this type of portlet.
     *
     * @return the java class name
     */
    public String getHomeClassName(  )
    {
        return _strHomeClassName;
    }

    /**
     * Sets the name of the java class which manages this type of portlet with the specified string.
     *
     * @param strHomeClassName The Home Class name
     */
    public void setHomeClassName( String strHomeClassName )
    {
        _strHomeClassName = strHomeClassName;
    }

    /**
     * Indicates if this portlet can be modified after its creation or not.
     *
     * @return 1 if the portlet can be updated, 0 if not
     */
    public int getAcceptAlias(  )
    {
        return _nAcceptAlias;
    }

    /**
     * Sets the flag which indicates that this portlet can be have a title or not.
     *
     * @param nDisplayPortletTitle The flag
     */
    public void setDisplayPortletTitle( int nDisplayPortletTitle )
    {
        _nDisplayPortletTitle = nDisplayPortletTitle;
    }

    /**
     * Indicates if this portlet can be modified have a title or not.
     *
     * @return 1 if the portlet can be have a title, 0 if not
     */
    public int getDisplayPortletTitle(  )
    {
        return _nDisplayPortletTitle;
    }

    /**
     * Sets the flag which indicates that this portlet can be updated or not.
     *
     * @param nAcceptAlias The flag
     */
    public void setAcceptAlias( int nAcceptAlias )
    {
        _nAcceptAlias = nAcceptAlias;
    }

    /**
     * Get the plugin Name
     *
     * @return The pluginName
     */
    public String getPluginName(  )
    {
        return _strPluginName;
    }

    /**
     * Sets the flag which indicates that this portlet can be updated or not.
     *
     * @param strPluginName The flag
     */
    public void setPluginName( String strPluginName )
    {
        _strPluginName = strPluginName;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Operations

    /**
     * This method copies the fields of the portlet specified in this portlet.
     *
     * @param portlet the portlet to copy
     */
    public void copy( Portlet portlet )
    {
        setId( portlet.getId(  ) );
        setPortletTypeId( portlet.getPortletTypeId(  ) );
        setPageId( portlet.getPageId(  ) );
        setStyleId( portlet.getStyleId(  ) );
        setName( portlet.getName(  ) );
        setPortletTypeName( portlet.getPortletTypeName(  ) );
        setUrlCreation( portlet.getUrlCreation(  ) );
        setUrlUpdate( portlet.getUrlUpdate(  ) );
        setDateUpdate( portlet.getDateUpdate(  ) );
        setColumn( portlet.getColumn(  ) );
        setOrder( portlet.getOrder(  ) );
        setAcceptAlias( portlet.getAcceptAlias(  ) );
        setPluginName( portlet.getPluginName(  ) );
        setDisplayPortletTitle( portlet.getDisplayPortletTitle(  ) );
        setStatus( portlet.getStatus(  ) );
    }

    /**
     * Add the common tags to all the portlets to the XML document
     *
     * @param strPortlet The string buffer which contains the XML content of this portlet
     * @return The XML content of this portlet encapsulated by the common tags
     */
    protected String addPortletTags( StringBuffer strPortlet )
    {
        StringBuffer strXml = new StringBuffer(  );
        XmlUtil.beginElement( strXml, TAG_PORTLET );
        XmlUtil.addElementHtml( strXml, TAG_PORTLET_NAME, getName(  ) );
        XmlUtil.addElement( strXml, TAG_PORTLET_ID, getId(  ) );
        XmlUtil.addElement( strXml, TAG_PAGE_ID, getPageId(  ) );
        XmlUtil.addElement( strXml, TAG_PLUGIN_NAME, getPluginName(  ) );
        XmlUtil.addElement( strXml, TAG_DISPLAY_PORTLET_TITLE, getDisplayPortletTitle(  ) );
        strXml.append( strPortlet.toString(  ) );
        XmlUtil.endElement( strXml, TAG_PORTLET );

        return strXml.toString(  );
    }

    /**
     * Recovers the stylesheet of the portlet according to the mode
     *
     * @param nMode the selected mode.
     * @return the name of the stylesheet file
     */
    public String getXslFile( int nMode )
    {
        StyleSheet xsl = new StyleSheet(  );

        // Added in v1.3
        // Use the same stylesheet for normal or admin mode
        switch ( nMode )
        {
            case MODE_NORMAL:
            case MODE_ADMIN:
                xsl = PortletHome.getXsl( getId(  ), MODE_NORMAL );

                break;

            default:
                xsl = PortletHome.getXsl( getId(  ), nMode );

                break;
        }

        return xsl.getFile(  );
    }

    /**
     * Recovers the stylesheet of the portlet according to the mode
     *
     * @param nMode the selected mode.
     * @return the content of the stylesheet file
     */
    public byte[] getXslSource( int nMode )
    {
        StyleSheet xsl = new StyleSheet(  );

        // Added in v1.3
        // Use the same stylesheet for normal or admin mode
        switch ( nMode )
        {
            case MODE_NORMAL:
            case MODE_ADMIN:
                xsl = PortletHome.getXsl( getId(  ), MODE_NORMAL );

                break;

            default:
                xsl = PortletHome.getXsl( getId(  ), nMode );

                break;
        }

        return xsl.getSource(  );
    }

    /**
     * Recovers the parameters to use with the stylesheet at the time of the transformation.<br> By default, portlets
     * do not return any parameter
     *
     * @return a collection of the type Dictionary (Use the Hashtable implementation)
     */
    public Map<String, String> getXslParams(  )
    {
        return null;
    }

    /**
     * Remove the portlet. This method MUST be overloaded on the level of the implementation of each portlet
     *
     * @throws AppException
     */
    public void remove(  )
    {
        throw new AppException( "Missing Overload : The remove method of the Portlet class can't be called" );
    }
}
