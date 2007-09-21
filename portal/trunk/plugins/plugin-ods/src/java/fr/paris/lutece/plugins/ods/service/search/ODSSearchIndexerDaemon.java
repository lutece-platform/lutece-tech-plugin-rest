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
package fr.paris.lutece.plugins.ods.service.search;

import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.service.search.action.IndexerActionHome;
import fr.paris.lutece.plugins.ods.service.search.indexer.Indexer;
import fr.paris.lutece.plugins.ods.service.search.indexer.IndexerHome;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.ArrayList;
import java.util.List;


/**
 * Daemon d'indexation: lance à intervalle régulier l'indexation incrémentale.
 * A la première éxécution le daemon crée l'index sur le serveur; cet index sera unique par machine serveur
 * même en cas de plusieurs serveurs Tomcat sur une seule et même machine.
 *
 */
public class ODSSearchIndexerDaemon extends Daemon
{
    private static final String PROPERTY_INDEXER_NAME = "odssearchindexer.name";
    private static final String PLUGIN_ODS_NAME = "ods";
    private Seance _seance;

    /**
     * Lance l'indexation à intervalle régulier
     */
    public void run(  )
    {
        Plugin plugin = PluginService.getPlugin( PLUGIN_ODS_NAME );
        List<Seance> listSeancesPassees = SeanceHome.findOldSeance( plugin );
        List<Seance> listSeancesFutures = SeanceHome.findSeanceList( plugin );
        List<Seance> listAllSeances = new ArrayList<Seance>(  );
        Seance prochaineSeance = SeanceHome.getProchaineSeance( plugin );
        String strIndexerName = AppPropertiesService.getProperty( PROPERTY_INDEXER_NAME );

        if ( listSeancesPassees != null )
        {
            listAllSeances.addAll( listSeancesPassees );
        }

        if ( listSeancesFutures != null )
        {
            listAllSeances.addAll( listSeancesFutures );
        }

        if ( ( listAllSeances.size(  ) != 0 ) && ( strIndexerName != null ) &&
                ( strIndexerName != OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            Indexer indexer = IndexerHome.findByName( strIndexerName, plugin );

            if ( ( ( _seance == null ) && ( prochaineSeance != null ) ) || ( indexer == null ) ||
                    indexer.isIndexationComplete(  ) )
            {
                IndexerActionHome.removeForIndexer( IndexerHome.findByName( strIndexerName, plugin ), plugin );

                // Dans ce cas il faut initialiser l'index
                if ( indexer == null )
                {
                    // inscription dans la liste des indexer ecrivant sur l'index + init
                    indexer = new Indexer(  );
                    indexer.setNomIndexer( strIndexerName );
                    indexer.setIndexationComplete( false );
                    IndexerHome.create( indexer, plugin );
                }

                indexer.setIndexationComplete( false );
                IndexerHome.update( indexer, plugin );

                if ( prochaineSeance != null )
                {
                    ODSIndexationServiceProchaineSeance.getInstance(  ).initIndex(  );
                }

                if ( ( listSeancesPassees != null ) )
                {
                    ODSIndexationServiceArchive.getInstance(  ).initIndex(  );
                }

                _seance = prochaineSeance;
            }
            else
            {
                if ( prochaineSeance != null )
                {
                    if ( _seance.getIdSeance(  ) != prochaineSeance.getIdSeance(  ) )
                    {
                        changeSeance( prochaineSeance, strIndexerName, plugin );
                    }

                    try
                    {
                        ODSIndexationServiceProchaineSeance.getInstance(  ).processIndexing(  );
                    }
                    catch ( AppException ae )
                    {
                        AppLogService.error( ae.getMessage(  ) );
                    }
                }

                if ( ( listSeancesPassees != null ) )
                {
                    try
                    {
                        ODSIndexationServiceArchive.getInstance(  ).processIndexing(  );
                    }
                    catch ( AppException ae )
                    {
                        AppLogService.error( ae.getMessage(  ) );
                    }
                }
            }
        }
    }

    /**
     * Fonction qui ajoute les documents de la séance passée à l'index des archives,
     * et réinitialise l'index prochaine séance
     * @param prochaineSeance la prochaine séance
     * @param strIndexerName le nom de l'indexer
     * @param plugin le plugin
     */
    private void changeSeance( Seance prochaineSeance, String strIndexerName, Plugin plugin )
    {
        IndexerActionHome.removeForIndexer( IndexerHome.findByName( strIndexerName, plugin ), plugin );

        try
        {
            ODSIndexationServiceProchaineSeance.getInstance(  ).initIndex(  );
        }
        catch ( AppException ae )
        {
            AppLogService.error( ae.getMessage(  ) );
        }

        try
        {
            ODSIndexationServiceArchive.getInstance(  ).changeSeance( _seance );
        }
        catch ( AppException ae )
        {
            AppLogService.error( ae.getMessage(  ) );
        }

        _seance = prochaineSeance;
    }
}
