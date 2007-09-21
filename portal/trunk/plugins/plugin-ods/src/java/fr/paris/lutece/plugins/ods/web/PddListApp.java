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

import fr.paris.lutece.plugins.ods.business.direction.Direction;
import fr.paris.lutece.plugins.ods.business.direction.DirectionHome;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilHome;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.pdd.PDDFilter;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.service.manager.LuteceUserManager;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * PddListApp: a pour responsabilité d'afficher la liste des PDD
 */
public class PddListApp implements XPageApplication
{
    private static final String TEMPLATE_DELIBERATION = "skin/plugins/ods/deliberations.html";
    private static final String MARK_LISTE_PDDS = "liste_pdds";
    private static final String MARK_FORMATION_CONSEIL = "formation_conseil";
    private static final String MARK_FONCTIONNALITE_IS_PROJET = "fonctionnalite_is_projet";
    private static final String MARK_DELEGATION_SERVICE_PUBLIC = "delegation_service_public";
    private static final String MARK_DIRECTION = "direction";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.pdd_list";
    private static final String PROPERTY_PAGE_TITLE = "ods.front.pddlist.page.title";

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

        //récupération des parametres de la requette
        String strIdFormationConseil = request.getParameter( OdsParameters.ID_FORMATION_CONSEIL );
        String strIdSeance = request.getParameter( OdsParameters.ID_SEANCE );
        String strIdDirection = request.getParameter( OdsParameters.ID_DIRECTION );
        int nIdDirection = -1;
        String strType = request.getParameter( OdsParameters.TYPE );
        String strDelegationService = request.getParameter( OdsParameters.PDD_DELEG_SERVICE_PUBLIC );

        List<PDD> pdds = null;
        List<PDD> pddRelieOdj = null;
        boolean bFonctionnaliteProjetDeDeliberation = false;
        boolean bDelegationServicePublic = false;
        FormationConseil formationConseil = null;
        Direction direction = null;
        Seance seance = null;
        PDDFilter pddFilter = new PDDFilter(  );
        int nIdInscritOdj = -1;

        try
        {
            /****************************On recupere la liste des pdds en fonction
                                                                                            des criteres de recherches selectionnés*********************/

            //on ne recupere que les pdds publiés
            pddFilter.setPublication( 1 );

            //on filtre les pdds sur la formation conseil
            if ( strIdFormationConseil != null )
            {
                pddFilter.setIdFormationConseil( Integer.parseInt( strIdFormationConseil ) );
                formationConseil = FormationConseilHome.findByPrimaryKey( Integer.parseInt( strIdFormationConseil ),
                        plugin );
            }

            //on recupere la seance du jour
            Seance prochaineSeance = SeanceHome.getProchaineSeance( plugin );

            if ( ( strIdSeance == null ) ||
                    ( ( prochaineSeance != null ) ||
                    strIdSeance.trim(  ).equals( "" + prochaineSeance.getIdSeance(  ) ) ) )
            {
                //on filtre les pdds sur la seance selectionnée
                pddFilter.setProchaineSeance( prochaineSeance.getIdSeance(  ) );
                seance = prochaineSeance;
            }
            else
            {
                int nIdSeance = Integer.parseInt( strIdSeance );
                pddFilter.setSeance( nIdSeance );
                seance = SeanceHome.findByPrimaryKey( nIdSeance, plugin );
            }

            //si les pdds doivent appartenir a une direction  
            //on recupere la direction selectionnée 
            if ( strIdDirection != null )
            {
                nIdDirection = Integer.parseInt( strIdDirection );
                direction = DirectionHome.findByPrimaryKey( nIdDirection, plugin );
            }
        }
        catch ( NumberFormatException ne )
        {
            AppLogService.error( ne );
        }

        //on filtre sur la delegation de service public(si le filtre sur les directions est demandé)  
        if ( strDelegationService != null )
        {
            pddFilter.setDelegationService( 1 );
            bDelegationServicePublic = true;
        }

