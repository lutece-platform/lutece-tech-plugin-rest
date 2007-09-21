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
package fr.paris.lutece.plugins.ods.web.pdd;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import fr.paris.lutece.plugins.ods.business.categoriedeliberation.CategorieDeliberation;
import fr.paris.lutece.plugins.ods.business.categoriedeliberation.CategorieDeliberationHome;
import fr.paris.lutece.plugins.ods.business.direction.Direction;
import fr.paris.lutece.plugins.ods.business.direction.DirectionHome;
import fr.paris.lutece.plugins.ods.business.fascicule.Fascicule;
import fr.paris.lutece.plugins.ods.business.fascicule.FasciculeHome;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierFilter;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysique;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysiqueHome;
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocument;
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocumentEnum;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilEnum;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilHome;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitique;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitiqueHome;
import fr.paris.lutece.plugins.ods.business.historique.Historique;
import fr.paris.lutece.plugins.ods.business.historique.HistoriqueHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.EntreeOrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.EntreeOrdreDuJourFilter;
import fr.paris.lutece.plugins.ods.business.ordredujour.EntreeOrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourFilter;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.TypeOrdreDuJourEnum;
import fr.paris.lutece.plugins.ods.business.pdd.Arrondissement;
import fr.paris.lutece.plugins.ods.business.pdd.DirectionCoEmetrice;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.pdd.PDDFilter;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ElementReleveDesTravaux;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ElementReleveDesTravauxHome;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravaux;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravauxHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.statut.Statut;
import fr.paris.lutece.plugins.ods.business.statut.StatutFilter;
import fr.paris.lutece.plugins.ods.business.statut.StatutHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementHome;
import fr.paris.lutece.plugins.ods.service.horodatage.HorodatageActionEnum;
import fr.paris.lutece.plugins.ods.service.horodatage.HorodatageServices;
import fr.paris.lutece.plugins.ods.service.role.PddGestionAvalResourceIdService;
import fr.paris.lutece.plugins.ods.service.role.ProjetProchaineSeanceResourceIdService;
import fr.paris.lutece.plugins.ods.service.role.PropositionProchaineSeanceResourceIdService;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.constants.OdsProperties;
import fr.paris.lutece.plugins.ods.web.ordredujour.OrdreDuJourJspBean;
import fr.paris.lutece.plugins.ods.web.relevetravaux.ReleveDesTravauxJspBean;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;


/**
 *
 * ProjetDeDeliberationJspBean
 */
