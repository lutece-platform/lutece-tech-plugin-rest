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
package fr.paris.lutece.plugins.ods.service.search.requete;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


/**
 * IRequeteUtilisateurDAO : interface pour gérer les requêtes sauvegardées par les utilisateurs
 */
public interface IRequeteUtilisateurDAO
{
    /**
     * Sauvegarde une nouvelle requête utilisateur dans la base
     * @param requete la requête utilisateur
     * @param plugin le plugin
     */
    void insert( RequeteUtilisateur requete, Plugin plugin );

    /**
     * Supprime une requête utilisateur sauvegardée
     * @param requete la requête utilisateur à supprimer
     * @param plugin le plugin
     */
    void delete( RequeteUtilisateur requete, Plugin plugin );

    /**
     * Met à jour les informations de la requête utilisateur<br/>
     * Attention cette méthode <u><b>ne modifie pas le nombre, ou la valeur des critères</b></u>
     * @param requete la requete à modifier
     * @param plugin le plugin
     */
    void update( RequeteUtilisateur requete, Plugin plugin );

    /**
     * Retourne la requête utilisateur identifiée par nKey
     * @param nKey l'identifiant de la requête
     * @param plugin le plugin
     * @return la requête utilisateur identifiée par nKey
     */
    RequeteUtilisateur load( int nKey, Plugin plugin );

    /**
     * Retourne la liste des requêtes sauvegardées par un utilisateur
     * @param strUserName l'utilisateur
     * @param plugin le plugin
     * @return la liste des requêtes de l'utilisateur
     */
    List<RequeteUtilisateur> selectRequetesByUser( String strUserName, Plugin plugin );
}
