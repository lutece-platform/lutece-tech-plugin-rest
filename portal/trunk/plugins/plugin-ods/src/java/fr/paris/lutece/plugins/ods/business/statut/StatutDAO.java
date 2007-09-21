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
package fr.paris.lutece.plugins.ods.business.statut;

import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * StatutDAO
 */
public class StatutDAO implements IStatutDAO
{
    /*
     * Requetes SQL
     */
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_statut ) FROM ods_statut ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_statut( " +
        "			id_statut, libelle_statut, est_pour_pdd, est_pour_voeu, est_pour_amendement " + "VALUES (?,?,?,?,?) ";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT " +
        "	id_statut, libelle_statut, est_pour_pdd, est_pour_voeu, est_pour_amendement " + " FROM ods_statut " +
        " WHERE id_statut = ? ";
    private static final String SQL_QUERY_STATUT_LIST = "SELECT " +
        "	id_statut, libelle_statut, est_pour_pdd, est_pour_voeu, est_pour_amendement " + " FROM ods_statut ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM ods_statut WHERE id_statut = ?  ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ods_statut " +
        " SET libelle_statut = ?, est_pour_pdd = ?, est_pour_voeu = ?, est_pour_amendement= ? ";
    private static final String SQL_QUERY_STATUT_LIST_BY_FILTER = "SELECT " +
        "	id_statut, libelle_statut, est_pour_pdd, est_pour_voeu, est_pour_amendement " + " FROM ods_statut " +
        " WHERE 1=1 ";
    private static final String SQL_QUERY_FILTER_BY_POUR_PDD = " AND est_pour_pdd = true ";
    private static final String SQL_QUERY_FILTER_BY_POUR_VOEU = " AND est_pour_voeu = true ";
    private static final String SQL_QUERY_FILTER_BY_POUR_AMENDEMENT = " AND est_pour_amendement = true ";
    private static final String SQL_QUERY_ORDER_BY_LIBELLE = " ORDER BY libelle_statut ASC ";

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
     * Supprime le statut correspondant à l’objet statut passé en paramètre
     * @param statut le statut à supprimer
     * @param plugin Plugin
     * @throws AppException exception levé si une dependance existe
     */
    public void delete( Statut statut, Plugin plugin )
        throws AppException
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, statut.getIdStatut(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.ods.business.statut.IStatutDAO#insert(fr.paris.lutece.plugins.ods.business.statut.Statut, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void insert( Statut statut, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        int nKey = newPrimaryKey( plugin );

        daoUtil.setInt( 1, nKey );
        daoUtil.setString( 2, statut.getLibelle(  ) );
        daoUtil.setBoolean( 3, statut.estPourPDD(  ) );
        daoUtil.setBoolean( 4, statut.estPourVoeu(  ) );
        daoUtil.setBoolean( 5, statut.estPourAmendement(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.ods.business.statut.IStatutDAO#load(int, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public Statut load( int nKey, Plugin plugin )
    {
        Statut statut = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            statut = new Statut(  );

            statut.setIdStatut( daoUtil.getInt( 1 ) );
            statut.setLibelle( daoUtil.getString( 2 ) );
            statut.setPourPDD( daoUtil.getBoolean( 3 ) );
            statut.setPourVoeu( daoUtil.getBoolean( 4 ) );
            statut.setPourAmendement( daoUtil.getBoolean( 5 ) );
        }

        daoUtil.free(  );

        return statut;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.ods.business.statut.IStatutDAO#selectStatutListWithFilter(fr.paris.lutece.plugins.ods.business.statut.StatutFilter, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public List<Statut> selectStatutListWithFilter( StatutFilter filter, Plugin plugin )
    {
        List<Statut> listStatuts = new ArrayList<Statut>(  );
        Statut statut = null;

        String strSQL = SQL_QUERY_STATUT_LIST_BY_FILTER;
        strSQL += ( ( filter.containsPourPDDCriteria(  ) ) ? SQL_QUERY_FILTER_BY_POUR_PDD
                                                           : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsPourVoeuCriteria(  ) ) ? SQL_QUERY_FILTER_BY_POUR_VOEU
                                                            : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsPourAmendementCriteria(  ) ) ? SQL_QUERY_FILTER_BY_POUR_AMENDEMENT
                                                                  : OdsConstants.CONSTANTE_CHAINE_VIDE );

        DAOUtil daoUtil = new DAOUtil( strSQL + SQL_QUERY_ORDER_BY_LIBELLE, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            statut = new Statut(  );

            statut.setIdStatut( daoUtil.getInt( 1 ) );
            statut.setLibelle( daoUtil.getString( 2 ) );
            statut.setPourPDD( daoUtil.getBoolean( 3 ) );
            statut.setPourVoeu( daoUtil.getBoolean( 4 ) );
            statut.setPourAmendement( daoUtil.getBoolean( 5 ) );

            listStatuts.add( statut );
        }

        daoUtil.free(  );

        return listStatuts;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.ods.business.statut.IStatutDAO#selectStatutList(fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public List<Statut> selectStatutList( Plugin plugin )
    {
        List<Statut> listStatuts = new ArrayList<Statut>(  );
        Statut statut;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_STATUT_LIST + SQL_QUERY_ORDER_BY_LIBELLE, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            statut = new Statut(  );

            statut.setIdStatut( daoUtil.getInt( 1 ) );
            statut.setLibelle( daoUtil.getString( 2 ) );
            statut.setPourPDD( daoUtil.getBoolean( 3 ) );
            statut.setPourVoeu( daoUtil.getBoolean( 4 ) );
            statut.setPourAmendement( daoUtil.getBoolean( 5 ) );

            listStatuts.add( statut );
        }

        daoUtil.free(  );

        return listStatuts;
    }

    /* (non-Javadoc)
     * @see fr.paris.lutece.plugins.ods.business.statut.IStatutDAO#store(fr.paris.lutece.plugins.ods.business.statut.Statut, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void store( Statut statut, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setString( 1, statut.getLibelle(  ) );
        daoUtil.setBoolean( 2, statut.estPourPDD(  ) );
        daoUtil.setBoolean( 3, statut.estPourVoeu(  ) );
        daoUtil.setBoolean( 4, statut.estPourAmendement(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
