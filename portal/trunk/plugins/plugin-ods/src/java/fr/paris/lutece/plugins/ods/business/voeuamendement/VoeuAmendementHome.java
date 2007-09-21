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
package fr.paris.lutece.plugins.ods.business.voeuamendement;

import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.service.search.ODSSearchService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 *
 *  VoeuAmendementHome
 *
 */
public class VoeuAmendementHome
{
    private static IVoeuAmendementDAO _dao = (IVoeuAmendementDAO) SpringContextService.getPluginBean( "ods",
            "voeuAmendementDAO" );

    /**
     * Insère un VoeuAmendement à partir de l’objet VoeuAmendement passé en parametre
     *  passé  en paramètre
     * @param   voeuAmendement le voeuAmendement à insérer dans la table ods_voeu_amendement
     * @param plugin plugin
     * @return l'id du VoeuAmendement créé
     */
    public static int create( VoeuAmendement voeuAmendement, Plugin plugin )
    {
        voeuAmendement.setIdVoeuAmendement( _dao.insert( voeuAmendement, plugin ) );

        boolean bArchive = false;

        if ( ( voeuAmendement != null ) && ( voeuAmendement.getFascicule(  ) != null ) &&
                ( voeuAmendement.getFascicule(  ).getSeance(  ) != null ) )
        {
            Seance seanceByVA = voeuAmendement.getFascicule(  ).getSeance(  );
            Seance nextSeance = SeanceHome.getProchaineSeance( plugin );

            if ( ( nextSeance != null ) && ( seanceByVA.getIdSeance(  ) != nextSeance.getIdSeance(  ) ) )
            {
                bArchive = true;
            }
        }

        ODSSearchService.addObjectToIndex( voeuAmendement, bArchive, plugin );

        return voeuAmendement.getIdVoeuAmendement(  );
    }

    /**
     * renvoie le VoeuAmendement  ayant comme identifiant nKey
     * @param nKey l'identifiant de l'élu
     * @param plugin plugin
     * @return le voeuAmendement d'id=nkey
     */
    public static VoeuAmendement findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Supprime le VoeuAmendement passé en paramètre
     * @param voeuAmendement le voeuAmendement à supprimer
     * @param plugin plugin
     */
    public static void remove( VoeuAmendement voeuAmendement, Plugin plugin )
    {
        boolean bArchive = false;

        if ( ( voeuAmendement != null ) && ( voeuAmendement.getFascicule(  ) != null ) &&
                ( voeuAmendement.getFascicule(  ).getSeance(  ) != null ) )
        {
            Seance seanceByVA = voeuAmendement.getFascicule(  ).getSeance(  );
            Seance nextSeance = SeanceHome.getProchaineSeance( plugin );

            if ( ( nextSeance != null ) && ( seanceByVA.getIdSeance(  ) != nextSeance.getIdSeance(  ) ) )
            {
                bArchive = true;
            }
        }

        ODSSearchService.deleteObjectFromIndex( voeuAmendement, bArchive, plugin );
        _dao.delete( voeuAmendement, plugin );
    }

    /**
     * Modifie le VoeuAmendement passé en paramètre
     * @param voeuAmendement le voeuAmendement à modifier dans la table ods_voeu_amendement
     * @param plugin plugin
     */
    public static void update( VoeuAmendement voeuAmendement, Plugin plugin )
    {
        boolean bArchive = false;

        if ( ( voeuAmendement != null ) && ( voeuAmendement.getFascicule(  ) != null ) &&
                ( voeuAmendement.getFascicule(  ).getSeance(  ) != null ) )
        {
            Seance seanceByVA = voeuAmendement.getFascicule(  ).getSeance(  );
            Seance nextSeance = SeanceHome.getProchaineSeance( plugin );

            if ( ( nextSeance != null ) && ( seanceByVA.getIdSeance(  ) != nextSeance.getIdSeance(  ) ) )
            {
                bArchive = true;
            }
        }

        ODSSearchService.updateObjectInIndex( voeuAmendement, bArchive, plugin );
        _dao.store( voeuAmendement, plugin );
    }

    /**
     * renvoie la liste des voeux et Amendements   répondant
     *  aux criteres de selections du filtre
     * @param filter le filtre de selection
     * @param plugin plugin
     * @return liste d'objets voeuAmendement
     */
    public static List<VoeuAmendement> findVoeuAmendementListByFilter( VoeuAmendementFilter filter, Plugin plugin )
    {
        return _dao.selectVoeuAmendementListByFilter( filter, plugin );
    }

    /**
     * test si la reference = strReference du VA existe deja dans le Fascicule d'id =nIdFascicule
     * @param nIdFascicule id du fascicule
     * @param strReference reference du VA
     * @param plugin plugin
     * @return retourne true si la reference existe deja pour ce fascicule false sinon
     */
    public static boolean isAlreadyExistInFascicule( int nIdFascicule, String strReference, Plugin plugin )
    {
        return _dao.isAlreadyExistInFascicule( nIdFascicule, strReference, plugin );
    }

    /**
     * renvoie la liasse des Voeux  Amendements   répondant
     *  aux criteres de selections du filtre
     * @param filter le filtre de selection
     * @param plugin plugin
     * @return liste d'objets voeuAmendement
     */
    public static List<VoeuAmendement> findLiasseByFilter( VoeuAmendementFilter filter, Plugin plugin )
    {
        return _dao.selectLiasseByFilter( filter, plugin );
    }

    /**
     * Retourne la liste des pdds associé au VoeuAmendement d'id nKey
     * @param nKey id du VA
     * @param plugin plugin
     * @return la liste des pdds du va
     */
    public static List<PDD> findPddListbyIdVoeuAmendement( int nKey, Plugin plugin )
    {
        return _dao.selectPddListbyIdVoeuAmendement( nKey, plugin );
    }

    /**
     * Retourne le voeu amendement dont le texte initial est le fichier
     * @param fichier le fichier du voeu amendement
     * @param plugin le plugin
     * @return la voeu amendement dont le texte initial est le fichier
     */
    public static VoeuAmendement findByTexteInitial( Fichier fichier, Plugin plugin )
    {
        return _dao.selectByTexteInitial( fichier, plugin );
    }

    /**
     * Retourne le voeu amendement dont la délibération est le fichier
     * @param fichier le fichier du voeu amendement
     * @param plugin le plugin
     * @return la voeu amendement dont le texte initial est le fichier
     */
    public static VoeuAmendement findByDeliberation( Fichier fichier, Plugin plugin )
    {
        return _dao.selectByDeliberation( fichier, plugin );
    }
}
