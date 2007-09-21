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
package fr.paris.lutece.plugins.newsletter.web;


/**
 * Shared constants
 */
public class SharedConstants
{
    public static final String CONSTANT_EMPTY_STRING = "";

    ///////////////////////////////////////////////////////////////////////
    // BOOKMARKS
    public static final String MARK_ROWS = "rows";

    //    public static final String MARK_PLUGIN_NAME = "plugin_name";
    public static final String MARK_TEMPLATE_ID = "template_id";
    public static final String PLUGIN_NAME = "newsletter";
    public static final String MARK_TEMPLATE_TYPE = "template_type";
    public static final String MARK_TEMPLATE_DESCRIPTION = "template_description";
    public static final String MARK_TEMPLATE_FILE_NAME = "template_file_name";
    public static final String MARK_TEMPLATE_PICTURE = "template_picture";
    public static final String MARK_TEMPLATE_SOURCE = "template_source";
    public static final String MARK_SUBSCRIBER_EMAIL = "subscriber_email";
    public static final String MARK_SUBSCRIBER_EMAIL_EACH = "@email@";
    public static final String MARK_SUBSCRIBER_DATE = "subscriber_date";
    public static final String MARK_SUBSCRIBER_ID = "subscriber_id";
    public static final String MARK_PREVIOUS_NEXT = "previous_next";
    public static final String MARK_PORTAL_URL = "portal_url";
    public static final String MARK_PROD_URL = "prod_url";
    public static final String PROPERTY_UNSUBSCRIBE_TRUE = "TRUE";
    public static final String PROPERTY_UNSUBSCRIBE_FALSE = "FALSE";

    ///////////////////////////////////////////////////////////////////////
    // bookmarks to use for documents templates
    public static final String MARK_DOCUMENT_PORTLET_ID = "portlet_id";
    public static final String MARK_DOCUMENT_ID = "document_id";
    public static final String MARK_DOCUMENT = "document";
    public static final String MARK_DOCUMENT_LIST = "document_list";
    public static final String MARK_DOCUMENT_THUMBNAIL = "document_thumbnail";
    public static final String MARK_DOCUMENT_TITLE = "document_title";
    public static final String MARK_DOCUMENT_INTRO = "document_intro";
    public static final String MARK_DOCUMENT_PUBLICATION_DATE = "document_publication_date";

    ///////////////////////////////////////////////////////////////////////
    // bookmark to use for newsletter templates
    public static final String MARK_DOCUMENTS_LIST = "@documents_list@";

    ///////////////////////////////////////////////////////////////////////
    // properties
    public static final String PROPERTY_PATH_FILE_NEWSLETTER_TEMPLATE = ".path.file.newsletter.template";
    public static final String PROPERTY_PROD_URL = "lutece.prod.url";
    public static final String PROPERTY_PATH_DOCUMENTS_IMAGES = "document.path.images";
    public static final String PROPERTY_PORTAL_JSP_PATH = "lutece.portal.path";
    public static final String PROPERTY_MAIL_MULTIPART = ".mail.multipart";
    public static final String ALL_GROUPS = "all";
    public static final String PROPERTY_LABEL_ALL_GROUPS = "portal.workgroup.labelAllGroups";

    /////////////////////////////////////////////////////////////////
    // parameters
    public static final String PARAMETER_PLUGIN_NAME = "plugin_name";
    public static final String PARAMETER_EMAIL = "email";
    public static final String PARAMETER_SUBSCRIBER_SEARCH = "subscriber_search";

    // newsletter templates //////////////////////////////////////////
    public static final String PARAMETER_NEWSLETTER_TEMPLATE_NAME = "newsletter_template_name";
    public static final String PARAMETER_NEWSLETTER_TEMPLATE_FILE = "newsletter_template_file";
    public static final String PARAMETER_NEWSLETTER_TEMPLATE_PICTURE = "newsletter_template_picture";
    public static final String PARAMETER_NEWSLETTER_TEMPLATE_NEW_FILE = "newsletter_template_new_file";
    public static final String PARAMETER_NEWSLETTER_TEMPLATE_NEW_PICTURE = "newsletter_template_new_picture";
    public static final String PARAMETER_NEWSLETTER_TEMPLATE_TYPE = "newsletter_template_type";
    public static final String PARAMETER_NEWSLETTER_TEMPLATE_ID = "newsletter_template_id";
    public static final String PARAMETER_NEWSLETTER_TEMPLATE_SOURCE = "newsletter_template_source";
    public static final String PARAMETER_NEWSLETTER_IMPORT_PATH = "newsletter_import_path";
    public static final String MARK_TEMPLATE = "template";
}
