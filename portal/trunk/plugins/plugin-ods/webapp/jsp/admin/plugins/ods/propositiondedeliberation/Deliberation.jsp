<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="pdd" scope="session" class="fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean" />

<% pdd.init( request, fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean.RIGHT_ODS_PROP_DE_DELIB );%>

<% 
pdd.setFonctionnalite( false );
pdd.setGestionAval( false );
pdd.initFilter( request );
%>

<%= pdd.getPDDList( request ) %>

<%@ include file="../../../AdminFooter.jsp" %>
