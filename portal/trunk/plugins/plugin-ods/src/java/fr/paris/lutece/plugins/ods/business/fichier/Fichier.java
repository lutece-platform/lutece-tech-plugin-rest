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
package fr.paris.lutece.plugins.ods.business.fichier;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.sql.Timestamp;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * Fichier
 */
public class Fichier
{
    public static final String MARK_FICHIER = "fichier";
    private static final String VERSION = "version";
    private static final String DATE_PUBLICATION = "datePublication";
    private static final String EN_LIGNE = "enLigne";
    private static final String TEXTE = "texte";
    private static final String TAILLE = "taille";
    private static final String NOM = "nom";
    private static final String EXTENSION = "extension";
    private static final String TITRE = "titre";
    private static final String ID = "id";
    private int _nId;
    private String _strTitre;
    private String _strExtension;
    private String _strNom;
    private int _nTaille;
    private String _strTexte;
    private Boolean _bEnLigne;
    private Timestamp _tsDatePublication;
    private int _nVersion;
    private TypeDocument _typdeDocument;
    private Seance _seance;
    private PDD _pdd;
    private VoeuAmendement _voeuAdopte;
    private FichierPhysique _fichier;
    private Commission _commission;
    private FormationConseil _formationConseil;

    /**
     * Retourne la date de publication d'un _fichier
     * @return Timestamp _tsDatePublication
     */
    public Timestamp getDatePublication(  )
    {
        return _tsDatePublication;
    }

    /**
     * Affecte la date de publication
     * @param tsDatePublication Timestamp
     */
    public void setDatePublication( Timestamp tsDatePublication )
    {
        this._tsDatePublication = tsDatePublication;
    }

    /**
     * Retourne l'objet Déliberation associé à un _fichier
     * @return Objet Deliberation
     */
    public PDD getPDD(  )
    {
        return _pdd;
    }

    /**
     * Affecte l'objet Déliberation à un _fichier
     * @param pdd le PDD
     */
    public void setPDD( PDD pdd )
    {
        this._pdd = pdd;
    }

    /**
     * Retourne le boolean qui spécifie la mise en ligne d'un _fichier
     * @return Boolean
     */
    public Boolean getEnLigne(  )
    {
        return _bEnLigne;
    }

    /**
     * Affecte le boolean qui spécifie la mise en ligne d'un _fichier
     * @param bEnLigne le statut du fichier
     */
    public void setEnLigne( Boolean bEnLigne )
    {
        this._bEnLigne = bEnLigne;
    }

    /**
     * Retourne l'objet FichierPhysique associé à un _fichier
     * @return FichierPhysique _fichier
     */
    public FichierPhysique getFichier(  )
    {
        return _fichier;
    }

    /**
     *  Affecte l'objet Fichier à un _fichier
     * @param fichier le fichier
     */
    public void setFichier( FichierPhysique fichier )
    {
        this._fichier = fichier;
    }

    /**
     * Retourne l'objet Seance associé à un _fichier
     * @return Seance _seance
     */
    public Seance getSeance(  )
    {
        return _seance;
    }

    /**
     * Affecte l'objet Seance à un _fichier
     * @param seance la séance
     */
    public void setSeance( Seance seance )
    {
        this._seance = seance;
    }

    /**
     * Retourne le _strTexte qui sert a la recherche
     * @return String _strTexte
     */
    public String getTexte(  )
    {
        return _strTexte;
    }

    /**
     * Affecte le _strTexte qui sert a la recherche
     * @param strTexte le texte
     */
    public void setTexte( String strTexte )
    {
        this._strTexte = strTexte;
    }

    /**
     * Retourne le _strTitre d'un _fichier
     * @return String _strTitre
     */
    public String getTitre(  )
    {
        return _strTitre;
    }

    /**
     * Affecte le _strTitre a un _fichier
     * @param strTitre le titre du fichier
     */
    public void setTitre( String strTitre )
    {
        this._strTitre = strTitre;
    }

    /**
     * Retourne l'objet TypeDocument associé à un _fichier
     * @return TypeDocument _typdeDocument
     */
    public TypeDocument getTypdeDocument(  )
    {
        return _typdeDocument;
    }

    /**
     * Affecte l'Objet TypeDocument à un _fichier
     * @param typdeDocument le type de document
     */
    public void setTypdeDocument( TypeDocument typdeDocument )
    {
        this._typdeDocument = typdeDocument;
    }

    /**
     * Retourne la _nVersion d'un _fichier
     * @return int _nVersion
     */
    public int getVersion(  )
    {
        return _nVersion;
    }

    /**
     * Affecte la _nVersion à un _fichier
     * @param nVersion le numéro de la version
     */
    public void setVersion( int nVersion )
    {
        this._nVersion = nVersion;
    }

