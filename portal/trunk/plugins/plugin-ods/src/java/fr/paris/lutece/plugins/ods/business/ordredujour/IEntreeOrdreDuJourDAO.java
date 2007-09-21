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

import java.util.List;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.portal.service.plugin.Plugin;


/**
 *
 *
 */
public interface IEntreeOrdreDuJourDAO
{
    /**
     * Supprime l'entr�e d'ordre du jour pass�e en parametre
     * @param entree EntreeOrdreDuJour
     * @param plugin Plugin
     */
    void delete( EntreeOrdreDuJour entree, Plugin plugin );

    /**
     * Insere l'entr�e d'ordre du jour pass� en parametre dans la table ods_entree_ordre_jour
     * @param entree EntreeOrdreDuJour
     * @param plugin Plugin
     * @return l'id de la nouvelle entr�e
     */
    int insert( EntreeOrdreDuJour entree, Plugin plugin );

    /**
     * Renvoie l'entr�e d'ordre du jour d'id nKey
     * @param nKey int
     * @param plugin Plugin
     * @return EntreeOrdreDuJour entree
     */
    EntreeOrdreDuJour load( int nKey, Plugin plugin );

    /**
     * Modifie l'entr�e d'ordre du jour transmise en parametre
     * @param entree EntreeOrdreDuJour
     * @param plugin Plugin
     */
    void store( EntreeOrdreDuJour entree, Plugin plugin );

    /**
     * renvoie la liste d'entr�es de l'ordre du jour ayant comme id nKey
     * @param nKey int l'id de l'ordre du jour
     * @param plugin Plugin
     * @return la liste d'entr�es de l'ordre du jour d'id nKey
     */
    List<EntreeOrdreDuJour> selectEntreesByIdOrdreDuJour( int nKey, Plugin plugin );

    /**
     * Renvoie la liste d'entr�es de l'ordre du jour correspondant aux filtres appliqu�s
     * @param filter Filtre sur les entr�es d'ordre du jour
     * @param plugin le plugin ODS
     * @return la liste d'entr�es de l'ordre du jour correspondant aux filtres appliqu�s
     */
    List<EntreeOrdreDuJour> findEntreesListByFilter( EntreeOrdreDuJourFilter filter, Plugin plugin );

    /**
     * Insere une commission � l 'entr�e d'id nIdEntree,
     * @param nIdEntree l'id de l'entr�e
     * @param nIdCommission l'id de la  commission � ins�rer
     * @param plugin le Plugin
     */
    void insertCommission( int nIdEntree, int nIdCommission, Plugin plugin );

    /**
     * Insere un rapporteur � l 'entr�e d'id nIdEntree,
     * @param nIdEntree l'id de l'entr�e
     * @param nIdElu l'id de l"elu � ins�rer
     * @param plugin le Plugin
     */
    void insertRapporteur( int nIdEntree, int nIdElu, Plugin plugin );

    
    /**
     * Retourne la liste des commissions ajout a l'entree de l'ordre du jour
     * 
     * @param nKey id de l'entree de l'ordre du jour
     * @param plugin Plugin
     */
    List<Commission> selectCommissionListByIdEntree( int nKey, Plugin plugin );

    /**
     * Retourne la liste des Rapporteur ajout a l'entree de l'ordre du jour
     * 
     * @param nKey id de l'entree de l'ordre du jour
     * @param plugin Plugin
     */
    List<Elu> selectRapporteursbyIdEntree( int nKey, Plugin plugin );
    
    /**
     * Supprime toutes les commissions li�es �  l 'entr�e d'id nIdEntree,
     * @param nIdEntree l'id de l'entr�e
     * @param plugin le Plugin
     */
    void deleteCommission( int nIdEntree, Plugin plugin );

    /**
     * Supprime tous les rapporteurs li�s � l'entr�e d'id nIdEntree,
     * @param nIdEntree l'id de l'entr�e
     * @param plugin le Plugin
     */
    void deleteRapporteur( int nIdEntree, Plugin plugin );
}
