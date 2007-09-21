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
package fr.paris.lutece.plugins.formengine.modules.evac.web;

import fr.paris.lutece.plugins.formengine.web.SubForm;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import javax.servlet.http.HttpServletRequest;


/**
 * Subform for the confirmation of form
 */
public class ConfirmationSubForm extends SubForm
{
    //properties
    private static final String PROPERTY_FRAGMENT_XSL_FILE_SUMMARY = ".xsl.file.summary.confirmation";
    private static final String PROPERTY_FRAGMENT_XSL_FILE_FORM = ".xsl.file.form.confirmation";
    private static final String PROPERTY_FRAGMENT_TEMPLATE_HEADER = ".template.header.sans.onglet";

    //actions
    private static final String ACTION_NAME_COMFIRM = "Fermer";

    /**
     *
     */
    public ConfirmationSubForm(  )
    {
        super(  );
    }

    /**
     *  One action provided : ACTION_NAME_COMFIRM
     * @param strActionName the action name
     * @param request the request
     * @return the name of next SubForm
     */
    public String doAction( String strActionName, HttpServletRequest request )
    {
        String strNextSubForm = "";

        if ( strActionName == null )
        {
            strNextSubForm = this.getName(  );
        }
        else if ( strActionName.equals( ACTION_NAME_COMFIRM ) )
        {
            strNextSubForm = this.getPreviousSubForm(  ).getName(  );
            this.setIsSubFormAllowed( request, false );
        }
        else
        {
            strNextSubForm = this.getName(  );
        }

        return strNextSubForm;
    }

    /**
     * @param request the request
     * @return the html to display in summary
     */
    protected String displaySummary( HttpServletRequest request )
    {
        String strHtml = "";

        // generate xml
        String strXml = getParentForm(  ).getXmlFormDocument( request );

        // generate html code via xsl transform
        String strFileName = AppPropertiesService.getProperty( this.getParentForm(  ).getName(  ) +
                PROPERTY_FRAGMENT_XSL_FILE_SUMMARY );
        strHtml = this.getHtml( strXml, strFileName );

        return strHtml;
    }

    /**
     * @see fr.paris.lutece.plugins.formengine.web.SubForm#getXslFormElementsFileName()
     * @return the xsl form elemnt of this SubForm
     */
    protected String getXslFormElementsFileName(  )
    {
        return AppPropertiesService.getProperty( this.getParentForm(  ).getName(  ) + PROPERTY_FRAGMENT_XSL_FILE_FORM );
    }

    /**
     * this method is used to display form header
     * @param request the used request
     * @return the html content of header template
     */
    protected String displayHeader( HttpServletRequest request )
    {
        HtmlTemplate template = AppTemplateService.getTemplate( AppPropertiesService.getProperty( getParentForm(  )
                                                                                                      .getName(  ) +
                    PROPERTY_FRAGMENT_TEMPLATE_HEADER ) );

        template.substitute( Constants.BOOKMARK_FORM_TITLE, this.getParentForm(  ).getTitle(  ) );
        template.substitute( Constants.BOOKMARK_SUBFORM_TITLE, this.getTitle(  ) );

        return template.getHtml(  );
    }
}
