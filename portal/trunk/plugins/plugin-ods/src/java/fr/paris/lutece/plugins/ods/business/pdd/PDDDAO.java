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
package fr.paris.lutece.plugins.ods.business.pdd;

import fr.paris.lutece.plugins.ods.business.categoriedeliberation.CategorieDeliberation;
import fr.paris.lutece.plugins.ods.business.direction.Direction;
import fr.paris.lutece.plugins.ods.business.fascicule.Fascicule;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitique;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.statut.Statut;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * PDDDAO est responsable de la communication avec la BDD
 *
 */
public class PDDDAO implements IPDDDAO
{
    private static final String SQL_QUERY_PDD_LIST = "SELECT * " + "FROM ods_pdd pdd " +
        "LEFT JOIN ods_entree_ordre_jour entree on (entree.id_pdd = pdd.id_pdd) " +
        "LEFT JOIN ods_odj odj on (entree.id_odj = odj.id_odj) " +
        "LEFT JOIN ods_categorie_deliberation cd on (pdd.id_categorie = cd.id_categorie) " +
        "LEFT JOIN ods_direction dir on (pdd.id_direction = dir.id_direction) " +
        "LEFT JOIN ods_statut st on (pdd.id_statut = st.id_statut) " +
        "LEFT JOIN ods_groupe gr on (pdd.id_groupe = gr.id_groupe) " +
        "LEFT JOIN ods_formation_conseil fc on (pdd.id_formation_conseil = fc.id_formation_conseil) " + "WHERE 1 = 1 ";
    private static final String SQL_QUERY_PDD_LIST_BY_DIRECTION_OR_DIRECTION_CO = "SELECT * " + "FROM ods_pdd pdd " +
        "LEFT JOIN ods_entree_ordre_jour entree on (entree.id_pdd = pdd.id_pdd) " +
        "LEFT JOIN ods_odj odj on (entree.id_odj = odj.id_odj) " +
        "LEFT JOIN ods_categorie_deliberation cd on (pdd.id_categorie = cd.id_categorie) " +
        "LEFT JOIN ods_direction dir on (pdd.id_direction = dir.id_direction) " +
        "LEFT JOIN ods_statut st on (pdd.id_statut = st.id_statut) " +
        "LEFT JOIN ods_groupe gr on (pdd.id_groupe = gr.id_groupe) " +
        "LEFT JOIN ods_formation_conseil fc on (pdd.id_formation_conseil = fc.id_formation_conseil) " +
        "LEFT JOIN ods_co_emettrice co on(pdd.id_pdd=co.id_pdd) " +
        "WHERE (( pdd.id_pdd=co.id_pdd and co.id_direction= ? ) or pdd.id_direction=?) ";
    private static final String SQL_QUERY_DIRECTIONS_CO_COEMETRICE_LIST_BY_PDD = "SELECT * FROM ods_co_emettrice co " +
        "LEFT JOIN ods_direction dir on (co.id_direction = dir.id_direction) " + "WHERE co.id_pdd = ? ";
    private static final String SQL_QUERY_ARRONDISSEMENTS_LIST_BY_PDD = "SELECT * FROM ods_arrondissement_du_pdd " +
        "WHERE id_pdd = ? ";
    private static final String SQL_QUERY_ORDER_BY_REFERENCE = " ORDER BY pdd.reference ";
    private static final String SQL_FILTER_PRIMARY_KEY = " AND pdd.id_pdd = ? ";
    private static final String SQL_GROUP_BY_ID_PDD = "GROUP BY pdd.id_pdd";
    private static final String SQL_FILTER_SEANCE = " AND odj.id_seance = ? ";
    private static final String SQL_FILTER_PROCHAINE_SEANCE = " AND pdd.id_pdd NOT IN (SELECT pdd.id_pdd FROM ods_pdd pdd " +
        "LEFT JOIN ods_entree_ordre_jour entree ON ( entree.id_pdd = pdd.id_pdd ) " +
        "LEFT JOIN ods_odj odj ON ( entree.id_odj = odj.id_odj ) " +
        "WHERE odj.id_seance !=? AND odj.id_type_odj =2) ";
    private static final String SQL_FILTER_ARCHIVES_AVEC_PS = " AND pdd.id_pdd IN (SELECT pdd.id_pdd FROM ods_pdd pdd" +
        "LEFT JOIN ods_entree_ordre_jour entree ON ( entree.id_pdd = pdd.id_pdd )" +
        "LEFT JOIN ods_odj odj ON ( entree.id_odj = odj.id_odj )" + "WHERE odj.id_seance !=? AND odj.id_type_odj =2)";
    private static final String SQL_FILTER_ARCHIVES_SANS_PS = " AND pdd.id_pdd IN (SELECT pdd.id_pdd FROM ods_pdd pdd" +
        "LEFT JOIN ods_entree_ordre_jour entree ON ( entree.id_pdd = pdd.id_pdd )" +
        "LEFT JOIN ods_odj odj ON ( entree.id_odj = odj.id_odj ) WHERE odj.id_type_odj =2)";
    private static final String SQL_FILTER_TYPE = " AND pdd.type_pdd = ? ";
    private static final String SQL_FILTER_CATEGORIE = " AND pdd.id_categorie = ? ";
    private static final String SQL_FILTER_STATUT = " AND pdd.id_statut = ? ";
    private static final String SQL_FILTER_NOSTATUT = " AND pdd.id_statut IS NULL ";
    private static final String SQL_FILTER_DIRECTION = " AND pdd.id_direction = ? ";
    private static final String SQL_FILTER_FORMATION_CONSEIL = " AND pdd.id_formation_conseil = ? ";
    private static final String SQL_FILTER_MODE_INTRO = " AND pdd.mode_introduction = ? ";
    private static final String SQL_FILTER_GROUPE = " AND pdd.id_groupe = ? ";
    private static final String SQL_FILTER_PUBLICATION = " AND pdd.en_ligne = ? ";
    private static final String SQL_FILTER_REFERENCE = " AND pdd.reference = ? ";
    private static final String SQL_FILTER_DELEGATION_SERVICE = " AND pdd.delegations_services = ? ";
    private static final String SQL_FILTER_TYPE_ORDRE_DU_JOUR = " AND odj.id_type_odj = ? ";
    private static final String SQL_FILTER_INSCRIT_ODJ = " AND ( odj.id_odj =? or odj.id_odj =? ) ";
    private static final String SQL_FILTER_NOT_INSCRIT_ODJ = " AND pdd.id_pdd NOT IN (" +
        "SELECT pdd.id_pdd  FROM ods_pdd pdd " +
        "LEFT JOIN ods_entree_ordre_jour entree on (entree.id_pdd = pdd.id_pdd) " +
        "LEFT JOIN ods_odj odj on (entree.id_odj = odj.id_odj) " + "WHERE  odj.id_odj =? or odj.id_odj =? ) ";
    private static final String SQL_FILTER_REFERENCE_LIKE = " AND pdd.reference LIKE ? ";
    private static final String SQL_FILTRE_EN_LIGNE = " AND pdd.en_ligne=? ";
    private static final String SQL_FILTER_DATE_PUBLICATION = " AND pdd.date_publication > ? ";
    private static final String SQL_FILTER_DATE_RETOUR_CONTROLE_LEGALITE = " AND pdd.date_retour_ctrl_legalite >= ? AND pdd.date_retour_ctrl_legalite <= ? ";
    private static final String SQL_QUERY_NEW_PK_PDD = "SELECT max( id_pdd ) FROM ods_pdd";
    private static final String SQL_QUERY_INSERT_PDD = " INSERT INTO ods_pdd (id_pdd, id_categorie, id_direction, id_formation_conseil, reference, type_pdd, delegations_services, mode_introduction, objet, pieces_manuelles, en_ligne, id_groupe, date_vote, date_retour_ctrl_legalite, date_publication) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), NOW()) ";
    private static final String SQL_QUERY_INSERT_COEMETRICE = " INSERT INTO ods_co_emettrice (id_pdd, id_direction, code_projet) VALUES ( ?, ?, ?)";
    private static final String SQL_QUERY_NEW_PK_ARRONDISSEMENT = "SELECT max( id_arrondissement ) FROM ods_arrondissement_du_pdd";
    private static final String SQL_QUERY_INSERT_ARRONDISSEMENT = " INSERT INTO ods_arrondissement_du_pdd (id_arrondissement, id_pdd, arrondissement) VALUES ( ?, ?, ?)";
    private static final String SQL_QUERY_DELETE = " DELETE FROM ods_pdd WHERE id_pdd = ?  ";
    private static final String SQL_QUERY_DELETE_PDD_IN_RATTACHE = "DELETE FROM  ods_va_rattache_pdd where id_pdd=?";
    private static final String SQL_QUERY_DELETE_DIRECTION_COEMETRICE = " DELETE FROM ods_co_emettrice WHERE id_pdd = ?  ";
    private static final String SQL_QUERY_DELETE_ARRONDISSEMENT = " DELETE FROM ods_arrondissement_du_pdd WHERE id_pdd = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE ods_pdd SET id_categorie = ?, id_direction = ?, id_formation_conseil = ?, reference = ?, type_pdd = ?, delegations_services = ?, mode_introduction = ?, objet = ?, pieces_manuelles = ?, en_ligne = ?, id_groupe = ?, version = ?, id_statut = ?, date_vote = ?, date_retour_ctrl_legalite = ?, date_publication = ? WHERE id_pdd = ?";
    private static final String SQL_QUERY_PUBLIER = " UPDATE ods_pdd SET date_publication = ?, en_ligne = ?, version = ? WHERE id_pdd = ?  ";
    private static final String SQL_QUERY_VA_RATTACHE_LIST_BY_PDD = "SELECT * FROM ods_va_rattache_pdd vrp " +
        "LEFT JOIN ods_voeu_amendement va  on(vrp.id_va = va.id_va)  " +
        "LEFT JOIN ods_statut statut on(va.id_statut = statut.id_statut)" +
        "LEFT JOIN ods_fichier fdelib on(va.id_delib=fdelib.id_document)" +
        "LEFT JOIN ods_fascicule fa on(va.id_fascicule=fa.id_fascicule )" +
        "LEFT JOIN ods_fichier fc on(va.id_texte_initial = fc.id_document)" +
        "WHERE vrp.id_pdd = ? GROUP BY va.id_va ORDER BY vrp.numero_ordre ";
    private static final String SQL_QUERY_NEW_NUM_ORDRE = "SELECT max( numero_ordre ) FROM ods_va_rattache_pdd WHERE id_pdd = ? ";
    private static final String SQL_QUERY_INSERT_VA_RATTACHE_PDD = " INSERT INTO ods_va_rattache_pdd (id_va, id_pdd, numero_ordre) VALUES ( ?, ?, ?) ";
    private static final String SQL_QUERY_DELETE_VA_RATTACHE_PDD = " DELETE FROM ods_va_rattache_pdd WHERE id_pdd = ? AND id_va = ? ";
    private static final String SQL_QUERY_SELECT_ID_ARRONDISSEMENT = "SELECT id_arrondissement FROM ods_arrondissement_du_pdd " +
        "WHERE arrondissement = ? AND id_pdd = ?";

