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
package fr.paris.lutece.plugins.helpdesk.web;

import fr.paris.lutece.plugins.helpdesk.business.QuestionAnswer;
import fr.paris.lutece.plugins.helpdesk.business.QuestionAnswerHome;
import fr.paris.lutece.plugins.helpdesk.business.QuestionTopic;
import fr.paris.lutece.plugins.helpdesk.business.QuestionTopicHome;
import fr.paris.lutece.plugins.helpdesk.business.QuestionType;
import fr.paris.lutece.plugins.helpdesk.business.QuestionTypeHome;
import fr.paris.lutece.plugins.helpdesk.business.Subject;
import fr.paris.lutece.plugins.helpdesk.business.SubjectHome;
import fr.paris.lutece.plugins.helpdesk.business.VisitorQuestion;
import fr.paris.lutece.plugins.helpdesk.business.VisitorQuestionHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class implements the HelpDesk XPage.
 */
public class HelpdeskApp implements XPageApplication
{
    private static final String TEMPLATE_SUBJECT_LIST = "skin/plugins/helpdesk/subject_list.html";
    private static final String TEMPLATE_SUBJECT_LIST_ROW = "skin/plugins/helpdesk/subject_list_row.html";
    private static final String TEMPLATE_QUESTION_LIST_ROW = "skin/plugins/helpdesk/question_list_row.html";
    private static final String TEMPLATE_SUBJECT_LIST_ROW_RETAIL = "skin/plugins/helpdesk/subject_list_row_retail.html";
    private static final String TEMPLATE_QUESTION_LIST_ROW_RETAIL = "skin/plugins/helpdesk/question_list_row_retail.html";
    private static final String TEMPLATE_CONTACT_FORM_RESULT = "skin/plugins/helpdesk/contact_result.html";
    private static final String TEMPLATE_CONTACT_FORM = "skin/plugins/helpdesk/contact.html";
    private static final String TEMPLATE_SEND_VISITOR_QUESTION = "skin/plugins/helpdesk/send_visitor_question.html";
    private static final String TEMPLATE_CONTACT_FORM_ERROR = "skin/plugins/helpdesk/contact_error.html";
    private static final String BOOKMARK_SUBJECT_NAME = "@subject_name@";
    private static final String BOOKMARK_QUESTION_LIST_ROWS_RETAIL = "@question_list_rows_retail@";
    private static final String BOOKMARK_ANSWER = "@answer@";
    private static final String BOOKMARK_QUESTION_ID = "@question_id@";
    private static final String BOOKMARK_QUESTION_LIST_ROWS = "@question_list_rows@";
    private static final String BOOKMARK_QUESTION = "@question@";
    private static final String PARAM_KEYWORDS = "form_search_keywords";
    private static final String PARAMETER_QUESTION_TOPIC_ID = "question_topic_id";
    private static final String PARAMETER_QUESTION_TYPE_ID = "question_type_id";
    private static final String PARAMETER_QUESTION = "question";
    private static final String PARAMETER_LAST_NAME = "last_name";
    private static final String PARAMETER_FIRST_NAME = "first_name";
    private static final String PARAMETER_EMAIL = "email";
    private static final String PROPERTY_SUBJECTLIST = "helpdesk.pageTitle.subjectList";
    private static final String PROPERTY_HELPDESKPATHLABEL = "helpdesk.pagePathLabel";
    private static final String PROPERTY_SUBJECTLIST_RESULTS = "helpdesk.pageTitle.subjectList_results";
    private static final String PROPERTY_SEARCH_NOK = "helpdesk.message.search_nok";
    private static final String PROPERTY_PORTAL_NAME = "lutece.name";
    private static final String PROPERTY_WEBMASTER_EMAIL = "email.webmaster";
    private static final String PROPERTY_MAIL_HELPDESK_SUBJECT = "mail.helpdesk.subject";

