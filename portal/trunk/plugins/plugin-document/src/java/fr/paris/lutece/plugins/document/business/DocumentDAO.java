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
package fr.paris.lutece.plugins.document.business;

import fr.paris.lutece.plugins.document.business.attributes.DocumentAttribute;
import fr.paris.lutece.plugins.document.business.category.Category;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for Document objects
 */
public final class DocumentDAO implements IDocumentDAO
{
    // Documents queries
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_document ) FROM document ";
    private static final String SQL_QUERY_SELECT = " SELECT a.id_document, a.code_document_type, a.title, a.date_creation, " +
        " a.date_modification, a.xml_working_content, a.xml_validated_content, a.id_space , b.name , " +
        " a.id_state , c.name_key, d.name , a.summary, a.comment , a.date_validity_begin , a.date_validity_end , " +
        " a.xml_metadata , a.id_creator, a.accept_site_comments, a.is_moderated_comment, a.is_email_notified_comment, a.id_mailinglist, " +
        " a.id_page_template_document " +
        " FROM document a, document_space b, document_workflow_state c, document_type d" +
        " WHERE a.id_space = b.id_space AND a.id_state = c.id_state AND " +
        " a.code_document_type = d.code_document_type AND a.id_document = ?  ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO document ( id_document, code_document_type, title, date_creation, " +
        " date_modification, xml_working_content, xml_validated_content, id_space, id_state	, summary, comment , " +
        " date_validity_begin , date_validity_end , xml_metadata , id_creator, accept_site_comments, is_moderated_comment , " +
        " is_email_notified_comment, id_mailinglist, id_page_template_document ) " +
        " VALUES ( ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM document WHERE id_document = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE document SET id_document = ?, " +
        " code_document_type = ?, title = ?, date_creation = ?, date_modification = ?, xml_working_content = ?, " +
        " xml_validated_content = ?, id_space = ?, id_state = ? , summary = ?, comment = ? , date_validity_begin = ? , date_validity_end = ? , " +
        " xml_metadata = ? , id_creator = ?, accept_site_comments = ?, is_moderated_comment = ? , is_email_notified_comment = ?, " +
        " id_mailinglist = ?, id_page_template_document = ? " + " WHERE id_document = ?  ";
    private static final String SQL_QUERY_SELECT_BY_FILTER = " SELECT a.id_document, a.code_document_type, a.title, " +
        " a.date_creation, a.date_modification, a.xml_working_content, a.xml_validated_content, a.id_space , b.name , " +
        " a.id_state , c.name_key , d.name ,  a.summary, a.comment , a.date_validity_begin , a.date_validity_end , " +
        " a.xml_metadata , a.id_creator, a.accept_site_comments, a.is_moderated_comment, a.is_email_notified_comment, " +
        " a.id_mailinglist , a.id_page_template_document " +
        " FROM document a, document_space b, document_workflow_state c, document_type d " +
        " WHERE a.id_space = b.id_space AND a.id_state = c.id_state AND " +
        " a.code_document_type = d.code_document_type ";
    private static final String SQL_FILTER_DOCUMENT_TYPE = " AND a.code_document_type = ? ";
    private static final String SQL_FILTER_SPACE = " AND a.id_space = ? ";
    private static final String SQL_FILTER_STATE = " AND a.id_state = ? ";
    private static final String SQL_ORDER_BY_LAST_MODIFICATION = " ORDER BY a.date_modification DESC ";
    private static final String SQL_QUERY_DELETE_DOCUMENT_HISTORY = "DELETE FROM document_history WHERE id_document = ?  ";

    // Document attributes queries
    private static final String SQL_QUERY_SELECT_ATTRIBUTES = "SELECT c.id_document_attribute , c.code , c.code_attribute_type , " +
        "c.code_document_type , c.name, c.description, c.attribute_order, c.required, c.searchable , " +
        "b.text_value, b.binary_value, b.mime_type " +
        "FROM document a, document_content b, document_type_attributes c " +
        " WHERE a.code_document_type = c.code_document_type " + " AND a.id_document = b.id_document  " +
        " AND b.id_document_attribute = c.id_document_attribute " + " AND a.id_document = ? ";
    private static final String SQL_QUERY_INSERT_ATTRIBUTE = "INSERT INTO document_content (id_document ,  id_document_attribute , text_value , binary_value, mime_type ) VALUES ( ? , ? , ? , ? , ? )";
    private static final String SQL_QUERY_DELETE_ATTRIBUTES = "DELETE FROM document_content WHERE id_document = ?  ";

    // Resources queries
    private static final String SQL_QUERY_SELECT_DOCUMENT_SPECIFIC_RESOURCE = " SELECT binary_value , mime_type FROM document_content WHERE id_document = ? AND id_document_attribute = ? ";
    private static final String SQL_QUERY_SELECT_DOCUMENT_RESOURCE = "SELECT a.binary_value , a.mime_type FROM document_content a, document b, document_type c WHERE a.id_document = ? " +
        " AND a.id_document_attribute = c.thumbnail_attribute_id " + " AND a.id_document = b.id_document " +
        " AND b.code_document_type = c.code_document_type ";
    private static final String SQL_QUERY_CHECK_PUBLISHED_DOCUMENT = " SELECT id_document FROM document_published " +
        " WHERE id_document = ? ";
    private final static String SQL_QUERY_SELECT_PAGE_TEMPLATE_PATH = " SELECT page_template_path FROM document_page_template " +
        " " + " WHERE id_page_template_document =  ? ";
    private static final String SQL_QUERY_SELECTALL_CATEGORY = " SELECT a.id_category, a.name, a.description, a.icon_content, a.icon_mime_type FROM document_category a, document_category_link b WHERE a.id_category=b.id_category AND b.id_document = ? ORDER BY name";
    private static final String SQL_QUERY_DELETE_LINKS_DOCUMENT = " DELETE FROM document_category_link WHERE id_document = ? ";
    private static final String SQL_QUERY_INSERT_LINK_CATEGORY_DOCUMENT = " INSERT INTO document_category_link ( id_category, id_document ) VALUES ( ?, ? )";
    private static final String SQL_QUERY_SELECT_PUBLISHED_RELATED_CATEGORY = "SELECT DISTINCT a.id_document, a.code_document_type, a.title, a.date_creation, " +
        " a.date_modification, a.xml_working_content, a.xml_validated_content, a.id_space , b.name , " +
        " a.id_state , c.name_key, d.name , a.summary, a.comment , a.date_validity_begin , a.date_validity_end , " +
        " a.xml_metadata , a.id_creator, a.accept_site_comments, a.is_moderated_comment, a.is_email_notified_comment, a.id_mailinglist, " +
        " a.id_page_template_document " + " FROM document a " +
        " INNER JOIN document_space b ON a.id_space = b.id_space " +
        " INNER JOIN document_workflow_state c ON a.id_state = c.id_state " +
        " INNER JOIN document_type d ON a.code_document_type = d.code_document_type " +
        " INNER JOIN document_published e ON a.id_document = e.id_document " +
        " LEFT OUTER JOIN document_category_link f ON a.id_document = f.id_document " +
        " WHERE e.status = 0 AND f.id_category IN ( SELECT g.id_category FROM document_category_link g WHERE g.id_document = ?) ";

    /**
     * Generates a new primary key
     * @return The new primary key
     */
    public int newPrimaryKey(  )
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
     * @param document The document object
     */
    public void insert( Document document )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        daoUtil.setInt( 1, document.getId(  ) );
        daoUtil.setString( 2, document.getCodeDocumentType(  ) );
        daoUtil.setString( 3, document.getTitle(  ) );
        daoUtil.setTimestamp( 4, document.getDateCreation(  ) );
        daoUtil.setTimestamp( 5, document.getDateModification(  ) );
        daoUtil.setString( 6, document.getXmlWorkingContent(  ) );
        daoUtil.setString( 7, document.getXmlValidatedContent(  ) );
        daoUtil.setInt( 8, document.getSpaceId(  ) );
        daoUtil.setInt( 9, document.getStateId(  ) );
        daoUtil.setString( 10, document.getSummary(  ) );
        daoUtil.setString( 11, document.getComment(  ) );
        daoUtil.setTimestamp( 12, document.getDateValidityBegin(  ) );
        daoUtil.setTimestamp( 13, document.getDateValidityEnd(  ) );
        daoUtil.setString( 14, document.getXmlMetadata(  ) );
        daoUtil.setInt( 15, document.getCreatorId(  ) );
        daoUtil.setInt( 16, document.getAcceptSiteComments(  ) );
        daoUtil.setInt( 17, document.getIsModeratedComment(  ) );
        daoUtil.setInt( 18, document.getIsEmailNotifiedComment(  ) );
        daoUtil.setInt( 19, document.getMailingListId(  ) );
        daoUtil.setInt( 20, document.getPageTemplateDocumentId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
        insertAttributes( document );
        insertCategories( document.getCategories(  ), document.getId(  ) );
    }

    /**
     * Insert attributes
     * @param document The document object
     */
    private void insertAttributes( Document document )
    {
        List<DocumentAttribute> listAttributes = document.getAttributes(  );

        for ( DocumentAttribute attribute : listAttributes )
        {
            insertAttribute( document.getId(  ), attribute );
        }
    }

    /**
     *
     * @param nDocumentId the document identifier
     * @param attribute The DocumentAttribute object
     */
    private void insertAttribute( int nDocumentId, DocumentAttribute attribute )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_ATTRIBUTE );
        daoUtil.setInt( 1, nDocumentId );
        daoUtil.setInt( 2, attribute.getId(  ) );

        if ( attribute.isBinary(  ) )
        {
            // File attribute, save content type and data in the binary column 
            daoUtil.setString( 3, "" );
            daoUtil.setBytes( 4, attribute.getBinaryValue(  ) );
            daoUtil.setString( 5, attribute.getValueContentType(  ) );
        }
        else
        {
            // Text attribute, no content type and save data in the text column 
            daoUtil.setString( 3, attribute.getTextValue(  ) );

            byte[] _bytes = null;
            daoUtil.setBytes( 4, _bytes );
            daoUtil.setString( 5, "" );
        }

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of Document from the table
     *
     * @param nDocumentId The identifier of Document
     * @return the instance of the Document
     */
    public Document load( int nDocumentId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nDocumentId );
        daoUtil.executeQuery(  );

        Document document = null;

        if ( daoUtil.next(  ) )
        {
            document = new Document(  );
            document.setId( daoUtil.getInt( 1 ) );
            document.setCodeDocumentType( daoUtil.getString( 2 ) );
            document.setTitle( daoUtil.getString( 3 ) );
            document.setDateCreation( daoUtil.getTimestamp( 4 ) );
            document.setDateModification( daoUtil.getTimestamp( 5 ) );
            document.setXmlWorkingContent( daoUtil.getString( 6 ) );
            document.setXmlValidatedContent( daoUtil.getString( 7 ) );
            document.setSpaceId( daoUtil.getInt( 8 ) );
            document.setSpace( daoUtil.getString( 9 ) );
            document.setStateId( daoUtil.getInt( 10 ) );
            document.setStateKey( daoUtil.getString( 11 ) );
            document.setType( daoUtil.getString( 12 ) );
            document.setSummary( daoUtil.getString( 13 ) );
            document.setComment( daoUtil.getString( 14 ) );
            document.setDateValidityBegin( daoUtil.getTimestamp( 15 ) );
            document.setDateValidityEnd( daoUtil.getTimestamp( 16 ) );
            document.setXmlMetadata( daoUtil.getString( 17 ) );
            document.setCreatorId( daoUtil.getInt( 18 ) );
            document.setAcceptSiteComments( daoUtil.getInt( 19 ) );
            document.setIsModeratedComment( daoUtil.getInt( 20 ) );
            document.setIsEmailNotifiedComment( daoUtil.getInt( 21 ) );
            document.setMailingListId( daoUtil.getInt( 22 ) );
            document.setPageTemplateDocumentId( daoUtil.getInt( 23 ) );
        }

        daoUtil.free(  );

        if ( document != null )
        {
            loadAttributes( document );
            document.setCategories( selectCategories( document.getId(  ) ) );
        }

        return document;
    }

    public void loadAttributes( Document document )
    {
        List<DocumentAttribute> listAttributes = new ArrayList<DocumentAttribute>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ATTRIBUTES );
        daoUtil.setInt( 1, document.getId(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DocumentAttribute attribute = new DocumentAttribute(  );
            attribute.setId( daoUtil.getInt( 1 ) );
            attribute.setCode( daoUtil.getString( 2 ) );
            attribute.setCodeAttributeType( daoUtil.getString( 3 ) );
            attribute.setCodeDocumentType( daoUtil.getString( 4 ) );
            attribute.setName( daoUtil.getString( 5 ) );
            attribute.setDescription( daoUtil.getString( 6 ) );
            attribute.setAttributeOrder( daoUtil.getInt( 7 ) );
            attribute.setRequired( daoUtil.getInt( 8 ) != 0 );
            attribute.setSearchable( daoUtil.getInt( 9 ) != 0 );

            String strContentType = daoUtil.getString( 12 );

            if ( ( strContentType != null ) && ( !strContentType.equals( "" ) ) )
            {
                // File attribute
                attribute.setBinary( true );
                attribute.setBinaryValue( daoUtil.getBytes( 11 ) );
                attribute.setValueContentType( strContentType );
            }
            else
            {
                // Text attribute
                attribute.setBinary( false );
                attribute.setTextValue( daoUtil.getString( 10 ) );
                attribute.setValueContentType( "" );
            }

            listAttributes.add( attribute );
        }

        document.setAttributes( listAttributes );
        daoUtil.free(  );
    }

    /**
     * Delete a record from the table
     *
     * @param nDocumentId the document identifier
     */
    public void delete( int nDocumentId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nDocumentId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        // Delete attributes
        deleteAttributes( nDocumentId );
        // Delete categories
        deleteCategories( nDocumentId );
        // Delete history
        deleteHistory( nDocumentId );
    }

    /**
     * Delete a record from the table
     * @param nDocumentId The Document identifier
     */
    private void deleteAttributes( int nDocumentId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ATTRIBUTES );
        daoUtil.setInt( 1, nDocumentId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete a record from the table
     * @param nDocumentId The Document identifier
     */
    private void deleteHistory( int nDocumentId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_DOCUMENT_HISTORY );
        daoUtil.setInt( 1, nDocumentId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     *
     * @param document The reference of document
     * @param bUpdateContent the boolean
     */
    public void store( Document document, boolean bUpdateContent )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setInt( 1, document.getId(  ) );
        daoUtil.setString( 2, document.getCodeDocumentType(  ) );
        daoUtil.setString( 3, document.getTitle(  ) );
        daoUtil.setTimestamp( 4, document.getDateCreation(  ) );
        daoUtil.setTimestamp( 5, document.getDateModification(  ) );
        daoUtil.setString( 6, document.getXmlWorkingContent(  ) );
        daoUtil.setString( 7, document.getXmlValidatedContent(  ) );
        daoUtil.setInt( 8, document.getSpaceId(  ) );
        daoUtil.setInt( 9, document.getStateId(  ) );
        daoUtil.setString( 10, document.getSummary(  ) );
        daoUtil.setString( 11, document.getComment(  ) );
        daoUtil.setTimestamp( 12, document.getDateValidityBegin(  ) );
        daoUtil.setTimestamp( 13, document.getDateValidityEnd(  ) );
        daoUtil.setString( 14, document.getXmlMetadata(  ) );
        daoUtil.setInt( 15, document.getCreatorId(  ) );
        daoUtil.setInt( 16, document.getAcceptSiteComments(  ) );
        daoUtil.setInt( 17, document.getIsModeratedComment(  ) );
        daoUtil.setInt( 18, document.getIsEmailNotifiedComment(  ) );
        daoUtil.setInt( 19, document.getMailingListId(  ) );
        daoUtil.setInt( 20, document.getPageTemplateDocumentId(  ) );
        daoUtil.setInt( 21, document.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        if ( bUpdateContent )
        {
            deleteAttributes( document.getId(  ) );
            insertAttributes( document );
            deleteCategories( document.getId(  ) );
            insertCategories( document.getCategories(  ), document.getId(  ) );
        }
    }

    /**
     * Load the list of documents
     *
     * @return The Collection of the Documents
     * @param filter The DocumentFilter Object
     */
    public List<Document> selectByFilter( DocumentFilter filter )
    {
        List<Document> listDocuments = new ArrayList<Document>(  );
        String strSQL = SQL_QUERY_SELECT_BY_FILTER;
        strSQL += ( ( filter.containsDocumentTypeCriteria(  ) ) ? SQL_FILTER_DOCUMENT_TYPE : "" );
        strSQL += ( ( filter.containsSpaceCriteria(  ) ) ? SQL_FILTER_SPACE : "" );
        strSQL += ( ( filter.containsStateCriteria(  ) ) ? SQL_FILTER_STATE : "" );
        strSQL += SQL_ORDER_BY_LAST_MODIFICATION;

        DAOUtil daoUtil = new DAOUtil( strSQL );
        int nIndex = 1;

        if ( filter.containsDocumentTypeCriteria(  ) )
        {
            daoUtil.setString( nIndex, filter.getCodeDocumentType(  ) );
            nIndex++;
        }

        if ( filter.containsSpaceCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdSpace(  ) );
            nIndex++;
        }

        if ( filter.containsStateCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdState(  ) );
            nIndex++;
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Document document = new Document(  );
            document.setId( daoUtil.getInt( 1 ) );
            document.setCodeDocumentType( daoUtil.getString( 2 ) );
            document.setTitle( daoUtil.getString( 3 ) );
            document.setDateCreation( daoUtil.getTimestamp( 4 ) );
            document.setDateModification( daoUtil.getTimestamp( 5 ) );
            document.setXmlWorkingContent( daoUtil.getString( 6 ) );
            document.setXmlValidatedContent( daoUtil.getString( 7 ) );
            document.setSpaceId( daoUtil.getInt( 8 ) );
            document.setSpace( daoUtil.getString( 9 ) );
            document.setStateId( daoUtil.getInt( 10 ) );
            document.setStateKey( daoUtil.getString( 11 ) );
            document.setType( daoUtil.getString( 12 ) );
            document.setSummary( daoUtil.getString( 13 ) );
            document.setComment( daoUtil.getString( 14 ) );
            document.setDateValidityBegin( daoUtil.getTimestamp( 15 ) );
            document.setDateValidityEnd( daoUtil.getTimestamp( 16 ) );
            document.setXmlMetadata( daoUtil.getString( 17 ) );
            document.setCreatorId( daoUtil.getInt( 18 ) );
            document.setAcceptSiteComments( daoUtil.getInt( 19 ) );
            document.setIsModeratedComment( daoUtil.getInt( 20 ) );
            document.setIsEmailNotifiedComment( daoUtil.getInt( 21 ) );
            document.setMailingListId( daoUtil.getInt( 22 ) );
            document.setPageTemplateDocumentId( daoUtil.getInt( 23 ) );

            document.setCategories( selectCategories( document.getId(  ) ) );
            listDocuments.add( document );
        }

        daoUtil.free(  );

        return listDocuments;
    }

    /**
     * Load the list of published documents in relation with categories of specified document
     * @param document The document with the categories
     * @return The Collection of the Documents
     */
    public List<Document> selectPublishedByRelatedCategories( Document document )
    {
        List<Document> listDocument = new ArrayList<Document>(  );

        if ( ( document == null ) || ( document.getId(  ) <= 0 ) )
        {
            return listDocument;
        }

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PUBLISHED_RELATED_CATEGORY );
        daoUtil.setInt( 1, document.getId(  ) );
        daoUtil.executeQuery(  );

        Document returnDocument = null;

        while ( daoUtil.next(  ) )
        {
            returnDocument = new Document(  );
            returnDocument.setId( daoUtil.getInt( 1 ) );
            returnDocument.setCodeDocumentType( daoUtil.getString( 2 ) );
            returnDocument.setTitle( daoUtil.getString( 3 ) );
            returnDocument.setDateCreation( daoUtil.getTimestamp( 4 ) );
            returnDocument.setDateModification( daoUtil.getTimestamp( 5 ) );
            returnDocument.setXmlWorkingContent( daoUtil.getString( 6 ) );
            returnDocument.setXmlValidatedContent( daoUtil.getString( 7 ) );
            returnDocument.setSpaceId( daoUtil.getInt( 8 ) );
            returnDocument.setSpace( daoUtil.getString( 9 ) );
            returnDocument.setStateId( daoUtil.getInt( 10 ) );
            returnDocument.setStateKey( daoUtil.getString( 11 ) );
            returnDocument.setType( daoUtil.getString( 12 ) );
            returnDocument.setSummary( daoUtil.getString( 13 ) );
            returnDocument.setComment( daoUtil.getString( 14 ) );
            returnDocument.setDateValidityBegin( daoUtil.getTimestamp( 15 ) );
            returnDocument.setDateValidityEnd( daoUtil.getTimestamp( 16 ) );
            returnDocument.setXmlMetadata( daoUtil.getString( 17 ) );
            returnDocument.setCreatorId( daoUtil.getInt( 18 ) );
            returnDocument.setAcceptSiteComments( daoUtil.getInt( 19 ) );
            returnDocument.setIsModeratedComment( daoUtil.getInt( 20 ) );
            returnDocument.setIsEmailNotifiedComment( daoUtil.getInt( 21 ) );
            returnDocument.setMailingListId( daoUtil.getInt( 22 ) );
            returnDocument.setPageTemplateDocumentId( daoUtil.getInt( 23 ) );

            listDocument.add( returnDocument );

            if ( document != null )
            {
                loadAttributes( document );
                document.setCategories( selectCategories( document.getId(  ) ) );
            }
        }

        daoUtil.free(  );

        return listDocument;
    }

    /**
     * Load a resource (image, file, ...) corresponding to an attribute of a Document
     *
     * @param nDocumentId The Document Id
     * @return the instance of the DocumentResource
     */
    public DocumentResource loadResource( int nDocumentId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_DOCUMENT_RESOURCE );
        daoUtil.setInt( 1, nDocumentId );
        daoUtil.executeQuery(  );

        DocumentResource resource = null;

        if ( daoUtil.next(  ) )
        {
            resource = new DocumentResource(  );
            resource.setContent( daoUtil.getBytes( 1 ) );
            resource.setContentType( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return resource;
    }

    /**
     * Load a resource (image, file, ...) corresponding to an attribute of a Document
     *
     * @param nDocumentId The Document Id
     * @param nAttributeId The Attribute Id
     * @return the instance of the DocumentResource
     */
    public DocumentResource loadSpecificResource( int nDocumentId, int nAttributeId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_DOCUMENT_SPECIFIC_RESOURCE );
        daoUtil.setInt( 1, nDocumentId );
        daoUtil.setInt( 2, nAttributeId );
        daoUtil.executeQuery(  );

        DocumentResource resource = null;

        if ( daoUtil.next(  ) )
        {
            resource = new DocumentResource(  );
            resource.setContent( daoUtil.getBytes( 1 ) );
            resource.setContentType( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return resource;
    }

    /**
     * Gets all documents
     * @return the document list
     */
    public List<Document> selectAll(  )
    {
        List<Document> listDocuments = new ArrayList<Document>(  );
        String strSQL = SQL_QUERY_SELECT_BY_FILTER;

        DAOUtil daoUtil = new DAOUtil( strSQL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Document document = new Document(  );
            document.setId( daoUtil.getInt( 1 ) );
            document.setCodeDocumentType( daoUtil.getString( 2 ) );
            document.setTitle( daoUtil.getString( 3 ) );
            document.setDateCreation( daoUtil.getTimestamp( 4 ) );
            document.setDateModification( daoUtil.getTimestamp( 5 ) );
            document.setXmlWorkingContent( daoUtil.getString( 6 ) );
            document.setXmlValidatedContent( daoUtil.getString( 7 ) );
            document.setSpaceId( daoUtil.getInt( 8 ) );
            document.setSpace( daoUtil.getString( 9 ) );
            document.setStateId( daoUtil.getInt( 10 ) );
            document.setStateKey( daoUtil.getString( 11 ) );
            document.setType( daoUtil.getString( 12 ) );
            document.setSummary( daoUtil.getString( 13 ) );
            document.setComment( daoUtil.getString( 14 ) );
            document.setDateValidityBegin( daoUtil.getTimestamp( 15 ) );
            document.setDateValidityEnd( daoUtil.getTimestamp( 16 ) );
            document.setXmlMetadata( daoUtil.getString( 17 ) );
            document.setCreatorId( daoUtil.getInt( 18 ) );
            document.setAcceptSiteComments( daoUtil.getInt( 19 ) );
            document.setIsModeratedComment( daoUtil.getInt( 20 ) );
            document.setIsEmailNotifiedComment( daoUtil.getInt( 21 ) );
            document.setMailingListId( daoUtil.getInt( 22 ) );
            document.setPageTemplateDocumentId( daoUtil.getInt( 23 ) );

            loadAttributes( document );
            document.setCategories( selectCategories( document.getId(  ) ) );
            listDocuments.add( document );
        }

        daoUtil.free(  );

        return listDocuments;
    }

    /**
     * Tests before deleting if an document is published in a portlet
     * @param nDocumentId The identifier of the document
     * @return true if the document is already published, false otherwise
     */
    public boolean documentIsPublished( int nDocumentId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_PUBLISHED_DOCUMENT );

        daoUtil.setInt( 1, nDocumentId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return true;
        }

        daoUtil.free(  );

        return false;
    }

    public String getPageTemplateDocumentPath( int IdPageTemplateDocument )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PAGE_TEMPLATE_PATH );
        daoUtil.setInt( 1, IdPageTemplateDocument );
        daoUtil.executeQuery(  );

        String strPageTemplatePath = "";

        if ( daoUtil.next(  ) )
        {
            strPageTemplatePath = daoUtil.getString( 1 );
        }

        daoUtil.free(  );

        return strPageTemplatePath;
    }

    /**
     * Select a list of Category for a specified Document id
     * @param nIdDocument The document Id
     * @return The Collection of Category (empty collection is no result)
     */
    private List<Category> selectCategories( int nIdDocument )
    {
        int nParam;
        List<Category> listCategory = new ArrayList<Category>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_CATEGORY );
        daoUtil.setInt( 1, nIdDocument );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            nParam = 0;

            Category category = new Category(  );
            category.setId( daoUtil.getInt( ++nParam ) );
            category.setName( daoUtil.getString( ++nParam ) );
            category.setDescription( daoUtil.getString( ++nParam ) );
            category.setIconContent( daoUtil.getBytes( ++nParam ) );
            category.setIconMimeType( daoUtil.getString( ++nParam ) );

            listCategory.add( category );
        }

        daoUtil.free(  );

        return listCategory;
    }

    /**
     * Insert links between Category and id document
     * @param listCategory The list of Category
     * @param nIdDocument The id of document
     *
     */
    private void insertCategories( List<Category> listCategory, int nIdDocument )
    {
        if ( listCategory != null )
        {
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_LINK_CATEGORY_DOCUMENT );

            for ( Category category : listCategory )
            {
                daoUtil.setInt( 1, category.getId(  ) );
                daoUtil.setInt( 2, nIdDocument );
                daoUtil.executeUpdate(  );
            }

            daoUtil.free(  );
        }
    }

    /**
     * Delete all links for a document
     * @param nIdDocument The identifier of the object Document
     */
    private void deleteCategories( int nIdDocument )
    {
        int nParam = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_LINKS_DOCUMENT );
        daoUtil.setInt( ++nParam, nIdDocument );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
