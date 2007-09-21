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
package fr.paris.lutece.plugins.ods.web.seance;

import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.service.role.AdminSaufOdjResourceIdService;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.constants.OdsProperties;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * Manage la fonctionnalité de la Seance
 *
 */
public class SeanceJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_SEANCE = "ODS_SEANCES";
    private static final String TEMPLATE_LISTE_SEANCES = "admin/plugins/ods/seance/seances.html";
    private static final String TEMPLATE_MODIFICATION_SEANCE = "admin/plugins/ods/seance/modification_seance.html";
    private static final String TEMPLATE_CREATION_SEANCE = "admin/plugins/ods/seance/creation_seance.html";
    private static final String TEMPLATE_JSP_CREATION_SEANCE = "jsp/admin/plugins/ods/seance/CreationSeance.jsp";
    private static final String PROPERTY_PAGE_CREATION_SEANCE = "ods.seance.creation.pageTitle";
    private static final String PROPERTY_PAGE_MODIFICATION_SEANCE = "ods.seance.modification.pageTitle";
    private static final String PROPERTY_ILLOGICAL_DATE = "ods.seance.illogical.date";
    private static final String PROPERTY_URL_DO_SUPPRESSION_SEANCE_JSP = "jsp/admin/plugins/ods/seance/DoSuppressionSeance.jsp";
    private static final String PROPERTY_LABEL_DATE_DEBUT = "ods.listeseances.label.start";
    private static final String PROPERTY_LABEL_DATE_FIN = "ods.listeseances.label.end";
    private static final String MARK_LISTE_SEANCES = "liste_seances";
    private static final String MARK_SEANCE_CREATING_OR_UPDATING = "seanceBean";
    private static final String MARK_FILTER_ERROR = "filter_error";
    private static final String MESSAGE_CONFIRM_DELETE_SEANCE = "ods.message.confirmDeleteSuppression";
    private static final String MESSAGE_CANNOT_DELETE_SEANCE = "ods.message.cannotDeleteSeance";
    private static final String MESSAGE_CANNOT_CREATE_SEANCE = "ods.message.cannotCreateSeance";
    private Seance _seanceBean;

    /**
     * retourne le formulaire de création d'une séance
     * @param request la requete HTTP
     * @return Template avec le formulaire de création d'un séance
     */
    public String getCreateSeance( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_CREATION_SEANCE );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        HtmlTemplate template;

        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( _seanceBean == null )
            {
                _seanceBean = new Seance(  );
            }

            model.put( MARK_SEANCE_CREATING_OR_UPDATING, _seanceBean );

            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_SEANCE, getLocale(  ), model );
        }
        else
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * méthode qui récupère les valeurs du formulaire de création ou de modification d'une séance<BR>
     * et affecte ces valeurs à l'objet séance stocké en session.
     * @param request La requete HTTP
     * @return int 1 si la date de debut ou 2 pour la date de fin (champs non correct) et 0 si tout est OK
     */
    private int getParamaterWhenCreatingOrUpdatingSeance( HttpServletRequest request )
    {
        // Récuperation des paramètres du formulaire
        String strSeanceBegin = request.getParameter( OdsParameters.SEANCE_BEGIN );
        String strSeanceEnd = request.getParameter( OdsParameters.SEANCE_END );
        String strSeanceDateRemiseCrSommaire = request.getParameter( OdsParameters.SEANCE_DATE_REMISE_CR_SOMMAIRE );
        String strSeanceDateDepotQuestions = request.getParameter( OdsParameters.SEANCE_DATE_DEPOT_QUESTION );
        String strSeanceDateLimiteDiffQuestions = request.getParameter( OdsParameters.SEANCE_DATE_LIMITE_DIFF_QUESTIONS );
        String strSeanceDateLimiteDiffPdd = request.getParameter( OdsParameters.SEANCE_DATE_LIMITE_DIFF_PDD );
        String strSeanceDateLimiteDiffDsp = request.getParameter( OdsParameters.SEANCE_DATE_LIMITE_DIFF_DSP );
        String strSeanceDateConference = request.getParameter( OdsParameters.SEANCE_DATE_CONFERENCE );
        String strSeanceDateReunionPostCommission = request.getParameter( OdsParameters.SEANCE_DATE_REUNION_POST_COMMISSION );
        String strSeanceDateReunion1 = request.getParameter( OdsParameters.SEANCE_DATE_REUNION_1 );
        String strSeanceDateReunion2 = request.getParameter( OdsParameters.SEANCE_DATE_REUNION_2 );
        String strSeanceDateReunion3 = request.getParameter( OdsParameters.SEANCE_DATE_REUNION_3 );

        // Positionne les proprietes de la seance qui est en train d'être crée
        _seanceBean.setDateSeance( OdsUtils.getDate( strSeanceBegin, true ) );
        _seanceBean.setDateCloture( OdsUtils.getDate( strSeanceEnd, false ) );

        if ( _seanceBean.getDateSeance(  ) == null )
        {
            return 1;
        }

        if ( _seanceBean.getDateCloture(  ) == null )
        {
            return 2;
        }

        _seanceBean.setDateRemiseCr( OdsUtils.getDate( strSeanceDateRemiseCrSommaire, false ) );
        _seanceBean.setDateDepotQuestionsOrales( OdsUtils.getDate( strSeanceDateDepotQuestions, false ) );
        _seanceBean.setDateLimiteDiffQuestions( OdsUtils.getDate( strSeanceDateLimiteDiffQuestions, false ) );
        _seanceBean.setDateLimiteDiffPdd( OdsUtils.getDate( strSeanceDateLimiteDiffPdd, false ) );
        _seanceBean.setDateLimiteDiffDsp( OdsUtils.getDate( strSeanceDateLimiteDiffDsp, false ) );
        _seanceBean.setDateConfOrg( OdsUtils.getDate( strSeanceDateConference, false ) );
        _seanceBean.setDateReuPostComm( OdsUtils.getDate( strSeanceDateReunionPostCommission, false ) );
        _seanceBean.setDateReu1( OdsUtils.getDate( strSeanceDateReunion1, false ) );
        _seanceBean.setDateReu2( OdsUtils.getDate( strSeanceDateReunion2, false ) );
        _seanceBean.setDateReu3( OdsUtils.getDate( strSeanceDateReunion3, false ) );

        return 0;
    }

    /**
     * crée une séance
     * si tous les champs obligatoires du formulaire de création d'une séance ont été renseigné,<BR>
     * la méthode retourne l'url de gestion des séances,<BR>
     * sinon l'url retournée est celle de l'interface spécifiant les messages d'erreurs.
     * @param request La requete HTTP
     * @return String template liste des seances
     */
    public String doCreateAndRegisterSeance( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            List<String> champsNonSaisie = new ArrayList<String>(  );
            int result = isValidForm( champsNonSaisie, request );

            String strDateBegin = request.getParameter( OdsParameters.SEANCE_BEGIN );
            String strDateCloture = request.getParameter( OdsParameters.SEANCE_END );

            if ( result == 0 )
            {
                if ( ( ( strDateBegin != null ) && ( strDateCloture != null ) && !strDateBegin.equals( strDateCloture ) &&
                        !OdsUtils.getDate( strDateBegin, true ).before( OdsUtils.getDate( strDateCloture, false ) ) ) ||
                        ( ( strDateCloture != null ) &&
                                !OdsUtils.getDate( request.getParameter( OdsParameters.SEANCE_BEGIN ), false )
                                             .after( OdsUtils.getCurrentDate(  ) ) ) )
                {
                    return AdminMessageService.getMessageUrl( request, PROPERTY_ILLOGICAL_DATE, AdminMessage.TYPE_STOP );
                }

                result = getParamaterWhenCreatingOrUpdatingSeance( request );

                if ( result == 0 )
                {
                    SeanceHome.create( _seanceBean, getPlugin(  ) );
                    _seanceBean = new Seance(  );
                }
                else
                {
                    Object[] messagesArgs = { I18nService.getLocalizedString( champsNonSaisie.get( 0 ), getLocale(  ) ) };

                    return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_CREATE_SEANCE, messagesArgs,
                        AdminMessage.TYPE_STOP );
                }

                return getHomeUrl( request );
            }
            else
            {
                Object[] messagesArgs = { I18nService.getLocalizedString( champsNonSaisie.get( 0 ), getLocale(  ) ) };

                return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_CREATE_SEANCE, messagesArgs,
                    AdminMessage.TYPE_STOP );
            }
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * réinitialise l'objet seance stocké en session
     * @param request La requete HTTP
     * @return String template liste des seances
     */
    public String doResetCreatingSeance( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            _seanceBean = new Seance(  );

            return getHomeUrl( request );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * Permet de calculer les dates automatiquement en fonction de la date de début et de fin de séance
     * Ne créer pas la séance.
     * @param request La requete HTTP
     * @return String template de creation de seance
     */
    public String doCreateSeance( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            List<String> champsNonSaisie = new ArrayList<String>(  );
            int result = isValidForm( champsNonSaisie, request );

            if ( result == 0 )
            {
                // Récuperation de l'action 
                String strIdSeancePropertyToRecalculate = request.getParameter( OdsParameters.SEANCE_ID_PROPERTY_TO_RECALCULATE );

                result = getParamaterWhenCreatingOrUpdatingSeance( request );

                int nIdSeancePropertyToRecalculate = -1;

                try
                {
                    nIdSeancePropertyToRecalculate = Integer.parseInt( strIdSeancePropertyToRecalculate );
                }
                catch ( NumberFormatException nfe )
                {
                    AppLogService.error( nfe );
                }

                if ( result == 0 )
                {
                    switch ( nIdSeancePropertyToRecalculate )
                    {
                        case 1:
                            _seanceBean.recalculateDateRemiseCrSommaire(  );

                            break;

                        case 2:
                            _seanceBean.recalculateDateDepotQuestion(  );

                            break;

                        case 3:
                            _seanceBean.recalculateDateLimiteDiffQuestions(  );

                            break;

                        case 4:
                            _seanceBean.recalculateDateLimiteDiffPdd(  );

                            break;

                        case 5:
                            _seanceBean.recalculateDateLimiteDiffDsp(  );

                            break;

                        case 6:
                            _seanceBean.recalculateDateConfOrg(  );

                            break;

                        case 7:
                            _seanceBean.recalculateDateReuPostComm(  );

                            break;

                        case 8:
                            _seanceBean.recalculateDateReu1(  );

                            break;

                        case 9:
                            _seanceBean.recalculateDateReu2(  );

                            break;

                        case 10:
                            _seanceBean.recalculateDateReu3(  );

                            break;

                        default:
                            break;
                    }
                }
            }

            if ( ( result == 1 ) || ( result == 2 ) )
            {
                Object[] messagesArgs = { I18nService.getLocalizedString( champsNonSaisie.get( 0 ), getLocale(  ) ) };

                return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_CREATE_SEANCE, messagesArgs,
                    AdminMessage.TYPE_STOP );
            }
            else
            {
                return AppPathService.getBaseUrl( request ) + TEMPLATE_JSP_CREATION_SEANCE;
            }
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * Modifie une séance
     * si tous les champs obligatoires du formulaire de modification d'une séance ont été renseigné,<BR>
     * la méthode retourne l'url de gestion des séances,<BR>
     * sinon l'url retournée est celle de l'interface spécifiant les messages d'erreurs.
     * @param request La requete HTTP
     * @return String template liste des seances
     */
    public String doUpdateAndRegisterSeance( HttpServletRequest request )
    {
        String strReturn = null;

        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            List<String> champsNonSaisie = new ArrayList<String>(  );
            int result = isValidForm( champsNonSaisie, request );

            if ( result == 0 )
            {
                if ( ( strReturn == null ) &&
                        ( OdsUtils.getDate( request.getParameter( OdsParameters.SEANCE_BEGIN ), true ) == null ) )
                {
                    Object[] messagesArgs = { I18nService.getLocalizedString( PROPERTY_LABEL_DATE_DEBUT, getLocale(  ) ) };

                    strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_CREATE_SEANCE, messagesArgs,
                            AdminMessage.TYPE_STOP );
                }

                if ( ( strReturn == null ) &&
                        ( OdsUtils.getDate( request.getParameter( OdsParameters.SEANCE_END ), true ) == null ) )
                {
                    Object[] messagesArgs = { I18nService.getLocalizedString( PROPERTY_LABEL_DATE_FIN, getLocale(  ) ) };

                    strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_CREATE_SEANCE, messagesArgs,
                            AdminMessage.TYPE_STOP );
                }

                if ( ( strReturn == null ) &&
                        !request.getParameter( OdsParameters.SEANCE_BEGIN )
                                    .equals( request.getParameter( OdsParameters.SEANCE_END ) ) &&
                        !OdsUtils.getDate( request.getParameter( OdsParameters.SEANCE_BEGIN ), true )
                                     .before( OdsUtils.getDate( request.getParameter( OdsParameters.SEANCE_END ), false ) ) )
                {
                    strReturn = AdminMessageService.getMessageUrl( request, PROPERTY_ILLOGICAL_DATE,
                            AdminMessage.TYPE_STOP );
                }

                if ( ( strReturn == null ) &&
                        !OdsUtils.getDate( request.getParameter( OdsParameters.SEANCE_END ), false )
                                     .after( OdsUtils.getCurrentDate(  ) ) )
                {
                    strReturn = AdminMessageService.getMessageUrl( request, PROPERTY_ILLOGICAL_DATE,
                            AdminMessage.TYPE_STOP );
                }

                if ( strReturn == null )
                {
                    result = getParamaterWhenCreatingOrUpdatingSeance( request );

                    SeanceHome.update( _seanceBean, getPlugin(  ) );
                    _seanceBean = new Seance(  );

                    strReturn = getHomeUrl( request );
                }
            }

            if ( strReturn == null )
            {
                Object[] messagesArgs = { I18nService.getLocalizedString( champsNonSaisie.get( 0 ), getLocale(  ) ) };

                strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_CREATE_SEANCE, messagesArgs,
                        AdminMessage.TYPE_STOP );
            }
        }
        else
        {
            strReturn = AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                    AdminMessage.TYPE_ERROR );
        }

        return strReturn;
    }

    /**
     * retourne le formulaire de modification d'une séance pré remplis avec  les critères de la séance choisi
     * @param request la requete HTTP
     * @return String template de modification d'une seance
     */
    public String getModificationSeance( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_MODIFICATION_SEANCE );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        HtmlTemplate template;

        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            String strIdSeance = request.getParameter( OdsParameters.ID_SEANCE );

            if ( strIdSeance != null )
            {
                setPageTitleProperty( PROPERTY_PAGE_MODIFICATION_SEANCE );

                int nIdSeance = -1;

                try
                {
                    nIdSeance = Integer.parseInt( strIdSeance );
                }
                catch ( NumberFormatException nfe )
                {
                    AppLogService.error( nfe );
                }

                _seanceBean = SeanceHome.findByPrimaryKey( nIdSeance, getPlugin(  ) );

                model.put( MARK_SEANCE_CREATING_OR_UPDATING, _seanceBean );

                template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_SEANCE, getLocale(  ), model );
            }
            else
            {
                return getSeanceList( request );
            }
        }
        else
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Permet de calculer les dates automatiquement en fonction de la date de début et de fin de séance
     * Ne modifie pas la séance.
     *
     * @param request la requete HTTP
     * @return String template de liste des seances
     */
    public String doModificationSeance( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_MODIFICATION_SEANCE );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        HtmlTemplate template;

        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            List<String> champsNonSaisie = new ArrayList<String>(  );
            int result = isValidForm( champsNonSaisie, request );

            if ( result == 0 )
            {
                // Récuperation de l'action 
                String strIdSeancePropertyToRecalculate = request.getParameter( OdsParameters.SEANCE_ID_PROPERTY_TO_RECALCULATE );

                getParamaterWhenCreatingOrUpdatingSeance( request );

                int nIdSeancePropertyToRecalculate = -1;

                try
                {
                    nIdSeancePropertyToRecalculate = Integer.parseInt( strIdSeancePropertyToRecalculate );
                }
                catch ( NumberFormatException nfe )
                {
                    AppLogService.error( nfe );
                }

                switch ( nIdSeancePropertyToRecalculate )
                {
                    case 1:
                        _seanceBean.recalculateDateRemiseCrSommaire(  );

                        break;

                    case 2:
                        _seanceBean.recalculateDateDepotQuestion(  );

                        break;

                    case 3:
                        _seanceBean.recalculateDateLimiteDiffQuestions(  );

                        break;

                    case 4:
                        _seanceBean.recalculateDateLimiteDiffPdd(  );

                        break;

                    case 5:
                        _seanceBean.recalculateDateLimiteDiffDsp(  );

                        break;

                    case 6:
                        _seanceBean.recalculateDateConfOrg(  );

                        break;

                    case 7:
                        _seanceBean.recalculateDateReuPostComm(  );

                        break;

                    case 8:
                        _seanceBean.recalculateDateReu1(  );

                        break;

                    case 9:
                        _seanceBean.recalculateDateReu2(  );

                        break;

                    case 10:
                        _seanceBean.recalculateDateReu3(  );

                        break;

                    default:
                        break;
                }
            }

            model.put( MARK_SEANCE_CREATING_OR_UPDATING, _seanceBean );

            template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_SEANCE, getLocale(  ), model );
        }
        else
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * réinitialise l'objet seance stocké en session
     * @param request la requete HTTP
     * @return String template
     */
    public String doResetModifyingSeance( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            _seanceBean = new Seance(  );

            return getHomeUrl( request );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * retourne une demande de confirmation pour la suppression de la séance choisie
     * @param request la requete HTTP
     * @return String Template de confirmation de suppression des seances
     */
    public String getSupressionSeance( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( request.getParameter( OdsParameters.ID_SEANCE ) != null )
            {
                String strIdSeance = request.getParameter( OdsParameters.ID_SEANCE );
                UrlItem url = new UrlItem( PROPERTY_URL_DO_SUPPRESSION_SEANCE_JSP );
                url.addParameter( OdsParameters.ID_SEANCE, strIdSeance );

                return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_SEANCE, url.getUrl(  ),
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
     * Supprime la séance choisi
     * @param request la requete HTTP
     * @return String template de liste des seances
     */
    public String doSupressionSeance( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            String strIdSeance = request.getParameter( OdsParameters.ID_SEANCE );

            if ( strIdSeance != null )
            {
                int nIdSeance;

                try
                {
                    nIdSeance = Integer.parseInt( strIdSeance );
                }
                catch ( NumberFormatException nfe )
                {
                    return getHomeUrl( request );
                }

                try
                {
                    Seance seance = SeanceHome.findByPrimaryKey( nIdSeance, getPlugin(  ) );
                    SeanceHome.remove( seance, getPlugin(  ) );
                }
                catch ( AppException ae )
                {
                    if ( ae.getInitialException(  ) instanceof SQLException )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_SEANCE,
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
     * retourne l'interface de gestion des séances<BR>
     * Seuls les séances dont la date de début est comprise entre les deux dates du filtre seront affichés
     * @param request request la requete HTTP
     * @return String template des listes des seances
     */
    public String applySeanceFilter( HttpServletRequest request )
    {
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        ajouterPermissionsDansHashmap( model );

        /*
         * Sinon le traitement de sa demande débute
         */
        String strSeanceBegin = request.getParameter( OdsParameters.SEANCE_BEGIN );
        String strSeanceEnd = request.getParameter( OdsParameters.SEANCE_END );

        List<String> champsNonSaisie = new ArrayList<String>(  );
        int result = isValidForm( champsNonSaisie, request );

        if ( ( result == 0 ) &&
                ( OdsUtils.getDate( strSeanceBegin.trim(  ), true )
                              .compareTo( OdsUtils.getDate( strSeanceEnd.trim(  ), true ) ) <= 0 ) &&
                ( OdsUtils.getDate( strSeanceEnd.trim(  ), false ).compareTo( OdsUtils.getCurrentDate(  ) ) >= 0 ) )
        {
            String strSeanceBeginFilter = request.getParameter( OdsParameters.SEANCE_BEGIN );
            String strSeanceEndFilter = request.getParameter( OdsParameters.SEANCE_END );

            setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

            Timestamp dSeanceDateBeginFilter = OdsUtils.getDate( strSeanceBeginFilter, true );
            Timestamp dSeanceDateEndFilter = OdsUtils.getDate( strSeanceEndFilter, false );

            List<Seance> seances = SeanceHome.findSeanceWidthFilterList( dSeanceDateBeginFilter, dSeanceDateEndFilter,
                    getPlugin(  ) );

            model.put( MARK_LISTE_SEANCES, seances );
            model.put( OdsParameters.SEANCE_BEGIN, strSeanceBeginFilter );
            model.put( OdsParameters.SEANCE_END, strSeanceEndFilter );
        }
        else
        {
            model.put( MARK_FILTER_ERROR, OdsConstants.CONSTANTE_CHAINE_VIDE );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_SEANCES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Retourne l'interface de gestion des séances<BR>
     * Seuls les séances dont la date de fin est supérieure ou égale à la date du jour seront affichés
     *
     * @param request la requete HTTP
     * @return String template des listes des seances
     */
    public String getSeanceList( HttpServletRequest request )
    {
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        List<Seance> seances = SeanceHome.findSeanceList( getPlugin(  ) );
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( MARK_LISTE_SEANCES, seances );
        model.put( OdsParameters.SEANCE_BEGIN, OdsConstants.CONSTANTE_CHAINE_VIDE );
        model.put( OdsParameters.SEANCE_END, OdsConstants.CONSTANTE_CHAINE_VIDE );

        ajouterPermissionsDansHashmap( model );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_SEANCES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * methode qui test si dans le formulaire de saisi d'une séance,l'ensemble des critéres obligatoires ont été renseigné
     * @param champsNonSaisie List<String> gere les champs non  obligatoires non renseignés
     * @param request la requete HTTP
     * @return int 1 si la date de debut ou 2 pour la date de fin (champs non correct) et 0 si tout est OK
     */
    private int isValidForm( List<String> champsNonSaisie, HttpServletRequest request )
    {
        String strSeanceBegin = request.getParameter( OdsParameters.SEANCE_BEGIN );
        String strSeanceEnd = request.getParameter( OdsParameters.SEANCE_END );

        int result = 0;

        if ( ( ( strSeanceBegin != null ) && !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strSeanceBegin.trim(  ) ) ) &&
                ( OdsUtils.getDate( strSeanceBegin.trim(  ), true ) != null ) )
        {
            result = 0;
        }
        else
        {
            champsNonSaisie.add( PROPERTY_LABEL_DATE_DEBUT );

            return 1;
        }

        if ( ( ( strSeanceEnd != null ) && !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strSeanceEnd.trim(  ) ) ) &&
                ( OdsUtils.getDate( strSeanceEnd.trim(  ), true ) != null ) )
        {
            result = 0;
        }
        else
        {
            champsNonSaisie.add( PROPERTY_LABEL_DATE_FIN );

            return 2;
        }

        return result;
    }

    /**
     * initalise la séance
     */
    public void initSeanceBean(  )
    {
        _seanceBean = new Seance(  );
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
