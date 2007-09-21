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
package fr.paris.lutece.plugins.ods.business.naturedossier;

import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * Cette classe permet de manipuler des objets NatureDesDossiers.
 */
public class NatureDesDossiersHome
{
    private static INatureDesDossiersDAO _dao = (INatureDesDossiersDAO) SpringContextService.getPluginBean( "ods",
            "natureDesDossiersDAO" );

    /**
     * Crée une nouvelle nature à partir de l'objet NatureDesDossiers passé en argument
     * @param nature la nature à créer
     * @param plugin le plugin
     */
    public static void create( NatureDesDossiers nature, Plugin plugin )
    {
        _dao.insert( nature, plugin );
    }

    /**
     * Retourne la nature de dossier ayant pour identifiant nKey
     * @param nKey l'identifiant de la nature
     * @param plugin le plugin
     * @return la nature de dossier
     */
    public static NatureDesDossiers findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Supprime la nature de dossier passée en argument
     * @param nature la nature à supprimer
     * @param plugin le plugin
     * @throws AppException si la nature de dossier contient des entrées dans l'ordre du jour
     */
    public static void remove( NatureDesDossiers nature, Plugin plugin )
        throws AppException
    {
        _dao.delete( nature, plugin );
    }

    /**
     * Met à jour la nature de dossier passée en argument
     * @param nature la nature à modifier
     * @param plugin le plugin
     */
    public static void update( NatureDesDossiers nature, Plugin plugin )
    {
        _dao.store( nature, plugin );
    }

    /**
     * Retourne la liste des natures de dossier
     * @param plugin le plugin
     * @return la liste des natures de dossier
     */
    public static List<NatureDesDossiers> findNatureList( Plugin plugin )
    {
        return _dao.loadListeNatures( plugin );
    }

    /**
     * Retourne la liste des natures de dossier affectées à la séance identifiée par nKey
     * @param plugin le plugin
     * @return la liste des natures de dossier affectées à une séance
     */
    public static List<NatureDesDossiers> findBySeance( int nKey, Plugin plugin )
    {
        return _dao.loadListBySeance( nKey, plugin );
    }

    /**
     * Indique si le numero de nature passé en argument est déjà utilisé
     * dans la table ods_nature_dossier pour cette séance.
     * La recherhce exclut la nature identifiée par exceptNature.
     * @param exceptNature nature à exclure de la recherche (aucune exclusion si =null)
     * @param nNumNature numéro recherché
     * @param seance la seance
     * @param plugin le plugin
     * @return true si le numéro est déjà utilisé, false sinon
     */
    public static boolean containsNumero( NatureDesDossiers exceptNature, int nNumNature, Seance seance, Plugin plugin )
    {
        return _dao.listNumNature( exceptNature, seance, plugin ).contains( new Integer( nNumNature ) );
    }
}
