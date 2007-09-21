<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="nature" scope="session" class="fr.paris.lutece.plugins.ods.web.naturedossier.NatureDesDossiersJspBean" />

<%
nature.init( request, fr.paris.lutece.plugins.ods.web.naturedossier.NatureDesDossiersJspBean.RIGHT_ODS_NATURE_DES_DOSSIERS);
response.sendRedirect(nature.doCreationNature( request ));
%>

<%@ include file="../../../AdminFooter.jsp" %>