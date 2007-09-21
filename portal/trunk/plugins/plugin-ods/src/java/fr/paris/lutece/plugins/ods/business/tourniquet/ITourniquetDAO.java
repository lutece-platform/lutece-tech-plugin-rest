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
package fr.paris.lutece.plugins.ods.business.tourniquet;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.List;


/**
 * ITourniquetDAO
 */
public interface ITourniquetDAO
{
    /**
     * Rajoute une entr�e dans le tourniquet.
     * @param tourniquet le nouvel �l�ment
     * @param plugin le Plugin
     */
    void insert( Tourniquet tourniquet, Plugin plugin );

    /**
     * Met � jour les informations concernant le tourniquet pass� en argument.
     * @param tourniquet les nouvelles informations sur le tourniquet
     * @param plugin le Plugin
     */
    void store( Tourniquet tourniquet, Plugin plugin );

    /**
     * Retourne l'�l�ment du tourniquet identifi� par nKey
     * @param nKey l'identifiant de l'�l�ment recherch�
     * @param plugin le Plugin
     * @return l'�l�ment identifi� par nKey
     */
    Tourniquet load( int nKey, Plugin plugin );

    /**
     * Retourne la liste des �l�ments du tourniquet dans l'ordre.
     * @param plugin le Plugin
     * @return l'�tat actuel du tourniquet
     */
    List<Tourniquet> loadTourniquet( Plugin plugin );

    /**
     * Vide la table tourniquet.
     * @param plugin le Plugin
     */
    void flushTourniquet( Plugin plugin );
}
