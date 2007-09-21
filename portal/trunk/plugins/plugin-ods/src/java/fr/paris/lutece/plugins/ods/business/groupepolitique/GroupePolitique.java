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
package fr.paris.lutece.plugins.ods.business.groupepolitique;

import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * Groupe politique
 */
public class GroupePolitique
{
    private static final String TAG_ACTIF = "actif";
    private static final String TAG_NOM_COMPLET = "nomComplet";
    private static final String TAG_NOM_GROUPE = "nomGroupe";
    private static final String TAG_ID = "id";
    private static final String TAG_GROUPE_POLITIQUE = "groupePolitique";
    private int _nIdGroupe;
    private String _strNomGroupe;
    private String _strNomComplet;
    private boolean _bActif;

    /**
     * retourne l'état du groupe politique
     * @return l'état du groupe politique
     */
    public boolean isActif(  )
    {
        return _bActif;
    }

    /**
     * affecte l'état du groupe politique avec la valeur passée en paramètre
     * @param bActif l'état du groupe politique
     */
    public void setActif( boolean bActif )
    {
        this._bActif = bActif;
    }

    /**
     * retourne l'id du groupe politique
     * @return id du groupe politique
     */
    public int getIdGroupe(  )
    {
        return _nIdGroupe;
    }

    /**
     * affecte l'id du groupe politique avec la valeur passée en paramètre
     * @param nIdGroupePolitique  id du groupe politique
     */
    public void setIdGroupe( int nIdGroupePolitique )
    {
        _nIdGroupe = nIdGroupePolitique;
    }

    /**
     * retourne le nom  du groupe politique
     * @return  nom du groupe politique
     */
    public String getNomGroupe(  )
    {
        return _strNomGroupe;
    }

    /**
     * affecte le nom du groupe politique avec la valeur passée en paramètre
     * @param strNomGroupe le nom du groupe
     */
    public void setNomGroupe( String strNomGroupe )
    {
        _strNomGroupe = strNomGroupe;
    }

    /**
     * retourne le nom complet  du groupe politique
     * @return  nom complet  du groupe politique
     */
    public String getNomComplet(  )
    {
        return _strNomComplet;
    }

    /**
     *  affecte le nom complet du groupe  politique avec la valeur passée en paramètre
     * @param strNomComplet le nom complet du groupe
     */
    public void setNomComplet( String strNomComplet )
    {
        _strNomComplet = strNomComplet;
    }

    /**
     * Génèration du XML d'un objet
     * @param request la requête
     * @return Le code XML représentant un groupe politique
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );

        attributes.put( TAG_ID, _nIdGroupe );
        XmlUtil.beginElement( buffer, TAG_GROUPE_POLITIQUE, attributes );

        if ( _strNomGroupe != null )
        {
            OdsUtils.addElement( buffer, TAG_NOM_GROUPE, _strNomGroupe );
        }

        if ( _strNomComplet != null )
        {
            OdsUtils.addElement( buffer, TAG_NOM_COMPLET, _strNomComplet );
        }

        OdsUtils.addElement( buffer, TAG_ACTIF, OdsConstants.CONSTANTE_CHAINE_VIDE + _bActif );

        XmlUtil.endElement( buffer, TAG_GROUPE_POLITIQUE );

        return buffer.toString(  );
    }
}
