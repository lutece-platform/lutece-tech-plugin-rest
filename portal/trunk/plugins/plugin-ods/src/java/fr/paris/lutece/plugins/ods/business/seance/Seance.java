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
package fr.paris.lutece.plugins.ods.business.seance;

import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.xml.XmlUtil;

import java.sql.Timestamp;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * Reponsable de decrire une Seance
 *
 */
public class Seance
{
    public static final String MARK_PROCHAINE_SEANCE = "prochaine_seance";
    public static final String MARK_SEANCE = "seance";
    private static final String TAG_DATE_REUNION3 = "dateReunion3";
    private static final String TAG_DATE_REUNION2 = "dateReunion2";
    private static final String TAG_DATE_REUNION1 = "dateReunion1";
    private static final String TAG_DATE_REUNION_POST_COMMISSION = "dateReunionPostCommission";
    private static final String TAG_DATE_CONFERENCE_ORGANISATION = "dateConferenceOrganisation";
    private static final String TAG_DATE_LIMITE_DIFFUSION_DSP = "dateLimiteDiffusionDsp";
    private static final String TAG_DATE_LIMITE_DIFFUSION_PDD = "dateLimiteDiffusionPdd";
    private static final String TAG_DATE_LIMITE_DIFFUSION_QUESTIONS = "dateLimiteDiffusionQuestions";
    private static final String TAG_DATE_DEPOT_QUESTIONS_ORALES = "dateDepotQuestionsOrales";
    private static final String TAG_DATE_REMISE_COMPTE_RENDU = "dateRemiseCompteRendu";
    private static final String TAG_DATE_CLOTURE = "dateCloture";
    private static final String TAG_DATE_SEANCE = "dateSeance";
    private static final String TAG_ID = "id";
    private static final String PROPERTY_SEANCE_DATE_REMISE_CR_SOMMAIRE = "odsseance.dateRemiseCrSommaire";
    private static final int DEFAULT_SEANCE_DATE_REMISE_CR_SOMMAIRE = 8;
    private static final String PROPERTY_SEANCE_DATE_DEPOT_QUESTION = "odsseance.dateDepotQuestion";
    private static final int DEFAULT_SEANCE_DATE_DEPOT_QUESTION = -14;
    private static final String PROPERTY_SEANCE_DATE_LIMITE_DIFF_QUESTIONS = "odsseance.dateLimiteDiffQuestions";
    private static final int DEFAULT_SEANCE_DATE_LIMITE_DIFF_QUESTIONS = -3;
    private static final String PROPERTY_SEANCE_DATE_LIMITE_DIFF_PDD = "odsseance.dateLimiteDiffPdd";
    private static final int DEFAULT_SEANCE_DATE_LIMITE_DIFF_PDD = -13;
    private static final String PROPERTY_SEANCE_DATE_LIMITE_DIFF_DSP = "odsseance.dateLimiteDiffDsp";
    private static final int DEFAULT_SEANCE_DATE_LIMITE_DIFF_DSP = -16;
    private static final String PROPERTY_SEANCE_DATE_CONF_ORG = "odsseance.dateConfOrg";
    private static final int DEFAULT_SEANCE_DATE_CONF_ORG = -10;
    private static final String PROPERTY_SEANCE_DATE_REU_POST_COMM = "odsseance.dateReuPostComm";
    private static final int DEFAULT_SEANCE_DATE_REU_POST_COMM = -4;
    private static final String PROPERTY_SEANCE_DATE_REU_1 = "odsseance.dateReu1";
    private static final int DEFAULT_SEANCE_DATE_REU_1 = -6;
    private static final String PROPERTY_SEANCE_DATE_REU_2 = "odsseance.dateReu2";
    private static final int DEFAULT_SEANCE_DATE_REU_2 = -5;
    private static final String PROPERTY_SEANCE_DATE_REU_3 = "odsseance.dateReu3";
    private static final int DEFAULT_SEANCE_DATE_REU_3 = -4;
    private int _nIdSeance;
    private java.sql.Timestamp _dateSeance;
    private java.sql.Timestamp _dateCloture;
    private java.sql.Timestamp _dateRemiseCr;
    private java.sql.Timestamp _dateDepotQuestionsOrales;
    private java.sql.Timestamp _dateLimiteDiffQuestions;
    private java.sql.Timestamp _dateLimiteDiffPdd;
    private java.sql.Timestamp _dateLimiteDiffDsp;
    private java.sql.Timestamp _dateConfOrg;
    private java.sql.Timestamp _dateReuPostComm;
    private java.sql.Timestamp _dateReu1;
    private java.sql.Timestamp _dateReu2;
    private java.sql.Timestamp _dateReu3;
    private java.sql.Timestamp _datePublicationArchive ;
    private long _tailleArchive = -1;

