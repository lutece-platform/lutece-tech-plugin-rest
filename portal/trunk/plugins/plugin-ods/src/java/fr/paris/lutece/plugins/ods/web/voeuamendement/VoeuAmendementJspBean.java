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
package fr.paris.lutece.plugins.ods.web.voeuamendement;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.commission.CommissionHome;
import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.elu.EluHome;
import fr.paris.lutece.plugins.ods.business.fascicule.Fascicule;
import fr.paris.lutece.plugins.ods.business.fascicule.FasciculeHome;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysique;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysiqueHome;
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocumentEnum;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilHome;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitique;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitiqueHome;
import fr.paris.lutece.plugins.ods.business.historique.Historique;
import fr.paris.lutece.plugins.ods.business.historique.HistoriqueHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.EntreeOrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.EntreeOrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourUtils;
import fr.paris.lutece.plugins.ods.business.ordredujour.TypeOrdreDuJourEnum;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ElementReleveDesTravaux;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ElementReleveDesTravauxHome;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravaux;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravauxFilter;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravauxHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.statut.Statut;
import fr.paris.lutece.plugins.ods.business.statut.StatutEnum;
import fr.paris.lutece.plugins.ods.business.statut.StatutFilter;
import fr.paris.lutece.plugins.ods.business.statut.StatutHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementFilter;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementLiasseComparator;
import fr.paris.lutece.plugins.ods.service.role.VoeuxAmendementsResourceIdService;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.constants.OdsProperties;
import fr.paris.lutece.plugins.ods.web.ordredujour.OrdreDuJourJspBean;
import fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean;
import fr.paris.lutece.plugins.ods.web.relevetravaux.ReleveDesTravauxJspBean;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;


/**
 * VoeuAmendementJspBean Classe qui gère les fonctionnalités d’administrations
 * des vœux rattachés, des vœux non rattachés et des amendements
 */
