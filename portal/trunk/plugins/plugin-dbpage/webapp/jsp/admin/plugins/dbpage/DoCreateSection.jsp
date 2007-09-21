<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="dbpage" scope="session" class="fr.paris.lutece.plugins.dbpage.web.DbPageJspBean" />

<%
    dbpage.init( request, dbpage.RIGHT_DBPAGE_MANAGEMENT );
    response.sendRedirect( dbpage.doCreateSection( request ) );
%>
