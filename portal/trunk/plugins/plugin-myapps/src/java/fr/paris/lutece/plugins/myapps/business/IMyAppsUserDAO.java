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
package fr.paris.lutece.plugins.myapps.business;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.Collection;


/**
* IMyAppsUserDAO Interface
*/
public interface IMyAppsUserDAO
{
    /**
     * Insert a new record in the table.
     * @param myAppsUser instance of the MyAppsUser object to inssert
     * @param plugin the Plugin
     */
    void insert( MyAppsUser myAppsUser, Plugin plugin );

    /**
    * Update the record in the table
    *
    * @param myAppsUser the reference of the MyAppsUser
    * @param plugin the Plugin
    */
    void store( MyAppsUser myAppsUser, Plugin plugin );

    /**
     * Delete a record from the table
     *
     * @param nIdMyAppsUser int identifier of the MyAppsUser to delete
     * @param plugin the Plugin
     */
    void delete( int nIdMyAppsUser, Plugin plugin );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * load the data of the right from the table
     *
     * @param strId The identifier of the myAppsUser
     * @param plugin the Plugin
     * @return The instance of the myAppsUser
     */
    MyAppsUser load( int nKey, Plugin plugin );

    /**
    * Loads the data of all the myAppsUsers and returns them in form of a collection
    *
    * @param plugin the Plugin
    * @return the collection which contains the data of all the myAppsUsers
    */
    Collection selectMyAppsUsersList( Plugin plugin );

    /**
    * Loads the a user credentials for a specific application
    *
    * @param plugin the Plugin
    * @return the myappuser for a specific application
    */
    MyAppsUser getCredentials( int nApplicationId, String strUserName, Plugin plugin );
}
