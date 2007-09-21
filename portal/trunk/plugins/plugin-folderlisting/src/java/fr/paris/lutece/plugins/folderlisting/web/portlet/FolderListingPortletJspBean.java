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
package fr.paris.lutece.plugins.folderlisting.web.portlet;

import fr.paris.lutece.plugins.folderlisting.business.portlet.FolderListingPortlet;
import fr.paris.lutece.plugins.folderlisting.business.portlet.FolderListingPortletHome;
import fr.paris.lutece.plugins.folderlisting.service.Folder;
import fr.paris.lutece.plugins.folderlisting.service.FolderService;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.portlet.PortletJspBean;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.io.File;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage FolderListing Portlet features
 */
public class FolderListingPortletJspBean extends PortletJspBean
{
    // Rights
    public static final String RIGHT_MANAGE_ADMIN_SITE = "CORE_ADMIN_SITE";

    // Messages
    private static final String MESSAGE_FOLDER_INVALID = "folderlisting.message.folderInvalid";

    //Parameters
    private static final String PARAMETER_PAGE_ID = "page_id";
    private static final String PARAMETER_PORTLET_ID = "portlet_id";
    private static final String PARAMETER_PORTLET_TYPE_ID = "portlet_type_id";
    private static final String PARAMETER_FOLDER = "folder";

    //Markers
    private static final String MARK_FOLDER_LIST = "folder_list";
    private static final String MARK_FOLDER_ID = "folder_id";

    // Templates
    private static final String TEMPLATE_COMBO_FOLDER = "admin/plugins/folderlisting/portlet/combo_folder.html";
    private static final String COMBO_FOLDER_LIST = "@combo_folders@";

    /**
     * Returns the FolderListing portlet creation form
     *
     * @param request The Http request
     *
     * @return The HTML form
     */
    public String getCreate( HttpServletRequest request )
    {
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        System.out.println( "strIdPage  " + strIdPage );

        String strPortletIdType = request.getParameter( PARAMETER_PORTLET_TYPE_ID );
        System.out.println( "strPortletIdType  " + strPortletIdType );

        HtmlTemplate template = getCreateTemplate( strIdPage, strPortletIdType );
        List<Folder> listFolders = FolderService.getInstance(  ).getFoldersCollection(  );
        listFolders = (List<Folder>) AdminWorkgroupService.getAuthorizedCollection( listFolders, getUser(  ) );

        ReferenceList rlFolders = new ReferenceList(  );

        for ( Folder folder : listFolders )
        {
            rlFolders.addItem( folder.getId(  ), folder.getName(  ) );
        }

        String strHtmlCombo = getFolderIndexCombo( rlFolders, "" );
        template.substitute( COMBO_FOLDER_LIST, strHtmlCombo );

        return template.getHtml(  );
    }

    /**
     * Returns the FolderListing portlet modification form
     *
     * @param request The Http request
     *
     * @return The HTML form
     */
    public String getModify( HttpServletRequest request )
    {
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        FolderListingPortlet portlet = (FolderListingPortlet) PortletHome.findByPrimaryKey( nPortletId );
        HtmlTemplate template = getModifyTemplate( portlet );
        List<Folder> listFolders = FolderService.getInstance(  ).getFoldersCollection(  );
        listFolders = (List<Folder>) AdminWorkgroupService.getAuthorizedCollection( listFolders, getUser(  ) );

        ReferenceList rlFolders = new ReferenceList(  );

        for ( Folder folder : listFolders )
        {
            rlFolders.addItem( folder.getId(  ), folder.getName(  ) );
        }

        String strHtmlCombo = getFolderIndexCombo( rlFolders, portlet.getRootFolderId(  ) );
        template.substitute( COMBO_FOLDER_LIST, strHtmlCombo );

        return template.getHtml(  );
    }

    /**
     * Returns portlet's properties prefix
     *
     * @return prefix
     */
    public String getPropertiesPrefix(  )
    {
        return "portlet.folderlisting";
    }

    /**
     * Process portlet's creation
     *
     * @param request The Http request
     *
     * @return The Jsp management URL of the process result
     */
    public String doCreate( HttpServletRequest request )
    {
        FolderListingPortlet portlet = new FolderListingPortlet(  );
        String strIdPage = request.getParameter( PARAMETER_PAGE_ID );
        String strFolderId = request.getParameter( PARAMETER_FOLDER );
        int nIdPage = Integer.parseInt( strIdPage );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        portlet.setPageId( nIdPage );

        if ( isValidFolder( strFolderId ) )
        {
            portlet.setRootFolderId( strFolderId );
            FolderListingPortletHome.getInstance(  ).create( portlet );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_FOLDER_INVALID, AdminMessage.TYPE_STOP );
        }

        //Displays the page with the new Portlet
        return getPageUrl( nIdPage );
    }

    /**
     * Process portlet's modification
     *
     * @param request The Http request
     *
     * @return The Jsp management URL of the process result
     */
    public String doModify( HttpServletRequest request )
    {
        // Getting portlet
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        int nPortletId = Integer.parseInt( strPortletId );
        FolderListingPortlet portlet = (FolderListingPortlet) PortletHome.findByPrimaryKey( nPortletId );

        // get portlet common attributes
        String strErrorUrl = setPortletCommonData( request, portlet );

        if ( strErrorUrl != null )
        {
            return strErrorUrl;
        }

        // Getting portlet's common attributs
        setPortletCommonData( request, portlet );

        String strFolderId = request.getParameter( PARAMETER_FOLDER );

        if ( isValidFolder( strFolderId ) )
        {
            portlet.setRootFolderId( strFolderId );
            portlet.update(  );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_FOLDER_INVALID, AdminMessage.TYPE_STOP );
        }

        return getPageUrl( portlet.getPageId(  ) );
    }

    /**
     * Return the folder listing depending on rights
     *
     * @param listFolders The list of folders
     * @param strDefaultFolderId the default id of the folder
     *
     * @return DOCUMENT ME!
     */
    String getFolderIndexCombo( ReferenceList listFolders, String strDefaultFolderId )
    {
        HashMap model = new HashMap(  );
        model.put( MARK_FOLDER_LIST, listFolders );
        model.put( MARK_FOLDER_ID, strDefaultFolderId );

        HtmlTemplate templateCombo = AppTemplateService.getTemplate( TEMPLATE_COMBO_FOLDER, getLocale(  ), model );

        return templateCombo.getHtml(  );
    }

    /**
     * Verifies whether the folder is valid
     *
     * @param strFolderId The Id corresponding to the folder
     *
     * @return true if the folder is a valid folder
     */
    private boolean isValidFolder( String strFolderId )
    {
        boolean bValue = false;

        if ( ( strFolderId != null ) && !strFolderId.equals( "" ) )
        {
            Folder folder = FolderService.getInstance(  ).getFolder( strFolderId );

            if ( folder != null )
            {
                String strFolderPath = "/" + folder.getPath(  );
                String strAbsolutePath = AppPathService.getAbsolutePathFromRelativePath( strFolderPath );

                if ( new File( strAbsolutePath ).exists(  ) )
                {
                    bValue = true;
                }
            }
        }

        return bValue;
    }
}
