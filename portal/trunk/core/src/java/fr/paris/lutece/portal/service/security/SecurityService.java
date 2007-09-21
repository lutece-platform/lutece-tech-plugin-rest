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
package fr.paris.lutece.portal.service.security;

import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.security.Principal;

import javax.security.auth.login.LoginException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * This class provides a security service to register and check user authentication
 */
public final class SecurityService
{
    /**
     * Session attribute that stores the LuteceUser object attached to the session
     */
    private static final String ATTRIBUTE_LUTECE_USER = "lutece_user";
    private static final String PROPERTY_AUTHENTICATION_CLASS = "mylutece.authentication.class";
    private static final String PROPERTY_AUTHENTICATION_ENABLE = "mylutece.authentication.enable";
    private static final String PROPERTY_PORTAL_AUTHENTICATION_REQUIRED = "mylutece.portal.authentication.required";
    private static SecurityService _singleton = new SecurityService(  );
    private static LuteceAuthentication _authenticationService;
    private static boolean _bEnable;

    /**
     * Private constructor
     */
    private SecurityService(  )
    {
    }

    /**
     * Initialize service
     */
    public static synchronized void init(  ) throws LuteceInitException
    {
        _bEnable = false;

        String strEnable = AppPropertiesService.getProperty( PROPERTY_AUTHENTICATION_ENABLE, "false" );

        if ( strEnable.equalsIgnoreCase( "true" ) )
        {
            _authenticationService = getPortalAuthentication(  );

            if ( _authenticationService != null )
            {
                _bEnable = true;
            }
        }
    }

    /**
     * Get the unique instance of the Security Service
     * @return The instance
     */
    public static SecurityService getInstance(  )
    {
        return _singleton;
    }

    /**
     * Returns the authentication's activation : enable or disable
     * @return true if the authentication is active, false otherwise
     */
    public static boolean isAuthenticationEnable(  )
    {
        return _bEnable;
    }

    /**
     * Gets the LuteceUser attached to the current Http session
     * @param request The Http request
     * @return A LuteceUser object if found
     * @throws UserNotSignedException If there is no current user
     */
    public LuteceUser getRemoteUser( HttpServletRequest request )
        throws UserNotSignedException
    {
        LuteceUser user = getRegisteredUser( request );

        if ( user == null )
        {
            // User is not registered by Lutece, but it may be authenticated by another system
            if ( _authenticationService.isExternalAuthentication(  ) )
            {
                user = _authenticationService.getHttpAuthenticatedUser( request );
                registerUser( request, user );
            }
            else
            {
                throw new UserNotSignedException(  );
            }
        }

        return user;
    }

    /**
     * Returns the user's principal
     * @param request The HTTP request
     * @return The user's principal
     * @throws UserNotSignedException The UserNotSignedException
     */
    public Principal getUserPrincipal( HttpServletRequest request )
        throws UserNotSignedException
    {
        return getRemoteUser( request );
    }

    /**
     * Checks if the user is associated to a given role
     * @param request The Http request
     * @param strRole The Role name
     * @return Returns true if the user is associated to the given role
     */
    public boolean isUserInRole( HttpServletRequest request, String strRole )
    {
        LuteceUser user;

        try
        {
            user = getRemoteUser( request );
        }
        catch ( UserNotSignedException e )
        {
            return false;
        }

        return _authenticationService.isUserInRole( user, request, strRole );
    }

    /**
     * Checks user's login with the Authentication service.
     * @param request The Http request
     * @param strUserName The user's login
     * @param strPassword The user's password
     * @throws LoginException The LoginException
     */
    public void loginUser( HttpServletRequest request, final String strUserName, final String strPassword )
        throws LoginException
    {
        LuteceUser user = _authenticationService.login( strUserName, strPassword, request );
        registerUser( request, user );
    }

    /**
     * Logout the user
     * @param request The HTTP request
     */
    public void logoutUser( HttpServletRequest request )
    {
        LuteceUser user;

        try
        {
            user = getRemoteUser( request );
        }
        catch ( UserNotSignedException e )
        {
            return;
        }

        _authenticationService.logout( user );
        unregisterUser( request );
    }

