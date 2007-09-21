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

import fr.paris.lutece.plugins.ods.business.elu.Elu;
import fr.paris.lutece.plugins.ods.business.groupepolitique.GroupePolitique;
import fr.paris.lutece.plugins.ods.business.pdd.PDD;
import fr.paris.lutece.plugins.ods.business.voeuamendement.VoeuAmendement;
import fr.paris.lutece.plugins.ods.web.utils.OdsUtils;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * ElementReleveDesTravaux
 */
public class ElementReleveDesTravaux
{
    private static final String TAG_OBSERVATIONS = "observations";
    private static final String TAG_NE_POUVANTPAS_VOTER = "nePouvantPasVoter";
    private static final String TAG_ABSTENTION = "abstention";
    private static final String TAG_CONTRE = "contre";
    private static final String TAG_POUR = "pour";
    private static final String TAG_ID = "id";
    private static final String TAG_ELEMENT_RELEVE_DES_TRAVAUX = "elementReleveDesTravaux";
    private int _nIdElementReleveDesTravaux;
    private ReleveDesTravaux _releve;
    private Elu _elu;
    private GroupePolitique _groupe;
    private VoeuAmendement _voeuAmendement;
    private PDD _projetDeliberation;
    private int _nPour;
    private int _nContre;
    private int _nAbstention;
    private int _nNePouvantPasVoter;
    private String _strObservations;

    /**
     * @return l'élu
     */
    public Elu getElu(  )
    {
        return _elu;
    }

    /**
     * @param elu l'élu à affecter
     */
    public void setElu( Elu elu )
    {
        _elu = elu;
    }

    /**
     * @return le groupe politique
     */
    public GroupePolitique getGroupe(  )
    {
        return _groupe;
    }

    /**
     * @param groupe le groupe politique à affecter
     */
    public void setGroupe( GroupePolitique groupe )
    {
        _groupe = groupe;
    }

    /**
     * @return le nombre d'abstentions
     */
    public int getAbstention(  )
    {
        return _nAbstention;
    }

    /**
     * @param nAbstention le nombre d'abstention à affecter
     */
    public void setAbstention( int nAbstention )
    {
        _nAbstention = nAbstention;
    }

    /**
     * @return le nombre de contre
     */
    public int getContre(  )
    {
        return _nContre;
    }

    /**
     * @param nContre le nombre de contre à affecter
     */
    public void setContre( int nContre )
    {
        _nContre = nContre;
    }

    /**
     * @return l'identifiant de l'élément de relevé de travaux
     */
    public int getIdElementReleveDesTravaux(  )
    {
        return _nIdElementReleveDesTravaux;
    }

    /**
     * @param nIdElementReleveDesTravaux l'identifiant à affecter
     */
    public void setIdElementReleveDesTravaux( int nIdElementReleveDesTravaux )
    {
        _nIdElementReleveDesTravaux = nIdElementReleveDesTravaux;
    }

    /**
     * @return le nombre de personnes ne pouvant pas voter
     */
    public int getNePouvantPasVoter(  )
    {
        return _nNePouvantPasVoter;
    }

    /**
     * @param nNePouvantPasVoter le nombre de personnes ne pouvant pas voter à affecter
     */
    public void setNePouvantPasVoter( int nNePouvantPasVoter )
    {
        _nNePouvantPasVoter = nNePouvantPasVoter;
    }

    /**
     * @return le nombre de votes Pour
     */
    public int getPour(  )
    {
        return _nPour;
    }

    /**
     * @param nPour le nombre de votes Pour à affecter
     */
    public void setPour( int nPour )
    {
        _nPour = nPour;
    }

    /**
     * @return le projet de délibération
     */
    public PDD getProjetDeliberation(  )
    {
        return _projetDeliberation;
    }

    /**
     * @param projetDeliberation le projet de délibération à affecter
     */
    public void setProjetDeliberation( PDD projetDeliberation )
    {
        _projetDeliberation = projetDeliberation;
    }

    /**
     * @return le relevé de travaux
     */
    public ReleveDesTravaux getReleve(  )
    {
        return _releve;
    }

    /**
     * @param releve le relevé de travaux à affecter
     */
    public void setReleve( ReleveDesTravaux releve )
    {
        _releve = releve;
    }

    /**
     * @return les observations
     */
    public String getObservations(  )
    {
        return _strObservations;
    }

    /**
     * @param strObservations les observations à affecter
     */
    public void setObservations( String strObservations )
    {
        _strObservations = strObservations;
    }

    /**
     * @return le Voeu / Amendement
     */
    public VoeuAmendement getVoeuAmendement(  )
    {
        return _voeuAmendement;
    }

    /**
     * @param voeuAmendement le Voeu / Amendement à affecter
     */
    public void setVoeuAmendement( VoeuAmendement voeuAmendement )
    {
        _voeuAmendement = voeuAmendement;
    }

    /**
     * Génèration du XML d'un objet
     * @param request la requête
     * @return Le code XML représentant un élement de relevé de travaux
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer buffer = new StringBuffer(  );
        HashMap<Object, Object> attributes = new HashMap<Object, Object>(  );

        attributes.put( TAG_ID, _nIdElementReleveDesTravaux );
        XmlUtil.beginElement( buffer, TAG_ELEMENT_RELEVE_DES_TRAVAUX, attributes );

        if ( _nPour >= 0 )
        {
            OdsUtils.addElement( buffer, TAG_POUR, _nPour );
        }

        if ( _nContre >= 0 )
        {
            OdsUtils.addElement( buffer, TAG_CONTRE, _nContre );
        }

        if ( _nAbstention >= 0 )
        {
            OdsUtils.addElement( buffer, TAG_ABSTENTION, _nAbstention );
        }

        if ( _nNePouvantPasVoter >= 0 )
        {
            OdsUtils.addElement( buffer, TAG_NE_POUVANTPAS_VOTER, _nNePouvantPasVoter );
        }

        if ( _strObservations != null )
        {
            OdsUtils.addElement( buffer, TAG_OBSERVATIONS, _strObservations );
        }

        if ( _elu != null )
        {
            buffer.append( _elu.getXml( request ) );
        }

        if ( _groupe != null )
        {
            buffer.append( _groupe.getXml( request ) );
        }

        if ( _voeuAmendement != null )
        {
            buffer.append( _voeuAmendement.getXml( request ) );
        }

        if ( _projetDeliberation != null )
        {
            buffer.append( _projetDeliberation.getXml( request ) );
        }

        XmlUtil.endElement( buffer, TAG_ELEMENT_RELEVE_DES_TRAVAUX );

        return buffer.toString(  );
    }
}
