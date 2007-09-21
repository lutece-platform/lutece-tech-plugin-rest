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
package fr.paris.lutece.plugins.ods.business.commission;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * CommissionHome
 */
public class CommissionHome
{
    private static ICommissionDAO _dao = (ICommissionDAO) SpringContextService.getPluginBean( "ods", "commissionDAO" );

    /**
    * Cr�e une nouvelle commission � partir de l�objet commission pass� en param�tre
    * @param commission la commission � ins�rer
    * @param plugin le Plugin actif
    */
    public static void create( Commission commission, Plugin plugin )
    {
        _dao.insert( commission, plugin );
    }

    /**
     * Modifie la commission correspondant � l�objet commission pass� en param�tre
     * @param commission la commission � modifier
     * @param plugin le Plugin actif
     */
    public static void update( Commission commission, Plugin plugin )
    {
        _dao.store( commission, plugin );
    }

    /**
     * Supprime la commission correspondant �  l�objet commission pass� en param�tre
     * @param commission la commission � supprimer
     * @param plugin le Plugin actif
     * @throws AppException si contrainte de cl� �trang�re
     */
    public static void remove( Commission commission, Plugin plugin )
        throws AppException
    {
        _dao.delete( commission, plugin );
    }

    /**
     * renvoie la commission ayant comme identifiant nKey
     * @param nKey idenfiant de la commission � supprimer
     * @param plugin le Plugin actif
     * @return objet Commission
     */
    public static Commission findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * renvoie la liste des commissions dont la date de fin est sup�rieur ou �gale a la date du jour
     * @param plugin le Plugin actif
     * @return liste d'objets Commission
     */
    public static List<Commission> findCommissionList( Plugin plugin )
    {
        return _dao.commissionList( plugin );
    }

    /**
     * Renvoie la commission qui a pour num�ro le nNumeroCommission pass� en param�tre
     *
     * @param nNumeroCommission numero de la commission recherch�e
     * @param plugin le Plugin actif
     * @return objet Commission
     */
    public static Commission findByNumeroCommission( int nNumeroCommission, Plugin plugin )
    {
        return _dao.loadByNumeroCommission( nNumeroCommission, plugin );
    }

    /**
     * Retourne la liste des commissions actives
     * @param plugin le plugin actif
     * @return la liste des commissions actives
     */
    public static List<Commission> findAllCommissionsActives( Plugin plugin )
    {
        return _dao.loadListCommissionsActives( plugin );
    }
}
