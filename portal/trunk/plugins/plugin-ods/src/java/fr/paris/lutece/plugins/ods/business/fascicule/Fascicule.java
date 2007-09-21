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
package fr.paris.lutece.plugins.ods.business.fascicule;

import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * Objet Fascicule, correspond à un enregistrement dans la base ods_fascicule
 */
public class Fascicule
{
    private static final String TAG_NOM_FASCICULE = "nomFascicule";
    private static final String TAG_NUMERO_ORDRE = "numeroOrdre";
    private static final String TAG_CODE_FASCICULE = "codeFascicule";
    private static final String TAG_ID = "id";
    private static final String TAG_FASCICULE = "fascicule";
    private int _nIdFascicule = -1;
    private Seance _seance;
    private String _strCodeFascicule;
    private int _nNumeroOrdre;
    private String _strNomFascicule;

    /**
     * Retuorne l'identifiant du fascicule.
     * @return l'identifiant du fascicule
     */
    public int getIdFascicule(  )
    {
        return _nIdFascicule;
    }

    /**
     * Fixe l'identifiant du fascicule.
     * @param nIdFascicule l'identifiant du facicule
     */
    public void setIdFascicule( int nIdFascicule )
    {
        _nIdFascicule = nIdFascicule;
    }

    /**
     * Retourne la séance associée au fascicule.
     * @return la séance associée au fascicule
     */
    public Seance getSeance(  )
    {
        return _seance;
    }

    /**
     * Fixe la séance associée au fascicule.
     * @param seance la séance associée au fascicule
     */
    public void setSeance( Seance seance )
    {
        _seance = seance;
    }

    /**
     * Retourne le code du fascicule.
     * @return le code du fascicule
     */
    public String getCodeFascicule(  )
    {
        return _strCodeFascicule;
    }

    /**
     * Fixe le code du fascicule.
     * @param strCodeFascicule le code du fascicule
     */
    public void setCodeFascicule( String strCodeFascicule )
    {
        _strCodeFascicule = strCodeFascicule;
    }

    /**
     * Retourne le numéro d'ordre du fascicule
     * @return le numéro d'ordre du fascicule
     */
    public int getNumeroOrdre(  )
    {
        return _nNumeroOrdre;
    }

    /**
     * Fixe le numéro d'ordre du fascicule
     * @param nNumeroOrdre le numéro d'ordre du fascicule
     */
    public void setNumeroOrdre( int nNumeroOrdre )
    {
        _nNumeroOrdre = nNumeroOrdre;
    }

    /**
     * Retourne le nom du fascicule.
     * @return le nom du fascicule
     */
    public String getNomFascicule(  )
    {
        return _strNomFascicule;
    }

    /**
     * Fixe le nom du fascicule.
     * @param strNomFascicule le nom du fascicule
     */
    public void setNomFascicule( String strNomFascicule )
    {
        _strNomFascicule = strNomFascicule;
    }

    /**
     * Génèration du XML d'un objet
     * @param request la requête
     * @return Le code XML représentant un fascicule
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );

        attributes.put( TAG_ID, _nIdFascicule );
        XmlUtil.beginElement( buffer, TAG_FASCICULE, attributes );

        if ( _strCodeFascicule != null )
        {
            OdsUtils.addElement( buffer, TAG_CODE_FASCICULE, _strCodeFascicule );
        }

        if ( _nNumeroOrdre >= 0 )
        {
            OdsUtils.addElement( buffer, TAG_NUMERO_ORDRE, _nNumeroOrdre );
        }

        if ( _strNomFascicule != null )
        {
            OdsUtils.addElement( buffer, TAG_NOM_FASCICULE, _strNomFascicule );
        }

        if ( _seance != null )
        {
            buffer.append( _seance.getXml( request ) );
        }

        XmlUtil.endElement( buffer, TAG_FASCICULE );

        return buffer.toString(  );
    }
}
