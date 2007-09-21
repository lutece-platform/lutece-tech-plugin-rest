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
import fr.paris.lutece.portal.service.fileupload.FileUploadService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.fileupload.FileItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class is reponsible for running the "Confirmation" subform for the "Projeau" formengine module.
 */
public class ConfirmationSubForm extends SubForm
{
    private static final String PROPERTY_FRAGMENT_XSL_FILE_FORM = ".xsl.file.form.confirmation";
    private static final String PROPERTY_FRAGMENT_TEMPLATE_HEADER = ".template.header.confirmation";
    private static final String ACTION_NAME_NEXT = "Valider";
    private static final String ACTION_NAME_PREVIOUS = "Retour";

    /**
     * Constructor
     */
    public ConfirmationSubForm(  )
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
     * Performs the action depending on the given action name.<br>
     * <i>No action to perform : this is the last subform.</i>
     * @see fr.paris.lutece.plugins.formengine.web.SubForm#doAction(java.lang.String, javax.servlet.http.HttpServletRequest)
     */

    /* public String doAction( String strActionName, HttpServletRequest request )
     {
         return this.getName(  );
     }*/
    public String doAction( String strActionName, HttpServletRequest request )
    {
        String strNextSubForm = "";

        if ( strActionName == null )
        {
            strNextSubForm = this.getName(  );
        }

        if ( strActionName.equals( ACTION_NAME_NEXT ) )
        {
            strNextSubForm = this.getNextSubForm(  ).getName(  );
            this.getParentForm(  ).setIsAccessToSubFormAllowed( request, strNextSubForm, true );
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
     * No summary to display for this subform - this method returns the empty string
     * @see fr.paris.lutece.plugins.formengine.web.SubForm#displaySummary(javax.servlet.http.HttpServletRequest)
     */
    protected String displaySummary( HttpServletRequest request )
    {
        return "";
    }

    /**
     * Display the form area
     * Overrides the displayForm method to allow the setting of the urls on the buttons of this page, and to pass the generated id to display.
     * What is also perform is the copy of all the session business object's fields into xsl parameters in order to
     * build a request that allows the user to print its projeau.
     * @param request the http request
     * @return the html code corrensponding to the last subform
     */
    protected String displayForm( HttpServletRequest request )
    {
        String strForm = "";

        // get the form
        ProjeauForm form = (ProjeauForm) this.getParentForm(  );

        // get the transaction id
        String strTransactionId = form.getTransactionId( request );

        Map params = new HashMap(  );

        // pass the transaction id as a parameter
        if ( strTransactionId != null )
        {
            params.put( SharedConstants.PARAMETER_TRANSACTION_ID, strTransactionId );
        }

        // pass all the projeau elements one by one for the print form
        ProjeauFormDocument formDocument = (ProjeauFormDocument) this.getParentForm(  ).getFormDocument( request );

        if ( formDocument != null )
        {
            Projeau projeau = formDocument.getProjeau(  );

            params.put( SharedConstants.PARAMETER_TITLE, projeau.getTitle(  ) );
            params.put( SharedConstants.PARAMETER_LAST_NAME, projeau.getLastName(  ) );
            params.put( SharedConstants.PARAMETER_FIRST_NAME, projeau.getFirstName(  ) );

            //params.put( SharedConstants.PARAMETER_MEDIATION_REQUEST, projeau.getMediationRequest(  ) );

            // pass the document names concatenated as a string (with a separator) :
            // this has to be done that way as we don't know how many documents we have (and thus how many parameters we have to declare in the xsl)
            String strDocumentListNames = "";
            ArrayList<FileItem> docList = formDocument.getUploadedDocumentsList(  );

            // build the document names list
            if ( ( docList != null ) && ( !docList.isEmpty(  ) ) )
            {
                Iterator<FileItem> it = docList.iterator(  );

                while ( it.hasNext(  ) )
                {
                    FileItem uploadedFile = it.next(  );
                    strDocumentListNames += FileUploadService.getFileNameOnly( uploadedFile );

                    if ( it.hasNext(  ) )
                    {
                        strDocumentListNames += SharedConstants.CONSTANT_FILENAME_LIST_SEPARATOR;
                    }
                }
            }

            params.put( SharedConstants.PARAMETER_DOCUMENT_NAME_LIST, strDocumentListNames );
        }

        // add the form name in parameters
        params.put( SharedConstants.PARAMETER_FORM_NAME, form.getName(  ) );

        // build the html form from the formelement object (provides fields and buttons - here buttons), and from the parameters declared.
        strForm = this.buildHtmlForm( getFormElements( request ), params );

        return strForm;
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