        //on filtre sur les types de pdds (proposition ou projet)
        if ( ( strType != null ) && strType.equals( PDD.CONSTANTE_TYPE_PROP ) )
        {
            pddFilter.setType( PDD.CONSTANTE_TYPE_PROP );
            bFonctionnaliteProjetDeDeliberation = false;
        }
        else
        {
            pddFilter.setType( PDD.CONSTANTE_TYPE_PROJET );
            bFonctionnaliteProjetDeDeliberation = true;
        }

        //si les pdds ne sont pas filtrés sur leurs appartenances a un ordre du jour de reference
        //on recupere la liste des pdds de la prochaine ainsi que la liste des pdds inscrits 
        //à l'ordre du jour   
        if ( ( request.getParameter( OdsParameters.ID_PDD_INSCRIT_ODJ ) == null ) ||
                request.getParameter( OdsParameters.ID_PDD_INSCRIT_ODJ ).trim(  ).equals( "-1" ) )
        {
            //si les pdds doivent appartenir a une direction  
            //on filtre les pdds sur la direction selectionnée 
            if ( strIdDirection != null )
            {
                pdds = PDDHome.findByFilterByDirectionOrDirectionCoemetrice( pddFilter, plugin, nIdDirection );
            }
            else
            {
                pdds = PDDHome.findByFilter( pddFilter, plugin );
            }

            // On récupère la liste des Pdds inscrits à l'ordre du jour de référence
            pddRelieOdj = OdsUtils.getPddsInscritsOdj( seance, request, plugin );
        }
        else
        {
            //les pdds doivent etre inscrit  a l'ordre du jour ou ne pas etre inscrit a l'ordre du jour
            try
            {
                nIdInscritOdj = Integer.parseInt( request.getParameter( OdsParameters.ID_PDD_INSCRIT_ODJ ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
            }

            if ( nIdInscritOdj == 0 )
            {
                //les pdds ne doivent pas etre inscrits à un ordre du jour de reference
                pdds = OdsUtils.getPddsNotInscritsOdj( seance, request, plugin );
            }

            if ( nIdInscritOdj == 1 )
            {
                //les pdds doivent etre inscrits à un ordre du jour de reference
                pdds = OdsUtils.getPddsInscritsOdj( seance, request, plugin );
                pddRelieOdj = pdds;
            }
        }

        XPage page = new XPage(  );
        model.put( MARK_LISTE_PDDS, pdds );

        // On récupère la liste des PDD mis en ligne après la date de dernière connexion de l'utilisateur
        List<PDD> listPdd = OdsUtils.getNouveauxPddsPublies( request, plugin );

        if ( listPdd != null )
        {
            model.put( OdsMarks.MARK_LISTE_NOUVEAUX_PDDS, listPdd );
        }

        if ( pddRelieOdj != null )
        {
            model.put( OdsMarks.MARK_LISTE_PDD_RATTACHE_ODJ, pddRelieOdj );
        }

        model.put( OdsParameters.ID_PDD_INSCRIT_ODJ, nIdInscritOdj );
        model.put( MARK_FORMATION_CONSEIL, formationConseil );
        model.put( MARK_DIRECTION, direction );
        model.put( MARK_FONCTIONNALITE_IS_PROJET, bFonctionnaliteProjetDeDeliberation );
        model.put( MARK_DELEGATION_SERVICE_PUBLIC, bDelegationServicePublic );
        model.put( Seance.MARK_SEANCE, seance );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DELIBERATION, request.getLocale(  ), model );
        page.setContent( template.getHtml(  ) );

        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, request.getLocale(  ) );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, request.getLocale(  ) );
        page.setContent( template.getHtml(  ) );
        page.setTitle( strPageTitle );
        page.setPathLabel( strPagePathLabel );

        return page;
    }
}
