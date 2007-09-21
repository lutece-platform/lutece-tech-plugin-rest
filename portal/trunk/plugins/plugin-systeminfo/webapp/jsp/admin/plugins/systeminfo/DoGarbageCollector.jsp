<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="systeminfo" scope="session" class="fr.paris.lutece.plugins.systeminfo.web.SystemInfoJspBean" />

<%
    systeminfo.init( request, systeminfo.RIGHT_SYSTEMINFO_MANAGEMENT);
    response.sendRedirect( systeminfo.doGarbageCollector( request ) );
%>