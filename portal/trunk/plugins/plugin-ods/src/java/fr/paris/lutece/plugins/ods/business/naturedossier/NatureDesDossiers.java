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
package fr.paris.lutece.plugins.ods.business.naturedossier;

import fr.paris.lutece.plugins.ods.business.seance.Seance;
import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * Objet NatureDesDossiers, correspond à une entrée dans la table ods_nature_dossier
 */
public class NatureDesDossiers
{
    private static final String TAG_LIBELLE_NATURE = "libelleNature";
    private static final String TAG_NUMERO_NATURE = "numeroNature";
    private static final String TAG_NATURE_DES_DOSSIERS = "natureDesDossiers";
    private static final String TAG_ID_NATURE = "id";
    private int _nIdNature;
    private Seance _seance;
    private int _nNumeroNature;
    private String _strLibelleNature;

    /**
     * Retourne l'identifiant de la nature.
     *
     * @return l'identifiant de la nature
     */
    public int getIdNature(  )
    {
        return _nIdNature;
    }

    /**
     * Fixe l'identifiant de la nature.
     *
     * @param nIdNature l'identifiant
     */
    public void setIdNature( int nIdNature )
    {
        _nIdNature = nIdNature;
    }

    /**
     * Retourne l'identifiant de la séance correspondante.
     *
     * @return l'identifiant de la séance correspondante
     */
    public Seance getSeance(  )
    {
        return _seance;
    }

    /**
     * Fixe la séance de la nature de dossiers
     *
     * @param seance la séance de la nature
     */
    public void setSeance( Seance seance )
    {
        _seance = seance;
    }

    /**
     * Retourne le numéro de la nature de dossier.
     *
     * @return le numéro de la nature de dossier
     */
    public int getNumeroNature(  )
    {
        return _nNumeroNature;
    }

    /**
     * Fixe le numéro de la nature de dossier.
     *
     * @param nNumeroNature le numéro de la nature
     */
    public void setNumeroNature( int nNumeroNature )
    {
        _nNumeroNature = nNumeroNature;
    }

    /**
     * Retourne l'intitulé de la nature de dossier.
     *
     * @return l'intitulé de la nature de dossier
     */
    public String getLibelleNature(  )
    {
        return _strLibelleNature;
    }

    /**
     * Fixe l'intitulé de la nature de dossier
     *
     * @param strLibelleNature l'intitulé de la nature
     */
    public void setLibelleNature( String strLibelleNature )
    {
        _strLibelleNature = strLibelleNature;
    }

    /**
     * génération XML
     * @param request la requête Http
     * @return génére Nature des dossiers en XML
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );
        attributes.put( TAG_ID_NATURE, OdsConstants.CONSTANTE_CHAINE_VIDE + _nIdNature );
        XmlUtil.beginElement( buffer, TAG_NATURE_DES_DOSSIERS, attributes );

        if ( _nNumeroNature != -1 )
        {
            OdsUtils.addElement( buffer, TAG_NUMERO_NATURE, _nNumeroNature );
        }

        if ( _strLibelleNature != null )
        {
            OdsUtils.addElement( buffer, TAG_LIBELLE_NATURE, _strLibelleNature );
        }

        XmlUtil.endElement( buffer, TAG_NATURE_DES_DOSSIERS );

        return buffer.toString(  );
    }
}
