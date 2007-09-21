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
package fr.paris.lutece.plugins.linkpages.business.portlet;

import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.portlet.IPortletInterfaceDAO;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;


/**
 *
 * @author lenaini
 */
public interface ILinkPagesPortletDAO extends IPortletInterfaceDAO
{
    /**
     * Deletes a record from the table
     * @param nPortletId Identifier portlet
     */
    void delete( int nPortletId );

    /**
     * Remove all link pages from a specified portlet
     * @param nPortletId The identifier of the portlet
     */
    void deleteAllLinkPages( int nPortletId );

    /**
     * Remove a specified link page from a specified portlet
     * @param nPortletId The identifier of the portlet
     * @param nLinkPageId The identifier of the link page
     */
    void deleteLinkPage( int nPortletId, int nLinkPageId );

    void insert( Portlet portlet ) throws AppException;

    /**
     * Insert a new record in the table.
     * @param nPortletId The identifier of the portlet
     * @param nLinkPageId The identifier of the link page
     * @param nOrder The order of the portlet to insert
     */
    void insertLinkPage( int nPortletId, int nLinkPageId, int nOrder );

    /**
     * Loads the data of a LinkdPagesPortlet whose identifier is specified in parameter from the table
     * @param nPortletId The LinkdPagesPortlet identifier
     * @return the LinkdPagesPortlet object
     */
    Portlet load( int nPortletId );

    /**
     * Returns the id of a link page wich has a specified order in a specified portlet
     * @param nPortletId The identifier of the portlet
     * @param nOrder The link page's order
     * @return The identifier of the link page
     */
    int selectLinkPageIdByOrder( int nPortletId, int nOrder );

    /**
     * Return the order of a link page in a specified portlet
     * @param nPortletId The identifier of the portlet to check
     * @param nPageId The identifier of the page
     * @return The order of the page in the portlet
     */
    int selectLinkPageOrder( int nPortletId, int nPageId );

    /**
     * Return a list of linkpages which belong to a specified portlet
     * @param nPortletId The identifier of the portlet
     * @return A list of linkpages objects
     */
    List<Page> selectLinkPagesInPortletList( int nPortletId );

    /**
     * Load the list of all the pages of the website
     * @return the list in form of a ReferenceList object
     */
    ReferenceList selectLinkPagesList(  );

    /**
     * Calculate a new primary key to add a new linkPage
     * @param nPortletId The identifier of the portlet
     * @return The new key.
     */
    int selectMaxOrder( int nPortletId );

    void store( Portlet portlet ) throws AppException;

    /**
     * Update the order of a specified link page in a specified portlet
     * @param nPortletId The identifier of the portlet
     * @param nLinkPageId The identifier of the link page
     * @param nOrder The new order
     */
    void storeLinkPageOrder( int nOrder, int nPortletId, int nLinkPageId );

    /**
     * Check if a specified link page is not already registered in a specified portlet
     * @param nPortletId The identifier of the portlet
     * @param nLinkPageId The identifier of the link page
     * @return The result(boolean)
     */
    boolean testDuplicate( int nPortletId, int nLinkPageId );
}
