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
     * Retourne l'état d'activité de l'élu
     * @return l'état d'activité de l'élu
     */
    public boolean isActif(  )
    {
        return _bActif;
    }

    /**
     * Fixe l'état d'activité de l'élu  avec la valeur passée en paramètre
     * @param bActif l'état d'activité de l'élu
     */
    public void setActif( boolean bActif )
    {
        _bActif = bActif;
    }

    /**
     * Retourne le groupe politique de l'élu
     * @return le groupe politique de l'élu
     */
    public GroupePolitique getGroupe(  )
    {
        return _groupe;
    }

    /**
     * Fixe le groupe politique de l'élu  avec la valeur passée en paramètre
     * @param groupe groupe politique de l'élu
     */
    public void setGroupe( GroupePolitique groupe )
    {
        _groupe = groupe;
    }

    /**
     * Retourne l'identifiant de l'élu
     * @return id de l'élu
     */
    public int getIdElu(  )
    {
        return _nIdElu;
    }

    /**
     * Fixe l'identifiant de l'élu avec la valeur passée en paramètre
     * @param nIdElu id de l'élu
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
     * Fixe l'identifiant de la commission avec la valeur passée en paramètre
     * @param commission l'identifiant de la commission
     */
    public void setCommission( Commission commission )
    {
        _commission = commission;
    }

    /**
     * Retourne l'élu remplaçant si l'élu est rapporteur pour une commission
     * @return l'élu remplaçant
     */
    public Elu getEluRemplace(  )
    {
        return _eluRemplace;
    }

    /**
     * Fixe l'élu remplaçant
     * @param eluRemplace l'élu remplaçant
     */
    public void setEluRemplace( Elu eluRemplace )
    {
        _eluRemplace = eluRemplace;
    }

    /**
     * Retourne la civilité de l'élu
     * @return la civilité de l'élu
     */
    public String getCivilite(  )
    {
        return _strCivilite;
    }

    /**
     * Fixe la civilité de l'élu avec la valeur passée en paramètre
     * @param strCivilite la civilité de l'élu
     */
    public void setCivilite( String strCivilite )
    {
        _strCivilite = strCivilite;
    }

    /**
     * Retourne le nom de l'élu
     * @return le nom de l'élu
     */
    public String getNomElu(  )
    {
        return _strNomElu;
    }

    /**
     * Fixe le nom de l'élu avec la valeur passée en paramètre
     * @param strNomElu le nom de l'élu
     */
    public void setNomElu( String strNomElu )
    {
        _strNomElu = strNomElu;
    }

    /**
     * Retourne le prénom de l'élu
     * @return le prénom de l'élu
     */
    public String getPrenomElu(  )
    {
        return _strPrenomElu;
    }

    /**
     * Fixe le prénom  de l'élu avec la valeur passée en paramètre
     * @param strPrenomElu le prénom  de l'élu
     */
    public void setPrenomElu( String strPrenomElu )
    {
        _strPrenomElu = strPrenomElu;
    }

    /**
     * Génération XML
     * @param request la requete Http
     * @return génére Elu en XML
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
     * Retourne un int unique par élu
     * @return l'id de l'élu
     */
    public int hashCode(  )
    {
        return getIdElu(  );
    }

    /**
     * Indique si 2 objets Elu sont égaux
     * @param elu l'élu à comparer
     * @return <b>true</b> si les id des 2 élus sont égaux, <b>false</b> sinon
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
