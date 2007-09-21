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
package fr.paris.lutece.plugins.whatsnew.business.portlet;

import fr.paris.lutece.portal.business.portlet.IPortletInterfaceDAO;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * This class provides Data Access methods for WhatsNewPortlet objects
 */
public final class WhatsNewPortletDAO implements IWhatsNewPortletDAO
{
    // Whatsnew queries
    private static final String SQL_QUERY_INSERT = "INSERT INTO whatsnew_portlet (id_portlet, show_documents, show_portlets, show_pages, period, nb_elements_max, elements_order) VALUES ( ?,?,?,?,?,?,? )";
    private static final String SQL_QUERY_DELETE = "DELETE FROM whatsnew_portlet WHERE id_portlet = ?";
    private static final String SQL_QUERY_SELECT = "SELECT id_portlet, show_documents, show_portlets, show_pages, period, nb_elements_max, elements_order FROM whatsnew_portlet WHERE id_portlet = ?";
    private static final String SQL_QUERY_UPDATE = "UPDATE whatsnew_portlet SET show_documents = ?, show_portlets = ?, show_pages = ?, period = ?, nb_elements_max = ?, elements_order = ? WHERE id_portlet = ?";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Insert a new record in the table whatsnew_portlet
     *
     * @param portlet the instance of the Portlet object to insert
     */
    public void insert( Portlet portlet )
    {
        WhatsNewPortlet p = (WhatsNewPortlet) portlet;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        daoUtil.setInt( 1, p.getId(  ) );

        if ( p.getShowDocuments(  ) )
        {
            daoUtil.setInt( 2, 1 );
        }
        else
        {
            daoUtil.setInt( 2, 0 );
        }

        if ( p.getShowPortlets(  ) )
        {
            daoUtil.setInt( 3, 1 );
        }
        else
        {
            daoUtil.setInt( 3, 0 );
        }

        if ( p.getShowPages(  ) )
        {
            daoUtil.setInt( 4, 1 );
        }
        else
        {
            daoUtil.setInt( 4, 0 );
        }

        daoUtil.setInt( 5, p.getPeriod(  ) );
        daoUtil.setInt( 6, p.getNbElementsMax(  ) );
        daoUtil.setInt( 7, p.getElementsOrder(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Delete a record from the table
     *
     * @param nPortletId The identifier of the portlet
     */
    public void delete( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nPortletId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Loads the data of What's new Portlet whose identifier is specified in parameter
     *
     * @param nPortletId The Portlet identifier
     * @return the whatsNew object
     */
    public Portlet load( int nPortletId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nPortletId );
        daoUtil.executeQuery(  );

        WhatsNewPortlet portlet = new WhatsNewPortlet(  );

        if ( daoUtil.next(  ) )
        {
            portlet.setId( daoUtil.getInt( 1 ) );

            if ( daoUtil.getInt( 2 ) == 0 )
            {
                portlet.setShowDocuments( false );
            }
            else
            {
                portlet.setShowDocuments( true );
            }

            if ( daoUtil.getInt( 3 ) == 0 )
            {
                portlet.setShowPortlets( false );
            }
            else
            {
                portlet.setShowPortlets( true );
            }

            if ( daoUtil.getInt( 4 ) == 0 )
            {
                portlet.setShowPages( false );
            }
            else
            {
                portlet.setShowPages( true );
            }

            portlet.setPeriod( daoUtil.getInt( 5 ) );
            portlet.setNbElementsMax( daoUtil.getInt( 6 ) );
            portlet.setElementsOrder( daoUtil.getInt( 7 ) );
        }

        daoUtil.free(  );

        return portlet;
    }

    /**
     * Update the record in the table
     *
     * @param portlet The instance of the object portlet
     */
    public void store( Portlet portlet )
    {
        WhatsNewPortlet p = (WhatsNewPortlet) portlet;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );

        if ( p.getShowDocuments(  ) )
        {
            daoUtil.setInt( 1, 1 );
        }
        else
        {
            daoUtil.setInt( 1, 0 );
        }

        if ( p.getShowPortlets(  ) )
        {
            daoUtil.setInt( 2, 1 );
        }
        else
        {
            daoUtil.setInt( 2, 0 );
        }

        if ( p.getShowPages(  ) )
        {
            daoUtil.setInt( 3, 1 );
        }
        else
        {
            daoUtil.setInt( 3, 0 );
        }

        daoUtil.setInt( 4, p.getPeriod(  ) );
        daoUtil.setInt( 5, p.getNbElementsMax(  ) );
        daoUtil.setInt( 6, p.getElementsOrder(  ) );
        daoUtil.setInt( 7, p.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
