/*
 * Copyright (c) 2002-2004, Mairie de Paris
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
package fr.paris.lutece.plugins.dbpage.business;

import fr.paris.lutece.plugins.dbpage.business.section.DbPageSection;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.resource.*;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * This class provides the properties object loader
 */
public class DbPageLoaderDatabase implements ResourceLoader
{
    private Plugin _plugin;
    private String strPluginName = "dbpage";

    /**
     * Gets all resource available for this Loader
     * @return A collection of resources
     */
    public Collection getResources(  )
    {
        if ( _plugin == null )
        {
            _plugin = PluginService.getPlugin( strPluginName );
        }

        List<DbPage> listPages = new ArrayList<DbPage>(  );
        List<DbPageDatabase> listDbPages = DbPageDatabaseHome.findDbPageDatabasesList( _plugin );

        for ( DbPageDatabase dbDatabasePage : listDbPages )
        {
            DbPage dbPage = loadPage( dbDatabasePage.getParamName(  ) );
            listPages.add( dbPage );
        }

        return listPages;
    }

    /**
     * Get a resource by its Id
     * @return The resource
     * @param strPageId a string representing the identifier of the Page
     */
    public Resource getResource( String strPageId )
    {
        Resource resource = null;

        resource = loadPage( strPageId );

        return resource;
    }

    /**
     * Return the page
     * @return dbPage
     * @param strPageName The name of the page
     */
    private DbPage loadPage( String strPageName )
    {
        if ( _plugin == null )
        {
            _plugin = PluginService.getPlugin( strPluginName );
        }

        DbPageDatabase dbPageDatabase = DbPageDatabaseHome.findByName( strPageName, _plugin );

        if ( dbPageDatabase == null )
        {
            return null;
        }

        DbPage dbPage = new DbPage(  );
        dbPage.setName( dbPageDatabase.getParamName(  ) );
        dbPage.setTitle( dbPageDatabase.getTitle(  ) );
        dbPage.setWorkgroup( dbPageDatabase.getWorkgroup(  ) );

        int nSections = DbPageDatabaseSectionHome.countNumberSections( dbPageDatabase.getId(  ), _plugin );
        dbPage.setNbSection( nSections );

        List<DbPageSection> listSections = new ArrayList<DbPageSection>(  );
        List<DbPageDatabaseSection> listSectionsDb = DbPageDatabaseSectionHome.findDbPageDatabaseSectionsList( _plugin );

        for ( DbPageDatabaseSection dbSection : listSectionsDb )
        {
            DbPageSection dbPageSection = null;

            if ( dbSection.getIdPage(  ) == dbPageDatabase.getId(  ) )
            {
                int nIdType = dbSection.getIdType(  );
                DbPageDatabaseType type = DbPageDatabaseTypeHome.findByPrimaryKey( nIdType, _plugin );
                String strSectionClass = type.getClassDesc(  );

                try
                {
                    dbPageSection = (DbPageSection) Class.forName( strSectionClass ).newInstance(  );
                }
                catch ( ClassNotFoundException e )
                {
                    AppLogService.error( e.getMessage(  ), e );
                }
                catch ( IllegalAccessException e )
                {
                    AppLogService.error( e.getMessage(  ), e );
                }
                catch ( InstantiationException e )
                {
                    AppLogService.error( e.getMessage(  ), e );
                }

                dbPageSection.setType( type.getClassDesc(  ) );
                dbPageSection.setTitle( dbSection.getTitle(  ) );
                dbPageSection.setSql( dbSection.getSql(  ) );
                dbPageSection.setDbPool( dbSection.getPool(  ) );
                dbPageSection.setRole( dbSection.getRole(  ) );

                String strTemplatePath = dbSection.getTemplatePath(  );

                dbPageSection.setTemplatePath( strTemplatePath );

                //Columns name loading
                String strColumnsName = dbSection.getColumn(  );

                if ( strColumnsName != null )
                {
                    List<String> aColumnsName = new ArrayList<String>(  );

                    while ( !strColumnsName.equals( "" ) )
                    {
                        int nPos = strColumnsName.indexOf( "," );

                        if ( nPos != -1 )
                        {
                            String strValue = strColumnsName.substring( 0, nPos );
                            strValue = strValue.trim(  );
                            strColumnsName = strColumnsName.substring( nPos + 1 );
                            aColumnsName.add( strValue );
                        }
                        else
                        {
                            aColumnsName.add( strColumnsName.trim(  ) );
                            strColumnsName = "";
                        }

                        dbPageSection.setColumnNames( aColumnsName );
                    }
                }

                listSections.add( dbPageSection );
            }
        }

        dbPage.setListSection( listSections );

        return dbPage;
    }
}
