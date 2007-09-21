/*
 * Copyright (c) 2002-2006, Mairie de Paris
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
package fr.paris.lutece.plugins.projeau.web;

import fr.paris.lutece.plugins.formengine.web.SubForm;
import fr.paris.lutece.plugins.projeau.business.Projeau;
import fr.paris.lutece.plugins.projeau.business.ProjeauFormDocument;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class is reponsible for running the "Summary" subform for the "Projeau" formengine module.
 */
public class SummarySubForm extends SubForm
{
    private static final String PROPERTY_FRAGMENT_XSL_FILE_FORM = ".xsl.file.form.summary";
    private static final String PROPERTY_FRAGMENT_XSL_FILE_SUMMARY = ".xsl.file.data.summary";
    private static final String PROPERTY_FRAGMENT_TEMPLATE_HEADER = ".template.header.summary";
    private static final String ACTION_NAME_CONFIRM = "Confirmer";
    private static final String ACTION_NAME_MODIFY = "Modifier";

    /**
     * Constructor
     */
    public SummarySubForm(  )
    {
        super(  );
    }

    /**
     * Get the xsl associated with this subform and needed to display the form area
     * @see fr.paris.lutece.plugins.formengine.web.SubForm#getXslFormElementsFileName()
     */
    protected String getXslFormElementsFileName(  )
    {
        String strProperty = this.getParentForm(  ).getName(  ) + PROPERTY_FRAGMENT_XSL_FILE_FORM;

        return AppPropertiesService.getProperty( strProperty );
    }

    /**
     * Performs the action depending on the given action name
     * <ul>
     * <li>For the "confirmation" action : call the ProjeauForm.processForm method and go to the last form</li>
     * <li>For the "modification" action : go back to the second form (the one after the introduction)</li>
     * </ul>
     * @see fr.paris.lutece.plugins.formengine.web.SubForm#doAction(java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    public String doAction( String strActionName, HttpServletRequest request )
    {
        String strNextSubForm = "";

        if ( strActionName == null )
        {
            strNextSubForm = this.getName(  );
        }
        else if ( strActionName.equals( ACTION_NAME_CONFIRM ) )
        {
            // call the processForm method in order to finish the process (generation of the outputs, ...).
            ProjeauForm form = (ProjeauForm) this.getParentForm(  );
            form.processForm( request );

            strNextSubForm = this.getNextSubForm(  ).getName(  );
            this.getParentForm(  ).setIsAccessToSubFormAllowed( request, strNextSubForm, true );
        }
        else if ( strActionName.equals( ACTION_NAME_MODIFY ) )
        {
            this.fillFields( request );

            // get the identity subform, ie the 2nd subform
            SubForm nextSubForm = this.getParentForm(  ).getFirstSubForm(  ).getNextSubForm(  );
            strNextSubForm = nextSubForm.getName(  );
        }
        else
        {
            strNextSubForm = this.getName(  );
        }

        return strNextSubForm;
    }

    /**
     * Builds the summary area html code. This code is built from the business in session.
     * Actually, this object provides xml code correponding to the data stored in session,
     * and an xsl stylesheet is associated to it in order to provide the corresponding html code.
     * @see fr.paris.lutece.plugins.formengine.web.SubForm#displaySummary(javax.servlet.http.HttpServletRequest)
     */
    protected String displaySummary( HttpServletRequest request )
    {
        // generate xml
        ProjeauForm form = (ProjeauForm) getParentForm(  );

        String strXml = form.getXmlFormDocument( request );

        // generate html code via xsl transform
        String strXslFileName = AppPropertiesService.getProperty( form.getName(  ) +
                PROPERTY_FRAGMENT_XSL_FILE_SUMMARY );

        Map params = new HashMap(  );

        String strHtml = this.getHtml( strXml, strXslFileName, params );

        return strHtml;
    }

    /**
     * Display the header of this subform as a set of tabs
     * @see fr.paris.lutece.plugins.formengine.web.SubForm#displayHeader(javax.servlet.http.HttpServletRequest)
     */
    protected String displayHeader( HttpServletRequest request )
    {
        HtmlTemplate template = AppTemplateService.getTemplate( AppPropertiesService.getProperty( getParentForm(  )
                                                                                                      .getName(  ) +
                    PROPERTY_FRAGMENT_TEMPLATE_HEADER ) );

        template.substitute( SharedConstants.BOOKMARK_FORM_TITLE, this.getParentForm(  ).getTitle(  ) );
        template.substitute( SharedConstants.BOOKMARK_SUBFORM_TITLE, this.getTitle(  ) );

        return template.getHtml(  );
    }
}
