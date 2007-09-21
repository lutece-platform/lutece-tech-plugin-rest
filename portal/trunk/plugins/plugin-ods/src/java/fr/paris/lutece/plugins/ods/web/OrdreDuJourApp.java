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

import fr.paris.lutece.plugins.ods.business.ordredujour.EntreeOrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.EntreeOrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourUtils;
import fr.paris.lutece.plugins.ods.business.ordredujour.TypeOrdreDuJourEnum;
import fr.paris.lutece.plugins.ods.business.ordredujour.generated.entete.Entete;
import fr.paris.lutece.plugins.ods.business.ordredujour.generated.pieddepage.PiedDePage;
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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * Interface d'affichage de l'ordre du jour
 */
public class OrdreDuJourApp implements XPageApplication
{
    private static final String TEMPLATE_ODJ = "skin/plugins/ods/ordre_du_jour.html";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.prochain_conseil";
    private static final String PROPERTY_PAGE_TITLE = "ods.front.prochainconseil.page.title";

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

        OrdreDuJour ordreDuJour = null;
        Locale locale = request.getLocale(  );

        if ( request.getParameter( OdsParameters.ID_ODJ ) != null )
        {
            int nIdOdj = -1;

            try
            {
                nIdOdj = Integer.parseInt( request.getParameter( OdsParameters.ID_ODJ ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
            }

            ordreDuJour = OrdreDuJourHome.findByPrimaryKey( nIdOdj, plugin );

            if ( ordreDuJour != null )
            {
                Seance seance = SeanceHome.findByPrimaryKey( ordreDuJour.getSeance(  ).getIdSeance(  ), plugin );
                ordreDuJour.setSeance( seance );

                // récupération de la liste des entrées associées à l'odj
                List<EntreeOrdreDuJour> listEntreeOrdreDuJour = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJour.getIdOrdreDuJour(  ),
                        plugin );

                // Les vœux et amendements rattachés à un PDD ne sont affichés que
                // si l’ordre du jour est définitif.
                // on recupere les voeux et amendements associés au pdd
                if ( ordreDuJour.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) == TypeOrdreDuJourEnum.DEFINITIF.getId(  ) )
                {
                    for ( EntreeOrdreDuJour entreeOrdreDuJour : listEntreeOrdreDuJour )
                    {
                        if ( entreeOrdreDuJour.getPdd(  ) != null )
                        {
                            entreeOrdreDuJour.setPdd( PDDHome.findByPrimaryKey( 
                                    entreeOrdreDuJour.getPdd(  ).getIdPdd(  ), plugin ) );
                        }
                    }
                }

                ordreDuJour.setListEntrees( listEntreeOrdreDuJour );
            }
        }

        XPage page = new XPage(  );
        model.put( OrdreDuJour.MARK_ODJ, ordreDuJour );

        //on recupere l'entete et pied de page de l'ordre du jour 
        Entete entete = OrdreDuJourUtils.getEntete( ordreDuJour, plugin );
        //On remplace dans l'entete d'ordre du jour la constante [date_seance] 
        //par la date du conseil 
        OrdreDuJourUtils.replaceDate( entete, ordreDuJour.getSeance(  ) );

        PiedDePage piedDePage = OrdreDuJourUtils.getPiedDePage( ordreDuJour, plugin );
        //On remplace dans le pied de page de d'ordre du jour la constante [date_seance] 
        //par la date du conseil 
        OrdreDuJourUtils.replaceDate( piedDePage, ordreDuJour.getSeance(  ) );
        model.put( OrdreDuJourUtils.TAG_ENTETE, entete );
        model.put( OrdreDuJourUtils.TAG_PIED_DE_PAGE, piedDePage );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ODJ, request.getLocale(  ), model );
        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );
        page.setTitle( strPageTitle );
        page.setContent( template.getHtml(  ) );
        page.setPathLabel( strPagePathLabel );

        return page;
    }
}
