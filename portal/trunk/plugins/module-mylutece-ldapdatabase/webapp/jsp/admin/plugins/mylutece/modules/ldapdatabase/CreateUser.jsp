<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:include page="../../../../AdminHeader.jsp" />

<jsp:useBean id="ldapUser" scope="session" class="fr.paris.lutece.plugins.mylutece.modules.ldapdatabase.authentication.web.LdapdatabaseJspBean" />

<% ldapUser.init( request, ldapUser.RIGHT_MANAGE_LDAPDATABASE_USERS ); %>
<%= ldapUser.getCreateUser( request ) %>

<%@ include file="../../../../AdminFooter.jsp" %>
