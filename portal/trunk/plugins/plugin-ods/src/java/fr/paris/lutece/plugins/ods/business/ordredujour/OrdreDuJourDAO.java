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
package fr.paris.lutece.plugins.ods.business.ordredujour;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *ordre du jour dao
 */
public class OrdreDuJourDAO implements fr.paris.lutece.plugins.ods.business.ordredujour.IOrdreDuJourDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_odj ) FROM ods_odj ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_odj( " +
        "			id_odj, id_seance,id_formation_conseil,id_type_odj, ods_id_odj, id_commission,intitule ," +
        "	mode_classement,tourniquet,publie,date_publication,est_sauvegarde,xml_correspondant,xml_entete,xml_pied_de_page) " +
        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ods_odj SET " +
        "			id_odj=?, id_seance=?,id_formation_conseil=?,id_type_odj=?, ods_id_odj=?," +
        "id_commission=?,intitule =? ,mode_classement=?,tourniquet=?,publie=?,date_publication=?," +
        "est_sauvegarde=? ,xml_correspondant=?,xml_entete=?,xml_pied_de_page=? WHERE id_odj=?";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ods_odj WHERE id_odj=?";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT odj.id_odj,odj.id_seance," +
        "fc.id_formation_conseil,fc.libelle_formation_conseil,ty.id_type_odj,ty.libelle_type_odj," +
        "odj.ods_id_odj,co.id_commission,co.numero_commission,co.libelle_commission,odj.intitule," +
        "odj.mode_classement,odj.tourniquet,odj.publie,odj.date_publication," +
        "odj.est_sauvegarde,odj.xml_correspondant,odj.xml_entete,odj.xml_pied_de_page FROM ods_odj odj " +
        "LEFT JOIN ods_commission co  on(odj.id_commission=co.id_commission) " +
        "LEFT JOIN ods_formation_conseil fc on(odj.id_formation_conseil=fc.id_formation_conseil) " +
        "LEFT JOIN ods_type_ordre_jour ty on(odj.id_type_odj=ty.id_type_odj) " + "WHERE odj.id_odj=? " +
        "AND odj.id_type_odj=ty.id_type_odj ";
    private static final String SQL_QUERY_FIND_BY_FILTER = "SELECT odj.id_odj,odj.id_seance," +
        "fc.id_formation_conseil,fc.libelle_formation_conseil,ty.id_type_odj,ty.libelle_type_odj," +
        "odj.ods_id_odj,co.id_commission,co.numero_commission,co.libelle_commission,odj.intitule," +
        "odj.mode_classement,odj.tourniquet,odj.publie,odj.date_publication," +
        "odj.est_sauvegarde,odj.xml_correspondant,odj.xml_entete,odj.xml_pied_de_page FROM ods_odj odj " +
        "LEFT JOIN ods_commission co  on(odj.id_commission=co.id_commission) " +
        "LEFT JOIN ods_formation_conseil fc on(odj.id_formation_conseil=fc.id_formation_conseil) " +
        "LEFT JOIN ods_type_ordre_jour ty on(odj.id_type_odj=ty.id_type_odj) " + "WHERE 1 ";
    private static final String SQL_QUERY_FIND_ODJ_REFERENCE = "SELECT odj.id_odj,odj.id_seance," +
        "fc.id_formation_conseil,fc.libelle_formation_conseil,ty.id_type_odj,ty.libelle_type_odj," +
        "odj.ods_id_odj,co.id_commission,co.numero_commission,co.libelle_commission,odj.intitule," +
        "odj.mode_classement,odj.tourniquet,odj.publie,odj.date_publication," +
        "odj.est_sauvegarde,odj.xml_correspondant,odj.xml_entete,odj.xml_pied_de_page FROM ods_odj odj " +
        "LEFT JOIN ods_commission co  on(odj.id_commission=co.id_commission) " +
        "LEFT JOIN ods_formation_conseil fc on(odj.id_formation_conseil=fc.id_formation_conseil) " +
        "LEFT JOIN ods_type_ordre_jour ty on(odj.id_type_odj=ty.id_type_odj) " + "WHERE  " +
        "odj.id_type_odj!=3 AND odj.id_type_odj!=4 AND odj.id_type_odj!=5 AND odj.id_commission is NULL";
    private static final String SQL_QUERY_SELECT_ODJ_LIST = "SELECT odj.id_odj,odj.id_seance," +
        "fc.id_formation_conseil,fc.libelle_formation_conseil,ty.id_type_odj,ty.libelle_type_odj," +
        "odj.ods_id_odj,co.id_commission,co.numero_commission,co.libelle_commission,odj.intitule," +
        "odj.mode_classement,odj.tourniquet,odj.publie,odj.date_publication," +
        "odj.est_sauvegarde,odj.xml_correspondant,odj.xml_entete,odj.xml_pied_de_page FROM ods_odj odj " +
        "LEFT JOIN ods_commission co  on(odj.id_commission=co.id_commission) " +
        "LEFT JOIN ods_formation_conseil fc on(odj.id_formation_conseil=fc.id_formation_conseil) " +
        "LEFT JOIN ods_type_ordre_jour ty on(odj.id_type_odj=ty.id_type_odj) " + "WHERE 1 ";
    private static final String SQL_QUERY_SELECT_ODJ_BY_RAPPORTEUR_FOR_PDD = "SELECT odj.id_odj FROM ods_odj odj " +
        "LEFT JOIN ods_entree_ordre_jour in_odj ON(odj.id_odj = in_odj.id_odj AND id_pdd IS NOT NULL) " +
        "LEFT JOIN ods_entree_elus in_elu ON(in_odj.id_entree = in_elu.id_entree ) " +
        "WHERE in_elu.id_elu = ? AND odj.est_sauvegarde = 0 AND odj.publie = 1 ";
    private static final String SQL_QUERY_SELECT_ODJ_BY_RAPPORTEUR_FOR_VNR = "SELECT odj.id_odj FROM ods_odj odj " +
        "LEFT JOIN ods_entree_ordre_jour in_odj ON(odj.id_odj = in_odj.id_odj AND id_va IS NOT NULL) " +
        "LEFT JOIN ods_entree_elus in_elu ON(in_odj.id_entree = in_elu.id_entree ) " +
        "WHERE in_elu.id_elu = ? AND odj.est_sauvegarde = 0 AND odj.publie = 1 ";
    private static final String SQL_FILTER_FORMATION_CONSEIL = " AND odj.id_formation_conseil = ? ";
    private static final String SQL_FILTER_PUBLIE = " AND odj.publie = ?  ";
    private static final String SQL_FILTER_SEANCE = " AND odj.id_seance= ? ";
    private static final String SQL_FILTER_TYPE = " AND odj.id_type_odj= ? ";
    private static final String SQL_FILTER_COMMISSION = " AND odj.id_commission = ? ";
    private static final String SQL_FILTER_EST_SAUVEGARDE = " AND odj.est_sauvegarde = ? ";
    private static final String SQL_FILTER_TOURNIQUET = " AND odj.tourniquet = ? ";
    private static final String SQL_FILTER_COMMISSION_NULL = " AND odj.id_commission is NULL ";
    private static final String SQL_QUERY_ALREADY_EXIST = "SELECT COUNT(*) FROM  ods_odj odj where 1=1  ";
    private static final String SQL_QUERY_ORDER_LIST_ODJ_BY_DATE_PUBLICATION = " ORDER BY odj.date_publication  DESC ";
    private static final String SQL_QUERY_ORDER_LIST_ODJ_FOR_BACK = " ORDER BY fc.id_formation_conseil,ty.id_type_odj,co.numero_commission ";
    private static final String SQL_FILTER_DATE_PUBLICATION = " AND odj.date_publication > ? ";

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
     *supprime l'ordre du jour passé en parametre
     * @param odj OrdreDuJour
     * @param plugin Plugin
     */
    public void delete( OrdreDuJour odj, Plugin plugin )
    {
        DAOUtil daoUtil;
        daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, odj.getIdOrdreDuJour(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * insère l'ordre du jour passé en parametre dans la table ods_odj
     * @param odj OrdreDuJour
     * @param plugin Plugin
     * @return l'id de l'ordre du jour nouvellement inséré
     */
    public int insert( OrdreDuJour odj, Plugin plugin )
    {
        int newPrimaryKey = newPrimaryKey( plugin );
        DAOUtil daoUtil;
        daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( 1, newPrimaryKey );
        daoUtil.setInt( 2, odj.getSeance(  ).getIdSeance(  ) );
        daoUtil.setInt( 3, odj.getFormationConseil(  ).getIdFormationConseil(  ) );
        daoUtil.setInt( 4, odj.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) );

        if ( null != odj.getOrdreDuJourSauveGarde(  ) )
        {
            daoUtil.setInt( 5, odj.getOrdreDuJourSauveGarde(  ).getIdOrdreDuJour(  ) );
        }
        else
        {
            daoUtil.setIntNull( 5 );
        }

        if ( null != odj.getCommission(  ) )
        {
            daoUtil.setInt( 6, odj.getCommission(  ).getIdCommission(  ) );
        }
        else
        {
            daoUtil.setIntNull( 6 );
        }

        daoUtil.setString( 7, odj.getIntitule(  ) );
        daoUtil.setString( 8, odj.getModeClassement(  ) );
        daoUtil.setBoolean( 9, odj.getTourniquet(  ) );
        daoUtil.setBoolean( 10, odj.getPublie(  ) );
        daoUtil.setTimestamp( 11, odj.getDatePublication(  ) );
        daoUtil.setBoolean( 12, odj.getEstSauvegarde(  ) );
        daoUtil.setString( 13, odj.getXmlPublication(  ) );
        daoUtil.setString( 14, odj.getXmlEntete(  ) );
        daoUtil.setString( 15, odj.getXmlPiedDePage(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        return newPrimaryKey;
    }

    /**
     *retourne l'ordre du jour d'id nKey
     * @param nKey int
     * @param plugin Plugin
     * @return OrdreDuJour ordreDuJour
     */
    public OrdreDuJour load( int nKey, Plugin plugin )
    {
        Seance seance = null;
        FormationConseil formationConseil = null;
        TypeOrdreDuJour typeOrdreDuJour = null;
        OrdreDuJour ordreDuJour = null;
        OrdreDuJour ordreDuJourSauvegarde = null;
        Commission commission = null;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            ordreDuJour = new OrdreDuJour(  );

            ordreDuJour.setIdOrdreDuJour( daoUtil.getInt( 1 ) );

            seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( 2 ) );
            ordreDuJour.setSeance( seance );

            formationConseil = new FormationConseil(  );
            formationConseil.setIdFormationConseil( daoUtil.getInt( 3 ) );
            formationConseil.setLibelle( daoUtil.getString( 4 ) );
            ordreDuJour.setFormationConseil( formationConseil );

            typeOrdreDuJour = new TypeOrdreDuJour(  );
            typeOrdreDuJour.setIdTypeOrdreDuJour( daoUtil.getInt( 5 ) );
            typeOrdreDuJour.setLibelle( daoUtil.getString( 6 ) );
            ordreDuJour.setTypeOrdreDuJour( typeOrdreDuJour );

            if ( null != daoUtil.getObject( 7 ) )
            {
                ordreDuJourSauvegarde = new OrdreDuJour(  );
                ordreDuJourSauvegarde.setIdOrdreDuJour( daoUtil.getInt( 7 ) );
                ordreDuJour.setOrdreDuJourSauveGarde( ordreDuJourSauvegarde );
            }

            if ( null != daoUtil.getObject( 8 ) )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 8 ) );
                commission.setNumero( daoUtil.getInt( 9 ) );
                commission.setLibelle( daoUtil.getString( 10 ) );
            }

            ordreDuJour.setCommission( commission );

            ordreDuJour.setIntitule( daoUtil.getString( 11 ) );
            ordreDuJour.setModeClassement( daoUtil.getString( 12 ) );
            ordreDuJour.setTourniquet( daoUtil.getBoolean( 13 ) );
            ordreDuJour.setPublie( daoUtil.getBoolean( 14 ) );
            ordreDuJour.setDatePublication( daoUtil.getTimestamp( 15 ) );
            ordreDuJour.setEstSauvegarde( daoUtil.getBoolean( 16 ) );
            ordreDuJour.setXmlPublication( daoUtil.getString( 17 ) );
            ordreDuJour.setXmlEntete( daoUtil.getString( 18 ) );
            ordreDuJour.setXmlPiedDePage( daoUtil.getString( 19 ) );
        }

        daoUtil.free(  );

        return ordreDuJour;
    }

    /**
     * renvoie l'odj répondant au filtre 'filter' .
     * @param filter le filtre de l'odj
     * @param plugin Plugin
     * @return OrdreDuJour ordreDuJour
     */
    public OrdreDuJour findByOdjFilter( OrdreDuJourFilter filter, Plugin plugin )
    {
        Seance seance = null;
        FormationConseil formationConseil = null;
        TypeOrdreDuJour typeOrdreDuJour = null;
        OrdreDuJour ordreDuJour = null;
        OrdreDuJour ordreDuJourSauvegarde = null;
        Commission commission = null;
        String strSQL = SQL_QUERY_FIND_BY_FILTER;
        strSQL += ( ( filter.containsIdCommissionCriteria(  ) ) ? SQL_FILTER_COMMISSION : SQL_FILTER_COMMISSION_NULL );
        strSQL += ( ( filter.containsIdFormationConseilCriteria(  ) ) ? SQL_FILTER_FORMATION_CONSEIL
                                                                      : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdPublieCriteria(  ) ) ? SQL_FILTER_PUBLIE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdSeanceCriteria(  ) ) ? SQL_FILTER_SEANCE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdTypeCriteria(  ) ) ? SQL_FILTER_TYPE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdSauvegardeCriteria(  ) ) ? SQL_FILTER_EST_SAUVEGARDE
                                                                : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdTourniquetCriteria(  ) ) ? SQL_FILTER_TOURNIQUET
                                                                : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsDatePublicationCriteria(  ) ) ? SQL_FILTER_DATE_PUBLICATION
                                                                   : OdsConstants.CONSTANTE_CHAINE_VIDE );

        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );

        int nIndex = 1;

        //si la commission n'est pas précisé on fitre sur la commission null 
        if ( filter.containsIdCommissionCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdCommission(  ) );
            nIndex++;
        }

        if ( filter.containsIdFormationConseilCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdFormationConseil(  ) );
            nIndex++;
        }

        if ( filter.containsIdPublieCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdPublie(  ) );
            nIndex++;
        }

        if ( filter.containsIdSeanceCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdSeance(  ) );
            nIndex++;
        }

        if ( filter.containsIdTypeCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdType(  ) );
            nIndex++;
        }

        if ( filter.containsIdSauvegardeCriteria(  ) )
        {
            if ( filter.getIdSauvegarde(  ) == 1 )
            {
                daoUtil.setBoolean( nIndex, true );
            }
            else
            {
                daoUtil.setBoolean( nIndex, false );
            }

            nIndex++;
        }

        if ( filter.containsIdTourniquetCriteria(  ) )
        {
            if ( filter.getIdTourniquet(  ) == 1 )
            {
                daoUtil.setBoolean( nIndex, true );
            }
            else
            {
                daoUtil.setBoolean( nIndex, false );
            }

            nIndex++;
        }

        if ( filter.containsDatePublicationCriteria(  ) )
        {
            daoUtil.setTimestamp( nIndex, filter.getDatePublication(  ) );
            nIndex++;
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            ordreDuJour = new OrdreDuJour(  );

            ordreDuJour.setIdOrdreDuJour( daoUtil.getInt( 1 ) );

            seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( 2 ) );
            ordreDuJour.setSeance( seance );

            formationConseil = new FormationConseil(  );
            formationConseil.setIdFormationConseil( daoUtil.getInt( 3 ) );
            formationConseil.setLibelle( daoUtil.getString( 4 ) );
            ordreDuJour.setFormationConseil( formationConseil );

            typeOrdreDuJour = new TypeOrdreDuJour(  );
            typeOrdreDuJour.setIdTypeOrdreDuJour( daoUtil.getInt( 5 ) );
            typeOrdreDuJour.setLibelle( daoUtil.getString( 6 ) );
            ordreDuJour.setTypeOrdreDuJour( typeOrdreDuJour );

            if ( null != daoUtil.getObject( 7 ) )
            {
                ordreDuJourSauvegarde = new OrdreDuJour(  );
                ordreDuJourSauvegarde.setIdOrdreDuJour( daoUtil.getInt( 7 ) );
                ordreDuJour.setOrdreDuJourSauveGarde( ordreDuJourSauvegarde );
            }

            if ( null != daoUtil.getObject( 8 ) )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 8 ) );
                commission.setNumero( daoUtil.getInt( 9 ) );
                commission.setLibelle( daoUtil.getString( 10 ) );
            }

            ordreDuJour.setCommission( commission );

            ordreDuJour.setIntitule( daoUtil.getString( 11 ) );
            ordreDuJour.setModeClassement( daoUtil.getString( 12 ) );
            ordreDuJour.setTourniquet( daoUtil.getBoolean( 13 ) );
            ordreDuJour.setPublie( daoUtil.getBoolean( 14 ) );
            ordreDuJour.setDatePublication( daoUtil.getTimestamp( 15 ) );
            ordreDuJour.setEstSauvegarde( daoUtil.getBoolean( 16 ) );
            ordreDuJour.setXmlPublication( daoUtil.getString( 17 ) );
            ordreDuJour.setXmlEntete( daoUtil.getString( 18 ) );
            ordreDuJour.setXmlPiedDePage( daoUtil.getString( 19 ) );
        }

        daoUtil.free(  );

        return ordreDuJour;
    }

    /**
     * renvoie l'odj de reference répondant au filtre 'filter'
     * l’ordre du jour de référence est le dernier ordre du jour qui a été publié..
     * @param filter le filtre de l'odj
     * @param plugin Plugin
     * @return OrdreDuJour ordreDuJour
     */
    public OrdreDuJour findOdjReferenceByFilter( OrdreDuJourFilter filter, Plugin plugin )
    {
        Seance seance = null;
        FormationConseil formationConseil = null;
        TypeOrdreDuJour typeOrdreDuJour = null;
        OrdreDuJour ordreDuJour = null;
        OrdreDuJour ordreDuJourSauvegarde = null;
        Commission commission = null;
        String strSQL = SQL_QUERY_FIND_ODJ_REFERENCE;
        strSQL += SQL_FILTER_COMMISSION_NULL;
        strSQL += SQL_FILTER_PUBLIE;
        strSQL += ( ( filter.containsIdFormationConseilCriteria(  ) ) ? SQL_FILTER_FORMATION_CONSEIL
                                                                      : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdSeanceCriteria(  ) ) ? SQL_FILTER_SEANCE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdSauvegardeCriteria(  ) ) ? SQL_FILTER_EST_SAUVEGARDE
                                                                : OdsConstants.CONSTANTE_CHAINE_VIDE );

        strSQL += SQL_QUERY_ORDER_LIST_ODJ_BY_DATE_PUBLICATION;

        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );

        int nIndex = 1;
        daoUtil.setInt( nIndex, 1 );
        nIndex++;

        if ( filter.containsIdFormationConseilCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdFormationConseil(  ) );
            nIndex++;
        }

        if ( filter.containsIdSeanceCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdSeance(  ) );
            nIndex++;
        }

        if ( filter.containsIdSauvegardeCriteria(  ) )
        {
            if ( filter.getIdSauvegarde(  ) == 1 )
            {
                daoUtil.setBoolean( nIndex, true );
            }
            else
            {
                daoUtil.setBoolean( nIndex, false );
            }

            nIndex++;
        }

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            ordreDuJour = new OrdreDuJour(  );
            ordreDuJour.setIdOrdreDuJour( daoUtil.getInt( 1 ) );

            seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( 2 ) );
            ordreDuJour.setSeance( seance );

            formationConseil = new FormationConseil(  );
            formationConseil.setIdFormationConseil( daoUtil.getInt( 3 ) );
            formationConseil.setLibelle( daoUtil.getString( 4 ) );
            ordreDuJour.setFormationConseil( formationConseil );

            typeOrdreDuJour = new TypeOrdreDuJour(  );
            typeOrdreDuJour.setIdTypeOrdreDuJour( daoUtil.getInt( 5 ) );
            typeOrdreDuJour.setLibelle( daoUtil.getString( 6 ) );
            ordreDuJour.setTypeOrdreDuJour( typeOrdreDuJour );

            if ( null != daoUtil.getObject( 7 ) )
            {
                ordreDuJourSauvegarde = new OrdreDuJour(  );
                ordreDuJourSauvegarde.setIdOrdreDuJour( daoUtil.getInt( 7 ) );
                ordreDuJour.setOrdreDuJourSauveGarde( ordreDuJourSauvegarde );
            }

            if ( null != daoUtil.getObject( 8 ) )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 8 ) );
                commission.setNumero( daoUtil.getInt( 9 ) );
                commission.setLibelle( daoUtil.getString( 10 ) );
            }

            ordreDuJour.setCommission( commission );

            ordreDuJour.setIntitule( daoUtil.getString( 11 ) );
            ordreDuJour.setModeClassement( daoUtil.getString( 12 ) );
            ordreDuJour.setTourniquet( daoUtil.getBoolean( 13 ) );
            ordreDuJour.setPublie( daoUtil.getBoolean( 14 ) );
            ordreDuJour.setDatePublication( daoUtil.getTimestamp( 15 ) );
            ordreDuJour.setEstSauvegarde( daoUtil.getBoolean( 16 ) );
            ordreDuJour.setXmlPublication( daoUtil.getString( 17 ) );
            ordreDuJour.setXmlEntete( daoUtil.getString( 18 ) );
            ordreDuJour.setXmlPiedDePage( daoUtil.getString( 19 ) );
        }

        daoUtil.free(  );

        return ordreDuJour;
    }

    /**
     * renvoie la liste des ordres du jour répondant au filtre filter passé en parametre
     * @param  filter le filtre d'ordre du jour
     * @param  plugin Plugin
     * @param bFrontOffice true si la liste est appellée par le front office false sinon
     * @return une liste d'ordre du jour
     */
    public List<OrdreDuJour> selectOrdreDuJourList( Plugin plugin, OrdreDuJourFilter filter, boolean bFrontOffice )
    {
        List<OrdreDuJour> listOrdreDuJour = new ArrayList<OrdreDuJour>(  );
        Seance seance = null;
        FormationConseil formationConseil = null;
        TypeOrdreDuJour typeOrdreDuJour = null;
        OrdreDuJour ordreDuJour = null;
        OrdreDuJour ordreDuJourSauvegarde = null;
        Commission commission = null;
        String strSQL;

        strSQL = SQL_QUERY_SELECT_ODJ_LIST;

        if ( bFrontOffice )
        {
            strSQL += ( ( filter.containsIdCommissionCriteria(  ) ) ? SQL_FILTER_COMMISSION : SQL_FILTER_COMMISSION_NULL );
        }
        else
        {
            strSQL += ( ( filter.containsIdCommissionCriteria(  ) ) ? SQL_FILTER_COMMISSION
                                                                    : OdsConstants.CONSTANTE_CHAINE_VIDE );
        }

        strSQL += ( ( filter.containsIdFormationConseilCriteria(  ) ) ? SQL_FILTER_FORMATION_CONSEIL
                                                                      : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdPublieCriteria(  ) ) ? SQL_FILTER_PUBLIE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdSeanceCriteria(  ) ) ? SQL_FILTER_SEANCE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdSauvegardeCriteria(  ) ) ? SQL_FILTER_EST_SAUVEGARDE
                                                                : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdTourniquetCriteria(  ) ) ? SQL_FILTER_TOURNIQUET
                                                                : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsDatePublicationCriteria(  ) ) ? SQL_FILTER_DATE_PUBLICATION
                                                                   : OdsConstants.CONSTANTE_CHAINE_VIDE );

        if ( bFrontOffice )
        {
            strSQL += SQL_QUERY_ORDER_LIST_ODJ_BY_DATE_PUBLICATION;
        }
        else
        {
            strSQL += SQL_QUERY_ORDER_LIST_ODJ_FOR_BACK;
        }

        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );

        int nIndex = 1;

        //si la commission n'est pas précisé on fitre sur la commission null 
        if ( filter.containsIdCommissionCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdCommission(  ) );
            nIndex++;
        }

        if ( filter.containsIdFormationConseilCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdFormationConseil(  ) );
            nIndex++;
        }

        if ( filter.containsIdPublieCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdPublie(  ) );
            nIndex++;
        }

        if ( filter.containsIdSeanceCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdSeance(  ) );
            nIndex++;
        }

        if ( filter.containsIdSauvegardeCriteria(  ) )
        {
            if ( filter.getIdSauvegarde(  ) == 1 )
            {
                daoUtil.setBoolean( nIndex, true );
            }
            else
            {
                daoUtil.setBoolean( nIndex, false );
            }

            nIndex++;
        }

        if ( filter.containsIdTourniquetCriteria(  ) )
        {
            if ( filter.getIdTourniquet(  ) == 1 )
            {
                daoUtil.setBoolean( nIndex, true );
            }
            else
            {
                daoUtil.setBoolean( nIndex, false );
            }

            nIndex++;
        }

        if ( filter.containsDatePublicationCriteria(  ) )
        {
            daoUtil.setTimestamp( nIndex, filter.getDatePublication(  ) );
            nIndex++;
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            ordreDuJour = new OrdreDuJour(  );

            ordreDuJour.setIdOrdreDuJour( daoUtil.getInt( 1 ) );

            seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( 2 ) );
            ordreDuJour.setSeance( seance );

            formationConseil = new FormationConseil(  );
            formationConseil.setIdFormationConseil( daoUtil.getInt( 3 ) );
            formationConseil.setLibelle( daoUtil.getString( 4 ) );
            ordreDuJour.setFormationConseil( formationConseil );

            typeOrdreDuJour = new TypeOrdreDuJour(  );
            typeOrdreDuJour.setIdTypeOrdreDuJour( daoUtil.getInt( 5 ) );
            typeOrdreDuJour.setLibelle( daoUtil.getString( 6 ) );
            ordreDuJour.setTypeOrdreDuJour( typeOrdreDuJour );

            if ( null != daoUtil.getObject( 7 ) )
            {
                ordreDuJourSauvegarde = new OrdreDuJour(  );
                ordreDuJourSauvegarde.setIdOrdreDuJour( daoUtil.getInt( 7 ) );
                ordreDuJour.setOrdreDuJourSauveGarde( ordreDuJourSauvegarde );
            }

            if ( null != daoUtil.getObject( 8 ) )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 8 ) );
                commission.setNumero( daoUtil.getInt( 9 ) );
                commission.setLibelle( daoUtil.getString( 10 ) );
            }

            ordreDuJour.setCommission( commission );

            ordreDuJour.setIntitule( daoUtil.getString( 11 ) );
            ordreDuJour.setModeClassement( daoUtil.getString( 12 ) );
            ordreDuJour.setTourniquet( daoUtil.getBoolean( 13 ) );
            ordreDuJour.setPublie( daoUtil.getBoolean( 14 ) );
            ordreDuJour.setDatePublication( daoUtil.getTimestamp( 15 ) );
            ordreDuJour.setEstSauvegarde( daoUtil.getBoolean( 16 ) );
            ordreDuJour.setXmlPublication( daoUtil.getString( 17 ) );
            ordreDuJour.setXmlEntete( daoUtil.getString( 18 ) );
            ordreDuJour.setXmlPiedDePage( daoUtil.getString( 19 ) );
            listOrdreDuJour.add( ordreDuJour );
        }

        daoUtil.free(  );

        return listOrdreDuJour;
    }

    /**
     * update l'ordre du jour transmis en parametre
     * @param odj OrdreDuJour
     * @param plugin Plugin
     */
    public void store( OrdreDuJour odj, Plugin plugin )
    {
        DAOUtil daoUtil;
        daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, odj.getIdOrdreDuJour(  ) );
        daoUtil.setInt( 2, odj.getSeance(  ).getIdSeance(  ) );
        daoUtil.setInt( 3, odj.getFormationConseil(  ).getIdFormationConseil(  ) );
        daoUtil.setInt( 4, odj.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) );

        if ( null != odj.getOrdreDuJourSauveGarde(  ) )
        {
            daoUtil.setInt( 5, odj.getOrdreDuJourSauveGarde(  ).getIdOrdreDuJour(  ) );
        }
        else
        {
            daoUtil.setIntNull( 5 );
        }

        if ( null != odj.getCommission(  ) )
        {
            daoUtil.setInt( 6, odj.getCommission(  ).getIdCommission(  ) );
        }
        else
        {
            daoUtil.setIntNull( 6 );
        }

        daoUtil.setString( 7, odj.getIntitule(  ) );
        daoUtil.setString( 8, odj.getModeClassement(  ) );
        daoUtil.setBoolean( 9, odj.getTourniquet(  ) );
        daoUtil.setBoolean( 10, odj.getPublie(  ) );
        daoUtil.setTimestamp( 11, odj.getDatePublication(  ) );
        daoUtil.setBoolean( 12, odj.getEstSauvegarde(  ) );
        daoUtil.setString( 13, odj.getXmlPublication(  ) );
        daoUtil.setString( 14, odj.getXmlEntete(  ) );
        daoUtil.setString( 15, odj.getXmlPiedDePage(  ) );
        daoUtil.setInt( 16, odj.getIdOrdreDuJour(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Test si un odj répondant au filtre 'filter' existe en base.
     * @param filter le filtre appliqué pour la recherche
     * @param plugin  plugin
     * @return true si un odj répondant au filtre 'filter' existe en base, false sinon
     */
    public boolean isAlreadyExist( OrdreDuJourFilter filter, Plugin plugin )
    {
        String strSQL = SQL_QUERY_ALREADY_EXIST;

        strSQL += ( ( filter.containsIdCommissionCriteria(  ) ) ? SQL_FILTER_COMMISSION : SQL_FILTER_COMMISSION_NULL );
        strSQL += ( ( filter.containsIdFormationConseilCriteria(  ) ) ? SQL_FILTER_FORMATION_CONSEIL
                                                                      : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdPublieCriteria(  ) ) ? SQL_FILTER_PUBLIE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdSeanceCriteria(  ) ) ? SQL_FILTER_SEANCE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdTypeCriteria(  ) ) ? SQL_FILTER_TYPE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdSauvegardeCriteria(  ) ) ? SQL_FILTER_EST_SAUVEGARDE
                                                                : OdsConstants.CONSTANTE_CHAINE_VIDE );

        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );

        int nIndex = 1;

        //si la commission n'est pas précisé on fitre sur la commission null 
        if ( filter.containsIdCommissionCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdCommission(  ) );
            nIndex++;
        }

        if ( filter.containsIdFormationConseilCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdFormationConseil(  ) );
            nIndex++;
        }

        if ( filter.containsIdPublieCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdPublie(  ) );
            nIndex++;
        }

        if ( filter.containsIdSeanceCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdSeance(  ) );
            nIndex++;
        }

        if ( filter.containsIdTypeCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdType(  ) );
            nIndex++;
        }

        if ( filter.containsIdSauvegardeCriteria(  ) )
        {
            if ( filter.getIdSauvegarde(  ) == 1 )
            {
                daoUtil.setBoolean( nIndex, true );
            }
            else
            {
                daoUtil.setBoolean( nIndex, false );
            }

            nIndex++;
        }

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) && ( daoUtil.getInt( 1 ) != 0 ) )
        {
            daoUtil.free(  );

            return true;
        }

        daoUtil.free(  );

        return false;
    }

    /**
     * Retourne les ODJ dont l'une des entrées a cet élu dans ses rapporteurs
     * @param nIdRapporteur l'id de l'éu rapporteur
     * @param bPourPdd true si les rapporteurs doivent etre rattachés à un pdd,
     * false si les rapporteurs doivent etre rattachés à un voeu non rattaché
     * @param plugin le plugin
     * @return les ODJ filtrés par rapporteur
     */
    public List<OrdreDuJour> selectByRapporteur( int nIdRapporteur, boolean bPourPdd, Plugin plugin )
    {
        List<OrdreDuJour> listOdjs = new ArrayList<OrdreDuJour>(  );
        String strSqlQuery;

        if ( bPourPdd )
        {
            strSqlQuery = SQL_QUERY_SELECT_ODJ_BY_RAPPORTEUR_FOR_PDD;
        }
        else
        {
            strSqlQuery = SQL_QUERY_SELECT_ODJ_BY_RAPPORTEUR_FOR_VNR;
        }

        DAOUtil daoUtil = new DAOUtil( strSqlQuery, plugin );
        daoUtil.setInt( 1, nIdRapporteur );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            OrdreDuJour odj = new OrdreDuJour(  );
            odj.setIdOrdreDuJour( daoUtil.getInt( 1 ) );
            listOdjs.add( odj );
        }

        daoUtil.free(  );

        return listOdjs;
    }
}
