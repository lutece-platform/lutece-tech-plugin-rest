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

import com.sun.org.apache.xalan.internal.extensions.ExpressionContext;

import fr.paris.lutece.portal.service.util.AppLogService;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

import java.util.StringTokenizer;


/**
 * Give extended functions to help to construct an index by XSL parsing<br/>
 *
 * Functions must be declared in XSL source file with (it's an example):<br>
 *
 * <code>
        &lt;xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"

            xmlns:axslt="http://xml.apache.org/xslt"
            xmlns:lucene="http://www.lutece.fr/plugins/comarquage/lucene"
            extension-element-prefixes="lucene"
        &gt;

        &lt;axslt:component prefix="lucene"
                 functions="startIndexer,startDocument,addText,endDocument,endIndexer"&gt;
                &lt;axslt:script lang="javaclass"
                      src="fr.paris.lutece.plugins.comarquage.util.Test"/&gt;
        &lt;/axslt:component&gt;
        </code>
 * <br/>
 * And must be used like:<br/>
 * <code>
        &lt;xsl:value-of select="lucene:startIndexer('indexDir', true())"/&gt;
 * </code>
 */
public class XslXalanExtension
{
    private IndexWriter _writer;
    private Document _currentDocument;

    /**
     * Public constructor
     * Reflection
     */
    public XslXalanExtension(  )
    {
        // nothing to do
    }

    /**
     * Start Lucene indexer
     *
     * @param context The node context of Xalan execution
     * @param strPath The path to directory index
     * @param bCreate The directory must be create?
     * @return Information about success of call (<code>true</code> means "All is ok")
     */
    public Boolean startIndexer( ExpressionContext context, String strPath, Boolean bCreate )
    {
        try
        {
            final Analyzer analyzer;
            analyzer = SearchUtils.getAnalyzer(  );
            _writer = new IndexWriter( strPath, analyzer, bCreate.booleanValue(  ) );

            return Boolean.TRUE;
        }
        catch ( IOException e )
        {
            AppLogService.error( e.getMessage(  ), e );

            return Boolean.FALSE;
        }
    }

    /**
     * Optimize the index directory
     *
     * @param context The node context of Xalan execution
     * @return Information about success of call (<code>true</code> means "All is ok")
     */
    public Boolean optimize( ExpressionContext context )
    {
        try
        {
            _writer.optimize(  );

            return Boolean.TRUE;
        }
        catch ( IOException e )
        {
            AppLogService.error( "Lucene/Xalan error, can't optimize: " + e.getMessage(  ), e );

            return Boolean.FALSE;
        }
    }

    /**
     * Stop (close) the indexer
     *
     * @param context The node context of Xalan execution
     * @return Information about success of call (<code>true</code> means "All is ok")
     */
    public Boolean endIndexer( ExpressionContext context )
    {
        try
        {
            _writer.close(  );
            _writer = null;

            return Boolean.TRUE;
        }
        catch ( IOException e )
        {
            AppLogService.error( "Lucene/Xalan error, can't close: " + e.getMessage(  ), e );

            return Boolean.FALSE;
        }
    }

    /**
     * Start a new Lucene document
     *
     * @param context The node context of Xalan execution
     * @return Information about success of call (<code>true</code> means "All is ok")
     */
    public Boolean startDocument( ExpressionContext context )
    {
        _currentDocument = new Document(  );

        return Boolean.TRUE;
    }

    /**
     * Add a new text attribute to the document (a text attribute is
     * store, indexed and tokenized)
     *
     * @param context The node context of Xalan execution
     * @param strKey Lucene attribute name
     * @param strValue Text value
     * @return Information about success of call (<code>true</code> means "All is ok")
     */
    public Boolean addText( ExpressionContext context, String strKey, String strValue )
    {
        //ancien	_currentDocument.add( Field.Text( strKey,  strValue) );
        _currentDocument.add( new Field( strKey, strValue, Field.Store.YES, Field.Index.UN_TOKENIZED ) );

        return Boolean.TRUE;
    }

