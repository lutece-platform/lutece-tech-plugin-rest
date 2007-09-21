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
package fr.paris.lutece.plugins.ods.service.search.indexer;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


/**
 * Interface IIndexerDAO
 */
public interface IIndexerDAO
{
    /**
     * Enregistre un nouvel indexer dans la base
     * @param indexer l'indexer à insérer
     * @param plugin le plugin
     */
    void insert( Indexer indexer, Plugin plugin );

    /**
     * Supprime un indexer de la base
     * @param indexer l'indexer à supprimer
     * @param plugin le plugin
     */
    void delete( Indexer indexer, Plugin plugin );

    /**
     * Modifie un indexer
     * @param indexer l'indexer modifié
     * @param plugin le plugin
     */
    void store( Indexer indexer, Plugin plugin );

    /**
     * Charge l'indexer identifié par nKey
     * @param nKey l'identifiant de l'indexer
     * @param plugin le plugin
     * @return l'indexer identifié par nKey
     */
    Indexer load( int nKey, Plugin plugin );

    /**
     * Retourne l'indexer dont le nom est strNomIndexer
     * @param strNomIndexer le nom de l'indexer recherché
     * @param plugin le plugin
     * @return l'indexer dont le nom est strNomIndexer
     */
    Indexer selectByNomIndexer( String strNomIndexer, Plugin plugin );

    /**
     * Retourne la liste des indexers
     * @param plugin le plugin
     * @return la liste des indexers
     */
    List<Indexer> selectAllIndexer( Plugin plugin );
}
