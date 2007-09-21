<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="reporting" scope="session" class="fr.paris.lutece.plugins.reporting.web.ReportingJspBean" />

<% reporting.init( request, reporting.RIGHT_REPORTING_MANAGEMENT ); %>
<%= reporting.getPublishingReporting( request ) %>

<%@ include file="../../AdminFooter.jsp" %>