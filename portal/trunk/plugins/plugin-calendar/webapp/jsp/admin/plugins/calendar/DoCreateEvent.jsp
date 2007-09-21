<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="event" scope="session" class="fr.paris.lutece.plugins.calendar.web.CalendarJspBean" />

<%
    event.init( request, event.RIGHT_MANAGE_CALENDAR );
    response.sendRedirect( event.doCreateEvent( request ) );
%>

