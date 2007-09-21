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
package fr.paris.lutece.plugins.comarquage.util;

import fr.paris.lutece.plugins.comarquage.util.cache.IChainManager;
import fr.paris.lutece.plugins.comarquage.util.cache.genericimpl.ChainManagerImpl;
import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppException;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;


/**
 * CoMarquage utilitary access class
 */
public final class CoMarquageUtils
{
    private static final String XML_CONFIG_PARAM_TARGET_LINK_SERVICE_ID = "targetLinkServiceId";
    private static final String PROPERTY_RESPONSIBILITY_CHAIN_FRAGMENT = ".respchain";
    private static final String PARAMETER_ID = "id";
    private static final String MASK_PUBLIC_CARD = "/F";
    private static final Map<String, IChainManager> _mapPluginByPluginName = new HashMap<String, IChainManager>(  );
    private static final Map<String, String> _mapPluginNameByLinkServiceId = new HashMap<String, String>(  );

    /**
     * Hidden constructor
     *
     */
    private CoMarquageUtils(  )
    {
        // nothing here
    }

    /**
     * Return id with one Card max and without double
     * @param request the request
     * @return the prepared id
     */
    public static synchronized String getId( HttpServletRequest request ) //TODO : getId( ) synchronized ?
    {
        String strId = request.getParameter( PARAMETER_ID );

        if ( null != strId )
        {
            final StringTokenizer st = new StringTokenizer( strId, "/" );

            while ( st.hasMoreTokens(  ) )
            {
                final String e = (String) st.nextToken(  );

                if ( strId.indexOf( e ) != strId.lastIndexOf( e ) )
                {
                    final String s1 = strId.substring( 0, strId.indexOf( e ) );
                    String s2 = strId.substring( strId.indexOf( e ) );
                    s2 = s2.substring( s2.indexOf( "/" ) + 1 );
                    strId = s1 + s2;
                }
            }

            final int i = strId.lastIndexOf( MASK_PUBLIC_CARD );

            if ( -1 != i )
            {
                strId = strId.substring( 0, strId.indexOf( MASK_PUBLIC_CARD ) ) + strId.substring( i );
            }
        }

        return strId;
    }

    /**
     * Call a chain manager by the plugin name and entry point.<br>
     * Find element referenced by key <code>key</code>
     *
     * @param strPluginName The name of the plugin (for retrieve configuration)
     * @param strFirstModuleInstanceName The name of the entry node
     * @param key Key reference for the object to search
     * @return The string fragment associate with this chain
     */
    public static String callChainManagerByPluginName( String strPluginName, String strFirstModuleInstanceName,
        Serializable key )
    {
        IChainManager chainManager = getChainManagerByPluginName( strPluginName );

        String strContents;

        try
        {
            strContents = (String) chainManager.callFilter( strFirstModuleInstanceName, key );
        }
        catch ( AppException e )
        {
            return null;
        }

        return strContents;
    }

    /**
     * Get chain manager by the plugin name.<br>
     *
     * @param strPluginName The name of the plugin (for retrieve configuration)
     * @return The chain manager associate with this plugin
     */
    public static synchronized IChainManager getChainManagerByPluginName( String strPluginName )
    {
        IChainManager chainManager = (IChainManager) _mapPluginByPluginName.get( strPluginName );

        if ( chainManager == null )
        {
            chainManager = new ChainManagerImpl( strPluginName );
            chainManager.init( strPluginName + PROPERTY_RESPONSIBILITY_CHAIN_FRAGMENT );

            // Register it in caches
            Plugin plugin = PluginService.getPlugin( strPluginName );
            String strLinkServiceId = plugin.getParamValue( XML_CONFIG_PARAM_TARGET_LINK_SERVICE_ID );
            _mapPluginByPluginName.put( strPluginName, chainManager );
            _mapPluginNameByLinkServiceId.put( strLinkServiceId, strPluginName );
        }

        return chainManager;
    }

    /**
     * Help method for retrieve the plugin associate to a content service.<br>
     *
     * In future, contentService may be modified to reference it's plugin owner.
     *
     * @param strContentServiceName name of content service
     * @return The collection of plugin names who declare this content service
     */
    /*public static synchronized Collection<String> getPluginNamesByContentServiceName( String strContentServiceName )
    {
        Collection<String> colPluginNames = new ArrayList<String>(  );

        // We must find which plugin define this contentService
        Collection<Plugin> pluginsList = PluginService.getPluginList(  );

        for ( Plugin plugin : pluginsList )
        {
            ContentService contentService = plugin.getContentService(  );

            if ( contentService != null )
            {
                String strServiceName =  contentService.getName(  );

                if ( strContentServiceName.equals( strServiceName ) )
                {
                    colPluginNames.add( plugin.getName(  ) );

                    break;
                }
            }
        }

        return colPluginNames;
    }*/
}
