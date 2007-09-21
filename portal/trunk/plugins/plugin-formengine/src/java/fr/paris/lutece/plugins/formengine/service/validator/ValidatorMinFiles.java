/*
 * ValidatorMinFiles.java
 *
 * Created on 8 mars 2007, 10:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package fr.paris.lutece.plugins.formengine.service.validator;

import fr.paris.lutece.plugins.formengine.business.jaxb.formdefinition.Field;
import fr.paris.lutece.plugins.formengine.service.validator.FieldValidator;
import fr.paris.lutece.plugins.formengine.web.FormErrorsList;


/**
 *
 * @author mazalaiv
 * This class checks the minimum number of files for an upload field.
 */
public class ValidatorMinFiles extends FieldValidator
{
    private static final String PROPERTY_VALIDATION_MESSAGE = "formengine.validator.message.minFiles";

    /**
     * The validate implementation.
     * Check that the field does not contain more files than the number given
     * as the parameter rule
     * @param field The field to validate
     * @param errors A FormErrorsList to add errors into
     * @return True if validate, false otherwise
     */
    public boolean validate( Field field, FormErrorsList errors )
    {
        // The parameter contains the minimum number of files allowed
        int nMinFiles = Integer.parseInt( this.getRuleParameter(  ) );

        if ( ( field.getFileNames(  ) == null ) || ( field.getFileNames(  ).getFileName(  ).size(  ) < nMinFiles ) )
        {
            String[] messageParams = new String[3];
            messageParams[0] = field.getName(  );
            messageParams[1] = field.getLabel(  );
            messageParams[2] = String.valueOf( nMinFiles );

            if ( this.getErrorMessage(  ) != null )
            {
                errors.addErrorMessage( this.getErrorMessage(  ) );
            }
            else
            {
                errors.addError( PROPERTY_VALIDATION_MESSAGE, messageParams );
            }

            return false;
        }

        return true;
    }
}
