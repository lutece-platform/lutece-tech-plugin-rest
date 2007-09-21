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
package fr.paris.lutece.plugins.ods.business.historique;

import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


/**
 *
 * IHistoriqueDAO est responsable de la communication avec la BDD
 */
public interface IHistoriqueDAO
{
    /**
     * Supprime l'objet historique en base
     * @param historique Historique
     * @param plugin Plugin
     */
    void delete( Historique historique, Plugin plugin );

    /**
     * Insere l'objet historique en base
     * @param historique Historique
     * @param plugin Plugin
     */
    void insert( Historique historique, Plugin plugin );

    /**
     * charge l'objet historique
     * @param nKey int
     * @param plugin Plugin
     * @return Historique l'objet historique
     */
    Historique load( int nKey, Plugin plugin );

    /**
     * Modifie le plugin
     * @param historique Historique
     * @param plugin Plugin
     */
    void store( Historique historique, Plugin plugin );

    /**
     * Retourne une liste d'historiques concernant le fichier passé en paramètre
     *
     * @param fichier Fichier
     * @param plugin Plugin
     * @return List<Historique> liste d'historiques
     */
    List<Historique> loadByFichier( Fichier fichier, Plugin plugin );

    /**
     * Retourne une liste d'historiques concernant le pdd passé en paramètre
     *
     * @param pdd PDD
     * @param plugin Plugin
     * @return List<Historique> liste d'historiques
     */
    List<Historique> loadByPDD( PDD pdd, Plugin plugin );

    /**
     * Retourne une liste d'historiques concernant le voeuAmendement passé en paramètre
     *
     * @param voeuAmendement VoeuAmendement
     * @param plugin Plugin
     * @return List<Historique> liste d'historiques
     */
    List<Historique> loadByVoeuAmendement( VoeuAmendement voeuAmendement, Plugin plugin );

    /**
     * Supprime tous les historiques concernant le fichier passé en paramètre
     *
     * @param fichier Fichier
     * @param plugin Plugin
     */
    void delete( Fichier fichier, Plugin plugin );

    /**
     * Supprime tous les historiques concernant le pdd passé en paramètre
     *
     * @param pdd PDD
     * @param plugin Plugin
     */
    void delete( PDD pdd, Plugin plugin );

    /**
     * Supprime tous les historiques concernant le voeuAmendement passé en paramètre
     *
     * @param voeuAmendement VoeuAmendement
     * @param plugin Plugin
     */
    void delete( VoeuAmendement voeuAmendement, Plugin plugin );
}
