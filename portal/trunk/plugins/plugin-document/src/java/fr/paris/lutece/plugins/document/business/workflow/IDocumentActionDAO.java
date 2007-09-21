/**
 *
 */
package fr.paris.lutece.plugins.document.business.workflow;

import fr.paris.lutece.plugins.document.business.Document;

import java.util.List;


/**
 * This class porvides Data Access methods for DocumentActionDAO  interface
 */
public interface IDocumentActionDAO
{
    /**
     * Load the list of actions for a document
     *
     * @return The Collection of actions
     * @param document The document to add available actions
     */
    public abstract List<DocumentAction> selectActions( Document document );
}
