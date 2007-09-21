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

import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.service.manager.LuteceUserManager;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * Interface d'affichage du calendrier des seances
 */
public class CalendrierApp implements XPageApplication
{
    private static final String TEMPLATE_CALENDRIER = "skin/plugins/ods/calendrier.html";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.calendrier";
    private static final String PROPERTY_PAGE_TITLE = "ods.front.calendrier.page.title";
    private static final String MARK_LISTE_SEANCES = "liste_seances";

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

        XPage page = new XPage(  );
        Locale locale = request.getLocale(  );

        //on recupere la liste des seances dont la date de fin est supérieure ou égale à la date du jour.
        List<Seance> listSeances = SeanceHome.findSeanceList( plugin );
        model.put( MARK_LISTE_SEANCES, listSeances );

        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CALENDRIER, locale, model );
        page.setTitle( strPageTitle );
        page.setPathLabel( strPagePathLabel );
        page.setContent( template.getHtml(  ) );

        return page;
    }
}
