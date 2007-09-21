<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:include page="../../../AdminHeader.jsp" />
<jsp:useBean id="groupe" scope="session" class="fr.paris.lutece.plugins.ods.web.groupepolitique.GroupePolitiqueJspBean" />

<%
groupe.init( request, fr.paris.lutece.plugins.ods.web.groupepolitique.GroupePolitiqueJspBean.RIGHT_ODS_GROUPE_POLITIQUE);
%>

<%= groupe.getModificationGroupe( request ) %>

<%@ include file="../../../AdminFooter.jsp" %>
