<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="categorie" scope="session" class="fr.paris.lutece.plugins.ods.web.categoriedeliberation.CategorieDeliberationJspBean" />

<%
	categorie.init( request, fr.paris.lutece.plugins.ods.web.categoriedeliberation.CategorieDeliberationJspBean.RIGHT_ODS_CATEGORIE_DELIBERATION);
	response.sendRedirect( categorie.doModificationCategorie( request ));
%>

