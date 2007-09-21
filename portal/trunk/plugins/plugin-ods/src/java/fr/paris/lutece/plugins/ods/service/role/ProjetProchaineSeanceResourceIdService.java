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
package fr.paris.lutece.plugins.ods.service.role;

import fr.paris.lutece.portal.service.rbac.Permission;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.rbac.ResourceType;
import fr.paris.lutece.portal.service.rbac.ResourceTypeManager;
import fr.paris.lutece.util.ReferenceList;

import java.util.Locale;


/**
 * ProjetProchaineSeanceResourceIdService
 */
public class ProjetProchaineSeanceResourceIdService extends ResourceIdService
{
    public static final String PERMISSION_CREATION = "CREATION";
    public static final String PERMISSION_MAJ_COMPLETE = "MAJ_COMPLETE";
    public static final String PERMISSION_SUPPRESSION = "SUPPRESSION";
    public static final String PERMISSION_PUBLICATION = "PUBLICATION";
    public static final String PERMISSION_DEPUBLICATION = "DEPUBLICATION";
    public static final String PROJET_PROCHAINE_SEANCE = "PROJET_PROCHAINE_SEANCE";
    private static final String PROPERTY_LABEL_RESOURCE_TYPE = "ods.rbac.resourceType.label.projetProchaineSeance";
    private static final String PROPERTY_LABEL_CREATION = "ods.rbac.permission.label.creation";
    private static final String PROPERTY_LABEL_MAJ_COMPLETE = "ods.rbac.permission.label.majComplete";
    private static final String PROPERTY_LABEL_SUPPRESSION = "ods.rbac.permission.label.suppression";
    private static final String PROPERTY_LABEL_PUBLICATION = "ods.rbac.permission.label.publication";
    private static final String PROPERTY_LABEL_DEPUBLICATION = "ods.rbac.permission.label.depublication";
    private static final String PLUGIN_NAME = "ods";

    /** Crée une nouvelle instance de ProjetProchaineSeanceResourceIdService */
    public ProjetProchaineSeanceResourceIdService(  )
    {
        setPluginName( PLUGIN_NAME );
    }

    /**
     * Enregistre les ressources Lutece
     */
    public void register(  )
    {
        ResourceType rt = new ResourceType(  );
        rt.setResourceIdServiceClass( ProjetProchaineSeanceResourceIdService.class.getName(  ) );
        rt.setPluginName( PLUGIN_NAME );
        rt.setResourceTypeKey( PROJET_PROCHAINE_SEANCE );
        rt.setResourceTypeLabelKey( PROPERTY_LABEL_RESOURCE_TYPE );

        Permission p = new Permission(  );
        p.setPermissionKey( PERMISSION_CREATION );
        p.setPermissionTitleKey( PROPERTY_LABEL_CREATION );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_SUPPRESSION );
        p.setPermissionTitleKey( PROPERTY_LABEL_SUPPRESSION );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_PUBLICATION );
        p.setPermissionTitleKey( PROPERTY_LABEL_PUBLICATION );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_DEPUBLICATION );
        p.setPermissionTitleKey( PROPERTY_LABEL_DEPUBLICATION );
        rt.registerPermission( p );

        p = new Permission(  );
        p.setPermissionKey( PERMISSION_MAJ_COMPLETE );
        p.setPermissionTitleKey( PROPERTY_LABEL_MAJ_COMPLETE );
        rt.registerPermission( p );

        ResourceTypeManager.registerResourceType( rt );
    }

    /**
    * Returns a list of resource ids - return null in this case
    * @param locale The current locale
    * @return A list of resource ids
     */
    public ReferenceList getResourceIdList( Locale locale )
    {
        return null;
    }

    /**
    * Returns the Title of a given resource - return null in this case
    * @param strId The Id of the resource
    * @param locale The current locale
    * @return The Title of a given resource
     */
    public String getTitle( String strId, Locale locale )
    {
        return null;
    }
}
