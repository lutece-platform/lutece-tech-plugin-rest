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
import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.horodatage.HorodatageJspBean;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * HorodatageDAO
 * Cette classe permet d'accèder aux données permettant la gestion de l'horodatage
 */
public class HorodatageDAO implements IHorodatageDAO
{
    private static final String SQL_QUERY_NEW_PK_PUBLICATION = "SELECT max( id_trace ) FROM ods_horodatage_publication ";
    private static final String SQL_QUERY_NEW_PK_NOTIFICATION = "SELECT max( id_trace ) FROM ods_horodatage_notifications ";
    private static final String SQL_QUERY_INSERT_PUBLICATION = "INSERT INTO ods_horodatage_publication (" +
        " id_trace, date_trace, date_seance, famille_doc, id_db_ods, reference, date_publication, intitule, version, action, signatures) " +
        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
    private static final String SQL_QUERY_INSERT_NOTIFICATION = "INSERT INTO ods_horodatage_notifications (" +
        "	id_trace, date_trace, objet, contenu, signature) " + "VALUES (?, ?, ?, ?, ?) ";
    private static final String SQL_QUERY_INSERT_NOTIFICATION_UTILISATEURS = "INSERT INTO ods_horodatage_notifications_utilisateurs (" +
        "	id_trace, id_utilisateur, date_envoi, destinataires) " + "VALUES (?, ?, ?, ?) ";
    private static final String SQL_QUERY_FIND_LAST_NOTIFICATION = " SELECT max( date_trace ) FROM ods_horodatage_notifications ";
    private static final String SQL_QUERY_FIND_ALL_PUBLICATION = "SELECT * FROM ods_horodatage_publication h WHERE 1 = 1 ";
    private static final String SQL_QUERY_FIND_ALL_NOTIFICATION = "SELECT * FROM ods_horodatage_notifications h " +
        " LEFT JOIN ods_horodatage_notifications_utilisateurs hnu on (h.id_trace = hnu.id_trace) " + " WHERE 1 = 1 ";
    private static final String SQL_FILTER_DATE_DEBUT = " AND h.date_trace >= ? ";
    private static final String SQL_FILTER_DATE_FIN = " AND h.date_trace <= ? ";
    private static final String SQL_QUERY_ORDER_BY_DATE_AJOUT = " ORDER BY h.date_trace ";

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
     * @return id qui a ete généré
     */
    public int insertTracePublication( Timestamp dateSeance, HorodatageFamilleDocumentEnum familleDocument,
        int idFichier, String reference, Timestamp datePublication, String intitule, int version,
        HorodatageActionEnum action, String signature, Plugin plugin, int id )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_PUBLICATION, plugin );
       
        int newIdTrace = newPrimaryKey( SQL_QUERY_NEW_PK_PUBLICATION, plugin );
        daoUtil.setInt( 1, newIdTrace );
        daoUtil.setTimestamp( 2, new Timestamp( System.currentTimeMillis(  ) ) );
        daoUtil.setTimestamp( 3, dateSeance );
        daoUtil.setString( 4, familleDocument.getId(  ) );
        daoUtil.setInt( 5, id );
        daoUtil.setString( 6, reference );
        daoUtil.setTimestamp( 7, datePublication );
        daoUtil.setString( 8, intitule );
        daoUtil.setInt( 9, version );
        daoUtil.setInt( 10, action.getId(  ) );
        daoUtil.setString( 11, signature );

        /*
        if ( ( familleDocument != null ) && familleDocument.equals( HorodatageFamilleDocumentEnum.PDD ) )
        {
            // Cas ou l'on traite un PDD
            daoUtil.setIntNull( 4 );
        }
        else if ( ( familleDocument != null ) &&
                familleDocument.equals( HorodatageFamilleDocumentEnum.VOEU_AMENDEMENT ) )
        {
            // Cas ou l'on traite un VA
            daoUtil.setIntNull( 4 );
        }
        else if ( ( familleDocument != null ) && familleDocument.equals( HorodatageFamilleDocumentEnum.FICHIER ) )
        {
            // Cas ou l'on traite un fichier 
            daoUtil.setInt( 4, idFichier );
        }
         */
        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        return newIdTrace;
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
    public int insertTraceNotification( String objet, String contenu, String signature, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_NOTIFICATION, plugin );

        int newIdTrace = newPrimaryKey( SQL_QUERY_NEW_PK_NOTIFICATION, plugin );
        daoUtil.setInt( 1, newIdTrace );
        daoUtil.setTimestamp( 2, OdsUtils.getCurrentDate(  ) );
        daoUtil.setString( 3, objet );
        daoUtil.setString( 4, contenu );
        daoUtil.setString( 5, signature );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        return newIdTrace;
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
    public void insertTraceNotificationDestinaire( int idTraceNotification, String idDestinataire, Timestamp dateEnvoi,
        String mails, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_NOTIFICATION_UTILISATEURS, plugin );
        daoUtil.setInt( 1, idTraceNotification );
        daoUtil.setString( 2, idDestinataire );
        daoUtil.setTimestamp( 3, dateEnvoi );
        daoUtil.setString( 4, mails );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne la date de na dernière notification
     *
     * @param plugin plugin
     * @return la date de la dernière notification
     */
    public Timestamp selectLastNotification( Plugin plugin )
    {
        Timestamp result = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_LAST_NOTIFICATION, plugin );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            result = daoUtil.getTimestamp( 1 );
        }

        daoUtil.free(  );

        return result;
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
    public List<Trace> selectAllTraces( Plugin plugin, String typeHorodatage, Timestamp dateStart, Timestamp dateEnd )
    {
        List<Trace> traces = new ArrayList<Trace>(  );

        String strSQL = null;

        if ( typeHorodatage.equals( HorodatageJspBean.CONSTANTE_PUBLICATION ) )
        {
            strSQL = SQL_QUERY_FIND_ALL_PUBLICATION;
        }
        else if ( typeHorodatage.equals( HorodatageJspBean.CONSTANTE_NOTIFICATION ) )
        {
            strSQL = SQL_QUERY_FIND_ALL_NOTIFICATION;
        }

        strSQL += ( ( dateStart != null ) ? SQL_FILTER_DATE_DEBUT : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( dateEnd != null ) ? SQL_FILTER_DATE_FIN : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += SQL_QUERY_ORDER_BY_DATE_AJOUT;

        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );
        int nIndex = 1;

        if ( dateStart != null )
        {
            daoUtil.setTimestamp( nIndex, dateStart );
            nIndex++;
        }

        if ( dateEnd != null )
        {
            daoUtil.setTimestamp( nIndex, dateEnd );
            nIndex++;
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Trace trace = null;

            if ( typeHorodatage.equals( HorodatageJspBean.CONSTANTE_PUBLICATION ) )
            {
                trace = new TracePublication(  );
                ( (TracePublication) trace ).setDateSeance( daoUtil.getTimestamp( "h.date_seance" ) );
                ( (TracePublication) trace ).setFamilleDoc( HorodatageFamilleDocumentEnum.getHorodatageFamilleDocumentEnum( 
                        daoUtil.getString( "famille_doc" ) ) );
                ( (TracePublication) trace ).setIdDbOds( daoUtil.getInt( "h.id_db_ods" ) );
                ( (TracePublication) trace ).setReference( daoUtil.getString( "h.reference" ) );
                ( (TracePublication) trace ).setDatePublication( daoUtil.getTimestamp( "h.date_publication" ) );
                ( (TracePublication) trace ).setIntitule( daoUtil.getString( "h.intitule" ) );
                ( (TracePublication) trace ).setVersion( daoUtil.getInt( "h.version" ) );
                ( (TracePublication) trace ).setAction( HorodatageActionEnum.getHorodatageActionEnum( daoUtil.getInt( 
                            "action" ) ) );
                trace.setSignatures( daoUtil.getString( "h.signatures" ) );
            }
            else if ( typeHorodatage.equals( HorodatageJspBean.CONSTANTE_NOTIFICATION ) )
            {
                trace = new TraceNotification(  );
                ( (TraceNotification) trace ).setObjet( daoUtil.getString( "h.objet" ) );
                ( (TraceNotification) trace ).setContenu( daoUtil.getString( "h.contenu" ) );
                ( (TraceNotification) trace ).setSignatures( daoUtil.getString( "h.signature" ) );
                ( (TraceNotification) trace ).setIdUtilisateur( daoUtil.getString( "hnu.id_utilisateur" ) );
                ( (TraceNotification) trace ).setDateEnvoi( daoUtil.getTimestamp( "hnu.date_envoi" ) );
                ( (TraceNotification) trace ).setDestinataires( daoUtil.getString( "hnu.destinataires" ) );
            }

            trace.setIdTrace( daoUtil.getInt( "h.id_trace" ) );
            trace.setDateTrace( daoUtil.getTimestamp( "h.date_trace" ) );

            traces.add( (Trace) trace );
        }

        daoUtil.free(  );

        return traces;
    }

    /**
     * Retourne une nouvelle clé primaire unique sur la table.
     * @param plugin le plugin actif
     * @param requeteSQL requete
     * @return nouvelle clé primaire
     */
    private static int newPrimaryKey( String requeteSQL, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( requeteSQL, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }
}
