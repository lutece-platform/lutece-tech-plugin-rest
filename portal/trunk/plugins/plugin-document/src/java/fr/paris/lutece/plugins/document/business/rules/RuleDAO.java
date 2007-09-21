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
package fr.paris.lutece.plugins.document.business.rules;

import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for Rule objects
 */
public final class RuleDAO implements IRuleDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_rule ) FROM document_rule ";
    private static final String SQL_QUERY_SELECT = "SELECT id_rule, rule_type FROM document_rule WHERE id_rule = ?  ";
    private static final String SQL_QUERY_SELECT_ATTRIBUTES = "SELECT attribute_name , attribute_value FROM document_rule_attributes WHERE id_rule = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO document_rule ( id_rule, rule_type ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_INSERT_ATTRIBUTE = " INSERT INTO document_rule_attributes ( id_rule, attribute_name , attribute_value ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM document_rule WHERE id_rule = ?  ";
    private static final String SQL_QUERY_DELETE_ATTRIBUTES = "DELETE FROM document_rule_attributes WHERE id_rule = ?  ";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_rule, rule_type FROM document_rule ";

    /**
     * Generates a new primary key
     * @return The new primary key
     */
    private int newPrimaryKey(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nKey;
    }

    /**
     * Insert a new record in the table.
     *
     * @param rule The rule object
     */
    public void insert( Rule rule )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        rule.setId( newPrimaryKey(  ) );
        daoUtil.setInt( 1, rule.getId(  ) );
        daoUtil.setString( 2, rule.getRuleTypeId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        // Rule attributes
        insertAttributes( rule );
    }

    private void insertAttributes( Rule rule )
    {
        String[] attributes = rule.getAttributesList(  );

        for ( int i = 0; i < attributes.length; i++ )
        {
            String strAttributeValue = rule.getAttribute( attributes[i] );
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_ATTRIBUTE );
            daoUtil.setInt( 1, rule.getId(  ) );
            daoUtil.setString( 2, attributes[i] );
            daoUtil.setString( 3, strAttributeValue );

            daoUtil.executeUpdate(  );
            daoUtil.free(  );
        }
    }

    /**
     * Load the data of Rule from the table
     *
     * @param nRuleId The identifier of Rule
     * @return the instance of the Rule
     */
    public Rule load( int nRuleId, IRuleTypesSet ruleTypesSet )
    {
        Rule rule = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nRuleId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            String strRuleTypeId = daoUtil.getString( 2 );
            rule = ruleTypesSet.newInstance( strRuleTypeId );
            rule.setId( nRuleId );
            rule.setRuleTypeId( strRuleTypeId );
            loadAttributes( rule );
        }

        daoUtil.free(  );

        return rule;
    }

    private void loadAttributes( Rule rule )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ATTRIBUTES );
        daoUtil.setInt( 1, rule.getId(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            rule.setAttribute( daoUtil.getString( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );
    }

    /**
     * Delete a record from the table
     * @param nRuleId The Rule Id
     */
    public void delete( int nRuleId )
    {
        deleteAttributes( nRuleId );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nRuleId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    private void deleteAttributes( int nRuleId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ATTRIBUTES );
        daoUtil.setInt( 1, nRuleId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param rule The reference of rule
     */
    public void store( Rule rule )
    {
        // Just update attributes
        deleteAttributes( rule.getId(  ) );
        insertAttributes( rule );
    }

    /**
     * Load the list of rules
     * @return The Collection of the Rules
     */
    public List<Rule> selectRuleList( IRuleTypesSet ruleTypesSet )
    {
        List<Rule> listRules = new ArrayList<Rule>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int nRuleId = daoUtil.getInt( 1 );
            Rule rule = load( nRuleId, ruleTypesSet );
            listRules.add( rule );
        }

        daoUtil.free(  );

        return listRules;
    }
}
