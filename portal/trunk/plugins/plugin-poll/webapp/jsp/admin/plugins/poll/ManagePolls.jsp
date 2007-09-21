<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="poll" scope="session" class="fr.paris.lutece.plugins.poll.web.PollJspBean" />

<% poll.init( request, poll.RIGHT_MANAGE_POLL ); %>
<%= poll.getManagePolls ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