    /**
     * Retourne la taille de l'archive contenant tous les documents de séance
     * @return _tailleArchive la taille de l'archive contenant tous les documents de séance
     */
    public long getTailleArchive(  )
    {
        return _tailleArchive;
    }

    /**
     * Affecte la taille de l'archive contenant tous les documents de séance
     * @param tailleArchive taille de l'archive contenant tous les documents de séance
     */
    public void setTailleArchive( long tailleArchive )
    {
        this._tailleArchive = tailleArchive;
    }

    /**
     * Retourne la date de publication de la l'archive contenant tous les documents de séance
     * @return getDatePublicationArchive date de publication de la l'archive contenant tous les documents de séance
     */
    public java.sql.Timestamp getDatePublicationArchive(  )
    {
        return _datePublicationArchive;
    }

    /**
     * Affecte la date de publication de la l'archive contenant tous les documents de séance
     * @param datePublicationArchive date de publication de la l'archive contenant tous les documents de séance
     */
    public void setDatePublicationArchive( java.sql.Timestamp datePublicationArchive )
    {
        this._datePublicationArchive = datePublicationArchive;
    }

    /**
     * Retourne la date de cloture de la seance
     * @return dateCloture date de cloture de la seance
     */
    public java.sql.Timestamp getDateCloture(  )
    {
        return _dateCloture;
    }

    /**
     * Affecte la date de cloture
     * @param dateCloture date de cloture
     */
    public void setDateCloture( java.sql.Timestamp dateCloture )
    {
        this._dateCloture = dateCloture;
    }

    /**
     * Retourne la date de la conférence d'organisation
     * @return dateConfOrg date de la conférence d'organisation
     */
    public java.sql.Timestamp getDateConfOrg(  )
    {
        return _dateConfOrg;
    }

    /**
     *  Affecte la date de la conférence d'organisation
     * @param dateConfOrg date de la conférence d'organisation
     */
    public void setDateConfOrg( java.sql.Timestamp dateConfOrg )
    {
        if ( dateConfOrg != null )
        {
            this._dateConfOrg = dateConfOrg;
        }
        else
        {
            recalculateDateConfOrg(  );
        }
    }

    /**
     * Retourne la date de dépot des questions orales
     * @return dateDepotQuestionsOrales date de dépot des questions orales
     */
    public java.sql.Timestamp getDateDepotQuestionsOrales(  )
    {
        return _dateDepotQuestionsOrales;
    }

    /**
     * Affecte la date de dépot des questions orales
     * @param dateDepotQuestionsOrales date de dépot des questions orales
     */
    public void setDateDepotQuestionsOrales( java.sql.Timestamp dateDepotQuestionsOrales )
    {
        if ( dateDepotQuestionsOrales != null )
        {
            this._dateDepotQuestionsOrales = dateDepotQuestionsOrales;
        }
        else
        {
            recalculateDateDepotQuestion(  );
        }
    }

    /**
     * Retourne la date limite de diffusion des questions
     * @return dateLimiteDiffQuestions date limite de diffusion des questions
     */
    public java.sql.Timestamp getDateLimiteDiffQuestions(  )
    {
        return _dateLimiteDiffQuestions;
    }

    /**
     * Affecte la date limite de diffusion des questions
     * @param dateLimiteDiffQuestions date limite de diffusion des questions
     */
    public void setDateLimiteDiffQuestions( java.sql.Timestamp dateLimiteDiffQuestions )
    {
        if ( dateLimiteDiffQuestions != null )
        {
            this._dateLimiteDiffQuestions = dateLimiteDiffQuestions;
        }
        else
        {
            recalculateDateLimiteDiffQuestions(  );
        }
    }

