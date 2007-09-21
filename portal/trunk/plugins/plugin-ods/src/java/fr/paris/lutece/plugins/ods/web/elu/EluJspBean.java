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
package fr.paris.lutece.plugins.ods.web.elu;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.commission.CommissionHome;
import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.elu.EluHome;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitique;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitiqueHome;
import fr.paris.lutece.plugins.ods.service.role.AdminSaufOdjResourceIdService;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.constants.OdsProperties;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * gestion des elus
 *
 */
public class EluJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_ELU = "ODS_ELUS";
    private static final String TEMPLATE_LISTE_ELUS = "admin/plugins/ods/elu/liste_elus.html";
    private static final String TEMPLATE_CREATION_ELU = "admin/plugins/ods/elu/creation_elu.html";
    private static final String TEMPLATE_MODIFICATION_ELU = "admin/plugins/ods/elu/modification_elu.html";
    private static final String PROPERTY_PAGE_TITLE_ELU = "ods.elu.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFICATION_ELU = "ods.modificationelu.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATION_ELU = "ods.creationelu.label.title";
    private static final String MARK_LISTE_ELUS_COUNT = "liste_elus_count";
    private static final String MARK_LISTE_ELUS = "liste_elus";
    private static final String MARK_LISTE_COM = "liste_commissions";
    private static final String MARK_LISTE_GROUPES = "liste_groupes";
    private static final String MARK_LISTE_REMP = "liste_remplacants";
    private static final String MARK_ELU = "elu";
    private static final String MARK_ID_GROUPE_SELECTED = "id_groupe_selected";
    private static final String MESSAGE_CONFIRMDELETEELU = "ods.message.confirmDeleteElu";
    private static final String MESSAGE_CANNOT_DELETE_ELU = "ods.message.cannotDeleteElu";
    private static final String FIELD_CIVILITE = "ods.creationelu.label.civilite";
    private static final String FIELD_PRENOM_ELU = "ods.creationelu.label.prenom_elu";
    private static final String FIELD_NOM_ELU = "ods.creationelu.label.nom_elu";
    private static final String FIELD_ID_GROUPE = "ods.creationelu.label.groupe";
    private static final String JSP_URL_DO_SUPPRESSION_ELU_JSP = "jsp/admin/plugins/ods/elu/DoSuppressionElu.jsp";

    /**
     * Retourne le formulaire de création d'un élu
     * @param request la requete HTTP
     * @return formulaire de création d'un élu
     */
    public String getCreationElu( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATION_ELU );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        HtmlTemplate template;

        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            // Création d'un objet Referencelist en vue de stocker l'ensemble 
            // des items représentant les groupes politiques, les commissions 
            // et les élus (remplaçants possibles).
            ReferenceList referenceListGroupes = new ReferenceList(  );
            ReferenceList referenceListCommissions = new ReferenceList(  );
            ReferenceList referenceListElus = new ReferenceList(  );

            List<GroupePolitique> listGroupePolitique = GroupePolitiqueHome.findGroupesActifs( getPlugin(  ) );

            // Ajout dans la referenceListGroupes de l'item chaine vide
            referenceListGroupes.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

            // Ajout dans la referenceListGroupes de l'ensemble des items représentant 
            // les groupes politiques
            for ( GroupePolitique groupe : listGroupePolitique )
            {
                referenceListGroupes.addItem( groupe.getIdGroupe(  ), groupe.getNomGroupe(  ) );
            }

            model.put( MARK_LISTE_GROUPES, referenceListGroupes );

            List<Commission> listCommissions = CommissionHome.findCommissionList( getPlugin(  ) );
            referenceListCommissions.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

            for ( Commission commission : listCommissions )
            {
                referenceListCommissions.addItem( commission.getIdCommission(  ), commission.getLibelle(  ) );
            }

            model.put( MARK_LISTE_COM, referenceListCommissions );

            List<Elu> listElusRemplacants = EluHome.findEluList( getPlugin(  ) );
            referenceListElus.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

            for ( Elu remplacant : listElusRemplacants )
            {
                String nom = remplacant.getCivilite(  ) + " " + remplacant.getPrenomElu(  ) + " " +
                    remplacant.getNomElu(  ) + " (" + remplacant.getGroupe(  ).getNomGroupe(  ) + ")";
                referenceListElus.addItem( remplacant.getIdElu(  ), nom );
            }

            model.put( MARK_LISTE_REMP, referenceListElus );

            template = AppTemplateService.getTemplate( TEMPLATE_CREATION_ELU, getLocale(  ), model );
        }
        else
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * crée élu.
     * si tous les champs obligatoires du formulaire de création d'un élu ont été renseigné,
     * la méthode retourne l'url de gestion des élus,
     * sinon l'url retournée est celle de l'interface spécifiant les messages d'erreurs.
     * @param request request
     * @return url url
     */
    public String doCreationElu( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( !requiredField( request ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                Object[] tabRequiredFields = { I18nService.getLocalizedString( requiredField( request ), getLocale(  ) ) };

                return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                    tabRequiredFields, AdminMessage.TYPE_STOP );
            }

            Elu elu = new Elu(  );

            String strNom = request.getParameter( OdsParameters.NOM_ELU ).trim(  );

            if ( strNom.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
            }

            elu.setNomElu( strNom );

            String strPrenom = request.getParameter( OdsParameters.PRENOM_ELU ).trim(  );
            elu.setPrenomElu( strPrenom );

            String strCivilite = request.getParameter( OdsParameters.CIVILITE ).trim(  );
            elu.setCivilite( strCivilite );

            int nIdGroupe = -1;

            try
            {
                nIdGroupe = Integer.parseInt( request.getParameter( OdsParameters.ID_GROUPE ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            elu.setGroupe( GroupePolitiqueHome.findByPrimaryKey( nIdGroupe, getPlugin(  ) ) );

            int nIdCommission = -1;

            try
            {
                nIdCommission = Integer.parseInt( request.getParameter( OdsParameters.ID_COMMISSION ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            Commission commission = null;

            if ( nIdCommission != -1 )
            {
                commission = CommissionHome.findByPrimaryKey( nIdCommission, getPlugin(  ) );
            }

            elu.setCommission( commission );

            int nIdRemplacant = -1;

            try
            {
                nIdRemplacant = Integer.parseInt( request.getParameter( OdsParameters.ID_REMPLACANT ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            Elu remplacant = null;

            if ( nIdRemplacant != -1 )
            {
                remplacant = EluHome.findByPrimaryKey( nIdRemplacant, getPlugin(  ) );
            }

            elu.setEluRemplace( remplacant );

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

            elu.setActif( bActif );

            EluHome.create( elu, getPlugin(  ) );

            return getHomeUrl( request );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * Retourne le formulaire de modification d'un élu  pré remplis avec  les critères  de l'élu choisi
     * @param request la requete HTTP
     * @return formulaire de modification d'un élu
     */
    public String getModificationElu( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFICATION_ELU );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        HtmlTemplate template;

        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            try
            {
                if ( null != request.getParameter( OdsParameters.ID_ELU ) )
                {
                    // Création d'un objet Referencelist en vue de stocker
                    // l'ensemble des items représentant les groupes politiques,
                    // les élus et les commissions.
                    ReferenceList referenceListGroupes = new ReferenceList(  );
                    ReferenceList referenceListCommissions = new ReferenceList(  );
                    ReferenceList referenceListElus = new ReferenceList(  );

                    List<GroupePolitique> listGroupePolitique = GroupePolitiqueHome.findGroupesActifs( getPlugin(  ) );

                    // Ajout dans la referenceListGroupes de l'item chaine vide
                    referenceListGroupes.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

                    // Ajout dans la referenceListGroupes de l'ensemble des items représentant 
                    // les groupes politiques
                    for ( GroupePolitique groupe : listGroupePolitique )
                    {
                        referenceListGroupes.addItem( groupe.getIdGroupe(  ), groupe.getNomGroupe(  ) );
                    }

                    model.put( MARK_LISTE_GROUPES, referenceListGroupes );

                    List<Commission> listCommissions = CommissionHome.findCommissionList( getPlugin(  ) );
                    referenceListCommissions.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

                    for ( Commission commission : listCommissions )
                    {
                        referenceListCommissions.addItem( commission.getIdCommission(  ), commission.getLibelle(  ) );
                    }

                    model.put( MARK_LISTE_COM, referenceListCommissions );

                    List<Elu> listElusRemplacants = EluHome.findEluList( getPlugin(  ) );
                    referenceListElus.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

                    for ( Elu eluRemplacant : listElusRemplacants )
                    {
                        int nIdElu = -1;

                        try
                        {
                            nIdElu = Integer.parseInt( request.getParameter( OdsParameters.ID_ELU ) );
                        }
                        catch ( NumberFormatException nfe )
                        {
                            AppLogService.error( nfe );
                        }

                        if ( eluRemplacant.getIdElu(  ) != nIdElu )
                        {
                            String strNom = eluRemplacant.getCivilite(  ) + " " + eluRemplacant.getPrenomElu(  ) + " " +
                                eluRemplacant.getNomElu(  ) + " (" + eluRemplacant.getGroupe(  ).getNomGroupe(  ) +
                                ")";
                            referenceListElus.addItem( eluRemplacant.getIdElu(  ), strNom );
                        }
                    }

                    model.put( MARK_LISTE_REMP, referenceListElus );

                    int nIdElu = -1;

                    try
                    {
                        nIdElu = Integer.parseInt( request.getParameter( OdsParameters.ID_ELU ) );
                    }
                    catch ( NumberFormatException nfe )
                    {
                        AppLogService.error( nfe );
                    }

                    Elu elu = EluHome.findByPrimaryKey( nIdElu, getPlugin(  ) );
                    model.put( MARK_ELU, elu );

                    model.put( OdsParameters.ID_COMMISSION, -1 );

                    if ( elu.getCommission(  ) != null )
                    {
                        model.put( OdsParameters.ID_COMMISSION, elu.getCommission(  ).getIdCommission(  ) );
                    }

                    model.put( OdsParameters.ID_REMPLACANT, -1 );

                    if ( elu.getEluRemplace(  ) != null )
                    {
                        model.put( OdsParameters.ID_REMPLACANT, elu.getEluRemplace(  ).getIdElu(  ) );
                    }

                    template = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_ELU, getLocale(  ), model );

                    return getAdminPage( template.getHtml(  ) );
                }

                return getEluList( request );
            }
            catch ( NumberFormatException e )
            {
                AppLogService.error( e );

                return getEluList( request );
            }
        }
        else
        {
            template = AppTemplateService.getTemplate( OdsProperties.TEMPLATE_PROFIL_ERROR, getLocale(  ), model );

            return getAdminPage( template.getHtml(  ) );
        }
    }

    /**
     * Modifie un élu.
     * si tous les champs obligatoires du formulaire de modification d'un élu ont été renseigné,
     * la méthode retourne l'url de gestion des élus,
     * sinon l'url retournée est celle de l'interface spécifiant les messages d'erreurs.
     * @param request request
     * @return url url
     */
    public String doModificationElu( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( !requiredField( request ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                Object[] tabRequiredFields = { I18nService.getLocalizedString( requiredField( request ), getLocale(  ) ) };

                return AdminMessageService.getMessageUrl( request, OdsProperties.PROPERTY_MESSAGE_FIELDREQUIRED,
                    tabRequiredFields, AdminMessage.TYPE_STOP );
            }

            Elu elu;

            int nIdElu = -1;

            try
            {
                nIdElu = Integer.parseInt( request.getParameter( OdsParameters.ID_ELU ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            elu = EluHome.findByPrimaryKey( nIdElu, getPlugin(  ) );

            String strNom = request.getParameter( OdsParameters.NOM_ELU ).trim(  );
            String strPrenom = request.getParameter( OdsParameters.PRENOM_ELU ).trim(  );
            String strCivilite = request.getParameter( OdsParameters.CIVILITE ).trim(  );
            int nIdGroupe = -1;

            try
            {
                nIdGroupe = Integer.parseInt( request.getParameter( OdsParameters.ID_GROUPE ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            String strIdCommission = request.getParameter( OdsParameters.ID_COMMISSION );
            int nIdCommission = -1;
            Commission commission = null;

            if ( !strIdCommission.equals( null ) )
            {
                try
                {
                    nIdCommission = Integer.parseInt( strIdCommission );
                }
                catch ( NumberFormatException nfe )
                {
                    AppLogService.error( nfe );
                }
            }

            if ( nIdCommission != -1 )
            {
                commission = CommissionHome.findByPrimaryKey( nIdCommission, getPlugin(  ) );
            }

            String strIdRemplacant = request.getParameter( OdsParameters.ID_REMPLACANT );
            int nIdRemplacant = -1;
            Elu remplacant = null;

            if ( !strIdRemplacant.equals( null ) )
            {
                try
                {
                    nIdRemplacant = Integer.parseInt( strIdRemplacant );
                }
                catch ( NumberFormatException nfe )
                {
                    AppLogService.error( nfe );
                }
            }

            if ( nIdRemplacant != -1 )
            {
                remplacant = EluHome.findByPrimaryKey( nIdRemplacant, getPlugin(  ) );
            }

            String strActif = request.getParameter( OdsParameters.ACTIF );
            boolean bActif = true;

            if ( null == strActif )
            {
                bActif = false;
            }

            elu.setCivilite( strCivilite );
            elu.setPrenomElu( strPrenom );
            elu.setNomElu( strNom );
            elu.setGroupe( GroupePolitiqueHome.findByPrimaryKey( nIdGroupe, getPlugin(  ) ) );
            elu.setCommission( commission );
            elu.setEluRemplace( remplacant );
            elu.setActif( bActif );
            EluHome.update( elu, getPlugin(  ) );

            return getHomeUrl( request );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, OdsProperties.MESSAGE_PROFIL_NO_RIGHT,
                AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * Retourne une demande pour la suppression de l'élu choisi
     * @param request la requete HTTP
     * @return interface de confirmation de suppression
     */
    public String getSuppressionElu( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( request.getParameter( OdsParameters.ID_ELU ) == null )
            {
                return getHomeUrl( request );
            }

            setPageTitleProperty( PROPERTY_PAGE_TITLE_ELU );

            String strEluID = request.getParameter( OdsParameters.ID_ELU );
            UrlItem url = new UrlItem( JSP_URL_DO_SUPPRESSION_ELU_JSP );
            url.addParameter( OdsParameters.ID_ELU, strEluID );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRMDELETEELU, url.getUrl(  ),
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
    public String doSuppressionElu( HttpServletRequest request )
    {
        if ( RBACService.isAuthorized( AdminSaufOdjResourceIdService.ADMINISTRATION_SAUF_ODJ,
                    RBAC.WILDCARD_RESOURCES_ID, AdminSaufOdjResourceIdService.PERMISSION_GESTION, getUser(  ) ) )
        {
            if ( null != request.getParameter( OdsParameters.ID_ELU ) )
            {
                Elu elu;

                try
                {
                    elu = EluHome.findByPrimaryKey( Integer.parseInt( request.getParameter( OdsParameters.ID_ELU ) ),
                            getPlugin(  ) );
                }
                catch ( NumberFormatException ne )
                {
                    AppLogService.error( ne );

                    return getHomeUrl( request );
                }

                try
                {
                    EluHome.remove( elu, getPlugin(  ) );
                }
                catch ( AppException ae )
                {
                    AppLogService.error( ae );

                    if ( ae.getInitialException(  ) instanceof SQLException )
                    {
                        return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_ELU,
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
     * Retourne l'interface de gestion des élus
     * @param request la requete HTTP
     * @return   interface de gestion des élus
     */
    public String getEluList( HttpServletRequest request )
    {
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        // Création d'un objet Referencelist en vue de stocker l'ensemble des items
        // représentant les groupes politiques.
        ReferenceList referenceList = new ReferenceList(  );

        List<Elu> elus = null;
        int nIdGroupe = -1;

        // Si un choix de groupe politique a été fait,on retourne la liste des élus
        // appartenant au groupe politique choisi.
        // Dans le cas contraire on retourne la liste complete des élus        
        if ( null != request.getParameter( OdsParameters.ID_GROUPE ) )
        {
            try
            {
                nIdGroupe = Integer.parseInt( request.getParameter( OdsParameters.ID_GROUPE ) );
                elus = EluHome.findEluListbyIdGroupe( nIdGroupe, getPlugin(  ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }
        }

        if ( nIdGroupe == -1 )
        {
            elus = EluHome.findEluList( getPlugin(  ) );
        }

        List<GroupePolitique> listGroupePolitique = GroupePolitiqueHome.findGroupePolitiqueList( getPlugin(  ) );

        // Ajout dans la referenceList de l'item chaine vide
        referenceList.addItem( -1, OdsConstants.CONSTANTE_CHAINE_VIDE );

        // Ajout dans la referenceList de l'ensemble des items représentant
        // les groupes politiques.
        for ( GroupePolitique groupe : listGroupePolitique )
        {
            referenceList.addItem( groupe.getIdGroupe(  ), groupe.getNomGroupe(  ) );
        }

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        ajouterPermissionsDansHashmap( model );

        // On ajoute l'id du groupe politique sélectionné.
        // Si aucun groupe n'a été renseigné  c'est la valeur -1 qui est transmise.
        model.put( MARK_ID_GROUPE_SELECTED, nIdGroupe );
        model.put( MARK_LISTE_GROUPES, referenceList );
        model.put( MARK_LISTE_ELUS_COUNT, elus.size(  ) );
        model.put( MARK_LISTE_ELUS, elus );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LISTE_ELUS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Retourne le premier champ requis manquant s'il y'en a un.
     * @param request  la requete HTTP
     * @return boolean retourne true si l'ensemble des critéres obligatoires ont été renseigné
     */
    private String requiredField( HttpServletRequest request )
    {
        String strRequiredField = OdsConstants.CONSTANTE_CHAINE_VIDE;

        if ( request.getParameter( OdsParameters.CIVILITE ).equals( null ) ||
                request.getParameter( OdsParameters.CIVILITE ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            strRequiredField = FIELD_CIVILITE;
        }
        else if ( request.getParameter( OdsParameters.PRENOM_ELU ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            strRequiredField = FIELD_PRENOM_ELU;
        }
        else if ( request.getParameter( OdsParameters.NOM_ELU ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            strRequiredField = FIELD_NOM_ELU;
        }
        else if ( request.getParameter( OdsParameters.ID_GROUPE ).equals( null ) ||
                ( Integer.parseInt( request.getParameter( OdsParameters.ID_GROUPE ) ) == -1 ) )
        {
            strRequiredField = FIELD_ID_GROUPE;
        }

        return strRequiredField;
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
