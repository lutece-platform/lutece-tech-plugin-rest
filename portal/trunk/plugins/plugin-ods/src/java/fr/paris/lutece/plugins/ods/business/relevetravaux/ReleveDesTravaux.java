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
package fr.paris.lutece.plugins.ods.business.relevetravaux;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * ReleveDesTravaux
 */
public class ReleveDesTravaux
{
    public static final String MARK_RELEVE = "releve";
    private static final String TAG_EN_LIGNE = "enLigne";
    private static final String TAG_INTITULE = "intitule";
    private static final String TAG_ID = "id";
    private static final String TAG_LIST_RELEVE_DES_TRAVAUX = "listeElementsReleveDesTravaux";
    private static final String TAG_RELEVE_DES_TRAVAUX = "releveDesTravaux";
    private int _nIdReleveDesTravaux;
    private Commission _commission;
    private Seance _seance;
    private String _strIntitule;
    private Boolean _bEnLigne;
    private List<ElementReleveDesTravaux> _listElementReleveDesTravaux;

    /**
     * Retourne l'id du relevé des travaux.
     * @return l'id du relevé des travaux
     */
    public int getIdReleveDesTravaux(  )
    {
        return _nIdReleveDesTravaux;
    }

    /**
     * Affecte l'id donné en paramètre au relevé des travaux.
     * @param idRelevedesTravaux l'id que l'on souhaite affecté au relevé des travaux
     */
    public void setIdReleveDesTravaux( int idRelevedesTravaux )
    {
        this._nIdReleveDesTravaux = idRelevedesTravaux;
    }

    /**
     * Retourne la commission référencée par le relevé des travaux.
     * @return la commission référencée
     */
    public Commission getCommission(  )
    {
        return _commission;
    }

    /**
     * Affecte la commission avec la valeur passée en paramètre
     * @param commission la Commission
     */
    public void setCommission( Commission commission )
    {
        _commission = commission;
    }

    /**
     * Retourne la séance référencée par le relevé des travaux.
     * @return la séance référencée
     */
    public Seance getSeance(  )
    {
        return _seance;
    }

    /**
     * Affecte la séance avec la valeur passée en paramètre
     * @param seance la Séance
     */
    public void setSeance( Seance seance )
    {
        _seance = seance;
    }

    /**
     * Retourne l'intitulé du relevé des travaux.
     * @return l'intitulé
     */
    public String getIntitule(  )
    {
        return _strIntitule;
    }

    /**
     * Affecte l'intitulé avec la valeur passée en paramètre
     * @param intitule l'Intitulé
     */
    public void setIntitule( String intitule )
    {
        _strIntitule = intitule;
    }

    /**
     * Retourne l'état de mise en ligne du relevé des travaux.
     * @return true si le relevé des travaux est en ligne, false sinon
     */
    public boolean isEnLigne(  )
    {
        return _bEnLigne;
    }

    /**
     * Affecte l'état de mise en ligne du relevé des travaux.
     * @param enLigne s'ile st en ligne ou pas
     */
    public void setEnLigne( boolean enLigne )
    {
        _bEnLigne = enLigne;
    }

    /**
     *
     * @return la liste des elements de releve des travaux
     */
    public List<ElementReleveDesTravaux> getElementReleveDesTravaux(  )
    {
        return _listElementReleveDesTravaux;
    }

    /**
     *
     * @param releveDesTravaux la liste des elements de releve des travaux
     */
    public void setElementReleveDesTravaux( List<ElementReleveDesTravaux> releveDesTravaux )
    {
        _listElementReleveDesTravaux = releveDesTravaux;
    }

    /**
     * Génèration du XML d'un objet
     * @param request la requête
     * @return Le code XML représentant relevé de travaux
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );

        attributes.put( TAG_ID, _nIdReleveDesTravaux );
        XmlUtil.beginElement( buffer, TAG_RELEVE_DES_TRAVAUX, attributes );

        if ( _strIntitule != null )
        {
            OdsUtils.addElement( buffer, TAG_INTITULE, _strIntitule );
        }

        OdsUtils.addElement( buffer, TAG_EN_LIGNE, OdsConstants.CONSTANTE_CHAINE_VIDE + _bEnLigne );

        if ( _commission != null )
        {
            buffer.append( _commission.getXml( request ) );
        }

        if ( _seance != null )
        {
            buffer.append( _seance.getXml( request ) );
        }

        if ( _listElementReleveDesTravaux != null )
        {
            XmlUtil.beginElement( buffer, TAG_LIST_RELEVE_DES_TRAVAUX );

            for ( ElementReleveDesTravaux element : _listElementReleveDesTravaux )
            {
                buffer.append( element.getXml( request ) );
            }

            XmlUtil.endElement( buffer, TAG_LIST_RELEVE_DES_TRAVAUX );
        }

        XmlUtil.endElement( buffer, TAG_RELEVE_DES_TRAVAUX );

        return buffer.toString(  );
    }
}
