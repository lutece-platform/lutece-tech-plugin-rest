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
import fr.paris.lutece.plugins.ods.business.pdd.PDDComparator;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.utilisateur.Utilisateur;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementComparator;
import fr.paris.lutece.plugins.ods.service.horodatage.HorodatageServices;
import fr.paris.lutece.plugins.ods.service.search.ODSSearchService;
import fr.paris.lutece.plugins.ods.service.search.requete.RequeteUtilisateur;
import fr.paris.lutece.plugins.ods.service.search.requete.ResultatRequete;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * ODSNotificationRequetesPersonnelles
 */
public final class ODSNotificationRequetesPersonnelles
{
    private static final String MARK_LIST_RESULTATS = "liste_resultats";
    private static final String TYPE_REQUETE_SIMPLE = "simple";
    private static final String TEMPLATE_NOTIFICATION_PDD = "admin/plugins/ods/notification/notification_requetes_personnelles.html";
    private static final String PROPERTY_ADRESSE_MAIL_OBJET_REQUETE = "ods.notification.mail.requete.objet";
    private static final String SEPARATOR_MAIL_LIST = AppPropertiesService.getProperty( "mail.list.separator" );

    /**
     * Retourne la liste des résultats aux requêtes de l'utilisateur
     * @param listeRequetes la liste de requêtes de l'utilisateur
     * @param tsDateDerniereNotification la date de la dernière notification
     * @param plugin le plugin
     * @return la liste des résultats des requêtes de l'utilisateur
     */
    public static List<ResultatRequete> getDocumentsModifies( List<RequeteUtilisateur> listeRequetes,
        Timestamp tsDateDerniereNotification, Plugin plugin )
    {
        ResultatRequete resultat = null;
        List<ResultatRequete> listeResultats = new ArrayList<ResultatRequete>(  );

        /*
         * Pour chaque requête, on ajoute les résultats des requêtes
         */
        for ( RequeteUtilisateur requete : listeRequetes )
        {
            resultat = null;

            if ( !requete.isRechercheArchive(  ) )
            {
                String strTypeRequete = requete.getTypeRequete(  );

                if ( requete.isNotifie(  ) )
                {
                    if ( strTypeRequete.equals( TYPE_REQUETE_SIMPLE ) )
                    {
                        if ( resultat == null )
                        {
                            resultat = ODSSearchService.getIndexedObjects( requete, plugin );
                        }
                        else
                        {
                            resultat.addAll( ODSSearchService.getIndexedObjects( requete, plugin ) );
                        }
                    }
                    else if ( strTypeRequete.equals( OdsParameters.REFERENCE ) )
                    {
                        if ( resultat == null )
                        {
                            resultat = ODSSearchService.getPDDByReference( requete, plugin );
                        }
                        else
                        {
                            resultat.addAll( ODSSearchService.getPDDByReference( requete, plugin ) );
                        }
                    }
                    else if ( strTypeRequete.equals( OdsParameters.MULTI_CRITERE ) )
                    {
                        if ( resultat == null )
                        {
                            resultat = ODSSearchService.getAllObjectsByCriterias( requete, plugin );
                        }
                        else
                        {
                            resultat.addAll( ODSSearchService.getAllObjectsByCriterias( requete, plugin ) );
                        }
                    }
                }

                /*
                 * On filtre les résultats pour ne garder que ceux dont la date de publication est
                 * supérieure à la date de dernière modification,
                 * et on trie :
                 * - les PDD en fonction de leur référence
                 * - les Voeux et les Amendements  en fonction de leur numéro
                 */

                // Filtre et trie sur la liste des PDDs
                if ( ( resultat != null ) && ( resultat.getListePDDs(  ) != null ) &&
                        !resultat.getListePDDs(  ).isEmpty(  ) )
                {
                    SortedSet<PDD> listePddsTriee = new TreeSet<PDD>( new PDDComparator(  ) );

                    List<PDD> listPDDs = resultat.getListePDDs(  );

                    for ( PDD pdd : listPDDs )
                    {
                        if ( ( tsDateDerniereNotification == null ) ||
                                ( pdd.getDatePublication(  ).compareTo( tsDateDerniereNotification ) > 0 ) )
                        {
                            listePddsTriee.add( pdd );
                        }
                    }

                    resultat.setListePDDs( new ArrayList<PDD>( listePddsTriee ) );
                }

                // Filtre et trie sur la liste des Amendements
                if ( ( resultat != null ) && ( resultat.getListeAmendements(  ) != null ) &&
                        !resultat.getListeAmendements(  ).isEmpty(  ) )
                {
                    SortedSet<VoeuAmendement> listeAmendementsTriee = new TreeSet<VoeuAmendement>( new VoeuAmendementComparator(  ) );

                    List<VoeuAmendement> listAmendements = resultat.getListeAmendements(  );

                    for ( VoeuAmendement amendement : listAmendements )
                    {
                        if ( ( tsDateDerniereNotification == null ) ||
                                ( amendement.getDatePublication(  ).compareTo( tsDateDerniereNotification ) > 0 ) )
                        {
                            listeAmendementsTriee.add( amendement );
                        }
                    }

                    resultat.setListeAmendements( new ArrayList<VoeuAmendement>( listeAmendementsTriee ) );
                }

                // Filtre et trie sur la liste des Voeux
                if ( ( resultat != null ) && ( resultat.getListeVoeux(  ) != null ) &&
                        !resultat.getListeVoeux(  ).isEmpty(  ) )
                {
                    SortedSet<VoeuAmendement> listeVoeuxTriee = new TreeSet<VoeuAmendement>( new VoeuAmendementComparator(  ) );

                    List<VoeuAmendement> listVoeux = resultat.getListeVoeux(  );

                    for ( VoeuAmendement voeu : listVoeux )
                    {
                        if ( ( tsDateDerniereNotification == null ) ||
                                ( voeu.getDatePublication(  ).compareTo( tsDateDerniereNotification ) > 0 ) )
                        {
                            listeVoeuxTriee.add( voeu );
                        }
                    }

                    resultat.setListeVoeux( new ArrayList<VoeuAmendement>( listeVoeuxTriee ) );
                }

                /* On ajoute le resultat de la requête dans la liste des résultats */
                if ( ( resultat != null ) && !resultat.isEmpty(  ) )
                {
                    listeResultats.add( resultat );
                }
            }
        }

        return listeResultats;
    }

