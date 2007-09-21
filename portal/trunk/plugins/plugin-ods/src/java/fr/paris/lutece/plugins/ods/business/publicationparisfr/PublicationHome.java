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
package fr.paris.lutece.plugins.ods.business.publicationparisfr;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 *
 * PublicationHome
 *
 */
public class PublicationHome
{
    private static IPublicationDAO _dao = (IPublicationDAO) SpringContextService.getPluginBean( "ods", "publicationDAO" );

    /**
    * Insère un nouvel enregistrement dans la table ods_publication_parisfr  à partir de l’objet publiaction passé en paramètre
    * @param publication  l'objet publication à inserer dans la table ods_publication_parisfr
    * @param plugin plugin
    */
    public static void create( Publication publication, Plugin plugin )
    {
        _dao.insert( publication, plugin );
    }

    /**
     * Supprime l' enregistrement dans la table ods_publication_parisfr  correspondant à  l’objet publication passé en paramètre
     * @param publication la publication à supprimer
     * @param plugin plugin
     */
    public static void remove( Publication publication, Plugin plugin )
    {
        _dao.delete( publication, plugin );
    }

    /**
     * renvoie la liste des ordres du jours  à envoyer à paris.fr sous forme d'une liste d'objets publications
     * @param  plugin Plugin
     * @return une liste d'objet publication
     */
    public static List<Publication> findPublicationList( Plugin plugin )
    {
        return _dao.selectPublicationList( plugin );
    }
}
