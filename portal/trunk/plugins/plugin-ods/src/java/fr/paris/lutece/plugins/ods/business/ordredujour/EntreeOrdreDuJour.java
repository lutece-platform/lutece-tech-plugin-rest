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

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.naturedossier.NatureDesDossiers;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * Objet Entr�eOrdreDuJour, correspond � une entr�e de la table
 * ods_entree_ordre_jour
 */
public class EntreeOrdreDuJour
{
    private static final String TAG_LISTE_ELUS = "listeElus";
    private static final String TAG_LISTE_COMMISSIONS = "listeCommissions";
    private static final String TAG_STYLE = "style";
    private static final String TAG_OBJET = "objet";
    private static final String TAG_TEXTE_LIBRE = "texteLibre";
    private static final String TAG_NUMERO_ORDRE = "numeroOrdre";
    private static final String TAG_TYPE = "type";
    private static final String TAG_ID = "id";
    private static final String TAG_ENTREE_ORDRE_DU_JOUR = "entreeOrdreDuJour";
    private int _nIdEntreeOrdreDuJour;
    private VoeuAmendement _voeuAmendement;
    private OrdreDuJour _ordreDuJour;
    private NatureDesDossiers _nature;
    private PDD _pdd;
    private String _strType;
    private int _nNumeroOrdre;
    private String _strTexteLibre;
    private String _strReference;
    private String _strObjet;
    private String _strStyle;
    private List<Commission> _commissions;
    private List<Elu> _elus;

    /**
     * Retourne la nature d'affaire de l'entr�e
     *
     * @return la nature d'affaire de l'entr�e
     */
    public NatureDesDossiers getNature(  )
    {
        return _nature;
    }

    /**
     * Fixe la nature d'affaire de l'entr�e
     *
     * @param nature
     *            la nature d'affaire de l'entr�e
     */
    public void setNature( NatureDesDossiers nature )
    {
        _nature = nature;
    }

    /**
     * Retourne le num�ro d'ordre de l'entr�e
     *
     * @return le num�ro d'ordre de l'entr�e
     */
    public int getNumeoOrdre(  )
    {
        return _nNumeroOrdre;
    }

    /**
     * Fixe le num�ro d'ordre de l'entr�e
     *
     * @param nOrdre
     *            le num�ro d'ordre de l'entr�e
     */
    public void setNumeroOrdre( int nOrdre )
    {
        _nNumeroOrdre = nOrdre;
    }

    /**
     * Retourne l'ordre du jour auquel est rattach�e l'entr�e
     *
     * @return l'ordre du jour auquel est rattach�e l'entr�e
     */
    public OrdreDuJour getOrdreDuJour(  )
    {
        return _ordreDuJour;
    }

    /**
     * Fixe l'ordre du jour auquel est rattach�e l'entr�e
     *
     * @param ordreDuJour
     *            l'ordre du jour de l'entr�e
     */
    public void setOrdreDuJour( OrdreDuJour ordreDuJour )
    {
        _ordreDuJour = ordreDuJour;
    }

    /**
     * Retourne l'objet de l'entr�e
     *
     * @return l'objet de l'entr�e
     */
    public String getObjet(  )
    {
        return _strObjet;
    }

    /**
     * Fixe l'objet de l'entr�e
     *
     * @param strObjet
     *            l'objet de l'entr�e
     */
    public void setObjet( String strObjet )
    {
        _strObjet = strObjet;
    }

    /**
     * Retourne la r�f�rence de l'entr�e
     *
     * @return la r�f�rence de l'entr�e
     */
    public String getReference(  )
    {
        return _strReference;
    }

    /**
     * Fixe la r�f�rence de l'entr�e
     *
     * @param strReference
     *            la r�f�rence de l'entr�e
     */
    public void setReference( String strReference )
    {
        _strReference = strReference;
    }

    /**
     * Retourne le style de l'entr�e
     *
     * @return le style de l'entr�e
     */
    public String getStyle(  )
    {
        return _strStyle;
    }

    /**
     * Fixe le style de l'entr�e
     *
     * @param strStyle
     *            le style de l'entr�e
     */
    public void setStyle( String strStyle )
    {
        _strStyle = strStyle;
    }

    /**
     * Retourne le texte de l'entr�e
     *
     * @return le texte de l'entr�e
     */
    public String getTexteLibre(  )
    {
        return _strTexteLibre;
    }

    /**
     * Fixe le texte de l'entr�e
     *
     * @param strTexte
     *            le texte de l'entr�e
     */
    public void setTexte( String strTexte )
    {
        _strTexteLibre = strTexte;
    }

    /**
     * Retourne le type de l'entr�e
     *
     * @return le type de l'entr�e
     */
    public String getType(  )
    {
        return _strType;
    }

    /**
     * Fixe le type de l'entr�e
     *
     * @param strType
     *            le type de l'entr�e
     */
    public void setType( String strType )
    {
        _strType = strType;
    }

    /**
     *
     * @return l'id de l'entree d'ordre du jour
     */
    public int getIdEntreeOrdreDuJour(  )
    {
        return _nIdEntreeOrdreDuJour;
    }

    /**
     *
     * @param idEntreeOrdreDuJour
     *            affecte un id a l'entree d'ordre du jour
     */
    public void setIdEntreeOrdreDuJour( int idEntreeOrdreDuJour )
    {
        _nIdEntreeOrdreDuJour = idEntreeOrdreDuJour;
    }

