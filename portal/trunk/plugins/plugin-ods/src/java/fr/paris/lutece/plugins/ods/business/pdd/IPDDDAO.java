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
package fr.paris.lutece.plugins.ods.business.pdd;

import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.portal.service.plugin.Plugin;

import java.sql.Timestamp;

import java.util.List;


/**
 * PDDDAO est responsable de la communication avec la BDD
 *
 */
public interface IPDDDAO
{
    /**
    * renvoie la liste des pdd de la prochaine seance
    * @param plugin Plugin
    * @param filter PDDFilter
    * @return List<PDD> liste d'objets Fichier
    */
    List<PDD> selectByFilter( PDDFilter filter, Plugin plugin );

    /**
     * renvoie la liste des pdd de la prochaine seance
     * @param plugin Plugin
     * @param filter PDDFilter
     * @param  nIdDirection nIdDirection
     * @return List<PDD> liste d'objets Fichier
     */
    List<PDD> selectByFilterByDirectionOrDirectionCoemetrice( PDDFilter filter, Plugin plugin, int nIdDirection );

    /**
     * renvoie le pdd en fonction de l'id passe en parametre
     * @param nKey int id du PDD
     * @param plugin Plugin
     * @return PDD le pdd qui a ete trouvé
     */
    PDD selectByPrimaryKey( int nKey, Plugin plugin );

    /**
     * Crée un nouveau PDD à partir de l’objet pdd passé en paramètre
     * @param pdd la Projet de deliberation à insérer
     * @param plugin Plugin
     * @return ProjetDeDeliberation pdd creer
     */
    PDD insert( PDD pdd, Plugin plugin );

    /**
     * Modifie un PDD à partir de l’objet pdd passé en paramètre
     * @param pdd la Projet de deliberation à modifier
     * @param plugin Plugin
     */
    void store( PDD pdd, Plugin plugin );

    /**
     * Supprime le PDD dont l'id est celui passé en paramètre
     * @param pdd le pdd
     * @param plugin Plugin
     */
    void delete( PDD pdd, Plugin plugin );

    /**
     * Permet de publier ou de dépublier un pdd
     * Publication d'un pdd
     * Si isPublier égale à TRUE, alors la méthode <b>publie</b> le pdd et incrémente la version<BR>
     * Sinon la méthode change le statut enLigne d'un pdd a FALSE
     *
     * @param nKey id du pdd
     * @param datePublication TimeStamp date de publication
     * @param version verssion
     * @param isPublier enLigne
     * @param plugin Plugin
     */
    void publication( int nKey, Timestamp datePublication, int version, boolean isPublier, Plugin plugin );

    /**
     * Crée une nouvelle Direction CoEmetrice à partir de l’objet directionCoEmetrice passé en paramètre
     * @param directionCoEmetrice La direction CoEmetrice à inserer
     * @param plugin Plugin
     */
    void insert( DirectionCoEmetrice directionCoEmetrice, Plugin plugin );

    /**
     * Crée un nouvel Arrondissement à partir de l’objet arrondissement passé en paramètre
     * @param arrondissement L'arrondissement à inserer
     * @param plugin Plugin
     */
    void insert( Arrondissement arrondissement, Plugin plugin );

    /**
     * renvoie la liste des Directions CoEmetrices de l'id du pdd passé en paramètre
     * @param pddPrimaryKey Id du pdd
     * @param plugin Plugin
     * @return List<Arrondissement> Liste d'objets DirectionCoEmetrice
     */
    List<DirectionCoEmetrice> selectDirectionsCoEmtricesByPDD( int pddPrimaryKey, Plugin plugin );

    /**
     * renvoie la liste des Arrondissements de l'id du pdd passé en paramètre
     * @param pddPrimaryKey Id du pdd
     * @param plugin Plugin
     * @return List<Arrondissement> Liste d'objets Arrondissement
     */
    List<Arrondissement> selectArrondissementsByPDD( int pddPrimaryKey, Plugin plugin );

    /**
     * Supprime toutes les arrondissements possedant l'id du pdd passé en paramètre
     * @param nIdPDD id du pdd
     * @param plugin Plugin
     */
    void deleteArrondissements( int nIdPDD, Plugin plugin );

    /**
     * Supprime toutes les directionCoEmetrice possedant l'id du pdd passé en paramètre
     * @param nIdPDD id du pdd
     * @param plugin Plugin
     */
    void deleteCoEmetrices( int nIdPDD, Plugin plugin );

    /**
     * Ajoute un VoeuxAmendement au pdd
     *
     * @param idVA id du Voeux amendement
     * @param idPdd id du pdd
     * @param plugin Plugin
     */
    void addVoeuAmendement( int idVA, int idPdd, Plugin plugin );

    /**
     * Supprime tous les élements contenus dans oldVoeuAmendements
     * et insére les nouveaux veuxAmendements rattachés aux pdd
     *
     * @param oldVoeuAmendements List<VoeuAmendement>
     * @param newVoeuAmendements List<VoeuAmendement>
     * @param idPDD idPDD
     * @param plugin Plugin
     */
    void updateVoeuxAmendement( List<VoeuAmendement> oldVoeuAmendements, List<VoeuAmendement> newVoeuAmendements,
        int idPDD, Plugin plugin );

    /**
     * Retourne l'identifiant unique pour le couple ( arrondissement, pdd )
     * @param strArrondissement l'arrondissement
     * @param pdd le pdd
     * @param plugin le plugin
     * @return l'identifiant unique pour le couple ( arrondissement, pdd )
     */
    int selectIdArrondissement( String strArrondissement, PDD pdd, Plugin plugin );

    /**
     * Retourne la liste des pdds dont la référence contient le texte passé en argument
     * @param strRecherche la chaîne de caractères recherchée
     * @param bArchive recherche sur les archives ou sur la prochaine séance
     * @param prochaineSeance la prochaine séance
     * @param plugin le plugin
     * @return la liste des pdds dont la référence contient <b>strRecherche</b>
     */
    List<PDD> selectPddsSearchedByReference( String strRecherche, boolean bArchive, Seance prochaineSeance,
        Plugin plugin );
}
