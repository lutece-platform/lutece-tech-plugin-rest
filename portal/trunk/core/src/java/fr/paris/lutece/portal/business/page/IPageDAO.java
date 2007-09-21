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
package fr.paris.lutece.portal.business.page;

import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;
import java.util.List;


/**
 * IPageDAO Interface
 */
public interface IPageDAO
{
    /**
     * Insert a new record in the table.
     * @param page The Page object
     */
    public abstract void insert( Page page );

    /**
     * load the data of Level from the table
     * @param nPageId The indentifier of the object Page
     * @param bPortlets The boolean
     * @return The Instance of the object Page
     */
    public abstract Page load( int nPageId, boolean bPortlets );

    /**
     * Delete a record from the table
     * @param nPageId The indentifier of the object nPageId
     */
    public abstract void delete( int nPageId );

    /**
     * Update the record in the table
     * @param page The instance of the page to update
     */
    public abstract void store( Page page );

    /**
     * Select all the child pages for a page which is specified in parameter
     *
     * @param nParentPageId The parent page identifier
     * @return The list of objects Page (without portlets list)
     */
    public abstract Collection<Page> selectChildPages( int nParentPageId );

    /**
     * Loads all the pages for a portal
     * @return The pages of the current portal (without portlets list, of course !)
     */
    public abstract List<Page> selectAllPages(  );

    /**
     * Invalidate the page after a modification
     *
     * @param nPageId the page identifier
     */
    public abstract void invalidatePage( int nPageId );

    /**
     * Load the Referencelist of documentTypes
     * @return listDocumentTypes
     */
    public abstract ReferenceList getPagesList(  );

    /**
     * Serach the last order of child page list
     * @return The new page order
     */
    public abstract int selectNewChildPageOrder( int nParentPageId );

    /**
     * Load a image corresponding to an Page
     *
     * @param nIdPage The Page Id
     * @return the instance of the ImageContent
     */
    public abstract ImageResource loadImageResource( int nIdPage );

    /**
     * Tests if page exist
     *
     * @param nPageId The identifier of the document
     * @return true if the page existed, false otherwise
     */
    boolean checkPageExist( int nPageId );
}
