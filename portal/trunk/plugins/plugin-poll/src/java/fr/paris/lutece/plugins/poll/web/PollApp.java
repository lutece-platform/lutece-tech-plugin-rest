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
package fr.paris.lutece.plugins.poll.web;

import fr.paris.lutece.plugins.poll.business.Poll;
import fr.paris.lutece.plugins.poll.business.PollChoice;
import fr.paris.lutece.plugins.poll.business.PollChoiceHome;
import fr.paris.lutece.plugins.poll.business.PollHome;
import fr.paris.lutece.plugins.poll.business.PollQuestion;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * This class manages Poll page.
 */
public class PollApp implements XPageApplication
{
    // markers
    private static final String MARK_POLL = "poll";
    private static final String MARK_POLLS_LIST = "polls_list";

    // templates
    private static final String TEMPLATE_XPAGE_POLL_LIST = "skin/plugins/poll/poll_list.html";
    private static final String TEMPLATE_XPAGE_QUESTIONS_LIST = "skin/plugins/poll/questions_list.html";
    private static final String TEMPLATE_XPAGE_RESULTS = "skin/plugins/poll/results.html";
    private static final String TEMPLATE_XPAGE_ANSWER_EXISTS = "skin/plugins/poll/answer_exists.html";
    private static final String TEMPLATE_XPAGE_CONFIRMATION = "skin/plugins/poll/answer_confirmation.html";

    // properties for page titles and path label
    private static final String PROPERTY_XPAGE_PAGETITLE = "poll.xpage.pagetitle";
    private static final String PROPERTY_XPAGE_PATHLABEL = "poll.xpage.pathlabel";

    // request parameters
    private static final String PARAM_POLL_ID = "poll_id";
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_ACTION_EVALUATE = "evaluate";
    private static final String PARAM_ACTION_VIEWRESULTS = "results";
    private static final String PARAM_VOTED = "voted";

    /**
     * Returns the Poll XPage content depending on the request parameters and the current mode.
     *
     * @param request The HTTP request.
     * @param nMode The current mode.
     * @param plugin The Plugin
     * @return The page content.
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        XPage page = new XPage(  );
        String strIdPoll = request.getParameter( PARAM_POLL_ID );
        page.setTitle( I18nService.getLocalizedString( PROPERTY_XPAGE_PAGETITLE, request.getLocale(  ) ) );
        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_XPAGE_PATHLABEL, request.getLocale(  ) ) );

        //display of the list of the polls.
        if ( strIdPoll == null )
        {
            page.setContent( getPollsList( request, plugin ) );
        }
        else
        {
            Poll poll = PollHome.findByPrimaryKey( Integer.parseInt( strIdPoll ), plugin );
            String strAction = request.getParameter( PARAM_ACTION );

            if ( strAction == null )
            {
                page.setContent( getQuestions( request, poll ) );
            }
            else if ( strAction.equals( PARAM_ACTION_EVALUATE ) )
            {
                page.setContent( getConfirmation( request, poll, plugin ) );
            }
            else if ( strAction.equals( PARAM_ACTION_VIEWRESULTS ) )
            {
                page.setContent( getResults( request, poll ) );
            }
        }

        return page;
    }

    /**
     * Return the html code to display the poll list
     * @param plugin The Plugin
     * @return The Html template
     */
    private String getPollsList( HttpServletRequest request, Plugin plugin )
    {
        HashMap model = new HashMap(  );
        model.put( MARK_POLLS_LIST, PollHome.getEnabledPollList( plugin ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_POLL_LIST, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Returns the list of the questions (and choices) associated to the selected poll.
     * @param request The HttpServletRequest
     * @param poll The Poll object
     * @return the html code to display the list
     */
    private String getQuestions( HttpServletRequest request, Poll poll )
    {
        HashMap model = new HashMap(  );
        HttpSession session = request.getSession(  );
        model.put( MARK_POLL, poll );

        if ( session.getAttribute( poll.getName(  ) ) != null )
        {
            //the user has already voted
            return AppTemplateService.getTemplate( TEMPLATE_XPAGE_ANSWER_EXISTS, request.getLocale(  ), model ).getHtml(  );
        }
        else
        {
            return AppTemplateService.getTemplate( TEMPLATE_XPAGE_QUESTIONS_LIST, request.getLocale(  ), model )
                                     .getHtml(  );
        }
    }

    /**
     * Returns the confirmation of the vote
     * @param request The HttpServletRequest
     * @param poll The Poll object
     * @param plugin the plugin
     * @return The html code to display
     */
    private String getConfirmation( HttpServletRequest request, Poll poll, Plugin plugin )
    {
        HashMap model = new HashMap(  );
        HttpSession session = request.getSession(  );
        List<PollQuestion> listQuestions = poll.getQuestions(  );

        for ( PollQuestion question : listQuestions )
        {
            String strUserAnswer = request.getParameter( "q" + question.getId(  ) );

            if ( strUserAnswer != null )
            {
                int nUserAnswer = Integer.parseInt( strUserAnswer );
                PollChoice choice = PollChoiceHome.findByPrimaryKey( nUserAnswer, plugin );
                choice.setScore( choice.getScore(  ) + 1 );
                PollChoiceHome.update( choice, plugin );
            }
        }

        session.setAttribute( poll.getName(  ), PARAM_VOTED );
        model.put( MARK_POLL, poll );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_CONFIRMATION, request.getLocale(  ),
                model );

        return template.getHtml(  );
    }

    /**
     * Returns the results for the poll
     * @param request The HttpServletRequest
     * @param poll The Poll object
     * @return The html code to display
     */
    private String getResults( HttpServletRequest request, Poll poll )
    {
        HashMap model = new HashMap(  );
        model.put( MARK_POLL, poll );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_RESULTS, request.getLocale(  ), model );

        return template.getHtml(  );
    }
}
