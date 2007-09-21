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
package fr.paris.lutece.plugins.ods.business.groupepolitique;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppException;

import java.util.List;


/**
 *
 * IGroupePolitiqueDAO
 *
 */
public interface IGroupePolitiqueDAO
{
    /**
     * Ins�re un nouvel enregistrement dans la table ods_groupe � partir de l�objet groupe pass� en param�tre
     * @param groupe le groupe politique � ins�rer dans la table ods_groupe
     * @param plugin plugin
     */
    void insert( GroupePolitique groupe, Plugin plugin );

    /**
     * Modifie l'enregistrement dans la table ods_groupe correspondant �  l�objet groupe pass� en param�tre
     * @param groupe le groupe politique � modifier dans la table ods_groupe
     * @param plugin plugin
     */
    void store( GroupePolitique groupe, Plugin plugin );

    /**
     * Supprime l' enregistrement dans la table ods_groupe correspondant �  l�objet groupe pass� en param�tre
     * @param groupe le groupe politique � supprimer
     * @param plugin plugin
     * @throws AppException si le groupe est r�f�renc� par un objet tiers
     */
    void delete( GroupePolitique groupe, Plugin plugin )
        throws AppException;

    /**
     * renvoie le groupe politique  correspondant � l'enregistrement dans la table ods_groupe ayant comme identifiant nKey
     * @param nKey idenfiant du groupe politique � supprimer
     * @param plugin plugin
     * @return objet GroupePolitique
     */
    GroupePolitique load( int nKey, Plugin plugin );

    /**
     * renvoie la liste des groupes politiques pr�sents dans la table ods_groupe
     * @param plugin plugin
     * @return liste d'objets GroupePolitique
     */
    List<GroupePolitique> groupeList( Plugin plugin );

    /**
     * renvoie le Groupe politique de l'�lu dont l'identifiant � �t� pass� en param�tre
     * @param nKey identifiant de l'�lu dont on recherche le groupe politique
     * @param plugin plugin
     * @return objet GroupePolitique
     */
    GroupePolitique selectByIdElu( int nKey, Plugin plugin );

    /**
     * Retourne la liste des groupes politiques dont le statut est actif.
     * @param plugin plugin
     * @return la liste des groupes actifs
     */
    List<GroupePolitique> selectGroupesActifs( Plugin plugin );
}
