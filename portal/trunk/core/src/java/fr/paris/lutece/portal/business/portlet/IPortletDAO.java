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
package fr.paris.lutece.portal.business.portlet;

import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;
import java.util.List;


/**
 * IPortletDAO Interface
 *
 */
public interface IPortletDAO
{
    /**
     * Insert a new record in the table. NB : The portlet identifier will already been obtained by the portlet girl
     *
     * @param portlet the portlet to insert in the database
     */
    public abstract void insert( Portlet portlet );

    /**
     * Delete a record from the table and its alias
     *
     * @param nPortletId the identifier of the portlet to be deleted
     */
    public abstract void delete( int nPortletId );

    /**
     * Load the data of a portlet from the database
     *
     * @param nPortletId the portlet identifier
     * @return the object Portlet initialized with the data of the database
     */
    public abstract Portlet load( int nPortletId );

    /**
     * Update the record in the table
     *
     * @param portlet the portlet reference
     */
    public abstract void store( Portlet portlet );

    /**
     * Returns a new primary key which will be used to add a new portlet
     *
     * @return The new key.
     */
    public abstract int newPrimaryKey(  );

    /**
     * Update the portlet status : 0 for activated - 1 for suspended
     *
     * @param portlet the portlet to upadte in the database
     * @param nStatus the status to update
     */
    public abstract void updateStatus( Portlet portlet, int nStatus );

    /**
     * Returns the stylesheet of the portlet according to the mode
     *
     * @param nPortletId the identifier of the portlet
     * @param nIdMode the selected mode
     * @return the stylesheet
     */
    public abstract StyleSheet selectXslFile( int nPortletId, int nIdMode );

    /**
     * Returns the list of portlets in a distinct name
     *
     * @param strPortletName the name of portlet
     * @return the list in form of Collection
     * @throws AppException
     */
    public abstract Collection selectPortletsListbyName( String strPortletName );

    /**
     * Returns a list of portlets according to the selected type
     *
     * @param strPortletTypeId the portlet type
     * @return the portlets in form of Collection
     */
    public abstract List<Portlet> selectPortletsByType( String strPortletTypeId );

    /**
     * Returns all the styles corresponding to a portlet type
     *
     * @param strPortletTypeId the identifier of the portlet type
     * @return the list of styles in form of ReferenceList
     */
    public abstract ReferenceList selectStylesList( String strPortletTypeId );

    /**
     * Indicates if the portlet has alias in the database or not.
     *
     * @param nPortletId the identifier of the portlet
     * @return true if the portlet has some alias, false if not.
     */
    public abstract boolean hasAlias( int nPortletId );

    /**
     * Returns the instance of the PortletType whose identifier is specified in parameter
     *
     * @param strPortletTypeId the identifier of the portlet type
     * @return the instance of the portlet type
     */
    public abstract PortletType selectPortletType( String strPortletTypeId );

    /**
     * Returns the list of the portlets associated to the style
     * @param nStyleId the identifier of the style
     * @return the list of the portlets in form of a Collection of Portlets objects
     */
    public abstract Collection selectPortletListByStyle( int nStyleId );
}
