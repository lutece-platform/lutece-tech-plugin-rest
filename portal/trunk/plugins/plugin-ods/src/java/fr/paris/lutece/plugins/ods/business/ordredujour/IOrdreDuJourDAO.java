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

import java.util.List;


/**
 *
 */
public interface IOrdreDuJourDAO
{
    /**
    *supprime l'ordre du jour passé en parametre
    * @param odj OrdreDuJour
    * @param plugin Plugin
    */
    void delete( OrdreDuJour odj, Plugin plugin );

    /**
     * insère l'ordre du jour passé en parametre dans la table ods_odj
     * @param odj OrdreDuJour
     * @param plugin Plugin
     * @return l'id de l'ordre du jour nouvellement inséré
     */
    int insert( OrdreDuJour odj, Plugin plugin );

    /**
     *retourne l'ordre du jour d'id nKey
     * @param nKey int
     * @param plugin Plugin
     * @return OrdreDuJour ordreDuJour
     */
    OrdreDuJour load( int nKey, Plugin plugin );

    /**
     * update l'ordre du jour transmis en parametre
     * @param odj OrdreDuJour
     * @param plugin Plugin
     */
    void store( OrdreDuJour odj, Plugin plugin );

    /**
     * renvoie la liste des ordres du jour répondant au filtre filter passé en parametre
     * @param filter le filtre appliqué pour la recherche
     * @param bFrontOffice true si la liste est appellée par le front office false sinon
     * @param plugin Plugin
     * @return OrdreDuJour ordreDuJour
     */
    List<OrdreDuJour> selectOrdreDuJourList( Plugin plugin, OrdreDuJourFilter filter, boolean bFrontOffice );

    /**
     * Test si un odj répondant au filtre 'filter' existe en base.
     * @param filter le filtre appliqué pour la recherche
     * @param plugin  plugin
     * @return true si un odj répondant au filtre 'filter' existe en base, false sinon
     */
    boolean isAlreadyExist( OrdreDuJourFilter filter, Plugin plugin );

    /**
     * renvoie l'odj répondant au filtre 'filter' .
     * @param filter le filtre de l'odj
     * @param plugin Plugin
     * @return OrdreDuJour ordreDuJour
     */
    OrdreDuJour findByOdjFilter( OrdreDuJourFilter filter, Plugin plugin );

    /**
         * renvoie l'odj de reference répondant au filtre 'filter'
         * l’ordre du jour de référence est le dernier ordre du jour qui a été publié..
         * @param filter le filtre de l'odj
         * @param plugin Plugin
         * @return OrdreDuJour ordreDuJour
         */
    OrdreDuJour findOdjReferenceByFilter( OrdreDuJourFilter filter, Plugin plugin );

    /**
     * Retourne les ODJ dont l'une des entrées a cet élu dans ses rapporteurs
     * @param nIdRapporteur l'id de l'éu rapporteur
     * @param bPourPdd l'élu est-il pour un PDD ?
     * @param plugin le plugin
     * @return les ODJ filtrés par rapporteur
     */
    List<OrdreDuJour> selectByRapporteur( int nIdRapporteur, boolean bPourPdd, Plugin plugin );
}
