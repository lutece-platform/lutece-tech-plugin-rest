/*
 * IDocumentSpaceDAO.java
 *
 * Created on 29 septembre 2006, 14:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.document.business.spaces;

import fr.paris.lutece.util.ReferenceList;

import java.util.List;
import java.util.Locale;


/**
 *
 * @author LEVY
 */
public interface IDocumentSpaceDAO
{
    /**
     * Delete a record from the table
     *
     *
     * @param nSpaceId The Id to delete
     */
    void delete( int nSpaceId );

    /**
     * Returns all allowed document types for a given space
     *
     * @param nSpaceId The space Id
     * @return Allowed documents types as a ReferenceList
     */
    ReferenceList getAllowedDocumentTypes( int nSpaceId );

    /**
     * Load the list of documentSpaces
     *
     * @return The Collection of the DocumentSpaces
     */
    ReferenceList getDocumentSpaceList(  );

    /**
     * Gets a list of icons available or space customization
     *
     * @return A list of icons
     */
    ReferenceList getIconsList(  );

    /**
     * Load the list of documentSpaces
     *
     * @return The Collection of the DocumentSpaces
     */
    ReferenceList getViewTypeList( Locale locale );

    /**
     * Insert a new record in the table.
     *
     *
     * @param space The space object
     */
    void insert( DocumentSpace space );

    /**
     * Load the data of DocumentSpace from the table
     *
     *
     * @param nDocumentSpaceId The identifier of DocumentSpace
     * @return the instance of the DocumentSpace
     */
    DocumentSpace load( int nDocumentSpaceId );

    /**
     * Select all spaces
     *
     * @return A collection of all spaces.
     */
    List<DocumentSpace> selectAll(  );

    /**
     * Load the list of documentSpaces
     *
     *
     * @param nSpaceId
     * @return The Collection of the DocumentSpaces
     */
    List<DocumentSpace> selectChilds( int nSpaceId );

    /**
     * Update the record in the table
     *
     * @param space The reference of space
     */
    void store( DocumentSpace space );
}
