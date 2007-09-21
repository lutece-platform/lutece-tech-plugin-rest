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
package fr.paris.lutece.plugins.document.service.spaces;

import fr.paris.lutece.plugins.document.business.spaces.DocumentSpace;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpaceHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.xml.XmlUtil;

import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;


/**
 * Document Spaces management Service.
 */
public class DocumentSpacesService
{
    private static final String TAG_SPACES = "spaces";
    private static final String TAG_SPACE = "space";
    private static final String TAG_SPACE_ID = "space-id";
    private static final String TAG_SPACE_NAME = "name";
    private static final String TAG_SPACE_DESCRIPTION = "description";
    private static final String TAG_SPACE_CHILDS = "child-spaces";
    private static final String TAG_SPACE_ICON_URL = "space-icon-url";

    //browser
    public static final String PARAMETER_BROWSER_SELECTED_SPACE_ID = "browser_selected_space_id";
    private static final String TEMPLATE_BROWSE_SPACES = "/admin/plugins/document/spaces/browse_spaces.html";
    private static final String MARK_SPACE = "space";
    private static final String PARAMETER_BROWSER_SPACE_ID = "browser_id_space";
    private static final String MARK_ACTION = "action";
    private static final String MARK_SPACES_LIST = "spaces_list";
    private static final String MARK_URLS_LIST = "has_childs";
    private static final String MARK_SELECTED_SPACE = "selected_space";
    private static final String MARK_GO_UP = "go_up";
    private static final String PATH_XSL = "/WEB-INF/plugins/document/xsl/";
    private static final String FILE_TREE_XSL = "document_spaces_tree.xsl";
    private static DocumentSpacesService _singleton = new DocumentSpacesService(  );

    /** Creates a new instance of DocumentSpacesService */
    private DocumentSpacesService(  )
    {
    }

    /**
     * Returns the unique instance of the service
     * @return the unique instance of the service
     */
    public static DocumentSpacesService getInstance(  )
    {
        return _singleton;
    }

    /**
     * Gets allowed Spaces for a given user as an XML document
     * @param user The current user
     * @return An XML document containing allowed spaces
     */
    public String getXmlSpacesList( AdminUser user )
    {
        StringBuffer sbXML = new StringBuffer(  );
        XmlUtil.beginElement( sbXML, TAG_SPACES );

        for ( DocumentSpace space : getUserSpaces( user ) )
        {
            findSpaces( sbXML, space.getId(  ) );
        }

        XmlUtil.endElement( sbXML, TAG_SPACES );

        return sbXML.toString(  );
    }

    /**
     * Gets user default space
     * @param user The current user
     * @return The user default space
     */
    public int getUserDefaultSpace( AdminUser user )
    {
        int nIdDefaultSpace = -1;

        for ( DocumentSpace space : getUserSpaces( user ) )
        {
            nIdDefaultSpace = space.getId(  );
        }

        return nIdDefaultSpace;
    }

    private Collection<DocumentSpace> getUserSpaces( AdminUser user )
    {
        Collection<DocumentSpace> listSpaces = DocumentSpaceHome.findAll(  );

        return RBACService.getAuthorizedCollection( listSpaces, SpaceResourceIdService.PERMISSION_VIEW, user );
    }

    /**
     * Gets the XSL to display user spaces tree
     * @return The XSL to display user spaces tree
     */
    public Source getTreeXsl(  )
    {
        FileInputStream fis = AppPathService.getResourceAsStream( PATH_XSL, FILE_TREE_XSL );
        Source xslSource = new StreamSource( fis );

        return xslSource;
    }

    /**
     * Build recursively the XML document containing the arborescence of spaces
     *
     * @param sbXML The buffer in which adding the current space of the arborescence
     * @param nSpaceId The current space of the recursive course
     */
    private void findSpaces( StringBuffer sbXML, int nSpaceId )
    {
        DocumentSpace space = DocumentSpaceHome.findByPrimaryKey( nSpaceId );
        XmlUtil.beginElement( sbXML, TAG_SPACE );
        XmlUtil.addElement( sbXML, TAG_SPACE_ID, space.getId(  ) );
        XmlUtil.addElement( sbXML, TAG_SPACE_NAME, space.getName(  ) );
        XmlUtil.addElement( sbXML, TAG_SPACE_DESCRIPTION, space.getDescription(  ) );
        XmlUtil.addElement( sbXML, TAG_SPACE_ICON_URL, space.getIconUrl(  ) );

        List<DocumentSpace> listChilds = DocumentSpaceHome.findChilds( nSpaceId );

        if ( listChilds.size(  ) > 0 )
        {
            XmlUtil.beginElement( sbXML, TAG_SPACE_CHILDS );

            for ( DocumentSpace child : listChilds )
            {
                findSpaces( sbXML, child.getId(  ) );
            }

            XmlUtil.endElement( sbXML, TAG_SPACE_CHILDS );
        }

        XmlUtil.endElement( sbXML, TAG_SPACE );
    }

