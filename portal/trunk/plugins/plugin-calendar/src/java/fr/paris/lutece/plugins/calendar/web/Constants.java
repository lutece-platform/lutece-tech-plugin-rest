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
package fr.paris.lutece.plugins.calendar.web;


/**
 * This class provides contants for the calendar plugin.
 */
public final class Constants
{
    // Markers
    public static final String MARK_AGENDA = "agenda";
    public static final String MARK_WEEKS = "weeks";
    public static final String MARK_DAY = "day";
    public static final String MARK_DAYS = "days";
    public static final String MARK_DAY_CLASS = "day_class";
    public static final String MARK_LINK_CLASS = "link_class";
    public static final String MARK_MONTH_LABEL = "month_label";
    public static final String MARK_JSP_URL = "jsp_url";
    public static final String MARK_EVENT_TITLE = "event_title";
    public static final String MARK_EVENT_SHORT_TITLE = "short_event_title";
    public static final String MARK_EVENT_DESCRIPTION = "event_description";
    public static final String MARK_EVENT_LOCATION = "event_location";
    public static final String MARK_EVENT_IMAGE = "event_image";
    public static final String MARK_EVENT_URL = "event_url";
    public static final String MARK_DAY_LINK = "day_link";
    public static final String MARK_EVENTS = "events";

    // Parameters
    public static final String PARAM_VIEW = "view";
    public static final String PARAM_DATE = "date";
    public static final String PARAM_AGENDA = "agenda";

    // Properties
    public static final String PROPERTY_TITLE = "calendar.title";
    public static final String PROPERTY_PATH = "calendar.path";
    public static final String PROPERTY_SHORTLABEL_MONDAY = "calendar.shortlabel.monday";
    public static final String PROPERTY_SHORTLABEL_TUESDAY = "calendar.shortlabel.tuesday";
    public static final String PROPERTY_SHORTLABEL_WEDNESDAY = "calendar.shortlabel.wednesday";
    public static final String PROPERTY_SHORTLABEL_THURSDAY = "calendar.shortlabel.thursday";
    public static final String PROPERTY_SHORTLABEL_FRIDAY = "calendar.shortlabel.friday";
    public static final String PROPERTY_SHORTLABEL_SATURDAY = "calendar.shortlabel.saturday";
    public static final String PROPERTY_SHORTLABEL_SUNDAY = "calendar.shortlabel.sunday";
    public static final String PROPERTY_LABEL_MONDAY = "calendar.label.monday";
    public static final String PROPERTY_LABEL_TUESDAY = "calendar.label.tuesday";
    public static final String PROPERTY_LABEL_WEDNESDAY = "calendar.label.wednesday";
    public static final String PROPERTY_LABEL_THURSDAY = "calendar.label.thursday";
    public static final String PROPERTY_LABEL_FRIDAY = "calendar.label.friday";
    public static final String PROPERTY_LABEL_SATURDAY = "calendar.label.saturday";
    public static final String PROPERTY_LABEL_SUNDAY = "calendar.label.sunday";
    public static final String PROPERTY_LABEL_FORMAT_DAY = "calendar.label.format.day";
    public static final String PROPERTY_LABEL_FORMAT_MONTH = "calendar.label.format.month";
    public static final String PROPERTY_LABEL_FORMAT_DATE_OF_DAY = "calendar.label.format.date.day";
    public static final String PROPERTY_LABEL_FORMAT_WEEK_DAY = "calendar.label.format.week.day";
    public static final String PROPERTY_SMALLCALENDAR_LINKCLASS_NO_EVENT = "calendar.smallcalendar.linkclass.noevent";
    public static final String PROPERTY_SMALLCALENDAR_LINKCLASS_HAS_EVENTS = "calendar.smallcalendar.linkclass.hasevents";
    public static final String PROPERTY_EVENT_SHORT_TITLE_LENGTH = "calendar.event.short.title.length";
    public static final String PROPERTY_EVENT_SHORT_TITLE_END = "calendar.event.short.title.end";
    public static final String PROPERTY_AGENDASERVICE_CACHE_ENABLE = "calendar.agendaservice.cache.enable";
    public static final String PROPERTY_AGENDA = "calendar.agenda.";
    public static final String PROPERTY_RUNAPP_JSP_URL = "calendar.runapp.jsp.url";
    public static final String PROPERTY_EVENTLIST_VIEW_DAY = "calendar.view.day.eventlist";
    public static final String PROPERTY_EVENTLIST_VIEW_WEEK = "calendar.view.week.eventlist";
    public static final String PROPERTY_EVENTLIST_VIEW_MONTH = "calendar.view.month.eventlist";
    public static final String PROPERTY_EVENTLIST = "calendar.eventlist.";
    public static final String PROPERTY_WORKING_DAYS_IN_WEEK = "calendar.working.days";
    public static final String PROPERTY_ICAL_TRACE_ENABLE = "calendar.ical.trace.enable";
    public static final String PROPERTY_READ_ONLY = "calendar.resourceType.readOnly";
    public static final String PROPERTY_READ_WRITE = "calendar.resourceType.readAndWrite";
    public static final String PROPERTY_ACCESSIBLE_BY_USER = "accessible";
    public static final String PROPERTY_ROLE_NONE = "none";

