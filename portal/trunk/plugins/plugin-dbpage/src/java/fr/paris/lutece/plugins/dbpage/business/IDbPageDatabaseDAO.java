/*
 * IDbPageDatabaseDAO.java
 *
 * Created on 10 mai 2007, 14:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.dbpage.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;


/**
 * IDbPageDatabaseDAO
 */
public interface IDbPageDatabaseDAO
{
    /**
     * Delete a record from the table
     * @param nDbPageDatabaseId The DbPageDatabase Id
     */
    void delete( int nDbPageDatabaseId, Plugin plugin );

    /**
     * Insert a new record in the table.
     * @param dbPageDatabase The dbPageDatabase object
     */
    void insert( DbPageDatabase dbPageDatabase, Plugin plugin );

    /**
     * Load the data of DbPageDatabase from the table
     * @param nDbPageDatabaseId The identifier of DbPageDatabase
     * @return the instance of the DbPageDatabase
     */
    DbPageDatabase load( int nDbPageDatabaseId, Plugin plugin );

    /**
     * Load the data of DbPageDatabase from the table
     * @param strDbPageName The identifier of DbPageDatabase
     * @return the instance of the DbPageDatabase
     */
    DbPageDatabase loadByName( String strDbPageName, Plugin plugin );

    /**
     * Load the list of dbPageDatabases
     * @return The List of the DbPages
     */
    List<DbPageDatabase> selectDbPageDatabaseList( Plugin plugin );

    /**
     * Load the list of dbpages
     * @return The ReferenceList of the DbPages
     */
    ReferenceList selectDbPagesList( Plugin plugin );

    /**
     * Update the record in the table
     * @param dbPageDatabase The reference of dbPageDatabase
     */
    void store( DbPageDatabase dbPageDatabase, Plugin plugin );
}
