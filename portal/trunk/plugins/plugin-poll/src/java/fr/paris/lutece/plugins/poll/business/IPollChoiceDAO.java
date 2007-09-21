/*
 * IPollChoiceDAO.java
 *
 * Created on 13 septembre 2006, 17:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.poll.business;

import fr.paris.lutece.portal.service.plugin.Plugin;


/**
 *
 * @author TaupenaS
 */
public interface IPollChoiceDAO
{
    /**
     * Delete a record from the table
     *
     * @param nPollChoiceId The PollChoice Id
     * @param plugin The Plugin using this data access service
     */
    void delete( int nPollChoiceId, Plugin plugin );

    /**
     * Insert a new record in the table.
     *
     *
     * @param pollChoice The pollChoice object
     * @param plugin The Plugin using this data access service
     */
    void insert( PollChoice pollChoice, Plugin plugin );

    /**
     * Load the data of PollChoice from the table
     *
     *
     * @param nPollChoiceId The identifier of PollChoice
     * @param plugin The Plugin using this data access service
     * @return the instance of the PollChoice
     */
    PollChoice load( int nPollChoiceId, Plugin plugin );

    /**
     * Update the record in the table
     *
     * @param pollChoice The reference of pollChoice
     * @param plugin The Plugin using this data access service
     */
    void store( PollChoice pollChoice, Plugin plugin );
}
