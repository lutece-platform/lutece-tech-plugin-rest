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
package fr.paris.lutece.plugins.contact.web;

import fr.paris.lutece.plugins.contact.business.Contact;
import fr.paris.lutece.plugins.contact.business.ContactHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage contact features ( manage, create, modify, remove, change order of
 * contact )
 */
public class ContactJspBean extends PluginAdminPageJspBean
{
    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // Right
    public static final String RIGHT_MANAGE_CONTACT = "CONTACT_MANAGEMENT";

    // parameters
    private static final String PARAMETER_CONTACT_ID = "contact_id";
    private static final String PARAMETER_CONTACT_NAME = "contact_name";
    private static final String PARAMETER_CONTACT_EMAIL = "contact_email";
    private static final String PARAMETER_CONTACTS_ORDER = "contacts_order";

    // templates
    private static final String TEMPLATE_CONTACTS = "/admin/plugins/contact/manage_contacts.html";
    private static final String TEMPLATE_CREATE_CONTACT = "/admin/plugins/contact/create_contact.html";
    private static final String TEMPLATE_MODIFY_CONTACT = "/admin/plugins/contact/modify_contact.html";

    // properties for page titles
    private static final String PROPERTY_PAGE_TITLE_CONTACTS = "contact.contacts.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY = "contact.modify_contact.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE = "contact.create_contact.pageTitle";

    // Markers
    private static final String MARK_CONTACT_LIST = "contact_list";
    private static final String MARK_ORDER_LIST = "order_list";

    // Jsp Definition
    private static final String JSP_DO_REMOVE_CONTACT = "jsp/admin/plugins/contact/DoRemoveContact.jsp";

    // Messages
    private static final String MESSAGE_CONFIRM_REMOVE_CONTACT = "contact.message.confirmRemoveContact";

    /**
     * Returns the list of people to contact by email
     *
     * @param request The Http request
     * @return the contacts list
     */
    public String getManageContacts( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CONTACTS );

        HashMap rootModel = new HashMap(  );
        rootModel.put( MARK_CONTACT_LIST, ContactHome.findContactsList( getPlugin(  ) ) );
        rootModel.put( MARK_ORDER_LIST, getOrderList(  ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_CONTACTS, getLocale(  ), rootModel );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Returns the form to create a contact
     *
     * @param request The Http request
     * @return the html code of the contact form
     */
    public String getCreateContact( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_CONTACT, getLocale(  ), null );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form of a new contact
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    public String doCreateContact( HttpServletRequest request )
    {
        Contact contact = new Contact(  );
        contact.setName( request.getParameter( PARAMETER_CONTACT_NAME ) );
        contact.setEmail( request.getParameter( PARAMETER_CONTACT_EMAIL ) );

        // Mandatory fields
        if ( request.getParameter( PARAMETER_CONTACT_NAME ).equals( "" ) ||
                request.getParameter( PARAMETER_CONTACT_EMAIL ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        ContactHome.create( contact, getPlugin(  ) );

        // if the operation occurred well, redirects towards the list of the Contact
        return getHomeUrl( request );
    }

    /**
     * Returns the form to update info about a contact
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    public String getModifyContact( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY );

        int nId = Integer.parseInt( request.getParameter( PARAMETER_CONTACT_ID ) );
        Contact contact = ContactHome.findByPrimaryKey( nId, getPlugin(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_CONTACT, getLocale(  ), contact );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the change form of a contact
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doModifyContact( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_CONTACT_ID ) );
        Contact contact = ContactHome.findByPrimaryKey( nId, getPlugin(  ) );
        contact.setName( request.getParameter( PARAMETER_CONTACT_NAME ) );
        contact.setEmail( request.getParameter( PARAMETER_CONTACT_EMAIL ) );

        // Mandatory fields
        if ( request.getParameter( PARAMETER_CONTACT_NAME ).equals( "" ) ||
                request.getParameter( PARAMETER_CONTACT_EMAIL ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        ContactHome.update( contact, getPlugin(  ) );

        // if the operation occurred well, redirects towards the list of the Contacts
        return getHomeUrl( request );
    }

    /**
     * Modifies the order in the list of contacts
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doModifyContactsOrder( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_CONTACT_ID ) );
        Contact contact = ContactHome.findByPrimaryKey( nId, getPlugin(  ) );
        int nOrder = contact.getContactOrder(  );
        int nNewOrder = Integer.parseInt( request.getParameter( PARAMETER_CONTACTS_ORDER ) );
        modifyContactsOrder( nId, nOrder, nNewOrder );

        return getHomeUrl( request );
    }

    /**
     * Manages the removal form of a contact whose identifier is in the http request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    public String getConfirmRemoveContact( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( JSP_DO_REMOVE_CONTACT );
        url.addParameter( PARAMETER_CONTACT_ID, request.getParameter( PARAMETER_CONTACT_ID ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CONTACT, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Treats the removal form of a contact
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage contacts
     */
    public String doRemoveContact( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_CONTACT_ID ) );
        Contact contact = ContactHome.findByPrimaryKey( nId, getPlugin(  ) );
        int nOrder = contact.getContactOrder(  );
        int nNewOrder = ContactHome.getMaxOrderContact( getPlugin(  ) );
        modifyContactsOrder( nId, nOrder, nNewOrder );
        ContactHome.remove( contact, getPlugin(  ) );

        // Go to the parent page
        return getHomeUrl( request );
    }

    //////////////////////////////////////////////////////////////////////////////////
    // Private implementation

    /**
     * Builts a list of sequence numbers
     *
     * @return the list of sequence numbers
     */
    private ReferenceList getOrderList(  )
    {
        int nMax = ContactHome.getMaxOrderContact( getPlugin(  ) );
        ReferenceList list = new ReferenceList(  );

        for ( int i = 1; i < ( nMax + 1 ); i++ )
        {
            list.addItem( i, Integer.toString( i ) );
        }

        return list;
    }

    /**
     * Modify the place in the list for a contact
     *
     * @param nId the contact identifier
     * @param nOrder the actual place in the list
     * @param nNewOrder the new place in the list
     * @return the new ordered list of contacts
     */
    private void modifyContactsOrder( int nId, int nOrder, int nNewOrder )
    {
        if ( nNewOrder < nOrder )
        {
            for ( int i = nOrder - 1; i > ( nNewOrder - 1 ); i-- )
            {
                int nIdContactOrder = ContactHome.getIdByOrder( i, getPlugin(  ) );
                ContactHome.updateContactOrder( i + 1, nIdContactOrder, getPlugin(  ) );
            }

            ContactHome.updateContactOrder( nNewOrder, nId, getPlugin(  ) );
        }
        else if ( nNewOrder > nOrder )
        {
            for ( int i = nOrder; i < ( nNewOrder + 1 ); i++ )
            {
                int nIdContactOrder = ContactHome.getIdByOrder( i, getPlugin(  ) );
                ContactHome.updateContactOrder( i - 1, nIdContactOrder, getPlugin(  ) );
            }

            ContactHome.updateContactOrder( nNewOrder, nId, getPlugin(  ) );
        }
    }
}
