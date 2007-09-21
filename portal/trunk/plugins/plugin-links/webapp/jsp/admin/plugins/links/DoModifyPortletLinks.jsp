<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="linksPortlet" scope="session" class="fr.paris.lutece.plugins.links.web.portlet.LinksPortletJspBean" />

<% 
	linksPortlet.init( request, linksPortlet.RIGHT_MANAGE_ADMIN_SITE );
   	response.sendRedirect( linksPortlet.doModify( request ) ); 
%>
