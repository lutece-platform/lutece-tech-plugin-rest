<jsp:useBean id="panier" scope="session" class="fr.paris.lutece.plugins.ods.web.PanierApp" />

<% panier.downloadSelectionPanier( request, response ); %>
