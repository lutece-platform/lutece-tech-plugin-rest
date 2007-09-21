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
package fr.paris.lutece.plugins.formengine.service.validator;

import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.Field;
import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.FileName;
import fr.paris.lutece.plugins.formengine.web.FormErrorsList;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;


/**
 * This class checks the file types for an upload field.
 */
public class ValidatorFileTypes extends FieldValidator
{
    private static final String PROPERTY_VALIDATION_MESSAGE = "formengine.validator.message.fileTypes";

    // The separators that can be used in the provided list of extensions
    private static final String SEPARATORS = ",; ";

    /**
     * The validate implementation.
     * Check that the field does not contain files with other extensions than
     * those specified
     * @param field The field to validate
     * @param errors A FormErrorsList to add errors into
     * @return True if validate, false otherwise
     */
    public boolean validate( Field field, FormErrorsList errors )
    {
        // The parameter contains the list of allowed file types
        String strFileTypes = this.getRuleParameter(  );

        // Parse the parameter and build a Set containing the extensions
        Set<String> fileTypes = new HashSet<String>(  );
        StringTokenizer parser = new StringTokenizer( strFileTypes, SEPARATORS );

        while ( parser.hasMoreTokens(  ) )
        {
            fileTypes.add( parser.nextToken(  ).toUpperCase(  ) );
        }

        // Check if all files have a valid extension
        boolean bValid = true;

        if ( field.getFileNames(  ) != null )
        {
            Iterator iterFileNames = field.getFileNames(  ).getFileName(  ).iterator(  );

            while ( bValid && iterFileNames.hasNext(  ) )
            {
                String strFileName = ( (FileName) iterFileNames.next(  ) ).getValue(  );
                String strFileType = strFileName.substring( strFileName.lastIndexOf( "." ) + 1 ).toUpperCase(  );
                bValid = fileTypes.contains( strFileType );
            }
        }

        if ( !bValid )
        {
            String[] messageParams = new String[3];
            messageParams[0] = field.getName(  );
            messageParams[1] = field.getLabel(  );
            messageParams[2] = strFileTypes;

            if ( this.getErrorMessage(  ) != null )
            {
                errors.addErrorMessage( this.getErrorMessage(  ) );
            }
            else
            {
                errors.addError( PROPERTY_VALIDATION_MESSAGE, messageParams );
            }
        }

        return bValid;
    }
}
