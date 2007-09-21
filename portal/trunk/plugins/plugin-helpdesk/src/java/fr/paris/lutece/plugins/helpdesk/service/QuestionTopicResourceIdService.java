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
package fr.paris.lutece.plugins.helpdesk.service;

import fr.paris.lutece.plugins.helpdesk.business.QuestionTopic;
import fr.paris.lutece.plugins.helpdesk.business.QuestionTopicHome;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

import java.util.Locale;


/**
 * Resource Id service for RBAC features to control access to portlet
 */
public class QuestionTopicResourceIdService extends ResourceIdService
{
    /** Permission for creating a question type */
    public static final String PERMISSION_CREATE = "CREATE";

    /** Permission for managing a question type */
    public static final String PERMISSION_MANAGE = "MANAGE";

    /** Permission for deleting a question type */
    public static final String PERMISSION_DELETE = "DELETE";

    /** Permission for modifying a question type */
    public static final String PERMISSION_MODIFY = "MODIFY";

    /** Permission for viewing a question type */
    public static final String PERMISSION_VIEW = "VIEW";
    private static final String PLUGIN_NAME = "helpdesk";
    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "helpdesk.resourceType2";
    private static final String PROPERTY_LABEL_CREATE = "helpdesk.permission.create";
    private static final String PROPERTY_LABEL_MANAGE = "helpdesk.permission.manage";
    private static final String PROPERTY_LABEL_MODIFY = "helpdesk.permission.modify";
    private static final String PROPERTY_LABEL_DELETE = "helpdesk.permission.delete";
    private static final String PROPERTY_LABEL_VIEW = "helpdesk.permission.view";

    /**
     * Initializes the service
     */
    public void register(  )
    {
        ResourceType rt = new ResourceType(  );
        rt.setResourceIdServiceClass( QuestionTopicResourceIdService.class.getName(  ) );
        rt.setResourceTypeKey( QuestionTopic.RESOURCE_TYPE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = new Permission(  );
        p.setPermissionKey( PERMISSION_CREATE );
        p.setPermissionTitleKey( PROPERTY_LABEL_CREATE );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_MANAGE );
        p.setPermissionTitleKey( PROPERTY_LABEL_MANAGE );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_DELETE );
        p.setPermissionTitleKey( PROPERTY_LABEL_DELETE );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_MODIFY );
        p.setPermissionTitleKey( PROPERTY_LABEL_MODIFY );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_VIEW );
        p.setPermissionTitleKey( PROPERTY_LABEL_VIEW );
        rt.registerPermission( p );

        ResourceTypeManager.registerResourceType( rt );
    }

    /**
     * Returns a list of resource ids
     * @param locale The current locale
     * @return A list of resource ids
     */
    public ReferenceList getResourceIdList( Locale locale )
    {
        return QuestionTopicHome.getQuestionTopicsList( PluginService.getPlugin( PLUGIN_NAME ) );
    }

    /**
     * Returns the Title of a given resource
     * @param strQuestionTopicId The Identifier of the resource
     * @param locale The current locale
     * @return The Title of a given resource
     */
    public String getTitle( String strQuestionTopicId, Locale locale )
    {
        QuestionTopic questionTopic = QuestionTopicHome.findByPrimaryKey( Integer.parseInt( strQuestionTopicId ),
                PluginService.getPlugin( PLUGIN_NAME ) );

        return questionTopic.getQuestionTopic(  );
    }
}
