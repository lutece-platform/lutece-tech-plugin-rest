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
package fr.paris.lutece.plugins.ods.service.publicationparisfr;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.direction.Direction;
import fr.paris.lutece.plugins.ods.business.direction.DirectionHome;
import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierFilter;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysique;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysiqueHome;
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocumentEnum;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilEnum;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilHome;
import fr.paris.lutece.plugins.ods.business.naturedossier.NatureDesDossiers;
import fr.paris.lutece.plugins.ods.business.ordredujour.EntreeOrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.EntreeOrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourUtils;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.pdd.PDDFilter;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.Publication;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.PublicationHome;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.detaildelibdirection.TypeCgeneral;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.detaildelibdirection.TypeCmunicipal;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.detaildelibdirection.TypeDelib;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.listedirections.DELIBS;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.listedirections.TypeCGENERAL;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.listedirections.TypeCMUNICIPAL;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.listedirections.TypeDIRECTION;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.ordredujour.OrdreDuJourGeneral;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.ordredujour.OrdreDuJourMunicipal;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.ordredujour.TypeAffaire;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.ordredujour.TypeAffaires;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.ordredujour.TypeCommission;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.ordredujour.TypeCommissions;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.ordredujour.TypeDeliberations;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.ordredujour.TypeDeliberationsParNature;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.ordredujour.TypeRegroupementAffaires;
import fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.ordredujour.TypeRegroupementsAffaires;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.ordredujour.OrdreDuJourJspBean;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.net.SocketException;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


/**
 *
 * gère la publication vers paris.fr  de l'ordre du jour et des documents associés.
 *
 */
public class PublicationService
{
    private static final String CONSTANTE_PACKAGE_ORDRE_DU_JOUR_GENERATION = "fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.ordredujour";
    private static final String CONSTANTE_PACKAGE_LISTE_DIRECTIONS_GENERATION = "fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.listedirections";
    private static final String CONSTANTE_PACKAGE_DETAIL_DIRECTION_GENERATION = "fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.detaildelibdirection";
    private static final String PROPERTY_DELEGATION = "ods.front.directionlist.label.delegation";
    private static final String CONSTANTE_PLUGIN_NAME_ODS = "ods";
    private static final String PROPERTY_HOST = "odsPublicationParisfr.ftp.Host";
    private static final String PROPERTY_USERNAME = "odsPublicationParisfr.ftp.UserName";
    private static final String PROPERTY_PASSWORD = "odsPublicationParisfr.ftp.Password";
    private static final String PROPERTY_CURRENT_DIRECTORY = "odsPublicationParisfr.ftp.CurrentDirectory";
    private static final String PROPERTY_OLDIES_DIRECTORY = "odsPublicationParisfr.ftp.OldiesDirectory";
    private static final String PROPERTY_LOCK_DIRECTORY = "odsPublicationParisfr.ftp.LockDirectory";
    private static final String PROPERTY_LOCK_FILE_PUBLICATION = "odsPublicationParisfr.ftp.LockDirectory.LockPublication";
    private static final String PROPERTY_LOCK_FILE_TRANSFERT = "odsPublicationParisfr.ftp.LockDirectory.LockTransfert";
    private static final String PROPERTY_LOCAL_DIRECTORY = "odsPublicationParisfr.localDirectory";
    private static final String PROPERTY_FILE_ODJ_CONSEIL_GENERAL_XML = "odsPublicationParisfr.fileOdjConseilGeneralXml";
    private static final String PROPERTY_FILE_ODJ_CONSEIL_MUNICIPAL_XML = "odsPublicationParisfr.fileOdjConseilMunicipalXml";
    private static final String PROPERTY_FILE_SYNTHESE_DELIB_DIRECTIONS_XML = "odsPublicationParisfr.fileSyntheseDelibDirectionsXml";
    private static final String CONSTANTE_EXTENSION_PDF = ".pdf";

