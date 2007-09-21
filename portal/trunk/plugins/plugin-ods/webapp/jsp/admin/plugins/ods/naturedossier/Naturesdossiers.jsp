<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="nature" scope="session" class="fr.paris.lutece.plugins.ods.web.naturedossier.NatureDesDossiersJspBean" />

<%
nature.init( request, fr.paris.lutece.plugins.ods.web.naturedossier.NatureDesDossiersJspBean.RIGHT_ODS_NATURE_DES_DOSSIERS);
%>

<%= nature.getListNatures( request )%>

<%@ include file="../../../AdminFooter.jsp" %>