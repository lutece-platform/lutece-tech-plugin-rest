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
package fr.paris.lutece.plugins.reporting.utils;

import fr.paris.lutece.portal.service.html.EncodingService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;


/**
 *
 * @author jSteve
 *
 */
public final class ReportingUtils
{
    private static final String STRING_NEW_LINE = "reporting.newLine";
    private static final String STRING_TAG_BR = "reporting.tagBR";

    /**
     *
     *
     */
    private ReportingUtils(  )
    {
    }

    /**
     * Replace the return line "\n" of Text area with a HTML tag "<br>"
     * @param strTargetString the string to modify
     * @return the chaine with one space beetween words
     */
    public static String convertNewLineToHtmlTag( String strTargetString )
    {
        return strTargetString.replace( AppPropertiesService.getProperty( STRING_NEW_LINE ),
            AppPropertiesService.getProperty( STRING_TAG_BR ) );
    }

    /**
     * Replace the HTML tag "<br>" with a return line "\n"
     * @param strTargetString the string to modify
     * @return the string with one space beetween words
     */
    public static String removeHtmlTagBr( String strTargetString )
    {
        return strTargetString.replace( AppPropertiesService.getProperty( STRING_TAG_BR ), "" );
    }

    /**
     * Encode astring for passage in parameter in URL
     * @param strEntry the string entry
     * @return the string encoding
     */
    public static String encodeForURL( String strEntry )
    {
        return EncodingService.encodeUrl( strEntry );
    }
}
