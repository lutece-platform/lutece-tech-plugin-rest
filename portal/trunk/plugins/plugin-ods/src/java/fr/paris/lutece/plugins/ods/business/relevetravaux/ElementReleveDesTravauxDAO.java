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

import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitique;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * ElementReleveDesTravauxDAO
 */
public class ElementReleveDesTravauxDAO implements IElementReleveDesTravauxDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_elmt_releve ) FROM ods_elmt_releve_travaux ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_elmt_releve_travaux " +
        "			(id_elmt_releve, id_pdd, id_releve, id_va, id_elu, id_groupe,  " +
        "            pour, contre, abst, nppv, observations)  " + "VALUES (?,?,?,?,?,?,?,?,?,?,?) ";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_elmt_releve, id_pdd, id_releve, id_va, id_elu, id_groupe,  " +
        "     	pour, contre, abst, nppv, observations   " + "FROM ods_elmt_releve_travaux elmt " +
        "WHERE elmt.id_elmt_releve = ?    ";
    private static final String SQL_QUERY_ELMT_RELEVE_LIST = "SELECT id_elmt_releve, id_pdd, id_releve, id_va, id_elu, id_groupe,  " +
        "     	pour, contre, abst, nppv, observations   " + "FROM ods_elmt_releve_travaux elmt ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ods_elmt_releve_travaux WHERE id_elmt_releve = ?  ";
    private static final String SQL_QUERY_DELETE_BY_PDD = "DELETE FROM ods_elmt_releve_travaux WHERE id_pdd = ?  ";
    private static final String SQL_QUERY_DELETE_BY_VA = "DELETE FROM ods_elmt_releve_travaux WHERE id_va = ?  ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ods_elmt_releve_travaux " +
        "SET id_pdd=?, id_releve=?, id_va=?, id_elu=?, id_groupe=?,  " +
        "     	pour=?, contre=?, abst=?, nppv=?, observations=?  " + "WHERE id_elmt_releve=? ";
    private static final String SQL_QUERY_FIND_BY_RELEVE = "SELECT elmt.id_elmt_releve, elmt.id_pdd, elmt.id_va, elmt.id_elu, elmt.id_groupe,  " +
        "     	elmt.pour, elmt.contre, elmt.abst, elmt.nppv, elmt.observations   " +
        "FROM ods_elmt_releve_travaux elmt " + "WHERE elmt.id_releve=?  ";
    private static final String SQL_QUERY_FIND_BY_RELEVE_TYPE_VA_RATACHE = "SELECT elmt.id_elmt_releve, elmt.id_va, elmt.id_elu, elmt.id_groupe, " +
        "       elmt.pour, elmt.contre, elmt.abst, elmt.nppv, elmt.observations  " +
        " FROM ods_elmt_releve_travaux elmt " + " LEFT JOIN ods_voeu_amendement va ON (elmt.id_va = va.id_va) " +
        " LEFT JOIN ods_va_rattache_pdd vrp ON (elmt.id_va = vrp.id_va) " +
        " LEFT JOIN ods_pdd pdd ON (vrp.id_pdd = pdd.id_pdd) " + " WHERE elmt.id_releve = ? " +
        " AND elmt.id_va = va.id_va " + " AND (va.type ='V'or va.type ='A' or va.type ='LR') " +
        " AND va.id_va IS NOT NULL " + " GROUP BY elmt.id_elmt_releve " +
        " ORDER BY vrp.id_pdd, vrp.numero_ordre, pdd.reference, va.type ";
    private static final String SQL_QUERY_FIND_BY_RELEVE_TYPE_VA_NON_RATACHE = "SELECT elmt.id_elmt_releve, elmt.id_va, elmt.id_elu, elmt.id_groupe, " +
        "  elmt.pour, elmt.contre, elmt.abst, elmt.nppv, elmt.observations  " + " FROM ods_elmt_releve_travaux elmt " +
        " LEFT JOIN ods_va_depose_par vdp ON (elmt.id_va = vdp.id_va) " +
        " LEFT JOIN ods_elu elu ON (elmt.id_elu = elu.id_elu) " +
        " LEFT JOIN ods_voeu_amendement va ON ( elmt.id_va = va.id_va ) " + " WHERE elmt.id_releve = ? " +
        " AND va.type ='VNR' " + " GROUP BY elmt.id_elmt_releve " + " ORDER BY va.date_publication ASC ";
    private static final String SQL_QUERY_FIND_BY_RELEVE_TYPE_PDD = "SELECT elmt.id_elmt_releve, elmt.id_pdd, elmt.id_elu, elmt.id_groupe,  " +
        "     	elmt.pour, elmt.contre, elmt.abst, elmt.nppv, elmt.observations   " +
        "FROM ods_elmt_releve_travaux elmt " + "WHERE elmt.id_releve=? " +
        " AND elmt.id_pdd IS NOT NULL AND elmt.id_va IS NULL ";

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
     * Supprime un élément de relevé de travaux de la base de données
     * @param elementReleve l'élément de relevé de travaux à supprimer
     * @param plugin le Plugin actif
     * @throws AppException SQLException si l'élément ne peut être supprimé
     */
    public void delete( ElementReleveDesTravaux elementReleve, Plugin plugin )
        throws AppException
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, elementReleve.getIdElementReleveDesTravaux(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * @see fr.paris.lutece.plugins.ods.business.relevetravaux.IElementReleveDesTravauxDAO#insert(fr.paris.lutece.plugins.ods.business.relevetravaux.ElementReleveDesTravaux, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void insert( ElementReleveDesTravaux elementReleve, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        int nKey = newPrimaryKey( plugin );
        daoUtil.setInt( 1, nKey );

        //si le PDD = -1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs séance 
        if ( null != elementReleve.getProjetDeliberation(  ) )
        {
            daoUtil.setInt( 2, elementReleve.getProjetDeliberation(  ).getIdPdd(  ) );
        }
        else
        {
            daoUtil.setIntNull( 2 );
        }

        //si le relevé des travaux = -1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs commission   
        if ( null != elementReleve.getReleve(  ) )
        {
            daoUtil.setInt( 3, elementReleve.getReleve(  ).getIdReleveDesTravaux(  ) );
        }
        else
        {
            daoUtil.setIntNull( 3 );
        }

        //si le voeu ou amendement = -1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs séance 
        if ( null != elementReleve.getVoeuAmendement(  ) )
        {
            daoUtil.setInt( 4, elementReleve.getVoeuAmendement(  ).getIdVoeuAmendement(  ) );
        }
        else
        {
            daoUtil.setIntNull( 4 );
        }

        //si l'élu = -1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs séance 
        if ( null != elementReleve.getElu(  ) )
        {
            daoUtil.setInt( 5, elementReleve.getElu(  ).getIdElu(  ) );
        }
        else
        {
            daoUtil.setIntNull( 5 );
        }

        //si le groupe = -1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs séance 
        if ( null != elementReleve.getGroupe(  ) )
        {
            daoUtil.setInt( 6, elementReleve.getGroupe(  ).getIdGroupe(  ) );
        }
        else
        {
            daoUtil.setIntNull( 6 );
        }

        daoUtil.setInt( 7, elementReleve.getPour(  ) );
        daoUtil.setInt( 8, elementReleve.getContre(  ) );
        daoUtil.setInt( 9, elementReleve.getAbstention(  ) );
        daoUtil.setInt( 10, elementReleve.getNePouvantPasVoter(  ) );
        daoUtil.setString( 11, elementReleve.getObservations(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * @see fr.paris.lutece.plugins.ods.business.relevetravaux.IElementReleveDesTravauxDAO#load(int, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public ElementReleveDesTravaux load( int nKey, Plugin plugin )
    {
        ElementReleveDesTravaux elementReleve = null;
        PDD pdd = null;
        ReleveDesTravaux releve = null;
        VoeuAmendement va = null;
        Elu elu = null;
        GroupePolitique groupe = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            elementReleve = new ElementReleveDesTravaux(  );

            elementReleve.setIdElementReleveDesTravaux( daoUtil.getInt( 1 ) );

            if ( null != daoUtil.getObject( 2 ) )
            {
                pdd = new PDD(  );
                pdd.setIdPdd( daoUtil.getInt( 2 ) );
                elementReleve.setProjetDeliberation( pdd );
            }

            if ( null != daoUtil.getObject( 3 ) )
            {
                releve = new ReleveDesTravaux(  );
                releve.setIdReleveDesTravaux( daoUtil.getInt( 3 ) );
                elementReleve.setReleve( releve );
            }

            if ( null != daoUtil.getObject( 4 ) )
            {
                va = new VoeuAmendement(  );
                va.setIdVoeuAmendement( daoUtil.getInt( 4 ) );
                elementReleve.setVoeuAmendement( va );
            }

            if ( null != daoUtil.getObject( 5 ) )
            {
                elu = new Elu(  );
                elu.setIdElu( daoUtil.getInt( 5 ) );
                elementReleve.setElu( elu );
            }

            if ( null != daoUtil.getObject( 6 ) )
            {
                groupe = new GroupePolitique(  );
                groupe.setIdGroupe( daoUtil.getInt( 6 ) );
                elementReleve.setGroupe( groupe );
            }

            elementReleve.setPour( daoUtil.getInt( 7 ) );
            elementReleve.setContre( daoUtil.getInt( 8 ) );
            elementReleve.setAbstention( daoUtil.getInt( 9 ) );
            elementReleve.setNePouvantPasVoter( daoUtil.getInt( 10 ) );
            elementReleve.setObservations( daoUtil.getString( 11 ) );
        }

        daoUtil.free(  );

        return elementReleve;
    }

    /**
     * @see fr.paris.lutece.plugins.ods.business.relevetravaux.IElementReleveDesTravauxDAO#store(fr.paris.lutece.plugins.ods.business.relevetravaux.ElementReleveDesTravaux, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public void store( ElementReleveDesTravaux elementReleve, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        //si le PDD = -1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs séance 
        if ( null != elementReleve.getProjetDeliberation(  ) )
        {
            daoUtil.setInt( 1, elementReleve.getProjetDeliberation(  ).getIdPdd(  ) );
        }
        else
        {
            daoUtil.setIntNull( 1 );
        }

        //si le relevé des travaux = -1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs commission   
        if ( null != elementReleve.getReleve(  ) )
        {
            daoUtil.setInt( 2, elementReleve.getReleve(  ).getIdReleveDesTravaux(  ) );
        }
        else
        {
            daoUtil.setIntNull( 2 );
        }

        //si le voeu ou amendement = -1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs séance 
        if ( null != elementReleve.getVoeuAmendement(  ) )
        {
            daoUtil.setInt( 3, elementReleve.getVoeuAmendement(  ).getIdVoeuAmendement(  ) );
        }
        else
        {
            daoUtil.setIntNull( 3 );
        }

        //si l'élu = -1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs séance 
        if ( null != elementReleve.getElu(  ) )
        {
            daoUtil.setInt( 4, elementReleve.getElu(  ).getIdElu(  ) );
        }
        else
        {
            daoUtil.setIntNull( 4 );
        }

        //si le groupe = -1 elle n'a pas été renseignée on rentre donc la valeur null dans le champs séance 
        if ( null != elementReleve.getGroupe(  ) )
        {
            daoUtil.setInt( 5, elementReleve.getGroupe(  ).getIdGroupe(  ) );
        }
        else
        {
            daoUtil.setIntNull( 5 );
        }

        daoUtil.setInt( 6, elementReleve.getPour(  ) );
        daoUtil.setInt( 7, elementReleve.getContre(  ) );
        daoUtil.setInt( 8, elementReleve.getAbstention(  ) );
        daoUtil.setInt( 9, elementReleve.getNePouvantPasVoter(  ) );
        daoUtil.setString( 10, elementReleve.getObservations(  ) );

        daoUtil.setInt( 11, elementReleve.getIdElementReleveDesTravaux(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * @see fr.paris.lutece.plugins.ods.business.relevetravaux.IElementReleveDesTravauxDAO#selectElementReleveDesTravauxList(fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public List<ElementReleveDesTravaux> selectElementReleveDesTravauxList( Plugin plugin )
    {
        List<ElementReleveDesTravaux> elementsReleve = new ArrayList<ElementReleveDesTravaux>(  );
        PDD pdd = null;
        ReleveDesTravaux releve = null;
        VoeuAmendement va = null;
        Elu elu = null;
        GroupePolitique groupe = null;
        ElementReleveDesTravaux elementReleve = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ELMT_RELEVE_LIST, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            elementReleve = new ElementReleveDesTravaux(  );

            elementReleve.setIdElementReleveDesTravaux( daoUtil.getInt( 1 ) );

            if ( null != daoUtil.getObject( 2 ) )
            {
                pdd = new PDD(  );
                pdd.setIdPdd( daoUtil.getInt( 2 ) );
                elementReleve.setProjetDeliberation( pdd );
            }

            if ( null != daoUtil.getObject( 3 ) )
            {
                releve = new ReleveDesTravaux(  );
                releve.setIdReleveDesTravaux( daoUtil.getInt( 3 ) );
                elementReleve.setReleve( releve );
            }

            if ( null != daoUtil.getObject( 4 ) )
            {
                va = new VoeuAmendement(  );
                va.setIdVoeuAmendement( daoUtil.getInt( 4 ) );
                elementReleve.setVoeuAmendement( va );
            }

            if ( null != daoUtil.getObject( 5 ) )
            {
                elu = new Elu(  );
                elu.setIdElu( daoUtil.getInt( 5 ) );
                elementReleve.setElu( elu );
            }

            if ( null != daoUtil.getObject( 6 ) )
            {
                groupe = new GroupePolitique(  );
                groupe.setIdGroupe( daoUtil.getInt( 6 ) );
                elementReleve.setGroupe( groupe );
            }

            elementReleve.setPour( daoUtil.getInt( 7 ) );
            elementReleve.setContre( daoUtil.getInt( 8 ) );
            elementReleve.setAbstention( daoUtil.getInt( 9 ) );
            elementReleve.setNePouvantPasVoter( daoUtil.getInt( 10 ) );
            elementReleve.setObservations( daoUtil.getString( 11 ) );

            elementsReleve.add( elementReleve );
        }

        daoUtil.free(  );

        return elementsReleve;
    }

    /**
     * @see fr.paris.lutece.plugins.ods.business.relevetravaux.IElementReleveDesTravauxDAO#selectElementReleveDesTravauxReleve(fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravaux, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public List<ElementReleveDesTravaux> selectElementReleveDesTravauxReleve( ReleveDesTravaux releve, Plugin plugin )
    {
        List<ElementReleveDesTravaux> elementsReleve = new ArrayList<ElementReleveDesTravaux>(  );
        PDD pdd = null;
        VoeuAmendement va = null;
        Elu elu = null;
        GroupePolitique groupe = null;
        ElementReleveDesTravaux elementReleve = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_RELEVE, plugin );
        daoUtil.setInt( 1, releve.getIdReleveDesTravaux(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            elementReleve = new ElementReleveDesTravaux(  );

            elementReleve.setIdElementReleveDesTravaux( daoUtil.getInt( 1 ) );

            if ( null != daoUtil.getObject( 2 ) )
            {
                pdd = new PDD(  );
                pdd.setIdPdd( daoUtil.getInt( 2 ) );
                elementReleve.setProjetDeliberation( pdd );
            }

            elementReleve.setReleve( releve );

            if ( null != daoUtil.getObject( 3 ) )
            {
                va = new VoeuAmendement(  );
                va.setIdVoeuAmendement( daoUtil.getInt( 3 ) );
                elementReleve.setVoeuAmendement( va );
            }

            if ( null != daoUtil.getObject( 4 ) )
            {
                elu = new Elu(  );
                elu.setIdElu( daoUtil.getInt( 4 ) );
                elementReleve.setElu( elu );
            }

            if ( null != daoUtil.getObject( 5 ) )
            {
                groupe = new GroupePolitique(  );
                groupe.setIdGroupe( daoUtil.getInt( 5 ) );
                elementReleve.setGroupe( groupe );
            }

            elementReleve.setPour( daoUtil.getInt( 6 ) );
            elementReleve.setContre( daoUtil.getInt( 7 ) );
            elementReleve.setAbstention( daoUtil.getInt( 8 ) );
            elementReleve.setNePouvantPasVoter( daoUtil.getInt( 9 ) );
            elementReleve.setObservations( daoUtil.getString( 10 ) );

            elementsReleve.add( elementReleve );
        }

        daoUtil.free(  );

        return elementsReleve;
    }

    /**
     * @see fr.paris.lutece.plugins.ods.business.relevetravaux.IElementReleveDesTravauxDAO#selectElementReleveDesTravauxReleveVA(fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravaux, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public List<ElementReleveDesTravaux> selectElementReleveDesTravauxReleveVA( ReleveDesTravaux releve, Plugin plugin )
    {
        List<ElementReleveDesTravaux> listElementsReleve = new ArrayList<ElementReleveDesTravaux>(  );
        PDD pdd = null;
        VoeuAmendement va = null;
        Elu elu = null;
        GroupePolitique groupe = null;
        ElementReleveDesTravaux elementReleve = null;

        /*
         * On récupère d'abord les amendements et les voeux rattachés
         */
        DAOUtil daoUtil1 = new DAOUtil( SQL_QUERY_FIND_BY_RELEVE_TYPE_VA_RATACHE, plugin );
        daoUtil1.setInt( 1, releve.getIdReleveDesTravaux(  ) );
        daoUtil1.executeQuery(  );

        while ( daoUtil1.next(  ) )
        {
            elementReleve = new ElementReleveDesTravaux(  );

            elementReleve.setIdElementReleveDesTravaux( daoUtil1.getInt( 1 ) );
            elementReleve.setProjetDeliberation( pdd );
            elementReleve.setReleve( releve );

            if ( null != daoUtil1.getObject( 2 ) )
            {
                va = new VoeuAmendement(  );
                va.setIdVoeuAmendement( daoUtil1.getInt( 2 ) );
                elementReleve.setVoeuAmendement( va );
            }

            if ( null != daoUtil1.getObject( 3 ) )
            {
                elu = new Elu(  );
                elu.setIdElu( daoUtil1.getInt( 3 ) );
                elementReleve.setElu( elu );
            }

            if ( null != daoUtil1.getObject( 4 ) )
            {
                groupe = new GroupePolitique(  );
                groupe.setIdGroupe( daoUtil1.getInt( 4 ) );
                elementReleve.setGroupe( groupe );
            }

            elementReleve.setPour( daoUtil1.getInt( 5 ) );
            elementReleve.setContre( daoUtil1.getInt( 6 ) );
            elementReleve.setAbstention( daoUtil1.getInt( 7 ) );
            elementReleve.setNePouvantPasVoter( daoUtil1.getInt( 8 ) );
            elementReleve.setObservations( daoUtil1.getString( 9 ) );

            listElementsReleve.add( elementReleve );
        }

        /*
         * On récupère ensuite les voeux non rattachés
         */
        DAOUtil daoUtil2 = new DAOUtil( SQL_QUERY_FIND_BY_RELEVE_TYPE_VA_NON_RATACHE, plugin );
        daoUtil2.setInt( 1, releve.getIdReleveDesTravaux(  ) );
        daoUtil2.executeQuery(  );

        while ( daoUtil2.next(  ) )
        {
            elementReleve = new ElementReleveDesTravaux(  );

            elementReleve.setIdElementReleveDesTravaux( daoUtil2.getInt( 1 ) );
            elementReleve.setProjetDeliberation( pdd );
            elementReleve.setReleve( releve );

            if ( null != daoUtil2.getObject( 2 ) )
            {
                va = new VoeuAmendement(  );
                va.setIdVoeuAmendement( daoUtil2.getInt( 2 ) );
                elementReleve.setVoeuAmendement( va );
            }

            if ( null != daoUtil2.getObject( 3 ) )
            {
                elu = new Elu(  );
                elu.setIdElu( daoUtil2.getInt( 3 ) );
                elementReleve.setElu( elu );
            }

            if ( null != daoUtil2.getObject( 4 ) )
            {
                groupe = new GroupePolitique(  );
                groupe.setIdGroupe( daoUtil2.getInt( 4 ) );
                elementReleve.setGroupe( groupe );
            }

            elementReleve.setPour( daoUtil2.getInt( 5 ) );
            elementReleve.setContre( daoUtil2.getInt( 6 ) );
            elementReleve.setAbstention( daoUtil2.getInt( 7 ) );
            elementReleve.setNePouvantPasVoter( daoUtil2.getInt( 8 ) );
            elementReleve.setObservations( daoUtil2.getString( 9 ) );

            listElementsReleve.add( elementReleve );
        }

        daoUtil1.free(  );
        daoUtil2.free(  );

        return listElementsReleve;
    }

    /**
     * @see fr.paris.lutece.plugins.ods.business.relevetravaux.IElementReleveDesTravauxDAO#selectElementReleveDesTravauxRelevePDD(fr.paris.lutece.plugins.ods.business.relevetravaux.ReleveDesTravaux, fr.paris.lutece.portal.service.plugin.Plugin)
     */
    public List<ElementReleveDesTravaux> selectElementReleveDesTravauxRelevePDD( ReleveDesTravaux releve, Plugin plugin )
    {
        List<ElementReleveDesTravaux> elementsReleve = new ArrayList<ElementReleveDesTravaux>(  );
        PDD pdd = null;
        VoeuAmendement va = null;
        Elu elu = null;
        GroupePolitique groupe = null;
        ElementReleveDesTravaux elementReleve = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_RELEVE_TYPE_PDD, plugin );
        daoUtil.setInt( 1, releve.getIdReleveDesTravaux(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            elementReleve = new ElementReleveDesTravaux(  );

            elementReleve.setIdElementReleveDesTravaux( daoUtil.getInt( 1 ) );

            if ( null != daoUtil.getObject( 2 ) )
            {
                pdd = new PDD(  );
                pdd.setIdPdd( daoUtil.getInt( 2 ) );
                elementReleve.setProjetDeliberation( pdd );
            }

            elementReleve.setReleve( releve );
            elementReleve.setVoeuAmendement( va );

            if ( null != daoUtil.getObject( 3 ) )
            {
                elu = new Elu(  );
                elu.setIdElu( daoUtil.getInt( 3 ) );
                elementReleve.setElu( elu );
            }

            if ( null != daoUtil.getObject( 4 ) )
            {
                groupe = new GroupePolitique(  );
                groupe.setIdGroupe( daoUtil.getInt( 4 ) );
                elementReleve.setGroupe( groupe );
            }

            elementReleve.setPour( daoUtil.getInt( 5 ) );
            elementReleve.setContre( daoUtil.getInt( 6 ) );
            elementReleve.setAbstention( daoUtil.getInt( 7 ) );
            elementReleve.setNePouvantPasVoter( daoUtil.getInt( 8 ) );
            elementReleve.setObservations( daoUtil.getString( 9 ) );

            elementsReleve.add( elementReleve );
        }

        daoUtil.free(  );

        return elementsReleve;
    }

    public void delete( PDD pdd, Plugin plugin ) throws AppException
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_PDD, plugin );
        daoUtil.setInt( 1, pdd.getIdPdd(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    public void delete( VoeuAmendement voeuAmendement, Plugin plugin )
        throws AppException
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_VA, plugin );
        daoUtil.setInt( 1, voeuAmendement.getIdVoeuAmendement(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
