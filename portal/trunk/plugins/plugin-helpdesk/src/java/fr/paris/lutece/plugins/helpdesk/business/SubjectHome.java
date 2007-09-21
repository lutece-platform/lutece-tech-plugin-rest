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
package fr.paris.lutece.plugins.helpdesk.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...)
 * for Subject objects
 */
public final class SubjectHome
{
    // Static variable pointed at the DAO instance
    private static ISubjectDAO _dao = (ISubjectDAO) SpringContextService.getPluginBean( "helpdesk", "subjectDAO" );

    /**
     * Private constructor - this class need not be instantiated.
     */
    private SubjectHome(  )
    {
    }

    /**
     * Creation of an instance of an article Subject
     *
     * @param subject An instance of the Subject which contains the informations to store
     * @param plugin The current plugin using this method
     * @return The instance of the Subject which has been created
     */
    public static Subject create( Subject subject, Plugin plugin )
    {
        _dao.insert( subject, plugin );

        return subject;
    }

    /**
     * Updates of the Subject instance specified in parameter
     *
     * @param subject An instance of the Subject which contains the informations to store
     * @param plugin The current plugin using this method
     * @return The instance of the Subject which has been updated.
     */
    public static Subject update( Subject subject, Plugin plugin )
    {
        _dao.store( subject, plugin );

        return subject;
    }

    /**
     * Deletes the Subject instance whose identifier is specified in parameter
     *
     * @param nIdSubject The identifier of the article Subject to delete in the database
     * @param plugin The current plugin using this method
     */
    public static void remove( int nIdSubject, Plugin plugin )
    {
        _dao.delete( nIdSubject, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of the article Subject whose identifier is specified in parameter
     *
     * @param nKey The primary key of the article to find in the database
     * @param plugin The current plugin using this method
     * @return An instance of the Subject which corresponds to the key
     */
    public static Subject findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Returns Subject list
     *
     * @param plugin The current plugin using this method
     * @return the list of the Subject of the database in form of a Subject Collection object
     */
    public static List<Subject> findAll( Plugin plugin )
    {
        return _dao.findAll( plugin );
    }

    /**
     * Returns Subject list
     *
     * @param plugin The current plugin using this method
     * @return the list of the Subject of the database in form of a Subject Collection object
     */
    public static ReferenceList findSubject( Plugin plugin )
    {
        return _dao.findSubject( plugin );
    }

    /**
     * Returns Question list for Subject
     * @param plugin The current plugin using this method
     * @param nIdSubject The Subject ID
     * @return the list of the Question of subject
     */
    public static List<QuestionAnswer> findQuestion( int nIdSubject, Plugin plugin )
    {
        return _dao.findQuestions( nIdSubject, plugin );
    }

    /**
     * return the count of all announce for Field
     * @param plugin The current plugin using this method
     * @param nIdSubject The subject ID
     * @return count of announce for Field
     */
    public static int countQuestionSubject( int nIdSubject, Plugin plugin )
    {
        return _dao.countQuestion( nIdSubject, plugin );
    }
}
