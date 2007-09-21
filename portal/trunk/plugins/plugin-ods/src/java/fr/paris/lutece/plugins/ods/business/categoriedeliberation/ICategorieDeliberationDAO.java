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
package fr.paris.lutece.plugins.ods.business.categoriedeliberation;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * Interface pour accèder aux / gérer les données de la table ods_categorie_deliberation
 */
public interface ICategorieDeliberationDAO
{
    /**
     * Crée une nouvelle catégorie de délibération dans la table ods_categorie_deliberation
     * @param categorieDeliberation la catégorie de délibération à insérer
     * @param plugin spécifie le plugin actif
     */
    void insert( CategorieDeliberation categorieDeliberation, Plugin plugin );

    /**
     * Supprime la caétégorie passée en paramètre de la table ods_categorie_deliberation
     * @param categorieDeliberation la catégorie de délibération à supprimer
     * @param plugin spécifie le plugin actif
     * @throws AppException si la catégorie de délibération est référencée par un PDD
     */
    void delete( CategorieDeliberation categorieDeliberation, Plugin plugin )
        throws AppException;

    /**
     * Retourne la catégorie de délibération associée à la clé passée en paramètre
     * @param nKey l'id unique de la catégorie de délibération dans la table ods_categorie_deliberation
     * @param plugin spécifie le plugin actif
     * @return la catégorie de délibération associée à l'identifiant nKey
     */
    CategorieDeliberation load( int nKey, Plugin plugin );

    /**
     * Met à jour les informations d'une catégorie de délibération dans la table ods_categorie_deliberation
     * @param categorieDeliberation contient les nouvelles valeurs de la catégorie de délibération
     * @param plugin spécifie le plugin actif
     */
    void store( CategorieDeliberation categorieDeliberation, Plugin plugin );

    /**
     * Retourne la liste des catégories de délibérations
     * @param plugin spécifie le plugin actif
     * @return une liste de catégorie de délibérations
     */
    List<CategorieDeliberation> loadListeCategorieDeliberation( Plugin plugin );

    /**
     * Retourne la liste des codes de la table ods_categorie_deliberation;
     * Exclut de la requête la catégorie de délibération associée à l'identifiant exceptIdcatégorie de délibération.
     * @param exceptCategorie la catégorie de délibération à exclure de la liste ( =null si aucune catégorie de délibération à exclure)
     * @param plugin spécifie le plugin actif
     * @return la liste des codes de catégorie de délibération
     */
    List<String> listCodes( CategorieDeliberation exceptCategorie, Plugin plugin );

    /**
     * Retourne la liste des catégories de délibération actives.
     * @param plugin spécifie le plugin actif
     * @return liste des catégories de délibération actives
     */
    List<CategorieDeliberation> listCategoriesActives( Plugin plugin );

    /**
     * Retourne la liste des catégories de délibération ordonnées par ordre alphabétiqque des libelles
     * @param plugin spécifie le plugin actif
     * @return la liste des catégories de délibération ordonnées
     */
    List<CategorieDeliberation> listCategoriesOrderByLibelle( Plugin plugin );
}
