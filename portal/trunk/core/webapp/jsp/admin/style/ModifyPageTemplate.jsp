<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:include page="../AdminHeader.jsp" />

<jsp:useBean id="pagetemplate" scope="session" class="fr.paris.lutece.portal.web.style.PageTemplatesJspBean" />

<% pagetemplate.init( request , pagetemplate.RIGHT_MANAGE_PAGE_TEMPLATES ); %>
<%= pagetemplate.getModifyPageTemplate( request )%>

<%@ include file="../AdminFooter.jsp" %>
