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
package fr.paris.lutece.plugins.ods.service.search.indexer;


/**
 * Objet Indexer
 */
public class Indexer
{
    private int _nIdIndexer = -1;
    private String _strNomIndexer;
    private boolean _bIndexationComplete;

    /**
     * Retourne l'id de l'indexer
     * @return l'id de l'indexer
     */
    public int getIdIndexer(  )
    {
        return _nIdIndexer;
    }

    /**
     * Fixe l'id de l'indexer
     * @param nIdIndexer the _nIdIndexer to set
     */
    public void setIdIndexer( int nIdIndexer )
    {
        _nIdIndexer = nIdIndexer;
    }

    /**
     * Retourne le nom de l'indexer
     * @return le nom de l'indexer
     */
    public String getNomIndexer(  )
    {
        return _strNomIndexer;
    }

    /**
     * Fixe le nom de l'indexer
     * @param strNomIndexer le nom de l'indexer
     */
    public void setNomIndexer( String strNomIndexer )
    {
        _strNomIndexer = strNomIndexer;
    }

    /**
     * Indique si l'indexation complète a été demandée
     * @return <b>true</b> si l'indexation complète est demandée, <b>false</b> sinon
     */
    public boolean isIndexationComplete(  )
    {
        return _bIndexationComplete;
    }

    /**
     * Fixer à <b>true</b> pour demander l'indexation complète
     * @param bIndexationComplete <b>true</b> si on veut lancer l'indexation complète au prochain tour du daemon.
     */
    public void setIndexationComplete( boolean bIndexationComplete )
    {
        _bIndexationComplete = bIndexationComplete;
    }
}
