<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="adminquery" scope="session" class="fr.paris.lutece.plugins.adminquery.web.AdminQueryJspBean" />

<% adminquery.init( request , adminquery.RIGHT_MANAGE_ADMIN_QUERY ); %>
<%= adminquery.getAdminQuery( request )%>

<%@include file="../../AdminFooter.jsp" %>
