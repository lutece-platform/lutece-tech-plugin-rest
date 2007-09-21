<%@ page errorPage="../../../ErrorPage.jsp" %>
<%@page import="fr.paris.lutece.plugins.ods.web.relevetravaux.ReleveDesTravauxJspBean"%>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="pdd" scope="session" class="fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean" />

<%
pdd.setFonctionnalite( false );
pdd.init( request, fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean.RIGHT_ODS_PROP_DE_DELIB );
pdd.initFilter( request );
%>

<%= pdd.getPDDListSelect( request,
		ReleveDesTravauxJspBean.CONSTANTE_URL_JSP_APPELANT_PROPOSITION,
		ReleveDesTravauxJspBean.CONSTANTE_URL_JSP_RETOUR_PDD ) %>

<%@ include file="../../../AdminFooter.jsp" %>
