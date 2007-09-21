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
package fr.paris.lutece.plugins.mylutece.modules.ldapdatabase.authentication.web;

import fr.paris.lutece.plugins.mylutece.modules.ldapdatabase.authentication.business.LdapDatabaseHome;
import fr.paris.lutece.plugins.mylutece.modules.ldapdatabase.authentication.business.LdapUser;
import fr.paris.lutece.plugins.mylutece.modules.ldapdatabase.authentication.business.LdapUserHome;
import fr.paris.lutece.plugins.mylutece.modules.ldapdatabase.authentication.util.LdapBrowser;
import fr.paris.lutece.portal.business.role.Role;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage roles features ( manage, create, modify, remove )
 */
public class LdapdatabaseJspBean extends PluginAdminPageJspBean
{
    private static final String MANAGE_USERS = "ManageUsers.jsp";

    // Right
    public static final String RIGHT_MANAGE_LDAPDATABASE_USERS = "LDAPDATABASE_MANAGEMENT_USERS";

    //JSP
    private static final String JSP_DO_REMOVE_USER = "jsp/admin/plugins/mylutece/modules/ldapdatabase/DoRemoveUser.jsp";

    //Propety
    private static final String PROPERTY_PAGE_TITLE_MANAGE_USERS = "module.mylutece.ldapdatabase.manage_users.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_USER = "module.mylutece.ldapdatabase.create_user.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MANAGE_ROLES_USER = "module.mylutece.ldapdatabase.manage_roles_user.pageTitle";

    //Messages
    private static final String MESSAGE_CONFIRM_REMOVE_USER = "module.mylutece.ldapdatabase.message.confirmRemoveUser";
    private static final String MESSAGE_USER_EXIST = "module.mylutece.ldapdatabase.message.user_exist";
    private static final String MESSAGE_ERROR_CREATE_USER = "module.mylutece.ldapdatabase.message.create.user";
    private static final String MESSAGE_ERROR_REMOVE_USER = "module.mylutece.ldapdatabase.message.remove.user";

    // Parameters
    private static final String PARAMETER_PLUGIN_NAME = "plugin_name";
    private static final String PARAMETER_MYLUTECE_LDAP_USER_ID = "mylutece_ldap_user_id";
    private static final String PARAMETER_MYLUTECE_LDAP_ROLE_ID = "mylutece_ldap_role_id";
    private static final String PARAMETER_GUID = "guid";
    private static final String PARAMETER_LAST_NAME = "last_name";
    private static final String PARAMETER_FIRST_NAME = "first_name";
    private static final String PARAMETER_EMAIL = "email";

    // Marks FreeMarker
    private static final String MARK_USERS_LIST = "user_list";
    private static final String MARK_USER = "user";
    private static final String MARK_PLUGIN_NAME = "plugin_name";
    private static final String MARK_LAST_NAME = "last_name";
    private static final String MARK_FIRST_NAME = "first_name";
    private static final String MARK_EMAIL = "email";
    private static final String MARK_ROLES_LIST = "role_list";
    private static final String MARK_ROLES_LIST_FOR_USER = "user_role_list";

    // Templates
    private static final String TEMPLATE_CREATE_USER = "admin/plugins/mylutece/modules/ldapdatabase/create_user.html";
    private static final String TEMPLATE_MANAGE_USERS = "admin/plugins/mylutece/modules/ldapdatabase/manage_users.html";
    private static final String TEMPLATE_MANAGE_ROLES_USER = "admin/plugins/mylutece/modules/ldapdatabase/manage_roles_user.html";
    private static final String CONSTANT_WILDCARD = "*";
    private static Plugin _plugin;

    /**
     * Creates a new WssodatabaseJspBean object.
     */
    public LdapdatabaseJspBean(  )
    {
    }

