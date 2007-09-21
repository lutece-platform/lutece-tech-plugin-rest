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
package fr.paris.lutece.plugins.dbpage.business;


/**
 * This class represents the business object DbPageDatabaseSection
 */
public class DbPageDatabaseSection
{
    private static final String EMPTY_STRING = "";
    private String _strColumn;
    private String _strPool;
    private String _strSql;
    private String _strTemplatePath;
    private String _strTitle;
    private int _nIdOrder;
    private int _nIdPage;
    private String _strRole;

    // Variables declarations
    private int _nIdSection;
    private int _nIdType;

    /**
     * Sets the Column
     *
     * @param strColumn The Column
     */
    public void setColumn( String strColumn )
    {
        _strColumn = ( strColumn == null ) ? EMPTY_STRING : strColumn;
    }

    /**
     * Returns the Column
     *
     * @return The Column
     */
    public String getColumn(  )
    {
        return _strColumn;
    }

    /**
     * Sets the Id
     *
     * @param nIdSection The Id
     */
    public void setId( int nIdSection )
    {
        _nIdSection = nIdSection;
    }

    /**
     * Returns the Id
     *
     * @return The Id
     */
    public int getId(  )
    {
        return _nIdSection;
    }

    /**
     * Sets the IdPage
     *
     * @param nIdPage The IdPage
     */
    public void setIdPage( int nIdPage )
    {
        _nIdPage = nIdPage;
    }

    /**
     * Returns the IdPage
     *
     * @return The IdPage
     */
    public int getIdPage(  )
    {
        return _nIdPage;
    }

    /**
     * Sets the IdType
     *
     * @param nIdType The IdType
     */
    public void setIdType( int nIdType )
    {
        _nIdType = nIdType;
    }

    /**
     * Returns the IdType
     *
     * @return The IdType
     */
    public int getIdType(  )
    {
        return _nIdType;
    }

    /**
     * Sets the Order
     *
     * @param nIdOrder The Order
     */
    public void setOrder( int nIdOrder )
    {
        _nIdOrder = nIdOrder;
    }

    /**
     * Returns the Order
     *
     * @return The Order
     */
    public int getOrder(  )
    {
        return _nIdOrder;
    }

    /**
     * Sets the Pool
     *
     * @param strPool The Pool
     */
    public void setPool( String strPool )
    {
        _strPool = strPool;
    }

    /**
     * Returns the Pool
     *
     * @return The Pool
     */
    public String getPool(  )
    {
        return _strPool;
    }

    /**
     * Sets the Sql
     *
     * @param strSql The Sql
     */
    public void setSql( String strSql )
    {
        _strSql = ( strSql == null ) ? EMPTY_STRING : strSql;
    }

    /**
     * Returns the Sql
     *
     * @return The Sql
     */
    public String getSql(  )
    {
        return _strSql;
    }

    /**
     * Sets the Template
     *
     * @param strTemplatePath The Template path
     */
    public void setTemplatePath( String strTemplatePath )
    {
        _strTemplatePath = strTemplatePath;
    }

    /**
     * Returns the Template
     *
     * @return The Template
     */
    public String getTemplatePath(  )
    {
        return _strTemplatePath;
    }

    /**
     * Sets the Title
     *
     * @param strTitle The Title
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Returns the Title
     *
     * @return The Title
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * Returns the Role
     *
     * @return The Role
     */
    public String getRole(  )
    {
        return _strRole;
    }

    /**
     * Sets the Role
     *
     * @param strRole The Role
     */
    public void setRole( String strRole )
    {
        _strRole = strRole;
    }
}
