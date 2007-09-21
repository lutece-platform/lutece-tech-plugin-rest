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
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
* IndexerActionHome : appelle le DAO pour gérer les actions d'indexation
*/
public class IndexerActionHome
{
    private static IIndexerActionDAO _dao = (IIndexerActionDAO) SpringContextService.getPluginBean( "ods",
            "indexerActionDAO" );

    /**
     * Créé une nouvelle action d'indexation dans la base
     * @param action l'action à ajouter
     * @param plugin plugin
     */
    public static void create( IndexerAction action, Plugin plugin )
    {
        _dao.insert( action, plugin );
    }

    /**
     * Supprime une action d'indexation
     * @param action l'action à supprimer
     * @param plugin plugin
     */
    public static void remove( IndexerAction action, Plugin plugin )
    {
        _dao.delete( action, plugin );
    }

    /**
     * Met à jour une action
     * @param action action
     * @param plugin plugin
     */
    public static void update( IndexerAction action, Plugin plugin )
    {
        _dao.store( action, plugin );
    }

    /**
     * Retourne la liste des actions pour un indexer, selon le type d'action voulu:
     * IndexAction.ACTION_CREATE,IndexAction.ACTION_DELETE, IndexAction.ACTION_MODIFY
     * @param nIdIndexer l'identifiant de l'indexer
     * @param nAction le type d'action voulu
     * @param bDelete indique si on supprime les actions chargées ou non
     * @param bArchive indique si l'action porte sur l'index archive ou non
     * @param plugin plugin
     * @return la liste des actions
     */
    public static List<IndexerAction> findActionsByIndexer( int nIdIndexer, int nAction, boolean bDelete,
        boolean bArchive, Plugin plugin )
    {
        return _dao.selectIndexerActions( nIdIndexer, nAction, bDelete, bArchive, plugin );
    }

    /**
     * Retourne l'action d'indexation voulue sur le fichier, si elle existe
     * @param nIdIndexer l'indexer sélectionné
     * @param nAction l'action sur le fichier
     * @param object l'objet concerné
     * @param plugin plugin
     * @return une action d'indexation si elle existe, null sinon
     */
    public static IndexerAction findActionOnObject( int nIdIndexer, int nAction, Object object, Plugin plugin )
    {
        return _dao.selectActionOnObject( nIdIndexer, nAction, object, plugin );
    }

    /**
     * Retire toutes les actions d'indexation pour cet indexer
     * @param indexer l'indexer à supprimer
     * @param plugin le plugin
     */
    public static void removeForIndexer( Indexer indexer, Plugin plugin )
    {
        _dao.deleteForIndexer( indexer, plugin );
    }
}
