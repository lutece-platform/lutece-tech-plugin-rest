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
package fr.paris.lutece.plugins.ods.business.modeleordredujour;

import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourFilter;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 * ModeleOrdreDuJourHome
 */
public class ModeleOrdreDuJourHome
{
    private static IModeleOrdreDuJourDAO _dao = (IModeleOrdreDuJourDAO) SpringContextService.getPluginBean( "ods",
            "modeleOrdreDuJourDAO" );

    /**
     * Crée un modele d'ordre du jour dans la base de données
     * @param modeleOrdreDuJour le modele d'ordre du jour
     * @param plugin le plugin ods
     * @return l'identifiant du modele créé
     */
    public static int create( ModeleOrdreDuJour modeleOrdreDuJour, Plugin plugin )
    {
        return _dao.insert( modeleOrdreDuJour, plugin );
    }

    /**
     * modifie le modele d'ordre du jour dans la base de données
     * @param modeleOrdreDuJour le modele d'ordre du jour
     * @param plugin le plugin ods
     */
    public static void update( ModeleOrdreDuJour modeleOrdreDuJour, Plugin plugin )
    {
        _dao.store( modeleOrdreDuJour, plugin );
    }

    /**
     * supprime le modele d'ordre du jour dans la base de données
     * @param modeleOrdreDuJour le modele d'ordre du jour
     * @param plugin le plugin ods
     */
    public static void remove( ModeleOrdreDuJour modeleOrdreDuJour, Plugin plugin )
    {
        _dao.delete( modeleOrdreDuJour, plugin );
    }

    /**
     * renvoie le modele d'ordre du jour ayant l'identifiant donné en parametre
     * @param nKey l'id du modele d'ordre du jour
     * @param plugin le plugin ods
     * @return le modele d'ordre du jour
     */
    public static ModeleOrdreDuJour findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * renvoie la liste des modeles d'ordre du jour
     * @param plugin le plugin ods
     * @return la liste des modeles d'ordre du jour
     */
    public static List<ModeleOrdreDuJour> findModeleOrdreDuJourList( Plugin plugin )
    {
        return _dao.selectListModeleOrdreDuJourList( plugin );
    }

    /**
     * verifie si le modele d'ordre du jour existe deja dans la base de données
     * @param nIdType identifiant du type
     * @param nIdFormationConseil identifiant de la formation de conseil
     * @param nIdCommission identifiant de la commission
     * @param nIdModele identifiant du modele
     * @param plugin plugin ods
     * @return TRUE si le modele d'ordre du jour existe deja dans la base de données, FALSE sinon
     */
    public static boolean isAlreadyExist( int nIdType, int nIdFormationConseil, int nIdCommission, int nIdModele,
        Plugin plugin )
    {
        return _dao.isAlreadyExist( nIdType, nIdFormationConseil, nIdCommission, nIdModele, plugin );
    }

    /**
     * renvoie le modele d'ordre du jour correspondant au criteres du filtre donné en parametre
     * @param filter le filtre
     * @param plugin plugin ods
     * @return le modele d'ordre du jour
     */
    public static ModeleOrdreDuJour findByFilter( OrdreDuJourFilter filter, Plugin plugin )
    {
        return _dao.load( filter, plugin );
    }
}
