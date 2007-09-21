<%@ page errorPage="../../../ErrorPage.jsp" %>

<jsp:useBean id="pdd" scope="session" class="fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean" />

<%
if(  pdd.isFonctionnaliteProjetDeDeliberation() )
{
	if( pdd.isGestionAval( ) )
	{
		pdd.init( request, fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean.RIGHT_ODS_PROJET_DE_DELIB_GESTION_AVAL );
	}
	else
	{
		pdd.init( request, fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean.RIGHT_ODS_PROJET_DE_DELIB );
	}
}
else
{
	if( pdd.isGestionAval( ) )
	{
		pdd.init( request, fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean.RIGHT_ODS_PROP_DE_DELIB_GESTION_AVAL );
	}
	else
	{
		pdd.init( request, fr.paris.lutece.plugins.ods.web.pdd.ProjetDeDeliberationJspBean.RIGHT_ODS_PROP_DE_DELIB );
	}
}
%>

<% response.sendRedirect( pdd.doSuppressionPDD( request ) ); %>