    /**
     * methode qui declenche le processus de publication vers paris.fr
     */
    public static void publicationParisFr(  )
    {
        String strHost = AppPropertiesService.getProperty( PROPERTY_HOST );
        String strUserName = AppPropertiesService.getProperty( PROPERTY_USERNAME );
        String strPassword = AppPropertiesService.getProperty( PROPERTY_PASSWORD );
        String strCurentDirectory = AppPropertiesService.getProperty( PROPERTY_CURRENT_DIRECTORY );
        String strOldiesDirectory = AppPropertiesService.getProperty( PROPERTY_OLDIES_DIRECTORY );
        String strLockDirectory = AppPropertiesService.getProperty( PROPERTY_LOCK_DIRECTORY );
        String strLockFilePublication = AppPropertiesService.getProperty( PROPERTY_LOCK_FILE_PUBLICATION );
        String strLockFileTransfert = AppPropertiesService.getProperty( PROPERTY_LOCK_FILE_TRANSFERT );
        String strLocalDirectory = AppPropertiesService.getProperty( PROPERTY_LOCAL_DIRECTORY );
        String strFileOdjConseilGeneralXml = AppPropertiesService.getProperty( PROPERTY_FILE_ODJ_CONSEIL_GENERAL_XML );
        String strFileOdjConseilMunicipalXml = AppPropertiesService.getProperty( PROPERTY_FILE_ODJ_CONSEIL_MUNICIPAL_XML );
        String strFileSyntheseDelibDirectionsXml = AppPropertiesService.getProperty( PROPERTY_FILE_SYNTHESE_DELIB_DIRECTIONS_XML );

        if ( allFtpParametersRequiered( strHost, strUserName, strPassword, strCurentDirectory, strOldiesDirectory,
                    strLockDirectory, strLockFilePublication, strLockFileTransfert, strLocalDirectory,
                    strFileOdjConseilGeneralXml, strFileOdjConseilMunicipalXml, strFileSyntheseDelibDirectionsXml ) )
        {
            try
            {
                //test si les ordres du jour municipal et general
                //sont à transferer vers paris.fr 
                Publication publicationOrdreDuJourMunicipal = new Publication(  );
                publicationOrdreDuJourMunicipal.setIdPublication( -1 );

                Publication publicationOrdreDuJourGeneral = new Publication(  );
                publicationOrdreDuJourGeneral.setIdPublication( -1 );

                Plugin plugin = PluginService.getPlugin( CONSTANTE_PLUGIN_NAME_ODS );

                if ( existOrdreDuJoursForExportParisFr( publicationOrdreDuJourMunicipal, publicationOrdreDuJourGeneral,
                            plugin ) )
                {
                    FTPClient ftpClient = ftpConnexion( strHost, strUserName, strPassword );

                    if ( !isLockPublication( ftpClient, strLockDirectory, strLockFilePublication ) )
                    {
                        // pas de  processus de publication (côté Paris.fr) en cours
                        //Positionnement du verrou ODS
                        setLockOds( ftpClient, strLockDirectory, strLockFileTransfert );
                        //on ferme la connexion avec le serveur ftp le temps de la creation des fichiers en local
                        ftpClient.disconnect(  );

                        //creation des fichiers ods en locale
                        boolean bCreateInLocaleDirectory = createOdsFilesInLocalDirectory( strLocalDirectory,
                                strFileOdjConseilGeneralXml, strFileOdjConseilMunicipalXml,
                                strFileSyntheseDelibDirectionsXml, publicationOrdreDuJourGeneral.getOrdreDuJour(  ),
                                publicationOrdreDuJourMunicipal.getOrdreDuJour(  ), plugin );

                        if ( bCreateInLocaleDirectory )
                        {
                            //debut du tansfert vers paris.fr
                            //déplacement (en ftp sur le répertoire cible) des fichiers présents dans 
                            //le répertoire /datas/current vers le répertoire /data/oldies/<date_transfert>.increment.
                            ftpClient = ftpConnexion( strHost, strUserName, strPassword );

                            //test sur la présence de  repertoires oldies générés le meme jour 
                            FTPFile[] oldies = ftpClient.listFiles( strOldiesDirectory );
                            int ncpt = 0;

                            if ( oldies != null )
                            {
                                for ( int i = 0; i < oldies.length; i++ )
                                {
                                    if ( oldies[i].getName(  ).contains( "" + getCurrentDate(  ) ) )
                                    {
                                        ncpt++;
                                    }
                                }
                            }

                            String strNewOldies = strOldiesDirectory + "/" + getCurrentDate(  );

                            if ( ncpt != 0 )
                            {
                                strNewOldies = strNewOldies + "." + ncpt;
                            }

                            ftpClient.rename( strCurentDirectory, strNewOldies );
                            ftpClient.makeDirectory( strCurentDirectory );
                            //export en ftp des nouveaux fichiers vers le répertoire /datas/current.
                            ftpClient.changeWorkingDirectory( strCurentDirectory );

                            File file = new File( strLocalDirectory );
                            File[] files = file.listFiles(  );

                            for ( int i = 0; i < files.length; i++ )
                            {
                                if ( files[i].isFile(  ) )
                                {
                                    FileInputStream fis = new FileInputStream( files[i] );
                                    ftpClient.storeFile( files[i].getName(  ), fis );
                                    fis.close(  );
                                }
                            }
                        }

                        //on supprime le verrou ods
                        if ( !removeLockOds( ftpClient, strLockDirectory, strLockFileTransfert ) )
                        {
                            AppLogService.error( PublicationError.getMessageError( 
                                    PublicationError.MESSAGE_DELETE_VERROU ) );
                        }

                        //on ferme la connexion avec le serveur ftp
                        ftpClient.disconnect(  );

                        //on supprime l'ensemble des fichiers presents dans le repertoire locale
                        if ( !removeOdsFilesInLocalDirectory( strLocalDirectory ) )
                        {
                            AppLogService.error( PublicationError.getMessageError( 
                                    PublicationError.MESSAGE_DELETE_LOCAL_DIRECTORY ) );
                        }

                        //suppression dans la table ods_publication_parisfr des ordres du jours tansférés
                        PublicationHome.remove( publicationOrdreDuJourGeneral, plugin );
                        PublicationHome.remove( publicationOrdreDuJourMunicipal, plugin );
                    }
                }
            }
            catch ( IOException e )
            {
                AppLogService.error( PublicationError.MESSAGE_PROBLEME_PARIS_FR );
                AppLogService.error( e );
            }
        }
        else
        {
            AppLogService.error( PublicationError.getMessageError( PublicationError.MESSAGE_ALL_PARAMETERS_REQUIERED ) );
        }
    }

    /**
     * Génère à partir d'un ordre du jour(municipal ou general)
     * un fichier (odj_conseil_municipal.xml ou odj_conseil_general.xml)
     * contenant l'ensemble des données nécessaires à la publication coté paris.fr de l'ordre du jour
     * @param ordreDuJour l'ordre du jour
     * @param strPathOdjXml le chemin d'acces au repertoire locale destiné à stocker le fichier xml généré
     * @param plugin plugin
     * @throws JAXBException  jAXBException
     * @throws IOException        iOException
     */
    public static void genereOrdreDuJourXml( OrdreDuJour ordreDuJour, String strPathOdjXml, Plugin plugin )
        throws JAXBException, IOException
    {
        Locale locale = Locale.getDefault(  );
        OrdreDuJourGeneral ordreDuJourGeneral = new OrdreDuJourGeneral(  );
        OrdreDuJourMunicipal ordreDuJourMunicipal = new OrdreDuJourMunicipal(  );
        TypeDeliberations typeDeliberations = new TypeDeliberations(  );
        List<Elu> rapporteursSauvegarde = null;
        List<Commission> commissionsSauvegarde = null;
        NatureDesDossiers natureDesDossiersSauvegarde = null;

        if ( ordreDuJour.getFormationConseil(  ).getIdFormationConseil(  ) == FormationConseilEnum.FORMATIONCONSEILMUNICIPAL.getId(  ) )
        {
            if ( ordreDuJour.getDatePublication(  ) != null )
            {
                ordreDuJourMunicipal.setDateMaj( getDateMaj( ordreDuJour.getDatePublication(  ) ) );
            }

            if ( ordreDuJour.getSeance(  ) != null )
            {
                Seance seance = SeanceHome.findByPrimaryKey( ordreDuJour.getSeance(  ).getIdSeance(  ), plugin );
                ordreDuJourMunicipal.setDateConseil( OdsUtils.getDateConseil( seance ) );
            }

            if ( ordreDuJour.getIntitule(  ) != null )
            {
                ordreDuJourMunicipal.setTitre( ordreDuJour.getIntitule(  ) );
            }

            ordreDuJourMunicipal.setDeliberations( typeDeliberations );
        }
        else
        {
            if ( ordreDuJour.getDatePublication(  ) != null )
            {
                ordreDuJourGeneral.setDateMaj( getDateMaj( ordreDuJour.getDatePublication(  ) ) );
            }

            if ( ordreDuJour.getSeance(  ) != null )
            {
                Seance seance = SeanceHome.findByPrimaryKey( ordreDuJour.getSeance(  ).getIdSeance(  ), plugin );
                ordreDuJourGeneral.setDateConseil( OdsUtils.getDateConseil( seance ) );
            }

            if ( ordreDuJour.getIntitule(  ) != null )
            {
                ordreDuJourGeneral.setTitre( ordreDuJour.getIntitule(  ) );
            }

            ordreDuJourGeneral.setDeliberations( typeDeliberations );
        }

        TypeAffaires typeAffaires;
        TypeCommissions typeCommissions;
        TypeDeliberationsParNature typeDeliberationsParNature;
        TypeRegroupementsAffaires typeRegroupementsAffaires;
        TypeRegroupementAffaires typeRegroupementAffaires;
        TypeCommission typeCommission;

        //recuperation de la liste des entrées d'ordre du jour
        List<EntreeOrdreDuJour> listeEntreeOrdreDuJour = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJour.getIdOrdreDuJour(  ),
                plugin );

