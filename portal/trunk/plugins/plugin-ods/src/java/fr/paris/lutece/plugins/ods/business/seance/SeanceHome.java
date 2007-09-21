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
package fr.paris.lutece.plugins.ods.business.seance;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;

import java.sql.Timestamp;

import java.util.List;


/**
 *
 *  SeanceHome
 */
public class SeanceHome
{
    private static ISeanceDAO _dao = (ISeanceDAO) SpringContextService.getPluginBean( "ods", "seanceDAO" );

    /**
    * Cr�e une nouvelle s�ance � partir de l�objet seance pass� en param�tre
    * @param seance la s�ance � ins�rer
    * @param plugin Plugin
    */
    public static void create( Seance seance, Plugin plugin )
    {
        _dao.insert( seance, plugin );
    }

    /**
     * Modifie la s�ance correspondant � l�objet seance pass� en param�tre
     * @param seance la s�ance � modifier
     * @param plugin Plugin
     */
    public static void update( Seance seance, Plugin plugin )
    {
        _dao.store( seance, plugin );
    }

    /**
     * Supprime la s�ance correspondant �  l�objet seance pass� en param�tre
     * @param seance la s�ance � supprimer
     * @param plugin Plugin
     * @throws AppException exception lev� si une dependance existe
     */
    public static void remove( Seance seance, Plugin plugin )
        throws AppException
    {
        _dao.delete( seance, plugin );
    }

    /**
     * renvoie la s�ance ayant comme identifiant nKey
     * @param nKey idenfiant de la s�ance � supprimer
     * @param plugin Plugin
     * @return objet Seance
     */
    public static Seance findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * renvoie la liste des s�ances dont la date de fin est sup�rieur ou �gale a la date du jour
     * @param plugin Plugin
     * @return liste d'objets Seance
     */
    public static List<Seance> findSeanceList( Plugin plugin )
    {
        return _dao.seanceList( plugin );
    }

    /**
     * renvoie la liste des s�ances dont la date de d�but est comprise entre startDateFilter et endDateFilter
     *
     * @param startDateFilter date de d�but
     * @param endDateFilter date de fin
     * @param plugin Plugin
     * @return liste d'objets Seance
     */
    public static List<Seance> findSeanceWidthFilterList( Timestamp startDateFilter, Timestamp endDateFilter,
        Plugin plugin )
    {
        return _dao.loadWidthFilter( startDateFilter, endDateFilter, plugin );
    }

    /**
     * Retourne la prochaine s�ance dont la date de fin est sup�rieur ou �gale a la date du jour
     * @param plugin Plugin
     * @return objet Seance
     */
    public static Seance getProchaineSeance( Plugin plugin )
    {
        return _dao.findNextSeance( plugin );
    }

    /**
     * Retourne la liste des s�ances pr�c�dentes
     * @param plugin Plugin
     * @return la liste des Seance pr�c�dentes
     */
    public static List<Seance> findOldSeance( Plugin plugin )
    {
        return _dao.findOldSeance( plugin );
    }

    /**
     * Retourne la derni�re s�ance dont la date de fin est inf�rieure � la date du jour
     * @param plugin Plugin
     * @return objet Seance
     */
    public static Seance getDerniereSeance( Plugin plugin )
    {
        return _dao.findLastSeance( plugin );
    }

    /**
    * Retourne la s�ance � laquelle est rattach�e un pdd
    * @param nIdPdd l'identifiant du Pdd
    * @param plugin le Plugin
    */
    public static Seance findSeanceByPdd( int nIdPdd, Plugin plugin )
    {
        return _dao.findSeanceByPdd( nIdPdd, plugin );
    }
}
