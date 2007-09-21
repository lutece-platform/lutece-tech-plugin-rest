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
package fr.paris.lutece.plugins.document.business.workflow;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class porvides Data Access methods for DocumentActionDAO  objects
 */
public class DocumentActionDAO implements IDocumentActionDAO
{
    private static final String SQL_QUERY_SELECT_ACTIONS = "SELECT a.name_key, a.description_key, a.action_url, a.icon_url, a.action_permission " +
        " FROM document_workflow_action a, document_workflow_transition b " +
        " WHERE a.id_action = b.id_action AND b.id_state = ? ";

    /**
     * Load the list of actions for a document
     *
     * @return The Collection of actions
     * @param document The document to add available actions
     */
    public List<DocumentAction> selectActions( Document document )
    {
        List<DocumentAction> listActions = new ArrayList<DocumentAction>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ACTIONS );
        daoUtil.setInt( 1, document.getStateId(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DocumentAction action = new DocumentAction(  );
            action.setNameKey( daoUtil.getString( 1 ) );
            action.setDescriptionKey( daoUtil.getString( 2 ) );
            action.setUrl( daoUtil.getString( 3 ) );
            action.setIconUrl( daoUtil.getString( 4 ) );
            action.setPermission( daoUtil.getString( 5 ) );

            listActions.add( action );
        }

        daoUtil.free(  );

        return listActions;
    }
}
