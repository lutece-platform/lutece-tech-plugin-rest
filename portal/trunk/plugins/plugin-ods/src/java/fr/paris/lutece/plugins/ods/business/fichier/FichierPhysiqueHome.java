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
import fr.paris.lutece.portal.service.spring.SpringContextService;


/**
 * Cette classe permet de manipuler des objets FichierPhysique
 */
public class FichierPhysiqueHome
{
    private static IFichierPhysiqueDAO _dao = (IFichierPhysiqueDAO) SpringContextService.getPluginBean( "ods",
            "fichierPhysiqueDAO" );

    /**
    * Crée un nouveau fichier physique à partir de l’objet fichierPhysique passé en paramètre
    * @param fichierPhysique le fichierPhysique à insérer
    * @param plugin le plugin
    * @return l'identifiant du fichier créé dans la base
    */
    public static int create( FichierPhysique fichierPhysique, Plugin plugin )
    {
        return _dao.insert( fichierPhysique, plugin );
    }

    /**
     * Modifie le fichierPhysique correspondant à l’objet fichierPhysique passé en paramètre
     * @param fichierPhysique le fichierPhysique à modifier
     * @param plugin le plugin
     */
    public static void update( FichierPhysique fichierPhysique, Plugin plugin )
    {
        _dao.store( fichierPhysique, plugin );
    }

    /**
     * Supprime le fichierPhysique correspondant à l’objet fichierPhysique passé en paramètre
     * @param fichierPhysique le fichierPhysique à supprimer
     * @param plugin le plugin
     */
    public static void remove( FichierPhysique fichierPhysique, Plugin plugin )
    {
        _dao.delete( fichierPhysique, plugin );
    }

    /**
     * renvoie le FichierPhysique ayant comme identifiant nKey
     * @param nKey idenfiant du FichierPhysique à supprimer
     * @param plugin le plugin
     * @return le fichier physique identifié par <b>nKey</b>
     */
    public static FichierPhysique findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }
}
