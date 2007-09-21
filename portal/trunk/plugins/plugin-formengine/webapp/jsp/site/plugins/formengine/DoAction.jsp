<%@ page errorPage="../../ErrorPagePortal.jsp" %>

<jsp:useBean id="formengine" scope="request" class="fr.paris.lutece.plugins.formengine.web.FormEngineApp" />

<%
	String strNext = formengine.doAction( request );
	String strUrl = formengine.setUrl( request, strNext );
	
	if ( strUrl != null )
	{
		response.sendRedirect( strUrl );
	}
	else
	{
		response.sendRedirect( request.getContextPath( ) + "/index.html" );
	}
	
%>