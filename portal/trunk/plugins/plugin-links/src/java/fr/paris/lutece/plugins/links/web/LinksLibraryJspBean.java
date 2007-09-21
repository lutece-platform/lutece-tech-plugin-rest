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
package fr.paris.lutece.plugins.links.web;

import fr.paris.lutece.plugins.links.business.Link;
import fr.paris.lutece.plugins.links.business.LinkHome;
import fr.paris.lutece.plugins.links.business.portlet.LinksPortletHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.fileupload.FileItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage links features ( manage, create, modify, remove, change order of
 * contact )
 */
public class LinksLibraryJspBean extends PluginAdminPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////////////
    // Constants

    // Jsp
    private static final String JSP_LINKS_LIBRARY = "LinksLibrary.jsp";
    private static final String JSP_LINKS_PAGE = "ManageLinksPage.jsp";
    private static final String JSP_DO_REMOVE_LINK = "jsp/admin/plugins/links/DoRemoveLink.jsp";

    // Right
    public static final String RIGHT_MANAGE_LINKS = "LINKS_MANAGEMENT";

    // Templates
    private static final String TEMPLATE_LINKS_LIST = "admin/plugins/links/links_list.html";
    private static final String TEMPLATE_MODIFY_LINK = "admin/plugins/links/modify_link.html";
    private static final String TEMPLATE_CREATE_LINK = "admin/plugins/links/create_link.html";
    private static final String TEMPLATE_MANAGE_LINKS_PAGE = "admin/plugins/links/manage_links_page.html";

    // Property
    private static final String PROPERTY_PAGE_TITLE_LINKS_LIST = "links.links_list.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_LINK = "links.create_link.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MANAGE_LINKS_PAGES = "links.manage_links_page.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_LINK = "links.modify_link.pageTitle";
    private static final String PROPERTY_LINKS_IMAGES_PATH = "links.images.path";
    private static final String PROPERTY_NO_IMAGE_ADMIN = "links.no.image.admin";
    private static final String PROPERTY_LINKS_URL_DEFAULT_KEY_NAME = "links.url.defaultKeyName";
    private static final String PROPERTY_LINK_DESCRIPTION_SIZE = "links.description.size";
    private static final String MESSAGE_CONFIRM_REMOVE_LINK = "links.message.confirmRemoveLink";
    private static final String PROPERTY_CREATE_LINKS_URL_TITLE = "links.create_link.title";
    private static final String PROPERTY_MODIFY_LINKS_URL_TITLE = "links.modify_link.title";

    // Marker
    private static final String MARK_LINKS_LIST = "links_list";
    private static final String MARK_ALTERNATE_URLS = "alternate_urls";
    private static final String MARK_LINK = "link";
    private static final String MARK_LIST_ALTERNATE_URLS = "alternate_urls_list";
    private static final String MARK_ORDER_LIST_SELECTED_PORTLETS = "order_list_selected_portlets";
    private static final String MARK_ORDER_LIST_UNSELECTED_PORTLETS = "order_list_unselected_portlets";
    private static final String MARK_PORTLETS_LIST = "portlets_list";
    private static final String MARK_UNSELECTED_PORTLETS_LIST = "unselected_portlets_list";
    private static final String MARK_LINK_URL_DEFAULT = "url_default";
    private static final String MARK_LINK_IMAGE_PATH = "images_path";
    private static final String MARK_LINK_NO_IMAGE = "no_image";
    private static final String MARK_WORKGROUPS_LIST = "workgroups_list";

    // Parameters
    private static final String PARAMETER_LINK_ID = "link_id";
    private static final String PARAMETER_LINK_NAME = "link_name";
    private static final String PARAMETER_LINK_URL = "link_url";
    private static final String PARAMETER_LINK_DESCRIPTION = "link_description";
    private static final String PARAMETER_LINK_UPDATE_IMAGE = "update_image";
    private static final String PARAMETER_PORTLET_ORDER = "portlet_order";
    private static final String PARAMETER_NEW_PORTLET_ORDER = "new_portlet_order";
    private static final String PARAMETER_PORTLET_ID = "portlet_id";
    private static final String PARAMETER_SUFFIX_ALTERNATE_URL = "_url";
    private static final String PARAMETER_SUFFIX_ALTERNATE_TYPE = "_type";
    private static final String PARAMETER_LINK_IMAGE = "link_image";
    private static final String PARAMETER_WORKGROUP = "workgroupKey";

    // Types for optional url input
    private static final int TYPE_NEW_URL = 1;
    private static final int TYPE_DEFAULT_URL = 2;
    private static final int TYPE_NO_URL = 3;

    /**
     * Creates a new LinksJspBean object.
     */
    public LinksLibraryJspBean(  )
    {
    }

    /**
     * Returns the links list
     *
     * @return the html code for display the links list
     */
    public String getLinksList( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_LINKS_LIST );

        HashMap model = new HashMap(  );
        String strDefaultUrl = AppPropertiesService.getProperty( PROPERTY_LINKS_URL_DEFAULT_KEY_NAME );
        String strImagesPath = AppPropertiesService.getProperty( PROPERTY_LINKS_IMAGES_PATH );

        ReferenceList virtualHosts = new ReferenceList( );
        String strDefaultUrlName = "";       
        
        try
        {                
            if( AppPathService.getAvailableVirtualHosts(  ) != null)
            {        
                virtualHosts = AppPathService.getAvailableVirtualHosts(  )  ;
            
                for ( ReferenceItem item : virtualHosts )
                {
                    if ( ( strDefaultUrl != null ) && item.getCode(  ).equals( strDefaultUrl ) )
                    {
                        strDefaultUrlName = item.getName(  );
                    }
                }
            }
        }
        catch( NullPointerException  e )
        {
            AppLogService.error( e.getMessage() , e );
        }

        //Replace links optional url code by url name
        Collection<Link> links = LinkHome.getLinksList(  );
        ReferenceList optUrls = null;

        for ( Link link : links )
        {
            optUrls = link.getOptionalUrls(  );

            for ( ReferenceItem item : virtualHosts )
            {
                for ( ReferenceItem optUrl : optUrls )
                {
                    if ( optUrl.getCode(  ).equals( item.getCode(  ) ) )
                    {
                        optUrl.setCode( item.getName(  ) );
                    }
                }
            }

            link.setOptionalUrls( optUrls );
        }
        
        links = (List<Link>) AdminWorkgroupService.getAuthorizedCollection( links, getUser(  ) );
        
        model.put( MARK_LINKS_LIST, links );
        model.put( MARK_LINK_URL_DEFAULT, strDefaultUrlName );
        model.put( MARK_LINK_IMAGE_PATH, strImagesPath );
        model.put( MARK_LINK_NO_IMAGE, AppPropertiesService.getProperty( PROPERTY_NO_IMAGE_ADMIN ) );
        
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_LINKS_LIST, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
    * Create a new Link
    *
    * @param request The Http request
    * @return The jsp URL which displays the links management page
    */
    public String doCreateLink( HttpServletRequest request )
    {
        MultipartHttpServletRequest mRequest = ( MultipartHttpServletRequest ) request;
        
        
        
        //get name and url
        Link link = new Link(  );

        if ( ( mRequest.getParameter( PARAMETER_LINK_NAME ) == null ) || ( mRequest.getParameter( PARAMETER_LINK_NAME ).equals( "" ) ) || ( mRequest.getParameter( PARAMETER_LINK_URL ) == null )  || ( mRequest.getParameter( PARAMETER_LINK_URL ).equals( "" )) ) 
        {
            return AdminMessageService.getMessageUrl( mRequest, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_ERROR );
        }
        

        // control description textarea length (links.properties)
        if ( mRequest.getParameter( PARAMETER_LINK_DESCRIPTION ).length(  ) > Integer.parseInt( AppPropertiesService.getProperty( PROPERTY_LINK_DESCRIPTION_SIZE ) ) )
        {
            Object[] messageArgs = { AppPropertiesService.getProperty( PROPERTY_LINK_DESCRIPTION_SIZE ) };
            return AdminMessageService.getMessageUrl( mRequest, Messages.TEXT_SIZE, messageArgs, AdminMessage.TYPE_ERROR );
        }
        
        ReferenceList listOptionalUrls = getUrlsListFromMultipart( link.getUrl(  ), mRequest );

        if ( listOptionalUrls == null )
        {
            return AdminMessageService.getMessageUrl( mRequest, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_ERROR );
        }

        link.setOptionalUrls( listOptionalUrls );
        link.setName( mRequest.getParameter( PARAMETER_LINK_NAME ) );
        link.setUrl( mRequest.getParameter( PARAMETER_LINK_URL ) );
        link.setDescription( mRequest.getParameter( PARAMETER_LINK_DESCRIPTION ) );
        link.setWorkgroupKey( mRequest.getParameter( PARAMETER_WORKGROUP ) );
        
        link.setDate( new java.sql.Date( new java.util.Date(  ).getTime(  ) ) );
        LinkHome.create( link );

        FileItem itemPicture = mRequest.getFile( PARAMETER_LINK_IMAGE );

     //   if ( itemPicture != null )
     //   {
            byte[] bytes = itemPicture.get(  );
            String strMimeType = itemPicture.getContentType(  );

            link.setImageContent( bytes );
            link.setMimeType( strMimeType );
       /* }
        else
            {
                link.setImageContent( null );
                link.setMimeType( null );
            }*/

        LinkHome.update( link );

        //getting link object in table
        return JSP_LINKS_LIBRARY;
    }

    /**
     * Returns the form to remove a link
     *
     * @param request The Http request
     * @return Html form
     */
    public String getRemoveLink( HttpServletRequest request )
    {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        UrlItem url = new UrlItem( JSP_DO_REMOVE_LINK );
        url.addParameter( PARAMETER_LINK_NAME, mRequest.getParameter( PARAMETER_LINK_NAME ) );
        url.addParameter( PARAMETER_LINK_ID, Integer.parseInt( mRequest.getParameter( PARAMETER_LINK_ID ) ) );

        return AdminMessageService.getMessageUrl( mRequest, MESSAGE_CONFIRM_REMOVE_LINK, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Remove a link from the database
     *
     * @param request The Http request
     * @return The jsp URL which displays the management page of the links
     */
    public String doRemoveLink( HttpServletRequest request )
    {
        int nLinkId = Integer.parseInt( request.getParameter( PARAMETER_LINK_ID ) );

        // Deleting record in database
        LinksPortletHome.removeLinkFromPortlets( nLinkId );
        LinkHome.delete( nLinkId );

        return JSP_LINKS_LIBRARY;
    }

    /**
     * Returns the form to create a new Link
     *
     * @param request The Http request
     * @return Html form
     */
    public String getCreateLink( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_LINK );

        HashMap model = new HashMap(  );

        ReferenceList virtualHosts = new ReferenceList( );
        String strUrlName = "";       
        
        try
        {                
            if( AppPathService.getAvailableVirtualHosts(  ) != null)
            {        
                virtualHosts = AppPathService.getAvailableVirtualHosts(  );
                strUrlName = I18nService.getLocalizedString( PROPERTY_CREATE_LINKS_URL_TITLE, request.getLocale(  ) );
                String strDefaultKey = AppPropertiesService.getProperty( PROPERTY_LINKS_URL_DEFAULT_KEY_NAME );

                if ( virtualHosts.isEmpty(  ) )
                {
                    model.put( MARK_ALTERNATE_URLS, "" );
                }
                else
                {
                    for ( ReferenceItem item : virtualHosts )
                    {
                        if ( item.getName(  ) == null )
                        {
                            item.setName( "" );
                        }

                        if ( strDefaultKey.equals( item.getCode(  ) ) )
                        {
                            strUrlName = item.getName(  );
                            virtualHosts.remove( item );
                        }
                        break;
                    }
                    model.put( MARK_LIST_ALTERNATE_URLS, virtualHosts );
                }   
            }
        }
        catch( NullPointerException  e )
        {
            AppLogService.error( e.getMessage() , e );
        }

        model.put( MARK_LINK_URL_DEFAULT, strUrlName );
        model.put( MARK_WORKGROUPS_LIST, AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_LINK, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the form to modify an existing link
     *
     * @param request The Http request
     * @return Html form
     */
    public String getModifyLink( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_LINK );

        String strImagesPath = AppPropertiesService.getProperty( PROPERTY_LINKS_IMAGES_PATH );
        String strUrlName = I18nService.getLocalizedString( PROPERTY_MODIFY_LINKS_URL_TITLE, request.getLocale(  ) );
        String strDefaultKey = AppPropertiesService.getProperty( PROPERTY_LINKS_URL_DEFAULT_KEY_NAME );
        String strNoImageName = AppPropertiesService.getProperty( PROPERTY_NO_IMAGE_ADMIN );
        HashMap model = new HashMap(  );
        String strId = request.getParameter( PARAMETER_LINK_ID );
        ReferenceList virtualHosts = new ReferenceList( );    
        
        try
        {                
            if( AppPathService.getAvailableVirtualHosts(  ) != null)
            {                
                virtualHosts = AppPathService.getAvailableVirtualHosts(  );

                if ( virtualHosts.isEmpty(  ) )
                {
                    model.put( MARK_ALTERNATE_URLS, "" );
                }
                else
                {
                    for ( ReferenceItem item : virtualHosts )
                    {
                        if ( strDefaultKey.equals( item.getCode(  ) ) )
                        {
                            strUrlName = item.getName(  );
                            virtualHosts.remove( item );
                        }
                            
                        break;
                    }
                }

                model.put( MARK_LIST_ALTERNATE_URLS, virtualHosts );
            }
        }
        catch( NullPointerException  e )
        {
            AppLogService.error( e.getMessage() , e );
        }
        
        //getting link object in table    	
        model.put( MARK_LINK, LinkHome.findByPrimaryKey( Integer.parseInt( strId ) ) );
        model.put( MARK_LINK_URL_DEFAULT, strUrlName );
        model.put( MARK_LINK_IMAGE_PATH, strImagesPath );
        model.put( MARK_LINK_NO_IMAGE, strNoImageName );
        model.put( MARK_WORKGROUPS_LIST, AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_LINK, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the Link's modification
     *
     * @param request The Http request
     * @return The jsp URL which displays the management page of the links
     */
    public String doModifyLink( HttpServletRequest request )
    {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        String strIdLink = mRequest.getParameter( PARAMETER_LINK_ID );
        int nLinkId = Integer.parseInt( strIdLink );

        //get name and url
        String strUpdateImage = mRequest.getParameter( PARAMETER_LINK_UPDATE_IMAGE );
        Link link = LinkHome.findByPrimaryKey( nLinkId );

       if ( ( mRequest.getParameter( PARAMETER_LINK_NAME ) == null ) || ( mRequest.getParameter( PARAMETER_LINK_NAME ).equals( "" ) ) || ( mRequest.getParameter( PARAMETER_LINK_URL ) == null )  || ( mRequest.getParameter( PARAMETER_LINK_URL ).equals( "" )) ) 
        {
            return AdminMessageService.getMessageUrl( mRequest, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_ERROR );
        }
        
        // control description textarea length (links.properties)
        if ( mRequest.getParameter( PARAMETER_LINK_DESCRIPTION ).length(  ) > Integer.parseInt( AppPropertiesService.getProperty( PROPERTY_LINK_DESCRIPTION_SIZE ) ) )
        {
            Object[] messageArgs = { AppPropertiesService.getProperty( PROPERTY_LINK_DESCRIPTION_SIZE ) };
            return AdminMessageService.getMessageUrl( mRequest, Messages.TEXT_SIZE, messageArgs, AdminMessage.TYPE_ERROR );
        }
        
        ReferenceList listOptionalUrls = getUrlsListFromMultipart( link.getUrl(  ), mRequest );

        if ( listOptionalUrls == null )
        {
            return AdminMessageService.getMessageUrl( mRequest, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_ERROR );
        }
        
        if ( ( strUpdateImage != null ) && ( strUpdateImage.equals( "on" ) ) )
        {
            FileItem itemPicture = mRequest.getFile( PARAMETER_LINK_IMAGE );

            // Checking object's existence
            if ( ( itemPicture == null ) || itemPicture.getName(  ).equals( "" ) )
            {
                link.setImageContent( null );
                link.setMimeType( null );
            }
            else
            {
                byte[] bytes = itemPicture.get(  );
                String strMimeType = itemPicture.getContentType(  );

                link.setImageContent( bytes );
                link.setMimeType( strMimeType );
            }
        }
        
        LinkHome.update( link );        
        
        //getting link object in table
        return JSP_LINKS_LIBRARY;
    }

    /**
     * Select a link portlet for the Links Page
     *
     * @param request The Http request
     * @return The jsp URL which displays the management of the links page
     */
    public String doSelectPortletLinks( HttpServletRequest request )
    {
        int nPortletId = Integer.parseInt( request.getParameter( PARAMETER_PORTLET_ID ) );
        String strOrder = request.getParameter( PARAMETER_PORTLET_ORDER );
        int nOrder = Integer.parseInt( strOrder );
        int nMax = LinksPortletHome.getPortletMaxOrder(  );

        for ( int i = nOrder; i < ( nMax + 1 ); i++ )
        {
            int nPortletIdTemp = LinksPortletHome.getPortletIdByOrder( i );
            LinksPortletHome.updatePortletOrder( i + 1, nPortletIdTemp );
        }

        LinksPortletHome.insertPortlet( nPortletId, nOrder );

        return JSP_LINKS_PAGE;
    }

    /**
     * Modify the order of the links portlets in the links page
     *
     * @param request The Http request
     * @return The jsp URL which displays the management of the links page
     */
    public String doModifyPortletsOrder( HttpServletRequest request )
    {
        int nPortletId = Integer.parseInt( request.getParameter( PARAMETER_PORTLET_ID ) );
        int nOrder = Integer.parseInt( request.getParameter( PARAMETER_PORTLET_ORDER ) );
        int nNewOrder = Integer.parseInt( request.getParameter( PARAMETER_NEW_PORTLET_ORDER ) );

        if ( ( nOrder - nNewOrder ) > 0 )
        {
            for ( int i = nOrder - 1; i >= nNewOrder; i-- )
            {
                int nPortletIdTemp = LinksPortletHome.getPortletIdByOrder( i );
                LinksPortletHome.updatePortletOrder( i + 1, nPortletIdTemp );
            }
        }
        else
        {
            for ( int i = nOrder + 1; i <= nNewOrder; i++ )
            {
                int nPortletIdTemp = LinksPortletHome.getPortletIdByOrder( i );
                LinksPortletHome.updatePortletOrder( i - 1, nPortletIdTemp );
            }
        }

        LinksPortletHome.updatePortletOrder( nNewOrder, nPortletId );

        return JSP_LINKS_PAGE;
    }

    /**
     * Remove a portlet from the links page
     *
     * @param request The Http request
     * @return The jsp URL which displays the management of the links page
     */
    public String doUnselectPortlet( HttpServletRequest request )
    {
        int nPortletId = Integer.parseInt( request.getParameter( PARAMETER_PORTLET_ID ) );

        // Deleting record in database
        String strOrder = request.getParameter( PARAMETER_PORTLET_ORDER );
        int nOrder = Integer.parseInt( strOrder );
        int nMax = LinksPortletHome.getPortletMaxOrder(  );
        LinksPortletHome.removePortlet( nPortletId );

        for ( int i = nOrder + 1; i < ( nMax + 1 ); i++ )
        {
            int nPortletIdTemp = LinksPortletHome.getPortletIdByOrder( i );
            LinksPortletHome.updatePortletOrder( i - 1, nPortletIdTemp );
        }

        return JSP_LINKS_PAGE;
    }

    /**
     * Creates and returns the management page fot links page
     *
     * @return the html code of the page
     */
    public String getManageLinksPage( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_LINKS_PAGES );

        HashMap model = new HashMap(  );
        ReferenceList unselectedPortletList = LinksPortletHome.findUnselectedPortlets(  );
        Collection portletsList = LinksPortletHome.getPortletsInLinksPage(  );

        model.put( MARK_ORDER_LIST_UNSELECTED_PORTLETS, getPortletLinksOrdersList(  ) );
        model.put( MARK_ORDER_LIST_SELECTED_PORTLETS, getOrdersList(  ) );

        model.put( MARK_PORTLETS_LIST, portletsList );
        model.put( MARK_UNSELECTED_PORTLETS_LIST, unselectedPortletList );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_LINKS_PAGE, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    // Private Implementations

    /**
     * Returns the list of the possible order for a new link in a portlet
     *
     * @return list
     */
    private ReferenceList getPortletLinksOrdersList(  )
    {
        int nMax = LinksPortletHome.getPortletMaxOrder(  );
        ReferenceList list = new ReferenceList(  );

        for ( int i = 1; i < ( nMax + 2 ); i++ )
        {
            list.addItem( i, Integer.toString( i ) );
        }

        return list;
    }

    /**
     * Returns the list of the possible order to move a link in a portlet
     *
     * @return list
     */
    private ReferenceList getOrdersList(  )
    {
        int nMax = LinksPortletHome.getPortletMaxOrder(  );
        ReferenceList list = new ReferenceList(  );

        for ( int i = 1; i < ( nMax + 1 ); i++ )
        {
            list.addItem( i, Integer.toString( i ) );
        }

        return list;
    }

    /**
     * get optional urls from form
     *
     * @param strDefaultUrl name of the default virtualHostKey
     * @param multi the form
     * @return list containing submitted urls
     */
    private ReferenceList getUrlsListFromMultipart( String strDefaultUrl, MultipartHttpServletRequest multi )
    {
        ReferenceList listUrls = new ReferenceList(  );

        ReferenceList virtualHosts = new ReferenceList( );            
        try
        {                
            if( AppPathService.getAvailableVirtualHosts(  ) != null)
            {           
                virtualHosts = AppPathService.getAvailableVirtualHosts(  );

                if ( !virtualHosts.isEmpty(  ) )
                {
                    String strDefaultKey = AppPropertiesService.getProperty( PROPERTY_LINKS_URL_DEFAULT_KEY_NAME );

                    for ( ReferenceItem item : virtualHosts )
                    {
                        String strCode = item.getCode(  );

                        if ( !strDefaultKey.equals( strCode ) )
                        {
                            // checkbox processing
                            switch ( Integer.parseInt( multi.getParameter( strCode + PARAMETER_SUFFIX_ALTERNATE_TYPE ) ) )
                            {
                                case TYPE_NEW_URL:                                
                                String strUrl = multi.getParameter( strCode + PARAMETER_SUFFIX_ALTERNATE_URL );
                                if ( ( strUrl == null ) || "".equals( strUrl ) )
                                {
                                    return null;
                                }
                                listUrls.addItem( strCode, strUrl );

                                break;
                                
                                case TYPE_DEFAULT_URL:
                                listUrls.addItem( strCode, strDefaultUrl );

                                break;

                                case TYPE_NO_URL:default:
                                break;
                            }
                        }
                    }
                }
            }
        }
        catch( NullPointerException  e )
        {
            AppLogService.error( e.getMessage() , e );
        }
        return listUrls;
    }
}
