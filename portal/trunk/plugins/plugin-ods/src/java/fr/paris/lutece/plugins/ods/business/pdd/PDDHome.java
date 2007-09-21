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
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.service.search.ODSSearchService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.sql.Timestamp;

import java.util.List;


/**
 *
 * Permet d'acceder aux methodes qui interroge la base de données
 * PDDHome a e role d'un proxy avec le PDDDAO
 *
 */
public class PDDHome
{
    private static IPDDDAO _dao = (IPDDDAO) SpringContextService.getPluginBean( "ods", "projetDeliberationDAO" );

    /**
    * renvoie la liste des pdd de la prochaine seance
    * @param plugin Plugin
    * @param filter PDDFilter
    * @return List<PDD> liste d'objets Fichier
    */
    public static List<PDD> findByFilter( PDDFilter filter, Plugin plugin )
    {
        return _dao.selectByFilter( filter, plugin );
    }

    /**
     * renvoie la liste des pdd de la prochaine seance
     * @param plugin Plugin
     * @param filter PDDFilter
     * @param  nIdDirection nIdDirection
     * @return List<PDD> liste d'objets Fichier
     */
    public static List<PDD> findByFilterByDirectionOrDirectionCoemetrice( PDDFilter filter, Plugin plugin,
        int nIdDirection )
    {
        return _dao.selectByFilterByDirectionOrDirectionCoemetrice( filter, plugin, nIdDirection );
    }

    /**
    * Modifie un PDD à partir de l’objet pdd passé en paramètre
    * @param pdd la Projet de deliberation à modifier
    * @param plugin Plugin
    */
    public static void update( PDD pdd, Plugin plugin )
    {
        boolean bArchive = true;
        PDDFilter filter = new PDDFilter(  );

        Seance nextSeance = SeanceHome.getProchaineSeance( plugin );

        if ( nextSeance != null )
        {
            filter.setSeance( nextSeance.getIdSeance(  ) );
        }

        List<PDD> listPDDProchaineSeance = PDDHome.findByFilter( filter, plugin );

        if ( ( listPDDProchaineSeance != null ) && listPDDProchaineSeance.contains( pdd ) )
        {
            bArchive = false;
        }

        ODSSearchService.updateObjectInIndex( pdd, bArchive, plugin );
        _dao.store( pdd, plugin );
    }

    /**
     * Supprime le PDD dont l'id est celui passé en paramètre
     * @param pdd le PDD
     * @param plugin le Plugin
     */
    public static void remove( PDD pdd, Plugin plugin )
    {
        boolean bArchive = true;
        PDDFilter filter = new PDDFilter(  );

        Seance nextSeance = SeanceHome.getProchaineSeance( plugin );

        if ( nextSeance != null )
        {
            filter.setSeance( nextSeance.getIdSeance(  ) );
        }

        List<PDD> listPDDProchaineSeance = PDDHome.findByFilter( filter, plugin );

        if ( ( listPDDProchaineSeance != null ) && listPDDProchaineSeance.contains( pdd ) )
        {
            bArchive = false;
        }

        ODSSearchService.deleteObjectFromIndex( pdd, bArchive, plugin );
        _dao.delete( pdd, plugin );
    }

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
    public static void publication( int nKey, Timestamp datePublication, int version, boolean isPublier, Plugin plugin )
    {
        _dao.publication( nKey, datePublication, version, isPublier, plugin );
    }

