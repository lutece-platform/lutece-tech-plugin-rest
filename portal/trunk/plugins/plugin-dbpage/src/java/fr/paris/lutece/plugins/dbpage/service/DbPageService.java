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
package fr.paris.lutece.plugins.dbpage.service;

import fr.paris.lutece.plugins.dbpage.business.DbPage;
import fr.paris.lutece.portal.service.resource.ResourceService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides DbPageService object
 */
public class DbPageService extends ResourceService
{
    private static DbPageService _singleton = new DbPageService(  );
    private static final String PROPERTY_NAME = "dbpage.service.name";
    private static final String PROPERTY_CACHE = "dbpage.service.cache";
    private static final String PROPERTY_LOADERS = "dbpage.service.loaders";
    private static final String PARAMETER_DBPAGE_VALUE = "value";

    /**
     * Private constructor
     */
    private DbPageService(  )
    {
        super(  );
        setCacheKey( PROPERTY_CACHE );
        setNameKey( PROPERTY_NAME );
    }

    /**
     * Returns the instance of the singleton
     *
     * @return The instance of the singleton
     */
    public static DbPageService getInstance(  )
    {
        return _singleton;
    }

    /**
     * Returns the DbPage
     *
     * @param strDbPageName The DbPage's name
     * @return the DbPage
     */
    public DbPage getDbPage( String strDbPageName )
    {
        DbPage dbPage = (DbPage) getResource( strDbPageName );

        if ( dbPage != null )
        {
            dbPage.setWorkgroup( AdminWorkgroupService.normalizeWorkgroupKey( dbPage.getWorkgroup(  ) ) );
        }

        return dbPage;
    }

    /**
     * Returns a Collection of all dbpages
     * @return The Collection of all dbpages
     */
    public Collection<DbPage> getDbPagesCollection(  )
    {
        Collection colDbPages = getResources(  );

        for ( DbPage dbPage : (Collection<DbPage>) colDbPages )
        {
            dbPage.setWorkgroup( AdminWorkgroupService.normalizeWorkgroupKey( dbPage.getWorkgroup(  ) ) );
        }

        return colDbPages;
    }

    /**
     * Returns a list of all dbpages
     * @return The list of all dbpages
     */
    public ReferenceList getDbPagesList(  )
    {
        ReferenceList list = new ReferenceList(  );

        for ( DbPage dbPage : getDbPagesCollection(  ) )
        {
            list.addItem( dbPage.getName(  ), dbPage.getTitle(  ) );
        }

        return list;
    }

    /**
     * Extracts values from the Http request
     * @param request The Http request
     * @return A list of values
     */
    public List getValues( HttpServletRequest request )
    {
        ArrayList list = new ArrayList(  );
        int i = 0;
        String strValue = request.getParameter( PARAMETER_DBPAGE_VALUE + ( i + 1 ) );

        while ( strValue != null )
        {
            list.add( i, strValue );
            i++;
            strValue = request.getParameter( PARAMETER_DBPAGE_VALUE + ( i + 1 ) );
        }

        return list;
    }

    /**
     * Returns the property key that contains the loaders list
     * @return A property key
     */
    protected String getLoadersProperty(  )
    {
        return PROPERTY_LOADERS;
    }
}
