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
package fr.paris.lutece.plugins.ods.business.commission;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * CommissionDAO
 */
public class CommissionDAO implements ICommissionDAO
{
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_commission ) FROM ods_commission ";
    private static final String SQL_QUERY_COMMISSION_LIST = " SELECT id_commission, numero_commission,libelle_commission, actif FROM ods_commission  ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO ods_commission(id_commission, numero_commission, libelle_commission, actif) VALUES ( ?, ?, ?, ?) ";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = " SELECT id_commission, numero_commission,libelle_commission, actif FROM ods_commission WHERE id_commission = ? ";
    private static final String SQL_QUERY_FIND_BY_NUMERO_COMMISSION = " SELECT id_commission, numero_commission, libelle_commission, actif FROM ods_commission WHERE numero_commission = ? ";
    private static final String SQL_QUERY_FIND_ACTIVES = " SELECT id_commission, numero_commission, libelle_commission FROM ods_commission WHERE actif=true";
    private static final String SQL_QUERY_UPDATE = " UPDATE ods_commission SET numero_commission = ?, libelle_commission = ?, actif=? WHERE id_commission= ?";
    private static final String SQL_QUERY_DELETE = " DELETE FROM ods_commission WHERE id_commission = ?  ";
    private static final String SQL_QUERY_ORDER_BY_NUMERO_COMMISSION = " ORDER BY numero_commission ";

    /**
    * Insere une nouvelle commission à partir de l’objet commission passé en paramètre
    * @param commission la commission à insérer
    * @param plugin le Plugin
    */
    public void insert( Commission commission, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        commission.setIdCommission( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, commission.getIdCommission(  ) );
        daoUtil.setInt( 2, commission.getNumero(  ) );
        daoUtil.setString( 3, commission.getLibelle(  ) );
        daoUtil.setBoolean( 4, commission.isActif(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Modifie la commission correspondant à l’objet commission passé en paramètre
     * @param commission la commission à modifier
     * @param plugin le Plugin
     */
    public void store( Commission commission, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, commission.getNumero(  ) );
        daoUtil.setString( 2, commission.getLibelle(  ) );
        daoUtil.setBoolean( 3, commission.isActif(  ) );
        daoUtil.setInt( 4, commission.getIdCommission(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime la commission correspondant à  l’objet commission passé en paramètre
     * @param commission la commission à supprimer
     * @param plugin le Plugin
     * @throws AppException si contrainte de clé étrangère
     */
    public void delete( Commission commission, Plugin plugin )
        throws AppException
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, commission.getIdCommission(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * renvoie la commission ayant comme identifiant nKey
     * @param nKey idenfiant de la commission à supprimer
     * @param plugin le Plugin
     * @return objet Commission
     */
    public Commission load( int nKey, Plugin plugin )
    {
        Commission commission = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            commission = new Commission(  );
            commission.setIdCommission( daoUtil.getInt( 1 ) );
            commission.setNumero( daoUtil.getInt( 2 ) );
            commission.setLibelle( daoUtil.getString( 3 ) );
            commission.setActif( daoUtil.getBoolean( 4 ) );
        }

        daoUtil.free(  );

        return commission;
    }

    /**
     * renvoie la liste des commissions dont la date de fin est supérieur ou égale a la date du jour
     * @param plugin le Plugin
     * @return liste d'objets Commission
     */
    public Commission loadByNumeroCommission( int nNumeroCommission, Plugin plugin )
    {
        Commission commission = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_NUMERO_COMMISSION + SQL_QUERY_ORDER_BY_NUMERO_COMMISSION,
                plugin );
        daoUtil.setInt( 1, nNumeroCommission );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            commission = new Commission(  );
            commission.setIdCommission( daoUtil.getInt( 1 ) );
            commission.setNumero( daoUtil.getInt( 2 ) );
            commission.setLibelle( daoUtil.getString( 3 ) );
            commission.setActif( daoUtil.getBoolean( 4 ) );
        }

        daoUtil.free(  );

        return commission;
    }

    /**
     * Renvoie la commission qui a pour numéro le nNumeroCommission passé en paramètre
     * @param nNumeroCommission numero de la commission recherchée
     * @param plugin le Plugin
     * @return objet Commission
     */
    public List<Commission> commissionList( Plugin plugin )
    {
        List<Commission> commissions = new ArrayList<Commission>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COMMISSION_LIST + SQL_QUERY_ORDER_BY_NUMERO_COMMISSION, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Commission commission = new Commission(  );
            commission.setIdCommission( daoUtil.getInt( 1 ) );
            commission.setNumero( daoUtil.getInt( 2 ) );
            commission.setLibelle( daoUtil.getString( 3 ) );
            commission.setActif( daoUtil.getBoolean( 4 ) );

            commissions.add( commission );
        }

        daoUtil.free(  );

        return commissions;
    }

    /**
     * Génère un nouvel identifiant
     * @param plugin le Plugin
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
     * Retourne la liste des commissions actives.
     * @param plugin le plugin actif
     * @return la liste des commissions actives
     */
    public List<Commission> loadListCommissionsActives( Plugin plugin )
    {
        List<Commission> listCommission = new ArrayList<Commission>(  );
        Commission commission;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ACTIVES + SQL_QUERY_ORDER_BY_NUMERO_COMMISSION, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            commission = new Commission(  );
            commission.setIdCommission( daoUtil.getInt( 1 ) );
            commission.setNumero( Integer.parseInt( daoUtil.getString( 2 ) ) );
            commission.setLibelle( daoUtil.getString( 3 ) );
            listCommission.add( commission );
        }

        daoUtil.free(  );

        return listCommission;
    }
}
