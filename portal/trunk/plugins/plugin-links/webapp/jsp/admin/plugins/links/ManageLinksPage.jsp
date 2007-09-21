<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="links" scope="session" class="fr.paris.lutece.plugins.links.web.LinksLibraryJspBean" />

<% links.init( request, links.RIGHT_MANAGE_LINKS ); %>
<%= links.getManageLinksPage( request ) %>

<%@ include file="../../AdminFooter.jsp" %>

