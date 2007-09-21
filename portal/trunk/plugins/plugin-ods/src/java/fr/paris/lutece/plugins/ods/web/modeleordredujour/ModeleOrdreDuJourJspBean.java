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
package fr.paris.lutece.plugins.ods.web.modeleordredujour;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.commission.CommissionHome;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysique;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysiqueHome;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilHome;
import fr.paris.lutece.plugins.ods.business.modeleordredujour.ModeleOrdreDuJour;
import fr.paris.lutece.plugins.ods.business.modeleordredujour.ModeleOrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.TypeOrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.TypeOrdreDuJourEnum;
import fr.paris.lutece.plugins.ods.business.ordredujour.TypeOrdreDuJourHome;
import fr.paris.lutece.plugins.ods.service.role.ModelesOdjResourceIdService;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.constants.OdsProperties;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.fileupload.FileItem;

import java.io.IOException;
import java.io.OutputStream;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * ModeleOrdreDuJourJspBean
 */
public class ModeleOrdreDuJourJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_MODELES_ODJ = "ODS_MODELES_ODJ";
    private static final String TEMPLATE_LISTE_MODELESODJ = "admin/plugins/ods/modeleordredujour/liste_modeles.html";
    private static final String TEMPLATE_CREATION_MODELEODJ = "admin/plugins/ods/modeleordredujour/creation_modele.html";
    private static final String TEMPLATE_MODIFICATION_MODELEODJ = "admin/plugins/ods/modeleordredujour/modification_modele.html";
    private static final String PROPERTY_LABEL_TITRE = "ods.modele.field.titre";
    private static final String PROPERTY_LABEL_TYPE = "ods.modele.field.type";
    private static final String PROPERTY_LABEL_FORMATION = "ods.modele.field.formation";
    private static final String MARK_MODELE = "modele";
    private static final String MARK_LIST_MODELE = "liste_modele";
    private static final String MARK_LISTE_FORMATION_CONSEIL = "liste_formation_conseil";
    private static final String MARK_LISTE_COMMISSION = "liste_commission";
    private static final String MARK_LISTE_TYPE = "liste_type";
    private static final String MESSAGE_COMMISSION_NE_DOIT_PAS_ETRE_SAISIE = "ods.modele.message.commissionNeDoitPasEtreSaisie";
    private static final String MESSAGE_COMMISSION_OBLIGATOIRE = "ods.modele.message.commissionObligatoire";
    private static final String MESSAGE_MODELE_ALREADY_EXIST = "ods.modele.message.existeDeja";
    private static final String MESSAGE_MODELE_ENTETE_NO_XML = "ods.modele.message.enteteNoXml";
    private static final String MESSAGE_MODELE_PIED_NO_XML = "ods.modele.message.piedNoXml";
    private static final String MESSAGE_CONFIRME_DELETE_MODELE = "ods.modele.message.confirmeSuppression";
    private static final String MESSAGE_CANNOT_DELETE_MODELE = "ods.modele.message.suppressionImpossible";
    private static final String JSP_URL_DO_SUPPRESSION_MODELE_JSP = "jsp/admin/plugins/ods/modeleordredujour/DoSuppressionModele.jsp";
    private static final String CONSTANTE_XML = "XML";

    /**
     * Récupère la page de gestion des modèles d'ordre du jour
     *@param request la requete
     * @return la page de gestion des modeles
     */
    public String getModelesOdjList( HttpServletRequest request )
    {
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        List<ModeleOrdreDuJour> modeleOrdreDuJours = ModeleOrdreDuJourHome.findModeleOrdreDuJourList( getPlugin(  ) );

        model.put( MARK_LIST_MODELE, modeleOrdreDuJours );

        ajouterPermissionsDansHashmap( model );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_MODELESODJ, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * retourne l'interface de creation  d'un modèle d'ordre du jour
     *@param request la requete
     *@return la page de creation d'un modele d'ordre du jour
     */
    public String getCreationModeleOdj( HttpServletRequest request )
    {
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        HtmlTemplate template;

        if ( RBACService.isAuthorized( ModelesOdjResourceIdService.MODELES_ODJ, RBAC.WILDCARD_RESOURCES_ID,
                    ModelesOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            ReferenceList referenceListCommission = initRefListCommission( getPlugin(  ) );
            ReferenceList referenceListFormation = initRefListFormationConseil( getPlugin(  ) );
            ReferenceList referenceListType = initRefListType( getPlugin(  ) );
            model.put( MARK_LISTE_COMMISSION, referenceListCommission );
            model.put( MARK_LISTE_FORMATION_CONSEIL, referenceListFormation );
            model.put( MARK_LISTE_TYPE, referenceListType );

            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_MODELEODJ, getLocale(  ), model );
        }
        else
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * retourne l'interface de modification du modèle d'ordre du jour selectionné
     *@param request la requete
     *@return l'interface de modification du modèle d'ordre du jour selectionné
     */
    public String getModificationModeleOdj( HttpServletRequest request )
    {
        int nIdModele;
        Plugin plugin = getPlugin(  );
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        HtmlTemplate template;

        if ( RBACService.isAuthorized( ModelesOdjResourceIdService.MODELES_ODJ, RBAC.WILDCARD_RESOURCES_ID,
                    ModelesOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            String strIdModele = request.getParameter( OdsParameters.ID_MODELE );

            if ( null != strIdModele )
            {
                try
                {
                    nIdModele = Integer.parseInt( strIdModele );
                }
                catch ( NumberFormatException ne )
                {
                    AppLogService.error( ne );

                    return getHomeUrl( request );
                }

                ModeleOrdreDuJour modeleOrdreDuJour = ModeleOrdreDuJourHome.findByPrimaryKey( nIdModele, plugin );
                ReferenceList referenceListCommission = initRefListCommission( plugin );
                ReferenceList referenceListFormation = initRefListFormationConseil( plugin );
                ReferenceList referenceListType = initRefListType( plugin );

                model.put( MARK_MODELE, modeleOrdreDuJour );
                model.put( MARK_LISTE_COMMISSION, referenceListCommission );
                model.put( MARK_LISTE_FORMATION_CONSEIL, referenceListFormation );
                model.put( MARK_LISTE_TYPE, referenceListType );

                template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_MODELEODJ, getLocale(  ), model );
            }
            else
            {
                return getModelesOdjList( request );
            }
        }
        else
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * crée un modele d'ordre du jour apartir des parametres contenus dans la requete
     *@param request la requete
     *@return retour sur l'interface de gestion des modeles d'ordre du jour
     */
    public String doCreationModeleOdj( HttpServletRequest request )
    {
        String strReturn = null;

        if ( RBACService.isAuthorized( ModelesOdjResourceIdService.MODELES_ODJ, RBAC.WILDCARD_RESOURCES_ID,
                    ModelesOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            ModeleOrdreDuJour modeleOrdreDuJour = new ModeleOrdreDuJour(  );
            modeleOrdreDuJour.setIdModele( -1 );

            String strMessage = testField( request, modeleOrdreDuJour );

            if ( !strMessage.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                if ( strMessage.equals( MESSAGE_COMMISSION_NE_DOIT_PAS_ETRE_SAISIE ) ||
                        strMessage.equals( MESSAGE_COMMISSION_OBLIGATOIRE ) ||
                        strMessage.equals( MESSAGE_MODELE_ALREADY_EXIST ) )
                {
                    strReturn = AdminMessageService.getMessageUrl( request, strMessage, AdminMessage.TYPE_STOP );
                }
                else
                {
                    Object[] tabRequiredFields = { I18nService.getLocalizedString( strMessage, getLocale(  ) ) };

                    strReturn = AdminMessageService.getMessageUrl( request,
                            OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED, tabRequiredFields, AdminMessage.TYPE_STOP );
                }
            }
            else
            {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                FileItem fileItemEnteteDocument = multipartRequest.getFile( OdsParameters.ENTETE_DOCUMENT );
                FileItem fileItemPiedDocument = multipartRequest.getFile( OdsParameters.PIED_DOCUMENT );

                if ( ( null != fileItemEnteteDocument ) && ( null != ( fileItemEnteteDocument.getName(  ) ) ) &&
                        !( OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileItemEnteteDocument.getName(  ) ) ) )
                {
                    if ( !isValideFile( fileItemEnteteDocument ) )
                    {
                        strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_MODELE_ENTETE_NO_XML,
                                AdminMessage.TYPE_STOP );
                    }
                    else
                    {
                        modeleOrdreDuJour.setEnteteDocument( doCreationFichierPhysique( fileItemEnteteDocument ) );
                    }
                }

                if ( ( strReturn == null ) && ( null != fileItemPiedDocument ) &&
                        ( null != fileItemPiedDocument.getName(  ) ) &&
                        !( OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileItemPiedDocument.getName(  ) ) ) )
                {
                    if ( !isValideFile( fileItemPiedDocument ) )
                    {
                        strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_MODELE_PIED_NO_XML,
                                AdminMessage.TYPE_STOP );
                    }
                    else
                    {
                        modeleOrdreDuJour.setPiedDocument( doCreationFichierPhysique( fileItemPiedDocument ) );
                    }
                }

                if ( strReturn == null )
                {
                    ModeleOrdreDuJourHome.create( modeleOrdreDuJour, getPlugin(  ) );

                    strReturn = getHomeUrl( request );
                }
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
     * modifie  un modele d'ordre du jour a partir des parametres contenus dans la requete
     *@param request la requete
     *@return retour sur l'interface de gestion des modeles d'ordre du jour
     */
    public String doModificationModeleOdj( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( ModelesOdjResourceIdService.MODELES_ODJ, RBAC.WILDCARD_RESOURCES_ID,
                    ModelesOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( request.getParameter( OdsParameters.ID_MODELE ) != null )
            {
                Plugin plugin = getPlugin(  );
                ModeleOrdreDuJour modeleOrdreDuJour;

                int nIdModele;

                try
                {
                    nIdModele = Integer.parseInt( request.getParameter( OdsParameters.ID_MODELE ) );
                    modeleOrdreDuJour = ModeleOrdreDuJourHome.findByPrimaryKey( nIdModele, plugin );
                }
                catch ( NumberFormatException ne )
                {
                    AppLogService.error( ne );

                    return getHomeUrl( request );
                }

                String strMessage = testField( request, modeleOrdreDuJour );

                if ( !strMessage.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    String strReturn = null;

                    if ( strMessage.equals( MESSAGE_COMMISSION_NE_DOIT_PAS_ETRE_SAISIE ) ||
                            strMessage.equals( MESSAGE_COMMISSION_OBLIGATOIRE ) ||
                            strMessage.equals( MESSAGE_MODELE_ALREADY_EXIST ) )
                    {
                        strReturn = AdminMessageService.getMessageUrl( request, strMessage, AdminMessage.TYPE_STOP );
                    }
                    else
                    {
                        Object[] tabRequiredFields = { I18nService.getLocalizedString( strMessage, getLocale(  ) ) };

                        strReturn = AdminMessageService.getMessageUrl( request,
                                OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED, tabRequiredFields, AdminMessage.TYPE_STOP );
                    }

                    return strReturn;
                }
                else
                {
                    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                    FileItem fileItemEnteteDocument = multipartRequest.getFile( OdsParameters.ENTETE_DOCUMENT );
                    FileItem fileItemPiedDocument = multipartRequest.getFile( OdsParameters.PIED_DOCUMENT );

                    String strReturn = null;

                    if ( ( null != fileItemEnteteDocument ) && ( null != ( fileItemEnteteDocument.getName(  ) ) ) &&
                            !( OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileItemEnteteDocument.getName(  ) ) ) &&
                            !isValideFile( fileItemEnteteDocument ) )
                    {
                        strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_MODELE_ENTETE_NO_XML,
                                AdminMessage.TYPE_STOP );
                    }
                    else if ( ( null != fileItemEnteteDocument ) && ( null != ( fileItemEnteteDocument.getName(  ) ) ) &&
                            !( OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileItemEnteteDocument.getName(  ) ) ) &&
                            ( null == modeleOrdreDuJour.getEnteteDocument(  ) ) )
                    {
                        modeleOrdreDuJour.setEnteteDocument( doCreationFichierPhysique( fileItemEnteteDocument ) );
                    }
                    else if ( ( null != fileItemEnteteDocument ) && ( null != ( fileItemEnteteDocument.getName(  ) ) ) &&
                            !( OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileItemEnteteDocument.getName(  ) ) ) )
                    {
                        FichierPhysique fichierEntete = modeleOrdreDuJour.getEnteteDocument(  );
                        doModificationFichierPhysique( fileItemEnteteDocument, fichierEntete );
                    }

                    if ( ( strReturn == null ) && ( null != fileItemPiedDocument ) &&
                            ( null != fileItemPiedDocument.getName(  ) ) &&
                            !( OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileItemPiedDocument.getName(  ) ) ) &&
                            ( !isValideFile( fileItemPiedDocument ) ) )
                    {
                        strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_MODELE_PIED_NO_XML,
                                AdminMessage.TYPE_STOP );
                    }
                    else if ( ( strReturn == null ) && ( null != fileItemPiedDocument ) &&
                            ( null != fileItemPiedDocument.getName(  ) ) &&
                            !( OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileItemPiedDocument.getName(  ) ) ) &&
                            ( null == modeleOrdreDuJour.getPiedDocument(  ) ) )
                    {
                        modeleOrdreDuJour.setPiedDocument( doCreationFichierPhysique( fileItemPiedDocument ) );
                    }
                    else if ( ( strReturn == null ) && ( null != fileItemPiedDocument ) &&
                            ( null != fileItemPiedDocument.getName(  ) ) &&
                            !( OdsConstants.CONSTANTE_CHAINE_VIDE.equals( fileItemPiedDocument.getName(  ) ) ) )
                    {
                        FichierPhysique fichierPiedDocument = modeleOrdreDuJour.getPiedDocument(  );
                        doModificationFichierPhysique( fileItemPiedDocument, fichierPiedDocument );
                    }

                    if ( strReturn != null )
                    {
                        return strReturn;
                    }
                }

                ModeleOrdreDuJourHome.update( modeleOrdreDuJour, getPlugin(  ) );
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
     * Permet d'initialiser la liste des différents type de conseil
     * @param plugin le plugin
     * @return la liste des formations
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
     * Permet d'initialiser la liste des choix de type de conseil
     * @param plugin le plugin
     * @return la liste des commissions
     */
    private ReferenceList initRefListCommission( Plugin plugin )
    {
        ReferenceList refListCommission = new ReferenceList(  );

        List<Commission> listCommission = CommissionHome.findCommissionList( plugin );
        // ajout dans la referenceList de l'item chaine vide
        refListCommission.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

        // ajout dans la referenceList de l'ensemble des items représentant les
        // groupes politiques
        for ( Commission commission : listCommission )
        {
            refListCommission.addItem( commission.getIdCommission(  ),
                OdsConstants.CONSTANTE_CHAINE_VIDE + commission.getNumero(  ) );
        }

        return refListCommission;
    }

    /**
     * Permet d'initialiser la liste des choix de type de conseil
     * @param plugin le plugin
     * @return la liste des types d'ordre du jour
     */
    private ReferenceList initRefListType( Plugin plugin )
    {
        ReferenceList refListType = new ReferenceList(  );
        List<TypeOrdreDuJour> listType = TypeOrdreDuJourHome.findTypeOrdreDuJourList( plugin );
        // ajout dans la referenceList de l'item chaine vide
        refListType.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

        // ajout dans la referenceList de l'ensemble des items représentant les
        // groupes politiques
        for ( TypeOrdreDuJour type : listType )
        {
            refListType.addItem( type.getIdTypeOrdreDuJour(  ), OdsConstants.CONSTANTE_CHAINE_VIDE +
                type.getLibelle(  ) );
        }

        return refListType;
    }

    /**
     * Retourne le premier champ requis manquant s'il y'en a .
     * Retourne un message d'erreur si les parametres saisis ne conviennent pas
     * @param request  la requete HTTP
     * @param modeleOrdreDuJour le modele d'ordre du jour
     * @return boolean retourne true si l'ensemble des critéres obligatoires ont été renseigné
     */
    private String testField( HttpServletRequest request, ModeleOrdreDuJour modeleOrdreDuJour )
    {
        String strRequiredField = OdsConstants.CONSTANTE_CHAINE_VIDE;

        if ( isValideForme( request ) )
        {
            FormationConseil formationConseil = new FormationConseil(  );
            TypeOrdreDuJour typeOrdreDuJour = new TypeOrdreDuJour(  );
            Commission commission = new Commission(  );
            int nIdFormationConseil;
            int nIdType;
            int nIdCommission;

            String strTitre = request.getParameter( OdsParameters.TITRE ).trim(  );

            try
            {
                nIdFormationConseil = Integer.parseInt( request.getParameter( OdsParameters.ID_FORMATION_CONSEIL ) );
                nIdType = Integer.parseInt( request.getParameter( OdsParameters.ID_TYPE_ORDRE_DU_JOUR ) );
                nIdCommission = Integer.parseInt( request.getParameter( OdsParameters.ID_COMMISSION ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );

                return strRequiredField;
            }

            if ( strTitre.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                strRequiredField = PROPERTY_LABEL_TITRE;
            }
            else if ( nIdType == -1 )
            {
                strRequiredField = PROPERTY_LABEL_TYPE;
            }
            else if ( nIdFormationConseil == -1 )
            {
                strRequiredField = PROPERTY_LABEL_FORMATION;
            }
            else if ( ( nIdCommission != -1 ) &&
                    ( ( nIdType == TypeOrdreDuJourEnum.PREVISIONNEL.getId(  ) ) ||
                    ( nIdType == TypeOrdreDuJourEnum.MIS_A_JOUR.getId(  ) ) ||
                    ( nIdType == TypeOrdreDuJourEnum.DEFINITIF.getId(  ) ) ) )
            {
                strRequiredField = MESSAGE_COMMISSION_NE_DOIT_PAS_ETRE_SAISIE;
            }
            else if ( ( nIdCommission == -1 ) && ( nIdType == TypeOrdreDuJourEnum.COMMISSION.getId(  ) ) )
            {
                strRequiredField = MESSAGE_COMMISSION_OBLIGATOIRE;
            }
            else if ( ( nIdType != -1 ) && ( nIdFormationConseil != -1 ) && ( nIdCommission != -1 ) &&
                    ModeleOrdreDuJourHome.isAlreadyExist( nIdType, nIdFormationConseil, nIdCommission,
                        modeleOrdreDuJour.getIdModele(  ), getPlugin(  ) ) )
            {
                strRequiredField = MESSAGE_MODELE_ALREADY_EXIST;
            }

            //on insere les parametres dans l'objet pasé en parametre
            modeleOrdreDuJour.setTitre( strTitre );
            formationConseil.setIdFormationConseil( nIdFormationConseil );
            modeleOrdreDuJour.setFormationConseil( formationConseil );

            typeOrdreDuJour.setIdTypeOrdreDuJour( nIdType );
            modeleOrdreDuJour.setTypeOrdreDuJour( typeOrdreDuJour );

            if ( nIdCommission != -1 )
            {
                commission.setIdCommission( nIdCommission );
            }
            else
            {
                commission = null;
            }

            modeleOrdreDuJour.setCommission( commission );
        }
        else
        {
            strRequiredField = PROPERTY_LABEL_TITRE;
        }

        return strRequiredField;
    }

    /**
     * methode qui test la validité  du formulaire de saisi ou de modification d'un MODJ,
     ** @param request  la requete HTTP
     * @return boolean
     */
    private boolean isValideForme( HttpServletRequest request )
    {
        if ( ( null != request.getParameter( OdsParameters.TITRE ) ) &&
                ( ( null != request.getParameter( OdsParameters.ID_TYPE_ORDRE_DU_JOUR ) ) &&
                ( null != request.getParameter( OdsParameters.ID_FORMATION_CONSEIL ) ) &&
                ( null != request.getParameter( OdsParameters.ID_COMMISSION ) ) ) )
        {
            return true;
        }

        return false;
    }

    /**
     * Méthode qui crée le  fichier récupéré de la requette
     * @param fileItem le fichier associé au voeu(récupéré de la requette )
     * @return Fichier créé
     */
    private FichierPhysique doCreationFichierPhysique( FileItem fileItem )
    {
        FichierPhysique newFichierPhysique = new FichierPhysique(  );
        // Creation de l'objet représentant le fichier physique
        newFichierPhysique.setDonnees( fileItem.get(  ) );
        newFichierPhysique.setIdFichier( FichierPhysiqueHome.create( newFichierPhysique, getPlugin(  ) ) );

        return newFichierPhysique;
    }

    /**
     * Modifie le fichier et le fichier physique lié au voeuAmendement
     * @param fileItem le fichier
     * @param fichierPhysique le fichier physique
     */
    private void doModificationFichierPhysique( FileItem fileItem, FichierPhysique fichierPhysique )
    {
        // Modification de l'objet représentant le fichier physique
        fichierPhysique.setDonnees( fileItem.get(  ) );
        FichierPhysiqueHome.update( fichierPhysique, getPlugin(  ) );
    }

    /**
     * test si le fichier fourni est valide
     * @param fileItem le fichier a tester
     * @return true si le fichier est valide  ont
     */
    private boolean isValideFile( FileItem fileItem )
    {
        boolean nIsFileGood = true;
        String extensionFile = fileItem.getName(  ).substring( fileItem.getName(  ).length(  ) - 3 ).toUpperCase(  );

        if ( !CONSTANTE_XML.equals( extensionFile ) )
        {
            nIsFileGood = false;
        }

        return nIsFileGood;
    }

    /**
     * Retourne une demande pour la suppression d'un modele choisi
     * @param request la requete HTTP
     * @return interface de confirmation de suppression
     */
    public String getSuppressionModele( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( ModelesOdjResourceIdService.MODELES_ODJ, RBAC.WILDCARD_RESOURCES_ID,
                    ModelesOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( request.getParameter( OdsParameters.ID_MODELE ) == null )
            {
                return getHomeUrl( request );
            }

            String strIdModele = request.getParameter( OdsParameters.ID_MODELE );
            UrlItem url = new UrlItem( JSP_URL_DO_SUPPRESSION_MODELE_JSP );
            url.addParameter( OdsParameters.ID_MODELE, strIdModele );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRME_DELETE_MODELE, url.getUrl(  ),
                AdminMessage.TYPE_CONFIRMATION );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * supprime l'élu choisi
     * @param request la requete HTTP
     * @return url de gestion des groupes poliques
     */
    public String doSuppressionModele( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( ModelesOdjResourceIdService.MODELES_ODJ, RBAC.WILDCARD_RESOURCES_ID,
                    ModelesOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( null != request.getParameter( OdsParameters.ID_MODELE ) )
            {
                ModeleOrdreDuJour modeleOrdreDuJour;
                Plugin plugin = getPlugin(  );

                try
                {
                    modeleOrdreDuJour = ModeleOrdreDuJourHome.findByPrimaryKey( Integer.parseInt( request.getParameter( 
                                    OdsParameters.ID_MODELE ) ), plugin );
                }
                catch ( NumberFormatException ne )
                {
                    AppLogService.error( ne );

                    return getHomeUrl( request );
                }

                try
                {
                    //suppression du  modele
                    ModeleOrdreDuJourHome.remove( modeleOrdreDuJour, plugin );

                    if ( null != modeleOrdreDuJour.getEnteteDocument(  ) )
                    {
                        FichierPhysiqueHome.remove( modeleOrdreDuJour.getEnteteDocument(  ), plugin );
                    }

                    if ( null != modeleOrdreDuJour.getPiedDocument(  ) )
                    {
                        FichierPhysiqueHome.remove( modeleOrdreDuJour.getPiedDocument(  ), plugin );
                    }
                }
                catch ( AppException ae )
                {
                    AppLogService.error( ae );

                    if ( ae.getInitialException(  ) instanceof SQLException )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_MODELE,
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
     * Télecharge un fichier.
     * Renvoie un flux de bytes du fichier dans l'objet HttpServletResponse response
     *
     * @param request la requete http
     * @param response la reponse Http
     */
    public void doDownloadFichier( HttpServletRequest request, HttpServletResponse response )
    {
        String strIdFichier = request.getParameter( OdsParameters.ID_FICHIER );
        String strType = request.getParameter( OdsParameters.TYPE );
        FichierPhysique fichierPhysique = null;

        if ( strIdFichier != null )
        {
            try
            {
                int nIdFichier = Integer.parseInt( strIdFichier );
                fichierPhysique = FichierPhysiqueHome.findByPrimaryKey( nIdFichier, getPlugin(  ) );
            }

            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
            }
        }

        try
        {
            String strFileName = ( strType != null ) ? strType : ( OdsConstants.CONSTANTE_CHAINE_VIDE + "." +
                CONSTANTE_XML );

            response.setHeader( "Content-Disposition", "attachment ;filename=\"" + strFileName + "\"" );

            response.setContentType( "application/x-msdownload" );

            response.setHeader( "Pragma", "public" );
            response.setHeader( "Expires", "0" );
            response.setHeader( "Cache-Control", "must-revalidate,post-check=0,pre-check=0" );

            OutputStream os = response.getOutputStream(  );

            if ( fichierPhysique != null )
            {
                os.write( fichierPhysique.getDonnees(  ) );
            }

            os.close(  );
        }
        catch ( IOException e )
        {
            AppLogService.error( e );
        }
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

        if ( !RBACService.isAuthorized( ModelesOdjResourceIdService.MODELES_ODJ, RBAC.WILDCARD_RESOURCES_ID,
                    ModelesOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            bPermissionGestion = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_GESTION, bPermissionGestion );
    }
}
