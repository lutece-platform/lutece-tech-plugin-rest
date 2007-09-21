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
import fr.paris.lutece.portal.business.portlet.IPortletInterfaceDAO;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;


/**
 * This class provides instances management methods for ArticlesListPortlet
 * objects
 */
public class DocumentListPortletHome extends PortletHome
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    // Static variable pointed at the DAO instance
    private static IDocumentListPortletDAO _dao = (IDocumentListPortletDAO) SpringContextService.getPluginBean( "document",
            "documentListPortletDAO" );

    /* This class implements the Singleton design pattern. */
    private static DocumentListPortletHome _singleton = null;

    /**
     * Constructor
     */
    public DocumentListPortletHome(  )
    {
        if ( _singleton == null )
        {
            _singleton = this;
        }
    }

    /**
     * Returns the instance of DocumentListPortletHome
     *
     * @return the DocumentListPortletHome instance
     */
    public static PortletHome getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new DocumentListPortletHome(  );
        }

        return _singleton;
    }

    /**
     * Returns the identifier of the portlet type
     *
     * @return the portlet type identifier
     */
    public String getPortletTypeId(  )
    {
        String strCurrentClassName = this.getClass(  ).getName(  );
        String strPortletTypeId = PortletTypeHome.getPortletTypeId( strCurrentClassName );

        return strPortletTypeId;
    }

    /**
     * Returns the instance of the portlet DAO singleton
     *
     * @return the instance of the DAO singleton
     */
    public IPortletInterfaceDAO getDAO(  )
    {
        return _dao;
    }

    /**
     * Returns an instance of a documentListPortlet whose identifier is specified in parameter
     *
     * @param nDocumentId The documenr identifier
     * @param nPortletId the portlet identifier
     * @return An instance of document
     */
    public static DocumentListPortlet getDocument( int nDocumentId, int nPortletId )
    {
        return _dao.selectDocument( nDocumentId, nPortletId );
    }

    /**
     * Returns a collection of documentTypes objects
     * @param strCodeDocumentType the code
     * @return A collection of documentTypes
     */
    public static Collection findByCodeDocumentType( int nDocumentId, String strCodeDocumentType )
    {
        return _dao.selectDocumentTypeListByCode( nDocumentId, strCodeDocumentType );
    }

    /**
     * Returns a list of couple id_portlet/name filtered by documentType and category
     * @param strCodeDocumentType the code
     * @return A collection of documentTypes
     */
    public static Collection findByCodeDocumentTypeAndCategory( int nDocumentId, String strCodeDocumentType )
    {
        return _dao.selectDocumentTypeListByCodeAndCategory( nDocumentId, strCodeDocumentType );
    }

    /**
     * Loads the list of the portlets whose type is the same as the one specified in parameter
     *
     * @param strDocumentId the document identifier
     * @return the list of the portlets in form of a ReferenceList
     */
    public static Collection getPortletsListbyDocumentId( String strDocumentId )
    {
        return _dao.selectPortletsListByDocumentId( strDocumentId );
    }

    /**
     * Checks if the document is not yet published into the portlet
     *
     * @param nDocumentId the article identifier
     * @param nPortletId the portlet identifier
     * @return the result of check
     */
    public static boolean checkPublishingIntoPortlet( int nDocumentId, int nPortletId )
    {
        return _dao.checkPublishingIntoPortlet( nDocumentId, nPortletId );
    }

    /**
     * Checks if the document is not yet assigned into the portlet
     *
     * @param nDocumentId the document identifier
     * @param nPortletId the portlet identifier
     * @return the result of check
     */
    public static boolean checkAssignIntoPortlet( int nDocumentId, int nPortletId )
    {
        return _dao.checkAssignIntoPortlet( nDocumentId, nPortletId );
    }

    /**
     * Insert the relationship between an document and a portlet for publishing
     *
     * @param nDocumentId the document identifier
     * @param nPortletId the portlet identifier
     */
    public static void insertIntoPortlet( int nDocumentId, int nPortletId, int nStatus )
    {
        _dao.insertIntoPortlet( nDocumentId, nPortletId, nStatus );
    }

    /**
     * Publishing documents assigned to a portlet
     *
     * @param document the DocumentListPortlet object
     * @param nPortletId the portlet identifier
     * @param nStatus the publishong status : 1 for unpublished - 0 for published
     */
    public static void publishingDocument( int nDocumentId, int nPortletId, int nStatus )
    {
        _dao.publishedIntoPortlet( nDocumentId, nPortletId, nStatus );
    }

    /**
     * UnPublishing documents assigned to a portlet
     *
     * @param nDocumentId the DocumentListPortlet identifier
     * @param nPortletId the portlet identifier
     * @param nStatus the publishong status : 0 for unpublished - 1 for published
     */
    public static void unPublishingDocument( int nDocumentId, int nPortletId, int nStatus )
    {
        _dao.unPublishedIntoPortlet( nDocumentId, nPortletId, nStatus );
    }

    /**
     * Delete the relationship between an document and a portlet
     *
     * @param nDocumentId the document identifier
     * @param nPortletId the portlet identifier
     */
    public static void deleteFromPortlet( int nDocumentId, int nPortletId )
    {
        _dao.deleteFromPortlet( nDocumentId, nPortletId );
    }

    /**
     * Returns a Collection of Documents objects from a portlet identifier
     *
     * @param nPortletId the Portlet identifier
     * @return a list of Articles objects
     */
    public static Collection<Document> findByPortlet( int nPortletId )
    {
        return _dao.selectByPortlet( nPortletId );
    }

    /**
     * Returns a Collection of Documents objects from a portlet identifier
     *
     * @param nPortletId the Portlet identifier
     * @return a list of Articles objects
     */
    public static Collection<Document> findDocumentsByPortlet( int nPortletId )
    {
        return _dao.selectDocumentsByPortlet( nPortletId );
    }

    /**
     * Loads the list of the docuemnts whose type is the same as the one specified in parameter
     *
     * @param nPortletId the portlet  identifier
     * @return the list of the document in form of a List
     */
    public static Collection<Document> getPublishedDocumentsList( int nPortletId )
    {
        return _dao.selectPublishedDocumentsList( nPortletId );
    }

    /**
     * Loads the list of the docuemnts whose type is the same as the one specified in parameter
     *
     * @param nPortletId the portlet  identifier
     * @return the list of the document in form of a List
     */
    public static Collection getAssignedDocumentsList( int nPortletId )
    {
        return _dao.selectAssignedDocumentsList( nPortletId );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Document Order management

    /**
     * Search the max order number of documents
     * @return int the max order
     */
    public static int getMaxOrderDocument( int nPortletId )
    {
        return _dao.maxOrderDocumentList( nPortletId );
    }

    /**
     * Search the order number of documents
     *
     * @param nDocumentOrder the number of order of the document
     * @param nPortletId the portlet identifier
     * @return The document identifier
     */
    public static int getIdByOrder( int nDocumentOrder, int nPortletId )
    {
        return _dao.selectIdByOrder( nDocumentOrder, nPortletId );
    }

    /**
     * Update the number order of documents
     *
     * @param nNewOrder the new number of order
     * @param nDocumentId the Identifier of documents
     * @param nPortletId the Identifier of portlet
     */
    public static void getModifyDocumentOrder( int nNewOrder, int nDocumentId, int nPortletId )
    {
        _dao.getModifyDocumentOrder( nNewOrder, nDocumentId, nPortletId );
    }

    public static boolean checkIsAliasPortlet( int nPortletId )
    {
        return _dao.checkIsAliasPortlet( nPortletId );
    }
}
