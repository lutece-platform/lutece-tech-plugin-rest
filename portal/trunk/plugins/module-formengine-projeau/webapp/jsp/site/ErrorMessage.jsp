<%@ page errorPage="ErrorPage.jsp" %>
<jsp:include page="PortalHeader.jsp" />


<jsp:useBean id="siteMessage" scope="request" class="fr.paris.lutece.portal.plugins.projeau.web.SiteMessageJspBean" />

<%=	 siteMessage.getMessage( request ) %>


