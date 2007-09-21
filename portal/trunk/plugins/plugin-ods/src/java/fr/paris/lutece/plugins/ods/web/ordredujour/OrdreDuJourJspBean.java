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
package fr.paris.lutece.plugins.ods.web.ordredujour;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jcopist.service.exception.ServerException;
import net.sf.jcopist.service.exception.UnavailableException;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.remoting.RemoteConnectFailureException;

import com.lowagie.text.DocumentException;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.commission.CommissionHome;
import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.elu.EluHome;
import fr.paris.lutece.plugins.ods.business.expression.ExpressionUsuelle;
import fr.paris.lutece.plugins.ods.business.expression.ExpressionUsuelleHome;
import fr.paris.lutece.plugins.ods.business.fascicule.Fascicule;
import fr.paris.lutece.plugins.ods.business.fascicule.FasciculeHome;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysique;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysiqueHome;
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocumentEnum;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilHome;
import fr.paris.lutece.plugins.ods.business.modeleordredujour.ModeleOrdreDuJour;
import fr.paris.lutece.plugins.ods.business.modeleordredujour.ModeleOrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.naturedossier.NatureDesDossiers;
import fr.paris.lutece.plugins.ods.business.naturedossier.NatureDesDossiersHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.EntreeOrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.EntreeOrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourFilter;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourUtils;
import fr.paris.lutece.plugins.ods.business.ordredujour.TypeOrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.TypeOrdreDuJourEnum;
import fr.paris.lutece.plugins.ods.business.ordredujour.TypeOrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.generated.entete.Entete;
import fr.paris.lutece.plugins.ods.business.ordredujour.generated.pieddepage.PiedDePage;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.Publication;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.PublicationHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.tourniquet.Tourniquet;
import fr.paris.lutece.plugins.ods.business.tourniquet.TourniquetHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementHome;
import fr.paris.lutece.plugins.ods.service.manager.DocumentManager;
import fr.paris.lutece.plugins.ods.service.role.ConstitutionOdjResourceIdService;
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
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;


/**
 * OrdreDuJourJspBean
 */
