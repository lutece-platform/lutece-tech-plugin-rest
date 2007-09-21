<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="systeminfo" scope="session" class="fr.paris.lutece.plugins.systeminfo.web.SystemInfoJspBean" />

<% systeminfo.init( request , systeminfo.RIGHT_SYSTEMINFO_MANAGEMENT ); %>
<%= systeminfo.getSystemInfo( request )%>

<%@include file="../../AdminFooter.jsp" %>