    /**
     * Retourne l'objet VoeuAmendement associé à un _fichier
     * @return VoeuAmendement _voeuAdopte
     */
    public VoeuAmendement getVoeuAdopte(  )
    {
        return _voeuAdopte;
    }

    /**
     * Affecte l'Objet VoeuAmendement à un _fichier
     * @param voeuAdopte le voeu adopté
     */
    public void setVoeuAdopte( VoeuAmendement voeuAdopte )
    {
        this._voeuAdopte = voeuAdopte;
    }

    /**
     * Retourne l'_nId d'un _fichier
     * @return int _nId
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Affecte l'_nId à un _fichier
     * @param nId l'identifiant d'un fichier
     */
    public void setId( int nId )
    {
        this._nId = nId;
    }

    /**
     * Retourne l'objet Commission associé à un _fichier
     * @return Commission _commission
     */
    public Commission getCommission(  )
    {
        return _commission;
    }

    /**
     * Affecte l'Objet Commission à un _fichier
     * @param commission la comission
     */
    public void setCommission( Commission commission )
    {
        this._commission = commission;
    }

    /**
     * Retourne l'_strExtension d'un _fichier
     * @return String _strExtension
     */
    public String getExtension(  )
    {
        return _strExtension;
    }

    /**
     * Affecte l'_strExtension à un _fichier
     * @param strExtension l'extension du fichier
     */
    public void setExtension( String strExtension )
    {
        this._strExtension = strExtension;
    }

    /**
     * Retourne la taille du fichier physique
     * @return int la taille du fichier physique
     */
    public int getTaille(  )
    {
        return _nTaille;
    }

    /**
     * Retourne la taille du fichier physique en ko
     * @return int la taille du fichier physique
     */
    public int getTailleKo(  )
    {
        return _nTaille / 1000;
    }

    /**
     * Affecte la _lTaille à un _fichier
     * @param taille la taille du fichier physique
     */
    public void setTaille( int taille )
    {
        this._nTaille = taille;
    }

    /**
     * Retourne le _strNom d'un _fichier
     * @return String _strNom
     */
    public String getNom(  )
    {
        return _strNom;
    }

    /**
     * Affecte le _strNom à un _fichier
     * @param strNom le nom du fichier
     */
    public void setNom( String strNom )
    {
        this._strNom = strNom;
    }

    /**
     * renvoie le hashcode de l'objet
     * @return le hashcode
     */
    public int hashCode(  )
    {
        return getId(  );
    }

    /**
     * verifie l'equivalence entre 2 objets
     * @param fichier objet fichier à comparer
     * @return TRUE si l'id est identique, FALSE sinon
     */
    public boolean equals( Object fichier )
    {
        if ( fichier instanceof Fichier )
        {
            return ( new Integer( ( (Fichier) fichier ).getId(  ) ) ).equals( new Integer( getId(  ) ) );
        }

        return false;
    }

    /**
     *
     * @return la formation conseil du fichier
     */
    public FormationConseil getFormationConseil(  )
    {
        return _formationConseil;
    }

    /**
     *
     * @param conseil la formation conseil du fichier
     */
    public void setFormationConseil( FormationConseil conseil )
    {
        _formationConseil = conseil;
    }

    /**
     * génération XML
     * @param request la requete http
     * @return génére Fichier en XML
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );
        attributes.put( ID, OdsConstants.CONSTANTE_CHAINE_VIDE + _nId );
        XmlUtil.beginElement( buffer, Fichier.MARK_FICHIER, attributes );

        if ( _strTitre != null )
        {
            OdsUtils.addElement( buffer, TITRE, _strTitre );
        }

        if ( _strExtension != null )
        {
            OdsUtils.addElement( buffer, EXTENSION, _strExtension );
        }

        if ( _strNom != null )
        {
            OdsUtils.addElement( buffer, NOM, _strNom );
        }

        if ( _nTaille != -1 )
        {
            OdsUtils.addElement( buffer, TAILLE, OdsConstants.CONSTANTE_CHAINE_VIDE + _nTaille );
        }

        if ( _strTexte != null )
        {
            OdsUtils.addElement( buffer, TEXTE, _strTexte );
        }

        OdsUtils.addElement( buffer, EN_LIGNE, OdsConstants.CONSTANTE_CHAINE_VIDE + _bEnLigne );

        if ( _tsDatePublication != null )
        {
            OdsUtils.addElement( buffer, DATE_PUBLICATION, OdsConstants.CONSTANTE_CHAINE_VIDE + _tsDatePublication );
        }

        if ( _nVersion != -1 )
        {
            OdsUtils.addElement( buffer, VERSION, OdsConstants.CONSTANTE_CHAINE_VIDE + _nVersion );
        }

        if ( _typdeDocument != null )
        {
            buffer.append( _typdeDocument.getXml( request ) );
        }

        XmlUtil.endElement( buffer, Fichier.MARK_FICHIER );

        return buffer.toString(  );
    }
}
