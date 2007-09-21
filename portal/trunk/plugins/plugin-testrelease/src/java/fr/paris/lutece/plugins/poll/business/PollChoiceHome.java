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
package fr.paris.lutece.plugins.poll.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * This class provides instances management methods (create, find, ...) for PollChoice objects
 */
public final class PollChoiceHome
{
    // Static variable pointed at the DAO instance
    private static IPollChoiceDAO _dao = (IPollChoiceDAO) SpringContextService.getPluginBean( "poll" , "pollChoiceDAO");
    
    
    /**
     * Private constructor - this class need not be instantiated
     */
    private PollChoiceHome()
    {
    }
    
    /**
     * Creation of an instance of an article PollChoice
     *
     * @param choice An instance of the PollChoice which contains the informations to store
     * @param plugin The Plugin object
     * @return The instance of the PollChoice which has been created
     */
    public static PollChoice create( PollChoice choice, Plugin plugin)
    {
        _dao.insert( choice, plugin );
        
        return choice;
    }
    
    /**
     * Updates of the PollChoice instance specified in parameter
     *
     * @param choice An instance of the PollChoice which contains the informations to store
     * @param plugin The Plugin object
     * @return choice The instance of the PollChoice which has been updated.
     */
    public static PollChoice update( PollChoice choice, Plugin plugin)
    {
        _dao.store( choice, plugin );
        
        return choice;
    }
    
    /**
     * Deletes the PollChoice instance whose identifier is specified in parameter
     *
     * @param nIdChoice The identifier of the article PollChoice to delete in the database
     * @param plugin The Plugin object
     */
    public static void remove( int nIdChoice, Plugin plugin)
    {
        _dao.delete( nIdChoice, plugin );
    }
    
    
    /**
     * Returns an instance of the article PollChoice whose identifier is
     * specified in parameter
     *
     * @param nKey The primary key of the article to find in the database
     * @param plugin The Plugin object
     * @return An instance of the PollChoice which corresponds to the key
     */
    public static PollChoice findByPrimaryKey( int nKey, Plugin plugin)
    {
        return _dao.load( nKey, plugin );
    }
    
    
}
