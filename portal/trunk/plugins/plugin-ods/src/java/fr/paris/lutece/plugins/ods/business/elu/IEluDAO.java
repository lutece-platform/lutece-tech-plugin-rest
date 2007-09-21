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
package fr.paris.lutece.plugins.ods.business.elu;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 *
 * Interface IEluDAO
 *
 */
public interface IEluDAO
{
    /**
     * Ins�re un nouvel enregistrement dans la table ods_elu � partir de l�objet elu pass� en param�tre
     * @param elu  l'�lu  � ins�rer dans la table ods_elu
     * @param plugin plugin
     */
    void insert( Elu elu, Plugin plugin );

    /**
     * Modifie l'enregistrement dans la table ods_elu correspondant �  l�objet �lu pass� en param�tre
     * @param elu l'�lu � modifier dans la table ods_�lu
     * @param plugin plugin
     */
    void store( Elu elu, Plugin plugin );

    /**
     * Supprime l' enregistrement dans la table ods_elu correspondant �  l�objet elu pass� en param�tre
     * @param elu l'�lu � supprimer dans la table ods_�lu
     * @param plugin plugin
     * @throws AppException si l'�lu est r�f�renc� par un objet tiers
     */
    void delete( Elu elu, Plugin plugin ) throws AppException;

    /**
     * renvoie l'�lu correspondant � l'enregistrement dans la table ods_elu ayant comme identifiant nKey
     * @param nKey l'identifiant de l'�lu
     * @param plugin plugin
     * @return
     */
    Elu load( int nKey, Plugin plugin );

    /**
     * renvoie la liste des �lus pr�sents dans la table ods_elu
     * @param plugin
     * @return liste d'objets Elu
     */
    List<Elu> selectEluList( Plugin plugin );

    /**
     * renvoie la liste des �lus ayant un groupe politique dont l'identifiant est �gal � nKey
     * @param nKey  identifiant du groupe politique
     * @param plugin
     * @return
     */
    List<Elu> selectEluListbyIdGroupe( int nKey, Plugin plugin );

    /**
     * Retourne la liste des �lus dont le statut est actif.
     * @param plugin
     * @return la liste des �lus actifs
     */
    List<Elu> selectEluActifs( Plugin plugin );

    /**
     * Retourne la liste des �lus actifs rempla�ants d'autres �lus
     * @param plugin le plugin
     * @return la liste des �lus actifs rempla�ants d'autres �lus
     */
    List<Elu> selectEluRemplacantsActifs( Plugin plugin );

    /**
     * Retourne la liste des �lus actifs rapporteurs dans une commission
     * @param plugin le plugin
     * @return la liste des rapporteurs
     */
    List<Elu> selectElusRapporteursActifs( Plugin plugin );

    /**
     * Retourne la liste de tous les �lus rapporteurs d'une commission
     * @param plugin le plugin
     * @return la liste des �lus rapporteurs
     */
    List<Elu> selectElusRapporteurs( Plugin plugin );
}
