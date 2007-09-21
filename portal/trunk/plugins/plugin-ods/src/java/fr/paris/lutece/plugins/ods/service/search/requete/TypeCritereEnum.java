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


/**
 * TypeCritereEnum
 */
public enum TypeCritereEnum
{DATE_1( 1, "premiere_date" ),
    DATE_2( 2, "deuxieme_date" ),
    CHAMP_RECHERCHE( 3, "champ_recherche" ),
    TYPES_DOCUMENT( 4, "types_document" ),
    FORMATIONS_CONSEIL( 5, "formations_conseil" ),
    COMMISSIONS( 6, "commissions" ),
    RAPPORTEURS( 7, "rapporteurs" ),
    ARRONDISSEMENTS( 8, "arrondissements" ),
    DIRECTIONS( 9, "directions" ),
    CATEGORIES_DELIBERATION( 10, "categories_deliberation" ),
    GROUPES_DEPOSITAIRES( 11, "groupes_depositaires" ),
    ELUS_DEPOSITAIRES( 12, "elus_depositaires" );

    private int _nIdTypeCritere;
    private String _strNomCritere;

    /**
     * Constructeur
     * @param nIdTypeCritere l'id du critère
     * @param strNomCritere le nom du critère
     */
    TypeCritereEnum( int nIdTypeCritere, String strNomCritere )
    {
        _nIdTypeCritere = nIdTypeCritere;
        _strNomCritere = strNomCritere;
    }

    /**
     * Retourne l'id du critère
     * @return l'id du critère
     */
    public int getIdTypeCritere(  )
    {
        return _nIdTypeCritere;
    }

    /**
     * Fixe l'id du critère
     * @param nIdTypeCritere l'id du critère
     */
    public void setIdTypeRequete( int nIdTypeCritere )
    {
        _nIdTypeCritere = nIdTypeCritere;
    }

    /**
     * Retourne le nom du critère
     * @return le nom du critère
     */
    public String getNomCritere(  )
    {
        return _strNomCritere;
    }

    /**
     * Fixe le nom du critère
     * @param strNomCritere le nom du critère
     */
    public void setNomCritere( String strNomCritere )
    {
        _strNomCritere = strNomCritere;
    }

    /**
     * Retourne l'id du critère dont le nom est strNomCritere
     * @param strNomCritere le nom du critere recherché
     * @return l'id du critère dont le nom est strNomCritere
     */
    public static int getIdTypeCritereFromNom( String strNomCritere )
    {
        for ( TypeCritereEnum tce : TypeCritereEnum.values(  ) )
        {
            if ( strNomCritere.trim(  ).equals( tce.getNomCritere(  ) ) )
            {
                return tce.getIdTypeCritere(  );
            }
        }

        return 0;
    }
}
