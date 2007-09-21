<%@ page errorPage="../../ErrorPagePortal.jsp" %>
<jsp:include page="../../PortalHeader.jsp" />


<jsp:useBean id="newsletter" scope="request" class="fr.paris.lutece.plugins.newsletter.web.NewsLetterApp" />

<%
	response.sendRedirect( newsletter.doSubscribeFromPortlet( request ) );
%>