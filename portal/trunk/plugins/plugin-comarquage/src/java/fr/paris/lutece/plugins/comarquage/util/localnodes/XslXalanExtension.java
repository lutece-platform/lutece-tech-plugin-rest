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
package fr.paris.lutece.plugins.comarquage.util.localnodes;



import fr.paris.lutece.plugins.comarquage.business.Card;
import fr.paris.lutece.plugins.comarquage.service.CoMarquageLocalListing;
import fr.paris.lutece.portal.service.util.AppLogService;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.sun.org.apache.xalan.internal.extensions.ExpressionContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.transform.TransformerException;


/**
 * This class is called by the xsl stylesheet "ajout_local.xsl".
 * It is responsible for adding to the public xml stream the local themes/cards that
 * where referenced by the deamon.
 */
public class XslXalanExtension
{
    //xml opening bracket
    private static final String XML_TAG_OPEN_DEBUT = "<";
    private static final String XML_TAG_OPEN_FIN = "> \n";
    private static final String XML_TAG_CLOSE_DEBUT = "</";
    private static final String XML_TAG_CLOSE_FIN = "> \n";
    private static final String XML_TAG_DEBUT = "<";
    private static final String XML_TAG_FIN = "/> \n";

    // xml opening tags
    private static final String XML_TAG_ITEM_OPEN = "<item>\n";
    private static final String XML_TAG_LIEN_OPEN_DEBUT = "	<lien type=\"";
    private static final String XML_TAG_LIEN_OPEN_FIN = "\">";
    private static final String XML_TAG_TITRE_OPEN = "<titre>";

    // xml closing tags
    private static final String XML_TAG_ITEM_CLOSE = "</item>\n";
    private static final String XML_TAG_LIEN_CLOSE = "</lien>\n";
    private static final String XML_TAG_TITRE_CLOSE = "</titre>\n";

    // parameters
    private static final String PARAMETER_ID = "id";
    private static final String PARAMETER_PATH = "path";
    private static final String PARAMETER_TITLE = "titre";
    private static final String PARAMETER_SLASH = "/";
    private static final String PARAMETER_QUOTATION_MARK = "\"";


    
    /**
     * Method called from the xsl stylesheet and that performs the addintion of the local nodes
     * to the xml stream.
     * @param context the expression context
     * @param strThemeId the id of the current theme (where local nodes might have to be added
     * @param strPluginName the plugin name
     * @return the xml code corresponding to the local nodes to add to this theme
     */
    
    public String nodesAttached( ExpressionContext context, String strThemeId, String strPluginName )
    {					
        // retrieve the hashmap referencing the local cards
        Map<String, Map> mapId = CoMarquageLocalListing.getMapId( strPluginName );

        if ( mapId == null )
        {
            AppLogService.debug( "nodesAttached (Xalan extension): No MapID found for plugin '" + strPluginName + "'." );

            return "";
        }

        Map<String, Collection<Card>> mapParentId = mapId.get( CoMarquageLocalListing.PARAMETER_MAP_PARENT_ID );

        if ( mapParentId == null )
        {
            AppLogService.debug( "nodesAttached (Xalan extension): No MapParentId found for plugin '" + strPluginName +
                "'. " + CoMarquageLocalListing.PARAMETER_MAP_PARENT_ID );

            return "";
        }

        // used to write the new xml elements
        final StringBuffer buf = new StringBuffer(  );

        // find in the hashmap the collection corresponding to the given id
        Collection<Card> list = mapParentId.get( strThemeId );

        if ( list != null )
        {
            //        	 go through the collection of "Card" objects
            for ( Card card : list )
            {
                // get the id  and title of each card of the collection
                String strChildId = card.getId(  );
                String strChildTitle = card.getTitle(  );
                String strChildType = card.getType(  );

                buf.append( XML_TAG_ITEM_OPEN ).append( XML_TAG_LIEN_OPEN_DEBUT ).append( strChildType )
                   .append( XML_TAG_LIEN_OPEN_FIN ).append( strChildId ).append( XML_TAG_LIEN_CLOSE )
                   .append( XML_TAG_TITRE_OPEN ).append( strChildTitle ).append( XML_TAG_TITRE_CLOSE )
                   .append( XML_TAG_ITEM_CLOSE );
            }
        }

        return buf.toString(  );
    }

