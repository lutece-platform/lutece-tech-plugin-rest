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
package fr.paris.lutece.plugins.ods.business.direction;

import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * Objet Direction, correspond à un enregistrement dans la table ods_direction
 */
public class Direction
{
    private static final String ACTIF = "actif";
    private static final String LIBELLE_LONG = "libelleLong";
    private static final String LIBELLE_COURT = "libelleCourt";
    private static final String CODE = "code";
    private static final String DIRECTION = "direction";
    private static final String ID_DIRECTION = "id";
    private int _nIdDirection;
    private String _strCode;
    private String _strLibelleCourt;
    private String _strLibelleLong;
    private boolean _bActif;

    /**
     * Retourne l'id de la direction.
     * @return id de la direction
     */
    public int getIdDirection(  )
    {
        return _nIdDirection;
    }

    /**
     * Fixe une nouvelle valeur à l'id de la direction.
     * @param nIdDirection le nouvel id de la direction
     */
    public void setIdDirection( int nIdDirection )
    {
        _nIdDirection = nIdDirection;
    }

    /**
     * Retourne le code de la direction.
     * @return le code de la direction
     */
    public String getCode(  )
    {
        return _strCode;
    }

    /**
     * Fixe une nouvelle valeur au code de la direction.
     * @param strCode le nouveau code de la direction
     */
    public void setCode( String strCode )
    {
        _strCode = strCode;
    }

    /**
     * Retourne l'intitulé court de la direction.
     * @return l'intitulé court de la direction
     */
    public String getLibelleCourt(  )
    {
        return _strLibelleCourt;
    }

    /**
     * Fixe une nouvelle valeur à l'intitulé court de la direction.
     * @param strLibelleCourt le nouvel intitulé court de la direction
     */
    public void setLibelleCourt( String strLibelleCourt )
    {
        _strLibelleCourt = strLibelleCourt;
    }

    /**
     * Retourne l'intitulé long de la direction.
     * @return l'intitulé long de la direction
     */
    public String getLibelleLong(  )
    {
        return _strLibelleLong;
    }

    /**
     * Fixe une nouvelle valeur à l'intitulé long de la direction.
     * @param strLibelleLong le nouvel intitulé long
     */
    public void setLibelleLong( String strLibelleLong )
    {
        _strLibelleLong = strLibelleLong;
    }

    /**
     * Indique si la direction est au statut actif ou non.
     * @return le statut de la direction
     */
    public boolean isActif(  )
    {
        return _bActif;
    }

    /**
     * Fixe le statut de la direction.
     * @param bActif le nouveua statut de la direction.
     */
    public void setActif( boolean bActif )
    {
        _bActif = bActif;
    }

    /**
     * Retourne la représentation XML de l'objet Direction
     * @param request la requête
     * @return la représentation XML de l'objet Direction
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );
        attributes.put( ID_DIRECTION, OdsConstants.CONSTANTE_CHAINE_VIDE + _nIdDirection );
        XmlUtil.beginElement( buffer, DIRECTION, attributes );

        if ( _strCode != null )
        {
            OdsUtils.addElement( buffer, CODE, _strCode );
        }

        if ( _strLibelleCourt != null )
        {
            OdsUtils.addElement( buffer, LIBELLE_COURT, _strLibelleCourt );
        }

        if ( _strLibelleLong != null )
        {
            OdsUtils.addElement( buffer, LIBELLE_LONG, _strLibelleLong );
        }

        OdsUtils.addElement( buffer, ACTIF, OdsConstants.CONSTANTE_CHAINE_VIDE + _bActif );
        XmlUtil.endElement( buffer, DIRECTION );

        return buffer.toString(  );
    }
}
