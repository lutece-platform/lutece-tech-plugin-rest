<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="helpdeskUser" scope="session" class="fr.paris.lutece.plugins.helpdesk.web.HelpdeskUserJspBean" />

<% helpdeskUser.init( request , helpdeskUser.RIGHT_MANAGE_USER_HELPDESK ); %>
<%= helpdeskUser.getManageHelpdeskAdmin(request) %>

<%@ include file="../../AdminFooter.jsp" %>