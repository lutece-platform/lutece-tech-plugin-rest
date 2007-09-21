<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="pdd" scope="session" class="fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean" />

<% 
pdd.init( request, fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean.RIGHT_ODS_PROJET_DE_DELIB_GESTION_AVAL );
%>

<% 
pdd.setFonctionnalite( true );
pdd.setGestionAval( true );
pdd.initFilter( request, true );
%>

<%= pdd.getPDDList( request ) %>

<%@ include file="../../../AdminFooter.jsp" %>
