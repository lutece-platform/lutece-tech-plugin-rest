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
package fr.paris.lutece.plugins.ods.business.panier;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


/**
 * Interface pour accèder/gérer la table ods_panier
 */
public interface IPanierDAO
{
    /**
     * Retourne Tableau contenant les ids des éléments du panier appartenant au user passé en paramètre
     *
     * @param idUser id du user
     * @param plugin le plugin actif
     * @return Tableau contenant les ids des éléments du panier appartenant au user passé en paramètre
     */
    String[] selectElementsPanierByIdUser( String idUser, Plugin plugin );

    /**
     * Retourne toutes les archives de l'utilisateur passé en paramètre
     *
     * @param idUser id du user
     * @param plugin le plugin actif
     * @return Liste des zip - List<Panier> -qui ont été téléchargé par le user passé en paramètre
     */
    List<Panier> selectZipByIdUser( String idUser, Plugin plugin );

    /**
     * Suppression l'element du panier qui possede l'id de l'utilisateur et l'id du fichier passé en paramètre
     * @param idUser id du user
     * @param idFichier id du fichier
     * @param plugin le plugin actif
     */
    void deleteElementPanier( String idUser, int idFichier, Plugin plugin );

    /**
    * Création de l'element du panier qui possede l'id de l'utilisateur et l'id du fichier passé en paramètre
    * @param idUser id du user
    * @param idFichier id du fichier
    * @param plugin le plugin actif
    */
    void createElementPanier( String idUser, int idFichier, Plugin plugin );

    /**
     * Retourne l'archive ZIP qui à été téléchargé a la date passé en paramètre
     *
     * @param dateAjout Date qui correspond à la demande du téléchargement de l'archive
     * @param plugin le plugin actif
     * @return L'element du Panier
     */
    Panier loadZipByDateAjout( String dateAjout, Plugin plugin );

    /**
     * Supprime tous les documents.
     *
     * @param plugin le plugin actif
     */
    void deleteAll( Plugin plugin );
}
