<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="group" scope="session" class="fr.paris.lutece.portal.web.group.GroupJspBean" />

<% 
	group.init( request, group.RIGHT_GROUPS_MANAGEMENT ); %>
<%= group.getModifyGroup( request ) %>

<%@ include file="../AdminFooter.jsp" %>


