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
package fr.paris.lutece.plugins.comarquage.util.cache.genericimpl;

import fr.paris.lutece.plugins.comarquage.util.cache.IChainManager;
import fr.paris.lutece.plugins.comarquage.util.cache.IChainNode;
import fr.paris.lutece.plugins.comarquage.util.cache.IContextChainManager;

import java.io.Serializable;


/**
 * The default context cache filter associated with {@link ContextChainManagerImpl}
 *
 * @see fr.paris.lutece.plugins.comarquage.util.cache.genericimpl.ChainManagerImpl
 */
public class ContextChainManagerImpl implements IContextChainManager
{
    private final IChainManager _filterManager;
    private final String _strFilterName;
    private final String _strNextFilterName;
    private final Serializable _initialKey;

    /**
     * Public constructor
     * @param filterManager the filter manager
     * @param strFilterName the filter name
     * @param strNextFilter the next filter in the chain
     * @param initialKey the innitial key
     */
    public ContextChainManagerImpl( IChainManager filterManager, String strFilterName, String strNextFilter,
        Serializable initialKey )
    {
        _filterManager = filterManager;
        _strFilterName = strFilterName;
        _strNextFilterName = strNextFilter;
        _initialKey = initialKey;
    }

    /**
     * @see IChainManager#init(String)
     */
    public void init( String strBase )
    {
        // Nothing todo...
    }

    /**
     * @see IChainManager#callFilter(String, Serializable)
     */
    public Object callFilter( String strInstanceName, Serializable key )
    {
        return _filterManager.callFilter( strInstanceName, key );
    }

    /**
     * @see IChainManager#getFilter(String)
     */
    public IChainNode getFilter( String strInstanceName )
    {
        return _filterManager.getFilter( strInstanceName );
    }

    /**
     * @see IContextChainManager#callNextFilter(Serializable, Object)
     */
    public Object callNextFilter( Serializable key, Object element )
    {
        if ( _strNextFilterName != null )
        {
            return _filterManager.callFilter( _strNextFilterName, key );
        }

        return null;
    }

    /**
     * @see fr.paris.lutece.plugins.comarquage.util.cache.IChainManager#getPluginName()
     */
    public String getPluginName(  )
    {
        return _filterManager.getPluginName(  );
    }

    /**
     * @see fr.paris.lutece.plugins.comarquage.util.cache.IContextChainManager#getCurrentFilterName()
     */
    public String getCurrentFilterName(  )
    {
        return _strFilterName;
    }

    /**
     * @see fr.paris.lutece.plugins.comarquage.util.cache.IContextChainManager#getNextFilterName()
     */
    public String getNextFilterName(  )
    {
        return _strNextFilterName;
    }

    /**
     * @see fr.paris.lutece.plugins.comarquage.util.cache.IContextChainManager#getIntialKey()
     */
    public Serializable getIntialKey(  )
    {
        return _initialKey;
    }

    /**
     * @see fr.paris.lutece.plugins.comarquage.util.cache.IChainManager#emptyCaches()
     */
    public void emptyCaches(  )
    {
        _filterManager.emptyCaches(  );
    }

    /**
     * @see fr.paris.lutece.plugins.comarquage.util.cache.IChainManager#getCachesSize()
     */
    public int getCachesSize(  )
    {
        return _filterManager.getCachesSize(  );
    }
}
