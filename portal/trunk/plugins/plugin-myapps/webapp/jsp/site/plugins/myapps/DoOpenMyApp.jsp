<jsp:useBean id="myAppsPortlet" scope="session" class="fr.paris.lutece.plugins.myapps.web.portlet.MyAppsPortletJspBean" />

<%
response.sendRedirect( myAppsPortlet.doOpenMyApp( request ));
%>

