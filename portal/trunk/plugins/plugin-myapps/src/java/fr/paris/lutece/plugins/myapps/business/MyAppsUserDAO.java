/*
 * Copyright (c) 2002-2006, Mairie de Paris
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
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.crypto.CryptoUtil;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for MyAppsUser objects
 */
public final class MyAppsUserDAO implements IMyAppsUserDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_user ) FROM myapps_user";
    private static final String SQL_QUERY_SELECT = "SELECT id_user, name, id_application, stored_user_name, stored_user_password, stored_user_data FROM myapps_user WHERE id_user = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO myapps_user ( id_user, name, id_application, stored_user_name, stored_user_password, stored_user_data ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM myapps_user WHERE id_user = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE myapps_user SET id_user = ?, name = ?, id_application = ?, stored_user_name = ?, stored_user_password = ?, stored_user_data = ? WHERE id_user = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_user, name, id_application, stored_user_name, stored_user_password, stored_user_data FROM myapps_user";
    private static final String SQL_QUERY_BY_USER = "SELECT id_user, name, id_application, stored_user_name, stored_user_password, stored_user_data FROM myapps_user " +
        "WHERE name= ? AND id_application = ? ";

    //Encryption param
    private static final String PROPERTY_CRYPTO_KEY = "myapps.key";
    private static final String KEY = AppPropertiesService.getProperty( PROPERTY_CRYPTO_KEY );

    /**
     * Generates a new primary key
     *
     * @return Plugin plugin
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
     * @param myAppsUser instance of the MyAppsUser object to insert
     * @param Plugin plugin
     */
    public void insert( MyAppsUser myAppsUser, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        myAppsUser.setIdUser( newPrimaryKey( plugin ) );

        // Encrypt username and password
        String strUsername = CryptoUtil.encrypt( myAppsUser.getStoredUserName(  ), KEY );
        String strPassword = CryptoUtil.encrypt( myAppsUser.getStoredUserPassword(  ), KEY );
        daoUtil.setInt( 1, myAppsUser.getIdUser(  ) );
        daoUtil.setString( 2, myAppsUser.getName(  ) );
        daoUtil.setInt( 3, myAppsUser.getIdApplication(  ) );
        daoUtil.setString( 4, strUsername );
        daoUtil.setString( 5, strPassword );
        daoUtil.setString( 6, myAppsUser.getStoredUserData(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of the myAppsUser from the table
     *
     * @param nId The identifier of the myAppsUser
     * @param Plugin plugin
     * @return the instance of the MyAppsUser
     */
    public MyAppsUser load( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        MyAppsUser myAppsUser = null;

        if ( daoUtil.next(  ) )
        {
            myAppsUser = new MyAppsUser(  );

            myAppsUser.setIdUser( daoUtil.getInt( 1 ) );
            myAppsUser.setName( daoUtil.getString( 2 ) );
            myAppsUser.setIdApplication( daoUtil.getInt( 3 ) );
            myAppsUser.setStoredUserName( daoUtil.getString( 4 ) );
            myAppsUser.setStoredUserPassword( daoUtil.getString( 5 ) );
            myAppsUser.setStoredUserData( daoUtil.getString( 6 ) );
        }

        daoUtil.free(  );

        return myAppsUser;
    }

    /**
     * Delete a record from the table
     *
     * @param nMyAppsUserId The identifier of the myAppsUser
     * @param Plugin plugin
     */
    public void delete( int nMyAppsUserId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nMyAppsUserId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     *
     * @param myAppsUser The reference of the myAppsUser
     * @param Plugin plugin
     */
    public void store( MyAppsUser myAppsUser, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, myAppsUser.getIdUser(  ) );
        daoUtil.setString( 2, myAppsUser.getName(  ) );
        daoUtil.setInt( 3, myAppsUser.getIdApplication(  ) );

        String strUsername = CryptoUtil.encrypt( myAppsUser.getStoredUserName(  ), KEY );
        String strPassword = CryptoUtil.encrypt( myAppsUser.getStoredUserPassword(  ), KEY );
        daoUtil.setString( 4, strUsername );
        daoUtil.setString( 5, strPassword );
        daoUtil.setString( 6, myAppsUser.getStoredUserData(  ) );
        daoUtil.setInt( 7, myAppsUser.getIdUser(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of all the myAppsUsers and returns them in form of a collection
     *
     * @param Plugin plugin
     * @return The Collection which contains the data of all the myAppsUsers
     */
    public List<MyAppsUser> selectMyAppsUsersList( Plugin plugin )
    {
        List<MyAppsUser> myAppsUserList = new ArrayList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            MyAppsUser myAppsUser = new MyAppsUser(  );

            myAppsUser.setIdUser( daoUtil.getInt( 1 ) );
            myAppsUser.setName( daoUtil.getString( 2 ) );
            myAppsUser.setIdApplication( daoUtil.getInt( 3 ) );
            myAppsUser.setStoredUserName( daoUtil.getString( 4 ) );
            myAppsUser.setStoredUserPassword( daoUtil.getString( 5 ) );
            myAppsUser.setStoredUserData( daoUtil.getString( 6 ) );

            myAppsUserList.add( myAppsUser );
        }

        daoUtil.free(  );

        return myAppsUserList;
    }

    public MyAppsUser getCredentials( int nApplicationId, String strUserName, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_BY_USER, plugin );
        daoUtil.setString( 1, strUserName );
        daoUtil.setInt( 2, nApplicationId );

        daoUtil.executeQuery(  );

        MyAppsUser myAppsUser = null;

        if ( daoUtil.next(  ) )
        {
            myAppsUser = new MyAppsUser(  );

            myAppsUser.setIdUser( daoUtil.getInt( 1 ) );
            myAppsUser.setName( daoUtil.getString( 2 ) );
            myAppsUser.setIdApplication( daoUtil.getInt( 3 ) );

            // Decrypt username and password
            String strUsername = CryptoUtil.decrypt( daoUtil.getString( 4 ), KEY );
            String strPassword = CryptoUtil.decrypt( daoUtil.getString( 5 ), KEY );
            myAppsUser.setStoredUserName( strUsername );
            myAppsUser.setStoredUserPassword( strPassword );
            myAppsUser.setStoredUserData( daoUtil.getString( 6 ) );
        }

        daoUtil.free(  );

        return myAppsUser;
    }
}
