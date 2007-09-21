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
package fr.paris.lutece.plugins.calendar.service;

import fr.paris.lutece.plugins.calendar.web.Constants;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * This class provides utils features to manipulate and convert calendars, date as string, ...
 */
public final class Utils
{
    /**
     * Constructs a 8 digits date string code YYYYMMDD
     * @param nYear The Year
     * @param nMonth The month index (0-11)
     * @param nDay The day of the month (1-31)
     * @return The date string code
     */
    public static String getDate( int nYear, int nMonth, int nDay )
    {
        String strDate;
        strDate = "" + nYear;

        int nMonthIndex = nMonth + 1;
        strDate += ( ( nMonthIndex < 10 ) ? ( "0" + nMonthIndex ) : ( "" + nMonthIndex ) );
        strDate += ( ( nDay < 10 ) ? ( "0" + nDay ) : ( "" + nDay ) );

        return strDate;
    }

    /**
     * Constructs a 8 digits date string YYYYMMDD
     * @param calendar A calendar positionned on the date
     * @return The date code
     */
    public static String getDate( Calendar calendar )
    {
        return getDate( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ),
            calendar.get( Calendar.DAY_OF_MONTH ) );
    }

    /**
     * Returns a the date code of today
     * @return The date code
     */
    public static String getDateToday(  )
    {
        Calendar calendar = new GregorianCalendar(  );

        return getDate( calendar );
    }

    /**
     * Returns the year from a date code
     * @param strDate The date code
     * @return The Year
     */
    public static int getYear( String strDate )
    {
        return Integer.parseInt( strDate.substring( 0, 4 ) );
    }

    /**
     * Returns the month from a date code
     * @param strDate The date code
     * @return The month index (0 - 11)
     */
    public static int getMonth( String strDate )
    {
        return Integer.parseInt( strDate.substring( 4, 6 ) ) - 1;
    }

    /**
     * Returns the day of month from a date code
     * @param strDate The date code
     * @return The day
     */
    public static int getDay( String strDate )
    {
        return Integer.parseInt( strDate.substring( 6, 8 ) );
    }

    /**
     * Returns the month as a formatted string corresponding to the date code
     * @return The month label
     * @param locale The locale used for display settings
     * @param strDate The date code
     */
    public static String getMonthLabel( String strDate, Locale locale )
    {
        Calendar calendar = new GregorianCalendar(  );
        calendar.set( Utils.getYear( strDate ), Utils.getMonth( strDate ), 1 );

        String strFormat = AppPropertiesService.getProperty( Constants.PROPERTY_LABEL_FORMAT_MONTH );
        DateFormat formatDate = new SimpleDateFormat( strFormat, locale );
        String strLabel = formatDate.format( calendar.getTime(  ) );

        return strLabel;
    }

    /**
     * Returns the Week as a formatted string corresponding to the date code
     * @return The week label
     * @param locale The locale used for display settings
     * @param strDate The date code
     */
    public static String getWeekLabel( String strDate, Locale locale )
    {
        Calendar calendar = new GregorianCalendar(  );
        Calendar calendarFirstDay = new GregorianCalendar(  );
        Calendar calendarLastDay = new GregorianCalendar(  );
        calendar.set( Utils.getYear( strDate ), Utils.getMonth( strDate ), Utils.getDay( strDate ) );

        int nDayOfWeek = calendar.get( Calendar.DAY_OF_WEEK );

        if ( nDayOfWeek == 1 )
        {
            nDayOfWeek = 8;
        }

        calendarFirstDay = calendar;
        calendarFirstDay.add( Calendar.DATE, Calendar.MONDAY - nDayOfWeek );
        calendarLastDay = (GregorianCalendar) calendarFirstDay.clone(  );
        calendarLastDay.add( Calendar.DATE, 6 );

        String strFormat = AppPropertiesService.getProperty( Constants.PROPERTY_LABEL_FORMAT_DATE_OF_DAY );
        DateFormat formatDate = new SimpleDateFormat( strFormat, locale );
        String strLabelFirstDay = formatDate.format( calendarFirstDay.getTime(  ) );
        String strLabelLastDay = formatDate.format( calendarLastDay.getTime(  ) );
        calendarFirstDay.clear(  );
        calendarLastDay.clear(  );

        return strLabelFirstDay + "-" + strLabelLastDay;
    }

    /**
     * Returns the first monday of a week as a formatted string corresponding to the date code
     * @param strDate The date code
     * @return The first day label
     */
    public static Calendar getFirstDayOfWeek( String strDate )
    {
        Calendar calendar = new GregorianCalendar(  );
        Calendar calendarFirstDay = new GregorianCalendar(  );
        calendar.set( Utils.getYear( strDate ), Utils.getMonth( strDate ), Utils.getDay( strDate ) );

        int nDayOfWeek = calendar.get( Calendar.DAY_OF_WEEK );

        if ( nDayOfWeek == 1 )
        {
            nDayOfWeek = 8;
        }

        calendarFirstDay = calendar;
        calendarFirstDay.add( Calendar.DATE, Calendar.MONDAY - nDayOfWeek );

        return calendarFirstDay;
    }

    /**
     * Returns the day as an international formatted string corresponding to the date code
     * @return The day as a string
     * @param locale The locale used for display settings
     * @param strDate The date code
     */
    public static String getDayLabel( String strDate, Locale locale )
    {
        Calendar calendar = new GregorianCalendar( locale );
        calendar.set( Utils.getYear( strDate ), Utils.getMonth( strDate ), Utils.getDay( strDate ) );

        String strFormat = AppPropertiesService.getProperty( Constants.PROPERTY_LABEL_FORMAT_DAY );
        DateFormat formatDate = new SimpleDateFormat( strFormat, locale );

        return formatDate.format( calendar.getTime(  ) );
    }

    /**
     * Returns the day as an international formatted string corresponding to the date code
     * @return The day as a string
     * @param locale The locale used for display settings
     * @param strDate The date code
     */
    public static String getWeekDayLabel( String strDate, Locale locale )
    {
        Calendar calendar = new GregorianCalendar( locale );
        calendar.set( Utils.getYear( strDate ), Utils.getMonth( strDate ), Utils.getDay( strDate ) );

        String strFormat = AppPropertiesService.getProperty( Constants.PROPERTY_LABEL_FORMAT_WEEK_DAY );
        DateFormat formatDate = new SimpleDateFormat( strFormat, locale );

        return formatDate.format( calendar.getTime(  ) );
    }

    /**
     * Returns a date code corresponding to a calendar roll of one month
     * @param strDate The date code
     * @return A date code one month later
     */
    public static String getNextMonth( String strDate )
    {
        int nYear = Utils.getYear( strDate );
        int nMonth = Utils.getMonth( strDate );

        if ( nMonth < 11 )
        {
            nMonth++;
        }
        else
        {
            nMonth = 0;
            nYear++;
        }

        return getDate( nYear, nMonth, 1 );
    }

    /**
     * Returns a date code corresponding to a calendar roll of one month backward
     * @param strDate The date code
     * @return A new date code one month earlier
     */
    public static String getPreviousMonth( String strDate )
    {
        int nYear = Utils.getYear( strDate );
        int nMonth = Utils.getMonth( strDate );

        if ( nMonth > 0 )
        {
            nMonth--;
        }
        else
        {
            nMonth = 11;
            nYear--;
        }

        return getDate( nYear, nMonth, 1 );
    }

    /**
     * Returns a date code corresponding to a calendar roll of one week backward
     * @param strDate The date code
     * @return A new date code one month earlier
     */
    public static String getPreviousWeek( String strDate )
    {
        Calendar calendar = new GregorianCalendar(  );
        calendar.set( Utils.getYear( strDate ), Utils.getMonth( strDate ), Utils.getDay( strDate ) );
        calendar.add( Calendar.DATE, -7 );

        return getDate( calendar );
    }

    /**
     * Returns a date code corresponding to a calendar roll of one week forward
     * @param strDate The date code
     * @return A new date code one month earlier
     */
    public static String getNextWeek( String strDate )
    {
        Calendar calendar = new GregorianCalendar(  );
        calendar.set( Utils.getYear( strDate ), Utils.getMonth( strDate ), Utils.getDay( strDate ) );
        calendar.add( Calendar.DATE, 7 );

        return getDate( calendar );
    }

    /**
     * Returns a date code corresponding to a calendar roll of one day forward
     * @param strDate The date code
     * @return A new date code one month earlier
     */
    public static String getNextDay( String strDate )
    {
        Calendar calendar = new GregorianCalendar(  );
        calendar.set( Utils.getYear( strDate ), Utils.getMonth( strDate ), Utils.getDay( strDate ) );
        calendar.add( Calendar.DATE, 1 );

        return getDate( calendar );
    }

    /**
     * Returns a date code corresponding to a calendar roll of one day backward
     * @param strDate The date code
     * @return A new date code one month earlier
     */
    public static String getPreviousDay( String strDate )
    {
        Calendar calendar = new GregorianCalendar(  );
        calendar.set( Utils.getYear( strDate ), Utils.getMonth( strDate ), Utils.getDay( strDate ) );
        calendar.add( Calendar.DATE, -1 );

        return getDate( calendar );
    }

    /**
     * Checks a date code
     * @param strDate The date code
     * @return True if valid otherwise false
     */
    public static boolean isValid( String strDate )
    {
        if ( strDate == null )
        {
            return false;
        }

        if ( strDate.length(  ) != 8 )
        {
            return false;
        }

        int nYear;
        int nMonth;
        int nDay;

        try
        {
            nYear = getYear( strDate );
            nMonth = getMonth( strDate );
            nDay = getDay( strDate );
        }
        catch ( NumberFormatException e )
        {
            return false;
        }

        if ( ( nYear < 1900 ) || ( nYear > 2100 ) )
        {
            return false;
        }

        if ( ( nMonth < 0 ) || ( nMonth > 11 ) )
        {
            return false;
        }

        if ( ( nDay < 1 ) || ( nDay > 31 ) )
        {
            return false;
        }

        return true;
    }

    /**
     * Checks if the day if Off (ie: Sunday) or not
     * @param calendar A calendar object positionned on the day to check
     * @return True if the day if Off, otherwise false
     */
    public static boolean isDayOff( Calendar calendar )
    {
        int nDayOfWeek = calendar.get( Calendar.DAY_OF_WEEK );

        if ( ( nDayOfWeek == Calendar.SATURDAY ) || ( nDayOfWeek == Calendar.SUNDAY ) )
        {
            return true;
        }

        // Add other checks here
        return false;
    }

    /**
     * Return a boolean: if the time is well formed  return true, else return false
     * @return a boolean
     * @param strTime The time
     */
    public static boolean checkTime( String strTime )
    {
        boolean bCheck = false;

        if ( strTime.equals( "" ) )
        {
            bCheck = true;
        }
        else if ( strTime.length(  ) == 5 )
        {
            try
            {
                int nHour = Integer.parseInt( strTime.substring( 0, 2 ) );
                int nMinute = Integer.parseInt( strTime.substring( 3, 5 ) );

                if ( ( strTime.charAt( 2 ) == ':' ) && ( nHour < 25 ) && ( nMinute < 60 ) )
                {
                    bCheck = true;
                }
            }
            catch ( NumberFormatException e )
            {
                bCheck = false;
            }
        }

        return bCheck;
    }

    /**
     * Returns a date code corresponding to a calendar roll of n days forward
     * @return A new date code one month earlier
     * @param n number of days to roll
     * @param strDate The date code
     */
    public static String getDayAfter( String strDate, int n )
    {
        Calendar calendar = new GregorianCalendar(  );
        calendar.set( Utils.getYear( strDate ), Utils.getMonth( strDate ), Utils.getDay( strDate ) );
        calendar.add( Calendar.DATE, n );

        return getDate( calendar );
    }
}
