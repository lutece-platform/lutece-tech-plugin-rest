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
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * Cette classe permet l'accès aux objets CatégorieDélibération.
 */
public class CategorieDeliberationHome
{
    private static ICategorieDeliberationDAO _dao = (ICategorieDeliberationDAO) SpringContextService.getPluginBean( "ods",
            "categorieDeliberationDAO" );

    /**
     * Crée une nouvelle catégorie de délibération à partir de l'objet passé en argument.
     * @param categorieDeliberation la cétagorie à créer
     * @param plugin le plugin actif
     */
    public static void create( CategorieDeliberation categorieDeliberation, Plugin plugin )
    {
        _dao.insert( categorieDeliberation, plugin );
    }

    /**
     * Retourne la catégorie de délibération ayant pour identifiant nKey.
     * @param nKey l'identifiant de la catégorie
     * @param plugin le plugin actif
     * @return la catégorie de délibération recherchée
     */
    public static CategorieDeliberation findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Supprime une catégories de délibération de la liste des catégories.
     * @param categorieDeliberation la catégorie à supprimer
     * @param plugin le plugin actif
     * @throws AppException si la catégorie de délibération est référencée par un PDD
     */
    public static void remove( CategorieDeliberation categorieDeliberation, Plugin plugin )
        throws AppException
    {
        _dao.delete( categorieDeliberation, plugin );
    }

    /**
     * Met à jour une catégorie de délibération
     * @param categorieDeliberation la catégorie à modifier
     * @param plugin le plugin actif
     */
    public static void update( CategorieDeliberation categorieDeliberation, Plugin plugin )
    {
        _dao.store( categorieDeliberation, plugin );
    }

    /**
     * Retourne la liste de toutes les catégories de délibération.
     * @param plugin le plugin actif
     * @return la liste des catégories de délibération
     */
    public static List<CategorieDeliberation> findCategorieDeliberationList( Plugin plugin )
    {
        return _dao.loadListeCategorieDeliberation( plugin );
    }

    /**
     * Indique si le code passé en argument existe déjà dans la table ods_categorie_deliberation;
     * Exclut de la recherche la catégorie associée à exceptIdCategorie
     * @param exceptCategorie la categorie à exclure (pas d'exclusion si =null)
     * @param code le code recherché
     * @param plugin
     * @return true si le code est déjà utilisé, sinon false
     */
    public static boolean containsCode( CategorieDeliberation exceptCategorie, String strCode, Plugin plugin )
    {
        return _dao.listCodes( exceptCategorie, plugin ).contains( strCode );
    }

    /**
     * Retourne la liste des catégories de délibération actives.
     * @param plugin
     * @return la liste des catégories de délibération actives
     */
    public static List<CategorieDeliberation> findAllCategoriesActives( Plugin plugin )
    {
        return _dao.listCategoriesActives( plugin );
    }

    /**
     * Retourne la liste des catégories actives, triées par ordre alphabétique des libellés
     * @param plugin
     * @return la liste ordonnée par libellé
     */
    public static List<CategorieDeliberation> findAllActivesOrderByLibelle( Plugin plugin )
    {
        return _dao.listCategoriesOrderByLibelle( plugin );
    }
}
