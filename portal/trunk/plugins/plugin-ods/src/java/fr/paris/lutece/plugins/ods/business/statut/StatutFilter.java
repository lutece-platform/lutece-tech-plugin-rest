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
package fr.paris.lutece.plugins.ods.business.statut;


/**
 * StatutFilter
 */
public class StatutFilter
{
    private boolean _bPourPDD;
    private boolean _bPourVoeu;
    private boolean _bPourAmendement;

    /**
     * Retourne si le statut concerne les PDDs.
     * @return TRUE ou FALSE
     */
    public boolean getPourPDD(  )
    {
        return _bPourPDD;
    }

    /**
     * Affecte si le statut concerne les PDDs ou pas
     * @param bPourPDD TRUE ou FALSE
     */
    public void setPourPDD( boolean bPourPDD )
    {
        _bPourPDD = bPourPDD;
    }

    /**
     * retourne TRUE si le filtre contient une indication sur le PDD, FALSE sinon
     * @return retourne TRUE si le filtre contient une indication sur le PDD, FALSE sinon
     */
    public boolean containsPourPDDCriteria(  )
    {
        return _bPourPDD;
    }

    /**
     * Retourne si le statut concerne les Voeux.
     * @return TRUE ou FALSE
     */
    public boolean getPourVoeux(  )
    {
        return _bPourVoeu;
    }

    /**
     * Affecte si le statut concerne les voeux ou pas
     * @param bPourVoeu TRUE ou FALSE
     */
    public void setPourVoeu( boolean bPourVoeu )
    {
        _bPourVoeu = bPourVoeu;
    }

    /**
     * retourne TRUE si le filtre contient une indication sur le voeu, FALSE sinon
     * @return retourne TRUE si le filtre contient une indication sur le voeu, FALSE sinon
     */
    public boolean containsPourVoeuCriteria(  )
    {
        return _bPourVoeu;
    }

    /**
     * Retourne si le statut concerne les amendements.
     * @return TRUE ou FALSE
     */
    public boolean getPourAmendement(  )
    {
        return _bPourAmendement;
    }

    /**
     * Affecte si le statut concerne les amendements ou pas
     * @param bPourAmendement TRUE ou FALSE
     */
    public void setPourAmendement( boolean bPourAmendement )
    {
        _bPourAmendement = bPourAmendement;
    }

    /**
     * retourne TRUE si le filtre contient une indication sur l'amendement, FALSE sinon
     * @return retourne TRUE si le filtre contient une indication sur l'amendement, FALSE sinon
     */
    public boolean containsPourAmendementCriteria(  )
    {
        return _bPourAmendement;
    }
}
