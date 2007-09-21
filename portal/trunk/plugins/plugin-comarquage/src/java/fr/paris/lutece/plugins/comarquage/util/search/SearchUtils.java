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
package fr.paris.lutece.plugins.comarquage.util.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;

import java.io.IOException;


/**
 * Help utils for search work
 */
public final class SearchUtils
{
    public static final String LUCENE_ATTRIBUT_CONTENTS = "contents";

    // list of words not to take in account
    private static final String[] ARRAY_STOP_WORDS = 
        {
            "des", "de", "du", "d", "les", "le", "la", "l", "pas", "n", "ses", "dans", "comment", "se", "sa", "s", "et",
            "un", "en", "on", "une", "est", "sur", "quels", "quelles", "où", "sont", "il", "elle", "elles", "leur",
            "leurs", "a", "au", "?",
        };

    /**
     * Hidden constructors
     *
     */
    private SearchUtils(  )
    {
        // nothing to do
    }

    /**
     * Searches the card
     * @param strPluginName the plugin name
     * @param strIndexPath the path
     * @param strQuery the query
     * @return the hits
     * @throws IOException if the index reader can not open the index
     * @throws ParseException if an error occured during the parsing of the query
     */
    public static Hits search( String strPluginName, String strIndexPath, String strQuery )
        throws IOException, ParseException
    {
        IndexReader reader = IndexReader.open( strIndexPath );

        Searcher searcher = new IndexSearcher( reader );

        //ancien	Analyzer analyzer = getAnalyzer(  );
        Analyzer _analyzer = getAnalyzer(  );

        //ancien	Query query;
        Query query = null;

        try
        {
            //ancien	query = QueryParser.parse( strQuery, LUCENE_ATTRIBUT_CONTENTS, analyzer );
            QueryParser parser = new QueryParser( LUCENE_ATTRIBUT_CONTENTS, _analyzer );
            query = parser.parse( ( strQuery != null ) ? strQuery : "" );

            // Get results documents
            return searcher.search( query );
        }
        catch ( Throwable e )
        {
            // Error in user query expression
            return null;
        }
    }

    /**
     * Returns an analyser
     * @return the analyser
     */
    static Analyzer getAnalyzer(  )
    {
        return new StopAnalyzer( ARRAY_STOP_WORDS );
    }
}
