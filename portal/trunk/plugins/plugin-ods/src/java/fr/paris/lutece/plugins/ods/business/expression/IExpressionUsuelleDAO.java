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
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 * IExpressionUsuelleDAO
 */
public interface IExpressionUsuelleDAO
{
    /**
     * Crée une nouvelle expression usuelle dans la table ods_expression_usuelle
     * @param expression l'expression à insérer
     * @param plugin le Plugin actif
     */
    void insert( ExpressionUsuelle expression, Plugin plugin );

    /**
     * Met à jour les informations d'une expression usuelle dans la table ods_expression_usuelle
     * @param expression la nouvelle expression
     * @param plugin le Plugin actif
     */
    void store( ExpressionUsuelle expression, Plugin plugin );

    /**
     * Supprime une expression usuelle de la table ods_expression_usuelle
     * @param expression l'expression à supprimer
     * @param plugin le Plugin actif
     * @throws AppException SQLException si l'expression ne peut être supprimée
     */
    void delete( ExpressionUsuelle expression, Plugin plugin )
        throws AppException;

    /**
     * Retourne l'expression usuelle associée à la clé primaire passée en argument de la méthode.
     * @param nKey l'identifiant de l'expression dans la table
     * @param plugin le Plugin actif
     * @return l'expression usuelle associée à la clé primaire passée en argument de la méthode
     */
    ExpressionUsuelle load( int nKey, Plugin plugin );

    /**
     * Retourne la liste des expressions usuelles.
     * @param plugin le Plugin actif
     * @return la liste des expressions usuelles
     */
    List<ExpressionUsuelle> loadListExpressions( Plugin plugin );
}
