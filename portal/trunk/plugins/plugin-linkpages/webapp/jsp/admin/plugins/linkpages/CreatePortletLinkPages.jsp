<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="portletLinkPages" scope="session" class="fr.paris.lutece.plugins.linkpages.web.portlet.LinkPagesPortletJspBean" />


<% portletLinkPages.init( request,  portletLinkPages.RIGHT_MANAGE_ADMIN_SITE  ); %>
<%= portletLinkPages.getCreate( request ) %>

<%@ include file="../../AdminFooter.jsp" %>

