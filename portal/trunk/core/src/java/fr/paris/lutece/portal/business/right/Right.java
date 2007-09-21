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
package fr.paris.lutece.portal.business.right;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.i18n.Localizable;

import java.util.Locale;


/**
 * This class represents business objects right
 */
public class Right implements Localizable
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String EMPTY_STRING = "";
    private String _strId;
    private String _strNameKey;
    private String _strDescriptionKey;
    private int _nLevel;
    private String _strUrl;
    private String _strPluginName;
    private String _strFeatureGroup;
    private String _strIconUrl;
    private Locale _locale;

    /**
     * set the local used by  this right
     *
     * @param locale the local to use
     */
    public void setLocale( Locale locale )
    {
        _locale = locale;
    }

    /**
     * Returns the identifier of this right
     *
     * @return the identifier of this right
     */
    public String getId(  )
    {
        return _strId;
    }

    /**
     * Sets the identifier of the right to the specified string.
     *
     * @param strId the new identifier
     */
    public void setId( String strId )
    {
        _strId = strId;
    }

    /**
     * Returns the name of this right.
     *
     * @return the right name
     */
    public String getNameKey(  )
    {
        return _strNameKey;
    }

    /**
     * Returns the name of this right.
     *
     * @return the right name
     */
    public String getName(  )
    {
        return I18nService.getLocalizedString( _strNameKey, _locale );
    }

    /**
     * Sets the name of the right to the specified string.
     *
     * @param strNameKey the new name
     */
    public void setNameKey( String strNameKey )
    {
        _strNameKey = ( strNameKey == null ) ? EMPTY_STRING : strNameKey;
    }

    /**
     * Returns the level of this right.
     *
     * @return the right level
     */
    public int getLevel(  )
    {
        return _nLevel;
    }

    /**
     * Sets the level of the right to the specified int.
     *
     * @param nLevel the new level
     */
    public void setLevel( int nLevel )
    {
        _nLevel = nLevel;
    }

    /**
     * Returns the url of the jsp component which manages this right.
     *
     * @return the right url function
     */
    public String getUrl(  )
    {
        return _strUrl;
    }

    /**
     * Sets the url of the right to the specified string.
     *
     * @param strUrl the new url
     */
    public void setUrl( String strUrl )
    {
        _strUrl = strUrl;
    }

    /**
     * Returns the description of this right.
     *
     * @return the right description
     */
    public String getDescriptionKey(  )
    {
        return _strDescriptionKey;
    }

    /**
     * Returns the description of this right.
     *
     * @return the right description
     */
    public String getDescription(  )
    {
        return I18nService.getLocalizedString( _strDescriptionKey, _locale );
    }

    /**
     * Sets the description of the right to the specified string.
     *
     * @param strDescriptionKey the new description
     */
    public void setDescriptionKey( String strDescriptionKey )
    {
        _strDescriptionKey = ( strDescriptionKey == null ) ? EMPTY_STRING : strDescriptionKey;
    }

    /**
     * Returns the isUpdatable tag of this right ( 1 if the right is updatable, 0 if not ).
     *
     * @return the is_upda
     */
    public String getPluginName(  )
    {
        return _strPluginName;
    }

    /**
     * Sets the name of the right to the specified string.
     *
     * @param strPluginName the new name
     */
    public void setPluginName( String strPluginName )
    {
        _strPluginName = ( strPluginName == null ) ? EMPTY_STRING : strPluginName;
    }

    /**
     * Returns the feature group of this right.
     *
     * @return the right feature group
         * @since 1.1.1
     */
    public String getFeatureGroup(  )
    {
        return _strFeatureGroup;
    }

    /**
     * Sets the feature group of the right to the specified string.
     *
     * @param strFeatureGroup the new feature group
         * @since 1.1.1
     */
    public void setFeatureGroup( String strFeatureGroup )
    {
        _strFeatureGroup = ( strFeatureGroup == null ) ? EMPTY_STRING : strFeatureGroup;
    }

    /**
     * Returns the url of the icon associated to the right.
     *
     * @return the icon url
     */
    public String getIconUrl(  )
    {
        return _strIconUrl;
    }

    /**
     * Sets the url of the icon associated to the right.
     *
     * @param strIconUrl the new url
     */
    public void setIconUrl( String strIconUrl )
    {
        _strIconUrl = strIconUrl;
    }
}
