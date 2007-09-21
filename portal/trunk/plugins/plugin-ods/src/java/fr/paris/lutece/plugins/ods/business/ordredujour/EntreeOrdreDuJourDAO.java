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
import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.fascicule.Fascicule;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.naturedossier.NatureDesDossiers;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *EntreeOrdreDuJourDAO
 *
 */
public class EntreeOrdreDuJourDAO implements fr.paris.lutece.plugins.ods.business.ordredujour.IEntreeOrdreDuJourDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_entree ) FROM ods_entree_ordre_jour  ";
    private static final String SQL_QUERY_NEW_NUMERO_ORDRE = "SELECT max( numero_ordre ) FROM ods_entree_ordre_jour  ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_entree_ordre_jour ( " +
        "id_entree,id_va,id_odj,id_nature,id_pdd,type_entree,numero_ordre,texte_libre," +
        "reference_affichee,objet,style ) VALUES (?,?,?,?,?,?,?,?,?,?,?) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ods_entree_ordre_jour SET " +
        "id_entree=?,id_va=?,id_odj=?,id_nature=?,id_pdd=?,type_entree=?,numero_ordre=?,texte_libre=?," +
        "reference_affichee=?,objet=?,style=? WHERE id_entree=? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ods_entree_ordre_jour WHERE id_entree=?";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT en.id_entree," +
        "va.id_va,va.type,va.reference_va,va.objet,va.en_ligne,va.id_texte_initial,fa.id_fascicule,fa.nom_fascicule," +
        "fa.code_fascicule,en.id_odj,na.id_nature," +
        "na.libelle_nature,na.num_nature,pdd.id_pdd,pdd.reference,pdd.type_pdd," +
        "pdd.en_ligne,pdd.date_publication,pdd.objet,pdd.delegations_services,en.type_entree,en.numero_ordre,en.texte_libre," +
        "en.reference_affichee,en.objet,en.style  FROM ods_entree_ordre_jour en  " +
        "LEFT JOIN  ods_voeu_amendement va on(en.id_va=va.id_va)" +
        "LEFT JOIN ods_fascicule fa on( va.id_fascicule=fa.id_fascicule) " +
        "LEFT JOIN ods_pdd pdd on(en.id_pdd=pdd.id_pdd) " +
        "LEFT JOIN ods_nature_dossier na on( en.id_nature=na.id_nature) " + "WHERE id_entree=? ";
    private static final String SQL_QUERY_SELECT_ENTREES_BY_FILTER = "SELECT en.id_entree, " +
        " va.id_va, va.type, va.reference_va, va.objet,va.en_ligne, va.id_texte_initial, fa.id_fascicule, fa.nom_fascicule," +
        " fa.code_fascicule, en.id_odj, na.id_nature, " +
        " na.libelle_nature, na.num_nature, pdd.id_pdd, pdd.reference, pdd.type_pdd, " +
        " pdd.en_ligne, pdd.date_publication, pdd.objet,pdd.delegations_services, en.type_entree, en.numero_ordre, en.texte_libre, " +
        " en.reference_affichee, en.objet, en.style " + " FROM ods_entree_ordre_jour en " +
        " LEFT JOIN ods_voeu_amendement va ON(en.id_va = va.id_va ) " +
        " LEFT JOIN ods_fascicule fa ON( va.id_fascicule = fa.id_fascicule ) " +
        " LEFT JOIN ods_pdd pdd ON(en.id_pdd = pdd.id_pdd ) " +
        " LEFT JOIN ods_nature_dossier na ON( en.id_nature = na.id_nature ) " +
        " LEFT JOIN ods_odj odj ON( en.id_odj = odj.id_odj ) " + " WHERE 1 ";
    private static final String SQL_QUERY_ELU_LIST_BY_ID_ENTREE = "SELECT elu.id_elu,elu.civilite," +
        " elu.nom_elu,elu.prenom_elu, com.id_commission,com.numero_commission," +
        " com.libelle_commission FROM  ods_entree_elus entreelu, ods_elu elu ,ods_commission com  WHERE " +
        " entreelu.id_entree=?  AND entreelu.id_elu=elu.id_elu AND elu.id_commission=com.id_commission " +
        " ORDER BY com.numero_commission, elu.nom_elu ";
    private static final String SQL_QUERY_INSERT_RAPPORTEUR = "INSERT INTO ods_entree_elus(id_entree,id_elu) VALUES(?,?) ";
    private static final String SQL_QUERY_DELETE_RAPPORTEUR = "DELETE FROM ods_entree_elus WHERE id_entree=? ";
    private static final String SQL_QUERY_INSERT_COMMISSION = "INSERT INTO ods_entree_commission(id_entree,id_commission) VALUES(?,?) ";
    private static final String SQL_QUERY_DELETE_COMMISSION = "DELETE FROM ods_entree_commission WHERE id_entree=? ";
    private static final String SQL_QUERY_COMMISSION_LIST_BY_ID_ENTREE = " SELECT com.id_commission," +
        "com.numero_commission,com.libelle_commission FROM ods_entree_commission entrecom,ods_commission com " +
        "WHERE entrecom.id_entree=? AND entrecom.id_commission=com.id_commission ORDER BY com.numero_commission ";
    private static final String SQL_FILTER_ODJ = " AND odj.id_odj = ? ";
    private static final String SQL_FILTER_PDD = " AND en.id_pdd = ? ";
    private static final String SQL_FILTER_PUBLICATION = " AND odj.publie = ? ";
    private static final String SQL_FILTER_SEANCE = " AND odj.id_seance = ? ";
    private static final String SQL_ORDER_BY_NUMERO_ORDRE = " ORDER BY en.numero_ordre ";

    /**
     * Génère un nouvel identifiant
     * @param plugin le Plugin
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
     * Génère un nouveau numero d'ordre
     * @param plugin le Plugin
     * @return Retourne le numero ordre généré
     */
    private int newNumeoOrdre( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_NUMERO_ORDRE, plugin );
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
     * supprime l'entrée d'ordre du jour passée en parametre
     * @param entree EntreeOrdreDuJour
     * @param plugin Plugin
     */
    public void delete( EntreeOrdreDuJour entree, Plugin plugin )
    {
        DAOUtil daoUtil;

        //	suppression des commissions liées à l'entrée 
        if ( entree.getCommissions(  ) != null )
        {
            deleteCommission( entree.getIdEntreeOrdreDuJour(  ), plugin );
        }

        //suppression des rapporteurs  liés à l'entrée 
        if ( entree.getElus(  ) != null )
        {
            deleteRapporteur( entree.getIdEntreeOrdreDuJour(  ), plugin );
        }

        daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, entree.getIdEntreeOrdreDuJour(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * insere l'entrée d'ordre du jour passé en parametre dans la table ods_entree_ordre_jour
     * @param entree EntreeOrdreDuJour
     * @param plugin Plugin
     * @return l'id de la nouvelle entrée
     */
    public int insert( EntreeOrdreDuJour entree, Plugin plugin )
    {
        int nNewId = newPrimaryKey( plugin );
        int nNewNumeroOrdre = newNumeoOrdre( plugin );
        entree.setIdEntreeOrdreDuJour( nNewId );

        DAOUtil daoUtil;
        daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( 1, entree.getIdEntreeOrdreDuJour(  ) );

        if ( null != entree.getVoeuAmendement(  ) )
        {
            daoUtil.setInt( 2, entree.getVoeuAmendement(  ).getIdVoeuAmendement(  ) );
        }
        else
        {
            daoUtil.setIntNull( 2 );
        }

        daoUtil.setInt( 3, entree.getOrdreDuJour(  ).getIdOrdreDuJour(  ) );

        if ( ( null != entree.getNature(  ) ) && ( entree.getNature(  ).getIdNature(  ) != -1 ) )
        {
            daoUtil.setInt( 4, entree.getNature(  ).getIdNature(  ) );
        }
        else
        {
            daoUtil.setIntNull( 4 );
        }

        if ( null != entree.getPdd(  ) )
        {
            daoUtil.setInt( 5, entree.getPdd(  ).getIdPdd(  ) );
        }
        else
        {
            daoUtil.setIntNull( 5 );
        }

        daoUtil.setString( 6, entree.getType(  ) );
        daoUtil.setInt( 7, nNewNumeroOrdre );
        daoUtil.setString( 8, entree.getTexteLibre(  ) );
        daoUtil.setString( 9, entree.getReference(  ) );
        daoUtil.setString( 10, entree.getObjet(  ) );
        daoUtil.setString( 11, entree.getStyle(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        return entree.getIdEntreeOrdreDuJour(  );
    }

    /**
     * renvoie l'entrée d'ordre du jour d'id nKey
     * @param nKey int
     * @param plugin Plugin
     * @return EntreeOrdreDuJour entree
     */
    public EntreeOrdreDuJour load( int nKey, Plugin plugin )
    {
        EntreeOrdreDuJour entreeOrdreDuJour = null;
        VoeuAmendement voeuAmendement = null;
        Fascicule fascicule = null;
        Fichier fichier = null;
        PDD pdd = null;
        OrdreDuJour ordreDuJour = null;
        NatureDesDossiers natureDesDossiers = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            entreeOrdreDuJour = new EntreeOrdreDuJour(  );
            entreeOrdreDuJour.setIdEntreeOrdreDuJour( daoUtil.getInt( 1 ) );

            if ( null != daoUtil.getObject( 2 ) )
            {
                voeuAmendement = new VoeuAmendement(  );
                voeuAmendement.setIdVoeuAmendement( daoUtil.getInt( 2 ) );
                voeuAmendement.setType( daoUtil.getString( 3 ) );
                voeuAmendement.setReference( daoUtil.getString( 4 ) );
                voeuAmendement.setObjet( daoUtil.getString( 5 ) );
                voeuAmendement.setEnLigne( daoUtil.getBoolean( 6 ) );
                //creation de l'objet fichier
                fichier = new Fichier(  );
                fichier.setId( daoUtil.getInt( 7 ) );
                voeuAmendement.setFichier( fichier );
                //creation de l'objet fascicule associé au va
                fascicule = new Fascicule(  );
                fascicule.setIdFascicule( daoUtil.getInt( 8 ) );
                fascicule.setNomFascicule( daoUtil.getString( 9 ) );
                fascicule.setCodeFascicule( daoUtil.getString( 10 ) );
                voeuAmendement.setFascicule( fascicule );
                entreeOrdreDuJour.setVoeuAmendement( voeuAmendement );
            }

            ordreDuJour = new OrdreDuJour(  );
            ordreDuJour.setIdOrdreDuJour( daoUtil.getInt( 11 ) );
            entreeOrdreDuJour.setOrdreDuJour( ordreDuJour );

            if ( null != daoUtil.getObject( 12 ) )
            {
                natureDesDossiers = new NatureDesDossiers(  );
                natureDesDossiers.setIdNature( daoUtil.getInt( 12 ) );
                natureDesDossiers.setLibelleNature( daoUtil.getString( 13 ) );
                natureDesDossiers.setNumeroNature( daoUtil.getInt( 14 ) );
                entreeOrdreDuJour.setNature( natureDesDossiers );
            }

            if ( null != daoUtil.getObject( 15 ) )
            {
                pdd = new PDD(  );
                pdd.setIdPdd( daoUtil.getInt( 15 ) );
                pdd.setReference( daoUtil.getString( 16 ) );
                pdd.setTypePdd( daoUtil.getString( 17 ) );
                pdd.setEnLigne( daoUtil.getBoolean( 18 ) );
                pdd.setDatePublication( daoUtil.getTimestamp( 19 ) );
                pdd.setObjet( daoUtil.getString( 20 ) );
                pdd.setDelegationsServices( daoUtil.getBoolean( 21 ) );
                entreeOrdreDuJour.setPdd( pdd );
            }

            //récupération de la liste des rapporteurs associés
            entreeOrdreDuJour.setElus( selectRapporteursbyIdEntree( entreeOrdreDuJour.getIdEntreeOrdreDuJour(  ), plugin ) );
            // récupération de la liste des commissions associés
            entreeOrdreDuJour.setCommissions( selectCommissionListByIdEntree( 
                    entreeOrdreDuJour.getIdEntreeOrdreDuJour(  ), plugin ) );

            entreeOrdreDuJour.setType( daoUtil.getString( 22 ) );
            entreeOrdreDuJour.setNumeroOrdre( daoUtil.getInt( 23 ) );
            entreeOrdreDuJour.setTexte( daoUtil.getString( 24 ) );
            entreeOrdreDuJour.setReference( daoUtil.getString( 25 ) );
            entreeOrdreDuJour.setObjet( daoUtil.getString( 26 ) );
            entreeOrdreDuJour.setStyle( daoUtil.getString( 27 ) );
        }

        daoUtil.free(  );

        return entreeOrdreDuJour;
    }

    /**
     * Retourne la liste d'entrées de l'ordre du jour pour l'ordre du jour identifié par nKey
     * @param nKey int l'id de l'ordre du jour
     * @param plugin le plugin
     * @return la liste d'entrées de l'ordre du jour d'id nKey
     */
    public List<EntreeOrdreDuJour> selectEntreesByIdOrdreDuJour( int nKey, Plugin plugin )
    {
        List<EntreeOrdreDuJour> listentreesOrdreDuJour = new ArrayList<EntreeOrdreDuJour>(  );
        EntreeOrdreDuJour entreeOrdreDuJour = null;
        VoeuAmendement voeuAmendement = null;
        Fascicule fascicule = null;
        Fichier fichier = null;
        PDD pdd = null;
        OrdreDuJour ordreDuJour = null;
        NatureDesDossiers natureDesDossiers = null;

        String strSQL = SQL_QUERY_SELECT_ENTREES_BY_FILTER;
        strSQL += ( SQL_FILTER_ODJ + SQL_ORDER_BY_NUMERO_ORDRE );

        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );

        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            entreeOrdreDuJour = new EntreeOrdreDuJour(  );
            entreeOrdreDuJour.setIdEntreeOrdreDuJour( daoUtil.getInt( 1 ) );

            if ( null != daoUtil.getObject( 2 ) )
            {
                voeuAmendement = new VoeuAmendement(  );
                voeuAmendement.setIdVoeuAmendement( daoUtil.getInt( 2 ) );
                voeuAmendement.setType( daoUtil.getString( 3 ) );
                voeuAmendement.setReference( daoUtil.getString( 4 ) );
                voeuAmendement.setObjet( daoUtil.getString( 5 ) );
                voeuAmendement.setEnLigne( daoUtil.getBoolean( 6 ) );
                //creation de l'objet fichier
                fichier = new Fichier(  );
                fichier.setId( daoUtil.getInt( 7 ) );
                voeuAmendement.setFichier( fichier );
                //creation de l'objet fascicule associé au va
                fascicule = new Fascicule(  );
                fascicule.setIdFascicule( daoUtil.getInt( 8 ) );
                fascicule.setNomFascicule( daoUtil.getString( 9 ) );
                fascicule.setCodeFascicule( daoUtil.getString( 10 ) );
                voeuAmendement.setFascicule( fascicule );
                entreeOrdreDuJour.setVoeuAmendement( voeuAmendement );
            }

            ordreDuJour = new OrdreDuJour(  );
            ordreDuJour.setIdOrdreDuJour( daoUtil.getInt( 11 ) );
            entreeOrdreDuJour.setOrdreDuJour( ordreDuJour );

            if ( null != daoUtil.getObject( 12 ) )
            {
                natureDesDossiers = new NatureDesDossiers(  );
                natureDesDossiers.setIdNature( daoUtil.getInt( 12 ) );
                natureDesDossiers.setLibelleNature( daoUtil.getString( 13 ) );
                natureDesDossiers.setNumeroNature( daoUtil.getInt( 14 ) );
                entreeOrdreDuJour.setNature( natureDesDossiers );
            }

            if ( null != daoUtil.getObject( 15 ) )
            {
                pdd = new PDD(  );
                pdd.setIdPdd( daoUtil.getInt( 15 ) );
                pdd.setReference( daoUtil.getString( 16 ) );
                pdd.setTypePdd( daoUtil.getString( 17 ) );
                pdd.setEnLigne( daoUtil.getBoolean( 18 ) );
                pdd.setDatePublication( daoUtil.getTimestamp( 19 ) );
                pdd.setObjet( daoUtil.getString( 20 ) );
                pdd.setDelegationsServices( daoUtil.getBoolean( 21 ) );
                entreeOrdreDuJour.setPdd( pdd );
            }

            //récupération de la liste des rapporteurs associés
            entreeOrdreDuJour.setElus( selectRapporteursbyIdEntree( entreeOrdreDuJour.getIdEntreeOrdreDuJour(  ), plugin ) );
            // récupération de la liste des commissions associés
            entreeOrdreDuJour.setCommissions( selectCommissionListByIdEntree( 
                    entreeOrdreDuJour.getIdEntreeOrdreDuJour(  ), plugin ) );

            entreeOrdreDuJour.setType( daoUtil.getString( 22 ) );
            entreeOrdreDuJour.setNumeroOrdre( daoUtil.getInt( 23 ) );
            entreeOrdreDuJour.setTexte( daoUtil.getString( 24 ) );
            entreeOrdreDuJour.setReference( daoUtil.getString( 25 ) );
            entreeOrdreDuJour.setObjet( daoUtil.getString( 26 ) );
            entreeOrdreDuJour.setStyle( daoUtil.getString( 27 ) );

            listentreesOrdreDuJour.add( entreeOrdreDuJour );
        }

        daoUtil.free(  );

        return listentreesOrdreDuJour;
    }

    /**
     * renvoie la liste d'entrées de l'ordre du jour correspondant aux filtres appliqués
     * @param filter Filtre sur les entrées d'ordre du jour
     * @param plugin le plugin ODS
     * @return la liste d'entrées de l'ordre du jour correspondant aux filtres appliqués
     */
    public List<EntreeOrdreDuJour> findEntreesListByFilter( EntreeOrdreDuJourFilter filter, Plugin plugin )
    {
        List<EntreeOrdreDuJour> listentreesOrdreDuJour = new ArrayList<EntreeOrdreDuJour>(  );
        EntreeOrdreDuJour entreeOrdreDuJour = null;
        VoeuAmendement voeuAmendement = null;
        Fascicule fascicule = null;
        Fichier fichier = null;
        PDD pdd = null;
        OrdreDuJour ordreDuJour = null;
        NatureDesDossiers natureDesDossiers = null;

        String strSQL = SQL_QUERY_SELECT_ENTREES_BY_FILTER;
        strSQL += ( ( filter.containsIdOrdreDuJourCriteria(  ) ) ? SQL_FILTER_ODJ : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdPddCriteria(  ) ) ? SQL_FILTER_PDD : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdPublieCriteria(  ) ) ? SQL_FILTER_PUBLICATION : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdSeanceCriteria(  ) ) ? SQL_FILTER_SEANCE : OdsConstants.CONSTANTE_CHAINE_VIDE );

        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );

        int nIndex = 1;

        // Filtre sur ordre du jour
        if ( filter.containsIdOrdreDuJourCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdOrdreDuJour(  ) );
            nIndex++;
        }

        // Filtre sur PDD
        if ( filter.containsIdPddCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdPdd(  ) );
            nIndex++;
        }

        // Filtre sur publication
        if ( filter.containsIdPublieCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdPublie(  ) );
            nIndex++;
        }

        // Filtre sur seance
        if ( filter.containsIdSeanceCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdSeance(  ) );
            nIndex++;
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            entreeOrdreDuJour = new EntreeOrdreDuJour(  );
            entreeOrdreDuJour.setIdEntreeOrdreDuJour( daoUtil.getInt( 1 ) );

            if ( null != daoUtil.getObject( 2 ) )
            {
                voeuAmendement = new VoeuAmendement(  );
                voeuAmendement.setIdVoeuAmendement( daoUtil.getInt( 2 ) );
                voeuAmendement.setType( daoUtil.getString( 3 ) );
                voeuAmendement.setReference( daoUtil.getString( 4 ) );
                voeuAmendement.setObjet( daoUtil.getString( 5 ) );
                voeuAmendement.setEnLigne( daoUtil.getBoolean( 6 ) );
                //creation de l'objet fichier
                fichier = new Fichier(  );
                fichier.setId( daoUtil.getInt( 7 ) );
                voeuAmendement.setFichier( fichier );
                //creation de l'objet fascicule associé au va
                fascicule = new Fascicule(  );
                fascicule.setIdFascicule( daoUtil.getInt( 8 ) );
                fascicule.setNomFascicule( daoUtil.getString( 9 ) );
                fascicule.setCodeFascicule( daoUtil.getString( 10 ) );
                voeuAmendement.setFascicule( fascicule );
                entreeOrdreDuJour.setVoeuAmendement( voeuAmendement );
            }

            ordreDuJour = new OrdreDuJour(  );
            ordreDuJour.setIdOrdreDuJour( daoUtil.getInt( 11 ) );
            entreeOrdreDuJour.setOrdreDuJour( ordreDuJour );

            if ( null != daoUtil.getObject( 12 ) )
            {
                natureDesDossiers = new NatureDesDossiers(  );
                natureDesDossiers.setIdNature( daoUtil.getInt( 12 ) );
                natureDesDossiers.setLibelleNature( daoUtil.getString( 13 ) );
                natureDesDossiers.setNumeroNature( daoUtil.getInt( 14 ) );
                entreeOrdreDuJour.setNature( natureDesDossiers );
            }

            if ( null != daoUtil.getObject( 15 ) )
            {
                pdd = new PDD(  );
                pdd.setIdPdd( daoUtil.getInt( 15 ) );
                pdd.setReference( daoUtil.getString( 16 ) );
                pdd.setTypePdd( daoUtil.getString( 17 ) );
                pdd.setEnLigne( daoUtil.getBoolean( 18 ) );
                pdd.setDatePublication( daoUtil.getTimestamp( 19 ) );
                pdd.setObjet( daoUtil.getString( 20 ) );
                pdd.setDelegationsServices( daoUtil.getBoolean( 21 ) );
                entreeOrdreDuJour.setPdd( pdd );
            }

            //récupération de la liste des rapporteurs associés
            entreeOrdreDuJour.setElus( selectRapporteursbyIdEntree( entreeOrdreDuJour.getIdEntreeOrdreDuJour(  ), plugin ) );
            // récupération de la liste des commissions associés
            entreeOrdreDuJour.setCommissions( selectCommissionListByIdEntree( 
                    entreeOrdreDuJour.getIdEntreeOrdreDuJour(  ), plugin ) );

            entreeOrdreDuJour.setType( daoUtil.getString( 22 ) );
            entreeOrdreDuJour.setNumeroOrdre( daoUtil.getInt( 23 ) );
            entreeOrdreDuJour.setTexte( daoUtil.getString( 24 ) );
            entreeOrdreDuJour.setReference( daoUtil.getString( 25 ) );
            entreeOrdreDuJour.setObjet( daoUtil.getString( 26 ) );
            entreeOrdreDuJour.setStyle( daoUtil.getString( 27 ) );

            listentreesOrdreDuJour.add( entreeOrdreDuJour );
        }

        daoUtil.free(  );

        return listentreesOrdreDuJour;
    }

    /**
     *        modifie l'entrée d'ordre du jour transmise en parametre
     * @param entree EntreeOrdreDuJour
     * @param plugin Plugin
     */
    public void store( EntreeOrdreDuJour entree, Plugin plugin )
    {
        DAOUtil daoUtil;
        daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, entree.getIdEntreeOrdreDuJour(  ) );

        if ( null != entree.getVoeuAmendement(  ) )
        {
            daoUtil.setInt( 2, entree.getVoeuAmendement(  ).getIdVoeuAmendement(  ) );
        }
        else
        {
            daoUtil.setIntNull( 2 );
        }

        daoUtil.setInt( 3, entree.getOrdreDuJour(  ).getIdOrdreDuJour(  ) );

        if ( ( null != entree.getNature(  ) ) && ( entree.getNature(  ).getIdNature(  ) != -1 ) )
        {
            daoUtil.setInt( 4, entree.getNature(  ).getIdNature(  ) );
        }
        else
        {
            daoUtil.setIntNull( 4 );
        }

        if ( null != entree.getPdd(  ) )
        {
            daoUtil.setInt( 5, entree.getPdd(  ).getIdPdd(  ) );
        }
        else
        {
            daoUtil.setIntNull( 5 );
        }

        daoUtil.setString( 6, entree.getType(  ) );
        daoUtil.setInt( 7, entree.getNumeoOrdre(  ) );
        daoUtil.setString( 8, entree.getTexteLibre(  ) );
        daoUtil.setString( 9, entree.getReference(  ) );
        daoUtil.setString( 10, entree.getObjet(  ) );
        daoUtil.setString( 11, entree.getStyle(  ) );
        daoUtil.setInt( 12, entree.getIdEntreeOrdreDuJour(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * retourne la liste des rapporteurs de l'entrée d'ordre du jour d'id nKey
     * @param nKey id de l'entrée de l'ordre du jour
     * @param plugin plugin
     * @return liste des rapporteurs de l'entrée de l'ordre du jour
     */
    public List<Elu> selectRapporteursbyIdEntree( int nKey, Plugin plugin )
    {
        List<Elu> elus = new ArrayList<Elu>(  );
        Commission commission = null;
        Elu elu = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ELU_LIST_BY_ID_ENTREE, plugin );
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
    public List<Commission> selectCommissionListByIdEntree( int nKey, Plugin plugin )
    {
        List<Commission> commissions = new ArrayList<Commission>(  );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COMMISSION_LIST_BY_ID_ENTREE, plugin );
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

    /**
     * Insere une commission à l 'entrée d'id nIdEntree,
     * @param nIdEntree l'id de l'entrée
     * @param nIdCommission l'id de la  commission à insérer
     * @param plugin le Plugin
     */
    public void insertCommission( int nIdEntree, int nIdCommission, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_COMMISSION, plugin );
        daoUtil.setInt( 1, nIdEntree );
        daoUtil.setInt( 2, nIdCommission );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Insere un rapporteur à l 'entrée d'id nIdEntree,
     * @param nIdEntree l'id de l'entrée
     * @param nIdElu l'id de l"elu à insérer
     * @param plugin le Plugin
     */
    public void insertRapporteur( int nIdEntree, int nIdElu, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_RAPPORTEUR, plugin );
        daoUtil.setInt( 1, nIdEntree );
        daoUtil.setInt( 2, nIdElu );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime toutes les commissions liées à  l 'entrée d'id nIdEntree,
     * @param nIdEntree l'id de l'entrée
     * @param plugin le Plugin
     */
    public void deleteCommission( int nIdEntree, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_COMMISSION, plugin );
        daoUtil.setInt( 1, nIdEntree );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     *Supprime tous les rapporteurs liés à l'entrée d'id nIdEntree,
     * @param nIdEntree l'id de l'entrée
     * @param plugin le Plugin
     */
    public void deleteRapporteur( int nIdEntree, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_RAPPORTEUR, plugin );
        daoUtil.setInt( 1, nIdEntree );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
