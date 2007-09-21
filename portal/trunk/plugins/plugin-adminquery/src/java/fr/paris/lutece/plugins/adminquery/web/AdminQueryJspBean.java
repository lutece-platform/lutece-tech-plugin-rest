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
package fr.paris.lutece.plugins.adminquery.web;

import fr.paris.lutece.plugins.adminquery.business.AdminQueryHome;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/*
 * This class provides the user interface to manage adminquery features
 */
public class AdminQueryJspBean extends PluginAdminPageJspBean
{
    /////////////////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_ADMIN_QUERY = "ADMIN_QUERY_MANAGEMENT";

    //Properties
    private static final String PROPERTY_QUERY = "adminquery.query";

    //Parameters
    private static final String PARAMETER_REQUEST = "sql_request";

    //Markers
    private static final String MARK_COLUMN_NAME_LIST = "column_name";
    private static final String MARK_ROW_LIST = "row_list";
    private static final String MARK_LINE_LIST = "line_list";
    private static final String MARK_REQUEST_LIST = "sql_request_list";

    //Templates
    private static final String TEMPLATE_MANAGE_ADMIN_QUERY = "admin/plugins/adminquery/manage_admin_query.html";
    private static final String TEMPLATE_RESULTS_LINE = "admin/plugins/adminquery/results_line.html";

    // Constants
    private static final String SUFFIX_LABEL = ".label";
    private static final String SUFFIX_SQL = ".sql";

    /**
     * Creates a new AdminQueryJspBean object.
     */
    public AdminQueryJspBean(  )
    {
    }

    /**
     * Returns the Sql request management form
     * @param request the SQL request
     * @return the HTML page
     */
    public String getAdminQuery( HttpServletRequest request )
    {
        return getAdminPage( getResults( request.getParameter( PARAMETER_REQUEST ) ) );
    }

    /**
     * Returns the result of the sql request in html form
     * @param strRequest the SQL request
     * @return  String the result of the request of row
     */
    private String getResults( String strRequest )
    {
        HashMap rootModel = new HashMap(  );
        rootModel.put( MARK_REQUEST_LIST, getRequestList(  ) );

        if ( !( ( strRequest == null ) || strRequest.equals( "0" ) ) )
        {
            try
            {
                strRequest = AppPropertiesService.getProperty( PROPERTY_QUERY + strRequest + SUFFIX_SQL );

                List<String> listColumnNames = AdminQueryHome.selectColumnNames( strRequest, getPlugin(  ) );

                if ( listColumnNames != null )
                {
                    rootModel.put( MARK_COLUMN_NAME_LIST, listColumnNames );
                    rootModel.put( MARK_ROW_LIST, getRows( strRequest ) );
                }
            }
            catch ( AppException e )
            {
                throw new AppException( "Request error in adminquery.properties" );
            }
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_ADMIN_QUERY, getLocale(  ), rootModel );

        return template.getHtml(  );
    }

    /**
     * Returns a List of row witch is in html form
     * Fill in the lines of the rows
     * @param strRequest the SQL request
     * @return  List of row
     */
    private List<String> getRows( String strRequest )
    {
        List<String> listRow = new ArrayList(  );

        //for row's values
        for ( List<String> listLine : AdminQueryHome.selectRows( strRequest, getPlugin(  ) ) )
        {
            HashMap model = new HashMap(  );
            model.put( MARK_LINE_LIST, listLine );

            HtmlTemplate tLine = AppTemplateService.getTemplate( TEMPLATE_RESULTS_LINE, getLocale(  ), model );
            listRow.add( tLine.getHtml(  ) );
        }

        return listRow;
    }

    /**
     * Return the list of requests from the file .properties
     * @return The list of requests
     */
    private ReferenceList getRequestList(  )
    {
        ReferenceList list = new ReferenceList(  );
        list.addItem( 0, "" );

        String strQueryLabel = null;

        int i = 1;
        String strKey = PROPERTY_QUERY + i + SUFFIX_LABEL;

        while ( ( strQueryLabel = AppPropertiesService.getProperty( strKey ) ) != null )
        {
            list.addItem( i, strQueryLabel );
            i++;
            strKey = PROPERTY_QUERY + i + SUFFIX_LABEL;
        }

        return list;
    }
}
