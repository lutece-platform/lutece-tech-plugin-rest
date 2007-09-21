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


/**
 *
 *
 */
public interface IFichierPhysiqueDAO
{
    /**
    * Crée un nouveau fichier physique à partir de l’objet fichierPhysique passé en paramètre
    * @param fichierPhysique le fichierPhysique à insérer
    * @param plugin Plugin
    * @return l'identifiant du fichier dans la base
    */
    int insert( FichierPhysique fichierPhysique, Plugin plugin );

    /**
     * Modifie le fichierPhysique correspondant à l’objet fichierPhysique passé en paramètre
     * @param fichierPhysique le fichierPhysique à modifier
     * @param plugin le plugin
     */
    void store( FichierPhysique fichierPhysique, Plugin plugin );

    /**
     * Supprime le fichierPhysique correspondant à l’objet fichierPhysique passé en paramètre
     * @param fichierPhysique le fichierPhysique à supprimer
     * @param plugin le plugin
     */
    void delete( FichierPhysique fichierPhysique, Plugin plugin );

    /**
     * Retourne le FichierPhysique ayant comme identifiant nKey
     * @param nKey idenfiant du FichierPhysique à supprimer
     * @param plugin le plugin
     * @return le fichier physique identifié par nKey dans la base
     */
    FichierPhysique load( int nKey, Plugin plugin );
}
