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
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * RequeteUtilisateurDAO : pour gérer les requêtes sauvegardées par les utilisateurs
 */
public class RequeteUtilisateurDAO implements IRequeteUtilisateurDAO
{
    private static final String SQL_FILTER_BY_REQUETE = " WHERE id_requete = ?";
    private static final String SQL_FILTER_BY_USER = " WHERE id_utilisateur = ?";
    private static final String SQL_FILTER_BY_CRITERE = " AND id_type_critere = ?";
    private static final String SQL_ORDER_BY_DATE = " ORDER BY date_creation DESC";
    private static final String SQL_QUERY_INSERT_REQUETE = "INSERT INTO ods_requete(id_requete, id_utilisateur, " +
        "type_requete, intitule, is_notifie, is_archive, date_creation) VALUES(?,?,?,?,?,?,?)";
    private static final String SQL_QUERY_UPDATE_REQUETE = "UPDATE ods_requete SET is_notifie = ? ";
    private static final String SQL_QUERY_DELETE_REQUETE = "DELETE FROM ods_requete";
    private static final String SQL_QUERY_LOAD = "SELECT id_requete, id_utilisateur, type_requete, intitule, " +
        "is_archive, is_notifie, date_creation FROM ods_requete ";
    private static final String SQL_QUERY_NEW_REQUETE_PK = "SELECT MAX(id_requete) FROM ods_requete";
    private static final String SQL_QUERY_INSERT_CRITERE = "INSERT INTO ods_critere(id_critere, id_requete, " +
        "id_type_critere, valeur_critere) VALUES (?,?,?,?)";
    private static final String SQL_QUERY_DELETE_CRITERE = "DELETE FROM ods_critere";
    private static final String SQL_QUERY_FIND_CRITERES = "SELECT id_critere, valeur_critere FROM ods_critere";
    private static final String SQL_QUERY_NEW_CRITERE_PK = "SELECT MAX(id_critere) FROM ods_critere";
    private static final String ALL_TYPES_DOC_NEXT = "proposition:projet:amendement:voeu";
    private static final String ALL_TYPES_DOC_ARCHIVE = "proposition:projet:amendement:voeu:seance:autres";
    private static final String PDD_ONLY = "proposition:projet";

    /**
     * Sauvegarde une nouvelle requête utilisateur dans la base
     * @param requete la requête utilisateur
     * @param plugin le plugin
     */
    public void insert( RequeteUtilisateur requete, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_REQUETE, plugin );
        int nPrimaryKey = newPrimaryKeyOnRequete( plugin );
        daoUtil.setInt( 1, nPrimaryKey );
        daoUtil.setString( 2, requete.getUserName(  ) );
        daoUtil.setString( 3, requete.getTypeRequete(  ) );
        daoUtil.setString( 4, requete.getNomRequete(  ) );
        daoUtil.setBoolean( 5, requete.isNotifie(  ) );
        daoUtil.setBoolean( 6, requete.isRechercheArchive(  ) );
        daoUtil.setTimestamp( 7, new Timestamp( System.currentTimeMillis(  ) ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );

        requete.setIdRequete( nPrimaryKey );
        insertAllCriteres( requete, plugin );
    }

