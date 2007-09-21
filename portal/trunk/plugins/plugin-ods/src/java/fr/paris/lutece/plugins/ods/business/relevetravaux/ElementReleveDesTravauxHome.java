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
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 * ElementReleveDesTravauxHome
 */
public class ElementReleveDesTravauxHome
{
    private static IElementReleveDesTravauxDAO _dao = (IElementReleveDesTravauxDAO) SpringContextService.getPluginBean( "ods",
            "elementReleveDAO" );

    /**
     * Cr�e un �l�ment de relev� de travaux dans la base de donn�es
     * @param elementReleve l'�l�ment de relev� de travaux
     * @param plugin le Plugin actif
     */
    public static void create( ElementReleveDesTravaux elementReleve, Plugin plugin )
    {
        _dao.insert( elementReleve, plugin );
    }

    /**
     * r�cup�re l'�l�ment de relev� de travaux pour un identifiant donn�
     * @param nKey l'identifiant
     * @param plugin le Plugin actif
     * @return un �l�ment de relev� de travaux
     */
    public static ElementReleveDesTravaux findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Supprime un �l�ment de relev� de travaux de la base de donn�es
     * @param elementReleve l'�l�ment de relev� de travaux � supprimer
     * @param plugin le Plugin actif
     */
    public static void remove( ElementReleveDesTravaux elementReleve, Plugin plugin )
    {
        _dao.delete( elementReleve, plugin );
    }

    /**
     * Supprime un �l�ment de relev� de travaux pour un pdd donn�
     * @param pdd le PDD
     * @param plugin le Plugin actif
     */
    public static void remove( PDD pdd, Plugin plugin )
    {
        _dao.delete( pdd, plugin );
    }

    /**
     * Supprime un �l�ment de relev� de travaux pour un Voeu/Amendement donn�
     * @param voeuAmendement le VA
     * @param plugin le Plugin actif
     */
    public static void remove( VoeuAmendement voeuAmendement, Plugin plugin )
    {
        _dao.delete( voeuAmendement, plugin );
    }

    /**
     * mets � jour un �l�ment de relev� de travaux
     * @param elementReleve l'�l�ment de relev� de travaux
     * @param plugin le Plugin actif
     */
    public static void update( ElementReleveDesTravaux elementReleve, Plugin plugin )
    {
        _dao.store( elementReleve, plugin );
    }

    /**
     * R�cup�re la liste des �l�ments de relev� de travaux
     * @param plugin le Plugin actif
     * @return une liste d'�l�ments de travaux
     */
    public static List<ElementReleveDesTravaux> findElementReleveDesTravauxList( Plugin plugin )
    {
        return _dao.selectElementReleveDesTravauxList( plugin );
    }

    /**
     * R�cup�re la liste des �l�ments de relev� de travaux pour un relev� de travaux donn�
     * @param releve le relev� de travaux
     * @param plugin le Plugin actif
     * @return une liste d'�l�ments de travaux
     */
    public static List<ElementReleveDesTravaux> findElementReleveDesTravauxByReleve( ReleveDesTravaux releve,
        Plugin plugin )
    {
        return _dao.selectElementReleveDesTravauxReleve( releve, plugin );
    }

    /**
     * R�cup�re la liste des �l�ments de relev� de travaux pour un relev� de travaux donn� �tant un Voeu/Amendement
     * @param releve le relev� de travaux
     * @param plugin le Plugin actif
     * @return une liste d'�l�ments de travaux
     */
    public static List<ElementReleveDesTravaux> findElementReleveDesTravauxByReleveVA( ReleveDesTravaux releve,
        Plugin plugin )
    {
        return _dao.selectElementReleveDesTravauxReleveVA( releve, plugin );
    }

    /**
     * R�cup�re la liste des �l�ments de relev� de travaux pour un relev� de travaux donn� �tant un PDD
     * @param releve le relev� de travaux
     * @param plugin le Plugin actif
     * @return une liste d'�l�ments de travaux
     */
    public static List<ElementReleveDesTravaux> findElementReleveDesTravauxByRelevePDD( ReleveDesTravaux releve,
        Plugin plugin )
    {
        return _dao.selectElementReleveDesTravauxRelevePDD( releve, plugin );
    }
}
