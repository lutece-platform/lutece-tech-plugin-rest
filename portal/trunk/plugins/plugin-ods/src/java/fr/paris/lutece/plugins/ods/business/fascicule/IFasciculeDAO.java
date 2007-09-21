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
package fr.paris.lutece.plugins.ods.business.fascicule;

import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * IFasciculeDAO
 */
public interface IFasciculeDAO
{
    /**
     * Supprime le fascicule de la base.
     * @param fascicule le fascicule à supprimer
     * @param plugin le plugin actif
     * @throws AppException si le fascicule ne peut être supprimé
     */
    void delete( Fascicule fascicule, Plugin plugin ) throws AppException;

    /**
     * Crée un nouveau fascicule dans la base.
     * @param fascicule le fascicule à créer
     * @param plugin le plugin actif
     */
    public void insert( Fascicule fascicule, Plugin plugin );

    /**
     * Retourne le fascicule identifié par la clé passée en argument.
     * @param nKey l'identifiant du fascicule
     * @param plugin le plugin actif
     * @return le fascicule
     */
    public Fascicule load( int nKey, Plugin plugin );

    /**
     * Met à jour les informations concernant le fascicule à partir de l'objet passé en argument
     * @param fascicule le fascicule à modifier
     * @param plugin le plugin actif
     */
    public void store( Fascicule fascicule, Plugin plugin );

    /**
     * Retourne la liste des fascicules pour une séance.
     * @param nKey l'identifiant de la séance
     * @param plugin le plugin actif
     * @return la liste des fascicules pour une séance
     */
    public List<Fascicule> selectFasciculeListByIdSeance( int nKey, Plugin plugin );

    /**
     * Retourne la liste des codes de fascicule d'une séance, à l'exception de celui du fascicule passé en argument
     * @param exceptFascicule fascicule à exclure de la recherche (pas d'exclusion si = null)
     * @param seance la seance
     * @param plugin le plugin actif
     * @return la liste des codes
     */
    public List<String> listCodeFascicule( Fascicule exceptFascicule, Seance seance, Plugin plugin );

    /**
     * Retourne la liste des numéros d'ordre d'une séance, à l'exception de celui du fascicule passé en argument
     * @param exceptFascicule fascicule à exclure de la recherche (pas d'exclusion si = null)
     * @param seance la seance
     * @param plugin le plugin actif
     * @return la liste des codes
     */
    public List<String> listNumeroOrdre( Fascicule exceptFascicule, Seance seance, Plugin plugin );

    /**
     * Retourne la liste des fascicules
     * @param plugin le plugin actif
     * @return la liste des fascicules
     */
    public List<Fascicule> selectFasciculeList( Plugin plugin );
}
