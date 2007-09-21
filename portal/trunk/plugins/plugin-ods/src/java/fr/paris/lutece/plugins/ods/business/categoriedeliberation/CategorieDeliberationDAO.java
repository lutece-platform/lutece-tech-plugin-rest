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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Cette classe permet d'accèder aux données de gérer la table ods_categorie_deliberation
 */
public class CategorieDeliberationDAO implements fr.paris.lutece.plugins.ods.business.categoriedeliberation.ICategorieDeliberationDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_categorie ) " + "FROM ods_categorie_deliberation ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_categorie_deliberation(" +
        "	id_categorie,code_categorie,libelle_categorie,actif) " + "VALUES (?,?,?,?) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ods_categorie_deliberation " +
        "WHERE id_categorie = ?  ";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_categorie,code_categorie,libelle_categorie,actif " +
        "FROM ods_categorie_deliberation " + "WHERE id_categorie = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ods_categorie_deliberation " +
        "SET code_categorie=?,libelle_categorie=?,actif=? " + "WHERE  id_categorie=?";
    private static final String SQL_QUERY_FIND_ALL = "SELECT id_categorie,code_categorie,libelle_categorie,actif " +
        "FROM ods_categorie_deliberation ";
    private static final String SQL_QUERY_FIND_BY_CODE = "SELECT id_categorie,code_categorie " +
        "FROM ods_categorie_deliberation ";
    private static final String SQL_QUERY_ORDER_BY_CODE_CATEGORIE = " ORDER BY code_categorie ASC ";
    private static final String SQL_QUERY_ORDER_BY_LIBELLE = " ORDER BY libelle_categorie ASC ";
    private static final String SQL_QUERY_FIND_ACTIVES = "SELECT id_categorie,code_categorie,libelle_categorie " +
        "FROM ods_categorie_deliberation " + "WHERE actif=true ";

    /**
     * Retourne une nouvelle clé primaire unique sur la table ods_categorie_deliberation.
     * @param plugin spécifie le plugin actif
     * @return nouvelle clé primaire
     */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
     * Crée une nouvelle catégorie de délibération dans la table ods_categorie_deliberation
     * @param categorieDeliberation la catégorie de délibération à insérer
     * @param plugin spécifie le plugin actif
     */
    public void insert( CategorieDeliberation categorieDeliberation, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( 1, newPrimaryKey( plugin ) );
        daoUtil.setInt( 2, categorieDeliberation.getCode(  ) );
        daoUtil.setString( 3, categorieDeliberation.getLibelle(  ) );
        daoUtil.setBoolean( 4, categorieDeliberation.isActif(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime la catégorie passée en paramètre de la table ods_categorie_deliberation
     * @param categorieDeliberation la catégorie de délibération à supprimer
     * @param plugin spécifie le plugin actif
     * @throws AppException si la catégorie de délibération est référencée par un PDD
     */
    public void delete( CategorieDeliberation categorieDeliberation, Plugin plugin )
        throws AppException
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, categorieDeliberation.getIdCategorie(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne la catégorie de délibération associée à la clé passée en paramètre
     * @param nKey l'id unique de la catégorie de délibération dans la table ods_categorie_deliberation
     * @param plugin spécifie le plugin actif
     * @return la catégorie de délibération associée à l'identifiant nKey
     */
    public CategorieDeliberation load( int nKey, Plugin plugin )
    {
        CategorieDeliberation categorie = new CategorieDeliberation(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            categorie.setIdCategorie( daoUtil.getInt( 1 ) );
            categorie.setCode( daoUtil.getInt( 2 ) );
            categorie.setLibelle( daoUtil.getString( 3 ) );
            categorie.setActif( daoUtil.getBoolean( 4 ) );
        }

        daoUtil.free(  );

        return categorie;
    }

    /**
     * Met à jour les informations d'une catégorie de délibération dans la table ods_categorie_deliberation
     * @param categorieDeliberation contient les nouvelles valeurs de la catégorie de délibération
     * @param plugin spécifie le plugin actif
     */
    public void store( CategorieDeliberation categorieDeliberation, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, categorieDeliberation.getCode(  ) );
        daoUtil.setString( 2, categorieDeliberation.getLibelle(  ) );
        daoUtil.setBoolean( 3, categorieDeliberation.isActif(  ) );
        daoUtil.setInt( 4, categorieDeliberation.getIdCategorie(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne la liste des catégories de délibérations
     * @param plugin spécifie le plugin actif
     * @return une liste de catégorie de délibérations
     */
    public List<CategorieDeliberation> loadListeCategorieDeliberation( Plugin plugin )
    {
        List<CategorieDeliberation> listCategorie = new ArrayList<CategorieDeliberation>(  );
        CategorieDeliberation categorie;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL + SQL_QUERY_ORDER_BY_CODE_CATEGORIE, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            categorie = new CategorieDeliberation(  );
            categorie.setIdCategorie( daoUtil.getInt( 1 ) );
            categorie.setCode( daoUtil.getInt( 2 ) );
            categorie.setLibelle( daoUtil.getString( 3 ) );
            categorie.setActif( daoUtil.getBoolean( 4 ) );
            listCategorie.add( categorie );
        }

        daoUtil.free(  );

        return listCategorie;
    }

    /**
     * Retourne la liste des codes de la table ods_categorie_deliberation;
     * Exclut de la requête la catégorie de délibération associée à l'identifiant exceptIdcatégorie de délibération.
     * @param exceptCategorie la catégorie de délibération à exclure de la liste ( =null si aucune catégorie de délibération à exclure)
     * @param plugin spécifie le plugin actif
     * @return la liste des codes de catégorie de délibération
     */
    public List<String> listCodes( CategorieDeliberation exceptCategorie, Plugin plugin )
    {
        int nIdExceptCategorie;

        if ( exceptCategorie != null )
        {
            nIdExceptCategorie = exceptCategorie.getIdCategorie(  );
        }
        else
        {
            nIdExceptCategorie = -1;
        }

        List<String> listCodes = new ArrayList<String>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_CODE + SQL_QUERY_ORDER_BY_CODE_CATEGORIE, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            if ( daoUtil.getInt( 1 ) != nIdExceptCategorie )
            {
                listCodes.add( daoUtil.getString( 2 ) );
            }
        }

        daoUtil.free(  );

        return listCodes;
    }

    /**
    * Retourne la liste des catégories de délibération actives.
    * @param plugin
    * @return liste des catégories de délibération actives
    */
    public List<CategorieDeliberation> listCategoriesActives( Plugin plugin )
    {
        List<CategorieDeliberation> listCategorie = new ArrayList<CategorieDeliberation>(  );
        CategorieDeliberation categorie;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ACTIVES + SQL_QUERY_ORDER_BY_CODE_CATEGORIE, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            categorie = new CategorieDeliberation(  );
            categorie.setIdCategorie( daoUtil.getInt( 1 ) );
            categorie.setCode( daoUtil.getInt( 2 ) );
            categorie.setLibelle( daoUtil.getString( 3 ) );
            listCategorie.add( categorie );
        }

        daoUtil.free(  );

        return listCategorie;
    }

    /**
     * Retourne la liste des catégories de délibération ordonnées par ordre alphabétiqque des libelles
     * @param plugin
     * @return la liste des catégories de délibération ordonnées
     */
    public List<CategorieDeliberation> listCategoriesOrderByLibelle( Plugin plugin )
    {
        List<CategorieDeliberation> listCategorie = new ArrayList<CategorieDeliberation>(  );
        CategorieDeliberation categorie;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ACTIVES + SQL_QUERY_ORDER_BY_LIBELLE, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            categorie = new CategorieDeliberation(  );
            categorie.setIdCategorie( daoUtil.getInt( 1 ) );
            categorie.setCode( daoUtil.getInt( 2 ) );
            categorie.setLibelle( daoUtil.getString( 3 ) );
            listCategorie.add( categorie );
        }

        daoUtil.free(  );

        return listCategorie;
    }
}
