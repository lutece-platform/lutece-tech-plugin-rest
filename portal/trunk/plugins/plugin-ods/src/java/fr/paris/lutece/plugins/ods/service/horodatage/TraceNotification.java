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
 * TraceNotification
 *
 */
public class TraceNotification extends Trace
{
    private String _strObjet;
    private String _strContenu;
    private String _strIdUtilisateur;
    private Timestamp _dateEnvoi;
    private String _strDestinataires;

    /**
     * @return the _strContenu
     */
    public String getContenu(  )
    {
        return _strContenu;
    }

    /**
     * @param contenu the _strContenu to set
     */
    public void setContenu( String contenu )
    {
        _strContenu = contenu;
    }

    /**
     * @return the _strObjet
     */
    public String getObjet(  )
    {
        return _strObjet;
    }

    /**
     * @param objet the _strObjet to set
     */
    public void setObjet( String objet )
    {
        _strObjet = objet;
    }

    /**
     * @return the _dateEnvoi
     */
    public Timestamp getDateEnvoi(  )
    {
        return _dateEnvoi;
    }

    /**
     * @param envoi the _dateEnvoi to set
     */
    public void setDateEnvoi( Timestamp envoi )
    {
        _dateEnvoi = envoi;
    }

    /**
     * @return the _strDestinataires
     */
    public String getDestinataires(  )
    {
        return _strDestinataires;
    }

    /**
     * @param destinataires the _strDestinataires to set
     */
    public void setDestinataires( String destinataires )
    {
        _strDestinataires = destinataires;
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
}
