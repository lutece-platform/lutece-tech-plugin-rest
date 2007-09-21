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
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.newsletter.web.SharedConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.sql.Timestamp;

import java.util.Collection;


/**
 * This class provides instances management methods (create, find, ...) for NewsLetter objects
 */
public final class NewsLetterHome
{
    // Static variable pointed at the DAO instance
    private static INewsLetterDAO _dao = (INewsLetterDAO) SpringContextService.getPluginBean( "newsletter",
            "newsLetterDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private NewsLetterHome(  )
    {
    }

    /**
     * Create an instance of the newsletter
     *
     * @param newsLetter the object to insert into the database
     * @param plugin the Plugin
     * @return the instance created
     */
    public static NewsLetter create( NewsLetter newsLetter, Plugin plugin )
    {
        _dao.insert( newsLetter, plugin );

        return newsLetter;
    }

    /**
     * update of the newsletter which is specified in parameter
     *
     * @param newsLetter the instacne of newsletter which contains the data to store
     * @param plugin the Plugin
     * @return the new instance updated
     */
    public static NewsLetter update( NewsLetter newsLetter, Plugin plugin )
    {
        _dao.store( newsLetter, plugin );

        return newsLetter;
    }

    /**
     * Remove the record from the identifier a newsletter
     *
     * @param nNewsLetterId the newsletter identifier
     * @param plugin the Plugin
     */
    public static void remove( int nNewsLetterId, Plugin plugin )
    {
        _dao.delete( nNewsLetterId, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an object NewsLetter from its identifier
     *
     * @param nKey the primary key of the newsletter
     * @param plugin the Plugin
     * @return an instance of the class
     */
    public static NewsLetter findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Returns a collection of NewsLetter objects
     *
     * @param plugin the Plugin
     * @return the collection of objects
     */
    public static Collection<NewsLetter> findAll( Plugin plugin )
    {
        return _dao.selectAll( plugin );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Operations

    /**
     * insert a new subscriber for e newsletter
     *
     * @param nNewsLetterId the newsletter identifier
     * @param nSubscriberId the subscriber indentifier
     * @param plugin the Plugin
     * @param tToday the day
     */
    public static void addSubscriber( int nNewsLetterId, int nSubscriberId, Timestamp tToday, Plugin plugin )
    {
        _dao.insertSubscriber( nNewsLetterId, nSubscriberId, tToday, plugin );
    }

    /**
     * removes an subscriber's inscription for a newsletter
     *
     * @param nNewsLetterId the newsletter identifier
     * @param nSubscriberId the subscriber identifier
     * @param plugin the Plugin
     */
    public static void removeSubscriber( int nNewsLetterId, int nSubscriberId, Plugin plugin )
    {
        _dao.deleteSubscriber( nNewsLetterId, nSubscriberId, plugin );
    }

    /**
     * loads the list of topics of the newsletter
     *
     * @param nNewsletterId the newsletter identifier
     * @param plugin the plugin
     * @return the array of ids
     */
    public static int[] findNewsletterThemesIds( int nNewsletterId, Plugin plugin )
    {
        return _dao.selectNewsletterThemeIds( nNewsletterId, plugin );
    }

    /**
     * loads the list of topics from a given list of ids
     *
     * @param nKeyArray the array of theme identifiers
     * @return the list of topics
     */
    public static ReferenceList findThemesFromIds( int[] nKeyArray )
    {
        return _dao.selectThemesFromIds( nKeyArray );
    }

    /**
     * loads the list of topics from a given id
     *
     * @param nPortletId the theme identifier
     * @return the name of the topic
     */
    public static String findDocumentListDescription( int nPortletId )
    {
        return _dao.selectDocumentList( nPortletId );
    }

    /**
     * Returns the number of subscriber for a newsletter
     *
     * @param nNewsLetterId the identifier of the newsletter
     * @param plugin the Plugin
     * @return the number of subscriber for a newsletter
     */
    public static int findNbrSubscribers( int nNewsLetterId, Plugin plugin )
    {
        return _dao.selectNbrSubscribers( nNewsLetterId, SharedConstants.CONSTANT_EMPTY_STRING, plugin );
    }

    /**
     * Returns the number of subscriber with an email containing a given string for a newsletter
     *
     * @param nNewsLetterId the identifier of the newsletter
     * @param strSearchString the string to search in the subscriber's email
     * @param plugin the Plugin
     * @return the number of subscriber for a newsletter
     */
    public static int findNbrSubscribers( int nNewsLetterId, String strSearchString, Plugin plugin )
    {
        return _dao.selectNbrSubscribers( nNewsLetterId, strSearchString, plugin );
    }

    /**
     * Associate a topic to a newsletter
     *
     * @param nNewsLetterId the newsletter identifier
     * @param nThemeId the topic identifier
     * @param plugin the Plugin
     */
    public static void associateNewsLetterTheme( int nNewsLetterId, int nThemeId, Plugin plugin )
    {
        _dao.associateNewsLetterTheme( nNewsLetterId, nThemeId, plugin );
    }

    /**
     * Removes the relationship between a list of topics and a newsletter
     *
     * @param nNewsLetterId the newsletter identifier
     * @param plugin the Plugin
     */
    public static void removeNewsLetterTheme( int nNewsLetterId, Plugin plugin )
    {
        _dao.deleteNewsLetterTheme( nNewsLetterId, plugin );
    }

    /**
     * Returns the list of documents published by date and by topic
     *
     * @param nDocumentListId the list identifier
     * @param dtDernierEnvoi the date of the last sending
     * @return a collection of document
     */
    public static Collection<Document> findDocumentsByDateAndList( int nDocumentListId, Timestamp dtDernierEnvoi )
    {
        return _dao.selectDocumentsByDateAndList( nDocumentListId, dtDernierEnvoi );
    }

    /**
     * Returns the  documents for a given document id
     *
     * @param nDocumentId the document id
     * @return an DocumentPortlet object
     */
    public static Document findDocumentById( int nDocumentId )
    {
        return DocumentHome.findByPrimaryKey( nDocumentId );
    }

    /**
     * controls that a subscriber is not yet registered for a newsletter
     *
     * @param nNewsLetterId the newsletter identifier
     * @param nSubscriberId the subscriber identifier
     * @param plugin the Plugin
     * @return true if he is registered, false if not
     */
    public static boolean findRegistration( int nNewsLetterId, int nSubscriberId, Plugin plugin )
    {
        return _dao.isRegistered( nNewsLetterId, nSubscriberId, plugin );
    }

    /**
     * controls that a template is used by a newsletter
     *
     * @param nTemplateId the template identifier
     * @param plugin the Plugin
     * @return true if the template is used, false if not
     */
    public static boolean findTemplate( int nTemplateId, Plugin plugin )
    {
        return _dao.isTemplateUsed( nTemplateId, plugin );
    }

    /**
    * Verifies if a portlet uses a newsletter
    *
    * @param nIdNewsletter the template identifier
    * @param plugin the Plugin
    * @return true if portlet uses newsletter
    */
    public static boolean checkLinkedPortlets( int nIdNewsletter )
    {
        return _dao.checkLinkedPortlet( nIdNewsletter );
    }

    /**
     * Returns a collection any portlet containing at least a published document
     *
     * @return the portlets in form of Collection
     */
    public static ReferenceList getDocumentLists(  )
    {
        return _dao.selectDocumentTypePortlets(  );
    }
}