    /**
     * renvoie la liste des pdd de la prochaine seance
     * @param plugin Plugin
     * @param filter PDDFilter
     * @return List<PDD> liste d'objets Fichier
     */
    public List<PDD> selectByFilter( PDDFilter filter, Plugin plugin )
    {
        List<PDD> pdds = new ArrayList<PDD>(  );

        String strSQL = SQL_QUERY_PDD_LIST;
        strSQL += ( ( filter.containsTypeCriteria(  ) ) ? SQL_FILTER_TYPE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsSeanceCriteria(  ) ) ? SQL_FILTER_SEANCE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsProchaineSeanceCriteria(  ) ) ? SQL_FILTER_PROCHAINE_SEANCE
                                                                   : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsArchiveAvecPSCriteria(  ) ) ? SQL_FILTER_ARCHIVES_AVEC_PS
                                                                 : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.getArchiveSansPS(  ) ) ? SQL_FILTER_ARCHIVES_SANS_PS : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsCategorieDeliberationCriteria(  ) ) ? SQL_FILTER_CATEGORIE
                                                                         : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsDirectionCriteria(  ) ) ? SQL_FILTER_DIRECTION : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsFormationConseilCriteria(  ) ) ? SQL_FILTER_FORMATION_CONSEIL
                                                                    : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsModeIntroductionCriteria(  ) ) ? SQL_FILTER_MODE_INTRO
                                                                    : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsGroupeDepositaireCriteria(  ) ) ? SQL_FILTER_GROUPE
                                                                     : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsPublicationCriteria(  ) ) ? SQL_FILTER_PUBLICATION
                                                               : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsReferenceCriteria(  ) ) ? SQL_FILTER_REFERENCE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsDelegationServiceCriteria(  ) ) ? SQL_FILTER_DELEGATION_SERVICE
                                                                     : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsDatePublicationCriteria(  ) ) ? SQL_FILTER_DATE_PUBLICATION
                                                                   : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsDateRetourControleDeLegaliteCriteria(  ) )
        ? SQL_FILTER_DATE_RETOUR_CONTROLE_LEGALITE : OdsConstants.CONSTANTE_CHAINE_VIDE );

        if ( filter.containsStatutCriteria(  ) )
        {
            if ( filter.getStatut(  ) == 0 )
            {
                strSQL += SQL_FILTER_NOSTATUT;
            }
            else
            {
                strSQL += SQL_FILTER_STATUT;
            }
        }

        strSQL += ( ( filter.containsTypeOrdreDuJourCriteria(  ) ) ? SQL_FILTER_TYPE_ORDRE_DU_JOUR
                                                                   : OdsConstants.CONSTANTE_CHAINE_VIDE );

        if ( filter.containsInscritODJCriteria(  ) )
        {
            if ( filter.getInscritODJ(  ) == 0 )
            {
                strSQL += SQL_FILTER_NOT_INSCRIT_ODJ;
            }
            else
            {
                strSQL += SQL_FILTER_INSCRIT_ODJ;
            }
        }

        strSQL += SQL_GROUP_BY_ID_PDD;
        strSQL += SQL_QUERY_ORDER_BY_REFERENCE;

        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );
        int nIndex = 1;

        if ( filter.containsTypeCriteria(  ) )
        {
            daoUtil.setString( nIndex, filter.getType(  ) );
            nIndex++;
        }

        if ( filter.containsSeanceCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getSeance(  ) );
            nIndex++;
        }

        if ( filter.containsProchaineSeanceCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getProchaineSeance(  ) );
            nIndex++;
        }

        if ( filter.containsArchiveAvecPSCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getArchiveAvecPS(  ) );
            nIndex++;
        }

        if ( filter.containsCategorieDeliberationCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getCategorieDeliberation(  ) );
            nIndex++;
        }

        if ( filter.containsDirectionCriteria(  ) )
        {
            daoUtil.setString( nIndex, filter.getDirection(  ) );
            nIndex++;
        }

        if ( filter.containsFormationConseilCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdFormationConseil(  ) );
            nIndex++;
        }

        if ( filter.containsModeIntroductionCriteria(  ) )
        {
            daoUtil.setString( nIndex, filter.getModeIntroduction(  ) );
            nIndex++;
        }

        if ( filter.containsGroupeDepositaireCriteria(  ) )
        {
            daoUtil.setInt( nIndex, Integer.parseInt( filter.getGroupeDepositaire(  ) ) );
            nIndex++;
        }

        if ( filter.containsPublicationCriteria(  ) )
        {
            daoUtil.setBoolean( nIndex, ( filter.getPublication(  ) == 1 ) ? true : false );
            nIndex++;
        }

        if ( filter.containsReferenceCriteria(  ) )
        {
            daoUtil.setString( nIndex, filter.getReference(  ) );
            nIndex++;
        }

        if ( filter.containsDelegationServiceCriteria(  ) )
        {
            daoUtil.setBoolean( nIndex, ( filter.getDelegationService(  ) == 1 ) ? true : false );
            nIndex++;
        }

        if ( filter.containsDatePublicationCriteria(  ) )
        {
            daoUtil.setTimestamp( nIndex, filter.getDatePublication(  ) );
            nIndex++;
        }

        if ( filter.containsDateRetourControleDeLegaliteCriteria(  ) )
        {
            Calendar caldate = new GregorianCalendar(  );
            caldate.setTimeInMillis( filter.getDateRetourControleDeLegalite(  ).getTime(  ) );
            caldate.set( Calendar.HOUR_OF_DAY, caldate.getActualMaximum( Calendar.HOUR_OF_DAY ) );
            caldate.set( Calendar.MINUTE, caldate.getActualMaximum( Calendar.MINUTE ) );
            caldate.set( Calendar.SECOND, caldate.getActualMaximum( Calendar.SECOND ) );
            caldate.set( Calendar.MILLISECOND, 0 );

            Timestamp tsFin = new Timestamp( caldate.getTimeInMillis(  ) );
            daoUtil.setTimestamp( nIndex, filter.getDateRetourControleDeLegalite(  ) );
            nIndex++;
            daoUtil.setTimestamp( nIndex, tsFin );
            nIndex++;
        }

        if ( filter.containsStatutCriteria(  ) && ( filter.getStatut(  ) > 0 ) )
        {
            daoUtil.setInt( nIndex, filter.getStatut(  ) );
            nIndex++;
        }

        if ( filter.containsTypeOrdreDuJourCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getTypeOrdreDuJour(  ) );
            nIndex++;
        }

        if ( filter.containsInscritODJCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdOdjReferenceFormationConseilGeneral(  ) );
            nIndex++;
            daoUtil.setInt( nIndex, filter.getIdOdjReferenceFormationConseilMunicipal(  ) );
            nIndex++;
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            PDD pdd = getBasePropertyFichier( daoUtil );
            pdd = setOtherPropertyPdd( daoUtil, plugin, pdd );
            pdds.add( pdd );
        }

        daoUtil.free(  );

        return pdds;
    }

    /**
     * renvoie la liste des pdd de la prochaine seance
     * @param plugin Plugin
     * @param filter PDDFilter
     * @param  nIdDirection nIdDirection
     * @return List<PDD> liste d'objets Fichier
     */
    public List<PDD> selectByFilterByDirectionOrDirectionCoemetrice( PDDFilter filter, Plugin plugin, int nIdDirection )
    {
        List<PDD> pdds = new ArrayList<PDD>(  );

        String strSQL = SQL_QUERY_PDD_LIST_BY_DIRECTION_OR_DIRECTION_CO;
        strSQL += ( ( filter.containsTypeCriteria(  ) ) ? SQL_FILTER_TYPE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsSeanceCriteria(  ) ) ? SQL_FILTER_SEANCE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsProchaineSeanceCriteria(  ) ) ? SQL_FILTER_PROCHAINE_SEANCE
                                                                   : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsArchiveAvecPSCriteria(  ) ) ? SQL_FILTER_ARCHIVES_AVEC_PS
                                                                 : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsCategorieDeliberationCriteria(  ) ) ? SQL_FILTER_CATEGORIE
                                                                         : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsFormationConseilCriteria(  ) ) ? SQL_FILTER_FORMATION_CONSEIL
                                                                    : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsModeIntroductionCriteria(  ) ) ? SQL_FILTER_MODE_INTRO
                                                                    : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsGroupeDepositaireCriteria(  ) ) ? SQL_FILTER_GROUPE
                                                                     : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsPublicationCriteria(  ) ) ? SQL_FILTER_PUBLICATION
                                                               : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsReferenceCriteria(  ) ) ? SQL_FILTER_REFERENCE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsDelegationServiceCriteria(  ) ) ? SQL_FILTER_DELEGATION_SERVICE
                                                                     : OdsConstants.CONSTANTE_CHAINE_VIDE );

        if ( filter.containsStatutCriteria(  ) )
        {
            if ( filter.getStatut(  ) == 0 )
            {
                strSQL += SQL_FILTER_NOSTATUT;
            }
            else
            {
                strSQL += SQL_FILTER_STATUT;
            }
        }

        strSQL += ( ( filter.containsTypeOrdreDuJourCriteria(  ) ) ? SQL_FILTER_TYPE_ORDRE_DU_JOUR
                                                                   : OdsConstants.CONSTANTE_CHAINE_VIDE );

        if ( filter.containsInscritODJCriteria(  ) )
        {
            if ( filter.getInscritODJ(  ) == 0 )
            {
                strSQL += SQL_FILTER_NOT_INSCRIT_ODJ;
            }
            else
            {
                strSQL += SQL_FILTER_INSCRIT_ODJ;
            }
        }

        strSQL += SQL_GROUP_BY_ID_PDD;
        strSQL += SQL_QUERY_ORDER_BY_REFERENCE;

        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );
        daoUtil.setInt( 1, nIdDirection );
        daoUtil.setInt( 2, nIdDirection );

        int nIndex = 3;

        if ( filter.containsTypeCriteria(  ) )
        {
            daoUtil.setString( nIndex, filter.getType(  ) );
            nIndex++;
        }

        if ( filter.containsSeanceCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getSeance(  ) );
            nIndex++;
        }

        if ( filter.containsProchaineSeanceCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getProchaineSeance(  ) );
            nIndex++;
        }

        if ( filter.containsArchiveAvecPSCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getArchiveAvecPS(  ) );
            nIndex++;
        }

        if ( filter.containsCategorieDeliberationCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getCategorieDeliberation(  ) );
            nIndex++;
        }

        if ( filter.containsFormationConseilCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdFormationConseil(  ) );
            nIndex++;
        }

        if ( filter.containsModeIntroductionCriteria(  ) )
        {
            daoUtil.setString( nIndex, filter.getModeIntroduction(  ) );
            nIndex++;
        }

        if ( filter.containsGroupeDepositaireCriteria(  ) )
        {
            daoUtil.setString( nIndex, filter.getGroupeDepositaire(  ) );
            nIndex++;
        }

        if ( filter.containsPublicationCriteria(  ) )
        {
            daoUtil.setBoolean( nIndex, ( filter.getPublication(  ) == 1 ) ? true : false );
            nIndex++;
        }

        if ( filter.containsReferenceCriteria(  ) )
        {
            daoUtil.setString( nIndex, filter.getReference(  ) );
            nIndex++;
        }

        if ( filter.containsDelegationServiceCriteria(  ) )
        {
            daoUtil.setBoolean( nIndex, ( filter.getDelegationService(  ) == 1 ) ? true : false );
            nIndex++;
        }

        if ( filter.containsStatutCriteria(  ) && ( filter.getStatut(  ) > 0 ) )
        {
            daoUtil.setInt( nIndex, filter.getStatut(  ) );
            nIndex++;
        }

        if ( filter.containsTypeOrdreDuJourCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getTypeOrdreDuJour(  ) );
            nIndex++;
        }

        if ( filter.containsInscritODJCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdOdjReferenceFormationConseilGeneral(  ) );
            nIndex++;
            daoUtil.setInt( nIndex, filter.getIdOdjReferenceFormationConseilMunicipal(  ) );
            nIndex++;
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            PDD pdd = getBasePropertyFichier( daoUtil );

            pdd = setOtherPropertyPdd( daoUtil, plugin, pdd );

            pdds.add( pdd );
        }

        daoUtil.free(  );

        return pdds;
    }

    /**
     * renvoie le pdd en fonction de l'id passe en parametre
     * @param nKey int id du PDD
     * @param plugin Plugin
     * @return PDD le pdd qui a ete trouvé
     */
    public PDD selectByPrimaryKey( int nKey, Plugin plugin )
    {
        PDD pdd = null;
        String strSQL = SQL_QUERY_PDD_LIST + SQL_FILTER_PRIMARY_KEY;
        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            pdd = getBasePropertyFichier( daoUtil );
            setOtherPropertyPdd( daoUtil, plugin, pdd );
        }

        daoUtil.free(  );

        return pdd;
    }

    /**
     * Permet de creer un objet PDD avec ses propriétés de base
     * @param daoUtil DAOUtil
     * @return PDD avec les propriétés remplies
     */
    private PDD getBasePropertyFichier( DAOUtil daoUtil )
    {
        PDD pdd = new PDD(  );
        pdd.setIdPdd( daoUtil.getInt( "id_pdd" ) );
        pdd.setReference( daoUtil.getString( "reference" ) );
        pdd.setTypePdd( daoUtil.getString( "type_pdd" ) );
        pdd.setDelegationsServices( daoUtil.getBoolean( "delegations_services" ) );
        pdd.setModeIntroduction( daoUtil.getString( "mode_introduction" ) );
        pdd.setObjet( daoUtil.getString( "objet" ) );
        pdd.setPiecesManuelles( daoUtil.getBoolean( "pieces_manuelles" ) );
        pdd.setDateVote( daoUtil.getTimestamp( "date_vote" ) );
        pdd.setDateRetourCtrlLegalite( daoUtil.getTimestamp( "date_retour_ctrl_legalite" ) );
        pdd.setEnLigne( daoUtil.getBoolean( "en_ligne" ) );
        pdd.setDatePublication( daoUtil.getTimestamp( "date_publication" ) );
        pdd.setVersion( daoUtil.getInt( "version" ) );

        return pdd;
    }

    /**
     * permet de remplir le pdd avec les propriétés qui ont des contraintes sur d'autres tables
     *
     * @param daoUtil DAOUtil
     * @param pdd pdd qui va etre rempli
     * @param plugin Plugin
     * @return PDD avec les propriétés remplis
     */
    private PDD setOtherPropertyPdd( DAOUtil daoUtil, Plugin plugin, PDD pdd )
    {
        if ( daoUtil.getObject( "pdd.id_categorie" ) != null )
        {
            CategorieDeliberation categorie = new CategorieDeliberation(  );
            categorie.setIdCategorie( daoUtil.getInt( "cd.id_categorie" ) );
            categorie.setCode( daoUtil.getInt( "cd.code_categorie" ) );
            categorie.setLibelle( daoUtil.getString( "cd.libelle_categorie" ) );
            categorie.setActif( daoUtil.getBoolean( "cd.actif" ) );
            pdd.setCategorieDeliberation( categorie );
        }

        if ( daoUtil.getObject( "pdd.id_direction" ) != null )
        {
            Direction direction = new Direction(  );

            direction.setIdDirection( daoUtil.getInt( "dir.id_direction" ) );
            direction.setCode( daoUtil.getString( "dir.code_direction" ) );
            direction.setLibelleCourt( daoUtil.getString( "dir.libelle_court" ) );
            direction.setLibelleLong( daoUtil.getString( "dir.libelle_long" ) );
            pdd.setDirection( direction );
        }

        if ( daoUtil.getObject( "pdd.id_formation_conseil" ) != null )
        {
            FormationConseil formationConseil = new FormationConseil(  );
            formationConseil.setIdFormationConseil( daoUtil.getInt( "fc.id_formation_conseil" ) );
            formationConseil.setLibelle( daoUtil.getString( "libelle_formation_conseil" ) );
            pdd.setFormationConseil( formationConseil );
        }

        if ( daoUtil.getObject( "pdd.id_groupe" ) != null )
        {
            GroupePolitique groupePolitique = new GroupePolitique(  );
            groupePolitique.setIdGroupe( daoUtil.getInt( "gr.id_groupe" ) );
            groupePolitique.setNomGroupe( daoUtil.getString( "gr.nom_groupe" ) );
            groupePolitique.setNomComplet( daoUtil.getString( "gr.nom_complet" ) );
            groupePolitique.setActif( daoUtil.getBoolean( "gr.actif" ) );
            pdd.setGroupePolitique( groupePolitique );
        }

        if ( daoUtil.getObject( "pdd.id_statut" ) != null )
        {
            Statut statut = new Statut(  );
            statut.setIdStatut( daoUtil.getInt( "st.id_statut" ) );
            statut.setLibelle( daoUtil.getString( "st.libelle_statut" ) );
            statut.setPourAmendement( daoUtil.getBoolean( "st.est_pour_amendement" ) );
            statut.setPourVoeu( daoUtil.getBoolean( "st.est_pour_voeu" ) );
            statut.setPourPDD( daoUtil.getBoolean( "st.est_pour_pdd" ) );
            pdd.setStatut( statut );
        }

        //gestion de la liste des va rattaché
        int idPdd = daoUtil.getInt( "pdd.id_pdd" );
        List<VoeuAmendement> voeuAmendements = selectVoeuxAmendementsbyIdPDD( idPdd, plugin );
        pdd.setVoeuxAmendements( voeuAmendements );

        List<VoeuAmendement> amendements = new ArrayList<VoeuAmendement>(  );
        List<VoeuAmendement> voeuxRattaches = new ArrayList<VoeuAmendement>(  );
        List<VoeuAmendement> voeuxNonRattaches = new ArrayList<VoeuAmendement>(  );

        for ( VoeuAmendement va : voeuAmendements )
        {
            String type = va.getType(  );

            if ( VoeuAmendementJspBean.CONSTANTE_TYPE_A.equals( type ) ||
                    VoeuAmendementJspBean.CONSTANTE_TYPE_LR.equals( type ) )
            {
                amendements.add( va );
            }

            if ( VoeuAmendementJspBean.CONSTANTE_TYPE_VR.equals( type ) )
            {
                voeuxRattaches.add( va );
            }

            if ( VoeuAmendementJspBean.CONSTANTE_TYPE_VNR.equals( type ) )
            {
                voeuxNonRattaches.add( va );
            }
        }

        pdd.setAmendements( amendements );
        pdd.setVoeuxRattaches( voeuxRattaches );
        pdd.setVoeuxNonRattaches( voeuxNonRattaches );

        return pdd;
    }

    /**
     *
     * @param idPdd id du PDD
     * @param plugin Plugin
     * @return List<VoeuAmendement>
     */
    private List<VoeuAmendement> selectVoeuxAmendementsbyIdPDD( int idPdd, Plugin plugin )
    {
        List<VoeuAmendement> voeuAmendements = new ArrayList<VoeuAmendement>(  );

        String strSQL = SQL_QUERY_VA_RATTACHE_LIST_BY_PDD;
        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );
        daoUtil.setInt( 1, idPdd );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            VoeuAmendement voeuAmendement = new VoeuAmendement(  );
            voeuAmendement.setIdVoeuAmendement( daoUtil.getInt( "va.id_va" ) );
            voeuAmendement.setType( daoUtil.getString( "type" ) );
            voeuAmendement.setReference( daoUtil.getString( "reference_va" ) );
            voeuAmendement.setObjet( daoUtil.getString( "objet" ) );
            voeuAmendement.setDeposeExecutif( daoUtil.getBoolean( "depose_executif" ) );
            voeuAmendement.setDateRetourControleLegalite( daoUtil.getTimestamp( "date_retour_ctrl_legalite" ) );
            voeuAmendement.setEnLigne( daoUtil.getBoolean( "en_ligne" ) );
            voeuAmendement.setDatePublication( daoUtil.getTimestamp( "date_publication" ) );
            voeuAmendement.setVersion( daoUtil.getInt( "version" ) );

            Fascicule fascicule = new Fascicule(  );
            fascicule.setIdFascicule( daoUtil.getInt( "fa.id_fascicule" ) );
            fascicule.setNomFascicule( daoUtil.getString( "fa.nom_fascicule" ) );
            fascicule.setCodeFascicule( daoUtil.getString( "fa.code_fascicule" ) );
            fascicule.setNumeroOrdre( daoUtil.getInt( "fa.numero_ordre" ) );
            voeuAmendement.setFascicule( fascicule );

            if ( daoUtil.getObject( "va.id_texte_initial" ) != null )
            {
                Fichier fichier = new Fichier(  );
                fichier.setId( daoUtil.getInt( "fc.id_document" ) );
                fichier.setTexte( daoUtil.getString( "fc.texte" ) );
                fichier.setVersion( daoUtil.getInt( "fc.version" ) );
                fichier.setExtension( daoUtil.getString( "fc.extension" ) );
                fichier.setTaille( daoUtil.getInt( "fc.taille" ) );
                fichier.setNom( daoUtil.getString( "fc.nom" ) );
                fichier.setEnLigne( daoUtil.getBoolean( "fc.en_ligne" ) );
                fichier.setDatePublication( daoUtil.getTimestamp( "fc.date_publication" ) );
                fichier.setTitre( daoUtil.getString( "fc.intitule" ) );
                voeuAmendement.setFichier( fichier );
            }

            if ( daoUtil.getObject( "va.id_delib" ) != null )
            {
                Fichier fichier = new Fichier(  );
                fichier.setId( daoUtil.getInt( "fdelib.id_document" ) );
                fichier.setTexte( daoUtil.getString( "fdelib.texte" ) );
                fichier.setVersion( daoUtil.getInt( "fdelib.version" ) );
                fichier.setExtension( daoUtil.getString( "fdelib.extension" ) );
                fichier.setTaille( daoUtil.getInt( "fdelib.taille" ) );
                fichier.setNom( daoUtil.getString( "fdelib.nom" ) );
                fichier.setEnLigne( daoUtil.getBoolean( "fdelib.en_ligne" ) );
                fichier.setDatePublication( daoUtil.getTimestamp( "fdelib.date_publication" ) );
                fichier.setTitre( daoUtil.getString( "fdelib.intitule" ) );
                voeuAmendement.setDeliberation( fichier );
            }

            if ( daoUtil.getObject( "va.id_statut" ) != null )
            {
                Statut statut = new Statut(  );
                statut.setIdStatut( daoUtil.getInt( "statut.id_statut" ) );
                statut.setLibelle( daoUtil.getString( "statut.libelle_statut" ) );
                voeuAmendement.setStatut( statut );
            }

            voeuAmendements.add( voeuAmendement );
        }

        daoUtil.free(  );

        return voeuAmendements;
    }

    /**
     * Crée un nouveau PDD à partir de l’objet pdd passé en paramètre
     * @param pdd la Projet de deliberation à insérer
     * @param plugin Plugin
     * @return ProjetDeDeliberation pdd creer
     */
    public PDD insert( PDD pdd, Plugin plugin )
    {
        int newPrimaryKey = newPrimaryKeyPdd( plugin );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_PDD, plugin );
        pdd.setIdPdd( newPrimaryKey );
        daoUtil.setInt( 1, pdd.getIdPdd(  ) );

        if ( pdd.getCategorieDeliberation(  ) == null )
        {
            daoUtil.setIntNull( 2 );
        }
        else
        {
            daoUtil.setInt( 2, pdd.getCategorieDeliberation(  ).getIdCategorie(  ) );
        }

        if ( pdd.getDirection(  ) == null )
        {
            daoUtil.setIntNull( 3 );
        }
        else
        {
            daoUtil.setInt( 3, pdd.getDirection(  ).getIdDirection(  ) );
        }

        if ( pdd.getFormationConseil(  ) == null )
        {
            daoUtil.setIntNull( 4 );
        }
        else
        {
            daoUtil.setInt( 4, pdd.getFormationConseil(  ).getIdFormationConseil(  ) );
        }

        daoUtil.setString( 5, pdd.getReference(  ) );
        daoUtil.setString( 6, pdd.getTypePdd(  ) );
        daoUtil.setBoolean( 7, pdd.isDelegationsServices(  ) );
        daoUtil.setString( 8, pdd.getModeIntroduction(  ) );
        daoUtil.setString( 9, pdd.getObjet(  ) );
        daoUtil.setBoolean( 10, pdd.isPiecesManuelles(  ) );
        daoUtil.setBoolean( 11, pdd.isEnLigne(  ) );

        if ( pdd.getGroupePolitique(  ) == null )
        {
            daoUtil.setIntNull( 12 );
        }
        else
        {
            daoUtil.setInt( 12, pdd.getGroupePolitique(  ).getIdGroupe(  ) );
        }

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        return pdd;
    }

    /**
     * Crée une nouvelle Direction CoEmetrice à partir de l’objet directionCoEmetrice passé en paramètre
     * @param directionCoEmetrice La direction CoEmetrice à inserer
     * @param plugin Plugin
     */
    public void insert( DirectionCoEmetrice directionCoEmetrice, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_COEMETRICE, plugin );
        daoUtil.setInt( 1, directionCoEmetrice.getIdPDD(  ) );

        if ( directionCoEmetrice.getDirection(  ) == null )
        {
            daoUtil.setIntNull( 2 );
        }
        else
        {
            daoUtil.setInt( 2, directionCoEmetrice.getDirection(  ).getIdDirection(  ) );
        }

        daoUtil.setString( 3, directionCoEmetrice.getCodeProjet(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Crée un nouvel Arrondissement à partir de l’objet arrondissement passé en paramètre
     * @param arrondissement L'arrondissement à inserer
     * @param plugin Plugin
     */
    public void insert( Arrondissement arrondissement, Plugin plugin )
    {
        int newPrimaryKey = newPrimaryKeyArrondissement( plugin );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_ARRONDISSEMENT, plugin );
        arrondissement.setIdArrondissement( newPrimaryKey );

        daoUtil.setInt( 1, arrondissement.getIdArrondissement(  ) );
        daoUtil.setInt( 2, arrondissement.getIdPDD(  ) );
        daoUtil.setInt( 3, arrondissement.getArrondissement(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * renvoie la liste des Directions CoEmetrices de l'id du pdd passé en paramètre
     * @param pddPrimaryKey Id du pdd
     * @param plugin Plugin
     * @return List<Arrondissement> Liste d'objets DirectionCoEmetrice
     */
    public List<DirectionCoEmetrice> selectDirectionsCoEmtricesByPDD( int pddPrimaryKey, Plugin plugin )
    {
        List<DirectionCoEmetrice> directionsCoEmetrices = new ArrayList<DirectionCoEmetrice>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DIRECTIONS_CO_COEMETRICE_LIST_BY_PDD, plugin );
        daoUtil.setInt( 1, pddPrimaryKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DirectionCoEmetrice directionCoEmetrice = new DirectionCoEmetrice(  );
            directionCoEmetrice.setIdPDD( daoUtil.getInt( "co.id_pdd" ) );
            directionCoEmetrice.setCodeProjet( daoUtil.getString( "co.code_projet" ) );

            if ( daoUtil.getObject( "co.id_direction" ) != null )
            {
                Direction direction = new Direction(  );
                direction.setIdDirection( daoUtil.getInt( "dir.id_direction" ) );
                direction.setCode( daoUtil.getString( "dir.code_direction" ) );
                direction.setLibelleCourt( daoUtil.getString( "dir.libelle_court" ) );
                direction.setLibelleLong( daoUtil.getString( "dir.libelle_long" ) );
                directionCoEmetrice.setDirection( direction );
            }

            directionsCoEmetrices.add( directionCoEmetrice );
        }

        daoUtil.free(  );

        return directionsCoEmetrices;
    }

    /**
     * renvoie la liste des Arrondissements de l'id du pdd passé en paramètre
     * @param pddPrimaryKey Id du pdd
     * @param plugin Plugin
     * @return List<Arrondissement> Liste d'objets Arrondissement
     */
    public List<Arrondissement> selectArrondissementsByPDD( int pddPrimaryKey, Plugin plugin )
    {
        List<Arrondissement> arrondissements = new ArrayList<Arrondissement>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ARRONDISSEMENTS_LIST_BY_PDD, plugin );
        daoUtil.setInt( 1, pddPrimaryKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Arrondissement arrondissement = new Arrondissement(  );
            arrondissement.setIdArrondissement( daoUtil.getInt( "id_arrondissement" ) );
            arrondissement.setIdPDD( daoUtil.getInt( "id_pdd" ) );
            arrondissement.setArrondissement( daoUtil.getInt( "arrondissement" ) );
            arrondissements.add( arrondissement );
        }

        daoUtil.free(  );

        return arrondissements;
    }

    /**
     * Supprime toutes les arrondissements possedant l'id du pdd passé en paramètre
     * @param nIdPDD id du pdd
     * @param plugin Plugin
     */
    public void deleteArrondissements( int nIdPDD, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ARRONDISSEMENT, plugin );
        daoUtil.setInt( 1, nIdPDD );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime toutes les directionCoEmetrice possedant l'id du pdd passé en paramètre
     * @param nIdPDD id du pdd
     * @param plugin Plugin
     */
    public void deleteCoEmetrices( int nIdPDD, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_DIRECTION_COEMETRICE, plugin );
        daoUtil.setInt( 1, nIdPDD );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Génère un nouvel identifiant
     * @param plugin Plugin
     * @return Retourne l’identifiant généré
     */
    int newPrimaryKeyPdd( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK_PDD, plugin );
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
     * Génère un nouvel identifiant
     * @param plugin Plugin
     * @return Retourne l’identifiant généré
     */
    int newPrimaryKeyArrondissement( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK_ARRONDISSEMENT, plugin );
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
     * Modifie un PDD à partir de l’objet pdd passé en paramètre
     * @param pdd la Projet de deliberation à modifier
     * @param plugin Plugin
     */
    public void store( PDD pdd, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        if ( pdd.getCategorieDeliberation(  ) == null )
        {
            daoUtil.setIntNull( 1 );
        }
        else
        {
            daoUtil.setInt( 1, pdd.getCategorieDeliberation(  ).getIdCategorie(  ) );
        }

        if ( pdd.getDirection(  ) == null )
        {
            daoUtil.setIntNull( 2 );
        }
        else
        {
            daoUtil.setInt( 2, pdd.getDirection(  ).getIdDirection(  ) );
        }

        if ( pdd.getFormationConseil(  ) == null )
        {
            daoUtil.setIntNull( 3 );
        }
        else
        {
            daoUtil.setInt( 3, pdd.getFormationConseil(  ).getIdFormationConseil(  ) );
        }

        daoUtil.setString( 4, pdd.getReference(  ) );
        daoUtil.setString( 5, pdd.getTypePdd(  ) );
        daoUtil.setBoolean( 6, pdd.isDelegationsServices(  ) );
        daoUtil.setString( 7, pdd.getModeIntroduction(  ) );
        daoUtil.setString( 8, pdd.getObjet(  ) );
        daoUtil.setBoolean( 9, pdd.isPiecesManuelles(  ) );
        daoUtil.setBoolean( 10, pdd.isEnLigne(  ) );

        if ( pdd.getGroupePolitique(  ) == null )
        {
            daoUtil.setIntNull( 11 );
        }
        else
        {
            daoUtil.setInt( 11, pdd.getGroupePolitique(  ).getIdGroupe(  ) );
        }

        daoUtil.setInt( 12, pdd.getVersion(  ) );

        if ( pdd.getStatut(  ) == null )
        {
            daoUtil.setIntNull( 13 );
        }
        else
        {
            daoUtil.setInt( 13, pdd.getStatut(  ).getIdStatut(  ) );
        }

        daoUtil.setTimestamp( 14, pdd.getDateVote(  ) );
        daoUtil.setTimestamp( 15, pdd.getDateRetourCtrlLegalite(  ) );

        daoUtil.setTimestamp( 16, pdd.getDatePublication(  ) );
        
        daoUtil.setInt( 17, pdd.getIdPdd(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime le PDD dont l'id est celui passé en paramètre
     * @param pdd le pdd
     * @param plugin Plugin
     */
    public void delete( PDD pdd, Plugin plugin )
    {
        // suppression dans la table ods_va_rattache_pdd des pdds rattachés au vA à supprimer
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_PDD_IN_RATTACHE, plugin );
        daoUtil.setInt( 1, pdd.getIdPdd(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, pdd.getIdPdd(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Permet de publier ou de dépublier un pdd
     * Publication d'un pdd
     * Si isPublier égale à TRUE, alors la méthode <b>publie</b> le pdd et incrémente la version<BR>
     * Sinon la méthode change le statut enLigne d'un pdd a FALSE
     *
     * @param nKey id du pdd
     * @param datePublication TimeStamp date de publication
     * @param version verssion
     * @param isPublier enLigne
     * @param plugin Plugin
     */
    public void publication( int nKey, Timestamp datePublication, int version, boolean isPublier, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_PUBLIER, plugin );

        if ( isPublier )
        {
            daoUtil.setTimestamp( 1, datePublication );
        }
        else
        {
            daoUtil.setTimestamp( 1, null );
        }

        daoUtil.setBoolean( 2, isPublier );
        daoUtil.setInt( 3, version );
        daoUtil.setInt( 4, nKey );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Génère un nouvel identifiant
     * @param plugin Plugin
     * @param nIdPDD id du PDD
     * @return Retourne l’identifiant généré
     */
    int newNumeroOrdre( Plugin plugin, int nIdPDD )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_NUM_ORDRE, plugin );
        daoUtil.setInt( 1, nIdPDD );
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
     * Ajoute un VoeuxAmendement au pdd
     *
     * @param nIdVA id du Voeux amendement
     * @param nIdPDD id du pdd
     * @param plugin Plugin
     */
    public void addVoeuAmendement( int nIdVA, int nIdPDD, Plugin plugin )
    {
        int nNewNumeroOrdre = newNumeroOrdre( plugin, nIdPDD );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_VA_RATTACHE_PDD, plugin );
        daoUtil.setInt( 1, nIdVA );
        daoUtil.setInt( 2, nIdPDD );
        daoUtil.setInt( 3, nNewNumeroOrdre );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime tous les élements contenus dans oldVoeuAmendements
     * et insére les nouveaux veuxAmendements rattachés aux pdd
     *
     * @param oldVoeuAmendements List<VoeuAmendement>
     * @param newVoeuAmendements List<VoeuAmendement>
     * @param idPDD idPDD
     * @param plugin Plugin
     */
    public void updateVoeuxAmendement( List<VoeuAmendement> oldVoeuAmendements,
        List<VoeuAmendement> newVoeuAmendements, int idPDD, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_VA_RATTACHE_PDD, plugin );
        daoUtil.setInt( 1, idPDD );

        for ( VoeuAmendement va : oldVoeuAmendements )
        {
            daoUtil.setInt( 2, va.getIdVoeuAmendement(  ) );
            daoUtil.executeUpdate(  );
        }

        daoUtil.free(  );

        daoUtil = new DAOUtil( SQL_QUERY_INSERT_VA_RATTACHE_PDD, plugin );

        int i = 1;

        for ( VoeuAmendement va : newVoeuAmendements )
        {
            daoUtil.setInt( 1, va.getIdVoeuAmendement(  ) );
            daoUtil.setInt( 2, idPDD );
            daoUtil.setInt( 3, i );
            daoUtil.executeUpdate(  );
            i++;
        }

        daoUtil.free(  );
    }

    /**
     * Retourne l'identifiant unique pour le couple ( arrondissement, pdd )
     * @param strArrondissement l'arrondissement
     * @param pdd le pdd
     * @param plugin le plugin
     * @return l'identifiant unique pour le couple ( arrondissement, pdd )
     */
    public int selectIdArrondissement( String strArrondissement, PDD pdd, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ID_ARRONDISSEMENT, plugin );
        daoUtil.setInt( 1, Integer.parseInt( strArrondissement ) );
        daoUtil.setInt( 2, pdd.getIdPdd(  ) );
        daoUtil.executeQuery(  );

        int nIdArrondissement = -1;

        while ( daoUtil.next(  ) )
        {
            nIdArrondissement = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nIdArrondissement;
    }

    /**
     * Retourne la liste des pdds dont la référence contient le texte passé en argument
     * @param strRecherche la chaîne de caractères recherchée
     * @param bArchive recherche sur les archives ou sur la prochaine séance
     * @param prochaineSeance la prochaine séance
     * @param plugin le plugin
     * @return la liste des pdds dont la référence contient <b>strRecherche</b>
     */
    public List<PDD> selectPddsSearchedByReference( String strRecherche, boolean bArchive, Seance prochaineSeance,
        Plugin plugin )
    {
        List<PDD> listPdds = new ArrayList<PDD>(  );
        PDD pdd;
        String strQuery = SQL_QUERY_PDD_LIST + SQL_FILTER_REFERENCE_LIKE + SQL_FILTRE_EN_LIGNE;

        if ( bArchive )
        {
            strQuery += SQL_FILTER_ARCHIVES_AVEC_PS;
        }
        else
        {
            strQuery += SQL_FILTER_PROCHAINE_SEANCE;
        }

        strQuery += SQL_GROUP_BY_ID_PDD;

        DAOUtil daoUtil = new DAOUtil( strQuery, plugin );
        daoUtil.setString( 1, "%" + strRecherche + "%" );
        daoUtil.setBoolean( 2, true );
        daoUtil.setInt( 3, prochaineSeance.getIdSeance(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            pdd = new PDD(  );
            pdd.setIdPdd( daoUtil.getInt( "id_pdd" ) );
            pdd.setReference( daoUtil.getString( "reference" ) );
            pdd.setObjet( daoUtil.getString( "objet" ) );
            pdd.setDatePublication( daoUtil.getTimestamp( "date_publication" ) );
            pdd.setVersion( daoUtil.getInt( "version" ) );
            listPdds.add( pdd );
        }

        daoUtil.free(  );

        return listPdds;
    }
}
