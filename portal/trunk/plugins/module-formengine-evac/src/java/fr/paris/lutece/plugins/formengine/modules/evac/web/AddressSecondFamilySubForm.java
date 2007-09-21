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

import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.CheckFieldRules;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.CheckRule;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.Choice;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.Field;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.Fields;
import fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.Adresse;
import fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.Cartereponse;
import fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.Famille;
import fr.paris.lutece.plugins.formengine.modules.evac.business.jaxb.ObjectFactory;
import fr.paris.lutece.plugins.formengine.modules.evac.service.address.AddressServiceProvider;
import fr.paris.lutece.plugins.formengine.modules.evac.utils.EvacUtils;
import fr.paris.lutece.plugins.formengine.service.validator.FieldValidator;
import fr.paris.lutece.plugins.formengine.service.validator.ValidatorFactory;
import fr.paris.lutece.plugins.formengine.web.FormErrorsList;
import fr.paris.lutece.plugins.formengine.web.SubForm;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.math.BigInteger;

import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Address to the second family subform
 */
public class AddressSecondFamilySubForm extends SubForm
{
    //properties
    private static final String PROPERTY_FRAGMENT_XSL_FILE_FORM = ".xsl.file.form.AddressSecondFamily";
    private static final String PROPERTY_FRAGMENT_XSL_FILE_SUMMARY_CHOIX_ADDRESS = ".xsl.file.summary.choiceAddressSecondFamily";
    private static final String PROPERTY_FRAGMENT_TEMPLATE_HEADER = ".template.header.AddressSecondFamily";

    //fields
    private static final String FIELD_NUM_VOIE = "numVoie";
    private static final String FIELD_SUFFIXE = "suffixe";
    private static final String FIELD_NAME_VOIE = "nomVoie";
    private static final String FIELD_ADDRESS_COMP = "complAddress";
    private static final String FIELD_ADDRESS_COMP_2 = "complAddress2";
    private static final String FIELD_LIST_ADDRESS = "address";

    //parameters for templates
    private static final String PARAMETER_HIDE_BUTTON_PREVIOUS = "hideButtonPrevious";
    private static final String PARAMETER_HIDE_SEARCH_ADDRESS = "hideSearchAddress";
    private static final String PARAMETER_HIDE_CHOICE_ADDRESS = "hideChoiceAddress";
    private static final String PARAMETER_HIDE_ADDRESS_NOT_FOUND = "hideAddressNotFound";

    //actions
    private static final String ACTION_NAME_SEARCH = "Rechercher";
    private static final String ACTION_NAME_VALID = "Valider";
    private static final String ACTION_NAME_MODIFY = "Modifier";

    //parameters in session
    private static final String RESPONSE_ADDRESS_SERVICE_LIST_ADDRESS_SECOND_FAMILY = "LIST_ADDRESS_SECOND_FAMILY";
    private static final String ID_ADDRESS_SELECTED_SECOND_FAMILY = "ID_ADDRESS_SELECTED_SECOND_FAMILY";
    private static final String ADDRESS_NOT_FOUND_SECOND_FAMILY = "ADDRESS_NOT_FOUND_SECOND_FAMILY";
    private static final String SECOND_FAMILY_VALIDATE = "SECOND_FAMILY_VALIDATE";

    //messages
    private static final String MESSAGE_ADDRESS_NOT_FOUND = ".error.message.addressService";
    private static final String ADDRESS_SEVICE_UNAVAILABLE = "ADDRESS_SEVICE_UNAVAILABLE";
    private static final String TEMPLATE_ERROR_ADDRESS_SERVICE = ".template.error.message.addressServiceUnavailable";

    //Utils
    private static final String CITY = ".referenceCity";
    private static final String SHOW = "0";
    private static final String HIDE = "1";

    /**
    *
    */
    public AddressSecondFamilySubForm(  )
    {
        super(  );
    }

