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
package fr.paris.lutece.plugins.dbpage.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;


/**
 *
 * @author lenaini
 */
public interface IDbPageDatabaseTypeDAO
{
    /**
     * Delete a record from the table
     * @param nDbPageDatabaseTypeId The DbPageDatabaseType Id
     */
    void delete( int nDbPageDatabaseTypeId, Plugin plugin );

    /**
     * Insert a new record in the table.
     * @param dbPageDatabaseType The dbPageDatabaseType object
     */
    void insert( DbPageDatabaseType dbPageDatabaseType, Plugin plugin );

    /**
     * Load the data of DbPageDatabaseType from the table
     * @param nDbPageDatabaseTypeId The identifier of DbPageDatabaseType
     * @param plugin The Plugin Object
     * @return the instance of the DbPageDatabaseType
     */
    DbPageDatabaseType load( int nDbPageDatabaseTypeId, Plugin plugin );

    /**
     * Load a database type list
     * @param plugin The Plugin Object
     * @return the type list
     */
    List<DbPageDatabaseType> selectDbPageDatabaseTypeList( Plugin plugin );

    /**
     * Load the list of types
     * @return The ReferenceList of the Types
     */
    ReferenceList selectTypesList( Plugin plugin );

    /**
     * Update the record in the table
     * @param dbPageDatabaseType The reference of dbPageDatabaseType
     */
    void store( DbPageDatabaseType dbPageDatabaseType, Plugin plugin );
}
