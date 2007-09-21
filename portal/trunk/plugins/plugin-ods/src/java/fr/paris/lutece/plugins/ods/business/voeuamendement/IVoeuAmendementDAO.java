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
import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


/**
 *
 * IVoeuAmendementDAO
 *
 */
public interface IVoeuAmendementDAO
{
    /**
     * Ins�re un nouvel enregistrement dans la table ods_voeu_amendement � partir de l�objet voeu_amendement
     *  pass�  en param�tre
     * @param   voeuAmendement le voeuAmendement � ins�rer dans la table ods_voeu_amendement
     * @param plugin plugin
     * @return l'id du VoeuAmendement cr��
     */
    int insert( VoeuAmendement voeuAmendement, Plugin plugin );

    /**
     * Modifie l'enregistrement dans la table ods_voeu_Amendement et les tables associ�es correspondant �  l�objet voeuAmendementpass� en param�tre
     * @param voeuAmendement le voeuAmendement � modifier dans la table ods_voeu_amendement
     * @param plugin plugin
     */
    void store( VoeuAmendement voeuAmendement, Plugin plugin );

    /**
     * Supprime l' enregistrement dans la table ods_voeu_amendement correspondant �  l�objet VoeuAmendement pass� en param�tre
     * @param voeuAmendement le voeuAmendement � supprimer dans la table ods_voeu_amendement
     * @param plugin plugin
     */
    void delete( VoeuAmendement voeuAmendement, Plugin plugin );

    /**
     * renvoie le VoeuAmendement correspondant � l'enregistrement dans la table ods_voeu_amendement ayant comme identifiant nKey
     * @param nKey l'identifiant de l'�lu
     * @param plugin  plugin
     * @return le voeuAmendement d'id=nkey
     */
    VoeuAmendement load( int nKey, Plugin plugin );

    /**
     * renvoie la liste des voeux et Amendements  pr�sents dans la table ods_voeu_amendement r�pondant
     *  aux criteres de selections du filtre
     * @param filter le filtre de selection
     * @param plugin  plugin
     * @return liste d'objets voeuAmendement
     */
    List<VoeuAmendement> selectVoeuAmendementListByFilter( VoeuAmendementFilter filter, Plugin plugin );

    /**
    * test si la reference = strReference du VA existe deja dans le Fascicule d'id =nIdFascicule
    * @param nIdFascicule id du fascicule
    * @param strReference reference du VA
    * @param plugin plugin
    * @return retourne true si la reference existe deja pour ce fascicule false sinon
    */
    boolean isAlreadyExistInFascicule( int nIdFascicule, String strReference, Plugin plugin );

    /**
     * renvoie la liasse des voeux et Amendements  pr�sents dans la table ods_voeu_amendement r�pondant
     *  aux criteres de selections du filtre
     * @param filter le filtre de selection
     * @param plugin plugin
     * @return liste d'objets voeuAmendement
     */
    List<VoeuAmendement> selectLiasseByFilter( VoeuAmendementFilter filter, Plugin plugin );

    /**
     * Retourne la liste des pdds associ� au VoeuAmendement d'id nKey
     * @param nKey id du VA
     * @param plugin plugin
     * @return la liste des pdds du va
     */
    List<PDD> selectPddListbyIdVoeuAmendement( int nKey, Plugin plugin );

    /**
     * Retourne le voeu amendement dont le texte initial est le fichier
     * @param fichier le fichier du voeu amendement
     * @param plugin le plugin
     * @return la voeu amendement dont le texte initial est le fichier
     */
    VoeuAmendement selectByTexteInitial( Fichier fichier, Plugin plugin );

    /**
     * Retourne le voeu amendement dont la deliberation est le fichier
     * @param fichier le fichier du voeu amendement
     * @param plugin le plugin
     * @return la voeu amendement dont le texte initial est le fichier
     */
    VoeuAmendement selectByDeliberation( Fichier fichier, Plugin plugin );
}
