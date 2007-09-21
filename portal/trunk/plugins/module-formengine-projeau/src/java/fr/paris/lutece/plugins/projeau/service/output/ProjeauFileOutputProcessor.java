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
package fr.paris.lutece.plugins.projeau.service.output;

import fr.paris.lutece.plugins.formengine.service.output.OutputProcessor;
import fr.paris.lutece.plugins.formengine.web.Form;
import fr.paris.lutece.plugins.projeau.business.Projeau;
import fr.paris.lutece.plugins.projeau.business.ProjeauFormDocument;
import fr.paris.lutece.plugins.projeau.web.ProjeauForm;
import fr.paris.lutece.portal.service.fileupload.FileUploadService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.filesystem.UploadUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.fileupload.FileItem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * This method is responsible for ouputing the form data to the file system.
 */
public class ProjeauFileOutputProcessor extends OutputProcessor
{
    // Markers
    private static final String MARK_PROJEAU = "projeau";
    private static final String MARK_FILE_LIST = "file_list";    
    
    //Properties
    private static final String PROPERTY_UPLOAD_DIRECTORY = "projeau.upload.directory";
    private static final String PROPERTY_TRANSACTION_FILE_EXTENSION = "projeau.transaction.file.extension";
    private static final String PROPERTY_PROJEAU_LOCALE = "projeau.locale";
    
    //Templates
    private static final String TEMPLATE_TRANSACTION_FILE = "admin/plugins/formengine/modules/projeau/transaction_file.html";
    
        
    /**
     * Initializes the plugin instance to use to write the files.
     * @see fr.paris.lutece.plugins.formengine.service.output.OutputProcessor#init(fr.paris.lutece.plugins.formengine.web.Form, java.lang.Object)
     */
    protected void init( Form form, Object formDocument )
    {
        ProjeauForm projeauForm = (ProjeauForm) form;
    }

    /** Actually performs the output processing, ie. files in the file system.
     *
     * @see fr.paris.lutece.plugins.formengine.service.output.OutputProcessor#generateOutput(java.lang.Object)
     */
    protected void generateOutput( Object formDocument )
    {
        // get the form document
        ProjeauFormDocument projeauFormDocument = (ProjeauFormDocument) formDocument;

        // get the projeau and create the corresponding record into the database.
        Projeau projeau = projeauFormDocument.getProjeau(  );
        String strIdInternet = projeau.getIdInternet(  );
        
        // get the uploaded documents list
        ArrayList<FileItem> fileList = projeauFormDocument.getUploadedDocumentsList(  );
                        
        String destPath = AppPropertiesService.getProperty( PROPERTY_UPLOAD_DIRECTORY ) + projeau.getIdInternet(  );
        
        try
        {
            HashMap model = new HashMap(  );            
            model.put( MARK_PROJEAU, projeau );
            model.put( MARK_FILE_LIST, fileList );
            Locale locale = new Locale( AppPropertiesService.getProperty( PROPERTY_PROJEAU_LOCALE ) );
            
            HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TRANSACTION_FILE, locale, model );           
            
            File file = new File( destPath );
            file.mkdir(  );
            FileWriter fw = new FileWriter( destPath + "/" + strIdInternet +
                    AppPropertiesService.getProperty( PROPERTY_TRANSACTION_FILE_EXTENSION ) );            
            fw.write( template.getHtml() );
            fw.close(  );
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage(  ) );
        }

        if ( ( fileList != null ) && ( !fileList.isEmpty(  ) ) )
        {
            int fileIndex = 1;
            for ( FileItem uploadedFile : fileList )
            {
                if ( uploadedFile.get(  ) != null )
                {
                    File destFile = new File( destPath, strIdInternet + "_" + fileIndex + "_" + UploadUtil.cleanFileName( uploadedFile.getName(  ) ) );
                    fileIndex += 1;
                    try
                    {
                        FileOutputStream fos = new FileOutputStream( destFile );
                        fos.flush(  );
                        fos.write( uploadedFile.get(  ) );
                        fos.close(  );
                    }
                    catch ( Exception e )
                    {
                        AppLogService.error( e.getMessage(  ) );
                    }                 
                }
            }
        }
    }
}
