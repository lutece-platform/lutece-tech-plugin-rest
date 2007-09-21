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
package fr.paris.lutece.plugins.document.business.attributes;

import fr.paris.lutece.plugins.document.business.*;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for DocumentAttribute objects
 */
public final class DocumentAttributeDAO implements IDocumentAttributeDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_document_attribute ) FROM document_type_attributes ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO document_type_attributes ( id_document_attribute, code_document_type, code_attribute_type, code, name, description, attribute_order, required, searchable ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM document_type_attributes WHERE id_document_attribute = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE document_type_attributes SET id_document_attribute = ?, code_document_type = ?, code_attribute_type = ?, code = ?, name = ?, description = ?, attribute_order = ?, required = ?, searchable = ? WHERE id_document_attribute = ?  ";
    private static final String SQL_QUERY_SELECTALL_ATTRIBUTES = " SELECT a.id_document_attribute, a.code_document_type," +
        " a.code_attribute_type, a.code, " + " a.name, a.description, a.attribute_order, a.required, a.searchable " +
        " FROM document_type_attributes a, document_attribute_type b" +
        " WHERE a.code_attribute_type =  b.code_attribute_type" +
        " AND a.code_document_type = ? ORDER BY  a.attribute_order";
    private static final String SQL_QUERY_SELECT_ATTRIBUTE = " SELECT a.id_document_attribute, a.code_document_type," +
        " a.code_attribute_type, a.code, " + " a.name, a.description, a.attribute_order, a.required, a.searchable " +
        " FROM document_type_attributes a, document_attribute_type b" +
        " WHERE a.code_attribute_type =  b.code_attribute_type" + " AND a.id_document_attribute = ? ";
    private static final String SQL_QUERY_INSERT_PARAMETER_VALUES = "INSERT INTO document_type_attributes_parameters ( id_document_attribute, parameter_name, id_list_parameter, parameter_value )" +
        "VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_SELECT_PARAMETERS = "SELECT DISTINCT parameter_name FROM document_type_attributes_parameters WHERE id_document_attribute = ? ";
    private static final String SQL_QUERY_SELECT_PARAMETER_VALUES = "SELECT parameter_value FROM document_type_attributes_parameters " +
        "WHERE id_document_attribute = ? AND parameter_name = ? ";
    private static final String SQL_QUERY_DELETE_PARAMETER_VALUES = "DELETE FROM document_type_attributes_parameters WHERE id_document_attribute = ? AND parameter_name = ? ";
    private static final String SQL_QUERY_DELETE_PARAMETERS_VALUES = "DELETE FROM document_type_attributes_parameters WHERE id_document_attribute = ? ";

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
     * @param documentAttribute The documentAttribute object
     */
    public void insert( DocumentAttribute documentAttribute )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        documentAttribute.setId( newPrimaryKey(  ) );
        daoUtil.setInt( 1, documentAttribute.getId(  ) );
        daoUtil.setString( 2, documentAttribute.getCodeDocumentType(  ) );
        daoUtil.setString( 3, documentAttribute.getCodeAttributeType(  ) );
        daoUtil.setString( 4, documentAttribute.getCode(  ) );
        daoUtil.setString( 5, documentAttribute.getName(  ) );
        daoUtil.setString( 6, documentAttribute.getDescription(  ) );
        daoUtil.setInt( 7, documentAttribute.getAttributeOrder(  ) );
        daoUtil.setInt( 8, documentAttribute.isRequired(  ) ? 1 : 0 );
        daoUtil.setInt( 9, documentAttribute.isSearchable(  ) ? 1 : 0 );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
        // Insert parameters
        insertAttributeParameters( documentAttribute );
    }

    /**
     * Load the data of DocumentAttribute from the table
     * @param nAttributeId The attribute Id
     * @return the instance of the DocumentAttribute
     */
    public DocumentAttribute load( int nAttributeId )
    {
        DocumentAttribute documentAttribute = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ATTRIBUTE );
        daoUtil.setInt( 1, nAttributeId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            documentAttribute = new DocumentAttribute(  );
            documentAttribute.setId( daoUtil.getInt( 1 ) );
            documentAttribute.setCodeDocumentType( daoUtil.getString( 2 ) );
            documentAttribute.setCodeAttributeType( daoUtil.getString( 3 ) );
            documentAttribute.setCode( daoUtil.getString( 4 ) );
            documentAttribute.setName( daoUtil.getString( 5 ) );
            documentAttribute.setDescription( daoUtil.getString( 6 ) );
            documentAttribute.setAttributeOrder( daoUtil.getInt( 7 ) );
            documentAttribute.setRequired( daoUtil.getInt( 8 ) != 0 );
            documentAttribute.setSearchable( daoUtil.getInt( 9 ) != 0 );
        }

        daoUtil.free(  );

        return documentAttribute;
    }

    /**
     * Delete a record from the table
     * @param nAttributeId The DocumentAttribute Id
     */
    public void delete( int nAttributeId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nAttributeId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
        deleteParameters( nAttributeId );
    }

    /**
     * Delete a record from the table
     * @param nAttributeId The DocumentAttribute Id
     */
    private void deleteParameters( int nAttributeId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_PARAMETERS_VALUES );
        daoUtil.setInt( 1, nAttributeId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete a record from the table
     * @param nAttributeId The DocumentAttribute Id
     */
    private void deleteParameter( int nAttributeId, String strParameterName )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_PARAMETER_VALUES );
        daoUtil.setInt( 1, nAttributeId );
        daoUtil.setString( 2, strParameterName );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param documentAttribute The document attribute
     */
    public void store( DocumentAttribute documentAttribute )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setInt( 1, documentAttribute.getId(  ) );
        daoUtil.setString( 2, documentAttribute.getCodeDocumentType(  ) );
        daoUtil.setString( 3, documentAttribute.getCodeAttributeType(  ) );
        daoUtil.setString( 4, documentAttribute.getCode(  ) );
        daoUtil.setString( 5, documentAttribute.getName(  ) );
        daoUtil.setString( 6, documentAttribute.getDescription(  ) );
        daoUtil.setInt( 7, documentAttribute.getAttributeOrder(  ) );
        daoUtil.setInt( 8, documentAttribute.isRequired(  ) ? 1 : 0 );
        daoUtil.setInt( 9, documentAttribute.isSearchable(  ) ? 1 : 0 );
        daoUtil.setInt( 10, documentAttribute.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        // Update parameters
        deleteParameters( documentAttribute.getId(  ) );
        insertAttributeParameters( documentAttribute );
    }

    /**
     * Add attributes to a document
     * @param documentType The document Type
     */
    public void selectAttributesByDocumentType( DocumentType documentType )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ATTRIBUTES );
        daoUtil.setString( 1, documentType.getCode(  ) );
        daoUtil.executeQuery(  );

        int nOrder = 1;

        while ( daoUtil.next(  ) )
        {
            DocumentAttribute documentAttribute = new DocumentAttribute(  );
            documentAttribute.setId( daoUtil.getInt( 1 ) );
            documentAttribute.setCodeDocumentType( daoUtil.getString( 2 ) );
            documentAttribute.setCodeAttributeType( daoUtil.getString( 3 ) );
            documentAttribute.setCode( daoUtil.getString( 4 ) );
            documentAttribute.setName( daoUtil.getString( 5 ) );
            documentAttribute.setDescription( daoUtil.getString( 6 ) );
            documentAttribute.setAttributeOrder( nOrder );
            documentAttribute.setRequired( daoUtil.getInt( 8 ) != 0 );
            documentAttribute.setSearchable( daoUtil.getInt( 9 ) != 0 );

            documentType.addAttribute( documentAttribute );
            nOrder++;
        }

        daoUtil.free(  );
    }

    // Parameters
    private void insertAttributeParameters( DocumentAttribute documentAttribute )
    {
        for ( AttributeTypeParameter parameter : documentAttribute.getParameters(  ) )
        {
            deleteParameter( documentAttribute.getId(  ), parameter.getName(  ) );

            int i = 0;

            for ( String value : parameter.getValueList(  ) )
            {
                DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_PARAMETER_VALUES );
                daoUtil.setInt( 1, documentAttribute.getId(  ) );
                daoUtil.setString( 2, parameter.getName(  ) );
                daoUtil.setInt( 3, i++ );
                daoUtil.setString( 4, value );

                daoUtil.executeUpdate(  );
                daoUtil.free(  );
            }
        }
    }

    /**
     * Gets Attribute parameters values
     * @param nAttributeId The attribute Id
     * @return List of attribute parameters values
     */
    public List<AttributeTypeParameter> selectAttributeParametersValues( int nAttributeId )
    {
        ArrayList<AttributeTypeParameter> listParameters = new ArrayList<AttributeTypeParameter>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PARAMETERS );
        daoUtil.setInt( 1, nAttributeId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AttributeTypeParameter parameter = new AttributeTypeParameter(  );
            parameter.setName( daoUtil.getString( 1 ) );
            parameter.setValueList( getAttributeParameterValues( nAttributeId, parameter.getName(  ) ) );
            listParameters.add( parameter );
        }

        daoUtil.free(  );

        return listParameters;
    }

    /**
     * Returns the parameter value of an attribute
     * @param nAttributeId The attribute Id
     * @param strParameterName The parameter name
     * @return The parameter values of an attribute
     */
    public List<String> getAttributeParameterValues( int nAttributeId, String strParameterName )
    {
        List<String> listValues = new ArrayList<String>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PARAMETER_VALUES );
        daoUtil.setInt( 1, nAttributeId );
        daoUtil.setString( 2, strParameterName );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            listValues.add( daoUtil.getString( 1 ) );
        }

        daoUtil.free(  );

        return listValues;
    }
}