    //Markers
    private static final String MARK_CONTACT_LIST = "contact_list";
    private static final String MARK_VISITOR_QUESTION = "visitor_question";
    private static final String MARK_PORTAL_URL = "portal_url";
    private static final String MARK_DATE = "date";
    private static final String MARK_PLUGIN_NAME = "plugin_name";
    private static final String MARK_SUBJECT_LIST_ROWS = "subject_list_rows";
    private static final String MARK_SUBJECT_LIST_ROWS_RETAIL = "subject_list_rows_retail";
    private static final String MARK_SEARCHED_KEYWORDS = "searched_keywords";
    private static final String MARK_PATH_LABEL = "path_label";
    private static final String MARK_QUESTION_TYPE = "question_type";
    private static final String MARK_QUESTION_TYPE_LIST = "question_type_list";
    private static final String JSP_CONTACT_FORM_RESULT = "ContactFormResult.jsp";
    private static final String JSP_CONTACT_ERROR = "ContactFormError.jsp";
    private Plugin _plugin;

    /**
     * Creates a new QuizPage object.
     */
    public HelpdeskApp(  )
    {
    }

    /**
     * Returns the Helpdesk XPage content depending on the request parameters and the current mode.
     *
     * @param request The HTTP request.
     * @param nMode The current mode.
         * @param plugin The plugin.
     * @return The page content.
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        XPage page = new XPage(  );

        String strKeywords = request.getParameter( PARAM_KEYWORDS );

        if ( ( strKeywords == null ) || ( strKeywords.equals( "" ) ) )
        {
            page.setContent( getSubjectList( request, plugin ) );
            page.setPathLabel( AppPropertiesService.getProperty( PROPERTY_HELPDESKPATHLABEL ) );
            page.setTitle( AppPropertiesService.getProperty( PROPERTY_SUBJECTLIST ) );
        }
        else
        {
            page.setContent( getSubjectListSearch( strKeywords, request, plugin ) );
            page.setPathLabel( AppPropertiesService.getProperty( PROPERTY_HELPDESKPATHLABEL ) );
            page.setTitle( AppPropertiesService.getProperty( PROPERTY_SUBJECTLIST_RESULTS ) );
        }

        return page;
    }

    /**
    * Returns the contact form
      * @param request The Html request
    * @return The Html template
    */
    public String getContactForm( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        String strPluginName = AppPropertiesService.getProperty( PROPERTY_HELPDESKPATHLABEL );
        _plugin = PluginService.getPlugin( strPluginName );

        List<QuestionType> listQuestionType = QuestionTypeHome.selectQuestionTypeList( _plugin );
        model.put( MARK_QUESTION_TYPE_LIST, listQuestionType );

        List<QuestionTopic> listQuestionTopic = QuestionTopicHome.selectTopicList( _plugin );
        model.put( MARK_CONTACT_LIST, listQuestionTopic );

        for( QuestionType questionType : listQuestionType )
        {
            questionType = QuestionTypeHome.findByPrimaryKey( questionType.getIdQuestionType(  ), _plugin );
            model.put( MARK_QUESTION_TYPE, questionType );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONTACT_FORM, request.getLocale(  ), model );

        return template.getHtml(  );
    }

    /**
     * Processes the sending of a question
         * @param request The Http request
     * @return The URL to redirect to
     */
    public String doSendQuestionMail( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        String strPluginName = AppPropertiesService.getProperty( PROPERTY_HELPDESKPATHLABEL );
        _plugin = PluginService.getPlugin( strPluginName );

        String strQuestionTopicId = request.getParameter( PARAMETER_QUESTION_TOPIC_ID );
        String strQuestionTypeId = request.getParameter( PARAMETER_QUESTION_TYPE_ID );
        String strVisitorLastName = request.getParameter( PARAMETER_LAST_NAME );
        String strVisitorFirstName = request.getParameter( PARAMETER_FIRST_NAME );
        String strVisitorEmail = request.getParameter( PARAMETER_EMAIL );
        String strQuestion = request.getParameter( PARAMETER_QUESTION );

        // Mandatory field
        if ( ( strVisitorLastName == null ) || ( strVisitorFirstName == null ) || strQuestion.equals( "" ) ||
                ( strVisitorEmail == null ) || ( strVisitorEmail.indexOf( '@' ) == -1 ) ||
                ( strVisitorEmail.length(  ) < 5 ) )
        {
            return JSP_CONTACT_ERROR;
        }

        String strToday = DateUtil.getCurrentDateString(  );
        java.sql.Date dateDateVQ = DateUtil.getDateSql( strToday );

        int nIdQuestionTopic = 0;
        int nIdQuestionType = 0;

        QuestionTopic questionTopic = new QuestionTopic(  );

        if ( strQuestionTopicId == null )
        {
            nIdQuestionTopic = 0;
        }
        else
        {
            nIdQuestionType = Integer.parseInt( strQuestionTypeId );
            questionTopic = QuestionTopicHome.findByPrimaryKey( nIdQuestionTopic, _plugin );
        }

        if ( strQuestionTypeId == null )
        {
            nIdQuestionType = questionTopic.getIdQuestionType(  );
        }
        else
        {
            nIdQuestionType = Integer.parseInt( strQuestionTypeId );
        }

        QuestionType questionType = QuestionTypeHome.findByPrimaryKey( nIdQuestionType, _plugin );

        VisitorQuestion visitorQuestion = new VisitorQuestion(  );
        visitorQuestion.setLastname( strVisitorLastName );
        visitorQuestion.setFirstname( strVisitorFirstName );
        visitorQuestion.setEmail( strVisitorEmail );
        visitorQuestion.setQuestion( strQuestion );
        visitorQuestion.setIdQuestionType( nIdQuestionType );
        visitorQuestion.setIdQuestionTopic( nIdQuestionTopic );
        visitorQuestion.setDate( dateDateVQ );
        visitorQuestion.setAnswer( "" );

        VisitorQuestionHome.create( visitorQuestion, _plugin );

        String strBaseUrl = AppPathService.getBaseUrl( request );

        model.put( MARK_VISITOR_QUESTION, visitorQuestion );
        model.put( MARK_DATE, dateDateVQ );
        model.put( MARK_PORTAL_URL, strBaseUrl );
        model.put( MARK_PLUGIN_NAME, _plugin );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SEND_VISITOR_QUESTION, request.getLocale(  ),
                model );

