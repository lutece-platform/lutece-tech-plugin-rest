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
package fr.paris.lutece.plugins.ods.service.search.requete;

import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;


/**
 * Cet objet correspond à un critère de recherche associé à une requête utilisateur
 */
public class Critere
{
    private int _nIdCritere = -1;
    private int _nIdRequete = -1;
    private int _nTypeCritere = -1;
    private String _strValeurCritere = OdsConstants.CONSTANTE_CHAINE_VIDE;

    /**
     * Retourne l'identifiant du critère de recherche
     * @return l'identifiant du critère de recherche
     */
    public int getIdCritere(  )
    {
        return _nIdCritere;
    }

    /**
     * Fixe l'identifiant du critère de recherche
     * @param nIdCritere l'identifiant du critère de recherche
     */
    public void setIdCritere( int nIdCritere )
    {
        _nIdCritere = nIdCritere;
    }

    /**
     * Retourne l'identifiant de la requête
     * @return l'identifiant de la requête
     */
    public int getIdRequete(  )
    {
        return _nIdRequete;
    }

    /**
     * Fixe l'identifiant de la requête
     * @param nIdRequete l'identifiant de la requête
     */
    public void setIdRequete( int nIdRequete )
    {
        _nIdRequete = nIdRequete;
    }

    /**
     * Retourne le type du critère de recherche
     * @return le type du critère de recherche
     */
    public int getTypeCritere(  )
    {
        return _nTypeCritere;
    }

    /**
     * Fixe le type du critère de recherche
     * @param nTypeCritere le type du critère de recherche
     */
    public void setTypeCritere( int nTypeCritere )
    {
        _nTypeCritere = nTypeCritere;
    }

    /**
     * Retourne la valeur du critère de recherche
     * @return la valeur du critère de recherche
     */
    public String getValeurCritere(  )
    {
        return _strValeurCritere;
    }

    /**
     * Fixe la valeur du critère de recherche
     * @param strValeurCritere la valeur du critère de recherche
     */
    public void setValeurCritere( String strValeurCritere )
    {
        _strValeurCritere = strValeurCritere;
    }
}
