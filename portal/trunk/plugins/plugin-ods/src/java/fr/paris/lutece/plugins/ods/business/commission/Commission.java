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
package fr.paris.lutece.plugins.ods.business.commission;

import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.ordredujour.EntreeOrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJour;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * Commission
 */
public class Commission
{
    private static final String TAG_ACTIF = "actif";
    private static final String TAG_LIBELLE = "libelle";
    private static final String TAG_NUMERO = "numero";
    private static final String TAG_COMMISSION = "commission";
    private static final String TAG_ID_COMMISSION = "id";
    private int _nIdCommission;
    private int _nNumero;
    private String _strLibelle;
    private Fichier _fichiers;
    private OrdreDuJour _ordreDuJour;
    private List<EntreeOrdreDuJour> _entreesOrdreDuJour;
    private Elu _elu;
    private boolean _bActif;

    /**
     * Retourne le _strLibelle de la commission
     * @return _strLibelle
     */
    public String getLibelle(  )
    {
        return _strLibelle;
    }

    /**
     * Affecte le _strLibelle de la commission
     * @param strLibelle le libell� � affecter
     */
    public void setLibelle( String strLibelle )
    {
        this._strLibelle = strLibelle;
    }

    /**
     * Retourne le num�ro de la commission
     * @return _nNumero
     */
    public int getNumero(  )
    {
        return _nNumero;
    }

    /**
     * Affecte le num�ro de la commission
     * @param nNumero le num�ro de la commission
     */
    public void setNumero( int nNumero )
    {
        this._nNumero = nNumero;
    }

    /**
     * Retourne l'identifiant de la commission
     * @return _nIdCommission
     */
    public int getIdCommission(  )
    {
        return _nIdCommission;
    }

    /**
     * Affecte l'identifiant de la commission
     * @param nIdCommission le num�ro de la commission
     */
    public void setIdCommission( int nIdCommission )
    {
        this._nIdCommission = nIdCommission;
    }

    /**
     * Retourne l'elu de la commission
     * @return Elu
     */
    public Elu getElus(  )
    {
        return _elu;
    }

    /**
     * Affecte l'elu � la commission
     * @param elu l'�lu
     */
    public void setElus( Elu elu )
    {
        this._elu = elu;
    }

    /**
     * Retourne la liste des ordre du jour de la commission
     * @return _entreesOrdreDuJour
     */
    public List<EntreeOrdreDuJour> getEntreesOrdreDuJour(  )
    {
        return _entreesOrdreDuJour;
    }

    /**
     * Affecte la liste des ordre du jour de la commission
     * @param entreesOrdreDuJour la liste des entr�es du jour
     */
    public void setEntreesOrdreDuJour( List<EntreeOrdreDuJour> entreesOrdreDuJour )
    {
        this._entreesOrdreDuJour = entreesOrdreDuJour;
    }

    /**
     * Retourne les _fichiers attach�s � la commission
     * @return _fichiers
     */
    public Fichier getFichiers(  )
    {
        return _fichiers;
    }

    /**
     * Affecte les _fichiers attach�s � la commission
     * @param fichier le fichier
     */
    public void setFichiers( Fichier fichier )
    {
        this._fichiers = fichier;
    }

    /**
     * Retourne l'ordre du jour de la commission
     * @return _ordreDuJour
     */
    public OrdreDuJour getOrdreDuJour(  )
    {
        return _ordreDuJour;
    }

    /**
     * Affecte l'ordre du jour de la commission
     * @param ordreDuJour l'ordre du jour
     */
    public void setOrdreDuJour( OrdreDuJour ordreDuJour )
    {
        this._ordreDuJour = ordreDuJour;
    }

    /**
     * Indique si la commission est au statut actif ou non.
     * @return le statut de la commission
     */
    public boolean isActif(  )
    {
        return _bActif;
    }

    /**
     * Fixe le statut de la commission.
     * @param bActif le nouveau statut de la commission.
     */
    public void setActif( boolean bActif )
    {
        _bActif = bActif;
    }

    /**
     * g�n�ration XML
     * @param request la requete Http
     * @return g�n�r� Commision en XML
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );
        attributes.put( TAG_ID_COMMISSION, OdsConstants.CONSTANTE_CHAINE_VIDE + _nIdCommission );
        XmlUtil.beginElement( buffer, TAG_COMMISSION, attributes );

        if ( _nNumero != -1 )
        {
            OdsUtils.addElement( buffer, TAG_NUMERO, _nNumero );
        }

        if ( _strLibelle != null )
        {
            OdsUtils.addElement( buffer, TAG_LIBELLE, _strLibelle );
        }

        OdsUtils.addElement( buffer, TAG_ACTIF, OdsConstants.CONSTANTE_CHAINE_VIDE + _bActif );
        XmlUtil.endElement( buffer, TAG_COMMISSION );

        return buffer.toString(  );
    }

    /**
     * Retourne l'id de la commission
     * @return l'id de la commission
     */
    public int hashCode(  )
    {
        return getIdCommission(  );
    }

    /**
     * Indique si les commissions sont �gales, cad si elles ont le m�me id
     * @param commission la commission � comparer
     * @return <b>true</b> si les id sont �gaux, <b>false</b> sinon
     */
    public boolean equals( Object commission )
    {
        if ( ( commission != null ) && commission instanceof Commission )
        {
            return ( new Integer( getIdCommission(  ) ).equals( new Integer( 
                    ( (Commission) commission ).getIdCommission(  ) ) ) );
        }

        return false;
    }
}
