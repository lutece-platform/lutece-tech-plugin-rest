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

import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.Output;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.OutputSet;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.Outputs;
import fr.paris.lutece.plugins.formengine.business.jaxb.transaction.ObjectFactory;
import fr.paris.lutece.plugins.formengine.business.jaxb.transaction.SenderInfo;
import fr.paris.lutece.plugins.formengine.business.jaxb.transaction.ServerInfo;
import fr.paris.lutece.plugins.formengine.business.jaxb.transaction.Transaction;
import fr.paris.lutece.plugins.formengine.business.jaxb.transaction.TransactionData;
import fr.paris.lutece.plugins.formengine.business.jaxb.transaction.TransactionInfo;
import fr.paris.lutece.plugins.formengine.service.output.IdGenerator;
import fr.paris.lutece.plugins.formengine.service.output.OutputProcessor;
import fr.paris.lutece.plugins.formengine.service.output.TransactionFileIdGenerator;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.io.StringWriter;

import java.sql.Date;
import java.sql.Time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Validator;


/**
 * This classe defines the main characteristics and methods of a form
 */
public abstract class Form
{
    private static final String SESSION_ATTRIBUTE_FORM_DOCUMENT = "FORM_DOCUMENT";
    private static final String SESSION_ATTRIBUTE_TRANSACTION_ID = "TRANSACTION_ID";
    private static final String SESSION_ATTRIBUTE_FORM_ERRORS = "ERRORS";
    private static final String SESSION_ATTRIBUTE_BLOCK_PROCESSING = "BLOCK_PROCESS";
    private static final String PROPERTY_FORMDEFINITION_PACKAGE_NAME = "formengine.jaxb.packagename.formdefinition";
    private String _strTitle;
    private String _strName;
    private SubForm _subformFirst;
    private Map _subForms;
    private Outputs _outputs;
    private JAXBContext _jaxbSpecificContext;
    private JAXBContext _jaxbContextFormElements;

