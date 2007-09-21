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
package fr.paris.lutece.plugins.ods.web;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.commission.CommissionHome;
import fr.paris.lutece.plugins.ods.business.documentseance.DocumentSeance;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierFilter;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocumentEnum;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilEnum;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourFilter;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravaux;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravauxFilter;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravauxHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementFilter;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementHome;
import fr.paris.lutece.plugins.ods.service.manager.LuteceUserManager;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * Affichage de l'interface Fiche de la séance archivée
 */
public class SeanceArchivesApp implements XPageApplication
{
    private static final String TEMPLATE_SEANCE_ARCHIVES = "skin/plugins/ods/seance_archives.html";
    private static final String MARK_LISTE_COMMISSION = "liste_commission";
    private static final String MARK_LISTE_FORMATION_CONSEIL = "liste_formation_conseil";
    private static final String MARK_LISTE_FORMATION_CONSEIL_VA = "liste_formation_conseil_va";
    private static final String MARK_LISTE_ORDRE_DU_JOUR_CONSEIL = "liste_ordre_du_jour_conseil";
    private static final String MARK_LISTE_ORDRE_DU_JOUR_REFERENCE = "liste_ordre_du_jour_reference";
    private static final String MARK_LISTE_ORDRE_DU_JOUR_COMMISSION = "liste_ordre_du_jour_commission";
    private static final String MARK_LISTE_AUTRES_DOCUMENTS_SEANCE = "liste_autres_documents_seance";
    private static final String MARK_LISTE_DOCUMENTS_APRES_SEANCE = "liste_documents_apres_seance";
    private static final String MARK_LISTE_DOCUMENT_SEANCE = "liste_document_seance";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.prochain_conseil";
    private static final String PROPERTY_PAGE_TITLE = "ods.front.prochainconseil.page.title";

    /**
     * Permet d'initialiser la liste des choix de type de conseil
     * @param plugin plugin
     * @return la liste des commissions
     */
    private ReferenceList initRefListCommission( Plugin plugin )
    {
        ReferenceList refListCommission = new ReferenceList(  );
        List<Commission> listCommission = CommissionHome.findAllCommissionsActives( plugin );

        //ajout dans la referenceList de l'item chaine vide
        //ajout dans la referenceList de l'ensemble des items représentant les groupes politiques
        for ( Commission commission : listCommission )
        {
            refListCommission.addItem( commission.getIdCommission(  ),
                OdsConstants.CONSTANTE_CHAINE_VIDE + commission.getNumero(  ) );
        }

        return refListCommission;
    }