    /**
     * Retourne la date limite de diffusion des projets de délibération
     * @return dateLimiteDiffPdd date limite de diffusion des projets de délibération
     */
    public java.sql.Timestamp getDateLimiteDiffPdd(  )
    {
        return _dateLimiteDiffPdd;
    }

    /**
     * Affecte la date limite de diffusion des projets de délibération
     * @param dateLimiteDiffPdd date limite de diffusion des projets de délibération
     */
    public void setDateLimiteDiffPdd( java.sql.Timestamp dateLimiteDiffPdd )
    {
        if ( dateLimiteDiffPdd != null )
        {
            this._dateLimiteDiffPdd = dateLimiteDiffPdd;
        }
        else
        {
            recalculateDateLimiteDiffPdd(  );
        }
    }

    /**
     * Retourne la date limite de diffusion des projets de délibération DSP
     * @return dateLimiteDiffDsp date limite de diffusion des projets de délibération DSP
     */
    public java.sql.Timestamp getDateLimiteDiffDsp(  )
    {
        return _dateLimiteDiffDsp;
    }

    /**
     * Affecte la date limite de diffusion des projets de délibération DSP
     * @param dateLimiteDiffDsp date limite de diffusion des projets de délibération DSP
     */
    public void setDateLimiteDiffDsp( java.sql.Timestamp dateLimiteDiffDsp )
    {
        if ( dateLimiteDiffDsp != null )
        {
            this._dateLimiteDiffDsp = dateLimiteDiffDsp;
        }
        else
        {
            recalculateDateLimiteDiffDsp(  );
        }
    }

    /**
     * Retourne la date de remise du compte rendu sommaire
     * @return dateRemiseCr date de remise du compte rendu sommaire
     */
    public java.sql.Timestamp getDateRemiseCr(  )
    {
        return _dateRemiseCr;
    }

    /**
     * Affecte la date de remise du compte rendu sommaire
     * @param dateRemiseCr date de remise du compte rendu sommaire
     */
    public void setDateRemiseCr( java.sql.Timestamp dateRemiseCr )
    {
        if ( dateRemiseCr != null )
        {
            this._dateRemiseCr = dateRemiseCr;
        }
        else
        {
            recalculateDateRemiseCrSommaire(  );
        }
    }

    /**
     * Retourne la date de la réunion post-commission
     * @return dateReuPostComm date de la réunion post-commission
     */
    public java.sql.Timestamp getDateReuPostComm(  )
    {
        return _dateReuPostComm;
    }

    /**
     * Affecte la date de la réunion post-commission
     * @param dateReuPostComm date de la réunion post-commission
     */
    public void setDateReuPostComm( java.sql.Timestamp dateReuPostComm )
    {
        if ( dateReuPostComm != null )
        {
            this._dateReuPostComm = dateReuPostComm;
        }
        else
        {
            recalculateDateReuPostComm(  );
        }
    }

    /**
     * Retourne la date de la première réunion des commissions
     * @return _dateReu1 date de la première réunion des commissions
     */
    public java.sql.Timestamp getDateReu1(  )
    {
        return _dateReu1;
    }

    /**
     * Affecte la date de la première réunion des commissions
     * @param dateReu1 date de la première réunion des commissions
     */
    public void setDateReu1( java.sql.Timestamp dateReu1 )
    {
        if ( dateReu1 != null )
        {
            this._dateReu1 = dateReu1;
        }
        else
        {
            recalculateDateReu1(  );
        }
    }

    /**
     * Retourne la date de la deuxième réunion des commissions
     * @return _dateReu2 date de la deuxième réunion des commissions
     */
    public java.sql.Timestamp getDateReu2(  )
    {
        return _dateReu2;
    }

    /**
     * Affecte la date de la deuxième réunion des commissions
     * @param dateReu2 date de la deuxième réunion des commissions
     */
    public void setDateReu2( java.sql.Timestamp dateReu2 )
    {
        if ( dateReu2 != null )
        {
            this._dateReu2 = dateReu2;
        }
        else
        {
            recalculateDateReu2(  );
        }
    }

