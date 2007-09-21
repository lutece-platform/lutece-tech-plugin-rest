<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="myapps" scope="session" class="fr.paris.lutece.plugins.myapps.web.MyAppsJspBean" />

<% myapps.init( request, myapps.RIGHT_MYAPPS_MANAGEMENT  ); %>
<%= myapps.getCreateMyApp( request ) %>

<%@ include file="../../AdminFooter.jsp" %>

