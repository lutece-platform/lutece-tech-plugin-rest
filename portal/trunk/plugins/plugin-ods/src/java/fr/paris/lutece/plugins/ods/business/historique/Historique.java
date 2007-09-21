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
package fr.paris.lutece.plugins.ods.business.historique;

import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.sql.Timestamp;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * Responsable de représenté l'historique
 *
 */
public class Historique
{
    public static final int NULL = -1;
    private static final String TAG_ID = "id";
    private static final String TAG_ID_VA = "idVa";
    private static final String TAG_ID_PDD = "idPdd";
    private static final String TAG_ID_DOCUMENT = "idDocument";
    private static final String TAG_DATE_PUBLICATION = "datePublication";
    private static final String TAG_VERSION = "version";
    private static final String TAG_HISTORIQUE = "historique";
    private int _nId;
    private int _nVersion;
    private int _nIdDocument = NULL;
    private int _nIdPDD = NULL;
    private int _nIdVa = NULL;
    private Timestamp _datePublication;

    /**
     *
     * @return Timestamp _datePublication
     */
    public Timestamp getDatePublication(  )
    {
        return _datePublication;
    }

    /**
     *
     * @param datePublication Timestamp
     */
    public void setDatePublication( Timestamp datePublication )
    {
        this._datePublication = datePublication;
    }

    /**
     *
     * @return _nId int
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     *
     * @param id _nId
     */
    public void setId( int id )
    {
        this._nId = id;
    }

    /**
     *
     * @return int _nVersion
     */
    public int getVersion(  )
    {
        return _nVersion;
    }

    /**
     *
     * @param version int
     */
    public void setVersion( int version )
    {
        this._nVersion = version;
    }

    /**
     *
     * @return int _nIdDocument
     */
    public int getIdDocument(  )
    {
        return _nIdDocument;
    }

    /**
     *
     * @param idDocument int
     */
    public void setIdDocument( int idDocument )
    {
        this._nIdDocument = idDocument;
    }

    /**
     * @return the idPDD
     */
    public int getIdPDD(  )
    {
        return _nIdPDD;
    }

    /**
     * @param idPDD the _nIdPDD to set
     */
    public void setIdPDD( int idPDD )
    {
        this._nIdPDD = idPDD;
    }

    /**
     * @return the _nIdVa
     */
    public int getIdVa(  )
    {
        return _nIdVa;
    }

    /**
     * @param idVa the _nIdVa to set
     */
    public void setIdVa( int idVa )
    {
        this._nIdVa = idVa;
    }

    /**
     * Génèration du XML d'un objet
     * @param request la requête
     * @return Le code XML représentant un historique
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );

        attributes.put( TAG_ID, _nId );
        XmlUtil.beginElement( buffer, TAG_HISTORIQUE, attributes );

        if ( _nVersion >= 0 )
        {
            OdsUtils.addElement( buffer, TAG_VERSION, _nVersion );
        }

        if ( _datePublication != null )
        {
            OdsUtils.addElement( buffer, TAG_DATE_PUBLICATION, _datePublication.toString(  ) );
        }

        if ( _nIdDocument >= 0 )
        {
            OdsUtils.addElement( buffer, TAG_ID_DOCUMENT, _nIdDocument );
        }

        if ( _nIdPDD >= 0 )
        {
            OdsUtils.addElement( buffer, TAG_ID_PDD, _nIdPDD );
        }

        if ( _nIdVa >= 0 )
        {
            OdsUtils.addElement( buffer, TAG_ID_VA, _nIdVa );
        }

        XmlUtil.endElement( buffer, TAG_HISTORIQUE );

        return buffer.toString(  );
    }
}
