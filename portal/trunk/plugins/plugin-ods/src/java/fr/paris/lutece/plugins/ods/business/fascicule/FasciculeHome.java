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
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * Cette classe permet de manipuler des objets Fascicule.
 */
public class FasciculeHome
{
    private static IFasciculeDAO _dao = (IFasciculeDAO) SpringContextService.getPluginBean( "ods", "fasciculeDAO" );

    /**
     * Cr�e un nouveau fascicule � partir de l'objet pass� en argument.
     * @param fascicule le fascicule � cr�er
     * @param plugin le plugin actif
     */
    public static void create( Fascicule fascicule, Plugin plugin )
    {
        _dao.insert( fascicule, plugin );
    }

    /**
     * Retourne le fascicule ayant pour identifiant nKey.
     * @param nKey l'identifiant du fascicule
     * @param plugin le plugin actif
     * @return le fascicule
     */
    public static Fascicule findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Supprime le fascicule pass� en argument.
     * @param fascicule le fascicule � supprimer
     * @param plugin le plugin actif
     * @throws AppException si le fascicule est r�f�renc� par un voeu ou un amendement
     */
    public static void remove( Fascicule fascicule, Plugin plugin )
        throws AppException
    {
        _dao.delete( fascicule, plugin );
    }

    /**
     * Met � jour le fascicule pass� en argument.
     * @param fascicule le fascicule � modifier
     * @param plugin
     */
    public static void update( Fascicule fascicule, Plugin plugin )
    {
        _dao.store( fascicule, plugin );
    }

    /**
     * Retourne la liste de fascicules pour la s�ance identifi�e par nKey.
     * @param nKey l'identifiant de la s�ance
     * @param plugin
     * @return la liste de fascicules pour la s�ance
     */
    public static List<Fascicule> findFasciculeByIdSeance( int nKey, Plugin plugin )
    {
        return _dao.selectFasciculeListByIdSeance( nKey, plugin );
    }

    /**
     * Indique si le code pass� en argument est d�j� utilis� par un fascicule de cette s�ance;
     * la recherche exclut le fascicule pass� en argument.
     * @param exceptFascicule le fascicule � exclure de la recherche (pas d'exclusion si = null)
     * @param strCode le code recherch�
     * @param seance la s�ance
     * @param plugin
     * @return true si le code est d�j� utilis�, false sinon
     */
    public static boolean containsCode( Fascicule exceptFascicule, String strCode, Seance seance, Plugin plugin )
    {
        return _dao.listCodeFascicule( exceptFascicule, seance, plugin ).contains( strCode );
    }

    /**
     * Indique si le num�ro d'ordre pass� en argument est d�j� utilis� par un fascicule de cette s�ance;
     * la recherche exclut le fascicule pass� en argument.
     * @param exceptFascicule le fascicule � exclure de la recherche (pas d'exclusion si = null)
     * @param nNumero le num�ro recherch�
     * @param seance la s�ance
     * @param plugin
     * @return true si le num�ro est d�j� utilis�, false sinon
     */
    public static boolean containsNumero( Fascicule exceptFascicule, int nNumero, Seance seance, Plugin plugin )
    {
        return _dao.listNumeroOrdre( exceptFascicule, seance, plugin )
                   .contains( OdsConstants.CONSTANTE_CHAINE_VIDE + nNumero );
    }

    /**
     * Retourne la liste des fascicules
     * @param plugin le plugin actif
     * @return la liste des fascicules
     */
    public static List<Fascicule> findAll( Plugin plugin )
    {
        return _dao.selectFasciculeList( plugin );
    }
}
