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

import javax.servlet.http.HttpServletRequest;


/**
 * affiche la liste des directions et pour chaque direction le nombre de projets associés
 */
public class DirectionListeApp implements XPageApplication
{
    private static final int CONSTANTE_INDEX_NOMBRE_PDD = 1;
    private static final int CONSTANTE_INDEX_DIRECTION = 0;
    private static final String TEMPLATE_DIRECTION_LIST = "skin/plugins/ods/direction_list.html";
    private static final String MARK_LISTE_COUPLE_DIRECTION_NB_PDD = "liste_couple_direction_nb_pdd";
    private static final String MARK_DSP_NB_PDD = "dsp_nb_pdd";
    private static final String MARK_FORMATION_CONSEIL = "formation_conseil";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.direction_list";
    private static final String PROPERTY_PAGE_TITLE = "ods.front.directionlist.page.title";

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

        String strIdFormationConseil = request.getParameter( OdsParameters.ID_FORMATION_CONSEIL );
        String strIdSeance = request.getParameter( OdsParameters.ID_SEANCE );

        FormationConseil formationConseil = null;
        Seance seance = null;
        PDDFilter pddFilter = new PDDFilter(  );

        try
        {
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
        }
        catch ( NumberFormatException ne )
        {
            AppLogService.error( ne );
        }

        //les pdds sont de type projet de deliberation et ont comme statut publie
        pddFilter.setType( PDD.CONSTANTE_TYPE_PROJET );
        pddFilter.setPublication( 1 );

        //chaque direction est affichée avec le nombre de projets associé
        //on stock le couple ( direction  ,nombre de projet) dans une ArrayList
        //Liste de couple ( direction  ,nombre de projet)
        ArrayList<Object[]> listeCoupleDirectionNbPdd = new ArrayList<Object[]>(  );

        //recupération de la  liste des directions ainsi que le nombre de projets associé à chaque direction
        List<Direction> directions;
        directions = DirectionHome.findAllDirectionsActives( plugin );

        for ( Direction direction : directions )
        {
            Object[] coupleDirectionNbPdd = new Object[2];
            coupleDirectionNbPdd[CONSTANTE_INDEX_DIRECTION] = direction;
            coupleDirectionNbPdd[CONSTANTE_INDEX_NOMBRE_PDD] = PDDHome.findByFilterByDirectionOrDirectionCoemetrice( pddFilter,
                    plugin, direction.getIdDirection(  ) ).size(  );
            listeCoupleDirectionNbPdd.add( coupleDirectionNbPdd );
        }

        //on recupere le nombre de projets associés à la  délégation de services publics 
        pddFilter.setDelegationService( 1 );

        List<PDD> pdds = new ArrayList<PDD>(  );
        pdds = PDDHome.findByFilter( pddFilter, plugin );

        model.put( Seance.MARK_SEANCE, seance );
        model.put( MARK_FORMATION_CONSEIL, formationConseil );
        model.put( MARK_DSP_NB_PDD, pdds.size(  ) );
        model.put( MARK_LISTE_COUPLE_DIRECTION_NB_PDD, listeCoupleDirectionNbPdd );

        XPage page = new XPage(  );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DIRECTION_LIST, request.getLocale(  ), model );
        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, request.getLocale(  ) );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, request.getLocale(  ) );
        page.setContent( template.getHtml(  ) );
        page.setTitle( strPageTitle );
        page.setPathLabel( strPagePathLabel );

        return page;
    }
}
