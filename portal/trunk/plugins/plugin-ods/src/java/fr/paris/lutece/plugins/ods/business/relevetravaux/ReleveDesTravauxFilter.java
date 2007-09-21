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
package fr.paris.lutece.plugins.ods.business.relevetravaux;


/**
 * filtre sur les relev�s de travaux
 */
public class ReleveDesTravauxFilter
{
    public static final String ALL_STRING = "all";
    public static final int ALL_INT = -1;
    private int _nIdPublie = ALL_INT;
    private int _nIdSeance = ALL_INT;
    private int _nIdCommission = ALL_INT;

    /**
     * retourne l'id de la commission contenue dans le filtre
     * @return l'id de la commission � filtrer
     */
    public int getIdCommission(  )
    {
        return _nIdCommission;
    }

    /**
     * initialise l'id de la commission � filtrer
     * @param commission l'id de la commission � filtrer
     */
    public void setIdCommission( int commission )
    {
        _nIdCommission = commission;
    }

    /**
     * retourne true si le filtre contient l'id de la commission false sinon
     * @return retourne true si le filtre contient l'id de la commission false sinon
     */
    public boolean containsIdCommissionCriteria(  )
    {
        return ( _nIdCommission != ALL_INT );
    }

    /**
     * retourne l'id publie pr�sent dans le filtre (0 ou 1)
     * @return l'id publie pr�sent dans le filtre (0 ou 1)
     */
    public int getIdPublie(  )
    {
        return _nIdPublie;
    }

    /**
     * initialise l'id publie � filtrer (0 ou 1)
     * @param idPublie l'id publie �  filtrer (0 ou 1)
     */
    public void setIdPublie( int idPublie )
    {
        _nIdPublie = idPublie;
    }

    /**
         * retourne true si le filtre contient l'id publie false sinon
         * @return retourne true si le filtre contient l'id publie false sinon
         */
    public boolean containsIdPublieCriteria(  )
    {
        return ( _nIdPublie != ALL_INT );
    }

    /**
     * retourne  l'id de la seance pr�sent dans le filtre
     * @return l'id de la seance pr�sent dans le filtre
     */
    public int getIdSeance(  )
    {
        return _nIdSeance;
    }

    /**
     * initialise l'id de la seance � filtrer
     * @param idSeance l'id de la seance  � filtrer
     */
    public void setIdSeance( int idSeance )
    {
        _nIdSeance = idSeance;
    }

    /**
     * retourne true si le filtre contient l'id de la seance false sinon
     * @return retourne true si le filtre contient l'id de la seance false sinon
     */
    public boolean containsIdSeanceCriteria(  )
    {
        return ( _nIdSeance != ALL_INT );
    }
}
