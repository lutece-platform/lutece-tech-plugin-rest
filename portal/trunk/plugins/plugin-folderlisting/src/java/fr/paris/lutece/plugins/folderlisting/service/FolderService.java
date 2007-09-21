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
package fr.paris.lutece.plugins.folderlisting.service;

import fr.paris.lutece.portal.service.resource.Resource;
import fr.paris.lutece.portal.service.resource.ResourceService;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.List;


public class FolderService extends ResourceService
{
    ////////////////////////////////////////////////////////////////////////////////
    // Constants
    private static FolderService _singleton = new FolderService(  );
    private static final String PROPERTY_NAME = "folderlisting.service.name";
    private static final String PROPERTY_CACHE = "folderlisting.service.cache";
    private static final String PROPERTY_LOADERS = "folderlisting.service.loaders";

    /**
     * Private constructor
     */
    private FolderService(  )
    {
        super(  );
        setCacheKey( PROPERTY_CACHE );
        setNameKey( PROPERTY_NAME );
    }

    /**
     * Returns the instance of the singleton
     *
     * @return The instance of the singleton
     */
    public static FolderService getInstance(  )
    {
        return _singleton;
    }

    /**
     * Returns the Folder
     *
     * @param strFolderName The name of the folder
     *
     * @return the Folder
     */
    public Folder getFolder( String strFolderName )
    {
        return (Folder) getResource( strFolderName );
    }

    /**
     * Returns a list of all folders
     *
     * @return The list of all folders
     */
    public ReferenceList getFoldersList(  )
    {
        ReferenceList list = new ReferenceList(  );

        for ( Resource resource : getResources(  ) )
        {
            Folder folder = (Folder) resource;
            list.addItem( folder.getId(  ), folder.getName(  ) );
        }

        return list;
    }

    /**
     * Returns a Collection of all folders
     *
     * @return The list of all folders
     */
    public List<Folder> getFoldersCollection(  )
    {
        List<Folder> list = new ArrayList<Folder>(  );

        for ( Resource resource : getResources(  ) )
        {
            Folder folder = (Folder) resource;
            list.add( folder );
        }

        return list;
    }

    /**
     * Returns the property key that contains the loaders list
     *
     * @return A property key
     */
    protected String getLoadersProperty(  )
    {
        return PROPERTY_LOADERS;
    }
}
