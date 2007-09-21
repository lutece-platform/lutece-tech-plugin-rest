<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="calendar" scope="session" class="fr.paris.lutece.plugins.calendar.web.CalendarJspBean" />

<% calendar.init( request, calendar.RIGHT_MANAGE_CALENDAR ); %>
<%= calendar.getModifyCalendar ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>