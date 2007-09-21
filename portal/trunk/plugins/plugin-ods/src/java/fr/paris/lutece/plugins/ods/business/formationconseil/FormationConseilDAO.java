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
package fr.paris.lutece.plugins.ods.business.formationconseil;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Cette classe permet d'accèder/gérer la table ods_formation_conseil
 */
public class FormationConseilDAO implements IFormationConseilDAO
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = " SELECT id_formation_conseil,libelle_formation_conseil FROM ods_formation_conseil WHERE id_formation_conseil = ? ";
    private static final String SQL_QUERY_FORMATION_CONSEIL_LIST = " SELECT id_formation_conseil,libelle_formation_conseil  FROM ods_formation_conseil";

    /**
     * Retourne la formation de conseil identifiée par <b>nKey</b>
     * @param nKey l'identifiant de la formation de conseil à charger
     * @param plugin le plugin
     * @return la formation de conseil identifiée par <b>nKey</b>
     */
    public FormationConseil load( int nKey, Plugin plugin )
    {
        FormationConseil formationConseil = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            formationConseil = new FormationConseil(  );
            formationConseil.setIdFormationConseil( daoUtil.getInt( 1 ) );
            formationConseil.setLibelle( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return formationConseil;
    }

    /**
     * Retourne la liste des formations de conseil
     * @param plugin le plugin
     * @return le libellé de la formation de conseil
     */
    public List<FormationConseil> selectFormationConseilList( Plugin plugin )
    {
        List<FormationConseil> formationConseils = new ArrayList<FormationConseil>(  );
        FormationConseil formationConseil = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FORMATION_CONSEIL_LIST, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            formationConseil = new FormationConseil(  );
            formationConseil.setIdFormationConseil( daoUtil.getInt( 1 ) );
            formationConseil.setLibelle( daoUtil.getString( 2 ) );
            formationConseils.add( formationConseil );
        }

        daoUtil.free(  );

        return formationConseils;
    }
}
