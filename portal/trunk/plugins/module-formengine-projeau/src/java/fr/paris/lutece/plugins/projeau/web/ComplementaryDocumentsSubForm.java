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
import fr.paris.lutece.plugins.projeau.business.ProjeauFormDocument;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.fileupload.FileItem;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;


/**
 * This class is reponsible for running the "Complementary documents" subform for the "Projeau" formengine module.
 */
public class ComplementaryDocumentsSubForm extends SubForm
{
    private static final String PROPERTY_FRAGMENT_XSL_FILE_FORM = ".xsl.file.form.complementary.documents";
    private static final String PROPERTY_FRAGMENT_TEMPLATE_HEADER = ".template.header.complementary.documents";
    private static final String ACTION_NAME_NEXT = "Accèder au récapitulatif";
    private static final String ACTION_NAME_PREVIOUS = "Retour";
    private static final String FIELD_NAME_DOCUMENTS = "internetComplementaryDocuments";

    /**
     * Constructor
     */
    public ComplementaryDocumentsSubForm(  )
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
     * <li>For the "next" action : validate, store the complementary documents (uses doSetComplementaryDocs) and go to the next form</li>
     * <li>For the "previous" action : go back to the previous form</li>
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
        else if ( strActionName.equals( ACTION_NAME_NEXT ) )
        {
            boolean bValid = doSetComplementaryDocs( request );

            if ( bValid )
            {
                strNextSubForm = this.getNextSubForm(  ).getName(  );
                this.getParentForm(  ).setIsAccessToSubFormAllowed( request, strNextSubForm, true );
            }
            else
            {
                strNextSubForm = this.getName(  );
            }
        }
        else if ( strActionName.equals( ACTION_NAME_PREVIOUS ) )
        {
            this.fillFields( request );
            strNextSubForm = this.getPreviousSubForm(  ).getName(  );
        }
        else
        {
            strNextSubForm = this.getName(  );
        }

        return strNextSubForm;
    }

    /**
     * Stores the data from the subform's fields into the business object in session.
     * This method first validates the fields. If validation succeeds, the data storage is performed.
     * @param request the http request
     * @return true if the field data to store is valid, false otherwise.
     */
    private boolean doSetComplementaryDocs( HttpServletRequest request )
    {
        boolean bValid;

        // set the fields' content
        this.fillFields( request );

        // check that the submitted data is valid
        bValid = this.validateFields( request );

        if ( bValid )
        {
            ProjeauFormDocument formDocument = (ProjeauFormDocument) this.getParentForm(  ).getFormDocument( request );
            ArrayList<FileItem> fileList = (ArrayList<FileItem>) this.getFileItems( request, FIELD_NAME_DOCUMENTS );

            formDocument.setUploadedDocumentsList( fileList );
            this.getParentForm(  ).setFormDocument( request, formDocument );
        }

        return bValid;
    }

    /**
     * No summary to display for this subform - this method returns the empty string
     * @see fr.paris.lutece.plugins.formengine.web.SubForm#displaySummary(javax.servlet.http.HttpServletRequest)
     */
    protected String displaySummary( HttpServletRequest request )
    {
        return "";
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
