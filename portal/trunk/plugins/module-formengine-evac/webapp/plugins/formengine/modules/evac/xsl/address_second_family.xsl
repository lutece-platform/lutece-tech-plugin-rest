<?xml version="1.0" encoding="iso-8859-1" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

 <xsl:include href="../../../xsl/formelements.xsl"/>
	
	<xsl:param name="hideButtonPrevious"/>
	<xsl:param name="hideSearchAddress"/>
	<xsl:param name="hideChoiceAddress"/>
	<xsl:param name="hideAddressNotFound"/>
	
 <xsl:template match="formElements">
	
	<xsl:if test="$hideChoiceAddress=0">
			<xsl:call-template name="mandatory-notice" />
		
		<fieldset class="formengine-fieldset">		
		<legend class="formengine-legend">Résultats</legend>
 			<xsl:apply-templates select="fields/field[@name='address']"/>
 			<div class="formengine-steps">
				<xsl:apply-templates select="buttons/button[@name='Valider']"/>
			</div>
		</fieldset>
		
		<fieldset class="formengine-fieldset">	
		<legend class="formengine-legend">Information</legend>	
 			<xsl:apply-templates select="notices/noticeGroup[@name='informationChoix']" mode="no-bullet"/>
		</fieldset>
		
	  </xsl:if>
	
	<xsl:if test="$hideSearchAddress=0">
   <xsl:call-template name="mandatory-notice" />

  
  <fieldset class="formengine-fieldset">
   <legend class="formengine-legend">Adresse 2de famille</legend>
    
   		<b>
   		<xsl:apply-templates select="notices/noticeGroup[@name='warning']" mode="no-bullet"/>
   		</b>
    
   		<div class="formengine-element">
	    		<span class="formengine-label">
					<label for="{fields/field[@name='numVoie']/@name}">
					<span class="formengine-mandatory" >* </span>
						<xsl:value-of select="fields/field[@name='numVoie']/label"/>
						<xsl:text> :</xsl:text>
					</label>
					<xsl:apply-templates select="fields/field[@name='numVoie']/additionalInfo"/>
				</span>
			<span class="formengine-field">
					<input type="text" name="{fields/field[@name='numVoie']/@name}" id="{fields/field[@name='numVoie']/@name}" value="{fields/field[@name='numVoie']/value}" size="4"/>
			</span>
			
		</div>
		
		<xsl:apply-templates select="fields/field[@name='suffixe']"/>
		
		<div class="formengine-element">
	    		<span class="formengine-label">
					<label for="{fields/field[@name='nomVoie']/@name}">
					<span class="formengine-mandatory" >* </span>
						<xsl:value-of select="fields/field[@name='nomVoie']/label"/>
						<xsl:text> :</xsl:text>
					</label>
					<xsl:apply-templates select="fields/field[@name='nomVoie']/additionalInfo"/>
				</span>
			<span class="formengine-field">
					<input type="text" name="{fields/field[@name='nomVoie']/@name}" id="{fields/field[@name='nomVoie']/@name}" value="{fields/field[@name='nomVoie']/value}" size="30"/>
			</span>
			
		</div>


   		<div class="formengine-element">
	    		<span class="formengine-label">
					<label for="{fields/field[@name='complAddress']/@name}">
						<xsl:value-of select="fields/field[@name='complAddress']/label"/>
						<xsl:text> :</xsl:text>
					</label>
					<xsl:apply-templates select="fields/field[@name='complAddress']/additionalInfo"/>
				</span>
	   			<span class="formengine-field">
	   					<input type="text" name="{fields/field[@name='complAddress']/@name}" id="{fields/field[@name='complAddress']/@name}" value="{fields/field[@name='complAddress']/value}" size="25"/>
				</span>
		</div>
		<div class="formengine-element">
				<span class="formengine-label">
					<label for="{fields/field[@name='complAddress2']/@name}">
						<xsl:value-of select="fields/field[@name='complAddress2']/label"/>
						<xsl:text> :</xsl:text>
					</label>
					<xsl:apply-templates select="fields/field[@name='complAddress2']/additionalInfo"/>
				</span>
				<span class="formengine-field">
	   					<input type="text" name="{fields/field[@name='complAddress2']/@name}" id="{fields/field[@name='complAddress2']/@name}" value="{fields/field[@name='complAddress2']/value}" size="25"/>
				</span>
		</div>
		
		<p><center><xsl:apply-templates select="notices/noticeGroup[@name='ville']" mode="no-bullet"/></center></p>
		<br/>
		<div class="formengine-steps">
		<xsl:apply-templates select="buttons/button[@name='Rechercher']"/>
		</div>
    </fieldset>
	
	</xsl:if>
   <div class="formengine-steps">
   	<xsl:if test="$hideButtonPrevious=0">
     		<xsl:apply-templates select="buttons/button[@name='Etape précédente']"/>
   	 </xsl:if>
       
   </div>
   
    <xsl:if test="$hideButtonPrevious=0">
		<fieldset class="formengine-fieldset">	
		<legend class="formengine-legend">Information</legend>	
 			<xsl:apply-templates select="notices/noticeGroup[@name='information']" mode="no-bullet"/>
		</fieldset>
	</xsl:if>
	
    <p><xsl:apply-templates select="notices/noticeGroup[@name='privacy']" mode="no-bullet"/></p>
	

	
 </xsl:template>
 
 
 
</xsl:stylesheet>