<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="poll" scope="session" class="fr.paris.lutece.plugins.poll.web.PollJspBean" />

<%
    poll.init( request, poll.RIGHT_MANAGE_POLL );
    response.sendRedirect( poll.doModifyChoice( request ) );
%>
