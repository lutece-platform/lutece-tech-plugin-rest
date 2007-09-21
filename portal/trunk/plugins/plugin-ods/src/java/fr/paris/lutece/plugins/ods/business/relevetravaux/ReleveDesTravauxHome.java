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
package fr.paris.lutece.plugins.ods.business.relevetravaux;

import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 * ReleveDesTravauxHome
 */
public class ReleveDesTravauxHome
{
    private static IReleveDesTravauxDAO _dao = (IReleveDesTravauxDAO) SpringContextService.getPluginBean( "ods",
            "releveDAO" );

    /**
     * Cr�e un relev� de travaux dans la base de donn�es.
     * @param releve le relev� de travaux
     * @param plugin le Plugin
     * @return l'identifiant du relev� de travaux cr��
     */
    public static int create( ReleveDesTravaux releve, Plugin plugin )
    {
        return _dao.insert( releve, plugin );
    }

    /**
     * Charge un relev� des travaux depuis sa cl� primaire.
     * @param nKey la cl� primaire
     * @param plugin le Plugin
     * @return un relev� des travaux
     */
    public static ReleveDesTravaux findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * supprime un relev� des travaux.
     * @param releve un relev� des travaux
     * @param plugin le Plugin
     */
    public static void remove( ReleveDesTravaux releve, Plugin plugin )
    {
        _dao.delete( releve, plugin );
    }

    /**
     * Mets � jour dans la base de donn�e d'un relev� des travaux.
     * @param releve un relev� des travaux
     * @param plugin le Plugin
     */
    public static void update( ReleveDesTravaux releve, Plugin plugin )
    {
        _dao.store( releve, plugin );
    }

    /**
     * Charge la liste des relev�s de travaux.
     * @param plugin le Plugin
     * @return une liste de relev� de travaux
     */
    public static List<ReleveDesTravaux> findReleveDesTravauxList( Plugin plugin )
    {
        return _dao.selectReleveDesTravauxList( plugin );
    }

    /**
     * Charge la liste des relev�s de travaux en ligne.
     * @param plugin le Plugin
     * @return une liste de relev�s de travaux.
     */
    public static List<ReleveDesTravaux> findReleveDesTravauxEnLigne( Plugin plugin )
    {
        return _dao.selectReleveDesTravauxEnLigne( plugin );
    }

    /**
     * Charge la liste des relev�s de travaux pour une s�ance donn�e.
     * @param seance la s�ance.
     * @param plugin le Plugin
     * @return une liste de relev�s de travaux.
     */
    public static List<ReleveDesTravaux> findReleveDesTravauxSeance( Seance seance, Plugin plugin )
    {
        return _dao.selectReleveDesTravauxSeance( seance, plugin );
    }

    /**
     * Charge la liste des relev�s de travaux correspondant au filtre donn�.
     * @param filter le filtre
     * @param plugin le Plugin
     * @return une liste de relev�s de travaux.
     */
    public static List<ReleveDesTravaux> findReleveDesTravauxByFilter( ReleveDesTravauxFilter filter, Plugin plugin )
    {
        return _dao.selectReleveDesTravauxByFilter( filter, plugin );
    }
}
