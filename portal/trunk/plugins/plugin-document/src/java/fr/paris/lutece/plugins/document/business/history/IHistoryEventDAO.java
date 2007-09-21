/*
 * IHistoryEventDAO.java
 *
 * Created on 10 octobre 2006, 14:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.document.business.history;

import java.util.Collection;
import java.util.List;


/**
 *
 * @author LEVY
 */
public interface IHistoryEventDAO
{
    /**
     * Delete a record from the table
     *
     *
     * @param nDocumentId
     */
    void delete( int nDocumentId );

    /**
     * Insert a new record in the table.
     *
     *
     * @param historyEvent The historyEvent object
     */
    void insert( HistoryEvent historyEvent );

    /**
     * Load the list of historyEvents
     *
     *
     * @param nDocumentId The document Id
     * @return The Collection of the HistoryEvents
     */
    List<HistoryEvent> selectEventListByDocument( int nDocumentId );

    /**
     * Load the list of historyEvents
     *
     *
     * @param strUserId The UserId
     * @return The Collection of the HistoryEvents
     */
    Collection<HistoryEvent> selectEventListByUser( String strUserId );
}
