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
package fr.paris.lutece.plugins.newsletter.business;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * This class provides Data Access methods for NewsLetter objects
 */
public final class NewsLetterDAO implements INewsLetterDAO
{
    // Constants
    private static final String SQL_QUERY_SELECT = "SELECT name, date_last_send, html, id_newsletter_template, id_document_template, workgroup_key, unsubscribe, sender_mail, test_recipients  FROM newsletter WHERE id_newsletter = ? ";
    private static final String SQL_QUERY_SELECT_ALL = "SELECT id_newsletter , name, date_last_send, html, id_newsletter_template, id_document_template, workgroup_key, test_recipients , sender_mail FROM newsletter ";
    private static final String SQL_QUERY_SELECT_NBR_SUBSCRIBERS = "SELECT count(*) FROM subscriber_newsletter a, subscriber b WHERE a.id_subscriber = b.id_subscriber AND b.email LIKE ? AND id_newsletter = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE newsletter SET name = ?, date_last_send = ?, html = ?, id_newsletter_template = ?, id_document_template = ?, workgroup_key = ? , unsubscribe = ? ,sender_mail = ? , test_recipients = ? WHERE id_newsletter = ? ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO newsletter ( id_newsletter , name, date_last_send, html, id_newsletter_template, id_document_template, workgroup_key, unsubscribe, sender_mail, test_recipients  ) VALUES ( ?, ?, ?, ?, ?, ?, ? , ?, ?, ? )";
    private static final String SQL_QUERY_INSERT_SUBSCRIBER = "INSERT INTO subscriber_newsletter ( id_newsletter , id_subscriber, date_subscription ) VALUES ( ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = "DELETE FROM newsletter WHERE id_newsletter = ? ";
    private static final String SQL_QUERY_DELETE_FROM_SUBSCRIBER = "DELETE FROM subscriber_newsletter WHERE id_newsletter = ? and id_subscriber = ? ";
    private static final String SQL_QUERY_DELETE_ALL_SUBSCRIBERS = "DELETE FROM subscriber_newsletter WHERE id_newsletter = ?";
    private static final String SQL_QUERY_DELETE_NEWSLETTER_THEME = "DELETE FROM theme_newsletter WHERE id_newsletter = ?";
    private static final String SQL_QUERY_CHECK_PRIMARY_KEY = "SELECT id_newsletter FROM newsletter WHERE id_newsletter = ?";
    private static final String SQL_QUERY_CHECK_LINKED_PORTLET = "SELECT id_newsletter FROM  portlet_newsletter_subscription WHERE id_newsletter = ?";
    private static final String SQL_QUERY_NEW_PRIMARY_KEY = "SELECT max(id_newsletter) FROM newsletter ";
    private static final String SQL_QUERY_CHECK_IS_REGISTERED = "SELECT id_newsletter FROM subscriber_newsletter WHERE id_newsletter = ? AND id_subscriber = ? ";
    private static final String SQL_QUERY_CHECK_IS_TEMPLATE_USED = "SELECT id_newsletter FROM newsletter WHERE id_newsletter_template = ? OR id_document_template = ? ";
    private static final String SQL_QUERY_SELECT_DOCUMENT_LIST = "SELECT DISTINCT name FROM core_portlet WHERE id_portlet_type='DOCUMENT_PORTLET' and id_portlet = ?  ";
    private static final String SQL_QUERY_SELECT_NEWSLETTER_THEMES_IDS = "SELECT DISTINCT id_theme FROM theme_newsletter WHERE id_newsletter = ?";
    private static final String SQL_QUERY_ASSOCIATE_NEWSLETTER_THEME = "INSERT INTO theme_newsletter ( id_newsletter , id_theme ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_SELECT_DOCUMENT_BY_DATE_AND_THEME = "SELECT a.id_document , a.code_document_type " +
        ", a.date_creation , a.date_modification,a.title,a.summary FROM document as a  " +
        " INNER JOIN document_published AS b WHERE a.id_document=b.id_document ";
    private static final String SQL_QUERY_DOCUMENT_TYPE_PORTLET = " SELECT DISTINCT id_portlet , name FROM core_portlet WHERE id_portlet_type='DOCUMENT_PORTLET'  ";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Insert a new record in the table.
     *
     * @param newsLetter the object to insert
     * @param plugin the Plugin
     */
    public void insert( NewsLetter newsLetter, Plugin plugin )
    {
        int nNewPrimaryKey = newPrimaryKey( plugin );
        newsLetter.setId( nNewPrimaryKey );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        daoUtil.setInt( 1, newsLetter.getId(  ) );
        daoUtil.setString( 2, newsLetter.getName(  ) );
        daoUtil.setTimestamp( 3, newsLetter.getDateLastSending(  ) );
        daoUtil.setString( 4, newsLetter.getHtml(  ) );
        daoUtil.setInt( 5, newsLetter.getNewsLetterTemplateId(  ) );
        daoUtil.setInt( 6, newsLetter.getDocumentTemplateId(  ) );
        daoUtil.setString( 7, newsLetter.getWorkgroup(  ) );
        daoUtil.setString( 8, newsLetter.getUnsubscribe(  ) );
        daoUtil.setString( 9, newsLetter.getNewsletterSenderMail(  ) );
        daoUtil.setString( 10, newsLetter.getTestRecipients(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Remove a record from the table
     *
     * @param nNewsLetterId the newsletter identifier
     * @param plugin the Plugin
     */
    public void delete( int nNewsLetterId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nNewsLetterId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * loads the data of the newsletter from the table
     *
     * @param nNewsLetterId the newsletter identifier
     * @param plugin the Plugin
     * @return the object inserted
     */
    public NewsLetter load( int nNewsLetterId, Plugin plugin )
    {
        NewsLetter newsLetter = new NewsLetter(  );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nNewsLetterId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            newsLetter.setId( nNewsLetterId );
            newsLetter.setName( daoUtil.getString( 1 ) );
            newsLetter.setDateLastSending( daoUtil.getTimestamp( 2 ) );
            newsLetter.setHtml( daoUtil.getString( 3 ) );
            newsLetter.setNewsLetterTemplateId( daoUtil.getInt( 4 ) );
            newsLetter.setDocumentTemplateId( daoUtil.getInt( 5 ) );
            newsLetter.setWorkgroup( daoUtil.getString( 6 ) );
            newsLetter.setUnsubscribe( daoUtil.getString( 7 ) );
            newsLetter.setNewsletterSenderMail( daoUtil.getString( 8 ) );
            newsLetter.setTestRecipients( daoUtil.getString( 9 ) );
        }

        daoUtil.free(  );

        return newsLetter;
    }

    /**
     * Update the record in the table
     *
     * @param newsLetter the object to be updated
     * @param plugin the Plugin
     */
    public void store( NewsLetter newsLetter, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setString( 1, newsLetter.getName(  ) );
        daoUtil.setTimestamp( 2, newsLetter.getDateLastSending(  ) );
        daoUtil.setString( 3, newsLetter.getHtml(  ) );
        daoUtil.setInt( 4, newsLetter.getNewsLetterTemplateId(  ) );
        daoUtil.setInt( 5, newsLetter.getDocumentTemplateId(  ) );
        daoUtil.setString( 6, newsLetter.getWorkgroup(  ) );
        daoUtil.setString( 7, newsLetter.getUnsubscribe(  ) );
        daoUtil.setString( 8, newsLetter.getNewsletterSenderMail(  ) );
        daoUtil.setString( 9, newsLetter.getTestRecipients(  ) );
        daoUtil.setInt( 10, newsLetter.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Check the unicity of the primary key
     *
     * @param nKey the key to be checked
     * @param plugin the Plugin
     * @return true if the identifier exist and false if not
     */
    public boolean checkPrimaryKey( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return false;
        }

        daoUtil.free(  );

        return true;
    }

    /**
     * Check whether a portlet is linked to a newsletter
     *
     * @param nIdNewsletter the id of the newsletter
     * @return true if the newsletter is used by subscription portlet
     */
    public boolean checkLinkedPortlet( int nIdNewsletter )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_LINKED_PORTLET );
        daoUtil.setInt( 1, nIdNewsletter );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return false;
        }

        daoUtil.free(  );

        return true;
    }

    /**
     * Generate a new primary key to add a newsletter
     *
     * @param plugin the Plugin
     * @return the new key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PRIMARY_KEY, plugin );
        int nKey;

        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            // If the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nKey;
    }

    /**
     * Select the list of the newsletters available
     * @param plugin the Plugin
     * @return a collection of objects
     */
    public Collection<NewsLetter> selectAll( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ALL, plugin );
        daoUtil.executeQuery(  );

        ArrayList<NewsLetter> list = new ArrayList<NewsLetter>(  );

        while ( daoUtil.next(  ) )
        {
            NewsLetter newsLetter = new NewsLetter(  );
            newsLetter.setId( daoUtil.getInt( 1 ) );
            newsLetter.setName( daoUtil.getString( 2 ) );
            newsLetter.setDateLastSending( daoUtil.getTimestamp( 3 ) );
            newsLetter.setHtml( daoUtil.getString( 4 ) );
            newsLetter.setNewsLetterTemplateId( daoUtil.getInt( 5 ) );
            newsLetter.setDocumentTemplateId( daoUtil.getInt( 6 ) );
            newsLetter.setWorkgroup( daoUtil.getString( 7 ) );
            newsLetter.setTestRecipients( daoUtil.getString( 8 ) );
            newsLetter.setNewsletterSenderMail( daoUtil.getString( 9 ) );
            list.add( newsLetter );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Insert a new subscriber for a newsletter
     *
     * @param nNewsLetterId the newsletter identifier
     * @param nSubscriberId the subscriber identifier
     * @param tToday The day
     * @param plugin the Plugin
     */
    public void insertSubscriber( int nNewsLetterId, int nSubscriberId, Timestamp tToday, Plugin plugin )
    {
        // Check if the subscriber is yet registered for the newsletter
        if ( isRegistered( nNewsLetterId, nSubscriberId, plugin ) )
        {
            return;
        }

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_SUBSCRIBER, plugin );

        daoUtil.setInt( 1, nNewsLetterId );
        daoUtil.setInt( 2, nSubscriberId );
        daoUtil.setTimestamp( 3, tToday );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Remove the subscriber's inscription to a newsletter
     *
     * @param nNewsLetterId the newsletter identifier
     * @param nSubscriberId the subscriber identifier
     * @param plugin the Plugin
     */
    public void deleteSubscriber( int nNewsLetterId, int nSubscriberId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_FROM_SUBSCRIBER, plugin );

        daoUtil.setInt( 1, nNewsLetterId );
        daoUtil.setInt( 2, nSubscriberId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * check if the subscriber is not yet registered to a newsletter
     *
     * @param nNewsLetterId the newsletter identifier
     * @param nSubscriberId the subscriber identifier
     * @param plugin the Plugin
     * @return true if he is registered and false if not
     */
    public boolean isRegistered( int nNewsLetterId, int nSubscriberId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_IS_REGISTERED, plugin );

        daoUtil.setInt( 1, nNewsLetterId );
        daoUtil.setInt( 2, nSubscriberId );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return false;
        }

        daoUtil.free(  );

        return true;
    }

    /**
     * controls that a template is used by a newsletter
     *
     * @param nTemplateId the template identifier
     * @param plugin the Plugin
     * @return true if the template is used, false if not
     */
    public boolean isTemplateUsed( int nTemplateId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHECK_IS_TEMPLATE_USED, plugin );

        daoUtil.setInt( 1, nTemplateId );
        daoUtil.setInt( 2, nTemplateId );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return false;
        }

