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

import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpace;
import fr.paris.lutece.plugins.document.service.spaces.DocumentSpacesService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.misc.ChainedFilter;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryFilter;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * DocumentSearchService
 */
public class DocSearchService
{
    // Constants corresponding to the variables defined in the lutece.properties file
    public static final String PATH_INDEX = "document.docsearch.lucene.indexPath";
    private static final String PROPERTY_WRITER_MERGE_FACTOR = "document.docsearch.lucene.writer.mergeFactor";
    private static final String PROPERTY_WRITER_MAX_FIELD_LENGTH = "document.docsearch.lucene.writer.maxFieldLength";
    private static final String PROPERTY_ANALYSER_CLASS_NAME = "document.docsearch.lucene.analyser.className";
    private static final int DEFAULT_WRITER_MERGE_FACTOR = 20;
    private static final int DEFAULT_WRITER_MAX_FIELD_LENGTH = 1000000;
    public static final String PARAM_FORCING = "forcing";
    private static String _strIndex;
    private static int _nWriterMergeFactor;
    private static int _nWriterMaxFieldLength;
    private static Analyzer _analyzer;
    private static Searcher _searcher;
    private static DocSearchService _singleton;
    private static IDocSearchIndexer _indexer;

    /** Creates a new instance of DocumentSearchService */
    private DocSearchService(  )
    {
        // Read configuration properties
        _strIndex = AppPathService.getPath( PATH_INDEX );

        if ( ( _strIndex == null ) || ( _strIndex.equals( "" ) ) )
        {
            throw new AppException( "Lucene index path not found in document.properties", null );
        }

        _nWriterMergeFactor = AppPropertiesService.getPropertyInt( PROPERTY_WRITER_MERGE_FACTOR,
                DEFAULT_WRITER_MERGE_FACTOR );
        _nWriterMaxFieldLength = AppPropertiesService.getPropertyInt( PROPERTY_WRITER_MAX_FIELD_LENGTH,
                DEFAULT_WRITER_MAX_FIELD_LENGTH );

        String strAnalyserClassName = AppPropertiesService.getProperty( PROPERTY_ANALYSER_CLASS_NAME );

        if ( ( strAnalyserClassName == null ) || ( strAnalyserClassName.equals( "" ) ) )
        {
            throw new AppException( "Analyser class name not found in lucene.properties", null );
        }

        _indexer = (IDocSearchIndexer) SpringContextService.getPluginBean( "document", "docSearchIndexer" );

        try
        {
            _analyzer = (Analyzer) Class.forName( strAnalyserClassName ).newInstance(  );
        }
        catch ( Exception e )
        {
            throw new AppException( "Failed to load Lucene Analyzer class", e );
        }
    }

    public static DocSearchService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new DocSearchService(  );
        }

        return _singleton;
    }

    public String processIndexing( boolean bCreate )
    {
        StringBuffer sbLogs = new StringBuffer(  );

        IndexWriter writer = null;

        try
        {
            sbLogs.append( "\r\nIndexing all contents ...\r\n" );

            Date start = new Date(  );
            writer = new IndexWriter( _strIndex, _analyzer, bCreate );
            writer.setMergeFactor( _nWriterMergeFactor );
            writer.setMaxFieldLength( _nWriterMaxFieldLength );

            List<Document> listDocuments = _indexer.getDocuments( DocumentHome.findAll(  ) );

            for ( Document doc : listDocuments )
            {
                writer.addDocument( doc );
                sbLogs.append( "Indexing " );
                sbLogs.append( doc.get( DocSearchItem.FIELD_TYPE ) );
                sbLogs.append( " #" );
                sbLogs.append( doc.get( DocSearchItem.FIELD_UID ) );
                sbLogs.append( " - " );
                sbLogs.append( doc.get( DocSearchItem.FIELD_TITLE ) );
                sbLogs.append( "\r\n" );
            }

            writer.optimize(  );

            Date end = new Date(  );
            sbLogs.append( "Duration of the treatment : " );
            sbLogs.append( end.getTime(  ) - start.getTime(  ) );
            sbLogs.append( " milliseconds\r\n" );
        }
        catch ( Exception e )
        {
            sbLogs.append( " caught a " );
            sbLogs.append( e.getClass(  ) );
            sbLogs.append( "\n with message: " );
            sbLogs.append( e.getMessage(  ) );
            sbLogs.append( "\r\n" );
            AppLogService.error( "Indexing error : " + e.getMessage(  ), e );
        }
        finally
        {
            try
            {
                if ( writer != null )
                {
                    writer.close(  );
                }
            }
            catch ( IOException e )
            {
                AppLogService.error( e.getMessage(  ), e );
            }
        }

        return sbLogs.toString(  );
    }

    /**
     * Return search results
     * @param strQuery The search query
     * @param nStartIndex The start index
     * @return Results as a collection of SarchItem
     */
    public List<DocSearchItem> getSearchResults( String strQuery, int nStartIndex, AdminUser user )
    {
        ArrayList<DocSearchItem> listResults = new ArrayList<DocSearchItem>(  );

        try
        {
            if ( _searcher == null )
            {
                _searcher = new IndexSearcher( _strIndex );
            }

            Query query = null;
            QueryParser parser = new QueryParser( DocSearchItem.FIELD_CONTENTS, _analyzer );
            query = parser.parse( ( strQuery != null ) ? strQuery : "" );

            List<DocumentSpace> listSpaces = DocumentSpacesService.getInstance(  ).getUserAllowedSpaces( user );
            Filter[] filters = new Filter[listSpaces.size(  )];
            int nIndex = 0;

            for ( DocumentSpace space : listSpaces )
            {
                Query querySpace = new TermQuery( new Term( DocSearchItem.FIELD_SPACE, "s" + space.getId(  ) ) );
                filters[nIndex++] = new QueryFilter( querySpace );
            }

            Filter filter = new ChainedFilter( filters, ChainedFilter.OR );

            // Get results documents
            Hits hits = _searcher.search( query, filter );

            Iterator i = hits.iterator(  );

            while ( i.hasNext(  ) )
            {
                Hit hit = (Hit) i.next(  );
                Document document = hit.getDocument(  );
                DocSearchItem si = new DocSearchItem( document );
                listResults.add( si );
            }

            //           _searcher.close();
        }
        catch ( Exception e )
        {
            AppLogService.error( e.getMessage(  ), e );
        }

        return listResults;
    }
}
