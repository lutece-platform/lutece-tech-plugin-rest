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
package fr.paris.lutece.plugins.ods.service.manager;

import fr.paris.lutece.portal.service.spring.SpringContextService;

import net.sf.jcopist.service.FileData;
import net.sf.jcopist.service.IDocumentService;
import net.sf.jcopist.service.ITemplateService;
import net.sf.jcopist.service.Job;
import net.sf.jcopist.service.Result;
import net.sf.jcopist.service.exception.ServerException;
import net.sf.jcopist.service.exception.UnavailableException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Classe qui a pour responsabilité de faire le proxy entre l'application ODS
 * et l'outil utilisé pour généré les documents PDF
 *
 */
public class DocumentManager
{
    public static final String PDF_CONVERSION = "pdf";
    public static final String CONSTANTE_ODT = "odt";
    public static final String PDF_WORD = "doc";

    /**
     * Cette méthode permet de convertir un fichier XML en un fichier PDF
     *
     * @param request HttpServletRequest
     * @param strFormatConversion Format de la conversion tel que PDF ou DOC
     * @param strNameTemplate Nom de la template utilisé
     * @param xml fichier XML
     * @param os OutputStream
     * @throws UnavailableException UnavailableException
     * @throws ServerException ServerException
     * @throws IOException IOException
     */
    public static byte[] convert( HttpServletRequest request, String strFormatConversion, String strNameTemplate,
        byte[] xml ) throws UnavailableException, ServerException, IOException
    {
        IDocumentService documentService = (IDocumentService) SpringContextService.getPluginBean( "ods",
                "documentService" );
        
        byte[] bConvertedDoc = null;

        /* Create a job */
        Job job = new Job(  );
        job.setDestFormat( strFormatConversion );
        job.setTemplateName( strNameTemplate );

        job.setXmlData( xml );

        /* Call the document service */
        Result result = null;

        result = documentService.process( job );

        if ( ( null != result ) && ( null != result.getFiles(  ) ) )
        {
            /* Save the result files */
            FileData[] fileDatas = result.getFiles(  );
            if (fileDatas != null && fileDatas.length > 0) 
            {
            	bConvertedDoc = fileDatas[0].getBinaryData(  );
            }
        }
        
        return bConvertedDoc;
    }

    /**
     * Deploie une nouvelle template
     * @param documentService le document
     * @param templateService la template
     * @throws Exception if an error occurs.
     */
    public static void deployTemplateService( String pathTemplate, String nameTemplate )
        throws Exception
    {
        ITemplateService templateService = (ITemplateService) SpringContextService.getPluginBean( "ods",
                "templateService" );

        final String TEMPLATE_ESSAI_PATH = pathTemplate;
        final String TEMPLATE_ESSAI_NAME = nameTemplate;

        Map<String, String> templateMap = new HashMap<String, String>(  );
        templateMap.put( TEMPLATE_ESSAI_NAME, TEMPLATE_ESSAI_PATH );

        for ( String templateName : templateMap.keySet(  ) )
        {
            // Read the template file data
            File templateFile = new File( templateMap.get( templateName ) );
            InputStream inStream = new BufferedInputStream( new FileInputStream( templateFile ) );
            ByteArrayOutputStream outStream = new ByteArrayOutputStream(  );
            int c;

            while ( ( c = inStream.read(  ) ) >= 0 )
            {
                outStream.write( c );
            }

            // Register the template
            templateService.register( templateName, CONSTANTE_ODT, outStream.toByteArray(  ) );
        }
    }
}
