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

import fr.paris.lutece.plugins.formengine.service.output.IdGenerator;
import fr.paris.lutece.plugins.formengine.web.Form;
import fr.paris.lutece.plugins.projeau.business.Projeau;
import fr.paris.lutece.plugins.projeau.business.ProjeauFormDocument;
import fr.paris.lutece.plugins.projeau.service.output.ProjeauFileIdGenerator;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;


/**
 * This class implements the methods needed by the "Projeau" formengine module.
 */
public class ProjeauForm extends Form
{
    private static final String PROPERTY_FRAGMENT_TRANSACTION_CODE = ".transaction.code";
    private static final String PROPERTY_FRAGMENT_TRANSACTION_NAME = ".transaction.name";
    private static final String PROPERTY_FRAGMENT_INSTANCE_NAME = ".instance.name";
    private static final String PROPERTY_FRAGMENT_PLUGIN_NAME = ".plugin.name";

    /**
     * Contructor
     * @param strFormName the name of the form
     * @see fr.paris.lutece.plugins.formengine.web.Form#Form(String)
     */
    public ProjeauForm( String strFormName )
    {
        super( strFormName );
    }

    /**
     * Get the path of the directory used to store the xsl stylesheets
     * @see fr.paris.lutece.plugins.formengine.web.Form#getXslDirectoryPath()
     */
    public String getXslDirectoryPath(  )
    {
        return AppPathService.getPath( this.getName(  ) + SharedConstants.PROPERTY_FRAGMENT_XSL_DIRECTORY );
    }

    /**
     * Get the specific package name for jaxb specific data generation.
     * In this case, we give the formdefinition package name to provide a valid configuration,
     * but JAXB is not used for the specific business part.
     * Thus, none of the business specific JAXB functionnalities can be used.
     * @see fr.paris.lutece.plugins.formengine.web.Form#getGeneratedPackageName()
     */
    protected String getGeneratedPackageName(  )
    {
        return AppPropertiesService.getProperty( "formengine.jaxb.packagename.formdefinition" );
    }

    /**
     * Get the transaction code
     * @see fr.paris.lutece.plugins.formengine.web.Form#getTransactionCode()
     */
    public String getTransactionCode(  )
    {
        return AppPropertiesService.getProperty( this.getName(  ) + PROPERTY_FRAGMENT_TRANSACTION_CODE );
    }

    /**
     * Get the instance name
     * @see fr.paris.lutece.plugins.formengine.web.Form#getInstanceName()
     */
    public String getInstanceName(  )
    {
        return AppPropertiesService.getProperty( this.getName(  ) + PROPERTY_FRAGMENT_INSTANCE_NAME );
    }

    /**
     * Get the transaction name
     * @see fr.paris.lutece.plugins.formengine.web.Form#getTransactionName()
     */
    public String getTransactionName(  )
    {
        return AppPropertiesService.getProperty( this.getName(  ) + PROPERTY_FRAGMENT_TRANSACTION_NAME );
    }

    /**
     * Overrides the processForm method.
     * This is done in order to perform the specific id generation,
     * and in order to save the date at which the projeau was performed.
     * The method from superclass is called to perform the "classic" processing
     * @see fr.paris.lutece.plugins.formengine.web.Form#processForm(javax.servlet.http.HttpServletRequest)
     */
    public void processForm( HttpServletRequest request )
    {
        // retrieve the form document
        ProjeauFormDocument projeauFormDocument = (ProjeauFormDocument) this.getFormDocument( request );

        // retrieve the projeau of this form document
        Projeau projeau = projeauFormDocument.getProjeau(  );

        // store the current date in the projeau object
        projeau.setRegistrationDate( new Timestamp( new java.util.Date(  ).getTime(  ) ) );

        // generate the specific id, and store it
        IdGenerator idGenerator = new ProjeauFileIdGenerator(  );
        String strIdInternet = idGenerator.getNewId( this );
        projeau.setIdInternet( strIdInternet );

        // call the method from superclass to actually process the form (ie generate the declared outputs)
        super.processForm( request );

        // save the transaction id.
        this.setTransactionId( request, strIdInternet );
    }

    /**
     * Overrides the getXmlFormDocument method from Form
     * This allows us to retrieve the specific document's xml data without using the existing JAXB mechanism.
     * @see fr.paris.lutece.plugins.formengine.web.Form#getXmlFormDocument(javax.servlet.http.HttpServletRequest)
     */
    public String getXmlFormDocument( HttpServletRequest request )
    {
        ProjeauFormDocument projeauFormDocument = (ProjeauFormDocument) this.getFormDocument( request );

        return projeauFormDocument.getXmlDocument(  );
    }
}
