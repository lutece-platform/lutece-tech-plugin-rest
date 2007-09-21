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
package fr.paris.lutece.plugins.ods.web.categoriedeliberation;

import fr.paris.lutece.plugins.ods.business.categoriedeliberation.CategorieDeliberation;
import fr.paris.lutece.plugins.ods.business.categoriedeliberation.CategorieDeliberationHome;
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
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Cette classe permet de gérer l'interface de gestion des catégories de délibération.
 */
public class CategorieDeliberationJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_CATEGORIE_DELIBERATION = "ODS_CATEGORIES";
    private static final String TEMPLATE_LISTE_CATEGORIES = "admin/plugins/ods/categoriedeliberation/liste_categorie_deliberation.html";
    private static final String TEMPLATE_CREATION_CATEGORIE = "admin/plugins/ods/categoriedeliberation/creation_categorie_deliberation.html";
    private static final String TEMPLATE_MODIFICATION_CATEGORIE = "admin/plugins/ods/categoriedeliberation/modification_categorie_deliberation.html";
    private static final String PROPERTY_PAGE_CREATION_CATEGORIE = "ods.categoriedeliberation.creation.pageTitle";
    private static final String PROPERTY_PAGE_MODIFICATION_CATEGORIE = "ods.categoriedeliberation.modification.pageTitle";
    private static final String PROPERTY_AFFICHER_AVERTISSEMENT_ALPACA = "odscategoriedeliberation.afficherAvertissement";
    private static final String MARK_LISTE_CATEGORIES = "liste_categories";
    private static final String MARK_CATEGORIE = "categorie";
    private static final String MARK_AVERTISSEMENT = "avertissement";
    private static final String MESSAGE_NFE_ON_CREATE_CODE = "ods.message.errorOnCreateCode";
    private static final String MESSAGE_CANNOT_CREATE_CATEGORIE = "ods.message.cannotCreateCategorieDeliberation";
    private static final String MESSAGE_NFE_ON_MODIFY_CODE = "ods.message.errorOnModifyCode";
    private static final String MESSAGE_CANNOT_MODIFY_CATEGORIE = "ods.message.cannotModifyCategorieDeliberation";
    private static final String MESSAGE_CONFIRM_DELETE_CATEGORIE = "ods.message.confirmDeleteCategorieDeliberation";
    private static final String MESSAGE_CANNOT_DELETE_CATEGORIE = "ods.message.cannotDeleteCategorieDeliberation";
    private static final int DEFAULT_AFFICHER_AVERTISSEMENT_ALPACA = 1;
    private static final String JSP_URL_DO_SUPPRESSION_CATEGORIE_JSP = "jsp/admin/plugins/ods/categoriedeliberation/DoSuppressionCategorieDeliberation.jsp";
    private static final String FIELD_CODE = "ods.creationcategoriedeliberation.label.code";
    private static final String FIELD_LIBELLE = "ods.creationcategoriedeliberation.label.libelle";

    /**
     * Retourne l'interface de gestion des catégories de délibération
     * @param request le requête HTTP
     * @return l'interface de gestion des catégories de délibération
     */
    public String getCategorieList( HttpServletRequest request )
    {
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        List<CategorieDeliberation> listCategorie = CategorieDeliberationHome.findCategorieDeliberationList( getPlugin(  ) );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( MARK_LISTE_CATEGORIES, listCategorie );

        ajouterPermissionsDansHashmap( model );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_CATEGORIES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Retourne l'interface de création d'une catégorie de délibération.
     * @param request le requête HTTP
     * @return l'interface de création d'une catégorie de délibération
     */
    public String getCreationCategorie( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_CREATION_CATEGORIE );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        HtmlTemplate template;

        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            model.put( MARK_AVERTISSEMENT,
                AppPropertiesService.getPropertyInt( PROPERTY_AFFICHER_AVERTISSEMENT_ALPACA,
                    DEFAULT_AFFICHER_AVERTISSEMENT_ALPACA ) );

            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_CATEGORIE, getLocale(  ), model );
        }
        else
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Crée une catégorie de délibération.
     * Si tous les champs obligatoires sont renseignés,
     * et si la valeur du code n'est pas déjà utilisée par une autre catégorie,
     * la méthode retourne l'url de gestion des catégories de délibération.
     * Sinon la méthode retourne l'url affichant les messages d'erreur
     * @param request la requête HTTP
     * @return url de la page de gestion si tous les champs sont renseignés et le code valide,<br>
     *  sinon un message d'erreur
     */
    public String doCreationCategorie( HttpServletRequest request )
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

            CategorieDeliberation categorie = new CategorieDeliberation(  );

            int nCode;

            try
            {
                nCode = Integer.parseInt( request.getParameter( OdsParameters.CODE_CATEGORIE ) );
            }
            catch ( NumberFormatException nfe )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_NFE_ON_CREATE_CODE, AdminMessage.TYPE_STOP );
            }

            if ( CategorieDeliberationHome.containsCode( null, OdsConstants.CONSTANTE_CHAINE_VIDE + nCode, getPlugin(  ) ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_CREATE_CATEGORIE,
                    AdminMessage.TYPE_STOP );
            }

            categorie.setCode( nCode );

            String strLibelle = request.getParameter( OdsParameters.INTITULE ).trim(  );
            categorie.setLibelle( strLibelle );

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

            categorie.setActif( bActif );

            CategorieDeliberationHome.create( categorie, getPlugin(  ) );

            return getHomeUrl( request );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * Retourne l'interface de modification d'une catégorie de délibération.
     * @param request le requête HTTP
     * @return l'interface de modification d'une catégorie de délibération
     */
    public String getModificationCategorie( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_MODIFICATION_CATEGORIE );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        HtmlTemplate template;

        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( request.getParameter( OdsParameters.ID_CATEGORIE ) == null )
            {
                return getCategorieList( request );
            }

            int nIdCategorie = -1;

            try
            {
                nIdCategorie = Integer.parseInt( request.getParameter( OdsParameters.ID_CATEGORIE ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            CategorieDeliberation categorie = CategorieDeliberationHome.findByPrimaryKey( nIdCategorie, getPlugin(  ) );

            model.put( MARK_CATEGORIE, categorie );
            model.put( OdsMarks.MARK_ACTIF, categorie.isActif(  ) );
            model.put( MARK_AVERTISSEMENT,
                AppPropertiesService.getPropertyInt( PROPERTY_AFFICHER_AVERTISSEMENT_ALPACA,
                    DEFAULT_AFFICHER_AVERTISSEMENT_ALPACA ) );

            template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_CATEGORIE, getLocale(  ), model );
        }
        else
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );
        }

        return ( getAdminPage( template.getHtml(  ) ) );
    }

    /**
     * Modifie une catégorie de délibération.
     * Si tous les champs obligatoires sont renseignés,
     * et si la valeur du code n'est pas déjà utilisée par une autre catégorie,
     * la méthode retourne l'url de gestion des catégories de délibération.
     * Sinon la méthode retourne l'url affichant les messages d'erreur
     * @param request la requête HTTP
     * @return une url
     */
    public String doModificationCategorie( HttpServletRequest request )
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

            int nIdCategorie = -1;

            try
            {
                nIdCategorie = Integer.parseInt( request.getParameter( OdsParameters.ID_CATEGORIE ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            CategorieDeliberation categorie = CategorieDeliberationHome.findByPrimaryKey( nIdCategorie, getPlugin(  ) );

            int nCode;

            try
            {
                nCode = Integer.parseInt( request.getParameter( OdsParameters.CODE_CATEGORIE ) );
            }
            catch ( NumberFormatException nfe )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_NFE_ON_MODIFY_CODE, AdminMessage.TYPE_STOP );
            }

            if ( CategorieDeliberationHome.containsCode( categorie, OdsConstants.CONSTANTE_CHAINE_VIDE + nCode,
                        getPlugin(  ) ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_MODIFY_CATEGORIE,
                    AdminMessage.TYPE_STOP );
            }

            categorie.setCode( nCode );

            String strLibelle = request.getParameter( OdsParameters.INTITULE ).trim(  );
            categorie.setLibelle( strLibelle );

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

            categorie.setActif( bActif );

            CategorieDeliberationHome.update( categorie, getPlugin(  ) );

            return getHomeUrl( request );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * Retourne l'interface de suppression d'une catégorie de délibération.
     * @param request le requête HTTP
     * @return l'interface de création d'une catégorie de délibération
     */
    public String getSuppressionCategorie( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            String strIdCategorie = request.getParameter( OdsParameters.ID_CATEGORIE );

            if ( strIdCategorie == null )
            {
                return getHomeUrl( request );
            }

            UrlItem url = new UrlItem( JSP_URL_DO_SUPPRESSION_CATEGORIE_JSP );
            url.addParameter( OdsParameters.ID_CATEGORIE, strIdCategorie );

            String[] strLibelle = new String[1];
            int nIdCategorie = -1;

            try
            {
                nIdCategorie = Integer.parseInt( strIdCategorie );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            strLibelle[0] = ( CategorieDeliberationHome.findByPrimaryKey( nIdCategorie, getPlugin(  ) ) ).getLibelle(  );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_CATEGORIE, strLibelle,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * Supprime une catégorie de délibération.
     * @param request la requête HTTP
     * @return url de gestion des catégories de délibération
     */
    public String doSuppressionCategorie( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( request.getParameter( OdsParameters.ID_CATEGORIE ) == null )
            {
                return getHomeUrl( request );
            }

            int nIdCategorie;

            try
            {
                nIdCategorie = Integer.parseInt( request.getParameter( OdsParameters.ID_CATEGORIE ) );
            }
            catch ( NumberFormatException nbe )
            {
                return getHomeUrl( request );
            }

            try
            {
                CategorieDeliberation categorie = CategorieDeliberationHome.findByPrimaryKey( nIdCategorie,
                        getPlugin(  ) );
                CategorieDeliberationHome.remove( categorie, getPlugin(  ) );
            }
            catch ( AppException ae )
            {
                if ( ae.getInitialException(  ) instanceof SQLException )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_CATEGORIE,
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
     * Indique si la requête est valide, c'est à dire que tous les champs obligatoires ont été remplis.
     * @param request la requête HTTP
     * @return true si la requête est valide, false sinon
     */
    private String requiredField( HttpServletRequest request )
    {
        if ( ( request.getParameter( OdsParameters.CODE_CATEGORIE ).equals( null ) ) ||
                ( request.getParameter( OdsParameters.CODE_CATEGORIE ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) )
        {
            return FIELD_CODE;
        }
        else if ( ( request.getParameter( OdsParameters.INTITULE ).equals( null ) ) ||
                ( request.getParameter( OdsParameters.INTITULE ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) )
        {
            return FIELD_LIBELLE;
        }

        return OdsConstants.CONSTANTE_CHAINE_VIDE;
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
