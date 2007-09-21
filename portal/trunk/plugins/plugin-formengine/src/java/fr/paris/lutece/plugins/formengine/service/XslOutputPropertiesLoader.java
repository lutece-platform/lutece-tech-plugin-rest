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
package fr.paris.lutece.plugins.formengine.service;

import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.Properties;

import javax.xml.transform.OutputKeys;


/**
 * Loader for xsl output properties.
 */
public class XslOutputPropertiesLoader
{
    private static final String PROPERTIES_OUTPUT_METHOD = "formengine.xsl.output.method";
    private static final String PROPERTIES_OUTPUT_ENCODING = "formengine.xsl.output.encoding";
    private static final String PROPERTIES_OUTPUT_INDENT = "formengine.xsl.output.indent";
    private static final String PROPERTIES_OUTPUT_OMIT_XML_DECLARATION = "formengine.xsl.output.omit.xml.declaration";
    private static final String PROPERTIES_OUTPUT_VERSION = "formengine.xsl.output.version";
    private static final String PROPERTIES_OUTPUT_MEDIA_TYPE = "formengine.xsl.output.media.type";
    private static final String PROPERTIES_OUTPUT_STANDALONE = "formengine.xsl.output.standalone";

    /**
     *
     */
    private XslOutputPropertiesLoader(  )
    {
    }

    /**
     * Retrieve the xsl output properties
     * @return the set of properties for xsl transformation
     */
    public static Properties getOutputProperties(  )
    {
        Properties ouputProperties = new Properties(  );

        String strMethod = AppPropertiesService.getProperty( PROPERTIES_OUTPUT_METHOD );
        String strEncoding = AppPropertiesService.getProperty( PROPERTIES_OUTPUT_ENCODING );
        String strIndent = AppPropertiesService.getProperty( PROPERTIES_OUTPUT_INDENT );
        String strOmitXmlDeclaration = AppPropertiesService.getProperty( PROPERTIES_OUTPUT_OMIT_XML_DECLARATION );
        String strVersion = AppPropertiesService.getProperty( PROPERTIES_OUTPUT_VERSION );
        String strMediaType = AppPropertiesService.getProperty( PROPERTIES_OUTPUT_MEDIA_TYPE );
        String strStandalone = AppPropertiesService.getProperty( PROPERTIES_OUTPUT_STANDALONE );

        if ( ( strMethod != null ) && ( !strMethod.trim(  ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.METHOD, strMethod );
        }

        if ( ( strVersion != null ) && ( !strVersion.trim(  ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.VERSION, strVersion );
        }

        if ( ( strEncoding != null ) && ( !strEncoding.trim(  ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.ENCODING, strEncoding );
        }

        if ( ( strIndent != null ) && ( !strIndent.trim(  ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.INDENT, strIndent );
        }

        if ( ( strOmitXmlDeclaration != null ) && ( !strOmitXmlDeclaration.trim(  ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.OMIT_XML_DECLARATION, strOmitXmlDeclaration );
        }

        if ( ( strMediaType != null ) && ( !strMediaType.trim(  ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.MEDIA_TYPE, strMediaType );
        }

        if ( ( strStandalone != null ) && ( !strStandalone.trim(  ).equals( "" ) ) )
        {
            ouputProperties.setProperty( OutputKeys.STANDALONE, strStandalone );
        }

        return ouputProperties;
    }
}
