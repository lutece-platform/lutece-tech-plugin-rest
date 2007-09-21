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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.poll.business.Poll;
import fr.paris.lutece.plugins.poll.business.PollChoice;
import fr.paris.lutece.plugins.poll.business.PollChoiceHome;
import fr.paris.lutece.plugins.poll.business.PollHome;
import fr.paris.lutece.plugins.poll.business.PollQuestion;
import fr.paris.lutece.plugins.poll.business.PollQuestionHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage poll features ( manage,
 * create, modify, remove)
 */
public class PollJspBean extends PluginAdminPageJspBean {

	// //////////////////////////////////////////////////////////////////////////
	// Constants

	// Right
	public static final String RIGHT_MANAGE_POLL = "POLL_MANAGEMENT";

	// Properties for page titles
	private static final String PROPERTY_PAGE_TITLE_MANAGE_POLLS = "poll.manage_polls.pageTitle";

	private static final String PROPERTY_PAGE_TITLE_CREATE_POLL = "poll.create_poll.pageTitle";

	private static final String PROPERTY_PAGE_TITLE_MODIFY_POLL = "poll.modify_poll.pageTitle";

	private static final String PROPERTY_PAGE_TITLE_CREATE_QUESTION = "poll.create_question.pageTitle";

	private static final String PROPERTY_PAGE_TITLE_MODIFY_QUESTION = "poll.modify_question.pageTitle";

	private static final String PROPERTY_PAGE_TITLE_CREATE_CHOICE = "poll.create_choice.pageTitle";

	private static final String PROPERTY_PAGE_TITLE_MODIFY_CHOICE = "poll.modify_choice.pageTitle";

	private static final String PROPERTY_PAGE_TITLE_VIEW_RESULTS = "poll.view_results.pageTitle";

	// Properties from poll.properties
	private static final String PROPERTY_CHOICES_MAX_COUNT = "poll.choices.maxCount";

	private static final String PROPERTY_CHOICES_MIN_COUNT = "poll.choices.minCount";

	// Markers
	private static final String MARK_POLLS_LIST = "polls_list";

	private static final String MARK_WORKGROUPS_LIST = "workgroups_list";

	private static final String MARK_POLL = "poll";

	private static final String MARK_QUESTION = "question";

	private static final String MARK_QUESTIONS_LIST = "questions_list";

	private static final String MARK_CHOICE = "choice";

	// Templates files path
	private static final String TEMPLATE_MANAGE_POLLS = "admin/plugins/poll/manage_polls.html";

	private static final String TEMPLATE_CREATE_POLL = "admin/plugins/poll/create_poll.html";

	private static final String TEMPLATE_MODIFY_POLL = "admin/plugins/poll/modify_poll.html";

	private static final String TEMPLATE_CREATE_QUESTION = "admin/plugins/poll/create_question.html";

	private static final String TEMPLATE_MODIFY_QUESTION = "admin/plugins/poll/modify_question.html";

	private static final String TEMPLATE_CREATE_CHOICE = "admin/plugins/poll/create_choice.html";

	private static final String TEMPLATE_MODIFY_CHOICE = "admin/plugins/poll/modify_choice.html";

	private static final String TEMPLATE_VIEW_SCORES = "admin/plugins/poll/view_scores.html";

	// Jsp Definition
	private static final String JSP_DO_REMOVE_POLL = "jsp/admin/plugins/poll/DoRemovePoll.jsp";

	private static final String JSP_DO_REMOVE_QUESTION = "jsp/admin/plugins/poll/DoRemoveQuestion.jsp";

	private static final String JSP_DO_REMOVE_CHOICE = "jsp/admin/plugins/poll/DoRemoveChoice.jsp";

	private static final String JSP_MODIFY_POLL = "ModifyPoll.jsp";

	private static final String JSP_MODIFY_QUESTION = "ModifyQuestion.jsp";

	// Message keys
	private static final String MESSAGE_CHOICES_MIN_COUNT = "poll.message.mandatoryChoicesCount";

	private static final String MESSAGE_CHOICES_MAX_COUNT = "poll.message.choicesMaxCount";

	private static final String MESSAGE_CONFIRM_REMOVE_POLL = "poll.message.confirmRemovePoll";

	private static final String MESSAGE_CONFIRM_REMOVE_QUESTION = "poll.message.confirmRemoveQuestion";

	// Parameters
	private static final String PARAMETER_POLL_ID = "poll_id";

	private static final String PARAMETER_POLL_NAME = "poll_name";

	private static final String PARAMETER_QUESTION_LABEL = "question_label";

