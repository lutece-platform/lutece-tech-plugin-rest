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
package fr.paris.lutece.plugins.ods.business.ordredujour;

import fr.paris.lutece.plugins.ods.business.seance.Seance;


/**
 * NatureAffaire, pour l'instant inutilisé
 */
public class NatureAffaire
{
    private int _nIdNature;
    private Seance _seance;
    private int _nNumNature;
    private String _strLibelleNature;

    /**
     * getter de IdNature
     * @return _nIdNature
     */
    public int getIdNature(  )
    {
        return _nIdNature;
    }

    /**
     * setter de IdNature
     * @param idNature l'id à affecter à _nIdNature
     */
    public void setIdNature( int idNature )
    {
        _nIdNature = idNature;
    }

    /**
     * getter de NumNature
     * @return _nNumNature
     */
    public int getNumNature(  )
    {
        return _nNumNature;
    }

    /**
     * setter de NumNature
     * @param numNature le numero de nature à affecter à _nNumNature
     */
    public void setNumNature( int numNature )
    {
        _nNumNature = numNature;
    }

    /**
     * getter de Seance
     * @return _seance
     */
    public Seance getSeance(  )
    {
        return _seance;
    }

    /**
     * setter de la seance
     * @param seance la seance à affecter à _seance
     */
    public void setSeance( Seance seance )
    {
        _seance = seance;
    }

    /**
     * getter de LibelleNature
     * @return _strLibelleNature
     */
    public String getLibelleNature(  )
    {
        return _strLibelleNature;
    }

    /**
     * setter de LibelleNature
     * @param libelleNature le libelle de la nature de l'odj
     */
    public void setLibelleNature( String libelleNature )
    {
        _strLibelleNature = libelleNature;
    }
}
