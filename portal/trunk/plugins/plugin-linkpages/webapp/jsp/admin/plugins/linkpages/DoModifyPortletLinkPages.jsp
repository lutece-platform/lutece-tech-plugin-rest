<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="portletLinkPages" scope="session" class="fr.paris.lutece.plugins.linkpages.web.portlet.LinkPagesPortletJspBean" />


<%
    portletLinkPages.init( request,   portletLinkPages.RIGHT_MANAGE_ADMIN_SITE  );
    response.sendRedirect(  portletLinkPages.doModify( request )  );
%>


