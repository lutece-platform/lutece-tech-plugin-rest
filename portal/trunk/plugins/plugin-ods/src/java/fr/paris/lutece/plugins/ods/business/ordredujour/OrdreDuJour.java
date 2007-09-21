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
package fr.paris.lutece.plugins.ods.business.ordredujour;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.sql.Timestamp;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * Objet OrdreDuJour, correspond à une entrée dans la table ods_odj
 */
public class OrdreDuJour
{
    public static final String MARK_ODJ = "odj";
    private static final String TAG_LISTE_ENTREES = "listeEntrees";
    private static final String TAG_EST_SAUVEGARDE = "estSauvegarde";
    private static final String TAG_EST_PUBLIE = "estPublie";
    private static final String TAG_TOURNIQUET = "tourniquet";
    private static final String TAG_DATE_PUBLICATION = "datePublication";
    private static final String TAG_MODE_CLASSEMENT = "modeClassement";
    private static final String TAG_INTITULE = "intitule";
    private static final String TAG_ID = "id";
    private static final String TAG_ORDRE_DU_JOUR = "ordreDuJour";
    private int _nIdOrdreDuJour;
    private Seance _seance;
    private FormationConseil _formationConseil;
    private TypeOrdreDuJour _typeOrdreDuJour;
    private OrdreDuJour _ordreDuJourSauveGarde;
    private Commission _commission;
    private String _strIntitule;
    private String _strModeClassement;
    private Boolean _bTourniquet;
    private Boolean _bPublie;
    private Timestamp _datePublication;
    private Boolean _bEstSauvegarde;
    private List<EntreeOrdreDuJour> _listEntrees;
    private String _strXmlPublication;
    private String _strXmlEntete;
    private String _strXmlPiedDePage;

    /**
     * @return retourne true si l'ordre du jour est une sauvegarde false sinon
     */
    public Boolean getEstSauvegarde(  )
    {
        return _bEstSauvegarde;
    }

    /**
     *
     * @param estSauvegarde affecte true  si l'ordre du jour est une false sinon
     */
    public void setEstSauvegarde( Boolean estSauvegarde )
    {
        _bEstSauvegarde = estSauvegarde;
    }

    /**
     *
     *@return retourne true si l'ordre du jour est publié false sinon
     */
    public Boolean getPublie(  )
    {
        return _bPublie;
    }

    /**
     *
     * @param publie  affecte true si l'ordre du jour est publié  false sinon
     */
    public void setPublie( Boolean publie )
    {
        _bPublie = publie;
    }

    /**
     *
     *@return retourne true si le tourniquet s'applique à l'ordre du jour false sinon
     */
    public Boolean getTourniquet(  )
    {
        return _bTourniquet;
    }

    /**
     *
     * @param tourniquet affecte true  si le tourniquet s'applique à l'ordre du jour false sinon
     */
    public void setTourniquet( Boolean tourniquet )
    {
        _bTourniquet = tourniquet;
    }

    /**
     *
     * @return retourne la commission de l'ordre du jour
     */
    public Commission getCommission(  )
    {
        return _commission;
    }

    /**
     *
     * @param commission  affecte une commission à l'ordre du jour
     */
    public void setCommission( Commission commission )
    {
        this._commission = commission;
    }

    /**
     *
     * @return retourne la date de publication de l'ordre du jour
     */
    public Timestamp getDatePublication(  )
    {
        return _datePublication;
    }

    /**
     *
     * @param publication affecte une date de publication à l'ordre du jour
     */
    public void setDatePublication( Timestamp publication )
    {
        _datePublication = publication;
    }

    /**
     *
     * @return retourne la formation conseil de l'ordre du jour
     */
    public FormationConseil getFormationConseil(  )
    {
        return _formationConseil;
    }

    /**
         *
         * @param conseil affecte une formation conseil à l'ordre du jour
         */
    public void setFormationConseil( FormationConseil conseil )
    {
        _formationConseil = conseil;
    }

    /**
     *
     * @return retourne la liste des entrées d'ordre du jour
     */
    public List<EntreeOrdreDuJour> getListEntrees(  )
    {
        return _listEntrees;
    }

    /**
         *
         * @param entrees affecte une liste d'entrées à l'ordre du jour
         */
    public void setListEntrees( List<EntreeOrdreDuJour> entrees )
    {
        _listEntrees = entrees;
    }

    /**
     *
     * @return retourne l'id de l'ordre du jour
     */
    public int getIdOrdreDuJour(  )
    {
        return _nIdOrdreDuJour;
    }

    /**
     *
     * @param idOrdreDuJour affecte un id a l'ordre du jour
     */
    public void setIdOrdreDuJour( int idOrdreDuJour )
    {
        _nIdOrdreDuJour = idOrdreDuJour;
    }

    /**
     *
     * @return retourne la sauvegarde de l'ordre du jour
     */
    public OrdreDuJour getOrdreDuJourSauveGarde(  )
    {
        return _ordreDuJourSauveGarde;
    }

    /**
     *
     * @param ordreDuJourSauveGarde affecte une sauvegarde à l'ordre du jour
     */
    public void setOrdreDuJourSauveGarde( OrdreDuJour ordreDuJourSauveGarde )
    {
        _ordreDuJourSauveGarde = ordreDuJourSauveGarde;
    }

    /**
     *
     * @return retourne la seance de l'ordre du jour
     */
    public Seance getSeance(  )
    {
        return _seance;
    }

