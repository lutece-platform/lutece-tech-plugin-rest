<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" encoding="ISO-8859-1" indent="yes"/>
<xsl:template match="cartereponse">
<html>
	<body>
	<xsl:text>
    Vous venez d’utiliser le service de  demande de rendez-vous en ligne de paris.fr
     en vue de l’inscription de votre (vos) enfant(s) au Vacances Arc en Ciel.
     </xsl:text><br />
    
    <xsl:text>
     Votre demande est en cours de transmission au service des Vacances Arc en Ciel sous le numéro : </xsl:text>
     <xsl:value-of select="numero" /><xsl:text>.</xsl:text><br />
     <xsl:text>
	 <strong>Merci de votre visite.</strong>
	 </xsl:text><br /><br/>
	 <xsl:text>
	 <strong>Récapitulatif :</strong>
	 </xsl:text>
     <p>
		<xsl:apply-templates select="famille1" />
	</p>
	
	<p>
		<xsl:apply-templates select="famille2" />
	</p>
     
	<xsl:text>     
     Merci de ne pas répondre à ce courriel.</xsl:text><br />
     <br/><br/>
     <center><xsl:text>
     Centre d'appel : Paris infos mairie – 3975</xsl:text></center><br />
     <xsl:text>
     D’autres démarches en ligne sont disponibles sur paris.fr : Etat civil, demande d’enlèvements encombrants, sectorisation scolaire...
     </xsl:text><br/><br/>
     <xsl:text>Pour y accéder  consulter la rubrique téléprocédures :</xsl:text><br/>
			<a href="http://www.paris.fr/portail/vosdemarches/Portal.lut?page_id=6475" target="_blank">» Consulter la rubrique</a>
     </body>
</html>
 </xsl:template>

 
 <xsl:template match="famille1">

 	1ère famille<br/>
	<xsl:apply-templates select="nom"/><xsl:text> </xsl:text><xsl:apply-templates select="prenom"/>
	<xsl:text>, </xsl:text>
     
    <xsl:apply-templates select="adresse" /><br/>
    
	<xsl:apply-templates select="courriel" /><br/>
    <strong>Téléphone : </strong><xsl:apply-templates select="telephone" /><br/>
	
	
 </xsl:template>


 <xsl:template match="famille2">
 	Seconde famille<br/>

	<xsl:apply-templates select="nom"/><xsl:text> </xsl:text><xsl:apply-templates select="prenom"/>
	<xsl:text>, </xsl:text>
     
    <xsl:apply-templates select="adresse" /><br/>
    <xsl:if test="telephone != ''">
    <strong>Téléphone : </strong><xsl:apply-templates select="telephone" /><br/>
	 </xsl:if>

 </xsl:template>

<xsl:template match="adresse">
     <xsl:apply-templates select="dunumero"/>
     <xsl:text> </xsl:text>
     <xsl:if test="dubis != ''">
     	<xsl:variable name="dubistemp">
		   			<xsl:value-of select="dubis"/>
		   		</xsl:variable>
		   		<xsl:choose>
				  <xsl:when test="$dubistemp='b'">
				   		<xsl:text>Bis</xsl:text>
				  </xsl:when>
				  <xsl:when test="$dubistemp='t'">
				   		<xsl:text>Ter</xsl:text>
				  </xsl:when>
				  <xsl:when test="$dubistemp='q'">
				   		<xsl:text>Quiquies</xsl:text>
				  </xsl:when>
				  <xsl:otherwise>
				    	<xsl:text></xsl:text>
				  </xsl:otherwise>
				</xsl:choose>
     <xsl:text> </xsl:text>
     </xsl:if>
     
   	 <xsl:apply-templates select="type-voie" />
     <xsl:text> </xsl:text>
     <xsl:apply-templates select="libelle-voie" />
	 <xsl:text>, </xsl:text>
     <xsl:if test="complement1-adresse != ''">
     	<xsl:apply-templates select="complement1-adresse" />
     	<xsl:text> </xsl:text>
     </xsl:if>
     <xsl:if test="complement2-adresse != ''">
     	<xsl:apply-templates select="complement2-adresse " />
     	<xsl:text> - </xsl:text>
     </xsl:if>
     <xsl:text> </xsl:text>
     <xsl:apply-templates select="code-commune" />
     <xsl:text> </xsl:text>
     <xsl:apply-templates select="ville" />
     
 </xsl:template>
 
 
</xsl:stylesheet>