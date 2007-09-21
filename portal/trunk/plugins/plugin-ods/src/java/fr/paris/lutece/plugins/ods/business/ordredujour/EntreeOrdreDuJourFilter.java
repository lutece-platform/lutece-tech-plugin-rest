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


/**
 *
 * filtre sur les voeux et amendements
 *
 */
public class EntreeOrdreDuJourFilter
{
    public static final String ALL_STRING = "all";
    public static final int ALL_INT = -1;
    private int _nIdOrdreDuJour = ALL_INT;
    private int _nIdPdd = ALL_INT;
    private int _nIdSeance = ALL_INT;
    private int _nIdPublie = ALL_INT;

    /**
     * retourne l'id de l'ordre du jour présent dans le filtre
     * @return id de l'ordre du jour présent dans le filtre
     */
    public int getIdOrdreDuJour(  )
    {
        return _nIdOrdreDuJour;
    }

    /**
     * initialise l'id de l'ordre du jour à filtrer
     * @param idOrdreDuJour id de l'ordre du jour présent dans le filtre
     */
    public void setIdOrdreDuJour( int idOrdreDuJour )
    {
        _nIdOrdreDuJour = idOrdreDuJour;
    }

    /**
     * retourne true si le filtre contient l'id de l'ordre du jour  false sinon
     * @return retourne true si le filtre contient l'id de l'ordre du jour, false sinon
     */
    public boolean containsIdOrdreDuJourCriteria(  )
    {
        return ( _nIdOrdreDuJour != ALL_INT );
    }

    /**
     * retourne l'id du pdd présent dans le filtre
     * @return id du pdd présent dans le filtre
     */
    public int getIdPdd(  )
    {
        return _nIdPdd;
    }

    /**
     * initialise l'id du pdd à filtrer
     * @param idPdd id du pdd présent dans le filtre
     */
    public void setIdPdd( int idPdd )
    {
        _nIdPdd = idPdd;
    }

    /**
     * retourne true si le filtre contient l'id du pdd  false sinon
     * @return retourne true si le filtre contient l'id du pdd false sinon
     */
    public boolean containsIdPddCriteria(  )
    {
        return ( _nIdPdd != ALL_INT );
    }

    /**
     * retourne l'id publie présent dans le filtre (0=FAUX ou 1=VRAI)
     * @return l'id publie présent dans le filtre (0=FAUX ou 1=VRAI)
     */
    public int getIdPublie(  )
    {
        return _nIdPublie;
    }

    /**
     * initialise l'id publie à filtrer (0=FAUX ou 1=VRAI)
     * @param idPublie l'id publie à  filtrer (0=FAUX ou 1=VRAI)
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
     * retourne  l'id de la seance présent dans le filtre
     * @return l'id de la seance présent dans le filtre
     */
    public int getIdSeance(  )
    {
        return _nIdSeance;
    }

    /**
     * initialise l'id de la seance à filtrer
     * @param idSeance l'id de la seance  à filtrer
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
