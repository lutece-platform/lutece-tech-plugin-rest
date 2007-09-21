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
package fr.paris.lutece.plugins.ods.business.formationconseil;

import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * Objet FormationConseil, correspond à une entrée de la table ods_formation_conseil
 */
public class FormationConseil
{
    private static final String TAG_LIBELLE = "libelle";
    private static final String TAG_CODE = "code";
    private static final String TAG_ID = "id";
    private static final String TAG_FORMATION_CONSEIL = "formationConseil";
    private int _nIdFormationConseil = -1;
    private String _strCode;
    private String _strLibelle;

    /**
     * Retourne le code de la formation de conseil
     * @return le code de la formation de conseil
     */
    public String getCode(  )
    {
        return _strCode;
    }

    /**
     * Fixe le code de la formation de conseil
     * @param strCode le code de la formation de conseil
     */
    public void setCode( String strCode )
    {
        _strCode = strCode;
    }

    /**
     * Retourne l'identifiant de la formation de conseil
     * @return l'identifiant de la formation de conseil
     */
    public int getIdFormationConseil(  )
    {
        return _nIdFormationConseil;
    }

    /**
     * Fixe l'identifiant de la formation de conseil
     * @param nIdFormationConseil l'identifiant de la formation de conseil
     */
    public void setIdFormationConseil( int nIdFormationConseil )
    {
        _nIdFormationConseil = nIdFormationConseil;
    }

    /**
     * Retourne le libellé de la formation de conseil
     * @return le libellé de la formation de conseil
     */
    public String getLibelle(  )
    {
        return _strLibelle;
    }

    /**
     * Fixe le libellé de la formation de conseil
     * @param strLibelle le libellé de la formation de conseil
     */
    public void setLibelle( String strLibelle )
    {
        _strLibelle = strLibelle;
    }

    /**
     * Génèration du XML d'un objet
     * @param request la requête
     * @return Le code XML représentant une formation de conseil
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );

        attributes.put( TAG_ID, _nIdFormationConseil );
        XmlUtil.beginElement( buffer, TAG_FORMATION_CONSEIL, attributes );

        if ( _strCode != null )
        {
            OdsUtils.addElement( buffer, TAG_CODE, _strCode );
        }

        if ( _strLibelle != null )
        {
            OdsUtils.addElement( buffer, TAG_LIBELLE, _strLibelle );
        }

        XmlUtil.endElement( buffer, TAG_FORMATION_CONSEIL );

        return buffer.toString(  );
    }
}
