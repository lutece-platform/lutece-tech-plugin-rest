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
package fr.paris.lutece.plugins.ods.business.seance;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * Responsable de communiqué avec la BDD
 */
public class SeanceDAO implements fr.paris.lutece.plugins.ods.business.seance.ISeanceDAO
{
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_seance ) FROM ods_seance ";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = " SELECT * FROM ods_seance WHERE id_seance = ? ";
    private static final String SQL_QUERY_SEANCE_LIST = " SELECT * FROM ods_seance where date_fin_seance >= ?  ";
    private static final String SQL_QUERY_SEANCE_WIDTH_FILTER_LIST = " SELECT * FROM ods_seance where date_seance >= ? and date_seance <= ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO ods_seance(id_seance, date_seance, date_fin_seance, date_remise_cr_sommaire, date_depot_questions, date_limite_diff_questions, date_limite_diff_pdd, date_limite_diff_dsp, date_conference, date_reunion_post_commission, date_reunion_premiere_commission, date_reunion_deuxieme_commission, date_reunion_troisieme_commission) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
    private static final String SQL_QUERY_UPDATE = " UPDATE ods_seance SET date_seance = ?, date_fin_seance = ?, date_remise_cr_sommaire = ?, date_depot_questions = ?, date_limite_diff_questions = ?, date_limite_diff_pdd = ?, date_limite_diff_dsp = ?, date_conference = ?, date_reunion_post_commission = ?, date_reunion_premiere_commission = ?, date_reunion_deuxieme_commission = ?, date_reunion_troisieme_commission = ? WHERE id_seance = ?  ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM ods_seance WHERE id_seance = ?  ";
    private static final String SQL_QUERY_SEANCE_NEXT = "SELECT * FROM ods_seance WHERE date_fin_seance >= NOW() ORDER BY date_seance LIMIT 1";
    private static final String SQL_QUERY_SEANCE_LAST = "SELECT * FROM ods_seance WHERE date_fin_seance < NOW() ORDER BY date_fin_seance DESC LIMIT 1";
    private static final String SQL_QUERY_SEANCE_OLD = "SELECT * FROM ods_seance WHERE date_fin_seance < NOW() ORDER BY date_fin_seance DESC";
    private static final String SQL_QUERY_ORDER_BY_DATE_SEANCE = "  ORDER BY date_seance ASC  ";
    private static final String SQL_QUERY_SEANCE_FOR_PDD = " SELECT seance.id_seance, seance.date_seance, seance.date_fin_seance, seance.date_remise_cr_sommaire, seance.date_depot_questions, " +
        "        seance.date_limite_diff_questions, seance.date_limite_diff_pdd, seance.date_limite_diff_dsp, seance.date_conference, " +
        "        seance.date_reunion_post_commission, seance.date_reunion_premiere_commission, seance.date_reunion_deuxieme_commission, seance.date_reunion_troisieme_commission " +
        " FROM ods_seance seance, ods_odj odj, ods_entree_ordre_jour entree" + " WHERE odj.id_odj = entree.id_odj " +
        " AND seance.id_seance = odj.id_seance " + " AND odj.id_type_odj = 2 " + " AND entree.id_pdd = ? ";

    /**
    * Crée une nouvelle séance à partir de l’objet seance passé en paramètre
    * @param seance la séance à insérer
    * @param plugin Plugin
    */
    public void insert( Seance seance, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        seance.setIdSeance( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, seance.getIdSeance(  ) );
        daoUtil.setTimestamp( 2, seance.getDateSeance(  ) );
        daoUtil.setTimestamp( 3, seance.getDateCloture(  ) );
        daoUtil.setTimestamp( 4, seance.getDateRemiseCr(  ) );
        daoUtil.setTimestamp( 5, seance.getDateDepotQuestionsOrales(  ) );
        daoUtil.setTimestamp( 6, seance.getDateLimiteDiffQuestions(  ) );
        daoUtil.setTimestamp( 7, seance.getDateLimiteDiffPdd(  ) );
        daoUtil.setTimestamp( 8, seance.getDateLimiteDiffDsp(  ) );
        daoUtil.setTimestamp( 9, seance.getDateConfOrg(  ) );
        daoUtil.setTimestamp( 10, seance.getDateReuPostComm(  ) );
        daoUtil.setTimestamp( 11, seance.getDateReu1(  ) );
        daoUtil.setTimestamp( 12, seance.getDateReu2(  ) );
        daoUtil.setTimestamp( 13, seance.getDateReu3(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Modifie la séance correspondant à l’objet seance passé en paramètre
     * @param seance la séance à modifier
     * @param plugin Plugin
     */
    public void store( Seance seance, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setTimestamp( 1, seance.getDateSeance(  ) );
        daoUtil.setTimestamp( 2, seance.getDateCloture(  ) );
        daoUtil.setTimestamp( 3, seance.getDateRemiseCr(  ) );
        daoUtil.setTimestamp( 4, seance.getDateDepotQuestionsOrales(  ) );
        daoUtil.setTimestamp( 5, seance.getDateLimiteDiffQuestions(  ) );
        daoUtil.setTimestamp( 6, seance.getDateLimiteDiffPdd(  ) );
        daoUtil.setTimestamp( 7, seance.getDateLimiteDiffDsp(  ) );
        daoUtil.setTimestamp( 8, seance.getDateConfOrg(  ) );
        daoUtil.setTimestamp( 9, seance.getDateReuPostComm(  ) );
        daoUtil.setTimestamp( 10, seance.getDateReu1(  ) );
        daoUtil.setTimestamp( 11, seance.getDateReu2(  ) );
        daoUtil.setTimestamp( 12, seance.getDateReu3(  ) );
        daoUtil.setInt( 13, seance.getIdSeance(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime la séance correspondant à  l’objet seance passé en paramètre
     * @param seance la séance à supprimer
     * @param plugin Plugin
     * @throws AppException exception levé si une dependance existe
     */
    public void delete( Seance seance, Plugin plugin )
        throws AppException
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, seance.getIdSeance(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * renvoie la séance ayant comme identifiant nKey
     * @param nKey idenfiant de la séance à supprimer
     * @param plugin Plugin
     * @return objet Seance
     */
    public Seance load( int nKey, Plugin plugin )
    {
        Seance seance = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( "id_seance" ) );
            seance.setDateSeance( daoUtil.getTimestamp( "date_seance" ) );
            seance.setDateCloture( daoUtil.getTimestamp( "date_fin_seance" ) );
            seance.setDateRemiseCr( daoUtil.getTimestamp( "date_remise_cr_sommaire" ) );
            seance.setDateDepotQuestionsOrales( daoUtil.getTimestamp( "date_depot_questions" ) );
            seance.setDateLimiteDiffQuestions( daoUtil.getTimestamp( "date_limite_diff_questions" ) );
            seance.setDateLimiteDiffPdd( daoUtil.getTimestamp( "date_limite_diff_pdd" ) );
            seance.setDateLimiteDiffDsp( daoUtil.getTimestamp( "date_limite_diff_dsp" ) );
            seance.setDateConfOrg( daoUtil.getTimestamp( "date_conference" ) );
            seance.setDateReuPostComm( daoUtil.getTimestamp( "date_reunion_post_commission" ) );
            seance.setDateReu1( daoUtil.getTimestamp( "date_reunion_premiere_commission" ) );
            seance.setDateReu2( daoUtil.getTimestamp( "date_reunion_deuxieme_commission" ) );
            seance.setDateReu3( daoUtil.getTimestamp( "date_reunion_troisieme_commission" ) );
        }

        daoUtil.free(  );

        return seance;
    }

    /**
     * renvoie la liste des séances dont la date de fin est supérieur ou égale a la date du jour
     * @param plugin Plugin
     * @return liste d'objets Seance
     */
    public List<Seance> seanceList( Plugin plugin )
    {
        List<Seance> seances = new ArrayList<Seance>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SEANCE_LIST + SQL_QUERY_ORDER_BY_DATE_SEANCE, plugin );
        daoUtil.setTimestamp( 1, new Timestamp( System.currentTimeMillis(  ) ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Seance seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( "id_seance" ) );
            seance.setDateSeance( daoUtil.getTimestamp( "date_seance" ) );
            seance.setDateCloture( daoUtil.getTimestamp( "date_fin_seance" ) );
            seance.setDateRemiseCr( daoUtil.getTimestamp( "date_remise_cr_sommaire" ) );
            seance.setDateDepotQuestionsOrales( daoUtil.getTimestamp( "date_depot_questions" ) );
            seance.setDateLimiteDiffQuestions( daoUtil.getTimestamp( "date_limite_diff_questions" ) );
            seance.setDateLimiteDiffPdd( daoUtil.getTimestamp( "date_limite_diff_pdd" ) );
            seance.setDateLimiteDiffDsp( daoUtil.getTimestamp( "date_limite_diff_dsp" ) );
            seance.setDateConfOrg( daoUtil.getTimestamp( "date_conference" ) );
            seance.setDateReuPostComm( daoUtil.getTimestamp( "date_reunion_post_commission" ) );
            seance.setDateReu1( daoUtil.getTimestamp( "date_reunion_premiere_commission" ) );
            seance.setDateReu2( daoUtil.getTimestamp( "date_reunion_deuxieme_commission" ) );
            seance.setDateReu3( daoUtil.getTimestamp( "date_reunion_troisieme_commission" ) );
            seances.add( seance );
        }

        daoUtil.free(  );

        return seances;
    }

    /**
     * renvoie la liste des séances dont la date de début est comprise entre startDateFilter et endDateFilter
     *
     * @param startDateFilter date de début
     * @param endDateFilter date de fin
     * @param plugin Plugin
     * @return liste d'objets Seance
     */
    public List<Seance> loadWidthFilter( Timestamp startDateFilter, Timestamp endDateFilter, Plugin plugin )
    {
        List<Seance> seances = new ArrayList<Seance>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SEANCE_WIDTH_FILTER_LIST + SQL_QUERY_ORDER_BY_DATE_SEANCE, plugin );
        daoUtil.setTimestamp( 1, startDateFilter );
        daoUtil.setTimestamp( 2, endDateFilter );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Seance seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( "id_seance" ) );
            seance.setDateSeance( daoUtil.getTimestamp( "date_seance" ) );
            seance.setDateCloture( daoUtil.getTimestamp( "date_fin_seance" ) );
            seance.setDateRemiseCr( daoUtil.getTimestamp( "date_remise_cr_sommaire" ) );
            seance.setDateDepotQuestionsOrales( daoUtil.getTimestamp( "date_depot_questions" ) );
            seance.setDateLimiteDiffQuestions( daoUtil.getTimestamp( "date_limite_diff_questions" ) );
            seance.setDateLimiteDiffPdd( daoUtil.getTimestamp( "date_limite_diff_pdd" ) );
            seance.setDateLimiteDiffDsp( daoUtil.getTimestamp( "date_limite_diff_dsp" ) );
            seance.setDateConfOrg( daoUtil.getTimestamp( "date_conference" ) );
            seance.setDateReuPostComm( daoUtil.getTimestamp( "date_reunion_post_commission" ) );
            seance.setDateReu1( daoUtil.getTimestamp( "date_reunion_premiere_commission" ) );
            seance.setDateReu2( daoUtil.getTimestamp( "date_reunion_deuxieme_commission" ) );
            seance.setDateReu3( daoUtil.getTimestamp( "date_reunion_troisieme_commission" ) );
            seances.add( seance );
        }

        daoUtil.free(  );

        return seances;
    }

    /**
     * Retourne la prochaine séance dont la date de fin est supérieur ou égale a la date du jour
     * @param plugin Plugin
     * @return objet Seance
     */
    public Seance findNextSeance( Plugin plugin )
    {
        Seance seance = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SEANCE_NEXT, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( "id_seance" ) );
            seance.setDateSeance( daoUtil.getTimestamp( "date_seance" ) );
            seance.setDateCloture( daoUtil.getTimestamp( "date_fin_seance" ) );
            seance.setDateRemiseCr( daoUtil.getTimestamp( "date_remise_cr_sommaire" ) );
            seance.setDateDepotQuestionsOrales( daoUtil.getTimestamp( "date_depot_questions" ) );
            seance.setDateLimiteDiffPdd( daoUtil.getTimestamp( "date_limite_diff_questions" ) );
            seance.setDateLimiteDiffPdd( daoUtil.getTimestamp( "date_limite_diff_pdd" ) );
            seance.setDateLimiteDiffPdd( daoUtil.getTimestamp( "date_limite_diff_dsp" ) );
            seance.setDateConfOrg( daoUtil.getTimestamp( "date_conference" ) );
            seance.setDateReuPostComm( daoUtil.getTimestamp( "date_reunion_post_commission" ) );
            seance.setDateReu1( daoUtil.getTimestamp( "date_reunion_premiere_commission" ) );
            seance.setDateReu2( daoUtil.getTimestamp( "date_reunion_deuxieme_commission" ) );
            seance.setDateReu3( daoUtil.getTimestamp( "date_reunion_troisieme_commission" ) );

            if ( daoUtil.getObject( "taille_archive" ) != null )
            {
                seance.setDatePublicationArchive( daoUtil.getTimestamp( "date_publication" ) );
                seance.setTailleArchive( (long) daoUtil.getInt( "taille_archive" ) );
            }
        }

        daoUtil.free(  );

        return seance;
    }

    /**
     * Retourne la liste des séances précédentes
     * @param plugin Plugin
     * @return la liste des Seance précédentes
     */
    public List<Seance> findOldSeance( Plugin plugin )
    {
        List<Seance> listSeance = new ArrayList<Seance>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SEANCE_OLD, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Seance seance = null;
            seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( "id_seance" ) );
            seance.setDateSeance( daoUtil.getTimestamp( "date_seance" ) );
            seance.setDateCloture( daoUtil.getTimestamp( "date_fin_seance" ) );
            seance.setDateRemiseCr( daoUtil.getTimestamp( "date_remise_cr_sommaire" ) );
            seance.setDateDepotQuestionsOrales( daoUtil.getTimestamp( "date_depot_questions" ) );
            seance.setDateLimiteDiffPdd( daoUtil.getTimestamp( "date_limite_diff_questions" ) );
            seance.setDateLimiteDiffPdd( daoUtil.getTimestamp( "date_limite_diff_pdd" ) );
            seance.setDateLimiteDiffPdd( daoUtil.getTimestamp( "date_limite_diff_dsp" ) );
            seance.setDateConfOrg( daoUtil.getTimestamp( "date_conference" ) );
            seance.setDateReuPostComm( daoUtil.getTimestamp( "date_reunion_post_commission" ) );
            seance.setDateReu1( daoUtil.getTimestamp( "date_reunion_premiere_commission" ) );
            seance.setDateReu2( daoUtil.getTimestamp( "date_reunion_deuxieme_commission" ) );
            seance.setDateReu3( daoUtil.getTimestamp( "date_reunion_troisieme_commission" ) );

            listSeance.add( seance );
        }

        daoUtil.free(  );

        return listSeance;
    }

    /**
     * Retourne la dernière séance dont la date de fin est inférieure à la date du jour
     * @param plugin Plugin
     * @return objet Seance
     */
    public Seance findLastSeance( Plugin plugin )
    {
        Seance seance = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SEANCE_LAST, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( "id_seance" ) );
            seance.setDateSeance( daoUtil.getTimestamp( "date_seance" ) );
            seance.setDateCloture( daoUtil.getTimestamp( "date_fin_seance" ) );
            seance.setDateRemiseCr( daoUtil.getTimestamp( "date_remise_cr_sommaire" ) );
            seance.setDateDepotQuestionsOrales( daoUtil.getTimestamp( "date_depot_questions" ) );
            seance.setDateLimiteDiffPdd( daoUtil.getTimestamp( "date_limite_diff_questions" ) );
            seance.setDateLimiteDiffPdd( daoUtil.getTimestamp( "date_limite_diff_pdd" ) );
            seance.setDateLimiteDiffPdd( daoUtil.getTimestamp( "date_limite_diff_dsp" ) );
            seance.setDateConfOrg( daoUtil.getTimestamp( "date_conference" ) );
            seance.setDateReuPostComm( daoUtil.getTimestamp( "date_reunion_post_commission" ) );
            seance.setDateReu1( daoUtil.getTimestamp( "date_reunion_premiere_commission" ) );
            seance.setDateReu2( daoUtil.getTimestamp( "date_reunion_deuxieme_commission" ) );
            seance.setDateReu3( daoUtil.getTimestamp( "date_reunion_troisieme_commission" ) );
        }

        daoUtil.free(  );

        return seance;
    }

    /**
     * Génère un nouvel identifiant
     * @param plugin Plugin
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
    * Retourne la séance à laquelle est rattachée un pdd
    * @param nIdPdd l'identifiant du Pdd
    * @param plugin le Plugin
    */
    public Seance findSeanceByPdd( int nIdPdd, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SEANCE_FOR_PDD, plugin );
        daoUtil.setInt( 1, nIdPdd );
        daoUtil.executeQuery(  );

        Seance seance;

        if ( daoUtil.next(  ) )
        {
            seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( "id_seance" ) );
            seance.setDateSeance( daoUtil.getTimestamp( "date_seance" ) );
            seance.setDateCloture( daoUtil.getTimestamp( "date_fin_seance" ) );
            seance.setDateRemiseCr( daoUtil.getTimestamp( "date_remise_cr_sommaire" ) );
            seance.setDateDepotQuestionsOrales( daoUtil.getTimestamp( "date_depot_questions" ) );
            seance.setDateLimiteDiffQuestions( daoUtil.getTimestamp( "date_limite_diff_questions" ) );
            seance.setDateLimiteDiffPdd( daoUtil.getTimestamp( "date_limite_diff_pdd" ) );
            seance.setDateLimiteDiffDsp( daoUtil.getTimestamp( "date_limite_diff_dsp" ) );
            seance.setDateConfOrg( daoUtil.getTimestamp( "date_conference" ) );
            seance.setDateReuPostComm( daoUtil.getTimestamp( "date_reunion_post_commission" ) );
            seance.setDateReu1( daoUtil.getTimestamp( "date_reunion_premiere_commission" ) );
            seance.setDateReu2( daoUtil.getTimestamp( "date_reunion_deuxieme_commission" ) );
            seance.setDateReu3( daoUtil.getTimestamp( "date_reunion_troisieme_commission" ) );
        }
        else
        {
            seance = findNextSeance( plugin );
        }

        daoUtil.free(  );

        return seance;
    }
}
