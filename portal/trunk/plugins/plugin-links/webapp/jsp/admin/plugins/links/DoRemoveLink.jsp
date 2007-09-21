<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="links" scope="session" class="fr.paris.lutece.plugins.links.web.LinksLibraryJspBean" />

<% links.init( request, links.RIGHT_MANAGE_LINKS ); 
	response.sendRedirect( links.doRemoveLink( request ) ); 
%>



