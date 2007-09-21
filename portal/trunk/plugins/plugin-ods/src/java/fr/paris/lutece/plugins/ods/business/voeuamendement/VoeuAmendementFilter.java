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
package fr.paris.lutece.plugins.ods.business.voeuamendement;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * filtre sur les voeux et amendements
 *
 */
public class VoeuAmendementFilter
{
    public static final String ALL_STRING = "all";
    public static final int ALL_INT = -1;
    public static final String ORDER_CODE_FASCICULE = "fa.code_fascicule";
    public static final String ORDER_NUMERO_ORDRE = "fa.numero_ordre";
    public static final String ORDER_NOM_FASCICULE = "fa.nom_fascicule";
    public static final String ORDER_REFERENCE_VA = "reference_va";
    public static final String ORDER_REFERENCE_VA_COMPLETE = "reference_tri";
    public static final String ORDER_DATE_PUBLICATION = "va.date_publication";
    private int _nIdFormationConseil = ALL_INT;
    private int _nIdSeance = ALL_INT;
    private int _nIdPublie = ALL_INT;
    private String _strType = ALL_STRING;
    private int _nIdCommission = ALL_INT;
    private int _nIdElu = ALL_INT;
    private int _nIdParent = ALL_INT;
    private int _nIdStatut = ALL_INT;
    private List<String> _orderByList = new ArrayList<String>(  );
    private int _nTypeOrdreDuJour = ALL_INT;
    private int _nIdOrdreDuJour = ALL_INT;
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
     * retourne le type du va(Amendement=A,Lettre rectificative=LR,Voeu rattache=V,Voeu non rattache=VNR)
     * @return type du va(Amendement=A,Lettre rectificative=LR,Voeu rattache=V,Voeu non rattache=VNR)
     */
    public String getType(  )
    {
        return _strType;
    }

    /**
     *initialise le type du va (Amendement=A,Lettre rectificative=LR,Voeu rattache=V,Voeu non rattache=VNR) à filtrer
     * @param strType le type du va (Amendement=A,Lettre rectificative=LR,Voeu rattache=V,Voeu non rattache=VNR)
     */
    public void setType( String strType )
    {
        _strType = strType;
    }

