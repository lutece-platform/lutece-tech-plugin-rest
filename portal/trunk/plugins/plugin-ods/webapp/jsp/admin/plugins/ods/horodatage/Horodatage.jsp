<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="horodatage" scope="session" class="fr.paris.lutece.plugins.ods.web.horodatage.HorodatageJspBean" />

<%
	horodatage.init( request, fr.paris.lutece.plugins.ods.web.horodatage.HorodatageJspBean.RIGHT_ODS_HORODATAGE);
%>

<%= horodatage.getHorodatage( request )%>

<%@ include file="../../../AdminFooter.jsp" %>