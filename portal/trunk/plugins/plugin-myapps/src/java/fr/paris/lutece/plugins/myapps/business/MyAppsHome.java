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
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for MyApps objects
 */
public final class MyAppsHome
{
    // Static variable pointed at the DAO instance
    private static IMyAppsDAO _dao = (IMyAppsDAO) SpringContextService.getPluginBean( "myapps", "myAppsDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private MyAppsHome(  )
    {
    }

    /**
     * Creation of an instance of myApps
     *
     * @param myApps The instance of the MyApps which contains the informations to store
     * @param plugin the Plugin
     * @return The  instance of myApps which has been created with its primary key.
     */
    public static MyApps create( MyApps myApps, Plugin plugin )
    {
        _dao.insert( myApps, plugin );

        return myApps;
    }

    /**
     * Update of the myApps which is specified in parameter
     *
     * @param myApps The instance of the MyApps which contains the data to store
     * @param plugin the Plugin
     * @return The instance of the  myApps which has been updated
     */
    public static MyApps update( MyApps myApps, Plugin plugin )
    {
        _dao.store( myApps, plugin );

        return myApps;
    }

    /**
     * Remove the myApps whose identifier is specified in parameter
     *
     * @param nMyAppsId The myApps Id
     * @param plugin the Plugin
     */
    public static void remove( int nMyAppsId, Plugin plugin )
    {
        _dao.delete( nMyAppsId, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a myApps whose identifier is specified in parameter
     *
     * @param nKey The myApps primary key
     * @param plugin the Plugin
     * @return an instance of MyApps
     */
    public static MyApps findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Loads a list of all myApps
     *
     * @param plugin the Plugin
     * @return the collection which contains the data of all the myApps
     */
    public static List<MyApps> getmyAppsList( Plugin plugin )
    {
        return _dao.selectMyAppsList( plugin );
    }

    /**
    * Loads a list of myApps belonging to a user
    *
    * @param strUser the user name
    * @param plugin the Plugin
    * @return the collection which contains the data of all the myApps
    */
    public static List<MyApps> getmyAppsListByUser( String strUser, Plugin plugin )
    {
        return _dao.selectMyAppsListByUser( strUser, plugin );
    }

    /**
     *
    * @param nMyAppsId The myApps Id
    * @param plugin the Plugin
    * @return ImageResource
    */
    public static ImageResource getImageResource( int nMyAppsId, Plugin plugin )
    {
        return _dao.getIconResource( nMyAppsId, plugin );
    }
}
