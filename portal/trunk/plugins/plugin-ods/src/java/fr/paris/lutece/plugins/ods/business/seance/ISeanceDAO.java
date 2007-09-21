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
import fr.paris.lutece.portal.service.util.AppException;

import java.sql.Timestamp;

import java.util.List;


/**
 *
 * Responsable de communiqué avec la BDD
 */
public interface ISeanceDAO
{
    /**
    * Crée une nouvelle séance à partir de l’objet seance passé en paramètre
    * @param seance la séance à insérer
    * @param plugin Plugin
    */
    void insert( Seance seance, Plugin plugin );

    /**
     * Modifie la séance correspondant à l’objet seance passé en paramètre
     * @param seance la séance à modifier
     * @param plugin Plugin
     */
    void store( Seance seance, Plugin plugin );

    /**
     * Supprime la séance correspondant à  l’objet seance passé en paramètre
     * @param seance la séance à supprimer
     * @param plugin Plugin
     * @throws AppException exception levé si une dependance existe
     */
    void delete( Seance seance, Plugin plugin ) throws AppException;

    /**
     * renvoie la séance ayant comme identifiant nKey
     * @param nKey idenfiant de la séance à supprimer
     * @param plugin Plugin
     * @return objet Seance
     */
    Seance load( int nKey, Plugin plugin );

    /**
     * renvoie la liste des séances dont la date de fin est supérieur ou égale a la date du jour
     * @param plugin Plugin
     * @return liste d'objets Seance
     */
    List<Seance> seanceList( Plugin plugin );

    /**
     * renvoie la liste des séances dont la date de début est comprise entre startDateFilter et endDateFilter
     *
     * @param startDateFilter date de début
     * @param endDateFilter date de fin
     * @param plugin Plugin
     * @return liste d'objets Seance
     */
    List<Seance> loadWidthFilter( Timestamp startDateFilter, Timestamp endDateFilter, Plugin plugin );

    /**
     * Retourne la prochaine séance dont la date de fin est supérieure ou égale a la date du jour
     * @param plugin Plugin
     * @return objet Seance
     */
    Seance findNextSeance( Plugin plugin );

    /**
     * Retourne la liste des séances précédentes
     * @param plugin Plugin
     * @return la liste des Seance précédentes
     */
    List<Seance> findOldSeance( Plugin plugin );

    /**
     * Retourne la dernière séance dont la date de fin est inférieure à la date du jour
     * @param plugin Plugin
     * @return objet Seance
     */
    Seance findLastSeance( Plugin plugin );

    /**
    * Retourne la séance à laquelle est rattachée un pdd
    * @param nIdPdd l'identifiant du Pdd
    * @param plugin le Plugin
    */
    Seance findSeanceByPdd( int nIdPdd, Plugin plugin );
}