    /**
     * Retourne la date de la troisième réunion des commissions
     * @return _dateReu3 date de la troisième réunion des commissions
     */
    public java.sql.Timestamp getDateReu3(  )
    {
        return _dateReu3;
    }

    /**
     * Affecte la date de la troisième réunion des commissions
     * @param dateReu3 date de la troisième réunion des commissions
     */
    public void setDateReu3( java.sql.Timestamp dateReu3 )
    {
        if ( dateReu3 != null )
        {
            this._dateReu3 = dateReu3;
        }
        else
        {
            recalculateDateReu3(  );
        }
    }

    /**
     * Retourne la date de début de la séance
     * @return dateSeance date de début de la séance
     */
    public java.sql.Timestamp getDateSeance(  )
    {
        return _dateSeance;
    }

    /**
     * Affecte la date de début de séance
     * @param dateSeance date de début de séance
     */
    public void setDateSeance( java.sql.Timestamp dateSeance )
    {
        this._dateSeance = dateSeance;
    }

    /**
     * Retourne l'identifiant de la séance
     * @return _nIdSeance identifiant de la séance
     */
    public int getIdSeance(  )
    {
        return _nIdSeance;
    }

    /**
     * Affecte l'identifiant de la séance
     * @param nIdSeance int
     */
    public void setIdSeance( int nIdSeance )
    {
        this._nIdSeance = nIdSeance;
    }

    /**
     *
     * Calcul la date de remise du compte rendu sommaire = date de fin + 8 jours calendaires
     */
    public void recalculateDateRemiseCrSommaire(  )
    {
        int delay = AppPropertiesService.getPropertyInt( PROPERTY_SEANCE_DATE_REMISE_CR_SOMMAIRE,
                DEFAULT_SEANCE_DATE_REMISE_CR_SOMMAIRE );
        GregorianCalendar gc = new GregorianCalendar(  );
        gc.setTime( new Date( _dateCloture.getTime(  ) ) );
        gc.add( GregorianCalendar.DATE, delay );
        this._dateRemiseCr = new Timestamp( gc.getTimeInMillis(  ) );
    }

    /**
     *
     * Calcul la date de dépôt des questions orales = date de la séance – 2 jours calendaires
     */
    public void recalculateDateDepotQuestion(  )
    {
        int delay = AppPropertiesService.getPropertyInt( PROPERTY_SEANCE_DATE_DEPOT_QUESTION,
                DEFAULT_SEANCE_DATE_DEPOT_QUESTION );

        GregorianCalendar gc = new GregorianCalendar(  );
        gc.setTime( new Date( _dateSeance.getTime(  ) ) );
        gc.add( GregorianCalendar.DATE, delay );
        this._dateDepotQuestionsOrales = new Timestamp( gc.getTimeInMillis(  ) );
    }

    /**
     * Calcul la date limite de diffusion des questions = date de la séance – 2 jours calendaires
     *
     */
    public void recalculateDateLimiteDiffQuestions(  )
    {
        int delay = AppPropertiesService.getPropertyInt( PROPERTY_SEANCE_DATE_LIMITE_DIFF_QUESTIONS,
                DEFAULT_SEANCE_DATE_LIMITE_DIFF_QUESTIONS );

        GregorianCalendar gc = new GregorianCalendar(  );
        gc.setTime( new Date( _dateSeance.getTime(  ) ) );
        gc.add( GregorianCalendar.DATE, delay );
        this._dateLimiteDiffQuestions = new Timestamp( gc.getTimeInMillis(  ) );
    }

    /**
     * Calcul la date limite de diffusion des projets de délibération = date de la séance – 12 jours calendaires
     *
     */
    public void recalculateDateLimiteDiffPdd(  )
    {
        int delay = AppPropertiesService.getPropertyInt( PROPERTY_SEANCE_DATE_LIMITE_DIFF_PDD,
                DEFAULT_SEANCE_DATE_LIMITE_DIFF_PDD );

        GregorianCalendar gc = new GregorianCalendar(  );
        gc.setTime( new Date( _dateSeance.getTime(  ) ) );
        gc.add( GregorianCalendar.DATE, delay );
        this._dateLimiteDiffPdd = new Timestamp( gc.getTimeInMillis(  ) );
    }

