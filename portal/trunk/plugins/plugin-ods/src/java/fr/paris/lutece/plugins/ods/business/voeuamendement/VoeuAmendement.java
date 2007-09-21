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
package fr.paris.lutece.plugins.ods.business.voeuamendement;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.fascicule.Fascicule;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.historique.Historique;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.statut.Statut;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.xml.XmlUtil;

import java.sql.Timestamp;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * VoeuAmendement
 *
 */
public class VoeuAmendement
{
    public static final String MARK_VOEUAMENDEMENT = "voeuamendement";
    private static final String PROPERTY_LIMIT_SIZE_AFFICHAGE = "odsvoeuxamendement.limiteSizeAffichage";
    private static final int DEFAULT_LIMIT_SIZE_AFFICHAGE = 100;
    private static final String TAG_LISTE_ELUS = "listeElus";
    private static final String TAG_LISTE_HISTORIQUES = "listeHistoriques";
    private static final String TAG_DATE_VOTE = "dateVote";
    private static final String TAG_DEPOSE_EXECUTIF = "deposeExecutif";
    private static final String TAG_VERSION = "version";
    private static final String TAG_DATE_PUBLICATION = "datePublication";
    private static final String TAG_DATE_RETOUR_CONTROLE_LEGALITE = "dateRetourControleLegalite";
    private static final String TAG_REFERENCE_COMPLETE = "referenceComplete";
    private static final String TAG_OBJET = "objet";
    private static final String TAG_TYPE = "type";
    private static final String TAG_VOEU_AMENDEMENT = "voeuAmendement";
    private static final String TAG_ID_VOEU_AMENDEMENT = "id";
    private int _nIdVoeuAmendement = -1;
    private FormationConseil _formationConseil;
    private String _strType;
    private String _strObjet;
    private String _strReference;
    private Fascicule _fascicule;
    private Statut _statut;
    private Timestamp _timestampDateRetourControleLegalite;
    private boolean _bEnLigne;
    private Timestamp _timestampDatePublication;
    private int _nVersion;
    private boolean _bDeposeExecutif;
    private List<Historique> _historiques;
    private Fichier _fichier;
    private List<Elu> _elus;
    private List<PDD> _pdds;
    private Commission _commission;
    private Timestamp _timestampDateVote;
    private String _strNumeroDeliberation;
    private Fichier _deliberation;
    private VoeuAmendement _parent;
    private List<Elu> _groupeRapporteurs;
    private List<Commission> _groupeCommissions;

    /**
     * retourne la référence complete du va
     * @return  retourne la référence complete du va
     */
    public String getReferenceComplete(  )
    {
        StringBuffer result = new StringBuffer(  );

        if ( VoeuAmendementJspBean.CONSTANTE_TYPE_VNR.equals( _strType ) )
        {
            result.append( "V" );
        }
        else
        {
            result.append( _strType );
        }

        if ( _strReference != null )
        {
            result.append( _strReference );
        }

        if ( _fascicule != null )
        {
            result.append( " " ).append( _fascicule.getCodeFascicule(  ) );
        }

        return result.toString(  );
    }

    /**
     * retourne la commission du va
     * @return  retourne la commission du va
     */
    public Commission getCommission(  )
    {
        return _commission;
    }

    /**
     * initialise la commission du va
     * @param releveCommission la commission du va
     */
    public void setCommission( Commission releveCommission )
    {
        _commission = releveCommission;
    }

    /**
     * retourne la formation conseil du va
     * @return la formation conseil du va
     */
    public FormationConseil getFormationConseil(  )
    {
        return _formationConseil;
    }

    /**
     * initialise la formationConseil du va
     * @param formationConseil la formationConseil du va
     */
    public void setFormationConseil( FormationConseil formationConseil )
    {
        _formationConseil = formationConseil;
    }

    /**
     * renvoie la liste des élus du va
     * @return la liste des élus du va
     */
    public List<Elu> getElus(  )
    {
        return _elus;
    }

    /**
     * initialise la liste des élus ayant déposé le va
     * @param elus la liste des élus ayant déposé le va
     */
    public void setElus( List<Elu> elus )
    {
        _elus = elus;
    }

    /**
     * renvoie la liste des  rapporteurs de l'entrée d'ordre du jour rattachée au va
     * @return la liste des rapporteurs du va
     */
    public List<Elu> getGroupeRapporteurs(  )
    {
        return _groupeRapporteurs;
    }

    /**
     * initialise la liste des  rapporteurs de l'entrée d'ordre du jour rattachée au va
     * @param elus la liste des rapporteurs du va
     */
    public void setGroupeRapporteurs( List<Elu> elus )
    {
        _groupeRapporteurs = elus;
    }

