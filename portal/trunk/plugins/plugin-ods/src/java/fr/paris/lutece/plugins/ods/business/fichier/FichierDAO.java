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

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;


/**
 * Cette classe permet d'accèder/gérer la table ods_fichier
 */
public class FichierDAO implements fr.paris.lutece.plugins.ods.business.fichier.IFichierDAO
{
    private static final String SQL_QUERY_ORDER_BY_INTITULE = " ORDER BY fc.intitule ";
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT * FROM ods_fichier fc " +
        "LEFT JOIN ods_fichier_physique fp on (fc.id_fichier = fp.id_fichier) " +
        "LEFT JOIN ods_type_document td on (fc.id_type_document = td.id_type_document) " +
        "LEFT JOIN ods_commission com on (fc.id_commission = com.id_commission) " +
        "LEFT JOIN ods_seance seance on (fc.id_seance = seance.id_seance) " +
        "LEFT JOIN ods_pdd pdd on (fc.id_pdd = pdd.id_pdd) " + "WHERE fc.id_document = ? ";
    private static final String SQL_QUERY_FICHIERS_LIST = "SELECT * FROM ods_fichier fc " +
        "LEFT JOIN ods_type_document td on (fc.id_type_document = td.id_type_document) " +
        "LEFT JOIN ods_pdd pdd on (fc.id_pdd = pdd.id_pdd) " +
        "LEFT JOIN ods_commission com on (fc.id_commission = com.id_commission) " +
        "LEFT JOIN ods_seance seance on (fc.id_seance = seance.id_seance) " + "WHERE 1 = 1 ";
    private static final String SQL_FILTER_SEANCE = " AND fc.id_seance = ? ";
    private static final String SQL_FILTER_TYPE_DOCUMENT = " AND fc.id_type_document = ? ";
    private static final String SQL_FILTER_PUBLICATION = " AND fc.en_ligne = ? ";
    private static final String SQL_FILTER_PDD = " AND fc.id_pdd = ? ";
    private static final String SQL_FILTER_COMMISSION = " AND fc.id_commission = ? ";
    private static final String SQL_FILTER_COMMISSION_NULL = " AND fc.id_commission is NULL ";
    private static final String SQL_FILTER_COMMISSION_NOT_NULL = " AND fc.id_commission is NOT NULL ";
    private static final String SQL_FILTER_INTITULE = " AND fc.intitule = ? ";
    private static final String SQL_FILTER_AUTRES_DOCUMENTS_SEANCES = " AND ( fc.id_type_document = " +
        TypeDocumentEnum.CONVOCATION_DU_MAIRE.getId(  ) + " OR fc.id_type_document = " +
        TypeDocumentEnum.DOCUMENT_LIBRE.getId(  ) + " OR fc.id_type_document = " +
        TypeDocumentEnum.LISTE_INTEGRALE_DES_QUESTIONS.getId(  ) + " OR fc.id_type_document = " +
        TypeDocumentEnum.RELEVE_DE_LA_CONFERENCE_DORGANISATION.getId(  ) + " OR fc.id_type_document = " +
        TypeDocumentEnum.RESUME_DES_QUESTIONS.getId(  ) + " OR fc.id_type_document = " +
        TypeDocumentEnum.DESIGNATION.getId(  ) + " ) ";
    private static final String SQL_FILTER_FORMATION_CONSEIL = " AND fc.id_formation_conseil = ? ";
    private static final String SQL_FILTER_DATE_PUBLICATION = " AND fc.date_publication > ? ";
    private static final String SQL_FILTER_TYPE_DOCUMENT_GESTION_AVAL_LIST = "AND ( " + " fc.id_type_document = " +
        TypeDocumentEnum.CONVOCATION_DU_MAIRE.getId(  ) + " OR " + " fc.id_type_document = " +
        TypeDocumentEnum.DOCUMENT_LIBRE.getId(  ) + " OR " + " fc.id_type_document = " +
        TypeDocumentEnum.LISTE_INTEGRALE_DES_QUESTIONS.getId(  ) + " OR " + " fc.id_type_document = " +
        TypeDocumentEnum.RELEVE_DE_LA_CONFERENCE_DORGANISATION.getId(  ) + " OR " + " fc.id_type_document = " +
        TypeDocumentEnum.RESUME_DES_QUESTIONS.getId(  ) + " OR " + " fc.id_type_document = " +
        TypeDocumentEnum.DESIGNATION.getId(  ) + " OR " + " fc.id_type_document = " +
        TypeDocumentEnum.COMPTE_RENDU.getId(  ) + " OR " + " fc.id_type_document = " +
        TypeDocumentEnum.DELIBERATION_DE_DESIGNATION.getId(  ) + " ) ";
    private static final String SQL_FILTER_OR_ID_TYPE_DOC = " OR fc.id_type_document = ? ";
    private static final String SQL_FILTER_AND_ID_TYPE_DOC = " AND ( fc.id_type_document = ? ";
    private static final String ORDER_BY_LIST = " ORDER BY ";
    private static final String SQL_QUERY_TYPE_DOCUMENT_LIST = " SELECT * FROM ods_type_document WHERE 1=1";
    private static final String SQL_QUERY_TYPE_DOCUMENT_BY_PRIMARY_KEY = " SELECT * FROM ods_type_document WHERE id_type_document = ? ";
    private static final String SQL_QUERY_TYPE_DOCUMENT_LIST_PROCHAINE_SEANCE = " SELECT * FROM ods_type_document WHERE est_creation_prochaine_seance=1 ";
    private static final String SQL_QUERY_TYPE_DOCUMENT_LIST_AVAL = " SELECT * FROM ods_type_document WHERE est_creation_aval=1 ";
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_document ) FROM ods_fichier ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO ods_fichier (id_document, id_fichier, id_commission, id_type_document, id_seance, intitule, texte, en_ligne, extension, taille, nom, id_pdd, version,id_formation_conseil) VALUES ( ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
    private static final String SQL_QUERY_PUBLIER = " UPDATE ods_fichier SET date_publication = ?, en_ligne = ?, version = ? WHERE id_document= ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE ods_fichier SET id_commission = ?, id_type_document = ?, intitule = ?, texte = ?, extension = ?, taille = ?, nom = ? , version = ?, date_publication = ?, id_seance = ?,id_formation_conseil=? WHERE id_document= ?  ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM ods_fichier WHERE id_document = ?  ";

    /**
    * Crée un nouveau fichier à partir de l’objet fichier passé en paramètre
    * @param fichier le fichier à insérer
    * @param plugin le plugin
    * @return int primaryKey
    */
    public int insert( Fichier fichier, Plugin plugin )
    {
        int newPrimaryKey = newPrimaryKey( plugin );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        fichier.setId( newPrimaryKey );
        daoUtil.setInt( 1, fichier.getId(  ) );

        if ( fichier.getFichier(  ) == null )
        {
            daoUtil.setIntNull( 2 );
        }
        else
        {
            daoUtil.setInt( 2, fichier.getFichier(  ).getIdFichier(  ) );
        }

        if ( fichier.getCommission(  ) == null )
        {
            daoUtil.setIntNull( 3 );
        }
        else
        {
            daoUtil.setInt( 3, fichier.getCommission(  ).getIdCommission(  ) );
        }

        if ( fichier.getTypdeDocument(  ) == null )
        {
            daoUtil.setIntNull( 4 );
        }
        else
        {
            daoUtil.setInt( 4, fichier.getTypdeDocument(  ).getId(  ) );
        }

        if ( fichier.getSeance(  ) == null )
        {
            daoUtil.setIntNull( 5 );
        }
        else
        {
            daoUtil.setInt( 5, fichier.getSeance(  ).getIdSeance(  ) );
        }

        daoUtil.setString( 6, fichier.getTitre(  ) );
        daoUtil.setString( 7, fichier.getTexte(  ) );
        daoUtil.setBoolean( 8, fichier.getEnLigne(  ) );
        daoUtil.setString( 9, fichier.getExtension(  ) );
        daoUtil.setInt( 10, fichier.getTaille(  ) );
        daoUtil.setString( 11, fichier.getNom(  ) );

        if ( fichier.getPDD(  ) == null )
        {
            daoUtil.setIntNull( 12 );
        }
        else
        {
            daoUtil.setInt( 12, fichier.getPDD(  ).getIdPdd(  ) );
        }

        daoUtil.setInt( 13, fichier.getVersion(  ) );

        if ( fichier.getFormationConseil(  ) == null )
        {
            daoUtil.setIntNull( 14 );
        }
        else
        {
            daoUtil.setInt( 14, fichier.getFormationConseil(  ).getIdFormationConseil(  ) );
        }

        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        return newPrimaryKey;
    }

    /**
     * Modifie le fichier correspondant à l’objet fichier passé en paramètre
     * @param fichier le fichier à modifier
     * @param plugin le plugin
     */
    public void store( Fichier fichier, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        if ( fichier.getCommission(  ) == null )
        {
            daoUtil.setIntNull( 1 );
        }
        else
        {
            daoUtil.setInt( 1, fichier.getCommission(  ).getIdCommission(  ) );
        }

        if ( fichier.getTypdeDocument(  ) == null )
        {
            daoUtil.setIntNull( 2 );
        }
        else
        {
            daoUtil.setInt( 2, fichier.getTypdeDocument(  ).getId(  ) );
        }

        daoUtil.setString( 3, fichier.getTitre(  ) );
        daoUtil.setString( 4, fichier.getTexte(  ) );
        daoUtil.setString( 5, fichier.getExtension(  ) );
        daoUtil.setInt( 6, fichier.getTaille(  ) );
        daoUtil.setString( 7, fichier.getNom(  ) );
        daoUtil.setInt( 8, fichier.getVersion(  ) );
        daoUtil.setTimestamp( 9, fichier.getDatePublication(  ) );

        if ( fichier.getSeance(  ) == null )
        {
            daoUtil.setIntNull( 10 );
        }
        else
        {
            daoUtil.setInt( 10, fichier.getSeance(  ).getIdSeance(  ) );
        }

        if ( fichier.getFormationConseil(  ) == null )
        {
            daoUtil.setIntNull( 11 );
        }
        else
        {
            daoUtil.setInt( 11, fichier.getFormationConseil(  ).getIdFormationConseil(  ) );
        }

        daoUtil.setInt( 12, fichier.getId(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime le fichier correspondant à l’objet fichier passé en paramètre
     * @param fichier le fichier à supprimer
     * @param plugin le plugin
     * @throws AppException si contrainte de clé étrangère
     */
    public void delete( Fichier fichier, Plugin plugin )
        throws AppException
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, fichier.getId(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne la liste des fichiers correspondants aux critères du filtre.
     * @param filter les critères de recherche du fichier
     * @param plugin le plugin
     * @return la liste de fichiers correspondants aux critères de recherche
     */
    public List<Fichier> selectByFilter( FichierFilter filter, Plugin plugin )
    {
        List<Fichier> fichiers = new ArrayList<Fichier>(  );

        String strSQL = SQL_QUERY_FICHIERS_LIST;
        strSQL += ( ( filter.containsSeanceCriteria(  ) ) ? SQL_FILTER_SEANCE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsTypeDocumentCriteria(  ) ) ? SQL_FILTER_TYPE_DOCUMENT
                                                                : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsPublicationCriteria(  ) ) ? SQL_FILTER_PUBLICATION
                                                               : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsPDDCriteria(  ) ) ? SQL_FILTER_PDD : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdCommissionCriteria(  ) ) ? SQL_FILTER_COMMISSION
                                                                : OdsConstants.CONSTANTE_CHAINE_VIDE );

        if ( filter.containsCommissionNullCriteria(  ) )
        {
            if ( filter.getCommissionNull(  ) == 1 )
            {
                strSQL += SQL_FILTER_COMMISSION_NULL;
            }
            else if ( filter.getCommissionNull(  ) == 0 )
            {
                strSQL += SQL_FILTER_COMMISSION_NOT_NULL;
            }
        }

        strSQL += ( ( filter.containsIntituleCriteria(  ) ) ? SQL_FILTER_INTITULE : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsAutreDocumentsSeanceCriteria(  ) ) ? SQL_FILTER_AUTRES_DOCUMENTS_SEANCES
                                                                        : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdFormationConseilCriteria(  ) ) ? SQL_FILTER_FORMATION_CONSEIL
                                                                      : OdsConstants.CONSTANTE_CHAINE_VIDE );

        strSQL += ( ( filter.containsDatePublicationCriteria(  ) ) ? SQL_FILTER_DATE_PUBLICATION
                                                                   : OdsConstants.CONSTANTE_CHAINE_VIDE );

        if ( filter.containsFilterTypeDocumentCriteria(  ) )
        {
            if ( filter.getFilterTypeDocument(  ) == 1 )
            {
                strSQL += SQL_FILTER_AUTRES_DOCUMENTS_SEANCES;
            }
            else if ( filter.getFilterTypeDocument(  ) == 2 )
            {
                strSQL += SQL_FILTER_TYPE_DOCUMENT_GESTION_AVAL_LIST;
            }
        }

        if ( filter.containsListeTypesDocumentsCriteria(  ) )
        {
            List<TypeDocumentEnum> listeTypesDoc = filter.getTypesDocList(  );
            int nNombreFiltresTypeDoc = listeTypesDoc.size(  );
            strSQL += SQL_FILTER_AND_ID_TYPE_DOC;

            for ( int n = 1; n < nNombreFiltresTypeDoc; n++ )
            {
                strSQL += SQL_FILTER_OR_ID_TYPE_DOC;
            }

            strSQL += " ) ";
        }

        // order By
        if ( filter.containsOrderByCriteria(  ) )
        {
            strSQL += buildOrderBy( filter );
        }
        else
        {
            strSQL += SQL_QUERY_ORDER_BY_INTITULE;
        }

        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );
        int nIndex = 1;

        if ( filter.containsSeanceCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdSeance(  ) );
            nIndex++;
        }

        if ( filter.containsTypeDocumentCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdTypeDocument(  ) );
            nIndex++;
        }

        if ( filter.containsPublicationCriteria(  ) )
        {
            daoUtil.setBoolean( nIndex, filter.isPublication(  ) );
            nIndex++;
        }

        if ( filter.containsPDDCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdPDD(  ) );
            nIndex++;
        }

        if ( filter.containsIdCommissionCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdCommission(  ) );
            nIndex++;
        }

        if ( filter.containsIntituleCriteria(  ) )
        {
            daoUtil.setString( nIndex, filter.getIntitule(  ) );
            nIndex++;
        }

        if ( filter.containsIdFormationConseilCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdFormationConseil(  ) );
            nIndex++;
        }

        if ( filter.containsDatePublicationCriteria(  ) )
        {
            daoUtil.setTimestamp( nIndex, filter.getDatePublication(  ) );
            nIndex++;
        }

        if ( filter.containsListeTypesDocumentsCriteria(  ) )
        {
            for ( TypeDocumentEnum typeDoc : filter.getTypesDocList(  ) )
            {
                daoUtil.setInt( nIndex, typeDoc.getId(  ) );
                nIndex++;
            }
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Fichier fichier = getBasePropertyFichier( daoUtil );
            setOtherPropertyFichier( daoUtil, fichier );

            fichiers.add( fichier );
        }

        daoUtil.free(  );

        return fichiers;
    }

    /**
     * Retourne le Fichier ayant comme identifiant nKey
     * @param nKey identifiant du fichier à charger
     * @param plugin le plugin
     * @return le fichier identifié par nKey
     */
    public Fichier load( int nKey, Plugin plugin )
    {
        Fichier fichier = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            fichier = new Fichier(  );
            fichier = getBasePropertyFichier( daoUtil );
            setOtherPropertyFichier( daoUtil, fichier );
        }

        daoUtil.free(  );

        return fichier;
    }

    /**
     * Retourne la liste des types de documents
     * @param plugin le plugin
     * @return la liste des objets TypeDocument
     */
    public List<TypeDocument> loadTypeDocumentList( Plugin plugin )
    {
        List<TypeDocument> typeDocuments = new ArrayList<TypeDocument>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_TYPE_DOCUMENT_LIST, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            TypeDocument typeDocument = new TypeDocument(  );
            typeDocument.setId( daoUtil.getInt( "id_type_document" ) );
            typeDocument.setLibelle( daoUtil.getString( "libelle_type" ) );

            if ( daoUtil.getObject( "est_creation_prochaine_seance" ) != null )
            {
                typeDocument.setEstPourProchaineSeance( daoUtil.getBoolean( "est_creation_prochaine_seance" ) );
            }

            if ( daoUtil.getObject( "est_creation_aval" ) != null )
            {
                typeDocument.setEstPourGestionAval( daoUtil.getBoolean( "est_creation_aval" ) );
            }

            typeDocuments.add( typeDocument );
        }

        daoUtil.free(  );

        return typeDocuments;
    }

    /**
     * Retourne la liste des types de documents pour la prochaine séance
     * @param plugin le plugin
     * @return la liste des objets TypeDocument
     */
    public List<TypeDocument> loadTypeDocumentProchaineSeanceList( Plugin plugin )
    {
        List<TypeDocument> typeDocuments = new ArrayList<TypeDocument>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_TYPE_DOCUMENT_LIST_PROCHAINE_SEANCE, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            TypeDocument typeDocument = new TypeDocument(  );
            typeDocument.setId( daoUtil.getInt( "id_type_document" ) );
            typeDocument.setLibelle( daoUtil.getString( "libelle_type" ) );

            if ( daoUtil.getObject( "est_creation_prochaine_seance" ) != null )
            {
                typeDocument.setEstPourProchaineSeance( daoUtil.getBoolean( "est_creation_prochaine_seance" ) );
            }

            if ( daoUtil.getObject( "est_creation_aval" ) != null )
            {
                typeDocument.setEstPourGestionAval( daoUtil.getBoolean( "est_creation_aval" ) );
            }

            typeDocuments.add( typeDocument );
        }

        daoUtil.free(  );

        return typeDocuments;
    }

    /**
     * Retourne la liste des types de documents pour la gestion aval
     * @param plugin le plugin
     * @return la liste des objets TypeDocument
     */
    public List<TypeDocument> loadTypeDocumentAvalList( Plugin plugin )
    {
        List<TypeDocument> typeDocuments = new ArrayList<TypeDocument>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_TYPE_DOCUMENT_LIST_AVAL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            TypeDocument typeDocument = new TypeDocument(  );
            typeDocument.setId( daoUtil.getInt( "id_type_document" ) );
            typeDocument.setLibelle( daoUtil.getString( "libelle_type" ) );

            if ( daoUtil.getObject( "est_creation_prochaine_seance" ) != null )
            {
                typeDocument.setEstPourProchaineSeance( daoUtil.getBoolean( "est_creation_prochaine_seance" ) );
            }

            if ( daoUtil.getObject( "est_creation_aval" ) != null )
            {
                typeDocument.setEstPourGestionAval( daoUtil.getBoolean( "est_creation_aval" ) );
            }

            typeDocuments.add( typeDocument );
        }

        daoUtil.free(  );

        return typeDocuments;
    }

    /**
     * Retourne le type de document en fonction de l'id passé en parametre
     * @param nKey identifiant du type de document que l'on souhaite
     * @param plugin le plugin
     * @return le type de document identifié par nKey
     */
    public TypeDocument loadTypeDocumentsById( int nKey, Plugin plugin )
    {
        TypeDocument typeDocument = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_TYPE_DOCUMENT_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            typeDocument = new TypeDocument(  );
            typeDocument.setId( daoUtil.getInt( "id_type_document" ) );
            typeDocument.setLibelle( daoUtil.getString( "libelle_type" ) );

            if ( daoUtil.getObject( "est_creation_prochaine_seance" ) != null )
            {
                typeDocument.setEstPourProchaineSeance( daoUtil.getBoolean( "est_creation_prochaine_seance" ) );
            }

            if ( daoUtil.getObject( "est_creation_aval" ) != null )
            {
                typeDocument.setEstPourGestionAval( daoUtil.getBoolean( "est_creation_aval" ) );
            }
        }

        daoUtil.free(  );

        return typeDocument;
    }

    /**
     * Permet de publier ou de dépublier un fichier
         * Si isPublie égale à TRUE, alors la méthode <b>publie</b> le fichier et incrémente la version<BR>
         * Sinon la méthode change le statut enLigne d'un fichier a <b>false</b>
     * @param nKey l'identifiant du fichier
     * @param tsDatePublication la date de publication
     * @param version la version du fichier
     * @param isPublie le statut du fichier
     * @param plugin le plugin
     */
    public void publication( int nKey, Timestamp tsDatePublication, int version, boolean isPublie, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_PUBLIER, plugin );

        if ( isPublie )
        {
            daoUtil.setTimestamp( 1, tsDatePublication );
            daoUtil.setInt( 3, version + 1 );
        }
        else
        {
            daoUtil.setTimestamp( 1, null );
            daoUtil.setInt( 3, version );
        }

        daoUtil.setBoolean( 2, isPublie );
        daoUtil.setInt( 4, nKey );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Permet de compléter le fichier avec les propriétés qui ont des contraintes sur d'autres tables
     * @param daoUtil contient les informations complémentaires du fichier
     * @param fichier fichier à compléter à partir des informations du daoUtil
     */
    private void setOtherPropertyFichier( DAOUtil daoUtil, Fichier fichier )
    {
        // si le fichier a un fichier physique on le recupere en creant un objet FichierPhysique et on l'affecte à l'objet Fichier
        if ( daoUtil.getObject( "fc.id_fichier" ) != null )
        {
            FichierPhysique fichierPhysique = new FichierPhysique(  );
            fichierPhysique.setIdFichier( daoUtil.getInt( "fc.id_fichier" ) );
            fichier.setFichier( fichierPhysique );
        }

        // si le fichier a une commission on le recupere en creant un objet Commission et on l'affecte à l'objet Fichier
        if ( daoUtil.getObject( "fc.id_commission" ) != null )
        {
            Commission commission = new Commission(  );
            commission.setIdCommission( daoUtil.getInt( "fc.id_commission" ) );
            commission.setNumero( daoUtil.getInt( "com.numero_commission" ) );
            fichier.setCommission( commission );
        }

        // si le fichier a un Type De Document on le recupere en creant un objet Type de document et on l'affecte à l'objet Fichier
        if ( daoUtil.getObject( "fc.id_type_document" ) != null )
        {
            TypeDocument typeDocument = new TypeDocument(  );
            typeDocument.setId( daoUtil.getInt( "td.id_type_document" ) );
            typeDocument.setLibelle( daoUtil.getString( "td.libelle_type" ) );
            fichier.setTypdeDocument( typeDocument );
        }

        // si le fichier a une seance on le recupere en creant un objet Seance et on l'affecte à l'objet Fichier
        if ( daoUtil.getObject( "fc.id_seance" ) != null )
        {
            Seance seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( "seance.id_seance" ) );
            fichier.setSeance( seance );
        }

        if ( daoUtil.getObject( "fc.id_pdd" ) != null )
        {
            PDD pdd = new PDD(  );
            pdd.setIdPdd( daoUtil.getInt( "id_pdd" ) );
            pdd.setReference( daoUtil.getString( "reference" ) );
            pdd.setTypePdd( daoUtil.getString( "type_pdd" ) );
            pdd.setDelegationsServices( daoUtil.getBoolean( "delegations_services" ) );
            pdd.setModeIntroduction( daoUtil.getString( "mode_introduction" ) );
            pdd.setObjet( daoUtil.getString( "objet" ) );
            pdd.setPiecesManuelles( daoUtil.getBoolean( "pieces_manuelles" ) );
            pdd.setDateVote( daoUtil.getTimestamp( "date_vote" ) );
            pdd.setDateRetourCtrlLegalite( daoUtil.getTimestamp( "date_retour_ctrl_legalite" ) );
            pdd.setEnLigne( daoUtil.getBoolean( "en_ligne" ) );
            pdd.setDatePublication( daoUtil.getTimestamp( "date_publication" ) );
            pdd.setVersion( daoUtil.getInt( "version" ) );
            fichier.setPDD( pdd );
        }

        if ( daoUtil.getObject( "fc.id_formation_conseil" ) != null )
        {
            FormationConseil formationConseil = new FormationConseil(  );
            formationConseil.setIdFormationConseil( daoUtil.getInt( "fc.id_formation_conseil" ) );
            fichier.setFormationConseil( formationConseil );
        }
    }

    /**
     * Permet de creer un objet fichier avec ses propriétés de base
     * @param daoUtil contient les informations permettant de construire l'objet
     * @return le fichier avec les informations de la requête
     */
    private Fichier getBasePropertyFichier( DAOUtil daoUtil )
    {
        Fichier fichier = new Fichier(  );
        fichier.setId( daoUtil.getInt( "id_document" ) );
        fichier.setTexte( daoUtil.getString( "texte" ) );
        fichier.setVersion( daoUtil.getInt( "version" ) );
        fichier.setExtension( daoUtil.getString( "extension" ) );
        fichier.setTaille( daoUtil.getInt( "taille" ) );
        fichier.setNom( daoUtil.getString( "nom" ) );
        fichier.setEnLigne( daoUtil.getBoolean( "en_ligne" ) );
        fichier.setDatePublication( daoUtil.getTimestamp( "date_publication" ) );
        fichier.setTitre( daoUtil.getString( "intitule" ) );

        return fichier;
    }

    /**
     * Génère un nouvel identifiant
     * @param plugin le plugin
     * @return Retourne l’identifiant généré
     */
    int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey = 1;

        if ( daoUtil.next(  ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free(  );

        return nKey;
    }

    /**
     * retourne la clause order by de la requette en fonction du filter
     * @param filter le filtre de selection
     * @return  retourne l'order by de la requette
     */
    private String buildOrderBy( FichierFilter filter )
    {
        StringBuffer strOrderBy = new StringBuffer(  );
        strOrderBy.append( ORDER_BY_LIST );

        for ( String order : filter.getOrderByList(  ) )
        {
            strOrderBy.append( order );
            strOrderBy.append( OdsConstants.VIRGULE );
        }

        return strOrderBy.substring( 0, strOrderBy.length(  ) - 1 );
    }
}