    /**
     *
     * @return le pdd de l'entr�e
     */
    public PDD getPdd(  )
    {
        return _pdd;
    }

    /**
     * @param pdd le pdd a affecter � l'entr�e
     */
    public void setPdd( PDD pdd )
    {
        this._pdd = pdd;
    }

    /**
     *
     * @return retourne le voeu de l'entr�e
     */
    public VoeuAmendement getVoeuAmendement(  )
    {
        return _voeuAmendement;
    }

    /**
     *
     * @param amendement affecte un voeu � l'entree
     */
    public void setVoeuAmendement( VoeuAmendement amendement )
    {
        _voeuAmendement = amendement;
    }

    /**
     *
     * @return la liste des commissions de l'entr�e
     */
    public List<Commission> getCommissions(  )
    {
        return _commissions;
    }

    /**
     *
     * @param commissions la liste des commissions � affecter � l'entr�e
     */
    public void setCommissions( List<Commission> commissions )
    {
        this._commissions = commissions;
    }

    /**
     *
     * @return retourne liste des rapporteurs de l'entr�e d'ordre du jour
     */
    public List<Elu> getElus(  )
    {
        return _elus;
    }

    /**
     *
     * @param elus
     *            affecte une liste de rapporteur a l'entr�e d'ordre du jour
     */
    public void setElus( List<Elu> elus )
    {
        this._elus = elus;
    }

    /**
     * Test si la liste des commissions pass�e en parametre est egale a la liste
     * des commissions de l'entr�e
     *
     * @param listCommissions
     *            liste des commissions
     * @return true si la liste des commissions pass�e en parametre est egale a
     *         la liste des commissions de l'entr�e,false sinon
     */
    public boolean getEqualListCommissions( List<Commission> listCommissions )
    {
        if ( _commissions.size(  ) != listCommissions.size(  ) )
        {
            return false;
        }

        for ( int cpt = 0; cpt < _commissions.size(  ); cpt++ )
        {
            if ( _commissions.get( cpt ).getIdCommission(  ) != listCommissions.get( cpt ).getIdCommission(  ) )
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Test si la liste des �lus pass�e en parametre est egale a la liste des
     * rapporteurs de l'entr�e
     *
     * @param listElus
     *            liste de rapporteurs
     * @return true si la liste des rapporteurs de l'entr�e est egale a la liste
     *         pass� en parametre
     */
    public boolean getEqualListRapporteurs( List<Elu> listElus )
    {
        if ( _elus.size(  ) != listElus.size(  ) )
        {
            return false;
        }

        for ( int cpt = 0; cpt < _elus.size(  ); cpt++ )
        {
            if ( _elus.get( cpt ).getIdElu(  ) != listElus.get( cpt ).getIdElu(  ) )
            {
                return false;
            }
        }

        return true;
    }

    /**
     * G�n�ration du XML d'un objet
     *
     * @param request
     *            la requ�te
     * @return Le code XML repr�sentant une entr�e d'ordre du jour
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );

        attributes.put( TAG_ID, _nIdEntreeOrdreDuJour );
        XmlUtil.beginElement( buffer, TAG_ENTREE_ORDRE_DU_JOUR, attributes );

        if ( _strType != null )
        {
            OdsUtils.addElement( buffer, TAG_TYPE, _strType );
        }

        if ( _nNumeroOrdre >= 0 )
        {
            OdsUtils.addElement( buffer, TAG_NUMERO_ORDRE, _nNumeroOrdre );
        }

        if ( _strTexteLibre != null )
        {
            OdsUtils.addElement( buffer, TAG_TEXTE_LIBRE, _strTexteLibre );
        }

        if ( _strReference != null )
        {
            OdsUtils.addElement( buffer, OdsParameters.REFERENCE, _strReference );
        }

        if ( _strObjet != null )
        {
            OdsUtils.addElement( buffer, TAG_OBJET, _strObjet );
        }

        if ( _strStyle != null )
        {
            OdsUtils.addElement( buffer, TAG_STYLE, _strStyle );
        }

        if ( _voeuAmendement != null )
        {
            buffer.append( _voeuAmendement.getXml( request ) );
        }

        if ( _nature != null )
        {
            buffer.append( _nature.getXml( request ) );
        }

        if ( _pdd != null )
        {
            buffer.append( _pdd.getXml( request ) );
        }

        if ( ( _commissions != null ) && !_commissions.isEmpty(  ) )
        {
            XmlUtil.beginElement( buffer, TAG_LISTE_COMMISSIONS );

            for ( Commission commission : _commissions )
            {
                if ( commission != null )
                {
                    buffer.append( commission.getXml( request ) );
                }
            }

            XmlUtil.endElement( buffer, TAG_LISTE_COMMISSIONS );
        }

        if ( ( _elus != null ) && !_elus.isEmpty(  ) )
        {
            XmlUtil.beginElement( buffer, TAG_LISTE_ELUS );

            for ( Elu elu : _elus )
            {
                if ( elu != null )
                {
                    buffer.append( elu.getXml( request ) );
                }
            }

            XmlUtil.endElement( buffer, TAG_LISTE_ELUS );
        }

        XmlUtil.endElement( buffer, TAG_ENTREE_ORDRE_DU_JOUR );

        return buffer.toString(  );
    }
}
