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

import fr.paris.lutece.plugins.comarquage.util.cache.IContextChainManager;

import java.io.Serializable;


/**
 * Common abstract accessor (get only, no store)<br/>
 *
 * <b>No properties read here.</b>
 */
public abstract class AbstractAccessor extends AbstractChainNode
{
    /**
     * Public constructor
     * @param strImplementationName the name of the implementation class
     * @param strImplementationDescription the description of the implementation
     */
    public AbstractAccessor( String strImplementationName, String strImplementationDescription )
    {
        super( strImplementationName, strImplementationDescription );
    }

    /**
     * @see fr.paris.lutece.plugins.comarquage.util.cache.IChainNode#init(String)
     */
    public void init( String strBase )
    {
        super.init( strBase );

        // Nothing to do
    }

    /**
     * @see fr.paris.lutece.plugins.comarquage.util.cache.IChainNode#getObject(IContextChainManager, Serializable, Object)
     */
    public final Object getObject( IContextChainManager filterManager, Serializable key, Object element )
    {
        Object elt = element;

        if ( elt != null )
        {
            // Element already finded... nothing to do, and an error in the manager?
            return elt;
        }

        elt = doSearch( filterManager, key );

        if ( elt == null )
        {
            elt = filterManager.callNextFilter( key, elt );
        }

        return elt;
    }

    /**
     * Realise the search of the object
     *
     * @param filterManager The context filter/cache manager, used to call next filter or ask parameters
     * @param key the key of the element
     * @return the element
     */
    public abstract Object doSearch( IContextChainManager filterManager, Serializable key );
}