    /**
         * retourne true si le filtre contient le critere Type false sinon
         * @return retourne si le filtre contient le critere Type false sinon
         */
    public boolean containsTypeCriteria(  )
    {
        return ( !_strType.equals( ALL_STRING ) );
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
     * @param nIdFormationConseil id de la formation conseil présent dans le filtre
     */
    public void setIdFormationConseil( int nIdFormationConseil )
    {
        _nIdFormationConseil = nIdFormationConseil;
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
     * retourne l'id publie présent dans le filtre (0 ou 1)
     * @return l'id publie présent dans le filtre (0 ou 1)
     */
    public int getIdPublie(  )
    {
        return _nIdPublie;
    }

    /**
     * initialise l'id publie à filtrer (0 ou 1)
     * @param nIdPublie l'id publie à  filtrer (0 ou 1)
     */
    public void setIdPublie( int nIdPublie )
    {
        _nIdPublie = nIdPublie;
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
     * @param nIdSeance l'id de la seance  à filtrer
     */
    public void setIdSeance( int nIdSeance )
    {
        _nIdSeance = nIdSeance;
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
     * Retourne l'id de la commission du voeu ou amendement
     * @return l'id de la commission du voeu ou amendement
     */
    public int getIdCommission(  )
    {
        return _nIdCommission;
    }

    /**
     * Fixe l'id de la commission du voeu ou amendement
     * @param nIdCommission l'id de la commission
     */
    public void setIdCommission( int nIdCommission )
    {
        _nIdCommission = nIdCommission;
    }

    /**
     * Indique si le filtre sur la commission est positionné
     * @return <b>true</b> si le filtre sur la commission est activé, <b>false</b> sinon
     */
    public boolean containsCommissionCriteria(  )
    {
        return ( _nIdCommission != ALL_INT );
    }

    /**
    * retourne la liste des criteres de tries
    * @return retourne la liste des criteres de tries
    */
    public List<String> getOrderByList(  )
    {
        return _orderByList;
    }

    /**
     * initialise la liste des criteres de tries
     * @param orderByList  liste des criteres de tries
     */
    public void setOrderByList( List<String> orderByList )
    {
        _orderByList = orderByList;
    }

    /**
     * ajoute un critere de trie
     * @param strOrder critere de trie
     */
    public void addInOrderBy( String strOrder )
    {
        _orderByList.add( strOrder );
    }

    /**
         * retourne true si le filtre contient des criters de tries sinon false
         * @return retourne true si le filtre contient des criters de tries sinon false
         */
    public boolean containsOrderByCriteria(  )
    {
        return ( !_orderByList.isEmpty(  ) );
    }

    /**
     * retourne l'id de l'elu dépositaire à filtrer
     * @return l'id de l'elu dépositaire à filtrer
     */
    public int getIdElu(  )
    {
        return _nIdElu;
    }

    /**
     * fixe l'id de l'élu dépositaire à filtrer
     * @param nIdElu l'id de l'elu dépositaire à filtrer
     */
    public void setIdElu( int nIdElu )
    {
        _nIdElu = nIdElu;
    }

    /**
     * indique si le filtre sur l'élu dépositaire est actif
     * @return <b>true</b> si le filtre sur élu est activé, <b>false</b> sinon
     */
    public boolean containsIdEluCriteria(  )
    {
        return ( _nIdElu != ALL_INT );
    }

    /**
     * retourne l'id du parent à filtrer
     * @return l'id du parent à filtrer
     */
    public int getIdParent(  )
    {
        return _nIdParent;
    }

    /**
     * fixe l'id du parent à filtrer
     * @param nIdParent l'id du parent à filtrer
     */
    public void setIdParent( int nIdParent )
    {
        _nIdParent = nIdParent;
    }

    /**
     * indique si le filtre sur le parent est actif
     * @return <b>true</b> si le filtre sur parent est activé, <b>false</b> sinon
     */
    public boolean containsIdParentCriteria(  )
    {
        return ( _nIdParent != ALL_INT );
    }

    /**
     * retourne l'id du statut a filtrer
     * @return l'id du statut a filtrer
     */
    public int getIdStatut(  )
    {
        return _nIdStatut;
    }

    /**
     * fixe l'id du statut a filtrer
     * @param nIdStatut l'id du statut a filtrer
     */
    public void setIdStatut( int nIdStatut )
    {
        _nIdStatut = nIdStatut;
    }

    /**
     * indique si le filtre sur le statut est actif
     * @return <b>true</b> si le filtre sur statut est activé, <b>false</b> sinon
     */
    public boolean containsIdStatutCriteria(  )
    {
        return ( _nIdStatut != ALL_INT );
    }

    /**
     * @return _nTypeOrdreDuJour
     */
    public int getTypeOrdreDuJour(  )
    {
        return _nTypeOrdreDuJour;
    }

    /**
     * @param typeOrdreDuJour _nTypeOrdreDuJour to set
     */
    public void setTypeOrdreDuJour( int typeOrdreDuJour )
    {
        _nTypeOrdreDuJour = typeOrdreDuJour;
    }

    /**
     * @return boolean TRUE si _nTypeOrdreDuJour est actif, FALSE sinon
     */
    public boolean containsTypeOrdreDuJourCriteria(  )
    {
        return ( _nTypeOrdreDuJour != ALL_INT );
    }

    /**
     * Retourne l'id d'ODJ à filtrer
     * @return l'id d'ODJ à filtrer
     */
    public int getIdOrdreDuJour(  )
    {
        return _nIdOrdreDuJour;
    }

    /**
     * Fixe l'id d'ODJ à filtrer
     * @param nIdOrdreDuJour l'id d'ODJ à filtrer
     */
    public void setIdOrdreDuJour( int nIdOrdreDuJour )
    {
        _nIdOrdreDuJour = nIdOrdreDuJour;
    }

    /**
     * Indique si l'id d'ODJ est fixé
     * @return <b>true</b> si l'id d'ODJ est fixé, <b>false</b> sinon
     */
    public boolean containsIdOrdreDuJourCriteria(  )
    {
        return ( _nIdOrdreDuJour != ALL_INT );
    }
}
