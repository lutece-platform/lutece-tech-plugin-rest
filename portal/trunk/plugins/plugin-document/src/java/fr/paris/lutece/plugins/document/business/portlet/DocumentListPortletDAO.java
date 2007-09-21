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
package fr.paris.lutece.plugins.document.business.portlet;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.category.Category;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * This class provides Data Access methods for ArticlesListPortlet objects
 */
public final class DocumentListPortletDAO implements IDocumentListPortletDAO
{
    private static final String SQL_QUERY_INSERT = "INSERT INTO document_list_portlet ( id_portlet , code_document_type ) VALUES ( ? , ? )";
    private static final String SQL_QUERY_SELECT = "SELECT id_portlet , code_document_type FROM document_list_portlet WHERE id_portlet = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE document_list_portlet SET id_portlet = ?, code_document_type = ? WHERE id_portlet = ? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM document_list_portlet WHERE id_portlet= ? ";
    private static final String SQL_QUERY_DELETE_PUBLISHED_DOCUMENT_PORTLET = " DELETE FROM document_published WHERE id_portlet = ? ";
    private static final String SQL_QUERY_SELECT_DOCUMENTS_BY_TYPE = " SELECT b.id_portlet , a.name " +
        " FROM document_list_portlet b " +
        " LEFT JOIN document_published c ON b.id_portlet = c.id_portlet AND c.id_document= ? " +
        " INNER JOIN core_portlet a ON b.id_portlet = a.id_portlet " +
        " WHERE c.id_portlet IS NULL AND b.code_document_type = ? ";
    private static final String SQL_QUERY_SELECT_DOCUMENTS_BY_TYPE_AND_CATEGORY = "SELECT DISTINCT b.id_portlet , a.name " +
        "FROM document_list_portlet b " +
        "LEFT JOIN document_published c ON b.id_portlet = c.id_portlet AND c.id_document= ? " +
        "INNER JOIN core_portlet a ON b.id_portlet = a.id_portlet " +
        "LEFT OUTER JOIN document_category_list_portlet d ON b.id_portlet = d.id_portlet " +
        "WHERE c.id_portlet IS NULL AND b.code_document_type = ? AND (d.id_category IN (SELECT e.id_category " +
        "FROM document_category_link e WHERE e.id_document = ?) OR d.id_category IS NULL) ";
    private static final String SQL_QUERY_SELECT_PORTLETS_BY_DOCUMENT_ID = " SELECT  a.id_portlet, a.name , b.status FROM core_portlet a, document_published b  " +
        " WHERE b.id_portlet = a.id_portlet AND b.id_document = ? ORDER BY a.name";
    private static final String SQL_QUERY_SELECT_PUBLISHED_DOCUMENTS = " SELECT  a.id_document, b.title, b.code_document_type , b.date_creation , b.date_modification, " +
        "b.summary , b.id_space, b.id_state, b.xml_working_content, b.xml_validated_content , b.date_validity_begin , b.date_validity_end FROM document_published a, document b  " +
        " WHERE b.id_document = a.id_document AND a.id_portlet = ? AND a.status = 0 ORDER BY b.title";
    private static final String SQL_QUERY_SELECT_ASSIGNED_DOCUMENTS = " SELECT  a.id_document, b.title FROM document_published a, document b  " +
        " WHERE b.id_document = a.id_document AND a.id_portlet = ? AND a.status = 1 ORDER BY b.title";
    private static final String SQL_QUERY_CHECK_ASSIGNED_DOCUMENT = " SELECT id_document FROM document_published " +
        " WHERE id_document = ? and id_portlet = ?";
    private static final String SQL_QUERY_CHECK_PUBLISHED_DOCUMENT = " SELECT id_document FROM document_published " +
        " WHERE id_document = ? and id_portlet = ? AND status = 0 ";
    private static final String SQL_QUERY_SELECT_DOCUMENT_PUBLISHED = " SELECT id_portlet , id_document , document_order , status FROM document_published WHERE id_portlet = ? AND id_document = ? ";
    private static final String SQL_QUERY_INSERT_INTO_PORTLET = " INSERT INTO document_published ( id_document , id_portlet , status , document_order ) VALUES ( ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE_FROM_PORTLET = " DELETE FROM document_published WHERE id_document = ?  AND id_portlet = ? ";
    private static final String SQL_QUERY_PUBLISHED_INTO_PORTLET = " UPDATE document_published SET status = ? , document_order = ? WHERE id_document = ? AND id_portlet = ? ";
    private static final String SQL_QUERY_UNPUBLISHED_INTO_PORTLET = " UPDATE document_published SET status = ? , document_order = NULL WHERE id_document = ? AND id_portlet = ? ";
    private static final String SQL_QUERY_SELECT_BY_PORTLET = " SELECT DISTINCT a.id_document , a.code_document_type , a.date_creation , a.date_modification, " +
        " a.title , a.summary , a.id_space, a.id_state, a.xml_working_content, a.xml_validated_content , a.date_validity_begin , a.date_validity_end, a.id_page_template_document, b.document_order  " +
        " FROM document a , document_published b " +
        " WHERE b.id_portlet = ? AND b.status = 0 AND a.id_document = b.id_document ORDER BY b.document_order ASC";
    private static final String SQL_QUERY_SELECT_DOCUMENTS_BY_PORTLET = " SELECT DISTINCT a.id_document , a.code_document_type , a.date_creation , a.date_modification, " +
        " a.title , a.id_space, a.date_validity_begin , a.date_validity_end, a.id_page_template_document , b.document_order   " +
        " FROM document a , document_published b " +
        " WHERE b.id_portlet = ? AND a.id_document = b.id_document ORDER BY b.document_order ASC";
    private static final String SQL_QUERY_MAX_ORDER = "SELECT max(document_order) FROM document_published WHERE id_portlet = ?  ";
    private static final String SQL_QUERY_MODIFY_ORDER_BY_ID = "SELECT id_document FROM document_published  WHERE document_order = ? AND id_portlet = ?";
    private static final String SQL_QUERY_MODIFY_ORDER = "UPDATE document_published SET document_order = ?  WHERE id_document = ? AND id_portlet = ?";
    private static final String SQL_QUERY_CHECK_IS_ALIAS = "SELECT id_alias FROM core_portlet_alias WHERE id_alias = ?";

