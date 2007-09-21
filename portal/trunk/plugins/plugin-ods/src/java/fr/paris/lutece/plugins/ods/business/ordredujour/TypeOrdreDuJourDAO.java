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
package fr.paris.lutece.plugins.ods.business.ordredujour;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Cette classe permet d'accèder/gérer la table ods_formation_conseil
 */
public class TypeOrdreDuJourDAO implements ITypeOrdreDuJourDAO
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = " SELECT id_type_odj,libelle_type_odj,libelle_long_type_odj FROM ods_type_ordre_jour WHERE id_type_odj = ? ";
    private static final String SQL_QUERY_TYPE_ODJ_LIST = " SELECT id_type_odj,libelle_type_odj,libelle_long_type_odj FROM ods_type_ordre_jour ";

    /**
     * Retourne le type d'ordtre du jour identifiée par <b>nKey</b>
     * @param nKey l'identifiant du type d'ordtre du jour à charger
     * @param plugin le plugin
     * @return le type d' ordre du jour  identifiée par <b>nKey</b>
     */
    public TypeOrdreDuJour load( int nKey, Plugin plugin )
    {
        TypeOrdreDuJour typeOrdreDuJour = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            typeOrdreDuJour = new TypeOrdreDuJour(  );
            typeOrdreDuJour.setIdTypeOrdreDuJour( daoUtil.getInt( 1 ) );
            typeOrdreDuJour.setLibelle( daoUtil.getString( 2 ) );
            typeOrdreDuJour.setLibelleLong( daoUtil.getString( 3 ) );
        }

        daoUtil.free(  );

        return typeOrdreDuJour;
    }

    /**
     * Retourne la liste des formations de conseil
     * @param plugin le plugin
     * @return le libellé de la formation de conseil
     */
    public List<TypeOrdreDuJour> selectTypeOrdreDuJourList( Plugin plugin )
    {
        List<TypeOrdreDuJour> typeOrdreDuJours = new ArrayList<TypeOrdreDuJour>(  );
        TypeOrdreDuJour typeOrdreDuJour = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_TYPE_ODJ_LIST, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            typeOrdreDuJour = new TypeOrdreDuJour(  );
            typeOrdreDuJour.setIdTypeOrdreDuJour( daoUtil.getInt( 1 ) );
            typeOrdreDuJour.setLibelle( daoUtil.getString( 2 ) );
            typeOrdreDuJour.setLibelleLong( daoUtil.getString( 3 ) );
            typeOrdreDuJours.add( typeOrdreDuJour );
        }

        daoUtil.free(  );

        return typeOrdreDuJours;
    }
}
