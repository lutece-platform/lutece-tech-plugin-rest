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
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 *
 *
 */
public class EntreeOrdreDuJourHome
{
    private static IEntreeOrdreDuJourDAO _dao = (IEntreeOrdreDuJourDAO) SpringContextService.getPluginBean( "ods",
            "entreeOrdreDuJourDAO" );

    /**
     * insere l'entrée d'ordre du jour passé en parametre dans la table ods_entree_ordre_jour
     * @param entree EntreeOrdreDuJour
     * @param plugin Plugin
     * @return l'id de la nouvelle entrée
     */
    public static int create( EntreeOrdreDuJour entree, Plugin plugin )
    {
        return _dao.insert( entree, plugin );
    }

    /**
     * renvoie l'entrée d'ordre du jour d'id nKey
     * @param nKey int
     * @param plugin Plugin
     * @return EntreeOrdreDuJour entree
     */
    public static EntreeOrdreDuJour findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * supprime l'entrée d'ordre du jour passée en parametre
     * @param entree EntreeOrdreDuJour
     * @param plugin Plugin
     */
    public static void remove( EntreeOrdreDuJour entree, Plugin plugin )
    {
        _dao.delete( entree, plugin );
    }

    /**
     *        modifie l'entrée d'ordre du jour transmise en parametre
     * @param entree EntreeOrdreDuJour
     * @param plugin Plugin
     */
    public static void update( EntreeOrdreDuJour entree, Plugin plugin )
    {
        _dao.store( entree, plugin );
    }

    /**
     * renvoie la liste d'entrées de l'ordre du jour ayant comme id nKey
     * @param nKey int l'id de l'ordre du jour
     * @param plugin Plugin
     * @return la liste d'entrées de l'ordre du jour d'id nKey
     */
    public static List<EntreeOrdreDuJour> findEntreesListByIdOrdreDuJour( int nKey, Plugin plugin )
    {
        return _dao.selectEntreesByIdOrdreDuJour( nKey, plugin );
    }

    /**
     * Renvoie la liste d'entrées de l'ordre du jour correspondant aux filtres appliqués
     * @param filter Filtre sur les entrées d'ordre du jour
     * @param plugin le plugin ODS
     * @return la liste d'entrées de l'ordre du jour correspondant aux filtres appliqués
     */
    public static List<EntreeOrdreDuJour> findEntreesListByFilter( EntreeOrdreDuJourFilter filter, Plugin plugin )
    {
        return _dao.findEntreesListByFilter( filter, plugin );
    }

    /**
     * Retourne la liste des commissions ajout a l'entree de l'ordre du jour
     * 
     * @param nKey id de l'entree de l'ordre du jour
     * @param plugin Plugin
     */
    public static  List<Commission> findCommissionListByIdEntree( int nKey, Plugin plugin )
    {
        return _dao.selectCommissionListByIdEntree( nKey, plugin );
    }

    /**
     * Retourne la liste des Rapporteur ajout a l'entree de l'ordre du jour
     * 
     * @param nKey id de l'entree de l'ordre du jour
     * @param plugin Plugin
     */
    public static  List<Elu> findRapporteursbyIdEntree( int nKey, Plugin plugin )
    {
        return _dao.selectRapporteursbyIdEntree( nKey, plugin );
    }

    /**
     * Insere une commission à l 'entrée d'id nIdEntree,
     * @param nIdEntree l'id de l'entrée
     * @param nIdCommission l'id de la  commission à insérer
     * @param plugin le Plugin
     */
    public static void insertCommission( int nIdEntree, int nIdCommission, Plugin plugin )
    {
        _dao.insertCommission( nIdEntree, nIdCommission, plugin );
    }

    /**
     * Insere un rapporteur à l 'entrée d'id nIdEntree,
     * @param nIdEntree l'id de l'entrée
     * @param nIdElu l'id de l"elu à insérer
     * @param plugin le Plugin
     */
    public static void insertRapporteur( int nIdEntree, int nIdElu, Plugin plugin )
    {
        _dao.insertRapporteur( nIdEntree, nIdElu, plugin );
    }

    /**
     * Supprime toutes les commissions liées à  l 'entrée d'id nIdEntree,
     * @param nIdEntree l'id de l'entrée
     * @param plugin le Plugin
     */
    public static void deleteCommission( int nIdEntree, Plugin plugin )
    {
        _dao.deleteCommission( nIdEntree, plugin );
    }

    /**
     *Supprime tous les rapporteurs liés à l'entrée d'id nIdEntree,
     * @param nIdEntree l'id de l'entrée
     * @param plugin le Plugin
     */
    public static void deleteRapporteur( int nIdEntree, Plugin plugin )
    {
        _dao.deleteRapporteur( nIdEntree, plugin );
    }
}
