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
package fr.paris.lutece.plugins.ods.web.groupepolitique;

import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitique;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitiqueHome;
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
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * gestion des groupes politiques
 *
 */
public class GroupePolitiqueJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_GROUPE_POLITIQUE = "ODS_GROUPES_POLITIQUES";
    private static final String TEMPLATE_LISTE_GROUPES = "admin/plugins/ods/groupepolitique/liste_groupes.html";
    private static final String TEMPLATE_CREATION_GROUPE = "admin/plugins/ods/groupepolitique/creation_groupe.html";
    private static final String TEMPLATE_MODIFICATION_GROUPE = "admin/plugins/ods/groupepolitique/modification_groupe.html";
    private static final String PROPERTY_PAGE_MODIFICATION_GROUPE = "ods.groupe.modification.pageTitle";
    private static final String PROPERTY_PAGE_CREATION_GROUPE = "ods.groupe.creation.pageTitle";
    private static final String MARK_LISTE_GROUPES = "liste_groupes";
    private static final String MARK_LISTE_GROUPES_COUNT = "liste_groupes_count";
    private static final String MARK_GROUPE = "groupe";
    private static final String MESSAGE_CONFIRMDELETEGROUPE = "ods.message.confirmDeleteGroupe";
    private static final String MESSAGE_CANNOT_DELETE_GROUPE = "ods.message.cannotDeleteGroupe";
    private static final String JSP_URL_DO_SUPPRESSION_GROUPE_JSP = "jsp/admin/plugins/ods/groupepolitique/DoSuppressionGroupe.jsp";
    private static final String FIELD_NOM_COMPLET = "ods.creationgroupe.label.nom_complet";
    private static final String FIELD_NOM_GROUPE = "ods.creationgroupe.label.nom_groupe";

    /**
     * retourne le formulaire de création d'un groupe politique
     * @param request la requete HTTP
     * @return formulaire de création d'un groupe politique
     */
    public String getCreationGroupe( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_CREATION_GROUPE );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        HtmlTemplate template;

        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_GROUPE, getLocale(  ) );
        }
        else
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * crée un groupe politique.
     * si tous les champs obligatoires du formulaire de création d'un groupe politique ont été renseigné,
     * la méthode retourne l'url de gestion des groupes poliques,
     * sinon l'url retournée est celle de l'interface spécifiant les messages d'erreurs.
     * @param request request
     * @return url url de retour
     */
    public String doCreationGroupe( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            String strChamp = requiredField( request );

            if ( !strChamp.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                Object[] message = { I18nService.getLocalizedString( strChamp, getLocale(  ) ) };

                return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                    message, AdminMessage.TYPE_STOP );
            }

            GroupePolitique groupe = new GroupePolitique(  );

            String strNomCourt = request.getParameter( OdsParameters.NOM_GROUPE ).trim(  );
            groupe.setNomGroupe( strNomCourt );

            String strNomLong = request.getParameter( OdsParameters.NOM_COMPLET ).trim(  );
            groupe.setNomComplet( strNomLong );

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

            groupe.setActif( bActif );

            GroupePolitiqueHome.create( groupe, getPlugin(  ) );

            return getHomeUrl( request );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * retourne le formulaire de modification d'un groupe politique  pré remplis avec  les critères  du groupe politique choisi
     * @param request la requete HTTP
     * @return formulaire de modification d'un groupe politique
     */
    public String getModificationGroupe( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_MODIFICATION_GROUPE );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        HtmlTemplate template;

        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( request.getParameter( OdsParameters.ID_GROUPE ) == null )
            {
                return getGroupeList( request );
            }

            int nIdGroupePolitique = -1;

            try
            {
                nIdGroupePolitique = Integer.parseInt( request.getParameter( OdsParameters.ID_GROUPE ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            GroupePolitique groupe = GroupePolitiqueHome.findByPrimaryKey( nIdGroupePolitique, getPlugin(  ) );

            model.put( MARK_GROUPE, groupe );
            model.put( OdsMarks.MARK_ACTIF, groupe.isActif(  ) );

            template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_GROUPE, getLocale(  ), model );
        }
        else
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Modifie un groupe politique.
     * si tous les champs obligatoires du formulaire de modification d'un groupe politique ont été renseigné,
     * la méthode retourne l'url de gestion des groupes poliques,
     * sinon l'url retournée est celle de l'interface spécifiant les messages d'erreurs.
     * @param request request
     * @return url url de retour
     */
    public String doModificationGroupe( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            String strChamp = requiredField( request );

            if ( !strChamp.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                Object[] message = { I18nService.getLocalizedString( strChamp, getLocale(  ) ) };

                return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                    message, AdminMessage.TYPE_STOP );
            }

            GroupePolitique groupe;
            int nIdGroupe;

            try
            {
                nIdGroupe = Integer.parseInt( request.getParameter( OdsParameters.ID_GROUPE ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            groupe = GroupePolitiqueHome.findByPrimaryKey( nIdGroupe, getPlugin(  ) );

            String strNomGroupe = request.getParameter( OdsParameters.NOM_GROUPE ).trim(  );
            groupe.setNomGroupe( strNomGroupe );

            String strNomComplet = request.getParameter( OdsParameters.NOM_COMPLET ).trim(  );
            groupe.setNomComplet( strNomComplet );

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

            groupe.setActif( bActif );

            GroupePolitiqueHome.update( groupe, getPlugin(  ) );

            return getHomeUrl( request );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * retourne une demande de confirmation pour la suppression du  groupe politique  choisi
     * @param request la requete HTTP
     * @return interface de confirmation de suppression
     */
    public String getSuppressionGroupe( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( request.getParameter( OdsParameters.ID_GROUPE ) == null )
            {
                return getHomeUrl( request );
            }

            String strIdGroupe = request.getParameter( OdsParameters.ID_GROUPE );
            UrlItem url = new UrlItem( JSP_URL_DO_SUPPRESSION_GROUPE_JSP );
            url.addParameter( OdsParameters.ID_GROUPE, strIdGroupe );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRMDELETEGROUPE, url.getUrl(  ),
                AdminMessage.TYPE_CONFIRMATION );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * supprime le groupe politique choisi
     * @param request la requete HTTP
     * @return url de gestion des groupes poliques
     */
    public String doSuppressionGroupe( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( request.getParameter( OdsParameters.ID_GROUPE ) == null )
            {
                return getHomeUrl( request );
            }

            int nIdGroupe;

            try
            {
                nIdGroupe = Integer.parseInt( request.getParameter( OdsParameters.ID_GROUPE ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getHomeUrl( request );
            }

            GroupePolitique groupe = GroupePolitiqueHome.findByPrimaryKey( nIdGroupe, getPlugin(  ) );

            try
            {
                GroupePolitiqueHome.remove( groupe, getPlugin(  ) );
            }
            catch ( AppException ae )
            {
                AppLogService.error( ae );

                if ( ae.getInitialException(  ) instanceof SQLException )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_GROUPE,
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
     * retourne l'interface de gestion des groupes politiques.
     * @param request la requete HTTP
     * @return   interface de gestion des groupes politiques
     */
    public String getGroupeList( HttpServletRequest request )
    {
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        List<GroupePolitique> groupes = GroupePolitiqueHome.findGroupePolitiqueList( getPlugin(  ) );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( MARK_LISTE_GROUPES, groupes );
        model.put( MARK_LISTE_GROUPES_COUNT, groupes.size(  ) );

        ajouterPermissionsDansHashmap( model );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_GROUPES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * methode qui test si  dans le formulaire de saisi d'un groupe politique  ,l'ensemble des critéres obligatoires ont été renseigné
     * @param request  la requete HTTP
     * @return l'ensemble des critéres obligatoires qui n'ont pas été renseigné
     */
    private String requiredField( HttpServletRequest request )
    {
        String strField = OdsConstants.CONSTANTE_CHAINE_VIDE;

        if ( ( null == request.getParameter( OdsParameters.NOM_GROUPE ) ) ||
                request.getParameter( OdsParameters.NOM_GROUPE ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            strField = FIELD_NOM_GROUPE;
        }
        else if ( ( null == request.getParameter( OdsParameters.NOM_COMPLET ) ) ||
                request.getParameter( OdsParameters.NOM_COMPLET ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            strField = FIELD_NOM_COMPLET;
        }

        return strField;
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
