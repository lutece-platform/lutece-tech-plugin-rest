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

import fr.paris.lutece.plugins.ods.business.fascicule.Fascicule;
import fr.paris.lutece.plugins.ods.business.fascicule.FasciculeHome;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierFilter;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocumentEnum;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.TypeOrdreDuJourEnum;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementFilter;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementHome;
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
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * VoeuxAmendementsApp
 */
public class VoeuxAmendementsApp implements XPageApplication
{
    private static final String TEMPLATE_LIASSE = "skin/plugins/ods/liasse.html";
    private static final String MARK_LISTE_VOEUAMENDEMENT = "liste_voeuamendement";
    private static final String MARK_LISTE_FASCICULE = "liste_fascicule";
    private static final String MARK_SEANCE = "seance";
    private static final String MARK_LIASSE_VA = "liasse";
    private static final String MARK_FORMATION_CONSEIL = "formation_conseil";
    private static final String PROPERTY_PAGE_TITLE = "ods.front.voeuxamendements.page.title";
    private static final String PROPERTY_PAGE_PATH_LABEL = "ods.pagePathLabel.voeux_amendements";

    /**
    * @param request la requ�te HTTP
    * @param nMode le mode
    * @param plugin le plugin
    * @return XPage template
    */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );

        // On met � jour la liste des utilisateurs et g�re la session de l'utilisateur
        LuteceUserManager.gestionUtilisateur( model, request, plugin );

        // Initialisation des param�tres de la requ�te
        String strIdFormationConseil = request.getParameter( OdsParameters.ID_FORMATION_CONSEIL );
        String strIdSeance = request.getParameter( OdsParameters.ID_SEANCE );
        int nIdSeance = -1;
        int nIdFormationConseil = -1;
        FormationConseil formationConseil = null;
        Seance seance = null;

        // Initialisation du filtre sur les VA
        VoeuAmendementFilter voeuAmendementFilter = new VoeuAmendementFilter(  );

        try
        {
            // On transforme l'identifiant de la formation conseil r�cup�r� en entier
            if ( strIdFormationConseil != null )
            {
                nIdFormationConseil = Integer.parseInt( strIdFormationConseil );
            }

            // Si on a bien r�cup�r� un identifiant non null pour la s�ance,
            // on le transforme en entier et on instancie avec une nouvelle s�ance
            if ( strIdSeance != null )
            {
                nIdSeance = Integer.parseInt( strIdSeance );
                seance = new Seance(  );
                seance.setIdSeance( nIdSeance );
            }

            // Sinon, par d�faut, la s�ance instanci�e est la prochaine s�ance
            else
            {
                seance = SeanceHome.getProchaineSeance( plugin );

                if ( seance != null )
                {
                    nIdSeance = seance.getIdSeance(  );
                }
            }
        }
        catch ( NumberFormatException ne )
        {
            AppLogService.error( ne );
        }

        // On filtre la liste des VA que l'on souhaite r�cup�rer suivants 4 crit�res :
        //   - la s�ance choisie ci-dessus
        //   - la formation conseil
        //   - le VA doit �tre publi�
        //   - Il doit �tre inscrit � l'ordre du jour d�finitif de la s�ance
        voeuAmendementFilter.setIdSeance( nIdSeance );
        voeuAmendementFilter.setIdFormationConseil( nIdFormationConseil );
        voeuAmendementFilter.setIdPublie( 1 );
        voeuAmendementFilter.setTypeOrdreDuJour( TypeOrdreDuJourEnum.DEFINITIF.getId(  ) );

        // La liste des VA que l'on souhaite r�cup�rer doit �tre class�e selon 
        // le code de fascicule et leur r�f�rence
        voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_CODE_FASCICULE );
        voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_REFERENCE_VA );

        // On r�cup�re la liste des VA correspondant
        List<VoeuAmendement> voeuAmendements = VoeuAmendementHome.findLiasseByFilter( voeuAmendementFilter, plugin );

        // On d�clare la liste des fascicules 
        List<Fascicule> fascicules = new ArrayList<Fascicule>(  );

        for ( VoeuAmendement voeuAmendement : voeuAmendements )
        {
            // si le fascicule du VA n'est pas d�j� dans la liste des fascicules, on l'ajoute.
            if ( !isAlreadyInListFascicule( voeuAmendement.getFascicule(  ).getIdFascicule(  ), fascicules ) )
            {
                //on insere le fascicule dans la liste des fascicules 
                fascicules.add( FasciculeHome.findByPrimaryKey( voeuAmendement.getFascicule(  ).getIdFascicule(  ),
                        plugin ) );
            }

            // On r�cup�re le texte initiale associ� au VA 
            voeuAmendement.setFichier( FichierHome.findByPrimaryKey( voeuAmendement.getFichier(  ).getId(  ), plugin ) );

            //on recupere la deliberation associ� au va si il en existe une 
            if ( voeuAmendement.getDeliberation(  ) != null )
            {
                voeuAmendement.setDeliberation( FichierHome.findByPrimaryKey( 
                        voeuAmendement.getDeliberation(  ).getId(  ), plugin ) );
            }
        }

        // On r�cup�re la formation du conseil via son identifiant r�cup�r� plus haut
        formationConseil = FormationConseilHome.findByPrimaryKey( nIdFormationConseil, plugin );

        // On filtre les fichiers afin de ne r�cup�rer que les les liasses des VAs de la s�ance 
        // donn�e et pour la formatin conseil donn�e.
        FichierFilter fichierFilter = new FichierFilter(  );
        fichierFilter.setIdSeance( nIdSeance );
        fichierFilter.setIdFormationConseil( nIdFormationConseil );
        fichierFilter.setIdTypeDocument( TypeDocumentEnum.LIASSE_DES_VA.getId(  ) );

        // On r�cup�re ainsi une liste de liasse des VAs
        List<Fichier> liasses = FichierHome.findByFilter( fichierFilter, plugin );

        // Si l'on est dans un �tat coh�rent, il n'y a qu'une seule liasse de VAs
        // par s�ance et par formation conseil
        if ( ( liasses != null ) && ( liasses.size(  ) > 0 ) )
        {
            // On ajoute la liasse en question dans la hashmap qui sera transmise � la template 
            model.put( MARK_LIASSE_VA, liasses.get( 0 ) );
        }

        // On instancie une XPage
        XPage page = new XPage(  );

        // On ajoute dans la hashmap la liste des VAs, la formation conseil, 
        // la liste des fascicules et la s�ance 
        model.put( MARK_LISTE_VOEUAMENDEMENT, voeuAmendements );
        model.put( MARK_FORMATION_CONSEIL, formationConseil );
        model.put( MARK_LISTE_FASCICULE, fascicules );
        model.put( MARK_SEANCE, seance );
        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );

        // On r�cup�re la liste des VA mis en ligne apr�s la date de derni�re connexion de l'utilisateur
        List<VoeuAmendement> listVa = OdsUtils.getNouveauxVAPublies( request, plugin );

        if ( listVa != null )
        {
            // Si la liste n'est pas nulle et n'est pas vide, on transmet cette liste dans la hashmap aussi
            model.put( OdsMarks.MARK_LISTE_NOUVEAUX_VAS, listVa );
        }

        // Chargement et affichage de la template
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LIASSE, request.getLocale(  ), model );
        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, request.getLocale(  ) );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, request.getLocale(  ) );
        page.setContent( template.getHtml(  ) );

        // On affecte un titre et un chemin � la XPage
        page.setTitle( strPageTitle );
        page.setPathLabel( strPagePathLabel );

        return page;
    }

    /**
     * Teste si le fascicule du voeuAmendement se trouve d�ja dans la liste des fascicules affich�s
     * @param idFascicule l'id du fascicule � tester
     * @param fascicules la liste des fascicules � afficher
     * @return true si le fascicule du voeuAmendement se trouve  dejadans  la liste des fascicules � afficher
     */
    private boolean isAlreadyInListFascicule( int idFascicule, List<Fascicule> fascicules )
    {
        for ( Fascicule fascicule : fascicules )
        {
            if ( fascicule.getIdFascicule(  ) == idFascicule )
            {
                return true;
            }
        }

        return false;
    }
}
