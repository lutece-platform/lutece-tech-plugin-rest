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
package fr.paris.lutece.plugins.ods.service.search;

import fr.paris.lutece.plugins.ods.business.categoriedeliberation.CategorieDeliberation;
import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.direction.Direction;
import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.elu.EluHome;
import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierFilter;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.fichier.TypeDocumentEnum;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitique;
import fr.paris.lutece.plugins.ods.business.pdd.Arrondissement;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.pdd.PDDFilter;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementFilter;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementHome;
import fr.paris.lutece.plugins.ods.service.search.requete.RequeteUtilisateur;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.portal.service.plugin.Plugin;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * ODSDocSearchService: réalise la recherche multi-critères à partir des données de la base
 */
public abstract class ODSDocSearchService
{
    private static final String TYPE_PROPOSITION = "proposition";
    private static final String TYPE_PROJET = "projet";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //																								//
    //								Projets/Propositions de délibération							//
    //																								//
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Retourne un filtre générique pour tous les PDD
     * @param plugin le plugin
     * @return un filtre pour n'avoir que les PDD inscrits à l'ordre du jour de la prochaine séance
     */
    public abstract PDDFilter getGenericPDDFilter( Plugin plugin );

    /**
     * Retourne la liste des projets et/ou propositions de délibération
     * correspondant aux critères de recherche contenus dans la requête
     * @param requete contient les critères de recherche
     * @param plugin le plugin
     * @return la liste des projets et/ou propositions recherchés
     */
    public abstract Set<PDD> getPDD( RequeteUtilisateur requete, Plugin plugin );

    /**
     * Retourne la liste des PDD selon le type de PDDcontenu dans la requête
     * @param requete la requete
     * @param plugin le plugin
     * @return une collection de PDD
     */
    public Collection<PDD> getPDDFilteredByType( RequeteUtilisateur requete, Plugin plugin )
    {
        // on a déjà vérifié si l'un des 2 est coché, donc on ne gère pas le cas ou aucun n'est coché
        PDDFilter filter = getGenericPDDFilter( plugin );
        // si les 2 sont cochés, cas par défaut (autrement dit pas de filtrage sur le type)
        filter.setType( PDDFilter.ALL_STRING );

        // si l'un des 2 cochés seulement
        if ( !( requete.getListeTypesDocumentFromRepresentation(  ).contains( TYPE_PROPOSITION ) ) )
        {
            filter.setType( PDD.CONSTANTE_TYPE_PROJET );
        }

        if ( ( !requete.getListeTypesDocumentFromRepresentation(  ).contains( TYPE_PROJET ) ) )
        {
            filter.setType( PDD.CONSTANTE_TYPE_PROP );
        }

        return PDDHome.findByFilter( filter, plugin );
    }

    /**
     * Retourne la liste des PDD selon la formation de conseil choisie (municipal ou général)
     * @param requete la requête
     * @param plugin le plugin
     * @return une collection de PDD
     */
    public Collection<PDD> getPDDFilteredByFormationConseil( RequeteUtilisateur requete, Plugin plugin )
    {
        PDDFilter filter = getGenericPDDFilter( plugin );
        // si les 2 sont cochés, pas de filtrage (par défaut)
        filter.setIdFormationConseil( PDDFilter.ALL_INT );

        List<FormationConseil> listeConseils = requete.getListeFormationsConseilFromRepresentation( plugin );

        if ( listeConseils.size(  ) != 2 )
        {
            filter.setIdFormationConseil( listeConseils.get( 0 ).getIdFormationConseil(  ) );
        }

        return PDDHome.findByFilter( filter, plugin );
    }

    /**
     * Retourne la liste des PDD dont la direction principale ou co-emettrice est
     * dans la liste des direction sélectionnées et dont le type est projet
     * @param requete la requête
     * @param plugin le plugin
     * @return une collection de PDD
     */
    public Collection<PDD> getPDDFilteredByDirection( RequeteUtilisateur requete, Plugin plugin )
    {
        PDDFilter filter = getGenericPDDFilter( plugin );
        Set<PDD> pddFilteredByDirection = new HashSet<PDD>( PDDHome.findByFilter( filter, plugin ) );

        // si le type projet n'est pas sélectionné le filtre direction ne s'applique pas
        if ( requete.getListeTypesDocumentFromRepresentation(  ).contains( TYPE_PROJET ) )
        {
            List<Direction> listeDirections = requete.getListeDirectionsFromRepresentation( plugin );

            if ( ( listeDirections.size(  ) != 0 ) )
            {
                pddFilteredByDirection = new HashSet<PDD>(  );

                for ( Direction direction : listeDirections )
                {
                    pddFilteredByDirection.addAll( PDDHome.findByFilterByDirectionOrDirectionCoemetrice( filter,
                            plugin, direction.getIdDirection(  ) ) );
                }

                /* Ajout de toutes les propositions (le filtre ne s'appliquant pas aux propositions) */
                filter.setType( PDD.CONSTANTE_TYPE_PROP );
                pddFilteredByDirection.addAll( PDDHome.findByFilter( filter, plugin ) );
            }
        }

        return pddFilteredByDirection;
    }

