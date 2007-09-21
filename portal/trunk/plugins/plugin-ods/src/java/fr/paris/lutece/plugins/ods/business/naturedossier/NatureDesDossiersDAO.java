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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Cette classe permet d'accèder/gérer la table ods_nature_dossier
 */
public class NatureDesDossiersDAO implements INatureDesDossiersDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_nature ) FROM ods_nature_dossier";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ods_nature_dossier WHERE id_nature=?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_nature_dossier(id_nature, id_seance, num_nature, " +
        "libelle_nature) VALUES(?,?,?,?)";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT a.id_nature, a.id_seance, a.num_nature, a.libelle_nature, " +
        "b.date_seance, b.date_fin_seance " +
        "FROM ods_nature_dossier a, ods_seance b WHERE a.id_seance = b.id_seance AND a.id_nature=?";
    private static final String SQL_QUERY_UPDATE = "UPDATE ods_nature_dossier " +
        "SET num_nature=?, libelle_nature=? " + "WHERE id_nature=?";
    private static final String SQL_QUERY_FIND_ALL = "SELECT a.id_nature, a.id_seance, a.num_nature, a.libelle_nature, " +
        " b.date_seance, b.date_fin_seance " + " FROM ods_nature_dossier a, ods_seance b " +
        " WHERE a.id_seance = b.id_seance " + " ORDER BY a.id_seance, a.num_nature ASC";
    private static final String SQL_QUERY_FIND_BY_SEANCE = "SELECT id_nature, num_nature, libelle_nature " +
        "FROM ods_nature_dossier " + "WHERE id_seance=? ORDER BY num_nature ASC";
    private static final String SQL_QUERY_FIND_ALL_NUM_NATURE_BY_SEANCE = "SELECT id_nature, num_nature FROM ods_nature_dossier WHERE id_seance=?";

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
     * Supprime la nature de dossier de la base
     * @param nature la nature à supprimer
     * @param plugin le plugin
     * @throws AppException si la nature de dossier contient des entrées dans l'ordre du jour
     */
    public void delete( NatureDesDossiers nature, Plugin plugin )
        throws AppException
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nature.getIdNature(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Crée une nouvelle nature de dossier dans la base
     * @param nature la nature à ajouter
     * @param plugin
     */
    public void insert( NatureDesDossiers nature, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( 1, newPrimaryKey( plugin ) );
        daoUtil.setInt( 2, nature.getSeance(  ).getIdSeance(  ) );
        daoUtil.setInt( 3, nature.getNumeroNature(  ) );
        daoUtil.setString( 4, nature.getLibelleNature(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne la nature de dossier identifiée par la clé passée en argument.
     * @param nKey l'identifiant de la nature
     * @param plugin
     */
    public NatureDesDossiers load( int nKey, Plugin plugin )
    {
        NatureDesDossiers nature = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nature = new NatureDesDossiers(  );
            nature.setIdNature( daoUtil.getInt( 1 ) );

            Seance seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( 2 ) );
            seance.setDateSeance( daoUtil.getTimestamp( 5 ) );
            seance.setDateCloture( daoUtil.getTimestamp( 6 ) );
            nature.setSeance( seance );

            nature.setNumeroNature( daoUtil.getInt( 3 ) );
            nature.setLibelleNature( daoUtil.getString( 4 ) );
        }

        daoUtil.free(  );

        return nature;
    }

    /**
     * Met à jour les informations concernant la nature de dossier passée en argument
     * @param nature la nature mise à jour
     * @param plugin
     */
    public void store( NatureDesDossiers nature, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, nature.getNumeroNature(  ) );
        daoUtil.setString( 2, nature.getLibelleNature(  ) );
        daoUtil.setInt( 3, nature.getIdNature(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne la liste des natures de dossier
     * @param plugin
     */
    public List<NatureDesDossiers> loadListeNatures( Plugin plugin )
    {
        List<NatureDesDossiers> natures = new ArrayList<NatureDesDossiers>(  );
        NatureDesDossiers nature = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nature = new NatureDesDossiers(  );
            nature.setIdNature( daoUtil.getInt( 1 ) );

            Seance seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( 2 ) );
            nature.setSeance( seance );

            nature.setNumeroNature( daoUtil.getInt( 3 ) );
            nature.setLibelleNature( daoUtil.getString( 4 ) );
            natures.add( nature );
        }

        daoUtil.free(  );

        return natures;
    }

    /**
     * Retourne la liste des natures de dossier pour la séance passée en argument
     * @param nKey l'identifiant de la séance voulue
     * @param plugin
     * @return
     */
    public List<NatureDesDossiers> loadListBySeance( int nKey, Plugin plugin )
    {
        List<NatureDesDossiers> natures = new ArrayList<NatureDesDossiers>(  );
        NatureDesDossiers nature = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_SEANCE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nature = new NatureDesDossiers(  );
            nature.setIdNature( daoUtil.getInt( 1 ) );

            Seance seance = new Seance(  );
            seance.setIdSeance( nKey );
            nature.setSeance( seance );

            nature.setNumeroNature( daoUtil.getInt( 2 ) );
            nature.setLibelleNature( daoUtil.getString( 3 ) );
            natures.add( nature );
        }

        daoUtil.free(  );

        return natures;
    }

    /**
     * Retourne la liste des numéros de nature de dossier pour une séance;
     * La recherche exclut la nature identifiée par nExceptIdNature
     * @param exceptNature la nature à exclure (aucune exclusion si =null)
     * @param seance la seance
     * @param plugin
     * @return la liste des numéros de nature
     */
    public List<Integer> listNumNature( NatureDesDossiers exceptNature, Seance seance, Plugin plugin )
    {
        int nIdExceptdNature;

        if ( exceptNature != null )
        {
            nIdExceptdNature = exceptNature.getIdNature(  );
        }
        else
        {
            nIdExceptdNature = -1;
        }

        List<Integer> listNum = new ArrayList<Integer>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL_NUM_NATURE_BY_SEANCE, plugin );
        daoUtil.setInt( 1, seance.getIdSeance(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            if ( daoUtil.getInt( 1 ) != nIdExceptdNature )
            {
                listNum.add( daoUtil.getInt( 2 ) );
            }
        }

        daoUtil.free(  );

        return listNum;
    }
}
