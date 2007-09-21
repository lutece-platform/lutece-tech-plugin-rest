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
import fr.paris.lutece.plugins.helpdesk.service.QuestionTopicResourceIdService;
import fr.paris.lutece.plugins.helpdesk.service.QuestionTypeResourceIdService;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage helpdesk features ( manage,
 * create, modify, remove)
 */
public class HelpdeskJspBean extends PluginAdminPageJspBean
{
    //right   
    public static final String RIGHT_MANAGE_HELPDESK = "HELPDESK_MANAGEMENT";

    //Jsp
    private static final String JSP_SUBJECTS_LIST = "SubjectsList.jsp";
    private static final String JSP_QUESTION_ANSWER_LIST = "QuestionAnswerList.jsp";
    private static final String JSP_VISITOR_QUESTION_LIST = "VisitorQuestionList.jsp";
    private static final String JSP_CREATE_QUESTION_ANSWER = "CreateQuestionAnswer.jsp";
    private static final String JSP_ARCHIVED_QUESTION_LIST = "ArchivedQuestionList.jsp";
    private static final String JSP_DO_REMOVE_SUBJECT = "jsp/admin/plugins/helpdesk/DoRemoveSubject.jsp";
    private static final String JSP_DO_REMOVE_QUESTION_ANSWER = "jsp/admin/plugins/helpdesk/DoRemoveQuestionAnswer.jsp";
    private static final String JSP_QUESTION_VISITOR_LIST = "jsp/admin/plugins/helpdesk/VisitorQuestionList.jsp";
    private static final String JSP_QUESTION_ARCHIVED_LIST = "jsp/admin/plugins/helpdesk/ArchivedQuestionList.jsp";
    private static final String JSP_LIST_SUBJECTS = "jsp/admin/plugins/helpdesk/SubjectsList.jsp";

    //Parameters
    private static final String PARAMETER_LAST_NAME = "last_name";
    private static final String PARAMETER_FIRST_NAME = "first_name";
    private static final String PARAMETER_EMAIL = "email";
    private static final String PARAMETER_QUESTION = "question";
    private static final String PARAMETER_QUESTION_ID = "question_id";
    private static final String PARAMETER_STATUS = "status";
    private static final String PARAMETER_ANSWER = "answer";
    private static final String PARAMETER_SUBJECT_ID = "subject_id";
    private static final String PARAMETER_SUBJECT = "subject";
    private static final String PARAMETER_ADD_QUESTION_ANSWER = "add_question_answer";

    //Templates
    private static final String TEMPLATE_SUBJECTS = "admin/plugins/helpdesk/subjects.html";
    private static final String TEMPLATE_CREATE_SUBJECT = "admin/plugins/helpdesk/create_subject.html";
    private static final String TEMPLATE_MODIFY_SUBJECT = "admin/plugins/helpdesk/modify_subject.html";
    private static final String TEMPLATE_HELPDESK_MENU = "admin/plugins/helpdesk/helpdesk_menu.html";
    private static final String TEMPLATE_QUESTION_ANSWER_LIST = "admin/plugins/helpdesk/question_answer_list.html";
    private static final String TEMPLATE_CREATE_QUESTION_ANSWER = "admin/plugins/helpdesk/create_question_answer.html";
    private static final String TEMPLATE_MODIFY_QUESTION_ANSWER = "admin/plugins/helpdesk/modify_question_answer.html";
    private static final String TEMPLATE_MODIFY_VISITOR_QUESTION = "admin/plugins/helpdesk/modify_visitor_question.html";
    private static final String TEMPLATE_ANSWER_SELECTION = "admin/plugins/helpdesk/answer_selection.html";
    private static final String TEMPLATE_VISITOR_QUESTION_LIST = "admin/plugins/helpdesk/visitor_question_list.html";
    private static final String TEMPLATE_VIEW_VISITOR_QUESTION = "admin/plugins/helpdesk/view_visitor_question.html";
    private static final String TEMPLATE_SEND_ANSWER = "admin/plugins/helpdesk/send_answer.html";
    private static final String TEMPLATE_ARCHIVED_QUESTION_LIST = "admin/plugins/helpdesk/archived_question_list.html";

