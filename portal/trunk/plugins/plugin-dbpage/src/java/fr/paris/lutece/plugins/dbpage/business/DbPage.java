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
package fr.paris.lutece.plugins.dbpage.business;

import fr.paris.lutece.plugins.dbpage.business.section.DbPageSection;
import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.service.resource.Resource;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupResource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents business object Page
 */
public class DbPage implements Resource, AdminWorkgroupResource
{
    private static final String EMPTY_STRING = "";
    public static final String RESOURCE_TYPE = "DBPAGE";

    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    private int _nNbSection;
    private String _strName;
    private String _strTitle;
    private String _strWorkgroup;
    private List<DbPageSection> _listSections;

    /**
     * Returns the number of sections of this dbpage.
     *
     * @return nNbSection the  number of sections
     */
    public int getNbSection(  )
    {
        return _nNbSection;
    }

    /**
     * Sets the number of section of the dbpage to the specified integer.
     *
     * @param nNbSection the new number of section
     */
    public void setNbSection( int nNbSection )
    {
        _nNbSection = nNbSection;
    }

    /**
     * Returns the name of this dbpage.
     *
     * @return the dbpage name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the name of the dbpage to the specified string.
     *
     * @param strName the new name
     */
    public void setName( String strName )
    {
        _strName = ( strName == null ) ? EMPTY_STRING : strName;
    }

    /**
     * Returns the title of this dbpage.
     *
     * @return the dbpage title
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * Sets the title of the dbpage to the specified string.
     *
     * @param strTitle the new email
     */
    public void setTitle( String strTitle )
    {
        _strTitle = ( strTitle == null ) ? EMPTY_STRING : strTitle;
    }

    /**
     * Returns the sections of this dbpage.
     *
     * @return the dbpage sections
     */
    public List<DbPageSection> getListSection(  )
    {
        return _listSections;
    }

    /**
     * Sets the sections of the dbpage to the specified list.
     * @param listSections Sets a list of sections
     */
    public void setListSection( List<DbPageSection> listSections )
    {
        _listSections = listSections;
    }

    public String getWorkgroup(  )
    {
        return _strWorkgroup;
    }

    public void setWorkgroup( String strWorkgroup )
    {
        _strWorkgroup = ( strWorkgroup == null ) ? EMPTY_STRING : strWorkgroup;
    }

    /**
     * Returns the DbPage Content
     * @param request The request
     * @param listValues The list of Values of parameters
     * @return The HTML code of the DbPage content
     */
    public String getContent( List listValues, HttpServletRequest request )
    {
        StringBuffer sbSections = new StringBuffer(  );

        for ( DbPageSection section : _listSections )
        {
            if ( isVisible( request, section.getRole(  ) ) )
            {
                sbSections.append( section.getHtmlSection( listValues, request ) );
            }
        }

        return sbSections.toString(  );
    }

    /**
     * Checks if the page is visible for the current user
     * @param request The HTTP request
     * @return true if the page could be shown to the user
     * @since v1.3.1
     */
    private boolean isVisible( HttpServletRequest request, String strRole )
    {
        if ( ( strRole == null ) || ( strRole.trim(  ).equals( EMPTY_STRING ) ) )
        {
            return true;
        }

        if ( !strRole.equals( Page.ROLE_NONE ) && SecurityService.isAuthenticationEnable(  ) )
        {
            return SecurityService.getInstance(  ).isUserInRole( request, strRole );
        }

        return true;
    }

    /**
     * Checks if the page is visible for the current user
     * @param request The HTTP request
     * @return true if the page could be shown to the user
     * @since v1.3.1
     */
    public boolean isVisible( HttpServletRequest request )
    {
        return isVisible( request, getWorkgroup(  ) );
    }
}
