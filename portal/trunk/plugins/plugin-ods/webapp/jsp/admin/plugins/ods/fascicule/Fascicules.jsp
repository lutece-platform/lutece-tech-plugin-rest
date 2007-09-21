<%@ page errorPage="../../../ErrorPage.jsp" %>

<%@page import="fr.paris.lutece.plugins.ods.web.fascicule.FasciculeJspBean"%>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="fascicule" scope="session" class="fr.paris.lutece.plugins.ods.web.fascicule.FasciculeJspBean" />

<%
fascicule.init( request, FasciculeJspBean.RIGHT_ODS_FASCICULE);
%>

<%= fascicule.getListeFascicules( request )%>

<%@ include file="../../../AdminFooter.jsp" %>