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
package fr.paris.lutece.plugins.ods.web.naturedossier;

import fr.paris.lutece.plugins.ods.business.naturedossier.NatureDesDossiers;
import fr.paris.lutece.plugins.ods.business.naturedossier.NatureDesDossiersHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.constants.OdsProperties;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * Cette classe gère l'interface de gastion des natures des dossiers de la prochaine séance
 */
public class NatureDesDossiersJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_NATURE_DES_DOSSIERS = "ODS_NATURE_DOSSIERS";
    private static final String TEMPLATE_LISTE_NATURES = "admin/plugins/ods/naturedossier/liste_nature_dossier.html";
    private static final String TEMPLATE_CREATION_NATURE = "admin/plugins/ods/naturedossier/creation_nature_dossier.html";
    private static final String TEMPLATE_MODIFICATION_NATURE = "admin/plugins/ods/naturedossier/modification_nature_dossier.html";
    private static final String TEMPLATE_AUCUN_REFERENTIEL = "admin/plugins/ods/naturedossier/no_ref.html";
    private static final String PROPERTY_PAGE_CREATION_NATURE = "ods.naturedossier.creation.pageTitle";
    private static final String PROPERTY_PAGE_MODIFICATION_NATURE = "ods.naturedossier.modification.pageTitle";
    private static final String MARK_LISTE_NATURE = "liste_natures";
    private static final String MARK_NOMBRE_LISTE_NATURE = "liste_natures_count";
    private static final String MARK_NATURE = "nature";
    private static final String MESSAGE_NUM_NATURE_NFE_ON_CREATE = "ods.creationnaturedossier.message.errNumOnNature";
    private static final String MESSAGE_CREATION_NATURE_IMPOSSIBLE = "ods.creationnaturedossier.message.cannotCreateNature";
    private static final String MESSAGE_NUM_NATURE_NFE_ON_MODIFY = "ods.modificationnaturedossier.message.errNumOnNumNature";
    private static final String MESSAGE_MODIFICATION_NATURE_IMPOSSIBLE = "ods.modificationnaturedossier.message.cannotModifyNature";
    private static final String MESSAGE_CONFIRMER_SUPPRESSION_NATURE = "ods.suppressionnaturedossier.message.confirmDeleteNature";
    private static final String MESSAGE_SUPPRESSION_NATURE_IMPOSSIBLE = "ods.suppressionnaturedossier.message.cannotDeleteNature";
    private static final String URL_DO_SUPPRESSION_NATURE_JSP = "jsp/admin/plugins/ods/naturedossier/DoSuppressionNatureDossier.jsp";
    private static final String FIELD_INTITULE = "ods.creationnaturedossier.label.libelle";
    private static final String FIELD_NUMERO = "ods.creationnaturedossier.label.ordre";

    /**
     * Retourne l'interface de gestion des natures de dossier.
     *
     * @param request la requête HTTP
     * @return l'interface de gestion des natures de dossier
     */
    public String getListNatures( HttpServletRequest request )
    {
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        Seance seance = SeanceHome.getProchaineSeance( getPlugin(  ) );

        if ( seance == null )
        {
            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_AUCUN_REFERENTIEL, getLocale(  ) );

            return getAdminPage( template.getHtml(  ) );
        }

        List<NatureDesDossiers> natures = NatureDesDossiersHome.findBySeance( seance.getIdSeance(  ), getPlugin(  ) );
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( Seance.MARK_SEANCE, seance );
        model.put( MARK_NOMBRE_LISTE_NATURE, natures.size(  ) );
        model.put( MARK_LISTE_NATURE, natures );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_NATURES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Retourne l'interface de création d'une nature de dossier.
     * @param request la requête HTTP
     * @return l'interface de création de nature de dossier
     */
    public String getCreationNature( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_CREATION_NATURE );

        Seance seance = SeanceHome.getProchaineSeance( getPlugin(  ) );
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( Seance.MARK_SEANCE, seance );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATION_NATURE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Crée une nouvelle nature de dossier.
     * @param request la requête HTTP
     * @return l'interface de gestion de natures de dossier
     */
    public String doCreationNature( HttpServletRequest request )
    {
        if ( !requiredField( request ).equals( ( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) )
        {
            Object[] tabRequiredFields = { I18nService.getLocalizedString( requiredField( request ), getLocale(  ) ) };

            return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        NatureDesDossiers nature;
        Seance seance;

        int nIdSeance = -1;

        try
        {
            nIdSeance = Integer.parseInt( request.getParameter( OdsParameters.ID_SEANCE ) );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        seance = new Seance(  );
        seance.setIdSeance( nIdSeance );
        nature = new NatureDesDossiers(  );
        nature.setSeance( seance );

        int nNumNature;
        String strLibelle = request.getParameter( OdsParameters.INTITULE_NATURE ).trim(  );

        try
        {
            nNumNature = Integer.parseInt( request.getParameter( OdsParameters.NUMERO_NATURE ) );
        }
        catch ( NumberFormatException nfe )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_NUM_NATURE_NFE_ON_CREATE, AdminMessage.TYPE_STOP );
        }

        if ( nNumNature < 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_NUM_NATURE_NFE_ON_CREATE, AdminMessage.TYPE_STOP );
        }

        if ( NatureDesDossiersHome.containsNumero( null, nNumNature, seance, getPlugin(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CREATION_NATURE_IMPOSSIBLE,
                AdminMessage.TYPE_STOP );
        }

        nature.setNumeroNature( nNumNature );
        nature.setLibelleNature( strLibelle );
        NatureDesDossiersHome.create( nature, getPlugin(  ) );

        return getHomeUrl( request );
    }

    /**
     * Retourne l'interface de modification de nature de dossier
     * @param request la requête HTTP
     * @return l'interface de modification de nature de dossier
     */
    public String getModificationNature( HttpServletRequest request )
    {
        if ( request.getParameter( OdsParameters.ID_NATURE ) == null )
        {
            return getListNatures( request );
        }

        setPageTitleProperty( PROPERTY_PAGE_MODIFICATION_NATURE );

        int nIdNature = -1;

        try
        {
            nIdNature = Integer.parseInt( request.getParameter( OdsParameters.ID_NATURE ) );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        NatureDesDossiers nature = NatureDesDossiersHome.findByPrimaryKey( nIdNature, getPlugin(  ) );
        Seance seance = nature.getSeance(  );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( Seance.MARK_SEANCE, seance );
        model.put( MARK_NATURE, nature );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_NATURE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Enregistre les modifications de la nature de dossier
     * @param request la requête HTTP
     * @return l'interface de gestion des natures de dossier
     */
    public String doModificationNature( HttpServletRequest request )
    {
        if ( !requiredField( request ).equals( ( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) )
        {
            Object[] tabRequiredFields = { I18nService.getLocalizedString( requiredField( request ), getLocale(  ) ) };

            return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                tabRequiredFields, AdminMessage.TYPE_STOP );
        }

        NatureDesDossiers nature;
        Seance seance;

        int nIdNature = -1;

        try
        {
            nIdNature = Integer.parseInt( request.getParameter( OdsParameters.ID_NATURE ) );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        nature = NatureDesDossiersHome.findByPrimaryKey( nIdNature, getPlugin(  ) );

        int nIdSeance = -1;

        try
        {
            nIdSeance = Integer.parseInt( request.getParameter( OdsParameters.ID_SEANCE ) );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        seance = new Seance(  );
        seance.setIdSeance( nIdSeance );
        nature.setSeance( seance );

        int nNumNature;
        String strLibelle = request.getParameter( OdsParameters.INTITULE_NATURE ).trim(  );

        try
        {
            nNumNature = Integer.parseInt( request.getParameter( OdsParameters.NUMERO_NATURE ) );
        }
        catch ( NumberFormatException nfe )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_NUM_NATURE_NFE_ON_MODIFY, AdminMessage.TYPE_STOP );
        }

        if ( nNumNature < 0 )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_NUM_NATURE_NFE_ON_MODIFY, AdminMessage.TYPE_STOP );
        }

        if ( NatureDesDossiersHome.containsNumero( nature, nNumNature, seance, getPlugin(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_MODIFICATION_NATURE_IMPOSSIBLE,
                AdminMessage.TYPE_STOP );
        }

        nature.setNumeroNature( nNumNature );
        nature.setLibelleNature( strLibelle );
        NatureDesDossiersHome.update( nature, getPlugin(  ) );

        return getHomeUrl( request );
    }

    /**
     * Retourne l'interface de suppression de nature de dossier
     * @param request la requete HTTP
     * @return l'interface de suppression de nature de dossier
     */
    public String getSuppressionNature( HttpServletRequest request )
    {
        if ( request.getParameter( OdsParameters.ID_NATURE ) == null )
        {
            return getHomeUrl( request );
        }

        String strIdNature = request.getParameter( OdsParameters.ID_NATURE );
        UrlItem url = new UrlItem( URL_DO_SUPPRESSION_NATURE_JSP );
        url.addParameter( OdsParameters.ID_NATURE, strIdNature );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRMER_SUPPRESSION_NATURE, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Supprime la nature de dossier
     * @param request la requête Http
     * @return l'interface de gestion des natures de dossier
     */
    public String doSuppressionNature( HttpServletRequest request )
    {
        if ( request.getParameter( OdsParameters.ID_NATURE ) == null )
        {
            return getHomeUrl( request );
        }

        int nIdNature = -1;

        try
        {
            nIdNature = Integer.parseInt( request.getParameter( OdsParameters.ID_NATURE ) );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        NatureDesDossiers nature = NatureDesDossiersHome.findByPrimaryKey( nIdNature, getPlugin(  ) );

        try
        {
            NatureDesDossiersHome.remove( nature, getPlugin(  ) );
        }
        catch ( AppException ae )
        {
            if ( ae.getInitialException(  ) instanceof SQLException )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_SUPPRESSION_NATURE_IMPOSSIBLE,
                    AdminMessage.TYPE_STOP );
            }
        }

        return getHomeUrl( request );
    }

    /**
     * Indique si la requête est valide, c'est à dire que tous les champs obligatoires ont été remplis.
     * @param request la requête HTTP
     * @return le premier champ manquant à la création/modification
     */
    private String requiredField( HttpServletRequest request )
    {
        String strRequiredField = OdsConstants.CONSTANTE_CHAINE_VIDE;

        if ( request.getParameter( OdsParameters.INTITULE_NATURE ).equals( null ) ||
                request.getParameter( OdsParameters.INTITULE_NATURE ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            strRequiredField = FIELD_INTITULE;
        }
        else if ( request.getParameter( OdsParameters.NUMERO_NATURE ).equals( null ) ||
                request.getParameter( OdsParameters.NUMERO_NATURE ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            strRequiredField = FIELD_NUMERO;
        }

        return strRequiredField;
    }
}