    /**
     * Check if the user can view a space
     * @param nIdSpace The Space Id
     * @param user The current user
     * @return True if the user has the permission to view document in the space.
     */
    public boolean isAuthorizedView( int nIdSpace, AdminUser user )
    {
        DocumentSpace space = DocumentSpaceHome.findByPrimaryKey( nIdSpace );
        boolean bAuthorized;

        while ( space != null )
        {
            bAuthorized = RBACService.isAuthorized( space, SpaceResourceIdService.PERMISSION_VIEW, user );

            if ( bAuthorized )
            {
                return true;
            }

            space = DocumentSpaceHome.findByPrimaryKey( space.getIdParent(  ) );
        }

        return false;
    }

    public List<DocumentSpace> getUserAllowedSpaces( AdminUser user )
    {
        List<DocumentSpace> listSpaces = new ArrayList<DocumentSpace>(  );

        for ( DocumentSpace space : getUserSpaces( user ) )
        {
            addChildSpaces( space, listSpaces );
        }

        return listSpaces;
    }

    /**
     * get the HTML code to display a space browser.
     *
     * @param request The HTTP request
     * @param user The current user
     * @param locale The Locale
     * @return The HTML form
     */
    public String getSpacesBrowser( HttpServletRequest request, AdminUser user, Locale locale )
    {
        String strIdSpaceFilter = request.getParameter( PARAMETER_BROWSER_SELECTED_SPACE_ID );
        String strIdSpace = request.getParameter( PARAMETER_BROWSER_SPACE_ID );
        HashMap model = new HashMap(  );
        DocumentSpace selectedSpace = null;
        DocumentSpace space;
        Collection<DocumentSpace> spacesList = new ArrayList<DocumentSpace>(  );
        int nIdSpace = -1;
        int i = 0;
        boolean bIsAuthorized = false;
        boolean bGoUp = true;

        // Selected space
        if ( ( strIdSpaceFilter != null ) && !strIdSpaceFilter.equals( "" ) )
        {
            selectedSpace = DocumentSpaceHome.findByPrimaryKey( Integer.parseInt( strIdSpaceFilter ) );
        }

        // if current space doesn't exists then set it up
        if ( strIdSpace == null )
        {
            nIdSpace = DocumentSpacesService.getInstance(  ).getUserDefaultSpace( user );
        }
        else
        {
            nIdSpace = Integer.parseInt( strIdSpace );
        }

        // set space list
        if ( nIdSpace == -1 )
        {
            space = new DocumentSpace(  );
            space.setId( -1 );
            space.setIdParent( -1 );
        }
        else
        {
            space = DocumentSpaceHome.findByPrimaryKey( nIdSpace );
        }

        bIsAuthorized = isAuthorizedView( space.getId(  ), user );

        spacesList = DocumentSpaceHome.findChilds( space.getId(  ) );

        if ( !bIsAuthorized )
        {
            spacesList = RBACService.getAuthorizedCollection( spacesList, SpaceResourceIdService.PERMISSION_VIEW, user );
        }

        // set links for childs spaces
        int[] arrayHasChilds = new int[spacesList.size(  )];

        for ( DocumentSpace documentSpace : spacesList )
        {
            // Check Childs spaces
            List<DocumentSpace> childsSpaces = DocumentSpaceHome.findChilds( documentSpace.getId(  ) );

            //If childs spaces list is not empty, then add into array
            if ( childsSpaces.size(  ) != 0 )
            {
                arrayHasChilds[i] = documentSpace.getId(  );
            }

            i++;
        }

        // display "go up" link
        if ( !bIsAuthorized )
        {
            bGoUp = false;
        }

        model.put( MARK_GO_UP, bGoUp );
        model.put( MARK_SELECTED_SPACE, selectedSpace );
        model.put( MARK_SPACE, space );
        model.put( MARK_SPACES_LIST, spacesList );
        model.put( MARK_URLS_LIST, arrayHasChilds );
        model.put( MARK_ACTION, request.getRequestURI(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_BROWSE_SPACES, locale, model );

        return template.getHtml(  );
    }

    private void addChildSpaces( DocumentSpace spaceParent, List<DocumentSpace> listSpaces )
    {
        listSpaces.add( spaceParent );

        List<DocumentSpace> listChilds = DocumentSpaceHome.findChilds( spaceParent.getId(  ) );

        for ( DocumentSpace space : listChilds )
        {
            addChildSpaces( space, listSpaces );
        }
    }
}
