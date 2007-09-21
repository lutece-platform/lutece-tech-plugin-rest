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
package fr.paris.lutece.plugins.ods.business.fichier;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;


/**
 * Un objet FichierFilter est utilisé pour retrouver les fichiers selon certains critères:
 * la séance, le type de documents, s'il est publié ou non, et le PDD auquel il est rattaché.
 */
public class FichierFilter
{
    public static final String ORDER_DATE_PUBLICATION = "fc.date_publication";
    public static final String ORDER_ID_COMMISSION = "fc.id_commission";
    public static final String ALL_STRING = "all";
    public static final int ALL_INT = -1;
    public static final int TYPE_DOCUMENT_PROCHAINE_SEANCE = 1;
    public static final int TYPE_DOCUMENT_GESTION_AVAL = 2;
    private int _nTypeDocument = ALL_INT;
    private int _nIdSeance = ALL_INT;
    private int _nIdTypeDocument = ALL_INT;
    private int _nPublication = ALL_INT;
    private int _nPDD = ALL_INT;
    private int _nIdCommission = ALL_INT;
    private int _nCommissionNull = ALL_INT;
    private String _strIntitule = ALL_STRING;
    private int _nAutreDocumentsSeance = ALL_INT;
    private int _nIdFormationConseil = ALL_INT;
    private Timestamp _tsDatePublication;
    private List<String> _orderByList = new ArrayList<String>(  );
    private List<TypeDocumentEnum> _typesDocList = new ArrayList<TypeDocumentEnum>(  );

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
     * Retourne la séance à filtrer
     * @return la séance à filtrer
     */
    public int getIdSeance(  )
    {
        return _nIdSeance;
    }

    /**
     * Fixe la valeur du filtre séance
     * @param nIdSeance la séance à filtrer
     */
    public void setIdSeance( int nIdSeance )
    {
        _nIdSeance = nIdSeance;
    }

    /**
     * Indique si le filtre sur la séance est activé.
     * @return <b>true</b> si le filtre sur lséance est positionné, <b>false</b> sinon
     */
    public boolean containsSeanceCriteria(  )
    {
        return ( _nIdSeance != ALL_INT );
    }

    /**
     * Retourne le type de documents à filtrer
     * @return le type de cdocument à filtrer
     */
    public int getIdTypeDocument(  )
    {
        return _nIdTypeDocument;
    }

    /**
     * Fixe le filtre sur le type de document
     * @param nIdTypeDocument la valeur à filtrer
     */
    public void setIdTypeDocument( int nIdTypeDocument )
    {
        _nIdTypeDocument = nIdTypeDocument;
    }

    /**
     * Indique si le filtre sur le type de documents est positionné
     * @return <b>true</b> si le filtre sur le type de documents est positionné, <b>false</b> sinon
     */
    public boolean containsTypeDocumentCriteria(  )
    {
        return ( _nIdTypeDocument != ALL_INT );
    }

    /**
     * Retourne la valeur de publication à filtrer
     * @return <b>true</b> si _nPublication vaut 1, <b>false</b> sinon
     */
    public boolean isPublication(  )
    {
        if ( _nPublication == 1 )
        {
            return true;
        }

        return false;
    }

    /**
     * Fixe le filtre sur la publication
     * 1 = publié
     * 0 = non publié
     * @param nPublication la valeur à filtrer
     */
    public void setPublication( int nPublication )
    {
        _nPublication = nPublication;
    }

    /**
     * Indique si le filtre sur la publication est activé
     * @return <b>true</b> si le filtre sur la publication est positionné, <b>false</b> sinon
     */
    public boolean containsPublicationCriteria(  )
    {
        return ( _nPublication != ALL_INT );
    }

    /**
     * Retourne la valeur de l'identifiant du PDD à filtrer
     * @return l'identifiant du PDD à filtrer
     */
    public int getIdPDD(  )
    {
        return _nPDD;
    }

    /**
     * Fixe l'identifiant du PDD à filtrer
     * @param nPDD l'identifiant du PDD
     */
    public void setPDD( int nPDD )
    {
        _nPDD = nPDD;
    }

    /**
     * Indique si le filtre doit prendre en compte le PDD
     * @return <b>true</b> si le filtre sur le PDD est positionné, <b>false</b> sinon
     */
    public boolean containsPDDCriteria(  )
    {
        return ( _nPDD != ALL_INT );
    }

    /**
     * retourne l'intitule du fichier
     * @return le titre du fichier
     */
    public String getIntitule(  )
    {
        return _strIntitule;
    }

