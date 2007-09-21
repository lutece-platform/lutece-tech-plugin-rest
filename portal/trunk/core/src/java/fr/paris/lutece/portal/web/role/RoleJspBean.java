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
package fr.paris.lutece.portal.web.role;

import fr.paris.lutece.portal.business.role.Role;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * JspBean for Role management
 */
public class RoleJspBean extends AdminFeaturesPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////////
    // Constant

    // Right
    public static final String RIGHT_ROLES_MANAGEMENT = "CORE_ROLES_MANAGEMENT";

    // Markers
    private static final String MARK_ROLES_LIST = "roles_list";
    private static final String MARK_ROLE = "role";

    // Parameters
    private static final String PARAMETER_PAGE_ROLE = "role";
    private static final String PARAMETER_PAGE_ROLE_DESCRIPTION = "role_description";

    // Templates
    private static final String TEMPLATE_MANAGE_ROLES = "admin/role/manage_roles.html";
    private static final String TEMPLATE_PAGE_ROLE_MODIFY = "admin/role/modify_page_role.html";
    private static final String TEMPLATE_CREATE_PAGE_ROLE = "admin/role/create_page_role.html";

    // Jsp
    private static final String PATH_JSP = "jsp/admin/role/";
    private static final String JSP_REMOVE_ROLE = "DoRemovePageRole.jsp";

    // Properties
    private static final String PROPERTY_PAGE_TITLE_CREATE_ROLE = "portal.role.create_role.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_ROLE = "portal.role.modify_role.pageTitle";

    // Message
    private static final String MESSAGE_ROLE_EXIST = "portal.role.message.roleexist";
    private static final String MESSAGE_CONFIRM_REMOVE = "portal.role.message.confirmRemoveRole";

    /**
     * Creates a new RoleJspBean object.
    */
    public RoleJspBean(  )
    {
    }

    /**
     * Returns Page Role management form
     * @param request The Http request
     * @return Html form
     */
    public String getManagePageRole( HttpServletRequest request )
    {
        setPageTitleProperty( null );

        HashMap model = new HashMap(  );
        model.put( MARK_ROLES_LIST, RoleHome.findAll(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_ROLES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Insert a new PageRole
     * @param request The HTTP request
     * @return String The html code page
     */
    public String getCreatePageRole( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_ROLE );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_PAGE_ROLE, getLocale(  ) );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Create PageRole
     * @param request The HTTP request
     * @return String The url page
     */
    public String doCreatePageRole( HttpServletRequest request )
    {
        String strPageRole = request.getParameter( PARAMETER_PAGE_ROLE );
        String strPageRoleDescription = request.getParameter( PARAMETER_PAGE_ROLE_DESCRIPTION );

        // Mandatory field
        if ( strPageRole.equals( "" ) || strPageRoleDescription.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // verifie que le role n'existe pas
        if ( RoleHome.findExistRole( strPageRole ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ROLE_EXIST, AdminMessage.TYPE_STOP );
        }

        Role role = new Role(  );
        role.setRole( strPageRole );
        role.setRoleDescription( strPageRoleDescription );
        RoleHome.create( role );

        return getHomeUrl( request );
    }

    /**
     *
     * @param request The HTTP request
     * @return String The html code page
     */
    public String getModifyPageRole( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_ROLE );

        HashMap model = new HashMap(  );

        String strPageRole = (String) request.getParameter( PARAMETER_PAGE_ROLE );
        model.put( MARK_ROLE, RoleHome.findByPrimaryKey( strPageRole ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PAGE_ROLE_MODIFY, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Modify PageRole
     * @param request The HTTP request
     * @return String The url page
     */
    public String doModifyPageRole( HttpServletRequest request )
    {
        String strPageRole = request.getParameter( PARAMETER_PAGE_ROLE );
        String strPageRoleDescription = request.getParameter( PARAMETER_PAGE_ROLE_DESCRIPTION );

        // Mandatory field
        if ( strPageRoleDescription.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        Role role = new Role(  );
        role.setRole( strPageRole );
        role.setRoleDescription( strPageRoleDescription );
        RoleHome.update( role );

        return getHomeUrl( request );
    }

    /**
     * confirm Delete PageRole
     * @param request The HTTP request
     * @return String The html code page
     */
    public String getRemovePageRole( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( PATH_JSP + JSP_REMOVE_ROLE + "?role=" + request.getParameter( PARAMETER_PAGE_ROLE ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Delete PageRole
     * @param request The HTTP request
     * @return String The url page
     */
    public String doRemovePageRole( HttpServletRequest request )
    {
        String strPageRole = (String) request.getParameter( PARAMETER_PAGE_ROLE );
        RoleHome.remove( strPageRole );

        return getHomeUrl( request );
    }
}
