package fr.paris.lutece.plugins.indexer.service;

import java.util.Map;

public interface IFileIndexerFactory
{
    
    void setIndexersMap( Map mapIndexers );
    
    IFileIndexer getIndexer( String strMimeType );
    
}