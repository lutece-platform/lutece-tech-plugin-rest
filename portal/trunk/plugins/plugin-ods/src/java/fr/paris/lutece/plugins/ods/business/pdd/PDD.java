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
package fr.paris.lutece.plugins.ods.business.pdd;

import fr.paris.lutece.plugins.ods.business.categoriedeliberation.CategorieDeliberation;
import fr.paris.lutece.plugins.ods.business.direction.Direction;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitique;
import fr.paris.lutece.plugins.ods.business.statut.Statut;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.sql.Timestamp;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * Représente un projet ou une proposition de délibération
 *
 */
public class PDD
{
    public static final String MARK_PDD = "pdd";
    public static final String CONSTANTE_TYPE_PROJET = "PJ";
    public static final String CONSTANTE_TYPE_PROP = "PP";
    public static final String CONSTANTE_TYPE_DSP = "DSP";
    private static final String TAG_LISTE_FICHIERS = "listeFichiers";
    private static final String TAG_LISTE_VOEUX_AMENDEMENTS = "listeVoeuxAmendements";
    private static final String TAG_LISTE_VOEUX_NON_RATTACHES = "listeVoeuxNonRattaches";
    private static final String TAG_LISTE_VOEUX_RATTACHES = "listeVoeuxRattaches";
    private static final String TAG_LISTE_AMENDEMENTS = "listeAmendements";
    private static final String TAG_LISTE_DIRECTIONS_CO_EMETRICES = "listeDirectionsCoEmetrices";
    private static final String TAG_VERSION = "version";
    private static final String TAG_DATE_PUBLICATION = "datePublication";
    private static final String TAG_EN_LIGNE = "enLigne";
    private static final String TAG_DATE_RETOUR_CTRL_LEGALITE = "dateRetourCtrlLegalite";
    private static final String TAG_DATE_VOTE = "dateVote";
    private static final String TAG_PIECES_MANUELLES = "piecesManuelles";
    private static final String TAG_OBJET = "objet";
    private static final String TAG_DELEGATIONS_SERVICES = "delegationsServices";
    private static final String TAG_MODE_INTRODUCTION = "modeIntroduction";
    private static final String TAG_TYPE_PDD = "typePdd";
    private static final String TAG_ID_PDD = "id";
    private int _nIdPdd;
    private CategorieDeliberation _categorieDeliberation;
    private GroupePolitique _groupePolitique;
    private Direction _direction;
    private FormationConseil _formationConseil;
    private Statut _statut;
    private String _strReference;
    private String _strTypePdd;
    private boolean _bDelegationsServices;
    private String _strModeIntroduction;
    private String _strObjet;
    private boolean _bPiecesManuelles;
    private Timestamp _dDateVote;
    private Timestamp _dDateRetourCtrlLegalite;
    private boolean _bEnLigne;
    private Timestamp _dDatePublication;
    private int _nVersion;
    private List<DirectionCoEmetrice> _directionsCoEmetrices;
    private List<VoeuAmendement> _amendements;
    private List<VoeuAmendement> _voeuxRattaches;
    private List<VoeuAmendement> _voeuxNonRattaches;
    private List<VoeuAmendement> _voeuxAmendements;
    private List<Fichier> _fichiers;

    /**
     * @return the categorieDeliberation
     */
    public CategorieDeliberation getCategorieDeliberation(  )
    {
        return _categorieDeliberation;
    }

    /**
     * @param categorieDeliberation the categorieDeliberation to set
     */
    public void setCategorieDeliberation( CategorieDeliberation categorieDeliberation )
    {
        this._categorieDeliberation = categorieDeliberation;
    }

    /**
     * @return the datePublication
     */
    public Timestamp getDatePublication(  )
    {
        return _dDatePublication;
    }

    /**
     * @param datePublication the datePublication to set
     */
    public void setDatePublication( Timestamp datePublication )
    {
        this._dDatePublication = datePublication;
    }

    /**
     * @return the dateRetourCtrlLegalite
     */
    public Timestamp getDateRetourCtrlLegalite(  )
    {
        return _dDateRetourCtrlLegalite;
    }

    /**
     * @param dateRetourCtrlLegalite the dateRetourCtrlLegalite to set
     */
    public void setDateRetourCtrlLegalite( Timestamp dateRetourCtrlLegalite )
    {
        this._dDateRetourCtrlLegalite = dateRetourCtrlLegalite;
    }

    /**
     * @return the dateVote
     */
    public Timestamp getDateVote(  )
    {
        return _dDateVote;
    }

