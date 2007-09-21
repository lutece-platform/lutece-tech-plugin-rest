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
package fr.paris.lutece.portal.web.user;

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.url.UrlItem;

import java.io.IOException;

import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Filter to prevent unauthenticated access to admin
 */
public class AuthenticationFilter implements Filter
{
    private static final String PROPERTY_URL_PREFIX = "path.jsp.admin.public.";
    private static final String PROPERTY_URL_SUFFIX_LIST = "list";
    private static final String CONSTANT_LIST_SEPARATOR = ",";

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     * @param config The FilterConfig
     * @throws ServletException
     */
    public void init( FilterConfig config ) throws ServletException
    {
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy(  )
    {
        // Do nothing
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     * @param request The ServletRequest
     * @param response The ServletResponse
     * @param chain The FilterChain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
        throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        AppLogService.debug( "lutece.authentication", "Accessing url : " + getResquestedUrl( req ) );

        if ( isPrivateUrl( req ) )
        {
            try
            {
                filterAccess( req );
            }
            catch ( UserNotSignedException e )
            {
                AppLogService.debug( "lutece.authentication", "Access NOT granted to url : " + getResquestedUrl( req ) );

                String strRedirectUrl = AdminMessageService.getMessageUrl( req,
                        Messages.MESSAGE_USER_NOT_AUTHENTICATED, getRedirectUrl( req ), AdminMessage.TYPE_WARNING );
                resp.sendRedirect( getAbsoluteUrl( req, strRedirectUrl ) );
            }
            catch ( AccessDeniedException e )
            {
                AppLogService.debug( "lutece.authentication", "Access NOT granted to url : " + getResquestedUrl( req ) );

                String strRedirectUrl = AdminMessageService.getMessageUrl( req, Messages.MESSAGE_AUTH_FAILURE,
                        getRedirectUrl( req ), AdminMessage.TYPE_ERROR );
                resp.sendRedirect( getAbsoluteUrl( req, strRedirectUrl ) );
            }
        }

        chain.doFilter( request, response );
    }

    /**
     * Build the url to redirect to if not logged.
     * This is actually the login page of the authentication module, completed with the request parameters.
     * @param request the http request
     * @return the string representation of the redirection url - absolute - with request parameters.
     */
    private String getRedirectUrl( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( getLoginUrl( request ) );

        Enumeration enumParams = request.getParameterNames(  );

        String strParamName;

        while ( enumParams.hasMoreElements(  ) )
        {
            strParamName = (String) enumParams.nextElement(  );

            if ( !strParamName.equals( Parameters.ACCESS_CODE ) && !strParamName.equals( Parameters.PASSWORD ) )
            {
                url.addParameter( strParamName, request.getParameter( strParamName ) );
            }
        }

        return url.getUrl(  );
    }

    /**
     * Get the absolute login url
     *
     * @param request the http request
     * @return the login url, in its absolute form
     *
     * */
    private String getLoginUrl( HttpServletRequest request )
    {
        String strLoginUrl = AdminAuthenticationService.getInstance(  ).getLoginPageUrl(  );

        return getAbsoluteUrl( request, strLoginUrl );
    }

    /**
     * Check wether a given url is to be considered as private (ie that
     * needs a successful authentication to be accessed) or public (ie that
     * can be access without being authenticated)
     * @param request the http request
     * @return true if the url needs to be authenticated, false otherwise
     *
     * */
    private boolean isPrivateUrl( HttpServletRequest request )
    {
        boolean bIsRestricted = true;
        String strUrl = getResquestedUrl( request );

        if ( strUrl.equals( getLoginUrl( request ) ) )
        {
            bIsRestricted = false;
        }
        else if ( isInPublicUrlList( request, strUrl ) )
        {
            bIsRestricted = false;
        }

        return bIsRestricted;
    }

    /**
     * check that the access is granted
     *  @param request The HTTP request
     *  @throws AccessDeniedException
     *  @throws UserNotSignedException
     *
     **/
    private static void filterAccess( HttpServletRequest request )
        throws UserNotSignedException, AccessDeniedException
    {
        if ( AdminAuthenticationService.getInstance(  ).isExternalAuthentication(  ) )
        {
            // The authentication is external
            // Should register the user if it's not already done
            AdminAuthenticationService.getInstance(  ).getRemoteUser( request );
        }
        else
        {
            if ( AdminAuthenticationService.getInstance(  ).getRegisteredUser( request ) == null )
            {
                // Authentication is required to access to the admin
                throw new UserNotSignedException(  );
            }
        }
    }

    /**
     * Checks if the requested is in the list of urls that are under
     * jsp/admin but shouldn't be protected
     * @param request the http request (provides the base path if needed)
     * @param strRequestedUrl the url to test : it should start with "http://" is absolute, or should be relative to the webapp root otherwise
     * @return true if the url is in the list, false otherwise
     *
     * */
    private boolean isInPublicUrlList( HttpServletRequest request, String strRequestedUrl )
    {
        // recovers list from the
        String strList = AppPropertiesService.getProperty( PROPERTY_URL_PREFIX + PROPERTY_URL_SUFFIX_LIST );

        // extracts each item (separated by a comma) from the includes list
        StringTokenizer strTokens = new StringTokenizer( strList, CONSTANT_LIST_SEPARATOR );

        while ( strTokens.hasMoreTokens(  ) )
        {
            String strName = strTokens.nextToken(  );
            String strUrl = AppPropertiesService.getProperty( PROPERTY_URL_PREFIX + strName );
            strUrl = getAbsoluteUrl( request, strUrl );

            if ( strRequestedUrl.equals( strUrl ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the absolute url corresponding to the given one, if the later
     * was found to be relative. An url starting with "http://" is absolute.
     * A relative url should be given relatively to the webapp root.
     * @param request the http request (provides the base path if needed)
     * @param strUrl the url to transform
     * @return the corresonding absolute url
     *
     * */
    private String getAbsoluteUrl( HttpServletRequest request, String strUrl )
    {
        if ( ( strUrl != null ) && !strUrl.startsWith( "http://" ) && !strUrl.startsWith( "https://" ) )
        {
            return AppPathService.getBaseUrl( request ) + strUrl;
        }
        else
        {
            return strUrl;
        }
    }

    /**
     * Return the absolute representation of the requested url
     * @param request the http request (provides the base path if needed)
     * @return the requested url has a string
     *
     * */
    private String getResquestedUrl( HttpServletRequest request )
    {
        return AppPathService.getBaseUrl( request ) + request.getServletPath(  ).substring( 1 );
    }
}
