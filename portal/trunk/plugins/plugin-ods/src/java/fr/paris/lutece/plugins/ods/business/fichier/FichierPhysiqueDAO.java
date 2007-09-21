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
package fr.paris.lutece.plugins.ods.business.fichier;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * Cette classe permet d'accèder/gérer la table ods_fichier_physique
 */
public class FichierPhysiqueDAO implements fr.paris.lutece.plugins.ods.business.fichier.IFichierPhysiqueDAO
{
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_fichier ) FROM ods_fichier_physique ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO ods_fichier_physique (id_fichier, fichier) VALUES ( ?, ?) ";
    private static final String SQL_QUERY_UPDATE = " UPDATE ods_fichier_physique SET fichier = ? WHERE id_fichier= ?  ";
    private static final String SQL_QUERY_FIND_BY_ID_FICHIER = " SELECT * FROM ods_fichier_physique WHERE id_fichier = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM ods_fichier_physique WHERE id_fichier = ?  ";

    /**
    * Crée un nouveau fichier physique à partir de l’objet fichierPhysique passé en paramètre
    * @param fichierPhysique le fichierPhysique à insérer
    * @param plugin le plugin
    * @return l'identifiant du fichier dans la base
    */
    public int insert( FichierPhysique fichierPhysique, Plugin plugin )
    {
        int nNewPrimaryKey = newPrimaryKey( plugin );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        fichierPhysique.setIdFichier( nNewPrimaryKey );
        daoUtil.setInt( 1, fichierPhysique.getIdFichier(  ) );
        daoUtil.setBytes( 2, fichierPhysique.getDonnees(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        return nNewPrimaryKey;
    }

    /**
     * Modifie le fichierPhysique correspondant à l’objet fichierPhysique passé en paramètre
     * @param fichierPhysique le fichierPhysique à modifier
     * @param plugin le plugin
     */
    public void store( FichierPhysique fichierPhysique, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setBytes( 1, fichierPhysique.getDonnees(  ) );
        daoUtil.setInt( 2, fichierPhysique.getIdFichier(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime le fichierPhysique correspondant à l’objet fichierPhysique passé en paramètre
     * @param fichierPhysique le fichierPhysique à supprimer
     * @param plugin le plugin
     */
    public void delete( FichierPhysique fichierPhysique, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, fichierPhysique.getIdFichier(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
    * renvoie le FichierPhysique ayant comme identifiant nKey
    * @param nKey idenfiant du FichierPhysique à supprimer
    * @param plugin le plugin
    * @return le fichier physique identifié par nKey
    */
    public FichierPhysique load( int nKey, Plugin plugin )
    {
        FichierPhysique fichierPhysique = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID_FICHIER, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            fichierPhysique = new FichierPhysique(  );
            fichierPhysique.setIdFichier( daoUtil.getInt( "id_fichier" ) );
            fichierPhysique.setDonnees( daoUtil.getBytes( "fichier" ) );
        }

        daoUtil.free(  );

        return fichierPhysique;
    }

    /**
     * Génère un nouvel identifiant
     * @param plugin le plugin
     * @return Retourne l’identifiant généré
     */
    int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey = 1;

        if ( daoUtil.next(  ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free(  );

        return nKey;
    }
}
