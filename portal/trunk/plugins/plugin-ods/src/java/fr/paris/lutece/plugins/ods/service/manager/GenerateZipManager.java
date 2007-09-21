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

import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.servlet.http.HttpServletRequest;


/**
 * Permet de généré l'archive des documents du panier séléctionnés par l'utilisateur
 *
 */
public class GenerateZipManager
{
    private static final String PROPERTY_JNDI_INITIAL = "java.naming.factory.initial";
    private static final String DEFAULT_PROPERTY_JNDI_INITIAL = "fr.dyade.aaa.jndi2.client.NamingContextFactory";
    private static final String PROPERTY_JNDI_HOST = "java.naming.factory.host";
    private static final String PROPERTY_JNDI_PORT = "java.naming.factory.port";
    private static final String PROPERTY_QUEUE_NAME = "queue_name";
    private static final String DEFAULT_PROPERTY_QUEUE_NAME = "queue";
    private static final String PROPERTY_CONNECTION_FACTORY = "connection_factory";
    private static final String DEFAULT_PROPERTY_CONNECTION_FACTORY = "qcf";

    /**
     * Permet de généré l'archive des documents passé en paramètre
     *
     * Cette méthode envoie une requête au seveur Joram qui se chargera de générer le ZIP et de l'insérer en Base
     *
     * La requete envoyée est de la forme suivante:
     *                         idApplication;idUser;nbFichiers;dateAjout;idFichier1-idFichier2-idFichier3-..-idFichierN
     *
     * @param request la requete
     * @param ids tableau des id des documents qui doivent être téléchargé
     * @param plugin le Plugin actif
     * @param idUser id de l'utilisateur
     * @param IdApplication idApplication
     * @param dateAjout Date qui correspond à la demande du téléchargement de l'archive
     * @throws NamingException NamingException
     * @throws JMSException JMSException
     */
    public static void generateZIP( HttpServletRequest request, String[] ids, Plugin plugin, String idUser,
        String dateAjout, int IdApplication ) throws NamingException, JMSException
    {
        Properties props = new Properties(  );
        props.setProperty( PROPERTY_JNDI_INITIAL,
            AppPropertiesService.getProperty( PROPERTY_JNDI_INITIAL, DEFAULT_PROPERTY_JNDI_INITIAL ) );
        props.setProperty( PROPERTY_JNDI_HOST, AppPropertiesService.getProperty( PROPERTY_JNDI_HOST ) );
        props.setProperty( PROPERTY_JNDI_PORT, AppPropertiesService.getProperty( PROPERTY_JNDI_PORT ) );

        Context ictx = new InitialContext( props );
        Queue queue = (Queue) ictx.lookup( AppPropertiesService.getProperty( PROPERTY_QUEUE_NAME,
                    DEFAULT_PROPERTY_QUEUE_NAME ) );
        QueueConnectionFactory qcf = (QueueConnectionFactory) ictx.lookup( AppPropertiesService.getProperty( 
                    PROPERTY_CONNECTION_FACTORY, DEFAULT_PROPERTY_CONNECTION_FACTORY ) );
        ictx.close(  );

        QueueConnection qc = qcf.createQueueConnection(  );
        QueueSession qs = qc.createQueueSession( true, 0 );
        QueueSender qsend = qs.createSender( queue );
        TextMessage msg = qs.createTextMessage(  );

        int lIds = ( ids != null ) ? ids.length : 0;

        StringBuffer buffer = new StringBuffer(  );
        buffer.append( IdApplication ).append( ";" );
        buffer.append( idUser ).append( ";" );
        buffer.append( lIds ).append( ";" );
        buffer.append( dateAjout ).append( ";" );

        for ( int i = 0; i < lIds; i++ )
        {
            if ( ( ids[i] != null ) && !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( ids[i] ) )
            {
                buffer.append( ids[i] );

                if ( i < ( lIds - 1 ) )
                {
                    buffer.append( "-" );
                }
            }
        }

        msg.setText( buffer.toString(  ) );
        qsend.send( msg );
        qs.commit(  );

        qc.close(  );
    }
}
