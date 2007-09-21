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
package fr.paris.lutece.plugins.document.service;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.document.business.DocumentType;
import fr.paris.lutece.plugins.document.business.DocumentTypeHome;
import fr.paris.lutece.plugins.document.business.attributes.DocumentAttribute;
import fr.paris.lutece.plugins.document.business.workflow.DocumentAction;
import fr.paris.lutece.plugins.document.business.workflow.DocumentActionHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.rbac.RBACResource;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.xml.XmlUtil;

import java.sql.Timestamp;

import java.util.List;
import java.util.Locale;


/**
 * This Service manages document actions (create, move, delete, validate ...)
 * and notify listeners.
 */
public class DocumentService
{
    private static DocumentService _singleton = new DocumentService(  );
    private static final String TAG_DOCUMENT_TITLE = "document-title";
    private static final String TAG_DOCUMENT_SUMMARY = "document-summary";
    private static DocumentEventListernersManager _managerEventListeners;

    /** Creates a new instance of DocumentService */
    private DocumentService(  )
    {
        _managerEventListeners = (DocumentEventListernersManager) SpringContextService.getPluginBean( "document",
                "documentEventListernersManager" );
    }

    /**
     * Get the unique instance of the service
     *
     * @return The unique instance
     */
    public static DocumentService getInstance(  )
    {
        return _singleton;
    }

    /**
     * Build an XML document that contains document's data.
     *
     * @param document The document
     * @return An XML fragment containing document's data
     */
    String buildXmlContent( Document document )
    {
        StringBuffer sbXml = new StringBuffer(  );
        XmlUtil.beginElement( sbXml, document.getCodeDocumentType(  ) );

        XmlUtil.addElement( sbXml, TAG_DOCUMENT_TITLE, document.getTitle(  ) );
        XmlUtil.addElement( sbXml, TAG_DOCUMENT_SUMMARY, document.getSummary(  ) );

        for ( DocumentAttribute attribute : document.getAttributes(  ) )
        {
            AttributeManager manager = AttributeService.getInstance(  ).getManager( attribute.getCodeAttributeType(  ) );
            XmlUtil.addElement( sbXml, document.getCodeDocumentType(  ) + "-" + attribute.getCode(  ),
                manager.getAttributeXmlValue( document, attribute ) );
        }

        XmlUtil.endElement( sbXml, document.getCodeDocumentType(  ) );

        return sbXml.toString(  );
    }

    /**
     * Change the state of the document
     *
     * @param document The document
     * @param user The user doing the action
     * @param nStateId The new state Id
     * @throws DocumentException raise when error occurs in event or rule
     */
    public void changeDocumentState( Document document, AdminUser user, int nStateId )
        throws DocumentException
    {
        document.setStateId( nStateId );
        DocumentHome.update( document, false );

        notify( document.getId(  ), user, DocumentEvent.DOCUMENT_STATE_CHANGED );
    }

