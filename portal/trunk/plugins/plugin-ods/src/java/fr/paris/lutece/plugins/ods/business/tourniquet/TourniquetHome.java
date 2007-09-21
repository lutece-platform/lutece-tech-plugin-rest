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
package fr.paris.lutece.plugins.ods.business.tourniquet;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 * TourniquetHome
 */
public class TourniquetHome
{
    private static ITourniquetDAO _dao = (ITourniquetDAO) SpringContextService.getPluginBean( "ods", "tourniquetDAO" );

    /**
     * Retourne l'entr�e du tourniquet identifi�e par nKey
     * @param nKey l'identifiant du tourniquet
     * @param plugin le Plugin actif
     * @return une entr�e du tourniquet
     */
    public static Tourniquet findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Retourne l'�tat actuel du tourniquet
     * @param plugin le Plugin actif
     * @return l'�tat du tourniquet dans la base de donn�es
     */
    public static List<Tourniquet> listTourniquet( Plugin plugin )
    {
        List<Tourniquet> listTourniquet = _dao.loadTourniquet( plugin );

        return listTourniquet;
    }

    /**
     * Met � jour la table � partir des comissions
     * @param listCom la liste des comissions
     * @param plugin le Plugin actif
     */
    public static void initTourniquetFromCommission( List<Commission> listCom, Plugin plugin )
    {
        _dao.flushTourniquet( plugin );

        int nOrdre = 1;

        for ( Commission commission : listCom )
        {
            Tourniquet tourniquet = new Tourniquet(  );
            tourniquet.setCommission( commission );
            tourniquet.setNumeroOrdre( nOrdre );
            _dao.insert( tourniquet, plugin );
            nOrdre++;
        }
    }

    /**
     * Met � jour la table avec les donn�es du tourniquet
     * @param tourniquet l'entr�e du tourniquet � modifier
     * @param plugin le Plugin actif
     */
    public static void update( Tourniquet tourniquet, Plugin plugin )
    {
        _dao.store( tourniquet, plugin );
    }

    /**
     * Supprime toutes les entr�es du tourniquet
     * @param plugin le Plugin actif
     */
    public static void removeAll( Plugin plugin )
    {
        _dao.flushTourniquet( plugin );
    }

    /**
     * Enregistre l'�tat actuel du tourniquet � partir de la liste des entr�es
     * @param tourniquets la liste de entr�es du tourniquet
     * @param plugin le Plugin actif
     */
    public static void initTourniquet( List<Tourniquet> tourniquets, Plugin plugin )
    {
        _dao.flushTourniquet( plugin );

        int nOrdre = 1;

        for ( Tourniquet tourniquet : tourniquets )
        {
            tourniquet.setNumeroOrdre( nOrdre );
            _dao.insert( tourniquet, plugin );
            nOrdre++;
        }
    }
}
