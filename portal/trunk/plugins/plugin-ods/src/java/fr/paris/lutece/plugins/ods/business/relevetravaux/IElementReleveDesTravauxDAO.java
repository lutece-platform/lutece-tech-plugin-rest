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
     * Supprime un �l�ment de relev� de travaux.
     * @param pdd le PDD
     * @param plugin le Plugin
     * @throws AppException Exception SQL si l'�l�ment ne peut �tre supprim�
     */
    void delete( PDD pdd, Plugin plugin ) throws AppException;

    /**
     * Supprime un �l�ment de relev� de travaux.
     * @param voeuAmendement le Voeu ou Amendement
     * @param plugin le plugin
     * @throws AppException Exception SQL si l'�l�ment ne peut �tre supprim�
     */
    void delete( VoeuAmendement voeuAmendement, Plugin plugin )
        throws AppException;

    /**
     * Supprime un �l�ment de relev� de travaux.
     * @param elementReleve l'�l�ment de relev� de travaux
     * @param plugin le Plugin
     * @throws AppException Exception SQL si l'�l�ment ne peut �tre supprim�
     */
    void delete( ElementReleveDesTravaux elementReleve, Plugin plugin )
        throws AppException;

    /**
     * Cr�e un �l�ment de relev� de travaux.
     * @param elementReleve l'�l�ment de relev� de travaux
     * @param plugin le Plugin
     */
    void insert( ElementReleveDesTravaux elementReleve, Plugin plugin );

    /**
     * Renvoie l'�l�ment de relev� de travaux dont la cl� primaire est donn�e en argument.
     * @param nKey la cl� d'�l�ment de travaux
     * @param plugin le Plugin
     * @return
     */
    ElementReleveDesTravaux load( int nKey, Plugin plugin );

    /**
     * Modifie un �l�ment de relev� de travaux.
     * @param elementReleve l'�l�ment de relev� de travaux
     * @param plugin le Plugin
     */
    void store( ElementReleveDesTravaux elementReleve, Plugin plugin );

    /**
     * Renvoie la liste des �l�ments de relev�s de travaux.
     * @param plugin le Plugin
     * @return une liste d'�l�ments de travaux
     */
    List<ElementReleveDesTravaux> selectElementReleveDesTravauxList( Plugin plugin );

    /**
     * Renvoie la liste des �l�ments de relev�s de travaux associ� un relev� donn� en argument.
     * @param plugin le Plugin
     * @return une liste d'�l�ments de travaux
     */
    List<ElementReleveDesTravaux> selectElementReleveDesTravauxReleve( ReleveDesTravaux releve, Plugin plugin );

    /**
     * Renvoie la liste des �l�ments de relev�s de travaux associ� un relev� donn� en argument et qui sont des voeux ou des amendements.
     * @param plugin le Plugin
     * @return une liste d'�l�ments de travaux
     */
    List<ElementReleveDesTravaux> selectElementReleveDesTravauxReleveVA( ReleveDesTravaux releve, Plugin plugin );

    /**
     * Renvoie la liste des �l�ments de relev�s de travaux associ� un relev� donn� en argument et qui sont des projets de d�lib�ration.
     * @param plugin le Plugin
     * @return une liste d'�l�ments de travaux
     */
    List<ElementReleveDesTravaux> selectElementReleveDesTravauxRelevePDD( ReleveDesTravaux releve, Plugin plugin );
}
