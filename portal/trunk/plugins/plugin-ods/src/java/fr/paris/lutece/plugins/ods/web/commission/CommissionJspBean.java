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
package fr.paris.lutece.plugins.ods.web.commission;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.commission.CommissionHome;
import fr.paris.lutece.plugins.ods.service.role.AdminSaufOdjResourceIdService;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.constants.OdsProperties;
import fr.paris.lutece.portal.business.rbac.RBAC;
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
 * PluginAdminPageJspBean
 */
public class CommissionJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_COMMISSION = "ODS_COMMISSIONS";
    private static final String TEMPLATE_LISTE_COMMISSIONS = "admin/plugins/ods/commission/commissions.html";
    private static final String TEMPLATE_CREATION_COMMISSION = "admin/plugins/ods/commission/creation_commission.html";
    private static final String TEMPLATE_MODIFICATION_COMMISSION = "admin/plugins/ods/commission/modification_commission.html";
    private static final String PROPERTY_PAGE_CREATION_COMMISSION = "ods.commission.creation.pageTitle";
    private static final String PROPERTY_PAGE_MODIFICATION_COMMISSION = "ods.commission.modification.pageTitle";
    private static final String PROPERTY_URL_DO_SUPPRESSION_COMMISSION_JSP = "jsp/admin/plugins/ods/commission/DoSuppressionCommission.jsp";
    private static final String MARK_LISTE_COMMISSIONS = "liste_commissions";
    private static final String MARK_COMMISSION = "commission";
    private static final String MESSAGE_INVALID_COMMISSION_NUMERO = "ods.message.commission.alreadyexist";
    private static final String MESSAGE_NUMERO_INVALID = "ods.message.commission.errornumero";
    private static final String MESSAGE_NUMERO_ABSENT = "ods.message.commission.numeroabsent";
    private static final String MESSAGE_CANNOT_DELETE_COMMISSION = "ods.message.cannotDeleteCommission";
    private static final String MESSAGE_CONFIRM_DELETE_COMMISSION = "ods.message.confirmDeleteCommission";

    /**
     * retourne le formulaire de création d'une commission
     * @param request la requete HTTP
     * @return formulaire de création d'une séance
     */
    public String getCreationCommission( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_CREATION_COMMISSION );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        HtmlTemplate template;

        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_COMMISSION, getLocale(  ), model );
        }
        else
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * crée une commission
     * si tous les champs obligatoires du formulaire de création d'une commission ont été renseigné,<BR>
     * la méthode retourne l'url de gestion des commissions,<BR>
     * sinon l'url retournée est celle de l'interface spécifiant les messages d'erreurs.
     * @param request la requête
     * @return url
     */
    public String doCreationCommission( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( isValideForme( request ) )
            {
                String strNumero = request.getParameter( OdsParameters.NUMERO_COMMISSION ).trim(  );
                String strDescription = request.getParameter( OdsParameters.DESCRIPTION_COMMISSION ).trim(  );
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

                String strRetour = null;

                if ( strNumero.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    strRetour = AdminMessageService.getMessageUrl( request, MESSAGE_NUMERO_ABSENT,
                            AdminMessage.TYPE_STOP );
                }
                else
                {
                    int nNumero = -1;

                    try
                    {
                        nNumero = Integer.parseInt( strNumero );
                    }
                    catch ( NumberFormatException nfe )
                    {
                        strRetour = AdminMessageService.getMessageUrl( request, MESSAGE_NUMERO_INVALID,
                                AdminMessage.TYPE_STOP );
                    }

                    if ( ( strRetour == null ) &&
                            ( CommissionHome.findByNumeroCommission( nNumero, getPlugin(  ) ) != null ) )
                    {
                        strRetour = AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_COMMISSION_NUMERO,
                                AdminMessage.TYPE_STOP );
                    }

                    if ( ( strRetour == null ) && ( nNumero < 1 ) )
                    {
                        strRetour = AdminMessageService.getMessageUrl( request, MESSAGE_NUMERO_INVALID,
                                AdminMessage.TYPE_STOP );
                    }

                    if ( strRetour == null )
                    {
                        Commission commission = new Commission(  );
                        commission.setNumero( nNumero );
                        commission.setLibelle( strDescription );
                        commission.setActif( bActif );
                        CommissionHome.create( commission, getPlugin(  ) );

                        strRetour = getHomeUrl( request );
                    }
                }

                return strRetour;
            }
            else
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_NUMERO_ABSENT, AdminMessage.TYPE_STOP );
            }
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * retourne le formulaire de modification d'une commission pré remplis avec  les critères de la commission choisi
     * @param request la requête
     * @return la page affichant les détails à modifier pour une comission
     */
    public String getModificationCommission( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_MODIFICATION_COMMISSION );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        HtmlTemplate template;

        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            String strIdCommission = request.getParameter( OdsParameters.ID_COMMISSION );

            if ( strIdCommission != null )
            {
                int nIdCommission = -1;

                try
                {
                    nIdCommission = Integer.parseInt( strIdCommission );
                }
                catch ( NumberFormatException nfe )
                {
                    AppLogService.error( nfe );
                }

                Commission commission = CommissionHome.findByPrimaryKey( nIdCommission, getPlugin(  ) );

                model.put( MARK_COMMISSION, commission );
                model.put( OdsMarks.MARK_ACTIF, commission.isActif(  ) );

                template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_COMMISSION, getLocale(  ), model );
            }
            else
            {
                return getCommissionList( request );
            }
        }
        else
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Modifie une commission
     * si tous les champs obligatoires du formulaire de modification d'une commission ont été renseigné,<BR>
     * la méthode retourne l'url de gestion des commissions,<BR>
     * sinon l'url retournée est celle de l'interface spécifiant les messages d'erreurs.
     * @param request la requête
     * @return l'url de le page à charger après modification de la comission
     */
    public String doModificationCommission( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            boolean bInformationManquante = false;

            if ( isValideForme( request ) )
            {
                String strIdCommission = request.getParameter( OdsParameters.ID_COMMISSION ).trim(  );
                String strNumero = request.getParameter( OdsParameters.NUMERO_COMMISSION ).trim(  );
                String strDescription = request.getParameter( OdsParameters.DESCRIPTION_COMMISSION ).trim(  );
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

                if ( strNumero.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    bInformationManquante = true;
                }
                else
                {
                    try
                    {
                        int nNumeroCommission = -1;
                        int nIdCommission = -1;

                        try
                        {
                            nNumeroCommission = Integer.parseInt( strNumero );
                            nIdCommission = Integer.parseInt( strIdCommission );
                        }
                        catch ( NumberFormatException nfe )
                        {
                            AppLogService.error( nfe );
                        }

                        Commission tmpCommission = CommissionHome.findByNumeroCommission( nNumeroCommission,
                                getPlugin(  ) );

                        if ( ( tmpCommission != null ) && ( tmpCommission.getIdCommission(  ) != nIdCommission ) )
                        {
                            return AdminMessageService.getMessageUrl( request, MESSAGE_INVALID_COMMISSION_NUMERO,
                                AdminMessage.TYPE_STOP );
                        }

                        Commission commission = new Commission(  );
                        commission.setIdCommission( nIdCommission );
                        commission.setNumero( nNumeroCommission );
                        commission.setLibelle( strDescription );
                        commission.setActif( bActif );
                        CommissionHome.update( commission, getPlugin(  ) );
                    }
                    catch ( NumberFormatException nfe )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_NUMERO_INVALID,
                            AdminMessage.TYPE_STOP );
                    }
                }
            }
            else
            {
                bInformationManquante = true;
            }

            if ( bInformationManquante )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
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
     * retourne une demande de confirmation pour la suppression de la commission choisie
     * @param request la requête
     * @return affiche le message de confirmation de suppression d'une comission
     */
    public String getSuppressionCommission( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( request.getParameter( OdsParameters.ID_COMMISSION ) != null )
            {
                String strIdCommission = request.getParameter( OdsParameters.ID_COMMISSION );
                UrlItem url = new UrlItem( PROPERTY_URL_DO_SUPPRESSION_COMMISSION_JSP );
                url.addParameter( OdsParameters.ID_COMMISSION, strIdCommission );

                return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_COMMISSION, url.getUrl(  ),
                    AdminMessage.TYPE_CONFIRMATION );
            }
            else
            {
                return getHomeUrl( request );
            }
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * supprime la commission choisie
     * @param request la requête
     * @return l'url de la page à charger après confirmation de la suppression de la comission
     */
    public String doSuppressionCommission( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            String strIdCommission = request.getParameter( OdsParameters.ID_COMMISSION );

            if ( strIdCommission != null )
            {
                int nIdCommission;

                try
                {
                    nIdCommission = Integer.parseInt( strIdCommission );
                }
                catch ( NumberFormatException nfe )
                {
                    return getHomeUrl( request );
                }

                try
                {
                    Commission commission = CommissionHome.findByPrimaryKey( nIdCommission, getPlugin(  ) );
                    CommissionHome.remove( commission, getPlugin(  ) );
                }
                catch ( AppException ae )
                {
                    if ( ae.getInitialException(  ) instanceof SQLException )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_COMMISSION,
                            AdminMessage.TYPE_STOP );
                    }
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
     * retourne l'interface de gestion des commissions
     * @param request la requête
     * @return la page affichant la liste des commissions
     */
    public String getCommissionList( HttpServletRequest request )
    {
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        List<Commission> commissions = CommissionHome.findCommissionList( getPlugin(  ) );
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( MARK_LISTE_COMMISSIONS, commissions );

        ajouterPermissionsDansHashmap( model );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_COMMISSIONS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * methode qui test si dans le formulaire de saisi d'une commission,l'ensemble des critéres obligatoires ont été renseigné
     * @param request la requête
     * @return VRAI si la requête est valide pour être traitée
     */
    private boolean isValideForme( HttpServletRequest request )
    {
        if ( null == request.getParameter( OdsParameters.NUMERO_COMMISSION ) )
        {
            return false;
        }

        return true;
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
