/*
 * IPollQuestionDAO.java
 *
 * Created on 13 septembre 2006, 17:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fr.paris.lutece.plugins.poll.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import java.util.List;

/**
 *
 * @author TaupenaS
 */
public interface IPollQuestionDAO
{
    /**
     * Delete a record from the table
     * 
     * @param nPollQuestionId The PollQuestion Id
     * @param plugin The Plugin using this data access service
     */
    void delete(int nPollQuestionId, Plugin plugin);

    /**
     * Insert a new record in the table.
     * 
     * 
     * @param pollQuestion The pollQuestion object
     * @param plugin The Plugin using this data access service
     */
    void insert(PollQuestion pollQuestion, Plugin plugin);

    /**
     * Load the data of PollQuestion from the table
     * 
     * 
     * @param nPollQuestionId The identifier of PollQuestion
     * @param plugin The Plugin using this data access service
     * @return the instance of the PollQuestion
     */
    PollQuestion load(int nPollQuestionId, Plugin plugin);

    /**
     * Load the list of pollQuestions by poll id
     * 
     * @param nIdPoll The poll identifier
     * @param plugin The Plugin using this data access service
     * @return The List of the PollQuestions
     */
    List<PollQuestion> selectPollQuestionListByPoll(int nIdPoll, Plugin plugin);

    /**
     * Update the record in the table
     * 
     * @param pollQuestion The reference of pollQuestion
     * @param plugin The Plugin using this data access service
     */
    void store(PollQuestion pollQuestion, Plugin plugin);
    
}