    /**
     * renvoie le pdd en fonction de l'id passe en parametre
     * @param nKey int id du PDD
     * @param plugin Plugin
     * @return PDD le pdd qui a ete trouvé
     */
    public static PDD findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.selectByPrimaryKey( nKey, plugin );
    }

    /**
    * Crée un nouveau PDD à partir de l’objet pdd passé en paramètre
    * @param pdd la Projet de deliberation à insérer
    * @param plugin Plugin
    * @return ProjetDeDeliberation pdd creer
    */
    public static PDD create( PDD pdd, Plugin plugin )
    {
        PDD pddTemp = _dao.insert( pdd, plugin );

        boolean bArchive = true;
        PDDFilter filter = new PDDFilter(  );

        Seance nextSeance = SeanceHome.getProchaineSeance( plugin );

        if ( nextSeance != null )
        {
            filter.setSeance( nextSeance.getIdSeance(  ) );
        }

        List<PDD> listPDDProchaineSeance = PDDHome.findByFilter( filter, plugin );

        if ( ( listPDDProchaineSeance != null ) && listPDDProchaineSeance.contains( pddTemp ) )
        {
            bArchive = false;
        }

        ODSSearchService.addObjectToIndex( pddTemp, bArchive, plugin );

        return pddTemp;
    }

    /**
     * Crée une nouvelle Direction CoEmetrice à partir de l’objet directionCoEmetrice passé en paramètre
     * @param directionCoEmetrice La direction CoEmetrice à inserer
     * @param plugin Plugin
     */
    public static void create( DirectionCoEmetrice directionCoEmetrice, Plugin plugin )
    {
        _dao.insert( directionCoEmetrice, plugin );
    }

    /**
     * Crée un nouvel Arrondissement à partir de l’objet arrondissement passé en paramètre
     * @param arrondissement L'arrondissement à inserer
     * @param plugin Plugin
     */
    public static void create( Arrondissement arrondissement, Plugin plugin )
    {
        _dao.insert( arrondissement, plugin );
    }

    /**
     * renvoie la liste des Directions CoEmetrices de l'id du pdd passé en paramètre
     * @param pddPrimaryKey Id du pdd
     * @param plugin Plugin
     * @return List<Arrondissement> Liste d'objets DirectionCoEmetrice
     */
    public static List<DirectionCoEmetrice> findDirectionsCoEmtricesByPDD( int pddPrimaryKey, Plugin plugin )
    {
        return _dao.selectDirectionsCoEmtricesByPDD( pddPrimaryKey, plugin );
    }

    /**
    * renvoie la liste des Arrondissements de l'id du pdd passé en paramètre
    * @param pddPrimaryKey Id du pdd
    * @param plugin Plugin
    * @return List<Arrondissement> Liste d'objets Arrondissement
    */
    public static List<Arrondissement> findArrondissementsByPDD( int pddPrimaryKey, Plugin plugin )
    {
        return _dao.selectArrondissementsByPDD( pddPrimaryKey, plugin );
    }

    /**
    * Supprime toutes les arrondissements possedant l'id du pdd passé en paramètre
    * @param nIdPDD id du pdd
    * @param plugin Plugin
    */
    public static void deleteArrondissements( int nIdPDD, Plugin plugin )
    {
        _dao.deleteArrondissements( nIdPDD, plugin );
    }

    /**
    * Supprime toutes les directionCoEmetrice possedant l'id du pdd passé en paramètre
    * @param nIdPDD id du pdd
    * @param plugin Plugin
    */
    public static void deleteCoEmetrices( int nIdPDD, Plugin plugin )
    {
        _dao.deleteCoEmetrices( nIdPDD, plugin );
    }

    /**
     * Ajoute un VoeuxAmendement au pdd
     *
     * @param idVA id du Voeux amendement
     * @param idPdd id du pdd
     * @param plugin Plugin
     */
    public static void addVoeuAmendement( int idVA, int idPdd, Plugin plugin )
    {
        _dao.addVoeuAmendement( idVA, idPdd, plugin );
    }

    /**
     * Supprime tous les élements contenus dans oldVoeuAmendements
     * et insére les nouveaux veuxAmendements rattachés aux pdd
     *
     * @param oldVoeuAmendements List<VoeuAmendement>
     * @param newVoeuAmendements List<VoeuAmendement>
     * @param nIdPDD idPDD
     * @param plugin Plugin
     */
    public static void updateVoeuxAmendement( List<VoeuAmendement> oldVoeuAmendements,
        List<VoeuAmendement> newVoeuAmendements, int nIdPDD, Plugin plugin )
    {
        _dao.updateVoeuxAmendement( oldVoeuAmendements, newVoeuAmendements, nIdPDD, plugin );
    }

    /**
     * Cette methode verifie l'unicité d'un pdd sur la référence au niveau d'une séance
     *
     * @param pdd pdd que l'on veut créer
     * @param seance la séance
     * @param plugin Plugin
     * @return FALSE si aucun pdd ne possede cette reference, TRUE sinon
     */
    public static boolean isPddAlreadyExist( PDD pdd, Seance seance, Plugin plugin )
    {
        PDDFilter filter = new PDDFilter(  );
        filter.setReference( pdd.getReference(  ) );

        Seance nextSeance = SeanceHome.getProchaineSeance( plugin );

        if ( ( nextSeance != null ) && ( seance != null ) && ( nextSeance.getIdSeance(  ) == seance.getIdSeance(  ) ) )
        {
            filter.setProchaineSeance( seance.getIdSeance(  ) );
        }
        else if ( seance != null )
        {
            filter.setSeance( seance.getIdSeance(  ) );
        }

        List<PDD> pdds = _dao.selectByFilter( filter, plugin );

        if ( ( pdds != null ) && ( pdds.size(  ) > 0 ) )
        {
            return true;
        }

        return false;
    }

    /**
     * Retourne l'identifiant dans la base pour le couple ( arrondissement, pdd )
     * @param strArrondissement l'arrondissement voulu
     * @param pdd le pdd
     * @param plugin le plugin
     * @return l'identifiant dans la base pour le couple ( arrondissement, pdd )
     */
    public static int findIdArrondissement( String strArrondissement, PDD pdd, Plugin plugin )
    {
        return _dao.selectIdArrondissement( strArrondissement, pdd, plugin );
    }

    /**
     * Retourne la liste des pdds dont la référence contient le texte passé en argument
     * @param strRecherche la chaîne de caractères recherchée
     * @param bArchive recherche sur l'index des archives ou de la prochaine séance
     * @param prochaineSeance la prochaine séance
     * @param plugin le plugin
     * @return la liste des pdds dont la référence contient <b>strRecherche</b>
     */
    public static List<PDD> findPddsListSearchedByReference( String strRecherche, boolean bArchive,
        Seance prochaineSeance, Plugin plugin )
    {
        return _dao.selectPddsSearchedByReference( strRecherche, bArchive, prochaineSeance, plugin );
    }
}
