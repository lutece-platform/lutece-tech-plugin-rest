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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;


/**
 * This class provides instances management methods (create, find, ...) for LdapUser objects
 */
public final class LdapUserHome
{
    // Static variable pointed at the DAO instance
    private static ILdapUserDAO _dao = (ILdapUserDAO) SpringContextService.getPluginBean( "ldapdatabase", "LdapUserDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private LdapUserHome(  )
    {
    }

    /**
     * Creation of an instance of ldapUser
     *
     * @param ldapUser The instance of the LdapUser which contains the informations to store
     * @param plugin The current plugin using this method
     * @return The  instance of LdapUser which has been created with its primary key.
     */
    public static LdapUser create( LdapUser ldapUser, Plugin plugin )
    {
        _dao.insert( ldapUser, plugin );

        return ldapUser;
    }

    /**
     * Update of the ldapUser which is specified in parameter
     *
     * @param ldapUser The instance of the LdapUser which contains the data to store
     * @param plugin The current plugin using this method
     * @return The instance of the  LdapUser which has been updated
     */
    public static LdapUser update( LdapUser ldapUser, Plugin plugin )
    {
        _dao.store( ldapUser, plugin );

        return ldapUser;
    }

    /**
     * Remove the ldapUser whose identifier is specified in parameter
     *
     * @param ldapUser The LdapUser object to remove
     * @param plugin The current plugin using this method
     */
    public static void remove( LdapUser ldapUser, Plugin plugin )
    {
        _dao.delete( ldapUser, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a LdapUser whose identifier is specified in parameter
     *
     * @param nKey The Primary key of the ldapUser
     * @param plugin The current plugin using this method
     * @return An instance of LdapUser
     */
    public static LdapUser findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Returns a collection of LdapUser objects
     * @param plugin The current plugin using this method
     * @return A collection of LdapUser
     */
    public static Collection findLdapUsersList( Plugin plugin )
    {
        return _dao.selectLdapUserList( plugin );
    }

    /**
     * Returns a collection of LdapUser objects for a guid
     * @param strLdapGuid The guid of the ldapUser
     * @param plugin The current plugin using this method
     * @return A collection of LdapUser
     */
    public static Collection findLdapUsersListForGuid( String strLdapGuid, Plugin plugin )
    {
        return _dao.selectLdapUserListForGuid( strLdapGuid, plugin );
    }
}
