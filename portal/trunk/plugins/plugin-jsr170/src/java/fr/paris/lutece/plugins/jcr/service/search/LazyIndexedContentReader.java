package fr.paris.lutece.plugins.jcr.service.search;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import fr.paris.lutece.plugins.indexer.service.IFileIndexer;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * Implementation of a lazy reader for indexed content (such as PDF).
 * 
 * The file content is indexed and parsed only when needed.
 */
public class LazyIndexedContentReader extends Reader
{
    private IFileIndexer _indexer;
    private InputStream _is;
    private StringReader _contentReader;

    public LazyIndexedContentReader( IFileIndexer indexer, InputStream is)
    {
        _indexer = indexer;
        _is = is;
    }

    @Override
    public void close( ) throws IOException
    {
        getReader( ).close( );
    }
    
    @Override
    public int read( char[] cbuf, int off, int len ) throws IOException
    {
        return getReader( ).read( cbuf, off, len );
    }

    /**
     * Return the cached Reader
     * @return
     */
    private Reader getReader( )
    {
        if ( _contentReader == null )
        {
            AppLogService.debug( "Initializing reader for stream... " + _is );
            _contentReader = new StringReader( _indexer.getContentToIndex( _is ) );
        }
        return _contentReader;
    }
}