    //Category
    private static final String SQL_QUERY_INSERT_CATEGORY_PORTLET = "INSERT INTO document_category_list_portlet ( id_portlet , id_category ) VALUES ( ? , ? )";
    private static final String SQL_QUERY_DELETE_CATEGORY_PORTLET = " DELETE FROM document_category_list_portlet WHERE id_portlet = ? ";
    private static final String SQL_QUERY_SELECT_CATEGORY_PORTLET = "SELECT id_category FROM document_category_list_portlet WHERE id_portlet = ? ";
    private static final String SQL_QUERY_SELECTALL_CATEGORY = " SELECT a.id_category, a.name, a.description, a.icon_content, a.icon_mime_type FROM document_category a, document_category_link b WHERE a.id_category=b.id_category AND b.id_document = ? ORDER BY name";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Insert a new record in the table portlet_articles_list
     *
     * @param portlet the instance of the Portlet object to insert
     */
    public void insert( Portlet portlet )
    {
        DocumentListPortlet p = (DocumentListPortlet) portlet;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        daoUtil.setInt( 1, p.getId(  ) );
        daoUtil.setString( 2, p.getDocumentTypeCode(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        insertCategory( portlet );
    }

    /**
     * Insert a list of category for a specified portlet
     * @param portlet the DocumentListPortlet to insert
     */
    private void insertCategory( Portlet portlet )
    {
        DocumentListPortlet p = (DocumentListPortlet) portlet;

        if ( p.getIdCategory(  ) != null )
        {
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_CATEGORY_PORTLET );

            for ( int nIdCategory : p.getIdCategory(  ) )
            {
                daoUtil.setInt( 1, p.getId(  ) );
                daoUtil.setInt( 2, nIdCategory );

                daoUtil.executeUpdate(  );
            }

            daoUtil.free(  );
        }
    }

    /**
    * Deletes records for a portlet identifier in the tables document_list_portlet, document_published,
    * document_category_list_portlet
    *
    * @param nPortletId the portlet identifier
    */
    public void delete( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        daoUtil = new DAOUtil( SQL_QUERY_DELETE_PUBLISHED_DOCUMENT_PORTLET );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        deleteCategories( nPortletId );
    }

    /**
     * Delete categories for the specified portlet
     * @param nPortletId The portlet identifier
     */
    private void deleteCategories( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_CATEGORY_PORTLET );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Loads the data of Document List Portlet whose identifier is specified in parameter
     *
     * @param nPortletId The Portlet identifier
     * @return theDocumentListPortlet object
     */
    public Portlet load( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        DocumentListPortlet portlet = new DocumentListPortlet(  );

        if ( daoUtil.next(  ) )
        {
            portlet.setId( daoUtil.getInt( 1 ) );
            portlet.setDocumentTypeCode( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        portlet.setIdCategory( loadCategories( nPortletId ) );

        return portlet;
    }

    /**
     * Load a list of Id categories
     * @param nPortletId
     * @return Array of categories
     */
    private int[] loadCategories( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CATEGORY_PORTLET );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        Collection<Integer> nListIdCategory = new ArrayList<Integer>(  );

        while ( daoUtil.next(  ) )
        {
            nListIdCategory.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        int[] nArrayIdCategory = new int[nListIdCategory.size(  )];
        int i = 0;

        for ( Integer nIdCategory : nListIdCategory )
        {
            nArrayIdCategory[i++] = nIdCategory.intValue(  );
        }

        return nArrayIdCategory;
    }

    /**
     * Update the record in the table
     *
     * @param portlet A portlet
     */
    public void store( Portlet portlet )
    {
        DocumentListPortlet p = (DocumentListPortlet) portlet;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setInt( 1, p.getId(  ) );
        daoUtil.setString( 2, p.getDocumentTypeCode(  ) );
        daoUtil.setInt( 3, p.getId(  ) );

        daoUtil.executeUpdate(  );

        daoUtil.free(  );

        deleteCategories( p.getId(  ) );
        insertCategory( p );
    }

    /**
     * Load the list of documentTypes
     * @param strCodeDocumentType The code
     * @return The Collection of the DocumentTypes
     */
    public Collection selectDocumentTypeListByCode( int nDocumentId, String strCodeDocumentType )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_DOCUMENTS_BY_TYPE );
        daoUtil.setInt( 1, nDocumentId );
        daoUtil.setString( 2, strCodeDocumentType );
        daoUtil.executeQuery(  );

        ReferenceList list = new ReferenceList(  );

        while ( daoUtil.next(  ) )
        {
            list.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Load the list of documentTypes
     * @param strCodeDocumentType The code
     * @return The Collection of the DocumentTypes
     */
    public Collection selectDocumentTypeListByCodeAndCategory( int nDocumentId, String strCodeDocumentType )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_DOCUMENTS_BY_TYPE_AND_CATEGORY );
        daoUtil.setInt( 1, nDocumentId );
        daoUtil.setString( 2, strCodeDocumentType );
        daoUtil.setInt( 3, nDocumentId );
        daoUtil.executeQuery(  );

        ReferenceList list = new ReferenceList(  );

        while ( daoUtil.next(  ) )
        {
            list.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Loads the list of the portlets whose type is the same as the one specified in parameter
     *
     * @param strDocumentId the document identifier
     * @return the list of the portlets in form of a List
     */
    public Collection selectPortletsListByDocumentId( String strDocumentId )
    {
        Collection portletList = new ArrayList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLETS_BY_DOCUMENT_ID );

        daoUtil.setInt( 1, Integer.parseInt( strDocumentId ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DocumentListPortlet documentPortlet = new DocumentListPortlet(  );

            documentPortlet.setId( daoUtil.getInt( 1 ) );
            documentPortlet.setName( daoUtil.getString( 2 ) );
            documentPortlet.setStatus( daoUtil.getInt( 3 ) );
            documentPortlet.setIdCategory( loadCategories( documentPortlet.getId(  ) ) );
            portletList.add( documentPortlet );
        }

        daoUtil.free(  );

        return portletList;
    }

    /**
     * Loads the list of the documents whose type is the same as the one specified in parameter
     *
     * @param nPortletId the portlet identifier
     * @return the list of the documents in form of a List
     */
    public Collection<Document> selectPublishedDocumentsList( int nPortletId )
    {
        Collection<Document> documentList = new ArrayList<Document>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PUBLISHED_DOCUMENTS );

        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Document document = new Document(  );

            document.setId( daoUtil.getInt( 1 ) );
            document.setTitle( daoUtil.getString( 2 ) );
            document.setCodeDocumentType( daoUtil.getString( 3 ) );
            document.setDateCreation( daoUtil.getTimestamp( 4 ) );
            document.setDateModification( daoUtil.getTimestamp( 5 ) );
            document.setSummary( daoUtil.getString( 6 ) );
            document.setSpaceId( daoUtil.getInt( 7 ) );
            document.setStateId( daoUtil.getInt( 8 ) );
            document.setXmlWorkingContent( daoUtil.getString( 9 ) );
            document.setXmlValidatedContent( daoUtil.getString( 10 ) );
            document.setDateValidityBegin( daoUtil.getTimestamp( 11 ) );
            document.setDateValidityEnd( daoUtil.getTimestamp( 12 ) );
            document.setCategories( selectCategories( document.getId(  ) ) );
            documentList.add( document );
        }

        daoUtil.free(  );

        return documentList;
    }

    /**
     * Load the list of documentTypes
     * @param nPortletId The portlet identifier
     * @return The Collection of the DocumentTypes
     */
    public Collection selectAssignedDocumentsList( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ASSIGNED_DOCUMENTS );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        ReferenceList list = new ReferenceList(  );

        while ( daoUtil.next(  ) )
        {
            list.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Tests before insert it, if an document is already published in a portlet
     *
     * @param nDocumentId The identifier of the document
     * @param nPortletId The identifier of the portlet
     * @return true if the article is already published, false otherwise
     */
    public boolean checkPublishingIntoPortlet( int nDocumentId, int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_PUBLISHED_DOCUMENT );

        daoUtil.setInt( 1, nDocumentId );
        daoUtil.setInt( 2, nPortletId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return true;
        }

        daoUtil.free(  );

        return false;
    }

    /**
     * Tests before insert it, if an article is already assigned in a portlet
     *
     * @param nDocumentId The identifier of the document
     * @param nPortletId The identifier of the portlet
     * @return true if the article is already published, false otherwise
     */
    public boolean checkAssignIntoPortlet( int nDocumentId, int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_ASSIGNED_DOCUMENT );

        daoUtil.setInt( 1, nDocumentId );
        daoUtil.setInt( 2, nPortletId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return true;
        }

        daoUtil.free(  );

        return false;
    }