    /**
     *
     * @param seance affecte une seance à l'ordre du jour
     */
    public void setSeance( Seance seance )
    {
        _seance = seance;
    }

    /**
     *
     * @return retourne l'intitulé de l'ordre du jour
     */
    public String getIntitule(  )
    {
        return _strIntitule;
    }

    /**
     *
     * @param intitule affecte un intitulé à l'ordre du jour
     */
    public void setIntitule( String intitule )
    {
        _strIntitule = intitule;
    }

    /**
     *
     * @return retourne le mode de classement de l'ordre du jour (Automatique,Manuel)
     */
    public String getModeClassement(  )
    {
        return _strModeClassement;
    }

    /**
     *
     * @param modeClassement affecte un mode de classement à l'ordre du jour (Automatique,Manuel)
     */
    public void setModeClassement( String modeClassement )
    {
        _strModeClassement = modeClassement;
    }

    /**
     *
     * @return le type d'ordre du jour
     */
    public TypeOrdreDuJour getTypeOrdreDuJour(  )
    {
        return _typeOrdreDuJour;
    }

    /**
     *
     * @param typeOrdreDuJour affecte un  type d'ordre du jour
     */
    public void setTypeOrdreDuJour( TypeOrdreDuJour typeOrdreDuJour )
    {
        _typeOrdreDuJour = typeOrdreDuJour;
    }

    /**
     *
     * @return l'entete de l'ordre du jour ,stockée au moment de la publication de ce dernier
     */
    public String getXmlEntete(  )
    {
        return _strXmlEntete;
    }

    /**
     *
     * @param entete initialise l'entete de l'ordre du jour au moment de la publication de ce dernier
     */
    public void setXmlEntete( String entete )
    {
        _strXmlEntete = entete;
    }

    /**
     *
     * @return le pied de page  de l'ordre du jour ,stocké au moment de la publication de ce dernier
     */
    public String getXmlPiedDePage(  )
    {
        return _strXmlPiedDePage;
    }

    /**
     *
     * @param piedDePage  initialise le pied de page  de l'ordre du jour au moment de la publication de ce dernier
     */
    public void setXmlPiedDePage( String piedDePage )
    {
        _strXmlPiedDePage = piedDePage;
    }

    /**
     *
     * @return le fichier xml de l'ordre du jour ,stocké au moment de la publication de ce dernier
     */
    public String getXmlPublication(  )
    {
        return _strXmlPublication;
    }

    /**
     *
     * @param xmlPublication  initialise le fichier xml  de l'ordre du jour au moment de la publication de ce dernier
     */
    public void setXmlPublication( String xmlPublication )
    {
        _strXmlPublication = xmlPublication;
    }

    /**
    * Génèration du XML d'un objet
    * @param request la requête
    * @return Le code XML représentant un ordre du jour
    */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );

        attributes.put( TAG_ID, _nIdOrdreDuJour );
        XmlUtil.beginElement( buffer, TAG_ORDRE_DU_JOUR, attributes );

        if ( _strXmlEntete == null )
        {
            //on ajoute un entete vide
            OdsUtils.addElement( buffer, OrdreDuJourUtils.TAG_ENTETE, _strXmlEntete );
        }
        else
        {
            //on ajoute le contenu du fichier xml	
            buffer.append( _strXmlEntete );
        }

        if ( _strXmlPiedDePage == null )
        {
            //on ajoute un pied de page vide
            OdsUtils.addElement( buffer, OrdreDuJourUtils.TAG_PIED_DE_PAGE, _strXmlPiedDePage );
        }
        else
        {
            //on ajoute le contenu du fichier xml	
            buffer.append( _strXmlPiedDePage );
        }

        OdsUtils.addElement( buffer, TAG_INTITULE, _strIntitule );
        OdsUtils.addElement( buffer, TAG_MODE_CLASSEMENT, _strModeClassement );
        OdsUtils.addElement( buffer, TAG_DATE_PUBLICATION, OdsConstants.CONSTANTE_CHAINE_VIDE + _datePublication );
        OdsUtils.addElement( buffer, TAG_TOURNIQUET, OdsConstants.CONSTANTE_CHAINE_VIDE + _bTourniquet );
        OdsUtils.addElement( buffer, TAG_EST_PUBLIE, OdsConstants.CONSTANTE_CHAINE_VIDE + _bPublie );
        OdsUtils.addElement( buffer, TAG_EST_SAUVEGARDE, OdsConstants.CONSTANTE_CHAINE_VIDE + _bEstSauvegarde );

        if ( _seance != null )
        {
            buffer.append( _seance.getXml( request ) );
        }

        if ( _formationConseil != null )
        {
            buffer.append( _formationConseil.getXml( request ) );
        }

        if ( _typeOrdreDuJour != null )
        {
            buffer.append( _typeOrdreDuJour.getXml( request ) );
        }

        if ( _commission != null )
        {
            buffer.append( _commission.getXml( request ) );
        }

        if ( ( _listEntrees != null ) )
        {
            XmlUtil.beginElement( buffer, TAG_LISTE_ENTREES );

            for ( EntreeOrdreDuJour entree : _listEntrees )
            {
                if ( entree != null )
                {
                    buffer.append( entree.getXml( request ) );
                }
            }

            XmlUtil.endElement( buffer, TAG_LISTE_ENTREES );
        }

        XmlUtil.endElement( buffer, TAG_ORDRE_DU_JOUR );

        return buffer.toString(  );
    }
}
