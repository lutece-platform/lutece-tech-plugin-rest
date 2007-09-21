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

import fr.paris.lutece.portal.service.rbac.RBACResource;


/**
 * This class represents business objects NewsLetterTemplate
 */
public class NewsLetterTemplate implements RBACResource
{
    public static final String RESOURCE_TYPE = "NEWSLETTER";
    public static final int CONSTANT_ID_NEWSLETTER = 0;
    public static final int CONSTANT_ID_DOCUMENT = 1;
    public static final String[] TEMPLATE_NAMES = { "template.type.newsletter.label", "template.type.document.label" };
    private static final String EMPTY_STRING = "";
    private int _nId;
    private int _nType;
    private String _strDescription;
    private String _strFileName;
    private String _strPicture;

    /**
     * Creates a new NewsLetterTemplate object.
     */
    public NewsLetterTemplate(  )
    {
    }

    /**
     * Returns the identifier of the template
     *
     * @return the template identifier
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the identifier of the template
     *
     * @param nId the template identifier
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the type of the template
     *
     * @return the template type
     */
    public int getType(  )
    {
        return _nType;
    }

    /**
     * Sets the type of the template
     *
     * @param nType the template type
     */
    public void setType( int nType )
    {
        _nType = nType;
    }

    /**
     * Returns the description of the template
     *
     * @return the template description
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the description of the template
     *
     * @param strDescription the template description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = ( strDescription == null ) ? EMPTY_STRING : strDescription;
    }

    /**
     * Returns the file name of the template
     *
     * @return the template file name
     */
    public String getFileName(  )
    {
        return _strFileName;
    }

    /**
     * Sets the file name of the template
     *
     * @param strFileName the template file name
     */
    public void setFileName( String strFileName )
    {
        _strFileName = ( strFileName == null ) ? EMPTY_STRING : strFileName;
    }

    /**
     * Returns the picture of the template
     *
     * @return the template picture
     */
    public String getPicture(  )
    {
        return _strPicture;
    }

    /**
     * Sets the picture of the template
     *
     * @param strPicture the template picture
     */
    public void setPicture( String strPicture )
    {
        _strPicture = ( strPicture == null ) ? EMPTY_STRING : strPicture;
    }

    /**
     * get the name of the type of template
     *
     * @return a property name associate with this label
     */
    public String getName(  )
    {
        return TEMPLATE_NAMES[getType(  )];
    }

    /**
     * RBAC resource implmentation
     * @return The resource type code
     */
    public String getResourceTypeCode(  )
    {
        return RESOURCE_TYPE;
    }

    /**
     * RBAC resource implmentation
     * @return The resourceId
     */
    public String getResourceId(  )
    {
        return "" + _nId;
    }
}
