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

import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.elu.EluHome;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitique;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitiqueHome;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ElementReleveDesTravaux;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ElementReleveDesTravauxHome;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravaux;
import fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravauxHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementHome;
import fr.paris.lutece.plugins.ods.service.manager.LuteceUserManager;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * ReleveDesTravauxCommApp
 */
public class ReleveDesTravauxCommApp implements XPageApplication
{
    private static final String CONSTANTE_RELEVE_INCORRECT = "releve_incorrect";
    private static final String TEMPLATE_RELEVE_TRAVAUX = "skin/plugins/ods/releve_travaux_comm.html";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.releve_travaux_comm";
    private static final String MARK_LISTE_VA = "liste_elmt_va";
    private static final String MARK_LISTE_PDD = "liste_elmt_pdd";
    private static final String MARK_ERROR = "error";

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

        // On instancie une nouvelle XPage
        XPage page = new XPage(  );

        // On récupère le relevé des travaux
        int nIdReleve = -1;
        ReleveDesTravaux releve = null;

        try
        {
            nIdReleve = Integer.parseInt( request.getParameter( OdsParameters.ID_RELEVE ) );
        }
        catch ( NumberFormatException nfe )
        {
            model.put( MARK_ERROR, CONSTANTE_RELEVE_INCORRECT );
        }

        if ( nIdReleve != -1 )
        {
            releve = ReleveDesTravauxHome.findByPrimaryKey( nIdReleve, plugin );
        }

        // Si le relevé n'est pas null,
        // alors on récupère la liste des élements de relevé de travaux.
        if ( releve == null )
        {
            model.put( MARK_ERROR, CONSTANTE_RELEVE_INCORRECT );
        }
        else if ( releve.isEnLigne(  ) )
        {
            List<ElementReleveDesTravaux> elementsVA = new ArrayList<ElementReleveDesTravaux>(  );
            List<ElementReleveDesTravaux> elementsPDD = new ArrayList<ElementReleveDesTravaux>(  );

            // Si l'élément est de type Voeu ou Amendement
            elementsVA = ElementReleveDesTravauxHome.findElementReleveDesTravauxByReleveVA( releve, plugin );

            if ( ( elementsVA != null ) && !elementsVA.isEmpty(  ) )
            {
                for ( ElementReleveDesTravaux element : elementsVA )
                {
                    VoeuAmendement va = null;

                    if ( element.getVoeuAmendement(  ) != null )
                    {
                        int nIdVA = element.getVoeuAmendement(  ).getIdVoeuAmendement(  );
                        va = VoeuAmendementHome.findByPrimaryKey( nIdVA, plugin );
                    }

                    if ( va != null )
                    {
                        element.setVoeuAmendement( va );
                    }
                }
            }

            // Si l'élément est de type PDD
            elementsPDD = ElementReleveDesTravauxHome.findElementReleveDesTravauxByRelevePDD( releve, plugin );

            if ( ( elementsPDD != null ) && !elementsPDD.isEmpty(  ) )
            {
                for ( ElementReleveDesTravaux element : elementsPDD )
                {
                    PDD pdd = null;
                    Elu elu = null;
                    GroupePolitique groupe = null;

                    if ( element.getProjetDeliberation(  ) != null )
                    {
                        int nIdPDD = element.getProjetDeliberation(  ).getIdPdd(  );
                        pdd = PDDHome.findByPrimaryKey( nIdPDD, plugin );
                    }

                    if ( element.getElu(  ) != null )
                    {
                        int nIdElu = element.getElu(  ).getIdElu(  );
                        elu = EluHome.findByPrimaryKey( nIdElu, plugin );
                    }

                    if ( element.getGroupe(  ) != null )
                    {
                        int nIdGroupe = element.getGroupe(  ).getIdGroupe(  );
                        groupe = GroupePolitiqueHome.findByPrimaryKey( nIdGroupe, plugin );
                    }

                    if ( pdd != null )
                    {
                        element.setProjetDeliberation( pdd );
                    }

                    if ( elu != null )
                    {
                        element.setElu( elu );
                    }

                    if ( groupe != null )
                    {
                        element.setGroupe( groupe );
                    }
                }
            }

            model.put( ReleveDesTravaux.MARK_RELEVE, releve );
            model.put( MARK_LISTE_VA, elementsVA );
            model.put( MARK_LISTE_PDD, elementsPDD );
        }

        // Chargement est affichage de la template
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RELEVE_TRAVAUX, request.getLocale(  ), model );
        String strPageTitle = I18nService.getLocalizedString( releve.getIntitule(  ), request.getLocale(  ) );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, request.getLocale(  ) );
        page.setContent( template.getHtml(  ) );

        // On affecte un titre et un chemin a la XPage
        page.setTitle( strPageTitle );
        page.setPathLabel( strPagePathLabel );

        return page;
    }
}
