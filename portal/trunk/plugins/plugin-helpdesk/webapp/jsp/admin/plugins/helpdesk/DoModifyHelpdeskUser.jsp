<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="helpdesk" scope="session" class="fr.paris.lutece.plugins.helpdesk.web.HelpdeskUserJspBean" />

<%
    helpdesk.init( request , helpdesk.RIGHT_MANAGE_USER_HELPDESK );
    response.sendRedirect( helpdesk.doModifyHelpdeskUser( request ) );
%>
