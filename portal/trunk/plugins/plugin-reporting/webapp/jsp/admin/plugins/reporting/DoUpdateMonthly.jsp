<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="reporting" scope="session" class="fr.paris.lutece.plugins.reporting.web.ReportingJspBean" />

<%
    reporting.init( request, reporting.RIGHT_REPORTING_MANAGEMENT );
    response.sendRedirect( reporting.doUpdateReportingMonthly( request ) );
%>
