<%@ page errorPage="../../../ErrorPage.jsp" %>
<%@ page import="fr.paris.lutece.plugins.ods.web.ordredujour.OrdreDuJourJspBean"%>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="pdd" scope="session" class="fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean" />

<%
pdd.setFonctionnalite( true ); 
pdd.init( request, fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean.RIGHT_ODS_PROJET_DE_DELIB );
pdd.initFilter( request );
%>

<%= pdd.getPDDListMultiSelect( request,
		OrdreDuJourJspBean.JSP_SELECTION_PROJETS,
		OrdreDuJourJspBean.JSP_SELECTION_PDD_RETOUR_MULTI,
		false ) %>

<%@ include file="../../../AdminFooter.jsp" %>