    /**
     * Insert the documents whose identifier is specified in parameter in a
     * portlet for assignes
     *
     * @param nDocumentId the identifier of the document
     * @param nPortletId the identifier of the portlet
     */
    public void insertIntoPortlet( int nDocumentId, int nPortletId, int nStatus )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_INTO_PORTLET );

        daoUtil.setInt( 1, nDocumentId );
        daoUtil.setInt( 2, nPortletId );
        daoUtil.setInt( 3, nStatus );
        daoUtil.setString( 4, null );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Deletes an document assigned from a portlet
     *
     * @param nDocumentId the identifier of the document to delete
     * @param nPortletId the identifier of the portlet
     */
    public void deleteFromPortlet( int nDocumentId, int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_FROM_PORTLET );
        daoUtil.setInt( 1, nDocumentId );
        daoUtil.setInt( 2, nPortletId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Published the documents whose identifier is specified in parameter in a
     * portlet for publishing : set status to 0
     *
     * @param document the DocumentListPortlet object
     * @param nPortletId the identifier of the portlet
     * @param nStatus the publishing status : 1 for unpublished - 0 for published
     */
    public void publishedIntoPortlet( int nDocumentId, int nPortletId, int nStatus )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_PUBLISHED_INTO_PORTLET );
        // Update the publishing status
        daoUtil.setInt( 1, nStatus );
        daoUtil.setInt( 2, maxOrderDocumentList( nPortletId ) + 1 );
        daoUtil.setInt( 3, nDocumentId );
        daoUtil.setInt( 4, nPortletId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * UnPublished the documents whose identifier is specified in parameter in a
     * portlet for publishing : set status to 1 and order to -1
     *
     * @param document the DocumentListPortlet object
     * @param nPortletId the identifier of the portlet
     * @param nStatus the publishing status : 1 for unpublished - 0 for published
     */
    public void unPublishedIntoPortlet( int nDocumentId, int nPortletId, int nStatus )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UNPUBLISHED_INTO_PORTLET );
        // Update the publishing status
        daoUtil.setInt( 1, nStatus );
        daoUtil.setInt( 2, nDocumentId );
        daoUtil.setInt( 3, nPortletId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Calculate the new max order
     * @param nPortletId the portlet identifer
     * @return the max order of document
     */
    public int maxOrderDocumentList( int nPortletId )
    {
        int nOrder = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_MAX_ORDER );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nOrder = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nOrder;
    }

    /**
     * Returns all the documents of a portlet whose identifier is specified
     *
     * @param nPortletId the identifier of the portlet
     * @return the document list in form of a Collection object
     */
    public Collection<Document> selectByPortlet( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PORTLET );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        Collection<Document> list = new ArrayList<Document>(  );

        while ( daoUtil.next(  ) )
        {
            Document document = new Document(  );

            document.setId( daoUtil.getInt( 1 ) );
            document.setCodeDocumentType( daoUtil.getString( 2 ) );
            document.setDateCreation( daoUtil.getTimestamp( 3 ) );
            document.setDateModification( daoUtil.getTimestamp( 4 ) );
            document.setTitle( daoUtil.getString( 5 ) );
            document.setSummary( daoUtil.getString( 6 ) );
            document.setSpaceId( daoUtil.getInt( 7 ) );
            document.setStateId( daoUtil.getInt( 8 ) );
            document.setXmlWorkingContent( daoUtil.getString( 9 ) );
            document.setXmlValidatedContent( daoUtil.getString( 10 ) );
            document.setDateValidityBegin( daoUtil.getTimestamp( 11 ) );
            document.setDateValidityEnd( daoUtil.getTimestamp( 12 ) );
            document.setPageTemplateDocumentId( daoUtil.getInt( 13 ) );
            document.setCategories( selectCategories( document.getId(  ) ) );
            list.add( document );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Returns all the documents of a portlet whose identifier is specified
     *
     * @param nPortletId the identifier of the portlet
     * @return the document list in form of a Collection object
     */
    public Collection<Document> selectDocumentsByPortlet( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_DOCUMENTS_BY_PORTLET );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        Collection<Document> list = new ArrayList<Document>(  );

        while ( daoUtil.next(  ) )
        {
            Document document = new Document(  );

            document.setId( daoUtil.getInt( 1 ) );
            document.setCodeDocumentType( daoUtil.getString( 2 ) );
            document.setDateCreation( daoUtil.getTimestamp( 3 ) );
            document.setDateModification( daoUtil.getTimestamp( 4 ) );
            document.setTitle( daoUtil.getString( 5 ) );
            document.setSpaceId( daoUtil.getInt( 6 ) );
            document.setDateValidityBegin( daoUtil.getTimestamp( 7 ) );
            document.setDateValidityEnd( daoUtil.getTimestamp( 8 ) );
            document.setPageTemplateDocumentId( daoUtil.getInt( 9 ) );
            document.setCategories( selectCategories( document.getId(  ) ) );
            list.add( document );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Load the data of DocumentListPortlet from the table
     *
     * @param nDocumentId The identifier of DocumentListPortlet
     * @param nPortletId the identifier of portlet
     * @return the instance of the DocumentListPortlet
     */
    public DocumentListPortlet selectDocument( int nDocumentId, int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_DOCUMENT_PUBLISHED );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.setInt( 2, nDocumentId );
        daoUtil.executeQuery(  );

        DocumentListPortlet document = new DocumentListPortlet(  );

        if ( daoUtil.next(  ) )
        {
            document.setPortletId( daoUtil.getInt( 1 ) );
            document.setDocumentId( daoUtil.getInt( 2 ) );
            document.setDocumentOrder( daoUtil.getInt( 3 ) );
            document.setStatus( daoUtil.getInt( 4 ) );
            document.setIdCategory( loadCategories( document.getPortletId(  ) ) );
        }

        daoUtil.free(  );

        return document;
    }

    /**
     * Returns a document identifier in a distinct order
     *
     * @param nDocumentOrder The order number
     * @param nPortletId the portlet identifier
     * @return The order of the Document
     */
    public int selectIdByOrder( int nDocumentOrder, int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_MODIFY_ORDER_BY_ID );
        int nResult = nDocumentOrder;
        daoUtil.setInt( 1, nResult );
        daoUtil.setInt( 2, nPortletId );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            // If number order doesn't exist
            nResult = 1;
        }
        else
        {
            nResult = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nResult;
    }

    /**
     * Modify the order of a document
     *
     * @param nNewOrder The order number
     * @param nDocumentId The document identifier
     * @param nPortletId the portlet identifier
     */
    public void getModifyDocumentOrder( int nNewOrder, int nDocumentId, int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_MODIFY_ORDER );
        daoUtil.setInt( 1, nNewOrder );
        daoUtil.setInt( 2, nDocumentId );
        daoUtil.setInt( 3, nPortletId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Tests if is a portlet is portlet type alias
     *
     * @param nPortletId The identifier of the document
     * @return true if the portlet is alias, false otherwise
     */
    public boolean checkIsAliasPortlet( int nPortletId )
    {
        boolean bIsAlias = false;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_IS_ALIAS );

        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            bIsAlias = true;
        }

        daoUtil.free(  );

        return bIsAlias;
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
}
