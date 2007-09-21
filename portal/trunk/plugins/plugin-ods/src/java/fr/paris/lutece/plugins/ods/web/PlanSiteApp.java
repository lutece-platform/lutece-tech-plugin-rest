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

import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilHome;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.pdd.PDDFilter;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
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
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * Affichage de l'interface prochain conseil
 */
public class PlanSiteApp implements XPageApplication
{
    private static final String TEMPLATE_PROCHAIN_CONSEIL = "skin/plugins/ods/plan_site.html";
    private static final String MARK_LISTE_FORMATION_CONSEIL = "liste_formation_conseil";
    private static final String MARK_LISTE_FORMATION_CONSEIL_PJ = "liste_formation_conseil_pj";
    private static final String MARK_LISTE_FORMATION_CONSEIL_PP = "liste_formation_conseil_pp";
    private static final String MARK_LISTE_FORMATION_CONSEIL_VA = "liste_formation_conseil_va";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.prochain_conseil";
    private static final String PROPERTY_PAGE_TITLE = "ods.front.plansite.page.title";

    /**
     * @param request la requête HTTP
     * @param nMode le mode
     * @param plugin le plugin
     * @return XPage template
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        // On met à jour la liste des utilisateurs et gère la session de l'utilisateur
        LuteceUserManager.gestionUtilisateur( model, request, plugin );

        // Récupération de la prochaine séance
        Seance seance = SeanceHome.getProchaineSeance( plugin );

        // On instancie une nouvelle XPage
        XPage page = new XPage(  );
        Locale locale = request.getLocale(  );

        // Initialisation des paramètres
        int nIdCommission = 1;
        List<FormationConseil> formationConseils = FormationConseilHome.findFormationConseilList( plugin );
        List<FormationConseil> formationConseilsPP = new ArrayList<FormationConseil>( formationConseils );
        List<FormationConseil> formationConseilsPJ = new ArrayList<FormationConseil>( formationConseils );
        List<FormationConseil> formationConseilsVA = new ArrayList<FormationConseil>( formationConseils );

        if ( ( seance != null ) && ( formationConseils != null ) && ( formationConseils.size(  ) >= 2 ) )
        {
            // Projet
            PDDFilter pddFilter = new PDDFilter(  );
            pddFilter.setType( PDD.CONSTANTE_TYPE_PROJET );
            pddFilter.setPublication( 1 );
            pddFilter.setIdFormationConseil( formationConseils.get( 0 ).getIdFormationConseil(  ) );

            List<PDD> pdds = PDDHome.findByFilter( pddFilter, plugin );

            int nI = 0;

            if ( ( pdds == null ) || ( pdds.size(  ) == 0 ) )
            {
                formationConseilsPJ.remove( nI );
            }
            else
            {
                nI++;
            }

            pddFilter = new PDDFilter(  );
            pddFilter.setIdFormationConseil( formationConseils.get( 1 ).getIdFormationConseil(  ) );
            pddFilter.setPublication( 1 );
            pddFilter.setType( PDD.CONSTANTE_TYPE_PROJET );
            pdds = PDDHome.findByFilter( pddFilter, plugin );

            if ( ( pdds == null ) || ( pdds.size(  ) == 0 ) )
            {
                formationConseilsPJ.remove( nI );
            }
            else
            {
                nI++;
            }

            // Proposition
            pddFilter = new PDDFilter(  );
            pddFilter.setType( PDD.CONSTANTE_TYPE_PROP );
            pddFilter.setPublication( 1 );
            pddFilter.setIdFormationConseil( formationConseils.get( 0 ).getIdFormationConseil(  ) );
            pdds = PDDHome.findByFilter( pddFilter, plugin );
            nI = 0;

            if ( ( pdds == null ) || ( pdds.size(  ) == 0 ) )
            {
                formationConseilsPP.remove( nI );
            }
            else
            {
                nI++;
            }

            pddFilter = new PDDFilter(  );
            pddFilter.setIdFormationConseil( formationConseils.get( 1 ).getIdFormationConseil(  ) );
            pddFilter.setPublication( 1 );
            pddFilter.setType( PDD.CONSTANTE_TYPE_PROP );
            pdds = PDDHome.findByFilter( pddFilter, plugin );

            if ( ( pdds == null ) || ( pdds.size(  ) == 0 ) )
            {
                formationConseilsPP.remove( nI );
            }
            else
            {
                nI++;
            }

            // VoeuAmendement
            VoeuAmendementFilter voeuAmendementFilter = new VoeuAmendementFilter(  );
            voeuAmendementFilter.setIdPublie( 1 );
            voeuAmendementFilter.setIdSeance( seance.getIdSeance(  ) );

            voeuAmendementFilter.setIdFormationConseil( formationConseils.get( 0 ).getIdFormationConseil(  ) );

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

            voeuAmendementFilter.setIdFormationConseil( formationConseils.get( 1 ).getIdFormationConseil(  ) );
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

            // Traitement pour relevé des travaux
            ReleveDesTravaux releve = null;
            ReleveDesTravauxFilter rdtFilter = new ReleveDesTravauxFilter(  );

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
        }

        model.put( MARK_LISTE_FORMATION_CONSEIL, formationConseils );
        model.put( MARK_LISTE_FORMATION_CONSEIL_PJ, formationConseilsPJ );
        model.put( MARK_LISTE_FORMATION_CONSEIL_PP, formationConseilsPP );
        model.put( MARK_LISTE_FORMATION_CONSEIL_VA, formationConseilsVA );
        model.put( MARK_LISTE_FORMATION_CONSEIL, formationConseils );
        model.put( Seance.MARK_SEANCE, seance );
        model.put( OdsParameters.ID_COMMISSION, nIdCommission );

        // Chargement et affichage de la template
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PROCHAIN_CONSEIL, request.getLocale(  ), model );
        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );
        page.setContent( template.getHtml(  ) );

        // On affecte un titre et un chemin à la XPage
        page.setTitle( strPageTitle );
        page.setPathLabel( strPagePathLabel );

        return page;
    }
}
