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
package fr.paris.lutece.plugins.ods.business.ordredujour;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 *OrdreDuJourHome
 *
 */
public class OrdreDuJourHome
{
    private static IOrdreDuJourDAO _dao = (IOrdreDuJourDAO) SpringContextService.getPluginBean( "ods", "ordreDuJourDAO" );

    /**
     * créé un nouvel ordre du jour
     * @param odj OrdreDuJour
     * @param plugin Plugin
     * @return l'id de l'odj créé
     */
    public static int create( OrdreDuJour odj, Plugin plugin )
    {
        return _dao.insert( odj, plugin );
    }

    /**
     * retourne l'ordre du jour d'id nKey
     * @param nKey int
     * @param plugin Plugin
     * @return l'ordre du jour d'id nKey
     */
    public static OrdreDuJour findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * supprime l'ordre du jour passé en parametre
     * @param odj OrdreDuJour
     * @param plugin Plugin
     */
    public static void remove( OrdreDuJour odj, Plugin plugin )
    {
        _dao.delete( odj, plugin );
    }

    /**
     *        modifie l'ordre du jour odj
     * @param odj OrdreDuJour
     * @param plugin Plugin
     */
    public static void update( OrdreDuJour odj, Plugin plugin )
    {
        _dao.store( odj, plugin );
    }

    /**
    * renvoie la liste des ordres du jour répondant au filtre filter passé en parametre
     * @param filter le filtre appliqué pour la recherche
     * @param bFrontOffice true si la liste est appellée par le front office false sinon
    * @param plugin Plugin
    * @return la liste des ordres du jour répondant au filtre filter
    */
    public static List<OrdreDuJour> findOrdreDuJourList( Plugin plugin, OrdreDuJourFilter filter, boolean bFrontOffice )
    {
        return _dao.selectOrdreDuJourList( plugin, filter, bFrontOffice );
    }

    /**
     * Test si un odj répondant au filtre 'filter' existe en base.
     * @param filter le filtre appliqué pour la recherche
     * @param plugin  plugin
     * @return true si un odj répondant au filtre 'filter' existe en base, false sinon
     */
    public static boolean isAlreadyExist( OrdreDuJourFilter filter, Plugin plugin )
    {
        return _dao.isAlreadyExist( filter, plugin );
    }

    /**
     * renvoie l'odj répondant au filtre 'filter' .
     * @param filter le filtre de l'odj
     * @param plugin Plugin
     * @return OrdreDuJour ordreDuJour
     */
    public static OrdreDuJour findByOdjFilter( OrdreDuJourFilter filter, Plugin plugin )
    {
        return _dao.findByOdjFilter( filter, plugin );
    }

    /**
         * renvoie l'odj de reference répondant au filtre 'filter'
         * l’ordre du jour de référence est le dernier ordre du jour qui a été publié..
         * @param filter le filtre de l'odj
         * @param plugin Plugin
         * @return OrdreDuJour ordreDuJour
         */
    public static OrdreDuJour findOdjReferenceByFilter( OrdreDuJourFilter filter, Plugin plugin )
    {
        return _dao.findOdjReferenceByFilter( filter, plugin );
    }

    /**
     * Retourne les ODJ dont l'une des entrées a cet élu dans ses rapporteurs
     * @param nIdRapporteur l'id de l'éu rapporteur
     * @param bPourPdd l'odj est-il pr un pdd ?
     * @param plugin le plugin
     * @return les ODJ filtrés par rapporteur
     */
    public static List<OrdreDuJour> findOdjByRapporteur( int nIdRapporteur, boolean bPourPdd, Plugin plugin )
    {
        return _dao.selectByRapporteur( nIdRapporteur, bPourPdd, plugin );
    }
}
