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
package fr.paris.lutece.plugins.ods.web.relevetravaux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jcopist.service.exception.ServerException;
import net.sf.jcopist.service.exception.UnavailableException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.commission.CommissionHome;
import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.elu.EluHome;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierFilter;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysique;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysiqueHome;
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocumentEnum;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitique;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitiqueHome;
import fr.paris.lutece.plugins.ods.business.historique.Historique;
import fr.paris.lutece.plugins.ods.business.historique.HistoriqueHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourUtils;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ElementReleveDesTravaux;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ElementReleveDesTravauxHome;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravaux;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravauxHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementFilter;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementHome;
import fr.paris.lutece.plugins.ods.service.manager.DocumentManager;
import fr.paris.lutece.plugins.ods.service.role.RelevesDesTravauxResourceIdService;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.constants.OdsProperties;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;


/**
 * PluginAdminPageJspBean
 */
public class ReleveDesTravauxJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_RELEVE = "ODS_RELEVE_TRAVAUX";
    public static final String CONSTANTE_URL_JSP_APPELANT_PROJET = "jsp/admin/plugins/ods/projetdeliberation/ProjetDeliberationForReleveDesTravaux.jsp";
    public static final String CONSTANTE_URL_JSP_APPELANT_PROPOSITION = "jsp/admin/plugins/ods/projetdeliberation/PropositionDeliberationForReleveDesTravaux.jsp";
    public static final String CONSTANTE_URL_JSP_APPELANT_AMENDEMENT = "jsp/admin/plugins/ods/voeuamendement/AmendementsForReleveDesTravaux.jsp";
    public static final String CONSTANTE_URL_JSP_APPELANT_VOEU_RATTACHE = "jsp/admin/plugins/ods/voeuamendement/VoeuxRattachesForReleveDesTravaux.jsp";
    public static final String CONSTANTE_URL_JSP_APPELANT_VOEU_NON_RATTACHE = "jsp/admin/plugins/ods/voeuamendement/VoeuxNonRattachesForReleveDesTravaux.jsp";
    public static final String CONSTANTE_URL_JSP_RETOUR = "jsp/admin/plugins/ods/relevetravaux/DoSelectionVoeuAmendement.jsp";
    public static final String CONSTANTE_URL_JSP_RETOUR_PDD = "jsp/admin/plugins/ods/relevetravaux/DoSelectionPDD.jsp";
    private static final String TEMPLATE_LISTE_RELEVES = "admin/plugins/ods/relevetravaux/liste_releves.html";
    private static final String TEMPLATE_MODIFICATION_RELEVE = "admin/plugins/ods/relevetravaux/modification_releve.html";
    private static final String TEMPLATE_MODIFICATION_ELEMENT_RELEVE = "admin/plugins/ods/relevetravaux/modification_element_releve.html";
    private static final String TEMPLATE_CREATION_ELEMENT_RELEVE = "admin/plugins/ods/relevetravaux/creation_element_releve.html";
    private static final String PROPERTY_PAGE_TITLE_RELEVE = "ods.releve.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFICATION_RELEVE = "ods.releve.modification.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFICATION_ELEMENT_RELEVE = "ods.releve.modificationElement.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATION_ELEMENT_RELEVE = "ods.releve.creationElement.pageTitle";
    private static final String PROPERTY_INTITULE_BEGIN = "ods.releve.label.relevecommission.first";
    private static final String PROPERTY_INTITULE_END1 = "ods.releve.label.relevecommission.end1";
    private static final String PROPERTY_INTITULE_END2 = "ods.releve.label.relevecommission.end2";
    private static final String PROPERTY_INTITULE_FICHIER = "ods.releve.fichier.intitule";
    private static final String MARK_LISTE_RELEVES = "liste_releves";
    private static final String MARK_LISTE_COMMISSIONS = "liste_commissions";
    private static final String MARK_LISTE_ELUS = "liste_elus";
    private static final String MARK_LISTE_GROUPES = "liste_groupes";
    private static final String MARK_ELEMENT_RELEVE = "element_releve";
    private static final String MARK_LISTE_VA = "liste_elmt_va";
    private static final String MARK_LISTE_PDD = "liste_elmt_pdd";
    private static final String MARK_VA_SELECTED = "va_selected";
    private static final String MARK_PDD_SELECTED = "pdd_selected";
    private static final String MARK_TYPE_ACTION_CREATION_ELEMENT = "type_creation_element";
    private static final String MESSAGE_CONFIRM_DELETE_RELEVE = "ods.releve.message.confirmDeleteReleve";
    private static final String MESSAGE_CONFIRM_DELETE_ELEMENT_RELEVE = "ods.releve.message.confirmDeleteElementReleve";
    private static final String MESSAGE_CANNOT_DELETE_RELEVE = "ods.releve.message.cannotDeleteReleve";
    private static final String MESSAGE_CANNOT_DELETE_ELEMENT_RELEVE = "ods.releve.message.cannotDeleteElementReleve";
    private static final String MESSAGE_NO_COMMISSION = "ods.releve.message.nocommission";
    private static final String MESSAGE_NO_SEANCE = "ods.releve.message.noseance";
    private static final String MESSAGE_NO_GROUPE = "ods.releve.message.noGroupe";
    private static final String MESSAGE_NO_ELU = "ods.releve.message.noElu";
    private static final String MESSAGE_NO_VA_SELECTED = "ods.releve.message.noVaSelected";
    private static final String MESSAGE_NO_PDD_SELECTED = "ods.releve.message.noPddSelected";
    private static final String JSP_URL_DO_SUPPRESSION_RELEVE_JSP = "jsp/admin/plugins/ods/relevetravaux/DoSuppressionReleveTravaux.jsp";
    private static final String JSP_URL_DO_SUPPRESSION_ELEMENT_RELEVE_JSP = "jsp/admin/plugins/ods/relevetravaux/DoSuppressionElementReleveTravaux.jsp";
    private static final String JSP_URL_GET_MODIFICATION_RELEVE = "jsp/admin/plugins/ods/relevetravaux/ModificationReleveTravaux.jsp?id_releve=";
    private static final String JSP_URL_GET_CREATION_ELEMENT_RELEVE = "jsp/admin/plugins/ods/relevetravaux/CreationElementReleveTravaux.jsp";
    private static final String JSP_URL_GET_MODIFICATION_ELEMENT_RELEVE = "jsp/admin/plugins/ods/relevetravaux/ModificationElementReleveTravaux.jsp";
    private static final String CONSTANTE_CANCEL = "cancel";
    private static final String CONSTANTE_SAVE = "save";
    private static final String CONSTANTE_VA = "va";
    private static final String CONSTANTE_ENTETE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String FIELD_NUM_COMMISSION = "ods.releve.label.listeCommissions";
    private static final String PROPERTY_RELEVE_NOM_TEMPLATE = "odsreleve.nomtemplate";
    private static final String DEFAULT_RELEVE_NOM_TEMPLATE = "releve_des_travaux";

    /*
     * Variables de session.
     */
    private ReleveDesTravaux _releveDesTravauxBean;
    private ElementReleveDesTravaux _elementReleveDesTravauxBean;
    private VoeuAmendement _voeuAmendementSelectedBean;
    private PDD _pddSelectedBean;

    /**
     * Crée un relevé des travaux.
     * @param request la requete Http
     * @return l'url de retour
     */
    public String doCreationReleve( HttpServletRequest request )
    {
        if ( ( null != request.getParameter( OdsParameters.NUM_COMMISSION ) ) )
        {
            ReleveDesTravaux releve = new ReleveDesTravaux(  );

            int nNumCommission = -1;

            try
            {
                nNumCommission = Integer.parseInt( request.getParameter( OdsParameters.NUMERO_COMMISSION ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            Commission commission = CommissionHome.findByNumeroCommission( nNumCommission, getPlugin(  ) );

            if ( commission != null )
            {
                releve.setCommission( commission );
            }
            else
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_NO_COMMISSION, AdminMessage.TYPE_STOP );
            }

            String strIntitule = I18nService.getLocalizedString( PROPERTY_INTITULE_BEGIN, getLocale(  ) ) + " " +
                commission.getNumero(  );

            if ( commission.getNumero(  ) == 1 )
            {
                strIntitule += I18nService.getLocalizedString( PROPERTY_INTITULE_END1, getLocale(  ) );
            }
            else
            {
                strIntitule += I18nService.getLocalizedString( PROPERTY_INTITULE_END2, getLocale(  ) );
            }

            releve.setIntitule( strIntitule );

            int nIdSeance = -1;

            try
            {
                nIdSeance = Integer.parseInt( request.getParameter( OdsParameters.ID_SEANCE ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            Seance prochaineSeance = SeanceHome.findByPrimaryKey( nIdSeance, getPlugin(  ) );

            if ( prochaineSeance != null )
            {
                releve.setSeance( prochaineSeance );
            }
            else
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_NO_SEANCE, AdminMessage.TYPE_STOP );
            }

            releve.setEnLigne( false );

            int nIdReleveDesTravaux = ReleveDesTravauxHome.create( releve, getPlugin(  ) );
            releve.setIdReleveDesTravaux( nIdReleveDesTravaux );

            /*
             * On rattache tous les voeux et amendements de la commission au relevé
             */
            VoeuAmendementFilter vaFilter = new VoeuAmendementFilter(  );
            vaFilter.setIdCommission( nNumCommission );
            vaFilter.setIdSeance( prochaineSeance.getIdSeance(  ) );

            List<VoeuAmendement> listVA = VoeuAmendementHome.findVoeuAmendementListByFilter( vaFilter, getPlugin(  ) );

            for ( VoeuAmendement va : listVA )
            {
                ElementReleveDesTravaux element = new ElementReleveDesTravaux(  );
                element.setVoeuAmendement( va );
                element.setReleve( releve );

                ElementReleveDesTravauxHome.create( element, getPlugin(  ) );
            }

            return getHomeUrl( request );
        }
        else
        {
            Object[] message = { I18nService.getLocalizedString( FIELD_NUM_COMMISSION, getLocale(  ) ) };

            return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED, message,
                AdminMessage.TYPE_STOP );
        }
    }

    /**
     * Affiche la page de modification d'un relevé de travaux.
     * @param request la requete Http
     * @return le contenu de la template
     */
    public String getModificationReleve( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_RELEVE );

        HashMap<String, Object> model = new HashMap<String, Object>(  );

        _releveDesTravauxBean = null;
        _voeuAmendementSelectedBean = null;
        _pddSelectedBean = null;

        if ( ( request.getParameter( OdsParameters.ID_RELEVE ) == null ) ||
                request.getParameter( OdsParameters.ID_RELEVE ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nIdReleve = -1;

        try
        {
            nIdReleve = Integer.parseInt( request.getParameter( OdsParameters.ID_RELEVE ).trim(  ) );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        _releveDesTravauxBean = ReleveDesTravauxHome.findByPrimaryKey( nIdReleve, getPlugin(  ) );

        HtmlTemplate template;

        /*
         * GESTION DES PROFILS
         * -------------------
         * Si l'utilisateur n'est pas autorisé a effectuer cette fonction il est renvoyé
         * vers une page lui indiquant qu'il n'a pas les droits nécessaires.
         */
        if ( _releveDesTravauxBean.isEnLigne(  ) &&
                !RBACService.isAuthorized( RelevesDesTravauxResourceIdService.RELEVES_DES_TRAVAUX,
                    RBAC.WILDCARD_RESOURCES_ID, RelevesDesTravauxResourceIdService.PERMISSION_MISE_A_JOUR, getUser(  ) ) )
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ) );

            return getAdminPage( template.getHtml(  ) );
        }

        List<ElementReleveDesTravaux> elementsVA = new ArrayList<ElementReleveDesTravaux>(  );
        List<ElementReleveDesTravaux> elementsPDD = new ArrayList<ElementReleveDesTravaux>(  );

        elementsVA = ElementReleveDesTravauxHome.findElementReleveDesTravauxByReleveVA( _releveDesTravauxBean,
                getPlugin(  ) );

        if ( ( elementsVA != null ) && !elementsVA.isEmpty(  ) )
        {
            for ( ElementReleveDesTravaux element : elementsVA )
            {
                VoeuAmendement va = null;

                if ( element.getVoeuAmendement(  ) != null )
                {
                    int nIdVA = element.getVoeuAmendement(  ).getIdVoeuAmendement(  );
                    va = VoeuAmendementHome.findByPrimaryKey( nIdVA, getPlugin(  ) );
                }

                if ( va != null )
                {
                    element.setVoeuAmendement( va );
                }
            }
        }

        elementsPDD = ElementReleveDesTravauxHome.findElementReleveDesTravauxByRelevePDD( _releveDesTravauxBean,
                getPlugin(  ) );

        if ( ( elementsPDD != null ) && !elementsPDD.isEmpty(  ) )
        {
            for ( ElementReleveDesTravaux element : elementsPDD )
            {
                PDD pdd = null;
                Elu elu = null;
                GroupePolitique groupe = null;

                if ( element.getProjetDeliberation(  ) != null )
                {
                    int nIdPDD = element.getProjetDeliberation(  ).getIdPdd(  );
                    pdd = PDDHome.findByPrimaryKey( nIdPDD, getPlugin(  ) );
                }

                if ( element.getElu(  ) != null )
                {
                    int nIdElu = element.getElu(  ).getIdElu(  );
                    elu = EluHome.findByPrimaryKey( nIdElu, getPlugin(  ) );
                }

                if ( element.getGroupe(  ) != null )
                {
                    int nIdGroupe = element.getGroupe(  ).getIdGroupe(  );
                    groupe = GroupePolitiqueHome.findByPrimaryKey( nIdGroupe, getPlugin(  ) );
                }

                if ( pdd != null )
                {
                    element.setProjetDeliberation( pdd );
                }

                if ( elu != null )
                {
                    element.setElu( elu );
                }

                if ( groupe != null )
                {
                    element.setGroupe( groupe );
                }
            }
        }

        model.put( ReleveDesTravaux.MARK_RELEVE, _releveDesTravauxBean );
        model.put( MARK_LISTE_VA, elementsVA );
        model.put( MARK_LISTE_PDD, elementsPDD );
        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );
        template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_RELEVE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Effectue les modifications demandées dans la base de données.
     * @param request la requete Http
     * @return l'url de retour
     */
    public String doModificationReleve( HttpServletRequest request )
    {
        String strReturn = null;

        if ( null != request.getParameter( OdsParameters.ID_RELEVE ) )
        {
            int nIdReleve = -1;

            try
            {
                nIdReleve = Integer.parseInt( request.getParameter( OdsParameters.ID_RELEVE ).trim(  ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            if ( null != request.getParameter( OdsParameters.PUBLICATION_RELEVE ) )
            {
                strReturn = this.doPublication( nIdReleve, request );
            }
            else if ( null != request.getParameter( OdsParameters.DEPUBLICATION_RELEVE ) )
            {
                strReturn = this.doDepublication( nIdReleve, request );
            }
        }

        if ( strReturn != null )
        {
            return strReturn;
        }

        if ( ( request.getParameter( OdsParameters.A_FAIRE ) != null ) &&
                !request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_CANCEL ) )
        {
            if ( ( null != request.getParameter( OdsParameters.INTITULE ) ) &&
                    ( null != request.getParameter( OdsParameters.ID_RELEVE ) ) &&
                    !request.getParameter( OdsParameters.INTITULE ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                ReleveDesTravaux releve = new ReleveDesTravaux(  );
                int nIdReleve = -1;

                try
                {
                    nIdReleve = Integer.parseInt( request.getParameter( OdsParameters.ID_RELEVE ).trim(  ) );
                }
                catch ( NumberFormatException nfe )
                {
                    AppLogService.error( nfe );
                }

                String strIntitule = request.getParameter( OdsParameters.INTITULE ).trim(  );
                releve = ReleveDesTravauxHome.findByPrimaryKey( nIdReleve, getPlugin(  ) );

                /*
                 * GESTION DES PROFILS
                 * -------------------
                 * Si l'utilisateur n'est pas autorisé a effectuer cette fonction il est renvoyé
                 * vers une page lui indiquant qu'il n'a pas les droits nécessaires.
                 */
                if ( releve.isEnLigne(  ) &&
                        !RBACService.isAuthorized( RelevesDesTravauxResourceIdService.RELEVES_DES_TRAVAUX,
                            RBAC.WILDCARD_RESOURCES_ID, RelevesDesTravauxResourceIdService.PERMISSION_MISE_A_JOUR,
                            getUser(  ) ) )
                {
                    return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                        AdminMessage.TYPE_ERROR );
                }

                if ( ( releve != null ) )
                {
                    releve.setIntitule( strIntitule );
                    ReleveDesTravauxHome.update( releve, getPlugin(  ) );
                }

                if ( request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_SAVE ) )
                {
                    _releveDesTravauxBean = null;

                    return getHomeUrl( request );
                }

                _releveDesTravauxBean = null;

                return AppPathService.getBaseUrl( request ) + JSP_URL_GET_MODIFICATION_RELEVE +
                releve.getIdReleveDesTravaux(  );
            }

            _releveDesTravauxBean = null;

            strReturn = getRelevesList( request );
        }

        if ( strReturn == null )
        {
            strReturn = getHomeUrl( request );
        }

        return strReturn;
    }

    /**
     * Affiche le message de confirmation de suppression d'un relevé de travaux.
     * @param request la requete http
     * @return le contenu de la template
     */
    public String getSuppressionReleve( HttpServletRequest request )
    {
        if ( null != request.getParameter( OdsParameters.ID_RELEVE ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_RELEVE );

            String strReleveID = request.getParameter( OdsParameters.ID_RELEVE ).trim(  );
            int nIdReleve = -1;

            try
            {
                nIdReleve = Integer.parseInt( strReleveID );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            ReleveDesTravaux releve = ReleveDesTravauxHome.findByPrimaryKey( nIdReleve, getPlugin(  ) );

            /*
             * GESTION DES PROFILS
             * -------------------
             * Si l'utilisateur n'est pas autorisé a effectuer cette fonction il est renvoyé
             * vers une page lui indiquant qu'il n'a pas les droits nécessaires.
             */
            if ( releve.isEnLigne(  ) &&
                    !RBACService.isAuthorized( RelevesDesTravauxResourceIdService.RELEVES_DES_TRAVAUX,
                        RBAC.WILDCARD_RESOURCES_ID, RelevesDesTravauxResourceIdService.PERMISSION_SUPPRESSION,
                        getUser(  ) ) )
            {
                return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                    AdminMessage.TYPE_ERROR );
            }

            UrlItem url = new UrlItem( JSP_URL_DO_SUPPRESSION_RELEVE_JSP );
            url.addParameter( OdsParameters.ID_RELEVE, strReleveID );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_RELEVE, url.getUrl(  ),
                AdminMessage.TYPE_CONFIRMATION );
        }
        else
        {
            return getHomeUrl( request );
        }
    }

    /**
     * Supprime un relevé de travaux de la base de données.
     * @param request la requete Http
     * @return l'url de retour
     */
    public String doSuppressionReleve( HttpServletRequest request )
    {
        if ( null != request.getParameter( OdsParameters.ID_RELEVE ) )
        {
            ReleveDesTravaux releve;

            try
            {
                releve = ReleveDesTravauxHome.findByPrimaryKey( Integer.parseInt( 
                            request.getParameter( OdsParameters.ID_RELEVE ).trim(  ) ), getPlugin(  ) );
            }
            catch ( NumberFormatException ne )
            {
                return getHomeUrl( request );
            }

            /*
             * GESTION DES PROFILS
             * -------------------
             * Si l'utilisateur n'est pas autorisé a effectuer cette fonction il est renvoyé
             * vers une page lui indiquant qu'il n'a pas les droits nécessaires.
             */
            if ( releve.isEnLigne(  ) &&
                    !RBACService.isAuthorized( RelevesDesTravauxResourceIdService.RELEVES_DES_TRAVAUX,
                        RBAC.WILDCARD_RESOURCES_ID, RelevesDesTravauxResourceIdService.PERMISSION_SUPPRESSION,
                        getUser(  ) ) )
            {
                return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                    AdminMessage.TYPE_ERROR );
            }

            try
            {
                List<ElementReleveDesTravaux> listElements = ElementReleveDesTravauxHome.findElementReleveDesTravauxByReleve( releve,
                        getPlugin(  ) );

                for ( ElementReleveDesTravaux element : listElements )
                {
                    ElementReleveDesTravauxHome.remove( element, getPlugin(  ) );
                }

                ReleveDesTravauxHome.remove( releve, getPlugin(  ) );
            }
            catch ( AppException ae )
            {
                if ( ae.getInitialException(  ) instanceof SQLException )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_RELEVE,
                        AdminMessage.TYPE_STOP );
                }
            }
        }

        return getHomeUrl( request );
    }

    /**
     * Affiche la page d'où peut être créé un élement d'un relevé de travaux.
     * @param request la requete Http
     * @return le contenu de la template
     */
    public String getCreationElementReleve( HttpServletRequest request )
    {
        // initialisation
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATION_ELEMENT_RELEVE );

        HashMap<String, Object> model = new HashMap<String, Object>(  );

        // Création d'un nouvel élément => élément en session réinitialisée
        _elementReleveDesTravauxBean = null;

        // Si le relevé de travaux en session est null => ERREUR
        if ( _releveDesTravauxBean == null )
        {
            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_RELEVES, getLocale(  ), model );

            return getAdminPage( template.getHtml(  ) );
        }

        // Sinon on met le relevé de travaux dans la hashmap
        model.put( ReleveDesTravaux.MARK_RELEVE, _releveDesTravauxBean );

        // Si le type d'élément de relevé de travaux à créer n'est pas indiqué => ERREUR
        if ( ( request.getParameter( OdsParameters.A_FAIRE ) == null ) ||
                request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_RELEVES, getLocale(  ), model );

            return getAdminPage( template.getHtml(  ) );
        }

        // Cas où l'élément à créer est de type Voeu ou Amendement
        if ( request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_VA ) )
        {
            model.put( MARK_TYPE_ACTION_CREATION_ELEMENT, CONSTANTE_VA );
            model.put( MARK_LISTE_GROUPES, OdsConstants.FILTER_ALL );
            model.put( MARK_LISTE_ELUS, OdsConstants.FILTER_ALL );
        }

        // Cas où l'élément à créer est de type PDD
        else if ( request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( PDD.MARK_PDD ) )
        {
            model.put( MARK_TYPE_ACTION_CREATION_ELEMENT, PDD.MARK_PDD );

            List<GroupePolitique> listGroupePolitique = GroupePolitiqueHome.findGroupePolitiqueList( getPlugin(  ) );
            model.put( MARK_LISTE_GROUPES, listGroupePolitique );

            List<Elu> listElus = EluHome.findEluList( getPlugin(  ) );
            model.put( MARK_LISTE_ELUS, listElus );
        }

        // si le type de l'élément à créer n'est pas bon => ERREUR
        else
        {
            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_RELEVES, getLocale(  ), model );

            return getAdminPage( template.getHtml(  ) );
        }

        // Si on reçoit en paramètre item_selected=none => on efface en session VA et PDD
        if ( ( request.getParameter( OdsParameters.ITEM_SELECTED ) != null ) &&
                request.getParameter( OdsParameters.ITEM_SELECTED ).trim(  ).equals( "none" ) )
        {
            _voeuAmendementSelectedBean = null;
            _pddSelectedBean = null;
        }

        // Si le VA ou le PDD en session n'est pas nul on la met dans la hashmap pour le template
        if ( _voeuAmendementSelectedBean != null )
        {
            model.put( MARK_VA_SELECTED, _voeuAmendementSelectedBean );
        }

        if ( _pddSelectedBean != null )
        {
            model.put( MARK_PDD_SELECTED, _pddSelectedBean );
        }

        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );

        // Appel de la template
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATION_ELEMENT_RELEVE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Crée un élément de relevé de travaux dans la base de données.
     * @param request la requete Http
     * @return l'url de retour
     */
    public String doCreationElementReleve( HttpServletRequest request )
    {
        ElementReleveDesTravaux elementReleve = new ElementReleveDesTravaux(  );

        // Traitement du nombre du RELEVE DES TRAVAUX
        if ( _releveDesTravauxBean == null )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        elementReleve.setReleve( _releveDesTravauxBean );

        if ( ( request.getParameter( OdsParameters.A_FAIRE ) != null ) &&
                !request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_CANCEL ) )
        {
            // Traitement du nombre de POUR
            if ( ( request.getParameter( OdsParameters.NOMBRE_POUR ) == null ) ||
                    request.getParameter( OdsParameters.NOMBRE_POUR ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                elementReleve.setPour( 0 );
            }
            else
            {
                try
                {
                    elementReleve.setPour( Integer.parseInt( request.getParameter( OdsParameters.NOMBRE_POUR ).trim(  ) ) );
                }
                catch ( NumberFormatException ne )
                {
                    elementReleve.setPour( 0 );
                }
            }

            // Traitement du nombre de CONTRE
            if ( ( request.getParameter( OdsParameters.NOMBRE_CONTRE ) == null ) ||
                    request.getParameter( OdsParameters.NOMBRE_CONTRE ).trim(  )
                               .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                elementReleve.setContre( 0 );
            }
            else
            {
                try
                {
                    elementReleve.setContre( Integer.parseInt( 
                            request.getParameter( OdsParameters.NOMBRE_CONTRE ).trim(  ) ) );
                }
                catch ( NumberFormatException ne )
                {
                    elementReleve.setContre( 0 );
                }
            }

            // Traitement du nombre d'ABSTENTION
            if ( ( request.getParameter( OdsParameters.NOMBRE_ABSTENTION ) == null ) ||
                    request.getParameter( OdsParameters.NOMBRE_ABSTENTION ).trim(  )
                               .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                elementReleve.setAbstention( 0 );
            }
            else
            {
                try
                {
                    elementReleve.setAbstention( Integer.parseInt( 
                            request.getParameter( OdsParameters.NOMBRE_ABSTENTION ).trim(  ) ) );
                }
                catch ( NumberFormatException ne )
                {
                    elementReleve.setAbstention( 0 );
                }
            }

            // Traitement du nombre de NE POUVANT PAS VOTER
            if ( ( request.getParameter( OdsParameters.NOMBRE_NPPV ) == null ) ||
                    request.getParameter( OdsParameters.NOMBRE_NPPV ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                elementReleve.setNePouvantPasVoter( 0 );
            }
            else
            {
                try
                {
                    elementReleve.setNePouvantPasVoter( Integer.parseInt( 
                            request.getParameter( OdsParameters.NOMBRE_NPPV ).trim(  ) ) );
                }
                catch ( NumberFormatException ne )
                {
                    elementReleve.setNePouvantPasVoter( 0 );
                }
            }

            // Traitement des OBSERVATIONS
            String strObservations = null;
            strObservations = request.getParameter( OdsParameters.OBJET ).trim(  );
            elementReleve.setObservations( strObservations );

            if ( ( request.getParameter( OdsParameters.A_FAIRE ) != null ) &&
                    request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_VA ) )
            {
                /*
                 * Cas où la création concerne un Voeu ou un Amendement
                 */

                // Si aucun VA n'a été selectionné
                if ( _voeuAmendementSelectedBean == null )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_NO_VA_SELECTED, AdminMessage.TYPE_STOP );
                }

                elementReleve.setVoeuAmendement( _voeuAmendementSelectedBean );
            }
            else if ( ( request.getParameter( OdsParameters.A_FAIRE ) != null ) &&
                    request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( PDD.MARK_PDD ) )
            {
                /*
                 * Cas où la création concerne un Projet ou une Proposition de Délibération
                 */

                // Si aucun PDD n'a été selectionné
                if ( _pddSelectedBean == null )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_NO_PDD_SELECTED, AdminMessage.TYPE_STOP );
                }

                GroupePolitique groupe = null;
                Elu elu = null;

                if ( ( request.getParameter( OdsParameters.ID_GROUPE ) != null ) &&
                        !request.getParameter( OdsParameters.ID_GROUPE ).trim(  )
                                    .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) &&
                        ( request.getParameter( OdsParameters.ID_ELU ) != null ) &&
                        !request.getParameter( OdsParameters.ID_ELU ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    // Traitement du GROUPE
                    int nIdGroupe = -1;

                    try
                    {
                        nIdGroupe = Integer.parseInt( request.getParameter( OdsParameters.ID_GROUPE ).trim(  ) );
                    }
                    catch ( NumberFormatException nfe )
                    {
                        AppLogService.error( nfe );
                    }

                    groupe = GroupePolitiqueHome.findByPrimaryKey( nIdGroupe, getPlugin(  ) );

                    if ( groupe == null )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_NO_GROUPE, AdminMessage.TYPE_STOP );
                    }

                    // Traitement de l'ELU
                    int nIdElu = -1;

                    try
                    {
                        nIdElu = Integer.parseInt( request.getParameter( OdsParameters.ID_ELU ).trim(  ) );
                    }
                    catch ( NumberFormatException nfe )
                    {
                        AppLogService.error( nfe );
                    }

                    elu = EluHome.findByPrimaryKey( nIdElu, getPlugin(  ) );

                    if ( elu == null )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_NO_ELU, AdminMessage.TYPE_STOP );
                    }
                }

                elementReleve.setGroupe( groupe );
                elementReleve.setElu( elu );

                elementReleve.setProjetDeliberation( _pddSelectedBean );
            }
            else
            {
                /*
                 * Cas où la création ne concerne ni un VA ni un PDD
                 */
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            ElementReleveDesTravauxHome.create( elementReleve, getPlugin(  ) );

            _releveDesTravauxBean = null;
            _voeuAmendementSelectedBean = null;
            _pddSelectedBean = null;
        }

        return AppPathService.getBaseUrl( request ) + JSP_URL_GET_MODIFICATION_RELEVE +
        elementReleve.getReleve(  ).getIdReleveDesTravaux(  );
    }

    /**
     * Affiche la page d'où se font les modifications d'un élément de relevé de travaux.
     * @param request la reqte Http
     * @return le contenu de la template
     */
    public String getModificationElementReleve( HttpServletRequest request )
    {
        // Initialisation
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_ELEMENT_RELEVE );

        HashMap<String, Object> model = new HashMap<String, Object>(  );
        boolean isNewVA = false;
        boolean isNewPDD = false;

        // Si l'id d'un élément de relevé de travaux est donné en paramètre on crée un élément à partir de cela
        if ( ( request.getParameter( OdsParameters.ID_ELEMENT_RELEVE ) != null ) &&
                !request.getParameter( OdsParameters.ID_ELEMENT_RELEVE ).trim(  )
                            .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            int nIdElementReleve = -1;

            try
            {
                nIdElementReleve = Integer.parseInt( request.getParameter( OdsParameters.ID_ELEMENT_RELEVE ).trim(  ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            _elementReleveDesTravauxBean = ElementReleveDesTravauxHome.findByPrimaryKey( nIdElementReleve, getPlugin(  ) );

            if ( ( _elementReleveDesTravauxBean.getVoeuAmendement(  ) != null ) &&
                    ( _elementReleveDesTravauxBean.getProjetDeliberation(  ) == null ) )
            {
                isNewVA = true;
            }
            else if ( ( _elementReleveDesTravauxBean.getVoeuAmendement(  ) == null ) &&
                    ( _elementReleveDesTravauxBean.getProjetDeliberation(  ) != null ) )
            {
                isNewPDD = true;
            }
        }

        // Si l'élément de relevé de travaux en session est null => ERREUR
        if ( _elementReleveDesTravauxBean == null )
        {
            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_RELEVES, getLocale(  ), model );

            return getAdminPage( template.getHtml(  ) );
        }

        // On ajoute l'élément de relevé de travaux dans la hashmap
        model.put( MARK_ELEMENT_RELEVE, _elementReleveDesTravauxBean );

        // Cas où l'élément de relevé de travaux référence un Voeu ou un Amendement
        if ( ( _elementReleveDesTravauxBean.getVoeuAmendement(  ) != null ) ||
                ( _elementReleveDesTravauxBean.getProjetDeliberation(  ) == null ) )
        {
            model.put( MARK_TYPE_ACTION_CREATION_ELEMENT, CONSTANTE_VA );
        }

        // Cas où l'élément de relevé de travaux référence un PDD
        if ( ( _elementReleveDesTravauxBean.getProjetDeliberation(  ) != null ) ||
                ( _elementReleveDesTravauxBean.getVoeuAmendement(  ) == null ) )
        {
            model.put( MARK_TYPE_ACTION_CREATION_ELEMENT, PDD.MARK_PDD );

            List<GroupePolitique> listGroupePolitique = GroupePolitiqueHome.findGroupePolitiqueList( getPlugin(  ) );
            model.put( MARK_LISTE_GROUPES, listGroupePolitique );

            List<Elu> listElus = EluHome.findEluList( getPlugin(  ) );
            model.put( MARK_LISTE_ELUS, listElus );
        }

        // Si on reçoit en paramètre item_selected=none => on efface en session VA et PDD
        if ( ( request.getParameter( OdsParameters.ITEM_SELECTED ) != null ) &&
                request.getParameter( OdsParameters.ITEM_SELECTED ).trim(  ).equals( "none" ) )
        {
            _voeuAmendementSelectedBean = null;
            _pddSelectedBean = null;
        }

        // Sinon si VA ou PDD en session est nul, alors on met en session le VA ou le PDD dont l'id est référencé par l'élément
        else
        {
            if ( isNewVA ||
                    ( ( _voeuAmendementSelectedBean == null ) &&
                    ( _elementReleveDesTravauxBean.getVoeuAmendement(  ) != null ) ) )
            {
                _voeuAmendementSelectedBean = VoeuAmendementHome.findByPrimaryKey( _elementReleveDesTravauxBean.getVoeuAmendement(  )
                                                                                                               .getIdVoeuAmendement(  ),
                        getPlugin(  ) );
            }
            else if ( isNewPDD ||
                    ( ( _pddSelectedBean == null ) && ( _elementReleveDesTravauxBean.getProjetDeliberation(  ) != null ) ) )
            {
                _pddSelectedBean = PDDHome.findByPrimaryKey( _elementReleveDesTravauxBean.getProjetDeliberation(  )
                                                                                         .getIdPdd(  ), getPlugin(  ) );
            }
        }

        // Si le VA ou le PDD en session n'est pas nul on la met dans la hashmap pour le template
        if ( _voeuAmendementSelectedBean != null )
        {
            model.put( MARK_VA_SELECTED, _voeuAmendementSelectedBean );
        }

        if ( _pddSelectedBean != null )
        {
            model.put( MARK_PDD_SELECTED, _pddSelectedBean );
        }

        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );

        // Appel de la template
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_ELEMENT_RELEVE, getLocale(  ),
                model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Mets à jour l'élément de relevé de travaux dans la base de données.
     * @param request la requete Http
     * @return l'url de retour
     */
    public String doModificationElementReleve( HttpServletRequest request )
    {
        ElementReleveDesTravaux elementReleve = new ElementReleveDesTravaux(  );

        // Traitement du nombre du RELEVE DES TRAVAUX
        if ( _releveDesTravauxBean == null )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        elementReleve.setReleve( _releveDesTravauxBean );

        if ( ( request.getParameter( OdsParameters.A_FAIRE ) != null ) &&
                !request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_CANCEL ) )
        {
            // Traitement du nombre du RELEVE DES TRAVAUX
            if ( _elementReleveDesTravauxBean == null )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            elementReleve.setIdElementReleveDesTravaux( _elementReleveDesTravauxBean.getIdElementReleveDesTravaux(  ) );

            // Traitement du nombre de POUR
            if ( ( request.getParameter( OdsParameters.NOMBRE_POUR ) == null ) ||
                    request.getParameter( OdsParameters.NOMBRE_POUR ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                elementReleve.setPour( 0 );
            }
            else
            {
                try
                {
                    elementReleve.setPour( Integer.parseInt( request.getParameter( OdsParameters.NOMBRE_POUR ).trim(  ) ) );
                }
                catch ( NumberFormatException ne )
                {
                    elementReleve.setPour( 0 );
                }
            }

            // Traitement du nombre de CONTRE
            if ( ( request.getParameter( OdsParameters.NOMBRE_CONTRE ) == null ) ||
                    request.getParameter( OdsParameters.NOMBRE_CONTRE ).trim(  )
                               .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                elementReleve.setContre( 0 );
            }
            else
            {
                try
                {
                    elementReleve.setContre( Integer.parseInt( 
                            request.getParameter( OdsParameters.NOMBRE_CONTRE ).trim(  ) ) );
                }
                catch ( NumberFormatException ne )
                {
                    elementReleve.setContre( 0 );
                }
            }

            // Traitement du nombre d'ABSTENTION
            if ( ( request.getParameter( OdsParameters.NOMBRE_ABSTENTION ) == null ) ||
                    request.getParameter( OdsParameters.NOMBRE_ABSTENTION ).trim(  )
                               .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                elementReleve.setAbstention( 0 );
            }
            else
            {
                try
                {
                    elementReleve.setAbstention( Integer.parseInt( 
                            request.getParameter( OdsParameters.NOMBRE_ABSTENTION ).trim(  ) ) );
                }
                catch ( NumberFormatException ne )
                {
                    elementReleve.setAbstention( 0 );
                }
            }

            // Traitement du nombre de NE POUVANT PAS VOTER
            if ( ( request.getParameter( OdsParameters.NOMBRE_NPPV ) == null ) ||
                    request.getParameter( OdsParameters.NOMBRE_NPPV ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                elementReleve.setNePouvantPasVoter( 0 );
            }
            else
            {
                try
                {
                    elementReleve.setNePouvantPasVoter( Integer.parseInt( 
                            request.getParameter( OdsParameters.NOMBRE_NPPV ).trim(  ) ) );
                }
                catch ( NumberFormatException ne )
                {
                    elementReleve.setNePouvantPasVoter( 0 );
                }
            }

            // Traitement des OBSERVATIONS
            elementReleve.setObservations( request.getParameter( OdsParameters.OBJET ).trim(  ) );

            if ( ( request.getParameter( OdsParameters.A_FAIRE ) != null ) &&
                    request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_VA ) )
            {
                /*
                 * Cas où la création concerne un Voeu ou un Amendement
                 */

                // Si aucun VA n'a été selectionné
                if ( _voeuAmendementSelectedBean == null )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_NO_VA_SELECTED, AdminMessage.TYPE_STOP );
                }

                elementReleve.setVoeuAmendement( _voeuAmendementSelectedBean );
            }
            else if ( ( request.getParameter( OdsParameters.A_FAIRE ) != null ) &&
                    request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( PDD.MARK_PDD ) )
            {
                /*
                 * Cas où la création concerne un Projet ou une Proposition de Délibération
                 */

                // Si aucun PDD n'a été selectionné
                if ( _pddSelectedBean == null )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_NO_PDD_SELECTED, AdminMessage.TYPE_STOP );
                }

                GroupePolitique groupe = null;
                Elu elu = null;

                if ( ( request.getParameter( OdsParameters.ID_GROUPE ) != null ) &&
                        !request.getParameter( OdsParameters.ID_GROUPE ).trim(  )
                                    .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    // Traitement du GROUPE
                    int nIdGroupe = -1;

                    try
                    {
                        nIdGroupe = Integer.parseInt( request.getParameter( OdsParameters.ID_GROUPE ).trim(  ) );
                    }
                    catch ( NumberFormatException nfe )
                    {
                        AppLogService.error( nfe );
                    }

                    groupe = GroupePolitiqueHome.findByPrimaryKey( nIdGroupe, getPlugin(  ) );

                    if ( groupe == null )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_NO_GROUPE, AdminMessage.TYPE_STOP );
                    }
                }

                if ( ( request.getParameter( OdsParameters.ID_ELU ) != null ) &&
                        !request.getParameter( OdsParameters.ID_ELU ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    // Traitement de l'ELU
                    int nIdElu = -1;

                    try
                    {
                        nIdElu = Integer.parseInt( request.getParameter( OdsParameters.ID_ELU ).trim(  ) );
                    }
                    catch ( NumberFormatException nfe )
                    {
                        AppLogService.error( nfe );
                    }

                    elu = EluHome.findByPrimaryKey( nIdElu, getPlugin(  ) );

                    if ( elu == null )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_NO_ELU, AdminMessage.TYPE_STOP );
                    }
                }

                elementReleve.setGroupe( groupe );
                elementReleve.setElu( elu );

                elementReleve.setProjetDeliberation( _pddSelectedBean );
            }
            else
            {
                /*
                 * Cas où la création ne concerne ni un VA ni un PDD => ERREUR
                 */
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            ElementReleveDesTravauxHome.update( elementReleve, getPlugin(  ) );

            _releveDesTravauxBean = null;
            _elementReleveDesTravauxBean = null;
            _voeuAmendementSelectedBean = null;
            _pddSelectedBean = null;
        }

        return AppPathService.getBaseUrl( request ) + JSP_URL_GET_MODIFICATION_RELEVE +
        elementReleve.getReleve(  ).getIdReleveDesTravaux(  );
    }

    /**
     * Affiche le message de confirmation de suppression d'un élement du relevé des travaux.
     * @param request la requete Http
     * @return le contenu de la template
     */
    public String getSuppressionElementReleve( HttpServletRequest request )
    {
        if ( null != request.getParameter( OdsParameters.ID_ELEMENT_RELEVE ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_RELEVE );

            String strElmtReleveID = request.getParameter( OdsParameters.ID_ELEMENT_RELEVE ).trim(  );
            UrlItem url = new UrlItem( JSP_URL_DO_SUPPRESSION_ELEMENT_RELEVE_JSP );
            url.addParameter( OdsParameters.ID_ELEMENT_RELEVE, strElmtReleveID );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_ELEMENT_RELEVE, url.getUrl(  ),
                AdminMessage.TYPE_CONFIRMATION );
        }
        else
        {
            return getHomeUrl( request );
        }
    }

    /**
     * Supprime un élément du relevé des travaux.
     * @param request la requete Http
     * @return l'url de retour
     */
    public String doSuppressionElementReleve( HttpServletRequest request )
    {
        ElementReleveDesTravaux elmtReleve = null;

        if ( null != request.getParameter( OdsParameters.ID_ELEMENT_RELEVE ) )
        {
            try
            {
                elmtReleve = ElementReleveDesTravauxHome.findByPrimaryKey( Integer.parseInt( 
                            request.getParameter( OdsParameters.ID_ELEMENT_RELEVE ).trim(  ) ), getPlugin(  ) );
            }
            catch ( NumberFormatException ne )
            {
                return getHomeUrl( request );
            }

            try
            {
                ElementReleveDesTravauxHome.remove( elmtReleve, getPlugin(  ) );
            }
            catch ( AppException ae )
            {
                if ( ae.getInitialException(  ) instanceof SQLException )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_ELEMENT_RELEVE,
                        AdminMessage.TYPE_STOP );
                }
            }
        }

        return AppPathService.getBaseUrl( request ) + JSP_URL_GET_MODIFICATION_RELEVE +
        elmtReleve.getReleve(  ).getIdReleveDesTravaux(  );
    }

    /**
     * Sélectionne un Voeu ou un Amendement à référencer par l'élément de relevé de travaux
     * @param request la requete Http
     * @return l'url de retour
     */
    public String doSelectionVoeuAmendement( HttpServletRequest request )
    {
        // Si l'action effectuée est "Annuler" on en fait rien.
        if ( ( request.getParameter( OdsParameters.A_FAIRE ) != null ) &&
                !request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_CANCEL ) )
        {
            // Si l'id de l'amendement n'est pas donné en paramètre => Retour à la case départ
            if ( ( request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ) == null ) ||
                    request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ).trim(  )
                               .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            // On récupère l'id de l'amendement et si il est invalide => retour à la case départ
            int nIdVA = -1;

            try
            {
                nIdVA = Integer.parseInt( request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ).trim(  ) );
            }
            catch ( NumberFormatException ne )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            if ( nIdVA == -1 )
            {
                return getHomeUrl( request );
            }

            // On met en session l'amendement dont on a récupéré l'id 
            _voeuAmendementSelectedBean = VoeuAmendementHome.findByPrimaryKey( nIdVA, getPlugin(  ) );
        }

        // Si l'élément en session est null => Affichage de la page de création
        if ( _elementReleveDesTravauxBean == null )
        {
            return ( AppPathService.getBaseUrl( request ) + JSP_URL_GET_CREATION_ELEMENT_RELEVE + "?" +
            OdsParameters.PLUGIN_NAME + "&" + OdsParameters.A_FAIRE + "=va" );
        }

        // Sinon c'est une modification de cet élément => affichage de la page de modification
        else
        {
            return ( AppPathService.getBaseUrl( request ) + JSP_URL_GET_MODIFICATION_ELEMENT_RELEVE + "?" +
            OdsParameters.PLUGIN_NAME + "&" + OdsParameters.A_FAIRE + "=va" );
        }
    }

    /**
     * Désélectionne un Voeu ou un Amendement de l'élément de relevé de travaux
     * @param request la requete Http
     * @return l'url de retour
     */
    public String doDeselectionVoeuAmendement( HttpServletRequest request )
    {
        // On met en session l'amendement dont on a récupéré l'id 
        _voeuAmendementSelectedBean = null;

        // Si l'élément en session est null => Affichage de la page de création
        if ( _elementReleveDesTravauxBean == null )
        {
            return ( AppPathService.getBaseUrl( request ) + JSP_URL_GET_CREATION_ELEMENT_RELEVE + "?" +
            OdsParameters.PLUGIN_NAME + "&" + OdsParameters.A_FAIRE + "=va&" + OdsParameters.ITEM_SELECTED + "=none" );
        }

        // Sinon c'est une modification de cet élément => affichage de la page de modification
        return ( AppPathService.getBaseUrl( request ) + JSP_URL_GET_MODIFICATION_ELEMENT_RELEVE + "?" +
        OdsParameters.PLUGIN_NAME + "&" + OdsParameters.A_FAIRE + "=va&" + OdsParameters.ITEM_SELECTED + "=none" );
    }

    /**
     * Sélectionne un PDD à référencer par l'élément de relevé de travaux
     * @param request la requete Http
     * @return l'url de retour
     */
    public String doSelectionPDD( HttpServletRequest request )
    {
        // Si l'action effectuée est "Annuler" on en fait rien.
        if ( ( request.getParameter( OdsParameters.A_FAIRE ) != null ) &&
                !request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_CANCEL ) )
        {
            // Si l'id du pdd n'est pas donné en paramètre => Retour à la case départ
            if ( ( request.getParameter( OdsParameters.ID_PDD ) == null ) ||
                    request.getParameter( OdsParameters.ID_PDD ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            // On récupère l'id du pdd et si il est invalide => retour à la case départ
            int nIdPDD = -1;

            try
            {
                nIdPDD = Integer.parseInt( request.getParameter( OdsParameters.ID_PDD ).trim(  ) );
            }
            catch ( NumberFormatException ne )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            if ( nIdPDD == -1 )
            {
                return getHomeUrl( request );
            }

            // On met en session le pdd dont on a récupéré l'id 
            _pddSelectedBean = PDDHome.findByPrimaryKey( nIdPDD, getPlugin(  ) );
        }

        // Si l'élément en session est null => Affichage de la page de création
        if ( _elementReleveDesTravauxBean == null )
        {
            return ( AppPathService.getBaseUrl( request ) + JSP_URL_GET_CREATION_ELEMENT_RELEVE + "?" +
            OdsParameters.PLUGIN_NAME + "&" + OdsParameters.A_FAIRE + "=pdd" );
        }

        // Sinon c'est une modification de cet élément => affichage de la page de modification
        else
        {
            return ( AppPathService.getBaseUrl( request ) + JSP_URL_GET_MODIFICATION_ELEMENT_RELEVE + "?" +
            OdsParameters.PLUGIN_NAME + "&" + OdsParameters.A_FAIRE + "=pdd" );
        }
    }

    /**
     * Désélectionne un PDD de l'élément de relevé de travaux
     * @param request la requete Http
     * @return l'url de retour
     */
    public String doDeselectionPDD( HttpServletRequest request )
    {
        // On met en session l'amendement dont on a récupéré l'id 
        _pddSelectedBean = null;

        // Si l'élément en session est null => Affichage de la page de création
        if ( _elementReleveDesTravauxBean == null )
        {
            return ( AppPathService.getBaseUrl( request ) + JSP_URL_GET_CREATION_ELEMENT_RELEVE + "?" +
            OdsParameters.PLUGIN_NAME + "&" + OdsParameters.A_FAIRE + "=pdd&" + OdsParameters.ITEM_SELECTED + "=none" );
        }

        // Sinon c'est une modification de cet élément => affichage de la page de modification
        else
        {
            return ( AppPathService.getBaseUrl( request ) + JSP_URL_GET_MODIFICATION_ELEMENT_RELEVE + "?" +
            OdsParameters.PLUGIN_NAME + "&" + OdsParameters.A_FAIRE + "=pdd&" + OdsParameters.ITEM_SELECTED + "=none" );
        }
    }

    /**
     * Publie en ligne le relevé des travaux dont l'id est donné en paramètre.
     * @param nIdReleve l'id du releve des travaux
     * @param request la requete Http
     * @return l'url de retour
     */
    public String doPublication( int nIdReleve, HttpServletRequest request )
    {
        ReleveDesTravaux releve = new ReleveDesTravaux(  );
        releve = ReleveDesTravauxHome.findByPrimaryKey( nIdReleve, getPlugin(  ) );

        if ( ( releve != null ) && !releve.isEnLigne(  ) )
        {
            releve.setEnLigne( true );
            ReleveDesTravauxHome.update( releve, getPlugin(  ) );
        }

        return getHomeUrl( request );
    }

    /**
     * Dépublie le relevé des travaux dont l'id est donné en paramètre.
     * @param nIdReleve l'id du releve des travaux
     * @param request la requete Http
     * @return l'url de retour
     */
    public String doDepublication( int nIdReleve, HttpServletRequest request )
    {
        ReleveDesTravaux releve = new ReleveDesTravaux(  );
        releve = ReleveDesTravauxHome.findByPrimaryKey( nIdReleve, getPlugin(  ) );

        if ( ( releve != null ) && releve.isEnLigne(  ) )
        {
            /*
             * GESTION DES PROFILS
             * -------------------
             * Si l'utilisateur n'est pas autorisé a effectuer cette fonction il est renvoyé
             * vers une page lui indiquant qu'il n'a pas les droits nécessaires.
             */
            if ( !RBACService.isAuthorized( RelevesDesTravauxResourceIdService.RELEVES_DES_TRAVAUX,
                        RBAC.WILDCARD_RESOURCES_ID, RelevesDesTravauxResourceIdService.PERMISSION_DEPUBLICATION,
                        getUser(  ) ) )
            {
                return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                    AdminMessage.TYPE_ERROR );
            }

            releve.setEnLigne( false );
            ReleveDesTravauxHome.update( releve, getPlugin(  ) );
        }

        return getHomeUrl( request );
    }

    /**
     * retourne l'interface de gestion des relevés de travaux.
     * @param request la requete HTTP
     * @return interface de gestion des relevés de travaux
     */
    public String getRelevesList( HttpServletRequest request )
    {
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        /*
         * Pour la gestion des profils
         */
        ajouterPermissionsDansHashmap( model );

        Seance prochaineSeance = SeanceHome.getProchaineSeance( getPlugin(  ) );

        List<ReleveDesTravaux> releves = null;

        if ( prochaineSeance != null )
        {
            releves = ReleveDesTravauxHome.findReleveDesTravauxSeance( prochaineSeance, getPlugin(  ) );
            model.put( Seance.MARK_PROCHAINE_SEANCE, prochaineSeance );
        }
        else
        {
            model.put( Seance.MARK_PROCHAINE_SEANCE, OdsConstants.FILTER_ALL );
        }

        // On récupère la liste des commissions actives
        List<Commission> listCommissions = CommissionHome.findAllCommissionsActives( getPlugin(  ) );

        // Si cette liste est null on met dans la hashmap la valeur OdsConstants.FILTER_ALL 
        if ( listCommissions == null )
        {
            model.put( MARK_LISTE_COMMISSIONS, OdsConstants.FILTER_ALL );
        }

        // Sinon on met dans la hashmap la liste des commissions 
        // qui n'ont pas déjà un relevé de travaux qui le référence
        else if ( releves != null )
        {
            for ( int i = 0; i < listCommissions.size(  ); i++ )
            {
                for ( ReleveDesTravaux rdt : releves )
                {
                    if ( rdt.getCommission(  ).getIdCommission(  ) == listCommissions.get( i ).getIdCommission(  ) )
                    {
                        listCommissions.remove( i );
                    }
                }
            }

            model.put( MARK_LISTE_COMMISSIONS, listCommissions );
        }

        // On ajoute dans la hashmap la liste de relevés de travaux
        model.put( MARK_LISTE_RELEVES, releves );

        // On appelle la template pour afficher la page de listing des relevés de travaux
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_RELEVES, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * génère le PDF
     * @param request la requete Http
     * @return le pdf
     */
    public String doVisualisationReleveDesTravaux( HttpServletRequest request )
    {
        String strIdReleve = request.getParameter( OdsParameters.ID_RELEVE );
        int nIdReleve = -1;
        Plugin plugin = getPlugin(  );
        byte[] tabXmlEntree;

        //creation du fichier pdf du releve de travaux
        if ( strIdReleve != null )
        {
            try
            {
                nIdReleve = Integer.parseInt( strIdReleve );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getHomeUrl( request );
            }

            if ( nIdReleve != -1 )
            {
                ReleveDesTravaux releve = ReleveDesTravauxHome.findByPrimaryKey( nIdReleve, plugin );
                List<ElementReleveDesTravaux> listElementReleve = ElementReleveDesTravauxHome.findElementReleveDesTravauxByReleveVA( releve,
                        plugin );
                VoeuAmendement va;

                for ( ElementReleveDesTravaux element : listElementReleve )
                {
                    if ( element.getVoeuAmendement(  ) != null )
                    {
                        int nIdVA = element.getVoeuAmendement(  ).getIdVoeuAmendement(  );
                        va = VoeuAmendementHome.findByPrimaryKey( nIdVA, getPlugin(  ) );
                        element.setVoeuAmendement( va );
                    }
                }

                releve.setElementReleveDesTravaux( listElementReleve );

                if ( releve.getCommission(  ) != null )
                {
                    try
                    {
                        FichierPhysique newFichierPhysique = new FichierPhysique(  );
                        tabXmlEntree = ( CONSTANTE_ENTETE_XML + releve.getXml( request ) ).getBytes( OdsConstants.UTF8 );

                        ByteArrayOutputStream os = new ByteArrayOutputStream(  );
                        String nomTemplate = AppPropertiesService.getProperty( PROPERTY_RELEVE_NOM_TEMPLATE,
                                DEFAULT_RELEVE_NOM_TEMPLATE );

                        tabXmlEntree = DocumentManager.convert( request, DocumentManager.PDF_CONVERSION, nomTemplate, tabXmlEntree);               

                        //on test si le fichier existe deja en base
                        FichierFilter fichierFilter = new FichierFilter(  );
                        fichierFilter.setIdCommission( releve.getCommission(  ).getIdCommission(  ) );
                        fichierFilter.setIdSeance( releve.getSeance(  ).getIdSeance(  ) );
                        fichierFilter.setIdTypeDocument( TypeDocumentEnum.RELEVE.getId(  ) );

                        List<Fichier> listFichier = FichierHome.findByFilter( fichierFilter, plugin );

                        if ( listFichier.size(  ) == 0 )
                        {
                            //le fichier n'existe pas en base
                            Fichier newFichier = new Fichier(  );
                            newFichierPhysique.setDonnees( tabXmlEntree );
                            newFichierPhysique.setIdFichier( FichierPhysiqueHome.create( newFichierPhysique,
                                    getPlugin(  ) ) );
                            newFichier.setFichier( newFichierPhysique );

                            StringBuffer strIntitule = new StringBuffer(  );
                            strIntitule.append( I18nService.getLocalizedString( PROPERTY_INTITULE_FICHIER, getLocale(  ) ) );

                            strIntitule.append( releve.getCommission(  ).getNumero(  ) );
                            int nNumero = releve.getCommission(  ).getNumero(  );
                            
                            strIntitule.append( I18nService.getLocalizedString( 
                            		( nNumero == 1 ) ? PROPERTY_INTITULE_END1 : PROPERTY_INTITULE_END2,
                            		getLocale(  ) ) );

                            newFichier.setTitre( strIntitule.toString(  ) );
                            newFichier.setNom( strIntitule.toString(  ) );
                            newFichier.setExtension( DocumentManager.PDF_CONVERSION );
                            newFichier.setTaille( tabXmlEntree.length );
                            
                            // Creation de l'objet représentant le Fichier
                            newFichier.setSeance( releve.getSeance(  ) );
                            newFichier.setTypdeDocument( TypeDocumentEnum.RELEVE.getTypeDocumentOnlyWidthId(  ) );
                            newFichier.setEnLigne( true );
                            newFichier.setDatePublication( OdsUtils.getCurrentDate(  ) );

                            newFichier.setCommission( releve.getCommission(  ) );

                            FichierHome.create( newFichier, getPlugin(  ) );
                        }
                        else
                        {
                            //le fichier existe en base
                            Fichier fichierExistant = listFichier.get( 0 );
                            newFichierPhysique = FichierPhysiqueHome.findByPrimaryKey( fichierExistant.getFichier(  )
                                                                                                      .getIdFichier(  ),
                                    getPlugin(  ) );

                            newFichierPhysique.setDonnees( tabXmlEntree );
                            FichierPhysiqueHome.update( newFichierPhysique, getPlugin(  ) );
                            fichierExistant.setFichier( newFichierPhysique );
                            fichierExistant.setTaille( tabXmlEntree.length );
                            fichierExistant.setVersion( fichierExistant.getVersion(  ) + 1 );
                            fichierExistant.setDatePublication( OdsUtils.getCurrentDate(  ) );
                            FichierHome.update( fichierExistant, getPlugin(  ) );

                            //Ajout de l'historique
                            Timestamp dateForVersion = new Timestamp( System.currentTimeMillis(  ) );
                            Historique historique = new Historique(  );
                            historique.setVersion( fichierExistant.getVersion(  ) );
                            historique.setDatePublication( dateForVersion );
                            historique.setIdDocument( fichierExistant.getId(  ) );
                            HistoriqueHome.create( historique, getPlugin(  ) );
                        }
                    }

                    catch ( UnsupportedEncodingException uee )
                    {
                        AppLogService.error( uee );
                    }
                    catch ( IllegalArgumentException iae )
                    {
                        AppLogService.error( iae );
                    }
                    catch ( UnavailableException ue )
                    {
                        AppLogService.error( ue );
                    }
                    catch ( ServerException se )
                    {
                        AppLogService.error( se );
                    }
                    catch ( IOException ioe )
                    {
                        AppLogService.error( ioe );
                    }

                    return AppPathService.getBaseUrl( request ) + JSP_URL_GET_MODIFICATION_RELEVE + nIdReleve;
                }
            }
        }

        return getHomeUrl( request );
    }

    /**
     * Genere le fichier PDF des releves des travaux
     *  de la prochaine séance, pour une formation de conseil donnée.
     *  
     * @param request La requête HTTP
     * 
     * @throws DocumentException  documentException
     * @throws IOException iOException
     * 
     * @return l'url de retour
     */
    public String doGenereRelevePdf( HttpServletRequest request )
        throws DocumentException, IOException
    {
        Locale locale = request.getLocale(  );
        String strIdReleve = request.getParameter( OdsParameters.ID_RELEVE );
        int nIdReleve = -1;
        Plugin plugin = getPlugin(  );

        //creation du fichier pdf du releve de travaux
        if ( strIdReleve != null )
        {
            try
            {
                nIdReleve = Integer.parseInt( strIdReleve );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getHomeUrl( request );
            }

            if ( nIdReleve != -1 )
            {
                FichierPhysique fichierPhysique;
                Fichier fichierInitial;
                ReleveDesTravaux releve = ReleveDesTravauxHome.findByPrimaryKey( nIdReleve, plugin );
                List<ElementReleveDesTravaux> listElementReleve = ElementReleveDesTravauxHome.findElementReleveDesTravauxByReleveVA( releve,
                        plugin );

                //creation de la liasse
                int nPageOffset = 0;
                Document document = null;
                PdfCopy writer = null;
                // etape 1: creation du document de sortie
                document = new Document(  );

                ByteArrayOutputStream byteArrayOutputStreamWriter = new ByteArrayOutputStream(  );
                writer = new PdfCopy( document, byteArrayOutputStreamWriter );

                BaseFont bf = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED );
                document.open(  );

                PdfReader pdfReader = insertEnteteInThePdfReleve( releve, locale );
                nPageOffset = OrdreDuJourUtils.concatenePdfReader( pdfReader, writer, nPageOffset );

                for ( ElementReleveDesTravaux elementReleveDesTravaux : listElementReleve )
                {
                    if ( elementReleveDesTravaux.getVoeuAmendement(  ) != null )
                    {
                        //on recupere le  va de l'entree
                        elementReleveDesTravaux.setVoeuAmendement( VoeuAmendementHome.findByPrimaryKey( 
                                elementReleveDesTravaux.getVoeuAmendement(  ).getIdVoeuAmendement(  ), plugin ) );
                        fichierInitial = FichierHome.findByPrimaryKey( elementReleveDesTravaux.getVoeuAmendement(  )
                                                                                              .getFichier(  ).getId(  ),
                                plugin );
                        fichierPhysique = FichierPhysiqueHome.findByPrimaryKey( fichierInitial.getFichier(  )
                                                                                              .getIdFichier(  ), plugin );

                        if ( fichierPhysique != null )
                        {
                            PdfReader reader;

                            if ( elementReleveDesTravaux.getVoeuAmendement(  ).getEnLigne(  ) )
                            {
                                reader = new PdfReader( fichierPhysique.getDonnees(  ) );
                            }
                            else
                            {
                                Document documentVide = new Document(  );
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
                                PdfWriter writerVide = PdfWriter.getInstance( documentVide, byteArrayOutputStream );
                                documentVide.open(  );

                                PdfContentByte cb = writerVide.getDirectContent(  );
                                cb.beginText(  );
                                cb.endText(  );
                                documentVide.close(  );
                                byteArrayOutputStream.close(  );
                                reader = new PdfReader( byteArrayOutputStream.toByteArray(  ) );
                            }

                            //positionnement du tampon
                            Rectangle rectangle = reader.getPageSize( 1 );
                            float nX = 500;
                            float nY = 1200;

                            if ( rectangle != null )
                            {
                                nX = reader.getPageSize( 1 ).width(  ) - 80;
                                nY = reader.getPageSize( 1 ).height(  ) - 80;
                            }

                            //on ajoute la référence du voeu ou amendement sur le fichier initial
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
                            PdfStamper stamp = new PdfStamper( reader, byteArrayOutputStream );
                            PdfContentByte over;
                            over = stamp.getOverContent( 1 );
                            over.beginText(  );
                            over.setFontAndSize( bf, 18 );
                            over.showTextAligned( Element.ALIGN_LEFT,
                                elementReleveDesTravaux.getVoeuAmendement(  ).getReferenceComplete(  ), nX, nY, 0 );
                            over.endText(  );
                            stamp.close(  );
                            byteArrayOutputStream.close(  );
                            reader = new PdfReader( byteArrayOutputStream.toByteArray(  ) );
                            //fin de l'ajout de la reference
                            nPageOffset = OrdreDuJourUtils.concatenePdfReader( reader, writer, nPageOffset );
                        }
                    }
                }

                document.close(  );
                byteArrayOutputStreamWriter.close(  );

                //insertion de la liasse en base 
                byte[] tabDonnees = byteArrayOutputStreamWriter.toByteArray(  );

                //on test si le fichier existe deja en base
                FichierPhysique newFichierPhysique = new FichierPhysique(  );
                FichierFilter fichierFilter = new FichierFilter(  );
                fichierFilter.setIdCommission( releve.getCommission(  ).getIdCommission(  ) );
                fichierFilter.setIdSeance( releve.getSeance(  ).getIdSeance(  ) );
                fichierFilter.setIdTypeDocument( TypeDocumentEnum.RELEVE.getId(  ) );

                List<Fichier> listFichier = FichierHome.findByFilter( fichierFilter, plugin );

                int nIdFichier = -1;
                
                if ( listFichier.size(  ) == 0 )
                {
                    //le fichier n'existe pas en base
                    Fichier newFichier = new Fichier(  );
                    newFichierPhysique.setDonnees( tabDonnees );
                    newFichierPhysique.setIdFichier( FichierPhysiqueHome.create( newFichierPhysique, plugin ) );
                    newFichier.setFichier( newFichierPhysique );

                    StringBuffer strIntitule = new StringBuffer(  );
                    strIntitule.append( I18nService.getLocalizedString( PROPERTY_INTITULE_FICHIER, locale ) );

                    if ( releve.getCommission(  ).getNumero(  ) == 1 )
                    {
                        strIntitule.append( releve.getCommission(  ).getNumero(  ) );
                        strIntitule.append( I18nService.getLocalizedString( PROPERTY_INTITULE_END1, locale ) );
                    }
                    else
                    {
                        strIntitule.append( releve.getCommission(  ).getNumero(  ) );
                        strIntitule.append( I18nService.getLocalizedString( PROPERTY_INTITULE_END2, locale ) );
                    }

                    newFichier.setTitre( strIntitule.toString(  ) );
                    newFichier.setNom( strIntitule.toString(  ) );
                    newFichier.setExtension( DocumentManager.PDF_CONVERSION );
                    newFichier.setTaille( tabDonnees.length );
                    // Creation de l'objet représentant le Fichier
                    newFichier.setSeance( releve.getSeance(  ) );
                    newFichier.setTypdeDocument( TypeDocumentEnum.RELEVE.getTypeDocumentOnlyWidthId(  ) );
                    newFichier.setEnLigne( true );
                    newFichier.setDatePublication( OdsUtils.getCurrentDate(  ) );

                    if ( releve.getCommission(  ) != null )
                    {
                        newFichier.setCommission( releve.getCommission(  ) );
                    }

                    nIdFichier = FichierHome.create( newFichier, plugin );                    
                }
                else
                {
                    //le fichier existe en base
                    Fichier fichierExistant = listFichier.get( 0 );
                    nIdFichier = fichierExistant.getId();
                    newFichierPhysique = FichierPhysiqueHome.findByPrimaryKey( fichierExistant.getFichier(  )
                                                                                              .getIdFichier(  ), plugin );

                    newFichierPhysique.setDonnees( tabDonnees );
                    FichierPhysiqueHome.update( newFichierPhysique, getPlugin(  ) );
                    fichierExistant.setFichier( newFichierPhysique );
                    fichierExistant.setTaille( tabDonnees.length );
                    fichierExistant.setVersion( fichierExistant.getVersion(  ) + 1 );
                    fichierExistant.setDatePublication( OdsUtils.getCurrentDate(  ) );
                    FichierHome.update( fichierExistant, getPlugin(  ) );

                    //Ajout de l'historique
                    Timestamp dateForVersion = new Timestamp( System.currentTimeMillis(  ) );
                    Historique historique = new Historique(  );
                    historique.setVersion( fichierExistant.getVersion(  ) );
                    historique.setDatePublication( dateForVersion );
                    historique.setIdDocument( fichierExistant.getId(  ) );
                    HistoriqueHome.create( historique, plugin );
                }
                
                Fichier fichierANotifier = FichierHome.findByPrimaryKey( nIdFichier, plugin );
                OdsUtils.doNotifierUtilisateurs( fichierANotifier, request, plugin, locale );

                return AppPathService.getBaseUrl( request ) + JSP_URL_GET_MODIFICATION_RELEVE + nIdReleve;
            }
        }

        return getHomeUrl( request );
    }

    /**
     * cette methode crée en memoire l'entete du releve sous format pdf
     * @param  ordreDuJour l'ordre du jour
     * @param  locale la locale
     * @return PdfReader un objet pdfReader contenant le document pdf de la liste de rapporteurs
     * @throws DocumentException
     * @throws IOException
     */
    private static PdfReader insertEnteteInThePdfReleve( ReleveDesTravaux releve, Locale locale )
        throws DocumentException, IOException
    {
        BaseFont bf = BaseFont.createFont( BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.EMBEDDED );
        Document document = new Document(  );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
        PdfWriter writer = PdfWriter.getInstance( document, byteArrayOutputStream );
        document.open(  );

        PdfContentByte cb = writer.getDirectContent(  );

        cb.beginText(  );
        cb.setFontAndSize( bf, 16 );
        cb.showTextAligned( PdfContentByte.ALIGN_CENTER,
            "Amendements et voeux déposés en commission " + releve.getCommission(  ).getNumero(  ), 260, 720, 0 );
        cb.endText(  );
        cb.beginText(  );
        cb.setFontAndSize( bf, 14 );
        cb.showTextAligned( PdfContentByte.ALIGN_CENTER, OrdreDuJourUtils.CONSTANTE_ETOILE, 260, 690, 0 );
        cb.endText(  );
        cb.beginText(  );
        cb.setFontAndSize( bf, 16 );
        cb.showTextAligned( PdfContentByte.ALIGN_CENTER,
            I18nService.getLocalizedString( OrdreDuJourUtils.PROPERTY_SEANCE, locale ), 260, 660, 0 );
        cb.endText(  );

        StringBuffer stringBuffer = new StringBuffer(  );
        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );
        String strDateDebut = dateFormat.format( releve.getSeance(  ).getDateSeance(  ) ).toString(  );
        String strDateFin = dateFormat.format( releve.getSeance(  ).getDateCloture(  ) ).toString(  );
        stringBuffer.append( I18nService.getLocalizedString( OrdreDuJourUtils.PROPERTY_DU, locale ) );
        stringBuffer.append( " " );
        stringBuffer.append( strDateDebut );
        stringBuffer.append( " " );
        stringBuffer.append( I18nService.getLocalizedString( OrdreDuJourUtils.PROPERTY_AU, locale ) );
        stringBuffer.append( " " );
        stringBuffer.append( strDateFin );
        stringBuffer.append( " " );

        cb.beginText(  );
        cb.setFontAndSize( bf, 18 );
        cb.showTextAligned( PdfContentByte.ALIGN_CENTER, stringBuffer.toString(  ), 260, 630, 0 );
        cb.endText(  );

        document.close(  );
        byteArrayOutputStream.close(  );

        return new PdfReader( byteArrayOutputStream.toByteArray(  ) );
    }

    /**
     * Permet de stocker toutes les permissions afin de gérer les profils au niveau des templates
     * @param model le hashmap contenant les parametres qui vont être envoyés au template
     */
    private void ajouterPermissionsDansHashmap( Map<Object, Object> model )
    {
        /*
         * Permission de dépublier
         */
        boolean bPermissionDepublication = true;

        if ( !RBACService.isAuthorized( RelevesDesTravauxResourceIdService.RELEVES_DES_TRAVAUX,
                    RBAC.WILDCARD_RESOURCES_ID, RelevesDesTravauxResourceIdService.PERMISSION_DEPUBLICATION, getUser(  ) ) )
        {
            bPermissionDepublication = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_DEPUBLICATION, bPermissionDepublication );

        /*
         * Permission de modifier
         */
        boolean bPermissionModification = true;

        if ( !RBACService.isAuthorized( RelevesDesTravauxResourceIdService.RELEVES_DES_TRAVAUX,
                    RBAC.WILDCARD_RESOURCES_ID, RelevesDesTravauxResourceIdService.PERMISSION_MISE_A_JOUR, getUser(  ) ) )
        {
            bPermissionModification = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_MODIFICATION, bPermissionModification );

        /*
         * Permission de supprimer
         */
        boolean bPermissionSuppression = true;

        if ( !RBACService.isAuthorized( RelevesDesTravauxResourceIdService.RELEVES_DES_TRAVAUX,
                    RBAC.WILDCARD_RESOURCES_ID, RelevesDesTravauxResourceIdService.PERMISSION_SUPPRESSION, getUser(  ) ) )
        {
            bPermissionSuppression = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_SUPPRESSION, bPermissionSuppression );
    }
}