    /**
     * @param request la requête HTTP
     * @param nMode le mode
     * @param plugin le plugin
     * @return XPage template
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        LuteceUserManager.gestionUtilisateur( model, request, plugin );

        //récupération de la séance archivée
        String strIdSeance = request.getParameter( OdsParameters.ID_SEANCE );
        int nIdSeance = -1;

        try
        {
            nIdSeance = Integer.parseInt( strIdSeance );
        }
        catch ( NumberFormatException ne )
        {
            AppLogService.error( ne );
        }

        Seance seance = SeanceHome.findByPrimaryKey( nIdSeance, plugin );
        ReferenceList refListCommission = initRefListCommission( plugin );
        XPage page = new XPage(  );
        Locale locale = request.getLocale(  );
        int nIdCommission = 1;
        List<FormationConseil> formationConseils = FormationConseilHome.findFormationConseilList( plugin );
        List<FormationConseil> formationConseilsVA = new ArrayList<FormationConseil>( formationConseils );
        List<OrdreDuJour> listeOrdreDuJourConseil = new ArrayList<OrdreDuJour>(  );
        List<OrdreDuJour> listeOrdreDuJourReference = new ArrayList<OrdreDuJour>(  );

        List<OrdreDuJour> listeOrdreDuJourCommission = new ArrayList<OrdreDuJour>(  );
        List<DocumentSeance> listeDocumentApresSeance = new ArrayList<DocumentSeance>(  );
        List<DocumentSeance> listeDocumentSeanceArchive = new ArrayList<DocumentSeance>(  );
        List<DocumentSeance> listeDocumentCommission = new ArrayList<DocumentSeance>(  );
        List<DocumentSeance> listeAutresDocumentsSeance = new ArrayList<DocumentSeance>(  );
        List<Fichier> listeFichier = new ArrayList<Fichier>(  );

        if ( ( seance != null ) && ( formationConseils != null ) && ( formationConseils.size(  ) > 1 ) )
        {
            int nI = 0;

            /***************************traitement pour les liasses des voeux et amendements***********************/
            //on recupere les liasses des voeux amendements de la seance archivée 
            //pour le conseil municipal et le conseil general 
            // on filtre sur le fait que:
            //								-les voeux amendements doivent etre publiés
            //								-la seance doit etre celle selectionnée 
            //								-la formation du conseil(conseil municipal et conseil general)
            VoeuAmendementFilter voeuAmendementFilter = new VoeuAmendementFilter(  );
            voeuAmendementFilter.setIdPublie( 1 );
            voeuAmendementFilter.setIdSeance( seance.getIdSeance(  ) );
            voeuAmendementFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILMUNICIPAL.getId(  ) );

            List<VoeuAmendement> vas = VoeuAmendementHome.findLiasseByFilter( voeuAmendementFilter, plugin );
            nI = 0;

            if ( ( vas == null ) || ( vas.size(  ) == 0 ) )
            {
                formationConseilsVA.remove( nI );
            }
            else
            {
                nI++;
            }

            //liasse des voeux amendements pour le conseil general
            voeuAmendementFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) );
            vas = VoeuAmendementHome.findLiasseByFilter( voeuAmendementFilter, plugin );

            if ( ( vas == null ) || ( vas.size(  ) == 0 ) )
            {
                formationConseilsVA.remove( nI );
            }
            else
            {
                nI++;
            }

            if ( null != request.getParameter( OdsParameters.ID_COMMISSION ) )
            {
                try
                {
                    nIdCommission = Integer.parseInt( request.getParameter( OdsParameters.ID_COMMISSION ) );
                }
                catch ( NumberFormatException ne )
                {
                    AppLogService.error( ne );
                }
            }

            /***************************traitement pour relevé des travaux***********************/
            ReleveDesTravaux releve = null;
            ReleveDesTravauxFilter rdtFilter = new ReleveDesTravauxFilter(  );

            // on recupere les relevé de travaux de la seance archivée 
            // pour la commission selectionnée 
            // on filtre sur le fait que:
            //								-les relevés doivent etre publiés
            //								-la seance doit etre celle selectionnée 
            //								-la commission selectionnée(commission 1 par defaut)
            if ( ( request.getParameter( OdsParameters.ID_COMMISSION ) == null ) ||
                    request.getParameter( OdsParameters.ID_COMMISSION ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
            {
                rdtFilter.setIdCommission( 1 );
            }
            else
            {
                rdtFilter.setIdCommission( Integer.parseInt( request.getParameter( OdsParameters.ID_COMMISSION ) ) );
            }

            rdtFilter.setIdPublie( 1 );
            rdtFilter.setIdSeance( seance.getIdSeance(  ) );

            List<ReleveDesTravaux> listReleves = ReleveDesTravauxHome.findReleveDesTravauxByFilter( rdtFilter, plugin );

            if ( ( listReleves != null ) && ( listReleves.size(  ) == 1 ) )
            {
                releve = listReleves.get( 0 );
                model.put( ReleveDesTravaux.MARK_RELEVE, releve );
            }

            /***************************traitement pour les ordres du jour du conseil***********************/
            //on recupere les ordres du jours  de la seance archivée 
            // on filtre sur le fait que:
            //								-la seance doit etre celle selectionnée 
            //								-les odjs doivent etre publiés
            //								-les odjs ne doivent pas etre une version de sauvegarde
            OrdreDuJourFilter ordreDuJourFilter = new OrdreDuJourFilter(  );
            ordreDuJourFilter.setIdSeance( seance.getIdSeance(  ) );
            ordreDuJourFilter.setIdPublie( 1 );
            ordreDuJourFilter.setIdSauvegarde( 0 );
            listeOrdreDuJourConseil = OrdreDuJourHome.findOrdreDuJourList( plugin, ordreDuJourFilter, true );

            //Ordre du jour  de la seance consultée 
            for ( OrdreDuJour odj : listeOrdreDuJourConseil )
            {
                DocumentSeance odjDocument = new DocumentSeance(  );
                odjDocument.setOrdreDuJour( odj );
                listeDocumentSeanceArchive.add( odjDocument );
            }

            //on recupere les ordres du jours  de references de la seance selectionnée(conseil municipal et conseil general)
            //pour le conseil municipal
            ordreDuJourFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILMUNICIPAL.getId(  ) );

            OrdreDuJour ordreDuJour = OrdreDuJourHome.findOdjReferenceByFilter( ordreDuJourFilter, plugin );

            if ( ordreDuJour != null )
            {
                listeOrdreDuJourReference.add( ordreDuJour );
            }

            //ordre du jour de reference
            //pour le conseil general
            ordreDuJourFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) );
            ordreDuJour = OrdreDuJourHome.findOdjReferenceByFilter( ordreDuJourFilter, plugin );

            if ( ordreDuJour != null )
            {
                listeOrdreDuJourReference.add( ordreDuJour );
            }

            //on recupere les  documents de type   ordre du jour  de la seance archivée 
            // on filtre sur le fait que:
            //								-la seance doit etre celle selectionnée 
            //								-les fichiers doivent etre de type odj
            //								-les fichiers ne doivent pas referencer une commission
            FichierFilter fichierFilter = new FichierFilter(  );
            fichierFilter.setIdSeance( seance.getIdSeance(  ) );
            fichierFilter.setIdTypeDocument( TypeDocumentEnum.ORDRE_DU_JOUR.getId(  ) );
            fichierFilter.setCommissionNul( 1 );
            listeFichier = FichierHome.findByFilter( fichierFilter, plugin );

            for ( Fichier fichier : listeFichier )
            {
                DocumentSeance odjDocument = new DocumentSeance(  );
                odjDocument.setFichier( fichier );
                listeDocumentSeanceArchive.add( odjDocument );
            }

            //on trie la liste par date de publication
            Collections.sort( listeDocumentSeanceArchive, new DocumentSeance(  ) );

            /***************************traitement pour les documents des commissions***********************/
            //on recupere les ordres du jours  des commissions  de la seance archivée 
            // on filtre sur le fait que:
            //								-la seance doit etre celle selectionnée 
            //								-les odjs doivent etre publiés
            //								-les odjs ne doivent pas etre une version de sauvegarde
            //								-les odjs doivent apartenir a la commission selectionnée		
            ordreDuJourFilter = new OrdreDuJourFilter(  );
            ordreDuJourFilter.setIdSeance( seance.getIdSeance(  ) );
            ordreDuJourFilter.setIdPublie( 1 );
            ordreDuJourFilter.setIdSauvegarde( 0 );
            ordreDuJourFilter.setIdCommission( nIdCommission );
            listeOrdreDuJourCommission = OrdreDuJourHome.findOrdreDuJourList( plugin, ordreDuJourFilter, true );

            for ( OrdreDuJour odj : listeOrdreDuJourCommission )
            {
                DocumentSeance odjDocument = new DocumentSeance(  );
                odjDocument.setOrdreDuJour( odj );
                listeDocumentCommission.add( odjDocument );
            }

            //ajout des fichiers de la seance selectionnée
            //dont le numéro de commission 
            //est la commission choisie. 
            fichierFilter = new FichierFilter(  );
            fichierFilter.setIdSeance( seance.getIdSeance(  ) );
            fichierFilter.setIdCommission( nIdCommission );
            listeFichier = FichierHome.findByFilter( fichierFilter, plugin );

            for ( Fichier fichier : listeFichier )
            {
                DocumentSeance odjDocument = new DocumentSeance(  );
                odjDocument.setFichier( fichier );
                listeDocumentCommission.add( odjDocument );
            }

            //on trie la liste par date de publication
            Collections.sort( listeDocumentCommission, new DocumentSeance(  ) );

            /***************************traitement les 'Autres documents de séance'***********************/
            FichierFilter filterAutreDocumentSeance = new FichierFilter(  );
            filterAutreDocumentSeance.setIdSeance( seance.getIdSeance(  ) );
            filterAutreDocumentSeance.setFilterTypeDocument( 2 );
            filterAutreDocumentSeance.setPublication( 1 );

            listeFichier = FichierHome.findByFilter( filterAutreDocumentSeance, plugin );

            for ( Fichier fichier : listeFichier )
            {
                DocumentSeance odjDocument = new DocumentSeance(  );
                odjDocument.setFichier( fichier );
                listeAutresDocumentsSeance.add( odjDocument );
            }

            Collections.sort( listeAutresDocumentsSeance, new DocumentSeance(  ) );

            /***************************traitement des documents de l’après séance ***********************/
            //traitement pour les  comptes-rendus de la séance.
            FichierFilter filterCompteRendu = new FichierFilter(  );
            filterCompteRendu.setIdSeance( seance.getIdSeance(  ) );
            filterCompteRendu.setIdTypeDocument( TypeDocumentEnum.COMPTE_RENDU.getId(  ) );
            filterCompteRendu.setPublication( 1 );
            listeFichier = FichierHome.findByFilter( filterCompteRendu, plugin );

            for ( Fichier fichier : listeFichier )
            {
                DocumentSeance compteRenduDocument = new DocumentSeance(  );
                compteRenduDocument.setFichier( fichier );
                listeDocumentApresSeance.add( compteRenduDocument );
            }

            //on trie la liste par date de publication
            Collections.sort( listeDocumentApresSeance, new DocumentSeance(  ) );
        }

        model.put( MARK_LISTE_DOCUMENTS_APRES_SEANCE, listeDocumentApresSeance );
        model.put( MARK_LISTE_DOCUMENT_SEANCE, listeDocumentSeanceArchive );
        model.put( MARK_LISTE_FORMATION_CONSEIL_VA, formationConseilsVA );
        model.put( MARK_LISTE_FORMATION_CONSEIL, formationConseils );
        model.put( MARK_LISTE_COMMISSION, refListCommission );
        model.put( MARK_LISTE_ORDRE_DU_JOUR_CONSEIL, listeOrdreDuJourConseil );
        model.put( MARK_LISTE_ORDRE_DU_JOUR_REFERENCE, listeOrdreDuJourReference );
        model.put( MARK_LISTE_ORDRE_DU_JOUR_COMMISSION, listeDocumentCommission );
        model.put( MARK_LISTE_AUTRES_DOCUMENTS_SEANCE, listeAutresDocumentsSeance );
        model.put( Seance.MARK_SEANCE, seance );
        model.put( OdsParameters.ID_COMMISSION, nIdCommission );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SEANCE_ARCHIVES, request.getLocale(  ), model );
        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );
        page.setTitle( strPageTitle );
        page.setContent( template.getHtml(  ) );
        page.setPathLabel( strPagePathLabel );

        return page;
    }
}
