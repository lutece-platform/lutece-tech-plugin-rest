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
package fr.paris.lutece.portal.service.plugin;

import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.business.right.Right;
import fr.paris.lutece.portal.business.right.RightHome;
import fr.paris.lutece.portal.service.content.ContentService;
import fr.paris.lutece.portal.service.content.ContentServiceEntry;
import fr.paris.lutece.portal.service.content.XPageAppService;
import fr.paris.lutece.portal.service.daemon.AppDaemonService;
import fr.paris.lutece.portal.service.daemon.DaemonEntry;
import fr.paris.lutece.portal.service.database.PluginConnectionService;
import fr.paris.lutece.portal.service.includes.PageIncludeEntry;
import fr.paris.lutece.portal.service.includes.PageIncludeService;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.insert.InsertService;
import fr.paris.lutece.portal.service.insert.InsertServiceManager;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.rbac.RBACResourceTypeEntry;
import fr.paris.lutece.portal.service.rbac.ResourceIdService;
import fr.paris.lutece.portal.service.search.IndexationService;
import fr.paris.lutece.portal.service.search.SearchIndexer;
import fr.paris.lutece.portal.service.search.SearchIndexerEntry;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPageApplicationEntry;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class is the general plugin element
 */
public abstract class Plugin implements Comparable<Plugin>
{
    // Constantes
    public static final int PLUGIN_TYPE_FEATURE = 0x01;
    public static final int PLUGIN_TYPE_PORTLET = 0x02;
    public static final int PLUGIN_TYPE_APPLICATION = 0x04;
    public static final int PLUGIN_TYPE_INSERTSERVICE = 0x08;
    public static final int PLUGIN_TYPE_CONTENTSERVICE = 0x10;
    public static final int PLUGIN_TYPE_DAEMON = 0x20;
    private static final String PROPERTY_DEFAULT_ICON_URL = "plugin.image.defaultIconUrl";

    // Variables
    private String _strName;
    private String _strVersion;
    private String _strDescription;
    private String _strProvider;
    private String _strProviderUrl;
    private String _strCopyright;
    private String _strPluginClass;
    private String _strDbPoolName;
    private String _strIconUrl;
    private boolean _bIsInstalled;
    private boolean _bDbPoolRequired;
    private ContentService _contentService;
    private String _strCssStylesheet; // added in v1.4.1
    private String _strJavascriptFile; // added in v1.4.1

    // Lists of rights and portlets of the plugin
    private List<XPageApplicationEntry> _listXPageApplications;
    private List<Right> _listRights;
    private List<PortletType> _listPortletTypes;
    private List<ContentServiceEntry> _listContentServices;
    private List<SearchIndexerEntry> _listSearchIndexers;
    private List<InsertService> _listInsertServices;
    private List<PageIncludeEntry> _listPageIncludes;
    private List<RBACResourceTypeEntry> _listRBACResourceTypes;
    private List<DaemonEntry> _listDaemons;

    //hashtable which contains all the params described in the xml plugin file
    private Map<String, String> _mapParams = new HashMap<String, String>(  );
    private PluginConnectionService _connectionService;

    /**
     * Initializes the plugin at the first load
     *
     */
    public abstract void init(  );

