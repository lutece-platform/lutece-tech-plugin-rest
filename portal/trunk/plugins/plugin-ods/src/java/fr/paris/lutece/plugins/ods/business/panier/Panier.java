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
package fr.paris.lutece.plugins.ods.business.panier;

import org.apache.commons.codec.binary.Base64;


/**
 * Objet Panier, correspond à un enregistrement dans la table ods_panier
 */
public class Panier
{
    private int _nIdElementPanier;
    private int _nIdDocument;
    private String _strIdUtilisateur;
    private java.sql.Timestamp _dateAjout;

    /**
     * @return the _dateAjout
     */
    public java.sql.Timestamp getDateAjout(  )
    {
        return _dateAjout;
    }

    /**
     * @param ajout the _dateAjout to set
     */
    public void setDateAjout( java.sql.Timestamp ajout )
    {
        _dateAjout = ajout;
    }

    /**
     * @return the _nIdDocument
     */
    public int getIdDocument(  )
    {
        return _nIdDocument;
    }

    /**
     * @param idDocument the _nIdDocument to set
     */
    public void setIdDocument( int idDocument )
    {
        _nIdDocument = idDocument;
    }

    /**
     * @return the _nIdElementPanier
     */
    public int getIdElementPanier(  )
    {
        return _nIdElementPanier;
    }

    /**
     * @param idElementPanier the _nIdElementPanier to set
     */
    public void setIdElementPanier( int idElementPanier )
    {
        _nIdElementPanier = idElementPanier;
    }

    /**
     * @return the _nIdUtilisateur
     */
    public String getIdUtilisateur(  )
    {
        return _strIdUtilisateur;
    }

    /**
     * @param idUtilisateur the _nIdUtilisateur to set
     */
    public void setIdUtilisateur( String idUtilisateur )
    {
        _strIdUtilisateur = idUtilisateur;
    }

    /**
     * Retourne un id unique codé en Base64 et composé de : idElementPanier;dateAjout
     * @return String id unique codé en Base64 et composé de : idElementPanier;dateAjout
     */
    public String getIdToDownload(  )
    {
        try
        {
            return new String( Base64.encodeBase64( new String( _nIdElementPanier + ";" + _dateAjout ).getBytes(  ) ) );
        }
        catch ( Exception e )
        {
            return _nIdElementPanier + ";" + _dateAjout;
        }
    }
}
