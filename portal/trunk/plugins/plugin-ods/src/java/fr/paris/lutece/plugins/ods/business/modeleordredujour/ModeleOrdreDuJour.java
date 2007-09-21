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
package fr.paris.lutece.plugins.ods.business.modeleordredujour;

import fr.paris.lutece.plugins.ods.business.commission.Commission;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysique;
import fr.paris.lutece.plugins.ods.business.formationconseil.FormationConseil;
import fr.paris.lutece.plugins.ods.business.ordredujour.TypeOrdreDuJour;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * Fichier
 */
public class ModeleOrdreDuJour
{
    private static final String TAG_TITRE = "titre";
    private static final String TAG_ID = "id";
    private static final String TAG_MODELE_ORDRE_DU_JOUR = "modeleOrdreDuJour";
    private int _nIdModele;
    private FichierPhysique _fEnteteDocument;
    private Commission _commission;
    private FichierPhysique _fPiedDocument;
    private TypeOrdreDuJour _typeOrdreDuJour;
    private FormationConseil _formationConseil;
    private String _strTitre;

    /**
     * getter commission
     * @return _commission
     */
    public Commission getCommission(  )
    {
        return _commission;
    }

    /**
     * @param commission la commission
     */
    public void setCommission( Commission commission )
    {
        _commission = commission;
    }

    /**
     * @return _fEnteteDocument
     */
    public FichierPhysique getEnteteDocument(  )
    {
        return _fEnteteDocument;
    }

    /**
     * @param enTete l'en tete
     */
    public void setEnteteDocument( FichierPhysique enTete )
    {
        _fEnteteDocument = enTete;
    }

    /**
     * @return _formationConseil
     */
    public FormationConseil getFormationConseil(  )
    {
        return _formationConseil;
    }

    /**
     * @param formationConseil la formation du conseil
     */
    public void setFormationConseil( FormationConseil formationConseil )
    {
        _formationConseil = formationConseil;
    }

    /**
     * @return _fPiedDocument
     */
    public FichierPhysique getPiedDocument(  )
    {
        return _fPiedDocument;
    }

    /**
     * @param piedDocument le pied du document
     */
    public void setPiedDocument( FichierPhysique piedDocument )
    {
        _fPiedDocument = piedDocument;
    }

    /**
     * @return _nIdModele
     */
    public int getIdModele(  )
    {
        return _nIdModele;
    }

    /**
     * @param idModele l'identifiant du modele
     */
    public void setIdModele( int idModele )
    {
        _nIdModele = idModele;
    }

    /**
     * @return _strTitre
     */
    public String getTitre(  )
    {
        return _strTitre;
    }

    /**
     * @param titre le titre
     */
    public void setTitre( String titre )
    {
        _strTitre = titre;
    }

    /**
     * @return _typeOrdreDuJour
     */
    public TypeOrdreDuJour getTypeOrdreDuJour(  )
    {
        return _typeOrdreDuJour;
    }

    /**
     * @param ordreDuJour le type d'ordre du jour
     */
    public void setTypeOrdreDuJour( TypeOrdreDuJour ordreDuJour )
    {
        _typeOrdreDuJour = ordreDuJour;
    }

    /**
     * Génèration du XML d'un objet
     * @param request la requête
     * @return Le code XML représentant un modèle ordre du jour
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );

        attributes.put( TAG_ID, _nIdModele );
        XmlUtil.beginElement( buffer, TAG_MODELE_ORDRE_DU_JOUR, attributes );

        if ( _strTitre != null )
        {
            OdsUtils.addElement( buffer, TAG_TITRE, _strTitre );
        }

        if ( _fEnteteDocument != null )
        {
            buffer.append( _fEnteteDocument.getXml( request ) );
        }

        if ( _commission != null )
        {
            buffer.append( _commission.getXml( request ) );
        }

        if ( _fPiedDocument != null )
        {
            buffer.append( _fPiedDocument.getXml( request ) );
        }

        if ( _typeOrdreDuJour != null )
        {
            buffer.append( _typeOrdreDuJour.getXml( request ) );
        }

        if ( _formationConseil != null )
        {
            buffer.append( _formationConseil.getXml( request ) );
        }

        XmlUtil.endElement( buffer, TAG_MODELE_ORDRE_DU_JOUR );

        return buffer.toString(  );
    }
}
