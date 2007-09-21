<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="event" scope="session" class="fr.paris.lutece.plugins.calendar.web.CalendarJspBean" />

<% event.init( request, event.RIGHT_MANAGE_CALENDAR ); %>
<%= event.getModifyEvent ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>