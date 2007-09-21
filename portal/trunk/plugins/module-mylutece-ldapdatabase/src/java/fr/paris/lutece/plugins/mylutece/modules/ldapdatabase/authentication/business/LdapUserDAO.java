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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for ldapUser objects
 */
public final class LdapUserDAO implements ILdapUserDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = " SELECT max( mylutece_ldapdatabase_user_id ) FROM mylutece_ldapdatabase_user ";
    private static final String SQL_QUERY_SELECT = " SELECT mylutece_ldapdatabase_user_id, ldap_guid, name_family, name_given, email FROM mylutece_ldapdatabase_user WHERE mylutece_ldapdatabase_user_id = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO mylutece_ldapdatabase_user ( mylutece_ldapdatabase_user_id, ldap_guid, name_family, name_given, email ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM mylutece_ldapdatabase_user WHERE mylutece_ldapdatabase_user_id = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE mylutece_ldapdatabase_user SET mylutece_ldapdatabase_user_id = ?, ldap_guid = ? name_family = ?, name_given = ?, email = ? WHERE mylutece_ldapdatabase_user_id = ?  ";
    private static final String SQL_QUERY_SELECTALL = " SELECT mylutece_ldapdatabase_user_id, ldap_guid, name_family, name_given, email FROM mylutece_ldapdatabase_user ";
    private static final String SQL_QUERY_SELECTALL_FOR_LDAP_GUID = " SELECT mylutece_ldapdatabase_user_id, ldap_guid, name_family, name_given, email FROM mylutece_ldapdatabase_user WHERE ldap_guid = ? ";

    /** This class implements the Singleton design pattern. */
    private static LdapUserDAO _dao = new LdapUserDAO(  );

    /**
     * Creates a new ldapUserDAO object.
     */
    private LdapUserDAO(  )
    {
    }

    /**
     * Returns the unique instance of the singleton.
     *
     * @return the instance
     */
    static LdapUserDAO getInstance(  )
    {
        return _dao;
    }

    /**
     * Generates a new primary key
     * @param plugin The Plugin using this data access service
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nKey;
    }

    /**
     * Insert a new record in the table.
     *
     * @param ldapUser The ldapUser object
     * @param plugin The Plugin using this data access service
     */
    public void insert( LdapUser ldapUser, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( 1, ldapUser.getUserId(  ) );
        daoUtil.setString( 2, ldapUser.getLdapGuid(  ) );
        daoUtil.setString( 3, ldapUser.getLastName(  ) );
        daoUtil.setString( 4, ldapUser.getFirstName(  ) );
        daoUtil.setString( 5, ldapUser.getEmail(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of LdapUser from the table
     *
     * @param nUserId The identifier of User
     * @param plugin The Plugin using this data access service
     * @return the instance of the LdapUser
     */
    public LdapUser load( int nUserId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nUserId );
        daoUtil.executeQuery(  );

        LdapUser ldapUser = null;

        if ( daoUtil.next(  ) )
        {
            ldapUser = new LdapUser(  );
            ldapUser.setUserId( daoUtil.getInt( 1 ) );
            ldapUser.setLdapGuid( daoUtil.getString( 2 ) );
            ldapUser.setLastName( daoUtil.getString( 3 ) );
            ldapUser.setFirstName( daoUtil.getString( 4 ) );
            ldapUser.setEmail( daoUtil.getString( 5 ) );
        }

        daoUtil.free(  );

        return ldapUser;
    }

    /**
     * Delete a record from the table
     * @param ldapUser The LdapUser object
     * @param plugin The Plugin using this data access service
     */
    public void delete( LdapUser ldapUser, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, ldapUser.getUserId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param ldapUser The reference of ldapUser
     * @param plugin The Plugin using this data access service
     */
    public void store( LdapUser ldapUser, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, ldapUser.getUserId(  ) );
        daoUtil.setString( 2, ldapUser.getLdapGuid(  ) );
        daoUtil.setString( 3, ldapUser.getLastName(  ) );
        daoUtil.setString( 4, ldapUser.getFirstName(  ) );
        daoUtil.setString( 5, ldapUser.getEmail(  ) );
        daoUtil.setInt( 6, ldapUser.getUserId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the list of ldapUsers
     * @param plugin The Plugin using this data access service
     * @return The Collection of the ldapUsers
     */
    public Collection<LdapUser> selectLdapUserList( Plugin plugin )
    {
        Collection<LdapUser> listLdapUsers = new ArrayList<LdapUser>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            LdapUser ldapUser = new LdapUser(  );
            ldapUser.setUserId( daoUtil.getInt( 1 ) );
            ldapUser.setLdapGuid( daoUtil.getString( 2 ) );
            ldapUser.setLastName( daoUtil.getString( 3 ) );
            ldapUser.setFirstName( daoUtil.getString( 4 ) );
            ldapUser.setEmail( daoUtil.getString( 5 ) );

            listLdapUsers.add( ldapUser );
        }

        daoUtil.free(  );

        return listLdapUsers;
    }

    /**
     * Load the list of ldapUsers for a ldap guid
     * @param strGuid The guid of ldapUser
     * @param plugin The Plugin using this data access service
     * @return The Collection of the ldapUsers
     */
    public Collection<LdapUser> selectLdapUserListForGuid( String strGuid, Plugin plugin )
    {
        Collection<LdapUser> listLdapUsers = new ArrayList<LdapUser>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_FOR_LDAP_GUID, plugin );
        daoUtil.setString( 1, strGuid );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            LdapUser ldapUser = new LdapUser(  );
            ldapUser.setUserId( daoUtil.getInt( 1 ) );
            ldapUser.setLdapGuid( daoUtil.getString( 2 ) );
            ldapUser.setLastName( daoUtil.getString( 3 ) );
            ldapUser.setFirstName( daoUtil.getString( 4 ) );
            ldapUser.setEmail( daoUtil.getString( 5 ) );

            listLdapUsers.add( ldapUser );
        }

        daoUtil.free(  );

        return listLdapUsers;
    }
}
