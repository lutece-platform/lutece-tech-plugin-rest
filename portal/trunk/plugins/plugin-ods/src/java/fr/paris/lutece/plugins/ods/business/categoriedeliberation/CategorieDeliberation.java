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
package fr.paris.lutece.plugins.ods.business.categoriedeliberation;

import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * Objet CategorieDeliberation, correspond � un enregistrement dans la table ods_categorie_deliberation
 */
public class CategorieDeliberation
{
    private static final String TAG_CATEGORIE_DELIBERATION = "categorieDeliberation";
    private static final String TAG_ID = "id";
    private static final String TAG_CODE = "code";
    private static final String TAG_LIBELLE = "libelle";
    private static final String TAG_ACTIF = "actif";
    private int _nIdCategorie;
    private int _nCode;
    private String _strLibelle;
    private boolean _bActif;

    /**
     * Indique le statut de la cat�gorie de d�lib�ration.
     * @return le statut de la cat�gorie de d�lib�ration
     */
    public boolean isActif(  )
    {
        return _bActif;
    }

    /**
     * Fixe le statut de la cat�gorie de d�lib�ration.
     * @param bActif le nouveau statut de la cat�gorie de d�lib�ration
     */
    public void setActif( boolean bActif )
    {
        _bActif = bActif;
    }

    /**
     * Retourne le code de la cat�gorie de d�lib�ration.
     * @return le code de la cat�gorie de d�lib�ration
     */
    public int getCode(  )
    {
        return _nCode;
    }

    /**
     * Fixe le code de la cat�gorie de d�lib�ration.
     * @param nCode le nouveau code de la cat�gorie de d�lib�ration
     */
    public void setCode( int nCode )
    {
        _nCode = nCode;
    }

    /**
     * Retourne l'identifiant de la cat�gorie de d�lib�ration.
     * @return l'identifiant de la cat�gorie de d�lib�ration
     */
    public int getIdCategorie(  )
    {
        return _nIdCategorie;
    }

    /**
     * Fixe l'identifiant de la cat�gorie de d�lib�ration.
     * @param nIdCategorie le nouvel identifiant de la cat�gorie de d�lib�ration
     */
    public void setIdCategorie( int nIdCategorie )
    {
        _nIdCategorie = nIdCategorie;
    }

    /**
     * Retourne l'intitul� de la cat�gorie de d�lib�ration.
     * @return l'intitul� de la cat�gorie de d�lib�ration
     */
    public String getLibelle(  )
    {
        return _strLibelle;
    }

    /**
     * Fixe l'intitul� de la cat�gorie de d�lib�ration.
     * @param strLibelle le nouvel intitul� de la cat�gorie de d�lib�ration
     */
    public void setLibelle( String strLibelle )
    {
        _strLibelle = strLibelle;
    }

    /**
     * G�n�ration du XML d'un objet
     * @param request la requ�te
     * @return Le code XML repr�sentant une cazt�gorie de d�lib�ration
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );

        attributes.put( TAG_ID, _nIdCategorie );
        XmlUtil.beginElement( buffer, TAG_CATEGORIE_DELIBERATION, attributes );

        if ( _nCode >= 0 )
        {
            OdsUtils.addElement( buffer, TAG_CODE, _nCode );
        }

        if ( _strLibelle != null )
        {
            OdsUtils.addElement( buffer, TAG_LIBELLE, _strLibelle );
        }

        OdsUtils.addElement( buffer, TAG_ACTIF, OdsConstants.CONSTANTE_CHAINE_VIDE + _bActif );

        XmlUtil.endElement( buffer, TAG_CATEGORIE_DELIBERATION );

        return buffer.toString(  );
    }
}
