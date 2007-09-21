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
package fr.paris.lutece.plugins.ods.business.groupepolitique;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 *
 */
public class GroupePolitiqueDAO implements fr.paris.lutece.plugins.ods.business.groupepolitique.IGroupePolitiqueDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_groupe ) FROM ods_groupe ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_groupe( " +
        "		id_groupe, nom_groupe, nom_complet, actif) " + "VALUES ( ?, ?, ?, ?) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ods_groupe " +
        "SET nom_groupe = ?, nom_complet = ?, actif = ? " + "WHERE id_groupe= ?  ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ods_groupe WHERE id_groupe = ?  ";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT nom_groupe,nom_complet, actif " +
        "FROM ods_groupe " + "WHERE id_groupe = ? ";
    private static final String SQL_QUERY_GROUPE_LIST = "SELECT id_groupe,nom_groupe,nom_complet,actif " +
        "FROM ods_groupe ";
    private static final String SQL_QUERY_LISTE_GROUPES_ACTIFS = "SELECT id_groupe,nom_groupe,nom_complet " +
        "FROM ods_groupe " + "WHERE actif=true ";
    private static final String SQL_QUERY_FIND_BY_ID_ELU = "SELECT g.id_groupe,g.nom_groupe,g.nom_complet " +
        "FROM ods_groupe g,ods_elu e " + "WHERE e.id_elu= ? and g.id_groupe = e.id_groupe ";
    private static final String SQL_QUERY_ORDER_BY_NOM_GROUPE = " ORDER BY nom_groupe ASC ";

    /**
     * Génère un nouvel identifiant
     * @param plugin plugin
     * @return Retourne l’identifiant généré
     */
    int newPrimaryKey( Plugin plugin )
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
     * Insère un nouvel enregistrement dans la table ods_groupe à partir de l’objet groupe passé en paramètre
     * @param groupe le groupe politique à insérer dans la table ods_groupe
     * @param plugin plugin
     */
    public void insert( GroupePolitique groupe, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        groupe.setIdGroupe( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, groupe.getIdGroupe(  ) );
        daoUtil.setString( 2, groupe.getNomGroupe(  ) );
        daoUtil.setString( 3, groupe.getNomComplet(  ) );
        daoUtil.setBoolean( 4, groupe.isActif(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Modifie l'enregistrement dans la table ods_groupe correspondant à  l’objet groupe passé en paramètre
     * @param groupe le groupe politique à modifier dans la table ods_groupe
     * @param plugin plugin
     */
    public void store( GroupePolitique groupe, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setString( 1, groupe.getNomGroupe(  ) );
        daoUtil.setString( 2, groupe.getNomComplet(  ) );
        daoUtil.setBoolean( 3, groupe.isActif(  ) );
        daoUtil.setInt( 4, groupe.getIdGroupe(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime l' enregistrement dans la table ods_groupe correspondant à  l’objet groupe passé en paramètre
     * @param groupe le groupe politique à supprimer
     * @param plugin plugin
     * @throws AppException si le groupe est référencé par un objet tiers
     */
    public void delete( GroupePolitique groupe, Plugin plugin )
        throws AppException
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, groupe.getIdGroupe(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * renvoie le groupe politique  correspondant à l'enregistrement dans la table ods_groupe ayant comme identifiant nKey
     * @param nKey idenfiant du groupe politique à supprimer
     * @param plugin plugin
     * @return objet GroupePolitique
     */
    public GroupePolitique load( int nKey, Plugin plugin )
    {
        GroupePolitique groupePolitique = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            groupePolitique = new GroupePolitique(  );
            groupePolitique.setIdGroupe( nKey );
            groupePolitique.setNomGroupe( daoUtil.getString( 1 ) );
            groupePolitique.setNomComplet( daoUtil.getString( 2 ) );
            groupePolitique.setActif( daoUtil.getBoolean( 3 ) );
        }

        daoUtil.free(  );

        return groupePolitique;
    }

    /**
     * renvoie la liste des groupes politiques présents dans la table ods_groupe
     * @param plugin plugin
     * @return liste d'objets GroupePolitique
     */
    public List<GroupePolitique> groupeList( Plugin plugin )
    {
        List<GroupePolitique> groupes = new ArrayList<GroupePolitique>(  );
        GroupePolitique groupePolitique = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GROUPE_LIST + SQL_QUERY_ORDER_BY_NOM_GROUPE, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            groupePolitique = new GroupePolitique(  );
            groupePolitique.setIdGroupe( daoUtil.getInt( 1 ) );
            groupePolitique.setNomGroupe( daoUtil.getString( 2 ) );
            groupePolitique.setNomComplet( daoUtil.getString( 3 ) );
            groupePolitique.setActif( daoUtil.getBoolean( 4 ) );
            groupes.add( groupePolitique );
        }

        daoUtil.free(  );

        return groupes;
    }

    /**
     * renvoie le Groupe politique de l'élu dont l'identifiant à été passé en paramètre
     * @param nKey identifiant de l'élu dont on recherche le groupe politique
     * @param plugin plugin
     * @return Objet GroupePolitique
     */
    public GroupePolitique selectByIdElu( int nKey, Plugin plugin )
    {
        GroupePolitique groupePolitique = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_ELU + SQL_QUERY_ORDER_BY_NOM_GROUPE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            groupePolitique = new GroupePolitique(  );
            groupePolitique.setIdGroupe( daoUtil.getInt( 1 ) );
            groupePolitique.setNomGroupe( daoUtil.getString( 2 ) );
            groupePolitique.setNomComplet( daoUtil.getString( 3 ) );
        }

        daoUtil.free(  );

        return groupePolitique;
    }

    /**
     * Retourne la liste des groupes politiques dont le statut est actif.
     * @param plugin plugin
     * @return la liste des groupes actifs
     */
    public List<GroupePolitique> selectGroupesActifs( Plugin plugin )
    {
        List<GroupePolitique> groupesActifs = new ArrayList<GroupePolitique>(  );
        GroupePolitique groupePolitique = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_LISTE_GROUPES_ACTIFS + SQL_QUERY_ORDER_BY_NOM_GROUPE, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            groupePolitique = new GroupePolitique(  );
            groupePolitique.setIdGroupe( daoUtil.getInt( 1 ) );
            groupePolitique.setNomGroupe( daoUtil.getString( 2 ) );
            groupePolitique.setNomComplet( daoUtil.getString( 3 ) );
            groupePolitique.setActif( true );
            groupesActifs.add( groupePolitique );
        }

        daoUtil.free(  );

        return groupesActifs;
    }
}