    /**
     * Create a new document
     *
     * @param document The document
     * @param user The user doing the action
     * @throws DocumentException raise when error occurs in event or rule
     */
    public void createDocument( Document document, AdminUser user )
        throws DocumentException
    {
        document.setId( DocumentHome.newPrimaryKey(  ) );
        document.setDateCreation( new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        document.setDateModification( new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        document.setXmlWorkingContent( buildXmlContent( document ) );

        DocumentHome.create( document );

        notify( document.getId(  ), user, DocumentEvent.DOCUMENT_CREATED );
    }

    /**
     * Modify a the content of a document
     *
     * @param document The document
     * @param user The user doing the action
     * @throws DocumentException raise when error occurs in event or rule
     */
    public void modifyDocument( Document document, AdminUser user )
        throws DocumentException
    {
        document.setDateModification( new Timestamp( new java.util.Date(  ).getTime(  ) ) );
        document.setXmlWorkingContent( buildXmlContent( document ) );
        DocumentHome.update( document, true );

        notify( document.getId(  ), user, DocumentEvent.DOCUMENT_CONTENT_MODIFIED );
    }

    /**
     * Validate a document
     * @param nStateId The new state id for a validated document
     * @param document The document
     * @param user The user doing the action
     * @throws DocumentException raise when error occurs in event or rule
     */
    public void validateDocument( Document document, AdminUser user, int nStateId )
        throws DocumentException
    {
        document.setStateId( nStateId );
        // Copy the working content into the validated content
        document.setXmlValidatedContent( document.getXmlWorkingContent(  ) );
        DocumentHome.update( document, false );

        notify( document.getId(  ), user, DocumentEvent.DOCUMENT_STATE_CHANGED );
    }

    /**
     * Archive a document
     * @param nStateId The new state id for a validated document
     * @param document The document
     * @param user The user doing the action
     * @throws DocumentException raise when error occurs in event or rule
     */
    public void archiveDocument( Document document, AdminUser user, int nStateId )
        throws DocumentException
    {
        document.setStateId( nStateId );
        DocumentHome.update( document, false );

        notify( document.getId(  ), user, DocumentEvent.DOCUMENT_STATE_CHANGED );
    }

    /**
     * Move a document from a space to another
     *
     * @param document The document
     * @param user The user doing the action
     * @param nNewSpace The Id of the destination space
     * @throws DocumentException raise when error occurs in event or rule
     */
    public void moveDocument( Document document, AdminUser user, int nNewSpace )
        throws DocumentException
    {
        document.setSpaceId( nNewSpace );
        DocumentHome.update( document, false );

        notify( document.getId(  ), user, DocumentEvent.DOCUMENT_MOVED );
    }

    /**
     * Notify an event to all listeners
     *
     * @param nDocumentId The document Id
     * @param user The user doing the action
     * @param nEventType The type of event
     * @throws DocumentException raise when error occurs in event or rule
     */
    private void notify( int nDocumentId, AdminUser user, int nEventType )
        throws DocumentException
    {
        // Reload document to have all data (ie : state_key !)
        Document document = DocumentHome.findByPrimaryKey( nDocumentId );
        DocumentEvent event = new DocumentEvent( document, user, nEventType );

        _managerEventListeners.notifyListeners( event );
    }

    /**
     * Add to the document all permitted actions according to the current user and
     * using the current locale
     * @param user The current user
     * @param document The document
     * @param locale The Locale
     */
    public void getActions( Document document, Locale locale, AdminUser user )
    {
        List<DocumentAction> listActions = DocumentActionHome.getActionsList( document, locale );
        RBACResource documentType = DocumentTypeHome.findByPrimaryKey( document.getCodeDocumentType(  ) );
        listActions = (List) RBACService.getAuthorizedActionsCollection( listActions, documentType, user );

        document.setActions( listActions );
    }

    /**
     * Build an HTML form for the document creation for a given document type
     *
     * @param strDocumentTypeCode The Document type code
     * @param locale The Locale
     *
     * @return The HTML form
     */
    public String getCreateForm( String strDocumentTypeCode, Locale locale )
    {
        StringBuffer sbForm = new StringBuffer(  );
        DocumentType documentType = DocumentTypeHome.findByPrimaryKey( strDocumentTypeCode );

        for ( DocumentAttribute attribute : documentType.getAttributes(  ) )
        {
            AttributeManager manager = AttributeService.getInstance(  ).getManager( attribute.getCodeAttributeType(  ) );
            sbForm.append( manager.getCreateFormHtml( attribute, locale ) );
        }

        return sbForm.toString(  );
    }

    /**
     * Build an HTML form for the document modification for a given document
     *
     * @param strDocumentId The Id of the document to modify
     * @param locale The Locale
     *
     * @return The HTML form
     */
    public String getModifyForm( String strDocumentId, Locale locale )
    {
        StringBuffer sbForm = new StringBuffer(  );
        Document document = DocumentHome.findByPrimaryKey( Integer.parseInt( strDocumentId ) );

        for ( DocumentAttribute attribute : document.getAttributes(  ) )
        {
            AttributeManager manager = AttributeService.getInstance(  ).getManager( attribute.getCodeAttributeType(  ) );
            sbForm.append( manager.getModifyFormHtml( attribute, locale ) );
        }

        return sbForm.toString(  );
    }
}
