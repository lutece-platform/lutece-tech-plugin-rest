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

import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.service.page.PageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;
import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for Portlet objects
 */
public abstract class PortletHome implements PortletHomeInterface
{
    // Static variable pointed at the DAO instance
    private static IPortletDAO _dao = (IPortletDAO) SpringContextService.getBean( "portletDAO" );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns the Portlet whose primary key is specified in parameter
     *
     * @param nKey the portlet identifier
     * @return The portlet object
     */
    public static Portlet findByPrimaryKey( int nKey )
    {
        Portlet portlet = _dao.load( nKey );
        String strHomeClass = portlet.getHomeClassName(  );
        Portlet p = null;

        try
        {
            PortletHomeInterface home = (PortletHomeInterface) Class.forName( strHomeClass ).newInstance(  );
            p = home.getDAO(  ).load( nKey );
            p.copy( portlet );
        }
        catch ( InstantiationException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( IllegalAccessException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( ClassNotFoundException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        return p;
    }

    /**
     * Returns a collection of portlets according to the selected type
     *
     * @param strIdPortletType the portlet type
     * @return the portlets in form of Collection
     */
    public static List<Portlet> findByType( String strIdPortletType )
    {
        return _dao.selectPortletsByType( strIdPortletType );
    }

    /**
     * Returns the list of portlets for the search on publishing
     *
     * @param strPortletName STh name of the portlet
     * @return the list in form of Collection
     */
    public static Collection getPortletsListbyName( String strPortletName )
    {
        return _dao.selectPortletsListbyName( strPortletName );
    }

    /**
     * Returns the stylesheet of the portlet according to the mode
     *
     * @param nIdPortlet the identifier of the portlet
     * @param nIdMode the selected mode
     * @return the stylesheet
     */
    static StyleSheet getXsl( int nIdPortlet, int nIdMode )
    {
        return _dao.selectXslFile( nIdPortlet, nIdMode );
    }

    /**
     * Returns all the styles corresponding to a portlet typeun type de portlet
     *
     * @param strIdPortletType the identifier of the portlet type
     * @return the list of styles in form of ReferenceList
     */
    public static ReferenceList getStylesList( String strIdPortletType )
    {
        return _dao.selectStylesList( strIdPortletType );
    }

    /**
     * Creates a new portlet in the database
     *
     * @param portlet An instance of the portlet to create
     * @return the Portlet instance created
     */
    public synchronized Portlet create( Portlet portlet )
    {
        // Recovery of an identifier for the new portlet
        int nIdPortlet = PortletHome.newPrimaryKey(  );
        portlet.setId( nIdPortlet );

        // Creation of the portlet child
        getDAO(  ).insert( portlet );

        // Creation of the portlet parent
        _dao.insert( portlet );

        // Invalidate the portlet
        invalidate( portlet );

        return portlet;
    }

    /**
     * Recovery of an identifier for the new portlet
     *
     * @return the new primary key
     */
    static int newPrimaryKey(  )
    {
        return _dao.newPrimaryKey(  );
    }

    /**
     * Deletes the portlet in the database
     *
     * @param portlet the portlet to remove
     */
    public synchronized void remove( Portlet portlet )
    {
        // Deleting of the portlet child
        getDAO(  ).delete( portlet.getId(  ) );

        // Deleting of the portlet parent and its alias if exist
        _dao.delete( portlet.getId(  ) );

        // Invalidate the portlet
        invalidate( portlet );
    }

    /**
     * Updates a portlet with the values of the specified portlet instance
     *
     * @param portlet portlet to update
     */
    public void update( Portlet portlet )
    {
        getDAO(  ).store( portlet );
        _dao.store( portlet );

        // Invalidate the portlet
        invalidate( portlet );
    }

    /**
     * Invalidate the portlet which is specified in parameter
     *
     * @param portlet the portlet instance
     */
    public static void invalidate( Portlet portlet )
    {
        PageService.getInstance(  ).invalidateContent( portlet.getPageId(  ) );
    }

    /**
     * Invalidate a portlet whose identifier is specified in paramaeter
     *
     * @param nIdPortlet the portlet identifier
     */
    public static void invalidate( int nIdPortlet )
    {
        Portlet portlet = findByPrimaryKey( nIdPortlet );
        invalidate( portlet );
    }

    /**
     * Indicates if the portlet has alias
     *
     * @param nIdPortlet the portlet identifier
     * @return true if the portlet has alias, false if not.
     */
    public static boolean hasAlias( int nIdPortlet )
    {
        return _dao.hasAlias( nIdPortlet );
    }

    /**
     * Update the status of portlet
     *
     * @param portlet the portlet to remove
     * @param nStatus The status to update
     */
    public static void updateStatus( Portlet portlet, int nStatus )
    {
        // Deleting of the portlet child
        _dao.updateStatus( portlet, nStatus );

        // Invalidate the portlet
        invalidate( portlet );
    }

    /**
     * Returns the instance of the PortletType whose identifier is specified in parameter
     *
     * @param strPortletTypeId the identifier of the portlet type
     * @return the instance of the portlet type
     */
    public static PortletType getPortletType( String strPortletTypeId )
    {
        return _dao.selectPortletType( strPortletTypeId );
    }

    /**
     * Returns the collection of the StyleSheet objects associated to the Style
     *
     * @param nStyleId identifier of the style
     * @return A collection of styles
     */
    public static Collection getPortletListByStyle( int nStyleId )
    {
        return _dao.selectPortletListByStyle( nStyleId );
    }
}
