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
package fr.paris.lutece.plugins.ods.service;

import fr.paris.lutece.plugins.ods.service.notification.ODSNotificationJob;
import fr.paris.lutece.plugins.ods.service.panier.ODSVidePanierJob;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;

import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;


/**
 * PluginODSImplementation
 */
public class PluginODSImplementation extends Plugin
{
    private static final String NOTIFICATION_TRIGGER_NAME = "TriggerNotification";
    private static final String NOTIFICATION_HEURE_EXECUTION = "daemon.odsNotification.heureExecution";
    private static final String NOTIFICATION_NOM_JOB = "daemon.odsNotification.name";
    private static final String NOTIFICATION_ACTIF = "daemon.odsNotification.actif";
    private static final String VIDANGE_PANIER_TRIGGER_NAME = "TriggerVidePanier";
    private static final String VIDANGE_PANIER_HEURE_EXECUTION = "daemon.odsVidePanier.heureExecution";
    private static final String VIDANGE_PANIER_NOM_JOB = "daemon.odsVidePanier.name";
    private static final String VIDANGE_PANIER_ACTIF = "daemon.odsVidePanier.actif";

    /**
     *
     */
    public void init(  )
    {
        // On crée une instance de factory de scheduler 
        SchedulerFactory schedFact = new StdSchedulerFactory(  );

        try
        {
            // On récupère une instance de scheduler par défaut
            Scheduler mySched = schedFact.getScheduler(  );

            // On démarre le scheduler(mise en attente de job)
            mySched.start(  );

            // Ajout du job de notification
            if ( AppPropertiesService.getPropertyInt( NOTIFICATION_ACTIF, 0 ) == 1 )
            {
                ajouterJobNotification( mySched );
            }

            // Ajout du job de vidange du panier
            if ( AppPropertiesService.getPropertyInt( VIDANGE_PANIER_ACTIF, 0 ) == 1 )
            {
                ajouterJobVidePanier( mySched );
            }
        }
        catch ( SchedulerException se )
        {
            AppLogService.error( se );
        }
    }

    /**
     * Crée le job de notification.
     * @param scheduler le scheduler auquel il faut ajouter le job
     */
    private void ajouterJobNotification( Scheduler scheduler )
    {
        try
        {
            // On instancie un nouveau Job
            JobDetail job = new JobDetail( AppPropertiesService.getProperty( NOTIFICATION_NOM_JOB ),
                    Scheduler.DEFAULT_GROUP, ODSNotificationJob.class );

            // On récupère le trigger dans ods.properties pour définir le rythme d'exécution
            String strCrontab = AppPropertiesService.getProperty( NOTIFICATION_HEURE_EXECUTION );
            CronTrigger trigger = null;

            try
            {
                trigger = new CronTrigger( NOTIFICATION_TRIGGER_NAME, Scheduler.DEFAULT_GROUP, strCrontab );
            }
            catch ( ParseException pe )
            {
                AppLogService.error( pe );
            }

            // On fournit le Job crée et le trçigger associé au Scheduler
            scheduler.scheduleJob( job, trigger );
        }
        catch ( SchedulerException se )
        {
            AppLogService.error( se );
        }
    }

    /**
     * Crée le job de vidange du panier.
     * @param scheduler le scheduler auquel il faut ajouter le job
     */
    private void ajouterJobVidePanier( Scheduler scheduler )
    {
        try
        {
            // On instancie un nouveau Job
            JobDetail job = new JobDetail( AppPropertiesService.getProperty( VIDANGE_PANIER_NOM_JOB ),
                    Scheduler.DEFAULT_GROUP, ODSVidePanierJob.class );

            // On récupère le trigger dans ods.properties pour définir le rythme d'exécution
            String strCrontab = AppPropertiesService.getProperty( VIDANGE_PANIER_HEURE_EXECUTION );
            CronTrigger trigger = null;

            try
            {
                trigger = new CronTrigger( VIDANGE_PANIER_TRIGGER_NAME, Scheduler.DEFAULT_GROUP, strCrontab );
            }
            catch ( ParseException pe )
            {
                AppLogService.error( pe );
            }

            // On fournit le Job crée et le trçigger associé au Scheduler
            scheduler.scheduleJob( job, trigger );
        }
        catch ( SchedulerException se )
        {
            AppLogService.error( se );
        }
    }
}
