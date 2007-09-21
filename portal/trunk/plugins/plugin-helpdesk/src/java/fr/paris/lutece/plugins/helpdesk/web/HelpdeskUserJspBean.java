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

import fr.paris.lutece.plugins.helpdesk.business.QuestionTopic;
import fr.paris.lutece.plugins.helpdesk.business.QuestionTopicHome;
import fr.paris.lutece.plugins.helpdesk.business.QuestionType;
import fr.paris.lutece.plugins.helpdesk.business.QuestionTypeHome;
import fr.paris.lutece.plugins.helpdesk.business.VisitorQuestion;
import fr.paris.lutece.plugins.helpdesk.business.VisitorQuestionHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 *  This class is used for back office management of helpdesk topics.
 */
public class HelpdeskUserJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_MANAGE_USER_HELPDESK = "HELPDESK_USER_MANAGEMENT";

    //markers
    private static final String MARK_QUESTION_TYPE = "question_type";
    private static final String MARK_QUESTION_TYPE_ID = "id_question_type";
    private static final String MARK_QUESTION_TYPE_LIST = "helpdesk_question_type_list";
    private static final String MARK_QUESTION_TOPIC_LIST = "question_topic_list";
    private static final String MARK_QUESTION_TOPIC = "question_topic";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";

    //parameters
    private static final String PARAMETER_QUESTION_TYPE = "question_type";
    private static final String PARAMETER_QUESTION_TYPE_ID = "question_type_id";
    private static final String PARAMETER_QUESTION_TOPIC = "question_topic";
    private static final String PARAMETER_QUESTION_TOPIC_ID = "question_topic_id";

    //properties
    private static final String PROPERTY_PAGE_TITLE_CREATE_QUESTION_TYPE = "helpdesk.create_question_type.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_QUESTION_TYPE = "helpdesk.modify_question_type.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_QUESTION_TYPE_LIST = "helpdesk.question_type_list.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_QUESTION_TOPIC_LIST = "helpdesk.question_topic_list.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_QUESTION_TOPIC = "helpdesk.create_question_topic.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_QUESTION_TOPIC = "helpdesk.modify_question_topic.pageTitle";
    private static final String PROPERTY_STYLES_PER_PAGE = "paginator.style.itemsPerPage";

    //templates
    private static final String TEMPLATE_QUESTION_TYPE_LIST = "admin/plugins/helpdesk/question_type_list.html";
    private static final String TEMPLATE_CREATE_QUESTION_TYPE = "admin/plugins/helpdesk/create_question_type.html";
    private static final String TEMPLATE_MODIFY_QUESTION_TYPE = "admin/plugins/helpdesk/modify_question_type.html";
    private static final String TEMPLATE_QUESTION_TOPIC_LIST = "admin/plugins/helpdesk/question_topic_list.html";
    private static final String TEMPLATE_CREATE_QUESTION_TOPIC = "admin/plugins/helpdesk/create_question_topic.html";
    private static final String TEMPLATE_MODIFY_QUESTION_TOPIC = "admin/plugins/helpdesk/modify_question_topic.html";

    //jsp
    private static final String JSP_MANAGE_HELPDESK_LIST = "ManageHelpdeskAdmin.jsp";
    private static final String JSP_QUESTION_TOPICS_LIST = "QuestionTopicList.jsp";
    private static final String JSP_QUESTION_TOPIC_LIST = "jsp/admin/plugins/helpdesk/QuestionTopicList.jsp";
    private static final String JSP_DO_REMOVE_QUESTION_TYPE = "jsp/admin/plugins/helpdesk/DoRemoveQuestionType.jsp";
    private static final String JSP_DO_REMOVE_QUESTION_TOPIC = "jsp/admin/plugins/helpdesk/DoRemoveQuestionTopic.jsp";

    //messages
    private static final String MESSAGE_CONFIRM_DELETE_QUESTION_TYPE = "helpdesk.message.confirmDeleteQuestionType";
    private static final String MESSAGE_CONFIRM_DELETE_QUESTION_TOPIC = "helpdesk.message.confirmDeleteQuestionTopic";
    private static final String MESSAGE_CANNOT_DELETE_QUESTION_TYPE = "helpdesk.message.cannotDeleteQuestionType";
    private static final String MESSAGE_CANNOT_DELETE_QUESTION_TOPIC = "helpdesk.message.cannotDeleteQuestionTopic";
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;

    /** Creates a new instance of HelpdeskUserJspBean */
    public HelpdeskUserJspBean(  )
    {
    }

    /**
     * Returns the help desk administration menu - the list of topics
     * @param request The Http request
     * @return The Html template
     */
    public String getManageHelpdeskAdmin( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );
        setPageTitleProperty( PROPERTY_PAGE_TITLE_QUESTION_TYPE_LIST );

        List<QuestionType> listQuestionType = QuestionTypeHome.selectQuestionTypeList( getPlugin(  ) );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_STYLES_PER_PAGE, 10 );
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        Paginator paginator = new Paginator( listQuestionType, _nItemsPerPage, getHomeUrl( request ),
                Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_QUESTION_TYPE_LIST, paginator.getPageItems(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_QUESTION_TYPE_LIST, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the subject removal form
     * @param request The Http request
     * @return The Html template
     */
    public String getConfirmRemoveQuestionType( HttpServletRequest request )
    {
        int nIdQuestionType = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_TYPE_ID ) );
        List<VisitorQuestion> listQuestion = QuestionTypeHome.getQuestions( nIdQuestionType, getPlugin(  ) );

        if ( listQuestion.size(  ) != 0 )
        {
            UrlItem url = new UrlItem( getHomeUrl( request ) );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_QUESTION_TYPE, url.getUrl(  ),
                AdminMessage.TYPE_STOP );
        }
        else
        {
            UrlItem url = new UrlItem( JSP_DO_REMOVE_QUESTION_TYPE );
            url.addParameter( PARAMETER_QUESTION_TYPE_ID, nIdQuestionType );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_QUESTION_TYPE, url.getUrl(  ),
                AdminMessage.TYPE_CONFIRMATION );
        }
    }

    /**
     * Processes question type removal
     * @param request The Http request
     * @return The URL to redirect to
     */
    public String doRemoveQuestionType( HttpServletRequest request )
    {
        int nIdQuestionType = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_TYPE_ID ) );
        List<QuestionTopic> listQuestionTopic = QuestionTopicHome.getQuestionTopicList( nIdQuestionType, getPlugin(  ) );

        for( QuestionTopic questionTopic :listQuestionTopic )
        {
             QuestionTopicHome.remove( questionTopic.getIdQuestionTopic(  ), getPlugin(  ) );
        }

        QuestionTypeHome.remove( nIdQuestionType, getPlugin(  ) );

        // If the operation is successfull, redirect towards the list of question types
        return JSP_MANAGE_HELPDESK_LIST;
    }

    /**
     * Returns the question type creation form
         * @param request The Http request
     * @return The Html template
     */
    public String getCreateQuestionType( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_QUESTION_TYPE );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_QUESTION_TYPE, getLocale(  ) );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the question type modification form
         * @param request The Http request
     * @return The Html template
     */
    public String getModifyQuestionType( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_QUESTION_TYPE );

        String strIdQuestionType = request.getParameter( PARAMETER_QUESTION_TYPE_ID );
        int nIdQuestionType = Integer.parseInt( strIdQuestionType );
        QuestionType questionType = QuestionTypeHome.findByPrimaryKey( nIdQuestionType, getPlugin(  ) );
        model.put( MARK_QUESTION_TYPE, questionType );

        List<QuestionTopic> listQuestionTopic = QuestionTopicHome.getQuestionTopicList( nIdQuestionType, getPlugin(  ) );
        model.put( MARK_QUESTION_TOPIC_LIST, listQuestionTopic );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_QUESTION_TYPE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes the creation of a question type
     * @param request The Http request
     * @return The URL to redirect to
     */
    public String doCreateQuestionType( HttpServletRequest request )
    {
        String strQuestionType = request.getParameter( PARAMETER_QUESTION_TYPE );
        QuestionType questionType = new QuestionType(  );
        questionType.setQuestionType( strQuestionType );

        // Mandatory field
        if ( strQuestionType.trim(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        QuestionTypeHome.create( questionType, getPlugin(  ) );

        // If the operation is successfull, redirect towards the list of question types
        return JSP_MANAGE_HELPDESK_LIST;
    }

    /**
     * Processes the modification of a question type
     * @param request The Http request
     * @return The URL to redirect to
     */
    public String doModifyQuestionType( HttpServletRequest request )
    {
        String strQuestionType = request.getParameter( PARAMETER_QUESTION_TYPE );
        String strIdQuestionType = request.getParameter( PARAMETER_QUESTION_TYPE_ID );
        int nIdQuestionType = Integer.parseInt( strIdQuestionType );
        QuestionType questionType = new QuestionType(  );
        questionType.setIdQuestionType( nIdQuestionType );
        questionType.setQuestionType( strQuestionType );

        // Mandatory field
        if ( request.getParameter( PARAMETER_QUESTION_TYPE ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        QuestionTypeHome.update( questionType, getPlugin(  ) );

        // If the operation is successfull, redirect towards the list of question types
        return JSP_MANAGE_HELPDESK_LIST;
    }

    /**
     * Returns the list of question topics
     * @param request The Http request
     * @return The Html template
     */
    public String getQuestionTopicList( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        setPageTitleProperty( PROPERTY_PAGE_TITLE_QUESTION_TOPIC_LIST );

        int nIdQuestionType = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_TYPE_ID ) );
        QuestionType questionType = QuestionTypeHome.findByPrimaryKey( nIdQuestionType, getPlugin(  ) );

        List<QuestionTopic> listQuestionTopic = QuestionTopicHome.getQuestionTopicList( nIdQuestionType, getPlugin(  ) );

        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_STYLES_PER_PAGE, 10 );
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        Paginator paginator = new Paginator( listQuestionTopic, _nItemsPerPage,
                JSP_QUESTION_TOPIC_LIST + "?question_type_id=" + nIdQuestionType, Paginator.PARAMETER_PAGE_INDEX,
                _strCurrentPageIndex );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_QUESTION_TOPIC_LIST, paginator.getPageItems(  ) );

        model.put( MARK_QUESTION_TYPE, questionType );
        model.put( MARK_QUESTION_TYPE_ID, nIdQuestionType );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_QUESTION_TOPIC_LIST, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the question topic creation form
     * @param request The Http request
     * @return The Html template
     */
    public String getCreateQuestionTopic( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_QUESTION_TOPIC );

        HashMap model = new HashMap(  );

        int nIdQuestionType = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_TYPE_ID ) );

        model.put( MARK_QUESTION_TYPE_ID, nIdQuestionType );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_QUESTION_TOPIC, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes the creation of a question topic
     * @param request The Http request
     * @return The URL to redirect to
     */
    public String doCreateQuestionTopic( HttpServletRequest request )
    {
        String strQuestionTopic = request.getParameter( PARAMETER_QUESTION_TOPIC );
        int nIdQuestionType = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_TYPE_ID ) );

        // Mandatory field
        if ( strQuestionTopic.trim(  ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        QuestionTopic questionTopic = new QuestionTopic(  );

        questionTopic.setQuestionTopic( strQuestionTopic );
        questionTopic.setIdQuestionType( nIdQuestionType );

        QuestionTopicHome.create( questionTopic, getPlugin(  ) );

        // If the operation is successfull, redirect towards the list of question topic
        return JSP_QUESTION_TOPICS_LIST + "?question_type_id=" + nIdQuestionType;
    }

    /**
     * Returns the question type modification form
     * @param request The Http request
     * @return The Html template
     */
    public String getModifyQuestionTopic( HttpServletRequest request )
    {
        HashMap model = new HashMap(  );

        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY_QUESTION_TOPIC );

        int nIdQuestionTopic = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_TOPIC_ID ) );

        QuestionTopic questionTopic = QuestionTopicHome.findByPrimaryKey( nIdQuestionTopic, getPlugin(  ) );
        QuestionType questionType = QuestionTypeHome.findByPrimaryKey( questionTopic.getIdQuestionType(  ),
                getPlugin(  ) );

        model.put( MARK_QUESTION_TOPIC, questionTopic );
        model.put( MARK_QUESTION_TYPE, questionType );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_QUESTION_TOPIC, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Processes the modification of a question type
     * @param request The Http request
     * @return The URL to redirect to
     */
    public String doModifyQuestionTopic( HttpServletRequest request )
    {
        String strQuestionTopic = request.getParameter( PARAMETER_QUESTION_TOPIC );
        int nIdQuestionType = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_TYPE_ID ) );
        int nIdQuestionTopic = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_TOPIC_ID ) );

        QuestionTopic questionTopic = new QuestionTopic(  );
        questionTopic.setIdQuestionTopic( nIdQuestionTopic );
        questionTopic.setQuestionTopic( strQuestionTopic );
        questionTopic.setIdQuestionType( nIdQuestionType );

        // Mandatory field
        if ( request.getParameter( PARAMETER_QUESTION_TOPIC ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        QuestionTopicHome.update( questionTopic, getPlugin(  ) );

        // If the operation is successfull, redirect towards the list of question types
        return JSP_QUESTION_TOPICS_LIST + "?question_type_id=" + nIdQuestionType;
    }

    /**
     * Returns the subject removal form
     * @param request The Http request
     * @return The Html template
     */
    public String getConfirmRemoveQuestionTopic( HttpServletRequest request )
    {
        int nIdQuestionTopic = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_TOPIC_ID ) );
        int nIdQuestionType = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_TYPE_ID ) );
        List<VisitorQuestion> listQuestion = VisitorQuestionHome.findByTopics( nIdQuestionTopic, getPlugin(  ) );

        if ( listQuestion.size(  ) != 0 )
        {
            UrlItem url = new UrlItem( JSP_QUESTION_TOPIC_LIST );
            url.addParameter( PARAMETER_QUESTION_TOPIC_ID, nIdQuestionTopic );
            url.addParameter( PARAMETER_QUESTION_TYPE_ID, nIdQuestionType );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CANNOT_DELETE_QUESTION_TOPIC, url.getUrl(  ),
                AdminMessage.TYPE_STOP );
        }
        else
        {
            UrlItem url = new UrlItem( JSP_DO_REMOVE_QUESTION_TOPIC );
            url.addParameter( PARAMETER_QUESTION_TOPIC_ID, nIdQuestionTopic );
            url.addParameter( PARAMETER_QUESTION_TYPE_ID, nIdQuestionType );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_QUESTION_TOPIC, url.getUrl(  ),
                AdminMessage.TYPE_CONFIRMATION );
        }
    }

    /**
     * Processes question type removal
     * @param request The Http request
     * @return The URL to redirect to
     */
    public String doRemoveQuestionTopic( HttpServletRequest request )
    {
        int nIdQuestionTopic = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_TOPIC_ID ) );
        int nIdQuestionType = Integer.parseInt( request.getParameter( PARAMETER_QUESTION_TYPE_ID ) );

        QuestionTopicHome.remove( nIdQuestionTopic, getPlugin(  ) );

        // If the operation is successfull, redirect towards the list of question types
        return JSP_QUESTION_TOPICS_LIST + "?question_type_id=" + nIdQuestionType;
    }
}
