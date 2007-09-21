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
package fr.paris.lutece.plugins.library.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;


public class MediaAttributeHome
{
    //  Static variable pointed at the DAO instance
    private static IMediaAttributeDAO _dao = (IMediaAttributeDAO) SpringContextService.getPluginBean( "library",
            "mediaAttributeDAO" );

    private MediaAttributeHome(  )
    {
    }

    public static Collection<MediaAttribute> findAllAttributesForMedia( int nIdMedia, Plugin plugin )
    {
        return _dao.selectAllAttributesForMedia( nIdMedia, plugin );
    }

    public static MediaAttribute findByPrimaryKey( int nAttributeId, Plugin plugin )
    {
        return _dao.load( nAttributeId, plugin );
    }

    public static void create( MediaAttribute attribute, Plugin plugin )
    {
        _dao.insert( attribute, plugin );
    }

    public static void update( MediaAttribute attribute, Plugin plugin )
    {
        _dao.store( attribute, plugin );
    }

    public static void remove( int nAttributeId, Plugin plugin )
    {
        _dao.delete( nAttributeId, plugin );
    }

    public static void removeAllForMedia( int nMediaId, Plugin plugin )
    {
        _dao.deleteAllForMedia( nMediaId, plugin );
    }
}
