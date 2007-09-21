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
import fr.paris.lutece.portal.business.portlet.IPortletInterfaceDAO;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;


/**
 * This class provides instances management methods for LinkPortlet objects
 */
public class LinksPortletHome extends PortletHome
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constantes

    /* This class implements the Singleton design pattern. */
    private static LinksPortletHome _singleton = null;

    // Static variable pointed at the DAO instance
    private static ILinksPortletDAO _dao = (ILinksPortletDAO) SpringContextService.getPluginBean( "links",
            "linksPortletDAO" );

    /**
     * Constructor
     */
    public LinksPortletHome(  )
    {
        if ( _singleton == null )
        {
            _singleton = this;
        }
    }

    /**
     * Returns The type of the portlet
     *
     * @return The type of the portlet (integer)
     */
    public String getPortletTypeId(  )
    {
        String strCurrentClassName = this.getClass(  ).getName(  );
        String strPortletTypeId = PortletTypeHome.getPortletTypeId( strCurrentClassName );

        return strPortletTypeId;
    }

    /**
     * Returns the instance of LinksPortletHome
     *
     * @return the LinksPortletHome instance
     */
    public static PortletHome getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new LinksPortletHome(  );
        }
        return _singleton;
    }

    /**
     * Returns the instance of the Link portlet DAO singleton
     *
     * @return the instance of the DAO singleton
     */
    public IPortletInterfaceDAO getDAO(  )
    {
        //        return LinksPortletDAO.getInstance(  );
        return _dao;
    }

    /**
     * Returns a list of all the links
     *
     * @return a list of links
     */
    public static ReferenceList getLinksList(  )
    {
        return _dao.selectLinksList(  );
    }

    /**
     * Returns the list of links in a specified portlet
     *
     * @param nPortletId The identifier of the portlet to check
     * @return A collection of links objects
     */
    public static Collection<Link> getLinksInPortletList( int nPortletId )
    {
        return _dao.selectLinksInPortletList( nPortletId );
    }

    /**
     * Insert a specified link in a specified Links portlet with a specified order
     *
     * @param nPortletId The identifier of the portlet to be inserted
     * @param nLinkId The identifier of the link to insert
     * @param nOrder The order of the inserted link
     */
    public static void insertLink( int nPortletId, int nLinkId, int nOrder )
    {
        _dao.insertLink( nPortletId, nLinkId, nOrder );

        // Invalidate portlet
        invalidate( nPortletId );
    }

    /**
     * Remove a link from a portlet
     *
     * @param nPortletId The identifier of the portlet from wich the link is removed
     * @param nLinkId The identifier of the link to remove
     */
    public static void removeLink( int nPortletId, int nLinkId )
    {
        _dao.deleteLink( nPortletId, nLinkId );

        // Invalidate portlet
        invalidate( nPortletId );
    }

    /**
     * Removes a link from all the portlets
         * @param nLinkId The identifier of the link
     */
    public static void removeLinkFromPortlets( int nLinkId )
    {
        _dao.removeLinkFromPortlets( nLinkId );
    }

    /**
     * Verify if an link is already in a portlet
     *
     * @param nPortletId The identifier of the portlet to check
     * @param nLinkId The identifier of the link to inscribe
     * @return A boolean of the result
     */
    public static boolean testDuplicate( int nPortletId, int nLinkId )
    {
        return _dao.testDuplicate( nPortletId, nLinkId );
    }

    /**
     * Return the order of an link in a specified portlet
     *
     * @param nPortletId The identifier of the portlet to check
     * @param nLinkId The identifier of the link
     * @return The order of the link
     */
    public static int getLinkOrder( int nPortletId, int nLinkId )
    {
        return _dao.selectLinkOrder( nPortletId, nLinkId );
    }

    /**
     * Return the max order of a portlet
     *
     * @param nPortletId The identifier of the portlet
     * @return The max order
     */
    public static int getMaxOrder( int nPortletId )
    {
        return _dao.selectMaxOrder( nPortletId );
    }

    /**
     * Returns the maximum order of the portlets in the links page
     *
     * @return the maximum order
     */
    public static int getPortletMaxOrder(  )
    {
        return _dao.selectPortletMaxOrder(  );
    }

    /**
     * Update the order of a specified link in a specified portlet
     *
     * @param nOrder The new order of the link
     * @param nPortletId The identifier of the portlet to update
     * @param nLinkId The identifier of the link who has a new order
     */
    public static void updateLinkOrder( int nOrder, int nPortletId, int nLinkId )
    {
        _dao.storeLinkOrder( nOrder, nPortletId, nLinkId );
        invalidate( nPortletId );
    }

    /**
     * Return the Id of an link by is order in a portlet
     *
     * @param nPortletId The identifier of the portlet in wich the link is inscribed
     * @param nOrder The  order of the link
     * @return The identifier of the link
     */
    public static int getLinkIdByOrder( int nPortletId, int nOrder )
    {
        return _dao.selectLinkIdByOrder( nPortletId, nOrder );
    }

    /**
     * Returns Links portlet list
     *
     * @return the list of the links portlets in the database as a links portlets Collection object
     */
    public static ReferenceList findUnselectedPortlets(  )
    {
        return _dao.findUnselectedPortlets(  );
    }

    /**
     * Returns a Collection of all the portlets of the links page
     *
     * @return collection
     */
    public static Collection<Portlet> getPortletsInLinksPage(  )
    {
        return _dao.selectPortletsInLinksPage(  );
    }

    /**
     * Returns the order of a portlet in the links page
     *
     * @param nPortletId The identifier of the portlet
     * @return order
     */
    public static int getPortletOrder( int nPortletId )
    {
        return _dao.selectPortletOrder( nPortletId );
    }

    /**
     * Removes a portlet from the links page
     *
         * @param nPortletId The identifier of the portlet
     */
    public static void removePortlet( int nPortletId )
    {
        _dao.removePortlet( nPortletId );
    }

    /**
     * Inserts a portlet in the links page
     *
         * @param nPortletId The identifier of the portlet
     * @param nOrder The order of the portlet
     */
    public static void insertPortlet( int nPortletId, int nOrder )
    {
        _dao.insertPortlet( nPortletId, nOrder );
    }

    /**
     * Returns the Id of a portlet from its order in the links page
     *
         * @param nOrder The order of the portlet
     * @return the portlet Id
     */
    public static int getPortletIdByOrder( int nOrder )
    {
        return _dao.selectPortletIdByOrder( nOrder );
    }

    /**
     * Updates the order of a portlet in the links page
     *
         * @param nOrder The order of the portlet
         * @param nPortletId The identifier of the portlet
     */
    public static void updatePortletOrder( int nOrder, int nPortletId )
    {
        _dao.storePortletOrder( nOrder, nPortletId );
    }
}
