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
package fr.paris.lutece.plugins.ods.web.direction;

import fr.paris.lutece.plugins.ods.business.direction.Direction;
import fr.paris.lutece.plugins.ods.business.direction.DirectionHome;
import fr.paris.lutece.plugins.ods.service.role.AdminSaufOdjResourceIdService;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.constants.OdsProperties;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Cette classe gère l'affichage de l'interface de gestion des directions.
 */
public class DirectionJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_DIRECTION = "ODS_DIRECTIONS";
    private static final String TEMPLATE_LISTE_DIRECTIONS = "admin/plugins/ods/direction/liste_directions.html";
    private static final String TEMPLATE_CREATION_DIRECTION = "admin/plugins/ods/direction/creation_direction.html";
    private static final String TEMPLATE_MODIFICATION_DIRECTION = "admin/plugins/ods/direction/modification_direction.html";
    private static final String PROPERTY_PAGE_CREATION_DIRECTION = "ods.direction.creation.pageTitle";
    private static final String PROPERTY_PAGE_MODIFICATION_DIRECTION = "ods.direction.modification.pageTitle";
    private static final String MARK_LISTE_DIRECTIONS = "liste_directions";
    private static final String MARK_DIRECTION = "direction";
    private static final String MESSAGE_CONFIRM_DELETE_DIRECTION = "ods.message.confirmDeleteDirection";
    private static final String MESSAGE_CANNOT_CREATE_DIRECTION = "ods.message.cannotCreateDirection";
    private static final String MESSAGE_NFE_ON_MODIFY = "ods.modificationdirection.label.nfeOnModify";
    private static final String MESSAGE_CANNOT_MODIFY_DIRECTION = "ods.message.cannotModifyDirection";
    private static final String MESSAGE_CANNOT_DELETE_DIRECTION = "ods.message.cannotDeleteDirection";
    private static final String JSP_URL_DO_SUPPRESSION_DIRECTION_JSP = "jsp/admin/plugins/ods/direction/DoSuppressionDirection.jsp";
    private static final String FIELD_CODE = "ods.creationdirection.label.code";
    private static final String FIELD_LIBELLE_COURT = "ods.creationdirection.label.libelle_court";
    private static final String FIELD_LIBELLE_LONG = "ods.creationdirection.label.libelle_long";

    /**
     * Retourne l'interface de création d'une direction.
     * @param request le requête HTTP
     * @return l'interface de création d'une direction
     */
    public String getCreationDirection( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_CREATION_DIRECTION );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        HtmlTemplate template;

        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_DIRECTION, getLocale(  ) );
        }
        else
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Crée une direction.
     * Si tous les champs obligatoires sont renseignés,
     * et si la valeur du code n'est pas déjà utilisée par une autre direction,
     * la méthode retourne l'url de gestion des directions.
     * Sinon la méthode retourne l'url affichant les messages d'erreur
     * @param request la requête HTTP
     * @return une url
     */
    public String doCreationDirection( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( !requiredField( request ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                Object[] tabRequiredFields = { I18nService.getLocalizedString( requiredField( request ), getLocale(  ) ) };

                return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                    tabRequiredFields, AdminMessage.TYPE_STOP );
            }

            String strCode = request.getParameter( OdsParameters.CODE_DIRECTION ).trim(  );
            String strLibelleCourt = request.getParameter( OdsParameters.INTITULE_COURT ).trim(  );
            String strLibelleLong = request.getParameter( OdsParameters.INTITULE_LONG ).trim(  );
            String strActif = request.getParameter( OdsParameters.ACTIF );
            boolean bActif;

            if ( null == strActif )
            {
                bActif = false;
            }
            else
            {
                bActif = true;
            }

            Direction direction = new Direction(  );

            if ( DirectionHome.containsCode( null, strCode, getPlugin(  ) ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_CREATE_DIRECTION,
                    AdminMessage.TYPE_STOP );
            }

            direction.setCode( strCode );
            direction.setLibelleCourt( strLibelleCourt );
            direction.setLibelleLong( strLibelleLong );
            direction.setActif( bActif );
            DirectionHome.create( direction, getPlugin(  ) );

            return getHomeUrl( request );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * Retourne l'interface de modification d'une direction.
     * @param request le requête HTTP
     * @return l'interface de modification d'une direction
     */
    public String getModificationDirection( HttpServletRequest request )
    {
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        HtmlTemplate template;

        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( request.getParameter( OdsParameters.ID_DIRECTION ) == null )
            {
                return getDirectionList( request );
            }

            setPageTitleProperty( PROPERTY_PAGE_MODIFICATION_DIRECTION );

            int nIdDirection = -1;

            try
            {
                nIdDirection = Integer.parseInt( request.getParameter( OdsParameters.ID_DIRECTION ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            Direction direction = DirectionHome.findByPrimaryKey( nIdDirection, getPlugin(  ) );

            model.put( MARK_DIRECTION, direction );
            model.put( OdsMarks.MARK_ACTIF, direction.isActif(  ) );

            template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_DIRECTION, getLocale(  ), model );
        }
        else
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );
        }

        return ( getAdminPage( template.getHtml(  ) ) );
    }

    /**
     * Modifie une direction.
     * Si tous les champs obligatoires sont renseignés,
     * et si la valeur du code n'est pas déjà utilisée par une autre direction,
     * la méthode retourne l'url de gestion des directions.
     * Sinon la méthode retourne l'url affichant les messages d'erreur
     * @param request la requête HTTP
     * @return une url
     */
    public String doModificationDirection( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( !requiredField( request ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                Object[] tabRequiredFields = { I18nService.getLocalizedString( requiredField( request ), getLocale(  ) ) };

                return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                    tabRequiredFields, AdminMessage.TYPE_STOP );
            }

            int nIdDirection = -1;

            try
            {
                nIdDirection = Integer.parseInt( request.getParameter( OdsParameters.ID_DIRECTION ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            Direction direction = DirectionHome.findByPrimaryKey( nIdDirection, getPlugin(  ) );

            String strCode = request.getParameter( OdsParameters.CODE_DIRECTION ).trim(  );
            String strLibelleCourt = request.getParameter( OdsParameters.INTITULE_COURT ).trim(  );
            String strLibelleLong = request.getParameter( OdsParameters.INTITULE_LONG ).trim(  );
            String strActif = request.getParameter( OdsParameters.ACTIF );
            boolean bActif;

            if ( null == strActif )
            {
                bActif = false;
            }
            else
            {
                bActif = true;
            }

            if ( DirectionHome.containsCode( direction, strCode, getPlugin(  ) ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_MODIFY_DIRECTION,
                    AdminMessage.TYPE_STOP );
            }

            direction.setCode( strCode );
            direction.setLibelleCourt( strLibelleCourt );
            direction.setLibelleLong( strLibelleLong );
            direction.setActif( bActif );
            DirectionHome.update( direction, getPlugin(  ) );

            return getHomeUrl( request );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * Retourne l'interface de suppression d'une direction.
     * @param request la requête HTTP
     * @return l'interface de création d'une direction
     */
    public String getSuppressionDirection( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( request.getParameter( OdsParameters.ID_DIRECTION ) == null )
            {
                return getHomeUrl( request );
            }

            String strIdDirection = request.getParameter( OdsParameters.ID_DIRECTION );
            UrlItem url = new UrlItem( JSP_URL_DO_SUPPRESSION_DIRECTION_JSP );
            url.addParameter( OdsParameters.ID_DIRECTION, strIdDirection );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_DIRECTION, url.getUrl(  ),
                AdminMessage.TYPE_CONFIRMATION );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * Supprime une direction.
     * @param request la requête HTTP
     * @return url de gestion des directions
     */
    public String doSuppressionDirection( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( null == request.getParameter( OdsParameters.ID_DIRECTION ) )
            {
                return getHomeUrl( request );
            }

            int nIdDirection;

            try
            {
                nIdDirection = Integer.parseInt( request.getParameter( OdsParameters.ID_DIRECTION ) );
            }
            catch ( NumberFormatException ne )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_NFE_ON_MODIFY, AdminMessage.TYPE_STOP );
            }

            Direction direction = DirectionHome.findByPrimaryKey( nIdDirection, getPlugin(  ) );

            try
            {
                DirectionHome.remove( direction, getPlugin(  ) );
            }
            catch ( AppException ae )
            {
                if ( ae.getInitialException(  ) instanceof SQLException )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_DIRECTION,
                        AdminMessage.TYPE_STOP );
                }
            }

            return getHomeUrl( request );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * Retourne l'interface de gestion des directions.
     * @param request le requête HTTP
     * @return l'interface de gestion des directions
     */
    public String getDirectionList( HttpServletRequest request )
    {
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        List<Direction> listDirection = DirectionHome.findDirectionList( getPlugin(  ) );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( MARK_LISTE_DIRECTIONS, listDirection );

        ajouterPermissionsDansHashmap( model );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_DIRECTIONS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
    * Indique si la requête est valide, c'est à dire que tous les champs obligatoires ont été remplis.
    * @param request la requête HTTP
    * @return true si la requête est valide, false sinon
    */
    private String requiredField( HttpServletRequest request )
    {
        String strRequiredField = OdsConstants.CONSTANTE_CHAINE_VIDE;

        if ( ( request.getParameter( OdsParameters.CODE_DIRECTION ).equals( null ) ) ||
                ( request.getParameter( OdsParameters.CODE_DIRECTION ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) )
        {
            strRequiredField = FIELD_CODE;
        }
        else if ( ( request.getParameter( OdsParameters.INTITULE_COURT ).equals( null ) ) ||
                ( request.getParameter( OdsParameters.INTITULE_COURT ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) )
        {
            strRequiredField = FIELD_LIBELLE_COURT;
        }
        else if ( ( request.getParameter( OdsParameters.INTITULE_LONG ).equals( null ) ) ||
                ( request.getParameter( OdsParameters.INTITULE_LONG ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) )
        {
            strRequiredField = FIELD_LIBELLE_LONG;
        }

        return strRequiredField;
    }

    /**
     * Permet de stocker toutes les permissions afin de gérer les profils au niveau des templates
     * @param model le hashmap contenant les parametres qui vont être envoyés au template
     */
    private void ajouterPermissionsDansHashmap( Map<Object, Object> model )
    {
        /*
         * Permission de gérer
         */
        boolean bPermissionGestion = true;

        if ( !RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            bPermissionGestion = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_GESTION, bPermissionGestion );
    }
}