    /**
     * renvoie la liste des  commissions de l'entrée d'ordre du jour rattachée au va
     * @return la liste des  commissions de l'entrée d'ordre du jour rattachée au va
     */
    public List<Commission> getGroupeCommissions(  )
    {
        return _groupeCommissions;
    }

    /**
     * initialise la liste des  commissions  de l'entrée d'ordre du jour rattachée au va
     * @param commissions la liste des  commissions  de l'entrée d'ordre du jour rattachée au va
     */
    public void setGroupeCommissions( List<Commission> commissions )
    {
        _groupeCommissions = commissions;
    }

    /**
     * retourne true si le va est en ligne false sinon
     * @return  true si le va est en ligne false sinon
     */
    public Boolean getEnLigne(  )
    {
        return _bEnLigne;
    }

    /**
     * initialise le statut en ligne a true ou false
     * @param enLigne le statut du va
     */
    public void setEnLigne( Boolean enLigne )
    {
        _bEnLigne = enLigne;
    }

    /**
     * retourne la date de publication du va
     * @return date de publication du va
     */
    public Timestamp getDatePublication(  )
    {
        return _timestampDatePublication;
    }

    /**
     * initialise la date de publication du va
     * @param datePublication date de publication du va
     */
    public void setDatePublication( Timestamp datePublication )
    {
        _timestampDatePublication = datePublication;
    }

    /**
     * retourne la date de retour de controle de légalité du va
     * @return  date de retour de controle de légalité du va
     */
    public Timestamp getDateRetourControleLegalite(  )
    {
        return _timestampDateRetourControleLegalite;
    }

    /**
     * initialise la date de retour de controle de légalité du va
     * @param dateRetourControleLegalite date de retour de controle de légalité
     */
    public void setDateRetourControleLegalite( Timestamp dateRetourControleLegalite )
    {
        _timestampDateRetourControleLegalite = dateRetourControleLegalite;
    }

    /**
     * retourne le fascicule dans lequel le va a été déposé
     * @return  le fascicule dans lequel le va a été déposé
     */
    public Fascicule getFascicule(  )
    {
        return _fascicule;
    }

    /**
     * initialise le fascicule dans lequel le va a été déposé
     * @param fascicule le fascicule dans lequel le va a été déposé
     */
    public void setFascicule( Fascicule fascicule )
    {
        _fascicule = fascicule;
    }

    /**
     * retourne la liste d'historiques du va
     * @return liste d'historiques du va
     */
    public List<Historique> getHistoriques(  )
    {
        return _historiques;
    }

    /**
     * initialise la liste d'historiques du va
     * @param historiques la liste d'historiques du va
     */
    public void setHistoriques( List<Historique> historiques )
    {
        _historiques = historiques;
    }

    /**
     * retourne true si le va a été déposé par l'exécutif,false sinon
     * @return  true si le va a été déposé par l'exécutif,false sinon
     */
    public boolean getDeposeExecutif(  )
    {
        return _bDeposeExecutif;
    }

    /**
     *
     * initialise le statut deposeExecutif du va à true si le va est déposé par l'exécutif, false sinon
     * @param deposeExecutif true si le va est déposé par l'exécutif, false sinon
     */
    public void setDeposeExecutif( boolean deposeExecutif )
    {
        _bDeposeExecutif = deposeExecutif;
    }

    /**
     * retourne l'id du va
     * @return  l'id du va
     */
    public int getIdVoeuAmendement(  )
    {
        return _nIdVoeuAmendement;
    }

    /**
     * initialise l'id  du va
     * @param idVoeuAmendement id  du va
     */
    public void setIdVoeuAmendement( int idVoeuAmendement )
    {
        _nIdVoeuAmendement = idVoeuAmendement;
    }

    /**
     * retourne la version du va
     * @return la version du va
     */
    public int getVersion(  )
    {
        return _nVersion;
    }

    /**
     * initialise la version du va
     * @param version la version du va
     */
    public void setVersion( int version )
    {
        _nVersion = version;
    }

    /**
     *
     * @return getObjetLimit
     */
    public String getObjetLimit(  )
    {
        int nLimitSize = AppPropertiesService.getPropertyInt( PROPERTY_LIMIT_SIZE_AFFICHAGE,
                DEFAULT_LIMIT_SIZE_AFFICHAGE );

        // objet du vœu ou de l’amendement tronqué au dernier espace avant le n ième caractère 
        if ( ( _strObjet != null ) && ( _strObjet.length(  ) > ( nLimitSize - 1 ) ) )
        {
            StringBuffer strObjet = new StringBuffer(  );
            int nLastEspace = _strObjet.lastIndexOf( " ", ( nLimitSize - 1 ) ) + 1;
            strObjet.append( _strObjet.substring( 0, nLastEspace ) ).append( "..." );

            return strObjet.toString(  );
        }

        return _strObjet;
    }