    /**
     * Notifie l'utilisateur d'un mail pour lui signaler que des résultats à ses requêtes ont changé
     * @param listeResultats la liste des PDDs modifiés
     * @param user l'utilisateur à notifier
     * @param plugin le plugin
     */
    public static void notifierUtilisateur( List<ResultatRequete> listeResultats, Utilisateur user, Plugin plugin )
    {
        Seance seance = SeanceHome.getProchaineSeance( plugin );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( MARK_LIST_RESULTATS, listeResultats );
        model.put( OdsMarks.MARK_URL_WEBAPP, AppPropertiesService.getProperty( OdsConstants.URL_FRONT ) );
        model.put( Seance.MARK_SEANCE, seance );

        String[] listArguments = 
            {
                DateUtil.getDateString( seance.getDateSeance(  ) ), DateUtil.getDateString( seance.getDateCloture(  ) ),
            };

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_NOTIFICATION_PDD, Locale.getDefault(  ), model );

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
            I18nService.getLocalizedString( PROPERTY_ADRESSE_MAIL_OBJET_REQUETE, listArguments, Locale.getDefault(  ) ),
            template.getHtml(  ) );

        HorodatageServices.traceNotification( OdsUtils.getCurrentDate(  ),
            I18nService.getLocalizedString( PROPERTY_ADRESSE_MAIL_OBJET_REQUETE, listArguments, Locale.getDefault(  ) ),
            template.getHtml(  ), user, listeAdressesMails, plugin );
    }
}
