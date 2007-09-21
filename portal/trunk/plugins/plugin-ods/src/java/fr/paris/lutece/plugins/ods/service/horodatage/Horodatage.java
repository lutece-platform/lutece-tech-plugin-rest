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

import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.utilisateur.Utilisateur;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.portal.service.plugin.Plugin;


/**
 *
 * Horodatage
 *
 */
public class Horodatage implements IHorodatage
{
    /**
     * Permet de tracer la publication ou la d�publication d'un pdd
     *
     * @param pdd PDD
     * @param prochaineSeance Seance
     * @param horodatageAcion HorodatageActionEnum
     * @param plugin le plugin actif
     * @return id qui a ete g�n�r�
     */
    public int tracePublicationPDD( PDD pdd, Seance prochaineSeance, HorodatageActionEnum horodatageAcion, Plugin plugin )
    {
        Timestamp dateSeance = prochaineSeance.getDateSeance(  );
        HorodatageFamilleDocumentEnum familleDocument = HorodatageFamilleDocumentEnum.PDD;
        int idFichier = -1;
        String reference = pdd.getReference(  );
        Timestamp datePublication = pdd.getDatePublication(  );
        String intitule = null;
        int version = pdd.getVersion(  );
        HorodatageActionEnum action = horodatageAcion;
        String signature = HorodatageUtils.getSignatures( pdd, plugin );

        return HorodatageHome.createTracePublication( dateSeance, familleDocument, idFichier, reference,
            datePublication, intitule, version, action, signature, plugin, pdd.getIdPdd() );
    }

    /**
     * Permet de tracer la publication ou la d�publication d'un voeu-amendement
     *
     * @param voeuAmendement VoeuAmendement
     * @param prochaineSeance Seance
     * @param horodatageAcion HorodatageActionEnum
     * @param plugin le plugin actif
     * @return id qui a ete g�n�r�
     */
    public int tracePublicationVA( VoeuAmendement voeuAmendement, Seance prochaineSeance,
        HorodatageActionEnum horodatageAcion, Plugin plugin )
    {
        Timestamp dateSeance = prochaineSeance.getDateSeance(  );
        HorodatageFamilleDocumentEnum familleDocument = HorodatageFamilleDocumentEnum.VOEU_AMENDEMENT;
        int idFichier = -1;
        String reference = voeuAmendement.getReference(  );
        Timestamp datePublication = voeuAmendement.getDatePublication(  );
        String intitule = null;
        int version = voeuAmendement.getVersion(  );
        HorodatageActionEnum action = horodatageAcion;
        String signature = HorodatageUtils.getSignature( voeuAmendement, plugin );

        return HorodatageHome.createTracePublication( dateSeance, familleDocument, idFichier, reference,
            datePublication, intitule, version, action, signature, plugin, voeuAmendement.getIdVoeuAmendement() );
    }

    /**
     * Permet de tracer la publication ou la d�publication d'un fichier
     *
     * @param fichier Fichier
     * @param prochaineSeance Seance
     * @param horodatageAcion HorodatageActionEnum
     * @param plugin le plugin actif
     * @return id qui a ete g�n�r�
     */
    public int tracePublicationFichier( Fichier fichier, Seance prochaineSeance, HorodatageActionEnum horodatageAcion,
        Plugin plugin )
    {
        Timestamp dateSeance = prochaineSeance.getDateSeance(  );
        HorodatageFamilleDocumentEnum familleDocument = HorodatageFamilleDocumentEnum.FICHIER;
        int idFichier = fichier.getId(  );
        String reference = null;
        Timestamp datePublication = fichier.getDatePublication(  );
        String intitule = null;
        int version = fichier.getVersion(  );
        HorodatageActionEnum action = horodatageAcion;
        String signature = HorodatageUtils.getSignature( fichier, plugin );

        return HorodatageHome.createTracePublication( dateSeance, familleDocument, idFichier, reference,
            datePublication, intitule, version, action, signature, plugin, fichier.getId() );
    }

    /**
     * Permet de tracer l'envoie de la notification
     *
     * @param dateEnvoi Date d'envoie de la notification
     * @param objet Objet de la notofication
     * @param contenu Contenu de la notification
     * @param user L'utilisateur qui envoie la notification
     * @param mails Liste contenant les mails des destinataires s�par�es par des ;
     * @param plugin le plugin actif
     */
    public void traceNotification( Timestamp dateEnvoi, String objet, String contenu, Utilisateur user, String mails,
        Plugin plugin )
    {
        String signature = HorodatageUtils.getSignature( contenu );
        String idDestinataire = user.getIdUtilisateur(  );
        int idTraceNotification = HorodatageHome.createTraceNotification( objet, contenu, signature, plugin );

        HorodatageHome.createTraceNotificationDestinaire( idTraceNotification, idDestinataire, dateEnvoi, mails, plugin );
    }

    /**
     * Permet de tracer l'envoi de la notification du message uniquement
     *
     * @param objet Objet de la notofication
     * @param contenu Contenu de la notification
     * @param signature la signature md5 du contenu du message
     * @param plugin le plugin actif
     * @return l'identifiant du message notifi�
     */
    public int traceNotificationMessageOnly( String objet, String contenu, String signature, Plugin plugin )
    {
        return HorodatageHome.createTraceNotification( objet, contenu, signature, plugin );
    }

    /**
     * Permet de tracer l'envoi de la notification � l'utilisateur d'un message notifi�
     *
     * @param idTraceNotification identifiant du message notifi�
     * @param idDestinataire identifiant de l'utilisateur
     * @param dateEnvoi date d'envoi du message
     * @param mails List contenant les mails des destinataires
     * @param plugin le plugin actif
     */
    public void traceNotificationUtilisateur( int idTraceNotification, String idDestinataire, Timestamp dateEnvoi,
        String mails, Plugin plugin )
    {
        HorodatageHome.createTraceNotificationDestinaire( idTraceNotification, idDestinataire, dateEnvoi, mails, plugin );
    }

    /**
     * Retourne la date de na derni�re notification
     * @param plugin plugin
     * @return la date de na derni�re notification
     */
    public Timestamp getLastNotification( Plugin plugin )
    {
        return HorodatageHome.findLastNotification( plugin );
    }
}