	private static final String PARAMETER_QUESTION_ID = "question_id";

	private static final String PARAMETER_CHOICE_LABEL = "choice_label";

	private static final String PARAMETER_CHOICE_ID = "choice_id";

	private static final String PARAMETER_PLUGIN_NAME = "plugin_name";

	private static final String PARAMETER_WORKGROUP = "workgroup";

	private Plugin _plugin;

	/**
	 * Returns poll management form
	 * 
	 * @param request
	 *            The Http request
	 * @return Html form
	 */
	public String getManagePolls(HttpServletRequest request) {
		setPageTitleProperty(PROPERTY_PAGE_TITLE_MANAGE_POLLS);
		if (_plugin == null) {
			String strPluginName = request.getParameter(PARAMETER_PLUGIN_NAME);
			_plugin = PluginService.getPlugin(strPluginName);
		}

		HashMap model = new HashMap();
		List listPoll = PollHome.findAll(_plugin);
		// TODO remove RBAC control
		// listPoll = (List) RBACService.getAuthorizedCollection( listPoll,
		// PollResourceIdService.PERMISSION_MANAGE, getUser() );

		listPoll = (List) AdminWorkgroupService.getAuthorizedCollection(
				listPoll, getUser());
		model.put(MARK_POLLS_LIST, listPoll);

		HtmlTemplate template = AppTemplateService.getTemplate(
				TEMPLATE_MANAGE_POLLS, getLocale(), model);

		return getAdminPage(template.getHtml());
	}

