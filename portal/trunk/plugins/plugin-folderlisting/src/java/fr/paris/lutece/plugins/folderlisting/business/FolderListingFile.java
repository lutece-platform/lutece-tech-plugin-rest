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
public class FolderListingFile
{
    ///////////////////////////////////////////////////////////////////////
    // Constants

    // Xml Tags
    private static final String TAG_FILE = "file";
    private static final String TAG_FILE_NAME = "file-name";
    private static final String TAG_FILE_EXTENSION = "file-extension";
    private static final String TAG_FILE_SIZE = "file-size";
    private static final String TAG_FILE_DATE = "file-date";
    private static final String TAG_FILE_PATH = "file-path";

    // Variables declarations
    private String _strName;
    private String _strExtension;
    private String _strDate;
    private String _strSize;
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
     * Returns the Extension
     *
     * @return The Extension
     */
    public String getExtension(  )
    {
        return _strExtension;
    }

    /**
     * Sets the Extension
     *
     * @param strExtension The Extension
     */
    public void setExtension( String strExtension )
    {
        _strExtension = strExtension;
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
     * Returns the Size
     *
     * @return The Size
     */
    public String getSize(  )
    {
        return _strSize;
    }

    /**
     * Sets the Size
     *
     * @param strSize The Size
     */
    public void setSize( String strSize )
    {
        _strSize = strSize;
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
        XmlUtil.beginElement( strXml, TAG_FILE );

        XmlUtil.addElement( strXml, TAG_FILE_NAME, getName(  ) );
        XmlUtil.addElement( strXml, TAG_FILE_EXTENSION, getExtension(  ) );
        XmlUtil.addElement( strXml, TAG_FILE_SIZE, getSize(  ) );
        XmlUtil.addElement( strXml, TAG_FILE_DATE, getDate(  ) );
        XmlUtil.addElement( strXml, TAG_FILE_PATH, getPath(  ) );

        XmlUtil.endElement( strXml, TAG_FILE );

        return strXml.toString(  );
    }
}
