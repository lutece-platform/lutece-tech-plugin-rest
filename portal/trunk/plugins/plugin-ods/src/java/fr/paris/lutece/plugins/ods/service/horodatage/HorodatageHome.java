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

import java.sql.Timestamp;
import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;


/**
 *
 * HorodatageHome
 *
 */
public class HorodatageHome
{
    private static IHorodatageDAO _dao = (IHorodatageDAO) SpringContextService.getPluginBean( "ods", "horodatageDAO" );

    /**
         * Permet de tracé la publication ou la dépublication d'un pdd
         *
         * @param dateSeance Date du premier jour de la séance
         * @param familleDocument Famille de documents
         * @param idFichier Identifiant du document dans la base de données ODS
         * @param reference Référence (uniquement pour pdd, vœux et amendements )
         * @param datePublication Date de publication du document
         * @param intitule Intitulé (uniquement pour les fichiers)
         * @param version Numéro de version (pdd, vœu-amendement, fichier)
         * @param action Type d'action effectuée (publication ou dépublication)
         * @param signature Liste des signatures numériques SHA224 des fichiers (les signatures sont séparées par le caractère '-')
         * @param plugin le plugin actif
         * @param id identifiant du pdd ou du va dans la base de données
         * @return id qui a ete généré
         */
    public static int createTracePublication( Timestamp dateSeance, HorodatageFamilleDocumentEnum familleDocument,
        int idFichier, String reference, Timestamp datePublication, String intitule, int version,
        HorodatageActionEnum action, String signature, Plugin plugin, int id )
    {
        return _dao.insertTracePublication( dateSeance, familleDocument, idFichier, reference, datePublication,
            intitule, version, action, signature, plugin, id );
    }

    /**
     * Permet de tracé l'envoie de la notification
     *
     * @param objet Objet du mail
     * @param contenu Contenu du message
     * @param signature Signature SHA224 du contenu du mail
     * @param plugin le plugin actif
     * @return id qui a ete généré
     */
    public static int createTraceNotification( String objet, String contenu, String signature, Plugin plugin )
    {
        return _dao.insertTraceNotification( objet, contenu, signature, plugin );
    }

    /**
     * Permet de tracé l'envoie de la notification des destinataires
     *
     * @param idTraceNotification Clé étrangère vers le champ id_trace de la table ods_horodatage_notifications
     * @param idDestinataire Clé étrangère vers le champ id_utilisateur de la table ods_utilisateur
     * @param dateEnvoi Date d'envoi du mail de notification
     * @param mails Liste des destinataires (séparés par le caractère ';') liés à un même utilisateur
     * @param plugin le plugin actif
     */
    public static void createTraceNotificationDestinaire( int idTraceNotification, String idDestinataire,
        Timestamp dateEnvoi, String mails, Plugin plugin )
    {
        _dao.insertTraceNotificationDestinaire( idTraceNotification, idDestinataire, dateEnvoi, mails, plugin );
    }

    /**
     * Retourne la date de na dernière notification
     * 
     * @param plugin Le plugin
     * @return la date de na dernière notification
     */
    public static Timestamp findLastNotification( Plugin plugin )
    {
        return _dao.selectLastNotification( plugin );
    }

    /**
     * Retourne une liste de traces dont la date d'ajout est comprise entre dateStart et dateEnd et dont le type est typeHorodatage
     *
     * @param plugin le plugin actif
     * @param typeHorodatage Type PUBLICATION ou NOTIFICATION
     * @param dateStart Filtre sur la date de début des traces
    * @param dateEnd Filtre sur la date de fin des traces
     * @return Liste de traces
     */
    public static List<Trace> findAllTrace( Plugin plugin, String typeHorodatage, Timestamp dateStart, Timestamp dateEnd )
    {
        return _dao.selectAllTraces( plugin, typeHorodatage, dateStart, dateEnd );
    }
}
