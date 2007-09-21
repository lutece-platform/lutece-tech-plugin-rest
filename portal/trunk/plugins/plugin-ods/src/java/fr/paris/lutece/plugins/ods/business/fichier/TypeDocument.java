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

import fr.paris.lutece.plugins.ods.web.constants.OdsConstants;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * TypeDocument
 */
public class TypeDocument
{
    private static final String EST_POUR_GESTION_AVAL = "estPourGestionAval";
    private static final String EST_POUR_PROCHAINE_SEANCE = "estPourProchaineSeance";
    private static final String LIBELLE = "libelle";
    private static final String TYPE_DOCUMENT = "typeDocument";
    private static final String ID = "id";
    private int _nId;
    private String _strLibelle;
    private boolean _bEstPourProchaineSeance;
    private boolean _bEstPourGestionAval;

    /**
     * Retourne le _strLibelle du type de document
     * @return _strLibelle
     */
    public String getLibelle(  )
    {
        return _strLibelle;
    }

    /**
     * Affecte le _strLibelle du type de document
     * @param strLibelle le libellé du type de document
     */
    public void setLibelle( String strLibelle )
    {
        this._strLibelle = strLibelle;
    }

    /**
     * Retourne l'_nId du type de document
     * @return _nId
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Affecte l'_nId du type de document
     * @param nId l'identifiant du type de document
     */
    public void setId( int nId )
    {
        this._nId = nId;
    }

    /**
     * @return TRUE si le type de document concerne les fichiers en gestion aval, FALSE sinon
     */
    public boolean getEstPourGestionAval(  )
    {
        return _bEstPourGestionAval;
    }

    /**
     * @param bEstPourGestionAval si le type de document est pour la gestion aval
     */
    public void setEstPourGestionAval( boolean bEstPourGestionAval )
    {
        _bEstPourGestionAval = bEstPourGestionAval;
    }

    /**
     * @return TRUE si le type de document concerne les fichiers de la prochaine séance, FALSE sinon
     */
    public boolean getEstPourProchaineSeance(  )
    {
        return _bEstPourProchaineSeance;
    }

    /**
     * @param bEstPourProchaineSeance si le type de document est pour la prochaine séance
     */
    public void setEstPourProchaineSeance( boolean bEstPourProchaineSeance )
    {
        _bEstPourProchaineSeance = bEstPourProchaineSeance;
    }

    /**
    * génération XML
    * @param request la requete Http
    * @return génére TypeDocument en XML
    */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );
        attributes.put( ID, OdsConstants.CONSTANTE_CHAINE_VIDE + _nId );
        XmlUtil.beginElement( buffer, TYPE_DOCUMENT, attributes );

        if ( _strLibelle != null )
        {
            OdsUtils.addElement( buffer, LIBELLE, _strLibelle );
        }

        OdsUtils.addElement( buffer, EST_POUR_PROCHAINE_SEANCE,
            OdsConstants.CONSTANTE_CHAINE_VIDE + _bEstPourProchaineSeance );
        OdsUtils.addElement( buffer, EST_POUR_GESTION_AVAL, OdsConstants.CONSTANTE_CHAINE_VIDE + _bEstPourGestionAval );

        return buffer.toString(  );
    }
}
