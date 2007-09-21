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
package fr.paris.lutece.plugins.whatsnew.business;

import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


/**
 * This class provides Data Access methods for What's New objects
 */
public class WhatsNewDAO implements IWhatsNewDAO
{
    ////////////////////////////////////////////////////////////////////////////
    // WhatsNew queries
    private static final String SQL_QUERY_SELECT_DOCUMENTS_BY_CRITERIAS = "SELECT a.title, a.summary as description, a.date_modification, a.id_document, b.id_portlet FROM document a, document_published b WHERE (a.id_document=b.id_document) AND (a.date_modification between ? AND ?) AND (a.date_validity_begin is null or ?>a.date_validity_begin) AND (a.date_validity_end is null or ?<a.date_validity_end)";
    private static final String SQL_QUERY_SELECT_PORTLETS_BY_CRITERIAS = "SELECT a.name as title,'',a.date_update, a.id_page, a.id_portlet FROM core_portlet a WHERE a.date_update between ? AND ?";
    private static final String SQL_QUERY_SELECT_PAGES_BY_CRITERIAS = "SELECT a.name as title, a.description, a.date_update, a.id_page FROM core_page a WHERE a.date_update between ? AND ?";

    /**
     * Returns the list of the documents which correspond to the criteria given
     * @param dateLimit the timestamp giving the beginning of the period to watch
     * @return the list in form of a Collection object
     */
    public Collection selectDocumentsByCriterias( Timestamp dateLimit )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_DOCUMENTS_BY_CRITERIAS );

        Timestamp timestampCurrent = new Timestamp( ( new Date(  ) ).getTime(  ) );
        daoUtil.setTimestamp( 1, dateLimit );
        daoUtil.setTimestamp( 2, timestampCurrent );
        daoUtil.setTimestamp( 3, timestampCurrent );
        daoUtil.setTimestamp( 4, timestampCurrent );
        daoUtil.executeQuery(  );

        ArrayList list = new ArrayList(  );

        while ( daoUtil.next(  ) )
        {
            WhatsNew whatsnew = new WhatsNew(  );
            whatsnew.setType( WhatsNew.TYPE_DOCUMENT );
            whatsnew.setTitle( daoUtil.getString( 1 ) );
            whatsnew.setDescription( daoUtil.getString( 2 ) );
            whatsnew.setDateUpdate( daoUtil.getTimestamp( 3 ) );
            whatsnew.setDocumentId( daoUtil.getInt( 4 ) );
            whatsnew.setPortletId( daoUtil.getInt( 5 ) );
            list.add( whatsnew );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Returns the list of the portlets which correspond to the criteria given
     * @param dateLimit the timestamp giving the beginning of the period to watch
     * @return the list in form of a Collection object
     */
    public Collection selectPortletsByCriterias( Timestamp dateLimit )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PORTLETS_BY_CRITERIAS );

        Timestamp timestampCurrent = new Timestamp( ( new Date(  ) ).getTime(  ) );
        daoUtil.setTimestamp( 1, dateLimit );
        daoUtil.setTimestamp( 2, timestampCurrent );
        daoUtil.executeQuery(  );

        ArrayList list = new ArrayList(  );

        while ( daoUtil.next(  ) ) //if
        {
            WhatsNew whatsnew = new WhatsNew(  );
            whatsnew.setType( WhatsNew.TYPE_PORTLET );
            whatsnew.setTitle( daoUtil.getString( 1 ) );
            whatsnew.setDescription( daoUtil.getString( 2 ) );
            whatsnew.setDateUpdate( daoUtil.getTimestamp( 3 ) );
            whatsnew.setPageId( daoUtil.getInt( 4 ) );
            whatsnew.setPortletId( daoUtil.getInt( 5 ) );
            list.add( whatsnew );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Returns the list of the pages which correspond to the criteria given
     * @param dateLimit the timestamp giving the beginning of the period to watch
     *
     * @return the list in form of a Collection object
     */
    public Collection selectPagesByCriterias( Timestamp dateLimit )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PAGES_BY_CRITERIAS );

        Timestamp timestampCurrent = new Timestamp( ( new Date(  ) ).getTime(  ) );
        daoUtil.setTimestamp( 1, dateLimit );
        daoUtil.setTimestamp( 2, timestampCurrent );
        daoUtil.executeQuery(  );

        ArrayList list = new ArrayList(  );

        while ( daoUtil.next(  ) )
        {
            WhatsNew whatsnew = new WhatsNew(  );
            whatsnew.setType( WhatsNew.TYPE_PAGE );
            whatsnew.setTitle( daoUtil.getString( 1 ) );
            whatsnew.setDescription( daoUtil.getString( 2 ) );
            whatsnew.setDateUpdate( daoUtil.getTimestamp( 3 ) );
            whatsnew.setPageId( daoUtil.getInt( 4 ) );
            list.add( whatsnew );
        }

        daoUtil.free(  );

        return list;
    }
}
