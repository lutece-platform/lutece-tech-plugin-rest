/*
 * IDocumentListPortletDAO.java
 *
 * Created on 10 octobre 2006, 15:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.document.business.portlet;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.portal.business.portlet.IPortletInterfaceDAO;
import fr.paris.lutece.portal.business.portlet.Portlet;

import java.util.Collection;


/**
 *
 */
public interface IDocumentListPortletDAO extends IPortletInterfaceDAO
{
    /**
     * Tests before insert it, if an article is already assigned in a portlet
     *
     *
     * @param nDocumentId The identifier of the document
     * @param nPortletId The identifier of the portlet
     * @return true if the article is already published, false otherwise
     */
    boolean checkAssignIntoPortlet( int nDocumentId, int nPortletId );

    /**
     * Tests before insert it, if an document is already published in a portlet
     *
     *
     * @param nDocumentId The identifier of the document
     * @param nPortletId The identifier of the portlet
     * @return true if the article is already published, false otherwise
     */
    boolean checkPublishingIntoPortlet( int nDocumentId, int nPortletId );

    /**
     * Deletes records for a portlet identifier in the tables portlet_articles_list, published_article_portlet,
     * auto_publishing
     *
     *
     * @param nPortletId the portlet identifier
     */
    void delete( int nPortletId );

    /**
     * Deletes an document assigned from a portlet
     *
     *
     * @param nDocumentId the identifier of the document to delete
     * @param nPortletId the identifier of the portlet
     */
    void deleteFromPortlet( int nDocumentId, int nPortletId );

    /**
     * Modify the order of a document
     *
     *
     * @param nNewOrder The order number
     * @param nDocumentId The document identifier
     * @param nPortletId the portlet identifier
     */
    void getModifyDocumentOrder( int nNewOrder, int nDocumentId, int nPortletId );

    /**
     * Insert a new record in the table portlet_articles_list
     *
     *
     * @param portlet the instance of the Portlet object to insert
     */
    void insert( Portlet portlet );

    /**
     * Insert the documents whose identifier is specified in parameter in a
     * portlet for assignes
     *
     *
     * @param nDocumentId the identifier of the document
     * @param nPortletId the identifier of the portlet
     */
    void insertIntoPortlet( int nDocumentId, int nPortletId, int nStatus );

    /**
     * Loads the data of Document List Portlet whose identifier is specified in parameter
     *
     *
     * @param nPortletId The Portlet identifier
     * @return theDocumentListPortlet object
     */
    Portlet load( int nPortletId );

    /**
     * Calculate the new max order
     *
     * @param nPortletId the portlet identifer
     * @return the max order of document
     */
    int maxOrderDocumentList( int nPortletId );

    /**
     * Published the documents whose identifier is specified in parameter in a
     * portlet for publishing : set status to 1
     *
     *
     * @param document the DocumentListPortlet object
     * @param nPortletId the identifier of the portlet
     * @param nStatus the publishing status : 1 for unpublished - 0 for published
     */
    void publishedIntoPortlet( int nDocumentId, int nPortletId, int nStatus );

    /**
     * Load the list of documentTypes
     *
     * @param nPortletId The portlet identifier
     * @return The Collection of the DocumentTypes
     */
    Collection selectAssignedDocumentsList( int nPortletId );

    /**
     * Returns all the documents of a portlet whose identifier is specified
     *
     *
     * @param nPortletId the identifier of the portlet
     * @return the document list in form of a Collection object
     */
    Collection<Document> selectByPortlet( int nPortletId );

    /**
      * Returns all the documents of a portlet whose identifier is specified
      *
      *
      * @param nPortletId the identifier of the portlet
      * @return the document list in form of a Collection object
      */
    Collection<Document> selectDocumentsByPortlet( int nPortletId );

    /**
     * Load the data of DocumentListPortlet from the table
     *
     *
     * @param nDocumentId The identifier of DocumentListPortlet
     * @param nPortletId the identifier of portlet
     * @return the instance of the DocumentListPortlet
     */
    DocumentListPortlet selectDocument( int nDocumentId, int nPortletId );

    /**
     * Load the list of documentTypes
     *
     * @param strCodeDocumentType The code
     * @return The Collection of the DocumentTypes
     */
    Collection selectDocumentTypeListByCode( int nDocumentId, String strCodeDocumentType );

    /**
     * Load the list of documentTypes
     *
     * @param strCodeDocumentType The code
     * @return The Collection of the DocumentTypes
     */
    Collection selectDocumentTypeListByCodeAndCategory( int nDocumentId, String strCodeDocumentType );

    /**
     * Returns a document identifier in a distinct order
     *
     *
     * @param nDocumentOrder The order number
     * @param nPortletId the portlet identifier
     * @return The order of the Document
     */
    int selectIdByOrder( int nDocumentOrder, int nPortletId );

    /**
     * Loads the list of the portlets whose type is the same as the one specified in parameter
     *
     *
     * @param strDocumentId the document identifier
     * @return the list of the portlets in form of a List
     */
    Collection selectPortletsListByDocumentId( String strDocumentId );

    /**
     * Loads the list of the documents whose type is the same as the one specified in parameter
     *
     *
     * @param nPortletId the portlet identifier
     * @return the list of the documents in form of a List
     */
    Collection<Document> selectPublishedDocumentsList( int nPortletId );

    /**
     * Update the record in the table
     *
     *
     * @param portlet A portlet
     */
    void store( Portlet portlet );

    /**
     * UnPublished the documents whose identifier is specified in parameter in a
     * portlet for publishing : set status to 1 and order to -1
     *
     *
     * @param document the DocumentListPortlet object
     * @param nPortletId the identifier of the portlet
     * @param nStatus the publishing status : 1 for unpublished - 0 for published
     */
    void unPublishedIntoPortlet( int nDocumentId, int nPortletId, int nStatus );

    /**
     * Tests if is a portlet is portlet type alias
     *
     * @param nPortletId The identifier of the document
     * @return true if the portlet is alias, false otherwise
     */
    boolean checkIsAliasPortlet( int nPortletId );
}
