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
package fr.paris.lutece.plugins.ods.web.fascicule;

import fr.paris.lutece.plugins.ods.business.fascicule.Fascicule;
import fr.paris.lutece.plugins.ods.business.fascicule.FasciculeHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
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

import javax.servlet.http.HttpServletRequest;


/**
 * Cette classe gère l'affichage de l'interface de gestion des fascicules
 */
public class FasciculeJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_FASCICULE = "ODS_FASCICULES";
    private static final String PROPERTY_PAGE_CREATION_FASCICULE = "ods.fascicule.creation.pageTitle";
    private static final String PROPERTY_PAGE_MODIFICATION_FASCICULE = "ods.fascicule.modification.pageTitle";
    private static final String TEMPLATE_LISTE_FASCICULES = "admin/plugins/ods/fascicule/liste_fascicules.html";
    private static final String TEMPLATE_CREATION_FASCICULE = "admin/plugins/ods/fascicule/creation_fascicule.html";
    private static final String TEMPLATE_MODIFICATION_FASCICULE = "admin/plugins/ods/fascicule/modification_fascicule.html";
    private static final String TEMPLATE_NO_REFERENTIEL = "admin/plugins/ods/fascicule/no_ref.html";
    private static final String MARK_LISTE_FASCICULES = "liste_fascicules";
    private static final String MARK_LISTE_FASCICULES_COUNT = "liste_fascicules_count";
    private static final String MARK_FASCICULE = "fascicule";
    private static final String MESSAGE_CANNOT_CREATE_CODE_FASCICULE = "ods.creationfascicule.message.cannotCreateCodeFascicule";
    private static final String MESSAGE_CREATE_CODE_TOO_LONG = "ods.creationfascicule.message.createCodeTooLong";
    private static final String MESSAGE_ID_SEANCE_NFE_ON_CREATE = "ods.creationfascicule.message.errNumOnSeance";
    private static final String MESSAGE_NUMERO_NFE_ON_CREATE = "ods.creationfascicule.message.errNumeroOrdreOnCreate";
    private static final String MESSAGE_CANNOT_CREATE_NUMERO_FASCICULE = "ods.creationfascicule.message.cannotCreateNumeroFascicule";
    private static final String MESSAGE_CANNOT_MODIFY_CODE_FASCICULE = "ods.modificationfascicule.message.cannotModifyCodeFascicule";
    private static final String MESSAGE_MODIFY_CODE_TOO_LONG = "ods.creationfascicule.message.createCodeTooLong";
    private static final String MESSAGE_ID_FASCICULE_NFE_ON_MODIFY = "ods.modificationfascicule.message.errNumOnIdFascicule";
    private static final String MESSAGE_ID_SEANCE_NFE_ON_MODIFY = "ods.modificationfascicule.message.errNumOnIdSeance";
    private static final String MESSAGE_CANNOT_MODIFY_NUMERO_FASCICULE = "ods.modificationfascicule.message.cannotModifyNumeroFascicule";
    private static final String MESSAGE_NUMERO_NFE_ON_MODIFY = "ods.modificationfascicule.message.errNumeroOrdreOnCreate";
    private static final String MESSAGE_CONFIRM_DELETE_FASCICULE = "ods.suppressionFascicule.message.confirmDeleteFascicule";
    private static final String MESSAGE_CANNOT_DELETE_FASCICULE = "ods.suppressionfascicule.message.cannotDeleteFascicule";
    private static final String MESSAGE_ID_FASCICULE_NFE_ON_DELETE = "ods.suppressionFascicule.message.confirmDeleteFascicule";
    private static final String URL_DO_SUPPRESSION_NATURE_JSP = "jsp/admin/plugins/ods/fascicule/DoSuppressionFascicule.jsp";

    /**
     * Retourne l'interface de gestion des fascicules.
     * @param request la requête Http
     * @return l'interface de gestion des fascicules
     */
    public String getListeFascicules( HttpServletRequest request )
    {
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        Seance seance = SeanceHome.getProchaineSeance( getPlugin(  ) );

        if ( seance == null )
        {
            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_NO_REFERENTIEL, getLocale(  ) );

            return getAdminPage( template.getHtml(  ) );
        }

        List<Fascicule> fascicules = FasciculeHome.findFasciculeByIdSeance( seance.getIdSeance(  ), getPlugin(  ) );

        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( Seance.MARK_SEANCE, seance );
        model.put( MARK_LISTE_FASCICULES_COUNT, fascicules.size(  ) );
        model.put( MARK_LISTE_FASCICULES, fascicules );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_FASCICULES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Retourne l'interface de création des fascicules.
     * @param request la requête Http
     * @return l'interface de création des fascicules
     */
    public String getCreationFascicule( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_CREATION_FASCICULE );

        Seance seance = SeanceHome.getProchaineSeance( getPlugin(  ) );

        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( Seance.MARK_SEANCE, seance );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATION_FASCICULE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Crée un fascicule à partir de données de la requête
     * @param request la requête http
     * @return l'interface de gestion des fascicules
     */
    public String doCreationFascicule( HttpServletRequest request )
    {
        if ( !isValideForme( request ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        /**** Initialisation des variables ****/
        Fascicule fascicule = new Fascicule(  );
        Seance seance = new Seance(  );

        int nIdSeance;

        try
        {
            nIdSeance = Integer.parseInt( request.getParameter( OdsParameters.ID_SEANCE ) );
        }
        catch ( NumberFormatException nfe )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ID_SEANCE_NFE_ON_CREATE, AdminMessage.TYPE_STOP );
        }

        seance.setIdSeance( nIdSeance );
        fascicule.setSeance( seance );

        String strCode = request.getParameter( OdsParameters.CODE_FASCICULE );

        String strReturn = null;

        if ( ( strReturn == null ) && strCode.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            strReturn = AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( ( strReturn == null ) && FasciculeHome.containsCode( null, strCode, seance, getPlugin(  ) ) )
        {
            strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_CREATE_CODE_FASCICULE,
                    AdminMessage.TYPE_STOP );
        }

        if ( ( strReturn == null ) && ( strCode.length(  ) > 6 ) )
        {
            strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_CREATE_CODE_TOO_LONG, AdminMessage.TYPE_STOP );
        }

        if ( strReturn != null )
        {
            return strReturn;
        }

        strReturn = null;

        fascicule.setCodeFascicule( strCode );

        int nNumeroOrdre = -1;

        try
        {
            nNumeroOrdre = Integer.parseInt( request.getParameter( OdsParameters.NUMERO_FASCICULE ) );
        }
        catch ( NumberFormatException nfe )
        {
            strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_NUMERO_NFE_ON_CREATE, AdminMessage.TYPE_STOP );
        }

        if ( ( strReturn == null ) && FasciculeHome.containsNumero( null, nNumeroOrdre, seance, getPlugin(  ) ) )
        {
            strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_CREATE_NUMERO_FASCICULE,
                    AdminMessage.TYPE_STOP );
        }

        if ( strReturn != null )
        {
            return strReturn;
        }

        strReturn = null;

        fascicule.setNumeroOrdre( nNumeroOrdre );

        String strLibelle = request.getParameter( OdsParameters.NOM_FASCICULE );
        fascicule.setNomFascicule( strLibelle );

        FasciculeHome.create( fascicule, getPlugin(  ) );

        return getHomeUrl( request );
    }

    /**
     * Retourne l'interface de modification d'un fascicule.
     * @param request la requête Http
     * @return l'interface de modification d'un fascicule
     */
    public String getModificationFascicule( HttpServletRequest request )
    {
        if ( request.getParameter( OdsParameters.ID_FASCICULE ) == null )
        {
            return getListeFascicules( request );
        }

        setPageTitleProperty( PROPERTY_PAGE_MODIFICATION_FASCICULE );

        int nIdFascicule = -1;

        try
        {
            nIdFascicule = Integer.parseInt( request.getParameter( OdsParameters.ID_FASCICULE ) );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        Fascicule fascicule = FasciculeHome.findByPrimaryKey( nIdFascicule, getPlugin(  ) );

        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_FASCICULE, fascicule );
        model.put( Seance.MARK_SEANCE, fascicule.getSeance(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_FASCICULE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Modifie un fascicule à partir de données de la requête
     * @param request la requête http
     * @return l'interface de gestion des fascicules
     */
    public String doModificationFascicule( HttpServletRequest request )
    {
        String strReturn = null;

        if ( !isValideForme( request ) )
        {
            strReturn = AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        Fascicule fascicule = null;
        Seance seance = null;
        int nIdFascicule = -1;
        int nIdSeance = -1;

        if ( strReturn == null )
        {
            try
            {
                nIdFascicule = Integer.parseInt( request.getParameter( OdsParameters.ID_FASCICULE ) );
            }
            catch ( NumberFormatException nfe )
            {
                strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_ID_FASCICULE_NFE_ON_MODIFY,
                        AdminMessage.TYPE_STOP );
            }
        }

        if ( strReturn == null )
        {
            fascicule = FasciculeHome.findByPrimaryKey( nIdFascicule, getPlugin(  ) );

            try
            {
                nIdSeance = Integer.parseInt( request.getParameter( OdsParameters.ID_SEANCE ) );
            }
            catch ( NumberFormatException nfe )
            {
                strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_ID_SEANCE_NFE_ON_MODIFY,
                        AdminMessage.TYPE_STOP );
            }
        }

        if ( strReturn != null )
        {
            return strReturn;
        }

        strReturn = null;

        seance = new Seance(  );
        seance.setIdSeance( nIdSeance );
        fascicule.setSeance( seance );

        String strCode = request.getParameter( OdsParameters.CODE_FASCICULE );

        if ( ( strReturn == null ) && strCode.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            strReturn = AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( ( strReturn == null ) && FasciculeHome.containsCode( fascicule, strCode, seance, getPlugin(  ) ) )
        {
            strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_MODIFY_CODE_FASCICULE,
                    AdminMessage.TYPE_STOP );
        }

        if ( ( strReturn == null ) && ( strCode.length(  ) > 6 ) )
        {
            strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_MODIFY_CODE_TOO_LONG, AdminMessage.TYPE_STOP );
        }

        if ( strReturn != null )
        {
            return strReturn;
        }

        strReturn = null;

        fascicule.setCodeFascicule( strCode );

        int nNumeroOrdre = -1;

        try
        {
            nNumeroOrdre = Integer.parseInt( request.getParameter( OdsParameters.NUMERO_FASCICULE ) );
        }
        catch ( NumberFormatException nfe )
        {
            strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_NUMERO_NFE_ON_MODIFY, AdminMessage.TYPE_STOP );
        }

        if ( ( strReturn == null ) && FasciculeHome.containsNumero( fascicule, nNumeroOrdre, seance, getPlugin(  ) ) )
        {
            strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_MODIFY_NUMERO_FASCICULE,
                    AdminMessage.TYPE_STOP );
        }

        if ( strReturn != null )
        {
            return strReturn;
        }

        strReturn = null;

        fascicule.setNumeroOrdre( nNumeroOrdre );

        String strLibelle = request.getParameter( OdsParameters.NOM_FASCICULE );
        fascicule.setNomFascicule( strLibelle );

        FasciculeHome.update( fascicule, getPlugin(  ) );

        return getHomeUrl( request );
    }

    /**
     * Retourne l'interface de suppression d'un fascicule.
     * @param request la requête Http
     * @return l'interface de suppression d'un fascicule
     */
    public String getSuppressionFascicule( HttpServletRequest request )
    {
        if ( request.getParameter( OdsParameters.ID_FASCICULE ) == null )
        {
            return getHomeUrl( request );
        }

        String strIdFascicule = request.getParameter( OdsParameters.ID_FASCICULE );
        UrlItem url = new UrlItem( URL_DO_SUPPRESSION_NATURE_JSP );
        url.addParameter( OdsParameters.ID_FASCICULE, strIdFascicule );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_FASCICULE, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Supprime un fascicule à partir de données de la requête
     * @param request la requête http
     * @return l'interface de gestion des fascicules
     */
    public String doSuppressionFascicule( HttpServletRequest request )
    {
        if ( request.getParameter( OdsParameters.ID_FASCICULE ) == null )
        {
            return getHomeUrl( request );
        }

        int nIdFascicule;

        try
        {
            nIdFascicule = Integer.parseInt( request.getParameter( OdsParameters.ID_FASCICULE ) );
        }
        catch ( NumberFormatException nfe )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_ID_FASCICULE_NFE_ON_DELETE,
                AdminMessage.TYPE_STOP );
        }

        Fascicule fascicule = FasciculeHome.findByPrimaryKey( nIdFascicule, getPlugin(  ) );

        try
        {
            FasciculeHome.remove( fascicule, getPlugin(  ) );
        }
        catch ( AppException ae )
        {
            if ( ae.getInitialException(  ) instanceof SQLException )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_FASCICULE,
                    AdminMessage.TYPE_STOP );
            }
        }

        return getHomeUrl( request );
    }

    /**
     * Indique si la requête est valide, c'est à dire que tous les champs obligatoires ont été remplis.
     * @param request la requête HTTP
     * @return true si la requête est valide, false sinon
     */
    private boolean isValideForme( HttpServletRequest request )
    {
        if ( ( request.getParameter( OdsParameters.CODE_FASCICULE ) == OdsConstants.CONSTANTE_CHAINE_VIDE ) ||
                ( request.getParameter( OdsParameters.NOM_FASCICULE ) == OdsConstants.CONSTANTE_CHAINE_VIDE ) ||
                ( request.getParameter( OdsParameters.NUMERO_FASCICULE ) == OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            return false;
        }

        return true;
    }
}
