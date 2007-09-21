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
package fr.paris.lutece.plugins.ods.business.statut;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * IStatutDAO
 */
public interface IStatutDAO
{
    /**
    * Insère un nouvel enregistrement dans la table ods_statut à partir de l’objet statut passé en paramètre
    * @param statut  le statut à insérer dans la table ods_statut
    * @param plugin plugin
    */
    void insert( Statut statut, Plugin plugin );

    /**
     * Modifie l'enregistrement dans la table ods_statut correspondant à  l’objet statut passé en paramètre
     * @param statut le statut à modifier dans la table ods_statut
     * @param plugin plugin
     */
    void store( Statut statut, Plugin plugin );

    /**
     * Supprime l'enregistrement dans la table ods_statut correspondant à l’objet statut passé en paramètre
     * @param statut le statut à supprimer dans la table ods_statut
     * @param plugin plugin
     * @throws AppException si le statut est référencé par un objet tiers
     */
    void delete( Statut statut, Plugin plugin ) throws AppException;

    /**
     * renvoie le statut correspondant à l'enregistrement dans la table ods_statut ayant comme identifiant nKey
     * @param nKey l'identifiant du statut
     * @param plugin plugin
     * @return le statut correspondant
     */
    Statut load( int nKey, Plugin plugin );

    /**
     * renvoie la liste des statuts présents dans la table ods_statut
     * @param plugin
     * @return liste d'objets statut
     */
    List<Statut> selectStatutList( Plugin plugin );

    /**
     * renvoie la liste des statuts correspondant au filtre donné en paramètre.
     * @param statutFilter le filtre
     * @param plugin plugin
     * @return liste des statuts correspondant
     */
    List<Statut> selectStatutListWithFilter( StatutFilter filter, Plugin plugin );
}
