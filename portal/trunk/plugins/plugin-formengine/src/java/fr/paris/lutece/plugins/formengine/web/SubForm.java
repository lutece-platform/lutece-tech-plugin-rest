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

import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.CheckFieldRules;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.CheckRule;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.CheckSubFormRules;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.Choice;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.ChoiceList;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.Field;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.Fields;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.FileName;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.FormElements;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.ObjectFactory;
import fr.paris.lutece.plugins.formengine.service.XslOutputPropertiesLoader;
import fr.paris.lutece.plugins.formengine.service.validator.FieldValidator;
import fr.paris.lutece.plugins.formengine.service.validator.SubFormValidator;
import fr.paris.lutece.plugins.formengine.service.validator.ValidatorFactory;
import fr.paris.lutece.portal.service.fileupload.FileUploadService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.upload.MultipartHttpServletRequest;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.filesystem.UploadUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.xml.XmlUtil;

import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;


/**
 * This classe defines the main characteristics and methods of a subform
 */
public abstract class SubForm
{
    private static final String SESSION_ATTRIBUTE_SUBFORM_ALLOWED = "SUBFORM_ALLOWED";
    private static final String SESSION_ATTRIBUTE_FORM_ELEMENTS = "FORM_ELEMENTS";
    private static final String TEMPLATE_MAIN = "/skin/plugins/formengine/subform_main_template.html";
    private static final String TEMPLATE_HEADER = "/skin/plugins/formengine/subform_template_header.html";
    private static final String TEMPLATE_ERROR = "/skin/plugins/formengine/error.html";
    private static final String TEMPLATE_ACCESS_FORBIDDEN = "/skin/plugins/formengine/access_forbidden.html";
    private static final String BOOKMARK_ERROR_MESSAGE = "@error_message@";
    private static final String BOOKMARK_ERROR_LIST = "@error_list@";
    private static final String BOOKMARK_HEADER = "@subform_header@";
    private static final String BOOKMARK_SUMMARY = "@selection_summary@";
    private static final String BOOKMARK_SUBFORM_CONTENT = "@subform_content@";
    private static final String BOOKMARK_SUBFORM_TITLE = "@subform_title@";
    private static final String BOOKMARK_FORM_TITLE = "@form_title@";
    private static final String BOOKMARK_PORTAL_URL = "@portal_url@";
    private static final String PROPERTY_FRAGMENT_URL_FOR_LAST = ".url.exit";
    private static final String PROPERTY_MESSAGE_EXISTING_FILE = "formengine.validator.message.existingFile";
    private static final String PARAMETER_XSL_FORM_NAME = "formName";
    private static final String PARAMETER_XSL_SUBFORM_NAME = "subFormName";
    private static final String PARAMETER_XSL_URL_EXIT = "defaultUrlToExit";
    private static final Object PARAMETER_XSL_UPLOAD_SUBMIT_PREFIX = "uploadSubmitPrefix";
    private static final Object PARAMETER_XSL_UPLOAD_DELETE_PREFIX = "uploadDeletePrefix";
    private static final Object PARAMETER_XSL_UPLOAD_CHECKBOX_PREFIX = "uploadCheckboxPrefix";
    private String _strTitle;
    private String _strName;
    private Form _formParent;
    private SubForm _subFormNext;
    private SubForm _subFormPrevious;
    private FormElements _initialFormElements;

    /**
     * Constructor
     */
    public SubForm(  )
    {
    }

    ////////////////////////////////////////////////////////////////////////////
    // Abstract methods

    /**
     * This method should be overriden.
     * It defines the name of the file associated with this subform
     * @return The absolute path for xsl stylesheets
     */
    protected abstract String getXslFormElementsFileName(  );

    /**
     * Performs an action depending on the given action name
     * and possibly on other parameters in request
     * @param strActionName the action to perform
     * @param request the http request
     * @return the name of the subform to display after the requested action is performed.
     */
    public abstract String doAction( String strActionName, HttpServletRequest request );

    /**
     * Displays the summary part of the screen.
     * @param request the http request
     * @return the html code corresponding to the summary
     */
    protected abstract String displaySummary( HttpServletRequest request );

    ////////////////////////////////////////////////////////////////////////////
    // Default implementation

    /**
     * This method provides a default implementation for the form display
     * It might be overriden if needed :
     * <ul>
     * <li>to initialize some fields from db (eg. combos)</li>
     * <li>to pass parameters to the stylesheet</li>
     * </ul>
     * @param request the http request
     * @return the html code corresponding to the form part
     */
    protected String displayForm( HttpServletRequest request )
    {
        FormElements formElements = this.getFormElements( request );

        // build the form page
        String strForm = "";

        // generate html code via xsl transform
        strForm = this.buildHtmlForm( formElements, null );

        return strForm;
    }

    /**
     *
     * @param request The HttpServletRequest
     * @return The html code to display
     */
    protected String displayHeader( HttpServletRequest request )
    {
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_HEADER );
        template.substitute( BOOKMARK_FORM_TITLE, this.getParentForm(  ).getTitle(  ) );
        template.substitute( BOOKMARK_SUBFORM_TITLE, this.getTitle(  ) );