    /**
     * Method called from the xsl stylesheet and that performs the creation of the path
     *
     * @param context the expression context
     * @param initialKey the id paramater (node or card asked)
     * @param node path node containing all paths available
     * @param strPluginName the plugin name
     * @return the xml code corresponding to the path
     * @throws TransformerException exception during path resolving
     */
    public String resolvePath( ExpressionContext context, String initialKey, Node node, String strPluginName )
        throws TransformerException
    {
        // retrieve the hashmap referencing the local cards
        Map<String, Map> mapId = CoMarquageLocalListing.getMapId( strPluginName );
        Map<String, String> mapXml = new HashMap<String, String>(  );
        ArrayList<String> arXml = new ArrayList<String>(  );
        String childNodeName = null;

        if ( mapId == null )
        {
            AppLogService.debug( "nodesAttached (Xalan extension): No MapID found for plugin '" + strPluginName + "'." );

            return "";
        }

        Map<String, String> mapLocalCard = mapId.get( CoMarquageLocalListing.PARAMETER_MAP_LOCAL_CARD );

        if ( mapLocalCard == null )
        {
            AppLogService.debug( "nodesAttached (Xalan extension): No MapLocalCard found for plugin '" + strPluginName +
                "'. " + CoMarquageLocalListing.PARAMETER_MAP_LOCAL_CARD );

            return "";
        }

        // used to write the new xml elements
        final StringBuffer buf = new StringBuffer(  );

        boolean bPathOk = false;

        String strStartInfo = XML_TAG_OPEN_DEBUT + node.getLocalName(  ) + XML_TAG_OPEN_FIN;
        String strEndInfo = XML_TAG_CLOSE_DEBUT + node.getLocalName(  ) + XML_TAG_CLOSE_FIN;

        final int pos = initialKey.indexOf( PARAMETER_SLASH, 1 ) + 1;
        final String strKey = initialKey.substring( pos );

        final String strSeparator = PARAMETER_SLASH;
        StringTokenizer tokenizer = new StringTokenizer( strKey, strSeparator );
        ArrayList<String> arKeyPath = new ArrayList<String>(  );
        ArrayList<String> arKey = new ArrayList<String>(  );
        ArrayList<ArrayList<String>> arKeyResult = new ArrayList<ArrayList<String>>(  );
        int nKey = 0;

        while ( tokenizer.hasMoreTokens(  ) )
        {
            String strKeyTemp = tokenizer.nextToken(  );
            String strKeyPathTemp = null;

            if ( nKey > 0 )
            {
                strKeyPathTemp = arKeyPath.get( nKey - 1 ) + PARAMETER_SLASH + strKeyTemp;
            }
            else
            {
                strKeyPathTemp = strKeyTemp;
            }

            arKey.add( strKeyTemp );
            arKeyPath.add( strKeyPathTemp );
            arKeyResult.add( (ArrayList<String>) arKey.clone(  ) );
            nKey++;
        }

        //warning : bug referenced about navigation into child nodes (don't modify navigation)
        Node childrenNodeTmp = node.getFirstChild(  );

        while ( childrenNodeTmp != null )
        {
            //Verify child node type
            if ( childrenNodeTmp.getNodeType(  ) == Node.ELEMENT_NODE )
            {
                NamedNodeMap nodeMap = childrenNodeTmp.getAttributes(  );
                String strId = null;

                if ( nodeMap.getNamedItem( PARAMETER_ID ) != null )
                {
                    strId = nodeMap.getNamedItem( PARAMETER_ID ).getNodeValue(  );

                    if ( strId.equals( strKey ) )
                    {
                        buf.append( strStartInfo );
                        buf.append( XML_TAG_OPEN_DEBUT + childrenNodeTmp.getLocalName(  ) );
                        buf.append( " " + PARAMETER_ID + "='" + strId + "' " );
                        buf.append( XML_TAG_OPEN_FIN );
                        bPathOk = true;
                    }
                }

                // path is equal to id asked
                if ( bPathOk )
                {
                    //build map of information from node
                    Node subChildrenNodeTmp = childrenNodeTmp.getFirstChild(  );
                    Integer nSubChildren = new Integer( 0 );

                    while ( subChildrenNodeTmp != null )
                    {
                        if ( subChildrenNodeTmp.getNodeType(  ) == Node.ELEMENT_NODE )
                        {
                            NamedNodeMap nodeSubMap = subChildrenNodeTmp.getAttributes(  );
                            String strSubTitle = null;
                            String strSubId = null;

                            if ( nodeSubMap.getNamedItem( PARAMETER_PATH ) != null )
                            {
                                strSubId = nodeSubMap.getNamedItem( PARAMETER_PATH ).getNodeValue(  );
                            }

                            if ( nodeSubMap.getNamedItem( PARAMETER_TITLE ) != null )
                            {
                                strSubTitle = nodeSubMap.getNamedItem( PARAMETER_TITLE ).getNodeValue(  );
                            }

                            mapXml.put( strSubId, strSubTitle );
                            arXml.add( strSubId );

                            childNodeName = subChildrenNodeTmp.getLocalName(  );

                            nSubChildren = new Integer( nSubChildren.intValue(  ) + 1 );
                        }

                        //go to next child node
                        subChildrenNodeTmp = subChildrenNodeTmp.getNextSibling(  );
                    }

                    //build path
                    int nbKey = 0;

                    while ( nbKey < ( arKey.size(  ) ) )
                    {
                        String strKeyTemp = arKey.get( nbKey );
                        String strKeyPathTemp = arKeyPath.get( nbKey );
                        ArrayList<String> arKeyTemp = arKeyResult.get( nbKey );
                        String strKeyPathCompTemp = createPath( arKeyTemp );
                        boolean bFound = false;

                        int nbXml = 0;

                        while ( nbXml < mapXml.size(  ) )
                        {
                            String xmlTemp = arXml.get( nbXml );

                            if ( xmlTemp.equals( strKeyPathCompTemp ) )
                            {
                                String titleTemp = mapXml.get( xmlTemp );
                                addTitle( buf, strKeyPathTemp, strKeyTemp, titleTemp, childNodeName );
                                bFound = true;

                                break;
                            }

                            nbXml++;
                        }

                        if ( !bFound )
                        {
                            completePathInfo( buf, strKeyPathTemp, strKeyTemp, mapLocalCard, childNodeName, arKeyResult );
                            bFound = true;
                        }

                        nbKey++;
                    }

                    buf.append( XML_TAG_CLOSE_DEBUT + childrenNodeTmp.getLocalName(  ) ).append( XML_TAG_CLOSE_FIN );

                    break;
                }
            }

            bPathOk = false;
            childrenNodeTmp = childrenNodeTmp.getNextSibling(  );
        }

        if ( bPathOk )
        {
            buf.append( strEndInfo );

            return buf.toString(  );
        }
        else
        {
            return null;
        }
    }

