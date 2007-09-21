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

import fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.Adresse;
import fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.Cartereponse;
import fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.Famille;
import fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.ObjectFactory;
import fr.paris.lutece.plugins.formengine.modules.evac.service.output.EvacIdGenerator;
import fr.paris.lutece.plugins.formengine.modules.evac.utils.EvacUtils;
import fr.paris.lutece.plugins.formengine.service.output.IdGenerator;
import fr.paris.lutece.plugins.formengine.web.SubForm;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * Summary Subform
 *
 */
public class SummarySubForm extends SubForm
{
    //properties
    private static final String PROPERTY_FRAGMENT_XSL_FILE_SUMMARY = ".xsl.file.summary.recapitulatif";
    private static final String PROPERTY_FRAGMENT_XSL_FILE_FORM = ".xsl.file.form.recapitulatif";
    private static final String PROPERTY_FRAGMENT_TEMPLATE_HEADER = ".template.header.Recapitulatif";

    //actions
    private static final String ACTION_NAME_MODIFY_FIRST_FAMILY = "Modifier la 1ère famille";
    private static final String ACTION_NAME_MODIFY_SECOND_FAMILY = "Modifier la 2de famille";
    private static final String ACTION_NAME_SECOND_FAMILY = "2de famille";
    private static final String ACTION_NAME_VALID = "Valider";

    //parameters for templates
    private static final String PARAMETER_HIDE_BUTTON_SECOND_FAMILY = "hideButtonSecondFamily";
    private static final String PARAMETER_HIDE_RECAP_SECOND_FAMILY = "hideRecapSecondFamily";
    private static final String PARAMETER_FIRST_FAMILY_TYPE_VOIE = "hideEspaceFirstFamilyTypeVoie";
    private static final String PARAMETER_SECOND_FAMILY_TYPE_VOIE = "hideEspaceSecondFamilyTypeVoie";

    //parameters in session
    private static final String SECOND_FAMILY_VALIDATE = "LUTECE_FORMENGINE_EVAC_ADDRESSSECONDFAMILY_SECOND_FAMILY_VALIDATE";

    //Utils
    private static final String SHOW = "0";
    private static final String HIDE = "1";

    /**
     *
     */
    public SummarySubForm(  )
    {
        super(  );
    }

