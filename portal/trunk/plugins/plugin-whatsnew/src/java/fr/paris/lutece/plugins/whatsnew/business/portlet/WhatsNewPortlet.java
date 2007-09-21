/*
 * Copyright (c) 2002-2006, Mairie de Paris
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
package fr.paris.lutece.plugins.whatsnew.business.portlet;

import fr.paris.lutece.plugins.whatsnew.business.WhatsNew;
import fr.paris.lutece.plugins.whatsnew.business.WhatsNewHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.util.xml.XmlUtil;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents business object WhatsNewPortlet
 */
public class WhatsNewPortlet extends Portlet
{
    public static final int ELEMENT_ORDER_DATE_DESC = 0;
    public static final int ELEMENT_ORDER_DATE_ASC = 1;
    public static final int ELEMENT_ORDER_ALPHA = 2;
    private static final String TAG_WHATS_NEW_PORTLET = "whatsnew-list-portlet";
    private static final String TAG_WHATS_NEW_MIN_DISPLAY = "whatsnew-min-display";
    private static final String TAG_WHATS_NEW_NUMBER_DISPLAY = "whatsnew-number-display";
    private static final String PARAMETER_MIN_DISPLAY = "min_display";

    /* comparator for sorting - date ascendant order */
    private static final Comparator COMPARATOR_TRI_ASC = new Comparator(  )
        {
            public int compare( Object obj1, Object obj2 )
            {
                WhatsNew whatsnew1 = (WhatsNew) obj1;
                WhatsNew whatsnew2 = (WhatsNew) obj2;

                int nDateOrder = whatsnew1.getDateUpdate(  ).compareTo( whatsnew2.getDateUpdate(  ) );

                return nDateOrder;
            }
        };

    /* comparator for sorting - date descendant order */
    private static final Comparator COMPARATOR_TRI_DESC = new Comparator(  )
        {
            public int compare( Object obj1, Object obj2 )
            {
                WhatsNew whatsnew1 = (WhatsNew) obj1;
                WhatsNew whatsnew2 = (WhatsNew) obj2;

                int nDateOrder = whatsnew1.getDateUpdate(  ).compareTo( whatsnew2.getDateUpdate(  ) );

                if ( nDateOrder != 0 )
                {
                    nDateOrder = -nDateOrder;
                }

                return nDateOrder;
            }
        };

    /* comparator for sorting - alphanumeric order */
    private static final Comparator COMPARATOR_TRI_ALPHA = new Comparator(  )
        {
            public int compare( Object obj1, Object obj2 )
            {
                WhatsNew whatsnew1 = (WhatsNew) obj1;
                WhatsNew whatsnew2 = (WhatsNew) obj2;

                int nDateOrder = whatsnew1.getTitle(  ).toUpperCase(  ).compareTo( whatsnew2.getTitle(  ).toUpperCase(  ) );

                return nDateOrder;
            }
        };

    private boolean _bShowDocuments;
    private boolean _bShowPortlets;
    private boolean _bShowPages;
    private int _nPeriod;
    private int _nNbElementsMax;
    private int _nElementsOrder;

    /**
     * Sets the identifier of the portlet type to the value specified in the WhatsNewPortletHome class
     */
    public WhatsNewPortlet(  )
    {
        setPortletTypeId( WhatsNewPortletHome.getInstance(  ).getPortletTypeId(  ) );
    }

    /**
     * @return Returns the boolean that indicates if the user wants to see the documents.
     */
    public boolean getShowDocuments(  )
    {
        return _bShowDocuments;
    }

    /**
     * @param bArticles The new boolean that indicates if the user wants to see the articles..
     */
    public void setShowDocuments( boolean bShowDocuments )
    {
        _bShowDocuments = bShowDocuments;
    }

    /**
     * @return Returns the boolean that indicates if the user wants to see the portlets.
     */
    public boolean getShowPortlets(  )
    {
        return _bShowPortlets;
    }

    /**
     * @param bPortlets The new boolean that indicates if the user wants to see the portlets.
     */
    public void setShowPortlets( boolean bPortlets )
    {
        _bShowPortlets = bPortlets;
    }

    /**
     * @return Returns the boolean that indicates if the user wants to see the pages.
     */
    public boolean getShowPages(  )
    {
        return _bShowPages;
    }

    /**
     * @param bPages The new boolean that indicates if the user wants to see the pages.
     */
    public void setShowPages( boolean bPages )
    {
        _bShowPages = bPages;
    }

    /**
     * @return Returns the period (number of days).
     */
    public int getPeriod(  )
    {
        return _nPeriod;
    }

    /**
     * @param nPeriod The period to set (number of days).
     */
    public void setPeriod( int nPeriod )
    {
        this._nPeriod = nPeriod;
    }

    /**
     * @return Returns the nbElementsMax.
     */
    public int getNbElementsMax(  )
    {
        return _nNbElementsMax;
    }

