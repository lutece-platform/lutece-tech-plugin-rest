<%@ page errorPage="../../ErrorPage.jsp"%>

<jsp:useBean id="category" scope="session" class="fr.paris.lutece.plugins.document.web.category.CategoryJspBean" />

<%
	category.init(request, category.RIGHT_CATEGORY_MANAGEMENT);
	response.sendRedirect(category.doRemoveCategory(request));
%>