    /**
     *  Six action provided : ACTION_NAME_SEARCH, ACTION_NAME_MODIFY,
     *          ACTION_NAME_VALID, ACTION_NAME_PREVIOUS
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
        else if ( strActionName.equals( ACTION_NAME_SEARCH ) )
        {
            this.removeSessionAttribute( request, ADDRESS_NOT_FOUND_SECOND_FAMILY );
            this.removeSessionAttribute( request, RESPONSE_ADDRESS_SERVICE_LIST_ADDRESS_SECOND_FAMILY );
            this.removeSessionAttribute( request, ID_ADDRESS_SELECTED_SECOND_FAMILY );

            boolean bValid = doValidFieldsSearch( request );

            if ( bValid )
            {
                doSearch( request );
                strNextSubForm = this.getName(  );
            }
            else
            {
                strNextSubForm = this.getName(  );
            }
        }
        else if ( strActionName.equals( ACTION_NAME_MODIFY ) )
        {
            this.removeSessionAttribute( request, RESPONSE_ADDRESS_SERVICE_LIST_ADDRESS_SECOND_FAMILY );
            strNextSubForm = this.getName(  );
        }
        else if ( strActionName.equals( ACTION_NAME_VALID ) )
        {
            boolean bValid = doValidFieldsChoice( request );

            if ( bValid )
            {
                String strIdAddress = request.getParameter( FIELD_LIST_ADDRESS );
                this.setSessionAttribute( request, ID_ADDRESS_SELECTED_SECOND_FAMILY, strIdAddress );

                //strNextSubForm = this.getName(  );
                doModifyFormDocument( request );

                this.setSessionAttribute( request, SECOND_FAMILY_VALIDATE, true );

                strNextSubForm = getNextSubForm(  ).getName(  );
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
            this.removeSessionAttribute( request, ADDRESS_NOT_FOUND_SECOND_FAMILY );
            strNextSubForm = this.getPreviousSubForm(  ).getName(  );
        }
        else
        {
            strNextSubForm = this.getName(  );
        }

        return strNextSubForm;
    }

    /**
     * Valid one field
     * @param request the request
     * @param fieldsName the list of field to validate
     * @return true if the list of fields is valid
     */
    private boolean doValidField( HttpServletRequest request, List<String> fieldsName )
    {
        boolean bValid = true;

        Collection<Field> colFields = new ArrayList<Field>(  );

        for ( String fieldName : fieldsName )
        {
            colFields.add( this.getFieldFromName( request, fieldName ) );
        }

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
                    bValid &= validator.validate( field, listErrors );
                }
            }
        }

        //      add the errors to the form
        for ( int k = 0; k < listErrors.size(  ); k++ )
        {
            getParentForm(  ).addErrorMessage( request, listErrors.get( k ) );
        }

        return bValid;
    }

    /**
     * Fill the fields with the parameters in request.
     * This method should be called before performing any action, to update the fields
     * with the new values passed in request.
     * @param request the http request
     * @param fieldsName the list of field to fill
     */
    public void fillFields( HttpServletRequest request, List<String> fieldsName )
    {
        Collection<Field> colFields = new ArrayList<Field>(  );

        for ( String fieldName : fieldsName )
        {
            colFields.add( this.getFieldFromName( request, fieldName ) );
        }

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
     * Valid the field of Choice list
     * @param request the request
     * @return true if fields are valid
     */
    private boolean doValidFieldsChoice( HttpServletRequest request )
    {
        boolean bValid = false;

        List<String> fieldsName = new ArrayList<String>(  );
        fieldsName.add( FIELD_LIST_ADDRESS );

        //set the field' content
        this.fillFields( request, fieldsName );

        bValid = doValidField( request, fieldsName );

        return bValid;
    }

    /**
    * Valid the field of search
    * @param request the request
    * @return true if fields are valid
    */
    private boolean doValidFieldsSearch( HttpServletRequest request )
    {
        boolean bValid = true;

        List<String> fieldsNameFill = new ArrayList<String>(  );
        fieldsNameFill.add( FIELD_NUM_VOIE );
        fieldsNameFill.add( FIELD_SUFFIXE );
        fieldsNameFill.add( FIELD_NAME_VOIE );
        fieldsNameFill.add( FIELD_ADDRESS_COMP );
        fieldsNameFill.add( FIELD_ADDRESS_COMP_2 );

        //update the fields of this subform
        this.fillFields( request, fieldsNameFill );

        List<String> fieldsNameValidate = new ArrayList<String>(  );
        fieldsNameValidate.add( FIELD_NUM_VOIE );
        fieldsNameValidate.add( FIELD_NAME_VOIE );

        bValid = doValidField( request, fieldsNameValidate );

        if ( bValid )
        {
            //create the ObjectFactory
            ObjectFactory factory = new ObjectFactory(  );

            //retrieve the root element in session
            Cartereponse carteReponse;
            carteReponse = (Cartereponse) getParentForm(  ).getFormDocument( request );

            if ( carteReponse == null )
            {
                carteReponse = factory.createCartereponse(  );
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

            Field fieldNumVoie = this.getFieldFromName( request, FIELD_NUM_VOIE );
            adresseFamille2.setDunumero( new BigInteger( fieldNumVoie.getValue(  ) ) );

            if ( ( this.getFieldFromName( request, FIELD_SUFFIXE ) != null ) &&
                    !this.getFieldFromName( request, FIELD_SUFFIXE ).equals( "" ) )
            {
                Field fieldSuffixe = this.getFieldFromName( request, FIELD_SUFFIXE );
                adresseFamille2.setDubis( fieldSuffixe.getValue(  ) );
            }

            Field fieldNameVoie = this.getFieldFromName( request, FIELD_NAME_VOIE );

            //remove multi space for web service
            String strNameVoie = EvacUtils.removeMultiSpace( fieldNameVoie.getValue(  ) );
            adresseFamille2.setLibelleVoie( strNameVoie );

            if ( this.getFieldFromName( request, FIELD_ADDRESS_COMP ) != null )
            {
                Field fieldComplAddress = this.getFieldFromName( request, FIELD_ADDRESS_COMP );
                adresseFamille2.setComplement1Adresse( fieldComplAddress.getValue(  ) );
            }

            if ( this.getFieldFromName( request, FIELD_ADDRESS_COMP_2 ) != null )
            {
                Field fieldComplAddress2 = this.getFieldFromName( request, FIELD_ADDRESS_COMP_2 );
                adresseFamille2.setComplement2Adresse( fieldComplAddress2.getValue(  ) );
            }

            adresseFamille2.setVille( AppPropertiesService.getProperty( this.getParentForm(  ).getName(  ) + CITY ) );

            famille2.setAdresse( adresseFamille2 );
            carteReponse.setFamille2( famille2 );
            this.getParentForm(  ).setFormDocument( request, carteReponse );
        }

        return bValid;
    }

    /**
     *  search a list of address correponding to the user address and put in session
     * @param request the request
     */
    private void doSearch( HttpServletRequest request )
    {
        Field fieldNumVoie = this.getFieldFromName( request, FIELD_NUM_VOIE );
        Field fieldNameVoie = this.getFieldFromName( request, FIELD_NAME_VOIE );
        Field fieldSuffixeVoie = this.getFieldFromName( request, FIELD_SUFFIXE );
        Field fieldListAddress = this.getFieldFromName( request, FIELD_LIST_ADDRESS );

        //remove multi space for web service
        String strNameVoie = EvacUtils.removeMultiSpace( fieldNameVoie.getValue(  ) );

        //get the label suffixe
        List<Choice> listChoice = fieldSuffixeVoie.getChoiceList(  ).getChoice(  );
        String suffixecurrentLabel = "";

        for ( Choice currentChoice : listChoice )
        {
            if ( currentChoice.getValue(  ).equals( fieldSuffixeVoie.getValue(  ) ) )
            {
                suffixecurrentLabel = currentChoice.getLabel(  );
            }
        }

        //build the string for seach method
        String strNumSuffixeNameVoie = "";

        if ( !suffixecurrentLabel.equals( "" ) )
        {
            strNumSuffixeNameVoie = fieldNumVoie.getValue(  ) + " " + suffixecurrentLabel + " " + strNameVoie;
        }
        else
        {
            strNumSuffixeNameVoie = fieldNumVoie.getValue(  ) + " " + strNameVoie;
        }

        //requetes au web Service adresse
        ReferenceList refList = null;

        try
        {
            refList = AddressServiceProvider.searchAddress( strNumSuffixeNameVoie );
        }
        catch ( RemoteException e )
        {
            this.setSessionAttribute( request, ADDRESS_SEVICE_UNAVAILABLE, true );

            return;
        }

        if ( ( refList == null ) || ( refList.size(  ) == 0 ) )
        {
            this.getParentForm(  )
                .addErrorMessage( request,
                AppPropertiesService.getProperty( getParentForm(  ).getName(  ) + MESSAGE_ADDRESS_NOT_FOUND ) );
            this.setSessionAttribute( request, ADDRESS_NOT_FOUND_SECOND_FAMILY, "" );
        }
        else if ( refList.size(  ) == 1 )
        {
            this.setChoiceList( fieldListAddress, refList );

            this.getFieldFromName( request, FIELD_LIST_ADDRESS ).setValue( refList.get( 0 ).getCode(  ) );
            //put response web service flux in session
            this.setSessionAttribute( request, RESPONSE_ADDRESS_SERVICE_LIST_ADDRESS_SECOND_FAMILY, refList );
        }
        else
        {
            this.setChoiceList( fieldListAddress, refList );

            //put response web service flux in session
            this.setSessionAttribute( request, RESPONSE_ADDRESS_SERVICE_LIST_ADDRESS_SECOND_FAMILY, refList );

            //}
        }
    }

    /**
     * Reloaded the fields when youy modified  the family address
     *
     * @param request the request
     */
    protected void doReloadFieldValues( HttpServletRequest request )
    {
        ObjectFactory factory = new ObjectFactory(  );

        //retrieve the root element in session
        Cartereponse carteReponse;
        carteReponse = (Cartereponse) getParentForm(  ).getFormDocument( request );

        if ( carteReponse == null )
        {
            carteReponse = factory.createCartereponse(  );
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
        else
        {
            Fields fields = this.getFormElements( request ).getFields(  );
            List<Field> listField = fields.getField(  );

            for ( Field currentField : listField )
            {
                if ( currentField.getName(  ).equals( FIELD_NUM_VOIE ) )
                {
                    currentField.setValue( adresseFamille2.getDunumero(  ).toString(  ) );
                }

                if ( currentField.getName(  ).equals( FIELD_SUFFIXE ) )
                {
                    currentField.setValue( adresseFamille2.getDubis(  ) );
                }

                if ( currentField.getName(  ).equals( FIELD_NAME_VOIE ) )
                {
                    if ( EvacUtils.isTerminateByApostrophe( adresseFamille2.getTypeVoie(  ) ) )
                    {
                        currentField.setValue( adresseFamille2.getTypeVoie(  ) + adresseFamille2.getLibelleVoie(  ) );
                    }
                    else
                    {
                        currentField.setValue( adresseFamille2.getTypeVoie(  ) + " " +
                            adresseFamille2.getLibelleVoie(  ) );
                    }
                }

                if ( currentField.getName(  ).equals( FIELD_ADDRESS_COMP ) )
                {
                    currentField.setValue( adresseFamille2.getComplement1Adresse(  ) );
                }

                if ( currentField.getName(  ).equals( FIELD_ADDRESS_COMP_2 ) )
                {
                    currentField.setValue( adresseFamille2.getComplement2Adresse(  ) );
                }

                if ( currentField.getName(  ).equals( FIELD_LIST_ADDRESS ) &&
                        ( adresseFamille2.getIadresse(  ) != null ) )
                {
                    currentField.setValue( adresseFamille2.getIadresse(  ).toString(  ) );
                }
            }
        }
    }

    /**
     * Modify the Form Document when valid your choice address
     *
     * @param request the request
     */
    protected void doModifyFormDocument( HttpServletRequest request )
    {
        String strIdAddress = (String) this.getSessionAttribute( request, ID_ADDRESS_SELECTED_SECOND_FAMILY );
        long lIdAddress = Long.parseLong( strIdAddress );

        //create the ObjectFactory
        ObjectFactory factory = new ObjectFactory(  );

        //		retrieve the root element in session
        Cartereponse carteReponse;
        carteReponse = (Cartereponse) getParentForm(  ).getFormDocument( request );

        if ( carteReponse == null )
        {
            carteReponse = factory.createCartereponse(  );
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

        Adresse adresse = null;

        try
        {
            adresse = AddressServiceProvider.getAdresseInfo( lIdAddress, false );

            adresseFamille2.setIadresse( adresse.getIadresse(  ) );
            adresseFamille2.setDunumero( adresse.getDunumero(  ) );
            adresseFamille2.setDubis( adresse.getDubis(  ) );
            adresseFamille2.setCodeCommune( adresse.getCodeCommune(  ) );

            adresseFamille2.setTypeVoie( adresse.getTypeVoie(  ) );
            adresseFamille2.setLibelleVoie( adresse.getLibelleVoie(  ) );

            famille2.setAdresse( adresseFamille2 );
            carteReponse.setFamille2( famille2 );
            this.getParentForm(  ).setFormDocument( request, carteReponse );
        }
        catch ( RemoteException e )
        {
            this.setSessionAttribute( request, ADDRESS_SEVICE_UNAVAILABLE, true );
        }
    }

    /**
     * @param request the request
     * @return the html to display in summary
     */
    protected String displaySummary( HttpServletRequest request )
    {
        if ( ( this.getSessionAttribute( request, RESPONSE_ADDRESS_SERVICE_LIST_ADDRESS_SECOND_FAMILY ) != null ) &&
                ( this.getSessionAttribute( request, ID_ADDRESS_SELECTED_SECOND_FAMILY ) == null ) )
        {
            return displaySummaryChoiceAddress( request );
        }
        else
        {
            return "";
        }
    }

    /**
     * Display the summary of choice address
     * @param request the request
     * @return the display summary
     */
    protected String displaySummaryChoiceAddress( HttpServletRequest request )
    {
        String strHtml = "";
        String strXml = "";

        //get the data object in session
        Cartereponse carteReponse = (Cartereponse) getParentForm(  ).getFormDocument( request );
        // set temp values to allow generation        
        carteReponse.setNumero( Constants.TMP_ID_XML_OUTPUT );
        // generate xml
        strXml = getParentForm(  ).getXmlFormDocument( request );

        // generate html code via xsl transform
        String strFileName = AppPropertiesService.getProperty( this.getParentForm(  ).getName(  ) +
                PROPERTY_FRAGMENT_XSL_FILE_SUMMARY_CHOIX_ADDRESS );
        strHtml = this.getHtml( strXml, strFileName );

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

        if ( this.getSessionAttribute( request, ADDRESS_SEVICE_UNAVAILABLE ) != null )
        {
            HtmlTemplate template = AppTemplateService.getTemplate( AppPropertiesService.getProperty( this.getParentForm(  )
                                                                                                          .getName(  ) +
                        TEMPLATE_ERROR_ADDRESS_SERVICE ) );
            strForm = template.getHtml(  );
            this.setSessionAttribute( request, ADDRESS_SEVICE_UNAVAILABLE, null );
        }
        else
        {
            Map<String, String> params = new HashMap<String, String>(  );

            if ( ( this.getSessionAttribute( request, RESPONSE_ADDRESS_SERVICE_LIST_ADDRESS_SECOND_FAMILY ) == null ) &&
                    ( this.getSessionAttribute( request, ID_ADDRESS_SELECTED_SECOND_FAMILY ) == null ) &&
                    ( this.getSessionAttribute( request, ADDRESS_NOT_FOUND_SECOND_FAMILY ) == null ) )
            {
                params.put( PARAMETER_HIDE_ADDRESS_NOT_FOUND, HIDE );
                params.put( PARAMETER_HIDE_CHOICE_ADDRESS, HIDE );
                params.put( PARAMETER_HIDE_BUTTON_PREVIOUS, SHOW );
                params.put( PARAMETER_HIDE_SEARCH_ADDRESS, SHOW );
            }
            else if ( ( this.getSessionAttribute( request, RESPONSE_ADDRESS_SERVICE_LIST_ADDRESS_SECOND_FAMILY ) == null ) &&
                    ( this.getSessionAttribute( request, ID_ADDRESS_SELECTED_SECOND_FAMILY ) == null ) &&
                    ( this.getSessionAttribute( request, ADDRESS_NOT_FOUND_SECOND_FAMILY ) != null ) )
            {
                params.put( PARAMETER_HIDE_ADDRESS_NOT_FOUND, SHOW );
                params.put( PARAMETER_HIDE_CHOICE_ADDRESS, HIDE );
                params.put( PARAMETER_HIDE_BUTTON_PREVIOUS, SHOW );
                params.put( PARAMETER_HIDE_SEARCH_ADDRESS, SHOW );
            }
            else if ( ( this.getSessionAttribute( request, RESPONSE_ADDRESS_SERVICE_LIST_ADDRESS_SECOND_FAMILY ) != null ) &&
                    ( this.getSessionAttribute( request, ID_ADDRESS_SELECTED_SECOND_FAMILY ) == null ) )
            {
                params.put( PARAMETER_HIDE_ADDRESS_NOT_FOUND, HIDE );
                params.put( PARAMETER_HIDE_CHOICE_ADDRESS, SHOW );
                params.put( PARAMETER_HIDE_BUTTON_PREVIOUS, HIDE );
                params.put( PARAMETER_HIDE_SEARCH_ADDRESS, HIDE );
            }
            else
            {
                doReloadFieldValues( request );
                params.put( PARAMETER_HIDE_ADDRESS_NOT_FOUND, HIDE );
                params.put( PARAMETER_HIDE_BUTTON_PREVIOUS, SHOW );
                params.put( PARAMETER_HIDE_CHOICE_ADDRESS, HIDE );
                params.put( PARAMETER_HIDE_SEARCH_ADDRESS, SHOW );
            }

            strForm = this.buildHtmlForm( getFormElements( request ), params );
        }

        return strForm;
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
