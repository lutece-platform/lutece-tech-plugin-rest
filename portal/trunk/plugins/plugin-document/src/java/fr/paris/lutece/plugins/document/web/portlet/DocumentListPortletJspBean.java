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
package fr.paris.lutece.plugins.document.web.portlet;

import fr.paris.lutece.plugins.document.business.DocumentTypeHome;
import fr.paris.lutece.plugins.document.business.portlet.DocumentListPortlet;
import fr.paris.lutece.plugins.document.business.portlet.DocumentListPortletHome;
import fr.paris.lutece.plugins.document.service.category.CategoryService;
import fr.paris.lutece.plugins.document.service.category.CategoryService.CategoryDisplay;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage DocumentList Portlet
 */
public class DocumentListPortletJspBean extends PortletJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";
    private static final String MARK_DOCUMENT_TYPE_LIST = "document_type_list";
    private static final String MARK_CATEGORY_LIST = "category_list";
    private static final String MARK_CODE_TYPE_DOCUMENT = "code_type_document";
    private static final String PARAMETER_DOCUMENT_TYPE_CODE = "document_type_code";
    private static final String PARAMETER_CATEGORY = "categories";
    private static final String COMBO_DOCUMENT_TYPE_LIST = "@combo_document_type@";
    private static final String COMBO_DOCUMENT_CATEGORY_LIST = "@combo_category_document@";
    private static final String TEMPLATE_DOCUMENT_TYPE_LIST = "admin/plugins/document/portlet/document_type_list.html";
    private static final String TEMPLATE_CATEGORY_DOCUMENT_LIST = "admin/plugins/document/portlet/category_document_list.html";

    ////////////////////////////////////////////////////////////////////////////
    // Class attributes

    /**
     * Returns portlet's properties prefix
     *
     * @return prefix
     */
    public String getPropertiesPrefix(  )
    {
        return "portlet.document";
    }

    /**
     * Returns the Download portlet creation form
     *
     * @param request The http request
     * @return The HTML form
     */
    public String getCreate( HttpServletRequest request )
    {
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        String strIdPortletType = request.getParameter( PARAMETER_PORTLET_TYPE_ID );

        HtmlTemplate template = getCreateTemplate( strIdPage, strIdPortletType );
        template.substitute( COMBO_DOCUMENT_TYPE_LIST, getDocumentTypesList( "" ) );
        template.substitute( COMBO_DOCUMENT_CATEGORY_LIST, getCategoryList( "" ) );

        return template.getHtml(  );
    }

    /**
     * Returns the Download portlet modification form
     *
     * @param request The Http request
     * @return The HTML form
     */
    public String getModify( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        DocumentListPortlet portlet = (DocumentListPortlet) PortletHome.findByPrimaryKey( nPortletId );
        HtmlTemplate template = getModifyTemplate( portlet );

        // Format the specific modify form (composed of the list of articles and buttons to modify the order of the articles)
        template.substitute( COMBO_DOCUMENT_TYPE_LIST, getDocumentTypesList( strPortletId ) );
        template.substitute( COMBO_DOCUMENT_CATEGORY_LIST, getCategoryList( strPortletId ) );

        return template.getHtml(  );
    }

    /**
     * Process portlet's creation
     *
     * @param request The Http request
     * @return The Jsp management URL of the process result
     */
    public String doCreate( HttpServletRequest request )
    {
        DocumentListPortlet portlet = new DocumentListPortlet(  );
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        int nIdPage = Integer.parseInt( strIdPage );

        //gets the identifier of the parent page
        String strDocumentTypeCode = request.getParameter( PARAMETER_DOCUMENT_TYPE_CODE );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        portlet.setPageId( nIdPage );

        //gets the specific parameters
        portlet.setDocumentTypeCode( strDocumentTypeCode );

        //Categories
        portlet.setIdCategory( setIdCategory( request ) );

        //Portlet creation
        DocumentListPortletHome.getInstance(  ).create( portlet );

        //Displays the page with the new Portlet
        return getPageUrl( nIdPage );
    }

    /**
     * Process portlet's modification
     *
     * @param request The http request
     * @return Management's Url
     */
    public String doModify( HttpServletRequest request )
    {
        // recovers portlet attributes
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        DocumentListPortlet portlet = (DocumentListPortlet) DocumentListPortletHome.findByPrimaryKey( nPortletId );

        // retrieve portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        String strDocumentTypeCode = request.getParameter( PARAMETER_DOCUMENT_TYPE_CODE );
        portlet.setDocumentTypeCode( strDocumentTypeCode );

        //Categories
        portlet.setIdCategory( setIdCategory( request ) );

        // updates the portlet
        portlet.update(  );

        // displays the page withe the potlet updated
        return getPageUrl( portlet.getPageId(  ) );
    }

    /**
     * Set the array of id categories
     * @param request The http servlet request
     * @return The array of id categories
     */
    private int[] setIdCategory( HttpServletRequest request )
    {
        String[] strArrayIdCategory = request.getParameterValues( PARAMETER_CATEGORY );

        if ( strArrayIdCategory != null )
        {
            int[] nArrayIdCategory = new int[strArrayIdCategory.length];
            int i = 0;

            for ( String strIdCategory : strArrayIdCategory )
            {
                nArrayIdCategory[i++] = Integer.parseInt( strIdCategory );
            }

            return nArrayIdCategory;
        }

        return null;
    }

    /**
     * Returns an html template containing the list of the portlet types
     * @param strPortletId The Portet Identifier
     * @return The html code
     */
    private String getDocumentTypesList( String strPortletId )
    {
        String strCodeTypeDocument = "";

        if ( !strPortletId.equals( "" ) )
        {
            DocumentListPortlet portlet = (DocumentListPortlet) PortletHome.findByPrimaryKey( Integer.parseInt( 
                        strPortletId ) );
            strCodeTypeDocument = portlet.getDocumentTypeCode(  );
        }

        HashMap model = new HashMap(  );

        model.put( MARK_CODE_TYPE_DOCUMENT, strCodeTypeDocument );
        model.put( MARK_DOCUMENT_TYPE_LIST, DocumentTypeHome.getDocumentTypesList(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DOCUMENT_TYPE_LIST, getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Returns an html template containing the list of the categories
     * @param strPortletId The Portet Identifier
     * @return The html code
     */
    private String getCategoryList( String strPortletId )
    {
        Collection<CategoryDisplay> listCategoriesDisplay = new ArrayList<CategoryDisplay>(  );

        if ( !strPortletId.equals( "" ) )
        {
            DocumentListPortlet portlet = (DocumentListPortlet) PortletHome.findByPrimaryKey( Integer.parseInt( 
                        strPortletId ) );
            listCategoriesDisplay = CategoryService.getAllCategoriesDisplay( portlet.getIdCategory(  ) );
        }
        else
        {
            listCategoriesDisplay = CategoryService.getAllCategoriesDisplay(  );
        }

        HashMap model = new HashMap(  );

        model.put( MARK_CATEGORY_LIST, listCategoriesDisplay );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CATEGORY_DOCUMENT_LIST, getLocale(  ), model );

        return template.getHtml(  );
    }
}