    /**
     * Retrieves the portal authentication service configured in the config.properties
     * @return A PortalAuthentication object
     */
    private static LuteceAuthentication getPortalAuthentication(  )
        throws LuteceInitException
    {
        String strAuthenticationClass = AppPropertiesService.getProperty( PROPERTY_AUTHENTICATION_CLASS );
        LuteceAuthentication authentication = null;

        try
        {
            authentication = (LuteceAuthentication) Class.forName( strAuthenticationClass ).newInstance(  );
            AppLogService.info( "Authentication service loaded : " + authentication.getAuthServiceName(  ) );
        }
        catch ( InstantiationException e )
        {
            throw new LuteceInitException( "Error instantiating Authentication Class", e );
        }
        catch ( IllegalAccessException e )
        {
            throw new LuteceInitException( "Error instantiating Authentication Class", e );
        }
        catch ( ClassNotFoundException e )
        {
            throw new LuteceInitException( "Error instantiating Authentication Class", e );
        }

        return authentication;
    }

    /**
     * Register the user in the Http session
     * @param request The Http request
     * @param user The current user
     */
    private void registerUser( HttpServletRequest request, LuteceUser user )
    {
        HttpSession session = request.getSession( true );
        session.setAttribute( ATTRIBUTE_LUTECE_USER, user );
    }

    /**
     * Unregister the user in the Http session
     * @param request The Http request
     */
    private void unregisterUser( HttpServletRequest request )
    {
        HttpSession session = request.getSession( true );
        session.removeAttribute( ATTRIBUTE_LUTECE_USER );
    }

    /**
     * Gets the Lutece user registered in the Http session
     * @param request The HTTP request
     * @return The User registered or null if the user has not been registered
     */
    public LuteceUser getRegisteredUser( HttpServletRequest request )
    {
        HttpSession session = request.getSession(  );

        if ( session != null )
        {
            return (LuteceUser) session.getAttribute( ATTRIBUTE_LUTECE_USER );
        }

        return null;
    }

    /**
     * Returns the authentication type : External or Lutece portal based
     * @return true if the user is already authenticated or false if it needs to login.
     */
    public boolean isExternalAuthentication(  )
    {
        return _authenticationService.isExternalAuthentication(  );
    }

    /**
     * Returns the Login page URL of the Authentication Service
     * @return The URL
     */
    public String getLoginPageUrl(  )
    {
        return _authenticationService.getLoginPageUrl(  );
    }

    /**
     * Returns the DoLogin URL of the Authentication Service
     * @return The URL
     */
    public String getDoLoginUrl(  )
    {
        return _authenticationService.getDoLoginUrl(  );
    }

    /**
     * Returns the DoLogout URL of the Authentication Service
     * @return The URL
     */
    public String getDoLogoutUrl(  )
    {
        return _authenticationService.getDoLogoutUrl(  );
    }

    /**
     * Returns the new account page URL of the Authentication Service
     * @return The URL
     */
    public String getNewAccountPageUrl(  )
    {
        return _authenticationService.getNewAccountPageUrl(  );
    }

    /**
     * Returns the view account page URL of the Authentication Service
     * @return The URL
     */
    public String getViewAccountPageUrl(  )
    {
        return _authenticationService.getViewAccountPageUrl(  );
    }

    /**
     * Returns the lost password URL of the Authentication Service
     * @return The URL
     */
    public String getLostPasswordPageUrl(  )
    {
        return _authenticationService.getLostPasswordPageUrl(  );
    }

    // Added in v1.3

    /**
     * Returns the access denied template
     * @return The template
     */
    public String getAccessDeniedTemplate(  )
    {
        return _authenticationService.getAccessDeniedTemplate(  );
    }

    /**
     * Returns the access controled template
     * @return The template
     */
    public String getAccessControledTemplate(  )
    {
        return _authenticationService.getAccessControledTemplate(  );
    }

    /**
     * Returns whether or not the portal needs authentication
     * @return true if the access needs authentication, otherwise
     * @since 1.3.1
     */
    public boolean isPortalAuthenticationRequired(  )
    {
        String strAuthenticationRequired = AppPropertiesService.getProperty( PROPERTY_PORTAL_AUTHENTICATION_REQUIRED,
                "false" );

        return strAuthenticationRequired.equals( "true" );
    }

    /**
     * Checks user's login with the Authentication service.
     * Used during remote authentication validation
     * We don't have to put user informations in session, since it is only used
     * in external applications
     * @param request the request
     * @param strUserName The user's login
     * @param strPassword The user's password
     * @return user's informations
     * @throws LoginException The LoginException
     */
    public LuteceUser remoteLoginUser( final HttpServletRequest request, final String strUserName,
        final String strPassword ) throws LoginException
    {
        LuteceUser user = _authenticationService.login( strUserName, strPassword, request );

        return user;
    }
}
