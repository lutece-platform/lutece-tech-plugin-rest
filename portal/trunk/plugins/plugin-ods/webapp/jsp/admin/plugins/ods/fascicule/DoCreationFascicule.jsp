<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="fascicule" scope="session" class="fr.paris.lutece.plugins.ods.web.fascicule.FasciculeJspBean" />

<%
	fascicule.init( request, fr.paris.lutece.plugins.ods.web.fascicule.FasciculeJspBean.RIGHT_ODS_FASCICULE);
	response.sendRedirect(fascicule.doCreationFascicule( request ));
%>

<%@ include file="../../../AdminFooter.jsp" %>