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
package fr.paris.lutece.plugins.document.web.publishing;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.document.business.DocumentPageTemplate;
import fr.paris.lutece.plugins.document.business.DocumentPageTemplateHome;
import fr.paris.lutece.plugins.document.business.portlet.DocumentListPortlet;
import fr.paris.lutece.plugins.document.business.portlet.DocumentListPortletHome;
import fr.paris.lutece.plugins.document.service.publishing.PublishingService;
import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.service.page.PageResourceIdService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 *
 */
public class DocumentPublishingJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_DOCUMENT_MANAGEMENT = "DOCUMENT_MANAGEMENT";
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";
    private static final String PARAMETER_DOCUMENT_ID = "id_document";
    private static final String PARAMETER_PORTLET_ID = "id_portlet";
    private static final String PARAMETER_DOCUMENT_ORDER = "document_order";
    private static final String PARAMETER_PORTLET_LIST_IDS = "list_portlet_ids2";
    private static final String PARAMETER_DOCUMENT_PUBLISHED_STATUS = "status";
    private static final String MARK_DOCUMENT = "document";
    private static final String MARK_DOCUMENT_PUBLISHED = "document_published";
    private static final String MARK_DOCUMENT_PUBLISHED_STATUS = "status";
    private static final String MARK_PORTLET_LIST = "portlet_list";
    private static final String MARK_NEW_PORTLET_LIST = "new_portlet_list";
    private static final String MARK_ASSIGNED_PORTLET = "assigned_portlet_list";
    private static final String MARK_PORTLET = "portlet";
    private static final String MARK_ASSIGNED_DOCUMENT_LIST = "assigned_document_list";
    private static final String MARK_DOCUMENT_ORDER = "document_order";
    private static final String MARK_DOCUMENT_ORDER_LIST = "document_order_list";
    private static final String MARK_PUBLISHED_DOCUMENT_LIST = "published_document_list";
    private static final String MARK_PORTLET_ID = "portlet_id";
    private static final String MARK_PAGE_NAME = "page_name";
    private static final String MARK_DOCUMENT_PAGE_TEMPLATE_PICTURE = "page_template_picture";
    private static final String PROPERTY_PUBLISHING_SPACE_PAGE_TITLE = "document.assign.pageTitle";
    private static final String PROPERTY_MANAGE_PUBLISHING = "document.portlet.publishing.pageTitle";
    private static final String TEMPLATE_DOCUMENT_PUBLISHING = "/admin/plugins/document/publishing/manage_document_publishing.html";
    private static final String TEMPLATE_PORTLET_PUBLISHING = "/admin/plugins/document/publishing/manage_portlet_publishing.html";
    private static final String TEMPLATE_PUBLISHED_DOCUMENT_LIST = "/admin/plugins/document/publishing/published_document_list.html";
    private static final String TEMPLATE_ASSIGNED_DOCUMENT_LIST = "/admin/plugins/document/publishing/assigned_document_list.html";
    private static final String JSP_DOCUMENTS_ASSIGN = "ManageDocumentPublishing.jsp";
    private static final String JSP_DOCUMENTS_PUBLISHING = "ManagePublishing.jsp";

    /**
     * Returns the publish template management
     * @param request The Http request
     * @return the html code for display the modes list
     */
    public String getManageDocumentPublishing( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PUBLISHING_SPACE_PAGE_TITLE );

        String strDocumentId = request.getParameter( PARAMETER_DOCUMENT_ID );

        Document document = DocumentHome.findByPrimaryKey( Integer.parseInt( strDocumentId ) );

        Collection listPortlet = DocumentListPortletHome.findByCodeDocumentTypeAndCategory( Integer.parseInt( 
                    strDocumentId ), document.getCodeDocumentType(  ) );
        listPortlet = RBACService.getAuthorizedReferenceList( (ReferenceList) listPortlet, PortletType.RESOURCE_TYPE,
                PageResourceIdService.PERMISSION_MANAGE, getUser(  ) );

        Collection listPortletNew = new ArrayList(  );
        Collection listAssignedPortlet = DocumentListPortletHome.getPortletsListbyDocumentId( strDocumentId );

        HashMap model = new HashMap(  );
        model.put( MARK_DOCUMENT, document );
        model.put( MARK_PORTLET_LIST, listPortlet );
        model.put( MARK_NEW_PORTLET_LIST, listPortletNew );
        model.put( MARK_ASSIGNED_PORTLET, listAssignedPortlet );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DOCUMENT_PUBLISHING, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the publishing article
     * @param request requete Http
     * @return The Jsp URL of the process result
     */
    public String doAssignedDocument( HttpServletRequest request )
    {
        // Recovery of parameters processing
        int nDocumentId = Integer.parseInt( request.getParameter( PARAMETER_DOCUMENT_ID ) );
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );

        //retrieve the selected portlets ids
        String[] arrayPortletIds = request.getParameterValues( PARAMETER_PORTLET_LIST_IDS );

        if ( ( arrayPortletIds != null ) || ( strPortletId != null ) )
        {
            if ( strPortletId == null )
            {
                for ( int i = 0; i < arrayPortletIds.length; i++ )
                {
                    int nPortletId = Integer.parseInt( arrayPortletIds[i] );
                    int nStatus = Integer.parseInt( request.getParameter( PARAMETER_DOCUMENT_PUBLISHED_STATUS ) );

                    if ( !DocumentListPortletHome.checkAssignIntoPortlet( nDocumentId, nPortletId ) )
                    {
                        // Publishing of document : if status = 1, the document is assigned, otherwize is assigned AND published
                        DocumentListPortletHome.insertIntoPortlet( nDocumentId, nPortletId, nStatus );

                        if ( nStatus == 0 )
                        {
                            PublishingService.getInstance(  ).publish( nDocumentId, nPortletId );
                        }
                    }
                }
            }
            else
            {
                int nIdPortlet = Integer.parseInt( strPortletId );
                PublishingService.getInstance(  ).publish( nDocumentId, nIdPortlet );
            }
        }

        // Display the page of publishing
        return getUrlAssignedPage( nDocumentId );
    }

    /**
     * Process of unselecting the article of publishing
     *
     * @param request requete Http
     * @return The Jsp URL of the process result
     */
    public String doUnAssignedDocument( HttpServletRequest request )
    {
        // Recovery of parameters processing
        int nDocumentId = Integer.parseInt( request.getParameter( PARAMETER_DOCUMENT_ID ) );
        int nPortletId = Integer.parseInt( request.getParameter( PARAMETER_PORTLET_ID ) );
        int nStatus = Integer.parseInt( request.getParameter( PARAMETER_DOCUMENT_PUBLISHED_STATUS ) );

        // Remove the document assigned
        if ( nStatus != 0 )
        {
            DocumentListPortletHome.deleteFromPortlet( nDocumentId, nPortletId );
        }
        else
        {
            PublishingService.getInstance(  ).unpublish( nDocumentId, nPortletId );
        }

        // Display the page of publishing
        return getUrlAssignedPage( nDocumentId );
    }

    /**
     * Returns the portlet document template management
     *
     * @param request The Http request
     * @return the html code
     */
    public String getPublishingManagement( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_MANAGE_PUBLISHING );

        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );

        DocumentListPortlet portlet = (DocumentListPortlet) DocumentListPortletHome.findByPrimaryKey( nPortletId );

        HashMap model = new HashMap(  );
        model.put( MARK_PORTLET, portlet );

        Page page = PageHome.findByPrimaryKey( portlet.getPageId(  ) );
        String strPageName = page.getName(  );
        model.put( MARK_PAGE_NAME, strPageName );

        StringBuffer strPublishedDocumentsRow = new StringBuffer(  );

        // Scan of the list
        for ( Document document : DocumentListPortletHome.findDocumentsByPortlet( nPortletId ) )
        {
            strPublishedDocumentsRow.append( getPublishedDocumentsList( document, nPortletId ) );
        }

        model.put( MARK_PUBLISHED_DOCUMENT_LIST, strPublishedDocumentsRow );

        StringBuffer strAssignedDocumentsRow = new StringBuffer(  );

        // Scan of the list
        for ( Document document : DocumentListPortletHome.findDocumentsByPortlet( nPortletId ) )
        {
            strAssignedDocumentsRow.append( getAssignedDocumentsList( document, nPortletId ) );
        }

        model.put( MARK_ASSIGNED_DOCUMENT_LIST, strAssignedDocumentsRow );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PORTLET_PUBLISHING, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the publishing article
     * @param request requete Http
     * @return The Jsp URL of the process result
     */
    public String doPublishingDocument( HttpServletRequest request )
    {
        // Recovery of parameters processing
        int nDocumentId = Integer.parseInt( request.getParameter( PARAMETER_DOCUMENT_ID ) );
        int nPortletId = Integer.parseInt( request.getParameter( PARAMETER_PORTLET_ID ) );

        PublishingService.getInstance(  ).publish( nDocumentId, nPortletId );

        // Display the page of publishing
        return getUrlPublishedPage( nPortletId, nDocumentId );
    }

    /**
     * Process of unselecting the article of publishing
     *
     * @param request requete Http
     * @return The Jsp URL of the process result
     */
    public String doUnPublishingDocument( HttpServletRequest request )
    {
        // Recovery of parameters processing
        int nDocumentId = Integer.parseInt( request.getParameter( PARAMETER_DOCUMENT_ID ) );
        int nPortletId = Integer.parseInt( request.getParameter( PARAMETER_PORTLET_ID ) );
        DocumentListPortlet document = DocumentListPortletHome.getDocument( nDocumentId, nPortletId );
        int nOrder = document.getDocumentOrder(  );
        int nNewOrder = DocumentListPortletHome.getMaxOrderDocument( nPortletId );
        getModifyDocumentOrder( nDocumentId, nPortletId, nOrder, nNewOrder );
        PublishingService.getInstance(  ).unpublish( nDocumentId, nPortletId );

        // Display the page of publishing
        return getUrlPublishedPage( nPortletId, nDocumentId );
    }

    /**
     * Modifies the order in the list of contacts
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doModifyDocumentOrder( HttpServletRequest request )
    {
        int nDocumentId = Integer.parseInt( request.getParameter( PARAMETER_DOCUMENT_ID ) );
        int nPortletId = Integer.parseInt( request.getParameter( PARAMETER_PORTLET_ID ) );
        DocumentListPortlet document = DocumentListPortletHome.getDocument( nDocumentId, nPortletId );
        int nOrder = document.getDocumentOrder(  );
        int nNewOrder = Integer.parseInt( request.getParameter( PARAMETER_DOCUMENT_ORDER ) );
        getModifyDocumentOrder( nDocumentId, nPortletId, nOrder, nNewOrder );

        // Display the page of publishing
        return getUrlPublishedPage( nPortletId, nDocumentId );
    }

    //////////////////////////////////////////////////////////////////////////////////
    // Private implementation

    /**
     * Returns an html template containing the list of the portlet types
     * @param document The document object
     * @param nPortletId The Portet Identifier
     * @return The html code
     */
    private String getPublishedDocumentsList( Document document, int nPortletId )
    {
        HashMap model = new HashMap(  );

        DocumentListPortlet documentPortlet = DocumentListPortletHome.getDocument( document.getId(  ), nPortletId );
        model.put( MARK_PORTLET_ID, Integer.toString( nPortletId ) );
        model.put( MARK_DOCUMENT_PUBLISHED_STATUS, Integer.toString( documentPortlet.getStatus(  ) ) );
        model.put( MARK_DOCUMENT_PUBLISHED, document );
        model.put( MARK_DOCUMENT_ORDER_LIST, getOrdersList( nPortletId ) );
        model.put( MARK_DOCUMENT_ORDER, Integer.toString( documentPortlet.getDocumentOrder(  ) ) );

        // Page Template display
        DocumentPageTemplate documentPageTemplate = DocumentPageTemplateHome.findByPrimaryKey( document.getPageTemplateDocumentId(  ) );
        model.put( MARK_DOCUMENT_PAGE_TEMPLATE_PICTURE, documentPageTemplate.getPicture(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_PUBLISHED_DOCUMENT_LIST, getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Returns an html template containing the list of the portlet types
     * @param document The document object
     * @param nPortletId The Portet Identifier
     * @return The html code
     */
    private String getAssignedDocumentsList( Document document, int nPortletId )
    {
        HashMap model = new HashMap(  );

        DocumentListPortlet documentPortlet = DocumentListPortletHome.getDocument( document.getId(  ), nPortletId );
        model.put( MARK_PORTLET_ID, Integer.toString( nPortletId ) );
        model.put( MARK_DOCUMENT_PUBLISHED_STATUS, Integer.toString( documentPortlet.getStatus(  ) ) );
        model.put( MARK_DOCUMENT_PUBLISHED, document );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ASSIGNED_DOCUMENT_LIST, getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Builts a list of sequence numbers
     * @param nPortletId the portlet identifier
     * @return the list of sequence numbers
     */
    private ReferenceList getOrdersList( int nPortletId )
    {
        int nMax = DocumentListPortletHome.getMaxOrderDocument( nPortletId );
        ReferenceList list = new ReferenceList(  );

        for ( int i = 1; i < ( nMax + 1 ); i++ )
        {
            list.addItem( i, Integer.toString( ( i ) ) );
        }

        return list;
    }

    /**
     * Modify the place in the list for a document
     *
     * @param nDocumentId the document identifier
     * @param nPortletId the portlet identifier
     * @param nOrder the actual place in the list
     * @param nNewOrder the new place in the list
     * @return the new ordered list of documents
     */
    private String getModifyDocumentOrder( int nDocumentId, int nPortletId, int nOrder, int nNewOrder )
    {
        if ( nNewOrder < nOrder )
        {
            for ( int i = nOrder - 1; i > ( nNewOrder - 1 ); i-- )
            {
                int nIdDocument = DocumentListPortletHome.getIdByOrder( i, nPortletId );
                DocumentListPortletHome.getModifyDocumentOrder( i + 1, nIdDocument, nPortletId );
            }

            DocumentListPortletHome.getModifyDocumentOrder( nNewOrder, nDocumentId, nPortletId );
        }
        else if ( nNewOrder > nOrder )
        {
            for ( int i = nOrder; i < ( nNewOrder + 1 ); i++ )
            {
                int nIdDocument = DocumentListPortletHome.getIdByOrder( i, nPortletId );
                DocumentListPortletHome.getModifyDocumentOrder( i - 1, nIdDocument, nPortletId );
            }

            DocumentListPortletHome.getModifyDocumentOrder( nNewOrder, nDocumentId, nPortletId );
        }

        return "";
    }

    /**
      * Return AdminSite Url
      * @param nId The PageId
      * @return url
      */
    private String getUrlAssignedPage( int nId )
    {
        UrlItem url = new UrlItem( JSP_DOCUMENTS_ASSIGN );
        url.addParameter( PARAMETER_DOCUMENT_ID, nId );

        return url.getUrl(  );
    }

    /**
     * Return AdminSite Url
     * @param nId The PageId
     * @return url
     */
    private String getUrlPublishedPage( int nPortletId, int nDocumentId )
    {
        UrlItem url = new UrlItem( JSP_DOCUMENTS_PUBLISHING );
        url.addParameter( PARAMETER_PORTLET_ID, nPortletId );
        url.addParameter( PARAMETER_DOCUMENT_ID, nDocumentId );

        return url.getUrl(  );
    }
}
