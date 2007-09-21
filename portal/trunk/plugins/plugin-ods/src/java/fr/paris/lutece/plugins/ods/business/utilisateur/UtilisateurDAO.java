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
package fr.paris.lutece.plugins.ods.business.utilisateur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;


/**
 * UtilisateurDAO
 */
public class UtilisateurDAO implements IUtilisateurDAO
{
    /*
     * Requetes SQL d'accès à la table ods_utilisateur
     */
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_utilisateur( id_utilisateur, nom_utilisateur, prenom_utilisateur, " +
        " 			email_utilisateur, email_1_copie, email_2_copie, " +
        "           derniere_connexion, dernier_id_session ) " + " VALUES( ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ods_utilisateur WHERE id_utilisateur = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ods_utilisateur " +
        " SET nom_utilisateur = ?, prenom_utilisateur = ?, " +
        " 			email_utilisateur = ?, email_1_copie = ?, email_2_copie = ?, " +
        "           derniere_connexion = ?, dernier_id_session = ? " + " WHERE id_utilisateur = ? ";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_utilisateur, nom_utilisateur, prenom_utilisateur, " +
        " 			email_utilisateur, email_1_copie, email_2_copie, " + "           derniere_connexion, dernier_id_session " +
        " FROM ods_utilisateur user " + " WHERE user.id_utilisateur=? ";
    private static final String SQL_QUERY_USER_LIST = "SELECT id_utilisateur, nom_utilisateur, prenom_utilisateur, " +
        " 			email_utilisateur, email_1_copie, email_2_copie, " + "           derniere_connexion, dernier_id_session " +
        " FROM ods_utilisateur user ";

    /**
     * Rajoute un utilisateur dans la base.
     * @param user le nouvel utilisateur
     * @param plugin le Plugin actif
     */
    public void insert( Utilisateur user, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setString( 1, user.getIdUtilisateur(  ) );
        daoUtil.setString( 2, user.getNom(  ) );
        daoUtil.setString( 3, user.getPrenom(  ) );
        daoUtil.setString( 4, user.getMail(  ) );
        daoUtil.setString( 5, user.getMailCopie1(  ) );
        daoUtil.setString( 6, user.getMailCopie2(  ) );
        daoUtil.setTimestamp( 7, user.getDerniereConnexion(  ) );
        daoUtil.setString( 8, user.getDerniereIdSession(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime de la table ods_utilisateur l'utilisateur passé en argument.
     * @param user l'utilisateur à supprimer
     * @param plugin le Plugin actif
     */
    public void delete( Utilisateur user, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setString( 1, user.getIdUtilisateur(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Met à jour les informations concernant l'utilisateur passé en argument.
     * @param user les nouvelles informations sur l'utilisateur
     * @param plugin le Plugin actif
     */
    public void store( Utilisateur user, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        daoUtil.setString( 1, user.getNom(  ) );
        daoUtil.setString( 2, user.getPrenom(  ) );
        daoUtil.setString( 3, user.getMail(  ) );
        daoUtil.setString( 4, user.getMailCopie1(  ) );
        daoUtil.setString( 5, user.getMailCopie2(  ) );

        if ( ( user.getDerniereConnexion(  ) != null ) && ( user.getDerniereIdSession(  ) != null ) )
        {
            daoUtil.setTimestamp( 6, user.getDerniereConnexion(  ) );
            daoUtil.setString( 7, user.getDerniereIdSession(  ) );
        }

        daoUtil.setString( 8, user.getIdUtilisateur(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne l'utilisateur identifié par nKey
     * @param nKey l'identifiant de l'utilisateur recherché
     * @param plugin le Plugin actif
     * @return l'utilisateur identifié par nKey
     */
    public Utilisateur load( String nKey, Plugin plugin )
    {
        Utilisateur user = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setString( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            user = new Utilisateur(  );

            user.setIdUtilisateur( nKey );
            user.setNom( daoUtil.getString( "nom_utilisateur" ) );
            user.setPrenom( daoUtil.getString( "prenom_utilisateur" ) );
            user.setMail( daoUtil.getString( "email_utilisateur" ) );
            user.setMailCopie1( daoUtil.getString( "email_1_copie" ) );
            user.setMailCopie2( daoUtil.getString( "email_2_copie" ) );
            user.setDerniereConnexion( daoUtil.getTimestamp( "derniere_connexion" ) );
            user.setDerniereIdSession( daoUtil.getString( "dernier_id_session" ) );
        }

        daoUtil.free(  );

        return user;
    }

    /**
     * Retourne la liste des utilisateurs de la base.
     * @param plugin le Plugin actif
     * @return la liste des utilisateurs de la base
     */
    public List<Utilisateur> getListUtilisateurs( Plugin plugin )
    {
        List<Utilisateur> listUtilisateurs = new ArrayList<Utilisateur>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_USER_LIST, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Utilisateur user = new Utilisateur(  );

            user.setIdUtilisateur( daoUtil.getString( "id_utilisateur" ) );
            user.setNom( daoUtil.getString( "nom_utilisateur" ) );
            user.setPrenom( daoUtil.getString( "prenom_utilisateur" ) );
            user.setMail( daoUtil.getString( "email_utilisateur" ) );
            user.setMailCopie1( daoUtil.getString( "email_1_copie" ) );
            user.setMailCopie2( daoUtil.getString( "email_2_copie" ) );
            user.setDerniereConnexion( daoUtil.getTimestamp( "derniere_connexion" ) );
            user.setDerniereIdSession( daoUtil.getString( "dernier_id_session" ) );

            listUtilisateurs.add( user );
        }

        daoUtil.free(  );

        return listUtilisateurs;
    }

    /**
     * Retourne une hashmap contenant la liste des utilisateurs de la base.
     * @param plugin le Plugin actif
     * @return une hashmap contenant la liste des utilisateurs de la base
     */
    public Map<String, Utilisateur> getHashmapUtilisateurs( Plugin plugin )
    {
        HashMap<String, Utilisateur> hmapUtilisateurs = new HashMap<String, Utilisateur>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_USER_LIST, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Utilisateur user = new Utilisateur(  );

            user.setIdUtilisateur( daoUtil.getString( "id_utilisateur" ) );
            user.setNom( daoUtil.getString( "nom_utilisateur" ) );
            user.setPrenom( daoUtil.getString( "prenom_utilisateur" ) );
            user.setMail( daoUtil.getString( "email_utilisateur" ) );
            user.setMailCopie1( daoUtil.getString( "email_1_copie" ) );
            user.setMailCopie2( daoUtil.getString( "email_2_copie" ) );
            user.setDerniereConnexion( daoUtil.getTimestamp( "derniere_connexion" ) );
            user.setDerniereIdSession( daoUtil.getString( "dernier_id_session" ) );

            hmapUtilisateurs.put( user.getIdUtilisateur(  ), user );
        }

        daoUtil.free(  );

        return hmapUtilisateurs;
    }
}
