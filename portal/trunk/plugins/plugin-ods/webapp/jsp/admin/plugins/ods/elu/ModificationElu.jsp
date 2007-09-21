<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="elu" scope="session" class="fr.paris.lutece.plugins.ods.web.elu.EluJspBean" />

<%
elu.init( request, fr.paris.lutece.plugins.ods.web.elu.EluJspBean.RIGHT_ODS_ELU);
%>

<%= elu.getModificationElu(request) %>

<%@ include file="../../../AdminFooter.jsp" %>
