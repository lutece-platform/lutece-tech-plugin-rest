<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="cartereponse">
	<fieldset class="formengine-fieldset">
		   <span class="formengine-field">
				<legend class="formengine-legend">Votre adresse</legend>
				
				<ul>
     				<li>
     					<strong>Numéro de la voie : </strong>
     					<xsl:value-of select="famille2/adresse/dunumero"/>
     				</li>
     				
    				<xsl:if test="string(famille2/adresse/dubis) !=''">
    				<li>
    					<strong>Suffixe : </strong>
		   		
				   		<xsl:variable name="dubistemp">
				   			<xsl:value-of select="famille2/adresse/dubis"/>
				   		</xsl:variable>
				   		<xsl:choose>
						  <xsl:when test="$dubistemp='b'">
						   		<xsl:text>Bis </xsl:text>
						  </xsl:when>
						  <xsl:when test="$dubistemp='t'">
						   		<xsl:text>Ter </xsl:text>
						  </xsl:when>
						  <xsl:when test="$dubistemp='q'">
						   		<xsl:text>Quiquies </xsl:text>
						  </xsl:when>
						  <xsl:otherwise>
						    	<xsl:text></xsl:text>
						  </xsl:otherwise>
						</xsl:choose>
				
				 	</li>
				 	</xsl:if>
				 	
     				<li>
     					<strong>Voie : </strong>
     					<xsl:value-of select="famille2/adresse/libelle-voie"/>
     				</li>
     				<xsl:if test="string(famille2/adresse/complement1-adresse) != ''">
     				<li>
     					<strong>complément d'adresse : </strong>
     					<xsl:value-of select="famille2/adresse/complement1-adresse"/>
     				</li>
     				</xsl:if>
     				
     				<xsl:if test="string(famille2/adresse/complement2-adresse) != ''"> 
     				<li>
     					<strong>complément d'adresse : </strong>
     					<xsl:value-of select="famille2/adresse/complement2-adresse"/>
     				</li>
     				</xsl:if>
     				<li>
     					<strong>Ville : </strong>
     					<xsl:value-of select="famille2/adresse/ville"/>
     				</li>
    
     		</ul>
				
	   	   </span>
   	  
   	   
   	   <form action="jsp/site/plugins/formengine/DoAction.jsp" id="evacaddressSecondFamily" name="evacaddressSecondFamily" enctype="multipart/form-data" method="post" class="formengine-form">
			<input name="page" value="formengine" type="hidden"/>
			<input name="form" value="evac" type="hidden"/>
			<input name="subform" value="addressSecondFamily" type="hidden"/>

			<div class="formengine-steps">
				<input name="action" value="Modifier" class="formengine-submit" type="submit"/>
			</div>
		</form>

   	</fieldset>
</xsl:template>

</xsl:stylesheet>