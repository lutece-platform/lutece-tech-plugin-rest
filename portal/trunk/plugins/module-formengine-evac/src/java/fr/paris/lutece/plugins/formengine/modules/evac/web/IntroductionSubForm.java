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

import fr.paris.lutece.plugins.formengine.modules.evac.service.address.AddressServiceProvider;
import fr.paris.lutece.plugins.formengine.web.CaptchaSubForm;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;


/**
 * Introduction SubForm
 */
public class IntroductionSubForm extends CaptchaSubForm
{
    //properties
    private static final String PROPERTY_FRAGMENT_XSL_FILE_FORM = ".xsl.file.form.introduction";
    private static final String PROPERTY_FRAGMENT_TEMPLATE_HEADER = ".template.header.introduction";
    private static final String PROPERTY_FRAGMENT_TEMPLATE_HEADER_HIDE = ".template.header.sans.onglet";
    
    //actions
    private static final String ACTION_NAME_NEXT_INTRODUCTION = "Débuter votre démarche";

    //web service
    private static final String ADDRESS_SEVICE_UNAVAILABLE = "ADDRESS_SEVICE_UNAVAILABLE";
    private static final String TEMPLATE_ERROR_ADDRESS_SERVICE = ".template.error.message.addressServiceUnavailable";
    private static final String ADDRESS_SEVICE_ID_TEST = ".address.id.test";

    /**
    *
    */
    public IntroductionSubForm(  )
    {
        super(  );
    }

    /**
     *  Two action provided : ACTION_NAME_NEXT_INTRODUCTION
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
        else if ( strActionName.equals( ACTION_NAME_NEXT_INTRODUCTION ) )
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
        else
        {
            strNextSubForm = this.getName(  );
        }

        return strNextSubForm;
    }

    /**
     *
     * @param request the request
     * @return true if fields are valid
     */
    private boolean doValidFields( HttpServletRequest request )
    {
        boolean bValid = false;

        // set the fields' content
        this.fillFields( request );

        // check that the submitted data is valid
        bValid = this.validateFields( request );

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
    	try
        {
            long lIdTest = Long.parseLong( AppPropertiesService.getProperty( this.getParentForm(  ).getName(  ) +
                        ADDRESS_SEVICE_ID_TEST ) );
            AddressServiceProvider.getAdresseInfo( lIdTest, true );
        }
        catch ( RemoteException e )
        {
        	 HtmlTemplate template = AppTemplateService.getTemplate( AppPropertiesService.getProperty( getParentForm(  )
                     .getName(  ) +
                     	PROPERTY_FRAGMENT_TEMPLATE_HEADER_HIDE ) );

        	 	template.substitute( Constants.BOOKMARK_FORM_TITLE, this.getParentForm(  ).getTitle(  ) );
        	 	template.substitute( Constants.BOOKMARK_SUBFORM_TITLE, this.getTitle(  ) );

        	 	return template.getHtml(  );
        }
    	
        HtmlTemplate template = AppTemplateService.getTemplate( AppPropertiesService.getProperty( getParentForm(  )
                                                                                                      .getName(  ) +
                    PROPERTY_FRAGMENT_TEMPLATE_HEADER ) );

        template.substitute( Constants.BOOKMARK_FORM_TITLE, this.getParentForm(  ).getTitle(  ) );
        template.substitute( Constants.BOOKMARK_SUBFORM_TITLE, this.getTitle(  ) );

        return template.getHtml(  );
    }

    /**
     * display the form
     * @param request the request
     * @return a html form
     */
    protected String displayForm( HttpServletRequest request )
    {
        String strForm = "";

        try
        {
            long lIdTest = Long.parseLong( AppPropertiesService.getProperty( this.getParentForm(  ).getName(  ) +
                        ADDRESS_SEVICE_ID_TEST ) );
            AddressServiceProvider.getAdresseInfo( lIdTest, true );
        }
        catch ( RemoteException e )
        {
            HtmlTemplate template = AppTemplateService.getTemplate( AppPropertiesService.getProperty( this.getParentForm(  )
                                                                                                          .getName(  ) +
                        TEMPLATE_ERROR_ADDRESS_SERVICE ) );
            strForm = template.getHtml(  );
            this.setSessionAttribute( request, ADDRESS_SEVICE_UNAVAILABLE, true );

            return strForm;
        }

        return super.displayForm( request );
    }
}
