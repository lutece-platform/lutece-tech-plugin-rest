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
package fr.paris.lutece.plugins.ods.service.search.action;

import fr.paris.lutece.plugins.ods.service.search.indexer.Indexer;
import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


/**
 * Cette interface fournit les fonctions d'interaction avec
 * la base de données pour gérer les actions d'indexations.
 */
public interface IIndexerActionDAO
{
    /**
     * Créé une nouvelle action d'indexation
     * @param action l'action à ajouter
     * @param plugin plugin
     */
    void insert( IndexerAction action, Plugin plugin );

    /**
     * Supprime une action d'indexation
     * @param action l'action à supprimer
     * @param plugin plugin
     */
    void delete( IndexerAction action, Plugin plugin );

    /**
     * Met à jour une action d'indexation
     * @param action l'action à modifier
     * @param plugin plugin
     */
    void store( IndexerAction action, Plugin plugin );

    /**
     * Retourne la liste des actions pour un indexer, selon le type d'action voulu:
     * IndexAction.ACTION_CREATE,IndexAction.ACTION_DELETE, IndexAction.ACTION_MODIFY
     * @param nIdIndexer l'identifiant de l'indexer
     * @param nAction le type d'action voulu
     * @param bDelete indique si on supprime les actions chargées ou non
     * @param bArchive l'action concerne-t-elle archives ?
     * @param plugin plugin
     * @return la liste des actions
     */
    List<IndexerAction> selectIndexerActions( int nIdIndexer, int nAction, boolean bDelete, boolean bArchive,
        Plugin plugin );

    /**
     * Retourne l'action d'indexation voulue sur le fichier, si elle existe
     * @param nIdIndexer l'indexer sélectionné
     * @param nAction l'action sur le fichier
     * @param object l'objet
     * @param plugin plugin
     * @return une action d'indexation si elle existe, <b>null</b> sinon
     */
    IndexerAction selectActionOnObject( int nIdIndexer, int nAction, Object object, Plugin plugin );

    /**
     * Supprime toutes les actions d'indexation pour cet indexer;
     * @param indexer l'indexer
     * @param plugin le plugin
     */
    void deleteForIndexer( Indexer indexer, Plugin plugin );
}