    /**
     *retourne l'objet du va
     * @return  l'objet du va
     */
    public String getObjet(  )
    {
        return _strObjet;
    }

    /**
     * initialise l'objet du va
     * @param objet l'objet du va
     */
    public void setObjet( String objet )
    {
        _strObjet = objet;
    }

    /**
     * retourne la reference du va
     * @return  la reference du va
     */
    public String getReference(  )
    {
        return _strReference;
    }

    /**
     * initialise la reference du va
     * @param reference la reference du va
     */
    public void setReference( String reference )
    {
        _strReference = reference;
    }

    /**
     * retourne le statut du va
     * @return  le statut du va
     */
    public Statut getStatut(  )
    {
        return _statut;
    }

    /**
     * initialise le statut du va
     * @param statut le statut du va
     */
    public void setStatut( Statut statut )
    {
        _statut = statut;
    }

    /**
     * retourne le type du va(Amendement=A,Lettre rectificative=LR,Voeu rattache=V,Voeu non rattache=VNR)
     * @return  le type du va(Amendement=A,Lettre rectificative=LR,Voeu rattache=V,Voeu non rattache=VNR)
     */
    public String getType(  )
    {
        return _strType;
    }

    /**
     * initialise le type du va (Amendement=A,Lettre rectificative=LR,Voeu rattache=V,Voeu non rattache=VNR)
     * @param type le type du va(Amendement=A,Lettre rectificative=LR,Voeu rattache=V,Voeu non rattache=VNR)
     */
    public void setType( String type )
    {
        _strType = type;
    }

    /**
     * le fichier associé au va
     * @return le fichier associé au va
     */
    public Fichier getFichier(  )
    {
        return _fichier;
    }

    /**
     * initialise le fichier du va
     * @param fichier le fichier du va
     */
    public void setFichier( Fichier fichier )
    {
        _fichier = fichier;
    }

    /**
     * retourne  la liste des pdds du va
     * @return  la liste des pdds du va
     */
    public List<PDD> getPdds(  )
    {
        return _pdds;
    }

    /**
     * initialise la liste des pdds du va
     * @param ds la liste des pdds du va
     */
    public void setPdds( List<PDD> ds )
    {
        _pdds = ds;
    }

    /**
     * retourne la délibération du VA.
     * @return la délibération
     */
    public Fichier getDeliberation(  )
    {
        return _deliberation;
    }

    /**
     * initialise la délibération du VA.
     * @param deliberation la délibératiion
     */
    public void setDeliberation( Fichier deliberation )
    {
        this._deliberation = deliberation;
    }

    /**
     * retourne le numéro de la délibération.
     * @return le numéro de la délibération
     */
    public String getNumeroDeliberation(  )
    {
        return _strNumeroDeliberation;
    }

    /**
     * initialise le numéro de la délibération.
     * @param numeroDeliberation le numéro de la délibération
     */
    public void setNumeroDeliberation( String numeroDeliberation )
    {
        _strNumeroDeliberation = numeroDeliberation;
    }

    /**
     * retourne la date du vote.
     * @return la date du vote
     */
    public Timestamp getDateVote(  )
    {
        return _timestampDateVote;
    }

    /**
     * initialise la date du vote.
     * @param dateVote la date du vote
     */
    public void setDateVote( Timestamp dateVote )
    {
        _timestampDateVote = dateVote;
    }

    /**
     * Indique si le voeu ou amendement passé en argument est le même
     * @param va le voeu/amendement à comparer
     * @return <b>true</b> si les 2 VA sont égaux, cad qu'ils ont le même id, <b>false</b> sinon
     */
    public boolean equals( Object va )
    {
        if ( va instanceof VoeuAmendement )
        {
            return new Integer( getIdVoeuAmendement(  ) ).equals( new Integer( 
                    ( (VoeuAmendement) va ).getIdVoeuAmendement(  ) ) );
        }

        return false;
    }

    /**
     * calcule le hashcode de l'objet
     * @return le hashcode
     */
    public int hashCode(  )
    {
        return getIdVoeuAmendement(  );
    }

