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
package fr.paris.lutece.plugins.dbpage.business.section;

import fr.paris.lutece.plugins.dbpage.business.*;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.sql.SQLException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents business object DbPageSectionForm
 */
public class DbPageSectionForm extends DbPageSection
{
    private static final String PROPERTY_ERROR_MESSAGE = "dbpage.section.properties.message.error";
    private static final String MARK_SECTION_TITLE = "section_title";
    private static final String MARK_SECTION_CONTENT = "section_content";
    private static final String PROPERTY_FILES_PATH = "dbpage.files.path";
    private static final String TEMPLATE_DEFAULT_FORM = "skin/plugins/dbpage/default_form.html";

    /**
     * Returns the Html Section Form
     * @return the html code of the html Section Form
     * @param request The request
     * @param listValues The list of id values to substitute in the SQL request
     */
    public String getHtmlSection( List listValues, HttpServletRequest request )
    {
        HashMap rootModel = new HashMap(  );

        rootModel.put( MARK_SECTION_TITLE, getTitle(  ) );

        if ( getTemplatePath(  ).equals( "" ) )
        {
            HtmlTemplate tForm = AppTemplateService.getTemplate( TEMPLATE_DEFAULT_FORM, request.getLocale(  ), rootModel );
            rootModel.put( MARK_SECTION_CONTENT, buildForm( tForm, listValues ) );

            return tForm.getHtml(  );
        }
        else
        {
            String strFilePath = AppPropertiesService.getProperty( PROPERTY_FILES_PATH );
            HtmlTemplate tForm = AppTemplateService.getTemplate( getTemplatePath(  ), strFilePath,
                    request.getLocale(  ), rootModel );
            rootModel.put( MARK_SECTION_CONTENT, buildForm( tForm, listValues ) );

            return tForm.getHtml(  );
        }
    }

    /**
     * Returns the Html of the filled form
     *
     * @return the Html code of the filled form
     * @param listValues The list of values substitute in the SQL request
     */
    public String buildForm( HtmlTemplate template, List listValues )
    {
        String strQuery = getValuatedQuery( listValues );

        if ( ( strQuery != null ) && !( strQuery.equals( "" ) ) )
        {
            Collection cRow;

            try
            {
                cRow = DbPageHome.selectRows( strQuery, getConnectionService( getDbPool(  ) ) );

                if ( ( cRow != null ) && !cRow.isEmpty(  ) )
                {
                    Iterator iRow = cRow.iterator(  ); //TODO Iterator remove
                    Iterator icollecColumnName = getColumnNames(  ).iterator(  );

                    while ( iRow.hasNext(  ) )
                    {
                        Collection cLine = (Collection) iRow.next(  );
                        Iterator iLine = cLine.iterator(  ); //TODO Iterator remove

                        while ( iLine.hasNext(  ) )
                        {
                            String strBookmarkName = (String) icollecColumnName.next(  );
                            String strElement = (String) iLine.next(  );
                            String strBookmark = "@" + strBookmarkName + "@";
                            template.substitute( strBookmark, strElement );
                        }
                    }

                    return template.getHtml(  );
                }
                else
                {
                    return AppPropertiesService.getProperty( PROPERTY_ERROR_MESSAGE ); //TODO Error handling
                }
            }
            catch ( SQLException e )
            {
                return null;
            }
        }
        else
        {
            return template.getHtml(  );
        }
    }
}
