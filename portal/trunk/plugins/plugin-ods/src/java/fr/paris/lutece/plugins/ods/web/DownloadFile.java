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
package fr.paris.lutece.plugins.ods.web;

import fr.paris.lutece.plugins.ods.business.fichier.Fichier;
import fr.paris.lutece.plugins.ods.business.fichier.FichierHome;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysique;
import fr.paris.lutece.plugins.ods.business.fichier.FichierPhysiqueHome;
import fr.paris.lutece.plugins.ods.web.constants.OdsParameters;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Cette classe permet de telecharger un fichier
 *
 */
public class DownloadFile
{
    private static final String MARK_PLUGIN_ODS = "ods";

    /**
     * Télecharge un fichier.
     * Renvoie un flux de bytes du fichier dans l'objet HttpServletResponse response
     *
     * @param request la requête HTTP
     * @param response la requête HTTP
     */
    public void doDowloadFichier( HttpServletRequest request, HttpServletResponse response )
    {
        String strIdFichier = request.getParameter( OdsParameters.ID_FICHIER );

        if ( strIdFichier != null )
        {
            Plugin plugin = PluginService.getPlugin( MARK_PLUGIN_ODS );
            int idFichier = Integer.parseInt( strIdFichier );

            Fichier fichierTelecharger = FichierHome.findByPrimaryKey( idFichier,
                    PluginService.getPlugin( MARK_PLUGIN_ODS ) );
            FichierPhysique fichierPhysique = FichierPhysiqueHome.findByPrimaryKey( fichierTelecharger.getFichier(  )
                                                                                                      .getIdFichier(  ),
                    plugin );

            try
            {
                String strFileName = fichierTelecharger.getNom(  ) + "." + fichierTelecharger.getExtension(  );

                response.setHeader( "Content-Disposition", "attachment ;filename=\"" + strFileName + "\"" );
                response.setContentType( "application/pdf" );
                response.setHeader( "Pragma", "public" );
                response.setHeader( "Expires", "0" );
                response.setHeader( "Cache-Control", "must-revalidate,post-check=0,pre-check=0" );
                response.setContentLength( (int) fichierPhysique.getDonnees(  ).length );

                OutputStream os = response.getOutputStream(  );
                os.write( fichierPhysique.getDonnees(  ) );
                os.close(  );
            }
            catch ( IOException e )
            {
                AppLogService.error( e );
            }
        }
    }
}
