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
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;


public interface ILinksPortletDAO extends IPortletInterfaceDAO
{
    /**
     * Insert the portlet
     *
     * @param portlet The Portlet object
     */
    void insert( Portlet portlet );

    /**
     * Insert a new record in the table.
     *
     * @param nPortletId The identifier of the portlet
     * @param nLinkId The identifier of the link
     * @param nOrder The order of the portlet to insert
     */
    void insertLink( int nPortletId, int nLinkId, int nOrder );

    /**
     * Remove a specified link from a specified portlet
     *
     * @param nPortletId The identifier of the portlet
     * @param nLinkId The identifier of the link
     */
    void deleteLink( int nPortletId, int nLinkId );

    /**
     * Deletes the portlet whose identifier is specified in parameter
     *
     * @param nPortletId The identifier of the portlet
     */
    void delete( int nPortletId );

    /**
     * Load the portlet whose identifier is specified in parameter
     *
     * @param nPortletId the identifier of the portlet
     * @return The portlet instance
     */
    Portlet load( int nPortletId );

    /**
     * Update the portlet
     *
     * @param portlet The portlet object
     */
    void store( Portlet portlet );

    /**
     * Returns a list of all the links
     *
     * @return A list of links in form of a ReferenceList object
     */
    ReferenceList selectLinksList(  );

    /**
     * Check if a specified links is not already registered in a specified portlet
     *
     * @param nPortletId The identifier of the portlet
     * @param nLinkId The identifier of the link
     * @return The result(boolean)
     */
    boolean testDuplicate( int nPortletId, int nLinkId );

    /**
     * Return a list of links wich belong to a specified portlet
     *
     * @param nPortletId The identifier of the portlet
     * @return A collection of links objects
     */
    Collection<Link> selectLinksInPortletList( int nPortletId );

    /**
     * Return the order of a specified link in a specified portlet
     *
     * @param nPortletId The identifier of the portlet
     * @param nLinkId The identifier of the link
     * @return The link's order
     */
    int selectLinkOrder( int nPortletId, int nLinkId );

    /**
     * Calculate a new primary key to add a new link
     *
     * @param nPortletId The identifier of the portlet
     * @return The new key.
     */
    int selectMaxOrder( int nPortletId );

    /**
     * Returns the maximum order of the portlets in the links page
     *
     * @return the max order
     */
    int selectPortletMaxOrder(  );

    /**
     * Update the order of a specified link in a specified portlet
     *
     * @param nPortletId The identifier of the portlet
     * @param nLinkId The identifier of the link
     * @param nOrder The new order
     */
    void storeLinkOrder( int nOrder, int nPortletId, int nLinkId );

    /**
     * Returns the id of an link wich has a specified order in a specified portlet
     *
     * @param nPortletId The identifier of the portlet
     * @param nOrder The link's order
     * @return The identifier of the link
     */
    int selectLinkIdByOrder( int nPortletId, int nOrder );

    /**
     * Finds the portlets which have not been selected in the links page
     *
     * @return the list of the unselected portlets
     */
    ReferenceList findUnselectedPortlets(  );

    /**
     * Selects the list of the portlets in the links page
     *
     * @return a collection of the unselected portlets
     */
    Collection<Portlet> selectPortletsInLinksPage(  );

    /**
     * Selects the order of a portlet in the links page
     *
     * @param nPortletId The identifier of the portlet
     * @return the order
     */
    int selectPortletOrder( int nPortletId );

    /**
     * Remove a portlet from the links page
     *
     * @param nPortletId The identifier of the portlet
     */
    void removePortlet( int nPortletId );

    /**
     * Removes a link from all the portlets
     *
     * @param nLinkId The identifier of the link
     */
    void removeLinkFromPortlets( int nLinkId );

    /**
     * Insert a new portlet in the links page
     *
     * @param nPortletId The identifier of the portlet
     * @param nOrder The order of the portlet
     */
    void insertPortlet( int nPortletId, int nOrder );

    /**
     * Selects a portlet Id from the links page by its order
     *
     * @param nOrder The order of the portlet
     * @return the portlet Id
     */
    int selectPortletIdByOrder( int nOrder );

    /**
     * Stores the order of a portlet in the links page
     *
     * @param nOrder The order of the portlet
     * @param nPortletId The identifier of the portlet
     */
    void storePortletOrder( int nOrder, int nPortletId );
}
