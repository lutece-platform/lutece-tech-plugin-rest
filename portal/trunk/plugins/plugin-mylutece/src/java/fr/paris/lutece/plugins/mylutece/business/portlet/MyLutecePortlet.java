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
package fr.paris.lutece.plugins.mylutece.business.portlet;

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.util.xml.XmlUtil;

import javax.servlet.http.HttpServletRequest;


public class MyLutecePortlet extends Portlet
{
    private static final String TAG_MY_LUTECE_PORTLET = "mylutece-portlet";
    private static final String TAG_USER_NOT_SIGNED = "user-not-signed";
    private static final String TAG_LUTECE_USER = "lutece-user";
    private static final String TAG_LUTECE_USER_NAME = "lutece-user-name";
    private static final String TAG_LUTECE_USER_NAME_GIVEN = "lutece-user-name-given";
    private static final String TAG_LUTECE_USER_NAME_FAMILY = "lutece-user-name-family";
    private static final String TAG_LUTECE_USER_NEW_ACCOUNT_URL = "lutece-user-new-account-url";
    private static final String TAG_LUTECE_USER_VIEW_ACCOUNT_URL = "lutece-user-view-account-url";
    private static final String TAG_LUTECE_USER_LOST_PASSWORD_URL = "lutece-user-lost-password-url";
    private static final String TAG_LUTECE_USER_LOGOUT_URL = "lutece-user-logout-url";

    /**
     * Constructor
     */
    public MyLutecePortlet(  )
    {
    }

    /**
     * Returns the Xml code of the MyLutece portlet without XML heading
     *
     * @param request The HTTP Servlet request
     * @return the Xml code of the MyLutece portlet content
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer sbXml = new StringBuffer(  );

        if ( !SecurityService.isAuthenticationEnable(  ) )
        {
            XmlUtil.beginElement( sbXml, TAG_MY_LUTECE_PORTLET );
            XmlUtil.endElement( sbXml, TAG_MY_LUTECE_PORTLET );

            return sbXml.toString(  );
        }

        LuteceUser user = SecurityService.getInstance(  ).getRegisteredUser( request );

        XmlUtil.beginElement( sbXml, TAG_MY_LUTECE_PORTLET );

        if ( user != null )
        {
            XmlUtil.beginElement( sbXml, TAG_LUTECE_USER );
            XmlUtil.addElementHtml( sbXml, TAG_LUTECE_USER_NAME, user.getName(  ) );
            XmlUtil.addElementHtml( sbXml, TAG_LUTECE_USER_NAME_GIVEN, user.getUserInfo( LuteceUser.NAME_GIVEN ) );
            XmlUtil.addElementHtml( sbXml, TAG_LUTECE_USER_NAME_FAMILY, user.getUserInfo( LuteceUser.NAME_FAMILY ) );

            String strLogoutUrl = SecurityService.getInstance(  ).getDoLogoutUrl(  );

            if ( strLogoutUrl != null )
            {
                XmlUtil.addElementHtml( sbXml, TAG_LUTECE_USER_LOGOUT_URL, strLogoutUrl );
            }

            String strViewAccountUrl = SecurityService.getInstance(  ).getViewAccountPageUrl(  );

            if ( strViewAccountUrl != null )
            {
                XmlUtil.addElementHtml( sbXml, TAG_LUTECE_USER_VIEW_ACCOUNT_URL, strViewAccountUrl );
            }

            XmlUtil.endElement( sbXml, TAG_LUTECE_USER );
        }
        else
        {
            XmlUtil.beginElement( sbXml, TAG_USER_NOT_SIGNED );

            String strNewAccountUrl = SecurityService.getInstance(  ).getNewAccountPageUrl(  );

            if ( strNewAccountUrl != null )
            {
                XmlUtil.addElementHtml( sbXml, TAG_LUTECE_USER_NEW_ACCOUNT_URL, strNewAccountUrl );
            }

            String strLostPasswordUrl = SecurityService.getInstance(  ).getLostPasswordPageUrl(  );

            if ( strLostPasswordUrl != null )
            {
                XmlUtil.addElementHtml( sbXml, TAG_LUTECE_USER_LOST_PASSWORD_URL, strLostPasswordUrl );
            }

            XmlUtil.endElement( sbXml, TAG_USER_NOT_SIGNED );
        }

        XmlUtil.endElement( sbXml, TAG_MY_LUTECE_PORTLET );

        return addPortletTags( sbXml );
    }

    /**
     * Returns the Xml code of the MyLutece portlet with XML heading
     *
     * @param request The HTTP servlet request
     * @return the Xml code of the MyLutece portlet
     */
    public String getXmlDocument( HttpServletRequest request )
    {
        return XmlUtil.getXmlHeader(  ) + getXml( request );
    }

    /**
     * Update portlet's data
     */
    public void update(  )
    {
        MyLutecePortletHome.getInstance(  ).update( this );
    }

    /**
     * Remove of this portlet
     */
    public void remove(  )
    {
        MyLutecePortletHome.getInstance(  ).remove( this );
    }
}
