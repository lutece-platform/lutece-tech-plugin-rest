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

import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.business.seance.SeanceHome;
import fr.paris.lutece.plugins.ods.service.search.ODSSearchService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.sql.Timestamp;

import java.util.List;


/**
 * FichierHome
 */
public class FichierHome
{
    private static IFichierDAO _dao = (IFichierDAO) SpringContextService.getPluginBean( "ods", "fichierDAO" );

    /**
    * Crée un nouveau fichier à partir de l’objet fichier passé en paramètre
    * @param fichier le fichier à insérer
    * @param plugin le Plugin actif
    * @return int primaryKey
    */
    public static int create( Fichier fichier, Plugin plugin )
    {
        fichier.setId( _dao.insert( fichier, plugin ) );

        boolean bArchive = false;

        if ( ( fichier != null ) && ( fichier.getSeance(  ) != null ) )
        {
            Seance seanceByFichier = fichier.getSeance(  );
            Seance nextSeance = SeanceHome.getProchaineSeance( plugin );

            if ( ( nextSeance != null ) && ( seanceByFichier.getIdSeance(  ) != nextSeance.getIdSeance(  ) ) )
            {
                bArchive = true;
            }
        }

        ODSSearchService.addObjectToIndex( fichier, bArchive, plugin );

        return fichier.getId(  );
    }

    /**
     * Modifie le fichier correspondant à l’objet fichier passé en paramètre
     * @param fichier le fichier à modifier
     * @param plugin le Plugin actif
     */
    public static void update( Fichier fichier, Plugin plugin )
    {
        boolean bArchive = false;

        if ( ( fichier != null ) && ( fichier.getSeance(  ) != null ) )
        {
            Seance seanceByFichier = fichier.getSeance(  );
            Seance nextSeance = SeanceHome.getProchaineSeance( plugin );

            if ( ( nextSeance != null ) && ( seanceByFichier.getIdSeance(  ) != nextSeance.getIdSeance(  ) ) )
            {
                bArchive = true;
            }
        }

        ODSSearchService.updateObjectInIndex( fichier, bArchive, plugin );
        _dao.store( fichier, plugin );
    }

    /**
     * Supprime le fichier correspondant à l’objet fichier passé en paramètre
     * @param fichier le fichier à supprimer
     * @param plugin le Plugin actif
     */
    public static void remove( Fichier fichier, Plugin plugin )
    {
        boolean bArchive = false;

        if ( ( fichier != null ) && ( fichier.getSeance(  ) != null ) )
        {
            Seance seanceByFichier = fichier.getSeance(  );
            Seance nextSeance = SeanceHome.getProchaineSeance( plugin );

            if ( ( nextSeance != null ) && ( seanceByFichier.getIdSeance(  ) != nextSeance.getIdSeance(  ) ) )
            {
                bArchive = true;
            }
        }

        ODSSearchService.deleteObjectFromIndex( fichier, bArchive, plugin );
        _dao.delete( fichier, plugin );
    }

    /**
     * renvoie le Fichier ayant comme identifiant nKey
     * @param nKey idenfiant du Fichier à supprimer
     * @param plugin le Plugin actif
     * @return un fichier
     */
    public static Fichier findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * récupère la liste des fichiers correspondant au filtre donné
     * @param filter le filtre
     * @param plugin le Plugin actif
     * @return uen liste de fichiers
     */
    public static List<Fichier> findByFilter( FichierFilter filter, Plugin plugin )
    {
        return _dao.selectByFilter( filter, plugin );
    }

    /**
     * renvoie la liste des Types de documents
     * @param plugin le Plugin actif
     * @return liste d'objets TypeDocument
     */
    public static List<TypeDocument> findTypeDocumentsList( Plugin plugin )
    {
        return _dao.loadTypeDocumentList( plugin );
    }

    /**
     * renvoie la liste des Types de documents pour la prochaine séance
     * @param plugin le Plugin actif
     * @return liste d'objets TypeDocument
     */
    public static List<TypeDocument> findTypeDocumentsProchaineSeanceList( Plugin plugin )
    {
        return _dao.loadTypeDocumentProchaineSeanceList( plugin );
    }

    /**
     * renvoie la liste des Types de documents aval
     * @param plugin le Plugin actif
     * @return liste d'objets TypeDocument
     */
    public static List<TypeDocument> findTypeDocumentsAvalList( Plugin plugin )
    {
        return _dao.loadTypeDocumentAvalList( plugin );
    }

    /**
     * Retourne le type de document en fonction de l'id passé en parametre
     * @param nKey id dy type de document que l'on souhaite
     * @param plugin le Plugin actif
     * @return TypeDocument
     */
    public static TypeDocument findTypeDocumentsById( int nKey, Plugin plugin )
    {
        return _dao.loadTypeDocumentsById( nKey, plugin );
    }

    /**
     * Permet de publier ou de dépublier un fichier
     * Publication d'un fichier
     * Si isPublier égale à TRUE, alors la méthode <b>publie</b> le fichier et incrémente la version<BR>
     * Sinon la méthode change le statut enLigne d'un fichier a FALSE
     * @param nKey id du fichier
     * @param datePublication TimeStamp date de publication
     * @param version verssion
     * @param isPublier enLigne
     * @param plugin le Plugin actif
     */
    public static void publication( int nKey, Timestamp datePublication, int version, boolean isPublier, Plugin plugin )
    {
        _dao.publication( nKey, datePublication, version, isPublier, plugin );
    }
}
