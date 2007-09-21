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
package fr.paris.lutece.plugins.ods.web.tourniquet;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.commission.CommissionHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourFilter;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.tourniquet.Tourniquet;
import fr.paris.lutece.plugins.ods.business.tourniquet.TourniquetHome;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.ordredujour.OrdreDuJourJspBean;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * TourniquetJspBean
 */
public class TourniquetJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_ODS_TOURNIQUET = "ODS_TOURNIQUET";
    private static final String TEMPLATE_TOURNIQUET = "admin/plugins/ods/tourniquet/tourniquet.html";
    private static final String TEMPLATE_NO_REFERENTIEL = "admin/plugins/ods/tourniquet/no_ref.html";
    private static final String MARK_TOURNIQUET = "liste_commission";
    private static final String MARK_SIZE = "list_size";
    private static final String MARK_ID_COM = "id_com";
    private static final String MARK_ERROR = "error";
    private static final String NO_ACTION = "none";
    private static final String URL_TOURNIQUET = "Tourniquet.jsp";
    private static final String URL_DO_TOURNIQUET = "DoTourniquet.jsp";
    private static final String CONSTANTE_NO_COMMISSSION = "nocomm";
    private static final String CONSTANTE_NO_SEANCE = "noseance";
    private static final String ACTION_SAVE = "save";
    private List<Tourniquet> _tourniquet;
    private Map<Object, Object> _model = new HashMap<Object, Object>(  );

    /**
     * Initialise l'objet _tourniquet à partir de la base
     * @param request la requête http
     */
    public void initTourniquet( HttpServletRequest request )
    {
        _model.put( MARK_ID_COM, -1 );
        _tourniquet = TourniquetHome.listTourniquet( getPlugin(  ) );

        if ( _tourniquet.isEmpty(  ) )
        {
            List<Commission> listCom = CommissionHome.findCommissionList( getPlugin(  ) );
            TourniquetHome.initTourniquetFromCommission( listCom, getPlugin(  ) );
            _tourniquet = TourniquetHome.listTourniquet( getPlugin(  ) );
        }
    }

    /**
     * Affiche l'interface de gestion du tourniquet
     * @param request la requête http
     * @return l'interface de gestion du tourniquet
     */
    public String getTourniquet( HttpServletRequest request )
    {
        setPageTitleProperty( OdsConstants.CONSTANTE_CHAINE_VIDE );

        if ( SeanceHome.getProchaineSeance( getPlugin(  ) ) == null )
        {
            _model.put( MARK_ERROR, CONSTANTE_NO_SEANCE );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_NO_REFERENTIEL, getLocale(  ), _model );

            return getAdminPage( template.getHtml(  ) );
        }

        if ( ( _tourniquet == null ) || _tourniquet.isEmpty(  ) )
        {
            _model.put( MARK_ERROR, CONSTANTE_NO_COMMISSSION );

            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_NO_REFERENTIEL, getLocale(  ), _model );

            return getAdminPage( template.getHtml(  ) );
        }

        _model.put( MARK_TOURNIQUET, _tourniquet );
        _model.put( MARK_SIZE, _tourniquet.size(  ) );
        _model.put( OdsParameters.A_FAIRE, NO_ACTION );

        if ( request.getParameter( OdsParameters.A_FAIRE ) != null )
        {
            _model.put( OdsParameters.A_FAIRE, ACTION_SAVE );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TOURNIQUET, getLocale(  ), _model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Enregistre les modifications du tourniquet dans la base.
     * @param request la requête http
     * @return l'interface de gestion du tourniquet
     */
    public String doModificationTourniquet( HttpServletRequest request )
    {
        Plugin plugin = getPlugin(  );
        TourniquetHome.initTourniquet( _tourniquet, plugin );
        _tourniquet = TourniquetHome.listTourniquet( plugin );

        //on récupère les ordres du jour non publiés de la prochaine seance 
        //dépendants du tourniquet  
        Seance seance = SeanceHome.getProchaineSeance( plugin );

        if ( seance != null )
        {
            OrdreDuJourFilter ordreDuJourFilter = new OrdreDuJourFilter(  );
            ordreDuJourFilter.setIdTourniquet( 1 );
            ordreDuJourFilter.setIdPublie( 0 );
            ordreDuJourFilter.setIdSeance( seance.getIdSeance(  ) );

            List<OrdreDuJour> listOrdreDuJours = OrdreDuJourHome.findOrdreDuJourList( plugin, ordreDuJourFilter, false );

            for ( OrdreDuJour ordreDuJour : listOrdreDuJours )
            {
                OrdreDuJourJspBean.classeAllEntrees( ordreDuJour, plugin );
            }
        }

        _model.put( MARK_ID_COM, -1 );

        UrlItem url = new UrlItem( URL_DO_TOURNIQUET );
        url.addParameter( OdsParameters.A_FAIRE, "save" );

        return url.getUrl(  );
    }

    /**
     * Reclasse le tourniquet à partir des informations de la base
     * @param request la requête http
     * @return l'interface de gestion du tourniquet
     */
    public String doAnnulationModification( HttpServletRequest request )
    {
        _tourniquet = TourniquetHome.listTourniquet( getPlugin(  ) );
        _model.put( MARK_ID_COM, -1 );

        UrlItem url = new UrlItem( URL_DO_TOURNIQUET );

        return url.getUrl(  );
    }

    /**
     * Synchronise la liste des commissions à partir de la liste des commissions
     * @param request la requête http
     * @return l'interface de gestion du tourniquet
     */
    public String doSynchronisationTourniquet( HttpServletRequest request )
    {
        TourniquetHome.removeAll( getPlugin(  ) );
        _tourniquet = null;

        UrlItem url = new UrlItem( URL_TOURNIQUET );

        return url.getUrl(  );
    }

    /**
     * Déplace un élément dans la liste; N'enregistre pas les modifications
     * @param request la requête http
     * @return l'interface de gestion du tourniquet
     */
    public String doMoveTourniquet( HttpServletRequest request )
    {
        if ( request.getParameter( OdsParameters.ID_COMMISSION ) != null )
        {
            int nSense = -1;
            int nIdCommission = -1;

            try
            {
                nSense = Integer.parseInt( request.getParameter( OdsParameters.MOVE_SENSE ) );
                nIdCommission = Integer.parseInt( request.getParameter( OdsParameters.ID_COMMISSION ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }

            _model.put( MARK_ID_COM, nIdCommission );

            int nListSize = _tourniquet.size(  );
            int nIndex = 0;

            if ( nSense == -1 )
            {
                while ( ( nIndex < nListSize ) &&
                        ( _tourniquet.get( nIndex ).getCommission(  ).getIdCommission(  ) != nIdCommission ) )
                {
                    nIndex++;
                }

                // si dernier élément de la liste, pas d'échange
                if ( nIndex != ( nListSize - 1 ) )
                {
                    Tourniquet temp = _tourniquet.get( nIndex );
                    _tourniquet.set( nIndex, _tourniquet.get( nIndex + 1 ) );
                    _tourniquet.set( nIndex + 1, temp );
                }
            }
            else
            {
                while ( ( nIndex < nListSize ) &&
                        ( _tourniquet.get( nIndex ).getCommission(  ).getIdCommission(  ) != nIdCommission ) )
                {
                    nIndex++;
                }

                // si premier élément de la liste, pas d'échange
                if ( nIndex != 0 )
                {
                    Tourniquet temp = _tourniquet.get( nIndex );
                    _tourniquet.set( nIndex, _tourniquet.get( nIndex - 1 ) );
                    _tourniquet.set( nIndex - 1, temp );
                }
            }
        }

        UrlItem url = new UrlItem( URL_DO_TOURNIQUET );

        return url.getUrl(  );
    }

    /**
     * Reclasse le tourniquet
     * @param request la requête http
     * @return l'interface de gestion du tourniquet
     */
    public String doReclassement( HttpServletRequest request )
    {
        _model.put( MARK_ID_COM, -1 );

        // Ne rien faire s'il y a au plus 2 éléments dans la liste
        int nNbTourniquets = _tourniquet.size(  );

        if ( nNbTourniquets <= 2 )
        {
            return getHomeUrl( request );
        }

        //Futur état du tourniquet
        List<Tourniquet> listTourniquet = new ArrayList<Tourniquet>(  );

        List<Commission> listCom = CommissionHome.findCommissionList( getPlugin(  ) );
        Tourniquet tourniquet;

        // on "nettoie" listCom à partir de Tourniquet ==> on ne synchronise pas!!!
        int nIndex;

        for ( Commission commission : listCom )
        {
            nIndex = 0;

            while ( ( nIndex < nNbTourniquets ) &&
                    ( _tourniquet.get( nIndex ).getCommission(  ).getIdCommission(  ) != commission.getIdCommission(  ) ) )
            {
                nIndex++;
            }

            // commission n'est pas dans _tourniquet ==> on la retire de la liste
            if ( nIndex == nNbTourniquets )
            {
                listCom.remove( commission );
            }
        }

        // on ajoute la 1ère commission, récupérée dans _tourniquet
        tourniquet = _tourniquet.get( 0 );
        tourniquet.setNumeroOrdre( 1 );
        listTourniquet.add( tourniquet );

        int nIdCommission = tourniquet.getCommission(  ).getIdCommission(  );
        // on récupère l'index de la 1ère commission dans la liste des commissions
        nIndex = 0;

        for ( Commission commission : listCom )
        {
            if ( commission.getIdCommission(  ) == nIdCommission )
            {
                break;
            }

            nIndex++;
        }

        listCom.remove( nIndex );

        // on ajoute la 2ème commission, récupérée dans _tourniquet
        tourniquet = _tourniquet.get( 1 );
        tourniquet.setNumeroOrdre( 2 );
        listTourniquet.add( tourniquet );

        nIdCommission = tourniquet.getCommission(  ).getIdCommission(  );
        nIndex = 0;

        for ( Commission commission : listCom )
        {
            //	on récupère l'index de la 2ème commission dans la liste des commissions
            if ( commission.getIdCommission(  ) == nIdCommission )
            {
                break;
            }

            nIndex++;
        }

        listCom.remove( nIndex );

        int nListComSize = listCom.size(  );

        // on classe à la suite, selon l'ordre dans listCom
        for ( int i = 0; i < nListComSize; i++ )
        {
            Commission com = listCom.get( ( i + nIndex ) % nListComSize );
            tourniquet = new Tourniquet(  );
            tourniquet.setCommission( com );
            tourniquet.setNumeroOrdre( i + 3 );
            listTourniquet.add( tourniquet );
        }

        _tourniquet = listTourniquet;

        UrlItem url = new UrlItem( URL_DO_TOURNIQUET );

        return url.getUrl(  );
    }
}