        for ( EntreeOrdreDuJour entree : listeEntreeOrdreDuJour )
        {
            if ( entree.getType(  ).equals( OrdreDuJourJspBean.CONSTANTE_ENTREE_TYPE_PDD ) &&
                    ( entree.getPdd(  ) != null ) && entree.getPdd(  ).isEnLigne(  ) )
            {
                if ( ( ( natureDesDossiersSauvegarde == null ) && ( entree.getNature(  ) != null ) ) ||
                        ( ( natureDesDossiersSauvegarde != null ) && ( entree.getNature(  ) != null ) &&
                        ( natureDesDossiersSauvegarde.getIdNature(  ) != entree.getNature(  ).getIdNature(  ) ) ) )
                {
                    // si nature de dossier sauvegarde == nul ou l'entree possede
                    // une nature de dossier différente de la nature sauvegardée
                    // on cree une nouvelle nature de dossiers
                    natureDesDossiersSauvegarde = entree.getNature(  );
                    typeDeliberationsParNature = new TypeDeliberationsParNature(  );
                    typeDeliberationsParNature.setNature( natureDesDossiersSauvegarde.getLibelleNature(  ) );
                    typeDeliberations.getDeliberationsParNature(  ).add( typeDeliberationsParNature );

                    if ( ( entree.getCommissions(  ) != null ) && ( entree.getCommissions(  ).size(  ) != 0 ) )
                    {
                        //	insertion du groupe de commissions
                        commissionsSauvegarde = entree.getCommissions(  );
                        typeCommissions = new TypeCommissions(  );
                        typeCommission = insertGroupeCommissions( typeCommissions, commissionsSauvegarde, locale );
                        typeDeliberationsParNature.getCommissions(  ).add( typeCommissions );

                        if ( ( entree.getElus(  ) != null ) && ( entree.getElus(  ).size(  ) != 0 ) )
                        {
                            //insertion du groupe de rapporteurs dans le groupe de commissions
                            rapporteursSauvegarde = entree.getElus(  );
                            typeRegroupementsAffaires = new TypeRegroupementsAffaires(  );
                            typeRegroupementAffaires = insertGroupeRapporteurs( typeRegroupementsAffaires,
                                    rapporteursSauvegarde, locale );
                            typeCommission.getRegroupementsAffaires(  ).add( typeRegroupementsAffaires );
                            //insertion du pdd dans le groupe de commissions
                            typeAffaires = new TypeAffaires(  );
                            insertPdd( typeAffaires, entree );
                            typeRegroupementAffaires.getAffaires(  ).add( typeAffaires );
                        }
                    }
                }
                else if ( entree.getNature(  ) != null )
                {
                    int nIndextypeDeliberationsParNature;

                    if ( ordreDuJour.getFormationConseil(  ).getIdFormationConseil(  ) == FormationConseilEnum.FORMATIONCONSEILMUNICIPAL.getId(  ) )
                    {
                        nIndextypeDeliberationsParNature = ordreDuJourMunicipal.getDeliberations(  )
                                                                               .getDeliberationsParNature(  ).size(  ) -
                            1;
                        typeDeliberationsParNature = ordreDuJourMunicipal.getDeliberations(  )
                                                                         .getDeliberationsParNature(  )
                                                                         .get( nIndextypeDeliberationsParNature );
                    }
                    else
                    {
                        nIndextypeDeliberationsParNature = ordreDuJourGeneral.getDeliberations(  )
                                                                             .getDeliberationsParNature(  ).size(  ) -
                            1;
                        typeDeliberationsParNature = ordreDuJourGeneral.getDeliberations(  ).getDeliberationsParNature(  )
                                                                       .get( nIndextypeDeliberationsParNature );
                    }

                    if ( ( ( commissionsSauvegarde == null ) && ( entree.getCommissions(  ) != null ) &&
                            ( entree.getCommissions(  ).size(  ) != 0 ) ) ||
                            ( ( commissionsSauvegarde != null ) && ( commissionsSauvegarde.size(  ) != 0 ) &&
                            !entree.getEqualListCommissions( commissionsSauvegarde ) ) )
                    {
                        //si le groupe de commissions  sauvegarde == nul ou l'entree possede
                        // un groupe de commission différente du groupe de commissions sauvegardé
                        // on cree un nouveau groupe de commissions
                        //insertion du groupe de commissions
                        commissionsSauvegarde = entree.getCommissions(  );
                        typeCommission = insertGroupeCommissions( typeDeliberationsParNature.getCommissions(  ).get( 0 ),
                                commissionsSauvegarde, locale );

                        if ( ( entree.getElus(  ) != null ) && ( entree.getElus(  ).size(  ) != 0 ) )
                        {
                            //insertion du groupe de rapporteurs dans le groupe de commissions
                            rapporteursSauvegarde = entree.getElus(  );
                            typeRegroupementsAffaires = new TypeRegroupementsAffaires(  );
                            typeRegroupementAffaires = insertGroupeRapporteurs( typeRegroupementsAffaires,
                                    rapporteursSauvegarde, locale );
                            typeCommission.getRegroupementsAffaires(  ).add( typeRegroupementsAffaires );
                            //insertion du pdd dans le groupe de commissions
                            typeAffaires = new TypeAffaires(  );
                            insertPdd( typeAffaires, entree );
                            typeRegroupementAffaires.getAffaires(  ).add( typeAffaires );
                        }
                    }
                    else if ( ( entree.getCommissions(  ) != null ) && ( entree.getCommissions(  ).size(  ) != 0 ) )
                    {
                        typeCommissions = typeDeliberationsParNature.getCommissions(  ).get( 0 );

                        int nIndexTypeCommission = typeCommissions.getCommission(  ).size(  ) - 1;
                        //insertion du groupe de rapporteurs dans le groupe de commissions
                        typeCommission = typeCommissions.getCommission(  ).get( nIndexTypeCommission );

                        if ( ( rapporteursSauvegarde != null ) && ( rapporteursSauvegarde.size(  ) != 0 ) &&
                                ( entree.getElus(  ) != null ) && ( entree.getElus(  ).size(  ) != 0 ) &&
                                !entree.getEqualListRapporteurs( rapporteursSauvegarde ) )
                        {
                            // si le groupe des rapporteurs de l'entrée est différent du
                            // groupe des rapporteurs sauvegardé
                            // on cree un nouveau groupe  de rapporteurs
                            rapporteursSauvegarde = entree.getElus(  );

                            typeRegroupementAffaires = insertGroupeRapporteurs( typeCommission.getRegroupementsAffaires(  )
                                                                                              .get( 0 ),
                                    rapporteursSauvegarde, locale );
                            //insertion du pdd dans le groupe de commissions
                            typeAffaires = new TypeAffaires(  );
                            insertPdd( typeAffaires, entree );
                            typeRegroupementAffaires.getAffaires(  ).add( typeAffaires );
                        }
                        else if ( ( entree.getElus(  ) != null ) && ( entree.getElus(  ).size(  ) != 0 ) )
                        {
                            typeRegroupementsAffaires = typeCommission.getRegroupementsAffaires(  ).get( 0 );

                            int nIndexRegroupementsAffaires = typeRegroupementsAffaires.getRegroupementAffaires(  )
                                                                                       .size(  ) - 1;
                            typeRegroupementAffaires = typeRegroupementsAffaires.getRegroupementAffaires(  )
                                                                                .get( nIndexRegroupementsAffaires );
                            insertPdd( typeRegroupementAffaires.getAffaires(  ).get( 0 ), entree );
                        }
                    }
                }
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream( strPathOdjXml );

        if ( ordreDuJour.getFormationConseil(  ).getIdFormationConseil(  ) == FormationConseilEnum.FORMATIONCONSEILMUNICIPAL.getId(  ) )
        {
            marshal( ordreDuJourMunicipal, CONSTANTE_PACKAGE_ORDRE_DU_JOUR_GENERATION, fileOutputStream );
        }
        else
        {
            marshal( ordreDuJourGeneral, CONSTANTE_PACKAGE_ORDRE_DU_JOUR_GENERATION, fileOutputStream );
        }
    }

    /**
     * renvoie la date courante
     * @return la date courante
     */
    private static String getCurrentDate(  )
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyyMMdd" );

        return dateFormat.format( new Timestamp( System.currentTimeMillis(  ) ) );
    }

