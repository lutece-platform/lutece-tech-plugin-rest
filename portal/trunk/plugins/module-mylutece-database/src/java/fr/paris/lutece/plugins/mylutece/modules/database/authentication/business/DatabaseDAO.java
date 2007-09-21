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

import fr.paris.lutece.plugins.mylutece.modules.database.authentication.BaseUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.security.LuteceAuthentication;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;


/**
 * This class provides Data Access methods for authentication (role retrieval).
 *
 */
public class DatabaseDAO implements IDatabaseDAO
{
    private static final String SQL_QUERY_FIND_USER_BY_LOGIN = "SELECT mylutece_database_user_id, login, name_family, name_given, email" +
        " FROM mylutece_database_user WHERE login like ? ";
    private static final String SQL_QUERY_FIND_ROLES_FROM_LOGIN = "SELECT b.role_key FROM mylutece_database_user a, mylutece_database_user_role b" +
        " WHERE a.mylutece_database_user_id = b.mylutece_database_user_id AND a.login like ? ";
    private static final String SQL_QUERY_DELETE_ROLES_FOR_USER = "DELETE FROM mylutece_database_user_role WHERE mylutece_database_user_id = ?";
    private static final String SQL_QUERY_INSERT_ROLE_FOR_USER = "INSERT INTO mylutece_database_user_role ( mylutece_database_user_id, role_key ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_FIND_GROUPS_FROM_LOGIN = "SELECT b.group_key FROM mylutece_database_user a, mylutece_database_user_group b" +
        " WHERE a.mylutece_database_user_id = b.mylutece_database_user_id AND a.login like ? ";
    private static final String SQL_QUERY_DELETE_GROUPS_FOR_USER = "DELETE FROM mylutece_database_user_group WHERE mylutece_database_user_id = ?";
    private static final String SQL_QUERY_INSERT_GROUP_FOR_USER = "INSERT INTO mylutece_database_user_group ( mylutece_database_user_id, group_key ) VALUES ( ?, ? ) ";

    /** This class implements the Singleton design pattern. */
    private static DatabaseDAO _dao = new DatabaseDAO(  );

    /**
     * Returns the unique instance of the singleton.
     *
     * @return the instance
     */
    static DatabaseDAO getInstance(  )
    {
        return _dao;
    }

    /**
     * Find DatabaseUser by login
     *
     * @param strLogin the login
     * @param plugin The Plugin using this data access service
     * @param authenticationService the LuteceAuthentication object
     * @return DatabaseUser the user corresponding to the login
     */
    public BaseUser selectLuteceUserByLogin( String strLogin, Plugin plugin, LuteceAuthentication authenticationService )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_USER_BY_LOGIN, plugin );
        daoUtil.setString( 1, strLogin );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return null;
        }

        String strLastName = daoUtil.getString( 3 );
        String strFirstName = daoUtil.getString( 4 );
        String strEmail = daoUtil.getString( 5 );

        BaseUser user = new BaseUser( strLogin, authenticationService );
        user.setUserInfo( LuteceUser.NAME_FAMILY, strLastName );
        user.setUserInfo( LuteceUser.NAME_GIVEN, strFirstName );
        user.setUserInfo( LuteceUser.BUSINESS_INFO_ONLINE_EMAIL, strEmail );
        daoUtil.free(  );

        return user;
    }

    /**
     * Find user's roles by login
     *
     * @param strLogin the login
     * @param plugin The Plugin using this data access service
     * @return ArrayList the roles key list corresponding to the login
     */
    public ArrayList<String> selectUserRolesFromLogin( String strLogin, Plugin plugin )
    {
        ArrayList<String> arrayRoles = new ArrayList<String>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ROLES_FROM_LOGIN, plugin );
        daoUtil.setString( 1, strLogin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            arrayRoles.add( daoUtil.getString( 1 ) );
        }

        daoUtil.free(  );

        return arrayRoles;
    }

    /**
     * Delete roles for a user
     * @param nIdUser The id of the user
     * @param plugin The Plugin using this data access service
     */
    public void deleteRolesForUser( int nIdUser, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ROLES_FOR_USER, plugin );
        daoUtil.setInt( 1, nIdUser );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Assign a role to user
     * @param nIdUser The id of the user
     * @param strRoleKey The key of the role
     * @param plugin The Plugin using this data access service
     */
    public void createRoleForUser( int nIdUser, String strRoleKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_ROLE_FOR_USER, plugin );
        daoUtil.setInt( 1, nIdUser );
        daoUtil.setString( 2, strRoleKey );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Find user's groups by login
     *
     * @param strLogin The login
     * @param plugin The Plugin using this data access service
     * @return ArrayList the group key list corresponding to the login
     */
    public ArrayList<String> selectUserGroupsFromLogin( String strLogin, Plugin plugin )
    {
        ArrayList<String> arrayGroups = new ArrayList<String>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_GROUPS_FROM_LOGIN, plugin );
        daoUtil.setString( 1, strLogin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            arrayGroups.add( daoUtil.getString( 1 ) );
        }

        daoUtil.free(  );

        return arrayGroups;
    }

    /**
     * Delete groups for a user
     * @param nIdUser The id of the user
     * @param plugin The Plugin using this data access service
     */
    public void deleteGroupsForUser( int nIdUser, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_GROUPS_FOR_USER, plugin );
        daoUtil.setInt( 1, nIdUser );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Assign a group to user
     * @param nIdUser The id of the user
     * @param strGroupKey The key of the group
     * @param plugin The Plugin using this data access service
     */
    public void createGroupForUser( int nIdUser, String strGroupKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_GROUP_FOR_USER, plugin );
        daoUtil.setInt( 1, nIdUser );
        daoUtil.setString( 2, strGroupKey );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
