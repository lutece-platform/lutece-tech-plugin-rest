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
package fr.paris.lutece.plugins.document.service;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentHome;

import java.util.List;


/**
 * This Class provides tools to fix document data
 */
public class DocumentTools
{
    /**
     * Rebuild all XML content according data found in the document_content table
     *
     * @param bTrace Add all XML to the output
     * @return An output of the process
     */
    public static String rebuildXmlContent( boolean bTrace )
    {
        StringBuffer sbOutput = new StringBuffer(  );
        List<Document> listDocuments = DocumentHome.findAll(  );

        for ( Document document : listDocuments )
        {
            String strXml = DocumentService.getInstance(  ).buildXmlContent( document );
            document.setXmlWorkingContent( strXml );
            DocumentHome.update( document, false );

            if ( bTrace )
            {
                sbOutput.append( "\n-----------------------------" );
                sbOutput.append( "\nDocument Title : " );
                sbOutput.append( document.getTitle(  ) );
                sbOutput.append( "\nDocument Type : " );
                sbOutput.append( document.getCodeDocumentType(  ) );
                sbOutput.append( "\nXML Content : \n" );
                sbOutput.append( strXml );
            }
        }

        sbOutput.append( "\n=============================\n" );
        sbOutput.append( listDocuments.size(  ) );
        sbOutput.append( " items processed" );

        return sbOutput.toString(  );
    }
}