        // Added in v1.3
        String strPortalUrl = request.getRequestURI(  );
        template.substitute( BOOKMARK_PORTAL_URL, strPortalUrl );

        return template.getHtml(  );
    }

    /**
    * This method should display. This is the main method called to display a subform.
    * <ul>
    * <li>the recap of what is already submitted</li>
    * <li>the error list</li>
    * <li>the form elements</li>
    * </ul>
    * @param request the http request
    * @return the html code to display
    */
    public String display( HttpServletRequest request )
    {
        if ( !this.getParentForm(  ).checkIsAccessToSubFormAllowed( request, this.getName(  ) ) )
        {
            HtmlTemplate templateForbidden = AppTemplateService.getTemplate( TEMPLATE_ACCESS_FORBIDDEN );

            return templateForbidden.getHtml(  );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MAIN );

        // display the header
        template.substitute( BOOKMARK_HEADER, this.displayHeader( request ) );

        // Added in v1.3
        String strPortalUrl = request.getRequestURI(  );
        template.substitute( BOOKMARK_PORTAL_URL, strPortalUrl );

        // display recap area
        template.substitute( BOOKMARK_SUMMARY, this.displaySummary( request ) );

        // display error message
        template.substitute( BOOKMARK_ERROR_LIST, this.getFormattedErrors( request ) );

        // display the form area
        template.substitute( BOOKMARK_SUBFORM_CONTENT, this.displayForm( request ) );

        return template.getHtml(  );
    }

    /**
    * Get the title of the subform
    * @return the subform title
    */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * Set the title of the subform
     * @param strTitle the title to set
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Get the name of the subform
     * @return the subform name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Set the name of the subform
     * @param strName the name to set
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * setter for the parent form.
         * @param formParent The parent Form
     */
    public void setParentForm( Form formParent )
    {
        _formParent = formParent;
    }

    /**
     * Accessor to the parent form.
     * @return the parent form
     */
    public Form getParentForm(  )
    {
        return _formParent;
    }

    /**
     * setter for the next subform.
     * @param subFormNext tje next subform
     */
    public void setNextSubForm( SubForm subFormNext )
    {
        _subFormNext = subFormNext;
    }

    /**
     * Accessor to the next subform.
     * @return the next subform
     */
    public SubForm getNextSubForm(  )
    {
        return _subFormNext;
    }

    /**
     * setter for the previous subform.
     * @param subFormPrevious the previous subform
     */
    public void setPreviousSubForm( SubForm subFormPrevious )
    {
        _subFormPrevious = subFormPrevious;
    }

    /**
     * Accessor to the previous subform.
     * @return the previous subform
     */
    public SubForm getPreviousSubForm(  )
    {
        return _subFormPrevious;
    }

    /**
     * Util to convert Xml docs into Html docs using XSLT stylesheets
     * @param strXmlCode The Xml
     * @param strXslFileName The Xsl stylesheet <strong>filename</strong>. Note that the path is retrieved from the getXslDirectoryPath method of the parent form
     * @param params a map of parameters to pass to the stylesheet
     * @return The Html code
     */
    protected String getHtml( String strXmlCode, String strXslFileName, Map params )
    {
        String strHtml = "";

        String strXslDirectory = this.getParentForm(  ).getXslDirectoryPath(  );
        String strXslPath = strXslDirectory + "/" + strXslFileName;

        File fileXsl = new File( strXslPath );
        StreamSource sourceStyleSheet = new StreamSource( fileXsl );
        StringReader srInput = new StringReader( strXmlCode );
        StreamSource sourceDocument = new StreamSource( srInput );

        Properties outputProperties = XslOutputPropertiesLoader.getOutputProperties(  );

        try
        {
            strHtml = XmlUtil.transform( sourceDocument, sourceStyleSheet, params, outputProperties );
        }
        catch ( Exception e )
        {
            throw new AppException( "Formengine : an error occurred during XSL transformation - form : " +
                this.getParentForm(  ).getName(  ) + " - subform " + this.getName(  ), e );
        }

        return strHtml;
    }

    /**
     * Util to convert Xml docs into Html docs using XSLT stylesheets
     * @param strXmlCode The Xml
     * @param strXslFileName The Xsl stylesheet <strong>filename</strong>. Note that the path is retrieved from the getXslDirectoryPath method of the parent form
     * @return The Html code
     */
    protected String getHtml( String strXmlCode, String strXslFileName )
    {
        return getHtml( strXmlCode, strXslFileName, null );
    }

    /**
     * Returns a default Html code to display filling errors
     * @param request The HTTP requesr
     * @return Errors presented in HTML
     */
    protected String getFormattedErrors( HttpServletRequest request )
    {
        String[] errors = getParentForm(  ).getErrors( request );
        StringBuffer strErrors = new StringBuffer(  );

        if ( ( errors != null ) && ( errors.length > 0 ) )
        {
            for ( int i = 0; i < errors.length; i++ )
            {
                HtmlTemplate tError = AppTemplateService.getTemplate( TEMPLATE_ERROR );
                //TODO
                // recuperer nom du champ
                // generer url avec #nom_du_champ
                // tError.substitute(balise ouvrante
                // tError.substitute(balise fermante
                tError.substitute( BOOKMARK_ERROR_MESSAGE, errors[i] );
                strErrors.append( tError.getHtml(  ) );
            }
        }
        else
        {
            strErrors.append( "" );
        }

        return strErrors.toString(  );
    }

    /**
     * Validates an given object.
     * Before to add an object to the session, it should be validated.
     * This method check the given object against the associated schema.t
     * @param objectToValidate the object on which validation should be performed
     * @return true if validation has been successfull, false otherwise.
     */
    protected boolean validate( Object objectToValidate )
    {
        boolean bValid = true;

        try
        {
            bValid = this.getParentForm(  ).getXmlValidator(  ).validate( objectToValidate );
        }
        catch ( JAXBException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        return bValid;
    }

    /**
     * Add a variable in session to state wether
     * the access to the current subform is allowed
     * @param request the http request
     * @param bIsAllowed true to allow access to the form, false otherwise
     */
    public void setIsSubFormAllowed( HttpServletRequest request, boolean bIsAllowed )
    {
        // Store the object in a session attribute
        this.setSessionAttribute( request, SESSION_ATTRIBUTE_SUBFORM_ALLOWED, Boolean.valueOf( bIsAllowed ) );
    }

    /**
     * Get the variable in session that states wether
     * the access to the current subform is allowed
     * @param request the http request
     * @return true if the subform access is allowed, false otherwise
     */
    public boolean getIsSubFormAllowed( HttpServletRequest request )
    {
        Boolean bIsAllowed = (Boolean) this.getSessionAttribute( request, SESSION_ATTRIBUTE_SUBFORM_ALLOWED );

        if ( bIsAllowed == null )
        {
            return false;
        }
        else
        {
            return bIsAllowed.booleanValue(  );
        }
    }

    /**
     * Fill the fields with the parameters in request.
     * This method should be called before performing any action, to update the fields
     * with the new values passed in request.
     * @param request the http request
     */
    public void fillFields( HttpServletRequest request )
    {
        FormElements formElements = getFormElements( request );
        Fields fields = formElements.getFields(  );
        Collection<Field> colFields = fields.getField(  );

        for ( Field field : colFields )
        {
            String strName = field.getName(  );

            if ( request.getParameter( strName ) != null )
            {
                field.setValue( request.getParameter( strName ) );
            }
            else
            {
                field.setValue( "" );
            }
        }
    }

    /**
     * Validate the data stored in the fields element against the checkrules specified.
     * This method should be called when submitting a form, in order to check the validity
     * of the data submitted. If a field is not valid, a list of errors is returned.
     * @param request the http request
     * @return true if all the fields are valid. If false, errors are written to the parent form's array of errors.
     */
    public boolean validateFields( HttpServletRequest request )
    {
        boolean bValidate = true;

        FormElements formElements = this.getFormElements( request );
        Fields fields = formElements.getFields(  );

        // try to validate each field separatly
        Collection<Field> colFields = fields.getField(  );

        //Iterator i = colFields.iterator(  );
        FormErrorsList listErrors = new FormErrorsList(  );

        for ( Field field : colFields )
        {
            CheckFieldRules checkFieldRules = field.getCheckFieldRules(  );

            if ( checkFieldRules != null )
            {
                Collection<CheckRule> colRules = checkFieldRules.getCheckRule(  );

                for ( CheckRule rule : colRules )
                {
                    FieldValidator validator = (FieldValidator) ValidatorFactory.getValidator( rule );
                    bValidate &= validator.validate( field, listErrors );
                }
            }
        }

        //check rules that apply to more than one field at a time
        CheckSubFormRules checkSubFormRules = formElements.getCheckSubFormRules(  );

        // TODO enlever la 1ere condition pour avoir toutes les erreurs a la fois ??
        if ( ( bValidate == true ) && ( checkSubFormRules != null ) )
        {
            Collection<CheckRule> colRules = checkSubFormRules.getCheckRule(  );

            for ( CheckRule rule : colRules )
            {
                SubFormValidator validator = (SubFormValidator) ValidatorFactory.getValidator( rule );
                bValidate &= validator.validate( this, request, listErrors );
            }
        }

        // add the errors to the form
        for ( int k = 0; k < listErrors.size(  ); k++ )
        {
            getParentForm(  ).addErrorMessage( request, listErrors.get( k ) );
        }

        return bValidate;
    }

    /**
     * Validate the data stored in an upload field element against the
     * checkrules specified for that field.
     * This method is called automatically by the upload methods.
     *
     * @param request the HTTP request
     * @param field the field to validate
     * @return true if the field is valid. If false, errors are written to the
     * parent form's array of errors.
     */
    private boolean validateUploadField( HttpServletRequest request, Field field )
    {
        boolean bValidate = true;
        FormErrorsList listErrors = new FormErrorsList(  );

        CheckFieldRules checkFieldRules = field.getCheckFieldRules(  );

        if ( checkFieldRules != null )
        {
            Collection<CheckRule> colRules = checkFieldRules.getCheckRule(  );

            for ( CheckRule rule : colRules )
            {
                FieldValidator validator = (FieldValidator) ValidatorFactory.getValidator( rule );
                bValidate &= validator.validate( field, listErrors );
            }
        }

        // add the errors to the form
        for ( int k = 0; k < listErrors.size(  ); k++ )
        {
            getParentForm(  ).addErrorMessage( request, listErrors.get( k ) );
        }

        return bValidate;
    }

    /**
     * Returns the fields element associated with that subform.<br/>
     * If the element is not found in session, a new one is created from the init element "_initialFields"
     * and added to the session.<br/>
     * The formelements element in session is returned
     * @param request the http request
     * @return the formelements object in session
     */
    public FormElements getFormElements( HttpServletRequest request )
    {
        // Retrieve the object from a session attribute
        FormElements formElements = (FormElements) this.getSessionAttribute( request, SESSION_ATTRIBUTE_FORM_ELEMENTS );

        if ( formElements == null )
        {
            formElements = getinitializedFormElements(  );
            setFormElements( request, formElements );
        }

        return formElements;
    }

    /**
     * Modify the formelements objectt in session
     * @param request the http request
     * @param formElements the element to add in session
     */
    public void setFormElements( HttpServletRequest request, FormElements formElements )
    {
        // Store the object in a session attribute
        this.setSessionAttribute( request, SESSION_ATTRIBUTE_FORM_ELEMENTS, formElements );
    }

    /**
     * Remove the current formelement object from the session, which has an effect to reinit the later.
     * @param request the http request
     */
    public void clearFormElements( HttpServletRequest request )
    {
        // remove the associated attribute in session
        this.removeSessionAttribute( request, SESSION_ATTRIBUTE_FORM_ELEMENTS );
    }

    /**
     * Set the initialFormElements.
     * This is called by the form registration service.
     * @param formElements the elemnts of forms
     */
    public void initFormElements( FormElements formElements )
    {
        _initialFormElements = formElements;
    }

    /**
     * Create a new fields object initialised with the _initialFields values and elements
     * @return a copy of _initialFields
     */
    private FormElements getinitializedFormElements(  )
    {
        FormElements formElements = null;

        ObjectFactory fieldFactory = new ObjectFactory(  );
        formElements = fieldFactory.createFormElements(  );

        if ( _initialFormElements != null )
        {
            //  we won't modify the checkrules, so we don't need to clone it.
            formElements.setCheckSubFormRules( _initialFormElements.getCheckSubFormRules(  ) );

            // we won't modify the buttons, so we don't need to clone them.
            formElements.setButtons( _initialFormElements.getButtons(  ) );

            //copy notices
            formElements.setNotices( _initialFormElements.getNotices(  ) );

            Fields initialFields = _initialFormElements.getFields(  );
            List<Field> initialFieldList = initialFields.getField(  );
            Fields newFields = fieldFactory.createFields(  );

            for ( Field initialField : initialFieldList )
            {
                Field newField = fieldFactory.createField(  );

                newField.setLabel( initialField.getLabel(  ) );
                newField.setName( initialField.getName(  ) );
                newField.setType( initialField.getType(  ) );
                newField.setValue( initialField.getValue(  ) );
                newField.setAdditionalInfo( initialField.getAdditionalInfo(  ) );
                newField.setRows( initialField.getRows(  ) );
                newField.setCols( initialField.getCols(  ) );

                // we won't modify the checkrules, so we don't need to clone it.
                newField.setCheckFieldRules( initialField.getCheckFieldRules(  ) );

                ChoiceList initialChoices = initialField.getChoiceList(  );
                ChoiceList newChoiceList = fieldFactory.createChoiceList(  );

                if ( initialChoices != null )
                {
                    List<Choice> initialChoicesList = initialChoices.getChoice(  );

                    for ( Choice initialChoice : initialChoicesList )
                    {
                        Choice newChoice = fieldFactory.createChoice(  );

                        newChoice.setLabel( initialChoice.getLabel(  ) );
                        newChoice.setValue( initialChoice.getValue(  ) );

                        newChoiceList.getChoice(  ).add( newChoice );
                    }
                }

                newField.setChoiceList( newChoiceList );

                newFields.getField(  ).add( newField );
            }

            formElements.setFields( newFields );
        }

        return formElements;
    }

    /**
     * Get a field of a given name from a given fields element
     * @param request the HttpServletRequest
     * @param strName the name of the field to find
     * @return the field of name given in parameter. If nothing is found, return null. If one of the parameters is null, return null.
     */
    public Field getFieldFromName( HttpServletRequest request, String strName )
    {
        FormElements formElements = getFormElements( request );
        Fields fields = formElements.getFields(  );

        // test that there are fields to search into
        if ( ( strName == null ) || ( fields == null ) || ( fields.getField(  ) == null ) )
        {
            return null;
        }

        // get the fields associated with the current subform
        List<Field> fieldsList = fields.getField(  );

        for ( Field field : fieldsList )
        {
            if ( field.getName(  ).equals( strName.trim(  ) ) )
            {
                return field;
            }
        }

        // if we have found nothing, return null
        return null;
    }

    /**
     * Build the choice list of a field from the given reference list.
     * This is useful when the content of a combo field has to be loaded
     * from a database
     * @param field the field whose choice list should be set
     * @param referenceList the list containing the data to load into the choice list
     */
    public void setChoiceList( Field field, ReferenceList referenceList )
    {
        ObjectFactory formFactory = new ObjectFactory(  );
        ChoiceList choices = formFactory.createChoiceList(  );
        addElementsToChoiceList( choices, referenceList );
        field.setChoiceList( choices );
    }

    /**
     * Add the elements in given reference list to given field's choice list.
     * @param field the field whose choice list should be updated
     * @param referenceList the list containing the data to load into the choice list
     */
    public void appendToChoiceList( Field field, ReferenceList referenceList )
    {
        ChoiceList choices = field.getChoiceList(  );

        if ( choices == null )
        {
            setChoiceList( field, referenceList );
        }
        else
        {
            addElementsToChoiceList( choices, referenceList );
        }
    }

    /**
     * Perform the insertion the elements from the given reference list into the given choiceList
     * @param choices the list of choices where to add elements
     * @param referenceList the elements to add
     */
    private void addElementsToChoiceList( ChoiceList choices, ReferenceList referenceList )
    {
        ObjectFactory formFactory = new ObjectFactory(  );

        if ( referenceList != null )
        {
            List<Choice> choiceList = choices.getChoice(  );

            for ( ReferenceItem item : referenceList )
            {
                String strValue = item.getCode(  );
                String strLabel = item.getName(  );
                Choice choice = formFactory.createChoice(  );
                choice.setLabel( strLabel );
                choice.setValue( strValue );
                choiceList.add( choice );
            }
        }
    }

    /**
     * Build the html code corresponding to the given form elements
     * @param formElements the fields and buttons to display
     * @param params additional params that might need to be passed to the stylesheet
     * @return the html code for the subform fields
     */
    public String buildHtmlForm( FormElements formElements, Map params )
    {
        String strXml = "";

        try
        {
            // generate the xml code correponding to the form-elements for this subform
            StringWriter writer = new StringWriter(  );
            Marshaller marshaller = getParentForm(  ).getFormElementsMarshaller(  );
            marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );

            marshaller.marshal( formElements, writer );
            strXml = writer.toString(  );
        }
        catch ( JAXBException e )
        {
            throw new AppException( "Formengine : an error occurred during marshalling - form : " +
                this.getParentForm(  ).getName(  ) + " - subform " + this.getName(  ), e );
        }

        Map formParams = new HashMap(  );

        if ( params != null )
        {
            formParams.putAll( params );
        }

        formParams.put( PARAMETER_XSL_FORM_NAME, this.getParentForm(  ).getName(  ) );
        formParams.put( PARAMETER_XSL_SUBFORM_NAME, this.getName(  ) );

        // The submit buttons and checkboxes associated with the upload widget
        // are given specific names (prefix + upload field name), we provide
        // the prefixes to the XSL stylesheet.
        formParams.put( PARAMETER_XSL_UPLOAD_SUBMIT_PREFIX, SharedConstants.UPLOAD_SUBMIT_PREFIX );
        formParams.put( PARAMETER_XSL_UPLOAD_DELETE_PREFIX, SharedConstants.UPLOAD_DELETE_PREFIX );
        formParams.put( PARAMETER_XSL_UPLOAD_CHECKBOX_PREFIX, SharedConstants.UPLOAD_CHECKBOX_PREFIX );

        if ( this.getNextSubForm(  ) == null )
        {
            String strLastUrl = AppPropertiesService.getProperty( this.getParentForm(  ).getName(  ) +
                    PROPERTY_FRAGMENT_URL_FOR_LAST );
            formParams.put( PARAMETER_XSL_URL_EXIT, strLastUrl );
        }

        String strForm = this.getHtml( strXml, this.getXslFormElementsFileName(  ), formParams );

        return strForm;
    }

    /**
     * Set a session attribute, with a name formatted to avoid conflicts between form  and subform instances.
     * @param request the http request
     * @param strAttributeName the name of the variable to put in session
     * @param attribute the object to put in session
     */
    public void setSessionAttribute( HttpServletRequest request, String strAttributeName, Object attribute )
    {
        HttpSession session = request.getSession( true );
        String strPrefix = SharedConstants.SESSION_PREFIX + "_" + this.getParentForm(  ).getName(  ) + "_" +
            this.getName(  ) + "_";
        String strSessionAttribute = strPrefix + strAttributeName.trim(  );
        strSessionAttribute = strSessionAttribute.toUpperCase(  );
        session.setAttribute( strSessionAttribute, attribute );
    }

    /**
     * Get an attribute in session. This method is to use to avoid conflicts between form  and subform instances.
     * @param request The Http Request
     * @param strAttributeName the name of the variable to retrieve from session
     * @return the corresponding object in session
     */
    public Object getSessionAttribute( HttpServletRequest request, String strAttributeName )
    {
        HttpSession session = request.getSession( true );
        String strPrefix = SharedConstants.SESSION_PREFIX + "_" + this.getParentForm(  ).getName(  ) + "_" +
            this.getName(  ) + "_";
        String strSessionAttribute = strPrefix + strAttributeName.trim(  );
        strSessionAttribute = strSessionAttribute.toUpperCase(  );

        return session.getAttribute( strSessionAttribute );
    }

    /**
     * Remove an attribute in session. This method is to use to avoid conflicts between form  and subform instances.
     * @param request The HttpServlerRequest
     * @param strAttributeName the name of the variable to remove from session
     */
    public void removeSessionAttribute( HttpServletRequest request, String strAttributeName )
    {
        HttpSession session = request.getSession( true );
        String strPrefix = SharedConstants.SESSION_PREFIX + "_" + this.getParentForm(  ).getName(  ) + "_" +
            this.getName(  ) + "_";
        String strSessionAttribute = strPrefix + strAttributeName.trim(  );
        strSessionAttribute = strSessionAttribute.toUpperCase(  );
        session.removeAttribute( strSessionAttribute );
    }

    /**
     * Performs an upload action.
     *
     * @param request the HTTP request
     * @param strUploadAction the name of the upload action
     * @return the name of the subform to display after having performed the requested action
     */

    /* public String doUploadAction( HttpServletRequest request, String strUploadAction )
     {
         // The values of the other fields are saved, but no validation is performed
         this.fillFields( request );
    
         // Get the name of the upload field
         String strFieldName = ( strUploadAction.startsWith( SharedConstants.UPLOAD_SUBMIT_PREFIX )
             ? strUploadAction.substring( SharedConstants.UPLOAD_SUBMIT_PREFIX.length(  ) )
             : strUploadAction.substring( SharedConstants.UPLOAD_DELETE_PREFIX.length(  ) ) );
    
         // Find the corresponding Field object
         Field field = null;
         Iterator iterFields = this.getFormElements( request ).getFields(  ).getField(  ).iterator(  );
         boolean bFound = false;
    
         while ( !bFound && iterFields.hasNext(  ) )
         {
             Field aField = (Field) iterFields.next(  );
    
             if ( strFieldName.equals( aField.getName(  ) ) )
             {
                 bFound = true;
                 field = aField;
             }
         }
    
         if ( strUploadAction.startsWith( SharedConstants.UPLOAD_SUBMIT_PREFIX ) )
         {
             // A file was submitted
             // Use the request wrapper to get the file
             FormEngineMultipartWrapper wrapper = (FormEngineMultipartWrapper) request;
             File file = wrapper.getFile( strFieldName );
    
             if ( file != null )
             {
                 // This is the name that will be displayed in the form. We keep
                 // the original name, but clean it to make it cross-platform.
                 String strFileName = UploadUtil.cleanFileName( wrapper.getOriginalFileName( strFieldName ) );
    
                 // Check if this file has not already been uploaded
                 List uploadedFiles = this.getFiles( request, strFieldName );
    
                 if ( uploadedFiles != null )
                 {
                     Iterator iterUploadedFiles = uploadedFiles.iterator(  );
                     boolean bNew = true;
    
                     while ( bNew && iterUploadedFiles.hasNext(  ) )
                     {
                         UploadedFile uploadedFile = (UploadedFile) iterUploadedFiles.next(  );
    
                         // If we find a file with the same name and the same
                         // length, we consider that the current file has
                         // already been uploaded
                         bNew = !( uploadedFile.getOriginalName(  ).equals( strFileName ) &&
                             ( uploadedFile.getFile(  ).length(  ) == file.length(  ) ) );
                     }
    
                     if ( !bNew )
                     {
                         // Delete the temporary file
                         file.delete(  );
    
                         // Raise an error
                         String[] messageParams = new String[2];
                         messageParams[0] = field.getName(  );
                         messageParams[1] = field.getLabel(  );
    
                         String strError = MessageFormat.format( AppPropertiesService.getProperty(
                                     PROPERTY_MESSAGE_EXISTING_FILE ), (Object[]) messageParams );
                         this.getParentForm(  ).addErrorMessage( request, strError );
    
                         return this.getName(  );
                     }
                 }
    
    
                 ObjectFactory objectFactory = new ObjectFactory(  );
    
                 if ( field.getFileNames(  ) == null )
                 {
                     field.setFileNames( objectFactory.createFileNames(  ) );
                 }
    
                 FileName fileName = objectFactory.createFileName(  );
                 fileName.setValue( strFileName );
                 field.getFileNames(  ).getFileName(  ).add( fileName );
    
    
                 // Run any validator associated to the upload field
                 boolean bValid = validateUploadField( request, field );
    
                 if ( bValid )
                 {
                     // Associate the file with the subform
                     this.addFile( request, strFieldName, strFileName, file );
                 }
                 else
                 {
                     // An error occurred, so the file won't be associated with
                     // the subform. Delete the temporary file
                     file.delete(  );
    
                     // The name of the file is still in the Field, remove it
                     List fileNames = field.getFileNames(  ).getFileName(  );
                     fileNames.remove( fileNames.size(  ) - 1 );
                 }
             }
         }
         else if ( strUploadAction.startsWith( SharedConstants.UPLOAD_DELETE_PREFIX ) )
         {
             // Some previously uploaded files were deleted
             // Build the prefix of the associated checkboxes
             String strPrefix = SharedConstants.UPLOAD_CHECKBOX_PREFIX + strFieldName;
    
             // Look for the checkboxes in the request
             Enumeration enumParamNames = request.getParameterNames(  );
    
             while ( enumParamNames.hasMoreElements(  ) )
             {
                 String strParamName = (String) enumParamNames.nextElement(  );
    
                 if ( strParamName.startsWith( strPrefix ) )
                 {
                     // Get the index from the name of the checkbox
                     int iIndex = Integer.parseInt( strParamName.substring( strPrefix.length(  ) ) );
    
                     // Delete the name from the Field object
                     field.getFileNames(  ).getFileName(  ).remove( iIndex );
    
                     // Remove the file from the subform (this will also delete
                     // the file physically)
                     this.removeFile( request, strFieldName, iIndex );
                 }
             }
         }
    
         // The result of an upload action is to reload the same subform
         return this.getName(  );
     }*/

    /**
     * Performs an upload action.
     *
     * @param request the HTTP request
     * @param strUploadAction the name of the upload action
     * @return the name of the subform to display after having performed the requested action
     */
    public String doUploadAction( HttpServletRequest request, String strUploadAction )
    {
        // The values of the other fields are saved, but no validation is performed
        this.fillFields( request );

        // Get the name of the upload field
        String strFieldName = ( strUploadAction.startsWith( SharedConstants.UPLOAD_SUBMIT_PREFIX )
            ? strUploadAction.substring( SharedConstants.UPLOAD_SUBMIT_PREFIX.length(  ) )
            : strUploadAction.substring( SharedConstants.UPLOAD_DELETE_PREFIX.length(  ) ) );

        // Find the corresponding Field object
        Field field = null;
        Iterator iterFields = this.getFormElements( request ).getFields(  ).getField(  ).iterator(  );
        boolean bFound = false;

        while ( !bFound && iterFields.hasNext(  ) )
        {
            Field aField = (Field) iterFields.next(  );

            if ( strFieldName.equals( aField.getName(  ) ) )
            {
                bFound = true;
                field = aField;
            }
        }

        if ( strUploadAction.startsWith( SharedConstants.UPLOAD_SUBMIT_PREFIX ) )
        {
            // A file was submitted
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

            FileItem fileItem = multipartRequest.getFile( strFieldName );

            if ( fileItem != null )
            {
                // This is the name that will be displayed in the form. We keep
                // the original name, but clean it to make it cross-platform.
                String strFileName = UploadUtil.cleanFileName( FileUploadService.getFileNameOnly( fileItem ) );

                // Check if this file has not already been uploaded
                List<FileItem> uploadedFiles = this.getFileItems( request, strFieldName );

                if ( uploadedFiles != null )
                {
                    Iterator<FileItem> iterUploadedFiles = uploadedFiles.iterator(  );
                    boolean bNew = true;

                    while ( bNew && iterUploadedFiles.hasNext(  ) )
                    {
                        FileItem uploadedFile = iterUploadedFiles.next(  );
                        String strUploadedFileName = UploadUtil.cleanFileName( FileUploadService.getFileNameOnly( 
                                    uploadedFile ) );
                        // If we find a file with the same name and the same
                        // length, we consider that the current file has
                        // already been uploaded
                        bNew = !( strUploadedFileName.equals( strFileName ) &&
                            ( uploadedFile.getSize(  ) == fileItem.getSize(  ) ) );
                    }

                    if ( !bNew )
                    {
                        // Delete the temporary file
                        // file.delete(  );

                        // Raise an error
                        String[] messageParams = new String[2];
                        messageParams[0] = field.getName(  );
                        messageParams[1] = field.getLabel(  );

                        String strError = MessageFormat.format( AppPropertiesService.getProperty( 
                                    PROPERTY_MESSAGE_EXISTING_FILE ), (Object[]) messageParams );
                        this.getParentForm(  ).addErrorMessage( request, strError );

                        return this.getName(  );
                    }
                }

                ObjectFactory objectFactory = new ObjectFactory(  );

                if ( field.getFileNames(  ) == null )
                {
                    field.setFileNames( objectFactory.createFileNames(  ) );
                }

                FileName fileName = objectFactory.createFileName(  );
                fileName.setValue( strFileName );
                field.getFileNames(  ).getFileName(  ).add( fileName );

                // Run any validator associated to the upload field
                boolean bValid = validateUploadField( request, field );

                if ( bValid )
                {
                    // Associate the file with the subform
                    this.addFileItem( request, strFieldName, fileItem );
                }
                else
                {
                    // An error occurred, so the file won't be associated with
                    // the subform. Delete the temporary file
                    // file.delete(  );

                    // The name of the file is still in the Field, remove it
                    List fileNames = field.getFileNames(  ).getFileName(  );
                    fileNames.remove( fileNames.size(  ) - 1 );
                }
            }
        }
        else if ( strUploadAction.startsWith( SharedConstants.UPLOAD_DELETE_PREFIX ) )
        {
            // Some previously uploaded files were deleted
            // Build the prefix of the associated checkboxes
            String strPrefix = SharedConstants.UPLOAD_CHECKBOX_PREFIX + strFieldName;

            // Look for the checkboxes in the request
            Enumeration enumParamNames = request.getParameterNames(  );

            while ( enumParamNames.hasMoreElements(  ) )
            {
                String strParamName = (String) enumParamNames.nextElement(  );

                if ( strParamName.startsWith( strPrefix ) )
                {
                    // Get the index from the name of the checkbox
                    int iIndex = Integer.parseInt( strParamName.substring( strPrefix.length(  ) ) );

                    // Delete the name from the Field object
                    field.getFileNames(  ).getFileName(  ).remove( iIndex );

                    // Remove the file from the subform (this will also delete
                    // the file physically)
                    this.removeFileItem( request, strFieldName, iIndex );
                }
            }
        }

        // The result of an upload action is to reload the same subform
        return this.getName(  );
    }

    /**
     * Associates a newly uploaded file to the subform. This method is used by
     * FormEngineApp and need not be overriden nor called from child classes.
     *
     * @param request the HTTP request
     * @param strFieldName the name of the upload field
     * @param strFileName the name of the file
     * @param file the File object
     */

    /*  public final void addFile( HttpServletRequest request, String strFieldName, String strFileName, File file )
      {
          // Build the attribute name and instantiate the object if none exists yet
          String strAttributeName = SharedConstants.FILELIST_PREFIX + strFieldName;
    
          if ( this.getSessionAttribute( request, strAttributeName ) == null )
          {
              this.setSessionAttribute( request, strAttributeName, new ArrayList(  ) );
          }
    
          // Get the HashMap from the session and put the File in it
          List listFiles = (List) this.getSessionAttribute( request, strAttributeName );
          listFiles.add( new UploadedFile( strFileName, file ) );
      }*/

    /**
     * Associates a newly uploaded file to the subform. This method is used by
     * FormEngineApp and need not be overriden nor called from child classes.
     *
     * @param request the HTTP request
     * @param strFieldName the name of the upload field
     * @param file the File object
     */
    public final void addFileItem( HttpServletRequest request, String strFieldName, FileItem fileItem )
    {
        // Build the attribute name and instantiate the object if none exists yet
        String strAttributeName = SharedConstants.FILELIST_PREFIX + strFieldName;

        if ( this.getSessionAttribute( request, strAttributeName ) == null )
        {
            this.setSessionAttribute( request, strAttributeName, new ArrayList<FileItem>(  ) );
        }

        // Get the HashMap from the session and put the File in it
        List<FileItem> listFiles = (List) this.getSessionAttribute( request, strAttributeName );
        listFiles.add( fileItem );
    }

    /**
     * Deletes a file previously associated with the subform. This method is
     * used by FormEngineApp and need not be overriden nor called from child
     * classes.
     *
     * @param request the HTTP request
     * @param strFieldName the name of the upload field
     * @param iIndex the index of the file to remove
     */

    /* public final void removeFile( HttpServletRequest request, String strFieldName, int iIndex )
     {
         // Build the attribute name and get the HashMap from the session
         String strAttributeName = SharedConstants.FILELIST_PREFIX + strFieldName;
         List listFiles = (List) this.getSessionAttribute( request, strAttributeName );
    
         if ( ( listFiles != null ) && ( listFiles.size(  ) > iIndex ) )
         {
             UploadedFile uploadedFile = (UploadedFile) listFiles.get( iIndex );
    
             // Remove the object from the Hashmap
             listFiles.remove( iIndex );
    
             // Physically remove the file
             uploadedFile.getFile(  ).delete(  );
         }
     }*/

    /**
     * Deletes a file previously associated with the subform. This method is
     * used by FormEngineApp and need not be overriden nor called from child
     * classes.
     *
     * @param request the HTTP request
     * @param strFieldName the name of the upload field
     * @param iIndex the index of the file to remove
     */
    public final void removeFileItem( HttpServletRequest request, String strFieldName, int iIndex )
    {
        // Build the attribute name and get the HashMap from the session
        String strAttributeName = SharedConstants.FILELIST_PREFIX + strFieldName;
        List<FileItem> listFiles = (List) this.getSessionAttribute( request, strAttributeName );

        if ( ( listFiles != null ) && ( listFiles.size(  ) > iIndex ) )
        {
            // Remove the object from the Hashmap
            listFiles.remove( iIndex );
        }
    }

    /**
     * Returns the files associated with an upload field of the subform. This
     * method can be used by output processors to access the uploaded files'
     * data.
     *
     * @param request the HTTP request
     * @param strFieldName the name of the upload field
     * @return a java.util.List containing UploadedFiles objects. Returns null
     * if no file was uploaded for this field.
     *
     * @see fr.paris.lutece.plugins.formengine.web.UploadedFile
     */

    /*  public final List getFiles( HttpServletRequest request, String strFieldName )
      {
          // Build the attribute name and get the HashMap from the session
          String strAttributeName = SharedConstants.FILELIST_PREFIX + strFieldName;
    
          return (List) this.getSessionAttribute( request, strAttributeName );
      }*/

    /**
     * Returns the files associated with an upload field of the subform. This
     * method can be used by output processors to access the uploaded files'
     * data.
     *
     * @param request the HTTP request
     * @param strFieldName the name of the upload field
     * @return a java.util.List containing UploadedFiles objects. Returns null
     * if no file was uploaded for this field.
     *
     */
    public final List<FileItem> getFileItems( HttpServletRequest request, String strFieldName )
    {
        // Build the attribute name and get the HashMap from the session
        String strAttributeName = SharedConstants.FILELIST_PREFIX + strFieldName;

        return (List<FileItem>) this.getSessionAttribute( request, strAttributeName );
    }
}
