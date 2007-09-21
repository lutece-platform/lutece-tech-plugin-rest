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

import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;


/**
 * This class provides instances management methods (create, find, ...) for Link objects
 */
public final class LinkHome
{
    // Static variable pointed at the DAO instance
    private static ILinkDAO _dao = (ILinkDAO) SpringContextService.getPluginBean( "links", "linkDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private LinkHome(  )
    {
    }

    /**
     * Create the link wich is specified in parameter
     *
     * @param link The instance  of link wich contains the data to store
     * @return The instance of the link wich has been created with a primary key.
     */
    public static Link create( Link link )
    {
        _dao.insert( link );

        return link;
    }

    /**
     * Returns an instance of link whose identifier is specified in parameter
     *
     * @param nKey The identifier (primary key) of the link
     * @return An instance of link object
     */
    public static Link findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }

    /**
     * Return the list of all the links
     *
     * @return A collection of links objects
     */
    public static Collection<Link> getLinksList(  )
    {
        return _dao.selectList(  );
    }

    /**
     * Update the link wich is specified in parameter
     *
     * @param link The instance  of link wich contains the data to store
     */
    public static void update( Link link )
    {
        _dao.store( link );
    }

    /**
     * Remove the link whose identifier is specified in parameter
     *
     * @param nLinkId The identifier of the link to remove
     */
    public static void delete( int nLinkId )
    {
        //link.doUnselect();
        _dao.delete( nLinkId );
    }

    /**
     * Return a collection of all the links in a defined portlet
     *
     * @param nPortletId The identifier of the portlet
     * @return a links collection
     */
    public static Collection<Link> findByPortlet( int nPortletId )
    {
        return _dao.selectByPortlet( nPortletId );
    }

    /**
    *
    * @param nLinkId
    * @return ImageResource
    */
    public static ImageResource getImageResource( int nLinkId )
    {
        return _dao.loadImageResource( nLinkId );
    }
}
