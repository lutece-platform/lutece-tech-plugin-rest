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

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJour;
import fr.paris.lutece.plugins.ods.business.ordredujour.TypeOrdreDuJour;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 *
 */
public class PublicationDAO implements fr.paris.lutece.plugins.ods.business.publicationparisfr.IPublicationDAO
{
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_publication ) FROM ods_publication_parisfr ";
    private static final String SQL_QUERY_FIND_ODJS_FOR_PUBLICATION = "SELECT odj.id_odj,odj.id_seance," +
        "fc.id_formation_conseil,fc.libelle_formation_conseil,ty.id_type_odj,ty.libelle_type_odj," +
        "odj.ods_id_odj,co.id_commission,co.numero_commission,co.libelle_commission,odj.intitule," +
        "odj.mode_classement,odj.tourniquet,odj.publie,odj.date_publication," +
        "odj.est_sauvegarde,odj.xml_correspondant,odj.xml_entete,odj.xml_pied_de_page,publi.id_publication FROM " +
        "ods_publication_parisfr publi " + "LEFT JOIN  ods_odj odj on (publi.id_odj=odj.id_odj)" +
        "LEFT JOIN  ods_odj odj2 on (odj.id_odj!=odj2.id_odj )" +
        "LEFT JOIN ods_commission co  on(odj.id_commission=co.id_commission) " +
        "LEFT JOIN ods_formation_conseil fc on(odj.id_formation_conseil=fc.id_formation_conseil) " +
        "LEFT JOIN ods_type_ordre_jour ty on(odj.id_type_odj=ty.id_type_odj) " + "WHERE publi.id_odj=odj.id_odj " +
        "AND odj.id_type_odj=ty.id_type_odj  " +
        "AND odj.id_type_odj=odj2.id_type_odj AND odj.publie=1 AND odj2.publie=1 " +
        "AND odj.id_commission IS NULL AND odj2.id_commission IS NULL  " +
        "GROUP BY odj.id_odj  ORDER BY odj.id_type_odj";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ods_publication_parisfr(id_publication,id_odj)VALUES (?,?)";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ods_publication_parisfr where id_publication=? ";

    /**
     * Génère un nouvel identifiant
     * @param plugin plugin
     * @return Retourne l’identifiant généré
     */
    int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
     * Insère un nouvel enregistrement dans la table ods_publication_parisfr  à partir de l’objet publiaction passé en paramètre
     * @param publication  l'objet publication à inserer dans la table ods_publication_parisfr
     * @param plugin plugin
     */
    public void insert( Publication publication, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        publication.setIdPublication( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, publication.getIdPublication(  ) );
        daoUtil.setInt( 2, publication.getOrdreDuJour(  ).getIdOrdreDuJour(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Supprime l' enregistrement dans la table ods_publication_parisfr  correspondant à  l’objet publication passé en paramètre
     * @param publication la publication à supprimer
     * @param plugin plugin
     */
    public void delete( Publication publication, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, publication.getIdPublication(  ) );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * renvoie la liste des ordres du jours  à envoyer à paris.fr sous forme d'une liste d'objets publications
     * @param  plugin Plugin
     * @return une liste d'objet publication
     */
    public List<Publication> selectPublicationList( Plugin plugin )
    {
        List<Publication> listPublication = new ArrayList<Publication>(  );
        Seance seance = null;
        FormationConseil formationConseil = null;
        TypeOrdreDuJour typeOrdreDuJour = null;
        Publication publication = null;
        OrdreDuJour ordreDuJour = null;
        OrdreDuJour ordreDuJourSauvegarde = null;
        Commission commission = null;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_ODJS_FOR_PUBLICATION, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            ordreDuJour = new OrdreDuJour(  );

            ordreDuJour.setIdOrdreDuJour( daoUtil.getInt( 1 ) );
            seance = new Seance(  );
            seance.setIdSeance( daoUtil.getInt( 2 ) );
            ordreDuJour.setSeance( seance );

            formationConseil = new FormationConseil(  );
            formationConseil.setIdFormationConseil( daoUtil.getInt( 3 ) );
            formationConseil.setLibelle( daoUtil.getString( 4 ) );
            ordreDuJour.setFormationConseil( formationConseil );

            typeOrdreDuJour = new TypeOrdreDuJour(  );
            typeOrdreDuJour.setIdTypeOrdreDuJour( daoUtil.getInt( 5 ) );
            typeOrdreDuJour.setLibelle( daoUtil.getString( 6 ) );
            ordreDuJour.setTypeOrdreDuJour( typeOrdreDuJour );

            if ( null != daoUtil.getObject( 7 ) )
            {
                ordreDuJourSauvegarde = new OrdreDuJour(  );
                ordreDuJourSauvegarde.setIdOrdreDuJour( daoUtil.getInt( 7 ) );
                ordreDuJour.setOrdreDuJourSauveGarde( ordreDuJourSauvegarde );
            }

            if ( null != daoUtil.getObject( 8 ) )
            {
                commission = new Commission(  );
                commission.setIdCommission( daoUtil.getInt( 8 ) );
                commission.setNumero( daoUtil.getInt( 9 ) );
                commission.setLibelle( daoUtil.getString( 10 ) );
            }

            ordreDuJour.setCommission( commission );

            ordreDuJour.setIntitule( daoUtil.getString( 11 ) );
            ordreDuJour.setModeClassement( daoUtil.getString( 12 ) );
            ordreDuJour.setTourniquet( daoUtil.getBoolean( 13 ) );
            ordreDuJour.setPublie( daoUtil.getBoolean( 14 ) );
            ordreDuJour.setDatePublication( daoUtil.getTimestamp( 15 ) );
            ordreDuJour.setEstSauvegarde( daoUtil.getBoolean( 16 ) );
            ordreDuJour.setXmlPublication( daoUtil.getString( 17 ) );
            ordreDuJour.setXmlEntete( daoUtil.getString( 18 ) );
            ordreDuJour.setXmlPiedDePage( daoUtil.getString( 19 ) );
            //publication
            publication = new Publication(  );
            publication.setIdPublication( daoUtil.getInt( 20 ) );
            publication.setOrdreDuJour( ordreDuJour );
            listPublication.add( publication );
        }

        daoUtil.free(  );

        return listPublication;
    }
}
