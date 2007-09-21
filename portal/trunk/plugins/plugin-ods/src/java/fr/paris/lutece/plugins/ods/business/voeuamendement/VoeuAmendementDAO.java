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
package fr.paris.lutece.plugins.ods.business.voeuamendement;

import fr.paris.lutece.plugins.ods.business.categoriedeliberation.CategorieDeliberation;
import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.fascicule.Fascicule;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitique;
import fr.paris.lutece.plugins.ods.business.ordredujour.TypeOrdreDuJourEnum;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.statut.Statut;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * VoeuAmendementDAO
 *
 */
public class VoeuAmendementDAO implements fr.paris.lutece.plugins.ods.business.voeuamendement.IVoeuAmendementDAO
{
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_va ) FROM ods_voeu_amendement ";
    private static final String CONSTANTE_TYPE_A = "A";
    private static final String CONSTANTE_TYPE_ALLV = "VRVNR";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = " SELECT va.id_va,va.type,va.reference_va, " +
        "va.objet,va.depose_executif,va.date_retour_ctrl_legalite,va.en_ligne,va.date_publication, " +
        "va.version,fc.id_formation_conseil,fc.libelle_formation_conseil,fa.id_fascicule,fa.nom_fascicule,fa.code_fascicule, " +
        "va.id_commission,fi.id_document,fi.texte,fi.version,fi.extension,fi.taille,fi.nom,fi.en_ligne,fi.date_publication,fi.intitule, " +
        "va.id_statut, va.num_deliberation, va.id_delib, va.date_vote, va.ods_id_va " +
        " FROM ods_voeu_amendement va " +
        " LEFT JOIN ods_formation_conseil fc on(va.id_formation_conseil=fc.id_formation_conseil) " +
        " LEFT JOIN ods_fascicule fa on(va.id_fascicule=fa.id_fascicule) " +
        " LEFT JOIN ods_fichier fi on(va.id_texte_initial=fi.id_document) " + " WHERE va.id_va = ? ";
    private static final String SQL_QUERY_LIASSE = "SELECT va.id_va,va.type,va.reference_va,va.objet, " +
        "va.depose_executif ,elu.id_elu,grou.id_groupe,grou.nom_groupe ,fa.id_fascicule,fa.nom_fascicule," +
        "fa.code_fascicule,va.id_texte_initial,va.en_ligne,va.date_publication,va.version,  " +
        "va.id_statut, va.num_deliberation, va.id_delib, va.date_vote, va.date_retour_ctrl_legalite, va.ods_id_va,entree.id_entree " +
        " FROM ods_voeu_amendement va " + " LEFT JOIN ods_va_depose_par vadepo on(va.id_va=vadepo.id_va) " +
        " LEFT JOIN ods_elu elu on( vadepo.id_elu=elu.id_elu) " +
        " LEFT JOIN ods_groupe grou on(elu.id_groupe=grou.id_groupe) " +
        " LEFT JOIN ods_va_rattache_pdd vapdd  on (va.id_va=vapdd.id_va) " +
        " LEFT JOIN ods_entree_ordre_jour entree on (entree.id_va = va.id_va OR entree.id_pdd = vapdd.id_pdd) " +
        " LEFT JOIN ods_odj odj on (entree.id_odj = odj.id_odj) " +
        " LEFT JOIN ods_fascicule fa on (va.id_fascicule=fa.id_fascicule) " +
        " LEFT JOIN ods_seance se on (odj.id_seance=se.id_seance) " + " WHERE odj.id_type_odj = " +
        TypeOrdreDuJourEnum.DEFINITIF.getId(  ) + " AND va.reference_va IS NOT NULL AND va.reference_va!='' ";
    private static final String SQL_QUERY_VOEUAMENDEMENT_LIST = "SELECT va.id_va,va.type,va.reference_va,va.objet, " +
        "va.depose_executif,va.date_retour_ctrl_legalite,va.en_ligne,va.date_publication,va.version, " +
        "fc.id_formation_conseil,fc.libelle_formation_conseil,fa.id_fascicule,fa.nom_fascicule, " +
        "fa.code_fascicule,va.id_texte_initial,va.id_commission, " +
        "va.id_statut, va.num_deliberation, va.id_delib, va.date_vote, va.ods_id_va " +
        " FROM ods_voeu_amendement va " +
        " LEFT JOIN ods_formation_conseil fc on(va.id_formation_conseil=fc.id_formation_conseil) " +
        " LEFT JOIN ods_fascicule fa on (va.id_fascicule=fa.id_fascicule) " +
        " LEFT JOIN ods_seance se on (fa.id_seance=se.id_seance) WHERE 1 ";
    private static final String SQL_QUERY_VOEUAMENDEMENT_LIST_AVEC_ODJ = "SELECT va.id_va,va.type,va.reference_va,va.objet, " +
        "va.depose_executif,va.date_retour_ctrl_legalite,va.en_ligne,va.date_publication,va.version, " +
        "fc.id_formation_conseil,fc.libelle_formation_conseil,fa.id_fascicule,fa.nom_fascicule, " +
        "fa.code_fascicule,va.id_texte_initial,va.id_commission, " +
        "va.id_statut, va.num_deliberation, va.id_delib, va.date_vote, va.ods_id_va " +
        " FROM ods_voeu_amendement va " + " LEFT JOIN ods_va_rattache_pdd vapdd  on (va.id_va=vapdd.id_va) " +
        " LEFT JOIN ods_entree_ordre_jour entree on (entree.id_va = va.id_va OR entree.id_pdd = vapdd.id_pdd) " +
        " LEFT JOIN ods_odj odj on (entree.id_odj = odj.id_odj) " +
        " LEFT JOIN ods_formation_conseil fc on(va.id_formation_conseil=fc.id_formation_conseil) " +
        " LEFT JOIN ods_fascicule fa on (va.id_fascicule=fa.id_fascicule) " +
        " LEFT JOIN ods_seance se on (fa.id_seance=se.id_seance) " + " WHERE 1 ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO ods_voeu_amendement(id_va,id_formation_conseil,id_commission, " +
        "ods_id_va,id_texte_initial,id_fascicule,type,reference_va,objet,depose_executif, " +
        "date_retour_ctrl_legalite,en_ligne,date_publication,version, id_statut, num_deliberation, id_delib, date_vote ) " +
        " VALUES  (?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM ods_voeu_amendement WHERE id_va = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE ods_voeu_amendement set id_va=?," +
        "id_formation_conseil=?,id_commission=?,ods_id_va=?,id_fascicule=?,type=?,reference_va=?,objet=?,depose_executif=?," +
        "date_retour_ctrl_legalite=?,en_ligne=?,date_publication=?,version=?, " +
        "id_statut=?, num_deliberation=?, id_delib=?, date_vote=? " + " where id_va=? ";
    private static final String SQL_QUERY_INSERT_ELU_IN_DEPOSE_PAR = "INSERT INTO ods_va_depose_par(id_elu,id_va)" +
        "VALUES(?,?)";
    private static final String SQL_QUERY_DELETE_ELU_IN_DEPOSE_PAR = "DELETE FROM  ods_va_depose_par where id_va=?";
    private static final String SQL_QUERY_ELU_LIST_BY_ID_VOEUAMENDEMENT = "SELECT elu.id_elu,elu.civilite," +
        "elu.nom_elu,elu.prenom_elu,grou.id_groupe,grou.nom_groupe,grou.nom_complet FROM ods_elu elu ," +
        "ods_groupe grou , ods_va_depose_par vadepo WHERE  vadepo.id_va=? and vadepo.id_elu=elu.id_elu " +
        "and elu.id_groupe=grou.id_groupe";
    private static final String SQL_QUERY_GROUPE_RAPPORTEURS_BY_ID_ENTREE = "SELECT elu.id_elu,elu.civilite," +
        " elu.nom_elu,elu.prenom_elu, com.id_commission,com.numero_commission," +
        " com.libelle_commission FROM  ods_entree_elus entreelu, ods_elu elu ,ods_commission com  WHERE " +
        " entreelu.id_entree=?  AND entreelu.id_elu=elu.id_elu AND elu.id_commission=com.id_commission " +
        " ORDER BY com.numero_commission, elu.nom_elu ";
    private static final String SQL_QUERY_GROUPE_COMMISSIONS_BY_ID_ENTREE = " SELECT com.id_commission," +
        "com.numero_commission,com.libelle_commission FROM ods_entree_commission entrecom,ods_commission com " +
        "WHERE entrecom.id_entree=? AND entrecom.id_commission=com.id_commission ORDER BY com.numero_commission ";
    private static final String SQL_IS_VA_ALREADY_EXIST_IN_FASCICULE = "SELECT COUNT(*) FROM ods_voeu_amendement" +
        " WHERE id_fascicule=? and  reference_va=? ";
    private static final String SQL_QUERY_PDD_LISTBY_ID_VOEUAMENDEMENT = "SELECT * FROM ods_pdd pdd ,ods_va_rattache_pdd vrp," +
        "ods_formation_conseil fc,ods_categorie_deliberation ca  WHERE vrp.id_va=? AND  vrp.id_pdd= pdd.id_pdd  and pdd.id_formation_conseil=fc.id_formation_conseil and pdd.id_categorie=ca.id_categorie ORDER BY vrp.numero_ordre";
    private static final String SQL_QUERY_DELETE_VA_IN_RATTACHE_PDD = "DELETE FROM  ods_va_rattache_pdd where id_va=?";
    private static final String SQL_QUERY_DELETE_PDD_IN_VA = "DELETE FROM  ods_va_rattache_pdd where id_pdd=? and id_va=?";
    private static final String SQL_QUERY_INSERT_PDD_IN_RATTACHE_PDD = "INSERT INTO ods_va_rattache_pdd(id_va,id_pdd,numero_ordre)" +
        "VALUES(?,?,?)";
    private static final String SQL_QUERY_IS_PDD_ALREADY_IN_VA = "SELECT COUNT(id_pdd) FROM ods_va_rattache_pdd where id_pdd=? and id_va= ?";
    private static final String SQL_QUERY_NEW_NUMERO_ORDRE_FOR_PDD = "SELECT MAX( numero_ordre ) FROM ods_va_rattache_pdd where id_pdd=?";
    private static final String SQL_QUERY_SELECT_ID_PDD_IN_RATTACHE_PDD = "SELECT id_pdd FROM ods_va_rattache_pdd where id_va= ? ";
    private static final String SQL_FILTER_COMMISSION = " AND va.id_commission = ? ";
    private static final String SQL_FILTER_FORMATION_CONSEIL = " AND va.id_formation_conseil = ? ";
    private static final String SQL_FILTER_ID_ODJ = " AND odj.id_odj = ? ";
    private static final String SQL_FILTER_PUBLIE = " AND va.en_ligne = ? ";
    private static final String SQL_FILTER_SEANCE = " AND se.id_seance= ? ";
    private static final String SQL_FILTER_TYPE = " AND va.type= ? ";
    private static final String SQL_FILTER_PARENT = " AND va.ods_id_va= ? ";
    private static final String SQL_FILTER_STATUT = " AND va.id_statut= ? ";
    private static final String SQL_FILTER_TYPE_ORDRE_DU_JOUR = " AND odj.id_type_odj = ? ";
    private static final String SQL_FILTER_TYPE_AMENDEMENT = " AND ( va.type='A' OR va.type='LR') ";
    private static final String SQL_FILTER_TYPE_TOUS_VOEUX = " AND ( va.type='V' OR va.type='VNR') ";
    private static final String SQL_FILTER_DATE_PUBLICATION = " AND va.date_publication > ? ";
    private static final String ORDER_BY_LIST = " ORDER BY ";
    private static final String GROUP_BY_ID_VA = " GROUP BY va.id_va ";
    private static final String SQL_QUERY_FIND_ID_BY_TEXTE_INITIAL = "SELECT id_va FROM ods_voeu_amendement WHERE id_texte_initial = ? ";
    private static final String SQL_QUERY_FIND_ID_BY_DELIBERATION = "SELECT id_va FROM ods_voeu_amendement WHERE id_delib = ? ";

    /**
     * Génère un nouvel identifiant
     * @param plugin plugin
     * @return Retourne l’identifiant généré
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
     * Insère un nouvel enregistrement dans la table ods_voeu_amendement à partir de l’objet voeu_amendement
     *  passé  en paramètre
     * @param   voeuAmendement le voeuAmendement à insérer dans la table ods_voeu_amendement
     * @param plugin plugin
     * @return l'id du VoeuAmendement créé
     */
    public int insert( VoeuAmendement voeuAmendement, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        int nIdVoeuAmendement = newPrimaryKey( plugin );
        voeuAmendement.setIdVoeuAmendement( nIdVoeuAmendement );
        daoUtil.setInt( 1, voeuAmendement.getIdVoeuAmendement(  ) );
        daoUtil.setInt( 2, voeuAmendement.getFormationConseil(  ).getIdFormationConseil(  ) );

        if ( null != voeuAmendement.getCommission(  ) )
        {
            daoUtil.setInt( 3, voeuAmendement.getCommission(  ).getIdCommission(  ) );
        }
        else
        {
            daoUtil.setIntNull( 3 );
        }

        if ( null != voeuAmendement.getParent(  ) )
        {
            daoUtil.setInt( 4, voeuAmendement.getParent(  ).getIdVoeuAmendement(  ) );
        }
        else
        {
            daoUtil.setIntNull( 4 );
        }

        //si la commission =-1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs commission 
        daoUtil.setInt( 5, voeuAmendement.getFichier(  ).getId(  ) );
        daoUtil.setInt( 6, voeuAmendement.getFascicule(  ).getIdFascicule(  ) );
        daoUtil.setString( 7, voeuAmendement.getType(  ) );
        daoUtil.setString( 8, voeuAmendement.getReference(  ) );
        daoUtil.setString( 9, voeuAmendement.getObjet(  ) );
        daoUtil.setBoolean( 10, voeuAmendement.getDeposeExecutif(  ) );
        daoUtil.setTimestamp( 11, voeuAmendement.getDateRetourControleLegalite(  ) );
        daoUtil.setBoolean( 12, voeuAmendement.getEnLigne(  ) );
        daoUtil.setTimestamp( 13, voeuAmendement.getDatePublication(  ) );
        daoUtil.setInt( 14, voeuAmendement.getVersion(  ) );

        if ( null != voeuAmendement.getStatut(  ) )
        {
            daoUtil.setInt( 15, voeuAmendement.getStatut(  ).getIdStatut(  ) );
        }
        else
        {
            daoUtil.setIntNull( 15 );
        }

        daoUtil.setString( 16, voeuAmendement.getNumeroDeliberation(  ) );

        if ( null != voeuAmendement.getDeliberation(  ) )
        {
            daoUtil.setInt( 17, voeuAmendement.getDeliberation(  ).getId(  ) );
        }
        else
        {
            daoUtil.setIntNull( 17 );
        }

        daoUtil.setTimestamp( 18, voeuAmendement.getDateVote(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        //insertion des elus rattachés au VA   dans  la table de jointure ods_va_depose_par
        if ( voeuAmendement.getElus(  ) != null )
        {
            for ( Elu elu : voeuAmendement.getElus(  ) )
            {
                insertEluInDeposePar( elu.getIdElu(  ), nIdVoeuAmendement, plugin );
            }
        }

        // insere les pdds rattachés au VA  qui sont dans la liste des pdds du va et qui ne sont pas   dans  la table de jointure ods_va_rattache_pdd si
        if ( voeuAmendement.getPdds(  ) != null )
        {
            int nNumeroOrdre;

            for ( PDD pdd : voeuAmendement.getPdds(  ) )
            {
                if ( !isPddAlreadyInVa( pdd.getIdPdd(  ), voeuAmendement.getIdVoeuAmendement(  ), plugin ) )
                {
                    nNumeroOrdre = newNumeroOrdre( pdd.getIdPdd(  ), plugin );
                    insertPddInRattachePdd( pdd.getIdPdd(  ), voeuAmendement.getIdVoeuAmendement(  ), nNumeroOrdre,
                        plugin );
                }
            }
        }

        return nIdVoeuAmendement;
    }

    /**
     * Supprime l' enregistrement dans la table ods_voeu_amendement correspondant à  l’objet VoeuAmendement passé en paramètre
     * @param voeuAmendement le voeuAmendement à supprimer dans la table ods_voeu_amendement
     * @param plugin plugin
     */
    public void delete( VoeuAmendement voeuAmendement, Plugin plugin )
    {
        // suppression dans la table ods_va_depose_par des élus rattachés au vA à supprimer
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ELU_IN_DEPOSE_PAR, plugin );
        daoUtil.setInt( 1, voeuAmendement.getIdVoeuAmendement(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        // suppression dans la table ods_va_rattache_pdd des pdds rattachés au vA à supprimer
        daoUtil = new DAOUtil( SQL_QUERY_DELETE_VA_IN_RATTACHE_PDD, plugin );
        daoUtil.setInt( 1, voeuAmendement.getIdVoeuAmendement(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        //suppression du VA dans la table ods_voeu_amendement
        daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, voeuAmendement.getIdVoeuAmendement(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * renvoie le VoeuAmendement correspondant à l'enregistrement dans la table ods_voeu_amendement ayant comme identifiant nKey
     * @param nKey l'identifiant de l'élu
     * @param plugin plugin
     * @return le voeuAmendement d'id=nkey
     */
    public VoeuAmendement load( int nKey, Plugin plugin )
    {
        VoeuAmendement voeuAmendement = null;
        FormationConseil formationConseil = null;
        Commission commission = null;
        Fascicule fascicule = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            voeuAmendement = new VoeuAmendement(  );
            voeuAmendement.setIdVoeuAmendement( ( daoUtil.getInt( 1 ) ) );
            voeuAmendement.setType( daoUtil.getString( 2 ) );
            voeuAmendement.setReference( daoUtil.getString( 3 ) );
            voeuAmendement.setObjet( daoUtil.getString( 4 ) );
            voeuAmendement.setDeposeExecutif( daoUtil.getBoolean( 5 ) );
            voeuAmendement.setDateRetourControleLegalite( daoUtil.getTimestamp( 6 ) );
            voeuAmendement.setEnLigne( daoUtil.getBoolean( 7 ) );
            voeuAmendement.setDatePublication( daoUtil.getTimestamp( 8 ) );
            voeuAmendement.setVersion( daoUtil.getInt( 9 ) );
            //creation de l'objet Formation  conseil associé au voeuAmendemant
            formationConseil = new FormationConseil(  );
            formationConseil.setIdFormationConseil( daoUtil.getInt( 10 ) );
            formationConseil.setLibelle( daoUtil.getString( 11 ) );
            voeuAmendement.setFormationConseil( formationConseil );
            //creation de l'objet fascicule associé au va
            fascicule = new Fascicule(  );
            fascicule.setIdFascicule( daoUtil.getInt( 12 ) );
            fascicule.setNomFascicule( daoUtil.getString( 13 ) );
            fascicule.setCodeFascicule( daoUtil.getString( 14 ) );
            voeuAmendement.setFascicule( fascicule );

            //récupération de l'objet commission  associé au va 
            if ( daoUtil.getObject( 15 ) != null )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 15 ) );
            }

            voeuAmendement.setCommission( commission );

            // creation de l'objet fichier associé au va
            Fichier fichier = new Fichier(  );
            fichier.setId( daoUtil.getInt( 16 ) );
            fichier.setTexte( daoUtil.getString( 17 ) );
            fichier.setVersion( daoUtil.getInt( 18 ) );
            fichier.setExtension( daoUtil.getString( 19 ) );
            fichier.setTaille( daoUtil.getInt( 20 ) );
            fichier.setNom( daoUtil.getString( 21 ) );
            fichier.setEnLigne( daoUtil.getBoolean( 22 ) );
            fichier.setDatePublication( daoUtil.getTimestamp( 23 ) );
            fichier.setTitre( daoUtil.getString( 24 ) );
            voeuAmendement.setFichier( fichier );

            // création du statut du VA
            if ( daoUtil.getObject( 25 ) != null )
            {
                Statut statut = new Statut(  );
                statut.setIdStatut( daoUtil.getInt( 25 ) );
                voeuAmendement.setStatut( statut );
            }

            voeuAmendement.setNumeroDeliberation( daoUtil.getString( 26 ) );

            // création du fichier de délibération du VA
            if ( daoUtil.getObject( 27 ) != null )
            {
                Fichier deliberation = new Fichier(  );
                deliberation.setId( daoUtil.getInt( 27 ) );
                voeuAmendement.setDeliberation( deliberation );
            }

            voeuAmendement.setDateVote( daoUtil.getTimestamp( 28 ) );

            if ( daoUtil.getObject( 29 ) != null )
            {
                VoeuAmendement va = load( daoUtil.getInt( 29 ), plugin );
                voeuAmendement.setParent( va );
            }

            //gestion de la liste des elus avec la table de jointure ods_va_depose_par
            voeuAmendement.setElus( selectEluListbyIdVoeuAmendement( nKey, plugin ) );
            //gestion de la liste des pdds rattachés
            voeuAmendement.setPdds( selectPddListbyIdVoeuAmendement( nKey, plugin ) );
        }

        daoUtil.free(  );

        return voeuAmendement;
    }

    /**
     * Modifie l'enregistrement dans la table ods_voeu_Amendement et les tables associées correspondant à  l’objet voeuAmendementpassé en paramètre
     * @param voeuAmendement le voeuAmendement à modifier dans la table ods_voeu_amendement
     * @param plugin plugin
     */
    public void store( VoeuAmendement voeuAmendement, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, voeuAmendement.getIdVoeuAmendement(  ) );
        daoUtil.setInt( 2, voeuAmendement.getFormationConseil(  ).getIdFormationConseil(  ) );

        if ( null != voeuAmendement.getCommission(  ) )
        {
            daoUtil.setInt( 3, voeuAmendement.getCommission(  ).getIdCommission(  ) );
        }
        else
        {
            daoUtil.setIntNull( 3 );
        }

        if ( null != voeuAmendement.getParent(  ) )
        {
            daoUtil.setInt( 4, voeuAmendement.getParent(  ).getIdVoeuAmendement(  ) );
        }
        else
        {
            daoUtil.setIntNull( 4 );
        }

        daoUtil.setInt( 5, voeuAmendement.getFascicule(  ).getIdFascicule(  ) );
        daoUtil.setString( 6, voeuAmendement.getType(  ) );
        daoUtil.setString( 7, voeuAmendement.getReference(  ) );
        daoUtil.setString( 8, voeuAmendement.getObjet(  ) );
        daoUtil.setBoolean( 9, voeuAmendement.getDeposeExecutif(  ) );
        daoUtil.setTimestamp( 10, voeuAmendement.getDateRetourControleLegalite(  ) );
        daoUtil.setBoolean( 11, voeuAmendement.getEnLigne(  ) );
        daoUtil.setTimestamp( 12, voeuAmendement.getDatePublication(  ) );
        daoUtil.setInt( 13, voeuAmendement.getVersion(  ) );

        if ( null != voeuAmendement.getStatut(  ) )
        {
            daoUtil.setInt( 14, voeuAmendement.getStatut(  ).getIdStatut(  ) );
        }
        else
        {
            daoUtil.setIntNull( 14 );
        }

        daoUtil.setString( 15, voeuAmendement.getNumeroDeliberation(  ) );

        if ( null != voeuAmendement.getDeliberation(  ) )
        {
            daoUtil.setInt( 16, voeuAmendement.getDeliberation(  ).getId(  ) );
        }
        else
        {
            daoUtil.setIntNull( 16 );
        }

        daoUtil.setTimestamp( 17, voeuAmendement.getDateVote(  ) );

        daoUtil.setInt( 18, voeuAmendement.getIdVoeuAmendement(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        // suppression des élus  dans la table de jointure ods_va_depose_par
        daoUtil = new DAOUtil( SQL_QUERY_DELETE_ELU_IN_DEPOSE_PAR, plugin );
        daoUtil.setInt( 1, voeuAmendement.getIdVoeuAmendement(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        // insertion des elus présent dans la liste du VA dans la table de jointure ods_va_depose_par
        if ( voeuAmendement.getElus(  ) != null )
        {
            for ( Elu elu : voeuAmendement.getElus(  ) )
            {
                insertEluInDeposePar( elu.getIdElu(  ), voeuAmendement.getIdVoeuAmendement(  ), plugin );
            }
        }

        // gestion de la liste des pdds
        // on supprime en base tous les pdds rattaché au va  présent en base  qui ne sont pas dans la liste des pdds du va à modifier	
        deletePddsNotInVa( voeuAmendement, plugin );

        // insere les pdds rattachés au VA  qui sont dans la liste des pdds du va et qui ne sont pas   dans  la table de jointure ods_va_rattache_pdd si
        if ( voeuAmendement.getPdds(  ) != null )
        {
            int nNumeroOrdre;

            for ( PDD pdd : voeuAmendement.getPdds(  ) )
            {
                if ( !isPddAlreadyInVa( pdd.getIdPdd(  ), voeuAmendement.getIdVoeuAmendement(  ), plugin ) )
                {
                    nNumeroOrdre = newNumeroOrdre( pdd.getIdPdd(  ), plugin );
                    insertPddInRattachePdd( pdd.getIdPdd(  ), voeuAmendement.getIdVoeuAmendement(  ), nNumeroOrdre,
                        plugin );
                }
            }
        }
    }

    /**
     * renvoie la liste des voeux et Amendements  présents dans la table ods_voeu_amendement répondant
     *  aux criteres de selections du filtre
     * @param filter le filtre de selection
     * @param plugin plugin
     * @return liste d'objets voeuAmendement
     */
    public List<VoeuAmendement> selectLiasseByFilter( VoeuAmendementFilter filter, Plugin plugin )
    {
        Elu elu = null;
        GroupePolitique groupe = null;
        Fascicule fascicule = null;
        Fichier fichier = null;
        List<VoeuAmendement> voeuAmendements = new ArrayList<VoeuAmendement>(  );
        VoeuAmendement voeuAmendement = null;
        String strSQL = SQL_QUERY_LIASSE;
        strSQL += ( ( filter.containsIdFormationConseilCriteria(  ) ) ? SQL_FILTER_FORMATION_CONSEIL
                                                                      : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdSeanceCriteria(  ) ) ? SQL_FILTER_SEANCE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsTypeOrdreDuJourCriteria(  ) ) ? SQL_FILTER_TYPE_ORDRE_DU_JOUR
                                                                   : OdsConstants.CONSTANTE_CHAINE_VIDE );
        //group by id_va
        strSQL += GROUP_BY_ID_VA;

        //order By
        if ( filter.containsOrderByCriteria(  ) )
        {
            strSQL += buildOrderBy( filter );
        }

        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );
        int nIndex = 1;

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

        if ( filter.containsTypeOrdreDuJourCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getTypeOrdreDuJour(  ) );
            nIndex++;
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            voeuAmendement = new VoeuAmendement(  );
            voeuAmendement.setIdVoeuAmendement( ( daoUtil.getInt( 1 ) ) );
            voeuAmendement.setType( daoUtil.getString( 2 ) );
            voeuAmendement.setReference( daoUtil.getString( 3 ) );
            voeuAmendement.setObjet( daoUtil.getString( 4 ) );
            voeuAmendement.setDeposeExecutif( daoUtil.getBoolean( 5 ) );

            //récupération de l'élu ayant déposé le VA  en vue d'afficher  son groupe politique
            if ( null != daoUtil.getObject( 6 ) )
            {
                elu = new Elu(  );
                elu.setIdElu( daoUtil.getInt( 6 ) );
                //récupération du groupe associé à l'élu
                groupe = new GroupePolitique(  );
                groupe.setIdGroupe( ( daoUtil.getInt( 7 ) ) );
                groupe.setNomGroupe( daoUtil.getString( ( 8 ) ) );
                elu.setGroupe( groupe );
                //initialisation de la liste des elus du va
                voeuAmendement.setElus( new ArrayList<Elu>(  ) );
                voeuAmendement.getElus(  ).add( elu );
            }

            //creation de l'objet fascicule associé au va
            fascicule = new Fascicule(  );
            fascicule.setIdFascicule( daoUtil.getInt( 9 ) );
            fascicule.setNomFascicule( daoUtil.getString( 10 ) );
            fascicule.setCodeFascicule( daoUtil.getString( 11 ) );
            voeuAmendement.setFascicule( fascicule );
            //gestion de la liste des pdds rattachés
            voeuAmendement.setPdds( selectPddListbyIdVoeuAmendement( voeuAmendement.getIdVoeuAmendement(  ), plugin ) );
            //fichier
            fichier = new Fichier(  );
            fichier.setId( daoUtil.getInt( 12 ) );
            voeuAmendement.setFichier( fichier );
            voeuAmendement.setEnLigne( daoUtil.getBoolean( 13 ) );
            voeuAmendement.setDatePublication( daoUtil.getTimestamp( 14 ) );
            voeuAmendement.setVersion( daoUtil.getInt( 15 ) );

            // création du statut du VA
            if ( daoUtil.getObject( 16 ) != null )
            {
                Statut statut = new Statut(  );
                statut.setIdStatut( daoUtil.getInt( 16 ) );
                voeuAmendement.setStatut( statut );
            }

            voeuAmendement.setNumeroDeliberation( daoUtil.getString( 17 ) );

            // création du fichier de délibération du VA
            if ( daoUtil.getObject( 18 ) != null )
            {
                Fichier deliberation = new Fichier(  );
                deliberation.setId( daoUtil.getInt( 18 ) );
                voeuAmendement.setDeliberation( deliberation );
            }

            voeuAmendement.setDateVote( daoUtil.getTimestamp( 19 ) );
            voeuAmendement.setDateRetourControleLegalite( daoUtil.getTimestamp( 20 ) );

            if ( daoUtil.getObject( 21 ) != null )
            {
                VoeuAmendement va = load( daoUtil.getInt( 21 ), plugin );
                voeuAmendement.setParent( va );
            }

            voeuAmendement.setGroupeCommissions( selectGroupeCommissionsbyIdEntree( daoUtil.getInt( 22 ), plugin ) );
            voeuAmendement.setGroupeRapporteurs( selectGroupeRapporteursbyIdEntree( daoUtil.getInt( 22 ), plugin ) );
            voeuAmendements.add( voeuAmendement );
        }

        daoUtil.free(  );

        return voeuAmendements;
    }

    /**
     * renvoie la liste des voeux et Amendements  présents dans la table ods_voeu_amendement répondant
     *  aux criteres de selections du filtre
     * @param filter le filtre de selection
     * @param plugin plugin
     * @return liste d'objets voeuAmendement
     */
    public List<VoeuAmendement> selectVoeuAmendementListByFilter( VoeuAmendementFilter filter, Plugin plugin )
    {
        List<VoeuAmendement> voeuAmendements = new ArrayList<VoeuAmendement>(  );
        VoeuAmendement voeuAmendement = null;
        FormationConseil formationConseil = null;
        Fascicule fascicule = null;
        Commission commission = null;

        String strSQL;

        if ( filter.containsTypeOrdreDuJourCriteria(  ) || filter.containsIdOrdreDuJourCriteria(  ) )
        {
            strSQL = SQL_QUERY_VOEUAMENDEMENT_LIST_AVEC_ODJ;
        }
        else
        {
            strSQL = SQL_QUERY_VOEUAMENDEMENT_LIST;
        }

        strSQL += ( ( filter.containsIdFormationConseilCriteria(  ) ) ? SQL_FILTER_FORMATION_CONSEIL
                                                                      : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdPublieCriteria(  ) ) ? SQL_FILTER_PUBLIE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdSeanceCriteria(  ) ) ? SQL_FILTER_SEANCE : OdsConstants.CONSTANTE_CHAINE_VIDE );

        if ( filter.containsTypeCriteria(  ) )
        {
            // TYPE_A ==> amendements et lettres rectificatives
            if ( filter.getType(  ) == CONSTANTE_TYPE_A )
            {
                strSQL += SQL_FILTER_TYPE_AMENDEMENT;
            }
            else if ( filter.getType(  ) == CONSTANTE_TYPE_ALLV )
            {
                strSQL += SQL_FILTER_TYPE_TOUS_VOEUX;
            }
            else
            {
                strSQL += SQL_FILTER_TYPE;
            }
        }

        strSQL += ( ( filter.containsCommissionCriteria(  ) ) ? SQL_FILTER_COMMISSION : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdParentCriteria(  ) ) ? SQL_FILTER_PARENT : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdStatutCriteria(  ) ) ? SQL_FILTER_STATUT : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsTypeOrdreDuJourCriteria(  ) ) ? SQL_FILTER_TYPE_ORDRE_DU_JOUR
                                                                   : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdOrdreDuJourCriteria(  ) ) ? SQL_FILTER_ID_ODJ : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsDatePublicationCriteria(  ) ) ? SQL_FILTER_DATE_PUBLICATION
                                                                   : OdsConstants.CONSTANTE_CHAINE_VIDE );

        strSQL += GROUP_BY_ID_VA;

        //order By
        if ( filter.containsOrderByCriteria(  ) )
        {
            strSQL += buildOrderBy( filter );
        }

        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );
        int nIndex = 1;

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

        if ( filter.containsTypeCriteria(  ) && !filter.getType(  ).equals( CONSTANTE_TYPE_A ) &&
                !filter.getType(  ).equals( CONSTANTE_TYPE_ALLV ) )
        {
            daoUtil.setString( nIndex, filter.getType(  ) );
            nIndex++;
        }

        if ( filter.containsCommissionCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdCommission(  ) );
            nIndex++;
        }

        if ( filter.containsIdParentCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdParent(  ) );
            nIndex++;
        }

        if ( filter.containsIdStatutCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdStatut(  ) );
            nIndex++;
        }

        if ( filter.containsTypeOrdreDuJourCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getTypeOrdreDuJour(  ) );
            nIndex++;
        }

        if ( filter.containsIdOrdreDuJourCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdOrdreDuJour(  ) );
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
            voeuAmendement = new VoeuAmendement(  );
            voeuAmendement.setIdVoeuAmendement( ( daoUtil.getInt( 1 ) ) );
            voeuAmendement.setType( daoUtil.getString( 2 ) );
            voeuAmendement.setReference( daoUtil.getString( 3 ) );
            voeuAmendement.setObjet( daoUtil.getString( 4 ) );
            voeuAmendement.setDeposeExecutif( daoUtil.getBoolean( 5 ) );
            voeuAmendement.setDateRetourControleLegalite( daoUtil.getTimestamp( 6 ) );
            voeuAmendement.setEnLigne( daoUtil.getBoolean( 7 ) );
            voeuAmendement.setDatePublication( daoUtil.getTimestamp( 8 ) );
            voeuAmendement.setVersion( daoUtil.getInt( 9 ) );

            //creation de l'objet Formation  conseil associé au voeuAmendemant
            formationConseil = new FormationConseil(  );
            formationConseil.setIdFormationConseil( daoUtil.getInt( 10 ) );
            formationConseil.setLibelle( daoUtil.getString( 11 ) );
            voeuAmendement.setFormationConseil( formationConseil );

            //creation de l'objet fascicule asocié au fascicule
            fascicule = new Fascicule(  );
            fascicule.setIdFascicule( daoUtil.getInt( 12 ) );
            fascicule.setNomFascicule( daoUtil.getString( 13 ) );
            fascicule.setCodeFascicule( daoUtil.getString( 14 ) );

            voeuAmendement.setFascicule( fascicule );

            //creation de l'objet fichier associé au va
            Fichier fichier = new Fichier(  );
            fichier.setId( daoUtil.getInt( 15 ) );
            voeuAmendement.setFichier( fichier );

            //recupération de l'objet commission  associé a u va 
            if ( daoUtil.getObject( 16 ) != null )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 16 ) );
                voeuAmendement.setCommission( commission );
            }

            // création du statut du VA
            if ( daoUtil.getObject( 17 ) != null )
            {
                Statut statut = new Statut(  );
                statut.setIdStatut( daoUtil.getInt( 17 ) );
                voeuAmendement.setStatut( statut );
            }

            voeuAmendement.setNumeroDeliberation( daoUtil.getString( 18 ) );

            // création du fichier de délibération du VA
            if ( daoUtil.getObject( 19 ) != null )
            {
                Fichier deliberation = new Fichier(  );
                deliberation.setId( daoUtil.getInt( 19 ) );
                voeuAmendement.setDeliberation( deliberation );
            }

            voeuAmendement.setDateVote( daoUtil.getTimestamp( 20 ) );

            if ( daoUtil.getObject( 21 ) != null )
            {
                VoeuAmendement va = load( daoUtil.getInt( 21 ), plugin );
                voeuAmendement.setParent( va );
            }

            //gestion de la liste des pdds rattachés
            voeuAmendement.setPdds( selectPddListbyIdVoeuAmendement( voeuAmendement.getIdVoeuAmendement(  ), plugin ) );

            if ( filter.containsIdEluCriteria(  ) )
            {
                List<Elu> elus = selectEluListbyIdVoeuAmendement( voeuAmendement.getIdVoeuAmendement(  ), plugin );

                for ( Elu elu : elus )
                {
                    //si l'un des elus dépositaire est sélectionné on ajoute le VA
                    if ( elu.getIdElu(  ) == filter.getIdElu(  ) )
                    {
                        voeuAmendements.add( voeuAmendement );

                        break;
                    }
                }
            }
            else
            {
                // si pas de critère sur l'élu on ajoute le VA
                voeuAmendements.add( voeuAmendement );
            }
        }

        daoUtil.free(  );

        return voeuAmendements;
    }

    /**
     * retourne la liste des élus ayant déposés le  VoeuAmendement d'id nKey
     * @param nKey id du VA
     * @param plugin plugin
     * @return liste des elus du va
     */
    private List<Elu> selectEluListbyIdVoeuAmendement( int nKey, Plugin plugin )
    {
        List<Elu> elus = new ArrayList<Elu>(  );
        GroupePolitique groupePolitique = null;
        Elu elu = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ELU_LIST_BY_ID_VOEUAMENDEMENT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            elu = new Elu(  );
            elu.setIdElu( daoUtil.getInt( 1 ) );
            elu.setCivilite( daoUtil.getString( 2 ) );
            elu.setNomElu( daoUtil.getString( 3 ) );
            elu.setPrenomElu( daoUtil.getString( 4 ) );
            //si l'elu a une commission on la recupere en creant un objet commission et on l'affecte à l'objet elu
            //on recupere le groupe politique de l'élu en créant un objet GroupePolitique et on l'affecte à l'objet elu
            groupePolitique = new GroupePolitique(  );
            groupePolitique.setIdGroupe( daoUtil.getInt( 5 ) );
            groupePolitique.setNomGroupe( daoUtil.getString( 6 ) );
            groupePolitique.setNomComplet( daoUtil.getString( 7 ) );
            elu.setGroupe( groupePolitique );
            //on ajoute l'objet elu a la liste des elus
            elus.add( elu );
        }

        daoUtil.free(  );

        return elus;
    }

    /**
     * insere dans la table ods_va_depose_par la liste des elus rattachés au VouAmendement
     * @param nIdElu l'id de l'elu a rattache au VA
     * @param nIdVoeuAmendement l'id du VoeuAmendement
     * @param plugin plugin
     */
    private void insertEluInDeposePar( int nIdElu, int nIdVoeuAmendement, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_ELU_IN_DEPOSE_PAR, plugin );
        daoUtil.setInt( 1, nIdElu );
        daoUtil.setInt( 2, nIdVoeuAmendement );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne la liste des pdds associé au VoeuAmendement d'id nKey
     * @param nKey id du VA
     * @param plugin plugin
     * @return la liste des pdds du va
     */
    public List<PDD> selectPddListbyIdVoeuAmendement( int nKey, Plugin plugin )
    {
        FormationConseil formationConseil = null;
        CategorieDeliberation categorieDeliberation = null;
        PDD pdd;
        List<PDD> pdds = new ArrayList<PDD>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_PDD_LISTBY_ID_VOEUAMENDEMENT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            pdd = new PDD(  );
            pdd.setIdPdd( daoUtil.getInt( "pdd.id_pdd" ) );
            pdd.setReference( daoUtil.getString( "pdd.reference" ) );
            pdd.setTypePdd( daoUtil.getString( "pdd.type_pdd" ) );
            pdd.setDelegationsServices( daoUtil.getBoolean( "pdd.delegations_services" ) );
            pdd.setModeIntroduction( daoUtil.getString( "pdd.mode_introduction" ) );
            pdd.setObjet( daoUtil.getString( "pdd.objet" ) );
            pdd.setPiecesManuelles( daoUtil.getBoolean( "pdd.pieces_manuelles" ) );
            pdd.setDateVote( daoUtil.getTimestamp( "pdd.date_vote" ) );
            pdd.setDateRetourCtrlLegalite( daoUtil.getTimestamp( "pdd.date_retour_ctrl_legalite" ) );
            pdd.setEnLigne( daoUtil.getBoolean( "pdd.en_ligne" ) );
            pdd.setDatePublication( daoUtil.getTimestamp( "pdd.date_publication" ) );
            pdd.setVersion( daoUtil.getInt( "pdd.version" ) );
            //creation de l'objet Formation  conseil associé au voeuAmendemant
            formationConseil = new FormationConseil(  );
            formationConseil.setIdFormationConseil( daoUtil.getInt( "fc.id_formation_conseil" ) );
            formationConseil.setLibelle( daoUtil.getString( "fc.libelle_formation_conseil" ) );
            pdd.setFormationConseil( formationConseil );
            //creation de l'objet categorieDeliberation
            categorieDeliberation = new CategorieDeliberation(  );
            categorieDeliberation.setIdCategorie( daoUtil.getInt( "ca.id_categorie" ) );
            categorieDeliberation.setLibelle( daoUtil.getString( "ca.libelle_categorie" ) );
            pdd.setCategorieDeliberation( categorieDeliberation );
            pdds.add( pdd );
        }

        daoUtil.free(  );

        return pdds;
    }

    /**
     * insere dans la table ods_va_rattache_pdd la liste des pdds rattachés au VouAmendement
     * @param nIdPdd l'id du pdd a rattache au VA
     * @param nIdVoeuAmendement l'id du VoeuAmendement
     * @param nNumeroOrdre numéro ordre
     * @param plugin plugin
     */
    private void insertPddInRattachePdd( int nIdPdd, int nIdVoeuAmendement, int nNumeroOrdre, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_PDD_IN_RATTACHE_PDD, plugin );
        daoUtil.setInt( 1, nIdVoeuAmendement );
        daoUtil.setInt( 2, nIdPdd );
        daoUtil.setInt( 3, nNumeroOrdre );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * test si la reference = strReference du VA existe deja dans le Fascicule d'id =nIdFascicule
     * @param nIdFascicule id du fascicule
     * @param strReference reference du VA
     * @param plugin plugin
     * @return retourne true si la reference existe deja pour ce fascicule false sinon
     */
    public boolean isAlreadyExistInFascicule( int nIdFascicule, String strReference, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_IS_VA_ALREADY_EXIST_IN_FASCICULE, plugin );
        daoUtil.setInt( 1, nIdFascicule );
        daoUtil.setString( 2, strReference );
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
     * retourne la clause order by de la requette en fonction du filter
     * @param filter le filtre de selection
     * @return  retourne l'order by de la requette
     */
    private String buildOrderBy( VoeuAmendementFilter filter )
    {
        StringBuffer strOrderBy = new StringBuffer(  );
        strOrderBy.append( ORDER_BY_LIST );

        for ( String order : filter.getOrderByList(  ) )
        {
            strOrderBy.append( order );
            strOrderBy.append( OdsConstants.VIRGULE );
        }

        return strOrderBy.substring( 0, strOrderBy.length(  ) - 1 );
    }

    /**
     * test si le pdd existe deja dans la liste des pdds du VoeuAmendement
     * @param nIdPdd  id du pdd
     * @param nIdVa id du VA
     * @param plugin plugin
     * @return retourne true si le pdd existe deja dans la liste des pdds du VA false sinon
     */
    private boolean isPddAlreadyInVa( int nIdPdd, int nIdVa, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_IS_PDD_ALREADY_IN_VA, plugin );
        daoUtil.setInt( 1, nIdPdd );
        daoUtil.setInt( 2, nIdVa );
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
     * retourne le numero d' ordre a inserer avec le pdd d'id nIdPdd
     * @param nIdPdd id du pdd à inserer
     * @param plugin plugin
     * @return  le numero d' ordre a inserer avec le pdd d'id nIdPdd
     */
    private int newNumeroOrdre( int nIdPdd, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_NUMERO_ORDRE_FOR_PDD, plugin );
        daoUtil.setInt( 1, nIdPdd );
        daoUtil.executeQuery(  );

        int nNumeroOrdre = 1;

        if ( daoUtil.next(  ) && ( daoUtil.getObject( 1 ) != null ) )
        {
            nNumeroOrdre = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free(  );

        return nNumeroOrdre;
    }

    /**
     * supprime dans la table va_rattache_pdd tous les pdds qui ne sont pas dans la liste des ppds du va à modifier
     * @param va le voeuAmendement à modifier
     * @param plugin plugin
     */
    private void deletePddsNotInVa( VoeuAmendement va, Plugin plugin )
    {
        int nIdPdd;
        boolean bIsInPddList;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ID_PDD_IN_RATTACHE_PDD, plugin );
        daoUtil.setInt( 1, va.getIdVoeuAmendement(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            bIsInPddList = false;
            nIdPdd = daoUtil.getInt( 1 );

            //boucle sur la liste des pdds du va
            if ( va.getPdds(  ) != null )
            {
                for ( PDD pdd : va.getPdds(  ) )
                {
                    if ( pdd.getIdPdd(  ) == nIdPdd )
                    {
                        bIsInPddList = true;
                    }
                }
            }

            if ( !bIsInPddList )
            {
                deletePdd( nIdPdd, va.getIdVoeuAmendement(  ), plugin );
            }
        }

        daoUtil.free(  );
    }

    /**
     * suppression dans la table ods_va_rattache_pdd du  couple pdd d'id nIdPdd et du va d'id nIdVa
     *@param nIdPdd l'id du pdd
     *@param nIdVa l'id du va
     *@param plugin plugin
     *
     */
    private void deletePdd( int nIdPdd, int nIdVa, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_PDD_IN_VA, plugin );
        daoUtil.setInt( 1, nIdPdd );
        daoUtil.setInt( 2, nIdVa );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne le voeu amendement dont le texte initial est le fichier
     * @param fichier le fichier du voeu amendement
     * @param plugin le plugin
     * @return la voeu amendement dont le texte initial est le fichier
     */
    public VoeuAmendement selectByTexteInitial( Fichier fichier, Plugin plugin )
    {
        int nIdVoeuAmendement = -1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ID_BY_TEXTE_INITIAL, plugin );
        daoUtil.setInt( 1, fichier.getId(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nIdVoeuAmendement = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return load( nIdVoeuAmendement, plugin );
    }

    /**
     * Retourne le voeu amendement dont la deliberation est le fichier
     * @param fichier le fichier du voeu amendement
     * @param plugin le plugin
     * @return la voeu amendement dont le texte initial est le fichier
     */
    public VoeuAmendement selectByDeliberation( Fichier fichier, Plugin plugin )
    {
        int nIdVoeuAmendement = -1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ID_BY_DELIBERATION, plugin );
        daoUtil.setInt( 1, fichier.getId(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nIdVoeuAmendement = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return load( nIdVoeuAmendement, plugin );
    }

    /**
     * retourne la liste des rapporteurs de l'entrée d'ordre du jour d'id nKey
     * @param nKey id de l'entrée de l'ordre du jour
     * @param plugin plugin
     * @return liste des rapporteurs de l'entrée de l'ordre du jour
     */
    private List<Elu> selectGroupeRapporteursbyIdEntree( int nKey, Plugin plugin )
    {
        List<Elu> elus = new ArrayList<Elu>(  );
        Commission commission = null;
        Elu elu = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GROUPE_RAPPORTEURS_BY_ID_ENTREE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            elu = new Elu(  );
            elu.setIdElu( daoUtil.getInt( 1 ) );
            elu.setCivilite( daoUtil.getString( 2 ) );
            elu.setNomElu( daoUtil.getString( 3 ) );
            elu.setPrenomElu( daoUtil.getString( 4 ) );

            //si l'elu a une commission on la recupere en creant un objet commission et on l'affecte à l'objet elu
            if ( null != daoUtil.getObject( 5 ) )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 5 ) );
                commission.setNumero( daoUtil.getInt( 6 ) );
                commission.setLibelle( daoUtil.getString( 7 ) );
                elu.setCommission( commission );
            }

            //on ajoute l'objet elu a la liste des elus
            elus.add( elu );
        }

        daoUtil.free(  );

        return elus;
    }

    /**
     * Renvoie la liste des commissions de l'entrée d'ordre du jour d'id nkey
     * @param nKey id de l'entrée
     * @param plugin le Plugin
     * @return  la liste des commissions de l'entrée de l'ordre du jour d'id nkey
     */
    private List<Commission> selectGroupeCommissionsbyIdEntree( int nKey, Plugin plugin )
    {
        List<Commission> commissions = new ArrayList<Commission>(  );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GROUPE_COMMISSIONS_BY_ID_ENTREE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Commission commission = new Commission(  );
            commission.setIdCommission( daoUtil.getInt( 1 ) );
            commission.setNumero( daoUtil.getInt( 2 ) );
            commission.setLibelle( daoUtil.getString( 3 ) );
            commissions.add( commission );
        }

        daoUtil.free(  );

        return commissions;
    }
}
