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
package fr.paris.lutece.plugins.ods.web.utils;

import fr.paris.lutece.plugins.mylutece.modules.wssodatabase.authentication.business.WssoUser;
import fr.paris.lutece.plugins.mylutece.modules.wssodatabase.authentication.business.WssoUserHome;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierFilter;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilEnum;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourFilter;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.pdd.PDDFilter;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.utilisateur.Utilisateur;
import fr.paris.lutece.plugins.ods.business.utilisateur.UtilisateurHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementFilter;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementHome;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsMarks;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.xml.XmlUtil;

import java.sql.Timestamp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * Classe utilitaire
 *
 */
public class OdsUtils
{
    private static final String PROPERTY_DU = "ods.all.label.from";
    private static final String PROPERTY_AU = "ods.all.label.to";
    private static final String CONSTANTE_ESPACE = " ";    

    private static final String PROPERTY_ADRESSE_MAIL_OBJET_FICHIERS = "ods.notification.mail.fichier.objet";
    private static final String SEPARATOR_MAIL_LIST = AppPropertiesService.getProperty( "mail.list.separator" );
    private static final String TEMPLATE_NOTIFICATION_FICHIER = "admin/plugins/ods/notification/notification_nouveau_fichier.html";

    /**
     *
     * @param strXmlBuffer buffer
     * @param strTag tag généré dans le XML
     * @param strValue Valeur de la balise
     */
    public static void addElement( StringBuffer strXmlBuffer, String strTag, String strValue )
    {
        if ( ( strValue != null ) && !strValue.equals( "null" ) &&
                !strValue.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            XmlUtil.addElement( strXmlBuffer, strTag, strValue );
        }
        else
        {
            XmlUtil.addElement( strXmlBuffer, strTag, OdsConstants.CONSTANTE_CHAINE_VIDE );
        }
    }

    /**
     *
     * @param strXmlBuffer buffer
     * @param strTag tag généré dans le XML
     * @param strValue Valeur de la balise
     */
    public static void addElement( StringBuffer strXmlBuffer, String strTag, int strValue )
    {
        XmlUtil.addElement( strXmlBuffer, strTag, strValue );
    }

    /**
     * Transfome une date en format string de type dd/MM/yyyy en objet Timestamp
     * @param strDate Date à transformer
     * @param isStartOfDayHour TRUE si l'heure doit etre 00h01 FALSE si l'heure doit etre 23H59
     * @return objet Timestamp correspondant à la date donné en paramètre
     */
    public static Timestamp getDate( String strDate, boolean isStartOfDayHour )
    {
        if ( ( strDate == null ) || ( strDate.trim(  ).length(  ) != OdsConstants.CONSTANTE_PATTERN_DATE.length(  ) ) )
        {
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat( OdsConstants.CONSTANTE_PATTERN_DATE );
        dateFormat.setLenient( false );

        Date date;

        try
        {
            date = dateFormat.parse( strDate.trim(  ) );
        }
        catch ( ParseException e )
        {
            return null;
        }

        Calendar caldate = new GregorianCalendar(  );
        caldate.setTime( date );
        caldate.set( Calendar.MILLISECOND, 0 );
        caldate.set( Calendar.SECOND, 0 );

        if ( isStartOfDayHour )
        {
            caldate.set( Calendar.HOUR_OF_DAY, caldate.getActualMinimum( Calendar.HOUR_OF_DAY ) );
            caldate.set( Calendar.MINUTE, caldate.getActualMinimum( Calendar.MINUTE ) );
        }
        else
        {
            caldate.set( Calendar.HOUR_OF_DAY, caldate.getActualMaximum( Calendar.HOUR_OF_DAY ) );
            caldate.set( Calendar.MINUTE, caldate.getActualMaximum( Calendar.MINUTE ) );
        }

        Timestamp timeStamp = new Timestamp( caldate.getTimeInMillis(  ) );

        return timeStamp;
    }

    /**
     * Retourne la date du jour
     * @return la date du jour
     */
    public static Timestamp getCurrentDate(  )
    {
        return new Timestamp( GregorianCalendar.getInstance(  ).getTimeInMillis(  ) );
    }

    /**
     * Recuperation de  la liste des pdds inscrits à l'ordre du jour de reference
     * pour le conseil general et municipal
     * @param request la requête Http
     * @param plugin le plugin ODS
     * @param seance la seance
     * @return la liste des Pdds inscrits à l'ordre du jour de référence
     */
    public static List<PDD> getPddsInscritsOdj( Seance seance, HttpServletRequest request, Plugin plugin )
    {
        String strIdFormationConseil = null;
        String strType = null;
        String strIdDirection = null;
        String strDelegationService = null;

        if ( request != null )
        {
            strIdFormationConseil = request.getParameter( OdsParameters.ID_FORMATION_CONSEIL );
            strType = request.getParameter( OdsParameters.TYPE );
            strIdDirection = request.getParameter( OdsParameters.ID_DIRECTION );
            strDelegationService = request.getParameter( OdsParameters.PDD_DELEG_SERVICE_PUBLIC );
        }

        //on recupere l'ordre du jour de reference pour le conseil general
        OrdreDuJourFilter ordreDuJourFilter = new OrdreDuJourFilter(  );
        ordreDuJourFilter.setIdPublie( 1 );
        ordreDuJourFilter.setIdSauvegarde( 0 );
        ordreDuJourFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) );