        daoUtil.free(  );

        return true;
    }

    /**
     * loads the list of topics from a given list of ids
     *
     * @param nKeyArray the theme identifiers list
     * @return the list of topics
     */
    public ReferenceList selectThemesFromIds( int[] nKeyArray )
    {
        String strSQL = " SELECT id_theme, description_theme FROM theme ";

        for ( int i = 0; i < nKeyArray.length; i++ )
        {
            if ( i == 0 )
            {
                strSQL += " WHERE id_theme = ? ";
            }
            else
            {
                strSQL += " OR id_theme = ? ";
            }
        }

        DAOUtil daoUtil = new DAOUtil( strSQL );

        for ( int j = 0; j < nKeyArray.length; j++ )
        {
            daoUtil.setInt( j + 1, nKeyArray[j] );
        }

        daoUtil.executeQuery(  );

        ReferenceList list = new ReferenceList(  );

        while ( daoUtil.next(  ) )
        {
            list.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * loads the list of document
     *
     * @param nPortletId the portlet id
     * @return the name of the topic
     */
    public String selectDocumentList( int nPortletId )
    {
        String strThemeName = "";
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_DOCUMENT_LIST );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            strThemeName = daoUtil.getString( 1 );
        }

        daoUtil.free(  );

        return strThemeName;
    }

    /**
     * loads the list of documents
     *
     * @param nNewsletterId the newsletter identifier
     * @param plugin the plugin
     * @return the array of ids
     */
    public int[] selectNewsletterThemeIds( int nNewsletterId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NEWSLETTER_THEMES_IDS, plugin );

        daoUtil.setInt( 1, nNewsletterId );

        daoUtil.executeQuery(  );

        List<Integer> list = new ArrayList<Integer>(  );

        while ( daoUtil.next(  ) )
        {
            int nResultId = daoUtil.getInt( 1 );
            list.add( new Integer( nResultId ) );
        }

        int[] nIdsArray = new int[list.size(  )];

        for ( int i = 0; i < list.size(  ); i++ )
        {
            Integer nId = (Integer) list.get( i );
            nIdsArray[i] = nId.intValue(  );
        }

        daoUtil.free(  );

        return nIdsArray;
    }

    /**
     * Associate a new topic to a newsletter
     *
     * @param nNewsLetterId the newsletter identifier
     * @param nThemeId the topic identifier
     * @param plugin the Plugin
     */
    public void associateNewsLetterTheme( int nNewsLetterId, int nThemeId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ASSOCIATE_NEWSLETTER_THEME, plugin );
        daoUtil.setInt( 1, nNewsLetterId );
        daoUtil.setInt( 2, nThemeId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Remove the relationship between a newsletter and the list of themes
     *
     * @param nNewsLetterId the newsletter identifier
     * @param plugin the Plugin
     */
    public void deleteNewsLetterTheme( int nNewsLetterId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_NEWSLETTER_THEME, plugin );

        daoUtil.setInt( 1, nNewsLetterId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Select the list of documents published since the last sending of the newsletter
     *
     * @param nPortletId the topic identifier to select documents
     * @param dateLastSending the date of the last newsletter sending
     * @return a list of documents
     */
    public Collection<Document> selectDocumentsByDateAndList( int nPortletId, Timestamp dateLastSending )
    {
        String strSQL = SQL_QUERY_SELECT_DOCUMENT_BY_DATE_AND_THEME;

        if ( nPortletId != 0 )
        {
            strSQL += " AND b.id_portlet = ? ";
        }

        if ( dateLastSending != null )
        {
            strSQL += " AND a.date_modification >= ?";
        }

        strSQL += " ORDER BY b.id_portlet, a.date_modification DESC ";

        DAOUtil daoUtil = new DAOUtil( strSQL );

        int nBindVariableIndex = 1;

        if ( nPortletId != 0 )
        {
            daoUtil.setInt( nBindVariableIndex, nPortletId );
            nBindVariableIndex++;
        }

        if ( dateLastSending != null )
        {
            daoUtil.setTimestamp( nBindVariableIndex, dateLastSending );
            nBindVariableIndex++;
        }

        daoUtil.executeQuery(  );

        List<Document> list = new ArrayList<Document>(  );

        while ( daoUtil.next(  ) )
        {
            Document document = new Document(  );
            document.setId( daoUtil.getInt( 1 ) );
            document.setCodeDocumentType( daoUtil.getString( 2 ) );
            document.setDateCreation( daoUtil.getTimestamp( 3 ) );
            document.setDateModification( daoUtil.getTimestamp( 4 ) );
            document.setTitle( daoUtil.getString( 5 ) );
            document.setSummary( daoUtil.getString( 6 ) );
            list.add( document );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Counts the subscribers for a newsletter
     *
     * @param nNewsLetterId the newsletter newsletter
     * @param strSearchString the string to search in the subscriber's email
     * @param plugin the Plugin
     * @return the number of subscribers
     */
    public int selectNbrSubscribers( int nNewsLetterId, String strSearchString, Plugin plugin )
    {
        int nCount;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NBR_SUBSCRIBERS, plugin );

        daoUtil.setString( 1, "%" + strSearchString + "%" );
        daoUtil.setInt( 2, nNewsLetterId );

        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            // If the table is empty
            nCount = 0;
        }

        nCount = daoUtil.getInt( 1 );

        daoUtil.free(  );

        return nCount;
    }

    /**
     * Returns the list of the portlets which are document portlets
     * @return the list in form of a Collection object
     */
    public ReferenceList selectDocumentTypePortlets(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DOCUMENT_TYPE_PORTLET );
        daoUtil.executeQuery(  );

        ReferenceList list = new ReferenceList(  );

        while ( daoUtil.next(  ) )
        {
            list.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Removes all the subscribers attached to a newsletter
     *
     * @param nNewsLetterId the newsletter identifier
     * @param plugin the Plugin
     */
    public void deleteAllSubscribers( int nNewsLetterId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ALL_SUBSCRIBERS, plugin );
        daoUtil.setInt( 1, nNewsLetterId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
