<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="hideRecapSecondFamily"/>
	<xsl:param name="hideEspaceFirstFamilyTypeVoie"/>
	<xsl:param name="hideEspaceSecondFamilyTypeVoie"/>
	
	
<xsl:template match="cartereponse">

		<xsl:apply-templates select="famille1" />
		
		<xsl:if test="$hideRecapSecondFamily=0">
			<xsl:apply-templates select="famille2" />
		</xsl:if>
		
 </xsl:template>

 <xsl:template match="famille1">
 <fieldset class="formengine-fieldset">
 <legend class="formengine-legend">1ère famille</legend>
 	<h3>Identité et coordonnées : </h3>
     <ul>
     <li><strong>Nom : </strong><xsl:apply-templates select="nom"/></li>
     <li><strong>Prénom : </strong><xsl:apply-templates select="prenom"/></li>
     <li><strong>Nombre d'enfant : </strong><xsl:apply-templates select="nb-enfant" /></li>
     <li><strong>Courriel : </strong><xsl:apply-templates select="courriel" /></li>
     <li><strong>Téléphone : </strong><xsl:apply-templates select="telephone" /></li>
     </ul>
     <h3>Adresse : </h3>
     
     <xsl:apply-templates select="adresse">
     	<xsl:with-param name="hideEspaceTypeVoie" select="$hideEspaceFirstFamilyTypeVoie"/>
     </xsl:apply-templates>
     
     <form action="jsp/site/plugins/formengine/DoAction.jsp" id="evacsummary" name="evacsummary" enctype="multipart/form-data" method="post" class="formengine-form">
			<input name="page" value="formengine" type="hidden"/>
			<input name="form" value="evac" type="hidden"/>
			<input name="subform" value="summary" type="hidden"/>

			<div class="formengine-steps">
				<input name="action" value="Modifier la 1ère famille" class="formengine-submit" type="submit"/>
			</div>
		</form>
     
 </fieldset>
 </xsl:template>


 <xsl:template match="famille2">
 <fieldset class="formengine-fieldset">
 <legend class="formengine-legend">2de famille</legend>
 	<h3>Identité et coordonnées : </h3>
     <ul>
		<li><strong>Nom : </strong><xsl:apply-templates select="nom"/></li>
     	<li><strong>Prénom : </strong><xsl:apply-templates select="prenom"/></li>
     	<li><strong>Nombre d'enfant : </strong><xsl:apply-templates select="nb-enfant" /></li>
     	<xsl:if test="string(telephone) != ''">
     		<li><strong>Téléphone : </strong><xsl:apply-templates select="telephone" /></li>
     	</xsl:if>
     </ul>
     <h3>Adresse : </h3>
	
	 <xsl:apply-templates select="adresse">
     	<xsl:with-param name="hideEspaceTypeVoie" select="$hideEspaceSecondFamilyTypeVoie"/>
     </xsl:apply-templates>
	 
	 <form action="jsp/site/plugins/formengine/DoAction.jsp" id="evacsummary" name="evacsummary" enctype="multipart/form-data" method="post" class="formengine-form">
			<input name="page" value="formengine" type="hidden"/>
			<input name="form" value="evac" type="hidden"/>
			<input name="subform" value="summary" type="hidden"/>

			<div class="formengine-steps">
				<input name="action" value="Modifier la 2de famille" class="formengine-submit" type="submit"/>
			</div>
		</form>
	 
  </fieldset>
 </xsl:template>

<xsl:template match="adresse">
	<xsl:param name="hideEspaceTypeVoie"/>
     <ul>
     <li><strong>Numéro de la voie : </strong><xsl:apply-templates select="dunumero"/></li>
     
     <xsl:if test="dubis != ''">
     <li><strong>Suffixe : </strong>
     
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

     </li>
     </xsl:if>
     
     <li>
     	<strong>Voie : </strong><xsl:apply-templates select="type-voie" />
				     	<xsl:if test="$hideEspaceTypeVoie=1">
				     		<xsl:text> </xsl:text>
				     	</xsl:if>
     					<xsl:apply-templates select="libelle-voie" />
     </li>
     <xsl:if test="complement1-adresse != ''">
     	<li><strong>complément d'adresse : </strong><xsl:apply-templates select="complement1-adresse" /></li>
     </xsl:if>
     <xsl:if test="complement2-adresse != ''">
     	<li><strong>complément d'adresse : </strong><xsl:apply-templates select="complement2-adresse " /></li>
     </xsl:if>
     <li><strong>Code postal : </strong><xsl:apply-templates select="code-commune" /></li>
     <li><strong>Ville : </strong><xsl:apply-templates select="ville" /></li>
     </ul>
 </xsl:template>


</xsl:stylesheet>