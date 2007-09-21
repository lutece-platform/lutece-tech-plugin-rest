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
package fr.paris.lutece.plugins.mylutece.modules.wssodatabase.authentication;

import fr.paris.lutece.plugins.mylutece.authentication.ExternalAuthentication;
import fr.paris.lutece.plugins.mylutece.modules.wssodatabase.authentication.business.IIdxWSSODatabaseDAO;
import fr.paris.lutece.plugins.mylutece.modules.wssodatabase.authentication.business.IdxWSSODatabaseDAO;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/**
 * The Class provides an implementation of the PortalService interface based on
 * a the IdealX WebSSO solution. It retrieves roles associated with the user from the database.
 */
public class IdxWSSODatabaseAuthentication extends ExternalAuthentication
{
    private static final String PROPERTY_AUTH_SERVICE_NAME = "mylutece-wssodatabase.service.name";
    private static final String PROPERTY_COOKIE_AUTHENTIFICATION = "mylutece-wssodatabase.cookie.authenticationMode"; // authentication mode, login/pwd or certificate
    private static final String PROPERTY_COOKIE_WSSOGUID = "mylutece-wssodatabase.cookie.wssoguid"; // unique hexa user id
    private static final String PLUGIN_NAME = "mylutece-wssodatabase";

    // Static variable pointed at the DAO instance
    private static IIdxWSSODatabaseDAO _dao = (IdxWSSODatabaseDAO) SpringContextService.getPluginBean( "wssodatabase",
            "IdxWSSODatabaseDAO" );

    /**
     * Constructor
     */
    public IdxWSSODatabaseAuthentication(  )
    {
    }

    /**
     * Gets the Authentification service name
     * @return The name of the authentication service
     */
    public String getAuthServiceName(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_AUTH_SERVICE_NAME );
    }

    /**
     * Gets the Authentification type
     * @param request The HTTP request
     * @return The type of authentication
     */
    public String getAuthType( HttpServletRequest request )
    {
        Cookie[] cookies = request.getCookies(  );
        String strAuthType = request.getAuthType(  );

        for ( int i = 0; i < cookies.length; i++ )
        {
            Cookie cookie = cookies[i];

            if ( cookie.getName(  ).equals( PROPERTY_COOKIE_AUTHENTIFICATION ) )
            {
                strAuthType = cookie.getValue(  );
            }
        }

        return strAuthType;
    }

    /**
     * This methods checks the login info in the base repository
     *
     * @param strUserName The username
     * @param strUserPassword The password
     * @param request The HTTP request
     * @return A LuteceUser object corresponding to the login
     * @throws LoginException The LoginException
     */
    public LuteceUser login( String strUserName, String strUserPassword, HttpServletRequest request )
        throws LoginException
    {
        // There is no login required : the user is supposed to be already authenticated
        return getHttpAuthenticatedUser( request );
    }

    /**
     * This methods logout the user
     * @param user The user
     */
    public void logout( LuteceUser user )
    {
    }

    /**
     * This method returns an anonymous Lutece user
     *
     * @return An anonymous Lutece user
     */
    public LuteceUser getAnonymousUser(  )
    {
        /**@todo Impl?menter cette m?thode fr.paris.lutece.portal.service.security.PortalAuthentication*/
        throw new java.lang.UnsupportedOperationException( "The method getAnonymousUser() is not implemented yet." );
    }

    /**
     * Checks that the current user is associated to a given role
     * @param user The user
     * @param request The HTTP request
     * @param strRole The role name
     * @return Returns true if the user is associated to the role, otherwise false
     */
    public boolean isUserInRole( LuteceUser user, HttpServletRequest request, String strRole )
    {
        String[] roles = user.getRoles(  );

        if ( roles != null )
        {
            for ( int i = 0; i < roles.length; i++ )
            {
                if ( strRole.equals( roles[i] ) )
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns a Lutece user object if the user is already authenticated by the WSSO
     * @param request The HTTP request
     * @return Returns A Lutece User
     */
    public LuteceUser getHttpAuthenticatedUser( HttpServletRequest request )
    {
        Cookie[] cookies = request.getCookies(  );
        IdxWSSODatabaseUser user = null;
        String strUserID = null;

        for ( int i = 0; i < cookies.length; i++ )
        {
            Cookie cookie = cookies[i];

            if ( cookie.getName(  ).equals( AppPropertiesService.getProperty( PROPERTY_COOKIE_WSSOGUID ) ) )
            {
                strUserID = cookie.getValue(  );
            }
        }

        if ( strUserID != null )
        {
            Plugin plugin = PluginService.getPlugin( PLUGIN_NAME );
            user = _dao.findUserByGuid( strUserID, plugin, this );

            ArrayList arrayRoles = _dao.findUserRolesFromGuid( strUserID, plugin, this );

            if ( ( user != null ) && !arrayRoles.isEmpty(  ) )
            {
                user.setRoles( arrayRoles );
            }
        }

        return user;
    }
}
