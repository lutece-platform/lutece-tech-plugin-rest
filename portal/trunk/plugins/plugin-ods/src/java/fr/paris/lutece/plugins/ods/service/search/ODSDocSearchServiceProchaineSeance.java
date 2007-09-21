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

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.fichier.FichierFilter;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseilEnum;
import fr.paris.lutece.plugins.ods.business.ordredujour.EntreeOrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.EntreeOrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourFilter;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJourHome;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.pdd.PDDFilter;
import fr.paris.lutece.plugins.ods.business.pdd.PDDHome;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementFilter;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendementHome;
import fr.paris.lutece.plugins.ods.service.search.requete.RequeteUtilisateur;
import fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean;
import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 *
 * ODSDocSearchServiceProchaineSeance
 *
 */
public class ODSDocSearchServiceProchaineSeance extends ODSDocSearchService
{
    private static final String CONSTANTE_TYPE_ALLV = "VRVNR";

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //																								//
    //								Projets/Propositions de délibération							//
    //																								//
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Retourne un filtre générique pour tous les PDD
     * @param plugin le plugin
     * @return un filtre pour n'avoir que les PDD inscrits à l'ordre du jour définitif de la prochaine séance
     */
    public PDDFilter getGenericPDDFilter( Plugin plugin )
    {
        PDDFilter genericPDDFilter = new PDDFilter(  );
        genericPDDFilter.setPublication( 1 );
        genericPDDFilter.setProchaineSeance( SeanceHome.getProchaineSeance( plugin ).getIdSeance(  ) );

        return genericPDDFilter;
    }

    /**
     * Retourne la liste des projets et/ou propositions de délibération
     * correspondant aux critères de recherche contenus dans la requête
     * @param requete contient les critères de recherche
     * @param plugin le plugin
     * @return la liste des projets et/ou propositions recherchés
     */
    public Set<PDD> getPDD( RequeteUtilisateur requete, Plugin plugin )
    {
        // Le test sur la validité de la requête a déjà été fait, on suppose 
        // donc que l'on a plus à tester le type et la formation de conseil
        Set<PDD> ensemblePdds = new HashSet<PDD>(  );
        // initialisation: critères génériques
        ensemblePdds.addAll( PDDHome.findByFilter( getGenericPDDFilter( plugin ), plugin ) );
        ensemblePdds.retainAll( getPDDFilteredByType( requete, plugin ) );
        ensemblePdds.retainAll( getPDDFilteredByFormationConseil( requete, plugin ) );
        ensemblePdds.retainAll( getPDDFilteredByCommission( requete, plugin ) );
        ensemblePdds.retainAll( getPDDFilteredByRapporteur( requete, plugin ) );
        ensemblePdds.retainAll( getPDDFilteredByDirection( requete, plugin ) );
        ensemblePdds.retainAll( getPDDFilteredByCategorie( requete, plugin ) );
        ensemblePdds.retainAll( getPDDFilteredByGroupe( requete, plugin ) );
        ensemblePdds.retainAll( getPDDFilteredByArrondissement( requete, ensemblePdds, plugin ) );

        for ( PDD pdd : ensemblePdds )
        {
            FichierFilter filter = new FichierFilter(  );
            filter.setPDD( pdd.getIdPdd(  ) );
            pdd.setFichiers( FichierHome.findByFilter( filter, plugin ) );
        }

        return ensemblePdds;
    }

    /**
     * Retourne la liste des PDD dont la commission est dans la liste des commissions sélectionnées
     * @param requete la requete utilisateur
     * @param plugin le plugin
     * @return une collection de PDD
     */
    public Collection<PDD> getPDDFilteredByCommission( RequeteUtilisateur requete, Plugin plugin )
    {
        Set<PDD> listPddsFilteredByCommission = new HashSet<PDD>( PDDHome.findByFilter( getGenericPDDFilter( plugin ),
                    plugin ) );
        List<Commission> listCommissions = requete.getListeCommissionsFromRepresentation( plugin );

        if ( listCommissions.size(  ) != 0 )
        {
            listPddsFilteredByCommission = new HashSet<PDD>(  );

            OrdreDuJourFilter ordreDuJourFilter = new OrdreDuJourFilter(  );
            ordreDuJourFilter.setIdSeance( SeanceHome.getProchaineSeance( plugin ).getIdSeance(  ) );
            ordreDuJourFilter.setIdPublie( 1 );
            ordreDuJourFilter.setIdSauvegarde( 0 );

            List<EntreeOrdreDuJour> listEntreesOdj;
            ordreDuJourFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) );

