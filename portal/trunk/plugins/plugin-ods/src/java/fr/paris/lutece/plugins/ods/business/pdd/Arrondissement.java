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

import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * Représente un arrondissement
 *
 */
public class Arrondissement
{
    private static final String TAG_ID_PDD = "idPDD";
    private static final String TAG_NUMERO_ARRONDISSEMENT = "numeroArrondissement";
    private static final String TAG_ARRONDISSEMENT = "arrondissement";
    private static final String TAG_ID_ARRONDISSEMENT = "id";
    private int _nIdArrondissement;
    private int _nArrondissement;
    private int _nIdPDD;

    /**
     * @return _nArrondissement int
     */
    public int getArrondissement(  )
    {
        return _nArrondissement;
    }

    /**
     * @param nArrondissement to set
     */
    public void setArrondissement( int nArrondissement )
    {
        this._nArrondissement = nArrondissement;
    }

    /**
     * @return the _nIdArrondissement
     */
    public int getIdArrondissement(  )
    {
        return _nIdArrondissement;
    }

    /**
     * @param nIdArrondissement the nIdArrondissement to set
     */
    public void setIdArrondissement( int nIdArrondissement )
    {
        this._nIdArrondissement = nIdArrondissement;
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
        this._nIdPDD = nIdPDD;
    }

    /**
     * calcule le hashcode
     * @return le hashcode
     */
    public int hashCode(  )
    {
        return getIdArrondissement(  );
    }

    /**
     * vérifie l'égalité entre 2 objets
     * @param arrondissement l'objet arrondissement
     * @return TRUE si les 2 objets ont le meme id, FALSE sinon
     */
    public boolean equals( Object arrondissement )
    {
        if ( arrondissement instanceof Arrondissement )
        {
            return new Integer( getArrondissement(  ) ).equals( new Integer( 
                    ( (Arrondissement) arrondissement ).getArrondissement(  ) ) ) &&
            new Integer( getIdPDD(  ) ).equals( new Integer( ( (Arrondissement) arrondissement ).getIdPDD(  ) ) );
        }

        return false;
    }

    /**
     * Génération de XML
     * @param request la requete
     * @return le code XML
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );
        attributes.put( TAG_ID_ARRONDISSEMENT, _nIdArrondissement );
        XmlUtil.beginElement( buffer, TAG_ARRONDISSEMENT, attributes );

        if ( _nArrondissement != -1 )
        {
            OdsUtils.addElement( buffer, TAG_NUMERO_ARRONDISSEMENT,
                OdsConstants.CONSTANTE_CHAINE_VIDE + _nArrondissement );
        }

        if ( _nIdPDD != -1 )
        {
            OdsUtils.addElement( buffer, TAG_ID_PDD, OdsConstants.CONSTANTE_CHAINE_VIDE + _nIdPDD );
        }

        XmlUtil.endElement( buffer, TAG_ARRONDISSEMENT );

        return buffer.toString(  );
    }
}
