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
package fr.paris.lutece.plugins.contact.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for Contact objects
 */
public final class ContactDAO implements IContactDAO
{
    // Constants
    private static final String SQL_QUERY_NEWPK = "SELECT max( id_contact ) FROM contact ";
    private static final String SQL_QUERY_UPDATE_ORDER = "UPDATE contact SET contact_order = ?  WHERE id_contact = ? ";
    private static final String SQL_QUERY_SELECT = "SELECT id_contact, description, email, contact_order FROM contact WHERE id_contact = ? ";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_contact, description, email, contact_order FROM contact ORDER BY contact_order";
    private static final String SQL_QUERY_INSERT = "INSERT INTO contact ( id_contact , description, email , contact_order )  VALUES ( ? , ? , ? , ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM contact WHERE id_contact = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE contact SET description = ? , email = ? , contact_order = ?  WHERE id_contact = ?  ";
    private static final String SQL_QUERY_MODIFY_ORDER = "UPDATE contact SET contact_order = ?  WHERE id_contact = ? ";
    private static final String SQL_QUERY_MODIFY_ORDER_BY_ID = "SELECT id_contact FROM contact  WHERE contact_order = ? ";
    private static final String SQL_QUERY_MAX_ORDER = "SELECT max(contact_order) FROM contact ";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Generates a new primary key
     * @param plugin The plugin
     * @return The new primary key
     */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEWPK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nKey;
    }

    ////////////////////////////////////////////////////////////////////////
    // Methods using a dynamic pool

    /**
     * Insert a new record in the table.
     *
     * @param contact The contact object
     * @param plugin The plugin
     */
    public void insert( Contact contact, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        contact.setId( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, contact.getId(  ) );
        daoUtil.setString( 2, contact.getName(  ) );
        daoUtil.setString( 3, contact.getEmail(  ) );

        contact.setContactOrder( maxOrderContact( plugin ) + 1 );
        daoUtil.setInt( 4, contact.getContactOrder(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of Contact from the table
     *
     * @param nContactId The identifier of Contact
     * @param plugin The plugin
     * @return the instance of the Contact
     * @throws AppException
     */
    public Contact load( int nContactId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nContactId );
        daoUtil.executeQuery(  );

        Contact contact = null;

        if ( daoUtil.next(  ) )
        {
            contact = new Contact(  );
            contact.setId( daoUtil.getInt( 1 ) );
            contact.setName( daoUtil.getString( 2 ) );
            contact.setEmail( daoUtil.getString( 3 ) );
            contact.setContactOrder( daoUtil.getInt( 4 ) );
        }

        daoUtil.free(  );

        return contact;
    }

    /**
     * Delete a record from the table
     * @param contact The Contact object
     * @param plugin The plugin
     */
    public void delete( Contact contact, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, contact.getId(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param contact The reference of contact
     * @param plugin The plugin
     */
    public void store( Contact contact, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nContactId = contact.getId(  );

        daoUtil.setString( 1, contact.getName(  ) );
        daoUtil.setString( 2, contact.getEmail(  ) );
        daoUtil.setInt( 3, contact.getContactOrder(  ) );
        daoUtil.setInt( 4, nContactId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the list of contacts
     *
     * @param plugin The plugin
     * @return The ReferenceList of the Contacts
     */
    public ReferenceList selectContactsList( Plugin plugin )
    {
        ReferenceList contactList = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            contactList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return contactList;
    }

    /**
     * Load the list of contacts
     *
     * @param plugin The plugin
     * @return The Collection of the Contacts
     */
    public Collection<Contact> selectContactList( Plugin plugin )
    {
        Collection<Contact> contactList = new ArrayList<Contact>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Contact contact = new Contact(  );
            contact.setId( daoUtil.getInt( 1 ) );
            contact.setName( daoUtil.getString( 2 ) );
            contact.setEmail( daoUtil.getString( 3 ) );
            contact.setContactOrder( daoUtil.getInt( 4 ) );
            contactList.add( contact );
        }

        daoUtil.free(  );

        return contactList;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Contact Order management

    /**
     * Modify the order of a contact
     * @param nNewOrder The order number
     * @param nId The contact identifier
     * @param plugin The plugin
     */
    public void storeContactOrder( int nNewOrder, int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_ORDER, plugin );
        daoUtil.setInt( 1, nNewOrder );
        daoUtil.setInt( 2, nId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Modify the order of a contact
     *
     * @param nNewOrder The order number
     * @param nId The contact identifier
     * @param plugin The plugin
     */
    public void updateContactOrder( int nNewOrder, int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_MODIFY_ORDER, plugin );
        daoUtil.setInt( 1, nNewOrder );
        daoUtil.setInt( 2, nId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Returns a contact identifier in a distinct order
     *
     * @param nContactOrder The order number
     * @param plugin The plugin
     * @return The order of the Contact
     */
    public int selectIdByOrder( int nContactOrder, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_MODIFY_ORDER_BY_ID, plugin );
        int nResult = nContactOrder;
        daoUtil.setInt( 1, nContactOrder );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            // If number order doesn't exist
            nResult = 1;
        }
        else
        {
            nResult = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nResult;
    }

    /**
     * Calculate the new max order
     * @param plugin The plugin
     * @return the max order of contact
     */
    public int maxOrderContact( Plugin plugin )
    {
        int nOrder = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_MAX_ORDER, plugin );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nOrder = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nOrder;
    }
}
