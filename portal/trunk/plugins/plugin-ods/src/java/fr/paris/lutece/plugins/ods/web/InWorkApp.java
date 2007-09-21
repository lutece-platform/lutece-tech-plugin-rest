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

import fr.paris.lutece.plugins.ods.service.manager.LuteceUserManager;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * InWorkApp: page non créée pour l'instant
 */
public class InWorkApp implements XPageApplication
{
    private static final String TEMPLATE_IN_WORK = "skin/plugins/ods/in_work.html";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.in_work";
    private static final String PROPERTY_PAGE_TITLE = "ods.front.inwork.page.title";

    /**
     * Retourne la page d'avertissement de page en construction
     * @param request la requête Http
     * @param nMode le mode
     * @param plugin le plugin
     * @return la page d'avertissement
     * @throws UserNotSignedException si aucun utilisateur n'est connecté
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
        throws UserNotSignedException
    {
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        LuteceUserManager.gestionUtilisateur( model, request, plugin );

        XPage page = new XPage(  );
        Locale locale = request.getLocale(  );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_IN_WORK, request.getLocale(  ), model );
        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );
        page.setContent( template.getHtml(  ) );
        page.setTitle( strPageTitle );
        page.setPathLabel( strPagePathLabel );

        return page;
    }
}
