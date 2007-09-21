<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="folderlistingPortlet" scope="session" class="fr.paris.lutece.plugins.folderlisting.web.portlet.FolderListingPortletJspBean" />

<% folderlistingPortlet.init( request, folderlistingPortlet.RIGHT_MANAGE_ADMIN_SITE ); %>
<%= folderlistingPortlet.getModify ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
