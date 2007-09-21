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
package fr.paris.lutece.plugins.projeau.business;

import fr.paris.lutece.plugins.projeau.business.Projeau;
import fr.paris.lutece.portal.service.fileupload.FileUploadService;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.xml.XmlUtil;

import org.apache.commons.fileupload.FileItem;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * This class provide the business object to store during the form processing.
 * It is made of a saisine and of a list of uploaded documents.
 */
public class ProjeauFormDocument
{
    private static final String TAG_PROJEAU = "projeau";
    private static final String TAG_ID_INTERNET = "id-internet";
    private static final String TAG_TITLE = "title";
    private static final String TAG_LAST_NAME = "last-name";
    private static final String TAG_FIRST_NAME = "first-name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_COMPLEMENTARY_DOCUMENTS = "complementary-documents";
    private static final String TAG_COMPLEMENTARY_DOCUMENT = "complementary-document";

    // the projeau
    private Projeau _projeau;

    // the list of uploaded documents
    private ArrayList<FileItem> _uploadedDocumentsList;

    /**
     * Constructor
     */
    public ProjeauFormDocument(  )
    {
    }

    /**
     * Getter for the projeau object
     * @return the projeau
     */
    public Projeau getProjeau(  )
    {
        return _projeau;
    }

    /**
     * Setter for the projeau object
     * @param projeau the projeau to set
     */
    public void setProjeau( Projeau projeau )
    {
        _projeau = projeau;
    }

    /**
     * Getter for the document list
     * @return the document list
     */
    public ArrayList<FileItem> getUploadedDocumentsList(  )
    {
        return _uploadedDocumentsList;
    }

    /**
     * Settter for the document list
     * @param documentsList the document list to set
     */
    public void setUploadedDocumentsList( ArrayList<FileItem> documentsList )
    {
        _uploadedDocumentsList = documentsList;
    }

    /**
     * Get the xml code associated with the current object.
     * This can be used to display easily the content of a form document
     * by associating it to an xsl stylesheet.
     * @return the xml code associated with the object instance.
     */
    public String getXml(  )
    {
        StringBuffer strXml = new StringBuffer(  );

        XmlUtil.beginElement( strXml, TAG_PROJEAU );

        Projeau projeau = getProjeau(  );

        XmlUtil.addElement( strXml, TAG_ID_INTERNET, projeau.getIdInternet(  ) );
        XmlUtil.addElement( strXml, TAG_TITLE, projeau.getTitle(  ) );
        XmlUtil.addElement( strXml, TAG_LAST_NAME, projeau.getLastName(  ) );
        XmlUtil.addElement( strXml, TAG_FIRST_NAME, projeau.getFirstName(  ) );

        XmlUtil.addElement( strXml, TAG_EMAIL, projeau.getEmail(  ) );
        
        ArrayList<FileItem> fileList = getUploadedDocumentsList(  );

        if ( ( fileList != null ) && ( !fileList.isEmpty(  ) ) )
        {
            XmlUtil.beginElement( strXml, TAG_COMPLEMENTARY_DOCUMENTS );

            for ( FileItem uploadedFile : fileList )
            {
                XmlUtil.addElement( strXml, TAG_COMPLEMENTARY_DOCUMENT,
                    FileUploadService.getFileNameOnly( uploadedFile ) );
            }

            XmlUtil.endElement( strXml, TAG_COMPLEMENTARY_DOCUMENTS );
        }

        XmlUtil.endElement( strXml, TAG_PROJEAU );

        return strXml.toString(  );
    }

    /**
    * Get the xml code associated with the current object, to which is added an xml header
    * @return the xml code (with header) associated with the object instance.
    */
    public String getXmlDocument(  )
    {
        return XmlUtil.getXmlHeader(  ) + getXml(  );
    }
}