    /**
     * Calcul la date limite de diffusion des projets de délibération DSP = date de la séance – 15 jours calendaires
     *
     */
    public void recalculateDateLimiteDiffDsp(  )
    {
        int delay = AppPropertiesService.getPropertyInt( PROPERTY_SEANCE_DATE_LIMITE_DIFF_DSP,
                DEFAULT_SEANCE_DATE_LIMITE_DIFF_DSP );

        GregorianCalendar gc = new GregorianCalendar(  );
        gc.setTime( new Date( _dateSeance.getTime(  ) ) );
        gc.add( GregorianCalendar.DATE, delay );
        this._dateLimiteDiffDsp = new Timestamp( gc.getTimeInMillis(  ) );
    }

    /**
     * Calcul la date de la conférence d'organisation = date de la séance – 8 jours calendaires
     *
     */
    public void recalculateDateConfOrg(  )
    {
        int delay = AppPropertiesService.getPropertyInt( PROPERTY_SEANCE_DATE_CONF_ORG, DEFAULT_SEANCE_DATE_CONF_ORG );

        GregorianCalendar gc = new GregorianCalendar(  );
        gc.setTime( new Date( _dateSeance.getTime(  ) ) );
        gc.add( GregorianCalendar.DATE, delay );
        this._dateConfOrg = new Timestamp( gc.getTimeInMillis(  ) );
    }

    /**
     * Calcul la date de la réunion post-commission = date de la séance – 3 jours calendaires
     */
    public void recalculateDateReuPostComm(  )
    {
        int delay = AppPropertiesService.getPropertyInt( PROPERTY_SEANCE_DATE_REU_POST_COMM,
                DEFAULT_SEANCE_DATE_REU_POST_COMM );

        GregorianCalendar gc = new GregorianCalendar(  );
        gc.setTime( new Date( _dateSeance.getTime(  ) ) );
        gc.add( GregorianCalendar.DATE, delay );
        this._dateReuPostComm = new Timestamp( gc.getTimeInMillis(  ) );
    }

    /**
     * Calcul la date de la première réunion des commissions = date de la séance – 5 jours calendaires
     */
    public void recalculateDateReu1(  )
    {
        int delay = AppPropertiesService.getPropertyInt( PROPERTY_SEANCE_DATE_REU_1, DEFAULT_SEANCE_DATE_REU_1 );

        GregorianCalendar gc = new GregorianCalendar(  );
        gc.setTime( new Date( _dateSeance.getTime(  ) ) );
        gc.add( GregorianCalendar.DATE, delay );
        this._dateReu1 = new Timestamp( gc.getTimeInMillis(  ) );
    }

    /**
     * Calcul la date de la deuxième réunion des commissions = date de la séance – 4 jours calendaires
     */
    public void recalculateDateReu2(  )
    {
        int delay = AppPropertiesService.getPropertyInt( PROPERTY_SEANCE_DATE_REU_2, DEFAULT_SEANCE_DATE_REU_2 );

        GregorianCalendar gc = new GregorianCalendar(  );
        gc.setTime( new Date( _dateSeance.getTime(  ) ) );
        gc.add( GregorianCalendar.DATE, delay );
        this._dateReu2 = new Timestamp( gc.getTimeInMillis(  ) );
    }

    /**
     * Calcul la date de la troisième réunion des commissions = date de la séance – 3 jours calendaires
     */
    public void recalculateDateReu3(  )
    {
        int delay = AppPropertiesService.getPropertyInt( PROPERTY_SEANCE_DATE_REU_3, DEFAULT_SEANCE_DATE_REU_3 );

        GregorianCalendar gc = new GregorianCalendar(  );
        gc.setTime( new Date( _dateSeance.getTime(  ) ) );
        gc.add( GregorianCalendar.DATE, delay );
        this._dateReu3 = new Timestamp( gc.getTimeInMillis(  ) );
    }

