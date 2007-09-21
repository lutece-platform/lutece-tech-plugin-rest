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
package fr.paris.lutece.plugins.formengine.service;

import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.FormDefinition;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.FormElements;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.SubFormDefinition;
import fr.paris.lutece.plugins.formengine.web.Form;
import fr.paris.lutece.plugins.formengine.web.SubForm;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.filesystem.FileListFilter;

import java.io.File;
import java.io.FilenameFilter;

import java.lang.reflect.Constructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


/**
 * This class is responsible for registrating the forms. It scans the directory containing all the form definitions
 * and load the data from those files into the Form and SubForm objects.
 */
public class FormsRegistrationService
{
    private static final String PATH_FORM_DEFINITIONS = "formengine.path.formdefinitions";
    private static final String EXTENSION_FILE = "xml";
    private static final String CLASS_NAME_STRING = "java.lang.String";
    private static final String PROPERTY_FORMDEFINITION_PACKAGE_NAME = "formengine.jaxb.packagename.formdefinition";
    private static final String PLUGIN_NAME = "formengine";
    private static Map _mapForms = new HashMap(  );

    private FormsRegistrationService(  )
    {
    }

    /**
     * Load the forms from the form definition files.
     * Scans  the form definitions directory and instanciate
     * the corresponding objects.
     *
     */
    public static void loadForms(  )
    {
        JAXBContext jaxbContextFormElements = null;

        String strPackageName = AppPropertiesService.getProperty( PROPERTY_FORMDEFINITION_PACKAGE_NAME );
        Unmarshaller unmarshaller;

        try
        {
            jaxbContextFormElements = JAXBContext.newInstance( strPackageName );
            unmarshaller = jaxbContextFormElements.createUnmarshaller(  );

            File dirPlugin = new File( AppPathService.getPath( PATH_FORM_DEFINITIONS ) );

            if ( ( dirPlugin != null ) && dirPlugin.exists(  ) )
            {
                FilenameFilter select = new FileListFilter( "", EXTENSION_FILE );
                File[] listFile = dirPlugin.listFiles( select );

                for ( int i = 0; i < listFile.length; i++ )
                {
                    File file = listFile[i];

                    try
                    {
                        FormDefinition formDefinition = (FormDefinition) unmarshaller.unmarshal( file );

                        String strFormClassName = formDefinition.getClassName(  );

                        Class formClass = Class.forName( strFormClassName );
                        Class stringClass = Class.forName( CLASS_NAME_STRING );
                        Constructor formConstructor = formClass.getConstructor( new Class[] { stringClass } );
                        String strFormName = formDefinition.getName(  );
                        Form form = (Form) formConstructor.newInstance( (Object[]) new String[] { strFormName } );

                        form.setTitle( formDefinition.getTitle(  ) );

                        // retrieve the outputs
                        form.setOutputs( formDefinition.getOutputs(  ) );

                        _mapForms.put( formDefinition.getName(  ), form );

                        List listSubForms = formDefinition.getSubForms(  ).getSubFormDefinition(  );

                        SubForm previousSubForm = null;

                        for ( int index = 0; index < listSubForms.size(  ); index++ )
                        {
                            SubFormDefinition subformDefinition = (SubFormDefinition) listSubForms.get( index );

                            String strSubFormClassName = subformDefinition.getClassName(  );
                            SubForm subForm = (SubForm) Class.forName( strSubFormClassName ).newInstance(  );

                            subForm.setName( subformDefinition.getName(  ) );
                            subForm.setTitle( subformDefinition.getTitle(  ) );

                            // keep the fields in order to initialise later the objets in session.
                            subForm.initFormElements( (FormElements) subformDefinition.getFormElements(  ) );

                            subForm.setParentForm( form );
                            form.addSubForm( subformDefinition.getName(  ), subForm );

                            if ( index == 0 )
                            {
                                form.setFirstSubForm( subForm );
                            }

                            subForm.setPreviousSubForm( previousSubForm );

                            if ( previousSubForm != null )
                            {
                                previousSubForm.setNextSubForm( subForm );
                            }

                            previousSubForm = subForm;
                        }
                    }
                    catch ( Exception e )
                    {
                        AppLogService.error( PLUGIN_NAME.toUpperCase(  ) + " : " + e.getMessage(  ), e );
                    }
                }
            }
        }
        catch ( JAXBException e )
        {
            AppLogService.error( PLUGIN_NAME.toUpperCase(  ) + " : " + e.getMessage(  ), e );
        }
    }

    /**
     * Return the form from its name
     * @param strFormName the name of the form to retrieve
     * @return the form of given name
     * This is the method used by FormEngineApp to access a form
     */
    public static Form getForm( String strFormName )
    {
        Form form = (Form) _mapForms.get( strFormName );

        return form;
    }
}
