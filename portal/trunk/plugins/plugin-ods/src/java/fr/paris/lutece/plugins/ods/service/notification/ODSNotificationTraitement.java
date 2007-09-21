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
package fr.paris.lutece.plugins.ods.service.notification;

import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.utilisateur.Utilisateur;
import fr.paris.lutece.plugins.ods.service.horodatage.HorodatageServices;
import fr.paris.lutece.plugins.ods.service.manager.LuteceUserManager;
import fr.paris.lutece.plugins.ods.service.search.requete.RequeteUtilisateur;
import fr.paris.lutece.plugins.ods.service.search.requete.RequeteUtilisateurHome;
import fr.paris.lutece.plugins.ods.service.search.requete.ResultatRequete;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.sql.Timestamp;

import java.util.List;


/**
 * ODSNotificationTraitement
 */
public class ODSNotificationTraitement
{
    /**
     * Lance la notification.
     * 
     * @param plugin le plugin
     */
    public static void run( Plugin plugin )
    {
        AppLogService.info( "odsNotification - start of process." );

        /*
         * On récupère la date de dernière exécution du démon de notification.
         */
        Timestamp tsDateDerniereNotification = HorodatageServices.getLastNotification( plugin );

        /*
         * On récupère la liste de tous les utilisateurs
         */
        List<Utilisateur> listeUtilisateurs = LuteceUserManager.getUserFrontList( plugin );

        /*
         * Notification des nouveaux projets et propositions de délibération.
         */
        List<PDD> listePdds = ODSNotificationNouveauxPDD.getNouveauxPddPublies( tsDateDerniereNotification, plugin );

        if ( ( listePdds != null ) && !listePdds.isEmpty(  ) )
        {
            ODSNotificationNouveauxPDD.notifierUtilisateurs( listeUtilisateurs, listePdds, plugin );
        }

        /*
         * Notification des nouveaux PDD, voeux et amendements de la prochaine séance
         * répondants aux requêtes personnelles.
         */
        List<ResultatRequete> listeResultats;

        for ( Utilisateur user : listeUtilisateurs )
        {
            listeResultats = null;

            List<RequeteUtilisateur> listeRequetes = RequeteUtilisateurHome.findByUser( user.getIdUtilisateur(  ),
                    plugin );

            if ( ( listeRequetes != null ) && !listeRequetes.isEmpty(  ) )
            {
                listeResultats = ODSNotificationRequetesPersonnelles.getDocumentsModifies( listeRequetes,
                        tsDateDerniereNotification, plugin );
            }

            if ( ( listeResultats != null ) && !listeResultats.isEmpty(  ) )
            {
                ODSNotificationRequetesPersonnelles.notifierUtilisateur( listeResultats, user, plugin );
            }
        }

        AppLogService.info( "odsNotification - end of process." );
    }
}
