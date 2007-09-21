<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="releve" scope="session" class="fr.paris.lutece.plugins.ods.web.relevetravaux.ReleveDesTravauxJspBean" />

<%
releve.init( request, fr.paris.lutece.plugins.ods.web.relevetravaux.ReleveDesTravauxJspBean.RIGHT_ODS_RELEVE);
%>

<%= releve.getRelevesList( request )%>

<%@ include file="../../../AdminFooter.jsp" %>
