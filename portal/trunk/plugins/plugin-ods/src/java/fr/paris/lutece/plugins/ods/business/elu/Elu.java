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
package fr.paris.lutece.plugins.ods.business.elu;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitique;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * Elu
 */
public class Elu
{
    private static final String TAG_PRENOM_ELU = "prenomElu";
    private static final String TAG_NOM_ELU = "nomElu";
    private static final String TAG_CIVILITE = "civilite";
    private static final String TAG_ELU = "elu";
    private static final String TAG_ID_ELU = "id";
    private static final String TAG_ACTIF = "actif";
    private int _nIdElu;
    private GroupePolitique _groupe;
    private Commission _commission;
    private Elu _eluRemplace;
    private String _strCivilite;
    private String _strNomElu;
    private String _strPrenomElu;
    private boolean _bActif;

    /**
     * Retourne l'�tat d'activit� de l'�lu
     * @return l'�tat d'activit� de l'�lu
     */
    public boolean isActif(  )
    {
        return _bActif;
    }

    /**
     * Fixe l'�tat d'activit� de l'�lu  avec la valeur pass�e en param�tre
     * @param bActif l'�tat d'activit� de l'�lu
     */
    public void setActif( boolean bActif )
    {
        _bActif = bActif;
    }

    /**
     * Retourne le groupe politique de l'�lu
     * @return le groupe politique de l'�lu
     */
    public GroupePolitique getGroupe(  )
    {
        return _groupe;
    }

    /**
     * Fixe le groupe politique de l'�lu  avec la valeur pass�e en param�tre
     * @param groupe groupe politique de l'�lu
     */
    public void setGroupe( GroupePolitique groupe )
    {
        _groupe = groupe;
    }

    /**
     * Retourne l'identifiant de l'�lu
     * @return id de l'�lu
     */
    public int getIdElu(  )
    {
        return _nIdElu;
    }

    /**
     * Fixe l'identifiant de l'�lu avec la valeur pass�e en param�tre
     * @param nIdElu id de l'�lu
     */
    public void setIdElu( int nIdElu )
    {
        _nIdElu = nIdElu;
    }

    /**
     * retourne l'id de la commission
     * @return id de la commission
     */
    public Commission getCommission(  )
    {
        return _commission;
    }

    /**
     * Fixe l'identifiant de la commission avec la valeur pass�e en param�tre
     * @param commission l'identifiant de la commission
     */
    public void setCommission( Commission commission )
    {
        _commission = commission;
    }

    /**
     * Retourne l'�lu rempla�ant si l'�lu est rapporteur pour une commission
     * @return l'�lu rempla�ant
     */
    public Elu getEluRemplace(  )
    {
        return _eluRemplace;
    }

    /**
     * Fixe l'�lu rempla�ant
     * @param eluRemplace l'�lu rempla�ant
     */
    public void setEluRemplace( Elu eluRemplace )
    {
        _eluRemplace = eluRemplace;
    }

    /**
     * Retourne la civilit� de l'�lu
     * @return la civilit� de l'�lu
     */
    public String getCivilite(  )
    {
        return _strCivilite;
    }

    /**
     * Fixe la civilit� de l'�lu avec la valeur pass�e en param�tre
     * @param strCivilite la civilit� de l'�lu
     */
    public void setCivilite( String strCivilite )
    {
        _strCivilite = strCivilite;
    }

    /**
     * Retourne le nom de l'�lu
     * @return le nom de l'�lu
     */
    public String getNomElu(  )
    {
        return _strNomElu;
    }

    /**
     * Fixe le nom de l'�lu avec la valeur pass�e en param�tre
     * @param strNomElu le nom de l'�lu
     */
    public void setNomElu( String strNomElu )
    {
        _strNomElu = strNomElu;
    }

    /**
     * Retourne le pr�nom de l'�lu
     * @return le pr�nom de l'�lu
     */
    public String getPrenomElu(  )
    {
        return _strPrenomElu;
    }

    /**
     * Fixe le pr�nom  de l'�lu avec la valeur pass�e en param�tre
     * @param strPrenomElu le pr�nom  de l'�lu
     */
    public void setPrenomElu( String strPrenomElu )
    {
        _strPrenomElu = strPrenomElu;
    }

    /**
     * G�n�ration XML
     * @param request la requete Http
     * @return g�n�re Elu en XML
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );
        attributes.put( TAG_ID_ELU, OdsConstants.CONSTANTE_CHAINE_VIDE + _nIdElu );
        XmlUtil.beginElement( buffer, TAG_ELU, attributes );

        if ( _strCivilite != null )
        {
            OdsUtils.addElement( buffer, TAG_CIVILITE, _strCivilite );
        }

        if ( _strNomElu != null )
        {
            OdsUtils.addElement( buffer, TAG_NOM_ELU, _strNomElu );
        }

        if ( _strPrenomElu != null )
        {
            OdsUtils.addElement( buffer, TAG_PRENOM_ELU, _strPrenomElu );
        }

        OdsUtils.addElement( buffer, TAG_ACTIF, OdsConstants.CONSTANTE_CHAINE_VIDE + _bActif );

        if ( _groupe != null )
        {
            buffer.append( _groupe.getXml( request ) );
        }

        if ( _commission != null )
        {
            buffer.append( _commission.getXml( request ) );
        }

        XmlUtil.endElement( buffer, TAG_ELU );

        return buffer.toString(  );
    }

    /**
     * Retourne un int unique par �lu
     * @return l'id de l'�lu
     */
    public int hashCode(  )
    {
        return getIdElu(  );
    }

    /**
     * Indique si 2 objets Elu sont �gaux
     * @param elu l'�lu � comparer
     * @return <b>true</b> si les id des 2 �lus sont �gaux, <b>false</b> sinon
     */
    public boolean equals( Object elu )
    {
        if ( elu instanceof Elu )
        {
            return ( new Integer( getIdElu(  ) ).equals( new Integer( ( (Elu) elu ).getIdElu(  ) ) ) );
        }

        return false;
    }
}
