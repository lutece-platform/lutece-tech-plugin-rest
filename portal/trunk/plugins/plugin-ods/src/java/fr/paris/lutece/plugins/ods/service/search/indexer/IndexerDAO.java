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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * IndexerDAO : inteface pour gérer les indexers
 */
public class IndexerDAO implements IIndexerDAO
{
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_front_indexer(id_indexer, nom_indexer, init_index) VALUES(?,?,?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ods_front_indexer WHERE id_indexer = ?";
    private static final String SQL_QUERY_UPDATE = "UPDATE ods_front_indexer SET nom_indexer = ?, init_index = ? WHERE id_indexer = ?";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT nom_indexer, init_index FROM ods_front_indexer WHERE id_indexer = ?";
    private static final String SQL_QUERY_FIND_BY_NAME = "SELECT id_indexer, init_index FROM ods_front_indexer WHERE nom_indexer = ?";
    private static final String SQL_QUERY_FIND_ALL = "SELECT id_indexer, nom_indexer, init_index FROM ods_front_indexer ";
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_indexer ) FROM ods_front_indexer ";

    /**
     * Enregistre un nouvel indexer dans la base
     * @param indexer l'indexer à insérer
     * @param plugin le plugin
     */
    public void insert( Indexer indexer, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( 1, newPrimaryKey( plugin ) );
        daoUtil.setString( 2, indexer.getNomIndexer(  ) );
        daoUtil.setBoolean( 3, indexer.isIndexationComplete(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime un indexer de la base
     * @param indexer l'indexer à supprimer
     * @param plugin le plugin
     */
    public void delete( Indexer indexer, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, indexer.getIdIndexer(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Modifie un indexer
     * @param indexer l'indexer modifié
     * @param plugin le plugin
     */
    public void store( Indexer indexer, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setString( 1, indexer.getNomIndexer(  ) );
        daoUtil.setBoolean( 2, indexer.isIndexationComplete(  ) );
        daoUtil.setInt( 3, indexer.getIdIndexer(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Charge l'indexer identifié par nKey
     * @param nKey l'identifiant de l'indexer
     * @param plugin le plugin
     * @return l'indexer identifié par nKey
     */
    public Indexer load( int nKey, Plugin plugin )
    {
        Indexer indexer = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            indexer = new Indexer(  );
            indexer.setIdIndexer( nKey );
            indexer.setNomIndexer( daoUtil.getString( 1 ) );
            indexer.setIndexationComplete( daoUtil.getBoolean( 2 ) );
        }

        daoUtil.free(  );

        return indexer;
    }

    /**
     * Retourne l'indexer dont le nom est strNomIndexer
     * @param strNomIndexer le nom de l'indexer recherché
     * @param plugin le plugin
     * @return l'indexer dont le nom est strNomIndexer
     */
    public Indexer selectByNomIndexer( String strNomIndexer, Plugin plugin )
    {
        Indexer indexer = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_NAME, plugin );
        daoUtil.setString( 1, strNomIndexer );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            indexer = new Indexer(  );
            indexer.setIdIndexer( daoUtil.getInt( 1 ) );
            indexer.setNomIndexer( strNomIndexer );
            indexer.setIndexationComplete( daoUtil.getBoolean( 2 ) );
        }

        daoUtil.free(  );

        return indexer;
    }

    /**
     * Retourne la liste des indexers
     * @param plugin le plugin
     * @return la liste des indexers
     */
    public List<Indexer> selectAllIndexer( Plugin plugin )
    {
        List<Indexer> listIndexers = new ArrayList<Indexer>(  );
        DAOUtil daoIndexers = new DAOUtil( SQL_QUERY_FIND_ALL, plugin );
        daoIndexers.executeQuery(  );

        Indexer indexer = null;

        while ( daoIndexers.next(  ) )
        {
            indexer = new Indexer(  );
            indexer.setIdIndexer( daoIndexers.getInt( 1 ) );
            indexer.setNomIndexer( daoIndexers.getString( 2 ) );
            indexer.setIndexationComplete( daoIndexers.getBoolean( 3 ) );
            listIndexers.add( indexer );
        }

        daoIndexers.free(  );

        return listIndexers;
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
}
