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
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 *
 *
 */
public class FichierPhysique
{
    private static final String TAG_ID_FICHIER = "id";
    private static final String TAG_FICHIER_PHYSIQUE = "fichierPhysique";
    private int _nIdFichier;
    private byte[] _donnees;

    /**
     * Retourne un tableau de byte qui représente les données du fichier physique
     * @return byte[] les donnees du fichier physique
     */
    public byte[] getDonnees(  )
    {
        return _donnees;
    }

    /**
     * Fixe les données du fichier physique
     * @param donnees les données du fichier physique
     */
    public void setDonnees( byte[] donnees )
    {
        this._donnees = donnees;
    }

    /**
     * Retourne l'identifiant du fichier physique
     * @return l'identifiant du fichier physique
     */
    public int getIdFichier(  )
    {
        return _nIdFichier;
    }

    /**
     * Fixe l'identifiant du fichier physique
     * @param nIdFichier l'identifiant du fichier physique
     */
    public void setIdFichier( int nIdFichier )
    {
        this._nIdFichier = nIdFichier;
    }

    /**
     * génération XML
     * @param request la requete Http
     * @return génére FichierPhysique en XML
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );
        attributes.put( TAG_ID_FICHIER, OdsConstants.CONSTANTE_CHAINE_VIDE + _nIdFichier );
        XmlUtil.addEmptyElement( buffer, TAG_FICHIER_PHYSIQUE, attributes );

        return buffer.toString(  );
    }

    /**
     * retourne la taille du fichier physique
     * @return la taille du fichier physique
     */
    public int getTaille(  )
    {
        if ( _donnees != null )
        {
            return _donnees.length;
        }

        return 0;
    }
}
