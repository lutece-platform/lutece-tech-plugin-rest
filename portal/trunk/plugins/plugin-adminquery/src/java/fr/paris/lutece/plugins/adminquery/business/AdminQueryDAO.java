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
package fr.paris.lutece.plugins.adminquery.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for AdminQuery DAO objects
 */
public final class AdminQueryDAO implements IAdminqueryDAO
{
    private static final String STR_SQL_ERROR = "SQL Error executing command : ";

    /**
     * Loads the list ColumnNames
     * @param strRequest The Request String
     * @param plugin the Plugin
     * @return a list of String
     */
    public List<String> selectColumnNames( String strRequest, Plugin plugin )
    {
        List<String> listNames = new ArrayList(  );
        DAOUtil daoUtil = new DAOUtil( strRequest, plugin );
        daoUtil.executeQuery(  );

        for ( int i = 1; i <= getColumnCount( daoUtil ); i++ )
        {
            String columnName = getColumnName( i, daoUtil );
            listNames.add( columnName );
        }

        daoUtil.free(  );

        return listNames;
    }

    /**
     * Return a list (all rows) of list (row of the request's table) of String (cells)
     * @param strRequest The Request String
     * @return a List of the Columns' values
     */
    public List<List> selectRows( String strRequest, Plugin plugin )
    {
        List<List> listRows = new ArrayList(  );
        DAOUtil daoUtil = new DAOUtil( strRequest, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            String strValue = null;
            List<String> listLine = new ArrayList(  );

            for ( int i = 1; i <= getColumnCount( daoUtil ); i++ )
            {
                switch ( getColumnType( i, daoUtil ) )
                {
                    case java.sql.Types.CHAR:
                    case java.sql.Types.VARCHAR:
                    case java.sql.Types.LONGVARCHAR:
                        strValue = daoUtil.getString( getColumnName( i, daoUtil ) );

                        break;

                    case java.sql.Types.INTEGER:
                    case java.sql.Types.BIGINT:
                    case java.sql.Types.SMALLINT:
                        strValue = "" + daoUtil.getInt( getColumnName( i, daoUtil ) );

                        break;

                    case java.sql.Types.TIMESTAMP:
                        strValue = DateUtil.getDateString( daoUtil.getTimestamp( getColumnName( i, daoUtil ) ) );

                        break;

                    case java.sql.Types.DATE:
                        strValue = DateUtil.getDateString( daoUtil.getDate( getColumnName( i, daoUtil ) ) );

                        break;

                    default:
                        break;
                }

                listLine.add( strValue );
            }

            listRows.add( listLine );
        }

        daoUtil.free(  );

        return listRows;
    }

    /**
     * Returns the number of columns
     *
     * @param daoUtil the DAOUtil object
     * @return int
     */
    private int getColumnCount( DAOUtil daoUtil )
    {
        try
        {
            ResultSetMetaData rsmd = daoUtil.getResultSet(  ).getMetaData(  );

            return rsmd.getColumnCount(  );
        }
        catch ( SQLException e )
        {
            daoUtil.free(  );
            throw new AppException( STR_SQL_ERROR + e.toString(  ) );
        }
    }

    /**
     * Returns the name of the column
     *
     * @param int nColumn, the index of the column
     * @param daoUtil the DAOUtil object
     * @return String, the name of the column
     */
    private String getColumnName( int nColumn, DAOUtil daoUtil )
    {
        try
        {
            ResultSetMetaData rsmd = daoUtil.getResultSet(  ).getMetaData(  );

            return rsmd.getColumnName( nColumn );
        }
        catch ( SQLException e )
        {
            daoUtil.free(  );
            throw new AppException( STR_SQL_ERROR + e.toString(  ) );
        }
    }

    /**
     * Returns the type of the column's data
     *
     * @param int nColumn, the index of the column
     * @param daoUtil the DAOUtil object
     * @return int
     */
    private int getColumnType( int nColumn, DAOUtil daoUtil )
    {
        try
        {
            ResultSetMetaData rsmd = daoUtil.getResultSet(  ).getMetaData(  );

            return rsmd.getColumnType( nColumn );
        }
        catch ( SQLException e )
        {
            daoUtil.free(  );
            throw new AppException( STR_SQL_ERROR + e.toString(  ) );
        }
    }
}