public class ProjetDeDeliberationJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_PROJET_DE_DELIB = "ODS_PROJET_DE_DELIB";
    public static final String RIGHT_ODS_PROP_DE_DELIB = "ODS_PROP_DE_DELIB";
    public static final String RIGHT_ODS_PROJET_DE_DELIB_GESTION_AVAL = "ODS_PROJET_DE_DELIB_GESTION_AVAL";
    public static final String RIGHT_ODS_PROP_DE_DELIB_GESTION_AVAL = "ODS_PROP_DE_DELIB_GESTION_AVAL";
    public static final String MARK_FONCTIONNALITE_IS_PROJET = "fonctionnalite_is_projet";
    public static final String CONSTANTE_URL_JSP_APPELANT_AMENDEMENT = "jsp/admin/plugins/ods/voeuamendement/AmendementsForPDD.jsp";
    public static final String CONSTANTE_URL_JSP_APPELANT_VOEUX = "jsp/admin/plugins/ods/voeuamendement/VoeuxRattachesForPDD.jsp";
    public static final String CONSTANTE_URL_JSP_RETOUR = "jsp/admin/plugins/ods/projetdeliberation/DoSelectionAmendement.jsp";
    public static final String CONSTANTE_URL_JSP_APPELANT_AMENDEMENT_GESTION_AVAL = "jsp/admin/plugins/ods/voeuamendement/AmendementsForPDDGestionAval.jsp";
    public static final String CONSTANTE_URL_JSP_APPELANT_VOEUX_GESTION_AVAL = "jsp/admin/plugins/ods/voeuamendement/VoeuxRattachesForPDDGestionAval.jsp";
    public static final String CONSTANTE_URL_JSP_RETOUR_GESTION_AVAL = "jsp/admin/plugins/ods/projetdeliberation/DoSelectionAmendement.jsp";
    private static final String TEMPLATE_LISTE_PDD = "admin/plugins/ods/projetdeliberation/deliberations.html";
    private static final String TEMPLATE_CREATION_PDD = "admin/plugins/ods/projetdeliberation/creation_pdd.html";
    private static final String TEMPLATE_MODIFICATION_PDD_GESTION_AVAL = "admin/plugins/ods/projetdeliberation/modification_pdd_aval.html";
    private static final String TEMPLATE_MODIFICATION_COMPLETE_PDD = "admin/plugins/ods/projetdeliberation/modification_complete_pdd.html";
    private static final String TEMPLATE_JSP_MODIFICATION_COMPLETE = "jsp/admin/plugins/ods/projetdeliberation/ModificationCompletePDD.jsp";
    private static final String TEMPLATE_JSP_LISTE = "jsp/admin/plugins/ods/projetdeliberation/pddListe.jsp";
    private static final String TEMPLATE_HISTORIQUE = "admin/plugins/ods/historique/historique.html";
    private static final String TEMPLATE_LISTE_PDD_SELECTION_MULTI = "admin/plugins/ods/projetdeliberation/deliberations_selection_multi.html";
    private static final String TEMPLATE_LISTE_PDD_SELECTION = "admin/plugins/ods/projetdeliberation/deliberations_selection.html";
    private static final String TEMPLATE_CONFIRM_DEPUBLICATION_PDD = "admin/plugins/ods/projetdeliberation/confirmation_depublication_pdd_odj.html";
    private static final String PROPERTY_URL_DO_SUPPRESSION_PDD_JSP = "jsp/admin/plugins/ods/projetdeliberation/DoSuppressionPDD.jsp";
    private static final String PROPERTY_URL_DO_SUPPRESSION_PIECE_ANNEXE_JSP = "jsp/admin/plugins/ods/projetdeliberation/DoSuppressionPieceAnnexe.jsp";
    private static final String PROPERTY_PAGE_HISTORIQUE = "ods.historique.pageTitle";
    private static final String PROPERTY_FICHIERS_PER_PAGE = "ods.itemPerPage.label";
    private static final String PROPERTY_PAGE_MODIFICATION_PJ = "ods.projetDeDeliberation.modification.page.title";
    private static final String PROPERTY_PAGE_CREATION_PJ = "ods.projetDeDeliberation.creation.page.title";
    private static final String PROPERTY_PAGE_MODIFICATION_PP = "ods.propositionDeDeliberation.modification.page.title";
    private static final String PROPERTY_PAGE_CREATION_PP = "ods.propositionDeDeliberation.creation.page.title";
    private static final String PROPERTY_ANNEE_PAS_CORRECT_TAILLE = "ods.message.errorAnneeLenght";
    private static final String PROPERTY_ANNEE_PAS_NUMERIQUE = "ods.message.errorAnneeNotNumerique";
    private static final String PROPERTY_NUMERO_PAS_NUMERIQUE = "ods.message.errorNumeroNotNumerique";
    private static final String PROPERTY_PDD_ALREADY_EXIST = "ods.pdd.reference.alreadyExist";
    private static final String PROPERTY_STATUT_NON_RENSEIGNE = "ods.pdd.statut.nonRenseigne";
    private static final String PROPERTY_LABEL_FORMATION_CONSEIL = "ods.pdd.label.formationConseil";
    private static final String PROPERTY_LABEL_ANNEE = "ods.pdd.label.annee";
    private static final String PROPERTY_LABEL_DIRECTION = "ods.pdd.label.direction";
    private static final String PROPERTY_LABEL_GROUPE = "ods.pdd.label.groupe";
    private static final String PROPERTY_LABEL_NUMERO = "ods.pdd.label.numero";
    private static final String PROPERTY_LABEL_OBJET = "ods.pdd.label.objet";
    private static final String PROPERTY_LABEL_MODE_INTRO = "ods.pdd.label.modeIntroduction";
    private static final String PROPERTY_LABEL_CATEGORIE_DELIB = "ods.pdd.label.categorieDeDeliberation";
    private static final String PROPERTY_LABEL_FILE_EXP_MOTIF = "ods.pdd.label.exposeDesMotifs";
    private static final String PROPERTY_LABEL_FILE_PJ_DELIB = "ods.pdd.label.projetDeDelibere";
    private static final String PROPERTY_LABEL_INTITULE = "ods.creationfichier.label.intitule";
    private static final String PROPERTY_LABEL_FICHIER = "ods.creationfichier.label.fichier";
    private static final String PROPERTY_LABEL_FICHIER_NO_PDF = "ods.creationfichier.label.fichier.noPDF";
    private static final String PROPERTY_LABEL_FICHIER_XPOSE_MOTIF_NO_PDF = "ods.message.pdd.fichier.xposeDesMotifsnoPDF";
    private static final String PROPERTY_LABEL_FICHIER_PROJET_DELIBERE_NO_PDF = "ods.message.pdd.fichier.projetDeDelibere";
    private static final String MARK_PDD_ECRAN = "pdd_ecran";
    private static final String MARK_PDD_ECRAN_MODIFICATION = "pdd_ecran_modification";
    private static final String MARK_LISTE_FORMATION_CONSEIL = "liste_formation_conseil";
    private static final String MARK_ID_FORMATION_CONSEIL_SELECTED = "id_formation_conseil_selected";
    private static final String MARK_LISTE_CATEGORIE_DELIBERATION = "liste_categorie_deliberation";
    private static final String MARK_ID_CATEGORIE_DELIBERATION_SELECTED = "id_categorie_deliberation_selected";
    private static final String MARK_LISTE_DIRECTION = "liste_direction";
    private static final String MARK_ID_DIRECTION_SELECTED = "id_direction_selected";
    private static final String MARK_LISTE_INSCRIT_ODJ = "liste_inscrit_odj";
    private static final String MARK_ID_INSCRIT_ODJ = "id_inscrit_odj_selected";
    private static final String MARK_LISTE_GROUPE_DEPOSITAIRE = "liste_groupe_depositaire";
    private static final String MARK_ID_GROUPE_DEPOSITAIRE_SELECTED = "id_groupe_depositaire_selected";
    private static final String MARK_LISTE_PUBLICATION = "liste_publication";
    private static final String MARK_ID_PUBLICATION_SELECTED = "id_publication_selected";
    private static final String MARK_LISTE_MODE_INTRO = "liste_mode_intro";
    private static final String MARK_ID_MODE_INTRO_SELECTED = "id_mode_intro_selected";
    private static final String MARK_LISTE_PDDS = "liste_pdds";
    private static final String MARK_LISTE_DIRECTION_CO_EMETRICE = "liste_direction_co_emetrice";
    private static final String MARK_ID_DIRECTION_CO_EMETRICE_SELECTED = "id_direction_co_emetrice_selected";
    private static final String MARK_LISTE_ARRONDISSEMENT = "liste_arrondissement";
    private static final String MARK_ID_ARRONDISSEMENT_SELECTED = "id_arronddissement_selected";
    private static final String MARK_LISTE_ARRONDISSEMENT_ADDED = "liste_arrondissement_added";
    private static final String MARK_LISTE_DIRECTION_CO_EMETRICE_ADDED = "liste_direction_co_emetrice_added";
    private static final String MARK_FICHIER_EXPOSE_DES_MOTIFS = "fichier_expose_des_motifs";
    private static final String MARK_LISTE_HISTORIQUES = "liste_historiques";
    private static final String MARK_FICHIER_PROJET_DE_DELIBERE = "fichier_projet_de_delibere";
    private static final String MARK_FICHIER_DELIBERATION = "fichier_deliberation";
    private static final String MARK_PIECES_ANNEXES = "liste_pieces_annexes";
    private static final String MARK_LISTE_AMENDEMENTS = "liste_amendements";
    private static final String MARK_LISTE_VOEUX_RATTACHES = "liste_voeuxRattaches";
    private static final String MARK_FORMATION_CONSEIL = "formation_conseil";
    private static final String MARK_INDEX_VA = "index";
    private static final String MARK_DIR = "dir";
    private static final String MARK_ENTREE = "entree";
    private static final String MARK_LISTE_SEANCES = "liste_seances";
    private static final String MARK_LISTE_STATUTS = "liste_statuts";
    private static final String MARK_ID_SEANCE_SELECTED = "id_seance_selected";
    private static final String MARK_ID_STATUT_SELECTED = "id_statut_selected";
    private static final String MARK_URL_JSP_APPELANT = "url_jsp_appelant";
    private static final String MARK_URL_RETOUR = "url_retour";
    private static final String MESSAGE_CANNOT_DELETE_PDD = "ods.message.cannotDeletePDD";
    private static final String MESSAGE_CANNOT_DELETE_PDD_ODJ_PUBLIE = "ods.message.cannotDeletePDDodjPublie";
    private static final String MESSAGE_CANNOT_DELETE_PDD_ODJ_NON_PUBLIE = "ods.message.cannotDeletePDDodjNonPublie";
    private static final String MESSAGE_CONFIRM_DELETE_PJ = "ods.message.confirmDeleteProjetDeDeliberation";
    private static final String MESSAGE_CONFIRM_DELETE_PP = "ods.message.confirmDeletePropositionDeDeliberation";
    private static final String MESSAGE_CONFIRM_DELETE_PIECE_ANNEXE = "ods.message.confirmDeletePieceAnnexe";
    private static final String JSP_URL_MODIFICATION_PDD_GESTION_AVAL = "jsp/admin/plugins/ods/projetdeliberation/ModificationPDD.jsp";
    private static final String CONSTANTE_PAGE_FIRST = "1";
    private static final String CONSTANTE_UP = "up";
    private static final String CONSTANTE_REMOVE = "remove";
    private static final String CONSTANTE_ACTION_APPLY = "apply";
    private static final String CONSTANTE_ACTION_CANCEL = "cancel";
    private static final String CONSTANTE_MODE_INTRO = "N";
    private static final String CONSTANTE_OUI = "ods.all.label.yes";
    private static final String CONSTANTE_NON = "ods.all.label.no";
    private static final String CONSTANTE_SOUMIS_AVIS_CONSEIL_LIBELLE = "ods.pdd.modification.modeIntroduction.soumisConseilArrondissement";
    private static final String CONSTANTE_SOUMIS_AVIS_CONSEIL_CODE = "CA";
    private static final String CONSTANTE_INTRO_DIRECT_LIBELLE = "ods.pdd.modification.modeIntroduction.introductionDirecte";
    private static final String CONSTANTE_INTRO_DIRECT_CODE = "D";
    private static final String CONSTANTE_TYPE_VR = "V";
    private static final String PLUGIN_ODS_NAME = "ods";

    // variable de session permettant de savoir si le retour ne doit pas ce
    // faire sur l'interface de gestion des vas
    private boolean _bUrlRetour;

    // variable de session stockant l'url de retour si le retour ne ce fait pas
    // sur l'interface de gestion des vas
    private String _strUrlRetour;
    private boolean _bGestionAval;
    private int _nIdSeanceSelected = -1;
    private int _nIdStatutSelected = -1;
    private boolean _bFonctionnaliteProjetDeDeliberation = true;
    private Seance _seance;

    // Referencelist en vue de stocker l'ensemble des items représentant les
    // types de documents
    private ReferenceList _referenceFormationConseilList;
    private int _nIdFormationConseilSelected;

    // Referencelist en vue de stocker l'ensemble des items représentant les
    // Categories du de deliberations
    private ReferenceList _referenceCategorieDeliberationList;
    private int _nIdCategorieDeliberationSelected;

    // Referencelist en vue de stocker l'ensemble des items représentant les
    // directions
    private ReferenceList _referenceDirectionList;
    private String _strDirectionSelected;

    // Referencelist en vue de stocker l'ensemble des items représentant les
    // directions
    private ReferenceList _referenceGroupeDepositaireList;
    private String _strGroupeDepositaireSelected;

    // Referencelist en vue de stocker les choix dans l'inscription dans l'ODJ
    private ReferenceList _referenceInscritODJList;
    private int _nIdInscritODJSelected;

    // Referencelist en vue de stocker les choix de publication
    private ReferenceList _referencePublicationList;
    private int _nIdPublicationSelected;

    // Referencelist en vue de stocker les différents Mode d'Intro
    private ReferenceList _referenceModeIntroList;
    private String _strModeIntroSelected;

    // Gere la page du Paginator
    private String _strCurrentPageIndex;

    // Gere le nombre d'item par page
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;
    private List<DirectionCoEmetrice> _directionCoEmetriceAdded;
    private ReferenceList _referenceDirectionCoEmetriceSelect;
    private String _strDirectionCoEmetriceSelected;
    private List<Arrondissement> _arrondissementAdded;
    private ReferenceList _referenceArrondissementSelect;
    private int _nIdArrondissementSelected;

    /**
     * Constructor
     *
     */
    public ProjetDeDeliberationJspBean(  )
    {
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_FICHIERS_PER_PAGE, 10 );
    }

    /**
     * Initialisation
     * @param request la requête HTTP
     */
    public void init( HttpServletRequest request )
    {
        int nIdPdd = OdsUtils.getIntegerParameter( OdsParameters.ID_PDD, request );

        if ( nIdPdd > 0 )
        {
            Plugin plugin = PluginService.getPlugin( PLUGIN_ODS_NAME );

            PDD pdd = PDDHome.findByPrimaryKey( nIdPdd, plugin );

            try
            {
                if ( pdd.getTypePdd(  ).equals( PDD.CONSTANTE_TYPE_PROJET ) )
                {
                    init( request, RIGHT_ODS_PROJET_DE_DELIB );
                    _bFonctionnaliteProjetDeDeliberation = true;
                }
                else
                {
                    init( request, RIGHT_ODS_PROP_DE_DELIB );
                    _bFonctionnaliteProjetDeDeliberation = false;
                }
            }
            catch ( AccessDeniedException ade )
            {
                AppLogService.error( ade );
            }
        }
    }

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
            _strUrlRetour += ( "&" + OdsParameters.ID_ODJ + "=" + request.getParameter( OdsParameters.ID_ODJ ) );
        }

        else if ( request.getParameter( OdsParameters.ID_ENTREE ) != null )
        {
            _strUrlRetour += ( "&" + OdsParameters.ID_ENTREE + "=" + request.getParameter( OdsParameters.ID_ENTREE ) );
        }
    }

    /**
     * retourne l'ecran de creation des projet de deliberation
     *
     * @param request la requête HTTP
     * @return String template
     */
    public String getPDD( HttpServletRequest request )
    {
        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_CREATION_PJ );
        }
        else
        {
            setPageTitleProperty( PROPERTY_PAGE_CREATION_PP );
        }

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        HtmlTemplate template;

        /*
         * GESTION DES PROFILS
         * -------------------
         * Si l'utilisateur n'est pas autorisé a effectuer cette fonction,
         * il est renvoyé vers une page lui indiquant qu'il n'a pas les droits nécessaires.
         */
        if ( isFonctionnaliteProjetDeDeliberation(  ) &&
                !RBACService.isAuthorized( ProjetProchaineSeanceResourceIdService.PROJET_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, ProjetProchaineSeanceResourceIdService.PERMISSION_CREATION, getUser(  ) ) )
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );

            return getAdminPage( template.getHtml(  ) );
        }

        resetCreation(  );

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            initCreationDirectionCoEmetrice( request );
        }

        initCreationArrondissement( request );

        model.put( Seance.MARK_PROCHAINE_SEANCE, _seance );
        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );
        model.put( MARK_FONCTIONNALITE_IS_PROJET, isFonctionnaliteProjetDeDeliberation(  ) );
        model.put( MARK_LISTE_FORMATION_CONSEIL, _referenceFormationConseilList );
        model.put( MARK_ID_FORMATION_CONSEIL_SELECTED, PDDFilter.ALL_INT );
        model.put( MARK_LISTE_MODE_INTRO, _referenceModeIntroList );
        model.put( MARK_ID_MODE_INTRO_SELECTED, PDDFilter.ALL_INT );
        model.put( MARK_LISTE_CATEGORIE_DELIBERATION, _referenceCategorieDeliberationList );
        model.put( MARK_ID_CATEGORIE_DELIBERATION_SELECTED, PDDFilter.ALL_INT );
        model.put( MARK_LISTE_DIRECTION_CO_EMETRICE, _referenceDirectionCoEmetriceSelect );
        model.put( MARK_ID_DIRECTION_CO_EMETRICE_SELECTED, PDDFilter.ALL_STRING );
        model.put( MARK_LISTE_ARRONDISSEMENT, _referenceArrondissementSelect );
        model.put( MARK_ID_ARRONDISSEMENT_SELECTED, PDDFilter.ALL_INT );

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            model.put( MARK_LISTE_DIRECTION, _referenceDirectionList );
            model.put( MARK_ID_DIRECTION_SELECTED, PDDFilter.ALL_STRING );
        }
        else
        {
            model.put( MARK_LISTE_GROUPE_DEPOSITAIRE, _referenceGroupeDepositaireList );
            model.put( MARK_ID_GROUPE_DEPOSITAIRE_SELECTED, PDDFilter.ALL_STRING );
            model.put( MARK_LISTE_PUBLICATION, _referencePublicationList );
            model.put( MARK_ID_PUBLICATION_SELECTED, PDDFilter.ALL_INT );
        }

        template = AppTemplateService.getTemplate( TEMPLATE_CREATION_PDD, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * retourne l'ecran de modification complète des projet de deliberation
     *
     * @param request la requête HTTP
     * @return String template
     */
    public String getModificationCompleteProjetDeDeliberation( HttpServletRequest request )
    {
        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            setPageTitleProperty( PROPERTY_PAGE_MODIFICATION_PJ );
        }
        else
        {
            setPageTitleProperty( PROPERTY_PAGE_MODIFICATION_PP );
        }

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        HtmlTemplate template;

        // gestion dans le cas d'un appel via l'ordre du jour
        String strIdOdj = request.getParameter( OdsParameters.ID_ODJ );

        if ( ( strIdOdj != null ) && !strIdOdj.trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            model.put( OdsParameters.ID_ODJ, strIdOdj );
        }

        /*
         * Permission de modifier tous les champs du formulaire
         */
        boolean bPermissionModification = true;

        if ( !isGestionAval(  ) && isFonctionnaliteProjetDeDeliberation(  ) &&
                !RBACService.isAuthorized( ProjetProchaineSeanceResourceIdService.PROJET_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, ProjetProchaineSeanceResourceIdService.PERMISSION_MAJ_COMPLETE,
                    getUser(  ) ) )
        {
            bPermissionModification = false;
        }
        else if ( !isGestionAval(  ) && !isFonctionnaliteProjetDeDeliberation(  ) &&
                !RBACService.isAuthorized( PropositionProchaineSeanceResourceIdService.PROPOSITION_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, PropositionProchaineSeanceResourceIdService.PERMISSION_MISE_A_JOUR,
                    getUser(  ) ) )
        {
            bPermissionModification = false;
        }
        else if ( isGestionAval(  ) &&
                !RBACService.isAuthorized( PddGestionAvalResourceIdService.PDD_GESTION_AVAL,
                    RBAC.WILDCARD_RESOURCES_ID, PddGestionAvalResourceIdService.PERMISSION_MAJ_COMPLETE, getUser(  ) ) )
        {
            bPermissionModification = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_MODIFICATION, bPermissionModification );

        Seance seance = null;

        if ( isGestionAval(  ) )
        {
            int nIdSeance = OdsUtils.getIntegerParameter( OdsParameters.ID_SEANCE, request );

            if ( nIdSeance == -1 )
            {
                nIdSeance = _nIdSeanceSelected;
            }

            seance = SeanceHome.findByPrimaryKey( nIdSeance, getPlugin(  ) );

            model.put( Seance.MARK_SEANCE, seance );
        }
        else
        {
            if ( _seance == null )
            {
                _seance = SeanceHome.getProchaineSeance( getPlugin(  ) );
            }

            model.put( Seance.MARK_PROCHAINE_SEANCE, _seance );
        }

        int pddPrimaryKey = OdsUtils.getIntegerParameter( OdsParameters.ID_PDD, request );

        if ( pddPrimaryKey == -1 )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        FichierFilter fichierFilter = new FichierFilter(  );
        PDD pdd = null;

        fichierFilter.setPDD( pddPrimaryKey );
        pdd = PDDHome.findByPrimaryKey( pddPrimaryKey, getPlugin(  ) );

        if ( pdd == null )
        {
            return getPDDList( request );
        }

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            _directionCoEmetriceAdded = PDDHome.findDirectionsCoEmtricesByPDD( pddPrimaryKey, getPlugin(  ) );
        }

        _arrondissementAdded = PDDHome.findArrondissementsByPDD( pddPrimaryKey, getPlugin(  ) );

        List<Fichier> fichiers = FichierHome.findByFilter( fichierFilter, getPlugin(  ) );
        Fichier fichierExposeDesMotifs = null;
        Fichier fichierProjetDeDelibere = null;
        List<Fichier> piecesAnnexes = new ArrayList<Fichier>(  );

        for ( int i = 0; ( fichiers != null ) && ( i < fichiers.size(  ) ); i++ )
        {
            Fichier f = fichiers.get( i );

            if ( ( f.getTypdeDocument(  ) != null ) &&
                    ( TypeDocumentEnum.EXPOSE_DES_MOTIFS.getId(  ) == f.getTypdeDocument(  ).getId(  ) ) )
            {
                fichierExposeDesMotifs = f;
            }

            if ( ( f.getTypdeDocument(  ) != null ) &&
                    ( TypeDocumentEnum.PROJET_DE_DELIBERE.getId(  ) == f.getTypdeDocument(  ).getId(  ) ) )
            {
                fichierProjetDeDelibere = f;
            }

            if ( ( f.getTypdeDocument(  ) != null ) &&
                    ( TypeDocumentEnum.PIECE_ANNEXE.getId(  ) == f.getTypdeDocument(  ).getId(  ) ) )
            {
                piecesAnnexes.add( f );
            }
        }

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            initCreationDirectionCoEmetrice( request );
        }

        initCreationArrondissement( request );

        if ( isGestionAval(  ) )
        {
            // Récupération du fichier de délibération
            FichierFilter fichierDeliberationFilter = new FichierFilter(  );
            fichierDeliberationFilter.setPDD( pddPrimaryKey );
            fichierDeliberationFilter.setIdTypeDocument( TypeDocumentEnum.DELIBERATION.getId(  ) );

            List<Fichier> listFichiersDeliberation = FichierHome.findByFilter( fichierDeliberationFilter, getPlugin(  ) );
            Fichier fichierDeliberation = null;

            if ( ( listFichiersDeliberation != null ) && ( listFichiersDeliberation.size(  ) >= 1 ) )
            {
                fichierDeliberation = listFichiersDeliberation.get( 0 );
            }

            // Récupération de la liste des statuts
            StatutFilter statutFilter = new StatutFilter(  );
            statutFilter.setPourPDD( true );

            ReferenceList refListStatuts = new ReferenceList(  );

            // ajout dans la referenceList de l'ensemble des items représentant les
            // statuts
            refListStatuts.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

            initRefListStatut( statutFilter, refListStatuts );

            int nIdStatutSelected;

            if ( pdd.getStatut(  ) != null )
            {
                nIdStatutSelected = pdd.getStatut(  ).getIdStatut(  );
            }
            else
            {
                nIdStatutSelected = -1;
            }

            model.put( MARK_LISTE_STATUTS, refListStatuts );
            model.put( MARK_ID_STATUT_SELECTED, nIdStatutSelected );

            if ( fichierDeliberation != null )
            {
                model.put( MARK_FICHIER_DELIBERATION, fichierDeliberation );
            }
        }

        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );
        model.put( MARK_FONCTIONNALITE_IS_PROJET, isFonctionnaliteProjetDeDeliberation(  ) );
        model.put( PDD.MARK_PDD, pdd );

        model.put( MARK_LISTE_FORMATION_CONSEIL, _referenceFormationConseilList );

        if ( pdd.getFormationConseil(  ) != null )
        {
            model.put( MARK_ID_FORMATION_CONSEIL_SELECTED, pdd.getFormationConseil(  ).getIdFormationConseil(  ) );
        }
        else
        {
            model.put( MARK_ID_FORMATION_CONSEIL_SELECTED, OdsConstants.FILTER_ALL );
        }

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            if ( _referenceDirectionList == null )
            {
                initDirection( request );
            }

            model.put( MARK_LISTE_DIRECTION, _referenceDirectionList );

            if ( pdd.getDirection(  ) != null )
            {
                int nIdDirection = pdd.getDirection(  ).getIdDirection(  );
                Direction direction = DirectionHome.findByPrimaryKey( nIdDirection, getPlugin(  ) );
                pdd.setDirection( direction );
                model.put( MARK_ID_DIRECTION_SELECTED, nIdDirection );
            }
            else
            {
                model.put( MARK_ID_DIRECTION_SELECTED, OdsConstants.FILTER_ALL );
            }
        }
        else
        {
            model.put( MARK_LISTE_GROUPE_DEPOSITAIRE, _referenceGroupeDepositaireList );

            if ( pdd.getGroupePolitique(  ) != null )
            {
                model.put( MARK_ID_GROUPE_DEPOSITAIRE_SELECTED, pdd.getGroupePolitique(  ).getIdGroupe(  ) );
            }
            else
            {
                model.put( MARK_ID_GROUPE_DEPOSITAIRE_SELECTED, OdsConstants.FILTER_ALL );
            }
        }

        if ( _referenceModeIntroList == null )
        {
            initModeIntro( request );
        }

        model.put( MARK_LISTE_MODE_INTRO, _referenceModeIntroList );

        if ( pdd.getModeIntroduction(  ) != null )
        {
            model.put( MARK_ID_MODE_INTRO_SELECTED, pdd.getModeIntroduction(  ) );
        }
        else
        {
            model.put( MARK_ID_MODE_INTRO_SELECTED, CONSTANTE_MODE_INTRO );
        }

        if ( _referenceCategorieDeliberationList == null )
        {
            initCategorieDeliberation( request );
        }

        model.put( MARK_LISTE_CATEGORIE_DELIBERATION, _referenceCategorieDeliberationList );

        if ( pdd.getCategorieDeliberation(  ) != null )
        {
            model.put( MARK_ID_CATEGORIE_DELIBERATION_SELECTED, pdd.getCategorieDeliberation(  ).getIdCategorie(  ) );
        }
        else
        {
            model.put( MARK_ID_CATEGORIE_DELIBERATION_SELECTED, OdsConstants.FILTER_ALL );
        }

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            if ( _referenceDirectionCoEmetriceSelect == null )
            {
                initCreationDirectionCoEmetrice( request );
            }

            model.put( MARK_LISTE_DIRECTION_CO_EMETRICE, _referenceDirectionCoEmetriceSelect );
            model.put( MARK_ID_DIRECTION_CO_EMETRICE_SELECTED, OdsConstants.FILTER_ALL );

            if ( ( _directionCoEmetriceAdded != null ) && ( _directionCoEmetriceAdded.size(  ) > 0 ) )
            {
                model.put( MARK_LISTE_DIRECTION_CO_EMETRICE_ADDED, _directionCoEmetriceAdded );
            }
        }

        if ( _referenceArrondissementSelect == null )
        {
            initCreationArrondissement( request );
        }

        model.put( MARK_LISTE_ARRONDISSEMENT, _referenceArrondissementSelect );
        model.put( MARK_ID_ARRONDISSEMENT_SELECTED, OdsConstants.FILTER_ALL );

        if ( ( _arrondissementAdded != null ) && ( _arrondissementAdded.size(  ) > 0 ) )
        {
            model.put( MARK_LISTE_ARRONDISSEMENT_ADDED, _arrondissementAdded );
        }

        if ( fichierExposeDesMotifs != null )
        {
            model.put( MARK_FICHIER_EXPOSE_DES_MOTIFS, fichierExposeDesMotifs );
        }

        if ( fichierProjetDeDelibere != null )
        {
            model.put( MARK_FICHIER_PROJET_DE_DELIBERE, fichierProjetDeDelibere );
        }

        if ( ( piecesAnnexes != null ) && ( piecesAnnexes.size(  ) > 0 ) )
        {
            model.put( MARK_PIECES_ANNEXES, piecesAnnexes );
        }

        if ( ( pdd.getAmendements(  ) != null ) && ( pdd.getAmendements(  ).size(  ) > 0 ) )
        {
            model.put( MARK_LISTE_AMENDEMENTS, pdd.getAmendements(  ) );
        }

        if ( ( pdd.getVoeuxRattaches(  ) != null ) && ( pdd.getVoeuxRattaches(  ).size(  ) > 0 ) )
        {
            model.put( MARK_LISTE_VOEUX_RATTACHES, pdd.getVoeuxRattaches(  ) );
        }

        if ( isGestionAval(  ) )
        {
            template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_PDD_GESTION_AVAL, getLocale(  ), model );
        }
        else
        {
            template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_COMPLETE_PDD, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Creation d'un fichier si tous les champs obligatoires du formulaire de
     * création d'un fichier ont été renseigné,<BR>
     * la méthode retourne l'url de gestion des fichiers,<BR>
     * sinon l'url retournée est celle de l'interface spécifiant les messages
     * d'erreurs.
     *
     * @param request
     *            la requête HTTP
     * @return String template
     */
    public String doCreationPieceAnnexe( HttpServletRequest request )
    {
        setPageTitleProperty( ( isFonctionnaliteProjetDeDeliberation(  ) ) ? PROPERTY_PAGE_MODIFICATION_PJ
                                                                           : PROPERTY_PAGE_MODIFICATION_PP );

        if ( isValidePieceAnnexe( request ) )
        {
            Timestamp dateForVersion = new Timestamp( System.currentTimeMillis(  ) );

            int nIdPdd = OdsUtils.getIntegerParameter( OdsParameters.ID_PDD, request );

            PDD pdd = PDDHome.findByPrimaryKey( nIdPdd, getPlugin(  ) );

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            String strCreationIntitule;
            FileItem fileItem = null;
            String strCreationFichierTxtRecherche;

            strCreationIntitule = request.getParameter( OdsParameters.FICHIER_INTITULE );

            if ( ( strCreationIntitule == null ) || OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strCreationIntitule ) )
            {
                Object[] messagesArgs = { I18nService.getLocalizedString( PROPERTY_LABEL_INTITULE, getLocale(  ) ) };

                return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                    messagesArgs, AdminMessage.TYPE_STOP );
            }

            strCreationFichierTxtRecherche = request.getParameter( OdsParameters.FICHIER_TXT_RECHERCHE );

            fileItem = multipartRequest.getFile( OdsParameters.FICHIER_PHYSIQUE );

            Fichier newFichier = new Fichier(  );
            newFichier.setPDD( pdd );

            FichierPhysique newFichierPhysique = new FichierPhysique(  );

            // Creation de l'objet représentant le fichier physique
            if ( ( fileItem != null ) && ( fileItem.getName(  ) != null ) &&
                    !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileItem.getName(  ) ) )
            {
                newFichierPhysique.setDonnees( fileItem.get(  ) );
                newFichierPhysique.setIdFichier( FichierPhysiqueHome.create( newFichierPhysique, getPlugin(  ) ) );
                newFichier.setFichier( newFichierPhysique );
                newFichier.setNom( getNameCourt( fileItem ) );

                String strExtension = fileItem.getName(  ).substring( fileItem.getName(  ).length(  ) - 3 ).toUpperCase(  );

                if ( ( strExtension != null ) && ( strExtension.length(  ) == 3 ) &&
                        OdsConstants.CONSTANTE_PDF.equals( strExtension ) )
                {
                    newFichier.setExtension( strExtension );
                }
                else
                {
                    return AdminMessageService.getMessageUrl( request, PROPERTY_LABEL_FICHIER_NO_PDF,
                        AdminMessage.TYPE_STOP );
                }

                newFichier.setTaille( (int) fileItem.getSize(  ) );
            }
            else
            {
                Object[] messagesArgs = { I18nService.getLocalizedString( PROPERTY_LABEL_FICHIER, getLocale(  ) ) };

                return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                    messagesArgs, AdminMessage.TYPE_STOP );
            }

            // Creation de l'objet représentant le Fichier
            if ( _seance != null )
            {
                newFichier.setSeance( _seance );
            }

            newFichier.setTypdeDocument( TypeDocumentEnum.PIECE_ANNEXE.getTypeDocumentOnlyWidthId(  ) );
            newFichier.setTitre( strCreationIntitule );
            newFichier.setTexte( strCreationFichierTxtRecherche );

            newFichier.setEnLigne( false );

            int newVersion = pdd.getVersion() + 1;
            if ( pdd.isEnLigne(  ) )
            {
                newFichier.setEnLigne( true );
                // On incremente le numéro de version
                newFichier.setVersion( 1 );
                newFichier.setDatePublication( dateForVersion );

                // On incremente le numéro de version du PDD
                pdd.setVersion( newVersion );
                pdd.setDatePublication( dateForVersion );
                PDDHome.update( pdd, getPlugin( ) );
            }

            FichierHome.create( newFichier, getPlugin(  ) );  

            if ( pdd.isEnLigne(  ) )
            {
                // Ajout de l'historique du fichier
                Historique historique = new Historique(  );
                historique.setVersion( 1 );
                historique.setDatePublication( dateForVersion );
                historique.setIdDocument( newFichier.getId(  ) );
                HistoriqueHome.create( historique, getPlugin(  ) );

                // Ajout de l'historique du PDD
                Historique historiquePDD = new Historique(  );
                historiquePDD.setVersion( newVersion );
                historiquePDD.setDatePublication( dateForVersion );
                historiquePDD.setIdPDD( pdd.getIdPdd(  ) );
                HistoriqueHome.create( historiquePDD, getPlugin(  ) );
            }

            return ( AppPathService.getBaseUrl( request ) + TEMPLATE_JSP_MODIFICATION_COMPLETE + "?" +
            OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_PDD + "=" + nIdPdd );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }
    }

    /**
     * Ajout d'un voeuxAmandement au pdd
     *
     * @param request la requête HTTP
     * @return String template
     */
    public String doAjoutVoeuAmendement( HttpServletRequest request )
    {
        setPageTitleProperty( ( isFonctionnaliteProjetDeDeliberation(  ) ) ? PROPERTY_PAGE_MODIFICATION_PJ
                                                                           : PROPERTY_PAGE_MODIFICATION_PP );

        String strIdPDD = request.getParameter( OdsParameters.ID_PDD );
        String strIdSeance = request.getParameter( OdsParameters.ID_SEANCE );
        String strAction = request.getParameter( OdsParameters.A_FAIRE );

        if ( ( strAction != null ) && strAction.equals( CONSTANTE_ACTION_CANCEL ) && ( strIdPDD != null ) )
        {
            String strJsp = TEMPLATE_JSP_MODIFICATION_COMPLETE;
            String strSeance = OdsConstants.CONSTANTE_CHAINE_VIDE;

            if ( isGestionAval(  ) && ( strIdSeance != null ) )
            {
                strJsp = JSP_URL_MODIFICATION_PDD_GESTION_AVAL;
                strSeance = "&" + OdsParameters.ID_SEANCE + "=" + strIdSeance.trim(  );
            }

            return ( AppPathService.getBaseUrl( request ) + strJsp + "?" + OdsParameters.PLUGIN_NAME + "&" +
            OdsParameters.ID_PDD + "=" + strIdPDD + strSeance );
        }

        if ( strIdPDD != null )
        {
            int nIdPdd = OdsUtils.getIntegerParameter( OdsParameters.ID_PDD, request );

            int nIdVA;

            if ( isGestionAval(  ) )
            {
                nIdVA = OdsUtils.getIntegerParameter( OdsParameters.ID_PARENT, request );
            }
            else
            {
                nIdVA = OdsUtils.getIntegerParameter( OdsParameters.ID_VOEUAMENDEMENT, request );
            }

            if ( nIdVA != -1 )
            {
                PDDHome.addVoeuAmendement( nIdVA, nIdPdd, getPlugin(  ) );
            }
            else
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            String strJsp = TEMPLATE_JSP_MODIFICATION_COMPLETE;
            String strSeance = OdsConstants.CONSTANTE_CHAINE_VIDE;

            if ( isGestionAval(  ) && ( strIdSeance != null ) )
            {
                strJsp = JSP_URL_MODIFICATION_PDD_GESTION_AVAL;
                strSeance = "&" + OdsParameters.ID_SEANCE + "=" + strIdSeance.trim(  );
            }

            return AppPathService.getBaseUrl( request ) + strJsp + "?" + OdsParameters.PLUGIN_NAME + "&" +
            OdsParameters.ID_PDD + "=" + strIdPDD + strSeance;
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }
    }

    /**
     * Modifie la piece annexe et le fichier physique Si le fichie est en ligne,
     * alors la version est incrémenté de 1 et ajout d'un historique si tous les
     * champs obligatoires du formulaire de modification d'un fichier ont été
     * renseigné,<BR>
     * la méthode retourne l'url de gestion des fichiers,<BR>
     * sinon l'url retournée est celle de l'interface spécifiant les messages
     * d'erreurs.
     *
     * @param request
     *            la requête HTTP
     * @return String template
     */
    public String doModificationPieceAnnexe( HttpServletRequest request )
    {
        Timestamp dateForVersion = new Timestamp( System.currentTimeMillis(  ) );

        int nIdPDD = OdsUtils.getIntegerParameter( OdsParameters.ID_PDD, request );

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        int nIdFichier = OdsUtils.getIntegerParameter( OdsParameters.ID_FICHIER, request );

        if ( nIdFichier != -1 )
        {
            Fichier fichier = FichierHome.findByPrimaryKey( nIdFichier, getPlugin(  ) );
            FichierPhysique fichierPhysique = FichierPhysiqueHome.findByPrimaryKey( fichier.getFichier(  ).getIdFichier(  ),
                    getPlugin(  ) );

            // Récupération des modifications
            String strModificationIntitule = request.getParameter( OdsParameters.FICHIER_INTITULE );

            if ( ( strModificationIntitule == null ) ||
                    OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strModificationIntitule ) )
            {
                Object[] messagesArgs = { I18nService.getLocalizedString( PROPERTY_LABEL_INTITULE, getLocale(  ) ) };

                return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                    messagesArgs, AdminMessage.TYPE_STOP );
            }

            String strModificationFichierTxtRecherche = request.getParameter( OdsParameters.FICHIER_TXT_RECHERCHE );
            FileItem fileItem = multipartRequest.getFile( OdsParameters.FICHIER_PHYSIQUE );

            if ( ( fileItem != null ) && ( fileItem.getName(  ) != null ) &&
                    !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileItem.getName(  ) ) )
            {
                // Modification de l'objet représentant le fichier physique
                fichierPhysique.setDonnees( fileItem.get(  ) );
                FichierPhysiqueHome.update( fichierPhysique, getPlugin(  ) );
                fichier.setFichier( fichierPhysique );
                fichier.setNom( getNameCourt( fileItem ) );

                String strExtension = fileItem.getName(  ).substring( fileItem.getName(  ).length(  ) - 3 ).toUpperCase(  );

                if ( ( strExtension != null ) && ( strExtension.length(  ) == 3 ) &&
                        OdsConstants.CONSTANTE_PDF.equals( strExtension ) )
                {
                    fichier.setExtension( strExtension );
                }
                else
                {
                    return AdminMessageService.getMessageUrl( request, PROPERTY_LABEL_FICHIER_NO_PDF,
                        AdminMessage.TYPE_STOP );
                }

                fichier.setTaille( (int) fileItem.getSize(  ) );
            }

            // Modification de l'objet représentant le Fichier
            fichier.setTitre( strModificationIntitule );
            fichier.setTexte( strModificationFichierTxtRecherche );

            if ( fichier.getEnLigne(  ) )
            {
                // on incremente le numero de version
                int newVersion = fichier.getVersion(  ) + 1;
                fichier.setVersion( newVersion );
                fichier.setDatePublication( dateForVersion );

                // Ajout de l'historique
                Historique historique = new Historique(  );
                historique.setVersion( newVersion );
                historique.setDatePublication( dateForVersion );
                historique.setIdDocument( fichier.getId(  ) );
                HistoriqueHome.create( historique, getPlugin(  ) );
            }

            FichierHome.update( fichier, getPlugin(  ) );

            PDD pddUpdated = PDDHome.findByPrimaryKey( nIdPDD, getPlugin(  ) );

            if ( pddUpdated.isEnLigne(  ) )
            {
                int newVersion = pddUpdated.getVersion(  ) + 1;
                // on incremente le numero de version
                pddUpdated.setVersion( newVersion );
                pddUpdated.setDatePublication( dateForVersion );

                // Ajout de l'historique
                Historique historique = new Historique(  );
                historique.setVersion( newVersion );
                historique.setDatePublication( dateForVersion );
                historique.setIdPDD( pddUpdated.getIdPdd(  ) );
                HistoriqueHome.create( historique, getPlugin(  ) );

                PDDHome.update( pddUpdated, getPlugin(  ) );
            }

            return ( AppPathService.getBaseUrl( request ) + TEMPLATE_JSP_MODIFICATION_COMPLETE + "?" +
            OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_PDD + "=" + nIdPDD );
        }
        else
        {
            Object[] messagesArgs = { I18nService.getLocalizedString( PROPERTY_LABEL_FICHIER, getLocale(  ) ) };

            return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                messagesArgs, AdminMessage.TYPE_STOP );
        }
    }

    /**
     * retourne l'ecran de visualisation de l'historique des versions d'une
     * piece annexe
     *
     * @param request
     *            la requête HTTP
     * @return String template
     */
    public String getHistoriquePieceAnnexe( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_HISTORIQUE );

        int nIdFichier = OdsUtils.getIntegerParameter( OdsParameters.ID_FICHIER, request );

        Fichier fichier = FichierHome.findByPrimaryKey( nIdFichier, getPlugin(  ) );

        List<Historique> historiques = HistoriqueHome.getHistoriqueList( fichier, getPlugin(  ) );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( MARK_LISTE_HISTORIQUES, historiques );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_HISTORIQUE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * retourne une demande de confirmation pour la suppression du pdd choisie
     *
     * @param request la requête HTTP
     * @return String template
     */
    public String getSuppressionPDD( HttpServletRequest request )
    {
        int nIdPDD = OdsUtils.getIntegerParameter( OdsParameters.ID_PDD, request );
        PDD pdd = null;

        /*
         * GESTION DES PROFILS
         * -------------------
         * Si l'utilisateur n'est pas autorisé a effectuer cette fonction,
         * il est renvoyé vers une page lui indiquant qu'il n'a pas les droits nécessaires.
         */
        boolean bAuthorized = true;

        if ( isFonctionnaliteProjetDeDeliberation(  ) &&
                !RBACService.isAuthorized( ProjetProchaineSeanceResourceIdService.PROJET_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, ProjetProchaineSeanceResourceIdService.PERMISSION_SUPPRESSION,
                    getUser(  ) ) )
        {
            bAuthorized = false;
        }
        else if ( isGestionAval(  ) &&
                !RBACService.isAuthorized( PddGestionAvalResourceIdService.PDD_GESTION_AVAL,
                    RBAC.WILDCARD_RESOURCES_ID, PddGestionAvalResourceIdService.PERMISSION_SUPPRESSION, getUser(  ) ) )
        {
            bAuthorized = false;
        }

        pdd = PDDHome.findByPrimaryKey( nIdPDD, getPlugin(  ) );

        if ( pdd == null )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( !isFonctionnaliteProjetDeDeliberation(  ) && pdd.isEnLigne(  ) &&
                !RBACService.isAuthorized( PropositionProchaineSeanceResourceIdService.PROPOSITION_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, PropositionProchaineSeanceResourceIdService.PERMISSION_SUPPRESSION,
                    getUser(  ) ) )
        {
            bAuthorized = false;
        }

        if ( !bAuthorized )
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }

        Seance seance = SeanceHome.getProchaineSeance( getPlugin(  ) );

        EntreeOrdreDuJourFilter filter = new EntreeOrdreDuJourFilter(  );
        filter.setIdPdd( pdd.getIdPdd(  ) );
        filter.setIdSeance( seance.getIdSeance(  ) );

        List<EntreeOrdreDuJour> listEntreesOdj = null;

        String strMessageUrl = null;
        /*
         * PROD-P1-CU03-RG13
         * Si le projet est inscrit dans un ordre du jour définitif de la prochaine séance
         * alors on affiche un message bloquant lui signalant qu'il n'est pas possible de
         * supprimer le projet.
         */
        filter.setIdPublie( 1 ); // 1 = publié
        listEntreesOdj = EntreeOrdreDuJourHome.findEntreesListByFilter( filter, getPlugin(  ) );

        if ( ( strMessageUrl == null ) && 
        		( listEntreesOdj != null ) && !listEntreesOdj.isEmpty(  ) )
        {
            strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_PDD_ODJ_PUBLIE,
                AdminMessage.TYPE_STOP );
        }

        /*
         * PROD-P1-CU03-RG13
         * Si le projet est inscrit dans un ordre du jour non publié de la
         * prochaine séance alors un message d’avertissement est affiché.
         */
        filter.setIdPublie( 0 ); // 0 = non publié
        listEntreesOdj = EntreeOrdreDuJourHome.findEntreesListByFilter( filter, getPlugin(  ) );

        if ( ( strMessageUrl == null ) && 
        		( listEntreesOdj != null ) && !listEntreesOdj.isEmpty(  ) )
        {
            UrlItem url = new UrlItem( PROPERTY_URL_DO_SUPPRESSION_PDD_JSP );
            url.addParameter( OdsParameters.ID_PDD, "" + nIdPDD );

            strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_PDD_ODJ_NON_PUBLIE,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
        }

        /*
         * Sinon on affiche juste le message de confirmation habituel.
         */
        if ( ( strMessageUrl == null ) && ( nIdPDD != -1 ) )
        {
            UrlItem url = new UrlItem( PROPERTY_URL_DO_SUPPRESSION_PDD_JSP );
            url.addParameter( OdsParameters.ID_PDD, "" + nIdPDD );

            strMessageUrl =  AdminMessageService.getMessageUrl( request,
                ( isFonctionnaliteProjetDeDeliberation(  ) ) ? MESSAGE_CONFIRM_DELETE_PJ : MESSAGE_CONFIRM_DELETE_PP,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
        }
        
        if ( strMessageUrl != null )
        {
        	return strMessageUrl;
        }

        return getHomeUrl( request );
    }

    /**
     * retourne une demande de confirmation pour la suppression de la piece
     * annexe choisie
     *
     * @param request
     *            la requête HTTP
     * @return String template
     */
    public String getSuppressionPieceAnnexe( HttpServletRequest request )
    {
        if ( request.getParameter( OdsParameters.ID_FICHIER ) != null )
        {
            String strIdFichier = request.getParameter( OdsParameters.ID_FICHIER );
            String strIdPDD = request.getParameter( OdsParameters.ID_PDD );
            UrlItem url = new UrlItem( PROPERTY_URL_DO_SUPPRESSION_PIECE_ANNEXE_JSP );
            url.addParameter( OdsParameters.ID_FICHIER, strIdFichier );
            url.addParameter( OdsParameters.ID_PDD, strIdPDD );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_PIECE_ANNEXE, url.getUrl(  ),
                AdminMessage.TYPE_CONFIRMATION );
        }
        else
        {
            return getHomeUrl( request );
        }
    }

    /**
     * retourne l'ecran de visualisation de l'historique des versions d'un pdd
     *
     * @param request
     *            la requête HTTP
     * @return String template
     */
    public String getHistoriquePDD( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_HISTORIQUE );

        int nIdFichier = OdsUtils.getIntegerParameter( OdsParameters.ID_PDD, request );

        PDD pdd = PDDHome.findByPrimaryKey( nIdFichier, getPlugin(  ) );

        List<Historique> historiques = HistoriqueHome.getHistoriqueList( pdd, getPlugin(  ) );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LISTE_HISTORIQUES, historiques );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_HISTORIQUE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Publication d'un pdd Si "OdsParameters.PDD_IS_PUBLIER" égale à TRUE,
     * alors la méthode <b>publie</b> le pdd et incrémente la version<BR>
     * Dans ce cas, un historique est crée Sinon la méthode change le statut
     * enLigne d'un pdd a FALSE
     *
     * @param request la requête HTTP
     * @return String template
     */
    public String doPublicationPDD( HttpServletRequest request )
    {
        HtmlTemplate template;

        Timestamp dateForVersion = new Timestamp( System.currentTimeMillis(  ) );

        int nIdPDD = OdsUtils.getIntegerParameter( OdsParameters.ID_PDD, request );
        int nIsPublier = OdsUtils.getIntegerParameter( OdsParameters.PDD_IS_PUBLIER_OR_DEPUBLIER, request );
        String strIsConfirmation = request.getParameter( OdsParameters.PDD_IS_CONFIRMATION );
        Seance seance = SeanceHome.getProchaineSeance( getPlugin(  ) );
        
        if ( ( nIdPDD != -1 ) && ( nIsPublier != -1 ) )
        {
            /*
             * GESTION DES PROFILS
             * -------------------
             * Si l'utilisateur n'est pas autorisé a effectuer cette fonction,
             * il est renvoyé vers une page lui indiquant qu'il n'a pas les droits nécessaires.
             */
            boolean bEstPublication = ( nIsPublier == 1 );
            boolean bAuthorized = true;

            if ( bEstPublication )
            {
                if ( isFonctionnaliteProjetDeDeliberation(  ) &&
                        !RBACService.isAuthorized( ProjetProchaineSeanceResourceIdService.PROJET_PROCHAINE_SEANCE,
                            RBAC.WILDCARD_RESOURCES_ID, ProjetProchaineSeanceResourceIdService.PERMISSION_PUBLICATION,
                            getUser(  ) ) )
                {
                    bAuthorized = false;
                }
                else if ( isGestionAval(  ) &&
                        !RBACService.isAuthorized( PddGestionAvalResourceIdService.PDD_GESTION_AVAL,
                            RBAC.WILDCARD_RESOURCES_ID, PddGestionAvalResourceIdService.PERMISSION_PUBLICATION,
                            getUser(  ) ) )
                {
                    bAuthorized = false;
                }
            }
            else
            {
                if ( isFonctionnaliteProjetDeDeliberation(  ) &&
                        !RBACService.isAuthorized( ProjetProchaineSeanceResourceIdService.PROJET_PROCHAINE_SEANCE,
                            RBAC.WILDCARD_RESOURCES_ID,
                            ProjetProchaineSeanceResourceIdService.PERMISSION_DEPUBLICATION, getUser(  ) ) )
                {
                    bAuthorized = false;
                }
                else if ( isGestionAval(  ) &&
                        !RBACService.isAuthorized( PddGestionAvalResourceIdService.PDD_GESTION_AVAL,
                            RBAC.WILDCARD_RESOURCES_ID, PddGestionAvalResourceIdService.PERMISSION_DEPUBLICATION,
                            getUser(  ) ) )
                {
                    bAuthorized = false;
                }
                else if ( !isFonctionnaliteProjetDeDeliberation(  ) &&
                        !RBACService.isAuthorized( 
                            PropositionProchaineSeanceResourceIdService.PROPOSITION_PROCHAINE_SEANCE,
                            RBAC.WILDCARD_RESOURCES_ID,
                            PropositionProchaineSeanceResourceIdService.PERMISSION_DEPUBLICATION, getUser(  ) ) )
                {
                    bAuthorized = false;
                }
            }

            if ( !bAuthorized )
            {
                template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ) );

                return getAdminPage( template.getHtml(  ) );
            }

            PDD pdd = PDDHome.findByPrimaryKey( nIdPDD, getPlugin(  ) );

            // Si le pdd est null c'est qu'il y a une erreur dans l'identifiant du PDD.
            // On renvoie l'utilisateur vers la liste des PDD.
            if ( pdd == null )
            {
                return getPDDList( request );
            }

            int newVersion = pdd.getVersion(  );

            /*
             * PROD-P1-CU03-RG15
             * Si le pdd est inscrit à l'ordre du jour,
             * on affiche un message de confirmation avant de le dépublier.
             */
            if ( !bEstPublication && ( strIsConfirmation == null ) )
            {
                

                EntreeOrdreDuJourFilter filter = new EntreeOrdreDuJourFilter(  );
                filter.setIdPdd( pdd.getIdPdd(  ) );
                filter.setIdSeance( seance.getIdSeance(  ) );
                filter.setIdPublie( 1 ); // 1 = Publié

                List<EntreeOrdreDuJour> listEntreesOdj = EntreeOrdreDuJourHome.findEntreesListByFilter( filter,
                        getPlugin(  ) );

                if ( ( listEntreesOdj != null ) && !listEntreesOdj.isEmpty(  ) )
                {
                    HashMap<Object, Object> model = new HashMap<Object, Object>(  );

                    model.put( OdsParameters.ID_PDD, pdd.getIdPdd(  ) );

                    template = AppTemplateService.getTemplate( TEMPLATE_CONFIRM_DEPUBLICATION_PDD, getLocale(  ), model );

                    return getAdminPage( template.getHtml(  ) );
                }
            }

            if ( bEstPublication )
            {
                newVersion++;
            }

            PDDHome.publication( pdd.getIdPdd(  ), dateForVersion, newVersion, bEstPublication, getPlugin(  ) );

            // on horodate la (de)publication
            HorodatageActionEnum action = (bEstPublication)?HorodatageActionEnum.PUBLICATION:HorodatageActionEnum.DE_PUBLICATION;
            HorodatageServices.tracePublicationPDD(pdd, seance, action, getPlugin(  ));
            
            
            if ( bEstPublication )
            {
                // on publie un historique pour le pdd
                Historique historique = new Historique(  );
                historique.setVersion( newVersion );
                historique.setDatePublication( dateForVersion );
                historique.setIdPDD( pdd.getIdPdd(  ) );
                HistoriqueHome.create( historique, getPlugin(  ) );

                // on publie les fichiers en pieces annexes
                FichierFilter fichierFilter = new FichierFilter(  );
                fichierFilter.setPDD( pdd.getIdPdd(  ) );

                List<Fichier> fichiers = FichierHome.findByFilter( fichierFilter, getPlugin(  ) );

                for ( int i = 0; ( fichiers != null ) && ( i < fichiers.size(  ) ); i++ )
                {
                    Fichier f = fichiers.get( i );

                    FichierHome.publication( f.getId(  ), dateForVersion, newVersion - 1,
                        ( nIsPublier == 1 ) ? true : false, getPlugin(  ) );

                    // on publie un historique pour la piece annexe annexe
                    Historique historiqueFichier = new Historique(  );
                    historiqueFichier.setVersion( newVersion );
                    historiqueFichier.setDatePublication( dateForVersion );
                    historiqueFichier.setIdDocument( f.getId(  ) );
                    HistoriqueHome.create( historiqueFichier, getPlugin(  ) );
                }
            }
        }

        return getPDDList( request );
    }

    /**
     * supprime le pdd choisi
     *
     * @param request
     *            la requête HTTP
     * @return String template
     */
    public String doSuppressionPDD( HttpServletRequest request )
    {
        int nIdPDD = OdsUtils.getIntegerParameter( OdsParameters.ID_PDD, request );
        PDD pdd = null;

        /*
         * GESTION DES PROFILS
         * -------------------
         * Si l'utilisateur n'est pas autorisé a effectuer cette fonction,
         * il est renvoyé vers une page lui indiquant qu'il n'a pas les droits nécessaires.
         */
        boolean bAuthorized = true;

        if ( isFonctionnaliteProjetDeDeliberation(  ) &&
                !RBACService.isAuthorized( ProjetProchaineSeanceResourceIdService.PROJET_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, ProjetProchaineSeanceResourceIdService.PERMISSION_SUPPRESSION,
                    getUser(  ) ) )
        {
            bAuthorized = false;
        }
        else if ( isGestionAval(  ) &&
                !RBACService.isAuthorized( PddGestionAvalResourceIdService.PDD_GESTION_AVAL,
                    RBAC.WILDCARD_RESOURCES_ID, PddGestionAvalResourceIdService.PERMISSION_SUPPRESSION, getUser(  ) ) )
        {
            bAuthorized = false;
        }

        pdd = PDDHome.findByPrimaryKey( nIdPDD, getPlugin(  ) );

        if ( pdd == null )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( !isFonctionnaliteProjetDeDeliberation(  ) && pdd.isEnLigne(  ) &&
                !RBACService.isAuthorized( PropositionProchaineSeanceResourceIdService.PROPOSITION_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, PropositionProchaineSeanceResourceIdService.PERMISSION_SUPPRESSION,
                    getUser(  ) ) )
        {
            bAuthorized = false;
        }

        if ( !bAuthorized )
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }

        try
        {
            EntreeOrdreDuJourFilter filterEntree = new EntreeOrdreDuJourFilter(  );
            filterEntree.setIdPdd( pdd.getIdPdd(  ) );

            List<EntreeOrdreDuJour> listEntreesOdj = EntreeOrdreDuJourHome.findEntreesListByFilter( filterEntree,
                    getPlugin(  ) );

            if ( ( listEntreesOdj != null ) && !listEntreesOdj.isEmpty(  ) )
            {
                for ( EntreeOrdreDuJour entree : listEntreesOdj )
                {
                    EntreeOrdreDuJourHome.remove( entree, getPlugin(  ) );
                }
            }

            // Suppression de toutes les directions co-emetrices existantes pour ce pdd
            PDDHome.deleteCoEmetrices( nIdPDD, getPlugin(  ) );

            // Suppression de toutes les arrondissements existants pour ce pdd
            PDDHome.deleteArrondissements( nIdPDD, getPlugin(  ) );

            // Suppression des fichiers du pdd
            FichierFilter filterFichier = new FichierFilter(  );
            filterFichier.setPDD( nIdPDD );

            List<Fichier> fichierToDelete = FichierHome.findByFilter( filterFichier, getPlugin(  ) );

            for ( int i = 0; ( fichierToDelete != null ) && ( i < fichierToDelete.size(  ) ); i++ )
            {
                Fichier f = fichierToDelete.get( i );
                HistoriqueHome.remove( f, getPlugin(  ) );
                FichierHome.remove( f, getPlugin(  ) );
                FichierPhysiqueHome.remove( f.getFichier(  ), getPlugin(  ) );
            }

            // Suppression de l'historique
            pdd.setIdPdd( nIdPDD );
            HistoriqueHome.remove( pdd, getPlugin(  ) );

            // Suppression des Elements de relevés de travaux concernant ce PDD
            ElementReleveDesTravauxHome.remove( pdd, getPlugin(  ) );

            // Suppression du PDD
            PDDHome.remove( pdd, getPlugin(  ) );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }
        catch ( AppException ae )
        {
            if ( ae.getInitialException(  ) instanceof SQLException )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_PDD, AdminMessage.TYPE_STOP );
            }
        }

        return ( AppPathService.getBaseUrl( request ) + TEMPLATE_JSP_LISTE + "?" + OdsParameters.PLUGIN_NAME + "&" +
        OdsParameters.ID_PDD + "=" + nIdPDD );
    }

    /**
     * supprime la piece annexe choisie
     *
     * @param request
     *            la requête HTTP
     * @return String template
     */
    public String doSuppressionPieceAnnexe( HttpServletRequest request )
    {
        int nIdPDD = OdsUtils.getIntegerParameter( OdsParameters.ID_PDD, request );
        int nIdFichier = OdsUtils.getIntegerParameter( OdsParameters.ID_FICHIER, request );

        if ( ( nIdPDD != -1 ) && ( nIdFichier != -1 ) )
        {
            Fichier fichier = FichierHome.findByPrimaryKey( nIdFichier, getPlugin(  ) );
            FichierPhysique fichierPhysique = FichierPhysiqueHome.findByPrimaryKey( fichier.getFichier(  ).getIdFichier(  ),
                    getPlugin(  ) );

            HistoriqueHome.remove( fichier, getPlugin(  ) );
            FichierHome.remove( fichier, getPlugin(  ) );
            FichierPhysiqueHome.remove( fichierPhysique, getPlugin(  ) );

            PDD pdd = PDDHome.findByPrimaryKey( nIdPDD, getPlugin( ) );
            if ( pdd.isEnLigne( ) )
            {
                Timestamp dateForVersion = new Timestamp( System.currentTimeMillis(  ) );
                int newVersion = pdd.getVersion() + 1;
                
            	// On incremente le numéro de version du PDD
            	pdd.setVersion( newVersion );
            	pdd.setDatePublication( dateForVersion );
            	PDDHome.update( pdd, getPlugin( ) );

                // Ajout de l'historique du PDD
                Historique historiquePDD = new Historique(  );
                historiquePDD.setVersion( newVersion );
                historiquePDD.setDatePublication( dateForVersion );
                historiquePDD.setIdPDD( pdd.getIdPdd(  ) );
                HistoriqueHome.create( historiquePDD, getPlugin(  ) );
            }

            return ( AppPathService.getBaseUrl( request ) + TEMPLATE_JSP_MODIFICATION_COMPLETE + "?" +
            OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_PDD + "=" + nIdPDD );
        }
        else
        {
            return getHomeUrl( request );
        }
    }

    /**
     * permet de reinitialise les directions coemetrices et les arrondissements
     * qui sont stockés en session
     *
     */
    private void resetCreation(  )
    {
        _directionCoEmetriceAdded = null;
        _arrondissementAdded = null;
    }

    /**
     * Permet de changer l'ordre des voeux Amandements rattachés
     *
     * @param isForAmandement
     *            TRUE pour traiter les amendements, FALSE pour les voeux
     *            rattachés
     * @param request
     *            la requête HTTP
     * @return String template
     */
    public String doChangeNumOrdreVoeuxAmendements( HttpServletRequest request, boolean isForAmandement )
    {
        int nIdPDD = OdsUtils.getIntegerParameter( OdsParameters.ID_PDD, request );
        int nIndex = OdsUtils.getIntegerParameter( MARK_INDEX_VA, request );
        String strIdVA = request.getParameter( OdsParameters.ID_VOEUAMENDEMENT );
        String strDir = request.getParameter( MARK_DIR );

        String strSeance = null;

        if ( isGestionAval(  ) )
        {
            strSeance = request.getParameter( OdsParameters.ID_SEANCE );
        }

        if ( ( nIdPDD != -1 ) && ( strIdVA != null ) && ( nIndex != -1 ) && ( strDir != null ) )
        {
            PDD pdd = PDDHome.findByPrimaryKey( nIdPDD, getPlugin(  ) );

            List<VoeuAmendement> oldAmendements = ( isForAmandement ) ? pdd.getAmendements(  ) : pdd.getVoeuxRattaches(  );
            List<VoeuAmendement> newAmendements = new ArrayList<VoeuAmendement>(  );

            if ( strDir.equals( CONSTANTE_REMOVE ) )
            {
                newAmendements.addAll( oldAmendements );
                newAmendements.remove( nIndex );
            }
            else
            {
                int nbAmendement = oldAmendements.size(  );

                if ( ( nIndex == 0 ) && strDir.equals( CONSTANTE_UP ) )
                {
                    // cas ou le premier element passe dernier
                    for ( int i = 1; i < nbAmendement; i++ )
                    {
                        newAmendements.add( oldAmendements.get( i ) );
                    }

                    newAmendements.add( oldAmendements.get( 0 ) );
                }
                else if ( ( nIndex == ( nbAmendement - 1 ) ) && !strDir.equals( CONSTANTE_UP ) )
                { // cas ou le dernier
                  // element passe premier
                    newAmendements.add( oldAmendements.get( nbAmendement - 1 ) );

                    for ( int i = 0; i < ( nbAmendement - 1 ); i++ )
                    {
                        newAmendements.add( oldAmendements.get( i ) );
                    }
                }
                else
                {
                    newAmendements.addAll( oldAmendements );

                    if ( strDir.equals( CONSTANTE_UP ) )
                    { // cas ou un element central
                      // monte

                        VoeuAmendement tmp = newAmendements.get( nIndex - 1 );
                        newAmendements.set( nIndex - 1, newAmendements.get( nIndex ) );
                        newAmendements.set( nIndex, tmp );
                    }
                    else
                    { // cas ou un element central descend

                        VoeuAmendement tmp = newAmendements.get( nIndex + 1 );
                        newAmendements.set( nIndex + 1, newAmendements.get( nIndex ) );
                        newAmendements.set( nIndex, tmp );
                    }
                }
            }

            PDDHome.updateVoeuxAmendement( oldAmendements, newAmendements, nIdPDD, getPlugin(  ) );

            if ( isGestionAval(  ) )
            {
                return AppPathService.getBaseUrl( request ) + JSP_URL_MODIFICATION_PDD_GESTION_AVAL + "?" +
                OdsParameters.ID_PDD + "=" + nIdPDD + "&" + OdsParameters.ID_SEANCE + "=" + strSeance;
            }

            return ( AppPathService.getBaseUrl( request ) + TEMPLATE_JSP_MODIFICATION_COMPLETE + "?" +
            OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_PDD + "=" + nIdPDD );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, Messages.PATH_CREATION_ERROR, AdminMessage.TYPE_STOP );
        }
    }

    /**
     * Permet d'ajouter une direction coemetrice au pdd qui est en cours de
     * creation ou de modification
     *
     * @param request
     *            la requête HTTP
     * @return String template
     */
    public String doAjoutDirectionCoEmetrice( HttpServletRequest request )
    {
        String strPDDEcran = request.getParameter( MARK_PDD_ECRAN );

        if ( MARK_PDD_ECRAN_MODIFICATION.equals( strPDDEcran ) )
        {
            setPageTitleProperty( ( isFonctionnaliteProjetDeDeliberation(  ) ) ? PROPERTY_PAGE_MODIFICATION_PJ
                                                                               : PROPERTY_PAGE_MODIFICATION_PP );
        }
        else
        {
            setPageTitleProperty( ( isFonctionnaliteProjetDeDeliberation(  ) ) ? PROPERTY_PAGE_CREATION_PJ
                                                                               : PROPERTY_PAGE_CREATION_PP );
        }

        // Recuperation des donnees du formulaire
        String strAnnee = request.getParameter( OdsParameters.PDD_ANNEE );
        String strNumero = request.getParameter( OdsParameters.PDD_NUMERO );
        String strObjet = request.getParameter( OdsParameters.PDD_OBJET );
        String strPieceManuel = request.getParameter( OdsParameters.PDD_PIECE_MANUEL );
        String strDelegationServicePublic = request.getParameter( OdsParameters.PDD_DELEG_SERVICE_PUBLIC );
        String strIdDirectionCoEmetrice = request.getParameter( OdsParameters.ID_DIRECTION_CO_EMETRICE );
        String strPddRefCoEmetrice = request.getParameter( OdsParameters.PDD_REF_CO_EMETRICE );

        if ( !OdsConstants.FILTER_ALL.equals( strIdDirectionCoEmetrice ) )
        {
            ajoutDirectionCoEmetrice( strIdDirectionCoEmetrice, strPddRefCoEmetrice );
        }

        return returnEcran( request, strPDDEcran, strAnnee, strNumero, strObjet, strPieceManuel,
            strDelegationServicePublic );
    }

    /**
     * Permet de supprimer une direction coemetrice au pdd qui est en cours de
     * creation ou de modification
     *
     * @param request
     *            la requête HTTP
     * @return String template
     */
    public String doDeleteDirectionCoEmetrice( HttpServletRequest request )
    {
        String strPDDEcran = request.getParameter( MARK_PDD_ECRAN );

        if ( MARK_PDD_ECRAN_MODIFICATION.equals( strPDDEcran ) )
        {
            setPageTitleProperty( ( isFonctionnaliteProjetDeDeliberation(  ) ) ? PROPERTY_PAGE_MODIFICATION_PJ
                                                                               : PROPERTY_PAGE_MODIFICATION_PP );
        }
        else
        {
            setPageTitleProperty( ( isFonctionnaliteProjetDeDeliberation(  ) ) ? PROPERTY_PAGE_CREATION_PJ
                                                                               : PROPERTY_PAGE_CREATION_PP );
        }

        // Recuperation des donnees du formulaire
        String strAnnee = request.getParameter( OdsParameters.PDD_ANNEE );
        String strNumero = request.getParameter( OdsParameters.PDD_NUMERO );
        String strObjet = request.getParameter( OdsParameters.PDD_OBJET );
        String strPieceManuel = request.getParameter( OdsParameters.PDD_PIECE_MANUEL );
        String strDelegationServicePublic = request.getParameter( OdsParameters.PDD_DELEG_SERVICE_PUBLIC );
        String strCodeDirectionCoEmetriceAdded = request.getParameter( OdsParameters.PDD_CODE_DIRECTION_CO_EMETRICE_ADDED );

        if ( strCodeDirectionCoEmetriceAdded != null )
        {
            deleteDirectionCoEmetrice( strCodeDirectionCoEmetriceAdded );
        }

        return returnEcran( request, strPDDEcran, strAnnee, strNumero, strObjet, strPieceManuel,
            strDelegationServicePublic );
    }

    /**
     * Supprime la direction coemetrice dont l'id est passé en paramètre
     *
     * @param strCodeDirectionCoEmetriceAdded
     *            String
     */
    private void deleteDirectionCoEmetrice( String strCodeDirectionCoEmetriceAdded )
    {
        DirectionCoEmetrice coEmetriceToDelete = null;

        for ( int i = 0; ( _directionCoEmetriceAdded != null ) && ( i < _directionCoEmetriceAdded.size(  ) ); i++ )
        {
            DirectionCoEmetrice coEmetrice = _directionCoEmetriceAdded.get( i );

            if ( strCodeDirectionCoEmetriceAdded.equals( coEmetrice.getDirection(  ).getCode(  ) ) )
            {
                coEmetriceToDelete = coEmetrice;
            }
        }

        if ( coEmetriceToDelete != null )
        {
            _directionCoEmetriceAdded.remove( coEmetriceToDelete );
        }
    }

    /**
     * Ajout de la direction coemetrice a la liste en cours
     *
     * @param strIdDirectionCoEmetrice
     *            String
     * @param strPddRefCoEmetrice
     *            String
     */
    private void ajoutDirectionCoEmetrice( String strIdDirectionCoEmetrice, String strPddRefCoEmetrice )
    {
        if ( _directionCoEmetriceAdded == null )
        {
            _directionCoEmetriceAdded = new ArrayList<DirectionCoEmetrice>(  );
        }

        DirectionCoEmetrice newCoEmetrice = new DirectionCoEmetrice(  );
        newCoEmetrice.setDirection( getDirectionObject( strIdDirectionCoEmetrice ) );
        newCoEmetrice.setCodeProjet( OdsConstants.CONSTANTE_CHAINE_VIDE + strPddRefCoEmetrice );

        boolean flagIsFound = false;

        for ( DirectionCoEmetrice c : _directionCoEmetriceAdded )
        {
            if ( ( c.getDirection(  ) != null ) &&
                    ( c.getDirection(  ).getIdDirection(  ) == newCoEmetrice.getDirection(  ).getIdDirection(  ) ) )
            {
                flagIsFound = true;

                break;
            }
        }

        if ( !flagIsFound )
        {
            _directionCoEmetriceAdded.add( newCoEmetrice );
        }
    }

    /**
     * Permet d'ajouter un arrondissement au pdd qui est en cours de creation ou
     * de modification
     *
     * @param request
     *            la requête HTTP
     * @return String template
     */
    public String doAjoutArrondissement( HttpServletRequest request )
    {
        String strPDDEcran = request.getParameter( MARK_PDD_ECRAN );

        if ( MARK_PDD_ECRAN_MODIFICATION.equals( strPDDEcran ) )
        {
            setPageTitleProperty( ( isFonctionnaliteProjetDeDeliberation(  ) ) ? PROPERTY_PAGE_MODIFICATION_PJ
                                                                               : PROPERTY_PAGE_MODIFICATION_PP );
        }
        else
        {
            setPageTitleProperty( ( isFonctionnaliteProjetDeDeliberation(  ) ) ? PROPERTY_PAGE_CREATION_PJ
                                                                               : PROPERTY_PAGE_CREATION_PP );
        }

        // Recuperation des donnees du formulaire
        String strAnnee = request.getParameter( OdsParameters.PDD_ANNEE );
        String strNumero = request.getParameter( OdsParameters.PDD_NUMERO );
        String strObjet = request.getParameter( OdsParameters.PDD_OBJET );
        String strPieceManuel = request.getParameter( OdsParameters.PDD_PIECE_MANUEL );
        String strDelegationServicePublic = request.getParameter( OdsParameters.PDD_DELEG_SERVICE_PUBLIC );
        String strIdArrondissement = request.getParameter( OdsParameters.ID_ARRONDISSEMENT );

        if ( ( strIdArrondissement != null ) && !OdsConstants.FILTER_ALL.equals( strIdArrondissement ) )
        {
            ajoutArrondissement( strIdArrondissement );
        }

        return returnEcran( request, strPDDEcran, strAnnee, strNumero, strObjet, strPieceManuel,
            strDelegationServicePublic );
    }

    /**
     * Permet de supprimer un arrondissement au pdd qui est en cours de creation
     * ou de modification
     *
     * @param request
     *            la requête HTTP
     * @return String template
     */
    public String doDeleteArrondissement( HttpServletRequest request )
    {
        String strPDDEcran = request.getParameter( MARK_PDD_ECRAN );

        if ( MARK_PDD_ECRAN_MODIFICATION.equals( strPDDEcran ) )
        {
            setPageTitleProperty( ( isFonctionnaliteProjetDeDeliberation(  ) ) ? PROPERTY_PAGE_MODIFICATION_PJ
                                                                               : PROPERTY_PAGE_MODIFICATION_PP );
        }
        else
        {
            setPageTitleProperty( ( isFonctionnaliteProjetDeDeliberation(  ) ) ? PROPERTY_PAGE_CREATION_PJ
                                                                               : PROPERTY_PAGE_CREATION_PP );
        }

        // Recuperation des donnees du formulaire
        String strAnnee = request.getParameter( OdsParameters.PDD_ANNEE );
        String strNumero = request.getParameter( OdsParameters.PDD_NUMERO );
        String strObjet = request.getParameter( OdsParameters.PDD_OBJET );
        String strPieceManuel = request.getParameter( OdsParameters.PDD_PIECE_MANUEL );
        String strDelegationServicePublic = request.getParameter( OdsParameters.PDD_DELEG_SERVICE_PUBLIC );
        String strArrondissementAdded = request.getParameter( OdsParameters.ID_ARRONDISSEMENT_ADDED );

        if ( strArrondissementAdded != null )
        {
            deleteArrondissement( strArrondissementAdded );
        }

        return returnEcran( request, strPDDEcran, strAnnee, strNumero, strObjet, strPieceManuel,
            strDelegationServicePublic );
    }

    /**
     * Permet de supprimer l'arrondissement passé en parametre de la liste en
     * cours
     *
     * @param strArrondissementAdded
     *            String
     */
    private void deleteArrondissement( String strArrondissementAdded )
    {
        Arrondissement arrondissementDelete = null;

        for ( int i = 0; ( _arrondissementAdded != null ) && ( i < _arrondissementAdded.size(  ) ); i++ )
        {
            Arrondissement arrondissement = _arrondissementAdded.get( i );

            if ( strArrondissementAdded.equals( OdsConstants.CONSTANTE_CHAINE_VIDE +
                        arrondissement.getArrondissement(  ) ) )
            {
                arrondissementDelete = arrondissement;
            }
        }

        if ( arrondissementDelete != null )
        {
            _arrondissementAdded.remove( arrondissementDelete );
        }
    }

    /**
     * Ajoute un arrondissement a la liste en cours
     *
     * @param strIdArrondissement
     *            String
     */
    private void ajoutArrondissement( String strIdArrondissement )
    {
        if ( _arrondissementAdded == null )
        {
            _arrondissementAdded = new ArrayList<Arrondissement>(  );
        }

        Arrondissement newArrondissement = new Arrondissement(  );
        int nIdArrondissement = -1;

        try
        {
            nIdArrondissement = Integer.parseInt( strIdArrondissement );
            newArrondissement.setArrondissement( nIdArrondissement );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        if ( ( _arrondissementAdded != null ) && !_arrondissementAdded.contains( newArrondissement ) )
        {
            _arrondissementAdded.add( newArrondissement );
        }
    }

    /**
     * Retourne l'ecran sur lequel on effectue les modifications Si strPDDEcran =
     * MARK_PDD_ECRAN_MODIFICATION alors on revient sur l'ecran de modification
     * Si strPDDEcran = MARK_PDD_ECRAN_CREATION alors on revient sur l'ecran de
     * creation
     *
     * La methode passe alors tous les paramètres nécessaire aux ecrans sur
     * lequel on est redirigé
     *
     * @param strPDDEcran identifiant du pdd donné par el formulaire
     * @param strAnnee année du pdd donnée par le formulaire
     * @param strNumero numero du pdd donné par le formulaire
     * @param strObjet objet du pdd donné par le formulaire
     * @param strPieceManuel piece manuelle donné par le formulaire
     * @param strDelegationServicePublic delegation de service public donné par le forùulaire
     * @param request la requête HTTP
     * @return String template
     */
    private String returnEcran( HttpServletRequest request, String strPDDEcran, String strAnnee, String strNumero,
        String strObjet, String strPieceManuel, String strDelegationServicePublic )
    {
        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            initCreationDirectionCoEmetrice( request );
        }

        initCreationArrondissement( request );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );
        model.put( Seance.MARK_PROCHAINE_SEANCE, _seance );

        int nIdCategorieDeliberationSelected = OdsUtils.getIntegerParameter( OdsParameters.ID_CATEGORIE, request );
        int nIdFormationConseilSelected = OdsUtils.getIntegerParameter( OdsParameters.ID_FORMATION_CONSEIL, request );
        String strModeIntroSelected = request.getParameter( OdsParameters.ID_PDD_MODE_INTRO );

        model.put( MARK_FONCTIONNALITE_IS_PROJET, isFonctionnaliteProjetDeDeliberation(  ) );
        model.put( MARK_LISTE_FORMATION_CONSEIL, _referenceFormationConseilList );
        model.put( MARK_ID_FORMATION_CONSEIL_SELECTED, nIdFormationConseilSelected );
        model.put( MARK_LISTE_CATEGORIE_DELIBERATION, _referenceCategorieDeliberationList );
        model.put( MARK_ID_CATEGORIE_DELIBERATION_SELECTED, nIdCategorieDeliberationSelected );

        String strDirectionSelected = null;
        String strGroupeDepositaireSelected = null;

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            strDirectionSelected = request.getParameter( OdsParameters.ID_DIRECTION );
            model.put( MARK_LISTE_DIRECTION, _referenceDirectionList );
            model.put( MARK_ID_DIRECTION_SELECTED, strDirectionSelected );
        }
        else
        {
            strGroupeDepositaireSelected = request.getParameter( OdsParameters.ID_GROUPE );
            model.put( MARK_LISTE_GROUPE_DEPOSITAIRE, _referenceGroupeDepositaireList );
            model.put( MARK_ID_GROUPE_DEPOSITAIRE_SELECTED, strGroupeDepositaireSelected );

            //model.put( MARK_LISTE_PUBLICATION, _referencePublicationList );
            //model.put( MARK_ID_PUBLICATION_SELECTED, _nIdPublicationSelected );
        }

        model.put( MARK_LISTE_MODE_INTRO, _referenceModeIntroList );
        model.put( MARK_ID_MODE_INTRO_SELECTED, strModeIntroSelected );
        model.put( MARK_LISTE_DIRECTION_CO_EMETRICE, _referenceDirectionCoEmetriceSelect );
        model.put( MARK_ID_DIRECTION_CO_EMETRICE_SELECTED, _strDirectionCoEmetriceSelected );
        model.put( MARK_LISTE_ARRONDISSEMENT, _referenceArrondissementSelect );
        model.put( MARK_ID_ARRONDISSEMENT_SELECTED, _nIdArrondissementSelected );

        ajouterPermissionsDansHashmap( model );

        if ( isFonctionnaliteProjetDeDeliberation(  ) && ( _directionCoEmetriceAdded != null ) &&
                ( _directionCoEmetriceAdded.size(  ) > 0 ) )
        {
            model.put( MARK_LISTE_DIRECTION_CO_EMETRICE_ADDED, _directionCoEmetriceAdded );
        }

        if ( ( _arrondissementAdded != null ) && ( _arrondissementAdded.size(  ) > 0 ) )
        {
            model.put( MARK_LISTE_ARRONDISSEMENT_ADDED, _arrondissementAdded );
        }

        boolean bPieceManuel = ( strPieceManuel != null );
        boolean bDelegationServicePublic = ( strDelegationServicePublic != null );

        if ( strPDDEcran.equals( MARK_PDD_ECRAN_MODIFICATION ) )
        {
            /*
             * Permission de modifier tous les champs du formulaire
             */
            boolean bPermissionModification = true;

            if ( !isGestionAval(  ) && isFonctionnaliteProjetDeDeliberation(  ) &&
                    !RBACService.isAuthorized( ProjetProchaineSeanceResourceIdService.PROJET_PROCHAINE_SEANCE,
                        RBAC.WILDCARD_RESOURCES_ID, ProjetProchaineSeanceResourceIdService.PERMISSION_MAJ_COMPLETE,
                        getUser(  ) ) )
            {
                bPermissionModification = false;
            }
            else if ( !isGestionAval(  ) && !isFonctionnaliteProjetDeDeliberation(  ) &&
                    !RBACService.isAuthorized( 
                        PropositionProchaineSeanceResourceIdService.PROPOSITION_PROCHAINE_SEANCE,
                        RBAC.WILDCARD_RESOURCES_ID, PropositionProchaineSeanceResourceIdService.PERMISSION_MISE_A_JOUR,
                        getUser(  ) ) )
            {
                bPermissionModification = false;
            }
            else if ( isGestionAval(  ) &&
                    !RBACService.isAuthorized( PddGestionAvalResourceIdService.PDD_GESTION_AVAL,
                        RBAC.WILDCARD_RESOURCES_ID, PddGestionAvalResourceIdService.PERMISSION_MAJ_COMPLETE, getUser(  ) ) )
            {
                bPermissionModification = false;
            }

            model.put( OdsMarks.MARK_PERMISSION_MODIFICATION, bPermissionModification );

            int nPddPrimaryKey = OdsUtils.getIntegerParameter( OdsParameters.ID_PDD, request );

            PDD pdd = PDDHome.findByPrimaryKey( nPddPrimaryKey, getPlugin(  ) );

            FichierFilter fichierFilter = new FichierFilter(  );
            fichierFilter.setPDD( nPddPrimaryKey );

            List<Fichier> fichiers = FichierHome.findByFilter( fichierFilter, getPlugin(  ) );
            Fichier fichierExposeDesMotifs = null;
            Fichier fichierProjetDeDelibere = null;
            List<Fichier> piecesAnnexes = new ArrayList<Fichier>(  );

            // On affecte à la valeur année la plus récente entre celle en base 
            // et celle envoyée par le formulaire 
            if ( ( strAnnee != null ) && !pdd.getAnnee(  ).equals( strAnnee.trim(  ) ) )
            {
                pdd.setReference( strAnnee.trim(  ) + strDirectionSelected + pdd.getNumero(  ) );
            }

            // On affecte à la valeur Numero la plus récente entre celle en base 
            // et celle envoyée par le formulaire 
            if ( ( strNumero != null ) && !pdd.getNumero(  ).trim(  ).equals( strNumero.trim(  ) ) )
            {
                pdd.setReference( pdd.getAnnee(  ) + " " + pdd.getDirection(  ).getCode(  ) + " " + strNumero.trim(  ) );
            }

            // On affecte à la valeur Objet la plus récente entre celle en base 
            // et celle envoyée par le formulaire 
            if ( ( strObjet != null ) && !pdd.getObjet(  ).equals( strObjet.trim(  ) ) )
            {
                pdd.setObjet( strObjet );
            }

            // si la valeur de délégation de services a changé on affecte le changement
            if ( pdd.isDelegationsServices(  ) != bDelegationServicePublic )
            {
                pdd.setDelegationsServices( bDelegationServicePublic );
            }

            // si la valeur de pieces manuelles a changé on affecte le changement
            if ( pdd.isPiecesManuelles(  ) != bPieceManuel )
            {
                pdd.setPiecesManuelles( bPieceManuel );
            }

            for ( int i = 0; ( fichiers != null ) && ( i < fichiers.size(  ) ); i++ )
            {
                Fichier f = fichiers.get( i );

                if ( ( f.getTypdeDocument(  ) != null ) &&
                        ( TypeDocumentEnum.EXPOSE_DES_MOTIFS.getId(  ) == f.getTypdeDocument(  ).getId(  ) ) )
                {
                    fichierExposeDesMotifs = f;
                }

                if ( ( f.getTypdeDocument(  ) != null ) &&
                        ( TypeDocumentEnum.PROJET_DE_DELIBERE.getId(  ) == f.getTypdeDocument(  ).getId(  ) ) )
                {
                    fichierProjetDeDelibere = f;
                }

                if ( ( f.getTypdeDocument(  ) != null ) &&
                        ( TypeDocumentEnum.PIECE_ANNEXE.getId(  ) == f.getTypdeDocument(  ).getId(  ) ) )
                {
                    piecesAnnexes.add( f );
                }
            }

            model.put( OdsParameters.ID_PDD, "" + nPddPrimaryKey );

            if ( fichierExposeDesMotifs != null )
            {
                model.put( MARK_FICHIER_EXPOSE_DES_MOTIFS, fichierExposeDesMotifs );
            }

            if ( fichierProjetDeDelibere != null )
            {
                model.put( MARK_FICHIER_PROJET_DE_DELIBERE, fichierProjetDeDelibere );
            }

            if ( ( piecesAnnexes != null ) && ( piecesAnnexes.size(  ) > 0 ) )
            {
                model.put( MARK_PIECES_ANNEXES, piecesAnnexes );
            }

            if ( ( pdd.getAmendements(  ) != null ) && ( pdd.getAmendements(  ).size(  ) > 0 ) )
            {
                model.put( MARK_LISTE_AMENDEMENTS, pdd.getAmendements(  ) );
            }

            if ( ( pdd.getVoeuxRattaches(  ) != null ) && ( pdd.getVoeuxRattaches(  ).size(  ) > 0 ) )
            {
                model.put( MARK_LISTE_VOEUX_RATTACHES, pdd.getVoeuxRattaches(  ) );
            }

            model.put( PDD.MARK_PDD, pdd );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_COMPLETE_PDD, getLocale(  ),
                    model );

            return getAdminPage( template.getHtml(  ) );
        }
        else
        {
            //initFilter( request, false );
            HtmlTemplate template = null;

            /*
             * GESTION DES PROFILS
             * -------------------
             * Si l'utilisateur n'est pas autorisé a effectuer cette fonction,
             * il est renvoyé vers une page lui indiquant qu'il n'a pas les droits nécessaires.
             */
            if ( isFonctionnaliteProjetDeDeliberation(  ) &&
                    !RBACService.isAuthorized( ProjetProchaineSeanceResourceIdService.PROJET_PROCHAINE_SEANCE,
                        RBAC.WILDCARD_RESOURCES_ID, ProjetProchaineSeanceResourceIdService.PERMISSION_CREATION,
                        getUser(  ) ) )
            {
                template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );

                return getAdminPage( template.getHtml(  ) );
            }

            model.put( OdsParameters.PDD_ANNEE, strAnnee );
            model.put( OdsParameters.PDD_NUMERO, strNumero );
            model.put( OdsParameters.PDD_OBJET, strObjet );
            model.put( OdsParameters.PDD_DELEG_SERVICE_PUBLIC, bDelegationServicePublic );
            model.put( OdsParameters.PDD_PIECE_MANUEL, bPieceManuel );

            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_PDD, getLocale(  ), model );

            return getAdminPage( template.getHtml(  ) );
        }
    }

    /**
     * Methode qui effectue les modifications Si retourList = TRUE alors on
     * enregistre les données et on revient sur la liste des pdd Si retourList =
     * FALSE alors on enregistre les données mais on reste sur l'ecran de
     * modification
     *
     * @param request la requête HTTP
     * @return String template
     */
    public String doModificationPDD( HttpServletRequest request )
    {
        boolean retourList = false;
        String strUrl = null;
        boolean bHasChanged = false;

        if ( ( request.getParameter( OdsParameters.A_FAIRE ) == null ) ||
                request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_ACTION_CANCEL ) )
        {
            resetCreation(  );

            strUrl = AppPathService.getBaseUrl( request ) + TEMPLATE_JSP_LISTE;

            if ( _bUrlRetour )
            {
                strUrl = AppPathService.getBaseUrl( request ) + _strUrlRetour;

                _bUrlRetour = false;
                _strUrlRetour = null;
            }

            return strUrl;
        }
        else if ( request.getParameter( OdsParameters.A_FAIRE ).trim(  ).equals( CONSTANTE_ACTION_APPLY ) )
        {
            retourList = false;
        }
        else
        {
            retourList = true;
        }

        // Récupération de l'id du PDD
        int nIdPDD = OdsUtils.getIntegerParameter( OdsParameters.ID_PDD, request );

        // Recuperation du projet de délibération
        PDD pddUpdated = null;

        if ( nIdPDD > 0 )
        {
            pddUpdated = PDDHome.findByPrimaryKey( nIdPDD, getPlugin(  ) );
        }
        else
        {
            strUrl = AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        if ( strUrl != null )
        {
            return strUrl;
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        if ( isGestionAval(  ) )
        {
            FileItem fileDelib;
            String strDateVote = request.getParameter( OdsParameters.DATE_VOTE );
            String strDateRetourControleLegalite = request.getParameter( OdsParameters.DATE_RETOUR_CONTROLE_LEGALITE );

            fileDelib = multipartRequest.getFile( OdsParameters.FICHIER_DELIBERATION );

            // Récupération du statut
            int nStatut = OdsUtils.getIntegerParameter( OdsParameters.ID_STATUT, request );

            if ( nStatut != -1 )
            {
                Statut statut = StatutHome.findByPrimaryKey( nStatut, getPlugin(  ) );
                pddUpdated.setStatut( statut );
            }
            else
            {
                pddUpdated.setStatut( null );
            }

            DateFormat dateFormat = null;
            dateFormat = new SimpleDateFormat( OdsConstants.CONSTANTE_PATTERN_DATE );
            dateFormat.setLenient( false );

            // Récupération de la date du vote
            if ( ( strDateVote != null ) && !strDateVote.trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                Timestamp timeStampDateVote;

                try
                {
                    timeStampDateVote = new Timestamp( dateFormat.parse( strDateVote ).getTime(  ) );
                    // if ( timeStampDateVote != null )
                    pddUpdated.setDateVote( timeStampDateVote );
                }
                catch ( ParseException pe )
                {
                    AppLogService.error( pe );
                }
            }

            // Récupération de la date de retour de controle de légalité
            if ( ( strDateRetourControleLegalite != null ) &&
                    !strDateRetourControleLegalite.trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                Timestamp timeStampDateRetourControleLegalite;

                try
                {
                    timeStampDateRetourControleLegalite = new Timestamp( dateFormat.parse( 
                                strDateRetourControleLegalite ).getTime(  ) );
                    // if ( timeStampDateRetourControleLegalite != null )
                    pddUpdated.setDateRetourCtrlLegalite( timeStampDateRetourControleLegalite );
                }
                catch ( ParseException pe )
                {
                    AppLogService.error( pe );
                }
            }

            if ( isValideFile( fileDelib ) )
            {
                // Récupération du fichier de délibération
                FichierFilter fichierDeliberationFilter = new FichierFilter(  );
                fichierDeliberationFilter.setPDD( nIdPDD );
                fichierDeliberationFilter.setIdTypeDocument( TypeDocumentEnum.DELIBERATION.getId(  ) );

                List<Fichier> listFichiersDeliberation = FichierHome.findByFilter( fichierDeliberationFilter,
                        getPlugin(  ) );
                Fichier fichierDeliberation = null;

                if ( ( listFichiersDeliberation != null ) && ( listFichiersDeliberation.size(  ) > 0 ) )
                {
                    fichierDeliberation = listFichiersDeliberation.get( 0 );
                }

                if ( fichierDeliberation == null )
                {
                    createFichier( pddUpdated, fileDelib, TypeDocumentEnum.DELIBERATION );
                }
                else
                {
                    updateFichier( fichierDeliberation, fileDelib );
                }
            }

            PDDHome.update( pddUpdated, getPlugin(  ) );

            if ( !RBACService.isAuthorized( PropositionProchaineSeanceResourceIdService.PROPOSITION_PROCHAINE_SEANCE,
                        RBAC.WILDCARD_RESOURCES_ID, PropositionProchaineSeanceResourceIdService.PERMISSION_MISE_A_JOUR,
                        getUser(  ) ) )
            {
                String strSeance = request.getParameter( OdsParameters.ID_SEANCE );

                if ( retourList )
                {
                    if ( _bUrlRetour )
                    {
                        strUrl = AppPathService.getBaseUrl( request ) + _strUrlRetour;

                        _bUrlRetour = false;
                        _strUrlRetour = null;
                    }
                    else
                    {
                        strUrl = getHomeUrl( request );
                    }
                }
                else
                {
                    strUrl = AppPathService.getBaseUrl( request ) + JSP_URL_MODIFICATION_PDD_GESTION_AVAL + "?" +
                        OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_PDD + "=" + nIdPDD + "&" +
                        OdsParameters.ID_SEANCE + "=" + strSeance;
                }

                return strUrl;
            }
        }

        if ( !isGestionAval(  ) && ( isFonctionnaliteProjetDeDeliberation(  ) || pddUpdated.isEnLigne(  ) ) &&
                !RBACService.isAuthorized( PropositionProchaineSeanceResourceIdService.PROPOSITION_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, PropositionProchaineSeanceResourceIdService.PERMISSION_MISE_A_JOUR,
                    getUser(  ) ) )
        {
            int nIdCategorieDeliberation = OdsUtils.getIntegerParameter( OdsParameters.ID_CATEGORIE, request );
            boolean bError = false;

            if ( nIdCategorieDeliberation != -1 )
            {
                CategorieDeliberation catdelib = CategorieDeliberationHome.findByPrimaryKey( nIdCategorieDeliberation,
                        getPlugin(  ) );
                pddUpdated.setCategorieDeliberation( catdelib );
            }
            else
            {
                bError = true;
            }

            if ( bError )
            {
                Object[] messagesArgs = { I18nService.getLocalizedString( PROPERTY_LABEL_CATEGORIE_DELIB, getLocale(  ) ) };

                return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                    messagesArgs, AdminMessage.TYPE_STOP );
            }

            PDDHome.update( pddUpdated, getPlugin(  ) );

            // Suppression de toutes les arrondissements existants pour ce pdd
            PDDHome.deleteArrondissements( pddUpdated.getIdPdd(  ), getPlugin(  ) );

            // Création des arrondissements
            for ( int i = 0; ( _arrondissementAdded != null ) && ( i < _arrondissementAdded.size(  ) ); i++ )
            {
                Arrondissement arrondissement = _arrondissementAdded.get( i );
                arrondissement.setIdPDD( pddUpdated.getIdPdd(  ) );
                PDDHome.create( arrondissement, getPlugin(  ) );
            }

            if ( retourList )
            {
                strUrl = AppPathService.getBaseUrl( request ) + TEMPLATE_JSP_LISTE;

                if ( _bUrlRetour )
                {
                    strUrl = AppPathService.getBaseUrl( request ) + _strUrlRetour;

                    _bUrlRetour = false;
                    _strUrlRetour = null;
                }
            }
            else
            {
                strUrl = ( AppPathService.getBaseUrl( request ) + TEMPLATE_JSP_MODIFICATION_COMPLETE + "?" +
                    OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_PDD + "=" + nIdPDD );
            }

            return strUrl;
        }

        /*
         * GESTION DES PROFILS
         * -------------------
         * Si l'utilisateur n'est pas autorisé a effectuer cette fonction,
         * il est renvoyé vers une page lui indiquant qu'il n'a pas les droits nécessaires.
         */
        if ( !isGestionAval(  ) && !isFonctionnaliteProjetDeDeliberation(  ) && pddUpdated.isEnLigne(  ) &&
                !RBACService.isAuthorized( PropositionProchaineSeanceResourceIdService.PROPOSITION_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, PropositionProchaineSeanceResourceIdService.PERMISSION_MISE_A_JOUR,
                    getUser(  ) ) )
        {
            strUrl = AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                    AdminMessage.TYPE_ERROR );
        }

        String strIdFormationConseil = request.getParameter( OdsParameters.ID_FORMATION_CONSEIL );
        String strAnnee = request.getParameter( OdsParameters.PDD_ANNEE );
        String strIdDirection = request.getParameter( OdsParameters.ID_DIRECTION );
        String strIdGroupePolitique = request.getParameter( OdsParameters.ID_GROUPE );
        String strNumero = request.getParameter( OdsParameters.PDD_NUMERO );
        String strObjet = request.getParameter( OdsParameters.PDD_OBJET );
        String strIdPddModeIntro = request.getParameter( OdsParameters.ID_PDD_MODE_INTRO );
        String strCategegorieDeDeliberation = request.getParameter( OdsParameters.ID_CATEGORIE );
        String strPieceManuel = request.getParameter( OdsParameters.PDD_PIECE_MANUEL );
        String strDelegationServicePublic = request.getParameter( OdsParameters.PDD_DELEG_SERVICE_PUBLIC );
        FileItem fileXposeDesMotifs = multipartRequest.getFile( OdsParameters.PDD_XPOSE_MOTIF );
        FileItem fileProjetDeDelibere = multipartRequest.getFile( OdsParameters.PDD_PROJET_DE_DELIBERE );

        List<String> champsNonSaisie = new ArrayList<String>(  );
        int nRetourTestForm = isValideForme( champsNonSaisie, strIdFormationConseil, strAnnee, strNumero,
                strIdDirection, strIdGroupePolitique, strObjet, strCategegorieDeDeliberation, strIdPddModeIntro );

        if ( nRetourTestForm == 0 )
        {
            int nAnnee = OdsUtils.getIntegerParameter( OdsParameters.PDD_ANNEE, request );

            if ( ( strAnnee.length(  ) != 4 ) || ( nAnnee < 1000 ) )
            {
                return AdminMessageService.getMessageUrl( request, PROPERTY_ANNEE_PAS_CORRECT_TAILLE,
                    AdminMessage.TYPE_STOP );
            }

            Timestamp dateForVersion = new Timestamp( System.currentTimeMillis(  ) );

            String strOldReference = pddUpdated.getReference(  );

            bHasChanged = majPDD( pddUpdated, strIdFormationConseil, strAnnee, strIdDirection, strIdGroupePolitique,
                    strNumero, strObjet, strIdPddModeIntro, strCategegorieDeDeliberation, strPieceManuel,
                    strDelegationServicePublic );

            if ( !PDDHome.isPddAlreadyExist( pddUpdated, _seance, getPlugin(  ) ) ||
                    strOldReference.equals( pddUpdated.getReference(  ) ) )
            {
                int newVersion = pddUpdated.getVersion(  );

                if ( pddUpdated.isEnLigne(  ) && ( !isGestionAval( ) || ( isGestionAval() && bHasChanged ) ) )
                {
                    newVersion++;

                    // On incrémente le numéro de version
                    pddUpdated.setVersion( newVersion );
                    pddUpdated.setDatePublication( dateForVersion );

                    // Ajout de l'historique
                    Historique historique = new Historique(  );
                    historique.setVersion( newVersion );
                    historique.setDatePublication( dateForVersion );
                    historique.setIdPDD( pddUpdated.getIdPdd(  ) );
                    HistoriqueHome.create( historique, getPlugin(  ) );
                }

                PDDHome.update( pddUpdated, getPlugin(  ) );
            }
            else
            {
                Object[] messagesArgs = { pddUpdated.getReference(  ) };

                return AdminMessageService.getMessageUrl( request, PROPERTY_PDD_ALREADY_EXIST, messagesArgs,
                    AdminMessage.TYPE_STOP );
            }

            if ( isFonctionnaliteProjetDeDeliberation(  ) )
            {
                // Suppression de toutes les directions co-emetrices existantes pour ce pdd
                PDDHome.deleteCoEmetrices( pddUpdated.getIdPdd(  ), getPlugin(  ) );

                // Création des directions co-emetrices
                for ( int i = 0; ( _directionCoEmetriceAdded != null ) && ( i < _directionCoEmetriceAdded.size(  ) );
                        i++ )
                {
                    DirectionCoEmetrice coEmetrice = _directionCoEmetriceAdded.get( i );
                    coEmetrice.setIdPDD( pddUpdated.getIdPdd(  ) );
                    PDDHome.create( coEmetrice, getPlugin(  ) );
                }
            }

            // Suppression de toutes les arrondissements existants pour ce pdd
            PDDHome.deleteArrondissements( pddUpdated.getIdPdd(  ), getPlugin(  ) );

            // Création des arrondissements
            for ( int i = 0; ( _arrondissementAdded != null ) && ( i < _arrondissementAdded.size(  ) ); i++ )
            {
                Arrondissement arrondissement = _arrondissementAdded.get( i );
                arrondissement.setIdPDD( pddUpdated.getIdPdd(  ) );
                PDDHome.create( arrondissement, getPlugin(  ) );
            }

            // Suppression des fichier pour ce type de PDD
            FichierFilter filter = new FichierFilter(  );
            filter.setPDD( pddUpdated.getIdPdd(  ) );

            List<Fichier> fichierToDelete = FichierHome.findByFilter( filter, getPlugin(  ) );
            Fichier fichierExposeDesMotifs = null;
            Fichier fichierProjetDeDelibere = null;

            for ( int i = 0; ( fichierToDelete != null ) && ( i < fichierToDelete.size(  ) ); i++ )
            {
                Fichier f = fichierToDelete.get( i );

                if ( ( f != null ) && ( f.getTypdeDocument(  ) != null ) &&
                        ( f.getTypdeDocument(  ).getId(  ) == TypeDocumentEnum.EXPOSE_DES_MOTIFS.getId(  ) ) )
                {
                    fichierExposeDesMotifs = f;
                }

                if ( ( f != null ) && ( f.getTypdeDocument(  ) != null ) &&
                        ( f.getTypdeDocument(  ).getId(  ) == TypeDocumentEnum.PROJET_DE_DELIBERE.getId(  ) ) )
                {
                    fichierProjetDeDelibere = f;
                }
            }

            if ( ( fileXposeDesMotifs != null ) && ( fileXposeDesMotifs.getName(  ) != null ) &&
                    !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileXposeDesMotifs.getName(  ) ) )
            {
                String strExt = fileXposeDesMotifs.getName(  ).substring( fileXposeDesMotifs.getName(  ).length(  ) -
                        3 ).toUpperCase(  );

                if ( OdsConstants.CONSTANTE_PDF.equals( strExt ) )
                {
                    if ( fichierExposeDesMotifs == null )
                    {
                        createFichier( pddUpdated, fileXposeDesMotifs, TypeDocumentEnum.EXPOSE_DES_MOTIFS );
                    }
                    else
                    {
                        updateFichier( fichierExposeDesMotifs, fileXposeDesMotifs );
                    }
                }
                else
                {
                    return AdminMessageService.getMessageUrl( request, PROPERTY_LABEL_FICHIER_XPOSE_MOTIF_NO_PDF,
                        AdminMessage.TYPE_STOP );
                }
            }

            if ( ( fileProjetDeDelibere != null ) && ( fileProjetDeDelibere.getName(  ) != null ) &&
                    !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileProjetDeDelibere.getName(  ) ) )
            {
                String strExt = fileProjetDeDelibere.getName(  )
                                                    .substring( fileProjetDeDelibere.getName(  ).length(  ) - 3 )
                                                    .toUpperCase(  );

                if ( OdsConstants.CONSTANTE_PDF.equals( strExt ) )
                {
                    if ( fichierProjetDeDelibere == null )
                    {
                        createFichier( pddUpdated, fileProjetDeDelibere, TypeDocumentEnum.PROJET_DE_DELIBERE );
                    }
                    else
                    {
                        updateFichier( fichierProjetDeDelibere, fileProjetDeDelibere );
                    }
                }
                else
                {
                    return AdminMessageService.getMessageUrl( request, PROPERTY_LABEL_FICHIER_PROJET_DELIBERE_NO_PDF,
                        AdminMessage.TYPE_STOP );
                }
            }

            if ( isGestionAval(  ) )
            {
                String strSeance = request.getParameter( OdsParameters.ID_SEANCE );

                if ( retourList )
                {
                    if ( _bUrlRetour )
                    {
                        strUrl = AppPathService.getBaseUrl( request ) + _strUrlRetour;

                        _bUrlRetour = false;
                        _strUrlRetour = null;
                    }

                    else
                    {
                        strUrl = getHomeUrl( request );
                    }
                }
                else
                {
                    strUrl = AppPathService.getBaseUrl( request ) + JSP_URL_MODIFICATION_PDD_GESTION_AVAL + "?" +
                        OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_PDD + "=" + nIdPDD + "&" +
                        OdsParameters.ID_SEANCE + "=" + strSeance;
                }

                return strUrl;
            }

            if ( retourList )
            {
                strUrl = AppPathService.getBaseUrl( request ) + TEMPLATE_JSP_LISTE;

                if ( _bUrlRetour )
                {
                    strUrl = AppPathService.getBaseUrl( request ) + _strUrlRetour;

                    _bUrlRetour = false;
                    _strUrlRetour = null;
                }
            }
            else
            {
                strUrl = ( AppPathService.getBaseUrl( request ) + TEMPLATE_JSP_MODIFICATION_COMPLETE + "?" +
                    OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_PDD + "=" + nIdPDD );
            }

            return strUrl;
        }
        else
        {
            if ( nRetourTestForm == 1 ) // cas ou l'annee n'est pas numerique
            {
                strUrl = AdminMessageService.getMessageUrl( request, PROPERTY_ANNEE_PAS_NUMERIQUE,
                        AdminMessage.TYPE_STOP );
            }
            else if ( nRetourTestForm == 2 ) // cas ou le numero n'est pas
                                             // numerique
            {
                strUrl = AdminMessageService.getMessageUrl( request, PROPERTY_NUMERO_PAS_NUMERIQUE,
                        AdminMessage.TYPE_STOP );
            }
            else
            {
                String strChamp = ( champsNonSaisie.get( 0 ) != null ) ? champsNonSaisie.get( 0 )
                                                                       : Messages.MANDATORY_FIELDS;
                Object[] messagesArgs = { I18nService.getLocalizedString( strChamp, getLocale(  ) ) };

                strUrl = AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                        messagesArgs, AdminMessage.TYPE_STOP );
            }

            return strUrl;
        }
    }

    /**
     * Méthode qui effectue la création.
     * Si retourList = TRUE alors on enregistre les données et on revient sur la liste des pdd.
     * Si retourList = FALSE alors on enregistre les données mais redirige sur l'ecran de modification.
     *
     * @param request la requête HTTP
     * @return String la template à afficher
     */
    public String doCreationPDD( HttpServletRequest request )
    {
        boolean retourList = false;
        String strUrl = null;

        /*
         * GESTION DES PROFILS
         * -------------------
         * Si l'utilisateur n'est pas autorisé a effectuer cette fonction,
         * il est renvoyé vers une page lui indiquant qu'il n'a pas les droits nécessaires.
         */
        if ( isFonctionnaliteProjetDeDeliberation(  ) &&
                !RBACService.isAuthorized( ProjetProchaineSeanceResourceIdService.PROJET_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, ProjetProchaineSeanceResourceIdService.PERMISSION_CREATION, getUser(  ) ) )
        {
            strUrl = AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                    AdminMessage.TYPE_ERROR );
        }

        if ( ( request.getParameter( OdsParameters.A_FAIRE ) != null ) &&
                request.getParameter( OdsParameters.A_FAIRE ).equals( CONSTANTE_ACTION_CANCEL ) )
        {
            resetCreation(  );

            strUrl = AppPathService.getBaseUrl( request ) + TEMPLATE_JSP_LISTE;
        }
        else if ( ( request.getParameter( OdsParameters.A_FAIRE ) != null ) &&
                request.getParameter( OdsParameters.A_FAIRE ).equals( CONSTANTE_ACTION_APPLY ) )
        {
            retourList = false;
        }
        else
        {
            retourList = true;
        }

        if ( strUrl != null )
        {
            return strUrl;
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        // Recuperation des donnees du formulaire
        String strIdFormationConseil = request.getParameter( OdsParameters.ID_FORMATION_CONSEIL );
        String strAnnee = request.getParameter( OdsParameters.PDD_ANNEE );
        String strIdDirection = request.getParameter( OdsParameters.ID_DIRECTION );
        String strIdGroupePolitique = request.getParameter( OdsParameters.ID_GROUPE );
        String strNumero = request.getParameter( OdsParameters.PDD_NUMERO );
        String strObjet = request.getParameter( OdsParameters.PDD_OBJET );
        String strIdPddModeIntro = request.getParameter( OdsParameters.ID_PDD_MODE_INTRO );
        String strCategegorieDeDeliberation = request.getParameter( OdsParameters.ID_CATEGORIE );
        String strPieceManuel = request.getParameter( OdsParameters.PDD_PIECE_MANUEL );
        String strDelegationServicePublic = request.getParameter( OdsParameters.PDD_DELEG_SERVICE_PUBLIC );
        FileItem fileXposeDesMotifs = multipartRequest.getFile( OdsParameters.PDD_XPOSE_MOTIF );
        FileItem fileProjetDeDelibere = multipartRequest.getFile( OdsParameters.PDD_PROJET_DE_DELIBERE );

        List<String> champsNonSaisie = new ArrayList<String>(  );
        int nRetourTestForm = isValideForme( champsNonSaisie, strIdFormationConseil, strAnnee, strNumero,
                strIdDirection, strIdGroupePolitique, strObjet, strCategegorieDeDeliberation, strIdPddModeIntro );

        if ( ( nRetourTestForm == 0 ) && isValideFile( champsNonSaisie, fileXposeDesMotifs, fileProjetDeDelibere ) )
        {
            int nAnnee = OdsUtils.getIntegerParameter( OdsParameters.PDD_ANNEE, request );

            if ( ( strAnnee.length(  ) != 4 ) || ( nAnnee < 1000 ) )
            {
                return AdminMessageService.getMessageUrl( request, PROPERTY_ANNEE_PAS_CORRECT_TAILLE,
                    AdminMessage.TYPE_STOP );
            }

            // Creation du projet de délibération
            PDD pddCreated = new PDD(  );
            pddCreated.setEnLigne( false );
            majPDD( pddCreated, strIdFormationConseil, strAnnee, strIdDirection, strIdGroupePolitique, strNumero,
                strObjet, strIdPddModeIntro, strCategegorieDeDeliberation, strPieceManuel, strDelegationServicePublic );

            if ( !PDDHome.isPddAlreadyExist( pddCreated, _seance, getPlugin(  ) ) )
            {
                pddCreated = PDDHome.create( pddCreated, getPlugin(  ) );
            }
            else
            {
                Object[] messagesArgs = { pddCreated.getReference(  ) };

                return AdminMessageService.getMessageUrl( request, PROPERTY_PDD_ALREADY_EXIST, messagesArgs,
                    AdminMessage.TYPE_STOP );
            }

            if ( isFonctionnaliteProjetDeDeliberation(  ) )
            {
                // Création des directions co-emetrices
                for ( int i = 0; ( _directionCoEmetriceAdded != null ) && ( i < _directionCoEmetriceAdded.size(  ) );
                        i++ )
                {
                    DirectionCoEmetrice coEmetrice = _directionCoEmetriceAdded.get( i );
                    coEmetrice.setIdPDD( pddCreated.getIdPdd(  ) );
                    PDDHome.create( coEmetrice, getPlugin(  ) );
                }
            }

            // Création des arrondissements
            for ( int i = 0; ( _arrondissementAdded != null ) && ( i < _arrondissementAdded.size(  ) ); i++ )
            {
                Arrondissement arrondissement = _arrondissementAdded.get( i );
                arrondissement.setIdPDD( pddCreated.getIdPdd(  ) );
                PDDHome.create( arrondissement, getPlugin(  ) );
            }

            // Création du fichier de type "Expose des motifs"
            createFichier( pddCreated, fileXposeDesMotifs, TypeDocumentEnum.EXPOSE_DES_MOTIFS );

            // Création du fichier de type "Projet de délibéré"
            createFichier( pddCreated, fileProjetDeDelibere, TypeDocumentEnum.PROJET_DE_DELIBERE );

            if ( retourList )
            {
                strUrl = AppPathService.getBaseUrl( request ) + TEMPLATE_JSP_LISTE;
            }
            else
            {
                strUrl = ( AppPathService.getBaseUrl( request ) + TEMPLATE_JSP_MODIFICATION_COMPLETE + "?" +
                    OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_PDD + "=" + pddCreated.getIdPdd(  ) );
            }
        }
        else
        {
            if ( nRetourTestForm == 1 ) // cas ou l'annee n'est pas numerique
            {
                strUrl = AdminMessageService.getMessageUrl( request, PROPERTY_ANNEE_PAS_NUMERIQUE,
                        AdminMessage.TYPE_STOP );
            }
            else if ( nRetourTestForm == 2 ) // cas ou le numero n'est pas numerique
            {
                strUrl = AdminMessageService.getMessageUrl( request, PROPERTY_NUMERO_PAS_NUMERIQUE,
                        AdminMessage.TYPE_STOP );
            }
            else
            {
                String strChamp = null;

                if ( PROPERTY_LABEL_FICHIER_XPOSE_MOTIF_NO_PDF.equals( champsNonSaisie.get( 0 ) ) )
                {
                    strUrl = AdminMessageService.getMessageUrl( request, PROPERTY_LABEL_FICHIER_NO_PDF,
                            AdminMessage.TYPE_STOP );
                }
                else
                {
                    strChamp = ( champsNonSaisie.get( 0 ) != null ) ? champsNonSaisie.get( 0 ) : Messages.MANDATORY_FIELDS;

                    Object[] messagesArgs = { I18nService.getLocalizedString( strChamp, getLocale(  ) ) };

                    strUrl = AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                            messagesArgs, AdminMessage.TYPE_STOP );
                }
            }
        }

        return strUrl;
    }

    /**
     * Permet de creer un fichier du type qui est passé en paramètre et de
     * l'associé au pdd passé en parametre
     *
     * @param pddCreated PDD
     * @param fichier FileItem
     * @param typeDocumentEnum TypeDocumentEnum
     */
    private void createFichier( final PDD pddCreated, final FileItem fichier, TypeDocumentEnum typeDocumentEnum )
    {
        FichierPhysique newFichierPhysique = new FichierPhysique(  );
        newFichierPhysique.setDonnees( fichier.get(  ) );

        int idFichierPhysique = FichierPhysiqueHome.create( newFichierPhysique, getPlugin(  ) );
        newFichierPhysique.setIdFichier( idFichierPhysique );

        Fichier newFichier = new Fichier(  );
        newFichier.setFichier( newFichierPhysique );
        newFichier.setPDD( pddCreated );
        newFichier.setNom( getNameCourt( fichier ) );
        newFichier.setExtension( fichier.getName(  ).substring( fichier.getName(  ).length(  ) - 3 ).toUpperCase(  ) );
        newFichier.setTaille( (int) fichier.getSize(  ) );

        TypeDocument typDocument = FichierHome.findTypeDocumentsById( typeDocumentEnum.getId(  ), getPlugin(  ) );
        newFichier.setTypdeDocument( typDocument );
        newFichier.setTitre( typDocument.getLibelle(  ) );

        newFichier.setEnLigne( pddCreated.isEnLigne(  ) );

        Timestamp dateForVersion = new Timestamp( System.currentTimeMillis(  ) );

        newFichier.setEnLigne( true );
        newFichier.setVersion( 1 );
        newFichier.setDatePublication( dateForVersion );

        FichierHome.create( newFichier, getPlugin(  ) );

        // Ajout de l'historique
        Historique historique = new Historique(  );
        historique.setVersion( 1 );
        historique.setDatePublication( dateForVersion );
        historique.setIdDocument( newFichier.getId(  ) );
        HistoriqueHome.create( historique, getPlugin(  ) );
    }

    /**
     * Modifie la piece annexe et le fichier physique Si le fichie est en ligne,
     * alors la version est incrémenté de 1 et ajout d'un historique si tous les
     * champs obligatoires du formulaire de modification d'un fichier ont été
     * renseigné,<BR>
     * la méthode retourne l'url de gestion des fichiers,<BR>
     * sinon l'url retournée est celle de l'interface spécifiant les messages
     * d'erreurs.
     * 
     * @param fichier l'objet fichier à updater
     * @param fileItem le fileItem contenant les nouvelles données du fichier
     */
    private void updateFichier( Fichier fichier, final FileItem fileItem )
    {
        Timestamp dateForVersion = new Timestamp( System.currentTimeMillis(  ) );

        if ( ( fichier != null ) && ( fichier.getFichier(  ) != null ) && ( fileItem != null ) &&
                ( fileItem.getName(  ) != null ) && !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileItem.getName(  ) ) )
        {
            // Modification de l'objet représentant le fichier physique
            FichierPhysique fichierPhysique = fichier.getFichier(  );
            fichierPhysique.setDonnees( fileItem.get(  ) );
            FichierPhysiqueHome.update( fichierPhysique, getPlugin(  ) );

            // Modification de l'objet représentant le Fichier
            fichier.setFichier( fichierPhysique );
            fichier.setNom( getNameCourt( fileItem ) );

            String strExtension = fileItem.getName(  ).substring( fileItem.getName(  ).length(  ) - 3 ).toUpperCase(  );

            if ( ( strExtension != null ) && ( strExtension.length(  ) == 3 ) &&
                    OdsConstants.CONSTANTE_PDF.equals( strExtension ) )
            {
                fichier.setExtension( strExtension );
            }

            fichier.setTaille( (int) fileItem.getSize(  ) );

            if ( fichier.getEnLigne(  ) )
            {
                // on incremente le numero de version
                int newVersion = fichier.getVersion(  ) + 1;
                fichier.setVersion( newVersion );
                fichier.setDatePublication( dateForVersion );

                // Ajout de l'historique
                Historique historique = new Historique(  );
                historique.setVersion( newVersion );
                historique.setDatePublication( dateForVersion );
                historique.setIdDocument( fichier.getId(  ) );
                HistoriqueHome.create( historique, getPlugin(  ) );
            }

            FichierHome.update( fichier, getPlugin(  ) );
        }
    }

    /**
     * Permet de mettre a jour le pdd qui est passé en paramètre
     *
     * @param pdd
     *            PPD que l'on desire mettre à jour
     * @param strIdFormationConseil
     *            id Formation conseil
     * @param strAnnee
     *            Annee
     * @param strIdDirection
     *            Id de la direction principale
     * @param strIdGroupePolitique
     *            Id du groupe politique
     * @param strNumero
     *            numero du PDD
     * @param strObjet
     *            Objet du PDD
     * @param strIdPddModeIntro
     *            id du mode d'intro du PDD
     * @param strCategegorieDeDeliberation
     *            id de la categorie de deliberation
     * @param strPieceManuel
     *            boolean de la piece manuelle
     * @param strDelegationServicePublic
     *            boolean de la delegation du service public
     * @return TRUE si la mise à jour a provoquée des changements, FALSE sinon
     */
    private boolean majPDD( PDD pdd, final String strIdFormationConseil, final String strAnnee,
        final String strIdDirection, final String strIdGroupePolitique, final String strNumero, final String strObjet,
        final String strIdPddModeIntro, final String strCategegorieDeDeliberation, final String strPieceManuel,
        final String strDelegationServicePublic )
    {
        boolean bHasChanged = false;

        if ( ( pdd.getCategorieDeliberation(  ) == null ) ||
                ( pdd.getCategorieDeliberation(  ).getIdCategorie(  ) != OdsUtils.string2id( 
                    strCategegorieDeDeliberation ) ) )
        {
            bHasChanged = true;
        }

        pdd.setCategorieDeliberation( getCategorieDeliberationObject( strCategegorieDeDeliberation ) );

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            if ( ( pdd.getDirection(  ) == null ) ||
                    ( pdd.getDirection(  ).getIdDirection(  ) != OdsUtils.string2id( strIdDirection ) ) )
            {
                bHasChanged = true;
            }

            pdd.setDirection( getDirectionObject( strIdDirection ) );
        }
        else
        {
            if ( ( pdd.getGroupePolitique(  ) == null ) ||
                    ( pdd.getGroupePolitique(  ).getIdGroupe(  ) != OdsUtils.string2id( strIdGroupePolitique ) ) )
            {
                bHasChanged = true;
            }

            pdd.setGroupePolitique( getGroupePolitiqueObject( strIdGroupePolitique ) );
        }

        if ( ( strIdFormationConseil != null ) && !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strIdFormationConseil ) )
        {
            if ( ( pdd.getFormationConseil(  ) == null ) ||
                    ( pdd.getFormationConseil(  ).getIdFormationConseil(  ) != OdsUtils.string2id( 
                        strIdFormationConseil ) ) )
            {
                bHasChanged = true;
            }

            pdd.setFormationConseil( getFormationConseilObject( strIdFormationConseil ) );
        }

        String strReference;

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            strReference = strAnnee + " " + pdd.getDirection(  ).getCode(  ) + " " + strNumero;
        }
        else
        {
            strReference = strAnnee + " " + pdd.getGroupePolitique(  ).getNomGroupe(  ) + " " + strNumero;
        }

        if ( !strReference.equals( pdd.getReference(  ) ) )
        {
            bHasChanged = true;
        }

        pdd.setReference( strReference );

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            pdd.setTypePdd( PDD.CONSTANTE_TYPE_PROJET );
        }
        else
        {
            pdd.setTypePdd( PDD.CONSTANTE_TYPE_PROP );
        }

        if ( ( ( strDelegationServicePublic != null ) && !pdd.isDelegationsServices(  ) ) ||
                ( ( strDelegationServicePublic == null ) && pdd.isDelegationsServices(  ) ) )
        {
            bHasChanged = true;
        }

        if ( strDelegationServicePublic != null )
        {
            pdd.setDelegationsServices( true );
        }
        else
        {
            pdd.setDelegationsServices( false );
        }

        if ( ( pdd.getModeIntroduction(  ) == null ) || ( !pdd.getModeIntroduction(  ).equals( strIdPddModeIntro ) ) )
        {
            bHasChanged = true;
        }

        if ( CONSTANTE_INTRO_DIRECT_CODE.equals( strIdPddModeIntro ) )
        {
            pdd.setModeIntroduction( CONSTANTE_INTRO_DIRECT_CODE );
        }
        else
        {
            pdd.setModeIntroduction( CONSTANTE_SOUMIS_AVIS_CONSEIL_CODE );
        }

        if ( ( pdd.getObjet(  ) == null ) || ( !pdd.getObjet(  ).equals( strObjet ) ) )
        {
            bHasChanged = true;
        }

        pdd.setObjet( strObjet );

        if ( ( ( strPieceManuel != null ) && !pdd.isPiecesManuelles(  ) ) ||
                ( ( strPieceManuel == null ) && pdd.isPiecesManuelles(  ) ) )
        {
            bHasChanged = true;
        }

        if ( strPieceManuel != null )
        {
            pdd.setPiecesManuelles( true );
        }
        else
        {
            pdd.setPiecesManuelles( false );
        }

        return bHasChanged;
    }

    /**
     * Télecharge un fichier. Renvoie un flux de bytes du fichier dans l'objet
     * HttpServletResponse response
     * 
     * @param request
     *            la requête HTTP
     * @param response
     *            la reponse HTTP
     */
    public void doDowloadFichier( HttpServletRequest request, HttpServletResponse response )
    {
        String strIdFichier = request.getParameter( OdsParameters.ID_FICHIER );

        if ( strIdFichier != null )
        {
            int nIdFichier = OdsUtils.getIntegerParameter( OdsParameters.ID_FICHIER, request );

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
     * methode qui test si dans le formulaire de saisi d'un pdd, l'ensemble des
     * critéres obligatoires ont été renseigné
     *
     * @param champsNonSaisie
     *            List<String> gere les champs non obligatoires non renseignés
     * @param strIdFormationConseil
     *            String formation du conseil
     * @param strAnnee
     *            String annee
     * @param strNumero
     *            String numero
     * @param strIdDirection
     *            String Direction
     * @param strIdGroupePolitique
     *            String Groupe politique
     * @param strObjet
     *            String objet
     * @param strCategegorieDeDeliberation
     *            String Categorie de deliberation
     * @param strIdPddModeIntro
     *            String Mode Introduction
     *
     * @return retour 1 si l'annee, 2 si le numero ne sont pas numerique, -1 si
     *         un champ n'est pas renseigné, et 0 si tout est OK
     */
    private int isValideForme( List<String> champsNonSaisie, String strIdFormationConseil, String strAnnee,
        String strNumero, String strIdDirection, String strIdGroupePolitique, String strObjet,
        String strCategegorieDeDeliberation, String strIdPddModeIntro )
    {
        int result = 0;

        if ( ( strIdFormationConseil == null ) || OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strIdFormationConseil ) ||
                OdsConstants.FILTER_ALL.equals( strIdFormationConseil ) )
        {
            result = -1;
            champsNonSaisie.add( PROPERTY_LABEL_FORMATION_CONSEIL );
        }

        if ( ( strAnnee == null ) || OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strAnnee ) )
        {
            result = -1;
            champsNonSaisie.add( PROPERTY_LABEL_ANNEE );
        }
        else
        {
            try
            {
                Integer.parseInt( strAnnee );
            }
            catch ( NumberFormatException nfe )
            {
                result = 1;
            }
        }

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            if ( ( strIdDirection == null ) || OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strIdDirection ) ||
                    OdsConstants.FILTER_ALL.equals( strIdDirection ) )
            {
                result = -1;
                champsNonSaisie.add( PROPERTY_LABEL_DIRECTION );
            }
        }
        else
        {
            if ( ( strIdGroupePolitique == null ) || OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strIdGroupePolitique ) ||
                    OdsConstants.FILTER_ALL.equals( strIdGroupePolitique ) )
            {
                result = -1;
                champsNonSaisie.add( PROPERTY_LABEL_GROUPE );
            }
        }

        if ( ( strNumero == null ) || OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strNumero ) )
        {
            result = -1;
            champsNonSaisie.add( PROPERTY_LABEL_NUMERO );
        }
        else
        {
            try
            {
                Integer.parseInt( strNumero );
            }
            catch ( NumberFormatException nfe )
            {
                result = 2;
            }
        }

        if ( ( strObjet == null ) || OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strObjet ) )
        {
            result = -1;
            champsNonSaisie.add( PROPERTY_LABEL_OBJET );
        }

        if ( ( strIdPddModeIntro == null ) || CONSTANTE_MODE_INTRO.equals( strIdPddModeIntro ) )
        {
            result = -1;
            champsNonSaisie.add( PROPERTY_LABEL_MODE_INTRO );
        }

        if ( ( strCategegorieDeDeliberation == null ) ||
                OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strCategegorieDeDeliberation ) ||
                OdsConstants.FILTER_ALL.equals( strCategegorieDeDeliberation ) )
        {
            result = -1;
            champsNonSaisie.add( PROPERTY_LABEL_CATEGORIE_DELIB );
        }

        return result;
    }

    /**
     * methode qui test si dans le formulaire de saisi d'un fichier,l'ensemble
     * des critéres obligatoires ont été renseigné
     *
     * @param request
     *            la requête HTTP
     * @return boolean true si la piece annexe est valide
     */
    private boolean isValidePieceAnnexe( HttpServletRequest request )
    {
        if ( request.getParameter( OdsParameters.ID_PDD ) != null )
        {
            return true;
        }

        return false;
    }

    /**
     * methode qui test si dans le formulaire de saisi d'un pdd, l'ensemble des
     * critéres obligatoires ont été renseigné
     *
     * @param champsNonSaisie
     *            List<String> List<String> gere les champs non obligatoires
     *            non renseignés
     * @param fileXposeDesMotifs
     *            FileItem Fichier pour Exposé des motifs
     * @param fileProjetDeDelibere
     *            FileItem Fichier pour Projet de délibéré
     *
     * @return TRUE si tous les champs obligatoires ont été renseigné, FALSE
     *         sinon
     */
    private boolean isValideFile( List<String> champsNonSaisie, FileItem fileXposeDesMotifs,
        FileItem fileProjetDeDelibere )
    {
        if ( ( fileXposeDesMotifs == null ) || ( fileXposeDesMotifs.getName(  ) == null ) ||
                OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileXposeDesMotifs.getName(  ) ) )
        {
            champsNonSaisie.add( PROPERTY_LABEL_FILE_EXP_MOTIF );

            return false;
        }

        String extensionXposeDesMotifs = fileXposeDesMotifs.getName(  )
                                                           .substring( fileXposeDesMotifs.getName(  ).length(  ) - 3 )
                                                           .toUpperCase(  );

        if ( !OdsConstants.CONSTANTE_PDF.equals( extensionXposeDesMotifs ) )
        {
            champsNonSaisie.add( PROPERTY_LABEL_FICHIER_XPOSE_MOTIF_NO_PDF );

            return false;
        }

        if ( ( fileProjetDeDelibere == null ) || ( fileProjetDeDelibere.getName(  ) == null ) ||
                OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileProjetDeDelibere.getName(  ) ) )
        {
            champsNonSaisie.add( PROPERTY_LABEL_FILE_PJ_DELIB );

            return false;
        }

        String extensionProjetDeDeliberes = fileProjetDeDelibere.getName(  )
                                                                .substring( fileProjetDeDelibere.getName(  ).length(  ) -
                3 ).toUpperCase(  );

        if ( !OdsConstants.CONSTANTE_PDF.equals( extensionProjetDeDeliberes ) )
        {
            champsNonSaisie.add( PROPERTY_LABEL_FICHIER_PROJET_DELIBERE_NO_PDF );

            return false;
        }

        return true;
    }

    /**
     *
     * @param fichier le fichier à vérifier
     * @return TRUE si le fichier est valide, FALSE sinon
     */
    private boolean isValideFile( FileItem fichier )
    {
        if ( ( fichier == null ) || ( fichier.getName(  ) == null ) ||
                OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fichier.getName(  ) ) )
        {
            return false;
        }

        String extensionFichier = fichier.getName(  ).substring( fichier.getName(  ).length(  ) - 3 ).toUpperCase(  );

        if ( !OdsConstants.CONSTANTE_PDF.equals( extensionFichier ) )
        {
            return false;
        }

        return true;
    }

    /**
     * Affiche la liste des pdd en fonction des filtres
     *
     * @param request la requête HTTP
     * @return String template
     */
    public String getPDDList( HttpServletRequest request )
    {
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        PDDFilter filter = new PDDFilter(  );

        //_nIdCategorieDeliberationSelected = getCategorieDeliberation( request, filter );
        //_nIdFormationConseilSelected = getFormationConseil( request, filter );
        if ( ( filter != null ) && ( _nIdCategorieDeliberationSelected != PDDFilter.ALL_INT ) )
        {
            filter.setCategorieDeliberation( _nIdCategorieDeliberationSelected );
        }

        if ( ( filter != null ) && ( _nIdFormationConseilSelected != PDDFilter.ALL_INT ) )
        {
            filter.setIdFormationConseil( _nIdFormationConseilSelected );
        }

        ajouterPermissionsDansHashmap( model );

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            //_strDirectionSelected = getDirection( request, filter );
            if ( ( filter != null ) && !_strDirectionSelected.equals( OdsConstants.FILTER_ALL ) )
            {
                filter.setDirection( _strDirectionSelected );
            }

            filter.setType( PDD.CONSTANTE_TYPE_PROJET );
        }
        else
        {
            //_strGroupeDepositaireSelected = getGroupeDepositaire( request, filter );
            if ( ( filter != null ) && !_strGroupeDepositaireSelected.equals( OdsConstants.FILTER_ALL ) )
            {
                filter.setGroupeDepositaire( _strGroupeDepositaireSelected );
            }

            filter.setType( PDD.CONSTANTE_TYPE_PROP );
        }

        //_strModeIntroSelected = getModeIntro( request, filter );
        if ( ( filter != null ) && !_strModeIntroSelected.equals( CONSTANTE_MODE_INTRO ) )
        {
            filter.setModeIntroduction( _strModeIntroSelected );
        }

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        List<PDD> pdds = new ArrayList<PDD>(  );

        if ( isGestionAval(  ) )
        {
            List<Seance> listSeance = SeanceHome.findOldSeance( getPlugin(  ) );

            StatutFilter statutFilter = new StatutFilter(  );
            statutFilter.setPourPDD( true );

            ReferenceList refListStatuts = new ReferenceList(  );

            // ajout dans la referenceList de l'ensemble des items représentant les
            // statuts
            refListStatuts.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );
            refListStatuts.addItem( 0, I18nService.getLocalizedString( PROPERTY_STATUT_NON_RENSEIGNE, getLocale(  ) ) );

            initRefListStatut( statutFilter, refListStatuts );

            if ( ( _seance == null ) ||
                    ( ( request.getParameter( OdsParameters.ID_SEANCE ) != null ) &&
                    !request.getParameter( OdsParameters.ID_SEANCE ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) ||
                    ( _seance.getIdSeance(  ) == SeanceHome.getProchaineSeance( getPlugin(  ) ).getIdSeance(  ) ) )
            {
                _nIdSeanceSelected = getSeance( request, filter );
            }
            else
            {
                _nIdSeanceSelected = _seance.getIdSeance(  );
                filter.setSeance( _nIdSeanceSelected );
            }

            _seance = SeanceHome.findByPrimaryKey( _nIdSeanceSelected, getPlugin(  ) );

            //_nIdStatutSelected = getStatut( request, filter );
            if ( ( filter != null ) && ( _nIdStatutSelected != PDDFilter.ALL_INT ) )
            {
                filter.setStatut( _nIdStatutSelected );
            }

            filter.setSeance( _nIdSeanceSelected );
            filter.setTypeOrdreDuJour( TypeOrdreDuJourEnum.DEFINITIF.getId(  ) );

            model.put( OdsMarks.MARK_GESTION_AVAL, true );
            model.put( MARK_LISTE_SEANCES, listSeance );
            model.put( MARK_LISTE_STATUTS, refListStatuts );
            model.put( MARK_ID_SEANCE_SELECTED, _nIdSeanceSelected );
            model.put( MARK_ID_STATUT_SELECTED, _nIdStatutSelected );
        }
        else
        {
            if ( _seance == null )
            {
                _seance = SeanceHome.getProchaineSeance( getPlugin(  ) );
            }

            if ( !isFonctionnaliteProjetDeDeliberation(  ) )
            {
                //_nIdPublicationSelected = getPublication( request, filter );
                if ( ( filter != null ) && ( _nIdPublicationSelected != PDDFilter.ALL_INT ) )
                {
                    filter.setPublication( _nIdPublicationSelected );
                }
            }

            if ( _seance != null )
            {
                filter.setProchaineSeance( _seance.getIdSeance(  ) );
            }

            //_nIdInscritODJSelected = getInscritODJ( request, filter );
            if ( ( filter != null ) && ( _nIdInscritODJSelected != PDDFilter.ALL_INT ) )
            {
                filter.setInscritODJ( _nIdInscritODJSelected );
            }

            if ( _nIdInscritODJSelected != PDDFilter.ALL_INT )
            {
                //recuperation de l'ordre du jour de reference du conseil general
                OrdreDuJourFilter odjFilter = new OrdreDuJourFilter(  );
                odjFilter.setIdSeance( _seance.getIdSeance(  ) );
                odjFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) );

                OrdreDuJour ordreDuJourReferenceConseilGeneral = OrdreDuJourHome.findOdjReferenceByFilter( odjFilter,
                        getPlugin(  ) );
                odjFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILMUNICIPAL.getId(  ) );

                OrdreDuJour ordreDuJourReferenceConseilMunicipal = OrdreDuJourHome.findOdjReferenceByFilter( odjFilter,
                        getPlugin(  ) );

                //si les pdds doivent etre inscrit a un ordre du jour de reference 
                //et qu'il n'en existe pas on initialise le filtre pour qu'il ne renvoie aucun pdd 
                filter.setInscritODJ( _nIdInscritODJSelected );

                if ( ordreDuJourReferenceConseilGeneral != null )
                {
                    filter.setIdOdjReferenceFormationConseilGeneral( ordreDuJourReferenceConseilGeneral.getIdOrdreDuJour(  ) );
                }
                else
                {
                    filter.setIdOdjReferenceFormationConseilGeneral( PDDFilter.ALL_INT );
                }

                if ( ordreDuJourReferenceConseilMunicipal != null )
                {
                    filter.setIdOdjReferenceFormationConseilMunicipal( ordreDuJourReferenceConseilMunicipal.getIdOrdreDuJour(  ) );
                }
                else
                {
                    filter.setIdOdjReferenceFormationConseilMunicipal( PDDFilter.ALL_INT );
                }

                if ( ( ordreDuJourReferenceConseilGeneral != null ) )
                {
                    filter.setIdOdjReferenceFormationConseilGeneral( ordreDuJourReferenceConseilGeneral.getIdOrdreDuJour(  ) );
                }

                if ( ( ordreDuJourReferenceConseilMunicipal != null ) )
                {
                    filter.setIdOdjReferenceFormationConseilMunicipal( ordreDuJourReferenceConseilMunicipal.getIdOrdreDuJour(  ) );
                }
            }

            model.put( OdsMarks.MARK_GESTION_AVAL, false );
            model.put( MARK_LISTE_PUBLICATION, _referencePublicationList );
            model.put( MARK_ID_PUBLICATION_SELECTED, _nIdPublicationSelected );
            model.put( Seance.MARK_PROCHAINE_SEANCE, _seance );
        }

        if ( _seance != null )
        {
            pdds = PDDHome.findByFilter( filter, getPlugin(  ) );
        }
        
        Collections.sort(pdds,new PDDComparator());

        Paginator paginator = new Paginator( pdds, _nItemsPerPage, getHomeUrl( request ),
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );
        model.put( MARK_FONCTIONNALITE_IS_PROJET, isFonctionnaliteProjetDeDeliberation(  ) );
        model.put( MARK_LISTE_PDDS, paginator.getPageItems(  ) );
        model.put( OdsMarks.MARK_NOMBRE_RESULTATS, pdds.size(  ) );
        model.put( OdsMarks.MARK_PAGINATOR, paginator );
        model.put( OdsMarks.MARK_NB_ITEMS_PER_PAGE, OdsConstants.CONSTANTE_CHAINE_VIDE + _nItemsPerPage );
        model.put( MARK_LISTE_FORMATION_CONSEIL, _referenceFormationConseilList );
        model.put( MARK_ID_FORMATION_CONSEIL_SELECTED, _nIdFormationConseilSelected );
        model.put( MARK_LISTE_CATEGORIE_DELIBERATION, _referenceCategorieDeliberationList );
        model.put( MARK_ID_CATEGORIE_DELIBERATION_SELECTED, _nIdCategorieDeliberationSelected );

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            model.put( MARK_LISTE_DIRECTION, _referenceDirectionList );
            model.put( MARK_ID_DIRECTION_SELECTED, _strDirectionSelected );
        }
        else
        {
            model.put( MARK_LISTE_GROUPE_DEPOSITAIRE, _referenceGroupeDepositaireList );
            model.put( MARK_ID_GROUPE_DEPOSITAIRE_SELECTED, _strGroupeDepositaireSelected );
        }

        model.put( MARK_LISTE_INSCRIT_ODJ, _referenceInscritODJList );
        model.put( MARK_ID_INSCRIT_ODJ, _nIdInscritODJSelected );
        model.put( MARK_LISTE_MODE_INTRO, _referenceModeIntroList );
        model.put( MARK_ID_MODE_INTRO_SELECTED, _strModeIntroSelected );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_PDD, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     *
     * @param request la requete Http
     * @param strUrlJSP l'url de la JSP appelant la méthode
     * @param strUrlRetour l'url de la JSP de retour
     * @return la template affichant une liste de PDD afin d'etre selectionné
     */
    public String getPDDListSelect( HttpServletRequest request, String strUrlJSP, String strUrlRetour )
    {
        int nIdOdj = OdsUtils.getIntegerParameter( OdsParameters.ID_ODJ, request );
        int nIdReleve = OdsUtils.getIntegerParameter( OdsParameters.ID_RELEVE, request );

        ReleveDesTravaux releve = null;

        // Initialisation
        HashMap<String, Object> model = new HashMap<String, Object>(  );
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        String strReference = OdsConstants.CONSTANTE_CHAINE_VIDE;

        // Initialisation de la séance
        Seance seanceLocal = null;

        // Instanciation d'un nouveau filtre
        PDDFilter filter = new PDDFilter(  );

        // Préparation des filtres
        if ( ( nIdOdj != -1 ) && estPourSelectionSimpleOrdreDuJour( strUrlJSP ) )
        {
            // si la liste des pdds est appellé par le module d'administarion de
            // l'ordre du jour
            // les pdds doivent etre de la même formation de conseil et de la
            // meme seance que l’ordre du jour
            OrdreDuJour ordreDuJour;
            ordreDuJour = OrdreDuJourHome.findByPrimaryKey( nIdOdj, getPlugin(  ) );
            model.put( OrdreDuJour.MARK_ODJ, ordreDuJour );

            if ( ordreDuJour.getFormationConseil(  ) != null )
            {
                filter.setIdFormationConseil( ordreDuJour.getFormationConseil(  ).getIdFormationConseil(  ) );
            }

            if ( ordreDuJour.getSeance(  ) != null )
            {
                filter.setProchaineSeance( ordreDuJour.getSeance(  ).getIdSeance(  ) );
                seanceLocal = SeanceHome.findByPrimaryKey( ordreDuJour.getSeance(  ).getIdSeance(  ), getPlugin(  ) );
            }
        }
        else if ( ( nIdReleve != -1 ) && estPourSelectionReleveDesTravaux( strUrlJSP ) )
        {
            releve = ReleveDesTravauxHome.findByPrimaryKey( nIdReleve, getPlugin(  ) );
            model.put( ReleveDesTravaux.MARK_RELEVE, releve );

            _nIdFormationConseilSelected = getFormationConseil( request, filter );

            if ( releve.getSeance(  ) != null )
            {
                filter.setProchaineSeance( releve.getSeance(  ).getIdSeance(  ) );
                seanceLocal = SeanceHome.findByPrimaryKey( releve.getSeance(  ).getIdSeance(  ), getPlugin(  ) );
            }
        }

        if ( null != request.getParameter( OdsParameters.REFERENCE ) )
        {
            strReference = request.getParameter( OdsParameters.REFERENCE ).trim(  );
            filter.setReference( strReference );
        }

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            _strDirectionSelected = getDirection( request, filter );
            filter.setType( PDD.CONSTANTE_TYPE_PROJET );
        }
        else
        {
            _strGroupeDepositaireSelected = getGroupeDepositaire( request, filter );
            filter.setType( PDD.CONSTANTE_TYPE_PROP );
        }

        filter.setType( ( isFonctionnaliteProjetDeDeliberation(  ) ) ? PDD.CONSTANTE_TYPE_PROJET : PDD.CONSTANTE_TYPE_PROP );

        // On récupère la liste des pdd filtrés
        List<PDD> pdds = PDDHome.findByFilter( filter, getPlugin(  ) );

        // On instancie un tableau qui va contenir tous les éléments qui vont
        // être supprimés
        List<PDD> pddToDelete = new ArrayList<PDD>(  );

        // traitement pour l'ordre du jour
        if ( ( pdds != null ) && estPourSelectionSimpleOrdreDuJour( strUrlJSP ) )
        {
            List<EntreeOrdreDuJour> listEntree = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( nIdOdj,
                    getPlugin(  ) );

            if ( listEntree != null )
            {
                for ( PDD pdd : pdds )
                {
                    for ( EntreeOrdreDuJour entreeOrdreDuJour : listEntree )
                    {
                        if ( ( entreeOrdreDuJour.getPdd(  ) != null ) &&
                                ( pdd.getIdPdd(  ) == entreeOrdreDuJour.getPdd(  ).getIdPdd(  ) ) )
                        {
                            pddToDelete.add( pdd );

                            break;
                        }
                    }
                }
            }
        }

        // traitement pour releves des travaux
        else if ( ( pdds != null ) && estPourSelectionReleveDesTravaux( strUrlJSP ) )
        {
            List<ElementReleveDesTravaux> listPDDElementReleveDesTravaux = ElementReleveDesTravauxHome.findElementReleveDesTravauxByRelevePDD( releve,
                    getPlugin(  ) );

            // Préparation des filtres
            _nIdFormationConseilSelected = getFormationConseil( request, filter );

            if ( listPDDElementReleveDesTravaux != null )
            {
                for ( PDD pdd : pdds )
                {
                    for ( ElementReleveDesTravaux elementPDD : listPDDElementReleveDesTravaux )
                    {
                        if ( pdd.getIdPdd(  ) == elementPDD.getProjetDeliberation(  ).getIdPdd(  ) )
                        {
                            pddToDelete.add( pdd );

                            break;
                        }
                    }
                }
            }
        }

        // Suppression des PDD qui ne doivent pas etre affichés
        pdds.removeAll( pddToDelete );

        Paginator paginator = null;

        // Initialisation du paginator
        if ( estPourSelectionSimpleOrdreDuJour( strUrlJSP ) )
        {
            paginator = new Paginator( pdds, _nItemsPerPage,
                    strUrlJSP + "?" + OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_ODJ + "=" + nIdOdj,
                    Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        }
        else if ( estPourSelectionReleveDesTravaux( strUrlJSP ) )
        {
            paginator = new Paginator( pdds, _nItemsPerPage,
                    strUrlJSP + "?" + OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_RELEVE + "=" + nIdReleve,
                    Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        }

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            model.put( MARK_LISTE_DIRECTION, _referenceDirectionList );
            model.put( MARK_ID_DIRECTION_SELECTED, _strDirectionSelected );
        }
        else
        {
            model.put( MARK_LISTE_GROUPE_DEPOSITAIRE, _referenceGroupeDepositaireList );
            model.put( MARK_ID_GROUPE_DEPOSITAIRE_SELECTED, _strGroupeDepositaireSelected );
        }

        // on remplit la hashmap avec les infos nécessaires pour le template
        // model.put(OdsParameters.ID_VOEUAMENDEMENT,.getIdVoeuAmendement());
        model.put( MARK_LISTE_FORMATION_CONSEIL, _referenceFormationConseilList );
        model.put( MARK_ID_FORMATION_CONSEIL_SELECTED, _nIdFormationConseilSelected );
        model.put( MARK_FONCTIONNALITE_IS_PROJET, isFonctionnaliteProjetDeDeliberation(  ) );
        model.put( MARK_LISTE_PDDS, paginator.getPageItems(  ) );
        model.put( OdsMarks.MARK_NOMBRE_RESULTATS, pdds.size(  ) );
        model.put( Seance.MARK_SEANCE, seanceLocal );
        model.put( OdsParameters.REFERENCE, strReference );
        model.put( OdsMarks.MARK_PAGINATOR, paginator );
        model.put( OdsMarks.MARK_NB_ITEMS_PER_PAGE, OdsConstants.CONSTANTE_CHAINE_VIDE + _nItemsPerPage );
        model.put( MARK_URL_JSP_APPELANT, strUrlJSP );
        model.put( MARK_URL_RETOUR, strUrlRetour );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_PDD_SELECTION, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Affiche un formulaire de selection multiple de PDDs
     * @param request la requête Http
     * @param strUrlJSP l'url appelant cette méthode
     * @param strUrlRetour l'url de retour du formulaire affiché
     * @param estGestionAval TRUE si l'on est en gestion aval, FALSE sinon
     * @return la template d'affichage du formulaire de selection multiple
     */
    public String getPDDListMultiSelect( HttpServletRequest request, String strUrlJSP, String strUrlRetour,
        boolean estGestionAval )
    {
        OrdreDuJour odj = null;
        EntreeOrdreDuJour entree = null;
        VoeuAmendement va = null;
        
        String strUrlResultat = strUrlRetour;

        // Initialisation
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        String strReference = OdsConstants.CONSTANTE_CHAINE_VIDE;
        Seance seanceLocal = null;
        PDDFilter filter = new PDDFilter(  );

        /*
         * Traitement pour le cas de l'Ordre du Jour
         */
        if ( estPourSelectionMultipleOrdreDuJour( strUrlJSP ) &&
                ( ( request.getParameter( OdsParameters.ID_ENTREE ) != null ) &&
                !request.getParameter( OdsParameters.ID_ENTREE ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) )
        {
            // si la liste des pdds est appellé par le module d'administarion de
            // l'ordre du jour
            // les pdds doivent etre de la même formation de conseil et de la
            // meme seance que l’ordre du jour
            int nIdEntreeOdj = OdsUtils.getIntegerParameter( OdsParameters.ID_ENTREE, request );

            if ( nIdEntreeOdj > 0 )
            {
                entree = EntreeOrdreDuJourHome.findByPrimaryKey( nIdEntreeOdj, getPlugin(  ) );
                odj = OrdreDuJourHome.findByPrimaryKey( entree.getOrdreDuJour(  ).getIdOrdreDuJour(  ), getPlugin(  ) );
                entree.setOrdreDuJour( odj );
                model.put( MARK_ENTREE, entree );

                if ( odj.getFormationConseil(  ) != null )
                {
                    filter.setIdFormationConseil( odj.getFormationConseil(  ).getIdFormationConseil(  ) );
                }

                if ( odj.getSeance(  ) != null )
                {
                    filter.setProchaineSeance( odj.getSeance(  ).getIdSeance(  ) );
                    seanceLocal = SeanceHome.findByPrimaryKey( odj.getSeance(  ).getIdSeance(  ), getPlugin(  ) );
                }

                model.put( MARK_FORMATION_CONSEIL, odj.getFormationConseil(  ) );
            }
        }

        /*
         * Traitement pour le cas d'un Voeu ou Amendement
         */
        else if ( estPourSelectionVoeuAmendement( strUrlJSP ) &&
                ( ( request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ) != null ) &&
                !request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) )
        {
            int nIdVa = OdsUtils.getIntegerParameter( OdsParameters.ID_VOEUAMENDEMENT, request );

            if ( nIdVa != -1 )
            {
                va = VoeuAmendementHome.findByPrimaryKey( nIdVa, getPlugin(  ) );

                _nIdFormationConseilSelected = va.getFormationConseil(  ).getIdFormationConseil(  );
                filter.setIdFormationConseil( _nIdFormationConseilSelected );
                model.put( MARK_FORMATION_CONSEIL, va.getFormationConseil(  ) );

                if ( ( va != null ) && ( va.getFascicule(  ).getSeance(  ) == null ) )
                {
                    int nIdFascicule = va.getFascicule(  ).getIdFascicule(  );
                    Fascicule f = FasciculeHome.findByPrimaryKey( nIdFascicule, getPlugin(  ) );
                    va.setFascicule( f );
                }

                int nIdSeance = va.getFascicule(  ).getSeance(  ).getIdSeance(  );
                seanceLocal = SeanceHome.findByPrimaryKey( nIdSeance, getPlugin(  ) );
                va.getFascicule(  ).setSeance( seanceLocal );

                model.put( VoeuAmendement.MARK_VOEUAMENDEMENT, va );

                if ( estGestionAval )
                {
                    filter.setTypeOrdreDuJour( TypeOrdreDuJourEnum.DEFINITIF.getId(  ) );
                    filter.setSeance( seanceLocal.getIdSeance(  ) );
                }
                else
                {
                    filter.setProchaineSeance( seanceLocal.getIdSeance(  ) );
                }

                _nIdCategorieDeliberationSelected = getCategorieDeliberation( request, filter );
                model.put( MARK_LISTE_CATEGORIE_DELIBERATION, _referenceCategorieDeliberationList );
                model.put( MARK_ID_CATEGORIE_DELIBERATION_SELECTED, _nIdCategorieDeliberationSelected );

                if ( va.getType(  ).equals( CONSTANTE_TYPE_VR ) )
                {
                	strUrlResultat = VoeuAmendementJspBean.CONSTANTE_URL_JSP_RETOUR_PDDS_IN_VOEURATTACHE;
                }
                else
                {
                	strUrlResultat = VoeuAmendementJspBean.CONSTANTE_URL_JSP_RETOUR_PDDS_IN_AMENDEMENT;
                }
            }
        }

        if ( null != request.getParameter( OdsParameters.REFERENCE ) )
        {
            strReference = request.getParameter( OdsParameters.REFERENCE ).trim(  );
            filter.setReference( strReference );
        }

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            _strDirectionSelected = getDirection( request, filter );
            filter.setType( PDD.CONSTANTE_TYPE_PROJET );

            model.put( MARK_LISTE_DIRECTION, _referenceDirectionList );
            model.put( MARK_ID_DIRECTION_SELECTED, _strDirectionSelected );
        }
        else
        {
            _strGroupeDepositaireSelected = getGroupeDepositaire( request, filter );
            filter.setType( PDD.CONSTANTE_TYPE_PROP );

            model.put( MARK_LISTE_GROUPE_DEPOSITAIRE, _referenceGroupeDepositaireList );
            model.put( MARK_ID_GROUPE_DEPOSITAIRE_SELECTED, _strGroupeDepositaireSelected );

            if ( estPourSelectionVoeuAmendement( strUrlJSP ) )
            {
                _nIdPublicationSelected = getPublication( request, filter );
            }
        }

        List<PDD> pdds = PDDHome.findByFilter( filter, getPlugin(  ) );
        List<PDD> pddASupprimer = new ArrayList<PDD>(  );

        if ( pdds != null )
        {
            // Traitement dans pour le cas des VA
            if ( estPourSelectionVoeuAmendement( strUrlJSP ) )
            {
                for ( PDD pdd : pdds )
                {
                    if ( isAlreadyInVA( pdd.getIdPdd(  ), va ) )
                    {
                        pddASupprimer.add( pdd );
                    }
                }
            }

            // Traitement pour le cas de l'OdJ
            else if ( estPourSelectionMultipleOrdreDuJour( strUrlJSP ) )
            {
                List<EntreeOrdreDuJour> listEntrees = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( odj.getIdOrdreDuJour(  ),
                        getPlugin(  ) );

                if ( listEntrees != null )
                {
                    for ( PDD pdd : pdds )
                    {
                        for ( EntreeOrdreDuJour entreeOdj : listEntrees )
                        {
                            if ( ( entreeOdj.getPdd(  ) != null ) &&
                                    ( pdd.getIdPdd(  ) == entreeOdj.getPdd(  ).getIdPdd(  ) ) )
                            {
                                pddASupprimer.add( pdd );

                                break;
                            }
                        }
                    }
                }
            }

            // Suppression de tous les PDDs contenus dans la liste "pddASupprimer"
            pdds.removeAll( pddASupprimer );
        }

        model.put( OdsMarks.MARK_GESTION_AVAL, estGestionAval );
        model.put( MARK_FONCTIONNALITE_IS_PROJET, isFonctionnaliteProjetDeDeliberation(  ) );
        model.put( MARK_LISTE_PDDS, pdds );
        model.put( OdsMarks.MARK_NOMBRE_RESULTATS, pdds.size(  ) );
        model.put( Seance.MARK_SEANCE, seanceLocal );
        model.put( OdsParameters.REFERENCE, strReference );

        model.put( MARK_URL_JSP_APPELANT, strUrlJSP );
        model.put( MARK_URL_RETOUR, strUrlResultat );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_PDD_SELECTION_MULTI, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     *
     * @param request
     *            la requête HTTP
     * @param filter
     *            PDDFilter
     * @return int l'id du Formation Conseil qui a ete choisi dans le filtre
     */
    private int getFormationConseil( HttpServletRequest request, PDDFilter filter )
    {
        int nCodeFormationConseilFilter = OdsUtils.getIntegerParameter( OdsParameters.ID_FORMATION_CONSEIL, request );

        if ( nCodeFormationConseilFilter == -1 )
        {
            if ( _nIdFormationConseilSelected != -1 )
            {
                nCodeFormationConseilFilter = _nIdFormationConseilSelected;
            }
            else
            {
                nCodeFormationConseilFilter = PDDFilter.ALL_INT;
            }
        }

        if ( ( filter != null ) && ( nCodeFormationConseilFilter != PDDFilter.ALL_INT ) )
        {
            filter.setIdFormationConseil( nCodeFormationConseilFilter );
        }

        if ( nCodeFormationConseilFilter != _nIdFormationConseilSelected )
        {
            resetPageIndex(  );
        }

        return nCodeFormationConseilFilter;
    }

    /**
     * @param request la requête HTTP
     * @param filter PDDFilter
     * @return int l'id de la Categorie de Deliberation qui a ete choisi dans le filtre
     */
    private int getCategorieDeliberation( HttpServletRequest request, PDDFilter filter )
    {
        int nCodeCategorieDeliberationFilter = OdsUtils.getIntegerParameter( OdsParameters.ID_CATEGORIE, request );

        if ( nCodeCategorieDeliberationFilter == -1 )
        {
            if ( _nIdCategorieDeliberationSelected != -1 )
            {
                nCodeCategorieDeliberationFilter = _nIdCategorieDeliberationSelected;
            }
            else
            {
                nCodeCategorieDeliberationFilter = PDDFilter.ALL_INT;
            }
        }

        if ( ( filter != null ) && ( nCodeCategorieDeliberationFilter != PDDFilter.ALL_INT ) )
        {
            filter.setCategorieDeliberation( nCodeCategorieDeliberationFilter );
        }

        if ( nCodeCategorieDeliberationFilter != _nIdCategorieDeliberationSelected )
        {
            resetPageIndex(  );
        }

        return nCodeCategorieDeliberationFilter;
    }

    /**
     * @param request la requête HTTP
     * @param filter PDDFilter
     * @return int l'id de la Categorie de Deliberation qui a ete choisi dans le filtre
     */
    private int getSeance( HttpServletRequest request, PDDFilter filter )
    {
        int nCodeSeanceFilter = OdsUtils.getIntegerParameter( OdsParameters.ID_SEANCE, request );

        if ( request.getParameter( OdsParameters.ID_SEANCE ) == null )
        {
            Seance seance = SeanceHome.getDerniereSeance( getPlugin(  ) );

            if ( seance != null )
            {
                nCodeSeanceFilter = SeanceHome.getDerniereSeance( getPlugin(  ) ).getIdSeance(  );
            }
            else
            {
                nCodeSeanceFilter = -1;
            }
        }

        if ( nCodeSeanceFilter == -1 )
        {
            if ( _nIdSeanceSelected != -1 )
            {
                nCodeSeanceFilter = _nIdSeanceSelected;
            }
            else
            {
                nCodeSeanceFilter = PDDFilter.ALL_INT;
            }
        }

        if ( nCodeSeanceFilter != PDDFilter.ALL_INT )
        {
            filter.setSeance( nCodeSeanceFilter );
        }

        if ( nCodeSeanceFilter != _nIdSeanceSelected )
        {
            resetPageIndex(  );
        }

        return nCodeSeanceFilter;
    }

    /**
     * Permet d'initialiser la liste des différents statuts
     * @param filter le filtre de statut
     * @param refList la liste de référence pour le menu déroulant des statuts
     */
    private void initRefListStatut( StatutFilter filter, ReferenceList refList )
    {
        List<Statut> listStatuts = null;

        if ( filter != null )
        {
            listStatuts = StatutHome.findStatutWithFilterList( filter, getPlugin(  ) );
        }
        else
        {
            listStatuts = StatutHome.findStatutList( getPlugin(  ) );
        }

        if ( ( listStatuts != null ) && !listStatuts.isEmpty(  ) )
        {
            for ( Statut statut : listStatuts )
            {
                refList.addItem( statut.getIdStatut(  ), statut.getLibelle(  ) );
            }
        }
    }

    /**
     *
     * @param request
     *            la requête HTTP
     * @param filter
     *            PDDFilter
     * @return String l'id de la Direction qui a ete choisi dans le filtre
     */
    private String getDirection( HttpServletRequest request, PDDFilter filter )
    {
        String strCodeDirectionFilter = request.getParameter( OdsParameters.ID_DIRECTION );

        if ( strCodeDirectionFilter == null )
        {
            if ( _strDirectionSelected != null )
            {
                strCodeDirectionFilter = _strDirectionSelected;
            }
            else
            {
                strCodeDirectionFilter = OdsConstants.FILTER_ALL;
            }
        }

        if ( ( filter != null ) && !strCodeDirectionFilter.equals( OdsConstants.FILTER_ALL ) )
        {
            filter.setDirection( strCodeDirectionFilter );
        }

        if ( !strCodeDirectionFilter.equals( _strDirectionSelected ) )
        {
            resetPageIndex(  );
        }

        return strCodeDirectionFilter;
    }

    /**
     *
     * @param request
     *            la requête HTTP
     * @param filter
     *            PDDFilter
     * @return String l'id du Groupe qui a ete choisi dans le filtre
     */
    private String getGroupeDepositaire( HttpServletRequest request, PDDFilter filter )
    {
        String strCodeGroupeDepositaireFilter = request.getParameter( OdsParameters.ID_GROUPE );

        if ( strCodeGroupeDepositaireFilter == null )
        {
            if ( _strGroupeDepositaireSelected != null )
            {
                strCodeGroupeDepositaireFilter = _strGroupeDepositaireSelected;
            }
            else
            {
                strCodeGroupeDepositaireFilter = OdsConstants.FILTER_ALL;
            }
        }

        if ( ( filter != null ) && !strCodeGroupeDepositaireFilter.equals( OdsConstants.FILTER_ALL ) )
        {
            filter.setGroupeDepositaire( strCodeGroupeDepositaireFilter );
        }

        if ( !strCodeGroupeDepositaireFilter.equals( _strGroupeDepositaireSelected ) )
        {
            resetPageIndex(  );
        }

        return strCodeGroupeDepositaireFilter;
    }

    /**
     *
     * @param request
     *            la requête HTTP
     * @param filter
     *            PDDFilter
     * @return int l'id de la valeur concernant la publication qui a ete choisi
     *         dans le filtre
     */
    private int getPublication( HttpServletRequest request, PDDFilter filter )
    {
        int nCodePublicationFilter = OdsUtils.getIntegerParameter( OdsParameters.PDD_IS_PUBLIER, request );

        if ( nCodePublicationFilter == -1 )
        {
            if ( _nIdPublicationSelected != -1 )
            {
                nCodePublicationFilter = _nIdPublicationSelected;
            }
            else
            {
                nCodePublicationFilter = PDDFilter.ALL_INT;
            }
        }

        if ( ( filter != null ) && ( nCodePublicationFilter != PDDFilter.ALL_INT ) )
        {
            filter.setPublication( nCodePublicationFilter );
        }

        if ( nCodePublicationFilter != _nIdPublicationSelected )
        {
            resetPageIndex(  );
        }

        return nCodePublicationFilter;
    }

    /**
     *
     * initialisation du numero de page pour la pagination
     */
    private void resetPageIndex(  )
    {
        _strCurrentPageIndex = CONSTANTE_PAGE_FIRST;
    }

    /**
     * Initialise tous les filtres
     *
     * @param request
     *            la requête HTTP
     */
    public void initFilter( HttpServletRequest request )
    {
        initFilter( request, false );
    }

    /**
     * Initialise tous les filtres
     * @param request la requete Http
     * @param isGestionAval si on est dans la gestion aval ou pas
     */
    public void initFilter( HttpServletRequest request, boolean isGestionAval )
    {
        initSeance( request, isGestionAval );
        initFormationConseil( request );
        initCategorieDeliberation( request );

        if ( isFonctionnaliteProjetDeDeliberation(  ) )
        {
            initDirection( request );
        }
        else
        {
            initPublication( request );
            initGroupeDepositaire( request );
        }

        if ( isGestionAval(  ) )
        {
            initStatut( request );
        }

        initInscritODJ( request );
        initModeIntro( request );
    }

    /**
     * Permet d'initialiser la liste des formation du conseil
     *
     * @param request la requête HTTP
     */
    private void initStatut( HttpServletRequest request )
    {
        int nIdStatut = OdsUtils.getIntegerParameter( OdsParameters.ID_STATUT, request );
        _nIdStatutSelected = nIdStatut;
    }

    /**
     * Permet d'initialiser la liste des formation du conseil
     *
     * @param request la requête HTTP
     */
    private void initFormationConseil( HttpServletRequest request )
    {
        int nIdFormationConseil = OdsUtils.getIntegerParameter( OdsParameters.ID_FORMATION_CONSEIL, request );

        if ( nIdFormationConseil == -1 )
        {
            List<FormationConseil> formationConseils = FormationConseilHome.findFormationConseilList( getPlugin(  ) );

            _referenceFormationConseilList = new ReferenceList(  );

            // ajout dans la referenceList de l'item chaine vide
            _referenceFormationConseilList.addItem( OdsConstants.FILTER_ALL, OdsConstants.CONSTANTE_CHAINE_VIDE );

            // ajout dans la referenceList de l'ensemble des items
            // représentant les formations du conseil
            for ( FormationConseil formationConseil : formationConseils )
            {
                _referenceFormationConseilList.addItem( formationConseil.getIdFormationConseil(  ),
                    formationConseil.getLibelle(  ) );
            }
        }

        _nIdFormationConseilSelected = nIdFormationConseil;
    }

    /**
     * Permet d'initialiser la liste des categorie de deliberations
     *
     * @param request
     *            la requête HTTP
     */
    private void initCategorieDeliberation( HttpServletRequest request )
    {
        int nIdCategorie = OdsUtils.getIntegerParameter( OdsParameters.ID_CATEGORIE, request );

        if ( nIdCategorie == -1 )
        {
            List<CategorieDeliberation> categorieDeliberations = CategorieDeliberationHome.findAllCategoriesActives( getPlugin(  ) );

            _referenceCategorieDeliberationList = new ReferenceList(  );

            // ajout dans la referenceList de l'item chaine vide
            _referenceCategorieDeliberationList.addItem( OdsConstants.FILTER_ALL, OdsConstants.CONSTANTE_CHAINE_VIDE );

            // ajout dans la referenceList de l'ensemble des items
            // représentant les categories de deliberations
            for ( CategorieDeliberation categorieDeliberation : categorieDeliberations )
            {
                _referenceCategorieDeliberationList.addItem( categorieDeliberation.getIdCategorie(  ),
                    categorieDeliberation.getLibelle(  ) );
            }
        }

        _nIdCategorieDeliberationSelected = nIdCategorie;
    }

    /**
     * Permet d'initialiser la liste des categorie des directions
     *
     * @param request
     *            la requête HTTP
     */
    public void initDirection( HttpServletRequest request )
    {
        int nIdDirection = OdsUtils.getIntegerParameter( OdsParameters.ID_DIRECTION, request );
        String strIdDirection = request.getParameter( OdsParameters.ID_DIRECTION );

        if ( ( request != null ) && ( strIdDirection != null ) && ( nIdDirection != -1 ) )
        {
            _strDirectionSelected = strIdDirection;
        }
        else
        {
            if ( ( request == null ) || ( strIdDirection == null ) )
            {
                List<Direction> directions = DirectionHome.findAllDirectionsActives( getPlugin(  ) );

                _referenceDirectionList = new ReferenceList(  );

                // ajout dans la referenceList de l'item chaine vide
                _referenceDirectionList.addItem( OdsConstants.FILTER_ALL, OdsConstants.CONSTANTE_CHAINE_VIDE );

                // ajout dans la referenceList de l'ensemble des items
                // représentant les categories de deliberations
                for ( Direction direction : directions )
                {
                    _referenceDirectionList.addItem( direction.getIdDirection(  ), direction.getLibelleLong(  ) );
                }
            }

            _strDirectionSelected = OdsConstants.FILTER_ALL;
        }
    }

    /**
     * Permet d'initialiser la liste de choix de publication
     * @param request la requête HTTP
     */
    public void initPublication( HttpServletRequest request )
    {
        _referencePublicationList = new ReferenceList(  );

        // ajout dans la referenceList de l'item chaine vide
        _referencePublicationList.addItem( PDDFilter.ALL_INT, OdsConstants.CONSTANTE_CHAINE_VIDE );
        _referencePublicationList.addItem( 0, I18nService.getLocalizedString( CONSTANTE_NON, getLocale(  ) ) );
        _referencePublicationList.addItem( 1, I18nService.getLocalizedString( CONSTANTE_OUI, getLocale(  ) ) );

        int nEstPublie = OdsUtils.getIntegerParameter( OdsParameters.PDD_IS_PUBLIER, request );

        _nIdPublicationSelected = nEstPublie;
    }

    /**
     * Permet d'initialiser la liste de choix de publication
     * @param request la requête HTTP
     */
    public void initGroupeDepositaire( HttpServletRequest request )
    {
        int nIdGroupe = OdsUtils.getIntegerParameter( OdsParameters.ID_GROUPE, request );
        String strIdGroupe = request.getParameter( OdsParameters.ID_GROUPE );

        if ( ( request != null ) && ( strIdGroupe != null ) && ( nIdGroupe != -1 ) )
        {
            _strGroupeDepositaireSelected = strIdGroupe;
        }
        else
        {
            if ( ( request == null ) || ( strIdGroupe == null ) )
            {
                List<GroupePolitique> groupePolitiques = GroupePolitiqueHome.findGroupesActifs( getPlugin(  ) );

                _referenceGroupeDepositaireList = new ReferenceList(  );

                // ajout dans la referenceList de l'item chaine vide
                _referenceGroupeDepositaireList.addItem( OdsConstants.FILTER_ALL, OdsConstants.CONSTANTE_CHAINE_VIDE );

                // ajout dans la referenceList de l'ensemble des items
                // représentant les categories de deliberations
                for ( GroupePolitique groupe : groupePolitiques )
                {
                    _referenceGroupeDepositaireList.addItem( groupe.getIdGroupe(  ), groupe.getNomGroupe(  ) );
                }
            }

            _strGroupeDepositaireSelected = OdsConstants.FILTER_ALL;
        }
    }

    /**
     * Permet d'initialiser la liste des directions coemetrices
     * @param request la requête HTTP
     */
    public void initCreationDirectionCoEmetrice( HttpServletRequest request )
    {
        _referenceDirectionCoEmetriceSelect = new ReferenceList(  );

        // _ReferenceDirectionCoEmetriceSelect.addItem( OdsConstants.FILTER_ALL,
        // OdsConstants.CONSTANTE_CHAINE_VIDE);
        if ( _referenceDirectionList != null )
        {
            for ( ReferenceItem refDir : _referenceDirectionList )
            {
                _referenceDirectionCoEmetriceSelect.addItem( refDir.getCode(  ), refDir.getName(  ) );
            }
        }

        int nIdDirection = OdsUtils.getIntegerParameter( OdsParameters.ID_DIRECTION_CO_EMETRICE, request );

        if ( nIdDirection != -1 )
        {
            _strDirectionCoEmetriceSelected = request.getParameter( OdsParameters.ID_DIRECTION_CO_EMETRICE );
        }
        else
        {
            _strDirectionCoEmetriceSelected = OdsConstants.FILTER_ALL;
        }

        ArrayList<ReferenceItem> toRemove = new ArrayList<ReferenceItem>(  );

        if ( _directionCoEmetriceAdded != null )
        {
            for ( ReferenceItem refDirSelect : _referenceDirectionCoEmetriceSelect )
            {
                for ( DirectionCoEmetrice dirCoEm : _directionCoEmetriceAdded )
                {
                    if ( refDirSelect.getCode(  )
                                         .equals( OdsConstants.CONSTANTE_CHAINE_VIDE +
                                dirCoEm.getDirection(  ).getIdDirection(  ) ) )
                    {
                        toRemove.add( refDirSelect );
                    }
                }
            }

            if ( toRemove.size(  ) > 0 )
            {
                _referenceDirectionCoEmetriceSelect.removeAll( toRemove );
                _strDirectionCoEmetriceSelected = OdsConstants.FILTER_ALL;
            }
        }
    }

    /**
     * Permet d'initialiser la liste des arrondissements
     * @param request la requête HTTP
     */
    public void initCreationArrondissement( HttpServletRequest request )
    {
        _referenceArrondissementSelect = new ReferenceList(  );

        _referenceArrondissementSelect.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

        for ( int i = 1; i <= 20; i++ )
        {
            _referenceArrondissementSelect.addItem( i, OdsConstants.CONSTANTE_CHAINE_VIDE + i );
        }

        _nIdArrondissementSelected = OdsUtils.getIntegerParameter( OdsParameters.ID_ARRONDISSEMENT, request );

        ArrayList<ReferenceItem> toRemove = new ArrayList<ReferenceItem>(  );

        if ( _arrondissementAdded != null )
        {
            for ( ReferenceItem refArrSelect : _referenceArrondissementSelect )
            {
                for ( Arrondissement arrAdded : _arrondissementAdded )
                {
                    if ( refArrSelect.getCode(  )
                                         .equals( OdsConstants.CONSTANTE_CHAINE_VIDE + arrAdded.getArrondissement(  ) ) )
                    {
                        toRemove.add( refArrSelect );
                    }
                }
            }

            if ( toRemove.size(  ) > 0 )
            {
                _referenceArrondissementSelect.removeAll( toRemove );
                _nIdArrondissementSelected = -1;
            }
        }
    }

    /**
     * Permet d'initialiser la liste de mode d'intro Les choix possibles: -
     * Introduction directe - Soumis à l'avis du conseil d'arrondissement
     * @param request la requête HTTP
     */
    public void initModeIntro( HttpServletRequest request )
    {
        _referenceModeIntroList = new ReferenceList(  );

        // ajout dans la referenceList de l'item chaine vide
        _referenceModeIntroList.addItem( CONSTANTE_MODE_INTRO, OdsConstants.CONSTANTE_CHAINE_VIDE );
        _referenceModeIntroList.addItem( CONSTANTE_INTRO_DIRECT_CODE,
            I18nService.getLocalizedString( CONSTANTE_INTRO_DIRECT_LIBELLE, getLocale(  ) ) );
        _referenceModeIntroList.addItem( CONSTANTE_SOUMIS_AVIS_CONSEIL_CODE,
            I18nService.getLocalizedString( CONSTANTE_SOUMIS_AVIS_CONSEIL_LIBELLE, getLocale(  ) ) );

        if ( ( request != null ) && ( request.getParameter( OdsParameters.ID_PDD_MODE_INTRO ) != null ) &&
                !CONSTANTE_MODE_INTRO.equals( request.getParameter( OdsParameters.ID_PDD_MODE_INTRO ) ) )
        {
            _strModeIntroSelected = OdsConstants.CONSTANTE_CHAINE_VIDE +
                request.getParameter( OdsParameters.ID_PDD_MODE_INTRO );
        }
        else
        {
            _strModeIntroSelected = CONSTANTE_MODE_INTRO;
        }
    }

    /**
     * Permet d'initialiser la liste des choix de publications Les choix
     * possibles: OUI,NON ou Pas d'importance
     * @param request la requête HTTP
     */
    public void initInscritODJ( HttpServletRequest request )
    {
        _referenceInscritODJList = new ReferenceList(  );

        // ajout dans la referenceList de l'item chaine vide
        _referenceInscritODJList.addItem( PDDFilter.ALL_INT, OdsConstants.CONSTANTE_CHAINE_VIDE );
        _referenceInscritODJList.addItem( 0, I18nService.getLocalizedString( CONSTANTE_NON, getLocale(  ) ) );
        _referenceInscritODJList.addItem( 1, I18nService.getLocalizedString( CONSTANTE_OUI, getLocale(  ) ) );

        int nIdPdd = OdsUtils.getIntegerParameter( OdsParameters.ID_PDD_INSCRIT_ODJ, request );
        _nIdInscritODJSelected = nIdPdd;
    }

    /**
     * Retourne l'object Direction a partir de l'id d'une direction passé en
     * parametre
     * @param strIdDirection String
     * @return Direction Direction
     */
    private Direction getDirectionObject( String strIdDirection )
    {
        int nIdDirection = -1;

        try
        {
            nIdDirection = Integer.parseInt( strIdDirection );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        return DirectionHome.findByPrimaryKey( nIdDirection, getPlugin(  ) );
    }

    /**
     * Retourne l'object GroupePolitique a partir de l'id d'une direction passé
     * en parametre
     * @param strIdGroupePolitique String
     * @return GroupePolitique groupe Politique
     */
    private GroupePolitique getGroupePolitiqueObject( String strIdGroupePolitique )
    {
        int nIdGroupePolitique = -1;

        if ( strIdGroupePolitique != null )
        {
            try
            {
                nIdGroupePolitique = Integer.parseInt( strIdGroupePolitique );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }
        }

        return GroupePolitiqueHome.findByPrimaryKey( nIdGroupePolitique, getPlugin(  ) );
    }

    /**
     * Retourne l'object FormationConseil a partir de l'id d'une direction passé
     * en parametre
     * @param strIdFormationConseil String
     * @return FormationConseil String
     */
    private FormationConseil getFormationConseilObject( String strIdFormationConseil )
    {
        int nIdFormationConseil = -1;

        try
        {
            nIdFormationConseil = Integer.parseInt( strIdFormationConseil );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        return FormationConseilHome.findByPrimaryKey( nIdFormationConseil, getPlugin(  ) );
    }

    /**
     * Retourne l'object CategorieDeliberation a partir de l'id d'une direction
     * passé en parametre
     * @param strCategegorieDeDeliberation String
     * @return CategorieDeliberation categorie de deliberation
     */
    private CategorieDeliberation getCategorieDeliberationObject( String strCategegorieDeDeliberation )
    {
        int nIdCategegorieDeDeliberation = -1;

        try
        {
            nIdCategegorieDeDeliberation = Integer.parseInt( strCategegorieDeDeliberation );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        return CategorieDeliberationHome.findByPrimaryKey( nIdCategegorieDeDeliberation, getPlugin(  ) );
    }

    /**
     * Permet d'initialiser la prochaine seance
     * @param request la requete Http
     * @param isGestionAval si on est dans la gestion aval ou pas
     */
    public void initSeance( HttpServletRequest request, boolean isGestionAval )
    {
        if ( ( request != null ) && !isGestionAval )
        {
            _seance = SeanceHome.getProchaineSeance( getPlugin(  ) );
        }
    }

    /**
     * Retourne le nom court du fichier contenu dans la methode getName() de
     * l'objet FileItem Par exemple, C:\temp\fichier.txt est le path du fichier.
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
     * test si le pdd d'id =nIdPdd se trouve dans la liste des pdds du VA
     *
     * @param nIdPdd
     *            int
     * @param va
     *            VoeuAmendement
     * @return boolean TRUE si le pdd d'id =nIdPdd se trouve dans la liste des
     *         pdds du VA, FALSE sinon
     */
    private boolean isAlreadyInVA( int nIdPdd, VoeuAmendement va )
    {
        for ( PDD pdd : va.getPdds(  ) )
        {
            if ( pdd.getIdPdd(  ) == nIdPdd )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * @param isFonctionnaliteProjetDeDeliberation TRUE si c'est un projet, FALSE sinon
     */
    public void setFonctionnalite( boolean isFonctionnaliteProjetDeDeliberation )
    {
        _bFonctionnaliteProjetDeDeliberation = isFonctionnaliteProjetDeDeliberation;
    }

    /**
     * @return _bFonctionnaliteProjetDeDeliberation boolean
     */
    public boolean isFonctionnaliteProjetDeDeliberation(  )
    {
        return _bFonctionnaliteProjetDeDeliberation;
    }

    /**
     *
     * @param bGestionAval TRUE si on est dans le contexte de la gestion aval, FALSE sinon
     */
    public void setGestionAval( boolean bGestionAval )
    {
        _bGestionAval = bGestionAval;
    }

    /**
     * @return TRUE si c'est en gestion aval, FALSE sinon
     */
    public boolean isGestionAval(  )
    {
        return _bGestionAval;
    }

    /**
     * Permet de savoir si il s'agit d'une demande de sélection de PDD pour Voeu/Amendement
     * @param strUrlJspAppelant l'url de la JSP source.
     * @return TRUE, si c'est pour le traitement des Voeux/amendement, FALSE sinon
     */
    private boolean estPourSelectionVoeuAmendement( String strUrlJspAppelant )
    {
        boolean bRetour = false;

        if ( strUrlJspAppelant.equals( VoeuAmendementJspBean.CONSTANTE_URL_JSP_APPELANT_PROJET ) ||
                strUrlJspAppelant.equals( VoeuAmendementJspBean.CONSTANTE_URL_JSP_APPELANT_PROJET_AVAL ) ||
                strUrlJspAppelant.equals( VoeuAmendementJspBean.CONSTANTE_URL_JSP_APPELANT_PROPOSITION ) ||
                strUrlJspAppelant.equals( VoeuAmendementJspBean.CONSTANTE_URL_JSP_APPELANT_PROPOSITION_AVAL ) )
        {
            bRetour = true;
        }

        return bRetour;
    }

    /**
     * Permet de savoir si il s'agit d'une demande de sélection de PDD pour Releve des Travaux
     * @param strUrlJspAppelant l'url de la JSP source.
     * @return TRUE, si c'est pour le traitement des Relevés de Travaux, FALSE sinon
     */
    private boolean estPourSelectionReleveDesTravaux( String strUrlJspAppelant )
    {
        boolean bRetour = false;

        if ( strUrlJspAppelant.equals( ReleveDesTravauxJspBean.CONSTANTE_URL_JSP_APPELANT_PROJET ) ||
                strUrlJspAppelant.equals( ReleveDesTravauxJspBean.CONSTANTE_URL_JSP_APPELANT_PROPOSITION ) )
        {
            bRetour = true;
        }

        return bRetour;
    }

    /**
     * Permet de savoir si il s'agit d'une demande de sélection simple de PDD pour Ordre du Jour
     * @param strUrlJspAppelant l'url de la JSP source.
     * @return TRUE, si c'est pour le traitement des ordres du jour, FALSE sinon
     */
    private boolean estPourSelectionSimpleOrdreDuJour( String strUrlJspAppelant )
    {
        boolean bRetour = false;

        if ( strUrlJspAppelant.equals( OrdreDuJourJspBean.JSP_SELECTION_PROJET ) ||
                strUrlJspAppelant.equals( OrdreDuJourJspBean.JSP_SELECTION_PROPOSITION ) )
        {
            bRetour = true;
        }

        return bRetour;
    }

    /**
     * Permet de savoir si il s'agit d'une demande de sélection multiple de PDD pour Ordre du Jour
     * @param strUrlJspAppelant l'url de la JSP source.
     * @return TRUE, si c'est pour le traitement des entrées d'Ordre du jour, FALSE sinon
     */
    private boolean estPourSelectionMultipleOrdreDuJour( String strUrlJspAppelant )
    {
        boolean bRetour = false;

        if ( strUrlJspAppelant.equals( OrdreDuJourJspBean.JSP_SELECTION_PROJETS ) ||
                strUrlJspAppelant.equals( OrdreDuJourJspBean.JSP_SELECTION_PROPOSITIONS ) )
        {
            bRetour = true;
        }

        return bRetour;
    }

    /**
     * Permet de stocker toutes les permissions afin de gérer les profils au niveau des templates
     * @param model le hashmap contenant les parametres qui vont être envoyés au template
     */
    private void ajouterPermissionsDansHashmap( Map<Object, Object> model )
    {
        /*
             * Permission de publier
             */
        boolean bPermissionPublication = true;

        if ( isFonctionnaliteProjetDeDeliberation(  ) &&
                !RBACService.isAuthorized( ProjetProchaineSeanceResourceIdService.PROJET_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, ProjetProchaineSeanceResourceIdService.PERMISSION_PUBLICATION,
                    getUser(  ) ) )
        {
            bPermissionPublication = false;
        }
        else if ( isGestionAval(  ) &&
                !RBACService.isAuthorized( PddGestionAvalResourceIdService.PDD_GESTION_AVAL,
                    RBAC.WILDCARD_RESOURCES_ID, PddGestionAvalResourceIdService.PERMISSION_PUBLICATION, getUser(  ) ) )
        {
            bPermissionPublication = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_PUBLICATION, bPermissionPublication );

        /*
         * Permission de dépublier
         */
        boolean bPermissionDepublication = true;

        if ( isFonctionnaliteProjetDeDeliberation(  ) &&
                !RBACService.isAuthorized( ProjetProchaineSeanceResourceIdService.PROJET_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, ProjetProchaineSeanceResourceIdService.PERMISSION_DEPUBLICATION,
                    getUser(  ) ) )
        {
            bPermissionDepublication = false;
        }
        else if ( isGestionAval(  ) &&
                !RBACService.isAuthorized( PddGestionAvalResourceIdService.PDD_GESTION_AVAL,
                    RBAC.WILDCARD_RESOURCES_ID, PddGestionAvalResourceIdService.PERMISSION_DEPUBLICATION, getUser(  ) ) )
        {
            bPermissionDepublication = false;
        }
        else if ( !isFonctionnaliteProjetDeDeliberation(  ) &&
                !RBACService.isAuthorized( PropositionProchaineSeanceResourceIdService.PROPOSITION_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, PropositionProchaineSeanceResourceIdService.PERMISSION_DEPUBLICATION,
                    getUser(  ) ) )
        {
            bPermissionDepublication = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_DEPUBLICATION, bPermissionDepublication );

        /*
         * Permission de créer
         */
        boolean bPermissionCreation = true;

        if ( isFonctionnaliteProjetDeDeliberation(  ) &&
                !RBACService.isAuthorized( ProjetProchaineSeanceResourceIdService.PROJET_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, ProjetProchaineSeanceResourceIdService.PERMISSION_CREATION, getUser(  ) ) )
        {
            bPermissionCreation = false;
        }
        else if ( isGestionAval(  ) )
        {
            bPermissionCreation = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_CREATION, bPermissionCreation );

        /*
         * Permission de modifier
         */
        boolean bPermissionModification = true;

        if ( !isFonctionnaliteProjetDeDeliberation(  ) &&
                !RBACService.isAuthorized( PropositionProchaineSeanceResourceIdService.PROPOSITION_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, PropositionProchaineSeanceResourceIdService.PERMISSION_MISE_A_JOUR,
                    getUser(  ) ) )
        {
            bPermissionModification = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_MODIFICATION, bPermissionModification );

        /*
         * Permission de supprimer
         */
        boolean bPermissionSuppression = true;

        if ( isFonctionnaliteProjetDeDeliberation(  ) &&
                !RBACService.isAuthorized( ProjetProchaineSeanceResourceIdService.PROJET_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, ProjetProchaineSeanceResourceIdService.PERMISSION_SUPPRESSION,
                    getUser(  ) ) )
        {
            bPermissionSuppression = false;
        }
        else if ( isGestionAval(  ) &&
                !RBACService.isAuthorized( PddGestionAvalResourceIdService.PDD_GESTION_AVAL,
                    RBAC.WILDCARD_RESOURCES_ID, PddGestionAvalResourceIdService.PERMISSION_SUPPRESSION, getUser(  ) ) )
        {
            bPermissionSuppression = false;
        }
        else if ( !isFonctionnaliteProjetDeDeliberation(  ) &&
                !RBACService.isAuthorized( PropositionProchaineSeanceResourceIdService.PROPOSITION_PROCHAINE_SEANCE,
                    RBAC.WILDCARD_RESOURCES_ID, PropositionProchaineSeanceResourceIdService.PERMISSION_SUPPRESSION,
                    getUser(  ) ) )
        {
            bPermissionSuppression = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_SUPPRESSION, bPermissionSuppression );
    }
}