    /**
     *  Two action provided : ACTION_NAME_VALID, ACTION_NAME_MODIFY
     * @param strActionName the action name
     * @param request the request
     * @return the name of next SubForm
     */
    public String doAction( String strActionName, HttpServletRequest request )
    {
        String strNextSubForm = "";
        Cartereponse carteReponse;

        if ( strActionName == null )
        {
            strNextSubForm = this.getName(  );
        }
        else if ( strActionName.equals( ACTION_NAME_VALID ) )
        {
            boolean bValid = doSetId( request );
            carteReponse = (Cartereponse) getParentForm(  ).getFormDocument( request );

            EvacForm parentForm = (EvacForm) this.getParentForm(  );
            parentForm.processForm( carteReponse, request );

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
        else if ( strActionName.equals( ACTION_NAME_MODIFY_FIRST_FAMILY ) )
        {
            this.setIsSubFormAllowed( request, false );
            this.getPreviousSubForm(  ).setIsSubFormAllowed( request, false );
            this.getPreviousSubForm(  ).getPreviousSubForm(  ).setIsSubFormAllowed( request, false );

            strNextSubForm = this.getPreviousSubForm(  ).getPreviousSubForm(  ).getPreviousSubForm(  )
                                 .getPreviousSubForm(  ).getName(  );
        }
        else if ( strActionName.equals( ACTION_NAME_MODIFY_SECOND_FAMILY ) )
        {
            this.setIsSubFormAllowed( request, false );
            this.getPreviousSubForm(  ).setIsSubFormAllowed( request, false );

            strNextSubForm = this.getPreviousSubForm(  ).getPreviousSubForm(  ).getName(  );
        }
        else if ( strActionName.equals( ACTION_NAME_SECOND_FAMILY ) )
        {
            this.setIsSubFormAllowed( request, false );
            this.getPreviousSubForm(  ).setIsSubFormAllowed( request, false );

            strNextSubForm = this.getPreviousSubForm(  ).getPreviousSubForm(  ).getName(  );
        }
        else
        {
            strNextSubForm = this.getName(  );
        }

        return strNextSubForm;
    }

    /**
     * Valid the field and generate the new id of carte reponse
     * @param request the request
     * @return true if the fields is valid
     */
    private boolean doSetId( HttpServletRequest request )
    {
        boolean bValid = false;

        // set the fields' content
        this.fillFields( request );

        // check that the submitted data is valid
        bValid = this.validateFields( request );

        if ( bValid )
        {
            // create the ObjectFactory
            ObjectFactory factory = new ObjectFactory(  );

            // retrieve the root element in session
            Cartereponse carteReponse;
            carteReponse = (Cartereponse) this.getParentForm(  ).getFormDocument( request );

            if ( carteReponse == null )
            {
                carteReponse = factory.createCartereponse(  );
            }

            IdGenerator idGenerator = new EvacIdGenerator(  );
            carteReponse.setNumero( idGenerator.getNewId( this.getParentForm(  ) ) );

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
        String strHtml = "";

        Map<String, String> params = new HashMap<String, String>(  );

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

        Adresse adresseFamille1 = famille1.getAdresse(  );

        if ( adresseFamille1 == null )
        {
            adresseFamille1 = factory.createAdresse(  );
        }

        if ( EvacUtils.isTerminateByApostrophe( adresseFamille1.getTypeVoie(  ) ) )
        {
            params.put( PARAMETER_FIRST_FAMILY_TYPE_VOIE, SHOW );
        }
        else
        {
            params.put( PARAMETER_FIRST_FAMILY_TYPE_VOIE, HIDE );
        }

        Famille famille2 = carteReponse.getFamille2(  );

        if ( famille2 == null )
        {
            famille2 = factory.createFamille(  );
        }

        Adresse adresseFamille2 = famille2.getAdresse(  );

        if ( adresseFamille2 == null )
        {
            adresseFamille2 = factory.createAdresse(  );
        }

        if ( ( adresseFamille2.getTypeVoie(  ) == null ) ||
                EvacUtils.isTerminateByApostrophe( adresseFamille2.getTypeVoie(  ) ) )
        {
            params.put( PARAMETER_SECOND_FAMILY_TYPE_VOIE, SHOW );
        }
        else
        {
            params.put( PARAMETER_SECOND_FAMILY_TYPE_VOIE, HIDE );
        }

        if ( request.getSession(  ).getAttribute( SECOND_FAMILY_VALIDATE ) != null )
        {
            params.put( PARAMETER_HIDE_RECAP_SECOND_FAMILY, SHOW );
        }
        else
        {
            params.put( PARAMETER_HIDE_RECAP_SECOND_FAMILY, HIDE );
        }

        carteReponse = (Cartereponse) getParentForm(  ).getFormDocument( request );
        // set temp values to allow generation        
        carteReponse.setNumero( Constants.TMP_ID_XML_OUTPUT );

        // generate xml
        String strXml = getParentForm(  ).getXmlFormDocument( request );

        // generate html code via xsl transform
        String strFileName = AppPropertiesService.getProperty( this.getParentForm(  ).getName(  ) +
                PROPERTY_FRAGMENT_XSL_FILE_SUMMARY );
        strHtml = this.getHtml( strXml, strFileName, params );

        carteReponse.setNumero( Constants.TMP_ID_XML_OUTPUT );

        return strHtml;
    }

    /**
     * display the form
     * @param request the request
     * @return a html form
     */
    protected String displayForm( HttpServletRequest request )
    {
        String strForm = "";

        Map<String, String> params = new HashMap<String, String>(  );

        if ( request.getSession(  ).getAttribute( SECOND_FAMILY_VALIDATE ) != null )
        {
            params.put( PARAMETER_HIDE_BUTTON_SECOND_FAMILY, HIDE );
        }
        else
        {
            params.put( PARAMETER_HIDE_BUTTON_SECOND_FAMILY, SHOW );
        }

        strForm = this.buildHtmlForm( getFormElements( request ), params );

        return strForm;
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
