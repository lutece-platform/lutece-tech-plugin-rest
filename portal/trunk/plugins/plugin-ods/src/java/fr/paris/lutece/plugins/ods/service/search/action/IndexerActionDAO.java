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

import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.service.search.indexer.Indexer;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
*IndexerActionDAO
*/
public class IndexerActionDAO implements IIndexerActionDAO
{
    private static final String SQL_QUERY_FIND_ALL = "SELECT id_task, type_objet, id_objet FROM ods_index_task WHERE 1 ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ods_index_task WHERE 1 ";
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_task ) FROM ods_index_task ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_index_task(id_task, id_indexer, type_objet, id_objet, code_task, is_archive) VALUES(?,?,?,?,?,?)";
    private static final String SQL_QUERY_UPDATE = "UPDATE ods_index_task SET id_indexer=?, type_objet=?, id_objet=?, code_task=?, is_archive=? WHERE id_task=?";
    private static final String SQL_QUERY_CLAUSE_ID_ACTION = " AND id_task = ? ";
    private static final String SQL_QUERY_CLAUSE_INDEXER = "AND id_indexer = ? ";
    private static final String SQL_QUERY_CLAUSE_ACTION = "AND code_task=? ";
    private static final String SQL_QUERY_CLAUSE_OBJECT = "AND id_objet = ? ";
    private static final String SQL_QUERY_CLAUSE_ARCHIVE = "AND is_archive = ? ";

    /**
     * Créé une nouvelle action d'indexation pour tous les serveurs
     * @param action l'action à ajouter
     * @param plugin plugin
     */
    public void insert( IndexerAction action, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( 1, newPrimaryKey( plugin ) );
        daoUtil.setInt( 2, action.getIdIndexer(  ) );
        daoUtil.setInt( 3, action.getType(  ) );
        daoUtil.setInt( 4, action.getIdObjet(  ) );
        daoUtil.setInt( 5, action.getCodeTask(  ) );
        daoUtil.setBoolean( 6, action.isArchive(  ) );
        daoUtil.executeUpdate(  );

        daoUtil.free(  );
    }

    /**
     * Supprime une action d'indexation
     * @param action l'action à supprimer
     * @param plugin plugin
     */
    public void delete( IndexerAction action, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE + SQL_QUERY_CLAUSE_ID_ACTION, plugin );
        daoUtil.setInt( 1, action.getIdAction(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Met à jour une action d'indexation
     * @param action l'action à modifier
     * @param plugin plugin
     */
    public void store( IndexerAction action, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setInt( 1, action.getIdIndexer(  ) );
        daoUtil.setInt( 2, action.getType(  ) );
        daoUtil.setInt( 3, action.getIdObjet(  ) );
        daoUtil.setInt( 4, action.getCodeTask(  ) );
        daoUtil.setBoolean( 5, action.isArchive(  ) );
        daoUtil.setInt( 6, action.getIdAction(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne la liste des actions pour un indexer, selon le type d'action voulu:
     * IndexAction.ACTION_CREATE,IndexAction.ACTION_DELETE, IndexAction.ACTION_MODIFY
     * @param nIdIndexer l'identifiant de l'indexer
     * @param nAction le type d'action voulu
     * @param bDelete indique si on supprime les actions chargées ou non
     * @param bArchive true si la recherche concerne les archives
     * @param plugin plugin
     * @return la liste des actions
     */
    public List<IndexerAction> selectIndexerActions( int nIdIndexer, int nAction, boolean bDelete, boolean bArchive,
        Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL + SQL_QUERY_CLAUSE_INDEXER + SQL_QUERY_CLAUSE_ACTION +
                SQL_QUERY_CLAUSE_ARCHIVE, plugin );
        daoUtil.setInt( 1, nIdIndexer );
        daoUtil.setInt( 2, nAction );
        daoUtil.setBoolean( 3, bArchive );
        daoUtil.executeQuery(  );

        List<IndexerAction> listActions = new ArrayList<IndexerAction>(  );

        while ( daoUtil.next(  ) )
        {
            IndexerAction action = new IndexerAction(  );
            action.setIdAction( daoUtil.getInt( 1 ) );
            action.setType( daoUtil.getInt( 2 ) );
            action.setIdObjet( daoUtil.getInt( 3 ) );
            action.setCodeTask( nAction );
            listActions.add( action );

            if ( bDelete )
            {
                delete( action, plugin );
            }
        }

        daoUtil.free(  );

        return listActions;
    }

    /**
     * Retourne l'action d'indexation voulue sur le fichier, si elle existe
     * @param nIdIndexer l'indexer sélectionné
     * @param nAction l'action sur le fichier
     * @param object l'objet recherché
     * @param plugin plugin
     * @return une action d'indexation si elle existe, <b>null</b> sinon
     */
    public IndexerAction selectActionOnObject( int nIdIndexer, int nAction, Object object, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL + SQL_QUERY_CLAUSE_INDEXER + SQL_QUERY_CLAUSE_ACTION +
                SQL_QUERY_CLAUSE_OBJECT, plugin );
        daoUtil.setInt( 1, nIdIndexer );
        daoUtil.setInt( 2, nAction );

        int nIdObjet = -1;

        if ( object instanceof Fichier )
        {
            nIdObjet = ( (Fichier) object ).getId(  );
        }
        else if ( object instanceof PDD )
        {
            nIdObjet = ( (PDD) object ).getIdPdd(  );
        }
        else if ( object instanceof VoeuAmendement )
        {
            nIdObjet = ( (VoeuAmendement) object ).getIdVoeuAmendement(  );
        }

        daoUtil.setInt( 3, nIdObjet );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return null;
        }

        IndexerAction action = new IndexerAction(  );
        action.setIdAction( daoUtil.getInt( 1 ) );
        action.setIdIndexer( nIdIndexer );
        action.setIdObjet( nIdObjet );
        action.setCodeTask( nAction );
        daoUtil.free(  );

        return action;
    }

    /**
     * Retourne une nouvelle clé primaire unique sur la table ods_index_actions
     * @param plugin plugin
     * @return un identifiant unique
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
     * Supprime toutes les actions d'indexation pour cet indexer;
     * @param indexer l'indexer
     * @param plugin le plugin
     */
    public void deleteForIndexer( Indexer indexer, Plugin plugin )
    {
        if ( indexer != null )
        {
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE + SQL_QUERY_CLAUSE_INDEXER, plugin );
            daoUtil.setInt( 1, indexer.getIdIndexer(  ) );
            daoUtil.executeUpdate(  );
            daoUtil.free(  );
        }
    }
}