    /**
     * Returns the User creation form
     *
     * @param request The Http request
     * @return Html creation form
     */
    public String getCreateUser( HttpServletRequest request )
    {
        LdapBrowser ldap = new LdapBrowser(  );
        Collection userList = null;

        if ( _plugin == null )
        {
            String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );
            _plugin = PluginService.getPlugin( strPluginName );
        }

        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_USER );

        // get the filter parameters
        String strUserLastName = request.getParameter( PARAMETER_LAST_NAME );

        if ( strUserLastName == null )
        {
            strUserLastName = "";
        }

        String strUserFirstName = request.getParameter( PARAMETER_FIRST_NAME );

        if ( strUserFirstName == null )
        {
            strUserFirstName = "";
        }

        String strUserEMail = request.getParameter( PARAMETER_EMAIL );

        if ( strUserEMail == null )
        {
            strUserEMail = "";
        }

        //search in LDAP
        if ( !( ( strUserLastName.equals( "" ) == true ) && ( strUserFirstName.equals( "" ) == true ) &&
                ( strUserEMail.equals( "" ) == true ) ) )
        {
            try
            {
                userList = ldap.getUserList( strUserLastName + CONSTANT_WILDCARD, strUserFirstName + CONSTANT_WILDCARD,
                        strUserEMail + CONSTANT_WILDCARD );
            }
            catch ( Exception e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }
        }

        HashMap model = new HashMap(  );
        model.put( MARK_USERS_LIST, userList );
        model.put( MARK_PLUGIN_NAME, _plugin.getName(  ) );
        model.put( MARK_LAST_NAME, strUserLastName );
        model.put( MARK_FIRST_NAME, strUserFirstName );
        model.put( MARK_EMAIL, strUserEMail );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_USER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process user's creation
     *
     * @param request The Http request
     * @return The user's Displaying Url
     */
    public String doCreateUser( HttpServletRequest request )
    {
        Collection userList = null;
        LdapUser user = null;
        LdapBrowser ldap = new LdapBrowser(  );

        if ( _plugin == null )
        {
            String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );
            _plugin = PluginService.getPlugin( strPluginName );
        }

        String strUserGuid = request.getParameter( PARAMETER_GUID );

        if ( strUserGuid == null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_CREATE_USER, AdminMessage.TYPE_ERROR );
        }

        userList = LdapUserHome.findLdapUsersListForGuid( strUserGuid, _plugin );

        if ( userList.size(  ) == 0 )
        {
            user = ldap.getUserPublicData( strUserGuid );

            if ( user == null )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_CREATE_USER, AdminMessage.TYPE_ERROR );
            }

            LdapUserHome.create( user, _plugin );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_USER_EXIST, AdminMessage.TYPE_STOP );
        }

        return MANAGE_USERS + "?" + PARAMETER_PLUGIN_NAME + "=" + _plugin.getName(  );
    }

    /**
     * Returns removal user's form
     *
     * @param request The Http request
     * @return Html form
     */
    public String getRemoveUser( HttpServletRequest request )
    {
        if ( _plugin == null )
        {
            String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );
            _plugin = PluginService.getPlugin( strPluginName );
        }

        int nUserId = Integer.parseInt( request.getParameter( PARAMETER_MYLUTECE_LDAP_USER_ID ) );

        UrlItem url = new UrlItem( JSP_DO_REMOVE_USER );
        url.addParameter( PARAMETER_PLUGIN_NAME, _plugin.getName(  ) );
        url.addParameter( PARAMETER_MYLUTECE_LDAP_USER_ID, nUserId );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_USER, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Process user's removal
     *
     * @param request The Http request
     * @return The Jsp management URL of the process result
     */
    public String doRemoveUser( HttpServletRequest request )
    {
        if ( _plugin == null )
        {
            String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );
            _plugin = PluginService.getPlugin( strPluginName );
        }

        if ( request.getParameter( PARAMETER_MYLUTECE_LDAP_USER_ID ) == null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REMOVE_USER, AdminMessage.TYPE_ERROR );
        }

        int nUserId = Integer.parseInt( request.getParameter( PARAMETER_MYLUTECE_LDAP_USER_ID ) );

        LdapUser user = LdapUserHome.findByPrimaryKey( nUserId, _plugin );

        if ( user == null )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_REMOVE_USER, AdminMessage.TYPE_ERROR );
        }

        LdapUserHome.remove( user, _plugin );

        LdapDatabaseHome.removeRolesForUser( nUserId, _plugin );

        return MANAGE_USERS + "?" + PARAMETER_PLUGIN_NAME + "=" + _plugin.getName(  );
    }

    /**
     * Returns users management form
     *
     * @param request The Http request
     * @return Html form
     */
    public String getManageUsers( HttpServletRequest request )
    {
        if ( _plugin == null )
        {
            String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );
            _plugin = PluginService.getPlugin( strPluginName );
        }

        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_USERS );

        Collection userList = LdapUserHome.findLdapUsersList( _plugin );
        HashMap model = new HashMap(  );
        model.put( MARK_USERS_LIST, userList );
        model.put( MARK_PLUGIN_NAME, _plugin.getName(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_USERS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns roles management form for a specified user
     *
     * @param request The Http request
     * @return Html form
     */
    public String getManageRolesUser( HttpServletRequest request )
    {
        if ( _plugin == null )
        {
            String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );
            _plugin = PluginService.getPlugin( strPluginName );
        }

        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_ROLES_USER );

        int nUserId = Integer.parseInt( request.getParameter( PARAMETER_MYLUTECE_LDAP_USER_ID ) );

        LdapUser user = LdapUserHome.findByPrimaryKey( nUserId, _plugin );

        Collection<Role> allRoleList = RoleHome.findAll(  );
        ArrayList<String> userRoleKeyList = LdapDatabaseHome.findUserRolesFromGuid( user.getLdapGuid(  ), _plugin );
        Collection<Role> userRoleList = new ArrayList<Role>(  );

        for ( String strRoleKey : userRoleKeyList )
        {
            for ( Role role : allRoleList )
            {
                if ( role.getRole(  ).equals( strRoleKey ) )
                {
                    userRoleList.add( RoleHome.findByPrimaryKey( strRoleKey ) );
                }
            }
        }

        HashMap model = new HashMap(  );
        model.put( MARK_ROLES_LIST, allRoleList );
        model.put( MARK_ROLES_LIST_FOR_USER, userRoleList );
        model.put( MARK_USER, user );
        model.put( MARK_PLUGIN_NAME, _plugin.getName(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_ROLES_USER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process assignation roles for a specified user
     *
     * @param request The Http request
     * @return Html form
     */
    public String doAssignRoleUser( HttpServletRequest request )
    {
        if ( _plugin == null )
        {
            String strPluginName = request.getParameter( PARAMETER_PLUGIN_NAME );
            _plugin = PluginService.getPlugin( strPluginName );
        }

        int nUserId = Integer.parseInt( request.getParameter( PARAMETER_MYLUTECE_LDAP_USER_ID ) );
        LdapUser user = LdapUserHome.findByPrimaryKey( nUserId, _plugin );

        String[] roleArray = request.getParameterValues( PARAMETER_MYLUTECE_LDAP_ROLE_ID );

        LdapDatabaseHome.removeRolesForUser( user.getUserId(  ), _plugin );

        if ( roleArray != null )
        {
            for ( int i = 0; i < roleArray.length; i++ )
            {
                LdapDatabaseHome.addRoleForUser( nUserId, roleArray[i], _plugin );
            }
        }

        return MANAGE_USERS + "?" + PARAMETER_PLUGIN_NAME + "=" + _plugin.getName(  );
    }
}
