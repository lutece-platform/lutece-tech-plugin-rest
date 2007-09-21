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
package fr.paris.lutece.plugins.myapps.service;

import fr.paris.lutece.plugins.myapps.business.MyAppsHome;
import fr.paris.lutece.portal.service.image.ImageResource;
import fr.paris.lutece.portal.service.image.ImageResourceManager;
import fr.paris.lutece.portal.service.image.ImageResourceProvider;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;


/**
 * This class delivers the images needed by myapps application
 */
public class MyAppsService implements ImageResourceProvider
{
    ////////////////////////////////////////////////////////////////////////////
    // Variables
    public static final String TEMPLATE_PAGE_ACCESS_DENIED = "/skin/site/page_access_denied.html";
    private static final String IMAGE_RESOURCE_TYPE_ID = "myapps_icon";
    private static MyAppsService _singleton = null;
    private static Plugin _plugin = null;

    /**
     * Creates a new PageService object.
     */
    public MyAppsService(  )
    {
        super(  );
        init(  );
        _singleton = this;
    }

    private void init(  )
    {
        _plugin = PluginService.getPlugin( "myapps" );
        ImageResourceManager.registerProvider( this );
    }

    public static synchronized MyAppsService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new MyAppsService(  );
        }

        return _singleton;
    }

    /**
     * Returns the resource type Id
     * @return The resource type Id
     */
    public String getResourceTypeId(  )
    {
        return IMAGE_RESOURCE_TYPE_ID;
    }

    /**
     * @param nIdResource The Resource identifier
     */
    public ImageResource getImageResource( int nMyAppsId )
    {
        return MyAppsHome.getImageResource( nMyAppsId, _plugin );
    }
}