        String strPortal = AppPropertiesService.getProperty( PROPERTY_PORTAL_NAME );
        String strSubject = AppPropertiesService.getProperty( PROPERTY_MAIL_HELPDESK_SUBJECT );
        String strEmailWebmaster = AppPropertiesService.getProperty( PROPERTY_WEBMASTER_EMAIL );
        String strMessage = template.getHtml(  );

        //find role key
        Collection listUsers = AdminUserHome.findByRole( questionTopic.RESOURCE_TYPE );
        Collection listUser = AdminUserHome.findByRole( questionType.RESOURCE_TYPE );
        Iterator i = listUsers.iterator(  );
        Iterator iter = listUser.iterator(  );

        while ( i.hasNext(  ) )
        {
            AdminUser helpdeskUser = (AdminUser) i.next(  );
            strEmailWebmaster = helpdeskUser.getEmail(  );
            MailService.sendMail( strEmailWebmaster, strPortal, visitorQuestion.getEmail(  ), strSubject, strMessage );
        }

        while ( iter.hasNext(  ) )
        {
            AdminUser helpdeskUser = (AdminUser) iter.next(  );
            strEmailWebmaster = helpdeskUser.getEmail(  );
            MailService.sendMail( strEmailWebmaster, strPortal, visitorQuestion.getEmail(  ), strSubject, strMessage );
        }

