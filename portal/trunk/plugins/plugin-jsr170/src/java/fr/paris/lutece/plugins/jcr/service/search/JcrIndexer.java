package fr.paris.lutece.plugins.jcr.service.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.apache.lucene.document.Document;
import org.springframework.beans.BeansException;
import org.springframework.dao.DataAccessException;

import fr.paris.lutece.plugins.jcr.business.RepositoryFileHome;
import fr.paris.lutece.plugins.jcr.business.admin.AdminJcrHome;
import fr.paris.lutece.plugins.jcr.business.admin.AdminView;
import fr.paris.lutece.plugins.jcr.business.admin.AdminWorkspace;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.search.SearchIndexer;
import fr.paris.lutece.portal.service.search.SearchItem;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * JCR indexer implementation.
 * 
 */
public class JcrIndexer implements SearchIndexer
{
    private static final String INDEXER_DESCRIPTION = "Indexer service for JCR";
    private static final String INDEXER_VERSION = "1.0.0";
    private static final String PLUGIN_NAME = "jsr170";

    /**
     * @return
     * @see fr.paris.lutece.portal.service.search.SearchIndexer#getDescription()
     */
    public String getDescription( )
    {
        return INDEXER_DESCRIPTION;
    }
    
    /**
     * Indexes all files among all defined views.
     * 
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws SiteMessageException
     * @see fr.paris.lutece.portal.service.search.SearchIndexer#getDocuments()
     */
    public List<Document> getDocuments( ) throws IOException,
            InterruptedException, SiteMessageException
    {
        Plugin plugin = PluginService.getPlugin( PLUGIN_NAME );
        
        // definition of the comparator used in the collected files
        // thus there can't be two Documents with the same UID Field 
        final Comparator<Document> documentComparator = new Comparator<Document> ()
        {
            public int compare( Document o1, Document o2 )
            {
                if( o1 != null && o2 != null )
                {
                    return o1.getField( SearchItem.FIELD_UID ).stringValue( ).compareTo( o2.getField( SearchItem.FIELD_UID ).stringValue( ) );
                }
                else if ( o1 == null && o2 != null )
                {
                    return -1;
                }
                else if ( o1 != null && o2 == null )
                {
                    return 1;
                }
                return 0;
            }
    
        };

        Collection<Document> documentList = new TreeSet<Document>( documentComparator );
        for ( AdminView view : AdminJcrHome.getInstance( )
                .findAllViews( plugin ) )
        {
            final AdminWorkspace adminWorkspace = AdminJcrHome.getInstance( )
                    .findWorkspaceById( view.getWorkspaceId( ), plugin );
            
            try
            {
                documentList.addAll( RepositoryFileHome
                        .getInstance( ).doRecursive( adminWorkspace,
                                view, view.getPath( ),
                                new IndexerNodeAction( documentComparator, PLUGIN_NAME, adminWorkspace ) ) );
            }
            catch(BeansException e)
            {
                AppLogService.error( e );
            }
            catch(DataAccessException e)
            {
                AppLogService.error( e );
            }
        }
        return new ArrayList<Document>( documentList );
    }
    
    /**
     * @return
     * @see fr.paris.lutece.portal.service.search.SearchIndexer#getName()
     */
    public String getName( )
    {
        return this.getClass( ).getSimpleName( );
    }
    
    /**
     * @return
     * @see fr.paris.lutece.portal.service.search.SearchIndexer#getVersion()
     */
    public String getVersion( )
    {
        return INDEXER_VERSION;
    }
    
    /**
     * @return
     * @see fr.paris.lutece.portal.service.search.SearchIndexer#isEnable()
     */
    public boolean isEnable( )
    {
        return true;
    }
    
}