    /**
     * Génèration du XML d'un objet
     * @param request la requête
     * @return Le code XML représentant un ordre du jour
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );

        attributes.put( TAG_ID, _nIdSeance );
        XmlUtil.beginElement( buffer, Seance.MARK_SEANCE, attributes );

        if ( _dateSeance != null )
        {
            OdsUtils.addElement( buffer, TAG_DATE_SEANCE, OdsConstants.CONSTANTE_CHAINE_VIDE + _dateSeance );
        }

        if ( _dateCloture != null )
        {
            OdsUtils.addElement( buffer, TAG_DATE_CLOTURE, OdsConstants.CONSTANTE_CHAINE_VIDE + _dateCloture );
        }

        if ( _dateRemiseCr != null )
        {
            OdsUtils.addElement( buffer, TAG_DATE_REMISE_COMPTE_RENDU,
                OdsConstants.CONSTANTE_CHAINE_VIDE + _dateRemiseCr );
        }

        if ( _dateDepotQuestionsOrales != null )
        {
            OdsUtils.addElement( buffer, TAG_DATE_DEPOT_QUESTIONS_ORALES,
                OdsConstants.CONSTANTE_CHAINE_VIDE + _dateDepotQuestionsOrales );
        }

        if ( _dateLimiteDiffQuestions != null )
        {
            OdsUtils.addElement( buffer, TAG_DATE_LIMITE_DIFFUSION_QUESTIONS,
                OdsConstants.CONSTANTE_CHAINE_VIDE + _dateLimiteDiffQuestions );
        }

        if ( _dateLimiteDiffPdd != null )
        {
            OdsUtils.addElement( buffer, TAG_DATE_LIMITE_DIFFUSION_PDD,
                OdsConstants.CONSTANTE_CHAINE_VIDE + _dateLimiteDiffPdd );
        }

        if ( _dateLimiteDiffDsp != null )
        {
            OdsUtils.addElement( buffer, TAG_DATE_LIMITE_DIFFUSION_DSP,
                OdsConstants.CONSTANTE_CHAINE_VIDE + _dateLimiteDiffDsp );
        }

        if ( _dateConfOrg != null )
        {
            OdsUtils.addElement( buffer, TAG_DATE_CONFERENCE_ORGANISATION,
                OdsConstants.CONSTANTE_CHAINE_VIDE + _dateConfOrg );
        }

        if ( _dateReuPostComm != null )
        {
            OdsUtils.addElement( buffer, TAG_DATE_REUNION_POST_COMMISSION,
                OdsConstants.CONSTANTE_CHAINE_VIDE + _dateReuPostComm );
        }

        if ( _dateReu1 != null )
        {
            OdsUtils.addElement( buffer, TAG_DATE_REUNION1, OdsConstants.CONSTANTE_CHAINE_VIDE + _dateReu1 );
        }

        if ( _dateReu2 != null )
        {
            OdsUtils.addElement( buffer, TAG_DATE_REUNION2, OdsConstants.CONSTANTE_CHAINE_VIDE + _dateReu2 );
        }

        if ( _dateReu3 != null )
        {
            OdsUtils.addElement( buffer, TAG_DATE_REUNION3, OdsConstants.CONSTANTE_CHAINE_VIDE + _dateReu3 );
        }

        XmlUtil.endElement( buffer, Seance.MARK_SEANCE );

        return buffer.toString(  );
    }

    /**
     * Indique si la séance passée en argument est la même que la séance actuelle
     * @param seance l'objet à comparer
     * @return <b>true</b> si les séances sont égales, cad si les id des 2 séances sont égaux, <b>false</b> sinon
     */
    public boolean equals( Object seance )
    {
        if ( seance instanceof Seance )
        {
            return ( new Integer( getIdSeance(  ) ) ).equals( new Integer( ( (Seance) seance ).getIdSeance(  ) ) );
        }

        return false;
    }

    /**
     * Redéfinition de la méthode hashcode( )
     * @return l'identifiant de la seance
     */
    public int hashCode(  )
    {
        return _nIdSeance;
    }
}
