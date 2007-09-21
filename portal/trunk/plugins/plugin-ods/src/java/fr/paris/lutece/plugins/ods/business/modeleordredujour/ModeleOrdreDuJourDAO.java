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
package fr.paris.lutece.plugins.ods.business.modeleordredujour;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysique;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourFilter;
import fr.paris.lutece.plugins.ods.business.ordredujour.TypeOrdreDuJour;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Cette classe permet d'accèder/gérer la table ods_fichier
 */
public class ModeleOrdreDuJourDAO implements fr.paris.lutece.plugins.ods.business.modeleordredujour.IModeleOrdreDuJourDAO
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_modele, id_fichier, id_commission," +
        "id_type_odj,ods_id_fichier,id_formation_conseil,titre FROM  ods_modele_ordre_jour " + "WHERE id_modele=? ";
    private static final String SQL_QUERY_FIND_BY_FILTER = "SELECT id_modele, id_fichier, id_commission," +
        "id_type_odj,ods_id_fichier,id_formation_conseil,titre FROM  ods_modele_ordre_jour " + "WHERE 1=1 ";
    private static final String SQL_QUERY_MODJ_LIST = "SELECT mo.id_modele, mo.id_fichier,com. id_commission," +
        "com.numero_commission,ty.id_type_odj,ty.libelle_type_odj,mo.ods_id_fichier,fc.id_formation_conseil," +
        "fc.libelle_formation_conseil,mo.titre FROM  ods_modele_ordre_jour mo " +
        " LEFT JOIN ods_commission com on (mo.id_commission = com.id_commission) " +
        " LEFT JOIN ods_formation_conseil fc on (mo.id_formation_conseil=fc.id_formation_conseil) " +
        " LEFT JOIN ods_type_ordre_jour ty on (mo.id_type_odj=ty.id_type_odj) " + " WHERE 1 " +
        " ORDER BY fc.id_formation_conseil,ty.id_type_odj,com.numero_commission";
    private static final String SQL_QUERY_ALREADY_EXIST = "SELECT COUNT(*) FROM  ods_modele_ordre_jour " +
        "WHERE id_type_odj=? and id_formation_conseil=? and id_commission=? ";
    private static final String SQL_FILTER_ID_MODELE = "and id_modele!=? ";
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_modele ) FROM ods_modele_ordre_jour ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO ods_modele_ordre_jour (id_modele, id_fichier, id_commission, id_type_odj,ods_id_fichier,id_formation_conseil,titre) VALUES ( ?, ?, ?, ?, ?, ?, ?) ";
    private static final String SQL_QUERY_UPDATE = " UPDATE ods_modele_ordre_jour SET id_modele=?, id_fichier=?, id_commission=?, id_type_odj=?,ods_id_fichier=?,id_formation_conseil=?,titre=?  WHERE id_modele=? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM ods_modele_ordre_jour WHERE id_modele = ?  ";
    private static final String SQL_FILTER_FORMATION_CONSEIL = " AND id_formation_conseil = ? ";
    private static final String SQL_FILTER_TYPE = " AND id_type_odj= ? ";
    private static final String SQL_FILTER_COMMISSION = " AND id_commission = ? ";
    private static final String SQL_FILTER_COMMISSION_NULL = " AND id_commission is NULL ";

    /**
     * Crée un nouveau MODJ à partir de l’objet MODJ passé en paramètre
     * @param modeleOrdreDuJour le MODJ à insérer
     * @param plugin le plugin
     * @return int primaryKey
     */
    public int insert( ModeleOrdreDuJour modeleOrdreDuJour, Plugin plugin )
    {
        int newPrimaryKey = newPrimaryKey( plugin );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        modeleOrdreDuJour.setIdModele( newPrimaryKey );
        daoUtil.setInt( 1, modeleOrdreDuJour.getIdModele(  ) );

        if ( modeleOrdreDuJour.getEnteteDocument(  ) == null )
        {
            daoUtil.setIntNull( 2 );
        }
        else
        {
            daoUtil.setInt( 2, modeleOrdreDuJour.getEnteteDocument(  ).getIdFichier(  ) );
        }

        if ( modeleOrdreDuJour.getCommission(  ) == null )
        {
            daoUtil.setIntNull( 3 );
        }
        else
        {
            daoUtil.setInt( 3, modeleOrdreDuJour.getCommission(  ).getIdCommission(  ) );
        }

        daoUtil.setInt( 4, modeleOrdreDuJour.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) );

        if ( modeleOrdreDuJour.getPiedDocument(  ) == null )
        {
            daoUtil.setIntNull( 5 );
        }
        else
        {
            daoUtil.setInt( 5, modeleOrdreDuJour.getPiedDocument(  ).getIdFichier(  ) );
        }

        daoUtil.setInt( 6, modeleOrdreDuJour.getFormationConseil(  ).getIdFormationConseil(  ) );
        daoUtil.setString( 7, modeleOrdreDuJour.getTitre(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        return newPrimaryKey;
    }

    /**
     * Modifie le MODJ correspondant à l’objet MODJ passé en paramètre
     * @param modeleOrdreDuJour le MODJ à modifier
     * @param plugin le plugin
     */
    public void store( ModeleOrdreDuJour modeleOrdreDuJour, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, modeleOrdreDuJour.getIdModele(  ) );

        if ( modeleOrdreDuJour.getEnteteDocument(  ) == null )
        {
            daoUtil.setIntNull( 2 );
        }
        else
        {
            daoUtil.setInt( 2, modeleOrdreDuJour.getEnteteDocument(  ).getIdFichier(  ) );
        }

        if ( modeleOrdreDuJour.getCommission(  ) == null )
        {
            daoUtil.setIntNull( 3 );
        }
        else
        {
            daoUtil.setInt( 3, modeleOrdreDuJour.getCommission(  ).getIdCommission(  ) );
        }

        daoUtil.setInt( 4, modeleOrdreDuJour.getTypeOrdreDuJour(  ).getIdTypeOrdreDuJour(  ) );

        if ( modeleOrdreDuJour.getPiedDocument(  ) == null )
        {
            daoUtil.setIntNull( 5 );
        }
        else
        {
            daoUtil.setInt( 5, modeleOrdreDuJour.getPiedDocument(  ).getIdFichier(  ) );
        }

        daoUtil.setInt( 6, modeleOrdreDuJour.getFormationConseil(  ).getIdFormationConseil(  ) );
        daoUtil.setString( 7, modeleOrdreDuJour.getTitre(  ) );
        daoUtil.setInt( 8, modeleOrdreDuJour.getIdModele(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime le MODJ correspondant à l’objet passé en paramètre
     * @param modeleOrdreDuJour  le  MODJ à supprimer
     * @param plugin le plugin
     * @throws AppException si contrainte de clé étrangère
     */
    public void delete( ModeleOrdreDuJour modeleOrdreDuJour, Plugin plugin )
        throws AppException
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, modeleOrdreDuJour.getIdModele(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne le ModeleOrdreDuJour ayant comme identifiant nKey
     * @param nKey identifiant du ModeleOrdreDuJour à charger
     * @param plugin le plugin
     * @return le fichier identifié par nKey
     */
    public List<ModeleOrdreDuJour> selectListModeleOrdreDuJourList( Plugin plugin )
    {
        List<ModeleOrdreDuJour> modeleOrdreDuJours = new ArrayList<ModeleOrdreDuJour>(  );
        ModeleOrdreDuJour modeleOrdreDuJour = null;
        FichierPhysique fichierEnTete = null;
        FichierPhysique fichierPiedDocument = null;
        FormationConseil formationConseil = null;
        Commission commission = null;
        TypeOrdreDuJour typeOrdreDuJour = null;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_MODJ_LIST, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            modeleOrdreDuJour = new ModeleOrdreDuJour(  );
            modeleOrdreDuJour.setIdModele( daoUtil.getInt( 1 ) );

            if ( daoUtil.getObject( 2 ) != null )
            {
                fichierEnTete = new FichierPhysique(  );
                fichierEnTete.setIdFichier( daoUtil.getInt( 2 ) );
                modeleOrdreDuJour.setEnteteDocument( fichierEnTete );
            }

            if ( daoUtil.getObject( 3 ) != null )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 3 ) );
                commission.setNumero( daoUtil.getInt( 4 ) );
                modeleOrdreDuJour.setCommission( commission );
            }

            typeOrdreDuJour = new TypeOrdreDuJour(  );
            typeOrdreDuJour.setIdTypeOrdreDuJour( daoUtil.getInt( 5 ) );
            typeOrdreDuJour.setLibelle( daoUtil.getString( 6 ) );
            modeleOrdreDuJour.setTypeOrdreDuJour( typeOrdreDuJour );

            if ( daoUtil.getObject( 7 ) != null )
            {
                fichierPiedDocument = new FichierPhysique(  );
                fichierPiedDocument.setIdFichier( daoUtil.getInt( 7 ) );
                modeleOrdreDuJour.setPiedDocument( fichierPiedDocument );
            }

            formationConseil = new FormationConseil(  );
            formationConseil.setIdFormationConseil( daoUtil.getInt( 8 ) );
            formationConseil.setLibelle( daoUtil.getString( 9 ) );
            modeleOrdreDuJour.setFormationConseil( formationConseil );

            modeleOrdreDuJour.setTitre( daoUtil.getString( 10 ) );

            modeleOrdreDuJours.add( modeleOrdreDuJour );
        }

        daoUtil.free(  );

        return modeleOrdreDuJours;
    }

    /**
     * Retourne le ModeleOrdreDuJour ayant comme identifiant nKey
     * @param nKey identifiant du ModeleOrdreDuJour à charger
     * @param plugin le plugin
     * @return le fichier identifié par nKey
     */
    public ModeleOrdreDuJour load( int nKey, Plugin plugin )
    {
        ModeleOrdreDuJour modeleOrdreDuJour = null;
        FichierPhysique fichierEnTete = null;
        FichierPhysique fichierPiedDocument = null;
        FormationConseil formationConseil = null;
        Commission commission = null;
        TypeOrdreDuJour typeOrdreDuJour = null;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            modeleOrdreDuJour = new ModeleOrdreDuJour(  );
            modeleOrdreDuJour.setIdModele( daoUtil.getInt( 1 ) );

            if ( daoUtil.getObject( 2 ) != null )
            {
                fichierEnTete = new FichierPhysique(  );
                fichierEnTete.setIdFichier( daoUtil.getInt( 2 ) );
                modeleOrdreDuJour.setEnteteDocument( fichierEnTete );
            }

            if ( daoUtil.getObject( 3 ) != null )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 3 ) );
                modeleOrdreDuJour.setCommission( commission );
            }

            typeOrdreDuJour = new TypeOrdreDuJour(  );
            typeOrdreDuJour.setIdTypeOrdreDuJour( daoUtil.getInt( 4 ) );
            modeleOrdreDuJour.setTypeOrdreDuJour( typeOrdreDuJour );

            if ( daoUtil.getObject( 5 ) != null )
            {
                fichierPiedDocument = new FichierPhysique(  );
                fichierPiedDocument.setIdFichier( daoUtil.getInt( 5 ) );
                modeleOrdreDuJour.setPiedDocument( fichierPiedDocument );
            }

            formationConseil = new FormationConseil(  );
            formationConseil.setIdFormationConseil( daoUtil.getInt( 6 ) );
            modeleOrdreDuJour.setFormationConseil( formationConseil );

            modeleOrdreDuJour.setTitre( daoUtil.getString( 7 ) );
        }

        daoUtil.free(  );

        return modeleOrdreDuJour;
    }

    /**
     * Retourne le ModeleOrdreDuJour répondant au filtre ordreDuJourFilter
     * @param ordreDuJourFilter filtre
     * @param plugin le plugin
     * @return  Retourne le ModeleOrdreDuJour répondant au filtre ordreDuJourFilter
     */
    public ModeleOrdreDuJour load( OrdreDuJourFilter filter, Plugin plugin )
    {
        ModeleOrdreDuJour modeleOrdreDuJour = null;
        FichierPhysique fichierEnTete = null;
        FichierPhysique fichierPiedDocument = null;
        FormationConseil formationConseil = null;
        Commission commission = null;
        TypeOrdreDuJour typeOrdreDuJour = null;

        String strSQL = SQL_QUERY_FIND_BY_FILTER;

        //si la commission n'est pas précisé on fitre sur la commission null
        strSQL += ( ( filter.containsIdCommissionCriteria(  ) ) ? SQL_FILTER_COMMISSION : SQL_FILTER_COMMISSION_NULL );
        strSQL += ( ( filter.containsIdFormationConseilCriteria(  ) ) ? SQL_FILTER_FORMATION_CONSEIL
                                                                      : OdsConstants.CONSTANTE_CHAINE_VIDE );
        strSQL += ( ( filter.containsIdTypeCriteria(  ) ) ? SQL_FILTER_TYPE : OdsConstants.CONSTANTE_CHAINE_VIDE );

        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );

        int nIndex = 1;

        if ( filter.containsIdCommissionCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdCommission(  ) );
            nIndex++;
        }

        if ( filter.containsIdFormationConseilCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdFormationConseil(  ) );
            nIndex++;
        }

        if ( filter.containsIdTypeCriteria(  ) )
        {
            daoUtil.setInt( nIndex, filter.getIdType(  ) );
            nIndex++;
        }

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            modeleOrdreDuJour = new ModeleOrdreDuJour(  );
            modeleOrdreDuJour.setIdModele( daoUtil.getInt( 1 ) );

            if ( daoUtil.getObject( 2 ) != null )
            {
                fichierEnTete = new FichierPhysique(  );
                fichierEnTete.setIdFichier( daoUtil.getInt( 2 ) );
                modeleOrdreDuJour.setEnteteDocument( fichierEnTete );
            }

            if ( daoUtil.getObject( 3 ) != null )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 3 ) );
                modeleOrdreDuJour.setCommission( commission );
            }

            typeOrdreDuJour = new TypeOrdreDuJour(  );
            typeOrdreDuJour.setIdTypeOrdreDuJour( daoUtil.getInt( 4 ) );
            modeleOrdreDuJour.setTypeOrdreDuJour( typeOrdreDuJour );

            if ( daoUtil.getObject( 5 ) != null )
            {
                fichierPiedDocument = new FichierPhysique(  );
                fichierPiedDocument.setIdFichier( daoUtil.getInt( 5 ) );
                modeleOrdreDuJour.setPiedDocument( fichierPiedDocument );
            }

            formationConseil = new FormationConseil(  );
            formationConseil.setIdFormationConseil( daoUtil.getInt( 6 ) );
            modeleOrdreDuJour.setFormationConseil( formationConseil );

            modeleOrdreDuJour.setTitre( daoUtil.getString( 7 ) );
        }

        daoUtil.free(  );

        return modeleOrdreDuJour;
    }

    /**
     * Test si le le modele est déja présent en base.si nIdModele est différent de -1
     * le test d'unicité doit  se faire sur tous les enregistrements n'ayant pas comme id nIdModele
     * @param nIdType
     * @param nIdFormationConseil
     * @param nIdCommission
     * @param nIdModele
     * @param plugin
     * @return
     */
    public boolean isAlreadyExist( int nIdType, int nIdFormationConseil, int nIdCommission, int nIdModele, Plugin plugin )
    {
        String strSQL = SQL_QUERY_ALREADY_EXIST;

        if ( nIdModele != -1 )
        {
            strSQL = SQL_QUERY_ALREADY_EXIST + SQL_FILTER_ID_MODELE;
        }

        DAOUtil daoUtil = new DAOUtil( strSQL, plugin );

        daoUtil.setInt( 1, nIdType );
        daoUtil.setInt( 2, nIdFormationConseil );
        daoUtil.setInt( 3, nIdCommission );

        if ( nIdModele != -1 )
        {
            daoUtil.setInt( 4, nIdModele );
        }

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) && ( daoUtil.getInt( 1 ) != 0 ) )
        {
            daoUtil.free(  );

            return true;
        }

        daoUtil.free(  );

        return false;
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

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }
}
