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
package fr.paris.lutece.plugins.ods.business.fichier;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.sql.Timestamp;

import java.util.List;


/**
 * IFichierDAO
 */
public interface IFichierDAO
{
    /**
    * Crée un nouveau fichier à partir de l’objet fichier passé en paramètre
    * @param fichier le fichier à insérer
    * @param plugin le Plugin actif
    * @return int primaryKey
    */
    int insert( Fichier fichier, Plugin plugin );

    /**
     * Modifie le fichier correspondant à l’objet fichier passé en paramètre
     * @param fichier le fichier à modifier
     * @param plugin le Plugin actif
     */
    void store( Fichier fichier, Plugin plugin );

    /**
     * Supprime le fichier correspondant à l’objet fichier passé en paramètre
     * @param fichier le fichier à supprimer
     * @param plugin le Plugin actif
     */
    void delete( Fichier fichier, Plugin plugin );

    /**
     * renvoie le Fichier ayant comme identifiant nKey
     * @param nKey idenfiant du Fichier à supprimer
     * @param plugin le Plugin actif
     * @return un fichier
     */
    Fichier load( int nKey, Plugin plugin );

    /**
     * renvoie la liste de fichiers correspondant au filtre donné
     * @param filter le filtre
     * @param plugin le Plugin actif
     * @return une liste de fichiers
     */
    List<Fichier> selectByFilter( FichierFilter filter, Plugin plugin );

    /**
     * renvoie la liste des Types de documents
     * @param plugin le Plugin actif
     * @return liste d'objets TypeDocument
     */
    List<TypeDocument> loadTypeDocumentList( Plugin plugin );

    /**
     * renvoie la liste des Types de documents de la prochaine séance
     * @param plugin le Plugin actif
     * @return liste d'objets TypeDocument
     */
    List<TypeDocument> loadTypeDocumentProchaineSeanceList( Plugin plugin );

    /**
     * renvoie la liste des Types de documents aval
     * @param plugin le Plugin actif
     * @return liste d'objets TypeDocument
     */
    List<TypeDocument> loadTypeDocumentAvalList( Plugin plugin );

    /**
    * Retourne le type de document en fonction de l'id passé en parametre
    * @param nKey id dy type de document que l'on souhaite
    * @param plugin le Plugin actif
    * @return TypeDocument
    */
    TypeDocument loadTypeDocumentsById( int nKey, Plugin plugin );

    /**
     * Permet de publier ou de dépublier un fichier
     * Publication d'un fichier
     * Si isPublier égale à TRUE, alors la méthode <b>publie</b> le fichier et incrémente la version<BR>
     * Sinon la méthode change le statut enLigne d'un fichier a FALSE
     * @param nKey id du fichier
     * @param tsDatePublication TimeStamp date de publication
     * @param version version
     * @param isPublier enLigne
     * @param plugin le Plugin actif
     */
    void publication( int nKey, Timestamp tsDatePublication, int version, boolean isPublier, Plugin plugin );
}