    /**
     * retourne la date de publication
     * @param timestampDatePublication timestampDatePublication
     * @return la date de publication
     */
    private static String getDateMaj( Timestamp timestampDatePublication )
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat( "EEEE dd MMMM yyyy" );
        String strDateDebut = dateFormat.format( timestampDatePublication ).toString(  );

        return strDateDebut;
    }

    /**
     * insere une balise  groupe de commissions dans l'ordre du jour(odj.xml)
     * @param typeCommissions la balise dans laquelle doit etre insérée le groupe de commission
     * @param groupeCommissions le groupe de commissions à inserer
     * @param locale locale
     * @return la balise groupe de commissions nouvellement créé
     */
    private static TypeCommission insertGroupeCommissions( TypeCommissions typeCommissions,
        List<Commission> groupeCommissions, Locale locale )
    {
        TypeCommission typeCommission;
        typeCommission = new TypeCommission(  );
        typeCommission.setTitre( OrdreDuJourUtils.getLibelleGroupeCommissions( groupeCommissions, locale ) );
        typeCommissions.getCommission(  ).add( typeCommission );

        return typeCommission;
    }

    /**
     * insere une balise  groupe de rapporteurs dans l'ordre du jour(odj.xml)
     * @param typeRegroupementsAffaires la balise dans laquelle doit etre insérée le groupe de rapporteurs
     * @param groupeElus le groupe de rapporteurs à inserer
     * @param locale locale
     * @return la balise groupe de rapporteurs nouvellement créé
     */
    private static TypeRegroupementAffaires insertGroupeRapporteurs( 
        TypeRegroupementsAffaires typeRegroupementsAffaires, List<Elu> groupeElus, Locale locale )
    {
        TypeRegroupementAffaires typeRegroupementAffaires;
        typeRegroupementAffaires = new TypeRegroupementAffaires(  );
        typeRegroupementAffaires.setRapporteurs( OrdreDuJourUtils.getLibelleGroupeRapporteurs( groupeElus, locale ) );
        typeRegroupementsAffaires.getRegroupementAffaires(  ).add( typeRegroupementAffaires );

        return typeRegroupementAffaires;
    }

    /**
     * insere une balise pdd dans l'ordre du jour(odj.xml)
     * @param typeAffaires la balise dans laquelle doit etre inséré le pdd
     * @param entreeOrdreDuJour l'entree d'ordre du jour contenant le pdd
     * @return la balise pdd nouvellement créé
     */
    private static TypeAffaire insertPdd( TypeAffaires typeAffaires, EntreeOrdreDuJour entreeOrdreDuJour )
    {
        TypeAffaire typeAffaire;
        typeAffaire = new TypeAffaire(  );
        typeAffaire.setFichier( genereCodeFichier( entreeOrdreDuJour.getPdd(  ) ) );
        typeAffaire.setReferenceAlpaca( entreeOrdreDuJour.getReference(  ) );
        typeAffaire.setObjet( entreeOrdreDuJour.getObjet(  ) );
        typeAffaires.getAffaire(  ).add( typeAffaire );

        return typeAffaire;
    }

    /**
     * genere à partir des  données contenues  dans les ordres du jour municipal et general
     * la liste des directions au format xml et pour chacune d'entre elle le nombre de pdds associés
     * @param ordreDuJourMunicipal ordreDuJourMunicipal
     * @param ordreDuJourGeneral ordreDuJourGeneral
     * @param strPathSyntheseXml  chemin ou sera généré la synthese des directions
     * @param plugin plugin
     * @throws JAXBException jAXBException
     * @throws IOException iOException
     */
    public static void genereListeDirectionsXml( OrdreDuJour ordreDuJourMunicipal, OrdreDuJour ordreDuJourGeneral,
        String strPathSyntheseXml, Plugin plugin ) throws JAXBException, IOException
    {
        if ( ordreDuJourMunicipal.getSeance(  ) != null )
        {
            Seance seance = SeanceHome.findByPrimaryKey( ordreDuJourMunicipal.getSeance(  ).getIdSeance(  ), plugin );
            ordreDuJourMunicipal.setSeance( seance );
            ordreDuJourGeneral.setSeance( seance );
        }

        DELIBS delibs = new DELIBS(  );
        TypeCGENERAL typeConseilGeneral = new TypeCGENERAL(  );
        TypeCMUNICIPAL typeConseilMunicipal = new TypeCMUNICIPAL(  );

        TypeDIRECTION typeDirection;
        int nbPdds;
        PDDFilter pddFilter = new PDDFilter(  );
        pddFilter.setPublication( 1 );
        pddFilter.setIdOdjReferenceFormationConseilGeneral( ordreDuJourGeneral.getIdOrdreDuJour(  ) );
        pddFilter.setIdOdjReferenceFormationConseilMunicipal( ordreDuJourMunicipal.getIdOrdreDuJour(  ) );
        pddFilter.setInscritODJ( 1 );

        List<Direction> directions = DirectionHome.findAllDirectionsActives( plugin );
        List<FormationConseil> formationConseils = FormationConseilHome.findFormationConseilList( plugin );

        for ( FormationConseil formationConseil : formationConseils )
        {
            pddFilter.setIdFormationConseil( formationConseil.getIdFormationConseil(  ) );
            //on recupere le nombre de projets associé à la  délégations de services publics 
            pddFilter.setDelegationService( 1 );
            nbPdds = PDDHome.findByFilter( pddFilter, plugin ).size(  );
            typeDirection = new TypeDIRECTION(  );
            typeDirection.setNB( OdsConstants.CONSTANTE_CHAINE_VIDE + nbPdds );
            typeDirection.setNOM( I18nService.getLocalizedString( PROPERTY_DELEGATION, Locale.getDefault(  ) ) );
            pddFilter.setDelegationService( PDDFilter.ALL_INT );

            if ( FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) == formationConseil.getIdFormationConseil(  ) )
            {
                typeConseilGeneral.setDATE( OdsUtils.getDateConseil( ordreDuJourGeneral.getSeance(  ) ) );
                typeConseilGeneral.getDIRECTION(  ).add( typeDirection );
                delibs.setCGENERAL( typeConseilGeneral );
            }
            else
            {
                typeConseilMunicipal.setDATE( OdsUtils.getDateConseil( ordreDuJourMunicipal.getSeance(  ) ) );
                typeConseilMunicipal.getDIRECTION(  ).add( typeDirection );
                delibs.setCMUNICIPAL( typeConseilMunicipal );
            }

            //chaque direction est affichée avec le nombre de projets associé
            //recupération de la  liste des directions ainsi que le nombre de projets associé à chaque direction
            for ( Direction direction : directions )
            {
                nbPdds = PDDHome.findByFilterByDirectionOrDirectionCoemetrice( pddFilter, plugin,
                        direction.getIdDirection(  ) ).size(  );
                typeDirection = new TypeDIRECTION(  );
                typeDirection.setNB( OdsConstants.CONSTANTE_CHAINE_VIDE + nbPdds );
                typeDirection.setNOM( direction.getLibelleLong(  ) );

                if ( FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) == formationConseil.getIdFormationConseil(  ) )
                {
                    typeConseilGeneral.getDIRECTION(  ).add( typeDirection );
                }
                else
                {
                    typeConseilMunicipal.getDIRECTION(  ).add( typeDirection );
                }
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream( strPathSyntheseXml );
        marshal( delibs, CONSTANTE_PACKAGE_LISTE_DIRECTIONS_GENERATION, fileOutputStream );
    }

    /**
     * genere à partir des  données contenues  dans les ordres du jour municipal et general
     * pour chaque direction un fichier au format xml contenant  les pdds associés
     * @param ordreDuJourMunicipal ordreDuJourMunicipal
     * @param ordreDuJourGeneral ordreDuJourGeneral
     * @param strLocalDirectory le repertoire locale dans lequel seront stockés les fichiers directions.xml
     * @param plugin plugin
     * @throws JAXBException jAXBException
     * @throws IOException iOException
     */
    public static void genereListePddByDirectionXml( OrdreDuJour ordreDuJourMunicipal, OrdreDuJour ordreDuJourGeneral,
        String strLocalDirectory, Plugin plugin ) throws JAXBException, IOException
    {
        List<PDD> pdds;
        PDDFilter pddFilter = new PDDFilter(  );
        pddFilter.setPublication( 1 );
        pddFilter.setIdOdjReferenceFormationConseilGeneral( ordreDuJourGeneral.getIdOrdreDuJour(  ) );
        pddFilter.setIdOdjReferenceFormationConseilMunicipal( ordreDuJourMunicipal.getIdOrdreDuJour(  ) );
        pddFilter.setInscritODJ( 1 );

        List<FormationConseil> formationConseils = FormationConseilHome.findFormationConseilList( plugin );
        List<Direction> directions = DirectionHome.findAllDirectionsActives( plugin );

        for ( Direction direction : directions )
        {
            fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.detaildelibdirection.DELIBS delibs = new fr.paris.lutece.plugins.ods.business.publicationparisfr.generated.detaildelibdirection.DELIBS(  );

            for ( FormationConseil formationConseil : formationConseils )
            {
                if ( FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) == formationConseil.getIdFormationConseil(  ) )
                {
                    TypeCgeneral typeCgeneral = new TypeCgeneral(  );
                    typeCgeneral.setDATE( OdsUtils.getDateConseil( ordreDuJourGeneral.getSeance(  ) ) );
                    delibs.setCGENERAL( typeCgeneral );
                }
                else
                {
                    TypeCmunicipal typeCmunicipal = new TypeCmunicipal(  );
                    typeCmunicipal.setDATE( OdsUtils.getDateConseil( ordreDuJourMunicipal.getSeance(  ) ) );
                    delibs.setCMUNICIPAL( typeCmunicipal );
                }

                pddFilter.setIdFormationConseil( formationConseil.getIdFormationConseil(  ) );
                pdds = PDDHome.findByFilterByDirectionOrDirectionCoemetrice( pddFilter, plugin,
                        direction.getIdDirection(  ) );

                for ( PDD pdd : pdds )
                {
                    TypeDelib typeDelib = new TypeDelib(  );
                    typeDelib.setANNEE( pdd.getAnnee(  ) );
                    typeDelib.setNUMERO( pdd.getReference(  ) );
                    typeDelib.setRESUME( pdd.getObjet(  ) );

                    if ( FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) == formationConseil.getIdFormationConseil(  ) )
                    {
                        delibs.getCGENERAL(  ).getDELIB(  ).add( typeDelib );
                    }
                    else
                    {
                        delibs.getCMUNICIPAL(  ).getDELIB(  ).add( typeDelib );
                    }
                }
            }

            FileOutputStream fileOutputStream = new FileOutputStream( strLocalDirectory + direction.getCode(  ) +
                    ".xml" );
            marshal( delibs, CONSTANTE_PACKAGE_DETAIL_DIRECTION_GENERATION, fileOutputStream );
        }
    }

    /**
     * genere un fichier xml correspondant a la representation de l'objet passé en parametre.
     *  @param object l'objet a partir du quel doit etre généré le fichier xml
     * @param strPackage le package necessaire a la generation du fichier xml
     * @param outputStream outputstream dans lequel le fichier xml doit etre généré
     * @throws JAXBException jAXBException
     * @throws IOException iOException
     */
    private static void marshal( Object object, String strPackage, OutputStream outputStream )
        throws JAXBException, IOException
    {
        /* create a JAXBContext capable of handling classes generated into */
        JAXBContext jc = JAXBContext.newInstance( strPackage );

        /* create an marshaller */
        Marshaller marshaller = jc.createMarshaller(  );

        /* unmarshal an invalid po instance document into a tree of Java
         * content objects composed of classes from the primer.po package */

        //creation de la liste des directions
        marshaller.marshal( object, outputStream );
        outputStream.close(  );
    }

    /**
     * genere a partir du pdd passé en parametre le nom du fichier destiné
     * à  contenir la concatenation de l'expose des motifs  et du projet de deliberation.
     * @param pdd pdd
     * @return le code du fichier
     */
    private static String genereCodeFichier( PDD pdd )
    {
        StringBuffer strCodePdd = new StringBuffer(  );
        strCodePdd.append( pdd.getReference(  ) );
        strCodePdd.append( CONSTANTE_EXTENSION_PDF );

        return ( strCodePdd.toString(  ) );
    }

    /**
     * Test si l'ensemble des parametres de configarations nécessaires au transfert
     * vers paris.fr(parametres FTP,configuration du  repertoire locale...)
     * ont été renseignés dans le fichier ods.properties
     * @param strHost strHost
     * @param strUserName strUserName
     * @param strPassword strPassword
     * @param strCurentDirectory strCurentDirectory
     * @param strOldiesDirectory strOldiesDirectory
     * @param strLockDirectory strLockDirectory
     * @param strLockFilePublication strLockFilePublication
     * @param strLockFileTransfert strLockFileTransfert
     * @param strLocalDirectory strLocalDirectory
     * @param strFileOdjConseilGeneralXml strFileOdjConseilGeneralXml
     * @param strFileOdjConseilMunicipalXml strFileOdjConseilMunicipalXml
     * @param strFileSyntheseDelibDirectionsXml strFileSyntheseDelibDirectionsXml
     * @return  true si l'ensemble des parametres ont été renseigné, false sinon
     */
    private static boolean allFtpParametersRequiered( String strHost, String strUserName, String strPassword,
        String strCurentDirectory, String strOldiesDirectory, String strLockDirectory, String strLockFilePublication,
        String strLockFileTransfert, String strLocalDirectory, String strFileOdjConseilGeneralXml,
        String strFileOdjConseilMunicipalXml, String strFileSyntheseDelibDirectionsXml )
    {
        boolean bFtpParametersRequiered = true;

        if ( strHost == null )
        {
            bFtpParametersRequiered = false;
        }

        if ( strUserName == null )
        {
            bFtpParametersRequiered = false;
        }

        if ( strPassword == null )
        {
            bFtpParametersRequiered = false;
        }

        if ( strCurentDirectory == null )
        {
            bFtpParametersRequiered = false;
        }

        if ( strOldiesDirectory == null )
        {
            bFtpParametersRequiered = false;
        }

        if ( strLockDirectory == null )
        {
            bFtpParametersRequiered = false;
        }

        if ( strLockFilePublication == null )
        {
            bFtpParametersRequiered = false;
        }

        if ( strLockFileTransfert == null )
        {
            bFtpParametersRequiered = false;
        }

        if ( strLocalDirectory == null )
        {
            bFtpParametersRequiered = false;
        }

        if ( strFileOdjConseilGeneralXml == null )
        {
            bFtpParametersRequiered = false;
        }

        if ( strFileOdjConseilMunicipalXml == null )
        {
            bFtpParametersRequiered = false;
        }

        if ( strFileSyntheseDelibDirectionsXml == null )
        {
            bFtpParametersRequiered = false;
        }

        return bFtpParametersRequiered;
    }

    /**
     * Test si un verrou empechant le transfert vers paris.fr est present dans le repertoire strDirLock
     * @param ftpClient ftpClient
     * @param strDirLock le chemin  pour acceder du fichier verrou
     * @param strFileLock le nom du  fichier verrou
     * @return true si un verrou a été posé, false sinon
     * @throws IOException iOException
     */
    private static boolean isLockPublication( FTPClient ftpClient, String strDirLock, String strFileLock )
        throws IOException
    {
        ftpClient.changeWorkingDirectory( strDirLock );

        FTPFile[] fileList = ftpClient.listFiles(  );

        if ( fileList != null )
        {
            for ( int i = 0; i < fileList.length; i++ )
            {
                if ( fileList[i].getName(  ).equals( strFileLock ) )
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Positionne le verrou ods dans le repertoire strDirLock
     * @param ftpClient ftpClient
     * @param strDirLock le chemin  pour acceder du fichier verrou
     * @param strFileLock le nom du  fichier verrou
     * @throws IOException
     */
    private static void setLockOds( FTPClient ftpClient, String strDirLock, String strFileLock )
        throws IOException
    {
        ftpClient.changeWorkingDirectory( strDirLock );

        OutputStream outputStream = ftpClient.storeFileStream( strFileLock );
        outputStream.close(  );
    }

    /**
     * Supprime  le verrou ods dans le repertoire strDirLock
     * @param ftpClient ftpClient
     * @param strDirLock le chemin  pour acceder du fichier verrou
     * @param strFileLock le nom du  fichier verrou
     * @return true si la suppression du verrou a bien été effectué, false sinon
     * @throws IOException
     */
    private static boolean removeLockOds( FTPClient ftpClient, String strDirLock, String strFileLock )
        throws IOException
    {
    	ftpClient.changeWorkingDirectory( strDirLock );
            
    	return ftpClient.deleteFile( strFileLock );
    }

    /**
     * Génère l'ensemble des fichiers destinés au transfert vers paris.fr
     * dans le repertoire locale strLocalDirectory
     * @param strLocalDirectory le repertoire locale dans lequel doit etre générés les fichiers destinés au transfert vers paris.fr
     * @param strFileOdjConseilGeneralXml le nom du fichier xml destiné a contenir l'ordre du jour general
     * @param strFileOdjConseilMunicipalXml le nom du fichier xml destiné a contenir l'ordre du jour municipal
     * @param strFileSyntheseDelibDirectionsXml le nom du fichier destiné a contenir la synthes des deliberations
     * @param ordreDuJourMunicipal ordre du jour municipal
     * @param ordreDuJourGeneral ordre du jour general
     * @return true si le processu de transfert vers paris.fr est declenché,false sinon
     */
    private static boolean createOdsFilesInLocalDirectory( String strLocalDirectory,
        String strFileOdjConseilGeneralXml, String strFileOdjConseilMunicipalXml,
        String strFileSyntheseDelibDirectionsXml, OrdreDuJour ordreDuJourMunicipal, OrdreDuJour ordreDuJourGeneral,
        Plugin plugin )
    {
        boolean bCreate = false;

        try
        {
            String strPath = strLocalDirectory;

            if ( strLocalDirectory.charAt( strLocalDirectory.length(  ) - 1 ) != File.separatorChar )
            {
                strPath += File.separatorChar;
            }

            //generation du fichier xml contenant les données de l'ordre du jour municipal
            genereOrdreDuJourXml( ordreDuJourGeneral, strPath + strFileOdjConseilGeneralXml, plugin );
            //generation du fichier xml contenant les données de l'ordre du jour municipal
            genereOrdreDuJourXml( ordreDuJourMunicipal, strPath + strFileOdjConseilMunicipalXml, plugin );
            //generation du fichier contenant la synthese des directions et le nombre de delibérations associées au direction 
            genereListeDirectionsXml( ordreDuJourMunicipal, ordreDuJourGeneral,
                strPath + strFileSyntheseDelibDirectionsXml, plugin );
            //generalion pour chaque direction d'un fichier xml contenat la liste des délibérations associées 
            genereListePddByDirectionXml( ordreDuJourMunicipal, ordreDuJourGeneral, strPath, plugin );
            //creation pour chaque pdd present dans l'odj municipal ou dans l'odj general
            //d'un fichier au format pdf concatenant
            //l'expose des motifs au projet de deliberation
            createDeliberationsFilePdfInLocalDirectory( ordreDuJourGeneral, strPath, plugin );
            createDeliberationsFilePdfInLocalDirectory( ordreDuJourMunicipal, strPath, plugin );
            bCreate = true;
        }
        catch ( JAXBException e )
        {
            AppLogService.error( PublicationError.getMessageError( PublicationError.MESSAGE_GENERE_XML ) );
            AppLogService.error( e );
        }
        catch ( IOException e )
        {
            AppLogService.error( PublicationError.getMessageError( PublicationError.MESSAGE_GENERE_FICHIERS ) );
            AppLogService.error( e );
        }
        catch ( DocumentException e )
        {
            AppLogService.error( PublicationError.getMessageError( PublicationError.MESSAGE_GENERE_PDF ) );
            AppLogService.error( e );
        }

        return bCreate;
    }

    /**
     * Cette methode test si le processus de transfert vers paris.fr doit etre declenché
     * (ordre du jour municipal et general de meme type present dans la table ods_publication_paris.fr).
     * @param publicationOrdreDuJourMunicipal ordre du jour municipal
     * @param publicationOrdreDuJourGeneral ordre du jour general
     * @param plugin plugin
     * @return  true si le processus de transfert doit etre declenché,false sinon
     */
    private static boolean existOrdreDuJoursForExportParisFr( Publication publicationOrdreDuJourMunicipal,
        Publication publicationOrdreDuJourGeneral, Plugin plugin )
    {
        List<Publication> publications = PublicationHome.findPublicationList( plugin );

        for ( Publication publication : publications )
        {
            if ( publication.getOrdreDuJour(  ) != null )
            {
                if ( publication.getOrdreDuJour(  ).getFormationConseil(  ).getIdFormationConseil(  ) == FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) )
                {
                    publicationOrdreDuJourGeneral.setIdPublication( publication.getIdPublication(  ) );
                    publicationOrdreDuJourGeneral.setOrdreDuJour( publication.getOrdreDuJour(  ) );
                }
                else
                {
                    publicationOrdreDuJourMunicipal.setIdPublication( publication.getIdPublication(  ) );
                    publicationOrdreDuJourMunicipal.setOrdreDuJour( publication.getOrdreDuJour(  ) );
                }
            }

            if ( ( publicationOrdreDuJourMunicipal.getIdPublication(  ) != -1 ) &&
                    ( publicationOrdreDuJourGeneral.getIdPublication(  ) != -1 ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Suppime l'ensemble des fichiers present dans le repertoire locale strLocalDirectory
     * @param strLocalDirectory
     * @return
     */
    private static boolean removeOdsFilesInLocalDirectory( String strLocalDirectory )
    {
        File file = new File( strLocalDirectory );
        File[] files = file.listFiles(  );

        for ( int i = 0; i < files.length; i++ )
        {
            if ( files[i].isFile(  ) && !files[i].delete(  ) )
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Creation pour chaque pdd present dans l'ordre du jour passé en parametre
     * d'un fichier au format pdf concatenant
     * l'expose des motifs au projet de deliberation
     * @param ordreDuJour ordreDuJour
     * @param strLocalDir le repertoire locale ou doivent etre générés les fichiers
     * @param plugin plugin
     * @throws DocumentException documentException
     * @throws IOException  iOException
     */
    public static void createDeliberationsFilePdfInLocalDirectory( OrdreDuJour ordreDuJour, String strLocalDir,
        Plugin plugin ) throws DocumentException, IOException
    {
        Fichier fichierExposeMotifs;
        Fichier fichierProjetDelibere;
        FichierPhysique fichierPhysique;
        FichierFilter fichierFilter = new FichierFilter(  );
        PdfReader readerExposeMotifs;
        PdfReader readerProjetDelibere;
        Document document;
        FileOutputStream fileOutputStreamWriter;
        PdfCopy writer;
        int nPageOffset;

        //	recuperation de la liste des entrées d'ordre du jour
        List<EntreeOrdreDuJour> listeEntreeOrdreDuJour = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( ordreDuJour.getIdOrdreDuJour(  ),
                plugin );

        for ( EntreeOrdreDuJour entree : listeEntreeOrdreDuJour )
        {
            if ( entree.getPdd(  ) != null )
            {
                fichierExposeMotifs = null;
                fichierProjetDelibere = null;
                fichierPhysique = null;
                readerExposeMotifs = null;
                readerProjetDelibere = null;

                fichierFilter.setPDD( entree.getPdd(  ).getIdPdd(  ) );

                List<Fichier> fichiers = FichierHome.findByFilter( fichierFilter, plugin );

                for ( Fichier fichier : fichiers )
                {
                    if ( ( fichier.getTypdeDocument(  ) != null ) &&
                            ( TypeDocumentEnum.EXPOSE_DES_MOTIFS.getId(  ) == fichier.getTypdeDocument(  ).getId(  ) ) )
                    {
                        fichierExposeMotifs = fichier;
                    }

                    if ( ( fichier.getTypdeDocument(  ) != null ) &&
                            ( TypeDocumentEnum.PROJET_DE_DELIBERE.getId(  ) == fichier.getTypdeDocument(  ).getId(  ) ) )
                    {
                        fichierProjetDelibere = fichier;
                    }
                }

                if ( ( fichierExposeMotifs != null ) && ( fichierProjetDelibere != null ) )
                {
                    fichierPhysique = FichierPhysiqueHome.findByPrimaryKey( fichierExposeMotifs.getFichier(  )
                                                                                               .getIdFichier(  ), plugin );

                    if ( fichierPhysique != null )
                    {
                        readerExposeMotifs = new PdfReader( fichierPhysique.getDonnees(  ) );
                        fichierPhysique = null;
                    }

                    fichierPhysique = FichierPhysiqueHome.findByPrimaryKey( fichierProjetDelibere.getFichier(  )
                                                                                                 .getIdFichier(  ),
                            plugin );

                    if ( fichierPhysique != null )
                    {
                        readerProjetDelibere = new PdfReader( fichierPhysique.getDonnees(  ) );
                    }

                    if ( ( readerExposeMotifs != null ) && ( readerProjetDelibere != null ) )
                    {
                        //creation du fichier pdf contenant la concatenation 
                        //de l'expose des motifs et du projet de deliberation
                        document = new Document(  );
                        fileOutputStreamWriter = new FileOutputStream( strLocalDir +
                                genereCodeFichier( entree.getPdd(  ) ) );
                        writer = new PdfCopy( document, fileOutputStreamWriter );
                        document.open(  );
                        nPageOffset = 0;
                        nPageOffset = OrdreDuJourUtils.concatenePdfReader( readerExposeMotifs, writer, nPageOffset );

                        //insertion d'une page blanche au debut de l'expose du projet de deliberation 
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
                        PdfStamper stamp = new PdfStamper( readerProjetDelibere, byteArrayOutputStream );
                        stamp.insertPage( 1, PageSize.A4 );
                        stamp.close(  );
                        byteArrayOutputStream.close(  );
                        OrdreDuJourUtils.concatenePdfReader( new PdfReader( byteArrayOutputStream.toByteArray(  ) ),
                            writer, nPageOffset );
                        document.close(  );
                        fileOutputStreamWriter.close(  );
                    }
                }
            }
        }
    }

    /**
     *
     * @param strHost strHost
     * @param strUserName strUserName
     * @param strPassword strPassword
     * @return FTPClient
     * @throws SocketException socketException
     * @throws IOException iOException
     */
    private static FTPClient ftpConnexion( String strHost, String strUserName, String strPassword )
        throws SocketException, IOException
    {
        FTPClient ftpClient = new FTPClient(  );
        ftpClient.connect( strHost );
        ftpClient.login( strUserName, strPassword );
        ftpClient.printWorkingDirectory(  );
        ftpClient.enterLocalPassiveMode(  );
        ftpClient.setFileType( FTP.BINARY_FILE_TYPE );

        return ftpClient;
    }
}