    /**
     * Load plugin's data from the plugin's xml file.
     *
     * @param pluginFile The plugin file object
     */
    void load( PluginFile pluginFile ) throws LuteceInitException
    {
        try
        {
            _strName = pluginFile.getName(  );
            _strVersion = pluginFile.getVersion(  );
            _strDescription = pluginFile.getDescription(  );
            _strProvider = pluginFile.getProvider(  );
            _strProviderUrl = pluginFile.getProviderUrl(  );

            String strDefaultIconUrl = AppPropertiesService.getProperty( PROPERTY_DEFAULT_ICON_URL );
            _strIconUrl = pluginFile.getIconUrl(  ).equals( "" ) ? strDefaultIconUrl : pluginFile.getIconUrl(  );
            _strCopyright = pluginFile.getCopyright(  );
            _strPluginClass = pluginFile.getPluginClass(  );
            _listXPageApplications = pluginFile.getXPageApplications(  );
            _listRights = pluginFile.getRights(  );
            _listPortletTypes = pluginFile.getPortletTypes(  );
            _listContentServices = pluginFile.getContentServices(  );
            _listInsertServices = pluginFile.getInsertServices(  );
            _listSearchIndexers = pluginFile.getSearchIndexers(  );
            _listPageIncludes = pluginFile.getPageIncludes(  );
            _listRBACResourceTypes = pluginFile.getRBACResourceTypes(  );
            _listDaemons = pluginFile.getDaemons(  );
            _mapParams = pluginFile.getParams(  );
            _bDbPoolRequired = pluginFile.isDbPoolRequired(  );

            // Css Stylesheet added in v1.4
            _strCssStylesheet = pluginFile.getCssStylesheet(  );
            // Javascript File Stylesheet added in v1.4
            _strJavascriptFile = pluginFile.getJavascriptFile(  );

            // Register plugin components
            registerXPageApplications(  );
            registerContentServices(  );
            registerInsertServices(  );
            registerSearchIndexers(  );
            registerPageIncludes(  );
            registerRBACResourceTypes(  );
            registerDaemons(  );
        }
        catch ( Exception e )
        {
            throw new LuteceInitException( "Error loading plugin : " + e.getMessage(  ), e );
        }
    }

    /**
     *
     * @return the content service
     */
    public ContentService getContentService(  )
    {
        return _contentService;
    }

    /**
     * Returns weither or not plugin has portlet.
     *
     * @return true if the plugin contains one or more portlet
     */
    public boolean hasPortlets(  )
    {
        return ( _listPortletTypes.size(  ) > 0 );
    }

    /**
     * Returns weither or not plugin has daemon.
     *
     * @return true if the plugin contains one or more daemon
     */
    public boolean hasDaemons(  )
    {
        return ( _listDaemons.size(  ) > 0 );
    }

    /**
     * Returns The daemons list of the plugin.
     *
     * @return The daemons list of the plugin
     */
    public List<DaemonEntry> getDaemons(  )
    {
        return _listDaemons;
    }

    /**
     * Updates the plg file
     */
    protected void update(  )
    {
        PluginService.updatePluginData( this );
    }

    /**
     * Modify the plugin status
     *
     * @param bStatus true installed, false uninstalled
     */
    public void setStatus( boolean bStatus )
    {
        _bIsInstalled = bStatus;
    }

    /**
     * Updates a database connection pool associated to the plugin and stores it
     * @param strPoolName the name of the pool
     */
    public void updatePoolName( String strPoolName )
    {
        _strDbPoolName = strPoolName;
        _connectionService.setPool( strPoolName );
        update(  );
    }

    /**
     * Updates a database connection pool associated to the plugin and stores it
     * @param strPoolName The name of the pool
     */
    public void setPoolName( String strPoolName )
    {
        _strDbPoolName = strPoolName;
    }

    /**
     * Gets the current database connection pool associated to the plugin
     * @return The name of the database for the pool checked
     */
    public String getDbPoolName(  )
    {
        return _strDbPoolName;
    }

    /**
     * Creates a new right in the rights set
     */
    protected void registerRights(  )
    {
        for ( Right right : _listRights )
        {
            if ( ( right != null ) && ( !( right.getId(  ).equals( "" ) ) ) )
            {
                RightHome.create( right );
            }
        }
    }

    /**
     * Remove a right from the rights set.
     */
    protected void unregisterRights(  )
    {
        for ( Right right : _listRights )
        {
            if ( ( right != null ) && ( !( right.getId(  ).equals( "" ) ) ) )
            {
                RightHome.remove( right.getId(  ) );
            }
        }
    }

    /**
     * Creates a new portlet in the portlets type set
     */
    protected void registerPortlets(  )
    {
        for ( PortletType portletType : _listPortletTypes )
        {
            if ( ( portletType.getHomeClass(  ) != null ) && ( !( portletType.getHomeClass(  ).equals( "" ) ) ) )
            {
                PortletTypeHome.create( portletType );
            }
        }
    }

    /**
     * Remove a portlet from the portlets type set.
     */
    protected void unregisterPortlets(  )
    {
        for ( PortletType portletType : _listPortletTypes )
        {
            PortletTypeHome.remove( portletType.getId(  ) );
        }
    }

