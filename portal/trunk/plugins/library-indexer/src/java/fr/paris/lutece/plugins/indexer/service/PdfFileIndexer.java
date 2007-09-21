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
package fr.paris.lutece.plugins.indexer.service;

import fr.paris.lutece.portal.service.util.AppLogService;

import org.pdfbox.exceptions.CryptographyException;
import org.pdfbox.exceptions.InvalidPasswordException;

import org.pdfbox.pdmodel.PDDocument;

import org.pdfbox.util.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;


/**
 * PdfFileIndexer
 */
public class PdfFileIndexer implements IFileIndexer
{
    public String getContentToIndex( InputStream is )
    {
        String strContent = "";
        PDDocument pdfDocument = null;

        try
        {
            pdfDocument = PDDocument.load( is );

            if ( pdfDocument.isEncrypted(  ) )
            {
                pdfDocument.decrypt( "" );
            }

            StringWriter writer = new StringWriter(  );
            PDFTextStripper stripper = new PDFTextStripper(  );
            stripper.writeText( pdfDocument, writer );
            strContent = writer.getBuffer(  ).toString(  );
        }
        catch ( CryptographyException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        catch ( InvalidPasswordException e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }
        finally
        {
            if ( pdfDocument != null )
            {
                try
                {
                    pdfDocument.close(  );
                }
                catch ( IOException e )
                {
                    AppLogService.error( e.getMessage(  ), e );
                }
            }
        }

        return strContent;
    }
}
