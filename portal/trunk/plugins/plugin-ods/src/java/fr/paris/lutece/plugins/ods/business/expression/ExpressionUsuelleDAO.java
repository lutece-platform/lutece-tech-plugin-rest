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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * IExpressionUsuelleDAO
 */
public class ExpressionUsuelleDAO implements IExpressionUsuelleDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_expression ) FROM ods_expression_usuelle ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_expression_usuelle(" +
        "	id_expression, expression) " + "VALUES (?,?) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ods_expression_usuelle " + "SET expression=? " +
        "WHERE id_expression=? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ods_expression_usuelle " + "WHERE id_expression=? ";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_expression, expression " +
        "FROM ods_expression_usuelle " + "WHERE id_expression=? ";
    private static final String SQL_QUERY_FIND_ALL = "SELECT id_expression, expression " +
        "FROM ods_expression_usuelle ";
    private static final String SQL_QUERY_ORDER_BY_EXPRESSION = " ORDER BY expression ASC ";

    /**
     * Retourne une nouvelle clé primaire unique sur la table ods_expression_usuelle.
     * @param plugin le plugin actif
     * @return nouvelle clé primaire
     */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nKey;
    }

    /**
     * Crée une nouvelle expression usuelle dans la table ods_expression_usuelle
     * @param expression l'expression à insérer
     * @param plugin le Plugin
     */
    public void insert( ExpressionUsuelle expression, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( 1, newPrimaryKey( plugin ) );
        daoUtil.setString( 2, expression.getExpression(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Met à jour les informations d'une expression usuelle dans la table ods_expression_usuelle
     * @param expression la nouvelle expression
     * @param plugin le Plugin
     */
    public void store( ExpressionUsuelle expression, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setString( 1, expression.getExpression(  ) );
        daoUtil.setInt( 2, expression.getIdExpression(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime une expression usuelle de la table ods_expression_usuelle
     * @param expression l'expression à supprimer
     * @param plugin le Plugin
     * @throws AppException Exception Sql si l'expression ne peut être supprimée
     */
    public void delete( ExpressionUsuelle expression, Plugin plugin )
        throws AppException
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, expression.getIdExpression(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne l'expression usuelle associée à la clé primaire passée en argument de la méthode.
     * @param nKey l'identifiant de l'expression dans la table ods_expression_usuelle
     * @param plugin le Plugin
     * @return l'expression usuelle associée à la clé primaire passée en argument de la méthode
     */
    public ExpressionUsuelle load( int nKey, Plugin plugin )
    {
        ExpressionUsuelle expression = new ExpressionUsuelle(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            expression.setIdExpression( daoUtil.getInt( 1 ) );
            expression.setExpression( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return expression;
    }

    /**
     * Retourne la liste des expressions usuelles.
     * @param plugin le Plugin
     * @return la liste des expressions usuelles
     */
    public List<ExpressionUsuelle> loadListExpressions( Plugin plugin )
    {
        List<ExpressionUsuelle> listExpression = new ArrayList<ExpressionUsuelle>(  );
        ExpressionUsuelle expression;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ALL + SQL_QUERY_ORDER_BY_EXPRESSION, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            expression = new ExpressionUsuelle(  );
            expression.setIdExpression( daoUtil.getInt( 1 ) );
            expression.setExpression( daoUtil.getString( 2 ) );
            listExpression.add( expression );
        }

        daoUtil.free(  );

        return listExpression;
    }
}
