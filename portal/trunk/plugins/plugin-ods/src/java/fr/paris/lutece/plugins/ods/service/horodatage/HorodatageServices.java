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
package fr.paris.lutece.plugins.ods.service.horodatage;

import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.utilisateur.Utilisateur;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.sql.Timestamp;


/**
 *
 * HorodatageServices
 *
 */
public class HorodatageServices
{
    private static IHorodatage _horodatage = (IHorodatage) SpringContextService.getPluginBean( "ods", "horodatage" );

    /**
     * Permet de tracer la publication ou la dépublication d'un pdd
     *
     * @param pdd PDD
     * @param prochaineSeance Seance
     * @param horodatageAcion HorodatageActionEnum
     * @param plugin le plugin actif
     * @return id qui a ete généré
     */
    public static int tracePublicationPDD( PDD pdd, Seance prochaineSeance, HorodatageActionEnum horodatageAcion,
        Plugin plugin )
    {
        return _horodatage.tracePublicationPDD( pdd, prochaineSeance, horodatageAcion, plugin );
    }

    /**
     * Permet de tracer la publication ou la dépublication d'un voeu-amendement
     *
     * @param voeuAmendement VoeuAmendement
     * @param prochaineSeance Seance
     * @param horodatageAcion HorodatageActionEnum
     * @param plugin le plugin actif
     * @return id qui a ete généré
     */
    public static int tracePublicationVA( VoeuAmendement voeuAmendement, Seance prochaineSeance,
        HorodatageActionEnum horodatageAcion, Plugin plugin )
    {
        return _horodatage.tracePublicationVA( voeuAmendement, prochaineSeance, horodatageAcion, plugin );
    }

    /**
     * Permet de tracer la publication ou la dépublication d'un fichier
     *
     * @param fichier Fichier
     * @param prochaineSeance Seance
     * @param horodatageAcion HorodatageActionEnum
     * @param plugin le plugin actif
     * @return id qui a ete généré
     */
    public static int tracePublicationFichier( Fichier fichier, Seance prochaineSeance,
        HorodatageActionEnum horodatageAcion, Plugin plugin )
    {
        return _horodatage.tracePublicationFichier( fichier, prochaineSeance, horodatageAcion, plugin );
    }

    /**
     * Permet de tracer l'envoi de la notification du message à l'utilisateur
     *
     * @param dateEnvoi Date d'envoie de la notification
     * @param objet Objet de la notofication
     * @param contenu Contenu de la notification
     * @param user L'utilisateur qui envoie la notification
     * @param mails List contenant les mails des destinataires
     * @param plugin le plugin actif
     */
    public static void traceNotification( Timestamp dateEnvoi, String objet, String contenu, Utilisateur user,
        String mails, Plugin plugin )
    {
        _horodatage.traceNotification( dateEnvoi, objet, contenu, user, mails, plugin );
    }

    /**
     * Permet de tracer l'envoi de la notification du message uniquement
     *
     * @param objet Objet de la notofication
     * @param contenu Contenu de la notification
     * @param signature la signature md5 du contenu du message
     * @param plugin le plugin actif
     * @return l'identifiant du message notifié
     */
    public static int traceNotificationMessageOnly( String objet, String contenu, String signature, Plugin plugin )
    {
        return _horodatage.traceNotificationMessageOnly( objet, contenu, signature, plugin );
    }

    /**
     * Permet de tracer l'envoi de la notification à l'utilisateur d'un message notifié
     *
     * @param idTraceNotification identifiant du message notifié
     * @param idDestinataire identifiant de l'utilisateur
     * @param dateEnvoi date d'envoi du message
     * @param mails List contenant les mails des destinataires
     * @param plugin le plugin actif
     */
    public static void traceNotificationUtilisateur( int idTraceNotification, String idDestinataire,
        Timestamp dateEnvoi, String mails, Plugin plugin )
    {
        _horodatage.traceNotificationUtilisateur( idTraceNotification, idDestinataire, dateEnvoi, mails, plugin );
    }

    /**
     * Retourne la date de na dernière notification
     * @param plugin le plugin actif
     * @return la date de na dernière notification
     */
    public static Timestamp getLastNotification( Plugin plugin )
    {
        return _horodatage.getLastNotification( plugin );
    }
}
