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
package fr.paris.lutece.plugins.ods.business.relevetravaux;

import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * IReleveDesTravauxDAO
 */
public interface IReleveDesTravauxDAO
{
    /**
     * Ajoute un relevé des travaux.
     * @param releve Relevé des travaux à insérer dans la table ods_releve_travaux
     * @param plugin le Plugin actif
     * @return l'identifiant du relevé de travaux inséré
     */
    int insert( ReleveDesTravaux releve, Plugin plugin );

    /**
     * Modifie le relevé des travaux donné en paramètre.
     * @param releve Relevé des travaux à modifier
     * @param plugin le Plugin actif
     */
    void store( ReleveDesTravaux releve, Plugin plugin );

    /**
     * Supprime un relevé des travaux.
     * @param releve Relevé des travaux à supprimer de la table ods_releve_travaux
     * @param plugin le Plugin actif
     * @throws AppException si impossible de supprimer
     */
    void delete( ReleveDesTravaux releve, Plugin plugin )
        throws AppException;

    /**
     * Renvoie le relevé des travaux ayant pour identifiant nKey.
     * @param nKey identifiant du relevé des travaux
     * @param plugin le Plugin actif
     * @return Un relevé des travaux
     */
    ReleveDesTravaux load( int nKey, Plugin plugin );

    /**
     * Renvoie la liste des relevés des travaux présent dans la table ods_releve_travaux.
     * @param plugin le Plugin actif
     * @return La liste des relevé des travaux
     */
    List<ReleveDesTravaux> selectReleveDesTravauxList( Plugin plugin );

    /**
     * Renvoie la liste des relevés des travaux qui sont marqués comme étant en ligne dans la table ods_releve_travaux.
     * @param plugin le Plugin actif
     * @return La liste des relevés de travaux en ligne.
     */
    List<ReleveDesTravaux> selectReleveDesTravauxEnLigne( Plugin plugin );

    /**
     * Renvoie la liste des relevés des travaux pour une séance donnée en argument.
     * @param seance la séance
     * @param plugin le Plugin actif
     * @return La liste des relevés de travaux pour une séance donnée
     */
    List<ReleveDesTravaux> selectReleveDesTravauxSeance( Seance seance, Plugin plugin );

    /**
     * Renvoie la liste des relevés des travaux correspondant au filtre donné.
     * @param filter le filtre
     * @param plugin le Plugin actif
     * @return La liste des relevés de travaux pour un filtre donnée
     */
    List<ReleveDesTravaux> selectReleveDesTravauxByFilter( ReleveDesTravauxFilter filter, Plugin plugin );
}
