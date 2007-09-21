<%@ page errorPage="../../ErrorPagePortal.jsp" %>
<jsp:include page="../../PortalHeader.jsp" />


<jsp:useBean id="helpdeskApp" scope="session" class="fr.paris.lutece.plugins.helpdesk.web.HelpdeskApp" />

<%
	out.println(helpdeskApp.getContactForm( request ));
%>