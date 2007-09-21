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
package fr.paris.lutece.plugins.ods.service.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.mylutece.modules.wssodatabase.authentication.business.WssoUser;
import fr.paris.lutece.plugins.mylutece.modules.wssodatabase.authentication.business.WssoUserHome;
import fr.paris.lutece.plugins.ods.business.utilisateur.Utilisateur;
import fr.paris.lutece.plugins.ods.business.utilisateur.UtilisateurHome;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;


/**
 * LuteceUserManager
 */
public class LuteceUserManager
{
    private static final String MESSAGE_CURRENT_USER_NULL = "ods.front.utilisateur.message.userNull";
    private static final String PROPERTY_SYNCHRONIZE_UTILISATEUR = "odsNotification.synchronizeUser";

    /**
     * Effectue les traitements pour la gestion des utilisateurs sur le front
     * @param model la table de hashage qui va être envoyé à la template freemarker
     * @param request la requête HTTP
     * @param plugin le plugin
     */
    public static void gestionUtilisateur( Map<Object, Object> model, HttpServletRequest request, Plugin plugin )
    {
        gestionConnexion( request, plugin );

        Utilisateur utilisateur = (Utilisateur) request.getSession(  ).getAttribute( OdsMarks.MARK_UTILISATEUR );

        model.put( OdsMarks.MARK_USER_GIVEN_NAME, getGivenName( request ) );

        if ( utilisateur != null )
        {
            model.put( OdsMarks.MARK_DERNIERE_CONNEXION, utilisateur.getDerniereConnexion(  ) );
        }
    }

    /**
     * Retourne le nom de l'utilisateur connecté
     * @param request la requete
     * @return le nom de l'utilisateur connecté
     */
    public static String getGivenName( HttpServletRequest request )
    {
        LuteceUser currentUser = SecurityService.getInstance(  ).getRegisteredUser( request );

        if ( currentUser != null )
        {
            return currentUser.getUserInfo( LuteceUser.NAME_GIVEN ) + " " +
            currentUser.getUserInfo( LuteceUser.NAME_FAMILY );
        }

        return OdsConstants.CONSTANTE_CHAINE_VIDE;
    }

    /**
     * Gere la derniere connexion d'un utilisateur en base et en session
     * @param request la requpête Http
     * @param plugin la Plugin
     */
    public static void gestionConnexion( HttpServletRequest request, Plugin plugin )
    {
        Utilisateur utilisateur = getUtilisateurCourant( request, plugin );

        if ( ( utilisateur != null ) && ( utilisateur.getDerniereIdSession(  ) != null ) &&
                ( request.getSession(  ) != null ) &&
                !utilisateur.getDerniereIdSession(  ).equals( request.getSession(  ).getId(  ) ) )
        {
            request.getSession(  ).setAttribute( OdsMarks.MARK_UTILISATEUR, utilisateur );
            
            Utilisateur utilisateurMaj = new Utilisateur( utilisateur );
            utilisateurMaj.setDerniereConnexion( OdsUtils.getCurrentDate(  ) );
            utilisateurMaj.setDerniereIdSession( request.getSession(  ).getId(  ) );

            UtilisateurHome.update( utilisateurMaj, plugin );
        }
    }

    /**
     * Récupère l'utilisateur courant et le crée si il n'existe pas.
     * @param request la requête Http
     * @param plugin la Plugin
     * @return l'utilisateur courant
     */
    public static Utilisateur getUtilisateurCourant( HttpServletRequest request, Plugin plugin )
    {
        String strIdUser = "-1";
        LuteceUser currentUser = SecurityService.getInstance(  ).getRegisteredUser( request );

        if ( currentUser != null )
        {
            strIdUser = currentUser.getName(  );
        }

        if ( strIdUser.equals( "-1" ) )
        {
            AppLogService.error( MESSAGE_CURRENT_USER_NULL );

            return null;
        }

        Utilisateur utilisateur = UtilisateurHome.findByPrimaryKey( strIdUser, plugin );

        if ( utilisateur == null )
        {
            utilisateur = new Utilisateur(  );

            utilisateur.setIdUtilisateur( currentUser.getName(  ) );
            utilisateur.setNom( currentUser.getUserInfo( LuteceUser.NAME_FAMILY ) );
            utilisateur.setPrenom( currentUser.getUserInfo( LuteceUser.NAME_GIVEN ) );
            utilisateur.setMail( currentUser.getUserInfo( LuteceUser.BUSINESS_INFO_ONLINE_EMAIL ) );
            utilisateur.setMailCopie1( null );
            utilisateur.setMailCopie2( null );
            utilisateur.setDerniereConnexion( null );
            utilisateur.setDerniereIdSession( "0" );

            UtilisateurHome.create( utilisateur, plugin );
        }

        return utilisateur;
    }

    /**
     * retourne la liste des utilisateurs du front
     * @param plugin plugin
     * @return la liste des utilisateurs
     */
    public static List<Utilisateur> getUserFrontList( Plugin plugin )
    {
        Map<String, Utilisateur> hmapUtilisateurs = UtilisateurHome.findUserListHashmap( plugin );
        List<Utilisateur> listeRetour = new ArrayList<Utilisateur>(  );
        Utilisateur user;

        for ( Object obj : WssoUserHome.findWssoUsersList( null ) )
        {
            user = null;

            if ( ( obj != null ) && ( obj instanceof WssoUser ) )
            {
                user = hmapUtilisateurs.get( ( (WssoUser) obj ).getGuid(  ) );
            }

            if ( user == null )
            {
                user = new Utilisateur(  );

                user.setIdUtilisateur( ( (WssoUser) obj ).getGuid(  ) );
                user.setNom( ( (WssoUser) obj ).getLastName(  ) );
                user.setPrenom( ( (WssoUser) obj ).getFirstName(  ) );
                user.setMail( ( (WssoUser) obj ).getEmail(  ) );
                user.setMailCopie1( null );
                user.setMailCopie2( null );

                String strSynchronizeUtilisateur = AppPropertiesService.getProperty( PROPERTY_SYNCHRONIZE_UTILISATEUR );

                if ( ( strSynchronizeUtilisateur != null ) &&
                        strSynchronizeUtilisateur.equals( OdsConstants.INTEGER_TRUE ) )
                {
                    UtilisateurHome.create( user, plugin );
                }
            }

            listeRetour.add( user );
        }

        return listeRetour;
    }
}
