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
package fr.paris.lutece.portal.service.mailinglist;

import fr.paris.lutece.portal.business.mailinglist.MailingList;
import fr.paris.lutece.portal.business.mailinglist.MailingListHome;
import fr.paris.lutece.portal.business.mailinglist.MailingListUsersFilter;
import fr.paris.lutece.portal.business.mailinglist.Recipient;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.business.workgroup.AdminWorkgroupHome;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import java.util.ArrayList;
import java.util.Collection;


/**
 * AdminMailingListService
 */
public class AdminMailingListService
{
    public static final String ALL_ROLES = "*";

    /**
     * Returns a list of all mailing list visible by the user
     * @param user The user
     * @return The list as a ReferenceList
     */
    public static ReferenceList getMailingLists( AdminUser user )
    {
        ReferenceList list = new ReferenceList(  );

        for ( MailingList mailinglist : getUserMailingLists( user ) )
        {
            list.addItem( mailinglist.getId(  ), mailinglist.getName(  ) );
        }

        return list;
    }

    /**
     * Returns a list of all mailing list visible by the user
     * @param user The user
     * @return The list as a mailinglist Collection
     */
    public static Collection<MailingList> getUserMailingLists( AdminUser user )
    {
        Collection<MailingList> listMailinglists = new ArrayList<MailingList>(  );

        // Add all global mailing lists
        listMailinglists.addAll( MailingListHome.findByWorkgroup( AdminWorkgroupService.ALL_GROUPS ) );

        // Add mailing list of the user's workgroups
        ReferenceList listWorkgroups = AdminWorkgroupHome.getUserWorkgroups( user );

        for ( ReferenceItem workgroup : listWorkgroups )
        {
            listMailinglists.addAll( MailingListHome.findByWorkgroup( workgroup.getCode(  ) ) );
        }

        return listMailinglists;
    }

    /**
     * Returns all the recipient of a given mailing list
     * @param nIdMailingList The mailing list Id
     * @return The list
     */
    public static Collection<Recipient> getRecipients( int nIdMailingList )
    {
        Collection<Recipient> listRecipients = new ArrayList<Recipient>(  );
        MailingList mailinglist = MailingListHome.findByPrimaryKey( nIdMailingList );

        for ( MailingListUsersFilter filter : mailinglist.getFilters(  ) )
        {
            listRecipients.addAll( getRecipients( filter.getWorkgroup(  ), filter.getRole(  ) ) );
        }

        return listRecipients;
    }

    /**
     * Gets all recipients corresponding to a filter based on a Workgroup and a role
     * @param strWorkgroup The workgroup
     * @param strRole The role
     * @return A collection of recipient
     */
    public static Collection<Recipient> getRecipients( String strWorkgroup, String strRole )
    {
        Collection<Recipient> listRecipients = new ArrayList<Recipient>(  );
        Collection<AdminUser> listUsers;

        if ( ( strWorkgroup != null ) && ( !strWorkgroup.equals( AdminWorkgroupService.ALL_GROUPS ) ) )
        {
            listUsers = AdminWorkgroupHome.getUserListForWorkgroup( strWorkgroup );
        }
        else
        {
            listUsers = AdminUserHome.findUserList(  );
        }

        for ( AdminUser user : listUsers )
        {
            if ( ( strRole != null ) && ( !strRole.equals( ALL_ROLES ) ) )
            {
                if ( !user.isInRole( strRole ) )
                {
                    // skip this user if it isn't in the role
                    continue;
                }
            }

            Recipient recipient = new Recipient(  );
            recipient.setName( user.getFirstName(  ) + " " + user.getLastName(  ) );
            recipient.setEmail( user.getEmail(  ) );
            listRecipients.add( recipient );
        }

        return listRecipients;
    }
}