    /**
     * Constructor
     */
    public Form( String strFormName )
    {
        this.setName( strFormName );

        try
        {
            // create context for specific xml generation
            _jaxbSpecificContext = JAXBContext.newInstance( this.getGeneratedPackageName(  ) );

            // jaxb context for forms display and modification
            String strFormElementsPackageName = AppPropertiesService.getProperty( PROPERTY_FORMDEFINITION_PACKAGE_NAME );
            _jaxbContextFormElements = JAXBContext.newInstance( strFormElementsPackageName );
        }
        catch ( JAXBException ex )
        {
            throw new AppException( "Formengine : an error occurred while instancing the form : " + strFormName, ex );
        }

        _subForms = new HashMap(  );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Abstract methods

    /**
     * This method should be overriden.
     * It defines the absolute path of the directory where are stored the xsl files
     * @return The absolute path for xsl stylesheets
     */
    public abstract String getXslDirectoryPath(  );

    /**
     * This method should be overriden to indicate the home package name of the
     * classes generated by JAXB. This refers to the classes responsible for generating
     * the specific part of the output xml file.
     * @return the fully qualified package name
     */
    protected abstract String getGeneratedPackageName(  );

    /**
     * This method should be overriden to indicate the code of the transaction
     * @return the transaction code for a given form
     */
    public abstract String getTransactionCode(  );

    /**
    * This method should be overriden to indicate the instance name
    * @return the instance name  for a given form
    */
    public abstract String getInstanceName(  );

    /**
     * This method should be overriden to indicate the name of the transaction
     * @return the transaction name for a given form
     */
    public abstract String getTransactionName(  );

    ////////////////////////////////////////////////////////////////////////////
    // Default implementation

    /**
     * Get the name of the form
     * @return the subform title
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
    * Set the name of the form
    * @param strName the name to set
    */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Get the title of the form
     * @return the subform title
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
    * Set the title of the form
    * @param strTitle the title to set
    */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Get the outputs of the form
     * @return the subform outputs
     */
    public Outputs getOutputs(  )
    {
        return _outputs;
    }

    /**
    * Set the outputs of the form
    * @param outputs the outputs to set
    */
    public void setOutputs( Outputs outputs )
    {
        _outputs = outputs;
    }

    /**
     * Get the transaction Id in session
     * @param request the http request
     * @return the transaction id
     */
    public String getTransactionId( HttpServletRequest request )
    {
        return (String) this.getSessionAttribute( request, SESSION_ATTRIBUTE_TRANSACTION_ID );
    }

    /**
    * Set the transaction Id in session
    * @param request the http request
    * @param strTransactionId the transactionid
    */
    public void setTransactionId( HttpServletRequest request, String strTransactionId )
    {
        setSessionAttribute( request, SESSION_ATTRIBUTE_TRANSACTION_ID, strTransactionId );
    }

    /**
     * This method returns a SubForm from a given name
     * @param strName The name of the SubForm to retrieve
     * @return The SubForm object or null of not found
     */
    public SubForm getSubForm( String strName )
    {
        return (SubForm) _subForms.get( strName );
    }

    /**
     * Add a subform to the current form
     * @param strName The SubForm name
     * @param subForm The SubForm object
     */
    public void addSubForm( String strName, SubForm subForm )
    {
        _subForms.put( strName, subForm );
    }

    /**
     * This method is used to defined the first SubForm of the form.
     * This subform is the default subform if the request parameter is not defined.
     * @param subform The first SubForm
     */
    public void setFirstSubForm( SubForm subform )
    {
        _subformFirst = subform;
    }

    /**
     * This method returns the first SubForm of the form.
     * This subform is the default subform if the request parameter is not defined.
     * @return The first SubForm
     */
    public SubForm getFirstSubForm(  )
    {
        return _subformFirst;
    }

    /**
     * This method build an XML document from a given object.
     * The object must have been generated by JAXB and be part
     * of the specific package.
     * @param object The object to marshall
     * @return The XML built as a String
     */
    public String getXml( Object object )
    {
        String strXml = "";

        if ( object == null )
        {
            return strXml;
        }

        try
        {
            StringWriter writer = new StringWriter(  );
            Marshaller marshaller = _jaxbSpecificContext.createMarshaller(  );
            marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
            marshaller.marshal( object, writer );
            strXml = writer.toString(  );
        }
        catch ( JAXBException e )
        {
            throw new AppException( "Formengine : an error occurred during marshalling for the form : " +
                this.getName(  ), e );
        }

        return strXml;
    }

    /**
     * This method retruns the root element containing the form data.
     * @param request The HTTP Request
     * @return The root element as an object
     */
    public Object getFormDocument( HttpServletRequest request )
    {
        // Retrieve the object from a session attribute
        Object object = this.getSessionAttribute( request, SESSION_ATTRIBUTE_FORM_DOCUMENT );

        if ( object == null )
        {
            AppLogService.error( "FORMENGINE : FormDocument retrieved is null" );
        }

        return object;
    }

    /**
     * Allow/forbid access to a subform given its name
     * @param request the httm request
     * @param strSubFormName the name of the subform to state access of
     * @param bIsSubFormAllowed true if the subform access should be allowed, false otherwise
     */
    public void setIsAccessToSubFormAllowed( HttpServletRequest request, String strSubFormName,
        boolean bIsSubFormAllowed )
    {
        SubForm subform = this.getSubForm( strSubFormName );

        if ( subform != null )
        {
            subform.setIsSubFormAllowed( request, bIsSubFormAllowed );
        }
    }

    /**
     * Check that the access to a subform is allowed.
     * This implies that the access to all previous subforms has been allowed.
     * @param request the http request
     * @param strSubFormName the name of the subform to check access of
     * @return
     */
    public boolean checkIsAccessToSubFormAllowed( HttpServletRequest request, String strSubFormName )
    {
        SubForm subform = this.getSubForm( strSubFormName );
        boolean bAllowed = false;

        while ( ( subform != null ) )
        {
            bAllowed = subform.getIsSubFormAllowed( request );

            if ( bAllowed == false )
            {
                break;
            }

            subform = subform.getPreviousSubForm(  );
        }

        return bAllowed;
    }

    /**
     * This method sets the object corresponding to the root element of the form
     * @param request The HTTP request
     * @param object The root element of the form data.
     */
    public void setFormDocument( HttpServletRequest request, Object object )
    {
        // Store the object in a session attribute
        this.setSessionAttribute( request, SESSION_ATTRIBUTE_FORM_DOCUMENT, object );
    }

    /**
     * This method provides the XML document corresponding to the form specific data.
     * @param request The HTTP request
     * @return The XML document as a String, ie the xml stream correponding to the root specific element
     */
    public String getXmlFormDocument( HttpServletRequest request )
    {
        return getXml( getFormDocument( request ) );
    }

    /**
     * This method provides a validator to check data stored in
     * the generated classes according the XML schema.
     * @return A validator object
     * @throws JAXBException
     */
    public Validator getXmlValidator(  ) throws JAXBException
    {
        return _jaxbSpecificContext.createValidator(  );
    }

    /**
     * This method provides a marshaller to create an XML document from
     * the generated classes
     * @return A marshaller object
     * @throws JAXBException
     */
    public Marshaller getXmlMarshaller(  ) throws JAXBException
    {
        return _jaxbSpecificContext.createMarshaller(  );
    }

    /**
     * This method process the form. It should called by the last subform after
     * the final validation.
     * @param request The HTTP request
     *
     */
    public void processForm( HttpServletRequest request )
    {
        try
        {
            // test if generation has already be done (protect from back)
            Boolean bForbidden = (Boolean) this.getSessionAttribute( request, SESSION_ATTRIBUTE_BLOCK_PROCESSING );

            // fix case where session has been flushed : the parameter in session is null but access to
            // output generation should not be granted.
            // Thus, if the form document is null also, this means that session has been flushed :
            // Access to output generation is not granted
            if ( ( bForbidden == null ) && ( this.getFormDocument( request ) == null ) )
            {
                bForbidden = Boolean.TRUE;
            }

            if ( ( bForbidden == null ) || ( bForbidden.booleanValue(  ) == false ) )
            {
                Outputs outputs = this.getOutputs(  );

                for ( OutputSet outputSet : outputs.getOutputSet(  ) )
                {
                    boolean bUseHeader = outputSet.isUseTransactionHeaders(  );

                    Object transactionObject;

                    // if we use header, init the transaction object and generate xml
                    if ( bUseHeader )
                    {
                        IdGenerator idGenerator;

                        // the transaction id generator might have been customized
                        String strSpecificTransactionIdGenerator = outputSet.getSpecificTransactionIdGeneratorClassName(  );

                        if ( ( strSpecificTransactionIdGenerator != null ) &&
                                ( !strSpecificTransactionIdGenerator.trim(  ).equals( "" ) ) )
                        {
                            idGenerator = (IdGenerator) Class.forName( strSpecificTransactionIdGenerator ).newInstance(  );
                        }
                        else
                        {
                            // if no custom id generator, use default one
                            idGenerator = new TransactionFileIdGenerator(  );
                        }

                        // the generator might need to know the form name
                        String strNewId = idGenerator.getNewId( this );

                        // put the transactionId in session for display
                        this.setTransactionId( request, strNewId );

                        // initialise transaction object
                        Transaction transaction = this.initializeTransactionHeaders( request );

                        // set the id
                        transaction.setTransactionId( strNewId );

                        // insert the specific document into the transaction
                        ObjectFactory factory = new ObjectFactory(  );
                        TransactionData data = factory.createTransactionData(  );
                        data.setAny( getFormDocument( request ) );
                        transaction.setTransactionData( data );
                        transactionObject = transaction;
                    }
                    else // generate specific xml if no header
                    {
                        transactionObject = this.getFormDocument( request );
                    }

                    // get all the output element of this outputSet
                    Collection<Output> colOutputs = outputSet.getOutput(  );

                    for ( Output output : colOutputs )
                    {
                        String strOutputClassName = output.getOutputProcessorClassName(  );
                        OutputProcessor ouputProcessor = (OutputProcessor) Class.forName( strOutputClassName )
                                                                                .newInstance(  );
                        ouputProcessor.process( this, transactionObject );
                    }
                }

                // successed : prevent from coming back here
                this.setSessionAttribute( request, SESSION_ATTRIBUTE_BLOCK_PROCESSING, Boolean.TRUE );
            }
        }
        catch ( InstantiationException e )
        {
            throw new AppException( "Formengine : an error occurred during the output processing for the form : " +
                this.getName(  ), e );
        }
        catch ( IllegalAccessException e )
        {
            throw new AppException( "Formengine : an error occurred during the output processing for the form : " +
                this.getName(  ), e );
        }
        catch ( ClassNotFoundException e )
        {
            throw new AppException( "Formengine : an error occurred during the output processing for the form : " +
                this.getName(  ), e );
        }
    }

    /**
     *
     * @param request
     * @return
     */
    Transaction initializeTransactionHeaders( HttpServletRequest request )
    {
        ObjectFactory factory = new ObjectFactory(  );

        SenderInfo sender = factory.createSenderInfo(  );
        sender.setRemoteAddress( request.getRemoteAddr(  ) );

        String strUser = this.getRemoteUser( request );

        if ( strUser != null )
        {
            sender.setRemoteUser( strUser );
        }
        else
        {
            sender.setRemoteUser( "" );
        }

        ServerInfo server = factory.createServerInfo(  );
        String strInstanceName = this.getInstanceName(  );
        server.setInstanceName( strInstanceName );

        TransactionInfo tInfo = factory.createTransactionInfo(  );
        tInfo.setTransactionCode( this.getTransactionCode(  ) );
        tInfo.setTransactionName( this.getTransactionName(  ) );

        Transaction transaction = factory.createTransaction(  );
        transaction.setSenderInfo( sender );
        transaction.setServerInfo( server );
        transaction.setTransactionInfo( tInfo );

        // generate the date and time of the transaction
        Date currentSqlDate = new Date( System.currentTimeMillis(  ) );
        Time currentSqlTime = new Time( System.currentTimeMillis(  ) );
        transaction.setTransactionDate( currentSqlDate );
        transaction.setTransactionTime( currentSqlTime );

        return transaction;
    }

    /**
     * This method returns the user of the request. The default implementation
     * returns the getRemoteUser of the request.
     *
     * @param request The HTTP request
     * @return The user as a String
     */
    public String getRemoteUser( HttpServletRequest request )
    {
        return request.getRemoteUser(  );
    }

    /**
     * Add an error to the current session
     * @param request The HTTP request
     * @param strErrorKey The error key defined in a properties file
     */
    public void addError( HttpServletRequest request, String strErrorKey )
    {
        this.addError( request, strErrorKey, null );
    }

    /**
     * Add an error to the current session, completing the message in property with a parameter
     * @param request The HTTP request
     * @param strErrorKey The error key defined in a properties file
     * @param strParameter The parameter to insert
     */
    public void addError( HttpServletRequest request, String strErrorKey, String strParameter )
    {
        FormErrorsList errors = (FormErrorsList) this.getSessionAttribute( request, SESSION_ATTRIBUTE_FORM_ERRORS );

        if ( errors == null )
        {
            errors = new FormErrorsList(  );
        }

        errors.addError( strErrorKey, strParameter );
        setSessionAttribute( request, SESSION_ATTRIBUTE_FORM_ERRORS, errors );
    }

    /**
     * Add an error to the current session, completing the message in property with a parameter
     * @param request The HTTP request
     * @param strErrorMessage The error message
     * @param strParameter The parameter to insert
     */
    public void addErrorMessage( HttpServletRequest request, String strErrorMessage )
    {
        FormErrorsList errors = (FormErrorsList) this.getSessionAttribute( request, SESSION_ATTRIBUTE_FORM_ERRORS );

        if ( errors == null )
        {
            errors = new FormErrorsList(  );
        }

        errors.addErrorMessage( strErrorMessage );
        setSessionAttribute( request, SESSION_ATTRIBUTE_FORM_ERRORS, errors );
    }

    /**
     * Reset all filling errors associated to the session
     * @param request The HTTP request
     */
    public void resetErrors( HttpServletRequest request )
    {
        FormErrorsList errors = (FormErrorsList) this.getSessionAttribute( request, SESSION_ATTRIBUTE_FORM_ERRORS );

        if ( errors != null )
        {
            errors.clear(  );
        }

        setSessionAttribute( request, SESSION_ATTRIBUTE_FORM_ERRORS, errors );
    }

    /**
     * Returns all filling errors associated to the session.
     * @param request
     * @return
     */
    public String[] getErrors( HttpServletRequest request )
    {
        FormErrorsList list = (FormErrorsList) this.getSessionAttribute( request, SESSION_ATTRIBUTE_FORM_ERRORS );

        if ( list == null )
        {
            return null;
        }

        String[] errors = new String[list.size(  )];

        for ( int i = 0; i < list.size(  ); i++ )
        {
            errors[i] = (String) list.get( i );
        }

        return errors;
    }

    /**
     * This method provides a marshaller to create a subform page
     * from the form definition classes
     * @return A marshaller object
     * @throws JAXBException
     */
    public Marshaller getFormElementsMarshaller(  ) throws JAXBException
    {
        return _jaxbContextFormElements.createMarshaller(  );
    }

    /**
     *
     * @param request
     * @param strAttributeName
     * @param attribute
     */
    public void setSessionAttribute( HttpServletRequest request, String strAttributeName, Object attribute )
    {
        HttpSession session = request.getSession( true );
        String strPrefix = SharedConstants.SESSION_PREFIX + "_" + this.getName(  ) + "_";
        String strSessionAttribute = strPrefix + strAttributeName.trim(  );
        strSessionAttribute = strSessionAttribute.toUpperCase(  );
        session.setAttribute( strSessionAttribute, attribute );
    }

    /**
     *
     * @param request
     * @param strAttributeName
     * @return
     */
    public Object getSessionAttribute( HttpServletRequest request, String strAttributeName )
    {
        HttpSession session = request.getSession( true );
        String strPrefix = SharedConstants.SESSION_PREFIX + "_" + this.getName(  ) + "_";
        String strSessionAttribute = strPrefix + strAttributeName.trim(  );
        strSessionAttribute = strSessionAttribute.toUpperCase(  );

        return session.getAttribute( strSessionAttribute );
    }

    /**
     *
     * @param request
     * @param strAttributeName
     * @param attribute
     */
    public void removeSessionAttribute( HttpServletRequest request, String strAttributeName )
    {
        HttpSession session = request.getSession( true );
        String strPrefix = SharedConstants.SESSION_PREFIX + "_" + this.getName(  ) + "_";
        String strSessionAttribute = strPrefix + strAttributeName.trim(  );
        strSessionAttribute = strSessionAttribute.toUpperCase(  );
        session.removeAttribute( strSessionAttribute );
    }

    /**
     * Invalidate the session.
     * The main use should be to close the session
     * when the form has been processed
     * @param request
     */
    public void flushSession( HttpServletRequest request )
    {
        HttpSession session = request.getSession( true );

        String strPrefix = SharedConstants.SESSION_PREFIX + "_" + this.getName(  ) + "_";
        strPrefix = strPrefix.toUpperCase(  );

        Enumeration names = session.getAttributeNames(  );
        Collection<String> luteceNamesCol = new ArrayList<String>(  );

        while ( names.hasMoreElements(  ) )
        {
            String strName = (String) names.nextElement(  );

            if ( strName.startsWith( strPrefix ) )
            {
                luteceNamesCol.add( strName );
            }
        }

        for ( String strName : luteceNamesCol )
        {
            session.removeAttribute( strName );
        }
    }
}
