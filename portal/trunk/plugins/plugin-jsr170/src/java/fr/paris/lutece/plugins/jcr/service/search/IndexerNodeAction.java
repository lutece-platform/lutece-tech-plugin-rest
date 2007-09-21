package fr.paris.lutece.plugins.jcr.service.search;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import org.apache.lucene.demo.html.HTMLParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import fr.paris.lutece.plugins.indexer.service.IFileIndexer;
import fr.paris.lutece.plugins.indexer.service.IFileIndexerFactory;
import fr.paris.lutece.plugins.jcr.business.INodeAction;
import fr.paris.lutece.plugins.jcr.business.IRepositoryFile;
import fr.paris.lutece.plugins.jcr.business.admin.AdminWorkspace;
import fr.paris.lutece.portal.service.search.SearchItem;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.url.UrlItem;

/**
 * Implementation of INodeAction for indexing the nodes of a JCR
 * 
 * It stores results using TreeSet and specific Comparator
 */
public class IndexerNodeAction implements INodeAction<Document, Collection<Document>>
{
    private final static String DISPLAY_FILE_BASE_URL = "jsp/site/plugins/jsr170/DisplayFile.jsp";
    private final static String PARAMETER_FILE_ID = "file_id";
    private final static String PARAMETER_WORKSPACE_ID = "workspace_id";

    private Comparator<Document> _nodeComparator;
    private String _strPluginName;
    private AdminWorkspace _adminWorkspace;

    /**
     * Default constructor
     * @param nodeComparator the comparator used to store the results in the collection
     * @param strPluginName the plugin name to retrieve Spring context
     * @param adminWorkspace the adminWorkspace to work on
     */
    public IndexerNodeAction( Comparator<Document> nodeComparator, String strPluginName, AdminWorkspace adminWorkspace )
    {
        _nodeComparator = nodeComparator;
        _strPluginName = strPluginName;
        _adminWorkspace = adminWorkspace;
    }

    /**
     * @return
     * @see fr.paris.lutece.plugins.jcr.business.INodeAction#createCollection()
     */
    public Collection<Document> createCollection( )
    {
        return new TreeSet<Document>( _nodeComparator );
    }
    
    /**
     * Creates a lucene Document using the JCR file
     * @param file 
     * @return a Document or null if this node should not be indexed
     * @see fr.paris.lutece.plugins.jcr.business.INodeAction#doAction(fr.paris.lutece.plugins.jcr.business.IRepositoryFile)
     */
    public Document doAction( IRepositoryFile file )
    {
        if( file.isDirectory( ) )
        {
            return null;
        }

        try
        {
            Document document = new Document( );
            if(file.isFile( ) )
            {
                Reader reader = getContentToIndex( file );
                document.add( new Field(
                        SearchItem.FIELD_CONTENTS,
                        reader ) );
                DateFormat formater = DateFormat.getDateInstance( DateFormat.SHORT );
                String strDate = formater.format( file.lastModified( ).getTime( ) );

                document.add( new Field(
                        SearchItem.FIELD_DATE,
                        strDate,
                        Field.Store.YES,
                        Field.Index.UN_TOKENIZED ) );
            }
            document.add( new Field(
                    SearchItem.FIELD_SUMMARY, file
                            .getName( ),
                    Field.Store.YES,
                    Field.Index.UN_TOKENIZED ) );
            document.add( new Field(
                    SearchItem.FIELD_TITLE, file
                            .getName( ),
                    Field.Store.YES,
                    Field.Index.UN_TOKENIZED ) );
            document.add( new Field(
                    SearchItem.FIELD_TYPE, _strPluginName,
                    Field.Store.YES,
                    Field.Index.UN_TOKENIZED ) );
            document.add( new Field(
                    SearchItem.FIELD_UID, file
                            .getResourceId( ) == null ? String.valueOf( file.hashCode( ) ): _adminWorkspace.getId( ) + "-" + file.getResourceId( ),
                    Field.Store.YES,
                    Field.Index.UN_TOKENIZED ) );

            UrlItem url = new UrlItem( DISPLAY_FILE_BASE_URL );
            url.addParameter( PARAMETER_FILE_ID, file.getResourceId( ) );
            url.addParameter( PARAMETER_WORKSPACE_ID, _adminWorkspace.getId( ) ); 
            document.add( new Field(
                    SearchItem.FIELD_URL, url.getUrl( ),
                    Field.Store.YES,
                    Field.Index.UN_TOKENIZED ) );
            return document;
        }
        catch ( IOException e )
        {
            return null;
        }
    }

    /**
     * Indexes a file content
     * @param file
     * @return a Reader that can be used to retrieve indexed content
     * @throws IOException
     */
    private Reader getContentToIndex( IRepositoryFile file ) throws IOException
    {
        // Gets indexer depending on the ContentType (ie: "application/pdf"
        // should use a PDF indexer)
        IFileIndexerFactory factory = (IFileIndexerFactory) SpringContextService.getPluginBean( _strPluginName, "fileIndexerFactory" );
        IFileIndexer indexer = factory.getIndexer( file.getMimeType( ) );
        if ( indexer != null )
        {
            return new LazyIndexedContentReader( indexer, file.getContent( ) );
        }
        else if( file.getMimeType( ).startsWith( "text/" ))
        {
            Reader readerPage = new InputStreamReader ( file.getContent( ));

            HTMLParser parser = new HTMLParser( readerPage );

            //the content of the article is recovered in the parser because this one
            //had replaced the encoded caracters (as &eacute;) by the corresponding special caracter (as ?)
            return parser.getReader(  );
        }
        return new StringReader( "" );
    }
    

}
