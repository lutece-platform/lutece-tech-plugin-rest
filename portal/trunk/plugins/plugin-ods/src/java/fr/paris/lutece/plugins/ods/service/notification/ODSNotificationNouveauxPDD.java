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
import fr.paris.lutece.plugins.ods.business.pdd.PDDFilter;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.utilisateur.Utilisateur;
import fr.paris.lutece.plugins.ods.service.horodatage.HorodatageServices;
import fr.paris.lutece.plugins.ods.service.horodatage.HorodatageUtils;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.sql.Timestamp;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * ODSNotificationNouveauFichier
 */
public final class ODSNotificationNouveauxPDD
{
    private static final String MARK_LISTE_PDDS = "liste_pdds";
    private static final String PROPERTY_ADRESSE_MAIL_OBJET_PDDS = "ods.notification.mail.pdd.objet";
    private static final String TEMPLATE_NOTIFICATION_PDD = "admin/plugins/ods/notification/notification_nouveaux_pdd.html";
    private static final String SEPARATOR_MAIL_LIST = AppPropertiesService.getProperty( "mail.list.separator" );

    /**
     * On recupere la liste des pdds publiés depuis la date de la derniere notification
     * @param tsDateDerniereNotification date de la derniere notification
     * @param plugin plugin
     * @return la liste des pdds publiés depuis la date de la derniere notification
     */
    public static List<PDD> getNouveauxPddPublies( Timestamp tsDateDerniereNotification, Plugin plugin )
    {
        /*
         * Initialisation
         */
        PDDFilter filter = new PDDFilter(  );
        Seance seance = SeanceHome.getProchaineSeance( plugin );

        List<PDD> listePdds = null;

        if ( tsDateDerniereNotification != null )
        {
            filter.setDatePublication( tsDateDerniereNotification );
        }

        filter.setPublication( 1 );
        filter.setProchaineSeance( seance.getIdSeance(  ) );

        /*
         * On récupère la liste des Pdds répondant aux critères suivant :
         *   - appartenants à la prochaine séance
         *   - étant publiés
         *   - dont la date de publication est supérieure à la date de dernière notification (si il y a eu une notification précédente)
         */
        listePdds = PDDHome.findByFilter( filter, plugin );

        return listePdds;
    }

    /** Notifie l'ensemble des utilisateurs présent dans la liste 'listUtilisateurs'
                de la publication des pdds présents dans la liste 'listPdds'
     * @param  listUtilisateurs liste des utilisateurs à notifier
     * @param listPdds liste des pdds à ajouter dans la notification
     * @param plugin plugin
     */
    public static void notifierUtilisateurs( List<Utilisateur> listUtilisateurs, List<PDD> listPdds, Plugin plugin )
    {
        Seance seance = SeanceHome.getProchaineSeance( plugin );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( MARK_LISTE_PDDS, listPdds );
        model.put( OdsMarks.MARK_URL_WEBAPP, AppPropertiesService.getProperty( OdsConstants.URL_FRONT ) );
        model.put( Seance.MARK_SEANCE, seance );

        String[] listArguments = 
            {
                DateUtil.getDateString( seance.getDateSeance(  ) ), DateUtil.getDateString( seance.getDateCloture(  ) ),
            };

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_NOTIFICATION_PDD, Locale.getDefault(  ), model );

        String signature = HorodatageUtils.getSignature( template.getHtml(  ) );

        int idTraceNotification = HorodatageServices.traceNotificationMessageOnly( I18nService.getLocalizedString( 
                    PROPERTY_ADRESSE_MAIL_OBJET_PDDS, listArguments, Locale.getDefault(  ) ), template.getHtml(  ),
                signature, plugin );

        for ( Utilisateur user : listUtilisateurs )
        {
            // Si l'utilisateur a des adresses mails en copie on les ajoute à l'adresse principale 
            // en le séparant par un point-virgule dans l'adresse des destinataires
            String listeAdressesMails = OdsConstants.CONSTANTE_CHAINE_VIDE;

            if ( user.getMail(  ) != null )
            {
                listeAdressesMails += user.getMail(  ).trim(  );
            }

            if ( user.getMailCopie1(  ) != null )
            {
                if ( !listeAdressesMails.trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    listeAdressesMails += ( SEPARATOR_MAIL_LIST );
                }

                listeAdressesMails += ( user.getMailCopie1(  ) );
            }

            if ( user.getMailCopie2(  ) != null )
            {
                if ( !listeAdressesMails.trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    listeAdressesMails += ( SEPARATOR_MAIL_LIST );
                }

                listeAdressesMails += ( user.getMailCopie2(  ) );
            }

            // Envoi du mail
            MailService.sendMail( listeAdressesMails, null, MailService.getNoReplyEmail(  ),
                I18nService.getLocalizedString( PROPERTY_ADRESSE_MAIL_OBJET_PDDS, listArguments, Locale.getDefault(  ) ),
                template.getHtml(  ) );

            HorodatageServices.traceNotificationUtilisateur( idTraceNotification, user.getIdUtilisateur(  ),
                OdsUtils.getCurrentDate(  ), listeAdressesMails, plugin );
        }
    }
}
