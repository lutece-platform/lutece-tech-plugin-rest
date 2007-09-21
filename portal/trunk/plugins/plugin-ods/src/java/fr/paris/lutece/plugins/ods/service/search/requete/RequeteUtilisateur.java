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

import fr.paris.lutece.plugins.ods.business.categoriedeliberation.CategorieDeliberation;
import fr.paris.lutece.plugins.ods.business.categoriedeliberation.CategorieDeliberationHome;
import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.commission.CommissionHome;
import fr.paris.lutece.plugins.ods.business.direction.Direction;
import fr.paris.lutece.plugins.ods.business.direction.DirectionHome;
import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.elu.EluHome;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilHome;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitique;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitiqueHome;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;


/**
 * Objet requête utilisateur: pour sauvegarder une requête de recherche
 */
public class RequeteUtilisateur
{
    private static final String URL_TEMPLATE_TO_HTML = "skin/plugins/ods/requete_utilisateur.html";
    private static final String MARK_LISTE_TYPES_DOC = "liste_types_doc";
    private static final String MARK_DATE_1 = "date_1";
    private static final String MARK_DATE_2 = "date_2";
    private static final String MARK_MOTS_RECHERCHES = "mots_recherches";
    private static final String MARK_LISTE_FORMATIONS_CONSEIL = "liste_formations_conseil";
    private static final String MARK_LISTE_COMMISSIONS = "liste_commissions";
    private static final String MARK_LISTE_RAPPORTEURS = "liste_rapporteurs";
    private static final String MARK_LISTE_ARRONDISSEMENTS = "liste_arrondissements";
    private static final String MARK_LISTE_DIRECTIONS = "liste_directions";
    private static final String MARK_LISTE_CATEGORIES = "liste_categories";
    private static final String MARK_LISTE_GROUPES = "liste_groupes";
    private static final String MARK_LISTE_ELUS = "liste_elus";
    private int _nIdRequete;
    private String _strUserName = OdsConstants.CONSTANTE_CHAINE_VIDE;
    private String _strNomRequete = OdsConstants.CONSTANTE_CHAINE_VIDE;
    private boolean _bRechercheArchive;
    private String _strTypeRequete = OdsConstants.CONSTANTE_CHAINE_VIDE;
    private Timestamp _tsPremiereDate;
    private Timestamp _tsDeuxiemeDate;
    private String _strChampRecherche = OdsConstants.CONSTANTE_CHAINE_VIDE;
    private String _strListeTypesDocument = OdsConstants.CONSTANTE_CHAINE_VIDE;
    private String _strListeFormationsConseil = OdsConstants.CONSTANTE_CHAINE_VIDE;
    private String _strListeCommissions = OdsConstants.CONSTANTE_CHAINE_VIDE;
    private String _strListeRapporteurs = OdsConstants.CONSTANTE_CHAINE_VIDE;
    private String _strListeArrondissements = OdsConstants.CONSTANTE_CHAINE_VIDE;
    private String _strListeDirections = OdsConstants.CONSTANTE_CHAINE_VIDE;
    private String _strListeCategoriesDeliberation = OdsConstants.CONSTANTE_CHAINE_VIDE;
    private String _strListeGroupesDepositaires = OdsConstants.CONSTANTE_CHAINE_VIDE;
    private String _strListeElusDepositaires = OdsConstants.CONSTANTE_CHAINE_VIDE;
    private boolean _bIsNotifie;
    private Timestamp _tsDateCreation;
    private String _strHtml;

    /**
     * Retourne l'id de la requête
     * @return l'id de la requête
     */
    public int getIdRequete(  )
    {
        return _nIdRequete;
    }

    /**
     * Fixe l'id de la requête
     * @param nIdRequete l'id de la requête
     */
    public void setIdRequete( int nIdRequete )
    {
        _nIdRequete = nIdRequete;
    }

    /**
     * Retourne l'id de l'utilisateur qui a enregistré sa requête
     * @return l'id de l'utilisateur qui a enregistré sa requête
     */
    public String getUserName(  )
    {
        return _strUserName;
    }