    /**
     * Retourne le parent du voeu/amendement
     * @return le parent
     */
    public VoeuAmendement getParent(  )
    {
        return _parent;
    }

    /**
     * intialise le parent du voeu/amendement
     * @param parent le parent
     */
    public void setParent( VoeuAmendement parent )
    {
        _parent = parent;
    }

    /**
     * Test si la liste des commissions passée en parametre est egale a la liste
     * des commissions de l'entrée
     *
     * @param listCommissions
     *            liste des commissions
     * @return true si la liste des commissions passée en parametre est egale a
     *         la liste des commissions de l'entrée,false sinon
     */
    public boolean getEqualListCommissions( List<Commission> listCommissions )
    {
        if ( _groupeCommissions.size(  ) != listCommissions.size(  ) )
        {
            return false;
        }

        for ( int cpt = 0; cpt < _groupeCommissions.size(  ); cpt++ )
        {
            if ( _groupeCommissions.get( cpt ).getIdCommission(  ) != listCommissions.get( cpt ).getIdCommission(  ) )
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Test si la liste des élus passée en parametre est egale au groupe de rapporteurs du va
     *
     *@param listElus
     *            liste de rapporteurs
     * @return true Test si la liste des élus passée en parametre est egale
     * au groupe de rapporteurs du va
     */
    public boolean getEqualListRapporteurs( List<Elu> listElus )
    {
        if ( _groupeRapporteurs.size(  ) != listElus.size(  ) )
        {
            return false;
        }

        for ( int cpt = 0; cpt < _groupeRapporteurs.size(  ); cpt++ )
        {
            if ( _groupeRapporteurs.get( cpt ).getIdElu(  ) != listElus.get( cpt ).getIdElu(  ) )
            {
                return false;
            }
        }

        return true;
    }

    /**
     * renvoie le code XML correspondant à l'objet
     * @param request la requete Http
     * @return le code XMl
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );
        attributes.put( TAG_ID_VOEU_AMENDEMENT, OdsConstants.CONSTANTE_CHAINE_VIDE + _nIdVoeuAmendement );
        XmlUtil.beginElement( buffer, TAG_VOEU_AMENDEMENT, attributes );

        OdsUtils.addElement( buffer, TAG_TYPE, _strType );
        OdsUtils.addElement( buffer, TAG_OBJET, _strObjet );
        OdsUtils.addElement( buffer, OdsParameters.REFERENCE, _strReference );
        OdsUtils.addElement( buffer, TAG_REFERENCE_COMPLETE, getReferenceComplete(  ) );
        OdsUtils.addElement( buffer, TAG_DATE_RETOUR_CONTROLE_LEGALITE,
            OdsConstants.CONSTANTE_CHAINE_VIDE + _timestampDateRetourControleLegalite );
        OdsUtils.addElement( buffer, TAG_DATE_PUBLICATION,
            OdsConstants.CONSTANTE_CHAINE_VIDE + _timestampDatePublication );
        OdsUtils.addElement( buffer, TAG_VERSION, _nVersion );
        OdsUtils.addElement( buffer, TAG_DEPOSE_EXECUTIF, OdsConstants.CONSTANTE_CHAINE_VIDE + _bDeposeExecutif );

        OdsUtils.addElement( buffer, TAG_DATE_VOTE, OdsConstants.CONSTANTE_CHAINE_VIDE + _timestampDateVote );

        if ( _formationConseil != null )
        {
            buffer.append( _formationConseil.getXml( request ) );
        }

        if ( _fascicule != null )
        {
            buffer.append( _fascicule.getXml( request ) );
        }

        XmlUtil.beginElement( buffer, TAG_LISTE_HISTORIQUES );

        if ( _historiques != null )
        {
            for ( Historique historique : _historiques )
            {
                buffer.append( historique.getXml( request ) );
            }
        }

        XmlUtil.endElement( buffer, TAG_LISTE_HISTORIQUES );

        if ( _fichier != null )
        {
            buffer.append( _fichier.getXml( request ) );
        }

        XmlUtil.beginElement( buffer, TAG_LISTE_ELUS );

        if ( _elus != null )
        {
            for ( Elu elu : _elus )
            {
                buffer.append( elu.getXml( request ) );
            }
        }

        XmlUtil.endElement( buffer, TAG_LISTE_ELUS );

        if ( _commission != null )
        {
            buffer.append( _commission.getXml( request ) );
        }

        if ( _deliberation != null )
        {
            buffer.append( _deliberation.getXml( request ) );
        }

        XmlUtil.endElement( buffer, TAG_VOEU_AMENDEMENT );

        return buffer.toString(  );
    }
}
