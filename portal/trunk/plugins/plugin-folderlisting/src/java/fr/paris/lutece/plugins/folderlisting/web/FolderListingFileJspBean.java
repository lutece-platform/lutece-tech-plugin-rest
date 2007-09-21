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
package fr.paris.lutece.plugins.folderlisting.web;

import fr.paris.lutece.plugins.folderlisting.business.portlet.FolderListingPortlet;
import fr.paris.lutece.plugins.folderlisting.business.portlet.FolderListingPortletHome;
import fr.paris.lutece.portal.business.page.Page;
import fr.paris.lutece.portal.business.page.PageHome;
import fr.paris.lutece.portal.service.html.EncodingService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.constants.Parameters;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides methods needed to serve the user a folderlisting file
 */
public class FolderListingFileJspBean
{
    //////////////////////////////////////////////////////////////////////////
    // Constants
    // Parameters
    private static final String PARAMETER_FILE_NAME = "file";
    private static final String PARAMETER_DIRECTORY_NAME = "folder";
    private static final String PARAMETER_PORTLET_ID = "portlet_id";
    private static final String PARAMETER_PAGE_ID = "page_id";
    private static final String PARAMETER_MESSAGE_ERROR = "error";

    //JSP
    private static final String PROPERTY_ERROR_NOT_FOUND = "folderlisting.error.NotFound";
    private static final String PROPERTY_ERROR_NOT_ALLOWED = "folderlisting.error.NotAllowed";
    public static final int FILE_NOT_FOUND = 0;
    public static final int FILE_NOT_ALLOWED = 1;

    /**
     * Public constructor
     */
    public FolderListingFileJspBean(  )
    {
    }

    /**
     * Get the directory path of the file to display from the request
     *
     * @param request the http request
     *
     * @return the path of the directory containing the file to display (given from the root folder defined for the
     *         porltet)
     */
    public String getDirectoryPath( HttpServletRequest request )
    {
        String strDirPath = request.getParameter( PARAMETER_DIRECTORY_NAME );

        return strDirPath;
    }

    /**
     * Get the binary content corresponding to the file to display
     *
     * @param request the http request
     *
     * @return the content to display
     */
    public byte[] getFileContent( HttpServletRequest request )
    {
        String strIdPortlet = request.getParameter( Parameters.PORTLET_ID );

        FolderListingPortlet folderListingPortlet = (FolderListingPortlet) FolderListingPortletHome.findByPrimaryKey( Integer.parseInt( 
                    strIdPortlet ) );

        //    check that the directory exists. if not, replace by root dir.
        File file = new File( AppPathService.getAbsolutePathFromRelativePath( folderListingPortlet.getRootFolderPath(  ) +
                    getDirectoryPath( request ) + "/" + getFilename( request ) ) );

        if ( file.exists(  ) && file.isFile(  ) )
        {
            try
            {
                final InputStream in0 = new FileInputStream( file );
                final InputStream in = new BufferedInputStream( in0 );

                final byte[] buf = new byte[(int) file.length(  )];
                int len = 0;
                int pos = 0;

                do
                {
                    len = in.read( buf, pos, buf.length - pos );
                    pos += len;
                }
                while ( len > 0 );

                in.close(  );

                return buf;
            }
            catch ( FileNotFoundException e )
            {
                AppLogService.error( e.getMessage(  ), e );

                return null;
            }
            catch ( IOException e )
            {
                AppLogService.error( e.getMessage(  ), e );

                return null;
            }
        }

        return null;
    }

    public String getFileErrorUrl( HttpServletRequest request, int nErrorType )
    {
        String strError = null;
        String strDirPath = request.getParameter( PARAMETER_DIRECTORY_NAME );
        String strPortletId = request.getParameter( PARAMETER_PORTLET_ID );
        String strPageId = request.getParameter( PARAMETER_PAGE_ID );

        switch ( nErrorType )
        {
            case FILE_NOT_FOUND:
                strError = I18nService.getLocalizedString( PROPERTY_ERROR_NOT_FOUND, request.getLocale(  ) );

                break;

            case FILE_NOT_ALLOWED:
                strError = I18nService.getLocalizedString( PROPERTY_ERROR_NOT_ALLOWED, request.getLocale(  ) );

                break;

            default:
                strError = I18nService.getLocalizedString( PROPERTY_ERROR_NOT_FOUND, request.getLocale(  ) );

                break;
        }

        strError = EncodingService.encodeUrl( strError );

        String strBaseUrl = AppPathService.getBaseUrl( request );
        String strParam = PARAMETER_MESSAGE_ERROR + "=" + strError + "&" + PARAMETER_PAGE_ID + "=" + strPageId + "&" +
            PARAMETER_DIRECTORY_NAME + "_" + strPortletId + "=" + strDirPath + "#" + PARAMETER_PORTLET_ID + "_" +
            strPortletId;
        String strUrl = strBaseUrl + AppPathService.getPortalUrl(  ) + "?" + strParam; // TODO Use UrlItem to build this url

        return strUrl;
    }

    /**
     * Get the name of the file to display from the request
     *
     * @param request the http request
     *
     * @return the name of the file to display
     */
    public String getFilename( HttpServletRequest request )
    {
        String strFileName = request.getParameter( PARAMETER_FILE_NAME );

        return strFileName;
    }

    /**
     * Check that the user can view the file requested
     *
     * @param request the http request
     *
     * @return true if the user has the right to view the file, false otherwise
     */
    public boolean checkRights( HttpServletRequest request )
    {
        String strIdPortlet = request.getParameter( Parameters.PORTLET_ID );

        FolderListingPortlet folderListingPortlet = (FolderListingPortlet) FolderListingPortletHome.findByPrimaryKey( Integer.parseInt( 
                    strIdPortlet ) );

        boolean bRights = false;

        Page page = PageHome.findByPrimaryKey( folderListingPortlet.getPageId(  ) );
        bRights = page.isVisible( request );

        return bRights;
    }
}
