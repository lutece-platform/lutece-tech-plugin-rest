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
package fr.paris.lutece.plugins.document.business.spaces;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * This class provides Data Access methods for DocumentSpace objects
 */
public final class DocumentSpaceDAO implements IDocumentSpaceDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_space ) FROM document_space ";
    private static final String SQL_QUERY_SELECT = " SELECT a.id_space, a.id_parent, a.name, a.description, a.view, a.id_space_icon, b.icon_url, a.document_creation_allowed " +
        " FROM document_space a, document_space_icon b " +
        " WHERE a.id_space_icon = b.id_space_icon AND id_space = ?  ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO document_space ( id_space, id_parent, name, description, view, id_space_icon, document_creation_allowed ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM document_space WHERE id_space = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE document_space SET id_space = ?, id_parent = ?, name = ?, description = ?, view = ?, id_space_icon = ?, document_creation_allowed = ? WHERE id_space = ?  ";
    private static final String SQL_QUERY_SELECT_CHILDS = " SELECT a.id_space, a.id_parent, a.name, a.description, a.view, a.id_space_icon, b.icon_url, a.document_creation_allowed " +
        " FROM document_space a, document_space_icon b " +
        " WHERE a.id_space_icon = b.id_space_icon AND id_parent = ? " + " ORDER BY space_order";
    private static final String SQL_QUERY_SELECTALL = " SELECT a.id_space, a.id_parent, a.name, a.description, a.view, a.id_space_icon, b.icon_url, a.document_creation_allowed " +
        " FROM document_space a, document_space_icon b " + " WHERE a.id_space_icon = b.id_space_icon";
    private static final String SQL_QUERY_SELECTALL_VIEWTYPE = " SELECT code_view , name_key FROM document_view";
    private static final String SQL_QUERY_SELECTALL_ICONS = " SELECT id_space_icon , icon_url FROM document_space_icon";
    private static final String SQL_QUERY_INSERT_DOCUMENT_TYPE = "INSERT INTO document_space_document_type ( id_space , code_document_type ) VALUES ( ? , ? )";
    private static final String SQL_QUERY_DELETE_DOCUMENT_TYPE = " DELETE FROM document_space_document_type WHERE id_space = ?  ";
    private static final String SQL_QUERY_SELECT_DOCUMENT_TYPE = " SELECT code_document_type FROM document_space_document_type WHERE id_space = ?  ";
    private static final String SQL_QUERY_SELECT_SPACE_DOCUMENT_TYPE = " SELECT a.code_document_type, a.name " +
        " FROM document_type a, document_space_document_type b " +
        " WHERE a.code_document_type = b.code_document_type AND b.id_space = ?" + " ORDER BY a.name";

    /**
     * Generates a new primary key
     * @return The new primary key
     */
    private int newPrimaryKey(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
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

    /**
     * Insert a new record in the table.
     *
     * @param space The space object
     */
    public void insert( DocumentSpace space )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        space.setId( newPrimaryKey(  ) );
        daoUtil.setInt( 1, space.getId(  ) );
        daoUtil.setInt( 2, space.getIdParent(  ) );
        daoUtil.setString( 3, space.getName(  ) );
        daoUtil.setString( 4, space.getDescription(  ) );
        daoUtil.setString( 5, space.getViewType(  ) );
        daoUtil.setInt( 6, space.getIdIcon(  ) );
        daoUtil.setInt( 7, space.isDocumentCreationAllowed(  ) ? 1 : 0 );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        // insert allowed document types
        insertAllowedDocumenTypes( space );
    }

    /**
     * Insert allowed document types to a space
     * @param space The space
     */
    private void insertAllowedDocumenTypes( DocumentSpace space )
    {
        String[] doctypes = space.getAllowedDocumentTypes(  );

        for ( int i = 0; i < doctypes.length; i++ )
        {
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_DOCUMENT_TYPE );
            daoUtil.setInt( 1, space.getId(  ) );
            daoUtil.setString( 2, doctypes[i] );

            daoUtil.executeUpdate(  );
            daoUtil.free(  );
        }
    }

    /**
     * Load the data of DocumentSpace from the table
     *
     * @param nDocumentSpaceId The identifier of DocumentSpace
     * @return the instance of the DocumentSpace
     */
    public DocumentSpace load( int nDocumentSpaceId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nDocumentSpaceId );
        daoUtil.executeQuery(  );

        DocumentSpace space = null;

        if ( daoUtil.next(  ) )
        {
            space = new DocumentSpace(  );
            space.setId( daoUtil.getInt( 1 ) );
            space.setIdParent( daoUtil.getInt( 2 ) );
            space.setName( daoUtil.getString( 3 ) );
            space.setDescription( daoUtil.getString( 4 ) );
            space.setViewType( daoUtil.getString( 5 ) );
            space.setIdIcon( daoUtil.getInt( 6 ) );
            space.setIconUrl( daoUtil.getString( 7 ) );
            space.setDocumentCreationAllowed( daoUtil.getInt( 8 ) != 0 );
        }

        daoUtil.free(  );

        if ( space != null )
        {
            loadAllowedDocumentTypes( space );
        }

        return space;
    }

    /**
     * Load allowed document types for a space
     * @param space  The space
     */
    private void loadAllowedDocumentTypes( DocumentSpace space )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_DOCUMENT_TYPE );
        daoUtil.setInt( 1, space.getId(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            space.addAllowedDocumentType( daoUtil.getString( 1 ) );
        }

        daoUtil.free(  );
    }

    /**
     * Delete a record from the table
     *
     * @param nSpaceId The Id to delete
     */
    public void delete( int nSpaceId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nSpaceId );
        daoUtil.executeUpdate(  );

        daoUtil.free(  );
        deleteAllowedDocumentTypes( nSpaceId );
    }

    /**
     * Delete allowed document types
     * @param nSpaceId
     */
    private void deleteAllowedDocumentTypes( int nSpaceId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_DOCUMENT_TYPE );
        daoUtil.setInt( 1, nSpaceId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param space The reference of space
     */
    public void store( DocumentSpace space )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setInt( 1, space.getId(  ) );
        daoUtil.setInt( 2, space.getIdParent(  ) );
        daoUtil.setString( 3, space.getName(  ) );
        daoUtil.setString( 4, space.getDescription(  ) );
        daoUtil.setString( 5, space.getViewType(  ) );
        daoUtil.setInt( 6, space.getIdIcon(  ) );
        daoUtil.setInt( 7, space.isDocumentCreationAllowed(  ) ? 1 : 0 );
        daoUtil.setInt( 8, space.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        deleteAllowedDocumentTypes( space.getId(  ) );
        insertAllowedDocumenTypes( space );
    }

    /**
     * Load the list of documentSpaces
     *
     * @return The Collection of the DocumentSpaces
     * @param nSpaceId
     */
    public List<DocumentSpace> selectChilds( int nSpaceId )
    {
        List<DocumentSpace> listDocumentSpaces = new ArrayList<DocumentSpace>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CHILDS );
        daoUtil.setInt( 1, nSpaceId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DocumentSpace space = new DocumentSpace(  );
            space.setId( daoUtil.getInt( 1 ) );
            space.setIdParent( daoUtil.getInt( 2 ) );
            space.setName( daoUtil.getString( 3 ) );
            space.setDescription( daoUtil.getString( 4 ) );
            space.setViewType( daoUtil.getString( 5 ) );
            space.setIdIcon( daoUtil.getInt( 6 ) );
            space.setIconUrl( daoUtil.getString( 7 ) );
            space.setDocumentCreationAllowed( daoUtil.getInt( 8 ) != 0 );

            listDocumentSpaces.add( space );
        }

        daoUtil.free(  );

        return listDocumentSpaces;
    }

    /**
     * Load the list of documentSpaces
     * @return The Collection of the DocumentSpaces
     */
    public ReferenceList getDocumentSpaceList(  )
    {
        ReferenceList list = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DocumentSpace space = new DocumentSpace(  );
            space.setId( daoUtil.getInt( 1 ) );
            space.setName( daoUtil.getString( 3 ) );

            list.addItem( space.getId(  ), space.getName(  ) );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Load the list of documentSpaces
     * @return The Collection of the DocumentSpaces
     */
    public ReferenceList getViewTypeList( Locale locale )
    {
        ReferenceList list = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_VIEWTYPE );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            String strCodeView = daoUtil.getString( 1 );
            String strViewNameKey = daoUtil.getString( 2 );
            list.addItem( strCodeView, I18nService.getLocalizedString( strViewNameKey, locale ) );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Gets a list of icons available or space customization
     * @return A list of icons
     */
    public ReferenceList getIconsList(  )
    {
        ReferenceList list = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ICONS );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int nIconId = daoUtil.getInt( 1 );
            String strIconUrl = daoUtil.getString( 2 );
            list.addItem( nIconId, strIconUrl );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Select all spaces
     * @return A collection of all spaces.
     */
    public List<DocumentSpace> selectAll(  )
    {
        List<DocumentSpace> listDocumentSpaces = new ArrayList<DocumentSpace>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DocumentSpace space = new DocumentSpace(  );
            space.setId( daoUtil.getInt( 1 ) );
            space.setIdParent( daoUtil.getInt( 2 ) );
            space.setName( daoUtil.getString( 3 ) );
            space.setDescription( daoUtil.getString( 4 ) );
            space.setViewType( daoUtil.getString( 5 ) );
            space.setIdIcon( daoUtil.getInt( 6 ) );
            space.setIconUrl( daoUtil.getString( 7 ) );
            space.setDocumentCreationAllowed( daoUtil.getInt( 8 ) != 0 );

            listDocumentSpaces.add( space );
        }

        daoUtil.free(  );

        return listDocumentSpaces;
    }

    /**
     * Returns all allowed document types for a given space
     * @param nSpaceId The space Id
     * @return Allowed documents types as a ReferenceList
     */
    public ReferenceList getAllowedDocumentTypes( int nSpaceId )
    {
        ReferenceList list = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_SPACE_DOCUMENT_TYPE );
        daoUtil.setInt( 1, nSpaceId );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            list.addItem( daoUtil.getString( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return list;
    }
}
