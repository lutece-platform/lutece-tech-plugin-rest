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
 * StatutEnum
 */
public enum StatutEnum
{ADOPTE( 1 ),
    AMENDE( 2 ),
    NON_PRESENTE( 3 ),
    PAS_DE_VOTE( 4 ),
    REJETE( 5 ),
    RETIRE( 6 ),
    VOEU_TRANSFORME_EN_AMENDEMENT_ADOPTE( 7 ),
    AMENDEMENT_TRANSFORME_EN_VOEU_ADOPTE( 8 );

    private int _nId;

    /**
     * Constructeur.
     * @param nId l'identifiant
     */
    StatutEnum( int nId )
    {
        _nId = nId;
    }

    /**
     * Renvoie l'identifiant
     * @return l'identifiant
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Renvoie le statut
     * @return le statut
     */
    public Statut getStatutOnlyWidthId(  )
    {
        Statut st = new Statut(  );
        st.setIdStatut( _nId );

        return st;
    }
}
