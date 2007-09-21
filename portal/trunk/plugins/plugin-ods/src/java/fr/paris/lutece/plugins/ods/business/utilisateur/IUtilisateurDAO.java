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
package fr.paris.lutece.plugins.ods.business.utilisateur;

import java.util.List;
import java.util.Map;

import fr.paris.lutece.portal.service.plugin.Plugin;


/**
 * IUtilisateurDAO
 */
public interface IUtilisateurDAO
{
    /**
    * Rajoute un utilisateur dans la base.
    * @param user le nouvel utilisateur
    * @param plugin le Plugin actif
    */
    void insert( Utilisateur user, Plugin plugin );

    /**
     * Supprime de la table ods_utilisateur l'utilisateur passé en argument.
     * @param user l'utilisateur à supprimer
     * @param plugin le Plugin actif
     */
    void delete( Utilisateur user, Plugin plugin );

    /**
     * Met à jour les informations concernant l'utilisateur passé en argument.
     * @param user les nouvelles informations sur l'utilisateur
     * @param plugin le Plugin actif
     */
    void store( Utilisateur user, Plugin plugin );

    /**
     * Retourne l'utilisateur identifié par nKey
     * @param nKey l'identifiant de l'utilisateur recherché
     * @param plugin le Plugin actif
     * @return l'utilisateur identifié par nKey
     */
    Utilisateur load( String nKey, Plugin plugin );

    /**
     * Retourne la liste des utilisateurs de la base.
     * @param plugin le Plugin actif
     * @return la liste des utilisateurs de la base
     */
    List<Utilisateur> getListUtilisateurs( Plugin plugin );

    /**
     * Retourne la liste des utilisateurs de la base.
     * @param plugin le Plugin actif
     * @return la liste des utilisateurs de la base
     */
    Map<String, Utilisateur> getHashmapUtilisateurs( Plugin plugin );
}
