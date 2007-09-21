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

import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;
import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for
 *DocumentComment objects
 */
public final class DocumentCommentHome
{
    // Static variable pointed at the DAO instance
    private static IDocumentCommentDAO _dao = (IDocumentCommentDAO) SpringContextService.getPluginBean( "document",
            "documentCommentDAO" );

    /**
     * Private constructor - this class needs not be instantiated.
     */
    private DocumentCommentHome(  )
    {
    }

    /**
     * Create an instance of the DocumentComment class
     *
     * @param documentComment the object to insert into the database
     * @return the instance created with its primary key
     */
    public static DocumentComment create( DocumentComment documentComment )
    {
        _dao.insert( documentComment );

        return documentComment;
    }

    /**
     * Update of the document comment data specified in parameter
     * @param documentComment the instance of class which contains the data to store
     */
    public static void update( DocumentComment documentComment )
    {
        _dao.store( documentComment );
    }

    /**
     * Remove from the master database the document comment whose identifier is
     * specified in parameter
     * @param nDocumentCommentId the article identifier
     */
    public static void remove( int nDocumentCommentId )
    {
        _dao.delete( nDocumentCommentId );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of an document comment whose identifier is specified
     * in parameter
     * @param nDocumentCommentId the document comment primary key
     * @return the instance of the class DocumentComment
     */
    public static DocumentComment findByPrimaryKey( int nDocumentCommentId )
    {
        DocumentComment documentComment = _dao.load( nDocumentCommentId );

        return documentComment;
    }

    /**
     * Returns all the comments on an document
     * @param nDocumentId the identifier of the document
     * @return the list of document comments
     */
    public static List<DocumentComment> findByDocument( int nDocumentId )
    {
        List<DocumentComment> documentComments = _dao.selectByDocument( nDocumentId, false );

        return documentComments;
    }

    /**
     * Returns all the published comments on an document
     * @param nDocumentId the identifier of the document
     * @return the list of document comments
     */
    public static List<DocumentComment> findPublishedByDocument( int nDocumentId )
    {
        List<DocumentComment> documentComments = _dao.selectByDocument( nDocumentId, true );

        return documentComments;
    }

    /**
     * Update status from the comment
     * @param nComment The Comment identifier
     * @param nStatus The published Status
     */
    public static void updateCommentStatus( int nCommentId, int nStatus )
    {
        _dao.updateCommentStatus( nCommentId, nStatus );
    }
}