    /**
     * @param nElementsMax The maximum number of elements to see in the portlet.
     */
    public void setNbElementsMax( int nElementsMax )
    {
        _nNbElementsMax = nElementsMax;
    }

    /**
     * @return Returns the maximum number of elements to see in the portlet.
     */
    public int getElementsOrder(  )
    {
        return _nElementsOrder;
    }

    /**
     * @param nOrder The maximum number of elements to see in the portlet.
     */
    public void setElementsOrder( int nOrder )
    {
        _nElementsOrder = nOrder;
    }

    /**
     * Returns the XML content of the portlet with the XML header
     * @param request The HTTP Servlet request
     * @return The XML content of this portlet
     */
    public String getXmlDocument( HttpServletRequest request )
    {
        return XmlUtil.getXmlHeader(  ) + getXml( request );
    }

    /**
     * Returns the XML content of the portlet without the XML header
     * @param request The HTTP Servlet request
     * @return The Xml content of this portlet
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer strXml = new StringBuffer(  );
        XmlUtil.beginElement( strXml, WhatsNewPortlet.TAG_WHATS_NEW_PORTLET );

        ArrayList listElements = new ArrayList(  );

        Timestamp limitTimestamp = getTimestampFromPeriodAndCurrentDate( getPeriod(  ) );

        if ( getShowDocuments(  ) )
        {
            Collection listDocuments = WhatsNewHome.selectDocumentsByCriterias( limitTimestamp );
            listElements.addAll( listDocuments );
        }

        if ( getShowPortlets(  ) )
        {
            Collection listPortlets = WhatsNewHome.selectPortletsByCriterias( limitTimestamp );
            listElements.addAll( listPortlets );
        }

        if ( getShowPages(  ) )
        {
            Collection listPages = WhatsNewHome.selectPagesByCriterias( limitTimestamp );
            listElements.addAll( listPages );
        }

        int comparator = getElementsOrder(  );

        if ( comparator == ELEMENT_ORDER_DATE_DESC )
        {
            Collections.sort( listElements, COMPARATOR_TRI_DESC );
        }
        else if ( comparator == ELEMENT_ORDER_DATE_ASC )
        {
            Collections.sort( listElements, COMPARATOR_TRI_ASC );
        }
        else if ( comparator == ELEMENT_ORDER_ALPHA )
        {
            Collections.sort( listElements, COMPARATOR_TRI_ALPHA );
        }
        else
        {
            // sort by descendant date order by default
            Collections.sort( listElements, COMPARATOR_TRI_DESC );
        }

        Iterator i = listElements.iterator(  );

        // retrieve from request the current display id parameter : to paginate the results
        // the request parameter is postfixed by the portlet id to be able to handle more than
        // one portlet in a page
        String strMinDisplay = request.getParameter( PARAMETER_MIN_DISPLAY + "_" + getId(  ) );

        if ( strMinDisplay != null )
        {
            XmlUtil.addElement( strXml, TAG_WHATS_NEW_MIN_DISPLAY, strMinDisplay );
        }
        else
        {
            XmlUtil.addElement( strXml, TAG_WHATS_NEW_MIN_DISPLAY, 1 );
        }

        // retrieve the number of elements to display in a portlet
        // this is filtered in the xsl in order to allow easy pagination
        XmlUtil.addElement( strXml, TAG_WHATS_NEW_NUMBER_DISPLAY, this.getNbElementsMax(  ) );

        // get the xml list of elements
        while ( i.hasNext(  ) )
        {
            WhatsNew whatsnew = (WhatsNew) i.next(  );
            strXml.append( whatsnew.getXml( request ) );
        }

        XmlUtil.endElement( strXml, WhatsNewPortlet.TAG_WHATS_NEW_PORTLET );

        return addPortletTags( strXml );
    }

    /**
     * Update of this portlet
     */
    public void update(  )
    {
        WhatsNewPortletHome.getInstance(  ).update( this );
    }

    /**
     * Removes the current instance of the WhatsNewPortlet object
     */
    public void remove(  )
    {
        WhatsNewPortletHome.getInstance(  ).remove( this );
    }

    /**
     * Get the timestamp correspnding to a given day in the past.
     * It is calculated from a period to remove from the current date.
     * @param nDays the number of days between the current day and the timestamp to calculate.
     * @return the timestamp corresponding to the limit in the past for the period given in days, with hours, minutesn sec. and millisec. set to 0.
     */
    private Timestamp getTimestampFromPeriodAndCurrentDate( int nDays )
    {
        Calendar currentCalendar = new GregorianCalendar( Locale.FRANCE );
        currentCalendar.set( Calendar.HOUR_OF_DAY, 0 );
        currentCalendar.set( Calendar.MINUTE, 0 );
        currentCalendar.set( Calendar.SECOND, 0 );
        currentCalendar.set( Calendar.MILLISECOND, 0 );

        currentCalendar.add( Calendar.DATE, -nDays );

        Timestamp timestampCurrent = new Timestamp( currentCalendar.getTimeInMillis(  ) );

        return timestampCurrent;
    }
}
