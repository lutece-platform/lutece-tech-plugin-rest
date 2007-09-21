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
 * Interface pour acc�der aux / g�rer les donn�es de la table ods_categorie_deliberation
 */
public interface ICategorieDeliberationDAO
{
    /**
     * Cr�e une nouvelle cat�gorie de d�lib�ration dans la table ods_categorie_deliberation
     * @param categorieDeliberation la cat�gorie de d�lib�ration � ins�rer
     * @param plugin sp�cifie le plugin actif
     */
    void insert( CategorieDeliberation categorieDeliberation, Plugin plugin );

    /**
     * Supprime la ca�t�gorie pass�e en param�tre de la table ods_categorie_deliberation
     * @param categorieDeliberation la cat�gorie de d�lib�ration � supprimer
     * @param plugin sp�cifie le plugin actif
     * @throws AppException si la cat�gorie de d�lib�ration est r�f�renc�e par un PDD
     */
    void delete( CategorieDeliberation categorieDeliberation, Plugin plugin )
        throws AppException;

    /**
     * Retourne la cat�gorie de d�lib�ration associ�e � la cl� pass�e en param�tre
     * @param nKey l'id unique de la cat�gorie de d�lib�ration dans la table ods_categorie_deliberation
     * @param plugin sp�cifie le plugin actif
     * @return la cat�gorie de d�lib�ration associ�e � l'identifiant nKey
     */
    CategorieDeliberation load( int nKey, Plugin plugin );

    /**
     * Met � jour les informations d'une cat�gorie de d�lib�ration dans la table ods_categorie_deliberation
     * @param categorieDeliberation contient les nouvelles valeurs de la cat�gorie de d�lib�ration
     * @param plugin sp�cifie le plugin actif
     */
    void store( CategorieDeliberation categorieDeliberation, Plugin plugin );

    /**
     * Retourne la liste des cat�gories de d�lib�rations
     * @param plugin sp�cifie le plugin actif
     * @return une liste de cat�gorie de d�lib�rations
     */
    List<CategorieDeliberation> loadListeCategorieDeliberation( Plugin plugin );

    /**
     * Retourne la liste des codes de la table ods_categorie_deliberation;
     * Exclut de la requ�te la cat�gorie de d�lib�ration associ�e � l'identifiant exceptIdcat�gorie de d�lib�ration.
     * @param exceptCategorie la cat�gorie de d�lib�ration � exclure de la liste ( =null si aucune cat�gorie de d�lib�ration � exclure)
     * @param plugin sp�cifie le plugin actif
     * @return la liste des codes de cat�gorie de d�lib�ration
     */
    List<String> listCodes( CategorieDeliberation exceptCategorie, Plugin plugin );

    /**
     * Retourne la liste des cat�gories de d�lib�ration actives.
     * @param plugin sp�cifie le plugin actif
     * @return liste des cat�gories de d�lib�ration actives
     */
    List<CategorieDeliberation> listCategoriesActives( Plugin plugin );

    /**
     * Retourne la liste des cat�gories de d�lib�ration ordonn�es par ordre alphab�tiqque des libelles
     * @param plugin sp�cifie le plugin actif
     * @return la liste des cat�gories de d�lib�ration ordonn�es
     */
    List<CategorieDeliberation> listCategoriesOrderByLibelle( Plugin plugin );
}
