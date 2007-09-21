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

import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for MyApps objects
 */
public final class MyAppsDAO implements IMyAppsDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_application ) FROM myapps_application";
    private static final String SQL_QUERY_SELECT = "SELECT id_application, name, description, url, code, password, data, code_heading, data_heading,icon_content, icon_mime_type FROM myapps_application WHERE id_application = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO myapps_application ( id_application, name, description, url, code, password, data, code_heading, data_heading ,icon_content, icon_mime_type) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM myapps_application WHERE id_application = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE myapps_application SET id_application = ?, name = ?, description = ?, url = ?, code = ?, password = ?, data = ?, code_heading = ?, data_heading = ? ,icon_content = ?, icon_mime_type = ? WHERE id_application = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_application, name, description, url, code, password, data, code_heading, data_heading, icon_content, icon_mime_type FROM myapps_application";
    private static final String SQL_QUERY_SELECT_BY_USER = "SELECT a.id_application, a.name, a.description, a.url, a.code, password, data, code_heading, data_heading , icon_content, icon_mime_type FROM myapps_application as a, " +
        "myapps_user as b WHERE a.id_application = b.id_application AND b.name= ? ";

    //Image resource fetching
    private static final String SQL_QUERY_SELECT_RESOURCE_IMAGE = "SELECT icon_content , icon_mime_type FROM myapps_application  WHERE id_application= ? ";

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
     * @param myApps instance of the MyApps object to insert
     * @param Plugin plugin
     */
    public void insert( MyApps myApps, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        myApps.setIdApplication( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, myApps.getIdApplication(  ) );
        daoUtil.setString( 2, myApps.getName(  ) );
        daoUtil.setString( 3, myApps.getDescription(  ) );
        daoUtil.setString( 4, myApps.getUrl(  ) );
        daoUtil.setString( 5, myApps.getCode(  ) );
        daoUtil.setString( 6, myApps.getPassword(  ) );
        daoUtil.setString( 7, myApps.getData(  ) );
        daoUtil.setString( 8, myApps.getCodeHeading(  ) );
        daoUtil.setString( 9, myApps.getDataHeading(  ) );

        if ( ( myApps.getIconContent(  ) == null ) )
        {
            daoUtil.setBytes( 10, null );
            daoUtil.setString( 11, "" );
        }
        else
        {
            daoUtil.setBytes( 10, myApps.getIconContent(  ) );
            daoUtil.setString( 11, myApps.getIconMimeType(  ) );
        }

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of the myApps from the table
     *
     * @param nId The identifier of the myApps
     * @param Plugin plugin
     * @return the instance of the MyApps
     */
    public MyApps load( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery(  );

        MyApps myApps = null;

        if ( daoUtil.next(  ) )
        {
            myApps = new MyApps(  );

            myApps.setIdApplication( daoUtil.getInt( 1 ) );
            myApps.setName( daoUtil.getString( 2 ) );
            myApps.setDescription( daoUtil.getString( 3 ) );
            myApps.setUrl( daoUtil.getString( 4 ) );
            myApps.setCode( daoUtil.getString( 5 ) );
            myApps.setPassword( daoUtil.getString( 6 ) );
            myApps.setData( daoUtil.getString( 7 ) );
            myApps.setCodeHeading( daoUtil.getString( 8 ) );
            myApps.setDataHeading( daoUtil.getString( 9 ) );
            myApps.setIconContent( daoUtil.getBytes( 10 ) );
            myApps.setIconMimeType( daoUtil.getString( 11 ) );
        }

        daoUtil.free(  );

        return myApps;
    }

    /**
     * Delete a record from the table
     *
     * @param nMyAppsId The identifier of the myApps
     * @param Plugin plugin
     */
    public void delete( int nMyAppsId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nMyAppsId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     *
     * @param myApps The reference of the myApps
     * @param Plugin plugin
     */
    public void store( MyApps myApps, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, myApps.getIdApplication(  ) );
        daoUtil.setString( 2, myApps.getName(  ) );
        daoUtil.setString( 3, myApps.getDescription(  ) );
        daoUtil.setString( 4, myApps.getUrl(  ) );
        daoUtil.setString( 5, myApps.getCode(  ) );
        daoUtil.setString( 6, myApps.getPassword(  ) );
        daoUtil.setString( 7, myApps.getData(  ) );
        daoUtil.setString( 8, myApps.getCodeHeading(  ) );
        daoUtil.setString( 9, myApps.getDataHeading(  ) );

        if ( ( myApps.getIconContent(  ) == null ) )
        {
            daoUtil.setBytes( 10, null );
            daoUtil.setString( 11, "" );
        }
        else
        {
            daoUtil.setBytes( 10, myApps.getIconContent(  ) );
            daoUtil.setString( 11, myApps.getIconMimeType(  ) );
        }

        daoUtil.setInt( 12, myApps.getIdApplication(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of all the myApps and returns them in form of a collection
     *
     * @param Plugin plugin
     * @return The List which contains the data of all the myApps
     */
    public List<MyApps> selectMyAppsList( Plugin plugin )
    {
        List<MyApps> myAppsList = new ArrayList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            MyApps myApps = new MyApps(  );

            myApps.setIdApplication( daoUtil.getInt( 1 ) );
            myApps.setName( daoUtil.getString( 2 ) );
            myApps.setDescription( daoUtil.getString( 3 ) );
            myApps.setUrl( daoUtil.getString( 4 ) );
            myApps.setCode( daoUtil.getString( 5 ) );
            myApps.setPassword( daoUtil.getString( 6 ) );
            myApps.setData( daoUtil.getString( 7 ) );
            myApps.setCodeHeading( daoUtil.getString( 8 ) );
            myApps.setDataHeading( daoUtil.getString( 9 ) );
            myApps.setIconContent( daoUtil.getBytes( 10 ) );
            myApps.setIconMimeType( daoUtil.getString( 11 ) );

            myAppsList.add( myApps );
        }

        daoUtil.free(  );

        return myAppsList;
    }

    /**
     * Loads the  myApps belonging to a user and returns them in form of a list
     *
     * @param strUserName The name of the user
     * @param Plugin plugin
     * @return The List which contains the myApps of a user
     */
    public List<MyApps> selectMyAppsListByUser( String strUserName, Plugin plugin )
    {
        List<MyApps> myAppsList = new ArrayList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_USER, plugin );
        daoUtil.setString( 1, strUserName );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            MyApps myApps = new MyApps(  );

            myApps.setIdApplication( daoUtil.getInt( 1 ) );
            myApps.setName( daoUtil.getString( 2 ) );
            myApps.setDescription( daoUtil.getString( 3 ) );
            myApps.setUrl( daoUtil.getString( 4 ) );
            myApps.setCode( daoUtil.getString( 5 ) );
            myApps.setPassword( daoUtil.getString( 6 ) );
            myApps.setData( daoUtil.getString( 7 ) );
            myApps.setCodeHeading( daoUtil.getString( 8 ) );
            myApps.setDataHeading( daoUtil.getString( 9 ) );
            myApps.setIconContent( daoUtil.getBytes( 10 ) );
            myApps.setIconMimeType( daoUtil.getString( 11 ) );

            myAppsList.add( myApps );
        }

        daoUtil.free(  );

        return myAppsList;
    }

    /**
     * Method to fetch the icon from the database
     * @param nIdMyApps The identifier of the application
     * @param plugin Plugin plugin
     * @return The image resource of the icon
     */
    public ImageResource getIconResource( int nIdMyApps, Plugin plugin )
    {
        //
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_RESOURCE_IMAGE, plugin );
        daoUtil.setInt( 1, nIdMyApps );
        daoUtil.executeQuery(  );

        ImageResource image = null;

        if ( daoUtil.next(  ) )
        {
            image = new ImageResource(  );
            image.setImage( daoUtil.getBytes( 1 ) );
            image.setMimeType( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return image;
    }
}
