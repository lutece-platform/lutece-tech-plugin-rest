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
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for DbPageDatabaseSection objects
 */
public final class DbPageDatabaseSectionHome
{
    // Static variable pointed at the DAO instance
    private static IDbPageDatabaseSectionDAO _dao = (IDbPageDatabaseSectionDAO) SpringContextService.getPluginBean( "dbpage",
            "dbPageDatabaseSectionDAO" );

    /**
    * Private constructor - this class need not be instantiated
    */
    private DbPageDatabaseSectionHome(  )
    {
    }

    /**
    * Creation of an instance of dbPageDatabaseSection
    * @param plugin The plugin object
    * @param dbPageDatabaseSection The instance of the dbPageDatabaseSection which contains the informations to store
    * @return The  instance of dbPageDatabaseSection which has been created with its primary key.
    */
    public static DbPageDatabaseSection create( DbPageDatabaseSection dbPageDatabaseSection, Plugin plugin )
    {
        _dao.insert( dbPageDatabaseSection, plugin );

        return dbPageDatabaseSection;
    }

    /**
    * Update of the dbPageDatabaseSection which is specified in parameter
    * @param plugin The plugin object
    * @param dbPageDatabaseSection The instance of the dbPageDatabaseSection which contains the data to store
    * @return The instance of the  dbPageDatabaseSection which has been updated
    */
    public static DbPageDatabaseSection update( DbPageDatabaseSection dbPageDatabaseSection, Plugin plugin )
    {
        _dao.store( dbPageDatabaseSection, plugin );

        return dbPageDatabaseSection;
    }

    /**
    * Remove the DbPageDatabaseSection whose identifier is specified in parameter
    * @param plugin The plugin object
    * @param nDbPageDatabaseSectionId The DbPageDatabaseSection Id
    */
    public static void remove( int nDbPageDatabaseSectionId, Plugin plugin )
    {
        _dao.delete( nDbPageDatabaseSectionId, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
    * Returns an instance of a dbPageDatabaseSection whose identifier is specified in parameter
    * @param plugin The plugin object
    * @param nKey The Primary key of the dbPageDatabaseSection
    * @return An instance of dbPageDatabaseSection
    */
    public static DbPageDatabaseSection findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
    * Returns a list of dbPageDatabaseSections objects
    * @return A list of dbPageDatabaseSections
    * @param plugin The plugin object
    */
    public static List<DbPageDatabaseSection> findDbPageDatabaseSectionsList( Plugin plugin )
    {
        return _dao.selectDbPageDatabaseSectionList( plugin );
    }

    /**
     * This method is used to find the sections of a dbpage
     * @param nPageId The id of the dbpage
     * @param plugin The plugin object
     * @return List of DbPageDatabaseSection
     */
    public static List<DbPageDatabaseSection> findSectionsByPage( int nPageId, Plugin plugin )
    {
        return _dao.loadSectionsByPageId( nPageId, plugin );
    }

    /**
    * Returns the number of Sections in a DbPage
    * @param plugin The plugin
    * @param nDbPageId The identifier of the DbPage
    * @return The number of sections in the DbPage
    */
    public static int countNumberSections( int nDbPageId, Plugin plugin )
    {
        return _dao.countSections( nDbPageId, plugin );
    }

    /**
     * This method allows to fetch a representation of the orders of a section in a dbpage
     * @param nDbPageId The dbPage identifier
     * @param plugin The plugin object
     * @return A list of sections representing the order
     */
    public static ReferenceList findOrderComboList( int nDbPageId, Plugin plugin )
    {
        return _dao.selectOrderList( nDbPageId, plugin );
    }

    /**
    * Update the order of a section
    * @param plugin The plugin object
    * @param nPageId the identifier of the page
    * @param nNewOrder the new number of order
    * @param nIdSection the Identifier of a scetion
    */
    public static void getModifySectionOrder( int nPageId, int nNewOrder, int nIdSection, Plugin plugin )
    {
        _dao.getModifySectionOrder( nPageId, nNewOrder, nIdSection, plugin );
    }

    /**
    * Search the order of a section
    * @return int  the id by a given order
    * @param plugin The plugin object
    * @param nItemOrder the number of orders of sections in the page
    * @param nPageId the identifier of the page
    */
    public static int getIdByOrder( int nPageId, int nItemOrder, Plugin plugin )
    {
        return _dao.selectIdByOrder( nPageId, nItemOrder, plugin );
    }

    /**
    * Get the maximum order on a page
    * @return int  the id by a given order
    * @param plugin The plugin object
    * @param nPageId the identifier of the page
    */
    public static int getMaxIdByOrder( int nPageId, Plugin plugin )
    {
        return _dao.selectMaxIdOrder( nPageId, plugin );
    }

    public static void reorderSections( int nIdSection1, int nOrderSection1, int nIdSection2, int nOrderSection2,
        Plugin plugin )
    {
        _dao.reorderSections( nIdSection1, nOrderSection1, nIdSection2, nOrderSection2, plugin );
    }
}
