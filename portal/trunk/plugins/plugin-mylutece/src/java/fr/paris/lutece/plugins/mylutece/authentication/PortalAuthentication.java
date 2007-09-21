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
package fr.paris.lutece.plugins.mylutece.authentication;

import fr.paris.lutece.plugins.mylutece.web.MyLuteceApp;
import fr.paris.lutece.portal.service.security.LuteceAuthentication;
import fr.paris.lutece.portal.service.security.LuteceUser;

import javax.servlet.http.HttpServletRequest;


public abstract class PortalAuthentication implements LuteceAuthentication
{
    /**
     * Indicates that the user should be already authenticated by an external
     * authentication service (ex : Web Server authentication).
     * @return true if the authentication is external, false if the authentication
     * is provided by the Lutece portal.
     */
    public boolean isExternalAuthentication(  )
    {
        return false;
    }

    /**
     * Returns the Login page URL of the Authentication Service
     * @return The URL
     */
    public String getLoginPageUrl(  )
    {
        return MyLuteceApp.getLoginPageUrl(  );
    }

    /**
     * Returns the DoLogin URL of the Authentication Service
     * @return The URL
     */
    public String getDoLoginUrl(  )
    {
        return MyLuteceApp.getDoLoginUrl(  );
    }

    /**
     * Returns the new account page URL of the Authentication Service
     * @return The URL
     */
    public String getNewAccountPageUrl(  )
    {
        return MyLuteceApp.getNewAccountUrl(  );
    }

    /**
     * Returns the View account page URL of the Authentication Service
     * @return The URL
     */
    public String getViewAccountPageUrl(  )
    {
        return MyLuteceApp.getViewAccountUrl(  );
    }

    /**
     * Returns the lost password URL of the Authentication Service
     * @return The URL
     */
    public String getLostPasswordPageUrl(  )
    {
        return MyLuteceApp.getLostPasswordUrl(  );
    }

    /**
     * Returns the disconnect URL of the Authentication Service
     * @return The URL
     */
    public String getDoLogoutUrl(  )
    {
        return MyLuteceApp.getDoLogoutUrl(  );
    }

    /**
     * Returns a Lutece user object if the user is already authenticated in the Http request.
     * This method should return null if the user is not authenticated or if
     * the authentication service is not based on Http authentication.
     * @param request The HTTP request
     * @return Returns A Lutece User
     */
    public LuteceUser getHttpAuthenticatedUser( HttpServletRequest request )
    {
        return null;
    }

    /**
     * Returns the access denied template
     * @return The template
     */
    public String getAccessDeniedTemplate(  )
    {
        return MyLuteceApp.getAccessDeniedTemplate(  );
    }

    /**
     * Returns the access controled template
     * @return The template
     */
    public String getAccessControledTemplate(  )
    {
        return MyLuteceApp.getAccessControledTemplate(  );
    }
}