    //properties
    private static final String PROPERTY_PAGE_TITLE_MANAGE_HELPDESK_MENU = "helpdesk.helpdesk_menu.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_SUBJECT_LIST = "helpdesk.subjects.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_SUBJECT = "helpdesk.create_subject.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_QUESTION_ANSWER_LIST = "helpdesk.question_answer_list.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_QUESTION_ANSWER = "helpdesk.create_question_answer.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_QUESTION_ANSWER = "helpdesk.modify_question_answer.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_VISITOR_QUESTION = "helpdesk.modify_visitor_question.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_VISITOR_QUESTION_LIST = "helpdesk.visitor_question_list.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_ARCHIVED_QUESTION_LIST = "helpdesk.archived_question_list.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_SUBJECT = "helpdesk.modify_subject.pageTitle";
    private static final String PROPERTY_MAIL_SUBJECT = "mail.helpdesk.subject";
    private static final String PROPERTY_PORTAL_NAME = "lutece.name";
    private static final String PROPERTY_MAIL_HELPDESK_SENDER = "mail.helpdesk.sender";
    private static final String PROPERTY_CHECK = "checked";
    private static final String PROPERTY_NULL = "";
    private static final String PROPERTY_STYLES_PER_PAGE = "paginator.style.itemsPerPage";

    //Messages
    private static final String MESSAGE_CONFIRM_DELETE_SUBJECT = "helpdesk.message.confirmDeleteSubject";
    private static final String MESSAGE_CONFIRM_DELETE_QUESTION_ANSWER = "helpdesk.message.confirmDeleteQuestionAnswer";

    // Markers       
    private static final String MARK_VISITOR_QUESTION = "visitor_question";
    private static final String MARK_PORTAL_URL = "portal_url";
    private static final String MARK_VISITOR_QUESTION_LIST = "helpdesk_visitor_question_list";
    private static final String MARK_QUESTIONS_NUMBER = "questions_number";
    private static final String MARK_SUBJECT_LIST = "helpdesk_subject_list";
    private static final String MARK_SUBJECT = "subject";
    private static final String MARK_QUESTION_LIST = "helpdesk_question_list";
    private static final String MARK_QUESTION_ANSWER = "question_answer";
    private static final String MARK_CHECKED = "checked";
    private static final String MARK_ANSWER = "answer";
    private static final String MARK_QUESTION = "question";
    private static final String MARK_HELPDESK_USER = "helpdesk_user";
    private static final String MARK_QUESTION_ANSWER_LIST = "question_answer_list";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_WEBAPP_URL = "webapp_url";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_HTML_CONTENT = "html_content";

    // Parameter
    private static final String PARAMETER_CONTENT_HTML = "html_content";
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;

    /**
     * Creates a new HelpdeskJspBean object.
     */
    public HelpdeskJspBean(  )
    {
    }

