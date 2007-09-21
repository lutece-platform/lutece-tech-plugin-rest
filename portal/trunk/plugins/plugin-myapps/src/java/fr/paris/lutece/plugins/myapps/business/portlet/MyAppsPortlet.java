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
package fr.paris.lutece.plugins.myapps.business.portlet;

import fr.paris.lutece.plugins.myapps.business.MyApps;
import fr.paris.lutece.plugins.myapps.business.MyAppsHome;
import fr.paris.lutece.plugins.myapps.service.MyAppsService;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.web.constants.Parameters;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


public class MyAppsPortlet extends Portlet
{
    private static final String TAG_APP_USER_LIST = "myapps-list";
    private static final String TAG_APP_USER = "myapp";
    private static final String TAG_APP_NAME = "myapp-name";
    private static final String TAG_APP_ICON = "myapp-icon";
    private static final String TAG_APP_DESCRIPTION = "myapp-description";
    private static final String TAG_LINK = "myapp-link";
    private static final String TAG_BUTTON_LABEL = "myapp-button";
    private static final String PARAMETER_ID_APP = "id_app";
    private static final String PROPERTY_USER_ANONYMOUS = "anonymous";
    public static final String PROPERTY_MANAGE_MYAPPS_BUTTON_LABEL = "myapps.portlet.buttonManageMyApps";
    private static final String JSP_OPEN_MYAPP = "jsp/site/plugins/myapps/DoOpenMyApp.jsp";

    ///////////////////////////////////////////////////////////////////////////////
    //Variables
    private Plugin _plugin;

    // WARNING
    private String _strPluginName = "myapps";

    /**
     * Constructor
     */
    public MyAppsPortlet(  )
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
        String strUserName = PROPERTY_USER_ANONYMOUS;

        try
        {
            strUserName = SecurityService.getInstance(  ).getRegisteredUser( request ).getName(  );
        }

        catch ( NullPointerException e )
        {
            //User is anonymous
        }

        _plugin = PluginService.getPlugin( _strPluginName );

        StringBuffer strXml = new StringBuffer(  );
        strXml.append( "" );

        if ( !strUserName.equalsIgnoreCase( PROPERTY_USER_ANONYMOUS ) )
        {
            XmlUtil.beginElement( strXml, TAG_APP_USER_LIST );

            List<MyApps> listApplications = MyAppsHome.getmyAppsListByUser( strUserName, _plugin );
            String strButtonLabel = I18nService.getLocalizedString( PROPERTY_MANAGE_MYAPPS_BUTTON_LABEL,
                    request.getLocale(  ) );

            for ( MyApps myapp : listApplications )
            {
                XmlUtil.beginElement( strXml, TAG_APP_USER );

                String strLink = JSP_OPEN_MYAPP + "?" + PARAMETER_ID_APP + "=" + myapp.getIdApplication(  );
                XmlUtil.addElement( strXml, TAG_APP_ICON,
                    getResourceImagePage( Integer.toString( myapp.getIdApplication(  ) ) ) );
                XmlUtil.addElement( strXml, TAG_APP_NAME, myapp.getName(  ) );
                XmlUtil.addElement( strXml, TAG_APP_DESCRIPTION, myapp.getDescription(  ) );
                XmlUtil.addElement( strXml, TAG_LINK, strLink );
                XmlUtil.endElement( strXml, TAG_APP_USER );
            }

            XmlUtil.addElement( strXml, TAG_BUTTON_LABEL, strButtonLabel );
            XmlUtil.endElement( strXml, TAG_APP_USER_LIST );
        }

        return addPortletTags( strXml );
    }

    /**
     * Returns the Xml code of the MyApps portlet with XML heading
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
        MyAppsPortletHome.getInstance(  ).update( this );
    }

    /**
     * Remove of this portlet
     */
    public void remove(  )
    {
        MyAppsPortletHome.getInstance(  ).remove( this );
    }

    /**
     * Management of the image associated to the application
     * @param page The MyApps Object
     * @param strMyAppsId The myapps identifier
     */
    public String getResourceImagePage( String strMyAppsId )
    {
        String strResourceType = MyAppsService.getInstance(  ).getResourceTypeId(  );
        UrlItem url = new UrlItem( Parameters.IMAGE_SERVLET );
        url.addParameter( Parameters.RESOURCE_TYPE, strResourceType );
        url.addParameter( Parameters.RESOURCE_ID, strMyAppsId );

        return url.getUrlWithEntity(  );
    }
}
