<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeaderSessionLess.jsp" />

<jsp:useBean id="reporting" scope="session" class="fr.paris.lutece.plugins.reporting.web.ReportingJspBean" />

<% reporting.init( request, reporting.RIGHT_REPORTING_MANAGEMENT ); %>
<%= reporting.getHistoryFicheProject( request ) %>

<%@ include file="../../AdminFooter.jsp" %>