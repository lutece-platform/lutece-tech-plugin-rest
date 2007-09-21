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
package fr.paris.lutece.util.date;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import java.util.Locale;


/**
 * This class provides date utils.
 */
public final class DateUtil
{
    private static SimpleDateFormat _formatter = new SimpleDateFormat( "dd'/'MM'/'yyyy", Locale.FRANCE );
    private static SimpleDateFormat _formatterDateTime = new SimpleDateFormat( "dd'/'MM'/'yyyy' 'HH':'mm", Locale.FRANCE );

    /**
     * Creates a new DateUtil object
     */
    private DateUtil(  )
    {
    }

    /**
     * Returns the date of the day in form of a String
     *
     * @return The Date of the day in a  "JJ/MM/AAAA" format
     */
    public static synchronized String getCurrentDateString(  )
    {
        return _formatter.format( new java.util.Date(  ) );
    }

    /**
     * Converts a String date in a "jj/mm/aaaa" format in a java.sql.Date type date
     *
     * @param strDate The String Date to convert, in a date in the "jj/mm/aaaa" format
     * @return The date in form of a java.sql.Date type date
     */
    public static synchronized java.sql.Date getDateSql( String strDate )
    {
        ParsePosition pos = new ParsePosition( 0 );
        java.util.Date date = _formatter.parse( strDate, pos );

        if ( date != null )
        {
            return new java.sql.Date( date.getTime(  ) );
        }

        return null;
    }

    /**
     * Converts a String date in a "jj/mm/aaaa" format in a java.util.Date type date
     *
     * @param strDate The String Date to convert, in a date in the "jj/mm/aaaa" format
     * @return The date in form of a java.sql.Date tyep date
     */
    public static synchronized java.util.Date getDate( String strDate )
    {
        ParsePosition pos = new ParsePosition( 0 );
        java.util.Date date = _formatter.parse( strDate, pos );

        return date;
    }

    /**
     * Converts a String date in a "jj/mm/aaaa" format in a java.sql.Timestamp type date
     *
     * @param strDate The String Date to convert, in a date in the "jj/mm/aaaa" format
     * @return The date in form of a java.sql.Date tyep date
     */
    public static synchronized java.sql.Timestamp getTimestamp( String strDate )
    {
        ParsePosition pos = new ParsePosition( 0 );
        java.util.Date date = _formatter.parse( strDate, pos );

        if ( date != null )
        {
            return ( new java.sql.Timestamp( date.getTime(  ) ) );
        }

        return null;
    }

    /**
     * Converts a java.sql.Date type date in a String date with a "jj/mm/aaaa" format
     *
     * @param date java.sql.Date date to convert
     * @return strDate The date converted to String in a "jj/mm/aaaa" format or an empty String if the date is null
     */
    public static synchronized String getDateString( java.sql.Date date )
    {
        if ( date != null )
        {
            StringBuffer strDate = new StringBuffer(  );
            _formatter.format( date, strDate, new FieldPosition( 0 ) );

            return strDate.toString(  );
        }

        return "";
    }

    ///////////////////////////////////////////////////////////////////////////
    // methodes using the java.sql.Timestamp type

    /**
     * Converts une java.sql.Timestamp date in a String date in a "jj/mm/aaaa" format
     *
     * @param date java.sql.Timestamp date to convert
     * @return strDate The String date in a "jj/mm/aaaa" format or the emmpty String if the date is null
     */
    public static synchronized String getDateString( java.sql.Timestamp date )
    {
        if ( date != null )
        {
            StringBuffer strDate = new StringBuffer(  );
            _formatter.format( date, strDate, new FieldPosition( 0 ) );

            return strDate.toString(  );
        }

        return "";
    }

    ///////////////////////////////////////////////////////////////////////////
    // methodes using the java.util.Date type

    /**
     * Converts a java.util.Date date in a String date in a "jj/mm/aaaa" format
     *
     * @param date java.util.Date date to convert
     * @return strDate A String date in a "jj/mm/aaaa" format or an empty String if the date is null
     */
    public static synchronized String getDateString( java.util.Date date )
    {
        if ( date != null )
        {
            StringBuffer strDate = new StringBuffer(  );
            _formatter.format( date, strDate, new FieldPosition( 0 ) );

            return strDate.toString(  );
        }

        return "";
    }

    ///////////////////////////////////////////////////////////////////////////
    // methods using a long value

    /**
     * Converts a long value to a String date in a "jj/mm/aaaa hh:mm" format
     *
     * @param lTime The long value to convert
     * @return The formatted string
     */
    public static synchronized String getDateTimeString( long lTime )
    {
        StringBuffer strDate = new StringBuffer(  );
        _formatterDateTime.format( new java.util.Date( lTime ), strDate, new FieldPosition( 0 ) );

        return strDate.toString(  );
    }
}
