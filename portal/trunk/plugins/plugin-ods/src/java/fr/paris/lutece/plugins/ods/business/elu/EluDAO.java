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
package fr.paris.lutece.plugins.ods.business.elu;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitique;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * EluDao
 *
 */
public class EluDAO implements fr.paris.lutece.plugins.ods.business.elu.IEluDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_elu ) FROM ods_elu ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_elu( " +
        "			id_elu, id_groupe, id_commission, ods_id_elu, " + "			civilite, nom_elu, prenom_elu, actif) " +
        "VALUES (?,?,?,?,?,?,?,?) ";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT " +
        "	elu.id_elu,elu.civilite, elu.nom_elu,elu.prenom_elu,elu.actif,elu.ods_id_elu, " +
        "	com.id_commission,com.numero_commission,com.libelle_commission," +
        "	grou.id_groupe,grou.nom_groupe,grou.nom_complet " + "FROM ods_elu elu LEFT JOIN ods_commission com " +
        "ON (elu.id_commission=com.id_commission) LEFT JOIN ods_groupe grou on (elu.id_groupe=grou.id_groupe) WHERE elu.id_elu = ? ";
    private static final String SQL_QUERY_ELU_LIST = "SELECT elu.id_elu, elu.civilite, elu.nom_elu, elu.prenom_elu, elu.actif, " +
        "		com.id_commission,com.numero_commission,com.libelle_commission, " +
        "		grou.id_groupe,grou.nom_groupe,grou.nom_complet " + "FROM ods_elu elu " +
        "	LEFT JOIN ods_commission com on (elu.id_commission=com.id_commission) " +
        "	LEFT JOIN ods_groupe grou on (elu.id_groupe=grou.id_groupe) " + "WHERE  1 ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM ods_elu WHERE id_elu = ?  ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ods_elu SET id_groupe=?, id_commission=?, ods_id_elu=?, " +
        "		civilite=?, nom_elu=?, prenom_elu=?, actif=? " + "WHERE id_elu=? ";
    private static final String SQL_QUERY_ELU_LIST_BY_ID_GROUPE = "SELECT elu.id_elu,elu.civilite,elu.nom_elu,elu.prenom_elu, elu.actif," +
        "		com.id_commission,com.numero_commission,com.libelle_commission," +
        "		grou.id_groupe,grou.nom_groupe,grou.nom_complet " + "FROM ods_elu elu " +
        "	LEFT JOIN ods_commission com on (elu.id_commission=com.id_commission) " +
        "	LEFT JOIN ods_groupe grou on (elu.id_groupe=grou.id_groupe) " + "WHERE elu.id_groupe=? ";
    private static final String SQL_QUERY_LISTE_ELUS_ACTIFS = "SELECT elu.id_elu, elu.civilite, elu.nom_elu, elu.prenom_elu, " +
        "		com.id_commission,com.numero_commission,com.libelle_commission, " +
        "		grou.id_groupe,grou.nom_groupe,grou.nom_complet " + "FROM ods_elu elu " +
        "	LEFT JOIN ods_commission com on (elu.id_commission=com.id_commission) " +
        "	LEFT JOIN ods_groupe grou on (elu.id_groupe=grou.id_groupe) " + "WHERE elu.actif=true";
    private static final String SQL_QUERY_ORDER_BY_NOM_ELU = " ORDER BY elu.nom_elu ASC ";
    private static final String SQL_QUERY_FIND_ALL_ID_REMPLACE = "SELECT ods_id_elu FROM ods_elu GROUP BY ods_id_elu";
    private static final String SQL_QUERY_ELU_RAPPORTEUR_LIST = "SELECT elu.id_elu, elu.civilite, elu.nom_elu, elu.prenom_elu " +
        "FROM ods_elu elu WHERE elu.id_commission IS NOT NULL ";
    private static final String SQL_QUERY_CLAUSE_ELU_ACTIF = " AND elu.actif = true";

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
     * Insère un nouvel enregistrement dans la table ods_elu à partir de l’objet elu passé en paramètre
     * @param elu  l'élu  à insérer dans la table ods_elu
     * @param plugin plugin
     */
    public void insert( Elu elu, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( 1, newPrimaryKey( plugin ) );
        daoUtil.setInt( 2, elu.getGroupe(  ).getIdGroupe(  ) );

        //si la commission =-1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs commission   
        if ( null != elu.getCommission(  ) )
        {
            daoUtil.setInt( 3, elu.getCommission(  ).getIdCommission(  ) );
        }
        else
        {
            daoUtil.setIntNull( 3 );
        }

        //si la commission =-1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs commission 
        if ( null != elu.getEluRemplace(  ) )
        {
            daoUtil.setInt( 4, elu.getEluRemplace(  ).getIdElu(  ) );
        }
        else
        {
            daoUtil.setIntNull( 4 );
        }

        daoUtil.setString( 5, elu.getCivilite(  ) );
        daoUtil.setString( 6, elu.getNomElu(  ) );
        daoUtil.setString( 7, elu.getPrenomElu(  ) );
        daoUtil.setBoolean( 8, elu.isActif(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Modifie l'enregistrement dans la table ods_elu correspondant à  l’objet élu passé en paramètre
     * @param elu l'élu à modifier dans la table ods_élu
     * @param plugin plugin
     */
    public void store( Elu elu, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, elu.getGroupe(  ).getIdGroupe(  ) );

        // si la commission =-1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs commission   
        if ( null != elu.getCommission(  ) )
        {
            daoUtil.setInt( 2, elu.getCommission(  ).getIdCommission(  ) );
        }
        else
        {
            daoUtil.setIntNull( 2 );
        }

        //si la commission =-1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs commission 
        if ( null != elu.getEluRemplace(  ) )
        {
            daoUtil.setInt( 3, elu.getEluRemplace(  ).getIdElu(  ) );
        }
        else
        {
            daoUtil.setIntNull( 3 );
        }

        daoUtil.setString( 4, elu.getCivilite(  ) );
        daoUtil.setString( 5, elu.getNomElu(  ) );
        daoUtil.setString( 6, elu.getPrenomElu(  ) );
        daoUtil.setBoolean( 7, elu.isActif(  ) );
        daoUtil.setInt( 8, elu.getIdElu(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime l' enregistrement dans la table ods_elu correspondant à  l’objet elu passé en paramètre
     * @param elu l'élu à supprimer dans la table ods_élu
     * @param plugin plugin
     * @throws AppException si l'élu est référencé par un objet tiers
     */
    public void delete( Elu elu, Plugin plugin ) throws AppException
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, elu.getIdElu(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne l'élu correspondant à l'enregistrement dans la table ods_elu ayant comme identifiant nKey
     * @param nKey l'identifiant de l'élu
     * @param plugin plugin
     * @return  l'élu correspondant à l'enregistrement dans la table ods_elu ayant comme identifiant nKey
     */
    public Elu load( int nKey, Plugin plugin )
    {
        Elu elu = null;
        Commission commission = null;
        GroupePolitique groupePolitique = null;
        Elu eluRemplace = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            elu = new Elu(  );
            elu.setIdElu( daoUtil.getInt( 1 ) );
            elu.setCivilite( daoUtil.getString( 2 ) );
            elu.setNomElu( daoUtil.getString( 3 ) );
            elu.setPrenomElu( daoUtil.getString( 4 ) );
            elu.setActif( daoUtil.getBoolean( 5 ) );

            // si l'elu a un remplacant on récupère son id
            if ( null != daoUtil.getObject( 6 ) )
            {
                eluRemplace = new Elu(  );
                eluRemplace.setIdElu( daoUtil.getInt( 6 ) );
                elu.setEluRemplace( eluRemplace );
            }

            //si l'elu a une commission on la recupere en creant un objet commission et on l'affecte à l'objet elu
            if ( null != daoUtil.getObject( 7 ) )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 7 ) );
                commission.setNumero( daoUtil.getInt( 8 ) );
                commission.setLibelle( daoUtil.getString( 9 ) );
                elu.setCommission( commission );
            }

            //on recupere le groupe politique de l'élu en créant un objet GroupePolitique et on l'affecte à l'objet elu
            groupePolitique = new GroupePolitique(  );
            groupePolitique.setIdGroupe( daoUtil.getInt( 10 ) );
            groupePolitique.setNomGroupe( daoUtil.getString( 11 ) );
            groupePolitique.setNomComplet( daoUtil.getString( 12 ) );
            elu.setGroupe( groupePolitique );
        }

        daoUtil.free(  );

        return elu;
    }

    /**
     * Retourne la liste des élus présents dans la table ods_elu
     * @param plugin plugin
     * @return liste d'objets Elu
     */
    public List<Elu> selectEluList( Plugin plugin )
    {
        List<Elu> elus = new ArrayList<Elu>(  );
        Commission commission = null;
        GroupePolitique groupePolitique = null;
        Elu elu = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ELU_LIST + SQL_QUERY_ORDER_BY_NOM_ELU, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            elu = new Elu(  );
            elu.setIdElu( daoUtil.getInt( 1 ) );
            elu.setCivilite( daoUtil.getString( 2 ) );
            elu.setNomElu( daoUtil.getString( 3 ) );
            elu.setPrenomElu( daoUtil.getString( 4 ) );
            elu.setActif( daoUtil.getBoolean( 5 ) );

            //si l'elu a une commission on la recupere en creant un objet commission et on l'affecte à l'objet elu
            if ( null != daoUtil.getObject( 6 ) )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 6 ) );
                commission.setNumero( daoUtil.getInt( 7 ) );
                commission.setLibelle( daoUtil.getString( 8 ) );
                elu.setCommission( commission );
            }

            //on recupere le groupe politique de l'élu en créant un objet GroupePolitique et on l'affecte à l'objet elu
            groupePolitique = new GroupePolitique(  );
            groupePolitique.setIdGroupe( daoUtil.getInt( 9 ) );
            groupePolitique.setNomGroupe( daoUtil.getString( 10 ) );
            groupePolitique.setNomComplet( daoUtil.getString( 11 ) );
            elu.setGroupe( groupePolitique );
            //on ajoute l'objet elu a la liste des elus
            elus.add( elu );
        }

        daoUtil.free(  );

        return elus;
    }

    /**
     * Retourne la liste des élus ayant un groupe politique dont l'identifiant est égal à nKey
     * @param nKey  identifiant du groupe politique
     * @param plugin plugin
     * @return la liste des élus ayant un groupe politique dont l'identifiant est égal à nKey
     */
    public List<Elu> selectEluListbyIdGroupe( int nKey, Plugin plugin )
    {
        List<Elu> elus = new ArrayList<Elu>(  );
        Commission commission = null;
        GroupePolitique groupePolitique = null;
        Elu elu = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ELU_LIST_BY_ID_GROUPE + SQL_QUERY_ORDER_BY_NOM_ELU, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            elu = new Elu(  );
            elu.setIdElu( daoUtil.getInt( 1 ) );
            elu.setCivilite( daoUtil.getString( 2 ) );
            elu.setNomElu( daoUtil.getString( 3 ) );
            elu.setPrenomElu( daoUtil.getString( 4 ) );
            elu.setActif( daoUtil.getBoolean( 5 ) );

            //si l'elu a une commission on la recupere en creant un objet commission et on l'affecte à l'objet elu
            if ( null != daoUtil.getObject( 6 ) )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 6 ) );
                commission.setNumero( daoUtil.getInt( 7 ) );
                commission.setLibelle( daoUtil.getString( 8 ) );
                elu.setCommission( commission );
            }

            //on recupere le groupe politique de l'élu en créant un objet GroupePolitique et on l'affecte à l'objet elu
            groupePolitique = new GroupePolitique(  );
            groupePolitique.setIdGroupe( daoUtil.getInt( 9 ) );
            groupePolitique.setNomGroupe( daoUtil.getString( 10 ) );
            groupePolitique.setNomComplet( daoUtil.getString( 11 ) );
            elu.setGroupe( groupePolitique );
            //on ajoute l'objet elu a la liste des elus
            elus.add( elu );
        }

        daoUtil.free(  );

        return elus;
    }

    /**
     * Retourne la liste des élus dont le statut est actif.
     * @param plugin plugin
     * @return la liste des élus actifs
     */
    public List<Elu> selectEluActifs( Plugin plugin )
    {
        List<Elu> elus = new ArrayList<Elu>(  );
        Commission commission = null;
        GroupePolitique groupePolitique = null;
        Elu elu = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_LISTE_ELUS_ACTIFS + SQL_QUERY_ORDER_BY_NOM_ELU, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            elu = new Elu(  );
            elu.setIdElu( daoUtil.getInt( 1 ) );
            elu.setCivilite( daoUtil.getString( 2 ) );
            elu.setNomElu( daoUtil.getString( 3 ) );
            elu.setPrenomElu( daoUtil.getString( 4 ) );
            elu.setActif( true );

            //si l'elu a une commission on la recupere en creant un objet commission et on l'affecte à l'objet elu
            if ( null != daoUtil.getObject( 5 ) )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 5 ) );
                commission.setNumero( daoUtil.getInt( 6 ) );
                commission.setLibelle( daoUtil.getString( 7 ) );
                elu.setCommission( commission );
            }

            //on recupere le groupe politique de l'élu en créant un objet GroupePolitique et on l'affecte à l'objet elu
            groupePolitique = new GroupePolitique(  );
            groupePolitique.setIdGroupe( daoUtil.getInt( 8 ) );
            groupePolitique.setNomGroupe( daoUtil.getString( 9 ) );
            groupePolitique.setNomComplet( daoUtil.getString( 10 ) );
            elu.setGroupe( groupePolitique );
            //on ajoute l'objet elu a la liste des elus
            elus.add( elu );
        }

        daoUtil.free(  );

        return elus;
    }

    /**
     * Retourne la liste des élus actifs remplaçants d'autres élus
     * @param plugin le plugin
     * @return la liste des élus actifs remplaçants d'autres élus
     */
    public List<Elu> selectEluRemplacantsActifs( Plugin plugin )
    {
        List<Elu> listElus = new ArrayList<Elu>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL_ID_REMPLACE, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Elu elu = load( daoUtil.getInt( 1 ), plugin );

            if ( ( elu != null ) && elu.isActif(  ) )
            {
                listElus.add( elu );
            }
        }

        daoUtil.free(  );

        return listElus;
    }

    /**
     * Retourne la liste des élus actifs rapporteurs dans une commission
     * @param plugin le plugin
     * @return la liste des rapporteurs
     */
    public List<Elu> selectElusRapporteursActifs( Plugin plugin )
    {
        List<Elu> listElus = new ArrayList<Elu>(  );
        Elu elu;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ELU_RAPPORTEUR_LIST + SQL_QUERY_CLAUSE_ELU_ACTIF, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            elu = new Elu(  );
            elu.setIdElu( daoUtil.getInt( 1 ) );
            elu.setCivilite( daoUtil.getString( 2 ) );
            elu.setNomElu( daoUtil.getString( 3 ) );
            elu.setPrenomElu( daoUtil.getString( 4 ) );
            elu.setActif( true );
            listElus.add( elu );
        }

        daoUtil.free(  );

        return listElus;
    }

    /**
     * Retourne la liste de tous les élus rapporteurs d'une commission
     * @param plugin le plugin
     * @return la liste des élus rapporteurs
     */
    public List<Elu> selectElusRapporteurs( Plugin plugin )
    {
        List<Elu> listElus = new ArrayList<Elu>(  );
        Elu elu;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ELU_RAPPORTEUR_LIST, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            elu = new Elu(  );
            elu.setIdElu( daoUtil.getInt( 1 ) );
            elu.setCivilite( daoUtil.getString( 2 ) );
            elu.setNomElu( daoUtil.getString( 3 ) );
            elu.setPrenomElu( daoUtil.getString( 4 ) );
            listElus.add( elu );
        }

        daoUtil.free(  );

        return listElus;
    }
}
