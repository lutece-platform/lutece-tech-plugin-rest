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

import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * Objet représentant le type d'un ordre du jour
 */
public class TypeOrdreDuJour
{
    private static final String TAG_LIBELLE_LONG = "libelleLong";
    private static final String TAG_LIBELLE = "libelle";
    private static final String TAG_ID = "id";
    private static final String TAG_TYPE_ORDRE_DU_JOUR = "typeOrdreDuJour";
    private int _nIdTypeOrdreDuJour;
    private String _strLibelle;
    private String _strLibelleLong;

    /**
     * retourne l'id du type de l'ordre du jour
     * @return id du type de l'ordre du jour
     */
    public int getIdTypeOrdreDuJour(  )
    {
        return _nIdTypeOrdreDuJour;
    }

    /**
     * initialise l'id du type de l'ordre du jour
     * @param idTypeOrdreDuJour id du type de l'ordre du jour
     */
    public void setIdTypeOrdreDuJour( int idTypeOrdreDuJour )
    {
        _nIdTypeOrdreDuJour = idTypeOrdreDuJour;
    }

    /**
     * retourne le libelle du type de l'ordre du jour
     * @return le libelle du type de l'ordre du jour
     */
    public String getLibelle(  )
    {
        return _strLibelle;
    }

    /**
     * initialise le libelle du type de l'ordre du jour
     * @param libelle type de l'ordre du jour
     */
    public void setLibelle( String libelle )
    {
        _strLibelle = libelle;
    }

    /**
     * retourne le libelle du type de l'ordre du jour
     * @return le libelle du type de l'ordre du jour
     */
    public String getLibelleLong(  )
    {
        return _strLibelleLong;
    }

    /**
     * initialise le libelle du type de l'ordre du jour
     * @param libelle type de l'ordre du jour
     */
    public void setLibelleLong( String libelle )
    {
        _strLibelleLong = libelle;
    }

    /**
     * Génèration du XML d'un objet
     * @param request la requête
     * @return Le code XML représentant un ordre du jour
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );

        attributes.put( TAG_ID, _nIdTypeOrdreDuJour );
        XmlUtil.beginElement( buffer, TAG_TYPE_ORDRE_DU_JOUR, attributes );

        if ( _strLibelle != null )
        {
            OdsUtils.addElement( buffer, TAG_LIBELLE, _strLibelle );
        }

        if ( _strLibelleLong != null )
        {
            OdsUtils.addElement( buffer, TAG_LIBELLE_LONG, _strLibelleLong );
        }

        XmlUtil.endElement( buffer, TAG_TYPE_ORDRE_DU_JOUR );

        return buffer.toString(  );
    }
}
