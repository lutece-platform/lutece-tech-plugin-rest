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
package fr.paris.lutece.plugins.document.service.publishing;

import fr.paris.lutece.plugins.document.business.portlet.DocumentListPortletHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;


/**
 * Publishing service
 */
public class PublishingService
{
    private static PublishingService _singleton = new PublishingService(  );
    private static PublishingEventListenersManager _manager;

    /** Creates a new instance of PublishingService */
    private PublishingService(  )
    {
        _manager = (PublishingEventListenersManager) SpringContextService.getPluginBean( "document",
                "publishingEventListenersManager" );
    }

    /**
     * Get the unique instance of the service
     * @return The unique instance
     */
    public static PublishingService getInstance(  )
    {
        return _singleton;
    }

    public void publish( int nDocumentId, int nPortletId )
    {
        // Publishing of document : set status to 0
        DocumentListPortletHome.publishingDocument( nDocumentId, nPortletId, 0 );

        PublishingEvent event = new PublishingEvent( nDocumentId, nPortletId, PublishingEvent.DOCUMENT_PUBLISHED );

        _manager.notifyListeners( event );
    }

    public void unpublish( int nDocumentId, int nPortletId )
    {
        // Publishing of document : set status to 1
        DocumentListPortletHome.unPublishingDocument( nDocumentId, nPortletId, 1 );

        PublishingEvent event = new PublishingEvent( nDocumentId, nPortletId, PublishingEvent.DOCUMENT_UNPUBLISHED );
        _manager.notifyListeners( event );
    }
}