        if ( seance != null )
        {
            ordreDuJourFilter.setIdSeance( seance.getIdSeance(  ) );
        }

        OrdreDuJour ordreDuJourReferenceConseilGeneral = OrdreDuJourHome.findOdjReferenceByFilter( ordreDuJourFilter,
                plugin );
        //on recupere l'ordre du jour de reference pour le conseil municipal
        ordreDuJourFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILMUNICIPAL.getId(  ) );

        OrdreDuJour ordreDuJourReferenceConseilMunicipal = OrdreDuJourHome.findOdjReferenceByFilter( ordreDuJourFilter,
                plugin );

        PDDFilter pddFilter = new PDDFilter(  );
        //on filtre sur le fait que les pdds doivent etre inscrits a un ordre du jour de reference
        pddFilter.setInscritODJ( 1 );
        pddFilter.setPublication( 1 );

        if ( ordreDuJourReferenceConseilGeneral != null )
        {
            pddFilter.setIdOdjReferenceFormationConseilGeneral( ordreDuJourReferenceConseilGeneral.getIdOrdreDuJour(  ) );
        }
        else
        {
            pddFilter.setIdOdjReferenceFormationConseilGeneral( PDDFilter.ALL_INT );
        }

        if ( ordreDuJourReferenceConseilMunicipal != null )
        {
            pddFilter.setIdOdjReferenceFormationConseilMunicipal( ordreDuJourReferenceConseilMunicipal.getIdOrdreDuJour(  ) );
        }
        else
        {
            pddFilter.setIdOdjReferenceFormationConseilMunicipal( PDDFilter.ALL_INT );
        }

        //Si les pdds sont filtrés sur la délégation de service public on l'ajoute au filtre
        if ( strDelegationService != null )
        {
            pddFilter.setDelegationService( 1 );
        }

        //si les pdds sont filtrés sur la formation conseil on rajoute le filtre formation conseil a la requete 
        if ( strIdFormationConseil != null )
        {
            try
            {
                pddFilter.setIdFormationConseil( Integer.parseInt( strIdFormationConseil ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
            }
        }

        //si les pdds sont filtrés par type (proposition ou projet ) on rajoute le filtre dans la requete
        if ( ( strType != null ) && strType.equals( PDD.CONSTANTE_TYPE_PROP ) )
        {
            pddFilter.setType( PDD.CONSTANTE_TYPE_PROP );
        }
        else if ( ( strType != null ) && strType.equals( PDD.CONSTANTE_TYPE_PROJET ) )
        {
            pddFilter.setType( PDD.CONSTANTE_TYPE_PROJET );
        }

        //si les pdds sont filtrés par direction  on rajoute le filtre dans la requete
        if ( strIdDirection != null )
        {
            try
            {
                int nIdDirection = Integer.parseInt( strIdDirection );

                return PDDHome.findByFilterByDirectionOrDirectionCoemetrice( pddFilter, plugin, nIdDirection );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }
        }

        return PDDHome.findByFilter( pddFilter, plugin );
    }

    /**
     * Recuperation de  la liste des pdds
     * Non inscrits à l'ordre du jour de reference de la prochaine seance
     * pour le conseil general et municipal
     * @param request la requête Http
     * @param plugin le plugin ODS
     * @param seance la seance
     * @return la liste des Pdds inscrits à l'ordre du jour de référence
     */
    public static List<PDD> getPddsNotInscritsOdj( Seance seance, HttpServletRequest request, Plugin plugin )
    {
        String strIdFormationConseil = request.getParameter( OdsParameters.ID_FORMATION_CONSEIL );
        String strIdDirection = request.getParameter( OdsParameters.ID_DIRECTION );
        String strType = request.getParameter( OdsParameters.TYPE );
        String strDelegationService = request.getParameter( OdsParameters.PDD_DELEG_SERVICE_PUBLIC );

        OrdreDuJourFilter ordreDuJourFilter = new OrdreDuJourFilter(  );

        // on recupere l'ordre du jour de reference pour le conseil general
        ordreDuJourFilter.setIdPublie( 1 );
        ordreDuJourFilter.setIdSauvegarde( 0 );

        if ( seance != null )
        {
            ordreDuJourFilter.setIdSeance( seance.getIdSeance(  ) );
        }

        ordreDuJourFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) );

        OrdreDuJour ordreDuJourReferenceConseilGeneral = OrdreDuJourHome.findOdjReferenceByFilter( ordreDuJourFilter,
                plugin );
        // on recupere l'ordre du jour de reference pour le conseil municipal
        ordreDuJourFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILMUNICIPAL.getId(  ) );

        OrdreDuJour ordreDuJourReferenceConseilMunicipal = OrdreDuJourHome.findOdjReferenceByFilter( ordreDuJourFilter,
                plugin );

        PDDFilter pddFilter = new PDDFilter(  );
        pddFilter.setPublication( 1 );

        //on filtre sur le fait que les pdds doivent appartenir a la prochaine seance 
        if ( seance != null )
        {
            pddFilter.setProchaineSeance( seance.getIdSeance(  ) );
        }

        //on ajoute le filtre non inscrit a l'ordre du jour de la prochaine seance 
        pddFilter.setInscritODJ( 0 );

        if ( ordreDuJourReferenceConseilGeneral != null )
        {
            pddFilter.setIdOdjReferenceFormationConseilGeneral( ordreDuJourReferenceConseilGeneral.getIdOrdreDuJour(  ) );
        }
        else
        {
            pddFilter.setIdOdjReferenceFormationConseilGeneral( PDDFilter.ALL_INT );
        }

        if ( ordreDuJourReferenceConseilMunicipal != null )
        {
            pddFilter.setIdOdjReferenceFormationConseilMunicipal( ordreDuJourReferenceConseilMunicipal.getIdOrdreDuJour(  ) );
        }
        else
        {
            pddFilter.setIdOdjReferenceFormationConseilMunicipal( PDDFilter.ALL_INT );
        }

        //si les pdds sont filtrés sur la délégation de service public on l'ajoute au filtre
        if ( strDelegationService != null )
        {
            pddFilter.setDelegationService( 1 );
        }

        //si les pdds sont filtrés sur la formation conseil on rajoute le filtre formation conseil a la requete
        if ( strIdFormationConseil != null )
        {
            try
            {
                pddFilter.setIdFormationConseil( Integer.parseInt( strIdFormationConseil ) );
            }
            catch ( NumberFormatException ne )
            {
                AppLogService.error( ne );
            }
        }

        //si les pdds sont filtrés par type (proposition ou projet ) on rajoute le filtre dans la requete
        if ( ( strType != null ) && strType.equals( PDD.CONSTANTE_TYPE_PROP ) )
        {
            pddFilter.setType( PDD.CONSTANTE_TYPE_PROP );
        }
        else
        {
            pddFilter.setType( PDD.CONSTANTE_TYPE_PROJET );
        }

        //si les pdds sont filtrés par direction  on rajoute le filtre dans la requete
        if ( strIdDirection != null )
        {
            try
            {
                int nIdDirection = Integer.parseInt( strIdDirection );

                return PDDHome.findByFilterByDirectionOrDirectionCoemetrice( pddFilter, plugin, nIdDirection );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }
        }

        return PDDHome.findByFilter( pddFilter, plugin );
    }

    /**
     * Recuperation de la liste des pdds publiés après la date de dernière connexion
     * de l'utilisateur courant
     * @param request la requête Http
     * @param plugin le plugin ODS
     * @return la liste des Pdds publiés après la date de dernière connexion de l'utilisateur courante
     */
    public static List<PDD> getNouveauxPddsPublies( HttpServletRequest request, Plugin plugin )
    {
        Utilisateur utilisateur = (Utilisateur) request.getSession(  ).getAttribute( OdsMarks.MARK_UTILISATEUR );
        Seance seance = SeanceHome.getProchaineSeance( plugin );

        if ( seance != null )
        {
            PDDFilter filter = new PDDFilter(  );
            if ( utilisateur != null ) 
            {
            	filter.setDatePublication( utilisateur.getDerniereConnexion(  ) );
            }
            filter.setPublication( 1 );
            filter.setProchaineSeance( seance.getIdSeance(  ) );

            // On récupère la liste des Pdd mis en ligne après la date de dernière connexion de l'utilisateur
            return PDDHome.findByFilter( filter, plugin );
        }

        return null;
    }

    /**
     * Recuperation de la liste des voeux et amendements publiés après la date de dernière connexion
     * de l'utilisateur courant
     * @param request la requête Http
     * @param plugin le plugin ODS
     * @return la liste des VAs publiés après la date de dernière connexion de l'utilisateur courante
     */
    public static List<VoeuAmendement> getNouveauxVAPublies( HttpServletRequest request, Plugin plugin )
    {
        Utilisateur utilisateur = (Utilisateur) request.getSession(  ).getAttribute( OdsMarks.MARK_UTILISATEUR );
        Seance seance = SeanceHome.getProchaineSeance( plugin );

        if ( seance != null )
        {
            VoeuAmendementFilter filter = new VoeuAmendementFilter(  );
            if ( utilisateur != null ) 
            {
            	filter.setDatePublication( utilisateur.getDerniereConnexion(  ) );
            }
            filter.setIdPublie( 1 );
            filter.setIdSeance( seance.getIdSeance(  ) );

            // On récupère la liste des VAs mis en ligne après la date de dernière connexion de l'utilisateur
            return VoeuAmendementHome.findVoeuAmendementListByFilter( filter, plugin );
        }

        return null;
    }

    /**
     * Recuperation de la liste des fichiers publiés après la date de dernière connexion
     * de l'utilisateur courant
     * @param request la requête Http
     * @param plugin le plugin ODS
     * @return la liste des fichiers publiés après al date de dernière connexion de l'utilisateur courante
     */
    public static List<Fichier> getNouveauxFichiersPublies( HttpServletRequest request, Plugin plugin )
    {
        Utilisateur utilisateur = (Utilisateur) request.getSession(  ).getAttribute( OdsMarks.MARK_UTILISATEUR );
        Seance seance = SeanceHome.getProchaineSeance( plugin );

        if ( seance != null )
        {
            FichierFilter filter = new FichierFilter(  );
            if ( utilisateur != null ) 
            {
            	filter.setDatePublication( utilisateur.getDerniereConnexion(  ) );
            }
            filter.setPublication( 1 );

            List<Fichier> listFichiers = FichierHome.findByFilter( filter, plugin );

            PDDFilter pddFilter = new PDDFilter(  );
            pddFilter.setPublication( 1 );
            pddFilter.setProchaineSeance( seance.getIdSeance(  ) );

            List<PDD> listPddProchaineSeance = PDDHome.findByFilter( pddFilter, plugin );

            // On ne garde que les fichiers pour la prochaine séance
            for ( PDD pdd : listPddProchaineSeance )
            {
                if ( pdd.getFichiers(  ) != null )
                {
                    listFichiers.addAll( pdd.getFichiers(  ) );
                }
            }

            // On récupère la liste des Fichiers mis en ligne après la date de dernière connexion de l'utilisateur
            return listFichiers;
        }

        return null;
    }

    /**
     * Recuperation de la liste des fichiers publiés après la date de dernière connexion
     * de l'utilisateur courant
     * @param request la requête Http
     * @param plugin le plugin ODS
     * @return la liste des fichiers publiés après al date de dernière connexion de l'utilisateur courante
     */
    public static List<OrdreDuJour> getNouveauxOdjPublies( HttpServletRequest request, Plugin plugin )
    {
        Utilisateur utilisateur = (Utilisateur) request.getSession(  ).getAttribute( OdsMarks.MARK_UTILISATEUR );
        Seance seance = SeanceHome.getProchaineSeance( plugin );

        if ( seance != null )
        {
            OrdreDuJourFilter filter = new OrdreDuJourFilter(  );
            if ( utilisateur != null ) 
            {
            	filter.setDatePublication( utilisateur.getDerniereConnexion(  ) );
            }
            filter.setIdPublie( 1 );
            filter.setIdSeance( seance.getIdSeance(  ) );

            // On récupère la liste des Fichiers mis en ligne après la date de dernière connexion de l'utilisateur
            return OrdreDuJourHome.findOrdreDuJourList( plugin, filter, true );
        }

        return null;
    }

    /**
     * Renvoie l'entier désigné par le paramètre donné dans la requête
     * @param strParameterName le nom du paramètre ( OdsParameters )
     * @param request la requete Http
     * @return l'entier désigné par le paramètre donné dans la requête
     */
    public static int getIntegerParameter( String strParameterName, HttpServletRequest request )
    {
        int nRetour = -1;

        if ( ( request != null ) && ( strParameterName != null ) &&
                !strParameterName.trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) &&
                ( request.getParameter( strParameterName ) != null ) &&
                !request.getParameter( strParameterName ).trim(  ).equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            try
            {
                nRetour = Integer.parseInt( request.getParameter( strParameterName ) );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }
        }

        return nRetour;
    }

    /**
     * Renvoie l'entier désigné par le paramètre donné dans la requête
     * @param strParameterName le nom du paramètre ( OdsParameters )
     * @return l'entier désigné par le paramètre donné dans la requête
     */
    public static int string2id( String strParameterName )
    {
        int nRetour = -1;

        if ( ( strParameterName != null ) &&
        		!strParameterName.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            try
            {
                nRetour = Integer.parseInt( strParameterName );
            }
            catch ( NumberFormatException nfe )
            {
                AppLogService.error( nfe );
            }
        }

        return nRetour;
    }

    /**
     * Renvoie la date du conseil sous la forme "du 'date_debut' au 'date_fin'"
     * @param seance la seance du conseil
     * @return la date du conseil
     */
    public static String getDateConseil( Seance seance )
    {
        StringBuffer buffer = new StringBuffer(  );
        SimpleDateFormat dateFormat = new SimpleDateFormat( "EEEE dd MMMM yyyy", Locale.FRENCH);
        

        String strDateDebut = dateFormat.format( seance.getDateSeance(  ) ).toString(  );
        String strDU = I18nService.getLocalizedString( PROPERTY_DU, Locale.getDefault(  ) );

        if ( strDU != null )
        {
            buffer.append( strDU );
        }

        buffer.append( CONSTANTE_ESPACE );
        buffer.append( strDateDebut );

        if ( seance.getDateCloture(  ) != seance.getDateSeance(  ) )
        {
            String strAu = I18nService.getLocalizedString( PROPERTY_AU, Locale.getDefault(  ) );

            if ( strAu != null )
            {
            	buffer.append( CONSTANTE_ESPACE );
                buffer.append( strAu );
            }

            buffer.append( CONSTANTE_ESPACE );

            String strDateFin = dateFormat.format( seance.getDateCloture(  ) ).toString(  );
            buffer.append( strDateFin );
        }

        return buffer.toString(  );
    }

    /**
     * Notifier tous les utilisateurs de la publication d'un nouveau fichier
     * @param fichier le fichier publié
     * @param request request
     */
    public static void doNotifierUtilisateurs( Fichier fichier, HttpServletRequest request, Plugin plugin, Locale locale )
    {
    	
        List<Utilisateur> listUtilisateurs = UtilisateurHome.findUserList( plugin );

        Seance seance = SeanceHome.getProchaineSeance( plugin );

        String[] listArguments = 
            {
                DateUtil.getDateString( seance.getDateSeance(  ) ), DateUtil.getDateString( seance.getDateCloture(  ) ),
            };

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( Fichier.MARK_FICHIER, fichier );
        model.put( Seance.MARK_SEANCE, seance );
        model.put( OdsMarks.MARK_URL_WEBAPP, AppPathService.getBaseUrl( request ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_NOTIFICATION_FICHIER, locale, model );

        for ( Object obj : WssoUserHome.findWssoUsersList( null ) )
        {
            if ( ( obj != null ) && ( obj instanceof WssoUser ) )
            {
                MailService.sendMail( ( (WssoUser) obj ).getEmail(  ), null, MailService.getNoReplyEmail(  ),
                    I18nService.getLocalizedString( PROPERTY_ADRESSE_MAIL_OBJET_FICHIERS, listArguments, locale ),
                    template.getHtml(  ) );
            }
        }

        for ( Utilisateur user : listUtilisateurs )
        {
            String mail = OdsConstants.CONSTANTE_CHAINE_VIDE;

            if ( ( user.getMailCopie1(  ) != null ) )
            {
                mail += user.getMailCopie1(  );
            }

            if ( ( user.getMailCopie2(  ) != null ) )
            {
                if ( !mail.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
                {
                    mail += SEPARATOR_MAIL_LIST;
                }

                mail += user.getMailCopie2(  );
            }

            MailService.sendMail( mail, null, MailService.getNoReplyEmail(  ),
                I18nService.getLocalizedString( PROPERTY_ADRESSE_MAIL_OBJET_FICHIERS, listArguments, locale ),
                template.getHtml(  ) );
        }
    }
}