    // Properties for page titles
    public static final String PROPERTY_PAGE_TITLE_MANAGE_EVENTS = "calendar.calendar_manage_events.pageTitle";
    public static final String PROPERTY_PAGE_TITLE_MANAGE_CALENDARS = "calendar.manage_calendars.pageTitle";
    public static final String PROPERTY_PAGE_TITLE_CREATE_CALENDAR = "calendar.create_calendar.pageTitle";
    public static final String PROPERTY_PAGE_TITLE_MODIFY_CALENDAR = "calendar.modify_calendar.pageTitle";
    public static final String PROPERTY_PAGE_TITLE_CREATE_EVENT = "calendar.create_event.pageTitle";
    public static final String PROPERTY_PAGE_TITLE_MODIFY_EVENT = "calendar.modify_event.pageTitle";
    public static final String PROPERTY_CALENDAR_DOTS_PATH = "calendar.dots.path";
    public static final String PROPERTY_MESSAGE_DATEFORMAT = "calendar.message.dateFormat";
    public static final String PROPERTY_MESSAGE_TIMEFORMAT = "calendar.message.timeFormat";
    public static final String PROPERTY_MESSAGE_EXIST = "calendar.message.exist";
    public static final String PROPERTY_EVENTS_PER_PAGE = "calendar.eventsPerPage";
    public static final String PROPERTY_SORT_EVENTS = "calendar.modify_calendar.sortEvents";

    //Parameters for Back Office
    public static final String PARAMETER_PLUGIN_NAME = "plugin_name";
    public static final String PARAMETER_CALENDAR_NAME = "calendar_name";
    public static final String PARAMETER_CALENDAR_IMAGE = "calendar_image";
    public static final String PARAMETER_CALENDAR_PREFIX = "calendar_prefix";
    public static final String PARAMETER_CALENDAR_ROLE = "calendar_role";
    public static final String PARAMETER_EVENT_OLD_DATE = "event_old_date";
    public static final String PARAMETER_EVENT_OLD_TITLE = "event_old_title";
    public static final String PARAMETER_WORKGROUP = "workgroup";
    public static final String PARAMETER_CALENDAR_ROLE_MANAGER = "calendar_role_manager";
    public static final String PARAMETER_PAGE_INDEX = "page_index";

    //Parameters for front office
    public static final String PARAMETER_ACTION = "action";
    public static final String PARAMETER_PAGE = "page";

    //Common parameters for back and front office
    public static final String PARAMETER_EVENT_DATE = "event_date";
    public static final String PARAMETER_EVENT_TITLE = "event_title";
    public static final String PARAMETER_EVENT_TIME_START = "event_time_start";
    public static final String PARAMETER_EVENT_TIME_END = "event_time_end";
    public static final String PARAMETER_SORT_EVENTS = "sort_events";
    public static final String PARAMETER_CALENDAR_ID = "calendar_id";
    public static final String PARAMETER_EVENT_ID = "event_id";

    //Bookmarks for BackOffice
    public static final String MARK_CALENDARS_LIST = "calendar_list";
    public static final String MARK_EVENTS_LIST = "event_list";
    public static final String MARK_EVENT = "event";
    public static final String MARK_DOTS_LIST = "dots_list";
    public static final String MARK_PAGINATOR = "paginator";
    public static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    public static final String MARK_EVENTS_SORT_LIST = "sort_list";
    public static final String MARK_DEFAULT_SORT_EVENT = "default_sort_event";
    public static final String MARK_WORKGROUPS_LIST = "workgroups_list";
    public static final String MARK_ROLES_LIST = "roles_list";

    //Bookmarks used in front
    public static final String MARK_PREVIOUS = "previous";
    public static final String MARK_NEXT = "next";
    public static final String MARK_TITLE = "title";
    public static final String MARK_LEGEND = "legend";
    public static final String MARK_VIEW_CALENDAR = "view_calendar";
    public static final String MARK_EVENT_LIST = "event_list";
    public static final String MARK_SMALL_MONTH_CALENDAR1 = "small_month_calendar1";
    public static final String MARK_SMALL_MONTH_CALENDAR2 = "small_month_calendar2";
    public static final String MARK_SMALL_MONTH_CALENDAR3 = "small_month_calendar3";
    public static final String MARK_AGENDA_RESOURCE_LIST = "agenda_resource_list";

    // Common bookmarks for back and front office
    public static final String MARK_DATE = "date";
    public static final String MARK_CALENDAR_ID = "calendar_id";
    public static final String MARK_CALENDAR = "calendar";
    public static final String MARK_LOCALE = "locale";

    //
    public static final String EMPTY_STRING = "";

    // Properties suffix
    public static final String SUFFIX_NAME = ".name";
    public static final String SUFFIX_LOADER_CLASS = ".loader.class";
    public static final String SUFFIX_LOADER_PARAMETER = ".loader.parameter";
    public static final String SUFFIX_LABEL = ".label";
    public static final String SUFFIX_EVENT_IMAGE = ".event.image";
    public static final String SUFFIX_ROLE = ".role";
    public static final String SUFFIX_CLASS = ".class";
    public static final String SUFFIX_TITLE = ".title";

    // Session Attributes
    public static final String ATTRIBUTE_CALENDAR_VIEW = "CALENDAR_CURRENT_VIEW_CLASS";
    public static final String ATTRIBUTE_CALENDAR_AGENDA = "CALENDAR_CURRENT_AGENDA";

    // Views
    public static final String VIEW_DAY = "day";
    public static final String VIEW_WEEK = "week";
    public static final String VIEW_MONTH = "month";

    // CSS Styles
    public static final String STYLE_CLASS_VIEW_MONTH_DAY = "calendar-view-month-day";
    public static final String STYLE_CLASS_VIEW_WEEK_DAY = "calendar-view-week-day";
    public static final String STYLE_CLASS_SMALLMONTH_DAY = "calendar-smallmonth-day";
    public static final String STYLE_CLASS_SUFFIX_OLD = "-old";
    public static final String STYLE_CLASS_SUFFIX_TODAY = "-today";
    public static final String STYLE_CLASS_SUFFIX_OFF = "-off";
}
