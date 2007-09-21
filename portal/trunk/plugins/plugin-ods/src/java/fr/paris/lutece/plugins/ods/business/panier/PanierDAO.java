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
package fr.paris.lutece.plugins.ods.business.panier;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;


/**
 * Permet l'accès aux données de la table ods_panier.
 */
public class PanierDAO implements IPanierDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_element_panier ) FROM ods_panier ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_panier (id_element_panier, id_document, id_utilisateur, date_ajout, est_zip) " +
        " VALUES (?,?,?,NOW(),?) ";
    private static final String SQL_QUERY_DELETE_ELEMENTS_PANIER = "DELETE FROM ods_panier WHERE id_utilisateur = ? AND id_document = ? AND est_zip = false ";
    private static final String SQL_QUERY_FIND_ELEMENTS_PANIER_BY_USER = "SELECT * FROM ods_panier WHERE id_utilisateur = ? AND est_zip = false ORDER BY date_ajout ASC ";
    private static final String SQL_QUERY_FIND_ZIP_BY_USER = "SELECT * FROM ods_panier WHERE id_utilisateur = ? AND est_zip = true ORDER BY date_ajout ASC ";
    private static final String SQL_QUERY_FIND_ZIP_BY_DATE_AJOUT = "SELECT * FROM ods_panier WHERE est_zip = true AND date_ajout = ? ";
    private static final String SQL_QUERY_DELETE_ALL = "DELETE FROM ods_panier ";

    /**
     * Création de l'element du panier qui possede l'id de l'utilisateur et l'id du fichier passé en paramètre
     * @param idUser id du user
     * @param idFichier id du fichier
     * @param plugin le plugin actif
     */
    public void createElementPanier( String idUser, int idFichier, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setInt( 1, newPrimaryKey( plugin ) );
        daoUtil.setInt( 2, idFichier );
        daoUtil.setString( 3, idUser );
        daoUtil.setBoolean( 4, false );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime l'element du panier qui possede l'id de l'utilisateur et l'id du fichier passé en paramètre
     * @param idUser id du user
     * @param idFichier id du fichier
     * @param plugin le plugin actif
     */
    public void deleteElementPanier( String idUser, int idFichier, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ELEMENTS_PANIER, plugin );
        daoUtil.setString( 1, idUser );
        daoUtil.setInt( 2, idFichier );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne Tableau contenant les ids des éléments du panier appartenant au user passé en paramètre
     *
     * @param idUser id du user
     * @param plugin le plugin actif
     * @return Tableau contenant les ids des éléments du panier appartenant au user passé en paramètre
     */
    public String[] selectElementsPanierByIdUser( String idUser, Plugin plugin )
    {
        List<String> listIds = new ArrayList<String>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ELEMENTS_PANIER_BY_USER, plugin );
        daoUtil.setString( 1, idUser );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            int idFichier = daoUtil.getInt( "id_document" );
            listIds.add( "" + idFichier );
        }

        daoUtil.free(  );

        String[] ids = new String[listIds.size(  )];
        int i = 0;

        for ( String s : listIds )
        {
            ids[i] = s;
            i++;
        }

        return ids;
    }

    /**
    * Retourne une nouvelle clé primaire unique sur la table ods_panier
    * @param plugin le plugin actif
    * @return nouvelle clé primaire
    */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
     * Retourne toutes les archives de l'utilisateur passé en paramètre
     *
     * @param idUser id du user
     * @param plugin le plugin actif
     * @return Liste des zip - List<Panier> -qui ont été téléchargé par le user passé en paramètre
     */
    public List<Panier> selectZipByIdUser( String idUser, Plugin plugin )
    {
        List<Panier> paniers = new ArrayList<Panier>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ZIP_BY_USER, plugin );
        daoUtil.setString( 1, idUser );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Panier panier = new Panier(  );
            panier.setIdElementPanier( daoUtil.getInt( "id_element_panier" ) );
            panier.setIdDocument( daoUtil.getInt( "id_document" ) );
            panier.setIdUtilisateur( daoUtil.getString( "id_utilisateur" ) );
            panier.setDateAjout( daoUtil.getTimestamp( "date_ajout" ) );
            paniers.add( panier );
        }

        daoUtil.free(  );

        return paniers;
    }

    /**
     * Supprime tous les documents.
     *
     * @param plugin le plugin actif
     */
    public void deleteAll( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ALL, plugin );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne l'archive ZIP qui à été téléchargé a la date passé en paramètre
     *
     * @param dateAjout Date qui correspond à la demande du téléchargement de l'archive
     * @param plugin le plugin actif
     * @return L'element du Panier
     */
    public Panier loadZipByDateAjout( String dateAjout, Plugin plugin )
    {
        Panier panier = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ZIP_BY_DATE_AJOUT, plugin );
        daoUtil.setTimestamp( 1, new Timestamp( Long.parseLong( dateAjout ) ) );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            panier = new Panier(  );
            panier.setIdElementPanier( daoUtil.getInt( "id_element_panier" ) );
            panier.setIdDocument( daoUtil.getInt( "id_document" ) );
            panier.setIdUtilisateur( daoUtil.getString( "id_utilisateur" ) );
            panier.setDateAjout( daoUtil.getTimestamp( "date_ajout" ) );
        }

        daoUtil.free(  );

        return panier;
    }
}