    /**
     * Fixe l'id de l'utilisateur qui a enregistré sa requête
     * @param strUserName l'id de l'utilisateur qui a enregistré sa requête
     */
    public void setUserName( String strUserName )
    {
        _strUserName = strUserName;
    }

    /**
     * Retourne le nom de sauvegarde de la requête
     * @return le nom de sauvegarde de la requête
     */
    public String getNomRequete(  )
    {
        return _strNomRequete;
    }

    /**
     * Fixe le nom de sauvegarde de la requête
     * @param strNomRequete le nom de sauvegarde de la requête
     */
    public void setNomRequete( String strNomRequete )
    {
        _strNomRequete = strNomRequete;
    }

    /**
     * Indique si la recherche concerne les archives ou la prochaine séance
     * @return <b>true</b> si la recherche concerne les archives, <b>false</b> sinon
     */
    public boolean isRechercheArchive(  )
    {
        return _bRechercheArchive;
    }

    /**
     * Pour rechercher sur les archives ou sur la prochaine séance
     * @param bRechercheArchive recherche sur archives ou non
     */
    public void setRechercheArchive( boolean bRechercheArchive )
    {
        _bRechercheArchive = bRechercheArchive;
    }

    /**
     * Retourne le type de la requête: simple (par le bandeau haut), par référence ou multi-critère
     * @return le type de la requête
     */
    public String getTypeRequete(  )
    {
        return _strTypeRequete;
    }

    /**
     * Fixe le type de la requête
     * @param strTypeRequete le type de la requête
     */
    public void setTypeRequete( String strTypeRequete )
    {
        _strTypeRequete = strTypeRequete;
    }

    /**
     * Retourne la premiere date de séance renseignée
     * @return la premiere date de séance renseignée
     */
    public Timestamp getPremiereDate(  )
    {
        return _tsPremiereDate;
    }

    /**
     * Fixe la premiere date de séance renseignée
     * @param tsPremiereDate la premiere date de séance renseignée
     */
    public void setPremiereDate( Timestamp tsPremiereDate )
    {
        _tsPremiereDate = tsPremiereDate;
    }

    /**
     * Retourne la deuxième date de séance renseignée
     * @return la deuxième date de séance renseignée
     */
    public Timestamp getDeuxiemeDate(  )
    {
        return _tsDeuxiemeDate;
    }

    /**
     * Fixe la deuxième date de séance renseignée
     * @param tsDeuxiemeDate la deuxième date de séance renseignée
     */
    public void setDeuxiemeDate( Timestamp tsDeuxiemeDate )
    {
        _tsDeuxiemeDate = tsDeuxiemeDate;
    }

    /**
     * Retourne les mots (ou la référence) recherchés
     * @return les mots (ou la référence) recherchée
     */
    public String getChampRecherche(  )
    {
        return _strChampRecherche;
    }

    /**
     * Fixe le champ de saisie sur les mots (ou la référence) recherchée
     * @param strChampRecherche les mots (ou la référence) recherchée
     */
    public void setChampRecherche( String strChampRecherche )
    {
        _strChampRecherche = strChampRecherche;
    }

    /**
     * Retourne la liste des types de document recherchés
     * @return la liste des types de document recherchés
     */
    public String getListeTypesDocument(  )
    {
        return _strListeTypesDocument;
    }

    /**
     * Fixe la liste des types de document recherchés
     * @param strListeTypesDocument la liste des types de document recherchés
     */
    public void setListeTypesDocument( String strListeTypesDocument )
    {
        _strListeTypesDocument = strListeTypesDocument;
    }

    /**
     * Fixe la représentation en base de la liste des types de documents
     * @param listeTypesDocument la liste des types de documents
     */
    public void setTypesDocumentFromListe( List<String> listeTypesDocument )
    {
        String strRepresentation = OdsConstants.CONSTANTE_CHAINE_VIDE;

        if ( listeTypesDocument != null )
        {
            for ( String typeDocument : listeTypesDocument )
            {
                if ( !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strRepresentation ) )
                {
                    strRepresentation += ":";
                }

                strRepresentation += typeDocument;
            }
        }

