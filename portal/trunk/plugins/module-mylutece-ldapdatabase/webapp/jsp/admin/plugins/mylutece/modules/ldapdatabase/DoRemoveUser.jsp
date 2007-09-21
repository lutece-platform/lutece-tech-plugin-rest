<%@ page errorPage="../../../../ErrorPage.jsp" %>

<jsp:useBean id="ldapUser" scope="session" class="fr.paris.lutece.plugins.mylutece.modules.ldapdatabase.authentication.web.LdapdatabaseJspBean" />

<%
	ldapUser.init( request, ldapUser.RIGHT_MANAGE_LDAPDATABASE_USERS );
   	response.sendRedirect( ldapUser.doRemoveUser( request ) ); 
%>
