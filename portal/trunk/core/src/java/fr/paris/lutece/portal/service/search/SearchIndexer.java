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
package fr.paris.lutece.portal.service.search;

import fr.paris.lutece.portal.service.message.SiteMessageException;

import org.apache.lucene.document.Document;

import java.io.IOException;

import java.util.List;


/**
 * Interface of pluggable indexer. An Indexer can add documents to the main Lucene index.
 */
public interface SearchIndexer
{
    /**
     * Returns a List of lucene documents to add to the index
     * @return A List of lucene documents to add to the index
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    public List<Document> getDocuments(  ) throws IOException, InterruptedException, SiteMessageException;

    /**
     * Returns the indexer service name
     * @return the indexer service name
     */
    public String getName(  );

    /**
     * Returns the indexer service version
     * @return the indexer service version
     */
    public String getVersion(  );

    /**
     * Returns the indexer service description
     * @return the indexer service description
     */
    public String getDescription(  );

    /**
     * Tells whether the service is enable or not
     * @return true if enable, otherwise false
     */
    public boolean isEnable(  );
}
