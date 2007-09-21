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
package fr.paris.lutece.plugins.ods.business.statut;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * StatutHome
 */
public class StatutHome
{
    private static IStatutDAO _dao = (IStatutDAO) SpringContextService.getPluginBean( "ods", "statutDAO" );

    /**
    * Crée un nouveau statut à partir de l’objet statut passé en paramètre
    * @param statut le statut à insérer
    * @param plugin Plugin
    */
    public static void create( Statut statut, Plugin plugin )
    {
        _dao.insert( statut, plugin );
    }

    /**
     * Modifie le statut correspondant à l’objet statut passé en paramètre
     * @param statut le statut à modifier
     * @param plugin Plugin
     */
    public static void update( Statut statut, Plugin plugin )
    {
        _dao.store( statut, plugin );
    }

    /**
     * Supprime le statut correspondant à l’objet statut passé en paramètre
     * @param statut le statut à supprimer
     * @param plugin Plugin
     * @throws AppException exception levé si une dependance existe
     */
    public static void remove( Statut statut, Plugin plugin )
        throws AppException
    {
        _dao.delete( statut, plugin );
    }

    /**
     * renvoie le statut ayant comme identifiant nKey
     * @param nKey idenfiant de le statut à supprimer
     * @param plugin Plugin
     * @return objet statut
     */
    public static Statut findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * renvoie la liste des statuts
     * @param plugin Plugin
     * @return liste d'objets statut
     */
    public static List<Statut> findStatutList( Plugin plugin )
    {
        return _dao.selectStatutList( plugin );
    }

    /**
     * renvoie la liste des statuts correspondant au(x) filtre(s) appliqué(s)
     * @param filter le filtre
     * @param plugin Plugin
     * @return liste d'objets statut
     */
    public static List<Statut> findStatutWithFilterList( StatutFilter filter, Plugin plugin )
    {
        return _dao.selectStatutListWithFilter( filter, plugin );
    }
}
