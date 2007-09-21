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
package fr.paris.lutece.plugins.formengine.web;

import fr.paris.lutece.plugins.formengine.service.FormsRegistrationService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;


public class FormEngineApp implements XPageApplication
{
    private static final String TEMPLATE_ACCESS_ERROR = "/skin/plugins/formengine/access_error.html";
    private static final String PARAMETER_PLUGIN_NAME = "page";
    private static final String PARAMETER_FORM_NAME = "form";
    private static final String PARAMETER_SUBFORM_NAME = "subform";
    private static final String PARAMETER_ACTION_NAME = "action";
    private static final String SESSION_ATTRIBUTE_FORM_REQUEST_URL = "REQUEST_URL";
    private static final String PROPERTY_MESSAGE_SESSION_LOST = "formengine.session.lost";

    /**
     * Constructor
     * Builds the list of avalaible forms
     * from the form definitions directory
     */
    public FormEngineApp(  )
    {
        FormsRegistrationService.loadForms(  );
    }

    /**
     * Get the xpage that display the form contents
     * @param request the http request
     * @param nMode
     * @param plugin
     * @return the xpage for the forms
     * @throws UserNotSignedException
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
        throws UserNotSignedException
    {
        XPage page = new XPage(  );

        // retrieve the parameters
        String strFormName = request.getParameter( PARAMETER_FORM_NAME );
        String strSubFormName = request.getParameter( PARAMETER_SUBFORM_NAME );

        // get the form from its name
        Form form = FormsRegistrationService.getForm( strFormName );

        SubForm subForm = null;

        if ( form == null )
        {
            // if the form is not found, display an error message
            page.setContent( this.getAccessErrorMessage(  ) );
        }
        else
        {
            if ( ( strSubFormName == null ) || ( strSubFormName.trim(  ).equals( "" ) ) )
            {
                // if no subform name was stated, choose the first subform
                subForm = form.getFirstSubForm(  );
            }
            else
            {
                // else retrieve the subform from its name
                subForm = form.getSubForm( strSubFormName );
            }

            if ( subForm == null )
            {
                // if no subform was found, display an error
                page.setContent( this.getAccessErrorMessage(  ) );
            }
            else
            {
                // don't forget to allow access to the first subform
                if ( subForm.getPreviousSubForm(  ) == null )
                {
                    // grant access to the first subform
                    subForm.setIsSubFormAllowed( request, true );

                    // add to session the url to use (will the first url used to call the form)
                    // only add it on first display of first subform
                    if ( form.getSessionAttribute( request, SESSION_ATTRIBUTE_FORM_REQUEST_URL ) == null )
                    {
                        form.setSessionAttribute( request, SESSION_ATTRIBUTE_FORM_REQUEST_URL, request.getRequestURL(  ) );
                    }
                }

                // display result
                page.setContent( subForm.display( request ) );
                page.setTitle( form.getTitle(  ) );
                page.setPathLabel( form.getTitle(  ) + " - " + subForm.getTitle(  ) );

                // reset errors
                form.resetErrors( request );

                // if we are at the last subform, clean the session
                if ( subForm.getNextSubForm(  ) == null )
                {
                    form.flushSession( request );
                }
            }
        }

        return page;
    }

    /**
     * Set the display URL of a form depending on the given subform name
     * @param request the http request - gives the plugin name and the form name
     * @param strSubFormName the name of the subform to display
     * @return the url to display the given subform
     */
    public String setUrl( HttpServletRequest request, String strSubFormName )
    {
        String strUrl;
        String strPluginName = SharedConstants.PLUGIN_NAME;
        String strFormName = request.getParameter( PARAMETER_FORM_NAME );

        // get the form from the name in request
        Form form = FormsRegistrationService.getForm( strFormName );

        if ( form != null )
        {
            // retrieve the url to use (stored in session)
            StringBuffer sbSessionAttribute = (StringBuffer) form.getSessionAttribute( request,
                    SESSION_ATTRIBUTE_FORM_REQUEST_URL );
            strUrl = "?" + PARAMETER_PLUGIN_NAME + "=" + strPluginName + "&" + PARAMETER_FORM_NAME + "=" + strFormName;

            // if session is lost
            if ( sbSessionAttribute == null )
            {
                strUrl = AppPathService.getBaseUrl( request ) + AppPathService.getPortalUrl(  ) + strUrl;
            }
            else
            {
                strUrl = sbSessionAttribute.toString(  ) + strUrl + "&" + PARAMETER_SUBFORM_NAME + "=" +
                    strSubFormName;
            }

            // add the form parameter and subform parameter
        }
        else
        {
            strUrl = null;
        }

        return strUrl;
    }

    /**
     * Perform an action on a given subform
     * @param request the http request - gives the form, subform and action name
     * @return the name of the subform to display after having performed the requested action
     */
    public String doAction( HttpServletRequest request )
    {
        // get the request parameters
        String strFormName = request.getParameter( PARAMETER_FORM_NAME );
        String strSubFormName = request.getParameter( PARAMETER_SUBFORM_NAME );
        String strActionName = request.getParameter( PARAMETER_ACTION_NAME );

        // default next subform (in case of error) is current subform
        String strNextSubForm = strSubFormName;

        // get the form from its name
        Form form = FormsRegistrationService.getForm( strFormName );

        // check session
        if ( !request.isRequestedSessionIdValid(  ) )
        {
            form.addErrorMessage( request, AppPropertiesService.getProperty( PROPERTY_MESSAGE_SESSION_LOST ) );

            return strSubFormName;
        }

        // Special case for upload fields : if no action is specified, a submit
        // button associated with an upload might have been pressed :
        if ( strActionName == null )
        {
            String strUploadAction = getUploadAction( request );

            if ( strUploadAction != null )
            {
                return form.getSubForm( strSubFormName ).doUploadAction( request, strUploadAction );
            }
        }

        SubForm subForm = null;

        if ( form != null ) // if we have a form corresponding to the name
        {
            // get the subform from its name
            subForm = form.getSubForm( strSubFormName );

            if ( subForm != null ) // if we have a subform corresponding to the name
            {
                if ( subForm.getIsSubFormAllowed( request ) )
                {
                    // perform the action
                    strNextSubForm = subForm.doAction( strActionName, request );
                }
            }
        }

        return strNextSubForm;
    }

    /**
     * Get the error message from its template
     * @return the html code for an error
     */
    private String getAccessErrorMessage(  )
    {
        HtmlTemplate templateNotFound = AppTemplateService.getTemplate( TEMPLATE_ACCESS_ERROR );

        return templateNotFound.getHtml(  );
    }

    /**
     * Checks the request parameters to see if an upload submit has been
     * called.
     *
     * @param request the HTTP request
     * @return the name of the upload action, if any. Null otherwise.
     */
    private static String getUploadAction( HttpServletRequest request )
    {
        Enumeration enumParamNames = request.getParameterNames(  );

        while ( enumParamNames.hasMoreElements(  ) )
        {
            String paramName = (String) enumParamNames.nextElement(  );

            if ( paramName.startsWith( SharedConstants.UPLOAD_SUBMIT_PREFIX ) ||
                    paramName.startsWith( SharedConstants.UPLOAD_DELETE_PREFIX ) )
            {
                return paramName;
            }
        }

        return null;
    }
}