	/**
	 * Returns the Poll creation form
	 * 
	 * @param request
	 *            The Http request
	 * @return Html creation form
	 */
	public String getCreatePoll(HttpServletRequest request) {
		setPageTitleProperty(PROPERTY_PAGE_TITLE_CREATE_POLL);
		HashMap model = new HashMap();
		ReferenceList ref=null;
		try {
			AdminUser adminUser=getUser();
			Locale locale=getLocale(); 
			ref = AdminWorkgroupService.getUserWorkgroups(adminUser,
					locale);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.put(MARK_WORKGROUPS_LIST, ref);
		HtmlTemplate template = AppTemplateService.getTemplate(
				TEMPLATE_CREATE_POLL, getLocale(), model);

		return getAdminPage(template.getHtml());
	}

	/**
	 * Process poll creation
	 * 
	 * @param request
	 *            The Http request
	 * @return URL
	 */
	public String doCreatePoll(HttpServletRequest request) {
		String strName = request.getParameter(PARAMETER_POLL_NAME);
		String strWorkgroup = request.getParameter(PARAMETER_WORKGROUP);

		// Mandatory field
		if (strName.equals("")) {
			return AdminMessageService.getMessageUrl(request,
					Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP);
		}

		Poll poll = new Poll();
		poll.setName(strName);
		poll.setStatus(false);
		poll.setWorkgroup(strWorkgroup);
		PollHome.create(poll, _plugin);

		return getHomeUrl(request);
	}

	/**
	 * Returns the confirmation to remove the poll
	 * 
	 * @param request
	 *            The Http request
	 * @return the confirmation page
	 */
	public String getConfirmRemovePoll(HttpServletRequest request) {
		UrlItem url = new UrlItem(JSP_DO_REMOVE_POLL);
		url.addParameter(PARAMETER_POLL_ID, Integer.parseInt(request
				.getParameter(PARAMETER_POLL_ID)));

		return AdminMessageService.getMessageUrl(request,
				MESSAGE_CONFIRM_REMOVE_POLL, url.getUrl(),
				AdminMessage.TYPE_CONFIRMATION);
	}

	/**
	 * Remove a poll
	 * 
	 * @param request
	 *            The Http request
	 * @return Html form
	 */
	public String doRemovePoll(HttpServletRequest request) {
		Poll poll = PollHome.findByPrimaryKey(Integer.parseInt(request
				.getParameter(PARAMETER_POLL_ID)), _plugin);
		List<PollQuestion> listQuestions = poll.getQuestions();
		for (PollQuestion question : listQuestions) {
			List<PollChoice> listChoices = question.getChoices();
			for (PollChoice choice : listChoices) {
				PollChoiceHome.remove(choice.getId(), _plugin);
			}
			PollQuestionHome.remove(question.getId(), _plugin);
		}
		PollHome.remove(poll.getId(), _plugin);

		// Go to the parent page
		return getHomeUrl(request);
	}

	/**
	 * Enable a Poll
	 * 
	 * @param request
	 *            The Http request
	 * @return Html form
	 */
	public String doEnablePoll(HttpServletRequest request) {
		Poll poll = PollHome.findByPrimaryKey(Integer.parseInt(request
				.getParameter(PARAMETER_POLL_ID)), _plugin);

		// Check if the required choices count is reached
		List<PollQuestion> listQuestions = poll.getQuestions();
		int nChoicesMinCount = Integer.parseInt(AppPropertiesService
				.getProperty(PROPERTY_CHOICES_MIN_COUNT));
		for (PollQuestion question : listQuestions) {
			if (question.getChoices().size() < nChoicesMinCount) {
				return AdminMessageService.getMessageUrl(request,
						MESSAGE_CHOICES_MIN_COUNT, AdminMessage.TYPE_STOP);
			}
		}

		poll.setStatus(true);
		PollHome.update(poll, _plugin);

		return getHomeUrl(request);
	}

	/**
	 * Disable a Poll
	 * 
	 * @param request
	 *            The Http request
	 * @return Html form
	 */
	public String doDisablePoll(HttpServletRequest request) {
		Poll poll = PollHome.findByPrimaryKey(Integer.parseInt(request
				.getParameter(PARAMETER_POLL_ID)), _plugin);
		poll.setStatus(false);
		PollHome.update(poll, _plugin);

		return getHomeUrl(request);
	}

	/**
	 * Displays the poll scores page
	 * 
	 * @param request
	 *            The Http request
	 * @return The Html code to display the scores poll
	 */
	public String getViewResults(HttpServletRequest request) {
		setPageTitleProperty(PROPERTY_PAGE_TITLE_VIEW_RESULTS);

		HashMap model = new HashMap();
		model.put(MARK_POLL, PollHome.findByPrimaryKey(Integer.parseInt(request
				.getParameter(PARAMETER_POLL_ID)), _plugin));
		model.put(MARK_QUESTIONS_LIST, PollQuestionHome
				.findPollQuestionListByPoll(Integer.parseInt(request
						.getParameter(PARAMETER_POLL_ID)), _plugin));
		HtmlTemplate template = AppTemplateService.getTemplate(
				TEMPLATE_VIEW_SCORES, getLocale(), model);

		return getAdminPage(template.getHtml());
	}

	/**
	 * Returns the form for poll modification
	 * 
	 * @param request
	 *            The Http request
	 * @return Html form
	 */
	public String getModifyPoll(HttpServletRequest request) {
		setPageTitleProperty(PROPERTY_PAGE_TITLE_MODIFY_POLL);

		int nPollId = Integer.parseInt(request.getParameter(PARAMETER_POLL_ID));

		HashMap model = new HashMap();
		model.put(MARK_POLL, PollHome.findByPrimaryKey(nPollId, _plugin));
		model.put(MARK_WORKGROUPS_LIST, AdminWorkgroupService
				.getUserWorkgroups(getUser(), getLocale()));
		model.put(MARK_QUESTIONS_LIST, PollQuestionHome
				.findPollQuestionListByPoll(nPollId, _plugin));
		HtmlTemplate template = AppTemplateService.getTemplate(
				TEMPLATE_MODIFY_POLL, getLocale(), model);

		return getAdminPage(template.getHtml());

	}

	/**
	 * Process the Poll modifications
	 * 
	 * @param request
	 *            The Http request
	 * @return Html form
	 */
	public String doModifyPoll(HttpServletRequest request) {
		String strName = request.getParameter(PARAMETER_POLL_NAME);
		String strWorkgroup = request.getParameter(PARAMETER_WORKGROUP);

		// Mandatory field
		if (strName.equals("")) {
			return AdminMessageService.getMessageUrl(request,
					Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP);
		}

		Poll poll = PollHome.findByPrimaryKey(Integer.parseInt(request
				.getParameter(PARAMETER_POLL_ID)), _plugin);
		poll.setName(strName);
		poll.setWorkgroup(strWorkgroup);
		PollHome.update(poll, _plugin);

		return getHomeUrl(request);
	}

	/**
	 * Returns the Question creation form
	 * 
	 * @param request
	 *            The Http request
	 * @return Html creation form
	 */
	public String getCreateQuestion(HttpServletRequest request) {
		setPageTitleProperty(PROPERTY_PAGE_TITLE_CREATE_QUESTION);

		HashMap model = new HashMap();
		model.put(MARK_POLL, PollHome.findByPrimaryKey(Integer.parseInt(request
				.getParameter(PARAMETER_POLL_ID)), _plugin));

		HtmlTemplate template = AppTemplateService.getTemplate(
				TEMPLATE_CREATE_QUESTION, getLocale(), model);

		return getAdminPage(template.getHtml());
	}

	/**
	 * Process question creation
	 * 
	 * @param request
	 *            The Http request
	 * @return URL
	 */
	public String doCreateQuestion(HttpServletRequest request) {
		String strLabel = request.getParameter(PARAMETER_QUESTION_LABEL);
		int nPollId = Integer.parseInt(request.getParameter(PARAMETER_POLL_ID));

		// Mandatory field
		if (strLabel.equals("")) {
			return AdminMessageService.getMessageUrl(request,
					Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP);
		}

		PollQuestion question = new PollQuestion();
		question.setIdPoll(nPollId);
		question.setLabel(strLabel);
		PollQuestionHome.create(question, _plugin);

		UrlItem url = new UrlItem(JSP_MODIFY_POLL);
		url.addParameter(PARAMETER_POLL_ID, nPollId);

		return url.getUrl();

	}

	/**
	 * Returns the confirmation to remove the question
	 * 
	 * @param request
	 *            The Http request
	 * @return the confirmation page
	 */
	public String getConfirmRemoveQuestion(HttpServletRequest request) {
		UrlItem url = new UrlItem(JSP_DO_REMOVE_QUESTION);
		url.addParameter(PARAMETER_QUESTION_ID, Integer.parseInt(request
				.getParameter(PARAMETER_QUESTION_ID)));

		return AdminMessageService.getMessageUrl(request,
				MESSAGE_CONFIRM_REMOVE_QUESTION, url.getUrl(),
				AdminMessage.TYPE_CONFIRMATION);
	}

	/**
	 * Remove a question
	 * 
	 * @param request
	 *            The Http request
	 * @return URL
	 */
	public String doRemoveQuestion(HttpServletRequest request) {
		PollQuestion question = PollQuestionHome
				.findByPrimaryKey(Integer.parseInt(request
						.getParameter(PARAMETER_QUESTION_ID)), _plugin);
		List<PollChoice> listChoices = question.getChoices();
		for (PollChoice choice : listChoices) {
			PollChoiceHome.remove(choice.getId(), _plugin);
		}

		PollQuestionHome.remove(question.getId(), _plugin);

		UrlItem url = new UrlItem(JSP_MODIFY_POLL);
		url.addParameter(PARAMETER_POLL_ID, question.getIdPoll());

		return url.getUrl();
	}

	/**
	 * Returns the question modification form
	 * 
	 * @param request
	 *            The Http request
	 * @return Html form
	 */
	public String getModifyQuestion(HttpServletRequest request) {
		setPageTitleProperty(PROPERTY_PAGE_TITLE_MODIFY_QUESTION);

		HashMap model = new HashMap();
		model.put(MARK_QUESTION, PollQuestionHome
				.findByPrimaryKey(Integer.parseInt(request
						.getParameter(PARAMETER_QUESTION_ID)), _plugin));

		HtmlTemplate template = AppTemplateService.getTemplate(
				TEMPLATE_MODIFY_QUESTION, getLocale(), model);

		return getAdminPage(template.getHtml());
	}

	/**
	 * Process the modifications of a question
	 * 
	 * @param request
	 *            The Http request
	 * @return The Jsp URL of the process result
	 */
	public String doModifyQuestion(HttpServletRequest request) {
		String strLabel = request.getParameter(PARAMETER_QUESTION_LABEL);

		// Mandatory field
		if (strLabel.equals("")) {
			return AdminMessageService.getMessageUrl(request,
					Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP);
		}

		PollQuestion question = PollQuestionHome
				.findByPrimaryKey(Integer.parseInt(request
						.getParameter(PARAMETER_QUESTION_ID)), _plugin);
		question.setLabel(strLabel);
		PollQuestionHome.update(question, _plugin);

		UrlItem url = new UrlItem(JSP_MODIFY_POLL);
		url.addParameter(PARAMETER_POLL_ID, question.getIdPoll());

		return url.getUrl();
	}

	/**
	 * Returns the choice creation form
	 * 
	 * @param request
	 *            The Http request
	 * @return Html creation form
	 */
	public String getCreateChoice(HttpServletRequest request) {
		// Check if the maximum choices count is reached
		PollQuestion question = PollQuestionHome
				.findByPrimaryKey(Integer.parseInt(request
						.getParameter(PARAMETER_QUESTION_ID)), _plugin);

		setPageTitleProperty(PROPERTY_PAGE_TITLE_CREATE_CHOICE);

		HashMap model = new HashMap();
		model.put(MARK_QUESTION, question);
		HtmlTemplate template = AppTemplateService.getTemplate(
				TEMPLATE_CREATE_CHOICE, getLocale(), model);

		return getAdminPage(template.getHtml());
	}

	/**
	 * Process choice creation
	 * 
	 * @param request
	 *            The Http request
	 * @return URL
	 */
	public String doCreateChoice(HttpServletRequest request) {
		PollQuestion question = PollQuestionHome
				.findByPrimaryKey(Integer.parseInt(request
						.getParameter(PARAMETER_QUESTION_ID)), _plugin);

		if (question.getChoices().size() >= Integer
				.parseInt(AppPropertiesService
						.getProperty(PROPERTY_CHOICES_MAX_COUNT))) {
			return AdminMessageService.getMessageUrl(request,
					MESSAGE_CHOICES_MAX_COUNT, AdminMessage.TYPE_STOP);
		}

		String strLabel = request.getParameter(PARAMETER_CHOICE_LABEL);

		// Mandatory field
		if (strLabel.equals("")) {
			return AdminMessageService.getMessageUrl(request,
					Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP);
		}

		PollChoice choice = new PollChoice();
		choice.setQuestionId(question.getId());
		choice.setLabel(strLabel);
		PollChoiceHome.create(choice, _plugin);

		UrlItem url = new UrlItem(JSP_MODIFY_QUESTION);
		url.addParameter(PARAMETER_QUESTION_ID, question.getId());

		return url.getUrl();

	}

	/**
	 * Returns the choice modification form
	 * 
	 * @param request
	 *            The Http request
	 * @return Html form
	 */
	public String getModifyChoice(HttpServletRequest request) {
		setPageTitleProperty(PROPERTY_PAGE_TITLE_MODIFY_CHOICE);

		HashMap model = new HashMap();
		model.put(MARK_CHOICE, PollChoiceHome.findByPrimaryKey(Integer
				.parseInt(request.getParameter(PARAMETER_CHOICE_ID)), _plugin));

		HtmlTemplate template = AppTemplateService.getTemplate(
				TEMPLATE_MODIFY_CHOICE, getLocale(), model);

		return getAdminPage(template.getHtml());
	}

	/**
	 * Process the choice modifications
	 * 
	 * @param request
	 *            The Http request
	 * @return Html form
	 */
	public String doModifyChoice(HttpServletRequest request) {
		String strLabel = request.getParameter(PARAMETER_CHOICE_LABEL);

		// Mandatory field
		if (strLabel.equals("")) {
			return AdminMessageService.getMessageUrl(request,
					Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP);
		}

		PollChoice choice = PollChoiceHome.findByPrimaryKey(Integer
				.parseInt(request.getParameter(PARAMETER_CHOICE_ID)), _plugin);
		choice.setLabel(strLabel);
		PollChoiceHome.update(choice, _plugin);

		UrlItem url = new UrlItem(JSP_MODIFY_QUESTION);
		url.addParameter(PARAMETER_QUESTION_ID, choice.getQuestionId());

		return url.getUrl();
	}

	/**
	 * Returns the confirmation to remove the choice
	 * 
	 * @param request
	 *            The Http request
	 * @return the confirmation page
	 */
	public String getConfirmRemoveChoice(HttpServletRequest request) {
		UrlItem url = new UrlItem(JSP_DO_REMOVE_CHOICE);
		url.addParameter(PARAMETER_QUESTION_ID, Integer.parseInt(request
				.getParameter(PARAMETER_QUESTION_ID)));
		url.addParameter(PARAMETER_CHOICE_ID, Integer.parseInt(request
				.getParameter(PARAMETER_CHOICE_ID)));

		return AdminMessageService.getMessageUrl(request,
				MESSAGE_CONFIRM_REMOVE_QUESTION, url.getUrl(),
				AdminMessage.TYPE_CONFIRMATION);
	}

	/**
	 * Remove a choice
	 * 
	 * @param request
	 *            The Http request
	 * @return URL
	 */
	public String doRemoveChoice(HttpServletRequest request) {
		PollChoiceHome.remove(Integer.parseInt(request
				.getParameter(PARAMETER_CHOICE_ID)), _plugin);

		UrlItem url = new UrlItem(JSP_MODIFY_QUESTION);
		url.addParameter(PARAMETER_QUESTION_ID, Integer.parseInt(request
				.getParameter(PARAMETER_QUESTION_ID)));

		return url.getUrl();
	}

}
