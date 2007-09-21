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

import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierFilter;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocumentEnum;
import fr.paris.lutece.plugins.ods.business.historique.Historique;
import fr.paris.lutece.plugins.ods.business.historique.HistoriqueHome;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.statut.StatutEnum;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.service.manager.LuteceUserManager;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * PddApp: a pour responsabilité d'afficher la fiche d'un PDD
 */
public class PddApp implements XPageApplication
{
    private static final String TEMPLATE_PDD = "skin/plugins/ods/pdd.html";
    private static final String MARK_LISTE_HISTORIQUES = "liste_historiques";
    private static final String MARK_LISTE_DOCUMENTS = "liste_documents";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.pdd";
    private static final String PROPERTY_PAGE_TITLE_PJ = "ods.front.pdd.projetdedeliberation.title";
    private static final String PROPERTY_PAGE_TITLE_PP = "ods.front.pdd.propositiondedeliberation.title";
    private static final String MARK_LISTE_IS_ARCHIVE = "bIsArchive";
    private static final String MARK_FICHIER_DELIBERATION = "fichier_deliberation";
    private static final String MARK_EST_ADOPTE = "est_adopte";

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

        PDD pdd = null;
        List<Historique> historiques = null;
        List<Fichier> documents = new ArrayList<Fichier>(  );
        Seance seance = null;

        String strIdPDD = request.getParameter( OdsParameters.ID_PDD );

        if ( strIdPDD != null )
        {
            try
            {
                int nIdPDD = Integer.parseInt( strIdPDD );
                pdd = PDDHome.findByPrimaryKey( nIdPDD, plugin );

                if ( pdd != null )
                {
                    seance = SeanceHome.findSeanceByPdd( pdd.getIdPdd(  ), plugin );

                    // Si la séance du PDD ne correspond pas à la prochaine séance 
                    // on ajoute un marqueur pour signaler à la template que c'est un PDD archivé
                    // et si c'est un PDD archivé on ajoute le fichier de délibération
                    Seance prochaineSeance = SeanceHome.getProchaineSeance( plugin );

                    if ( ( prochaineSeance == null ) || ( seance.getIdSeance(  ) != prochaineSeance.getIdSeance(  ) ) )
                    {
                        model.put( MARK_LISTE_IS_ARCHIVE, Boolean.TRUE );

                        // Récupération du fichier de délibération
                        FichierFilter fichierDeliberationFilter = new FichierFilter(  );
                        fichierDeliberationFilter.setPDD( pdd.getIdPdd(  ) );
                        fichierDeliberationFilter.setIdTypeDocument( TypeDocumentEnum.DELIBERATION.getId(  ) );

                        List<Fichier> listFichiersDeliberation = FichierHome.findByFilter( fichierDeliberationFilter,
                                plugin );
                        Fichier fichierDeliberation = null;

                        if ( ( listFichiersDeliberation != null ) && ( listFichiersDeliberation.size(  ) >= 1 ) )
                        {
                            fichierDeliberation = listFichiersDeliberation.get( 0 );
                        }

                        model.put( MARK_FICHIER_DELIBERATION, fichierDeliberation );

                        if ( ( pdd.getStatut(  ) != null ) &&
                                ( ( pdd.getStatut(  ).getIdStatut(  ) == StatutEnum.ADOPTE.getId(  ) ) ||
                                ( pdd.getStatut(  ).getIdStatut(  ) == StatutEnum.AMENDE.getId(  ) ) ) )
                        {
                            model.put( MARK_EST_ADOPTE, true );
                        }
                    }
                    else
                    {
                        model.put( MARK_LISTE_IS_ARCHIVE, Boolean.FALSE );
                    }

                    // Recuperation de l'historique
                    historiques = HistoriqueHome.getHistoriqueList( pdd, plugin );
                    Collections.reverse( historiques );

                    // Recuperation des documents
                    FichierFilter fichierFilter = new FichierFilter(  );
                    fichierFilter.setPDD( nIdPDD );

                    List<Fichier> fichiers = FichierHome.findByFilter( fichierFilter, plugin );
                    Fichier fichierExposeDesMotifs = null;
                    Fichier fichierProjetDeDelibere = null;
                    List<Fichier> piecesAnnexes = new ArrayList<Fichier>(  );

                    for ( int i = 0; ( fichiers != null ) && ( i < fichiers.size(  ) ); i++ )
                    {
                        Fichier f = fichiers.get( i );

                        if ( ( f.getTypdeDocument(  ) != null ) &&
                                ( TypeDocumentEnum.EXPOSE_DES_MOTIFS.getId(  ) == f.getTypdeDocument(  ).getId(  ) ) )
                        {
                            fichierExposeDesMotifs = f;
                        }

                        if ( ( f.getTypdeDocument(  ) != null ) &&
                                ( TypeDocumentEnum.PROJET_DE_DELIBERE.getId(  ) == f.getTypdeDocument(  ).getId(  ) ) )
                        {
                            fichierProjetDeDelibere = f;
                        }

                        if ( ( f.getTypdeDocument(  ) != null ) &&
                                ( TypeDocumentEnum.PIECE_ANNEXE.getId(  ) == f.getTypdeDocument(  ).getId(  ) ) )
                        {
                            piecesAnnexes.add( f );
                        }
                    }

                    documents.add( fichierExposeDesMotifs );
                    documents.add( fichierProjetDeDelibere );
                    documents.addAll( piecesAnnexes );
                }
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }
        }

        XPage page = new XPage(  );

        model.put( PDD.MARK_PDD, pdd );
        model.put( MARK_LISTE_HISTORIQUES, historiques );
        model.put( MARK_LISTE_DOCUMENTS, documents );
        model.put( Seance.MARK_SEANCE, seance );

        // On récupère la liste des PDD mis en ligne après la date de dernière connexion de l'utilisateur
        List<PDD> listPdd = OdsUtils.getNouveauxPddsPublies( request, plugin );

        if ( listPdd != null )
        {
            model.put( OdsMarks.MARK_LISTE_NOUVEAUX_PDDS, listPdd );
        }

        // On récupère la liste des VA mis en ligne après la date de dernière connexion de l'utilisateur
        List<VoeuAmendement> listVa = OdsUtils.getNouveauxVAPublies( request, plugin );

        if ( listVa != null )
        {
            model.put( OdsMarks.MARK_LISTE_NOUVEAUX_VAS, listVa );
        }

        // On récupère la liste des Fichiers mis en ligne après la date de dernière connexion de l'utilisateur
        List<Fichier> listFichiers = OdsUtils.getNouveauxFichiersPublies( request, plugin );

        if ( listFichiers != null )
        {
            model.put( OdsMarks.MARK_LISTE_NOUVEAUX_FICHIERS, listFichiers );
        }

        List<PDD> pddRelieOdj = OdsUtils.getPddsInscritsOdj( seance, request, plugin );

        if ( pddRelieOdj != null )
        {
            model.put( OdsMarks.MARK_LISTE_PDD_RATTACHE_ODJ, pddRelieOdj );
        }

        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PDD, request.getLocale(  ), model );
        String strPageTitle;

        if ( pdd.getTypePdd(  ).equals( PDD.CONSTANTE_TYPE_PROJET ) )
        {
            strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE_PJ, request.getLocale(  ) );
        }
        else
        {
            strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE_PP, request.getLocale(  ) );
        }

        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, request.getLocale(  ) );
        page.setTitle( strPageTitle );
        page.setPathLabel( strPagePathLabel );
        page.setContent( template.getHtml(  ) );

        return page;
    }
}