    /**
     * Retourne la liste des PDD dont la catégorie de délibération est dans la
     * liste des catégories sélectionnées
     * @param requete la requête
     * @param plugin le plugin
     * @return la liste des PDD
     */
    public Collection<PDD> getPDDFilteredByCategorie( RequeteUtilisateur requete, Plugin plugin )
    {
        PDDFilter filter = getGenericPDDFilter( plugin );
        Set<PDD> pddFilteredByCategorie = new HashSet<PDD>( PDDHome.findByFilter( filter, plugin ) );
        List<CategorieDeliberation> listeCategories = requete.getListeCategorieDeliberationFromRepresentation( plugin );

        if ( listeCategories.size(  ) != 0 )
        {
            pddFilteredByCategorie = new HashSet<PDD>(  );

            for ( CategorieDeliberation categorie : listeCategories )
            {
                filter.setCategorieDeliberation( categorie.getIdCategorie(  ) );
                pddFilteredByCategorie.addAll( PDDHome.findByFilter( filter, plugin ) );
            }
        }

        return pddFilteredByCategorie;
    }

    /**
     * Retourne la liste des PDD dont le groupe dépositaire est dans la liste
     * des groupes sélectionnés et dont le type est proposition
     * @param requete la requête
     * @param plugin le plugin
     * @return une collection de PDD
     */
    public Collection<PDD> getPDDFilteredByGroupe( RequeteUtilisateur requete, Plugin plugin )
    {
        PDDFilter filter = getGenericPDDFilter( plugin );
        Set<PDD> pddFilteredByGroupe = new HashSet<PDD>( PDDHome.findByFilter( filter, plugin ) );

        // si le type proposition n'est pas sélectionné le filtre groupe 
        // dépositaire ne s'applique pas
        if ( requete.getListeTypesDocumentFromRepresentation(  ).contains( TYPE_PROPOSITION ) )
        {
            List<GroupePolitique> listeGroupes = requete.getListeGroupesDepositairesFromRepresentation( plugin );

            if ( listeGroupes.size(  ) != 0 )
            {
                pddFilteredByGroupe = new HashSet<PDD>(  );

                for ( GroupePolitique groupe : listeGroupes )
                {
                    filter.setGroupeDepositaire( OdsConstants.CONSTANTE_CHAINE_VIDE + groupe.getIdGroupe(  ) );
                    pddFilteredByGroupe.addAll( PDDHome.findByFilter( filter, plugin ) );
                }
            }
        }

        return pddFilteredByGroupe;
    }