public class OrdreDuJourJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_ODJ = "ODS_ODJ";
    public static final String JSP_SELECTION_PROPOSITION = "jsp/admin/plugins/ods/projetdeliberation/PropositionDeliberationForOdj.jsp";
    public static final String JSP_SELECTION_PROJET = "jsp/admin/plugins/ods/projetdeliberation/ProjetDeliberationForOdj.jsp";
    public static final String JSP_SELECTION_PROPOSITIONS = "jsp/admin/plugins/ods/projetdeliberation/PropositionsDeliberationForOdj.jsp";
    public static final String JSP_SELECTION_PROJETS = "jsp/admin/plugins/ods/projetdeliberation/ProjetsDeliberationForOdj.jsp";
    public static final String JSP_SELECTION_ENTREE_ORDRE_DU_JOUR = "jsp/admin/plugins/ods/ordredujour/SelectionEntreeOrdreDuJour.jsp";
    public static final String JSP_SELECTION_PDD_RETOUR = "jsp/admin/plugins/ods/ordredujour/DoSelectionPdd.jsp";
    public static final String JSP_SELECTION_PDD_RETOUR_MULTI = "jsp/admin/plugins/ods/ordredujour/DoSelectionPdds.jsp";
    public static final String JSP_SELECTION_VOEU = "jsp/admin/plugins/ods/voeuamendement/VoeuNonRattacheForOdj.jsp";
    public static final String JSP_SELECTION_VOEUX = "jsp/admin/plugins/ods/voeuamendement/VoeuxNonRattachesForOdj.jsp";
    public static final String JSP_SELECTION_VOEU_RETOUR = "jsp/admin/plugins/ods/ordredujour/DoSelectionVoeuAmendement.jsp";
    public static final String JSP_AJOUT_VOEU = "jsp/admin/plugins/ods/voeuamendement/CreationVoeuNonRattacheForOdj.jsp";
    public static final String JSP_SELECTION_EXPRESSION = "jsp/admin/plugins/ods/expression/ExpressionsForOdj.jsp";
    public static final String JSP_MODIFICATION_ENTREE_ORDRE_DU_JOUR = "jsp/admin/plugins/ods/ordredujour/ModificationEntreeOrdreDuJour.jsp";
    public static final String JSP_DO_MODIFICATION_ENTREE_ORDRE_DU_JOUR = "jsp/admin/plugins/ods/ordredujour/DoModificationEntreeOrdreDuJour.jsp";
    public static final String JSP_CREATION_ENTREE_ORDRE_DU_JOUR = "jsp/admin/plugins/ods/ordredujour/CreationEntreeOrdreDuJour.jsp";
    public static final String JSP_DO_CREATION_ENTREE_ORDRE_DU_JOUR = "jsp/admin/plugins/ods/ordredujour/DoCreationEntreeOrdreDuJour.jsp";
    public static final String JSP_DO_SUPPRESSION_ENTREE_JSP = "jsp/admin/plugins/ods/ordredujour/DoSuppressionEntreeOrdreDuJour.jsp";
    public static final String JSP_DO_ANNULER_MODE_MANUEL = "jsp/admin/plugins/ods/ordredujour/DoAnnulerModeManuel.jsp";
    public static final String JSP_DO_GENERER_ADDITIF = "jsp/admin/plugins/ods/ordredujour/DoGenererAdditif.jsp";
    public static final String JSP_DO_GENERER_RECTIFICATIF = "jsp/admin/plugins/ods/ordredujour/DoGenererRectificatif.jsp";
    public static final String JSP_DO_SUPPRESSION_LISTE_ENTREE = "jsp/admin/plugins/ods/ordredujour/DoSuppressionListeEntreeOrdreDuJour.jsp";
    public static final String JSP_URL_DO_SUPPRESSION_ODJ = "jsp/admin/plugins/ods/ordredujour/DoSuppressionOrdreDuJour.jsp";
    public static final String JSP_MODIFICATION_ORDRE_DU_JOUR = "jsp/admin/plugins/ods/ordredujour/ModificationOrdreDuJour.jsp";
    public static final String CONSTANTE_ENTREE_TYPE_PDD = "P";
    public static final String CONSTANTE_ENTREE_TYPE_VOEU = "V";
    public static final String CONSTANTE_ENTREE_TYPE_TEXTE = "T";
    private static final String TEMPLATE_LISTE_ORDRES_DU_JOUR = "admin/plugins/ods/ordredujour/liste_ordres_du_jour.html";
    private static final String TEMPLATE_MODIFICATION_ORDRES_DU_JOUR = "admin/plugins/ods/ordredujour/modification_ordre_du_jour.html";
    private static final String TEMPLATE_CREATION_ENTREE_ORDRE_DU_JOUR = "admin/plugins/ods/ordredujour/creation_entree_ordre_du_jour.html";
    private static final String TEMPLATE_MODIFICATION_ENTREE_ORDRE_DU_JOUR = "admin/plugins/ods/ordredujour/modification_entree_ordre_du_jour.html";
    private static final String TEMPLATE_SELECTION_ENTREE_ORDRE_DU_JOUR = "admin/plugins/ods/ordredujour/selection_entree_ordre_du_jour.html";
    private static final String PROPERTY_LABEL_REFERENCE = "ods.entreeodj.label.reference";
    private static final String PROPERTY_LABEL_OBJET = "ods.entreeodj.label.objet";
    private static final String PROPERTY_LABEL_INTITULE = "ods.odj.modificationOdj.label.intitule";
    private static final String MARK_ENTREE_ODJ = "entree_odj";
    private static final String MARK_LISTE_FORMATION_CONSEIL = "liste_formation_conseil";
    private static final String MARK_ID_FORMATION_CONSEIL_SELECTED = "id_formation_conseil_selected";
    private static final String MARK_ID_ODJ_SELECTED = "id_odj_selected";
    private static final String MARK_LISTE_ORDRE_DU_JOUR = "liste_ordre_du_jour";
    private static final String MARK_LISTE_NOUVEL_ORDRE_DU_JOUR = "liste_nouvel_ordre_du_jour";
    private static final String MARK_LISTE_NATURE_DOSSIER = "liste_nature_dossier";
    private static final String MARK_LISTE_RAPPORTEUR = "liste_rapporteur";
    private static final String MARK_LISTE_COMMISSION = "liste_commission";
    private static final String MARK_LISTE_ID_NOUVELLE_ENTREE = "liste_nouvelle_entree";
    private static final String MESSAGE_CONFIRME_DELETE_ENTREE = "ods.entreeodj.message.confirmeSuppression";
    private static final String MESSAGE_CONFIRME_DELETE_LISTE_ENTREE = "ods.entreeodj.message.confirmeSuppressionListe";
    private static final String MESSAGE_CONFIRME_DELETE_ODJ = "ods.odj.message.confirmeSuppression";
    private static final String MESSAGE_CONFIRME_ANNULER_MODE_MANUEL = "ods.odj.message.confirmeSuppressionModeManuel";
    private static final String MESSAGE_EXISTE_PDD_PAS_PUBLIE = "ods.odj.message.pddPasPublie";
    private static final String MESSAGE_EXISTE_ELEMENT_PAS_PUBLIE = "ods.odj.message.elementPasPublie";
    private static final String MESSAGE_CANNOT_DELETE_ENTREE = "ods.entreeodj.message.suppressionImpossible";
    private static final String MESSAGE_PAS_DE_PROJET_CHOISIE = "ods.voeuamendement.message.pasdeprojetchoisie";
    private static final String MESSAGE_PAS_DE_PROPOSITION_CHOISIE = "ods.voeuamendement.message.pasdepropositionchoisie";
    private static final String MESSAGE_PAS_DE_PDD_CHOISI = "ods.entreeodj.message.pasdepddchoisi";
    private static final String MESSAGE_PAS_DE_VOEU_CHOISI = "ods.entreeodj.message.pasdevoeuchoisi";
    private static final String MESSAGE_PAS_EXPRESSION_CHOISI = "ods.entreeodj.message.pasexpressionchoisi";
    private static final String MESSAGE_PAS_ENTREE_CHOISIE = "ods.odj.message.pasentreechoisie";
    private static final String MESSAGE_IMPOSSIBLE_DE_REVENIR_MODE_AUTOMATIQUE = "ods.odj.message.impossibleModeManuel";
    private static final String MESSAGE_PROBLEME_DE_CREATION_ODJ_PDF = "ods.odj.message.problemeCreationOdjPdf";
    private static final String MESSAGE_PROBLEME_DE_CREATION_DE_LA_LIASSE = "ods.odj.message.problemeCreationLiassePdf";
    private static final String MESSAGE_NO_ENTREE_SELECTED = "ods.odj.message.noEntreeSelected";
    private static final String MESSAGE_INCOHERENCE_RAPPORTEUR_COMMISSION = "ods.odj.message.incoherenceRapporteurCommission";
    private static final String JSP_URL_DO_PUBLICATION_ODJ = "jsp/admin/plugins/ods/ordredujour/DoPublicationOrdreDuJour.jsp";
    private static final String JSP_LISTE_ORDRE_DU_JOUR = "jsp/admin/plugins/ods/ordredujour/Ordresdujour.jsp";
    private static final String CONSTANTE_TEMPLATE_XML_ODJ = "ordre_du_jour";
    private static final String CONSTANTE_TYPE_AUTOMATIQUE = "A";
    private static final String CONSTANTE_TYPE_MANUEL = "M";
    private static final String CONSTANTE_NOUVEL_ODJ = "ods.odj.liste.label.nouvelOdj";
    private static final String CONSTANTE_SESSION = "session";
    private static final String CONSTANTE_APPLIQUER = "appliquer";
    private static final String CONSTANTE_ANNULER = "annuler";
    private static final String CONSTANTE_CANCEL = "cancel";
    private static final String CONSTANTE_ENREGISTRER = "enregistrer";
    private static final String CONSTANTE_AJOUTER_RAPPORTEUR = "ajouter_rapporteur";
    private static final String CONSTANTE_AJOUTER_COMMISSION = "ajouter_commission";
    private static final String CONSTANTE_AJOUTER_PROJET = "ajouter_projet";
    private static final String CONSTANTE_AJOUTER_DES_PROJETS = "ajouter_des_projets";
    private static final String CONSTANTE_AJOUTER_PROPOSITION = "ajouter_proposition";
    private static final String CONSTANTE_AJOUTER_DES_PROPOSITIONS = "ajouter_des_propositions";
    private static final String CONSTANTE_AJOUTER_VOEU = "ajouter_voeu";
    private static final String CONSTANTE_AJOUTER_PDD = "ajouter_pdd";
    private static final String CONSTANTE_AJOUTER_DES_VOEUX = "ajouter_des_voeux";
    private static final String CONSTANTE_AJOUTER_TEXTE = "ajouter_texte";
    private static final String CONSTANTE_AJOUTER_AVANT_SELECTION = "ajouter_avant_selection";
    private static final String CONSTANTE_AJOUTER_APRES_SELECTION = "ajouter_apres_selection";
    private static final String CONSTANTE_GENERER_ADDITIF = "generer_additif";
    private static final String CONSTANTE_GENERER_RECTIFICATIF = "generer_rectificatif";
    private static final String CONSTANTE_DEPLACER_SELECTION = "deplacer_selection";
    private static final String CONSTANTE_SUPPRIMER_SELECTION = "supprimer_selection";
    private static final String CONSTANTE_SELECTION_VOEU = "selectionner_voeu";
    private static final String CONSTANTE_AJOUTER_EXPRESSION = "ajouter_expression";
    private static final String CONSTANTE_DESELECTION_PDD = "deselectionner_pdd";
    private static final String CONSTANTE_DESELECTION_VOEU = "deselectionner_voeu";
    private static final String CONSTANTE_TYPE_SUPPRESSION = "type_suppression";
    private static final String PROPERTY_COMMISSION = "ods.odj.creation.label.commission";
    private static final String CONSTANTE_TYPE_SUPPRESSION_ELU = "elu";
    private static final String CONSTANTE_TYPE_SUPPRESSION_COMMISSION = "commission";
    private static final String CONSTANTE_ESPACE = " ";
    private static final String CONSTANTE_OUVERTURE_ACCOLADE = "(";
    private static final String CONSTANTE_FERMETURE_ACCOLADE = ")";
    private static final String PROPERTY_ERE = "ods.all.label.ere";
    private static final String PROPERTY_EME = "ods.all.label.eme";
    private static final String PROPERTY_ODJ = "ods.odj.ordreDuJourDeLa";
    private static final String CONSTANTE_TIRET = "-";
    private static final String CONSTANTE_DIESE = "#";
    private static final String CONSTANTE_ENTETE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String CONSTANTE_STYLE_H2 = "2";
    private static final String CONSTANTE_STYLE_H3 = "3";
    private static final String CONSTANTE_STYLE_H4 = "4";
    private static final String CONSTANTE_GUILLEMET = "\"";
    private static final String CONSTANTE_POINT_VIRGULE = ";";
    private static final String CONSTANTE_ANTI_SLASH = "\n";
    private static final String PROPERTY_ODJ_NATURES_STYLE = "odsodj.natures.style";
    private static final String PROPERTY_ODJ_GROUPE_DE_RAPPORTEURS_STYLE = "odsodj.groupeDeRapporteurs.style";
    private static final String PROPERTY_ODJ_GROUPE_DE_COMMISSIONS_STYLE = "odsodj.groupeDeCommissions.style";
    private static final String PROPERTY_ODJ_NOM_TEMPLATE = "odsodj.nomtemplate";
    private static final String DEFAULT_ODJ_NOM_TEMPLATE = "ordre_du_jour";

    /**
     * *************************** variables de session *************************************************
     * */
    private int _nIdFormationConseilSelected = -1;
    private int _nIdOdjSelected = -1;
    private EntreeOrdreDuJour _entreeOrdreDuJourBean;
    private int _nIdPositionInsert = -1;
    private String[] _strTabIdSelect;
    private List<Integer> _listIdNouvelleEntree;

    /**
     * Permet d'initialiser la liste des différentes Natures de dossier en
     * fonction de l'id de la seance passé en parmetre
     *
     * @param nIdSeance
     *            l'id de la séance
     * @param plugin
     *            plugin
     * @return ReferenceList la liste des différentes Natures de dossier
     */
    private ReferenceList initRefListNatureDossier( Plugin plugin, int nIdSeance )
    {
        ReferenceList refListNature = new ReferenceList(  );

        List<NatureDesDossiers> listNatureDossiers = NatureDesDossiersHome.findBySeance( nIdSeance, plugin );

        // ajout dans la referenceList de l'item chaine vide
        refListNature.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

        // ajout dans la referenceList de l'ensemble des items représentant les
        // groupes politiques
        for ( NatureDesDossiers natureDesDossiers : listNatureDossiers )
        {
            refListNature.addItem( natureDesDossiers.getIdNature(  ), natureDesDossiers.getLibelleNature(  ) );
        }

        return refListNature;
    }

    /**
     * methode qui initialise la liste des élus à afficher en fonction de
     * l'entrée d'ordre du jour passée en parametre passé en parametre
     *
     * @param entree
     *            entree
     * @param plugin
     *            Plugin
     * @return ReferenceList la liste des différents elus a afficher
     */
    private ReferenceList initRefListElu( EntreeOrdreDuJour entree, Plugin plugin )
    {
        List<Elu> listElu = EluHome.findEluList( plugin );
        List<Elu> listRapporteur = entree.getElus(  );

        ReferenceList refListElu = new ReferenceList(  );
        refListElu.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

        for ( Elu elu : listElu )
        {
            // si l'elu se trouve deja dans la liste des rapporteurs on ne
            // l'affiche pas dans la combo élu
            if ( ( ( listRapporteur == null ) || ( listRapporteur.size(  ) == 0 ) ||
                    !isInTheEluList( elu.getIdElu(  ), listRapporteur ) ) && ( elu.getCommission(  ) != null ) )
            {
                // récupération de la commission de l'élu
                Commission commission = CommissionHome.findByPrimaryKey( elu.getCommission(  ).getIdCommission(  ),
                        plugin );
                elu.setCommission( commission );

                // construction pour chaque item de la liste élu du libellé à
                // afficher
                StringBuffer strLibelleElu = new StringBuffer(  );
                strLibelleElu.append( elu.getCivilite(  ) );
                strLibelleElu.append( CONSTANTE_ESPACE );
                strLibelleElu.append( elu.getPrenomElu(  ) );
                strLibelleElu.append( CONSTANTE_ESPACE );
                strLibelleElu.append( elu.getNomElu(  ) );
                strLibelleElu.append( CONSTANTE_ESPACE );
                strLibelleElu.append( CONSTANTE_OUVERTURE_ACCOLADE );
                strLibelleElu.append( elu.getCommission(  ).getNumero(  ) );

                if ( elu.getCommission(  ).getNumero(  ) == 1 )
                {
                    strLibelleElu.append( I18nService.getLocalizedString( PROPERTY_ERE, getLocale(  ) ) );
                }
                else
                {
                    strLibelleElu.append( I18nService.getLocalizedString( PROPERTY_EME, getLocale(  ) ) );
                }

                strLibelleElu.append( CONSTANTE_ESPACE );
                strLibelleElu.append( I18nService.getLocalizedString( PROPERTY_COMMISSION, getLocale(  ) ) );
                strLibelleElu.append( CONSTANTE_FERMETURE_ACCOLADE );
                refListElu.addItem( elu.getIdElu(  ), strLibelleElu.toString(  ) );
            }
        }

        return refListElu;
    }

    /**
     * methode qui initialise la liste des commissions à afficher en fonction de
     * l'entrée d'ordre du jour passée en parametre
     *
     * @param entree
     *            entree
     * @param plugin
     *            Plugin
     * @return ReferenceList la liste des commissions à afficher
     */
    private ReferenceList initRefListCommission( EntreeOrdreDuJour entree, Plugin plugin )
    {
        List<Commission> listCommission = CommissionHome.findAllCommissionsActives( plugin );
        List<Commission> listComEntree = entree.getCommissions(  );

        ReferenceList refListCommission = new ReferenceList(  );
        refListCommission.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

        for ( Commission commission : listCommission )
        {
            // si l'elu se trouve deja dans la liste des rapporteurs on ne
            // l'affiche pas dans la combo élu
            if ( ( ( listComEntree == null ) || ( listComEntree.size(  ) == 0 ) ||
                    !isInTheCommissionList( commission.getIdCommission(  ), listComEntree ) ) )
            {
                refListCommission.addItem( commission.getIdCommission(  ),
                    commission.getNumero(  ) + OdsConstants.CONSTANTE_CHAINE_VIDE );
            }
        }

        return refListCommission;
    }

    /**
     * Permet d'initialiser la liste des différents type de conseil
     *
     * @param plugin
     *            Plugin
     * @return ReferenceList la liste des différents type de conseil
     */
    private ReferenceList initRefListFormationConseil( Plugin plugin )
    {
        ReferenceList refListFormationConseil = new ReferenceList(  );

        List<FormationConseil> listFormationConseil = FormationConseilHome.findFormationConseilList( plugin );

        // ajout dans la referenceList de l'item chaine vide
        refListFormationConseil.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

        // ajout dans la referenceList de l'ensemble des items représentant les
        // groupes politiques
        for ( FormationConseil formationConseil : listFormationConseil )
        {
            refListFormationConseil.addItem( formationConseil.getIdFormationConseil(  ), formationConseil.getLibelle(  ) );
        }

        return refListFormationConseil;
    }

    /**
     * Permet d'initialiser la liste des types d'ordres du jour que peut créé
     * l'utilisateur en fonction de la seance passée en parametre
     *
     * @param plugin
     *            plugin
     * @param seance
     *            la seance pour laquelle on veut creer les différents types
     *            d'ordre du jour
     * @return la liste des ordres du jour qui peuvent etre créé
     */
    private ReferenceList initRefListNewOdj( Plugin plugin, Seance seance )
    {
        ReferenceList refListOdj = new ReferenceList(  );

        TypeOrdreDuJour typeOrdreDuJour;
        List<FormationConseil> listFormationConseil = FormationConseilHome.findFormationConseilList( plugin );
        List<Commission> listCommission = CommissionHome.findAllCommissionsActives( plugin );
        // ajout dans la referenceList de l'item chaine vide
        refListOdj.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

        // ajout dans la referenceList de l'ensemble des items représentant les
        // groupes politiques
        OrdreDuJourFilter ordreDuJourFilter;

        for ( FormationConseil formationConseil : listFormationConseil )
        {
            ordreDuJourFilter = new OrdreDuJourFilter(  );

            if ( seance != null )
            {
                ordreDuJourFilter.setIdSeance( seance.getIdSeance(  ) );
            }

            if ( formationConseil != null )
            {
                ordreDuJourFilter.setIdFormationConseil( formationConseil.getIdFormationConseil(  ) );
            }

            ordreDuJourFilter.setIdType( TypeOrdreDuJourEnum.PREVISIONNEL.getId(  ) );
            ordreDuJourFilter.setIdSauvegarde( 0 );

            if ( !OrdreDuJourHome.isAlreadyExist( ordreDuJourFilter, plugin ) )
            {
                // si l'ordre du jour previsionnel de cette formation n'existe
                // pas on l'affiche
                typeOrdreDuJour = TypeOrdreDuJourHome.findByPrimaryKey( TypeOrdreDuJourEnum.PREVISIONNEL.getId(  ),
                        plugin );

                if ( typeOrdreDuJour != null )
                {
                    refListOdj.addItem( formationConseil.getIdFormationConseil(  ) + CONSTANTE_DIESE +
                        typeOrdreDuJour.getIdTypeOrdreDuJour(  ),
                        typeOrdreDuJour.getLibelleLong(  ) + CONSTANTE_TIRET + formationConseil.getLibelle(  ) );
                }
            }
            else
            {
                //on verifie que l'ordre du jour previsionnel  a été publié
                ordreDuJourFilter.setIdPublie( 1 );

                if ( OrdreDuJourHome.isAlreadyExist( ordreDuJourFilter, plugin ) )
                {
                    //l'ordre du jour previsionnel  a été publié
                    //traitement de l'ordre du jour de type Mise a jour
                    ordreDuJourFilter.setIdPublie( OrdreDuJourFilter.ALL_INT );
                    ordreDuJourFilter.setIdType( TypeOrdreDuJourEnum.MIS_A_JOUR.getId(  ) );

                    if ( !OrdreDuJourHome.isAlreadyExist( ordreDuJourFilter, plugin ) )
                    {
                        typeOrdreDuJour = TypeOrdreDuJourHome.findByPrimaryKey( TypeOrdreDuJourEnum.MIS_A_JOUR.getId(  ),
                                plugin );

                        if ( typeOrdreDuJour != null )
                        {
                            refListOdj.addItem( formationConseil.getIdFormationConseil(  ) + CONSTANTE_DIESE +
                                typeOrdreDuJour.getIdTypeOrdreDuJour(  ),
                                typeOrdreDuJour.getLibelleLong(  ) + CONSTANTE_TIRET + formationConseil.getLibelle(  ) );
                        }
                    }
                    else
                    {
                        //traitement pour les ordre du jour de  type definitif
                        //on verifie que l'ordre du jour de mise à jour  a été publié
                        ordreDuJourFilter.setIdPublie( 1 );

                        if ( OrdreDuJourHome.isAlreadyExist( ordreDuJourFilter, plugin ) )
                        {
                            //l'ordre du jour de mise à jour a été publié
                            ordreDuJourFilter.setIdPublie( OrdreDuJourFilter.ALL_INT );
                            ordreDuJourFilter.setIdType( TypeOrdreDuJourEnum.DEFINITIF.getId(  ) );

                            if ( !OrdreDuJourHome.isAlreadyExist( ordreDuJourFilter, plugin ) )
                            {
                                typeOrdreDuJour = TypeOrdreDuJourHome.findByPrimaryKey( TypeOrdreDuJourEnum.DEFINITIF.getId(  ),
                                        plugin );

                                if ( typeOrdreDuJour != null )
                                {
                                    refListOdj.addItem( formationConseil.getIdFormationConseil(  ) + CONSTANTE_DIESE +
                                        typeOrdreDuJour.getIdTypeOrdreDuJour(  ),
                                        typeOrdreDuJour.getLibelleLong(  ) + CONSTANTE_TIRET +
                                        formationConseil.getLibelle(  ) );
                                }
                            }
                        }
                    }

                    // traitement pour les ordres du jour des commissions
                    ordreDuJourFilter.setIdType( TypeOrdreDuJourEnum.COMMISSION.getId(  ) );

                    for ( Commission commission : listCommission )
                    {
                        ordreDuJourFilter.setIdCommission( commission.getIdCommission(  ) );

                        if ( !OrdreDuJourHome.isAlreadyExist( ordreDuJourFilter, plugin ) )
                        {
                            StringBuffer strLibelle = new StringBuffer(  );
                            strLibelle.append( I18nService.getLocalizedString( PROPERTY_ODJ, getLocale(  ) ) );
                            strLibelle.append( CONSTANTE_ESPACE );
                            strLibelle.append( commission.getNumero(  ) );
                            strLibelle.append( CONSTANTE_ESPACE );

                            strLibelle.append( I18nService.getLocalizedString( 
                            			( commission.getNumero(  ) <= 1 ) ? PROPERTY_ERE : PROPERTY_EME, 
                            					getLocale(  ) ) );

                            strLibelle.append( CONSTANTE_ESPACE );
                            strLibelle.append( I18nService.getLocalizedString( PROPERTY_COMMISSION, getLocale(  ) ) );

                            strLibelle.append( CONSTANTE_TIRET );
                            strLibelle.append( formationConseil.getLibelle(  ) );
                            refListOdj.addItem( formationConseil.getIdFormationConseil(  ) + CONSTANTE_DIESE +
                                TypeOrdreDuJourEnum.COMMISSION.getId(  ) + CONSTANTE_DIESE +
                                commission.getIdCommission(  ), strLibelle.toString(  ) );
                        }
                    }
                }
            }
        }

        return refListOdj;
    }

    /**
     * retourne l'id du type de conseil sélectionné si le parametre SESSION est
     * passé dans la requette on renvoie l'id du type de conseil stocké en
     * session
     *
     * @param request
     *            request
     * @return id du type de conseil sélectionné
     */
    private int getIdFormationConseil( HttpServletRequest request )
    {
        int nIdFormationConseil = -1;

        if ( request.getParameter( OdsParameters.ID_FORMATION_CONSEIL ) != null )
        {
            try
            {
                nIdFormationConseil = Integer.parseInt( ( request.getParameter( OdsParameters.ID_FORMATION_CONSEIL ) ) );
                _nIdFormationConseilSelected = nIdFormationConseil;
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
                nIdFormationConseil = _nIdFormationConseilSelected;
            }
        }
        else
        {
            if ( null != request.getParameter( CONSTANTE_SESSION ) )
            {
                nIdFormationConseil = _nIdFormationConseilSelected;
            }
            else
            {
                // réinitialisation de la variable de session
                _nIdFormationConseilSelected = -1;
            }
        }

        return nIdFormationConseil;
    }

    /**
     * Récupère la page de gestion de l'ordre du jour
     *
     * @param request
     *            request
     * @return la page de gestion de l'ordre du jour
     */
    public String getOrdreDuJourList( HttpServletRequest request )
    {
        Plugin plugin = getPlugin(  );
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        ajouterPermissionsDansHashmap( model );

        Seance seance = SeanceHome.getProchaineSeance( plugin );
        ReferenceList refListFormationConseil = initRefListFormationConseil( plugin );
        ReferenceList refListNewOdj = initRefListNewOdj( plugin, seance );

        // initialisation du filtre
        OrdreDuJourFilter ordreDuJourFilter = new OrdreDuJourFilter(  );

        if ( seance != null )
        {
            ordreDuJourFilter.setIdSeance( seance.getIdSeance(  ) );
        }

        ordreDuJourFilter.setIdSauvegarde( 0 );
        ordreDuJourFilter.setIdFormationConseil( getIdFormationConseil( request ) );

        List<OrdreDuJour> listOrdreDuJour = OrdreDuJourHome.findOrdreDuJourList( plugin, ordreDuJourFilter, false );

        model.put( Seance.MARK_SEANCE, seance );
        model.put( MARK_LISTE_FORMATION_CONSEIL, refListFormationConseil );
        model.put( MARK_ID_FORMATION_CONSEIL_SELECTED, _nIdFormationConseilSelected );
        model.put( MARK_LISTE_NOUVEL_ORDRE_DU_JOUR, refListNewOdj );
        model.put( MARK_ID_ODJ_SELECTED, _nIdOdjSelected );
        model.put( MARK_LISTE_ORDRE_DU_JOUR, listOrdreDuJour );

        HtmlTemplate template;
        template = AppTemplateService.getTemplate( TEMPLATE_LISTE_ORDRES_DU_JOUR, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Effectue la création d'un ordre du jour à partir des informations
     * contenues dans la requête
     *
     * @param request
     *            la requête Http
     * @return l'url de gestion des ordres du jour si la requête est valide
     */
    public String doCreationOrdreDuJour( HttpServletRequest request )
    {
        Plugin plugin = getPlugin(  );
        Seance seance = SeanceHome.getProchaineSeance( plugin );
        FormationConseil formationConseil;
        TypeOrdreDuJour typeOrdreDuJour;
        Commission commission;
        OrdreDuJour newOrdreDuJour;

        if ( request.getParameter( OdsParameters.ID_NOUVEL_ODJ ) != null )
        {
            String strIdNouvelOdj = request.getParameter( OdsParameters.ID_NOUVEL_ODJ );
            int nIdFormationConseil = -1;
            int nIdTypeOrdreDuJour = -1;
            int nIdCommission = -1;

            String[] tabNouvelOdj = strIdNouvelOdj.split( CONSTANTE_DIESE );

            if ( ( strIdNouvelOdj.contains( CONSTANTE_DIESE ) ) && ( tabNouvelOdj.length == 2 ) )
            {
                try
                {
                    nIdFormationConseil = Integer.parseInt( tabNouvelOdj[0].trim(  ) );
                    nIdTypeOrdreDuJour = Integer.parseInt( tabNouvelOdj[1].trim(  ) );
                }
                catch ( NumberFormatException nfe )
                {
                    AppLogService.error( nfe );
                }
            }
            else if ( ( strIdNouvelOdj.contains( CONSTANTE_DIESE ) ) && ( tabNouvelOdj.length == 3 ) )
            {
                try
                {
                    nIdFormationConseil = Integer.parseInt( tabNouvelOdj[0].trim(  ) );
                    nIdTypeOrdreDuJour = Integer.parseInt( tabNouvelOdj[1].trim(  ) );
                    nIdCommission = Integer.parseInt( tabNouvelOdj[2].trim(  ) );
                }
                catch ( NumberFormatException nfe )
                {
                    AppLogService.error( nfe );
                }
            }
            else if ( strIdNouvelOdj.contains( CONSTANTE_DIESE ) )
            {
                return getHomeUrl( request );
            }

            if ( ( strIdNouvelOdj.contains( CONSTANTE_DIESE ) ) &&
                    ( ( nIdFormationConseil != -1 ) && ( nIdTypeOrdreDuJour != -1 ) ) )
            {
                newOrdreDuJour = new OrdreDuJour(  );
                newOrdreDuJour.setSeance( seance );

                formationConseil = new FormationConseil(  );
                formationConseil.setIdFormationConseil( nIdFormationConseil );
                newOrdreDuJour.setFormationConseil( formationConseil );

                if ( nIdCommission != -1 )
                {
                    commission = new Commission(  );
                    commission.setIdCommission( nIdCommission );
                }

                typeOrdreDuJour = new TypeOrdreDuJour(  );
                typeOrdreDuJour.setIdTypeOrdreDuJour( nIdTypeOrdreDuJour );
                newOrdreDuJour.setTypeOrdreDuJour( typeOrdreDuJour );

                if ( nIdTypeOrdreDuJour == TypeOrdreDuJourEnum.PREVISIONNEL.getId(  ) )
                {
                    // Ordre du jour previsionnel
                    newOrdreDuJour.setModeClassement( CONSTANTE_TYPE_AUTOMATIQUE );
                    newOrdreDuJour.setTourniquet( false );
                    newOrdreDuJour.setPublie( false );
                    newOrdreDuJour.setEstSauvegarde( false );

                    // récupération du modele d'ordre du jour qui s'applique
                    // au nouvel ordre du jour
                    // si le modele n'existe pas l'intitule de l'odj est
                    // "Nouvel Ordre du jour"
                    OrdreDuJourFilter ordreDuJourFilter = new OrdreDuJourFilter(  );
                    ordreDuJourFilter.setIdFormationConseil( nIdFormationConseil );
                    ordreDuJourFilter.setIdCommission( nIdCommission );
                    ordreDuJourFilter.setIdType( nIdTypeOrdreDuJour );

                    ModeleOrdreDuJour modeleOrdreDuJour = ModeleOrdreDuJourHome.findByFilter( ordreDuJourFilter, plugin );

                    if ( null == modeleOrdreDuJour )
                    {
                        newOrdreDuJour.setIntitule( I18nService.getLocalizedString( CONSTANTE_NOUVEL_ODJ, getLocale(  ) ) );
                    }
                    else
                    {
                        newOrdreDuJour.setIntitule( modeleOrdreDuJour.getTitre(  ) );
                    }

                    OrdreDuJourHome.create( newOrdreDuJour, plugin );
                }
                else if ( ( ( ( strIdNouvelOdj.contains( CONSTANTE_DIESE ) ) &&
                        ( nIdTypeOrdreDuJour == TypeOrdreDuJourEnum.MIS_A_JOUR.getId(  ) ) ) ||
                        ( nIdTypeOrdreDuJour == TypeOrdreDuJourEnum.DEFINITIF.getId(  ) ) ) )
                {
                    // creation d'un odj de type 'mise à jour' ou 'definitif'
                    // recuperation de l'ordre du jour  de type  previsionnel ou mise a jour 
                    // servant de model en fonction au type de création demandée
                    OrdreDuJourFilter ordreDuJourFilter = new OrdreDuJourFilter(  );
                    ordreDuJourFilter.setIdFormationConseil( nIdFormationConseil );
                    ordreDuJourFilter.setIdSeance( seance.getIdSeance(  ) );

                    if ( nIdTypeOrdreDuJour == TypeOrdreDuJourEnum.MIS_A_JOUR.getId(  ) )
                    {
                        //la création d'un odj de type mise a jour se fait a partir de l'odj de type previsionnel
                        ordreDuJourFilter.setIdType( TypeOrdreDuJourEnum.PREVISIONNEL.getId(  ) );
                    }
                    else
                    {
                        //la création d'un odj de type definitif se fait a partir de l'odj de type mise à jour
                        ordreDuJourFilter.setIdType( TypeOrdreDuJourEnum.MIS_A_JOUR.getId(  ) );
                    }

                    OrdreDuJour ordreDuJourModel = OrdreDuJourHome.findByOdjFilter( ordreDuJourFilter, plugin );

                    if ( ordreDuJourModel == null )
                    {
                        return getHomeUrl( request );
                    }

                    // le mode de classement est celui de l’ordre du jour
                    // servant de model
                    // le tourniquet s’applique s’il s’applique à l’ordre du
                    // jour servant de model
                    newOrdreDuJour.setModeClassement( ordreDuJourModel.getModeClassement(  ) );
                    newOrdreDuJour.setTourniquet( ordreDuJourModel.getTourniquet(  ) );
                    newOrdreDuJour.setPublie( false );
                    newOrdreDuJour.setEstSauvegarde( false );

                    // récupération du modele d'ordre du jour qui s'applique
                    // au nouvel ordre du jour
                    // si le modele n'existe pas l'intitule de l'odj est
                    // "Nouvel Ordre du jour"
                    ordreDuJourFilter.setIdType( nIdTypeOrdreDuJour );

                    ModeleOrdreDuJour modeleOrdreDuJour = ModeleOrdreDuJourHome.findByFilter( ordreDuJourFilter, plugin );

                    if ( null == modeleOrdreDuJour )
                    {
                        newOrdreDuJour.setIntitule( I18nService.getLocalizedString( CONSTANTE_NOUVEL_ODJ, getLocale(  ) ) );
                    }
                    else
                    {
                        newOrdreDuJour.setIntitule( modeleOrdreDuJour.getTitre(  ) );
                    }

                    newOrdreDuJour.setIdOrdreDuJour( OrdreDuJourHome.create( newOrdreDuJour, plugin ) );

                    // on recupere l'id de l' ordre du jour créé
                    // A la création, les entrées de l’ordre du jour mis à
                    // jour sont les entrées de
                    // l’ordre du jour prévisionnel.
                    List<EntreeOrdreDuJour> listEntreOrdreDuJour = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJourModel.getIdOrdreDuJour(  ),
                            plugin );
                    int nIdNewEntree;

                    for ( EntreeOrdreDuJour entree : listEntreOrdreDuJour )
                    {
                        entree.setOrdreDuJour( newOrdreDuJour );
                        nIdNewEntree = EntreeOrdreDuJourHome.create( entree, plugin );

                        if ( entree.getCommissions(  ) != null )
                        {
                            // insertion des commissions de l'entrée
                            for ( Commission commissionEntree : entree.getCommissions(  ) )
                            {
                                EntreeOrdreDuJourHome.insertCommission( nIdNewEntree,
                                    commissionEntree.getIdCommission(  ), plugin );
                            }
                        }

                        if ( entree.getElus(  ) != null )
                        {
                            // insertion des rapporteurs
                            for ( Elu elu : entree.getElus(  ) )
                            {
                                EntreeOrdreDuJourHome.insertRapporteur( nIdNewEntree, elu.getIdElu(  ), plugin );
                            }
                        }
                    }
                }
                else if ( ( strIdNouvelOdj.contains( CONSTANTE_DIESE ) ) &&
                        ( ( nIdTypeOrdreDuJour == TypeOrdreDuJourEnum.COMMISSION.getId(  ) ) && ( nIdCommission != -1 ) ) )
                {
                    // Ordre du jour des commissions

                    // recuperation de l'ordre du jour previsionnel par
                    // l'intremediare du filtre
                    OrdreDuJourFilter ordreDuJourFilter = new OrdreDuJourFilter(  );
                    ordreDuJourFilter.setIdFormationConseil( nIdFormationConseil );
                    ordreDuJourFilter.setIdSeance( seance.getIdSeance(  ) );
                    ordreDuJourFilter.setIdType( TypeOrdreDuJourEnum.PREVISIONNEL.getId(  ) );

                    OrdreDuJour ordreDuJourPrev = OrdreDuJourHome.findByOdjFilter( ordreDuJourFilter, plugin );

                    if ( ordreDuJourPrev == null )
                    {
                        return getHomeUrl( request );
                    }

                    // le mode de classement est celui de l’ordre du jour
                    // prévisionnel
                    // le tourniquet ne s'applique pas
                    newOrdreDuJour.setModeClassement( ordreDuJourPrev.getModeClassement(  ) );
                    newOrdreDuJour.setTourniquet( false );
                    newOrdreDuJour.setPublie( false );
                    newOrdreDuJour.setEstSauvegarde( false );
                    // Commission de l'ordre du jour
                    commission = new Commission(  );
                    commission.setIdCommission( nIdCommission );
                    newOrdreDuJour.setCommission( commission );
                    // récupération du modele d'ordre du jour qui s'applique
                    // au nouvel ordre du jour
                    // si le modele n'existe pas l'intitule de l'odj est
                    // "Nouvel Ordre du jour"
                    ordreDuJourFilter.setIdType( nIdTypeOrdreDuJour );
                    ordreDuJourFilter.setIdCommission( nIdCommission );

                    ModeleOrdreDuJour modeleOrdreDuJour = ModeleOrdreDuJourHome.findByFilter( ordreDuJourFilter, plugin );

                    if ( null == modeleOrdreDuJour )
                    {
                        newOrdreDuJour.setIntitule( I18nService.getLocalizedString( CONSTANTE_NOUVEL_ODJ, getLocale(  ) ) );
                    }
                    else
                    {
                        newOrdreDuJour.setIntitule( modeleOrdreDuJour.getTitre(  ) );
                    }

                    newOrdreDuJour.setIdOrdreDuJour( OrdreDuJourHome.create( newOrdreDuJour, plugin ) );

                    // on recupere l'id de l' ordre du jour créé
                    // Les entrées de l’ordre du jour de la commission
                    // sont les entrées de l’ordre du jour prévisionnel dont
                    // au moins une des commissions
                    // est cette commission
                    List<EntreeOrdreDuJour> listEntreOrdreDuJour = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJourPrev.getIdOrdreDuJour(  ),
                            plugin );
                    int nIdNewEntree;

                    for ( EntreeOrdreDuJour entree : listEntreOrdreDuJour )
                    {
                        if ( isInTheCommissionList( nIdCommission, entree.getCommissions(  ) ) )
                        {
                            entree.setOrdreDuJour( newOrdreDuJour );
                            nIdNewEntree = EntreeOrdreDuJourHome.create( entree, plugin );

                            if ( entree.getCommissions(  ) != null )
                            {
                                // insertion de la commission de l'entrée
                                    EntreeOrdreDuJourHome.insertCommission( nIdNewEntree,
                                        nIdCommission, plugin );
                             }

                            if ( entree.getElus(  ) != null )
                            {
                                // insertion des rapporteurs de la commission
                                for ( Elu elu : entree.getElus(  ) )
                                {
                                    if(elu.getCommission().getIdCommission()==nIdCommission)
                                    {
                                    	EntreeOrdreDuJourHome.insertRapporteur( nIdNewEntree, elu.getIdElu(  ), plugin );
                                
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return AppPathService.getBaseUrl( request ) + JSP_LISTE_ORDRE_DU_JOUR + '?' + CONSTANTE_SESSION + '=' +
        CONSTANTE_SESSION;
    }

    /**
     * Retourne l'interface de modification d'un ordre du jour.
     *
     * @param request
     *            la requête Http
     * @return l'interface de modification d'un ordre du jour
     */
    public String getModificationOrdreDuJour( HttpServletRequest request )
    {
        Plugin plugin = getPlugin(  );
        Seance seance = SeanceHome.getProchaineSeance( plugin );
        HashMap<String, Object> model = new HashMap<String, Object>(  );

        if ( request.getParameter( OdsParameters.ID_ODJ ) != null )
        {
            int nIdOdj;

            try
            {
                nIdOdj = Integer.parseInt( request.getParameter( OdsParameters.ID_ODJ ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getOrdreDuJourList( request );
            }

            OrdreDuJour ordreDuJour = OrdreDuJourHome.findByPrimaryKey( nIdOdj, plugin );

            if ( ordreDuJour == null )
            {
                return getOrdreDuJourList( request );
            }

            // récupération de la liste des entrées associées à l'odj
            List<EntreeOrdreDuJour> listEntreeOrdreDuJour = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJour.getIdOrdreDuJour(  ),
                    plugin );
            ordreDuJour.setListEntrees( listEntreeOrdreDuJour );

            // Les vœux et amendements rattachés à un PDD ne sont affichés que
            // si l’ordre du jour est définitif.
            // on recupere les voeux et amendements associés au pdd
            if ( ordreDuJour.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) == TypeOrdreDuJourEnum.DEFINITIF.getId(  ) )
            {
                for ( EntreeOrdreDuJour entreeOrdreDuJour : ordreDuJour.getListEntrees(  ) )
                {
                    if ( entreeOrdreDuJour.getPdd(  ) != null )
                    {
                        entreeOrdreDuJour.setPdd( PDDHome.findByPrimaryKey( entreeOrdreDuJour.getPdd(  ).getIdPdd(  ),
                                plugin ) );
                    }
                }
            }

            ordreDuJour.setSeance( seance );

            if ( ( _listIdNouvelleEntree != null ) && ( _listIdNouvelleEntree.size(  ) != 0 ) )
            {
                model.put( MARK_LISTE_ID_NOUVELLE_ENTREE, _listIdNouvelleEntree );
                _listIdNouvelleEntree = null;
            }

            model.put( Seance.MARK_SEANCE, seance );
            model.put( OrdreDuJour.MARK_ODJ, ordreDuJour );

            Entete entete = OrdreDuJourUtils.getEntete( ordreDuJour, plugin );
            OrdreDuJourUtils.replaceDate( entete, seance );

            PiedDePage piedDePage = OrdreDuJourUtils.getPiedDePage( ordreDuJour, plugin );
            OrdreDuJourUtils.replaceDate( piedDePage, seance );
            model.put( OrdreDuJourUtils.TAG_ENTETE, entete );
            model.put( OrdreDuJourUtils.TAG_PIED_DE_PAGE, piedDePage );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_ORDRES_DU_JOUR,
                    getLocale(  ), model );

            return getAdminPage( template.getHtml(  ) );
        }

        return getOrdreDuJourList( request );
    }

    /**
     * Retourne l'ordre du jour sous format PDF ou Word
     * @param request la requete Http
     * @param response la reponse
     * @return l'ordre du jour sous format PDF ou Word
     */
    public String doVisualisationODJ( HttpServletRequest request, HttpServletResponse response )
    {
        String strIdODJ = request.getParameter( OdsParameters.ID_ODJ );
        String strTypeConversion = request.getParameter( OdsParameters.TYPE );
        Plugin plugin = getPlugin(  );

        OrdreDuJour ordreDuJour = null;

        if ( ( strIdODJ != null ) && ( strTypeConversion != null ) )
        {
            try
            {
                int nIdODJ = Integer.parseInt( strIdODJ );
                ordreDuJour = OrdreDuJourHome.findByPrimaryKey( nIdODJ, plugin );

                if ( ordreDuJour.getModeClassement(  ).equals( CONSTANTE_TYPE_AUTOMATIQUE ) )
                {
                    // on insere les ruptures si on est en mode automatique
                    insertRuptureInTheOdjList( ordreDuJour, plugin, false );
                }
                else
                {
                    //sinon on recupere la liste des entrées en base
                    List<EntreeOrdreDuJour> listEntrees = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJour.getIdOrdreDuJour(  ),
                            plugin );
                    ordreDuJour.setListEntrees( listEntrees );
                }

                //si c'est un ordre du jour definitif on recupere les vas rattachés au pdd
                if ( ordreDuJour.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) == TypeOrdreDuJourEnum.DEFINITIF.getId(  ) )
                {
                    PDD pdd;

                    for ( EntreeOrdreDuJour entree : ordreDuJour.getListEntrees(  ) )
                    {
                        if ( entree.getPdd(  ) != null )
                        {
                            pdd = PDDHome.findByPrimaryKey( entree.getPdd(  ).getIdPdd(  ), plugin );
                            entree.setPdd( pdd );
                        }
                    }
                }

                Seance seance = SeanceHome.findByPrimaryKey( ordreDuJour.getSeance(  ).getIdSeance(  ), plugin );
                ordreDuJour.setSeance( seance );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            if ( ordreDuJour != null )
            {
                try
                {
                    Entete entete = OrdreDuJourUtils.getEntete( ordreDuJour, plugin );
                    PiedDePage piedDePage = OrdreDuJourUtils.getPiedDePage( ordreDuJour, plugin );

                    if ( entete != null )
                    {
                        ordreDuJour.setXmlEntete( OrdreDuJourUtils.getXmlEntete( entete, ordreDuJour.getSeance(  ) ) );
                    }

                    if ( piedDePage != null )
                    {
                        ordreDuJour.setXmlPiedDePage( OrdreDuJourUtils.getXmlPiedDePage( piedDePage,
                                ordreDuJour.getSeance(  ) ) );
                    }

                    String strXml = ordreDuJour.getXml( request );
                    //on réinitialise l'entete et le pied de page a null
                    ordreDuJour.setXmlEntete( null );
                    ordreDuJour.setXmlPiedDePage( null );

                    byte[] xml = ( CONSTANTE_ENTETE_XML + strXml ).getBytes( OdsConstants.UTF8 );
                    byte[] bConvertedDoc = null;
                    
                    if ( strTypeConversion.trim(  ).equals( DocumentManager.PDF_CONVERSION ) )
                    {
                    	bConvertedDoc = DocumentManager.convert( request, DocumentManager.PDF_CONVERSION, CONSTANTE_TEMPLATE_XML_ODJ,
                                xml );
                    }
                    else if ( strTypeConversion.trim(  ).equals( DocumentManager.PDF_WORD ) )
                    {
                    	bConvertedDoc = DocumentManager.convert( request, DocumentManager.PDF_WORD, CONSTANTE_TEMPLATE_XML_ODJ,
                                xml );
                    }
          
                    
                    
                    if( bConvertedDoc != null && bConvertedDoc.length > 0)
                    {
                    	OutputStream os = response.getOutputStream(  );
                        if ( strTypeConversion.trim(  ).equals( DocumentManager.PDF_CONVERSION ) )
                        {
                            String strFileName = ordreDuJour.getIntitule(  ) + ".pdf";
                            response.setHeader( "Content-Disposition", "attachment ;filename=\"" + strFileName + "\"" );
                            response.setContentType( "application/pdf" );
                            response.setHeader( "Pragma", "public" );
                            response.setHeader( "Expires", "0" );
                            response.setHeader( "Cache-Control", "must-revalidate,post-check=0,pre-check=0" );

                        }
                        else if ( strTypeConversion.trim(  ).equals( DocumentManager.PDF_WORD ) )
                        {
                            String strFileName = ordreDuJour.getIntitule(  ) + ".doc";
                            response.setHeader( "Content-Disposition", "attachment ;filename=\"" + strFileName + "\"" );
                            response.setContentType( "application/msword" );
                            response.setHeader( "Pragma", "public" );
                            response.setHeader( "Expires", "0" );
                            response.setHeader( "Cache-Control", "must-revalidate,post-check=0,pre-check=0" );                            
                        }
                        os.write(bConvertedDoc);
                        
                    	os.close(  );
                    }
                    else 
                    {
                    	return AdminMessageService.getMessageUrl(request, "Problème lors de la conversion",AdminMessage.TYPE_STOP);
                    }
                    
                }
                catch ( Exception e )
                {
                    e.printStackTrace(  );
                    return AdminMessageService.getMessageUrl(request, "Problème lors de la conversion",AdminMessage.TYPE_STOP);
                }
            }
        }

        return getModificationOrdreDuJour( request );
    }

    /**
     * Retourne l'ordre du jour sous format csv
     * @param request la requete Http
     * @param response la reponse
     */
    public void doVisualisationODJcsv( HttpServletRequest request, HttpServletResponse response )
    {
        String strIdODJ = request.getParameter( OdsParameters.ID_ODJ );
        Plugin plugin = getPlugin(  );

        OrdreDuJour ordreDuJour = null;

        if ( ( strIdODJ != null ) )
        {
            try
            {
                int nIdODJ = Integer.parseInt( strIdODJ );
                ordreDuJour = OrdreDuJourHome.findByPrimaryKey( nIdODJ, plugin );

                //sinon on recupere la liste des entrées en base
                List<EntreeOrdreDuJour> listEntrees = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJour.getIdOrdreDuJour(  ),
                        plugin );
                ordreDuJour.setListEntrees( listEntrees );

                Seance seance = SeanceHome.findByPrimaryKey( ordreDuJour.getSeance(  ).getIdSeance(  ), plugin );
                ordreDuJour.setSeance( seance );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            if ( ordreDuJour != null )
            {
                StringBuffer strDonnees = new StringBuffer(  );
                String strSeance = OdsUtils.getDateConseil( ordreDuJour.getSeance(  ) );

                for ( EntreeOrdreDuJour entree : ordreDuJour.getListEntrees(  ) )
                {
                    //on ne recupere que les entrees de type pdd ou un voeu
                    if ( !entree.getType(  ).equals( CONSTANTE_ENTREE_TYPE_TEXTE ) )
                    {
                        strDonnees.append( CONSTANTE_GUILLEMET );
                        strDonnees.append( strSeance );
                        strDonnees.append( CONSTANTE_GUILLEMET );
                        strDonnees.append( CONSTANTE_POINT_VIRGULE );
                        strDonnees.append( CONSTANTE_GUILLEMET );

                        if ( entree.getType(  ).equals( CONSTANTE_ENTREE_TYPE_VOEU ) )
                        {
                            strDonnees.append( entree.getVoeuAmendement(  ).getReferenceComplete(  ) );
                        }
                        else
                        {
                            strDonnees.append( entree.getReference(  ) );
                        }

                        strDonnees.append( CONSTANTE_GUILLEMET );
                        strDonnees.append( CONSTANTE_POINT_VIRGULE );
                        strDonnees.append( CONSTANTE_GUILLEMET );

                        if ( entree.getType(  ).equals( CONSTANTE_ENTREE_TYPE_PDD ) )
                        {
                            if ( ( entree.getPdd(  ) != null ) &&
                            		( entree.getPdd(  ).isDelegationsServices(  ) ) )
                            {
                                strDonnees.append( PDD.CONSTANTE_TYPE_DSP );
                            }
                            else if ( entree.getPdd(  ) != null )
                            {
                                strDonnees.append( entree.getPdd(  ).getTypePdd(  ) );
                            }
                            else
                            {
                                strDonnees.append( PDD.CONSTANTE_TYPE_PROJET );
                            }
                        }
                        else
                        {
                            strDonnees.append( entree.getType(  ) );
                        }

                        strDonnees.append( CONSTANTE_GUILLEMET );
                        strDonnees.append( CONSTANTE_POINT_VIRGULE );
                        strDonnees.append( CONSTANTE_GUILLEMET );
                        strDonnees.append( OrdreDuJourUtils.getLibelleGroupeRapporteurs( entree.getElus(  ),
                                getLocale(  ) ) );
                        strDonnees.append( CONSTANTE_GUILLEMET );
                        strDonnees.append( CONSTANTE_POINT_VIRGULE );
                        //fin de ligne
                        strDonnees.append( CONSTANTE_ANTI_SLASH );
                    }
                }

                response.setContentType( "enctype=multipart/form-data" );
                response.setHeader( "Content-Disposition", "attachment; filename=\"odj.csv \";" );

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
        }
    }

    /**
     * Effectue la modification d'un ordre du jour à partir des informations
     * contenues dans la requête
     *
     * @param request
     *            la requête Http
     * @return l'url de gestion des ordres du jour
     */
    public String doModificationOrdreDuJour( HttpServletRequest request )
    {
        Plugin plugin = getPlugin(  );

        if ( ( request.getParameter( CONSTANTE_ANNULER ) == null ) &&
                ( request.getParameter( OdsParameters.ID_ODJ ) != null ) )
        {
            int nIdOdj;

            try
            {
                nIdOdj = Integer.parseInt( request.getParameter( OdsParameters.ID_ODJ ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getHomeUrl( request );
            }

            if ( ( null != request.getParameter( OdsParameters.TITRE ) ) &&
                    !request.getParameter( OdsParameters.TITRE ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                OrdreDuJour ordreDuJour = OrdreDuJourHome.findByPrimaryKey( nIdOdj, plugin );
                ordreDuJour.setIntitule( request.getParameter( OdsParameters.TITRE ) );
                OrdreDuJourHome.update( ordreDuJour, plugin );
            }
            else
            {
                Object[] messagesArgs = { I18nService.getLocalizedString( PROPERTY_LABEL_INTITULE, getLocale(  ) ) };

                return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                    messagesArgs, AdminMessage.TYPE_STOP );
            }

            if ( null != request.getParameter( CONSTANTE_APPLIQUER ) )
            {
                return getJspModificationOdj( request, nIdOdj );
            }
        }

        return AppPathService.getBaseUrl( request ) + JSP_LISTE_ORDRE_DU_JOUR + '?' + CONSTANTE_SESSION + '=' +
        CONSTANTE_SESSION;
    }

    /**
     * Retourne l'interface de confirmation de la suppression de l'ordre du jour
     *
     * @param request
     *            la requête Http
     * @return l'interface de confirmation de la suppression de l'ordre du jour
     */
    public String getSuppressionOrdreDuJour( HttpServletRequest request )
    {
        /*
         * GESTION DES PROFILS
         * -------------------
         * Si l'utilisateur n'est pas autorisé a effectuer cette fonction,
         *  il est renvoyé vers une page lui indiquant qu'il n'a pas les droits nécessaires.
         */
        if ( !RBACService.isAuthorized( ConstitutionOdjResourceIdService.CONSTITUTION_ODJ, RBAC.WILDCARD_RESOURCES_ID,
                    ConstitutionOdjResourceIdService.PERMISSION_SUPPRESSION, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }

        if ( request.getParameter( OdsParameters.ID_ODJ ) == null )
        {
            return getHomeUrl( request );
        }

        String strIdOdj = request.getParameter( OdsParameters.ID_ODJ );
        UrlItem url = new UrlItem( JSP_URL_DO_SUPPRESSION_ODJ );
        url.addParameter( OdsParameters.ID_ODJ, strIdOdj );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRME_DELETE_ODJ, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Effectue la suppression d'un ordre du jour à partir des informations
     * contenues dans la requête
     *
     * @param request
     *            la requête Http
     * @return l'url de gestion des ordres du jour
     */
    public String doSuppressionOrdreDuJour( HttpServletRequest request )
    {
        /*
         * GESTION DES PROFILS
         * -------------------
         * Si l'utilisateur n'est pas autorisé a effectuer cette fonction,
         *  il est renvoyé vers une page lui indiquant qu'il n'a pas les droits nécessaires.
         */
        if ( !RBACService.isAuthorized( ConstitutionOdjResourceIdService.CONSTITUTION_ODJ, RBAC.WILDCARD_RESOURCES_ID,
                    ConstitutionOdjResourceIdService.PERMISSION_SUPPRESSION, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }

        if ( null != request.getParameter( OdsParameters.ID_ODJ ) )
        {
            OrdreDuJour ordreDuJour;
            Plugin plugin = getPlugin(  );

            try
            {
                ordreDuJour = OrdreDuJourHome.findByPrimaryKey( Integer.parseInt( request.getParameter( 
                                OdsParameters.ID_ODJ ) ), plugin );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getHomeUrl( request );
            }

            if ( !ordreDuJour.getPublie(  ) )
            {
                List<EntreeOrdreDuJour> entreeOrdreDuJours = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJour.getIdOrdreDuJour(  ),
                        plugin );

                try
                {
                    // suppression de toutes les entrées d'ordre du jour
                    for ( EntreeOrdreDuJour entreeOrdreDuJour : entreeOrdreDuJours )
                    {
                        // suppression des entrees liées a l'ordre du jour
                        EntreeOrdreDuJourHome.remove( entreeOrdreDuJour, plugin );
                    }

                    // suppression de l'ordre du jour
                    OrdreDuJourHome.remove( ordreDuJour, plugin );

                    //si l'ordre du jour possede une sauvegarde on la supprime aussi
                    if ( ordreDuJour.getOrdreDuJourSauveGarde(  ) != null )
                    {
                        entreeOrdreDuJours = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJour.getOrdreDuJourSauveGarde(  )
                                                                                                              .getIdOrdreDuJour(  ),
                                plugin );

                        // suppression de toutes les entrées de la sauvegarde d'ordre du jour 
                        for ( EntreeOrdreDuJour entreeOrdreDuJour : entreeOrdreDuJours )
                        {
                            // suppression des entrees liées a l'ordre du jour
                            EntreeOrdreDuJourHome.remove( entreeOrdreDuJour, plugin );
                        }

                        //	 suppression de la sauvegarde d'ordre du jour
                        OrdreDuJourHome.remove( ordreDuJour.getOrdreDuJourSauveGarde(  ), plugin );
                    }
                }
                catch ( AppException ae )
                {
                    AppLogService.error( ae );

                    if ( ae.getInitialException(  ) instanceof SQLException )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_ENTREE,
                            AdminMessage.TYPE_STOP );
                    }
                }
            }
        }

        return AppPathService.getBaseUrl( request ) + JSP_LISTE_ORDRE_DU_JOUR + '?' + CONSTANTE_SESSION + '=' +
        CONSTANTE_SESSION;
    }

    /**
     * Retourne l'interface de confirmation de publication si au moins un pdd
     * rattaché à l'ordre du jour n'est pas publié sinon redirection vers le jsp
     * de publication
     *
     * @param request
     *            la requête Http
     * @return l'interface de confirmation de publication si au moins un pdd
     *         rattaché à l'ordre du jour n'est pas publié sinon redirection
     *         vers le jsp de publication
     */
    public String getPublicationOrdreDuJour( HttpServletRequest request )
    {
        if ( request.getParameter( OdsParameters.ID_ODJ ) != null )
        {
            OrdreDuJour ordreDuJour;
            Plugin plugin = getPlugin(  );

            try
            {
                ordreDuJour = OrdreDuJourHome.findByPrimaryKey( Integer.parseInt( request.getParameter( 
                                OdsParameters.ID_ODJ ) ), plugin );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getHomeUrl( request );
            }

            if ( !ordreDuJour.getPublie(  ) )
            {
                List<EntreeOrdreDuJour> entreeOrdreDuJours = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJour.getIdOrdreDuJour(  ),
                        plugin );

                for ( EntreeOrdreDuJour entreeOrdreDuJour : entreeOrdreDuJours )
                {
                    if ( entreeOrdreDuJour.getPdd(  ) != null )
                    {
                        UrlItem url = new UrlItem( JSP_URL_DO_PUBLICATION_ODJ );
                        url.addParameter( OdsParameters.ID_ODJ, ordreDuJour.getIdOrdreDuJour(  ) );

                        if ( !entreeOrdreDuJour.getPdd(  ).isEnLigne(  ) )
                        {
                            // Si tous les PDD présents dans l’ordre du jour n’ont pas été
                            // mis en ligne alors un message d’avertissement est affiché
                            return AdminMessageService.getMessageUrl( request, MESSAGE_EXISTE_PDD_PAS_PUBLIE,
                                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
                        }
                        else if ( ordreDuJour.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) == TypeOrdreDuJourEnum.DEFINITIF.getId(  ) )
                        {
                            //si les vas non rattachés au pdd ne sont tous  publiés
                            PDD pdd = PDDHome.findByPrimaryKey( entreeOrdreDuJour.getPdd(  ).getIdPdd(  ), plugin );

                            for ( VoeuAmendement voeuAmendement : pdd.getVoeuxAmendements(  ) )
                            {
                                if ( !voeuAmendement.getEnLigne(  ) )
                                {
                                    return AdminMessageService.getMessageUrl( request,
                                        MESSAGE_EXISTE_ELEMENT_PAS_PUBLIE, url.getUrl(  ),
                                        AdminMessage.TYPE_CONFIRMATION );
                                }
                            }
                        }
                    }
                    else if ( ( ordreDuJour.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) == TypeOrdreDuJourEnum.DEFINITIF.getId(  ) ) &&
                            ( entreeOrdreDuJour.getVoeuAmendement(  ) != null ) &&
                            !entreeOrdreDuJour.getVoeuAmendement(  ).getEnLigne(  ) )
                    {
                        //si le va non  rattaché n'est pas publié
                        UrlItem url = new UrlItem( JSP_URL_DO_PUBLICATION_ODJ );

                        return AdminMessageService.getMessageUrl( request, MESSAGE_EXISTE_ELEMENT_PAS_PUBLIE,
                            url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
                    }
                }

                return AppPathService.getBaseUrl( request ) + JSP_URL_DO_PUBLICATION_ODJ + "?" + OdsParameters.ID_ODJ +
                "=" + ordreDuJour.getIdOrdreDuJour(  );
            }
        }

        return getHomeUrl( request );
    }

    /**
     * Effectue la publication d'un ordre du jour à partir de l'id de l'odj
     * contenu dans la requette
     *
     * @param request
     *            la requête Http
     * @return l'url de gestion des ordres du jour si la requête est valide, un
     *         message d'erreur sinon
     */
    public String doPublicationOrdreDuJour( HttpServletRequest request )
    {
        if ( null != request.getParameter( OdsParameters.ID_ODJ ) )
        {
            Timestamp dateForVersion = new Timestamp( System.currentTimeMillis(  ) );
            OrdreDuJour ordreDuJour;
            Plugin plugin = getPlugin(  );
            Locale locale = getLocale(  );
            byte[] tabXmlOdj;

            try
            {
                ordreDuJour = OrdreDuJourHome.findByPrimaryKey( Integer.parseInt( request.getParameter( 
                                OdsParameters.ID_ODJ ) ), plugin );

                if ( ordreDuJour.getModeClassement(  ).equals( CONSTANTE_TYPE_AUTOMATIQUE ) )
                {
                    // on insere les ruptures si on est en mode automatique
                    insertRuptureInTheOdjList( ordreDuJour, plugin, false );
                }
                else
                {
                    //sinon on recupere la liste des entrées en base
                    List<EntreeOrdreDuJour> listEntrees = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJour.getIdOrdreDuJour(  ),
                            plugin );
                    ordreDuJour.setListEntrees( listEntrees );
                }

                //si c'est un ordre du jour definitif on recupere les vas rattachés au pdd
                if ( ordreDuJour.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) == TypeOrdreDuJourEnum.DEFINITIF.getId(  ) )
                {
                    PDD pdd;

                    for ( EntreeOrdreDuJour entree : ordreDuJour.getListEntrees(  ) )
                    {
                        if ( entree.getPdd(  ) != null )
                        {
                            pdd = PDDHome.findByPrimaryKey( entree.getPdd(  ).getIdPdd(  ), plugin );
                            entree.setPdd( pdd );
                        }
                    }
                }

                Seance seance = SeanceHome.findByPrimaryKey( ordreDuJour.getSeance(  ).getIdSeance(  ), plugin );
                ordreDuJour.setSeance( seance );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getHomeUrl( request );
            }

            //recuperation des entetes et pied de page de l'ordre du jour   
            Entete entete = OrdreDuJourUtils.getEntete( ordreDuJour, plugin );
            PiedDePage piedPage = OrdreDuJourUtils.getPiedDePage( ordreDuJour, plugin );

            if ( entete != null )
            {
                ordreDuJour.setXmlEntete( OrdreDuJourUtils.getXmlEntete( entete, ordreDuJour.getSeance(  ) ) );
            }
            else
            {
                ordreDuJour.setXmlEntete( null );
            }

            if ( piedPage != null )
            {
                ordreDuJour.setXmlPiedDePage( OrdreDuJourUtils.getXmlPiedDePage( piedPage, ordreDuJour.getSeance(  ) ) );
            }
            else
            {
                ordreDuJour.setXmlPiedDePage( null );
            }

            ordreDuJour.setXmlPublication( ordreDuJour.getXml( request ) );
            ordreDuJour.setPublie( true );
            ordreDuJour.setDatePublication( dateForVersion );

            // creation du fichier pdf de l'ordre du jour
            Fichier newFichier = new Fichier(  );
            FichierPhysique newFichierPhysique = new FichierPhysique(  );

            // Creation de l'objet représentant le fichier physique
            // recuperation du xml de l'ordre du jour a
            try
            {
                tabXmlOdj = ( CONSTANTE_ENTETE_XML + ordreDuJour.getXmlPublication(  ) ).getBytes( OdsConstants.UTF8 );

                String nomTemplate = AppPropertiesService.getProperty( PROPERTY_ODJ_NOM_TEMPLATE,
                        DEFAULT_ODJ_NOM_TEMPLATE );

                tabXmlOdj = DocumentManager.convert( request, DocumentManager.PDF_CONVERSION, nomTemplate, tabXmlOdj);
                
                newFichierPhysique.setDonnees( tabXmlOdj );
                newFichierPhysique.setIdFichier( FichierPhysiqueHome.create( newFichierPhysique, getPlugin(  ) ) );
                newFichier.setFichier( newFichierPhysique );
                newFichier.setTitre( ordreDuJour.getIntitule(  ) );
                newFichier.setNom( ordreDuJour.getIntitule(  ) );
                newFichier.setDatePublication( dateForVersion );
                newFichier.setExtension( DocumentManager.PDF_CONVERSION );
                newFichier.setTaille( tabXmlOdj.length );
                // Creation de l'objet représentant le Fichier
                newFichier.setSeance( ordreDuJour.getSeance(  ) );
                newFichier.setTypdeDocument( TypeDocumentEnum.ORDRE_DU_JOUR.getTypeDocumentOnlyWidthId(  ) );
                newFichier.setEnLigne( true );

                if ( ordreDuJour.getCommission(  ) != null )
                {
                    newFichier.setCommission( ordreDuJour.getCommission(  ) );
                }

                if ( ordreDuJour.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) == TypeOrdreDuJourEnum.DEFINITIF.getId(  ) )
                {
                    try
                    {
                        //creation de la liasse des vas 
                        OrdreDuJourUtils.genereLiasse( ordreDuJour, plugin, dateForVersion, request, locale );
                    }
                    catch ( IOException ioe )
                    {
                        AppLogService.error( ioe );

                        return AdminMessageService.getMessageUrl( request, MESSAGE_PROBLEME_DE_CREATION_DE_LA_LIASSE,
                            AdminMessage.TYPE_STOP );
                    }
                    catch ( DocumentException e )
                    {
                        AppLogService.error( e );

                        return AdminMessageService.getMessageUrl( request, MESSAGE_PROBLEME_DE_CREATION_DE_LA_LIASSE,
                            AdminMessage.TYPE_STOP );
                    }
                }

                OrdreDuJourHome.update( ordreDuJour, plugin );
                int nIdFichier = FichierHome.create( newFichier, plugin );
                
                Fichier fichierANotifier = FichierHome.findByPrimaryKey( nIdFichier, plugin );
                OdsUtils.doNotifierUtilisateurs( fichierANotifier, request, plugin, locale );

                //publication vers paris.fr
                if ( ordreDuJour.getCommission(  ) == null )
                {
                    Publication publication = new Publication(  );
                    publication.setOrdreDuJour( ordreDuJour );
                    PublicationHome.create( publication, plugin );
                }

                return AppPathService.getBaseUrl( request ) + JSP_LISTE_ORDRE_DU_JOUR + '?' + CONSTANTE_SESSION + '=' +
                CONSTANTE_SESSION;
            }
            catch ( UnsupportedEncodingException uee )
            {
                AppLogService.error( uee );
            }
            catch ( RemoteConnectFailureException e )
            {
                AppLogService.error( e );
            }
            catch ( BeanCreationException be )
            {
                AppLogService.error( be );
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

            return AdminMessageService.getMessageUrl( request, MESSAGE_PROBLEME_DE_CREATION_ODJ_PDF,
                AdminMessage.TYPE_STOP );
        }

        return AppPathService.getBaseUrl( request ) + JSP_LISTE_ORDRE_DU_JOUR + '?' + CONSTANTE_SESSION + '=' +
        CONSTANTE_SESSION;
    }

    /**
     * Retourne l'interface de création d'une entrée de l'ordre du jour
     *
     * @param request
     *            la requête Http
     * @return l'interface de création d'une entrée de l'ordre du jour
     */
    public String getCreationEntreeOrdreDuJour( HttpServletRequest request )
    {
        Plugin plugin = getPlugin(  );
        HashMap<String, Object> model = new HashMap<String, Object>(  );

        if ( ( null == request.getParameter( CONSTANTE_SESSION ) ) &&
                ( null != request.getParameter( OdsParameters.TYPE ) ) &&
                ( null != request.getParameter( OdsParameters.ID_ODJ ) ) )
        {
            // initialisation de l'objet en session
            _entreeOrdreDuJourBean = new EntreeOrdreDuJour(  );
            _entreeOrdreDuJourBean.setIdEntreeOrdreDuJour( -1 );

            String strTypeCreation = request.getParameter( OdsParameters.TYPE );
            _entreeOrdreDuJourBean.setType( strTypeCreation );

            try
            {
                // récupération de l'ordre du jour
                int nIdOdj = Integer.parseInt( request.getParameter( OdsParameters.ID_ODJ ) );
                OrdreDuJour ordreDuJour = OrdreDuJourHome.findByPrimaryKey( nIdOdj, plugin );
                _entreeOrdreDuJourBean.setOrdreDuJour( ordreDuJour );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                // probleme de parametres
                return getOrdreDuJourList( request );
            }
        }

        if ( _entreeOrdreDuJourBean.getType(  ).equals( CONSTANTE_ENTREE_TYPE_PDD ) ||
                _entreeOrdreDuJourBean.getType(  ).equals( CONSTANTE_ENTREE_TYPE_VOEU ) )
        {
            // si le type d'entree est PDD ou VOEU
            ReferenceList refListRapporteur = initRefListElu( _entreeOrdreDuJourBean, plugin );
            ReferenceList refListCommission = initRefListCommission( _entreeOrdreDuJourBean, plugin );
            ReferenceList refListNatureDossier = initRefListNatureDossier( plugin,
                    _entreeOrdreDuJourBean.getOrdreDuJour(  ).getSeance(  ).getIdSeance(  ) );

            model.put( MARK_LISTE_RAPPORTEUR, refListRapporteur );
            model.put( MARK_LISTE_COMMISSION, refListCommission );
            model.put( MARK_LISTE_NATURE_DOSSIER, refListNatureDossier );
        }

        model.put( MARK_ENTREE_ODJ, _entreeOrdreDuJourBean );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATION_ENTREE_ORDRE_DU_JOUR, getLocale(  ),
                model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Effectue l'ensemble des traitements necessaire lors de la création d'une
     * entrée de l'ordre du jour à partir des informations contenues dans la
     * requête creation d'une nouvelle entrée ,ajout d'un rapporteur ,d'une
     * commission à l'entrée en cours de creation.....
     *
     * @param request
     *            la requête Http
     * @return en fonction des différents traitements retour vers l'url de
     *         modification d'un ordre du jour ,de creation de l'entrée en cours
     *         de creation...
     */
    public String doCreationEntreeOrdreDuJour( HttpServletRequest request )
    {
        Plugin plugin = getPlugin(  );
        String strUrlRetour = getHomeUrl( request );
        List<String> listErreurs = new ArrayList<String>(  );

        if ( null == request.getParameter( CONSTANTE_ANNULER ) )
        {
            if ( request.getParameter( "flag" ) != null || setEntreeOrdreDuJour( request ) )
            {
                // si la récupération des parametres du formulaire n'a pas posé
                // de problème
                if ( request.getParameter( "flag" ) != null || ( null != request.getParameter( CONSTANTE_ENREGISTRER ) ) )
                {
                    // si l'utilisateur click sur le bouton enregistrer
                    // on test les parametres saisis en utilisant l'objet
                    // EntreeOrdreDuJour en session
                    if ( request.getParameter( "flag" ) == null && !isFieldRequired( listErreurs ) )
                    {
                        String strChamp = ( listErreurs.get( 0 ) != null ) ? listErreurs.get( 0 )
                                                                           : OdsConstants.CONSTANTE_CHAINE_VIDE;
                        Object[] messagesArgs = { I18nService.getLocalizedString( strChamp, getLocale(  ) ) };

                        return AdminMessageService.getMessageUrl( request,
                            OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED, messagesArgs, AdminMessage.TYPE_STOP );
                    }
                    
                    if ( request.getParameter( "flag" ) == null && 
                    		( _entreeOrdreDuJourBean.getElus() != null ) &&
                    		( _entreeOrdreDuJourBean.getCommissions() != null ) &&
                    		!isRapporteurAppartientCommission( ) )
                    {
                        UrlItem url = new UrlItem( JSP_DO_CREATION_ENTREE_ORDRE_DU_JOUR );
                        url.addParameter( "flag", "ok" );
                        //url.addParameter( OdsParameters.ID_ODJ, _entreeOrdreDuJourBean.getOrdreDuJour(  ).getIdOrdreDuJour(  ) );

                        return AdminMessageService.getMessageUrl( request, MESSAGE_INCOHERENCE_RAPPORTEUR_COMMISSION, url.getUrl(  ),
                            AdminMessage.TYPE_CONFIRMATION );
                    }

                    
                    _entreeOrdreDuJourBean.setIdEntreeOrdreDuJour( EntreeOrdreDuJourHome.create( 
                            _entreeOrdreDuJourBean, plugin ) );

                    // on ajoute a la liste des nouvelles entrées l'id de la
                    // nouvelle entrée
                    if ( _listIdNouvelleEntree == null )
                    {
                        _listIdNouvelleEntree = new ArrayList<Integer>(  );
                    }

                    _listIdNouvelleEntree.add( _entreeOrdreDuJourBean.getIdEntreeOrdreDuJour(  ) );

                    if ( _entreeOrdreDuJourBean.getCommissions(  ) != null )
                    {
                        // insertion des commissions de l'entrée
                        for ( Commission commission : _entreeOrdreDuJourBean.getCommissions(  ) )
                        {
                            EntreeOrdreDuJourHome.insertCommission( _entreeOrdreDuJourBean.getIdEntreeOrdreDuJour(  ),
                                commission.getIdCommission(  ), plugin );
                        }
                    }

                    if ( _entreeOrdreDuJourBean.getElus(  ) != null )
                    {
                        // insertion des rapporteurs
                        for ( Elu elu : _entreeOrdreDuJourBean.getElus(  ) )
                        {
                            EntreeOrdreDuJourHome.insertRapporteur( _entreeOrdreDuJourBean.getIdEntreeOrdreDuJour(  ),
                                elu.getIdElu(  ), plugin );
                        }
                    }

                    // on recupere la nouvelle entrée en base
                    // si on est en mode automatique on aplique
                    // l'algorithme de classement automatique
                    // sinon on recupere la position ou doit etre inséré la
                    // nouvelle entrée
                    _entreeOrdreDuJourBean = EntreeOrdreDuJourHome.findByPrimaryKey( _entreeOrdreDuJourBean.getIdEntreeOrdreDuJour(  ),
                            plugin );

                    if ( _entreeOrdreDuJourBean.getOrdreDuJour(  ) != null )
                    {
                        OrdreDuJour ordreDuJour = 
                        	OrdreDuJourHome.findByPrimaryKey( _entreeOrdreDuJourBean.getOrdreDuJour(  ).getIdOrdreDuJour(  ),
                                plugin );
                        _entreeOrdreDuJourBean.setOrdreDuJour( ordreDuJour );

                        if ( ordreDuJour.getModeClassement(  ).equals( CONSTANTE_TYPE_AUTOMATIQUE ) )
                        { 
                        	// mode automatique.
                            classeEntree( _entreeOrdreDuJourBean );
                        }
                        else
                        {
                            // mode manuel
                            List<EntreeOrdreDuJour> listEntrees = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJour.getIdOrdreDuJour(  ),
                                    plugin );

                            // on supprime la derniere entrée inséré de la
                            // liste pour l'insérer a la place sélectionné
                            // par l'utilisateur
                            if ( _nIdPositionInsert != -1 )
                            {
                                listEntrees.remove( listEntrees.size(  ) - 1 );
                                listEntrees.add( _nIdPositionInsert, _entreeOrdreDuJourBean );

                                // on update les modifications
                                int nNumeroOrdre = 1;

                                for ( EntreeOrdreDuJour entree : listEntrees )
                                {
                                    entree.setNumeroOrdre( nNumeroOrdre );
                                    EntreeOrdreDuJourHome.update( entree, plugin );
                                    nNumeroOrdre++;
                                }
                            }
                        }
                        
                        // on retourne sur l'interface de modification de l'
                        // ODJ concerné
                        strUrlRetour = getJspModificationOdj( request,
                                _entreeOrdreDuJourBean.getOrdreDuJour(  ).getIdOrdreDuJour(  ) );
                    }
                }
                else if ( null != request.getParameter( CONSTANTE_AJOUTER_COMMISSION ) )
                {
                    // si l'utilisateur click sur le bouton Ajouter une
                    // commission
                    // on insere la commission dans l'objet EntreeOrdreDuJour en
                    // session
                    String strIdCommission = request.getParameter( OdsParameters.ID_COMMISSION );

                    try
                    {
                        int nIdCommission = Integer.parseInt( strIdCommission );

                        // si la liste des commissions de l'entrée en session
                        // est null on l'initialise
                        if ( _entreeOrdreDuJourBean.getCommissions(  ) == null )
                        {
                            _entreeOrdreDuJourBean.setCommissions( new ArrayList<Commission>(  ) );
                        }

                        if ( ( nIdCommission != -1 ) &&
                                !isInTheCommissionList( nIdCommission, _entreeOrdreDuJourBean.getCommissions(  ) ) )
                        {
                            Commission commission = CommissionHome.findByPrimaryKey( nIdCommission, plugin );
                            _entreeOrdreDuJourBean.getCommissions(  ).add( commission );
                        }
                    }
                    catch ( NumberFormatException ne )
                    {
                        AppLogService.error( ne );
                    }

                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_CREATION_ENTREE_ORDRE_DU_JOUR + '?' +
                        CONSTANTE_SESSION + '=' + CONSTANTE_SESSION;
                }
                else if ( null != request.getParameter( CONSTANTE_AJOUTER_RAPPORTEUR ) )
                {
                    // Si l'utilisateur click sur le bouton Ajouter un
                    // rapporteur
                    // on insere l'élu dans l'objet EntreeOrdreDuJour en session
                    String strIdElu = request.getParameter( OdsParameters.ID_ELU );

                    try
                    {
                        int nIdElu = Integer.parseInt( strIdElu );

                        // si la liste des élus de l'entrée en session est null
                        // on l'initialise
                        if ( _entreeOrdreDuJourBean.getElus(  ) == null )
                        {
                            _entreeOrdreDuJourBean.setElus( new ArrayList<Elu>(  ) );
                        }

                        if ( ( nIdElu != -1 ) && !isInTheEluList( nIdElu, _entreeOrdreDuJourBean.getElus(  ) ) )
                        {
                            Elu elu = EluHome.findByPrimaryKey( nIdElu, plugin );

                            // on recupere la commission lié à l'élu
                            Commission commission = CommissionHome.findByPrimaryKey( elu.getCommission(  )
                                                                                        .getIdCommission(  ), plugin );
                            elu.setCommission( commission );
                            _entreeOrdreDuJourBean.getElus(  ).add( elu );
                        }
                    }
                    catch ( NumberFormatException ne )
                    {
                        AppLogService.error( ne );
                    }

                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_CREATION_ENTREE_ORDRE_DU_JOUR + '?' +
                        CONSTANTE_SESSION + '=' + CONSTANTE_SESSION;
                }
                else if ( ( request.getParameter( CONSTANTE_TYPE_SUPPRESSION ) != null ) &&
                        request.getParameter( CONSTANTE_TYPE_SUPPRESSION ).equals( CONSTANTE_TYPE_SUPPRESSION_COMMISSION ) &&
                        ( request.getParameter( OdsParameters.ID_COMMISSION_TO_REMOVE ) != null ) )
                {
                    // si l'utilisateur click sur le bouton supprimer une
                    // commission
                    // on recupere l'id de la commission à supprimer dans la
                    // liste des commissions
                    // de l'objet en session
                    String strIdCommissionToRemove = request.getParameter( OdsParameters.ID_COMMISSION_TO_REMOVE );

                    try
                    {
                        int nIdCommissionToRemove = Integer.parseInt( strIdCommissionToRemove );

                        // si la liste des commissions de l'entrée en session
                        // est null on l'initialise
                        if ( _entreeOrdreDuJourBean.getCommissions(  ) == null )
                        {
                            _entreeOrdreDuJourBean.setCommissions( new ArrayList<Commission>(  ) );
                        }

                        removeCommissionInTheList( nIdCommissionToRemove, _entreeOrdreDuJourBean.getCommissions(  ) );
                    }
                    catch ( NumberFormatException ne )
                    {
                        AppLogService.error( ne );
                    }

                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_CREATION_ENTREE_ORDRE_DU_JOUR + '?' +
                        CONSTANTE_SESSION + '=' + CONSTANTE_SESSION;
                }
                else if ( ( request.getParameter( CONSTANTE_TYPE_SUPPRESSION ) != null ) &&
                        request.getParameter( CONSTANTE_TYPE_SUPPRESSION ).equals( CONSTANTE_TYPE_SUPPRESSION_ELU ) &&
                        ( request.getParameter( OdsParameters.ID_ELU_TO_REMOVE ) != null ) )
                {
                    // Si l'utilisateur click sur le bouton suppression d' un
                    // rapporteur
                    // on supprimme l'élu selectionné dans l'objet
                    // EntreeOrdreDuJour en session
                    String strIdEluToRemove = request.getParameter( OdsParameters.ID_ELU_TO_REMOVE );

                    try
                    {
                        int nIdElu = Integer.parseInt( strIdEluToRemove );

                        // si la liste des élus de l'entrée en session est null
                        // on l'initialise
                        if ( _entreeOrdreDuJourBean.getElus(  ) == null )
                        {
                            _entreeOrdreDuJourBean.setElus( new ArrayList<Elu>(  ) );
                        }

                        removeEluInTheList( nIdElu, _entreeOrdreDuJourBean.getElus(  ) );
                    }
                    catch ( NumberFormatException ne )
                    {
                        AppLogService.error( ne );
                    }

                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_CREATION_ENTREE_ORDRE_DU_JOUR + '?' +
                        CONSTANTE_SESSION + '=' + CONSTANTE_SESSION;
                }
                else if ( request.getParameter( CONSTANTE_AJOUTER_PROJET ) != null )
                {
                    // l'utilisateur click sur le bouton ajouter un projet de
                    // délibération
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_SELECTION_PROJET + "?" +
                        OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_ODJ + "=" +
                        _entreeOrdreDuJourBean.getOrdreDuJour(  ).getIdOrdreDuJour(  );
                }
                else if ( request.getParameter( CONSTANTE_AJOUTER_PROPOSITION ) != null )
                {
                    // l'utilisateur click sur le bouton ajouter une proposition
                    // de délibération
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_SELECTION_PROPOSITION + "?" +
                        OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_ODJ + "=" +
                        _entreeOrdreDuJourBean.getOrdreDuJour(  ).getIdOrdreDuJour(  );
                }
                else if ( request.getParameter( CONSTANTE_DESELECTION_PDD ) != null )
                {
                    // l'utilisateur click sur le bouton supprimer une
                    // proposition
                    // ou un projet de deliberation
                    _entreeOrdreDuJourBean.setReference( OdsConstants.CONSTANTE_CHAINE_VIDE );
                    _entreeOrdreDuJourBean.setObjet( OdsConstants.CONSTANTE_CHAINE_VIDE );
                    _entreeOrdreDuJourBean.setPdd( null );
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_CREATION_ENTREE_ORDRE_DU_JOUR + '?' +
                        CONSTANTE_SESSION + '=' + CONSTANTE_SESSION;
                }
                else if ( request.getParameter( CONSTANTE_SELECTION_VOEU ) != null )
                {
                    // l'utilisateur click sur le bouton ajouter un voeu
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_SELECTION_VOEU + "?" +
                        OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_ODJ + "=" +
                        _entreeOrdreDuJourBean.getOrdreDuJour(  ).getIdOrdreDuJour(  );
                }
                else if ( request.getParameter( CONSTANTE_AJOUTER_VOEU ) != null )
                {
                    // l'utilisateur click sur le bouton ajouter un voeu
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_AJOUT_VOEU + "?" +
                        OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_ODJ + "=" +
                        _entreeOrdreDuJourBean.getOrdreDuJour(  ).getIdOrdreDuJour(  );
                }
                else if ( request.getParameter( CONSTANTE_DESELECTION_VOEU ) != null )
                {
                    // l'utilisateur click sur le bouton supprimer un voeu
                    _entreeOrdreDuJourBean.setReference( OdsConstants.CONSTANTE_CHAINE_VIDE );
                    _entreeOrdreDuJourBean.setObjet( OdsConstants.CONSTANTE_CHAINE_VIDE );
                    _entreeOrdreDuJourBean.setVoeuAmendement( null );
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_CREATION_ENTREE_ORDRE_DU_JOUR + '?' +
                        CONSTANTE_SESSION + '=' + CONSTANTE_SESSION;
                }
                else if ( request.getParameter( CONSTANTE_AJOUTER_EXPRESSION ) != null )
                {
                    // l'utilisateur click sur le bouton inserer une expression
                    // usuelle
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_SELECTION_EXPRESSION + "?" +
                        OdsParameters.PLUGIN_NAME;
                }
            }
        }
        else
        {
            // si l'utilisateur click sur le bouton annuler on retourne sur
            // l'interface
            // de modification de l'ordre du jour précédemment sélectionné
            if ( _entreeOrdreDuJourBean.getOrdreDuJour(  ) != null )
            {
                strUrlRetour = getJspModificationOdj( request,
                        _entreeOrdreDuJourBean.getOrdreDuJour(  ).getIdOrdreDuJour(  ) );
            }
        }

        return strUrlRetour;
    }

    private boolean isRapporteurAppartientCommission( ) {
    	List<Commission> commissions = _entreeOrdreDuJourBean.getCommissions();
    	List<Elu> elus = _entreeOrdreDuJourBean.getElus();
    	
    	for ( Elu e : elus )
    	{
    		boolean isOk = false;
    		int idCommissionElu = e.getCommission().getIdCommission();
    		for ( Commission c : commissions )
    		{
        		if( idCommissionElu == c.getIdCommission() )
        		{
        			isOk = true;
        		}
        	}
    		if( !isOk ){
    			return false;
    		}
    	}
    	return true;
	}

	/**
     * Retourne l'interface de modification de l'entrée d'ordre du jour
     * selectionnée
     *
     * @param request
     *            la requête Http
     * @return l'interface de modification de l'entrée d'ordre du jour
     *         selectionnée
     */
    public String getModificationEntreeOrdreDuJour( HttpServletRequest request )
    {
        Plugin plugin = getPlugin(  );
        HashMap<String, Object> model = new HashMap<String, Object>(  );

        if ( ( null == request.getParameter( CONSTANTE_SESSION ) ) &&
                ( null != request.getParameter( OdsParameters.ID_ENTREE ) ) )
        {
            // on récupère l'objet en base
            try
            {
                // récupération de l' entrée d'ordre du jour à modifier
                int nIdEntreeOdj = Integer.parseInt( request.getParameter( OdsParameters.ID_ENTREE ) );
                _entreeOrdreDuJourBean = EntreeOrdreDuJourHome.findByPrimaryKey( nIdEntreeOdj, plugin );

                // récupération de l'ordre du jour
                OrdreDuJour ordreDuJour = OrdreDuJourHome.findByPrimaryKey( _entreeOrdreDuJourBean.getOrdreDuJour(  )
                                                                                                  .getIdOrdreDuJour(  ),
                        plugin );
                _entreeOrdreDuJourBean.setOrdreDuJour( ordreDuJour );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                // probleme de parametres
                return getOrdreDuJourList( request );
            }
        }

        if ( ( _entreeOrdreDuJourBean != null ) &&
                ( _entreeOrdreDuJourBean.getType(  ).equals( CONSTANTE_ENTREE_TYPE_PDD ) ||
                _entreeOrdreDuJourBean.getType(  ).equals( CONSTANTE_ENTREE_TYPE_VOEU ) ) )
        {
            // si le type d'entree est PDD ou VOEU
            ReferenceList refListRapporteur = initRefListElu( _entreeOrdreDuJourBean, plugin );
            ReferenceList refListCommission = initRefListCommission( _entreeOrdreDuJourBean, plugin );
            ReferenceList refListNatureDossier = initRefListNatureDossier( plugin,
                    _entreeOrdreDuJourBean.getOrdreDuJour(  ).getSeance(  ).getIdSeance(  ) );

            model.put( MARK_LISTE_RAPPORTEUR, refListRapporteur );
            model.put( MARK_LISTE_COMMISSION, refListCommission );
            model.put( MARK_LISTE_NATURE_DOSSIER, refListNatureDossier );
        }

        model.put( MARK_ENTREE_ODJ, _entreeOrdreDuJourBean );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_ENTREE_ORDRE_DU_JOUR,
                getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Effectue l'ensemble des traitements necessaire lors d'une modification
     * d'une entrée d'ordre du jour à partir des informations contenues dans la
     * requête modification d'une entrée ,ajout d'un rapporteur ,d'une
     * commission à l'entrée en cours de modification.....
     *
     * @param request
     *            la requête Http
     * @return en fonction des différents traitements retour vers l'url de
     *         modification d'un ordre du jour ,de creation de l'entrée en cours
     *         de creation...
     */
    public String doModificationEntreeOrdreDuJour( HttpServletRequest request )
    {
        Plugin plugin = getPlugin(  );
        String strUrlRetour = getHomeUrl( request );
        List<String> listErreurs = new ArrayList<String>(  );

        if ( null == request.getParameter( CONSTANTE_ANNULER ) )
        {
            if ( request.getParameter( "flag" ) != null ||
            		setEntreeOrdreDuJour( request ) )
            {
                // si la récupération des parametres du formulaire n'a pas posé
                // de problème
                if ( request.getParameter( "flag" ) != null ||
                		null != request.getParameter( CONSTANTE_ENREGISTRER ) )
                {
                    // si l'utilisateur click sur le bouton enregistrer
                    // on test les parametres saisis en utilisant l'objet
                    // EntreeOrdreDuJour en session
                    if ( request.getParameter( "flag" ) == null && 
                    		!isFieldRequired( listErreurs ) )
                    {
                        String strChamp = ( listErreurs.get( 0 ) != null ) ? listErreurs.get( 0 )
                                                                           : OdsConstants.CONSTANTE_CHAINE_VIDE;
                        Object[] messagesArgs = { I18nService.getLocalizedString( strChamp, getLocale(  ) ) };

                        return AdminMessageService.getMessageUrl( request,
                            OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED, messagesArgs, AdminMessage.TYPE_STOP );
                    }
                    else
                    {
                    	if( request.getParameter( "flag" ) == null && 
                        		( _entreeOrdreDuJourBean.getElus() != null ) &&
                        		( _entreeOrdreDuJourBean.getCommissions() != null ) &&
                        		!isRapporteurAppartientCommission( ) )
                        {
                            UrlItem url = new UrlItem( JSP_DO_MODIFICATION_ENTREE_ORDRE_DU_JOUR );
                            url.addParameter( "flag", "ok" );
                            //url.addParameter( OdsParameters.ID_ODJ, _entreeOrdreDuJourBean.getOrdreDuJour(  ).getIdOrdreDuJour(  ) );

                            return AdminMessageService.getMessageUrl( request, MESSAGE_INCOHERENCE_RAPPORTEUR_COMMISSION, url.getUrl(  ),
                                AdminMessage.TYPE_CONFIRMATION );
                        }

                    	
                        EntreeOrdreDuJourHome.update( _entreeOrdreDuJourBean, plugin );

                        // modification des commissions de l'entrée
                        EntreeOrdreDuJourHome.deleteCommission( _entreeOrdreDuJourBean.getIdEntreeOrdreDuJour(  ),
                            plugin );

                        if ( _entreeOrdreDuJourBean.getCommissions(  ) != null )
                        {
                            for ( Commission commission : _entreeOrdreDuJourBean.getCommissions(  ) )
                            {
                                EntreeOrdreDuJourHome.insertCommission( _entreeOrdreDuJourBean.getIdEntreeOrdreDuJour(  ),
                                    commission.getIdCommission(  ), plugin );
                            }
                        }

                        // modification des rapporteurs
                        EntreeOrdreDuJourHome.deleteRapporteur( _entreeOrdreDuJourBean.getIdEntreeOrdreDuJour(  ),
                            plugin );

                        if ( _entreeOrdreDuJourBean.getElus(  ) != null )
                        {
                            for ( Elu elu : _entreeOrdreDuJourBean.getElus(  ) )
                            {
                                EntreeOrdreDuJourHome.insertRapporteur( _entreeOrdreDuJourBean.getIdEntreeOrdreDuJour(  ),
                                    elu.getIdElu(  ), plugin );
                            }
                        }

                        // on recupere la nouvelle entrée en base
                        // si on est en mode automatique on aplique
                        // l'algorithme de classement automatique
                        _entreeOrdreDuJourBean = EntreeOrdreDuJourHome.findByPrimaryKey( _entreeOrdreDuJourBean.getIdEntreeOrdreDuJour(  ),
                                plugin );

                        if ( _entreeOrdreDuJourBean.getOrdreDuJour(  ) != null )
                        {
                            OrdreDuJour ordreDuJour = OrdreDuJourHome.findByPrimaryKey( _entreeOrdreDuJourBean.getOrdreDuJour(  )
                                                                                                              .getIdOrdreDuJour(  ),
                                    plugin );
                            _entreeOrdreDuJourBean.setOrdreDuJour( ordreDuJour );

                            if ( ordreDuJour.getModeClassement(  ).equals( CONSTANTE_TYPE_AUTOMATIQUE ) )
                            { // mode
                              // automatique
                                classeEntree( _entreeOrdreDuJourBean );
                            }

                            // on retourne sur l'intreface de modification de l'
                            // ODJ concerné
                            strUrlRetour = getJspModificationOdj( request,
                                    _entreeOrdreDuJourBean.getOrdreDuJour(  ).getIdOrdreDuJour(  ) );
                        }
                    }
                }
                else if ( null != request.getParameter( CONSTANTE_AJOUTER_COMMISSION ) )
                {
                    // si l'utilisateur click sur le bouton Ajouter une
                    // commission
                    // on insere la commission dans l'objet EntreeOrdreDuJour en
                    // session
                    String strIdCommission = request.getParameter( OdsParameters.ID_COMMISSION );

                    try
                    {
                        int nIdCommission = Integer.parseInt( strIdCommission );

                        // si la liste des commissions de l'entrée en session
                        // est null on l'initialise
                        if ( _entreeOrdreDuJourBean.getCommissions(  ) == null )
                        {
                            _entreeOrdreDuJourBean.setCommissions( new ArrayList<Commission>(  ) );
                        }

                        if ( ( nIdCommission != -1 ) &&
                                !isInTheCommissionList( nIdCommission, _entreeOrdreDuJourBean.getCommissions(  ) ) )
                        {
                            Commission commission = CommissionHome.findByPrimaryKey( nIdCommission, plugin );
                            _entreeOrdreDuJourBean.getCommissions(  ).add( commission );
                        }
                    }
                    catch ( NumberFormatException ne )
                    {
                        AppLogService.error( ne );
                    }

                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_MODIFICATION_ENTREE_ORDRE_DU_JOUR + '?' +
                        CONSTANTE_SESSION + '=' + CONSTANTE_SESSION;
                }
                else if ( null != request.getParameter( CONSTANTE_AJOUTER_RAPPORTEUR ) )
                {
                    // Si l'utilisateur click sur le bouton Ajouter un
                    // rapporteur
                    // on insere l'élu dans l'objet EntreeOrdreDuJour en session
                    String strIdElu = request.getParameter( OdsParameters.ID_ELU );

                    try
                    {
                        int nIdElu = Integer.parseInt( strIdElu );

                        // si la liste des élus de l'entrée en session est null
                        // on l'initialise
                        if ( _entreeOrdreDuJourBean.getElus(  ) == null )
                        {
                            _entreeOrdreDuJourBean.setElus( new ArrayList<Elu>(  ) );
                        }

                        if ( ( nIdElu != -1 ) && !isInTheEluList( nIdElu, _entreeOrdreDuJourBean.getElus(  ) ) )
                        {
                            Elu elu = EluHome.findByPrimaryKey( nIdElu, plugin );

                            // on recupere la commission lié à l'élu
                            Commission commission = CommissionHome.findByPrimaryKey( elu.getCommission(  )
                                                                                        .getIdCommission(  ), plugin );
                            elu.setCommission( commission );
                            _entreeOrdreDuJourBean.getElus(  ).add( elu );
                        }
                    }
                    catch ( NumberFormatException ne )
                    {
                        AppLogService.error( ne );
                    }

                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_MODIFICATION_ENTREE_ORDRE_DU_JOUR + '?' +
                        CONSTANTE_SESSION + '=' + CONSTANTE_SESSION;
                }
                else if ( ( request.getParameter( CONSTANTE_TYPE_SUPPRESSION ) != null ) &&
                        request.getParameter( CONSTANTE_TYPE_SUPPRESSION ).equals( CONSTANTE_TYPE_SUPPRESSION_COMMISSION ) &&
                        ( request.getParameter( OdsParameters.ID_COMMISSION_TO_REMOVE ) != null ) )
                {
                    // si l'utilisateur click sur le bouton supprimer une
                    // commission
                    // on recupere l'id de la commission à supprimer dans la
                    // liste des commissions
                    // de l'objet en session
                    String strIdCommissionToRemove = request.getParameter( OdsParameters.ID_COMMISSION_TO_REMOVE );

                    try
                    {
                        int nIdCommissionToRemove = Integer.parseInt( strIdCommissionToRemove );

                        // si la liste des commissions de l'entrée en session
                        // est null on l'initialise
                        if ( _entreeOrdreDuJourBean.getCommissions(  ) == null )
                        {
                            _entreeOrdreDuJourBean.setCommissions( new ArrayList<Commission>(  ) );
                        }

                        removeCommissionInTheList( nIdCommissionToRemove, _entreeOrdreDuJourBean.getCommissions(  ) );
                    }
                    catch ( NumberFormatException ne )
                    {
                        AppLogService.error( ne );
                    }

                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_MODIFICATION_ENTREE_ORDRE_DU_JOUR + '?' +
                        CONSTANTE_SESSION + '=' + CONSTANTE_SESSION;
                }
                else if ( ( request.getParameter( CONSTANTE_TYPE_SUPPRESSION ) != null ) &&
                        request.getParameter( CONSTANTE_TYPE_SUPPRESSION ).equals( CONSTANTE_TYPE_SUPPRESSION_ELU ) &&
                        ( request.getParameter( OdsParameters.ID_ELU_TO_REMOVE ) != null ) )
                {
                    // Si l'utilisateur click sur le bouton suppression d' un
                    // rapporteur
                    // on supprimme l'élu selectionné dans l'objet
                    // EntreeOrdreDuJour en session
                    String strIdEluToRemove = request.getParameter( OdsParameters.ID_ELU_TO_REMOVE );

                    try
                    {
                        int nIdElu = Integer.parseInt( strIdEluToRemove );

                        // si la liste des élus de l'entrée en session est null
                        // on l'initialise
                        if ( _entreeOrdreDuJourBean.getElus(  ) == null )
                        {
                            _entreeOrdreDuJourBean.setElus( new ArrayList<Elu>(  ) );
                        }

                        removeEluInTheList( nIdElu, _entreeOrdreDuJourBean.getElus(  ) );
                    }
                    catch ( NumberFormatException ne )
                    {
                        AppLogService.error( ne );
                    }

                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_MODIFICATION_ENTREE_ORDRE_DU_JOUR + '?' +
                        CONSTANTE_SESSION + '=' + CONSTANTE_SESSION;
                }
                else if ( request.getParameter( CONSTANTE_AJOUTER_PROJET ) != null )
                {
                    // l'utilisateur click sur le bouton ajouter un projet de
                    // délibération
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_SELECTION_PROJET + "?" +
                        OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_ODJ + "=" +
                        _entreeOrdreDuJourBean.getOrdreDuJour(  ).getIdOrdreDuJour(  );
                }
                else if ( request.getParameter( CONSTANTE_AJOUTER_PROPOSITION ) != null )
                {
                    // l'utilisateur click sur le bouton ajouter une proposition
                    // de délibération
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_SELECTION_PROPOSITION + "?" +
                        OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_ODJ + "=" +
                        _entreeOrdreDuJourBean.getOrdreDuJour(  ).getIdOrdreDuJour(  );
                }
                else if ( request.getParameter( CONSTANTE_DESELECTION_PDD ) != null )
                {
                    // l'utilisateur click sur le bouton supprimer une
                    // proposition
                    // ou un projet de deliberation
                    _entreeOrdreDuJourBean.setReference( OdsConstants.CONSTANTE_CHAINE_VIDE );
                    _entreeOrdreDuJourBean.setObjet( OdsConstants.CONSTANTE_CHAINE_VIDE );
                    _entreeOrdreDuJourBean.setPdd( null );
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_MODIFICATION_ENTREE_ORDRE_DU_JOUR + '?' +
                        CONSTANTE_SESSION + '=' + CONSTANTE_SESSION;
                }
                else if ( request.getParameter( CONSTANTE_SELECTION_VOEU ) != null )
                {
                    // l'utilisateur click sur le bouton ajouter un voeu
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_SELECTION_VOEU + "?" +
                        OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_ODJ + "=" +
                        _entreeOrdreDuJourBean.getOrdreDuJour(  ).getIdOrdreDuJour(  );
                }
                else if ( request.getParameter( CONSTANTE_AJOUTER_VOEU ) != null )
                {
                    // l'utilisateur click sur le bouton ajouter un voeu
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_AJOUT_VOEU + "?" +
                        OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_ODJ + "=" +
                        _entreeOrdreDuJourBean.getOrdreDuJour(  ).getIdOrdreDuJour(  );
                }
                else if ( request.getParameter( CONSTANTE_DESELECTION_VOEU ) != null )
                {
                    // l'utilisateur click sur le bouton supprimer un voeu
                    _entreeOrdreDuJourBean.setReference( OdsConstants.CONSTANTE_CHAINE_VIDE );
                    _entreeOrdreDuJourBean.setObjet( OdsConstants.CONSTANTE_CHAINE_VIDE );
                    _entreeOrdreDuJourBean.setVoeuAmendement( null );
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_MODIFICATION_ENTREE_ORDRE_DU_JOUR + '?' +
                        CONSTANTE_SESSION + '=' + CONSTANTE_SESSION;
                }
                else if ( request.getParameter( CONSTANTE_AJOUTER_EXPRESSION ) != null )
                {
                    // l'utilisateur click sur le bouton inserer une expression
                    // usuelle
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_SELECTION_EXPRESSION + "?" +
                        OdsParameters.PLUGIN_NAME;
                }
            }
        }
        else
        {
            // si l'utilisateur click sur le bouton annuler on retourne sur
            // l'interface
            // de modification de l'ordre du jour précédemment sélectionné
            if ( _entreeOrdreDuJourBean.getOrdreDuJour(  ) != null )
            {
                strUrlRetour = getJspModificationOdj( request,
                        _entreeOrdreDuJourBean.getOrdreDuJour(  ).getIdOrdreDuJour(  ) );
            }
        }

        return strUrlRetour;
    }

    /**
     * Retourne l'interface de confirmation de suppression d'une entrée d'ordre
     * du jour
     *
     * @param request
     *            la requête Http
     * @return l'interface de confirmation de suppression d'une entrée d'ordre
     *         du jour
     */
    public String getSuppressionEntreeOrdreDuJour( HttpServletRequest request )
    {
        if ( request.getParameter( OdsParameters.ID_ENTREE ) == null )
        {
            return getHomeUrl( request );
        }

        String strIdEntree = request.getParameter( OdsParameters.ID_ENTREE );
        UrlItem url = new UrlItem( JSP_DO_SUPPRESSION_ENTREE_JSP );
        url.addParameter( OdsParameters.ID_ENTREE, strIdEntree );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRME_DELETE_ENTREE, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Effectue la suppression d'une entrée ordre du jour à partir des
     * informations contenues dans la requête
     *
     * @param request
     *            la requête Http
     * @return l'url de modification de l' ordre du jour en cours de
     *         modification
     */
    public String doSuppressionEntreeOrdreDuJour( HttpServletRequest request )
    {
        if ( null != request.getParameter( OdsParameters.ID_ENTREE ) )
        {
            EntreeOrdreDuJour entreeOrdreDuJour;
            Plugin plugin = getPlugin(  );

            try
            {
                entreeOrdreDuJour = EntreeOrdreDuJourHome.findByPrimaryKey( Integer.parseInt( request.getParameter( 
                                OdsParameters.ID_ENTREE ) ), plugin );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getHomeUrl( request );
            }

            try
            {
                // suppression de l'entree
                EntreeOrdreDuJourHome.remove( entreeOrdreDuJour, plugin );

                // on update les modifications
                int nNumeroOrdre = 1;
                List<EntreeOrdreDuJour> listEntrees = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( entreeOrdreDuJour.getOrdreDuJour(  )
                                                                                                                             .getIdOrdreDuJour(  ),
                        plugin );

                for ( EntreeOrdreDuJour entree : listEntrees )
                {
                    entree.setNumeroOrdre( nNumeroOrdre );
                    EntreeOrdreDuJourHome.update( entree, plugin );
                    nNumeroOrdre++;
                }

                // on retourn sur l'inteface de modification
                return getJspModificationOdj( request, entreeOrdreDuJour.getOrdreDuJour(  ).getIdOrdreDuJour(  ) );
            }
            catch ( AppException ae )
            {
                AppLogService.error( ae );

                if ( ae.getInitialException(  ) instanceof SQLException )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_ENTREE,
                        AdminMessage.TYPE_STOP );
                }
            }
        }

        return getHomeUrl( request );
    }

    /**
     * Effectue la suppression de le liste des entées d'ordre du jour
     * selectionnées par l'utisateur
     *
     * @param request
     *            la requête Http
     * @return l'url de modification de l' ordre du jour en cours de
     *         modification
     */
    public String doSuppressionListeEntree( HttpServletRequest request )
    {
        if ( ( _strTabIdSelect != null ) && ( _strTabIdSelect.length != 0 ) &&
                ( request.getParameter( OdsParameters.ID_ODJ ) != null ) )
        {
            Plugin plugin = getPlugin(  );
            EntreeOrdreDuJour entreeOrdreDuJour;

            for ( String strIdEntre : _strTabIdSelect )
            {
                try
                {
                    int nIdEntree = Integer.parseInt( strIdEntre );
                    entreeOrdreDuJour = EntreeOrdreDuJourHome.findByPrimaryKey( nIdEntree, plugin );
                    EntreeOrdreDuJourHome.remove( entreeOrdreDuJour, plugin );
                }
                catch ( NumberFormatException ne )
                {
                    return getHomeUrl( request );
                }
            }

            try
            {
                int nIdOdj = Integer.parseInt( request.getParameter( OdsParameters.ID_ODJ ) );

                // on update les modifications
                int nNumeroOrdre = 1;
                List<EntreeOrdreDuJour> listEntrees = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( nIdOdj,
                        plugin );

                for ( EntreeOrdreDuJour entree : listEntrees )
                {
                    entree.setNumeroOrdre( nNumeroOrdre );
                    EntreeOrdreDuJourHome.update( entree, plugin );
                    nNumeroOrdre++;
                }

                // on retourne sur l'intreface de modification de l' ODJ
                // concerné
                return getJspModificationOdj( request, nIdOdj );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getHomeUrl( request );
            }
        }

        return getHomeUrl( request );
    }

    /**
     * insere le PDD selectionné dans l'entrée d'ordre du jour en session
     *
     * @param request
     *            la requête Http
     * @return retourne sur l'entrée d'ordre du jour en cours de traitement
     */
    public String doSelectionPDD( HttpServletRequest request )
    {
        // Si l'action effectuée est "Annuler" on en fait rien.
        if ( ( request.getParameter( OdsParameters.A_FAIRE ) != null ) &&
                !request.getParameter( OdsParameters.A_FAIRE ).equals( CONSTANTE_CANCEL ) )
        {
            // Si l'id du pdd n'est pas donné en paramètre message
            if ( ( request.getParameter( OdsParameters.ID_PDD ) == null ) ||
                    request.getParameter( OdsParameters.ID_PDD ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_PAS_DE_PDD_CHOISI, AdminMessage.TYPE_STOP );
            }

            // On récupère l'id du pdd et si il est invalide => retour à la case
            // départ
            int nIdPDD = -1;

            try
            {
                nIdPDD = Integer.parseInt( request.getParameter( OdsParameters.ID_PDD ) );
            }
            catch ( NumberFormatException ne )
            {
                return getHomeUrl( request );
            }

            if ( nIdPDD == -1 )
            {
                return getHomeUrl( request );
            }

            // On insert dans l'objet entreeOrdreDuJour en session le pdd dont
            // on a récupéré l'id
            PDD pddSelected = PDDHome.findByPrimaryKey( nIdPDD, getPlugin(  ) );
            _entreeOrdreDuJourBean.setPdd( pddSelected );
            // Les champs de saisie de la référence et de l’objet affichés sont
            // valorisés avec la référence
            // et l’objet du PDD sélectionné.
            _entreeOrdreDuJourBean.setReference( pddSelected.getReference(  ) );
            _entreeOrdreDuJourBean.setObjet( pddSelected.getObjet(  ) );
        }

        if ( _entreeOrdreDuJourBean.getIdEntreeOrdreDuJour(  ) == -1 )
        {
            // Si l'id de l'entree en session =-1 retour sur l'interface de
            // creation
            return AppPathService.getBaseUrl( request ) + JSP_CREATION_ENTREE_ORDRE_DU_JOUR + '?' + CONSTANTE_SESSION +
            '=' + CONSTANTE_SESSION;
        }
        else
        {
            // retour sur l'interface de modification
            return AppPathService.getBaseUrl( request ) + JSP_MODIFICATION_ENTREE_ORDRE_DU_JOUR + '?' +
            CONSTANTE_SESSION + '=' + CONSTANTE_SESSION;
        }
    }

    /**
     * methode qui crée pour chaque Pdds selectionnés une entrée d'ordre du jour
     *
     * @param request
     *            la requête Http
     * @return retour sur l'ordre du jour en cours de modification
     */
    public String doSelectionPDDs( HttpServletRequest request )
    {
        if ( null != request.getParameter( OdsParameters.ID_ENTREE ) )
        {
            Plugin plugin = getPlugin(  );
            EntreeOrdreDuJour entreeOrdreDuJour;

            try
            {
                entreeOrdreDuJour = EntreeOrdreDuJourHome.findByPrimaryKey( Integer.parseInt( request.getParameter( 
                                OdsParameters.ID_ENTREE ) ), plugin );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getHomeUrl( request );
            }

            if ( request.getParameter( CONSTANTE_ANNULER ) == null )
            {
                // recupération de la liste des Id pdds à ajouter au va
                String[] strListePdd = request.getParameterValues( OdsParameters.LISTE_PDD_CHECKED );

                if ( ( strListePdd != null ) && ( strListePdd.length != 0 ) )
                {
                    // on recupere l'entrée d'ordre du jour dans le but de
                    // recuperer
                    // son groupe de commissions et son groupe de rapporteurs
                    EntreeOrdreDuJour newEntree;
                    PDD pdd;
                    List<EntreeOrdreDuJour> listEntreeATrier = new ArrayList<EntreeOrdreDuJour>(  );

                    for ( String strIdPdd : strListePdd )
                    {
                        int nIdPdd;

                        try
                        {
                            nIdPdd = Integer.parseInt( strIdPdd );
                            pdd = PDDHome.findByPrimaryKey( nIdPdd, plugin );
                        }
                        catch ( NumberFormatException ne )
                        {
                            return getHomeUrl( request );
                        }

                        newEntree = new EntreeOrdreDuJour(  );
                        newEntree.setPdd( pdd );
                        newEntree.setReference( pdd.getReference(  ) );
                        newEntree.setObjet( pdd.getObjet(  ) );
                        newEntree.setType( CONSTANTE_ENTREE_TYPE_PDD );
                        newEntree.setNature( entreeOrdreDuJour.getNature(  ) );
                        newEntree.setOrdreDuJour( entreeOrdreDuJour.getOrdreDuJour(  ) );
                        newEntree.setIdEntreeOrdreDuJour( EntreeOrdreDuJourHome.create( newEntree, plugin ) );

                        if ( _listIdNouvelleEntree == null )
                        {
                            _listIdNouvelleEntree = new ArrayList<Integer>(  );
                        }

                        // on ajoute a la liste des nouvelles entrées l'id de la
                        // nouvelle entrée
                        _listIdNouvelleEntree.add( newEntree.getIdEntreeOrdreDuJour(  ) );

                        listEntreeATrier.add( newEntree );
                    }

                    // Les commissions et les rapporteurs des entrées qui sont
                    // créées sont celles du groupe choisi.
                    for ( EntreeOrdreDuJour entree : listEntreeATrier )
                    {
                        entree.setCommissions( entreeOrdreDuJour.getCommissions(  ) );
                        entree.setElus( entreeOrdreDuJour.getElus(  ) );

                        for ( Commission commission : entree.getCommissions(  ) )
                        {
                            EntreeOrdreDuJourHome.insertCommission( entree.getIdEntreeOrdreDuJour(  ),
                                commission.getIdCommission(  ), plugin );
                        }

                        for ( Elu elu : entree.getElus(  ) )
                        {
                            EntreeOrdreDuJourHome.insertRapporteur( entree.getIdEntreeOrdreDuJour(  ),
                                elu.getIdElu(  ), plugin );
                        }
                    }

                    // on reclasse l'ordre du jour en mode automatique
                    classeListEntrees( listEntreeATrier );
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

            return getJspModificationOdj( request, entreeOrdreDuJour.getOrdreDuJour(  ).getIdOrdreDuJour(  ) );
        }
        else
        {
            return getHomeUrl( request );
        }
    }

    /**
     * insere une expression usuelle dans l'entrée d'ordre du jour en session
     *
     * @param request
     *            la requête Http
     * @return l'url de traitement de l'entrée d'ordre du jour
     */
    public String doSelectionExpression( HttpServletRequest request )
    {
        // Si l'action effectuée est "Annuler" on en fait rien.
        if ( ( request.getParameter( CONSTANTE_ANNULER ) == null ) )
        {
            // Si l'id du de l'expression n'est pas donné en paramètre => Retour
            // à la case départ
            if ( ( request.getParameter( OdsParameters.ID_EXPRESSION ) == null ) ||
                    request.getParameter( OdsParameters.ID_EXPRESSION ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_PAS_EXPRESSION_CHOISI, AdminMessage.TYPE_STOP );
            }

            // On récupère l'id du pdd et si il est invalide => retour à la case
            // départ
            int nIdExpression = -1;

            try
            {
                nIdExpression = Integer.parseInt( request.getParameter( OdsParameters.ID_EXPRESSION ) );
            }
            catch ( NumberFormatException ne )
            {
                return getHomeUrl( request );
            }

            if ( nIdExpression == -1 )
            {
                return getHomeUrl( request );
            }

            ExpressionUsuelle expressionSelected = ExpressionUsuelleHome.findByPrimaryKey( nIdExpression, getPlugin(  ) );

            // Le texte de l’expression est copié dans l’objet, à la suite du
            // texte déjà saisi le cas échéant
            String strObjet = ( _entreeOrdreDuJourBean.getObjet(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                ? expressionSelected.getExpression(  )
                : ( _entreeOrdreDuJourBean.getObjet(  ) + " " + expressionSelected.getExpression(  ) );
            _entreeOrdreDuJourBean.setObjet( strObjet );
        }

        if ( _entreeOrdreDuJourBean.getIdEntreeOrdreDuJour(  ) == -1 )
        {
            // Si l'id de l'entree en session =-1 retour sur l'interface de
            // creation
            return AppPathService.getBaseUrl( request ) + JSP_CREATION_ENTREE_ORDRE_DU_JOUR + '?' + CONSTANTE_SESSION +
            '=' + CONSTANTE_SESSION;
        }
        else
        {
            // retour sur l'interface de modification
            return AppPathService.getBaseUrl( request ) + JSP_MODIFICATION_ENTREE_ORDRE_DU_JOUR + '?' +
            CONSTANTE_SESSION + '=' + CONSTANTE_SESSION;
        }
    }

    /**
     * insere un voeu dans l'entrée de l'ordre du jour
     *
     * @param request
     *            la requête Http
     * @return l'url de traitement de l'entrée d'ordre du jour
     */
    public String doSelectionVoeuAmendement( HttpServletRequest request )
    {
        // Si l'action effectuée est "Annuler" on en fait rien.
        if ( ( ( request.getParameter( OdsParameters.A_FAIRE ) != null ) &&
                !request.getParameter( OdsParameters.A_FAIRE ).equals( CONSTANTE_CANCEL ) ) ||
                ( request.getParameter( OdsParameters.A_FAIRE ) == null ) )
        {
            // Si l'id de l'amendement n'est pas donné en paramètre => Retour à
            // la case départ
            if ( ( request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ) == null ) ||
                    request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_PAS_DE_VOEU_CHOISI, AdminMessage.TYPE_STOP );
            }

            // On récupère l'id de l'amendement et si il est invalide => retour
            // à la case départ
            int nIdVA = -1;

            try
            {
                nIdVA = Integer.parseInt( request.getParameter( OdsParameters.ID_VOEUAMENDEMENT ) );
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
            _entreeOrdreDuJourBean.setVoeuAmendement( VoeuAmendementHome.findByPrimaryKey( nIdVA, getPlugin(  ) ) );
        }

        if ( _entreeOrdreDuJourBean.getIdEntreeOrdreDuJour(  ) == -1 )
        {
            // Si l'id de l'entree en session =-1 retour sur l'interface de
            // creation
            return AppPathService.getBaseUrl( request ) + JSP_CREATION_ENTREE_ORDRE_DU_JOUR + '?' + CONSTANTE_SESSION +
            '=' + CONSTANTE_SESSION;
        }
        else
        {
            // retour sur l'interface de modification
            return AppPathService.getBaseUrl( request ) + JSP_MODIFICATION_ENTREE_ORDRE_DU_JOUR + '?' +
            CONSTANTE_SESSION + '=' + CONSTANTE_SESSION;
        }
    }

    /**
     * methode qui crée pour chaque voeux selectionnés une entrée d'ordre du
     * jour
     *
     * @param request
     *            la requête Http
     * @return retour sur l'ordre du jour en cours de modification
     */
    public String doSelectionVoeuxAmendements( HttpServletRequest request )
    {
        if ( null != request.getParameter( OdsParameters.ID_ENTREE ) )
        {
            EntreeOrdreDuJour entreeOrdreDuJour;
            Plugin plugin = getPlugin(  );

            try
            {
                entreeOrdreDuJour = EntreeOrdreDuJourHome.findByPrimaryKey( Integer.parseInt( request.getParameter( 
                                OdsParameters.ID_ENTREE ) ), plugin );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getHomeUrl( request );
            }

            if ( request.getParameter( CONSTANTE_ANNULER ) == null )
            {
                // recupération de la liste des Id pdds à ajouter au va
                String[] strListeVoeu = request.getParameterValues( OdsParameters.LISTE_VA_CHECKED );

                if ( ( strListeVoeu != null ) && ( strListeVoeu.length != 0 ) )
                {
                    // on recupere l'entrée d'ordre du jour dans le but de
                    // recuperer
                    // son groupe de commissions et son groupe de rapporteurs
                    EntreeOrdreDuJour newEntree;
                    VoeuAmendement voeuAmendement;
                    List<EntreeOrdreDuJour> listEntreeATrier = new ArrayList<EntreeOrdreDuJour>(  );

                    for ( String strIdVoeu : strListeVoeu )
                    {
                        int nIdVa;

                        try
                        {
                            nIdVa = Integer.parseInt( strIdVoeu );
                            voeuAmendement = VoeuAmendementHome.findByPrimaryKey( nIdVa, plugin );
                        }
                        catch ( NumberFormatException ne )
                        {
                            return getHomeUrl( request );
                        }

                        newEntree = new EntreeOrdreDuJour(  );
                        newEntree.setVoeuAmendement( voeuAmendement );
                        newEntree.setReference( voeuAmendement.getReferenceComplete(  ) );
                        newEntree.setType( CONSTANTE_ENTREE_TYPE_VOEU );
                        newEntree.setNature( entreeOrdreDuJour.getNature(  ) );
                        newEntree.setOrdreDuJour( entreeOrdreDuJour.getOrdreDuJour(  ) );
                        newEntree.setIdEntreeOrdreDuJour( EntreeOrdreDuJourHome.create( newEntree, plugin ) );

                        if ( _listIdNouvelleEntree == null )
                        {
                            _listIdNouvelleEntree = new ArrayList<Integer>(  );
                        }

                        // on ajoute a la liste des nouvelles entrées l'id de la
                        // nouvelle entrée
                        _listIdNouvelleEntree.add( newEntree.getIdEntreeOrdreDuJour(  ) );

                        listEntreeATrier.add( newEntree );
                    }

                    // Les commissions et les rapporteurs des entrées qui sont
                    // créées sont celles du groupe choisi.
                    for ( EntreeOrdreDuJour entree : listEntreeATrier )
                    {
                        entree.setCommissions( entreeOrdreDuJour.getCommissions(  ) );
                        entree.setElus( entreeOrdreDuJour.getElus(  ) );

                        for ( Commission commission : entree.getCommissions(  ) )
                        {
                            EntreeOrdreDuJourHome.insertCommission( entree.getIdEntreeOrdreDuJour(  ),
                                commission.getIdCommission(  ), plugin );
                        }

                        for ( Elu elu : entree.getElus(  ) )
                        {
                            EntreeOrdreDuJourHome.insertRapporteur( entree.getIdEntreeOrdreDuJour(  ),
                                elu.getIdElu(  ), plugin );
                        }
                    }

                    // on reclasse l'ordre du jour en mode automatique
                    classeListEntrees( listEntreeATrier );
                }
                else
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_PAS_DE_VOEU_CHOISI,
                        AdminMessage.TYPE_STOP );
                }
            }

            return getJspModificationOdj( request, entreeOrdreDuJour.getOrdreDuJour(  ).getIdOrdreDuJour(  ) );
        }
        else
        {
            return getHomeUrl( request );
        }
    }

    /**
     * methode qui classe automatiquement la nouvelle entrée fournie en
     * parametre dans la liste des entrées de l'ordre du jour
     *
     * @param newEntree
     *            l'entrée a classer dans la liste des entrées
     */
    private void classeEntree( EntreeOrdreDuJour newEntree )
    {
        Plugin plugin = getPlugin(  );

        // initialistaion du numero d'ordre de la premiere entrée dans la liste
        // a 1
        int nCpt = 1;

        // on recupere la liste des entées de l'ordre du jour
        OrdreDuJour ordreDuJour = newEntree.getOrdreDuJour(  );
        ordreDuJour.setListEntrees( EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( 
                ordreDuJour.getIdOrdreDuJour(  ), plugin ) );
        // on supprime l'entrée à positionner de la liste des entrées
        removeEntreeInTheList( newEntree.getIdEntreeOrdreDuJour(  ), ordreDuJour.getListEntrees(  ) );

        int nNewEntreePosition = getPositionNewEntree( newEntree, ordreDuJour, plugin );
        // on insere la nouvelle entrée dans la liste des entrées
        ordreDuJour.getListEntrees(  ).add( nNewEntreePosition, newEntree );

        for ( EntreeOrdreDuJour entree : ordreDuJour.getListEntrees(  ) )
        {
            entree.setNumeroOrdre( nCpt );
            EntreeOrdreDuJourHome.update( entree, plugin );
            nCpt++;
        }
    }

    /**
     * methode qui classe automatiquement la liste des entrées fournies en
     * parametre dans la liste des entrées de l'ordre du jour
     *
     * @param entrees
     *            la liste des entrées a classer dans la liste des entrées
     */
    private void classeListEntrees( List<EntreeOrdreDuJour> entrees )
    {
        Plugin plugin = getPlugin(  );
        OrdreDuJour ordreDuJour = null;

        if ( entrees.size(  ) != 0 )
        {
            ordreDuJour = entrees.get( 0 ).getOrdreDuJour(  );
        }

        ordreDuJour = OrdreDuJourHome.findByPrimaryKey( ordreDuJour.getIdOrdreDuJour(  ), plugin );
        ordreDuJour.setListEntrees( EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( 
                ordreDuJour.getIdOrdreDuJour(  ), plugin ) );

        int nCpt = 1;

        for ( EntreeOrdreDuJour newEntree : entrees )
        {
            // on supprime l'entrée à positionner de la liste des entrées
            removeEntreeInTheList( newEntree.getIdEntreeOrdreDuJour(  ), ordreDuJour.getListEntrees(  ) );

            int nNewEntreePosition = getPositionNewEntree( newEntree, ordreDuJour, plugin );
            // on insere la nouvelle entrée dans la liste des entrées
            ordreDuJour.getListEntrees(  ).add( nNewEntreePosition, newEntree );
        }

        // update sur la liste des entrées
        for ( EntreeOrdreDuJour entree : ordreDuJour.getListEntrees(  ) )
        {
            entree.setNumeroOrdre( nCpt );
            EntreeOrdreDuJourHome.update( entree, plugin );
            nCpt++;
        }
    }

    /**
     * methode qui reclasse toutes les entrées de l'ordre du jour en mode
     * automatique
     *@param ordreDuJour l'ordre du jour a classer en mode automatique
     *@param plugin plugin
     */
    public static void classeAllEntrees( OrdreDuJour ordreDuJour, Plugin plugin )
    {
        ordreDuJour.setListEntrees( EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( 
                ordreDuJour.getIdOrdreDuJour(  ), plugin ) );

        List<EntreeOrdreDuJour> listAllEntrees = new ArrayList<EntreeOrdreDuJour>(  );
        listAllEntrees.addAll( ordreDuJour.getListEntrees(  ) );

        int nCpt = 1;

        for ( EntreeOrdreDuJour newEntree : listAllEntrees )
        {
            // on supprime l'entrée à positionner de la liste des entrées
            removeEntreeInTheList( newEntree.getIdEntreeOrdreDuJour(  ), ordreDuJour.getListEntrees(  ) );

            int nNewEntreePosition = getPositionNewEntree( newEntree, ordreDuJour, plugin );
            // on insere la nouvelle entrée dans la liste des entrées
            ordreDuJour.getListEntrees(  ).add( nNewEntreePosition, newEntree );
        }

        // update sur la liste des entrées
        for ( EntreeOrdreDuJour entree : ordreDuJour.getListEntrees(  ) )
        {
            entree.setNumeroOrdre( nCpt );
            EntreeOrdreDuJourHome.update( entree, plugin );
            nCpt++;
        }
    }

    /**
     * retoune la position ou doit etre inséré l'entrée newEntre dans la liste
     * des entrées d'ordre du jour
     * @param newEntree l'entrée à positionner
     * @param ordreDuJour l'ordre du jour en cours de modification
     * @param plugin plugin
     * @return retoune la position ou doit etre inséré l'entrée newEntre
     */
    private static int getPositionNewEntree( EntreeOrdreDuJour newEntree, OrdreDuJour ordreDuJour, Plugin plugin )
    {
        List<EntreeOrdreDuJour> listentreesOdj = ordreDuJour.getListEntrees(  );
        int nPosition = 0;
        List<Commission> commissionsNewEntree = newEntree.getCommissions(  );
        List<Elu> elusNewEntree = newEntree.getElus(  );
        List<Tourniquet> tourniquets = TourniquetHome.listTourniquet( plugin );

        for ( EntreeOrdreDuJour entree : listentreesOdj )
        {
            List<Commission> commissionsEntree = entree.getCommissions(  );
            List<Elu> elusEntree = entree.getElus(  );

            // Les textes libres sont placés avant le premier groupe par nature.
            // Ils sont ordonnés entre eux par ordre d’introduction dans l’ordre
            // du jour.
            if ( newEntree.getType(  ).equals( CONSTANTE_ENTREE_TYPE_TEXTE ) &&
                    !entree.getType(  ).equals( CONSTANTE_ENTREE_TYPE_TEXTE ) )
            {
                break;
            }
            else if ( newEntree.getType(  ).equals( CONSTANTE_ENTREE_TYPE_TEXTE ) &&
                    entree.getType(  ).equals( CONSTANTE_ENTREE_TYPE_TEXTE ) )
            {
                // Quand la nature d’une entrée n’est pas définie, cette entrée
                // précède les entrées regroupées par nature.
                if ( ( newEntree.getNature(  ) == null ) && ( entree.getNature(  ) != null ) )
                {
                    break;
                }
                else if ( ( newEntree.getNature(  ) != null ) && ( entree.getNature(  ) != null ) &&
                        ( newEntree.getNature(  ).getNumeroNature(  ) < entree.getNature(  ).getNumeroNature(  ) ) )
                {
                    break;
                }
                else if ( ( newEntree.getNature(  ) == null ) && ( entree.getNature(  ) == null ) )
                {
                    break;
                }
            }
            else
            {
                // Quand la nature d’une entrée n’est pas définie, cette entrée
                // précède les entrées regroupées par nature.
                if ( ( newEntree.getNature(  ) == null ) && ( entree.getNature(  ) != null ) )
                {
                    break;
                }

                else if ( ( newEntree.getNature(  ) != null ) && ( entree.getNature(  ) != null ) &&
                        ( newEntree.getNature(  ).getNumeroNature(  ) < entree.getNature(  ).getNumeroNature(  ) ) )
                {
                    break;
                }
                else if ( ( entree.getNature(  ) != null ) && ( entree.getNature(  ) != null ) &&
                        ( newEntree.getNature(  ).getNumeroNature(  ) == entree.getNature(  ).getNumeroNature(  ) ) )
                {
                    // Quand une entrée n’est associée à aucune commission,
                    // elle précède celles regroupées par groupe de commissions
                    if ( ( commissionsNewEntree == null ) || ( commissionsNewEntree.size(  ) == 0 ) )
                    {
                        break;
                    }

                    // tri par numéro de la commission en première position dans
                    // le groupe
                    // si le tourniquet n’est pas appliqué sinon par ordre de
                    // cette commission dans le tourniquet
                    else if ( ( commissionsEntree != null ) && ( commissionsEntree.size(  ) != 0 ) )
                    {
                        if ( !ordreDuJour.getTourniquet(  ) &&
                                ( commissionsNewEntree.get( 0 ).getNumero(  ) < commissionsEntree.get( 0 ).getNumero(  ) ) )
                        {
                            break;
                        }
                        else if ( ordreDuJour.getTourniquet(  ) &&
                                ( compareOrdreInTourniquet( commissionsNewEntree.get( 0 ), commissionsEntree.get( 0 ),
                                    tourniquets ) == 1 ) )
                        {
                            break;
                        }

                        else if ( commissionsNewEntree.get( 0 ).getNumero(  ) == commissionsEntree.get( 0 ).getNumero(  ) )
                        {
                            // puis par nombre de commissions dans le groupe
                            if ( commissionsNewEntree.size(  ) < commissionsEntree.size(  ) )
                            {
                                break;
                            }

                            else if ( commissionsNewEntree.size(  ) == commissionsEntree.size(  ) )
                            {
                                boolean bNewGroupeCommissionInf = false;
                                boolean bNewGroupeCommissionEgale = true;

                                // puis par numéros des commissions autres que
                                // la première
                                for ( int ncpt = 1; ncpt < commissionsNewEntree.size(  ); ncpt++ )
                                {
                                    if ( commissionsNewEntree.get( ncpt ).getNumero(  ) < commissionsEntree.get( ncpt )
                                                                                                               .getNumero(  ) )
                                    {
                                        bNewGroupeCommissionInf = true;

                                        break;
                                    }
                                    else if ( commissionsNewEntree.get( ncpt ).getNumero(  ) > commissionsEntree.get( 
                                                ncpt ).getNumero(  ) )
                                    {
                                        bNewGroupeCommissionEgale = false;
                                    }
                                }

                                if ( bNewGroupeCommissionInf )
                                {
                                    break;
                                }
                                else if ( bNewGroupeCommissionEgale )
                                {
                                    // Quand une entrée n’est associée à aucun
                                    // rapporteur, elle précède celles
                                    // regroupées par groupe de rapporteurs
                                    if ( ( elusNewEntree == null ) || ( elusNewEntree.size(  ) == 0 ) )
                                    {
                                        break;
                                    }

                                    // Au sein d’un groupe de commissions, les
                                    // groupes de rapporteurs sont ordonnés
                                    // entre eux :
                                    // par nombre de rapporteurs
                                    else if ( ( elusEntree != null ) && ( elusNewEntree.size(  ) < elusEntree.size(  ) ) )
                                    {
                                        break;
                                    }
                                    else if ( ( elusEntree != null ) &&
                                            ( elusNewEntree.size(  ) == elusEntree.size(  ) ) )
                                    {
                                        boolean bNewGroupeEluInf = false;
                                        boolean bNewGroupeEluEgale = true;

                                        // par ordre croissant du numéro de la
                                        // commission
                                        for ( int ncpt = 0; ncpt < elusNewEntree.size(  ); ncpt++ )
                                        {
                                            if ( elusNewEntree.get( ncpt ).getCommission(  ).getNumero(  ) < elusEntree.get( 
                                                        ncpt ).getCommission(  ).getNumero(  ) )
                                            {
                                                bNewGroupeEluInf = true;

                                                break;
                                            }
                                            else if ( elusNewEntree.get( ncpt ).getCommission(  ).getNumero(  ) > elusEntree.get( 
                                                        ncpt ).getCommission(  ).getNumero(  ) )
                                            {
                                                bNewGroupeEluEgale = false;
                                            }
                                        }

                                        if ( bNewGroupeEluInf )
                                        {
                                            break;
                                        }
                                        else if ( bNewGroupeEluEgale )
                                        {
                                            // puis par ordre alphabétique des
                                            // noms des rapporteurs
                                            bNewGroupeEluInf = false;
                                            bNewGroupeEluEgale = true;

                                            // par ordre croissant du numéro de
                                            // la commission
                                            for ( int ncpt = 0; ncpt < elusNewEntree.size(  ); ncpt++ )
                                            {
                                                if ( elusNewEntree.get( ncpt ).getNomElu(  )
                                                                      .compareTo( elusEntree.get( ncpt ).getNomElu(  ) ) < 0 )
                                                {
                                                    bNewGroupeEluInf = true;

                                                    break;
                                                }
                                                else if ( elusNewEntree.get( ncpt ).getNomElu(  )
                                                                           .compareTo( elusEntree.get( ncpt ).getNomElu(  ) ) > 0 )
                                                {
                                                    bNewGroupeEluEgale = false;
                                                }
                                            }

                                            if ( bNewGroupeEluInf )
                                            {
                                                break;
                                            }
                                            else if ( bNewGroupeEluEgale &&
                                                    newEntree.getType(  ).equals( CONSTANTE_ENTREE_TYPE_PDD ) &&
                                                    entree.getType(  ).equals( CONSTANTE_ENTREE_TYPE_VOEU ) )
                                            {
                                                // les PDD sont placés avant les
                                                // vœux non rattachés
                                                break;
                                            }
                                            else if ( bNewGroupeEluEgale &&
                                                    newEntree.getType(  ).equals( CONSTANTE_ENTREE_TYPE_PDD ) &&
                                                    entree.getType(  ).equals( CONSTANTE_ENTREE_TYPE_PDD ) &&
                                                    ( newEntree.getReference(  ).compareTo( entree.getReference(  ) ) < 0 ) )
                                            {
                                                // Les PDD sont classés de
                                                // référence affichée
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // on incremente la position
            nPosition++;
        }

        return nPosition;
    }

    /**
     * methode qui test si l'elu d'id nkey se trouve dans la liste des
     * rapporteurs listRapporteurs
     *
     * @param listRapporteurs
     *            la liste des rapporteurs de l'entrée en cours de traitement
     * @param nKey
     *            id de l'élu
     * @return true si l'elu se trouve dans la liste des rapporteurs,false sinon
     */
    private Boolean isInTheEluList( int nKey, List<Elu> listRapporteurs )
    {
        for ( Elu elu : listRapporteurs )
        {
            if ( elu.getIdElu(  ) == nKey )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * methode qui test si la commission d'id nkey se trouve dans la liste des
     * commissions listCommission
     *
     * @param listCommission
     *            la liste des rapporteurs de l'entrée en cours de traitement
     * @param nKey
     *            id de la commission
     * @return true si la commission se trouve dans la liste des
     *         commissio,s,false sinon
     */
    private Boolean isInTheCommissionList( int nKey, List<Commission> listCommission )
    {
        for ( Commission commission : listCommission )
        {
            if ( commission.getIdCommission(  ) == nKey )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * méthode qui recupère les parametres fournis par l'utilisateur en vue de
     * les insérer dans l'entree d'ordre du jour stocké en session
     *
     * @param request
     *            request
     * @return retourne true si aucune erreur ne c'est produite
     */
    private Boolean setEntreeOrdreDuJour( HttpServletRequest request )
    {
        _entreeOrdreDuJourBean.setReference( request.getParameter( OdsParameters.REFERENCE ) );
        _entreeOrdreDuJourBean.setObjet( request.getParameter( OdsParameters.OBJET ) );

        if ( null != request.getParameter( OdsParameters.ID_NATURE ) )
        {
            try
            {
                int nIdNature = Integer.parseInt( request.getParameter( OdsParameters.ID_NATURE ) );

                if ( ( nIdNature != -1 ) && ( null == _entreeOrdreDuJourBean.getNature(  ) ) )
                {
                    NatureDesDossiers natureDesDossiers = new NatureDesDossiers(  );
                    natureDesDossiers.setIdNature( nIdNature );
                    _entreeOrdreDuJourBean.setNature( natureDesDossiers );
                }
                else if ( nIdNature != -1 )
                {
                    // nIdNature!=-1 et l'objet nature n'est pas vide
                    _entreeOrdreDuJourBean.getNature(  ).setIdNature( nIdNature );
                }
                else
                {
                    // nIdNature egale a -1
                    _entreeOrdreDuJourBean.setNature( null );
                }
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return false;
            }
        }

        if ( null != request.getParameter( OdsParameters.STYLE ) )
        {
            String strStyle = request.getParameter( OdsParameters.STYLE ).trim(  );

            if ( !strStyle.equals( OdsConstants.FILTER_ALL ) )
            {
                _entreeOrdreDuJourBean.setStyle( strStyle );
            }
            else
            {
                _entreeOrdreDuJourBean.setStyle( null );
            }
        }

        return true;
    }

    /**
     * test si l'ensemble des parametres contenus dans l'objet EntreeOrdreDujour
     * en session permettent une insertion ou une modification de l'entrée en
     * cours de traitement.
     *
     * @param listErreurs
     *            la liste des erreurs
     * @return true si aucune erreur n'est constaté
     */
    private boolean isFieldRequired( List<String> listErreurs )
    {
        boolean bIsFieldRequired = true;

        if ( _entreeOrdreDuJourBean.getType(  ).equals( CONSTANTE_ENTREE_TYPE_PDD ) &&
                ( _entreeOrdreDuJourBean.getPdd(  ) == null ) &&
                ( ( _entreeOrdreDuJourBean.getReference(  ) == null ) ||
                _entreeOrdreDuJourBean.getReference(  ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) )
        {
            listErreurs.add( PROPERTY_LABEL_REFERENCE );
            bIsFieldRequired = false;
        }

        if ( _entreeOrdreDuJourBean.getType(  ).equals( CONSTANTE_ENTREE_TYPE_PDD ) &&
                ( ( _entreeOrdreDuJourBean.getObjet(  ) == null ) ||
                _entreeOrdreDuJourBean.getObjet(  ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) ) )
        {
            listErreurs.add( PROPERTY_LABEL_OBJET );
            bIsFieldRequired = false;
        }

        return bIsFieldRequired;
    }

    /**
     * retourne l'url de modification de l'ordre du jour d'id nIdOdj
     *
     * @param request
     *            request
     * @param nIdOdj
     *            l'id de l'ordre du jour
     * @return l'url de modification de l'ordre du jour d'id nIdOdj
     */
    private String getJspModificationOdj( HttpServletRequest request, int nIdOdj )
    {
        return AppPathService.getBaseUrl( request ) + JSP_MODIFICATION_ORDRE_DU_JOUR + "?" + OdsParameters.ID_ODJ +
        "=" + nIdOdj;
    }

    /**
     * methode qui supprime l'elu d'id nkey de la liste des rapporteurs elus
     *
     * @param elus
     *            la liste des rapporteurs de l'entrée en cours de traitement
     * @param nKey
     *            id de l'élu
     */
    private void removeEluInTheList( int nKey, List<Elu> elus )
    {
        Elu eluToRemove = null;

        for ( Elu elu : elus )
        {
            if ( elu.getIdElu(  ) == nKey )
            {
                eluToRemove = elu;

                break;
            }
        }

        if ( eluToRemove != null )
        {
            elus.remove( eluToRemove );
        }
    }

    /**
     * methode qui supprime la commission d'id nkey de la liste des commissions
     *
     * @param commissions
     *            la liste des commissions de l'entrée en cours de traitement
     * @param nKey
     *            id de la commission
     */
    private void removeCommissionInTheList( int nKey, List<Commission> commissions )
    {
        Commission commissionToRemove = null;

        for ( Commission commission : commissions )
        {
            if ( commission.getIdCommission(  ) == nKey )
            {
                commissionToRemove = commission;

                break;
            }
        }

        if ( commissionToRemove != null )
        {
            commissions.remove( commissionToRemove );
        }
    }

    /**
     * supprime l'entrée d'id nkey de la liste des entrées d'ordre du jour
     *
     * @param nKey
     *            id de l'entrée à supprimer
     * @param entrees
     *            la liste des entées
     */
    private static void removeEntreeInTheList( int nKey, List<EntreeOrdreDuJour> entrees )
    {
        EntreeOrdreDuJour entreeToRemove = null;

        for ( EntreeOrdreDuJour entree : entrees )
        {
            if ( entree.getIdEntreeOrdreDuJour(  ) == nKey )
            {
                entreeToRemove = entree;

                break;
            }
        }

        if ( entreeToRemove != null )
        {
            entrees.remove( entreeToRemove );
        }
    }

    /**
     * methode qui compare l'ordre de passage dans le tourniquet de la
     * commission 'commissionNewEntree' avec la commission 'commissionEntree'.
     * Methode qui renvoie 1 si commissionNewEntree est avant commissionEntree 0
     * si l'ordre est identique -1 si commissionNewEntree est après
     * commissionEntree
     *
     * @param commissionNewEntree
     *            commission
     * @param commissionEntree
     *            commission
     * @param tourniquets
     *            le tourniquet de la prochaine seance
     * @return 1 si commissionNewEntree est avant commissionEntree 0 si l'ordre
     *         est identique -1 si commissionNewEntree est après
     *         commissionEntree
     */
    private static int compareOrdreInTourniquet( Commission commissionNewEntree, Commission commissionEntree,
        List<Tourniquet> tourniquets )
    {
        Tourniquet tourniquetNewEntree = new Tourniquet(  );
        tourniquetNewEntree.setCommission( commissionNewEntree );

        Tourniquet tourniquetEntree = new Tourniquet(  );
        tourniquetEntree.setCommission( commissionEntree );

        for ( Tourniquet tourniquet : tourniquets )
        {
            if ( tourniquet.getCommission(  ).getIdCommission(  ) == tourniquetNewEntree.getCommission(  )
                                                                                            .getIdCommission(  ) )
            {
                tourniquetNewEntree = tourniquet;
            }

            if ( tourniquet.getCommission(  ).getIdCommission(  ) == tourniquetEntree.getCommission(  ).getIdCommission(  ) )
            {
                tourniquetEntree = tourniquet;
            }
        }

        if ( tourniquetNewEntree.getNumeroOrdre(  ) < tourniquetEntree.getNumeroOrdre(  ) )
        {
            return 1;
        }
        else if ( tourniquetNewEntree.getNumeroOrdre(  ) == tourniquetEntree.getNumeroOrdre(  ) )
        {
            return 0;
        }
        else
        {
            return -1;
        }
    }

    /**
     * methode qui active ou desactive le tourniquer pour reclasser les entrees
     * d'ordre du jour
     *
     * @param request
     *            request
     * @return l'url de modification de l'ordre du jour en cours de traitement
     */
    public String activeTourniquet( HttpServletRequest request )
    {
        Plugin plugin = getPlugin(  );

        if ( request.getParameter( OdsParameters.ID_ODJ ) != null )
        {
            int nIdOdj;

            try
            {
                nIdOdj = Integer.parseInt( request.getParameter( OdsParameters.ID_ODJ ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getHomeUrl( request );
            }

            OrdreDuJour ordreDuJour = OrdreDuJourHome.findByPrimaryKey( nIdOdj, plugin );

            if ( !ordreDuJour.getTourniquet(  ) && ( request.getParameter( CONSTANTE_APPLIQUER ) != null ) )
            {
                ordreDuJour.setTourniquet( true );
                OrdreDuJourHome.update( ordreDuJour, plugin );
                classeAllEntrees( ordreDuJour, plugin );
            }
            else if ( ordreDuJour.getTourniquet(  ) && ( request.getParameter( CONSTANTE_ANNULER ) != null ) )
            {
                ordreDuJour.setTourniquet( false );
                OrdreDuJourHome.update( ordreDuJour, plugin );
                classeAllEntrees( ordreDuJour, plugin );
            }

            return getJspModificationOdj( request, nIdOdj );
        }

        return getHomeUrl( request );
    }

    /**
     * methode qui gere l'ensemble des traitements effectués sur la liste des
     * entrées d'ordre du jour en cours de traitement
     *
     * @param request
     *            request
     * @return l'url de modification de l'ordre du jour en cours de traitement
     */
    public String doTraitementEntreeOdj( HttpServletRequest request )
    {
        Plugin plugin = getPlugin(  );
        String strUrlRetour = getHomeUrl( request );

        if ( request.getParameter( OdsParameters.ID_ODJ ) != null )
        {
            String strIdOdj = request.getParameter( OdsParameters.ID_ODJ );

            if ( request.getParameter( CONSTANTE_AJOUTER_PDD ) != null )
            {
                // demande de creation d'une entrée de type pdd
                strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_CREATION_ENTREE_ORDRE_DU_JOUR + "?" +
                    OdsParameters.TYPE + "=" + CONSTANTE_ENTREE_TYPE_PDD + "&" + OdsParameters.ID_ODJ + "=" + strIdOdj;
            }

            else if ( request.getParameter( CONSTANTE_AJOUTER_TEXTE ) != null )
            {
                // demande de creation d'une entrée de type texte
                strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_CREATION_ENTREE_ORDRE_DU_JOUR + "?" +
                    OdsParameters.TYPE + "=" + CONSTANTE_ENTREE_TYPE_TEXTE + "&" + OdsParameters.ID_ODJ + "=" +
                    strIdOdj;
            }
            else if ( request.getParameter( CONSTANTE_AJOUTER_VOEU ) != null )
            {
                // demande de creation d'une entrée de type voeu
                strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_CREATION_ENTREE_ORDRE_DU_JOUR + "?" +
                    OdsParameters.TYPE + "=" + CONSTANTE_ENTREE_TYPE_VOEU + "&" + OdsParameters.ID_ODJ + "=" +
                    strIdOdj;
            }
            else if ( ( request.getParameter( OdsParameters.TYPE ) != null ) &&
                    request.getParameter( OdsParameters.TYPE ).trim(  ).equals( CONSTANTE_AJOUTER_DES_PROJETS ) )
            {
                // demande de creation d'une liste d' entrées de type projets de
                // délibération
                if ( request.getParameter( OdsParameters.ID_ENTREE ) != null )
                {
                    String strIdEntree = request.getParameter( OdsParameters.ID_ENTREE );
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_SELECTION_PROJETS + "?" +
                        OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_ENTREE + "=" + strIdEntree;
                }
            }
            else if ( ( request.getParameter( OdsParameters.TYPE ) != null ) &&
                    request.getParameter( OdsParameters.TYPE ).trim(  ).equals( CONSTANTE_AJOUTER_DES_PROPOSITIONS ) )
            {
                // demande  de creation d'une liste d' entrées  de type propositions de délibération
                if ( request.getParameter( OdsParameters.ID_ENTREE ) != null )
                {
                    String strIdEntree = request.getParameter( OdsParameters.ID_ENTREE );
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_SELECTION_PROPOSITIONS + "?" +
                        OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_ENTREE + "=" + strIdEntree;
                }
            }
            else if ( ( request.getParameter( OdsParameters.TYPE ) != null ) &&
                    request.getParameter( OdsParameters.TYPE ).trim(  ).equals( CONSTANTE_AJOUTER_DES_VOEUX ) )
            {
                // demande de  creation d'une liste d' entrées de type voeux
                if ( request.getParameter( OdsParameters.ID_ENTREE ) != null )
                {
                    String strIdEntree = request.getParameter( OdsParameters.ID_ENTREE );
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_SELECTION_VOEUX + "?" +
                        OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_ENTREE + "=" + strIdEntree;
                }
            }
            else if ( ( request.getParameter( CONSTANTE_AJOUTER_AVANT_SELECTION ) != null ) ||
                    ( request.getParameter( CONSTANTE_AJOUTER_APRES_SELECTION ) != null ) )
            {
                // demande de creation d'une entrée d'ordre du jour avant ou
                // aprés
                // l'entrée d'ordre du jour selectionnée
                _nIdPositionInsert = -1;

                // recupération de la liste des entrées selectionnées
                String[] strListEntree = request.getParameterValues( OdsParameters.LISTE_ENTREE_CHECKED );

                if ( ( request.getParameter( OdsParameters.TYPE ) != null ) &&
                        ( request.getParameter( OdsParameters.TYPE ).trim(  ) != OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    if ( ( strListEntree != null ) && ( strListEntree.length != 0 ) )
                    {
                        try
                        {
                            int nIdEntree;
                            EntreeOrdreDuJour entree;

                            if ( request.getParameter( CONSTANTE_AJOUTER_AVANT_SELECTION ) != null )
                            {
                                nIdEntree = Integer.parseInt( strListEntree[0] );
                                entree = EntreeOrdreDuJourHome.findByPrimaryKey( nIdEntree, plugin );
                                _nIdPositionInsert = entree.getNumeoOrdre(  ) - 1;
                            }
                            else
                            {
                                nIdEntree = Integer.parseInt( strListEntree[strListEntree.length - 1] );
                                entree = EntreeOrdreDuJourHome.findByPrimaryKey( nIdEntree, plugin );
                                _nIdPositionInsert = entree.getNumeoOrdre(  );
                            }

                            String strType = request.getParameter( OdsParameters.TYPE );
                            strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_CREATION_ENTREE_ORDRE_DU_JOUR +
                                "?" + OdsParameters.TYPE + "=" + strType + "&" + OdsParameters.ID_ODJ + "=" + strIdOdj;
                        }
                        catch ( NumberFormatException ne )
                        {
                            return getHomeUrl( request );
                        }
                    }
                    else
                    {
                        //pas d'entrée choisie
                        try
                        {
                            int nIdOdj = Integer.parseInt( strIdOdj );

                            if ( EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( nIdOdj, plugin ).size(  ) == 0 )
                            {
                                //pas d'entree dans la liste des entrees d'ordre du jour on insere 
                                //la nouvelle entree en premiere position 
                                _nIdPositionInsert = 0;

                                String strType = request.getParameter( OdsParameters.TYPE );
                                strUrlRetour = AppPathService.getBaseUrl( request ) +
                                    JSP_CREATION_ENTREE_ORDRE_DU_JOUR + "?" + OdsParameters.TYPE + "=" + strType + "&" +
                                    OdsParameters.ID_ODJ + "=" + strIdOdj;
                            }
                            else
                            { //message pas d'entree choisie
                                strUrlRetour = AdminMessageService.getMessageUrl( request, MESSAGE_PAS_ENTREE_CHOISIE,
                                        AdminMessage.TYPE_STOP );
                            }
                        }
                        catch ( NumberFormatException ne )
                        {
                            AppLogService.error( ne );

                            return getHomeUrl( request );
                        }
                    }
                }
                else
                {
                    //pas de type d'entré choisi
                    // on retourne sur l'interface de modification de l'
                    // ODJ concerné
                    try
                    {
                        int nIdOdj = Integer.parseInt( strIdOdj );
                        strUrlRetour = getJspModificationOdj( request, nIdOdj );
                    }
                    catch ( NumberFormatException ne )
                    {
                        AppLogService.error( ne );
                    }
                }
            }

            else if ( request.getParameter( CONSTANTE_SUPPRIMER_SELECTION ) != null )
            {
                // demande de suppression des entées selectionnées
                // recupération de la liste des entrées à supprimer
                String[] strListEntree = request.getParameterValues( OdsParameters.LISTE_ENTREE_CHECKED );

                if ( ( strListEntree != null ) && ( strListEntree.length != 0 ) )
                {
                    _strTabIdSelect = strListEntree;

                    UrlItem url = new UrlItem( JSP_DO_SUPPRESSION_LISTE_ENTREE );
                    url.addParameter( OdsParameters.ID_ODJ, strIdOdj );
                    strUrlRetour = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRME_DELETE_LISTE_ENTREE,
                            url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
                }

                else
                {
                    strUrlRetour = AdminMessageService.getMessageUrl( request, MESSAGE_PAS_ENTREE_CHOISIE,
                            AdminMessage.TYPE_STOP );
                }
            }
            else if ( request.getParameter( CONSTANTE_DEPLACER_SELECTION ) != null )
            {
                // recupération de la liste des entrées à deplacer
                String[] strListEntree = request.getParameterValues( OdsParameters.LISTE_ENTREE_CHECKED );

                if ( ( strListEntree != null ) && ( strListEntree.length != 0 ) )
                {
                    try
                    {
                        int nIdOdj = Integer.parseInt( strIdOdj );
                        List<EntreeOrdreDuJour> listEntree = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( nIdOdj,
                                getPlugin(  ) );

                        if ( ( listEntree.size(  ) <= 1 ) || ( strListEntree.length == listEntree.size(  ) ) )
                        {
                            // si il n'y a qu'une entrée dans la liste ou si la selection concerne toutes les entrées
                            // on retourne directement à l'interface de modification
                            strUrlRetour = getJspModificationOdj( request, nIdOdj );
                        }
                        else
                        {
                            _strTabIdSelect = strListEntree;
                            strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_SELECTION_ENTREE_ORDRE_DU_JOUR +
                                "?" + OdsParameters.ID_ODJ + "=" + strIdOdj;
                        }
                    }
                    catch ( NumberFormatException ne )
                    {
                        return getHomeUrl( request );
                    }
                }

                else
                {
                    strUrlRetour = AdminMessageService.getMessageUrl( request, MESSAGE_PAS_ENTREE_CHOISIE,
                            AdminMessage.TYPE_STOP );
                }
            }
            else if ( request.getParameter( CONSTANTE_GENERER_ADDITIF ) != null )
            {
                // demande de creation d'un additif a partir de la liste des
                // entrées selectionnées
                String[] strListEntree = request.getParameterValues( OdsParameters.LISTE_ENTREE_CHECKED );

                if ( ( strListEntree != null ) && ( strListEntree.length != 0 ) )
                {
                    _strTabIdSelect = strListEntree;
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_DO_GENERER_ADDITIF + "?" +
                        OdsParameters.ID_ODJ + "=" + strIdOdj;
                }

                else
                {
                    strUrlRetour = AdminMessageService.getMessageUrl( request, MESSAGE_PAS_ENTREE_CHOISIE,
                            AdminMessage.TYPE_STOP );
                }
            }
            else if ( request.getParameter( CONSTANTE_GENERER_RECTIFICATIF ) != null )
            {
                // demande de creation d'un rectificatif a partir de la liste
                // des entrées selectionnées
                String[] strListEntree = request.getParameterValues( OdsParameters.LISTE_ENTREE_CHECKED );

                if ( ( strListEntree != null ) && ( strListEntree.length != 0 ) )
                {
                    _strTabIdSelect = strListEntree;
                    strUrlRetour = AppPathService.getBaseUrl( request ) + JSP_DO_GENERER_RECTIFICATIF + "?" +
                        OdsParameters.ID_ODJ + "=" + strIdOdj;
                }

                else
                {
                    strUrlRetour = AdminMessageService.getMessageUrl( request, MESSAGE_PAS_ENTREE_CHOISIE,
                            AdminMessage.TYPE_STOP );
                }
            }
        }

        return strUrlRetour;
    }

    /**
     * methode qui bascule l'ordre du jour en mode de classement manuel.
     *
     * @param request
     *            request
     * @return l'url de modification de l'ordre du jour en cours de traitement
     */
    public String activeModeManuel( HttpServletRequest request )
    {
        Plugin plugin = getPlugin(  );

        if ( request.getParameter( OdsParameters.ID_ODJ ) != null )
        {
            int nIdOdj;

            try
            {
                nIdOdj = Integer.parseInt( request.getParameter( OdsParameters.ID_ODJ ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getHomeUrl( request );
            }

            OrdreDuJour ordreDuJour = OrdreDuJourHome.findByPrimaryKey( nIdOdj, plugin );

            if ( ( request.getParameter( CONSTANTE_APPLIQUER ) != null ) &&
                    ordreDuJour.getModeClassement(  ).equals( CONSTANTE_TYPE_AUTOMATIQUE ) )
            {
                // creation d'une sauvegarde de l'odj
                createSauvegardeOdj( ordreDuJour, plugin );
                insertRuptureInTheOdjList( ordreDuJour, plugin, true );
            }
            else if ( ( request.getParameter( CONSTANTE_ANNULER ) != null ) &&
                    ordreDuJour.getModeClassement(  ).equals( CONSTANTE_TYPE_MANUEL ) )
            {
                if ( ordreDuJour.getOrdreDuJourSauveGarde(  ) != null )
                {
                    UrlItem url = new UrlItem( JSP_DO_ANNULER_MODE_MANUEL );
                    url.addParameter( OdsParameters.ID_ODJ, nIdOdj );

                    return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRME_ANNULER_MODE_MANUEL,
                        url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
                }
                else
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_IMPOSSIBLE_DE_REVENIR_MODE_AUTOMATIQUE,
                        AdminMessage.TYPE_STOP );
                }
            }

            return getJspModificationOdj( request, nIdOdj );
        }

        return getHomeUrl( request );
    }

    /**
     * methode qui bascule l'ordre du jour en mode de classement automatique.
     *
     * @param request
     *            request
     * @return l'url de modification de l'ordre du jour en cours de traitement
     */
    public String doAnnulerModeManuel( HttpServletRequest request )
    {
        if ( null != request.getParameter( OdsParameters.ID_ODJ ) )
        {
            OrdreDuJour ordreDuJourManuel;
            OrdreDuJour ordreDuJourSauvegarde;
            Plugin plugin = getPlugin(  );
            int nIdodj;

            try
            {
                nIdodj = Integer.parseInt( request.getParameter( OdsParameters.ID_ODJ ) );
                ordreDuJourManuel = OrdreDuJourHome.findByPrimaryKey( nIdodj, plugin );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getHomeUrl( request );
            }

            // on recupere l'ordre du jour en mode automatique
            ordreDuJourSauvegarde = OrdreDuJourHome.findByPrimaryKey( ordreDuJourManuel.getOrdreDuJourSauveGarde(  )
                                                                                       .getIdOrdreDuJour(  ), plugin );
            ordreDuJourSauvegarde.setEstSauvegarde( false );
            OrdreDuJourHome.update( ordreDuJourSauvegarde, plugin );

            List<EntreeOrdreDuJour> entreeOrdreDuJours = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJourManuel.getIdOrdreDuJour(  ),
                    plugin );

            try
            {
                // suppression de toutes les entrées d'ordre du jour
                for ( EntreeOrdreDuJour entreeOrdreDuJour : entreeOrdreDuJours )
                {
                    // suppression des entrees liées a l'ordre du jour
                    EntreeOrdreDuJourHome.remove( entreeOrdreDuJour, plugin );
                }

                // suppression de l'ordre du jour
                OrdreDuJourHome.remove( ordreDuJourManuel, plugin );
            }
            catch ( AppException ae )
            {
                AppLogService.error( ae );

                if ( ae.getInitialException(  ) instanceof SQLException )
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_ENTREE,
                        AdminMessage.TYPE_STOP );
                }
            }

            return getJspModificationOdj( request, ordreDuJourSauvegarde.getIdOrdreDuJour(  ) );
        }

        return getHomeUrl( request );
    }

    /**
     * methode qui genere un additif ou un rectificatif à partir de la liste des
     * entrées stockées en session
     *
     * @param request
     *            request
     * @param nIdType
     *            l'id du type d'ordre du jour a generer(additif ou
     *            rectificatif)
     * @return l'url de modification de l'ordre du jour en cours de traitement
     */
    public String doGenereAdditifOrRectificatif( HttpServletRequest request, int nIdType )
    {
        OrdreDuJour ordreDuJour;
        OrdreDuJour newOrdreDuJour = new OrdreDuJour(  );
        EntreeOrdreDuJour newEntree;
        TypeOrdreDuJour type = new TypeOrdreDuJour(  );
        Plugin plugin = getPlugin(  );
        int nIdOdj = -1;
        int nIdEntreeSelection;

        try
        {
            nIdOdj = Integer.parseInt( request.getParameter( OdsParameters.ID_ODJ ) );
        }
        catch ( NumberFormatException ne )
        {
            return getHomeUrl( request );
        }

        ordreDuJour = OrdreDuJourHome.findByPrimaryKey( nIdOdj, plugin );

        // préFionnel,mis à jour,d’une commission
        if ( ( ordreDuJour.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) != TypeOrdreDuJourEnum.PREVISIONNEL.getId(  ) ) &&
                ( ordreDuJour.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) != TypeOrdreDuJourEnum.MIS_A_JOUR.getId(  ) ) &&
                ( ordreDuJour.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) != TypeOrdreDuJourEnum.COMMISSION.getId(  ) ) )
        {
            return getHomeUrl( request );
        }

        // la séance, la formation de conseil, la commission, le mode de
        // classement,
        // l’application du tourniquet sont des informations reprise de l’ordre
        // du jour parent
        newOrdreDuJour.setSeance( ordreDuJour.getSeance(  ) );
        newOrdreDuJour.setFormationConseil( ordreDuJour.getFormationConseil(  ) );
        newOrdreDuJour.setCommission( ordreDuJour.getCommission(  ) );
        newOrdreDuJour.setModeClassement( ordreDuJour.getModeClassement(  ) );
        newOrdreDuJour.setTourniquet( ordreDuJour.getTourniquet(  ) );
        newOrdreDuJour.setPublie( false );
        newOrdreDuJour.setEstSauvegarde( false );

        // type de l'ordre du jour additif ou rectificatif
        if ( ( nIdType == TypeOrdreDuJourEnum.ADDITIF.getId(  ) ) ||
                ( nIdType == TypeOrdreDuJourEnum.RECTIFICATIF.getId(  ) ) )
        {
            type.setIdTypeOrdreDuJour( nIdType );
        }

        newOrdreDuJour.setTypeOrdreDuJour( type );

        // récupération du modele d'ordre du jour qui s'applique
        // au nouvel ordre du jour
        // si le modele n'existe pas l'intitule de l'odj est
        // "Nouvel Ordre du jour"
        OrdreDuJourFilter ordreDuJourFilter = new OrdreDuJourFilter(  );
        ordreDuJourFilter.setIdFormationConseil( newOrdreDuJour.getFormationConseil(  ).getIdFormationConseil(  ) );
        ordreDuJourFilter.setIdSeance( newOrdreDuJour.getSeance(  ).getIdSeance(  ) );
        ordreDuJourFilter.setIdType( newOrdreDuJour.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) );
        if(newOrdreDuJour.getCommission()!=null)
        {
        	ordreDuJourFilter.setIdCommission(newOrdreDuJour.getCommission().getIdCommission());
        	
        }
        ModeleOrdreDuJour modeleOrdreDuJour = ModeleOrdreDuJourHome.findByFilter( ordreDuJourFilter, plugin );

        if ( null == modeleOrdreDuJour )
        {
            newOrdreDuJour.setIntitule( I18nService.getLocalizedString( CONSTANTE_NOUVEL_ODJ, getLocale(  ) ) );
        }
        else
        {
            newOrdreDuJour.setIntitule( modeleOrdreDuJour.getTitre(  ) );
        }

        newOrdreDuJour.setIdOrdreDuJour( OrdreDuJourHome.create( newOrdreDuJour, plugin ) );

        // on insere dans l'ordre du jour 'rectificatif' ou 'additif ' les
        // entrées sélectionnées
        for ( String strIdEntree : _strTabIdSelect )
        {
            try
            {
                nIdEntreeSelection = Integer.parseInt( strIdEntree );
            }
            catch ( NumberFormatException ne )
            {
                return getHomeUrl( request );
            }

            newEntree = EntreeOrdreDuJourHome.findByPrimaryKey( nIdEntreeSelection, plugin );
            newEntree.setOrdreDuJour( newOrdreDuJour );
            newEntree.setIdEntreeOrdreDuJour( EntreeOrdreDuJourHome.create( newEntree, plugin ) );

            if ( newEntree.getCommissions(  ) != null )
            {
                // insertion des commissions de l'entrée
                for ( Commission commissionEntree : newEntree.getCommissions(  ) )
                {
                    EntreeOrdreDuJourHome.insertCommission( newEntree.getIdEntreeOrdreDuJour(  ),
                        commissionEntree.getIdCommission(  ), plugin );
                }
            }

            if ( newEntree.getElus(  ) != null )
            {
                // insertion des rapporteurs
                for ( Elu elu : newEntree.getElus(  ) )
                {
                    EntreeOrdreDuJourHome.insertRapporteur( newEntree.getIdEntreeOrdreDuJour(  ), elu.getIdElu(  ),
                        plugin );
                }
            }
        }

        return getHomeUrl( request );
    }

    /**
     * crée une sauvegarde de l'ordre du jour et l'insere dans l'ordre du jour
     * passé en parametre
     *
     * @param ordreDuJour
     *            l'ordre du jour a sauvegarder
     * @param plugin
     *            plugin
     */
    private void createSauvegardeOdj( OrdreDuJour ordreDuJour, Plugin plugin )
    {
        // Une sauvegarde de l’ordre du jour est créée
        OrdreDuJour ordreDuJourSauveGarde = new OrdreDuJour(  );
        // parametre estsauvegarde = true juste pour la creation de la
        // sauvegarde
        ordreDuJour.setEstSauvegarde( true );
        ordreDuJourSauveGarde.setIdOrdreDuJour( OrdreDuJourHome.create( ordreDuJour, plugin ) );
        // on update l'ordre du jour
        ordreDuJour.setEstSauvegarde( false );
        ordreDuJour.setModeClassement( CONSTANTE_TYPE_MANUEL );
        ordreDuJour.setOrdreDuJourSauveGarde( ordreDuJourSauveGarde );
        OrdreDuJourHome.update( ordreDuJour, plugin );

        List<EntreeOrdreDuJour> listEntreOrdreDuJour = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJour.getIdOrdreDuJour(  ),
                plugin );
        int nIdNewEntree;

        for ( EntreeOrdreDuJour entree : listEntreOrdreDuJour )
        {
            entree.setOrdreDuJour( ordreDuJourSauveGarde );
            nIdNewEntree = EntreeOrdreDuJourHome.create( entree, plugin );

            if ( entree.getCommissions(  ) != null )
            {
                // insertion des commissions de l'entrée
                for ( Commission commissionEntree : entree.getCommissions(  ) )
                {
                    EntreeOrdreDuJourHome.insertCommission( nIdNewEntree, commissionEntree.getIdCommission(  ), plugin );
                }
            }

            if ( entree.getElus(  ) != null )
            {
                // insertion des rapporteurs
                for ( Elu elu : entree.getElus(  ) )
                {
                    EntreeOrdreDuJourHome.insertRapporteur( nIdNewEntree, elu.getIdElu(  ), plugin );
                }
            }
        }
    }

    /**
     * insérées des nouvelles entrées de type texte libre qui correspondent aux
     * ruptures affichées en mode automatique :natures,groupes de commissions,
     * groupes de rapporteurs
     *
     * @param ordreDuJour
     *            l'ordre du jour en cours de taitement
     * @param nBase
     *            true si les ruptures doivent etre insérées en base false sinon
     * @param plugin
     *            plugin
     */
    private void insertRuptureInTheOdjList( OrdreDuJour ordreDuJour, Plugin plugin, boolean nBase )
    {
        EntreeOrdreDuJour newEntree;
        List<EntreeOrdreDuJour> listEntreOrdreDuJour = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJour.getIdOrdreDuJour(  ),
                plugin );
        List<EntreeOrdreDuJour> listEntreOrdreDuJourWithRupture = new ArrayList<EntreeOrdreDuJour>(  );
        listEntreOrdreDuJourWithRupture.addAll( listEntreOrdreDuJour );

        int ncpt = 0;
        List<Elu> rapporteursSauvegarde = null;
        List<Commission> commissionsSauvegarde = null;
        NatureDesDossiers natureDesDossiersSauvegarde = null;

        for ( EntreeOrdreDuJour entree : listEntreOrdreDuJour )
        {
            if ( ( ( natureDesDossiersSauvegarde == null ) && ( entree.getNature(  ) != null ) ) ||
                    ( ( natureDesDossiersSauvegarde != null ) && ( entree.getNature(  ) != null ) &&
                    ( natureDesDossiersSauvegarde.getIdNature(  ) != entree.getNature(  ).getIdNature(  ) ) ) )
            {
                // si nature de dossier sauvegarde == nul ou l'entree possede
                // une nature de dossier différente de la nature sauvegardée
                // on affiche la naure de dossier
                // on test si on doit afficher des rapporteurs
                if ( ( rapporteursSauvegarde != null ) && ( rapporteursSauvegarde.size(  ) != 0 ) )
                {
                    // creation de la nouvelle entrée à inserer
                    newEntree = createEntreerapporteurs( rapporteursSauvegarde, ordreDuJour );

                    if ( nBase )
                    {
                        newEntree.setIdEntreeOrdreDuJour( EntreeOrdreDuJourHome.create( newEntree, plugin ) );
                    }

                    listEntreOrdreDuJourWithRupture.add( ncpt, newEntree );
                    ncpt++;
                    rapporteursSauvegarde = null;
                }

                newEntree = createEntreeNatureDossiers( entree.getNature(  ), ordreDuJour );

                if ( nBase )
                {
                    newEntree.setIdEntreeOrdreDuJour( EntreeOrdreDuJourHome.create( newEntree, plugin ) );
                }

                listEntreOrdreDuJourWithRupture.add( ncpt, newEntree );
                ncpt++;
                // on initialise la commission de sauvegarde a null pour
                // afficher la commission de l'entree
                // si elle en possede
                commissionsSauvegarde = null;
                natureDesDossiersSauvegarde = entree.getNature(  );
            }

            if ( ( ( commissionsSauvegarde == null ) && ( entree.getCommissions(  ) != null ) &&
                    ( entree.getCommissions(  ).size(  ) != 0 ) ) ||
                    ( ( commissionsSauvegarde != null ) && ( commissionsSauvegarde.size(  ) != 0 ) &&
                    !entree.getEqualListCommissions( commissionsSauvegarde ) ) )
            {
                // si la liste des commissions == nul ou l'entree possede une
                // liste de commission différente
                // de la liste des commissions sauvegardé
                // on affiche la naure de dossier
                // on test si on doit afficher des rapporteurs
                if ( ( rapporteursSauvegarde != null ) && ( rapporteursSauvegarde.size(  ) != 0 ) )
                {
                    // creation de la nouvelle entrée à inserer
                    newEntree = createEntreerapporteurs( rapporteursSauvegarde, ordreDuJour );

                    if ( nBase )
                    {
                        newEntree.setIdEntreeOrdreDuJour( EntreeOrdreDuJourHome.create( newEntree, plugin ) );
                    }

                    listEntreOrdreDuJourWithRupture.add( ncpt, newEntree );
                    ncpt++;
                    rapporteursSauvegarde = null;
                }

                newEntree = createEntreeCommissions( entree.getCommissions(  ), ordreDuJour );

                if ( nBase )
                {
                    newEntree.setIdEntreeOrdreDuJour( EntreeOrdreDuJourHome.create( newEntree, plugin ) );
                }

                listEntreOrdreDuJourWithRupture.add( ncpt, newEntree );
                ncpt++;
                // on sauvegarde la commission de l'entrée
                commissionsSauvegarde = entree.getCommissions(  );
            }

            if ( ( rapporteursSauvegarde == null ) && ( entree.getElus(  ) != null ) &&
                    ( entree.getElus(  ).size(  ) != 0 ) )
            {
                // on sauvegarde la liste des rapporteurs de l'entrée
                rapporteursSauvegarde = entree.getElus(  );
            }

            if ( ( rapporteursSauvegarde != null ) && ( rapporteursSauvegarde.size(  ) != 0 ) &&
                    ( entree.getElus(  ) != null ) && ( entree.getElus(  ).size(  ) != 0 ) &&
                    !entree.getEqualListRapporteurs( rapporteursSauvegarde ) )
            {
                // si la liste des rapporteurs de l'entrée est différente de
                // liste des rapporteurs sauvegardé
                // on affiche la liste des rapporteurs sauvegardé
                newEntree = createEntreerapporteurs( rapporteursSauvegarde, ordreDuJour );

                if ( nBase )
                {
                    newEntree.setIdEntreeOrdreDuJour( EntreeOrdreDuJourHome.create( newEntree, plugin ) );
                }

                listEntreOrdreDuJourWithRupture.add( ncpt, newEntree );
                ncpt++;
                rapporteursSauvegarde = entree.getElus(  );
            }

            ncpt++;
        }

        if ( rapporteursSauvegarde != null )
        {
            // si les rapporteurs de la derniere entrée n'ont pas été affiché on
            // les affiches
            newEntree = createEntreerapporteurs( rapporteursSauvegarde, ordreDuJour );

            if ( nBase )
            {
                newEntree.setIdEntreeOrdreDuJour( EntreeOrdreDuJourHome.create( newEntree, plugin ) );
            }

            listEntreOrdreDuJourWithRupture.add( ncpt, newEntree );
        }

        if ( nBase )
        {
            // on reclasse en base la nouvelle liste des entrées de l'ordre du
            // jour
            int nNumeroOrdre = 1;

            for ( EntreeOrdreDuJour entree : listEntreOrdreDuJourWithRupture )
            {
                entree.setNumeroOrdre( nNumeroOrdre );
                EntreeOrdreDuJourHome.update( entree, plugin );
                nNumeroOrdre++;
            }
        }

        ordreDuJour.setListEntrees( listEntreOrdreDuJourWithRupture );
    }

    /**
     * crée une entrée de type texte à partir de la nature de dossiers
     *
     * @param nature
     *            la nature à transformer en entrée d'ordre du jour
     * @param ordreDuJour
     *            l'ordre du jour en cours de traitement
     * @return l'entrée d'ordre du jour créée
     */
    private EntreeOrdreDuJour createEntreeNatureDossiers( NatureDesDossiers nature, OrdreDuJour ordreDuJour )
    {
        EntreeOrdreDuJour newEntreeOdj = new EntreeOrdreDuJour(  );
        newEntreeOdj.setOrdreDuJour( ordreDuJour );
        newEntreeOdj.setType( CONSTANTE_ENTREE_TYPE_TEXTE );

        if ( AppPropertiesService.getProperty( PROPERTY_ODJ_NATURES_STYLE ) != null )
        {
            newEntreeOdj.setStyle( AppPropertiesService.getProperty( PROPERTY_ODJ_NATURES_STYLE ) );
        }
        else
        {
            newEntreeOdj.setStyle( CONSTANTE_STYLE_H2 );
        }

        newEntreeOdj.setObjet( nature.getLibelleNature(  ) );

        return newEntreeOdj;
    }

    /**
     * crée une entrée de type texte a partir de la liste des commissions
     * passées en parametre
     *
     * @param commissions
     *            la liste des commissions à transformer en entrée d'ordre du
     *            jour
     * @param ordreDuJour
     *            l'ordre du jour en cours de traitement
     * @return l'entrée d'ordre du jour créée
     */
    private EntreeOrdreDuJour createEntreeCommissions( List<Commission> commissions, OrdreDuJour ordreDuJour )
    {
        EntreeOrdreDuJour newEntreeOdj = new EntreeOrdreDuJour(  );
        newEntreeOdj.setOrdreDuJour( ordreDuJour );
        newEntreeOdj.setType( CONSTANTE_ENTREE_TYPE_TEXTE );

        if ( AppPropertiesService.getProperty( PROPERTY_ODJ_GROUPE_DE_COMMISSIONS_STYLE ) != null )
        {
            newEntreeOdj.setStyle( AppPropertiesService.getProperty( PROPERTY_ODJ_GROUPE_DE_COMMISSIONS_STYLE ) );
        }
        else
        {
            newEntreeOdj.setStyle( CONSTANTE_STYLE_H3 );
        }

        newEntreeOdj.setObjet( OrdreDuJourUtils.getLibelleGroupeCommissions( commissions, getLocale(  ) ) );

        return newEntreeOdj;
    }

    /**
     * crée une entrée de type texte a partir de la liste des rapporteurs
     * passées en parametre
     *
     * @param rapporteurs la liste des rapporteurs à transformer en entrée d'ordre du jour
     * @param ordreDuJour l'ordre du jour en cours de traitement
     * @return l'entrée d'ordre du jour créée
     */
    private EntreeOrdreDuJour createEntreerapporteurs( List<Elu> rapporteurs, OrdreDuJour ordreDuJour )
    {
        EntreeOrdreDuJour newEntreeOdj = new EntreeOrdreDuJour(  );
        newEntreeOdj.setOrdreDuJour( ordreDuJour );
        newEntreeOdj.setType( CONSTANTE_ENTREE_TYPE_TEXTE );

        if ( AppPropertiesService.getProperty( PROPERTY_ODJ_GROUPE_DE_RAPPORTEURS_STYLE ) != null )
        {
            newEntreeOdj.setStyle( AppPropertiesService.getProperty( PROPERTY_ODJ_GROUPE_DE_RAPPORTEURS_STYLE ) );
        }
        else
        {
            newEntreeOdj.setStyle( CONSTANTE_STYLE_H4 );
        }

        newEntreeOdj.setObjet( OrdreDuJourUtils.getLibelleGroupeRapporteurs( rapporteurs, getLocale(  ) ) );

        return newEntreeOdj;
    }

    /**
     * Retourne l'interface de selection d'une entrée d'ordre du jour.
     *
     * @param request la requête Http
     * @return l'interface de selection d'une entrée d'ordre du jour.
     */
    public String getSelectionEntreeOrdreDuJour( HttpServletRequest request )
    {
        Plugin plugin = getPlugin(  );
        HashMap<String, Object> model = new HashMap<String, Object>(  );

        if ( request.getParameter( OdsParameters.ID_ODJ ) != null )
        {
            int nIdOdj;

            try
            {
                nIdOdj = Integer.parseInt( request.getParameter( OdsParameters.ID_ODJ ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getOrdreDuJourList( request );
            }

            OrdreDuJour ordreDuJour = OrdreDuJourHome.findByPrimaryKey( nIdOdj, plugin );

            if ( ordreDuJour == null )
            {
                return getOrdreDuJourList( request );
            }

            // récupération de la liste des entrées associées à l'odj
            List<EntreeOrdreDuJour> listEntreeOrdreDuJour = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJour.getIdOrdreDuJour(  ),
                    plugin );
            List<EntreeOrdreDuJour> listEntreeOrdreDuJourSelection = new ArrayList<EntreeOrdreDuJour>(  );

            // Les vœux et amendements rattachés à un PDD ne sont affichés que
            // si l’ordre du jour est définitif.
            // on recupere les voeux et amendements associés au pdd
            for ( EntreeOrdreDuJour entreeOrdreDuJour : listEntreeOrdreDuJour )
            {
                boolean isSelect = false;

                if ( _strTabIdSelect != null )
                {
                    int nIdEntreeDeplace;

                    for ( String strIdSelect : _strTabIdSelect )
                    {
                        try
                        {
                            nIdEntreeDeplace = Integer.parseInt( strIdSelect );

                            if ( nIdEntreeDeplace == entreeOrdreDuJour.getIdEntreeOrdreDuJour(  ) )
                            {
                                isSelect = true;
                            }
                        }
                        catch ( NumberFormatException ne )
                        {
                            return getHomeUrl( request );
                        }
                    }
                }

                if ( !isSelect )
                {
                    if ( ( ordreDuJour.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) == TypeOrdreDuJourEnum.DEFINITIF.getId(  ) ) &&
                            ( entreeOrdreDuJour.getPdd(  ) != null ) )
                    {
                        entreeOrdreDuJour.setPdd( PDDHome.findByPrimaryKey( entreeOrdreDuJour.getPdd(  ).getIdPdd(  ),
                                plugin ) );
                    }

                    listEntreeOrdreDuJourSelection.add( entreeOrdreDuJour );
                }
            }

            ordreDuJour.setListEntrees( listEntreeOrdreDuJourSelection );
            model.put( OrdreDuJour.MARK_ODJ, ordreDuJour );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SELECTION_ENTREE_ORDRE_DU_JOUR,
                    getLocale(  ), model );

            return getAdminPage( template.getHtml(  ) );
        }

        return getOrdreDuJourList( request );
    }

    /**
     * Numerote les voeux et amendements de l'ordre du jour definitif
     *
     * @param request
     *            request
     * @return l'url de modification de l'ordre du jour en cours de traitement
     */
    public String doNumeroteVa( HttpServletRequest request )
    {
        if ( request.getParameter( OdsParameters.ID_ODJ ) != null )
        {
            Plugin plugin = getPlugin(  );
            int nIdOdj;
            int nIndexList;
            int nNumero;

            try
            {
                nIdOdj = Integer.parseInt( request.getParameter( OdsParameters.ID_ODJ ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return getHomeUrl( request );
            }

            // recuperation de l'ordre du jour
            OrdreDuJour ordreDuJour = OrdreDuJourHome.findByPrimaryKey( nIdOdj, plugin );

            // l'ordre du jour doit etre de type definitif pour avoir le droit
            // de numeroter les vas
            if ( ordreDuJour.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) == TypeOrdreDuJourEnum.DEFINITIF.getId(  ) )
            {
                List<List<VoeuAmendement>> listFasciculeva = new ArrayList<List<VoeuAmendement>>(  );
                List<Fascicule> listFascicule = FasciculeHome.findFasciculeByIdSeance( ordreDuJour.getSeance(  )
                                                                                                  .getIdSeance(  ),
                        plugin );

                // on créé pour chaque fascicule une liste destinée à stocker
                // les vas rataché au fascicule
                // ainsi qu'une liste stockant l'id du fascicule
                for ( int i = 0; i < listFascicule.size(  ); i++ )
                {
                    listFasciculeva.add( new ArrayList<VoeuAmendement>(  ) );
                }

                // récupération de la liste des entrées associées à l'odj
                List<EntreeOrdreDuJour> listEntreeOrdreDuJour = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJour.getIdOrdreDuJour(  ),
                        plugin );

                for ( EntreeOrdreDuJour entreeOrdreDuJour : listEntreeOrdreDuJour )
                {
                    if ( entreeOrdreDuJour.getVoeuAmendement(  ) != null )
                    {
                        nIndexList = getIndexListFascicule( entreeOrdreDuJour.getVoeuAmendement(  ).getFascicule(  )
                                                                             .getIdFascicule(  ), listFascicule );

                        // si le va n'existe pas dans la liste des vas du
                        // fascicule on l'ajoute
                        if ( ( nIndexList != -1 ) &&
                                !vaAlreadyExistInFascicule( 
                                    entreeOrdreDuJour.getVoeuAmendement(  ).getIdVoeuAmendement(  ),
                                    listFasciculeva.get( nIndexList ) ) )
                        {
                            listFasciculeva.get( nIndexList ).add( entreeOrdreDuJour.getVoeuAmendement(  ) );
                        }
                    }

                    if ( entreeOrdreDuJour.getPdd(  ) != null )
                    {
                        entreeOrdreDuJour.setPdd( PDDHome.findByPrimaryKey( entreeOrdreDuJour.getPdd(  ).getIdPdd(  ),
                                plugin ) );

                        for ( VoeuAmendement voeuAmendement : entreeOrdreDuJour.getPdd(  ).getVoeuxAmendements(  ) )
                        {
                            nIndexList = getIndexListFascicule( voeuAmendement.getFascicule(  ).getIdFascicule(  ),
                                    listFascicule );

                            // si le va n'existe pas dans la liste des vas du
                            // fascicule on l'ajoute
                            if ( ( nIndexList != -1 ) &&
                                    !vaAlreadyExistInFascicule( voeuAmendement.getIdVoeuAmendement(  ),
                                        listFasciculeva.get( nIndexList ) ) )
                            {
                                listFasciculeva.get( nIndexList ).add( voeuAmendement );
                            }
                        }
                    }
                }

                // pour chaque fascicule on numerote les voeux
                for ( List<VoeuAmendement> listFascicules : listFasciculeva )
                {
                    nNumero = 1;

                    for ( VoeuAmendement voeuAmendement : listFascicules )
                    {
                        voeuAmendement = VoeuAmendementHome.findByPrimaryKey( voeuAmendement.getIdVoeuAmendement(  ),
                                plugin );
                        voeuAmendement.setReference( OdsConstants.CONSTANTE_CHAINE_VIDE + nNumero );
                        VoeuAmendementHome.update( voeuAmendement, plugin );
                        nNumero++;
                    }
                }
            }

            return getJspModificationOdj( request, nIdOdj );
        }

        return getHomeUrl( request );
    }

    /**
     * Deplace la liste des entrées selectionnées à la position choisie par
     * l'utilisateur
     *
     * @param request
     *            request
     * @return l'url de modification de l'ordre du jour en cours de traitement
     */
    public String doDeplaceListeEntree( HttpServletRequest request )
    {
        if ( ( _strTabIdSelect != null ) && ( _strTabIdSelect.length != 0 ) )
        {
            Plugin plugin = getPlugin(  );
            List<EntreeOrdreDuJour> listEntreeOrdreDuJour;
            List<EntreeOrdreDuJour> listEntreeADeplacer;
            int nIdEntreeSelect = -1;
            int nIdEntreeDeplace = -1;
            int nIdOdj = -1;

            if ( ( request.getParameter( CONSTANTE_ANNULER ) != null ) ||
                    ( ( request.getParameter( OdsParameters.ID_ODJ ) != null ) &&
                    ( request.getParameter( OdsParameters.ID_ENTREE ) != null ) &&
                    ( request.getParameter( OdsParameters.TYPE ) != null ) ) )
            {
                try
                {
                    nIdOdj = Integer.parseInt( request.getParameter( OdsParameters.ID_ODJ ) );
                }
                catch ( NumberFormatException ne )
                {
                    AppLogService.error( ne );

                    return getHomeUrl( request );
                }

                if ( request.getParameter( CONSTANTE_ANNULER ) == null )
                {
                    try
                    {
                        nIdEntreeSelect = Integer.parseInt( request.getParameter( OdsParameters.ID_ENTREE ) );
                    }
                    catch ( NumberFormatException nfe )
                    {
                        AppLogService.error( nfe );
                    }

                    listEntreeOrdreDuJour = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( nIdOdj, plugin );
                    listEntreeADeplacer = new ArrayList<EntreeOrdreDuJour>(  );

                    for ( String strIdEntree : _strTabIdSelect )
                    {
                        try
                        {
                            nIdEntreeDeplace = Integer.parseInt( strIdEntree );
                        }
                        catch ( NumberFormatException ne )
                        {
                            return getHomeUrl( request );
                        }

                        for ( EntreeOrdreDuJour entree : listEntreeOrdreDuJour )
                        {
                            if ( entree.getIdEntreeOrdreDuJour(  ) == nIdEntreeDeplace )
                            {
                                listEntreeADeplacer.add( entree );
                            }
                        }
                    }

                    // on supprime les entrées avant de les inserer a la
                    // position selectionné
                    listEntreeOrdreDuJour.removeAll( listEntreeADeplacer );

                    // on determine l'index de l'entrée selectionné
                    int nIndex = 0;

                    for ( EntreeOrdreDuJour entree : listEntreeOrdreDuJour )
                    {
                        if ( entree.getIdEntreeOrdreDuJour(  ) == nIdEntreeSelect )
                        {
                            break;
                        }

                        nIndex++;
                    }

                    if ( request.getParameter( OdsParameters.TYPE ).equals( CONSTANTE_AJOUTER_APRES_SELECTION ) )
                    {
                        nIndex++;
                    }

                    for ( EntreeOrdreDuJour entree : listEntreeADeplacer )
                    {
                        listEntreeOrdreDuJour.add( nIndex, entree );
                        nIndex++;
                    }

                    // mise a joure en base des numero d'ordre des entrées
                    int nNumeroOrdre = 1;

                    for ( EntreeOrdreDuJour entree : listEntreeOrdreDuJour )
                    {
                        entree.setNumeroOrdre( nNumeroOrdre );
                        EntreeOrdreDuJourHome.update( entree, plugin );
                        nNumeroOrdre++;
                    }
                }

                return getJspModificationOdj( request, nIdOdj );
            }
            else
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_NO_ENTREE_SELECTED, AdminMessage.TYPE_STOP );
            }
        }

        return getHomeUrl( request );
    }

    /**
     * test si le voeu amendement d'id nIdVa existe dans la liste des voeux
     * amendements passée en parametre
     *
     * @param nIdVa
     *            l'id du va
     * @param listVas
     *            la liste des vas
     * @return true si le va d'id nIdVa existe dans la liste des vas
     */
    private boolean vaAlreadyExistInFascicule( int nIdVa, List<VoeuAmendement> listVas )
    {
        for ( VoeuAmendement va : listVas )
        {
            if ( va.getIdVoeuAmendement(  ) == nIdVa )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * retourne l'index du fascicule d'id nIdFascicule dans la liste des
     * fascicules listFascicule
     *
     * @param nIdFascicule
     *            l'id du fascicule
     * @param listFascicule
     *            la liste des fascicules
     * @return retourne l'index du fascicule d'id nIdFascicule dans la liste des
     *         fascicules listFascicule
     */
    private int getIndexListFascicule( int nIdFascicule, List<Fascicule> listFascicule )
    {
        int ncpt = 0;

        for ( Fascicule fascicule : listFascicule )
        {
            if ( fascicule.getIdFascicule(  ) == nIdFascicule )
            {
                return ncpt;
            }

            ncpt++;
        }

        return -1;
    }

    /**
     * Permet de stocker toutes les permissions afin de gérer les profils au niveau des templates
     * @param model le hashmap contenant les parametres qui vont être envoyés au template
     */
    private void ajouterPermissionsDansHashmap( Map<Object, Object> model )
    {
        /*
         * Permission de supprimer
         */
        boolean bPermissionSuppression = true;

        if ( !RBACService.isAuthorized( ConstitutionOdjResourceIdService.CONSTITUTION_ODJ, RBAC.WILDCARD_RESOURCES_ID,
                    ConstitutionOdjResourceIdService.PERMISSION_SUPPRESSION, getUser(  ) ) )
        {
            bPermissionSuppression = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_SUPPRESSION, bPermissionSuppression );
    }
}