    /**
     * Register XPage applications
     */
    protected void registerXPageApplications(  ) throws LuteceInitException
    {
        for ( XPageApplicationEntry entry : _listXPageApplications )
        {
            entry.setPluginName( getName(  ) );
            XPageAppService.registerXPageApplication( entry );
        }
    }

    /**
     * Register Content Services
     */
    protected void registerContentServices(  ) throws LuteceInitException
    {
        for ( ContentServiceEntry entry : _listContentServices )
        {
            try
            {
                ContentService cs = (ContentService) Class.forName( entry.getClassName(  ) ).newInstance(  );
                PortalService.registerContentService( cs.getName(  ), cs );
            }
            catch ( IllegalAccessException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
            catch ( ClassNotFoundException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
            catch ( InstantiationException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
        }
    }

    /**
     * Register Insert Services
     */
    protected void registerInsertServices(  ) throws LuteceInitException
    {
        for ( InsertService is : _listInsertServices )
        {
            is.setPluginName( getName(  ) );
            InsertServiceManager.registerInsertService( is );
        }
    }

    /**
     * Register Search Indexers
     */
    protected void registerSearchIndexers(  ) throws LuteceInitException
    {
        for ( SearchIndexerEntry entry : _listSearchIndexers )
        {
            try
            {
                SearchIndexer indexer = (SearchIndexer) Class.forName( entry.getClassName(  ) ).newInstance(  );
                IndexationService.registerIndexer( indexer );
            }
            catch ( IllegalAccessException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
            catch ( ClassNotFoundException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
            catch ( InstantiationException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
        }
    }

    /**
     * Register Page Includes
     */
    protected void registerPageIncludes(  ) throws LuteceInitException
    {
        for ( PageIncludeEntry entry : _listPageIncludes )
        {
            entry.setPluginName( getName(  ) );
            PageIncludeService.registerPageInclude( entry );
        }
    }

    /**
     * Register RBAC Resource Types
     */
    protected void registerRBACResourceTypes(  ) throws LuteceInitException
    {
        for ( RBACResourceTypeEntry entry : _listRBACResourceTypes )
        {
            ResourceIdService ris;

            try
            {
                ris = (ResourceIdService) Class.forName( entry.getClassName(  ) ).newInstance(  );
                // Each resource id service should register itself and its permissions
                ris.register(  );
            }
            catch ( IllegalAccessException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
            catch ( ClassNotFoundException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
            catch ( InstantiationException e )
            {
                throw new LuteceInitException( e.getMessage(  ), e );
            }
        }
    }

    /**
     * Register Daemons
     */
    protected void registerDaemons(  ) throws LuteceInitException
    {
        for ( DaemonEntry entry : _listDaemons )
        {
            entry.setPluginName( getName(  ) );
            AppDaemonService.registerDaemon( entry );
        }
    }

    /**
     * Remove a portlet from the portlets type set.
     */
    protected void unregisterDaemons(  )
    {
        for ( DaemonEntry daemonEntry : _listDaemons )
        {
            //            AppDaemonService.unregisterDaemon( daemonEntry );
        }
    }

    /**
     * Installs a Plugin
     */
    public void install(  )
    {
        // Register a new right for the plugin
        registerRights(  );

        // Register a new portlets as plugin
        registerPortlets(  );
        _bIsInstalled = true;
        update(  );
    }

    /**
     * Uninstalls a Plugin
     */
    public void uninstall(  )
    {
        // Unregister a new right for the plugin
        unregisterRights(  );

        // Unregister a new portlets as plugin
        unregisterPortlets(  );
        _bIsInstalled = false;
        update(  );
    }

    /**
     * Returns the type of the plugin
     *
     * @return the plugin type as a int
     */
    public int getType(  )
    {
        // Load the Type
        int nPluginTypeFlags = 0;

        if ( ( _listXPageApplications != null ) && ( _listXPageApplications.size(  ) > 0 ) )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_APPLICATION;
        }

        if ( ( _listPortletTypes != null ) && ( _listPortletTypes.size(  ) > 0 ) )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_PORTLET;
        }

        if ( ( _listRights != null ) && ( _listRights.size(  ) > 0 ) )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_FEATURE;
        }

        if ( ( _listInsertServices != null ) && ( _listInsertServices.size(  ) > 0 ) )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_INSERTSERVICE;
        }

        if ( ( _listContentServices != null ) && ( _listContentServices.size(  ) > 0 ) )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_CONTENTSERVICE;
        }

        if ( ( _listDaemons != null ) && ( _listDaemons.size(  ) != 0 ) )
        {
            nPluginTypeFlags |= PLUGIN_TYPE_DAEMON;
        }

        return nPluginTypeFlags;
    }

    /**
     * Returns the list of insert services of the plugin
     *
     * @return the plugin list of ContentServiceEntry
     */
    public List<InsertService> getInsertServices(  )
    {
        return _listInsertServices;
    }

    /**
     * Returns the list of Content services of the plugin
     *
     * @return the plugin list of ContentServiceEntry
     */
    public List<ContentServiceEntry> getContentServices(  )
    {
        return _listContentServices;
    }

    /**
     * Returns the list of XPage Applications of the plugin
     *
     * @return the plugin list of XPageApplicationEntry
     */
    public List<XPageApplicationEntry> getApplications(  )
    {
        return _listXPageApplications;
    }

    /**
     * Returns the list of portlet type of the plugin
     *
     * @return the plugin list of portlet type
     */
    public List<PortletType> getPortletTypes(  )
    {
        return _listPortletTypes;
    }

    /**
     * Sets the list of portlet type
     *
     * @param listPortletTypes The portlet type list
     */
    public void setPortletTypes( List<PortletType> listPortletTypes )
    {
        _listPortletTypes = listPortletTypes;
    }

    /**
     * Returns the list of portlet type of the plugin
     *
     * @return the plugin list of rights
     */
    public List<Right> getRights(  )
    {
        return _listRights;
    }

    /**
     * Sets plugin rights list
     *
     * @param vRights The rights list
     */
    public void setRights( List<Right> listRights )
    {
        _listRights = listRights;
    }

    /**
     * Returns the name of the plugin
     *
     * @return the plugin name as a String
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the name of the plugin
     *
     * @param strName The plugin name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the version of the plugin
     *
     * @return the plugin version as a String
     */
    public String getVersion(  )
    {
        return _strVersion;
    }

    /**
     * Sets the version plugin name
     * @param strVersion The version
     */
    public void setVersion( String strVersion )
    {
        _strVersion = strVersion;
    }

    /**
     * Returns the description of the plugin
     *
     * @return the plugin description as a String
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the description of the plugin
     *
     * @param strDescription The description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Returns the Provider of the plugin
     *
     * @return the plugin Provider as a String
     */
    public String getProvider(  )
    {
        return _strProvider;
    }

    /**
     * Sets the provider name
     *
     * @param strProvider The provider name
     */
    public void setProvider( String strProvider )
    {
        _strProvider = strProvider;
    }

    /**
     * Returns the Provider's URL of the plugin
     *
     * @return the plugin Provider's URL as a String
     */
    public String getProviderUrl(  )
    {
        return _strProviderUrl;
    }

    /**
     * Sets the provider url
     *
     * @param strProviderUrl the name of the provider
     */
    public void setProviderUrl( String strProviderUrl )
    {
        _strProviderUrl = strProviderUrl;
    }

    /**
     * Returns the Icon's URL of the plugin
     *
     * @return the plugin Icon's URL as a String
     */
    public String getIconUrl(  )
    {
        return _strIconUrl;
    }

    /**
     * Sets the url of the plugin's icon
     *
     * @param strIconUrl The url of icon
     */
    public void setIconUrl( String strIconUrl )
    {
        _strIconUrl = strIconUrl;
    }

    /**
     * Returns the Copyright of the plugin
     *
     * @return the plugin Copyright as a String
     */
    public String getCopyright(  )
    {
        return _strCopyright;
    }

    /**
     * Sets the copyright
     *
     * @param strCopyright The copyright
     */
    public void setCopyright( String strCopyright )
    {
        _strCopyright = strCopyright;
    }

    /**
     * Returns the main Class of the plugin
     *
     * @return the Class as a String
     */
    public String getServiceClass(  )
    {
        return _strPluginClass;
    }

    /**
     * Sets the class service of plugin
     *
     * @param strPluginClass The plugin class
     */
    public void setServiceClass( String strPluginClass )
    {
        _strPluginClass = strPluginClass;
    }

    /**
     * Returns the installation status of the plugin
     *
     * @return the installation status as an int
     */
    public boolean isInstalled(  )
    {
        return _bIsInstalled;
    }

    /**
     * Sets the boolean which shows if the plugin is installed
     *
     * @param bIsInstalled The installed boolean
     */
    public void setIsInstalled( boolean bIsInstalled )
    {
        _bIsInstalled = bIsInstalled;
    }

    /**
     * Returns if the plugin needs a database connection pool
     *
     * @return <b>true</b> if the plugin needs a database connection pool, otherwise <b>false</b>
     */
    public boolean isDbPoolRequired(  )
    {
        return _bDbPoolRequired;
    }

    /**
     * Sets the boolean which shows if a pool is required
     *
     * @param bDbPoolRequired The dbpool boolean
     */
    public void setIsDbPoolRequired( boolean bDbPoolRequired )
    {
        _bDbPoolRequired = bDbPoolRequired;
    }

    /**
     * Returns a Connection Service associated to the plugin
     * @return _connectionService The connection service
     */
    public PluginConnectionService getConnectionService(  )
    {
        return _connectionService;
    }

    /**
     * Sets the connection service
     *
     * @param connectionService The connection Service object
     */
    public void setConnectionService( PluginConnectionService connectionService )
    {
        _connectionService = connectionService;
    }

    /**
     * Initializes the plugin's ConnectionService
     *
     * @param strPoolName The pool name
     */
    public void initConnectionService( String strPoolName )
    {
        _connectionService = new PluginConnectionService( strPoolName );
    }

    /**
     * Gets plugin's parameters
     * @return _mapParams The hastable of parameters
     */
    public Map<String, String> getParams(  )
    {
        return _mapParams;
    }

    /**
     * Gets a parameter value for a given parameter name
     * @param strParamName The name of the parameter
     * @return null
     */
    public String getParamValue( String strParamName )
    {
        if ( !_mapParams.containsKey( strParamName ) )
        {
            return null;
        }

        return _mapParams.get( strParamName );
    }

    /**
     * Sets parameters values with an hashtable
     * @param htParams The hashtable of the parameters
     */
    public void setParams( Map<String, String> mapParams )
    {
        _mapParams = mapParams;
    }

    /**
     * Sets a parameter value for a given parameter name
     * @param strParamName The name of the parameter
     * @param strParamValue The value of the parameter
     */
    public void setParamValue( String strParamName, String strParamValue )
    {
        if ( _mapParams.containsKey( strParamName ) )
        {
            _mapParams.put( strParamName, strParamValue );
        }
    }

    /**
     * Implementation of the Comparable interface.
     * @param o A plugin Object
     * @return 1, 0 ou -1 according the plugin name
     */
    public int compareTo( Plugin plugin )
    {
        Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;

        return comparator.compare( this.getName(  ), plugin.getName(  ) );
    }

    /**
     * Returns the CssStylesheet
     *
     * @return The CssStylesheet
     * @since 1.4.1
     */
    public String getCssStylesheet(  )
    {
        return _strCssStylesheet;
    }

    /**
     * Sets the CssStylesheet
     *
     * @param strCssStylesheet The CssStylesheet
     * @since 1.4.1
     */
    public void setCssStylesheet( String strCssStylesheet )
    {
        _strCssStylesheet = strCssStylesheet;
    }

    /**
     * Returns the JavascriptFile
     * @return The JavascriptFile
     * @since 1.4.1
     */
    public String getJavascriptFile(  )
    {
        return _strJavascriptFile;
    }

    /**
     * Sets the JavascriptFile
     *
     * @param strJavascriptFile The JavascriptFile
     * @since 1.4.1
     */
    public void setJavascriptFile( String strJavascriptFile )
    {
        _strJavascriptFile = strJavascriptFile;
    }
}
