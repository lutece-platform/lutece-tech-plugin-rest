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
package fr.paris.lutece.plugins.ods.service.horodatage;

import java.sql.Timestamp;


/**
 *
 * TracePublication
 *
 */
public class TracePublication extends Trace
{
    private Timestamp _dateSeance;
    private HorodatageFamilleDocumentEnum _familleDoc;
    private int _nIdDbOds;
    private String _strReference;
    private Timestamp _datePublication;
    private String _strIntitule;
    private int _nVersion;
    private HorodatageActionEnum _action;

    /**
     * @return the _datePublication
     */
    public Timestamp getDatePublication(  )
    {
        return _datePublication;
    }

    /**
     * @param publication the _datePublication to set
     */
    public void setDatePublication( Timestamp publication )
    {
        _datePublication = publication;
    }

    /**
     * @return the _dateSeance
     */
    public Timestamp getDateSeance(  )
    {
        return _dateSeance;
    }

    /**
     * @param seance the _dateSeance to set
     */
    public void setDateSeance( Timestamp seance )
    {
        _dateSeance = seance;
    }

    /**
     * @return the _action
     */
    public HorodatageActionEnum getAction(  )
    {
        return _action;
    }

    /**
     * @param action the _action to set
     */
    public void setAction( HorodatageActionEnum action )
    {
        _action = action;
    }

    /**
     * @return the _nIdDbOds
     */
    public int getIdDbOds(  )
    {
        return _nIdDbOds;
    }

    /**
     * @param idDbOds the _nIdDbOds to set
     */
    public void setIdDbOds( int idDbOds )
    {
        _nIdDbOds = idDbOds;
    }

    /**
     * @return the _nVersion
     */
    public int getVersion(  )
    {
        return _nVersion;
    }

    /**
     * @param version the _nVersion to set
     */
    public void setVersion( int version )
    {
        _nVersion = version;
    }

    /**
     * @return the _famille_doc
     */
    public HorodatageFamilleDocumentEnum getFamilleDoc(  )
    {
        return _familleDoc;
    }

    /**
     * @param familleDoc the _famille_doc to set
     */
    public void setFamilleDoc( HorodatageFamilleDocumentEnum familleDoc )
    {
        _familleDoc = familleDoc;
    }

    /**
     * @return the _strIntitule
     */
    public String getIntitule(  )
    {
        return _strIntitule;
    }

    /**
     * @param intitule the _strIntitule to set
     */
    public void setIntitule( String intitule )
    {
        _strIntitule = intitule;
    }

    /**
     * @return the _strReference
     */
    public String getReference(  )
    {
        return _strReference;
    }

    /**
     * @param reference the _strReference to set
     */
    public void setReference( String reference )
    {
        _strReference = reference;
    }
}