    /**
     * @param dateVote the dateVote to set
     */
    public void setDateVote( Timestamp dateVote )
    {
        this._dDateVote = dateVote;
    }

    /**
     * @return the delegationsServices
     */
    public boolean isDelegationsServices(  )
    {
        return _bDelegationsServices;
    }

    /**
     * @param delegationsServices the delegationsServices to set
     */
    public void setDelegationsServices( boolean delegationsServices )
    {
        this._bDelegationsServices = delegationsServices;
    }

    /**
     * @return the direction
     */
    public Direction getDirection(  )
    {
        return _direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection( Direction direction )
    {
        this._direction = direction;
    }

    /**
     * @return the enLigne
     */
    public boolean isEnLigne(  )
    {
        return _bEnLigne;
    }

    /**
     * @param enLigne the enLigne to set
     */
    public void setEnLigne( boolean enLigne )
    {
        this._bEnLigne = enLigne;
    }

    /**
     * @return the formationConseil
     */
    public FormationConseil getFormationConseil(  )
    {
        return _formationConseil;
    }

    /**
     * @param formationConseil the formationConseil to set
     */
    public void setFormationConseil( FormationConseil formationConseil )
    {
        this._formationConseil = formationConseil;
    }

    /**
     * @return the groupePolitique
     */
    public GroupePolitique getGroupePolitique(  )
    {
        return _groupePolitique;
    }

    /**
     * @param groupePolitique the groupePolitique to set
     */
    public void setGroupePolitique( GroupePolitique groupePolitique )
    {
        this._groupePolitique = groupePolitique;
    }

    /**
     * @return the idPdd
     */
    public int getIdPdd(  )
    {
        return _nIdPdd;
    }

    /**
     * @param idPdd the idPdd to set
     */
    public void setIdPdd( int idPdd )
    {
        this._nIdPdd = idPdd;
    }

    /**
     * @return the mode_introduction
     */
    public String getModeIntroduction(  )
    {
        return _strModeIntroduction;
    }

    /**
     * @param modeIntroduction the modeIntroduction to set
     */
    public void setModeIntroduction( String modeIntroduction )
    {
        this._strModeIntroduction = modeIntroduction;
    }

    /**
     * @return the objet
     */
    public String getObjet(  )
    {
        return _strObjet;
    }

    /**
     * @param objet the objet to set
     */
    public void setObjet( String objet )
    {
        this._strObjet = objet;
    }

    /**
     * @return the piecesManuelles
     */
    public boolean isPiecesManuelles(  )
    {
        return _bPiecesManuelles;
    }

    /**
     * @param piecesManuelles the piecesManuelles to set
     */
    public void setPiecesManuelles( boolean piecesManuelles )
    {
        this._bPiecesManuelles = piecesManuelles;
    }

    /**
     * @return the reference
     */
    public String getReference(  )
    {
        return _strReference;
    }

    /**
     * @param reference the reference to set
     */
    public void setReference( String reference )
    {
        this._strReference = reference;
    }

    /**
     * @return the statut
     */
    public Statut getStatut(  )
    {
        return _statut;
    }

    /**
     * @param statut the statut to set
     */
    public void setStatut( Statut statut )
    {
        this._statut = statut;
    }

    /**
     * @return the typePdd
     */
    public String getTypePdd(  )
    {
        return _strTypePdd;
    }

    /**
     * @param typePdd the typePdd to set
     */
    public void setTypePdd( String typePdd )
    {
        this._strTypePdd = typePdd;
    }

    /**
     * @return the version
     */
    public int getVersion(  )
    {
        return _nVersion;
    }

    /**
     * @param version the version to set
     */
    public void setVersion( int version )
    {
        this._nVersion = version;
    }

    /**
     * Retourne l'annee qui est stocké dans la reference
     *  (reference = ANNEE +' '+ CODE_DIRECTION  +' '+ NUMERO )
     * @return String Annee
     */
    public String getAnnee(  )
    {
        if ( _strReference != null )
        {
            return _strReference.substring( 0, 4 );
        }

        return OdsConstants.CONSTANTE_CHAINE_VIDE;
    }

    /**
     * Retourne le numero qui est stocké dans la reference
     * (reference = ANNEE +' '+ CODE_DIRECTION  +' '+ NUMERO )
     * @return String Numero
     */
    public String getNumero(  )
    {
        if ( _strReference != null )
        {
            String[] reference = _strReference.split( " " );

            return reference[reference.length - 1];
        }

        return OdsConstants.CONSTANTE_CHAINE_VIDE;
    }

    /**
     * @return the directionsCoEmetrices
     */
    public List<DirectionCoEmetrice> getDirectionsCoEmetrices(  )
    {
        return _directionsCoEmetrices;
    }

    /**
     * @param directionsCoEmetrices the directionsCoEmetrices to set
     */
    public void setDirectionsCoEmetrices( List<DirectionCoEmetrice> directionsCoEmetrices )
    {
        this._directionsCoEmetrices = directionsCoEmetrices;
    }

    /**
     * @return the amendements
     */
    public List<VoeuAmendement> getAmendements(  )
    {
        return _amendements;
    }

    /**
     * @param amendements the amendements to set
     */
    public void setAmendements( List<VoeuAmendement> amendements )
    {
        this._amendements = amendements;
    }

    /**
     * @return the voeuxNonRattaches
     */
    public List<VoeuAmendement> getVoeuxNonRattaches(  )
    {
        return _voeuxNonRattaches;
    }

    /**
     * @param voeuxNonRattaches the voeuxNonRattaches to set
     */
    public void setVoeuxNonRattaches( List<VoeuAmendement> voeuxNonRattaches )
    {
        this._voeuxNonRattaches = voeuxNonRattaches;
    }

    /**
     * @return the voeuxRattaches
     */
    public List<VoeuAmendement> getVoeuxRattaches(  )
    {
        return _voeuxRattaches;
    }

    /**
     * @param voeuxRattaches the voeuxRattaches to set
     */
    public void setVoeuxRattaches( List<VoeuAmendement> voeuxRattaches )
    {
        this._voeuxRattaches = voeuxRattaches;
    }

    /**
     * @return the voeuxvoeuxAmendements
     */
    public List<VoeuAmendement> getVoeuxAmendements(  )
    {
        return _voeuxAmendements;
    }

    /**
     * @param voeuxAmendements the voeuxAmendements to set
     */
    public void setVoeuxAmendements( List<VoeuAmendement> voeuxAmendements )
    {
        this._voeuxAmendements = voeuxAmendements;
    }

    /**
     *
     * @param va VoeuAmendement
     * @return boolean TRUE si le VoeuAmendement est contenu dans une des listes
     */
    public boolean containsVoeuxAmandements( VoeuAmendement va )
    {
        if ( _amendements != null )
        {
            for ( VoeuAmendement voeuAmendement : _amendements )
            {
                if ( voeuAmendement.getIdVoeuAmendement(  ) == va.getIdVoeuAmendement(  ) )
                {
                    return true;
                }
            }
        }

        if ( _voeuxRattaches != null )
        {
            for ( VoeuAmendement voeuAmendement : _voeuxRattaches )
            {
                if ( voeuAmendement.getIdVoeuAmendement(  ) == va.getIdVoeuAmendement(  ) )
                {
                    return true;
                }
            }
        }

        if ( _voeuxNonRattaches != null )
        {
            for ( VoeuAmendement voeuAmendement : _voeuxNonRattaches )
            {
                if ( voeuAmendement.getIdVoeuAmendement(  ) == va.getIdVoeuAmendement(  ) )
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Indique si les 2 PDD sont égaux, cad s'ils ont le même id
     * @param pdd le PDD à comparer
     * @return <b>true</b> si les 2 PDD sont égaux, <b>false</b> sinon
     */
    public boolean equals( Object pdd )
    {
        if ( pdd instanceof PDD )
        {
            return ( new Integer( getIdPdd(  ) ) ).equals( new Integer( ( (PDD) pdd ).getIdPdd(  ) ) );
        }

        return false;
    }

    /**
     * Retourne la liste des fichiers reliés au pdd
     * @return la liste des fichiers du pdd
     */
    public List<Fichier> getFichiers(  )
    {
        return _fichiers;
    }

    /**
     * Fixe la liste des fichiers du pdd
     * @param fichiers la liste des fichiers du pdd
     */
    public void setFichiers( List<Fichier> fichiers )
    {
        _fichiers = fichiers;
    }

    /**
     * @return le hashcode de l'objet
     */
    public int hashCode(  )
    {
        return getIdPdd(  );
    }

    /**
     *
     * @param request la requête Http
     * @return le code XML correspondant à l'objet
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );
        attributes.put( TAG_ID_PDD, OdsConstants.CONSTANTE_CHAINE_VIDE + _nIdPdd );
        XmlUtil.beginElement( buffer, PDD.MARK_PDD, attributes );
        OdsUtils.addElement( buffer, OdsParameters.REFERENCE, _strReference );
        OdsUtils.addElement( buffer, TAG_TYPE_PDD, _strTypePdd );
        OdsUtils.addElement( buffer, TAG_MODE_INTRODUCTION, _strModeIntroduction );
        OdsUtils.addElement( buffer, TAG_DELEGATIONS_SERVICES,
            OdsConstants.CONSTANTE_CHAINE_VIDE + _bDelegationsServices );
        OdsUtils.addElement( buffer, TAG_OBJET, _strObjet );
        OdsUtils.addElement( buffer, TAG_PIECES_MANUELLES, OdsConstants.CONSTANTE_CHAINE_VIDE + _bPiecesManuelles );

        OdsUtils.addElement( buffer, TAG_DATE_VOTE, OdsConstants.CONSTANTE_CHAINE_VIDE + _dDateVote );
        OdsUtils.addElement( buffer, TAG_DATE_RETOUR_CTRL_LEGALITE,
            OdsConstants.CONSTANTE_CHAINE_VIDE + _dDateRetourCtrlLegalite );
        OdsUtils.addElement( buffer, TAG_EN_LIGNE, OdsConstants.CONSTANTE_CHAINE_VIDE + _bEnLigne );

        OdsUtils.addElement( buffer, TAG_DATE_PUBLICATION, OdsConstants.CONSTANTE_CHAINE_VIDE + _dDatePublication );
        OdsUtils.addElement( buffer, TAG_VERSION, _nVersion );

        if ( _categorieDeliberation != null )
        {
            buffer.append( _categorieDeliberation.getXml( request ) );
        }

        if ( _groupePolitique != null )
        {
            buffer.append( _groupePolitique.getXml( request ) );
        }

        if ( _direction != null )
        {
            buffer.append( _direction.getXml( request ) );
        }

        if ( _formationConseil != null )
        {
            buffer.append( _formationConseil.getXml( request ) );
        }

        if ( _statut != null )
        {
            buffer.append( _statut.getXml( request ) );
        }

        if ( _directionsCoEmetrices != null )
        {
            XmlUtil.beginElement( buffer, TAG_LISTE_DIRECTIONS_CO_EMETRICES );

            for ( DirectionCoEmetrice direction : _directionsCoEmetrices )
            {
                buffer.append( direction.getXml( request ) );
            }

            XmlUtil.endElement( buffer, TAG_LISTE_DIRECTIONS_CO_EMETRICES );
        }

        if ( _amendements != null )
        {
            XmlUtil.beginElement( buffer, TAG_LISTE_AMENDEMENTS );

            for ( VoeuAmendement va : _amendements )
            {
                buffer.append( va.getXml( request ) );
            }

            XmlUtil.endElement( buffer, TAG_LISTE_AMENDEMENTS );
        }

        if ( _voeuxRattaches != null )
        {
            XmlUtil.beginElement( buffer, TAG_LISTE_VOEUX_RATTACHES );

            for ( VoeuAmendement va : _voeuxRattaches )
            {
                buffer.append( va.getXml( request ) );
            }

            XmlUtil.endElement( buffer, TAG_LISTE_VOEUX_RATTACHES );
        }

        if ( _voeuxNonRattaches != null )
        {
            XmlUtil.beginElement( buffer, TAG_LISTE_VOEUX_NON_RATTACHES );

            for ( VoeuAmendement va : _voeuxNonRattaches )
            {
                buffer.append( va.getXml( request ) );
            }

            XmlUtil.endElement( buffer, TAG_LISTE_VOEUX_NON_RATTACHES );
        }

        if ( _voeuxAmendements != null )
        {
            XmlUtil.beginElement( buffer, TAG_LISTE_VOEUX_AMENDEMENTS );

            for ( VoeuAmendement va : _voeuxAmendements )
            {
                buffer.append( va.getXml( request ) );
            }

            XmlUtil.endElement( buffer, TAG_LISTE_VOEUX_AMENDEMENTS );
        }

        if ( _fichiers != null )
        {
            XmlUtil.beginElement( buffer, TAG_LISTE_FICHIERS );

            for ( Fichier fichier : _fichiers )
            {
                buffer.append( fichier.getXml( request ) );
            }

            XmlUtil.endElement( buffer, TAG_LISTE_FICHIERS );
        }

        XmlUtil.endElement( buffer, PDD.MARK_PDD );

        return buffer.toString(  );
    }
}
