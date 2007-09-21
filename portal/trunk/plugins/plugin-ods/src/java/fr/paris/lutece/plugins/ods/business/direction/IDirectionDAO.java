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
package fr.paris.lutece.plugins.ods.business.direction;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * Interface pour acc�der/g�rer la table ods_direction
 */
public interface IDirectionDAO
{
    /**
     * Supprime la direction pass�e en param�tre de la table ods_direction
     * @param direction la direction � supprimer
     * @param plugin le plugin actif
     * @throws AppException si la direction est r�f�renc�e par un PDD
     */
    void delete( Direction direction, Plugin plugin ) throws AppException;

    /**
         * Cr�e une nouvelle direction dans la table ods_direction
         * @param direction la direction � ins�rer
         * @param plugin le plugin actif
         */
    void insert( Direction direction, Plugin plugin );

    /**
     * Retourne la direction associ�e � la cl� pass�e en param�tre
     * @param nKey l'id unique de la direction dans la table ods_direction
     * @param plugin le plugin actif
     * @return la direction associ�e � l'identifiant nKey
     */
    Direction load( int nKey, Plugin plugin );

    /**
     * Met � jour les informations d'une direction dans la table ods_direction
     * @param direction contient les nouvelles valeurs de la direction
     * @param plugin le plugin actif
     */
    void store( Direction direction, Plugin plugin );

    /**
     * Retourne la liste des directions
     * @param plugin le plugin actif
     * @return une liste de directions
     */
    List<Direction> loadListDirection( Plugin plugin );

    /**
     * Retourne la liste des codes de direction except� celui de la direction associ�e � exceptIdDirection
     * @param exceptDirection direction � exclure de la liste (aucune exclusion si = null)
     * @param plugin le plugin actif
     * @return la liste des codes de direction
     */
    List<String> listCodes( Direction exceptDirection, Plugin plugin );

    /**
     * Retourne la liste des directions actives.
     * @param plugin
     * @return la liste des directions actives
     */
    List<Direction> loadListDirectionsActives( Plugin plugin );
}