        setListeTypesDocument( strRepresentation );
    }

    /**
     * Retourne la liste des types de document à partir de la représentation en base
     * @return la liste des types de document à partir de la représentation en base
     */
    public List<String> getListeTypesDocumentFromRepresentation(  )
    {
        List<String> listeTypesDocument = new ArrayList<String>(  );
        StringTokenizer stringTokenizer = new StringTokenizer( getListeTypesDocument(  ), ":" );

        while ( stringTokenizer.hasMoreTokens(  ) )
        {
            listeTypesDocument.add( stringTokenizer.nextToken(  ) );
        }

        return listeTypesDocument;
    }

    /**
     * Retourne la liste des formations de conseil sélectionnées
     * @return la liste des formations de conseil sélectionnées
     */
    public String getListeFormationsConseil(  )
    {
        return _strListeFormationsConseil;
    }

    /**
     * Fixe la liste des formations de conseil sélectionnées
     * @param strListeFormationsConseil la liste des formations de conseil sélectionnées
     */
    public void setListeFormationsConseil( String strListeFormationsConseil )
    {
        _strListeFormationsConseil = strListeFormationsConseil;
    }

    /**
     * Fixe la représentation en base de la liste des formations sélectionnées
     * @param listeFormationsConseil la liste des formations sélectionnées
     */
    public void setFormationsConseilFromListe( List<FormationConseil> listeFormationsConseil )
    {
        String strRepresentation = OdsConstants.CONSTANTE_CHAINE_VIDE;

        if ( getListeFormationsConseil(  ) != null )
        {
            for ( FormationConseil formationConseil : listeFormationsConseil )
            {
                if ( !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strRepresentation ) )
                {
                    strRepresentation += ":";
                }

                strRepresentation += formationConseil.getIdFormationConseil(  );
            }
        }

        setListeFormationsConseil( strRepresentation );
    }

    /**
     * Retourne la liste des formations de conseil sélectionnées à partir de la représentation en base
     * @param plugin le plugin
     * @return la liste des formations de conseil sélectionnées
     */
    public List<FormationConseil> getListeFormationsConseilFromRepresentation( Plugin plugin )
    {
        List<FormationConseil> listeFormationsConseil = new ArrayList<FormationConseil>(  );
        StringTokenizer stringTokenizer = new StringTokenizer( getListeFormationsConseil(  ), ":" );

        while ( stringTokenizer.hasMoreTokens(  ) )
        {
            listeFormationsConseil.add( FormationConseilHome.findByPrimaryKey( Integer.parseInt( 
                        stringTokenizer.nextToken(  ) ), plugin ) );
        }

        return listeFormationsConseil;
    }

    /**
     * Retourne la liste des commissions sélectionnées
     * @return la liste des commissions sélectionnées
     */
    public String getListeCommissions(  )
    {
        return _strListeCommissions;
    }

    /**
     * Fixe la liste des commissions sélectionnées
     * @param strListeCommissions la liste des commissions sélectionnées
     */
    public void setListeCommissions( String strListeCommissions )
    {
        _strListeCommissions = strListeCommissions;
    }

    /**
     * Fixe la représentation en base de la liste des commissions sélectionnées
     * @param listeCommissions la liste des commissions sélectionnées
     */
    public void setCommissionsFromListe( List<Commission> listeCommissions )
    {
        String strRepresentation = OdsConstants.CONSTANTE_CHAINE_VIDE;

        if ( listeCommissions != null )
        {
            for ( Commission commission : listeCommissions )
            {
                if ( !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strRepresentation ) )
                {
                    strRepresentation += ":";
                }

                strRepresentation += commission.getIdCommission(  );
            }
        }

        setListeCommissions( strRepresentation );
    }

    /**
     * Retourne la liste des commissions sélectionnées à partir de la représentation en base
     * @param plugin le plugin
     * @return la liste des commissions sélectionnées à partir de la représentation en base
     */
    public List<Commission> getListeCommissionsFromRepresentation( Plugin plugin )
    {
        List<Commission> listeCommissions = new ArrayList<Commission>(  );
        StringTokenizer stringTokenizer = new StringTokenizer( getListeCommissions(  ), ":" );

        while ( stringTokenizer.hasMoreTokens(  ) )
        {
            listeCommissions.add( CommissionHome.findByPrimaryKey( Integer.parseInt( stringTokenizer.nextToken(  ) ),
                    plugin ) );
        }

        return listeCommissions;
    }

    /**
     * Retourne la liste des élus rapporteurs sélectionnés
     * @return la liste des élus rapporteurs sélectionnés
     */
    public String getListeRapporteurs(  )
    {
        return _strListeRapporteurs;
    }

    /**
     * Fixe la liste des élus rapporteurs sélectionnés
     * @param strListeRapporteurs la liste des élus rapporteurs sélectionnés
     */
    public void setListeRapporteurs( String strListeRapporteurs )
    {
        _strListeRapporteurs = strListeRapporteurs;
    }

    /**
     * Fixe la représentation en base de la liste des élus rapporteurs sélectionnés
     * @param listeRapporteurs la liste des élus rapporteurs sélectionnés
     */
    public void setRapporteursFromListe( List<Elu> listeRapporteurs )
    {
        String strRepresentation = OdsConstants.CONSTANTE_CHAINE_VIDE;

        if ( listeRapporteurs != null )
        {
            for ( Elu rapporteur : listeRapporteurs )
            {
                if ( !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strRepresentation ) )
                {
                    strRepresentation += ":";
                }

                strRepresentation += rapporteur.getIdElu(  );
            }
        }

        setListeRapporteurs( strRepresentation );
    }

    /**
     * Retourne la liste des élus rapporteurs à partir de la représentation en base
     * @param plugin le plugin
     * @return la liste des élus rapporteurs à partir de la représentation en base
     */
    public List<Elu> getListeRapporteursFromRepresentation( Plugin plugin )
    {
        List<Elu> listeRapporteurs = new ArrayList<Elu>(  );
        StringTokenizer stringTokenizer = new StringTokenizer( getListeRapporteurs(  ), ":" );

        while ( stringTokenizer.hasMoreTokens(  ) )
        {
            listeRapporteurs.add( EluHome.findByPrimaryKey( Integer.parseInt( stringTokenizer.nextToken(  ) ), plugin ) );
        }

        return listeRapporteurs;
    }

    /**
     * Retourne la liste des arrondissements sélectionnés
     * @return la liste des arrondissements sélectionnés
     */
    public String getListeArrondissements(  )
    {
        return _strListeArrondissements;
    }

    /**
     * Fixe la liste des arrondissements sélectionnés
     * @param strListeArrondissements la liste des arrondissements sélectionnés
     */
    public void setListeArrondissements( String strListeArrondissements )
    {
        _strListeArrondissements = strListeArrondissements;
    }

    /**
     * Fixe la représentation en base de la liste des arrondissements
     * @param listeArrondissements la liste des arrondissements
     */
    public void setArrondissementsFromListe( int[] listeArrondissements )
    {
        String strRepresentation = OdsConstants.CONSTANTE_CHAINE_VIDE;

        if ( getListeArrondissements(  ) != null )
        {
            for ( Integer arrondissement : listeArrondissements )
            {
                if ( !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strRepresentation ) )
                {
                    strRepresentation += ":";
                }

                strRepresentation += arrondissement.toString(  );
            }
        }

        setListeArrondissements( strRepresentation );
    }

    /**
     * Retourne la liste de arrondissements sélectionnés à partir de la représentation en base
     * @param plugin le plugin
     * @return la liste des arrondissements sélectionnés
     */
    public int[] getListeArrondissementsFromRepresentation( Plugin plugin )
    {
        StringTokenizer stringTokenizer = new StringTokenizer( getListeArrondissements(  ), ":" );
        int[] listeArrondissements = new int[stringTokenizer.countTokens(  )];
        int nIndex = 0;

        while ( stringTokenizer.hasMoreTokens(  ) )
        {
            listeArrondissements[nIndex++] = Integer.parseInt( stringTokenizer.nextToken(  ) );
        }

        return listeArrondissements;
    }

    /**
     * Retourne la liste des directions sélectionnées
     * @return la liste des directions sélectionnées
     */
    public String getListeDirections(  )
    {
        return _strListeDirections;
    }

    /**
     * Fixe la liste des directions sélectionnées
     * @param strListeDirections la liste des directions sélectionnées
     */
    public void setListeDirections( String strListeDirections )
    {
        _strListeDirections = strListeDirections;
    }

    /**
     * Fixe la représentation en base de la liste des directions sélectionnées
     * @param listeDirections la liste des directions sélectionnées
     */
    public void setDirectionsFromListe( List<Direction> listeDirections )
    {
        String strRepresentation = OdsConstants.CONSTANTE_CHAINE_VIDE;

        if ( listeDirections != null )
        {
            for ( Direction direction : listeDirections )
            {
                if ( !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strRepresentation ) )
                {
                    strRepresentation += ":";
                }

                strRepresentation += direction.getIdDirection(  );
            }
        }

        setListeDirections( strRepresentation );
    }

    /**
     * Retourne la liste des directions sélectionnées à partir de la représentation en base
     * @param plugin le plugin
     * @return la liste des directions sélectionnées
     */
    public List<Direction> getListeDirectionsFromRepresentation( Plugin plugin )
    {
        List<Direction> listeDirections = new ArrayList<Direction>(  );
        StringTokenizer stringTokenizer = new StringTokenizer( getListeDirections(  ), ":" );

        while ( stringTokenizer.hasMoreTokens(  ) )
        {
            listeDirections.add( DirectionHome.findByPrimaryKey( Integer.parseInt( stringTokenizer.nextToken(  ) ),
                    plugin ) );
        }

        return listeDirections;
    }

    /**
     * Retourne la liste des catégories de délibération sélectionnées
     * @return la liste des catégories de délibération sélectionnées
     */
    public String getListeCategoriesDeliberation(  )
    {
        return _strListeCategoriesDeliberation;
    }

    /**
     * Fixe la liste des catégories de délibération sélectionnées
     * @param strListeCategoriesDeliberation la liste des catégories de délibération sélectionnées
     */
    public void setListeCategoriesDeliberation( String strListeCategoriesDeliberation )
    {
        _strListeCategoriesDeliberation = strListeCategoriesDeliberation;
    }

    /**
     * Fixe la représentation en base de la liste des catégories de délibération sélectionnées
     * @param listeCategoriesDeliberation la liste des catégories de délibération sélectionnées
     */
    public void setCategoriesDeliberationFromListe( List<CategorieDeliberation> listeCategoriesDeliberation )
    {
        String strRepresentation = OdsConstants.CONSTANTE_CHAINE_VIDE;

        if ( getListeCategoriesDeliberation(  ) != null )
        {
            for ( CategorieDeliberation categorie : listeCategoriesDeliberation )
            {
                if ( !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strRepresentation ) )
                {
                    strRepresentation += ":";
                }

                strRepresentation += categorie.getIdCategorie(  );
            }
        }

        setListeCategoriesDeliberation( strRepresentation );
    }

    /**
     * Retourne la liste des catégories de délibération sélectionnées à partir de la représentation en base
     * @param plugin le plugin
     * @return la liste des catégories de délibération
     */
    public List<CategorieDeliberation> getListeCategorieDeliberationFromRepresentation( Plugin plugin )
    {
        List<CategorieDeliberation> listeCategoriesDeliberation = new ArrayList<CategorieDeliberation>(  );
        StringTokenizer stringTokenizer = new StringTokenizer( getListeCategoriesDeliberation(  ), ":" );

        while ( stringTokenizer.hasMoreTokens(  ) )
        {
            listeCategoriesDeliberation.add( CategorieDeliberationHome.findByPrimaryKey( Integer.parseInt( 
                        stringTokenizer.nextToken(  ) ), plugin ) );
        }

        return listeCategoriesDeliberation;
    }

    /**
     * Retourne la liste des groupes dépositaires sélectionnés
     * @return la liste des groupes dépositaires sélectionnés
     */
    public String getListeGroupesDepositaires(  )
    {
        return _strListeGroupesDepositaires;
    }

    /**
     * Fixe la liste des groupes dépositaires sélectionnés
     * @param strListeGroupesDepositaires la liste des groupes dépositaires sélectionnés
     */
    public void setListeGroupesDepositaires( String strListeGroupesDepositaires )
    {
        _strListeGroupesDepositaires = strListeGroupesDepositaires;
    }

    /**
     * Fixe la représentation en base de la liste des groupes dépositaires sélectionnés
     * @param listeGroupesDepositaires la liste des groupes dépositaires sélectionnés
     */
    public void setGroupesDepositairesFromListe( List<GroupePolitique> listeGroupesDepositaires )
    {
        String strRepresentation = OdsConstants.CONSTANTE_CHAINE_VIDE;

        if ( listeGroupesDepositaires != null )
        {
            for ( GroupePolitique groupe : listeGroupesDepositaires )
            {
                if ( !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strRepresentation ) )
                {
                    strRepresentation += ":";
                }

                strRepresentation += groupe.getIdGroupe(  );
            }
        }

        setListeGroupesDepositaires( strRepresentation );
    }

    /**
     * Retourne la liste des groupes dépositaires sélectionnés à partir de la représentation en base
     * @param plugin le plugin
     * @return la liste des groupes dépositaires sélectionnés
     */
    public List<GroupePolitique> getListeGroupesDepositairesFromRepresentation( Plugin plugin )
    {
        List<GroupePolitique> listeGroupesDepositaires = new ArrayList<GroupePolitique>(  );
        StringTokenizer stringTokenizer = new StringTokenizer( getListeGroupesDepositaires(  ), ":" );

        while ( stringTokenizer.hasMoreTokens(  ) )
        {
            listeGroupesDepositaires.add( GroupePolitiqueHome.findByPrimaryKey( Integer.parseInt( 
                        stringTokenizer.nextToken(  ) ), plugin ) );
        }

        return listeGroupesDepositaires;
    }

    /**
     * Retourne la liste des élus dépositaires sélectionnés
     * @return la liste des élus dépositaires sélectionnés
     */
    public String getListeElusDepositaires(  )
    {
        return _strListeElusDepositaires;
    }

    /**
     * Fixe la liste des élus dépositaires sélectionnés
     * @param strListeElusDepositaires la liste des élus dépositaires sélectionnés
     */
    public void setListeElusDepositaires( String strListeElusDepositaires )
    {
        _strListeElusDepositaires = strListeElusDepositaires;
    }

    /**
     * Fixe la représentation en base de la liste des élus dépositaires sélectionnés
     * @param listeElusDepositaires la liste des élus sélectionnés
     */
    public void setElusDepositairesFromListe( List<Elu> listeElusDepositaires )
    {
        String strRepresentation = OdsConstants.CONSTANTE_CHAINE_VIDE;

        if ( listeElusDepositaires != null )
        {
            for ( Elu eluDepositaire : listeElusDepositaires )
            {
                if ( !OdsConstants.CONSTANTE_CHAINE_VIDE.equals( strRepresentation ) )
                {
                    strRepresentation += ":";
                }

                strRepresentation += eluDepositaire.getIdElu(  );
            }
        }

        setListeElusDepositaires( strRepresentation );
    }

    /**
     * Retourne la liste des élus dépositaires sélectionnés à partir de la représentation en base
     * @param plugin le plugin
     * @return la liste des élus dépositaires sélectionnés
     */
    public List<Elu> getListeElusDepositairesFromRepresentation( Plugin plugin )
    {
        List<Elu> listeElusDepositaires = new ArrayList<Elu>(  );
        StringTokenizer stringTokenizer = new StringTokenizer( getListeElusDepositaires(  ), ":" );

        while ( stringTokenizer.hasMoreTokens(  ) )
        {
            listeElusDepositaires.add( EluHome.findByPrimaryKey( Integer.parseInt( stringTokenizer.nextToken(  ) ),
                    plugin ) );
        }

        return listeElusDepositaires;
    }

    /**
     * Indique si cette requête doit être notifiée à l'utilisateur si le résultat a changé
     * @return <b>true</b> si l'utilisateur veut être alerté, <b>false</b> sinon
     */
    public boolean isNotifie(  )
    {
        return _bIsNotifie;
    }

    /**
     * Fixe le paramètre de notification
     * @param bIsNotifie la valeur à fixer
     */
    public void setNotifie( boolean bIsNotifie )
    {
        _bIsNotifie = bIsNotifie;
    }

    /**
     * Retuorne la date d'enregistrement de la requête
     * @return la date d'enregistrement de la requête
     */
    public Timestamp getDateCreation(  )
    {
        return _tsDateCreation;
    }

    /**
     * Fixe la date d'enregistrement de la requête
     * @param tsDateCreation la date d'enregistrement de la requête
     */
    public void setDateCreation( Timestamp tsDateCreation )
    {
        _tsDateCreation = tsDateCreation;
    }

    /**
     * Retourne le HTML
     * @return String HTML
     */
    public String getHtml(  )
    {
        return _strHtml;
    }

    /**
     *
     * @param strHtml le code Html
     */
    public void setHtml( String strHtml )
    {
        _strHtml = strHtml;
    }

    /**
     * Retourne le code HTML correspondant à la requête
     *
     * Retourne la requête préparée pour l'affichage dans la liste des requêtes
     * @param plugin Plugin
     * @param locale Local
     */
    public void toHtml( Plugin plugin, Locale locale )
    {
        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        model.put( MARK_LISTE_TYPES_DOC, getListeTypesDocumentFromRepresentation(  ) );
        model.put( MARK_DATE_1, getPremiereDate(  ) );
        model.put( MARK_DATE_2, getDeuxiemeDate(  ) );
        model.put( MARK_MOTS_RECHERCHES, getChampRecherche(  ) );
        model.put( MARK_LISTE_FORMATIONS_CONSEIL, getListeFormationsConseilFromRepresentation( plugin ) );
        model.put( MARK_LISTE_COMMISSIONS, getListeCommissionsFromRepresentation( plugin ) );
        model.put( MARK_LISTE_RAPPORTEURS, getListeRapporteursFromRepresentation( plugin ) );
        model.put( MARK_LISTE_ARRONDISSEMENTS, getListeArrondissementsFromRepresentation( plugin ) );
        model.put( MARK_LISTE_DIRECTIONS, getListeDirectionsFromRepresentation( plugin ) );
        model.put( MARK_LISTE_CATEGORIES, getListeCategorieDeliberationFromRepresentation( plugin ) );
        model.put( MARK_LISTE_GROUPES, getListeGroupesDepositairesFromRepresentation( plugin ) );
        model.put( MARK_LISTE_ELUS, getListeElusDepositairesFromRepresentation( plugin ) );

        HtmlTemplate template = AppTemplateService.getTemplate( URL_TEMPLATE_TO_HTML, locale, model );
        _strHtml = template.getHtml(  );
    }

    /**
     * Retourne un identifiant unique par RequeteUtilisateur
     * @return l'identifiant unique de la requête
     */
    public int hashCode(  )
    {
        return getIdRequete(  );
    }

    /**
     * 2 requêtes utilisateurs sont considérées égales si elles ont le même nom pour le même utilisateur
     * @param requete une requete
     * @return <b>true</b> si l'objet passé en argument est une requête utilisateur et si le couple
         *           (nom de requête / utilisateur) est égal, <b>false</b> sinon
     */
    public boolean equals( Object requete )
    {
        if ( requete instanceof RequeteUtilisateur )
        {
            return ( new Integer( getUserName(  ) ).equals( new Integer( 
                    ( (RequeteUtilisateur) requete ).getUserName(  ) ) ) ) &&
            ( new String( getNomRequete(  ) ).equals( new String( ( (RequeteUtilisateur) requete ).getNomRequete(  ) ) ) );
        }

        return false;
    }

    /**
     * Texte affiché lorsque l'on affiche l'objet (concrètement le nom de la requête)
     * @return le nom de la requête
     */
    public String toString(  )
    {
        return getNomRequete(  );
    }
}