    /**
     * Returns the helpdesk menu
     * @param request The Http request
     * @return The Html template
     */
    public String getManageHelpdesk( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_HELPDESK_MENU );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_HELPDESK_MENU, getLocale(  ) );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the list of subjects
     * @param request The Http request
     * @return The Html template
     */
    public String getSubjectsList( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        setPageTitleProperty( PROPERTY_PAGE_TITLE_SUBJECT_LIST );

        List<Subject> listSubject = SubjectHome.findAll( getPlugin(  ) );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_STYLES_PER_PAGE, 10 );
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        Paginator paginator = new Paginator( listSubject, _nItemsPerPage, JSP_LIST_SUBJECTS,
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_SUBJECT_LIST, paginator.getPageItems(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SUBJECTS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the subject creation form
     * @param request The Http request
     * @return The Html template
     */
    public String getCreateSubject( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_SUBJECT );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_SUBJECT, getLocale(  ) );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes subject creation
         * @param request The Http request
     * @return The URL to redirect to
     */
    public String doCreateSubject( HttpServletRequest request )
    {
        String strSubject = request.getParameter( PARAMETER_SUBJECT );
        Subject subject = new Subject(  );
        subject.setSubject( strSubject );

        // Mandatory field
        if ( request.getParameter( PARAMETER_SUBJECT ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        SubjectHome.create( subject, getPlugin(  ) );

        // If the operation is successfull, redirect towards the list of subjects
        return JSP_SUBJECTS_LIST;
    }

    /**
     * Returns the subject modification form
         * @param request The Http request
     * @return The Html template
     */
    public String getModifySubject( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_SUBJECT );

        String strIdSubject = request.getParameter( PARAMETER_SUBJECT_ID );

        int nIdSubject = Integer.parseInt( strIdSubject );
        Subject subject = new Subject(  );
        subject = SubjectHome.findByPrimaryKey( nIdSubject, getPlugin(  ) );

        model.put( MARK_SUBJECT, subject );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_SUBJECT, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes subject modification
     * @param request The Http request
     * @return The URL to redirect to
     */
    public String doModifySubject( HttpServletRequest request )
    {
        String strSubject = request.getParameter( PARAMETER_SUBJECT );
        String strIdSubject = request.getParameter( PARAMETER_SUBJECT_ID );
        int nIdSubject = Integer.parseInt( strIdSubject );
        Subject subject = new Subject(  );
        subject.setIdSubject( nIdSubject );
        subject.setSubject( strSubject );

        // Mandatory field
        if ( request.getParameter( PARAMETER_SUBJECT ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        SubjectHome.update( subject, getPlugin(  ) );

        // If the operation is successfull, redirect towards the list of subjects
        return JSP_SUBJECTS_LIST;
    }

    /**
     * Returns the subject removal form
     * @param request The Http request
     * @return The Html template
     */
    public String getConfirmRemoveSubject( HttpServletRequest request )
    {
        int nIdSubject = Integer.parseInt( request.getParameter( PARAMETER_SUBJECT_ID ) );

        UrlItem url = new UrlItem( JSP_DO_REMOVE_SUBJECT );
        url.addParameter( PARAMETER_SUBJECT_ID, nIdSubject );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_SUBJECT, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Processes subject removal
     * @param request The Http request
     * @return The URL to redirect to
     */
    public String doRemoveSubject( HttpServletRequest request )
    {
        int nIdSubject = Integer.parseInt( request.getParameter( PARAMETER_SUBJECT_ID ) );

        if ( QuestionAnswerHome.countbySubject( nIdSubject, getPlugin(  ) ) > 0 )
        {
            QuestionAnswerHome.removeBySubject( nIdSubject, getPlugin(  ) );
        }

        SubjectHome.remove( nIdSubject, getPlugin(  ) );

        // If the operation is successfull, redirect towards the list of subjects
        return JSP_SUBJECTS_LIST;
    }

    /**
      * Returns the list of QuestionAnswer
      * @param request The Http request
      * @return The Html template
      */
    public String getQuestionAnswerList( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        setPageTitleProperty( PROPERTY_PAGE_TITLE_QUESTION_ANSWER_LIST );

        List<Subject> listSubject = SubjectHome.findAll( getPlugin(  ) );
        model.put( MARK_SUBJECT_LIST, listSubject );       
        model.put( MARK_QUESTION_LIST, QuestionAnswerHome.findAll( getPlugin(  ) ) );
        
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_QUESTION_ANSWER_LIST, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the QuestionAnswer creation form
     * @param request The Http request
     * @return The Html template
     */
    public String getCreateQuestionAnswer( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_QUESTION_ANSWER );

        String strIdQuestion = request.getParameter( PARAMETER_QUESTION_ID );

        ReferenceList listSubject = SubjectHome.findSubject( getPlugin(  ) );
        model.put( MARK_SUBJECT_LIST, listSubject );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, AdminUserService.getLocale( request ).getLanguage(  ) );

        if ( strIdQuestion == null )
        {
            model.put( MARK_QUESTION, "" );
            model.put( MARK_HTML_CONTENT, "" );
        }
        else
        {
            VisitorQuestion visitorQuestion = VisitorQuestionHome.findByPrimaryKey( Integer.parseInt( strIdQuestion ),
                    getPlugin(  ) );
            model.put( MARK_QUESTION, visitorQuestion.getQuestion(  ) );
            model.put( MARK_HTML_CONTENT, visitorQuestion.getAnswer(  ) );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_QUESTION_ANSWER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes QuestionAnswer creation
     * @param request The Http request
     * @return The URL to redirect to
     */
    public String doCreateQuestionAnswer( HttpServletRequest request )
    {
        int nIdSubject = Integer.parseInt( request.getParameter( PARAMETER_SUBJECT_ID ) );
        String strQuestion = request.getParameter( PARAMETER_QUESTION );
        String strAnswer = request.getParameter( PARAMETER_CONTENT_HTML );
        String strStatus = request.getParameter( PARAMETER_STATUS );
        int nStatus = 0;

        if ( strStatus != null )
        {
            nStatus = Integer.parseInt( strStatus );
        }

        QuestionAnswer questionAnswer = new QuestionAnswer(  );
        questionAnswer.setIdSubject( nIdSubject );
        questionAnswer.setQuestion( strQuestion );
        questionAnswer.setAnswer( strAnswer );
        questionAnswer.setStatus( nStatus );

        // Mandatory field
        if ( request.getParameter( PARAMETER_SUBJECT_ID ).equals( "" ) ||
                request.getParameter( ( PARAMETER_QUESTION ) ).equals( "" ) ||
                request.getParameter( PARAMETER_CONTENT_HTML ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        QuestionAnswerHome.create( questionAnswer, getPlugin(  ) );

        // If the operation is successfull, redirect towards the list of question/answer couples
        return JSP_QUESTION_ANSWER_LIST;
    }

    /**
     * Returns the QuestionAnswer modification
     *
     * @param request The Http request
     * @return The Html template
     */
    public String getModifyQuestionAnswer( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_QUESTION_ANSWER );

        int nIdQuestionAnswer = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_ID ) );

        QuestionAnswer questionAnswer = new QuestionAnswer(  );
        questionAnswer = QuestionAnswerHome.findByPrimaryKey( nIdQuestionAnswer, getPlugin(  ) );

        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, AdminUserService.getLocale( request ).getLanguage(  ) );
        model.put( MARK_HTML_CONTENT, questionAnswer.getAnswer(  ) );

        ReferenceList listSubject = SubjectHome.findSubject( getPlugin(  ) );
        model.put( MARK_SUBJECT_LIST, listSubject );

        questionAnswer.setAnswer( questionAnswer.getAnswer(  ) );

        if ( questionAnswer.isEnabled(  ) )
        {
            model.put( MARK_CHECKED, PROPERTY_CHECK );
        }
        else
        {
            model.put( MARK_CHECKED, PROPERTY_NULL );
        }

        model.put( MARK_QUESTION_ANSWER, questionAnswer );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_QUESTION_ANSWER, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes QuestionAnswer modification
     * @param request The Http request
     * @return The URL to redirect to
     */
    public String doModifyQuestionAnswer( HttpServletRequest request )
    {
        int nIdQuestionAnswer = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_ID ) );
        int nIdSubject = Integer.parseInt( request.getParameter( PARAMETER_SUBJECT_ID ) );
        String strQuestion = request.getParameter( PARAMETER_QUESTION );
        String strAnswer = request.getParameter( PARAMETER_CONTENT_HTML );

        int nStatus = 0;

        if ( request.getParameter( PARAMETER_STATUS ) != null )
        {
            nStatus = 1;
        }

        QuestionAnswer questionAnswer = new QuestionAnswer(  );
        questionAnswer.setIdQuestionAnswer( nIdQuestionAnswer );
        questionAnswer.setIdSubject( nIdSubject );
        questionAnswer.setQuestion( strQuestion );
        questionAnswer.setAnswer( strAnswer );
        questionAnswer.setStatus( nStatus );

        // Mandatory field
        if ( request.getParameter( PARAMETER_SUBJECT_ID ).equals( "" ) ||
                request.getParameter( ( PARAMETER_QUESTION ) ).equals( "" ) ||
                request.getParameter( PARAMETER_CONTENT_HTML ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        QuestionAnswerHome.update( questionAnswer, getPlugin(  ) );

        // If the operation is successfull, redirect towards the list of question/answer couples
        return JSP_QUESTION_ANSWER_LIST;
    }

    /**
     * Returns the subject removal form
     * @param request The Http request
     * @return The Html template
     */
    public String getConfirmRemoveQuestionAnswer( HttpServletRequest request )
    {
        int strIdQuestionAnswer = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_ID ) );

        UrlItem url = new UrlItem( JSP_DO_REMOVE_QUESTION_ANSWER );
        url.addParameter( PARAMETER_QUESTION_ID, strIdQuestionAnswer );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_QUESTION_ANSWER, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Processes QuestionAnswer removal
     * @param request The Http request
     * @return The URL to redirect to
     */
    public String doRemoveQuestionAnswer( HttpServletRequest request )
    {
        String strIdQuestionAnswer = request.getParameter( PARAMETER_QUESTION_ID );
        int nIdQuestionAnswer = Integer.parseInt( strIdQuestionAnswer );
        QuestionAnswerHome.remove( nIdQuestionAnswer, getPlugin(  ) );

        // If the operation is successfull, redirect towards the list of question/answer couples
        return JSP_QUESTION_ANSWER_LIST;
    }

    /**
     * Returns the visitor question modification form
     * @param request The Http request
     * @return The Html template
     */
    public String getModifyVisitorQuestion( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_VISITOR_QUESTION );

        int nIdVisitorQuestion = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_ID ) );
        VisitorQuestion visitorQuestion = VisitorQuestionHome.findByPrimaryKey( nIdVisitorQuestion, getPlugin(  ) );
        model.put( MARK_VISITOR_QUESTION, visitorQuestion );

        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, AdminUserService.getLocale( request ).getLanguage(  ) );
        model.put( MARK_ANSWER, visitorQuestion.getAnswer(  ) );

        HtmlTemplate template;

        if ( visitorQuestion.getAnswer(  ).equals( "" ) )
        {
            template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_VISITOR_QUESTION, getLocale(  ), model );
        }
        else
        {
            // The question has already been treated
            AdminUser helpdeskUser = AdminUserHome.findByPrimaryKey( visitorQuestion.getIdUser(  ) );
            List<QuestionAnswer> listQuestionAnswer = QuestionAnswerHome.findByKeywords( visitorQuestion.getAnswer(  ),
                    getPlugin(  ) );
            model.put( MARK_HELPDESK_USER, helpdeskUser );
            model.put( MARK_QUESTION_ANSWER_LIST, listQuestionAnswer );
            template = AppTemplateService.getTemplate( TEMPLATE_VIEW_VISITOR_QUESTION, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the answer selection
     * @param request The Http request
     * @return The Html template
     */
    public String getAnswerSelection( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        List<Subject> listSubject = SubjectHome.findAll( getPlugin(  ) );
        model.put( MARK_SUBJECT_LIST, listSubject );
        model.put( MARK_QUESTION_LIST, QuestionAnswerHome.findAll( getPlugin(  ) ) ); 
        
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ANSWER_SELECTION, getLocale(  ), model );
        return template.getHtml(  );
    }

    /**
     *  Fetches a visitor's list of questions
     * @param request The HttpRequest
     * @return The result in a string form
     */
    public String getVisitorQuestionList( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        setPageTitleProperty( PROPERTY_PAGE_TITLE_VISITOR_QUESTION_LIST );

        model.put( MARK_VISITOR_QUESTION_LIST, "" );
        model.put( MARK_QUESTIONS_NUMBER, 0 );

        int nQuestion = 0;
        List<VisitorQuestion> listQuestion = new ArrayList(  );

        List<QuestionType> listQuestionType = QuestionTypeHome.selectQuestionTypeList( getPlugin(  ) );
        
        for( QuestionType questionType : listQuestionType )
        {            
            questionType = QuestionTypeHome.findByPrimaryKey( questionType.getIdQuestionType(  ), getPlugin(  ) );

            String strQuestionTypeId = Integer.toString( questionType.getIdQuestionType(  ) );

            if ( RBACService.isAuthorized( QuestionType.RESOURCE_TYPE, strQuestionTypeId,
                        QuestionTypeResourceIdService.PERMISSION_MANAGE, getUser(  ) ) )
            {
                List<VisitorQuestion> listVisitorQuestion = VisitorQuestionHome.findByType( Integer.parseInt( 
                            strQuestionTypeId ), getPlugin(  ) );
                listQuestion.addAll( listVisitorQuestion );

                for( VisitorQuestion visitorQuestion : listVisitorQuestion )
                { 

                    if ( visitorQuestion.getAnswer(  ).equals( "" ) )
                    {
                        nQuestion = nQuestion + 1;
                    }
                }

                model.put( MARK_QUESTIONS_NUMBER, nQuestion );
            }
            else
            {
                List<QuestionTopic> listQuestionTopic = QuestionTopicHome.getQuestionTopicList( questionType.getIdQuestionType(  ),
                        getPlugin(  ) );

                for( QuestionTopic questionTopic : listQuestionTopic)
                { 
                    questionTopic = QuestionTopicHome.findByPrimaryKey( questionTopic.getIdQuestionTopic(  ),
                            getPlugin(  ) );

                    String strQuestionTopicId = Integer.toString( questionTopic.getIdQuestionTopic(  ) );

                    if ( RBACService.isAuthorized( QuestionTopic.RESOURCE_TYPE, strQuestionTopicId,
                                QuestionTopicResourceIdService.PERMISSION_MANAGE, getUser(  ) ) )
                    {
                        List<VisitorQuestion> listQuestionVisitor = VisitorQuestionHome.findByTopics( questionTopic.getIdQuestionTopic(  ),
                                getPlugin(  ) );
                        listQuestion.addAll( listQuestionVisitor );
                        
                        for( VisitorQuestion visitorQuestion : listQuestionVisitor)
                        { 
                            if ( visitorQuestion.getAnswer(  ).equals( "" ) )
                            {
                                nQuestion = nQuestion + 1;
                            }
                        }

                        model.put( MARK_QUESTIONS_NUMBER, nQuestion );
                    }
                }
            }
        }

        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_STYLES_PER_PAGE, 10 );
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        Paginator paginator = new Paginator( listQuestion, _nItemsPerPage, JSP_QUESTION_VISITOR_LIST,
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_VISITOR_QUESTION_LIST, paginator.getPageItems(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_VISITOR_QUESTION_LIST, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes visitor question modification
     * @param request The Http request
     * @return The URL to redirect to
     */
    public String doModifyVisitorQuestion( HttpServletRequest request )
    {
        String strIdVisitorQuestion = request.getParameter( PARAMETER_QUESTION_ID );
        int nIdVisitorQuestion = Integer.parseInt( strIdVisitorQuestion );
        String strLastname = request.getParameter( PARAMETER_LAST_NAME );
        String strFirstname = request.getParameter( PARAMETER_FIRST_NAME );
        String strEmail = request.getParameter( PARAMETER_EMAIL );
        String strQuestion = request.getParameter( PARAMETER_QUESTION );
        String strAnswer = request.getParameter( PARAMETER_ANSWER );
        AdminUser user = getUser(  );

        VisitorQuestion visitorQuestion = new VisitorQuestion(  );
        visitorQuestion.setIdVisitorQuestion( nIdVisitorQuestion );
        visitorQuestion.setLastname( strLastname );
        visitorQuestion.setFirstname( strFirstname );
        visitorQuestion.setEmail( strEmail );
        visitorQuestion.setQuestion( strQuestion );
        visitorQuestion.setAnswer( strAnswer );
        visitorQuestion.setIdUser( user.getUserId(  ) );

        // Mandatory field
        if ( request.getParameter( PARAMETER_ANSWER ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        VisitorQuestionHome.update( visitorQuestion, getPlugin(  ) );

        String strPortalUrl = AppPathService.getBaseUrl( request );

        HashMap model = new HashMap(  );

        model.put( MARK_QUESTION, visitorQuestion.getQuestion(  ) );
        model.put( MARK_ANSWER, visitorQuestion.getAnswer(  ) );
        model.put( MARK_PORTAL_URL, strPortalUrl );

        String strPortal = AppPropertiesService.getProperty( PROPERTY_PORTAL_NAME );
        String strSubject = AppPropertiesService.getProperty( PROPERTY_MAIL_SUBJECT );
        String strEmailWebmaster = AppPropertiesService.getProperty( PROPERTY_MAIL_HELPDESK_SENDER );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_SEND_ANSWER, request.getLocale(  ), model );

        String strMessageText = template.getHtml(  );

        MailService.sendMail( visitorQuestion.getEmail(  ), strPortal, strEmailWebmaster, strSubject, strMessageText );

        if ( request.getParameter( PARAMETER_ADD_QUESTION_ANSWER ) == null )
        {
            // If the operation is successfull, redirect towards the list of visitor's questions
            return JSP_VISITOR_QUESTION_LIST;
        }
        else
        {
            return JSP_CREATE_QUESTION_ANSWER + "?question_id=" + nIdVisitorQuestion;
        }
    }

    /**
     * Returns the list of archived questions
     * @param request The Http request
     * @return The Html template
     */
    public String getArchivedQuestionList( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        setPageTitleProperty( PROPERTY_PAGE_TITLE_ARCHIVED_QUESTION_LIST );

        int nQuestionsNumber = 0;
        AdminUser user = getUser(  );
        int nIdUser = user.getUserId(  );

        List<VisitorQuestion> listVisitorQuestionArchived = VisitorQuestionHome.findAll( getPlugin(  ) );
        for( VisitorQuestion visitorQuestion : listVisitorQuestionArchived)
        {
            _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_STYLES_PER_PAGE, 10 );
            _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
            _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                    _nDefaultItemsPerPage );

            Paginator paginator = new Paginator( listVisitorQuestionArchived, _nItemsPerPage,
                    JSP_QUESTION_ARCHIVED_LIST, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

            model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
            model.put( MARK_PAGINATOR, paginator );
            model.put( MARK_QUESTION_LIST, paginator.getPageItems(  ) );

            if ( ( visitorQuestion.getIdUser(  ) == nIdUser ) && ( !visitorQuestion.getAnswer(  ).equals( "" ) ) )
            {
                //model.put( MARK_QUESTION_LIST, ListVisitorQuestionArchived );                       
                nQuestionsNumber++;
            }
        }

        model.put( MARK_QUESTIONS_NUMBER, nQuestionsNumber );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_ARCHIVED_QUESTION_LIST, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes visitor question removal
     * @param request The Http request
     * @return The URL to redirect to
     */
    public String doRemoveVisitorQuestion( HttpServletRequest request )
    {
        int nIdQuestionAnswer = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_ID ) );
        VisitorQuestionHome.remove( nIdQuestionAnswer, getPlugin(  ) );

        // If the operation is successfull, redirect towards the list of question/answer couples
        return JSP_ARCHIVED_QUESTION_LIST;
    }
}