            OrdreDuJour odjCG = OrdreDuJourHome.findOdjReferenceByFilter( ordreDuJourFilter, plugin );

            if ( odjCG != null )
            {
                listEntreesOdj = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( odjCG.getIdOrdreDuJour(  ),
                        plugin );

                for ( EntreeOrdreDuJour entreeOdj : listEntreesOdj )
                {
                    List<Commission> listCommissionsEntree = entreeOdj.getCommissions(  );

                    for ( Commission commission : listCommissions )
                    {
                        if ( listCommissionsEntree.contains( commission ) && ( entreeOdj.getPdd(  ) != null ) )
                        {
                            listPddsFilteredByCommission.add( entreeOdj.getPdd(  ) );
                        }
                    }
                }
            }

            ordreDuJourFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILMUNICIPAL.getId(  ) );

            OrdreDuJour odjCM = OrdreDuJourHome.findOdjReferenceByFilter( ordreDuJourFilter, plugin );

            if ( odjCM != null )
            {
                listEntreesOdj = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( odjCM.getIdOrdreDuJour(  ),
                        plugin );

                for ( EntreeOrdreDuJour entreeOdj : listEntreesOdj )
                {
                    List<Commission> listCommissionsEntree = entreeOdj.getCommissions(  );

                    for ( Commission commission : listCommissions )
                    {
                        if ( listCommissionsEntree.contains( commission ) && ( entreeOdj.getPdd(  ) != null ) )
                        {
                            listPddsFilteredByCommission.add( entreeOdj.getPdd(  ) );
                        }
                    }
                }
            }
        }

        return listPddsFilteredByCommission;
    }

    /**
     * Retourne la liste des PDDs dont l'un des rapporteurs est dans la liste des rapporteurs sélectionnés
     * @param requete la requete
     * @param plugin le plugin
     * @return une collection de PDD
     */
    public Collection<PDD> getPDDFilteredByRapporteur( RequeteUtilisateur requete, Plugin plugin )
    {
        Set<PDD> listPddsFilteredByRapporteurs = new HashSet<PDD>( PDDHome.findByFilter( getGenericPDDFilter( plugin ),
                    plugin ) );
        List<Elu> listRapporteurs = requete.getListeRapporteursFromRepresentation( plugin );

        if ( listRapporteurs.size(  ) != 0 )
        {
            listPddsFilteredByRapporteurs = new HashSet<PDD>(  );

            List<EntreeOrdreDuJour> listEntreesOdj;
            List<Elu> listRapporteursEntree;
            OrdreDuJour odjCG;
            OrdreDuJour odjCM;

            OrdreDuJourFilter ordreDuJourFilter = new OrdreDuJourFilter(  );
            ordreDuJourFilter.setIdPublie( 1 );
            ordreDuJourFilter.setIdSauvegarde( 0 );
            ordreDuJourFilter.setIdSeance( SeanceHome.getProchaineSeance( plugin ).getIdSeance(  ) );

            ordreDuJourFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) );
            odjCG = OrdreDuJourHome.findOdjReferenceByFilter( ordreDuJourFilter, plugin );

            if ( odjCG != null )
            {
                listEntreesOdj = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( odjCG.getIdOrdreDuJour(  ),
                        plugin );

                for ( EntreeOrdreDuJour entreeOdj : listEntreesOdj )
                {
                    listRapporteursEntree = entreeOdj.getElus(  );

                    for ( Elu rapporteur : listRapporteurs )
                    {
                        if ( listRapporteursEntree.contains( rapporteur ) && ( entreeOdj.getPdd(  ) != null ) )
                        {
                            listPddsFilteredByRapporteurs.add( entreeOdj.getPdd(  ) );
                        }
                    }
                }
            }

            ordreDuJourFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILMUNICIPAL.getId(  ) );
            odjCM = OrdreDuJourHome.findOdjReferenceByFilter( ordreDuJourFilter, plugin );

            if ( odjCM != null )
            {
                listEntreesOdj = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( odjCM.getIdOrdreDuJour(  ),
                        plugin );

                for ( EntreeOrdreDuJour entreeOdj : listEntreesOdj )
                {
                    listRapporteursEntree = entreeOdj.getElus(  );

                    for ( Elu rapporteur : listRapporteurs )
                    {
                        if ( listRapporteursEntree.contains( rapporteur ) && ( entreeOdj.getPdd(  ) != null ) )
                        {
                            listPddsFilteredByRapporteurs.add( entreeOdj.getPdd(  ) );
                        }
                    }
                }
            }
        }

        return listPddsFilteredByRapporteurs;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //																								//
    //										Amendements												//
    //																								//
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * retourne un filtre sur VA avec les filtres sur la séance et le type amendement initialisés
     * @param plugin le plugin
     * @return un filtre générique pour les amendements
     */
    public VoeuAmendementFilter getGenericAmendementFilter( Plugin plugin )
    {
        VoeuAmendementFilter genericAmendementFilter = new VoeuAmendementFilter(  );
        genericAmendementFilter.setIdSeance( SeanceHome.getProchaineSeance( plugin ).getIdSeance(  ) );
        genericAmendementFilter.setIdPublie( 1 );
        genericAmendementFilter.setType( VoeuAmendementJspBean.CONSTANTE_TYPE_A );

        return genericAmendementFilter;
    }

    /**
     * Retourne la liste des amendements recherchés correspondant
     * aux critères de recherche contenus dans la requête
     * @param requete contient les critères de recherche
     * @param plugin le plugin
     * @return la liste des amendements recherchés
     */
    public Set<VoeuAmendement> getAmendements( RequeteUtilisateur requete, Plugin plugin )
    {
        Set<VoeuAmendement> ensembleAmendements = new HashSet<VoeuAmendement>(  );

        ensembleAmendements.addAll( VoeuAmendementHome.findVoeuAmendementListByFilter( getGenericAmendementFilter( 
                    plugin ), plugin ) );
        ensembleAmendements.retainAll( getAmendementsFilteredByFormationconseil( requete, plugin ) );
        ensembleAmendements.retainAll( getAmendementsFilteredByCommission( requete, plugin ) );
        ensembleAmendements.retainAll( getAmendementsFilteredByDepositaire( requete, plugin ) );

        for ( VoeuAmendement amendement : ensembleAmendements )
        {
            amendement.setFichier( FichierHome.findByPrimaryKey( amendement.getFichier(  ).getId(  ), plugin ) );
            amendement.setPdds( VoeuAmendementHome.findPddListbyIdVoeuAmendement( amendement.getIdVoeuAmendement(  ),
                    plugin ) );
        }

        return ensembleAmendements;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //																								//
    //											Voeux												//
    //																								//
    //////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Retourne le filtre générique sur les VoeuAmendements pour n'avoir que les voeux
     * @param plugin le plugin
     * @return le filtre pour n'avoir que les voeux inscrits à l'ordre du jour de la prochaine séance
     */
    public VoeuAmendementFilter getGenericVoeuFilter( Plugin plugin )
    {
        VoeuAmendementFilter genericVoeuFilter = new VoeuAmendementFilter(  );
        genericVoeuFilter.setIdSeance( SeanceHome.getProchaineSeance( plugin ).getIdSeance(  ) );
        genericVoeuFilter.setIdPublie( 1 );
        genericVoeuFilter.setType( CONSTANTE_TYPE_ALLV );

        return genericVoeuFilter;
    }

    /**
     * Retourne la liste des voeux correspondant
     * aux critères de recherche contenus dans la requête
     * @param requete contient les critères de recherche
     * @param plugin le plugin
     * @return la liste de voeux recherchés
     */
    public Set<VoeuAmendement> getVoeux( RequeteUtilisateur requete, Plugin plugin )
    {
        Set<VoeuAmendement> ensembleVoeux = new HashSet<VoeuAmendement>(  );

        ensembleVoeux.addAll( VoeuAmendementHome.findVoeuAmendementListByFilter( getGenericVoeuFilter( plugin ), plugin ) );
        ensembleVoeux.retainAll( getVoeuxFilteredByFormationConseil( requete, plugin ) );
        ensembleVoeux.retainAll( getVoeuxFilteredByCommission( requete, plugin ) );
        ensembleVoeux.retainAll( getVoeuxFilteredByRapporteur( requete, plugin ) );
        ensembleVoeux.retainAll( getVoeuxFilteredByDepositaire( requete, plugin ) );

        for ( VoeuAmendement voeu : ensembleVoeux )
        {
            voeu.setFichier( FichierHome.findByPrimaryKey( voeu.getFichier(  ).getId(  ), plugin ) );
            voeu.setPdds( VoeuAmendementHome.findPddListbyIdVoeuAmendement( voeu.getIdVoeuAmendement(  ), plugin ) );
        }

        return ensembleVoeux;
    }

    /**
     * Retourne les voeux non rattachés inscrit à l'ordre du jour définitif pour un des rapporteurs sélectionné
     * @param requete la requete
     * @param plugin le plugin
     * @return une collection de VoeuAmendement
     */
    public Collection<VoeuAmendement> getVoeuxFilteredByRapporteur( RequeteUtilisateur requete, Plugin plugin )
    {
        VoeuAmendementFilter filter = getGenericVoeuFilter( plugin );
        Set<VoeuAmendement> listVoeuxFilteredByRapporteurs = new HashSet<VoeuAmendement>( VoeuAmendementHome.findVoeuAmendementListByFilter( 
                    filter, plugin ) );
        List<Elu> listRapporteurs = requete.getListeRapporteursFromRepresentation( plugin );

        if ( listRapporteurs.size(  ) != 0 )
        {
            listVoeuxFilteredByRapporteurs = new HashSet<VoeuAmendement>(  );

            OrdreDuJour odjCG;
            OrdreDuJour odjCM;
            List<EntreeOrdreDuJour> listEntreesOdj;
            List<Elu> listRapporteursEntree;
            OrdreDuJourFilter ordreDuJourFilter = new OrdreDuJourFilter(  );

            ordreDuJourFilter.setIdPublie( 1 );
            ordreDuJourFilter.setIdSauvegarde( 0 );
            ordreDuJourFilter.setIdSeance( SeanceHome.getProchaineSeance( plugin ).getIdSeance(  ) );

            ordreDuJourFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILGENERAL.getId(  ) );
            odjCG = OrdreDuJourHome.findOdjReferenceByFilter( ordreDuJourFilter, plugin );

            if ( odjCG != null )
            {
                listEntreesOdj = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( odjCG.getIdOrdreDuJour(  ),
                        plugin );

                for ( EntreeOrdreDuJour entreeOdj : listEntreesOdj )
                {
                    listRapporteursEntree = entreeOdj.getElus(  );

                    for ( Elu rapporteur : listRapporteurs )
                    {
                        if ( listRapporteursEntree.contains( rapporteur ) && ( entreeOdj.getVoeuAmendement(  ) != null ) )
                        {
                            listVoeuxFilteredByRapporteurs.add( entreeOdj.getVoeuAmendement(  ) );
                        }
                    }
                }
            }

            ordreDuJourFilter.setIdFormationConseil( FormationConseilEnum.FORMATIONCONSEILMUNICIPAL.getId(  ) );
            odjCM = OrdreDuJourHome.findOdjReferenceByFilter( ordreDuJourFilter, plugin );

            if ( odjCM != null )
            {
                listEntreesOdj = EntreeOrdreDuJourHome.findEntreesListByIdOrdreDuJour( odjCM.getIdOrdreDuJour(  ),
                        plugin );

                for ( EntreeOrdreDuJour entreeOdj : listEntreesOdj )
                {
                    listRapporteursEntree = entreeOdj.getElus(  );

                    for ( Elu rapporteur : listRapporteurs )
                    {
                        if ( listRapporteursEntree.contains( rapporteur ) && ( entreeOdj.getVoeuAmendement(  ) != null ) )
                        {
                            listVoeuxFilteredByRapporteurs.add( entreeOdj.getVoeuAmendement(  ) );
                        }
                    }
                }
            }
        }

        return listVoeuxFilteredByRapporteurs;
    }
}
