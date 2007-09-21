<%@page errorPage="../../../ErrorPage.jsp" %>
<%@page import="fr.paris.lutece.plugins.ods.web.indexer.FrontIndexerJspBean"%>

<jsp:useBean id="indexer" scope="session" class="fr.paris.lutece.plugins.ods.web.indexer.FrontIndexerJspBean" />

<%
	indexer.init( request, FrontIndexerJspBean.RIGHT_ODS_INDEXER);
	response.sendRedirect( indexer.getDemandeIndexation( request ) );
%>