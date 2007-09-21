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
     * Ajoute un relev� des travaux.
     * @param releve Relev� des travaux � ins�rer dans la table ods_releve_travaux
     * @param plugin le Plugin actif
     * @return l'identifiant du relev� de travaux ins�r�
     */
    int insert( ReleveDesTravaux releve, Plugin plugin );

    /**
     * Modifie le relev� des travaux donn� en param�tre.
     * @param releve Relev� des travaux � modifier
     * @param plugin le Plugin actif
     */
    void store( ReleveDesTravaux releve, Plugin plugin );

    /**
     * Supprime un relev� des travaux.
     * @param releve Relev� des travaux � supprimer de la table ods_releve_travaux
     * @param plugin le Plugin actif
     * @throws AppException si impossible de supprimer
     */
    void delete( ReleveDesTravaux releve, Plugin plugin )
        throws AppException;

    /**
     * Renvoie le relev� des travaux ayant pour identifiant nKey.
     * @param nKey identifiant du relev� des travaux
     * @param plugin le Plugin actif
     * @return Un relev� des travaux
     */
    ReleveDesTravaux load( int nKey, Plugin plugin );

    /**
     * Renvoie la liste des relev�s des travaux pr�sent dans la table ods_releve_travaux.
     * @param plugin le Plugin actif
     * @return La liste des relev� des travaux
     */
    List<ReleveDesTravaux> selectReleveDesTravauxList( Plugin plugin );

    /**
     * Renvoie la liste des relev�s des travaux qui sont marqu�s comme �tant en ligne dans la table ods_releve_travaux.
     * @param plugin le Plugin actif
     * @return La liste des relev�s de travaux en ligne.
     */
    List<ReleveDesTravaux> selectReleveDesTravauxEnLigne( Plugin plugin );

    /**
     * Renvoie la liste des relev�s des travaux pour une s�ance donn�e en argument.
     * @param seance la s�ance
     * @param plugin le Plugin actif
     * @return La liste des relev�s de travaux pour une s�ance donn�e
     */
    List<ReleveDesTravaux> selectReleveDesTravauxSeance( Seance seance, Plugin plugin );

    /**
     * Renvoie la liste des relev�s des travaux correspondant au filtre donn�.
     * @param filter le filtre
     * @param plugin le Plugin actif
     * @return La liste des relev�s de travaux pour un filtre donn�e
     */
    List<ReleveDesTravaux> selectReleveDesTravauxByFilter( ReleveDesTravauxFilter filter, Plugin plugin );
}
