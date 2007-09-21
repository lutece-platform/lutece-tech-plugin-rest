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
package fr.paris.lutece.plugins.ods.business.pdd;

import fr.paris.lutece.plugins.ods.business.direction.Direction;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * Représente une direction co-Emetrice
 *
 */
public class DirectionCoEmetrice
{
    private static final String CODE_PROJET = "codeProjet";
    private static final String DIRECTION_CO_EMETRICE = "directionCoEmetrice";
    private static final String ID_PDD = "idPdd";
    private int _nIdPDD;
    private Direction _direction;
    private String _strCodeProjet;

    /**
     * @return the _strCodeProjet
     */
    public String getCodeProjet(  )
    {
        return _strCodeProjet;
    }

    /**
     * @param strCodeProjet the _strCodeProjet to set
     */
    public void setCodeProjet( String strCodeProjet )
    {
        _strCodeProjet = strCodeProjet;
    }

    /**
     * @return the _direction
     */
    public Direction getDirection(  )
    {
        return this._direction;
    }

    /**
     * @param direction the _direction to set
     */
    public void setDirection( Direction direction )
    {
        this._direction = direction;
    }

    /**
     * @return the _nIdPDD
     */
    public int getIdPDD(  )
    {
        return _nIdPDD;
    }

    /**
     * @param nIdPDD the _nIdPDD to set
     */
    public void setIdPDD( int nIdPDD )
    {
        _nIdPDD = nIdPDD;
    }

    /**
     * renvoie le code XML correspondant à l'objet
     * @param request la requete http
     * @return le code XML
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );

        XmlUtil.beginElement( buffer, DIRECTION_CO_EMETRICE );

        if ( _strCodeProjet != null )
        {
            OdsUtils.addElement( buffer, ID_PDD, _nIdPDD );
        }

        if ( _strCodeProjet != null )
        {
            OdsUtils.addElement( buffer, CODE_PROJET, _strCodeProjet );
        }

        if ( _direction != null )
        {
            buffer.append( _direction.getXml( request ) );
        }

        XmlUtil.endElement( buffer, DIRECTION_CO_EMETRICE );

        return buffer.toString(  );
    }
}
