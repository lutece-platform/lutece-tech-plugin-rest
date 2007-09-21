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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Permet l'accès aux données de la table ods_direction.
 */
public class DirectionDAO implements IDirectionDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_direction ) " + "FROM ods_direction ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ods_direction WHERE id_direction = ?  ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_direction(" +
        "	id_direction,code_direction,libelle_court,libelle_long,actif) " + "VALUES (?,?,?,?,?) ";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_direction,code_direction,libelle_court,libelle_long,actif " +
        "FROM ods_direction " + "WHERE id_direction = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ods_direction " +
        "SET code_direction=?,libelle_court=?,libelle_long=?,actif=? " + "WHERE  id_direction=?";
    private static final String SQL_QUERY_FIND_ALL = "SELECT id_direction,code_direction,libelle_court,libelle_long,actif " +
        "FROM ods_direction ";
    private static final String SQL_QUERY_FILTER_ACTIF = "WHERE actif=true ";
    private static final String SQL_QUERY_ORDER_BY_CODE_DIRECTION = " ORDER BY code_direction ASC ";

    /**
     * Retourne une nouvelle clé primaire unique sur la table ods_direction.
     * @param plugin le plugin actif
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
     * Supprime la direction passée en paramètre de la table ods_direction
     * @param direction la direction à supprimer
     * @param plugin le plugin actif
     * @throws AppException si la direction est référencée par un PDD
     */
    public void delete( Direction direction, Plugin plugin )
        throws AppException
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, direction.getIdDirection(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Crée une nouvelle direction dans la table ods_direction
     * @param direction la direction à insérer
     * @param plugin le plugin actif
     */
    public void insert( Direction direction, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        direction.setIdDirection( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, direction.getIdDirection(  ) );
        daoUtil.setString( 2, direction.getCode(  ) );
        daoUtil.setString( 3, direction.getLibelleCourt(  ) );
        daoUtil.setString( 4, direction.getLibelleLong(  ) );
        daoUtil.setBoolean( 5, direction.isActif(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne la direction associée à la clé passée en paramètre
     * @param nKey l'id unique de la direction dans la table ods_direction
     * @param plugin le plugin actif
     * @return la direction associée à l'identifiant nKey
     */
    public Direction load( int nKey, Plugin plugin )
    {
        Direction direction = new Direction(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            direction = new Direction(  );
            direction.setIdDirection( daoUtil.getInt( 1 ) );
            direction.setCode( daoUtil.getString( 2 ) );
            direction.setLibelleCourt( daoUtil.getString( 3 ) );
            direction.setLibelleLong( daoUtil.getString( 4 ) );
            direction.setActif( daoUtil.getBoolean( 5 ) );
        }

        daoUtil.free(  );

        return direction;
    }

    /**
     * Met à jour les informations d'une direction dans la table ods_direction
     * @param direction contient les nouvelles valeurs de la direction
     * @param plugin le plugin actif
     */
    public void store( Direction direction, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setString( 1, direction.getCode(  ) );
        daoUtil.setString( 2, direction.getLibelleCourt(  ) );
        daoUtil.setString( 3, direction.getLibelleLong(  ) );
        daoUtil.setBoolean( 4, direction.isActif(  ) );
        daoUtil.setInt( 5, direction.getIdDirection(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne la liste des directions
     * @param plugin le plugin actif
     * @return une liste de directions
     */
    public List<Direction> loadListDirection( Plugin plugin )
    {
        List<Direction> listDirection = new ArrayList<Direction>(  );
        Direction direction;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL + SQL_QUERY_ORDER_BY_CODE_DIRECTION, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            direction = new Direction(  );
            direction.setIdDirection( daoUtil.getInt( 1 ) );
            direction.setCode( daoUtil.getString( 2 ) );
            direction.setLibelleCourt( daoUtil.getString( 3 ) );
            direction.setLibelleLong( daoUtil.getString( 4 ) );
            direction.setActif( daoUtil.getBoolean( 5 ) );
            listDirection.add( direction );
        }

        daoUtil.free(  );

        return listDirection;
    }

    /**
     * Retourne la liste des codes de direction excepté celui de la direction associée à exceptIdDirection
     * @param exceptDirection direction à exclure de la liste (aucune exclusion si = null)
     * @param plugin le plugin actif
     * @return la liste des codes de direction
     */
    public List<String> listCodes( Direction exceptDirection, Plugin plugin )
    {
        int nIdExceptDirection;

        if ( exceptDirection != null )
        {
            nIdExceptDirection = exceptDirection.getIdDirection(  );
        }
        else
        {
            nIdExceptDirection = -1;
        }

        List<String> listcodes = new ArrayList<String>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL + SQL_QUERY_ORDER_BY_CODE_DIRECTION, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            if ( daoUtil.getInt( 1 ) != nIdExceptDirection )
            {
                listcodes.add( daoUtil.getString( 2 ) );
            }
        }

        daoUtil.free(  );

        return listcodes;
    }

    /**
     * Retourne la liste des directions actives.
     * @param plugin
     * @return la liste des directions actives
     */
    public List<Direction> loadListDirectionsActives( Plugin plugin )
    {
        List<Direction> listDirection = new ArrayList<Direction>(  );
        Direction direction;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL + SQL_QUERY_FILTER_ACTIF + SQL_QUERY_ORDER_BY_CODE_DIRECTION,
                plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            direction = new Direction(  );
            direction.setIdDirection( daoUtil.getInt( 1 ) );
            direction.setCode( daoUtil.getString( 2 ) );
            direction.setLibelleCourt( daoUtil.getString( 3 ) );
            direction.setLibelleLong( daoUtil.getString( 4 ) );
            direction.setActif( true );
            listDirection.add( direction );
        }

        daoUtil.free(  );

        return listDirection;
    }
}
