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
package fr.paris.lutece.plugins.ods.business.expression;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * ExpressionUsuelleHome
 */
public class ExpressionUsuelleHome
{
    private static IExpressionUsuelleDAO _dao = (IExpressionUsuelleDAO) SpringContextService.getPluginBean( "ods",
            "expressionUsuelleDAO" );

    /**
     * Crée une nouvelle expression usuelle
     * @param expression l'expression à ajouter
     * @param plugin le Plugin actif
     */
    public static void create( ExpressionUsuelle expression, Plugin plugin )
    {
        _dao.insert( expression, plugin );
    }

    /**
    * Met à jour une expression usuelle
    * @param expression l'expression à modifier
    * @param plugin le Plugin actif
    */
    public static void update( ExpressionUsuelle expression, Plugin plugin )
    {
        _dao.store( expression, plugin );
    }

    /**
    * Supprime une expression usuelle
    * @param expression l'expression à supprimer
    * @param plugin le Plugin actif
    * @throws AppException si l'expression est référencée par un objet tiers
    */
    public static void remove( ExpressionUsuelle expression, Plugin plugin )
        throws AppException
    {
        _dao.delete( expression, plugin );
    }

    /**
    * Retourne l'expression usuelle identifiée par nKey
    * @param nKey l'identifiant de l'expression
    * @param plugin le Plugin actif
    * @return l'expression usuelle
    */
    public static ExpressionUsuelle findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
    * Retourne la liste des expressions usuelles
    * @param plugin le Plugin actif
    * @return la liste des expressions usuelles
    */
    public static List<ExpressionUsuelle> findExpressionList( Plugin plugin )
    {
        return _dao.loadListExpressions( plugin );
    }
}
