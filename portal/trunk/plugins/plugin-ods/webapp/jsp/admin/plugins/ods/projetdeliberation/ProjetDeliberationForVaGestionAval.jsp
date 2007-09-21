<%@ page errorPage="../../../ErrorPage.jsp" %>
<%@ page import="fr.paris.lutece.plugins.ods.web.voeuamendement.VoeuAmendementJspBean"%>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="pdd" scope="session" class="fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean" />

<% 
if(  pdd.isFonctionnaliteProjetDeDeliberation() )
{ 
	pdd.init( request, fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean.RIGHT_ODS_PROJET_DE_DELIB_GESTION_AVAL );
}
else
{
	pdd.init( request, fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean.RIGHT_ODS_PROP_DE_DELIB_GESTION_AVAL );
}
%>

<%
pdd.setFonctionnalite( true );
pdd.initFilter( request, true );
%>

<%= pdd.getPDDListMultiSelect( request,
		VoeuAmendementJspBean.CONSTANTE_URL_JSP_APPELANT_PROJET_AVAL,
		null,
		true ) %>


<%@ include file="../../../AdminFooter.jsp" %>
