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

import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.Field;
import fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.Cartereponse;
import fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.Famille;
import fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.ObjectFactory;
import fr.paris.lutece.plugins.formengine.web.SubForm;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;


/**
 *  First family subform
 */
public class FirstFamilySubForm extends SubForm
{
    //properties
    private static final String PROPERTY_FRAGMENT_XSL_FILE_FORM = ".xsl.file.form.firstFamily";
    private static final String PROPERTY_FRAGMENT_TEMPLATE_HEADER = ".template.header.firstFamily";

    //fields
    private static final String FIELD_NAME_LAST_NAME = "LastName";
    private static final String FIELD_NAME_FIRST_NAME = "FirstName";
    private static final String FIELD_NAME_PHONE = "phone";
    private static final String FIELD_NAME_EMAIL = "email";
    private static final String FIELD_NAME_NUMBER_CHILDS = "NumberChilds";

    /**
    *
    */
    public FirstFamilySubForm(  )
    {
        super(  );
    }

    /**
     *  Two action provided : ACTION_NAME_PREVIOUS, ACTION_NAME_NEXT
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
        else if ( strActionName.equals( Constants.ACTION_NAME_NEXT ) )
        {
            boolean bValid = doValidFields( request );

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
        else if ( strActionName.equals( Constants.ACTION_NAME_PREVIOUS ) )
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
     * Valid the fields
     * @param request the request
     * @return true if the fields are valid
     */
    private boolean doValidFields( HttpServletRequest request )
    {
        boolean bValid = false;

        // set the fields' content
        this.fillFields( request );

        // check that the submitted data is valid
        bValid = this.validateFields( request );

        if ( bValid )
        {
            //pour enregistré dans le fichier xml

            //create the ObjectFactory
            ObjectFactory factory = new ObjectFactory(  );

            //retrieve the root element in session
            Cartereponse carteReponse;
            carteReponse = (Cartereponse) getParentForm(  ).getFormDocument( request );

            if ( carteReponse == null )
            {
                carteReponse = factory.createCartereponse(  );
            }

            Famille famille1 = carteReponse.getFamille1(  );

            if ( famille1 == null )
            {
                famille1 = factory.createFamille(  );
            }

            Field fieldFirstName = this.getFieldFromName( request, FIELD_NAME_FIRST_NAME );
            famille1.setPrenom( fieldFirstName.getValue(  ) );

            Field fieldLastName = this.getFieldFromName( request, FIELD_NAME_LAST_NAME );
            famille1.setNom( fieldLastName.getValue(  ) );

            Field fieldPhone = this.getFieldFromName( request, FIELD_NAME_PHONE );
            famille1.setTelephone( fieldPhone.getValue(  ) );

            Field fieldEmail = this.getFieldFromName( request, FIELD_NAME_EMAIL );
            famille1.setCourriel( fieldEmail.getValue(  ) );

            Field fieldNumberChilds = this.getFieldFromName( request, FIELD_NAME_NUMBER_CHILDS );
            famille1.setNbEnfant( new BigInteger( fieldNumberChilds.getValue(  ) ) );

            carteReponse.setFamille1( famille1 );
            this.getParentForm(  ).setFormDocument( request, carteReponse );
        }

        return bValid;
    }

    /**
     * @param request the request
     * @return the html to display in summary
     */
    protected String displaySummary( HttpServletRequest request )
    {
        return "";
    }

    /**
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
