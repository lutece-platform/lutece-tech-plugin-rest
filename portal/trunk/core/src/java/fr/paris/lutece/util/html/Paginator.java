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
package fr.paris.lutece.util.html;

import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * Paginator provides a way to display a collection of items on severals pages.
 */
public class Paginator
{
    /** Default value for Page Index Parameter */
    public static final String PARAMETER_PAGE_INDEX = "page_index";

    /** Default value for Items Per Page Parameter */
    public static final String PARAMETER_ITEMS_PER_PAGE = "items_per_page";

    // Variables declarations
    private String _strPageIndexParameterName;
    private int _nItemPerPage;
    private String _strBaseUrl;
    private List _list;
    private int _nPageCurrent;

    /**
     * Creates a new instance of Paginator
     * @param list The collection to paginate
     * @param nItemPerPage Number of items to display per page
     * @param strBaseUrl The base Url for build links on each page link
     * @param strPageIndexParameterName The parameter name for the page index
     * @param strPageIndex The current page index
     */
    public Paginator( List list, int nItemPerPage, String strBaseUrl, String strPageIndexParameterName,
        String strPageIndex )
    {
        _list = list;
        _nItemPerPage = nItemPerPage;
        _strBaseUrl = strBaseUrl;
        _strPageIndexParameterName = strPageIndexParameterName;

        try
        {
            _nPageCurrent = Integer.parseInt( strPageIndex );
        }
        catch ( NumberFormatException e )
        {
            // strPageIndex invalid, use 1 as default page index.
            _nPageCurrent = 1;
        }

        if ( _nPageCurrent > getPagesCount(  ) )
        {
            _nPageCurrent = 1;
        }
    }

    /**
     * Gets the number of pages
     * @return the number of pages
     */
    public int getPagesCount(  )
    {
        return ( ( _list.size(  ) - 1 ) / _nItemPerPage ) + 1;
    }

    /**
     * Returns the List
     *
     * @return The List
     */
    public List getPageItems(  )
    {
        int nStartIndex = ( _nPageCurrent - 1 ) * _nItemPerPage;
        ArrayList list = new ArrayList(  );

        for ( int i = nStartIndex; ( i < ( nStartIndex + _nItemPerPage ) ) && ( i < _list.size(  ) ); i++ )
        {
            list.add( _list.get( i ) );
        }

        return list;
    }

    /**
     * Returns the current page index
     *
     * @return The current page index
     */
    public int getPageCurrent(  )
    {
        return _nPageCurrent;
    }

    /**
     * Returns the previous page link
     *
     * @return The previous page link
     */
    public String getPreviousPageLink(  )
    {
        int nPreviousIndex = _nPageCurrent - 1;
        UrlItem url = new UrlItem( _strBaseUrl );
        url.addParameter( _strPageIndexParameterName, "" + nPreviousIndex );

        return url.getUrl(  );
    }

    /**
     * Returns the previous page link
     *
     * @return The previous page link
     */
    public String getNextPageLink(  )
    {
        int nNextIndex = _nPageCurrent + 1;
        UrlItem url = new UrlItem( _strBaseUrl );
        url.addParameter( _strPageIndexParameterName, "" + nNextIndex );

        return url.getUrl(  );
    }

    /**
     * Returns Pages Links
     *
     * @return Pages Links
     */
    public List<PaginatorPage> getPagesLinks(  )
    {
        ArrayList<PaginatorPage> list = new ArrayList<PaginatorPage>(  );

        for ( int i = 1; i <= getPagesCount(  ); i++ )
        {
            PaginatorPage page = new PaginatorPage(  );
            String strIndex = "" + i;
            UrlItem url = new UrlItem( _strBaseUrl );
            url.addParameter( _strPageIndexParameterName, strIndex );
            page.setUrl( url.getUrl(  ) );
            page.setName( strIndex );
            page.setIndex( i );
            list.add( page );
        }

        return list;
    }

    /**
     * Returns the index of the first item of the current page range
     * @return The index of the first item of the current page range
     */
    public int getRangeMin(  )
    {
        return ( _list.size(  ) != 0 ) ? ( ( _nItemPerPage * ( _nPageCurrent - 1 ) ) + 1 ) : 0;
    }

    /**
     * Returns the index of the last item of the current page range
     * @return The index of the last item of the current page range
     */
    public int getRangeMax(  )
    {
        return ( _list.size(  ) < ( ( _nItemPerPage * _nPageCurrent ) - 1 ) ) ? _list.size(  )
                                                                              : ( _nItemPerPage * _nPageCurrent );
    }

    /**
     * Returns the number of items in the collection
     * @return The number of items in the collection
     */
    public int getItemsCount(  )
    {
        return _list.size(  );
    }

    /**
     * Gets the number of items per page from a request parameter
     * @param request The HTTP request
     * @param strParameter The request parameter name
     * @param nCurrent The current number of items
     * @param nDefault The default number of items
     * @return The new number of items
     */
    public static int getItemsPerPage( HttpServletRequest request, String strParameter, int nCurrent, int nDefault )
    {
        int nItemsPerPage;
        String strItemsPerPage = request.getParameter( strParameter );

        if ( strItemsPerPage != null )
        {
            nItemsPerPage = Integer.parseInt( strItemsPerPage );
        }
        else
        {
            if ( nCurrent != 0 )
            {
                nItemsPerPage = nCurrent;
            }
            else
            {
                nItemsPerPage = nDefault;
            }
        }

        return nItemsPerPage;
    }

    /**
     * Gets the new page index from a request parameter
     * @param request The HTTP request
     * @param strParameter The request parameter name
     * @param strCurrentPageIndex The current page index
     * @return The new page index
     */
    public static String getPageIndex( HttpServletRequest request, String strParameter, String strCurrentPageIndex )
    {
        String strPageIndex = request.getParameter( strParameter );
        strPageIndex = ( strPageIndex != null ) ? strPageIndex : strCurrentPageIndex;

        return strPageIndex;
    }
}
