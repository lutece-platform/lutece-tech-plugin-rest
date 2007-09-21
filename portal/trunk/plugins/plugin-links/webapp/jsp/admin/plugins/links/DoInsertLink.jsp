<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="links" scope="session" class="fr.paris.lutece.plugins.links.web.LinksServiceJspBean" />

<% 
     response.sendRedirect( links.doInsertLink( request ) );
%>
