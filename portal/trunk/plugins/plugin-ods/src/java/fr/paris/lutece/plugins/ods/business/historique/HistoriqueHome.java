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
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 *
 * Proxy du HistoriqueDAO
 */
public class HistoriqueHome
{
    private static IHistoriqueDAO _dao = (IHistoriqueDAO) SpringContextService.getPluginBean( "ods", "historiqueDAO" );

    /**
         * Insere l'objet historique en base
         * @param historique Historique
         * @param plugin Plugin
         */
    public static void create( Historique historique, Plugin plugin )
    {
        _dao.insert( historique, plugin );
    }

    /**
         * charge l'objet historique
         * @param nKey int
         * @param plugin Plugin
         * @return Historique l'objet historique
         */
    public static Historique findByPrimaryKey( int nKey, Plugin plugin )
    {
        return null;
    }

    /**
     * Modifie le plugin
     * @param historique Historique
     * @param plugin Plugin
     */
    public static void update( Historique historique, Plugin plugin )
    {
    }

    /**
     * Retourne une liste d'historiques concernant le fichier passé en paramètre
     *
     * @param fichier Fichier
     * @param plugin Plugin
     * @return List<Historique> liste d'historiques
     */
    public static List<Historique> getHistoriqueList( Fichier fichier, Plugin plugin )
    {
        return _dao.loadByFichier( fichier, plugin );
    }

    /**
     * Retourne une liste d'historiques concernant le pdd passé en paramètre
     *
     * @param pdd PDD
     * @param plugin Plugin
     * @return List<Historique> liste d'historiques
     */
    public static List<Historique> getHistoriqueList( PDD pdd, Plugin plugin )
    {
        return _dao.loadByPDD( pdd, plugin );
    }

    /**
     * Retourne une liste d'historiques concernant le voeuAmendement passé en paramètre
     *
     * @param voeuAmendement VoeuAmendement
     * @param plugin Plugin
     * @return List<Historique> liste d'historiques
     */
    public static List<Historique> getHistoriqueList( VoeuAmendement voeuAmendement, Plugin plugin )
    {
        return _dao.loadByVoeuAmendement( voeuAmendement, plugin );
    }

    /**
     * Supprime tous les historiques concernant le fichier passé en paramètre
     *
     * @param fichier Fichier
     * @param plugin Plugin
     */
    public static void remove( Fichier fichier, Plugin plugin )
    {
        _dao.delete( fichier, plugin );
    }

    /**
     * Supprime tous les historiques concernant le pdd passé en paramètre
     *
     * @param pdd PDD
     * @param plugin Plugin
     */
    public static void remove( PDD pdd, Plugin plugin )
    {
        _dao.delete( pdd, plugin );
    }

    /**
     * Supprime tous les historiques concernant le voeuAmendement passé en paramètre
     *
     * @param voeuAmendement VoeuAmendement
     * @param plugin Plugin
     */
    public static void remove( VoeuAmendement voeuAmendement, Plugin plugin )
    {
        _dao.delete( voeuAmendement, plugin );
    }
}
