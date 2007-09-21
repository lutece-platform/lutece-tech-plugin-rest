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

import java.sql.Timestamp;


/**
 *
 * filtre sur les voeux et amendements
 *
 */
public class OrdreDuJourFilter
{
    public static final String ALL_STRING = "all";
    public static final int ALL_INT = -1;
    private int _nIdFormationConseil = ALL_INT;
    private int _nIdCommission = ALL_INT;
    private int _nIdSeance = ALL_INT;
    private int _nIdPublie = ALL_INT;
    private int _nIdType = ALL_INT;
    private int _nIdSauvegarde = ALL_INT;
    private int _nIdTourniquet = ALL_INT;
    private Timestamp _tsDatePublication;

    /**
     * @return _tsDatePublication Timestamp
     */
    public Timestamp getDatePublication(  )
    {
        return _tsDatePublication;
    }

    /**
     * @param tsDatePubliocation _tsDatePublication to set
     */
    public void setDatePublication( Timestamp tsDatePubliocation )
    {
        _tsDatePublication = tsDatePubliocation;
    }

    /**
     * @return boolean TRUE si _tsDatePublication est actif, FALSE sinon
     */
    public boolean containsDatePublicationCriteria(  )
    {
        return ( _tsDatePublication != null );
    }

    /**
     * retourne l'id de la formation conseil présent dans le filtre
     * @return id de la formation conseil présent dans le filtre
     */
    public int getIdFormationConseil(  )
    {
        return _nIdFormationConseil;
    }

    /**
     *  initialise l'id de la formation conseil à filtrer
     * @param idFormationConseil id de la formation conseil présent dans le filtre
     */
    public void setIdFormationConseil( int idFormationConseil )
    {
        _nIdFormationConseil = idFormationConseil;
    }

    /**
         * retourne true si le filtre contient l'id de la formation conseil  false sinon
         * @return retourne true si le filtre contient l'id de la formation conseil  false sinon
         */
    public boolean containsIdFormationConseilCriteria(  )
    {
        return ( _nIdFormationConseil != ALL_INT );
    }

    /**
     * retourne l'id de la commission présent dans le filtre
     * @return id de la commission présent dans le filtre
     */
    public int getIdCommission(  )
    {
        return _nIdCommission;
    }

    /**
     *  initialise l'id de la commission à filtrer
     * @param idCommission id de la commission présent dans le filtre
     */
    public void setIdCommission( int idCommission )
    {
        _nIdCommission = idCommission;
    }

    /**
         * retourne true si le filtre contient l'id de la commission  false sinon
         * @return retourne true si le filtre contient l'id de la commission  false sinon
         */
    public boolean containsIdCommissionCriteria(  )
    {
        return ( _nIdCommission != ALL_INT );
    }

    /**
     * retourne l'id publie présent dans le filtre (0 ou 1)
     * @return l'id publie présent dans le filtre (0 ou 1)
     */
    public int getIdPublie(  )
    {
        return _nIdPublie;
    }

    /**
     * initialise l'id publie à filtrer (0 ou 1)
     * @param idPublie l'id publie à  filtrer (0 ou 1)
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

    /**
     * retourne  l'id du type de l'odj
     * @return l'id du type de l'odj
     */
    public int getIdType(  )
    {
        return _nIdType;
    }

    /**
     *initialise le type de l'odj à filtrer
     * @param idType l'id du type de l'odj
     */
    public void setIdType( int idType )
    {
        _nIdType = idType;
    }

    /**
         * retourne true si le filtre contient le critere type false sinon
         * @return retourne true si le filtre contient le critere Type false sinon
         */
    public boolean containsIdTypeCriteria(  )
    {
        return ( _nIdType != ALL_INT );
    }

    /**
     * retourne l'id de sauvegarde  de l'odj
     * @return le  sauvegarde  de l'odj
     */
    public int getIdSauvegarde(  )
    {
        return _nIdSauvegarde;
    }

    /**
     *initialise le  sauvegarde  de l'odj à filtrer
     *@param idSauvegarde l'id  de  sauvegarde  de l'odj
     */
    public void setIdSauvegarde( int idSauvegarde )
    {
        _nIdSauvegarde = idSauvegarde;
    }

    /**
         * retourne true si le filtre contient le critere  sauvegarde  false sinon
         * @return retourne si le filtre contient le critere  sauvegarde false sinon
         */
    public boolean containsIdSauvegardeCriteria(  )
    {
        return ( _nIdSauvegarde != ALL_INT );
    }

    /**
     * retourne 1 si les odjs filtrés doivent etre en mode tourniquet
     *                         0 si les odjs filtrés ne doivent pas  etre en mode tourniquet
     *                         -1 si le mode tourniquet n'est pas a prendre en compte dans le filtre
     * @return le  sauvegarde  de l'odj
     */
    public int getIdTourniquet(  )
    {
        return _nIdTourniquet;
    }

    /**
     *
     *@param idTourniquet initialisé  a  1 si les odjs filtrés doivent etre en mode tourniquet
     *                                                                            0  si les odjs filtrés ne doivent pas  etre en mode tourniquet
     *                                                                         -1 si le mode tourniquet n'est pas a prendre en compte dans le filtre
     */
    public void setIdTourniquet( int idTourniquet )
    {
        _nIdTourniquet = idTourniquet;
    }

    /**
         * retourne true si le filtre contient le critere  tourniquet  false sinon
         * @return retourne si le filtre contient le critere  tourniquet false sinon
         */
    public boolean containsIdTourniquetCriteria(  )
    {
        return ( _nIdTourniquet != ALL_INT );
    }
}
