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
package fr.paris.lutece.plugins.ods.business.fascicule;

import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * FasciculeDAO
 */
public class FasciculeDAO implements IFasciculeDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_fascicule ) FROM ods_fascicule";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT f.id_fascicule,f.code_fascicule, " +
        "		f.numero_ordre,f.nom_fascicule, " + "       s.id_seance,s.date_seance,s.date_fin_seance " +
        "FROM ods_fascicule f,ods_seance s " + "WHERE f.id_fascicule = ? and f.id_seance =s.id_seance ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ods_fascicule WHERE id_fascicule=? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_fascicule(" +
        "	id_fascicule, id_seance, code_fascicule, " + "	numero_ordre, nom_fascicule) " + "VALUES(?,?,?,?,?) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ods_fascicule " +
        "SET code_fascicule=?, numero_ordre=?, nom_fascicule=? " + "WHERE id_fascicule=?";
    private static final String SQL_QUERY_FIND_BY_ID_SEANCE = "SELECT f.id_fascicule, f.code_fascicule, " +
        "		f.numero_ordre, f.nom_fascicule, " + "       s.id_seance,s.date_seance,s.date_fin_seance " +
        "FROM ods_fascicule f,ods_seance s " + "WHERE f.id_seance = ? and f.id_seance =s.id_seance " +
        "ORDER BY f.numero_ordre ASC";
    private static final String SQL_QUERY_FIND_ALL_CODE_BY_SEANCE = "SELECT id_fascicule, code_fascicule FROM ods_fascicule WHERE id_seance=?";
    private static final String SQL_QUERY_FIND_ALL_NUMERO_BY_SEANCE = "SELECT id_fascicule, numero_ordre FROM ods_fascicule WHERE id_seance=?";
    private static final String SQL_QUERY_FIND_ALL = "SELECT f.id_fascicule, f.code_fascicule, f.numero_ordre, f.nom_fascicule, " +
        " s.id_seance,s.date_seance,s.date_fin_seance " + "FROM ods_fascicule f, ods_seance s " +
        "WHERE f.id_seance =s.id_seance " + "ORDER BY f.numero_ordre ASC ";

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
     * Supprime le fascicule de la base.
     * @param fascicule le fascicule à supprimer
     * @param plugin le plugin actif
     * @throws AppException si le fascicule ne peut être supprimé
     */
    public void delete( Fascicule fascicule, Plugin plugin )
        throws AppException
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, fascicule.getIdFascicule(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Crée un nouveau fascicule dans la base.
     * @param fascicule le fascicule à créer
     * @param plugin le plugin actif
     */
    public void insert( Fascicule fascicule, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( 1, newPrimaryKey( plugin ) );
        daoUtil.setInt( 2, fascicule.getSeance(  ).getIdSeance(  ) );
        daoUtil.setString( 3, fascicule.getCodeFascicule(  ) );
        daoUtil.setInt( 4, fascicule.getNumeroOrdre(  ) );
        daoUtil.setString( 5, fascicule.getNomFascicule(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne le fascicule identifié par la clé passée en argument.
     * @param nKey l'identifiant du fascicule
     * @param plugin le plugin actif
     * @return le fascicule
     */
    public Fascicule load( int nKey, Plugin plugin )
    {
        Fascicule fascicule = null;
        Seance seance = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            fascicule = new Fascicule(  );
            fascicule.setIdFascicule( daoUtil.getInt( 1 ) );
            fascicule.setCodeFascicule( daoUtil.getString( 2 ) );
            fascicule.setNumeroOrdre( daoUtil.getInt( 3 ) );
            fascicule.setNomFascicule( daoUtil.getString( 4 ) );
            //creation de l'objet seance
            seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( 5 ) );
            seance.setDateSeance( daoUtil.getTimestamp( 6 ) );
            seance.setDateCloture( daoUtil.getTimestamp( 7 ) );
            fascicule.setSeance( seance );
        }

        daoUtil.free(  );

        return fascicule;
    }

    /**
     * Met à jour les informations concernant le fascicule à partir de l'objet passé en argument
     * @param fascicule le fascicule à modifier
     * @param plugin le plugin actif
     */
    public void store( Fascicule fascicule, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setString( 1, fascicule.getCodeFascicule(  ) );
        daoUtil.setInt( 2, fascicule.getNumeroOrdre(  ) );
        daoUtil.setString( 3, fascicule.getNomFascicule(  ) );
        daoUtil.setInt( 4, fascicule.getIdFascicule(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne la liste des fascicules pour une séance.
     * @param nKey l'identifiant de la séance
     * @param plugin le plugin actif
     * @return la liste des fascicules pour une séance
     */
    public List<Fascicule> selectFasciculeListByIdSeance( int nKey, Plugin plugin )
    {
        List<Fascicule> fascicules = new ArrayList<Fascicule>(  );
        Fascicule fascicule = null;
        Seance seance = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_SEANCE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            fascicule = new Fascicule(  );
            fascicule.setIdFascicule( daoUtil.getInt( 1 ) );
            fascicule.setCodeFascicule( daoUtil.getString( 2 ) );
            fascicule.setNumeroOrdre( daoUtil.getInt( 3 ) );
            fascicule.setNomFascicule( daoUtil.getString( 4 ) );
            //creation de l'objet seance
            seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( 5 ) );
            seance.setDateSeance( daoUtil.getTimestamp( 6 ) );
            seance.setDateCloture( daoUtil.getTimestamp( 7 ) );
            fascicule.setSeance( seance );
            fascicules.add( fascicule );
        }

        daoUtil.free(  );

        return fascicules;
    }

    /**
     * Retourne la liste des fascicules
     * @param plugin le plugin actif
     * @return la liste des fascicules
     */
    public List<Fascicule> selectFasciculeList( Plugin plugin )
    {
        List<Fascicule> fascicules = new ArrayList<Fascicule>(  );
        Fascicule fascicule = null;
        Seance seance = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            fascicule = new Fascicule(  );
            fascicule.setIdFascicule( daoUtil.getInt( 1 ) );
            fascicule.setCodeFascicule( daoUtil.getString( 2 ) );
            fascicule.setNumeroOrdre( daoUtil.getInt( 3 ) );
            fascicule.setNomFascicule( daoUtil.getString( 4 ) );
            //creation de l'objet seance
            seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( 5 ) );
            seance.setDateSeance( daoUtil.getTimestamp( 6 ) );
            seance.setDateCloture( daoUtil.getTimestamp( 7 ) );
            fascicule.setSeance( seance );
            fascicules.add( fascicule );
        }

        daoUtil.free(  );

        return fascicules;
    }

    /**
     * Retourne la liste des codes de fascicule d'une séance, à l'exception de celui du fascicule passé en argument
     * @param exceptFascicule fascicule à exclure de la recherche (pas d'exclusion si = null)
     * @param seance la seance
     * @param plugin le plugin actif
     * @return la liste des codes
     */
    public List<String> listCodeFascicule( Fascicule exceptFascicule, Seance seance, Plugin plugin )
    {
        int nIdExceptFascicule;

        if ( exceptFascicule != null )
        {
            nIdExceptFascicule = exceptFascicule.getIdFascicule(  );
        }
        else
        {
            nIdExceptFascicule = -1;
        }

        List<String> listCodes = new ArrayList<String>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL_CODE_BY_SEANCE, plugin );
        daoUtil.setInt( 1, seance.getIdSeance(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )

            if ( daoUtil.getInt( 1 ) != nIdExceptFascicule )
            {
                listCodes.add( daoUtil.getString( 2 ) );
            }

        daoUtil.free(  );

        return listCodes;
    }

    /**
     * Retourne la liste des numéros d'ordre d'une séance, à l'exception de celui du fascicule passé en argument
     * @param exceptFascicule fascicule à exclure de la recherche (pas d'exclusion si = null)
     * @param seance la seance
     * @param plugin le plugin actif
     * @return la liste des codes
     */
    public List<String> listNumeroOrdre( Fascicule exceptFascicule, Seance seance, Plugin plugin )
    {
        int nIdExceptFascicule;

        if ( exceptFascicule != null )
        {
            nIdExceptFascicule = exceptFascicule.getIdFascicule(  );
        }
        else
        {
            nIdExceptFascicule = -1;
        }

        List<String> listNumeros = new ArrayList<String>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL_NUMERO_BY_SEANCE, plugin );
        daoUtil.setInt( 1, seance.getIdSeance(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )

            if ( daoUtil.getInt( 1 ) != nIdExceptFascicule )
            {
                listNumeros.add( OdsConstants.CONSTANTE_CHAINE_VIDE + daoUtil.getInt( 2 ) );
            }

        daoUtil.free(  );

        return listNumeros;
    }
}
