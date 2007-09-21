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
package fr.paris.lutece.plugins.ods.business.documentseance;

import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.ordredujour.OrdreDuJour;

import java.util.Comparator;


/**
 *
 * objet chargé de contenir un ordre du jour ou un fichier.
 *  utilisé dans le front office pour trier les documents de seance et les documents
 *  des commissions par date
 *
 */
public class DocumentSeance implements Comparator
{
    private OrdreDuJour _ordreDuJour;
    private Fichier _fichier;

    /**
     *
     * @return fichier
     */
    public Fichier getFichier(  )
    {
        return _fichier;
    }

    /**
     *
     * @param fichier initialise le fichier
     */
    public void setFichier( Fichier fichier )
    {
        this._fichier = fichier;
    }

    /**
     *
     * @return l'ordre du jour
     */
    public OrdreDuJour getOrdreDuJour(  )
    {
        return _ordreDuJour;
    }

    /**
     *
     * @param ordreDuJour initialise l'ordre du  jour
     */
    public void setOrdreDuJour( OrdreDuJour ordreDuJour )
    {
        this._ordreDuJour = ordreDuJour;
    }

    /**
     * @param object1 Object
     * @param object2 Object
     * @return int retourne 0 si egaliste, 1 ou -1 sinon
     */
    public int compare( Object object1, Object object2 )
    {
        int nReturn = 1;
        DocumentSeance ordreDuJourDocument1 = (DocumentSeance) object1;
        DocumentSeance ordreDuJourDocument2 = (DocumentSeance) object2;

        // the following line will return a negative value, if one is smaller in size than two
        if ( ( ordreDuJourDocument1.getOrdreDuJour(  ) != null ) && ( ordreDuJourDocument2.getOrdreDuJour(  ) != null ) )
        {
            if ( ordreDuJourDocument1.getOrdreDuJour(  ).getDatePublication(  )
                                         .before( ordreDuJourDocument2.getOrdreDuJour(  ).getDatePublication(  ) ) )
            {
                nReturn = 1;
            }
            else if ( ordreDuJourDocument1.getOrdreDuJour(  ).getDatePublication(  )
                                              .after( ordreDuJourDocument2.getOrdreDuJour(  ).getDatePublication(  ) ) )
            {
                nReturn = -1;
            }
            else
            {
                nReturn = 0;
            }
        }
        else if ( ( ordreDuJourDocument1.getOrdreDuJour(  ) != null ) && ( ordreDuJourDocument2.getFichier(  ) != null ) )
        {
            if ( ordreDuJourDocument1.getOrdreDuJour(  ).getDatePublication(  )
                                         .before( ordreDuJourDocument2.getFichier(  ).getDatePublication(  ) ) )
            {
                nReturn = 1;
            }
            else if ( ordreDuJourDocument1.getOrdreDuJour(  ).getDatePublication(  )
                                              .after( ordreDuJourDocument2.getFichier(  ).getDatePublication(  ) ) )
            {
                nReturn = -1;
            }
            else
            {
                nReturn = 0;
            }
        }

        else if ( ( ordreDuJourDocument1.getFichier(  ) != null ) && ( ordreDuJourDocument2.getOrdreDuJour(  ) != null ) )
        {
            if ( ordreDuJourDocument1.getFichier(  ).getDatePublication(  )
                                         .before( ordreDuJourDocument2.getOrdreDuJour(  ).getDatePublication(  ) ) )
            {
                nReturn = 1;
            }
            else if ( ordreDuJourDocument1.getFichier(  ).getDatePublication(  )
                                              .after( ordreDuJourDocument2.getOrdreDuJour(  ).getDatePublication(  ) ) )
            {
                nReturn = -1;
            }
            else
            {
                nReturn = 0;
            }
        }
        else if ( ( ordreDuJourDocument1.getFichier(  ) != null ) && ( ordreDuJourDocument2.getFichier(  ) != null ) )
        {
            if ( ordreDuJourDocument1.getFichier(  ).getDatePublication(  )
                                         .before( ordreDuJourDocument2.getFichier(  ).getDatePublication(  ) ) )
            {
                nReturn = 1;
            }
            else if ( ordreDuJourDocument1.getFichier(  ).getDatePublication(  )
                                              .after( ordreDuJourDocument2.getFichier(  ).getDatePublication(  ) ) )
            {
                nReturn = -1;
            }
            else
            {
                nReturn = 0;
            }
        }

        return nReturn;
    }
}
