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
package fr.paris.lutece.plugins.myapps.business.portlet;

import fr.paris.lutece.portal.business.portlet.IPortletInterfaceDAO;
import fr.paris.lutece.portal.business.portlet.Portlet;


/**
 * this class provides Data Access methods for HtmlPortlet objects
 */
public final class MyAppsPortletDAO implements IMyAppsPortletDAO
{
    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Insert a new record in the table.
     * @param portlet the object to be inserted
     * @throws AppException
     */
    public void insert( Portlet portlet )
    {
        // Nothing to insert into a specific table
    }

    /**
     * Delete a record from the table
     * @param nPortletId the Html Portlet identifier
     */
    public void delete( int nPortletId )
    {
        // Nothing to delete from a specific table
    }

    /**
     * loads the data of portlet from the table
     * @param nPortletId The Html Portlet identifier
     * @return the Html Portlet object
     */
    public Portlet load( int nPortletId )
    {
        // Nothing to load from a specific table
        MyAppsPortlet portlet = new MyAppsPortlet(  );

        return portlet;
    }

    /**
     * Update the record in the table
     * @param portlet the instance of Portlet class to be updated
     */
    public void store( Portlet portlet )
    {
        // Nothing is stored in a specific table
    }
}
