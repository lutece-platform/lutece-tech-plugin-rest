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
package fr.paris.lutece.plugins.ods.business.elu;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/** */
public class EluHome
{
    private static IEluDAO _dao = (IEluDAO) SpringContextService.getPluginBean( "ods", "eluDAO" );

    /**
     * Crée un nouvel élu  à partir de l’objet elu passé en paramètre
     * @param elu  l'élu  à insérer
     * @param plugin plugin
     */
    public static void create( Elu elu, Plugin plugin )
    {
        _dao.insert( elu, plugin );
    }

    /**
     * Modifie  l'élu correspondant à l’objet elu passé en paramètre
     * @param elu l'élu à modifier
     * @param plugin plugin
     */
    public static void update( Elu elu, Plugin plugin )
    {
        _dao.store( elu, plugin );
    }

    /**
     * Supprime l'élu correspondant à  l’objet elu passé en paramètre
     * @param elu l'élu à supprimer
     * @param plugin plugin
     * @throws AppException si l'élu est référencé par un objet tiers
     */
    public static void remove( Elu elu, Plugin plugin )
        throws AppException
    {
        _dao.delete( elu, plugin );
    }

    /**
     * Retourne l'élu  ayant comme identifiant nKey
     * @param nKey l'identifiant de l'élu
     * @param plugin plugin
     * @return l'élu  ayant comme identifiant nKey
     */
    public static Elu findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Retourne la liste des élus
     * @param plugin plugin
     * @return liste d'objets Elu
     */
    public static List<Elu> findEluList( Plugin plugin )
    {
        return _dao.selectEluList( plugin );
    }

    /**
     * Retourne la liste des élus ayant un groupe politique dont l'identifiant est égal à nKey
     * @param nKey  identifiant du groupe politique
     * @param plugin plugin
     * @return la liste des élus ayant un groupe politique dont l'identifiant est égal à nKey
     */
    public static List<Elu> findEluListbyIdGroupe( int nKey, Plugin plugin )
    {
        return _dao.selectEluListbyIdGroupe( nKey, plugin );
    }

    /**
     * Retourne la liste des élus dont le statut est actif.
     * @param plugin plugin
     * @return la liste de élus actifs
     */
    public static List<Elu> findElusActifs( Plugin plugin )
    {
        return _dao.selectEluActifs( plugin );
    }

    /**
     * Retourne la liste des élus remplaçants d'un autre élu
     * @param plugin le plugin
     * @return la liste des élus remplaçants d'un autre élu
     */
    public static List<Elu> findEluRemplacantsActifs( Plugin plugin )
    {
        return _dao.selectEluRemplacantsActifs( plugin );
    }

    /**
     * Retourne la liste des élus rapporteurs dans une commission
     * @param plugin le plugin
     * @return la liste des rapporteurs
     */
    public static List<Elu> findElusRapporteursActifs( Plugin plugin )
    {
        return _dao.selectElusRapporteursActifs( plugin );
    }

    /**
     * Retourne la liste de tous les rapporteurs
     * @param plugin le plugin
     * @return la liste de tous les rapporteurs
     */
    public static List<Elu> findAllRapporteurs( Plugin plugin )
    {
        return _dao.selectElusRapporteurs( plugin );
    }
}
