<?xml version="1.0" encoding="iso-8859-1" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

 <xsl:include href="../../../xsl/formelements.xsl"/>
	
 <xsl:template match="formElements">
	
   <xsl:call-template name="mandatory-notice" />

  
  
  <fieldset class="formengine-fieldset">
   <legend class="formengine-legend">Identité et coordonnées</legend>
    
   		<b>
   		<xsl:apply-templates select="notices/noticeGroup[@name='warning']" mode="no-bullet"/>
   		</b>

   		<xsl:apply-templates select="fields/field[@name='LastName']"/>
   		<xsl:apply-templates select="fields/field[@name='FirstName']"/>
   		
		<div class="formengine-element">
	    		<span class="formengine-label">
					<label for="{fields/field[@name='email']/@name}">
					<span class="formengine-mandatory" >* </span>
						<xsl:value-of select="fields/field[@name='email']/label"/>
						<xsl:text> :</xsl:text>
					</label>
					
				</span>
			<span class="formengine-field">
					<input type="text" name="{fields/field[@name='email']/@name}" id="{fields/field[@name='email']/@name}" value="{fields/field[@name='email']/value}" size="25"/>
			</span>
		</div>
		
		<div class="formengine-element">
	    		<span class="formengine-label">
					<label for="{fields/field[@name='confirmEmail']/@name}">
					<span class="formengine-mandatory" >* </span>
						<xsl:value-of select="fields/field[@name='confirmEmail']/label"/>
						<xsl:text> :</xsl:text>
					</label>
					<xsl:apply-templates select="fields/field[@name='confirmEmail']/additionalInfo"/>
				</span>
				<span class="formengine-field">
						<input type="text" name="{fields/field[@name='confirmEmail']/@name}" id="{fields/field[@name='confirmEmail']/@name}" value="{fields/field[@name='confirmEmail']/value}" size="25"/>
				</span>
		</div>
		
		<div class="formengine-element">
	    		<span class="formengine-label">
					<label for="{fields/field[@name='phone']/@name}">
					<span class="formengine-mandatory" >* </span>
						<xsl:value-of select="fields/field[@name='phone']/label"/>
						<xsl:text> :</xsl:text>
					</label>
					<xsl:apply-templates select="fields/field[@name='phone']/additionalInfo"/>
				</span>

			<span class="formengine-field">
					<input type="text" name="{fields/field[@name='phone']/@name}" id="{fields/field[@name='phone']/@name}" value="{fields/field[@name='phone']/value}" size="10"/>
			</span>
		</div>
		
		<div class="formengine-element">
	    		<span class="formengine-label">
					<label for="{fields/field[@name='NumberChilds']/@name}">
					<span class="formengine-mandatory" >* </span>
						<xsl:value-of select="fields/field[@name='NumberChilds']/label"/>
						<xsl:text> :</xsl:text>
					</label>
					<xsl:apply-templates select="fields/field[@name='NumberChilds']/additionalInfo"/>
				</span>
				
				<span class="formengine-field">
						<input type="text" name="{fields/field[@name='NumberChilds']/@name}" id="{fields/field[@name='NumberChilds']/@name}" value="{fields/field[@name='NumberChilds']/value}" size="3"/>
				</span>
		</div>
		
    </fieldset>

   <div class="formengine-steps">
            <xsl:call-template name="button-list"/>
    </div>
    
    <p><xsl:apply-templates select="notices/noticeGroup[@name='privacy']" mode="no-bullet"/></p>
	

 </xsl:template>
 
</xsl:stylesheet>