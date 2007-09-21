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

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;
import java.util.Locale;


/**
 * This class provides instances management methods (create, find, ...) for Document objects
 */
public final class DocumentHome
{
    // Static variable pointed at the DAO instance
    private static IDocumentDAO _dao = (IDocumentDAO) SpringContextService.getPluginBean( "document", "documentDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DocumentHome(  )
    {
    }

    /**
     * Creation of an instance of document
     *
     * @param document The instance of the document which contains the informations to store
     * @return The  instance of document which has been created with its primary key.
     */
    public static Document create( Document document )
    {
        _dao.insert( document );

        return document;
    }

    /**
     * Update of the document which is specified in parameter
     *
     * @return The instance of the  document which has been updated
     * @param bUpdateContent
     * @param document The instance of the document which contains the data to store
     */
    public static Document update( Document document, boolean bUpdateContent )
    {
        _dao.store( document, bUpdateContent );

        return document;
    }

    /**
     * Remove the Document whose identifier is specified in parameter
     *
     * @param nDocumentId
     */
    public static void remove( int nDocumentId )
    {
        _dao.delete( nDocumentId );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a document whose identifier is specified in parameter
     *
     * @param nKey The Primary key of the document
     * @return An instance of document
     */
    public static Document findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }

    /**
     * Returns a collection of documents objects
     * @return A collection of documents
     * @param filter
     * @param locale
     */
    public static List<Document> findByFilter( DocumentFilter filter, Locale locale )
    {
        List<Document> listDocuments = _dao.selectByFilter( filter );

        return (List) I18nService.localizeCollection( listDocuments, locale );
    }
    
    /**
     * Returns a collection of documents objects
     * If more than one category is specified on filter, 
     * the result will corresponding to the document wich matched with one category at least.
     * @return A collection of documents
     * @param filter
     * @param locale
     */
    public static List<Document> findPublishedByRelatedCategories( Document document, Locale locale )
    {
        List<Document> listDocuments = _dao.selectPublishedByRelatedCategories( document );

        return (List) I18nService.localizeCollection( listDocuments, locale );
    }

    /**
     *
     * @param nDocumentId
     * @param nAttributeId
     * @return
     */
    public static DocumentResource getResource( int nDocumentId, int nAttributeId )
    {
        return _dao.loadSpecificResource( nDocumentId, nAttributeId );
    }

    /**
     *
     * @param nDocumentId
     * @return
     */
    public static DocumentResource getResource( int nDocumentId )
    {
        return _dao.loadResource( nDocumentId );
    }

    /**
     *
     * @return
     */
    public static int newPrimaryKey(  )
    {
        return _dao.newPrimaryKey(  );
    }

    /**
     *
     * @return
     */
    public static List<Document> findAll(  )
    {
        return _dao.selectAll(  );
    }

    /**
      * Checks if the document is published on a portlet
      *
      * @param nDocumentId the document identifier
      * @return the result of check
      */
    public static boolean documentIsPublished( int nDocumentId )
    {
        return _dao.documentIsPublished( nDocumentId );
    }

    /**
     * Load document attributes
     * @param document the document reference
     */
    public static void loadAttributes( Document document )
    {
        _dao.loadAttributes( document );
    }
    
    /**
     * Load document pageTemplatePath
     * @param IdPageTemplateDocument the Id page template identifier
     */    
    public static String getPageTemplateDocumentPath( int IdPageTemplateDocument )
    {
        return _dao.getPageTemplateDocumentPath( IdPageTemplateDocument );
    }
}
