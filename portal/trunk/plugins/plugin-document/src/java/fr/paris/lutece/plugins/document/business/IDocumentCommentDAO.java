/*
 * IDocumentCommentDAO.java
 *
 * Created on 28 février 2007, 18:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.document.business;

import java.util.List;


/**
 *
 * @author lenaini
 */
public interface IDocumentCommentDAO
{
    /**
     * Delete a record from the table
     * @param nCommentId the documentComment identifier
     */
    void delete( int nCommentId );

    /**
     * Insert a new record in the table.
     * @param documentComment The DocumentComment object
     */
    void insert( DocumentComment documentComment );

    /**
     * Load the data of DocumentComment from the table
     * @param nCommentId The identifier of DocumentComment
     * @return the instance of the DocumentComment
     */
    DocumentComment load( int nCommentId );

    /**
     * Gets all the comments for an document
     * @param nDocumentId the identifier of the Document
     * @param bPublishedOnly set to true to return published comments only
     * @return the list of comments, in chronological order for published
     * comments, in reverse chronological order otherwise
     */
    List<DocumentComment> selectByDocument( int nDocumentId, boolean bPublishedOnly );

    /**
     * Update the record in the table
     * @param documentComment The reference of DdocumentComment
     */
    void store( DocumentComment documentComment );

    /**
     * Update status from the comment
     * @param nComment The Comment identifier
     * @param nStatus The published Status
     */
    void updateCommentStatus( int nCommentId, int nStatus );
}