    /**
     *initialise l'intitule du fichier
     * @param strTitre le titre du fichier
     */
    public void setIntitule( String strTitre )
    {
        _strIntitule = strTitre;
    }

    /**
         * retourne true si le filtre contient le critere Titre false sinon
         * @return retourne si le filtre contient le critere Titre false sinon
         */
    public boolean containsIntituleCriteria(  )
    {
        return ( !_strIntitule.equals( ALL_STRING ) );
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
     * retourne 1 si la  commission doit etre null
     * @return id de la commission présent dans le filtre
     */
    public int getCommissionNull(  )
    {
        return _nCommissionNull;
    }

    /**
     *  initialise a 1 si la commission doit etre nulle
     * @param idCommission id de la commission présent dans le filtre
     */
    public void setCommissionNul( int idCommission )
    {
        _nCommissionNull = idCommission;
    }

    /**
         * retourne true si le filtre contient la commission  null false sinon
         * @return retourne true si le filtre contient la commission  null false sinon
         */
    public boolean containsCommissionNullCriteria(  )
    {
        return ( _nCommissionNull != ALL_INT );
    }

    /**
     * @return the _nAutreDocumentsSeance
     */
    public int getAutreDocumentsSeance(  )
    {
        return _nAutreDocumentsSeance;
    }

    /**
     * @param autreDocumentsSeance the _nAutreDocumentsSeance to set
     */
    public void setAutreDocumentsSeance( int autreDocumentsSeance )
    {
        _nAutreDocumentsSeance = autreDocumentsSeance;
    }

    /**
    * retourne true si le filtre pour rechercher les autres documents de seances est activé
    * @return retourne true si le filtre pour rechercher les autres documents de seances est activé  false sinon
    */
    public boolean containsAutreDocumentsSeanceCriteria(  )
    {
        return ( _nAutreDocumentsSeance != ALL_INT );
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
     * ajoute un critere de tri
     * @param strOrder critere de tri
     */
    public void addInOrderBy( String strOrder )
    {
        _orderByList.add( strOrder );
    }

    /**
     * retourne true si le filtre contient des criteres de tris sinon false
     * @return retourne true si le filtre contient des criteres de tris sinon false
     */
    public boolean containsOrderByCriteria(  )
    {
        return ( !_orderByList.isEmpty(  ) );
    }

    /**
     * Retourne :
     * 1 si on veut filter que les types de documents propres à la prochaine séance :
     *         - Convocation du maire
     *         - Document Libre
     *         - Liste intégrale des questions
     *         - Relevé de la conférence d'organisation
     *         - Résumé des questions
     *         - Désignation
     * 2 si on veut filtrer ceux de la gestion aval :
     *         - pareil que pour la prochaine séance
     *         + Compte rendu
     *         + Délibération de désignation
     * @return 1 pour la prochaine séance, 2 pour la gestion aval
     */
    public int getFilterTypeDocument(  )
    {
        return _nTypeDocument;
    }

    /**
     * Affecte le filtrage par type de document suivant si c'est la liste
     * pour la prochaine séance ou pour la gestion aval
     * @param type 1 pour la prochaine séance, 2 pour la gestion aval
     */
    public void setFilterTypeDocument( int type )
    {
        _nTypeDocument = type;
    }

    /**
     * retourne true si le filtre contient 1 ou 2 suivant le type de document
     * @return retourne true si le filtre contient 1 ou 2 suivant le type de document
     */
    public boolean containsFilterTypeDocumentCriteria(  )
    {
        return ( ( _nTypeDocument == 1 ) || ( _nTypeDocument == 2 ) );
    }

    /**
     * Retourne la liste des types de document voulus
     * @return la liste des types de documents
     */
    public List<TypeDocumentEnum> getTypesDocList(  )
    {
        return _typesDocList;
    }

    /**
     * Fixe la liste des types de documents voulus
     * @param typesDocList la liste des types de documents voulus
     */
    public void setTypesDocList( List<TypeDocumentEnum> typesDocList )
    {
        _typesDocList = typesDocList;
    }

    /**
     * Ajoute un type de document à filtrer
     * @param typeDoc le type à filtrer
     */
    public void addInTypeDoc( TypeDocumentEnum typeDoc )
    {
        _typesDocList.add( typeDoc );
    }

    /**
     * Indique si l'on veut filtrer par type de documents
     * @return <b>true</b> si la liste des types de documents n'est pas vide, <b>false</b> sinon
     */
    public boolean containsListeTypesDocumentsCriteria(  )
    {
        return ( !_typesDocList.isEmpty(  ) );
    }
}