    /**
     * Add a new keyword attribute to the document (a keyword attribute is
     * store, indexed and not tokenized)
     *
     * @param context The node context of Xalan execution
     * @param strKey Lucene attribute name
     * @param strValue Keyword value
     * @return Information about success of call (<code>true</code> means "All is ok")
     */
    public Boolean addKeyword( ExpressionContext context, String strKey, String strValue )
    {
        //ancien	_currentDocument.add( Field.Keyword( strKey, strValue ) );
        _currentDocument.add( new Field( strKey, strValue, Field.Store.YES, Field.Index.TOKENIZED ) );

        return Boolean.TRUE;
    }

    /**
     * Add a new keywords attributes to the document (a keyword attribute is
     * store, indexed and not tokenized)
     *
     * @param context The node context of Xalan execution
     * @param strKey Lucene attribute name
     * @param strValues Keywords value (separate by <code>separators</code> char)
     * @param separators The separators of key words (for use with {@link StringTokenizer})
     * @return Information about success of call (<code>true</code> means "All is ok")
     */
    public Boolean addKeywords( ExpressionContext context, String strKey, String strValues, String separators )
    {
        StringTokenizer tokenizer = new StringTokenizer( strValues, separators );

        while ( tokenizer.hasMoreTokens(  ) )
        {
            String strNextToken = tokenizer.nextToken(  ).trim(  );

            if ( strNextToken.length(  ) > 0 )
            {
                //ancien	_currentDocument.add( Field.Keyword( strKey, strNextToken ) );
                _currentDocument.add( new Field( strKey, strNextToken, Field.Store.YES, Field.Index.TOKENIZED ) );
            }
        }

        return Boolean.TRUE;
    }

    /**
     * Add a new undexed attribute to the document (a undexed attribute is
     * store, no indexed and tokenized)
     *
     * @param context The node context of Xalan execution
     * @param strKey Lucene attribute name
     * @param strValue Text value
     * @return Information about success of call (<code>true</code> means "All is ok")
     */
    public Boolean addUnIndexed( ExpressionContext context, String strKey, String strValue )
    {
        //ancien	_currentDocument.add( Field.UnIndexed( strKey, strValue ) );
        _currentDocument.add( new Field( strKey, strValue, Field.Store.YES, Field.Index.NO ) );

        return Boolean.TRUE;
    }

    /**
     * Add a new unstored (indexed) attribute to the document (a unstored attribute is
     * no stored, indexed and tokenized)
     *
     * @param context The node context of Xalan execution
     * @param strKey Lucene attribute name
     * @param strValue Text value
     * @return Information about success of call (<code>true</code> means "All is ok")
     */
    public Boolean addUnStored( ExpressionContext context, String strKey, String strValue )
    {
        //ancien	_currentDocument.add( Field.UnStored( strKey, strValue ) );
        _currentDocument.add( new Field( strKey, strValue, Field.Store.NO, Field.Index.NO_NORMS ) );

        return Boolean.TRUE;
    }

    /**
     * Add a personnalized attribute
     *
     * @param context The node context of Xalan execution
     * @param strKey Lucene attribute name
     * @param strValue Text value
     * @param bStore Indicate if the text value must be stored
     * @param bIndex Indicate if the text value must be indexed
     * @param bToken Indicate if the text value must be tokenized
     * @return Information about success of call (<code>true</code> means "All is ok")
     */

    /*ancien la nouvelle version de lucene entraine une surcharge de test if sur des boolean dans cette methode... pas très jolie.
     * public Boolean add( ExpressionContext context, String strKey, String strValue, Boolean bStore, Boolean bIndex,
        Boolean bToken )
    {
        _currentDocument.add( new Field( strKey, strValue, bStore.booleanValue(  ), bIndex.booleanValue(  ),
                bToken.booleanValue(  ) ) );
    
        return Boolean.TRUE;
    }*/

    /**
     * Add a document to index
     *
     * @param context The node context of Xalan execution
     * @return Information about success of call (<code>true</code> means "All is ok")
     */
    public Boolean endDocument( ExpressionContext context )
    {
        try
        {
            _writer.addDocument( _currentDocument );
            _currentDocument = null;

            return Boolean.TRUE;
        }
        catch ( IOException e )
        {
            AppLogService.error( "Lucene/Xalan error, can't append the document to indexer: " + e.getMessage(  ), e );

            return Boolean.FALSE;
        }
    }
}
