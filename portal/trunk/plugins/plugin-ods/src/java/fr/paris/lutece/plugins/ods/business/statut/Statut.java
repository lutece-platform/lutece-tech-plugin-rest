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
package fr.paris.lutece.plugins.ods.business.statut;

import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * Statut d'un Voeu ou Amendement
 */
public class Statut
{
    private static final String TAG_EST_POUR_PDD = "estPourPdd";
    private static final String TAG_EST_POUR_VOEU = "estPourVoeu";
    private static final String TAG_EST_POUR_AMENDEMENT = "estPourAmendement";
    private static final String TAG_LIBELLE = "libelle";
    private static final String TAG_ID = "id";
    private static final String TAG_STATUT = "statut";

    /*
     * Attributs décrivant un Statut de Voeu Amendement
     */
    private int _nIdStatut = -1;
    private String _strLibelle;
    private boolean _bPourAmendement;
    private boolean _bPourVoeu;
    private boolean _bPourPDD;

    /**
     * Retourne si le statut concerne les amendements.
     * @return TRUE si le statut concerne les amendements, FALSE sinon
     */
    public boolean estPourAmendement(  )
    {
        return _bPourAmendement;
    }

    /**
     * Fixe si le statut concerne les amendements.
     * @param bPourAmendement TRUE ou FALSE
     */
    public void setPourAmendement( boolean bPourAmendement )
    {
        _bPourAmendement = bPourAmendement;
    }

    /**
     * Retourne si le statut concerne les PDD.
     * @return TRUE si le statut concerne les PDD, FALSE sinon
     */
    public boolean estPourPDD(  )
    {
        return _bPourPDD;
    }

    /**
     * Fixe si le statut concerne les PDD.
     * @param bPourPDD TRUE ou FALSE
     */
    public void setPourPDD( boolean bPourPDD )
    {
        _bPourPDD = bPourPDD;
    }

    /**
     * Retourne si le statut concerne les voeux.
     * @return TRUE si le statut concerne les voeux, FALSE sinon
     */
    public boolean estPourVoeu(  )
    {
        return _bPourVoeu;
    }

    /**
     * Fixe si le statut concerne les voeux.
     * @param bPourVoeu TRUE ou FALSE
     */
    public void setPourVoeu( boolean bPourVoeu )
    {
        _bPourVoeu = bPourVoeu;
    }

    /**
     * Retourne l'identifiant du statut.
     * @return l'identifiant du statut
     */
    public int getIdStatut(  )
    {
        return _nIdStatut;
    }

    /**
     * Affecte une valeur à l'identifiant du statut.
     * @param nIdStatut l'identifiant
     */
    public void setIdStatut( int nIdStatut )
    {
        _nIdStatut = nIdStatut;
    }

    /**
     * Retourne le libellé du statut.
     * @return le libellé du statut
     */
    public String getLibelle(  )
    {
        return _strLibelle;
    }

    /**
     * Affecte un libellé au statut.
     * @param strLibelle le libellé du statut
     */
    public void setLibelle( String strLibelle )
    {
        _strLibelle = strLibelle;
    }

    /**
     * Génèration du XML d'un objet
     * @param request la requête
     * @return Le code XML représentant un statut
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );

        attributes.put( TAG_ID, _nIdStatut );
        XmlUtil.beginElement( buffer, TAG_STATUT, attributes );

        if ( _strLibelle != null )
        {
            OdsUtils.addElement( buffer, TAG_LIBELLE, _strLibelle );
        }

        OdsUtils.addElement( buffer, TAG_EST_POUR_AMENDEMENT, OdsConstants.CONSTANTE_CHAINE_VIDE + _bPourAmendement );
        OdsUtils.addElement( buffer, TAG_EST_POUR_VOEU, OdsConstants.CONSTANTE_CHAINE_VIDE + _bPourVoeu );
        OdsUtils.addElement( buffer, TAG_EST_POUR_PDD, OdsConstants.CONSTANTE_CHAINE_VIDE + _bPourPDD );

        XmlUtil.endElement( buffer, TAG_STATUT );

        return buffer.toString(  );
    }
}
