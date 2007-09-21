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
package fr.paris.lutece.plugins.mylutece.modules.ldapdatabase.authentication.business;

import fr.paris.lutece.plugins.mylutece.modules.ldapdatabase.authentication.LDAPDatabaseUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.security.LuteceAuthentication;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.ArrayList;


/**
 * This class provides instances management methods (create, find, ...) for ldapUser objects
 */
public final class LdapDatabaseHome
{
    // Static variable pointed at the DAO instance
    private static ILdapDatabaseDAO _dao = (LdapDatabaseDAO) SpringContextService.getPluginBean( "ldapdatabase",
            "LdapDatabaseDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private LdapDatabaseHome(  )
    {
    }

    /**
     * Find users by guid
     *
     * @param strLdapGuid the Ldap guid
     * @param plugin The Plugin using this data access service
     * @param authenticationService the LuteceAuthentication object
     * @return LdapDatabaseUser the user corresponding to the guid
     */
    public static LDAPDatabaseUser findLuteceUserByGuid( String strLdapGuid, Plugin plugin,
        LuteceAuthentication authenticationService )
    {
        return _dao.selectLuteceUserByGuid( strLdapGuid, plugin, authenticationService );
    }

    /**
     * Find user's roles by guid
     *
     * @param strGuid the Ldap guid
     * @param plugin The Plugin using this data access service
     * @return ArrayList the role key list corresponding to the guid
     */
    public static ArrayList<String> findUserRolesFromGuid( String strGuid, Plugin plugin )
    {
        return _dao.selectUserRolesFromGuid( strGuid, plugin );
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
}
