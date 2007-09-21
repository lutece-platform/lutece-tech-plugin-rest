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
package fr.paris.lutece.plugins.ods.business.ordredujour;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysique;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysiqueHome;
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocumentEnum;
import fr.paris.lutece.plugins.ods.business.historique.Historique;
import fr.paris.lutece.plugins.ods.business.historique.HistoriqueHome;
import fr.paris.lutece.plugins.ods.business.modeleordredujour.ModeleOrdreDuJour;
import fr.paris.lutece.plugins.ods.business.modeleordredujour.ModeleOrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.generated.entete.Entete;
import fr.paris.lutece.plugins.ods.business.ordredujour.generated.pieddepage.PiedDePage;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementFilter;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementLiasseComparator;
import fr.paris.lutece.plugins.ods.service.manager.DocumentManager;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.xml.XmlUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * OrdreDuJourUtils
 * Classe contenant des méthodes utilitaires pour les gestion des ordres du jour 
 */
public class OrdreDuJourUtils
{
    public static final String TAG_ENTETE = "entete";
    public static final String TAG_PIED_DE_PAGE = "piedDePage";
    public static final String CONSTANTE_ETOILE = "=*=*=*";
    public static final String PROPERTY_SEANCE = "ods.all.label.seance";
    public static final String PROPERTY_DU = "ods.all.label.from";
    public static final String PROPERTY_AU = "ods.all.label.to";
    private static final String CONSTANTE_ESPACE = " ";
    private static final String CONSTANTE_OUVERTURE_ACCOLADE = "(";
    private static final String CONSTANTE_FERMETURE_ACCOLADE = ")";
    private static final String PROPERTY_ERE = "ods.all.label.ere";
    private static final String PROPERTY_EME = "ods.all.label.eme";
    private static final String PROPERTY_COMMISSION = "ods.odj.creation.label.commission";
    private static final String CONSTANTE_RAPPORTEUR_FEMININ_SINGULIER = "ods.odj.modificationOdj.label.rapporteure";
    private static final String CONSTANTE_RAPPORTEUR_FEMININ_PLURIEL = "ods.odj.modificationOdj.label.rapporteures";
    private static final String CONSTANTE_RAPPORTEUR_SINGULIER = "ods.odj.modificationOdj.label.rapporteur";
    private static final String CONSTANTE_RAPPORTEUR_PLURIEL = "ods.odj.modificationOdj.label.rapporteurs";
    private static final String CONSTANTE_VIRGULE = ",";
    private static final String CONSTANTE_POINT = ".";
    private static final String CONSTANTE_CIVILITE_MR = "M.";
    private static final String PROPERTY_VA = "ods.odj.label.amendementEtVoeux";
    private static final String PROPERTY_CODE_DATE_SEANCE = "odsOrdreDuJour.codeDateSeance";
    private static final String PROPERTY_LIASSE_TITRE_FICHIER = "ods.odj.liasse.fichier.titre";
    private static final String CONSTANTE_PACKAGE_ENTETE_GENERATION = "fr.paris.lutece.plugins.ods.business.ordredujour.generated.entete";
    private static final String CONSTANTE_PACKAGE_PIEDDEPAGE_GENERATION = "fr.paris.lutece.plugins.ods.business.ordredujour.generated.pieddepage";
    private static final String TAG_ENTREE_ENTETE = "entreeEntete";
    private static final String TAG_STYLE = "style";
    private static final String TAG_TEXTE_LIBRE = "texteLibre";
    private static final String TAG_ENTREE_PIED_DE_PAGE = "entreePiedDePage";

    /**
     *retourne le libelle du groupe de commissions passé en parametre
     * @param commissions la liste des commissions
     * @param locale locale
     * @return libelle du groupe de commissions passé en parametre
     */
    public static String getLibelleGroupeCommissions( List<Commission> commissions, Locale locale )
    {
        int ncpt = 1;
        StringBuffer strObjet = new StringBuffer(  );

        for ( Commission commission : commissions )
        {
            strObjet.append( commission.getNumero(  ) );

            if ( commission.getNumero(  ) == 1 )
            {
                strObjet.append( I18nService.getLocalizedString( PROPERTY_ERE, locale ) );
            }
            else
            {
                strObjet.append( I18nService.getLocalizedString( PROPERTY_EME, locale ) );
            }

            strObjet.append( CONSTANTE_ESPACE );

            strObjet.append( I18nService.getLocalizedString( PROPERTY_COMMISSION, locale ) );

            if ( ncpt != commissions.size(  ) )
            {
                strObjet.append( CONSTANTE_VIRGULE );
                strObjet.append( CONSTANTE_ESPACE );
                ncpt++;
            }
        }

        return strObjet.toString(  );
    }

    /**
     *retourne le libelle du groupe de rapporteur passé en parametre
     * @param rapporteurs la liste des rapporteurs
     * @param locale locale
     * @return le libelle du groupe de rapporteur passé en parametre
     */
    public static String getLibelleGroupeRapporteurs( List<Elu> rapporteurs, Locale locale )
    {
        StringBuffer strObjet = new StringBuffer(  );
        int ncpt = 1;
        int nbMasc = 0;
        for ( Elu elu : rapporteurs )
        {
        	if( elu.getCivilite().equals( CONSTANTE_CIVILITE_MR )) {
        		nbMasc++;
        	}
            strObjet.append( elu.getCivilite(  ) );
            strObjet.append( CONSTANTE_ESPACE );
            strObjet.append( elu.getPrenomElu(  ) );
            strObjet.append( CONSTANTE_ESPACE );
            strObjet.append( elu.getNomElu(  ) );
            strObjet.append( CONSTANTE_ESPACE );
            strObjet.append( CONSTANTE_OUVERTURE_ACCOLADE );
            strObjet.append( elu.getCommission(  ).getNumero(  ) );

            if ( elu.getCommission(  ).getNumero(  ) == 1 )
            {
                strObjet.append( I18nService.getLocalizedString( PROPERTY_ERE, locale ) );
            }
            else
            {
                strObjet.append( I18nService.getLocalizedString( PROPERTY_EME, locale ) );
            }

            strObjet.append( CONSTANTE_ESPACE );
            strObjet.append( I18nService.getLocalizedString( PROPERTY_COMMISSION, locale ) );
            strObjet.append( CONSTANTE_FERMETURE_ACCOLADE );

            if ( ncpt != rapporteurs.size(  ) )
            {
                strObjet.append( CONSTANTE_VIRGULE );
                strObjet.append( CONSTANTE_ESPACE );
                ncpt++;
            }
        }

        if ( rapporteurs.size(  ) == 1 )
        {
        	strObjet.append(CONSTANTE_ESPACE);
        	if( nbMasc > 0) 
        	{
            strObjet.append( I18nService.getLocalizedString( CONSTANTE_RAPPORTEUR_SINGULIER, locale ) +
                CONSTANTE_POINT );
        	}
        	else 
        	{
                strObjet.append( I18nService.getLocalizedString( CONSTANTE_RAPPORTEUR_FEMININ_SINGULIER, locale ) +
                        CONSTANTE_POINT );
        	}
        }
        else if ( rapporteurs.size(  ) > 1 )
        {
        	strObjet.append(CONSTANTE_ESPACE);
        	if( nbMasc > 0) 
        	{
        		strObjet.append( I18nService.getLocalizedString( CONSTANTE_RAPPORTEUR_PLURIEL, locale ) + CONSTANTE_POINT );	
        	}
        	else
        	{
        		strObjet.append( I18nService.getLocalizedString( CONSTANTE_RAPPORTEUR_FEMININ_PLURIEL, locale ) + CONSTANTE_POINT );
        	}        	
            
        }

        return strObjet.toString(  );
    }

