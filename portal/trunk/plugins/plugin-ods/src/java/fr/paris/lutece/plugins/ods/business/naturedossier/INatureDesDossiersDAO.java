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
package fr.paris.lutece.plugins.ods.business.naturedossier;

import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * Interface pour accèder/gérer la table ods_nature_dossier
 */
public interface INatureDesDossiersDAO
{
    /**
     * Supprime la nature de dossier de la base
     * @param nature la nature à supprimer
     * @param plugin le plugin
     * @throws AppException si la nature de dossier contient des entrées dans l'ordre du jour
     */
    void delete( NatureDesDossiers nature, Plugin plugin )
        throws AppException;

    /**
     * Crée une nouvelle nature de dossier dans la base
     * @param nature la nature à ajouter
     * @param plugin
     */
    void insert( NatureDesDossiers nature, Plugin plugin );

    /**
     * Retourne la nature de dossier identifiée par la clé passée en argument.
     * @param nKey l'identifiant de la nature
     * @param plugin
     */
    NatureDesDossiers load( int nKey, Plugin plugin );

    /**
     * Met à jour les informations concernant la nature de dossier passée en argument
     * @param nature la nature mise à jour
     * @param plugin
     */
    void store( NatureDesDossiers nature, Plugin plugin );

    /**
     * Retourne la liste des natures de dossier
     * @param plugin
     */
    List<NatureDesDossiers> loadListeNatures( Plugin plugin );

    /**
     * Retourne la liste des natures de dossier pour la séance passée en argument
     * @param nKey l'identifiant de la séance voulue
     * @param plugin
     * @return
     */
    List<NatureDesDossiers> loadListBySeance( int nKey, Plugin plugin );

    /**
     * Retourne la liste des numéros de nature de dossier pour une séance;
     * La recherche exclut la nature identifiée par nExceptIdNature
     * @param exceptNature la nature à exclure (aucune exclusion si =null)
     * @param seance la seance
     * @param plugin
     * @return la liste des numéros de nature
     */
    List<Integer> listNumNature( NatureDesDossiers exceptNature, Seance seance, Plugin plugin );
}