public class VoeuAmendementJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_AMENDEMENT = "ODS_AMENDEMENTS";
    public static final String RIGHT_ODS_VOEU_RATTACHE = "ODS_VOEUXRATTACHES";
    public static final String RIGHT_ODS_VOEU_NON_RATTACHE = "ODS_VOEUX";
    public static final String RIGHT_ODS_VA_NUMEROTES = "ODS_VA_NUMEROTES";
    public static final String RIGHT_ODS_STATUTVA_GESTION_AVAL = "ODS_STATUTVA_GESTION_AVAL";
    public static final String RIGHT_ODS_AMENDEMENT_GESTION_AVAL = "ODS_AMENDEMENTS_GESTION_AVAL";
    public static final String RIGHT_ODS_VOEU_RATTACHE_GESTION_AVAL = "ODS_VOEUXRATTACHES_GESTION_AVAL";
    public static final String RIGHT_ODS_VOEU_NON_RATTACHE_GESTION_AVAL = "ODS_VOEUX_GESTION_AVAL";
    public static final String RIGHT_ODS_VA_NUMEROTES_GESTION_AVAL = "ODS_VA_NUMEROTES_GESTION_AVAL";
    public static final String CONSTANTE_TYPE_A = "A";
    public static final String CONSTANTE_TYPE_LR = "LR";
    public static final String CONSTANTE_TYPE_VR = "V";
    public static final String CONSTANTE_TYPE_VNR = "VNR";
    public static final String CONSTANTE_TYPE_STATUT_VA = "STATUTVA";
    public static final String CONSTANTE_GENERE_CSV = "genere_csv";
    public static final String CONSTANTE_GENERE_PDF = "genere_pdf";
    public static final String CONSTANTE_PARAM_LIASSE = "liasse";
    public static final String CONSTANTE_ACTION_SAVE = "save";
    public static final String CONSTANTE_ACTION_APPLY = "apply";
    public static final String CONSTANTE_ACTION_CANCEL = "cancel";
    public static final String CONSTANTE_ACTION_ADD_PARENT = "add_parent";
    public static final String CONSTANTE_ACTION_STATUT_VA = "statutva";
    public static final String CONSTANTE_ACTION_REMOVE_PARENT = "remove_parent";
    public static final String CONSTANTE_RETOUR_CREATION = "creation";
    public static final String CONSTANTE_RETOUR_MODIFICATION = "modification";
    public static final String CONSTANTE_URL_JSP_APPELANT_PROJET = "jsp/admin/plugins/ods/projetdeliberation/ProjetDeliberationForVa.jsp";
    public static final String CONSTANTE_URL_JSP_APPELANT_PROJET_AVAL = "jsp/admin/plugins/ods/projetdeliberation/ProjetDeliberationForVaGestionAval.jsp";
    public static final String CONSTANTE_URL_JSP_APPELANT_PROPOSITION = "jsp/admin/plugins/ods/projetdeliberation/PropositionDeliberationForVa.jsp";
    public static final String CONSTANTE_URL_JSP_APPELANT_PROPOSITION_AVAL = "jsp/admin/plugins/ods/projetdeliberation/PropositionDeliberationForVaGestionAval.jsp";
    public static final String CONSTANTE_URL_JSP_APPELANT_AMENDEMENT_PARENT_MODIFICATION = "jsp/admin/plugins/ods/voeuamendement/AmendementsForParentModification.jsp";
    public static final String CONSTANTE_URL_JSP_APPELANT_AMENDEMENT_PARENT_CREATION = "jsp/admin/plugins/ods/voeuamendement/AmendementsForParentCreation.jsp";
    public static final String CONSTANTE_URL_JSP_APPELANT_VOEU_RATTACHE_PARENT_MODIFICATION = "jsp/admin/plugins/ods/voeuamendement/VoeuxRattachesForParentModification.jsp";
    public static final String CONSTANTE_URL_JSP_APPELANT_VOEU_RATTACHE_PARENT_CREATION = "jsp/admin/plugins/ods/voeuamendement/VoeuxRattachesForParentCreation.jsp";
    public static final String CONSTANTE_URL_JSP_APPELANT_VOEU_NON_RATTACHE_PARENT_MODIFICATION = "jsp/admin/plugins/ods/voeuamendement/VoeuxNonRattachesForParentModification.jsp";
    public static final String CONSTANTE_URL_JSP_APPELANT_VOEU_NON_RATTACHE_PARENT_CREATION = "jsp/admin/plugins/ods/voeuamendement/VoeuxNonRattachesForParentCreation.jsp";
    public static final String CONSTANTE_URL_JSP_RETOUR_PDDS_IN_AMENDEMENT = "jsp/admin/plugins/ods/voeuamendement/AddPddsInAmendement.jsp";
    public static final String CONSTANTE_URL_JSP_RETOUR_PDDS_IN_VOEURATTACHE = "jsp/admin/plugins/ods/voeuamendement/AddPddsInVoeuRattache.jsp";
    public static final String CONSTANTE_URL_JSP_RETOUR_PARENT_MODIFICATION_AMENDEMENT = "jsp/admin/plugins/ods/voeuamendement/AddParentModificationInAmendement.jsp";
    public static final String CONSTANTE_URL_JSP_RETOUR_PARENT_MODIFICATION_VOEU_RATTACHE = "jsp/admin/plugins/ods/voeuamendement/AddParentModificationInVoeuRattache.jsp";
    public static final String CONSTANTE_URL_JSP_RETOUR_PARENT_MODIFICATION_VOEU_NON_RATTACHE = "jsp/admin/plugins/ods/voeuamendement/AddParentModificationInVoeuNonRattache.jsp";
    public static final String CONSTANTE_URL_JSP_RETOUR_PARENT_CREATION_AMENDEMENT = "jsp/admin/plugins/ods/voeuamendement/AddParentCreationInAmendement.jsp";
    public static final String CONSTANTE_URL_JSP_RETOUR_PARENT_CREATION_VOEU_RATTACHE = "jsp/admin/plugins/ods/voeuamendement/AddParentCreationInVoeuRattache.jsp";
    public static final String CONSTANTE_URL_JSP_RETOUR_PARENT_CREATION_VOEU_NON_RATTACHE = "jsp/admin/plugins/ods/voeuamendement/AddParentCreationInVoeuNonRattache.jsp";
    public static final String JSP_URL_LIASSE = "jsp/admin/plugins/ods/voeuamendement/VaNumerotes.jsp";
    
    private static final String CONSTANTE_CANCEL = "cancel";
    private static final String CONSTANTE_TYPE_ALLV = "VRVNR";
    private static final String CONSTANTE_ANNULER = "annuler";
    private static final String TEMPLATE_HISTORIQUE = "admin/plugins/ods/historique/historique.html";
    private static final String TEMPLATE_LISTE_AMENDEMENT = "admin/plugins/ods/voeuamendement/liste_amendements.html";
    private static final String TEMPLATE_CREATION_AMENDEMENT = "admin/plugins/ods/voeuamendement/creation_amendement.html";
    private static final String TEMPLATE_MODIFICATION_AMENDEMENT = "admin/plugins/ods/voeuamendement/modification_amendement.html";
    private static final String TEMPLATE_LISTE_VOEU_RATTACHE = "admin/plugins/ods/voeuamendement/liste_voeux_rattaches.html";
    private static final String TEMPLATE_CREATION_VOEU_RATTACHE = "admin/plugins/ods/voeuamendement/creation_voeu_rattache.html";
    private static final String TEMPLATE_MODIFICATION_VOEU_RATTACHE = "admin/plugins/ods/voeuamendement/modification_voeu_rattache.html";
    private static final String TEMPLATE_MODIFICATION_STATUT_VA = "admin/plugins/ods/voeuamendement/modification_statut_va.html";
    private static final String TEMPLATE_LISTE_VOEU_NON_RATACHE = "admin/plugins/ods/voeuamendement/liste_voeux_non_rattaches.html";
    private static final String TEMPLATE_CREATION_VOEU_NON_RATACHE = "admin/plugins/ods/voeuamendement/creation_voeu_non_rattache.html";
    private static final String TEMPLATE_MODIFICATION_VOEU_NON_RATACHE = "admin/plugins/ods/voeuamendement/modification_voeu_non_rattache.html";
    private static final String TEMPLATE_LIASSE = "admin/plugins/ods/voeuamendement/liasse.html";
    private static final String TEMPLATE_LISTE_AMENDEMENT_SELECTION = "admin/plugins/ods/voeuamendement/liste_amendements_selection.html";
    private static final String TEMPLATE_LISTE_VOEU_RATTACHE_SELECTION = "admin/plugins/ods/voeuamendement/liste_voeux_rattaches_selection.html";
    private static final String TEMPLATE_LISTE_VOEU_NON_RATACHE_SELECTION = "admin/plugins/ods/voeuamendement/liste_voeux_non_rattaches_selection.html";
    private static final String TEMPLATE_LISTE_VOEU_NON_RATACHE_SELECTION_FOR_ODJ = "admin/plugins/ods/voeuamendement/liste_voeux_non_rattaches_selection_for_odj.html";
    private static final String MESSAGE_LA_REFERENCE_EXISTE_DEJA_DANS_LE_FASCICULE = "ods.message.voeuamendement.referencealreadyexist";
    private static final String MESSAGE_CONFIRM_ASSIGN_STATUT = "ods.message.confirmAssignStatut";
    private static final String MESSAGE_CONFIRMDELETEVOEU = "ods.message.confirmdeletevoeu";
    private static final String MESSAGE_PAS_DE_PROJET_CHOISIE = "ods.voeuamendement.message.pasdeprojetchoisie";
    private static final String MESSAGE_PAS_DE_PROPOSITION_CHOISIE = "ods.voeuamendement.message.pasdepropositionchoisie";
    private static final String MESSAGE_LABEL_FICHIER_NO_PDF = "ods.voeuamendement.message.fichier.nopdf";
    private static final String MESSAGE_NO_NUMERO_VOEU_ADOPTE = "ods.voeuamendement.message.nonumerovoeuadopte";
    private static final String MESSAGE_CONFIRMDELETEAMENDEMENT = "ods.message.confirmdeleteamendement";
    private static final String MESSAGE_AVERTISSEMENT_ENREGISTREMENT = "ods.voeuamendement.message.avertissementEnregistrement";
    private static final String MESSAGE_AVERTISSEMENT_PUBLICATION = "ods.voeuamendement.message.avertissementPublication";
    private static final String MESSAGE_REFERENCE_TOO_LONG = "ods.voeuamendement.message.refTooLong";
    private static final String MESSAGE_NO_PARENT_SELECTED = "ods.voeuamendement.message.noparentselected";
    private static final String MARK_LISTE_FORMATION_CONSEIL = "liste_formation_conseil";
    private static final String MARK_LISTE_COMMISSION = "liste_commission";
    private static final String MARK_LISTE_GROUPE = "liste_groupe";
    private static final String MARK_LISTE_PUBLIE = "liste_publie";
    private static final String MARK_LISTE_ELU = "liste_elu";
    private static final String MARK_LISTE_ELU_A_AFFICHER = "liste_elu_a_afficher";
    private static final String MARK_LISTE_FASCICULE_A_AFFICHER = "liste_fascicule_a_afficher";
    private static final String MARK_LISTE_FASCICULE = "liste_fascicule";
    private static final String MARK_LISTE_VOEUAMENDEMENT = "liste_voeuamendement";
    private static final String MARK_LISTE_SEANCES = "liste_seances";
    private static final String MARK_LISTE_STATUTS = "liste_statuts";
    private static final String MARK_LISTE_STATUTS_VOEUX = "liste_statuts_voeux";
    private static final String MARK_LISTE_STATUTS_AMENDEMENTS = "liste_statuts_amendements";
    private static final String MARK_LISTE_HISTORIQUES = "liste_historiques";
    private static final String MARK_ID_FORMATION_CONSEIL_SELECTED = "id_formation_conseil_selected";
    private static final String MARK_ID_SEANCE_SELECTED = "id_seance_selected";
    private static final String MARK_ID_STATUT_SELECTED = "id_statut_selected";
    private static final String MARK_ID_PUBLICATION_SELECTED = "id_publication_selected";
    private static final String MARK_URL_JSP_APPELANT = "url_jsp_appelant";
    private static final String MARK_URL_RETOUR = "url_retour";
    private static final String MARK_FORMATION_CONSEIL = "formation_conseil";
    private static final String MARK_STATUT_VA = "statut_va";
    private static final String MARK_ENTREE = "entree";
    private static final String PROPERTY_FICHIERS_PER_PAGE = "ods.itemPerPage.label";
    private static final String PROPERTY_PAGE_TITLE_CREATION_AMENDEMENT = "ods.creationamendement.page.title";
    private static final String PROPERTY_PAGE_TITLE_CREATION_VOEU = "ods.creationvoeurattache.page.title";
    private static final String PROPERTY_PAGE_TITLE_CREATION_VOEU_NON_RATACHE = "ods.creationvoeunonrattache.page.title";
    private static final String PROPERTY_PAGE_TITLE_MODIFICATION_AMENDEMENT = "ods.modificationamendement.page.title";
    private static final String PROPERTY_PAGE_TITLE_MODIFICATION_VOEU = "ods.modificationvoeurattache.page.title";
    private static final String PROPERTY_PAGE_TITLE_MODIFICATION_VOEU_NON_RATACHE = "ods.modificationvoeunonrattache.page.title";
    private static final String PROPERTY_LABEL_FORMATION_CONSEIL = "ods.voeuamendement.label.formationconseil";
    private static final String PROPERTY_LABEL_FASCICULE = "ods.voeuamendement.label.fascicule";
    private static final String PROPERTY_LABEL_OBJET = "ods.voeuamendement.label.objet";
    private static final String PROPERTY_LABEL_FICHIER = "ods.voeuamendement.label.fichier";
    private static final String PROPERTY_LABEL_EXECUTIF = "ods.liasse.label.executif";
    private static final String CONSTANTE_ESPACE = " ";
    private static final String CONSTANTE_GUILLEMET = "\"";
    private static final String CONSTANTE_OUVERTURE_ACCOLADE = "(";
    private static final String CONSTANTE_FERMETURE_ACCOLADE = ")";
    private static final String CONSTANTE_POINT_VIRGULE = ";";
    private static final String CONSTANTE_VIRGULE = ",";
    private static final String CONSTANTE_CREATION_FICHIER_INTITULE = "Texte Déposé";
    private static final String CONSTANTE_ANTI_SLASH = "\n";
    private static final String CONSTANTE_FIN_INTITULE_FICHIER_DELIBERATION = "délibération";
    private static final String JSP_URL_DO_SUPPRESSIONAMENDEMENT = "jsp/admin/plugins/ods/voeuamendement/DoSuppressionAmendement.jsp";
    private static final String JSP_URL_DO_SUPPRESSIONVOEURATTACHE = "jsp/admin/plugins/ods/voeuamendement/DoSuppressionVoeuRattache.jsp";
    private static final String JSP_URL_DO_SUPPRESSIONVOEUNONRATTACHE = "jsp/admin/plugins/ods/voeuamendement/DoSuppressionVoeuNonRattache.jsp";
    private static final String JSP_URL_GET_MODIFICATION_AMENDEMENT = "jsp/admin/plugins/ods/voeuamendement/ModificationAmendement.jsp";
    private static final String JSP_URL_GET_MODIFICATION_VOEU_RATTACHE = "jsp/admin/plugins/ods/voeuamendement/ModificationVoeuRattache.jsp";
    private static final String JSP_URL_GET_MODIFICATION_VOEU_NON_RATTACHE = "jsp/admin/plugins/ods/voeuamendement/ModificationVoeuNonRattache.jsp";
    private static final String JSP_URL_GET_CREATION_AMENDEMENT = "jsp/admin/plugins/ods/voeuamendement/CreationAmendementNoReinit.jsp";
    private static final String JSP_URL_GET_CREATION_VOEU_RATTACHE = "jsp/admin/plugins/ods/voeuamendement/CreationVoeuRattacheNoReinit.jsp";
    private static final String JSP_URL_GET_CREATION_VOEU_NON_RATTACHE = "jsp/admin/plugins/ods/voeuamendement/CreationVoeuNonRattacheNoReinit.jsp";
    private static final String JSP_URL_GET_AMENDEMENTS_LIST = "jsp/admin/plugins/ods/voeuamendement/AmendementsNoReinit.jsp";
    private static final String JSP_URL_GET_VOEUX_RATTACHES_LIST = "jsp/admin/plugins/ods/voeuamendement/VoeuxrattachesNoReinit.jsp";
    private static final String JSP_URL_GET_VOEUX_NON_RATTACHES_LIST = "jsp/admin/plugins/ods/voeuamendement/VoeuxnonrattachesNoReinit.jsp";
    private static final String JSP_URL_GET_AMENDEMENTS_LIST_GESTION_AVAL = "jsp/admin/plugins/ods/voeuamendement/AmendementsGestionAvalNoReinit.jsp";
    private static final String JSP_URL_GET_VOEUX_RATTACHES_LIST_GESTION_AVAL = "jsp/admin/plugins/ods/voeuamendement/VoeuxrattachesGestionAvalNoReinit.jsp";
    private static final String JSP_URL_GET_VOEUX_NON_RATTACHES_LIST_GESTION_AVAL = "jsp/admin/plugins/ods/voeuamendement/VoeuxnonrattachesGestionAvalNoReinit.jsp";
    private static final String JSP_URL_GET_STATUT_VA = "jsp/admin/plugins/ods/voeuamendement/StatutVAGestionAvalNoReinit.jsp";
    private static final String JSP_URL_DO_MODIFICATION_STATUTS_NON_DEFINIS = "jsp/admin/plugins/ods/voeuamendement/DoModificationStatutsNonDefinis.jsp";
    private static final String JSP_URL_GET_MODIFICATION_STATUT_VA = "jsp/admin/plugins/ods/voeuamendement/ModificationStatutVA.jsp";
    private static final String JSP_URL_GET_AVERTISSEMENT_ENREGISTREMENT_VOEU_RATTACHE = "jsp/admin/plugins/ods/voeuamendement/AvertissementEnregistrementVoeuRattache.jsp";
    private static final String JSP_URL_GET_AVERTISSEMENT_ENREGISTREMENT_AMENDEMENT = "jsp/admin/plugins/ods/voeuamendement/AvertissementEnregistrementAmendement.jsp";
    private static final String CONSTANTE_PAGE_FIRST = "1";

    /*****************************variables de session****************************************************************************/
    private VoeuAmendement _voeuAmendementBean;
    private Seance _seance;
    private boolean _bIsGestionAval;

    //variable de session permettant de savoir si le retour ne doit pas ce faire sur l'interface de gestion des vas 
    private boolean _bUrlRetour;

    //variable de session stockant l'url de retour si le retour ne ce fait pas sur l'interface de gestion des vas 
    private String _strUrlRetour;

    //Referencelist en vue de stocker l'ensemble des items représentant les types de conseils
    private int _nIdFormationConseilSelected = -1;

    //Referencelist en vue de stocker l'ensemble des items représentant les types de publication
    private int _nIdPublicationSelected = -1;

    // Referencelist en vue de stocker l'ensemble des items représentant les statuts
    //	private ReferenceList _refListStatuts;
    private int _nIdStatutSelected = -1;

    //Id de la séance sélectionnée
    private int _nIdSeanceSelected = -1;

    //gère si il ya eu des changements dans un Va avant enregistrement
    private boolean _bHasChanged;

    //Gere la page du Paginator
    private String _strCurrentPageIndex;

    //Gere le nombre d'item par page
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage = -1;

    /*********************************************************************************************************************************/

    /**
     * Permet d'initialiser l'url de retour
     * @param strUrl url de retour
     * @param request la requête http
     */
    public void initUrlRetour( String strUrl, HttpServletRequest request )
    {
        _strUrlRetour = strUrl;
        _bUrlRetour = true;

        _strUrlRetour += ( "?" + OdsParameters.PLUGIN_NAME );

        if ( request.getParameter( OdsParameters.ID_ODJ ) != null )
        {
            _strUrlRetour += ( "&" + OdsParameters.ID_ODJ + "=" + request.getParameter( OdsParameters.ID_ODJ ) +
            "&creation=creation" );
        }
    }

    /**
     * Permet d'initialiser la liste des différents type de conseil
     * @param bLiasse parametre qui spécifie si la liste
     * est destinée à etre affichée dans l'interface représentant la liasse de va.
     * Si c'est le cas la liste déroulante ne doit pas contenir l'option chaine vide
     * @param refList la liste du menu déroulant
     */
    private void initRefListFormationConseil( boolean bLiasse, ReferenceList refList )
    {
        List<FormationConseil> listFormationConseil = FormationConseilHome.findFormationConseilList( getPlugin(  ) );

        //ajout dans la referenceList de l'item chaine vide si bLiasse=false
        if ( !bLiasse )
        {
            refList.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );
        }

        //ajout dans la referenceList de l'ensemble des items représentant les groupes politiques
        for ( FormationConseil formationConseil : listFormationConseil )
        {
            refList.addItem( formationConseil.getIdFormationConseil(  ), formationConseil.getLibelle(  ) );
        }
    }

    /**
     * Permet d'initialiser la liste des différents statuts
     * @param filter le filtre de statut
     * @param refList la liste pour le menu déroulant
     */
    private void initRefListStatut( StatutFilter filter, ReferenceList refList )
    {
        List<Statut> listStatuts;

        if ( filter != null )
        {
            listStatuts = StatutHome.findStatutWithFilterList( filter, getPlugin(  ) );
        }
        else
        {
            listStatuts = StatutHome.findStatutList( getPlugin(  ) );
        }

        //ajout dans la referenceList de l'ensemble des items représentant les statuts
        refList.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

        if ( ( listStatuts != null ) && !listStatuts.isEmpty(  ) )
        {
            for ( Statut statut : listStatuts )
            {
                refList.addItem( statut.getIdStatut(  ), statut.getLibelle(  ) );
            }
        }
    }

    /**
     * Permet d'initialiser la liste des choix de type de conseil
     * @param refList la liste pour le menu déroulant
     */
    private void initRefListCommission( ReferenceList refList )
    {
        List<Commission> listCommission;

        if ( isGestionAval(  ) )
        {
            listCommission = CommissionHome.findCommissionList( getPlugin(  ) );
        }
        else
        {
            listCommission = CommissionHome.findAllCommissionsActives( getPlugin(  ) );
        }

        //ajout dans la referenceList de l'item chaine vide
        refList.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

        //ajout dans la referenceList de l'ensemble des items représentant les groupes politiques
        for ( Commission commission : listCommission )
        {
            refList.addItem( commission.getIdCommission(  ),
                OdsConstants.CONSTANTE_CHAINE_VIDE + commission.getNumero(  ) );
        }
    }

    /**
     * Permet d'initialiser la liste des choix des groupes politiques
     * @param refList la liste pour le menu déroulant
     */
    private void initRefListGroupe( ReferenceList refList )
    {
        List<GroupePolitique> listGroupePolitique;

        if ( isGestionAval(  ) )
        {
            listGroupePolitique = GroupePolitiqueHome.findGroupePolitiqueList( getPlugin(  ) );
        }
        else
        {
            listGroupePolitique = GroupePolitiqueHome.findGroupesActifs( getPlugin(  ) );
        }

        //ajout dans la referenceList de l'item chaine vide
        refList.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

        //ajout dans la referenceList de l'ensemble des items représentant les groupes politiques
        for ( GroupePolitique groupe : listGroupePolitique )
        {
            refList.addItem( groupe.getIdGroupe(  ), groupe.getNomGroupe(  ) );
        }
    }

    /**
     * Permet d'initialiser la liste des choix de publications
     * Les choix possibles: OUI,NON
     * @param refList la liste pour le menu déroulant
     */
    private void initRefListPublie( ReferenceList refList )
    {
        refList.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );
        refList.addItem( 1, I18nService.getLocalizedString( OdsConstants.CONSTANTE_OUI, getLocale(  ) ) );
        refList.addItem( 0, I18nService.getLocalizedString( OdsConstants.CONSTANTE_NON, getLocale(  ) ) );
    }

    /**
     * Reinitialise tous les attributs de session
     * @param request requete HTTP
     * @param strRight droits
     * @param bIsGestionAval TRUE si c'est de la gestion en aval, FALSE sinon
     * @throws AccessDeniedException   AccessDeniedException
     */
    public void reinitSession( HttpServletRequest request, String strRight, boolean bIsGestionAval )
        throws AccessDeniedException
    {
        super.init( request, strRight );
        _voeuAmendementBean = null;
        _bIsGestionAval = bIsGestionAval;
        _bHasChanged = false;
        _bUrlRetour = false;
        _strUrlRetour = null;
        _nIdFormationConseilSelected = -1;
        _nIdPublicationSelected = -1;
        _nIdSeanceSelected = -1;
        _nIdStatutSelected = -1;

        if ( -1 == _nDefaultItemsPerPage )
        {
            _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_FICHIERS_PER_PAGE, 10 );
        }

        initSeance( request, true );

        if ( _seance != null )
        {
            _nIdSeanceSelected = _seance.getIdSeance(  );
        }
    }

    /**
     * Methode qui fait appelle a la methode init() de PluginAdminPageJspBean
     * et initialise les variables se sessions si elles n'ont pas étées auparavant initialisées
     * @param request request
     * @param strRight droits
     * @param isGestionAval TRUE si c'est de la gestion en aval, FALSE sinon
     * @throws AccessDeniedException
     */
    public void init( HttpServletRequest request, String strRight, boolean bIsGestionAval )
        throws AccessDeniedException
    {
        super.init( request, strRight );
        _bIsGestionAval = bIsGestionAval;
        initSeance( request, false );

        if ( -1 == _nDefaultItemsPerPage )
        {
            _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_FICHIERS_PER_PAGE, 10 );
        }
    }

    /**
     * Initialise la variable de session de seance
     * @param request la requête HTTP
     */
    private void initSeance( HttpServletRequest request, boolean bReinit )
    {
        /*
         * Cas où l'on donne en paramètre l'id de la séance sur laquelle s'initialiser.
         */
        if ( ( request.getParameter( OdsParameters.ID_SEANCE ) != null ) &&
                !request.getParameter( OdsParameters.ID_SEANCE ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            int nIdSeance = -1;

            try
            {
                nIdSeance = Integer.parseInt( request.getParameter( OdsParameters.ID_SEANCE ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
                nIdSeance = -1;
            }

            _seance = SeanceHome.findByPrimaryKey( nIdSeance, getPlugin(  ) );
        }

        /*
         * Cas où l'utilisateur a sélectionné une séance dans un menu déroulant
         */
        else if ( ( _nIdSeanceSelected != -1 ) && ( _seance == null ) )
        {
            _seance = SeanceHome.findByPrimaryKey( _nIdSeanceSelected, getPlugin(  ) );
        }

        /*
         * Cas où la séance est nulle et que l'on est en Gestion Aval.
         * La séance est donc la dernière séance archivée.
         */
        else if ( isGestionAval(  ) &&
                ( bReinit || ( _seance == null ) ||
                ( ( SeanceHome.getProchaineSeance( getPlugin(  ) ) != null ) &&
                ( _seance.getIdSeance(  ) == SeanceHome.getProchaineSeance( getPlugin(  ) ).getIdSeance(  ) ) ) ) )
        {
            _seance = SeanceHome.getDerniereSeance( getPlugin(  ) );
        }

        /*
         * Cas où l'on n'est pas en Gestion Aval.
         * La séance est donc la prochaine séance.
         */
        else if ( !isGestionAval(  ) && ( SeanceHome.getProchaineSeance( getPlugin(  ) ) != null ) )
        {
            _seance = SeanceHome.getProchaineSeance( getPlugin(  ) );
        }

        /*
         * Cas où la séance en session est nulle
         * mais que le VA en session est rattaché à un fascicule
         */
        else if ( ( _seance == null ) && ( _voeuAmendementBean != null ) &&
                ( _voeuAmendementBean.getFascicule(  ) != null ) )
        {
            if ( _voeuAmendementBean.getFascicule(  ).getSeance(  ) == null )
            {
                Fascicule fascicule = FasciculeHome.findByPrimaryKey( _voeuAmendementBean.getFascicule(  )
                                                                                         .getIdFascicule(  ),
                        getPlugin(  ) );

                if ( fascicule.getSeance(  ) != null )
                {
                    _seance = SeanceHome.findByPrimaryKey( fascicule.getSeance(  ).getIdSeance(  ), getPlugin(  ) );
                    fascicule.setSeance( _seance );
                }

                _voeuAmendementBean.setFascicule( fascicule );
            }
        }
    }

    /**
     * Retourne l'id du type de conseil sélectionné et initialise le filtre si une selection a été faite
     * @param request request
     * @param filter filtre initialisé si une selection a été faite sur le type de conseil
     * @return id du type de conseil sélectionné
     */
    private int getIdFormationConseil( HttpServletRequest request, VoeuAmendementFilter filter )
    {
        int nCodeFormationConseilFilter = VoeuAmendementFilter.ALL_INT;

        if ( request.getParameter( OdsParameters.ID_FORMATION_CONSEIL ) != null )
        {
            try
            {
                nCodeFormationConseilFilter = Integer.parseInt( ( request.getParameter( 
                            OdsParameters.ID_FORMATION_CONSEIL ) ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
                nCodeFormationConseilFilter = _nIdFormationConseilSelected;
            }

            //si la selection différe de la selection stocké en session on réinitialise le paginator 
            if ( nCodeFormationConseilFilter != _nIdFormationConseilSelected )
            {
                resetPageIndex(  );
            }
        }
        else
        {
            nCodeFormationConseilFilter = _nIdFormationConseilSelected;
        }

        if ( ( filter != null ) && ( nCodeFormationConseilFilter != VoeuAmendementFilter.ALL_INT ) )
        {
            filter.setIdFormationConseil( nCodeFormationConseilFilter );
        }

        return nCodeFormationConseilFilter;
    }

    /**
     * Retourne l'id du type de publication sélectionné et initialise le filtre si une selection a été faite
     * @param request request
     * @param filter filtre initialisé si une selection a été faite sur le type de publication
     * @return id du type de publication
     */
    private int getIdPublie( HttpServletRequest request, VoeuAmendementFilter filter )
    {
        int nCodePublieFilter = VoeuAmendementFilter.ALL_INT;

        if ( request.getParameter( OdsParameters.ID_PUBLICATION ) != null )
        {
            try
            {
                nCodePublieFilter = Integer.parseInt( ( request.getParameter( OdsParameters.ID_PUBLICATION ) ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
                nCodePublieFilter = _nIdPublicationSelected;
            }

            //si la selection différe de la selection stocké en session on réinitialise l'index de la page courante
            if ( nCodePublieFilter != _nIdPublicationSelected )
            {
                resetPageIndex(  );
            }
        }
        else
        {
            nCodePublieFilter = _nIdPublicationSelected;
        }

        filter.setIdPublie( nCodePublieFilter );

        return nCodePublieFilter;
    }

    /**
     * retourne l'id de la séance sélectionné et initialise le filtre si une selection a été faite
     * @param request request
     * @param filter filtre initialisé si une selection a été faite sur le type de publication
     * @return id de la séance
     */
    private int getIdSeance( HttpServletRequest request, VoeuAmendementFilter filter )
    {
        if ( ( request.getParameter( OdsParameters.ID_SEANCE ) != null ) &&
                !request.getParameter( OdsParameters.ID_SEANCE ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            int nIdSeance = -1;

            try
            {
                String strIdSeance = request.getParameter( OdsParameters.ID_SEANCE ).trim(  );
                nIdSeance = Integer.parseInt( strIdSeance );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            if ( nIdSeance > 0 )
            {
                filter.setIdSeance( nIdSeance );

                return nIdSeance;
            }
        }

        return -1;
    }

    /**
     * retourne l'id du statut sélectionné et initialise le filtre si une selection a été faite
     * @param request request
     * @param filter filtre initialisé si une selection a été faite sur le statut
     * @return id du statut
     */
    private int getIdStatut( HttpServletRequest request, VoeuAmendementFilter filter )
    {
        int nCodeStatutFilter = VoeuAmendementFilter.ALL_INT;

        if ( request.getParameter( OdsParameters.ID_STATUT ) != null )
        {
            try
            {
                nCodeStatutFilter = Integer.parseInt( ( request.getParameter( OdsParameters.ID_STATUT ).trim(  ) ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
                nCodeStatutFilter = _nIdStatutSelected;
            }

            //si la selection différe de la selection stocké en session on réinitialise l'index de la page courante
            if ( nCodeStatutFilter != _nIdStatutSelected )
            {
                resetPageIndex(  );
            }
        }
        else
        {
            nCodeStatutFilter = _nIdStatutSelected;
        }

        filter.setIdStatut( nCodeStatutFilter );

        return nCodeStatutFilter;
    }

    /**
     * réinitialise l'index de la page courante
     *
     */
    private void resetPageIndex(  )
    {
        _strCurrentPageIndex = CONSTANTE_PAGE_FIRST;
    }

    /**
     * Retourne l'écran de creation d'un amendement, d'un voeu rattaché ou d'un voeu non rattaché en fonction du type de création demandé
     * @param request la requete
     * @param strType le type de VA
     * @param doReinitVA doit-on reinitialiser le VA en session
     * @return template template
     */
    public String getCreationVoeuAmendement( HttpServletRequest request, String strType, boolean doReinitVA )
    {
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        if ( doReinitVA )
        {
            //on initialise le voeuAmendement en session et stock en session le type du VA (par defaut   deposeExecutif = true)
            _voeuAmendementBean = new VoeuAmendement(  );
            _voeuAmendementBean.setType( strType );
            _voeuAmendementBean.setDeposeExecutif( false );
        }

        ReferenceList refListElu = new ReferenceList(  );
        List<Elu> listElu;

        if ( isGestionAval(  ) )
        {
            listElu = EluHome.findEluList( getPlugin(  ) );
        }
        else
        {
            listElu = EluHome.findElusActifs( getPlugin(  ) );
        }

        List<Elu> listEluAAfficher = new ArrayList<Elu>(  );

        //initialisation de la referenceliste refListElu et création du tableau javascript
        //associant un groupe à une liste d'élus   
        init_list_elu( refListElu, listElu, listEluAAfficher );

        // Dans le cas de la gestion aval si la séance est nulle on met en session 
        // la dernière séance par défaut
        _nIdSeanceSelected = 1;

        ReferenceList refListFormationConseil = new ReferenceList(  );
        initRefListFormationConseil( false, refListFormationConseil );

        ReferenceList refListGroupe = new ReferenceList(  );
        initRefListGroupe( refListGroupe );

        ReferenceList refListCommission = new ReferenceList(  );
        initRefListCommission( refListCommission );

        ReferenceList refListFascicule = new ReferenceList(  );
        List<Fascicule> listFasciculeAAfficher;

        if ( isGestionAval(  ) )
        {
            listFasciculeAAfficher = FasciculeHome.findAll( getPlugin(  ) );
        }
        else
        {
            listFasciculeAAfficher = FasciculeHome.findFasciculeByIdSeance( _seance.getIdSeance(  ), getPlugin(  ) );
        }

        // initialisation de la referenceliste refListFascicule et création du tableau javascript
        // associant une séance à une liste de fascicules
        init_list_fascicule( refListFascicule, listFasciculeAAfficher, _seance );

        if ( isGestionAval(  ) )
        {
            List<Seance> listSeance = SeanceHome.findOldSeance( getPlugin(  ) );

            StatutFilter filter = new StatutFilter(  );

            if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VR ) ||
                    _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VNR ) )
            {
                filter.setPourVoeu( true );

                if ( _voeuAmendementBean.getDeliberation(  ) != null )
                {
                    Fichier deliberation = FichierHome.findByPrimaryKey( _voeuAmendementBean.getDeliberation(  ).getId(  ),
                            getPlugin(  ) );
                    _voeuAmendementBean.setDeliberation( deliberation );
                }
            }

            if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                    _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
            {
                filter.setPourAmendement( true );
            }

            ReferenceList refListStatuts = new ReferenceList(  );
            initRefListStatut( filter, refListStatuts );

            // elements necessaires pour la gestion aval
            model.put( OdsMarks.MARK_GESTION_AVAL, true );
            model.put( MARK_LISTE_SEANCES, listSeance );
            model.put( MARK_ID_SEANCE_SELECTED, _nIdSeanceSelected );
            model.put( MARK_LISTE_STATUTS, refListStatuts );
            model.put( MARK_ID_STATUT_SELECTED, _nIdStatutSelected );
        }
        else
        {
            // indique que ce n'est pas la gestion aval
            model.put( OdsMarks.MARK_GESTION_AVAL, false );
        }

        model.put( MARK_LISTE_ELU_A_AFFICHER, listEluAAfficher );
        model.put( MARK_LISTE_FASCICULE_A_AFFICHER, listFasciculeAAfficher );
        model.put( VoeuAmendement.MARK_VOEUAMENDEMENT, _voeuAmendementBean );
        model.put( Seance.MARK_SEANCE, _seance );
        model.put( MARK_LISTE_FASCICULE, refListFascicule );
        model.put( MARK_LISTE_FORMATION_CONSEIL, refListFormationConseil );
        model.put( MARK_LISTE_GROUPE, refListGroupe );
        model.put( MARK_LISTE_ELU, refListElu );
        model.put( MARK_LISTE_COMMISSION, refListCommission );
        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );

        //test sur le type de création demandé(amendement,voeu,voeu non rataché)
        HtmlTemplate template;

        if ( strType.equals( CONSTANTE_TYPE_A ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATION_AMENDEMENT );
            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_AMENDEMENT, getLocale(  ), model );
        }
        else if ( strType.equals( CONSTANTE_TYPE_VR ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATION_VOEU );
            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_VOEU_RATTACHE, getLocale(  ), model );
        }
        else
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATION_VOEU_NON_RATACHE );
            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_VOEU_NON_RATACHE, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * methode qui initialise la liste des élus à afficher dans les interfaces de modification et créations
     * des Voeux et Amendements.
     * @param refListElu referenceList à initialiser
     * @param listElu liste des élus présent en base
     * @param listEluAAfficher liste des élus à afficher
     * @param strTableauJavascriptGroupeElus  tableau javascript destiné à associer un groupe à une liste d'élus
     */
    public void init_list_elu( ReferenceList refListElu, List<Elu> listElu, List<Elu> listEluAAfficher )
    {
        refListElu.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

        for ( Elu elu : listElu )
        {
            //si l'elu se trouve deja dans la liste des elus du voeuAmendement stocké en session(_VoeuAmendementBean) on ne l'affiche pas dans la combo élu		    
            if ( ( _voeuAmendementBean.getElus(  ) != null ) && ( _voeuAmendementBean.getElus(  ).size(  ) != 0 ) &&
                    isInTheEluList( elu.getIdElu(  ) ) )
            {
                AppLogService.debug( _voeuAmendementBean );
            }
            else
            {
                listEluAAfficher.add( elu );

                //construction pour chaque item de la liste élu du libellé à afficher
                StringBuffer strLibelleElu = new StringBuffer(  );
                strLibelleElu.append( elu.getCivilite(  ) );
                strLibelleElu.append( CONSTANTE_ESPACE );
                strLibelleElu.append( elu.getPrenomElu(  ) );
                strLibelleElu.append( CONSTANTE_ESPACE );
                strLibelleElu.append( elu.getNomElu(  ) );
                strLibelleElu.append( CONSTANTE_ESPACE );
                strLibelleElu.append( CONSTANTE_OUVERTURE_ACCOLADE );
                strLibelleElu.append( elu.getGroupe(  ).getNomGroupe(  ) );
                strLibelleElu.append( CONSTANTE_FERMETURE_ACCOLADE );
                refListElu.addItem( elu.getIdElu(  ), strLibelleElu.toString(  ) );
            }
        }
    }

    /**
     * methode qui initialise la liste des élus à afficher dans les interfaces de modification et créations
     * des Voeux et Amendements.
     * @param refListElu referenceList à initialiser
     * @param listElu liste des élus présent en base
     * @param listEluAAfficher liste des élus à afficher
     * @param strTableauJavascriptGroupeElus  tableau javascript destiné à associer un groupe à une liste d'élus
     */
    public void init_list_fascicule( ReferenceList refListFascicule, List<Fascicule> listFasciculeAAfficher,
        Seance seance )
    {
        refListFascicule.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

        for ( Fascicule fascicule : listFasciculeAAfficher )
        {
            if ( !isGestionAval(  ) ||
                    ( ( fascicule.getSeance(  ) != null ) && ( seance != null ) &&
                    ( fascicule.getSeance(  ).getIdSeance(  ) == seance.getIdSeance(  ) ) ) )
            {
                //construction pour chaque item de la liste élu du libellé à afficher
                StringBuffer strLibelleFascicule = new StringBuffer(  );
                strLibelleFascicule.append( fascicule.getCodeFascicule(  ) );
                strLibelleFascicule.append( CONSTANTE_ESPACE );
                strLibelleFascicule.append( CONSTANTE_OUVERTURE_ACCOLADE );
                strLibelleFascicule.append( fascicule.getNomFascicule(  ) );
                strLibelleFascicule.append( CONSTANTE_FERMETURE_ACCOLADE );
                refListFascicule.addItem( fascicule.getIdFascicule(  ), strLibelleFascicule.toString(  ) );
            }
        }
    }

    /**
     * Methode qui effectue la creation d'un amendement, d'un voeu rattaché ou d'un voeu non rattaché
     * en fonction du type de création demandé.
     * Redirection vers l'interface d'administration(amendement,voeu rattaché ou voeu non rattaché)
     * @param request request
     * @param strType type de création demandé
     * @return url
     */
    public String doCreationVoeuAmendement( HttpServletRequest request, String strType )
    {
        // cas où l'utilisateur a cliqué sur Annuler
        if ( ( request.getParameter( OdsParameters.A_FAIRE ) != null ) &&
                request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_ACTION_CANCEL ) )
        {
            String strReturn = null;

            if ( _bUrlRetour )
            {
                strReturn = ( AppPathService.getBaseUrl( request ) + _strUrlRetour + "&" +
                    OdsParameters.ID_VOEUAMENDEMENT + "=" + _voeuAmendementBean.getIdVoeuAmendement(  ) );

                if ( OrdreDuJourJspBean.JSP_SELECTION_VOEU_RETOUR.equals( _strUrlRetour ) )
                {
                    strReturn += ( "&" + OdsParameters.ID_VOEUAMENDEMENT + "=" +
                    _voeuAmendementBean.getIdVoeuAmendement(  ) );
                }

                strReturn = AppPathService.getBaseUrl( request ) + _strUrlRetour + "&" + OdsParameters.A_FAIRE + "=" +
                    CONSTANTE_CANCEL;
                _bUrlRetour = false;
                _strUrlRetour = null;

                return strReturn;
            }
            else
            {
                return getHomeUrl( request, _voeuAmendementBean.getType(  ) );
            }
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        FileItem fileItem = null;
        fileItem = multipartRequest.getFile( OdsParameters.FICHIER_PHYSIQUE );

        List<String> champsNonSaisie = new ArrayList<String>(  );
        List<String> fileProblems = new ArrayList<String>(  );

        //test sur la nullité des parametres de l'interface de saisie
        if ( isFieldRequired( request, champsNonSaisie ) )
        {
            if ( ( request.getParameter( OdsParameters.REFERENCE ) != null ) &&
                    ( request.getParameter( OdsParameters.REFERENCE ).trim(  ).length(  ) > 10 ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_REFERENCE_TOO_LONG, AdminMessage.TYPE_STOP );
            }

            if ( setVoeuAmendementBean( request ) )
            {
                if ( isValideFile( fileItem, fileProblems ) )
                {
                    // pour la gestion en aval, on doit aussi traiter le fichier de délibération
                    FileItem fileDelib = null;

                    if ( isGestionAval(  ) && !_voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) )
                    {
                        fileDelib = multipartRequest.getFile( OdsParameters.FICHIER_DELIBERATION );

                        if ( !isValideFile( fileDelib, fileProblems ) )
                        {
                            String strFileProblem = ( fileProblems.get( 0 ) != null ) ? fileProblems.get( 0 ) : null;

                            if ( ( null != strFileProblem ) && strFileProblem.equals( MESSAGE_LABEL_FICHIER_NO_PDF ) )
                            {
                                return AdminMessageService.getMessageUrl( request, MESSAGE_LABEL_FICHIER_NO_PDF,
                                    AdminMessage.TYPE_STOP );
                            }
                        }

                        if ( ( null != fileDelib ) &&
                                ( ( null != fileDelib.getName(  ) ) &&
                                !fileDelib.getName(  ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) &&
                                ( ( null == request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ) ) ||
                                request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ).trim(  )
                                           .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) )
                        {
                            return AdminMessageService.getMessageUrl( request, MESSAGE_NO_NUMERO_VOEU_ADOPTE,
                                AdminMessage.TYPE_STOP );
                        }
                    }

                    //Les élus peuvent être saisis uniquement si le vœu ou amendement n’est pas déposé par l’exécutif(on vide la liste des elus ratachés au va)
                    if ( _voeuAmendementBean.getDeposeExecutif(  ) && ( null != _voeuAmendementBean.getElus(  ) ) )
                    {
                        _voeuAmendementBean.getElus(  ).clear(  );
                    }

                    // Test si  la référence de l' amendement  existe deja  dans le fascicule.
                    if ( ( _voeuAmendementBean.getReference(  ) != null ) &&
                            !_voeuAmendementBean.getReference(  ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) &&
                            VoeuAmendementHome.isAlreadyExistInFascicule( 
                                _voeuAmendementBean.getFascicule(  ).getIdFascicule(  ),
                                _voeuAmendementBean.getReference(  ), getPlugin(  ) ) )
                    {
                        return AdminMessageService.getMessageUrl( request,
                            MESSAGE_LA_REFERENCE_EXISTE_DEJA_DANS_LE_FASCICULE, AdminMessage.TYPE_STOP );
                    }

                    // Creation du  Fichier et stockage en session du fichier créé
                    _voeuAmendementBean.setFichier( doCreationFichier( fileItem, false, null ) );

                    // pour la gestion en aval on traite aussi le fichier de délibération
                    if ( isGestionAval(  ) && !_voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) &&
                            ( null != fileDelib ) && ( null != fileDelib.getName(  ) ) &&
                            !fileDelib.getName(  ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                    {
                        _voeuAmendementBean.setDeliberation( new Fichier(  ) );
                        _voeuAmendementBean.setDeliberation( doCreationFichier( fileDelib, true,
                                request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ) ) );
                    }

                    int nIdVA = VoeuAmendementHome.create( _voeuAmendementBean, getPlugin(  ) );
                    _voeuAmendementBean.setIdVoeuAmendement( nIdVA );

                    ajouteVAaReleveDesTravaux( _voeuAmendementBean );

                    boolean bIsPDDListEmpty = ( ( _voeuAmendementBean.getPdds(  ) == null ) ||
                        ( _voeuAmendementBean.getPdds(  ).isEmpty(  ) ) );

                    if ( request.getParameter( OdsParameters.A_FAIRE ).equals( CONSTANTE_ACTION_APPLY ) )
                    {
                        //enregistrement du nouveau VA , en base
                        //on initialise l'id du VA en session  
                        _voeuAmendementBean.setIdVoeuAmendement( nIdVA );

                        String strReturn = getModificationUrl( request, _voeuAmendementBean.getType(  ), nIdVA );

                        if ( isGestionAval(  ) )
                        {
                            strReturn += ( "&" + OdsParameters.ID_SEANCE + "=" + _seance.getIdSeance(  ) );
                        }

                        return strReturn;
                    }
                    else
                    {
                        if ( bIsPDDListEmpty && _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VR ) )
                        {
                            return AppPathService.getBaseUrl( request ) +
                            JSP_URL_GET_AVERTISSEMENT_ENREGISTREMENT_VOEU_RATTACHE;
                        }

                        if ( bIsPDDListEmpty &&
                                ( ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) ) ||
                                ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ) ) )
                        {
                            return AppPathService.getBaseUrl( request ) +
                            JSP_URL_GET_AVERTISSEMENT_ENREGISTREMENT_AMENDEMENT;
                        }

                        String strReturn = null;

                        if ( _bUrlRetour )
                        {
                            strReturn = ( AppPathService.getBaseUrl( request ) + _strUrlRetour + "&" +
                                OdsParameters.ID_VOEUAMENDEMENT + "=" + _voeuAmendementBean.getIdVoeuAmendement(  ) );

                            if ( OrdreDuJourJspBean.JSP_SELECTION_VOEU_RETOUR.equals( _strUrlRetour ) )
                            {
                                strReturn += ( "&" + OdsParameters.ID_VOEUAMENDEMENT + "=" +
                                _voeuAmendementBean.getIdVoeuAmendement(  ) );
                            }

                            _bUrlRetour = false;
                            _strUrlRetour = null;

                            return strReturn;
                        }
                        else
                        {
                            return getHomeUrl( request, _voeuAmendementBean.getType(  ) );
                        }
                    }
                }
                else
                {
                    String message = OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED;
                    String strFileProblem = ( fileProblems.get( 0 ) != null ) ? fileProblems.get( 0 ) : null;
                    Object[] messagesArgs = { I18nService.getLocalizedString( strFileProblem, getLocale(  ) ) };

                    if ( ( null != strFileProblem ) && strFileProblem.equals( MESSAGE_LABEL_FICHIER_NO_PDF ) )
                    {
                        message = MESSAGE_LABEL_FICHIER_NO_PDF;
                    }

                    return AdminMessageService.getMessageUrl( request, message, messagesArgs, AdminMessage.TYPE_STOP );
                }
            }
            else
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }
        }
        else
        {
            String strChamp = ( champsNonSaisie.get( 0 ) != null ) ? champsNonSaisie.get( 0 ) : null;
            Object[] messagesArgs = { I18nService.getLocalizedString( strChamp, getLocale(  ) ) };

            return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                messagesArgs, AdminMessage.TYPE_STOP );
        }
    }

    /**
     * Retourne l'écran de modification d'un amendement,d'un voeu rattaché,ou d'un voeu non rattaché
     * en fonction du voeu Amendement choisit dans la liste des VA .
     * @param request request
     * @return template template
     */
    public String getModificationVoeuAmendement( HttpServletRequest request )
    {
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        HtmlTemplate template;

        /*
         * Permission de modifier
         */
        boolean bPermissionModification = true;

        if ( !RBACService.isAuthorized( VoeuxAmendementsResourceIdService.VOEUX_AMENDEMENTS,
                    RBAC.WILDCARD_RESOURCES_ID, VoeuxAmendementsResourceIdService.PERMISSION_MISE_A_JOUR, getUser(  ) ) )
        {
            bPermissionModification = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_MODIFICATION, bPermissionModification );

        int nIdVoeuAmendement = -1;
        VoeuAmendement parent = null;
        boolean bParentRenseigne = false;

        if ( null != request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ) )
        {
            try
            {
                nIdVoeuAmendement = Integer.parseInt( request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ).trim(  ) );
            }
            catch ( NumberFormatException e )
            {
                AppLogService.error( e );

                return getVoeuAmendementList( request, _voeuAmendementBean.getType(  ) );
            }
        }

        // On récupère le parent du VA s'il en a un afin de répercuter les modifications à l'affichage sans 
        // modifier en base.
        if ( ( _voeuAmendementBean != null ) && ( _voeuAmendementBean.getIdVoeuAmendement(  ) == nIdVoeuAmendement ) )
        {
            parent = _voeuAmendementBean.getParent(  );
            bParentRenseigne = true;
        }

        _voeuAmendementBean = null;

        if ( nIdVoeuAmendement > -1 )
        {
            _voeuAmendementBean = VoeuAmendementHome.findByPrimaryKey( nIdVoeuAmendement, getPlugin(  ) );
        }

        if ( _voeuAmendementBean != null )
        {
            if ( bParentRenseigne )
            {
                _voeuAmendementBean.setParent( parent );

                if ( parent != null )
                {
                    _voeuAmendementBean.setPdds( parent.getPdds(  ) );
                }
            }

            if ( ( null != request.getParameter( OdsParameters.A_FAIRE ) ) &&
                    request.getParameter( OdsParameters.A_FAIRE ).equals( CONSTANTE_ACTION_STATUT_VA ) )
            {
                StatutFilter filter = new StatutFilter(  );

                if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VR ) ||
                        _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VNR ) )
                {
                    filter.setPourVoeu( true );

                    if ( _voeuAmendementBean.getDeliberation(  ) != null )
                    {
                        Fichier deliberation = FichierHome.findByPrimaryKey( _voeuAmendementBean.getDeliberation(  )
                                                                                                .getId(  ),
                                getPlugin(  ) );
                        _voeuAmendementBean.setDeliberation( deliberation );
                    }
                }

                if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                        _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
                {
                    filter.setPourAmendement( true );
                }

                if ( _voeuAmendementBean.getStatut(  ) != null )
                {
                    _nIdStatutSelected = _voeuAmendementBean.getStatut(  ).getIdStatut(  );
                }
                else
                {
                    _nIdStatutSelected = -1;
                }

                ReferenceList refListStatuts = new ReferenceList(  );
                initRefListStatut( filter, refListStatuts );

                model.put( MARK_LISTE_STATUTS, refListStatuts );
                model.put( MARK_ID_STATUT_SELECTED, _nIdStatutSelected );
                model.put( OdsMarks.MARK_GESTION_AVAL, false );
                model.put( MARK_STATUT_VA, true );
            }
            else if ( isGestionAval(  ) )
            {
                int nIdFascicule = _voeuAmendementBean.getFascicule(  ).getIdFascicule(  );
                Fascicule fascicule = FasciculeHome.findByPrimaryKey( nIdFascicule, getPlugin(  ) );
                fascicule.setSeance( _seance );
                _voeuAmendementBean.setFascicule( fascicule );

                List<Seance> listSeance = SeanceHome.findOldSeance( getPlugin(  ) );

                StatutFilter filter = new StatutFilter(  );

                if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VR ) ||
                        _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VNR ) )
                {
                    filter.setPourVoeu( true );

                    if ( _voeuAmendementBean.getDeliberation(  ) != null )
                    {
                        Fichier deliberation = FichierHome.findByPrimaryKey( _voeuAmendementBean.getDeliberation(  )
                                                                                                .getId(  ),
                                getPlugin(  ) );
                        _voeuAmendementBean.setDeliberation( deliberation );
                    }
                }

                if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                        _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
                {
                    filter.setPourAmendement( true );
                }

                if ( _voeuAmendementBean.getStatut(  ) != null )
                {
                    _nIdStatutSelected = _voeuAmendementBean.getStatut(  ).getIdStatut(  );
                }
                else
                {
                    _nIdStatutSelected = -1;
                }

                ReferenceList refListStatuts = new ReferenceList(  );
                initRefListStatut( filter, refListStatuts );

                // elements necessaires pour la gestion aval
                model.put( OdsMarks.MARK_GESTION_AVAL, true );
                model.put( MARK_STATUT_VA, false );
                model.put( MARK_LISTE_SEANCES, listSeance );
                model.put( MARK_ID_SEANCE_SELECTED, _nIdSeanceSelected );
                model.put( MARK_LISTE_STATUTS, refListStatuts );
                model.put( MARK_ID_STATUT_SELECTED, _nIdStatutSelected );
            }
            else
            {
                // indique que ce n'est pas la gestion aval
                model.put( OdsMarks.MARK_GESTION_AVAL, false );
                model.put( MARK_STATUT_VA, false );
            }

            // Récupération du fichier lié au voeu
            Fichier fichier = FichierHome.findByPrimaryKey( _voeuAmendementBean.getFichier(  ).getId(  ), getPlugin(  ) );
            _voeuAmendementBean.setFichier( fichier );

            ReferenceList refListElu = new ReferenceList(  );
            List<Elu> listElu;

            if ( isGestionAval(  ) )
            {
                listElu = EluHome.findEluList( getPlugin(  ) );
            }
            else
            {
                listElu = EluHome.findElusActifs( getPlugin(  ) );
            }

            List<Elu> listEluAAfficher = new ArrayList<Elu>(  );

            // initialisation de la referenceliste refListElu et création du tableau javascript
            // associant un groupe à une liste d'élus   
            init_list_elu( refListElu, listElu, listEluAAfficher );

            ReferenceList refListFascicule = new ReferenceList(  );
            List<Fascicule> listFasciculeAAfficher;

            if ( isGestionAval(  ) )
            {
                listFasciculeAAfficher = FasciculeHome.findAll( getPlugin(  ) );
            }
            else
            {
                listFasciculeAAfficher = FasciculeHome.findFasciculeByIdSeance( _seance.getIdSeance(  ), getPlugin(  ) );
            }

            // initialisation de la referenceliste refListFascicule et création du tableau javascript
            // associant une séance à une liste de fascicules   
            if ( ( _voeuAmendementBean.getFascicule(  ) != null ) &&
                    ( _voeuAmendementBean.getFascicule(  ).getSeance(  ) != null ) )
            {
                init_list_fascicule( refListFascicule, listFasciculeAAfficher,
                    _voeuAmendementBean.getFascicule(  ).getSeance(  ) );
            }
            else
            {
                init_list_fascicule( refListFascicule, listFasciculeAAfficher, null );
            }

            ReferenceList refListFormationConseil = new ReferenceList(  );
            initRefListFormationConseil( false, refListFormationConseil );

            ReferenceList refListGroupe = new ReferenceList(  );
            initRefListGroupe( refListGroupe );

            ReferenceList refListCommission = new ReferenceList(  );
            initRefListCommission( refListCommission );

            model.put( MARK_LISTE_ELU_A_AFFICHER, listEluAAfficher );
            model.put( MARK_LISTE_FASCICULE_A_AFFICHER, listFasciculeAAfficher );
            model.put( VoeuAmendement.MARK_VOEUAMENDEMENT, _voeuAmendementBean );
            model.put( Seance.MARK_SEANCE, _seance );
            model.put( MARK_LISTE_FASCICULE, refListFascicule );
            model.put( MARK_LISTE_FORMATION_CONSEIL, refListFormationConseil );
            model.put( MARK_LISTE_ELU, refListElu );
            model.put( MARK_LISTE_GROUPE, refListGroupe );
            model.put( MARK_LISTE_COMMISSION, refListCommission );
            model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );

            // test sur le type de modification demandé(amendement,voeu,voeu non rataché)
            if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                    _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
            {
                setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_AMENDEMENT );
                template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_AMENDEMENT, getLocale(  ), model );
            }
            else if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VR ) )
            {
                setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_VOEU );
                template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_VOEU_RATTACHE, getLocale(  ), model );
            }
            else
            {
                setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_VOEU_NON_RATACHE );
                template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_VOEU_NON_RATACHE, getLocale(  ), model );
            }

            return getAdminPage( template.getHtml(  ) );
        }
        else
        {
            return getVoeuAmendementList( request, VoeuAmendementJspBean.CONSTANTE_TYPE_A );
        }
    }

    /**
     * Retourne l'écran de modification d'un amendement,d'un voeu rattaché,ou d'un voeu non rattaché
     * en fonction du voeu Amendement choisit dans la liste des VA .
     * @param request request
     * @return template template
     */
    public String getModificationStatutVA( HttpServletRequest request )
    {
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        if ( ( null != request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ) ) &&
                !request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ).trim(  )
                            .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            try
            {
                int nIdVoeuAmendement = Integer.parseInt( request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ).trim(  ) );

                if ( ( _voeuAmendementBean == null ) ||
                        ( _voeuAmendementBean.getIdVoeuAmendement(  ) != nIdVoeuAmendement ) )
                {
                    _voeuAmendementBean = VoeuAmendementHome.findByPrimaryKey( nIdVoeuAmendement, getPlugin(  ) );
                }
            }
            catch ( NumberFormatException e )
            {
                AppLogService.error( e );

                return getHomeUrl( request, CONSTANTE_TYPE_STATUT_VA );
            }

            if ( _voeuAmendementBean.getFascicule(  ) != null )
            {
                int nIdFascicule = _voeuAmendementBean.getFascicule(  ).getIdFascicule(  );
                Fascicule fascicule = FasciculeHome.findByPrimaryKey( nIdFascicule, getPlugin(  ) );
                _voeuAmendementBean.setFascicule( fascicule );

                if ( fascicule.getSeance(  ) != null )
                {
                    int nIdSeance = fascicule.getSeance(  ).getIdSeance(  );
                    Seance seance = SeanceHome.findByPrimaryKey( nIdSeance, getPlugin(  ) );
                    _voeuAmendementBean.getFascicule(  ).setSeance( seance );
                }
            }

            if ( _voeuAmendementBean.getCommission(  ) != null )
            {
                int nIdCommission = _voeuAmendementBean.getCommission(  ).getIdCommission(  );
                Commission commission = CommissionHome.findByPrimaryKey( nIdCommission, getPlugin(  ) );
                _voeuAmendementBean.setCommission( commission );
            }

            StatutFilter filter = new StatutFilter(  );

            if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VR ) ||
                    _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VNR ) )
            {
                filter.setPourVoeu( true );

                if ( _voeuAmendementBean.getDeliberation(  ) != null )
                {
                    Fichier deliberation = FichierHome.findByPrimaryKey( _voeuAmendementBean.getDeliberation(  ).getId(  ),
                            getPlugin(  ) );
                    _voeuAmendementBean.setDeliberation( deliberation );
                }
            }

            if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                    _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
            {
                filter.setPourAmendement( true );
            }

            if ( _voeuAmendementBean.getStatut(  ) != null )
            {
                _nIdStatutSelected = _voeuAmendementBean.getStatut(  ).getIdStatut(  );
            }
            else
            {
                _nIdStatutSelected = -1;
            }

            ReferenceList refListStatuts = new ReferenceList(  );
            initRefListStatut( filter, refListStatuts );

            model.put( MARK_LISTE_STATUTS, refListStatuts );
            model.put( MARK_ID_STATUT_SELECTED, _nIdStatutSelected );
        }

        // recupération du fichier lié au voeu
        Fichier fichier = FichierHome.findByPrimaryKey( _voeuAmendementBean.getFichier(  ).getId(  ), getPlugin(  ) );
        _voeuAmendementBean.setFichier( fichier );

        HtmlTemplate template;
        model.put( VoeuAmendement.MARK_VOEUAMENDEMENT, _voeuAmendementBean );
        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );

        // test sur le type de modification demandé(amendement,voeu,voeu non rataché)
        if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_AMENDEMENT );
        }
        else if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VR ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_VOEU );
        }
        else
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_VOEU_NON_RATACHE );
        }

        template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_STATUT_VA, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Methode qui effectue la modification d'un amendement, d'un voeu rattaché
     * en fonction du type du VoeuAmendement présent en session.
     * Redirection vers l'interface d'administration(amendement,voeu non rattaché,voeu rattaché)
     * @param request request
     * @return template
     */
    public String doModificationVoeuAmendement( HttpServletRequest request )
    {
        // cas où l'utilisateur à cliqué sur Annuler
        if ( request.getParameter( OdsParameters.A_FAIRE ).equals( CONSTANTE_ACTION_CANCEL ) )
        {
            String strReturn;

            if ( _bUrlRetour )
            {
                strReturn = ( AppPathService.getBaseUrl( request ) + _strUrlRetour + "&" +
                    OdsParameters.ID_VOEUAMENDEMENT + "=" + _voeuAmendementBean.getIdVoeuAmendement(  ) );

                if ( OrdreDuJourJspBean.JSP_SELECTION_VOEU_RETOUR.equals( _strUrlRetour ) )
                {
                    strReturn += ( "&" + OdsParameters.ID_VOEUAMENDEMENT + "=" +
                    _voeuAmendementBean.getIdVoeuAmendement(  ) );
                }

                _bUrlRetour = false;
                _strUrlRetour = null;

                return strReturn;
            }
            else
            {
                return getHomeUrl( request, _voeuAmendementBean.getType(  ) );
            }
        }

        /*
         * GESTION DES PROFILS
         * -------------------
         * Si l'utilisateur n'est pas autorisé a effectuer cette fonction il est renvoyé
         * vers une page lui indiquant qu'il n'a pas les droits nécessaires.
         */
        if ( !isGestionAval(  ) && _voeuAmendementBean.getEnLigne(  ) &&
                !RBACService.isAuthorized( VoeuxAmendementsResourceIdService.VOEUX_AMENDEMENTS,
                    RBAC.WILDCARD_RESOURCES_ID, VoeuxAmendementsResourceIdService.PERMISSION_MISE_A_JOUR, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<String> champsNonSaisie = new ArrayList<String>(  );
        List<String> fileProblems = new ArrayList<String>(  );

        if ( ( request.getParameter( OdsParameters.REFERENCE ) != null ) &&
                ( request.getParameter( OdsParameters.REFERENCE ).trim(  ).length(  ) > 10 ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_REFERENCE_TOO_LONG, AdminMessage.TYPE_STOP );
        }

        String strMessage;

        /*
         * Dans le cas ou le VA est en ligne et que l'utilisateur est un chargé de mise en ligne,
         * il ne peut modifier que les attributs propres au statut du VA.
         * Donc seules ces modifications sont prises en compte.
         */
        if ( isGestionAval(  ) && _voeuAmendementBean.getEnLigne(  ) &&
                !RBACService.isAuthorized( VoeuxAmendementsResourceIdService.VOEUX_AMENDEMENTS,
                    RBAC.WILDCARD_RESOURCES_ID, VoeuxAmendementsResourceIdService.PERMISSION_MISE_A_JOUR, getUser(  ) ) )
        {
            //récupération du statut
            int nStatut;

            if ( ( request.getParameter( OdsParameters.ID_STATUT ) != null ) &&
                    !request.getParameter( OdsParameters.ID_STATUT ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                try
                {
                    nStatut = Integer.parseInt( request.getParameter( OdsParameters.ID_STATUT ) );

                    Statut statut = StatutHome.findByPrimaryKey( nStatut, getPlugin(  ) );
                    _voeuAmendementBean.setStatut( statut );
                }
                catch ( NumberFormatException ne )
                {
                    AppLogService.error( ne );
                }
            }

            DateFormat dateFormat = null;
            dateFormat = new SimpleDateFormat( OdsConstants.CONSTANTE_PATTERN_DATE );
            dateFormat.setLenient( false );

            //récupération de la date du vote
            if ( ( request.getParameter( OdsParameters.DATE_VOTE ) != null ) &&
                    !request.getParameter( OdsParameters.DATE_VOTE ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                String strDateVote = request.getParameter( OdsParameters.DATE_VOTE );
                Timestamp timeStampDateVote;

                try
                {
                    timeStampDateVote = new Timestamp( dateFormat.parse( strDateVote ).getTime(  ) );
                    _voeuAmendementBean.setDateVote( timeStampDateVote );
                }
                catch ( ParseException pe )
                {
                    AppLogService.error( pe );
                }
            }

            //récupération du numéro du voeu adopté
            if ( ( request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ) != null ) &&
                    !request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ).trim(  )
                                .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                String strNumeroVoeu = request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE );
                _voeuAmendementBean.setNumeroDeliberation( strNumeroVoeu );
            }

            //récupération de la date de retour de controle de légalité
            if ( ( request.getParameter( OdsParameters.DATE_RETOUR_CONTROLE_LEGALITE ) != null ) &&
                    !request.getParameter( OdsParameters.DATE_RETOUR_CONTROLE_LEGALITE ).trim(  )
                                .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                //récupération de la date de retour de contrôle de légalité
                String strDateRetourControleLegalite = request.getParameter( OdsParameters.DATE_RETOUR_CONTROLE_LEGALITE );
                Timestamp timeStampDateRetourControleLegalite;

                try
                {
                    timeStampDateRetourControleLegalite = new Timestamp( dateFormat.parse( 
                                strDateRetourControleLegalite ).getTime(  ) );
                    _voeuAmendementBean.setDateRetourControleLegalite( timeStampDateRetourControleLegalite );
                }
                catch ( ParseException pe )
                {
                    AppLogService.error( pe );
                }
            }

            // pour la gestion en aval, on doit traiter le fichier de délibération
            FileItem fileDelib = null;

            if ( !_voeuAmendementBean.getType(  ).trim(  ).equals( CONSTANTE_TYPE_A ) &&
                    ( multipartRequest.getFile( OdsParameters.FICHIER_DELIBERATION ) != null ) &&
                    ( multipartRequest.getFile( OdsParameters.FICHIER_DELIBERATION ).getName(  ) != null ) &&
                    !multipartRequest.getFile( OdsParameters.FICHIER_DELIBERATION ).getName(  )
                                         .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                fileDelib = multipartRequest.getFile( OdsParameters.FICHIER_DELIBERATION );

                if ( !isValideFile( fileDelib, fileProblems ) )
                {
                    String strFileProblem = ( fileProblems.get( 0 ) != null ) ? fileProblems.get( 0 ) : null;

                    if ( ( null != strFileProblem ) && strFileProblem.trim(  ).equals( MESSAGE_LABEL_FICHIER_NO_PDF ) )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_LABEL_FICHIER_NO_PDF,
                            AdminMessage.TYPE_STOP );
                    }
                }

                if ( ( null != fileDelib ) &&
                        ( ( null != fileDelib.getName(  ) ) &&
                        !fileDelib.getName(  ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) &&
                        ( ( null == request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ) ) ||
                        request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ).trim(  )
                                   .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_NO_NUMERO_VOEU_ADOPTE,
                        AdminMessage.TYPE_STOP );
                }

                Timestamp dateForVersion = new Timestamp( System.currentTimeMillis(  ) );

                if ( _voeuAmendementBean.getDeliberation(  ) != null )
                {
                    doModificationFichier( fileDelib, dateForVersion, true );
                }
                else
                {
                    _voeuAmendementBean.setDeliberation( new Fichier(  ) );
                    _voeuAmendementBean.setDeliberation( doCreationFichier( fileDelib, true,
                            request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ) ) );
                }
            }

            VoeuAmendementHome.update( _voeuAmendementBean, getPlugin(  ) );

            String strType = _voeuAmendementBean.getType(  );
            int nIdVA = _voeuAmendementBean.getIdVoeuAmendement(  );
            boolean bIsPDDListEmpty = ( ( _voeuAmendementBean.getPdds(  ) == null ) ||
                ( _voeuAmendementBean.getPdds(  ).isEmpty(  ) ) );

            if ( request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_ACTION_APPLY ) )
            {
                //enregistrement du nouveau VA , en base
                //on initialise l'id du VA en session  
                _voeuAmendementBean.setIdVoeuAmendement( nIdVA );
                strMessage = getModificationUrl( request, strType, nIdVA );

                strMessage += ( "&" + OdsParameters.ID_SEANCE + "=" + _seance.getIdSeance(  ) );
            }

            //si _nliasse=true on retourne sur la liasse des VA sinon getHomeUrl  
            else if ( request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_ACTION_SAVE ) &&
                    _bUrlRetour )
            {
                //on vide l'objet voeuAmendement en session
                _voeuAmendementBean = new VoeuAmendement(  );

                if ( bIsPDDListEmpty )
                {
                    if ( strType.equals( CONSTANTE_TYPE_VR ) )
                    {
                        return AppPathService.getBaseUrl( request ) +
                        JSP_URL_GET_AVERTISSEMENT_ENREGISTREMENT_VOEU_RATTACHE;
                    }

                    if ( ( strType.equals( CONSTANTE_TYPE_LR ) ) || ( strType.equals( CONSTANTE_TYPE_A ) ) )
                    {
                        return AppPathService.getBaseUrl( request ) +
                        JSP_URL_GET_AVERTISSEMENT_ENREGISTREMENT_AMENDEMENT;
                    }
                }

                strMessage = AppPathService.getBaseUrl( request ) + _strUrlRetour;

                _bUrlRetour = false;
                _strUrlRetour = null;
            }
            else
            {
                if ( bIsPDDListEmpty )
                {
                    if ( strType.equals( CONSTANTE_TYPE_VR ) )
                    {
                        return AppPathService.getBaseUrl( request ) +
                        JSP_URL_GET_AVERTISSEMENT_ENREGISTREMENT_VOEU_RATTACHE;
                    }

                    if ( ( strType.equals( CONSTANTE_TYPE_LR ) ) || ( strType.equals( CONSTANTE_TYPE_A ) ) )
                    {
                        return AppPathService.getBaseUrl( request ) +
                        JSP_URL_GET_AVERTISSEMENT_ENREGISTREMENT_AMENDEMENT;
                    }
                }

                strMessage = getHomeUrl( request, strType );
            }

            return strMessage;
        }

        /*
         * Dans le cas ou l'utilisateur peut modifier tous les attributs d'un VA,
         * alors on effectue la procédure de mise à jour suivante.
         */
        else if ( ( null != request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ) ) &&
                isFieldRequired( request, champsNonSaisie ) && setVoeuAmendementBean( request ) )
        {
            //test pour savoir si le fichier a été modifié et si c'est le cas si le fichier est bien au format PDF
            FileItem fileItem = multipartRequest.getFile( OdsParameters.FICHIER_PHYSIQUE );

            if ( !isValideFile( fileItem, fileProblems ) )
            {
                String strFileProblem = ( fileProblems.get( 0 ) != null ) ? fileProblems.get( 0 ) : null;

                if ( ( null != strFileProblem ) && strFileProblem.trim(  ).equals( MESSAGE_LABEL_FICHIER_NO_PDF ) )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_LABEL_FICHIER_NO_PDF,
                        AdminMessage.TYPE_STOP );
                }
            }

            if ( ( null != fileItem ) && ( null != fileItem.getName(  ) ) &&
                    !fileItem.getName(  ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                _bHasChanged = true;
            }

            // pour la gestion en aval, on doit aussi traiter le fichier de délibération
            FileItem fileDelib = null;

            if ( isGestionAval(  ) && !_voeuAmendementBean.getType(  ).trim(  ).equals( CONSTANTE_TYPE_A ) )
            {
                fileDelib = multipartRequest.getFile( OdsParameters.FICHIER_DELIBERATION );

                if ( !isValideFile( fileDelib, fileProblems ) )
                {
                    String strFileProblem = ( fileProblems.get( 0 ) != null ) ? fileProblems.get( 0 ) : null;

                    if ( ( null != strFileProblem ) && strFileProblem.trim(  ).equals( MESSAGE_LABEL_FICHIER_NO_PDF ) )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_LABEL_FICHIER_NO_PDF,
                            AdminMessage.TYPE_STOP );
                    }
                }

                if ( ( null != fileDelib ) &&
                        ( ( null != fileDelib.getName(  ) ) &&
                        !fileDelib.getName(  ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) &&
                        ( ( null == request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ) ) ||
                        request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ).trim(  )
                                   .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_NO_NUMERO_VOEU_ADOPTE,
                        AdminMessage.TYPE_STOP );
                }
            }

            //Test pour savoir si la référence modifié n'existe pas  dans le fascicule.
            if ( isReferenceModifyAlreadyInFascicule(  ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_LA_REFERENCE_EXISTE_DEJA_DANS_LE_FASCICULE,
                    AdminMessage.TYPE_STOP );
            }

            int nIdVa = -1;

            try
            {
                nIdVa = Integer.parseInt( request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            _voeuAmendementBean.setIdVoeuAmendement( nIdVa );

            //Les élus peuvent être saisis uniquement si le vœu ou amendement n’est pas déposé par l’exécutif(on vide la liste des elus ratachés au va)
            if ( _voeuAmendementBean.getDeposeExecutif(  ) && ( null != _voeuAmendementBean.getElus(  ) ) )
            {
                _voeuAmendementBean.getElus(  ).clear(  );
            }

            Timestamp dateForVersion = new Timestamp( System.currentTimeMillis(  ) );

            //gestion de l'historique du voeu Amendement			    		
            if ( _voeuAmendementBean.getEnLigne(  ) && _bHasChanged )
            {
                _voeuAmendementBean.setVersion( _voeuAmendementBean.getVersion(  ) + 1 );
                _voeuAmendementBean.setEnLigne( true );
                _voeuAmendementBean.setDatePublication( dateForVersion );

                //gestion de l'historique du voeu
                Historique historique = new Historique(  );
                historique.setVersion( _voeuAmendementBean.getVersion(  ) );
                historique.setDatePublication( dateForVersion );
                historique.setIdVa( _voeuAmendementBean.getIdVoeuAmendement(  ) );
                HistoriqueHome.create( historique, getPlugin(  ) );
            }

            _bHasChanged = false;

            //modification du fichier
            doModificationFichier( fileItem, dateForVersion, false );

            // pour la gestion en aval on traite aussi le fichier de délibération
            if ( isGestionAval(  ) && !_voeuAmendementBean.getType(  ).trim(  ).equals( CONSTANTE_TYPE_A ) &&
                    ( null != fileDelib ) && ( null != fileDelib.getName(  ) ) &&
                    !fileDelib.getName(  ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                if ( _voeuAmendementBean.getDeliberation(  ) != null )
                {
                    doModificationFichier( fileDelib, dateForVersion, true );
                }
                else
                {
                    _voeuAmendementBean.setDeliberation( new Fichier(  ) );
                    _voeuAmendementBean.setDeliberation( doCreationFichier( fileDelib, true,
                            request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ) ) );
                }
            }

            if ( !isGestionAval(  ) )
            {
                // Si il existe un élément de relevé de travaux rattaché à ce VA on le supprime
                ElementReleveDesTravauxHome.remove( _voeuAmendementBean, getPlugin(  ) );

                // On ajoute le VA au relevé des travaux de la commission à laquelle il est rattaché
                ajouteVAaReleveDesTravaux( _voeuAmendementBean );
            }

            VoeuAmendementHome.update( _voeuAmendementBean, getPlugin(  ) );

            String strType = _voeuAmendementBean.getType(  );
            int nIdVA = _voeuAmendementBean.getIdVoeuAmendement(  );

            boolean bIsPDDListEmpty = ( ( _voeuAmendementBean.getPdds(  ) == null ) ||
                ( _voeuAmendementBean.getPdds(  ).isEmpty(  ) ) );

            if ( request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_ACTION_APPLY ) )
            {
                //enregistrement du nouveau VA , en base
                //on initialise l'id du VA en session  
                _voeuAmendementBean.setIdVoeuAmendement( nIdVA );
                strMessage = getModificationUrl( request, strType, nIdVA );

                if ( isGestionAval(  ) )
                {
                    strMessage += ( "&" + OdsParameters.ID_SEANCE + "=" + _seance.getIdSeance(  ) );
                }
            }

            else if ( request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_ACTION_SAVE ) &&
                    _bUrlRetour )
            {
                //on vide l'objet voeuAmendement en session
                _voeuAmendementBean = new VoeuAmendement(  );

                if ( bIsPDDListEmpty )
                {
                    if ( strType.equals( CONSTANTE_TYPE_VR ) )
                    {
                        return AppPathService.getBaseUrl( request ) +
                        JSP_URL_GET_AVERTISSEMENT_ENREGISTREMENT_VOEU_RATTACHE;
                    }

                    if ( ( strType.equals( CONSTANTE_TYPE_LR ) ) || ( strType.equals( CONSTANTE_TYPE_A ) ) )
                    {
                        return AppPathService.getBaseUrl( request ) +
                        JSP_URL_GET_AVERTISSEMENT_ENREGISTREMENT_AMENDEMENT;
                    }
                }

                strMessage = AppPathService.getBaseUrl( request ) + _strUrlRetour;

                _bUrlRetour = false;
                _strUrlRetour = null;
            }
            else
            {
                if ( bIsPDDListEmpty )
                {
                    if ( strType.equals( CONSTANTE_TYPE_VR ) )
                    {
                        return AppPathService.getBaseUrl( request ) +
                        JSP_URL_GET_AVERTISSEMENT_ENREGISTREMENT_VOEU_RATTACHE;
                    }

                    if ( ( strType.equals( CONSTANTE_TYPE_LR ) ) || ( strType.equals( CONSTANTE_TYPE_A ) ) )
                    {
                        return AppPathService.getBaseUrl( request ) +
                        JSP_URL_GET_AVERTISSEMENT_ENREGISTREMENT_AMENDEMENT;
                    }
                }

                strMessage = getHomeUrl( request, strType );
            }

            return strMessage;
        }

        /*
         * Sinon certains champs obligatoires n'ont pas été convenablement remplis?
         * On indique donc à l'utilisateur quel champ doit être saisi à nouveau.
         */
        else
        {
            String strChamp = ( champsNonSaisie.get( 0 ) != null ) ? champsNonSaisie.get( 0 )
                                                                   : OdsConstants.CONSTANTE_CHAINE_VIDE;
            Object[] messagesArgs = { I18nService.getLocalizedString( strChamp, getLocale(  ) ) };

            return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                messagesArgs, AdminMessage.TYPE_STOP );
        }
    }

    /**
     * Methode qui effectue la modification d'un amendement, d'un voeu rattaché
     * en fonction du type du VoeuAmendement présent en session.
     * Redirection vers l'interface d'administration des statuts VA
     * @param request request
     * @return template
     */
    public String doModificationStatutVA( HttpServletRequest request )
    {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<String> fileProblems = new ArrayList<String>(  );

        if ( ( null != request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ) ) &&
                !request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ).trim(  )
                            .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            // pour la gestion en aval, on doit aussi traiter le fichier de délibération
            FileItem fileDelib = null;

            if ( isGestionAval(  ) && !_voeuAmendementBean.getType(  ).trim(  ).equals( CONSTANTE_TYPE_A ) )
            {
                fileDelib = multipartRequest.getFile( OdsParameters.FICHIER_DELIBERATION );

                if ( !isValideFile( fileDelib, fileProblems ) )
                {
                    String strFileProblem = ( fileProblems.get( 0 ) != null ) ? fileProblems.get( 0 ) : null;

                    if ( ( null != strFileProblem ) && strFileProblem.trim(  ).equals( MESSAGE_LABEL_FICHIER_NO_PDF ) )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_LABEL_FICHIER_NO_PDF,
                            AdminMessage.TYPE_STOP );
                    }
                }

                if ( ( null != fileDelib ) &&
                        ( ( null != fileDelib.getName(  ) ) &&
                        !fileDelib.getName(  ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) &&
                        ( ( null == request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ) ) ||
                        request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ).trim(  )
                                   .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_NO_NUMERO_VOEU_ADOPTE,
                        AdminMessage.TYPE_STOP );
                }
            }

            //Test pour savoir si la référence modifié n'existe pas  dans le fascicule.
            if ( isReferenceModifyAlreadyInFascicule(  ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_LA_REFERENCE_EXISTE_DEJA_DANS_LE_FASCICULE,
                    AdminMessage.TYPE_STOP );
            }

            int nIdVa = -1;

            try
            {
                nIdVa = Integer.parseInt( request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ).trim(  ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            _voeuAmendementBean.setIdVoeuAmendement( nIdVa );

            Timestamp dateForVersion = new Timestamp( System.currentTimeMillis(  ) );

            // pour la gestion en aval on traite aussi le fichier de délibération
            if ( !_voeuAmendementBean.getType(  ).trim(  ).equals( CONSTANTE_TYPE_A ) && ( null != fileDelib ) &&
                    ( null != fileDelib.getName(  ) ) &&
                    !fileDelib.getName(  ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                if ( _voeuAmendementBean.getDeliberation(  ) != null )
                {
                    doModificationFichier( fileDelib, dateForVersion, true );
                }
                else
                {
                    _voeuAmendementBean.setDeliberation( new Fichier(  ) );
                    _voeuAmendementBean.setDeliberation( doCreationFichier( fileDelib, true,
                            request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ) ) );
                }
            }

            //récupération du statut
            int nStatut;

            if ( ( request.getParameter( OdsParameters.ID_STATUT ) != null ) &&
                    !request.getParameter( OdsParameters.ID_STATUT ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                try
                {
                    nStatut = Integer.parseInt( request.getParameter( OdsParameters.ID_STATUT ) );

                    Statut statut = StatutHome.findByPrimaryKey( nStatut, getPlugin(  ) );
                    _voeuAmendementBean.setStatut( statut );
                }
                catch ( NumberFormatException ne )
                {
                    AppLogService.error( ne );
                }
            }

            DateFormat dateFormat = null;
            dateFormat = new SimpleDateFormat( OdsConstants.CONSTANTE_PATTERN_DATE );
            dateFormat.setLenient( false );

            //récupération de la date du vote
            if ( ( request.getParameter( OdsParameters.DATE_VOTE ) != null ) &&
                    !request.getParameter( OdsParameters.DATE_VOTE ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                String strDateVote = request.getParameter( OdsParameters.DATE_VOTE );
                Timestamp timeStampDateVote;

                try
                {
                    timeStampDateVote = new Timestamp( dateFormat.parse( strDateVote ).getTime(  ) );
                    _voeuAmendementBean.setDateVote( timeStampDateVote );
                }
                catch ( ParseException pe )
                {
                    AppLogService.error( pe );
                }
            }

            //récupération du numéro du voeu adopté
            if ( ( request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ) != null ) &&
                    !request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ).trim(  )
                                .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                String strNumeroVoeu = request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE );
                _voeuAmendementBean.setNumeroDeliberation( strNumeroVoeu );
            }

            //récupération de la date de retour de controle de légalité
            if ( ( request.getParameter( OdsParameters.DATE_RETOUR_CONTROLE_LEGALITE ) != null ) &&
                    !request.getParameter( OdsParameters.DATE_RETOUR_CONTROLE_LEGALITE ).trim(  )
                                .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                //récupération de la date de retour de contrôle de légalité
                String strDateRetourControleLegalite = request.getParameter( OdsParameters.DATE_RETOUR_CONTROLE_LEGALITE );
                Timestamp timeStampDateRetourControleLegalite;

                try
                {
                    timeStampDateRetourControleLegalite = new Timestamp( dateFormat.parse( 
                                strDateRetourControleLegalite ).getTime(  ) );
                    _voeuAmendementBean.setDateRetourControleLegalite( timeStampDateRetourControleLegalite );
                }
                catch ( ParseException pe )
                {
                    AppLogService.error( pe );
                }
            }

            VoeuAmendementHome.update( _voeuAmendementBean, getPlugin(  ) );

            int nIdVA = _voeuAmendementBean.getIdVoeuAmendement(  );
            String strMessage;

            if ( request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_ACTION_SAVE ) )
            {
                //on vide l'objet voeuAmendement en session
                _voeuAmendementBean = new VoeuAmendement(  );
            }

            if ( request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_ACTION_APPLY ) )
            {
                //enregistrement du nouveau VA , en base
                //on initialise l'id du VA en session  
                _voeuAmendementBean.setIdVoeuAmendement( nIdVA );
                strMessage = getModificationUrl( request, CONSTANTE_TYPE_STATUT_VA, nIdVA );
            }
            else
            {
                strMessage = getHomeUrl( request, CONSTANTE_TYPE_STATUT_VA );
            }

            return strMessage;
        }

        return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
    }

    /**
     * retourne une demande pour la suppression du VA choisi
     * @param request la requete HTTP
     * @return interface de confirmation de suppression
     */
    public String getSuppressionVoeuAmendement( HttpServletRequest request )
    {
        UrlItem url;
        String strMessage;
        String strIdVoeuAmendement = request.getParameter( OdsParameters.ID_VOEUAMENDEMENT );

        try
        {
            if ( null != request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ) )
            {
                VoeuAmendement voeuAmendement = VoeuAmendementHome.findByPrimaryKey( Integer.parseInt( 
                            strIdVoeuAmendement ), getPlugin(  ) );

                /*
                 * GESTION DES PROFILS
                 * -------------------
                 * Si l'utilisateur n'est pas autorisé a effectuer cette fonction il est renvoyé
                 * vers une page lui indiquant qu'il n'a pas les droits nécessaires.
                 */
                if ( voeuAmendement.getEnLigne(  ) &&
                        !RBACService.isAuthorized( VoeuxAmendementsResourceIdService.VOEUX_AMENDEMENTS,
                            RBAC.WILDCARD_RESOURCES_ID, VoeuxAmendementsResourceIdService.PERMISSION_SUPPRESSION,
                            getUser(  ) ) )
                {
                    return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                        AdminMessage.TYPE_ERROR );
                }

                if ( voeuAmendement.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                        voeuAmendement.getType(  ).equals( CONSTANTE_TYPE_LR ) )
                {
                    strMessage = MESSAGE_CONFIRMDELETEAMENDEMENT;
                    url = new UrlItem( JSP_URL_DO_SUPPRESSIONAMENDEMENT );
                }
                else if ( voeuAmendement.getType(  ).equals( CONSTANTE_TYPE_VR ) )
                {
                    strMessage = MESSAGE_CONFIRMDELETEVOEU;
                    url = new UrlItem( JSP_URL_DO_SUPPRESSIONVOEURATTACHE );
                }
                else
                {
                    url = new UrlItem( JSP_URL_DO_SUPPRESSIONVOEUNONRATTACHE );
                    strMessage = MESSAGE_CONFIRMDELETEVOEU;
                }

                url.addParameter( OdsParameters.ID_VOEUAMENDEMENT, strIdVoeuAmendement );

                return AdminMessageService.getMessageUrl( request, strMessage, url.getUrl(  ),
                    AdminMessage.TYPE_CONFIRMATION );
            }
            else
            {
                return getVoeuAmendementList( request, OdsConstants.CONSTANTE_CHAINE_VIDE );
            }
        }
        catch ( NumberFormatException ne )
        {
            AppLogService.error( ne );

            return getVoeuAmendementList( request, OdsConstants.CONSTANTE_CHAINE_VIDE );
        }
    }

    /**
     * retourne un avertissement pour signaler que le VA n'est associé à aucun PDD
     * @param request la requete HTTP
     * @return interface de confirmation de suppression
     */
    public String getAvertissementEnregistrementSansPDD( HttpServletRequest request )
    {
        UrlItem url;
        String strMessage;

        if ( _bUrlRetour )
        {
            String strReturn = null;
            strReturn = ( AppPathService.getBaseUrl( request ) + _strUrlRetour + "&" + OdsParameters.ID_VOEUAMENDEMENT +
                "=" + _voeuAmendementBean.getIdVoeuAmendement(  ) );

            if ( OrdreDuJourJspBean.JSP_SELECTION_VOEU_RETOUR.equals( _strUrlRetour ) )
            {
                strReturn += ( "&" + OdsParameters.ID_VOEUAMENDEMENT + "=" +
                _voeuAmendementBean.getIdVoeuAmendement(  ) );
            }

            url = new UrlItem( AppPathService.getBaseUrl( request ) + strReturn );

            _bUrlRetour = false;
            _strUrlRetour = null;
        }
        else
        {
            url = new UrlItem( getHomeUrl( request, _voeuAmendementBean.getType(  ) ) );
        }

        strMessage = MESSAGE_AVERTISSEMENT_ENREGISTREMENT;

        return AdminMessageService.getMessageUrl( request, strMessage, url.getUrl(  ), AdminMessage.TYPE_WARNING );
    }

    /**
     * retourne une demande de confirmation pour l'assignation du statut sélectionné
     * pour tous les VA dont le statut n'est pas défini.
     * @param request la requete HTTP
     * @return interface de confirmation de suppression
     */
    public String getModificationStatutsNonDefinis( HttpServletRequest request )
    {
        UrlItem url;

        // On récupère les id necessaires
        String strIdFormationConseil = request.getParameter( OdsParameters.ID_FORMATION_CONSEIL );
        String strIdSeance = request.getParameter( OdsParameters.ID_SEANCE );
        String strIdStatut = request.getParameter( OdsParameters.ID_STATUT );

        if ( ( strIdFormationConseil != null ) &&
                !strIdFormationConseil.trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) && ( strIdSeance != null ) &&
                !strIdSeance.trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) && ( strIdStatut != null ) &&
                !strIdStatut.trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            int nIdFormationConseil = -1;
            int nIdSeance = -1;
            int nIdStatut = -1;

            try
            {
                nIdFormationConseil = Integer.parseInt( strIdFormationConseil );
                nIdSeance = Integer.parseInt( strIdSeance );
                nIdStatut = Integer.parseInt( strIdStatut );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
            }

            Statut statut = StatutHome.findByPrimaryKey( nIdStatut, getPlugin(  ) );

            if ( statut != null )
            {
                Object[] messagesArgs = { '"' + statut.getLibelle(  ) + '"' };

                url = new UrlItem( JSP_URL_DO_MODIFICATION_STATUTS_NON_DEFINIS );

                url.addParameter( OdsParameters.ID_FORMATION_CONSEIL, nIdFormationConseil );
                url.addParameter( OdsParameters.ID_SEANCE, nIdSeance );
                url.addParameter( OdsParameters.ID_STATUT, nIdStatut );

                return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_ASSIGN_STATUT, messagesArgs,
                    url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
            }
        }

        return getHomeUrl( request );
    }

    /**
     * supprime le voeuAmendement choisi
     * @param request la requete HTTP
     * @return url de gestion des voeux amendements
     */
    public String doSuppressionVoeuAmendement( HttpServletRequest request )
    {
        if ( null != request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ) )
        {
            int nIdVA;

            try
            {
                nIdVA = Integer.parseInt( request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getHomeUrl( request );
            }

            VoeuAmendement voeuAmendement = VoeuAmendementHome.findByPrimaryKey( nIdVA, getPlugin(  ) );

            /*
             * GESTION DES PROFILS
             * -------------------
             * Si l'utilisateur n'est pas autorisé a effectuer cette fonction il est renvoyé
             * vers une page lui indiquant qu'il n'a pas les droits nécessaires.
             */
            if ( voeuAmendement.getEnLigne(  ) &&
                    !RBACService.isAuthorized( VoeuxAmendementsResourceIdService.VOEUX_AMENDEMENTS,
                        RBAC.WILDCARD_RESOURCES_ID, VoeuxAmendementsResourceIdService.PERMISSION_SUPPRESSION,
                        getUser(  ) ) )
            {
                return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                    AdminMessage.TYPE_ERROR );
            }

            voeuAmendement.setFichier( FichierHome.findByPrimaryKey( voeuAmendement.getFichier(  ).getId(  ),
                    getPlugin(  ) ) );

            FichierPhysique fichierPhysique = FichierPhysiqueHome.findByPrimaryKey( voeuAmendement.getFichier(  )
                                                                                                  .getFichier(  )
                                                                                                  .getIdFichier(  ),
                    getPlugin(  ) );

            FichierPhysique fichierPhysiqueDelib = null;

            if ( isGestionAval(  ) && ( voeuAmendement.getDeliberation(  ) != null ) )
            {
                voeuAmendement.setDeliberation( FichierHome.findByPrimaryKey( 
                        voeuAmendement.getDeliberation(  ).getId(  ), getPlugin(  ) ) );

                fichierPhysiqueDelib = FichierPhysiqueHome.findByPrimaryKey( voeuAmendement.getDeliberation(  )
                                                                                           .getFichier(  ).getIdFichier(  ),
                        getPlugin(  ) );
            }

            //suppression des éléments de releve travaux
            ElementReleveDesTravauxHome.remove( voeuAmendement, getPlugin(  ) );
            //suppression de l'historique du VA 
            HistoriqueHome.remove( voeuAmendement, getPlugin(  ) );
            //suppression du VA en base
            VoeuAmendementHome.remove( voeuAmendement, getPlugin(  ) );

            //suppression de l'historique du fichier associé au VA 
            HistoriqueHome.remove( voeuAmendement.getFichier(  ), getPlugin(  ) );
            //suppression du fichier
            FichierHome.remove( voeuAmendement.getFichier(  ), getPlugin(  ) );
            FichierPhysiqueHome.remove( fichierPhysique, getPlugin(  ) );

            if ( isGestionAval(  ) && ( voeuAmendement.getDeliberation(  ) != null ) )
            {
                //suppression de l'historique du fichier de délibération associé au VA 
                HistoriqueHome.remove( voeuAmendement.getDeliberation(  ), getPlugin(  ) );
                //suppression du fichier de délibération
                FichierHome.remove( voeuAmendement.getDeliberation(  ), getPlugin(  ) );
                FichierPhysiqueHome.remove( fichierPhysiqueDelib, getPlugin(  ) );
            }

            //on vide l'objet voeuAmendement en session
            String strType = voeuAmendement.getType(  );
            _voeuAmendementBean = new VoeuAmendement(  );

            return getHomeUrl( request, strType );
        }

        return getHomeUrl( request, _voeuAmendementBean.getType(  ) );
    }

    /**
     * affecte un statut à tous les vA dont le statut n'est pas defini
     * pour une formation conseil et une seance donnée
     * @param request la requete HTTP
     * @return url de gestion des voeux amendements
     */
    public String doModificationStatutsNonDefinis( HttpServletRequest request )
    {
        String strIdFormationConseil = request.getParameter( OdsParameters.ID_FORMATION_CONSEIL );
        String strIdSeance = request.getParameter( OdsParameters.ID_SEANCE );
        String strIdStatut = request.getParameter( OdsParameters.ID_STATUT );

        if ( ( strIdFormationConseil != null ) && ( strIdSeance != null ) && ( strIdStatut != null ) )
        {
            int nIdFormationConseil = -1;
            int nIdSeance = -1;
            int nIdStatut = -1;

            try
            {
                nIdFormationConseil = Integer.parseInt( strIdFormationConseil.trim(  ) );
                nIdSeance = Integer.parseInt( strIdSeance.trim(  ) );
                nIdStatut = Integer.parseInt( strIdStatut.trim(  ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
            }

            if ( ( nIdFormationConseil != -1 ) && ( nIdSeance != -1 ) )
            {
                VoeuAmendementFilter filter = new VoeuAmendementFilter(  );
                filter.setIdFormationConseil( nIdFormationConseil );
                filter.setIdSeance( nIdSeance );

                if ( StatutEnum.VOEU_TRANSFORME_EN_AMENDEMENT_ADOPTE.getId(  ) == nIdStatut )
                {
                    filter.setType( CONSTANTE_TYPE_ALLV );
                }
                else if ( StatutEnum.AMENDEMENT_TRANSFORME_EN_VOEU_ADOPTE.getId(  ) == nIdStatut )
                {
                    filter.setType( CONSTANTE_TYPE_A );
                }

                List<VoeuAmendement> listVA = VoeuAmendementHome.findVoeuAmendementListByFilter( filter, getPlugin(  ) );
                Statut statut = StatutHome.findByPrimaryKey( nIdStatut, getPlugin(  ) );

                for ( VoeuAmendement va : listVA )
                {
                    if ( va.getStatut(  ) == null )
                    {
                        va.setStatut( statut );
                    }

                    VoeuAmendementHome.update( va, getPlugin(  ) );
                }
            }

            return ( AppPathService.getBaseUrl( request ) + JSP_URL_GET_STATUT_VA + "?" +
            OdsParameters.ID_FORMATION_CONSEIL + "=" + nIdFormationConseil + "&" + OdsParameters.ID_SEANCE + "=" +
            nIdSeance );
        }

        return AppPathService.getBaseUrl( request ) + JSP_URL_GET_STATUT_VA;
    }

    /**
     * retourne l'interface de gestion des amendements, des voeux  rattachés ou des voeux non rattachés en fonction du type demandé .
     * @param request la requete HTTP
     * @param strType (amendement,voeu rattache,voeu non rattache)
     * @return   interface de gestion des élus
     */
    public String getVoeuAmendementList( HttpServletRequest request, String strType )
    {
        _bHasChanged = false;

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        /*
         * Pour la gestion des profils
         */
        ajouterPermissionsDansHashmap( model );

        //on initialise la variable de session nUrlRetour a false 
        _bUrlRetour = false;
        _strUrlRetour = null;
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        //on initialise le filtre 
        VoeuAmendementFilter voeuAmendementFilter = new VoeuAmendementFilter(  );
        _nIdFormationConseilSelected = getIdFormationConseil( request, voeuAmendementFilter );
        voeuAmendementFilter.setType( strType );

        if ( isGestionAval(  ) )
        {
            List<Seance> listSeance = SeanceHome.findOldSeance( getPlugin(  ) );

            StatutFilter filter = new StatutFilter(  );

            if ( strType.equals( CONSTANTE_TYPE_VR ) || strType.equals( CONSTANTE_TYPE_VNR ) )
            {
                filter.setPourVoeu( true );
            }

            if ( strType.equals( CONSTANTE_TYPE_A ) || strType.equals( CONSTANTE_TYPE_LR ) )
            {
                filter.setPourAmendement( true );
            }

            ReferenceList refListStatuts = new ReferenceList(  );
            initRefListStatut( filter, refListStatuts );

            if ( _seance == null )
            {
                _nIdSeanceSelected = getIdSeance( request, voeuAmendementFilter );
            }
            else
            {
                resetPageIndex(  );
                _nIdSeanceSelected = _seance.getIdSeance(  );
            }

            _nIdStatutSelected = getIdStatut( request, voeuAmendementFilter );
            voeuAmendementFilter.setIdSeance( _nIdSeanceSelected );
            voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_CODE_FASCICULE );
            voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_REFERENCE_VA );

            model.put( OdsMarks.MARK_GESTION_AVAL, true );
            model.put( MARK_LISTE_SEANCES, listSeance );
            model.put( MARK_LISTE_STATUTS, refListStatuts );
            model.put( MARK_ID_SEANCE_SELECTED, _nIdSeanceSelected );
            model.put( MARK_ID_STATUT_SELECTED, _nIdStatutSelected );
        }
        else
        {
            _nIdPublicationSelected = getIdPublie( request, voeuAmendementFilter );

            if ( _seance != null )
            {
                voeuAmendementFilter.setIdSeance( _seance.getIdSeance(  ) );
            }

            voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_NUMERO_ORDRE );
            voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_DATE_PUBLICATION );

            ReferenceList refListPublie = new ReferenceList(  );
            initRefListPublie( refListPublie );

            model.put( OdsMarks.MARK_GESTION_AVAL, false );
            model.put( MARK_LISTE_PUBLIE, refListPublie );
            model.put( MARK_ID_PUBLICATION_SELECTED, _nIdPublicationSelected );
            model.put( Seance.MARK_SEANCE, _seance );
        }

        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        List<VoeuAmendement> voeuAmendements;
        voeuAmendements = VoeuAmendementHome.findVoeuAmendementListByFilter( voeuAmendementFilter, getPlugin(  ) );

        String strHomeUrl = getHomeUrl( request, strType );

        Paginator paginator = new Paginator( voeuAmendements, _nItemsPerPage, strHomeUrl,
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        ReferenceList refListFormationConseil = new ReferenceList(  );
        initRefListFormationConseil( false, refListFormationConseil );

        model.put( MARK_LISTE_FORMATION_CONSEIL, refListFormationConseil );
        model.put( MARK_ID_FORMATION_CONSEIL_SELECTED, _nIdFormationConseilSelected );
        model.put( MARK_LISTE_VOEUAMENDEMENT, paginator.getPageItems(  ) );
        model.put( OdsMarks.MARK_NOMBRE_RESULTATS, voeuAmendements.size(  ) );
        model.put( OdsMarks.MARK_PAGINATOR, paginator );
        model.put( OdsMarks.MARK_NB_ITEMS_PER_PAGE, OdsConstants.CONSTANTE_CHAINE_VIDE + _nItemsPerPage );

        HtmlTemplate template;

        if ( strType.equals( CONSTANTE_TYPE_A ) || strType.equals( CONSTANTE_TYPE_LR ) )
        {
            template = AppTemplateService.getTemplate( TEMPLATE_LISTE_AMENDEMENT, getLocale(  ), model );
        }
        else if ( strType.equals( CONSTANTE_TYPE_VR ) )
        {
            template = AppTemplateService.getTemplate( TEMPLATE_LISTE_VOEU_RATTACHE, getLocale(  ), model );
        }
        else if ( strType.equals( CONSTANTE_TYPE_VNR ) )
        {
            template = AppTemplateService.getTemplate( TEMPLATE_LISTE_VOEU_NON_RATACHE, getLocale(  ), model );
        }
        else
        {
            template = AppTemplateService.getTemplate( TEMPLATE_LISTE_AMENDEMENT, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * retourne l'interface de gestion des amendements, des voeux  rattachés ou des voeux non rattachés en fonction du type demandé .
     * @param request la requete HTTP
     * @param strType (amendement,voeu rattache,voeu non rattache)
     * @param strUrlJSP l'url du JSP
     * @param strUrlRetour l'url de retour
     * @return   interface de gestion des élus
     */
    public String getVoeuAmendementList( HttpServletRequest request, String strType, String strUrlJSP,
        String strUrlRetour )
    {
        String strUrlAppelant = strUrlJSP;
        int nIdOdj = -1;
        _bHasChanged = false;

        HashMap<String, Object> model = new HashMap<String, Object>(  );

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        Seance seanceLocal = null;

        if ( ( request.getParameter( OdsParameters.ID_SEANCE ) != null ) &&
                !request.getParameter( OdsParameters.ID_SEANCE ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            int nIdSeanceLocal = -1;

            try
            {
                nIdSeanceLocal = Integer.parseInt( request.getParameter( OdsParameters.ID_SEANCE ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            seanceLocal = SeanceHome.findByPrimaryKey( nIdSeanceLocal, getPlugin(  ) );
        }

        if ( ( seanceLocal == null ) && isGestionAval(  ) )
        {
            if ( ( _voeuAmendementBean.getFascicule(  ) != null ) &&
                    ( _voeuAmendementBean.getFascicule(  ).getSeance(  ) == null ) )
            {
                int nIdFascicule = _voeuAmendementBean.getFascicule(  ).getIdFascicule(  );
                Fascicule fascicule = FasciculeHome.findByPrimaryKey( nIdFascicule, getPlugin(  ) );

                fascicule.setSeance( _seance );
                _voeuAmendementBean.setFascicule( fascicule );
            }

            seanceLocal = _voeuAmendementBean.getFascicule(  ).getSeance(  );
        }
        else if ( ( seanceLocal == null ) && !isGestionAval(  ) )
        {
            seanceLocal = SeanceHome.getProchaineSeance( getPlugin(  ) );
        }

        model.put( OdsMarks.MARK_GESTION_AVAL, isGestionAval(  ) );

        //initialisation du filtre 
        VoeuAmendementFilter voeuAmendementFilter = new VoeuAmendementFilter(  );
        voeuAmendementFilter.setType( strType );

        List<VoeuAmendement> voeuAmendements;

        if ( strUrlJSP.equals( OrdreDuJourJspBean.JSP_SELECTION_VOEU ) ||
                strUrlJSP.equals( OrdreDuJourJspBean.JSP_SELECTION_VOEU ) )
        {
            //si la liste des vas est appellé par le module d'administarion de l'ordre du jour
            //les vas doivent etre de la même formation de conseil  et de la meme seance que l’ordre du jour 
            if ( ( request.getParameter( OdsParameters.ID_ODJ ) != null ) &&
                    !request.getParameter( OdsParameters.ID_ODJ ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                try
                {
                    OrdreDuJour ordreDuJour;

                    nIdOdj = Integer.parseInt( request.getParameter( OdsParameters.ID_ODJ ) );
                    ordreDuJour = OrdreDuJourHome.findByPrimaryKey( nIdOdj, getPlugin(  ) );
                    model.put( OrdreDuJour.MARK_ODJ, ordreDuJour );
                    //utilisé par le paginator
                    strUrlAppelant += ( "?" + OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_ODJ + "=" + nIdOdj );

                    if ( ordreDuJour.getFormationConseil(  ) != null )
                    {
                        voeuAmendementFilter.setIdFormationConseil( ordreDuJour.getFormationConseil(  )
                                                                               .getIdFormationConseil(  ) );
                    }

                    if ( ordreDuJour.getSeance(  ) != null )
                    {
                        voeuAmendementFilter.setIdSeance( ordreDuJour.getSeance(  ).getIdSeance(  ) );
                        seanceLocal = SeanceHome.findByPrimaryKey( ordreDuJour.getSeance(  ).getIdSeance(  ),
                                getPlugin(  ) );
                    }
                }
                catch ( NumberFormatException ne )
                {
                    AppLogService.error( ne );
                }
            }
        }
        else if ( estPourSelectionParents( strUrlJSP ) )
        {
            voeuAmendementFilter.setTypeOrdreDuJour( TypeOrdreDuJourEnum.DEFINITIF.getId(  ) );
            _nIdFormationConseilSelected = getIdFormationConseil( request, voeuAmendementFilter );
            _nIdPublicationSelected = getIdPublie( request, voeuAmendementFilter );
            voeuAmendementFilter.setIdSeance( seanceLocal.getIdSeance(  ) );
        }
        else
        {
            _nIdFormationConseilSelected = getIdFormationConseil( request, voeuAmendementFilter );
            _nIdPublicationSelected = getIdPublie( request, voeuAmendementFilter );
            voeuAmendementFilter.setIdSeance( seanceLocal.getIdSeance(  ) );
        }

        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        voeuAmendements = VoeuAmendementHome.findVoeuAmendementListByFilter( voeuAmendementFilter, getPlugin(  ) );

        // On instancie un tableau qui va contenir tous les éléments qui vont être supprimés
        List<VoeuAmendement> vaToDelete = new ArrayList<VoeuAmendement>(  );

        /*
         * Traitement pour les parents lors d'une modification d'un VA
         */
        if ( ( request.getParameter( OdsParameters.ID_SEANCE ) == null ) && ( voeuAmendements != null ) &&
                estPourSelectionParents( strUrlJSP ) )
        {
            for ( VoeuAmendement va : voeuAmendements )
            {
                // Si la formation du conseil référencée est différente => A SUPPRIMER
                // Si la séance est différente => A SUPPRIMER
                // Si le VA est déjà le parent => A SUPPRIMER
                // Si le VA est le voeuamendement courant => A SUPPRIMER
                if ( ( va.getIdVoeuAmendement(  ) == _voeuAmendementBean.getIdVoeuAmendement(  ) ) ||
                        ( ( _voeuAmendementBean.getFormationConseil(  ) != null ) &&
                        ( va.getFormationConseil(  ).getIdFormationConseil(  ) != _voeuAmendementBean.getFormationConseil(  )
                                                                                                         .getIdFormationConseil(  ) ) ) ||
                        ( ( va.getFascicule(  ).getSeance(  ) != null ) &&
                        ( _voeuAmendementBean.getFascicule(  ).getSeance(  ) != null ) &&
                        ( va.getFascicule(  ).getSeance(  ).getIdSeance(  ) != _voeuAmendementBean.getFascicule(  )
                                                                                                      .getSeance(  )
                                                                                                      .getIdSeance(  ) ) ) )
                {
                    vaToDelete.add( va );
                }
            }

            if ( _voeuAmendementBean.getFormationConseil(  ) != null )
            {
                model.put( MARK_FORMATION_CONSEIL, _voeuAmendementBean.getFormationConseil(  ) );
            }
        }

        /*
         * Traitement pour les Releves de Travaux
         */
        if ( ( voeuAmendements != null ) &&
                ( strUrlJSP.equals( ReleveDesTravauxJspBean.CONSTANTE_URL_JSP_APPELANT_AMENDEMENT ) ||
                strUrlJSP.equals( ReleveDesTravauxJspBean.CONSTANTE_URL_JSP_APPELANT_VOEU_RATTACHE ) ||
                strUrlJSP.equals( ReleveDesTravauxJspBean.CONSTANTE_URL_JSP_APPELANT_VOEU_NON_RATTACHE ) ) )
        {
            int nIdReleve = -1;

            if ( ( request.getParameter( OdsParameters.ID_RELEVE ) != null ) &&
                    !request.getParameter( OdsParameters.ID_RELEVE ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                try
                {
                    nIdReleve = Integer.parseInt( request.getParameter( OdsParameters.ID_RELEVE ) );
                }
                catch ( NumberFormatException nfe )
                {
                    AppLogService.error( nfe );
                }
            }

            ReleveDesTravaux releve = ReleveDesTravauxHome.findByPrimaryKey( nIdReleve, getPlugin(  ) );
            model.put( ReleveDesTravaux.MARK_RELEVE, releve );

            List<ElementReleveDesTravaux> listVAElementReleveDesTravaux = ElementReleveDesTravauxHome.findElementReleveDesTravauxByReleveVA( releve,
                    getPlugin(  ) );

            for ( VoeuAmendement va : voeuAmendements )
            {
                // Si la commission référencée est NULL => A SUPPRIMER
                if ( va.getCommission(  ) == null )
                {
                    vaToDelete.add( va );
                }

                // Si le VA référence une autre commission => A SUPPRIMER
                else if ( va.getCommission(  ).getIdCommission(  ) != releve.getCommission(  ).getIdCommission(  ) )
                {
                    vaToDelete.add( va );
                }

                // Si le VA est déjà dans la liste des éléments de relevé de travaux => A SUPPRIMER
                else
                {
                    if ( listVAElementReleveDesTravaux != null )
                    {
                        for ( ElementReleveDesTravaux elementVA : listVAElementReleveDesTravaux )
                        {
                            if ( va.getIdVoeuAmendement(  ) == elementVA.getVoeuAmendement(  ).getIdVoeuAmendement(  ) )
                            {
                                vaToDelete.add( va );

                                break;
                            }
                        }
                    }
                }
            }
        }

        /*
         * Traitement pour les PDD
         */
        if ( ( voeuAmendements != null ) &&
                ( strUrlJSP.equals( ProjetDeDeliberationJspBean.CONSTANTE_URL_JSP_APPELANT_AMENDEMENT ) ||
                strUrlJSP.equals( ProjetDeDeliberationJspBean.CONSTANTE_URL_JSP_APPELANT_VOEUX ) ||
                strUrlJSP.equals( ProjetDeDeliberationJspBean.CONSTANTE_URL_JSP_APPELANT_AMENDEMENT_GESTION_AVAL ) ||
                strUrlJSP.equals( ProjetDeDeliberationJspBean.CONSTANTE_URL_JSP_APPELANT_VOEUX_GESTION_AVAL ) ) )
        {
            String strIdPDD = request.getParameter( OdsParameters.ID_PDD );

            if ( strIdPDD != null )
            {
                int pddPrimaryKey = -1;

                try
                {
                    pddPrimaryKey = Integer.parseInt( strIdPDD );
                }
                catch ( NumberFormatException nfe )
                {
                    AppLogService.error( nfe );
                }

                model.put( OdsParameters.ID_PDD, strIdPDD );

                PDD pdd = PDDHome.findByPrimaryKey( pddPrimaryKey, getPlugin(  ) );

                model.put( MARK_FORMATION_CONSEIL, pdd.getFormationConseil(  ) );

                if ( pdd != null )
                {
                    for ( VoeuAmendement va : voeuAmendements )
                    {
                        // Les vœux ou amendements disponibles sont ceux de la même formation de conseil que le projet 
                        if ( ( va.getFormationConseil(  ) == null ) ||
                                ( va.getFormationConseil(  ).getIdFormationConseil(  ) != pdd.getFormationConseil(  )
                                                                                                 .getIdFormationConseil(  ) ) )
                        {
                            vaToDelete.add( va );
                        }

                        // Et qui ne sont pas déjà rattachés à ce projet.
                        else if ( pdd.containsVoeuxAmandements( va ) )
                        {
                            vaToDelete.add( va );
                        }
                    }
                }
            }
        }

        /*
         * Traitement pour l'ordre du jour
         */
        if ( ( voeuAmendements != null ) && ( strUrlJSP.equals( OrdreDuJourJspBean.JSP_SELECTION_VOEU ) ) )
        {
            List<EntreeOrdreDuJour> listEntree = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( nIdOdj,
                    getPlugin(  ) );

            if ( listEntree != null )
            {
                for ( VoeuAmendement va : voeuAmendements )
                {
                    for ( EntreeOrdreDuJour entreeOrdreDuJour : listEntree )
                    {
                        if ( ( entreeOrdreDuJour.getVoeuAmendement(  ) != null ) &&
                                ( va.getIdVoeuAmendement(  ) == entreeOrdreDuJour.getVoeuAmendement(  )
                                                                                     .getIdVoeuAmendement(  ) ) )
                        {
                            vaToDelete.add( va );

                            break;
                        }
                    }
                }
            }
        }

        //Suppression des VA qui ne doivent pas etre affichés
        voeuAmendements.removeAll( vaToDelete );

        ReferenceList refListFormationConseil = new ReferenceList(  );
        initRefListFormationConseil( false, refListFormationConseil );

        ReferenceList refListPublie = new ReferenceList(  );
        initRefListPublie( refListPublie );

        Paginator paginator = new Paginator( voeuAmendements, _nItemsPerPage, strUrlAppelant,
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        model.put( MARK_LISTE_FORMATION_CONSEIL, refListFormationConseil );
        model.put( MARK_ID_FORMATION_CONSEIL_SELECTED, _nIdFormationConseilSelected );
        model.put( MARK_LISTE_PUBLIE, refListPublie );
        model.put( MARK_ID_PUBLICATION_SELECTED, _nIdPublicationSelected );
        model.put( MARK_LISTE_VOEUAMENDEMENT, paginator.getPageItems(  ) );
        model.put( OdsMarks.MARK_NOMBRE_RESULTATS, voeuAmendements.size(  ) );
        model.put( Seance.MARK_SEANCE, seanceLocal );
        model.put( OdsMarks.MARK_PAGINATOR, paginator );
        model.put( OdsMarks.MARK_NB_ITEMS_PER_PAGE, OdsConstants.CONSTANTE_CHAINE_VIDE + _nItemsPerPage );
        model.put( MARK_URL_JSP_APPELANT, strUrlJSP );
        model.put( MARK_URL_RETOUR, strUrlRetour );

        HtmlTemplate template;

        if ( strType.equals( CONSTANTE_TYPE_A ) )
        {
            template = AppTemplateService.getTemplate( TEMPLATE_LISTE_AMENDEMENT_SELECTION, getLocale(  ), model );
        }
        else if ( strType.equals( CONSTANTE_TYPE_VR ) )
        {
            template = AppTemplateService.getTemplate( TEMPLATE_LISTE_VOEU_RATTACHE_SELECTION, getLocale(  ), model );
        }
        else if ( strType.equals( CONSTANTE_TYPE_VNR ) )
        {
            template = AppTemplateService.getTemplate( TEMPLATE_LISTE_VOEU_NON_RATACHE_SELECTION, getLocale(  ), model );
        }
        else
        {
            template = AppTemplateService.getTemplate( TEMPLATE_LISTE_AMENDEMENT_SELECTION, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * retourne l'interface de gestion des amendements, des voeux  rattachés ou des voeux non rattachés en fonction du type demandé .
     * @param request la requete HTTP
     * @param strType (amendement,voeu rattache,voeu non rattache)
     * @param strUrlJSP l'url du JSP
     * @param strUrlRetour l'url de retour
     * @return   interface de gestion des élus
     */
    public String getVoeuListForOdj( HttpServletRequest request )
    {
        OrdreDuJour ordreDuJour = new OrdreDuJour(  );
        Plugin plugin = getPlugin(  );
        HashMap<String, Object> model = new HashMap<String, Object>(  );

        Seance seanceLocal = null;

        //initialisation du filtre 
        VoeuAmendementFilter voeuAmendementFilter = new VoeuAmendementFilter(  );
        voeuAmendementFilter.setType( CONSTANTE_TYPE_VNR );

        //les vas doivent etre de la même formation de conseil  et de la meme seance que l’ordre du jour 
        if ( ( request.getParameter( OdsParameters.ID_ENTREE ) != null ) &&
                !request.getParameter( OdsParameters.ID_ENTREE ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            try
            {
                int nIdEntree = Integer.parseInt( request.getParameter( OdsParameters.ID_ENTREE ) );
                EntreeOrdreDuJour entreeOrdreDuJour = EntreeOrdreDuJourHome.findByPrimaryKey( nIdEntree, plugin );
                ordreDuJour = OrdreDuJourHome.findByPrimaryKey( entreeOrdreDuJour.getOrdreDuJour(  ).getIdOrdreDuJour(  ),
                        plugin );
                entreeOrdreDuJour.setOrdreDuJour( ordreDuJour );
                model.put( MARK_ENTREE, entreeOrdreDuJour );

                if ( ordreDuJour.getFormationConseil(  ) != null )
                {
                    voeuAmendementFilter.setIdFormationConseil( ordreDuJour.getFormationConseil(  )
                                                                           .getIdFormationConseil(  ) );
                }

                if ( ordreDuJour.getSeance(  ) != null )
                {
                    voeuAmendementFilter.setIdSeance( ordreDuJour.getSeance(  ).getIdSeance(  ) );
                    seanceLocal = SeanceHome.findByPrimaryKey( ordreDuJour.getSeance(  ).getIdSeance(  ), getPlugin(  ) );
                }
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
            }
        }

        // on récupère la liste des VA 
        List<VoeuAmendement> voeuAmendements = VoeuAmendementHome.findVoeuAmendementListByFilter( voeuAmendementFilter,
                getPlugin(  ) );

        // On instancie un tableau qui va contenir tous les éléments qui vont être supprimés
        List<VoeuAmendement> vaToDelete = new ArrayList<VoeuAmendement>(  );

        //traitement pour l'ordre du jour
        List<EntreeOrdreDuJour> listEntree = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJour.getIdOrdreDuJour(  ),
                getPlugin(  ) );

        if ( listEntree != null )
        {
            for ( VoeuAmendement va : voeuAmendements )
            {
                for ( EntreeOrdreDuJour entreeOrdreDuJour : listEntree )
                {
                    if ( ( entreeOrdreDuJour.getVoeuAmendement(  ) != null ) &&
                            ( va.getIdVoeuAmendement(  ) == entreeOrdreDuJour.getVoeuAmendement(  ).getIdVoeuAmendement(  ) ) )
                    {
                        vaToDelete.add( va );

                        break;
                    }
                }
            }
        }

        //Suppression des VA qui ne doivent pas etre affichés
        voeuAmendements.removeAll( vaToDelete );

        model.put( MARK_LISTE_VOEUAMENDEMENT, voeuAmendements );
        model.put( OdsMarks.MARK_NOMBRE_RESULTATS, voeuAmendements.size(  ) );
        model.put( Seance.MARK_SEANCE, seanceLocal );

        HtmlTemplate template;

        template = AppTemplateService.getTemplate( TEMPLATE_LISTE_VOEU_NON_RATACHE_SELECTION_FOR_ODJ, getLocale(  ),
                model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * retourne la liasse des voeux amendements présent en base
     * @param request
     * @return
     */
    public String getLiasse( HttpServletRequest request )
    {
        // initialisation 
        _bUrlRetour = false;
        _strUrlRetour = null;

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        VoeuAmendementFilter voeuAmendementFilter = new VoeuAmendementFilter(  );

        if ( _nIdFormationConseilSelected != -1 )
        {
            _nIdFormationConseilSelected = getIdFormationConseil( request, voeuAmendementFilter );
        }
        else
        {
            _nIdFormationConseilSelected = 0;
            voeuAmendementFilter.setIdFormationConseil( _nIdFormationConseilSelected );
        }

        List<VoeuAmendement> voeuAmendements;

        if ( isGestionAval(  ) )
        {
            List<Seance> listSeance = SeanceHome.findOldSeance( getPlugin(  ) );

            ReferenceList refListStatuts = new ReferenceList(  );
            initRefListStatut( null, refListStatuts );

            // on met dans la hashmap la liste des statuts uniquement pour les voeux
            // afin d'éviter le traitement par freemarker
            ReferenceList refListStatutsVoeux = new ReferenceList(  );
            StatutFilter filterVoeux = new StatutFilter(  );
            filterVoeux.setPourVoeu( true );
            initRefListStatut( filterVoeux, refListStatutsVoeux );

            // on met dans la hashmap la liste des statuts uniquement pour les amendements
            // afin d'éviter le traitement par freemarker
            ReferenceList refListStatutsAmendements = new ReferenceList(  );
            StatutFilter filterAmendements = new StatutFilter(  );
            filterAmendements.setPourAmendement( true );
            initRefListStatut( filterAmendements, refListStatutsAmendements );

            if ( _seance == null )
            {
                _nIdSeanceSelected = getIdSeance( request, voeuAmendementFilter );
            }
            else
            {
                resetPageIndex(  );
                _nIdSeanceSelected = _seance.getIdSeance(  );
            }

            _nIdStatutSelected = -1;

            voeuAmendementFilter.setIdSeance( _nIdSeanceSelected );
            voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_CODE_FASCICULE );
            voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_REFERENCE_VA );

            model.put( OdsMarks.MARK_GESTION_AVAL, true );
            model.put( MARK_LISTE_SEANCES, listSeance );
            model.put( MARK_ID_SEANCE_SELECTED, _nIdSeanceSelected );
            model.put( MARK_LISTE_STATUTS, refListStatuts );
            model.put( MARK_ID_STATUT_SELECTED, _nIdStatutSelected );
            model.put( MARK_LISTE_STATUTS_AMENDEMENTS, refListStatutsAmendements );
            model.put( MARK_LISTE_STATUTS_VOEUX, refListStatutsVoeux );

            voeuAmendements = VoeuAmendementHome.findVoeuAmendementListByFilter( voeuAmendementFilter, getPlugin(  ) );
        }
        else
        {
            _nIdPublicationSelected = getIdPublie( request, voeuAmendementFilter );

            if ( _seance != null )
            {
                voeuAmendementFilter.setIdSeance( _seance.getIdSeance(  ) );
            }

            //order by
            voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_NUMERO_ORDRE );
            voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_REFERENCE_VA );

            model.put( OdsMarks.MARK_GESTION_AVAL, false );
            model.put( Seance.MARK_SEANCE, _seance );

            voeuAmendements = new ArrayList<VoeuAmendement>(  );

            if ( _seance != null )
            {
                voeuAmendements = VoeuAmendementHome.findLiasseByFilter( voeuAmendementFilter, getPlugin(  ) );                        
            }
        }

        Collections.sort(voeuAmendements, new VoeuAmendementLiasseComparator());

        
        ReferenceList refListFormationConseil = new ReferenceList(  );
        initRefListFormationConseil( true, refListFormationConseil );

        model.put( MARK_LISTE_FORMATION_CONSEIL, refListFormationConseil );
        model.put( MARK_ID_FORMATION_CONSEIL_SELECTED, _nIdFormationConseilSelected );
        model.put( MARK_LISTE_VOEUAMENDEMENT, new ArrayList<VoeuAmendement>(voeuAmendements) );
        model.put( OdsMarks.MARK_NOMBRE_RESULTATS, voeuAmendements.size(  ) );

        HtmlTemplate template;
        template = AppTemplateService.getTemplate( TEMPLATE_LIASSE, getLocale(  ), model );
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * méthode qui recupère les parametres fournis par l'utilisateur en vue de les insérer dans le voeuamendementBean stocké en session
     * @param request
     * @return retourne true si aucune erreur ne c'est produite
     */
    private Boolean setVoeuAmendementBean( HttpServletRequest request )
    {
        if ( isValideForme( request ) )
        {
            int nIdFormationConseil;
            int nIdCommission;
            int nIdFascicule;

            try
            {
                nIdFormationConseil = Integer.parseInt( request.getParameter( OdsParameters.ID_FORMATION_CONSEIL ) );
                nIdCommission = Integer.parseInt( request.getParameter( OdsParameters.ID_COMMISSION ) );
                nIdFascicule = Integer.parseInt( request.getParameter( OdsParameters.ID_FASCICULE ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return false;
            }

            // Récupération de formationConseil en vue de  l'insérer dans le VA en session
            if ( _voeuAmendementBean.getFormationConseil(  ) == null )
            {
                _voeuAmendementBean.setFormationConseil( new FormationConseil(  ) );
            }

            if ( _voeuAmendementBean.getFormationConseil(  ).getIdFormationConseil(  ) != nIdFormationConseil )
            {
                _voeuAmendementBean.getFormationConseil(  ).setIdFormationConseil( nIdFormationConseil );
                _bHasChanged = true;
            }

            // Récupération de la commission en vue de l'insérer dans le VA en session
            if ( _voeuAmendementBean.getCommission(  ) == null )
            {
                if ( nIdCommission > 0 )
                {
                    _bHasChanged = true;
                }

                _voeuAmendementBean.setCommission( new Commission(  ) );
                _voeuAmendementBean.getCommission(  ).setIdCommission( -1 );
            }

            if ( _voeuAmendementBean.getCommission(  ).getIdCommission(  ) != nIdCommission )
            {
                _bHasChanged = true;
            }

            if ( -1 == nIdCommission )
            {
                _voeuAmendementBean.setCommission( null );
            }
            else
            {
                _voeuAmendementBean.getCommission(  ).setIdCommission( nIdCommission );
            }

            // Récupération du fascicule en vue de  l'insérer dans le VA en session
            if ( _voeuAmendementBean.getFascicule(  ) == null )
            {
                _voeuAmendementBean.setFascicule( new Fascicule(  ) );
            }

            if ( _voeuAmendementBean.getFascicule(  ).getIdFascicule(  ) != nIdFascicule )
            {
                _voeuAmendementBean.getFascicule(  ).setIdFascicule( nIdFascicule );
                _bHasChanged = true;
            }

            String strFichierTexte = request.getParameter( OdsParameters.FICHIER_TXT_RECHERCHE );

            // Récupération du fichier en vue de  l'insérer dans le VA en session
            if ( ( _voeuAmendementBean.getFichier(  ) == null ) ||
                    !_voeuAmendementBean.getFichier(  ).getTexte(  ).equals( strFichierTexte ) )
            {
                _bHasChanged = true;
            }

            if ( null == _voeuAmendementBean.getFichier(  ) )
            {
                _voeuAmendementBean.setFichier( new Fichier(  ) );
            }

            _voeuAmendementBean.getFichier(  ).setTexte( request.getParameter( OdsParameters.FICHIER_TXT_RECHERCHE ) );

            // Récupération de la propriété déposéé par l'executif 
            int nDeposeExecutif = -1;

            try
            {
                if ( null != request.getParameter( OdsParameters.DEPOSE_EXECUTIF ) )
                {
                    nDeposeExecutif = Integer.parseInt( request.getParameter( OdsParameters.DEPOSE_EXECUTIF ) );
                }
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return false;
            }

            if ( ( _voeuAmendementBean.getDeposeExecutif(  ) && ( nDeposeExecutif != 1 ) ) ||
                    ( !_voeuAmendementBean.getDeposeExecutif(  ) && ( nDeposeExecutif == 1 ) ) )
            {
                _bHasChanged = true;
            }

            if ( 1 == nDeposeExecutif )
            {
                _voeuAmendementBean.setDeposeExecutif( true );
            }
            else
            {
                _voeuAmendementBean.setDeposeExecutif( false );
            }

            // Récupération de la propriété lettre rectificative
            int nLettreRectificative = -1;

            try
            {
                if ( null != request.getParameter( OdsParameters.LETTRE_RECTIFICATIVE ) )
                {
                    nLettreRectificative = Integer.parseInt( request.getParameter( OdsParameters.LETTRE_RECTIFICATIVE ) );
                }
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return false;
            }

            if ( ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) && ( nLettreRectificative != 1 ) ) ||
                    ( !_voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) && ( nLettreRectificative == 1 ) ) )
            {
                _bHasChanged = true;
            }

            if ( nLettreRectificative == 1 )
            {
                _voeuAmendementBean.setType( CONSTANTE_TYPE_LR );
            }
            else
            {
                //si le type du voeuAmendement stocké en session = lettre rectificative on le modifie en Amendement
                if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
                {
                    _voeuAmendementBean.setType( CONSTANTE_TYPE_A );
                }
            }

            // Récupération de la référence du VA
            String strReference = request.getParameter( OdsParameters.REFERENCE ).trim(  );

            if ( strReference.length(  ) > 10 )
            {
                return false;
            }
            else if ( ( _voeuAmendementBean.getReference(  ) == null ) ||
                    !_voeuAmendementBean.getReference(  ).equals( strReference ) )
            {
                _bHasChanged = true;

                if ( strReference.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    _voeuAmendementBean.setReference( null );
                }
                else
                {
                    _voeuAmendementBean.setReference( strReference );
                }
            }

            // Récupération de l'objet
            String strObjet = request.getParameter( OdsParameters.OBJET );

            if ( ( _voeuAmendementBean.getObjet(  ) == null ) || !_voeuAmendementBean.getObjet(  ).equals( strObjet ) )
            {
                _voeuAmendementBean.setObjet( strObjet );
                _bHasChanged = true;
            }

            /*
             * Dans le cas de la gestion en aval on traite les champs supplémentaires
             */
            if ( isGestionAval(  ) )
            {
                // Récupération du parent
                int nIdParent;

                if ( ( request.getParameter( OdsParameters.ID_PARENT ) != null ) &&
                        !request.getParameter( OdsParameters.ID_PARENT ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    try
                    {
                        nIdParent = Integer.parseInt( request.getParameter( OdsParameters.ID_PARENT ) );

                        VoeuAmendement parent = VoeuAmendementHome.findByPrimaryKey( nIdParent, getPlugin(  ) );
                        _voeuAmendementBean.setParent( parent );
                    }
                    catch ( NumberFormatException ne )
                    {
                        AppLogService.error( ne );

                        return false;
                    }
                }

                // Récupération de la séance
                if ( _seance == null )
                {
                    return false;
                }
                else
                {
                    _voeuAmendementBean.getFascicule(  ).setSeance( _seance );
                }

                // Récupération du statut
                int nStatut;

                if ( ( request.getParameter( OdsParameters.ID_STATUT ) != null ) &&
                        !request.getParameter( OdsParameters.ID_STATUT ).trim(  )
                                    .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    try
                    {
                        nStatut = Integer.parseInt( request.getParameter( OdsParameters.ID_STATUT ) );

                        Statut statut = StatutHome.findByPrimaryKey( nStatut, getPlugin(  ) );
                        _voeuAmendementBean.setStatut( statut );
                    }
                    catch ( NumberFormatException ne )
                    {
                        AppLogService.error( ne );

                        return false;
                    }
                }

                DateFormat dateFormat = null;
                dateFormat = new SimpleDateFormat( OdsConstants.CONSTANTE_PATTERN_DATE );
                dateFormat.setLenient( false );

                // Récupération de la date du vote
                if ( ( request.getParameter( OdsParameters.DATE_VOTE ) != null ) &&
                        !request.getParameter( OdsParameters.DATE_VOTE ).trim(  )
                                    .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    String strDateVote = request.getParameter( OdsParameters.DATE_VOTE );
                    Timestamp timeStampDateVote;

                    try
                    {
                        timeStampDateVote = new Timestamp( dateFormat.parse( strDateVote ).getTime(  ) );
                        _voeuAmendementBean.setDateVote( timeStampDateVote );
                    }
                    catch ( ParseException pe )
                    {
                        AppLogService.error( pe );

                        return false;
                    }
                }

                // Récupération du numéro du voeu adopté
                if ( ( request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ) != null ) &&
                        !request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE ).trim(  )
                                    .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    String strNumeroVoeu = request.getParameter( OdsParameters.NUMERO_VOEU_ADOPTE );
                    _voeuAmendementBean.setNumeroDeliberation( strNumeroVoeu );
                }

                // Récupération de la date de retour de controle de légalité
                if ( ( request.getParameter( OdsParameters.DATE_RETOUR_CONTROLE_LEGALITE ) != null ) &&
                        !request.getParameter( OdsParameters.DATE_RETOUR_CONTROLE_LEGALITE ).trim(  )
                                    .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    String strDateRetourControleLegalite = request.getParameter( OdsParameters.DATE_RETOUR_CONTROLE_LEGALITE );
                    Timestamp timeStampDateRetourControleLegalite;

                    try
                    {
                        timeStampDateRetourControleLegalite = new Timestamp( dateFormat.parse( 
                                    strDateRetourControleLegalite ).getTime(  ) );
                        _voeuAmendementBean.setDateRetourControleLegalite( timeStampDateRetourControleLegalite );
                    }
                    catch ( ParseException pe )
                    {
                        AppLogService.error( pe );

                        return false;
                    }
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * methode qui ajoute un élu en dans la liste des élus du VA .
     * Retour sur l'interface de création d'un amendement,voeu non rattaché ou
     * voeu rattaché en fonction du type du VA
     * @param request
     * @return
     */
    public String doAddEluCreation( HttpServletRequest request )
    {
        //on sauvegarde en session les valeurs saisies par l'utilisateur
        setVoeuAmendementBean( request );

        if ( null == _voeuAmendementBean.getElus(  ) )
        {
            _voeuAmendementBean.setElus( new ArrayList<Elu>(  ) );
        }

        if ( null != request.getParameter( OdsParameters.ID_ELU ) )
        {
            try
            {
                int nIdElu = Integer.parseInt( request.getParameter( OdsParameters.ID_ELU ) );

                if ( ( -1 != nIdElu ) && !isInTheEluList( nIdElu ) )
                {
                    _voeuAmendementBean.getElus(  ).add( EluHome.findByPrimaryKey( nIdElu, getPlugin(  ) ) );
                    _bHasChanged = true;
                }
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
            }
        }

        //on reconstruit le formulaire de creation
        ReferenceList refListElu = new ReferenceList(  );
        List<Elu> listElu;

        if ( isGestionAval(  ) )
        {
            listElu = EluHome.findEluList( getPlugin(  ) );
        }
        else
        {
            listElu = EluHome.findElusActifs( getPlugin(  ) );
        }

        List<Elu> listEluAAfficher = new ArrayList<Elu>(  );

        //initialisation de la referenceliste refListElu et création du tableau javascript
        //associant un groupe à une liste d'élus   
        init_list_elu( refListElu, listElu, listEluAAfficher );

        ReferenceList refListFascicule = new ReferenceList(  );
        List<Fascicule> listFasciculeAAfficher;

        if ( isGestionAval(  ) )
        {
            listFasciculeAAfficher = FasciculeHome.findAll( getPlugin(  ) );
        }
        else
        {
            listFasciculeAAfficher = FasciculeHome.findFasciculeByIdSeance( _seance.getIdSeance(  ), getPlugin(  ) );
        }

        // initialisation de la referenceliste refListFascicule et création du tableau javascript
        // associant une séance à une liste de fascicules   
        if ( ( _voeuAmendementBean.getFascicule(  ) != null ) &&
                ( _voeuAmendementBean.getFascicule(  ).getSeance(  ) != null ) )
        {
            init_list_fascicule( refListFascicule, listFasciculeAAfficher,
                _voeuAmendementBean.getFascicule(  ).getSeance(  ) );
        }
        else
        {
            init_list_fascicule( refListFascicule, listFasciculeAAfficher, null );
        }

        ReferenceList refListFormationConseil = new ReferenceList(  );
        initRefListFormationConseil( false, refListFormationConseil );

        ReferenceList refListGroupe = new ReferenceList(  );
        initRefListGroupe( refListGroupe );

        ReferenceList refListCommission = new ReferenceList(  );
        initRefListCommission( refListCommission );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( VoeuAmendement.MARK_VOEUAMENDEMENT, _voeuAmendementBean );
        model.put( Seance.MARK_SEANCE, _seance );
        model.put( MARK_LISTE_FASCICULE, refListFascicule );
        model.put( MARK_LISTE_FASCICULE_A_AFFICHER, listFasciculeAAfficher );
        model.put( MARK_LISTE_FORMATION_CONSEIL, refListFormationConseil );
        model.put( MARK_LISTE_GROUPE, refListGroupe );
        model.put( MARK_LISTE_ELU, refListElu );
        model.put( MARK_LISTE_ELU_A_AFFICHER, listEluAAfficher );
        model.put( MARK_LISTE_COMMISSION, refListCommission );
        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );

        if ( isGestionAval(  ) )
        {
            List<Seance> listSeance = SeanceHome.findOldSeance( getPlugin(  ) );

            StatutFilter filter = new StatutFilter(  );

            if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VR ) ||
                    _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VNR ) )
            {
                filter.setPourVoeu( true );
            }

            if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                    _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
            {
                filter.setPourAmendement( true );
            }

            if ( _voeuAmendementBean.getStatut(  ) != null )
            {
                _nIdStatutSelected = _voeuAmendementBean.getStatut(  ).getIdStatut(  );
            }
            else
            {
                _nIdStatutSelected = -1;
            }

            ReferenceList refListStatuts = new ReferenceList(  );
            initRefListStatut( filter, refListStatuts );

            // elements necessaires pour la gestion aval
            model.put( OdsMarks.MARK_GESTION_AVAL, true );
            model.put( MARK_LISTE_SEANCES, listSeance );
            model.put( MARK_LISTE_STATUTS, refListStatuts );
            model.put( MARK_ID_STATUT_SELECTED, _nIdStatutSelected );
        }
        else
        {
            model.put( OdsMarks.MARK_GESTION_AVAL, false );
        }

        //test sur la redirection à effectuer(amendement,voeu,voeu non rataché)
        //on stock en session le type du VA
        HtmlTemplate template;

        if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATION_AMENDEMENT );
            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_AMENDEMENT, getLocale(  ), model );
        }
        else if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VR ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATION_VOEU );
            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_VOEU_RATTACHE, getLocale(  ), model );
        }
        else
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATION_VOEU_NON_RATACHE );
            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_VOEU_NON_RATACHE, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * methode qui supprime  un élu en dans la liste des élus du VA .
     * Retour sur l'interface de création d'un amendement,voeu non rattaché ou
     * voeu rattaché en fonction du type du VA
     * @param request
     * @return
     */
    public String doRemoveEluCreation( HttpServletRequest request )
    {
        //on sauvegarde en session les valeurs saisies par l'utilisateur
        setVoeuAmendementBean( request );

        if ( null == _voeuAmendementBean.getElus(  ) )
        {
            _voeuAmendementBean.setElus( new ArrayList<Elu>(  ) );
        }

        if ( null != request.getParameter( OdsParameters.ID_ELU_TO_REMOVE ) )
        {
            try
            {
                int nIdElu = Integer.parseInt( request.getParameter( OdsParameters.ID_ELU_TO_REMOVE ) );
                removeEluInEluList( nIdElu );
                _bHasChanged = true;
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
            }
        }

        //on reconstruit le formulaire de creation
        ReferenceList refListElu = new ReferenceList(  );
        List<Elu> listElu;

        if ( isGestionAval(  ) )
        {
            listElu = EluHome.findEluList( getPlugin(  ) );
        }
        else
        {
            listElu = EluHome.findElusActifs( getPlugin(  ) );
        }

        List<Elu> listEluAAfficher = new ArrayList<Elu>(  );

        //initialisation de la referenceliste refListElu et création du tableau javascript
        //associant un groupe à une liste d'élus   
        init_list_elu( refListElu, listElu, listEluAAfficher );

        ReferenceList refListFascicule = new ReferenceList(  );
        List<Fascicule> listFasciculeAAfficher;

        if ( isGestionAval(  ) )
        {
            listFasciculeAAfficher = FasciculeHome.findAll( getPlugin(  ) );
        }
        else
        {
            listFasciculeAAfficher = FasciculeHome.findFasciculeByIdSeance( _seance.getIdSeance(  ), getPlugin(  ) );
        }

        // initialisation de la referenceliste refListFascicule et création du tableau javascript
        // associant une séance à une liste de fascicules   
        if ( ( _voeuAmendementBean.getFascicule(  ) != null ) &&
                ( _voeuAmendementBean.getFascicule(  ).getSeance(  ) != null ) )
        {
            init_list_fascicule( refListFascicule, listFasciculeAAfficher,
                _voeuAmendementBean.getFascicule(  ).getSeance(  ) );
        }
        else
        {
            init_list_fascicule( refListFascicule, listFasciculeAAfficher, null );
        }

        ReferenceList refListFormationConseil = new ReferenceList(  );
        initRefListFormationConseil( false, refListFormationConseil );

        ReferenceList refListGroupe = new ReferenceList(  );
        initRefListGroupe( refListGroupe );

        ReferenceList refListCommission = new ReferenceList(  );
        initRefListCommission( refListCommission );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( VoeuAmendement.MARK_VOEUAMENDEMENT, _voeuAmendementBean );
        model.put( Seance.MARK_SEANCE, _seance );
        model.put( MARK_LISTE_FASCICULE, refListFascicule );
        model.put( MARK_LISTE_FASCICULE_A_AFFICHER, listFasciculeAAfficher );
        model.put( MARK_LISTE_FORMATION_CONSEIL, refListFormationConseil );
        model.put( MARK_LISTE_GROUPE, refListGroupe );
        model.put( MARK_LISTE_ELU, refListElu );
        model.put( MARK_LISTE_ELU_A_AFFICHER, listEluAAfficher );
        model.put( MARK_LISTE_COMMISSION, refListCommission );
        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );

        if ( isGestionAval(  ) )
        {
            List<Seance> listSeance = SeanceHome.findOldSeance( getPlugin(  ) );

            StatutFilter filter = new StatutFilter(  );

            if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VR ) ||
                    _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VNR ) )
            {
                filter.setPourVoeu( true );
            }

            if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                    _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
            {
                filter.setPourAmendement( true );
            }

            if ( _voeuAmendementBean.getStatut(  ) != null )
            {
                _nIdStatutSelected = _voeuAmendementBean.getStatut(  ).getIdStatut(  );
            }
            else
            {
                _nIdStatutSelected = -1;
            }

            ReferenceList refListStatuts = new ReferenceList(  );
            initRefListStatut( filter, refListStatuts );

            // elements necessaires pour la gestion aval
            model.put( OdsMarks.MARK_GESTION_AVAL, true );
            model.put( MARK_LISTE_SEANCES, listSeance );
            model.put( MARK_LISTE_STATUTS, refListStatuts );
            model.put( MARK_ID_STATUT_SELECTED, _nIdStatutSelected );
        }
        else
        {
            model.put( OdsMarks.MARK_GESTION_AVAL, false );
        }

        //test sur la redirection à effectuer(amendement,voeu,voeu non rataché)
        //on stock en session le type du VA
        HtmlTemplate template;

        if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATION_AMENDEMENT );
            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_AMENDEMENT, getLocale(  ), model );
        }
        else if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VR ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATION_VOEU );
            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_VOEU_RATTACHE, getLocale(  ), model );
        }
        else
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATION_VOEU_NON_RATACHE );
            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_VOEU_NON_RATACHE, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * methode qui ajoute un élu en dans la liste des élus du VA .
     * Retour sur l'interface de modification d'un amendement,voeu non rattaché ou
     * voeu rattaché en fonction du type du VA
     * @param request
     * @return
     */
    public String doAddEluModification( HttpServletRequest request )
    {
        //on sauvegarde en session les valeurs saisies par l'utilisateur
        setVoeuAmendementBean( request );

        //si le voeuamendement stocké en session a une liste d'élus=null on l'initialise 
        if ( null == _voeuAmendementBean.getElus(  ) )
        {
            _voeuAmendementBean.setElus( new ArrayList<Elu>(  ) );
        }

        if ( null != request.getParameter( OdsParameters.ID_ELU ) )
        {
            try
            {
                int nIdElu = Integer.parseInt( request.getParameter( OdsParameters.ID_ELU ) );

                //si un élu a été sélectionné on l' ajoute a la liste des élus du voeuamendement stocké en session  
                if ( ( -1 != nIdElu ) && !isInTheEluList( nIdElu ) )
                {
                    _voeuAmendementBean.getElus(  ).add( EluHome.findByPrimaryKey( nIdElu, getPlugin(  ) ) );
                    _bHasChanged = true;
                }
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
            }
        }

        ReferenceList refListElu = new ReferenceList(  );
        List<Elu> listElu;

        if ( isGestionAval(  ) )
        {
            listElu = EluHome.findEluList( getPlugin(  ) );
        }
        else
        {
            listElu = EluHome.findElusActifs( getPlugin(  ) );
        }

        List<Elu> listEluAAfficher = new ArrayList<Elu>(  );

        //initialisation de la referenceliste refListElu et création du tableau javascript
        //associant un groupe à une liste d'élus   
        init_list_elu( refListElu, listElu, listEluAAfficher );

        ReferenceList refListFascicule = new ReferenceList(  );
        List<Fascicule> listFasciculeAAfficher;

        if ( isGestionAval(  ) )
        {
            listFasciculeAAfficher = FasciculeHome.findAll( getPlugin(  ) );
        }
        else
        {
            listFasciculeAAfficher = FasciculeHome.findFasciculeByIdSeance( _seance.getIdSeance(  ), getPlugin(  ) );
        }

        // initialisation de la referenceliste refListFascicule et création du tableau javascript
        // associant une séance à une liste de fascicules   
        if ( ( _voeuAmendementBean.getFascicule(  ) != null ) &&
                ( _voeuAmendementBean.getFascicule(  ).getSeance(  ) != null ) )
        {
            init_list_fascicule( refListFascicule, listFasciculeAAfficher,
                _voeuAmendementBean.getFascicule(  ).getSeance(  ) );
        }
        else
        {
            init_list_fascicule( refListFascicule, listFasciculeAAfficher, null );
        }

        ReferenceList refListFormationConseil = new ReferenceList(  );
        initRefListFormationConseil( false, refListFormationConseil );

        ReferenceList refListGroupe = new ReferenceList(  );
        initRefListGroupe( refListGroupe );

        ReferenceList refListCommission = new ReferenceList(  );
        initRefListCommission( refListCommission );

        HtmlTemplate template;
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        /*
         * Permission de modifier
         */
        boolean bPermissionModification = true;

        if ( !RBACService.isAuthorized( VoeuxAmendementsResourceIdService.VOEUX_AMENDEMENTS,
                    RBAC.WILDCARD_RESOURCES_ID, VoeuxAmendementsResourceIdService.PERMISSION_MISE_A_JOUR, getUser(  ) ) )
        {
            bPermissionModification = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_MODIFICATION, bPermissionModification );

        model.put( VoeuAmendement.MARK_VOEUAMENDEMENT, _voeuAmendementBean );
        model.put( Seance.MARK_SEANCE, _seance );
        model.put( MARK_LISTE_FASCICULE, refListFascicule );
        model.put( MARK_LISTE_FASCICULE_A_AFFICHER, listFasciculeAAfficher );
        model.put( MARK_LISTE_FORMATION_CONSEIL, refListFormationConseil );
        model.put( MARK_LISTE_ELU, refListElu );
        model.put( MARK_LISTE_ELU_A_AFFICHER, listEluAAfficher );
        model.put( MARK_LISTE_GROUPE, refListGroupe );
        model.put( MARK_LISTE_COMMISSION, refListCommission );
        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );

        if ( isGestionAval(  ) )
        {
            List<Seance> listSeance = SeanceHome.findOldSeance( getPlugin(  ) );

            StatutFilter filter = new StatutFilter(  );

            if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VR ) ||
                    _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VNR ) )
            {
                filter.setPourVoeu( true );
            }

            if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                    _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
            {
                filter.setPourAmendement( true );
            }

            if ( _voeuAmendementBean.getStatut(  ) != null )
            {
                _nIdStatutSelected = _voeuAmendementBean.getStatut(  ).getIdStatut(  );
            }
            else
            {
                _nIdStatutSelected = -1;
            }

            ReferenceList refListStatuts = new ReferenceList(  );
            initRefListStatut( filter, refListStatuts );

            // elements necessaires pour la gestion aval
            model.put( OdsMarks.MARK_GESTION_AVAL, true );
            model.put( MARK_LISTE_SEANCES, listSeance );
            model.put( MARK_LISTE_STATUTS, refListStatuts );
            model.put( MARK_ID_STATUT_SELECTED, _nIdStatutSelected );
        }
        else
        {
            model.put( OdsMarks.MARK_GESTION_AVAL, false );
        }

        //test sur la redirection à effectuer(amendement,voeu,voeu non rataché)
        if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_AMENDEMENT );
            template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_AMENDEMENT, getLocale(  ), model );
        }
        else if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VR ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_VOEU );
            template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_VOEU_RATTACHE, getLocale(  ), model );
        }
        else
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_VOEU_NON_RATACHE );
            template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_VOEU_NON_RATACHE, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * methode qui supprime  un élu en dans la liste des élus du VA .
     * Retour sur l'interface de modification d'un amendement,voeu non rattaché ou
     * voeu rattaché en fonction du type du VA
     * @param request
     * @return
     */
    public String doRemoveEluModification( HttpServletRequest request )
    {
        //On sauvegarde en session les valeurs saisies par l'utilisateur
        setVoeuAmendementBean( request );

        if ( null == _voeuAmendementBean.getElus(  ) )
        {
            _voeuAmendementBean.setElus( new ArrayList<Elu>(  ) );
        }

        if ( null != request.getParameter( OdsParameters.ID_ELU_TO_REMOVE ) )
        {
            try
            {
                int nIdElu = Integer.parseInt( request.getParameter( OdsParameters.ID_ELU_TO_REMOVE ) );
                removeEluInEluList( nIdElu );
                _bHasChanged = true;
            }

            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
            }
        }

        //on reconstruit le formulaire de modification
        ReferenceList refListElu = new ReferenceList(  );
        List<Elu> listElu;

        if ( isGestionAval(  ) )
        {
            listElu = EluHome.findEluList( getPlugin(  ) );
        }
        else
        {
            listElu = EluHome.findElusActifs( getPlugin(  ) );
        }

        List<Elu> listEluAAfficher = new ArrayList<Elu>(  );

        //initialisation de la referenceliste refListElu et création du tableau javascript
        //associant un groupe à une liste d'élus   
        init_list_elu( refListElu, listElu, listEluAAfficher );

        ReferenceList refListFascicule = new ReferenceList(  );
        List<Fascicule> listFasciculeAAfficher;

        if ( isGestionAval(  ) )
        {
            listFasciculeAAfficher = FasciculeHome.findAll( getPlugin(  ) );
        }
        else
        {
            listFasciculeAAfficher = FasciculeHome.findFasciculeByIdSeance( _seance.getIdSeance(  ), getPlugin(  ) );
        }

        // initialisation de la referenceliste refListFascicule et création du tableau javascript
        // associant une séance à une liste de fascicules   
        if ( ( _voeuAmendementBean.getFascicule(  ) != null ) &&
                ( _voeuAmendementBean.getFascicule(  ).getSeance(  ) != null ) )
        {
            init_list_fascicule( refListFascicule, listFasciculeAAfficher,
                _voeuAmendementBean.getFascicule(  ).getSeance(  ) );
        }
        else
        {
            init_list_fascicule( refListFascicule, listFasciculeAAfficher, null );
        }

        ReferenceList refListFormationConseil = new ReferenceList(  );
        initRefListFormationConseil( false, refListFormationConseil );

        ReferenceList refListGroupe = new ReferenceList(  );
        initRefListGroupe( refListGroupe );

        ReferenceList refListCommission = new ReferenceList(  );
        initRefListCommission( refListCommission );

        HtmlTemplate template;
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        /*
         * Permission de modifier
         */
        boolean bPermissionModification = true;

        if ( !RBACService.isAuthorized( VoeuxAmendementsResourceIdService.VOEUX_AMENDEMENTS,
                    RBAC.WILDCARD_RESOURCES_ID, VoeuxAmendementsResourceIdService.PERMISSION_MISE_A_JOUR, getUser(  ) ) )
        {
            bPermissionModification = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_MODIFICATION, bPermissionModification );

        model.put( VoeuAmendement.MARK_VOEUAMENDEMENT, _voeuAmendementBean );
        model.put( Seance.MARK_SEANCE, _seance );
        model.put( MARK_LISTE_FASCICULE, refListFascicule );
        model.put( MARK_LISTE_FASCICULE_A_AFFICHER, listFasciculeAAfficher );
        model.put( MARK_LISTE_FORMATION_CONSEIL, refListFormationConseil );
        model.put( MARK_LISTE_ELU, refListElu );
        model.put( MARK_LISTE_ELU_A_AFFICHER, listEluAAfficher );
        model.put( MARK_LISTE_GROUPE, refListGroupe );
        model.put( MARK_LISTE_COMMISSION, refListCommission );
        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );

        if ( isGestionAval(  ) )
        {
            List<Seance> listSeance = SeanceHome.findOldSeance( getPlugin(  ) );

            StatutFilter filter = new StatutFilter(  );

            if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VR ) ||
                    _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VNR ) )
            {
                filter.setPourVoeu( true );
            }

            if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                    _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
            {
                filter.setPourAmendement( true );
            }

            if ( _voeuAmendementBean.getStatut(  ) != null )
            {
                _nIdStatutSelected = _voeuAmendementBean.getStatut(  ).getIdStatut(  );
            }
            else
            {
                _nIdStatutSelected = -1;
            }

            ReferenceList refListStatuts = new ReferenceList(  );
            initRefListStatut( filter, refListStatuts );

            // elements necessaires pour la gestion aval
            model.put( OdsMarks.MARK_GESTION_AVAL, true );
            model.put( MARK_LISTE_SEANCES, listSeance );
            model.put( MARK_LISTE_STATUTS, refListStatuts );
            model.put( MARK_ID_STATUT_SELECTED, _nIdStatutSelected );
        }
        else
        {
            model.put( OdsMarks.MARK_GESTION_AVAL, false );
        }

        //test sur la redirection à effectuer(amendement,voeu,voeu non rataché)
        if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_AMENDEMENT );
            template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_AMENDEMENT, getLocale(  ), model );
        }
        else if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VR ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_VOEU );
            template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_VOEU_RATTACHE, getLocale(  ), model );
        }
        else
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_VOEU_NON_RATACHE );
            template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_VOEU_NON_RATACHE, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * methode qui supprime  un Pdd dans la liste des pdds du VA .
     * Retour sur l'interface de modification d'un amendement,ou
     * voeu rattaché en fonction du type du VA
     * @param request
     * @return
     */
    public String doRemovePdd( HttpServletRequest request )
    {
        if ( null == _voeuAmendementBean.getPdds(  ) )
        {
            _voeuAmendementBean.setPdds( new ArrayList<PDD>(  ) );
        }

        if ( null != request.getParameter( OdsParameters.ID_PDD ) )
        {
            try
            {
                //reinitialise le  va en session avec  le va en base.
                _voeuAmendementBean = VoeuAmendementHome.findByPrimaryKey( _voeuAmendementBean.getIdVoeuAmendement(  ),
                        getPlugin(  ) );

                int nIdPdd = Integer.parseInt( request.getParameter( OdsParameters.ID_PDD ) );
                removePddInPddList( nIdPdd );
                VoeuAmendementHome.update( _voeuAmendementBean, getPlugin(  ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
            }
        }

        //reconstruction de l'interface de modification	
        //recupération du fichier lié au voeu
        Fichier fichier = FichierHome.findByPrimaryKey( _voeuAmendementBean.getFichier(  ).getId(  ), getPlugin(  ) );
        _voeuAmendementBean.setFichier( fichier );

        ReferenceList refListElu = new ReferenceList(  );
        List<Elu> listElu = EluHome.findElusActifs( getPlugin(  ) );
        List<Elu> listEluAAfficher = new ArrayList<Elu>(  );

        //initialisation de la referenceliste refListElu et création du tableau javascript
        //associant un groupe à une liste d'élus   
        init_list_elu( refListElu, listElu, listEluAAfficher );

        ReferenceList refListFascicule = new ReferenceList(  );
        List<Fascicule> listFasciculeAAfficher;

        if ( isGestionAval(  ) )
        {
            listFasciculeAAfficher = FasciculeHome.findAll( getPlugin(  ) );
        }
        else
        {
            listFasciculeAAfficher = FasciculeHome.findFasciculeByIdSeance( _seance.getIdSeance(  ), getPlugin(  ) );
        }

        // initialisation de la referenceliste refListFascicule et création du tableau javascript
        // associant une séance à une liste de fascicules   
        if ( ( _voeuAmendementBean.getFascicule(  ) != null ) &&
                ( _voeuAmendementBean.getFascicule(  ).getSeance(  ) != null ) )
        {
            init_list_fascicule( refListFascicule, listFasciculeAAfficher,
                _voeuAmendementBean.getFascicule(  ).getSeance(  ) );
        }
        else
        {
            init_list_fascicule( refListFascicule, listFasciculeAAfficher, null );
        }

        ReferenceList refListFormationConseil = new ReferenceList(  );
        initRefListFormationConseil( false, refListFormationConseil );

        ReferenceList refListGroupe = new ReferenceList(  );
        initRefListGroupe( refListGroupe );

        ReferenceList refListCommission = new ReferenceList(  );
        initRefListCommission( refListCommission );

        HtmlTemplate template;
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        /*
         * Permission de modifier
         */
        boolean bPermissionModification = true;

        if ( !RBACService.isAuthorized( VoeuxAmendementsResourceIdService.VOEUX_AMENDEMENTS,
                    RBAC.WILDCARD_RESOURCES_ID, VoeuxAmendementsResourceIdService.PERMISSION_MISE_A_JOUR, getUser(  ) ) )
        {
            bPermissionModification = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_MODIFICATION, bPermissionModification );

        if ( isGestionAval(  ) )
        {
            List<Seance> listSeance = SeanceHome.findOldSeance( getPlugin(  ) );

            StatutFilter filter = new StatutFilter(  );

            if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VR ) ||
                    _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_VNR ) )
            {
                filter.setPourVoeu( true );

                if ( _voeuAmendementBean.getDeliberation(  ) != null )
                {
                    Fichier deliberation = FichierHome.findByPrimaryKey( _voeuAmendementBean.getDeliberation(  ).getId(  ),
                            getPlugin(  ) );
                    _voeuAmendementBean.setDeliberation( deliberation );
                }
            }

            if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                    _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
            {
                filter.setPourAmendement( true );
            }

            if ( _voeuAmendementBean.getStatut(  ) != null )
            {
                _nIdStatutSelected = _voeuAmendementBean.getStatut(  ).getIdStatut(  );
            }
            else
            {
                _nIdStatutSelected = -1;
            }

            ReferenceList refListStatuts = new ReferenceList(  );
            initRefListStatut( filter, refListStatuts );

            // elements necessaires pour la gestion aval
            model.put( OdsMarks.MARK_GESTION_AVAL, true );
            model.put( MARK_LISTE_SEANCES, listSeance );
            model.put( MARK_ID_SEANCE_SELECTED, _nIdSeanceSelected );
            model.put( MARK_LISTE_STATUTS, refListStatuts );
            model.put( MARK_ID_STATUT_SELECTED, _nIdStatutSelected );
        }
        else
        {
            model.put( OdsMarks.MARK_GESTION_AVAL, false );
        }

        model.put( VoeuAmendement.MARK_VOEUAMENDEMENT, _voeuAmendementBean );
        model.put( Seance.MARK_SEANCE, _seance );
        model.put( MARK_LISTE_FASCICULE, refListFascicule );
        model.put( MARK_LISTE_FASCICULE_A_AFFICHER, listFasciculeAAfficher );
        model.put( MARK_LISTE_FORMATION_CONSEIL, refListFormationConseil );
        model.put( MARK_LISTE_ELU, refListElu );
        model.put( MARK_LISTE_ELU_A_AFFICHER, listEluAAfficher );
        model.put( MARK_LISTE_GROUPE, refListGroupe );
        model.put( MARK_LISTE_COMMISSION, refListCommission );
        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );

        if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_AMENDEMENT );
            template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_AMENDEMENT, getLocale(  ), model );
        }
        else
        {
            setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_VOEU );
            template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_VOEU_RATTACHE, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * methode qui ajoutes  les  Pdds selectionnés par l'utilisateur dans la liste des pdds du VA .
     * Retour sur l'interface de modification d'un amendement,ou
     * voeu rattaché en fonction du type du VA
     * @param request
     * @return
     */
    public String doAddPdds( HttpServletRequest request )
    {
        if ( request.getParameter( CONSTANTE_ANNULER ) != null )
        {
            String strReturn = getModificationUrl( request, _voeuAmendementBean.getType(  ),
                    _voeuAmendementBean.getIdVoeuAmendement(  ) );

            if ( isGestionAval(  ) )
            {
                strReturn += ( "&" + OdsParameters.ID_SEANCE + "=" + _seance.getIdSeance(  ) );
            }

            return strReturn;
        }
        else if ( null != request.getParameterValues( OdsParameters.LISTE_PDD_CHECKED ) )
        {
            //on recupere le va de la base
            _voeuAmendementBean = VoeuAmendementHome.findByPrimaryKey( _voeuAmendementBean.getIdVoeuAmendement(  ),
                    getPlugin(  ) );

            //recupération de la liste des Id pdds à ajouter au va 
            String[] strListePdd = request.getParameterValues( OdsParameters.LISTE_PDD_CHECKED );

            if ( strListePdd.length != 0 )
            {
                for ( String strIdPdd : strListePdd )
                {
                    int nIdPdd;

                    try
                    {
                        nIdPdd = Integer.parseInt( strIdPdd );
                    }
                    catch ( NumberFormatException ne )
                    {
                        return getHomeUrl( request, _voeuAmendementBean.getType(  ) );
                    }

                    if ( !isInThePddList( nIdPdd ) )
                    {
                        PDD pdd = PDDHome.findByPrimaryKey( nIdPdd, getPlugin(  ) );
                        _voeuAmendementBean.getPdds(  ).add( pdd );
                    }
                }

                VoeuAmendementHome.update( _voeuAmendementBean, getPlugin(  ) );

                String strReturn = getModificationUrl( request, _voeuAmendementBean.getType(  ),
                        _voeuAmendementBean.getIdVoeuAmendement(  ) );

                if ( isGestionAval(  ) )
                {
                    strReturn += ( "&" + OdsParameters.ID_SEANCE + "=" + _seance.getIdSeance(  ) );
                }

                return strReturn;
            }
            else
            {
                String strMessage = null;

                if ( ( null != request.getParameter( OdsParameters.TYPE ) ) &&
                        request.getParameter( OdsParameters.TYPE ).equals( PDD.CONSTANTE_TYPE_PROJET ) )
                {
                    strMessage = MESSAGE_PAS_DE_PROJET_CHOISIE;
                }
                else
                {
                    strMessage = MESSAGE_PAS_DE_PROPOSITION_CHOISIE;
                }

                return AdminMessageService.getMessageUrl( request, strMessage, AdminMessage.TYPE_STOP );
            }
        }
        else
        {
            String strMessage = null;

            if ( ( null != request.getParameter( OdsParameters.TYPE ) ) &&
                    request.getParameter( OdsParameters.TYPE ).equals( PDD.CONSTANTE_TYPE_PROJET ) )
            {
                strMessage = MESSAGE_PAS_DE_PROJET_CHOISIE;
            }
            else
            {
                strMessage = MESSAGE_PAS_DE_PROPOSITION_CHOISIE;
            }

            return AdminMessageService.getMessageUrl( request, strMessage, AdminMessage.TYPE_STOP );
        }
    }

    /**
     * methode qui test si l'elu est deja dans la liste des personnes ayant déposé l'amendement traité
     * @param nKey id de l'élu
     * @return
     */
    private Boolean isInTheEluList( int nKey )
    {
        for ( Elu elu : _voeuAmendementBean.getElus(  ) )
        {
            if ( elu.getIdElu(  ) == nKey )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * methode qui test si le pdd  est deja dans la liste des pdds du voeuAmendement
     * @param nKey id du pdd
     * @return
     */
    private Boolean isInThePddList( int nKey )
    {
        for ( PDD pdd : _voeuAmendementBean.getPdds(  ) )
        {
            if ( pdd.getIdPdd(  ) == nKey )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * supprime l'élu d'id nkey de la liste des elus du VA
     * @param nKey id de l'élu à supprimer
     */
    private void removeEluInEluList( int nKey )
    {
        Elu eluToRemove = null;
        List<Elu> elus = _voeuAmendementBean.getElus(  );

        for ( Elu elu : elus )
        {
            if ( elu.getIdElu(  ) == nKey )
            {
                eluToRemove = elu;
            }
        }

        if ( eluToRemove != null )
        {
            elus.remove( eluToRemove );
        }
    }

    /**
     * supprime le pdd d'id nkey de la liste des pdds du VA
     * @param nKey id du pdd à supprimer
     */
    private void removePddInPddList( int nKey )
    {
        PDD pddToRemove = null;
        List<PDD> pdds = _voeuAmendementBean.getPdds(  );

        for ( PDD pdd : pdds )
        {
            if ( pdd.getIdPdd(  ) == nKey )
            {
                pddToRemove = pdd;
            }
        }

        if ( pddToRemove != null )
        {
            pdds.remove( pddToRemove );
        }
    }

    /**
     * methode qui test si  dans le formulaire de saisi ou de modification d'un VA,
     * l'ensemble des critéres commun aux deux formulaires sont présents dans la requette
     * @param request  la requete HTTP
     * @return boolean
     */
    private boolean isValideForme( HttpServletRequest request )
    {
        if ( ( null != request.getParameter( OdsParameters.ID_FASCICULE ) ) &&
                ( null != request.getParameter( OdsParameters.ID_FORMATION_CONSEIL ) ) &&
                ( null != request.getParameter( OdsParameters.OBJET ) ) &&
                ( null != request.getParameter( OdsParameters.REFERENCE ) ) &&
                ( null != request.getParameter( OdsParameters.ID_COMMISSION ) ) &&
                ( null != request.getParameter( OdsParameters.FICHIER_TXT_RECHERCHE ) ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * réinitialise le voeuAmendement en session
     * @param request
     * @return
     */
    public String doResetVoeuAmendement( HttpServletRequest request )
    {
        _voeuAmendementBean = new VoeuAmendement(  );

        return getHomeUrl( request, _voeuAmendementBean.getType(  ) );
    }

    /**
     * méthode qui gère la publication du voeu amendement
     * @param request request
     * @param   strType
     * @return interface d'administration
     */
    public String doPublicationVoeuAmendement( HttpServletRequest request, String strType )
    {
        Timestamp dateForVersion = new Timestamp( System.currentTimeMillis(  ) );
        String strIdVoeuAmendement = request.getParameter( OdsParameters.ID_VOEUAMENDEMENT );

        if ( strIdVoeuAmendement != null )
        {
            int idVoeuAmendement;

            try
            {
                idVoeuAmendement = Integer.parseInt( strIdVoeuAmendement );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getVoeuAmendementList( request, strType );
            }

            VoeuAmendement voeuAmendement = VoeuAmendementHome.findByPrimaryKey( idVoeuAmendement, getPlugin(  ) );

            if ( false == voeuAmendement.getEnLigne(  ) )
            {
                if ( !voeuAmendement.getType(  ).equals( CONSTANTE_TYPE_VNR ) &&
                        ( ( voeuAmendement.getPdds(  ) == null ) || ( voeuAmendement.getPdds(  ).isEmpty(  ) ) ) )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_AVERTISSEMENT_PUBLICATION,
                        AdminMessage.TYPE_STOP );
                }
                else
                {
                    voeuAmendement.setVersion( voeuAmendement.getVersion(  ) + 1 );
                    voeuAmendement.setEnLigne( true );
                    voeuAmendement.setDatePublication( dateForVersion );

                    //gestion de l'historique du voeu
                    Historique historique = new Historique(  );
                    historique.setVersion( voeuAmendement.getVersion(  ) );
                    historique.setDatePublication( dateForVersion );
                    historique.setIdVa( voeuAmendement.getIdVoeuAmendement(  ) );
                    HistoriqueHome.create( historique, getPlugin(  ) );
                }
            }
            else
            {
                /*
                 * GESTION DES PROFILS
                 * -------------------
                 * Si l'utilisateur n'est pas autorisé a effectuer cette fonction il est renvoyé
                 * vers une page lui indiquant qu'il n'a pas les droits nécessaires.
                 */
                if ( !RBACService.isAuthorized( VoeuxAmendementsResourceIdService.VOEUX_AMENDEMENTS,
                            RBAC.WILDCARD_RESOURCES_ID, VoeuxAmendementsResourceIdService.PERMISSION_DEPUBLICATION,
                            getUser(  ) ) )
                {
                    return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                        AdminMessage.TYPE_ERROR );
                }

                voeuAmendement.setEnLigne( false );
            }

            //gestion du fichier associé
            Fichier fichier = FichierHome.findByPrimaryKey( voeuAmendement.getFichier(  ).getId(  ), getPlugin(  ) );
            FichierHome.publication( fichier.getId(  ), dateForVersion, fichier.getVersion(  ),
                voeuAmendement.getEnLigne(  ), getPlugin(  ) );

            // Si c'est une publication on modifie l'historique du fichier
            if ( voeuAmendement.getEnLigne(  ) )
            {
                //Ajout de l'historique
                Historique historique = new Historique(  );
                historique.setVersion( fichier.getVersion(  ) );
                historique.setDatePublication( dateForVersion );
                historique.setIdDocument( fichier.getId(  ) );
                HistoriqueHome.create( historique, getPlugin(  ) );
            }

            //gestion du fichier de délibération associé
            if ( isGestionAval(  ) && ( voeuAmendement.getDeliberation(  ) != null ) )
            {
                Fichier deliberation = FichierHome.findByPrimaryKey( voeuAmendement.getDeliberation(  ).getId(  ),
                        getPlugin(  ) );
                FichierHome.publication( deliberation.getId(  ), dateForVersion, deliberation.getVersion(  ),
                    voeuAmendement.getEnLigne(  ), getPlugin(  ) );
            }

            //mise àjour du voeuamendement en base
            VoeuAmendementHome.update( voeuAmendement, getPlugin(  ) );
        }

        //return getVoeuAmendementList( request, strType );
        return getHomeUrl( request, strType );
    }

    /**
     * retourne l'ecran de visualisation du voeuAmendement choisi
     * @param request
     * @return
     */
    public String getHistoriqueVoeuAmendement( HttpServletRequest request )
    {
        List<Historique> historiques = new ArrayList<Historique>(  );

        String strIdVoeuAmendement = request.getParameter( OdsParameters.ID_VOEUAMENDEMENT );

        try
        {
            int nIdVoeuAmendement = Integer.parseInt( strIdVoeuAmendement );
            VoeuAmendement voeuAmendement = VoeuAmendementHome.findByPrimaryKey( nIdVoeuAmendement, getPlugin(  ) );
            historiques = HistoriqueHome.getHistoriqueList( voeuAmendement, getPlugin(  ) );
        }
        catch ( NumberFormatException ne )
        {
            AppLogService.error( ne );
        }

        HashMap<String, Object> model = new HashMap<String, Object>(  );
        model.put( MARK_LISTE_HISTORIQUES, historiques );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_HISTORIQUE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Télecharge un fichier.
     * Renvoie un flux de bytes du fichier dans l'objet HttpServletResponse response
     *
     * @param request
     * @param response
     * @return
     */
    public void doDownloadFichier( HttpServletRequest request, HttpServletResponse response )
    {
        String strIdFichier = request.getParameter( OdsParameters.ID_FICHIER );

        if ( strIdFichier != null )
        {
            int nIdFichier = -1;

            try
            {
                nIdFichier = Integer.parseInt( strIdFichier );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            Fichier fichierTelecharger = FichierHome.findByPrimaryKey( nIdFichier, getPlugin(  ) );
            FichierPhysique fichierPhysique = FichierPhysiqueHome.findByPrimaryKey( fichierTelecharger.getFichier(  )
                                                                                                      .getIdFichier(  ),
                    getPlugin(  ) );

            try
            {
                String strFileName = fichierTelecharger.getNom(  ) + "." + fichierTelecharger.getExtension(  );

                response.setHeader( "Content-Disposition", "attachment ;filename=\"" + strFileName + "\"" );
                response.setContentType( "application/pdf" );
                response.setHeader( "Pragma", "public" );
                response.setHeader( "Expires", "0" );
                response.setHeader( "Cache-Control", "must-revalidate,post-check=0,pre-check=0" );
                response.setContentLength( (int) fichierPhysique.getDonnees(  ).length );

                OutputStream os = response.getOutputStream(  );
                os.write( fichierPhysique.getDonnees(  ) );
                os.close(  );
            }
            catch ( IOException e )
            {
                AppLogService.error( e );
            }
        }
    }

    /**
     * Modifie le fichier et le fichier physique lié au voeuAmendement
     * @param request
     * @return
     */
    public void doModificationFichier( FileItem fileItem, Timestamp dateForVersion, boolean bEstDeliberation )
    {
        if ( _voeuAmendementBean.getFichier(  ).getId(  ) != -1 )
        {
            Fichier fichier;

            if ( bEstDeliberation )
            {
                fichier = FichierHome.findByPrimaryKey( _voeuAmendementBean.getDeliberation(  ).getId(  ), getPlugin(  ) );
                fichier.setTexte( _voeuAmendementBean.getDeliberation(  ).getTexte(  ) );
            }
            else
            {
                fichier = FichierHome.findByPrimaryKey( _voeuAmendementBean.getFichier(  ).getId(  ), getPlugin(  ) );
                fichier.setTexte( _voeuAmendementBean.getFichier(  ).getTexte(  ) );
            }

            FichierPhysique fichierPhysique = FichierPhysiqueHome.findByPrimaryKey( fichier.getFichier(  ).getIdFichier(  ),
                    getPlugin(  ) );

            // recupération des modifications
            if ( ( fileItem != null ) && ( fileItem.getName(  ) != null ) &&
                    !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileItem.getName(  ).trim(  ) ) )
            {
                // Modification de l'objet représentant le fichier physique
                fichierPhysique.setDonnees( fileItem.get(  ) );
                FichierPhysiqueHome.update( fichierPhysique, getPlugin(  ) );
                fichier.setFichier( fichierPhysique );
                fichier.setNom( getNameCourt( fileItem ) );
                fichier.setExtension( fileItem.getName(  ).substring( fileItem.getName(  ).length(  ) - 3 ).toUpperCase(  ) );
                fichier.setTaille( (int) fileItem.getSize(  ) );

                // Modification de l'objet représentant le Fichier
                if ( !fichier.getEnLigne(  ) )
                {
                    // on incremente le numero de version
                    fichier.setDatePublication( dateForVersion );

                    if ( !bEstDeliberation )
                    {
                        fichier.setVersion( fichier.getVersion(  ) + 1 );

                        //Ajout de l'historique
                        Historique historique = new Historique(  );
                        historique.setVersion( fichier.getVersion(  ) );
                        historique.setDatePublication( dateForVersion );
                        historique.setIdDocument( fichier.getId(  ) );
                        HistoriqueHome.create( historique, getPlugin(  ) );
                    }
                }
            }

            FichierHome.update( fichier, getPlugin(  ) );
        }
    }

    /**
     * Modifie le statut selectionné pour lui attribué le statut demandé
     * @param request la requête
     * @return l'url de retour
     */
    public String doAssignStatut( HttpServletRequest request )
    {
        if ( ( null != request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ) ) &&
                !request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ).trim(  )
                            .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            int nIdVA = -1;
            int nIdStatut = -1;

            try
            {
                nIdVA = Integer.parseInt( request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ).trim(  ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            if ( ( null != request.getParameter( OdsParameters.ID_STATUT ) ) &&
                    !request.getParameter( OdsParameters.ID_STATUT ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                try
                {
                    nIdStatut = Integer.parseInt( request.getParameter( OdsParameters.ID_STATUT ).trim(  ) );
                }
                catch ( NumberFormatException nfe )
                {
                    AppLogService.error( nfe );
                }
            }

            if ( nIdVA > 0 )
            {
                VoeuAmendement va = VoeuAmendementHome.findByPrimaryKey( nIdVA, getPlugin(  ) );

                if ( nIdStatut > 0 )
                {
                    Statut st = StatutHome.findByPrimaryKey( nIdStatut, getPlugin(  ) );
                    va.setStatut( st );
                }
                else
                {
                    va.setStatut( null );
                }

                VoeuAmendementHome.update( va, getPlugin(  ) );

                return ( AppPathService.getBaseUrl( request ) + JSP_URL_GET_STATUT_VA + "?" + OdsParameters.ID_SEANCE +
                "=" + _nIdSeanceSelected );
            }
        }

        return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
    }

    /**
     * Retourne le nom court du fichier contenu dans la methode getName() de l'objet FileItem
     * Par exemple, C:\temp\fichier.txt est le path du fichier.
     * La methode retourne "fichier"
     * @param fileItem Fichier télécharger
     * @return String le nom court du fichier
     */
    private static String getNameCourt( FileItem fileItem )
    {
        String nomCourt = OdsConstants.CONSTANTE_CHAINE_VIDE;

        if ( fileItem != null )
        {
            String nomLong = fileItem.getName(  );

            if ( nomLong != null )
            {
                nomCourt = nomLong.substring( 0, nomLong.indexOf( '.' ) );
            }
        }

        return nomCourt;
    }

    /**
     * Méthode qui crée le  fichier récupéré de la requette
     * @param fileItem le fichier associé au voeu(récupéré de la requette )
     * @return Fichier créé
     */
    private Fichier doCreationFichier( FileItem fileItem, boolean bEstDeliberation, String strNumeroDeliberation )
    {
        Fichier newFichier;

        if ( bEstDeliberation )
        {
            newFichier = _voeuAmendementBean.getDeliberation(  );
        }
        else
        {
            newFichier = _voeuAmendementBean.getFichier(  );
        }

        FichierPhysique newFichierPhysique = new FichierPhysique(  );
        // Creation de l'objet représentant le fichier physique
        newFichierPhysique.setDonnees( fileItem.get(  ) );

        int nIdFichierPhysique = FichierPhysiqueHome.create( newFichierPhysique, getPlugin(  ) );
        newFichierPhysique.setIdFichier( nIdFichierPhysique );

        newFichier.setFichier( newFichierPhysique );
        newFichier.setNom( getNameCourt( fileItem ) );
        newFichier.setExtension( fileItem.getName(  ).substring( fileItem.getName(  ).length(  ) - 3 ).toUpperCase(  ) );
        newFichier.setTaille( (int) fileItem.getSize(  ) );
        // Creation de l'objet représentant le Fichier
        newFichier.setSeance( _seance );

        if ( bEstDeliberation )
        {
            newFichier.setTypdeDocument( TypeDocumentEnum.DELIBERATION.getTypeDocumentOnlyWidthId(  ) );

            newFichier.setTitre( OdsConstants.CONSTANTE_CHAINE_VIDE + Calendar.getInstance(  ).get( Calendar.YEAR ) +
                " " + CONSTANTE_TYPE_VR + strNumeroDeliberation + " " + CONSTANTE_FIN_INTITULE_FICHIER_DELIBERATION );

            if ( _voeuAmendementBean.getEnLigne(  ) )
            {
                newFichier.setEnLigne( true );
                newFichier.setVersion( 1 );

                Timestamp currentTime = new Timestamp( System.currentTimeMillis(  ) );
                newFichier.setDatePublication( currentTime );
            }
            else
            {
                newFichier.setEnLigne( false );
            }
        }
        else
        {
            if ( _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_A ) ||
                    _voeuAmendementBean.getType(  ).equals( CONSTANTE_TYPE_LR ) )
            {
                newFichier.setTypdeDocument( TypeDocumentEnum.AMENDEMENT.getTypeDocumentOnlyWidthId(  ) );
            }
            else
            {
                newFichier.setTypdeDocument( TypeDocumentEnum.VOEU.getTypeDocumentOnlyWidthId(  ) );
            }

            newFichier.setTitre( CONSTANTE_CREATION_FICHIER_INTITULE );

            newFichier.setEnLigne( false );
        }

        newFichier.setId( FichierHome.create( newFichier, getPlugin(  ) ) );

        return newFichier;
    }

    /**
     * renvoie la liasse sous format pdf ou csv.
     * Renvoie un flux de bytes du fichier dans l'objet HttpServletResponse response
     *
     * @param request
     * @param response
     * @return
     */
    public void doDownloadLiasse( HttpServletRequest request, HttpServletResponse response )
    {
        if ( request.getParameter( CONSTANTE_GENERE_CSV ) != null )
        {
            doDownloadLiasseCsv( request, response );
        }
        else if ( request.getParameter( CONSTANTE_GENERE_PDF ) != null )
        {
            doDownloadLiassePdf( request, response );
        }
    }

    /**
     * Retourne la liasse sous format csv
     * Renvoie un flux de bytes du fichier dans l'objet HttpServletResponse response
     *
     * @param request request
     * @param response Retourne la liasse sous format csv
     * @return
     */
    private void doDownloadLiasseCsv( HttpServletRequest request, HttpServletResponse response )
    {
        VoeuAmendementFilter voeuAmendementFilter = new VoeuAmendementFilter(  );

        if ( _nIdFormationConseilSelected != -1 )
        {
            _nIdFormationConseilSelected = getIdFormationConseil( request, voeuAmendementFilter );
        }
        else
        {
            _nIdFormationConseilSelected = 0;
            voeuAmendementFilter.setIdFormationConseil( _nIdFormationConseilSelected );
        }

        if ( _seance == null )
        {
            _nIdSeanceSelected = getIdSeance( request, voeuAmendementFilter );
        }
        else
        {
            resetPageIndex(  );
            _nIdSeanceSelected = _seance.getIdSeance(  );
        }

        voeuAmendementFilter.setIdSeance( _nIdSeanceSelected );

        //order by
        voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_NUMERO_ORDRE );
        voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_REFERENCE_VA );

        List<VoeuAmendement> voeuAmendements;

        if ( isGestionAval(  ) )
        {
            voeuAmendements = VoeuAmendementHome.findVoeuAmendementListByFilter( voeuAmendementFilter, getPlugin(  ) );
        }
        else
        {
            voeuAmendements = VoeuAmendementHome.findLiasseByFilter( voeuAmendementFilter, getPlugin(  ) );

            List<VoeuAmendement> voeuAmendementsToRemove = new ArrayList<VoeuAmendement>(  );

            for ( VoeuAmendement va : voeuAmendements )
            {
                if ( ( va.getReference(  ) == null ) ||
                        va.getReference(  ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    voeuAmendementsToRemove.add( va );
                }
            }

            voeuAmendements.removeAll( voeuAmendementsToRemove );
        }

        StringBuffer strDonnees = new StringBuffer(  );

        Collections.sort(voeuAmendements, new VoeuAmendementLiasseComparator());
                
        for ( VoeuAmendement voeuAmendement : voeuAmendements )
        {
            //construction de la reference du VA
            strDonnees.append( CONSTANTE_GUILLEMET );

            if ( voeuAmendement.getType(  ).equals( CONSTANTE_TYPE_VNR ) ||
                    voeuAmendement.getType(  ).equals( CONSTANTE_TYPE_VR ) )
            {
                strDonnees.append( CONSTANTE_TYPE_VR );
            }
            else
            {
                strDonnees.append( voeuAmendement.getType(  ) );
            }

            if ( voeuAmendement.getReference(  ) != null )
            {
                strDonnees.append( voeuAmendement.getReference(  ) );
            }

            strDonnees.append( CONSTANTE_ESPACE );
            strDonnees.append( voeuAmendement.getFascicule(  ).getCodeFascicule(  ) );
            strDonnees.append( CONSTANTE_GUILLEMET );
            strDonnees.append( CONSTANTE_POINT_VIRGULE );
            strDonnees.append( CONSTANTE_GUILLEMET );

            // Déposé Par
            if ( voeuAmendement.getDeposeExecutif(  ) )
            {
                strDonnees.append( I18nService.getLocalizedString( PROPERTY_LABEL_EXECUTIF, getLocale(  ) ) );
            }
            else
            {
                if ( voeuAmendement.getElus(  ) != null )
                {
                    for ( Elu elu : voeuAmendement.getElus(  ) )
                    {
                        strDonnees.append( elu.getGroupe(  ).getNomGroupe(  ) );
                    }
                }
            }

            strDonnees.append( CONSTANTE_GUILLEMET );

            strDonnees.append( CONSTANTE_POINT_VIRGULE );

            // PDD
            strDonnees.append( CONSTANTE_GUILLEMET );

            if ( voeuAmendement.getPdds(  ) != null )
            {
                StringBuffer strPdds = new StringBuffer(  );
                int ncp = 0;

                for ( PDD pdd : voeuAmendement.getPdds(  ) )
                {
                    ncp++;
                    strPdds.append( pdd.getReference(  ) );

                    if ( ncp != voeuAmendement.getPdds(  ).size(  ) )
                    {
                        strPdds.append( CONSTANTE_VIRGULE );
                        strPdds.append( CONSTANTE_ESPACE );
                    }
                }

                strDonnees.append( strPdds.toString(  ) );
            }

            strDonnees.append( CONSTANTE_GUILLEMET );

            // Statut
            if ( isGestionAval(  ) && ( voeuAmendement.getStatut(  ) != null ) )
            {
                strDonnees.append( CONSTANTE_POINT_VIRGULE );
                strDonnees.append( CONSTANTE_GUILLEMET );

                int nIdStatut = voeuAmendement.getStatut(  ).getIdStatut(  );
                Statut statut = StatutHome.findByPrimaryKey( nIdStatut, getPlugin(  ) );
                strDonnees.append( statut.getLibelle(  ) );

                strDonnees.append( CONSTANTE_GUILLEMET );
            }

            //fin de ligne
            strDonnees.append( CONSTANTE_ANTI_SLASH );
        }

        response.setContentType( "enctype=multipart/form-data" );

        if ( isGestionAval(  ) )
        {
            response.setHeader( "Content-Disposition", "attachment; filename=\"StatutsVA.csv \";" );
        }
        else
        {
            response.setHeader( "Content-Disposition", "attachment; filename=\"liasse.csv \";" );
        }

        try
        {
            OutputStream os = response.getOutputStream(  );
            os.write( strDonnees.toString(  ).getBytes( "ISO-8859-1" ) );
            os.close(  );
        }
        catch ( IOException e )
        {
            AppLogService.error( e );
        }
    }

    /**
     * Retourne la liasse sous format PDF
     * @param request la requete Http
     * @param response la reponse
     * @return la liasse sous format PDF
     */
    private void doDownloadLiassePdf( HttpServletRequest request, HttpServletResponse response )
    {
        VoeuAmendementFilter voeuAmendementFilter = new VoeuAmendementFilter(  );

        if ( _nIdFormationConseilSelected != -1 )
        {
            _nIdFormationConseilSelected = getIdFormationConseil( request, voeuAmendementFilter );
        }
        else
        {
            _nIdFormationConseilSelected = 0;
            voeuAmendementFilter.setIdFormationConseil( _nIdFormationConseilSelected );
        }

        if ( _seance == null )
        {
            _nIdSeanceSelected = getIdSeance( request, voeuAmendementFilter );
        }
        else
        {
            resetPageIndex(  );
            _nIdSeanceSelected = _seance.getIdSeance(  );
        }

        OrdreDuJour ordreDuJourDeFinitif = new OrdreDuJour(  );
        FormationConseil formationConseil = FormationConseilHome.findByPrimaryKey( _nIdFormationConseilSelected,
                getPlugin(  ) );
        ordreDuJourDeFinitif.setSeance( _seance );
        ordreDuJourDeFinitif.setFormationConseil( formationConseil );

        String strFileName = "liasse.pdf";
        response.setHeader( "Content-Disposition", "attachment ;filename=\"" + strFileName + "\"" );
        response.setContentType( "application/pdf" );
        response.setHeader( "Pragma", "public" );
        response.setHeader( "Expires", "0" );
        response.setHeader( "Cache-Control", "must-revalidate,post-check=0,pre-check=0" );

        try
        {
            OutputStream os = response.getOutputStream(  );
            OrdreDuJourUtils.visualiseLiasse( ordreDuJourDeFinitif, getPlugin(  ), getLocale(  ), os );
            os.close(  );
        }
        catch ( Exception e )
        {
            e.printStackTrace(  );
        }
    }

    /**
     * Supprime le parent du voeu/amendement.
     * @param request la requete
     * @param strAction marqueur de l'action à effecuer (ajout ou suppression)
     * @param strReturn marqueur sur la page de retour (création ou modification)
     * @return la page de création ou de modification
     */
    public String doAddRemoveParent( HttpServletRequest request, String strAction, String strReturn )
    {
        if ( ( ( null == request.getParameter( OdsParameters.A_FAIRE ) ) ||
                !request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_ACTION_CANCEL ) ) &&
                strAction.equals( CONSTANTE_ACTION_ADD_PARENT ) )
        {
            if ( request.getParameter( OdsParameters.ID_PARENT ) == null )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_NO_PARENT_SELECTED, AdminMessage.TYPE_STOP );
            }

            int nIdParent = -1;

            try
            {
                nIdParent = Integer.parseInt( request.getParameter( OdsParameters.ID_PARENT ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
            }

            if ( ( nIdParent != -1 ) &&
                    ( ( _voeuAmendementBean.getParent(  ) == null ) ||
                    ( _voeuAmendementBean.getParent(  ).getIdVoeuAmendement(  ) != nIdParent ) ) )
            {
                VoeuAmendement parent = VoeuAmendementHome.findByPrimaryKey( nIdParent, getPlugin(  ) );
                _voeuAmendementBean.setParent( parent );

                // initialisation des informations du voeu ou amendement sélectionné
                _voeuAmendementBean.setFormationConseil( parent.getFormationConseil(  ) );
                _voeuAmendementBean.setFascicule( parent.getFascicule(  ) );
                _voeuAmendementBean.setDeposeExecutif( parent.getDeposeExecutif(  ) );
                _voeuAmendementBean.setElus( parent.getElus(  ) );
                _voeuAmendementBean.setPdds( parent.getPdds(  ) );

                if ( ( parent.getReference(  ) != null ) && parent.getReference(  ).endsWith( "bis" ) )
                {
                    _voeuAmendementBean.setReference( parent.getReference(  )
                                                            .substring( 0, parent.getReference(  ).length(  ) - 3 ) +
                        "ter" );
                }
                else
                {
                    _voeuAmendementBean.setReference( parent.getReference(  ) + "bis" );
                }

                _bHasChanged = true;
            }
        }
        else if ( strAction.equals( CONSTANTE_ACTION_REMOVE_PARENT ) )
        {
            _voeuAmendementBean.setParent( null );
        }

        if ( strReturn.equals( CONSTANTE_RETOUR_CREATION ) )
        {
            return getCreationUrl( request, _voeuAmendementBean.getType(  ) );
        }

        if ( strReturn.equals( CONSTANTE_RETOUR_MODIFICATION ) )
        {
            return getModificationUrl( request, _voeuAmendementBean.getType(  ),
                _voeuAmendementBean.getIdVoeuAmendement(  ) );
        }

        return getHomeUrl( request, _voeuAmendementBean.getType(  ) );
    }

    /**
     * teste si les champs obligatoires(formationconseil,fascicule,objet)  ont bien été saisies
     * @param champsNonSaisie  liste des champs non saisies
     * @return true si les champs obligatoires(formationconseil,fascicule,objet)  ont bien été saisies
     */
    private boolean isFieldRequired( HttpServletRequest request, List<String> champsNonSaisie )
    {
        boolean bIsFieldRequired = true;

        if ( ( request.getParameter( OdsParameters.ID_FORMATION_CONSEIL ) == null ) ||
                request.getParameter( OdsParameters.ID_FORMATION_CONSEIL ).equals( OdsConstants.FILTER_ALL ) ||
                request.getParameter( OdsParameters.ID_FORMATION_CONSEIL ).trim(  )
                           .equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            champsNonSaisie.add( PROPERTY_LABEL_FORMATION_CONSEIL );
            bIsFieldRequired = false;
        }
        else if ( ( request.getParameter( OdsParameters.ID_FASCICULE ) == null ) ||
                request.getParameter( OdsParameters.ID_FASCICULE ).equals( OdsConstants.FILTER_ALL ) ||
                request.getParameter( OdsParameters.ID_FASCICULE ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            champsNonSaisie.add( PROPERTY_LABEL_FASCICULE );
            bIsFieldRequired = false;
        }
        else if ( ( request.getParameter( OdsParameters.OBJET ) == null ) ||
                request.getParameter( OdsParameters.OBJET ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            champsNonSaisie.add( PROPERTY_LABEL_OBJET );
            bIsFieldRequired = false;
        }

        return bIsFieldRequired;
    }

    /**
     * test si le fichier fourni est valide
     * @param fileItem le fichier a tester
     * @param fileProblems Liste des problemes liés au fichier
     * @return true si le fichier est valide  ont
     */
    private boolean isValideFile( FileItem fileItem, List<String> fileProblems )
    {
        boolean nIsFileGood = true;

        if ( ( null != fileItem ) && ( null != fileItem.getName(  ) ) )
        {
            if ( OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileItem.getName(  ).trim(  ) ) )
            {
                fileProblems.add( PROPERTY_LABEL_FICHIER );
                nIsFileGood = false;
            }
            else
            {
                String extensionFile = fileItem.getName(  ).substring( fileItem.getName(  ).length(  ) - 3 )
                                               .toUpperCase(  );

                if ( !OdsConstants.CONSTANTE_PDF.equals( extensionFile ) )
                {
                    fileProblems.add( MESSAGE_LABEL_FICHIER_NO_PDF );
                    nIsFileGood = false;
                }
            }
        }
        else
        {
            nIsFileGood = false;
            fileProblems.add( PROPERTY_LABEL_FICHIER );
        }

        return nIsFileGood;
    }

    /**
     * Test pour savoir si la référence  modifié n'existe pas  dans le fascicule.
     * @return
     */
    private boolean isReferenceModifyAlreadyInFascicule(  )
    {
        //recupération de la référence du va présent en base
        String strVaReferenceEnBase = VoeuAmendementHome.findByPrimaryKey( _voeuAmendementBean.getIdVoeuAmendement(  ),
                getPlugin(  ) ).getReference(  );

        if ( ( strVaReferenceEnBase != null ) && ( _voeuAmendementBean.getReference(  ) != null ) &&
                !strVaReferenceEnBase.equals( _voeuAmendementBean.getReference(  ) ) &&
                VoeuAmendementHome.isAlreadyExistInFascicule( _voeuAmendementBean.getFascicule(  ).getIdFascicule(  ),
                    _voeuAmendementBean.getReference(  ), getPlugin(  ) ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @return _bIsGestionAval boolean
     */
    public boolean isGestionAval(  )
    {
        return _bIsGestionAval;
    }

    /**
     * @return _b boolean
     */
    public boolean isUrLRetour(  )
    {
        return _bUrlRetour;
    }

    /**
     * affecte la variable bUrlRetour retour
     * @param bUrlRetour =true retour a l'interface de gestion de jsp _strUrlRetour
     */
    public void setBurlRetour( boolean bUrlRetour )
    {
        _bUrlRetour = bUrlRetour;
    }

    /**
     * affecte  l'url de retour
     * @param strUrl url de retour
     */
    public void setUrlRetour( String strUrl )
    {
        _strUrlRetour = strUrl;
    }

    /**
     * retourne l'url de retour
     * @return l'url de retour
     */
    public String getUrlRetour(  )
    {
        return _strUrlRetour;
    }

    /**
     * retourne l'URL du home
     * @param request la requete
     * @param typeVa le type de VA
     * @return l'url du Home
     */
    private String getHomeUrl( HttpServletRequest request, String typeVa )
    {
        String strJspReturn = AppPathService.getBaseUrl( request );

        if ( typeVa != null )
        {
            if ( isGestionAval(  ) )
            {
                if ( typeVa.equals( CONSTANTE_TYPE_A ) || typeVa.equals( CONSTANTE_TYPE_LR ) )
                {
                    strJspReturn += JSP_URL_GET_AMENDEMENTS_LIST_GESTION_AVAL;
                }
                else if ( typeVa.equals( CONSTANTE_TYPE_VR ) )
                {
                    strJspReturn += JSP_URL_GET_VOEUX_RATTACHES_LIST_GESTION_AVAL;
                }
                else if ( typeVa.equals( CONSTANTE_TYPE_VNR ) )
                {
                    strJspReturn += JSP_URL_GET_VOEUX_NON_RATTACHES_LIST_GESTION_AVAL;
                }
                else if ( typeVa.equals( CONSTANTE_TYPE_STATUT_VA ) )
                {
                    strJspReturn += JSP_URL_GET_STATUT_VA;
                }
                else
                {
                    strJspReturn = getHomeUrl( request );
                }
            }
            else
            {
                if ( typeVa.equals( CONSTANTE_TYPE_A ) || typeVa.equals( CONSTANTE_TYPE_LR ) )
                {
                    strJspReturn += JSP_URL_GET_AMENDEMENTS_LIST;
                }
                else if ( typeVa.equals( CONSTANTE_TYPE_VR ) )
                {
                    strJspReturn += JSP_URL_GET_VOEUX_RATTACHES_LIST;
                }
                else if ( typeVa.equals( CONSTANTE_TYPE_VNR ) )
                {
                    strJspReturn += JSP_URL_GET_VOEUX_NON_RATTACHES_LIST;
                }
                else
                {
                    strJspReturn = getHomeUrl( request );
                }
            }
        }
        else
        {
            strJspReturn = getHomeUrl( request );
        }

        return strJspReturn;
    }

    /**
     * retourne l'URL de la page de modification d'un VA
     * @param request la requete
     * @param typeVa le type de VA
     * @return l'URL de la page de modification d'un VA
     */
    private String getModificationUrl( HttpServletRequest request, String typeVa, int nIdVA )
    {
        String strJspReturn = AppPathService.getBaseUrl( request );

        if ( typeVa != null )
        {
            if ( typeVa.equals( CONSTANTE_TYPE_A ) || typeVa.equals( CONSTANTE_TYPE_LR ) )
            {
                strJspReturn += JSP_URL_GET_MODIFICATION_AMENDEMENT;
            }
            else if ( typeVa.equals( CONSTANTE_TYPE_VR ) )
            {
                strJspReturn += JSP_URL_GET_MODIFICATION_VOEU_RATTACHE;
            }
            else if ( typeVa.equals( CONSTANTE_TYPE_VNR ) )
            {
                strJspReturn += JSP_URL_GET_MODIFICATION_VOEU_NON_RATTACHE;
            }
            else if ( typeVa.equals( CONSTANTE_TYPE_STATUT_VA ) )
            {
                strJspReturn += JSP_URL_GET_MODIFICATION_STATUT_VA;
            }

            return strJspReturn + "?id_voeuamendement=" + nIdVA;
        }

        return getHomeUrl( request );
    }

    /**
     * retourne l'URL de la page de modification d'un VA
     * @param request la requete
     * @param typeVa le type de VA
     * @return l'URL de la page de modification d'un VA
     */
    private String getCreationUrl( HttpServletRequest request, String typeVa )
    {
        String strJspReturn = AppPathService.getBaseUrl( request );

        if ( typeVa != null )
        {
            if ( typeVa.equals( CONSTANTE_TYPE_A ) || typeVa.equals( CONSTANTE_TYPE_LR ) )
            {
                strJspReturn += JSP_URL_GET_CREATION_AMENDEMENT;
            }
            else if ( typeVa.equals( CONSTANTE_TYPE_VR ) )
            {
                strJspReturn += JSP_URL_GET_CREATION_VOEU_RATTACHE;
            }
            else if ( typeVa.equals( CONSTANTE_TYPE_VNR ) )
            {
                strJspReturn += JSP_URL_GET_CREATION_VOEU_NON_RATTACHE;
            }

            return strJspReturn;
        }

        return getHomeUrl( request );
    }

    /**
     * permet de savoir si l'url donné en paramètre correspond à l'url pour la gestiond es parents d'un VA
     * @param strUrl l'url à tester
     * @return VRAI si il s'agit de l'url de gestion des parents, FAUX sinon
     */
    private boolean estPourSelectionParents( String strUrl )
    {
        Boolean bRetour = false;

        if ( strUrl.equals( CONSTANTE_URL_JSP_APPELANT_AMENDEMENT_PARENT_CREATION ) ||
                strUrl.equals( CONSTANTE_URL_JSP_APPELANT_AMENDEMENT_PARENT_MODIFICATION ) ||
                strUrl.equals( CONSTANTE_URL_JSP_APPELANT_VOEU_RATTACHE_PARENT_CREATION ) ||
                strUrl.equals( CONSTANTE_URL_JSP_APPELANT_VOEU_RATTACHE_PARENT_MODIFICATION ) ||
                strUrl.equals( CONSTANTE_URL_JSP_APPELANT_VOEU_NON_RATTACHE_PARENT_CREATION ) ||
                strUrl.equals( CONSTANTE_URL_JSP_APPELANT_VOEU_NON_RATTACHE_PARENT_MODIFICATION ) )
        {
            bRetour = true;
        }

        return bRetour;
    }

    /**
     * Si il existe un relevé des travaux pour la commission sélectionnée,
     * et que le VA mis à jour n'etait pas rattaché à ce relevé,
     * alors on rattache le VA au relevé des travaux.
     *
     * @param va le voeu ou amendement à rattacher à un relevé de travaux
     */
    public void ajouteVAaReleveDesTravaux( VoeuAmendement va )
    {
        if ( ( va != null ) && ( va.getCommission(  ) != null ) )
        {
            ReleveDesTravauxFilter releveFilter = new ReleveDesTravauxFilter(  );
            releveFilter.setIdCommission( va.getCommission(  ).getIdCommission(  ) );

            if ( va.getFascicule(  ) != null )
            {
                Fascicule fascicule = FasciculeHome.findByPrimaryKey( va.getFascicule(  ).getIdFascicule(  ),
                        getPlugin(  ) );

                if ( fascicule.getSeance(  ) != null )
                {
                    releveFilter.setIdSeance( fascicule.getSeance(  ).getIdSeance(  ) );
                }
            }

            List<ReleveDesTravaux> listReleves = ReleveDesTravauxHome.findReleveDesTravauxByFilter( releveFilter,
                    getPlugin(  ) );

            ElementReleveDesTravaux elmtReleve = new ElementReleveDesTravaux(  );
            elmtReleve.setVoeuAmendement( va );

            for ( ReleveDesTravaux rdt : listReleves )
            {
                if ( rdt.getElementReleveDesTravaux(  ) == null )
                {
                    rdt.setElementReleveDesTravaux( new ArrayList<ElementReleveDesTravaux>(  ) );
                }

                rdt.getElementReleveDesTravaux(  ).add( elmtReleve );
                ReleveDesTravauxHome.update( rdt, getPlugin(  ) );
                elmtReleve.setReleve( rdt );
                ElementReleveDesTravauxHome.create( elmtReleve, getPlugin(  ) );

                break;
            }
        }
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

        if ( !RBACService.isAuthorized( VoeuxAmendementsResourceIdService.VOEUX_AMENDEMENTS,
                    RBAC.WILDCARD_RESOURCES_ID, VoeuxAmendementsResourceIdService.PERMISSION_DEPUBLICATION, getUser(  ) ) )
        {
            bPermissionDepublication = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_DEPUBLICATION, bPermissionDepublication );

        /*
         * Permission de modifier
         */
        boolean bPermissionModification = true;

        if ( !RBACService.isAuthorized( VoeuxAmendementsResourceIdService.VOEUX_AMENDEMENTS,
                    RBAC.WILDCARD_RESOURCES_ID, VoeuxAmendementsResourceIdService.PERMISSION_MISE_A_JOUR, getUser(  ) ) )
        {
            bPermissionModification = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_MODIFICATION, bPermissionModification );

        /*
         * Permission de supprimer
         */
        boolean bPermissionSuppression = true;

        if ( !RBACService.isAuthorized( VoeuxAmendementsResourceIdService.VOEUX_AMENDEMENTS,
                    RBAC.WILDCARD_RESOURCES_ID, VoeuxAmendementsResourceIdService.PERMISSION_SUPPRESSION, getUser(  ) ) )
        {
            bPermissionSuppression = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_SUPPRESSION, bPermissionSuppression );
    }
}
