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
package fr.paris.lutece.plugins.ods.business.relevetravaux;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * ReleveDesTravauxDAO
 */
public class ReleveDesTravauxDAO implements fr.paris.lutece.plugins.ods.business.relevetravaux.IReleveDesTravauxDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_releve ) FROM ods_releve_travaux ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_releve_travaux " +
        "			(id_releve, id_commission, id_seance, intitule, en_ligne)  " + "VALUES (?,?,?,?,?) ";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT releve.id_releve, releve.intitule, releve.en_ligne,  " +
        "       com.id_commission, com.numero_commission, com.libelle_commission,  " +
        "       seance.id_seance, seance.date_seance, seance.date_fin_seance  " + "FROM ods_releve_travaux releve " +
        "     LEFT JOIN ods_commission com ON (releve.id_commission=com.id_commission) " +
        "     LEFT JOIN ods_seance seance ON (releve.id_seance=seance.id_seance)   " + "WHERE releve.id_releve = ?";
    private static final String SQL_QUERY_RELEVE_LIST = "SELECT releve.id_releve, releve.intitule, releve.en_ligne," +
        "       com.id_commission,com.numero_commission,com.libelle_commission," +
        "       seance.id_seance,seance.date_seance, seance.date_fin_seance  " + "FROM ods_releve_travaux releve " +
        "     LEFT JOIN ods_commission com ON (releve.id_commission=com.id_commission) " +
        "     LEFT JOIN ods_seance seance ON (releve.id_seance=seance.id_seance)   ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM ods_releve_travaux WHERE id_releve = ?  ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ods_releve_travaux " +
        "SET id_commission=?, id_seance=?, intitule=?, en_ligne=? " + "WHERE id_releve=? ";
    private static final String SQL_QUERY_LISTE_RELEVES_EN_LIGNE = "SELECT releve.id_releve, releve.intitule, releve.en_ligne," +
        "       com.id_commission,com.numero_commission,com.libelle_commission," +
        "       seance.id_seance, seance.date_seance, seance.date_fin_seance    " + "FROM ods_releve_travaux releve " +
        "     LEFT JOIN ods_commission com ON (releve.id_commission=com.id_commission) " +
        "     LEFT JOIN ods_seance seance ON (releve.id_seance=seance.id_seance)   " + "WHERE releve.en_ligne = true";
    private static final String SQL_QUERY_FIND_BY_SEANCE = "SELECT releve.id_releve, releve.intitule, releve.en_ligne," +
        "       com.id_commission,com.numero_commission,com.libelle_commission  " + "FROM ods_releve_travaux releve " +
        "     LEFT JOIN ods_commission com ON (releve.id_commission=com.id_commission)   " +
        "WHERE releve.id_seance = ?";
    private static final String SQL_QUERY_ORDER_BY_NUM_COMMISSION = " ORDER BY com.numero_commission ASC ";
    private static final String SQL_QUERY_LISTE_RELEVES_BY_FILTER = "SELECT releve.id_releve, releve.intitule, releve.en_ligne," +
        "       com.id_commission,com.numero_commission,com.libelle_commission," +
        "       seance.id_seance, seance.date_seance, seance.date_fin_seance " + " FROM ods_releve_travaux releve " +
        "   LEFT JOIN ods_commission com ON ( releve.id_commission = com.id_commission ) " +
        "   LEFT JOIN ods_seance seance ON ( releve.id_seance = seance.id_seance ) " + " WHERE 1=1 ";
    private static final String SQL_QUERY_FILTER_BY_PUBLICATION = " AND releve.en_ligne = true ";
    private static final String SQL_QUERY_FILTER_BY_SEANCE = " AND releve.id_seance = ? ";
    private static final String SQL_QUERY_FILTER_BY_COMMISSION = " AND releve.id_commission = ? ";

    /**
     * Génère un nouvel identifiant
     * @param plugin le Plugin actif
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
     * Insère un relevé des travaux dans la base de données
     * @param releve le relevé des travaux
     * @param plugin le Plugin actif
     * @return l'identifiant du relevé de travaux inséré
     */
    public int insert( ReleveDesTravaux releve, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        int nKey = newPrimaryKey( plugin );
        daoUtil.setInt( 1, nKey );

        //si la commission = -1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs commission   
        if ( null != releve.getCommission(  ) )
        {
            daoUtil.setInt( 2, releve.getCommission(  ).getIdCommission(  ) );
        }
        else
        {
            daoUtil.setIntNull( 2 );
        }

        //si la séance = -1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs séance 
        if ( null != releve.getSeance(  ) )
        {
            daoUtil.setInt( 3, releve.getSeance(  ).getIdSeance(  ) );
        }
        else
        {
            daoUtil.setIntNull( 3 );
        }

        daoUtil.setString( 4, releve.getIntitule(  ) );
        daoUtil.setBoolean( 5, releve.isEnLigne(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        return nKey;
    }

    /**
     * Met à jour un relevé des travaux dans la base de données
     * @param releve le relevé des travaux
     * @param plugin le Plugin actif
     */
    public void store( ReleveDesTravaux releve, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        // si la commission = -1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs commission   
        if ( null != releve.getCommission(  ) )
        {
            daoUtil.setInt( 1, releve.getCommission(  ).getIdCommission(  ) );
        }
        else
        {
            daoUtil.setIntNull( 1 );
        }

        // si la séance = -1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs séance 
        if ( null != releve.getSeance(  ) )
        {
            daoUtil.setInt( 2, releve.getSeance(  ).getIdSeance(  ) );
        }
        else
        {
            daoUtil.setIntNull( 2 );
        }

        daoUtil.setString( 3, releve.getIntitule(  ) );
        daoUtil.setBoolean( 4, releve.isEnLigne(  ) );
        daoUtil.setInt( 5, releve.getIdReleveDesTravaux(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime un relevé de travaux de la base de données
     * @param releve le relevé des travaux à supprimer
     * @param plugin le Plugin actif
     * @throws AppException SQLException si le relevé de travaux ne peut être supprimé
     */
    public void delete( ReleveDesTravaux releve, Plugin plugin )
        throws AppException
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, releve.getIdReleveDesTravaux(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Charge le relevé des travaux ayant un identifiant donné
     * @param nKey l'identifiant du relevé de travaux
     * @param plugin le Plugin actif
     * @return un relevé de travaux
     */
    public ReleveDesTravaux load( int nKey, Plugin plugin )
    {
        ReleveDesTravaux releve = null;
        Commission commission = null;
        Seance seance = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            releve = new ReleveDesTravaux(  );

            releve.setIdReleveDesTravaux( daoUtil.getInt( 1 ) );
            releve.setIntitule( daoUtil.getString( 2 ) );
            releve.setEnLigne( daoUtil.getBoolean( 3 ) );

            if ( null != daoUtil.getObject( 4 ) )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 4 ) );
                commission.setNumero( daoUtil.getInt( 5 ) );
                commission.setLibelle( daoUtil.getString( 6 ) );
                releve.setCommission( commission );
            }

            if ( null != daoUtil.getObject( 7 ) )
            {
                seance = new Seance(  );
                seance.setIdSeance( daoUtil.getInt( 7 ) );
                seance.setDateSeance( daoUtil.getTimestamp( 8 ) );
                seance.setDateCloture( daoUtil.getTimestamp( 9 ) );
                releve.setSeance( seance );
            }
        }

        daoUtil.free(  );

        return releve;
    }

    /**
     * @see fr.paris.lutece.plugins.ods.business.relevetravaux.IReleveDesTravauxDAO#selectReleveDesTravauxList(fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public List<ReleveDesTravaux> selectReleveDesTravauxList( Plugin plugin )
    {
        List<ReleveDesTravaux> releves = new ArrayList<ReleveDesTravaux>(  );
        Commission commission = null;
        Seance seance = null;
        ReleveDesTravaux releve = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_RELEVE_LIST + SQL_QUERY_ORDER_BY_NUM_COMMISSION, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            releve = new ReleveDesTravaux(  );

            releve.setIdReleveDesTravaux( daoUtil.getInt( 1 ) );
            releve.setIntitule( daoUtil.getString( 2 ) );
            releve.setEnLigne( daoUtil.getBoolean( 3 ) );

            if ( null != daoUtil.getObject( 4 ) )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 4 ) );
                commission.setNumero( daoUtil.getInt( 5 ) );
                commission.setLibelle( daoUtil.getString( 6 ) );
                releve.setCommission( commission );
            }

            if ( null != daoUtil.getObject( 7 ) )
            {
                seance = new Seance(  );
                seance.setIdSeance( daoUtil.getInt( 7 ) );
                seance.setDateSeance( daoUtil.getTimestamp( 8 ) );
                seance.setDateCloture( daoUtil.getTimestamp( 9 ) );
                releve.setSeance( seance );
            }

            //on ajoute le relevé à la liste des relevés de travaux
            releves.add( releve );
        }

        daoUtil.free(  );

        return releves;
    }

    /**
     * @see fr.paris.lutece.plugins.ods.business.relevetravaux.IReleveDesTravauxDAO#selectReleveDesTravauxEnLigne(fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public List<ReleveDesTravaux> selectReleveDesTravauxEnLigne( Plugin plugin )
    {
        List<ReleveDesTravaux> releves = new ArrayList<ReleveDesTravaux>(  );
        Commission commission = null;
        Seance seance = null;
        ReleveDesTravaux releve = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_LISTE_RELEVES_EN_LIGNE + SQL_QUERY_ORDER_BY_NUM_COMMISSION, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            releve = new ReleveDesTravaux(  );

            releve.setIdReleveDesTravaux( daoUtil.getInt( 1 ) );
            releve.setIntitule( daoUtil.getString( 2 ) );
            releve.setEnLigne( daoUtil.getBoolean( 3 ) );

            if ( null != daoUtil.getObject( 4 ) )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 4 ) );
                commission.setNumero( daoUtil.getInt( 5 ) );
                commission.setLibelle( daoUtil.getString( 6 ) );
                releve.setCommission( commission );
            }

            if ( null != daoUtil.getObject( 7 ) )
            {
                seance = new Seance(  );
                seance.setIdSeance( daoUtil.getInt( 7 ) );
                seance.setDateSeance( daoUtil.getTimestamp( 8 ) );
                seance.setDateCloture( daoUtil.getTimestamp( 9 ) );
                releve.setSeance( seance );
            }

            //on ajoute le relevé à la liste des relevés de travaux
            releves.add( releve );
        }

        daoUtil.free(  );

        return releves;
    }

    /**
     * @see fr.paris.lutece.plugins.ods.business.relevetravaux.IReleveDesTravauxDAO#selectReleveDesTravauxSeance(int, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public List<ReleveDesTravaux> selectReleveDesTravauxSeance( Seance seance, Plugin plugin )
    {
        List<ReleveDesTravaux> releves = new ArrayList<ReleveDesTravaux>(  );

        Commission commission = null;
        ReleveDesTravaux releve = null;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_SEANCE + SQL_QUERY_ORDER_BY_NUM_COMMISSION, plugin );
        daoUtil.setInt( 1, seance.getIdSeance(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            releve = new ReleveDesTravaux(  );

            releve.setIdReleveDesTravaux( daoUtil.getInt( 1 ) );
            releve.setIntitule( daoUtil.getString( 2 ) );
            releve.setEnLigne( daoUtil.getBoolean( 3 ) );

            if ( null != daoUtil.getObject( 4 ) )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 4 ) );
                commission.setNumero( daoUtil.getInt( 5 ) );
                commission.setLibelle( daoUtil.getString( 6 ) );
                releve.setCommission( commission );
            }

            releve.setSeance( seance );
            releves.add( releve );
        }

        daoUtil.free(  );

        return releves;
    }

    /**
     * @see fr.paris.lutece.plugins.ods.business.relevetravaux.IReleveDesTravauxDAO#selectReleveDesTravauxByFilter(int, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public List<ReleveDesTravaux> selectReleveDesTravauxByFilter( ReleveDesTravauxFilter filter, Plugin plugin )
    {
        List<ReleveDesTravaux> releves = new ArrayList<ReleveDesTravaux>(  );

        Seance seance = null;
        Commission commission = null;
        ReleveDesTravaux releve = null;

        String strSQL = SQL_QUERY_LISTE_RELEVES_BY_FILTER;
        strSQL += ( ( filter.containsIdPublieCriteria(  ) ) ? SQL_QUERY_FILTER_BY_PUBLICATION
                                                            : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdSeanceCriteria(  ) ) ? SQL_QUERY_FILTER_BY_SEANCE
                                                            : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdCommissionCriteria(  ) ) ? SQL_QUERY_FILTER_BY_COMMISSION
                                                                : OdsConstants.CONSTANTE_CHAINE_VIDE );

        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );
        int nIndex = 1;

        if ( filter.containsIdSeanceCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdSeance(  ) );
            nIndex++;
        }

        if ( filter.containsIdCommissionCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdCommission(  ) );
            nIndex++;
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            releve = new ReleveDesTravaux(  );

            releve.setIdReleveDesTravaux( daoUtil.getInt( 1 ) );
            releve.setIntitule( daoUtil.getString( 2 ) );
            releve.setEnLigne( daoUtil.getBoolean( 3 ) );

            if ( null != daoUtil.getObject( 4 ) )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 4 ) );
                commission.setNumero( daoUtil.getInt( 5 ) );
                commission.setLibelle( daoUtil.getString( 6 ) );
                releve.setCommission( commission );
            }

            if ( null != daoUtil.getObject( 7 ) )
            {
                seance = new Seance(  );
                seance.setIdSeance( daoUtil.getInt( 7 ) );
                seance.setDateSeance( daoUtil.getTimestamp( 8 ) );
                seance.setDateCloture( daoUtil.getTimestamp( 9 ) );
                releve.setSeance( seance );
            }

            releves.add( releve );
        }

        daoUtil.free(  );

        return releves;
    }
}
