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

import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * IElementReleveDesTravauxDAO
 */
public interface IElementReleveDesTravauxDAO
{
    /**
     * Supprime un élément de relevé de travaux.
     * @param pdd le PDD
     * @param plugin le Plugin
     * @throws AppException Exception SQL si l'élément ne peut être supprimé
     */
    void delete( PDD pdd, Plugin plugin ) throws AppException;

    /**
     * Supprime un élément de relevé de travaux.
     * @param voeuAmendement le Voeu ou Amendement
     * @param plugin le plugin
     * @throws AppException Exception SQL si l'élément ne peut être supprimé
     */
    void delete( VoeuAmendement voeuAmendement, Plugin plugin )
        throws AppException;

    /**
     * Supprime un élément de relevé de travaux.
     * @param elementReleve l'élément de relevé de travaux
     * @param plugin le Plugin
     * @throws AppException Exception SQL si l'élément ne peut être supprimé
     */
    void delete( ElementReleveDesTravaux elementReleve, Plugin plugin )
        throws AppException;

    /**
     * Crée un élément de relevé de travaux.
     * @param elementReleve l'élément de relevé de travaux
     * @param plugin le Plugin
     */
    void insert( ElementReleveDesTravaux elementReleve, Plugin plugin );

    /**
     * Renvoie l'élément de relevé de travaux dont la clé primaire est donnée en argument.
     * @param nKey la clé d'élément de travaux
     * @param plugin le Plugin
     * @return
     */
    ElementReleveDesTravaux load( int nKey, Plugin plugin );

    /**
     * Modifie un élément de relevé de travaux.
     * @param elementReleve l'élément de relevé de travaux
     * @param plugin le Plugin
     */
    void store( ElementReleveDesTravaux elementReleve, Plugin plugin );

    /**
     * Renvoie la liste des éléments de relevés de travaux.
     * @param plugin le Plugin
     * @return une liste d'éléments de travaux
     */
    List<ElementReleveDesTravaux> selectElementReleveDesTravauxList( Plugin plugin );

    /**
     * Renvoie la liste des éléments de relevés de travaux associé un relevé donné en argument.
     * @param plugin le Plugin
     * @return une liste d'éléments de travaux
     */
    List<ElementReleveDesTravaux> selectElementReleveDesTravauxReleve( ReleveDesTravaux releve, Plugin plugin );

    /**
     * Renvoie la liste des éléments de relevés de travaux associé un relevé donné en argument et qui sont des voeux ou des amendements.
     * @param plugin le Plugin
     * @return une liste d'éléments de travaux
     */
    List<ElementReleveDesTravaux> selectElementReleveDesTravauxReleveVA( ReleveDesTravaux releve, Plugin plugin );

    /**
     * Renvoie la liste des éléments de relevés de travaux associé un relevé donné en argument et qui sont des projets de délibération.
     * @param plugin le Plugin
     * @return une liste d'éléments de travaux
     */
    List<ElementReleveDesTravaux> selectElementReleveDesTravauxRelevePDD( ReleveDesTravaux releve, Plugin plugin );
}
