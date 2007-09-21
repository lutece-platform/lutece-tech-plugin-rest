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
package fr.paris.lutece.plugins.ods.business.historique;

import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * HistoriqueDAO est responsable de la communication avec la BDD
 *
 */
public class HistoriqueDAO implements fr.paris.lutece.plugins.ods.business.historique.IHistoriqueDAO
{
    private static final String SQL_QUERY_HISTORIQUES_LIST_BY_FICHIER = "SELECT * FROM ods_historique where id_document = ? ";
    private static final String SQL_QUERY_DELETE_BY_FICHIER = " DELETE FROM ods_historique WHERE id_document = ?  ";
    private static final String SQL_QUERY_DELETE_BY_PDD = " DELETE FROM ods_historique WHERE id_pdd = ?  ";
    private static final String SQL_QUERY_DELETE_BY_VA = " DELETE FROM ods_historique WHERE id_va = ?  ";
    private static final String SQL_QUERY_HISTORIQUES_LIST_BY_PDD = "SELECT * FROM ods_historique where id_pdd = ? ";
    private static final String SQL_QUERY_HISTORIQUES_LIST_BY_VA = "SELECT * FROM ods_historique where id_va = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO ods_historique (id_historique, version, date_publication, id_document, id_pdd, id_va) VALUES ( ?, ?, ?, ?, ?, ?) ";
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_historique ) FROM ods_historique ";

    /**
         * Supprime l'objet historique en base
         * @param historique Historique
         * @param plugin Plugin
         */
    public void delete( Historique historique, Plugin plugin )
    {
    }

    /**
         * Insere l'objet historique en base
         * @param historique Historique
         * @param plugin Plugin
         */
    public void insert( Historique historique, Plugin plugin )
    {
        int newPrimaryKey = newPrimaryKey( plugin );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        historique.setId( newPrimaryKey );
        daoUtil.setInt( 1, historique.getId(  ) );
        daoUtil.setInt( 2, historique.getVersion(  ) );
        daoUtil.setTimestamp( 3, historique.getDatePublication(  ) );

        if ( historique.getIdDocument(  ) == Historique.NULL )
        {
            daoUtil.setIntNull( 4 );
        }
        else
        {
            daoUtil.setInt( 4, historique.getIdDocument(  ) );
        }

        if ( historique.getIdPDD(  ) == Historique.NULL )
        {
            daoUtil.setIntNull( 5 );
        }
        else
        {
            daoUtil.setInt( 5, historique.getIdPDD(  ) );
        }

        if ( historique.getIdVa(  ) == Historique.NULL )
        {
            daoUtil.setIntNull( 6 );
        }
        else
        {
            daoUtil.setInt( 6, historique.getIdVa(  ) );
        }

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
         * charge l'objet historique
         * @param nKey int
         * @param plugin Plugin
         * @return Historique l'objet historique
         */
    public Historique load( int nKey, Plugin plugin )
    {
        return null;
    }

    /**
     * Modifie le plugin
     * @param historique Historique
     * @param plugin Plugin
     */
    public void store( Historique historique, Plugin plugin )
    {
    }

    /**
     * Retourne une liste d'historiques concernant le fichier passé en paramètre
     *
     * @param fichier Fichier
     * @param plugin Plugin
     * @return List<Historique> liste d'historiques
     */
    public List<Historique> loadByFichier( Fichier fichier, Plugin plugin )
    {
        List<Historique> historiques = new ArrayList<Historique>(  );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_HISTORIQUES_LIST_BY_FICHIER, plugin );
        daoUtil.setInt( 1, fichier.getId(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Historique historique = new Historique(  );
            historique.setId( daoUtil.getInt( "id_historique" ) );
            historique.setVersion( daoUtil.getInt( "version" ) );
            historique.setDatePublication( daoUtil.getTimestamp( "date_publication" ) );
            historiques.add( historique );
        }

        daoUtil.free(  );

        return historiques;
    }

    /**
     * Retourne une liste d'historiques concernant le pdd passé en paramètre
     *
     * @param pdd PDD
     * @param plugin Plugin
     * @return List<Historique> liste d'historiques
     */
    public List<Historique> loadByPDD( PDD pdd, Plugin plugin )
    {
        List<Historique> historiques = new ArrayList<Historique>(  );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_HISTORIQUES_LIST_BY_PDD, plugin );
        daoUtil.setInt( 1, pdd.getIdPdd(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Historique historique = new Historique(  );
            historique.setId( daoUtil.getInt( "id_historique" ) );
            historique.setVersion( daoUtil.getInt( "version" ) );
            historique.setDatePublication( daoUtil.getTimestamp( "date_publication" ) );
            historiques.add( historique );
        }

        daoUtil.free(  );

        return historiques;
    }

    /**
     * Retourne une liste d'historiques concernant le voeuAmendement passé en paramètre
     *
     * @param voeuAmendement VoeuAmendement
     * @param plugin Plugin
     * @return List<Historique> liste d'historiques
     */
    public List<Historique> loadByVoeuAmendement( VoeuAmendement voeuAmendement, Plugin plugin )
    {
        List<Historique> historiques = new ArrayList<Historique>(  );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_HISTORIQUES_LIST_BY_VA, plugin );
        daoUtil.setInt( 1, voeuAmendement.getIdVoeuAmendement(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Historique historique = new Historique(  );
            historique.setId( daoUtil.getInt( "id_historique" ) );
            historique.setVersion( daoUtil.getInt( "version" ) );
            historique.setDatePublication( daoUtil.getTimestamp( "date_publication" ) );
            historiques.add( historique );
        }

        daoUtil.free(  );

        return historiques;
    }

    /**
     * Génère un nouvel identifiant
     * @param plugin Plugin
     * @return int Retourne l’identifiant généré
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
     * Supprime tous les historiques concernant le fichier passé en paramètre
     *
     * @param fichier Fichier
     * @param plugin Plugin
     */
    public void delete( Fichier fichier, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_FICHIER, plugin );
        daoUtil.setInt( 1, fichier.getId(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime tous les historiques concernant le pdd passé en paramètre
     *
     * @param pdd PDD
     * @param plugin Plugin
     */
    public void delete( PDD pdd, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_PDD, plugin );
        daoUtil.setInt( 1, pdd.getIdPdd(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime tous les historiques concernant le voeuAmendement passé en paramètre
     *
     * @param voeuAmendement VoeuAmendement
     * @param plugin Plugin
     */
    public void delete( VoeuAmendement voeuAmendement, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_VA, plugin );
        daoUtil.setInt( 1, voeuAmendement.getIdVoeuAmendement(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
