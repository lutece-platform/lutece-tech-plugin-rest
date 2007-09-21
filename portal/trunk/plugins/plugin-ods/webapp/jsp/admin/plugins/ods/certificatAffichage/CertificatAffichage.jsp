<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="certificat" scope="session" class="fr.paris.lutece.plugins.ods.web.certificataffichage.CertificatAffichageJspBean" />

<%
certificat.init( request, fr.paris.lutece.plugins.ods.web.certificataffichage.CertificatAffichageJspBean.RIGHT_ODS_CERTIFICAT_AFFICHAGE);
%>

<%= certificat.getCertificatList( request )%>

<%@ include file="../../../AdminFooter.jsp" %>