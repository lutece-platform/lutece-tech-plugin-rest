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
package fr.paris.lutece.plugins.ods.business.groupepolitique;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/** */
public class GroupePolitiqueHome
{
    private static IGroupePolitiqueDAO _dao = (IGroupePolitiqueDAO) SpringContextService.getPluginBean( "ods",
            "groupePolitiqueDAO" );

    /**
    * Crée un nouveau groupe politique  à partir de l’objet groupe passé en paramètre
    * @param groupe le groupe politique à insérer
    * @param plugin plugin
    */
    public static void create( GroupePolitique groupe, Plugin plugin )
    {
        _dao.insert( groupe, plugin );
    }

    /**
    * Modifie  le groupe politique correspondant à l’objet groupe passé en paramètre
    * @param groupe le groupe politique à modifier
    * @param plugin plugin
    */
    public static void update( GroupePolitique groupe, Plugin plugin )
    {
        _dao.store( groupe, plugin );
    }

    /**
    * Supprime le groupe politique  correspondant à  l’objet groupe passé en paramètre
    * @param groupe le groupe politique à supprimer
    * @param plugin plugin
    * @throws AppException si le groupe est référencé par un objet tiers
    */
    public static void remove( GroupePolitique groupe, Plugin plugin )
        throws AppException
    {
        _dao.delete( groupe, plugin );
    }

    /**
    * renvoie le groupe politique  ayant comme identifiant nKey
    * @param nKey idenfiant du groupe politique à supprimer
    * @param plugin plugin
    * @return objet GroupePolitique
    */
    public static GroupePolitique findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
    * renvoie la liste des groupes politiques
    * @param plugin plugin
    * @return liste d'objets GroupePolitique
    */
    public static List<GroupePolitique> findGroupePolitiqueList( Plugin plugin )
    {
        return _dao.groupeList( plugin );
    }

    /**
     * renvoie le Groupe politique de l'élu dont l'identifiant à été passé en paramètre
     * @param nKey identifiant de l'élu dont on recherche le groupe politique
     * @param plugin plugin
     * @return objet GroupePolitique
     */
    public static GroupePolitique findByIdElu( int nKey, Plugin plugin )
    {
        return _dao.selectByIdElu( nKey, plugin );
    }

    /**
    * Retourne la liste des groupes politiques dont le statut est actif.
    * @param plugin plugin
    * @return la liste des groupes actifs
    */
    public static List<GroupePolitique> findGroupesActifs( Plugin plugin )
    {
        return _dao.selectGroupesActifs( plugin );
    }
}
