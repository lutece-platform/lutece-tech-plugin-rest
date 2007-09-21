package fr.paris.lutece.plugins.jcr.business;

import java.util.Collection;

/**
 * Specify action to perform when browsing a JCR
 *  
 * @param <T> the type of result elements
 * @param <L> the collection type
 */
public interface INodeAction<T, L extends Collection<T>>
{
    /**
     * Performs an action on file.
     * 
     * The result is an instance of T, or null to ignore the node
     * @param file
     * @return the transformed node
     */
    T doAction( IRepositoryFile file );

    /**
     * Creates the resulting collection
     * @return a collection
     */
    L createCollection( );
}