    /**
     * Supprime une requête utilisateur sauvegardée
     * @param requete la requête utilisateur à supprimer
     * @param plugin le plugin
     */
    public void delete( RequeteUtilisateur requete, Plugin plugin )
    {
        deleteAllCriteres( requete, plugin );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_REQUETE + SQL_FILTER_BY_REQUETE, plugin );
        daoUtil.setInt( 1, requete.getIdRequete(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Met à jour les informations de la requête utilisateur<br/>
     * Attention cette méthode <u><b>ne modifie pas le nombre ou la valeur des critères</b></u><br/>
     * Le seul attribut que l'on peut changer est la notification par mail
     * @param requete la requete à modifier
     * @param plugin le plugin
     */
    public void update( RequeteUtilisateur requete, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_REQUETE + SQL_FILTER_BY_REQUETE, plugin );
        daoUtil.setBoolean( 1, requete.isNotifie(  ) );
        daoUtil.setInt( 2, requete.getIdRequete(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Retourne la requête utilisateur identifiée par nKey
     * @param nKey l'identifiant de la requête
     * @param plugin le plugin
     * @return la requête utilisateur identifiée par nKey
     */
    public RequeteUtilisateur load( int nKey, Plugin plugin )
    {
        RequeteUtilisateur requete = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_LOAD + SQL_FILTER_BY_REQUETE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            requete = new RequeteUtilisateur(  );
            requete.setIdRequete( nKey );
            requete.setUserName( daoUtil.getString( 2 ) );
            requete.setTypeRequete( daoUtil.getString( 3 ) );
            requete.setNomRequete( daoUtil.getString( 4 ) );
            requete.setRechercheArchive( daoUtil.getBoolean( 5 ) );
            requete.setNotifie( daoUtil.getBoolean( 6 ) );
            requete.setDateCreation( daoUtil.getTimestamp( 7 ) );
            loadAllCriteres( requete, plugin );
        }

        daoUtil.free(  );

        return requete;
    }

    /**
     * Retourne la liste des requêtes sauvegardées par un utilisateur
     * @param strUserName le 'nom' de l'utilisateur connecté
     * @param plugin le plugin
     * @return la liste des requêtes de l'utilisateur
     */
    public List<RequeteUtilisateur> selectRequetesByUser( String strUserName, Plugin plugin )
    {
        List<RequeteUtilisateur> listeRequetesByUser = new ArrayList<RequeteUtilisateur>(  );
        RequeteUtilisateur requete = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_LOAD + SQL_FILTER_BY_USER + SQL_ORDER_BY_DATE, plugin );
        daoUtil.setString( 1, strUserName );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            requete = new RequeteUtilisateur(  );
            requete.setIdRequete( daoUtil.getInt( 1 ) );
            requete.setUserName( strUserName );
            requete.setTypeRequete( daoUtil.getString( 3 ) );
            requete.setNomRequete( daoUtil.getString( 4 ) );
            requete.setRechercheArchive( daoUtil.getBoolean( 5 ) );
            requete.setNotifie( daoUtil.getBoolean( 6 ) );
            requete.setDateCreation( daoUtil.getTimestamp( 7 ) );
            loadAllCriteres( requete, plugin );
            listeRequetesByUser.add( requete );
        }

        daoUtil.free(  );

        return listeRequetesByUser;
    }

    /**
     * Enregistre les critères de la recherche dans la base
     * @param requete la requête
     * @param plugin le plugin
     */
    private void insertAllCriteres( RequeteUtilisateur requete, Plugin plugin )
    {
        Critere critere = new Critere(  );
        critere.setIdRequete( requete.getIdRequete(  ) );

        String strChampRecherche = requete.getChampRecherche(  );

        if ( ( strChampRecherche != null ) && !strChampRecherche.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            critere.setTypeCritere( TypeCritereEnum.CHAMP_RECHERCHE.getIdTypeCritere(  ) );
            critere.setValeurCritere( strChampRecherche );
            insertCritere( critere, plugin );
        }

        String strTypesDocument;

        if ( requete.getTypeRequete(  ).equals( "simple" ) )
        {
            if ( requete.isRechercheArchive(  ) )
            {
                strTypesDocument = ALL_TYPES_DOC_ARCHIVE;
            }
            else
            {
                strTypesDocument = ALL_TYPES_DOC_NEXT;
            }
        }
        else if ( requete.getTypeRequete(  ).equals( "reference" ) )
        {
            strTypesDocument = PDD_ONLY;
        }
        else
        {
            strTypesDocument = requete.getListeTypesDocument(  );
        }

        critere.setTypeCritere( TypeCritereEnum.TYPES_DOCUMENT.getIdTypeCritere(  ) );
        critere.setValeurCritere( strTypesDocument );
        insertCritere( critere, plugin );

        Timestamp tsDate1 = requete.getPremiereDate(  );

        if ( tsDate1 != null )
        {
            Calendar calendar = new GregorianCalendar(  );
            calendar.setTimeInMillis( tsDate1.getTime(  ) );

            int nJDate1 = calendar.get( Calendar.DATE );
            String strJDate1 = ( ( nJDate1 < 10 ) ? ( "0" + nJDate1 ) : nJDate1 ) + "/";
            int nMDate1 = calendar.get( Calendar.MONTH ) + 1;
            String strMDate1 = ( ( nMDate1 < 10 ) ? ( "0" + nMDate1 ) : nMDate1 ) + "/";
            String strDate1 = strJDate1 + strMDate1 + calendar.get( Calendar.YEAR );
            critere.setTypeCritere( TypeCritereEnum.DATE_1.getIdTypeCritere(  ) );
            critere.setValeurCritere( strDate1 );
            insertCritere( critere, plugin );
        }

        Timestamp tsDate2 = requete.getDeuxiemeDate(  );

        if ( tsDate2 != null )
        {
            Calendar calendar = new GregorianCalendar(  );
            calendar.setTimeInMillis( tsDate2.getTime(  ) );

            int nJDate2 = calendar.get( Calendar.DATE );
            String strJDate2 = ( ( nJDate2 < 10 ) ? ( "0" + nJDate2 ) : nJDate2 ) + "/";
            int nMDate2 = calendar.get( Calendar.MONTH ) + 1;
            String strMDate2 = ( ( nMDate2 < 10 ) ? ( "0" + nMDate2 ) : nMDate2 ) + "/";
            String strDate2 = strJDate2 + strMDate2 + calendar.get( Calendar.YEAR );
            critere.setTypeCritere( TypeCritereEnum.DATE_2.getIdTypeCritere(  ) );
            critere.setValeurCritere( strDate2 );
            insertCritere( critere, plugin );
        }

        String strFormationsConseil = requete.getListeFormationsConseil(  );

        if ( ( strFormationsConseil != null ) && !strFormationsConseil.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            critere.setTypeCritere( TypeCritereEnum.FORMATIONS_CONSEIL.getIdTypeCritere(  ) );
            critere.setValeurCritere( strFormationsConseil );
            insertCritere( critere, plugin );
        }

        String strCommissions = requete.getListeCommissions(  );

        if ( ( strCommissions != null ) && !strCommissions.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            critere.setTypeCritere( TypeCritereEnum.COMMISSIONS.getIdTypeCritere(  ) );
            critere.setValeurCritere( strCommissions );
            insertCritere( critere, plugin );
        }

        String strRapporteurs = requete.getListeRapporteurs(  );

        if ( ( strRapporteurs != null ) && !strRapporteurs.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            critere.setTypeCritere( TypeCritereEnum.RAPPORTEURS.getIdTypeCritere(  ) );
            critere.setValeurCritere( strRapporteurs );
            insertCritere( critere, plugin );
        }

        String strArrondissements = requete.getListeArrondissements(  );

        if ( ( strArrondissements != null ) && !strArrondissements.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            critere.setTypeCritere( TypeCritereEnum.ARRONDISSEMENTS.getIdTypeCritere(  ) );
            critere.setValeurCritere( strArrondissements );
            insertCritere( critere, plugin );
        }

        String strDirections = requete.getListeDirections(  );

        if ( ( strDirections != null ) && !strDirections.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            critere.setTypeCritere( TypeCritereEnum.DIRECTIONS.getIdTypeCritere(  ) );
            critere.setValeurCritere( strDirections );
            insertCritere( critere, plugin );
        }

        String strCategoriesDeliberation = requete.getListeCategoriesDeliberation(  );

        if ( ( strCategoriesDeliberation != null ) &&
                !strCategoriesDeliberation.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            critere.setTypeCritere( TypeCritereEnum.CATEGORIES_DELIBERATION.getIdTypeCritere(  ) );
            critere.setValeurCritere( strCategoriesDeliberation );
            insertCritere( critere, plugin );
        }

        String strGroupesDepositaires = requete.getListeGroupesDepositaires(  );

        if ( ( strGroupesDepositaires != null ) &&
                !strGroupesDepositaires.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            critere.setTypeCritere( TypeCritereEnum.GROUPES_DEPOSITAIRES.getIdTypeCritere(  ) );
            critere.setValeurCritere( strGroupesDepositaires );
            insertCritere( critere, plugin );
        }

        String strElusDepositaires = requete.getListeElusDepositaires(  );

        if ( ( strElusDepositaires != null ) && !strElusDepositaires.equals( OdsConstants.CONSTANTE_CHAINE_VIDE ) )
        {
            critere.setTypeCritere( TypeCritereEnum.ELUS_DEPOSITAIRES.getIdTypeCritere(  ) );
            critere.setValeurCritere( strElusDepositaires );
            insertCritere( critere, plugin );
        }
    }

    /**
     * Supprime tous les critères de recherche de la requête
     * @param requete la requête
     * @param plugin le plugin
     */
    private void deleteAllCriteres( RequeteUtilisateur requete, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_CRITERE + SQL_FILTER_BY_REQUETE, plugin );
        daoUtil.setInt( 1, requete.getIdRequete(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Récupère la valeur des critères de recherche d'une requête enregistrée
     * @param requete la requête
     * @param plugin le plugin
     */
    private void loadAllCriteres( RequeteUtilisateur requete, Plugin plugin )
    {
        requete.setChampRecherche( loadCritere( requete, TypeCritereEnum.CHAMP_RECHERCHE, plugin ).getValeurCritere(  ) );
        requete.setListeTypesDocument( ( loadCritere( requete, TypeCritereEnum.TYPES_DOCUMENT, plugin ) ).getValeurCritere(  ) );
        requete.setPremiereDate( OdsUtils.getDate( loadCritere( requete, TypeCritereEnum.DATE_1, plugin )
                                                       .getValeurCritere(  ), true ) );
        requete.setDeuxiemeDate( OdsUtils.getDate( loadCritere( requete, TypeCritereEnum.DATE_2, plugin )
                                                       .getValeurCritere(  ), true ) );
        requete.setListeFormationsConseil( loadCritere( requete, TypeCritereEnum.FORMATIONS_CONSEIL, plugin )
                                               .getValeurCritere(  ) );
        requete.setListeCommissions( loadCritere( requete, TypeCritereEnum.COMMISSIONS, plugin ).getValeurCritere(  ) );
        requete.setListeRapporteurs( loadCritere( requete, TypeCritereEnum.RAPPORTEURS, plugin ).getValeurCritere(  ) );
        requete.setListeArrondissements( loadCritere( requete, TypeCritereEnum.ARRONDISSEMENTS, plugin )
                                             .getValeurCritere(  ) );
        requete.setListeDirections( loadCritere( requete, TypeCritereEnum.DIRECTIONS, plugin ).getValeurCritere(  ) );
        requete.setListeCategoriesDeliberation( loadCritere( requete, TypeCritereEnum.CATEGORIES_DELIBERATION, plugin )
                                                    .getValeurCritere(  ) );
        requete.setListeGroupesDepositaires( loadCritere( requete, TypeCritereEnum.GROUPES_DEPOSITAIRES, plugin )
                                                 .getValeurCritere(  ) );
        requete.setListeElusDepositaires( loadCritere( requete, TypeCritereEnum.ELUS_DEPOSITAIRES, plugin )
                                              .getValeurCritere(  ) );
    }

    /**
     * Enregistre un critère de recherche
     * @param critere le critère à sauvegarder
     * @param plugin le plugin
     */
    private void insertCritere( Critere critere, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_CRITERE, plugin );
        daoUtil.setInt( 1, newPrimaryKeyOnCritere( plugin ) );
        daoUtil.setInt( 2, critere.getIdRequete(  ) );
        daoUtil.setInt( 3, critere.getTypeCritere(  ) );
        daoUtil.setString( 4, critere.getValeurCritere(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Récupère le critère voulu pour une requête donnée
     * @param requete la requête
     * @param typeCritere le type de critère
     * @param plugin le plugin
     * @return le critère voulu pour une requête donnée
     */
    private Critere loadCritere( RequeteUtilisateur requete, TypeCritereEnum typeCritere, Plugin plugin )
    {
        Critere critere = new Critere(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_CRITERES + SQL_FILTER_BY_REQUETE + SQL_FILTER_BY_CRITERE, plugin );
        daoUtil.setInt( 1, requete.getIdRequete(  ) );
        daoUtil.setInt( 2, typeCritere.getIdTypeCritere(  ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            critere.setIdCritere( daoUtil.getInt( 1 ) );
            critere.setIdRequete( requete.getIdRequete(  ) );
            critere.setTypeCritere( typeCritere.getIdTypeCritere(  ) );
            critere.setValeurCritere( daoUtil.getString( 2 ) );
        }

        daoUtil.free(  );

        return critere;
    }

    /**
    * Retourne une nouvelle clé primaire unique sur la table ods_requete
    * @param plugin plugin
    * @return un identifiant unique
    */
    private int newPrimaryKeyOnRequete( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_REQUETE_PK, plugin );
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

    /**
     * Retourne une nouvelle clé primaire unique sur la table ods_critere
     * @param plugin plugin
     * @return un identifiant unique
     */
    private int newPrimaryKeyOnCritere( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_CRITERE_PK, plugin );
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
