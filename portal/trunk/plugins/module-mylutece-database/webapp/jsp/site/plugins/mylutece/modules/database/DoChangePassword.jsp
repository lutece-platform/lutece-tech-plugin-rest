<jsp:useBean id="myluteceDatabaseApp" scope="request" class="fr.paris.lutece.plugins.mylutece.modules.database.authentication.web.MyLuteceDatabaseApp" />
<jsp:include page="../../PortalHeader.jsp" />

<%
	response.sendRedirect( myluteceDatabaseApp.doChangePassword( request ) );
%>
