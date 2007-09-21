<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="dbpage" scope="session" class="fr.paris.lutece.plugins.dbpage.web.DbPageJspBean" />

<% dbpage.init( request, dbpage.RIGHT_DBPAGE_MANAGEMENT ); %>
<%= dbpage.getModifySection ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>

