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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;


/**
*  IDbPageDatabaseSectionDAO interface
 */
public interface IDbPageDatabaseSectionDAO
{
    /**
     * Insert a new record in the table.
     * @param dbPageDatabaseSection The dbPageDatabaseSection object
     */
    public void insert( DbPageDatabaseSection dbPageDatabaseSection, Plugin plugin );

    /**
     * Calculates the number of sections in a page
     * @param nDbPageId The id of the page
     * @return The number of sections
     */
    int countSections( int nDbPageId, Plugin plugin );

    /**
     * Delete a record from the table
     * @param nDbPageDatabaseSectionId The DbPageDatabaseSection Id
     */
    void delete( int nDbPageDatabaseSectionId, Plugin plugin );

    /**
     * Modify the order of a section
     * @param nPageId The Page identifier
     * @param nNewOrder The order number
     * @param nIdSection The Section identifier
     */
    void getModifySectionOrder( int nPageId, int nNewOrder, int nIdSection, Plugin plugin );

    /**
     * Load the data of DbPageDatabaseSection from the table
     * @param nDbPageDatabaseSectionId The identifier of DbPageDatabaseSection
     * @return the instance of the DbPageDatabaseSection
     */
    DbPageDatabaseSection load( int nDbPageDatabaseSectionId, Plugin plugin );

    /**
     * Load the sections related to a page
     * @param nDbPageId The identifier of DbPageDatabase
     * @return the A List of the DbPageDatabaseSections
     */
    List<DbPageDatabaseSection> loadSectionsByPageId( int nDbPageId, Plugin plugin );

    /**
     * @param nIdSection1 the section order
     * @param nOrderSection1 the section order
     * @param nIdSection2 the section order
     * @param nOrderSection2 the section order
     */
    void reorderSections( int nIdSection1, int nOrderSection1, int nIdSection2, int nOrderSection2, Plugin plugin );

    /**
     * Load the list of dbPageDatabaseSections by page
     * @return The List of the DbPageDatabaseSections
     */
    List<DbPageDatabaseSection> selectDbPageDatabaseSectionList( Plugin plugin );

    /**
     * Returns the identifier of a section in a distinct order
     * @param nPageId The identifier of the page
     * @param nSectionOrder The order number
     * @return The order of the Section
     */
    int selectIdByOrder( int nPageId, int nSectionOrder, Plugin plugin );

    /**
     * Returns the highest order on a page
     * @param nPageId The identifier of the page
     * @return The order of the Section
     */
    int selectMaxIdOrder( int nPageId, Plugin plugin );

    /**
     * Load the list of orders
     * @param nPageId The identifier of the page
     * @return The ReferenceList of the Orders
     */
    ReferenceList selectOrderList( int nPageId, Plugin plugin );

    /**
     * Update the record in the table
     * @param dbPageDatabaseSection The reference of dbPageDatabaseSection
     */
    void store( DbPageDatabaseSection dbPageDatabaseSection, Plugin plugin );
}
