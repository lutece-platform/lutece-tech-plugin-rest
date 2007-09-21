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
package fr.paris.lutece.plugins.ods.business.tourniquet;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * TourniquetDAO
 */
public class TourniquetDAO implements ITourniquetDAO
{
    private static final String SQL_QUERY_DELETE = "DELETE FROM ods_tourniquet WHERE id_commission=?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_tourniquet(" + "	id_commission, num_ordre) " +
        "VALUES(?,?)";
    private static final String SQL_QUERY_UPDATE = "UPDATE ods_tourniquet " + "SET num_ordre=? " +
        "WHERE id_commission=?";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT t.num_ordre, c.numero_commission " +
        "FROM ods_tourniquet t, ods_commission c " + "WHERE 	t.id_commission=? AND " +
        "		c.id_commission=t.id_commission";
    private static final String SQL_QUERY_LOAD_TOURNIQUET = "SELECT t.id_commission, c.numero_commission " +
        "FROM ods_tourniquet t, ods_commission c " + "WHERE c.id_commission=t.id_commission " + "ORDER BY t.num_ordre";
    private static final String SQL_QUERY_FLUSH = "DELETE FROM ods_tourniquet";

    /**
     * Supprime de la table ods_tourniquet l'élément du tourniquet passé en argument.
     * @param tourniquet l'élément à supprimer
     * @param plugin le Plugin actif
     */
    public void delete( Tourniquet tourniquet, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, tourniquet.getCommission(  ).getIdCommission(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Rajoute une entrée dans le tourniquet.
     * @param tourniquet le nouvel élément
     * @param plugin le Plugin actif
     */
    public void insert( Tourniquet tourniquet, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( 1, tourniquet.getCommission(  ).getIdCommission(  ) );
        daoUtil.setInt( 2, tourniquet.getNumeroOrdre(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Met à jour les informations concernant le tourniquet passé en argument.
     * @param tourniquet les nouvelles informations sur le tourniquet
     * @param plugin le Plugin actif
     */
    public void store( Tourniquet tourniquet, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, tourniquet.getNumeroOrdre(  ) );
        daoUtil.setInt( 2, tourniquet.getCommission(  ).getIdCommission(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne l'élément du tourniquet identifié par nKey
     * @param nKey l'identifiant de l'élément recherché
     * @param plugin le Plugin actif
     * @return l'élément identifié par nKey
     */
    public Tourniquet load( int nKey, Plugin plugin )
    {
        Tourniquet tourniquet = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            tourniquet = new Tourniquet(  );
            tourniquet.setNumeroOrdre( daoUtil.getInt( 1 ) );

            Commission commission = new Commission(  );
            commission.setIdCommission( nKey );
            commission.setNumero( daoUtil.getInt( 2 ) );
            tourniquet.setCommission( commission );
        }

        daoUtil.free(  );

        return tourniquet;
    }

    /**
     * Retourne la liste des éléments du tourniquet dans l'ordre.
     * @param plugin le Plugin actif
     * @return l'état actuel du tourniquet
     */
    public List<Tourniquet> loadTourniquet( Plugin plugin )
    {
        List<Tourniquet> listTourniquet = new ArrayList<Tourniquet>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_LOAD_TOURNIQUET, plugin );
        daoUtil.executeQuery(  );

        int nOrdre = 1;

        while ( daoUtil.next(  ) )
        {
            Commission commission = new Commission(  );
            commission.setIdCommission( daoUtil.getInt( 1 ) );
            commission.setNumero( daoUtil.getInt( 2 ) );

            Tourniquet tourniquet = new Tourniquet(  );
            tourniquet.setCommission( commission );
            tourniquet.setNumeroOrdre( nOrdre );
            listTourniquet.add( tourniquet );
            nOrdre++;
        }

        daoUtil.free(  );

        return listTourniquet;
    }

    /**
     * Vide la table tourniquet.
     * @param plugin le Plugin actif
     */
    public void flushTourniquet( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FLUSH, plugin );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
