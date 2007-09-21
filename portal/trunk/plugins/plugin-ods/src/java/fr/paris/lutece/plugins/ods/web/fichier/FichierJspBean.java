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
package fr.paris.lutece.plugins.ods.web.fichier;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierFilter;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysique;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysiqueHome;
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocument;
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocumentEnum;
import fr.paris.lutece.plugins.ods.business.historique.Historique;
import fr.paris.lutece.plugins.ods.business.historique.HistoriqueHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.service.role.FichiersResourceIdService;
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
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;


/**
 * FichierJspBean
 */
public class FichierJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_FICHIERS = "ODS_FICHIERS";
    public static final String RIGHT_ODS_FICHIERS_GESTION_AVAL = "ODS_FICHIERS_GESTION_AVAL";
    private static final String TEMPLATE_LISTE_FICHIERS = "admin/plugins/ods/fichier/fichiers.html";
    private static final String TEMPLATE_CREATION_FICHIER = "admin/plugins/ods/fichier/creation_fichier.html";
    private static final String TEMPLATE_CREATION_PIECE_ANNEXE = "admin/plugins/ods/fichier/creation_piece_annexe.html";
    private static final String TEMPLATE_MODIFICATION_FICHIER = "admin/plugins/ods/fichier/modification_fichier.html";
    private static final String TEMPLATE_MODIFICATION_PIECE_ANNEXE = "admin/plugins/ods/fichier/modification_piece_annexe.html";
    private static final String TEMPLATE_HISTORIQUE = "admin/plugins/ods/historique/historique.html";
    private static final String PROPERTY_PAGE_CREATION_FICHIER = "ods.fichier.creation.pageTitle";
    private static final String PROPERTY_PAGE_MODIFICATION_FICHIER = "ods.fichier.modification.pageTitle";
    private static final String PROPERTY_PAGE_HISTORIQUE = "ods.historique.pageTitle";
    private static final String PROPERTY_FICHIERS_PER_PAGE = "ods.itemPerPage.label";
    private static final String PROPERTY_URL_DO_SUPPRESSION_FICHIER_JSP = "jsp/admin/plugins/ods/fichiers/DoSuppressionFichier.jsp";
    private static final String PROPERTY_URL_DO_SUPPRESSION_FICHIER_GESTION_AVAL_JSP = "jsp/admin/plugins/ods/fichiers/DoSuppressionFichierGestionAval.jsp";
    private static final String PROPERTY_LABEL_TYPE_DOCUMENT = "ods.creationfichier.label.typededocument";
    private static final String PROPERTY_LABEL_INTITULE = "ods.creationfichier.label.intitule";
    private static final String PROPERTY_LABEL_FICHIER = "ods.creationfichier.label.fichier";
    private static final String PROPERTY_LABEL_FICHIER_NO_PDF = "ods.creationfichier.label.fichier.noPDF";
    private static final String MARK_LISTE_FICHIERS = "liste_fichiers";
    private static final String MARK_LISTE_HISTORIQUES = "liste_historiques";
    private static final String MARK_LISTE_FICHIERS_SIZE_TOTAL = "liste_fichiers_size_total";
    private static final String MARK_LISTE_TYPE_DOCUMENT = "liste_type_document";
    private static final String MARK_ID_TYPE_DOCUMENT_SELECTED = "id_type_document_selected";
    private static final String MARK_LISTE_PUBLICATION = "liste_publication";
    private static final String MARK_ID_PUBLICATION_SELECTED = "id_publication_selected";
    private static final String MARK_LISTE_COMMISSION = "liste_commission";
    private static final String MARK_ID_COMMISSION_SELECTED = "id_commission_selected";
    private static final String MARK_LISTE_SEANCES = "liste_seances";
    private static final String MARK_ID_SEANCE_SELECTED = "id_seance_selected";
    private static final String MESSAGE_CANNOT_DELETE_FICHIER = "ods.message.cannotDeleteFichier";
    private static final String MESSAGE_CONFIRM_DELETE_FICHIER = "ods.message.confirmDeleteFichier";

    // Variable de session pour la séance
    private Seance _seance;

    // liste des seances pour la gestion aval
    private List<Seance> _listSeances;
    private int _nIdSeanceSelected;

    // Referencelist en vue de stocker l'ensemble des items représentant les types de documents
    private ReferenceList _referenceTypeDocumentList;
    private int _nIdTypeDocumentSelected;

    // Referencelist en vue de stocker l'ensemble des items représentant les commissions
    private ReferenceList _referenceCommissionList;
    private int _nIdCommissionSelected;

    // Referencelist en vue de stocker l'ensemble des items représentant les types de publication
    private ReferenceList _referencePublicationList;
    private int _nIdPublicationSelected;

    // Gere la page du Paginator
    private String _strCurrentPageIndex;

    //Gere le nombre d'item par page
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;

    /**
     * Constructor.
     */
    public FichierJspBean(  )
    {
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_FICHIERS_PER_PAGE, 10 );
    }

    /**
     * Retourne l'interface de gestion des fichiers.
     * @param request la requête http
     * @param bIsGestionAval si on est dans la gestion aval ou pas
     * @return la page affichant la liste des fichiers
     */
    public String getFichiersList( HttpServletRequest request, boolean bIsGestionAval )
    {
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        FichierFilter filter = new FichierFilter(  );

        List<Fichier> fichiers = new ArrayList<Fichier>(  );

        // on recupère la liste des fichiers correspondants au filtre
        if ( _seance != null )
        {
            filter.setIdSeance( _seance.getIdSeance(  ) );

            if ( _nIdTypeDocumentSelected != -1 )
            {
                filter.setIdTypeDocument( _nIdTypeDocumentSelected );
                filter.setPublication( _nIdPublicationSelected );
            }

            if ( _nIdPublicationSelected == -1 )
            {
                filter.setPublication( _nIdPublicationSelected );
            }

            if ( bIsGestionAval )
            {
                filter.setFilterTypeDocument( 2 );
            }
            else
            {
                filter.setFilterTypeDocument( 1 );
            }

            fichiers = FichierHome.findByFilter( filter, getPlugin(  ) );
        }

        setAllTypeDocuments( request, bIsGestionAval );

        Paginator paginator = new Paginator( fichiers, _nItemsPerPage, getHomeUrl( request ),
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        // Ajouts des éléments nécessaire dans la Hashmap
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        /*
         * Pour la gestion des profils
         */
        ajouterPermissionsDansHashmap( model );

        if ( bIsGestionAval )
        {
            model.put( MARK_LISTE_SEANCES, _listSeances );
            model.put( MARK_ID_SEANCE_SELECTED, _nIdSeanceSelected );
        }

        model.put( OdsMarks.MARK_GESTION_AVAL, bIsGestionAval );
        model.put( MARK_LISTE_FICHIERS, paginator.getPageItems(  ) );
        model.put( MARK_LISTE_FICHIERS_SIZE_TOTAL, fichiers.size(  ) );
        model.put( OdsMarks.MARK_PAGINATOR, paginator );
        model.put( OdsMarks.MARK_NB_ITEMS_PER_PAGE, OdsConstants.CONSTANTE_CHAINE_VIDE + _nItemsPerPage );
        model.put( Seance.MARK_PROCHAINE_SEANCE, _seance );
        model.put( MARK_LISTE_TYPE_DOCUMENT, _referenceTypeDocumentList );
        model.put( MARK_ID_TYPE_DOCUMENT_SELECTED, _nIdTypeDocumentSelected );
        model.put( MARK_LISTE_PUBLICATION, _referencePublicationList );
        model.put( MARK_ID_PUBLICATION_SELECTED, _nIdPublicationSelected );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_FICHIERS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Publication d'un fichier
     * Si "OdsParameters.FICHIER_IS_PUBLIER" égale à TRUE, alors la méthode <b>publie</b> le fichier et incrémente la version<BR>
     *                 Dans ce cas, un historique est crée
     * Sinon la méthode change le statut enLigne d'un fichier a FALSE     *
     * @param request la requête http
     * @param bNoReinit FALSE si on reinitialise, TRUE sinon
     * @return l'url
     */
    public String doPublicationFichier( HttpServletRequest request, boolean bNoReinit )
    {
        Timestamp dateForVersion = new Timestamp( System.currentTimeMillis(  ) );

        String strIdFichier = request.getParameter( OdsParameters.ID_FICHIER );
        String strIsPublier = request.getParameter( OdsParameters.FICHIER_IS_PUBLIER );

        /*
         * GESTION DES PROFILS
         * -------------------
         * Si l'utilisateur n'est pas autorisé a effectuer cette fonction il est renvoyé
         * vers une page lui indiquant qu'il n'a pas les droits nécessaires.
         */
        if ( !Boolean.parseBoolean( strIsPublier ) &&
                !RBACService.isAuthorized( FichiersResourceIdService.FICHIERS, RBAC.WILDCARD_RESOURCES_ID,
                    FichiersResourceIdService.PERMISSION_DEPUBLICATION, getUser(  ) ) )
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }

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

            Fichier fichier = FichierHome.findByPrimaryKey( nIdFichier, getPlugin(  ) );
            FichierHome.publication( fichier.getId(  ), dateForVersion, fichier.getVersion(  ),
                Boolean.parseBoolean( OdsConstants.CONSTANTE_CHAINE_VIDE + strIsPublier ), getPlugin(  ) );

            if ( Boolean.parseBoolean( OdsConstants.CONSTANTE_CHAINE_VIDE + strIsPublier ) )
            {
                Historique historique = new Historique(  );
                historique.setVersion( fichier.getVersion(  ) + 1 );
                historique.setDatePublication( dateForVersion );
                historique.setIdDocument( fichier.getId(  ) );
                HistoriqueHome.create( historique, getPlugin(  ) );
            }

            String strReturn = getHomeUrl( request );

            if ( bNoReinit )
            {
                strReturn += ( "?" + OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_SEANCE + "=" +
                fichier.getSeance(  ).getIdSeance(  ) );
            }

            if ( Boolean.parseBoolean( OdsConstants.CONSTANTE_CHAINE_VIDE + strIsPublier ) &&
                    estTypeNotification( fichier ) )
            {
                OdsUtils.doNotifierUtilisateurs( fichier, request, getPlugin(), getLocale() );
            }

            return strReturn;
        }

        String strReturn = getHomeUrl( request );

        if ( bNoReinit )
        {
            strReturn += ( "?" + OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_SEANCE + "=" +
            _seance.getIdSeance(  ) );
        }

        return strReturn;
    }

    /**
     * Télecharge un fichier.
     * Renvoie un flux de bytes du fichier dans l'objet HttpServletResponse response     *
     * @param request la requête http
     * @param response la réponse
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
     * retourne le formulaire de création d'un fichier
     * @param request la requête http
     * @param bIsGestionAval TRUE si on est dans la gestion aval, FALSE sinon
     * @return la page de creation de fichier
     */
    public String getCreationFichier( HttpServletRequest request, boolean bIsGestionAval )
    {
        setPageTitleProperty( PROPERTY_PAGE_CREATION_FICHIER );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        if ( bIsGestionAval )
        {
            model.put( MARK_LISTE_SEANCES, _listSeances );

            if ( _seance != null )
            {
                model.put( MARK_ID_SEANCE_SELECTED, _seance.getIdSeance(  ) );
            }
            else
            {
                model.put( MARK_ID_SEANCE_SELECTED, _nIdSeanceSelected );
            }
        }

        model.put( Seance.MARK_PROCHAINE_SEANCE, _seance );

        if ( bIsGestionAval )
        {
            /* Préparation du menu déroulant des types de documents dans la gestion aval */
            int nIdTypeDocument = -1;
            _referenceTypeDocumentList = new ReferenceList(  );
            _referenceTypeDocumentList.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );
            nIdTypeDocument = TypeDocumentEnum.COMPTE_RENDU.getId(  );
            _referenceTypeDocumentList.addItem( nIdTypeDocument,
                FichierHome.findTypeDocumentsById( nIdTypeDocument, getPlugin(  ) ).getLibelle(  ) );
            nIdTypeDocument = TypeDocumentEnum.DELIBERATION_DE_DESIGNATION.getId(  );
            _referenceTypeDocumentList.addItem( nIdTypeDocument,
                FichierHome.findTypeDocumentsById( nIdTypeDocument, getPlugin(  ) ).getLibelle(  ) );
            nIdTypeDocument = TypeDocumentEnum.DOCUMENT_LIBRE.getId(  );
            _referenceTypeDocumentList.addItem( nIdTypeDocument,
                FichierHome.findTypeDocumentsById( nIdTypeDocument, getPlugin(  ) ).getLibelle(  ) );
        }

        model.put( MARK_LISTE_TYPE_DOCUMENT, _referenceTypeDocumentList );
        model.put( MARK_LISTE_COMMISSION, _referenceCommissionList );
        model.put( MARK_ID_COMMISSION_SELECTED, _nIdCommissionSelected );
        model.put( OdsMarks.MARK_GESTION_AVAL, bIsGestionAval );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATION_FICHIER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * retourne le formulaire de création d'une piece annexe pour un pdd
     * @param request la requête http
     * @return la page de création de fichier pour un PDD
     */
    public String getCreationFichierForPDD( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_CREATION_FICHIER );

        String idPDD = request.getParameter( OdsParameters.ID_PDD );

        TypeDocument typePieceAnnexe = FichierHome.findTypeDocumentsById( TypeDocumentEnum.PIECE_ANNEXE.getId(  ),
                getPlugin(  ) );

        if ( _seance == null )
        {
            _seance = SeanceHome.getProchaineSeance( getPlugin(  ) );
        }

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( Seance.MARK_PROCHAINE_SEANCE, _seance );
        model.put( MARK_LISTE_TYPE_DOCUMENT, typePieceAnnexe );
        model.put( OdsParameters.ID_PDD, idPDD );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATION_PIECE_ANNEXE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * retourne le formulaire de modification d'une piece annexe pour un pdd
     * @param request la requête http
     * @return la page de modification d'un fichier pour un PDD
     */
    public String getModificationFichierForPDD( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_CREATION_FICHIER );

        String idPDD = request.getParameter( OdsParameters.ID_PDD );
        String strIdFichier = request.getParameter( OdsParameters.ID_FICHIER );
        int nIdFichier = -1;

        try
        {
            nIdFichier = Integer.parseInt( strIdFichier );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        Fichier fichier = FichierHome.findByPrimaryKey( nIdFichier, getPlugin(  ) );

        TypeDocument typePieceAnnexe = FichierHome.findTypeDocumentsById( TypeDocumentEnum.PIECE_ANNEXE.getId(  ),
                getPlugin(  ) );

        if ( _seance == null )
        {
            _seance = SeanceHome.getProchaineSeance( getPlugin(  ) );
        }

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( Seance.MARK_PROCHAINE_SEANCE, _seance );
        model.put( Fichier.MARK_FICHIER, fichier );
        model.put( MARK_LISTE_TYPE_DOCUMENT, typePieceAnnexe );
        model.put( OdsParameters.ID_PDD, idPDD );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_PIECE_ANNEXE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * retourne le formulaire de modification d'un fichier
     * @param request la requête http
     * @param bIsGestionAval TRUE si on est dans la gestion aval, FALSE sinon
     * @return la page de modification de fichier
     */
    public String getModificationFichier( HttpServletRequest request, boolean bIsGestionAval )
    {
        setPageTitleProperty( PROPERTY_PAGE_MODIFICATION_FICHIER );

        String strIdFichier = request.getParameter( OdsParameters.ID_FICHIER );
        int nIdFichier = -1;

        try
        {
            nIdFichier = Integer.parseInt( strIdFichier );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        Fichier fichier = FichierHome.findByPrimaryKey( nIdFichier, getPlugin(  ) );

        HtmlTemplate template;

        /*
         * GESTION DES PROFILS
         * -------------------
         * Si l'utilisateur n'est pas autorisé a effectuer cette fonction il est renvoyé
         * vers une page lui indiquant qu'il n'a pas les droits nécessaires.
         */
        if ( fichier.getEnLigne(  ) &&
                !RBACService.isAuthorized( FichiersResourceIdService.FICHIERS, RBAC.WILDCARD_RESOURCES_ID,
                    FichiersResourceIdService.PERMISSION_MISE_A_JOUR, getUser(  ) ) )
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ) );

            return getAdminPage( template.getHtml(  ) );
        }

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( Seance.MARK_PROCHAINE_SEANCE, _seance );
        model.put( Fichier.MARK_FICHIER, fichier );

        if ( bIsGestionAval )
        {
            /* Préparation du menu déroulant des types de documents dans la gestion aval */
            int nIdTypeDocument = -1;
            _referenceTypeDocumentList = new ReferenceList(  );
            _referenceTypeDocumentList.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );
            nIdTypeDocument = TypeDocumentEnum.COMPTE_RENDU.getId(  );
            _referenceTypeDocumentList.addItem( nIdTypeDocument,
                FichierHome.findTypeDocumentsById( nIdTypeDocument, getPlugin(  ) ).getLibelle(  ) );
            nIdTypeDocument = TypeDocumentEnum.DELIBERATION_DE_DESIGNATION.getId(  );
            _referenceTypeDocumentList.addItem( nIdTypeDocument,
                FichierHome.findTypeDocumentsById( nIdTypeDocument, getPlugin(  ) ).getLibelle(  ) );
            nIdTypeDocument = TypeDocumentEnum.DOCUMENT_LIBRE.getId(  );
            _referenceTypeDocumentList.addItem( nIdTypeDocument,
                FichierHome.findTypeDocumentsById( nIdTypeDocument, getPlugin(  ) ).getLibelle(  ) );
        }

        // on vérifie que la liste contient le type de document sinon on l'ajoute
        int nIdTypeDocumentFichier = fichier.getTypdeDocument(  ).getId(  );

        if ( bIsGestionAval && ( nIdTypeDocumentFichier != TypeDocumentEnum.COMPTE_RENDU.getId(  ) ) &&
                ( nIdTypeDocumentFichier != TypeDocumentEnum.DELIBERATION_DE_DESIGNATION.getId(  ) ) &&
                ( nIdTypeDocumentFichier != TypeDocumentEnum.DOCUMENT_LIBRE.getId(  ) ) )
        {
            ReferenceList refListTypeDocumentModification = _referenceTypeDocumentList;
            refListTypeDocumentModification.addItem( fichier.getTypdeDocument(  ).getId(  ),
                fichier.getTypdeDocument(  ).getLibelle(  ) );
            model.put( MARK_LISTE_TYPE_DOCUMENT, refListTypeDocumentModification );
        }
        else
        {
            model.put( MARK_LISTE_TYPE_DOCUMENT, _referenceTypeDocumentList );
        }

        // on ajoute l'id du type de document sélectionné ou l'id par défaut
        if ( fichier.getTypdeDocument(  ) != null )
        {
            model.put( MARK_ID_TYPE_DOCUMENT_SELECTED, fichier.getTypdeDocument(  ).getId(  ) );
        }
        else
        {
            model.put( MARK_ID_TYPE_DOCUMENT_SELECTED, _nIdTypeDocumentSelected );
        }

        model.put( MARK_LISTE_COMMISSION, _referenceCommissionList );

        if ( fichier.getCommission(  ) != null )
        {
            model.put( MARK_ID_COMMISSION_SELECTED, fichier.getCommission(  ).getIdCommission(  ) );
        }
        else
        {
            model.put( MARK_ID_COMMISSION_SELECTED, _nIdCommissionSelected );
        }

        if ( bIsGestionAval )
        {
            model.put( MARK_LISTE_SEANCES, _listSeances );

            if ( fichier.getSeance(  ) != null )
            {
                model.put( MARK_ID_SEANCE_SELECTED, fichier.getSeance(  ).getIdSeance(  ) );
            }
            else
            {
                model.put( MARK_ID_SEANCE_SELECTED, _nIdSeanceSelected );
            }
        }

        model.put( OdsMarks.MARK_GESTION_AVAL, bIsGestionAval );

        template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_FICHIER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * retourne l'ecran de visualisation de l'historique des versions d'un fichier
     * @param request la requête http
     * @return la page de l'historique d'un fichier
     */
    public String getHistoriqueFichier( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_HISTORIQUE );

        String strIdFichier = request.getParameter( OdsParameters.ID_FICHIER );
        int nIdFichier = -1;

        try
        {
            nIdFichier = Integer.parseInt( strIdFichier );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        Fichier fichier = FichierHome.findByPrimaryKey( nIdFichier, getPlugin(  ) );

        List<Historique> historiques = HistoriqueHome.getHistoriqueList( fichier, getPlugin(  ) );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( MARK_LISTE_HISTORIQUES, historiques );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_HISTORIQUE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * retourne une demande de confirmation pour la suppression du fichier choisie
     * @param request la requête http
     * @param bIsGestionAval TRUE si on est dans la gestion aval, FALSE sinon
     * @return la page de confirmation de suppression d'un fichier
     */
    public String getSuppressionFichier( HttpServletRequest request, boolean bIsGestionAval )
    {
        if ( request.getParameter( OdsParameters.ID_FICHIER ) != null )
        {
            String strIdFichier = request.getParameter( OdsParameters.ID_FICHIER );
            int nIdFichier = -1;

            try
            {
                nIdFichier = Integer.parseInt( strIdFichier.trim(  ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            Fichier fichier = FichierHome.findByPrimaryKey( nIdFichier, getPlugin(  ) );

            /*
            * GESTION DES PROFILS
            * -------------------
            * Si l'utilisateur n'est pas autorisé a effectuer cette fonction il est renvoyé
            * vers une page lui indiquant qu'il n'a pas les droits nécessaires.
            */
            if ( fichier.getEnLigne(  ) &&
                    !RBACService.isAuthorized( FichiersResourceIdService.FICHIERS, RBAC.WILDCARD_RESOURCES_ID,
                        FichiersResourceIdService.PERMISSION_SUPPRESSION, getUser(  ) ) )
            {
                return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                    AdminMessage.TYPE_ERROR );
            }

            UrlItem url;

            if ( bIsGestionAval )
            {
                url = new UrlItem( PROPERTY_URL_DO_SUPPRESSION_FICHIER_GESTION_AVAL_JSP );
            }
            else
            {
                url = new UrlItem( PROPERTY_URL_DO_SUPPRESSION_FICHIER_JSP );
            }

            url.addParameter( OdsParameters.ID_FICHIER, strIdFichier );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_FICHIER, url.getUrl(  ),
                AdminMessage.TYPE_CONFIRMATION );
        }
        else
        {
            return getHomeUrl( request );
        }
    }

    /**
     * supprime le fichier choisi
     * @param request la requête http
     * @param bNoReinit FALSE si on reinitialise, TRUE sinon
     * @return l'url de suppression de fichier
     */
    public String doSuppressionFichier( HttpServletRequest request, boolean bNoReinit )
    {
        String strIdFichier = request.getParameter( OdsParameters.ID_FICHIER );

        if ( strIdFichier != null )
        {
            String strReturn = null;

            try
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

                Fichier fichier = FichierHome.findByPrimaryKey( nIdFichier, getPlugin(  ) );

                /*
                 * GESTION DES PROFILS
                 * -------------------
                 * Si l'utilisateur n'est pas autorisé a effectuer cette fonction il est renvoyé
                 * vers une page lui indiquant qu'il n'a pas les droits nécessaires.
                 */
                if ( fichier.getEnLigne(  ) &&
                        !RBACService.isAuthorized( FichiersResourceIdService.FICHIERS, RBAC.WILDCARD_RESOURCES_ID,
                            FichiersResourceIdService.PERMISSION_SUPPRESSION, getUser(  ) ) )
                {
                    return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                        AdminMessage.TYPE_ERROR );
                }

                FichierPhysique fichierPhysique = FichierPhysiqueHome.findByPrimaryKey( fichier.getFichier(  )
                                                                                               .getIdFichier(  ),
                        getPlugin(  ) );

                HistoriqueHome.remove( fichier, getPlugin(  ) );
                FichierHome.remove( fichier, getPlugin(  ) );
                FichierPhysiqueHome.remove( fichierPhysique, getPlugin(  ) );

                strReturn = getHomeUrl( request );

                if ( bNoReinit )
                {
                    strReturn += ( "?" + OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_SEANCE + "=" +
                    fichier.getSeance(  ).getIdSeance(  ) );
                }
            }
            catch ( NumberFormatException nfe )
            {
                strReturn = getHomeUrl( request );
            }
            catch ( AppException ae )
            {
                if ( ae.getInitialException(  ) instanceof SQLException )
                {
                    strReturn = AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_FICHIER,
                            AdminMessage.TYPE_STOP );
                }

                if ( strReturn == null )
                {
                    strReturn = getHomeUrl( request );

                    if ( bNoReinit )
                    {
                        strReturn += ( "?" + OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_SEANCE + "=" +
                        _seance.getIdSeance(  ) );
                    }
                }
            }

            return strReturn;
        }
        else
        {
            String strReturn = getHomeUrl( request );

            if ( bNoReinit )
            {
                strReturn += ( "?" + OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_SEANCE + "=" +
                _seance.getIdSeance(  ) );
            }

            return strReturn;
        }
    }

    /**
     * Modifie le fichier et le fichier physique
     * Si le fichie est en ligne, alors la version est incrémenté de 1 et ajout d'un historique
     * si tous les champs obligatoires du formulaire de modification d'un fichier ont été renseigné,<BR>
     * la méthode retourne l'url de gestion des fichiers,<BR>
     * sinon l'url retournée est celle de l'interface spécifiant les messages d'erreurs.
     * @param request la requête http
     * @param bNoReinit FALSE si on reinitialise, TRUE sinon
     * @return l'url de modification d'un fichier
     */
    public String doModificationFichier( HttpServletRequest request, boolean bNoReinit )
    {
        Timestamp dateForVersion = new Timestamp( System.currentTimeMillis(  ) );
        String strIdFichier = request.getParameter( OdsParameters.ID_FICHIER ).trim(  );
        int nModificationIdTypeDocument = -1;

        try
        {
            nModificationIdTypeDocument = Integer.parseInt( request.getParameter( OdsParameters.ID_TYPE_DOCUMENT ).trim(  ) );
        }
        catch ( NumberFormatException nfe )
        {
            AppLogService.error( nfe );
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        if ( ( strIdFichier != null ) && ( nModificationIdTypeDocument != -1 ) )
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

            Fichier fichier = FichierHome.findByPrimaryKey( nIdFichier, getPlugin(  ) );

            /*
             * GESTION DES PROFILS
             * -------------------
             * Si l'utilisateur n'est pas autorisé a effectuer cette fonction il est renvoyé
             * vers une page lui indiquant qu'il n'a pas les droits nécessaires.
             */
            if ( fichier.getEnLigne(  ) &&
                    !RBACService.isAuthorized( FichiersResourceIdService.FICHIERS, RBAC.WILDCARD_RESOURCES_ID,
                        FichiersResourceIdService.PERMISSION_MISE_A_JOUR, getUser(  ) ) )
            {
                return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                    AdminMessage.TYPE_ERROR );
            }

            FichierPhysique fichierPhysique = FichierPhysiqueHome.findByPrimaryKey( fichier.getFichier(  ).getIdFichier(  ),
                    getPlugin(  ) );

            // recupération des modifications
            int nModificationIdSeance = -1;

            if ( request.getParameter( OdsParameters.ID_SEANCE ) != null )
            {
                try
                {
                    nModificationIdSeance = Integer.parseInt( request.getParameter( OdsParameters.ID_SEANCE ).trim(  ) );
                }
                catch ( NumberFormatException nfe )
                {
                    AppLogService.error( nfe );
                }
            }

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
            fichier.setTypdeDocument( getTypeDocumentObject( nModificationIdTypeDocument ) );

            if ( nModificationIdSeance > 0 )
            {
                Seance seance = SeanceHome.findByPrimaryKey( nModificationIdSeance, getPlugin(  ) );
                fichier.setSeance( seance );
            }

            fichier.setTitre( strModificationIntitule );
            fichier.setTexte( strModificationFichierTxtRecherche );

            if ( fichier.getEnLigne(  ) )
            {
                // on incremente le numero de version
                fichier.setVersion( fichier.getVersion(  ) + 1 );
                fichier.setDatePublication( dateForVersion );

                //Ajout de l'historique
                Historique historique = new Historique(  );
                historique.setVersion( fichier.getVersion(  ) );
                historique.setDatePublication( dateForVersion );
                historique.setIdDocument( fichier.getId(  ) );
                HistoriqueHome.create( historique, getPlugin(  ) );
            }

            FichierHome.update( fichier, getPlugin(  ) );

            String strReturn = getHomeUrl( request );

            if ( bNoReinit )
            {
                strReturn += ( "?" + OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_SEANCE + "=" +
                fichier.getSeance(  ).getIdSeance(  ) );
            }

            return strReturn;
        }
        else
        {
            Object[] messagesArgs = { I18nService.getLocalizedString( PROPERTY_LABEL_TYPE_DOCUMENT, getLocale(  ) ) };

            return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                messagesArgs, AdminMessage.TYPE_STOP );
        }
    }

    /**
     * Creation d'un fichier
     * si tous les champs obligatoires du formulaire de création d'un fichier ont été renseigné,<BR>
     * la méthode retourne l'url de gestion des fichiers,<BR>
     * sinon l'url retournée est celle de l'interface spécifiant les messages d'erreurs.
     * @param request la requête http
     * @param bNoReinit FALSE si on reinitialise, TRUE sinon
     * @return l'url de creation d'un fichier
     */
    public String doCreationFichier( HttpServletRequest request, boolean bNoReinit )
    {
        List<String> champsNonSaisie = new ArrayList<String>(  );

        if ( isValideForme( champsNonSaisie, request ) )
        {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            int nCreationIdTypeDocument = -1;
            String strCreationIntitule;
            FileItem fileItem = null;
            String strCreationFichierTxtRecherche;
            boolean enLigne = false;

            int nModificationIdSeance = -1;

            if ( request.getParameter( OdsParameters.ID_SEANCE ) != null )
            {
                try
                {
                    nModificationIdSeance = Integer.parseInt( request.getParameter( OdsParameters.ID_SEANCE ).trim(  ) );
                    nCreationIdTypeDocument = Integer.parseInt( request.getParameter( OdsParameters.ID_TYPE_DOCUMENT ) );
                }
                catch ( NumberFormatException nfe )
                {
                    AppLogService.error( nfe );
                }
            }

            strCreationIntitule = request.getParameter( OdsParameters.FICHIER_INTITULE );
            strCreationFichierTxtRecherche = request.getParameter( OdsParameters.FICHIER_TXT_RECHERCHE );

            fileItem = multipartRequest.getFile( OdsParameters.FICHIER_PHYSIQUE );

            Fichier newFichier = new Fichier(  );
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
                    return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_NO_PDF,
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
            if ( nModificationIdSeance > 0 )
            {
                Seance seance = SeanceHome.findByPrimaryKey( nModificationIdSeance, getPlugin(  ) );
                newFichier.setSeance( seance );
            }

            if ( nCreationIdTypeDocument > 0 )
            {
                newFichier.setTypdeDocument( getTypeDocumentObject( nCreationIdTypeDocument ) );
            }

            newFichier.setTitre( strCreationIntitule );
            newFichier.setTexte( strCreationFichierTxtRecherche );
            newFichier.setEnLigne( enLigne );
            FichierHome.create( newFichier, getPlugin(  ) );

            String strReturn = getHomeUrl( request );

            if ( bNoReinit )
            {
                strReturn += ( "?" + OdsParameters.PLUGIN_NAME + "&" + OdsParameters.ID_SEANCE +
                newFichier.getSeance(  ).getIdSeance(  ) );
            }

            return strReturn;
        }
        else
        {
            String strChamp = ( champsNonSaisie.get( 0 ) != null ) ? champsNonSaisie.get( 0 ) : Messages.MANDATORY_FIELDS;
            Object[] messagesArgs = { I18nService.getLocalizedString( strChamp, getLocale(  ) ) };

            return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                messagesArgs, AdminMessage.TYPE_STOP );
        }
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
     * Retourne l'objet correspondant à l'id passé en paramètre
     * Si l'id n'est pas trouvé alors on renvoie un objet vide
     * @param creationIdTypeDocument id d'un type de document
     * @return Objet TypeDocument correspondant à l'Id
     */
    private TypeDocument getTypeDocumentObject( int creationIdTypeDocument )
    {
        TypeDocument typeDocument = new TypeDocument(  );
        typeDocument.setId( creationIdTypeDocument );
        typeDocument.setLibelle( OdsConstants.CONSTANTE_CHAINE_VIDE );

        return typeDocument;
    }

    /**
     * Permet d'initialiser la prochaine seance
     */
    public void setProchaineSeance(  )
    {
        _seance = SeanceHome.getProchaineSeance( getPlugin(  ) );
    }

    /**
     * Permet d'initialiser la dernière seance
     * @param request la requete Http
     */
    public void setAllSeances( HttpServletRequest request )
    {
        // la liste contient toutes les seances passées
        _listSeances = SeanceHome.findOldSeance( getPlugin(  ) );

        Seance prochaineSeance = SeanceHome.getProchaineSeance( getPlugin(  ) );

        if ( ( request.getParameter( OdsParameters.ID_SEANCE ) != null ) &&
                !request.getParameter( OdsParameters.ID_SEANCE ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            int nIdSeance = -1;

            try
            {
                nIdSeance = Integer.parseInt( request.getParameter( OdsParameters.ID_SEANCE ).trim(  ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            if ( nIdSeance > 0 )
            {
                _seance = SeanceHome.findByPrimaryKey( nIdSeance, getPlugin(  ) );
            }
            else
            {
                _seance = SeanceHome.getDerniereSeance( getPlugin(  ) );
            }
        }
        else if ( ( _seance == null ) ||
                ( ( prochaineSeance != null ) && ( _seance.getIdSeance(  ) == prochaineSeance.getIdSeance(  ) ) ) )
        {
            _seance = SeanceHome.getDerniereSeance( getPlugin(  ) );
        }

        if ( _seance != null )
        {
            _nIdSeanceSelected = _seance.getIdSeance(  );
        }
        else
        {
            _nIdSeanceSelected = -1;
        }
    }

    /**
     * Permet d'initialiser la liste des différents types de documents
     * @param request la requête http
     * @param bIsGestionAval TRUE si on est dans la gestion aval, FALSE sinon
     */
    public void setAllTypeDocuments( HttpServletRequest request, boolean bIsGestionAval )
    {
        List<TypeDocument> typeDocuments;

        if ( bIsGestionAval )
        {
            typeDocuments = FichierHome.findTypeDocumentsAvalList( getPlugin(  ) );
        }
        else
        {
            typeDocuments = FichierHome.findTypeDocumentsProchaineSeanceList( getPlugin(  ) );
        }

        _referenceTypeDocumentList = new ReferenceList(  );
        //ajout dans la referenceList de l'item chaine vide
        _referenceTypeDocumentList.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

        //ajout dans la referenceList de l'ensemble des items représentant les types de documents
        for ( TypeDocument typeDocument : typeDocuments )
        {
            _referenceTypeDocumentList.addItem( typeDocument.getId(  ), typeDocument.getLibelle(  ) );
        }

        int nIdTypeDocument = -1;

        if ( request.getParameter( OdsParameters.ID_TYPE_DOCUMENT ) != null )
        {
            try
            {
                nIdTypeDocument = Integer.parseInt( request.getParameter( OdsParameters.ID_TYPE_DOCUMENT ).trim(  ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }
        }

        _nIdTypeDocumentSelected = nIdTypeDocument;
    }

    /**
     * Permet d'initialiser la liste des choix de publications
     * Les choix possibles: OUI, NON ou Pas d'importance
     * @param request la requête http
     */
    public void setAllPublication( HttpServletRequest request )
    {
        _referencePublicationList = new ReferenceList(  );

        //ajout dans la referenceList de l'item chaine vide
        _referencePublicationList.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );
        _referencePublicationList.addItem( 0,
            I18nService.getLocalizedString( OdsConstants.CONSTANTE_NON, getLocale(  ) ) );
        _referencePublicationList.addItem( 1,
            I18nService.getLocalizedString( OdsConstants.CONSTANTE_OUI, getLocale(  ) ) );

        int nIdPublication = -1;

        if ( request.getParameter( OdsParameters.ID_PUBLICATION ) != null )
        {
            nIdPublication = Integer.parseInt( request.getParameter( OdsParameters.ID_PUBLICATION ) );
        }

        _nIdPublicationSelected = nIdPublication;
    }

    /**
     * methode qui test si dans le formulaire de saisi d'un fichier,l'ensemble des critéres obligatoires ont été renseigné
     * @param request la requête http
     * @param champsNonSaisie List<String>
     * @return VRAI si le formulaire envoyé est valide
     */
    private boolean isValideForme( List<String> champsNonSaisie, HttpServletRequest request )
    {
        boolean isValid = true;

        try
        {
            if ( Integer.parseInt( request.getParameter( OdsParameters.ID_TYPE_DOCUMENT ) ) == -1 )
            {
                champsNonSaisie.add( PROPERTY_LABEL_TYPE_DOCUMENT );
                isValid = false;
            }
        }
        catch ( NumberFormatException nfe )
        {
            isValid = false;
        }

        String strCreationIntitule = request.getParameter( OdsParameters.FICHIER_INTITULE );

        if ( ( strCreationIntitule == null ) || OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strCreationIntitule ) )
        {
            champsNonSaisie.add( PROPERTY_LABEL_INTITULE );
            isValid = false;
        }

        return isValid;
    }

    /**
     * Vérifie que le fichier correspond bien au type de fichier pour lequel on doit
     * notifier les utilisateurs de leur publication.
     * @param fichier le fichier à vérifier
     * @return TRUE si le fichier doit être notifié, FALSE sinon
     */
    private boolean estTypeNotification( Fichier fichier )
    {
        int idTypeFichier = fichier.getTypdeDocument(  ).getId(  );

        boolean bRetour = false;

        if ( idTypeFichier == TypeDocumentEnum.CONVOCATION_DU_MAIRE.getId(  ) )
        {
            bRetour = true;
        }
        else if ( idTypeFichier == TypeDocumentEnum.LISTE_INTEGRALE_DES_QUESTIONS.getId(  ) )
        {
            bRetour = true;
        }
        else if ( idTypeFichier == TypeDocumentEnum.RELEVE_DE_LA_CONFERENCE_DORGANISATION.getId(  ) )
        {
            bRetour = true;
        }
        else if ( idTypeFichier == TypeDocumentEnum.RESUME_DES_QUESTIONS.getId(  ) )
        {
            bRetour = true;
        }
        else if ( idTypeFichier == TypeDocumentEnum.DESIGNATION.getId(  ) )
        {
            bRetour = true;
        }
        else if ( idTypeFichier == TypeDocumentEnum.DOCUMENT_LIBRE.getId(  ) )
        {
            bRetour = true;
        }
        else if ( idTypeFichier == TypeDocumentEnum.LIASSE_DES_VA.getId(  ) )
        {
            bRetour = true;
        }
        else if ( idTypeFichier == TypeDocumentEnum.RELEVE.getId(  ) )
        {
            bRetour = true;
        }
        else if ( idTypeFichier == TypeDocumentEnum.ORDRE_DU_JOUR.getId(  ) )
        {
            bRetour = true;
        }
        else if ( idTypeFichier == TypeDocumentEnum.COMPTE_RENDU.getId(  ) )
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
         * Permission de dépublier
         */
        boolean bPermissionDepublication = true;

        if ( !RBACService.isAuthorized( FichiersResourceIdService.FICHIERS, RBAC.WILDCARD_RESOURCES_ID,
                    FichiersResourceIdService.PERMISSION_DEPUBLICATION, getUser(  ) ) )
        {
            bPermissionDepublication = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_DEPUBLICATION, bPermissionDepublication );

        /*
         * Permission de modifier
         */
        boolean bPermissionModification = true;

        if ( !RBACService.isAuthorized( FichiersResourceIdService.FICHIERS, RBAC.WILDCARD_RESOURCES_ID,
                    FichiersResourceIdService.PERMISSION_MISE_A_JOUR, getUser(  ) ) )
        {
            bPermissionModification = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_MODIFICATION, bPermissionModification );

        /*
         * Permission de supprimer
         */
        boolean bPermissionSuppression = true;

        if ( !RBACService.isAuthorized( FichiersResourceIdService.FICHIERS, RBAC.WILDCARD_RESOURCES_ID,
                    FichiersResourceIdService.PERMISSION_SUPPRESSION, getUser(  ) ) )
        {
            bPermissionSuppression = false;
        }

        model.put( OdsMarks.MARK_PERMISSION_SUPPRESSION, bPermissionSuppression );
    }
}
