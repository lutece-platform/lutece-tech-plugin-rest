<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="calendar" scope="session" class="fr.paris.lutece.plugins.calendar.web.CalendarJspBean" />

<%
    calendar.init( request, calendar.RIGHT_MANAGE_CALENDAR );
    response.sendRedirect( calendar.doRemoveCalendar( request ) );
%>

