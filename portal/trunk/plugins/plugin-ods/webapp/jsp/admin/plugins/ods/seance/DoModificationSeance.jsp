<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="seance" scope="session" class="fr.paris.lutece.plugins.ods.web.seance.SeanceJspBean" />

<%
seance.init( request, fr.paris.lutece.plugins.ods.web.seance.SeanceJspBean.RIGHT_ODS_SEANCE );
%>

<%= seance.doModificationSeance( request ) %>

<%@ include file="../../../AdminFooter.jsp" %>