    /**
     * Genere  la liasse PDF des voeux et amendements de l’ordre du jour définitif
     *  de la prochaine séance, pour une formation de conseil donnée.
     * @param ordreDuJour l'ordre du jour definitif en cours de taitement
     * @param plugin plugin
     * @param  locale la locale
     * @param dateForVersion la date de la  publication
     * @throws DocumentException  documentException
     * @throws IOException iOException
     */
    public static void genereLiasse( OrdreDuJour ordreDuJour, Plugin plugin, Timestamp dateForVersion, HttpServletRequest request, Locale locale )
        throws DocumentException, IOException
    {
        FichierPhysique fichierPhysique;
        Fichier fichierInitial;
        VoeuAmendementFilter voeuAmendementFilter = new VoeuAmendementFilter(  );
        voeuAmendementFilter.setIdFormationConseil( ordreDuJour.getFormationConseil(  ).getIdFormationConseil(  ) );
        voeuAmendementFilter.setIdSeance( ordreDuJour.getSeance(  ).getIdSeance(  ) );
        voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_CODE_FASCICULE );
        voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_REFERENCE_VA );

        List<VoeuAmendement> ListvoeuAmendements = VoeuAmendementHome.findLiasseByFilter( voeuAmendementFilter, plugin );
        List<Elu> rapporteursSauvegarde = null;
        List<Commission> commissionsSauvegarde = null;

        //creation de la liasse
        int nPageOffset = 0;
        Document document = null;
        PdfCopy writer = null;
        // etape 1: creation du document de sortie
        document = new Document(  );

        ByteArrayOutputStream byteArrayOutputStreamWriter = new ByteArrayOutputStream(  );
        writer = new PdfCopy( document, byteArrayOutputStreamWriter );

        BaseFont bf = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED );
        document.open(  );

        PdfReader pdfReader = insertEnteteInThePdfLiasse( ordreDuJour, locale );
        nPageOffset = concatenePdfReader( pdfReader, writer, nPageOffset );

        for ( VoeuAmendement voeuAmendement : ListvoeuAmendements )
        {
            if ( ( ( commissionsSauvegarde == null ) && ( voeuAmendement.getGroupeCommissions(  ) != null ) &&
                    ( voeuAmendement.getGroupeCommissions(  ).size(  ) != 0 ) ) ||
                    ( ( commissionsSauvegarde != null ) && ( commissionsSauvegarde.size(  ) != 0 ) &&
                    !voeuAmendement.getEqualListCommissions( commissionsSauvegarde ) ) )
            {
                //creation et insertion de la rupture du groupe de commissions 
                pdfReader = insertCommissionsAndRapporteursInThePdfLiasse( voeuAmendement.getGroupeCommissions(  ),
                        voeuAmendement.getGroupeRapporteurs(  ), locale );
                nPageOffset = concatenePdfReader( pdfReader, writer, nPageOffset );
                // on sauvegarde la commission de l'entrée
                //on sauvegarde la liste des rapporteurs de l'entrée
                commissionsSauvegarde = voeuAmendement.getGroupeCommissions(  );
                rapporteursSauvegarde = voeuAmendement.getGroupeRapporteurs(  );
            }

            else if ( ( ( rapporteursSauvegarde != null ) && ( rapporteursSauvegarde.size(  ) != 0 ) &&
                    ( voeuAmendement.getGroupeRapporteurs(  ) != null ) &&
                    ( voeuAmendement.getGroupeRapporteurs(  ).size(  ) != 0 ) &&
                    !voeuAmendement.getEqualListRapporteurs( rapporteursSauvegarde ) ) ||
                    ( ( rapporteursSauvegarde == null ) && ( voeuAmendement.getGroupeRapporteurs(  ) != null ) &&
                    ( voeuAmendement.getGroupeRapporteurs(  ).size(  ) != 0 ) ) )
            {
                // si la liste des rapporteurs de l'entrée est différente de
                // liste des rapporteurs sauvegardé
                // on affiche la liste des rapporteurs sauvegardé
                pdfReader = insertRapporteursInThePdfLiasse( voeuAmendement.getGroupeRapporteurs(  ) );
                nPageOffset = concatenePdfReader( pdfReader, writer, nPageOffset );
                rapporteursSauvegarde = voeuAmendement.getGroupeRapporteurs(  );
            }

            //on recupere le contenu du texte initial du va
            fichierInitial = FichierHome.findByPrimaryKey( voeuAmendement.getFichier(  ).getId(  ), plugin );
            fichierPhysique = FichierPhysiqueHome.findByPrimaryKey( fichierInitial.getFichier(  ).getIdFichier(  ),
                    plugin );

            if ( fichierPhysique != null )
            {
                PdfReader reader;

                if ( voeuAmendement.getEnLigne(  ) )
                {
                    reader = new PdfReader( fichierPhysique.getDonnees(  ) );
                }
                else
                {
                    Document documentVide = new Document(  );
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
                    PdfWriter writerVide = PdfWriter.getInstance( documentVide, byteArrayOutputStream );
                    documentVide.open(  );

                    PdfContentByte cb = writerVide.getDirectContent(  );
                    cb.beginText(  );
                    cb.endText(  );
                    documentVide.close(  );
                    byteArrayOutputStream.close(  );
                    reader = new PdfReader( byteArrayOutputStream.toByteArray(  ) );
                }

                //positionnement du tampon
                Rectangle rectangle = reader.getPageSize( 1 );
                float nX = 500;
                float nY = 1200;

                if ( rectangle != null )
                {
                    nX = reader.getPageSize( 1 ).width(  ) - 80;
                    nY = reader.getPageSize( 1 ).height(  ) - 80;
                }

                //on ajoute la référence du vœu ou amendement sur le fichier initial
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
                PdfStamper stamp = new PdfStamper( reader, byteArrayOutputStream );
                PdfContentByte over;
                over = stamp.getOverContent( 1 );
                over.beginText(  );
                over.setFontAndSize( bf, 18 );
                over.showTextAligned( Element.ALIGN_LEFT, voeuAmendement.getReferenceComplete(  ), nX, nY, 0 );
                over.endText(  );
                stamp.close(  );
                byteArrayOutputStream.close(  );
                reader = new PdfReader( byteArrayOutputStream.toByteArray(  ) );
                //fin de l'ajout de la reference
                nPageOffset = concatenePdfReader( reader, writer, nPageOffset );
            }
        }

        document.close(  );
        byteArrayOutputStreamWriter.close(  );

        //insertion de la liasse en base 
        Fichier newFichier = new Fichier(  );
        FichierPhysique newFichierPhysique = new FichierPhysique(  );
        byte[] tabDonnees = byteArrayOutputStreamWriter.toByteArray(  );
        newFichierPhysique.setDonnees( tabDonnees );
        newFichierPhysique.setIdFichier( FichierPhysiqueHome.create( newFichierPhysique, plugin ) );
        newFichier.setFichier( newFichierPhysique );

        String strTitre = I18nService.getLocalizedString( PROPERTY_LIASSE_TITRE_FICHIER, locale );
        strTitre += ( CONSTANTE_ESPACE + ordreDuJour.getFormationConseil(  ).getLibelle(  ) );
        newFichier.setTitre( strTitre );
        newFichier.setNom( strTitre );
        newFichier.setDatePublication( dateForVersion );
        newFichier.setExtension( DocumentManager.PDF_CONVERSION );
        newFichier.setTaille( tabDonnees.length );
        newFichier.setFormationConseil( ordreDuJour.getFormationConseil(  ) );
        // Creation de l'objet représentant le Fichier
        newFichier.setSeance( ordreDuJour.getSeance(  ) );
        newFichier.setTypdeDocument( TypeDocumentEnum.LIASSE_DES_VA.getTypeDocumentOnlyWidthId(  ) );
        newFichier.setEnLigne( true );
        int nIdFichier = FichierHome.create( newFichier, plugin );
        
        Fichier fichierANotifier = FichierHome.findByPrimaryKey( nIdFichier, plugin );
        OdsUtils.doNotifierUtilisateurs( fichierANotifier, request, plugin, locale );

        //gestion de l'historique
        Historique historique = new Historique(  );
        historique.setVersion( newFichier.getVersion(  ) + 1 );
        historique.setDatePublication( dateForVersion );
        historique.setIdDocument( newFichier.getId(  ) );
        HistoriqueHome.create( historique, plugin );
    }

    /**
     * ecrit dans  l'os passée en parametre la liasse PDF des voeux et amendements
     * de l'ordre du jour définitif passé en parametre.
     * @param ordreDuJour l'ordre du jour definitif en cours de taitement
     * @param plugin plugin
     * @param  locale la locale
     * @param os l'os dans laquelle on doit transferer les données
     * @throws DocumentException  documentException
     * @throws IOException iOException
     */
    public static void visualiseLiasse( OrdreDuJour ordreDuJour, Plugin plugin, Locale locale, OutputStream os )
        throws DocumentException, IOException
    {
        FichierPhysique fichierPhysique;
        Fichier fichierInitial;
        VoeuAmendementFilter voeuAmendementFilter = new VoeuAmendementFilter(  );

        if ( ( ordreDuJour.getFormationConseil(  ) != null ) && ( ordreDuJour.getSeance(  ) != null ) )
        {
            voeuAmendementFilter.setIdFormationConseil( ordreDuJour.getFormationConseil(  ).getIdFormationConseil(  ) );
            voeuAmendementFilter.setIdSeance( ordreDuJour.getSeance(  ).getIdSeance(  ) );
            voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_CODE_FASCICULE );
            voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_REFERENCE_VA );

            List<VoeuAmendement> listvoeuAmendements = VoeuAmendementHome.findLiasseByFilter( voeuAmendementFilter,
                    plugin );
            
            Collections.sort(listvoeuAmendements,new VoeuAmendementLiasseComparator());
            
            List<Elu> rapporteursSauvegarde = null;
            List<Commission> commissionsSauvegarde = null;

            //creation de la liasse
            int nPageOffset = 0;
            Document document = null;
            PdfCopy writer = null;
            // etape 1: creation du document de sortie
            document = new Document(  );
            writer = new PdfCopy( document, os );

            BaseFont bf = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED );
            document.open(  );

            PdfReader pdfReader = insertEnteteInThePdfLiasse( ordreDuJour, locale );
            nPageOffset = concatenePdfReader( pdfReader, writer, nPageOffset );

            for ( VoeuAmendement voeuAmendement : listvoeuAmendements )
            {
                if ( ( ( commissionsSauvegarde == null ) && ( voeuAmendement.getGroupeCommissions(  ) != null ) &&
                        ( voeuAmendement.getGroupeCommissions(  ).size(  ) != 0 ) ) ||
                        ( ( commissionsSauvegarde != null ) && ( commissionsSauvegarde.size(  ) != 0 ) &&
                        !voeuAmendement.getEqualListCommissions( commissionsSauvegarde ) ) )
                {
                    //creation et insertion de la rupture du groupe de commissions 
                    pdfReader = insertCommissionsAndRapporteursInThePdfLiasse( voeuAmendement.getGroupeCommissions(  ),
                            voeuAmendement.getGroupeRapporteurs(  ), locale );
                    nPageOffset = concatenePdfReader( pdfReader, writer, nPageOffset );
                    // on sauvegarde la commission de l'entrée
                    //on sauvegarde la liste des rapporteurs de l'entrée
                    commissionsSauvegarde = voeuAmendement.getGroupeCommissions(  );
                    rapporteursSauvegarde = voeuAmendement.getGroupeRapporteurs(  );
                }

                else if ( ( ( rapporteursSauvegarde != null ) && ( rapporteursSauvegarde.size(  ) != 0 ) &&
                        ( voeuAmendement.getGroupeRapporteurs(  ) != null ) &&
                        ( voeuAmendement.getGroupeRapporteurs(  ).size(  ) != 0 ) &&
                        !voeuAmendement.getEqualListRapporteurs( rapporteursSauvegarde ) ) ||
                        ( ( rapporteursSauvegarde == null ) && ( voeuAmendement.getGroupeRapporteurs(  ) != null ) &&
                        ( voeuAmendement.getGroupeRapporteurs(  ).size(  ) != 0 ) ) )
                {
                    // si la liste des rapporteurs de l'entrée est différente de
                    // liste des rapporteurs sauvegardé
                    // on affiche la liste des rapporteurs sauvegardé
                    pdfReader = insertRapporteursInThePdfLiasse( voeuAmendement.getGroupeRapporteurs(  ) );
                    nPageOffset = concatenePdfReader( pdfReader, writer, nPageOffset );
                    rapporteursSauvegarde = voeuAmendement.getGroupeRapporteurs(  );
                }

                //on recupere le contenu du texte initial du va
                fichierInitial = FichierHome.findByPrimaryKey( voeuAmendement.getFichier(  ).getId(  ), plugin );
                fichierPhysique = FichierPhysiqueHome.findByPrimaryKey( fichierInitial.getFichier(  ).getIdFichier(  ),
                        plugin );

                if ( fichierPhysique != null )
                {
                    PdfReader reader;

                    if ( voeuAmendement.getEnLigne(  ) )
                    {
                        reader = new PdfReader( fichierPhysique.getDonnees(  ) );
                    }
                    else
                    {
                        Document documentVide = new Document(  );
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
                        PdfWriter writerVide = PdfWriter.getInstance( documentVide, byteArrayOutputStream );
                        documentVide.open(  );

                        PdfContentByte cb = writerVide.getDirectContent(  );
                        cb.beginText(  );
                        cb.endText(  );
                        documentVide.close(  );
                        byteArrayOutputStream.close(  );
                        reader = new PdfReader( byteArrayOutputStream.toByteArray(  ) );
                    }

                    //positionnement du tampon
                    Rectangle rectangle = reader.getPageSize( 1 );
                    float nX = 500;
                    float nY = 1200;

                    if ( rectangle != null )
                    {
                        nX = reader.getPageSize( 1 ).width(  ) - 80;
                        nY = reader.getPageSize( 1 ).height(  ) - 80;
                    }

                    //on ajoute la référence du vœu ou amendement sur le fichier initial
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
                    PdfStamper stamp = new PdfStamper( reader, byteArrayOutputStream );
                    PdfContentByte over;
                    over = stamp.getOverContent( 1 );
                    over.beginText(  );
                    over.setFontAndSize( bf, 18 );
                    over.showTextAligned( Element.ALIGN_LEFT, voeuAmendement.getReferenceComplete(  ), nX, nY, 0 );
                    over.endText(  );
                    stamp.close(  );
                    byteArrayOutputStream.close(  );
                    reader = new PdfReader( byteArrayOutputStream.toByteArray(  ) );
                    //fin de l'ajout de la reference
                    nPageOffset = concatenePdfReader( reader, writer, nPageOffset );
                }
            }

            document.close(  );
        }
    }

    /**
     * Genere  la liasse PDF des voeux et amendements de l’ordre du jour définitif
     *  de la prochaine séance, pour une formation de conseil donnée.
     * @param ordreDuJour l'ordre du jour definitif en cours de taitement
     * @param plugin plugin
     * @param  locale la locale
     * @param dateForVersion la date de la  publication
     * @throws DocumentException  documentException
     * @throws IOException iOException
     */
    public static void genere( OrdreDuJour ordreDuJour, Plugin plugin, Timestamp dateForVersion, Locale locale )
        throws DocumentException, IOException
    {
        FichierPhysique fichierPhysique;
        Fichier fichierInitial;
        VoeuAmendementFilter voeuAmendementFilter = new VoeuAmendementFilter(  );
        voeuAmendementFilter.setIdFormationConseil( ordreDuJour.getFormationConseil(  ).getIdFormationConseil(  ) );
        voeuAmendementFilter.setIdSeance( ordreDuJour.getSeance(  ).getIdSeance(  ) );
        voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_CODE_FASCICULE );
        voeuAmendementFilter.addInOrderBy( VoeuAmendementFilter.ORDER_REFERENCE_VA );

        List<VoeuAmendement> ListvoeuAmendements = VoeuAmendementHome.findLiasseByFilter( voeuAmendementFilter, plugin );
        List<Elu> rapporteursSauvegarde = null;
        List<Commission> commissionsSauvegarde = null;

        //creation de la liasse
        int nPageOffset = 0;
        Document document = null;
        PdfCopy writer = null;
        // etape 1: creation du document de sortie
        document = new Document(  );

        ByteArrayOutputStream byteArrayOutputStreamWriter = new ByteArrayOutputStream(  );
        writer = new PdfCopy( document, byteArrayOutputStreamWriter );

        BaseFont bf = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED );
        document.open(  );

        PdfReader pdfReader = insertEnteteInThePdfLiasse( ordreDuJour, locale );
        nPageOffset = concatenePdfReader( pdfReader, writer, nPageOffset );

        for ( VoeuAmendement voeuAmendement : ListvoeuAmendements )
        {
            if ( ( ( commissionsSauvegarde == null ) && ( voeuAmendement.getGroupeCommissions(  ) != null ) &&
                    ( voeuAmendement.getGroupeCommissions(  ).size(  ) != 0 ) ) ||
                    ( ( commissionsSauvegarde != null ) && ( commissionsSauvegarde.size(  ) != 0 ) &&
                    !voeuAmendement.getEqualListCommissions( commissionsSauvegarde ) ) )
            {
                //creation et insertion de la rupture du groupe de commissions 
                pdfReader = insertCommissionsAndRapporteursInThePdfLiasse( voeuAmendement.getGroupeCommissions(  ),
                        voeuAmendement.getGroupeRapporteurs(  ), locale );
                nPageOffset = concatenePdfReader( pdfReader, writer, nPageOffset );
                // on sauvegarde la commission de l'entrée
                //on sauvegarde la liste des rapporteurs de l'entrée
                commissionsSauvegarde = voeuAmendement.getGroupeCommissions(  );
                rapporteursSauvegarde = voeuAmendement.getGroupeRapporteurs(  );
            }

            else if ( ( ( rapporteursSauvegarde != null ) && ( rapporteursSauvegarde.size(  ) != 0 ) &&
                    ( voeuAmendement.getGroupeRapporteurs(  ) != null ) &&
                    ( voeuAmendement.getGroupeRapporteurs(  ).size(  ) != 0 ) &&
                    !voeuAmendement.getEqualListRapporteurs( rapporteursSauvegarde ) ) ||
                    ( ( rapporteursSauvegarde == null ) && ( voeuAmendement.getGroupeRapporteurs(  ) != null ) &&
                    ( voeuAmendement.getGroupeRapporteurs(  ).size(  ) != 0 ) ) )
            {
                // si la liste des rapporteurs de l'entrée est différente de
                // liste des rapporteurs sauvegardé
                // on affiche la liste des rapporteurs sauvegardé
                pdfReader = insertRapporteursInThePdfLiasse( voeuAmendement.getGroupeRapporteurs(  ) );
                nPageOffset = concatenePdfReader( pdfReader, writer, nPageOffset );
                rapporteursSauvegarde = voeuAmendement.getGroupeRapporteurs(  );
            }

            //on recupere le contenu du texte initial du va
            fichierInitial = FichierHome.findByPrimaryKey( voeuAmendement.getFichier(  ).getId(  ), plugin );
            fichierPhysique = FichierPhysiqueHome.findByPrimaryKey( fichierInitial.getFichier(  ).getIdFichier(  ),
                    plugin );

            if ( fichierPhysique != null )
            {
                PdfReader reader;

                if ( voeuAmendement.getEnLigne(  ) )
                {
                    reader = new PdfReader( fichierPhysique.getDonnees(  ) );
                }
                else
                {
                    Document documentVide = new Document(  );
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
                    PdfWriter writerVide = PdfWriter.getInstance( documentVide, byteArrayOutputStream );
                    documentVide.open(  );

                    PdfContentByte cb = writerVide.getDirectContent(  );
                    cb.beginText(  );
                    cb.endText(  );
                    documentVide.close(  );
                    byteArrayOutputStream.close(  );
                    reader = new PdfReader( byteArrayOutputStream.toByteArray(  ) );
                }

                //positionnement du tampon
                Rectangle rectangle = reader.getPageSize( 1 );
                float nX = 500;
                float nY = 1200;

                if ( rectangle != null )
                {
                    nX = reader.getPageSize( 1 ).width(  ) - 80;
                    nY = reader.getPageSize( 1 ).height(  ) - 80;
                }

                //on ajoute la référence du vœu ou amendement sur le fichier initial
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
                PdfStamper stamp = new PdfStamper( reader, byteArrayOutputStream );
                PdfContentByte over;
                over = stamp.getOverContent( 1 );
                over.beginText(  );
                over.setFontAndSize( bf, 18 );
                over.showTextAligned( Element.ALIGN_LEFT, voeuAmendement.getReferenceComplete(  ), nX, nY, 0 );
                over.endText(  );
                stamp.close(  );
                byteArrayOutputStream.close(  );
                reader = new PdfReader( byteArrayOutputStream.toByteArray(  ) );
                //fin de l'ajout de la reference
                nPageOffset = concatenePdfReader( reader, writer, nPageOffset );
            }
        }

        document.close(  );
        byteArrayOutputStreamWriter.close(  );

        //insertion de la liasse en base 
        Fichier newFichier = new Fichier(  );
        FichierPhysique newFichierPhysique = new FichierPhysique(  );
        byte[] tabDonnees = byteArrayOutputStreamWriter.toByteArray(  );
        newFichierPhysique.setDonnees( tabDonnees );
        newFichierPhysique.setIdFichier( FichierPhysiqueHome.create( newFichierPhysique, plugin ) );
        newFichier.setFichier( newFichierPhysique );

        String strTitre = I18nService.getLocalizedString( PROPERTY_LIASSE_TITRE_FICHIER, locale );
        strTitre += ( CONSTANTE_ESPACE + ordreDuJour.getFormationConseil(  ).getLibelle(  ) );
        newFichier.setTitre( strTitre );
        newFichier.setNom( strTitre );
        newFichier.setDatePublication( dateForVersion );
        newFichier.setExtension( DocumentManager.PDF_CONVERSION );
        newFichier.setTaille( tabDonnees.length );
        newFichier.setFormationConseil( ordreDuJour.getFormationConseil(  ) );
        // Creation de l'objet représentant le Fichier
        newFichier.setSeance( ordreDuJour.getSeance(  ) );
        newFichier.setTypdeDocument( TypeDocumentEnum.LIASSE_DES_VA.getTypeDocumentOnlyWidthId(  ) );
        newFichier.setEnLigne( true );
        FichierHome.create( newFichier, plugin );

        //gestion de l'historique
        Historique historique = new Historique(  );
        historique.setVersion( newFichier.getVersion(  ) + 1 );
        historique.setDatePublication( dateForVersion );
        historique.setIdDocument( newFichier.getId(  ) );
        HistoriqueHome.create( historique, plugin );
    }

    /**
     * cette methode crée en memoire l'entete de la liasse pdf
     * @param  ordreDuJour l'ordre du jour
     * @param  locale la locale
     * @return PdfReader un objet pdfReader contenant le document pdf de la liste de rapporteurs
     * @throws DocumentException
     * @throws IOException
     */
    private static PdfReader insertEnteteInThePdfLiasse( OrdreDuJour ordreDuJour, Locale locale )
        throws DocumentException, IOException
    {
        BaseFont bf = BaseFont.createFont( BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.EMBEDDED );
        Document document = new Document(  );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
        PdfWriter writer = PdfWriter.getInstance( document, byteArrayOutputStream );
        document.open(  );

        PdfContentByte cb = writer.getDirectContent(  );

        cb.beginText(  );
        cb.setFontAndSize( bf, 16 );
        cb.showTextAligned( PdfContentByte.ALIGN_CENTER, ordreDuJour.getFormationConseil(  ).getLibelle(  ), 260, 720, 0 );
        cb.endText(  );
        cb.beginText(  );
        cb.setFontAndSize( bf, 14 );
        cb.showTextAligned( PdfContentByte.ALIGN_CENTER, CONSTANTE_ETOILE, 260, 690, 0 );
        cb.endText(  );
        cb.beginText(  );
        cb.setFontAndSize( bf, 16 );
        cb.showTextAligned( PdfContentByte.ALIGN_CENTER, I18nService.getLocalizedString( PROPERTY_SEANCE, locale ),
            260, 660, 0 );
        cb.endText(  );

        StringBuffer stringBuffer = new StringBuffer(  );
        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );
        String strDateDebut = dateFormat.format( ordreDuJour.getSeance(  ).getDateSeance(  ) ).toString(  );
        String strDateFin = dateFormat.format( ordreDuJour.getSeance(  ).getDateCloture(  ) ).toString(  );
        stringBuffer.append( I18nService.getLocalizedString( PROPERTY_DU, locale ) );
        stringBuffer.append( strDateDebut );
        stringBuffer.append( I18nService.getLocalizedString( PROPERTY_AU, locale ) );
        stringBuffer.append( strDateFin );

        cb.beginText(  );
        cb.setFontAndSize( bf, 18 );
        cb.showTextAligned( PdfContentByte.ALIGN_CENTER, stringBuffer.toString(  ), 260, 630, 0 );
        cb.endText(  );

        cb.beginText(  );
        cb.setFontAndSize( bf, 16 );
        cb.showTextAligned( PdfContentByte.ALIGN_CENTER, CONSTANTE_ETOILE, 260, 610, 0 );
        cb.endText(  );

        cb.beginText(  );
        cb.setFontAndSize( bf, 16 );
        cb.showTextAligned( PdfContentByte.ALIGN_CENTER, I18nService.getLocalizedString( PROPERTY_VA, locale ), 260,
            580, 0 );
        cb.endText(  );

        document.close(  );
        byteArrayOutputStream.close(  );

        return new PdfReader( byteArrayOutputStream.toByteArray(  ) );
    }

    /**
     * cette methode crée en memoire un document pdf qui contient une  liste de rapporteurs
     * @param  listRapporteurs la liste des rapporteurs à inserer dans le fichier pdf créé en memoire
     * @param  locale la locale
     * @return PdfReader un objet pdfReader contenant le document pdf de la liste de rapporteurs
     * @throws DocumentException
     * @throws IOException
     */
    private static PdfReader insertRapporteursInThePdfLiasse( List<Elu> listRapporteurs )
        throws DocumentException, IOException
    {
        BaseFont bf = BaseFont.createFont( BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.EMBEDDED );

        //FontFactory.getFont(FontFactory.COURIER, 20, Font.NORMAL,
        //		new Color(255, 0, 0)));
        Document document = new Document(  );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
        PdfWriter writer = PdfWriter.getInstance( document, byteArrayOutputStream );
        document.open(  );

        PdfContentByte cb = writer.getDirectContent(  );
        StringBuffer strObjet;
        int nCpt = 0;

        for ( Elu elu : listRapporteurs )
        {
            strObjet = new StringBuffer(  );
            strObjet.append( elu.getCivilite(  ) );
            strObjet.append( CONSTANTE_ESPACE );
            strObjet.append( elu.getPrenomElu(  ) );
            strObjet.append( CONSTANTE_ESPACE );
            strObjet.append( elu.getNomElu(  ) );
            strObjet.append( CONSTANTE_ESPACE );
            cb.beginText(  );
            cb.setFontAndSize( bf, 12 );
            cb.showTextAligned( PdfContentByte.ALIGN_CENTER, strObjet.toString(  ), 260, 720 - ( 20 * nCpt ), 0 );
            cb.endText(  );
            nCpt++;
        }

        document.close(  );
        byteArrayOutputStream.close(  );

        return new PdfReader( byteArrayOutputStream.toByteArray(  ) );
    }

    /**
     * cette methode crée en memoire un document pdf qui contient une  liste de commissions  et une liste de rapporteurs
     * @param listCommissions la liste des commissions à inserer dans le fichier pdf créé en memoire
     * @param  locale la locale
     * @param  listRapporteurs la liste des rapporteurs à inserer dans le fichier pdf créé en memoire
     * @return PdfReader un objet pdfReader contenant le document pdf de la liste de commissions et des rapporteurs
     * @throws DocumentException
     * @throws IOException
     */
    private static PdfReader insertCommissionsAndRapporteursInThePdfLiasse( List<Commission> listCommissions,
        List<Elu> listRapporteurs, Locale locale ) throws DocumentException, IOException
    {
        Document document = new Document(  );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
        PdfWriter writer = PdfWriter.getInstance( document, byteArrayOutputStream );
        BaseFont bf = BaseFont.createFont( BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.EMBEDDED );
        int nCpt = 0;
        document.open(  );

        StringBuffer strObjet;
        PdfContentByte cb = writer.getDirectContent(  );

        for ( Commission commission : listCommissions )
        {
            strObjet = new StringBuffer(  );
            strObjet.append( commission.getNumero(  ) );

            if ( commission.getNumero(  ) == 1 )
            {
                strObjet.append( I18nService.getLocalizedString( PROPERTY_ERE, locale ) );
            }
            else
            {
                strObjet.append( I18nService.getLocalizedString( PROPERTY_EME, locale ) );
            }

            strObjet.append( CONSTANTE_ESPACE );
            strObjet.append( I18nService.getLocalizedString( PROPERTY_COMMISSION, locale ) );
            cb.beginText(  );
            cb.setFontAndSize( bf, 16 );
            cb.showTextAligned( PdfContentByte.ALIGN_CENTER, strObjet.toString(  ), 260, 720 - ( 20 * nCpt ), 0 );
            cb.endText(  );
            nCpt++;
        }

        if ( listRapporteurs != null )
        {
            cb.beginText(  );
            cb.setFontAndSize( bf, 14 );
            cb.showTextAligned( PdfContentByte.ALIGN_CENTER, CONSTANTE_ETOILE, 260, 720 - ( 20 * nCpt ), 0 );
            cb.endText(  );

            for ( Elu elu : listRapporteurs )
            {
                nCpt++;
                strObjet = new StringBuffer(  );
                strObjet.append( elu.getCivilite(  ) );
                strObjet.append( CONSTANTE_ESPACE );
                strObjet.append( elu.getPrenomElu(  ) );
                strObjet.append( CONSTANTE_ESPACE );
                strObjet.append( elu.getNomElu(  ) );
                strObjet.append( CONSTANTE_ESPACE );
                cb.beginText(  );
                cb.setFontAndSize( bf, 12 );
                cb.showTextAligned( PdfContentByte.ALIGN_CENTER, strObjet.toString(  ), 260, 720 - ( 20 * nCpt ), 0 );
                cb.endText(  );
            }
        }

        document.close(  );
        byteArrayOutputStream.close(  );

        return new PdfReader( byteArrayOutputStream.toByteArray(  ) );
    }

    /**
     * cette methode ajoute a la suite du fichier pdfWriter le fichier pdfReader
     * @param pdfReader le fichier à concatener au fichier pdfWriter
     * @param pdfWriter le fichier dans lequel on ajoute le contenu du fichier pdfReader
     * @param nPageOffset le nombre de page du fichier pdfWriter
     * @return le nombre de page du fichier pdfWriter apres concatenation
     * @throws DocumentException
     * @throws IOException
     */
    public static int concatenePdfReader( PdfReader pdfReader, PdfCopy pdfWriter, int nPageOffset )
        throws DocumentException, IOException
    {
        // we retrieve the total number of pages
        int n = pdfReader.getNumberOfPages(  );

        //on ajoute le contenu de pdfReader au contenu de pdfWriter
        PdfImportedPage page;

        for ( int i = 0; i < n; )
        {
            ++i;
            page = pdfWriter.getImportedPage( pdfReader, i );
            pdfWriter.addPage( page );
        }

        PRAcroForm form = pdfReader.getAcroForm(  );

        if ( form != null )
        {
            pdfWriter.copyAcroForm( pdfReader );
        }

        return ( nPageOffset + n );
    }

    /**
     * Retourne l'en-tête de l'ordre du jour passé en paramètre
     * @param odj l'ordre du jour
     * @return l'en-tête de l'ordre du jour passé en paramètre
     */
    public static Entete getEntete( OrdreDuJour odj, Plugin plugin )
    {
        Entete entete = null;

        if ( !odj.getPublie(  ) )
        {
            //si l'ordre du jour n'est pas publie on recupere l'entete du modele d'ordre du jour correspondant
            ModeleOrdreDuJour modele;
            OrdreDuJourFilter ordreDuJourFilter = new OrdreDuJourFilter(  );

            if ( odj.getCommission(  ) != null )
            {
                ordreDuJourFilter.setIdCommission( odj.getCommission(  ).getIdCommission(  ) );
            }

            if ( odj.getFormationConseil(  ) != null )
            {
                ordreDuJourFilter.setIdFormationConseil( odj.getFormationConseil(  ).getIdFormationConseil(  ) );
            }

            if ( odj.getTypeOrdreDuJour(  ) != null )
            {
                ordreDuJourFilter.setIdType( odj.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) );
            }

            modele = ModeleOrdreDuJourHome.findByFilter( ordreDuJourFilter, plugin );

            if ( ( modele != null ) && ( modele.getEnteteDocument(  ) != null ) )
            {
                FichierPhysique enteteFichierPhysique = modele.getEnteteDocument(  );
                enteteFichierPhysique = FichierPhysiqueHome.findByPrimaryKey( enteteFichierPhysique.getIdFichier(  ),
                        plugin );

                if ( enteteFichierPhysique.getDonnees(  ) != null )
                {
                    try
                    {
                        entete = analyzeEnteteXML( enteteFichierPhysique.getDonnees(  ) );
                    }

                    catch ( JAXBException e )
                    {
                        AppLogService.error( e );
                    }

                    catch ( Exception e )
                    {
                        AppLogService.error( e );
                    }
                }
            }
        }
        else
        {
            //l'ordre du jour est publié
            if ( odj.getXmlEntete(  ) != null )
            {
                try
                {
                    entete = analyzeEnteteXML( odj.getXmlEntete(  ).getBytes( OdsConstants.UTF8 ) );
                }
                catch ( JAXBException e )
                {
                    AppLogService.error( e );
                }
                catch ( Exception e )
                {
                    AppLogService.error( e );
                }
            }
        }

        return entete;
    }

    /**
     * retourne le pied de page  de l'ordre du jour passé en parametre
     * @param odj l'ordre du jour
     * @return
     */
    public static PiedDePage getPiedDePage( OrdreDuJour odj, Plugin plugin )
    {
        PiedDePage piedDePage = null;

        if ( !odj.getPublie(  ) )
        {
            //si l'ordre du jour n'est pas publie on recupere l'entete du modele d'ordre du jour correspondant
            ModeleOrdreDuJour modele;
            OrdreDuJourFilter ordreDuJourFilter = new OrdreDuJourFilter(  );

            if ( odj.getCommission(  ) != null )
            {
                ordreDuJourFilter.setIdCommission( odj.getCommission(  ).getIdCommission(  ) );
            }

            if ( odj.getFormationConseil(  ) != null )
            {
                ordreDuJourFilter.setIdFormationConseil( odj.getFormationConseil(  ).getIdFormationConseil(  ) );
            }

            if ( odj.getTypeOrdreDuJour(  ) != null )
            {
                ordreDuJourFilter.setIdType( odj.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) );
            }

            modele = ModeleOrdreDuJourHome.findByFilter( ordreDuJourFilter, plugin );

            if ( ( modele != null ) && ( modele.getPiedDocument(  ) != null ) )
            {
                FichierPhysique piedDePageFichierPhysique = modele.getPiedDocument(  );
                piedDePageFichierPhysique = FichierPhysiqueHome.findByPrimaryKey( piedDePageFichierPhysique.getIdFichier(  ),
                        plugin );

                if ( piedDePageFichierPhysique.getDonnees(  ) != null )
                {
                    try
                    {
                        piedDePage = analyzePiedDePageXML( piedDePageFichierPhysique.getDonnees(  ) );
                    }
                    catch ( JAXBException e )
                    {
                        AppLogService.error( e );
                    }

                    catch ( Exception e )
                    {
                        AppLogService.error( e );
                    }
                }
            }
        }
        else
        {
            //l'ordre du jour est publié
            if ( odj.getXmlPiedDePage(  ) != null )
            {
                try
                {
                    piedDePage = analyzePiedDePageXML( odj.getXmlPiedDePage(  ).getBytes( OdsConstants.UTF8 ) );
                }
                catch ( JAXBException e )
                {
                    AppLogService.error( e );
                }

                catch ( Exception e )
                {
                    AppLogService.error( e );
                }
            }
        }

        return piedDePage;
    }

    /**
     * Cette méthode permet de valider le XMl passé en paramètre à l'aide
     * d'un fichier XSD et de retourner l'objet PiedDePage correspondant.
     * Si le fichier est valide alors l'objet PiedDePage est instancié et retourné
     *
     * @param  ByteArrayXML  Flux xml qui va etre analisé
     * @return PiedDePage Objet qui contient toutes les données du fichier XML PiedDePage
     * @throws JAXBException JAXBException
     * @throws IOException IOException
     * @throws SAXException SAXException
     * @throws ParserConfigurationException ParserConfigurationException
     * @throws ParseException parse exception
     */
    private static PiedDePage analyzePiedDePageXML( byte[] byteArrayXML )
        throws JAXBException, IOException
    {
        /* create a JAXBContext capable of handling classes generated into */
        JAXBContext jc = JAXBContext.newInstance( CONSTANTE_PACKAGE_PIEDDEPAGE_GENERATION );

        /* create an Unmarshaller */
        Unmarshaller u = jc.createUnmarshaller(  );
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( byteArrayXML );

        /* unmarshal an invalid po instance document into a tree of Java
         * content objects composed of classes from the primer.po package */
        PiedDePage piedDePage = (PiedDePage) u.unmarshal( byteArrayInputStream );
        byteArrayInputStream.close(  );

        return piedDePage;
    }

    /**
     * Cette méthode permet de valider le XMl passé en paramètre à l'aide
     * d'un fichier XSD et de retourner l'objet entete coorespondant.
     * Si le fichier est valide alors l'objet Affaire est instancié et retourné
     *
     * @param  ByteArrayXML  Flux xml qui va etre analisé
     * @return Affaire Objet qui contient toutes les données du fichier XML
     * @throws JAXBException JAXBException
     * @throws IOException IOException
     * @throws SAXException SAXException
     * @throws ParserConfigurationException ParserConfigurationException
     * @throws ParseException parse exception
     */
    private static Entete analyzeEnteteXML( byte[] byteArrayXML )
        throws JAXBException, IOException
    {
        /* create a JAXBContext capable of handling classes generated into */
        JAXBContext jc = JAXBContext.newInstance( CONSTANTE_PACKAGE_ENTETE_GENERATION );

        /* create an Unmarshaller */
        Unmarshaller u = jc.createUnmarshaller(  );
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream( byteArrayXML );

        /* unmarshal an invalid po instance document into a tree of Java
         * content objects composed of classes from the primer.po package */
        Entete entete = (Entete) u.unmarshal( byteArrayInputStream );
        byteArrayInputStream.close(  );

        return entete;
    }

    /**
     *@param entete  entete
     * @return retourne le fragment de code xml representant l'entete a partir de l'objet entete
     */
    public static String getXmlEntete( Entete entete, Seance seance )
    {
        StringBuffer buffer = new StringBuffer(  );
        XmlUtil.beginElement( buffer, TAG_ENTETE );

        for ( Entete.EntreeEntete entree : entete.getEntreeEntete(  ) )
        {
            buffer.append( getXmlEntreeEntete( entree, seance ) );
        }

        XmlUtil.endElement( buffer, TAG_ENTETE );

        return buffer.toString(  );
    }

    /**
     * @param entree entree
     * @return retourne le fragment de code xml representant une entree de l'entete  a partir de l'objet entree
     */
    private static String getXmlEntreeEntete( Entete.EntreeEntete entree, Seance seance )
    {
        StringBuffer buffer = new StringBuffer(  );
        XmlUtil.beginElement( buffer, TAG_ENTREE_ENTETE );
        OdsUtils.addElement( buffer, TAG_STYLE, entree.getStyle(  ) );
        OdsUtils.addElement( buffer, TAG_TEXTE_LIBRE, replaceDate( entree.getTexteLibre(  ), seance ) );
        XmlUtil.endElement( buffer, TAG_ENTREE_ENTETE );

        return buffer.toString(  );
    }

    /**
     *
     * @param piedDePage PiedDePage
     * @return retourne le fragment de code xml representant le pied de page  a partir de l'objet piedDePage
     */
    public static String getXmlPiedDePage( PiedDePage piedDePage, Seance seance )
    {
        StringBuffer buffer = new StringBuffer(  );
        XmlUtil.beginElement( buffer, TAG_PIED_DE_PAGE );

        for ( PiedDePage.EntreePiedDePage entree : piedDePage.getEntreePiedDePage(  ) )
        {
            buffer.append( getXmlEntreePiedDePage( entree, seance ) );
        }

        XmlUtil.endElement( buffer, TAG_PIED_DE_PAGE );

        return buffer.toString(  );
    }

    /**
     *
     * @param entree Entree
     * @return retourne le fragment de code xml representant une entree du pied de page
     */
    private static String getXmlEntreePiedDePage( PiedDePage.EntreePiedDePage entree, Seance seance )
    {
        StringBuffer buffer = new StringBuffer(  );
        XmlUtil.beginElement( buffer, TAG_ENTREE_PIED_DE_PAGE );
        OdsUtils.addElement( buffer, TAG_STYLE, entree.getStyle(  ) );
        OdsUtils.addElement( buffer, TAG_TEXTE_LIBRE, replaceDate( entree.getTexteLibre(  ), seance ) );
        XmlUtil.endElement( buffer, TAG_ENTREE_PIED_DE_PAGE );

        return buffer.toString(  );
    }

    /**
     * Remplace dans la chaine de caractère 'strEntree',
     * toutes les constantes représentants la date du conseil
     * par la date du conseil de la séance passée en paramètre
     * @param strEntree strEntree
     * @param seance la seance
     * @return la chaine de caractère modifiée
     */
    private static String replaceDate( String strEntree, Seance seance )
    {
    	String strEntreeResultat = strEntree ;
        if ( seance != null )
        {
            String strCodeDateSeance = AppPropertiesService.getProperty( PROPERTY_CODE_DATE_SEANCE );

            if ( strCodeDateSeance != null )
            {
            	strEntreeResultat = strEntreeResultat.replace( strCodeDateSeance, OdsUtils.getDateConseil( seance ) );
            }
        }

        return strEntreeResultat;
    }

    /**
     * Remplace dans l'entete de l'ordre du jour,
     * toutes les constantes représentants la date du conseil
     * par la date du conseil de la séance passée en paramètre
     * @param entete entete
     * @param seance la seance
     */
    public static void replaceDate( Entete entete, Seance seance )
    {
        if ( ( entete != null ) && ( seance != null ) )
        {
            for ( Entete.EntreeEntete entree : entete.getEntreeEntete(  ) )
            {
                entree.setTexteLibre( replaceDate( entree.getTexteLibre(  ), seance ) );
            }
        }
    }

    /**
     * Remplace dans le pied de page  de l'ordre du jour,
     * toutes les constantes représentants la date du conseil
     * par la date du conseil de la séance passée en paramètre
     * @param piedDePage piedDePage
     * @param seance Seance
     */
    public static void replaceDate( PiedDePage piedDePage, Seance seance )
    {
        if ( ( piedDePage != null ) && ( seance != null ) )
        {
            for ( PiedDePage.EntreePiedDePage entree : piedDePage.getEntreePiedDePage(  ) )
            {
                entree.setTexteLibre( replaceDate( entree.getTexteLibre(  ), seance ) );
            }
        }
    }
}