    /**
     * Retourne la liste des PDD dont l'un des arrondissements est dans la
     * liste des arrondissements selectionnés
     * @param requete la requête
     * @param ensemblePdds l'ensemble des PDD déjà filtrés
     * @param plugin le plugin
     * @return une collection de PDD, null si le filtre n'est pas activé
     */
    public Collection<PDD> getPDDFilteredByArrondissement( RequeteUtilisateur requete, Set<PDD> ensemblePdds,
        Plugin plugin )
    {
        PDDFilter filter = getGenericPDDFilter( plugin );
        Set<PDD> pddsFilteredByArrondissement = new HashSet<PDD>( PDDHome.findByFilter( filter, plugin ) );
        int[] tabArrondissements = requete.getListeArrondissementsFromRepresentation( plugin );

        if ( ( tabArrondissements != null ) && ( tabArrondissements.length != 0 ) )
        {
            pddsFilteredByArrondissement = new HashSet<PDD>(  );

            for ( PDD pdd : ensemblePdds )
            {
                List<Arrondissement> arrDuPdd = PDDHome.findArrondissementsByPDD( pdd.getIdPdd(  ), plugin );

                for ( int nArrondissement : tabArrondissements )
                {
                    Arrondissement arrondissement = new Arrondissement(  );
                    arrondissement.setIdArrondissement( PDDHome.findIdArrondissement( OdsConstants.CONSTANTE_CHAINE_VIDE +
                            nArrondissement, pdd, plugin ) );
                    arrondissement.setArrondissement( nArrondissement );
                    arrondissement.setIdPDD( pdd.getIdPdd(  ) );

                    if ( arrDuPdd.contains( arrondissement ) )
                    {
                        pddsFilteredByArrondissement.add( pdd );

                        break;
                    }
                }
            }
        }

        return pddsFilteredByArrondissement;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //																								//
    //										Amendements												//
    //																								//
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Retourne la liste des amendements recherchés correspondant
     * aux critères de recherche contenus dans la requête
     * @param requete contient les critères de recherche
     * @param plugin le plugin
     * @return la liste des amendements recherchés
     */
    public abstract Set<VoeuAmendement> getAmendements( RequeteUtilisateur requete, Plugin plugin );

    /**
     * retourne un filtre sur VA avec les filtres sur la séance et le type amendement initialisés
     * @param plugin le plugin
     * @return un filtre générique pour les amendements
     */
    public abstract VoeuAmendementFilter getGenericAmendementFilter( Plugin plugin );

    /**
     * retourne la liste des amendements filtrés par formation de conseil
     * @param requete la requête Http
     * @param plugin le plugin
     * @return une collection de VoeuAmendement de type amendement et lettre rectificatives
     */
    public Collection<VoeuAmendement> getAmendementsFilteredByFormationconseil( RequeteUtilisateur requete,
        Plugin plugin )
    {
        VoeuAmendementFilter filter = getGenericAmendementFilter( plugin );

        // par défaut les 2 sont cochés, pas de filtrage
        filter.setIdFormationConseil( VoeuAmendementFilter.ALL_INT );

        List<FormationConseil> listeConseils = requete.getListeFormationsConseilFromRepresentation( plugin );

        // si l'un des 2 seulement
        if ( listeConseils.size(  ) != 2 )
        {
            filter.setIdFormationConseil( listeConseils.get( 0 ).getIdFormationConseil(  ) );
        }

        return VoeuAmendementHome.findVoeuAmendementListByFilter( filter, plugin );
    }

    /**
     * Retourne la liste des voeux selon les commissionssélectionnées
     * @param requete la requête Http
     * @param plugin le plugin
     * @return une collection de VoeuAmendement
     */
    public Collection<VoeuAmendement> getAmendementsFilteredByCommission( RequeteUtilisateur requete, Plugin plugin )
    {
        VoeuAmendementFilter filter = getGenericAmendementFilter( plugin );
        Set<VoeuAmendement> listAmendementFilteredByCommission = new HashSet<VoeuAmendement>( VoeuAmendementHome.findVoeuAmendementListByFilter( 
                    filter, plugin ) );
        List<Commission> listCommissions = requete.getListeCommissionsFromRepresentation( plugin );

        if ( listCommissions.size(  ) != 0 )
        {
            listAmendementFilteredByCommission = new HashSet<VoeuAmendement>(  );

            for ( Commission commission : listCommissions )
            {
                filter.setIdCommission( commission.getIdCommission(  ) );
                listAmendementFilteredByCommission.addAll( VoeuAmendementHome.findVoeuAmendementListByFilter( filter,
                        plugin ) );
            }
        }

        return listAmendementFilteredByCommission;
    }

    /**
     * Retourne la liste des amendements filtrés par élu ou groupe dépositaire
     * @param requete la requête Http
     * @param plugin le plugin
     * @return une collection d'amendemants
     */
    public Collection<VoeuAmendement> getAmendementsFilteredByDepositaire( RequeteUtilisateur requete, Plugin plugin )
    {
        VoeuAmendementFilter filter = getGenericAmendementFilter( plugin );
        Set<VoeuAmendement> amendementsFilteredByDepositaires = new HashSet<VoeuAmendement>( VoeuAmendementHome.findVoeuAmendementListByFilter( 
                    filter, plugin ) );
        Set<VoeuAmendement> amendementsToRemove = new HashSet<VoeuAmendement>(  );
        List<GroupePolitique> listeGroupes = requete.getListeGroupesDepositairesFromRepresentation( plugin );

        if ( listeGroupes.size(  ) != 0 )
        {
            amendementsToRemove.addAll( amendementsFilteredByDepositaires );

            for ( GroupePolitique groupe : listeGroupes )
            {
                List<Elu> elus = EluHome.findEluListbyIdGroupe( groupe.getIdGroupe(  ), plugin );

                for ( Elu elu : elus )
                {
                    filter.setIdElu( elu.getIdElu(  ) );
                    amendementsToRemove.removeAll( VoeuAmendementHome.findVoeuAmendementListByFilter( filter, plugin ) );
                }
            }
        }

        List<Elu> listeElus = requete.getListeElusDepositairesFromRepresentation( plugin );

        if ( listeElus.size(  ) != 0 )
        {
            if ( amendementsToRemove.isEmpty(  ) )
            {
                amendementsToRemove.addAll( amendementsFilteredByDepositaires );
            }

            for ( Elu elu : listeElus )
            {
                filter.setIdElu( elu.getIdElu(  ) );
                amendementsToRemove.removeAll( VoeuAmendementHome.findVoeuAmendementListByFilter( filter, plugin ) );
            }
        }

        amendementsFilteredByDepositaires.removeAll( amendementsToRemove );

        return amendementsFilteredByDepositaires;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //																								//
    //											Voeux												//
    //																								//
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Retourne la liste des voeux correspondant
     * aux critères de recherche contenus dans la requête
     * @param requete contient les critères de recherche
     * @param plugin le plugin
     * @return la liste de voeux recherchés
     */
    public abstract Set<VoeuAmendement> getVoeux( RequeteUtilisateur requete, Plugin plugin );

    /**
     * Retourne le filtre générique sur les VoeuAmendements pour n'avoir que les voeux
     * @param plugin le plugin
     * @return le filtre pour n'avoir que les voeux inscrits à l'ordre du jour de la prochaine séance
     */
    public abstract VoeuAmendementFilter getGenericVoeuFilter( Plugin plugin );

    /**
     * Retourne la liste des voeux selon les formations de conseil choisies
     * @param requete la requête Http
     * @param plugin le plugin
     * @return une collection de VoeuAmendement
     */
    public Collection<VoeuAmendement> getVoeuxFilteredByFormationConseil( RequeteUtilisateur requete, Plugin plugin )
    {
        VoeuAmendementFilter filter = getGenericVoeuFilter( plugin );
        // si les 2 sont cochés, pas de filtrage (par défaut)
        filter.setIdFormationConseil( VoeuAmendementFilter.ALL_INT );

        List<FormationConseil> listeConseils = requete.getListeFormationsConseilFromRepresentation( plugin );

        if ( listeConseils.size(  ) != 2 )
        {
            filter.setIdFormationConseil( listeConseils.get( 0 ).getIdFormationConseil(  ) );
        }

        return VoeuAmendementHome.findVoeuAmendementListByFilter( filter, plugin );
    }

    /**
     * Retourne la liste des voeux selon les commissions choisies
     * @param requete la requête Http
     * @param plugin le plugin
     * @return une collection de voeux inscrits à l'ordre du jour de la prochaine séance, ou null si le filtre n'est pas actif
     */
    public Collection<VoeuAmendement> getVoeuxFilteredByCommission( RequeteUtilisateur requete, Plugin plugin )
    {
        VoeuAmendementFilter filter = getGenericVoeuFilter( plugin );
        Set<VoeuAmendement> listVoeuFilteredByCommission = new HashSet<VoeuAmendement>( VoeuAmendementHome.findVoeuAmendementListByFilter( 
                    filter, plugin ) );
        List<Commission> listCommissions = requete.getListeCommissionsFromRepresentation( plugin );

        if ( listCommissions.size(  ) != 0 )
        {
            listVoeuFilteredByCommission = new HashSet<VoeuAmendement>(  );

            for ( Commission commission : listCommissions )
            {
                filter.setIdCommission( commission.getIdCommission(  ) );
                listVoeuFilteredByCommission.addAll( VoeuAmendementHome.findVoeuAmendementListByFilter( filter, plugin ) );
            }
        }

        return listVoeuFilteredByCommission;
    }

    /**
     * Retourne la liste des voeux filtré par élu ou groupe dépositaire; un filtrage par groupe est une extension
     * d'un filtrage par élu, avec tous les élus du groupe
     * @param requete la requête Http
     * @param plugin le plugin
     * @return une collection de VoeuAmendement
     */
    public Collection<VoeuAmendement> getVoeuxFilteredByDepositaire( RequeteUtilisateur requete, Plugin plugin )
    {
        VoeuAmendementFilter filter = getGenericVoeuFilter( plugin );
        Set<VoeuAmendement> voeuxFilteredByDepositaire = new HashSet<VoeuAmendement>(  );
        voeuxFilteredByDepositaire.addAll( VoeuAmendementHome.findVoeuAmendementListByFilter( filter, plugin ) );

        Set<VoeuAmendement> voeuxToRemove = new HashSet<VoeuAmendement>(  );
        List<GroupePolitique> listeGroupes = requete.getListeGroupesDepositairesFromRepresentation( plugin );

        if ( listeGroupes.size(  ) != 0 )
        {
            voeuxToRemove.addAll( voeuxFilteredByDepositaire );

            for ( GroupePolitique groupe : listeGroupes )
            {
                List<Elu> elus = EluHome.findEluListbyIdGroupe( groupe.getIdGroupe(  ), plugin );

                for ( Elu elu : elus )
                {
                    filter.setIdElu( elu.getIdElu(  ) );
                    voeuxToRemove.removeAll( VoeuAmendementHome.findVoeuAmendementListByFilter( filter, plugin ) );
                }
            }
        }

        List<Elu> listeElus = requete.getListeElusDepositairesFromRepresentation( plugin );

        if ( listeElus.size(  ) != 0 )
        {
            if ( voeuxToRemove.isEmpty(  ) )
            {
                voeuxToRemove.addAll( voeuxFilteredByDepositaire );
            }

            for ( Elu elu : listeElus )
            {
                filter.setIdElu( elu.getIdElu(  ) );
                voeuxToRemove.removeAll( VoeuAmendementHome.findVoeuAmendementListByFilter( filter, plugin ) );
            }
        }

        voeuxFilteredByDepositaire.removeAll( voeuxToRemove );

        return voeuxFilteredByDepositaire;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //																								//
    //											Séances												//
    //																								//
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Retourne la liste des séances comprises entre les 2 dates de la requête
     * @param requete la requete utilisateur
     * @param plugin le plugin
     * @return la liste des séances
     */
    public List<Seance> getSeances( RequeteUtilisateur requete, Plugin plugin )
    {
        Timestamp tsDate1 = requete.getPremiereDate(  );

        if ( tsDate1 == null )
        {
            return SeanceHome.findOldSeance( plugin );
        }

        Timestamp tsDate2 = requete.getDeuxiemeDate(  );

        if ( ( tsDate2 == null ) || tsDate2.after( SeanceHome.getDerniereSeance( plugin ).getDateSeance(  ) ) )
        {
            tsDate2 = SeanceHome.getDerniereSeance( plugin ).getDateSeance(  );
        }

        return SeanceHome.findSeanceWidthFilterList( tsDate1, tsDate2, plugin );
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //																								//
    //										Autres documents										//
    //																								//
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Retourne la liste de documents des séances voulues de type Convocation du maire, Document libre, Liste intégrale des questions,
     * Relevé de la conférence d'organisation, Résumé des questions, Désignations, Compte rendu et Délibérations de désignation
     * @param requete la requete utilisateur
     * @param listFichiers la liste des fichiers indexés dans les archives
     * @param plugin le plugin
     * @return la liste des autres documents de séance
     */
    public List<Fichier> getAutresDocuments( RequeteUtilisateur requete, Set<Fichier> listFichiers, Plugin plugin )
    {
        List<Fichier> listAutresDocuments = new ArrayList<Fichier>(  );
        FichierFilter filter = new FichierFilter(  );
        filter.setPublication( 1 );
        filter.addInTypeDoc( TypeDocumentEnum.CONVOCATION_DU_MAIRE );
        filter.addInTypeDoc( TypeDocumentEnum.DOCUMENT_LIBRE );
        filter.addInTypeDoc( TypeDocumentEnum.LISTE_INTEGRALE_DES_QUESTIONS );
        filter.addInTypeDoc( TypeDocumentEnum.RELEVE_DE_LA_CONFERENCE_DORGANISATION );
        filter.addInTypeDoc( TypeDocumentEnum.RESUME_DES_QUESTIONS );
        filter.addInTypeDoc( TypeDocumentEnum.DESIGNATION );
        filter.addInTypeDoc( TypeDocumentEnum.COMPTE_RENDU );
        filter.addInTypeDoc( TypeDocumentEnum.DELIBERATION_DE_DESIGNATION );

        List<Seance> listSeances = getSeances( requete, plugin );

        for ( Seance seance : listSeances )
        {
            filter.setIdSeance( seance.getIdSeance(  ) );
            listAutresDocuments.addAll( FichierHome.findByFilter( filter, plugin ) );
        }

        if ( listFichiers != null )
        {
            listAutresDocuments.retainAll( listFichiers );
        }

        return listAutresDocuments;
    }
}