    /**
     * Create path using arraylist
     *
     * @param arKeyTemp arrayList of keys
     * @return list of keys separated by slash
     */
    public String createPath( ArrayList<String> arKeyTemp )
    {
        int i = 0;
        StringBuffer strPathBuffer = new StringBuffer(  );

        for ( String strTmp : arKeyTemp )
        {
            if ( i != 0 )
            {
                strPathBuffer.append( PARAMETER_SLASH );
            }

            strPathBuffer.append( strTmp );
            i++;
        }

        return strPathBuffer.toString(  );
    }

    /**
     * Method called to add one line of path
     * @param buf result stringBuffer
     * @param strKeyPathTemp value of attribute id of the node created
     * @param strSubTitle value of attribute title of the node created
     * @param strChildNodeName local name of the node created
     */
    public void addTitle( StringBuffer buf, String strKeyPathTemp, String strKeyTemp, String strSubTitle,
        String strChildNodeName )
    {
        String outTmp = XML_TAG_DEBUT + strChildNodeName + " " + PARAMETER_PATH + "=" + PARAMETER_QUOTATION_MARK +
            strKeyPathTemp + PARAMETER_QUOTATION_MARK + " " + PARAMETER_ID + "=" + PARAMETER_QUOTATION_MARK +
            strKeyTemp + PARAMETER_QUOTATION_MARK + " " + PARAMETER_TITLE + "=" + PARAMETER_QUOTATION_MARK +
            strSubTitle + PARAMETER_QUOTATION_MARK + XML_TAG_FIN;

        buf.append( outTmp );
    }

    /**
     * Method called to complete missing line path
     * @param buf result StringBuffer
     * @param strKeyPathTemp value of attribute id of the node created
     * @param strKeyTemp key
     * @param mapLocalCard map of local cards
     * @param strChildNodeName local name of the node created
     * @param arKeyResult arrayList of keys
     */
    public void completePathInfo( StringBuffer buf, String strKeyPathTemp, String strKeyTemp,
        Map<String, String> mapLocalCard, String strChildNodeName, ArrayList<ArrayList<String>> arKeyResult )
    {
        // find in the hashmap the collection corresponding to the given id
        String strTitle = mapLocalCard.get( strKeyTemp );

        if ( strTitle != null )
        {
            suppKey( arKeyResult, strKeyTemp );
            addTitle( buf, strKeyPathTemp, strKeyTemp, strTitle, strChildNodeName );
        }
        else
        {
            AppLogService.debug( "Pas de chemin valide dans la fiche pour l'url demandée" );
        }
    }

    /**
     * Delete key in an arrayList
     *
     * @param arKeyResult arrayList of keys
     * @param strKeyTemp key
     */
    public void suppKey( ArrayList<ArrayList<String>> arKeyResult, String strKeyTemp )
    {

        for ( ArrayList arListTmp : arKeyResult )
        {
            arListTmp.remove( strKeyTemp );
        }
    }
}