        // If the operation is successfull, redirect towards the confirmation page
        return JSP_CONTACT_FORM_RESULT;
    }

    /**
     * Returns the contact form's result page
     * @param request The Http request
     * @return The Html template
     */
    public String getContactFormResult( HttpServletRequest request )
    {
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONTACT_FORM_RESULT, request.getLocale(  ) );

        return template.getHtml(  );
    }

    /**
     * Returns the contact form's result page
     * @param request The Http request
     * @return The Html template
     */
    public String getContactFormError( HttpServletRequest request )
    {
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONTACT_FORM_ERROR, request.getLocale(  ) );

        return template.getHtml(  );
    }

    /**
     * Returns the list of subjects.
     * @param request The Http request
     * @param plugin The plugin
     * @return The Html template
     */
    private String getSubjectList( HttpServletRequest request, Plugin plugin )
    {
        HashMap model = new HashMap(  );

        String strPathLabel = AppPropertiesService.getProperty( PROPERTY_HELPDESKPATHLABEL );

        HtmlTemplate tRow = AppTemplateService.getTemplate( TEMPLATE_SUBJECT_LIST_ROW, request.getLocale(  ) );
        HtmlTemplate tRowRetail = AppTemplateService.getTemplate( TEMPLATE_SUBJECT_LIST_ROW_RETAIL,
                request.getLocale(  ) );
        HtmlTemplate tRowQuestion = AppTemplateService.getTemplate( TEMPLATE_QUESTION_LIST_ROW, request.getLocale(  ) );
        HtmlTemplate tRowQuestionRetail = AppTemplateService.getTemplate( TEMPLATE_QUESTION_LIST_ROW_RETAIL,
                request.getLocale(  ) );
        StringBuffer strListSubject = new StringBuffer(  );
        StringBuffer strListSubjectRetail = new StringBuffer(  );
        Collection listSubject = SubjectHome.findAll( plugin );
        Iterator i = listSubject.iterator(  );

        while ( i.hasNext(  ) )
        {
            Subject subject = (Subject) i.next(  );
            HtmlTemplate templateRow = new HtmlTemplate( tRow );
            HtmlTemplate templateRowRetail = new HtmlTemplate( tRowRetail );

            templateRow.substitute( BOOKMARK_SUBJECT_NAME, subject.getSubject(  ) );
            templateRowRetail.substitute( BOOKMARK_SUBJECT_NAME, subject.getSubject(  ) );

            StringBuffer strListSubjectRow = new StringBuffer(  );
            StringBuffer strListSubjectRowRetail = new StringBuffer(  );
            Collection listQuestion = subject.getQuestions(  );
            Iterator iterQuestions = listQuestion.iterator(  );
            int nQuestionExists = 0;

            while ( iterQuestions.hasNext(  ) )
            {
                QuestionAnswer question = (QuestionAnswer) iterQuestions.next(  );

                if ( question.isEnabled(  ) )
                {
                    nQuestionExists = 1;

                    HtmlTemplate templateRowQuestion = new HtmlTemplate( tRowQuestion );
                    HtmlTemplate templateRowQuestionRetail = new HtmlTemplate( tRowQuestionRetail );
                    templateRowQuestion.substitute( BOOKMARK_QUESTION_ID, question.getIdQuestionAnswer(  ) );
                    templateRowQuestion.substitute( BOOKMARK_QUESTION, question.getQuestion(  ) );
                    templateRowQuestionRetail.substitute( BOOKMARK_QUESTION_ID, question.getIdQuestionAnswer(  ) );
                    templateRowQuestionRetail.substitute( BOOKMARK_QUESTION, question.getQuestion(  ) );
                    templateRowQuestionRetail.substitute( BOOKMARK_ANSWER, question.getAnswer(  ) );
                    strListSubjectRow.append( templateRowQuestion.getHtml(  ) );
                    strListSubjectRowRetail.append( templateRowQuestionRetail.getHtml(  ) );
                }
            }

            if ( nQuestionExists == 1 )
            {
                templateRow.substitute( BOOKMARK_QUESTION_LIST_ROWS, strListSubjectRow.toString(  ) );
                templateRowRetail.substitute( BOOKMARK_QUESTION_LIST_ROWS_RETAIL, strListSubjectRowRetail.toString(  ) );
                strListSubject.append( templateRow.getHtml(  ) );
                strListSubjectRetail.append( templateRowRetail.getHtml(  ) );
            }
        }

        model.put( MARK_SUBJECT_LIST_ROWS, strListSubject.toString(  ) );
        model.put( MARK_SUBJECT_LIST_ROWS_RETAIL, strListSubjectRetail.toString(  ) );
        model.put( MARK_SEARCHED_KEYWORDS, "" );
        model.put( MARK_PATH_LABEL, strPathLabel );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_SUBJECT_LIST, request.getLocale(  ), model );

        return t.getHtml(  );
    }

    /**
     * Returns the list of subjects containing a set of keywords.
     * @param strKeywords The keywords
     * @param plugin The plugin
     * @param request The Http request
     * @return The Html template
     */
    private String getSubjectListSearch( String strKeywords, HttpServletRequest request, Plugin plugin )
    {
        HashMap model = new HashMap(  );
        String strPathLabel = AppPropertiesService.getProperty( PROPERTY_HELPDESKPATHLABEL );

        HtmlTemplate tRow = AppTemplateService.getTemplate( TEMPLATE_SUBJECT_LIST_ROW, request.getLocale(  ) );
        HtmlTemplate tRowRetail = AppTemplateService.getTemplate( TEMPLATE_SUBJECT_LIST_ROW_RETAIL,
                request.getLocale(  ) );
        HtmlTemplate tRowQuestion = AppTemplateService.getTemplate( TEMPLATE_QUESTION_LIST_ROW, request.getLocale(  ) );
        HtmlTemplate tRowQuestionRetail = AppTemplateService.getTemplate( TEMPLATE_QUESTION_LIST_ROW_RETAIL,
                request.getLocale(  ) );
        StringBuffer strListQuestionAnswer = new StringBuffer(  );
        StringBuffer strListSubjectRetail = new StringBuffer(  );
        List<QuestionAnswer> listQuestionAnswer = QuestionAnswerHome.findByKeywords( strKeywords, plugin );
        Iterator i = listQuestionAnswer.iterator(  );
        int nPreviousIdSubject = 0;

        if ( i.hasNext(  ) )
        {
            QuestionAnswer questionanswer = (QuestionAnswer) i.next(  );

            while ( nPreviousIdSubject != questionanswer.getIdSubject(  ) )
            {
                StringBuffer strListSubjectRow = new StringBuffer(  );
                StringBuffer strListSubjectRowRetail = new StringBuffer(  );
                Subject subject = SubjectHome.findByPrimaryKey( questionanswer.getIdSubject(  ), plugin );
                HtmlTemplate templateRow = new HtmlTemplate( tRow );
                HtmlTemplate templateRowRetail = new HtmlTemplate( tRowRetail );
                nPreviousIdSubject = questionanswer.getIdSubject(  );

                boolean bExistsEnabledResult = false;

                while ( nPreviousIdSubject == questionanswer.getIdSubject(  ) )
                {
                    if ( questionanswer.isEnabled(  ) )
                    {
                        bExistsEnabledResult = true;

                        HtmlTemplate templateRowQuestion = new HtmlTemplate( tRowQuestion );
                        HtmlTemplate templateRowQuestionRetail = new HtmlTemplate( tRowQuestionRetail );
                        templateRowQuestion.substitute( BOOKMARK_QUESTION_ID, questionanswer.getIdQuestionAnswer(  ) );
                        templateRowQuestion.substitute( BOOKMARK_QUESTION, questionanswer.getQuestion(  ) );
                        templateRowQuestionRetail.substitute( BOOKMARK_QUESTION_ID,
                            questionanswer.getIdQuestionAnswer(  ) );
                        templateRowQuestionRetail.substitute( BOOKMARK_QUESTION, questionanswer.getQuestion(  ) );
                        templateRowQuestionRetail.substitute( BOOKMARK_ANSWER, questionanswer.getAnswer(  ) );
                        strListSubjectRow.append( templateRowQuestion.getHtml(  ) );
                        strListSubjectRowRetail.append( templateRowQuestionRetail.getHtml(  ) );
                    }

                    if ( i.hasNext(  ) )
                    {
                        questionanswer = (QuestionAnswer) i.next(  );
                    }
                    else
                    {
                        break;
                    }
                }

                if ( bExistsEnabledResult )
                {
                    templateRow.substitute( BOOKMARK_SUBJECT_NAME, subject.getSubject(  ) );
                    templateRowRetail.substitute( BOOKMARK_SUBJECT_NAME, subject.getSubject(  ) );
                    templateRow.substitute( BOOKMARK_QUESTION_LIST_ROWS, strListSubjectRow.toString(  ) );
                    templateRowRetail.substitute( BOOKMARK_QUESTION_LIST_ROWS_RETAIL,
                        strListSubjectRowRetail.toString(  ) );
                    strListQuestionAnswer.append( templateRow.getHtml(  ) );
                    strListSubjectRetail.append( templateRowRetail.getHtml(  ) );
                }
            }

            model.put( MARK_SUBJECT_LIST_ROWS, strListQuestionAnswer.toString(  ) );
        }
        else
        {
            String strNoResult = I18nService.getLocalizedString( PROPERTY_SEARCH_NOK, request.getLocale(  ) );
            model.put( MARK_SUBJECT_LIST_ROWS, strNoResult );
        }

        model.put( MARK_SUBJECT_LIST_ROWS_RETAIL, strListSubjectRetail.toString(  ) );
        model.put( MARK_SEARCHED_KEYWORDS, strKeywords );
        model.put( MARK_PATH_LABEL, strPathLabel );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_SUBJECT_LIST, request.getLocale(  ), model );

        return t.getHtml(  );
    }
}
