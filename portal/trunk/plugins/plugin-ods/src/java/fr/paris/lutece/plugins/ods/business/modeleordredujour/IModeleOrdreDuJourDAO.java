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
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * IFichierDAO
 */
public interface IModeleOrdreDuJourDAO
{
    /**
     * Crée un nouveau MODJ à partir de l’objet MODJ passé en paramètre
     * @param modeleOrdreDuJour le MODJ à insérer
     * @param plugin le plugin
     * @return int primaryKey
     */
    int insert( ModeleOrdreDuJour modeleOrdreDuJour, Plugin plugin );

    /**
     * Modifie le MODJ correspondant à l’objet MODJ passé en paramètre
     * @param modeleOrdreDuJour le MODJ à modifier
     * @param plugin le plugin
     */
    void store( ModeleOrdreDuJour modeleOrdreDuJour, Plugin plugin );

    /**
     * Supprime le MODJ correspondant à l’objet passé en paramètre
     * @param modeleOrdreDuJour  le  MODJ à supprimer
     * @param plugin le plugin
     * @throws AppException si contrainte de clé étrangère
     */
    void delete( ModeleOrdreDuJour modeleOrdreDuJour, Plugin plugin )
        throws AppException;

    /**
     * Retourne le ModeleOrdreDuJour ayant comme identifiant nKey
     * @param nKey identifiant du ModeleOrdreDuJour à charger
     * @param plugin le plugin
     * @return le fichier identifié par nKey
     */
    List<ModeleOrdreDuJour> selectListModeleOrdreDuJourList( Plugin plugin );

    /**
     * Retourne le ModeleOrdreDuJour ayant comme identifiant nKey
     * @param nKey identifiant du ModeleOrdreDuJour à charger
     * @param plugin le plugin
     * @return le fichier identifié par nKey
     */
    ModeleOrdreDuJour load( int nKey, Plugin plugin );

    /**
    * Retourne le ModeleOrdreDuJour répondant au filtre ordreDuJourFilter
    * @param ordreDuJourFilter filtre
    * @param plugin le plugin
    * @return  Retourne le ModeleOrdreDuJour répondant au filtre ordreDuJourFilter
    */
    ModeleOrdreDuJour load( OrdreDuJourFilter filter, Plugin plugin );

    boolean isAlreadyExist( int nIdType, int nIdFormationConseil, int nIdModele, int nIdCommission, Plugin plugin );
}
