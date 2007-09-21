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

import javax.security.auth.login.LoginException;

import javax.servlet.http.HttpServletRequest;


/**
 * This Interface defines all methods required by an authentication service password is not valid
 */
public interface LuteceAuthentication
{
    /**
     * Gets the Authentification service name
     * @return The Service Name
     */
    String getAuthServiceName(  );

    /**
     * Gets the Authentification type
     * @param request The HTTP request
     * @return The type of authentication
     */
    String getAuthType( HttpServletRequest request );

    /**
     * Checks the login
     *
     * @param strUserName The username
     * @param strUserPassword The user's passord
     * @param request The HttpServletRequest
     * @return The login
     * @throws LoginException The Login Exception
     */
    LuteceUser login( final String strUserName, final String strUserPassword, HttpServletRequest request )
        throws LoginException;

    /**
     * logout the user
     * @param user The user
     */
    void logout( LuteceUser user );

    /**
     * This method create an anonymous user
     *
     * @return A LuteceUser object corresponding to an anonymous user
     */
    LuteceUser getAnonymousUser(  );

    /**
     * Checks that the current user is associated to a given role
     * @param user The user
     * @param request The HTTP request
     * @param strRole The role name
     * @return Returns true if the user is associated to the role, otherwise false
     */
    boolean isUserInRole( LuteceUser user, HttpServletRequest request, String strRole );

    /**
     * Indicates that the user should be already authenticated by an external
     * authentication service (ex : Web Server authentication).
     * @return true if the authentication is external, false if the authentication
     * is provided by the Lutece portal.
     */
    boolean isExternalAuthentication(  );

    /**
     * Returns a Lutece user object if the user is already authenticated in the Http request.
     * This method should return null if the user is not authenticated or if
     * the authentication service is not based on Http authentication.
     * @param request The HTTP request
     * @return Returns A Lutece User
     */
    LuteceUser getHttpAuthenticatedUser( HttpServletRequest request );

    /**
     * Returns the Login page URL of the Authentication Service
     * @return The URL
     */
    String getLoginPageUrl(  );

    /**
     * Returns the DoLogin URL of the Authentication Service
     * @return The URL
     */
    String getDoLoginUrl(  );

    /**
     * Returns the DoLogout URL of the Authentication Service
     * @return The URL
     */
    String getDoLogoutUrl(  );

    /**
     * Returns the new account page URL of the Authentication Service
     * @return The URL
     */
    String getNewAccountPageUrl(  );

    /**
     * Returns the view account page URL of the Authentication Service
     * @return The URL
     */
    String getViewAccountPageUrl(  );

    /**
     * Returns the lost password URL of the Authentication Service
     * @return The URL
     */
    String getLostPasswordPageUrl(  );

    /**
     * Returns the template that contains the Access Denied message.
     * @return The template path
     */
    String getAccessDeniedTemplate(  );

    /**
     * Returns the template that contains the Access Controled message.
     * @return The template path
     */
    String getAccessControledTemplate(  );
}
