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
package fr.paris.lutece.plugins.document.modules.rulemovespace.business;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.rules.AbstractRule;
import fr.paris.lutece.plugins.document.business.spaces.DocumentSpaceHome;
import fr.paris.lutece.plugins.document.business.workflow.DocumentState;
import fr.paris.lutece.plugins.document.business.workflow.DocumentStateHome;
import fr.paris.lutece.plugins.document.service.DocumentException;
import fr.paris.lutece.plugins.document.service.DocumentService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;


/**
 * This rule lets move a document from a space to another when the document's state is changing
 */
public class MoveSpaceRule extends AbstractRule
{
    private static final String TEMPLATE_CREATE_RULE = "/admin/plugins/document/modules/rulemovespace/create_rule_move_space.html";
    private static final String MARK_SPACES_LIST = "spaces_list";
    private static final String MARK_STATES_LIST = "states_list";
    private static final String PARAMETER_SPACE_SOURCE_ID = "id_space_source";
    private static final String PARAMETER_SPACE_DESTINATION_ID = "id_space_destination";
    private static final String PARAMETER_STATE_ID = "id_state";
    private static final String PROPERTY_RULE_DESCRIPTION = "module.document.rulemovespace.ruleLiteral";
    private static final String PROPERTY_RULE_ERROR_SAME_SPACE = "module.document.rulemovespace.message.create_rule_move_space.errorSameSpace";
    private static final String PROPERTY_RULE_UNKNOWN_ERROR = "module.document.rulemovespace.message.create_rule_move_space.unknownError";
    private static final String PROPERTY_RULE_NAME = "module.document.rulemovespace.ruleName";
    private static String[] _attributes = { PARAMETER_SPACE_SOURCE_ID, PARAMETER_SPACE_DESTINATION_ID, PARAMETER_STATE_ID };

    /**
     * Gets the Rule name key
     * @return The Rule name key
     */
    public String getNameKey(  )
    {
        return PROPERTY_RULE_NAME;
    }

    /**
     * Method invoked by the RuleEngine
     * @param document The document concerned by the rule
     * @param user The user of the action
     * @throws DocumentException raise when error occurs in event or rule
     */
    public void apply( Document document, AdminUser user )
        throws DocumentException
    {
        try
        {
            int nSourceSpace = Integer.parseInt( getAttribute( PARAMETER_SPACE_SOURCE_ID ) );
            int nDestinationSpace = Integer.parseInt( getAttribute( PARAMETER_SPACE_DESTINATION_ID ) );
            int nState = Integer.parseInt( getAttribute( PARAMETER_STATE_ID ) );

            if ( ( document.getStateId(  ) == nState ) && ( nSourceSpace != nDestinationSpace ) )
            {
                if ( document.getSpaceId(  ) == nSourceSpace )
                {
                    DocumentService.getInstance(  ).moveDocument( document, user, nDestinationSpace );
                }
            }
        }
        catch ( Exception e )
        {
        	AppLogService.error( "Error in MoveSpaceRule event : " + e.getMessage(  ) );
        	e.printStackTrace();
            throw new DocumentException( PROPERTY_RULE_UNKNOWN_ERROR );
        }
    }

    /**
     * Gets a specific form to enter rule's attributes
     * @param user The current user
     * @param locale The current Locale
     * @return The HTML code of the form
     */
    public String getCreateForm( AdminUser user, Locale locale )
    {
        HashMap model = new HashMap(  );
        Collection listSpaces = DocumentSpaceHome.getDocumentSpaceList(  );
        Collection listStates = DocumentStateHome.getDocumentStatesList( locale );
        model.put( MARK_SPACES_LIST, listSpaces );
        model.put( MARK_STATES_LIST, listStates );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_RULE, locale, model );

        return template.getHtml(  );
    }

    /**
     * Check the rule
     *
     * @return null if rule is valid, message if rule not valid
     */
    public String validateRule(  )
    {
        int nSourceSpace = Integer.parseInt( getAttribute( PARAMETER_SPACE_SOURCE_ID ) );
        int nDestinationSpace = Integer.parseInt( getAttribute( PARAMETER_SPACE_DESTINATION_ID ) );

        if ( nSourceSpace == nDestinationSpace )
        {
            return PROPERTY_RULE_ERROR_SAME_SPACE;
        }

        return null;
    }

    /**
     * Gets all attributes of the rule
     * @return attributes of the rule
     */
    public String[] getAttributesList(  )
    {
        return _attributes;
    }

    /**
     * Gets the explicit text of the rule
     * @return The text of the rule
     */
    public String getRule(  )
    {
        int nSourceSpaceId = Integer.parseInt( getAttribute( PARAMETER_SPACE_SOURCE_ID ) );
        String strSourceSpace = DocumentSpaceHome.findByPrimaryKey( nSourceSpaceId ).getName(  );
        int nDestinationSpaceId = Integer.parseInt( getAttribute( PARAMETER_SPACE_DESTINATION_ID ) );
        String strDestinationSpace = DocumentSpaceHome.findByPrimaryKey( nDestinationSpaceId ).getName(  );
        int nStateId = Integer.parseInt( getAttribute( PARAMETER_STATE_ID ) );
        DocumentState state = DocumentStateHome.findByPrimaryKey( nStateId );
        state.setLocale( getLocale(  ) );

        String strState = state.getName(  );
        String[] ruleArgs = { strSourceSpace, strState, strDestinationSpace };

        return I18nService.getLocalizedString( PROPERTY_RULE_DESCRIPTION, ruleArgs, getLocale(  ) );
    }
}
