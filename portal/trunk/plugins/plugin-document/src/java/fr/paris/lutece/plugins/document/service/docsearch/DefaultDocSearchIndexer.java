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
package fr.paris.lutece.plugins.document.service.docsearch;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.attributes.DocumentAttribute;
import fr.paris.lutece.plugins.document.service.fileindexing.FileIndexerFactory;
import fr.paris.lutece.plugins.document.service.fileindexing.IFileIndexer;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.apache.lucene.demo.html.HTMLParser;
import org.apache.lucene.document.Field;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.List;


/**
 * DefaultDocSearchIndexer
 */
public class DefaultDocSearchIndexer implements IDocSearchIndexer
{
    private static FileIndexerFactory _factoryIndexer = (FileIndexerFactory) SpringContextService.getPluginBean( "document",
            "fileIndexerFactory" );

    /**
     * Build Lucene docs to index
     * @param listDocuments Documents to index
     * @throws java.io.IOException
     * @return A list of Lucene documents
     */
    public List<org.apache.lucene.document.Document> getDocuments( List<Document> listDocuments )
        throws IOException
    {
        List<org.apache.lucene.document.Document> listLuceneDocs = new ArrayList<org.apache.lucene.document.Document>(  );

        for ( Document document : listDocuments )
        {
            listLuceneDocs.add( getDocument( document ) );
        }

        return listLuceneDocs;
    }

    private org.apache.lucene.document.Document getDocument( Document document )
        throws IOException
    {
        // make a new, empty Lucene document
        org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document(  );

        // Add the last modified date of the file a field named "modified".
        // Use a field that is indexed (i.e. searchable), but don't tokenize
        // the field into words.
        DateFormat formater = DateFormat.getDateInstance( DateFormat.SHORT );
        String strDate = formater.format( document.getDateModification(  ) );
        doc.add( new Field( DocSearchItem.FIELD_DATE, strDate, Field.Store.YES, Field.Index.UN_TOKENIZED ) );

        // Add the uid as a field, so that index can be incrementally maintained.
        // This field is stored with document, it is indexed, but it is not
        // tokenized prior to indexing.
        String strIdDocument = String.valueOf( document.getId(  ) );
        doc.add( new Field( DocSearchItem.FIELD_UID, strIdDocument, Field.Store.YES, Field.Index.UN_TOKENIZED ) );

        String strContentToIndex = getContentToIndex( document );
        StringReader readerPage = new StringReader( strContentToIndex );
        HTMLParser parser = new HTMLParser( readerPage );

        //the content of the article is recovered in the parser because this one
        //had replaced the encoded caracters (as &eacute;) by the corresponding special caracter (as ?)
        Reader reader = parser.getReader(  );
        int c;
        StringBuffer sb = new StringBuffer(  );

        while ( ( c = reader.read(  ) ) != -1 )
        {
            sb.append( String.valueOf( (char) c ) );
        }

        reader.close(  );

        // Add the tag-stripped contents as a Reader-valued Text field so it will
        // get tokenized and indexed.
        doc.add( new Field( DocSearchItem.FIELD_CONTENTS, sb.toString(  ), Field.Store.NO, Field.Index.TOKENIZED ) );

        // Add the title as a separate Text field, so that it can be searched
        // separately.
        doc.add( new Field( DocSearchItem.FIELD_TITLE, document.getTitle(  ), Field.Store.YES, Field.Index.NO ) );
        doc.add( new Field( DocSearchItem.FIELD_SUMMARY, document.getSummary(  ), Field.Store.YES, Field.Index.NO ) );

        doc.add( new Field( DocSearchItem.FIELD_TYPE, document.getType(  ), Field.Store.YES, Field.Index.TOKENIZED ) );
        doc.add( new Field( DocSearchItem.FIELD_SPACE, "s" + document.getSpaceId(  ), Field.Store.YES,
                Field.Index.TOKENIZED ) );

        // return the document
        return doc;
    }

    private static String getContentToIndex( Document document )
    {
        StringBuffer sbContentToIndex = new StringBuffer(  );
        sbContentToIndex.append( document.getTitle(  ) );
        sbContentToIndex.append( " " );
        sbContentToIndex.append( document.getSummary(  ) );
        sbContentToIndex.append( " " );

        for ( DocumentAttribute attribute : document.getAttributes(  ) )
        {
            if ( attribute.isSearchable(  ) )
            {
                if ( !attribute.isBinary(  ) )
                {
                    sbContentToIndex.append( attribute.getTextValue(  ) );
                    sbContentToIndex.append( " " );
                }
                else
                {
                    IFileIndexer indexer = _factoryIndexer.getIndexer( attribute.getValueContentType(  ) );

                    if ( indexer != null )
                    {
                        try
                        {
                            ByteArrayInputStream bais = new ByteArrayInputStream( attribute.getBinaryValue(  ) );
                            sbContentToIndex.append( indexer.getContentToIndex( bais ) );
                            sbContentToIndex.append( " " );
                            bais.close(  );
                        }
                        catch ( IOException e )
                        {
                            AppLogService.error( e.getMessage(  ), e );
                        }
                    }
                }
            }
        }

        // Add metadata in XML (xml tags will be ignored by the HTML parsing)
        sbContentToIndex.append( document.getXmlMetadata(  ) );

        return sbContentToIndex.toString(  );
    }
}
