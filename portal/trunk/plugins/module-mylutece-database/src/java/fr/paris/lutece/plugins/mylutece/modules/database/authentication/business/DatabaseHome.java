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
package fr.paris.lutece.plugins.mylutece.modules.database.authentication.business;

import java.util.ArrayList;

import fr.paris.lutece.plugins.mylutece.modules.database.authentication.BaseUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.security.LuteceAuthentication;
import fr.paris.lutece.portal.service.spring.SpringContextService;


/**
 * This class provides instances management methods (create, find, ...) for databaseUser objects
 */
public final class DatabaseHome
{
    // Static variable pointed at the DAO instance
    private static IDatabaseDAO _dao = (IDatabaseDAO) SpringContextService.getPluginBean( "database", "DatabaseDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DatabaseHome(  )
    {
    }

    /**
     * Find users by login
     *
     * @param strLogin the login
     * @param plugin The Plugin using this data access service
     * @param authenticationService the LuteceAuthentication object
     * @return DatabaseUser the user corresponding to the login
     */
    public static BaseUser findLuteceUserByLogin( String strLogin, Plugin plugin,
        LuteceAuthentication authenticationService )
    {
        return _dao.selectLuteceUserByLogin( strLogin, plugin, authenticationService );
    }
    
    /**
     * Find user's roles by login
     *
     * @param strLogin the login
     * @param plugin The Plugin using this data access service
     * @return ArrayList the role key list corresponding to the login
     */
    public static ArrayList<String> findUserRolesFromLogin( String strLogin, Plugin plugin )
    {
        return _dao.selectUserRolesFromLogin( strLogin, plugin );
    }

    /**
     * Delete roles for a user
     * @param nIdUser The id of the user
     * @param plugin The Plugin using this data access service
     */
    public static void removeRolesForUser( int nIdUser, Plugin plugin )
    {
        _dao.deleteRolesForUser( nIdUser, plugin );
    }

    /**
     * Assign a role to user
     * @param nIdUser The id of the user
     * @param strRoleKey The key of the role
     * @param plugin The Plugin using this data access service
     */
    public static void addRoleForUser( int nIdUser, String strRoleKey, Plugin plugin )
    {
        _dao.createRoleForUser( nIdUser, strRoleKey, plugin );
    }

    /**
     * Find user's groups by login
     *
     * @param strLogin the login
     * @param plugin The Plugin using this data access service
     * @return ArrayList the group key list corresponding to the login
     */
    public static ArrayList<String> findUserGroupsFromLogin( String strLogin, Plugin plugin )
    {
        return _dao.selectUserGroupsFromLogin( strLogin, plugin );
    }

    /**
     * Delete groups for a user
     * @param nIdUser The id of the user
     * @param plugin The Plugin using this data access service
     */
    public static void removeGroupsForUser( int nIdUser, Plugin plugin )
    {
        _dao.deleteGroupsForUser( nIdUser, plugin );
    }

    /**
     * Assign a group to user
     * @param nIdUser The id of the user
     * @param strGroupKey The key of the group
     * @param plugin The Plugin using this data access service
     */
    public static void addGroupForUser( int nIdUser, String strGroupKey, Plugin plugin )
    {
        _dao.createGroupForUser( nIdUser, strGroupKey, plugin );
    }
}
