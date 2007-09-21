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
package fr.paris.lutece.plugins.ods.business.direction;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * Cette classe permet de gérer des objets Direction.
 */
public class DirectionHome
{
    private static IDirectionDAO _dao = (IDirectionDAO) SpringContextService.getPluginBean( "ods", "directionDAO" );

    /**
     * Crée une nouvelle direction à partir de l'objet Direction passé en argument
     * @param direction la direction à créer
     * @param plugin le plugin actif
     */
    public static void create( Direction direction, Plugin plugin )
    {
        _dao.insert( direction, plugin );
    }

    /**
     * Retourne la direction ayant pour identifiant nKey
     * @param nKey l'identifiant de la direction voulue
     * @param plugin le plugin actif
     * @return la direction
     */
    public static Direction findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Supprime une direction de la liste des directions
     * @param direction la direction à supprimer
     * @param plugin le plugin actif
     * @throws AppException si la direction est référencée par un PDD
     */
    public static void remove( Direction direction, Plugin plugin )
        throws AppException
    {
        _dao.delete( direction, plugin );
    }

    /**
     * Met à jour les informations d"'une direction à partir de lobjet passé en paramètre
     * @param direction la direction et ses nouvelles valeurs
     * @param plugin le plugin actif
     */
    public static void update( Direction direction, Plugin plugin )
    {
        _dao.store( direction, plugin );
    }

    /**
     * Retourne la liste des directions
     * @param plugin le plugin actif
     * @return la liste des directions
     */
    public static List<Direction> findDirectionList( Plugin plugin )
    {
        return _dao.loadListDirection( plugin );
    }

    /**
     * Indique si le code passé en argument existe déjà dans la table ods_direction;
     * Exclut de la recherche la direction associée à exceptIdDirection
     * @param exceptDirection direction à exclure de la liste (aucune exclusion si =null)
     * @param strCode le code recherché
     * @param plugin le plugin actif
     * @return true si le code existe déjà dans la table ods_direction; false sinon
     */
    public static boolean containsCode( Direction exceptDirection, String strCode, Plugin plugin )
    {
        return _dao.listCodes( exceptDirection, plugin ).contains( strCode );
    }

    /**
     * Retourne la liste des directions actives pour la prochaine séance
     * @param plugin
     * @return la liste des directions actives
     */
    public static List<Direction> findAllDirectionsActives( Plugin plugin )
    {
        return _dao.loadListDirectionsActives( plugin );
    }
}
