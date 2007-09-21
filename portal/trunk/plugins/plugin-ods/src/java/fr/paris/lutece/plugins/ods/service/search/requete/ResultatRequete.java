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

import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;

import java.util.List;


/**
 * ResultatRequete
 */
public class ResultatRequete
{
    private RequeteUtilisateur _requeteUtilisateur;
    private List<PDD> _listPDDs;
    private List<VoeuAmendement> _listAmendements;
    private List<VoeuAmendement> _listVoeux;
    private List<Seance> _listSeances;
    private List<Fichier> _listAutresDocuments;

    /**
     * Retourne la requete utilisateur dont elle est le résultat
     * @return la requete utilisateur dont elle est le résultat
     */
    public RequeteUtilisateur getRequeteUtilisateur(  )
    {
        return _requeteUtilisateur;
    }

    /**
     * Fixe la requete utilisateur dont elle est le résultat
     * @param requeteUtilisateur la requete utilisateur dont elle est le résultat
     */
    public void setRequeteUtilisateur( RequeteUtilisateur requeteUtilisateur )
    {
        _requeteUtilisateur = requeteUtilisateur;
    }

    /**
     * Retourne la liste des PDDs de la requete triés par référence
     * @return la liste des PDDs de la requete
     */
    public List<PDD> getListePDDs(  )
    {
        return _listPDDs;
    }

    /**
     * Fixe la liste des PDDs de la requete
     * @param listPDDs la liste des PDDs de la requete
     */
    public void setListePDDs( List<PDD> listPDDs )
    {
        _listPDDs = listPDDs;
    }

    /**
     * Retourne la liste des amendements de la requete triés par numéro
     * @return la liste des amendements de la requete
     */
    public List<VoeuAmendement> getListeAmendements(  )
    {
        return _listAmendements;
    }

    /**
     * Fixe la liste des amendements de la requete
     * @param listAmendements la liste des amendements de la requete
     */
    public void setListeAmendements( List<VoeuAmendement> listAmendements )
    {
        _listAmendements = listAmendements;
    }

    /**
     * Retourne la liste des voeux de la requete triés par numéro
     * @return la liste des voeux de la requete
     */
    public List<VoeuAmendement> getListeVoeux(  )
    {
        return _listVoeux;
    }

    /**
     * Fixe la liste des voeux de la requete
     * @param listVoeux la liste des voeux de la requete
     */
    public void setListeVoeux( List<VoeuAmendement> listVoeux )
    {
        _listVoeux = listVoeux;
    }

    /**
     * Retourne la liste des séances de la requete triées par date de séance (de la plus récente à la plus ancienne)
     * @return la liste des séances de la requete
     */
    public List<Seance> getListeSeances(  )
    {
        return _listSeances;
    }

    /**
     * Fixe la liste des séances de la requete
     * @param listSeances la liste des séances de la requete
     */
    public void setListeSeances( List<Seance> listSeances )
    {
        _listSeances = listSeances;
    }

    /**
     * Retourne la liste des autres documents de séance de la requete
     * @return la liste des autres documents de séance de la requete
     */
    public List<Fichier> getListeAutresDocuments(  )
    {
        return _listAutresDocuments;
    }

    /**
     * Fixe la liste des autres documents de séance de la requete
     * @param listAutresDocuments la liste des autres documents de séance de la requete
     */
    public void setListeAutresDocuments( List<Fichier> listAutresDocuments )
    {
        _listAutresDocuments = listAutresDocuments;
    }

    /**
     * Ajoute les résultats d'une autre requête à ce résultat
     * @param resultat l'autre résultat de requête
     */
    public void addAll( ResultatRequete resultat )
    {
        /*
         * Ajout des nouveaux PDDs
         */
        if ( _listPDDs == null )
        {
            _listPDDs = resultat.getListePDDs(  );
        }
        else if ( resultat.getListePDDs(  ) != null )
        {
            List<PDD> listPdds = resultat.getListePDDs(  );
            boolean bAjouter;

            for ( PDD pdd1 : listPdds )
            {
                bAjouter = true;

                for ( PDD pdd2 : _listPDDs )
                {
                    if ( pdd1.equals( pdd2 ) )
                    {
                        bAjouter = false;

                        break;
                    }
                }

                if ( bAjouter )
                {
                    _listPDDs.add( pdd1 );
                }
            }
        }

        /*
         * Ajout des nouveaux Amendements
         */
        if ( _listAmendements == null )
        {
            _listAmendements = resultat.getListeAmendements(  );
        }
        else if ( resultat.getListeAmendements(  ) != null )
        {
            List<VoeuAmendement> listAmendements = resultat.getListeAmendements(  );
            boolean bAjouter;

            for ( VoeuAmendement amendement1 : listAmendements )
            {
                bAjouter = true;

                for ( VoeuAmendement amendement2 : _listAmendements )
                {
                    if ( amendement1.equals( amendement2 ) )
                    {
                        bAjouter = false;

                        break;
                    }
                }

                if ( bAjouter )
                {
                    _listAmendements.add( amendement1 );
                }
            }
        }

        /*
         * Ajout des nouveaux Voeux
         */
        if ( _listVoeux == null )
        {
            _listVoeux = resultat.getListeVoeux(  );
        }
        else if ( resultat.getListeVoeux(  ) != null )
        {
            List<VoeuAmendement> listVoeux = resultat.getListeVoeux(  );
            boolean bAjouter;

            for ( VoeuAmendement voeu1 : listVoeux )
            {
                bAjouter = true;

                for ( VoeuAmendement voeu2 : _listVoeux )
                {
                    if ( voeu1.equals( voeu2 ) )
                    {
                        bAjouter = false;

                        break;
                    }
                }

                if ( bAjouter )
                {
                    _listVoeux.add( voeu1 );
                }
            }
        }

        /*
         * Ajout des nouvelles séances
         */
        if ( _listSeances == null )
        {
            _listSeances = resultat.getListeSeances(  );
        }
        else if ( resultat.getListeSeances(  ) != null )
        {
            _listSeances.addAll( resultat.getListeSeances(  ) );
        }

        /*
         * Ajout des nouveaux autres documents
         */
        if ( _listAutresDocuments == null )
        {
            _listAutresDocuments = resultat.getListeAutresDocuments(  );
        }
        else if ( resultat.getListeAutresDocuments(  ) != null )
        {
            _listAutresDocuments.addAll( resultat.getListeAutresDocuments(  ) );
        }
    }

    /**
     *
     * @return true si aucun resultat , false sinon
     */
    public boolean isEmpty(  )
    {
        boolean bRetour = true;

        if ( ( _listPDDs != null ) && !_listPDDs.isEmpty(  ) )
        {
            bRetour = false;
        }

        else if ( ( _listAmendements != null ) && !_listAmendements.isEmpty(  ) )
        {
            bRetour = false;
        }

        else if ( ( _listVoeux != null ) && !_listVoeux.isEmpty(  ) )
        {
            bRetour = false;
        }

        else if ( ( _listSeances != null ) && !_listSeances.isEmpty(  ) )
        {
            bRetour = false;
        }

        else if ( ( _listAutresDocuments != null ) && !_listAutresDocuments.isEmpty(  ) )
        {
            bRetour = false;
        }

        return bRetour;
    }
}
