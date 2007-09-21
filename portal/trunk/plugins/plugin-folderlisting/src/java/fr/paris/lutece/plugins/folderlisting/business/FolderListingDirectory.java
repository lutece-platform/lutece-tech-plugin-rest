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
package fr.paris.lutece.plugins.folderlisting.business;

import fr.paris.lutece.util.xml.XmlUtil;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents the business object FolderListingFile
 */
public class FolderListingDirectory
{
    ///////////////////////////////////////////////////////////////////////
    // Constants

    // Xml Tags
    private static final String TAG_DIRECTORY = "directory";
    private static final String TAG_DIRECTORY_NAME = "directory-name";
    private static final String TAG_DIRECTORY_DATE = "directory-date";
    private static final String TAG_DIRECTORY_PATH = "directory-path";

    // Variables declarations
    private String _strName;
    private String _strDate;
    private String _strPath;

    /**
     * Returns the Name
     *
     * @return The Name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the Name
     *
     * @param strName The Name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the Date
     *
     * @return The Date
     */
    public String getDate(  )
    {
        return _strDate;
    }

    /**
     * Sets the Date
     *
     * @param strDate The Date
     */
    public void setDate( String strDate )
    {
        _strDate = strDate;
    }

    /**
     * Returns the Path
     *
     * @return The Path
     */
    public String getPath(  )
    {
        return _strPath;
    }

    /**
     * Sets the Path
     *
     * @param strPath The Path
     */
    public void setPath( String strPath )
    {
        _strPath = strPath;
    }

    /**
     * Builds the XML content of this FolderListingFile and returns it
     * @param request The HttpServletRequest
     * @return the Xml content of this FolderListingFile
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer strXml = new StringBuffer(  );
        XmlUtil.beginElement( strXml, TAG_DIRECTORY );

        XmlUtil.addElement( strXml, TAG_DIRECTORY_NAME, getName(  ) );
        XmlUtil.addElement( strXml, TAG_DIRECTORY_DATE, getDate(  ) );
        XmlUtil.addElement( strXml, TAG_DIRECTORY_PATH, getPath(  ) );

        XmlUtil.endElement( strXml, TAG_DIRECTORY );

        return strXml.toString(  );
    }
}
